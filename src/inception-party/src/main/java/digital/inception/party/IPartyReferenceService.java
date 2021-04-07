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

package digital.inception.party;

import digital.inception.core.service.ServiceUnavailableException;
import java.util.List;

/**
 * The <b>IPartyReferenceService</b> interface defines the functionality provided by a Party
 * Reference Service implementation.
 *
 * @author Marcus Portmann
 */
public interface IPartyReferenceService {

  /**
   * Retrieve the attribute type categories.
   *
   * @param localeId the Unicode locale identifier for the locale to retrieve the attribute type
   *     categories for or <b>null</b> to retrieve the attribute type categories for all locales
   * @return the attribute type categories
   */
  List<AttributeTypeCategory> getAttributeTypeCategories(String localeId)
      throws ServiceUnavailableException;

  /**
   * Retrieve the attribute types.
   *
   * @param localeId the Unicode locale identifier for the locale to retrieve the attribute types
   *     for or <b>null</b> to retrieve the attribute types for all locales
   * @return the attribute types
   */
  List<AttributeType> getAttributeTypes(String localeId) throws ServiceUnavailableException;

  /**
   * Retrieve the consent types.
   *
   * @param localeId the Unicode locale identifier for the locale to retrieve the consent types for
   *     or <b>null</b> to retrieve the consent types for all locales
   * @return the consent types
   */
  List<ConsentType> getConsentTypes(String localeId) throws ServiceUnavailableException;

  /**
   * Retrieve the contact mechanism purposes.
   *
   * @param localeId the Unicode locale identifier for the locale to retrieve the contact mechanism
   *     purposes for or <b>null</b> to retrieve the contact mechanism purposes for all locales
   * @return the contact mechanism purposes
   */
  List<ContactMechanismPurpose> getContactMechanismPurposes(String localeId)
      throws ServiceUnavailableException;

  /**
   * Retrieve the contact mechanism roles.
   *
   * @param localeId the Unicode locale identifier for the locale to retrieve the contact mechanism
   *     purposes for or <b>null</b> to retrieve the contact mechanism roles for all locales
   * @return the contact mechanism roles
   */
  List<ContactMechanismRole> getContactMechanismRoles(String localeId)
      throws ServiceUnavailableException;

  /**
   * Retrieve the contact mechanism types.
   *
   * @param localeId the Unicode locale identifier for the locale to retrieve the contact mechanism
   *     types for or <b>null</b> to retrieve the contact mechanism types for all locales
   * @return the contact mechanism types
   */
  List<ContactMechanismType> getContactMechanismTypes(String localeId)
      throws ServiceUnavailableException;

  /**
   * Retrieve the employment statuses.
   *
   * @param localeId the Unicode locale identifier for the locale to retrieve the employment
   *     statuses for or <b>null</b> to retrieve the employment statuses for all locales
   * @return the employment statuses
   */
  List<EmploymentStatus> getEmploymentStatuses(String localeId) throws ServiceUnavailableException;

  /**
   * Retrieve the employment types.
   *
   * @param localeId the Unicode locale identifier for the locale to retrieve the employment types
   *     for or <b>null</b> to retrieve the employment types for all locales
   * @return the employment types
   */
  List<EmploymentType> getEmploymentTypes(String localeId) throws ServiceUnavailableException;

  /**
   * Retrieve the fields of study.
   *
   * @param localeId the Unicode locale identifier for the locale to retrieve the qualification
   *     types for or <b>null</b> to retrieve the fields of study for all locales
   * @return the fields of study
   */
  List<FieldOfStudy> getFieldsOfStudy(String localeId) throws ServiceUnavailableException;

  /**
   * Retrieve the genders.
   *
   * @param localeId the Unicode locale identifier for the locale to retrieve the genders for or
   *     <b>null</b> to retrieve the genders for all locales
   * @return the genders
   */
  List<Gender> getGenders(String localeId) throws ServiceUnavailableException;

  /**
   * Retrieve the identity document types.
   *
   * @param localeId the Unicode locale identifier for the locale to retrieve the identity document
   *     types for or <b>null</b> to retrieve the identity document types for all locales
   * @return the identity document types
   */
  List<IdentityDocumentType> getIdentityDocumentTypes(String localeId)
      throws ServiceUnavailableException;

