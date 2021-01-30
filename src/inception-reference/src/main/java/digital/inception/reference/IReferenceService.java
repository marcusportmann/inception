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

import java.util.List;

/**
 * The <b>IReferenceService</b> interface defines the functionality provided by a Reference Service
 * implementation.
 *
 * @author Marcus Portmann
 */
public interface IReferenceService {

  /**
   * Retrieve all the contact mechanism purposes.
   *
   * @return the contact mechanism purposes
   */
  List<ContactMechanismPurpose> getContactMechanismPurposes() throws ReferenceServiceException;

  /**
   * Retrieve the contact mechanism purposes.
   *
   * @param localeId the Unicode locale identifier for the locale to retrieve the contact mechanism
   *     purposes for or <b>null</b> to retrieve the contact mechanism purposes for all locales
   * @return the contact mechanism purposes
   */
  List<ContactMechanismPurpose> getContactMechanismPurposes(String localeId)
      throws ReferenceServiceException;

  /**
   * Retrieve all the contact mechanism types.
   *
   * @return the contact mechanism types
   */
  List<ContactMechanismType> getContactMechanismTypes() throws ReferenceServiceException;

  /**
   * Retrieve the contact mechanism types.
   *
   * @param localeId the Unicode locale identifier for the locale to retrieve the contact mechanism
   *     types for or <b>null</b> to retrieve the contact mechanism types for all locales
   * @return the contact mechanism types
   */
  List<ContactMechanismType> getContactMechanismTypes(String localeId)
      throws ReferenceServiceException;

  /**
   * Retrieve all the countries.
   *
   * @return the countries
   */
  List<Country> getCountries() throws ReferenceServiceException;

  /**
   * Retrieve the countries.
   *
   * @param localeId the Unicode locale identifier for the locale to retrieve the countries for or
   *     <b>null</b> to retrieve the countries for all locales
   * @return the countries
   */
  List<Country> getCountries(String localeId) throws ReferenceServiceException;

  /**
   * Retrieve all the employment statuses.
   *
   * @return the employment statuses
   */
  List<EmploymentStatus> getEmploymentStatuses() throws ReferenceServiceException;

  /**
   * Retrieve the employment statuses.
   *
   * @param localeId the Unicode locale identifier for the locale to retrieve the employment
   *     statuses for or <b>null</b> to retrieve the employment statuses for all locales
   * @return the employment statuses
   */
  List<EmploymentStatus> getEmploymentStatuses(String localeId) throws ReferenceServiceException;

  /**
   * Retrieve all the employment types.
   *
   * @return the employment types
   */
  List<EmploymentType> getEmploymentTypes() throws ReferenceServiceException;

  /**
   * Retrieve the employment types.
   *
   * @param localeId the Unicode locale identifier for the locale to retrieve the employment types
   *     for or <b>null</b> to retrieve the employment types for all locales
   * @return the employment types
   */
  List<EmploymentType> getEmploymentTypes(String localeId) throws ReferenceServiceException;

  /**
   * Retrieve all the genders.
   *
   * @return the genders
   */
  List<Gender> getGenders() throws ReferenceServiceException;

  /**
   * Retrieve the genders.
   *
   * @param localeId the Unicode locale identifier for the locale to retrieve the genders for or
   *     <b>null</b> to retrieve the genders for all locales
   * @return the genders
   */
  List<Gender> getGenders(String localeId) throws ReferenceServiceException;

  /**
   * Retrieve all the identity document types.
   *
   * @return the identity document types
   */
  List<IdentityDocumentType> getIdentityDocumentTypes() throws ReferenceServiceException;

  /**
   * Retrieve the identity document types.
   *
   * @param localeId the Unicode locale identifier for the locale to retrieve the identity document
   *     types for or <b>null</b> to retrieve the identity document types for all locales
   * @return the identity document types
   */
  List<IdentityDocumentType> getIdentityDocumentTypes(String localeId)
      throws ReferenceServiceException;

  /**
   * Retrieve all the languages.
   *
   * @return the languages
   */
  List<Language> getLanguages() throws ReferenceServiceException;

