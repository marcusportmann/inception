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
public class ReferenceService implements IReferenceService {

  /* Logger */
  private static final Logger logger = LoggerFactory.getLogger(ReferenceService.class);

  /** The Contact Mechanism Sub Type Repository. */
  private final ContactMechanismPurposeRepository contactMechanismPurposeRepository;

  /** The Contact Mechanism Type Repository. */
  private final ContactMechanismTypeRepository contactMechanismTypeRepository;

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

  /** The Next Of Kin Type Repository. */
  private final NextOfKinTypeRepository nextOfKinTypeRepository;

  /** The Occupation Repository. */
  private final OccupationRepository occupationRepository;

  /** The Race Repository. */
  private final RaceRepository raceRepository;

  /** The Region Repository. */
  private final RegionRepository regionRepository;

  /** The Residence Permit Type Repository. */
  private final ResidencePermitTypeRepository residencePermitTypeRepository;

  /** The Residency Status Repository. */
  private final ResidencyStatusRepository residencyStatusRepository;

  /** The Residential Type Repository. */
  private final ResidentialTypeRepository residentialTypeRepository;

  /** The Sources of Funds Repository. */
  private final SourceOfFundsRepository sourceOfFundsRepository;

  /** The Tax Number Type Repository. */
  private final TaxNumberTypeRepository taxNumberTypeRepository;

  /** The Title Repository. */
  private final TitleRepository titleRepository;

  /** The Verification Method Repository. */
  private final VerificationMethodRepository verificationMethodRepository;

  /** The Verification Status Repository. */
  private final VerificationStatusRepository verificationStatusRepository;

  /**
   * Constructs a new <code>ReferenceService</code>.
   *
   * @param contactMechanismPurposeRepository the Contact Mechanism Sub Type Repository
   * @param contactMechanismTypeRepository the Contact Mechanism Type Repository
   * @param countryRepository the Country Repository
   * @param employmentStatusRepository the Employment Status Repository
   * @param employmentTypeRepository the Employment Type Repository
   * @param genderRepository the Gender Repository
   * @param identityDocumentTypeRepository the Identity Document Type Repository
   * @param languageRepository the Language Repository
   * @param maritalStatusRepository the Marital Status Repository
   * @param marriageTypeRepository the Marriage Type Repository
   * @param nextOfKinTypeRepository the Next Of Kin Repository
   * @param occupationRepository the Occupation Repository
   * @param raceRepository the Race Repository
   * @param regionRepository the Region Repository
   * @param residencePermitTypeRepository the Residence Permit Type Repository*
   * @param residencyStatusRepository the Residency Status Repository
   * @param residentialTypeRepository the Residential Type Repository
   * @param sourceOfFundsRepository the Source Of Funds Repository
   * @param taxNumberTypeRepository the Tax Number Type Repository
   * @param titleRepository the Title Repository
   * @param verificationMethodRepository the Verification Method Repository
   * @param verificationStatusRepository the Verification Status Repository
   */
  public ReferenceService(
      ContactMechanismPurposeRepository contactMechanismPurposeRepository,
      ContactMechanismTypeRepository contactMechanismTypeRepository,
      CountryRepository countryRepository,
      EmploymentStatusRepository employmentStatusRepository,
      EmploymentTypeRepository employmentTypeRepository,
      GenderRepository genderRepository,
      IdentityDocumentTypeRepository identityDocumentTypeRepository,
      LanguageRepository languageRepository,
      MaritalStatusRepository maritalStatusRepository,
      MarriageTypeRepository marriageTypeRepository,
      NextOfKinTypeRepository nextOfKinTypeRepository,
      OccupationRepository occupationRepository,
      RaceRepository raceRepository,
      RegionRepository regionRepository,
      ResidencePermitTypeRepository residencePermitTypeRepository,
      ResidencyStatusRepository residencyStatusRepository,
      ResidentialTypeRepository residentialTypeRepository,
      SourceOfFundsRepository sourceOfFundsRepository,
      TaxNumberTypeRepository taxNumberTypeRepository,
      TitleRepository titleRepository,
      VerificationMethodRepository verificationMethodRepository,
      VerificationStatusRepository verificationStatusRepository) {
    this.contactMechanismPurposeRepository = contactMechanismPurposeRepository;
    this.contactMechanismTypeRepository = contactMechanismTypeRepository;
    this.countryRepository = countryRepository;
    this.employmentStatusRepository = employmentStatusRepository;
    this.employmentTypeRepository = employmentTypeRepository;
    this.genderRepository = genderRepository;
    this.identityDocumentTypeRepository = identityDocumentTypeRepository;
    this.languageRepository = languageRepository;
    this.maritalStatusRepository = maritalStatusRepository;
    this.marriageTypeRepository = marriageTypeRepository;
    this.nextOfKinTypeRepository = nextOfKinTypeRepository;
    this.occupationRepository = occupationRepository;
    this.raceRepository = raceRepository;
    this.regionRepository = regionRepository;
    this.residencePermitTypeRepository = residencePermitTypeRepository;
    this.residencyStatusRepository = residencyStatusRepository;
    this.residentialTypeRepository = residentialTypeRepository;
    this.sourceOfFundsRepository = sourceOfFundsRepository;
    this.taxNumberTypeRepository = taxNumberTypeRepository;
    this.titleRepository = titleRepository;
    this.verificationMethodRepository = verificationMethodRepository;
    this.verificationStatusRepository = verificationStatusRepository;
  }

