/*
 * Copyright 2021 Marcus Portmann
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
import java.util.Objects;
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

/**
 * The <b>MessagePart</b> class holds the information for a message part.
 *
 * @author Marcus Portmann
 */
@Schema(description = "A message part")
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
  "id",
  "partNo",
  "totalParts",
  "status",
  "sendAttempts",
  "downloadAttempts",
  "messageId",
  "messageUsername",
  "messageDeviceId",
  "messageTypeId",
  "messageCorrelationId",
  "messagePriority",
  "messageCreated",
  "messageDataHash",
  "messageEncryptionIV",
  "messageChecksum",
  "lockName",
  "data"
})
@XmlRootElement(name = "MessagePart", namespace = "http://messaging.inception.digital")
@XmlType(
    name = "MessagePart",
    namespace = "http://messaging.inception.digital",
    propOrder = {
      "id",
      "partNo",
      "totalParts",
      "status",
      "sendAttempts",
      "downloadAttempts",
      "messageId",
      "messageUsername",
      "messageDeviceId",
      "messageTypeId",
      "messageCorrelationId",
      "messagePriority",
      "messageCreated",
      "messageDataHash",
      "messageEncryptionIV",
      "messageChecksum",
      "lockName",
      "data"
    })
@XmlAccessorType(XmlAccessType.FIELD)
@Entity
@Table(schema = "messaging", name = "message_parts")
@SuppressWarnings({"WeakerAccess"})
public class MessagePart {

  /** The maximum size of a message part in bytes. */
  public static final int MAX_MESSAGE_PART_SIZE = 40000;

  /** The binary data for the message part. */
  @Schema(description = "The data for the message part", required = true)
  @JsonProperty(required = true)
  @XmlElement(name = "Data", required = true)
  @NotNull
  @Column(name = "data", nullable = false)
  private byte[] data;

  /** The number of times that downloading of the message part was attempted. */
  @Schema(description = "The number of times that downloading of the message part was attempted")
  @JsonProperty
  @XmlElement(name = "DownloadAttempts")
  @Column(name = "download_attempts")
  private Integer downloadAttempts;

  /** The Universally Unique Identifier (UUID) for the message part. */
  @Schema(
      description = "The Universally Unique Identifier (UUID) for the message part",
      required = true)
  @JsonProperty(required = true)
  @XmlElement(name = "Id", required = true)
  @NotNull
  @Id
  @Column(name = "id", nullable = false)
  private UUID id;

  /** The name of the entity that has locked the message part for processing. */
  @Schema(description = "The name of the entity that has locked the message part for processing")
  @JsonProperty
  @XmlElement(name = "LockName")
  @Size(min = 1, max = 100)
  @Column(name = "lock_name", length = 100)
  private String lockName;

  /** The checksum for the original message. */
  @Schema(description = "The checksum for the original message", required = true)
  @JsonProperty(required = true)
  @XmlElement(name = "MessageChecksum", required = true)
  @NotNull
  @Size(min = 1, max = 100)
  @Column(name = "message_checksum", length = 100, nullable = false)
  private String messageChecksum;

  /** The optional Universally Unique Identifier (UUID) used to correlate the original message. */
  @Schema(
      description =
          "The optional Universally Unique Identifier (UUID) used to correlate the original message")
  @JsonProperty
  @XmlElement(name = "MessageCorrelationId")
  @Column(name = "message_correlation_id")
  private UUID messageCorrelationId;

  /** The date and time the original message was created. */
  @Schema(description = "The date and time the original message was created", required = true)
  @JsonProperty(required = true)
  @XmlElement(name = "MessageCreated", required = true)
  @XmlJavaTypeAdapter(LocalDateTimeAdapter.class)
  @XmlSchemaType(name = "dateTime")
  @NotNull
  @Column(name = "message_created", nullable = false)
  private LocalDateTime messageCreated;

  /** The hash of the unencrypted data for the original message if the message was encrypted. */
  @Schema(
      description =
          "The hash of the unencrypted data for the original message if the message was encrypted")
  @JsonProperty
  @XmlElement(name = "MessageDataHash")
  @Size(min = 1, max = 100)
  @Column(name = "message_data_hash", length = 100)
  private String messageDataHash;

