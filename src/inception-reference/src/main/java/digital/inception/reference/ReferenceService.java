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

  /** The Address Type Repository. */
  private final AddressTypeRepository addressTypeRepository;

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

  /** The Language Repository. */
  private final LanguageRepository languageRepository;

  /** The Marital Status Repository. */
  private final MaritalStatusRepository maritalStatusRepository;

  /** The Marriage Type Repository. */
  private final MarriageTypeRepository marriageTypeRepository;

  /** The Minor Type Repository. */
  private final MinorTypeRepository minorTypeRepository;

  /** The Next Of Kin Type Repository. */
  private final NextOfKinTypeRepository nextOfKinTypeRepository;

  /** The Occupation Repository. */
  private final OccupationRepository occupationRepository;

  /** The Permit Type Repository. */
  private final PermitTypeRepository permitTypeRepository;

  /** The Race Repository. */
  private final RaceRepository raceRepository;

  /** The Region Repository. */
  private final RegionRepository regionRepository;

  /** The Residential Status Repository. */
  private final ResidentialStatusRepository residentialStatusRepository;

  /**
   * Constructs a new <code>ReferenceService</code>.
   *
   * @param addressTypeRepository the Address Type Repository
   * @param countryRepository the Country Repository
   * @param communicationMethodRepository the Communication Method Repository
   * @param employmentStatusRepository the Employment Status Repository
   * @param employmentTypeRepository the Employment Type Repository
   * @param genderRepository the Gender Repository
   * @param identityDocumentTypeRepository the Identity Document Type Repository
   * @param languageRepository the Language Repository
   * @param maritalStatusRepository the Marital Status Repository
   * @param marriageTypeRepository the Marriage Type Repository
   * @param minorTypeRepository the Minor Type Repository
   * @param nextOfKinTypeRepository the Next Of Kin Repository
   * @param occupationRepository the Occupation Repository
   * @param permitTypeRepository the Permit Type Repository
   * @param raceRepository the Race Repository
   * @param regionRepository the Region Repository
   * @param residentialStatusRepository the Residential Status Repository
   */
  public ReferenceService(
      AddressTypeRepository addressTypeRepository,
      CountryRepository countryRepository,
      CommunicationMethodRepository communicationMethodRepository,
      EmploymentStatusRepository employmentStatusRepository,
      EmploymentTypeRepository employmentTypeRepository,
      GenderRepository genderRepository,
      IdentityDocumentTypeRepository identityDocumentTypeRepository,
      LanguageRepository languageRepository,
      MaritalStatusRepository maritalStatusRepository,
      MarriageTypeRepository marriageTypeRepository,
      MinorTypeRepository minorTypeRepository,
      NextOfKinTypeRepository nextOfKinTypeRepository,
      OccupationRepository occupationRepository,
      PermitTypeRepository permitTypeRepository,
      RaceRepository raceRepository,
      RegionRepository regionRepository,
      ResidentialStatusRepository residentialStatusRepository) {
    this.addressTypeRepository = addressTypeRepository;
    this.countryRepository = countryRepository;
    this.communicationMethodRepository = communicationMethodRepository;
    this.employmentStatusRepository = employmentStatusRepository;
    this.employmentTypeRepository = employmentTypeRepository;
    this.genderRepository = genderRepository;
    this.identityDocumentTypeRepository = identityDocumentTypeRepository;
    this.languageRepository = languageRepository;
    this.maritalStatusRepository = maritalStatusRepository;
    this.marriageTypeRepository = marriageTypeRepository;
    this.minorTypeRepository = minorTypeRepository;
    this.nextOfKinTypeRepository = nextOfKinTypeRepository;
    this.occupationRepository = occupationRepository;
    this.permitTypeRepository = permitTypeRepository;
    this.raceRepository = raceRepository;
    this.regionRepository = regionRepository;
    this.residentialStatusRepository = residentialStatusRepository;
  }

  /**
   * Retrieve all the address types.
   *
   * @return the address types
   */
  @Override
  public List<AddressType> getAddressTypes() throws ReferenceServiceException {
    return getAddressTypes(null);
  }

  /**
   * Retrieve the address types.
   *
   * @param localeId the Unicode locale identifier identifying the locale to retrieve the address
   *     types for or <code>null</code> to retrieve the address types for all locales
   * @return the address types
   */
  @Override
  public List<AddressType> getAddressTypes(String localeId) throws ReferenceServiceException {
    try {
      if (StringUtils.isEmpty(localeId)) {
        return addressTypeRepository.findAll(Sort.by(Direction.ASC, "locale", "sortIndex"));
      } else {
        return addressTypeRepository.findByLocaleIgnoreCase(
            localeId, Sort.by(Direction.ASC, "locale", "sortIndex"));
      }
    } catch (Throwable e) {
      throw new ReferenceServiceException("Failed to retrieve the address types", e);
    }
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

  /**
   * Retrieve all the languages.
   *
   * @return the languages
   */
  @Override
  public List<Language> getLanguages() throws ReferenceServiceException {
    return getLanguages(null);
  }

  /**
   * Retrieve the languages.
   *
   * @param localeId the Unicode locale identifier identifying the locale to retrieve the languages
   *     for or <code>null</code> to retrieve the languages for all locales
   * @return the languages
   */
  @Override
  public List<Language> getLanguages(String localeId) throws ReferenceServiceException {
    try {
      if (StringUtils.isEmpty(localeId)) {
        return languageRepository.findAll(Sort.by(Direction.ASC, "locale", "sortIndex"));
      } else {
        return languageRepository.findByLocaleIgnoreCase(
            localeId, Sort.by(Direction.ASC, "locale", "sortIndex"));
      }
    } catch (Throwable e) {
      throw new ReferenceServiceException("Failed to retrieve the languages", e);
    }
  }

  /**
   * Retrieve all the marital statuses.
   *
   * @return the marital statuses
   */
  @Override
  public List<MaritalStatus> getMaritalStatuses() throws ReferenceServiceException {
    return getMaritalStatuses(null);
  }

  /**
   * Retrieve the marital statuses.
   *
   * @param localeId the Unicode locale identifier identifying the locale to retrieve the marital
   *     statuses for or <code>null</code> to retrieve the marital statuses for all locales
   * @return the marital statuses
   */
  @Override
  public List<MaritalStatus> getMaritalStatuses(String localeId) throws ReferenceServiceException {
    try {
      if (StringUtils.isEmpty(localeId)) {
        return maritalStatusRepository.findAll(Sort.by(Direction.ASC, "locale", "sortIndex"));
      } else {
        return maritalStatusRepository.findByLocaleIgnoreCase(
            localeId, Sort.by(Direction.ASC, "locale", "sortIndex"));
      }
    } catch (Throwable e) {
      throw new ReferenceServiceException("Failed to retrieve the marital statuses", e);
    }
  }

  /**
   * Retrieve all the marriage types.
   *
   * @return the marriage types
   */
  @Override
  public List<MarriageType> getMarriageTypes() throws ReferenceServiceException {
    return getMarriageTypes(null);
  }

  /**
   * Retrieve the marriage types.
   *
   * @param localeId the Unicode locale identifier identifying the locale to retrieve the marriage
   *     types for or <code>null</code> to retrieve the marriage types for all locales
   * @return the marriage types
   */
  @Override
  public List<MarriageType> getMarriageTypes(String localeId) throws ReferenceServiceException {
    try {
      if (StringUtils.isEmpty(localeId)) {
        return marriageTypeRepository.findAll(Sort.by(Direction.ASC, "locale", "sortIndex"));
      } else {
        return marriageTypeRepository.findByLocaleIgnoreCase(
            localeId, Sort.by(Direction.ASC, "locale", "sortIndex"));
      }
    } catch (Throwable e) {
      throw new ReferenceServiceException("Failed to retrieve the marriage types", e);
    }
  }

  /**
   * Retrieve all the minor types.
   *
   * @return the minor types
   */
  @Override
  public List<MinorType> getMinorTypes() throws ReferenceServiceException {
    return getMinorTypes(null);
  }

  /**
   * Retrieve the minor types.
   *
   * @param localeId the Unicode locale identifier identifying the locale to retrieve the minor
   *     types for or <code>null</code> to retrieve the minor types for all locales
   * @return the minor types
   */
  @Override
  public List<MinorType> getMinorTypes(String localeId) throws ReferenceServiceException {
    try {
      if (StringUtils.isEmpty(localeId)) {
        return minorTypeRepository.findAll(Sort.by(Direction.ASC, "locale", "sortIndex"));
      } else {
        return minorTypeRepository.findByLocaleIgnoreCase(
            localeId, Sort.by(Direction.ASC, "locale", "sortIndex"));
      }
    } catch (Throwable e) {
      throw new ReferenceServiceException("Failed to retrieve the minor types", e);
    }
  }

  /**
   * Retrieve all the next of kin types.
   *
   * @return the next of kin types
   */
  @Override
  public List<NextOfKinType> getNextOfKinTypes() throws ReferenceServiceException {
    return getNextOfKinTypes(null);
  }

  /**
   * Retrieve the next of kin types.
   *
   * @param localeId the Unicode locale identifier identifying the locale to retrieve the next of
   *     kin types for or <code>null</code> to retrieve the next of kin types for all locales
   * @return the next of kin types
   */
  @Override
  public List<NextOfKinType> getNextOfKinTypes(String localeId) throws ReferenceServiceException {
    try {
      if (StringUtils.isEmpty(localeId)) {
        return nextOfKinTypeRepository.findAll(Sort.by(Direction.ASC, "locale", "sortIndex"));
      } else {
        return nextOfKinTypeRepository.findByLocaleIgnoreCase(
            localeId, Sort.by(Direction.ASC, "locale", "sortIndex"));
      }
    } catch (Throwable e) {
      throw new ReferenceServiceException("Failed to retrieve the next of kin types", e);
    }
  }

  /**
   * Retrieve all the occupations.
   *
   * @return the occupations
   */
  @Override
  public List<Occupation> getOccupations() throws ReferenceServiceException {
    return getOccupations(null);
  }

  /**
   * Retrieve the occupations.
   *
   * @param localeId the Unicode locale identifier identifying the locale to retrieve the
   *     occupations for or <code>null</code> to retrieve the occupations for all locales
   * @return the occupations
   */
  @Override
  public List<Occupation> getOccupations(String localeId) throws ReferenceServiceException {
    try {
      if (StringUtils.isEmpty(localeId)) {
        return occupationRepository.findAll(Sort.by(Direction.ASC, "locale", "sortIndex"));
      } else {
        return occupationRepository.findByLocaleIgnoreCase(
            localeId, Sort.by(Direction.ASC, "locale", "sortIndex"));
      }
    } catch (Throwable e) {
      throw new ReferenceServiceException("Failed to retrieve the occupations", e);
    }
  }

  /**
   * Retrieve all the permit types.
   *
   * @return the permit types
   */
  @Override
  public List<PermitType> getPermitTypes() throws ReferenceServiceException {
    return getPermitTypes(null);
  }

  /**
   * Retrieve the permit types.
   *
   * @param localeId the Unicode locale identifier identifying the locale to retrieve the permit
   *     types for or <code>null</code> to retrieve the permit types for all locales
   * @return the permit types
   */
  @Override
  public List<PermitType> getPermitTypes(String localeId) throws ReferenceServiceException {
    try {
      if (StringUtils.isEmpty(localeId)) {
        return permitTypeRepository.findAll(Sort.by(Direction.ASC, "locale", "sortIndex"));
      } else {
        return permitTypeRepository.findByLocaleIgnoreCase(
            localeId, Sort.by(Direction.ASC, "locale", "sortIndex"));
      }
    } catch (Throwable e) {
      throw new ReferenceServiceException("Failed to retrieve the permit types", e);
    }
  }

  /**
   * Retrieve all the races.
   *
   * @return the races
   */
  @Override
  public List<Race> getRaces() throws ReferenceServiceException {
    return getRaces(null);
  }

  /**
   * Retrieve the races.
   *
   * @param localeId the Unicode locale identifier identifying the locale to retrieve the races for
   *     or <code>null</code> to retrieve the races for all locales
   * @return the races
   */
  @Override
  public List<Race> getRaces(String localeId) throws ReferenceServiceException {
    try {
      if (StringUtils.isEmpty(localeId)) {
        return raceRepository.findAll(Sort.by(Direction.ASC, "locale", "sortIndex"));
      } else {
        return raceRepository.findByLocaleIgnoreCase(
            localeId, Sort.by(Direction.ASC, "locale", "sortIndex"));
      }
    } catch (Throwable e) {
      throw new ReferenceServiceException("Failed to retrieve the races", e);
    }
  }

  /**
   * Retrieve all the regions.
   *
   * @return the regions
   */
  @Override
  public List<Region> getRegions() throws ReferenceServiceException {
    return getRegions(null);
  }

  /**
   * Retrieve the regions.
   *
   * @param localeId the Unicode locale identifier identifying the locale to retrieve the regions
   *     for or <code>null</code> to retrieve the regions for all locales
   * @return the regions
   */
  @Override
  public List<Region> getRegions(String localeId) throws ReferenceServiceException {
    try {
      if (StringUtils.isEmpty(localeId)) {
        return regionRepository.findAll(Sort.by(Direction.ASC, "locale", "sortIndex"));
      } else {
        return regionRepository.findByLocaleIgnoreCase(
            localeId, Sort.by(Direction.ASC, "locale", "sortIndex"));
      }
    } catch (Throwable e) {
      throw new ReferenceServiceException("Failed to retrieve the regions", e);
    }
  }

  /**
   * Retrieve all the residential statuses.
   *
   * @return the residential statuses
   */
  @Override
  public List<ResidentialStatus> getResidentialStatuses() throws ReferenceServiceException {
    return getResidentialStatuses(null);
  }

  /**
   * Retrieve the residential statuses.
   *
   * @param localeId the Unicode locale identifier identifying the locale to retrieve the
   *     residential statuses for or <code>null</code> to retrieve the residential statuses for all
   *     locales
   * @return the residential statuses
   */
  @Override
  public List<ResidentialStatus> getResidentialStatuses(String localeId)
      throws ReferenceServiceException {
    try {
      if (StringUtils.isEmpty(localeId)) {
        return residentialStatusRepository.findAll(Sort.by(Direction.ASC, "locale", "sortIndex"));
      } else {
        return residentialStatusRepository.findByLocaleIgnoreCase(
            localeId, Sort.by(Direction.ASC, "locale", "sortIndex"));
      }
    } catch (Throwable e) {
      throw new ReferenceServiceException("Failed to retrieve the residential statuses", e);
    }
  }
}
