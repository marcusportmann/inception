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
import static org.junit.Assert.fail;

import digital.inception.reference.AddressType;
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
import digital.inception.reference.PermitType;
import digital.inception.reference.Race;
import digital.inception.reference.Region;
import digital.inception.reference.ResidentialStatus;
import digital.inception.reference.ResidentialType;
import digital.inception.reference.SourceOfFunds;
import digital.inception.reference.SuitableTimeToContact;
import digital.inception.reference.TaxNumberType;
import digital.inception.reference.Title;
import digital.inception.reference.VerificationMethod;
import digital.inception.reference.VerificationStatus;
import digital.inception.test.TestClassRunner;
import digital.inception.test.TestConfiguration;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
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

  /** Test the address type reference functionality. */
  @Test
  public void addressTypeTest() throws Exception {
    List<AddressType> retrievedAddressTypes = referenceService.getAddressTypes();

    assertEquals(
        "The correct number of address types was not retrieved", 14, retrievedAddressTypes.size());

    retrievedAddressTypes = referenceService.getAddressTypes("en-US");

    assertEquals(
        "The correct number of address types was not retrieved", 7, retrievedAddressTypes.size());
  }

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
  }

  /** Test the country reference functionality. */
  @Test
  public void countryTest() throws Exception {
    List<Country> retrievedCountries = referenceService.getCountries();

    Country retrievedCountry = retrievedCountries.get(0);

    assertEquals(
        "The correct number of countries was not retrieved", 490, retrievedCountries.size());

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
        "The correct number of countries was not retrieved", 245, retrievedCountries.size());

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

  /** Create the Liquibase inserts. */
  @Test
  public void createLiquibaseInserts() throws Exception {
    boolean createLiquibaseInserts = false;
    boolean createAddressTypeInserts = createLiquibaseInserts && false;
    boolean createCommunicationMethodInserts = createLiquibaseInserts && false;
    boolean createCountryInserts = createLiquibaseInserts && false;
    boolean createEmploymentStatusInserts = createLiquibaseInserts && false;
    boolean createGenderInserts = createLiquibaseInserts && false;
    boolean createIdentityDocumentTypeInserts = createLiquibaseInserts && false;
    boolean createLanguageInserts = createLiquibaseInserts && false;
    boolean createMaritalStatusInserts = createLiquibaseInserts && false;
    boolean createMarriageTypeInserts = createLiquibaseInserts && false;
    boolean createMinorTypeInserts = createLiquibaseInserts && false;
    boolean createNextOfKinInserts = createLiquibaseInserts && false;
    boolean createOccupationInserts = createLiquibaseInserts && false;
    boolean createPermitTypeInserts = createLiquibaseInserts && true;

    if (createAddressTypeInserts) {
      for (AddressType addressType : referenceService.getAddressTypes("en-US")) {

        System.out.println("    <insert schemaName=\"reference\" tableName=\"address_types\">");
        System.out.println("      <column name=\"code\" value=\"" + addressType.getCode() + "\"/>");
        System.out.println(
            "      <column name=\"locale\" value=\"" + addressType.getLocaleId() + "\"/>");
        System.out.println(
            "      <column name=\"sort_index\" value=\"" + addressType.getSortIndex() + "\"/>");
        System.out.println("      <column name=\"name\" value=\"" + addressType.getName() + "\"/>");
        System.out.println(
            "      <column name=\"description\" value=\"" + addressType.getDescription() + "\"/>");
        System.out.println("    </insert>");
      }

      System.out.println();
    }

    if (createCommunicationMethodInserts) {

      for (CommunicationMethod communicationMethod :
          referenceService.getCommunicationMethods("en-US")) {

        System.out.println(
            "    <insert schemaName=\"reference\" tableName=\"communication_methods\">");
        System.out.println(
            "      <column name=\"code\" value=\"" + communicationMethod.getCode() + "\"/>");
        System.out.println(
            "      <column name=\"locale\" value=\"" + communicationMethod.getLocaleId() + "\"/>");
        System.out.println(
            "      <column name=\"sort_index\" value=\""
                + communicationMethod.getSortIndex()
                + "\"/>");
        System.out.println(
            "      <column name=\"name\" value=\"" + communicationMethod.getName() + "\"/>");
        System.out.println(
            "      <column name=\"description\" value=\""
                + communicationMethod.getDescription()
                + "\"/>");
        System.out.println("    </insert>");
      }

      System.out.println();
    }

    if (createCountryInserts) {

      List<Country> sortedCountries =
          referenceService.getCountries("en-US").stream()
              .sorted(Comparator.comparing(Country::getName))
              .collect(Collectors.toList());

      int counter = 1;

      for (Country country : sortedCountries) {

        if (country.getNationality().contains(",")) {
          fail("Invalid nationality for country (" + country.getCode() + ")");
        }

        System.out.println("<insert schemaName=\"reference\" tableName=\"countries\">");
        System.out.println("  <column name=\"code\" value=\"" + country.getCode() + "\"/>");
        System.out.println("  <column name=\"locale\" value=\"" + country.getLocaleId() + "\"/>");
        System.out.println("  <column name=\"sort_index\" value=\"" + counter + "\"/>");
        System.out.println("  <column name=\"name\" value=\"" + country.getName() + "\"/>");
        System.out.println(
            "  <column name=\"short_name\" value=\"" + country.getShortName() + "\"/>");
        System.out.println(
            "  <column name=\"description\" value=\"" + country.getDescription() + "\"/>");
        System.out.println(
            "  <column name=\"sovereign_state\" value=\"" + country.getSovereignState() + "\"/>");
        System.out.println(
            "  <column name=\"nationality\" value=\"" + country.getNationality() + "\"/>");
        System.out.println("</insert>");

        counter++;
      }

      System.out.println();
    }

    if (createEmploymentStatusInserts) {

      for (EmploymentStatus employmentStatus : referenceService.getEmploymentStatuses("en-US")) {

        System.out.println(
            "    <insert schemaName=\"reference\" tableName=\"employment_statuses\">");
        System.out.println(
            "      <column name=\"code\" value=\"" + employmentStatus.getCode() + "\"/>");
        System.out.println(
            "      <column name=\"locale\" value=\"" + employmentStatus.getLocaleId() + "\"/>");
        System.out.println(
            "      <column name=\"sort_index\" value=\""
                + employmentStatus.getSortIndex()
                + "\"/>");
        System.out.println(
            "      <column name=\"name\" value=\"" + employmentStatus.getName() + "\"/>");
        System.out.println(
            "      <column name=\"description\" value=\""
                + employmentStatus.getDescription()
                + "\"/>");
        System.out.println("    </insert>");
      }

      System.out.println();

      for (EmploymentType employmentType : referenceService.getEmploymentTypes("en-US")) {

        System.out.println("    <insert schemaName=\"reference\" tableName=\"employment_types\">");
        System.out.println(
            "      <column name=\"employment_status\" value=\""
                + employmentType.getEmploymentStatus()
                + "\"/>");
        System.out.println(
            "      <column name=\"code\" value=\"" + employmentType.getCode() + "\"/>");
        System.out.println(
            "      <column name=\"locale\" value=\"" + employmentType.getLocaleId() + "\"/>");
        System.out.println(
            "      <column name=\"sort_index\" value=\"" + employmentType.getSortIndex() + "\"/>");
        System.out.println(
            "      <column name=\"name\" value=\"" + employmentType.getName() + "\"/>");
        System.out.println(
            "      <column name=\"description\" value=\""
                + employmentType.getDescription()
                + "\"/>");
        System.out.println("    </insert>");
      }

      System.out.println();
    }

    if (createGenderInserts) {

      for (Gender gender : referenceService.getGenders("en-US")) {

        System.out.println("    <insert schemaName=\"reference\" tableName=\"genders\">");
        System.out.println("      <column name=\"code\" value=\"" + gender.getCode() + "\"/>");
        System.out.println("      <column name=\"locale\" value=\"" + gender.getLocaleId() + "\"/>");
        System.out.println(
            "      <column name=\"sort_index\" value=\"" + gender.getSortIndex() + "\"/>");
        System.out.println("      <column name=\"name\" value=\"" + gender.getName() + "\"/>");
        System.out.println(
            "      <column name=\"description\" value=\"" + gender.getDescription() + "\"/>");
        System.out.println("    </insert>");
      }

      System.out.println();
    }

    if (createIdentityDocumentTypeInserts) {

      for (IdentityDocumentType identityDocumentType :
          referenceService.getIdentityDocumentTypes("en-US")) {

        System.out.println(
            "    <insert schemaName=\"reference\" tableName=\"identity_document_types\">");
        System.out.println(
            "      <column name=\"code\" value=\"" + identityDocumentType.getCode() + "\"/>");
        System.out.println(
            "      <column name=\"locale\" value=\"" + identityDocumentType.getLocaleId() + "\"/>");
        System.out.println(
            "      <column name=\"sort_index\" value=\""
                + identityDocumentType.getSortIndex()
                + "\"/>");
        System.out.println(
            "      <column name=\"name\" value=\"" + identityDocumentType.getName() + "\"/>");
        System.out.println(
            "      <column name=\"description\" value=\""
                + identityDocumentType.getDescription()
                + "\"/>");
        System.out.println(
            "      <column name=\"country_of_issue\" value=\""
                + identityDocumentType.getCountryOfIssue()
                + "\"/>");
        System.out.println("    </insert>");
      }

      System.out.println();
    }

    if (createLanguageInserts) {

      for (Language language : referenceService.getLanguages("en-US")) {

        System.out.println("    <insert schemaName=\"reference\" tableName=\"languages\">");
        System.out.println(
            "      <column name=\"code\" value=\"" + language.getCode().toUpperCase() + "\"/>");
        System.out.println(
            "      <column name=\"locale\" value=\"" + language.getLocaleId() + "\"/>");
        System.out.println(
            "      <column name=\"sort_index\" value=\"" + language.getSortIndex() + "\"/>");
        System.out.println("      <column name=\"name\" value=\"" + language.getName() + "\"/>");
        System.out.println(
            "      <column name=\"short_name\" value=\"" + language.getShortName() + "\"/>");
        System.out.println(
            "      <column name=\"description\" value=\"" + language.getDescription() + "\"/>");
        System.out.println("    </insert>");
      }

      System.out.println();
    }

    if (createMaritalStatusInserts) {

      for (MaritalStatus maritalStatus : referenceService.getMaritalStatuses("en-US")) {

        System.out.println("    <insert schemaName=\"reference\" tableName=\"marital_statuses\">");
        System.out.println(
            "      <column name=\"code\" value=\"" + maritalStatus.getCode() + "\"/>");
        System.out.println(
            "      <column name=\"locale\" value=\"" + maritalStatus.getLocaleId() + "\"/>");
        System.out.println(
            "      <column name=\"sort_index\" value=\"" + maritalStatus.getSortIndex() + "\"/>");
        System.out.println(
            "      <column name=\"name\" value=\"" + maritalStatus.getName() + "\"/>");
        System.out.println(
            "      <column name=\"description\" value=\""
                + maritalStatus.getDescription()
                + "\"/>");
        System.out.println("    </insert>");
      }

      System.out.println();
    }

    if (createMarriageTypeInserts) {

      for (MarriageType marriageType : referenceService.getMarriageTypes("en-US")) {

        System.out.println("    <insert schemaName=\"reference\" tableName=\"marriage_types\">");
        System.out.println(
            "      <column name=\"marital_status\" value=\""
                + marriageType.getMaritalStatus()
                + "\"/>");
        System.out.println(
            "      <column name=\"code\" value=\"" + marriageType.getCode() + "\"/>");
        System.out.println(
            "      <column name=\"locale\" value=\"" + marriageType.getLocaleId() + "\"/>");
        System.out.println(
            "      <column name=\"sort_index\" value=\"" + marriageType.getSortIndex() + "\"/>");
        System.out.println(
            "      <column name=\"name\" value=\"" + marriageType.getName() + "\"/>");
        System.out.println(
            "      <column name=\"description\" value=\"" + marriageType.getDescription() + "\"/>");
        System.out.println("    </insert>");
      }

      System.out.println();
    }

    if (createMinorTypeInserts) {

      for (MinorType minorType : referenceService.getMinorTypes("en-US")) {

        System.out.println("    <insert schemaName=\"reference\" tableName=\"minor_types\">");
        System.out.println("      <column name=\"code\" value=\"" + minorType.getCode() + "\"/>");
        System.out.println(
            "      <column name=\"locale\" value=\"" + minorType.getLocaleId() + "\"/>");
        System.out.println(
            "      <column name=\"sort_index\" value=\"" + minorType.getSortIndex() + "\"/>");
        System.out.println("      <column name=\"name\" value=\"" + minorType.getName() + "\"/>");
        System.out.println(
            "      <column name=\"description\" value=\"" + minorType.getDescription() + "\"/>");
        System.out.println("    </insert>");
      }

      System.out.println();
    }

    if (createNextOfKinInserts) {

      for (NextOfKinType nextOfKinType : referenceService.getNextOfKinTypes("en-US")) {

        System.out.println("    <insert schemaName=\"reference\" tableName=\"next_of_kin_types\">");
        System.out.println(
            "      <column name=\"code\" value=\"" + nextOfKinType.getCode() + "\"/>");
        System.out.println(
            "      <column name=\"locale\" value=\"" + nextOfKinType.getLocaleId() + "\"/>");
        System.out.println(
            "      <column name=\"sort_index\" value=\"" + nextOfKinType.getSortIndex() + "\"/>");
        System.out.println(
            "      <column name=\"name\" value=\"" + nextOfKinType.getName() + "\"/>");
        System.out.println(
            "      <column name=\"description\" value=\""
                + nextOfKinType.getDescription()
                + "\"/>");
        System.out.println("    </insert>");
      }

      System.out.println();
    }

    if (createOccupationInserts) {

      for (Occupation occupation : referenceService.getOccupations("en-US")) {

        System.out.println("    <insert schemaName=\"reference\" tableName=\"occupations\">");
        System.out.println("      <column name=\"code\" value=\"" + occupation.getCode() + "\"/>");
        System.out.println(
            "      <column name=\"locale\" value=\"" + occupation.getLocaleId() + "\"/>");
        System.out.println(
            "      <column name=\"sort_index\" value=\"" + occupation.getSortIndex() + "\"/>");
        System.out.println("      <column name=\"name\" value=\"" + occupation.getName() + "\"/>");
        System.out.println(
            "      <column name=\"description\" value=\"" + occupation.getDescription() + "\"/>");
        System.out.println("    </insert>");
      }

      System.out.println();
    }

    if (createPermitTypeInserts) {

      for (Occupation occupation : referenceService.getOccupations("en-US")) {

        System.out.println("    <insert schemaName=\"reference\" tableName=\"occupations\">");
        System.out.println("      <column name=\"code\" value=\"" + occupation.getCode() + "\"/>");
        System.out.println(
            "      <column name=\"locale\" value=\"" + occupation.getLocaleId() + "\"/>");
        System.out.println(
            "      <column name=\"sort_index\" value=\"" + occupation.getSortIndex() + "\"/>");
        System.out.println("      <column name=\"name\" value=\"" + occupation.getName() + "\"/>");
        System.out.println(
            "      <column name=\"description\" value=\"" + occupation.getDescription() + "\"/>");
        System.out.println("    </insert>");
      }

      System.out.println();
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
  }

  /** Test the employment type reference functionality. */
  @Test
  public void employmentTypeTest() throws Exception {
    List<EmploymentType> retrievedEmploymentTypes = referenceService.getEmploymentTypes();

    assertEquals(
        "The correct number of employment types was not retrieved",
        22,
        retrievedEmploymentTypes.size());

    retrievedEmploymentTypes = referenceService.getEmploymentTypes("en-US");

    assertEquals(
        "The correct number of employment types was not retrieved",
        11,
        retrievedEmploymentTypes.size());
  }

  /** Test the gender reference functionality. */
  @Test
  public void genderTest() throws Exception {
    List<Gender> retrievedGenders = referenceService.getGenders();

    assertEquals("The correct number of genders was not retrieved", 10, retrievedGenders.size());

    retrievedGenders = referenceService.getGenders("en-US");

    assertEquals("The correct number of genders was not retrieved", 5, retrievedGenders.size());
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
  }

  /** Test the marriage type reference functionality. */
  @Test
  public void marriageTypeTest() throws Exception {
    List<MarriageType> retrievedMarriageTypes = referenceService.getMarriageTypes();

    assertEquals(
        "The correct number of marriage types was not retrieved", 8, retrievedMarriageTypes.size());

    retrievedMarriageTypes = referenceService.getMarriageTypes("en-US");

    assertEquals(
        "The correct number of marriage types was not retrieved", 4, retrievedMarriageTypes.size());
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
  }

  /** Test the occupation reference functionality. */
  @Test
  public void occupationTest() throws Exception {
    List<Occupation> retrievedOccupations = referenceService.getOccupations();

    assertEquals(
        "The correct number of occupations was not retrieved", 58, retrievedOccupations.size());

    retrievedOccupations = referenceService.getOccupations("en-US");

    assertEquals(
        "The correct number of occupations was not retrieved", 29, retrievedOccupations.size());
  }

  /** Test the permit type reference functionality. */
  @Test
  public void permitTypeTest() throws Exception {
    List<PermitType> retrievedPermitTypes = referenceService.getPermitTypes();

    assertEquals(
        "The correct number of permit types was not retrieved", 18, retrievedPermitTypes.size());

    retrievedPermitTypes = referenceService.getPermitTypes("en-US");

    assertEquals(
        "The correct number of permit types was not retrieved", 9, retrievedPermitTypes.size());
  }

  /** Test the race reference functionality. */
  @Test
  public void raceTest() throws Exception {
    List<Race> retrievedRaces = referenceService.getRaces();

    assertEquals("The correct number of races was not retrieved", 12, retrievedRaces.size());

    retrievedRaces = referenceService.getRaces("en-US");

    assertEquals("The correct number of races was not retrieved", 6, retrievedRaces.size());
  }

  /** Test the region reference functionality. */
  @Test
  public void regionTest() throws Exception {
    List<Region> retrievedRegions = referenceService.getRegions();

    assertEquals("The correct number of regions was not retrieved", 18, retrievedRegions.size());

    retrievedRegions = referenceService.getRegions("en-US");

    assertEquals("The correct number of regions was not retrieved", 9, retrievedRegions.size());
  }

  /** Test the residential status reference functionality. */
  @Test
  public void residentialStatusTest() throws Exception {
    List<ResidentialStatus> retrievedResidentialStatuses =
        referenceService.getResidentialStatuses();

    assertEquals(
        "The correct number of residential statuses was not retrieved",
        10,
        retrievedResidentialStatuses.size());

    retrievedResidentialStatuses = referenceService.getResidentialStatuses("en-US");

    assertEquals(
        "The correct number of residential statuses was not retrieved",
        5,
        retrievedResidentialStatuses.size());
  }

  /** Test the residential type reference functionality. */
  @Test
  public void residentialTypeTest() throws Exception {
    List<ResidentialType> retrievedResidentialTypes = referenceService.getResidentialTypes();

    assertEquals(
        "The correct number of residential types was not retrieved",
        14,
        retrievedResidentialTypes.size());

    retrievedResidentialTypes = referenceService.getResidentialTypes("en-US");

    assertEquals(
        "The correct number of residential types was not retrieved",
        7,
        retrievedResidentialTypes.size());
  }

  /** Test the sources of funds reference functionality. */
  @Test
  public void sourceOfFundsTest() throws Exception {
    List<SourceOfFunds> retrievedSourceOfFunds = referenceService.getSourcesOfFunds();

    assertEquals(
        "The correct number of sources of funds was not retrieved",
        38,
        retrievedSourceOfFunds.size());

    retrievedSourceOfFunds = referenceService.getSourcesOfFunds("en-US");

    assertEquals(
        "The correct number of sources of funds was not retrieved",
        19,
        retrievedSourceOfFunds.size());
  }

  /** Test the suitable time to contact reference functionality. */
  @Test
  public void suitableTimeToContactFundsTest() throws Exception {
    List<SuitableTimeToContact> retrievedSuitableTimesToContact =
        referenceService.getSuitableTimesToContact();

    assertEquals(
        "The correct number of suitable times to contact was not retrieved",
        10,
        retrievedSuitableTimesToContact.size());

    retrievedSuitableTimesToContact = referenceService.getSuitableTimesToContact("en-US");

    assertEquals(
        "The correct number of suitable times to contact was not retrieved",
        5,
        retrievedSuitableTimesToContact.size());
  }

  /** Test the tax number type reference functionality. */
  @Test
  public void taxNumberTypeTest() throws Exception {
    List<TaxNumberType> retrievedTaxNumberTypes = referenceService.getTaxNumberTypes();

    assertEquals(
        "The correct number of tax number types was not retrieved",
        14,
        retrievedTaxNumberTypes.size());

    retrievedTaxNumberTypes = referenceService.getTaxNumberTypes("en-US");

    assertEquals(
        "The correct number of tax number types was not retrieved",
        7,
        retrievedTaxNumberTypes.size());
  }

  /** Test the title reference functionality. */
  @Test
  public void titleTest() throws Exception {
    List<Title> retrievedTitles = referenceService.getTitles();

    assertEquals("The correct number of titles was not retrieved", 24, retrievedTitles.size());

    retrievedTitles = referenceService.getTitles("en-US");

    assertEquals("The correct number of titles was not retrieved", 12, retrievedTitles.size());
  }

  /** Test the verification method functionality. */
  @Test
  public void verificationMethodTest() throws Exception {
    List<VerificationMethod> retrievedVerificationMethods =
        referenceService.getVerificationMethods();

    assertEquals(
        "The correct number of verification methods was not retrieved",
        6,
        retrievedVerificationMethods.size());

    retrievedVerificationMethods = referenceService.getVerificationMethods("en-US");

    assertEquals(
        "The correct number of verification methods was not retrieved",
        3,
        retrievedVerificationMethods.size());
  }

  /** Test the verification status functionality. */
  @Test
  public void verificationStatusTest() throws Exception {
    List<VerificationStatus> retrievedVerificationStatuses =
        referenceService.getVerificationStatuses();

    assertEquals(
        "The correct number of verification statuses was not retrieved",
        10,
        retrievedVerificationStatuses.size());

    retrievedVerificationStatuses = referenceService.getVerificationStatuses("en-US");

    assertEquals(
        "The correct number of verification statuses was not retrieved",
        5,
        retrievedVerificationStatuses.size());
  }
}
