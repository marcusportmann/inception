/*
 * Copyright 2018 Marcus Portmann
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

//~--- non-JDK imports --------------------------------------------------------

import digital.inception.core.util.ISO8601Util;
import digital.inception.core.util.StringUtil;
import digital.inception.core.wbxml.Document;
import digital.inception.core.wbxml.Element;
import digital.inception.core.wbxml.Encoder;

import java.time.LocalDateTime;
import java.util.UUID;

//~--- JDK imports ------------------------------------------------------------

/**
 * The <code>Message</code> class holds the information for a message.
 * <p/>
 * It provides facilities to convert to and from the WBXML representation of the message.
 *
 * @author Marcus Portmann
 */
public class Message
{
  /**
   * The maximum size of an asynchronous message in bytes. Messages larger than this size will be
   * split into a number of message parts.
   */
  public static final int MAX_ASYNC_MESSAGE_SIZE = 40000;

  /**
   * The Universally Unique Identifier (UUID) used to correlate the message.
   */
  private UUID correlationId;

  /**
   * The date and time the message was created.
   */
  private LocalDateTime created;

  /**
   * The data for the message.
   */
  private byte[] data;

  /**
   * The hash of the unencrypted data for the message.
   * <p/>
   * If the message data is encrypted then this will be the hash of the unencrypted message data and
   * will be used to verify the message data has been decrypted successfully.
   */
  private String dataHash;

  /**
   * The Universally Unique Identifier (UUID) used to uniquely identify the device the message
   * originated from.
   */
  private UUID deviceId;

  /**
   * The number of times that downloading of the message was attempted.
   */
  private int downloadAttempts;

  /**
   * The base-64 encoded initialization vector for the encryption scheme for the message.
   */
  private String encryptionIV;

  /**
   * The Universally Unique Identifier (UUID) used to uniquely identify the message.
   */
  private UUID id;

  /**
   * Is encryption disabled for the message.
   */
  private boolean isEncryptionDisabled;

  /**
   * The date and time the last attempt was made to process the message.
   */
  private LocalDateTime lastProcessed;

  /**
   * The name of the entity that has locked this message for processing or <code>null</code> if
   * the message is not being processed.
   */
  private String lockName;

  /**
   * The date and time the message was persisted.
   */
  private LocalDateTime persisted;

  /**
   * The message priority. Messages with a higher priority value are processed before messages with
   * a lower priority value.
   */
  private MessagePriority priority;

  /**
   * The number of times that the processing of the message was attempted.
   */
  private int processAttempts;

  /**
   * The number of times that the sending of the message was attempted.
   */
  private int sendAttempts;

  /**
   * The message status e.g. Initialized, Sending, etc.
   */
  private MessageStatus status;

  /**
   * The Universally Unique Identifier (UUID) used to uniquely identify the type of message.
   */
  private UUID typeId;

  /**
   * The date and time the message was updated.
   */
  private LocalDateTime updated;

  /**
   * The username identifying the user associated with the message.
   */
  private String username;

  /**
   * Constructs a new <code>Message</code>.
   */
  public Message() {}

  /**
   * Constructs a new <code>Message</code> and populates it from the message information stored
   * in the specified WBXML document.
   *
   * @param document the WBXML document containing the message information
   */
  public Message(Document document)
    throws MessagingServiceException
  {
    Element rootElement = document.getRootElement();

    this.id = UUID.fromString(rootElement.getAttributeValue("id"));
    this.username = rootElement.getAttributeValue("username");
    this.deviceId = UUID.fromString(rootElement.getAttributeValue("deviceId"));
    this.typeId = UUID.fromString(rootElement.getAttributeValue("typeId"));
    this.correlationId = UUID.fromString(rootElement.getAttributeValue("correlationId"));
    this.priority = MessagePriority.fromCode(Integer.parseInt(rootElement.getAttributeValue(
        "priority")));
    this.data = rootElement.getOpaque();
    this.dataHash = rootElement.getAttributeValue("dataHash");
    this.encryptionIV = rootElement.getAttributeValue("encryptionIV");

    String createdAttributeValue = rootElement.getAttributeValue("created");

    try
    {
      this.created = ISO8601Util.toLocalDateTime(createdAttributeValue);
    }
    catch (Throwable e)
    {
      throw new MessagingServiceException(String.format(
          "Failed to parse the created ISO8601 timestamp (%s) for the message (%s)",
          createdAttributeValue, id), e);
    }

    this.sendAttempts = Integer.parseInt(rootElement.getAttributeValue("sendAttempts"));
    this.processAttempts = Integer.parseInt(rootElement.getAttributeValue("processAttempts"));
    this.downloadAttempts = Integer.parseInt(rootElement.getAttributeValue("downloadAttempts"));
    this.status = MessageStatus.INITIALIZED;
  }

