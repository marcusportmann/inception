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

import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

/**
 * The <code>BackgroundMessageAssembler</code> class implements the Background Message Assembler.
 *
 * @author Marcus Portmann
 */
@Service
@SuppressWarnings("unused")
public class BackgroundMessageAssembler implements InitializingBean {

  /* Logger */
  private static final Logger logger = LoggerFactory.getLogger(BackgroundMessageAssembler.class);

  /** The Messaging Service. */
  private IMessagingService messagingService;

  /**
   * Constructs a new <code>BackgroundMessageAssembler</code>.
   *
   * @param messagingService the Messaging Service
   */
  public BackgroundMessageAssembler(IMessagingService messagingService) {
    this.messagingService = messagingService;
  }

  /** Initialize the Background Message Assembler. */
  @Override
  public void afterPropertiesSet() {
    logger.info("Initializing the Background Message Assembler");

    if (messagingService != null) {
      /*
       * Reset any locks for messages parts that were previously being assembled by the Background
       * Message Assembler.
       */
      try {
        logger.info("Resetting the message part locks for the message parts being assembled");

        messagingService.resetMessagePartLocks(
            MessagePartStatus.ASSEMBLING, MessagePartStatus.QUEUED_FOR_ASSEMBLY);
      } catch (Throwable e) {
        logger.error(
            "Failed to reset the message part locks for the message parts being assembled", e);
      }
    } else {
      logger.error(
          "Failed to initialize the Background Message Assembler: "
              + "The Messaging Service was NOT injected");
    }
  }

  /**
   * Assemble the message from the message parts that have been queued for assembly.
   *
   * @param messageId the Universally Unique Identifier (UUID) uniquely identifying the message
   * @param totalParts the total number of parts for the message
   */
  @Async
  public void assembleMessage(UUID messageId, int totalParts) {
    try {
      messagingService.assembleMessage(messageId, totalParts);
    } catch (Throwable e) {
      logger.error("Failed to assemble the message parts for the message (" + messageId + ")", e);

      /*
       * Reset the status for the message parts for the message to QueuedForAssembly so that the
       * assembly of the message can be retried.
       */
    }
  }

  /** Assemble the messages. */
  @Scheduled(cron = "0 * * * * *")
  @Async
  public void assembleMessages() {
    // TODO: Retrieve a list of messages that have message parts that are queued for assembly

    // TODO: Retrieve the total number of message parts for each message

    // TODO: Attempt to assemble each message
  }
}
