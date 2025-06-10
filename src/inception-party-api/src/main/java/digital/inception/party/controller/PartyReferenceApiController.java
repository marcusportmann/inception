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

package digital.inception.party.controller;

import digital.inception.core.api.ProblemDetails;
import digital.inception.core.exception.InvalidArgumentException;
import digital.inception.core.exception.ServiceUnavailableException;
import digital.inception.party.model.AssociationPropertyType;
import digital.inception.party.model.AssociationType;
import digital.inception.party.model.AttributeType;
import digital.inception.party.model.AttributeTypeCategory;
import digital.inception.party.model.ConsentType;
import digital.inception.party.model.ContactMechanismPurpose;
import digital.inception.party.model.ContactMechanismRole;
import digital.inception.party.model.ContactMechanismType;
import digital.inception.party.model.EmploymentStatus;
import digital.inception.party.model.EmploymentType;
import digital.inception.party.model.ExternalReferenceType;
import digital.inception.party.model.FieldOfStudy;
import digital.inception.party.model.Gender;
import digital.inception.party.model.IdentificationType;
import digital.inception.party.model.IndustryClassification;
import digital.inception.party.model.IndustryClassificationCategory;
import digital.inception.party.model.IndustryClassificationSystem;
import digital.inception.party.model.LinkType;
import digital.inception.party.model.LockType;
import digital.inception.party.model.LockTypeCategory;
import digital.inception.party.model.MandataryRole;
import digital.inception.party.model.MandatePropertyType;
import digital.inception.party.model.MandateType;
import digital.inception.party.model.MaritalStatus;
import digital.inception.party.model.MarriageType;
import digital.inception.party.model.NextOfKinType;
import digital.inception.party.model.Occupation;
import digital.inception.party.model.PhysicalAddressPurpose;
import digital.inception.party.model.PhysicalAddressRole;
import digital.inception.party.model.PhysicalAddressType;
import digital.inception.party.model.PreferenceType;
import digital.inception.party.model.PreferenceTypeCategory;
import digital.inception.party.model.QualificationType;
import digital.inception.party.model.Race;
import digital.inception.party.model.ResidencePermitType;
import digital.inception.party.model.ResidencyStatus;
import digital.inception.party.model.ResidentialType;
import digital.inception.party.model.RolePurpose;
import digital.inception.party.model.RoleType;
import digital.inception.party.model.RoleTypeAttributeTypeConstraint;
import digital.inception.party.model.RoleTypePreferenceTypeConstraint;
import digital.inception.party.model.Segment;
import digital.inception.party.model.SegmentationType;
import digital.inception.party.model.SkillType;
import digital.inception.party.model.SourceOfFundsType;
import digital.inception.party.model.SourceOfWealthType;
import digital.inception.party.model.StatusType;
import digital.inception.party.model.StatusTypeCategory;
import digital.inception.party.model.TaxNumberType;
import digital.inception.party.model.TimeToContact;
import digital.inception.party.model.Title;
import digital.inception.party.service.PartyReferenceService;
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
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * The {@code PartyReferenceApiController} interface.
 *
 * @author Marcus Portmann
 */
