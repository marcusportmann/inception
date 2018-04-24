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

//~--- non-JDK imports --------------------------------------------------------

import digital.inception.core.util.ServiceUtil;
import digital.inception.core.util.StringUtil;
import digital.inception.validation.InvalidArgumentException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.sql.*;
import java.util.*;
import java.util.Date;

//~--- JDK imports ------------------------------------------------------------

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
  @Autowired
  private ApplicationContext applicationContext;

  /*
   * The delay in milliseconds between successive attempts to execute a job.
   */
  @Value("${application.scheduler.jobExecutionRetryDelay:#{60000}}")
  private int jobExecutionRetryDelay;

  /*
   * The maximum number of times execution will be attempted for a job.
   */
  @Value("${application.scheduler.maximumJobExecutionAttempts:#{144}}")
  private int maximumJobExecutionAttempts;

  /**
   * The data source used to provide connections to the application database.
   */
  @Autowired
  @Qualifier("applicationDataSource")
  private DataSource dataSource;

  /**
   * Initialize the Scheduler Service.
   *
   * @throws Exception
   */
  @Override
  public void afterPropertiesSet()
    throws Exception
  {
    logger.info(String.format("Initializing the Scheduler Service (%s)", instanceName));
  }

  /**
   * Create the job.
   *
   * @param job the <code>Job</code> instance containing the information for the job
   */
  @Override
  public void createJob(Job job)
    throws InvalidArgumentException, SchedulerServiceException
  {
    if (job == null)
    {
      throw new InvalidArgumentException("job");
    }

    String createJobSQL =
        "INSERT INTO scheduler.jobs (id, name, scheduling_pattern, job_class, is_enabled, status) "
        + "VALUES (?, ?, ?, ?, ?, ?)";

    try (Connection connection = dataSource.getConnection();
      PreparedStatement statement = connection.prepareStatement(createJobSQL))
    {
      statement.setObject(1, job.getId());
      statement.setString(2, job.getName());
      statement.setString(3, job.getSchedulingPattern());
      statement.setString(4, job.getJobClass());
      statement.setBoolean(5, job.getIsEnabled());
      statement.setInt(6, job.getStatus().getCode());

      if (statement.executeUpdate() != 1)
      {
        throw new SchedulerServiceException(String.format(
            "No rows were affected as a result of executing the SQL statement (%s)", createJobSQL));
      }
    }
    catch (Throwable e)
    {
      throw new SchedulerServiceException(String.format("Failed to add the job (%s)",
          job.getName()), e);
    }
  }

  /**
   * Create the job parameter.
   *
   * @param jobParameter the job parameter
   */
  @Override
  public void createJobParameter(JobParameter jobParameter)
    throws InvalidArgumentException, SchedulerServiceException
  {
    if (jobParameter == null)
    {
      throw new InvalidArgumentException("jobParameter");
    }

    String createJobParameterSQL =
        "INSERT INTO scheduler.job_parameters (id, job_id, name, value) VALUES (?, ?, ?, ?)";

    try (Connection connection = dataSource.getConnection();
      PreparedStatement statement = connection.prepareStatement(createJobParameterSQL))
    {
      statement.setObject(1, jobParameter.getId());
      statement.setObject(2, jobParameter.getJobId());
      statement.setString(3, jobParameter.getName());
      statement.setString(4, jobParameter.getValue());

      if (statement.executeUpdate() != 1)
      {
        throw new SchedulerServiceException(String.format(
            "No rows were affected as a result of executing the SQL statement (%s)",
            createJobParameterSQL));
      }
    }
    catch (Throwable e)
    {
      throw new SchedulerServiceException(String.format(
          "Failed to add the job parameter (%s) for the job (%s)", jobParameter.getName(),
          jobParameter.getId()), e);
    }
  }

  /**
   * Delete the job
   *
   * @param jobId the Universally Unique Identifier (UUID) used to uniquely identify the job
   */
  @Override
  public void deleteJob(UUID jobId)
    throws InvalidArgumentException, SchedulerServiceException
  {
    if (jobId == null)
    {
      throw new InvalidArgumentException("jobId");
    }

    String deleteJobSQL = "DELETE FROM scheduler.jobs WHERE id=?";

    try (Connection connection = dataSource.getConnection();
      PreparedStatement statement = connection.prepareStatement(deleteJobSQL))
    {
      statement.setObject(1, jobId);

      if (statement.executeUpdate() != 1)
      {
        throw new SchedulerServiceException(String.format(
            "No rows were affected as a result of executing the SQL statement (%s)", deleteJobSQL));
      }
    }
    catch (Throwable e)
    {
      throw new SchedulerServiceException(String.format("Failed to delete the job (%s)", jobId), e);
    }
  }

  /**
   * Execute the job.
   *
   * @param job the job
   */
  @Override
  public void executeJob(Job job)
    throws InvalidArgumentException, SchedulerServiceException
  {
    if (job == null)
    {
      throw new InvalidArgumentException("job");
    }

    Class<?> jobClass;

    // Load the job class.
    try
    {
      jobClass = Thread.currentThread().getContextClassLoader().loadClass(job.getJobClass());
    }
    catch (Throwable e)
    {
      throw new SchedulerServiceException(String.format(
          "Failed to execute the job (%s) with ID (%s): Failed to load the job class (%s)",
          job.getName(), job.getId(), job.getJobClass()), e);
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
        throw new SchedulerServiceException(String.format(
            "The job class (%s) does not implement the digital.inception.scheduler.IJob interface",
            job.getJobClass()));
      }

      jobImplementation = (IJob) jobObject;

      // Perform dependency injection for the job implementation
      applicationContext.getAutowireCapableBeanFactory().autowireBean(jobImplementation);
    }
    catch (Throwable e)
    {
      throw new SchedulerServiceException(String.format(
          "Failed to initialize the job (%s) with ID (%s)", job.getName(), job.getId()), e);
    }

    // Execute the job
    try
    {
      // Retrieve the parameters for the job
      List<JobParameter> jobParameters = getJobParameters(job.getId());

      Map<String, String> parameters = new HashMap<>();

      for (JobParameter jobParameter : jobParameters)
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
      throw new SchedulerServiceException(String.format(
          "Failed to execute the job (%s) with ID (%s)", job.getName(), job.getId()), e);
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
    throws InvalidArgumentException, SchedulerServiceException
  {
    if (filter == null)
    {
      throw new InvalidArgumentException("filter");
    }

    String getJobsSQL =
        "SELECT id, name, scheduling_pattern, job_class, is_enabled, status, execution_attempts, "
        + "lock_name, last_executed, next_execution, updated FROM scheduler.jobs";

    String getFilteredJobsSQL =
        "SELECT id, name, scheduling_pattern, job_class, is_enabled, status, execution_attempts, "
        + "lock_name, last_executed, next_execution, updated FROM scheduler.jobs "
        + "WHERE (UPPER(name) LIKE ?) OR (UPPER(job_class) LIKE ?)";

    try (Connection connection = dataSource.getConnection();
      PreparedStatement statement = connection.prepareStatement(StringUtil.isNullOrEmpty(filter)
          ? getJobsSQL
          : getFilteredJobsSQL))
    {
      if (!StringUtil.isNullOrEmpty(filter))
      {
        statement.setString(1, "%" + filter.toUpperCase() + "%");
        statement.setString(2, "%" + filter.toUpperCase() + "%");
      }

      return getJobs(statement);
    }
    catch (Throwable e)
    {
      throw new SchedulerServiceException(String.format(
          "Failed to retrieve the jobs matching the filter (%s)", filter), e);
    }
  }

  /**
   * Retrieve the job.
   *
   * @param jobId the Universally Unique Identifier (UUID) used to uniquely identify the job
   *
   * @return the job or <code>null</code> if the job could not be found
   */
  @Override
  public Job getJob(UUID jobId)
    throws InvalidArgumentException, SchedulerServiceException
  {
    if (jobId == null)
    {
      throw new InvalidArgumentException("jobId");
    }

    String getJobSQL =
        "SELECT id, name, scheduling_pattern, job_class, is_enabled, status, execution_attempts, "
        + "lock_name, last_executed, next_execution, updated FROM scheduler.jobs WHERE id = ?";

    try (Connection connection = dataSource.getConnection();
      PreparedStatement statement = connection.prepareStatement(getJobSQL))
    {
      statement.setObject(1, jobId);

      try (ResultSet rs = statement.executeQuery())
      {
        if (rs.next())
        {
          return getJob(rs);
        }
        else
        {
          return null;
        }
      }
    }
    catch (Throwable e)
    {
      throw new SchedulerServiceException(String.format("Failed to retrieve the job (%s)", jobId),
          e);
    }
  }

  /**
   * Retrieve the parameters for the job.
   *
   * @param jobId the Universally Unique Identifier (UUID) used to uniquely identify the job
   *
   * @return the parameters for the job
   */
  @Override
  public List<JobParameter> getJobParameters(UUID jobId)
    throws InvalidArgumentException, SchedulerServiceException
  {
    if (jobId == null)
    {
      throw new InvalidArgumentException("jobId");
    }

    String getJobParametersSQL =
        "SELECT id, job_id, name, value FROM scheduler.job_parameters WHERE job_id = ?";

    try (Connection connection = dataSource.getConnection();
      PreparedStatement statement = connection.prepareStatement(getJobParametersSQL))
    {
      statement.setObject(1, jobId);

      List<JobParameter> jobParameters = new ArrayList<>();

      try (ResultSet rs = statement.executeQuery())
      {
        while (rs.next())
        {
          jobParameters.add(getJobParameter(rs));
        }
      }

      return jobParameters;
    }
    catch (Throwable e)
    {
      throw new SchedulerServiceException(String.format(
          "Failed to retrieve the parameters for the job (%s)", jobId), e);
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
    String getJobsSQL =
        "SELECT id, name, scheduling_pattern, job_class, is_enabled, status, execution_attempts, "
        + "lock_name, last_executed, next_execution, updated FROM scheduler.jobs";

    try (Connection connection = dataSource.getConnection();
      PreparedStatement statement = connection.prepareStatement(getJobsSQL))
    {
      return getJobs(statement);
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
    String getNextJobScheduledForExecutionSQL =
        "SELECT id, name, scheduling_pattern, job_class, is_enabled, status, execution_attempts, "
        + "lock_name, last_executed, next_execution, updated FROM scheduler.jobs "
        + "WHERE status=? AND ((execution_attempts=0) OR "
        + "((execution_attempts>0) AND (last_executed<?))) AND next_execution <= ? "
        + "ORDER BY updated FETCH FIRST 1 ROWS ONLY FOR UPDATE";

    String lockJobSQL = "UPDATE scheduler.jobs SET status=?, lock_name=?, updated=? WHERE id=?";

    try
    {
      Job job = null;

      try (Connection connection = dataSource.getConnection();
        PreparedStatement statement = connection.prepareStatement(
            getNextJobScheduledForExecutionSQL))
      {
        Timestamp processedBefore = new Timestamp(System.currentTimeMillis()
            - jobExecutionRetryDelay);

        statement.setInt(1, JobStatus.SCHEDULED.getCode());
        statement.setTimestamp(2, processedBefore);
        statement.setTimestamp(3, new Timestamp(System.currentTimeMillis()));

        try (ResultSet rs = statement.executeQuery())
        {
          if (rs.next())
          {
            Timestamp updated = new Timestamp(System.currentTimeMillis());

            job = getJob(rs);

            job.setStatus(JobStatus.EXECUTING);
            job.setLockName(instanceName);
            job.setUpdated(updated);

            try (PreparedStatement updateStatement = connection.prepareStatement(lockJobSQL))
            {
              updateStatement.setInt(1, JobStatus.EXECUTING.getCode());
              updateStatement.setString(2, instanceName);
              updateStatement.setTimestamp(3, updated);
              updateStatement.setObject(4, job.getId());

              if (updateStatement.executeUpdate() != 1)
              {
                throw new SchedulerServiceException(String.format(
                    "No rows were affected as a result of executing the SQL statement (%s)",
                    lockJobSQL));
              }
            }
          }
        }
      }

      return job;
    }
    catch (Throwable e)
    {
      throw new SchedulerServiceException(
          "Failed to retrieve the next job that has been scheduled for execution", e);
    }
  }

  /**
   * Retrieve the number of filtered jobs.
   *
   * @param filter the filter to apply to the jobs
   *
   * @return the number of filtered jobs
   */
  @Override
  public int getNumberOfFilteredJobs(String filter)
    throws InvalidArgumentException, SchedulerServiceException
  {
    if (filter == null)
    {
      throw new InvalidArgumentException("filter");
    }

    String getNumberOfJobsSQL = "SELECT COUNT(id) FROM scheduler.jobs";

    String getNumberOfFilteredJobsSQL = "SELECT COUNT(id) FROM scheduler.jobs "
        + "WHERE (UPPER(name) LIKE ?) OR (UPPER(job_class) LIKE ?)";

    try (Connection connection = dataSource.getConnection();
      PreparedStatement statement = connection.prepareStatement(StringUtil.isNullOrEmpty(filter)
          ? getNumberOfJobsSQL
          : getNumberOfFilteredJobsSQL))
    {
      if (!StringUtil.isNullOrEmpty(filter))
      {
        statement.setString(1, "%" + filter.toUpperCase() + "%");
        statement.setString(2, "%" + filter.toUpperCase() + "%");
      }

      try (ResultSet rs = statement.executeQuery())
      {
        if (rs.next())
        {
          return rs.getInt(1);
        }
        else
        {
          return 0;
        }
      }
    }
    catch (Throwable e)
    {
      throw new SchedulerServiceException(String.format(
          "Failed to retrieve the number of jobs matching the filter (%s)", filter), e);
    }
  }

  /**
   * Retrieve the number of jobs.
   *
   * @return the number of jobs
   */
  @Override
  public int getNumberOfJobs()
    throws SchedulerServiceException
  {
    String getNumberOfJobsSQL = "SELECT COUNT(id) FROM scheduler.jobs";

    try (Connection connection = dataSource.getConnection();
      PreparedStatement statement = connection.prepareStatement(getNumberOfJobsSQL))
    {
      try (ResultSet rs = statement.executeQuery())
      {
        if (rs.next())
        {
          return rs.getInt(1);
        }
        else
        {
          return 0;
        }
      }
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
    String getUnscheduledJobsSQL =
        "SELECT id, name, scheduling_pattern, job_class, is_enabled, status, execution_attempts, "
        + "lock_name, last_executed, next_execution, updated FROM scheduler.jobs "
        + "WHERE is_enabled = TRUE AND status = 0";

    try (Connection connection = dataSource.getConnection();
      PreparedStatement statement = connection.prepareStatement(getUnscheduledJobsSQL))
    {
      List<Job> unscheduledJobs = new ArrayList<>();

      try (ResultSet rs = statement.executeQuery())
      {
        while (rs.next())
        {
          unscheduledJobs.add(getJob(rs));
        }
      }

      return unscheduledJobs;
    }
    catch (Throwable e)
    {
      throw new SchedulerServiceException("Failed to retrieve the unscheduled jobs", e);
    }
  }

  /**
   * Increment the execution attempts for the job.
   *
   * @param jobId the Universally Unique Identifier (UUID) used to uniquely identify the job
   */
  @Override
  public void incrementJobExecutionAttempts(UUID jobId)
    throws InvalidArgumentException, SchedulerServiceException
  {
    if (jobId == null)
    {
      throw new InvalidArgumentException("jobId");
    }

    String incrementJobExecutionAttemptsSQL = "UPDATE scheduler.jobs "
        + "SET execution_attempts=execution_attempts + 1, updated=?, last_executed=? WHERE id=?";

    try (Connection connection = dataSource.getConnection();
      PreparedStatement statement = connection.prepareStatement(incrementJobExecutionAttemptsSQL))
    {
      Timestamp currentTime = new Timestamp(System.currentTimeMillis());

      statement.setTimestamp(1, currentTime);
      statement.setTimestamp(2, currentTime);
      statement.setObject(3, jobId);

      if (statement.executeUpdate() != 1)
      {
        throw new SchedulerServiceException(String.format(
            "No rows were affected as a result of executing the SQL statement (%s)",
            incrementJobExecutionAttemptsSQL));
      }
    }
    catch (Throwable e)
    {
      throw new SchedulerServiceException(String.format(
          "Failed to increment the execution attempts for the job (%s)", jobId), e);
    }
  }

  /**
   * Lock a job.
   *
   * @param jobId  the Universally Unique Identifier (UUID) used to uniquely identify the job
   * @param status the new status for the locked job
   */
  @Override
  public void lockJob(UUID jobId, JobStatus status)
    throws InvalidArgumentException, SchedulerServiceException
  {
    if (jobId == null)
    {
      throw new InvalidArgumentException("jobId");
    }

    String lockJobSQL = "UPDATE scheduler.jobs SET status=?, lock_name=?, updated=? WHERE id=?";

    try (Connection connection = dataSource.getConnection();
      PreparedStatement statement = connection.prepareStatement(lockJobSQL))
    {
      statement.setInt(1, status.getCode());
      statement.setString(2, instanceName);
      statement.setTimestamp(3, new Timestamp(System.currentTimeMillis()));
      statement.setObject(4, jobId);

      if (statement.executeUpdate() != 1)
      {
        throw new SchedulerServiceException(String.format(
            "No rows were affected as a result of executing the SQL statement (%s)", lockJobSQL));
      }
    }
    catch (Throwable e)
    {
      throw new SchedulerServiceException(String.format(
          "Failed to lock and set the status for the job (%s) to (%s)", jobId, status), e);
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
  public void rescheduleJob(UUID jobId, String schedulingPattern)
    throws InvalidArgumentException, SchedulerServiceException
  {
    if (jobId == null)
    {
      throw new InvalidArgumentException("jobId");
    }

    try (Connection connection = dataSource.getConnection())
    {
      Predictor predictor = new Predictor(schedulingPattern, System.currentTimeMillis());

      Date nextExecution = predictor.nextMatchingDate();

      scheduleJob(connection, jobId, nextExecution);
    }
    catch (Throwable e)
    {
      throw new SchedulerServiceException(String.format(
          "Failed to reschedule the job (%s) for execution", jobId), e);
    }
  }

  /**
   * Reset the job locks.
   *
   * @param status    the current status of the jobs that have been locked
   * @param newStatus the new status for the jobs that have been unlocked
   *
   * @return the number of job locks reset
   */
  @Override
  public int resetJobLocks(JobStatus status, JobStatus newStatus)
    throws InvalidArgumentException, SchedulerServiceException
  {
    String resetJobLocksSQL = "UPDATE scheduler.jobs SET status=?, lock_name=NULL, updated=? "
        + "WHERE lock_name=? AND status=?";

    try (Connection connection = dataSource.getConnection();
      PreparedStatement statement = connection.prepareStatement(resetJobLocksSQL))
    {
      statement.setInt(1, newStatus.getCode());
      statement.setTimestamp(2, new Timestamp(System.currentTimeMillis()));
      statement.setString(3, instanceName);
      statement.setInt(4, status.getCode());

      return statement.executeUpdate();
    }
    catch (Throwable e)
    {
      throw new SchedulerServiceException(String.format(
          "Failed to reset the locks for the jobs with status (%s) that have been locked using the "
          + "lock name (%s)", status, instanceName), e);
    }
  }

  /**
   * Schedule the next unscheduled job for execution.
   *
   * @return <code>true</code> if there are more unscheduled jobs to schedule or <code>false</code>
   *         if there are no more unscheduled jobs to schedule
   */
  @Override
  @Transactional(propagation = Propagation.REQUIRES_NEW)
  public boolean scheduleNextUnscheduledJobForExecution()
    throws SchedulerServiceException
  {
    String getNextUnscheduledJobSQL =
        "SELECT id, name, scheduling_pattern, job_class, is_enabled, status, execution_attempts, "
        + "lock_name, last_executed, next_execution, updated FROM scheduler.jobs "
        + "WHERE is_enabled = TRUE AND status = 0 "
        + "ORDER BY updated FETCH FIRST 1 ROWS ONLY FOR UPDATE";

    try
    {
      boolean hasMoreUnscheduledJobs;

      try (Connection connection = dataSource.getConnection();
        PreparedStatement statement = connection.prepareStatement(getNextUnscheduledJobSQL))
      {
        try (ResultSet rs = statement.executeQuery())
        {
          if (rs.next())
          {
            Job job = getJob(rs);

            Date nextExecution = null;

            try
            {
              Predictor predictor = new Predictor(job.getSchedulingPattern(),
                  System.currentTimeMillis());

              nextExecution = predictor.nextMatchingDate();
            }
            catch (Throwable e)
            {
              logger.error(String.format(
                  "The next execution date could not be determined for the unscheduled job (%s) "
                  + "with the scheduling pattern (%s): The job will be marked as FAILED",
                  job.getId(), job.getSchedulingPattern()), e);
            }

            if (nextExecution == null)
            {
              setJobStatus(connection, job.getId(), JobStatus.FAILED);
            }
            else
            {
              logger.info(String.format(
                  "Scheduling the unscheduled job (%s) for execution at (%s)", job.getId(),
                  nextExecution));

              scheduleJob(connection, job.getId(), nextExecution);
            }

            hasMoreUnscheduledJobs = true;
          }
          else
          {
            hasMoreUnscheduledJobs = false;
          }
        }
      }

      return hasMoreUnscheduledJobs;
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
  public void setJobStatus(UUID jobId, JobStatus status)
    throws InvalidArgumentException, SchedulerServiceException
  {
    if (jobId == null)
    {
      throw new InvalidArgumentException("jobId");
    }

    try (Connection connection = dataSource.getConnection())
    {
      setJobStatus(connection, jobId, status);
    }
    catch (Throwable e)
    {
      throw new SchedulerServiceException(String.format(
          "Failed to set the status (%s) for the job (%s)", status, jobId), e);
    }
  }

  /**
   * Unlock a locked job.
   *
   * @param jobId  the Universally Unique Identifier (UUID) used to uniquely identify the job
   * @param status the new status for the unlocked job
   */
  @Override
  public void unlockJob(UUID jobId, JobStatus status)
    throws InvalidArgumentException, SchedulerServiceException
  {
    if (jobId == null)
    {
      throw new InvalidArgumentException("jobId");
    }

    String unlockJobSQL =
        "UPDATE scheduler.jobs SET status=?, updated=?, lock_name=NULL WHERE id=?";

    try (Connection connection = dataSource.getConnection();
      PreparedStatement statement = connection.prepareStatement(unlockJobSQL))
    {
      statement.setInt(1, status.getCode());
      statement.setTimestamp(2, new Timestamp(System.currentTimeMillis()));
      statement.setObject(3, jobId);

      if (statement.executeUpdate() != 1)
      {
        throw new SchedulerServiceException(String.format(
            "No rows were affected as a result  of executing the SQL statement (%s)",
            unlockJobSQL));
      }
    }
    catch (Throwable e)
    {
      throw new SchedulerServiceException(String.format(
          "Failed to unlock and set the status for the job (%s) to (%s)", jobId, status), e);
    }
  }

  /**
   * Update the job.
   *
   * @param job the <code>Job</code> instance containing the updated information for the job
   */
  @Override
  public void updateJob(Job job)
    throws InvalidArgumentException, SchedulerServiceException
  {
    if (job == null)
    {
      throw new InvalidArgumentException("job");
    }

    String updateJobSQL =
        "UPDATE scheduler.jobs SET name=?, scheduling_pattern=?, job_class=?, is_enabled=?, "
        + "status=? WHERE id=?";

    try (Connection connection = dataSource.getConnection();
      PreparedStatement statement = connection.prepareStatement(updateJobSQL))
    {
      statement.setString(1, job.getName());
      statement.setString(2, job.getSchedulingPattern());
      statement.setString(3, job.getJobClass());
      statement.setBoolean(4, job.getIsEnabled());
      statement.setInt(5, job.getStatus().getCode());
      statement.setObject(6, job.getId());

      if (statement.executeUpdate() != 1)
      {
        throw new SchedulerServiceException(String.format(
            "No rows were affected as a result of executing the SQL statement (%s)", updateJobSQL));
      }
    }
    catch (Throwable e)
    {
      throw new SchedulerServiceException(String.format("Failed to update the job (%s)",
          job.getId()), e);
    }
  }

  private Job getJob(ResultSet rs)
    throws SQLException
  {
    return new Job(UUID.fromString(rs.getString(1)), rs.getString(2), rs.getString(3), rs.getString(
        4), rs.getBoolean(5), JobStatus.fromCode(rs.getInt(6)), rs.getInt(7), rs.getString(8),
        rs.getTimestamp(9), rs.getTimestamp(10), rs.getTimestamp(11));
  }

  private JobParameter getJobParameter(ResultSet rs)
    throws SQLException
  {
    return new JobParameter(UUID.fromString(rs.getString(1)), UUID.fromString(rs.getString(2)),
        rs.getString(3), rs.getString(4));
  }

  private List<Job> getJobs(PreparedStatement statement)
    throws SQLException
  {
    try (ResultSet rs = statement.executeQuery())
    {
      List<Job> list = new ArrayList<>();

      while (rs.next())
      {
        list.add(getJob(rs));
      }

      return list;
    }
  }

  private void scheduleJob(Connection connection, UUID id, Date nextExecution)
    throws SchedulerServiceException
  {
    String scheduleJobSQL =
        "UPDATE scheduler.jobs SET status=1, execution_attempts=0, next_execution=?, updated=? "
        + "WHERE id=?";

    try (PreparedStatement statement = connection.prepareStatement(scheduleJobSQL))
    {
      statement.setTimestamp(1, new Timestamp(nextExecution.getTime()));
      statement.setTimestamp(2, new Timestamp(System.currentTimeMillis()));
      statement.setObject(3, id);

      if (statement.executeUpdate() != 1)
      {
        throw new SchedulerServiceException(String.format(
            "No rows were affected as a result of executing the SQL statement (%s)",
            scheduleJobSQL));
      }
    }
    catch (Throwable e)
    {
      throw new SchedulerServiceException(String.format("Failed to schedule the job (%s)", id), e);
    }
  }

  private void setJobStatus(Connection connection, UUID id, JobStatus status)
    throws SchedulerServiceException
  {
    String setJobStatusSQL = "UPDATE scheduler.jobs SET status=? WHERE id=?";

    try (PreparedStatement statement = connection.prepareStatement(setJobStatusSQL))
    {
      statement.setInt(1, status.getCode());
      statement.setObject(2, id);

      if (statement.executeUpdate() != 1)
      {
        throw new SchedulerServiceException(String.format(
            "No rows were affected as a result of executing the SQL statement (%s)",
            setJobStatusSQL));
      }
    }
    catch (Throwable e)
    {
      throw new SchedulerServiceException(String.format(
          "Failed to set the status (%s) for the job (%s)", status, id), e);
    }
  }
}