  /**
   * Constructs a new <code>Message</code>.
   *
   * @param username      the username identifying the user associated with the message
   * @param deviceId      the Universally Unique Identifier (UUID) used to uniquely identify the
   *                      device the message originated from
   * @param typeId        the Universally Unique Identifier (UUID) used to uniquely identify the
   *                      type of message
   * @param correlationId the Universally Unique Identifier (UUID) used to correlate the message
   * @param priority      the message priority
   * @param data          the data for the message which is NOT encrypted
   */
  public Message(String username, UUID deviceId, UUID typeId, UUID correlationId,
      MessagePriority priority, byte[] data)
  {
    this.id = UUID.randomUUID();
    this.username = username;
    this.deviceId = deviceId;
    this.typeId = typeId;
    this.correlationId = correlationId;
    this.priority = priority;
    this.data = data;
    this.dataHash = "";
    this.encryptionIV = "";
    this.created = LocalDateTime.now();
    this.sendAttempts = 0;
    this.processAttempts = 0;
    this.downloadAttempts = 0;
    this.status = MessageStatus.INITIALIZED;
  }

  /**
   * Constructs a new <code>Message</code>.
   *
   * @param username      the username identifying the user associated with the message
   * @param deviceId      the Universally Unique Identifier (UUID) used to uniquely identify the
   *                      device the message originated from
   * @param typeId        the Universally Unique Identifier (UUID) used to uniquely identify the
   *                      type of message
   * @param correlationId the Universally Unique Identifier (UUID) used to correlate the message
   * @param priority      the message priority
   * @param data          the data for the message which may be encrypted
   * @param dataHash      the hash of the unencrypted data for the message
   * @param encryptionIV  the base-64 encoded initialization vector for the encryption scheme
   */
  public Message(String username, UUID deviceId, UUID typeId, UUID correlationId,
      MessagePriority priority, byte[] data, String dataHash, String encryptionIV)
  {
    this.id = UUID.randomUUID();
    this.username = username;
    this.deviceId = deviceId;
    this.typeId = typeId;
    this.correlationId = correlationId;
    this.priority = priority;
    this.data = data;
    this.dataHash = dataHash;

    if (dataHash.length() == 0)
    {
      throw new RuntimeException(
          "Unable to initialize a message with encrypted data using a blank data hash");
    }

    this.encryptionIV = encryptionIV;
    this.created = LocalDateTime.now();
    this.sendAttempts = 0;
    this.processAttempts = 0;
    this.downloadAttempts = 0;
    this.status = MessageStatus.INITIALIZED;
  }

