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
import digital.inception.core.service.InvalidArgumentException;
import digital.inception.core.service.ServiceUnavailableException;
import digital.inception.operations.model.CreateDocumentRequest;
import digital.inception.operations.model.CreateWorkflowRequest;
import digital.inception.operations.exception.DocumentDefinitionNotFoundException;
import digital.inception.operations.exception.DuplicateWorkflowDefinitionCategoryException;
import digital.inception.operations.model.WorkflowDefinitionCategory;
import digital.inception.operations.exception.WorkflowDefinitionNotFoundException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * The {@code OperationsApiController} interface.
 *
 * @author Marcus Portmann
 */
@Tag(name = "Operations")
@RequestMapping(value = "/api/operations")
// @el (isSecurityDisabled:
// digital.inception.api.SecureApiSecurityExpressionRoot.isSecurityEnabled)
public interface OperationsApiController {

  /**
   * Create the document.
   *
   * @param tenantId the ID for the tenant
   * @param createDocumentRequest the request to create the document
   * @return the ID for the document
   * @throws InvalidArgumentException if an argument is invalid
   * @throws DocumentDefinitionNotFoundException if the document definition could not be found
   * @throws ServiceUnavailableException if the document could not be created
   */
  @Operation(summary = "Create the document", description = "Create the document")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "200", description = "The document was created"),
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
            description = "The document definition could not be found",
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
      value = "/create-document",
      method = RequestMethod.POST,
      produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.OK)
  @PreAuthorize(
      "isSecurityDisabled() or hasRole('Administrator') or hasAuthority('FUNCTION_Operations.OperationsAdministration')")
  UUID createDocument(
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
              description = "The request to create the document",
              required = true)
          @RequestBody
          CreateDocumentRequest createDocumentRequest)
      throws InvalidArgumentException,
          DocumentDefinitionNotFoundException,
          ServiceUnavailableException;

  /**
   * Create the workflow.
   *
   * @param tenantId the ID for the tenant
   * @param createWorkflowRequest the request to create the workflow
   * @return the ID for the workflow
   * @throws InvalidArgumentException if an argument is invalid
   * @throws WorkflowDefinitionNotFoundException if the workflow definition could not be found
   * @throws ServiceUnavailableException if the workflow could not be created
   */
  @Operation(summary = "Create the workflow", description = "Create the workflow")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "200", description = "The workflow was created"),
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
      value = "/create-workflow",
      method = RequestMethod.POST,
      produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.OK)
  @PreAuthorize(
      "isSecurityDisabled() or hasRole('Administrator') or hasAuthority('FUNCTION_Operations.OperationsAdministration')")
  UUID createWorkflow(
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
              description = "The request to create the workflow",
              required = true)
          @RequestBody
          CreateWorkflowRequest createWorkflowRequest)
      throws InvalidArgumentException,
          WorkflowDefinitionNotFoundException,
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
}