  /**
   * Retrieve the lock type categories.
   *
   * @param localeId the Unicode locale identifier for the locale to retrieve the lock type
   *     categories for or <b>null</b> to retrieve the lock type categories for all locales
   * @return the lock type categories
   */
  List<LockTypeCategory> getLockTypeCategories(String localeId) throws ServiceUnavailableException;

  /**
   * Retrieve the lock types.
   *
   * @param localeId the Unicode locale identifier for the locale to retrieve the lock types for or
   *     <b>null</b> to retrieve the lock types for all locales
   * @return the lock types
   */
  List<LockType> getLockTypes(String localeId) throws ServiceUnavailableException;

  /**
   * Retrieve the marital statuses.
   *
   * @param localeId the Unicode locale identifier for the locale to retrieve the marital statuses
   *     for or <b>null</b> to retrieve the marital statuses for all locales
   * @return the marital statuses
   */
  List<MaritalStatus> getMaritalStatuses(String localeId) throws ServiceUnavailableException;

  /**
   * Retrieve the marriage types.
   *
   * @param localeId the Unicode locale identifier for the locale to retrieve the marriage types for
   *     or <b>null</b> to retrieve the marriage types for all locales
   * @return the marriage types
   */
  List<MarriageType> getMarriageTypes(String localeId) throws ServiceUnavailableException;

  /**
   * Retrieve the next of kin types.
   *
   * @param localeId the Unicode locale identifier for the locale to retrieve the next of kin types
   *     for or <b>null</b> to retrieve the next of kin types for all locales
   * @return the next of kin types
   */
  List<NextOfKinType> getNextOfKinTypes(String localeId) throws ServiceUnavailableException;

  /**
   * Retrieve the occupations.
   *
   * @param localeId the Unicode locale identifier for the locale to retrieve the occupations for or
   *     <b>null</b> to retrieve the occupations for all locales
   * @return the occupations
   */
  List<Occupation> getOccupations(String localeId) throws ServiceUnavailableException;

  /**
   * Retrieve the physical address purposes.
   *
   * @param localeId the Unicode locale identifier for the locale to retrieve the physical address
   *     purposes for or <b>null</b> to retrieve the physical address purposes for all locales
   * @return the physical address purposes
   */
  List<PhysicalAddressPurpose> getPhysicalAddressPurposes(String localeId)
      throws ServiceUnavailableException;

  /**
   * Retrieve the physical address roles.
   *
   * @param localeId the Unicode locale identifier for the locale to retrieve the physical address
   *     roles for or <b>null</b> to retrieve the physical address roles for all locales
   * @return the physical address roles
   */
  List<PhysicalAddressRole> getPhysicalAddressRoles(String localeId)
      throws ServiceUnavailableException;

  /**
   * Retrieve the physical address types.
   *
   * @param localeId the Unicode locale identifier for the locale to retrieve the physical address
   *     types for or <b>null</b> to retrieve the physical address types for all locales
   * @return the physical address types
   */
  List<PhysicalAddressType> getPhysicalAddressTypes(String localeId)
      throws ServiceUnavailableException;

  /**
   * Retrieve the preference type categories.
   *
   * @param localeId the Unicode locale identifier for the locale to retrieve the preference type
   *     categories for or <b>null</b> to retrieve the preference type categories for all locales
   * @return the preference type categories
   */
  List<PreferenceTypeCategory> getPreferenceTypeCategories(String localeId)
      throws ServiceUnavailableException;

  /**
   * Retrieve the preference types.
   *
   * @param localeId the Unicode locale identifier for the locale to retrieve the preference types
   *     for or <b>null</b> to retrieve the preference types for all locales
   * @return the preference types
   */
  List<PreferenceType> getPreferenceTypes(String localeId) throws ServiceUnavailableException;

  /**
   * Retrieve the qualification types.
   *
   * @param localeId the Unicode locale identifier for the locale to retrieve the qualification
   *     types for or <b>null</b> to retrieve the qualification types for all locales
   * @return the qualification types
   */
  List<QualificationType> getQualificationTypes(String localeId) throws ServiceUnavailableException;

