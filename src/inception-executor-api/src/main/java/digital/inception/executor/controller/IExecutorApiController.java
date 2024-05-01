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

package digital.inception.executor.controller;

import digital.inception.core.api.ProblemDetails;
import digital.inception.core.service.InvalidArgumentException;
import digital.inception.core.service.ServiceUnavailableException;
import digital.inception.core.sorting.SortDirection;
import digital.inception.executor.model.BatchTasksNotFoundException;
import digital.inception.executor.model.DuplicateTaskTypeException;
import digital.inception.executor.model.InvalidTaskStatusException;
import digital.inception.executor.model.QueueTaskRequest;
import digital.inception.executor.model.Task;
import digital.inception.executor.model.TaskEvent;
import digital.inception.executor.model.TaskNotFoundException;
import digital.inception.executor.model.TaskSortBy;
import digital.inception.executor.model.TaskStatus;
import digital.inception.executor.model.TaskSummaries;
import digital.inception.executor.model.TaskType;
import digital.inception.executor.model.TaskTypeNotFoundException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import java.util.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * The <b>IExecutorApiController</b> interface.
 *
 * @author Marcus Portmann
 */
@Tag(name = "Executor")
@RequestMapping(value = "/api/executor")
// @el (isSecurityDisabled:
// digital.inception.api.SecureApiSecurityExpressionRoot.isSecurityEnabled)
public interface IExecutorApiController {

