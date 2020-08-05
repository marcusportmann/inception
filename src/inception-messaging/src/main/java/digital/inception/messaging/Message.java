/*
 * Copyright 2020 Marcus Portmann
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package digital.inception.messaging;

// ~--- non-JDK imports --------------------------------------------------------

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.github.f4b6a3.uuid.UuidCreator;
import digital.inception.core.util.ISO8601Util;
import digital.inception.core.wbxml.Document;
import digital.inception.core.wbxml.Element;
import digital.inception.core.wbxml.Encoder;
import digital.inception.core.xml.LocalDateTimeAdapter;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import org.springframework.util.StringUtils;

// ~--- JDK imports ------------------------------------------------------------

/**
 * The <code>Message</code> class holds the information for a message.
 *
 * <p>It provides facilities to convert to and from the WBXML representation of the message.
 *
 * @author Marcus Portmann
 */
@Schema(description = "Message")
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "id",
    "username",
    "deviceId",
    "typeId",
    "correlationId",
    "priority",
    "status",
    "created",
    "sendAttempts",
    "processAttempts",
    "downloadAttempts",
    "lastProcessed",
    "lockName",
    "encryptionIV",
    "dataHash"
})
@XmlRootElement(name = "Message", namespace = "http://messaging.inception.digital")
@XmlType(
    name = "Message",
    namespace = "http://messaging.inception.digital",
    propOrder = {
        "id",
        "username",
        "deviceId",
        "typeId",
        "correlationId",
        "priority",
        "status",
        "created",
        "sendAttempts",
        "processAttempts",
        "downloadAttempts",
        "lastProcessed",
        "lockName",
        "encryptionIV",
        "dataHash"
    })
@XmlAccessorType(XmlAccessType.FIELD)
@Entity
@Table(schema = "messaging", name = "messages")
@SuppressWarnings({"WeakerAccess"})
public class Message {

  /**
   * The maximum size of an asynchronous message in bytes. Messages larger than this size will be
   * split into a number of message parts.
   */
  public static final int MAX_ASYNC_MESSAGE_SIZE = 40000;

  /**
   * The optional Universally Unique Identifier (UUID) used to correlate the message.
   */
  @Schema(
      description =
          "The optional Universally Unique Identifier (UUID) used to correlate the message")
  @JsonProperty
  @XmlElement(name = "CorrelationId")
  @Column(name = "correlation_id")
  private UUID correlationId;

  /**
   * The date and time the message was created.
   */
  @Schema(description = "The date and time the message was created", required = true)
  @JsonProperty(required = true)
  @XmlElement(name = "Created", required = true)
  @XmlJavaTypeAdapter(LocalDateTimeAdapter.class)
  @XmlSchemaType(name = "dateTime")
  @NotNull
  @Column(name = "created", nullable = false)
  private LocalDateTime created;

  /**
   * The data for the message.
   */
  @Schema(description = "The data for the message", required = true)
  @JsonProperty(required = true)
  @XmlElement(name = "Data", required = true)
  @NotNull
  @Column(name = "data", nullable = false)
  private byte[] data;

  /**
   * The hash of the unencrypted data for the message if the message is encrypted.
   *
   * <p>If the message data is encrypted then this will be the hash of the unencrypted message data
   * and will be used to verify the message data has been decrypted successfully.
   */
  @Schema(
      description = "The hash of the unencrypted data for the message if the message is encrypted")
  @JsonProperty
  @XmlElement(name = "DataHash")
  @Size(max = 100)
  @Column(name = "data_hash", length = 100)
  private String dataHash;

  /**
   * The Universally Unique Identifier (UUID) uniquely identifying the device the message originated
   * from.
   */
  @Schema(
      description =
          "The Universally Unique Identifier (UUID) uniquely identifying the device the message "
              + "originated from",
      required = true)
  @JsonProperty(required = true)
  @XmlElement(name = "DeviceId", required = true)
  @NotNull
  @Column(name = "device_id", nullable = false)
  private UUID deviceId;

  /**
   * The number of times that downloading of the message was attempted.
   */
  @Schema(description = "The number of times that downloading of the message was attempted")
  @JsonProperty
  @XmlElement(name = "DownloadAttempts")
  @Column(name = "download_attempts")
  private Integer downloadAttempts;

