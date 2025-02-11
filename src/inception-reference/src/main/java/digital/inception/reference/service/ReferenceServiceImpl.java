/*
 * Copyright Marcus Portmann
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

package digital.inception.reference.service;

import com.ibm.icu.util.TimeZone.SystemTimeZoneType;
import digital.inception.core.service.InvalidArgumentException;
import digital.inception.core.service.ServiceUnavailableException;
import digital.inception.reference.model.Country;
import digital.inception.reference.model.Language;
import digital.inception.reference.model.MeasurementSystem;
import digital.inception.reference.model.MeasurementUnit;
import digital.inception.reference.model.MeasurementUnitType;
import digital.inception.reference.model.Region;
import digital.inception.reference.model.TimeZone;
import digital.inception.reference.persistence.CountryRepository;
import digital.inception.reference.persistence.LanguageRepository;
import digital.inception.reference.persistence.MeasurementSystemRepository;
import digital.inception.reference.persistence.MeasurementUnitRepository;
import digital.inception.reference.persistence.MeasurementUnitTypeRepository;
import digital.inception.reference.persistence.RegionRepository;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * The <b>ReferenceServiceImpl</b> class provides the Reference Service implementation.
 *
 * @author Marcus Portmann
 */
@Service
public class ReferenceServiceImpl implements ReferenceService {

  /** The Spring application context. */
  private final ApplicationContext applicationContext;

  /** The Country Repository. */
  private final CountryRepository countryRepository;

  /** The Language Repository. */
  private final LanguageRepository languageRepository;

  /** The Measurement System Repository. */
  private final MeasurementSystemRepository measurementSystemRepository;

  /** The Measurement Unit Repository. */
  private final MeasurementUnitRepository measurementUnitRepository;

  /** The Measurement Unit Type Repository. */
  private final MeasurementUnitTypeRepository measurementUnitTypeRepository;

  /** The Region Repository. */
  private final RegionRepository regionRepository;

  /**
   * Constructs a new <b>ReferenceServiceImpl</b>.
   *
   * @param applicationContext the Spring application context
   * @param countryRepository the Country Repository
   * @param languageRepository the Language Repository
   * @param measurementSystemRepository the Measurement System Repository
   * @param measurementUnitRepository the Measurement Unit Type Repository
   * @param measurementUnitTypeRepository the Measurement Unit Type Repository
   * @param regionRepository the Region Repository
   */
  public ReferenceServiceImpl(
      ApplicationContext applicationContext,
      CountryRepository countryRepository,
      LanguageRepository languageRepository,
      MeasurementSystemRepository measurementSystemRepository,
      MeasurementUnitRepository measurementUnitRepository,
      MeasurementUnitTypeRepository measurementUnitTypeRepository,
      RegionRepository regionRepository) {
    this.applicationContext = applicationContext;
    this.countryRepository = countryRepository;
    this.languageRepository = languageRepository;
    this.measurementSystemRepository = measurementSystemRepository;
    this.measurementUnitRepository = measurementUnitRepository;
    this.measurementUnitTypeRepository = measurementUnitTypeRepository;
    this.regionRepository = regionRepository;
  }

  @Override
  @Cacheable(cacheNames = "reference", key = "'countries.ALL'")
  public List<Country> getCountries() throws ServiceUnavailableException {
    try {
      return countryRepository.getAllCountries();
    } catch (Throwable e) {
      throw new ServiceUnavailableException("Failed to retrieve the country reference data", e);
    }
  }

  @Override
  @Cacheable(cacheNames = "reference", key = "'countries.' + #localeId")
  public List<Country> getCountries(String localeId)
      throws InvalidArgumentException, ServiceUnavailableException {
    if (!StringUtils.hasText(localeId)) {
      throw new InvalidArgumentException("localeId");
    }

    return getReferenceService().getCountries().stream()
        .filter(
            country ->
                (country.getLocaleId() == null || localeId.equalsIgnoreCase(country.getLocaleId())))
        .collect(Collectors.toList());
  }

  @Override
  @Cacheable(cacheNames = "reference", key = "'languages.ALL'")
  public List<Language> getLanguages() throws ServiceUnavailableException {
    try {
      return languageRepository.getAllLanguages();
    } catch (Throwable e) {
      throw new ServiceUnavailableException("Failed to retrieve the language reference data", e);
    }
  }

  @Override
  @Cacheable(cacheNames = "reference", key = "'languages.' + #localeId")
  public List<Language> getLanguages(String localeId)
      throws InvalidArgumentException, ServiceUnavailableException {
    if (!StringUtils.hasText(localeId)) {
      throw new InvalidArgumentException("localeId");
    }

    return getReferenceService().getLanguages().stream()
        .filter(
            language ->
                (language.getLocaleId() == null
                    || localeId.equalsIgnoreCase(language.getLocaleId())))
        .collect(Collectors.toList());
  }