  /**
   * Cancel the batch.
   *
   * @param batchId the ID for the batch
   * @throws InvalidArgumentException if an argument is invalid
   * @throws BatchTasksNotFoundException if no tasks could be found for the batch
   * @throws ServiceUnavailableException if the batch could not be cancelled
   */
  @Operation(summary = "Cancel the batch", description = "Cancel the batch")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "204", description = "The batch was successfully cancelled"),
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
            description = "No tasks could be found for the batch",
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
      value = "/batches/{batchId}/cancel",
      method = RequestMethod.DELETE,
      produces = "application/json")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  @PreAuthorize(
      "isSecurityDisabled() or hasRole('Administrator') or hasAccessToFunction('Executor.TaskAdministration')")
  void cancelBatch(
      @Parameter(name = "batchId", description = "The ID for the batch", required = true)
          @PathVariable
          String batchId)
      throws InvalidArgumentException, BatchTasksNotFoundException, ServiceUnavailableException;

  /**
   * Cancel the task.
   *
   * @param taskId the ID for the task
   * @throws InvalidArgumentException if an argument is invalid
   * @throws TaskNotFoundException if the task could not be found
   * @throws InvalidTaskStatusException if the status of the task is invalid for the operation
   * @throws ServiceUnavailableException if the task could not be cancelled
   */
  @Operation(summary = "Cancel the task", description = "Cancel the task")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "204", description = "The task was successfully cancelled"),
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
            description = "The task could not be found",
            content =
                @Content(
                    mediaType = "application/problem+json",
                    schema = @Schema(implementation = ProblemDetails.class))),
        @ApiResponse(
            responseCode = "409",
            description = "The status of the task is invalid for the operation",
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
      value = "/tasks/{taskId}/cancel",
      method = RequestMethod.DELETE,
      produces = "application/json")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  @PreAuthorize(
      "isSecurityDisabled() or hasRole('Administrator') or hasAccessToFunction('Executor.TaskAdministration')")
  void cancelTask(
      @Parameter(name = "taskId", description = "The ID for the task", required = true)
          @PathVariable
          UUID taskId)
      throws InvalidArgumentException,
          TaskNotFoundException,
          InvalidTaskStatusException,
          ServiceUnavailableException;

  /**
   * Create the task type.
   *
   * @param taskType the task type
   * @throws InvalidArgumentException if an argument is invalid
   * @throws DuplicateTaskTypeException if the task type already exists
   * @throws ServiceUnavailableException if the task type could not be created
   */
  @Operation(summary = "Create the task type", description = "Create the task type")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "204", description = "The task type was created successfully"),
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
            description = "A task type with the specified code already exists",
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
  @RequestMapping(value = "/task-types", method = RequestMethod.POST, produces = "application/json")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  @PreAuthorize(
      "isSecurityDisabled() or hasRole('Administrator') or hasAccessToFunction('Executor.TaskTypeAdministration')")
  void createTaskType(
      @RequestBody
          @io.swagger.v3.oas.annotations.parameters.RequestBody(
              description = "The task type to create",
              required = true)
          TaskType taskType)
      throws InvalidArgumentException, DuplicateTaskTypeException, ServiceUnavailableException;

  /**
   * Delete the task.
   *
   * @param taskId the ID for the task
   * @throws InvalidArgumentException if an argument is invalid
   * @throws TaskNotFoundException if the task could not be found
   * @throws ServiceUnavailableException if the task could not be deleted
   */
  @Operation(summary = "Delete the task", description = "Delete the task")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "204", description = "The task was deleted successfully"),
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
            description = "The task could not be found",
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
      value = "/tasks/{taskId}",
      method = RequestMethod.DELETE,
      produces = "application/json")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  @PreAuthorize(
      "isSecurityDisabled() or hasRole('Administrator') or hasAccessToFunction('Executor.TaskAdministration')")
  void deleteTask(
      @Parameter(name = "taskId", description = "The ID for the task", required = true)
          @PathVariable
          UUID taskId)
      throws InvalidArgumentException, TaskNotFoundException, ServiceUnavailableException;

  /**
   * Delete the task type.
   *
   * @param taskTypeCode the code for the task type
   * @throws InvalidArgumentException if an argument is invalid
   * @throws TaskTypeNotFoundException if the task type could not be found
   * @throws ServiceUnavailableException if the task type could not be deleted
   */
  @Operation(summary = "Delete the task type", description = "Delete the task type")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "204", description = "The task type was deleted successfully"),
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
            description = "The task type could not be found",
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
      value = "/task-types/{taskTypeCode}",
      method = RequestMethod.DELETE,
      produces = "application/json")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  @PreAuthorize(
      "isSecurityDisabled() or hasRole('Administrator') or hasAccessToFunction('Executor.TaskTypeAdministration')")
  void deleteTaskType(
      @Parameter(name = "taskTypeCode", description = "The code for the task type", required = true)
          @PathVariable
          String taskTypeCode)
      throws InvalidArgumentException, TaskTypeNotFoundException, ServiceUnavailableException;

  /**
   * Retrieve the task
   *
   * @param taskId the ID for the task
   * @return the task
   * @throws InvalidArgumentException if an argument is invalid
   * @throws TaskNotFoundException if the task could not be found
   * @throws ServiceUnavailableException if the task could not be retrieved
   */
  @Operation(summary = "Retrieve the task", description = "Retrieve the task")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "200", description = "OK"),
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
            description = "The task could not be found",
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
      value = "/tasks/{taskId}",
      method = RequestMethod.GET,
      produces = "application/json")
  @ResponseStatus(HttpStatus.OK)
  @PreAuthorize(
      "isSecurityDisabled() or hasRole('Administrator') or hasAccessToFunction('Executor.TaskAdministration')")
  Task getTask(
      @Parameter(name = "taskId", description = "The ID for the task", required = true)
          @PathVariable
          UUID taskId)
      throws InvalidArgumentException, TaskNotFoundException, ServiceUnavailableException;

  /**
   * Retrieve the task events for a task
   *
   * @param taskId the ID for the task
   * @return the task events for the task
   * @throws InvalidArgumentException if an argument is invalid
   * @throws TaskNotFoundException if the task could not be found
   * @throws ServiceUnavailableException if the task events for the task could not be retrieved
   */
  @Operation(
      summary = "Retrieve the task events for a task",
      description = "Retrieve the task events for a task")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "200", description = "OK"),
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
            description = "The task could not be found",
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
      value = "/tasks/{taskId}/task-events",
      method = RequestMethod.GET,
      produces = "application/json")
  @ResponseStatus(HttpStatus.OK)
  @PreAuthorize(
      "isSecurityDisabled() or hasRole('Administrator') or hasAccessToFunction('Executor.TaskAdministration')")
  List<TaskEvent> getTaskEventsForTask(
      @Parameter(name = "taskId", description = "The ID for the task", required = true)
          @PathVariable
          UUID taskId)
      throws InvalidArgumentException, TaskNotFoundException, ServiceUnavailableException;

  /**
   * Retrieve the summaries for the tasks.
   *
   * @param type the optional task type code filter to apply to the task summaries
   * @param status the optional status filter to apply to the task summaries
   * @param filter the optional filter to apply to the task summaries
   * @param sortBy the optional method used to sort the task summaries e.g. by type
   * @param sortDirection the optional sort direction to apply to the task summaries
   * @param pageIndex the optional page index
   * @param pageSize the optional page size
   * @return the summaries for the tasks
   * @throws InvalidArgumentException if an argument is invalid
   * @throws ServiceUnavailableException if the tasks summaries could not be retrieved
   */
  @Operation(summary = "Retrieve the task summaries", description = "Retrieve the task summaries")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "200", description = "OK"),
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
            responseCode = "500",
            description =
                "An error has occurred and the request could not be processed at this time",
            content =
                @Content(
                    mediaType = "application/problem+json",
                    schema = @Schema(implementation = ProblemDetails.class)))
      })
  @RequestMapping(
      value = "/task-summaries",
      method = RequestMethod.GET,
      produces = "application/json")
  @ResponseStatus(HttpStatus.OK)
  @PreAuthorize(
      "isSecurityDisabled() or hasRole('Administrator') or hasAccessToFunction('Executor.TaskAdministration')")
  TaskSummaries getTaskSummaries(
      @Parameter(
              name = "type",
              description = "The optional task type code filter to apply to the task summaries")
          @RequestParam(value = "type", required = false)
          String type,
      @Parameter(
              name = "status",
              description = "The optional status filter to apply to the task summaries")
          @RequestParam(value = "status", required = false)
          TaskStatus status,
      @Parameter(
              name = "filter",
              description = "The optional filter to apply to the task summaries")
          @RequestParam(value = "filter", required = false)
          String filter,
      @Parameter(
              name = "sortBy",
              description = "The optional method used to sort the task summaries e.g. by type")
          @RequestParam(value = "sortBy", required = false)
          TaskSortBy sortBy,
      @Parameter(
              name = "sortDirection",
              description = "The optional sort direction to apply to the task summaries")
          @RequestParam(value = "sortDirection", required = false)
          SortDirection sortDirection,
      @Parameter(name = "pageIndex", description = "The optional page index", example = "0")
          @RequestParam(value = "pageIndex", required = false, defaultValue = "0")
          Integer pageIndex,
      @Parameter(name = "pageSize", description = "The optional page size", example = "10")
          @RequestParam(value = "pageSize", required = false, defaultValue = "10")
          Integer pageSize)
      throws InvalidArgumentException, ServiceUnavailableException;

  /**
   * Retrieve the task type
   *
   * @param taskTypeCode the code for the task type
   * @return the task type
   * @throws InvalidArgumentException if an argument is invalid
   * @throws TaskTypeNotFoundException if the task type could not be found
   * @throws ServiceUnavailableException if the task type could not be retrieved
   */
  @Operation(summary = "Retrieve the task type", description = "Retrieve the task type")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "200", description = "OK"),
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
            description = "The task type could not be found",
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
      value = "/task-types/{taskTypeCode}",
      method = RequestMethod.GET,
      produces = "application/json")
  @ResponseStatus(HttpStatus.OK)
  @PreAuthorize(
      "isSecurityDisabled() or hasRole('Administrator') or hasAccessToFunction('Executor.TaskTypeAdministration')")
  TaskType getTaskType(
      @Parameter(name = "taskTypeCode", description = "The code for the task type", required = true)
          @PathVariable
          String taskTypeCode)
      throws InvalidArgumentException, TaskTypeNotFoundException, ServiceUnavailableException;

  /**
   * Retrieve the name of the task type.
   *
   * @param taskTypeCode the code for the task type
   * @return the name of the task type
   * @throws InvalidArgumentException if an argument is invalid
   * @throws TaskTypeNotFoundException if the task type could not be found
   * @throws ServiceUnavailableException if the name of the task type could not be retrieved
   */
  @Operation(
      summary = "Retrieve the name of the task type",
      description = "Retrieve the name of the task type")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "200", description = "OK"),
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
            description = "The task type could not be found",
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
      value = "/task-types/{taskTypeCode}/name",
      method = RequestMethod.GET,
      produces = "application/json")
  @ResponseStatus(HttpStatus.OK)
  @PreAuthorize(
      "isSecurityDisabled() or hasRole('Administrator') or hasAccessToFunction('Executor.TaskTypeAdministration')")
  String getTaskTypeName(
      @Parameter(name = "taskTypeCode", description = "The code for the task type", required = true)
          @PathVariable
          String taskTypeCode)
      throws InvalidArgumentException, TaskTypeNotFoundException, ServiceUnavailableException;

  /**
   * Retrieve all the task types.
   *
   * @return the task types
   * @throws ServiceUnavailableException if the task types could not be retrieved
   */
  @Operation(summary = "Retrieve all the task types", description = "Retrieve all the task types")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "200", description = "OK"),
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
  @RequestMapping(value = "/task-types", method = RequestMethod.GET, produces = "application/json")
  @ResponseStatus(HttpStatus.OK)
  @PreAuthorize(
      "isSecurityDisabled() or hasRole('Administrator') or hasAccessToFunction('Executor.TaskTypeAdministration')")
  List<TaskType> getTaskTypes() throws ServiceUnavailableException;

  /**
   * Queue a task for execution.
   *
   * @param queueTaskRequest the request to queue a task for execution
   * @return the ID for the task that has been queued for execution
   * @throws InvalidArgumentException if an argument is invalid
   * @throws TaskTypeNotFoundException if the task type could not be found
   * @throws ServiceUnavailableException if the task could not be queued for execution
   */
  @Operation(summary = "Queue a task for execution", description = "Queue a task for execution")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "200", description = "OK"),
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
            description = "The task type could not be found",
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
  @RequestMapping(value = "/queue-task", method = RequestMethod.POST, produces = "application/json")
  @ResponseStatus(HttpStatus.OK)
  @PreAuthorize(
      "isSecurityDisabled() or hasRole('Administrator') or hasAccessToFunction('Executor.TaskAdministration')")
  UUID queueTask(
      @io.swagger.v3.oas.annotations.parameters.RequestBody(
              description = "The request to queue a task for execution",
              required = true)
          @RequestBody
          QueueTaskRequest queueTaskRequest)
      throws InvalidArgumentException, TaskTypeNotFoundException, ServiceUnavailableException;

  /**
   * Suspend the batch.
   *
   * @param batchId the ID for the batch
   * @throws InvalidArgumentException if an argument is invalid
   * @throws BatchTasksNotFoundException if no tasks could be found for the batch
   * @throws ServiceUnavailableException if the batch could not be suspended
   */
  @Operation(summary = "Suspend the batch", description = "Suspend the batch")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "204", description = "The batch was successfully suspended"),
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
            description = "No tasks could be found for the batch",
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
      value = "/batches/{batchId}/suspend",
      method = RequestMethod.PATCH,
      produces = "application/json")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  @PreAuthorize(
      "isSecurityDisabled() or hasRole('Administrator') or hasAccessToFunction('Executor.TaskAdministration')")
  void suspendBatch(
      @Parameter(name = "batchId", description = "The ID for the batch", required = true)
          @PathVariable
          String batchId)
      throws InvalidArgumentException, BatchTasksNotFoundException, ServiceUnavailableException;

  /**
   * Suspend the task.
   *
   * @param taskId the ID for the task
   * @throws InvalidArgumentException if an argument is invalid
   * @throws TaskNotFoundException if the task could not be found
   * @throws InvalidTaskStatusException if the status of the task is invalid for the operation
   * @throws ServiceUnavailableException if the task could not be suspended
   */
  @Operation(summary = "Suspend the task", description = "Suspend the task")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "204", description = "The task was successfully suspended"),
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
            description = "The task could not be found",
            content =
                @Content(
                    mediaType = "application/problem+json",
                    schema = @Schema(implementation = ProblemDetails.class))),
        @ApiResponse(
            responseCode = "409",
            description = "The status of the task is invalid for the operation",
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
      value = "/tasks/{taskId}/suspend",
      method = RequestMethod.PATCH,
      produces = "application/json")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  @PreAuthorize(
      "isSecurityDisabled() or hasRole('Administrator') or hasAccessToFunction('Executor.TaskAdministration')")
  void suspendTask(
      @Parameter(name = "taskId", description = "The ID for the task", required = true)
          @PathVariable
          UUID taskId)
      throws InvalidArgumentException,
          TaskNotFoundException,
          InvalidTaskStatusException,
          ServiceUnavailableException;

  /**
   * Unsuspend the batch.
   *
   * @param batchId the ID for the batch
   * @throws InvalidArgumentException if an argument is invalid
   * @throws BatchTasksNotFoundException if no tasks could be found for the batch
   * @throws ServiceUnavailableException if the batch could not be unsuspended
   */
  @Operation(summary = "Unsuspend the batch", description = "Unsuspend the batch")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "204", description = "The batch was successfully unsuspended"),
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
            description = "No tasks could be found for the batch",
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
      value = "/batches/{batchId}/unsuspend",
      method = RequestMethod.PATCH,
      produces = "application/json")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  @PreAuthorize(
      "isSecurityDisabled() or hasRole('Administrator') or hasAccessToFunction('Executor.TaskAdministration')")
  void unsuspendBatch(
      @Parameter(name = "batchId", description = "The ID for the batch", required = true)
          @PathVariable
          String batchId)
      throws InvalidArgumentException, BatchTasksNotFoundException, ServiceUnavailableException;

  /**
   * Unsuspend the task.
   *
   * @param taskId the ID for the task
   * @throws InvalidArgumentException if an argument is invalid
   * @throws TaskNotFoundException if the task could not be found
   * @throws InvalidTaskStatusException if the status of the task is invalid for the operation
   * @throws ServiceUnavailableException if the task could not be unsuspended
   */
  @Operation(summary = "Unsuspend the task", description = "Unsuspend the task")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "204", description = "The task was successfully unsuspended"),
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
            description = "The task could not be found",
            content =
                @Content(
                    mediaType = "application/problem+json",
                    schema = @Schema(implementation = ProblemDetails.class))),
        @ApiResponse(
            responseCode = "409",
            description = "The status of the task is invalid for the operation",
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
      value = "/tasks/{taskId}/unsuspend",
      method = RequestMethod.PATCH,
      produces = "application/json")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  @PreAuthorize(
      "isSecurityDisabled() or hasRole('Administrator') or hasAccessToFunction('Executor.TaskAdministration')")
  void unsuspendTask(
      @Parameter(name = "taskId", description = "The ID for the task", required = true)
          @PathVariable
          UUID taskId)
      throws InvalidArgumentException,
          TaskNotFoundException,
          InvalidTaskStatusException,
          ServiceUnavailableException;

  /**
   * Update the task type.
   *
   * @param taskTypeCode the code for the task type
   * @param taskType the task type
   * @throws InvalidArgumentException if an argument is invalid
   * @throws TaskTypeNotFoundException if the task type could not be found
   * @throws ServiceUnavailableException if the task type could not be updated
   */
  @Operation(summary = "Update the task type", description = "Update the task type")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "204", description = "The task type was updated successfully"),
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
            description = "The task type could not be found",
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
      value = "/task-types/{taskTypeCode}",
      method = RequestMethod.PUT,
      produces = "application/json")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  @PreAuthorize(
      "isSecurityDisabled() or hasRole('Administrator') or hasAccessToFunction('Executor.TaskTypeAdministration')")
  void updateTaskType(
      @Parameter(name = "taskTypeCode", description = "The code for the task type", required = true)
          @PathVariable
          String taskTypeCode,
      @io.swagger.v3.oas.annotations.parameters.RequestBody(
              description = "The task type to update",
              required = true)
          @RequestBody
          TaskType taskType)
      throws InvalidArgumentException, TaskTypeNotFoundException, ServiceUnavailableException;
}