  /**
   * The base-64 encoded initialization vector for the encryption scheme for the message.
   */
  @Schema(
      description =
          "The base-64 encoded initialization vector for the encryption scheme for the message")
  @JsonProperty
  @XmlElement(name = "EncryptionIV")
  @Size(min = 1, max = 100)
  @Column(name = "encryption_iv", length = 100)
  private String encryptionIV;

  /**
   * The Universally Unique Identifier (UUID) uniquely identifying the message.
   */
  @Schema(
      description = "The Universally Unique Identifier (UUID) uniquely identifying the message",
      required = true)
  @JsonProperty(required = true)
  @XmlElement(name = "Id", required = true)
  @NotNull
  @Id
  @Column(name = "id", nullable = false)
  private UUID id;

  /**
   * The date and time the last attempt was made to process the message.
   */
  @Schema(description = "The date and time the last attempt was made to process the message")
  @JsonProperty
  @XmlElement(name = "LastProcessed")
  @XmlJavaTypeAdapter(LocalDateTimeAdapter.class)
  @XmlSchemaType(name = "dateTime")
  @Column(name = "last_processed")
  private LocalDateTime lastProcessed;

  /**
   * The name of the entity that has locked this message for processing.
   */
  @Schema(description = "The name of the entity that has locked this message for processing")
  @JsonProperty
  @XmlElement(name = "LockName")
  @Size(min = 1, max = 100)
  @Column(name = "lock_name", length = 100)
  private String lockName;

  /**
   * The message priority.
   *
   * <p>Messages with a higher priority value are processed before messages with a lower priority
   * value.
   */
  @Schema(description = "The message priority", required = true)
  @JsonProperty(required = true)
  @XmlElement(name = "Priority", required = true)
  @NotNull
  @Column(name = "priority", nullable = false)
  private MessagePriority priority;

  /**
   * The number of times that the processing of the message was attempted.
   */
  @Schema(description = "The number of times that the processing of the message was attempted")
  @JsonProperty
  @XmlElement(name = "ProcessAttempts")
  @Column(name = "process_attempts")
  private Integer processAttempts;

  /**
   * The number of times that the sending of the message was attempted.
   */
  @Schema(description = "The number of times that the sending of the message was attempted")
  @JsonProperty
  @XmlElement(name = "SendAttempts")
  @Column(name = "send_attempts")
  private Integer sendAttempts;

  /**
   * The message status e.g. Initialized, Sending, etc.
   */
  @Schema(description = "The message status")
  @JsonProperty(required = true)
  @XmlElement(name = "Status", required = true)
  @NotNull
  @Column(name = "status", nullable = false)
  private MessageStatus status;

  /**
   * The Universally Unique Identifier (UUID) uniquely identifying the type of message.
   */
  @Schema(
      description =
          "The Universally Unique Identifier (UUID) uniquely identifying the type of message",
      required = true)
  @JsonProperty(required = true)
  @XmlElement(name = "TypeId", required = true)
  @NotNull
  @Column(name = "type_id", nullable = false)
  private UUID typeId;

  /**
   * The username identifying the user associated with the message.
   */
  @Schema(
      description = "The username identifying the user associated with the message",
      required = true)
  @JsonProperty(required = true)
  @XmlElement(name = "Username", required = true)
  @NotNull
  @Size(min = 1, max = 100)
  @Column(name = "username", nullable = false, length = 100)
  private String username;

  /**
   * Constructs a new <code>Message</code>.
   */
  public Message() {
  }

