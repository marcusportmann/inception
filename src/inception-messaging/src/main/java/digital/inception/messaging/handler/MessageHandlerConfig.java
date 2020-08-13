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

package digital.inception.messaging.handler;

// ~--- JDK imports ------------------------------------------------------------

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * The <code>MessageHandlerConfig</code> class stores the configuration information for a message
 * handler. This is the configuration read from the META-INF/MessagingConfig.xml configuration files
 * on the classpath.
 *
 * @author Marcus Portmann
 */
public class MessageHandlerConfig {

  /**
   * The fully qualified name of the class that implements the message handler.
   */
  private String className;

  /**
   * The configuration information for the messages the handler is capable of processing.
   */
  private List<MessageConfig> messagesConfig;

  /**
   * The name of the message handler.
   */
  private String name;

  /**
   * Constructs a new <code>MessageHandlerConfig</code>.
   *
   * @param name      the name of the message handler
   * @param className fully qualified name of the class that implements the message handler
   */
  public MessageHandlerConfig(String name, String className) {
    this.name = name;
    this.className = className;
    this.messagesConfig = new ArrayList<>();
  }

  /**
   * Add the message configuration to the message handler configuration. The message configuration
   * defines which messages a message handler is capable of processing synchronously and
   * asynchronously.
   *
   * @param messageTypeId  the Universally Unique Identifier (UUID) uniquely identifying the message
   *                       type
   * @param isSynchronous  is the handler capable of synchronously processing messages of the
   *                       supported message type
   * @param isAsynchronous is the handler capable of asynchronously processing messages of the
   *                       supported message type
   * @param isSecure       should messages of the supported message type be processed securely i.e.
   *                       should these messages be encrypted
   * @param isArchivable   should messages of the supported message type be archived
   */
  public void addMessageConfig(
      UUID messageTypeId,
      boolean isSynchronous,
      boolean isAsynchronous,
      boolean isSecure,
      boolean isArchivable) {
    messagesConfig.add(
        new MessageConfig(messageTypeId, isSynchronous, isAsynchronous, isSecure, isArchivable));
  }

  /**
   * Return the fully qualified name of the class that implements the message handler.
   *
   * @return the fully qualified name of the class that implements the message handler
   */
  public String getClassName() {
    return className;
  }

  /**
   * Returns the configuration information for the messages the handler is capable of processing.
   *
   * @return configuration information for the messages the handler is capable of processing
   */
  public List<MessageConfig> getMessagesConfig() {
    return messagesConfig;
  }

  /**
   * Returns the name of the message handler.
   *
   * @return the name of the message handler
   */
  public String getName() {
    return name;
  }

  /**
   * Returns whether the messages of the specified message type should be archived.
   *
   * @param messageTypeId the Universally Unique Identifier (UUID) uniquely identifying the message
   *                      type
   *
   * @return <code>true</code> if messages of the specified message type should be archived or
   * <code>false</code> otherwise
   */
  public boolean isArchivable(UUID messageTypeId) {
    for (MessageConfig messageConfig : messagesConfig) {
      if (messageConfig.getMessageTypeId().equals(messageTypeId)) {
        return messageConfig.isArchivable();
      }
    }

    return false;
  }

  /**
   * Returns whether the messages of the specified message type should be processed securely.
   *
   * @param messageTypeId the Universally Unique Identifier (UUID) uniquely identifying the message
   *                      type
   *
   * @return <code>true</code> if messages of the specified message type should be processed
   * securely or <code>false</code> otherwise
   */
  public boolean isSecure(UUID messageTypeId) {
    for (MessageConfig messageConfig : messagesConfig) {
      if (messageConfig.getMessageTypeId().equals(messageTypeId)) {
        return messageConfig.isSecure();
      }
    }

    return false;
  }

  /**
   * Set the name of the message handler.
   *
   * @param name the name of the message handler
   */
  public void setName(String name) {
    this.name = name;
  }

