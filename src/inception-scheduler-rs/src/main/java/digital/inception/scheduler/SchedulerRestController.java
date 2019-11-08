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

import digital.inception.rs.RestControllerError;
import digital.inception.rs.RestUtil;
import digital.inception.rs.SecureRestController;
import digital.inception.validation.InvalidArgumentException;
import digital.inception.validation.ValidationError;

import io.swagger.annotations.*;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

//~--- JDK imports ------------------------------------------------------------

import java.util.List;
import java.util.Set;
import java.util.UUID;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;

/**
 * The <code>SchedulerRestController</code> class.
 *
 * @author Marcus Portmann
 */
@Api(tags = "Scheduler API")
@RestController
@RequestMapping(value = "/api/scheduler")
@SuppressWarnings({ "unused", "WeakerAccess" })
public class SchedulerRestController extends SecureRestController
{
  /**
   * The Scheduler Service.
   */
  private ISchedulerService schedulerService;

  /**
   * The JSR-303 validator.
   */
  private Validator validator;

  /**
   * Constructs a new <code>SchedulerRestController</code>.
   *
   * @param schedulerService the Scheduler Service
   * @param validator        the JSR-303 validator
   */
  public SchedulerRestController(ISchedulerService schedulerService, Validator validator)
  {
    this.schedulerService = schedulerService;
    this.validator = validator;
  }

  /**
   * Create the job.
   *
   * @param job the job to create
   */
  @ApiOperation(value = "Create the job", notes = "Create the job")
  @ApiResponses(value = { @ApiResponse(code = 204, message = "The job was created successfully") ,
      @ApiResponse(code = 400, message = "Invalid argument", response = RestControllerError.class) ,
      @ApiResponse(code = 409, message = "A job with the specified ID already exists",
          response = RestControllerError.class) ,
      @ApiResponse(code = 500,
          message = "An error has occurred and the service is unable to process the request at this time",
          response = RestControllerError.class) })
  @RequestMapping(value = "/jobs", method = RequestMethod.POST, produces = "application/json")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  @PreAuthorize(
      "hasRole('Administrator') or hasAuthority('FUNCTION_Scheduler.SchedulerAdministration') or hasAuthority('FUNCTION_Scheduler.JobAdministration')")
  public void createJob(@ApiParam(name = "job", value = "The job", required = true)
  @RequestBody Job job)
    throws InvalidArgumentException, DuplicateJobException, SchedulerServiceException
  {
    if (job == null)
    {
      throw new InvalidArgumentException("job");
    }

    Set<ConstraintViolation<Job>> constraintViolations = validator.validate(job);

    if (!constraintViolations.isEmpty())
    {
      throw new InvalidArgumentException("job", ValidationError.toValidationErrors(
          constraintViolations));
    }

    schedulerService.createJob(job);
  }

  /**
   * Delete the job.
   *
   * @param jobId the Universally Unique Identifier (UUID) used to uniquely identify the job
   */
  @ApiOperation(value = "Delete the job", notes = "Delete the job")
  @ApiResponses(value = { @ApiResponse(code = 204, message = "The job was deleted successfully") ,
      @ApiResponse(code = 400, message = "Invalid argument", response = RestControllerError.class) ,
      @ApiResponse(code = 404, message = "The job could not be found",
          response = RestControllerError.class) ,
      @ApiResponse(code = 500,
          message = "An error has occurred and the service is unable to process the request at this time",
          response = RestControllerError.class) })
  @RequestMapping(value = "/jobs/{jobId}", method = RequestMethod.DELETE,
      produces = "application/json")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  @PreAuthorize(
      "hasRole('Administrator') or hasAuthority('FUNCTION_Scheduler.SchedulerAdministration') or hasAuthority('FUNCTION_Scheduler.JobAdministration')")
  public void deleteJob(@ApiParam(name = "jobId",
      value = "The Universally Unique Identifier (UUID) used to uniquely identify the job", required = true)
  @PathVariable UUID jobId)
    throws InvalidArgumentException, JobNotFoundException, SchedulerServiceException
  {
    if (StringUtils.isEmpty(jobId))
    {
      throw new InvalidArgumentException("jobId");
    }

    schedulerService.deleteJob(jobId);
  }

  /**
   * Retrieve the job.
   *
   * @param jobId the Universally Unique Identifier (UUID) used to uniquely identify the code job
   *
   * @return the job
   */
  @ApiOperation(value = "Retrieve the job", notes = "Retrieve the job")
  @ApiResponses(value = { @ApiResponse(code = 200, message = "OK") ,
      @ApiResponse(code = 400, message = "Invalid argument", response = RestControllerError.class) ,
      @ApiResponse(code = 404, message = "The job could not be found",
          response = RestControllerError.class) ,
      @ApiResponse(code = 500,
          message = "An error has occurred and the service is unable to process the request at this time",
          response = RestControllerError.class) })
  @RequestMapping(value = "/jobs/{jobId}", method = RequestMethod.GET,
      produces = "application/json")
  @ResponseStatus(HttpStatus.OK)
  @PreAuthorize(
      "hasRole('Administrator') or hasAuthority('FUNCTION_Scheduler.SchedulerAdministration') or hasAuthority('FUNCTION_Scheduler.JobAdministration')")
  public Job getJob(@ApiParam(name = "job",
      value = "The Universally Unique Identifier (UUID) used to uniquely identify the job",
      required = true)
  @PathVariable UUID jobId)
    throws InvalidArgumentException, JobNotFoundException, SchedulerServiceException
  {
    if (StringUtils.isEmpty(jobId))
    {
      throw new InvalidArgumentException("jobId");
    }

    return schedulerService.getJob(jobId);
  }

