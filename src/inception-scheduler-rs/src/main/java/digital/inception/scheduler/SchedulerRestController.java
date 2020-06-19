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

package digital.inception.scheduler;

// ~--- non-JDK imports --------------------------------------------------------

import digital.inception.rs.RestControllerError;
import digital.inception.rs.RestUtil;
import digital.inception.rs.SecureRestController;
import digital.inception.validation.InvalidArgumentException;
import digital.inception.validation.ValidationError;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import java.util.Set;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

// ~--- JDK imports ------------------------------------------------------------

/**
 * The <code>SchedulerRestController</code> class.
 *
 * @author Marcus Portmann
 */
@Tag(name = "Scheduler API")
@RestController
@RequestMapping(value = "/api/scheduler")
@SuppressWarnings({"unused", "WeakerAccess"})
public class SchedulerRestController extends SecureRestController {

  /** The Scheduler Service. */
  private final ISchedulerService schedulerService;

  /** The JSR-303 validator. */
  private final Validator validator;

  /**
   * Constructs a new <code>SchedulerRestController</code>.
   *
   * @param schedulerService the Scheduler Service
   * @param validator the JSR-303 validator
   */
  public SchedulerRestController(ISchedulerService schedulerService, Validator validator) {
    this.schedulerService = schedulerService;
    this.validator = validator;
  }

