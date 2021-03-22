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
import javax.annotation.Resource;
import javax.validation.Validator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * The <b>ReferenceService</b> class provides the Reference Service implementation.
 *
 * @author Marcus Portmann
 */
@Service
public class ReferenceService implements IReferenceService {

  /* Logger */
  private static final Logger logger = LoggerFactory.getLogger(ReferenceService.class);

  /** The Country repository. */
  private final CountryRepository countryRepository;

  /** The Language Repository. */
  private final LanguageRepository languageRepository;

  /** The Region Repository. */
  private final RegionRepository regionRepository;

  /** The JSR-303 validator. */
  private final Validator validator;

  /** The Verification Method Repository. */
  private final VerificationMethodRepository verificationMethodRepository;

  /** The Verification Status Repository. */
  private final VerificationStatusRepository verificationStatusRepository;

  /** The internal reference to the Reference Service to enable caching. */
  @Resource private IReferenceService self;

  /**
   * Constructs a new <b>ReferenceService</b>.
   *
   * @param validator the JSR-303 validator
   * @param countryRepository the Country Repository
   * @param languageRepository the Language Repository
   * @param regionRepository the Region Repository
   * @param verificationMethodRepository the Verification Method Repository
   * @param verificationStatusRepository the Verification Status Repository
   */
  public ReferenceService(
      Validator validator,
      CountryRepository countryRepository,
      LanguageRepository languageRepository,
      RegionRepository regionRepository,
      VerificationMethodRepository verificationMethodRepository,
      VerificationStatusRepository verificationStatusRepository) {
    this.validator = validator;
    this.countryRepository = countryRepository;
    this.languageRepository = languageRepository;
    this.regionRepository = regionRepository;
    this.verificationMethodRepository = verificationMethodRepository;
    this.verificationStatusRepository = verificationStatusRepository;
  }

  /**
   * Retrieve all the countries.
   *
   * @return the countries
   */
  @Override
  @Cacheable(value = "reference", key = "'countries.ALL'")
  public List<Country> getCountries() throws ServiceUnavailableException {
    return getCountries(null);
  }

  /**
   * Retrieve the countries.
   *
   * @param localeId the Unicode locale identifier for the locale to retrieve the countries for or
   *     <b>null</b> to retrieve the countries for all locales
   * @return the countries
   */
  @Override
  @Cacheable(value = "reference", key = "'countries.' + #localeId")
  public List<Country> getCountries(String localeId) throws ServiceUnavailableException {
    try {
      if (!StringUtils.hasText(localeId)) {
        return countryRepository.findAll(Sort.by(Direction.ASC, "localeId", "sortIndex"));
      } else {
        return countryRepository.findByLocaleIdIgnoreCase(
            localeId, Sort.by(Direction.ASC, "localeId", "sortIndex"));
      }
    } catch (Throwable e) {
      throw new ServiceUnavailableException("Failed to retrieve the countries", e);
    }
  }

  /**
   * Retrieve all the languages.
   *
   * @return the languages
   */
  @Override
  @Cacheable(value = "reference", key = "'languages.ALL'")
  public List<Language> getLanguages() throws ServiceUnavailableException {
    return getLanguages(null);
  }

  /**
   * Retrieve the languages.
   *
   * @param localeId the Unicode locale identifier for the locale to retrieve the languages for or
   *     <b>null</b> to retrieve the languages for all locales
   * @return the languages
   */
  @Override
  @Cacheable(value = "reference", key = "'languages.' + #localeId")
  public List<Language> getLanguages(String localeId) throws ServiceUnavailableException {
    try {
      if (!StringUtils.hasText(localeId)) {
        return languageRepository.findAll(Sort.by(Direction.ASC, "localeId", "sortIndex"));
      } else {
        return languageRepository.findByLocaleIdIgnoreCase(
            localeId, Sort.by(Direction.ASC, "localeId", "sortIndex"));
      }
    } catch (Throwable e) {
      throw new ServiceUnavailableException("Failed to retrieve the languages", e);
    }
  }

  /**
   * Retrieve all the regions.
   *
   * @return the regions
   */
  @Override
  @Cacheable(value = "reference", key = "'regions.ALL'")
  public List<Region> getRegions() throws ServiceUnavailableException {
    return getRegions(null);
  }

  /**
   * Retrieve the regions.
   *
   * @param localeId the Unicode locale identifier for the locale to retrieve the regions for or
   *     <b>null</b> to retrieve the regions for all locales
   * @return the regions
   */
  @Override
  @Cacheable(value = "reference", key = "'regions.' + #localeId")
  public List<Region> getRegions(String localeId) throws ServiceUnavailableException {
    try {
      if (!StringUtils.hasText(localeId)) {
        return regionRepository.findAll(Sort.by(Direction.ASC, "localeId", "sortIndex"));
      } else {
        return regionRepository.findByLocaleIdIgnoreCase(
            localeId, Sort.by(Direction.ASC, "localeId", "sortIndex"));
      }
    } catch (Throwable e) {
      throw new ServiceUnavailableException("Failed to retrieve the regions", e);
    }
  }