  /**
   * Retrieve the languages.
   *
   * @param localeId the Unicode locale identifier for the locale to retrieve the languages for or
   *     <b>null</b> to retrieve the languages for all locales
   * @return the languages
   */
  List<Language> getLanguages(String localeId) throws ReferenceServiceException;

  /**
   * Retrieve all the marital statuses.
   *
   * @return the marital statuses
   */
  List<MaritalStatus> getMaritalStatuses() throws ReferenceServiceException;

  /**
   * Retrieve the marital statuses.
   *
   * @param localeId the Unicode locale identifier for the locale to retrieve the marital statuses
   *     for or <b>null</b> to retrieve the marital statuses for all locales
   * @return the marital statuses
   */
  List<MaritalStatus> getMaritalStatuses(String localeId) throws ReferenceServiceException;

  /**
   * Retrieve all the marriage types.
   *
   * @return the marriage types
   */
  List<MarriageType> getMarriageTypes() throws ReferenceServiceException;

  /**
   * Retrieve the marriage types.
   *
   * @param localeId the Unicode locale identifier for the locale to retrieve the marriage types for
   *     or <b>null</b> to retrieve the marriage types for all locales
   * @return the marriage types
   */
  List<MarriageType> getMarriageTypes(String localeId) throws ReferenceServiceException;

  /**
   * Retrieve all the next of kin types.
   *
   * @return the next of kin types
   */
  List<NextOfKinType> getNextOfKinTypes() throws ReferenceServiceException;

  /**
   * Retrieve the next of kin types.
   *
   * @param localeId the Unicode locale identifier for the locale to retrieve the next of kin types
   *     for or <b>null</b> to retrieve the next of kin types for all locales
   * @return the next of kin types
   */
  List<NextOfKinType> getNextOfKinTypes(String localeId) throws ReferenceServiceException;

  /**
   * Retrieve all the occupations.
   *
   * @return the occupations
   */
  List<Occupation> getOccupations() throws ReferenceServiceException;

  /**
   * Retrieve the occupations.
   *
   * @param localeId the Unicode locale identifier for the locale to retrieve the occupations for or
   *     <b>null</b> to retrieve the occupations for all locales
   * @return the occupations
   */
  List<Occupation> getOccupations(String localeId) throws ReferenceServiceException;

  /**
   * Retrieve all the party role purposes.
   *
   * @return the party role purposes
   */
  List<PartyRolePurpose> getPartyRolePurposes() throws ReferenceServiceException;

  /**
   * Retrieve the party role purposes.
   *
   * @param localeId the Unicode locale identifier for the locale to retrieve the party role
   *     purposes for or <b>null</b> to retrieve the party role purposes for all locales
   * @return the party role purposes
   */
  List<PartyRolePurpose> getPartyRolePurposes(String localeId) throws ReferenceServiceException;

  /**
   * Retrieve all the party role types.
   *
   * @return the party role types
   */
  List<PartyRoleType> getPartyRoleTypes() throws ReferenceServiceException;

  /**
   * Retrieve the party role types.
   *
   * @param localeId the Unicode locale identifier for the locale to retrieve the party role types
   *     for or <b>null</b> to retrieve the party role types for all locales
   * @return the party role types
   */
  List<PartyRoleType> getPartyRoleTypes(String localeId) throws ReferenceServiceException;

  /**
   * Retrieve all the physical address purposes.
   *
   * @return the physical address purposes
   */
  List<PhysicalAddressPurpose> getPhysicalAddressPurposes() throws ReferenceServiceException;

  /**
   * Retrieve the physical address purposes.
   *
   * @param localeId the Unicode locale identifier for the locale to retrieve the physical address
   *     purposes for or <b>null</b> to retrieve the physical address purposes for all locales
   * @return the physical address purposes
   */
  List<PhysicalAddressPurpose> getPhysicalAddressPurposes(String localeId)
      throws ReferenceServiceException;

  /**
   * Retrieve all the physical address types.
   *
   * @return the physical address types
   */
  List<PhysicalAddressType> getPhysicalAddressTypes() throws ReferenceServiceException;