  /**
   * Create the job.
   *
   * @param job the job to create
   */
  @Operation(summary = "Create the job", description = "Create the job")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "204", description = "The job was created successfully"),
        @ApiResponse(
            responseCode = "400",
            description = "Invalid argument",
            content =
                @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = RestControllerError.class))),
        @ApiResponse(
            responseCode = "409",
            description = "A job with the specified ID already exists",
            content =
                @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = RestControllerError.class))),
        @ApiResponse(
            responseCode = "500",
            description =
                "An error has occurred and the request could not be processed at this time",
            content =
                @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = RestControllerError.class)))
      })
  @RequestMapping(value = "/jobs", method = RequestMethod.POST, produces = "application/json")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  @PreAuthorize(
      "hasRole('Administrator') or hasAuthority('FUNCTION_Scheduler.SchedulerAdministration') "
          + "or hasAuthority('FUNCTION_Scheduler.JobAdministration')")
  public void createJob(
      @Parameter(name = "job", description = "The job", required = true) @RequestBody Job job)
      throws InvalidArgumentException, DuplicateJobException, SchedulerServiceException {
    if (job == null) {
      throw new InvalidArgumentException("job");
    }

    Set<ConstraintViolation<Job>> constraintViolations = validator.validate(job);

    if (!constraintViolations.isEmpty()) {
      throw new InvalidArgumentException(
          "job", ValidationError.toValidationErrors(constraintViolations));
    }

    schedulerService.createJob(job);
  }

  /**
   * Delete the job.
   *
   * @param jobId the ID uniquely identifying the job
   */
  @Operation(summary = "Delete the job", description = "Delete the job")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "204", description = "The job was deleted successfully"),
        @ApiResponse(
            responseCode = "400",
            description = "Invalid argument",
            content =
                @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = RestControllerError.class))),
        @ApiResponse(
            responseCode = "404",
            description = "The job could not be found",
            content =
                @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = RestControllerError.class))),
        @ApiResponse(
            responseCode = "500",
            description =
                "An error has occurred and the request could not be processed at this time",
            content =
                @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = RestControllerError.class)))
      })
  @RequestMapping(
      value = "/jobs/{jobId}",
      method = RequestMethod.DELETE,
      produces = "application/json")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  @PreAuthorize(
      "hasRole('Administrator') or hasAuthority('FUNCTION_Scheduler.SchedulerAdministration') "
          + "or hasAuthority('FUNCTION_Scheduler.JobAdministration')")
  public void deleteJob(
      @Parameter(
              name = "jobId",
              description = "The ID uniquely identifying the job",
              required = true)
          @PathVariable
          String jobId)
      throws InvalidArgumentException, JobNotFoundException, SchedulerServiceException {
    if (StringUtils.isEmpty(jobId)) {
      throw new InvalidArgumentException("jobId");
    }

    schedulerService.deleteJob(jobId);
  }

  /**
   * Retrieve the job.
   *
   * @param jobId the ID uniquely identifying the code job
   * @return the job
   */
  @Operation(summary = "Retrieve the job", description = "Retrieve the job")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "200", description = "OK"),
        @ApiResponse(
            responseCode = "400",
            description = "Invalid argument",
            content =
                @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = RestControllerError.class))),
        @ApiResponse(
            responseCode = "404",
            description = "The job could not be found",
            content =
                @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = RestControllerError.class))),
        @ApiResponse(
            responseCode = "500",
            description =
                "An error has occurred and the request could not be processed at this time",
            content =
                @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = RestControllerError.class)))
      })
  @RequestMapping(
      value = "/jobs/{jobId}",
      method = RequestMethod.GET,
      produces = "application/json")
  @ResponseStatus(HttpStatus.OK)
  @PreAuthorize(
      "hasRole('Administrator') or hasAuthority('FUNCTION_Scheduler.SchedulerAdministration') "
          + "or hasAuthority('FUNCTION_Scheduler.JobAdministration')")
  public Job getJob(
      @Parameter(name = "job", description = "The ID uniquely identifying the job", required = true)
          @PathVariable
          String jobId)
      throws InvalidArgumentException, JobNotFoundException, SchedulerServiceException {
    if (StringUtils.isEmpty(jobId)) {
      throw new InvalidArgumentException("jobId");
    }

    return schedulerService.getJob(jobId);
  }

  /**
   * Retrieve the name of the job.
   *
   * @param jobId the ID uniquely identifying the job
   * @return the name of the job
   */
  @Operation(summary = "Retrieve the name of the job", description = "Retrieve the name of the job")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "200", description = "OK"),
        @ApiResponse(
            responseCode = "400",
            description = "Invalid argument",
            content =
                @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = RestControllerError.class))),
        @ApiResponse(
            responseCode = "404",
            description = "The job could not be found",
            content =
                @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = RestControllerError.class))),
        @ApiResponse(
            responseCode = "500",
            description =
                "An error has occurred and the request could not be processed at this time",
            content =
                @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = RestControllerError.class)))
      })
  @RequestMapping(
      value = "/jobs/{jobId}/name",
      method = RequestMethod.GET,
      produces = "application/json")
  @ResponseStatus(HttpStatus.OK)
  @PreAuthorize(
      "hasRole('Administrator') or hasAuthority('FUNCTION_Scheduler.SchedulerAdministration') "
          + "or hasAuthority('FUNCTION_Scheduler.JobAdministration')")
  public String getJobName(
      @Parameter(
              name = "jobId",
              description = "The ID uniquely identifying the job",
              required = true)
          @PathVariable
          String jobId)
      throws InvalidArgumentException, JobNotFoundException, SchedulerServiceException {
    if (StringUtils.isEmpty(jobId)) {
      throw new InvalidArgumentException("jobId");
    }

    return RestUtil.quote(schedulerService.getJobName(jobId));
  }

  /**
   * Retrieve the jobs.
   *
   * @return the jobs
   */
  @Operation(summary = "Retrieve the jobs", description = "Retrieve the jobs")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "200", description = "OK"),
        @ApiResponse(
            responseCode = "500",
            description =
                "An error has occurred and the request could not be processed at this time",
            content =
                @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = RestControllerError.class)))
      })
  @RequestMapping(value = "/jobs", method = RequestMethod.GET, produces = "application/json")
  @ResponseStatus(HttpStatus.OK)
  @PreAuthorize(
      "hasRole('Administrator') or hasAuthority('FUNCTION_Scheduler.SchedulerAdministration') "
          + "or hasAuthority('FUNCTION_Scheduler.JobAdministration')")
  public List<Job> getJobs() throws SchedulerServiceException {
    return schedulerService.getJobs();
  }

  /**
   * Update the job.
   *
   * @param jobId the ID uniquely identifying the job
   * @param job the job
   */
  @Operation(summary = "Update the job", description = "Update the job")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "204", description = "The job was updated successfully"),
        @ApiResponse(
            responseCode = "400",
            description = "Invalid argument",
            content =
                @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = RestControllerError.class))),
        @ApiResponse(
            responseCode = "404",
            description = "The job could not be found",
            content =
                @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = RestControllerError.class))),
        @ApiResponse(
            responseCode = "500",
            description =
                "An error has occurred and the request could not be processed at this time",
            content =
                @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = RestControllerError.class)))
      })
  @RequestMapping(
      value = "/jobs/{jobId}",
      method = RequestMethod.PUT,
      produces = "application/json")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  @PreAuthorize(
      "hasRole('Administrator') or hasAuthority('FUNCTION_Scheduler.SchedulerAdministration') "
          + "or hasAuthority('FUNCTION_Scheduler.JobAdministration')")
  public void updateJob(
      @Parameter(
              name = "jobId",
              description = "The ID uniquely identifying the job",
              required = true)
          @PathVariable
          String jobId,
      @Parameter(name = "job", description = "The job", required = true) @RequestBody Job job)
      throws InvalidArgumentException, JobNotFoundException, SchedulerServiceException {
    if (StringUtils.isEmpty(jobId)) {
      throw new InvalidArgumentException("jobId");
    }

    if (jobId == null) {
      throw new InvalidArgumentException("jobId");
    }

    if (!job.getId().equals(jobId)) {
      throw new InvalidArgumentException("jobId");
    }

    Set<ConstraintViolation<Job>> constraintViolations = validator.validate(job);

    if (!constraintViolations.isEmpty()) {
      throw new InvalidArgumentException(
          "job", ValidationError.toValidationErrors(constraintViolations));
    }

    schedulerService.updateJob(job);
  }
}
