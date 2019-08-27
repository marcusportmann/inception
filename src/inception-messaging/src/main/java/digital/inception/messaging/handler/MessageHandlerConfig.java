/*
 * Copyright 2019 Marcus Portmann
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

//~--- JDK imports ------------------------------------------------------------

import java.util.ArrayList;
import java.util.List;

/**
 * The <code>MessageHandlerConfig</code> class stores the configuration information for a
 * message handler. This is the configuration read from the META-INF/MessagingConfig.xml
 * configuration files on the classpath.
 *
 * @author Marcus Portmann
 */
public class MessageHandlerConfig
{
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
  public MessageHandlerConfig(String name, String className)
  {
    this.name = name;
    this.className = className;
    this.messagesConfig = new ArrayList<>();
  }

  /**
   * Add the message configuration to the message handler configuration. The message configuration
   * defines which messages a message handler is capable of processing synchronously and
   * asynchronously.
   *
   * @param messageTypeId  the ID used to uniquely identify the message type
   * @param isSynchronous  is the handler capable of synchronously processing messages of the
   *                       supported message type
   * @param isAsynchronous is the handler capable of asynchronously processing messages of the
   *                       supported message type
   * @param isArchivable   should messages of the supported message type be archived
   */
  public void addMessageConfig(String messageTypeId, boolean isSynchronous, boolean isAsynchronous,
      boolean isArchivable)
  {
    messagesConfig.add(new MessageConfig(messageTypeId, isSynchronous, isAsynchronous,
        isArchivable));
  }

  /**
   * Return the fully qualified name of the class that implements the message handler.
   *
   * @return the fully qualified name of the class that implements the message handler
   */
  public String getClassName()
  {
    return className;
  }

  /**
   * Returns the configuration information for the messages the handler is capable of processing.
   *
   * @return configuration information for the messages the handler is capable of processing
   */
  public List<MessageConfig> getMessagesConfig()
  {
    return messagesConfig;
  }

  /**
   * Returns the name of the message handler.
   *
   * @return the name of the message handler
   */
  public String getName()
  {
    return name;
  }

  /**
   * Returns <code>true</code> if messages of the specified message type should be archived or
   * <code>false</code> otherwise.
   *
   * @param messageTypeId the ID used to uniquely identify the message type
   *
   * @return <code>true</code> if messages of the specified message type should be archived or
   *         <code>false</code> otherwise
   */
  public boolean isArchivable(String messageTypeId)
  {
    for (MessageConfig messageConfig : messagesConfig)
    {
      if (messageConfig.getMessageTypeId().equals(messageTypeId))
      {
        return messageConfig.isArchivable();
      }
    }

    return false;
  }

  /**
   * Set the name of the message handler.
   *
   * @param name the name of the message handler
   */
  public void setName(String name)
  {
    this.name = name;
  }

  /**
   * Returns <code>true</code> if the message handler supports asynchronous processing of the
   * specified message type or <code>false</code> otherwise.
   *
   * @param messageTypeId the ID used to uniquely identify the message type
   *
   * @return <code>true</code> if the message handler supports asynchronous processing of the
   *         specified message type or <code>false</code> otherwise
   */
  public boolean supportsAsynchronousProcessing(String messageTypeId)
  {
    for (MessageConfig messageConfig : messagesConfig)
    {
      if (messageConfig.getMessageTypeId().equals(messageTypeId))
      {
        return messageConfig.isAsynchronous();
      }
    }

    return false;
  }

  /**
   * Returns <code>true</code> if the message handler supports synchronous processing of the
   * specified message type or <code>false</code> otherwise.
   *
   * @param messageTypeId the ID used to uniquely identify the message type
   *
   * @return <code>true</code> if the message handler supports synchronous processing of the
   *         specified message type or <code>false</code> otherwise
   */
  public boolean supportsSynchronousProcessing(String messageTypeId)
  {
    for (MessageConfig messageConfig : messagesConfig)
    {
      if (messageConfig.getMessageTypeId().equals(messageTypeId))
      {
        return messageConfig.isSynchronous();
      }
    }

    return false;
  }

  /**
   * The <code>MessageConfig</code> inner class stores the configuration information for a
   * a message that a message handler is capable of processing.
   */
  public static class MessageConfig
  {
    /**
     * Should messages of the supported message type be archived?
     */
    private boolean isArchivable;

    /**
     * Is the handler capable of asynchronously processing messages of the supported message type.
     */
    private boolean isAsynchronous;

    /**
     * Is the handler capable of synchronously processing messages of the supported message type.
     */
    private boolean isSynchronous;

    /**
     * The ID used to uniquely identify the message type.
     */
    private String messageTypeId;

    /**
     * Constructs a new <code>MessageConfig</code>.
     *
     * @param messageTypeId  the ID used to uniquely identify the message type
     * @param isSynchronous  is the handler capable of synchronously processing messages of
     *                       the supported message type
     * @param isAsynchronous is the handler capable of asynchronously processing messages of
     *                       the supported message type
     * @param isArchivable   should messages of the supported message type be archived
     */
    MessageConfig(String messageTypeId, boolean isSynchronous, boolean isAsynchronous,
        boolean isArchivable)
    {
      this.messageTypeId = messageTypeId;
      this.isSynchronous = isSynchronous;
      this.isAsynchronous = isAsynchronous;
      this.isArchivable = isArchivable;
    }

    /**
     * Return the ID used to uniquely identify the message type.
     *
     * @return the ID used to uniquely identify the message type
     */
    public String getMessageTypeId()
    {
      return messageTypeId;
    }

    /**
     * Returns <code>true</code> if the handler is capable of asynchronously processing messages of
     * the supported message type or <code>false</code> otherwise.
     *
     * @return <code>true</code> if the handler is capable of asynchronously processing messages of
     *         the supported message type or <code>false</code> otherwise
     */
    public boolean isAsynchronous()
    {
      return isAsynchronous;
    }

    /**
     * Returns <code>true</code> if the handler is capable of synchronously processing messages of
     * the supported message type or <code>false</code> otherwise.
     *
     * @return <code>true</code> if the handler is capable of synchronously processing messages of
     *         the supported message type or <code>false</code> otherwise
     */
    public boolean isSynchronous()
    {
      return isSynchronous;
    }

    /**
     * Returns <code>true</code> if messages of the supported message type should be archived or
     * <code>false</code> otherwise.
     *
     * @return <code>true</code> if messages of the supported message type should be archived or
     *         <code>false</code> otherwise
     */
    boolean isArchivable()
    {
      return isArchivable;
    }
  }
}
