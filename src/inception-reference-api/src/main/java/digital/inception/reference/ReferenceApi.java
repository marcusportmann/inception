/*
 * Copyright 2021 Marcus Portmann
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

package digital.inception.reference;

import digital.inception.api.ProblemDetails;
import digital.inception.api.SecureApi;
import digital.inception.core.service.ServiceUnavailableException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 * The <b>ReferenceApi</b> class.
 *
 * @author Marcus Portmann
 */
@Tag(name = "Reference")
@RestController
@RequestMapping(value = "/api/reference")
@CrossOrigin
@SuppressWarnings({"unused", "WeakerAccess"})
// @el (isSecurityDisabled: digital.inception.api.ApiSecurityExpressionRoot.isSecurityEnabled)
public class ReferenceApi extends SecureApi {

  /** The Reference Service. */
  private final IReferenceService referenceService;

  /**
   * Constructs a new <b>ReferenceRestController</b>.
   *
   * @param applicationContext the Spring application context
   * @param referenceService the Reference Service
   */
  public ReferenceApi(ApplicationContext applicationContext, IReferenceService referenceService) {
    super(applicationContext);

    this.referenceService = referenceService;
  }

  /**
   * Retrieve the countries.
   *
   * @param localeId the Unicode locale identifier for the locale to retrieve the countries for or
   *     <b>null</b> to retrieve the countries for all locales
   * @return the countries
   */
  @Operation(summary = "Retrieve the countries", description = "Retrieve the countries")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "200", description = "OK"),
        @ApiResponse(
            responseCode = "500",
            description =
                "An error has occurred and the request could not be processed at this time",
            content =
                @Content(
                    mediaType = "application/problem+json",
                    schema = @Schema(implementation = ProblemDetails.class)))
      })
  @RequestMapping(value = "/countries", method = RequestMethod.GET, produces = "application/json")
  @ResponseStatus(HttpStatus.OK)
  @PreAuthorize("isSecurityDisabled() or isAuthenticated()")
  public List<Country> getCountries(
      @Parameter(
              name = "localeId",
              description =
                  "The optional Unicode locale identifier for the locale to retrieve the countries for",
              example = "en-US")
          @RequestParam(value = "localeId", required = false)
          String localeId)
      throws ServiceUnavailableException {
    return referenceService.getCountries(localeId);
  }

  /**
   * Retrieve the languages.
   *
   * @param localeId the Unicode locale identifier for the locale to retrieve the languages for or
   *     <b>null</b> to retrieve the languages for all locales
   * @return the languages
   */
  @Operation(summary = "Retrieve the languages", description = "Retrieve the languages")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "200", description = "OK"),
        @ApiResponse(
            responseCode = "500",
            description =
                "An error has occurred and the request could not be processed at this time",
            content =
                @Content(
                    mediaType = "application/problem+json",
                    schema = @Schema(implementation = ProblemDetails.class)))
      })
  @RequestMapping(value = "/languages", method = RequestMethod.GET, produces = "application/json")
  @ResponseStatus(HttpStatus.OK)
  @PreAuthorize("isSecurityDisabled() or isAuthenticated()")
  public List<Language> getLanguages(
      @Parameter(
              name = "localeId",
              description =
                  "The optional Unicode locale identifier for the locale to retrieve the languages for",
              example = "en-US")
          @RequestParam(value = "localeId", required = false)
          String localeId)
      throws ServiceUnavailableException {
    return referenceService.getLanguages(localeId);
  }

  /**
   * Retrieve the regions.
   *
   * @param localeId the Unicode locale identifier for the locale to retrieve the regions for or
   *     <b>null</b> to retrieve the regions for all locales
   * @return the regions
   */
  @Operation(summary = "Retrieve the regions", description = "Retrieve the regions")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "200", description = "OK"),
        @ApiResponse(
            responseCode = "500",
            description =
                "An error has occurred and the request could not be processed at this time",
            content =
                @Content(
                    mediaType = "application/problem+json",
                    schema = @Schema(implementation = ProblemDetails.class)))
      })
  @RequestMapping(value = "/regions", method = RequestMethod.GET, produces = "application/json")
  @ResponseStatus(HttpStatus.OK)
  @PreAuthorize("isSecurityDisabled() or isAuthenticated()")
  public List<Region> getRegions(
      @Parameter(
              name = "localeId",
              description =
                  "The optional Unicode locale identifier for the locale to retrieve the regions for",
              example = "en-US")
          @RequestParam(value = "localeId", required = false)
          String localeId)
      throws ServiceUnavailableException {
    return referenceService.getRegions(localeId);
  }

  /**
   * Retrieve the verification methods.
   *
   * @param localeId the Unicode locale identifier for the locale to retrieve the verification
   *     methods for or <b>null</b> to retrieve the verification methods for all locales
   * @return the verification methods
   */
  @Operation(
      summary = "Retrieve the verification methods",
      description = "Retrieve the verification methods")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "200", description = "OK"),
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
      value = "/verification-methods",
      method = RequestMethod.GET,
      produces = "application/json")
  @ResponseStatus(HttpStatus.OK)
  @PreAuthorize("isSecurityDisabled() or isAuthenticated()")
  public List<VerificationMethod> getVerificationMethods(
      @Parameter(
              name = "localeId",
              description =
                  "The optional Unicode locale identifier for the locale to retrieve the verification methods for",
              example = "en-US")
          @RequestParam(value = "localeId", required = false)
          String localeId)
      throws ServiceUnavailableException {
    return referenceService.getVerificationMethods(localeId);
  }

  /**
   * Retrieve the verification statuses.
   *
   * @param localeId the Unicode locale identifier for the locale to retrieve the verification
   *     statuses for or <b>null</b> to retrieve the verification statuses for all locales
   * @return the verification statuses
   */
  @Operation(
      summary = "Retrieve the verification statuses",
      description = "Retrieve the verification statuses")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "200", description = "OK"),
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
      value = "/verification-statuses",
      method = RequestMethod.GET,
      produces = "application/json")
  @ResponseStatus(HttpStatus.OK)
  @PreAuthorize("isSecurityDisabled() or isAuthenticated()")
  public List<VerificationStatus> getVerificationStatuses(
      @Parameter(
              name = "localeId",
              description =
                  "The optional Unicode locale identifier for the locale to retrieve the verification statuses for",
              example = "en-US")
          @RequestParam(value = "localeId", required = false)
          String localeId)
      throws ServiceUnavailableException {
    return referenceService.getVerificationStatuses(localeId);
  }
}
