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

import digital.inception.core.util.Base64Util;
import digital.inception.core.util.CryptoUtil;
import java.security.MessageDigest;
import java.util.UUID;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import org.springframework.util.StringUtils;

// ~--- JDK imports ------------------------------------------------------------

/**
 * The <code>MessageTranslator</code> class provides the facilities to create messages containing
 * WBXML message data. It also provides facilities to retrieve the WBXML message data from a
 * message.
 *
 * @author Marcus Portmann
 */
@SuppressWarnings({"WeakerAccess"})
public class MessageTranslator {

  private static ThreadLocal<MessageDigest> threadLocalMessageDigest =
      ThreadLocal.withInitial(
          () -> {
            try {
              return MessageDigest.getInstance("SHA-256");
            } catch (Throwable e) {
              throw new RuntimeException("Failed to initialize the SHA-256 message digest", e);
            }
          });

  /**
   * The Universally Unique Identifier (UUID) uniquely identifying the device the message originated
   * from.
   */
  private UUID deviceId;

  /** The encryption key used to encrypt or decrypt the message data. */
  private byte[] encryptionKey;

  /** The username uniquely identifying the username responsible for the message. */
  private String username;

  /**
   * Constructs a new <code>MessageTranslator</code>.
   *
   * @param username the username uniquely identifying the username responsible for the message
   * @param deviceId the Universally Unique Identifier (UUID) uniquely identifying the device the
   *     message originated from
   */
  public MessageTranslator(String username, UUID deviceId) {
    this.username = username;
    this.deviceId = deviceId;
    this.encryptionKey = null;
  }

  /**
   * Constructs a new <code>MessageTranslator</code>.
   *
   * @param username the username uniquely identifying the username responsible for the message
   * @param deviceId the Universally Unique Identifier (UUID) uniquely identifying the device the
   *     message originated from
   * @param encryptionKey the key used to encrypt or decrypt the message data
   */
  public MessageTranslator(String username, UUID deviceId, byte[] encryptionKey) {
    this.username = username;
    this.deviceId = deviceId;
    this.encryptionKey = encryptionKey;
  }

  /**
   * Decrypt the message data.
   *
   * @param encryptionKey the encryption key to use to decrypt the message data
   * @param encryptionIV the encryption initialization vector
   * @param data the message data to decrypt
   * @return the decrypted message data
   */
  public static byte[] decryptMessageData(byte[] encryptionKey, byte[] encryptionIV, byte[] data)
      throws MessagingServiceException {
    if ((encryptionKey == null) || (encryptionKey.length == 0)) {
      throw new MessagingServiceException(
          "Failed to decrypt the message data: Invalid encryption key");
    }

    try {
      SecretKey secretKey = new SecretKeySpec(encryptionKey, CryptoUtil.AES_KEY_SPEC);
      IvParameterSpec iv = new IvParameterSpec(encryptionIV);
      Cipher cipher = Cipher.getInstance(CryptoUtil.AES_TRANSFORMATION_NAME);

      cipher.init(Cipher.DECRYPT_MODE, secretKey, iv);

      return cipher.doFinal(data);
    } catch (Throwable e) {
      throw new MessagingServiceException("Failed to decrypt the message data", e);
    }
  }

  /**
   * Encrypt the message data.
   *
   * @param encryptionKey the encryption key to use to encrypt the message data
   * @param encryptionIV the encryption initialization vector
   * @param data the message data to encrypt
   * @return the encrypted message data
   */
  public static byte[] encryptMessageData(byte[] encryptionKey, byte[] encryptionIV, byte[] data)
      throws MessagingServiceException {
    if (encryptionKey == null) {
      throw new MessagingServiceException(
          "Failed to encrypt the message data: Invalid encryption key");
    }

    try {
      SecretKey secretKey = new SecretKeySpec(encryptionKey, CryptoUtil.AES_KEY_SPEC);
      IvParameterSpec iv = new IvParameterSpec(encryptionIV);
      Cipher cipher = Cipher.getInstance(CryptoUtil.AES_TRANSFORMATION_NAME);

      cipher.init(Cipher.ENCRYPT_MODE, secretKey, iv);

      return cipher.doFinal(data);
    } catch (Throwable e) {
      throw new MessagingServiceException("Failed to encrypt the message data", e);
    }
  }

