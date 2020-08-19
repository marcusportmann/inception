/*
 * Copyright 2020 Marcus Portmann
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

// ~--- non-JDK imports --------------------------------------------------------

import static org.junit.Assert.assertEquals;

import digital.inception.reference.CommunicationMethod;
import digital.inception.reference.Country;
import digital.inception.reference.EmploymentStatus;
import digital.inception.reference.EmploymentType;
import digital.inception.reference.Gender;
import digital.inception.reference.IReferenceService;
import digital.inception.reference.IdentityDocumentType;
import digital.inception.test.TestClassRunner;
import digital.inception.test.TestConfiguration;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.List;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;
import org.springframework.util.StringUtils;

// ~--- JDK imports ------------------------------------------------------------

/**
 * The <code>ReferenceServiceTest</code> class contains the implementation of the JUnit tests for
 * the <code>ReferenceService</code> class.
 *
 * @author Marcus Portmann
 */
@RunWith(TestClassRunner.class)
@ContextConfiguration(classes = {TestConfiguration.class})
@TestExecutionListeners(
    listeners = {
      DependencyInjectionTestExecutionListener.class,
      DirtiesContextTestExecutionListener.class,
      TransactionalTestExecutionListener.class
    })
public class ReferenceServiceTest {

  /** The Reference Service. */
  @Autowired private IReferenceService referenceService;

  /** Test the communication method reference functionality. */
  @Test
  public void communicationMethodTest() throws Exception {
    List<CommunicationMethod> retrievedCommunicationMethods =
        referenceService.getCommunicationMethods();

    assertEquals(
        "The correct number of communication methods was not retrieved",
        6,
        retrievedCommunicationMethods.size());

    retrievedCommunicationMethods = referenceService.getCommunicationMethods("en-ZA");

    assertEquals(
        "The correct number of communication methods was not retrieved",
        3,
        retrievedCommunicationMethods.size());
  }

  /** Test the country reference functionality. */
  @Test
  public void countryTest() throws Exception {
    List<Country> retrievedCountries = referenceService.getCountries();

    Country retrievedCountry = retrievedCountries.get(0);

    assertEquals(
        "The correct number of countries was not retrieved", 498, retrievedCountries.size());

    assertEquals(
        "The correct code was not retrieved for the country", "AD", retrievedCountry.getCode());
    assertEquals(
        "The correct locale was not retrieved for the country",
        "en-US",
        retrievedCountry.getLocale());
    assertEquals(
        "The correct name was not retrieved for the country",
        "Andorra",
        retrievedCountry.getName());
    assertEquals(
        "The correct short name was not retrieved for the country",
        "Andorra",
        retrievedCountry.getName());
    assertEquals(
        "The correct description was not retrieved for the country",
        "Andorra",
        retrievedCountry.getName());

    if (false) {
      for (Country country : retrievedCountries) {
        System.out.println(
            "INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)");
        System.out.println(
            "   VALUES ('"
                + country.getCode()
                + "', '"
                + country.getLocale()
                + "', "
                + country.getSortIndex()
                + ", '"
                + country.getName().replace("'", "''")
                + "', '"
                + country.getShortName().replace("'", "''")
                + "', '"
                + country.getDescription().replace("'", "''")
                + "');");
      }
    }

    retrievedCountries = referenceService.getCountries("en-ZA");

    assertEquals(
        "The correct number of countries was not retrieved", 249, retrievedCountries.size());

    retrievedCountry = retrievedCountries.get(0);

    assertEquals(
        "The correct code was not retrieved for the country", "AD", retrievedCountry.getCode());
    assertEquals(
        "The correct locale was not retrieved for the country",
        "en-ZA",
        retrievedCountry.getLocale());
    assertEquals(
        "The correct name was not retrieved for the country",
        "Andorra",
        retrievedCountry.getName());
    assertEquals(
        "The correct short name was not retrieved for the country",
        "Andorra",
        retrievedCountry.getName());
    assertEquals(
        "The correct description was not retrieved for the country",
        "Andorra",
        retrievedCountry.getName());

    retrievedCountries = referenceService.getCountries("en-US");

    // NOTE: The code below is used to generate the insert statements for the Liquibase changeset
    if (false) {
      for (Country country : retrievedCountries) {
        System.out.println("<insert schemaName=\"reference\" tableName=\"countries\">");
        System.out.println("  <column name=\"code\" value=\"" + country.getCode() + "\"/>");
        System.out.println("  <column name=\"locale\" value=\"en-US\"/>");
        System.out.println(
            "  <column name=\"sort_index\" value=\"" + country.getSortIndex() + "\"/>");
        System.out.println("  <column name=\"name\" value=\"" + country.getName() + "\"/>");
        System.out.println(
            "  <column name=\"short_name\" value=\"" + country.getShortName() + "\"/>");
        System.out.println(
            "  <column name=\"description\" value=\"" + country.getDescription() + "\"/>");
        System.out.println("</insert>");
      }
    }
  }

