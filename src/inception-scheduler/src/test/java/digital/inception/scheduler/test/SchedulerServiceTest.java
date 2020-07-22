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

package digital.inception.scheduler.test;

// ~--- non-JDK imports --------------------------------------------------------

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import digital.inception.scheduler.ISchedulerService;
import digital.inception.scheduler.Job;
import digital.inception.scheduler.JobNotFoundException;
import digital.inception.scheduler.JobParameter;
import digital.inception.scheduler.JobStatus;
import digital.inception.test.TestClassRunner;
import digital.inception.test.TestConfiguration;
import java.util.List;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;

// ~--- JDK imports ------------------------------------------------------------

/**
 * The <code>SchedulerServiceTest</code> class contains the implementation of the JUnit tests for
 * the Scheduler Service.
 *
 * @author Marcus Portmann
 */
@RunWith(TestClassRunner.class)
@ContextConfiguration(classes = {TestConfiguration.class})
@TestExecutionListeners(
    listeners = {
      DependencyInjectionTestExecutionListener.class,
      DirtiesContextTestExecutionListener.class,
      TransactionalTestExecutionListener.class
    })
public class SchedulerServiceTest {

  private static int jobCount;

  /** The Scheduler Service. */
  @Autowired private ISchedulerService schedulerService;

  private static synchronized Job getTestJobDetails() {
    jobCount++;

    Job job = new Job();
    job.setId("TestJob" + jobCount);
    job.setName("Test Job Name " + jobCount);
    job.setSchedulingPattern("5 * * * *");
    job.setJobClass("digital.inception.scheduler.TestJob");
    job.setEnabled(true);
    job.setStatus(JobStatus.UNSCHEDULED);

    for (int i = 1; i <= 10; i++) {
      JobParameter parameter =
          new JobParameter("Job Parameter Name " + i, "Job Parameter Value " + i);

      job.addParameter(parameter);
    }

    return job;
  }

  private void compareJobs(Job job1, Job job2) {
    assertEquals("The ID values for the two jobs do not match", job1.getId(), job2.getId());
    assertEquals("The name values for the two jobs do not match", job1.getName(), job2.getName());
    assertEquals(
        "The scheduling pattern values for the two jobs do not match",
        job1.getSchedulingPattern(),
        job2.getSchedulingPattern());
    assertEquals(
        "The job class values for the two jobs do not match",
        job1.getJobClass(),
        job2.getJobClass());
    assertEquals(
        "The is enabled values for the two jobs do not match", job1.isEnabled(), job2.isEnabled());
    assertEquals(
        "The status values for the two jobs do not match", job1.getStatus(), job2.getStatus());
    assertEquals(
        "The execution attempts values for the two jobs do not match",
        job1.getExecutionAttempts(),
        job2.getExecutionAttempts());
    assertEquals(
        "The lock name values for the two jobs do not match",
        job1.getLockName(),
        job2.getLockName());
    assertEquals(
        "The last executed values for the two jobs do not match",
        job1.getLastExecuted(),
        job2.getLastExecuted());
    assertEquals(
        "The number of parameters for the two jobs do not match",
        job1.getParameters().size(),
        job2.getParameters().size());

    for (JobParameter job1Parameter : job1.getParameters()) {
      boolean foundParameter = false;

      for (JobParameter job2Parameter : job2.getParameters()) {
        if (job1Parameter.getName().equalsIgnoreCase(job2Parameter.getName())) {
          assertEquals(
              "The values for the two job parameters ("
                  + job1Parameter.getName()
                  + ") do not match",
              job1Parameter.getValue(),
              job2Parameter.getValue());

          foundParameter = true;
        }
      }

      if (!foundParameter) {
        fail("Failed to find the job parameter (" + job1Parameter.getName() + ")");
      }
    }
  }

  /** Test the execute job functionality. */
  @Test
  public void executeJobTest() throws Exception {
    Job job = getTestJobDetails();

    schedulerService.executeJob(job);
  }