  /**
   * Retrieve all the contact mechanism purposes.
   *
   * @return the contact mechanism purposes
   */
  @Override
  public List<ContactMechanismPurpose> getContactMechanismPurposes()
      throws ReferenceServiceException {
    return getContactMechanismPurposes(null);
  }

  /**
   * Retrieve the contact mechanism purposes.
   *
   * @param localeId the Unicode locale identifier identifying the locale to retrieve the contact
   *     mechanism purposes for or <code>null</code> to retrieve the contact mechanism purposes
   *     for all locales
   * @return the contact mechanism purposes
   */
  @Override
  public List<ContactMechanismPurpose> getContactMechanismPurposes(String localeId)
      throws ReferenceServiceException {
    try {
      if (StringUtils.isEmpty(localeId)) {
        return contactMechanismPurposeRepository.findAll(
            Sort.by(Direction.ASC, "localeId", "sortIndex"));
      } else {
        return contactMechanismPurposeRepository.findByLocaleIdIgnoreCase(
            localeId, Sort.by(Direction.ASC, "localeId", "sortIndex"));
      }
    } catch (Throwable e) {
      throw new ReferenceServiceException("Failed to retrieve the contact mechanism purposes", e);
    }
  }

  /**
   * Retrieve all the contact mechanism types.
   *
   * @return the contact mechanism types
   */
  @Override
  public List<ContactMechanismType> getContactMechanismTypes() throws ReferenceServiceException {
    return getContactMechanismTypes(null);
  }

