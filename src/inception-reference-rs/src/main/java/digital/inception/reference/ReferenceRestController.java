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



import digital.inception.rs.RestControllerError;
import digital.inception.rs.SecureRestController;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import javax.validation.Validator;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;



/**
 * The <b>ReferenceRestController</b> class.
 *
 * @author Marcus Portmann
 */
@Tag(name = "Reference API")
@RestController
@RequestMapping(value = "/api/reference")
@CrossOrigin
@SuppressWarnings({"unused", "WeakerAccess"})
public class ReferenceRestController extends SecureRestController {

  /** The Reference Service. */
  private final IReferenceService referenceService;

  /** The JSR-303 validator. */
  private final Validator validator;

  /**
   * Constructs a new <b>ReferenceRestController</b>.
   *
   * @param referenceService the Reference Service
   * @param validator the JSR-303 validator
   */
  public ReferenceRestController(IReferenceService referenceService, Validator validator) {
    this.referenceService = referenceService;
    this.validator = validator;
  }

  /**
   * Retrieve the contact mechanism purposes.
   *
   * @param localeId the Unicode locale identifier for the locale to retrieve the contact mechanism
   *     purposes for or <b>null</b> to retrieve the contact mechanism purposes for all
   *     locales
   * @return the contact mechanism purposes
   */
  @Operation(
      summary = "Retrieve the contact mechanism purposes",
      description = "Retrieve the contact mechanism purposes")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "200", description = "OK"),
        @ApiResponse(
            responseCode = "500",
            description =
                "An error has occurred and the request could not be processed at this time",
            content =
                @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = RestControllerError.class)))
      })
  @RequestMapping(
      value = "/contact-mechanism-purposes",
      method = RequestMethod.GET,
      produces = "application/json")
  @ResponseStatus(HttpStatus.OK)
  // @PreAuthorize("isAuthenticated()")
  public List<ContactMechanismPurpose> getContactMechanismPurposes(
      @Parameter(
              name = "localeId",
              description =
                  "The optional Unicode locale identifier for the locale to retrieve the contact mechanism purposes for",
              example = "en-US")
          @RequestParam(value = "localeId", required = false)
          String localeId)
      throws ReferenceServiceException {
    return referenceService.getContactMechanismPurposes(localeId);
  }

  /**
   * Retrieve the contact mechanism types.
   *
   * @param localeId the Unicode locale identifier for the locale to retrieve the contact mechanism
   *     types for or <b>null</b> to retrieve the contact mechanism types for all locales
   * @return the contact mechanism types
   */
  @Operation(
      summary = "Retrieve the contact mechanism types",
      description = "Retrieve the contact mechanism types")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "200", description = "OK"),
        @ApiResponse(
            responseCode = "500",
            description =
                "An error has occurred and the request could not be processed at this time",
            content =
                @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = RestControllerError.class)))
      })
  @RequestMapping(
      value = "/contact-mechanism-types",
      method = RequestMethod.GET,
      produces = "application/json")
  @ResponseStatus(HttpStatus.OK)
  // @PreAuthorize("isAuthenticated()")
  public List<ContactMechanismType> getContactMechanismTypes(
      @Parameter(
              name = "localeId",
              description =
                  "The optional Unicode locale identifier for the locale to retrieve the contact mechanism types for",
              example = "en-US")
          @RequestParam(value = "localeId", required = false)
          String localeId)
      throws ReferenceServiceException {
    return referenceService.getContactMechanismTypes(localeId);
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
                    mediaType = "application/json",
                    schema = @Schema(implementation = RestControllerError.class)))
      })
  @RequestMapping(value = "/countries", method = RequestMethod.GET, produces = "application/json")
  @ResponseStatus(HttpStatus.OK)
  // @PreAuthorize("isAuthenticated()")
  public List<Country> getCountries(
      @Parameter(
              name = "localeId",
              description =
                  "The optional Unicode locale identifier for the locale to retrieve the countries for",
              example = "en-US")
          @RequestParam(value = "localeId", required = false)
          String localeId)
      throws ReferenceServiceException {
    return referenceService.getCountries(localeId);
  }

  /**
   * Retrieve the employment statuses.
   *
   * @param localeId the Unicode locale identifier for the locale to retrieve the employment
   *     statuses for or <b>null</b> to retrieve the employment statuses for all locales
   * @return the employment statuses
   */
  @Operation(
      summary = "Retrieve the employment statuses",
      description = "Retrieve the employment statuses")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "200", description = "OK"),
        @ApiResponse(
            responseCode = "500",
            description =
                "An error has occurred and the request could not be processed at this time",
            content =
                @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = RestControllerError.class)))
      })
  @RequestMapping(
      value = "/employment-statuses",
      method = RequestMethod.GET,
      produces = "application/json")
  @ResponseStatus(HttpStatus.OK)
  // @PreAuthorize("isAuthenticated()")
  public List<EmploymentStatus> getEmploymentStatuses(
      @Parameter(
              name = "localeId",
              description =
                  "The optional Unicode locale identifier for the locale to retrieve the employment statuses for",
              example = "en-US")
          @RequestParam(value = "localeId", required = false)
          String localeId)
      throws ReferenceServiceException {
    return referenceService.getEmploymentStatuses(localeId);
  }

  /**
   * Retrieve the employment types.
   *
   * @param localeId the Unicode locale identifier for the locale to retrieve the employment types
   *     for or <b>null</b> to retrieve the employment types for all locales
   * @return the employment types
   */
  @Operation(
      summary = "Retrieve the employment types",
      description = "Retrieve the employment types")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "200", description = "OK"),
        @ApiResponse(
            responseCode = "500",
            description =
                "An error has occurred and the request could not be processed at this time",
            content =
                @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = RestControllerError.class)))
      })
  @RequestMapping(
      value = "/employment-types",
      method = RequestMethod.GET,
      produces = "application/json")
  @ResponseStatus(HttpStatus.OK)
  // @PreAuthorize("isAuthenticated()")
  public List<EmploymentType> getEmploymentTypes(
      @Parameter(
              name = "localeId",
              description =
                  "The optional Unicode locale identifier for the locale to retrieve the employment types for",
              example = "en-US")
          @RequestParam(value = "localeId", required = false)
          String localeId)
      throws ReferenceServiceException {
    return referenceService.getEmploymentTypes(localeId);
  }

  /**
   * Retrieve the genders.
   *
   * @param localeId the Unicode locale identifier for the locale to retrieve the genders for or
   *     <b>null</b> to retrieve the genders for all locales
   * @return the genders
   */
  @Operation(summary = "Retrieve the genders", description = "Retrieve the genders")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "200", description = "OK"),
        @ApiResponse(
            responseCode = "500",
            description =
                "An error has occurred and the request could not be processed at this time",
            content =
                @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = RestControllerError.class)))
      })
  @RequestMapping(value = "/genders", method = RequestMethod.GET, produces = "application/json")
  @ResponseStatus(HttpStatus.OK)
  // @PreAuthorize("isAuthenticated()")
  public List<Gender> getGenders(
      @Parameter(
              name = "localeId",
              description =
                  "The optional Unicode locale identifier for the locale to retrieve the genders for",
              example = "en-US")
          @RequestParam(value = "localeId", required = false)
          String localeId)
      throws ReferenceServiceException {
    return referenceService.getGenders(localeId);
  }

  /**
   * Retrieve the identity document types.
   *
   * @param localeId the Unicode locale identifier for the locale to retrieve the identity document
   *     types for or <b>null</b> to retrieve the identity document types for all locales
   * @return the identity document types
   */
  @Operation(
      summary = "Retrieve the identity document types",
      description = "Retrieve the identity document types")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "200", description = "OK"),
        @ApiResponse(
            responseCode = "500",
            description =
                "An error has occurred and the request could not be processed at this time",
            content =
                @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = RestControllerError.class)))
      })
  @RequestMapping(
      value = "/identity-document-types",
      method = RequestMethod.GET,
      produces = "application/json")
  @ResponseStatus(HttpStatus.OK)
  // @PreAuthorize("isAuthenticated()")
  public List<IdentityDocumentType> getIdentityDocumentTypes(
      @Parameter(
              name = "localeId",
              description =
                  "The optional Unicode locale identifier for the locale to retrieve the identity document types for",
              example = "en-US")
          @RequestParam(value = "localeId", required = false)
          String localeId)
      throws ReferenceServiceException {
    return referenceService.getIdentityDocumentTypes(localeId);
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
                    mediaType = "application/json",
                    schema = @Schema(implementation = RestControllerError.class)))
      })
  @RequestMapping(value = "/languages", method = RequestMethod.GET, produces = "application/json")
  @ResponseStatus(HttpStatus.OK)
  // @PreAuthorize("isAuthenticated()")
  public List<Language> getLanguages(
      @Parameter(
              name = "localeId",
              description =
                  "The optional Unicode locale identifier for the locale to retrieve the languages for",
              example = "en-US")
          @RequestParam(value = "localeId", required = false)
          String localeId)
      throws ReferenceServiceException {
    return referenceService.getLanguages(localeId);
  }

  /**
   * Retrieve the marital statuses.
   *
   * @param localeId the Unicode locale identifier for the locale to retrieve the marital statuses
   *     for or <b>null</b> to retrieve the marital statuses for all locales
   * @return the marital statuses
   */
  @Operation(
      summary = "Retrieve the marital statuses",
      description = "Retrieve the marital statuses")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "200", description = "OK"),
        @ApiResponse(
            responseCode = "500",
            description =
                "An error has occurred and the request could not be processed at this time",
            content =
                @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = RestControllerError.class)))
      })
  @RequestMapping(
      value = "/marital-statuses",
      method = RequestMethod.GET,
      produces = "application/json")
  @ResponseStatus(HttpStatus.OK)
  // @PreAuthorize("isAuthenticated()")
  public List<MaritalStatus> getMaritalStatuses(
      @Parameter(
              name = "localeId",
              description =
                  "The optional Unicode locale identifier for the locale to retrieve the marital statuses for",
              example = "en-US")
          @RequestParam(value = "localeId", required = false)
          String localeId)
      throws ReferenceServiceException {
    return referenceService.getMaritalStatuses(localeId);
  }

  /**
   * Retrieve the marriage types.
   *
   * @param localeId the Unicode locale identifier for the locale to retrieve the marriage types for
   *     or <b>null</b> to retrieve the marriage types for all locales
   * @return the marriage types
   */
  @Operation(summary = "Retrieve the marriage types", description = "Retrieve the marriage types")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "200", description = "OK"),
        @ApiResponse(
            responseCode = "500",
            description =
                "An error has occurred and the request could not be processed at this time",
            content =
                @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = RestControllerError.class)))
      })
  @RequestMapping(
      value = "/marriage-types",
      method = RequestMethod.GET,
      produces = "application/json")
  @ResponseStatus(HttpStatus.OK)
  // @PreAuthorize("isAuthenticated()")
  public List<MarriageType> getMarriageTypes(
      @Parameter(
              name = "localeId",
              description =
                  "The optional Unicode locale identifier for the locale to retrieve the marriage types for",
              example = "en-US")
          @RequestParam(value = "localeId", required = false)
          String localeId)
      throws ReferenceServiceException {
    return referenceService.getMarriageTypes(localeId);
  }

  /**
   * Retrieve the next of kin types.
   *
   * @param localeId the Unicode locale identifier for the locale to retrieve the next of kin types
   *     for or <b>null</b> to retrieve the next of kin types for all locales
   * @return the next of kin types
   */
  @Operation(
      summary = "Retrieve the next of kin types",
      description = "Retrieve the next of kin types")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "200", description = "OK"),
        @ApiResponse(
            responseCode = "500",
            description =
                "An error has occurred and the request could not be processed at this time",
            content =
                @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = RestControllerError.class)))
      })
  @RequestMapping(
      value = "/next-of-kin-types",
      method = RequestMethod.GET,
      produces = "application/json")
  @ResponseStatus(HttpStatus.OK)
  // @PreAuthorize("isAuthenticated()")
  public List<NextOfKinType> getNextOfKinTypes(
      @Parameter(
              name = "localeId",
              description =
                  "The optional Unicode locale identifier for the locale to retrieve the next of kin types for",
              example = "en-US")
          @RequestParam(value = "localeId", required = false)
          String localeId)
      throws ReferenceServiceException {
    return referenceService.getNextOfKinTypes(localeId);
  }

  /**
   * Retrieve the occupations.
   *
   * @param localeId the Unicode locale identifier for the locale to retrieve the occupations for or
   *     <b>null</b> to retrieve the occupations for all locales
   * @return the occupations
   */
  @Operation(summary = "Retrieve the occupations", description = "Retrieve the occupations")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "200", description = "OK"),
        @ApiResponse(
            responseCode = "500",
            description =
                "An error has occurred and the request could not be processed at this time",
            content =
                @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = RestControllerError.class)))
      })
  @RequestMapping(value = "/occupations", method = RequestMethod.GET, produces = "application/json")
  @ResponseStatus(HttpStatus.OK)
  // @PreAuthorize("isAuthenticated()")
  public List<Occupation> getOccupations(
      @Parameter(
              name = "localeId",
              description =
                  "The optional Unicode locale identifier for the locale to retrieve the occupations for",
              example = "en-US")
          @RequestParam(value = "localeId", required = false)
          String localeId)
      throws ReferenceServiceException {
    return referenceService.getOccupations(localeId);
  }

  /**
   * Retrieve the party role purposes.
   *
   * @param localeId the Unicode locale identifier for the locale to retrieve the party role
   *     purposes for or <b>null</b> to retrieve the party role purposes for all locales
   * @return the party role purposes
   */
  @Operation(
      summary = "Retrieve the party role purposes",
      description = "Retrieve the party role purposes")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "200", description = "OK"),
        @ApiResponse(
            responseCode = "500",
            description =
                "An error has occurred and the request could not be processed at this time",
            content =
                @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = RestControllerError.class)))
      })
  @RequestMapping(
      value = "/party-role-purposes",
      method = RequestMethod.GET,
      produces = "application/json")
  @ResponseStatus(HttpStatus.OK)
  // @PreAuthorize("isAuthenticated()")
  public List<PartyRolePurpose> getPartyRolePurposes(
      @Parameter(
              name = "localeId",
              description =
                  "The optional Unicode locale identifier for the locale to retrieve the party role purposes for",
              example = "en-US")
          @RequestParam(value = "localeId", required = false)
          String localeId)
      throws ReferenceServiceException {
    return referenceService.getPartyRolePurposes(localeId);
  }

  /**
   * Retrieve the party role types.
   *
   * @param localeId the Unicode locale identifier for the locale to retrieve the party role types
   *     for or <b>null</b> to retrieve the party role types for all locales
   * @return the party role types
   */
  @Operation(
      summary = "Retrieve the party role types",
      description = "Retrieve the party role types")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "200", description = "OK"),
        @ApiResponse(
            responseCode = "500",
            description =
                "An error has occurred and the request could not be processed at this time",
            content =
                @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = RestControllerError.class)))
      })
  @RequestMapping(
      value = "/party-role-types",
      method = RequestMethod.GET,
      produces = "application/json")
  @ResponseStatus(HttpStatus.OK)
  // @PreAuthorize("isAuthenticated()")
  public List<PartyRoleType> getPartyRoleTypes(
      @Parameter(
              name = "localeId",
              description =
                  "The optional Unicode locale identifier for the locale to retrieve the party role types for",
              example = "en-US")
          @RequestParam(value = "localeId", required = false)
          String localeId)
      throws ReferenceServiceException {
    return referenceService.getPartyRoleTypes(localeId);
  }

  /**
   * Retrieve the physical address purposes.
   *
   * @param localeId the Unicode locale identifier for the locale to retrieve the contact mechanism
   *     purposes for or <b>null</b> to retrieve the physical address purposes for all locales
   * @return the physical address purposes
   */
  @Operation(
      summary = "Retrieve the physical address purposes",
      description = "Retrieve the physical address purposes")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "200", description = "OK"),
        @ApiResponse(
            responseCode = "500",
            description =
                "An error has occurred and the request could not be processed at this time",
            content =
                @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = RestControllerError.class)))
      })
  @RequestMapping(
      value = "/physical-address-purposes",
      method = RequestMethod.GET,
      produces = "application/json")
  @ResponseStatus(HttpStatus.OK)
  // @PreAuthorize("isAuthenticated()")
  public List<PhysicalAddressPurpose> getPhysicalAddressPurposes(
      @Parameter(
              name = "localeId",
              description =
                  "The optional Unicode locale identifier for the locale to retrieve the physical address purposes for",
              example = "en-US")
          @RequestParam(value = "localeId", required = false)
          String localeId)
      throws ReferenceServiceException {
    return referenceService.getPhysicalAddressPurposes(localeId);
  }

  /**
   * Retrieve the physical address types.
   *
   * @param localeId the Unicode locale identifier for the locale to retrieve the contact mechanism
   *     types for or <b>null</b> to retrieve the physical address types for all locales
   * @return the physical address types
   */
  @Operation(
      summary = "Retrieve the physical address types",
      description = "Retrieve the physical address types")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "200", description = "OK"),
        @ApiResponse(
            responseCode = "500",
            description =
                "An error has occurred and the request could not be processed at this time",
            content =
                @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = RestControllerError.class)))
      })
  @RequestMapping(
      value = "/physical-address-types",
      method = RequestMethod.GET,
      produces = "application/json")
  @ResponseStatus(HttpStatus.OK)
  // @PreAuthorize("isAuthenticated()")
  public List<PhysicalAddressType> getPhysicalAddressTypes(
      @Parameter(
              name = "localeId",
              description =
                  "The optional Unicode locale identifier for the locale to retrieve the physical address types for",
              example = "en-US")
          @RequestParam(value = "localeId", required = false)
          String localeId)
      throws ReferenceServiceException {
    return referenceService.getPhysicalAddressTypes(localeId);
  }

  /**
   * Retrieve the preference type categories.
   *
   * @param localeId the Unicode locale identifier for the locale to retrieve the preference type
   *     categories for or <b>null</b> to retrieve the preference type categories for all
   *     locales
   * @return the preference type categories
   */
  @Operation(
      summary = "Retrieve the preference type categories",
      description = "Retrieve the preference type categories")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "200", description = "OK"),
        @ApiResponse(
            responseCode = "500",
            description =
                "An error has occurred and the request could not be processed at this time",
            content =
                @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = RestControllerError.class)))
      })
  @RequestMapping(
      value = "/preference-type-categories",
      method = RequestMethod.GET,
      produces = "application/json")
  @ResponseStatus(HttpStatus.OK)
  // @PreAuthorize("isAuthenticated()")
  public List<PreferenceTypeCategory> getPreferenceTypeCategories(
      @Parameter(
              name = "localeId",
              description =
                  "The optional Unicode locale identifier for the locale to retrieve the preference type categories for",
              example = "en-US")
          @RequestParam(value = "localeId", required = false)
          String localeId)
      throws ReferenceServiceException {
    return referenceService.getPreferenceTypeCategories(localeId);
  }

  /**
   * Retrieve the preference types.
   *
   * @param localeId the Unicode locale identifier for the locale to retrieve the preference types
   *     for or <b>null</b> to retrieve the preference types for all locales
   * @return the preference types
   */
  @Operation(
      summary = "Retrieve the preference types",
      description = "Retrieve the preference types")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "200", description = "OK"),
        @ApiResponse(
            responseCode = "500",
            description =
                "An error has occurred and the request could not be processed at this time",
            content =
                @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = RestControllerError.class)))
      })
  @RequestMapping(
      value = "/preference-types",
      method = RequestMethod.GET,
      produces = "application/json")
  @ResponseStatus(HttpStatus.OK)
  // @PreAuthorize("isAuthenticated()")
  public List<PreferenceType> getPreferenceTypes(
      @Parameter(
              name = "localeId",
              description =
                  "The optional Unicode locale identifier for the locale to retrieve the preference types for",
              example = "en-US")
          @RequestParam(value = "localeId", required = false)
          String localeId)
      throws ReferenceServiceException {
    return referenceService.getPreferenceTypes(localeId);
  }

  /**
   * Retrieve the races.
   *
   * @param localeId the Unicode locale identifier for the locale to retrieve the races for or
   *     <b>null</b> to retrieve the races for all locales
   * @return the races
   */
  @Operation(summary = "Retrieve the races", description = "Retrieve the races")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "200", description = "OK"),
        @ApiResponse(
            responseCode = "500",
            description =
                "An error has occurred and the request could not be processed at this time",
            content =
                @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = RestControllerError.class)))
      })
  @RequestMapping(value = "/races", method = RequestMethod.GET, produces = "application/json")
  @ResponseStatus(HttpStatus.OK)
  // @PreAuthorize("isAuthenticated()")
  public List<Race> getRaces(
      @Parameter(
              name = "localeId",
              description =
                  "The optional Unicode locale identifier for the locale to retrieve the races for",
              example = "en-US")
          @RequestParam(value = "localeId", required = false)
          String localeId)
      throws ReferenceServiceException {
    return referenceService.getRaces(localeId);
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
                    mediaType = "application/json",
                    schema = @Schema(implementation = RestControllerError.class)))
      })
  @RequestMapping(value = "/regions", method = RequestMethod.GET, produces = "application/json")
  @ResponseStatus(HttpStatus.OK)
  // @PreAuthorize("isAuthenticated()")
  public List<Region> getRegions(
      @Parameter(
              name = "localeId",
              description =
                  "The optional Unicode locale identifier for the locale to retrieve the regions for",
              example = "en-US")
          @RequestParam(value = "localeId", required = false)
          String localeId)
      throws ReferenceServiceException {
    return referenceService.getRegions(localeId);
  }

  /**
   * Retrieve the residence permit types.
   *
   * @param localeId the Unicode locale identifier for the locale to retrieve the residence permit
   *     types for or <b>null</b> to retrieve the residence permit types for all locales
   * @return the residence permit types
   */
  @Operation(
      summary = "Retrieve the residence permit types",
      description = "Retrieve the residence permit types")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "200", description = "OK"),
        @ApiResponse(
            responseCode = "500",
            description =
                "An error has occurred and the request could not be processed at this time",
            content =
                @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = RestControllerError.class)))
      })
  @RequestMapping(
      value = "/residence-permit-types",
      method = RequestMethod.GET,
      produces = "application/json")
  @ResponseStatus(HttpStatus.OK)
  // @PreAuthorize("isAuthenticated()")
  public List<ResidencePermitType> getResidencePermitTypes(
      @Parameter(
              name = "localeId",
              description =
                  "The optional Unicode locale identifier for the locale to retrieve the residence permit types for",
              example = "en-US")
          @RequestParam(value = "localeId", required = false)
          String localeId)
      throws ReferenceServiceException {
    return referenceService.getResidencePermitTypes(localeId);
  }

  /**
   * Retrieve the residency statuses.
   *
   * @param localeId the Unicode locale identifier for the locale to retrieve the residency statuses
   *     for or <b>null</b> to retrieve the residency statuses for all locales
   * @return the residency statuses
   */
  @Operation(
      summary = "Retrieve the residency statuses",
      description = "Retrieve the residency statuses")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "200", description = "OK"),
        @ApiResponse(
            responseCode = "500",
            description =
                "An error has occurred and the request could not be processed at this time",
            content =
                @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = RestControllerError.class)))
      })
  @RequestMapping(
      value = "/residency-statuses",
      method = RequestMethod.GET,
      produces = "application/json")
  @ResponseStatus(HttpStatus.OK)
  // @PreAuthorize("isAuthenticated()")
  public List<ResidencyStatus> getResidencyStatuses(
      @Parameter(
              name = "localeId",
              description =
                  "The optional Unicode locale identifier for the locale to retrieve the residency statuses for",
              example = "en-US")
          @RequestParam(value = "localeId", required = false)
          String localeId)
      throws ReferenceServiceException {
    return referenceService.getResidencyStatuses(localeId);
  }

  /**
   * Retrieve the residential types.
   *
   * @param localeId the Unicode locale identifier for the locale to retrieve the residential types
   *     for or <b>null</b> to retrieve the residential types for all locales
   * @return the residential types
   */
  @Operation(
      summary = "Retrieve the residential types",
      description = "Retrieve the residential types")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "200", description = "OK"),
        @ApiResponse(
            responseCode = "500",
            description =
                "An error has occurred and the request could not be processed at this time",
            content =
                @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = RestControllerError.class)))
      })
  @RequestMapping(
      value = "/residential-types",
      method = RequestMethod.GET,
      produces = "application/json")
  @ResponseStatus(HttpStatus.OK)
  // @PreAuthorize("isAuthenticated()")
  public List<ResidentialType> getResidentialTypes(
      @Parameter(
              name = "localeId",
              description =
                  "The optional Unicode locale identifier for the locale to retrieve the residential types for",
              example = "en-US")
          @RequestParam(value = "localeId", required = false)
          String localeId)
      throws ReferenceServiceException {
    return referenceService.getResidentialTypes(localeId);
  }

  /**
   * Retrieve the sources of funds.
   *
   * @param localeId the Unicode locale identifier for the locale to retrieve the sources of funds
   *     for or <b>null</b> to retrieve the sources of funds for all locales
   * @return the sources of funds
   */
  @Operation(
      summary = "Retrieve the sources of funds",
      description = "Retrieve the sources of funds")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "200", description = "OK"),
        @ApiResponse(
            responseCode = "500",
            description =
                "An error has occurred and the request could not be processed at this time",
            content =
                @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = RestControllerError.class)))
      })
  @RequestMapping(
      value = "/sources-of-funds",
      method = RequestMethod.GET,
      produces = "application/json")
  @ResponseStatus(HttpStatus.OK)
  // @PreAuthorize("isAuthenticated()")
  public List<SourceOfFunds> getSourcesOfFunds(
      @Parameter(
              name = "localeId",
              description =
                  "The optional Unicode locale identifier for the locale to retrieve the sources of funds for",
              example = "en-US")
          @RequestParam(value = "localeId", required = false)
          String localeId)
      throws ReferenceServiceException {
    return referenceService.getSourcesOfFunds(localeId);
  }

  /**
   * Retrieve the tax number types.
   *
   * @param localeId the Unicode locale identifier for the locale to retrieve the tax number types
   *     for or <b>null</b> to retrieve the tax number types for all locales
   * @return the tax number types
   */
  @Operation(
      summary = "Retrieve the tax number types",
      description = "Retrieve the tax number types")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "200", description = "OK"),
        @ApiResponse(
            responseCode = "500",
            description =
                "An error has occurred and the request could not be processed at this time",
            content =
                @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = RestControllerError.class)))
      })
  @RequestMapping(
      value = "/tax-number-types",
      method = RequestMethod.GET,
      produces = "application/json")
  @ResponseStatus(HttpStatus.OK)
  // @PreAuthorize("isAuthenticated()")
  public List<TaxNumberType> getTaxNumberTypes(
      @Parameter(
              name = "localeId",
              description =
                  "The optional Unicode locale identifier for the locale to retrieve the tax number types for",
              example = "en-US")
          @RequestParam(value = "localeId", required = false)
          String localeId)
      throws ReferenceServiceException {
    return referenceService.getTaxNumberTypes(localeId);
  }

  /**
   * Retrieve the times to contact.
   *
   * @param localeId the Unicode locale identifier for the locale to retrieve the times to contact
   *     for or <b>null</b> to retrieve the times to contact for all locales
   * @return the times to contact
   */
  @Operation(
      summary = "Retrieve the times to contact",
      description = "Retrieve the times to contact")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "200", description = "OK"),
        @ApiResponse(
            responseCode = "500",
            description =
                "An error has occurred and the request could not be processed at this time",
            content =
                @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = RestControllerError.class)))
      })
  @RequestMapping(
      value = "/times-to-contact",
      method = RequestMethod.GET,
      produces = "application/json")
  @ResponseStatus(HttpStatus.OK)
  // @PreAuthorize("isAuthenticated()")
  public List<TimeToContact> getTimesToContact(
      @Parameter(
              name = "localeId",
              description =
                  "The optional Unicode locale identifier for the locale to retrieve the times to contact for",
              example = "en-US")
          @RequestParam(value = "localeId", required = false)
          String localeId)
      throws ReferenceServiceException {
    return referenceService.getTimesToContact(localeId);
  }

  /**
   * Retrieve the titles.
   *
   * @param localeId the Unicode locale identifier for the locale to retrieve the titles for or
   *     <b>null</b> to retrieve the titles for all locales
   * @return the titles
   */
  @Operation(summary = "Retrieve the titles", description = "Retrieve the titles")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "200", description = "OK"),
        @ApiResponse(
            responseCode = "500",
            description =
                "An error has occurred and the request could not be processed at this time",
            content =
                @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = RestControllerError.class)))
      })
  @RequestMapping(value = "/titles", method = RequestMethod.GET, produces = "application/json")
  @ResponseStatus(HttpStatus.OK)
  // @PreAuthorize("isAuthenticated()")
  public List<Title> getTitles(
      @Parameter(
              name = "localeId",
              description =
                  "The optional Unicode locale identifier for the locale to retrieve the titles for",
              example = "en-US")
          @RequestParam(value = "localeId", required = false)
          String localeId)
      throws ReferenceServiceException {
    return referenceService.getTitles(localeId);
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
                    mediaType = "application/json",
                    schema = @Schema(implementation = RestControllerError.class)))
      })
  @RequestMapping(
      value = "/verification-methods",
      method = RequestMethod.GET,
      produces = "application/json")
  @ResponseStatus(HttpStatus.OK)
  // @PreAuthorize("isAuthenticated()")
  public List<VerificationMethod> getVerificationMethods(
      @Parameter(
              name = "localeId",
              description =
                  "The optional Unicode locale identifier for the locale to retrieve the verification methods for",
              example = "en-US")
          @RequestParam(value = "localeId", required = false)
          String localeId)
      throws ReferenceServiceException {
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
                    mediaType = "application/json",
                    schema = @Schema(implementation = RestControllerError.class)))
      })
  @RequestMapping(
      value = "/verification-statuses",
      method = RequestMethod.GET,
      produces = "application/json")
  @ResponseStatus(HttpStatus.OK)
  // @PreAuthorize("isAuthenticated()")
  public List<VerificationStatus> getVerificationStatuses(
      @Parameter(
              name = "localeId",
              description =
                  "The optional Unicode locale identifier for the locale to retrieve the verification statuses for",
              example = "en-US")
          @RequestParam(value = "localeId", required = false)
          String localeId)
      throws ReferenceServiceException {
    return referenceService.getVerificationStatuses(localeId);
  }
}