  /**
   * Retrieve the races.
   *
   * @param localeId the Unicode locale identifier for the locale to retrieve the races for or
   *     <b>null</b> to retrieve the races for all locales
   * @return the races
   */
  List<Race> getRaces(String localeId) throws ServiceUnavailableException;

  /**
   * Retrieve the residence permit types.
   *
   * @param localeId the Unicode locale identifier for the locale to retrieve the residence permit
   *     types for or <b>null</b> to retrieve the residence permit types for all locales
   * @return the residence permit types
   */
  List<ResidencePermitType> getResidencePermitTypes(String localeId)
      throws ServiceUnavailableException;

  /**
   * Retrieve the residency statuses.
   *
   * @param localeId the Unicode locale identifier for the locale to retrieve the residency statuses
   *     for or <b>null</b> to retrieve the residency statuses for all locales
   * @return the residency statuses
   */
  List<ResidencyStatus> getResidencyStatuses(String localeId) throws ServiceUnavailableException;

  /**
   * Retrieve the residential types.
   *
   * @param localeId the Unicode locale identifier for the locale to retrieve the residential types
   *     for or <b>null</b> to retrieve the residential types for all locales
   * @return the residential types
   */
  List<ResidentialType> getResidentialTypes(String localeId) throws ServiceUnavailableException;

  /**
   * Retrieve the role purposes.
   *
   * @param localeId the Unicode locale identifier for the locale to retrieve the role purposes for
   *     or <b>null</b> to retrieve the role purposes for all locales
   * @return the role purposes
   */
  List<RolePurpose> getRolePurposes(String localeId) throws ServiceUnavailableException;

  /**
   * Retrieve the role type attribute type constraints.
   *
   * @return the role type attribute type constraints
   */
  List<RoleTypeAttributeTypeConstraint> getRoleTypeAttributeTypeConstraints()
      throws ServiceUnavailableException;

  /**
   * Retrieve the role type attribute type constraints.
   *
   * @param roleType the code for the role type to retrieve the attribute constraints for
   * @return the role type attribute type constraints
   */
  List<RoleTypeAttributeTypeConstraint> getRoleTypeAttributeTypeConstraints(String roleType)
      throws ServiceUnavailableException;

  /**
   * Retrieve the role type preference type constraints.
   *
   * @return the role type preference type constraints
   */
  List<RoleTypePreferenceTypeConstraint> getRoleTypePreferenceTypeConstraints()
      throws ServiceUnavailableException;

  /**
   * Retrieve the role type preference type constraints.
   *
   * @param roleType the code for the role type to retrieve the preference constraints for
   * @return the role type preference type constraints
   */
  List<RoleTypePreferenceTypeConstraint> getRoleTypePreferenceTypeConstraints(String roleType)
      throws ServiceUnavailableException;

  /**
   * Retrieve the role types.
   *
   * @param localeId the Unicode locale identifier for the locale to retrieve the role types for or
   *     <b>null</b> to retrieve the role types for all locales
   * @return the role types
   */
  List<RoleType> getRoleTypes(String localeId) throws ServiceUnavailableException;

  /**
   * Retrieve the segments.
   *
   * @param localeId the Unicode locale identifier for the locale to retrieve the segments for or
   *     <b>null</b> to retrieve the segments for all locales
   * @return the segments
   */
  List<Segment> getSegments(String localeId) throws ServiceUnavailableException;

  /**
   * Retrieve the source of funds types.
   *
   * @param localeId the Unicode locale identifier for the locale to retrieve the source of funds
   *     types for or <b>null</b> to retrieve the source of funds types for all locales
   * @return the source of funds types
   */
  List<SourceOfFundsType> getSourceOfFundsTypes(String localeId) throws ServiceUnavailableException;

  /**
   * Retrieve the source of wealth types.
   *
   * @param localeId the Unicode locale identifier for the locale to retrieve the source of wealth
   *     types for or <b>null</b> to retrieve the source of wealth types for all locales
   * @return the source of wealth types
   */
  List<SourceOfWealthType> getSourceOfWealthTypes(String localeId)
      throws ServiceUnavailableException;