  /**
   * Retrieve all the verification methods.
   *
   * @return the verification methods
   */
  @Override
  @Cacheable(value = "reference", key = "'verificationMethods.ALL'")
  public List<VerificationMethod> getVerificationMethods() throws ServiceUnavailableException {
    return getVerificationMethods(null);
  }

  /**
   * Retrieve the verification methods.
   *
   * @param localeId the Unicode locale identifier for the locale to retrieve the verification
   *     methods for or <b>null</b> to retrieve the verification methods for all locales
   * @return the verification methods
   */
  @Override
  @Cacheable(value = "reference", key = "'verificationMethods.' + #localeId")
  public List<VerificationMethod> getVerificationMethods(String localeId)
      throws ServiceUnavailableException {
    try {
      if (!StringUtils.hasText(localeId)) {
        return verificationMethodRepository.findAll(
            Sort.by(Direction.ASC, "localeId", "sortIndex"));
      } else {
        return verificationMethodRepository.findByLocaleIdIgnoreCase(
            localeId, Sort.by(Direction.ASC, "localeId", "sortIndex"));
      }
    } catch (Throwable e) {
      throw new ServiceUnavailableException("Failed to retrieve the verification methods", e);
    }
  }

  /**
   * Retrieve all the verification statuses.
   *
   * @return the verification statuses
   */
  @Override
  @Cacheable(value = "reference", key = "'verificationStatuses.ALL'")
  public List<VerificationStatus> getVerificationStatuses() throws ServiceUnavailableException {
    return getVerificationStatuses(null);
  }

  /**
   * Retrieve the verification statuses.
   *
   * @param localeId the Unicode locale identifier for the locale to retrieve the verification
   *     statuses for or <b>null</b> to retrieve the verification statuses for all locales
   * @return the verification statuses
   */
  @Override
  @Cacheable(value = "reference", key = "'verificationStatuses.' + #localeId")
  public List<VerificationStatus> getVerificationStatuses(String localeId)
      throws ServiceUnavailableException {
    try {
      if (!StringUtils.hasText(localeId)) {
        return verificationStatusRepository.findAll(
            Sort.by(Direction.ASC, "localeId", "sortIndex"));
      } else {
        return verificationStatusRepository.findByLocaleIdIgnoreCase(
            localeId, Sort.by(Direction.ASC, "localeId", "sortIndex"));
      }
    } catch (Throwable e) {
      throw new ServiceUnavailableException("Failed to retrieve the verification statuses", e);
    }
  }

  /**
   * Check whether the code is a valid code for a country.
   *
   * @param countryCode the code for the country
   * @return <b>true</b> if the code is a valid code for a country or <b>false</b> otherwise
   */
  @Override
  public boolean isValidCountry(String countryCode) throws ServiceUnavailableException {
    if (!StringUtils.hasText(countryCode)) {
      return false;
    }

    return self.getCountries().stream().anyMatch(country -> country.getCode().equals(countryCode));
  }

  /**
   * Check whether the code is a valid ISO 639-1 alpha-2 code for a language.
   *
   * @param languageCode the code for the language
   * @return <b>true</b> if the code is a valid ISO 639-1 alpha-2 code for a language or
   *     <b>false</b> otherwise
   */
  @Override
  public boolean isValidLanguage(String languageCode) throws ServiceUnavailableException {
    if (!StringUtils.hasText(languageCode)) {
      return false;
    }

    return self.getLanguages().stream()
        .anyMatch(language -> language.getCode().equals(languageCode));
  }

  /**
   * Check whether the code is a valid code for a region.
   *
   * @param regionCode the code for the region
   * @return <b>true</b> if the code is a valid code for a region or <b>false</b> otherwise
   */
  @Override
  public boolean isValidRegion(String regionCode) throws ServiceUnavailableException {
    if (!StringUtils.hasText(regionCode)) {
      return false;
    }

    return self.getRegions().stream().anyMatch(region -> region.getCode().equals(regionCode));
  }

  /**
   * Check whether the code is a valid code for a verification method.
   *
   * @param verificationMethodCode the code for the verification method
   * @return <b>true</b> if the code is a valid code for a verification method or <b>false</b>
   *     otherwise
   */
  @Override
  public boolean isValidVerificationMethod(String verificationMethodCode)
      throws ServiceUnavailableException {
    if (!StringUtils.hasText(verificationMethodCode)) {
      return false;
    }

    return self.getVerificationMethods().stream()
        .anyMatch(
            verificationMethod -> verificationMethod.getCode().equals(verificationMethodCode));
  }

  /**
   * Check whether the code is a valid code for a verification status.
   *
   * @param verificationStatusCode the code for the verification status
   * @return <b>true</b> if the code is a valid code for a verification status or <b>false</b>
   *     otherwise
   */
  @Override
  public boolean isValidVerificationStatus(String verificationStatusCode)
      throws ServiceUnavailableException {
    if (!StringUtils.hasText(verificationStatusCode)) {
      return false;
    }

    return self.getVerificationStatuses().stream()
        .anyMatch(
            verificationStatus -> verificationStatus.getCode().equals(verificationStatusCode));
  }
}