@Tag(name = "Party Reference")
@RequestMapping(value = "/api/party/reference")
// @el (isSecurityDisabled: digital.inception.api.ApiSecurityExpressionRoot.isSecurityEnabled)
public interface PartyReferenceApiController {

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
        @ApiResponse(
            responseCode = "200",
            description = "The association property type reference data was retrieved"),
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
      value = "/association-property-types",
      method = RequestMethod.GET,
      produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.OK)
  @PreAuthorize("isSecurityDisabled() or isAuthenticated()")
  List<AssociationPropertyType> getAssociationPropertyTypes(
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
              example = PartyReferenceService.DEFAULT_LOCALE_ID)
          @RequestParam(
              value = "localeId",
              required = false,
              defaultValue = PartyReferenceService.DEFAULT_LOCALE_ID)
          String localeId)
      throws InvalidArgumentException, ServiceUnavailableException;

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
        @ApiResponse(
            responseCode = "200",
            description = "The association type reference data was retrieved"),
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
      value = "/association-types",
      method = RequestMethod.GET,
      produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.OK)
  @PreAuthorize("isSecurityDisabled() or isAuthenticated()")
  List<AssociationType> getAssociationTypes(
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
              example = PartyReferenceService.DEFAULT_LOCALE_ID)
          @RequestParam(
              value = "localeId",
              required = false,
              defaultValue = PartyReferenceService.DEFAULT_LOCALE_ID)
          String localeId)
      throws InvalidArgumentException, ServiceUnavailableException;

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
        @ApiResponse(
            responseCode = "200",
            description = "The attribute type category reference data was retrieved"),
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
      value = "/attribute-type-categories",
      method = RequestMethod.GET,
      produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.OK)
  @PreAuthorize("isSecurityDisabled() or isAuthenticated()")
  List<AttributeTypeCategory> getAttributeTypeCategories(
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
              example = PartyReferenceService.DEFAULT_LOCALE_ID)
          @RequestParam(
              value = "localeId",
              required = false,
              defaultValue = PartyReferenceService.DEFAULT_LOCALE_ID)
          String localeId)
      throws InvalidArgumentException, ServiceUnavailableException;

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
        @ApiResponse(
            responseCode = "200",
            description = "The attribute type reference data was retrieved"),
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
      value = "/attribute-types",
      method = RequestMethod.GET,
      produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.OK)
  @PreAuthorize("isSecurityDisabled() or isAuthenticated()")
  List<AttributeType> getAttributeTypes(
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
              example = PartyReferenceService.DEFAULT_LOCALE_ID)
          @RequestParam(
              value = "localeId",
              required = false,
              defaultValue = PartyReferenceService.DEFAULT_LOCALE_ID)
          String localeId)
      throws InvalidArgumentException, ServiceUnavailableException;

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
        @ApiResponse(
            responseCode = "200",
            description = "The consent type reference data was retrieved"),
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
      value = "/consent-types",
      method = RequestMethod.GET,
      produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.OK)
  @PreAuthorize("isSecurityDisabled() or isAuthenticated()")
  List<ConsentType> getConsentTypes(
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
              example = PartyReferenceService.DEFAULT_LOCALE_ID)
          @RequestParam(
              value = "localeId",
              required = false,
              defaultValue = PartyReferenceService.DEFAULT_LOCALE_ID)
          String localeId)
      throws InvalidArgumentException, ServiceUnavailableException;

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
        @ApiResponse(
            responseCode = "200",
            description = "The contact mechanism purpose reference data was retrieved"),
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
      value = "/contact-mechanism-purposes",
      method = RequestMethod.GET,
      produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.OK)
  @PreAuthorize("isSecurityDisabled() or isAuthenticated()")
  List<ContactMechanismPurpose> getContactMechanismPurposes(
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
              example = PartyReferenceService.DEFAULT_LOCALE_ID)
          @RequestParam(
              value = "localeId",
              required = false,
              defaultValue = PartyReferenceService.DEFAULT_LOCALE_ID)
          String localeId)
      throws InvalidArgumentException, ServiceUnavailableException;

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
        @ApiResponse(
            responseCode = "200",
            description = "The contact mechanism role reference data was retrieved"),
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
      value = "/contact-mechanism-roles",
      method = RequestMethod.GET,
      produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.OK)
  @PreAuthorize("isSecurityDisabled() or isAuthenticated()")
  List<ContactMechanismRole> getContactMechanismRoles(
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
              example = PartyReferenceService.DEFAULT_LOCALE_ID)
          @RequestParam(
              value = "localeId",
              required = false,
              defaultValue = PartyReferenceService.DEFAULT_LOCALE_ID)
          String localeId)
      throws InvalidArgumentException, ServiceUnavailableException;

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
        @ApiResponse(
            responseCode = "200",
            description = "The contact mechanism type reference data was retrieved"),
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
      value = "/contact-mechanism-types",
      method = RequestMethod.GET,
      produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.OK)
  @PreAuthorize("isSecurityDisabled() or isAuthenticated()")
  List<ContactMechanismType> getContactMechanismTypes(
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
              example = PartyReferenceService.DEFAULT_LOCALE_ID)
          @RequestParam(
              value = "localeId",
              required = false,
              defaultValue = PartyReferenceService.DEFAULT_LOCALE_ID)
          String localeId)
      throws InvalidArgumentException, ServiceUnavailableException;

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
        @ApiResponse(
            responseCode = "200",
            description = "The employment status reference data was retrieved"),
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
      value = "/employment-statuses",
      method = RequestMethod.GET,
      produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.OK)
  @PreAuthorize("isSecurityDisabled() or isAuthenticated()")
  List<EmploymentStatus> getEmploymentStatuses(
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
              example = PartyReferenceService.DEFAULT_LOCALE_ID)
          @RequestParam(
              value = "localeId",
              required = false,
              defaultValue = PartyReferenceService.DEFAULT_LOCALE_ID)
          String localeId)
      throws InvalidArgumentException, ServiceUnavailableException;

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
        @ApiResponse(
            responseCode = "200",
            description = "The employment type reference data was retrieved"),
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
      value = "/employment-types",
      method = RequestMethod.GET,
      produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.OK)
  @PreAuthorize("isSecurityDisabled() or isAuthenticated()")
  List<EmploymentType> getEmploymentTypes(
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
              example = PartyReferenceService.DEFAULT_LOCALE_ID)
          @RequestParam(
              value = "localeId",
              required = false,
              defaultValue = PartyReferenceService.DEFAULT_LOCALE_ID)
          String localeId)
      throws InvalidArgumentException, ServiceUnavailableException;

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
        @ApiResponse(
            responseCode = "200",
            description = "The external reference type reference data was retrieved"),
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
  @PreAuthorize("isSecurityDisabled() or isAuthenticated()")
  List<ExternalReferenceType> getExternalReferenceTypes(
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
              example = PartyReferenceService.DEFAULT_LOCALE_ID)
          @RequestParam(
              value = "localeId",
              required = false,
              defaultValue = PartyReferenceService.DEFAULT_LOCALE_ID)
          String localeId)
      throws InvalidArgumentException, ServiceUnavailableException;

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
        @ApiResponse(
            responseCode = "200",
            description = "The fields of study reference data was retrieved"),
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
      value = "/fields-of-study",
      method = RequestMethod.GET,
      produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.OK)
  @PreAuthorize("isSecurityDisabled() or isAuthenticated()")
  List<FieldOfStudy> getFieldsOfStudy(
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
              example = PartyReferenceService.DEFAULT_LOCALE_ID)
          @RequestParam(
              value = "localeId",
              required = false,
              defaultValue = PartyReferenceService.DEFAULT_LOCALE_ID)
          String localeId)
      throws InvalidArgumentException, ServiceUnavailableException;

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
        @ApiResponse(responseCode = "200", description = "The gender reference data was retrieved"),
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
      value = "/genders",
      method = RequestMethod.GET,
      produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.OK)
  @PreAuthorize("isSecurityDisabled() or isAuthenticated()")
  List<Gender> getGenders(
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
              example = PartyReferenceService.DEFAULT_LOCALE_ID)
          @RequestParam(
              value = "localeId",
              required = false,
              defaultValue = PartyReferenceService.DEFAULT_LOCALE_ID)
          String localeId)
      throws InvalidArgumentException, ServiceUnavailableException;

  /**
   * Retrieve the identification type reference data for a specific locale.
   *
   * @param tenantId the ID for the tenant the identification type reference data is specific to
   * @param localeId the Unicode locale identifier for the locale to retrieve the identification
   *     type reference data for
   * @return the identification type reference data
   * @throws InvalidArgumentException if an argument is invalid
   * @throws ServiceUnavailableException if the identification type reference data could not be
   *     retrieved
   */
  @Operation(
      summary = "Retrieve the identification type reference data for a specific locale",
      description = "Retrieve the identification type reference data for a specific locale")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "200",
            description = "The identification type reference data was retrieved"),
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
      value = "/identification-types",
      method = RequestMethod.GET,
      produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.OK)
  @PreAuthorize("isSecurityDisabled() or isAuthenticated()")
  List<IdentificationType> getIdentificationTypes(
      @Parameter(
              name = "Tenant-ID",
              description =
                  "The ID for the tenant the identification type reference data is specific to",
              example = "00000000-0000-0000-0000-000000000000")
          @RequestHeader(
              name = "Tenant-ID",
              defaultValue = "00000000-0000-0000-0000-000000000000",
              required = false)
          UUID tenantId,
      @Parameter(
              name = "localeId",
              description =
                  "The Unicode locale identifier for the locale to retrieve the identification type reference data for",
              example = PartyReferenceService.DEFAULT_LOCALE_ID)
          @RequestParam(
              value = "localeId",
              required = false,
              defaultValue = PartyReferenceService.DEFAULT_LOCALE_ID)
          String localeId)
      throws InvalidArgumentException, ServiceUnavailableException;

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
        @ApiResponse(
            responseCode = "200",
            description = "The industry classification category reference data was retrieved"),
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
      value = "/industry-classification-categories",
      method = RequestMethod.GET,
      produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.OK)
  @PreAuthorize("isSecurityDisabled() or isAuthenticated()")
  List<IndustryClassificationCategory> getIndustryClassificationCategories(
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
              example = PartyReferenceService.DEFAULT_LOCALE_ID)
          @RequestParam(
              value = "localeId",
              required = false,
              defaultValue = PartyReferenceService.DEFAULT_LOCALE_ID)
          String localeId)
      throws InvalidArgumentException, ServiceUnavailableException;

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
        @ApiResponse(
            responseCode = "200",
            description = "The industry classification system reference data was retrieved"),
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
      value = "/industry-classification-systems",
      method = RequestMethod.GET,
      produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.OK)
  @PreAuthorize("isSecurityDisabled() or isAuthenticated()")
  List<IndustryClassificationSystem> getIndustryClassificationSystems(
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
              example = PartyReferenceService.DEFAULT_LOCALE_ID)
          @RequestParam(
              value = "localeId",
              required = false,
              defaultValue = PartyReferenceService.DEFAULT_LOCALE_ID)
          String localeId)
      throws InvalidArgumentException, ServiceUnavailableException;

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
        @ApiResponse(
            responseCode = "200",
            description = "The industry classification reference data was retrieved"),
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
      value = "/industry-classifications",
      method = RequestMethod.GET,
      produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.OK)
  @PreAuthorize("isSecurityDisabled() or isAuthenticated()")
  List<IndustryClassification> getIndustryClassifications(
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
              example = PartyReferenceService.DEFAULT_LOCALE_ID)
          @RequestParam(
              value = "localeId",
              required = false,
              defaultValue = PartyReferenceService.DEFAULT_LOCALE_ID)
          String localeId)
      throws InvalidArgumentException, ServiceUnavailableException;

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
        @ApiResponse(
            responseCode = "200",
            description = "The link type reference data was retrieved"),
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
      value = "/link-types",
      method = RequestMethod.GET,
      produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.OK)
  @PreAuthorize("isSecurityDisabled() or isAuthenticated()")
  List<LinkType> getLinkTypes(
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
              example = PartyReferenceService.DEFAULT_LOCALE_ID)
          @RequestParam(
              value = "localeId",
              required = false,
              defaultValue = PartyReferenceService.DEFAULT_LOCALE_ID)
          String localeId)
      throws InvalidArgumentException, ServiceUnavailableException;

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
        @ApiResponse(
            responseCode = "200",
            description = "The lock type category reference data was retrieved"),
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
      value = "/lock-type-categories",
      method = RequestMethod.GET,
      produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.OK)
  @PreAuthorize("isSecurityDisabled() or isAuthenticated()")
  List<LockTypeCategory> getLockTypeCategories(
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
              example = PartyReferenceService.DEFAULT_LOCALE_ID)
          @RequestParam(
              value = "localeId",
              required = false,
              defaultValue = PartyReferenceService.DEFAULT_LOCALE_ID)
          String localeId)
      throws InvalidArgumentException, ServiceUnavailableException;

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
        @ApiResponse(
            responseCode = "200",
            description = "The lock type reference data was retrieved"),
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
      value = "/lock-types",
      method = RequestMethod.GET,
      produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.OK)
  @PreAuthorize("isSecurityDisabled() or isAuthenticated()")
  List<LockType> getLockTypes(
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
              example = PartyReferenceService.DEFAULT_LOCALE_ID)
          @RequestParam(
              value = "localeId",
              required = false,
              defaultValue = PartyReferenceService.DEFAULT_LOCALE_ID)
          String localeId)
      throws InvalidArgumentException, ServiceUnavailableException;

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
        @ApiResponse(
            responseCode = "200",
            description = "The mandatary role reference data was retrieved"),
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
      value = "/mandatary-types",
      method = RequestMethod.GET,
      produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.OK)
  @PreAuthorize("isSecurityDisabled() or isAuthenticated()")
  List<MandataryRole> getMandataryRoles(
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
              example = PartyReferenceService.DEFAULT_LOCALE_ID)
          @RequestParam(
              value = "localeId",
              required = false,
              defaultValue = PartyReferenceService.DEFAULT_LOCALE_ID)
          String localeId)
      throws InvalidArgumentException, ServiceUnavailableException;

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
        @ApiResponse(
            responseCode = "200",
            description = "The mandate property type reference data was retrieved"),
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
      value = "/mandate-property-types",
      method = RequestMethod.GET,
      produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.OK)
  @PreAuthorize("isSecurityDisabled() or isAuthenticated()")
  List<MandatePropertyType> getMandatePropertyTypes(
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
              example = PartyReferenceService.DEFAULT_LOCALE_ID)
          @RequestParam(
              value = "localeId",
              required = false,
              defaultValue = PartyReferenceService.DEFAULT_LOCALE_ID)
          String localeId)
      throws InvalidArgumentException, ServiceUnavailableException;

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
        @ApiResponse(
            responseCode = "200",
            description = "The mandate type reference data was retrieved"),
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
      value = "/mandate-types",
      method = RequestMethod.GET,
      produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.OK)
  @PreAuthorize("isSecurityDisabled() or isAuthenticated()")
  List<MandateType> getMandateTypes(
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
              example = PartyReferenceService.DEFAULT_LOCALE_ID)
          @RequestParam(
              value = "localeId",
              required = false,
              defaultValue = PartyReferenceService.DEFAULT_LOCALE_ID)
          String localeId)
      throws InvalidArgumentException, ServiceUnavailableException;

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
        @ApiResponse(
            responseCode = "200",
            description = "The marital status reference data was retrieved"),
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
      value = "/marital-statuses",
      method = RequestMethod.GET,
      produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.OK)
  @PreAuthorize("isSecurityDisabled() or isAuthenticated()")
  List<MaritalStatus> getMaritalStatuses(
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
              example = PartyReferenceService.DEFAULT_LOCALE_ID)
          @RequestParam(
              value = "localeId",
              required = false,
              defaultValue = PartyReferenceService.DEFAULT_LOCALE_ID)
          String localeId)
      throws InvalidArgumentException, ServiceUnavailableException;

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
        @ApiResponse(
            responseCode = "200",
            description = "The marriage type reference data was retrieved"),
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
      value = "/marriage-types",
      method = RequestMethod.GET,
      produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.OK)
  @PreAuthorize("isSecurityDisabled() or isAuthenticated()")
  List<MarriageType> getMarriageTypes(
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
              example = PartyReferenceService.DEFAULT_LOCALE_ID)
          @RequestParam(
              value = "localeId",
              required = false,
              defaultValue = PartyReferenceService.DEFAULT_LOCALE_ID)
          String localeId)
      throws InvalidArgumentException, ServiceUnavailableException;

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
        @ApiResponse(
            responseCode = "200",
            description = "The next of kin type reference data was retrieved"),
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
      value = "/next-of-kin-types",
      method = RequestMethod.GET,
      produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.OK)
  @PreAuthorize("isSecurityDisabled() or isAuthenticated()")
  List<NextOfKinType> getNextOfKinTypes(
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
              example = PartyReferenceService.DEFAULT_LOCALE_ID)
          @RequestParam(
              value = "localeId",
              required = false,
              defaultValue = PartyReferenceService.DEFAULT_LOCALE_ID)
          String localeId)
      throws InvalidArgumentException, ServiceUnavailableException;

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
        @ApiResponse(
            responseCode = "200",
            description = "The occupation reference data was retrieved"),
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
      value = "/occupations",
      method = RequestMethod.GET,
      produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.OK)
  @PreAuthorize("isSecurityDisabled() or isAuthenticated()")
  List<Occupation> getOccupations(
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
              example = PartyReferenceService.DEFAULT_LOCALE_ID)
          @RequestParam(
              value = "localeId",
              required = false,
              defaultValue = PartyReferenceService.DEFAULT_LOCALE_ID)
          String localeId)
      throws InvalidArgumentException, ServiceUnavailableException;

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
        @ApiResponse(
            responseCode = "200",
            description = "The physical address purpose reference data was retrieved"),
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
      value = "/physical-address-purposes",
      method = RequestMethod.GET,
      produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.OK)
  @PreAuthorize("isSecurityDisabled() or isAuthenticated()")
  List<PhysicalAddressPurpose> getPhysicalAddressPurposes(
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
              example = PartyReferenceService.DEFAULT_LOCALE_ID)
          @RequestParam(
              value = "localeId",
              required = false,
              defaultValue = PartyReferenceService.DEFAULT_LOCALE_ID)
          String localeId)
      throws InvalidArgumentException, ServiceUnavailableException;

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
        @ApiResponse(
            responseCode = "200",
            description = "The physical address role reference data was retrieved"),
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
      value = "/physical-address-roles",
      method = RequestMethod.GET,
      produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.OK)
  @PreAuthorize("isSecurityDisabled() or isAuthenticated()")
  List<PhysicalAddressRole> getPhysicalAddressRoles(
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
              example = PartyReferenceService.DEFAULT_LOCALE_ID)
          @RequestParam(
              value = "localeId",
              required = false,
              defaultValue = PartyReferenceService.DEFAULT_LOCALE_ID)
          String localeId)
      throws InvalidArgumentException, ServiceUnavailableException;

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
        @ApiResponse(
            responseCode = "200",
            description = "The physical address type reference data was retrieved"),
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
      value = "/physical-address-types",
      method = RequestMethod.GET,
      produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.OK)
  @PreAuthorize("isSecurityDisabled() or isAuthenticated()")
  List<PhysicalAddressType> getPhysicalAddressTypes(
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
              example = PartyReferenceService.DEFAULT_LOCALE_ID)
          @RequestParam(
              value = "localeId",
              required = false,
              defaultValue = PartyReferenceService.DEFAULT_LOCALE_ID)
          String localeId)
      throws InvalidArgumentException, ServiceUnavailableException;

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
        @ApiResponse(
            responseCode = "200",
            description = "The preference type category reference data was retrieved"),
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
      value = "/preference-type-categories",
      method = RequestMethod.GET,
      produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.OK)
  @PreAuthorize("isSecurityDisabled() or isAuthenticated()")
  List<PreferenceTypeCategory> getPreferenceTypeCategories(
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
              example = PartyReferenceService.DEFAULT_LOCALE_ID)
          @RequestParam(
              value = "localeId",
              required = false,
              defaultValue = PartyReferenceService.DEFAULT_LOCALE_ID)
          String localeId)
      throws InvalidArgumentException, ServiceUnavailableException;

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
        @ApiResponse(
            responseCode = "200",
            description = "The preference type reference data was retrieved"),
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
      value = "/preference-types",
      method = RequestMethod.GET,
      produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.OK)
  @PreAuthorize("isSecurityDisabled() or isAuthenticated()")
  List<PreferenceType> getPreferenceTypes(
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
              example = PartyReferenceService.DEFAULT_LOCALE_ID)
          @RequestParam(
              value = "localeId",
              required = false,
              defaultValue = PartyReferenceService.DEFAULT_LOCALE_ID)
          String localeId)
      throws InvalidArgumentException, ServiceUnavailableException;

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
        @ApiResponse(
            responseCode = "200",
            description = "The qualification type reference data was retrieved"),
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
      value = "/qualification-types",
      method = RequestMethod.GET,
      produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.OK)
  @PreAuthorize("isSecurityDisabled() or isAuthenticated()")
  List<QualificationType> getQualificationTypes(
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
              example = PartyReferenceService.DEFAULT_LOCALE_ID)
          @RequestParam(
              value = "localeId",
              required = false,
              defaultValue = PartyReferenceService.DEFAULT_LOCALE_ID)
          String localeId)
      throws InvalidArgumentException, ServiceUnavailableException;

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
        @ApiResponse(responseCode = "200", description = "The race reference data was retrieved"),
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
      value = "/races",
      method = RequestMethod.GET,
      produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.OK)
  @PreAuthorize("isSecurityDisabled() or isAuthenticated()")
  List<Race> getRaces(
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
              example = PartyReferenceService.DEFAULT_LOCALE_ID)
          @RequestParam(
              value = "localeId",
              required = false,
              defaultValue = PartyReferenceService.DEFAULT_LOCALE_ID)
          String localeId)
      throws InvalidArgumentException, ServiceUnavailableException;

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
        @ApiResponse(
            responseCode = "200",
            description = "The residence permit type reference data was retrieved"),
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
      value = "/residence-permit-types",
      method = RequestMethod.GET,
      produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.OK)
  @PreAuthorize("isSecurityDisabled() or isAuthenticated()")
  List<ResidencePermitType> getResidencePermitTypes(
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
              example = PartyReferenceService.DEFAULT_LOCALE_ID)
          @RequestParam(
              value = "localeId",
              required = false,
              defaultValue = PartyReferenceService.DEFAULT_LOCALE_ID)
          String localeId)
      throws InvalidArgumentException, ServiceUnavailableException;

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
        @ApiResponse(
            responseCode = "200",
            description = "The residency status reference data was retrieved"),
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
      value = "/residency-statuses",
      method = RequestMethod.GET,
      produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.OK)
  @PreAuthorize("isSecurityDisabled() or isAuthenticated()")
  List<ResidencyStatus> getResidencyStatuses(
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
              example = PartyReferenceService.DEFAULT_LOCALE_ID)
          @RequestParam(
              value = "localeId",
              required = false,
              defaultValue = PartyReferenceService.DEFAULT_LOCALE_ID)
          String localeId)
      throws InvalidArgumentException, ServiceUnavailableException;

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
        @ApiResponse(
            responseCode = "200",
            description = "The residential type reference data was retrieved"),
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
      value = "/residential-types",
      method = RequestMethod.GET,
      produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.OK)
  @PreAuthorize("isSecurityDisabled() or isAuthenticated()")
  List<ResidentialType> getResidentialTypes(
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
              example = PartyReferenceService.DEFAULT_LOCALE_ID)
          @RequestParam(
              value = "localeId",
              required = false,
              defaultValue = PartyReferenceService.DEFAULT_LOCALE_ID)
          String localeId)
      throws InvalidArgumentException, ServiceUnavailableException;

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
        @ApiResponse(
            responseCode = "200",
            description = "The role purpose reference data was retrieved"),
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
      value = "/role-purposes",
      method = RequestMethod.GET,
      produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.OK)
  @PreAuthorize("isSecurityDisabled() or isAuthenticated()")
  List<RolePurpose> getRolePurposes(
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
              example = PartyReferenceService.DEFAULT_LOCALE_ID)
          @RequestParam(
              value = "localeId",
              required = false,
              defaultValue = PartyReferenceService.DEFAULT_LOCALE_ID)
          String localeId)
      throws InvalidArgumentException, ServiceUnavailableException;

  /**
   * Retrieve the role type attribute type constraint for a specific role type.
   *
   * @param roleType the code for the role type to retrieve the attribute constraint for
   * @return the role type attribute type constraint
   * @throws InvalidArgumentException if an argument is invalid
   * @throws ServiceUnavailableException if the role type attribute type constraint for a specific
   *     role type could not be retrieved
   */
  @Operation(
      summary = "Retrieve the role type attribute type constraint",
      description = "Retrieve the role type attribute type constraint")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "200",
            description = "The role type attribute type constraint was retrieved"),
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
      value = "/role-type-attribute-type-constraints",
      method = RequestMethod.GET,
      produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.OK)
  @PreAuthorize("isSecurityDisabled() or isAuthenticated()")
  List<RoleTypeAttributeTypeConstraint> getRoleTypeAttributeTypeConstraints(
      @Parameter(
              name = "roleType",
              description =
                  "The code for the role type to retrieve the role type attribute type constraint for")
          @RequestParam(value = "roleType", required = false)
          String roleType)
      throws InvalidArgumentException, ServiceUnavailableException;

  /**
   * Retrieve the role type preference type constraint for a specific role type.
   *
   * @param roleType the code for the role type to retrieve the preference constraint for
   * @return the role type preference type constraint
   * @throws InvalidArgumentException if an argument is invalid
   * @throws ServiceUnavailableException if the role type preference type constraint for a specific
   *     role type could not be retrieved
   */
  @Operation(
      summary = "Retrieve the role type preference type constraint",
      description = "Retrieve the role type preference type constraint")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "200",
            description = "The role type preference type constraint was retrieved"),
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
      value = "/role-type-preference-type-constraints",
      method = RequestMethod.GET,
      produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.OK)
  @PreAuthorize("isSecurityDisabled() or isAuthenticated()")
  List<RoleTypePreferenceTypeConstraint> getRoleTypePreferenceTypeConstraints(
      @Parameter(
              name = "roleType",
              description =
                  "The code for the role type to retrieve the role type preference type constraint for")
          @RequestParam(value = "roleType", required = false)
          String roleType)
      throws InvalidArgumentException, ServiceUnavailableException;

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
        @ApiResponse(
            responseCode = "200",
            description = "The role type reference data was retrieved"),
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
      value = "/role-types",
      method = RequestMethod.GET,
      produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.OK)
  @PreAuthorize("isSecurityDisabled() or isAuthenticated()")
  List<RoleType> getRoleTypes(
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
              example = PartyReferenceService.DEFAULT_LOCALE_ID)
          @RequestParam(
              value = "localeId",
              required = false,
              defaultValue = PartyReferenceService.DEFAULT_LOCALE_ID)
          String localeId)
      throws InvalidArgumentException, ServiceUnavailableException;

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
        @ApiResponse(
            responseCode = "200",
            description = "The segmentation type reference data was retrieved"),
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
      value = "/segmentation-types",
      method = RequestMethod.GET,
      produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.OK)
  @PreAuthorize("isSecurityDisabled() or isAuthenticated()")
  List<SegmentationType> getSegmentationTypes(
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
              example = PartyReferenceService.DEFAULT_LOCALE_ID)
          @RequestParam(
              value = "localeId",
              required = false,
              defaultValue = PartyReferenceService.DEFAULT_LOCALE_ID)
          String localeId)
      throws InvalidArgumentException, ServiceUnavailableException;

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
        @ApiResponse(
            responseCode = "200",
            description = "The segment reference data was retrieved"),
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
      value = "/segments",
      method = RequestMethod.GET,
      produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.OK)
  @PreAuthorize("isSecurityDisabled() or isAuthenticated()")
  List<Segment> getSegments(
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
              example = PartyReferenceService.DEFAULT_LOCALE_ID)
          @RequestParam(
              value = "localeId",
              required = false,
              defaultValue = PartyReferenceService.DEFAULT_LOCALE_ID)
          String localeId)
      throws InvalidArgumentException, ServiceUnavailableException;

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
        @ApiResponse(
            responseCode = "200",
            description = "The skill type reference data was retrieved"),
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
      value = "/skill-types",
      method = RequestMethod.GET,
      produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.OK)
  @PreAuthorize("isSecurityDisabled() or isAuthenticated()")
  List<SkillType> getSkillTypes(
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
              example = PartyReferenceService.DEFAULT_LOCALE_ID)
          @RequestParam(
              value = "localeId",
              required = false,
              defaultValue = PartyReferenceService.DEFAULT_LOCALE_ID)
          String localeId)
      throws InvalidArgumentException, ServiceUnavailableException;

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
        @ApiResponse(
            responseCode = "200",
            description = "The source of funds type reference data was retrieved"),
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
      value = "/source-of-funds-types",
      method = RequestMethod.GET,
      produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.OK)
  @PreAuthorize("isSecurityDisabled() or isAuthenticated()")
  List<SourceOfFundsType> getSourceOfFundsTypes(
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
              example = PartyReferenceService.DEFAULT_LOCALE_ID)
          @RequestParam(
              value = "localeId",
              required = false,
              defaultValue = PartyReferenceService.DEFAULT_LOCALE_ID)
          String localeId)
      throws InvalidArgumentException, ServiceUnavailableException;

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
        @ApiResponse(
            responseCode = "200",
            description = "The source of wealth type reference data was retrieved"),
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
      value = "/source-of-wealth-types",
      method = RequestMethod.GET,
      produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.OK)
  @PreAuthorize("isSecurityDisabled() or isAuthenticated()")
  List<SourceOfWealthType> getSourceOfWealthTypes(
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
              example = PartyReferenceService.DEFAULT_LOCALE_ID)
          @RequestParam(
              value = "localeId",
              required = false,
              defaultValue = PartyReferenceService.DEFAULT_LOCALE_ID)
          String localeId)
      throws InvalidArgumentException, ServiceUnavailableException;

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
        @ApiResponse(
            responseCode = "200",
            description = "The status type category reference data was retrieved"),
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
      value = "/status-type-categories",
      method = RequestMethod.GET,
      produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.OK)
  @PreAuthorize("isSecurityDisabled() or isAuthenticated()")
  List<StatusTypeCategory> getStatusTypeCategories(
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
              example = PartyReferenceService.DEFAULT_LOCALE_ID)
          @RequestParam(
              value = "localeId",
              required = false,
              defaultValue = PartyReferenceService.DEFAULT_LOCALE_ID)
          String localeId)
      throws InvalidArgumentException, ServiceUnavailableException;

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
        @ApiResponse(
            responseCode = "200",
            description = "The status type reference data was retrieved"),
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
      value = "/status-types",
      method = RequestMethod.GET,
      produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.OK)
  @PreAuthorize("isSecurityDisabled() or isAuthenticated()")
  List<StatusType> getStatusTypes(
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
              example = PartyReferenceService.DEFAULT_LOCALE_ID)
          @RequestParam(
              value = "localeId",
              required = false,
              defaultValue = PartyReferenceService.DEFAULT_LOCALE_ID)
          String localeId)
      throws InvalidArgumentException, ServiceUnavailableException;

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
        @ApiResponse(
            responseCode = "200",
            description = "The tax number type reference data was retrieved"),
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
      value = "/tax-number-types",
      method = RequestMethod.GET,
      produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.OK)
  @PreAuthorize("isSecurityDisabled() or isAuthenticated()")
  List<TaxNumberType> getTaxNumberTypes(
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
              example = PartyReferenceService.DEFAULT_LOCALE_ID)
          @RequestParam(
              value = "localeId",
              required = false,
              defaultValue = PartyReferenceService.DEFAULT_LOCALE_ID)
          String localeId)
      throws InvalidArgumentException, ServiceUnavailableException;

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
        @ApiResponse(
            responseCode = "200",
            description = "The times to contact reference data was retrieved"),
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
      value = "/times-to-contact",
      method = RequestMethod.GET,
      produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.OK)
  @PreAuthorize("isSecurityDisabled() or isAuthenticated()")
  List<TimeToContact> getTimesToContact(
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
              example = PartyReferenceService.DEFAULT_LOCALE_ID)
          @RequestParam(
              value = "localeId",
              required = false,
              defaultValue = PartyReferenceService.DEFAULT_LOCALE_ID)
          String localeId)
      throws InvalidArgumentException, ServiceUnavailableException;

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
        @ApiResponse(responseCode = "200", description = "The title reference data was retrieved"),
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
      value = "/titles",
      method = RequestMethod.GET,
      produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.OK)
  @PreAuthorize("isSecurityDisabled() or isAuthenticated()")
  List<Title> getTitles(
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
              example = PartyReferenceService.DEFAULT_LOCALE_ID)
          @RequestParam(
              value = "localeId",
              required = false,
              defaultValue = PartyReferenceService.DEFAULT_LOCALE_ID)
          String localeId)
      throws InvalidArgumentException, ServiceUnavailableException;
}
