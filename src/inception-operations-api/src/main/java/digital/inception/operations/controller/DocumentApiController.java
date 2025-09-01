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
import digital.inception.operations.exception.DocumentAttributeDefinitionNotFoundException;
import digital.inception.operations.exception.DocumentDefinitionCategoryNotFoundException;
import digital.inception.operations.exception.DocumentDefinitionNotFoundException;
import digital.inception.operations.exception.DocumentNotFoundException;
import digital.inception.operations.exception.DocumentNoteNotFoundException;
import digital.inception.operations.exception.DuplicateDocumentAttributeDefinitionException;
import digital.inception.operations.exception.DuplicateDocumentDefinitionCategoryException;
import digital.inception.operations.exception.DuplicateDocumentDefinitionException;
import digital.inception.operations.model.CreateDocumentNoteRequest;
import digital.inception.operations.model.CreateDocumentRequest;
import digital.inception.operations.model.Document;
import digital.inception.operations.model.DocumentAttributeDefinition;
import digital.inception.operations.model.DocumentDefinition;
import digital.inception.operations.model.DocumentDefinitionCategory;
import digital.inception.operations.model.DocumentDefinitionSummary;
import digital.inception.operations.model.DocumentNote;
import digital.inception.operations.model.DocumentNoteSortBy;
import digital.inception.operations.model.DocumentNotes;
import digital.inception.operations.model.DocumentSortBy;
import digital.inception.operations.model.DocumentSummaries;
import digital.inception.operations.model.SearchDocumentsRequest;
import digital.inception.operations.model.UpdateDocumentNoteRequest;
import digital.inception.operations.model.UpdateDocumentRequest;
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
 * The {@code DocumentApiController} interface.
 *
 * @author Marcus Portmann
 */
