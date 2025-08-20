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
import digital.inception.operations.exception.DuplicateInteractionException;
import digital.inception.operations.exception.DuplicateInteractionSourceException;
import digital.inception.operations.exception.InteractionNotFoundException;
import digital.inception.operations.exception.InteractionNoteNotFoundException;
import digital.inception.operations.exception.InteractionSourceNotFoundException;
import digital.inception.operations.exception.PartyNotFoundException;
import digital.inception.operations.model.AssignInteractionRequest;
import digital.inception.operations.model.CreateInteractionNoteRequest;
import digital.inception.operations.model.DelinkPartyFromInteractionRequest;
import digital.inception.operations.model.Interaction;
import digital.inception.operations.model.InteractionDirection;
import digital.inception.operations.model.InteractionNote;
import digital.inception.operations.model.InteractionNoteSortBy;
import digital.inception.operations.model.InteractionNotes;
import digital.inception.operations.model.InteractionSortBy;
import digital.inception.operations.model.InteractionSource;
import digital.inception.operations.model.InteractionSourceSummary;
import digital.inception.operations.model.InteractionStatus;
import digital.inception.operations.model.InteractionSummaries;
import digital.inception.operations.model.LinkPartyToInteractionRequest;
import digital.inception.operations.model.UpdateInteractionNoteRequest;
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
 * The {@code InteractionApiController} interface.
 *
 * @author Marcus Portmann
 */
@Tag(name = "Interaction")
@RequestMapping(value = "/api/operations")
// @el (isSecurityDisabled: PolicyDecisionPointSecurityExpressionRoot.isSecurityDisabled)
public interface InteractionApiController {

