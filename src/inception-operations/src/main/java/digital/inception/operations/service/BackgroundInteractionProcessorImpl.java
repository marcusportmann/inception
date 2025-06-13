///*
// * Copyright Marcus Portmann
// *
// * Licensed under the Apache License, Version 2.0 (the "License");
// * you may not use this file except in compliance with the License.
// * You may obtain a copy of the License at
// *
// *   http://www.apache.org/licenses/LICENSE-2.0
// *
// * Unless required by applicable law or agreed to in writing, software
// * distributed under the License is distributed on an "AS IS" BASIS,
// * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// * See the License for the specific language governing permissions and
// * limitations under the License.
// */
//
//package digital.inception.operations.service;
//
//import digital.inception.operations.model.Interaction;
//import digital.inception.operations.model.InteractionStatus;
//import jakarta.annotation.PostConstruct;
//import java.util.Optional;
//import java.util.concurrent.LinkedBlockingQueue;
//import java.util.concurrent.ThreadPoolExecutor;
//import java.util.concurrent.TimeUnit;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.SmartLifecycle;
//import org.springframework.scheduling.annotation.Async;
//import org.springframework.scheduling.annotation.Scheduled;
//import org.springframework.stereotype.Component;
//
///**
// * The {@code BackgroundInteractionProcessorImpl} class implements the Background Interaction Processor.
// *
// * @author Marcus Portmann
// */
//@Component
//@SuppressWarnings("unused")
//public class BackgroundInteractionProcessorImpl implements BackgroundInteractionProcessor, SmartLifecycle {
//
//  /* Logger */
//  private static final Logger log = LoggerFactory.getLogger(BackgroundInteractionProcessorImpl.class);
//
//  /** The Interaction Service. */
//  private final InteractionService interactionService;
//
//  /** The number of interaction processing threads to start initially. */
//  @Value("${inception.operations.initial-interaction-processing-threads:#{1}}")
//  private int initialInteractionProcessingThreads;
//
//  /** Is the Background Interaction Processor running. */
//  private boolean isRunning;
//
//  /**
//   * The maximum number of interactions to queue for processing if no interaction processing threads are available.
//   */
//  @Value("${inception.operations.maximum-interaction-processing-queue-length:#{50}}")
//  private int maximumInteractionProcessingQueueLength;
//
//  /** The maximum number of interaction processing threads to create to execute tasks. */
//  @Value("${inception.operations.maximum-interaction-processing-threads:#{10}}")
//  private int maximumInteractionProcessingThreads;
//
//  private LinkedBlockingQueue<Runnable> interactionProcessingQueue;
//
//  /** The number of minutes an idle interaction processing thread should be kept alive. */
//  @Value("${inception.operations.interaction-processing-thread-keep-alive:#{5}}")
//  private int interactionProcessingThreadKeepAlive;
//
//  /** The executor responsible for processing interactions. */
//  private ThreadPoolExecutor interactionProcessor;
//
//  /**
//   * Constructs a new {@code BackgroundInteractionProcessorImpl}.
//   *
//   * @param interactionService the Interaction Service
//   */
//  public BackgroundInteractionProcessorImpl(InteractionService interactionService) {
//    this.interactionService = interactionService;
//  }
//
//  /** Process the interactions. */
//  @Scheduled(cron = "0 * * * * *")
//  @Async
//  public void processInteractions() {
//    Optional<Interaction> interactionOptional;
//
//    if (interactionService == null) {
//      return;
//    }
//
//    while (isRunning) {
//      // Retrieve the next interaction queued for processing
//      try {
//        if (interactionProcessor.getQueue().remainingCapacity() == 0) {
//          if (log.isDebugEnabled()) {
//            log.debug(
//                "The maximum number of interactions queued for processing has been reached ("
//                    + maximumInteractionProcessingQueueLength
//                    + ")");
//          }
//          return;
//        }
//
//        interactionOptional = interactionService.getNextInteractionQueuedForProcessing();
//
//        if (interactionOptional.isEmpty()) {
//          if (log.isDebugEnabled()) {
//            log.debug("No interactions queued for processing");
//          }
//
//          return;
//        }
//      } catch (Throwable e) {
//        log.error("Failed to retrieve the next interaction queued for processing", e);
//        return;
//      }
//
//      interactionProcessor.execute(new InteractionProcessor(interactionService, interactionOptional.get()));
//    }
//  }
//
//  /** Initialize the Background Interaction Processor. */
//  @PostConstruct
//  public void init() {
//    log.info("Initializing the Background Interaction Processor");
//
//    if (interactionService != null) {
//      // Initialize the interaction processor
//      interactionProcessingQueue = new LinkedBlockingQueue<>(maximumInteractionProcessingQueueLength);
//
//      // NOTE: We set the initial number of threads to the maximum number of threads because
//      //       the implementation of the thread pool executor will never increase the number of
//      //       threads if the queue is not full.
//      //
//      // https://medium.com/@ankithahjpgowda/policies-of-threadpoolexecutor-in-java-75f22fd6f637
//      this.interactionProcessor =
//          new ThreadPoolExecutor(
//              maximumInteractionProcessingThreads,
//              maximumInteractionProcessingThreads,
//              interactionProcessingThreadKeepAlive,
//              TimeUnit.MINUTES,
//              interactionProcessingQueue);
//
//      // Reset any locks for interactions that were previously being processed
//      try {
//        log.info("Resetting the locks for the interactions being processed");
//
//        interactionService.resetInteractionLocks(InteractionStatus.PROCESSING, InteractionStatus.RECEIVED);
//      } catch (Throwable e) {
//        log.error("Failed to reset the locks for the interactions being processed", e);
//      }
//    } else {
//      log.error(
//          "Failed to initialize the Background Interaction Processor: "
//              + "The Interaction Service was NOT injected");
//    }
//  }
//
//  @Override
//  public boolean isRunning() {
//    return isRunning || interactionProcessor.isTerminating();
//  }
//
//  @Override
//  public void start() {
//    log.info("Starting the Background Interaction Processor");
//    isRunning = true;
//  }
//
//  /** The number of milliseconds to wait for the processing of an interaction to complete. */
//  @Value("${inception.operations.interaction-processing-timeout:#{60000L}}")
//  private long interactionProcessingTimeout;
//
//
//  @Override
//  public void stop() {
//    long terminationTimeout =
//        Math.max(
//            5 * 60000L,
//            (interactionProcessor.getActiveCount() + interactionProcessor.getQueue().size())
//                * interactionProcessingTimeout);
//
//    log.info(
//        "Shutting down the Background Interaction Processor with "
//            + interactionProcessor.getActiveCount()
//            + " interactions currently being processed and "
//            + interactionProcessor.getQueue().size()
//            + " interactions queued for processing (Timeout is "
//            + terminationTimeout
//            + " milliseconds)");
//
//    isRunning = false;
//
//    try {
//      interactionProcessor.shutdown();
//
//      if (interactionProcessor.awaitTermination(terminationTimeout, TimeUnit.MILLISECONDS)) {
//        log.info("Successfully shutdown the Background Interaction Processor");
//      } else {
//        log.warn("Failed to cleanly shutdown the Background Interaction Processor");
//      }
//    } catch (InterruptedException e) {
//      log.warn("The shutdown of the Background Interaction Processor was interrupted");
//    }
//  }
//
//  /**
//   * The {@code InteractionProcessor} class.
//   *
//   * @author Marcus Portmann
//   */
//  public static class InteractionProcessor implements Runnable {
//
//    /** The Interaction Service. */
//    private final InteractionService interactionService;
//
//    /** The interaction. */
//    private final Interaction interaction;
//
//    /**
//     * Constructs a new {@code InteractionProcessor}.
//     *
//     * @param interactionService the Interaction Service
//     * @param task the task
//     */
//    public InteractionProcessor(ExecutorService interactionService, Task task) {
//      this.interactionService = interactionService;
//      this.task = task;
//    }
//
//    @Override
//    public void run() {
//      try {
//        if (log.isDebugEnabled()) {
//          log.debug("Executing the task (%s)".formatted(task.getId()));
//        }
//
//        long startTime = System.currentTimeMillis();
//
//        TaskExecutionResult taskExecutionResult = interactionService.executeTask(task);
//
//        long finishTime = System.currentTimeMillis();
//
//        // Complete the task or advance the task to the next step in the case of a multistep task.
//        try {
//          interactionService.completeTask(task, taskExecutionResult, finishTime - startTime);
//        } catch (Throwable e) {
//          log.error("Failed to complete the task (%s)".formatted(task.getId()), e);
//
//          try {
//            interactionService.unlockTask(task.getId(), TaskStatus.FAILED);
//          } catch (Throwable f) {
//            log.error(
//                "Failed to unlock and set the status for the task (%s) to FAILED"
//                    .formatted(task.getId()),
//                f);
//          }
//        }
//      } catch (TaskExecutionRetryableException e) {
//        try {
//          interactionService.requeueTask(task);
//        } catch (Throwable f) {
//          log.error("Failed to requeue the task (%s)".formatted(task.getId()), f);
//        }
//      } catch (TaskExecutionDelayedException e) {
//        try {
//          interactionService.delayTask(task, e.getDelay());
//        } catch (Throwable f) {
//          log.error(
//              "Failed to delay the task (%s) by %d milliseconds"
//                  .formatted(task.getId(), e.getDelay()),
//              f);
//        }
//      } catch (Throwable e) {
//        log.error("Failed to execute the task (%s)".formatted(task.getId()), e);
//
//        // Fail the task
//        try {
//          interactionService.failTask(task);
//        } catch (Throwable f) {
//          log.error("Failed to fail the task (%s)".formatted(task.getId()), f);
//
//          try {
//            interactionService.unlockTask(task.getId(), TaskStatus.FAILED);
//          } catch (Throwable g) {
//            log.error(
//                "Failed to unlock and set the status for the task (%s) to FAILED"
//                    .formatted(task.getId()),
//                g);
//          }
//        }
//      }
//    }
//  }
//}
//