  /**
   * The Universally Unique Identifier (UUID) for the device the original message originated from.
   */
  @Schema(
      description =
          "The Universally Unique Identifier (UUID) for the device the original "
              + "message originated from",
      required = true)
  @JsonProperty(required = true)
  @XmlElement(name = "MessageDeviceId", required = true)
  @NotNull
  @Column(name = "message_device_id", nullable = false)
  private UUID messageDeviceId;

  /**
   * The base-64 encoded initialization vector for the encryption scheme for the original message.
   */
  @Schema(
      description =
          "The base-64 encoded initialization vector for the encryption scheme for the original "
              + "message")
  @JsonProperty
  @XmlElement(name = "MessageEncryptionIV")
  @Size(min = 1, max = 100)
  @Column(name = "message_encryption_iv", length = 100)
  private String messageEncryptionIV;

  /** The Universally Unique Identifier (UUID) for the original message. */
  @Schema(
      description = "The Universally Unique Identifier (UUID) for the original message",
      required = true)
  @JsonProperty(required = true)
  @XmlElement(name = "MessageId", required = true)
  @NotNull
  @Column(name = "message_id", nullable = false)
  private UUID messageId;

  /** The priority for the original message. */
  @Schema(description = "The priority for the original message", required = true)
  @JsonProperty(required = true)
  @XmlElement(name = "MessagePriority", required = true)
  @NotNull
  @Column(name = "message_priority", nullable = false)
  private MessagePriority messagePriority;

  /** The Universally Unique Identifier (UUID) for the message type for the original message. */
  @Schema(
      description =
          "The Universally Unique Identifier (UUID) for the message type for the original message",
      required = true)
  @JsonProperty(required = true)
  @XmlElement(name = "MessageTypeId", required = true)
  @NotNull
  @Column(name = "message_type_id", nullable = false)
  private UUID messageTypeId;

  /** The username for the user associated with the original message. */
  @Schema(
      description = "The username for the user associated with the original message",
      required = true)
  @JsonProperty(required = true)
  @XmlElement(name = "MessageUsername", required = true)
  @NotNull
  @Size(min = 1, max = 100)
  @Column(name = "message_username", length = 100, nullable = false)
  private String messageUsername;

  /** The number of the message part in the set of message parts for the original message. */
  @Schema(
      description =
          "The number of the message part in the set of message parts for the original message",
      required = true)
  @JsonProperty(required = true)
  @XmlElement(name = "PartNo", required = true)
  @NotNull
  @Column(name = "part_no", nullable = false)
  private int partNo;

  /** The number of times that the sending of the message part was attempted. */
  @Schema(description = "The number of times that the sending of the message part was attempted")
  @JsonProperty
  @XmlElement(name = "SendAttempts")
  @Column(name = "send_attempts")
  private Integer sendAttempts;

  /** The message part status e.g. Initialized, Sending, etc */
  @Schema(description = "The message part status", required = true)
  @JsonProperty(required = true)
  @XmlElement(name = "Status", required = true)
  @NotNull
  @Column(name = "status", nullable = false)
  private MessagePartStatus status;

  /** The total number of parts in the set of message parts for the original message. */
  @Schema(
      description =
          "The total number of parts in the set of message parts for the original message",
      required = true)
  @JsonProperty(required = true)
  @XmlElement(name = "TotalParts", required = true)
  @NotNull
  @Column(name = "total_parts", nullable = false)
  private int totalParts;

  /** Constructs a new <b>MessagePart</b>. */
  public MessagePart() {}

