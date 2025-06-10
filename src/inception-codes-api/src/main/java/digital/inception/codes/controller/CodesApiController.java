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

package digital.inception.codes.controller;

import digital.inception.codes.exception.CodeCategoryNotFoundException;
import digital.inception.codes.exception.CodeNotFoundException;
import digital.inception.codes.exception.DuplicateCodeCategoryException;
import digital.inception.codes.exception.DuplicateCodeException;
import digital.inception.codes.model.Code;
import digital.inception.codes.model.CodeCategory;
import digital.inception.codes.model.CodeCategorySummary;
import digital.inception.core.api.ProblemDetails;
import digital.inception.core.exception.InvalidArgumentException;
import digital.inception.core.exception.ServiceUnavailableException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.time.OffsetDateTime;
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
 * The {@code CodesApiController} interface.
 *
 * @author Marcus Portmann
 */
@Tag(name = "Codes")
@RequestMapping(value = "/api/codes")
// @el (isSecurityDisabled: digital.inception.api.SecureApiSecurityExpressionRoot.isSecurityEnabled)
public interface CodesApiController {

  /**
   * Create the code.
   *
   * @param codeCategoryId the ID for the code category
   * @param code the code to create
   * @throws InvalidArgumentException if an argument is invalid
   * @throws DuplicateCodeException if the code already exists
   * @throws CodeCategoryNotFoundException if the code category could not be found
   * @throws ServiceUnavailableException if the code could not be created
   */
  @Operation(summary = "Create the code", description = "Create the code")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "204", description = "The code was created"),
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
            description = "A code with the specified ID already exists",
            content =
                @Content(
                    mediaType = "application/problem+json",
                    schema = @Schema(implementation = ProblemDetails.class))),
        @ApiResponse(
            responseCode = "404",
            description = "The code category could not be found",
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
      value = "/code-categories/{codeCategoryId}/codes",
      method = RequestMethod.POST,
      produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.NO_CONTENT)
  @PreAuthorize(
      "isSecurityDisabled() or hasRole('Administrator') or hasAccessToFunction('Codes.CodeAdministration')")
  void createCode(
      @Parameter(
              name = "codeCategoryId",
              description = "The ID for the code category",
              required = true)
          @PathVariable
          String codeCategoryId,
      @io.swagger.v3.oas.annotations.parameters.RequestBody(
              description = "The code to create",
              required = true)
          @RequestBody
          Code code)
      throws InvalidArgumentException,
          DuplicateCodeException,
          CodeCategoryNotFoundException,
          ServiceUnavailableException;

  /**
   * Create the code category.
   *
   * @param codeCategory the code category to create
   * @throws InvalidArgumentException if an argument is invalid
   * @throws DuplicateCodeCategoryException if the code category already exists
   * @throws ServiceUnavailableException if the code category could not be created
   */
  @Operation(summary = "Create the code category", description = "Create the code category")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "204", description = "The code category was created"),
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
            description = "A code category with the specified ID already exists",
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
      value = "/code-categories",
      method = RequestMethod.POST,
      produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.NO_CONTENT)
  @PreAuthorize(
      "isSecurityDisabled() or hasRole('Administrator') or hasAccessToFunction('Codes.CodeAdministration')")
  void createCodeCategory(
      @RequestBody
          @io.swagger.v3.oas.annotations.parameters.RequestBody(
              description = "The code category to create",
              required = true)
          CodeCategory codeCategory)
      throws InvalidArgumentException, DuplicateCodeCategoryException, ServiceUnavailableException;

  /**
   * Delete the code.
   *
   * @param codeCategoryId the ID for the code category
   * @param codeId the ID for the code
   * @throws InvalidArgumentException if an argument is invalid
   * @throws CodeNotFoundException if the code could not be found
   * @throws ServiceUnavailableException if the code could not be deleted
   */
  @Operation(summary = "Delete the code", description = "Delete the code")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "204", description = "The code was deleted"),
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
            description = "The code could not be found",
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
      value = "/code-categories/{codeCategoryId}/codes/{codeId}",
      method = RequestMethod.DELETE,
      produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.NO_CONTENT)
  @PreAuthorize(
      "isSecurityDisabled() or hasRole('Administrator') or hasAccessToFunction('Codes.CodeAdministration')")
  void deleteCode(
      @Parameter(
              name = "codeCategoryId",
              description = "The ID for the code category",
              required = true)
          @PathVariable
          String codeCategoryId,
      @Parameter(name = "codeId", description = "The ID for the code", required = true)
          @PathVariable
          String codeId)
      throws InvalidArgumentException, CodeNotFoundException, ServiceUnavailableException;

  /**
   * Delete the code category.
   *
   * @param codeCategoryId the ID for the code category
   * @throws InvalidArgumentException if an argument is invalid
   * @throws CodeCategoryNotFoundException if the code category could not be found
   * @throws ServiceUnavailableException if the code category could not be deleted
   */
  @Operation(summary = "Delete the code category", description = "Delete the code category")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "204", description = "The code category was deleted"),
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
            description = "The code category could not be found",
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
      value = "/code-categories/{codeCategoryId}",
      method = RequestMethod.DELETE,
      produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.NO_CONTENT)
  @PreAuthorize(
      "isSecurityDisabled() or hasRole('Administrator') or hasAccessToFunction('Codes.CodeAdministration')")
  void deleteCodeCategory(
      @Parameter(
              name = "codeCategoryId",
              description = "The ID for the code category",
              required = true)
          @PathVariable
          String codeCategoryId)
      throws InvalidArgumentException, CodeCategoryNotFoundException, ServiceUnavailableException;

  /**
   * Retrieve the code
   *
   * @param codeCategoryId the ID for the code category the code is associated with
   * @param codeId the ID for the code
   * @return the code
   * @throws InvalidArgumentException if an argument is invalid
   * @throws CodeNotFoundException if the code could not be found
   * @throws ServiceUnavailableException if the code could not be retrieved
   */
  @Operation(summary = "Retrieve the code", description = "Retrieve the code")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "200", description = "The code was retrieved"),
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
            description = "The code could not be found",
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
      value = "/code-categories/{codeCategoryId}/codes/{codeId}",
      method = RequestMethod.GET,
      produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.OK)
  @PreAuthorize(
      "isSecurityDisabled() or hasRole('Administrator') or hasAccessToFunction('Codes.CodeAdministration')")
  Code getCode(
      @Parameter(
              name = "codeCategoryId",
              description = "The ID for the code category the code is associated with",
              required = true)
          @PathVariable
          String codeCategoryId,
      @Parameter(name = "codeId", description = "The ID for the code", required = true)
          @PathVariable
          String codeId)
      throws InvalidArgumentException, CodeNotFoundException, ServiceUnavailableException;

  /**
   * Retrieve all the code categories.
   *
   * @return the code categories
   * @throws ServiceUnavailableException if the code categories could not be retrieved
   */
  @Operation(
      summary = "Retrieve all the code categories",
      description = "Retrieve all the code categories")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "200", description = "The code categories were retrieved"),
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
      value = "/code-categories",
      method = RequestMethod.GET,
      produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.OK)
  @PreAuthorize(
      "isSecurityDisabled() or hasRole('Administrator') or hasAccessToFunction('Codes.CodeAdministration')")
  List<CodeCategory> getCodeCategories() throws ServiceUnavailableException;

  /**
   * Retrieve the code category
   *
   * @param codeCategoryId the ID for the code category
   * @return the code category
   * @throws InvalidArgumentException if an argument is invalid
   * @throws CodeCategoryNotFoundException if the code category could not be found
   * @throws ServiceUnavailableException if the code category could not be retrieved
   */
  @Operation(summary = "Retrieve the code category", description = "Retrieve the code category")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "200", description = "The code category was retrieved"),
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
            description = "The code category could not be found",
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
      value = "/code-categories/{codeCategoryId}",
      method = RequestMethod.GET,
      produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.OK)
  @PreAuthorize(
      "isSecurityDisabled() or hasRole('Administrator') or hasAccessToFunction('Codes.CodeAdministration')")
  CodeCategory getCodeCategory(
      @Parameter(
              name = "codeCategoryId",
              description = "The ID for the code category",
              required = true)
          @PathVariable
          String codeCategoryId)
      throws InvalidArgumentException, CodeCategoryNotFoundException, ServiceUnavailableException;

  /**
   * Retrieve the XML or JSON data for a code category
   *
   * @param codeCategoryId the ID for the code category
   * @return the XML or JSON data for the code category
   * @throws InvalidArgumentException if an argument is invalid
   * @throws CodeCategoryNotFoundException if the code category could not be found
   * @throws ServiceUnavailableException if the code category data could not be retrieved
   */
  @Operation(
      summary = "Retrieve the XML or JSON data for a code category",
      description = "Retrieve the XML or JSON data for a code category")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "200",
            description = "The XML or JSON data for the code category was retrieved"),
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
            description = "The code category could not be found",
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
      value = "/code-categories/{codeCategoryId}/data",
      method = RequestMethod.GET,
      produces = "text/plain")
  @ResponseStatus(HttpStatus.OK)
  @PreAuthorize(
      "isSecurityDisabled() or hasRole('Administrator') or hasAccessToFunction('Codes.CodeAdministration')")
  String getCodeCategoryData(
      @Parameter(
              name = "codeCategoryId",
              description = "The ID for the code category",
              required = true)
          @PathVariable
          String codeCategoryId)
      throws InvalidArgumentException, CodeCategoryNotFoundException, ServiceUnavailableException;

  /**
   * Returns the date and time the code category was last modified.
   *
   * @param codeCategoryId the ID for the code category
   * @return the date and time the code category was last modified
   * @throws InvalidArgumentException if an argument is invalid
   * @throws CodeCategoryNotFoundException if the code category could not be found
   * @throws ServiceUnavailableException if the date and time the code category was last modified
   *     could not be retrieved
   */
  @Operation(
      summary = "Retrieve the date and time the code category was last modified",
      description = "Retrieve the date and time the code category was last modified")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "200",
            description = "The date and time the code category was last modified was retrieved"),
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
            description = "The code category could not be found",
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
      value = "/code-categories/{codeCategoryId}/last-modified",
      method = RequestMethod.GET,
      produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.OK)
  @PreAuthorize(
      "isSecurityDisabled() or hasRole('Administrator') or hasAccessToFunction('Codes.CodeAdministration')")
  OffsetDateTime getCodeCategoryLastModified(
      @Parameter(
              name = "codeCategoryId",
              description = "The ID for the code category",
              required = true)
          @PathVariable
          String codeCategoryId)
      throws InvalidArgumentException, CodeCategoryNotFoundException, ServiceUnavailableException;

  /**
   * Retrieve the name of the code category.
   *
   * @param codeCategoryId the ID for the code category
   * @return the name of the code category
   * @throws InvalidArgumentException if an argument is invalid
   * @throws CodeCategoryNotFoundException if the code category could not be found
   * @throws ServiceUnavailableException if the name of the code category could not be retrieved
   */
  @Operation(
      summary = "Retrieve the name of the code category",
      description = "Retrieve the name of the code category")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "200",
            description = "The name of the code category was retrieved"),
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
            description = "The code category could not be found",
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
      value = "/code-categories/{codeCategoryId}/name",
      method = RequestMethod.GET,
      produces = "text/plain")
  @ResponseStatus(HttpStatus.OK)
  @PreAuthorize(
      "isSecurityDisabled() or hasRole('Administrator') or hasAccessToFunction('Codes.CodeAdministration')")
  String getCodeCategoryName(
      @Parameter(
              name = "codeCategoryId",
              description = "The ID for the code category",
              required = true)
          @PathVariable
          String codeCategoryId)
      throws InvalidArgumentException, CodeCategoryNotFoundException, ServiceUnavailableException;

  /**
   * Retrieve the summaries for all the code categories.
   *
   * @return the summaries for the code categories
   * @throws ServiceUnavailableException if the code category summaries could not be retrieved
   */
  @Operation(
      summary = "Retrieve the code category summaries",
      description = "Retrieve the code category summaries")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "200",
            description = "The code category summaries were retrieved"),
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
      value = "/code-category-summaries",
      method = RequestMethod.GET,
      produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.OK)
  @PreAuthorize(
      "isSecurityDisabled() or hasRole('Administrator') or hasAccessToFunction('Codes.CodeAdministration')")
  List<CodeCategorySummary> getCodeCategorySummaries() throws ServiceUnavailableException;

  /**
   * Retrieve the name of the code.
   *
   * @param codeCategoryId the ID for the code category the code is associated with
   * @param codeId the ID for the code
   * @return the name of the code
   * @throws InvalidArgumentException if an argument is invalid
   * @throws CodeNotFoundException if the code could not be found
   * @throws ServiceUnavailableException if the name of the code could not be retrieved
   */
  @Operation(
      summary = "Retrieve the name of the code",
      description = "Retrieve the name of the code")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "200", description = "The name of the code was retrieved"),
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
            description = "The code could not be found",
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
      value = "/code-categories/{codeCategoryId}/codes/{codeId}/name",
      method = RequestMethod.GET,
      produces = "text/plain")
  @ResponseStatus(HttpStatus.OK)
  @PreAuthorize(
      "isSecurityDisabled() or hasRole('Administrator') or hasAccessToFunction('Codes.CodeAdministration')")
  String getCodeName(
      @Parameter(
              name = "codeCategoryId",
              description = "The ID for the code category the code is associated with",
              required = true)
          @PathVariable
          String codeCategoryId,
      @Parameter(name = "codeId", description = "The ID for the code", required = true)
          @PathVariable
          String codeId)
      throws InvalidArgumentException, CodeNotFoundException, ServiceUnavailableException;

  /**
   * Retrieve the codes for a code category
   *
   * @param codeCategoryId the ID for the code category
   * @return the codes for the code category
   * @throws InvalidArgumentException if an argument is invalid
   * @throws CodeCategoryNotFoundException if the code category could not be found
   * @throws ServiceUnavailableException if the codes for the code category could not be retrieved
   */
  @Operation(
      summary = "Retrieve the codes for a code category",
      description = "Retrieve the codes for a code category")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "200",
            description = "The codes for the code category were retrieved"),
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
            description = "The code category could not be found",
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
      value = "/code-categories/{codeCategoryId}/codes",
      method = RequestMethod.GET,
      produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.OK)
  @PreAuthorize(
      "isSecurityDisabled() or hasRole('Administrator') or hasAccessToFunction('Codes.CodeAdministration')")
  List<Code> getCodesForCodeCategory(
      @Parameter(
              name = "codeCategoryId",
              description = "The ID for the code category",
              required = true)
          @PathVariable
          String codeCategoryId)
      throws InvalidArgumentException, CodeCategoryNotFoundException, ServiceUnavailableException;

  /**
   * Update the code.
   *
   * @param codeCategoryId the ID for the code category
   * @param codeId the ID for the code
   * @param code the code to create
   * @throws InvalidArgumentException if an argument is invalid
   * @throws CodeNotFoundException if the code could not be found
   * @throws ServiceUnavailableException if the code could not be updated
   */
  @Operation(summary = "Update the code", description = "Update the code")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "204", description = "The code was updated"),
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
            description = "The code could not be found",
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
      value = "/code-categories/{codeCategoryId}/codes/{codeId}",
      method = RequestMethod.PUT,
      produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.NO_CONTENT)
  @PreAuthorize(
      "isSecurityDisabled() or hasRole('Administrator') or hasAccessToFunction('Codes.CodeAdministration')")
  void updateCode(
      @Parameter(
              name = "codeCategoryId",
              description = "The ID for the code category",
              required = true)
          @PathVariable
          String codeCategoryId,
      @Parameter(name = "codeId", description = "The ID for the code", required = true)
          @PathVariable
          String codeId,
      @io.swagger.v3.oas.annotations.parameters.RequestBody(
              description = "The code to update",
              required = true)
          @RequestBody
          Code code)
      throws InvalidArgumentException, CodeNotFoundException, ServiceUnavailableException;

  /**
   * Update the code category.
   *
   * @param codeCategoryId the ID for the code category
   * @param codeCategory the code category
   * @throws InvalidArgumentException if an argument is invalid
   * @throws CodeCategoryNotFoundException if the code category could not be found
   * @throws ServiceUnavailableException if the code category could not be updated
   */
  @Operation(summary = "Update the code category", description = "Update the code category")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "204", description = "The code category was updated"),
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
            description = "The code category could not be found",
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
      value = "/code-categories/{codeCategoryId}",
      method = RequestMethod.PUT,
      produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.NO_CONTENT)
  @PreAuthorize(
      "isSecurityDisabled() or hasRole('Administrator') or hasAccessToFunction('Codes.CodeAdministration')")
  void updateCodeCategory(
      @Parameter(
              name = "codeCategoryId",
              description = "The ID for the code category",
              required = true)
          @PathVariable
          String codeCategoryId,
      @io.swagger.v3.oas.annotations.parameters.RequestBody(
              description = "The code category to update",
              required = true)
          @RequestBody
          CodeCategory codeCategory)
      throws InvalidArgumentException, CodeCategoryNotFoundException, ServiceUnavailableException;
}