  /**
   * Retrieve the physical address types.
   *
   * @param localeId the Unicode locale identifier for the locale to retrieve the physical address
   *     types for or <b>null</b> to retrieve the physical address types for all locales
   * @return the physical address types
   */
  List<PhysicalAddressType> getPhysicalAddressTypes(String localeId)
      throws ReferenceServiceException;

  /**
   * Retrieve all the preference type categories.
   *
   * @return the preference type categories
   */
  List<PreferenceTypeCategory> getPreferenceTypeCategories() throws ReferenceServiceException;

  /**
   * Retrieve the preference type categories.
   *
   * @param localeId the Unicode locale identifier for the locale to retrieve the preference type
   *     categories for or <b>null</b> to retrieve the preference type categories for all locales
   * @return the preference type categories
   */
  List<PreferenceTypeCategory> getPreferenceTypeCategories(String localeId)
      throws ReferenceServiceException;

  /**
   * Retrieve all the preference types.
   *
   * @return the preference types
   */
  List<PreferenceType> getPreferenceTypes() throws ReferenceServiceException;

  /**
   * Retrieve the preference types.
   *
   * @param localeId the Unicode locale identifier for the locale to retrieve the preference types
   *     for or <b>null</b> to retrieve the preference types for all locales
   * @return the preference types
   */
  List<PreferenceType> getPreferenceTypes(String localeId) throws ReferenceServiceException;

  /**
   * Retrieve all the races.
   *
   * @return the races
   */
  List<Race> getRaces() throws ReferenceServiceException;

  /**
   * Retrieve the races.
   *
   * @param localeId the Unicode locale identifier for the locale to retrieve the races for or
   *     <b>null</b> to retrieve the races for all locales
   * @return the races
   */
  List<Race> getRaces(String localeId) throws ReferenceServiceException;

  /**
   * Retrieve all the regions.
   *
   * @return the regions
   */
  List<Region> getRegions() throws ReferenceServiceException;

  /**
   * Retrieve the regions.
   *
   * @param localeId the Unicode locale identifier for the locale to retrieve the regions for or
   *     <b>null</b> to retrieve the regions for all locales
   * @return the regions
   */
  List<Region> getRegions(String localeId) throws ReferenceServiceException;

  /**
   * Retrieve all the residence permit types.
   *
   * @return the residence permit types
   */
  List<ResidencePermitType> getResidencePermitTypes() throws ReferenceServiceException;

  /**
   * Retrieve the residence permit types.
   *
   * @param localeId the Unicode locale identifier for the locale to retrieve the residence permit
   *     types for or <b>null</b> to retrieve the residence permit types for all locales
   * @return the residence permit types
   */
  List<ResidencePermitType> getResidencePermitTypes(String localeId)
      throws ReferenceServiceException;

  /**
   * Retrieve all the residency statuses.
   *
   * @return the residency statuses
   */
  List<ResidencyStatus> getResidencyStatuses() throws ReferenceServiceException;

  /**
   * Retrieve the residency statuses.
   *
   * @param localeId the Unicode locale identifier for the locale to retrieve the residency statuses
   *     for or <b>null</b> to retrieve the residency statuses for all locales
   * @return the residency statuses
   */
  List<ResidencyStatus> getResidencyStatuses(String localeId) throws ReferenceServiceException;

  /**
   * Retrieve all the residential types.
   *
   * @return the residential types
   */
  List<ResidentialType> getResidentialTypes() throws ReferenceServiceException;

  /**
   * Retrieve the residential types.
   *
   * @param localeId the Unicode locale identifier for the locale to retrieve the residential types
   *     for or <b>null</b> to retrieve the residential types for all locales
   * @return the residential types
   */
  List<ResidentialType> getResidentialTypes(String localeId) throws ReferenceServiceException;

  /**
   * Retrieve all the sources of funds.
   *
   * @return the sources of funds
   */
  List<SourceOfFunds> getSourcesOfFunds() throws ReferenceServiceException;

  /**
   * Retrieve the sources of funds.
   *
   * @param localeId the Unicode locale identifier for the locale to retrieve the sources of funds
   *     for or <b>null</b> to retrieve the sources of funds for all locales
   * @return the sources of funds
   */
  List<SourceOfFunds> getSourcesOfFunds(String localeId) throws ReferenceServiceException;