@Tag(name = "Document")
@RequestMapping(value = "/api/operations")
// @el (isSecurityDisabled: PolicyDecisionPointSecurityExpressionRoot.isSecurityDisabled)
public interface DocumentApiController {

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
      "isSecurityDisabled() or hasRole('Administrator') or hasAuthority('FUNCTION_Operations.OperationsAdministration') or hasAuthority('FUNCTION_Operations.DocumentAdministration') or hasAuthority('FUNCTION_Operations.Indexing')")
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
   * Create the document attribute definition.
   *
   * @param documentAttributeDefinition the document attribute definition
   * @throws InvalidArgumentException if an argument is invalid
   * @throws DuplicateDocumentAttributeDefinitionException if the document attribute definition
   *     already exists
   * @throws ServiceUnavailableException if the document attribute definition could not be created
   */
  @Operation(
      summary = "Create the document attribute definition",
      description = "Create the document attribute definition")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "204",
            description = "The document attribute definition was created"),
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
            description = "A document attribute definition with the specified code already exists",
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
      value = "/document-attribute-definitions",
      method = RequestMethod.POST,
      produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.NO_CONTENT)
  @PreAuthorize(
      "isSecurityDisabled() or hasRole('Administrator') or hasAuthority('FUNCTION_Operations.OperationsAdministration')")
  void createDocumentAttributeDefinition(
      @io.swagger.v3.oas.annotations.parameters.RequestBody(
              description = "The document attribute definition to create",
              required = true)
          @RequestBody
          DocumentAttributeDefinition documentAttributeDefinition)
      throws InvalidArgumentException,
          DuplicateDocumentAttributeDefinitionException,
          ServiceUnavailableException;

  /**
   * Create the document definition.
   *
   * @param documentDefinitionCategoryId the ID for the document definition category
   * @param documentDefinition the document definition
   * @throws InvalidArgumentException if an argument is invalid
   * @throws DuplicateDocumentDefinitionException if the document definition already exists
   * @throws DocumentDefinitionCategoryNotFoundException if the document definition category could
   *     not be found
   * @throws ServiceUnavailableException if the document definition could not be created
   */
  @Operation(
      summary = "Create the document definition",
      description = "Create the document definition")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "204", description = "The document definition was created"),
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
            description = "The document definition category could not be found",
            content =
                @Content(
                    mediaType = "application/problem+json",
                    schema = @Schema(implementation = ProblemDetails.class))),
        @ApiResponse(
            responseCode = "409",
            description = "A document definition with the specified ID already exists",
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
      value = "/document-definition-categories/{documentDefinitionCategoryId}/document-definitions",
      method = RequestMethod.POST,
      produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.NO_CONTENT)
  @PreAuthorize(
      "isSecurityDisabled() or hasRole('Administrator') or hasAuthority('FUNCTION_Operations.OperationsAdministration')")
  void createDocumentDefinition(
      @Parameter(
              name = "documentDefinitionCategoryId",
              description = "The ID for the document definition category",
              required = true)
          @PathVariable
          String documentDefinitionCategoryId,
      @io.swagger.v3.oas.annotations.parameters.RequestBody(
              description = "The document definition version to create",
              required = true)
          @RequestBody
          DocumentDefinition documentDefinition)
      throws InvalidArgumentException,
          DuplicateDocumentDefinitionException,
          DocumentDefinitionCategoryNotFoundException,
          ServiceUnavailableException;

  /**
   * Create the document definition category.
   *
   * @param documentDefinitionCategory the document definition category
   * @throws InvalidArgumentException if an argument is invalid
   * @throws DuplicateDocumentDefinitionCategoryException if the document definition category
   *     already exists
   * @throws ServiceUnavailableException if the document definition category could not be created
   */
  @Operation(
      summary = "Create the document definition category",
      description = "Create the document definition category")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "204",
            description = "The document definition category was created"),
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
            description = "A document definition category with the specified ID already exists",
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
      value = "/document-definition-categories",
      method = RequestMethod.POST,
      produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.NO_CONTENT)
  @PreAuthorize(
      "isSecurityDisabled() or hasRole('Administrator') or hasAuthority('FUNCTION_Operations.OperationsAdministration')")
  void createDocumentDefinitionCategory(
      @io.swagger.v3.oas.annotations.parameters.RequestBody(
              description = "The document definition category to create",
              required = true)
          @RequestBody
          DocumentDefinitionCategory documentDefinitionCategory)
      throws InvalidArgumentException,
          DuplicateDocumentDefinitionCategoryException,
          ServiceUnavailableException;

  /**
   * Create the document note.
   *
   * @param tenantId the ID for the tenant
   * @param createDocumentNoteRequest the request to create the document note
   * @return the ID for the document note
   * @throws InvalidArgumentException if an argument is invalid
   * @throws DocumentNotFoundException if the document could not be found
   * @throws ServiceUnavailableException if the document note could not be created
   */
  @Operation(summary = "Create the document note", description = "Create the document note")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "200", description = "The document note was created"),
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
            description = "The document could not be found",
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
      value = "/create-document-note",
      method = RequestMethod.POST,
      produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.OK)
  @PreAuthorize(
      "isSecurityDisabled() or hasRole('Administrator') or hasAuthority('FUNCTION_Operations.OperationsAdministration') or hasAuthority('FUNCTION_Operations.DocumentAdministration') or hasAuthority('FUNCTION_Operations.DocumentNoteAdministration') or hasAuthority('FUNCTION_Operations.Indexing')")
  UUID createDocumentNote(
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
              description = "The request to create the document note",
              required = true)
          @RequestBody
          CreateDocumentNoteRequest createDocumentNoteRequest)
      throws InvalidArgumentException, DocumentNotFoundException, ServiceUnavailableException;

  /**
   * Delete the document.
   *
   * @param tenantId the ID for the tenant
   * @param documentId the ID for the document
   * @throws InvalidArgumentException if an argument is invalid
   * @throws DocumentNotFoundException if the document could not be found
   * @throws ServiceUnavailableException if the document could not be deleted
   */
  @Operation(summary = "Delete the document", description = "Delete the document")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "204", description = "The document was deleted"),
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
            description = "The document could not be found",
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
      value = "/documents/{documentId}",
      method = RequestMethod.DELETE,
      produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.NO_CONTENT)
  @PreAuthorize(
      "isSecurityDisabled() or hasRole('Administrator') or hasAuthority('FUNCTION_Operations.OperationsAdministration') or hasAuthority('FUNCTION_Operations.DocumentAdministration')")
  void deleteDocument(
      @Parameter(
              name = "Tenant-ID",
              description = "The ID for the tenant",
              example = "00000000-0000-0000-0000-000000000000")
          @RequestHeader(
              name = "Tenant-ID",
              defaultValue = "00000000-0000-0000-0000-000000000000",
              required = false)
          UUID tenantId,
      @Parameter(name = "documentId", description = "The ID for the document", required = true)
          @PathVariable
          UUID documentId)
      throws InvalidArgumentException, DocumentNotFoundException, ServiceUnavailableException;

  /**
   * Delete the document attribute definition.
   *
   * @param documentAttributeDefinitionCode the code for the document attribute definition
   * @throws InvalidArgumentException if an argument is invalid
   * @throws DocumentAttributeDefinitionNotFoundException if the document attribute definition could
   *     not be found
   * @throws ServiceUnavailableException if the document attribute definition could not be deleted
   */
  @Operation(
      summary = "Delete the document attribute definition",
      description = "Delete the document attribute definition")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "204",
            description = "The document attribute definition was deleted"),
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
            description = "The document attribute definition could not be found",
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
      value = "/document-attribute-definitions/{documentAttributeDefinitionCode}",
      method = RequestMethod.DELETE,
      produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.NO_CONTENT)
  @PreAuthorize(
      "isSecurityDisabled() or hasRole('Administrator') or hasAuthority('FUNCTION_Operations.OperationsAdministration')")
  void deleteDocumentAttributeDefinition(
      @Parameter(
              name = "documentAttributeDefinitionCode",
              description = "The code for the document attribute definition",
              required = true)
          @PathVariable
          String documentAttributeDefinitionCode)
      throws InvalidArgumentException,
          DocumentAttributeDefinitionNotFoundException,
          ServiceUnavailableException;

  /**
   * Delete the document definition.
   *
   * @param documentDefinitionCategoryId the ID for the document definition category
   * @param documentDefinitionId the ID for the document definition
   * @throws InvalidArgumentException if an argument is invalid
   * @throws DocumentDefinitionCategoryNotFoundException if the document definition category could
   *     not be found
   * @throws DocumentDefinitionNotFoundException if the document definition could not be found
   * @throws ServiceUnavailableException if the document definition could not be deleted
   */
  @Operation(
      summary = "Delete the document definition",
      description = "Delete the document definition")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "204", description = "The document definition was deleted"),
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
                "The document definition category or document definition could not be found",
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
          "/document-definition-categories/{documentDefinitionCategoryId}/document-definitions/{documentDefinitionId}",
      method = RequestMethod.DELETE,
      produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.NO_CONTENT)
  @PreAuthorize(
      "isSecurityDisabled() or hasRole('Administrator') or hasAuthority('FUNCTION_Operations.OperationsAdministration')")
  void deleteDocumentDefinition(
      @Parameter(
              name = "documentDefinitionCategoryId",
              description = "The ID for the document definition category",
              required = true)
          @PathVariable
          String documentDefinitionCategoryId,
      @Parameter(
              name = "documentDefinitionId",
              description = "The ID for the document definition",
              required = true)
          @PathVariable
          String documentDefinitionId)
      throws InvalidArgumentException,
          DocumentDefinitionCategoryNotFoundException,
          DocumentDefinitionNotFoundException,
          ServiceUnavailableException;

  /**
   * Delete the document definition category.
   *
   * @param documentDefinitionCategoryId the ID for the document definition category
   * @throws InvalidArgumentException if an argument is invalid
   * @throws DocumentDefinitionCategoryNotFoundException if the document definition category could
   *     not be found
   * @throws ServiceUnavailableException if the document definition category could not be deleted
   */
  @Operation(
      summary = "Delete the document definition category",
      description = "Delete the document definition category")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "204",
            description = "The document definition category was deleted"),
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
            description = "The document definition category could not be found",
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
      value = "/document-definition-categories/{documentDefinitionCategoryId}",
      method = RequestMethod.DELETE,
      produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.NO_CONTENT)
  @PreAuthorize(
      "isSecurityDisabled() or hasRole('Administrator') or hasAuthority('FUNCTION_Operations.OperationsAdministration')")
  void deleteDocumentDefinitionCategory(
      @Parameter(
              name = "documentDefinitionCategoryId",
              description = "The ID for the document definition category",
              required = true)
          @PathVariable
          String documentDefinitionCategoryId)
      throws InvalidArgumentException,
          DocumentDefinitionCategoryNotFoundException,
          ServiceUnavailableException;

  /**
   * Delete the document note.
   *
   * @param tenantId the ID for the tenant
   * @param documentId the ID for the document
   * @param documentNoteId the ID for the document note
   * @throws InvalidArgumentException if an argument is invalid
   * @throws DocumentNotFoundException if the document could not be found
   * @throws DocumentNoteNotFoundException if the document note could not be found
   * @throws ServiceUnavailableException if the document note could not be deleted
   */
  @Operation(summary = "Delete the document note", description = "Delete the document note")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "204", description = "The document note was deleted"),
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
            description = "The document or document note could not be found",
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
      value = "/documents/{documentId}/notes/{documentNoteId}",
      method = RequestMethod.DELETE,
      produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.NO_CONTENT)
  @PreAuthorize(
      "isSecurityDisabled() or hasRole('Administrator') or hasAuthority('FUNCTION_Operations.OperationsAdministration') or hasAuthority('FUNCTION_Operations.DocumentAdministration') or hasAuthority('FUNCTION_Operations.DocumentNoteAdministration') or hasAuthority('FUNCTION_Operations.Indexing')")
  void deleteDocumentNote(
      @Parameter(
              name = "Tenant-ID",
              description = "The ID for the tenant",
              example = "00000000-0000-0000-0000-000000000000")
          @RequestHeader(
              name = "Tenant-ID",
              defaultValue = "00000000-0000-0000-0000-000000000000",
              required = false)
          UUID tenantId,
      @Parameter(name = "documentId", description = "The ID for the document", required = true)
          @PathVariable
          UUID documentId,
      @Parameter(
              name = "documentNoteId",
              description = "The ID for the document note",
              required = true)
          @PathVariable
          UUID documentNoteId)
      throws InvalidArgumentException,
          DocumentNotFoundException,
          DocumentNoteNotFoundException,
          ServiceUnavailableException;

  /**
   * Retrieve the document.
   *
   * @param tenantId the ID for the tenant
   * @param documentId the ID for the document
   * @return the document
   * @throws InvalidArgumentException if an argument is invalid
   * @throws DocumentNotFoundException if the document could not be found
   * @throws ServiceUnavailableException if the document could not be retrieved
   */
  @Operation(summary = "Retrieve the document", description = "Retrieve the document")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "200", description = "The document was retrieved"),
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
            description = "The document could not be found",
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
      value = "/documents/{documentId}",
      method = RequestMethod.GET,
      produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.OK)
  @PreAuthorize(
      "isSecurityDisabled() or hasRole('Administrator') or hasAuthority('FUNCTION_Operations.OperationsAdministration') or hasAuthority('FUNCTION_Operations.DocumentAdministration') or hasAuthority('FUNCTION_Operations.Indexing')")
  Document getDocument(
      @Parameter(
              name = "Tenant-ID",
              description = "The ID for the tenant",
              example = "00000000-0000-0000-0000-000000000000")
          @RequestHeader(
              name = "Tenant-ID",
              defaultValue = "00000000-0000-0000-0000-000000000000",
              required = false)
          UUID tenantId,
      @Parameter(name = "documentId", description = "The ID for the document", required = true)
          @PathVariable
          UUID documentId)
      throws InvalidArgumentException, DocumentNotFoundException, ServiceUnavailableException;

  /**
   * Retrieve the document attribute definition.
   *
   * @param documentAttributeDefinitionCode the code for the document attribute definition
   * @return the document attribute definition
   * @throws InvalidArgumentException if an argument is invalid
   * @throws DocumentAttributeDefinitionNotFoundException if the document attribute definition could
   *     not be found
   * @throws ServiceUnavailableException if the document attribute definition could not be retrieved
   */
  @Operation(
      summary = "Retrieve the document attribute definition",
      description = "Retrieve the document attribute definition")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "200",
            description = "The document attribute definition was retrieved"),
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
            description = "The document attribute definition could not be found",
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
      value = "/document-attribute-definitions/{documentAttributeDefinitionCode}",
      method = RequestMethod.GET,
      produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.OK)
  @PreAuthorize(
      "isSecurityDisabled() or hasRole('Administrator') or hasAuthority('FUNCTION_Operations.OperationsAdministration')")
  DocumentAttributeDefinition getDocumentAttributeDefinition(
      @Parameter(
              name = "documentAttributeDefinitionCode",
              description = "The code for the document attribute definition",
              required = true)
          @PathVariable
          String documentAttributeDefinitionCode)
      throws InvalidArgumentException,
          DocumentAttributeDefinitionNotFoundException,
          ServiceUnavailableException;

  /**
   * Retrieve the document attribute definitions.
   *
   * @param tenantId the ID for the tenant
   * @return the document attribute definitions
   * @throws InvalidArgumentException if an argument is invalid
   * @throws ServiceUnavailableException if the document attribute definitions could not be
   *     retrieved
   */
  @Operation(
      summary = "Retrieve the document attribute definitions",
      description = "Retrieve the document attribute definitions")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "200",
            description = "The document attribute definitions were retrieved"),
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
      value = "/document-attribute-definitions",
      method = RequestMethod.GET,
      produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.OK)
  @PreAuthorize(
      "isSecurityDisabled() or hasRole('Administrator') or hasAuthority('FUNCTION_Operations.OperationsAdministration') or hasAuthority('FUNCTION_Operations.DocumentAdministration') or hasAuthority('FUNCTION_Operations.Indexing')")
  List<DocumentAttributeDefinition> getDocumentAttributeDefinitions(
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
   * Retrieve the document definition.
   *
   * @param documentDefinitionCategoryId the ID for the document definition category
   * @param documentDefinitionId the ID for the document definition
   * @return the document definition
   * @throws InvalidArgumentException if an argument is invalid
   * @throws DocumentDefinitionCategoryNotFoundException if the document definition category could
   *     not be found
   * @throws DocumentDefinitionNotFoundException if the document definition could not be found
   * @throws ServiceUnavailableException if the document definition could not be retrieved
   */
  @Operation(
      summary = "Retrieve the document definition",
      description = "Retrieve the document definition")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "200", description = "The document definition was retrieved"),
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
                "The document definition category or document definition could not be found",
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
          "/document-definition-categories/{documentDefinitionCategoryId}/document-definitions/{documentDefinitionId}",
      method = RequestMethod.GET,
      produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.OK)
  @PreAuthorize(
      "isSecurityDisabled() or hasRole('Administrator') or hasAuthority('FUNCTION_Operations.OperationsAdministration') or hasAuthority('FUNCTION_Operations.DocumentAdministration') or hasAuthority('FUNCTION_Operations.Indexing')")
  DocumentDefinition getDocumentDefinition(
      @Parameter(
              name = "documentDefinitionCategoryId",
              description = "The ID for the document definition category",
              required = true)
          @PathVariable
          String documentDefinitionCategoryId,
      @Parameter(
              name = "documentDefinitionId",
              description = "The ID for the document definition",
              required = true)
          @PathVariable
          String documentDefinitionId)
      throws InvalidArgumentException,
          DocumentDefinitionCategoryNotFoundException,
          DocumentDefinitionNotFoundException,
          ServiceUnavailableException;

  /**
   * Retrieve the document definition categories.
   *
   * @param tenantId the ID for the tenant
   * @return the document definition categories
   * @throws InvalidArgumentException if an argument is invalid
   * @throws ServiceUnavailableException if the document definition categories could not be
   *     retrieved
   */
  @Operation(
      summary = "Retrieve the document definition categories",
      description = "Retrieve the document definition categories")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "200",
            description = "The document definition categories were retrieved"),
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
      value = "/document-definition-categories",
      method = RequestMethod.GET,
      produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.OK)
  @PreAuthorize(
      "isSecurityDisabled() or hasRole('Administrator') or hasAuthority('FUNCTION_Operations.OperationsAdministration') or hasAuthority('FUNCTION_Operations.DocumentAdministration') or hasAuthority('FUNCTION_Operations.Indexing')")
  List<DocumentDefinitionCategory> getDocumentDefinitionCategories(
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
   * Retrieve the document definition category.
   *
   * @param documentDefinitionCategoryId the ID for the document definition category
   * @return the document definition category
   * @throws InvalidArgumentException if an argument is invalid
   * @throws DocumentDefinitionCategoryNotFoundException if the document definition category could
   *     not be found
   * @throws ServiceUnavailableException if the document definition category could not be retrieved
   */
  @Operation(
      summary = "Retrieve the document definition category",
      description = "Retrieve the document definition category")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "200",
            description = "The document definition category was retrieved"),
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
            description = "The document definition category could not be found",
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
      value = "/document-definition-categories/{documentDefinitionCategoryId}",
      method = RequestMethod.GET,
      produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.OK)
  @PreAuthorize(
      "isSecurityDisabled() or hasRole('Administrator') or hasAuthority('FUNCTION_Operations.OperationsAdministration') or hasAuthority('FUNCTION_Operations.DocumentAdministration') or hasAuthority('FUNCTION_Operations.Indexing')")
  DocumentDefinitionCategory getDocumentDefinitionCategory(
      @Parameter(
              name = "documentDefinitionCategoryId",
              description = "The ID for the document definition category",
              required = true)
          @PathVariable
          String documentDefinitionCategoryId)
      throws InvalidArgumentException,
          DocumentDefinitionCategoryNotFoundException,
          ServiceUnavailableException;

  /**
   * Retrieve the summaries for the document definitions associated with the document definition
   * category with the specified ID.
   *
   * @param tenantId the ID for the tenant
   * @param documentDefinitionCategoryId the ID for the document definition category the document
   *     definitions are associated with
   * @return the summaries for the document definitions associated with the document definition
   *     category with the specified ID
   * @throws InvalidArgumentException if an argument is invalid
   * @throws DocumentDefinitionCategoryNotFoundException if the document definition category could
   *     not be found
   * @throws ServiceUnavailableException if the document definition summaries could not be retrieved
   */
  @Operation(
      summary = "Retrieve the document definition summaries",
      description = "Retrieve the document definition summaries")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "200",
            description = "The document definition summaries were retrieved"),
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
          "/document-definition-categories/{documentDefinitionCategoryId}/document-definition-summaries",
      method = RequestMethod.GET,
      produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.OK)
  @PreAuthorize(
      "isSecurityDisabled() or hasRole('Administrator') or hasAuthority('FUNCTION_Operations.OperationsAdministration') or hasAuthority('FUNCTION_Operations.DocumentAdministration') or hasAuthority('FUNCTION_Operations.Indexing')")
  List<DocumentDefinitionSummary> getDocumentDefinitionSummaries(
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
              name = "documentDefinitionCategoryId",
              description = "The ID for the document definition category",
              required = true)
          @PathVariable
          String documentDefinitionCategoryId)
      throws InvalidArgumentException,
          DocumentDefinitionCategoryNotFoundException,
          ServiceUnavailableException;

  /**
   * Retrieve the document note.
   *
   * @param tenantId the ID for the tenant
   * @param documentId the ID for the document
   * @param documentNoteId the ID for the document note
   * @return the document note
   * @throws InvalidArgumentException if an argument is invalid
   * @throws DocumentNotFoundException if the document could not be found
   * @throws DocumentNoteNotFoundException if the document note could not be found
   * @throws ServiceUnavailableException if the document note could not be retrieved
   */
  @Operation(summary = "Retrieve the document note", description = "Retrieve the document note")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "200", description = "The document note was retrieved"),
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
            description = "The document or document note could not be found",
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
      value = "/documents/{documentId}/notes/{documentNoteId}",
      method = RequestMethod.GET,
      produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.OK)
  @PreAuthorize(
      "isSecurityDisabled() or hasRole('Administrator') or hasAuthority('FUNCTION_Operations.OperationsAdministration') or hasAuthority('FUNCTION_Operations.DocumentAdministration') or hasAuthority('FUNCTION_Operations.DocumentNoteAdministration') or hasAuthority('FUNCTION_Operations.Indexing')")
  DocumentNote getDocumentNote(
      @Parameter(
              name = "Tenant-ID",
              description = "The ID for the tenant",
              example = "00000000-0000-0000-0000-000000000000")
          @RequestHeader(
              name = "Tenant-ID",
              defaultValue = "00000000-0000-0000-0000-000000000000",
              required = false)
          UUID tenantId,
      @Parameter(name = "documentId", description = "The ID for the document", required = true)
          @PathVariable
          UUID documentId,
      @Parameter(
              name = "documentNoteId",
              description = "The ID for the document note",
              required = true)
          @PathVariable
          UUID documentNoteId)
      throws InvalidArgumentException,
          DocumentNotFoundException,
          DocumentNoteNotFoundException,
          ServiceUnavailableException;

  /**
   * Retrieve the document notes for the document.
   *
   * @param tenantId the ID for the tenant
   * @param documentId the ID for the document the document notes are associated with
   * @param filter the filter to apply to the document notes
   * @param sortBy the method used to sort the document notes, e.g. by created
   * @param sortDirection the sort direction to apply to the document notes
   * @param pageIndex the page index
   * @param pageSize the page size
   * @return the document notes
   * @throws InvalidArgumentException if an argument is invalid
   * @throws DocumentNotFoundException if the document could not be found
   * @throws ServiceUnavailableException if the document notes could not be retrieved
   */
  @Operation(
      summary = "Retrieve the document notes for the document",
      description = "Retrieve the document notes for the document")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "200",
            description = "The document notes for the document were retrieved"),
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
      value = "/documents/{documentId}/notes",
      method = RequestMethod.GET,
      produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.OK)
  @PreAuthorize(
      "isSecurityDisabled() or hasRole('Administrator') or hasAuthority('FUNCTION_Operations.OperationsAdministration') or hasAuthority('FUNCTION_Operations.DocumentAdministration') or hasAuthority('FUNCTION_Operations.DocumentNoteAdministration') or hasAuthority('FUNCTION_Operations.Indexing')")
  DocumentNotes getDocumentNotes(
      @Parameter(
              name = "Tenant-ID",
              description = "The ID for the tenant",
              example = "00000000-0000-0000-0000-000000000000")
          @RequestHeader(
              name = "Tenant-ID",
              defaultValue = "00000000-0000-0000-0000-000000000000",
              required = false)
          UUID tenantId,
      @Parameter(name = "documentId", description = "The ID for the document", required = true)
          @PathVariable
          UUID documentId,
      @Parameter(name = "filter", description = "The filter to apply to the document notes")
          @RequestParam(value = "filter", required = false)
          String filter,
      @Parameter(
              name = "sortBy",
              description = "The method used to sort the document notes e.g. by created")
          @RequestParam(value = "sortBy", required = false)
          DocumentNoteSortBy sortBy,
      @Parameter(
              name = "sortDirection",
              description = "The sort direction to apply to the document summaries")
          @RequestParam(value = "sortDirection", required = false)
          SortDirection sortDirection,
      @Parameter(name = "pageIndex", description = "The page index", example = "0")
          @RequestParam(value = "pageIndex", required = false, defaultValue = "0")
          Integer pageIndex,
      @Parameter(name = "pageSize", description = "The page size", example = "10")
          @RequestParam(value = "pageSize", required = false, defaultValue = "10")
          Integer pageSize)
      throws InvalidArgumentException, DocumentNotFoundException, ServiceUnavailableException;

  /**
   * Retrieve the summaries for the documents.
   *
   * @param tenantId the ID for the tenant
   * @param documentDefinitionId the document definition ID filter to apply to the document
   *     summaries
   * @param filter the filter to apply to the document summaries
   * @param sortBy the method used to sort the document summaries e.g. by definition ID
   * @param sortDirection the sort direction to apply to the document summaries
   * @param pageIndex the page index
   * @param pageSize the page size
   * @return the summaries for the documents
   * @throws InvalidArgumentException if an argument is invalid
   * @throws ServiceUnavailableException if the document summaries could not be retrieved
   */
  @Operation(
      summary = "Retrieve the document summaries",
      description = "Retrieve the document summaries")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "200", description = "The document summaries were retrieved"),
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
      value = "/document-summaries",
      method = RequestMethod.GET,
      produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.OK)
  @PreAuthorize(
      "isSecurityDisabled() or hasRole('Administrator') or hasAuthority('FUNCTION_Operations.OperationsAdministration') or hasAuthority('FUNCTION_Operations.DocumentAdministration') or hasAuthority('FUNCTION_Operations.Indexing')")
  DocumentSummaries getDocumentSummaries(
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
              name = "documentDefinitionId",
              description = "The document definition ID filter to apply to the document summaries")
          @RequestParam(value = "documentDefinitionId", required = false)
          String documentDefinitionId,
      @Parameter(name = "filter", description = "The filter to apply to the document summaries")
          @RequestParam(value = "filter", required = false)
          String filter,
      @Parameter(
              name = "sortBy",
              description = "The method used to sort the document summaries e.g. by definition ID")
          @RequestParam(value = "sortBy", required = false)
          DocumentSortBy sortBy,
      @Parameter(
              name = "sortDirection",
              description = "The sort direction to apply to the document summaries")
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
   * Search for documents.
   *
   * @param tenantId the ID for the tenant
   * @param searchDocumentsRequest the request to search for documents matching specific criteria
   * @return the summaries for the documents matching the search criteria
   * @throws InvalidArgumentException if an argument is invalid
   * @throws ServiceUnavailableException if the document search failed
   */
  @Operation(summary = "Search for documents", description = "Search for documents")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "200", description = "The document summaries were retrieved"),
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
      value = "/search-documents",
      method = RequestMethod.POST,
      produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.OK)
  @PreAuthorize(
      "isSecurityDisabled() or hasRole('Administrator') or hasAuthority('FUNCTION_Operations.OperationsAdministration') or hasAuthority('FUNCTION_Operations.DocumentAdministration') or hasAuthority('FUNCTION_Operations.Indexing')")
  DocumentSummaries searchDocuments(
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
              description = "The request to search for documents matching specific criteria",
              required = true)
          @RequestBody
          SearchDocumentsRequest searchDocumentsRequest)
      throws InvalidArgumentException, ServiceUnavailableException;

  /**
   * Update the document.
   *
   * @param tenantId the ID for the tenant
   * @param updateDocumentRequest the request to update the document
   * @throws InvalidArgumentException if an argument is invalid
   * @throws DocumentNotFoundException if the document could not be found
   * @throws ServiceUnavailableException if the document could not be updated
   */
  @Operation(summary = "Update the document", description = "Update the document")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "204", description = "The document was updated"),
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
            description = "The document could not be found",
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
      value = "/update-document",
      method = RequestMethod.POST,
      produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.NO_CONTENT)
  @PreAuthorize(
      "isSecurityDisabled() or hasRole('Administrator') or hasAuthority('FUNCTION_Operations.OperationsAdministration') or hasAuthority('FUNCTION_Operations.DocumentAdministration') or hasAuthority('FUNCTION_Operations.Indexing')")
  void updateDocument(
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
              description = "The request to update the document",
              required = true)
          @RequestBody
          UpdateDocumentRequest updateDocumentRequest)
      throws InvalidArgumentException, DocumentNotFoundException, ServiceUnavailableException;

  /**
   * Update the document attribute definition.
   *
   * @param documentAttributeDefinitionCode the code for the document attribute definition
   * @param documentAttributeDefinition the document attribute definition
   * @throws InvalidArgumentException if an argument is invalid
   * @throws DocumentAttributeDefinitionNotFoundException if the document attribute definition could
   *     not be found
   * @throws ServiceUnavailableException if the document attribute definition could not be updated
   */
  @Operation(
      summary = "Update the document attribute definition",
      description = "Update the document attribute definition")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "204",
            description = "The document attribute definition was updated"),
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
            description = "The document attribute definition could not be found",
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
      value = "/document-attribute-definitions/{documentAttributeDefinitionCode}",
      method = RequestMethod.PUT,
      produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.NO_CONTENT)
  @PreAuthorize(
      "isSecurityDisabled() or hasRole('Administrator') or hasAuthority('FUNCTION_Operations.OperationsAdministration')")
  void updateDocumentAttributeDefinition(
      @Parameter(
              name = "documentAttributeDefinitionCode",
              description = "The code for the document attribute definition",
              required = true)
          @PathVariable
          String documentAttributeDefinitionCode,
      @io.swagger.v3.oas.annotations.parameters.RequestBody(
              description = "The document attribute definition",
              required = true)
          @RequestBody
          DocumentAttributeDefinition documentAttributeDefinition)
      throws InvalidArgumentException,
          DocumentAttributeDefinitionNotFoundException,
          ServiceUnavailableException;

  /**
   * Update the document definition.
   *
   * @param documentDefinitionCategoryId the ID for the document definition category
   * @param documentDefinitionId the ID for the document definition
   * @param documentDefinition the document definition
   * @throws InvalidArgumentException if an argument is invalid
   * @throws DocumentDefinitionCategoryNotFoundException if the document definition category could
   *     not be found
   * @throws DocumentDefinitionNotFoundException if the document definition could not be found
   * @throws ServiceUnavailableException if the document definition could not be updated
   */
  @Operation(
      summary = "Update the document definition",
      description = "Update the document definition")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "204", description = "The document definition was updated"),
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
                "The document definition category or document definition could not be found",
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
          "/document-definition-categories/{documentDefinitionCategoryId}/document-definitions/{documentDefinitionId}",
      method = RequestMethod.PUT,
      produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.NO_CONTENT)
  @PreAuthorize(
      "isSecurityDisabled() or hasRole('Administrator') or hasAuthority('FUNCTION_Operations.OperationsAdministration')")
  void updateDocumentDefinition(
      @Parameter(
              name = "documentDefinitionCategoryId",
              description = "The ID for the document definition category",
              required = true)
          @PathVariable
          String documentDefinitionCategoryId,
      @Parameter(
              name = "documentDefinitionId",
              description = "The ID for the document definition",
              required = true)
          @PathVariable
          String documentDefinitionId,
      @io.swagger.v3.oas.annotations.parameters.RequestBody(
              description = "The document definition",
              required = true)
          @RequestBody
          DocumentDefinition documentDefinition)
      throws InvalidArgumentException,
          DocumentDefinitionCategoryNotFoundException,
          DocumentDefinitionNotFoundException,
          ServiceUnavailableException;

  /**
   * Update the document definition category.
   *
   * @param documentDefinitionCategoryId the ID for the document definition category
   * @param documentDefinitionCategory the document definition category
   * @throws InvalidArgumentException if an argument is invalid
   * @throws DocumentDefinitionCategoryNotFoundException if the document definition category could
   *     not be found
   * @throws ServiceUnavailableException if the document definition category could not be updated
   */
  @Operation(
      summary = "Update the document definition category",
      description = "Update the document definition category")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "204",
            description = "The document definition category was updated"),
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
            description = "The document definition category could not be found",
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
      value = "/document-definition-categories/{documentDefinitionCategoryId}",
      method = RequestMethod.PUT,
      produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.NO_CONTENT)
  @PreAuthorize(
      "isSecurityDisabled() or hasRole('Administrator') or hasAuthority('FUNCTION_Operations.OperationsAdministration')")
  void updateDocumentDefinitionCategory(
      @Parameter(
              name = "documentDefinitionCategoryId",
              description = "The ID for the document definition category",
              required = true)
          @PathVariable
          String documentDefinitionCategoryId,
      @io.swagger.v3.oas.annotations.parameters.RequestBody(
              description = "The document definition category",
              required = true)
          @RequestBody
          DocumentDefinitionCategory documentDefinitionCategory)
      throws InvalidArgumentException,
          DocumentDefinitionCategoryNotFoundException,
          ServiceUnavailableException;

  /**
   * Update the document note.
   *
   * @param tenantId the ID for the tenant
   * @param updateDocumentNoteRequest the request to update the document note
   * @throws InvalidArgumentException if an argument is invalid
   * @throws DocumentNoteNotFoundException if the document note could not be found
   * @throws ServiceUnavailableException if the document note could not be updated
   */
  @Operation(summary = "Update the document note", description = "Update the document note")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "204", description = "The document note was updated"),
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
            description = "The document note could not be found",
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
      value = "/update-document-note",
      method = RequestMethod.POST,
      produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.NO_CONTENT)
  @PreAuthorize(
      "isSecurityDisabled() or hasRole('Administrator') or hasAuthority('FUNCTION_Operations.OperationsAdministration') or hasAuthority('FUNCTION_Operations.DocumentAdministration') or hasAuthority('FUNCTION_Operations.DocumentNoteAdministration') or hasAuthority('FUNCTION_Operations.Indexing')")
  void updateDocumentNote(
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
              description = "The request to update the document note",
              required = true)
          @RequestBody
          UpdateDocumentNoteRequest updateDocumentNoteRequest)
      throws InvalidArgumentException, DocumentNoteNotFoundException, ServiceUnavailableException;
}