  /**
   * Constructs a new <b>MessagePart</b> and populates it from the message information stored in the
   * specified WBXML document.
   *
   * @param document the WBXML document containing the message information
   */
  public MessagePart(Document document) {
    Element rootElement = document.getRootElement();

    this.id = UUID.fromString(rootElement.getAttributeValue("id"));
    this.partNo = Integer.parseInt(rootElement.getAttributeValue("partNo"));
    this.totalParts = Integer.parseInt(rootElement.getAttributeValue("totalParts"));

    if (rootElement.hasAttribute("sendAttempts")) {
      this.sendAttempts = Integer.parseInt(rootElement.getAttributeValue("sendAttempts"));
    }

    if (rootElement.hasAttribute("downloadAttempts")) {
      this.downloadAttempts = Integer.parseInt(rootElement.getAttributeValue("downloadAttempts"));
    }

    this.status = MessagePartStatus.INITIALIZED;

    this.messageId = UUID.fromString(rootElement.getAttributeValue("messageId"));
    this.messageUsername = rootElement.getAttributeValue("messageUsername");
    this.messageDeviceId = UUID.fromString(rootElement.getAttributeValue("messageDeviceId"));
    this.messageTypeId = UUID.fromString(rootElement.getAttributeValue("messageTypeId"));

    if (rootElement.hasAttribute("messageCorrelationId")) {
      this.messageCorrelationId =
          UUID.fromString(rootElement.getAttributeValue("messageCorrelationId"));
    }

    this.messagePriority =
        MessagePriority.fromNumericCode(
            Integer.parseInt(rootElement.getAttributeValue("messagePriority")));

    String messageCreatedAttributeValue = rootElement.getAttributeValue("messageCreated");

    try {
      this.messageCreated = ISO8601Util.toLocalDateTime(messageCreatedAttributeValue);
    } catch (Throwable e) {
      throw new RuntimeException(
          String.format(
              "Failed to parse the messageCreated ISO8601 timestamp (%s) for the message part (%s)",
              messageCreatedAttributeValue, id),
          e);
    }

    if (rootElement.hasAttribute("messageDataHash")) {
      this.messageDataHash = rootElement.getAttributeValue("messageDataHash");
    }

    if (rootElement.hasAttribute("messageEncryptionIV")) {
      this.messageEncryptionIV = rootElement.getAttributeValue("messageEncryptionIV");
    }

    this.messageChecksum = rootElement.getAttributeValue("messageChecksum");

    this.data = rootElement.getOpaque();
  }

  /**
   * Constructs a new <b>MessagePart</b>.
   *
   * @param partNo the number of the message part in the set of message parts for the original
   *     message
   * @param totalParts the total number of parts in the set of message parts for the original
   *     message
   * @param messageId the Universally Unique Identifier (UUID) for the original message
   * @param messageUsername the username for the user associated with the original message
   * @param messageDeviceId the Universally Unique Identifier (UUID) for the device the original
   *     message originated from
   * @param messageTypeId the Universally Unique Identifier (UUID) for the message type for the
   *     original message
   * @param messageCorrelationId the Universally Unique Identifier (UUID) used to correlate the
   *     original message
   * @param messagePriority the priority for the original message
   * @param messageCreated the date and time the original message was created
   * @param messageDataHash the hash of the unencrypted data for the original message
   * @param messageEncryptionIV the base-64 encoded initialization vector for the encryption scheme
   *     for the original message
   * @param messageChecksum the checksum for the original message
   * @param data the binary data for the message part
   */
  public MessagePart(
      int partNo,
      int totalParts,
      UUID messageId,
      String messageUsername,
      UUID messageDeviceId,
      UUID messageTypeId,
      UUID messageCorrelationId,
      MessagePriority messagePriority,
      LocalDateTime messageCreated,
      String messageDataHash,
      String messageEncryptionIV,
      String messageChecksum,
      byte[] data) {
    this.id = UuidCreator.getShortPrefixComb();
    this.partNo = partNo;
    this.totalParts = totalParts;
    this.status = MessagePartStatus.INITIALIZED;
    this.messageId = messageId;
    this.messageUsername = messageUsername;
    this.messageDeviceId = messageDeviceId;
    this.messageTypeId = messageTypeId;
    this.messageCorrelationId = messageCorrelationId;
    this.messagePriority = messagePriority;
    this.messageCreated = messageCreated;
    this.messageDataHash = messageDataHash;
    this.messageEncryptionIV = messageEncryptionIV;
    this.messageChecksum = messageChecksum;
    this.data = data;
  }

