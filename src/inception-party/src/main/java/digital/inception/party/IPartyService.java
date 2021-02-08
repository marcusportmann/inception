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
import digital.inception.core.sorting.SortDirection;
import digital.inception.core.service.InvalidArgumentException;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import javax.validation.ConstraintViolation;

/**
 * The <b>IPartyService</b> interface defines the functionality provided by a Party Service
 * implementation.
 *
 * @author Marcus Portmann
 */
@SuppressWarnings("unused")
public interface IPartyService {

  /**
   * Create the new organization.
   *
   * @param organization the organization
   */
  void createOrganization(Organization organization)
      throws InvalidArgumentException, DuplicateOrganizationException, ServiceUnavailableException;

  /**
   * Create the new person.
   *
   * @param person the person
   */
  void createPerson(Person person)
      throws InvalidArgumentException, DuplicatePersonException, ServiceUnavailableException;

  /**
   * Delete the organization.
   *
   * @param organizationId the Universally Unique Identifier (UUID) for the organization
   */
  void deleteOrganization(UUID organizationId)
      throws InvalidArgumentException, OrganizationNotFoundException, ServiceUnavailableException;

  /**
   * Delete the party.
   *
   * @param partyId the Universally Unique Identifier (UUID) for the party
   */
  void deleteParty(UUID partyId)
      throws InvalidArgumentException, PartyNotFoundException, ServiceUnavailableException;

  /**
   * Delete the person.
   *
   * @param personId the Universally Unique Identifier (UUID) for the person
   */
  void deletePerson(UUID personId)
      throws InvalidArgumentException, PersonNotFoundException, ServiceUnavailableException;

  /**
   * Retrieve all the contact mechanism purposes.
   *
   * @return the contact mechanism purposes
   */
  List<ContactMechanismPurpose> getContactMechanismPurposes() throws ServiceUnavailableException;

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
   * Retrieve all the contact mechanism types.
   *
   * @return the contact mechanism types
   */
  List<ContactMechanismType> getContactMechanismTypes() throws ServiceUnavailableException;

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
   * Retrieve all the employment statuses.
   *
   * @return the employment statuses
   */
  List<EmploymentStatus> getEmploymentStatuses() throws ServiceUnavailableException;

  /**
   * Retrieve the employment statuses.
   *
   * @param localeId the Unicode locale identifier for the locale to retrieve the employment
   *     statuses for or <b>null</b> to retrieve the employment statuses for all locales
   * @return the employment statuses
   */
  List<EmploymentStatus> getEmploymentStatuses(String localeId) throws ServiceUnavailableException;

  /**
   * Retrieve all the employment types.
   *
   * @return the employment types
   */
  List<EmploymentType> getEmploymentTypes() throws ServiceUnavailableException;

  /**
   * Retrieve the employment types.
   *
   * @param localeId the Unicode locale identifier for the locale to retrieve the employment types
   *     for or <b>null</b> to retrieve the employment types for all locales
   * @return the employment types
   */
  List<EmploymentType> getEmploymentTypes(String localeId) throws ServiceUnavailableException;

  /**
   * Retrieve all the genders.
   *
   * @return the genders
   */
  List<Gender> getGenders() throws ServiceUnavailableException;

  /**
   * Retrieve the genders.
   *
   * @param localeId the Unicode locale identifier for the locale to retrieve the genders for or
   *     <b>null</b> to retrieve the genders for all locales
   * @return the genders
   */
  List<Gender> getGenders(String localeId) throws ServiceUnavailableException;

  /**
   * Retrieve all the identity document types.
   *
   * @return the identity document types
   */
  List<IdentityDocumentType> getIdentityDocumentTypes() throws ServiceUnavailableException;

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
   * Retrieve all the marital statuses.
   *
   * @return the marital statuses
   */
  List<MaritalStatus> getMaritalStatuses() throws ServiceUnavailableException;

  /**
   * Retrieve the marital statuses.
   *
   * @param localeId the Unicode locale identifier for the locale to retrieve the marital statuses
   *     for or <b>null</b> to retrieve the marital statuses for all locales
   * @return the marital statuses
   */
  List<MaritalStatus> getMaritalStatuses(String localeId) throws ServiceUnavailableException;

  /**
   * Retrieve all the marriage types.
   *
   * @return the marriage types
   */
  List<MarriageType> getMarriageTypes() throws ServiceUnavailableException;

  /**
   * Retrieve the marriage types.
   *
   * @param localeId the Unicode locale identifier for the locale to retrieve the marriage types for
   *     or <b>null</b> to retrieve the marriage types for all locales
   * @return the marriage types
   */
  List<MarriageType> getMarriageTypes(String localeId) throws ServiceUnavailableException;

