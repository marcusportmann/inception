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

package digital.inception.scheduler;

//~--- non-JDK imports --------------------------------------------------------

import digital.inception.core.util.ServiceUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

//~--- JDK imports ------------------------------------------------------------

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import java.util.*;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * The <code>SchedulerService</code> class provides the Scheduler Service implementation.
 *
 * @author Marcus Portmann
 */
@Service
public class SchedulerService
  implements ISchedulerService, InitializingBean
{
  /* Logger */
  private static final Logger logger = LoggerFactory.getLogger(SchedulerService.class);

  /* The name of the Scheduler Service instance. */
  private String instanceName = ServiceUtil.getServiceInstanceName("SchedulerService");

  /**
   * The Spring application context.
   */
  private ApplicationContext applicationContext;

  /* Entity Manager */
  @PersistenceContext(unitName = "applicationPersistenceUnit")
  private EntityManager entityManager;

  /*
   * The delay in milliseconds between successive attempts to execute a job.
   */
  @Value("${application.scheduler.jobExecutionRetryDelay:#{60000}}")
  private int jobExecutionRetryDelay;

  /**
   * The Job Repository.
   */
  private JobRepository jobRepository;

  /*
   * The maximum number of times execution will be attempted for a job.
   */
  @Value("${application.scheduler.maximumJobExecutionAttempts:#{144}}")
  private int maximumJobExecutionAttempts;

  /**
   * Constructs a new <code>SchedulerService</code>.
   *
   * @param applicationContext the Spring application context
   * @param jobRepository      the Job Repository
   */
  public SchedulerService(ApplicationContext applicationContext, JobRepository jobRepository)
  {
    this.applicationContext = applicationContext;
    this.jobRepository = jobRepository;
  }

  /**
   * Initialize the Scheduler Service.
   */
  @Override
  public void afterPropertiesSet()
  {
    logger.info("Initializing the Scheduler Service (" + instanceName + ")");
  }

  /**
   * Create the job.
   *
   * @param job the <code>Job</code> instance containing the information for the job
   */
  @Override
  @Transactional
  public void createJob(Job job)
    throws SchedulerServiceException
  {
    try
    {
      jobRepository.saveAndFlush(job);
    }
    catch (Throwable e)
    {
      throw new SchedulerServiceException("Failed to create the job (" + job.getName() + ")", e);
    }
  }

  /**
   * Delete the job
   *
   * @param jobId the Universally Unique Identifier (UUID) used to uniquely identify the job
   */
  @Override
  @Transactional
  public void deleteJob(UUID jobId)
    throws JobNotFoundException, SchedulerServiceException
  {
    try
    {
      if (!jobRepository.existsById(jobId))
      {
        throw new JobNotFoundException(jobId);
      }

      jobRepository.deleteById(jobId);
    }
    catch (JobNotFoundException e)
    {
      throw e;
    }
    catch (Throwable e)
    {
      throw new SchedulerServiceException("Failed to delete the job (" + jobId + ")", e);
    }
  }

  /**
   * Execute the job.
   *
   * @param job the job
   */
  @Override
  public void executeJob(Job job)
    throws SchedulerServiceException
  {
    Class<?> jobClass;

    // Load the job class.
    try
    {
      jobClass = Thread.currentThread().getContextClassLoader().loadClass(job.getJobClass());
    }
    catch (Throwable e)
    {
      throw new SchedulerServiceException("Failed to execute the job (" + job.getName()
          + ") with ID (" + job.getId() + "): Failed to load the job class (" + job.getJobClass()
          + ")", e);
    }

    // Initialize the job
    IJob jobImplementation;

    try
    {
      // Create a new instance of the job
      Object jobObject = jobClass.getConstructor().newInstance();

      // Check if the job is a valid job
      if (!(jobObject instanceof IJob))
      {
        throw new SchedulerServiceException("The job class (" + job.getJobClass()
            + ") does not implement the digital.inception.scheduler.IJob interface");
      }

      jobImplementation = (IJob) jobObject;

      // Perform dependency injection for the job implementation
      applicationContext.getAutowireCapableBeanFactory().autowireBean(jobImplementation);
    }
    catch (Throwable e)
    {
      throw new SchedulerServiceException("Failed to initialize the job (" + job.getName()
          + ") with ID (" + job.getId() + ")", e);
    }

    // Execute the job
    try
    {
      // Retrieve the parameters for the job
      Map<String, String> parameters = new HashMap<>();

      for (JobParameter jobParameter : job.getParameters())
      {
        parameters.put(jobParameter.getName(), jobParameter.getValue());
      }

      // Initialize the job execution context
      JobExecutionContext context = new JobExecutionContext(job.getNextExecution(), parameters);

      // Execute the job
      jobImplementation.execute(context);
    }
    catch (Throwable e)
    {
      throw new SchedulerServiceException("Failed to execute the job (" + job.getName()
          + ") with ID (" + job.getId() + ")", e);
    }
  }

  /**
   * Retrieve the filtered jobs.
   *
   * @param filter the filter to apply to the jobs
   *
   * @return the jobs
   */
  @Override
  public List<Job> getFilteredJobs(String filter)
    throws SchedulerServiceException
  {
    try
    {
      if (!StringUtils.isEmpty(filter))
      {
        return jobRepository.findFiltered("%" + filter.toUpperCase() + "%");
      }
      else
      {
        return jobRepository.findAll();
      }
    }
    catch (Throwable e)
    {
      throw new SchedulerServiceException("Failed to retrieve the jobs matching the filter ("
          + filter + ")", e);
    }
  }

  /**
   * Retrieve the job.
   *
   * @param jobId the Universally Unique Identifier (UUID) used to uniquely identify the job
   *
   * @return the job
   */
  @Override
  public Job getJob(UUID jobId)
    throws JobNotFoundException, SchedulerServiceException
  {
    try
    {
      Optional<Job> jobOptional = jobRepository.findById(jobId);

      if (jobOptional.isPresent())
      {
        return jobOptional.get();
      }
      else
      {
        throw new JobNotFoundException(jobId);
      }
    }
    catch (JobNotFoundException e)
    {
      throw e;
    }
    catch (Throwable e)
    {
      throw new SchedulerServiceException("Failed to retrieve the job (" + jobId + ")", e);
    }
  }

  /**
   * Retrieve the jobs.
   *
   * @return the jobs
   */
  @Override
  public List<Job> getJobs()
    throws SchedulerServiceException
  {
    try
    {
      return jobRepository.findAll();
    }
    catch (Throwable e)
    {
      throw new SchedulerServiceException("Failed to retrieve the jobs", e);
    }
  }

  /**
   * Returns the maximum number of times execution will be attempted for a job.
   *
   * @return the maximum number of times execution will be attempted for a job
   */
  @Override
  public int getMaximumJobExecutionAttempts()
  {
    return maximumJobExecutionAttempts;
  }

  /**
   * Retrieve the next job that is scheduled for execution.
   * <p/>
   * The job will be locked to prevent duplicate processing.
   *
   * @return the next job that is scheduled for execution or <code>null</code> if no jobs are
   *         currently scheduled for execution
   */
  @Override
  @Transactional(propagation = Propagation.REQUIRES_NEW)
  public Job getNextJobScheduledForExecution()
    throws SchedulerServiceException
  {
    try
    {
      LocalDateTime lastExecutedBefore = LocalDateTime.now();

      lastExecutedBefore = lastExecutedBefore.minus(jobExecutionRetryDelay, ChronoUnit.MILLIS);

      PageRequest pageRequest = PageRequest.of(0, 1);

      List<Job> jobs = jobRepository.findJobsScheduledForExecutionForWrite(lastExecutedBefore,
          pageRequest);

      if (jobs.size() > 0)
      {
        Job job = jobs.get(0);

        LocalDateTime when = LocalDateTime.now();

        jobRepository.lockJobForExecution(job.getId(), instanceName, when);

        entityManager.detach(job);

        job.setStatus(JobStatus.EXECUTING);
        job.setLockName(instanceName);
        job.incrementExecutionAttempts();
        job.setLastExecuted(when);

        return job;
      }
      else
      {
        return null;
      }
    }
    catch (Throwable e)
    {
      throw new SchedulerServiceException(
          "Failed to retrieve the next job that has been scheduled for execution", e);
    }
  }

  /**
   * Retrieve the number of jobs.
   *
   * @return the number of jobs
   */
  @Override
  public long getNumberOfJobs()
    throws SchedulerServiceException
  {
    try
    {
      return jobRepository.count();
    }
    catch (Throwable e)
    {
      throw new SchedulerServiceException("Failed to retrieve the number of jobs", e);
    }
  }

  /**
   * Retrieve the unscheduled jobs.
   *
   * @return the unscheduled jobs
   */
  @Override
  public List<Job> getUnscheduledJobs()
    throws SchedulerServiceException
  {
    try
    {
      return jobRepository.findUnscheduledJobs();
    }
    catch (Throwable e)
    {
      throw new SchedulerServiceException("Failed to retrieve the unscheduled jobs", e);
    }
  }

  /**
   * Reschedule the job for execution.
   *
   * @param jobId             the Universally Unique Identifier (UUID) used to uniquely identify
   *                          the job
   * @param schedulingPattern the cron-style scheduling pattern for the job used to determine the
   *                          next execution time
   */
  @Override
  @Transactional
  public void rescheduleJob(UUID jobId, String schedulingPattern)
    throws SchedulerServiceException
  {
    try
    {
      Predictor predictor = new Predictor(schedulingPattern, System.currentTimeMillis());

      jobRepository.scheduleJob(jobId, predictor.nextMatchingLocalDateTime());
    }
    catch (Throwable e)
    {
      throw new SchedulerServiceException("Failed to reschedule the job (" + jobId
          + ") for execution", e);
    }
  }

  /**
   * Reset the job locks.
   *
   * @param status    the current status of the jobs that have been locked
   * @param newStatus the new status for the jobs that have been unlocked
   */
  @Override
  @Transactional
  public void resetJobLocks(JobStatus status, JobStatus newStatus)
    throws SchedulerServiceException
  {
    try
    {
      jobRepository.resetJobLocks(status, newStatus, instanceName);
    }
    catch (Throwable e)
    {
      throw new SchedulerServiceException("Failed to reset the locks for the jobs with status ("
          + status + ") that have been locked using the lock name (" + instanceName + ")", e);
    }
  }

  /**
   * Schedule the next unscheduled job for execution.
   *
   * @return <code>true</code> if a job was successfully scheduled for execution or
   *         <code>false</code> otherwise
   */
  @Override
  @Transactional(propagation = Propagation.REQUIRES_NEW)
  public boolean scheduleNextUnscheduledJobForExecution()
    throws SchedulerServiceException
  {
    try
    {
      PageRequest pageRequest = PageRequest.of(0, 1);

      List<Job> jobs = jobRepository.findUnscheduledJobsForWrite(pageRequest);

      if (jobs.size() > 0)
      {
        Job job = jobs.get(0);

        LocalDateTime nextExecution = null;

        try
        {
          Predictor predictor = new Predictor(job.getSchedulingPattern(),
              System.currentTimeMillis());

          nextExecution = predictor.nextMatchingLocalDateTime();
        }
        catch (Throwable e)
        {
          logger.error("The next execution date could not be determined for the unscheduled job ("
              + job.getId() + ") with the scheduling pattern (" + job.getSchedulingPattern()
              + "): The job will be marked as FAILED", e);
        }

        if (nextExecution == null)
        {
          jobRepository.setJobStatus(job.getId(), JobStatus.FAILED);
        }
        else
        {
          logger.info("Scheduling the unscheduled job (" + job.getId() + ") for execution at ("
              + nextExecution + ")");

          jobRepository.scheduleJob(job.getId(), nextExecution);
        }

        return true;
      }
      else
      {
        return false;
      }
    }
    catch (Throwable e)
    {
      throw new SchedulerServiceException("Failed to schedule the next unscheduled job", e);
    }
  }

  /**
   * Set the status for the job.
   *
   * @param jobId  the Universally Unique Identifier (UUID) used to uniquely identify the job
   * @param status the new status for the job
   */
  @Override
  @Transactional
  public void setJobStatus(UUID jobId, JobStatus status)
    throws JobNotFoundException, SchedulerServiceException
  {
    try
    {
      if (!jobRepository.existsById(jobId))
      {
        throw new JobNotFoundException(jobId);
      }

      jobRepository.setJobStatus(jobId, status);
    }
    catch (JobNotFoundException e)
    {
      throw e;
    }
    catch (Throwable e)
    {
      throw new SchedulerServiceException("Failed to set the status (" + status + ") for the job ("
          + jobId + ")", e);
    }
  }

  /**
   * Unlock a locked job.
   *
   * @param jobId  the Universally Unique Identifier (UUID) used to uniquely identify the job
   * @param status the new status for the unlocked job
   */
  @Override
  @Transactional
  public void unlockJob(UUID jobId, JobStatus status)
    throws JobNotFoundException, SchedulerServiceException
  {
    try
    {
      if (!jobRepository.existsById(jobId))
      {
        throw new JobNotFoundException(jobId);
      }

      jobRepository.unlockJob(jobId, status);
    }
    catch (JobNotFoundException e)
    {
      throw e;
    }
    catch (Throwable e)
    {
      throw new SchedulerServiceException("Failed to unlock and set the status for the job ("
          + jobId + ") to (" + status + ")", e);
    }
  }

  /**
   * Update the job.
   *
   * @param job the <code>Job</code> instance containing the updated information for the job
   */
  @Override
  public void updateJob(Job job)
    throws JobNotFoundException, SchedulerServiceException
  {
    try
    {
      if (!jobRepository.existsById(job.getId()))
      {
        throw new JobNotFoundException(job.getId());
      }

      jobRepository.saveAndFlush(job);
    }
    catch (JobNotFoundException e)
    {
      throw e;
    }
    catch (Throwable e)
    {
      throw new SchedulerServiceException("Failed to update the job (" + job.getId() + ")", e);
    }
  }
}