  /**
   * Retrieve the status type categories.
   *
   * @param localeId the Unicode locale identifier for the locale to retrieve the status type
   *     categories for or <b>null</b> to retrieve the status type categories for all locales
   * @return the status type categories
   */
  List<StatusTypeCategory> getStatusTypeCategories(String localeId)
      throws ServiceUnavailableException;

  /**
   * Retrieve the status types.
   *
   * @param localeId the Unicode locale identifier for the locale to retrieve the status types for
   *     or <b>null</b> to retrieve the status types for all locales
   * @return the status types
   */
  List<StatusType> getStatusTypes(String localeId) throws ServiceUnavailableException;

  /**
   * Retrieve the tax number types.
   *
   * @param localeId the Unicode locale identifier for the locale to retrieve the tax number types
   *     for or <b>null</b> to retrieve the tax number types for all locales
   * @return the tax number types
   */
  List<TaxNumberType> getTaxNumberTypes(String localeId) throws ServiceUnavailableException;

  /**
   * Retrieve the times to contact.
   *
   * @param localeId the Unicode locale identifier for the locale to retrieve the times to contact
   *     for or <b>null</b> to retrieve the times to contact for all locales
   * @return the times to contact
   */
  List<TimeToContact> getTimesToContact(String localeId) throws ServiceUnavailableException;

  /**
   * Retrieve the titles.
   *
   * @param localeId the Unicode locale identifier for the locale to retrieve the titles for or
   *     <b>null</b> to retrieve the titles for all locales
   * @return the titles
   */
  List<Title> getTitles(String localeId) throws ServiceUnavailableException;

  /**
   * Check whether the code is a valid code for a attribute type for the party type.
   *
   * @param partyTypeCode the party type code
   * @param attributeTypeCode the code for the attribute type
   * @return <b>true</b> if the code is a valid code for a attribute type or <b>false</b> otherwise
   */
  boolean isValidAttributeType(String partyTypeCode, String attributeTypeCode)
      throws ServiceUnavailableException;

  /**
   * Check whether the code is a valid code for a attribute type category.
   *
   * @param attributeTypeCategoryCode the code for the attribute type category
   * @return <b>true</b> if the code is a valid code for a attribute type category or <b>false</b>
   *     otherwise
   */
  boolean isValidAttributeTypeCategory(String attributeTypeCategoryCode)
      throws ServiceUnavailableException;

  /**
   * Check whether the code is a valid code for a consent type.
   *
   * @param consentTypeCode the code for the consent type
   * @return <b>true</b> if the code is a valid code for a consent type or <b>false</b> otherwise
   */
  boolean isValidConsentType(String consentTypeCode) throws ServiceUnavailableException;

  /**
   * Check whether the code is a valid code for a contact mechanism purpose for the party type.
   *
   * @param partyTypeCode the party type code
   * @param contactMechanismTypeCode the code for the contact mechanism type
   * @param contactMechanismPurposeCode the code for the contact mechanism purpose
   * @return <b>true</b> if the code is a valid code for a contact mechanism purpose or <b>false</b>
   *     otherwise
   */
  boolean isValidContactMechanismPurpose(
      String partyTypeCode, String contactMechanismTypeCode, String contactMechanismPurposeCode)
      throws ServiceUnavailableException;

  /**
   * Check whether the code is a valid code for a contact mechanism role for the party type.
   *
   * @param partyTypeCode the party type code
   * @param contactMechanismTypeCode the code for the contact mechanism type
   * @param contactMechanismRoleCode the code for the contact mechanism role
   * @return <b>true</b> if the code is a valid code for a contact mechanism role or <b>false</b>
   *     otherwise
   */
  boolean isValidContactMechanismRole(
      String partyTypeCode, String contactMechanismTypeCode, String contactMechanismRoleCode)
      throws ServiceUnavailableException;

  /**
   * Check whether the code is a valid code for a contact mechanism type.
   *
   * @param contactMechanismTypeCode the code for the contact mechanism type
   * @return <b>true</b> if the code is a valid code for a contact mechanism type or <b>false</b>
   *     otherwise
   */
  boolean isValidContactMechanismType(String contactMechanismTypeCode)
      throws ServiceUnavailableException;