  /**
   * Retrieve all the next of kin types.
   *
   * @return the next of kin types
   */
  List<NextOfKinType> getNextOfKinTypes() throws ServiceUnavailableException;

  /**
   * Retrieve the next of kin types.
   *
   * @param localeId the Unicode locale identifier for the locale to retrieve the next of kin types
   *     for or <b>null</b> to retrieve the next of kin types for all locales
   * @return the next of kin types
   */
  List<NextOfKinType> getNextOfKinTypes(String localeId) throws ServiceUnavailableException;

  /**
   * Retrieve all the occupations.
   *
   * @return the occupations
   */
  List<Occupation> getOccupations() throws ServiceUnavailableException;

  /**
   * Retrieve the occupations.
   *
   * @param localeId the Unicode locale identifier for the locale to retrieve the occupations for or
   *     <b>null</b> to retrieve the occupations for all locales
   * @return the occupations
   */
  List<Occupation> getOccupations(String localeId) throws ServiceUnavailableException;

  /**
   * Retrieve the organization.
   *
   * @param organizationId the Universally Unique Identifier (UUID) for the organization
   * @return the organization
   */
  Organization getOrganization(UUID organizationId)
      throws InvalidArgumentException, OrganizationNotFoundException, ServiceUnavailableException;

  /**
   * Retrieve the organizations.
   *
   * @param filter the optional filter to apply to the organizations
   * @param sortDirection the optional sort direction to apply to the organizations
   * @param pageIndex the optional page index
   * @param pageSize the optional page size
   * @return the organizations
   */
  Organizations getOrganizations(
      String filter, SortDirection sortDirection, Integer pageIndex, Integer pageSize)
      throws InvalidArgumentException, ServiceUnavailableException;

  /**
   * Retrieve the parties.
   *
   * @param filter the optional filter to apply to the parties
   * @param sortDirection the optional sort direction to apply to the parties
   * @param pageIndex the optional page index
   * @param pageSize the optional page size
   * @return the parties
   */
  Parties getParties(
      String filter, SortDirection sortDirection, Integer pageIndex, Integer pageSize)
      throws InvalidArgumentException, ServiceUnavailableException;

  /**
   * Retrieve the party.
   *
   * @param partyId the Universally Unique Identifier (UUID) for the party
   * @return the party
   */
  Party getParty(UUID partyId)
      throws InvalidArgumentException, PartyNotFoundException, ServiceUnavailableException;

  /**
   * Retrieve all the party attribute type categories.
   *
   * @return the party attribute type categories
   */
  List<PartyAttributeTypeCategory> getPartyAttributeTypeCategories()
      throws ServiceUnavailableException;

  /**
   * Retrieve the party attribute type categories.
   *
   * @param localeId the Unicode locale identifier for the locale to retrieve the party attribute
   *     type categories for or <b>null</b> to retrieve the party attribute type categories for all
   *     locales
   * @return the party attribute type categories
   */
  List<PartyAttributeTypeCategory> getPartyAttributeTypeCategories(String localeId)
      throws ServiceUnavailableException;

  /**
   * Retrieve all the party attribute types.
   *
   * @return the party attribute types
   */
  List<PartyAttributeType> getPartyAttributeTypes() throws ServiceUnavailableException;

  /**
   * Retrieve the party attribute types.
   *
   * @param localeId the Unicode locale identifier for the locale to retrieve the party attribute
   *     types for or <b>null</b> to retrieve the party attribute types for all locales
   * @return the party attribute types
   */
  List<PartyAttributeType> getPartyAttributeTypes(String localeId)
      throws ServiceUnavailableException;

  /**
   * Retrieve all the party role purposes.
   *
   * @return the party role purposes
   */
  List<PartyRolePurpose> getPartyRolePurposes() throws ServiceUnavailableException;

  /**
   * Retrieve the party role purposes.
   *
   * @param localeId the Unicode locale identifier for the locale to retrieve the party role
   *     purposes for or <b>null</b> to retrieve the party role purposes for all locales
   * @return the party role purposes
   */
  List<PartyRolePurpose> getPartyRolePurposes(String localeId) throws ServiceUnavailableException;

  /**
   * Retrieve all the party role types.
   *
   * @return the party role types
   */
  List<PartyRoleType> getPartyRoleTypes() throws ServiceUnavailableException;