  /**
   * Constructs a new <code>Message</code> and populates it from the message information stored in
   * the specified WBXML document.
   *
   * @param document the WBXML document containing the message information
   */
  public Message(Document document) throws MessagingServiceException {
    Element rootElement = document.getRootElement();

    this.id = UUID.fromString(rootElement.getAttributeValue("id"));
    this.username = rootElement.getAttributeValue("username");
    this.deviceId = UUID.fromString(rootElement.getAttributeValue("deviceId"));
    this.typeId = UUID.fromString(rootElement.getAttributeValue("typeId"));

    if (rootElement.hasAttribute("correlationId")) {
      this.correlationId = UUID.fromString(rootElement.getAttributeValue("correlationId"));
    }

    this.priority =
        MessagePriority.fromCode(Integer.parseInt(rootElement.getAttributeValue("priority")));
    this.data = rootElement.getOpaque();

    if (rootElement.hasAttribute("dataHash")) {
      this.dataHash = rootElement.getAttributeValue("dataHash");
    }

    if (rootElement.hasAttribute("encryptionIV")) {
      this.encryptionIV = rootElement.getAttributeValue("encryptionIV");
    }

    String createdAttributeValue = rootElement.getAttributeValue("created");

    try {
      this.created = ISO8601Util.toLocalDateTime(createdAttributeValue);
    } catch (Throwable e) {
      throw new MessagingServiceException(
          String.format(
              "Failed to parse the created ISO8601 timestamp (%s) for the message (%s)",
              createdAttributeValue, id),
          e);
    }

    if (rootElement.hasAttribute("sendAttempts")) {
      this.sendAttempts = Integer.parseInt(rootElement.getAttributeValue("sendAttempts"));
    }

    if (rootElement.hasAttribute("processAttempts")) {
      this.processAttempts = Integer.parseInt(rootElement.getAttributeValue("processAttempts"));
    }

    if (rootElement.hasAttribute("downloadAttempts")) {
      this.downloadAttempts = Integer.parseInt(rootElement.getAttributeValue("downloadAttempts"));
    }

    this.status = MessageStatus.INITIALIZED;
  }

  /**
   * Constructs a new <code>Message</code>.
   *
   * @param username the username identifying the user associated with the message
   * @param deviceId the Universally Unique Identifier (UUID) uniquely identifying the device the
   *                 message originated from
   * @param typeId   the Universally Unique Identifier (UUID) uniquely identifying the type of
   *                 message
   * @param priority the message priority
   * @param data     the data for the message which is NOT encrypted
   */
  public Message(
      String username, UUID deviceId, UUID typeId, MessagePriority priority, byte[] data) {
    this.id = UuidCreator.getShortPrefixComb();
    this.username = username;
    this.deviceId = deviceId;
    this.typeId = typeId;
    this.priority = priority;
    this.data = data;
    this.created = LocalDateTime.now();
    this.status = MessageStatus.INITIALIZED;
  }

  /**
   * Constructs a new <code>Message</code>.
   *
   * @param username      the username identifying the user associated with the message
   * @param deviceId      the Universally Unique Identifier (UUID) uniquely identifying the device
   *                      the message originated from
   * @param typeId        the Universally Unique Identifier (UUID) uniquely identifying the type of
   *                      message
   * @param correlationId the Universally Unique Identifier (UUID) used to correlate the message
   * @param priority      the message priority
   * @param data          the data for the message which is NOT encrypted
   */
  public Message(
      String username,
      UUID deviceId,
      UUID typeId,
      UUID correlationId,
      MessagePriority priority,
      byte[] data) {
    this.id = UuidCreator.getShortPrefixComb();
    this.username = username;
    this.deviceId = deviceId;
    this.typeId = typeId;
    this.correlationId = correlationId;
    this.priority = priority;
    this.data = data;
    this.created = LocalDateTime.now();
    this.status = MessageStatus.INITIALIZED;
  }

  /**
   * Constructs a new <code>Message</code>.
   *
   * @param username     the username identifying the user associated with the message
   * @param deviceId     the Universally Unique Identifier (UUID) uniquely identifying the device
   *                     the message originated from
   * @param typeId       the Universally Unique Identifier (UUID) uniquely identifying the type of
   *                     message
   * @param priority     the message priority
   * @param data         the data for the message which may be encrypted
   * @param dataHash     the hash of the unencrypted data for the message if the message is
   *                     encrypted
   * @param encryptionIV the base-64 encoded initialization vector for the encryption scheme
   */
  public Message(
      String username,
      UUID deviceId,
      UUID typeId,
      MessagePriority priority,
      byte[] data,
      String dataHash,
      String encryptionIV) {
    this.id = UuidCreator.getShortPrefixComb();
    this.username = username;
    this.deviceId = deviceId;
    this.typeId = typeId;
    this.priority = priority;
    this.data = data;
    this.dataHash = dataHash;

    if (StringUtils.isEmpty(dataHash.length())) {
      throw new RuntimeException(
          "Unable to initialize a message with encrypted data using a blank data hash");
    }

    this.encryptionIV = encryptionIV;
    this.created = LocalDateTime.now();
    this.status = MessageStatus.INITIALIZED;
  }