  /**
   * Check whether the code is a valid code for an employment status.
   *
   * @param employmentStatusCode the code for the employment status
   * @return <b>true</b> if the code is a valid code for an employment status or <b>false</b>
   *     otherwise
   */
  boolean isValidEmploymentStatus(String employmentStatusCode) throws ServiceUnavailableException;

  /**
   * Check whether the code is a valid code for an employment type.
   *
   * @param employmentStatusCode the code for the employment status
   * @param employmentTypeCode the code for the employment type
   * @return <b>true</b> if the code is a valid code for an employment type or <b>false</b>
   *     otherwise
   */
  boolean isValidEmploymentType(String employmentStatusCode, String employmentTypeCode)
      throws ServiceUnavailableException;

  /**
   * Check whether the code is a valid code for an employment type.
   *
   * @param employmentTypeCode the code for the employment type
   * @return <b>true</b> if the code is a valid code for an employment type or <b>false</b>
   *     otherwise
   */
  boolean isValidEmploymentType(String employmentTypeCode) throws ServiceUnavailableException;

  /**
   * Check whether the code is a valid code for a field of study.
   *
   * @param fieldOfStudyCode the code for the field of study
   * @return <b>true</b> if the code is a valid code for a field of study or <b>false</b> otherwise
   */
  boolean isValidFieldOfStudy(String fieldOfStudyCode) throws ServiceUnavailableException;

  /**
   * Check whether the code is a valid code for a gender.
   *
   * @param genderCode the code for the gender
   * @return <b>true</b> if the code is a valid code for a gender or <b>false</b> otherwise
   */
  boolean isValidGender(String genderCode) throws ServiceUnavailableException;

  /**
   * Check whether the code is a valid code for an identity document type for the party type.
   *
   * @param partyTypeCode the party type code
   * @param identityDocumentTypeCode the code for the identity document type
   * @return <b>true</b> if the code is a valid code for an identity document type or <b>false</b>
   *     otherwise
   */
  boolean isValidIdentityDocumentType(String partyTypeCode, String identityDocumentTypeCode)
      throws ServiceUnavailableException;

  /**
   * Check whether the code is a valid code for a lock type for the party type.
   *
   * @param partyTypeCode the party type code
   * @param lockTypeCode the code for the lock type
   * @return <b>true</b> if the code is a valid code for a lock type or <b>false</b> otherwise
   */
  boolean isValidLockType(String partyTypeCode, String lockTypeCode)
      throws ServiceUnavailableException;

  /**
   * Check whether the code is a valid code for a lock type category.
   *
   * @param lockTypeCategoryCode the code for the lock type category
   * @return <b>true</b> if the code is a valid code for a lock type category or <b>false</b>
   *     otherwise
   */
  boolean isValidLockTypeCategory(String lockTypeCategoryCode) throws ServiceUnavailableException;

  /**
   * Check whether the code is a valid code for a marital status.
   *
   * @param maritalStatusCode the code for the marital status
   * @return <b>true</b> if the code is a valid code for a marital status or <b>false</b> otherwise
   */
  boolean isValidMaritalStatus(String maritalStatusCode) throws ServiceUnavailableException;

  /**
   * Check whether the code is a valid code for a marriage type.
   *
   * @param maritalStatusCode the code for the marital status
   * @param marriageTypeCode the code for the marriage type
   * @return <b>true</b> if the code is a valid code for a marriage type or <b>false</b> otherwise
   */
  boolean isValidMarriageType(String maritalStatusCode, String marriageTypeCode)
      throws ServiceUnavailableException;

  /**
   * Check whether the measurement unit is valid for the attribute type with the specified code.
   *
   * @param attributeTypeCode the code for the attribute type
   * @param measurementUnit the measurement unit
   * @return <b>true</b> if the measurement unit is valid for the attribute type with the specified
   *     code or <b>false</b> otherwise
   */
  boolean isValidMeasurementUnitForAttributeType(
      String attributeTypeCode, MeasurementUnit measurementUnit) throws ServiceUnavailableException;

