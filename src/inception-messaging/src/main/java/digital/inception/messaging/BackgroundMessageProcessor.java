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

package digital.inception.messaging;

//~--- non-JDK imports --------------------------------------------------------

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

/**
 * The <code>BackgroundMessageProcessor</code> class implements the Background Message Processor.
 *
 * @author Marcus Portmann
 */
@Service
@SuppressWarnings("unused")
public class BackgroundMessageProcessor
  implements InitializingBean
{
  /* Logger */
  private static Logger logger = LoggerFactory.getLogger(BackgroundMessageProcessor.class);

  /* Messaging Service */
  @Autowired
  private IMessagingService messagingService;

  /**
   * Initialize the Background Message Processor.
   *
   * @throws Exception
   */
  @Override
  public void afterPropertiesSet()
    throws Exception
  {
    logger.info("Initializing the Background Message Processor");

    if (messagingService != null)
    {
      /*
       * Reset any locks for messages that were previously being processed by the Background
       * Message Processor.
       */
      try
      {
        logger.info("Resetting the message locks for the messages being processed");

        messagingService.resetMessageLocks(MessageStatus.PROCESSING, MessageStatus
            .QUEUED_FOR_PROCESSING);
      }
      catch (Throwable e)
      {
        logger.error("Failed to reset the message locks for the messages being processed", e);
      }
    }
    else
    {
      logger.error("Failed to initialize the Background Message Processor: "
          + "The Messaging Service was NOT injected");
    }
  }

  /**
   * Process the messages.
   */
  @Scheduled(cron = "0 * * * * *")
  @Async
  public void processMessages()
  {
    Message message;

    while (true)
    {
      // Retrieve the next message queued for processing
      try
      {
        message = messagingService.getNextMessageQueuedForProcessing();

        if (message == null)
        {
          if (logger.isDebugEnabled())
          {
            logger.debug("No messages queued for processing");
          }

          return;
        }
      }
      catch (Throwable e)
      {
        logger.error("Failed to retrieve the next message queued for processing", e);

        return;
      }

      // Process the asynchronous message
      try
      {
        if (logger.isDebugEnabled())
        {
          logger.debug(String.format("Processing the queued message (%s)%s  %s", message.getId(),
              System.getProperty("line.separator"), message.toString()));
        }

        Message responseMessage = messagingService.processMessage(message);

        if (responseMessage != null)
        {
          messagingService.queueMessageForDownload(responseMessage);
        }

        // Remove the processed message from the queue
        messagingService.deleteMessage(message);
      }
      catch (Throwable e)
      {
        logger.error(String.format("Failed to process the queued message (%s)", message.getId()),
            e);

        // Increment the processing attempts for the message
        try
        {
          messagingService.incrementMessageProcessingAttempts(message);

          message.setProcessAttempts(message.getProcessAttempts() + 1);
        }
        catch (Throwable f)
        {
          logger.error(String.format(
              "Failed to increment the processing attempts for the queued message (%s)",
              message.getId()), f);
        }

        try
        {
          /*
           * If the message has exceeded the maximum number of processing attempts then unlock it
           * and set its status to "Failed" otherwise unlock it and set its status to
           * "QueuedForProcessing".
           */
          if (message.getProcessAttempts() >= messagingService.getMaximumProcessingAttempts())
          {
            logger.warn(String.format(
                "The queued message (%s) has exceeded the maximum number of processing attempts "
                + "and will be marked as \"Failed\"", message.getId()));

            messagingService.unlockMessage(message, MessageStatus.FAILED);
          }
          else
          {
            messagingService.unlockMessage(message, MessageStatus.QUEUED_FOR_PROCESSING);
          }
        }
        catch (Throwable f)
        {
          logger.error(String.format(
              "Failed to unlock and set the status for the queued message (%s)", message.getId()),
              f);
        }
      }
    }
  }
}