  /**
   * Constructs a new <code>Message</code>.
   *
   * @param username      the username identifying the user associated with the message
   * @param deviceId      the Universally Unique Identifier (UUID) uniquely identifying the device
   *                      the message originated from
   * @param typeId        the Universally Unique Identifier (UUID) uniquely identifying the type of
   *                      message
   * @param correlationId the Universally Unique Identifier (UUID) used to correlate the message
   * @param priority      the message priority
   * @param data          the data for the message which may be encrypted
   * @param dataHash      the hash of the unencrypted data for the message if the message is
   *                      encrypted
   * @param encryptionIV  the base-64 encoded initialization vector for the encryption scheme
   */
  public Message(
      String username,
      UUID deviceId,
      UUID typeId,
      UUID correlationId,
      MessagePriority priority,
      byte[] data,
      String dataHash,
      String encryptionIV) {
    this.id = UuidCreator.getShortPrefixComb();
    this.username = username;
    this.deviceId = deviceId;
    this.typeId = typeId;
    this.correlationId = correlationId;
    this.priority = priority;
    this.data = data;
    this.dataHash = dataHash;

    if (StringUtils.isEmpty(dataHash.length())) {
      throw new RuntimeException(
          "Unable to initialize a message with encrypted data using a blank data hash");
    }

    this.encryptionIV = encryptionIV;
    this.created = LocalDateTime.now();
    this.status = MessageStatus.INITIALIZED;
  }

  /**
   * Constructs a new <code>Message</code>.
   *
   * @param id            the Universally Unique Identifier (UUID) uniquely identifying the message
   * @param username      the username identifying the user associated with the message
   * @param deviceId      the Universally Unique Identifier (UUID) uniquely identifying the device
   *                      the message originated from
   * @param typeId        the Universally Unique Identifier (UUID) uniquely identifying the type of
   *                      message
   * @param correlationId the Universally Unique Identifier (UUID) used to correlate the message
   * @param priority      the message priority
   * @param created       the date and time the message was created
   * @param data          the data for the message which may be encrypted
   * @param dataHash      the hash of the unencrypted data for the message if the message is
   *                      encrypted
   * @param encryptionIV  the base-64 encoded initialization vector for the encryption scheme
   */
  public Message(
      UUID id,
      String username,
      UUID deviceId,
      UUID typeId,
      UUID correlationId,
      MessagePriority priority,
      LocalDateTime created,
      byte[] data,
      String dataHash,
      String encryptionIV) {
    this.id = id;
    this.username = username;
    this.deviceId = deviceId;
    this.typeId = typeId;
    this.correlationId = correlationId;
    this.priority = priority;
    this.status = MessageStatus.INITIALIZED;
    this.created = created;
    this.data = data;
    this.dataHash = dataHash;
    this.encryptionIV = encryptionIV;
  }

  /**
   * Returns <code>true</code> if the WBXML document contains valid message information or <code>
   * false</code> otherwise.
   *
   * @param document the WBXML document to validate
   *
   * @return <code>true</code> if the WBXML document contains valid message information or <code>
   * false</code> otherwise
   */
  public static boolean isValidWBXML(Document document) {
    Element rootElement = document.getRootElement();

    return rootElement.getName().equals("Message")
        && !((!rootElement.hasAttribute("id"))
        || (!rootElement.hasAttribute("username"))
        || (!rootElement.hasAttribute("deviceId"))
        || (!rootElement.hasAttribute("priority"))
        || (!rootElement.hasAttribute("typeId"))
        || (!rootElement.hasAttribute("created")));
  }

