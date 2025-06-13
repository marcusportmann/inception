/*
 * Copyright Marcus Portmann
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

package digital.inception.executor.service;

import digital.inception.executor.exception.TaskExecutionDelayedException;
import digital.inception.executor.exception.TaskExecutionRetryableException;
import digital.inception.executor.model.Task;
import digital.inception.executor.model.TaskExecutionResult;
import digital.inception.executor.model.TaskStatus;
import jakarta.annotation.PostConstruct;
import java.util.Optional;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.SmartLifecycle;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * The {@code BackgroundTaskExecutorImpl} class implements the Background Task Executor.
 *
 * @author Marcus Portmann
 */
@Component
@SuppressWarnings("unused")
public class BackgroundTaskExecutorImpl implements BackgroundTaskExecutor, SmartLifecycle {

  /* Logger */
  private static final Logger log = LoggerFactory.getLogger(BackgroundTaskExecutorImpl.class);

  /** The Executor Service. */
  private final ExecutorService executorService;

  /** The number of task execution threads to start initially. */
  @Value("${inception.executor.initial-task-execution-threads:#{1}}")
  private int initialTaskExecutionThreads;

  /** Is the Background Task Executor running. */
  private boolean isRunning;

  /**
   * The maximum number of tasks to queue for execution if no task execution threads are available.
   */
  @Value("${inception.executor.maximum-task-execution-queue-length:#{50}}")
  private int maximumTaskExecutionQueueLength;

  /** The maximum number of task execution threads to create to execute tasks. */
  @Value("${inception.executor.maximum-task-execution-threads:#{10}}")
  private int maximumTaskExecutionThreads;

  /** The number of milliseconds to wait for an executing or queued task to complete. */
  @Value("${inception.executor.task-completion-timeout:#{60000L}}")
  private long taskCompletionTimeout;

  private LinkedBlockingQueue<Runnable> taskExecutionQueue;

  /** The number of minutes an idle task execution thread should be kept alive. */
  @Value("${inception.executor.task-execution-thread-keep-alive:#{5}}")
  private int taskExecutionThreadKeepAlive;

  /** The executor responsible for executing tasks. */
  private ThreadPoolExecutor taskExecutor;

  /**
   * Constructs a new {@code BackgroundTaskExecutorImpl}.
   *
   * @param executorService the Executor Service
   */
  public BackgroundTaskExecutorImpl(ExecutorService executorService) {
    this.executorService = executorService;
  }

  /** Execute the tasks. */
  @Scheduled(cron = "0 * * * * *")
  @Async
  public void executeTasks() {
    Optional<Task> taskOptional;

    if (executorService == null) {
      return;
    }

    while (isRunning) {
      // Retrieve the next task queued for execution
      try {
        if (taskExecutor.getQueue().remainingCapacity() == 0) {
          if (log.isDebugEnabled()) {
            log.debug(
                "The maximum number of tasks queued for execution has been reached ("
                    + maximumTaskExecutionQueueLength
                    + ")");
          }
          return;
        }

        taskOptional = executorService.getNextTaskQueuedForExecution();

        if (taskOptional.isEmpty()) {
          if (log.isDebugEnabled()) {
            log.debug("No tasks queued for execution");
          }

          return;
        }
      } catch (Throwable e) {
        log.error("Failed to retrieve the next task queued for execution", e);
        return;
      }

      taskExecutor.execute(new TaskExecutor(executorService, taskOptional.get()));
    }
  }

  /** Initialize the Background Task Executor. */
  @PostConstruct
  public void init() {
    log.info("Initializing the Background Task Executor");

    if (executorService != null) {
      // Initialize the task executor
      taskExecutionQueue = new LinkedBlockingQueue<>(maximumTaskExecutionQueueLength);

      // NOTE: We set the initial number of threads to the maximum number of threads because
      //       the implementation of the thread pool executor will never increase the number of
      //       threads if the queue is not full.
      //
      // https://medium.com/@ankithahjpgowda/policies-of-threadpoolexecutor-in-java-75f22fd6f637
      this.taskExecutor =
          new ThreadPoolExecutor(
              maximumTaskExecutionThreads,
              maximumTaskExecutionThreads,
              taskExecutionThreadKeepAlive,
              TimeUnit.MINUTES,
              taskExecutionQueue);

      // Reset any locks for tasks that were previously being executed
      try {
        log.info("Resetting the locks for the tasks being executed");

        executorService.resetTaskLocks(TaskStatus.EXECUTING, TaskStatus.QUEUED);
      } catch (Throwable e) {
        log.error("Failed to reset the locks for the tasks being executed", e);
      }
    } else {
      log.error(
          "Failed to initialize the Background Task Executor: "
              + "The Executor Service was NOT injected");
    }
  }

