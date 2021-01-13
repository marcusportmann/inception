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

// ~--- non-JDK imports --------------------------------------------------------

import static org.junit.Assert.assertEquals;

import digital.inception.reference.ContactMechanismPurpose;
import digital.inception.reference.ContactMechanismType;
import digital.inception.reference.Country;
import digital.inception.reference.EmploymentStatus;
import digital.inception.reference.EmploymentType;
import digital.inception.reference.Gender;
import digital.inception.reference.IReferenceService;
import digital.inception.reference.IdentityDocumentType;
import digital.inception.reference.Language;
import digital.inception.reference.MaritalStatus;
import digital.inception.reference.MarriageType;
import digital.inception.reference.NextOfKinType;
import digital.inception.reference.Occupation;
import digital.inception.reference.PhysicalAddressPurpose;
import digital.inception.reference.PhysicalAddressType;
import digital.inception.reference.Race;
import digital.inception.reference.Region;
import digital.inception.reference.ResidencePermitType;
import digital.inception.reference.ResidencyStatus;
import digital.inception.reference.ResidentialType;
import digital.inception.reference.SourceOfFunds;
import digital.inception.reference.TaxNumberType;
import digital.inception.reference.Title;
import digital.inception.reference.VerificationMethod;
import digital.inception.reference.VerificationStatus;
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

  /** Test the contact mechanism purpose functionality. */
  @Test
  public void contactMechanismPurposesTest() throws Exception {
    List<ContactMechanismPurpose> retrievedContactMechanismPurposes =
        referenceService.getContactMechanismPurposes();

    retrievedContactMechanismPurposes = referenceService.getContactMechanismPurposes("en-US");
  }

  /** Test the contact mechanism type functionality. */
  @Test
  public void contactMechanismTypeTest() throws Exception {
    List<ContactMechanismType> retrievedContactMechanismTypes =
        referenceService.getContactMechanismTypes();

    retrievedContactMechanismTypes = referenceService.getContactMechanismTypes("en-US");
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

  /** Test the physical address purpose functionality. */
  @Test
  public void physicalAddressPurposesTest() throws Exception {
    List<PhysicalAddressPurpose> retrievedPhysicalAddressPurposes =
        referenceService.getPhysicalAddressPurposes();

    retrievedPhysicalAddressPurposes = referenceService.getPhysicalAddressPurposes("en-US");
  }

  /** Test the physical address type functionality. */
  @Test
  public void physicalAddressTypeTest() throws Exception {
    List<PhysicalAddressType> retrievedPhysicalAddressTypes =
        referenceService.getPhysicalAddressTypes();

    retrievedPhysicalAddressTypes = referenceService.getPhysicalAddressTypes("en-US");
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

  /** Test the residence permit type reference functionality. */
  @Test
  public void residencePermitTypeTest() throws Exception {
    List<ResidencePermitType> retrievedResidencePermitTypes =
        referenceService.getResidencePermitTypes();

    assertEquals(
        "The correct number of residence permit types was not retrieved",
        18,
        retrievedResidencePermitTypes.size());

    retrievedResidencePermitTypes = referenceService.getResidencePermitTypes("en-US");

    assertEquals(
        "The correct number of residence permit types was not retrieved",
        9,
        retrievedResidencePermitTypes.size());
  }

  /** Test the residency status reference functionality. */
  @Test
  public void residencyStatusTest() throws Exception {
    List<ResidencyStatus> retrievedResidencyStatuses = referenceService.getResidencyStatuses();

    assertEquals(
        "The correct number of residency statuses was not retrieved",
        10,
        retrievedResidencyStatuses.size());

    retrievedResidencyStatuses = referenceService.getResidencyStatuses("en-US");

    assertEquals(
        "The correct number of residency statuses was not retrieved",
        5,
        retrievedResidencyStatuses.size());
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