  /**
   * Indicates whether some other object is "equal to" this one.
   *
   * @param object the reference object with which to compare
   *
   * @return <code>true</code> if this object is the same as the object argument otherwise <code>
   * false</code>
   */
  @Override
  public boolean equals(Object object) {
    if (this == object) {
      return true;
    }

    if (object == null) {
      return false;
    }

    if (getClass() != object.getClass()) {
      return false;
    }

    Message other = (Message) object;

    return (id != null) && id.equals(other.id);
  }

  /**
   * Returns the Universally Unique Identifier (UUID) used to correlate the message.
   *
   * @return the Universally Unique Identifier (UUID) used to correlate the message
   */
  public UUID getCorrelationId() {
    return correlationId;
  }

  /**
   * Set the Universally Unique Identifier (UUID) used to correlate the message.
   *
   * @param correlationId the Universally Unique Identifier (UUID) used to correlate the message
   */
  public void setCorrelationId(UUID correlationId) {
    this.correlationId = correlationId;
  }

  /**
   * Returns the date and time the message was created.
   *
   * @return the date and time the message was created
   */
  public LocalDateTime getCreated() {
    return created;
  }

  /**
   * Set the date and time the message was created.
   *
   * @param created the date and time the message was created
   */
  public void setCreated(LocalDateTime created) {
    this.created = created;
  }

  /**
   * Returns the data for the message which may be encrypted.
   *
   * @return the data for the message which may be encrypted
   */
  public byte[] getData() {
    return data;
  }

  /**
   * Set the data for the message which may be encrypted.
   *
   * @param data the data for the message which may be encrypted
   */
  public void setData(byte[] data) {
    this.data = data;
  }

  /**
   * Returns the hash of the unencrypted data for the message if the message is encrypted.
   *
   * @return the hash of the unencrypted data for the message if the message is encrypted
   */
  public String getDataHash() {
    return dataHash;
  }

  /**
   * Set the hash of the unencrypted data for the message if the message is encrypted.
   *
   * @param dataHash the hash of the unencrypted data for the message if the message is encrypted
   */
  public void setDataHash(String dataHash) {
    this.dataHash = dataHash;
  }

  /**
   * The Universally Unique Identifier (UUID) uniquely identifying the device the message originated
   * from.
   *
   * @return the Universally Unique Identifier (UUID) uniquely identifying the device the message
   * originated from
   */
  public UUID getDeviceId() {
    return deviceId;
  }

  /**
   * Set the Universally Unique Identifier (UUID) uniquely identifying the device the message
   * originated from.
   *
   * @param deviceId the Universally Unique Identifier (UUID) uniquely identifying the device the
   *                 message originated from
   */
  public void setDeviceId(UUID deviceId) {
    this.deviceId = deviceId;
  }

  /**
   * Returns the number of times that downloading of the message was attempted.
   *
   * @return the number of times that downloading of the message was attempted
   */
  public Integer getDownloadAttempts() {
    return downloadAttempts;
  }

  /**
   * Set the number of times that downloading of the message was attempted.
   *
   * @param downloadAttempts the number of times that downloading of the message was attempted
   */
  public void setDownloadAttempts(Integer downloadAttempts) {
    this.downloadAttempts = downloadAttempts;
  }

  /**
   * Returns the base-64 encoded initialization vector for the encryption scheme for the message.
   *
   * @return the base-64 encoded initialization vector for the encryption scheme for the message
   */
  public String getEncryptionIV() {
    return encryptionIV;
  }

  /**
   * Set the base-64 encoded initialization vector for the encryption scheme for the message.
   *
   * @param encryptionIV the base-64 encoded initialization vector for the encryption scheme for the
   *                     message
   */
  public void setEncryptionIV(String encryptionIV) {
    this.encryptionIV = encryptionIV;
  }

  /**
   * Returns the Universally Unique Identifier (UUID) uniquely identifying the message.
   *
   * @return the Universally Unique Identifier (UUID) uniquely identifying the message
   */
  public UUID getId() {
    return id;
  }

  /**
   * Set the Universally Unique Identifier (UUID) uniquely identifying the message.
   *
   * @param id the Universally Unique Identifier (UUID) uniquely identifying the message
   */
  public void setId(UUID id) {
    this.id = id;
  }