  /**
   * Assign an interaction to a user.
   *
   * @param tenantId the ID for the tenant
   * @param assignInteractionRequest the request to assign an interaction to a user
   * @throws InvalidArgumentException if an argument is invalid
   * @throws InteractionNotFoundException if the interaction could not be found
   * @throws ServiceUnavailableException if the interaction could not be assigned to the user
   */
  @Operation(summary = "Assign interaction to user", description = "Assign interaction to user")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "204",
            description = "The interaction was assigned to the user"),
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
            description = "The interaction could not be found",
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
      value = "/assign-interaction",
      method = RequestMethod.POST,
      produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.NO_CONTENT)
  @PreAuthorize(
      "isSecurityDisabled() or hasRole('Administrator') or hasAuthority('FUNCTION_Operations.OperationsAdministration') or hasAuthority('FUNCTION_Operations.InteractionAdministration') or hasAuthority('FUNCTION_Operations.Indexing')")
  void assignInteraction(
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
              description = "The request to assign the interaction to a user",
              required = true)
          @RequestBody
          AssignInteractionRequest assignInteractionRequest)
      throws InvalidArgumentException, InteractionNotFoundException, ServiceUnavailableException;

  /**
   * Create the interaction.
   *
   * @param tenantId the ID for the tenant
   * @param interaction the interaction
   * @throws InvalidArgumentException if an argument is invalid
   * @throws DuplicateInteractionException if the interaction already exists
   * @throws ServiceUnavailableException if the interaction could not be created
   */
  @Operation(summary = "Create the interaction", description = "Create the interaction")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "204", description = "The interaction was created"),
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
            description = "An interaction with the specified ID already exists",
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
      value = "/interactions",
      method = RequestMethod.POST,
      produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.NO_CONTENT)
  @PreAuthorize(
      "isSecurityDisabled() or hasRole('Administrator') or hasAuthority('FUNCTION_Operations.OperationsAdministration')")
  void createInteraction(
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
              description = "The interaction to create",
              required = true)
          @RequestBody
          Interaction interaction)
      throws InvalidArgumentException, DuplicateInteractionException, ServiceUnavailableException;

  /**
   * Create the interaction note.
   *
   * @param tenantId the ID for the tenant
   * @param createInteractionNoteRequest the request to create the interaction note
   * @return the ID for the interaction note
   * @throws InvalidArgumentException if an argument is invalid
   * @throws InteractionNotFoundException if the interaction could not be found
   * @throws ServiceUnavailableException if the interaction note could not be created
   */
  @Operation(summary = "Create the interaction note", description = "Create the interaction note")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "200", description = "The interaction note was created"),
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
            description = "The interaction could not be found",
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
      value = "/create-interaction-note",
      method = RequestMethod.POST,
      produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.OK)
  @PreAuthorize(
      "isSecurityDisabled() or hasRole('Administrator') or hasAuthority('FUNCTION_Operations.OperationsAdministration') or hasAuthority('FUNCTION_Operations.InteractionAdministration') or hasAuthority('FUNCTION_Operations.InteractionNoteAdministration') or hasAuthority('FUNCTION_Operations.Indexing')")
  UUID createInteractionNote(
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
              description = "The request to create the interaction note",
              required = true)
          @RequestBody
          CreateInteractionNoteRequest createInteractionNoteRequest)
      throws InvalidArgumentException, InteractionNotFoundException, ServiceUnavailableException;

  /**
   * Create the interaction source.
   *
   * @param tenantId the ID for the tenant
   * @param interactionSource the interaction source
   * @throws InvalidArgumentException if an argument is invalid
   * @throws DuplicateInteractionSourceException if the interaction source already exists
   * @throws ServiceUnavailableException if the interaction source could not be created
   */
  @Operation(
      summary = "Create the interaction source",
      description = "Create the interaction source")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "204", description = "The interaction source was created"),
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
            description = "An interaction source with the specified ID already exists",
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
      value = "/interaction-sources",
      method = RequestMethod.POST,
      produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.NO_CONTENT)
  @PreAuthorize(
      "isSecurityDisabled() or hasRole('Administrator') or hasAuthority('FUNCTION_Operations.OperationsAdministration')")
  void createInteractionSource(
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
              description = "The interaction source to create",
              required = true)
          @RequestBody
          InteractionSource interactionSource)
      throws InvalidArgumentException,
          DuplicateInteractionSourceException,
          ServiceUnavailableException;

  /**
   * Delete the interaction.
   *
   * @param tenantId the ID for the tenant
   * @param interactionId the ID for the interaction
   * @throws InvalidArgumentException if an argument is invalid
   * @throws InteractionNotFoundException if the interaction could not be found
   * @throws ServiceUnavailableException if the interaction could not be deleted
   */
  @Operation(summary = "Delete the interaction", description = "Delete the interaction")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "204", description = "The interaction was deleted"),
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
            description = "The interaction could not be found",
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
      value = "/interactions/{interactionId}",
      method = RequestMethod.DELETE,
      produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.NO_CONTENT)
  @PreAuthorize(
      "isSecurityDisabled() or hasRole('Administrator') or hasAuthority('FUNCTION_Operations.OperationsAdministration') or hasAuthority('FUNCTION_Operations.InteractionAdministration')")
  void deleteInteraction(
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
              name = "interactionId",
              description = "The ID for the interaction",
              required = true)
          @PathVariable
          UUID interactionId)
      throws InvalidArgumentException, InteractionNotFoundException, ServiceUnavailableException;

  /**
   * Delete the interaction source.
   *
   * @param tenantId the ID for the tenant
   * @param interactionSourceId the ID for the interaction source
   * @throws InvalidArgumentException if an argument is invalid
   * @throws InteractionSourceNotFoundException if the interaction source could not be found
   * @throws ServiceUnavailableException if the interaction source could not be deleted
   */
  @Operation(
      summary = "Delete the interaction source",
      description = "Delete the interaction source")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "204", description = "The interaction source was deleted"),
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
            description = "The interaction source could not be found",
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
      value = "/interaction-sources/{interactionSourceId}",
      method = RequestMethod.DELETE,
      produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.NO_CONTENT)
  @PreAuthorize(
      "isSecurityDisabled() or hasRole('Administrator') or hasAuthority('FUNCTION_Operations.OperationsAdministration')")
  void deleteInteractionSource(
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
              name = "interactionSourceId",
              description = "The ID for the interaction source",
              required = true)
          @PathVariable
          UUID interactionSourceId)
      throws InvalidArgumentException,
          InteractionSourceNotFoundException,
          ServiceUnavailableException;

  /**
   * Delink the party from the interaction.
   *
   * @param tenantId the ID for the tenant
   * @param delinkPartyFromInteractionRequest the request to delink the party from the interaction
   * @throws InvalidArgumentException if an argument is invalid
   * @throws InteractionNotFoundException if the interaction could not be found
   * @throws ServiceUnavailableException if the party could not be delinked from the interaction
   */
  @Operation(
      summary = "Delink the party from the interaction",
      description = "Delink the party from the interaction")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "204",
            description = "The party was delinked from the interaction"),
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
            description = "The interaction could not be found",
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
      value = "/delink-party-from-interaction",
      method = RequestMethod.POST,
      produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.NO_CONTENT)
  @PreAuthorize(
      "isSecurityDisabled() or hasRole('Administrator') or hasAuthority('FUNCTION_Operations.OperationsAdministration') or hasAuthority('FUNCTION_Operations.InteractionAdministration') or hasAuthority('FUNCTION_Operations.InteractionPartyLinkAdministration') or hasAuthority('FUNCTION_Operations.Indexing')")
  void delinkPartyFromInteraction(
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
              description = "The request to delink a party from the interaction",
              required = true)
          @RequestBody
          DelinkPartyFromInteractionRequest delinkPartyFromInteractionRequest)
      throws InvalidArgumentException, InteractionNotFoundException, ServiceUnavailableException;

  /**
   * Retrieve the interaction.
   *
   * @param tenantId the ID for the tenant
   * @param interactionId the ID for the interaction
   * @return the interaction
   * @throws InvalidArgumentException if an argument is invalid
   * @throws InteractionNotFoundException if the interaction could not be found
   * @throws ServiceUnavailableException if the interaction could not be retrieved
   */
  @Operation(summary = "Retrieve the interaction", description = "Retrieve the interaction")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "200", description = "The interaction was retrieved"),
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
            description = "The interaction could not be found",
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
      value = "/interactions/{interactionId}",
      method = RequestMethod.GET,
      produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.OK)
  @PreAuthorize(
      "isSecurityDisabled() or hasRole('Administrator') or hasAuthority('FUNCTION_Operations.OperationsAdministration') or hasAuthority('FUNCTION_Operations.InteractionAdministration') or hasAuthority('FUNCTION_Operations.Indexing')")
  Interaction getInteraction(
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
              name = "interactionId",
              description = "The ID for the interaction",
              required = true)
          @PathVariable
          UUID interactionId)
      throws InvalidArgumentException, InteractionNotFoundException, ServiceUnavailableException;

  /**
   * Retrieve the interaction note.
   *
   * @param tenantId the ID for the tenant
   * @param interactionId the ID for the interaction
   * @param interactionNoteId the ID for the interaction note
   * @return the interaction note
   * @throws InvalidArgumentException if an argument is invalid
   * @throws InteractionNotFoundException if the interaction could not be found
   * @throws InteractionNoteNotFoundException if the interaction note could not be found
   * @throws ServiceUnavailableException if the interaction note could not be retrieved
   */
  @Operation(
      summary = "Retrieve the interaction note",
      description = "Retrieve the interaction note")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "200", description = "The interaction note was retrieved"),
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
            description = "The interaction or interaction note could not be found",
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
      value = "/interactions/{interactionId}/notes/{interactionNoteId}",
      method = RequestMethod.GET,
      produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.OK)
  @PreAuthorize(
      "isSecurityDisabled() or hasRole('Administrator') or hasAuthority('FUNCTION_Operations.OperationsAdministration') or hasAuthority('FUNCTION_Operations.InteractionAdministration') or hasAuthority('FUNCTION_Operations.InteractionNoteAdministration') or hasAuthority('FUNCTION_Operations.Indexing')")
  InteractionNote getInteractionNote(
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
              name = "interactionId",
              description = "The ID for the interaction",
              required = true)
          @PathVariable
          UUID interactionId,
      @Parameter(
              name = "interactionNoteId",
              description = "The ID for the interaction note",
              required = true)
          @PathVariable
          UUID interactionNoteId)
      throws InvalidArgumentException,
          InteractionNotFoundException,
          InteractionNoteNotFoundException,
          ServiceUnavailableException;

  /**
   * Retrieve the interaction notes for the interaction.
   *
   * @param tenantId the ID for the tenant
   * @param interactionId the ID for the interaction the interaction notes are associated with
   * @param filter the filter to apply to the interaction notes
   * @param sortBy the method used to sort the interaction notes, e.g. by created
   * @param sortDirection the sort direction to apply to the interaction notes
   * @param pageIndex the page index
   * @param pageSize the page size
   * @return the interaction notes
   * @throws InvalidArgumentException if an argument is invalid
   * @throws InteractionNotFoundException if the interaction could not be found
   * @throws ServiceUnavailableException if the interaction notes could not be retrieved
   */
  @Operation(
      summary = "Retrieve the interaction notes for the interaction",
      description = "Retrieve the interaction notes for the interaction")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "200",
            description = "The interaction notes for the interaction were retrieved"),
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
            description = "The interaction could not be found",
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
      value = "/interactions/{interactionId}/notes",
      method = RequestMethod.GET,
      produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.OK)
  @PreAuthorize(
      "isSecurityDisabled() or hasRole('Administrator') or hasAuthority('FUNCTION_Operations.OperationsAdministration') or hasAuthority('FUNCTION_Operations.InteractionAdministration') or hasAuthority('FUNCTION_Operations.InteractionNoteAdministration') or hasAuthority('FUNCTION_Operations.Indexing')")
  InteractionNotes getInteractionNotes(
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
              name = "interactionId",
              description = "The ID for the interaction",
              required = true)
          @PathVariable
          UUID interactionId,
      @Parameter(name = "filter", description = "The filter to apply to the interaction notes")
          @RequestParam(value = "filter", required = false)
          String filter,
      @Parameter(
              name = "sortBy",
              description = "The method used to sort the interaction notes e.g. by created")
          @RequestParam(value = "sortBy", required = false)
          InteractionNoteSortBy sortBy,
      @Parameter(
              name = "sortDirection",
              description = "The sort direction to apply to the interaction notes")
          @RequestParam(value = "sortDirection", required = false)
          SortDirection sortDirection,
      @Parameter(name = "pageIndex", description = "The page index", example = "0")
          @RequestParam(value = "pageIndex", required = false, defaultValue = "0")
          Integer pageIndex,
      @Parameter(name = "pageSize", description = "The page size", example = "10")
          @RequestParam(value = "pageSize", required = false, defaultValue = "10")
          Integer pageSize)
      throws InvalidArgumentException, InteractionNotFoundException, ServiceUnavailableException;

  /**
   * Retrieve the interaction source.
   *
   * @param tenantId the ID for the tenant
   * @param interactionSourceId the ID for the interaction source
   * @return the interaction source
   * @throws InvalidArgumentException if an argument is invalid
   * @throws InteractionSourceNotFoundException if the interaction source could not be found
   * @throws ServiceUnavailableException if the interaction source could not be retrieved
   */
  @Operation(
      summary = "Retrieve the interaction source",
      description = "Retrieve the interaction source")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "200", description = "The interaction source was retrieved"),
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
            description = "The interaction source could not be found",
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
      value = "/interaction-sources/{interactionSourceId}",
      method = RequestMethod.GET,
      produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.OK)
  @PreAuthorize(
      "isSecurityDisabled() or hasRole('Administrator') or hasAuthority('FUNCTION_Operations.OperationsAdministration')")
  InteractionSource getInteractionSource(
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
              name = "interactionSourceId",
              description = "The ID for the interaction source",
              required = true)
          @PathVariable
          UUID interactionSourceId)
      throws InvalidArgumentException,
          InteractionSourceNotFoundException,
          ServiceUnavailableException;

  /**
   * Retrieve the summaries for the interaction sources.
   *
   * @param tenantId the ID for the tenant
   * @return the summaries for the interaction sources
   * @throws InvalidArgumentException if an argument is invalid
   * @throws ServiceUnavailableException if the interaction source summaries could not be retrieved
   */
  @Operation(
      summary = "Retrieve the interaction source summaries",
      description = "Retrieve the interaction source summaries")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "200",
            description = "The interaction source summaries were retrieved"),
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
      value = "/interaction-source-summaries",
      method = RequestMethod.GET,
      produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.OK)
  @PreAuthorize(
      "isSecurityDisabled() or hasRole('Administrator') or hasAuthority('FUNCTION_Operations.OperationsAdministration') or hasAuthority('FUNCTION_Operations.Indexing')")
  List<InteractionSourceSummary> getInteractionSourceSummaries(
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
   * Retrieve the interaction sources.
   *
   * @param tenantId the ID for the tenant
   * @return the interaction sources
   * @throws InvalidArgumentException if an argument is invalid
   * @throws ServiceUnavailableException if the interaction sources could not be retrieved
   */
  @Operation(
      summary = "Retrieve the interaction sources",
      description = "Retrieve the interaction sources")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "200", description = "The interaction sources were retrieved"),
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
      value = "/interaction-sources",
      method = RequestMethod.GET,
      produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.OK)
  @PreAuthorize(
      "isSecurityDisabled() or hasRole('Administrator') or hasAuthority('FUNCTION_Operations.OperationsAdministration')")
  List<InteractionSource> getInteractionSources(
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
   * Retrieve the summaries for the interactions for the interaction source with the specified ID.
   *
   * @param tenantId the ID for the tenant
   * @param interactionSourceId the ID for the interaction source the interactions are associated
   *     with
   * @param status the status filter to apply to the interaction summaries
   * @param direction the direction filter to apply to the interaction summaries, i.e., inbound or
   *     outbound
   * @param filter the filter to apply to the interaction summaries
   * @param sortBy the method used to sort the interaction summaries e.g. by occurred
   * @param sortDirection the sort direction to apply to the interaction summaries
   * @param pageIndex the page index
   * @param pageSize the page size
   * @return the summaries for the interactions
   * @throws InvalidArgumentException if an argument is invalid
   * @throws InteractionSourceNotFoundException if the interaction source could not be found
   * @throws ServiceUnavailableException if the interaction summaries could not be retrieved
   */
  @Operation(
      summary = "Retrieve the summaries for the interactions for the interaction source",
      description = "Retrieve the summaries for the interactions for the interaction source")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "200",
            description = "The interaction summaries were retrieved for the interaction source"),
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
            description = "The interaction source could not be found",
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
      value = "/interaction-sources/{interactionSourceId}/interaction-summaries",
      method = RequestMethod.GET,
      produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.OK)
  @PreAuthorize(
      "isSecurityDisabled() or hasRole('Administrator') or hasAuthority('FUNCTION_Operations.OperationsAdministration') or hasAuthority('FUNCTION_Operations.InteractionAdministration') or hasAuthority('FUNCTION_Operations.Indexing')")
  InteractionSummaries getInteractionSummaries(
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
              name = "interactionSourceId",
              description = "The ID for the interaction source",
              required = true)
          @PathVariable
          UUID interactionSourceId,
      @Parameter(
              name = "status",
              description = "The status filter to apply to the interaction summaries")
          @RequestParam(value = "status", required = false)
          InteractionStatus status,
      @Parameter(
              name = "direction",
              description = "The direction filter to apply to the interaction summaries")
          @RequestParam(value = "direction", required = false)
          InteractionDirection direction,
      @Parameter(name = "filter", description = "The filter to apply to the interaction summaries")
          @RequestParam(value = "filter", required = false)
          String filter,
      @Parameter(
              name = "sortBy",
              description = "The method used to sort the interaction summaries e.g. by occurred")
          @RequestParam(value = "sortBy", required = false)
          InteractionSortBy sortBy,
      @Parameter(
              name = "sortDirection",
              description = "The sort direction to apply to the interaction summaries")
          @RequestParam(value = "sortDirection", required = false)
          SortDirection sortDirection,
      @Parameter(name = "pageIndex", description = "The page index", example = "0")
          @RequestParam(value = "pageIndex", required = false, defaultValue = "0")
          Integer pageIndex,
      @Parameter(name = "pageSize", description = "The page size", example = "10")
          @RequestParam(value = "pageSize", required = false, defaultValue = "10")
          Integer pageSize)
      throws InvalidArgumentException,
          InteractionSourceNotFoundException,
          ServiceUnavailableException;

  /**
   * Link a party to an interaction.
   *
   * @param tenantId the ID for the tenant
   * @param linkPartyToInteractionRequest the request to link a party to an interaction
   * @throws InvalidArgumentException if an argument is invalid
   * @throws InteractionNotFoundException if the interaction could not be found
   * @throws PartyNotFoundException if the party could not be found
   * @throws ServiceUnavailableException if the party could not be linked to the interaction
   */
  @Operation(
      summary = "Link a party to an interaction",
      description = "Link a party to an interaction")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "204", description = "The party was linked to the interaction"),
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
            description = "The interaction or party could not be found",
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
      value = "/link-party-to-interaction",
      method = RequestMethod.POST,
      produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.NO_CONTENT)
  @PreAuthorize(
      "isSecurityDisabled() or hasRole('Administrator') or hasAuthority('FUNCTION_Operations.OperationsAdministration') or hasAuthority('FUNCTION_Operations.InteractionAdministration') or hasAuthority('FUNCTION_Operations.InteractionPartyLinkAdministration') or hasAuthority('FUNCTION_Operations.Indexing')")
  void linkPartyToInteraction(
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
              description = "The request to link a party to an interaction",
              required = true)
          @RequestBody
          LinkPartyToInteractionRequest linkPartyToInteractionRequest)
      throws InvalidArgumentException,
          InteractionNotFoundException,
          PartyNotFoundException,
          ServiceUnavailableException;

  /**
   * Update the interaction.
   *
   * @param tenantId the ID for the tenant
   * @param interactionId the ID for the interaction
   * @param interaction the interaction
   * @throws InvalidArgumentException if an argument is invalid
   * @throws InteractionNotFoundException if the interaction could not be found
   * @throws ServiceUnavailableException if the interaction could not be updated
   */
  @Operation(summary = "Update the interaction", description = "Update the interaction")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "204", description = "The interaction was updated"),
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
            description = "The interaction could not be found",
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
      value = "/interactions/{interactionId}",
      method = RequestMethod.PUT,
      produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.NO_CONTENT)
  @PreAuthorize(
      "isSecurityDisabled() or hasRole('Administrator') or hasAuthority('FUNCTION_Operations.OperationsAdministration')")
  void updateInteraction(
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
              name = "interactionId",
              description = "The ID for the interaction",
              required = true)
          @PathVariable
          UUID interactionId,
      @io.swagger.v3.oas.annotations.parameters.RequestBody(
              description = "The interaction to update",
              required = true)
          @RequestBody
          Interaction interaction)
      throws InvalidArgumentException, InteractionNotFoundException, ServiceUnavailableException;

  /**
   * Update the interaction note.
   *
   * @param tenantId the ID for the tenant
   * @param updateInteractionNoteRequest the request to update the interaction note
   * @throws InvalidArgumentException if an argument is invalid
   * @throws InteractionNoteNotFoundException if the interaction note could not be found
   * @throws ServiceUnavailableException if the interaction note could not be updated
   */
  @Operation(summary = "Update the interaction note", description = "Update the interaction note")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "204", description = "The interaction note was updated"),
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
            description = "The interaction note could not be found",
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
      value = "/update-interaction-note",
      method = RequestMethod.POST,
      produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.NO_CONTENT)
  @PreAuthorize(
      "isSecurityDisabled() or hasRole('Administrator') or hasAuthority('FUNCTION_Operations.OperationsAdministration') or hasAuthority('FUNCTION_Operations.InteractionAdministration') or hasAuthority('FUNCTION_Operations.InteractionNoteAdministration') or hasAuthority('FUNCTION_Operations.Indexing')")
  void updateInteractionNote(
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
              description = "The request to update the interaction note",
              required = true)
          @RequestBody
          UpdateInteractionNoteRequest updateInteractionNoteRequest)
      throws InvalidArgumentException,
          InteractionNoteNotFoundException,
          ServiceUnavailableException;

  /**
   * Update the interaction source.
   *
   * @param tenantId the ID for the tenant
   * @param interactionSourceId the ID for the interaction source
   * @param interactionSource the interaction source
   * @throws InvalidArgumentException if an argument is invalid
   * @throws InteractionSourceNotFoundException if the interaction source could not be found
   * @throws ServiceUnavailableException if the interaction source could not be updated
   */
  @Operation(
      summary = "Update the interaction source",
      description = "Update the interaction source")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "204", description = "The interaction source was updated"),
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
            description = "The interaction source could not be found",
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
      value = "/interaction-sources/{interactionSourceId}",
      method = RequestMethod.PUT,
      produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.NO_CONTENT)
  @PreAuthorize(
      "isSecurityDisabled() or hasRole('Administrator') or hasAuthority('FUNCTION_Operations.OperationsAdministration')")
  void updateInteractionSource(
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
              name = "interactionSourceId",
              description = "The ID for the interaction source",
              required = true)
          @PathVariable
          UUID interactionSourceId,
      @io.swagger.v3.oas.annotations.parameters.RequestBody(
              description = "The interaction source",
              required = true)
          @RequestBody
          InteractionSource interactionSource)
      throws InvalidArgumentException,
          InteractionSourceNotFoundException,
          ServiceUnavailableException;
}