  /**
   * Retrieve all the tax number types.
   *
   * @return the tax number types
   */
  List<TaxNumberType> getTaxNumberTypes() throws ReferenceServiceException;

  /**
   * Retrieve the tax number types.
   *
   * @param localeId the Unicode locale identifier for the locale to retrieve the tax number types
   *     for or <b>null</b> to retrieve the tax number types for all locales
   * @return the tax number types
   */
  List<TaxNumberType> getTaxNumberTypes(String localeId) throws ReferenceServiceException;

  /**
   * Retrieve all the times to contact.
   *
   * @return the times to contact
   */
  List<TimeToContact> getTimesToContact() throws ReferenceServiceException;

  /**
   * Retrieve the times to contact.
   *
   * @param localeId the Unicode locale identifier for the locale to retrieve the times to contact
   *     for or <b>null</b> to retrieve the times to contact for all locales
   * @return the times to contact
   */
  List<TimeToContact> getTimesToContact(String localeId) throws ReferenceServiceException;

  /**
   * Retrieve all the titles.
   *
   * @return the titles
   */
  List<Title> getTitles() throws ReferenceServiceException;

  /**
   * Retrieve the titles.
   *
   * @param localeId the Unicode locale identifier for the locale to retrieve the titles for or
   *     <b>null</b> to retrieve the titles for all locales
   * @return the titles
   */
  List<Title> getTitles(String localeId) throws ReferenceServiceException;

  /**
   * Retrieve all the verification methods.
   *
   * @return the verification methods
   */
  List<VerificationMethod> getVerificationMethods() throws ReferenceServiceException;

  /**
   * Retrieve the verification methods.
   *
   * @param localeId the Unicode locale identifier for the locale to retrieve the verification
   *     methods for or <b>null</b> to retrieve the verification methods for all locales
   * @return the verification methods
   */
  List<VerificationMethod> getVerificationMethods(String localeId) throws ReferenceServiceException;

  /**
   * Retrieve all the verification statuses.
   *
   * @return the verification statuses
   */
  List<VerificationStatus> getVerificationStatuses() throws ReferenceServiceException;

  /**
   * Retrieve the verification statuses.
   *
   * @param localeId the Unicode locale identifier for the locale to retrieve the verification
   *     statuses for or <b>null</b> to retrieve the verification statuses for all locales
   * @return the verification statuses
   */
  List<VerificationStatus> getVerificationStatuses(String localeId)
      throws ReferenceServiceException;

  /**
   * Check whether the specified code is a valid code for a contact mechanism purpose.
   *
   * @param contactMechanismTypeCode the code for the contact mechanism type
   * @param contactMechanismPurposeCode the code for the contact mechanism purpose
   * @return <b>true</b> if the code is a valid code for a contact mechanism purpose or <b>false</b>
   *     otherwise
   */
  boolean isValidContactMechanismPurpose(
      String contactMechanismTypeCode, String contactMechanismPurposeCode)
      throws ReferenceServiceException;

  /**
   * Check whether the specified code is a valid code for a contact mechanism type.
   *
   * @param contactMechanismTypeCode the code for the contact mechanism type
   * @return <b>true</b> if the code is a valid code for a contact mechanism type or <b>false</b>
   *     otherwise
   */
  boolean isValidContactMechanismType(String contactMechanismTypeCode)
      throws ReferenceServiceException;

  /**
   * Check whether the specified code is a valid code for a country.
   *
   * @param countryCode the code for the country
   * @return <b>true</b> if the code is a valid code for a country or <b>false</b> otherwise
   */
  boolean isValidCountry(String countryCode) throws ReferenceServiceException;

  /**
   * Check whether the specified code is a valid code for an employment status.
   *
   * @param employmentStatusCode the code for the employment status
   * @return <b>true</b> if the code is a valid code for an employment status or <b>false</b>
   *     otherwise
   */
  boolean isValidEmploymentStatus(String employmentStatusCode) throws ReferenceServiceException;