  @Override
  @Cacheable(cacheNames = "reference", key = "'measurementSystems.ALL'")
  public List<MeasurementSystem> getMeasurementSystems() throws ServiceUnavailableException {
    try {
      return measurementSystemRepository.getAllMeasurementSystems();
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to retrieve the measurement system reference data", e);
    }
  }

  @Override
  @Cacheable(cacheNames = "reference", key = "'measurementSystems.' + #localeId")
  public List<MeasurementSystem> getMeasurementSystems(String localeId)
      throws InvalidArgumentException, ServiceUnavailableException {
    if (!StringUtils.hasText(localeId)) {
      throw new InvalidArgumentException("localeId");
    }

    return getReferenceService().getMeasurementSystems().stream()
        .filter(
            measurementSystem ->
                (measurementSystem.getLocaleId() == null
                    || localeId.equalsIgnoreCase(measurementSystem.getLocaleId())))
        .collect(Collectors.toList());
  }

  @Override
  @Cacheable(cacheNames = "reference", key = "'measurementUnitTypes.ALL'")
  public List<MeasurementUnitType> getMeasurementUnitTypes() throws ServiceUnavailableException {
    try {
      return measurementUnitTypeRepository.getAllMeasurementUnitTypes();
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to retrieve the measurement unit type reference data", e);
    }
  }

  @Override
  @Cacheable(cacheNames = "reference", key = "'measurementUnitTypes.' + #localeId")
  public List<MeasurementUnitType> getMeasurementUnitTypes(String localeId)
      throws InvalidArgumentException, ServiceUnavailableException {
    if (!StringUtils.hasText(localeId)) {
      throw new InvalidArgumentException("localeId");
    }

    return getReferenceService().getMeasurementUnitTypes().stream()
        .filter(
            measurementUnitType ->
                (measurementUnitType.getLocaleId() == null
                    || localeId.equalsIgnoreCase(measurementUnitType.getLocaleId())))
        .collect(Collectors.toList());
  }

  @Override
  @Cacheable(cacheNames = "reference", key = "'measurementUnits.ALL'")
  public List<MeasurementUnit> getMeasurementUnits() throws ServiceUnavailableException {
    try {
      return measurementUnitRepository.getAllMeasurementUnits();
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to retrieve the measurement unit reference data", e);
    }
  }

  @Override
  @Cacheable(cacheNames = "reference", key = "'measurementUnits.' + #localeId")
  public List<MeasurementUnit> getMeasurementUnits(String localeId)
      throws InvalidArgumentException, ServiceUnavailableException {
    if (!StringUtils.hasText(localeId)) {
      throw new InvalidArgumentException("localeId");
    }

    return getReferenceService().getMeasurementUnits().stream()
        .filter(
            measurementUnit ->
                (measurementUnit.getLocaleId() == null
                    || localeId.equalsIgnoreCase(measurementUnit.getLocaleId())))
        .collect(Collectors.toList());
  }

  @Override
  @Cacheable(cacheNames = "reference", key = "'regions.ALL'")
  public List<Region> getRegions() throws ServiceUnavailableException {
    try {
      return regionRepository.getAllRegions();
    } catch (Throwable e) {
      throw new ServiceUnavailableException("Failed to retrieve the region reference data", e);
    }
  }

  @Override
  @Cacheable(cacheNames = "reference", key = "'regions.' + #localeId")
  public List<Region> getRegions(String localeId)
      throws InvalidArgumentException, ServiceUnavailableException {
    if (!StringUtils.hasText(localeId)) {
      throw new InvalidArgumentException("localeId");
    }

    return getReferenceService().getRegions().stream()
        .filter(
            region ->
                (region.getLocaleId() == null || localeId.equalsIgnoreCase(region.getLocaleId())))
        .collect(Collectors.toList());
  }

  @Override
  @Cacheable(cacheNames = "reference", key = "'regions.' + #country + '.' + #localeId")
  public List<Region> getRegions(String localeId, String country)
      throws InvalidArgumentException, ServiceUnavailableException {
    if (!StringUtils.hasText(localeId)) {
      throw new InvalidArgumentException("localeId");
    }

    if (StringUtils.hasText(country)) {
      return getReferenceService().getRegions().stream()
          .filter(
              region ->
                  ((region.getLocaleId() == null || localeId.equalsIgnoreCase(region.getLocaleId()))
                      && (country.equalsIgnoreCase(region.getCountry()))))
          .collect(Collectors.toList());
    } else {
      return getReferenceService().getRegions().stream()
          .filter(
              region ->
                  (region.getLocaleId() == null || localeId.equalsIgnoreCase(region.getLocaleId())))
          .collect(Collectors.toList());
    }
  }