  /**
   * Retrieve the party role types.
   *
   * @param localeId the Unicode locale identifier for the locale to retrieve the party role types
   *     for or <b>null</b> to retrieve the party role types for all locales
   * @return the party role types
   */
  List<PartyRoleType> getPartyRoleTypes(String localeId) throws ServiceUnavailableException;

  /**
   * Retrieve the person.
   *
   * @param personId the Universally Unique Identifier (UUID) for the person
   * @return the person
   */
  Person getPerson(UUID personId)
      throws InvalidArgumentException, PersonNotFoundException, ServiceUnavailableException;

  /**
   * Retrieve the persons.
   *
   * @param filter the optional filter to apply to the persons
   * @param sortBy the optional method used to sort the persons e.g. by name
   * @param sortDirection the optional sort direction to apply to the persons
   * @param pageIndex the optional page index
   * @param pageSize the optional page size
   * @return the persons
   */
  Persons getPersons(
      String filter,
      PersonSortBy sortBy,
      SortDirection sortDirection,
      Integer pageIndex,
      Integer pageSize)
      throws InvalidArgumentException, ServiceUnavailableException;

  /**
   * Retrieve all the physical address purposes.
   *
   * @return the physical address purposes
   */
  List<PhysicalAddressPurpose> getPhysicalAddressPurposes() throws ServiceUnavailableException;

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
   * Retrieve all the physical address types.
   *
   * @return the physical address types
   */
  List<PhysicalAddressType> getPhysicalAddressTypes() throws ServiceUnavailableException;

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
   * Retrieve all the preference type categories.
   *
   * @return the preference type categories
   */
  List<PreferenceTypeCategory> getPreferenceTypeCategories() throws ServiceUnavailableException;

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
   * Retrieve all the preference types.
   *
   * @return the preference types
   */
  List<PreferenceType> getPreferenceTypes() throws ServiceUnavailableException;

  /**
   * Retrieve the preference types.
   *
   * @param localeId the Unicode locale identifier for the locale to retrieve the preference types
   *     for or <b>null</b> to retrieve the preference types for all locales
   * @return the preference types
   */
  List<PreferenceType> getPreferenceTypes(String localeId) throws ServiceUnavailableException;

  /**
   * Retrieve all the races.
   *
   * @return the races
   */
  List<Race> getRaces() throws ServiceUnavailableException;

  /**
   * Retrieve the races.
   *
   * @param localeId the Unicode locale identifier for the locale to retrieve the races for or
   *     <b>null</b> to retrieve the races for all locales
   * @return the races
   */
  List<Race> getRaces(String localeId) throws ServiceUnavailableException;

  /**
   * Retrieve all the residence permit types.
   *
   * @return the residence permit types
   */
  List<ResidencePermitType> getResidencePermitTypes() throws ServiceUnavailableException;

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
   * Retrieve all the residency statuses.
   *
   * @return the residency statuses
   */
  List<ResidencyStatus> getResidencyStatuses() throws ServiceUnavailableException;

  /**
   * Retrieve the residency statuses.
   *
   * @param localeId the Unicode locale identifier for the locale to retrieve the residency statuses
   *     for or <b>null</b> to retrieve the residency statuses for all locales
   * @return the residency statuses
   */
  List<ResidencyStatus> getResidencyStatuses(String localeId) throws ServiceUnavailableException;

  /**
   * Retrieve all the residential types.
   *
   * @return the residential types
   */
  List<ResidentialType> getResidentialTypes() throws ServiceUnavailableException;

  /**
   * Retrieve the residential types.
   *
   * @param localeId the Unicode locale identifier for the locale to retrieve the residential types
   *     for or <b>null</b> to retrieve the residential types for all locales
   * @return the residential types
   */
  List<ResidentialType> getResidentialTypes(String localeId) throws ServiceUnavailableException;

  /**
   * Retrieve all the sources of funds.
   *
   * @return the sources of funds
   */
  List<SourceOfFunds> getSourcesOfFunds() throws ServiceUnavailableException;

  /**
   * Retrieve the sources of funds.
   *
   * @param localeId the Unicode locale identifier for the locale to retrieve the sources of funds
   *     for or <b>null</b> to retrieve the sources of funds for all locales
   * @return the sources of funds
   */
  List<SourceOfFunds> getSourcesOfFunds(String localeId) throws ServiceUnavailableException;

  /**
   * Retrieve all the tax number types.
   *
   * @return the tax number types
   */
  List<TaxNumberType> getTaxNumberTypes() throws ServiceUnavailableException;