  /** Test the employment status reference functionality. */
  @Test
  public void employmentStatusTest() throws Exception {
    List<EmploymentStatus> retrievedEmploymentStatuses = referenceService.getEmploymentStatuses();

    assertEquals(
        "The correct number of employment statuses was not retrieved",
        6,
        retrievedEmploymentStatuses.size());

    retrievedEmploymentStatuses = referenceService.getEmploymentStatuses("en-ZA");

    assertEquals(
        "The correct number of employment statuses was not retrieved",
        3,
        retrievedEmploymentStatuses.size());
  }

  /** Test the employment type reference functionality. */
  @Test
  public void employmentTypeTest() throws Exception {
    List<EmploymentType> retrievedEmploymentTypes = referenceService.getEmploymentTypes();

    assertEquals(
        "The correct number of employment types was not retrieved",
        26,
        retrievedEmploymentTypes.size());

    retrievedEmploymentTypes = referenceService.getEmploymentTypes("en-ZA");

    assertEquals(
        "The correct number of employment types was not retrieved",
        13,
        retrievedEmploymentTypes.size());
  }

  /** Test the gender reference functionality. */
  @Test
  public void genderTest() throws Exception {
    List<Gender> retrievedGenders = referenceService.getGenders();

    assertEquals("The correct number of genders was not retrieved", 12, retrievedGenders.size());

    retrievedGenders = referenceService.getGenders("en-ZA");

    assertEquals("The correct number of genders was not retrieved", 6, retrievedGenders.size());
  }

  @Test
  public void generateLanguagesSQL() throws Exception {

    if (false) {

      BufferedReader reader =
          new BufferedReader(
              new InputStreamReader(
                  Thread.currentThread()
                      .getContextClassLoader()
                      .getResourceAsStream("csv/languages.csv")));

      String line = reader.readLine();

      int counter = 1;

      while (line != null) {
        String[] parts = line.split(",");

        if (parts.length != 3) {
          Assert.fail("Invalid number of language parts");
        }

        System.out.println(
            "INSERT INTO reference.languages (code, locale, sort_index, name, short_name, description)");
        System.out.println(
            "   VALUES ('"
                + parts[0]
                + "', 'en-US', "
                + counter
                + ", '"
                + parts[1].replace("'", "''").replace(";", ",")
                + "', '"
                + parts[1].replace("'", "''").replace(";", ",")
                + "', '"
                + parts[2].replace("'", "''").replace(";", ",")
                + "');");

        line = reader.readLine();
        counter++;
      }
    }

    if (false) {

      BufferedReader reader =
          new BufferedReader(
              new InputStreamReader(
                  Thread.currentThread()
                      .getContextClassLoader()
                      .getResourceAsStream("csv/languages.csv")));

      String line = reader.readLine();

      int counter = 1;

      while (line != null) {
        String[] parts = line.split(",");

        if (parts.length != 3) {
          Assert.fail("Invalid number of language parts");
        }

        System.out.println("    <insert schemaName=\"reference\" tableName=\"languages\">");
        System.out.println("      <column name=\"code\" value=\"" + parts[0] + "\"/>");
        System.out.println("      <column name=\"locale\" value=\"en-US\"/>");
        System.out.println("      <column name=\"sort_index\" value=\"" + counter + "\"/>");
        System.out.println("      <column name=\"name\" value=\"" + parts[1].replace(";", ",") + "\"/>");
        System.out.println("      <column name=\"short_name\" value=\"" + parts[1].replace(";", ",") + "\"/>");
        System.out.println("      <column name=\"description\" value=\"" + parts[2].replace(";", ",") + "\"/>");
        System.out.println("    </insert>");

        line = reader.readLine();
        counter++;
      }
    }
  }

  /** Test the identity document type reference functionality. */
  @Test
  public void identityDocumentTypeTest() throws Exception {
    List<IdentityDocumentType> retrievedIdentityDocumentTypes =
        referenceService.getIdentityDocumentTypes();

    assertEquals(
        "The correct number of identity document types was not retrieved",
        8,
        retrievedIdentityDocumentTypes.size());

    retrievedIdentityDocumentTypes = referenceService.getIdentityDocumentTypes("en-ZA");

    assertEquals(
        "The correct number of identity document types was not retrieved",
        4,
        retrievedIdentityDocumentTypes.size());
  }
}
