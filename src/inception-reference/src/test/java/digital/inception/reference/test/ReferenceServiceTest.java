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
import digital.inception.test.TestClassRunner;
import digital.inception.test.TestConfiguration;
import java.util.List;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;

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

    assertEquals(
        "The correct number of countries was not retrieved", 498, retrievedCountries.size());

    retrievedCountries = referenceService.getCountries("en-ZA");

    assertEquals(
        "The correct number of countries was not retrieved", 249, retrievedCountries.size());

    // NOTE: The code below is used to generate the insert statements for the Liquibase changeset
    //    for (Country retrievedCountry : retrievedCountries) {
    //      System.out.println("<insert schemaName=\"reference\" tableName=\"countries\">");
    //      System.out.println("  <column name=\"code\" value=\"" + retrievedCountry.getCode() +
    // "\"/>");
    //      System.out.println("  <column name=\"locale\" value=\"en-US\"/>");
    //      System.out.println("  <column name=\"sort_index\" value=\"" +
    // retrievedCountry.getSortIndex() + "\"/>");
    //      System.out.println("  <column name=\"name\" value=\"" + retrievedCountry.getName() +
    // "\"/>");
    //      System.out.println(
    //          "  <column name=\"description\" value=\"" + retrievedCountry.getDescription() +
    // "\"/>");
    //      System.out.println("</insert>");
    //    }

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
}