  /**
   * Constructs a new <b>MessagePart</b>.
   *
   * @param id the Universally Unique Identifier (UUID) for the message part
   * @param partNo the number of the message part in the set of message parts for the original
   *     message
   * @param totalParts total number of parts in the set of message parts for the original message
   * @param sendAttempts the number of times that the sending of the message part was attempted
   * @param downloadAttempts the number of times that downloading of the message part was attempted
   * @param status the message part status e.g. Initialized, Sending, etc
   * @param messageId the Universally Unique Identifier (UUID) for the original message
   * @param messageUsername the username for the user associated with the original message
   * @param messageDeviceId the Universally Unique Identifier (UUID) for the device the original
   *     message originated from
   * @param messageTypeId the Universally Unique Identifier (UUID) for the message type for the
   *     original message
   * @param messageCorrelationId the Universally Unique Identifier (UUID) used to correlate the
   *     original message
   * @param messagePriority the priority for the original message
   * @param messageCreated the date and time the original message was created
   * @param messageDataHash the hash of the unencrypted data for the original message
   * @param messageEncryptionIV the base-64 encoded initialization vector for the encryption scheme
   *     for the original message
   * @param messageChecksum the checksum for the original message
   * @param lockName the name of the entity that has locked the message part for processing
   * @param data the binary data for the message part
   */
  public MessagePart(
      UUID id,
      int partNo,
      int totalParts,
      Integer sendAttempts,
      Integer downloadAttempts,
      MessagePartStatus status,
      UUID messageId,
      String messageUsername,
      UUID messageDeviceId,
      UUID messageTypeId,
      UUID messageCorrelationId,
      MessagePriority messagePriority,
      LocalDateTime messageCreated,
      String messageDataHash,
      String messageEncryptionIV,
      String messageChecksum,
      String lockName,
      byte[] data) {
    this.id = id;
    this.partNo = partNo;
    this.totalParts = totalParts;
    this.sendAttempts = sendAttempts;
    this.downloadAttempts = downloadAttempts;
    this.status = status;
    this.messageId = messageId;
    this.messageUsername = messageUsername;
    this.messageDeviceId = messageDeviceId;
    this.messageTypeId = messageTypeId;
    this.messageCorrelationId = messageCorrelationId;
    this.messagePriority = messagePriority;
    this.messageCreated = messageCreated;
    this.messageDataHash = messageDataHash;
    this.messageEncryptionIV = messageEncryptionIV;
    this.messageChecksum = messageChecksum;
    this.lockName = lockName;
    this.data = data;
  }

  /**
   * Returns <b>true</b> if the WBXML document contains valid message part information or
   * <b>false</b> otherwise.
   *
   * @param document the WBXML document to validate
   * @return <b>true</b> if the WBXML document contains valid message part information or
   *     <b>false</b> otherwise
   */
  public static boolean isValidWBXML(Document document) {
    Element rootElement = document.getRootElement();

    return rootElement.getName().equals("MessagePart")
        && !((!rootElement.hasAttribute("id"))
            || (!rootElement.hasAttribute("partNo"))
            || (!rootElement.hasAttribute("totalParts"))
            || (!rootElement.hasAttribute("messageId"))
            || (!rootElement.hasAttribute("messageUsername"))
            || (!rootElement.hasAttribute("messageDeviceId"))
            || (!rootElement.hasAttribute("messageTypeId"))
            || (!rootElement.hasAttribute("messagePriority"))
            || (!rootElement.hasAttribute("messageCreated"))
            || (!rootElement.hasAttribute("messageChecksum")));
  }

  /**
   * Indicates whether some other object is "equal to" this one.
   *
   * @param object the reference object with which to compare
   * @return <b>true</b> if this object is the same as the object argument otherwise <b> false</b>
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

    MessagePart other = (MessagePart) object;

    return Objects.equals(id, other.id);
  }

  /**
   * Returns the binary data for the message part.
   *
   * @return the binary data for the message part
   */
  public byte[] getData() {
    return data;
  }