  /**
   * Check whether the specified code is a valid code for an employment type.
   *
   * @param employmentStatusCode the code for the employment status
   * @param employmentTypeCode the code for the employment type
   * @return <b>true</b> if the code is a valid code for an employment type or <b>false</b>
   *     otherwise
   */
  boolean isValidEmploymentType(String employmentStatusCode, String employmentTypeCode)
      throws ReferenceServiceException;

  /**
   * Check whether the specified code is a valid code for a gender.
   *
   * @param genderCode the code for the gender
   * @return <b>true</b> if the code is a valid code for a gender or <b>false</b> otherwise
   */
  boolean isValidGender(String genderCode) throws ReferenceServiceException;

  /**
   * Check whether the specified code is a valid code for a identity document type.
   *
   * @param identityDocumentTypeCode the code for the identity document type
   * @return <b>true</b> if the code is a valid code for a identity document type or <b>false</b>
   *     otherwise
   */
  boolean isValidIdentityDocumentType(String identityDocumentTypeCode)
      throws ReferenceServiceException;
  /**
   * Check whether the specified code is a valid code for a language.
   *
   * @param languageCode the code for the language
   * @return <b>true</b> if the code is a valid code for a language or <b>false</b> otherwise
   */
  boolean isValidLanguage(String languageCode) throws ReferenceServiceException;

  /**
   * Check whether the specified code is a valid code for a marital status.
   *
   * @param maritalStatusCode the code for the marital status
   * @return <b>true</b> if the code is a valid code for a marital status or <b>false</b> otherwise
   */
  boolean isValidMaritalStatus(String maritalStatusCode) throws ReferenceServiceException;

  /**
   * Check whether the specified code is a valid code for a marriage type.
   *
   * @param maritalStatusCode the code for the marital status
   * @param marriageTypeCode the code for the marriage type
   * @return <b>true</b> if the code is a valid code for a marriage type or <b>false</b> otherwise
   */
  boolean isValidMarriageType(String maritalStatusCode, String marriageTypeCode)
      throws ReferenceServiceException;

  /**
   * Check whether the specified code is a valid code for a next of kin type.
   *
   * @param nextOfKinTypeCode the code for the next of kin type
   * @return <b>true</b> if the code is a valid code for a next of kin type or <b>false</b>
   *     otherwise
   */
  boolean isValidNextOfKinType(String nextOfKinTypeCode) throws ReferenceServiceException;

  /**
   * Check whether the specified code is a valid code for a occupation.
   *
   * @param occupationCode the code for the occupation
   * @return <b>true</b> if the code is a valid code for a occupation or <b>false</b> otherwise
   */
  boolean isValidOccupation(String occupationCode) throws ReferenceServiceException;

  /**
   * Check whether the specified code is a valid code for a party role purpose.
   *
   * @param partyRolePurposeCode the code for the party role purpose
   * @return <b>true</b> if the code is a valid code for a party role purpose or <b>false</b>
   *     otherwise
   */
  boolean isValidPartyRolePurpose(String partyRolePurposeCode) throws ReferenceServiceException;

  /**
   * Check whether the specified code is a valid code for a party role type.
   *
   * @param partyRoleTypeCode the code for the party role type
   * @return <b>true</b> if the code is a valid code for a party role type or <b>false</b> otherwise
   */
  boolean isValidPartyRoleType(String partyRoleTypeCode) throws ReferenceServiceException;

  /**
   * Check whether the specified code is a valid code for a physical address purpose.
   *
   * @param physicalAddressPurposeCode the code for the physical address purpose
   * @return <b>true</b> if the code is a valid code for a physical address purpose or <b>false</b>
   *     otherwise
   */
  boolean isValidPhysicalAddressPurpose(String physicalAddressPurposeCode)
      throws ReferenceServiceException;

  /**
   * Check whether the specified code is a valid code for a physical address type.
   *
   * @param physicalAddressTypeCode the code for the physical address type
   * @return <b>true</b> if the code is a valid code for a physical address type or <b>false</b>
   *     otherwise
   */
  boolean isValidPhysicalAddressType(String physicalAddressTypeCode)
      throws ReferenceServiceException;

