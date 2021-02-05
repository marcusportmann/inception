/*
 * Copyright 2021 Marcus Portmann
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

import digital.inception.core.service.ServiceUnavailableException;
import digital.inception.core.validation.InvalidArgumentException;
import java.util.List;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.xml.bind.annotation.XmlElement;

/**
 * The <b>SchedulerWebService</b> class.
 *
 * @author Marcus Portmann
 */
@WebService(
    serviceName = "SchedulerService",
    name = "ISchedulerService",
    targetNamespace = "http://scheduler.inception.digital")
@SOAPBinding
@SuppressWarnings({"unused", "WeakerAccess", "ValidExternallyBoundObject"})
public class SchedulerWebService {

  /** The Scheduler Service. */
  private final ISchedulerService schedulerService;

  /**
   * Constructs a new <b>SchedulerWebService</b>.
   *
   * @param schedulerService the Scheduler Service
   */
  public SchedulerWebService(ISchedulerService schedulerService) {
    this.schedulerService = schedulerService;
  }

  /**
   * Create the new job.
   *
   * @param job the job to create
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
   */
  @WebMethod(operationName = "UpdateJob")
  public void updateJob(@WebParam(name = "Job") @XmlElement(required = true) Job job)
      throws InvalidArgumentException, JobNotFoundException, ServiceUnavailableException {
    schedulerService.updateJob(job);
  }
}