  @Override
  @Cacheable(cacheNames = "reference", key = "'timeZones.ALL'")
  public List<TimeZone> getTimeZones() throws ServiceUnavailableException {
    try {
      List<TimeZone> timeZones = new ArrayList<>();

      int sortIndex = 1;

      Locale locale = Locale.forLanguageTag("en-US");

      int maxLength = 0;

      for (String timeZoneId :
          com.ibm.icu.util.TimeZone.getAvailableIDs(SystemTimeZoneType.CANONICAL, null, null)
              .stream()
              .filter(timeZoneId -> (!timeZoneId.startsWith("SystemV")))
              .filter(timeZoneId -> (timeZoneId.contains("/")))
              .toList()) {

        maxLength = Math.max(maxLength, timeZoneId.length());

        String zoneName = com.ibm.icu.util.TimeZone.getTimeZone(timeZoneId).getDisplayName(locale);

        timeZones.add(new TimeZone(timeZoneId, "en-US", zoneName, zoneName, sortIndex));

        sortIndex++;
      }

      return timeZones;
    } catch (Throwable e) {
      throw new ServiceUnavailableException("Failed to retrieve the time zone reference data", e);
    }
  }

  @Override
  @Cacheable(cacheNames = "reference", key = "'timeZones.' + #localeId")
  public List<TimeZone> getTimeZones(String localeId)
      throws InvalidArgumentException, ServiceUnavailableException {
    if (!StringUtils.hasText(localeId)) {
      throw new InvalidArgumentException("localeId");
    }

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
              .toList()) {

        maxLength = Math.max(maxLength, timeZoneId.length());

        String zoneName = com.ibm.icu.util.TimeZone.getTimeZone(timeZoneId).getDisplayName(locale);

        timeZones.add(new TimeZone(timeZoneId, localeId, zoneName, zoneName, sortIndex));

        sortIndex++;
      }

      return timeZones;
    } catch (Throwable e) {
      throw new ServiceUnavailableException("Failed to retrieve the time zones", e);
    }
  }

  @Override
  public boolean isValidCountry(String countryCode) throws ServiceUnavailableException {
    if (!StringUtils.hasText(countryCode)) {
      return false;
    }

    return getReferenceService().getCountries().stream()
        .anyMatch(country -> country.getCode().equals(countryCode));
  }

  @Override
  public boolean isValidLanguage(String languageCode) throws ServiceUnavailableException {
    if (!StringUtils.hasText(languageCode)) {
      return false;
    }

    return getReferenceService().getLanguages().stream()
        .anyMatch(language -> language.getCode().equals(languageCode));
  }

  @Override
  public boolean isValidMeasurementSystem(String measurementSystemCode)
      throws ServiceUnavailableException {
    if (!StringUtils.hasText(measurementSystemCode)) {
      return false;
    }

    return getReferenceService().getMeasurementSystems().stream()
        .anyMatch(measurementSystem -> measurementSystem.getCode().equals(measurementSystemCode));
  }

  @Override
  public boolean isValidMeasurementUnit(String measurementUnitCode)
      throws ServiceUnavailableException {
    if (!StringUtils.hasText(measurementUnitCode)) {
      return false;
    }

    return getReferenceService().getMeasurementUnits().stream()
        .anyMatch(measurementUnit -> measurementUnit.getCode().equals(measurementUnitCode));
  }

  @Override
  public boolean isValidMeasurementUnitType(String measurementUnitTypeCode)
      throws ServiceUnavailableException {
    if (!StringUtils.hasText(measurementUnitTypeCode)) {
      return false;
    }

    return getReferenceService().getMeasurementUnitTypes().stream()
        .anyMatch(
            measurementUnitType -> measurementUnitType.getCode().equals(measurementUnitTypeCode));
  }

  @Override
  public boolean isValidRegion(String regionCode) throws ServiceUnavailableException {
    if (!StringUtils.hasText(regionCode)) {
      return false;
    }

    return getReferenceService().getRegions().stream()
        .anyMatch(region -> region.getCode().equals(regionCode));
  }

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

  /**
   * Returns the internal reference to the Reference Service to enable caching.
   *
   * @return the internal reference to the Reference Service to enable caching.
   */
  private ReferenceService getReferenceService() {
    return applicationContext.getBean(ReferenceService.class);
  }
}
