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
import org.springframework.util.StringUtils;
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
              example = ReferenceService.DEFAULT_LOCALE_ID)
          @RequestParam(value = "localeId", required = false)
          String localeId)
      throws ServiceUnavailableException {
    return referenceService.getCountries(
        StringUtils.hasText(localeId) ? localeId : ReferenceService.DEFAULT_LOCALE_ID);
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
              example = ReferenceService.DEFAULT_LOCALE_ID)
          @RequestParam(value = "localeId", required = false)
          String localeId)
      throws ServiceUnavailableException {
    return referenceService.getLanguages(
        StringUtils.hasText(localeId) ? localeId : ReferenceService.DEFAULT_LOCALE_ID);
  }

  /**
   * Retrieve the measurement systems.
   *
   * @param localeId the Unicode locale identifier for the locale to retrieve the measurement
   *     systems for or <b>null</b> to retrieve the measurement systems for all locales
   * @return the measurement systems
   */
  @Operation(
      summary = "Retrieve the measurement systems",
      description = "Retrieve the measurement systems")
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
      value = "/measurement-systems",
      method = RequestMethod.GET,
      produces = "application/json")
  @ResponseStatus(HttpStatus.OK)
  @PreAuthorize("isSecurityDisabled() or isAuthenticated()")
  public List<MeasurementSystem> getMeasurementSystems(
      @Parameter(
              name = "localeId",
              description =
                  "The optional Unicode locale identifier for the locale to retrieve the measurement systems for",
              example = ReferenceService.DEFAULT_LOCALE_ID)
          @RequestParam(value = "localeId", required = false)
          String localeId)
      throws ServiceUnavailableException {
    return referenceService.getMeasurementSystems(
        StringUtils.hasText(localeId) ? localeId : ReferenceService.DEFAULT_LOCALE_ID);
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
              example = ReferenceService.DEFAULT_LOCALE_ID)
          @RequestParam(value = "localeId", required = false)
          String localeId)
      throws ServiceUnavailableException {
    return referenceService.getRegions(
        StringUtils.hasText(
                StringUtils.hasText(localeId) ? localeId : ReferenceService.DEFAULT_LOCALE_ID)
            ? localeId
            : ReferenceService.DEFAULT_LOCALE_ID);
  }

  /**
   * Retrieve the time zones.
   *
   * @param localeId the Unicode locale identifier for the locale to retrieve the time zone for or
   *     <b>null</b> to retrieve the time zones for all locales
   * @return the time zones
   */
  @Operation(summary = "Retrieve the time zones", description = "Retrieve the time zones")
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
      value = "/time-zones",
      method = RequestMethod.GET,
      produces = "application/json")
  @ResponseStatus(HttpStatus.OK)
  @PreAuthorize("isSecurityDisabled() or isAuthenticated()")
  public List<TimeZone> getTimeZones(
      @Parameter(
              name = "localeId",
              description =
                  "The optional Unicode locale identifier for the locale to retrieve the time zones for",
              example = ReferenceService.DEFAULT_LOCALE_ID)
          @RequestParam(value = "localeId", required = false)
          String localeId)
      throws ServiceUnavailableException {
    return referenceService.getTimeZones(
        StringUtils.hasText(localeId) ? localeId : ReferenceService.DEFAULT_LOCALE_ID);
  }
}
