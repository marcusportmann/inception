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

package digital.inception.reference.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import digital.inception.reference.model.Country;
import digital.inception.reference.model.Language;
import digital.inception.reference.model.MeasurementSystem;
import digital.inception.reference.model.MeasurementUnit;
import digital.inception.reference.model.MeasurementUnitType;
import digital.inception.reference.model.Region;
import digital.inception.reference.model.TimeZone;
import digital.inception.reference.service.ReferenceService;
import digital.inception.test.InceptionExtension;
import digital.inception.test.TestConfiguration;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.ConfigDataApplicationContextInitializer;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;

/**
 * The {@code ReferenceServiceTests} class contains the JUnit tests for the {@code ReferenceService}
 * class.
 *
 * @author Marcus Portmann
 */
@ExtendWith(SpringExtension.class)
@ExtendWith(InceptionExtension.class)
@ContextConfiguration(
    classes = {TestConfiguration.class},
    initializers = {ConfigDataApplicationContextInitializer.class})
@TestExecutionListeners(
    listeners = {
      DependencyInjectionTestExecutionListener.class,
      DirtiesContextTestExecutionListener.class,
      TransactionalTestExecutionListener.class
    })
public class ReferenceServiceTests {

  /** The Reference Service. */
  @Autowired private ReferenceService referenceService;

  /** Test the country reference functionality. */
  @Test
  public void countryTest() throws Exception {
    List<Country> retrievedCountries =
        referenceService.getCountries(ReferenceService.DEFAULT_LOCALE_ID);

    assertEquals(
        246, retrievedCountries.size(), "The correct number of countries was not retrieved");

    Optional<Country> retrievedCountryOptional =
        retrievedCountries.stream().filter(country -> country.getCode().equals("AF")).findFirst();

    if (retrievedCountryOptional.isEmpty()) {
      fail("Failed to retrieve the country with code (AF)");
    } else {
      Country retrievedCountry = retrievedCountryOptional.get();

      assertEquals(
          "AF", retrievedCountry.getCode(), "The correct code was not retrieved for the country");
      assertEquals(
          ReferenceService.DEFAULT_LOCALE_ID,
          retrievedCountry.getLocaleId(),
          "The correct locale was not retrieved for the country");
      assertEquals(
          "Afghanistan",
          retrievedCountry.getName(),
          "The correct name was not retrieved for the country");
      assertEquals(
          "Afghanistan",
          retrievedCountry.getName(),
          "The correct short name was not retrieved for the country");
      assertEquals(
          "Afghanistan",
          retrievedCountry.getName(),
          "The correct description was not retrieved for the country");
    }

    retrievedCountries = referenceService.getCountries();

    assertEquals(
        492, retrievedCountries.size(), "The correct number of countries was not retrieved");
  }

  /** Generate the Country list for the static reference class. */
  @Test
  @Disabled
  public void generateCountryList() throws Exception {
    List<Country> countries = referenceService.getCountries();

    StringBuilder buffer = new StringBuilder();

    for (Country country : countries) {
      if (!buffer.isEmpty()) {
        buffer.append(",").append(System.lineSeparator());
      }
      buffer
          .append("new  Country(\"")
          .append(country.getCode())
          .append("\", \"")
          .append(country.getIso3Code())
          .append("\", ")
          .append((country.getSortIndex() == null) ? 1 : country.getSortIndex())
          .append(", \"")
          .append(country.getName())
          .append("\", \"")
          .append(country.getShortName())
          .append("\", \"")
          .append(country.getDescription())
          .append("\", \"")
          .append(country.getSovereignState())
          .append("\", \"")
          .append(country.getNationality())
          .append("\")");
    }

    System.out.println(
        "private static final List<Country> countries = List.of("
            + System.lineSeparator()
            + buffer
            + ");");
  }

  /** Generate the Language list for the static reference class. */
  @Test
  @Disabled
  public void generateLanguageList() throws Exception {
    List<Language> languages = referenceService.getLanguages();

    StringBuilder buffer = new StringBuilder();

    for (Language language : languages) {
      if (!buffer.isEmpty()) {
        buffer.append(",").append(System.lineSeparator());
      }
      buffer
          .append("new  Language(\"")
          .append(language.getCode())
          .append("\", \"")
          .append(language.getIso3Code())
          .append("\", ")
          .append((language.getSortIndex() == null) ? 1 : language.getSortIndex())
          .append(", \"")
          .append(language.getName())
          .append("\", \"")
          .append(language.getShortName())
          .append("\", \"")
          .append(language.getDescription())
          .append("\")");
    }

    System.out.println(
        "private static final List<Language> languages = List.of("
            + System.lineSeparator()
            + buffer
            + ");");
  }