  /**
   * Returns the number of times that downloading of the message part was attempted.
   *
   * @return the number of times that downloading of the message part was attempted
   */
  public Integer getDownloadAttempts() {
    return downloadAttempts;
  }

  /**
   * Returns the Universally Unique Identifier (UUID) for the message part.
   *
   * @return the Universally Unique Identifier (UUID) for the message part
   */
  public UUID getId() {
    return id;
  }

  /**
   * Returns the name of the entity that has locked the message part for processing.
   *
   * @return the name of the entity that has locked the message part for processing
   */
  public String getLockName() {
    return lockName;
  }

  /**
   * Returns the checksum for the original message.
   *
   * @return the checksum for the original message
   */
  public String getMessageChecksum() {
    return messageChecksum;
  }

  /**
   * Returns the Universally Unique Identifier (UUID) used to correlate the original message.
   *
   * @return the Universally Unique Identifier (UUID) used to correlate the original message
   */
  public UUID getMessageCorrelationId() {
    return messageCorrelationId;
  }

  /**
   * Returns the date and time the original message was created.
   *
   * @return the date and time the original message was created
   */
  public LocalDateTime getMessageCreated() {
    return messageCreated;
  }

  /**
   * Returns the hash of the unencrypted data for the original message if the message was encrypted.
   *
   * @return the hash of the unencrypted data for the original message if the message was encrypted
   */
  public String getMessageDataHash() {
    return messageDataHash;
  }

  /**
   * Returns the Universally Unique Identifier (UUID) for the device the original message originated
   * from
   *
   * @return the Universally Unique Identifier (UUID) for the device the original message originated
   *     from
   */
  public UUID getMessageDeviceId() {
    return messageDeviceId;
  }

  /**
   * Returns the base-64 encoded initialization vector for the encryption scheme for the original
   * message.
   *
   * @return the base-64 encoded initialization vector for the encryption scheme for the original
   *     message
   */
  public String getMessageEncryptionIV() {
    return messageEncryptionIV;
  }

  /**
   * Returns the Universally Unique Identifier (UUID) for the original message.
   *
   * @return the Universally Unique Identifier (UUID) for the original message
   */
  public UUID getMessageId() {
    return messageId;
  }

  /**
   * Returns the priority for the original message.
   *
   * @return the priority for the original message
   */
  public MessagePriority getMessagePriority() {
    return messagePriority;
  }

  /**
   * Returns the Universally Unique Identifier (UUID) for the message type for the original message.
   *
   * @return the Universally Unique Identifier (UUID) for the message type for the original message
   */
  public UUID getMessageTypeId() {
    return messageTypeId;
  }

  /**
   * Returns the username for the user associated with the original message.
   *
   * @return the username for the user associated with the original message
   */
  public String getMessageUsername() {
    return messageUsername;
  }

  /**
   * Returns the number of the message part in the set of message parts for the original message.
   *
   * @return the number of the message part in the set of message parts for the original message
   */
  public int getPartNo() {
    return partNo;
  }

  /**
   * Returns the number of times that the sending of the message part was attempted.
   *
   * @return the number of times that the sending of the message part was attempted
   */
  public Integer getSendAttempts() {
    return sendAttempts;
  }

  /**
   * Returns the message part status e.g. Initialized, Sending, etc.
   *
   * @return the message part status e.g. Initialized, Sending, etc
   */
  public MessagePartStatus getStatus() {
    return status;
  }