  /**
   * Constructs a new <code>Message</code>.
   *
   * @param id               the Universally Unique Identifier (UUID) used to uniquely identify the
   *                         message
   * @param username         the username identifying the user associated with the message
   * @param deviceId         the Universally Unique Identifier (UUID) used to uniquely identify the
   *                         device the message originated from
   * @param typeId           the Universally Unique Identifier (UUID) used to uniquely identify the
   *                         type of message
   * @param correlationId    the Universally Unique Identifier (UUID) used to correlate the message
   * @param priority         the message priority
   * @param status           the message status e.g. Initialized, Sending, etc
   * @param created          the date and time the message was created
   * @param persisted        the date and time the message was persisted
   * @param updated          the date and time the message was updated
   * @param sendAttempts     the number of times that the sending of the message was attempted
   * @param processAttempts  the number of times that the processing of the message was attempted
   * @param downloadAttempts the number of times that downloading of the message was attempted
   * @param lockName         the name of the entity that has locked this message for processing or
   *                         <code>null</code> if the message is not being processed
   * @param lastProcessed    the date and time the last attempt was made to process the message
   * @param data             the data for the message which may be encrypted
   * @param dataHash         the hash of the unencrypted data for the message
   * @param encryptionIV     the base-64 encoded initialization vector for the encryption scheme
   */
  public Message(UUID id, String username, UUID deviceId, UUID typeId, UUID correlationId,
      MessagePriority priority, MessageStatus status, LocalDateTime created,
      LocalDateTime persisted, LocalDateTime updated, int sendAttempts, int processAttempts,
      int downloadAttempts, String lockName, LocalDateTime lastProcessed, byte[] data,
      String dataHash, String encryptionIV)
  {
    this.id = id;
    this.username = username;
    this.deviceId = deviceId;
    this.typeId = typeId;
    this.correlationId = correlationId;
    this.priority = priority;
    this.status = status;
    this.created = created;
    this.persisted = persisted;
    this.updated = updated;
    this.sendAttempts = sendAttempts;
    this.processAttempts = processAttempts;
    this.downloadAttempts = downloadAttempts;
    this.lockName = lockName;
    this.lastProcessed = lastProcessed;
    this.data = data;
    this.dataHash = dataHash;
    this.encryptionIV = encryptionIV;
  }

  /**
   * Returns <code>true</code> if the WBXML document contains valid message information or
   * <code>false</code> otherwise.
   *
   * @param document the WBXML document to validate
   *
   * @return <code>true</code> if the WBXML document contains valid message information or
   *         <code>false</code> otherwise
   */
  public static boolean isValidWBXML(Document document)
  {
    Element rootElement = document.getRootElement();

    return rootElement.getName().equals("Message")
        && !((!rootElement.hasAttribute("id")) || (!rootElement.hasAttribute("username"))
            || (!rootElement.hasAttribute("deviceId")) || (!rootElement.hasAttribute("priority"))
            || (!rootElement.hasAttribute("typeId")) || (!rootElement.hasAttribute(
            "correlationId")) || (!rootElement.hasAttribute("created"))
            || (!rootElement.hasAttribute("sendAttempts")) || (!rootElement.hasAttribute(
            "downloadAttempts")) || (!rootElement.hasAttribute("dataHash"))
            || (!rootElement.hasAttribute("encryptionIV")));
  }

  /**
   * Returns the Universally Unique Identifier (UUID) used to correlate the message.
   *
   * @return the Universally Unique Identifier (UUID) used to correlate the message
   */
  public UUID getCorrelationId()
  {
    return correlationId;
  }

  /**
   * Returns the date and time the message was created.
   *
   * @return the date and time the message was created
   */
  public LocalDateTime getCreated()
  {
    return created;
  }

  /**
   * Returns the data for the message which may be encrypted.
   *
   * @return the data for the message which may be encrypted
   */
  public byte[] getData()
  {
    return data;
  }

  /**
   * Returns the hash of the unencrypted data for the message.
   *
   * @return the hash of the unencrypted data for the message
   */
  public String getDataHash()
  {
    return dataHash;
  }

  /**
   * The Universally Unique Identifier (UUID) used to uniquely identify the device the message
   * originated from.
   *
   * @return the Universally Unique Identifier (UUID) used to uniquely identify the device the
   * message originated from
   */
  public UUID getDeviceId()
  {
    return deviceId;
  }

  /**
   * Returns the number of times that downloading of the message was attempted.
   *
   * @return the number of times that downloading of the message was attempted
   */
  public int getDownloadAttempts()
  {
    return downloadAttempts;
  }

