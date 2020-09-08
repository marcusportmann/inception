/*
 * Copyright 2017 Marcus Portmann
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

// ~--- JDK imports ------------------------------------------------------------

import java.util.List;

/**
 * The <code>IReferenceService</code> interface defines the functionality provided by a Reference
 * Service implementation.
 *
 * @author Marcus Portmann
 */
public interface IReferenceService {

  /**
   * Retrieve all the countries.
   *
   * @return the countries
   */
  List<Country> getCountries() throws ReferenceServiceException;

  /**
   * Retrieve the countries.
   *
   * @param localeId the Unicode locale identifier identifying the locale to retrieve the countries
   *     for or <code>null</code> to retrieve the countries for all locales
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
   * @param localeId the Unicode locale identifier identifying the locale to retrieve the employment
   *     statuses for or <code>null</code> to retrieve the employment statuses for all locales
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
   * @param localeId the Unicode locale identifier identifying the locale to retrieve the employment
   *     types for or <code>null</code> to retrieve the employment types for all locales
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
   * @param localeId the Unicode locale identifier identifying the locale to retrieve the genders
   *     for or <code>null</code> to retrieve the genders for all locales
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
   * @param localeId the Unicode locale identifier identifying the locale to retrieve the identity
   *     document types for or <code>null</code> to retrieve the identity document types for all
   *     locales
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
   * @param localeId the Unicode locale identifier identifying the locale to retrieve the languages
   *     for or <code>null</code> to retrieve the languages for all locales
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
   * @param localeId the Unicode locale identifier identifying the locale to retrieve the marital
   *     statuses for or <code>null</code> to retrieve the marital statuses for all locales
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
   * @param localeId the Unicode locale identifier identifying the locale to retrieve the marriage
   *     types for or <code>null</code> to retrieve the marriage types for all locales
   * @return the marriage types
   */
  List<MarriageType> getMarriageTypes(String localeId) throws ReferenceServiceException;

  /**
   * Retrieve all the minor types.
   *
   * @return the minor types
   */
  List<MinorType> getMinorTypes() throws ReferenceServiceException;

  /**
   * Retrieve the minor types.
   *
   * @param localeId the Unicode locale identifier identifying the locale to retrieve the minor
   *     types for or <code>null</code> to retrieve the minor types for all locales
   * @return the minor types
   */
  List<MinorType> getMinorTypes(String localeId) throws ReferenceServiceException;

  /**
   * Retrieve all the next of kin types.
   *
   * @return the next of kin types
   */
  List<NextOfKinType> getNextOfKinTypes() throws ReferenceServiceException;

  /**
   * Retrieve the next of kin types.
   *
   * @param localeId the Unicode locale identifier identifying the locale to retrieve the next of
   *     kin types for or <code>null</code> to retrieve the next of kin types for all locales
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
   * @param localeId the Unicode locale identifier identifying the locale to retrieve the
   *     occupations for or <code>null</code> to retrieve the occupations for all locales
   * @return the occupations
   */
  List<Occupation> getOccupations(String localeId) throws ReferenceServiceException;

  /**
   * Retrieve all the races.
   *
   * @return the races
   */
  List<Race> getRaces() throws ReferenceServiceException;

  /**
   * Retrieve the races.
   *
   * @param localeId the Unicode locale identifier identifying the locale to retrieve the races for
   *     or <code>null</code> to retrieve the races for all locales
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
   * @param localeId the Unicode locale identifier identifying the locale to retrieve the regions
   *     for or <code>null</code> to retrieve the regions for all locales
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
   * @param localeId the Unicode locale identifier identifying the locale to retrieve the residence
   *     permit types for or <code>null</code> to retrieve the residence permit types for all
   *     locales
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
   * @param localeId the Unicode locale identifier identifying the locale to retrieve the residency
   *     statuses for or <code>null</code> to retrieve the residency statuses for all locales
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
   * @param localeId the Unicode locale identifier identifying the locale to retrieve the
   *     residential types for or <code>null</code> to retrieve the residential types for all
   *     locales
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
   * @param localeId the Unicode locale identifier identifying the locale to retrieve the sources of
   *     funds for or <code>null</code> to retrieve the sources of funds for all locales
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
   * @param localeId the Unicode locale identifier identifying the locale to retrieve the tax number
   *     types for or <code>null</code> to retrieve the tax number types for all locales
   * @return the tax number types
   */
  List<TaxNumberType> getTaxNumberTypes(String localeId) throws ReferenceServiceException;

  /**
   * Retrieve all the titles.
   *
   * @return the titles
   */
  List<Title> getTitles() throws ReferenceServiceException;

  /**
   * Retrieve the titles.
   *
   * @param localeId the Unicode locale identifier identifying the locale to retrieve the titles for
   *     or <code>null</code> to retrieve the titles for all locales
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
   * @param localeId the Unicode locale identifier identifying the locale to retrieve the
   *     verification methods for or <code>null</code> to retrieve the verification methods for all
   *     locales
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
   * @param localeId the Unicode locale identifier identifying the locale to retrieve the
   *     verification statuses for or <code>null</code> to retrieve the verification statuses for
   *     all locales
   * @return the verification statuses
   */
  List<VerificationStatus> getVerificationStatuses(String localeId)
      throws ReferenceServiceException;
}