  /**
   * Retrieve the contact mechanism types.
   *
   * @param localeId the Unicode locale identifier identifying the locale to retrieve the contact
   *     mechanism types for or <code>null</code> to retrieve the contact mechanism types for all
   *     locales
   * @return the contact mechanism types
   */
  @Override
  public List<ContactMechanismType> getContactMechanismTypes(String localeId)
      throws ReferenceServiceException {
    try {
      if (StringUtils.isEmpty(localeId)) {
        return contactMechanismTypeRepository.findAll(
            Sort.by(Direction.ASC, "localeId", "sortIndex"));
      } else {
        return contactMechanismTypeRepository.findByLocaleIdIgnoreCase(
            localeId, Sort.by(Direction.ASC, "localeId", "sortIndex"));
      }
    } catch (Throwable e) {
      throw new ReferenceServiceException("Failed to retrieve the contact mechanism types", e);
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
        return countryRepository.findAll(Sort.by(Direction.ASC, "localeId", "sortIndex"));
      } else {
        return countryRepository.findByLocaleIdIgnoreCase(
            localeId, Sort.by(Direction.ASC, "localeId", "sortIndex"));
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
        return employmentStatusRepository.findAll(Sort.by(Direction.ASC, "localeId", "sortIndex"));
      } else {
        return employmentStatusRepository.findByLocaleIdIgnoreCase(
            localeId, Sort.by(Direction.ASC, "localeId", "sortIndex"));
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
        return employmentTypeRepository.findAll(Sort.by(Direction.ASC, "localeId", "sortIndex"));
      } else {
        return employmentTypeRepository.findByLocaleIdIgnoreCase(
            localeId, Sort.by(Direction.ASC, "localeId", "sortIndex"));
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
        return genderRepository.findAll(Sort.by(Direction.ASC, "localeId", "sortIndex"));
      } else {
        return genderRepository.findByLocaleIdIgnoreCase(
            localeId, Sort.by(Direction.ASC, "localeId", "sortIndex"));
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
            Sort.by(Direction.ASC, "localeId", "sortIndex"));
      } else {
        return identityDocumentTypeRepository.findByLocaleIdIgnoreCase(
            localeId, Sort.by(Direction.ASC, "localeId", "sortIndex"));
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
        return languageRepository.findAll(Sort.by(Direction.ASC, "localeId", "sortIndex"));
      } else {
        return languageRepository.findByLocaleIdIgnoreCase(
            localeId, Sort.by(Direction.ASC, "localeId", "sortIndex"));
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
        return maritalStatusRepository.findAll(Sort.by(Direction.ASC, "localeId", "sortIndex"));
      } else {
        return maritalStatusRepository.findByLocaleIdIgnoreCase(
            localeId, Sort.by(Direction.ASC, "localeId", "sortIndex"));
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
        return marriageTypeRepository.findAll(Sort.by(Direction.ASC, "localeId", "sortIndex"));
      } else {
        return marriageTypeRepository.findByLocaleIdIgnoreCase(
            localeId, Sort.by(Direction.ASC, "localeId", "sortIndex"));
      }
    } catch (Throwable e) {
      throw new ReferenceServiceException("Failed to retrieve the marriage types", e);
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
        return nextOfKinTypeRepository.findAll(Sort.by(Direction.ASC, "localeId", "sortIndex"));
      } else {
        return nextOfKinTypeRepository.findByLocaleIdIgnoreCase(
            localeId, Sort.by(Direction.ASC, "localeId", "sortIndex"));
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
        return occupationRepository.findAll(Sort.by(Direction.ASC, "localeId", "sortIndex"));
      } else {
        return occupationRepository.findByLocaleIdIgnoreCase(
            localeId, Sort.by(Direction.ASC, "localeId", "sortIndex"));
      }
    } catch (Throwable e) {
      throw new ReferenceServiceException("Failed to retrieve the occupations", e);
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
        return raceRepository.findAll(Sort.by(Direction.ASC, "localeId", "sortIndex"));
      } else {
        return raceRepository.findByLocaleIdIgnoreCase(
            localeId, Sort.by(Direction.ASC, "localeId", "sortIndex"));
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
        return regionRepository.findAll(Sort.by(Direction.ASC, "localeId", "sortIndex"));
      } else {
        return regionRepository.findByLocaleIdIgnoreCase(
            localeId, Sort.by(Direction.ASC, "localeId", "sortIndex"));
      }
    } catch (Throwable e) {
      throw new ReferenceServiceException("Failed to retrieve the regions", e);
    }
  }

  /**
   * Retrieve all the residence permit types.
   *
   * @return the residence permit types
   */
  @Override
  public List<ResidencePermitType> getResidencePermitTypes() throws ReferenceServiceException {
    return getResidencePermitTypes(null);
  }

  /**
   * Retrieve the residence permit types.
   *
   * @param localeId the Unicode locale identifier identifying the locale to retrieve the residence
   *     permit types for or <code>null</code> to retrieve the residence permit types for all
   *     locales
   * @return the residence permit types
   */
  @Override
  public List<ResidencePermitType> getResidencePermitTypes(String localeId)
      throws ReferenceServiceException {
    try {
      if (StringUtils.isEmpty(localeId)) {
        return residencePermitTypeRepository.findAll(
            Sort.by(Direction.ASC, "localeId", "sortIndex"));
      } else {
        return residencePermitTypeRepository.findByLocaleIdIgnoreCase(
            localeId, Sort.by(Direction.ASC, "localeId", "sortIndex"));
      }
    } catch (Throwable e) {
      throw new ReferenceServiceException("Failed to retrieve the residence permit types", e);
    }
  }

  /**
   * Retrieve all the residency statuses.
   *
   * @return the residency statuses
   */
  @Override
  public List<ResidencyStatus> getResidencyStatuses() throws ReferenceServiceException {
    return getResidencyStatuses(null);
  }

  /**
   * Retrieve the residency statuses.
   *
   * @param localeId the Unicode locale identifier identifying the locale to retrieve the residency
   *     statuses for or <code>null</code> to retrieve the residency statuses for all locales
   * @return the residency statuses
   */
  @Override
  public List<ResidencyStatus> getResidencyStatuses(String localeId)
      throws ReferenceServiceException {
    try {
      if (StringUtils.isEmpty(localeId)) {
        return residencyStatusRepository.findAll(Sort.by(Direction.ASC, "localeId", "sortIndex"));
      } else {
        return residencyStatusRepository.findByLocaleIdIgnoreCase(
            localeId, Sort.by(Direction.ASC, "localeId", "sortIndex"));
      }
    } catch (Throwable e) {
      throw new ReferenceServiceException("Failed to retrieve the residency statuses", e);
    }
  }

  /**
   * Retrieve all the residential types.
   *
   * @return the residential types
   */
  @Override
  public List<ResidentialType> getResidentialTypes() throws ReferenceServiceException {
    return getResidentialTypes(null);
  }

  /**
   * Retrieve the residential types.
   *
   * @param localeId the Unicode locale identifier identifying the locale to retrieve the
   *     residential types for or <code>null</code> to retrieve the residential types for all
   *     locales
   * @return the residential types
   */
  @Override
  public List<ResidentialType> getResidentialTypes(String localeId)
      throws ReferenceServiceException {
    try {
      if (StringUtils.isEmpty(localeId)) {
        return residentialTypeRepository.findAll(Sort.by(Direction.ASC, "localeId", "sortIndex"));
      } else {
        return residentialTypeRepository.findByLocaleIdIgnoreCase(
            localeId, Sort.by(Direction.ASC, "localeId", "sortIndex"));
      }
    } catch (Throwable e) {
      throw new ReferenceServiceException("Failed to retrieve the residential types", e);
    }
  }

  /**
   * Retrieve all the sources of funds.
   *
   * @return the sources of funds
   */
  @Override
  public List<SourceOfFunds> getSourcesOfFunds() throws ReferenceServiceException {
    return getSourcesOfFunds(null);
  }

  /**
   * Retrieve the sources of funds.
   *
   * @param localeId the Unicode locale identifier identifying the locale to retrieve the sources of
   *     funds for or <code>null</code> to retrieve the sources of funds for all locales
   * @return the sources of funds
   */
  @Override
  public List<SourceOfFunds> getSourcesOfFunds(String localeId) throws ReferenceServiceException {
    try {
      if (StringUtils.isEmpty(localeId)) {
        return sourceOfFundsRepository.findAll(Sort.by(Direction.ASC, "localeId", "sortIndex"));
      } else {
        return sourceOfFundsRepository.findByLocaleIdIgnoreCase(
            localeId, Sort.by(Direction.ASC, "localeId", "sortIndex"));
      }
    } catch (Throwable e) {
      throw new ReferenceServiceException("Failed to retrieve the sources of funds", e);
    }
  }

  /**
   * Retrieve all the tax number types.
   *
   * @return the tax number types
   */
  @Override
  public List<TaxNumberType> getTaxNumberTypes() throws ReferenceServiceException {
    return getTaxNumberTypes(null);
  }

  /**
   * Retrieve the tax number types.
   *
   * @param localeId the Unicode locale identifier identifying the locale to retrieve the tax number
   *     types for or <code>null</code> to retrieve the tax number types for all locales
   * @return the tax number types
   */
  @Override
  public List<TaxNumberType> getTaxNumberTypes(String localeId) throws ReferenceServiceException {
    try {
      if (StringUtils.isEmpty(localeId)) {
        return taxNumberTypeRepository.findAll(Sort.by(Direction.ASC, "localeId", "sortIndex"));
      } else {
        return taxNumberTypeRepository.findByLocaleIdIgnoreCase(
            localeId, Sort.by(Direction.ASC, "localeId", "sortIndex"));
      }
    } catch (Throwable e) {
      throw new ReferenceServiceException("Failed to retrieve the tax number types", e);
    }
  }

  /**
   * Retrieve all the titles.
   *
   * @return the titles
   */
  @Override
  public List<Title> getTitles() throws ReferenceServiceException {
    return getTitles(null);
  }

  /**
   * Retrieve the titles.
   *
   * @param localeId the Unicode locale identifier identifying the locale to retrieve the titles for
   *     or <code>null</code> to retrieve the titles for all locales
   * @return the titles
   */
  @Override
  public List<Title> getTitles(String localeId) throws ReferenceServiceException {
    try {
      if (StringUtils.isEmpty(localeId)) {
        return titleRepository.findAll(Sort.by(Direction.ASC, "localeId", "sortIndex"));
      } else {
        return titleRepository.findByLocaleIdIgnoreCase(
            localeId, Sort.by(Direction.ASC, "localeId", "sortIndex"));
      }
    } catch (Throwable e) {
      throw new ReferenceServiceException("Failed to retrieve the titles", e);
    }
  }

  /**
   * Retrieve all the verification methods.
   *
   * @return the verification methods
   */
  @Override
  public List<VerificationMethod> getVerificationMethods() throws ReferenceServiceException {
    return getVerificationMethods(null);
  }

  /**
   * Retrieve the verification methods.
   *
   * @param localeId the Unicode locale identifier identifying the locale to retrieve the
   *     verification methods for or <code>null</code> to retrieve the verification methods for all
   *     locales
   * @return the verification methods
   */
  @Override
  public List<VerificationMethod> getVerificationMethods(String localeId)
      throws ReferenceServiceException {
    try {
      if (StringUtils.isEmpty(localeId)) {
        return verificationMethodRepository.findAll(
            Sort.by(Direction.ASC, "localeId", "sortIndex"));
      } else {
        return verificationMethodRepository.findByLocaleIdIgnoreCase(
            localeId, Sort.by(Direction.ASC, "localeId", "sortIndex"));
      }
    } catch (Throwable e) {
      throw new ReferenceServiceException("Failed to retrieve the verification methods", e);
    }
  }

  /**
   * Retrieve all the verification statuses.
   *
   * @return the verification statuses
   */
  @Override
  public List<VerificationStatus> getVerificationStatuses() throws ReferenceServiceException {
    return getVerificationStatuses(null);
  }

  /**
   * Retrieve the verification statuses.
   *
   * @param localeId the Unicode locale identifier identifying the locale to retrieve the
   *     verification statuses for or <code>null</code> to retrieve the verification statuses for
   *     all locales
   * @return the verification statuses
   */
  @Override
  public List<VerificationStatus> getVerificationStatuses(String localeId)
      throws ReferenceServiceException {
    try {
      if (StringUtils.isEmpty(localeId)) {
        return verificationStatusRepository.findAll(
            Sort.by(Direction.ASC, "localeId", "sortIndex"));
      } else {
        return verificationStatusRepository.findByLocaleIdIgnoreCase(
            localeId, Sort.by(Direction.ASC, "localeId", "sortIndex"));
      }
    } catch (Throwable e) {
      throw new ReferenceServiceException("Failed to retrieve the verification statuses", e);
    }
  }
}