  /**
   * Retrieve the name of the job.
   *
   * @param jobId the Universally Unique Identifier (UUID) used to uniquely identify the job
   *
   * @return the name of the job
   */
  @ApiOperation(value = "Retrieve the name of the job", notes = "Retrieve the name of the job")
  @ApiResponses(value = { @ApiResponse(code = 200, message = "OK") ,
      @ApiResponse(code = 400, message = "Invalid argument", response = RestControllerError.class) ,
      @ApiResponse(code = 404, message = "The job could not be found",
          response = RestControllerError.class) ,
      @ApiResponse(code = 500,
          message = "An error has occurred and the service is unable to process the request at this time",
          response = RestControllerError.class) })
  @RequestMapping(value = "/jobs/{jobId}/name", method = RequestMethod.GET,
      produces = "application/json")
  @ResponseStatus(HttpStatus.OK)
  @PreAuthorize(
      "hasRole('Administrator') or hasAuthority('FUNCTION_Scheduler.SchedulerAdministration') or hasAuthority('FUNCTION_Scheduler.JobAdministration')")
  public String getJobName(@ApiParam(name = "jobId",
      value = "The Universally Unique Identifier (UUID) used to uniquely identify the job", required = true)
  @PathVariable UUID jobId)
    throws InvalidArgumentException, JobNotFoundException, SchedulerServiceException
  {
    if (StringUtils.isEmpty(jobId))
    {
      throw new InvalidArgumentException("jobId");
    }

    return RestUtil.quote(schedulerService.getJobName(jobId));
  }

  /**
   * Retrieve the jobs.
   *
   * @return the jobs
   */
  @ApiOperation(value = "Retrieve the jobs", notes = "Retrieve the jobs")
  @ApiResponses(value = { @ApiResponse(code = 200, message = "OK") ,
      @ApiResponse(code = 500,
          message = "An error has occurred and the service is unable to process the request at this time",
          response = RestControllerError.class) })
  @RequestMapping(value = "/jobs", method = RequestMethod.GET, produces = "application/json")
  @ResponseStatus(HttpStatus.OK)
  @PreAuthorize(
      "hasRole('Administrator') or hasAuthority('FUNCTION_Scheduler.SchedulerAdministration') or hasAuthority('FUNCTION_Scheduler.JobAdministration')")
  public List<Job> getJobs()
    throws SchedulerServiceException
  {
    return schedulerService.getJobs();
  }

  /**
   * Update the job.
   *
   * @param jobId the Universally Unique Identifier (UUID) used to uniquely identify the job
   * @param job   the job
   */
  @ApiOperation(value = "Update the job", notes = "Update the job")
  @ApiResponses(value = { @ApiResponse(code = 204, message = "The job was updated successfully") ,
      @ApiResponse(code = 400, message = "Invalid argument", response = RestControllerError.class) ,
      @ApiResponse(code = 404, message = "The job could not be found",
          response = RestControllerError.class) ,
      @ApiResponse(code = 500,
          message = "An error has occurred and the service is unable to process the request at this time",
          response = RestControllerError.class) })
  @RequestMapping(value = "/jobs/{jobId}", method = RequestMethod.PUT,
      produces = "application/json")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  @PreAuthorize(
      "hasRole('Administrator') or hasAuthority('FUNCTION_Scheduler.SchedulerAdministration') or hasAuthority('FUNCTION_Scheduler.JobAdministration')")
  public void updateJob(@ApiParam(name = "jobId",
      value = "The Universally Unique Identifier (UUID) used to uniquely identify the job",
      required = true)
  @PathVariable UUID jobId, @ApiParam(name = "job", value = "The job", required = true)
  @RequestBody Job job)
    throws InvalidArgumentException, JobNotFoundException, SchedulerServiceException
  {
    if (StringUtils.isEmpty(jobId))
    {
      throw new InvalidArgumentException("jobId");
    }

    if (jobId == null)
    {
      throw new InvalidArgumentException("jobId");
    }

    if (!job.getId().equals(jobId))
    {
      throw new InvalidArgumentException("jobId");
    }

    Set<ConstraintViolation<Job>> constraintViolations = validator.validate(job);

    if (!constraintViolations.isEmpty())
    {
      throw new InvalidArgumentException("job", ValidationError.toValidationErrors(
          constraintViolations));
    }

    schedulerService.updateJob(job);
  }
}
