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

package digital.inception.reference.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import digital.inception.reference.Country;
import digital.inception.reference.IReferenceService;
import digital.inception.reference.Language;
import digital.inception.reference.Region;
import digital.inception.test.TestClassRunner;
import digital.inception.test.TestConfiguration;
import java.util.List;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.ConfigDataApplicationContextInitializer;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;

/**
 * The <b>ReferenceServiceTest</b> class contains the implementation of the JUnit tests for the
 * <b>ReferenceService</b> class.
 *
 * @author Marcus Portmann
 */
@RunWith(TestClassRunner.class)
@ContextConfiguration(
    classes = {TestConfiguration.class},
    initializers = {ConfigDataApplicationContextInitializer.class})
@TestExecutionListeners(
    listeners = {
      DependencyInjectionTestExecutionListener.class,
      DirtiesContextTestExecutionListener.class,
      TransactionalTestExecutionListener.class
    })
public class ReferenceServiceTest {

  /** The Reference Service. */
  @Autowired private IReferenceService referenceService;

  /** Test the country reference functionality. */
  @Test
  public void countryTest() throws Exception {
    List<Country> retrievedCountries = referenceService.getCountries();

    Country retrievedCountry = retrievedCountries.get(0);

    assertEquals(
        "The correct number of countries was not retrieved", 492, retrievedCountries.size());

    assertEquals(
        "The correct code was not retrieved for the country", "AF", retrievedCountry.getCode());
    assertEquals(
        "The correct locale was not retrieved for the country",
        "en-US",
        retrievedCountry.getLocaleId());
    assertEquals(
        "The correct name was not retrieved for the country",
        "Afghanistan",
        retrievedCountry.getName());
    assertEquals(
        "The correct short name was not retrieved for the country",
        "Afghanistan",
        retrievedCountry.getName());
    assertEquals(
        "The correct description was not retrieved for the country",
        "Afghanistan",
        retrievedCountry.getName());

    retrievedCountries = referenceService.getCountries("en-US");

    assertEquals(
        "The correct number of countries was not retrieved", 246, retrievedCountries.size());

    retrievedCountry = retrievedCountries.get(0);

    assertEquals(
        "The correct code was not retrieved for the country", "AF", retrievedCountry.getCode());
    assertEquals(
        "The correct locale was not retrieved for the country",
        "en-US",
        retrievedCountry.getLocaleId());
    assertEquals(
        "The correct name was not retrieved for the country",
        "Afghanistan",
        retrievedCountry.getName());
    assertEquals(
        "The correct short name was not retrieved for the country",
        "Afghanistan",
        retrievedCountry.getName());
    assertEquals(
        "The correct description was not retrieved for the country",
        "Afghanistan",
        retrievedCountry.getName());
  }

  /** Test the language reference functionality. */
  @Test
  public void languageTest() throws Exception {

    List<Language> retrievedLanguages = referenceService.getLanguages();

    assertEquals(
        "The correct number of languages was not retrieved", 186, retrievedLanguages.size());

    retrievedLanguages = referenceService.getLanguages("en-US");

    assertEquals(
        "The correct number of languages was not retrieved", 93, retrievedLanguages.size());
  }

  /** Test the region reference functionality. */
  @Test
  public void regionTest() throws Exception {
    List<Region> retrievedRegions = referenceService.getRegions();

    assertEquals("The correct number of regions was not retrieved", 18, retrievedRegions.size());

    retrievedRegions = referenceService.getRegions("en-US");

    assertEquals("The correct number of regions was not retrieved", 9, retrievedRegions.size());
  }

  /** Test the reference validity check functionality. */
  @Test
  public void validityTest() throws Exception {
    assertTrue(referenceService.isValidCountry("ZA"));
    assertTrue(referenceService.isValidLanguage("EN"));
    assertTrue(referenceService.isValidRegion("EC"));
  }
}
