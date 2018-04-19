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

package digital.inception.scheduler;

//~--- JDK imports ------------------------------------------------------------

import java.util.List;
import java.util.UUID;

/**
 * The <code>ISchedulerService</code> interface defines the functionality provided by a Scheduler
 * Service implementation.
 *
 * @author Marcus Portmann
 */
@SuppressWarnings("unused")
public interface ISchedulerService
{
  /**
   * Create the job.
   *
   * @param job the <code>Job</code> instance containing the information for the job
   */
  void createJob(Job job)
    throws SchedulerServiceException;

  /**
   * Create the job parameter.
   *
   * @param jobParameter the job parameter
   */
  void createJobParameter(JobParameter jobParameter)
    throws SchedulerServiceException;

  /**
   * Delete the job
   *
   * @param jobId the Universally Unique Identifier (UUID) used to uniquely identify the job
   */
  void deleteJob(UUID jobId)
    throws SchedulerServiceException;

  /**
   * Execute the job.
   *
   * @param job the job
   */
  void executeJob(Job job)
    throws SchedulerServiceException;

  /**
   * Retrieve the filtered jobs.
   *
   * @param filter the filter to apply to the jobs
   *
   * @return the jobs
   */
  List<Job> getFilteredJobs(String filter)
    throws SchedulerServiceException;

  /**
   * Retrieve the job.
   *
   * @param jobId the Universally Unique Identifier (UUID) used to uniquely identify the job
   *
   * @return the job or <code>null</code> if the job could not be found
   */
  Job getJob(UUID jobId)
    throws SchedulerServiceException;

  /**
   * Retrieve the parameters for the job.
   *
   * @param jobId the Universally Unique Identifier (UUID) used to uniquely identify the job
   *
   * @return the parameters for the job
   */
  List<JobParameter> getJobParameters(UUID jobId)
    throws SchedulerServiceException;

  /**
   * Retrieve the jobs.
   *
   * @return the jobs
   */
  List<Job> getJobs()
    throws SchedulerServiceException;

  /**
   * Returns the maximum number of times execution will be attempted for a job.
   *
   * @return the maximum number of times execution will be attempted for a job
   */
  int getMaximumJobExecutionAttempts();

  /**
   * Retrieve the next job that is scheduled for execution.
   * <p/>
   * The job will be locked to prevent duplicate processing.
   *
   * @return the next job that is scheduled for execution or <code>null</code> if no jobs are
   *         currently scheduled for execution
   */
  Job getNextJobScheduledForExecution()
    throws SchedulerServiceException;

  /**
   * Retrieve the number of filtered jobs.
   *
   * @param filter the filter to apply to the jobs
   *
   * @return the number of filtered jobs
   */
  int getNumberOfFilteredJobs(String filter)
    throws SchedulerServiceException;

  /**
   * Retrieve the number of jobs.
   *
   * @return the number of jobs
   */
  int getNumberOfJobs()
    throws SchedulerServiceException;

  /**
   * Retrieve the unscheduled jobs.
   *
   * @return the unscheduled jobs
   */
  List<Job> getUnscheduledJobs()
    throws SchedulerServiceException;

  /**
   * Increment the execution attempts for the job.
   *
   * @param jobId the Universally Unique Identifier (UUID) used to uniquely identify the job
   */
  void incrementJobExecutionAttempts(UUID jobId)
    throws SchedulerServiceException;

  /**
   * Lock a job.
   *
   * @param jobId  the Universally Unique Identifier (UUID) used to uniquely identify the job
   * @param status the new status for the locked job
   */
  void lockJob(UUID jobId, JobStatus status)
    throws SchedulerServiceException;

  /**
   * Reschedule the job for execution.
   *
   * @param jobId             the Universally Unique Identifier (UUID) used to uniquely identify
   *                          the job
   * @param schedulingPattern the cron-style scheduling pattern for the job used to determine the
   *                          next execution time
   */
  void rescheduleJob(UUID jobId, String schedulingPattern)
    throws SchedulerServiceException;

  /**
   * Reset the job locks.
   *
   * @param status    the current status of the jobs that have been locked
   * @param newStatus the new status for the jobs that have been unlocked
   *
   * @return the number of job locks reset
   */
  int resetJobLocks(JobStatus status, JobStatus newStatus)
    throws SchedulerServiceException;

  /**
   * Schedule the next unscheduled job for execution.
   *
   * @return <code>true</code> if there are more unscheduled jobs to schedule or <code>false</code>
   *         if there are no more unscheduled jobs to schedule
   */
  boolean scheduleNextUnscheduledJobForExecution()
    throws SchedulerServiceException;

  /**
   * Set the status for the job.
   *
   * @param jobId  the Universally Unique Identifier (UUID) used to uniquely identify the job
   * @param status the new status for the job
   */
  void setJobStatus(UUID jobId, JobStatus status)
    throws SchedulerServiceException;

  /**
   * Unlock a locked job.
   *
   * @param jobId  the Universally Unique Identifier (UUID) used to uniquely identify the job
   * @param status the new status for the unlocked job
   */
  void unlockJob(UUID jobId, JobStatus status)
    throws SchedulerServiceException;

  /**
   * Update the job.
   *
   * @param job the <code>Job</code> instance containing the updated information for the job
   */
  void updateJob(Job job)
    throws SchedulerServiceException;
}