  /** Test the job parameters functionality. */
  @Test
  public void jobParametersTest() throws Exception {
    Job job = getTestJobDetails();
    job.setEnabled(false);

    for (int i = 0; i < 10; i++) {
      job.addParameter(
          new JobParameter(job.getName() + " Parameter " + i, job.getName() + " Value " + i));
    }

    schedulerService.createJob(job);

    Job retrievedJob = schedulerService.getJob(job.getId());

    compareJobs(job, retrievedJob);
  }

  /** Test the job functionality. */
  @Test
  public void jobTest() throws Exception {
    Job disabledJob = getTestJobDetails();
    disabledJob.setEnabled(false);

    schedulerService.createJob(disabledJob);

    List<Job> unscheduledJobs = schedulerService.getUnscheduledJobs();

    for (Job unscheduledJob : unscheduledJobs) {
      if (unscheduledJob.getId().equals(disabledJob.getId())) {
        fail(
            String.format(
                "The disabled job (%s) was retrieved incorrectly as an unscheduled job",
                disabledJob.getId()));
      }
    }

    Job job = getTestJobDetails();

    List<Job> beforeRetrievedJobs = schedulerService.getJobs();

    schedulerService.createJob(job);

    Job retrievedJob = schedulerService.getJob(job.getId());

    compareJobs(job, retrievedJob);

    String retrievedJobName = schedulerService.getJobName(job.getId());

    assertEquals("The correct job name was not retrieved", retrievedJobName, job.getName());

    long numberOfJobs = schedulerService.getNumberOfJobs();

    assertEquals(
        "The correct number of jobs was not retrieved",
        beforeRetrievedJobs.size() + 1,
        numberOfJobs);

    List<Job> afterRetrievedJobs = schedulerService.getJobs();

    assertEquals(
        "The correct number of jobs was not retrieved",
        beforeRetrievedJobs.size() + 1,
        afterRetrievedJobs.size());

    boolean foundJob = false;

    for (Job afterRetrievedJob : afterRetrievedJobs) {
      if (afterRetrievedJob.getId().equals(job.getId())) {
        compareJobs(job, afterRetrievedJob);

        foundJob = true;

        break;
      }
    }

    if (!foundJob) {
      fail(String.format("Failed to find the job (%s) in the list of jobs", job.getId()));
    }

    boolean foundUnscheduledJob = false;

    unscheduledJobs = schedulerService.getUnscheduledJobs();

    for (Job unscheduledJob : unscheduledJobs) {
      if (unscheduledJob.getId().equals(job.getId())) {
        foundUnscheduledJob = true;

        break;
      }
    }

    if (!foundUnscheduledJob) {
      fail(
          String.format(
              "Failed to find the job (%s) in the list of unscheduled jobs", job.getId()));
    }

    // noinspection StatementWithEmptyBody
    while (schedulerService.scheduleNextUnscheduledJobForExecution()) {}

    unscheduledJobs = schedulerService.getUnscheduledJobs();

    for (Job unscheduledJob : unscheduledJobs) {
      if (unscheduledJob.getId().equals(job.getId())) {
        fail(
            String.format(
                "The job (%s) was retrieved incorrectly as an unscheduled job", job.getId()));

        break;
      }
    }

    retrievedJob = schedulerService.getJob(job.getId());

    assertEquals(
        "The status for the job (" + job.getId() + ") is incorrect",
        JobStatus.SCHEDULED,
        retrievedJob.getStatus());

    job.removeParameter(job.getParameters().iterator().next().getName());

    schedulerService.updateJob(job);

    Job updatedJob = schedulerService.getJob(job.getId());

    compareJobs(job, updatedJob);

    schedulerService.deleteJob(job.getId());

    try {
      schedulerService.getJob(job.getId());

      fail("Retrieved the job (" + job.getId() + ") that should have been deleted");
    } catch (JobNotFoundException ignore) {
    }
  }
}
