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

// ~--- non-JDK imports --------------------------------------------------------

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

// ~--- JDK imports ------------------------------------------------------------

/**
 * The <code>ReferenceService</code> class provides the Reference Service implementation.
 *
 * @author Marcus Portmann
 */
@Service
@SuppressWarnings("unused")
public class ReferenceService implements IReferenceService {

  /* Logger */
  private static final Logger logger = LoggerFactory.getLogger(ReferenceService.class);

  /** The Communication Method Repository. */
  private final CommunicationMethodRepository communicationMethodRepository;

  /** The Country repository. */
  private final CountryRepository countryRepository;

  /** The Employment Status Repository */
  private final EmploymentStatusRepository employmentStatusRepository;

  /** The Employment Type Repository. */
  private final EmploymentTypeRepository employmentTypeRepository;

  /** The Gender Repository. */
  private final GenderRepository genderRepository;

  /** The Identity Document Type Repository. */
  private final IdentityDocumentTypeRepository identityDocumentTypeRepository;

  /**
   * Constructs a new <code>ReferenceService</code>.
   *
   * @param countryRepository the Country Repository
   * @param communicationMethodRepository the Communication Method Repository
   * @param employmentStatusRepository the Employment Status Repository
   * @param employmentTypeRepository the Employment Type Repository
   * @param genderRepository the Gender Repository
   * @param identityDocumentTypeRepository the Identity Document Type Repository
   */
  public ReferenceService(
      CountryRepository countryRepository,
      CommunicationMethodRepository communicationMethodRepository,
      EmploymentStatusRepository employmentStatusRepository,
      EmploymentTypeRepository employmentTypeRepository,
      GenderRepository genderRepository,
      IdentityDocumentTypeRepository identityDocumentTypeRepository) {
    this.countryRepository = countryRepository;
    this.communicationMethodRepository = communicationMethodRepository;
    this.employmentStatusRepository = employmentStatusRepository;
    this.employmentTypeRepository = employmentTypeRepository;
    this.genderRepository = genderRepository;
    this.identityDocumentTypeRepository = identityDocumentTypeRepository;
  }

  /**
   * Retrieve all the communication methods.
   *
   * @return the communication methods
   */
  @Override
  public List<CommunicationMethod> getCommunicationMethods() throws ReferenceServiceException {
    return getCommunicationMethods(null);
  }

  /**
   * Retrieve the communication methods.
   *
   * @param localeId the Unicode locale identifier identifying the locale to retrieve the
   *     communication methods for or <code>null</code> to retrieve the communication methods for
   *     all locales
   * @return the communication methods
   */
  @Override
  public List<CommunicationMethod> getCommunicationMethods(String localeId)
      throws ReferenceServiceException {
    try {
      if (StringUtils.isEmpty(localeId)) {
        return communicationMethodRepository.findAll(Sort.by(Direction.ASC, "locale", "sortIndex"));
      } else {
        return communicationMethodRepository.findByLocaleIgnoreCase(
            localeId, Sort.by(Direction.ASC, "locale", "sortIndex"));
      }
    } catch (Throwable e) {
      throw new ReferenceServiceException("Failed to retrieve the communication methods", e);
    }
  }

  /**
   * Retrieve all the countries.
   *
   * @return the countries
   */
  @Override
  public List<Country> getCountries() throws ReferenceServiceException {
    return getCountries(null);
  }

  /**
   * Retrieve the countries.
   *
   * @param localeId the Unicode locale identifier identifying the locale to retrieve the countries
   *     for or <code>null</code> to retrieve the countries for all locales
   * @return the countries
   */
  @Override
  public List<Country> getCountries(String localeId) throws ReferenceServiceException {
    try {
      if (StringUtils.isEmpty(localeId)) {
        return countryRepository.findAll(Sort.by(Direction.ASC, "locale", "sortIndex"));
      } else {
        return countryRepository.findByLocaleIgnoreCase(
            localeId, Sort.by(Direction.ASC, "locale", "sortIndex"));
      }
    } catch (Throwable e) {
      throw new ReferenceServiceException("Failed to retrieve the countries", e);
    }
  }