  /**
   * Returns the date and time the last attempt was made to process the message.
   *
   * @return the date and time the last attempt was made to process the message
   */
  public LocalDateTime getLastProcessed() {
    return lastProcessed;
  }

  /**
   * Set the date and time the last attempt was made to process the message.
   *
   * @param lastProcessed the date and time the last attempt was made to process the message
   */
  public void setLastProcessed(LocalDateTime lastProcessed) {
    this.lastProcessed = lastProcessed;
  }

  /**
   * Returns the name of the entity that has locked this message for processing or <code>null</code>
   * if the message is not being processed.
   *
   * @return the name of the entity that has locked this message for processing or <code>null</code>
   * if the message is not being processed
   */
  public String getLockName() {
    return lockName;
  }

  /**
   * Set the name of the entity that has locked this message for processing or <code>null</code> if
   * the message is not being processed.
   *
   * @param lockName the name of the entity that has locked this message for processing or <code>
   *                 null</code> if the message is not being processed
   */
  public void setLockName(String lockName) {
    this.lockName = lockName;
  }

  /**
   * Returns the message priority.
   *
   * <p>"Out of Order" processing is usually applied to messages so that messages with a higher
   * priority value are processed before messages with a lower priority value.
   *
   * @return the message priority
   */
  public MessagePriority getPriority() {
    return priority;
  }

  /**
   * Set the message priority. Messages with a higher priority value are processed before messages
   * with a lower priority value.
   *
   * @param priority the message priority. Messages with a higher priority value are processed
   *                 before messages with a lower priority value
   */
  public void setPriority(MessagePriority priority) {
    this.priority = priority;
  }

  /**
   * Returns the number of times that the processing of the message was attempted.
   *
   * @return the number of times that the processing of the message was attempted
   */
  public Integer getProcessAttempts() {
    return processAttempts;
  }

  /**
   * Set the number of times that the processing of the message was attempted.
   *
   * @param processAttempts the number of times that the processing of the message was attempted
   */
  public void setProcessAttempts(Integer processAttempts) {
    this.processAttempts = processAttempts;
  }

  /**
   * Returns the number of times that the sending of the message was attempted.
   *
   * @return the number of times that the sending of the message was attempted
   */
  public Integer getSendAttempts() {
    return sendAttempts;
  }

  /**
   * Set the number of times that the sending of the message was attempted.
   *
   * @param sendAttempts the number of times that the sending of the message was attempted
   */
  public void setSendAttempts(Integer sendAttempts) {
    this.sendAttempts = sendAttempts;
  }

  /**
   * Returns the message status e.g. Initialized, Sending, etc.
   *
   * @return the message status e.g. Initialized, Sending, etc
   */
  public MessageStatus getStatus() {
    return status;
  }

  /**
   * Set the message status e.g. Initialized, Sending, etc.
   *
   * @param status the message status e.g. Initialized, Sending, etc
   */
  public void setStatus(MessageStatus status) {
    this.status = status;
  }

  /**
   * Returns the Universally Unique Identifier (UUID) uniquely identifying the type of message.
   *
   * @return the Universally Unique Identifier (UUID) uniquely identifying the type of message
   */
  public UUID getTypeId() {
    return typeId;
  }

  /**
   * Set the Universally Unique Identifier (UUID) uniquely identifying the type of message.
   *
   * @param typeId the Universally Unique Identifier (UUID) uniquely identifying the type of
   *               message
   */
  public void setTypeId(UUID typeId) {
    this.typeId = typeId;
  }

  /**
   * Returns the username identifying the user associated with the message.
   *
   * @return the username identifying the user associated with the message
   */
  public String getUsername() {
    return username;
  }

  /**
   * Set the username identifying the user associated with the message.
   *
   * @param username the username identifying the user associated with the message
   */
  public void setUsername(String username) {
    this.username = username;
  }

  /**
   * Returns a hash code value for the object.
   *
   * @return a hash code value for the object
   */
  @Override
  public int hashCode() {
    return (id == null) ? 0 : id.hashCode();
  }

  /**
   * Increment the download attempts.
   */
  public void incrementDownloadAttempts() {
    if (downloadAttempts == null) {
      downloadAttempts = 1;
    } else {
      downloadAttempts++;
    }
  }