  /**
   * Retrieve the tax number types.
   *
   * @param localeId the Unicode locale identifier for the locale to retrieve the tax number types
   *     for or <b>null</b> to retrieve the tax number types for all locales
   * @return the tax number types
   */
  List<TaxNumberType> getTaxNumberTypes(String localeId) throws ServiceUnavailableException;

  /**
   * Retrieve all the times to contact.
   *
   * @return the times to contact
   */
  List<TimeToContact> getTimesToContact() throws ServiceUnavailableException;

  /**
   * Retrieve the times to contact.
   *
   * @param localeId the Unicode locale identifier for the locale to retrieve the times to contact
   *     for or <b>null</b> to retrieve the times to contact for all locales
   * @return the times to contact
   */
  List<TimeToContact> getTimesToContact(String localeId) throws ServiceUnavailableException;

  /**
   * Retrieve all the titles.
   *
   * @return the titles
   */
  List<Title> getTitles() throws ServiceUnavailableException;

  /**
   * Retrieve the titles.
   *
   * @param localeId the Unicode locale identifier for the locale to retrieve the titles for or
   *     <b>null</b> to retrieve the titles for all locales
   * @return the titles
   */
  List<Title> getTitles(String localeId) throws ServiceUnavailableException;

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
   * Check whether the code is a valid code for a party attribute type for the party type.
   *
   * @param partyTypeCode the party type code
   * @param partyAttributeTypeCode the code for the party attribute type
   * @return <b>true</b> if the code is a valid code for a party attribute type or <b>false</b>
   *     otherwise
   */
  boolean isValidPartyAttributeType(String partyTypeCode, String partyAttributeTypeCode)
      throws ServiceUnavailableException;

  /**
   * Check whether the code is a valid code for a party attribute type category.
   *
   * @param partyAttributeTypeCategoryCode the code for the party attribute type category
   * @return <b>true</b> if the code is a valid code for a party attribute type category or
   *     <b>false</b> otherwise
   */
  boolean isValidPartyAttributeTypeCategory(String partyAttributeTypeCategoryCode)
      throws ServiceUnavailableException;

  /**
   * Check whether the code is a valid code for a party role purpose.
   *
   * @param partyRolePurposeCode the code for the party role purpose
   * @return <b>true</b> if the code is a valid code for a party role purpose or <b>false</b>
   *     otherwise
   */
  boolean isValidPartyRolePurpose(String partyRolePurposeCode) throws ServiceUnavailableException;

  /**
   * Check whether the code is a valid code for a party role type for the party type.
   *
   * @param partyTypeCode the party type code
   * @param partyRoleTypeCode the code for the party role type
   * @return <b>true</b> if the code is a valid code for a party role type or <b>false</b> otherwise
   */
  boolean isValidPartyRoleType(String partyTypeCode, String partyRoleTypeCode)
      throws ServiceUnavailableException;

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
   * Check whether the code is a valid code for a source of funds.
   *
   * @param sourceOfFundsCode the code for the source of funds
   * @return <b>true</b> if the code is a valid code for a source of funds or <b>false</b> otherwise
   */
  boolean isValidSourceOfFunds(String sourceOfFundsCode) throws ServiceUnavailableException;

  /**
   * Check whether the code is a valid code for a tax number type.
   *
   * @param taxNumberTypeCode the code for the tax number type
   * @return <b>true</b> if the code is a valid code for a tax number type or <b>false</b> otherwise
   */
  boolean isValidTaxNumberType(String taxNumberTypeCode) throws ServiceUnavailableException;

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

  /**
   * Update the organization.
   *
   * @param organization the organization
   */
  void updateOrganization(Organization organization)
      throws InvalidArgumentException, OrganizationNotFoundException, ServiceUnavailableException;

  /**
   * Update the person.
   *
   * @param person the person
   */
  void updatePerson(Person person)
      throws InvalidArgumentException, PersonNotFoundException, ServiceUnavailableException;

  /**
   * Validate the organization.
   *
   * @param organization the organization
   * @return the constraint violations for the organization
   */
  Set<ConstraintViolation<Organization>> validateOrganization(Organization organization);

  /**
   * Validate the party.
   *
   * @param party the party
   * @return the constraint violations for the party
   */
  Set<ConstraintViolation<Party>> validateParty(Party party);

  /**
   * Validate the person.
   *
   * @param person the person
   * @return the constraint violations for the person
   */
  Set<ConstraintViolation<Person>> validatePerson(Person person);

  /**
   * Validate the physical address.
   *
   * @param physicalAddress the physical address
   * @return the constraint violations for the physical address
   */
  Set<ConstraintViolation<PhysicalAddress>> validatePhysicalAddress(
      PhysicalAddress physicalAddress);
}
