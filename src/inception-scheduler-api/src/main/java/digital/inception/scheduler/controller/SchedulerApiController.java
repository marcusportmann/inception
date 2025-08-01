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

package digital.inception.scheduler.controller;

import digital.inception.core.api.ProblemDetails;
import digital.inception.core.exception.InvalidArgumentException;
import digital.inception.core.exception.ServiceUnavailableException;
import digital.inception.scheduler.exception.DuplicateJobException;
import digital.inception.scheduler.exception.JobNotFoundException;
import digital.inception.scheduler.model.Job;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * The {@code SchedulerApiController} interface.
 *
 * @author Marcus Portmann
 */
@Tag(name = "Scheduler")
@RequestMapping(value = "/api/scheduler")
// @el (isSecurityDisabled: digital.inception.api.SecureApiSecurityExpressionRoot.isSecurityEnabled)
public interface SchedulerApiController {

  /**
   * Create the job.
   *
   * @param job the job to create
   * @throws InvalidArgumentException if an argument is invalid
   * @throws DuplicateJobException if the job already exists
   * @throws ServiceUnavailableException if the job could not be created
   */
  @Operation(summary = "Create the job", description = "Create the job")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "204", description = "The job was created"),
        @ApiResponse(
            responseCode = "400",
            description = "Invalid argument",
            content =
                @Content(
                    mediaType = "application/problem+json",
                    schema = @Schema(implementation = ProblemDetails.class))),
        @ApiResponse(
            responseCode = "403",
            description = "Access denied",
            content =
                @Content(
                    mediaType = "application/problem+json",
                    schema = @Schema(implementation = ProblemDetails.class))),
        @ApiResponse(
            responseCode = "409",
            description = "A job with the specified ID already exists",
            content =
                @Content(
                    mediaType = "application/problem+json",
                    schema = @Schema(implementation = ProblemDetails.class))),
        @ApiResponse(
            responseCode = "500",
            description =
                "An error has occurred and the request could not be processed at this time",
            content =
                @Content(
                    mediaType = "application/problem+json",
                    schema = @Schema(implementation = ProblemDetails.class)))
      })
  @RequestMapping(
      value = "/jobs",
      method = RequestMethod.POST,
      produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.NO_CONTENT)
  @PreAuthorize(
      "isSecurityDisabled() or hasRole('Administrator') or hasAccessToFunction('Scheduler.SchedulerAdministration') or hasAccessToFunction('Scheduler.JobAdministration')")
  void createJob(
      @io.swagger.v3.oas.annotations.parameters.RequestBody(
              description = "The job to create",
              required = true)
          @RequestBody
          Job job)
      throws InvalidArgumentException, DuplicateJobException, ServiceUnavailableException;

  /**
   * Delete the job.
   *
   * @param jobId the ID for the job
   * @throws InvalidArgumentException if an argument is invalid
   * @throws JobNotFoundException if the job could not be found
   * @throws ServiceUnavailableException if the job could not be deleted
   */
  @Operation(summary = "Delete the job", description = "Delete the job")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "204", description = "The job was deleted"),
        @ApiResponse(
            responseCode = "400",
            description = "Invalid argument",
            content =
                @Content(
                    mediaType = "application/problem+json",
                    schema = @Schema(implementation = ProblemDetails.class))),
        @ApiResponse(
            responseCode = "403",
            description = "Access denied",
            content =
                @Content(
                    mediaType = "application/problem+json",
                    schema = @Schema(implementation = ProblemDetails.class))),
        @ApiResponse(
            responseCode = "404",
            description = "The job could not be found",
            content =
                @Content(
                    mediaType = "application/problem+json",
                    schema = @Schema(implementation = ProblemDetails.class))),
        @ApiResponse(
            responseCode = "500",
            description =
                "An error has occurred and the request could not be processed at this time",
            content =
                @Content(
                    mediaType = "application/problem+json",
                    schema = @Schema(implementation = ProblemDetails.class)))
      })
  @RequestMapping(
      value = "/jobs/{jobId}",
      method = RequestMethod.DELETE,
      produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.NO_CONTENT)
  @PreAuthorize(
      "isSecurityDisabled() or hasRole('Administrator') or hasAccessToFunction('Scheduler.SchedulerAdministration') or hasAccessToFunction('Scheduler.JobAdministration')")
  void deleteJob(
      @Parameter(name = "jobId", description = "The ID for the job", required = true) @PathVariable
          String jobId)
      throws InvalidArgumentException, JobNotFoundException, ServiceUnavailableException;

  /**
   * Retrieve the job.
   *
   * @param jobId the ID for the job
   * @return the job
   * @throws InvalidArgumentException if an argument is invalid
   * @throws JobNotFoundException if the job could not be found
   * @throws ServiceUnavailableException if the job could not be retrieved
   */
  @Operation(summary = "Retrieve the job", description = "Retrieve the job")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "200", description = "The job was retrieved"),
        @ApiResponse(
            responseCode = "400",
            description = "Invalid argument",
            content =
                @Content(
                    mediaType = "application/problem+json",
                    schema = @Schema(implementation = ProblemDetails.class))),
        @ApiResponse(
            responseCode = "403",
            description = "Access denied",
            content =
                @Content(
                    mediaType = "application/problem+json",
                    schema = @Schema(implementation = ProblemDetails.class))),
        @ApiResponse(
            responseCode = "404",
            description = "The job could not be found",
            content =
                @Content(
                    mediaType = "application/problem+json",
                    schema = @Schema(implementation = ProblemDetails.class))),
        @ApiResponse(
            responseCode = "500",
            description =
                "An error has occurred and the request could not be processed at this time",
            content =
                @Content(
                    mediaType = "application/problem+json",
                    schema = @Schema(implementation = ProblemDetails.class)))
      })
  @RequestMapping(
      value = "/jobs/{jobId}",
      method = RequestMethod.GET,
      produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.OK)
  @PreAuthorize(
      "isSecurityDisabled() or hasRole('Administrator') or hasAccessToFunction('Scheduler.SchedulerAdministration') or hasAccessToFunction('Scheduler.JobAdministration')")
  Job getJob(
      @Parameter(name = "jobId", description = "The ID for the job", required = true) @PathVariable
          String jobId)
      throws InvalidArgumentException, JobNotFoundException, ServiceUnavailableException;

  /**
   * Retrieve the name of the job.
   *
   * @param jobId the ID for the job
   * @return the name of the job
   * @throws InvalidArgumentException if an argument is invalid
   * @throws JobNotFoundException if the job could not be found
   * @throws ServiceUnavailableException if the name of the job could not be retrieved
   */
  @Operation(summary = "Retrieve the name of the job", description = "Retrieve the name of the job")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "200", description = "The name of the job was retrieved"),
        @ApiResponse(
            responseCode = "400",
            description = "Invalid argument",
            content =
                @Content(
                    mediaType = "application/problem+json",
                    schema = @Schema(implementation = ProblemDetails.class))),
        @ApiResponse(
            responseCode = "403",
            description = "Access denied",
            content =
                @Content(
                    mediaType = "application/problem+json",
                    schema = @Schema(implementation = ProblemDetails.class))),
        @ApiResponse(
            responseCode = "404",
            description = "The job could not be found",
            content =
                @Content(
                    mediaType = "application/problem+json",
                    schema = @Schema(implementation = ProblemDetails.class))),
        @ApiResponse(
            responseCode = "500",
            description =
                "An error has occurred and the request could not be processed at this time",
            content =
                @Content(
                    mediaType = "application/problem+json",
                    schema = @Schema(implementation = ProblemDetails.class)))
      })
  @RequestMapping(value = "/jobs/{jobId}/name", method = RequestMethod.GET, produces = "text/plain")
  @ResponseStatus(HttpStatus.OK)
  @PreAuthorize(
      "isSecurityDisabled() or hasRole('Administrator') or hasAccessToFunction('Scheduler.SchedulerAdministration') or hasAccessToFunction('Scheduler.JobAdministration')")
  String getJobName(
      @Parameter(name = "jobId", description = "The ID for the job", required = true) @PathVariable
          String jobId)
      throws InvalidArgumentException, JobNotFoundException, ServiceUnavailableException;

  /**
   * Retrieve the jobs.
   *
   * @return the jobs
   * @throws ServiceUnavailableException if the jobs could not be retrieved
   */
  @Operation(summary = "Retrieve the jobs", description = "Retrieve the jobs")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "200", description = "The jobs were retrieved"),
        @ApiResponse(
            responseCode = "403",
            description = "Access denied",
            content =
                @Content(
                    mediaType = "application/problem+json",
                    schema = @Schema(implementation = ProblemDetails.class))),
        @ApiResponse(
            responseCode = "500",
            description =
                "An error has occurred and the request could not be processed at this time",
            content =
                @Content(
                    mediaType = "application/problem+json",
                    schema = @Schema(implementation = ProblemDetails.class)))
      })
  @RequestMapping(
      value = "/jobs",
      method = RequestMethod.GET,
      produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.OK)
  @PreAuthorize(
      "isSecurityDisabled() or hasRole('Administrator') or hasAccessToFunction('Scheduler.SchedulerAdministration') or hasAccessToFunction('Scheduler.JobAdministration')")
  List<Job> getJobs() throws ServiceUnavailableException;

  /**
   * Update the job.
   *
   * @param jobId the ID for the job
   * @param job the job
   * @throws InvalidArgumentException if an argument is invalid
   * @throws JobNotFoundException if the job could not be found
   * @throws ServiceUnavailableException if the job could not be updated
   */
  @Operation(summary = "Update the job", description = "Update the job")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "204", description = "The job was updated"),
        @ApiResponse(
            responseCode = "400",
            description = "Invalid argument",
            content =
                @Content(
                    mediaType = "application/problem+json",
                    schema = @Schema(implementation = ProblemDetails.class))),
        @ApiResponse(
            responseCode = "403",
            description = "Access denied",
            content =
                @Content(
                    mediaType = "application/problem+json",
                    schema = @Schema(implementation = ProblemDetails.class))),
        @ApiResponse(
            responseCode = "404",
            description = "The job could not be found",
            content =
                @Content(
                    mediaType = "application/problem+json",
                    schema = @Schema(implementation = ProblemDetails.class))),
        @ApiResponse(
            responseCode = "500",
            description =
                "An error has occurred and the request could not be processed at this time",
            content =
                @Content(
                    mediaType = "application/problem+json",
                    schema = @Schema(implementation = ProblemDetails.class)))
      })
  @RequestMapping(
      value = "/jobs/{jobId}",
      method = RequestMethod.PUT,
      produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.NO_CONTENT)
  @PreAuthorize(
      "isSecurityDisabled() or hasRole('Administrator') or hasAccessToFunction('Scheduler.SchedulerAdministration') or hasAccessToFunction('Scheduler.JobAdministration')")
  void updateJob(
      @Parameter(name = "jobId", description = "The ID for the job", required = true) @PathVariable
          String jobId,
      @io.swagger.v3.oas.annotations.parameters.RequestBody(
              description = "The job to update",
              required = true)
          @RequestBody
          Job job)
      throws InvalidArgumentException, JobNotFoundException, ServiceUnavailableException;
}