  /**
   * Retrieve all the employment statuses.
   *
   * @return the employment statuses
   */
  @Override
  public List<EmploymentStatus> getEmploymentStatuses() throws ReferenceServiceException {
    return getEmploymentStatuses(null);
  }

  /**
   * Retrieve the employment statuses.
   *
   * @param localeId the Unicode locale identifier identifying the locale to retrieve the employment
   *     statuses for or <code>null</code> to retrieve the employment statuses for all locales
   * @return the employment statuses
   */
  @Override
  public List<EmploymentStatus> getEmploymentStatuses(String localeId)
      throws ReferenceServiceException {
    try {
      if (StringUtils.isEmpty(localeId)) {
        return employmentStatusRepository.findAll(Sort.by(Direction.ASC, "locale", "sortIndex"));
      } else {
        return employmentStatusRepository.findByLocaleIgnoreCase(
            localeId, Sort.by(Direction.ASC, "locale", "sortIndex"));
      }
    } catch (Throwable e) {
      throw new ReferenceServiceException("Failed to retrieve the employment statuses", e);
    }
  }

  /**
   * Retrieve all the employment types.
   *
   * @return the employment types
   */
  @Override
  public List<EmploymentType> getEmploymentTypes() throws ReferenceServiceException {
    return getEmploymentTypes(null);
  }

  /**
   * Retrieve the employment types.
   *
   * @param localeId the Unicode locale identifier identifying the locale to retrieve the employment
   *     types for or <code>null</code> to retrieve the employment types for all locales
   * @return the employment types
   */
  @Override
  public List<EmploymentType> getEmploymentTypes(String localeId) throws ReferenceServiceException {
    try {
      if (StringUtils.isEmpty(localeId)) {
        return employmentTypeRepository.findAll(Sort.by(Direction.ASC, "locale", "sortIndex"));
      } else {
        return employmentTypeRepository.findByLocaleIgnoreCase(
            localeId, Sort.by(Direction.ASC, "locale", "sortIndex"));
      }
    } catch (Throwable e) {
      throw new ReferenceServiceException("Failed to retrieve the employment types", e);
    }
  }

  /**
   * Retrieve the genders.
   *
   * @param localeId the Unicode locale identifier identifying the locale to retrieve the genders
   *     for or <code>null</code> to retrieve the genders for all locales
   * @return the genders
   */
  @Override
  public List<Gender> getGenders(String localeId) throws ReferenceServiceException {
    try {
      if (StringUtils.isEmpty(localeId)) {
        return genderRepository.findAll(Sort.by(Direction.ASC, "locale", "sortIndex"));
      } else {
        return genderRepository.findByLocaleIgnoreCase(
            localeId, Sort.by(Direction.ASC, "locale", "sortIndex"));
      }
    } catch (Throwable e) {
      throw new ReferenceServiceException("Failed to retrieve the genders", e);
    }
  }

  /**
   * Retrieve all the genders.
   *
   * @return the genders
   */
  @Override
  public List<Gender> getGenders() throws ReferenceServiceException {
    return getGenders(null);
  }

  /**
   * Retrieve all the identity document types.
   *
   * @return the identity document types
   */
  @Override
  public List<IdentityDocumentType> getIdentityDocumentTypes() throws ReferenceServiceException {
    return getIdentityDocumentTypes(null);
  }

  /**
   * Retrieve the identity document types.
   *
   * @param localeId the Unicode locale identifier identifying the locale to retrieve the identity
   *     document types for or <code>null</code> to retrieve the identity document types for all
   *     locales
   * @return the identity document types
   */
  @Override
  public List<IdentityDocumentType> getIdentityDocumentTypes(String localeId)
      throws ReferenceServiceException {
    try {
      if (StringUtils.isEmpty(localeId)) {
        return identityDocumentTypeRepository.findAll(
            Sort.by(Direction.ASC, "locale", "sortIndex"));
      } else {
        return identityDocumentTypeRepository.findByLocaleIgnoreCase(
            localeId, Sort.by(Direction.ASC, "locale", "sortIndex"));
      }
    } catch (Throwable e) {
      throw new ReferenceServiceException("Failed to retrieve the identity document types", e);
    }
  }
}