  @Override
  public boolean isRunning() {
    return isRunning || taskExecutor.isTerminating();
  }

  @Override
  public void start() {
    log.info("Starting the Background Task Executor");
    isRunning = true;
  }

  @Override
  public void stop() {
    long terminationTimeout =
        Math.max(
            5 * 60000L,
            (taskExecutor.getActiveCount() + taskExecutor.getQueue().size())
                * taskCompletionTimeout);

    log.info(
        "Shutting down the Background Task Executor with "
            + taskExecutor.getActiveCount()
            + " active tasks and "
            + taskExecutor.getQueue().size()
            + " queued tasks (Timeout is "
            + terminationTimeout
            + " milliseconds)");

    isRunning = false;

    try {
      taskExecutor.shutdown();

      if (taskExecutor.awaitTermination(terminationTimeout, TimeUnit.MILLISECONDS)) {
        log.info("Successfully shutdown the Background Task Executor");
      } else {
        log.warn("Failed to cleanly shutdown the Background Task Executor");
      }
    } catch (InterruptedException e) {
      log.warn("The shutdown of the Background Task Executor was interrupted");
    }
  }

  /**
   * The {@code TaskExecutor} class.
   *
   * @author Marcus Portmann
   */
  public static class TaskExecutor implements Runnable {

    /** The Executor Service. */
    private final ExecutorService executorService;

    /** The task. */
    private final Task task;

    /**
     * Constructs a new {@code TaskExecutor}.
     *
     * @param executorService the Executor Service
     * @param task the task
     */
    public TaskExecutor(ExecutorService executorService, Task task) {
      this.executorService = executorService;
      this.task = task;
    }

    @Override
    public void run() {
      try {
        if (log.isDebugEnabled()) {
          log.debug("Executing the task (%s)".formatted(task.getId()));
        }

        long startTime = System.currentTimeMillis();

        TaskExecutionResult taskExecutionResult = executorService.executeTask(task);

        long finishTime = System.currentTimeMillis();

        // Complete the task or advance the task to the next step in the case of a multistep task.
        try {
          executorService.completeTask(task, taskExecutionResult, finishTime - startTime);
        } catch (Throwable e) {
          log.error("Failed to complete the task (%s)".formatted(task.getId()), e);

          try {
            executorService.unlockTask(task.getId(), TaskStatus.FAILED);
          } catch (Throwable f) {
            log.error(
                "Failed to unlock and set the status for the task (%s) to FAILED"
                    .formatted(task.getId()),
                f);
          }
        }
      } catch (TaskExecutionRetryableException e) {
        try {
          executorService.requeueTask(task);
        } catch (Throwable f) {
          log.error("Failed to requeue the task (%s)".formatted(task.getId()), f);
        }
      } catch (TaskExecutionDelayedException e) {
        try {
          executorService.delayTask(task, e.getDelay());
        } catch (Throwable f) {
          log.error(
              "Failed to delay the task (%s) by %d milliseconds"
                  .formatted(task.getId(), e.getDelay()),
              f);
        }
      } catch (Throwable e) {
        log.error("Failed to execute the task (%s)".formatted(task.getId()), e);

        // Fail the task
        try {
          executorService.failTask(task);
        } catch (Throwable f) {
          log.error("Failed to fail the task (%s)".formatted(task.getId()), f);

          try {
            executorService.unlockTask(task.getId(), TaskStatus.FAILED);
          } catch (Throwable g) {
            log.error(
                "Failed to unlock and set the status for the task (%s) to FAILED"
                    .formatted(task.getId()),
                g);
          }
        }
      }
    }
  }
}
