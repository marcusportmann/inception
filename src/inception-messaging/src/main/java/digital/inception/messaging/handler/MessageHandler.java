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

package digital.inception.messaging.handler;

/**
 * The <code>MessageHandler</code> class provides the base class that all message handlers should
 * be derived from.
 *
 * @author Marcus Portmann
 */
public abstract class MessageHandler
  implements IMessageHandler
{
  /**
   * The configuration information for the message handler.
   */
  private MessageHandlerConfig messageHandlerConfig;

  /**
   * The name of the message handler.
   */
  private String name;

  /**
   * Constructs a new <code>MessageHandler</code>.
   *
   * @param name                 the name of the message handler
   * @param messageHandlerConfig the configuration information for the message handler
   */
  public MessageHandler(String name, MessageHandlerConfig messageHandlerConfig)
  {
    this.name = name;
    this.messageHandlerConfig = messageHandlerConfig;
  }

  /**
   * Returns the configuration information for the message handler.
   *
   * @return the configuration information for the message handler
   */
  @SuppressWarnings("unused")
  public MessageHandlerConfig getMessageHandlerConfig()
  {
    return messageHandlerConfig;
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
}