  /** Test the language reference functionality. */
  @Test
  public void languageTest() throws Exception {

    List<Language> retrievedLanguages =
        referenceService.getLanguages(ReferenceService.DEFAULT_LOCALE_ID);

    assertEquals(
        93, retrievedLanguages.size(), "The correct number of languages was not retrieved");

    retrievedLanguages = referenceService.getLanguages();

    assertEquals(
        186, retrievedLanguages.size(), "The correct number of languages was not retrieved");
  }

  /** Test the measurement systems reference functionality. */
  @Test
  public void measurementSystemsTest() throws Exception {
    List<MeasurementSystem> retrievedMeasurementSystems =
        referenceService.getMeasurementSystems(ReferenceService.DEFAULT_LOCALE_ID);

    assertEquals(
        3,
        retrievedMeasurementSystems.size(),
        "The correct number of measurement systems was not retrieved");

    retrievedMeasurementSystems = referenceService.getMeasurementSystems();

    assertEquals(
        6,
        retrievedMeasurementSystems.size(),
        "The correct number of measurement systems was not retrieved");
  }

  /** Test the measurement unit types reference functionality. */
  @Test
  public void measurementUnitTypesTest() throws Exception {
    List<MeasurementUnitType> retrievedMeasurementUnitTypes =
        referenceService.getMeasurementUnitTypes(ReferenceService.DEFAULT_LOCALE_ID);

    assertEquals(
        3,
        retrievedMeasurementUnitTypes.size(),
        "The correct number of measurement unit types was not retrieved");

    retrievedMeasurementUnitTypes = referenceService.getMeasurementUnitTypes();

    assertEquals(
        6,
        retrievedMeasurementUnitTypes.size(),
        "The correct number of measurement unit types was not retrieved");
  }

  /** Test the measurement units reference functionality. */
  @Test
  public void measurementUnitsTest() throws Exception {
    List<MeasurementUnit> retrievedMeasurementUnits =
        referenceService.getMeasurementUnits(ReferenceService.DEFAULT_LOCALE_ID);

    assertEquals(
        9,
        retrievedMeasurementUnits.size(),
        "The correct number of measurement units was not retrieved");

    retrievedMeasurementUnits = referenceService.getMeasurementUnits();

    assertEquals(
        18,
        retrievedMeasurementUnits.size(),
        "The correct number of measurement units was not retrieved");
  }

  /** Test the region reference functionality. */
  @Test
  public void regionTest() throws Exception {
    List<Region> retrievedRegions = referenceService.getRegions(ReferenceService.DEFAULT_LOCALE_ID);

    assertEquals(9, retrievedRegions.size(), "The correct number of regions was not retrieved");

    retrievedRegions = referenceService.getRegions();

    assertEquals(18, retrievedRegions.size(), "The correct number of regions was not retrieved");

    retrievedRegions = referenceService.getRegions(ReferenceService.DEFAULT_LOCALE_ID, "ZA");

    assertEquals(9, retrievedRegions.size(), "The correct number of regions was not retrieved");

    retrievedRegions = referenceService.getRegions(ReferenceService.DEFAULT_LOCALE_ID, "GB");

    assertEquals(0, retrievedRegions.size(), "The correct number of regions was not retrieved");
  }

  /** Test the time zone reference functionality. */
  @Test
  public void timeZoneTest() throws Exception {
    List<TimeZone> retrievedTimeZones =
        referenceService.getTimeZones(ReferenceService.DEFAULT_LOCALE_ID);

    if (retrievedTimeZones.size() <= 0) {
      fail("Failed to retrieve the time zones");
    }
  }

  /** Test the reference validity check functionality. */
  @Test
  public void validityTest() throws Exception {
    assertTrue(referenceService.isValidCountry("ZA"));
    assertTrue(referenceService.isValidLanguage("EN"));
    assertTrue(referenceService.isValidMeasurementSystem("metric"));
    assertTrue(referenceService.isValidMeasurementUnit("metric_centimeter"));
    assertTrue(referenceService.isValidMeasurementUnitType("length"));
    assertTrue(referenceService.isValidTimeZone("Africa/Johannesburg"));
    assertTrue(referenceService.isValidRegion("ZA-EC"));
  }
}
