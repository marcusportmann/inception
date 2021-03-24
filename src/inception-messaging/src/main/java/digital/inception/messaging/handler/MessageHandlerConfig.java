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

package digital.inception.messaging.handler;

import java.util.ArrayList;
import java.util.List;

/**
 * The <b>MessageHandlerConfig</b> class stores the configuration information for a message handler.
 * This is the configuration read from the META-INF/MessagingConfig.xml configuration files on the
 * classpath.
 *
 * @author Marcus Portmann
 */
public class MessageHandlerConfig {

  /** The fully qualified name of the class that implements the message handler. */
  private final String className;

  /** The configuration information for the messages the handler is capable of processing. */
  private final List<MessageConfig> messagesConfig;

  /** The name of the message handler. */
  private String name;

  /**
   * Constructs a new <b>MessageHandlerConfig</b>.
   *
   * @param name the name of the message handler
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
   * @param messageType the code for the message type
   * @param isSynchronous is the handler capable of synchronously processing messages of the
   *     supported message type
   * @param isAsynchronous is the handler capable of asynchronously processing messages of the
   *     supported message type
   * @param isSecure should messages of the supported message type be processed securely i.e. should
   *     these messages be encrypted
   * @param isArchivable should messages of the supported message type be archived
   */
  public void addMessageConfig(
      String messageType,
      boolean isSynchronous,
      boolean isAsynchronous,
      boolean isSecure,
      boolean isArchivable) {
    messagesConfig.add(
        new MessageConfig(messageType, isSynchronous, isAsynchronous, isSecure, isArchivable));
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
   * @param messageType the code for the message type
   * @return <b>true</b> if messages of the specified message type should be archived or
   *     <b>false</b> otherwise
   */
  public boolean isArchivable(String messageType) {
    for (MessageConfig messageConfig : messagesConfig) {
      if (messageConfig.getMessageType().equals(messageType)) {
        return messageConfig.isArchivable();
      }
    }

    return false;
  }

  /**
   * Returns whether the messages of the specified message type should be processed securely.
   *
   * @param messageType the code for the message type
   * @return <b>true</b> if messages of the specified message type should be processed securely or
   *     <b>false</b> otherwise
   */
  public boolean isSecure(String messageType) {
    for (MessageConfig messageConfig : messagesConfig) {
      if (messageConfig.getMessageType().equals(messageType)) {
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
   * @param messageType the code for the message type
   * @return <b>true</b> if the message handler supports asynchronous processing of the specified
   *     message type or <b>false</b> otherwise
   */
  public boolean supportsAsynchronousProcessing(String messageType) {
    for (MessageConfig messageConfig : messagesConfig) {
      if (messageConfig.getMessageType().equals(messageType)) {
        return messageConfig.isAsynchronous();
      }
    }

    return false;
  }

  /**
   * Returns whether the message handler supports synchronous processing of the specified message
   * type.
   *
   * @param messageType the code for the message type
   * @return <b>true</b> if the message handler supports synchronous processing of the specified
   *     message type or <b>false</b> otherwise
   */
  public boolean supportsSynchronousProcessing(String messageType) {
    for (MessageConfig messageConfig : messagesConfig) {
      if (messageConfig.getMessageType().equals(messageType)) {
        return messageConfig.isSynchronous();
      }
    }

    return false;
  }

  /**
   * The <b>MessageConfig</b> inner class stores the configuration information for a a message that
   * a message handler is capable of processing.
   */
  public class MessageConfig {

    /** Should messages of the supported message type be archived? */
    private final boolean isArchivable;

    /**
     * Is the handler capable of asynchronously processing messages of the supported message type.
     */
    private final boolean isAsynchronous;

    /**
     * Should the messages of the supported message type be processed securely i.e. should these
     * messages be encrypted?
     */
    private final boolean isSecure;

    /**
     * Is the handler capable of synchronously processing messages of the supported message type.
     */
    private final boolean isSynchronous;

    /** The code for the message type. */
    private final String messageType;

    /**
     * Constructs a new <b>MessageConfig</b>.
     *
     * @param messageType the code for the message type
     * @param isSynchronous is the handler capable of synchronously processing messages of the
     *     supported message type
     * @param isAsynchronous is the handler capable of asynchronously processing messages of the
     *     supported message type
     * @param isSecure should messages of the supported message type be processed securely i.e.
     *     should these messages be encrypted
     * @param isArchivable should messages of the supported message type be archived
     */
    MessageConfig(
        String messageType,
        boolean isSynchronous,
        boolean isAsynchronous,
        boolean isSecure,
        boolean isArchivable) {
      this.messageType = messageType;
      this.isSynchronous = isSynchronous;
      this.isAsynchronous = isAsynchronous;
      this.isSecure = isSecure;
      this.isArchivable = isArchivable;
    }

    /**
     * Return the code for the message type.
     *
     * @return the code for the message type
     */
    public String getMessageType() {
      return messageType;
    }

    /**
     * Returns whether the messages of the supported message type should be archived.
     *
     * @return <b>true</b> if messages of the supported message type should be archived or
     *     <b>false</b> otherwise
     */
    public boolean isArchivable() {
      return isArchivable;
    }

    /**
     * Returns whether the handler is capable of asynchronously processing messages of the supported
     * message type.
     *
     * @return <b>true</b> if the handler is capable of asynchronously processing messages of the
     *     supported message type or <b>false</b> otherwise
     */
    public boolean isAsynchronous() {
      return isAsynchronous;
    }

    /**
     * Returns whether the messages of the supported message type should be processed securely.
     *
     * @return <b>true</b> if the messages of the supported message type should be processed
     *     securely or <b>false</b> otherwise
     */
    public boolean isSecure() {
      return isSecure;
    }

    /**
     * Returns whether the handler is capable of synchronously processing messages of the supported
     * message type.
     *
     * @return <b>true</b> if the handler is capable of synchronously processing messages of the
     *     supported message type or <b>false</b> otherwise
     */
    public boolean isSynchronous() {
      return isSynchronous;
    }
  }
}
