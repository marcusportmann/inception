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

package digital.inception.scheduler.service;

import digital.inception.scheduler.model.Job;
import digital.inception.scheduler.model.JobStatus;
import jakarta.annotation.PostConstruct;
import java.util.Optional;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

/**
 * The <b>BackgroundJobExecutor</b> class implements the Background Job Executor.
 *
 * @author Marcus Portmann
 */
@Service
@SuppressWarnings("unused")
public class BackgroundJobExecutor {

  /* Logger */
  private static final Logger logger = LoggerFactory.getLogger(BackgroundJobExecutor.class);

  /** The Scheduler Service. */
  private final ISchedulerService schedulerService;

  /** The number of job execution threads to start initially. */
  @Value("${inception.scheduler.initial-job-execution-threads:#{1}}")
  private int initialJobExecutionThreads;

  /** The number of minutes an idle job execution thread should be kept alive. */
  @Value("${inception.scheduler.job-execution-thread-keep-alive:#{5}}")
  private int jobExecutionThreadKeepAlive;

  /** The executor responsible for executing jobs. */
  private Executor jobExecutor;

  /**
   * The maximum number of jobs to queue for execution if no job execution threads are available.
   */
  @Value("${inception.scheduler.maximum-job-execution-queue-length:#{50}}")
  private int maximumJobExecutionQueueLength;

  private LinkedBlockingQueue<Runnable> jobExecutionQueue;

  /** The maximum number of job execution threads to create to execute jobs. */
  @Value("${inception.scheduler.maximum-job-execution-threads:#{10}}")
  private int maximumJobExecutionThreads;

  /**
   * Constructs a new <b>BackgroundJobExecutor</b>.
   *
   * @param schedulerService the Scheduler Service
   */
  public BackgroundJobExecutor(ISchedulerService schedulerService) {
    this.schedulerService = schedulerService;
  }

  /** Execute the jobs. */
  @SuppressWarnings("StatementWithEmptyBody")
  @Scheduled(cron = "0 * * * * *")
  public void executeJobs() {
    Optional<Job> jobOptional;

    if (schedulerService == null) {
      return;
    }

    while (true) {
      // Retrieve the next job scheduled for execution
      try {
        if (jobExecutionQueue.size() == maximumJobExecutionQueueLength) {
          logger.warn(
              "The maximum number of jobs queued for execution has been reached ("
                  + maximumJobExecutionQueueLength
                  + ")");
          return;
        }

        jobOptional = schedulerService.getNextJobScheduledForExecution();

        if (jobOptional.isEmpty()) {
          if (logger.isDebugEnabled()) {
            logger.debug("No jobs scheduled for execution");
          }

          // Schedule any unscheduled jobs
          while (schedulerService.scheduleNextUnscheduledJobForExecution()) {}

          return;
        }
      } catch (Throwable e) {
        logger.error("Failed to retrieve the next job scheduled for execution", e);
        return;
      }

      jobExecutor.execute(new JobExecutor(schedulerService, jobOptional.get()));
    }
  }

  /** Initialize the Background Job Executor. */
  @SuppressWarnings("StatementWithEmptyBody")
  @PostConstruct
  public void init() {
    logger.info("Initializing the Background Job Executor");

    if (schedulerService != null) {
      // Initialize the job executor
      jobExecutionQueue = new LinkedBlockingQueue<>(maximumJobExecutionQueueLength);

      this.jobExecutor =
          new ThreadPoolExecutor(
              initialJobExecutionThreads,
              maximumJobExecutionThreads,
              jobExecutionThreadKeepAlive,
              TimeUnit.MINUTES,
              jobExecutionQueue);

      // Reset any locks for jobs that were previously being executed
      try {
        logger.info("Resetting the locks for the jobs being executed");

        schedulerService.resetJobLocks(JobStatus.EXECUTING, JobStatus.SCHEDULED);
      } catch (Throwable e) {
        logger.error("Failed to reset the locks for the jobs being executed", e);
      }

      // Schedule any unscheduled jobs
      try {
        while (schedulerService.scheduleNextUnscheduledJobForExecution()) {}
      } catch (Throwable e) {
        logger.error("Failed to schedule the unscheduled jobs for execution");
      }
    } else {
      logger.error(
          "Failed to initialize the Background Job Executor: "
              + "The Scheduler Service was NOT injected");
    }
  }

  /**
   * The <b>JobExecutor</b> class.
   *
   * @author Marcus Portmann
   */
  public static class JobExecutor implements Runnable {

    /* Logger */
    private static final Logger logger = LoggerFactory.getLogger(JobExecutor.class);

    private final Job job;

    private final ISchedulerService schedulerService;

    /**
     * Constructs a new <b>JobExecutorThread</b>.
     *
     * @param schedulerService the Scheduler Service
     * @param job the job
     */
    public JobExecutor(ISchedulerService schedulerService, Job job) {
      this.schedulerService = schedulerService;
      this.job = job;
    }

    @Override
    public void run() {
      try {
        if (logger.isDebugEnabled()) {
          logger.debug(String.format("Executing the job (%s)", job.getId()));
        }

        schedulerService.executeJob(job);

        // Reschedule the job
        try {
          schedulerService.rescheduleJob(job.getId(), job.getSchedulingPattern());

          try {
            schedulerService.unlockJob(job.getId(), JobStatus.SCHEDULED);
          } catch (Throwable f) {
            logger.error(
                String.format(
                    "Failed to unlock and set the status for the job (%s) to SCHEDULED",
                    job.getId()),
                f);
          }
        } catch (Throwable e) {
          logger.warn(
              String.format(
                  "The job (%s) could not be rescheduled and will be marked as FAILED",
                  job.getId()));

          try {
            schedulerService.unlockJob(job.getId(), JobStatus.FAILED);
          } catch (Throwable f) {
            logger.error(
                String.format(
                    "Failed to unlock and set the status for the job (%s) to FAILED",
                    job.getId()),
                f);
          }
        }
      } catch (Throwable e) {
        logger.error(String.format("Failed to execute the job (%s)", job.getId()), e);

        try {
          /*
           * If the job has exceeded the maximum number of execution attempts then
           * unlock it and set its status to "Failed" otherwise unlock it and set its status to
           * "Scheduled".
           */
          if (job.getExecutionAttempts() >= schedulerService.getMaximumJobExecutionAttempts()) {
            logger.warn(
                String.format(
                    "The job (%s) has exceeded the maximum  number of execution attempts and "
                        + "will be marked as FAILED",
                    job.getId()));

            schedulerService.unlockJob(job.getId(), JobStatus.FAILED);
          } else {
            schedulerService.unlockJob(job.getId(), JobStatus.SCHEDULED);
          }
        } catch (Throwable f) {
          logger.error(
              String.format("Failed to unlock and set the status for the job (%s)", job.getId()),
              f);
        }
      }
    }
  }
}
