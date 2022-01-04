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

import digital.inception.messaging.IMessagingService;

/**
 * The <b>MessageHandler</b> class provides the base class that all message handlers should be
 * derived from.
 *
 * @author Marcus Portmann
 */
@SuppressWarnings("WeakerAccess")
public abstract class MessageHandler implements IMessageHandler {

  /** The configuration information for the message handler. */
  private final MessageHandlerConfig messageHandlerConfig;

  /** The name of the message handler. */
  private final String name;

  /**
   * Returns the Messaging Service.
   *
   * @return the Messaging Service
   */
  public IMessagingService getMessagingService() {
    return messagingService;
  }

  /**
   * The Messaging Service.
   */
  private final IMessagingService messagingService;

  /**
   * Constructs a new <b>MessageHandler</b>.
   *
   * @param name the name of the message handler
   * @param messageHandlerConfig the configuration information for the message handler
   * @param messagingService the Messaging Service
   */
  public MessageHandler(
      String name, MessageHandlerConfig messageHandlerConfig, IMessagingService messagingService) {
    this.name = name;
    this.messageHandlerConfig = messageHandlerConfig;
    this.messagingService = messagingService;
  }

  /**
   * Returns the configuration information for the message handler.
   *
   * @return the configuration information for the message handler
   */
  @SuppressWarnings("unused")
  public MessageHandlerConfig getMessageHandlerConfig() {
    return messageHandlerConfig;
  }

  /**
   * Returns the name of the message handler.
   *
   * @return the name of the message handler
   */
  public String getName() {
    return name;
  }
}
