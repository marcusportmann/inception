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

import com.ibm.icu.util.TimeZone.SystemTimeZoneType;
import digital.inception.core.service.ServiceUnavailableException;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;
import javax.annotation.Resource;
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

  /** The default locale ID. */
  public static final String DEFAULT_LOCALE_ID = "en-US";

  /** The Country repository. */
  private final CountryRepository countryRepository;

  /** The Language Repository. */
  private final LanguageRepository languageRepository;

  /** The Measurement System Repository. */
  private final MeasurementSystemRepository measurementSystemRepository;

  /** The Region Repository. */
  private final RegionRepository regionRepository;

  /** The internal reference to the Reference Service to enable caching. */
  @Resource private IReferenceService self;

  /**
   * Constructs a new <b>ReferenceService</b>.
   *
   * @param countryRepository the Country Repository
   * @param languageRepository the Language Repository
   * @param measurementSystemRepository the Measurement System Repository
   * @param regionRepository the Region Repository
   */
  public ReferenceService(
      CountryRepository countryRepository,
      LanguageRepository languageRepository,
      MeasurementSystemRepository measurementSystemRepository,
      RegionRepository regionRepository) {
    this.countryRepository = countryRepository;
    this.languageRepository = languageRepository;
    this.measurementSystemRepository = measurementSystemRepository;
    this.regionRepository = regionRepository;
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
   * Retrieve the measurement systems.
   *
   * @param localeId the Unicode locale identifier for the locale to retrieve the measurement
   *     systems for or <b>null</b> to retrieve the measurement systems for all locales
   * @return the measurement systems
   */
  @Override
  @Cacheable(value = "reference", key = "'measurementSystems.' + #localeId")
  public List<MeasurementSystem> getMeasurementSystems(String localeId)
      throws ServiceUnavailableException {
    try {
      if (!StringUtils.hasText(localeId)) {
        return measurementSystemRepository.findAll(Sort.by(Direction.ASC, "localeId", "sortIndex"));
      } else {
        return measurementSystemRepository.findByLocaleIdIgnoreCase(
            localeId, Sort.by(Direction.ASC, "localeId", "sortIndex"));
      }
    } catch (Throwable e) {
      throw new ServiceUnavailableException("Failed to retrieve the measurement systems", e);
    }
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
   * Retrieve the time zones.
   *
   * @param localeId the Unicode locale identifier for the locale to retrieve the time zones for or
   *     <b>null</b> to retrieve the time zones for all locales
   * @return the time zones
   */
  @Override
  @Cacheable(value = "reference", key = "'timeZones.' + #localeId")
  public List<TimeZone> getTimeZones(String localeId) throws ServiceUnavailableException {
    try {
      List<TimeZone> timeZones = new ArrayList<>();

      int sortIndex = 1;

      Locale locale = Locale.forLanguageTag(localeId);

      int maxLength = 0;

      for (String timeZoneId :
          com.ibm.icu.util.TimeZone.getAvailableIDs(SystemTimeZoneType.CANONICAL, null, null)
              .stream()
              .filter(timeZoneId -> (!timeZoneId.startsWith("SystemV")))
              .filter(timeZoneId -> (timeZoneId.contains("/")))
              .collect(Collectors.toList())) {

        maxLength = Math.max(maxLength, timeZoneId.length());

        String zoneName = com.ibm.icu.util.TimeZone.getTimeZone(timeZoneId).getDisplayName(locale);

        timeZones.add(new TimeZone(timeZoneId, localeId, zoneName, zoneName, sortIndex));

        sortIndex++;
      }

      System.out.println("maxLength = " + maxLength);

      return timeZones;
    } catch (Throwable e) {
      throw new ServiceUnavailableException("Failed to retrieve the time zones", e);
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

    return self.getCountries(DEFAULT_LOCALE_ID).stream()
        .anyMatch(country -> country.getCode().equals(countryCode));
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

    return self.getLanguages(DEFAULT_LOCALE_ID).stream()
        .anyMatch(language -> language.getCode().equals(languageCode));
  }

  /**
   * Check whether the code is a valid code for a measurement system.
   *
   * @param measurementSystemCode the code for the measurement system
   * @return <b>true</b> if the code is a valid code for a measurement system or <b>false</b>
   *     otherwise
   */
  @Override
  public boolean isValidMeasurementSystem(String measurementSystemCode)
      throws ServiceUnavailableException {
    if (!StringUtils.hasText(measurementSystemCode)) {
      return false;
    }

    return self.getMeasurementSystems(DEFAULT_LOCALE_ID).stream()
        .anyMatch(measurementSystem -> measurementSystem.getCode().equals(measurementSystemCode));
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

    return self.getRegions(DEFAULT_LOCALE_ID).stream()
        .anyMatch(region -> region.getCode().equals(regionCode));
  }

  /**
   * Check whether the code is a valid code for a time zone.
   *
   * @param timeZoneCode the code for the time zone
   * @return <b>true</b> if the code is a valid code for a time zone or <b>false</b> otherwise
   */
  @Override
  public boolean isValidTimeZone(String timeZoneCode) {
    if (!StringUtils.hasText(timeZoneCode)) {
      return false;
    }

    try {
      //noinspection ResultOfMethodCallIgnored
      ZoneId.of(timeZoneCode);
      return true;
    } catch (Throwable ignored) {
      return false;
    }
  }
}
