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

import digital.inception.executor.model.Task;
import digital.inception.executor.model.TaskExecutionDelayedException;
import digital.inception.executor.model.TaskExecutionResult;
import digital.inception.executor.model.TaskExecutionRetryableException;
import digital.inception.executor.model.TaskStatus;
import jakarta.annotation.PostConstruct;
import java.util.Optional;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

/**
 * The <b>BackgroundTaskExecutor</b> class implements the Background Task Executor.
 *
 * @author Marcus Portmann
 */
@Slf4j
@Service
@SuppressWarnings("unused")
public class BackgroundTaskExecutor {

  /** The Executor Service. */
  private final IExecutorService executorService;

  /** The number of task execution threads to start initially. */
  @Value("${inception.executor.initial-task-execution-threads:#{1}}")
  private int initialTaskExecutionThreads;

  /**
   * The maximum number of tasks to queue for execution if no task execution threads are available.
   */
  @Value("${inception.executor.maximum-task-execution-queue-length:#{50}}")
  private int maximumTaskExecutionQueueLength;

  /** The maximum number of task execution threads to create to execute tasks. */
  @Value("${inception.executor.maximum-task-execution-threads:#{10}}")
  private int maximumTaskExecutionThreads;

  private LinkedBlockingQueue<Runnable> taskExecutionQueue;

  /** The number of minutes an idle task execution thread should be kept alive. */
  @Value("${inception.executor.task-execution-thread-keep-alive:#{5}}")
  private int taskExecutionThreadKeepAlive;

  /** The executor responsible for executing tasks. */
  private Executor taskExecutor;

  /**
   * Constructs a new <b>BackgroundTaskExecutor</b>.
   *
   * @param executorService the Executor Service
   */
  public BackgroundTaskExecutor(IExecutorService executorService) {
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

    while (true) {
      // Retrieve the next task queued for execution
      try {
        if (taskExecutionQueue.size() == maximumTaskExecutionQueueLength) {
          log.warn(
              "The maximum number of tasks queued for execution has been reached ("
                  + maximumTaskExecutionQueueLength
                  + ")");
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

      this.taskExecutor =
          new ThreadPoolExecutor(
              initialTaskExecutionThreads,
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

  /**
   * The <b>TaskExecutor</b> class.
   *
   * @author Marcus Portmann
   */
  @Slf4j
  public static class TaskExecutor implements Runnable {

    /** The Executor Service. */
    private final IExecutorService executorService;

    /** The task. */
    private final Task task;

    /**
     * Constructs a new <b>TaskExecutorThread</b>.
     *
     * @param executorService the Executor Service
     * @param task the task
     */
    public TaskExecutor(IExecutorService executorService, Task task) {
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
