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
import digital.inception.reference.Language;
import digital.inception.reference.MaritalStatus;
import digital.inception.reference.MarriageType;
import digital.inception.reference.MinorType;
import digital.inception.reference.NextOfKinType;
import digital.inception.reference.Occupation;
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

    retrievedCommunicationMethods = referenceService.getCommunicationMethods("en-US");

    assertEquals(
        "The correct number of communication methods was not retrieved",
        3,
        retrievedCommunicationMethods.size());

    // NOTE: The code below is used to generate the insert statements for the Liquibase changeset
    if (false) {
      for (CommunicationMethod retrievedCommunicationMethod : retrievedCommunicationMethods) {

        System.out.println(
            "    <insert schemaName=\"reference\" tableName=\"communication_methods\">");
        System.out.println(
            "      <column name=\"code\" value=\""
                + retrievedCommunicationMethod.getCode()
                + "\"/>");
        System.out.println(
            "      <column name=\"locale\" value=\""
                + retrievedCommunicationMethod.getLocale()
                + "\"/>");
        System.out.println(
            "      <column name=\"sort_index\" value=\""
                + retrievedCommunicationMethod.getSortIndex()
                + "\"/>");
        System.out.println(
            "      <column name=\"name\" value=\""
                + retrievedCommunicationMethod.getName()
                + "\"/>");
        System.out.println(
            "      <column name=\"description\" value=\""
                + retrievedCommunicationMethod.getDescription()
                + "\"/>");
        System.out.println("    </insert>");
      }
    }
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

    retrievedCountries = referenceService.getCountries("en-US");

    assertEquals(
        "The correct number of countries was not retrieved", 249, retrievedCountries.size());

    retrievedCountry = retrievedCountries.get(0);

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

    // NOTE: The code below is used to generate the insert statements for the Liquibase changeset
    if (false) {
      for (Country country : retrievedCountries) {
        System.out.println("<insert schemaName=\"reference\" tableName=\"countries\">");
        System.out.println("  <column name=\"code\" value=\"" + country.getCode() + "\"/>");
        System.out.println("  <column name=\"locale\" value=\"" + country.getLocale() + "\"/>");
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

    retrievedEmploymentStatuses = referenceService.getEmploymentStatuses("en-US");

    assertEquals(
        "The correct number of employment statuses was not retrieved",
        3,
        retrievedEmploymentStatuses.size());

    // NOTE: The code below is used to generate the insert statements for the Liquibase changeset
    if (false) {
      for (EmploymentStatus retrievedEmploymentStatus : retrievedEmploymentStatuses) {

        System.out.println(
            "    <insert schemaName=\"reference\" tableName=\"employment_statuses\">");
        System.out.println(
            "      <column name=\"code\" value=\"" + retrievedEmploymentStatus.getCode() + "\"/>");
        System.out.println(
            "      <column name=\"locale\" value=\""
                + retrievedEmploymentStatus.getLocale()
                + "\"/>");
        System.out.println(
            "      <column name=\"sort_index\" value=\""
                + retrievedEmploymentStatus.getSortIndex()
                + "\"/>");
        System.out.println(
            "      <column name=\"name\" value=\"" + retrievedEmploymentStatus.getName() + "\"/>");
        System.out.println(
            "      <column name=\"description\" value=\""
                + retrievedEmploymentStatus.getDescription()
                + "\"/>");
        System.out.println("    </insert>");
      }
    }
  }

  /** Test the employment type reference functionality. */
  @Test
  public void employmentTypeTest() throws Exception {
    List<EmploymentType> retrievedEmploymentTypes = referenceService.getEmploymentTypes();

    assertEquals(
        "The correct number of employment types was not retrieved",
        26,
        retrievedEmploymentTypes.size());

    retrievedEmploymentTypes = referenceService.getEmploymentTypes("en-US");

    assertEquals(
        "The correct number of employment types was not retrieved",
        13,
        retrievedEmploymentTypes.size());

    // NOTE: The code below is used to generate the insert statements for the Liquibase changeset
    if (false) {
      for (EmploymentType retrievedEmploymentType : retrievedEmploymentTypes) {

        System.out.println("    <insert schemaName=\"reference\" tableName=\"employment_types\">");
        System.out.println(
            "      <column name=\"employment_status\" value=\""
                + retrievedEmploymentType.getEmploymentStatus()
                + "\"/>");
        System.out.println(
            "      <column name=\"code\" value=\"" + retrievedEmploymentType.getCode() + "\"/>");
        System.out.println(
            "      <column name=\"locale\" value=\""
                + retrievedEmploymentType.getLocale()
                + "\"/>");
        System.out.println(
            "      <column name=\"sort_index\" value=\""
                + retrievedEmploymentType.getSortIndex()
                + "\"/>");
        System.out.println(
            "      <column name=\"name\" value=\"" + retrievedEmploymentType.getName() + "\"/>");
        System.out.println(
            "      <column name=\"description\" value=\""
                + retrievedEmploymentType.getDescription()
                + "\"/>");
        System.out.println("    </insert>");
      }
    }
  }

  /** Test the gender reference functionality. */
  @Test
  public void genderTest() throws Exception {
    List<Gender> retrievedGenders = referenceService.getGenders();

    assertEquals("The correct number of genders was not retrieved", 12, retrievedGenders.size());

    retrievedGenders = referenceService.getGenders("en-US");

    assertEquals("The correct number of genders was not retrieved", 6, retrievedGenders.size());

    // NOTE: The code below is used to generate the insert statements for the Liquibase changeset
    if (false) {
      for (Gender retrievedGender : retrievedGenders) {

        System.out.println("    <insert schemaName=\"reference\" tableName=\"genders\">");
        System.out.println(
            "      <column name=\"code\" value=\"" + retrievedGender.getCode() + "\"/>");
        System.out.println(
            "      <column name=\"locale\" value=\"" + retrievedGender.getLocale() + "\"/>");
        System.out.println(
            "      <column name=\"sort_index\" value=\"" + retrievedGender.getSortIndex() + "\"/>");
        System.out.println(
            "      <column name=\"name\" value=\"" + retrievedGender.getName() + "\"/>");
        System.out.println(
            "      <column name=\"description\" value=\""
                + retrievedGender.getDescription()
                + "\"/>");
        System.out.println("    </insert>");
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

    retrievedIdentityDocumentTypes = referenceService.getIdentityDocumentTypes("en-US");

    assertEquals(
        "The correct number of identity document types was not retrieved",
        4,
        retrievedIdentityDocumentTypes.size());

    // NOTE: The code below is used to generate the insert statements for the Liquibase changeset
    if (false) {
      for (IdentityDocumentType retrievedIdentityDocumentType : retrievedIdentityDocumentTypes) {

        System.out.println("    <insert schemaName=\"reference\" tableName=\"genders\">");
        System.out.println(
            "      <column name=\"code\" value=\""
                + retrievedIdentityDocumentType.getCode()
                + "\"/>");
        System.out.println(
            "      <column name=\"locale\" value=\""
                + retrievedIdentityDocumentType.getLocale()
                + "\"/>");
        System.out.println(
            "      <column name=\"sort_index\" value=\""
                + retrievedIdentityDocumentType.getSortIndex()
                + "\"/>");
        System.out.println(
            "      <column name=\"name\" value=\""
                + retrievedIdentityDocumentType.getName()
                + "\"/>");
        System.out.println(
            "      <column name=\"description\" value=\""
                + retrievedIdentityDocumentType.getDescription()
                + "\"/>");
        System.out.println(
            "      <column name=\"country_of_issue\" value=\""
                + retrievedIdentityDocumentType.getCountryOfIssue()
                + "\"/>");
        System.out.println("    </insert>");
      }
    }
  }

  /** Test the language reference functionality. */
  @Test
  public void languageTest() throws Exception {

    List<Language> retrievedLanguages = referenceService.getLanguages();

    assertEquals(
        "The correct number of languages was not retrieved", 184, retrievedLanguages.size());

    retrievedLanguages = referenceService.getLanguages("en-US");

    assertEquals(
        "The correct number of languages was not retrieved", 92, retrievedLanguages.size());

    // NOTE: The code below is used to generate the insert statements for the Liquibase changeset
    if (false) {
      for (Language retrievedLanguage : retrievedLanguages) {

        System.out.println("    <insert schemaName=\"reference\" tableName=\"languages\">");
        System.out.println(
            "      <column name=\"code\" value=\"" + retrievedLanguage.getCode() + "\"/>");
        System.out.println(
            "      <column name=\"locale\" value=\"" + retrievedLanguage.getLocale() + "\"/>");
        System.out.println(
            "      <column name=\"sort_index\" value=\""
                + retrievedLanguage.getSortIndex()
                + "\"/>");
        System.out.println(
            "      <column name=\"name\" value=\"" + retrievedLanguage.getName() + "\"/>");
        System.out.println(
            "      <column name=\"short_name\" value=\""
                + retrievedLanguage.getShortName()
                + "\"/>");
        System.out.println(
            "      <column name=\"description\" value=\""
                + retrievedLanguage.getDescription()
                + "\"/>");
        System.out.println("    </insert>");
      }
    }
  }

  /** Test the marital status reference functionality. */
  @Test
  public void maritalStatusTest() throws Exception {
    List<MaritalStatus> retrievedMaritalStatuses = referenceService.getMaritalStatuses();

    assertEquals(
        "The correct number of marital statuses was not retrieved",
        12,
        retrievedMaritalStatuses.size());

    retrievedMaritalStatuses = referenceService.getMaritalStatuses("en-US");

    assertEquals(
        "The correct number of marital statuses was not retrieved",
        6,
        retrievedMaritalStatuses.size());

    // NOTE: The code below is used to generate the insert statements for the Liquibase changeset
    if (false) {
      for (MaritalStatus retrievedMaritalStatus : retrievedMaritalStatuses) {

        System.out.println("    <insert schemaName=\"reference\" tableName=\"marital_statuses\">");
        System.out.println(
            "      <column name=\"code\" value=\"" + retrievedMaritalStatus.getCode() + "\"/>");
        System.out.println(
            "      <column name=\"locale\" value=\"" + retrievedMaritalStatus.getLocale() + "\"/>");
        System.out.println(
            "      <column name=\"sort_index\" value=\""
                + retrievedMaritalStatus.getSortIndex()
                + "\"/>");
        System.out.println(
            "      <column name=\"name\" value=\"" + retrievedMaritalStatus.getName() + "\"/>");
        System.out.println(
            "      <column name=\"description\" value=\""
                + retrievedMaritalStatus.getDescription()
                + "\"/>");
        System.out.println("    </insert>");
      }
    }
  }

  /** Test the marriage type reference functionality. */
  @Test
  public void marriageTypeTest() throws Exception {
    List<MarriageType> retrievedMarriageTypes = referenceService.getMarriageTypes();

    assertEquals(
        "The correct number of marriage types was not retrieved",
        10,
        retrievedMarriageTypes.size());

    retrievedMarriageTypes = referenceService.getMarriageTypes("en-US");

    assertEquals(
        "The correct number of marriage types was not retrieved", 5, retrievedMarriageTypes.size());

    // NOTE: The code below is used to generate the insert statements for the Liquibase changeset
    if (false) {
      for (MarriageType retrievedMarriageType : retrievedMarriageTypes) {

        System.out.println("    <insert schemaName=\"reference\" tableName=\"marriage_types\">");
        System.out.println(
            "      <column name=\"marital_status\" value=\""
                + retrievedMarriageType.getMaritalStatus()
                + "\"/>");
        System.out.println(
            "      <column name=\"code\" value=\"" + retrievedMarriageType.getCode() + "\"/>");
        System.out.println(
            "      <column name=\"locale\" value=\"" + retrievedMarriageType.getLocale() + "\"/>");
        System.out.println(
            "      <column name=\"sort_index\" value=\""
                + retrievedMarriageType.getSortIndex()
                + "\"/>");
        System.out.println(
            "      <column name=\"name\" value=\"" + retrievedMarriageType.getName() + "\"/>");
        System.out.println(
            "      <column name=\"description\" value=\""
                + retrievedMarriageType.getDescription()
                + "\"/>");
        System.out.println("    </insert>");
      }
    }
  }

  /** Test the minor type reference functionality. */
  @Test
  public void minorTypeTest() throws Exception {
    List<MinorType> retrievedMinorTypes = referenceService.getMinorTypes();

    assertEquals(
        "The correct number of minor types was not retrieved", 8, retrievedMinorTypes.size());

    retrievedMinorTypes = referenceService.getMinorTypes("en-US");

    assertEquals(
        "The correct number of minor types was not retrieved", 4, retrievedMinorTypes.size());

    // NOTE: The code below is used to generate the insert statements for the Liquibase changeset
    if (false) {
      for (MinorType retrievedMinorType : retrievedMinorTypes) {

        System.out.println("    <insert schemaName=\"reference\" tableName=\"minor_types\">");
        System.out.println(
            "      <column name=\"code\" value=\"" + retrievedMinorType.getCode() + "\"/>");
        System.out.println(
            "      <column name=\"locale\" value=\"" + retrievedMinorType.getLocale() + "\"/>");
        System.out.println(
            "      <column name=\"sort_index\" value=\""
                + retrievedMinorType.getSortIndex()
                + "\"/>");
        System.out.println(
            "      <column name=\"name\" value=\"" + retrievedMinorType.getName() + "\"/>");
        System.out.println(
            "      <column name=\"description\" value=\""
                + retrievedMinorType.getDescription()
                + "\"/>");
        System.out.println("    </insert>");
      }
    }
  }

  /** Test the next of kin type reference functionality. */
  @Test
  public void nextOfKinTypeTest() throws Exception {
    List<NextOfKinType> retrievedNextOfKinTypes = referenceService.getNextOfKinTypes();

    assertEquals(
        "The correct number of next of kin types was not retrieved",
        34,
        retrievedNextOfKinTypes.size());

    retrievedNextOfKinTypes = referenceService.getNextOfKinTypes("en-US");

    assertEquals(
        "The correct number of next of kin types was not retrieved",
        17,
        retrievedNextOfKinTypes.size());

    // NOTE: The code below is used to generate the insert statements for the Liquibase changeset
    if (false) {
      for (NextOfKinType retrievedNextOfKinType : retrievedNextOfKinTypes) {

        System.out.println("    <insert schemaName=\"reference\" tableName=\"next_of_kin_types\">");
        System.out.println(
            "      <column name=\"code\" value=\"" + retrievedNextOfKinType.getCode() + "\"/>");
        System.out.println(
            "      <column name=\"locale\" value=\"" + retrievedNextOfKinType.getLocale() + "\"/>");
        System.out.println(
            "      <column name=\"sort_index\" value=\""
                + retrievedNextOfKinType.getSortIndex()
                + "\"/>");
        System.out.println(
            "      <column name=\"name\" value=\"" + retrievedNextOfKinType.getName() + "\"/>");
        System.out.println(
            "      <column name=\"description\" value=\""
                + retrievedNextOfKinType.getDescription()
                + "\"/>");
        System.out.println("    </insert>");
      }
    }
  }

  /** Test the occupation reference functionality. */
  @Test
  public void occupationTest() throws Exception {
    List<Occupation> retrievedOccupations = referenceService.getOccupations();

    assertEquals(
        "The correct number of occupations was not retrieved", 58 , retrievedOccupations.size());

    retrievedOccupations = referenceService.getOccupations("en-US");

    assertEquals(
        "The correct number of occupations was not retrieved", 29, retrievedOccupations.size());

    // NOTE: The code below is used to generate the insert statements for the Liquibase changeset
    if (false) {
      for (Occupation retrievedOccupation : retrievedOccupations) {

        System.out.println("    <insert schemaName=\"reference\" tableName=\"occupations\">");
        System.out.println(
            "      <column name=\"code\" value=\"" + retrievedOccupation.getCode() + "\"/>");
        System.out.println(
            "      <column name=\"locale\" value=\"" + retrievedOccupation.getLocale() + "\"/>");
        System.out.println(
            "      <column name=\"sort_index\" value=\""
                + retrievedOccupation.getSortIndex()
                + "\"/>");
        System.out.println(
            "      <column name=\"name\" value=\"" + retrievedOccupation.getName() + "\"/>");
        System.out.println(
            "      <column name=\"description\" value=\""
                + retrievedOccupation.getDescription()
                + "\"/>");
        System.out.println("    </insert>");
      }
    }
  }
}