  /**
   * Check whether the code is a valid code for a next of kin type.
   *
   * @param nextOfKinTypeCode the code for the next of kin type
   * @return <b>true</b> if the code is a valid code for a next of kin type or <b>false</b>
   *     otherwise
   */
  boolean isValidNextOfKinType(String nextOfKinTypeCode) throws ServiceUnavailableException;

  /**
   * Check whether the code is a valid code for a occupation.
   *
   * @param occupationCode the code for the occupation
   * @return <b>true</b> if the code is a valid code for a occupation or <b>false</b> otherwise
   */
  boolean isValidOccupation(String occupationCode) throws ServiceUnavailableException;

  /**
   * Check whether the code is a valid code for a physical address purpose for the party type.
   *
   * @param partyTypeCode the party type code
   * @param physicalAddressPurposeCode the code for the physical address purpose
   * @return <b>true</b> if the code is a valid code for a physical address purpose or <b>false</b>
   *     otherwise
   */
  boolean isValidPhysicalAddressPurpose(String partyTypeCode, String physicalAddressPurposeCode)
      throws ServiceUnavailableException;

  /**
   * Check whether the code is a valid code for a physical address purpose.
   *
   * @param physicalAddressPurposeCode the code for the physical address purpose
   * @return <b>true</b> if the code is a valid code for a physical address purpose or <b>false</b>
   *     otherwise
   */
  boolean isValidPhysicalAddressPurpose(String physicalAddressPurposeCode)
      throws ServiceUnavailableException;

  /**
   * Check whether the code is a valid code for a physical address role for the party type.
   *
   * @param partyTypeCode the party type code
   * @param physicalAddressRoleCode the code for the physical address role
   * @return <b>true</b> if the code is a valid code for a physical address role or <b>false</b>
   *     otherwise
   */
  boolean isValidPhysicalAddressRole(String partyTypeCode, String physicalAddressRoleCode)
      throws ServiceUnavailableException;

  /**
   * Check whether the code is a valid code for a physical address role for the party type.
   *
   * @param physicalAddressRoleCode the code for the physical address role
   * @return <b>true</b> if the code is a valid code for a physical address role or <b>false</b>
   *     otherwise
   */
  boolean isValidPhysicalAddressRole(String physicalAddressRoleCode)
      throws ServiceUnavailableException;

  /**
   * Check whether the code is a valid code for a physical address type.
   *
   * @param physicalAddressTypeCode the code for the physical address type
   * @return <b>true</b> if the code is a valid code for a physical address type or <b>false</b>
   *     otherwise
   */
  boolean isValidPhysicalAddressType(String physicalAddressTypeCode)
      throws ServiceUnavailableException;

  /**
   * Check whether the code is a valid code for a preference type for the party type.
   *
   * @param partyTypeCode the party type code
   * @param preferenceTypeCode the code for the preference type
   * @return <b>true</b> if the code is a valid code for a preference type or <b>false</b> otherwise
   */
  boolean isValidPreferenceType(String partyTypeCode, String preferenceTypeCode)
      throws ServiceUnavailableException;

  /**
   * Check whether the code is a valid code for a preference type category.
   *
   * @param preferenceTypeCategoryCode the code for the preference type category
   * @return <b>true</b> if the code is a valid code for a preference type category or <b>false</b>
   *     otherwise
   */
  boolean isValidPreferenceTypeCategory(String preferenceTypeCategoryCode)
      throws ServiceUnavailableException;

  /**
   * Check whether the code is a valid code for a qualification type.
   *
   * @param qualificationTypeCode the code for the qualification type
   * @return <b>true</b> if the code is a valid code for a qualification type or <b>false</b>
   *     otherwise
   */
  boolean isValidQualificationType(String qualificationTypeCode) throws ServiceUnavailableException;

  /**
   * Check whether the code is a valid code for a race.
   *
   * @param raceCode the code for the race
   * @return <b>true</b> if the code is a valid code for a race or <b>false</b> otherwise
   */
  boolean isValidRace(String raceCode) throws ServiceUnavailableException;

  /**
   * Check whether the code is a valid code for a residence permit type.
   *
   * @param residencePermitTypeCode the code for the residence permit type
   * @return <b>true</b> if the code is a valid code for a residence permit type or <b>false</b>
   *     otherwise
   */
  boolean isValidResidencePermitType(String residencePermitTypeCode)
      throws ServiceUnavailableException;