  /**
   * Returns whether the message handler supports asynchronous processing of the specified message
   * type.
   *
   * @param messageTypeId the Universally Unique Identifier (UUID) uniquely identifying the message
   *                      type
   *
   * @return <code>true</code> if the message handler supports asynchronous processing of the
   * specified message type or <code>false</code> otherwise
   */
  public boolean supportsAsynchronousProcessing(UUID messageTypeId) {
    for (MessageConfig messageConfig : messagesConfig) {
      if (messageConfig.getMessageTypeId().equals(messageTypeId)) {
        return messageConfig.isAsynchronous();
      }
    }

    return false;
  }

  /**
   * Returns whether the message handler supports synchronous processing of the specified message
   * type.
   *
   * @param messageTypeId the Universally Unique Identifier (UUID) uniquely identifying the message
   *                      type
   *
   * @return <code>true</code> if the message handler supports synchronous processing of the
   * specified message type or <code>false</code> otherwise
   */
  public boolean supportsSynchronousProcessing(UUID messageTypeId) {
    for (MessageConfig messageConfig : messagesConfig) {
      if (messageConfig.getMessageTypeId().equals(messageTypeId)) {
        return messageConfig.isSynchronous();
      }
    }

    return false;
  }

  /**
   * The <code>MessageConfig</code> inner class stores the configuration information for a a message
   * that a message handler is capable of processing.
   */
  public class MessageConfig {

    /**
     * Should messages of the supported message type be archived?
     */
    private boolean isArchivable;

    /**
     * Is the handler capable of asynchronously processing messages of the supported message type.
     */
    private boolean isAsynchronous;

    /**
     * Should the messages of the supported message type be processed securely i.e. should these
     * messages be encrypted?
     */
    private boolean isSecure;

    /**
     * Is the handler capable of synchronously processing messages of the supported message type.
     */
    private boolean isSynchronous;

    /**
     * The Universally Unique Identifier (UUID) uniquely identifying the message type.
     */
    private UUID messageTypeId;

    /**
     * Constructs a new <code>MessageConfig</code>.
     *
     * @param messageTypeId  the Universally Unique Identifier (UUID) uniquely identifying the
     *                       message type
     * @param isSynchronous  is the handler capable of synchronously processing messages of the
     *                       supported message type
     * @param isAsynchronous is the handler capable of asynchronously processing messages of the
     *                       supported message type
     * @param isSecure       should messages of the supported message type be processed securely
     *                       i.e. should these messages be encrypted
     * @param isArchivable   should messages of the supported message type be archived
     */
    MessageConfig(
        UUID messageTypeId,
        boolean isSynchronous,
        boolean isAsynchronous,
        boolean isSecure,
        boolean isArchivable) {
      this.messageTypeId = messageTypeId;
      this.isSynchronous = isSynchronous;
      this.isAsynchronous = isAsynchronous;
      this.isSecure = isSecure;
      this.isArchivable = isArchivable;
    }

    /**
     * Return the Universally Unique Identifier (UUID) uniquely identifying the message type.
     *
     * @return the Universally Unique Identifier (UUID) uniquely identifying the message type
     */
    public UUID getMessageTypeId() {
      return messageTypeId;
    }

    /**
     * Returns whether the messages of the supported message type should be archived.
     *
     * @return <code>true</code> if messages of the supported message type should be archived or
     * <code>false</code> otherwise
     */
    public boolean isArchivable() {
      return isArchivable;
    }

    /**
     * Returns whether the handler is capable of asynchronously processing messages of the supported
     * message type.
     *
     * @return <code>true</code> if the handler is capable of asynchronously processing messages of
     * the supported message type or <code>false</code> otherwise
     */
    public boolean isAsynchronous() {
      return isAsynchronous;
    }

    /**
     * Returns whether the messages of the supported message type should be processed securely.
     *
     * @return <code>true</code> if the messages of the supported message type should be processed
     * securely or <code>false</code> otherwise
     */
    public boolean isSecure() {
      return isSecure;
    }

    /**
     * Returns whether the handler is capable of synchronously processing messages of the supported
     * message type.
     *
     * @return <code>true</code> if the handler is capable of synchronously processing messages of
     * the supported message type or <code>false</code> otherwise
     */
    public boolean isSynchronous() {
      return isSynchronous;
    }
  }
}
