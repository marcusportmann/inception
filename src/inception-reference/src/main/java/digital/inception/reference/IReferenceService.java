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
@SuppressWarnings("unused")
public interface IReferenceService {

  /**
   * Retrieve all the communication methods.
   *
   * @return the communication methods
   */
  List<CommunicationMethod> getCommunicationMethods() throws ReferenceServiceException;

  /**
   * Retrieve the communication methods.
   *
   * @param localeId the Unicode locale identifier identifying the locale to retrieve the
   *     communication methods for or <code>null</code> to retrieve the communication methods for
   *     all locales
   * @return the communication methods
   */
  List<CommunicationMethod> getCommunicationMethods(String localeId)
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
   * @param localeId the Unicode locale identifier identifying the locale to retrieve the identity document
   *     types for or <code>null</code> to retrieve the identity document types for all locales
   * @return the identity document types
   */
  List<IdentityDocumentType> getIdentityDocumentTypes(String localeId) throws ReferenceServiceException;
  
  
}
