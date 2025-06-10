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

package digital.inception.scheduler.ws;

import digital.inception.core.exception.InvalidArgumentException;
import digital.inception.core.exception.ServiceUnavailableException;
import digital.inception.scheduler.exception.DuplicateJobException;
import digital.inception.scheduler.exception.JobNotFoundException;
import digital.inception.scheduler.model.Job;
import digital.inception.scheduler.service.SchedulerService;
import digital.inception.ws.AbstractWebServiceBase;
import jakarta.jws.WebMethod;
import jakarta.jws.WebParam;
import jakarta.jws.WebResult;
import jakarta.jws.WebService;
import jakarta.jws.soap.SOAPBinding;
import jakarta.xml.bind.annotation.XmlElement;
import java.util.List;
import org.springframework.context.ApplicationContext;

/**
 * The {@code SchedulerWebService} class.
 *
 * @author Marcus Portmann
 */
@WebService(
    serviceName = "SchedulerService",
    name = "ISchedulerService",
    targetNamespace = "https://inception.digital/scheduler")
@SOAPBinding
@SuppressWarnings({"unused", "WeakerAccess", "ValidExternallyBoundObject"})
public class SchedulerWebService extends AbstractWebServiceBase {

  /** The Scheduler Service. */
  private final SchedulerService schedulerService;

  /**
   * Constructs a new {@code SchedulerWebService}.
   *
   * @param applicationContext the Spring application context
   * @param schedulerService the Scheduler Service
   */
  public SchedulerWebService(
      ApplicationContext applicationContext, SchedulerService schedulerService) {
    super(applicationContext);

    this.schedulerService = schedulerService;
  }

  /**
   * Create the job.
   *
   * @param job the job to create
   * @throws InvalidArgumentException if an argument is invalid
   * @throws DuplicateJobException if the job already exists
   * @throws ServiceUnavailableException if the job could not be created
   */
  @WebMethod(operationName = "CreateJob")
  public void createJob(@WebParam(name = "Job") @XmlElement(required = true) Job job)
      throws InvalidArgumentException, DuplicateJobException, ServiceUnavailableException {
    schedulerService.createJob(job);
  }

  /**
   * Delete the job.
   *
   * @param jobId the ID for the job
   * @throws InvalidArgumentException if an argument is invalid
   * @throws JobNotFoundException if the job could not be found
   * @throws ServiceUnavailableException if the job could not be deleted
   */
  @WebMethod(operationName = "DeleteJob")
  public void deleteJob(@WebParam(name = "JobId") @XmlElement(required = true) String jobId)
      throws InvalidArgumentException, JobNotFoundException, ServiceUnavailableException {
    schedulerService.deleteJob(jobId);
  }

  /**
   * Retrieve the job.
   *
   * @param jobId the ID for the job
   * @return the job
   * @throws InvalidArgumentException if an argument is invalid
   * @throws JobNotFoundException if the job could not be found
   * @throws ServiceUnavailableException if the job could not be retrieved
   */
  @WebMethod(operationName = "GetJob")
  @WebResult(name = "Job")
  public Job getJob(@WebParam(name = "JobId") @XmlElement(required = true) String jobId)
      throws InvalidArgumentException, JobNotFoundException, ServiceUnavailableException {
    return schedulerService.getJob(jobId);
  }

  /**
   * Retrieve the name of the job.
   *
   * @param jobId the ID for the job
   * @return the name of the job
   * @throws InvalidArgumentException if an argument is invalid
   * @throws JobNotFoundException if the job could not be found
   * @throws ServiceUnavailableException if the name of the job could not be retrieved
   */
  @WebMethod(operationName = "GetJobName")
  @WebResult(name = "JobName")
  public String getJobName(@WebParam(name = "jobId") @XmlElement(required = true) String jobId)
      throws InvalidArgumentException, JobNotFoundException, ServiceUnavailableException {
    return schedulerService.getJobName(jobId);
  }

  /**
   * Retrieve the jobs.
   *
   * @return the jobs
   * @throws ServiceUnavailableException if the jobs could not be retrieved
   */
  @WebMethod(operationName = "GetJobs")
  @WebResult(name = "Job")
  public List<Job> getJobs() throws ServiceUnavailableException {
    return schedulerService.getJobs();
  }

  /**
   * Update the job.
   *
   * @param job the job
   * @throws InvalidArgumentException if an argument is invalid
   * @throws JobNotFoundException if the job could not be found
   * @throws ServiceUnavailableException if the job could not be updated
   */
  @WebMethod(operationName = "UpdateJob")
  public void updateJob(@WebParam(name = "Job") @XmlElement(required = true) Job job)
      throws InvalidArgumentException, JobNotFoundException, ServiceUnavailableException {
    schedulerService.updateJob(job);
  }
}
