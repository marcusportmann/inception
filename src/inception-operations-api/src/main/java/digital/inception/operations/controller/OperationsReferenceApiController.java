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
import digital.inception.operations.exception.DuplicateExternalReferenceTypeException;
import digital.inception.operations.exception.ExternalReferenceTypeNotFoundException;
import digital.inception.operations.model.ExternalReferenceType;
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
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * The {@code OperationsReferenceApiController} interface.
 *
 * @author Marcus Portmann
 */
@Tag(name = "Operations Reference")
@RequestMapping(value = "/api/operations/reference")
// @el (isSecurityDisabled: PolicyDecisionPointSecurityExpressionRoot.isSecurityDisabled)
public interface OperationsReferenceApiController {

  /**
   * Create the external reference type.
   *
   * @param externalReferenceType the external reference type
   * @throws InvalidArgumentException if an argument is invalid
   * @throws DuplicateExternalReferenceTypeException if the external reference type already exists
   * @throws ServiceUnavailableException if the external reference type could not be created
   */
  @Operation(
      summary = "Create the external reference type",
      description = "Create the external reference type")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "204", description = "The external reference type was created"),
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
            description = "An external reference type with the specified code already exists",
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
      value = "/external-reference-types",
      method = RequestMethod.POST,
      produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.NO_CONTENT)
  @PreAuthorize(
      "isSecurityDisabled() or hasRole('Administrator') or hasAuthority('FUNCTION_Operations.OperationsAdministration')")
  void createExternalReferenceType(
      @io.swagger.v3.oas.annotations.parameters.RequestBody(
              description = "The external reference type to create",
              required = true)
          @RequestBody
          ExternalReferenceType externalReferenceType)
      throws InvalidArgumentException,
          DuplicateExternalReferenceTypeException,
          ServiceUnavailableException;

  /**
   * Delete the external reference type.
   *
   * @param externalReferenceTypeCode the code for the external reference type
   * @throws InvalidArgumentException if an argument is invalid
   * @throws ExternalReferenceTypeNotFoundException if the external reference type could not be
   *     found
   * @throws ServiceUnavailableException if the external reference type could not be deleted
   */
  @Operation(
      summary = "Delete the external reference type",
      description = "Delete the external reference type")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "204", description = "The external reference type was deleted"),
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
            description = "The external reference type could not be found",
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
      value = "/external-reference-types/{externalReferenceTypeCode}",
      method = RequestMethod.DELETE,
      produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.NO_CONTENT)
  @PreAuthorize(
      "isSecurityDisabled() or hasRole('Administrator') or hasAuthority('FUNCTION_Operations.OperationsAdministration')")
  void deleteExternalReferenceType(
      @Parameter(
              name = "externalReferenceTypeCode",
              description = "The code for the external reference type",
              required = true)
          @PathVariable
          String externalReferenceTypeCode)
      throws InvalidArgumentException,
          ExternalReferenceTypeNotFoundException,
          ServiceUnavailableException;

  /**
   * Retrieve the external reference type.
   *
   * @param externalReferenceTypeCode the code for the external reference type
   * @return the external reference type
   * @throws InvalidArgumentException if an argument is invalid
   * @throws ExternalReferenceTypeNotFoundException if the external reference type could not be
   *     found
   * @throws ServiceUnavailableException if the external reference type could not be retrieved
   */
  @Operation(
      summary = "Retrieve the external reference type",
      description = "Retrieve the external reference type")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "200",
            description = "The external reference type was retrieved"),
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
            description = "The external reference type could not be found",
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
      value = "/external-reference-types/{externalReferenceTypeCode}",
      method = RequestMethod.GET,
      produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.OK)
  @PreAuthorize(
      "isSecurityDisabled() or hasRole('Administrator') or hasAuthority('FUNCTION_Operations.OperationsAdministration')")
  ExternalReferenceType getExternalReferenceType(
      @Parameter(
              name = "externalReferenceTypeCode",
              description = "The code for the external reference type",
              required = true)
          @PathVariable
          String externalReferenceTypeCode)
      throws InvalidArgumentException,
          ExternalReferenceTypeNotFoundException,
          ServiceUnavailableException;

  /**
   * Retrieve the external reference types.
   *
   * @param tenantId the ID for the tenant
   * @return the external reference types
   * @throws InvalidArgumentException if an argument is invalid
   * @throws ServiceUnavailableException if the external reference types could not be retrieved
   */
  @Operation(
      summary = "Retrieve the external reference types",
      description = "Retrieve the external reference types")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "200",
            description = "The external reference types were retrieved"),
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
      value = "/external-reference-types",
      method = RequestMethod.GET,
      produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.OK)
  @PreAuthorize(
      "isSecurityDisabled() or hasRole('Administrator') or hasAuthority('FUNCTION_Operations.OperationsAdministration')")
  List<ExternalReferenceType> getExternalReferenceTypes(
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
   * Update the external reference type.
   *
   * @param externalReferenceTypeCode the code for the external reference type
   * @param externalReferenceType the external reference type
   * @throws InvalidArgumentException if an argument is invalid
   * @throws ExternalReferenceTypeNotFoundException if the external reference type could not be
   *     found
   * @throws ServiceUnavailableException if the external reference type could not be updated
   */
  @Operation(
      summary = "Update the external reference type",
      description = "Update the external reference type")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "204", description = "The external reference type was updated"),
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
            description = "The external reference type could not be found",
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
      value = "/external-reference-types/{externalReferenceTypeCode}",
      method = RequestMethod.PUT,
      produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.NO_CONTENT)
  @PreAuthorize(
      "isSecurityDisabled() or hasRole('Administrator') or hasAuthority('FUNCTION_Operations.OperationsAdministration')")
  void updateExternalReferenceType(
      @Parameter(
              name = "externalReferenceTypeCode",
              description = "The code for the external reference type",
              required = true)
          @PathVariable
          String externalReferenceTypeCode,
      @io.swagger.v3.oas.annotations.parameters.RequestBody(
              description = "The external reference type",
              required = true)
          @RequestBody
          ExternalReferenceType externalReferenceType)
      throws InvalidArgumentException,
          ExternalReferenceTypeNotFoundException,
          ServiceUnavailableException;
}
