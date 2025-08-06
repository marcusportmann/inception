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

package digital.inception.operations.controller;

import digital.inception.core.api.ProblemDetails;
import digital.inception.core.exception.InvalidArgumentException;
import digital.inception.core.exception.ServiceUnavailableException;
import digital.inception.core.sorting.SortDirection;
import digital.inception.operations.exception.DuplicateWorkflowDefinitionCategoryException;
import digital.inception.operations.exception.DuplicateWorkflowDefinitionVersionException;
import digital.inception.operations.exception.DuplicateWorkflowEngineException;
import digital.inception.operations.exception.WorkflowDefinitionCategoryNotFoundException;
import digital.inception.operations.exception.WorkflowDefinitionNotFoundException;
import digital.inception.operations.exception.WorkflowDefinitionVersionNotFoundException;
import digital.inception.operations.exception.WorkflowEngineNotFoundException;
import digital.inception.operations.exception.WorkflowNotFoundException;
import digital.inception.operations.exception.WorkflowNoteNotFoundException;
import digital.inception.operations.exception.WorkflowStepNotFoundException;
import digital.inception.operations.model.CreateWorkflowNoteRequest;
import digital.inception.operations.model.FinalizeWorkflowRequest;
import digital.inception.operations.model.FinalizeWorkflowStepRequest;
import digital.inception.operations.model.InitiateWorkflowRequest;
import digital.inception.operations.model.InitiateWorkflowStepRequest;
import digital.inception.operations.model.UpdateWorkflowNoteRequest;
import digital.inception.operations.model.UpdateWorkflowRequest;
import digital.inception.operations.model.Workflow;
import digital.inception.operations.model.WorkflowDefinition;
import digital.inception.operations.model.WorkflowDefinitionCategory;
import digital.inception.operations.model.WorkflowDefinitionSummary;
import digital.inception.operations.model.WorkflowDocumentSortBy;
import digital.inception.operations.model.WorkflowDocuments;
import digital.inception.operations.model.WorkflowEngine;
import digital.inception.operations.model.WorkflowNote;
import digital.inception.operations.model.WorkflowNoteSortBy;
import digital.inception.operations.model.WorkflowNotes;
import digital.inception.operations.model.WorkflowSortBy;
import digital.inception.operations.model.WorkflowStatus;
import digital.inception.operations.model.WorkflowSummaries;
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
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * The {@code WorkflowApiController} interface.
 *
 * @author Marcus Portmann
 */
@Tag(name = "Workflow")
@RequestMapping(value = "/api/operations")
// @el (isSecurityDisabled:
// digital.inception.api.SecureApiSecurityExpressionRoot.isSecurityEnabled)
public interface WorkflowApiController {