  /**
   * Retrieve the WBXML-based message data from the message.
   *
   * @param message the message
   * @param messageData the WBXML-based message data object to populate
   * @param <T> the message data type for the WBXML-based message data
   * @return the WBXML-based message data
   */
  public <T extends WbxmlMessageData> T fromMessage(Message message, T messageData)
      throws MessagingServiceException {
    byte[] data = message.getData();

    // Decrypt the message if required
    if (message.isEncrypted()) {
      data =
          decryptMessageData(
              encryptionKey,
              StringUtils.isEmpty(message.getEncryptionIV())
                  ? new byte[0]
                  : Base64Util.decode(message.getEncryptionIV()),
              message.getData());

      // Retrieve the SHA-256 hash of the unencrypted message data
      String dataHash = getMessageDataHash(data);

      if (!message.getDataHash().equals(dataHash)) {
        throw new MessagingServiceException(
            String.format(
                "Failed to decrypt the message data since the data hash for the message (%s) does not "
                    + "match the hash for the message data (%s)",
                message.getDataHash(), dataHash));
      }
    }

    // Check that the message type for the message data and the specified message match
    if (!messageData.getMessageTypeId().equals(message.getTypeId())) {
      throw new MessagingServiceException(
          String.format(
              "The message type for the message (%s) does not match the message type for the message "
                  + "data (%s)",
              message.getTypeId(), messageData.getMessageTypeId()));
    }

    /*
     * Populate the message data instance with the information contained in the WBXML data for the
     * message.
     */
    if (messageData.fromMessageData(data)) {
      return messageData;
    } else {
      throw new MessagingServiceException(
          String.format(
              "Failed to populate the instance of the message data class (%s) from the WBXML data for "
                  + "the message",
              messageData.getClass().getName()));
    }
  }

  /**
   * Returns the message containing the WBXML-based message data.
   *
   * @param messageData the WBXML-based message data
   * @return the message that can be sent via the messaging infrastructure
   */
  public Message toMessage(WbxmlMessageData messageData) throws MessagingServiceException {
    return toMessage(messageData, null);
  }

  /**
   * Returns the message containing the WBXML-based message data.
   *
   * @param messageData the WBXML-based message data
   * @param correlationId the Universally Unique Identifier (UUID) used to correlate the message
   * @return the message that can be sent via the messaging infrastructure
   */
  public Message toMessage(WbxmlMessageData messageData, UUID correlationId)
      throws MessagingServiceException {
    if (StringUtils.isEmpty(username)) {
      throw new MessagingServiceException(
          String.format(
              "Failed to create the message with type (%s): A username has not been specified",
              messageData.getMessageTypeId()));
    }

    if (deviceId == null) {
      throw new MessagingServiceException(
          String.format(
              "Failed to create the message with type (%s): A device ID has not been specified",
              messageData.getMessageTypeId()));
    }

    byte[] data = messageData.toMessageData();

    // Encrypt the message data
    if (encryptionKey != null) {
      // Retrieve the SHA-256 hash of the unencrypted message data
      String dataHash = getMessageDataHash(data);

      byte[] encryptionIV = CryptoUtil.createRandomEncryptionIV(CryptoUtil.AES_BLOCK_SIZE);

      data = encryptMessageData(encryptionKey, encryptionIV, data);

      return new Message(
          username,
          deviceId,
          messageData.getMessageTypeId(),
          correlationId,
          messageData.getMessageTypePriority(),
          data,
          dataHash,
          (encryptionIV.length == 0) ? "" : Base64Util.encodeBytes(encryptionIV));
    } else {
      return new Message(
          username,
          deviceId,
          messageData.getMessageTypeId(),
          correlationId,
          messageData.getMessageTypePriority(),
          data);
    }
  }

  /**
   * Generate the SHA-256 hash for the message data.
   *
   * @param data the message data to return the SHA-256 hash for
   * @return the SHA-256 hash for the message data
   */
  private String getMessageDataHash(byte[] data) throws MessagingServiceException {
    try {
      return Base64Util.encodeBytes(threadLocalMessageDigest.get().digest(data));
    } catch (Throwable e) {
      throw new MessagingServiceException(
          "Failed to generate the SHA-256 hash for the message data", e);
    }
  }
}