  /**
   * Returns the base-64 encoded initialization vector for the encryption scheme for the message.
   *
   * @return the base-64 encoded initialization vector for the encryption scheme for the message
   */
  public String getEncryptionIV()
  {
    return encryptionIV;
  }

  /**
   * Returns the Universally Unique Identifier (UUID) used to uniquely identify the message.
   *
   * @return the Universally Unique Identifier (UUID) used to uniquely identify the message
   */
  public UUID getId()
  {
    return id;
  }

  /**
   * Returns the date and time the last attempt was made to process the message.
   *
   * @return the date and time the last attempt was made to process the message
   */
  public LocalDateTime getLastProcessed()
  {
    return lastProcessed;
  }

  /**
   * Returns the name of the entity that has locked this message for processing or
   * <code>null</code> if the message is not being processed.
   *
   * @return the name of the entity that has locked this message for processing or
   *         <code>null</code> if the message is not being processed
   */
  public String getLockName()
  {
    return lockName;
  }

  /**
   * Returns the date and time the message was persisted.
   *
   * @return the date and time the message was persisted
   */
  public LocalDateTime getPersisted()
  {
    return persisted;
  }

  /**
   * Returns the message priority.
   * <p/>
   * "Out of Order" processing is usually applied to messages so that messages with a higher
   * priority value are processed before messages with a lower priority value.
   *
   * @return the message priority
   */
  public MessagePriority getPriority()
  {
    return priority;
  }

  /**
   * Returns the number of times that the processing of the message was attempted.
   *
   * @return the number of times that the processing of the message was attempted
   */
  public int getProcessAttempts()
  {
    return processAttempts;
  }

  /**
   * Returns the number of times that the sending of the message was attempted.
   *
   * @return the number of times that the sending of the message was attempted
   */
  public int getSendAttempts()
  {
    return sendAttempts;
  }

  /**
   * Returns the message status e.g. Initialized, Sending, etc.
   *
   * @return the message status e.g. Initialized, Sending, etc
   */
  public MessageStatus getStatus()
  {
    return status;
  }

  /**
   * Returns the Universally Unique Identifier (UUID) used to uniquely identify the type of message.
   *
   * @return the Universally Unique Identifier (UUID) used to uniquely identify the type of message
   */
  public UUID getTypeId()
  {
    return typeId;
  }

  /**
   * Returns the date and time the message was updated.
   *
   * @return the date and time the message was updated
   */
  public LocalDateTime getUpdated()
  {
    return updated;
  }

  /**
   * Returns the username identifying the user associated with the message.
   *
   * @return the username identifying the user associated with the message
   */
  public String getUsername()
  {
    return username;
  }

  /**
   * Returns <code>true</code> if the data for the message is encrypted or <code>false</code>
   * otherwise.
   *
   * @return <code>true</code> if the data for the message is encrypted or <code>false</code>
   * otherwise
   */
  public boolean isEncrypted()
  {
    return (!StringUtil.isNullOrEmpty(dataHash));
  }

  /**
   * Returns <code>true</code> if encryption is disabled for the message or <code>false</code>
   * otherwise.
   *
   * @return <code>true</code> if encryption is disabled for the message or <code>false</code>
   * otherwise
   */
  public boolean isEncryptionDisabled()
  {
    return isEncryptionDisabled;
  }

  /**
   * Set the Universally Unique Identifier (UUID) used to correlate the message.
   *
   * @param correlationId the Universally Unique Identifier (UUID) used to correlate the message
   */
  public void setCorrelationId(UUID correlationId)
  {
    this.correlationId = correlationId;
  }

  /**
   * Set the date and time the message was created.
   *
   * @param created the date and time the message was created
   */
  public void setCreated(LocalDateTime created)
  {
    this.created = created;
  }

  /**
   * Set the data for the message which may be encrypted.
   *
   * @param data the data for the message which may be encrypted
   */
  public void setData(byte[] data)
  {
    this.data = data;
  }