  /**
   * Increment the processing attempts.
   */
  public void incrementProcessAttempts() {
    if (processAttempts == null) {
      processAttempts = 1;
    } else {
      processAttempts++;
    }
  }

  /**
   * Returns <code>true</code> if the data for the message is encrypted or <code>false</code>
   * otherwise.
   *
   * @return <code>true</code> if the data for the message is encrypted or <code>false</code>
   * otherwise
   */
  public boolean isEncrypted() {
    return (!StringUtils.isEmpty(dataHash));
  }

  /**
   * Returns the String representation of the message.
   *
   * @return the String representation of the message
   */
  @Override
  public String toString() {
    StringBuilder buffer = new StringBuilder("<Message");

    buffer.append(" id=\"").append(id).append("\"");
    buffer.append(" username=\"").append(username).append("\"");
    buffer.append(" deviceId=\"").append(deviceId).append("\"");
    buffer.append(" typeId=\"").append(typeId).append("\"");

    buffer
        .append(" correlationId=\"")
        .append((correlationId != null) ? correlationId.toString() : "")
        .append("\"");
    buffer.append(" priority=\"").append(priority).append("\"");
    buffer.append(" status=\"").append(status).append("\"");
    buffer.append(" created=\"").append(ISO8601Util.fromLocalDateTime(created)).append("\"");

    if (sendAttempts != null) {
      buffer.append(" sendAttempts=\"").append(sendAttempts).append("\"");
    }

    if (processAttempts != null) {
      buffer.append(" processAttempts=\"").append(processAttempts).append("\"");
    }

    if (downloadAttempts != null) {
      buffer.append(" downloadAttempts=\"").append(downloadAttempts).append("\"");
    }

    if (lockName != null) {
      buffer.append(" lockName=\"").append(lockName).append("\"");
    } else {
      buffer.append(" lockName=\"None\"");
    }

    if (lastProcessed != null) {
      buffer
          .append(" lastProcessed=\"")
          .append(ISO8601Util.fromLocalDateTime(lastProcessed))
          .append("\"");
    } else {
      buffer.append(" lastProcessed=\"Never\"");
    }

    buffer.append(" dataHash=\"").append((dataHash != null) ? dataHash : "").append("\"");
    buffer
        .append(" encryptionIV=\"")
        .append((encryptionIV != null) ? encryptionIV : "")
        .append("\"");

    if (isEncrypted()) {
      buffer.append(">").append(data.length).append(" bytes of opaque encrypted data</Message>");
    } else {
      buffer.append(">").append(data.length).append(" bytes of opaque data</Message>");
    }

    return buffer.toString();
  }

  /**
   * Returns the WBXML representation of the message.
   *
   * @return the WBXML representation of the message
   */
  public byte[] toWBXML() {
    Element rootElement = new Element("Message");

    rootElement.setAttribute("id", id.toString());
    rootElement.setAttribute("username", username);
    rootElement.setAttribute("deviceId", deviceId.toString());
    rootElement.setAttribute("typeId", typeId.toString());

    if (correlationId != null) {
      rootElement.setAttribute("correlationId", correlationId.toString());
    }

    rootElement.setAttribute("priority", Integer.toString(priority.code()));
    rootElement.setAttribute("created", ISO8601Util.fromLocalDateTime(created));

    if (sendAttempts != null) {
      rootElement.setAttribute("sendAttempts", Integer.toString(sendAttempts));
    }

    if (processAttempts != null) {
      rootElement.setAttribute("processAttempts", Integer.toString(processAttempts));
    }

    if (downloadAttempts != null) {
      rootElement.setAttribute("downloadAttempts", Integer.toString(downloadAttempts));
    }

    if (!StringUtils.isEmpty(dataHash)) {
      rootElement.setAttribute("dataHash", dataHash);
    } else {
      rootElement.setAttribute("dataHash", "");
    }

    if (encryptionIV != null) {
      rootElement.setAttribute("encryptionIV", encryptionIV);
    }

    rootElement.addContent(data);

    Encoder encoder = new Encoder(new Document(rootElement));

    return encoder.getData();
  }
}
