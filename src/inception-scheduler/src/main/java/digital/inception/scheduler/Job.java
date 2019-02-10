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

//~--- JDK imports ------------------------------------------------------------

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

/**
 * The <code>Job</code> class holds the information for a job.
 *
 * @author Marcus Portmann
 */
@SuppressWarnings({ "unused", "WeakerAccess" })
public class Job
  implements Serializable
{
  private static final long serialVersionUID = 1000000;

  /**
   * The number of times the current execution of the job has been attempted.
   */
  private int executionAttempts;

  /**
   * The Universally Unique Identifier (UUID) used to uniquely identify the job.
   */
  private UUID id;

  /**
   * The fully qualified name of the Java class that implements the job.
   */
  private String jobClass;

  /**
   * The date and time the job was last executed.
   */
  private Date lastExecuted;

  /**
   * The name of the entity that has locked the job for execution.
   */
  private String lockName;

  /**
   * The name of the job.
   */
  private String name;

  /**
   * The date and time when the job will next be executed.
   */
  private Date nextExecution;

  /**
   * The cron-style scheduling pattern for the job.
   */
  private String schedulingPattern;

  /**
   * The status of the job.
   */
  private JobStatus status;

  /**
   * The date and time the job was updated.
   */
  private Date updated;

  /**
   * Is the job enabled for execution.
   */
  private boolean isEnabled;

  /**
   * Constructs a new <code>Job</code>.
   */
  public Job() {}

  /**
   * Constructs a new <code>Job</code>.
   *
   * @param id                the Universally Unique Identifier (UUID) used to uniquely identify
   *                          the job
   * @param name              the name of the job
   * @param schedulingPattern the cron-style scheduling pattern for the job
   * @param jobClass          the fully qualified name of the Java class that implements the job
   * @param isEnabled         is the job enabled for execution
   * @param status            the status of the job
   * @param executionAttempts the number of times the current execution of the job has
   *                          been attempted
   * @param lockName          the name of the entity that has locked the job for execution
   * @param lastExecuted      the date and time the job was last executed
   * @param nextExecution     the date and time when the job will next be executed
   * @param updated           the date and time the job was updated
   */
  public Job(UUID id, String name, String schedulingPattern, String jobClass, boolean isEnabled,
      JobStatus status, int executionAttempts, String lockName, Date lastExecuted,
      Date nextExecution, Date updated)
  {
    this.id = id;
    this.name = name;
    this.schedulingPattern = schedulingPattern;
    this.jobClass = jobClass;
    this.isEnabled = isEnabled;
    this.status = status;
    this.executionAttempts = executionAttempts;
    this.lockName = lockName;
    this.lastExecuted = lastExecuted;
    this.nextExecution = nextExecution;
    this.updated = updated;
  }

  /**
   * Returns the number of times the current execution of the job has been attempted.
   *
   * @return the number of times the current execution of the job has been attempted
   */
  public int getExecutionAttempts()
  {
    return executionAttempts;
  }

  /**
   * Returns the Universally Unique Identifier (UUID) used to uniquely identify the job.
   *
   * @return the Universally Unique Identifier (UUID) used to uniquely identify the job
   */
  public UUID getId()
  {
    return id;
  }

  /**
   * Returns whether the job is enabled for execution.
   *
   * @return <code>true</code> if the job is enabled for execution or <code>false</code> otherwise
   */
  public boolean getIsEnabled()
  {
    return isEnabled;
  }

  /**
   * Returns the fully qualified name of the Java class that implements the job.
   *
   * @return the fully qualified name of the Java class that implements the job
   */
  public String getJobClass()
  {
    return jobClass;
  }

  /**
   * Returns date and time the job was last executed.
   *
   * @return the date and time the job was last executed
   */
  public Date getLastExecuted()
  {
    return lastExecuted;
  }

  /**
   * Returns the name of the entity that has locked the job for execution.
   *
   * @return the name of the entity that has locked the job for execution
   */
  public String getLockName()
  {
    return lockName;
  }

  /**
   * Returns the name of the job.
   *
   * @return the name of the job
   */
  public String getName()
  {
    return name;
  }

  /**
   * Returns the date and time when the job will next be executed.
   *
   * @return the date and time when the job will next be executed
   */
  public Date getNextExecution()
  {
    return nextExecution;
  }

  /**
   * Returns the cron-style scheduling pattern for the job.
   *
   * @return the cron-style scheduling pattern for the job
   */
  public String getSchedulingPattern()
  {
    return schedulingPattern;
  }

  /**
   * Returns the status of the job.
   *
   * @return the status of the job
   */
  public JobStatus getStatus()
  {
    return status;
  }

  /**
   * Returns the date and time the job was updated.
   *
   * @return the date and time the job was updated
   */
  public Date getUpdated()
  {
    return updated;
  }

  /**
   * Set the number of times the current execution of the job has been attempted.
   *
   * @param executionAttempts the number of times the current execution of the job has
   *                          been attempted
   */
  public void setExecutionAttempts(int executionAttempts)
  {
    this.executionAttempts = executionAttempts;
  }

  /**
   * Set the Universally Unique Identifier (UUID) used to uniquely identify the job.
   *
   * @param id the Universally Unique Identifier (UUID) used to uniquely identify the scheduled
   *           job
   */
  public void setId(UUID id)
  {
    this.id = id;
  }

  /**
   * Set whether the job is enabled for execution.
   *
   * @param isEnabled <code>true</code> if the job is enabled for execution or <code>false</code>
   *                  otherwise
   */
  public void setIsEnabled(boolean isEnabled)
  {
    this.isEnabled = isEnabled;
  }

  /**
   * Set the fully qualified name of the Java class that implements the job.
   *
   * @param jobClass the fully qualified name of the Java class that implements the job
   */
  public void setJobClass(String jobClass)
  {
    this.jobClass = jobClass;
  }

  /**
   * Set the date and time the job was last executed.
   *
   * @param lastExecuted the date and time the job was last executed
   */
  public void setLastExecuted(Date lastExecuted)
  {
    this.lastExecuted = lastExecuted;
  }

  /**
   * Set the name of the entity that has locked the job for execution.
   *
   * @param lockName the name of the entity that has locked the job for execution
   */
  public void setLockName(String lockName)
  {
    this.lockName = lockName;
  }

  /**
   * Set the name of the job.
   *
   * @param name the name of the job
   */
  public void setName(String name)
  {
    this.name = name;
  }

  /**
   * Set the date and time when the job will next be executed.
   *
   * @param nextExecution the date and time when the job will next be executed
   */
  public void setNextExecution(Date nextExecution)
  {
    this.nextExecution = nextExecution;
  }

  /**
   * Set the cron-style scheduling pattern for the job.
   *
   * @param schedulingPattern the cron-style scheduling pattern for the job
   */
  public void setSchedulingPattern(String schedulingPattern)
  {
    this.schedulingPattern = schedulingPattern;
  }

  /**
   * Set the status of the job.
   *
   * @param status the status of the job
   */
  public void setStatus(JobStatus status)
  {
    this.status = status;
  }

  /**
   * Set the date and time the job was updated.
   *
   * @param updated the date and time the job was updated
   */
  public void setUpdated(Date updated)
  {
    this.updated = updated;
  }
}