  /**
   * Set the hash of the unencrypted data for the message.
   *
   * @param dataHash the hash of the unencrypted data for the message
   */
  public void setDataHash(String dataHash)
  {
    this.dataHash = dataHash;
  }

  /**
   * Set the Universally Unique Identifier (UUID) used to uniquely identify the device the message
   * originated from.
   *
   * @param deviceId the Universally Unique Identifier (UUID) used to uniquely identify the device
   *                 the message originated from
   */
  public void setDeviceId(UUID deviceId)
  {
    this.deviceId = deviceId;
  }

  /**
   * Set the number of times that downloading of the message was attempted.
   *
   * @param downloadAttempts the number of times that downloading of the message was attempted
   */
  public void setDownloadAttempts(int downloadAttempts)
  {
    this.downloadAttempts = downloadAttempts;
  }

  /**
   * Set the base-64 encoded initialization vector for the encryption scheme for the message.
   *
   * @param encryptionIV the base-64 encoded initialization vector for the encryption scheme for
   *                     the message
   */
  public void setEncryptionIV(String encryptionIV)
  {
    this.encryptionIV = encryptionIV;
  }

  /**
   * Set the Universally Unique Identifier (UUID) used to uniquely identify the message.
   *
   * @param id the Universally Unique Identifier (UUID) used to uniquely identify the message
   */
  public void setId(UUID id)
  {
    this.id = id;
  }

  /**
   * Set whether encryption is disabled for the message.
   *
   * @param isEncryptionDisabled <code>true</code> if encryption is disabled for the message or
   *                             <code>false</code> otherwise
   */
  public void setIsEncryptionDisabled(boolean isEncryptionDisabled)
  {
    this.isEncryptionDisabled = isEncryptionDisabled;
  }

  /**
   * Set the date and time the last attempt was made to process the message.
   *
   * @param lastProcessed the date and time the last attempt was made to process the message
   */
  public void setLastProcessed(LocalDateTime lastProcessed)
  {
    this.lastProcessed = lastProcessed;
  }

  /**
   * Set the name of the entity that has locked this message for processing or <code>null</code>
   * if the message is not being processed.
   *
   * @param lockName the name of the entity that has locked this message for processing or
   *                 <code>null</code> if the message is not being processed
   */
  public void setLockName(String lockName)
  {
    this.lockName = lockName;
  }

  /**
   * Set the date and time the message was persisted.
   *
   * @param persisted the date and time the message was persisted
   */
  public void setPersisted(LocalDateTime persisted)
  {
    this.persisted = persisted;
  }

  /**
   * Set the message priority. Messages with a higher priority value are processed before messages
   * with a lower priority value.
   *
   * @param priority the message priority. Messages with a higher priority value are processed
   *                 before messages with a lower priority value
   */
  public void setPriority(MessagePriority priority)
  {
    this.priority = priority;
  }

  /**
   * Set the number of times that the processing of the message was attempted.
   *
   * @param processAttempts the number of times that the processing of the message was attempted
   */
  public void setProcessAttempts(int processAttempts)
  {
    this.processAttempts = processAttempts;
  }

  /**
   * Set the number of times that the sending of the message was attempted.
   *
   * @param sendAttempts the number of times that the sending of the message was attempted
   */
  public void setSendAttempts(int sendAttempts)
  {
    this.sendAttempts = sendAttempts;
  }

  /**
   * Set the message status e.g. Initialized, Sending, etc.
   *
   * @param status the message status e.g. Initialized, Sending, etc
   */
  public void setStatus(MessageStatus status)
  {
    this.status = status;
  }

  /**
   * Set the Universally Unique Identifier (UUID) used to uniquely identify the type of message.
   *
   * @param typeId the Universally Unique Identifier (UUID) used to uniquely identify the type of
   *               message
   */
  public void setTypeId(UUID typeId)
  {
    this.typeId = typeId;
  }