  /**
   * Returns the total number of parts in the set of message parts for the original message.
   *
   * @return the total number of parts in the set of message parts for the original message
   */
  public int getTotalParts() {
    return totalParts;
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

  /** Increment the download attempts. */
  public void incrementDownloadAttempts() {
    if (downloadAttempts == null) {
      downloadAttempts = 1;
    } else {
      downloadAttempts++;
    }
  }

  /**
   * Returns <b>true</b> if the data for the original message is encrypted or <b>false</b>
   * otherwise.
   *
   * @return <b>true</b> if the data for the original message is encrypted or <b>false</b> otherwise
   */
  public boolean messageIsEncrypted() {
    return ((messageDataHash != null) && (messageDataHash.length() > 0));
  }

  /**
   * Set the binary data for the message part.
   *
   * @param data the binary data for the message part
   */
  public void setData(byte[] data) {
    this.data = data;
  }

  /**
   * Set the number of times that downloading of the message part was attempted.
   *
   * @param downloadAttempts the number of times that downloading of the message part was attempted
   */
  public void setDownloadAttempts(Integer downloadAttempts) {
    this.downloadAttempts = downloadAttempts;
  }

  /**
   * Set the Universally Unique Identifier (UUID) for the message part.
   *
   * @param id the Universally Unique Identifier (UUID) for the message part
   */
  public void setId(UUID id) {
    this.id = id;
  }

  /**
   * Set the name of the entity that has locked the message part for processing.
   *
   * @param lockName the name of the entity that has locked the message part for processing
   */
  public void setLockName(String lockName) {
    this.lockName = lockName;
  }

  /**
   * Set the checksum for the original message.
   *
   * @param messageChecksum the checksum for the original message
   */
  public void setMessageChecksum(String messageChecksum) {
    this.messageChecksum = messageChecksum;
  }

  /**
   * Set the Universally Unique Identifier (UUID) used to correlate the original message.
   *
   * @param messageCorrelationId the Universally Unique Identifier (UUID) used to correlate the
   *     original message
   */
  public void setMessageCorrelationId(UUID messageCorrelationId) {
    this.messageCorrelationId = messageCorrelationId;
  }

  /**
   * Set the date and time the original message was created.
   *
   * @param messageCreated the date and time the original message was created
   */
  public void setMessageCreated(LocalDateTime messageCreated) {
    this.messageCreated = messageCreated;
  }

  /**
   * Set the hash of the unencrypted data for the original message if the message was encrypted.
   *
   * @param messageDataHash the hash of the unencrypted data for the original message if the message
   *     was encrypted
   */
  public void setMessageDataHash(String messageDataHash) {
    this.messageDataHash = messageDataHash;
  }

  /**
   * Set the Universally Unique Identifier (UUID) for the device the original message originated
   * from.
   *
   * @param messageDeviceId the Universally Unique Identifier (UUID) for the device the original
   *     message originated from
   */
  public void setMessageDeviceId(UUID messageDeviceId) {
    this.messageDeviceId = messageDeviceId;
  }

  /**
   * Set the base-64 encoded initialization vector for the encryption scheme for the original
   * message.
   *
   * @param messageEncryptionIV the base-64 encoded initialization vector for the encryption scheme
   *     for the original message
   */
  public void setMessageEncryptionIV(String messageEncryptionIV) {
    this.messageEncryptionIV = messageEncryptionIV;
  }

  /**
   * Set the Universally Unique Identifier (UUID) for the original message.
   *
   * @param messageId the Universally Unique Identifier (UUID) for the original message
   */
  public void setMessageId(UUID messageId) {
    this.messageId = messageId;
  }

  /**
   * Set the priority for the original message.
   *
   * @param messagePriority the priority for the original message
   */
  public void setMessagePriority(MessagePriority messagePriority) {
    this.messagePriority = messagePriority;
  }

  /**
   * Set the Universally Unique Identifier (UUID) for the message type for the original message.
   *
   * @param messageTypeId the Universally Unique Identifier (UUID) for the message type for the
   *     original message
   */
  public void setMessageTypeId(UUID messageTypeId) {
    this.messageTypeId = messageTypeId;
  }

  /**
   * Set the username for the user associated with the original message.
   *
   * @param messageUsername the username for the user associated with the original message
   */
  public void setMessageUsername(String messageUsername) {
    this.messageUsername = messageUsername;
  }

  /**
   * Set the number of the message part in the set of message parts for the original message.
   *
   * @param partNo the number of the message part in the set of message parts for the original
   *     message
   */
  public void setPartNo(int partNo) {
    this.partNo = partNo;
  }

  /**
   * Set the number of times that the sending of the message part was attempted.
   *
   * @param sendAttempts the number of times that the sending of the message part was attempted
   */
  public void setSendAttempts(Integer sendAttempts) {
    this.sendAttempts = sendAttempts;
  }

  /**
   * Set the message part status e.g. Initialized, Sending, etc.
   *
   * @param status the message part status e.g. Initialized, Sending, etc
   */
  public void setStatus(MessagePartStatus status) {
    this.status = status;
  }

  /**
   * Set the total number of parts in the set of message parts for the original message.
   *
   * @param totalParts the total number of parts in the set of message parts for the original
   *     message
   */
  public void setTotalParts(int totalParts) {
    this.totalParts = totalParts;
  }

  /**
   * Returns the String representation of the message part.
   *
   * @return the String representation of the message part
   */
  @Override
  public String toString() {
    StringBuilder buffer = new StringBuilder("<MessagePart");

    buffer.append(" id=\"").append(id).append("\"");
    buffer.append(" partNo=\"").append(partNo).append("\"");
    buffer.append(" totalParts=\"").append(totalParts).append("\"");

    if (sendAttempts != null) {
      buffer.append(" sendAttempts=\"").append(sendAttempts).append("\"");
    }

    if (downloadAttempts != null) {
      buffer.append(" downloadAttempts=\"").append(downloadAttempts).append("\"");
    }

    buffer.append(" status=\"").append(status).append("\"");
    buffer.append(" messageId=\"").append(messageId).append("\"");
    buffer.append(" messageUsername=\"").append(messageUsername).append("\"");
    buffer.append(" messageDeviceId=\"").append(messageDeviceId).append("\"");
    buffer.append(" messageTypeId=\"").append(messageTypeId).append("\"");

    if (messageCorrelationId != null) {
      buffer.append(" messageCorrelationId=\"").append(messageCorrelationId).append("\"");
    }

    buffer.append(" messagePriority=\"").append(messagePriority).append("\"");
    buffer
        .append(" messageCreated=\"")
        .append(ISO8601Util.fromLocalDateTime(messageCreated))
        .append("\"");
    buffer.append(" messageDataHash=\"").append(messageDataHash).append("\"");
    buffer.append(" messageEncryptionIV=\"").append(messageEncryptionIV).append("\"");
    buffer.append(" messageChecksum=\"").append(messageChecksum).append("\"");

    buffer.append(">").append(data.length).append(" bytes of data</MessagePart>");

    return buffer.toString();
  }

  /**
   * Returns the WBXML representation of the message part.
   *
   * @return the WBXML representation of the message part
   */
  public byte[] toWBXML() {
    Element rootElement = new Element("MessagePart");

    rootElement.setAttribute("id", id.toString());
    rootElement.setAttribute("partNo", Integer.toString(partNo));
    rootElement.setAttribute("totalParts", Integer.toString(totalParts));

    if (sendAttempts != null) {
      rootElement.setAttribute("sendAttempts", Integer.toString(sendAttempts));
    }

    if (downloadAttempts != null) {
      rootElement.setAttribute("downloadAttempts", Integer.toString(downloadAttempts));
    }

    rootElement.setAttribute("messageId", messageId.toString());
    rootElement.setAttribute("messageUsername", messageUsername);
    rootElement.setAttribute("messageDeviceId", messageDeviceId.toString());
    rootElement.setAttribute("messageTypeId", messageTypeId.toString());

    if (messageCorrelationId != null) {
      rootElement.setAttribute("messageCorrelationId", messageCorrelationId.toString());
    }

    rootElement.setAttribute(
        "messagePriority", Integer.toString(MessagePriority.toNumericCode(messagePriority)));
    rootElement.setAttribute("messageCreated", ISO8601Util.fromLocalDateTime(messageCreated));

    if (messageDataHash != null) {
      rootElement.setAttribute("messageDataHash", messageDataHash);
    }

    if (messageEncryptionIV != null) {
      rootElement.setAttribute("messageEncryptionIV", messageEncryptionIV);
    }

    rootElement.setAttribute("messageChecksum", messageChecksum);
    rootElement.addContent(data);

    Encoder encoder = new Encoder(new Document(rootElement));

    return encoder.getData();
  }
}