  /**
   * Check whether the specified code is a valid code for a preference type.
   *
   * @param preferenceTypeCode the code for the preference type
   * @return <b>true</b> if the code is a valid code for a preference type or <b>false</b> otherwise
   */
  boolean isValidPreferenceType(String preferenceTypeCode) throws ReferenceServiceException;

  /**
   * Check whether the specified code is a valid code for a preference type category.
   *
   * @param preferenceTypeCategoryCode the code for the preference type category
   * @return <b>true</b> if the code is a valid code for a preference type category or <b>false</b>
   *     otherwise
   */
  boolean isValidPreferenceTypeCategory(String preferenceTypeCategoryCode)
      throws ReferenceServiceException;

  /**
   * Check whether the specified code is a valid code for a race.
   *
   * @param raceCode the code for the race
   * @return <b>true</b> if the code is a valid code for a race or <b>false</b> otherwise
   */
  boolean isValidRace(String raceCode) throws ReferenceServiceException;

  /**
   * Check whether the specified code is a valid code for a region.
   *
   * @param regionCode the code for the region
   * @return <b>true</b> if the code is a valid code for a region or <b>false</b> otherwise
   */
  boolean isValidRegion(String regionCode) throws ReferenceServiceException;
  /**
   * Check whether the specified code is a valid code for a residence permit type.
   *
   * @param residencePermitTypeCode the code for the residence permit type
   * @return <b>true</b> if the code is a valid code for a residence permit type or <b>false</b>
   *     otherwise
   */
  boolean isValidResidencePermitType(String residencePermitTypeCode)
      throws ReferenceServiceException;

  /**
   * Check whether the specified code is a valid code for a residency status.
   *
   * @param residencyStatusCode the code for the residency status
   * @return <b>true</b> if the code is a valid code for a residency status or <b>false</b>
   *     otherwise
   */
  boolean isValidResidencyStatus(String residencyStatusCode) throws ReferenceServiceException;

  /**
   * Check whether the specified code is a valid code for a residential type.
   *
   * @param residentialTypeCode the code for the residential type
   * @return <b>true</b> if the code is a valid code for a residential type or <b>false</b>
   *     otherwise
   */
  boolean isValidResidentialType(String residentialTypeCode) throws ReferenceServiceException;

  /**
   * Check whether the specified code is a valid code for a source of funds.
   *
   * @param sourceOfFundsCode the code for the source of funds
   * @return <b>true</b> if the code is a valid code for a source of funds or <b>false</b> otherwise
   */
  boolean isValidSourceOfFunds(String sourceOfFundsCode) throws ReferenceServiceException;

  /**
   * Check whether the specified code is a valid code for a tax number type.
   *
   * @param taxNumberTypeCode the code for the tax number type
   * @return <b>true</b> if the code is a valid code for a tax number type or <b>false</b> otherwise
   */
  boolean isValidTaxNumberType(String taxNumberTypeCode) throws ReferenceServiceException;

  /**
   * Check whether the specified code is a valid code for a time to contact.
   *
   * @param timeToContactCode the code for the time to contact
   * @return <b>true</b> if the code is a valid code for a time to contact or <b>false</b> otherwise
   */
  boolean isValidTimeToContact(String timeToContactCode) throws ReferenceServiceException;

  /**
   * Check whether the specified code is a valid code for a title.
   *
   * @param titleCode the code for the title
   * @return <b>true</b> if the code is a valid code for a title or <b>false</b> otherwise
   */
  boolean isValidTitle(String titleCode) throws ReferenceServiceException;

  /**
   * Check whether the specified code is a valid code for a verification method.
   *
   * @param verificationMethodCode the code for the verification method
   * @return <b>true</b> if the code is a valid code for a verification method or <b>false</b>
   *     otherwise
   */
  boolean isValidVerificationMethod(String verificationMethodCode) throws ReferenceServiceException;

  /**
   * Check whether the specified code is a valid code for a verification status.
   *
   * @param verificationStatusCode the code for the verification status
   * @return <b>true</b> if the code is a valid code for a verification status or <b>false</b>
   *     otherwise
   */
  boolean isValidVerificationStatus(String verificationStatusCode) throws ReferenceServiceException;
}
