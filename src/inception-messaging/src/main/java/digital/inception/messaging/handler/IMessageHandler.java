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

//~--- non-JDK imports --------------------------------------------------------

import digital.inception.messaging.Message;

/**
 * The <code>IMessageHandler</code> interface defines the interface that must be implemented by all
 * message handlers.
 *
 * @author Marcus Portmann
 */
public interface IMessageHandler {

  /**
   * Returns the name of the message handler.
   *
   * @return the name of the message handler
   */
  String getName();

  /**
   * Process the specified message.
   *
   * @param message the message to process
   *
   * @return the response message or <code>null</code> if no response message exists
   */
  Message processMessage(Message message)
      throws MessageHandlerException;
}