  /**
   * Set the date and time the message was updated.
   *
   * @param updated the date and time the message was updated
   */
  public void setUpdated(LocalDateTime updated)
  {
    this.updated = updated;
  }

  /**
   * Set the username identifying the user associated with the message.
   *
   * @param username the username identifying the user associated with the message
   */
  public void setUsername(String username)
  {
    this.username = username;
  }

  /**
   * Returns the String representation of the message.
   *
   * @return the String representation of the message
   */
  @Override
  public String toString()
  {
    StringBuilder buffer = new StringBuilder("<Message");

    buffer.append(" id=\"").append(id).append("\"");
    buffer.append(" username=\"").append(username).append("\"");
    buffer.append(" deviceId=\"").append(deviceId).append("\"");
    buffer.append(" typeId=\"").append(typeId).append("\"");
    buffer.append(" correlationId=\"").append(correlationId).append("\"");
    buffer.append(" priority=\"").append(priority).append("\"");
    buffer.append(" status=\"").append(status).append("\"");
    buffer.append(" created=\"").append(ISO8601Util.fromLocalDateTime(created)).append("\"");

    if (persisted != null)
    {
      buffer.append(" persisted=\"").append(ISO8601Util.fromLocalDateTime(persisted)).append("\"");
    }
    else
    {
      buffer.append(" persisted=\"Never\"");
    }

    if (updated != null)
    {
      buffer.append(" updated=\"").append(ISO8601Util.fromLocalDateTime(updated)).append("\"");
    }
    else
    {
      buffer.append(" updated=\"Never\"");
    }

    buffer.append(" sendAttempts=\"").append(sendAttempts).append("\"");
    buffer.append(" processAttempts=\"").append(processAttempts).append("\"");
    buffer.append(" downloadAttempts=\"").append(downloadAttempts).append("\"");

    if (lockName != null)
    {
      buffer.append(" lockName=\"").append(lockName).append("\"");
    }
    else
    {
      buffer.append(" lockName=\"None\"");
    }

    if (lastProcessed != null)
    {
      buffer.append(" lastProcessed=\"").append(ISO8601Util.fromLocalDateTime(lastProcessed))
          .append("\"");
    }
    else
    {
      buffer.append(" lastProcessed=\"Never\"");
    }

    buffer.append(" dataHash=\"").append(dataHash).append("\"");
    buffer.append(" encryptionIV=\"").append(encryptionIV).append("\"");

    if (isEncrypted())
    {
      buffer.append(">").append(data.length).append(" bytes of opaque encrypted data</Message>");
    }
    else
    {
      buffer.append(">").append(data.length).append(" bytes of opaque data</Message>");
    }

    return buffer.toString();
  }

  /**
   * Returns the WBXML representation of the message.
   *
   * @return the WBXML representation of the message
   */
  public byte[] toWBXML()
  {
    Element rootElement = new Element("Message");

    rootElement.setAttribute("id", id.toString());
    rootElement.setAttribute("username", username);
    rootElement.setAttribute("deviceId", deviceId.toString());
    rootElement.setAttribute("typeId", typeId.toString());
    rootElement.setAttribute("correlationId", correlationId.toString());
    rootElement.setAttribute("priority", Integer.toString(priority.getCode()));
    rootElement.setAttribute("created", ISO8601Util.fromLocalDateTime(created));
    rootElement.setAttribute("sendAttempts", Integer.toString(sendAttempts));
    rootElement.setAttribute("processAttempts", Integer.toString(processAttempts));
    rootElement.setAttribute("downloadAttempts", Integer.toString(downloadAttempts));

    if (dataHash != null)
    {
      rootElement.setAttribute("dataHash", dataHash);
    }
    else
    {
      rootElement.setAttribute("dataHash", "");
    }

    rootElement.setAttribute("encryptionIV", encryptionIV);

    rootElement.addContent(data);

    Encoder encoder = new Encoder(new Document(rootElement));

    return encoder.getData();
  }
}
