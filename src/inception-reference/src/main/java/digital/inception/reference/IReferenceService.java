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

import digital.inception.core.service.ServiceUnavailableException;
import java.util.List;

/**
 * The <b>IReferenceService</b> interface defines the functionality provided by a Reference Service
 * implementation.
 *
 * @author Marcus Portmann
 */
public interface IReferenceService {

  /**
   * Retrieve all the countries.
   *
   * @return the countries
   */
  List<Country> getCountries() throws ServiceUnavailableException;

  /**
   * Retrieve the countries.
   *
   * @param localeId the Unicode locale identifier for the locale to retrieve the countries for or
   *     <b>null</b> to retrieve the countries for all locales
   * @return the countries
   */
  List<Country> getCountries(String localeId) throws ServiceUnavailableException;

  /**
   * Retrieve all the languages.
   *
   * @return the languages
   */
  List<Language> getLanguages() throws ServiceUnavailableException;

  /**
   * Retrieve the languages.
   *
   * @param localeId the Unicode locale identifier for the locale to retrieve the languages for or
   *     <b>null</b> to retrieve the languages for all locales
   * @return the languages
   */
  List<Language> getLanguages(String localeId) throws ServiceUnavailableException;

  /**
   * Retrieve all the regions.
   *
   * @return the regions
   */
  List<Region> getRegions() throws ServiceUnavailableException;

  /**
   * Retrieve the regions.
   *
   * @param localeId the Unicode locale identifier for the locale to retrieve the regions for or
   *     <b>null</b> to retrieve the regions for all locales
   * @return the regions
   */
  List<Region> getRegions(String localeId) throws ServiceUnavailableException;

  /**
   * Retrieve all the verification methods.
   *
   * @return the verification methods
   */
  List<VerificationMethod> getVerificationMethods() throws ServiceUnavailableException;

  /**
   * Retrieve the verification methods.
   *
   * @param localeId the Unicode locale identifier for the locale to retrieve the verification
   *     methods for or <b>null</b> to retrieve the verification methods for all locales
   * @return the verification methods
   */
  List<VerificationMethod> getVerificationMethods(String localeId)
      throws ServiceUnavailableException;

  /**
   * Retrieve all the verification statuses.
   *
   * @return the verification statuses
   */
  List<VerificationStatus> getVerificationStatuses() throws ServiceUnavailableException;

  /**
   * Retrieve the verification statuses.
   *
   * @param localeId the Unicode locale identifier for the locale to retrieve the verification
   *     statuses for or <b>null</b> to retrieve the verification statuses for all locales
   * @return the verification statuses
   */
  List<VerificationStatus> getVerificationStatuses(String localeId)
      throws ServiceUnavailableException;

  /**
   * Check whether the code is a valid code for a country.
   *
   * @param countryCode the code for the country
   * @return <b>true</b> if the code is a valid code for a country or <b>false</b> otherwise
   */
  boolean isValidCountry(String countryCode) throws ServiceUnavailableException;

  /**
   * Check whether the code is a valid code for a language.
   *
   * @param languageCode the code for the language
   * @return <b>true</b> if the code is a valid code for a language or <b>false</b> otherwise
   */
  boolean isValidLanguage(String languageCode) throws ServiceUnavailableException;

  /**
   * Check whether the code is a valid code for a region.
   *
   * @param regionCode the code for the region
   * @return <b>true</b> if the code is a valid code for a region or <b>false</b> otherwise
   */
  boolean isValidRegion(String regionCode) throws ServiceUnavailableException;

  /**
   * Check whether the code is a valid code for a verification method.
   *
   * @param verificationMethodCode the code for the verification method
   * @return <b>true</b> if the code is a valid code for a verification method or <b>false</b>
   *     otherwise
   */
  boolean isValidVerificationMethod(String verificationMethodCode)
      throws ServiceUnavailableException;

  /**
   * Check whether the code is a valid code for a verification status.
   *
   * @param verificationStatusCode the code for the verification status
   * @return <b>true</b> if the code is a valid code for a verification status or <b>false</b>
   *     otherwise
   */
  boolean isValidVerificationStatus(String verificationStatusCode)
      throws ServiceUnavailableException;
}