  /**
   * Check whether the code is a valid code for a residency status.
   *
   * @param residencyStatusCode the code for the residency status
   * @return <b>true</b> if the code is a valid code for a residency status or <b>false</b>
   *     otherwise
   */
  boolean isValidResidencyStatus(String residencyStatusCode) throws ServiceUnavailableException;

  /**
   * Check whether the code is a valid code for a residential type.
   *
   * @param residentialTypeCode the code for the residential type
   * @return <b>true</b> if the code is a valid code for a residential type or <b>false</b>
   *     otherwise
   */
  boolean isValidResidentialType(String residentialTypeCode) throws ServiceUnavailableException;

  /**
   * Check whether the code is a valid code for a role purpose.
   *
   * @param rolePurposeCode the code for the role purpose
   * @return <b>true</b> if the code is a valid code for a role purpose or <b>false</b> otherwise
   */
  boolean isValidRolePurpose(String rolePurposeCode) throws ServiceUnavailableException;

  /**
   * Check whether the code is a valid code for a role type for the party type.
   *
   * @param partyTypeCode the party type code
   * @param roleTypeCode the code for the role type
   * @return <b>true</b> if the code is a valid code for a role type or <b>false</b> otherwise
   */
  boolean isValidRoleType(String partyTypeCode, String roleTypeCode)
      throws ServiceUnavailableException;

  /**
   * Check whether the code is a valid code for a segment.
   *
   * @param segmentCode the code for the segment
   * @return <b>true</b> if the code is a valid code for a segment or <b>false</b> otherwise
   */
  boolean isValidSegment(String segmentCode) throws ServiceUnavailableException;

  /**
   * Check whether the code is a valid code for a source of funds type.
   *
   * @param sourceOfFundsTypeCode the code for the source of funds type
   * @return <b>true</b> if the code is a valid code for a source of funds type or <b>false</b>
   *     otherwise
   */
  boolean isValidSourceOfFundsType(String sourceOfFundsTypeCode) throws ServiceUnavailableException;

  /**
   * Check whether the code is a valid code for a source of wealth type.
   *
   * @param sourceOfWealthTypeCode the code for the source of wealth type
   * @return <b>true</b> if the code is a valid code for a source of wealth type or <b>false</b>
   *     otherwise
   */
  boolean isValidSourceOfWealthType(String sourceOfWealthTypeCode)
      throws ServiceUnavailableException;

  /**
   * Check whether the code is a valid code for a status type for the party type.
   *
   * @param partyTypeCode the party type code
   * @param statusTypeCode the code for the status type
   * @return <b>true</b> if the code is a valid code for a status type or <b>false</b> otherwise
   */
  boolean isValidStatusType(String partyTypeCode, String statusTypeCode)
      throws ServiceUnavailableException;

  /**
   * Check whether the code is a valid code for a status type category.
   *
   * @param statusTypeCategoryCode the code for the status type category
   * @return <b>true</b> if the code is a valid code for a status type category or <b>false</b>
   *     otherwise
   */
  boolean isValidStatusTypeCategory(String statusTypeCategoryCode)
      throws ServiceUnavailableException;

  /**
   * Check whether the code is a valid code for a tax number type.
   *
   * @param partyTypeCode the party type code
   * @param taxNumberTypeCode the code for the tax number type
   * @return <b>true</b> if the code is a valid code for a tax number type or <b>false</b> otherwise
   */
  boolean isValidTaxNumberType(String partyTypeCode, String taxNumberTypeCode)
      throws ServiceUnavailableException;

  /**
   * Check whether the code is a valid code for a time to contact.
   *
   * @param timeToContactCode the code for the time to contact
   * @return <b>true</b> if the code is a valid code for a time to contact or <b>false</b> otherwise
   */
  boolean isValidTimeToContact(String timeToContactCode) throws ServiceUnavailableException;

  /**
   * Check whether the code is a valid code for a title.
   *
   * @param titleCode the code for the title
   * @return <b>true</b> if the code is a valid code for a title or <b>false</b> otherwise
   */
  boolean isValidTitle(String titleCode) throws ServiceUnavailableException;
}
