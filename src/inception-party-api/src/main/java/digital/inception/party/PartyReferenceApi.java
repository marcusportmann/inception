/*
 * Copyright 2022 Marcus Portmann
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

package digital.inception.party;

import digital.inception.api.ProblemDetails;
import digital.inception.api.SecureApi;
import digital.inception.core.service.InvalidArgumentException;
import digital.inception.core.service.ServiceUnavailableException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import java.util.UUID;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 * The <b>PartyReferenceApi</b> class.
 *
 * @author Marcus Portmann
 */
@Tag(name = "Party Reference")
@RestController
@RequestMapping(value = "/api/party/reference")
@CrossOrigin
@SuppressWarnings({"unused", "WeakerAccess"})
// @el (isSecurityDisabled: digital.inception.api.ApiSecurityExpressionRoot.isSecurityEnabled)
public class PartyReferenceApi extends SecureApi {

  /** The Party Reference Service. */
  private final IPartyReferenceService partyReferenceService;

  /**
   * Constructs a new <b>PartyReferenceRestController</b>.
   *
   * @param applicationContext the Spring application context
   * @param partyReferenceService the Party Reference Service
   */
  public PartyReferenceApi(
      ApplicationContext applicationContext, IPartyReferenceService partyReferenceService) {
    super(applicationContext);

    this.partyReferenceService = partyReferenceService;
  }