  /**
   * Create the workflow definition version.
   *
   * @param tenantId the ID for the tenant
   * @param workflowDefinitionCategoryId the ID for the workflow definition category
   * @param workflowDefinition the workflow definition version
   * @throws InvalidArgumentException if an argument is invalid
   * @throws DuplicateWorkflowDefinitionVersionException if the workflow definition version already
   *     exists
   * @throws WorkflowDefinitionCategoryNotFoundException if the workflow definition category could
   *     not be found
   * @throws ServiceUnavailableException if the workflow definition version could not be created
   */
  @Operation(
      summary = "Create the workflow definition version",
      description = "Create the workflow definition version")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "204",
            description = "The workflow definition version was created"),
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
            description = "The workflow definition category could not be found",
            content =
                @Content(
                    mediaType = "application/problem+json",
                    schema = @Schema(implementation = ProblemDetails.class))),
        @ApiResponse(
            responseCode = "409",
            description = "A workflow definition with the specified ID and version already exists",
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
      value = "/workflow-definition-categories/{workflowDefinitionCategoryId}/workflow-definitions",
      method = RequestMethod.POST,
      produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.NO_CONTENT)
  @PreAuthorize(
      "isSecurityDisabled() or hasRole('Administrator') or hasAuthority('FUNCTION_Operations.OperationsAdministration')")
  void createWorkflowDefinition(
      @Parameter(
              name = "Tenant-ID",
              description = "The ID for the tenant",
              example = "00000000-0000-0000-0000-000000000000")
          @RequestHeader(
              name = "Tenant-ID",
              defaultValue = "00000000-0000-0000-0000-000000000000",
              required = false)
          UUID tenantId,
      @Parameter(
              name = "workflowDefinitionCategoryId",
              description = "The ID for the workflow definition category",
              required = true)
          @PathVariable
          String workflowDefinitionCategoryId,
      @io.swagger.v3.oas.annotations.parameters.RequestBody(
              description = "The workflow definition version to create",
              required = true)
          @RequestBody
          WorkflowDefinition workflowDefinition)
      throws InvalidArgumentException,
          DuplicateWorkflowDefinitionVersionException,
          WorkflowDefinitionCategoryNotFoundException,
          ServiceUnavailableException;

  /**
   * Create the workflow definition category.
   *
   * @param tenantId the ID for the tenant
   * @param workflowDefinitionCategory the workflow definition category
   * @throws InvalidArgumentException if an argument is invalid
   * @throws DuplicateWorkflowDefinitionCategoryException if the workflow definition category
   *     already exists
   * @throws ServiceUnavailableException if the workflow definition category could not be created
   */
  @Operation(
      summary = "Create the workflow definition category",
      description = "Create the workflow definition category")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "204",
            description = "The workflow definition category was created"),
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
            description = "A workflow definition category with the specified ID already exists",
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
      value = "/workflow-definition-categories",
      method = RequestMethod.POST,
      produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.NO_CONTENT)
  @PreAuthorize(
      "isSecurityDisabled() or hasRole('Administrator') or hasAuthority('FUNCTION_Operations.OperationsAdministration')")
  void createWorkflowDefinitionCategory(
      @Parameter(
              name = "Tenant-ID",
              description = "The ID for the tenant",
              example = "00000000-0000-0000-0000-000000000000")
          @RequestHeader(
              name = "Tenant-ID",
              defaultValue = "00000000-0000-0000-0000-000000000000",
              required = false)
          UUID tenantId,
      @io.swagger.v3.oas.annotations.parameters.RequestBody(
              description = "The workflow definition category to create",
              required = true)
          @RequestBody
          WorkflowDefinitionCategory workflowDefinitionCategory)
      throws InvalidArgumentException,
          DuplicateWorkflowDefinitionCategoryException,
          ServiceUnavailableException;

  /**
   * Create the workflow engine.
   *
   * @param workflowEngine the workflow engine
   * @throws InvalidArgumentException if an argument is invalid
   * @throws DuplicateWorkflowEngineException if the workflow engine already exists
   * @throws ServiceUnavailableException if the workflow engine could not be created
   */
  @Operation(summary = "Create the workflow engine", description = "Create the workflow engine")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "204", description = "The workflow engine was created"),
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
            description = "A workflow engine with the specified ID already exists",
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
      value = "/workflow-engines",
      method = RequestMethod.POST,
      produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.NO_CONTENT)
  @PreAuthorize(
      "isSecurityDisabled() or hasRole('Administrator') or hasAuthority('FUNCTION_Operations.OperationsAdministration')")
  void createWorkflowEngine(
      @io.swagger.v3.oas.annotations.parameters.RequestBody(
              description = "The workflow engine to create",
              required = true)
          @RequestBody
          WorkflowEngine workflowEngine)
      throws InvalidArgumentException,
          DuplicateWorkflowEngineException,
          ServiceUnavailableException;

  /**
   * Create the workflow note.
   *
   * @param tenantId the ID for the tenant
   * @param createWorkflowNoteRequest the request to create the workflow note
   * @return the ID for the workflow note
   * @throws InvalidArgumentException if an argument is invalid
   * @throws WorkflowNotFoundException if the workflow could not be found
   * @throws ServiceUnavailableException if the workflow note could not be created
   */
  @Operation(summary = "Create the workflow note", description = "Create the workflow note")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "200", description = "The workflow note was created"),
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
            description = "The workflow could not be found",
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
      value = "/create-workflow-note",
      method = RequestMethod.POST,
      produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.OK)
  @PreAuthorize(
      "isSecurityDisabled() or hasRole('Administrator') or hasAuthority('FUNCTION_Operations.OperationsAdministration') or hasAuthority('FUNCTION_Operations.WorkflowAdministration') or hasAuthority('FUNCTION_Operations.Indexing')")
  UUID createWorkflowNote(
      @Parameter(
              name = "Tenant-ID",
              description = "The ID for the tenant",
              example = "00000000-0000-0000-0000-000000000000")
          @RequestHeader(
              name = "Tenant-ID",
              defaultValue = "00000000-0000-0000-0000-000000000000",
              required = false)
          UUID tenantId,
      @io.swagger.v3.oas.annotations.parameters.RequestBody(
              description = "The request to create the workflow note",
              required = true)
          @RequestBody
          CreateWorkflowNoteRequest createWorkflowNoteRequest)
      throws InvalidArgumentException, WorkflowNotFoundException, ServiceUnavailableException;

  /**
   * Delete the workflow.
   *
   * @param tenantId the ID for the tenant
   * @param workflowId the ID for the workflow
   * @throws InvalidArgumentException if an argument is invalid
   * @throws WorkflowNotFoundException if the workflow could not be found
   * @throws ServiceUnavailableException if the workflow could not be deleted
   */
  @Operation(summary = "Delete the workflow", description = "Delete the workflow")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "204", description = "The workflow was deleted"),
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
            description = "The workflow could not be found",
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
      value = "/workflows/{workflowId}",
      method = RequestMethod.DELETE,
      produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.NO_CONTENT)
  @PreAuthorize(
      "isSecurityDisabled() or hasRole('Administrator') or hasAuthority('FUNCTION_Operations.OperationsAdministration') or hasAuthority('FUNCTION_Operations.WorkflowAdministration')")
  void deleteWorkflow(
      @Parameter(
              name = "Tenant-ID",
              description = "The ID for the tenant",
              example = "00000000-0000-0000-0000-000000000000")
          @RequestHeader(
              name = "Tenant-ID",
              defaultValue = "00000000-0000-0000-0000-000000000000",
              required = false)
          UUID tenantId,
      @Parameter(name = "workflowId", description = "The ID for the workflow", required = true)
          @PathVariable
          UUID workflowId)
      throws InvalidArgumentException, WorkflowNotFoundException, ServiceUnavailableException;

  /**
   * Delete all versions of the workflow definition.
   *
   * @param tenantId the ID for the tenant
   * @param workflowDefinitionCategoryId the ID for the workflow definition category
   * @param workflowDefinitionId the ID for the workflow definition
   * @throws InvalidArgumentException if an argument is invalid
   * @throws WorkflowDefinitionCategoryNotFoundException if the workflow definition category could
   *     not be found
   * @throws WorkflowDefinitionNotFoundException if the workflow definition could not be found
   * @throws ServiceUnavailableException if the workflow definition could not be deleted
   */
  @Operation(
      summary = "Delete all versions of the workflow definition",
      description = "Delete all versions of the workflow definition")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "204", description = "The workflow definition was deleted"),
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
            description =
                "The workflow definition category or workflow definition could not be found",
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
      value =
          "/workflow-definition-categories/{workflowDefinitionCategoryId}/workflow-definitions/{workflowDefinitionId}",
      method = RequestMethod.DELETE,
      produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.NO_CONTENT)
  @PreAuthorize(
      "isSecurityDisabled() or hasRole('Administrator') or hasAuthority('FUNCTION_Operations.OperationsAdministration')")
  void deleteWorkflowDefinition(
      @Parameter(
              name = "Tenant-ID",
              description = "The ID for the tenant",
              example = "00000000-0000-0000-0000-000000000000")
          @RequestHeader(
              name = "Tenant-ID",
              defaultValue = "00000000-0000-0000-0000-000000000000",
              required = false)
          UUID tenantId,
      @Parameter(
              name = "workflowDefinitionCategoryId",
              description = "The ID for the workflow definition category",
              required = true)
          @PathVariable
          String workflowDefinitionCategoryId,
      @Parameter(
              name = "workflowDefinitionId",
              description = "The ID for the workflow definition",
              required = true)
          @PathVariable
          String workflowDefinitionId)
      throws InvalidArgumentException,
          WorkflowDefinitionCategoryNotFoundException,
          WorkflowDefinitionNotFoundException,
          ServiceUnavailableException;

  /**
   * Delete the workflow definition category.
   *
   * @param tenantId the ID for the tenant
   * @param workflowDefinitionCategoryId the ID for the workflow definition category
   * @throws InvalidArgumentException if an argument is invalid
   * @throws WorkflowDefinitionCategoryNotFoundException if the workflow definition category could
   *     not be found
   * @throws ServiceUnavailableException if the workflow definition category could not be deleted
   */
  @Operation(
      summary = "Delete the workflow definition category",
      description = "Delete the workflow definition category")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "204",
            description = "The workflow definition category was deleted"),
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
            description = "The workflow definition category could not be found",
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
      value = "/workflow-definition-categories/{workflowDefinitionCategoryId}",
      method = RequestMethod.DELETE,
      produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.NO_CONTENT)
  @PreAuthorize(
      "isSecurityDisabled() or hasRole('Administrator') or hasAuthority('FUNCTION_Operations.OperationsAdministration')")
  void deleteWorkflowDefinitionCategory(
      @Parameter(
              name = "Tenant-ID",
              description = "The ID for the tenant",
              example = "00000000-0000-0000-0000-000000000000")
          @RequestHeader(
              name = "Tenant-ID",
              defaultValue = "00000000-0000-0000-0000-000000000000",
              required = false)
          UUID tenantId,
      @Parameter(
              name = "workflowDefinitionCategoryId",
              description = "The ID for the workflow definition category",
              required = true)
          @PathVariable
          String workflowDefinitionCategoryId)
      throws InvalidArgumentException,
          WorkflowDefinitionCategoryNotFoundException,
          ServiceUnavailableException;

  /**
   * Delete the workflow definition version.
   *
   * @param tenantId the ID for the tenant
   * @param workflowDefinitionCategoryId the ID for the workflow definition category
   * @param workflowDefinitionId the ID for the workflow definition
   * @param workflowDefinitionVersion the version of the workflow definition
   * @throws InvalidArgumentException if an argument is invalid
   * @throws WorkflowDefinitionCategoryNotFoundException if the workflow definition category could
   *     not be found
   * @throws WorkflowDefinitionNotFoundException if the workflow definition could not be found
   * @throws WorkflowDefinitionVersionNotFoundException if the workflow definition version could not
   *     be found
   * @throws ServiceUnavailableException if the workflow definition version could not be deleted
   */
  @Operation(
      summary = "Delete the workflow definition version",
      description = "Delete the workflow definition version")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "204",
            description = "The workflow definition version was deleted"),
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
            description =
                "The workflow definition category or workflow definition version could not be found",
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
      value =
          "/workflow-definition-categories/{workflowDefinitionCategoryId}/workflow-definitions/{workflowDefinitionId}/{workflowDefinitionVersion}",
      method = RequestMethod.DELETE,
      produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.NO_CONTENT)
  @PreAuthorize(
      "isSecurityDisabled() or hasRole('Administrator') or hasAuthority('FUNCTION_Operations.OperationsAdministration')")
  void deleteWorkflowDefinitionVersion(
      @Parameter(
              name = "Tenant-ID",
              description = "The ID for the tenant",
              example = "00000000-0000-0000-0000-000000000000")
          @RequestHeader(
              name = "Tenant-ID",
              defaultValue = "00000000-0000-0000-0000-000000000000",
              required = false)
          UUID tenantId,
      @Parameter(
              name = "workflowDefinitionCategoryId",
              description = "The ID for the workflow definition category",
              required = true)
          @PathVariable
          String workflowDefinitionCategoryId,
      @Parameter(
              name = "workflowDefinitionId",
              description = "The ID for the workflow definition",
              required = true)
          @PathVariable
          String workflowDefinitionId,
      @Parameter(
              name = "workflowDefinitionVersion",
              description = "The version of the workflow definition",
              required = true)
          @PathVariable
          int workflowDefinitionVersion)
      throws InvalidArgumentException,
          WorkflowDefinitionCategoryNotFoundException,
          WorkflowDefinitionNotFoundException,
          WorkflowDefinitionVersionNotFoundException,
          ServiceUnavailableException;

  /**
   * Delete the workflow engine.
   *
   * @param workflowEngineId the ID for the workflow engine
   * @throws InvalidArgumentException if an argument is invalid
   * @throws WorkflowEngineNotFoundException if the workflow engine could not be found
   * @throws ServiceUnavailableException if the workflow engine could not be deleted
   */
  @Operation(summary = "Delete the workflow engine", description = "Delete the workflow engine")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "204", description = "The workflow engine was deleted"),
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
            description = "The workflow engine could not be found",
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
      value = "/workflow-engines/{workflowEngineId}",
      method = RequestMethod.DELETE,
      produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.NO_CONTENT)
  @PreAuthorize(
      "isSecurityDisabled() or hasRole('Administrator') or hasAuthority('FUNCTION_Operations.OperationsAdministration')")
  void deleteWorkflowEngine(
      @Parameter(
              name = "workflowEngineId",
              description = "The ID for the workflow engine",
              required = true)
          @PathVariable
          String workflowEngineId)
      throws InvalidArgumentException, WorkflowEngineNotFoundException, ServiceUnavailableException;

  /**
   * Delete the workflow note.
   *
   * @param tenantId the ID for the tenant
   * @param workflowId the ID for the workflow
   * @param workflowNoteId the ID for the workflow note
   * @throws InvalidArgumentException if an argument is invalid
   * @throws WorkflowNotFoundException if the workflow could not be found
   * @throws WorkflowNoteNotFoundException if the workflow note could not be found
   * @throws ServiceUnavailableException if the workflow note could not be deleted
   */
  @Operation(summary = "Delete the workflow note", description = "Delete the workflow note")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "204", description = "The workflow note was deleted"),
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
            description = "The workflow or workflow note could not be found",
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
      value = "/workflows/{workflowId}/notes/{workflowNoteId}",
      method = RequestMethod.DELETE,
      produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.NO_CONTENT)
  @PreAuthorize(
      "isSecurityDisabled() or hasRole('Administrator') or hasAuthority('FUNCTION_Operations.OperationsAdministration') or hasAuthority('FUNCTION_Operations.WorkflowAdministration') or hasAuthority('FUNCTION_Operations.Indexing')")
  void deleteWorkflowNote(
      @Parameter(
              name = "Tenant-ID",
              description = "The ID for the tenant",
              example = "00000000-0000-0000-0000-000000000000")
          @RequestHeader(
              name = "Tenant-ID",
              defaultValue = "00000000-0000-0000-0000-000000000000",
              required = false)
          UUID tenantId,
      @Parameter(name = "workflowId", description = "The ID for the workflow", required = true)
          @PathVariable
          UUID workflowId,
      @Parameter(
              name = "workflowNoteId",
              description = "The ID for the workflow note",
              required = true)
          @PathVariable
          UUID workflowNoteId)
      throws InvalidArgumentException,
          WorkflowNotFoundException,
          WorkflowNoteNotFoundException,
          ServiceUnavailableException;

  /**
   * Finalize a workflow.
   *
   * @param tenantId the ID for the tenant
   * @param finalizeWorkflowRequest the request to finalize a workflow
   * @throws InvalidArgumentException if an argument is invalid
   * @throws WorkflowNotFoundException if the workflow could not be found
   * @throws ServiceUnavailableException if the workflow could not be finalized
   */
  @Operation(summary = "Finalize a workflow", description = "Finalize a workflow")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "204", description = "The workflow was finalized"),
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
            description = "The workflow could not be found",
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
      value = "/finalize-workflow",
      method = RequestMethod.POST,
      produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.NO_CONTENT)
  @PreAuthorize(
      "isSecurityDisabled() or hasRole('Administrator') or hasAuthority('FUNCTION_Operations.OperationsAdministration') or hasAuthority('FUNCTION_Operations.WorkflowAdministration')")
  void finalizeWorkflow(
      @Parameter(
              name = "Tenant-ID",
              description = "The ID for the tenant",
              example = "00000000-0000-0000-0000-000000000000")
          @RequestHeader(
              name = "Tenant-ID",
              defaultValue = "00000000-0000-0000-0000-000000000000",
              required = false)
          UUID tenantId,
      @io.swagger.v3.oas.annotations.parameters.RequestBody(
              description = "The request to finalize the workflow",
              required = true)
          @RequestBody
          FinalizeWorkflowRequest finalizeWorkflowRequest)
      throws InvalidArgumentException, WorkflowNotFoundException, ServiceUnavailableException;

  /**
   * Finalize a workflow step.
   *
   * @param tenantId the ID for the tenant
   * @param finalizeWorkflowStepRequest the request to finalize a workflow step
   * @throws InvalidArgumentException if an argument is invalid
   * @throws WorkflowNotFoundException if the workflow could not be found
   * @throws WorkflowStepNotFoundException if the workflow step could not be found
   * @throws ServiceUnavailableException if the workflow step could not be finalized
   */
  @Operation(summary = "Finalize a workflow step", description = "Finalize a workflow step")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "204", description = "The workflow step was finalized"),
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
            description = "The workflow or workflow step could not be found",
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
      value = "/finalize-workflow-step",
      method = RequestMethod.POST,
      produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.NO_CONTENT)
  @PreAuthorize(
      "isSecurityDisabled() or hasRole('Administrator') or hasAuthority('FUNCTION_Operations.OperationsAdministration') or hasAuthority('FUNCTION_Operations.WorkflowAdministration')")
  void finalizeWorkflowStep(
      @Parameter(
              name = "Tenant-ID",
              description = "The ID for the tenant",
              example = "00000000-0000-0000-0000-000000000000")
          @RequestHeader(
              name = "Tenant-ID",
              defaultValue = "00000000-0000-0000-0000-000000000000",
              required = false)
          UUID tenantId,
      @io.swagger.v3.oas.annotations.parameters.RequestBody(
              description = "The request to finalize the workflow step",
              required = true)
          @RequestBody
          FinalizeWorkflowStepRequest finalizeWorkflowStepRequest)
      throws InvalidArgumentException,
          WorkflowNotFoundException,
          WorkflowStepNotFoundException,
          ServiceUnavailableException;

  /**
   * Retrieve the workflow.
   *
   * @param tenantId the ID for the tenant
   * @param workflowId the ID for the workflow
   * @return the workflow
   * @throws InvalidArgumentException if an argument is invalid
   * @throws WorkflowNotFoundException if the workflow could not be found
   * @throws ServiceUnavailableException if the workflow could not be retrieved
   */
  @Operation(summary = "Retrieve the workflow", description = "Retrieve the workflow")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "200", description = "The workflow was retrieved"),
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
            description = "The workflow could not be found",
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
      value = "/workflows/{workflowId}",
      method = RequestMethod.GET,
      produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.OK)
  @PreAuthorize(
      "isSecurityDisabled() or hasRole('Administrator') or hasAuthority('FUNCTION_Operations.OperationsAdministration') or hasAuthority('FUNCTION_Operations.WorkflowAdministration') or hasAuthority('FUNCTION_Operations.Indexing')")
  Workflow getWorkflow(
      @Parameter(
              name = "Tenant-ID",
              description = "The ID for the tenant",
              example = "00000000-0000-0000-0000-000000000000")
          @RequestHeader(
              name = "Tenant-ID",
              defaultValue = "00000000-0000-0000-0000-000000000000",
              required = false)
          UUID tenantId,
      @Parameter(name = "workflowId", description = "The ID for the workflow", required = true)
          @PathVariable
          UUID workflowId)
      throws InvalidArgumentException, WorkflowNotFoundException, ServiceUnavailableException;

  /**
   * Retrieve the latest version of the workflow definition.
   *
   * @param tenantId the ID for the tenant
   * @param workflowDefinitionCategoryId the ID for the workflow definition category
   * @param workflowDefinitionId the ID for the workflow definition
   * @return the latest version of the workflow definition
   * @throws InvalidArgumentException if an argument is invalid
   * @throws WorkflowDefinitionCategoryNotFoundException if the workflow definition category could
   *     not be found
   * @throws WorkflowDefinitionNotFoundException if the workflow definition could not be found
   * @throws ServiceUnavailableException if the latest version of the workflow definition could not
   *     be retrieved
   */
  @Operation(
      summary = "Retrieve the latest version of the workflow definition",
      description = "Retrieve the latest version of the workflow definition")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "200",
            description = "The latest version of the workflow definition was retrieved"),
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
            description =
                "The workflow definition category or workflow definition could not be found",
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
      value =
          "/workflow-definition-categories/{workflowDefinitionCategoryId}/workflow-definitions/{workflowDefinitionId}",
      method = RequestMethod.GET,
      produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.OK)
  @PreAuthorize(
      "isSecurityDisabled() or hasRole('Administrator') or hasAuthority('FUNCTION_Operations.OperationsAdministration')")
  WorkflowDefinition getWorkflowDefinition(
      @Parameter(
              name = "Tenant-ID",
              description = "The ID for the tenant",
              example = "00000000-0000-0000-0000-000000000000")
          @RequestHeader(
              name = "Tenant-ID",
              defaultValue = "00000000-0000-0000-0000-000000000000",
              required = false)
          UUID tenantId,
      @Parameter(
              name = "workflowDefinitionCategoryId",
              description = "The ID for the workflow definition category",
              required = true)
          @PathVariable
          String workflowDefinitionCategoryId,
      @Parameter(
              name = "workflowDefinitionId",
              description = "The ID for the workflow definition",
              required = true)
          @PathVariable
          String workflowDefinitionId)
      throws InvalidArgumentException,
          WorkflowDefinitionCategoryNotFoundException,
          WorkflowDefinitionNotFoundException,
          ServiceUnavailableException;

  /**
   * Retrieve the workflow definition categories.
   *
   * @param tenantId the ID for the tenant
   * @return the workflow definition categories
   * @throws InvalidArgumentException if an argument is invalid
   * @throws ServiceUnavailableException if the workflow definition categories could not be
   *     retrieved
   */
  @Operation(
      summary = "Retrieve the workflow definition categories",
      description = "Retrieve the workflow definition categories")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "200",
            description = "The workflow definition categories were retrieved"),
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
      value = "/workflow-definition-categories",
      method = RequestMethod.GET,
      produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.OK)
  @PreAuthorize(
      "isSecurityDisabled() or hasRole('Administrator') or hasAuthority('FUNCTION_Operations.OperationsAdministration') or hasAuthority('FUNCTION_Operations.WorkflowAdministration') or hasAuthority('FUNCTION_Operations.Indexing')")
  List<WorkflowDefinitionCategory> getWorkflowDefinitionCategories(
      @Parameter(
              name = "Tenant-ID",
              description = "The ID for the tenant",
              example = "00000000-0000-0000-0000-000000000000")
          @RequestHeader(
              name = "Tenant-ID",
              defaultValue = "00000000-0000-0000-0000-000000000000",
              required = false)
          UUID tenantId)
      throws InvalidArgumentException, ServiceUnavailableException;

  /**
   * Retrieve the workflow definition category.
   *
   * @param tenantId the ID for the tenant
   * @param workflowDefinitionCategoryId the ID for the workflow definition category
   * @return the workflow definition category
   * @throws InvalidArgumentException if an argument is invalid
   * @throws WorkflowDefinitionCategoryNotFoundException if the workflow definition category could
   *     not be found
   * @throws ServiceUnavailableException if the workflow definition category could not be retrieved
   */
  @Operation(
      summary = "Retrieve the workflow definition category",
      description = "Retrieve the workflow definition category")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "200",
            description = "The workflow definition category was retrieved"),
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
            description = "The workflow definition category could not be found",
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
      value = "/workflow-definition-categories/{workflowDefinitionCategoryId}",
      method = RequestMethod.GET,
      produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.OK)
  @PreAuthorize(
      "isSecurityDisabled() or hasRole('Administrator') or hasAuthority('FUNCTION_Operations.OperationsAdministration')")
  WorkflowDefinitionCategory getWorkflowDefinitionCategory(
      @Parameter(
              name = "Tenant-ID",
              description = "The ID for the tenant",
              example = "00000000-0000-0000-0000-000000000000")
          @RequestHeader(
              name = "Tenant-ID",
              defaultValue = "00000000-0000-0000-0000-000000000000",
              required = false)
          UUID tenantId,
      @Parameter(
              name = "workflowDefinitionCategoryId",
              description = "The ID for the workflow definition category",
              required = true)
          @PathVariable
          String workflowDefinitionCategoryId)
      throws InvalidArgumentException,
          WorkflowDefinitionCategoryNotFoundException,
          ServiceUnavailableException;

  /**
   * Retrieve the summaries for the workflow definitions associated with the workflow definition
   * category with the specified ID.
   *
   * @param tenantId the ID for the tenant
   * @param workflowDefinitionCategoryId the ID for the workflow definition category the workflow
   *     definitions are associated with
   * @return the summaries for the workflow definitions associated with the workflow definition
   *     category with the specified ID
   * @throws InvalidArgumentException if an argument is invalid
   * @throws WorkflowDefinitionCategoryNotFoundException if the workflow definition category could
   *     not be found
   * @throws ServiceUnavailableException if the workflow definition summaries could not be retrieved
   */
  @Operation(
      summary = "Retrieve the workflow definition summaries",
      description = "Retrieve the workflow definition summaries")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "200",
            description = "The workflow definition summaries were retrieved"),
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
      value =
          "/workflow-definition-categories/{workflowDefinitionCategoryId}/workflow-definition-summaries",
      method = RequestMethod.GET,
      produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.OK)
  @PreAuthorize(
      "isSecurityDisabled() or hasRole('Administrator') or hasAuthority('FUNCTION_Operations.OperationsAdministration') or hasAuthority('FUNCTION_Operations.WorkflowAdministration') or hasAuthority('FUNCTION_Operations.Indexing')")
  List<WorkflowDefinitionSummary> getWorkflowDefinitionSummaries(
      @Parameter(
              name = "Tenant-ID",
              description = "The ID for the tenant",
              example = "00000000-0000-0000-0000-000000000000")
          @RequestHeader(
              name = "Tenant-ID",
              defaultValue = "00000000-0000-0000-0000-000000000000",
              required = false)
          UUID tenantId,
      @Parameter(
              name = "workflowDefinitionCategoryId",
              description = "The ID for the workflow definition category",
              required = true)
          @PathVariable
          String workflowDefinitionCategoryId)
      throws InvalidArgumentException,
          WorkflowDefinitionCategoryNotFoundException,
          ServiceUnavailableException;

  /**
   * Retrieve the workflow definition version.
   *
   * @param tenantId the ID for the tenant
   * @param workflowDefinitionCategoryId the ID for the workflow definition category
   * @param workflowDefinitionId the ID for the workflow definition
   * @param workflowDefinitionVersion the version of the workflow definition
   * @return the workflow definition version
   * @throws InvalidArgumentException if an argument is invalid
   * @throws WorkflowDefinitionCategoryNotFoundException if the workflow definition category could
   *     not be found
   * @throws WorkflowDefinitionNotFoundException if the workflow definition could not be found
   * @throws WorkflowDefinitionVersionNotFoundException if the workflow definition version could not
   *     be found
   * @throws ServiceUnavailableException if the workflow definition version could not be retrieved
   */
  @Operation(
      summary = "Retrieve the workflow definition version",
      description = "Retrieve the workflow definition version")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "200",
            description = "The workflow definition version was retrieved"),
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
            description =
                "The workflow definition category or workflow definition version could not be found",
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
      value =
          "/workflow-definition-categories/{workflowDefinitionCategoryId}/workflow-definitions/{workflowDefinitionId}/{workflowDefinitionVersion}",
      method = RequestMethod.GET,
      produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.OK)
  @PreAuthorize(
      "isSecurityDisabled() or hasRole('Administrator') or hasAuthority('FUNCTION_Operations.OperationsAdministration')")
  WorkflowDefinition getWorkflowDefinitionVersion(
      @Parameter(
              name = "Tenant-ID",
              description = "The ID for the tenant",
              example = "00000000-0000-0000-0000-000000000000")
          @RequestHeader(
              name = "Tenant-ID",
              defaultValue = "00000000-0000-0000-0000-000000000000",
              required = false)
          UUID tenantId,
      @Parameter(
              name = "workflowDefinitionCategoryId",
              description = "The ID for the workflow definition category",
              required = true)
          @PathVariable
          String workflowDefinitionCategoryId,
      @Parameter(
              name = "workflowDefinitionId",
              description = "The ID for the workflow definition",
              required = true)
          @PathVariable
          String workflowDefinitionId,
      @Parameter(
              name = "workflowDefinitionVersion",
              description = "The version of the workflow definition",
              required = true)
          @PathVariable
          int workflowDefinitionVersion)
      throws InvalidArgumentException,
          WorkflowDefinitionCategoryNotFoundException,
          WorkflowDefinitionNotFoundException,
          WorkflowDefinitionVersionNotFoundException,
          ServiceUnavailableException;

  /**
   * Retrieve the workflow documents for the workflow.
   *
   * @param tenantId the ID for the tenant
   * @param workflowId the ID for the workflow the workflow documents are associated with
   * @param filter the filter to apply to the workflow documents
   * @param sortBy the method used to sort the workflow documents, e.g. by created
   * @param sortDirection the sort direction to apply to the workflow documents
   * @param pageIndex the page index
   * @param pageSize the page size
   * @return the workflow documents
   * @throws InvalidArgumentException if an argument is invalid
   * @throws WorkflowNotFoundException if the workflow could not be found
   * @throws ServiceUnavailableException if the workflow documents could not be retrieved
   */
  @Operation(
      summary = "Retrieve the workflow documents for the workflow",
      description = "Retrieve the workflow documents for the workflow")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "200",
            description = "The workflow documents for the workflow were retrieved"),
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
      value = "/workflows/{workflowId}/documents",
      method = RequestMethod.GET,
      produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.OK)
  @PreAuthorize(
      "isSecurityDisabled() or hasRole('Administrator') or hasAuthority('FUNCTION_Operations.OperationsAdministration') or hasAuthority('FUNCTION_Operations.WorkflowAdministration') or hasAuthority('FUNCTION_Operations.Indexing')")
  WorkflowDocuments getWorkflowDocuments(
      @Parameter(
              name = "Tenant-ID",
              description = "The ID for the tenant",
              example = "00000000-0000-0000-0000-000000000000")
          @RequestHeader(
              name = "Tenant-ID",
              defaultValue = "00000000-0000-0000-0000-000000000000",
              required = false)
          UUID tenantId,
      @Parameter(name = "workflowId", description = "The ID for the workflow", required = true)
          @PathVariable
          UUID workflowId,
      @Parameter(name = "filter", description = "The filter to apply to the workflow documents")
          @RequestParam(value = "filter", required = false)
          String filter,
      @Parameter(
              name = "sortBy",
              description = "The method used to sort the workflow documents e.g. by created")
          @RequestParam(value = "sortBy", required = false)
          WorkflowDocumentSortBy sortBy,
      @Parameter(
              name = "sortDirection",
              description = "The sort direction to apply to the workflow documents")
          @RequestParam(value = "sortDirection", required = false)
          SortDirection sortDirection,
      @Parameter(name = "pageIndex", description = "The page index", example = "0")
          @RequestParam(value = "pageIndex", required = false, defaultValue = "0")
          Integer pageIndex,
      @Parameter(name = "pageSize", description = "The page size", example = "10")
          @RequestParam(value = "pageSize", required = false, defaultValue = "10")
          Integer pageSize)
      throws InvalidArgumentException, WorkflowNotFoundException, ServiceUnavailableException;

  /**
   * Retrieve the workflow engine.
   *
   * @param workflowEngineId the ID for the workflow engine
   * @return the workflow engine
   * @throws InvalidArgumentException if an argument is invalid
   * @throws WorkflowEngineNotFoundException if the workflow engine could not be found
   * @throws ServiceUnavailableException if the workflow engine could not be retrieved
   */
  @Operation(summary = "Retrieve the workflow engine", description = "Retrieve the workflow engine")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "200", description = "The workflow engine was retrieved"),
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
            description = "The workflow engine could not be found",
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
      value = "/workflow-engines/{workflowEngineId}",
      method = RequestMethod.GET,
      produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.OK)
  @PreAuthorize(
      "isSecurityDisabled() or hasRole('Administrator') or hasAuthority('FUNCTION_Operations.OperationsAdministration')")
  WorkflowEngine getWorkflowEngine(
      @Parameter(
              name = "workflowEngineId",
              description = "The ID for the workflow engine",
              required = true)
          @PathVariable
          String workflowEngineId)
      throws InvalidArgumentException, WorkflowEngineNotFoundException, ServiceUnavailableException;

  /**
   * Retrieve the workflow engines.
   *
   * @return the workflow engines
   * @throws InvalidArgumentException if an argument is invalid
   * @throws ServiceUnavailableException if the workflow engines could not be retrieved
   */
  @Operation(
      summary = "Retrieve the workflow engines",
      description = "Retrieve the workflow engines")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "200", description = "The workflow engines were retrieved"),
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
      value = "/workflow-engines",
      method = RequestMethod.GET,
      produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.OK)
  @PreAuthorize(
      "isSecurityDisabled() or hasRole('Administrator') or hasAuthority('FUNCTION_Operations.OperationsAdministration')")
  List<WorkflowEngine> getWorkflowEngines()
      throws InvalidArgumentException, ServiceUnavailableException;

  /**
   * Retrieve the workflow note.
   *
   * @param tenantId the ID for the tenant
   * @param workflowId the ID for the workflow
   * @param workflowNoteId the ID for the workflow note
   * @return the workflow note
   * @throws InvalidArgumentException if an argument is invalid
   * @throws WorkflowNotFoundException if the workflow could not be found
   * @throws WorkflowNoteNotFoundException if the workflow note could not be found
   * @throws ServiceUnavailableException if the workflow note could not be retrieved
   */
  @Operation(summary = "Retrieve the workflow note", description = "Retrieve the workflow note")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "200", description = "The workflow note was retrieved"),
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
            description = "The workflow or workflow note could not be found",
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
      value = "/workflows/{workflowId}/notes/{workflowNoteId}",
      method = RequestMethod.GET,
      produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.OK)
  @PreAuthorize(
      "isSecurityDisabled() or hasRole('Administrator') or hasAuthority('FUNCTION_Operations.OperationsAdministration') or hasAuthority('FUNCTION_Operations.WorkflowAdministration') or hasAuthority('FUNCTION_Operations.Indexing')")
  WorkflowNote getWorkflowNote(
      @Parameter(
              name = "Tenant-ID",
              description = "The ID for the tenant",
              example = "00000000-0000-0000-0000-000000000000")
          @RequestHeader(
              name = "Tenant-ID",
              defaultValue = "00000000-0000-0000-0000-000000000000",
              required = false)
          UUID tenantId,
      @Parameter(name = "workflowId", description = "The ID for the workflow", required = true)
          @PathVariable
          UUID workflowId,
      @Parameter(
              name = "workflowNoteId",
              description = "The ID for the workflow note",
              required = true)
          @PathVariable
          UUID workflowNoteId)
      throws InvalidArgumentException,
          WorkflowNotFoundException,
          WorkflowNoteNotFoundException,
          ServiceUnavailableException;

  /**
   * Retrieve the workflow notes for the workflow.
   *
   * @param tenantId the ID for the tenant
   * @param workflowId the ID for the workflow the workflow notes are associated with
   * @param filter the filter to apply to the workflow notes
   * @param sortBy the method used to sort the workflow notes, e.g. by created
   * @param sortDirection the sort direction to apply to the workflow notes
   * @param pageIndex the page index
   * @param pageSize the page size
   * @return the workflow notes
   * @throws InvalidArgumentException if an argument is invalid
   * @throws WorkflowNotFoundException if the workflow could not be found
   * @throws ServiceUnavailableException if the workflow notes could not be retrieved
   */
  @Operation(
      summary = "Retrieve the workflow notes for the workflow",
      description = "Retrieve the workflow notes for the workflow")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "200",
            description = "The workflow notes for the workflow were retrieved"),
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
      value = "/workflows/{workflowId}/notes",
      method = RequestMethod.GET,
      produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.OK)
  @PreAuthorize(
      "isSecurityDisabled() or hasRole('Administrator') or hasAuthority('FUNCTION_Operations.OperationsAdministration') or hasAuthority('FUNCTION_Operations.WorkflowAdministration') or hasAuthority('FUNCTION_Operations.Indexing')")
  WorkflowNotes getWorkflowNotes(
      @Parameter(
              name = "Tenant-ID",
              description = "The ID for the tenant",
              example = "00000000-0000-0000-0000-000000000000")
          @RequestHeader(
              name = "Tenant-ID",
              defaultValue = "00000000-0000-0000-0000-000000000000",
              required = false)
          UUID tenantId,
      @Parameter(name = "workflowId", description = "The ID for the workflow", required = true)
          @PathVariable
          UUID workflowId,
      @Parameter(name = "filter", description = "The filter to apply to the workflow notes")
          @RequestParam(value = "filter", required = false)
          String filter,
      @Parameter(
              name = "sortBy",
              description = "The method used to sort the workflow notes e.g. by created")
          @RequestParam(value = "sortBy", required = false)
          WorkflowNoteSortBy sortBy,
      @Parameter(
              name = "sortDirection",
              description = "The sort direction to apply to the workflow notes")
          @RequestParam(value = "sortDirection", required = false)
          SortDirection sortDirection,
      @Parameter(name = "pageIndex", description = "The page index", example = "0")
          @RequestParam(value = "pageIndex", required = false, defaultValue = "0")
          Integer pageIndex,
      @Parameter(name = "pageSize", description = "The page size", example = "10")
          @RequestParam(value = "pageSize", required = false, defaultValue = "10")
          Integer pageSize)
      throws InvalidArgumentException, WorkflowNotFoundException, ServiceUnavailableException;

  /**
   * Retrieve the summaries for the workflows.
   *
   * @param tenantId the ID for the tenant
   * @param definitionId the workflow definition ID filter to apply to the workflow summaries
   * @param status the status filter to apply to the workflow summaries
   * @param filter the filter to apply to the workflow summaries
   * @param sortBy the method used to sort the workflow summaries e.g. by definition ID
   * @param sortDirection the sort direction to apply to the workflow summaries
   * @param pageIndex the page index
   * @param pageSize the page size
   * @return the summaries for the workflows
   * @throws InvalidArgumentException if an argument is invalid
   * @throws ServiceUnavailableException if the workflow summaries could not be retrieved
   */
  @Operation(
      summary = "Retrieve the workflow summaries",
      description = "Retrieve the workflow summaries")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "200", description = "The workflow summaries were retrieved"),
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
      value = "/workflow-summaries",
      method = RequestMethod.GET,
      produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.OK)
  @PreAuthorize(
      "isSecurityDisabled() or hasRole('Administrator') or hasAuthority('FUNCTION_Operations.OperationsAdministration') or hasAuthority('FUNCTION_Operations.WorkflowAdministration') or hasAuthority('FUNCTION_Operations.Indexing')")
  WorkflowSummaries getWorkflowSummaries(
      @Parameter(
              name = "Tenant-ID",
              description = "The ID for the tenant",
              example = "00000000-0000-0000-0000-000000000000")
          @RequestHeader(
              name = "Tenant-ID",
              defaultValue = "00000000-0000-0000-0000-000000000000",
              required = false)
          UUID tenantId,
      @Parameter(
              name = "definitionId",
              description = "The workflow definition ID filter to apply to the workflow summaries")
          @RequestParam(value = "definitionId", required = false)
          String definitionId,
      @Parameter(
              name = "status",
              description = "The status filter to apply to the workflow summaries")
          @RequestParam(value = "status", required = false)
          WorkflowStatus status,
      @Parameter(name = "filter", description = "The filter to apply to the workflow summaries")
          @RequestParam(value = "filter", required = false)
          String filter,
      @Parameter(
              name = "sortBy",
              description = "The method used to sort the workflow summaries e.g. by definition ID")
          @RequestParam(value = "sortBy", required = false)
          WorkflowSortBy sortBy,
      @Parameter(
              name = "sortDirection",
              description = "The sort direction to apply to the workflow summaries")
          @RequestParam(value = "sortDirection", required = false)
          SortDirection sortDirection,
      @Parameter(name = "pageIndex", description = "The page index", example = "0")
          @RequestParam(value = "pageIndex", required = false, defaultValue = "0")
          Integer pageIndex,
      @Parameter(name = "pageSize", description = "The page size", example = "10")
          @RequestParam(value = "pageSize", required = false, defaultValue = "10")
          Integer pageSize)
      throws InvalidArgumentException, ServiceUnavailableException;

  /**
   * Initiate a workflow.
   *
   * @param tenantId the ID for the tenant
   * @param initiateWorkflowRequest the request to initiate the workflow
   * @return the ID for the workflow
   * @throws InvalidArgumentException if an argument is invalid
   * @throws WorkflowDefinitionNotFoundException if the workflow definition could not be found
   * @throws ServiceUnavailableException if the workflow could not be initiated
   */
  @Operation(summary = "Initiate a workflow", description = "Initiate a workflow")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "200", description = "The workflow was initiated"),
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
            description = "The workflow definition could not be found",
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
      value = "/initiate-workflow",
      method = RequestMethod.POST,
      produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.OK)
  @PreAuthorize(
      "isSecurityDisabled() or hasRole('Administrator') or hasAuthority('FUNCTION_Operations.OperationsAdministration') or hasAuthority('FUNCTION_Operations.WorkflowAdministration') or hasAuthority('FUNCTION_Operations.Indexing')")
  UUID initiateWorkflow(
      @Parameter(
              name = "Tenant-ID",
              description = "The ID for the tenant",
              example = "00000000-0000-0000-0000-000000000000")
          @RequestHeader(
              name = "Tenant-ID",
              defaultValue = "00000000-0000-0000-0000-000000000000",
              required = false)
          UUID tenantId,
      @io.swagger.v3.oas.annotations.parameters.RequestBody(
              description = "The request to initiate the workflow",
              required = true)
          @RequestBody
          InitiateWorkflowRequest initiateWorkflowRequest)
      throws InvalidArgumentException,
          WorkflowDefinitionNotFoundException,
          ServiceUnavailableException;

  /**
   * Initiate a workflow step.
   *
   * @param tenantId the ID for the tenant
   * @param initiateWorkflowStepRequest the request to initiate a workflow step
   * @throws InvalidArgumentException if an argument is invalid
   * @throws WorkflowNotFoundException if the workflow could not be found
   * @throws ServiceUnavailableException if the workflow step could not be initiated
   */
  @Operation(summary = "Initiate a workflow step", description = "Initiate a workflow step")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "204", description = "The workflow step was initiated"),
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
            description = "The workflow could not be found",
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
      value = "/initiate-workflow-step",
      method = RequestMethod.POST,
      produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.NO_CONTENT)
  @PreAuthorize(
      "isSecurityDisabled() or hasRole('Administrator') or hasAuthority('FUNCTION_Operations.OperationsAdministration') or hasAuthority('FUNCTION_Operations.WorkflowAdministration')")
  void initiateWorkflowStep(
      @Parameter(
              name = "Tenant-ID",
              description = "The ID for the tenant",
              example = "00000000-0000-0000-0000-000000000000")
          @RequestHeader(
              name = "Tenant-ID",
              defaultValue = "00000000-0000-0000-0000-000000000000",
              required = false)
          UUID tenantId,
      @io.swagger.v3.oas.annotations.parameters.RequestBody(
              description = "The request to initiate the workflow step",
              required = true)
          @RequestBody
          InitiateWorkflowStepRequest initiateWorkflowStepRequest)
      throws InvalidArgumentException, WorkflowNotFoundException, ServiceUnavailableException;

  /**
   * Update the workflow.
   *
   * @param tenantId the ID for the tenant
   * @param updateWorkflowRequest the request to update the workflow
   * @throws InvalidArgumentException if an argument is invalid
   * @throws WorkflowNotFoundException if the workflow could not be found
   * @throws ServiceUnavailableException if the workflow could not be updated
   */
  @Operation(summary = "Update the workflow", description = "Update the workflow")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "204", description = "The workflow was updated"),
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
            description = "The workflow could not be found",
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
      value = "/update-workflow",
      method = RequestMethod.POST,
      produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.NO_CONTENT)
  @PreAuthorize(
      "isSecurityDisabled() or hasRole('Administrator') or hasAuthority('FUNCTION_Operations.OperationsAdministration') or hasAuthority('FUNCTION_Operations.WorkflowAdministration') or hasAuthority('FUNCTION_Operations.Indexing')")
  void updateWorkflow(
      @Parameter(
              name = "Tenant-ID",
              description = "The ID for the tenant",
              example = "00000000-0000-0000-0000-000000000000")
          @RequestHeader(
              name = "Tenant-ID",
              defaultValue = "00000000-0000-0000-0000-000000000000",
              required = false)
          UUID tenantId,
      @io.swagger.v3.oas.annotations.parameters.RequestBody(
              description = "The request to update the workflow",
              required = true)
          @RequestBody
          UpdateWorkflowRequest updateWorkflowRequest)
      throws InvalidArgumentException, WorkflowNotFoundException, ServiceUnavailableException;

  /**
   * Update the workflow definition version.
   *
   * @param workflowDefinitionCategoryId the ID for the workflow definition category
   * @param workflowDefinitionId the ID for the workflow definition
   * @param workflowDefinitionVersion the version of the workflow definition
   * @param workflowDefinition the workflow definition version
   * @throws InvalidArgumentException if an argument is invalid
   * @throws WorkflowDefinitionCategoryNotFoundException if the workflow definition category could
   *     not be found
   * @throws WorkflowDefinitionVersionNotFoundException if the workflow definition version could not
   *     be found
   * @throws ServiceUnavailableException if the workflow definition version could not be updated
   */
  @Operation(
      summary = "Update the workflow definition version",
      description = "Update the workflow definition version")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "204",
            description = "The workflow definition version was updated"),
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
            description =
                "The workflow definition category or workflow definition version could not be found",
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
      value =
          "/workflow-definition-categories/{workflowDefinitionCategoryId}/workflow-definitions/{workflowDefinitionId}/{workflowDefinitionVersion}",
      method = RequestMethod.PUT,
      produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.NO_CONTENT)
  @PreAuthorize(
      "isSecurityDisabled() or hasRole('Administrator') or hasAuthority('FUNCTION_Operations.OperationsAdministration')")
  void updateWorkflowDefinition(
      @Parameter(
              name = "workflowDefinitionCategoryId",
              description = "The ID for the workflow definition category",
              required = true)
          @PathVariable
          String workflowDefinitionCategoryId,
      @Parameter(
              name = "workflowDefinitionId",
              description = "The ID for the workflow definition",
              required = true)
          @PathVariable
          String workflowDefinitionId,
      @Parameter(
              name = "workflowDefinitionVersion",
              description = "The version of the workflow definition",
              required = true)
          @PathVariable
          Integer workflowDefinitionVersion,
      @io.swagger.v3.oas.annotations.parameters.RequestBody(
              description = "The workflow definition",
              required = true)
          @RequestBody
          WorkflowDefinition workflowDefinition)
      throws InvalidArgumentException,
          WorkflowDefinitionCategoryNotFoundException,
          WorkflowDefinitionVersionNotFoundException,
          ServiceUnavailableException;

  /**
   * Update the workflow definition category.
   *
   * @param workflowDefinitionCategoryId the ID for the workflow definition category
   * @param workflowDefinitionCategory the workflow definition category
   * @throws InvalidArgumentException if an argument is invalid
   * @throws WorkflowDefinitionCategoryNotFoundException if the workflow definition category could
   *     not be found
   * @throws ServiceUnavailableException if the workflow definition category could not be updated
   */
  @Operation(
      summary = "Update the workflow definition category",
      description = "Update the workflow definition category")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "204",
            description = "The workflow definition category was updated"),
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
            description = "The workflow definition category could not be found",
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
      value = "/workflow-definition-categories/{workflowDefinitionCategoryId}",
      method = RequestMethod.PUT,
      produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.NO_CONTENT)
  @PreAuthorize(
      "isSecurityDisabled() or hasRole('Administrator') or hasAuthority('FUNCTION_Operations.OperationsAdministration')")
  void updateWorkflowDefinitionCategory(
      @Parameter(
              name = "workflowDefinitionCategoryId",
              description = "The ID for the workflow definition category",
              required = true)
          @PathVariable
          String workflowDefinitionCategoryId,
      @io.swagger.v3.oas.annotations.parameters.RequestBody(
              description = "The workflow definition category",
              required = true)
          @RequestBody
          WorkflowDefinitionCategory workflowDefinitionCategory)
      throws InvalidArgumentException,
          WorkflowDefinitionCategoryNotFoundException,
          ServiceUnavailableException;

  /**
   * Update the workflow engine.
   *
   * @param workflowEngineId the ID for the workflow engine
   * @param workflowEngine the workflow engine
   * @throws InvalidArgumentException if an argument is invalid
   * @throws WorkflowEngineNotFoundException if the workflow engine could not be found
   * @throws ServiceUnavailableException if the workflow engine could not be updated
   */
  @Operation(summary = "Update the workflow engine", description = "Update the workflow engine")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "204", description = "The workflow engine was updated"),
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
            description = "The workflow engine could not be found",
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
      value = "/workflow-engines/{workflowEngineId}",
      method = RequestMethod.PUT,
      produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.NO_CONTENT)
  @PreAuthorize(
      "isSecurityDisabled() or hasRole('Administrator') or hasAuthority('FUNCTION_Operations.OperationsAdministration')")
  void updateWorkflowEngine(
      @Parameter(
              name = "workflowEngineId",
              description = "The ID for the workflow engine",
              required = true)
          @PathVariable
          String workflowEngineId,
      @io.swagger.v3.oas.annotations.parameters.RequestBody(
              description = "The workflow engine",
              required = true)
          @RequestBody
          WorkflowEngine workflowEngine)
      throws InvalidArgumentException, WorkflowEngineNotFoundException, ServiceUnavailableException;

  /**
   * Update the workflow note.
   *
   * @param tenantId the ID for the tenant
   * @param updateWorkflowNoteRequest the request to update the workflow note
   * @throws InvalidArgumentException if an argument is invalid
   * @throws WorkflowNoteNotFoundException if the workflow note could not be found
   * @throws ServiceUnavailableException if the workflow note could not be updated
   */
  @Operation(summary = "Update the workflow note", description = "Update the workflow note")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "204", description = "The workflow note was updated"),
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
            description = "The workflow note could not be found",
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
      value = "/update-workflow-note",
      method = RequestMethod.POST,
      produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.NO_CONTENT)
  @PreAuthorize(
      "isSecurityDisabled() or hasRole('Administrator') or hasAuthority('FUNCTION_Operations.OperationsAdministration') or hasAuthority('FUNCTION_Operations.WorkflowAdministration') or hasAuthority('FUNCTION_Operations.Indexing')")
  void updateWorkflowNote(
      @Parameter(
              name = "Tenant-ID",
              description = "The ID for the tenant",
              example = "00000000-0000-0000-0000-000000000000")
          @RequestHeader(
              name = "Tenant-ID",
              defaultValue = "00000000-0000-0000-0000-000000000000",
              required = false)
          UUID tenantId,
      @io.swagger.v3.oas.annotations.parameters.RequestBody(
              description = "The request to update the workflow note",
              required = true)
          @RequestBody
          UpdateWorkflowNoteRequest updateWorkflowNoteRequest)
      throws InvalidArgumentException, WorkflowNoteNotFoundException, ServiceUnavailableException;
}