  /**
   * Retrieve the association property type reference data for a specific locale.
   *
   * @param tenantId the ID for the tenant the association property type reference data is specific
   *     to
   * @param localeId the Unicode locale identifier for the locale to retrieve the association
   *     property type reference data for
   * @return the association property type reference data
   * @throws InvalidArgumentException if an argument is invalid
   * @throws ServiceUnavailableException if the association property type reference data could not
   *     be retrieved
   */
  @Operation(
      summary = "Retrieve the association property type reference data for a specific locale",
      description = "Retrieve the association property type reference data for a specific locale")
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
            responseCode = "500",
            description =
                "An error has occurred and the request could not be processed at this time",
            content =
                @Content(
                    mediaType = "application/problem+json",
                    schema = @Schema(implementation = ProblemDetails.class)))
      })
  @RequestMapping(
      value = "/association-property-types",
      method = RequestMethod.GET,
      produces = "application/json")
  @ResponseStatus(HttpStatus.OK)
  @PreAuthorize("isSecurityDisabled() or isAuthenticated()")
  public List<AssociationPropertyType> getAssociationPropertyTypes(
      @Parameter(
              name = "Tenant-ID",
              description =
                  "The ID for the tenant the association property type reference data is specific to",
              example = "00000000-0000-0000-0000-000000000000")
          @RequestHeader(
              name = "Tenant-ID",
              defaultValue = "00000000-0000-0000-0000-000000000000",
              required = false)
          UUID tenantId,
      @Parameter(
              name = "localeId",
              description =
                  "The Unicode locale identifier for the locale to retrieve the association property type reference data for",
              example = IPartyReferenceService.DEFAULT_LOCALE_ID)
          @RequestParam(
              value = "localeId",
              required = false,
              defaultValue = IPartyReferenceService.DEFAULT_LOCALE_ID)
          String localeId)
      throws InvalidArgumentException, ServiceUnavailableException {
    return partyReferenceService.getAssociationPropertyTypes(tenantId, localeId);
  }

  /**
   * Retrieve the association type reference data for a specific locale.
   *
   * @param tenantId the ID for the tenant the association type reference data is specific to
   * @param localeId the Unicode locale identifier for the locale to retrieve the association type
   *     reference data for
   * @return the association type reference data
   * @throws InvalidArgumentException if an argument is invalid
   * @throws ServiceUnavailableException if the association type reference data could not be
   *     retrieved
   */
  @Operation(
      summary = "Retrieve the association type reference data for a specific locale",
      description = "Retrieve the association type reference data for a specific locale")
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
            responseCode = "500",
            description =
                "An error has occurred and the request could not be processed at this time",
            content =
                @Content(
                    mediaType = "application/problem+json",
                    schema = @Schema(implementation = ProblemDetails.class)))
      })
  @RequestMapping(
      value = "/association-types",
      method = RequestMethod.GET,
      produces = "application/json")
  @ResponseStatus(HttpStatus.OK)
  @PreAuthorize("isSecurityDisabled() or isAuthenticated()")
  public List<AssociationType> getAssociationTypes(
      @Parameter(
              name = "Tenant-ID",
              description =
                  "The ID for the tenant the association type reference data is specific to",
              example = "00000000-0000-0000-0000-000000000000")
          @RequestHeader(
              name = "Tenant-ID",
              defaultValue = "00000000-0000-0000-0000-000000000000",
              required = false)
          UUID tenantId,
      @Parameter(
              name = "localeId",
              description =
                  "The Unicode locale identifier for the locale to retrieve the association type reference data for",
              example = IPartyReferenceService.DEFAULT_LOCALE_ID)
          @RequestParam(
              value = "localeId",
              required = false,
              defaultValue = IPartyReferenceService.DEFAULT_LOCALE_ID)
          String localeId)
      throws InvalidArgumentException, ServiceUnavailableException {
    return partyReferenceService.getAssociationTypes(tenantId, localeId);
  }

  /**
   * Retrieve the attribute type category reference data for a specific locale.
   *
   * @param tenantId the ID for the tenant the attribute type category reference data is specific to
   * @param localeId the Unicode locale identifier for the locale to retrieve the attribute type
   *     category reference data for
   * @return the attribute type category reference data
   * @throws InvalidArgumentException if an argument is invalid
   * @throws ServiceUnavailableException if the attribute type category reference data could not be
   *     retrieved
   */
  @Operation(
      summary = "Retrieve the attribute type category reference data for a specific locale",
      description = "Retrieve the attribute type category reference data for a specific locale")
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
            responseCode = "500",
            description =
                "An error has occurred and the request could not be processed at this time",
            content =
                @Content(
                    mediaType = "application/problem+json",
                    schema = @Schema(implementation = ProblemDetails.class)))
      })
  @RequestMapping(
      value = "/attribute-type-categories",
      method = RequestMethod.GET,
      produces = "application/json")
  @ResponseStatus(HttpStatus.OK)
  @PreAuthorize("isSecurityDisabled() or isAuthenticated()")
  public List<AttributeTypeCategory> getAttributeTypeCategories(
      @Parameter(
              name = "Tenant-ID",
              description =
                  "The ID for the tenant the attribute type category reference data is specific to",
              example = "00000000-0000-0000-0000-000000000000")
          @RequestHeader(
              name = "Tenant-ID",
              defaultValue = "00000000-0000-0000-0000-000000000000",
              required = false)
          UUID tenantId,
      @Parameter(
              name = "localeId",
              description =
                  "The Unicode locale identifier for the locale to retrieve the attribute type category reference data for",
              example = IPartyReferenceService.DEFAULT_LOCALE_ID)
          @RequestParam(
              value = "localeId",
              required = false,
              defaultValue = IPartyReferenceService.DEFAULT_LOCALE_ID)
          String localeId)
      throws InvalidArgumentException, ServiceUnavailableException {
    return partyReferenceService.getAttributeTypeCategories(tenantId, localeId);
  }

  /**
   * Retrieve the attribute type reference data for a specific locale.
   *
   * @param tenantId the ID for the tenant the attribute type reference data is specific to
   * @param localeId the Unicode locale identifier for the locale to retrieve the attribute type
   *     reference data for
   * @return the attribute type reference data
   * @throws InvalidArgumentException if an argument is invalid
   * @throws ServiceUnavailableException if the attribute type reference data could not be retrieved
   */
  @Operation(
      summary = "Retrieve the attribute type reference data for a specific locale",
      description = "Retrieve the attribute type reference data for a specific locale")
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
            responseCode = "500",
            description =
                "An error has occurred and the request could not be processed at this time",
            content =
                @Content(
                    mediaType = "application/problem+json",
                    schema = @Schema(implementation = ProblemDetails.class)))
      })
  @RequestMapping(
      value = "/attribute-types",
      method = RequestMethod.GET,
      produces = "application/json")
  @ResponseStatus(HttpStatus.OK)
  @PreAuthorize("isSecurityDisabled() or isAuthenticated()")
  public List<AttributeType> getAttributeTypes(
      @Parameter(
              name = "Tenant-ID",
              description =
                  "The ID for the tenant the attribute type reference data is specific to",
              example = "00000000-0000-0000-0000-000000000000")
          @RequestHeader(
              name = "Tenant-ID",
              defaultValue = "00000000-0000-0000-0000-000000000000",
              required = false)
          UUID tenantId,
      @Parameter(
              name = "localeId",
              description =
                  "The Unicode locale identifier for the locale to retrieve the attribute type reference data for",
              example = IPartyReferenceService.DEFAULT_LOCALE_ID)
          @RequestParam(
              value = "localeId",
              required = false,
              defaultValue = IPartyReferenceService.DEFAULT_LOCALE_ID)
          String localeId)
      throws InvalidArgumentException, ServiceUnavailableException {
    return partyReferenceService.getAttributeTypes(tenantId, localeId);
  }

  /**
   * Retrieve the consent type reference data for a specific locale.
   *
   * @param tenantId the ID for the tenant the consent type reference data is specific to
   * @param localeId the Unicode locale identifier for the locale to retrieve the consent type
   *     reference data for
   * @return the consent type reference data
   * @throws InvalidArgumentException if an argument is invalid
   * @throws ServiceUnavailableException if the consent type reference data could not be retrieved
   */
  @Operation(
      summary = "Retrieve the consent type reference data for a specific locale",
      description = "Retrieve the consent type reference data for a specific locale")
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
            responseCode = "500",
            description =
                "An error has occurred and the request could not be processed at this time",
            content =
                @Content(
                    mediaType = "application/problem+json",
                    schema = @Schema(implementation = ProblemDetails.class)))
      })
  @RequestMapping(
      value = "/consent-types",
      method = RequestMethod.GET,
      produces = "application/json")
  @ResponseStatus(HttpStatus.OK)
  @PreAuthorize("isSecurityDisabled() or isAuthenticated()")
  public List<ConsentType> getConsentTypes(
      @Parameter(
              name = "Tenant-ID",
              description = "The ID for the tenant the consent type reference data is specific to",
              example = "00000000-0000-0000-0000-000000000000")
          @RequestHeader(
              name = "Tenant-ID",
              defaultValue = "00000000-0000-0000-0000-000000000000",
              required = false)
          UUID tenantId,
      @Parameter(
              name = "localeId",
              description =
                  "The Unicode locale identifier for the locale to retrieve the consent types reference data for",
              example = IPartyReferenceService.DEFAULT_LOCALE_ID)
          @RequestParam(
              value = "localeId",
              required = false,
              defaultValue = IPartyReferenceService.DEFAULT_LOCALE_ID)
          String localeId)
      throws InvalidArgumentException, ServiceUnavailableException {
    return partyReferenceService.getConsentTypes(tenantId, localeId);
  }

  /**
   * Retrieve the contact mechanism purpose reference data for a specific locale.
   *
   * @param tenantId the ID for the tenant the contact mechanism purpose reference data is specific
   *     to
   * @param localeId the Unicode locale identifier for the locale to retrieve the contact mechanism
   *     purpose reference data for
   * @return the contact mechanism purpose reference data
   * @throws InvalidArgumentException if an argument is invalid
   * @throws ServiceUnavailableException if the contact mechanism purpose reference data could not
   *     be retrieved
   */
  @Operation(
      summary = "Retrieve the contact mechanism purpose reference data for a specific locale",
      description = "Retrieve the contact mechanism purpose reference data for a specific locale")
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
            responseCode = "500",
            description =
                "An error has occurred and the request could not be processed at this time",
            content =
                @Content(
                    mediaType = "application/problem+json",
                    schema = @Schema(implementation = ProblemDetails.class)))
      })
  @RequestMapping(
      value = "/contact-mechanism-purposes",
      method = RequestMethod.GET,
      produces = "application/json")
  @ResponseStatus(HttpStatus.OK)
  @PreAuthorize("isSecurityDisabled() or isAuthenticated()")
  public List<ContactMechanismPurpose> getContactMechanismPurposes(
      @Parameter(
              name = "Tenant-ID",
              description =
                  "The ID for the tenant the contact mechanism purpose reference data is specific to",
              example = "00000000-0000-0000-0000-000000000000")
          @RequestHeader(
              name = "Tenant-ID",
              defaultValue = "00000000-0000-0000-0000-000000000000",
              required = false)
          UUID tenantId,
      @Parameter(
              name = "localeId",
              description =
                  "The Unicode locale identifier for the locale to retrieve the contact mechanism purpose reference data for",
              example = IPartyReferenceService.DEFAULT_LOCALE_ID)
          @RequestParam(
              value = "localeId",
              required = false,
              defaultValue = IPartyReferenceService.DEFAULT_LOCALE_ID)
          String localeId)
      throws InvalidArgumentException, ServiceUnavailableException {
    return partyReferenceService.getContactMechanismPurposes(tenantId, localeId);
  }

  /**
   * Retrieve the contact mechanism role reference data for a specific locale.
   *
   * @param tenantId the ID for the tenant the contact mechanism role reference data is specific to
   * @param localeId the Unicode locale identifier for the locale to retrieve the contact mechanism
   *     role reference data for
   * @return the contact mechanism role reference data
   * @throws InvalidArgumentException if an argument is invalid
   * @throws ServiceUnavailableException if the contact mechanism role reference data could not be
   *     retrieved
   */
  @Operation(
      summary = "Retrieve the contact mechanism role reference data for a specific locale",
      description = "Retrieve the contact mechanism role reference data for a specific locale")
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
            responseCode = "500",
            description =
                "An error has occurred and the request could not be processed at this time",
            content =
                @Content(
                    mediaType = "application/problem+json",
                    schema = @Schema(implementation = ProblemDetails.class)))
      })
  @RequestMapping(
      value = "/contact-mechanism-roles",
      method = RequestMethod.GET,
      produces = "application/json")
  @ResponseStatus(HttpStatus.OK)
  @PreAuthorize("isSecurityDisabled() or isAuthenticated()")
  public List<ContactMechanismRole> getContactMechanismRoles(
      @Parameter(
              name = "Tenant-ID",
              description =
                  "The ID for the tenant the contact mechanism role reference data is specific to",
              example = "00000000-0000-0000-0000-000000000000")
          @RequestHeader(
              name = "Tenant-ID",
              defaultValue = "00000000-0000-0000-0000-000000000000",
              required = false)
          UUID tenantId,
      @Parameter(
              name = "localeId",
              description =
                  "The Unicode locale identifier for the locale to retrieve the contact mechanism role reference data for",
              example = IPartyReferenceService.DEFAULT_LOCALE_ID)
          @RequestParam(
              value = "localeId",
              required = false,
              defaultValue = IPartyReferenceService.DEFAULT_LOCALE_ID)
          String localeId)
      throws InvalidArgumentException, ServiceUnavailableException {
    return partyReferenceService.getContactMechanismRoles(tenantId, localeId);
  }

  /**
   * Retrieve the contact mechanism type reference data for a specific locale.
   *
   * @param tenantId the ID for the tenant the contact mechanism type reference data is specific to
   * @param localeId the Unicode locale identifier for the locale to retrieve the contact mechanism
   *     type reference data for
   * @return the contact mechanism type reference data
   * @throws InvalidArgumentException if an argument is invalid
   * @throws ServiceUnavailableException if the contact mechanism type reference data could not be
   *     retrieved
   */
  @Operation(
      summary = "Retrieve the contact mechanism type reference data for a specific locale",
      description = "Retrieve the contact mechanism type reference data for a specific locale")
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
            responseCode = "500",
            description =
                "An error has occurred and the request could not be processed at this time",
            content =
                @Content(
                    mediaType = "application/problem+json",
                    schema = @Schema(implementation = ProblemDetails.class)))
      })
  @RequestMapping(
      value = "/contact-mechanism-types",
      method = RequestMethod.GET,
      produces = "application/json")
  @ResponseStatus(HttpStatus.OK)
  @PreAuthorize("isSecurityDisabled() or isAuthenticated()")
  public List<ContactMechanismType> getContactMechanismTypes(
      @Parameter(
              name = "Tenant-ID",
              description =
                  "The ID for the tenant the contact mechanism type reference data is specific to",
              example = "00000000-0000-0000-0000-000000000000")
          @RequestHeader(
              name = "Tenant-ID",
              defaultValue = "00000000-0000-0000-0000-000000000000",
              required = false)
          UUID tenantId,
      @Parameter(
              name = "localeId",
              description =
                  "The Unicode locale identifier for the locale to retrieve the contact mechanism type reference data for",
              example = IPartyReferenceService.DEFAULT_LOCALE_ID)
          @RequestParam(
              value = "localeId",
              required = false,
              defaultValue = IPartyReferenceService.DEFAULT_LOCALE_ID)
          String localeId)
      throws InvalidArgumentException, ServiceUnavailableException {
    return partyReferenceService.getContactMechanismTypes(tenantId, localeId);
  }

  /**
   * Retrieve the employment status reference data for a specific locale.
   *
   * @param tenantId the ID for the tenant the employment status reference data is specific to
   * @param localeId the Unicode locale identifier for the locale to retrieve the employment status
   *     reference data for
   * @return the employment status reference data
   * @throws InvalidArgumentException if an argument is invalid
   * @throws ServiceUnavailableException if the employment status reference data could not be
   *     retrieved
   */
  @Operation(
      summary = "Retrieve the employment status reference data for a specific locale",
      description = "Retrieve the employment status reference data for a specific locale")
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
            responseCode = "500",
            description =
                "An error has occurred and the request could not be processed at this time",
            content =
                @Content(
                    mediaType = "application/problem+json",
                    schema = @Schema(implementation = ProblemDetails.class)))
      })
  @RequestMapping(
      value = "/employment-statuses",
      method = RequestMethod.GET,
      produces = "application/json")
  @ResponseStatus(HttpStatus.OK)
  @PreAuthorize("isSecurityDisabled() or isAuthenticated()")
  public List<EmploymentStatus> getEmploymentStatuses(
      @Parameter(
              name = "Tenant-ID",
              description =
                  "The ID for the tenant the employment status reference data is specific to",
              example = "00000000-0000-0000-0000-000000000000")
          @RequestHeader(
              name = "Tenant-ID",
              defaultValue = "00000000-0000-0000-0000-000000000000",
              required = false)
          UUID tenantId,
      @Parameter(
              name = "localeId",
              description =
                  "The Unicode locale identifier for the locale to retrieve the employment status reference data for",
              example = IPartyReferenceService.DEFAULT_LOCALE_ID)
          @RequestParam(
              value = "localeId",
              required = false,
              defaultValue = IPartyReferenceService.DEFAULT_LOCALE_ID)
          String localeId)
      throws InvalidArgumentException, ServiceUnavailableException {
    return partyReferenceService.getEmploymentStatuses(tenantId, localeId);
  }

  /**
   * Retrieve the employment type reference data for a specific locale.
   *
   * @param tenantId the ID for the tenant the employment type reference data is specific to
   * @param localeId the Unicode locale identifier for the locale to retrieve the employment type
   *     reference data for
   * @return the employment type reference data
   * @throws InvalidArgumentException if an argument is invalid
   * @throws ServiceUnavailableException if the employment type reference data could not be
   *     retrieved
   */
  @Operation(
      summary = "Retrieve the employment type reference data for a specific locale",
      description = "Retrieve the employment type reference data for a specific locale")
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
            responseCode = "500",
            description =
                "An error has occurred and the request could not be processed at this time",
            content =
                @Content(
                    mediaType = "application/problem+json",
                    schema = @Schema(implementation = ProblemDetails.class)))
      })
  @RequestMapping(
      value = "/employment-types",
      method = RequestMethod.GET,
      produces = "application/json")
  @ResponseStatus(HttpStatus.OK)
  @PreAuthorize("isSecurityDisabled() or isAuthenticated()")
  public List<EmploymentType> getEmploymentTypes(
      @Parameter(
              name = "Tenant-ID",
              description =
                  "The ID for the tenant the employment type reference data is specific to",
              example = "00000000-0000-0000-0000-000000000000")
          @RequestHeader(
              name = "Tenant-ID",
              defaultValue = "00000000-0000-0000-0000-000000000000",
              required = false)
          UUID tenantId,
      @Parameter(
              name = "localeId",
              description =
                  "The Unicode locale identifier for the locale to retrieve the employment type reference data for",
              example = IPartyReferenceService.DEFAULT_LOCALE_ID)
          @RequestParam(
              value = "localeId",
              required = false,
              defaultValue = IPartyReferenceService.DEFAULT_LOCALE_ID)
          String localeId)
      throws InvalidArgumentException, ServiceUnavailableException {
    return partyReferenceService.getEmploymentTypes(tenantId, localeId);
  }

  /**
   * Retrieve the external reference type reference data for a specific locale.
   *
   * @param tenantId the ID for the tenant the external reference type reference data is specific to
   * @param localeId the Unicode locale identifier for the locale to retrieve the external reference
   *     types reference data for
   * @return the external reference type reference data
   * @throws InvalidArgumentException if an argument is invalid
   * @throws ServiceUnavailableException if the external reference type reference data could not be
   *     retrieved
   */
  @Operation(
      summary = "Retrieve the external reference type reference data for a specific locale",
      description = "Retrieve the external reference type reference data for a specific locale")
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
      produces = "application/json")
  @ResponseStatus(HttpStatus.OK)
  @PreAuthorize("isSecurityDisabled() or isAuthenticated()")
  public List<ExternalReferenceType> getExternalReferenceTypes(
      @Parameter(
              name = "Tenant-ID",
              description =
                  "The ID for the tenant the external reference type reference data is specific to",
              example = "00000000-0000-0000-0000-000000000000")
          @RequestHeader(
              name = "Tenant-ID",
              defaultValue = "00000000-0000-0000-0000-000000000000",
              required = false)
          UUID tenantId,
      @Parameter(
              name = "localeId",
              description =
                  "The Unicode locale identifier for the locale to retrieve the external reference type reference data for",
              example = IPartyReferenceService.DEFAULT_LOCALE_ID)
          @RequestParam(
              value = "localeId",
              required = false,
              defaultValue = IPartyReferenceService.DEFAULT_LOCALE_ID)
          String localeId)
      throws InvalidArgumentException, ServiceUnavailableException {
    return partyReferenceService.getExternalReferenceTypes(tenantId, localeId);
  }

  /**
   * Retrieve the fields of study reference data for a specific locale.
   *
   * @param tenantId the ID for the tenant the fields of study reference data is specific to
   * @param localeId the Unicode locale identifier for the locale to retrieve the fields of study
   *     reference data for
   * @return the fields of study reference data
   * @throws InvalidArgumentException if an argument is invalid
   * @throws ServiceUnavailableException if the fields of study reference data could not be
   *     retrieved
   */
  @Operation(
      summary = "Retrieve the fields of study reference data for a specific locale",
      description = "Retrieve the fields of study reference data for a specific locale")
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
            responseCode = "500",
            description =
                "An error has occurred and the request could not be processed at this time",
            content =
                @Content(
                    mediaType = "application/problem+json",
                    schema = @Schema(implementation = ProblemDetails.class)))
      })
  @RequestMapping(
      value = "/fields-of-study",
      method = RequestMethod.GET,
      produces = "application/json")
  @ResponseStatus(HttpStatus.OK)
  @PreAuthorize("isSecurityDisabled() or isAuthenticated()")
  public List<FieldOfStudy> getFieldsOfStudy(
      @Parameter(
              name = "Tenant-ID",
              description =
                  "The ID for the tenant the fields of study reference data is specific to",
              example = "00000000-0000-0000-0000-000000000000")
          @RequestHeader(
              name = "Tenant-ID",
              defaultValue = "00000000-0000-0000-0000-000000000000",
              required = false)
          UUID tenantId,
      @Parameter(
              name = "localeId",
              description =
                  "The Unicode locale identifier for the locale to retrieve the fields of study reference data for",
              example = IPartyReferenceService.DEFAULT_LOCALE_ID)
          @RequestParam(
              value = "localeId",
              required = false,
              defaultValue = IPartyReferenceService.DEFAULT_LOCALE_ID)
          String localeId)
      throws InvalidArgumentException, ServiceUnavailableException {
    return partyReferenceService.getFieldsOfStudy(tenantId, localeId);
  }

  /**
   * Retrieve the gender reference data for a specific locale.
   *
   * @param tenantId the ID for the tenant the gender reference data is specific to
   * @param localeId the Unicode locale identifier for the locale to retrieve the gender reference
   *     data for
   * @return the gender reference data
   * @throws InvalidArgumentException if an argument is invalid
   * @throws ServiceUnavailableException if the gender reference data could not be retrieved
   */
  @Operation(
      summary = "Retrieve the gender reference data for a specific locale",
      description = "Retrieve the gender reference data for a specific locale")
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
            responseCode = "500",
            description =
                "An error has occurred and the request could not be processed at this time",
            content =
                @Content(
                    mediaType = "application/problem+json",
                    schema = @Schema(implementation = ProblemDetails.class)))
      })
  @RequestMapping(value = "/genders", method = RequestMethod.GET, produces = "application/json")
  @ResponseStatus(HttpStatus.OK)
  @PreAuthorize("isSecurityDisabled() or isAuthenticated()")
  public List<Gender> getGenders(
      @Parameter(
              name = "Tenant-ID",
              description = "The ID for the tenant the gender reference data is specific to",
              example = "00000000-0000-0000-0000-000000000000")
          @RequestHeader(
              name = "Tenant-ID",
              defaultValue = "00000000-0000-0000-0000-000000000000",
              required = false)
          UUID tenantId,
      @Parameter(
              name = "localeId",
              description =
                  "The Unicode locale identifier for the locale to retrieve the gender reference data for",
              example = IPartyReferenceService.DEFAULT_LOCALE_ID)
          @RequestParam(
              value = "localeId",
              required = false,
              defaultValue = IPartyReferenceService.DEFAULT_LOCALE_ID)
          String localeId)
      throws InvalidArgumentException, ServiceUnavailableException {
    return partyReferenceService.getGenders(tenantId, localeId);
  }

  /**
   * Retrieve the identity document type reference data for a specific locale.
   *
   * @param tenantId the ID for the tenant the identity document type reference data is specific to
   * @param localeId the Unicode locale identifier for the locale to retrieve the identity document
   *     type reference data for
   * @return the identity document type reference data
   * @throws InvalidArgumentException if an argument is invalid
   * @throws ServiceUnavailableException if the identity document type reference data could not be
   *     retrieved
   */
  @Operation(
      summary = "Retrieve the identity document type reference data for a specific locale",
      description = "Retrieve the identity document type reference data for a specific locale")
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
            responseCode = "500",
            description =
                "An error has occurred and the request could not be processed at this time",
            content =
                @Content(
                    mediaType = "application/problem+json",
                    schema = @Schema(implementation = ProblemDetails.class)))
      })
  @RequestMapping(
      value = "/identity-document-types",
      method = RequestMethod.GET,
      produces = "application/json")
  @ResponseStatus(HttpStatus.OK)
  @PreAuthorize("isSecurityDisabled() or isAuthenticated()")
  public List<IdentityDocumentType> getIdentityDocumentTypes(
      @Parameter(
              name = "Tenant-ID",
              description =
                  "The ID for the tenant the identity document type reference data is specific to",
              example = "00000000-0000-0000-0000-000000000000")
          @RequestHeader(
              name = "Tenant-ID",
              defaultValue = "00000000-0000-0000-0000-000000000000",
              required = false)
          UUID tenantId,
      @Parameter(
              name = "localeId",
              description =
                  "The Unicode locale identifier for the locale to retrieve the identity document type reference data for",
              example = IPartyReferenceService.DEFAULT_LOCALE_ID)
          @RequestParam(
              value = "localeId",
              required = false,
              defaultValue = IPartyReferenceService.DEFAULT_LOCALE_ID)
          String localeId)
      throws InvalidArgumentException, ServiceUnavailableException {
    return partyReferenceService.getIdentityDocumentTypes(tenantId, localeId);
  }

  /**
   * Retrieve the industry classification category reference data for a specific locale.
   *
   * @param tenantId the ID for the tenant the industry classification category reference data is
   *     specific to
   * @param localeId the Unicode locale identifier for the locale to retrieve the industry
   *     classification category reference data for
   * @return the industry classification category reference data
   * @throws InvalidArgumentException if an argument is invalid
   * @throws ServiceUnavailableException if the industry classification category reference data
   *     could not be retrieved
   */
  @Operation(
      summary =
          "Retrieve the industry classification category reference data for a specific locale",
      description =
          "Retrieve the industry classification category reference data for a specific locale")
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
            responseCode = "500",
            description =
                "An error has occurred and the request could not be processed at this time",
            content =
                @Content(
                    mediaType = "application/problem+json",
                    schema = @Schema(implementation = ProblemDetails.class)))
      })
  @RequestMapping(
      value = "/industry-classification-categories",
      method = RequestMethod.GET,
      produces = "application/json")
  @ResponseStatus(HttpStatus.OK)
  @PreAuthorize("isSecurityDisabled() or isAuthenticated()")
  public List<IndustryClassificationCategory> getIndustryClassificationCategories(
      @Parameter(
              name = "Tenant-ID",
              description =
                  "The ID for the tenant the industry classification category reference data is specific to",
              example = "00000000-0000-0000-0000-000000000000")
          @RequestHeader(
              name = "Tenant-ID",
              defaultValue = "00000000-0000-0000-0000-000000000000",
              required = false)
          UUID tenantId,
      @Parameter(
              name = "localeId",
              description =
                  "The Unicode locale identifier for the locale to retrieve the industry classification category reference data for",
              example = IPartyReferenceService.DEFAULT_LOCALE_ID)
          @RequestParam(
              value = "localeId",
              required = false,
              defaultValue = IPartyReferenceService.DEFAULT_LOCALE_ID)
          String localeId)
      throws InvalidArgumentException, ServiceUnavailableException {
    return partyReferenceService.getIndustryClassificationCategories(tenantId, localeId);
  }

  /**
   * Retrieve the industry classification system reference data for a specific locale.
   *
   * @param tenantId the ID for the tenant the industry classification system reference data is
   *     specific to
   * @param localeId the Unicode locale identifier for the locale to retrieve the industry
   *     classification system reference data for
   * @return the industry classification system reference data
   * @throws InvalidArgumentException if an argument is invalid
   * @throws ServiceUnavailableException if the industry classification system reference data could
   *     not be retrieved
   */
  @Operation(
      summary = "Retrieve the industry classification system reference data for a specific locale",
      description =
          "Retrieve the industry classification system reference data for a specific locale")
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
            responseCode = "500",
            description =
                "An error has occurred and the request could not be processed at this time",
            content =
                @Content(
                    mediaType = "application/problem+json",
                    schema = @Schema(implementation = ProblemDetails.class)))
      })
  @RequestMapping(
      value = "/industry-classification-systems",
      method = RequestMethod.GET,
      produces = "application/json")
  @ResponseStatus(HttpStatus.OK)
  @PreAuthorize("isSecurityDisabled() or isAuthenticated()")
  public List<IndustryClassificationSystem> getIndustryClassificationSystems(
      @Parameter(
              name = "Tenant-ID",
              description =
                  "The ID for the tenant the industry classification system reference data is specific to",
              example = "00000000-0000-0000-0000-000000000000")
          @RequestHeader(
              name = "Tenant-ID",
              defaultValue = "00000000-0000-0000-0000-000000000000",
              required = false)
          UUID tenantId,
      @Parameter(
              name = "localeId",
              description =
                  "The Unicode locale identifier for the locale to retrieve the industry classification system reference data for",
              example = IPartyReferenceService.DEFAULT_LOCALE_ID)
          @RequestParam(
              value = "localeId",
              required = false,
              defaultValue = IPartyReferenceService.DEFAULT_LOCALE_ID)
          String localeId)
      throws InvalidArgumentException, ServiceUnavailableException {
    return partyReferenceService.getIndustryClassificationSystems(tenantId, localeId);
  }

  /**
   * Retrieve the industry classification reference data for a specific locale.
   *
   * @param tenantId the ID for the tenant the industry classification reference data is specific to
   * @param localeId the Unicode locale identifier for the locale to retrieve the industry
   *     classification reference data for
   * @return the industry classification reference data
   * @throws InvalidArgumentException if an argument is invalid
   * @throws ServiceUnavailableException if the industry classification reference data could not be
   *     retrieved
   */
  @Operation(
      summary = "Retrieve the industry classification reference data for a specific locale",
      description = "Retrieve the industry classification reference data for a specific locale")
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
            responseCode = "500",
            description =
                "An error has occurred and the request could not be processed at this time",
            content =
                @Content(
                    mediaType = "application/problem+json",
                    schema = @Schema(implementation = ProblemDetails.class)))
      })
  @RequestMapping(
      value = "/industry-classifications",
      method = RequestMethod.GET,
      produces = "application/json")
  @ResponseStatus(HttpStatus.OK)
  @PreAuthorize("isSecurityDisabled() or isAuthenticated()")
  public List<IndustryClassification> getIndustryClassifications(
      @Parameter(
              name = "Tenant-ID",
              description =
                  "The ID for the tenant the industry classification reference data is specific to",
              example = "00000000-0000-0000-0000-000000000000")
          @RequestHeader(
              name = "Tenant-ID",
              defaultValue = "00000000-0000-0000-0000-000000000000",
              required = false)
          UUID tenantId,
      @Parameter(
              name = "localeId",
              description =
                  "The Unicode locale identifier for the locale to retrieve the industry classification reference data for",
              example = IPartyReferenceService.DEFAULT_LOCALE_ID)
          @RequestParam(
              value = "localeId",
              required = false,
              defaultValue = IPartyReferenceService.DEFAULT_LOCALE_ID)
          String localeId)
      throws InvalidArgumentException, ServiceUnavailableException {
    return partyReferenceService.getIndustryClassifications(tenantId, localeId);
  }

  /**
   * Retrieve the link type reference data for a specific locale.
   *
   * @param tenantId the ID for the tenant the link type reference data is specific to
   * @param localeId the Unicode locale identifier for the locale to retrieve the link type
   *     reference data for
   * @return the link type reference data
   * @throws InvalidArgumentException if an argument is invalid
   * @throws ServiceUnavailableException if the link type reference data could not be retrieved
   */
  @Operation(
      summary = "Retrieve the link type reference data for a specific locale",
      description = "Retrieve the link type reference data for a specific locale")
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
            responseCode = "500",
            description =
                "An error has occurred and the request could not be processed at this time",
            content =
                @Content(
                    mediaType = "application/problem+json",
                    schema = @Schema(implementation = ProblemDetails.class)))
      })
  @RequestMapping(value = "/link-types", method = RequestMethod.GET, produces = "application/json")
  @ResponseStatus(HttpStatus.OK)
  @PreAuthorize("isSecurityDisabled() or isAuthenticated()")
  public List<LinkType> getLinkTypes(
      @Parameter(
              name = "Tenant-ID",
              description = "The ID for the tenant the link type reference data is specific to",
              example = "00000000-0000-0000-0000-000000000000")
          @RequestHeader(
              name = "Tenant-ID",
              defaultValue = "00000000-0000-0000-0000-000000000000",
              required = false)
          UUID tenantId,
      @Parameter(
              name = "localeId",
              description =
                  "The Unicode locale identifier for the locale to retrieve the link type reference data for",
              example = IPartyReferenceService.DEFAULT_LOCALE_ID)
          @RequestParam(
              value = "localeId",
              required = false,
              defaultValue = IPartyReferenceService.DEFAULT_LOCALE_ID)
          String localeId)
      throws InvalidArgumentException, ServiceUnavailableException {
    return partyReferenceService.getLinkTypes(tenantId, localeId);
  }

  /**
   * Retrieve the lock type category reference data for a specific locale.
   *
   * @param tenantId the ID for the tenant the lock type category reference data is specific to
   * @param localeId the Unicode locale identifier for the locale to retrieve the lock type category
   *     reference data for
   * @return the lock type category reference data
   * @throws InvalidArgumentException if an argument is invalid
   * @throws ServiceUnavailableException if the lock type category reference data could not be
   *     retrieved
   */
  @Operation(
      summary = "Retrieve the lock type category reference data for a specific locale",
      description = "Retrieve the lock type category reference data for a specific locale")
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
            responseCode = "500",
            description =
                "An error has occurred and the request could not be processed at this time",
            content =
                @Content(
                    mediaType = "application/problem+json",
                    schema = @Schema(implementation = ProblemDetails.class)))
      })
  @RequestMapping(
      value = "/lock-type-categories",
      method = RequestMethod.GET,
      produces = "application/json")
  @ResponseStatus(HttpStatus.OK)
  @PreAuthorize("isSecurityDisabled() or isAuthenticated()")
  public List<LockTypeCategory> getLockTypeCategories(
      @Parameter(
              name = "Tenant-ID",
              description =
                  "The ID for the tenant the lock type category reference data is specific to",
              example = "00000000-0000-0000-0000-000000000000")
          @RequestHeader(
              name = "Tenant-ID",
              defaultValue = "00000000-0000-0000-0000-000000000000",
              required = false)
          UUID tenantId,
      @Parameter(
              name = "localeId",
              description =
                  "The Unicode locale identifier for the locale to retrieve the lock type category reference data for",
              example = IPartyReferenceService.DEFAULT_LOCALE_ID)
          @RequestParam(
              value = "localeId",
              required = false,
              defaultValue = IPartyReferenceService.DEFAULT_LOCALE_ID)
          String localeId)
      throws InvalidArgumentException, ServiceUnavailableException {
    return partyReferenceService.getLockTypeCategories(tenantId, localeId);
  }

  /**
   * Retrieve the lock type reference data for a specific locale.
   *
   * @param tenantId the ID for the tenant the lock type reference data is specific to
   * @param localeId the Unicode locale identifier for the locale to retrieve the lock type
   *     reference data for
   * @return the lock type reference data
   * @throws InvalidArgumentException if an argument is invalid
   * @throws ServiceUnavailableException if the lock type reference data could not be retrieved
   */
  @Operation(
      summary = "Retrieve the lock type reference data for a specific locale",
      description = "Retrieve the lock type reference data for a specific locale")
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
            responseCode = "500",
            description =
                "An error has occurred and the request could not be processed at this time",
            content =
                @Content(
                    mediaType = "application/problem+json",
                    schema = @Schema(implementation = ProblemDetails.class)))
      })
  @RequestMapping(value = "/lock-types", method = RequestMethod.GET, produces = "application/json")
  @ResponseStatus(HttpStatus.OK)
  @PreAuthorize("isSecurityDisabled() or isAuthenticated()")
  public List<LockType> getLockTypes(
      @Parameter(
              name = "Tenant-ID",
              description = "The ID for the tenant the lock type reference data is specific to",
              example = "00000000-0000-0000-0000-000000000000")
          @RequestHeader(
              name = "Tenant-ID",
              defaultValue = "00000000-0000-0000-0000-000000000000",
              required = false)
          UUID tenantId,
      @Parameter(
              name = "localeId",
              description =
                  "The Unicode locale identifier for the locale to retrieve the lock type reference data for",
              example = IPartyReferenceService.DEFAULT_LOCALE_ID)
          @RequestParam(
              value = "localeId",
              required = false,
              defaultValue = IPartyReferenceService.DEFAULT_LOCALE_ID)
          String localeId)
      throws InvalidArgumentException, ServiceUnavailableException {
    return partyReferenceService.getLockTypes(tenantId, localeId);
  }

  /**
   * Retrieve the mandatary role reference data for a specific locale.
   *
   * @param tenantId the ID for the tenant the mandatary role reference data is specific to
   * @param localeId the Unicode locale identifier for the locale to retrieve the mandatary role
   *     reference data for
   * @return the mandatary role reference data
   * @throws InvalidArgumentException if an argument is invalid
   * @throws ServiceUnavailableException if the mandatary role reference data could not be retrieved
   */
  @Operation(
      summary = "Retrieve the mandatary role reference data for a specific locale",
      description = "Retrieve the mandatary role reference data for a specific locale")
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
            responseCode = "500",
            description =
                "An error has occurred and the request could not be processed at this time",
            content =
                @Content(
                    mediaType = "application/problem+json",
                    schema = @Schema(implementation = ProblemDetails.class)))
      })
  @RequestMapping(
      value = "/mandatary-types",
      method = RequestMethod.GET,
      produces = "application/json")
  @ResponseStatus(HttpStatus.OK)
  @PreAuthorize("isSecurityDisabled() or isAuthenticated()")
  public List<MandataryRole> getMandataryRoles(
      @Parameter(
              name = "Tenant-ID",
              description =
                  "The ID for the tenant the mandatary role reference data is specific to",
              example = "00000000-0000-0000-0000-000000000000")
          @RequestHeader(
              name = "Tenant-ID",
              defaultValue = "00000000-0000-0000-0000-000000000000",
              required = false)
          UUID tenantId,
      @Parameter(
              name = "localeId",
              description =
                  "The Unicode locale identifier for the locale to retrieve the mandatary role reference data for",
              example = IPartyReferenceService.DEFAULT_LOCALE_ID)
          @RequestParam(
              value = "localeId",
              required = false,
              defaultValue = IPartyReferenceService.DEFAULT_LOCALE_ID)
          String localeId)
      throws InvalidArgumentException, ServiceUnavailableException {
    return partyReferenceService.getMandataryRoles(tenantId, localeId);
  }

  /**
   * Retrieve the mandate property type reference data for a specific locale.
   *
   * @param tenantId the ID for the tenant the mandate property type reference data is specific to
   * @param localeId the Unicode locale identifier for the locale to retrieve the mandate property
   *     type reference data for
   * @return the mandate property type reference data
   * @throws InvalidArgumentException if an argument is invalid
   * @throws ServiceUnavailableException if the mandate property type reference data could not be
   *     retrieved
   */
  @Operation(
      summary = "Retrieve the mandate property type reference data for a specific locale",
      description = "Retrieve the mandate property type reference data for a specific locale")
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
            responseCode = "500",
            description =
                "An error has occurred and the request could not be processed at this time",
            content =
                @Content(
                    mediaType = "application/problem+json",
                    schema = @Schema(implementation = ProblemDetails.class)))
      })
  @RequestMapping(
      value = "/mandate-property-types",
      method = RequestMethod.GET,
      produces = "application/json")
  @ResponseStatus(HttpStatus.OK)
  @PreAuthorize("isSecurityDisabled() or isAuthenticated()")
  public List<MandatePropertyType> getMandatePropertyTypes(
      @Parameter(
              name = "Tenant-ID",
              description =
                  "The ID for the tenant the mandate property type reference data is specific to",
              example = "00000000-0000-0000-0000-000000000000")
          @RequestHeader(
              name = "Tenant-ID",
              defaultValue = "00000000-0000-0000-0000-000000000000",
              required = false)
          UUID tenantId,
      @Parameter(
              name = "localeId",
              description =
                  "The Unicode locale identifier for the locale to retrieve the mandate property type reference data for",
              example = IPartyReferenceService.DEFAULT_LOCALE_ID)
          @RequestParam(
              value = "localeId",
              required = false,
              defaultValue = IPartyReferenceService.DEFAULT_LOCALE_ID)
          String localeId)
      throws InvalidArgumentException, ServiceUnavailableException {
    return partyReferenceService.getMandatePropertyTypes(tenantId, localeId);
  }

  /**
   * Retrieve the mandate type reference data for a specific locale.
   *
   * @param tenantId the ID for the tenant the mandate type reference data is specific to
   * @param localeId the Unicode locale identifier for the locale to retrieve the mandate type
   *     reference data for
   * @return the mandate type reference data
   * @throws InvalidArgumentException if an argument is invalid
   * @throws ServiceUnavailableException if the mandate type reference data could not be retrieved
   */
  @Operation(
      summary = "Retrieve the mandate type reference data for a specific locale",
      description = "Retrieve the mandate type reference data for a specific locale")
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
            responseCode = "500",
            description =
                "An error has occurred and the request could not be processed at this time",
            content =
                @Content(
                    mediaType = "application/problem+json",
                    schema = @Schema(implementation = ProblemDetails.class)))
      })
  @RequestMapping(
      value = "/mandate-types",
      method = RequestMethod.GET,
      produces = "application/json")
  @ResponseStatus(HttpStatus.OK)
  @PreAuthorize("isSecurityDisabled() or isAuthenticated()")
  public List<MandateType> getMandateTypes(
      @Parameter(
              name = "Tenant-ID",
              description = "The ID for the tenant the mandate type reference data is specific to",
              example = "00000000-0000-0000-0000-000000000000")
          @RequestHeader(
              name = "Tenant-ID",
              defaultValue = "00000000-0000-0000-0000-000000000000",
              required = false)
          UUID tenantId,
      @Parameter(
              name = "localeId",
              description =
                  "The Unicode locale identifier for the locale to retrieve the mandate type reference data for",
              example = IPartyReferenceService.DEFAULT_LOCALE_ID)
          @RequestParam(
              value = "localeId",
              required = false,
              defaultValue = IPartyReferenceService.DEFAULT_LOCALE_ID)
          String localeId)
      throws InvalidArgumentException, ServiceUnavailableException {
    return partyReferenceService.getMandateTypes(tenantId, localeId);
  }

  /**
   * Retrieve the marital status reference data for a specific locale.
   *
   * @param tenantId the ID for the tenant the marital status reference data is specific to
   * @param localeId the Unicode locale identifier for the locale to retrieve the marital status
   *     reference data for
   * @return the marital status reference data
   * @throws InvalidArgumentException if an argument is invalid
   * @throws ServiceUnavailableException if the marital status reference data could not be retrieved
   */
  @Operation(
      summary = "Retrieve the marital status reference data for a specific locale",
      description = "Retrieve the marital status reference data for a specific locale")
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
            responseCode = "500",
            description =
                "An error has occurred and the request could not be processed at this time",
            content =
                @Content(
                    mediaType = "application/problem+json",
                    schema = @Schema(implementation = ProblemDetails.class)))
      })
  @RequestMapping(
      value = "/marital-statuses",
      method = RequestMethod.GET,
      produces = "application/json")
  @ResponseStatus(HttpStatus.OK)
  @PreAuthorize("isSecurityDisabled() or isAuthenticated()")
  public List<MaritalStatus> getMaritalStatuses(
      @Parameter(
              name = "Tenant-ID",
              description =
                  "The ID for the tenant the marital status reference data is specific to",
              example = "00000000-0000-0000-0000-000000000000")
          @RequestHeader(
              name = "Tenant-ID",
              defaultValue = "00000000-0000-0000-0000-000000000000",
              required = false)
          UUID tenantId,
      @Parameter(
              name = "localeId",
              description =
                  "The Unicode locale identifier for the locale to retrieve the marital status reference data for",
              example = IPartyReferenceService.DEFAULT_LOCALE_ID)
          @RequestParam(
              value = "localeId",
              required = false,
              defaultValue = IPartyReferenceService.DEFAULT_LOCALE_ID)
          String localeId)
      throws InvalidArgumentException, ServiceUnavailableException {
    return partyReferenceService.getMaritalStatuses(tenantId, localeId);
  }

  /**
   * Retrieve the marriage type reference data for a specific locale.
   *
   * @param tenantId the ID for the tenant the marriage type reference data is specific to
   * @param localeId the Unicode locale identifier for the locale to retrieve the marriage type
   *     reference data
   * @return the marriage type reference data
   * @throws InvalidArgumentException if an argument is invalid
   * @throws ServiceUnavailableException if the marriage type reference data could not be retrieved
   */
  @Operation(
      summary = "Retrieve the marriage type reference data for a specific locale",
      description = "Retrieve the marriage type reference data for a specific locale")
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
            responseCode = "500",
            description =
                "An error has occurred and the request could not be processed at this time",
            content =
                @Content(
                    mediaType = "application/problem+json",
                    schema = @Schema(implementation = ProblemDetails.class)))
      })
  @RequestMapping(
      value = "/marriage-types",
      method = RequestMethod.GET,
      produces = "application/json")
  @ResponseStatus(HttpStatus.OK)
  @PreAuthorize("isSecurityDisabled() or isAuthenticated()")
  public List<MarriageType> getMarriageTypes(
      @Parameter(
              name = "Tenant-ID",
              description = "The ID for the tenant the marriage type reference data is specific to",
              example = "00000000-0000-0000-0000-000000000000")
          @RequestHeader(
              name = "Tenant-ID",
              defaultValue = "00000000-0000-0000-0000-000000000000",
              required = false)
          UUID tenantId,
      @Parameter(
              name = "localeId",
              description =
                  "The Unicode locale identifier for the locale to retrieve the marriage type reference data for",
              example = IPartyReferenceService.DEFAULT_LOCALE_ID)
          @RequestParam(
              value = "localeId",
              required = false,
              defaultValue = IPartyReferenceService.DEFAULT_LOCALE_ID)
          String localeId)
      throws InvalidArgumentException, ServiceUnavailableException {
    return partyReferenceService.getMarriageTypes(tenantId, localeId);
  }

  /**
   * Retrieve the next of kin type reference data for a specific locale.
   *
   * @param tenantId the ID for the tenant the next of kin type reference data is specific to
   * @param localeId the Unicode locale identifier for the locale to retrieve the next of kin type
   *     reference data for
   * @return the next of kin type reference data
   * @throws InvalidArgumentException if an argument is invalid
   * @throws ServiceUnavailableException if the next of kin type reference data could not be
   *     retrieved
   */
  @Operation(
      summary = "Retrieve the next of kin type reference data for a specific locale",
      description = "Retrieve the next of kin type reference data for a specific locale")
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
            responseCode = "500",
            description =
                "An error has occurred and the request could not be processed at this time",
            content =
                @Content(
                    mediaType = "application/problem+json",
                    schema = @Schema(implementation = ProblemDetails.class)))
      })
  @RequestMapping(
      value = "/next-of-kin-types",
      method = RequestMethod.GET,
      produces = "application/json")
  @ResponseStatus(HttpStatus.OK)
  @PreAuthorize("isSecurityDisabled() or isAuthenticated()")
  public List<NextOfKinType> getNextOfKinTypes(
      @Parameter(
              name = "Tenant-ID",
              description =
                  "The ID for the tenant the next of kin type reference data is specific to",
              example = "00000000-0000-0000-0000-000000000000")
          @RequestHeader(
              name = "Tenant-ID",
              defaultValue = "00000000-0000-0000-0000-000000000000",
              required = false)
          UUID tenantId,
      @Parameter(
              name = "localeId",
              description =
                  "The Unicode locale identifier for the locale to retrieve the next of kin type reference data for",
              example = IPartyReferenceService.DEFAULT_LOCALE_ID)
          @RequestParam(
              value = "localeId",
              required = false,
              defaultValue = IPartyReferenceService.DEFAULT_LOCALE_ID)
          String localeId)
      throws InvalidArgumentException, ServiceUnavailableException {
    return partyReferenceService.getNextOfKinTypes(tenantId, localeId);
  }

  /**
   * Retrieve the occupation reference data for a specific locale.
   *
   * @param tenantId the ID for the tenant the occupation reference data is specific to
   * @param localeId the Unicode locale identifier for the locale to retrieve the occupation
   *     reference data for
   * @return the occupation reference data
   * @throws InvalidArgumentException if an argument is invalid
   * @throws ServiceUnavailableException if the occupation reference data could not be retrieved
   */
  @Operation(
      summary = "Retrieve the occupation reference data for a specific locale",
      description = "Retrieve the occupation reference data for a specific locale")
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
            responseCode = "500",
            description =
                "An error has occurred and the request could not be processed at this time",
            content =
                @Content(
                    mediaType = "application/problem+json",
                    schema = @Schema(implementation = ProblemDetails.class)))
      })
  @RequestMapping(value = "/occupations", method = RequestMethod.GET, produces = "application/json")
  @ResponseStatus(HttpStatus.OK)
  @PreAuthorize("isSecurityDisabled() or isAuthenticated()")
  public List<Occupation> getOccupations(
      @Parameter(
              name = "Tenant-ID",
              description = "The ID for the tenant the occupation reference data is specific to",
              example = "00000000-0000-0000-0000-000000000000")
          @RequestHeader(
              name = "Tenant-ID",
              defaultValue = "00000000-0000-0000-0000-000000000000",
              required = false)
          UUID tenantId,
      @Parameter(
              name = "localeId",
              description =
                  "The Unicode locale identifier for the locale to retrieve the occupation reference data for",
              example = IPartyReferenceService.DEFAULT_LOCALE_ID)
          @RequestParam(
              value = "localeId",
              required = false,
              defaultValue = IPartyReferenceService.DEFAULT_LOCALE_ID)
          String localeId)
      throws InvalidArgumentException, ServiceUnavailableException {
    return partyReferenceService.getOccupations(tenantId, localeId);
  }

  /**
   * Retrieve the physical address purpose reference data for a specific locale.
   *
   * @param tenantId the ID for the tenant the physical address purpose reference data is specific
   *     to
   * @param localeId the Unicode locale identifier for the locale to retrieve the physical address
   *     purpose reference data for
   * @return the physical address purpose reference data
   * @throws InvalidArgumentException if an argument is invalid
   * @throws ServiceUnavailableException if the physical address purpose reference data could not be
   *     retrieved
   */
  @Operation(
      summary = "Retrieve the physical address purpose reference data for a specific locale",
      description = "Retrieve the physical address purpose reference data for a specific locale")
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
            responseCode = "500",
            description =
                "An error has occurred and the request could not be processed at this time",
            content =
                @Content(
                    mediaType = "application/problem+json",
                    schema = @Schema(implementation = ProblemDetails.class)))
      })
  @RequestMapping(
      value = "/physical-address-purposes",
      method = RequestMethod.GET,
      produces = "application/json")
  @ResponseStatus(HttpStatus.OK)
  @PreAuthorize("isSecurityDisabled() or isAuthenticated()")
  public List<PhysicalAddressPurpose> getPhysicalAddressPurposes(
      @Parameter(
              name = "Tenant-ID",
              description =
                  "The ID for the tenant the physical address purpose reference data is specific to",
              example = "00000000-0000-0000-0000-000000000000")
          @RequestHeader(
              name = "Tenant-ID",
              defaultValue = "00000000-0000-0000-0000-000000000000",
              required = false)
          UUID tenantId,
      @Parameter(
              name = "localeId",
              description =
                  "The Unicode locale identifier for the locale to retrieve the physical address purpose reference data for",
              example = IPartyReferenceService.DEFAULT_LOCALE_ID)
          @RequestParam(
              value = "localeId",
              required = false,
              defaultValue = IPartyReferenceService.DEFAULT_LOCALE_ID)
          String localeId)
      throws InvalidArgumentException, ServiceUnavailableException {
    return partyReferenceService.getPhysicalAddressPurposes(tenantId, localeId);
  }

  /**
   * Retrieve the physical address role reference data for a specific locale.
   *
   * @param tenantId the ID for the tenant the physical address role reference data is specific to
   * @param localeId the Unicode locale identifier for the locale to retrieve the physical address
   *     role reference data for
   * @return the physical address role reference data
   * @throws InvalidArgumentException if an argument is invalid
   * @throws ServiceUnavailableException if the physical address role reference data could not be
   *     retrieved
   */
  @Operation(
      summary = "Retrieve the physical address role reference data for a specific locale",
      description = "Retrieve the physical address role reference data for a specific locale")
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
            responseCode = "500",
            description =
                "An error has occurred and the request could not be processed at this time",
            content =
                @Content(
                    mediaType = "application/problem+json",
                    schema = @Schema(implementation = ProblemDetails.class)))
      })
  @RequestMapping(
      value = "/physical-address-roles",
      method = RequestMethod.GET,
      produces = "application/json")
  @ResponseStatus(HttpStatus.OK)
  @PreAuthorize("isSecurityDisabled() or isAuthenticated()")
  public List<PhysicalAddressRole> getPhysicalAddressRoles(
      @Parameter(
              name = "Tenant-ID",
              description =
                  "The ID for the tenant the physical address role reference data is specific to",
              example = "00000000-0000-0000-0000-000000000000")
          @RequestHeader(
              name = "Tenant-ID",
              defaultValue = "00000000-0000-0000-0000-000000000000",
              required = false)
          UUID tenantId,
      @Parameter(
              name = "localeId",
              description =
                  "The Unicode locale identifier for the locale to retrieve the physical address role reference data for",
              example = IPartyReferenceService.DEFAULT_LOCALE_ID)
          @RequestParam(
              value = "localeId",
              required = false,
              defaultValue = IPartyReferenceService.DEFAULT_LOCALE_ID)
          String localeId)
      throws InvalidArgumentException, ServiceUnavailableException {
    return partyReferenceService.getPhysicalAddressRoles(tenantId, localeId);
  }

  /**
   * Retrieve the physical address type reference data for a specific locale.
   *
   * @param tenantId the ID for the tenant the physical address type reference data is specific to
   * @param localeId the Unicode locale identifier for the locale to retrieve the physical address
   *     type reference data for
   * @return the physical address type reference data
   * @throws InvalidArgumentException if an argument is invalid
   * @throws ServiceUnavailableException if the physical address type reference data could not be
   *     retrieved
   */
  @Operation(
      summary = "Retrieve the physical address type reference data for a specific locale",
      description = "Retrieve the physical address type reference data for a specific locale")
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
            responseCode = "500",
            description =
                "An error has occurred and the request could not be processed at this time",
            content =
                @Content(
                    mediaType = "application/problem+json",
                    schema = @Schema(implementation = ProblemDetails.class)))
      })
  @RequestMapping(
      value = "/physical-address-types",
      method = RequestMethod.GET,
      produces = "application/json")
  @ResponseStatus(HttpStatus.OK)
  @PreAuthorize("isSecurityDisabled() or isAuthenticated()")
  public List<PhysicalAddressType> getPhysicalAddressTypes(
      @Parameter(
              name = "Tenant-ID",
              description =
                  "The ID for the tenant the physical address type reference data is specific to",
              example = "00000000-0000-0000-0000-000000000000")
          @RequestHeader(
              name = "Tenant-ID",
              defaultValue = "00000000-0000-0000-0000-000000000000",
              required = false)
          UUID tenantId,
      @Parameter(
              name = "localeId",
              description =
                  "The Unicode locale identifier for the locale to retrieve the physical address type reference data for",
              example = IPartyReferenceService.DEFAULT_LOCALE_ID)
          @RequestParam(
              value = "localeId",
              required = false,
              defaultValue = IPartyReferenceService.DEFAULT_LOCALE_ID)
          String localeId)
      throws InvalidArgumentException, ServiceUnavailableException {
    return partyReferenceService.getPhysicalAddressTypes(tenantId, localeId);
  }

  /**
   * Retrieve the preference type category reference data for a specific locale.
   *
   * @param tenantId the ID for the tenant the physical address purpose reference data is specific
   *     to
   * @param localeId the Unicode locale identifier for the locale to retrieve the preference type
   *     category reference data for
   * @return the preference type category reference data
   * @throws InvalidArgumentException if an argument is invalid
   * @throws ServiceUnavailableException if the preference type category reference data could not be
   *     retrieved
   */
  @Operation(
      summary = "Retrieve the preference type category reference data for a specific locale",
      description = "Retrieve the preference type category reference data for a specific locale")
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
            responseCode = "500",
            description =
                "An error has occurred and the request could not be processed at this time",
            content =
                @Content(
                    mediaType = "application/problem+json",
                    schema = @Schema(implementation = ProblemDetails.class)))
      })
  @RequestMapping(
      value = "/preference-type-categories",
      method = RequestMethod.GET,
      produces = "application/json")
  @ResponseStatus(HttpStatus.OK)
  @PreAuthorize("isSecurityDisabled() or isAuthenticated()")
  public List<PreferenceTypeCategory> getPreferenceTypeCategories(
      @Parameter(
              name = "Tenant-ID",
              description =
                  "The ID for the tenant the preference type category reference data is specific to",
              example = "00000000-0000-0000-0000-000000000000")
          @RequestHeader(
              name = "Tenant-ID",
              defaultValue = "00000000-0000-0000-0000-000000000000",
              required = false)
          UUID tenantId,
      @Parameter(
              name = "localeId",
              description =
                  "The Unicode locale identifier for the locale to retrieve the preference type category reference data for",
              example = IPartyReferenceService.DEFAULT_LOCALE_ID)
          @RequestParam(
              value = "localeId",
              required = false,
              defaultValue = IPartyReferenceService.DEFAULT_LOCALE_ID)
          String localeId)
      throws InvalidArgumentException, ServiceUnavailableException {
    return partyReferenceService.getPreferenceTypeCategories(tenantId, localeId);
  }

  /**
   * Retrieve the preference type reference data for a specific locale.
   *
   * @param tenantId the ID for the tenant the preference type reference data is specific to
   * @param localeId the Unicode locale identifier for the locale to retrieve the preference type
   *     reference data for
   * @return the preference type reference data
   * @throws InvalidArgumentException if an argument is invalid
   * @throws ServiceUnavailableException if the preference type reference data could not be
   *     retrieved
   */
  @Operation(
      summary = "Retrieve the preference type reference data for a specific locale",
      description = "Retrieve the preference type reference data for a specific locale")
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
            responseCode = "500",
            description =
                "An error has occurred and the request could not be processed at this time",
            content =
                @Content(
                    mediaType = "application/problem+json",
                    schema = @Schema(implementation = ProblemDetails.class)))
      })
  @RequestMapping(
      value = "/preference-types",
      method = RequestMethod.GET,
      produces = "application/json")
  @ResponseStatus(HttpStatus.OK)
  @PreAuthorize("isSecurityDisabled() or isAuthenticated()")
  public List<PreferenceType> getPreferenceTypes(
      @Parameter(
              name = "Tenant-ID",
              description =
                  "The ID for the tenant the preference type reference data is specific to",
              example = "00000000-0000-0000-0000-000000000000")
          @RequestHeader(
              name = "Tenant-ID",
              defaultValue = "00000000-0000-0000-0000-000000000000",
              required = false)
          UUID tenantId,
      @Parameter(
              name = "localeId",
              description =
                  "The Unicode locale identifier for the locale to retrieve the preference type reference data for",
              example = IPartyReferenceService.DEFAULT_LOCALE_ID)
          @RequestParam(
              value = "localeId",
              required = false,
              defaultValue = IPartyReferenceService.DEFAULT_LOCALE_ID)
          String localeId)
      throws InvalidArgumentException, ServiceUnavailableException {
    return partyReferenceService.getPreferenceTypes(tenantId, localeId);
  }

  /**
   * Retrieve the qualification type reference data for a specific locale.
   *
   * @param tenantId the ID for the tenant the qualification type reference data is specific to
   * @param localeId the Unicode locale identifier for the locale to retrieve the qualification type
   *     reference data for
   * @return the qualification type reference data
   * @throws InvalidArgumentException if an argument is invalid
   * @throws ServiceUnavailableException if the qualification type reference data could not be
   *     retrieved
   */
  @Operation(
      summary = "Retrieve the qualification type reference data for a specific locale",
      description = "Retrieve the qualification type reference data for a specific locale")
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
            responseCode = "500",
            description =
                "An error has occurred and the request could not be processed at this time",
            content =
                @Content(
                    mediaType = "application/problem+json",
                    schema = @Schema(implementation = ProblemDetails.class)))
      })
  @RequestMapping(
      value = "/qualification-types",
      method = RequestMethod.GET,
      produces = "application/json")
  @ResponseStatus(HttpStatus.OK)
  @PreAuthorize("isSecurityDisabled() or isAuthenticated()")
  public List<QualificationType> getQualificationTypes(
      @Parameter(
              name = "Tenant-ID",
              description =
                  "The ID for the tenant the qualification type reference data is specific to",
              example = "00000000-0000-0000-0000-000000000000")
          @RequestHeader(
              name = "Tenant-ID",
              defaultValue = "00000000-0000-0000-0000-000000000000",
              required = false)
          UUID tenantId,
      @Parameter(
              name = "localeId",
              description =
                  "The Unicode locale identifier for the locale to retrieve the qualification type reference data for",
              example = IPartyReferenceService.DEFAULT_LOCALE_ID)
          @RequestParam(
              value = "localeId",
              required = false,
              defaultValue = IPartyReferenceService.DEFAULT_LOCALE_ID)
          String localeId)
      throws InvalidArgumentException, ServiceUnavailableException {
    return partyReferenceService.getQualificationTypes(tenantId, localeId);
  }

  /**
   * Retrieve the race reference data for a specific locale.
   *
   * @param tenantId the ID for the tenant the race reference data is specific to
   * @param localeId the Unicode locale identifier for the locale to retrieve the race reference
   *     data for
   * @return the race reference data
   * @throws InvalidArgumentException if an argument is invalid
   * @throws ServiceUnavailableException if the race reference data could not be retrieved
   */
  @Operation(
      summary = "Retrieve the race reference data for a specific locale",
      description = "Retrieve the race reference data for a specific locale")
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
            responseCode = "500",
            description =
                "An error has occurred and the request could not be processed at this time",
            content =
                @Content(
                    mediaType = "application/problem+json",
                    schema = @Schema(implementation = ProblemDetails.class)))
      })
  @RequestMapping(value = "/races", method = RequestMethod.GET, produces = "application/json")
  @ResponseStatus(HttpStatus.OK)
  @PreAuthorize("isSecurityDisabled() or isAuthenticated()")
  public List<Race> getRaces(
      @Parameter(
              name = "Tenant-ID",
              description = "The ID for the tenant the race reference data is specific to",
              example = "00000000-0000-0000-0000-000000000000")
          @RequestHeader(
              name = "Tenant-ID",
              defaultValue = "00000000-0000-0000-0000-000000000000",
              required = false)
          UUID tenantId,
      @Parameter(
              name = "localeId",
              description =
                  "The Unicode locale identifier for the locale to retrieve the race reference data for",
              example = IPartyReferenceService.DEFAULT_LOCALE_ID)
          @RequestParam(
              value = "localeId",
              required = false,
              defaultValue = IPartyReferenceService.DEFAULT_LOCALE_ID)
          String localeId)
      throws InvalidArgumentException, ServiceUnavailableException {
    return partyReferenceService.getRaces(tenantId, localeId);
  }

  /**
   * Retrieve the residence permit type reference data for a specific locale.
   *
   * @param tenantId the ID for the tenant the residence permit type reference data is specific to
   * @param localeId the Unicode locale identifier for the locale to retrieve the residence permit
   *     type reference data for
   * @return the residence permit type reference data
   * @throws InvalidArgumentException if an argument is invalid
   * @throws ServiceUnavailableException if the residence permit type reference data could not be
   *     retrieved
   */
  @Operation(
      summary = "Retrieve the residence permit type reference data for a specific locale",
      description = "Retrieve the residence permit type reference data for a specific locale")
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
            responseCode = "500",
            description =
                "An error has occurred and the request could not be processed at this time",
            content =
                @Content(
                    mediaType = "application/problem+json",
                    schema = @Schema(implementation = ProblemDetails.class)))
      })
  @RequestMapping(
      value = "/residence-permit-types",
      method = RequestMethod.GET,
      produces = "application/json")
  @ResponseStatus(HttpStatus.OK)
  @PreAuthorize("isSecurityDisabled() or isAuthenticated()")
  public List<ResidencePermitType> getResidencePermitTypes(
      @Parameter(
              name = "Tenant-ID",
              description =
                  "The ID for the tenant the residence permit type reference data is specific to",
              example = "00000000-0000-0000-0000-000000000000")
          @RequestHeader(
              name = "Tenant-ID",
              defaultValue = "00000000-0000-0000-0000-000000000000",
              required = false)
          UUID tenantId,
      @Parameter(
              name = "localeId",
              description =
                  "The Unicode locale identifier for the locale to retrieve the residence permit type reference data for",
              example = IPartyReferenceService.DEFAULT_LOCALE_ID)
          @RequestParam(
              value = "localeId",
              required = false,
              defaultValue = IPartyReferenceService.DEFAULT_LOCALE_ID)
          String localeId)
      throws InvalidArgumentException, ServiceUnavailableException {
    return partyReferenceService.getResidencePermitTypes(tenantId, localeId);
  }

  /**
   * Retrieve the residency status reference data for a specific locale.
   *
   * @param tenantId the ID for the tenant the residency status reference data is specific to
   * @param localeId the Unicode locale identifier for the locale to retrieve the residency status
   *     reference data for
   * @return the residency status reference data
   * @throws InvalidArgumentException if an argument is invalid
   * @throws ServiceUnavailableException if the residency status reference data could not be
   *     retrieved
   */
  @Operation(
      summary = "Retrieve the residency status reference data for a specific locale",
      description = "Retrieve the residency status reference data for a specific locale")
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
            responseCode = "500",
            description =
                "An error has occurred and the request could not be processed at this time",
            content =
                @Content(
                    mediaType = "application/problem+json",
                    schema = @Schema(implementation = ProblemDetails.class)))
      })
  @RequestMapping(
      value = "/residency-statuses",
      method = RequestMethod.GET,
      produces = "application/json")
  @ResponseStatus(HttpStatus.OK)
  @PreAuthorize("isSecurityDisabled() or isAuthenticated()")
  public List<ResidencyStatus> getResidencyStatuses(
      @Parameter(
              name = "Tenant-ID",
              description =
                  "The ID for the tenant the residency status reference data is specific to",
              example = "00000000-0000-0000-0000-000000000000")
          @RequestHeader(
              name = "Tenant-ID",
              defaultValue = "00000000-0000-0000-0000-000000000000",
              required = false)
          UUID tenantId,
      @Parameter(
              name = "localeId",
              description =
                  "The Unicode locale identifier for the locale to retrieve the residency status reference data for",
              example = IPartyReferenceService.DEFAULT_LOCALE_ID)
          @RequestParam(
              value = "localeId",
              required = false,
              defaultValue = IPartyReferenceService.DEFAULT_LOCALE_ID)
          String localeId)
      throws InvalidArgumentException, ServiceUnavailableException {
    return partyReferenceService.getResidencyStatuses(tenantId, localeId);
  }

  /**
   * Retrieve the residential type reference data for a specific locale.
   *
   * @param tenantId the ID for the tenant the residential type reference data is specific to
   * @param localeId the Unicode locale identifier for the locale to retrieve the residential type
   *     reference data for
   * @return the residential type reference data
   * @throws InvalidArgumentException if an argument is invalid
   * @throws ServiceUnavailableException if the residential type reference data could not be
   *     retrieved
   */
  @Operation(
      summary = "Retrieve the residential type reference data for a specific locale",
      description = "Retrieve the residential type reference data for a specific locale")
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
            responseCode = "500",
            description =
                "An error has occurred and the request could not be processed at this time",
            content =
                @Content(
                    mediaType = "application/problem+json",
                    schema = @Schema(implementation = ProblemDetails.class)))
      })
  @RequestMapping(
      value = "/residential-types",
      method = RequestMethod.GET,
      produces = "application/json")
  @ResponseStatus(HttpStatus.OK)
  @PreAuthorize("isSecurityDisabled() or isAuthenticated()")
  public List<ResidentialType> getResidentialTypes(
      @Parameter(
              name = "Tenant-ID",
              description =
                  "The ID for the tenant the residential type reference data is specific to",
              example = "00000000-0000-0000-0000-000000000000")
          @RequestHeader(
              name = "Tenant-ID",
              defaultValue = "00000000-0000-0000-0000-000000000000",
              required = false)
          UUID tenantId,
      @Parameter(
              name = "localeId",
              description =
                  "The Unicode locale identifier for the locale to retrieve the residential type reference data for",
              example = IPartyReferenceService.DEFAULT_LOCALE_ID)
          @RequestParam(
              value = "localeId",
              required = false,
              defaultValue = IPartyReferenceService.DEFAULT_LOCALE_ID)
          String localeId)
      throws InvalidArgumentException, ServiceUnavailableException {
    return partyReferenceService.getResidentialTypes(tenantId, localeId);
  }

  /**
   * Retrieve the role purpose reference data for a specific locale.
   *
   * @param tenantId the ID for the tenant the role purpose reference data is specific to
   * @param localeId the Unicode locale identifier for the locale to retrieve the role purpose
   *     reference data for
   * @return the role purpose reference data
   * @throws InvalidArgumentException if an argument is invalid
   * @throws ServiceUnavailableException if the role purpose reference data could not be retrieved
   */
  @Operation(
      summary = "Retrieve the role purpose reference data for a specific locale",
      description = "Retrieve the role purpose reference data for a specific locale")
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
            responseCode = "500",
            description =
                "An error has occurred and the request could not be processed at this time",
            content =
                @Content(
                    mediaType = "application/problem+json",
                    schema = @Schema(implementation = ProblemDetails.class)))
      })
  @RequestMapping(
      value = "/role-purposes",
      method = RequestMethod.GET,
      produces = "application/json")
  @ResponseStatus(HttpStatus.OK)
  @PreAuthorize("isSecurityDisabled() or isAuthenticated()")
  public List<RolePurpose> getRolePurposes(
      @Parameter(
              name = "Tenant-ID",
              description = "The ID for the tenant the role purpose reference data is specific to",
              example = "00000000-0000-0000-0000-000000000000")
          @RequestHeader(
              name = "Tenant-ID",
              defaultValue = "00000000-0000-0000-0000-000000000000",
              required = false)
          UUID tenantId,
      @Parameter(
              name = "localeId",
              description =
                  "The Unicode locale identifier for the locale to retrieve the role purpose reference data for",
              example = IPartyReferenceService.DEFAULT_LOCALE_ID)
          @RequestParam(
              value = "localeId",
              required = false,
              defaultValue = IPartyReferenceService.DEFAULT_LOCALE_ID)
          String localeId)
      throws InvalidArgumentException, ServiceUnavailableException {
    return partyReferenceService.getRolePurposes(tenantId, localeId);
  }

  /**
   * Retrieve the role type attribute type constraints for a specific role type.
   *
   * @param roleType the code for the role type to retrieve the attribute constraints for
   * @return the role type attribute type constraints
   * @throws InvalidArgumentException if an argument is invalid
   * @throws ServiceUnavailableException if the role type attribute type constraints for a specific
   *     role type could not be retrieved
   */
  @Operation(
      summary = "Retrieve the role type attribute type constraints",
      description = "Retrieve the role type attribute type constraints")
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
            responseCode = "500",
            description =
                "An error has occurred and the request could not be processed at this time",
            content =
                @Content(
                    mediaType = "application/problem+json",
                    schema = @Schema(implementation = ProblemDetails.class)))
      })
  @RequestMapping(
      value = "/role-type-attribute-type-constraints",
      method = RequestMethod.GET,
      produces = "application/json")
  @ResponseStatus(HttpStatus.OK)
  @PreAuthorize("isSecurityDisabled() or isAuthenticated()")
  public List<RoleTypeAttributeTypeConstraint> getRoleTypeAttributeTypeConstraints(
      @Parameter(
              name = "roleType",
              description =
                  "The optional code for the role type to retrieve the role type attribute type constraints for")
          @RequestParam(value = "roleType", required = false)
          String roleType)
      throws InvalidArgumentException, ServiceUnavailableException {
    return partyReferenceService.getRoleTypeAttributeTypeConstraints(roleType);
  }

  /**
   * Retrieve the role type preference type constraints for a specific role type.
   *
   * @param roleType the code for the role type to retrieve the preference constraints for
   * @return the role type preference type constraints
   * @throws InvalidArgumentException if an argument is invalid
   * @throws ServiceUnavailableException if the role type preference type constraints for a specific
   *     role type could not be retrieved
   */
  @Operation(
      summary = "Retrieve the role type preference type constraints",
      description = "Retrieve the role type preference type constraints")
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
            responseCode = "500",
            description =
                "An error has occurred and the request could not be processed at this time",
            content =
                @Content(
                    mediaType = "application/problem+json",
                    schema = @Schema(implementation = ProblemDetails.class)))
      })
  @RequestMapping(
      value = "/role-type-preference-type-constraints",
      method = RequestMethod.GET,
      produces = "application/json")
  @ResponseStatus(HttpStatus.OK)
  @PreAuthorize("isSecurityDisabled() or isAuthenticated()")
  public List<RoleTypePreferenceTypeConstraint> getRoleTypePreferenceTypeConstraints(
      @Parameter(
              name = "roleType",
              description =
                  "The optional code for the role type to retrieve the role type preference type constraints for")
          @RequestParam(value = "roleType", required = false)
          String roleType)
      throws InvalidArgumentException, ServiceUnavailableException {
    return partyReferenceService.getRoleTypePreferenceTypeConstraints(roleType);
  }

  /**
   * Retrieve the role type reference data for a specific locale.
   *
   * @param tenantId the ID for the tenant the role type reference data is specific to
   * @param localeId the Unicode locale identifier for the locale to retrieve the role type
   *     reference data for
   * @return the role type reference data
   * @throws InvalidArgumentException if an argument is invalid
   * @throws ServiceUnavailableException if the role type reference data could not be retrieved
   */
  @Operation(
      summary = "Retrieve the role type reference data for a specific locale",
      description = "Retrieve the role type reference data for a specific locale")
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
            responseCode = "500",
            description =
                "An error has occurred and the request could not be processed at this time",
            content =
                @Content(
                    mediaType = "application/problem+json",
                    schema = @Schema(implementation = ProblemDetails.class)))
      })
  @RequestMapping(value = "/role-types", method = RequestMethod.GET, produces = "application/json")
  @ResponseStatus(HttpStatus.OK)
  @PreAuthorize("isSecurityDisabled() or isAuthenticated()")
  public List<RoleType> getRoleTypes(
      @Parameter(
              name = "Tenant-ID",
              description = "The ID for the tenant the role type reference data is specific to",
              example = "00000000-0000-0000-0000-000000000000")
          @RequestHeader(
              name = "Tenant-ID",
              defaultValue = "00000000-0000-0000-0000-000000000000",
              required = false)
          UUID tenantId,
      @Parameter(
              name = "localeId",
              description =
                  "The Unicode locale identifier for the locale to retrieve the role type reference data for",
              example = IPartyReferenceService.DEFAULT_LOCALE_ID)
          @RequestParam(
              value = "localeId",
              required = false,
              defaultValue = IPartyReferenceService.DEFAULT_LOCALE_ID)
          String localeId)
      throws InvalidArgumentException, ServiceUnavailableException {
    return partyReferenceService.getRoleTypes(tenantId, localeId);
  }

  /**
   * Retrieve the segmentation type reference data for a specific locale.
   *
   * @param tenantId the ID for the tenant the segmentation type reference data is specific to
   * @param localeId the Unicode locale identifier for the locale to retrieve the segmentation type
   *     reference data for
   * @return the segmentation type reference data
   * @throws InvalidArgumentException if an argument is invalid
   * @throws ServiceUnavailableException if the segmentation type reference data could not be
   *     retrieved
   */
  @Operation(
      summary = "Retrieve the segmentation type reference data for a specific locale",
      description = "Retrieve the segmentation type reference data for a specific locale")
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
            responseCode = "500",
            description =
                "An error has occurred and the request could not be processed at this time",
            content =
                @Content(
                    mediaType = "application/problem+json",
                    schema = @Schema(implementation = ProblemDetails.class)))
      })
  @RequestMapping(
      value = "/segmentation-types",
      method = RequestMethod.GET,
      produces = "application/json")
  @ResponseStatus(HttpStatus.OK)
  @PreAuthorize("isSecurityDisabled() or isAuthenticated()")
  public List<SegmentationType> getSegmentationTypes(
      @Parameter(
              name = "Tenant-ID",
              description =
                  "The ID for the tenant the segmentation type reference data is specific to",
              example = "00000000-0000-0000-0000-000000000000")
          @RequestHeader(
              name = "Tenant-ID",
              defaultValue = "00000000-0000-0000-0000-000000000000",
              required = false)
          UUID tenantId,
      @Parameter(
              name = "localeId",
              description =
                  "The Unicode locale identifier for the locale to retrieve the segmentation type reference data for",
              example = IPartyReferenceService.DEFAULT_LOCALE_ID)
          @RequestParam(
              value = "localeId",
              required = false,
              defaultValue = IPartyReferenceService.DEFAULT_LOCALE_ID)
          String localeId)
      throws InvalidArgumentException, ServiceUnavailableException {
    return partyReferenceService.getSegmentationTypes(tenantId, localeId);
  }

  /**
   * Retrieve the segment reference data for a specific locale.
   *
   * @param tenantId the ID for the tenant the segment reference data is specific to
   * @param localeId the Unicode locale identifier for the locale to retrieve the segment reference
   *     data for
   * @return the segment reference data
   * @throws InvalidArgumentException if an argument is invalid
   * @throws ServiceUnavailableException if the segment reference data could not be retrieved
   */
  @Operation(
      summary = "Retrieve the segment reference data for a specific locale",
      description = "Retrieve the segment reference data for a specific locale")
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
            responseCode = "500",
            description =
                "An error has occurred and the request could not be processed at this time",
            content =
                @Content(
                    mediaType = "application/problem+json",
                    schema = @Schema(implementation = ProblemDetails.class)))
      })
  @RequestMapping(value = "/segments", method = RequestMethod.GET, produces = "application/json")
  @ResponseStatus(HttpStatus.OK)
  @PreAuthorize("isSecurityDisabled() or isAuthenticated()")
  public List<Segment> getSegments(
      @Parameter(
              name = "Tenant-ID",
              description = "The ID for the tenant the segment reference data is specific to",
              example = "00000000-0000-0000-0000-000000000000")
          @RequestHeader(
              name = "Tenant-ID",
              defaultValue = "00000000-0000-0000-0000-000000000000",
              required = false)
          UUID tenantId,
      @Parameter(
              name = "localeId",
              description =
                  "The Unicode locale identifier for the locale to retrieve the segment reference data for",
              example = IPartyReferenceService.DEFAULT_LOCALE_ID)
          @RequestParam(
              value = "localeId",
              required = false,
              defaultValue = IPartyReferenceService.DEFAULT_LOCALE_ID)
          String localeId)
      throws InvalidArgumentException, ServiceUnavailableException {
    return partyReferenceService.getSegments(tenantId, localeId);
  }

  /**
   * Retrieve the skill type reference data for a specific locale.
   *
   * @param tenantId the ID for the tenant the skill type reference data is specific to
   * @param localeId the Unicode locale identifier for the locale to retrieve the skill type
   *     reference data for
   * @return the skill type reference data
   * @throws InvalidArgumentException if an argument is invalid
   * @throws ServiceUnavailableException if the skill type reference data could not be retrieved
   */
  @Operation(
      summary = "Retrieve the skill type reference data for a specific locale",
      description = "Retrieve the skill type reference data for a specific locale")
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
            responseCode = "500",
            description =
                "An error has occurred and the request could not be processed at this time",
            content =
                @Content(
                    mediaType = "application/problem+json",
                    schema = @Schema(implementation = ProblemDetails.class)))
      })
  @RequestMapping(value = "/skill-types", method = RequestMethod.GET, produces = "application/json")
  @ResponseStatus(HttpStatus.OK)
  @PreAuthorize("isSecurityDisabled() or isAuthenticated()")
  public List<SkillType> getSkillTypes(
      @Parameter(
              name = "Tenant-ID",
              description = "The ID for the tenant the skill type reference data is specific to",
              example = "00000000-0000-0000-0000-000000000000")
          @RequestHeader(
              name = "Tenant-ID",
              defaultValue = "00000000-0000-0000-0000-000000000000",
              required = false)
          UUID tenantId,
      @Parameter(
              name = "localeId",
              description =
                  "The Unicode locale identifier for the locale to retrieve the skill type reference data for",
              example = IPartyReferenceService.DEFAULT_LOCALE_ID)
          @RequestParam(
              value = "localeId",
              required = false,
              defaultValue = IPartyReferenceService.DEFAULT_LOCALE_ID)
          String localeId)
      throws InvalidArgumentException, ServiceUnavailableException {
    return partyReferenceService.getSkillTypes(tenantId, localeId);
  }

  /**
   * Retrieve the source of funds type reference data for a specific locale.
   *
   * @param tenantId the ID for the tenant the source of funds type reference data is specific to
   * @param localeId the Unicode locale identifier for the locale to retrieve the source of funds
   *     type reference data
   * @return the source of funds type reference data
   * @throws InvalidArgumentException if an argument is invalid
   * @throws ServiceUnavailableException if the source of funds type reference data could not be
   *     retrieved
   */
  @Operation(
      summary = "Retrieve the source of funds type reference data for a specific locale",
      description = "Retrieve the source of funds type reference data for a specific locale")
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
            responseCode = "500",
            description =
                "An error has occurred and the request could not be processed at this time",
            content =
                @Content(
                    mediaType = "application/problem+json",
                    schema = @Schema(implementation = ProblemDetails.class)))
      })
  @RequestMapping(
      value = "/source-of-funds-types",
      method = RequestMethod.GET,
      produces = "application/json")
  @ResponseStatus(HttpStatus.OK)
  @PreAuthorize("isSecurityDisabled() or isAuthenticated()")
  public List<SourceOfFundsType> getSourceOfFundsTypes(
      @Parameter(
              name = "Tenant-ID",
              description =
                  "The ID for the tenant the source of funds type reference data is specific to",
              example = "00000000-0000-0000-0000-000000000000")
          @RequestHeader(
              name = "Tenant-ID",
              defaultValue = "00000000-0000-0000-0000-000000000000",
              required = false)
          UUID tenantId,
      @Parameter(
              name = "localeId",
              description =
                  "The Unicode locale identifier for the locale to retrieve the source of funds type reference data for",
              example = IPartyReferenceService.DEFAULT_LOCALE_ID)
          @RequestParam(
              value = "localeId",
              required = false,
              defaultValue = IPartyReferenceService.DEFAULT_LOCALE_ID)
          String localeId)
      throws InvalidArgumentException, ServiceUnavailableException {
    return partyReferenceService.getSourceOfFundsTypes(tenantId, localeId);
  }

  /**
   * Retrieve the source of wealth type reference data for a specific locale.
   *
   * @param tenantId the ID for the tenant the source of wealth type reference data is specific to
   * @param localeId the Unicode locale identifier for the locale to retrieve the source of wealth
   *     type reference data for
   * @return the source of wealth type reference data
   * @throws InvalidArgumentException if an argument is invalid
   * @throws ServiceUnavailableException if the source of wealth type reference data could not be
   *     retrieved
   */
  @Operation(
      summary = "Retrieve the source of wealth type reference data for a specific locale",
      description = "Retrieve the source of wealth type reference data for a specific locale")
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
            responseCode = "500",
            description =
                "An error has occurred and the request could not be processed at this time",
            content =
                @Content(
                    mediaType = "application/problem+json",
                    schema = @Schema(implementation = ProblemDetails.class)))
      })
  @RequestMapping(
      value = "/source-of-wealth-types",
      method = RequestMethod.GET,
      produces = "application/json")
  @ResponseStatus(HttpStatus.OK)
  @PreAuthorize("isSecurityDisabled() or isAuthenticated()")
  public List<SourceOfWealthType> getSourceOfWealthTypes(
      @Parameter(
              name = "Tenant-ID",
              description =
                  "The ID for the tenant the source of wealth type reference data is specific to",
              example = "00000000-0000-0000-0000-000000000000")
          @RequestHeader(
              name = "Tenant-ID",
              defaultValue = "00000000-0000-0000-0000-000000000000",
              required = false)
          UUID tenantId,
      @Parameter(
              name = "localeId",
              description =
                  "The Unicode locale identifier for the locale to retrieve the source of wealth type reference data for",
              example = IPartyReferenceService.DEFAULT_LOCALE_ID)
          @RequestParam(
              value = "localeId",
              required = false,
              defaultValue = IPartyReferenceService.DEFAULT_LOCALE_ID)
          String localeId)
      throws InvalidArgumentException, ServiceUnavailableException {
    return partyReferenceService.getSourceOfWealthTypes(tenantId, localeId);
  }

  /**
   * Retrieve the status type category reference data for a specific locale.
   *
   * @param tenantId the ID for the tenant the status type category reference data is specific to
   * @param localeId the Unicode locale identifier for the locale to retrieve the status type
   *     category reference data for
   * @return the status type category reference data
   * @throws InvalidArgumentException if an argument is invalid
   * @throws ServiceUnavailableException if the status type category reference data could not be
   *     retrieved
   */
  @Operation(
      summary = "Retrieve the status type category reference data for a specific locale",
      description = "Retrieve the status type category reference data for a specific locale")
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
            responseCode = "500",
            description =
                "An error has occurred and the request could not be processed at this time",
            content =
                @Content(
                    mediaType = "application/problem+json",
                    schema = @Schema(implementation = ProblemDetails.class)))
      })
  @RequestMapping(
      value = "/status-type-categories",
      method = RequestMethod.GET,
      produces = "application/json")
  @ResponseStatus(HttpStatus.OK)
  @PreAuthorize("isSecurityDisabled() or isAuthenticated()")
  public List<StatusTypeCategory> getStatusTypeCategories(
      @Parameter(
              name = "Tenant-ID",
              description =
                  "The ID for the tenant the status type category reference data is specific to",
              example = "00000000-0000-0000-0000-000000000000")
          @RequestHeader(
              name = "Tenant-ID",
              defaultValue = "00000000-0000-0000-0000-000000000000",
              required = false)
          UUID tenantId,
      @Parameter(
              name = "localeId",
              description =
                  "The Unicode locale identifier for the locale to retrieve the status type category reference data for",
              example = IPartyReferenceService.DEFAULT_LOCALE_ID)
          @RequestParam(
              value = "localeId",
              required = false,
              defaultValue = IPartyReferenceService.DEFAULT_LOCALE_ID)
          String localeId)
      throws InvalidArgumentException, ServiceUnavailableException {
    return partyReferenceService.getStatusTypeCategories(tenantId, localeId);
  }

  /**
   * Retrieve the status type reference data for a specific locale.
   *
   * @param tenantId the ID for the tenant the status type reference data is specific to
   * @param localeId the Unicode locale identifier for the locale to retrieve the status type
   *     reference data for
   * @return the status type reference data
   * @throws InvalidArgumentException if an argument is invalid
   * @throws ServiceUnavailableException if the status type reference data could not be retrieved
   */
  @Operation(
      summary = "Retrieve the status type reference data for a specific locale",
      description = "Retrieve the status type reference data for a specific locale")
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
            responseCode = "500",
            description =
                "An error has occurred and the request could not be processed at this time",
            content =
                @Content(
                    mediaType = "application/problem+json",
                    schema = @Schema(implementation = ProblemDetails.class)))
      })
  @RequestMapping(
      value = "/status-types",
      method = RequestMethod.GET,
      produces = "application/json")
  @ResponseStatus(HttpStatus.OK)
  @PreAuthorize("isSecurityDisabled() or isAuthenticated()")
  public List<StatusType> getStatusTypes(
      @Parameter(
              name = "Tenant-ID",
              description = "The ID for the tenant the status type reference data is specific to",
              example = "00000000-0000-0000-0000-000000000000")
          @RequestHeader(
              name = "Tenant-ID",
              defaultValue = "00000000-0000-0000-0000-000000000000",
              required = false)
          UUID tenantId,
      @Parameter(
              name = "localeId",
              description =
                  "The Unicode locale identifier for the locale to retrieve the status type reference data for",
              example = IPartyReferenceService.DEFAULT_LOCALE_ID)
          @RequestParam(
              value = "localeId",
              required = false,
              defaultValue = IPartyReferenceService.DEFAULT_LOCALE_ID)
          String localeId)
      throws InvalidArgumentException, ServiceUnavailableException {
    return partyReferenceService.getStatusTypes(tenantId, localeId);
  }

  /**
   * Retrieve the tax number type reference data for a specific locale.
   *
   * @param tenantId the ID for the tenant the tax number type reference data is specific to
   * @param localeId the Unicode locale identifier for the locale to retrieve the tax number type
   *     reference data for
   * @return the tax number type reference data
   * @throws InvalidArgumentException if an argument is invalid
   * @throws ServiceUnavailableException if the tax number type reference data could not be
   *     retrieved
   */
  @Operation(
      summary = "Retrieve the tax number type reference data for a specific locale",
      description = "Retrieve the tax number type reference data for a specific locale")
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
            responseCode = "500",
            description =
                "An error has occurred and the request could not be processed at this time",
            content =
                @Content(
                    mediaType = "application/problem+json",
                    schema = @Schema(implementation = ProblemDetails.class)))
      })
  @RequestMapping(
      value = "/tax-number-types",
      method = RequestMethod.GET,
      produces = "application/json")
  @ResponseStatus(HttpStatus.OK)
  @PreAuthorize("isSecurityDisabled() or isAuthenticated()")
  public List<TaxNumberType> getTaxNumberTypes(
      @Parameter(
              name = "Tenant-ID",
              description =
                  "The ID for the tenant the tax number type reference data is specific to",
              example = "00000000-0000-0000-0000-000000000000")
          @RequestHeader(
              name = "Tenant-ID",
              defaultValue = "00000000-0000-0000-0000-000000000000",
              required = false)
          UUID tenantId,
      @Parameter(
              name = "localeId",
              description =
                  "The Unicode locale identifier for the locale to retrieve the tax number type reference data for",
              example = IPartyReferenceService.DEFAULT_LOCALE_ID)
          @RequestParam(
              value = "localeId",
              required = false,
              defaultValue = IPartyReferenceService.DEFAULT_LOCALE_ID)
          String localeId)
      throws InvalidArgumentException, ServiceUnavailableException {
    return partyReferenceService.getTaxNumberTypes(tenantId, localeId);
  }

  /**
   * Retrieve the times to contact reference data for a specific locale.
   *
   * @param tenantId the ID for the tenant the times to contact reference data is specific to
   * @param localeId the Unicode locale identifier for the locale to retrieve the times to contact
   *     reference data for
   * @return the times to contact reference data
   * @throws InvalidArgumentException if an argument is invalid
   * @throws ServiceUnavailableException if the times to contact reference data could not be
   *     retrieved
   */
  @Operation(
      summary = "Retrieve the times to contact reference data for a specific locale",
      description = "Retrieve the times to contact reference data for a specific locale")
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
            responseCode = "500",
            description =
                "An error has occurred and the request could not be processed at this time",
            content =
                @Content(
                    mediaType = "application/problem+json",
                    schema = @Schema(implementation = ProblemDetails.class)))
      })
  @RequestMapping(
      value = "/times-to-contact",
      method = RequestMethod.GET,
      produces = "application/json")
  @ResponseStatus(HttpStatus.OK)
  @PreAuthorize("isSecurityDisabled() or isAuthenticated()")
  public List<TimeToContact> getTimesToContact(
      @Parameter(
              name = "Tenant-ID",
              description =
                  "The ID for the tenant the times to contact reference data is specific to",
              example = "00000000-0000-0000-0000-000000000000")
          @RequestHeader(
              name = "Tenant-ID",
              defaultValue = "00000000-0000-0000-0000-000000000000",
              required = false)
          UUID tenantId,
      @Parameter(
              name = "localeId",
              description =
                  "The Unicode locale identifier for the locale to retrieve the times to contact reference data for",
              example = IPartyReferenceService.DEFAULT_LOCALE_ID)
          @RequestParam(
              value = "localeId",
              required = false,
              defaultValue = IPartyReferenceService.DEFAULT_LOCALE_ID)
          String localeId)
      throws InvalidArgumentException, ServiceUnavailableException {
    return partyReferenceService.getTimesToContact(tenantId, localeId);
  }

  /**
   * Retrieve the title reference data for a specific locale.
   *
   * @param tenantId the ID for the tenant the title reference data is specific to
   * @param localeId the Unicode locale identifier for the locale to retrieve the title reference
   *     data for
   * @return the title reference data
   * @throws InvalidArgumentException if an argument is invalid
   * @throws ServiceUnavailableException if the title reference data could not be retrieved
   */
  @Operation(
      summary = "Retrieve the title reference data for a specific locale",
      description = "Retrieve the title reference data for a specific locale")
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
            responseCode = "500",
            description =
                "An error has occurred and the request could not be processed at this time",
            content =
                @Content(
                    mediaType = "application/problem+json",
                    schema = @Schema(implementation = ProblemDetails.class)))
      })
  @RequestMapping(value = "/titles", method = RequestMethod.GET, produces = "application/json")
  @ResponseStatus(HttpStatus.OK)
  @PreAuthorize("isSecurityDisabled() or isAuthenticated()")
  public List<Title> getTitles(
      @Parameter(
              name = "Tenant-ID",
              description = "The ID for the tenant the title reference data is specific to",
              example = "00000000-0000-0000-0000-000000000000")
          @RequestHeader(
              name = "Tenant-ID",
              defaultValue = "00000000-0000-0000-0000-000000000000",
              required = false)
          UUID tenantId,
      @Parameter(
              name = "localeId",
              description =
                  "The Unicode locale identifier for the locale to retrieve the title reference data for",
              example = IPartyReferenceService.DEFAULT_LOCALE_ID)
          @RequestParam(
              value = "localeId",
              required = false,
              defaultValue = IPartyReferenceService.DEFAULT_LOCALE_ID)
          String localeId)
      throws InvalidArgumentException, ServiceUnavailableException {
    return partyReferenceService.getTitles(tenantId, localeId);
  }
}
