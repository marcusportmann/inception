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

package digital.inception.banking.customer.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import com.github.f4b6a3.uuid.UuidCreator;
import digital.inception.banking.customer.ICustomerService;
import digital.inception.banking.customer.IndividualCustomer;
import digital.inception.banking.customer.IndividualCustomerSortBy;
import digital.inception.banking.customer.IndividualCustomers;
import digital.inception.core.sorting.SortDirection;
import digital.inception.party.ContactMechanism;
import digital.inception.party.ContactMechanismType;
import digital.inception.party.IdentityDocument;
import digital.inception.party.PartyAttribute;
import digital.inception.party.PhysicalAddress;
import digital.inception.party.PhysicalAddressPurpose;
import digital.inception.party.PhysicalAddressType;
import digital.inception.party.PartyPreference;
import digital.inception.party.TaxNumber;
import digital.inception.test.TestClassRunner;
import digital.inception.test.TestConfiguration;
import java.time.LocalDate;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;
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
 * The <b>CustomerServiceTest</b> class contains the implementation of the JUnit tests for the
 * <b>CustomerService</b> class.
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
public class CustomerServiceTest {

  private static int individualCustomerCount;

  /** The Customer Service. */
  @Autowired private ICustomerService customerService;

  /** The JSR-303 validator. */
  @Autowired private Validator validator;

  private static synchronized IndividualCustomer getTestCompleteIndividualCustomerDetails() {
    individualCustomerCount++;

    IndividualCustomer individualCustomer = new IndividualCustomer();

    individualCustomer.setCountryOfBirth("US");
    individualCustomer.setCountryOfResidence("ZA");
    individualCustomer.setDateOfBirth(LocalDate.of(1976, 3, 7));
    individualCustomer.setEmploymentStatus("other");
    individualCustomer.setEmploymentType("unemployed");
    individualCustomer.setGender("female");
    individualCustomer.setGivenName("GivenName" + individualCustomerCount);
    individualCustomer.setHomeLanguage("EN");
    individualCustomer.setId(UuidCreator.getShortPrefixComb());
    individualCustomer.setInitials("G M");
    individualCustomer.setMaidenName("MaidenName" + individualCustomerCount);
    individualCustomer.setMaritalStatus("married");
    individualCustomer.setMarriageType("in_community_of_property");
    individualCustomer.setMiddleNames("MiddleName" + individualCustomerCount);
    individualCustomer.setName(
        "GivenName"
            + individualCustomerCount
            + " "
            + "MiddleName"
            + individualCustomerCount
            + " "
            + "Surname"
            + individualCustomerCount);
    individualCustomer.setOccupation("professional_business");
    individualCustomer.setPreferredName("PreferredName" + individualCustomerCount);
    individualCustomer.setRace("white");
    individualCustomer.setResidencyStatus("citizen");
    individualCustomer.setResidentialType("owner");
    individualCustomer.setSurname("Surname" + individualCustomerCount);
    individualCustomer.setTenantId(UUID.fromString("00000000-0000-0000-0000-000000000000"));
    individualCustomer.setTitle("mrs");

    individualCustomer.addIdentityDocument(
        new IdentityDocument("za_id_card", "ZA", LocalDate.of(2012, 5, 1), "8904085800089"));

    individualCustomer.setCountryOfTaxResidence("ZA");
    individualCustomer.addTaxNumber(new TaxNumber("za_income_tax_number", "ZA", "123456789"));

    individualCustomer.addContactMechanism(
        new ContactMechanism(
            ContactMechanismType.MOBILE_NUMBER, "personal_mobile_number", "+27835551234"));
    individualCustomer.addContactMechanism(
        new ContactMechanism(
            ContactMechanismType.EMAIL_ADDRESS, "personal_email_address", "test@test.com"));

    PhysicalAddress residentialAddress =
        new PhysicalAddress(PhysicalAddressType.STREET, PhysicalAddressPurpose.RESIDENTIAL);
    residentialAddress.setStreetNumber("1");
    residentialAddress.setStreetName("Discovery Place");
    residentialAddress.setSuburb("Sandhurst");
    residentialAddress.setCity("Sandton");
    residentialAddress.setRegion("GP");
    residentialAddress.setCountry("ZA");
    residentialAddress.setPostalCode("2194");

    individualCustomer.addPhysicalAddress(residentialAddress);

    PhysicalAddress correspondenceAddress =
        new PhysicalAddress(
            PhysicalAddressType.UNSTRUCTURED, PhysicalAddressPurpose.CORRESPONDENCE);

    correspondenceAddress.setLine1("1 Apple Park Way");
    correspondenceAddress.setCity("Cupertino");
    correspondenceAddress.setRegion("CA");
    correspondenceAddress.setCountry("US");
    correspondenceAddress.setPostalCode("CA 95014");

    individualCustomer.addPhysicalAddress(correspondenceAddress);

    individualCustomer.addPreference(new PartyPreference("correspondence_language", "EN"));

    individualCustomer.addAttribute(new PartyAttribute("weight", "80kg"));

    return individualCustomer;
  }

  /** Test the individual customer functionality. */
  @Test
  public void individualCustomerTest() throws Exception {
    IndividualCustomer individualCustomer = getTestCompleteIndividualCustomerDetails();

    customerService.createIndividualCustomer(individualCustomer);

    IndividualCustomer retrievedIndividualCustomer =
        customerService.getIndividualCustomer(individualCustomer.getId());

    compareIndividualCustomers(individualCustomer, retrievedIndividualCustomer);

    individualCustomer.setCountryOfBirth("GB");
    individualCustomer.setCountryOfResidence("ZA");
    individualCustomer.setCountryOfTaxResidence("ZA");
    individualCustomer.setDateOfBirth(LocalDate.of(1985, 5, 1));
    individualCustomer.setDateOfDeath(LocalDate.of(2200, 1, 1));
    individualCustomer.setEmploymentStatus("employed");
    individualCustomer.setEmploymentType("full_time");
    individualCustomer.setGender("female");
    individualCustomer.setGivenName(individualCustomer.getGivenName() + " Updated");
    individualCustomer.setHomeLanguage("AF");
    individualCustomer.setInitials(individualCustomer.getInitials() + " Updated");
    individualCustomer.setMaidenName(individualCustomer.getMaidenName() + " Updated");
    individualCustomer.setMaritalStatus("divorced");
    individualCustomer.setMarriageType(null);
    individualCustomer.setMiddleNames(individualCustomer.getMiddleNames() + " Updated");
    individualCustomer.setOccupation("professional_business");
    individualCustomer.setName(individualCustomer.getName() + " Updated");
    individualCustomer.setPreferredName(individualCustomer.getPreferredName() + " Updated");
    individualCustomer.setRace("black");
    individualCustomer.setResidencyStatus("citizen");
    individualCustomer.setResidentialType("owner");
    individualCustomer.setSurname(individualCustomer.getSurname() + " Updated");
    individualCustomer.setTitle("ms");

    individualCustomer.setCountryOfTaxResidence("GB");
    individualCustomer.addTaxNumber(new TaxNumber("uk_tax_number", "GB", "987654321"));

    individualCustomer.removeTaxNumber("za_income_tax_number");

    individualCustomer.addIdentityDocument(
        new IdentityDocument(
            "passport", "ZA", LocalDate.of(2016, 10, 7), LocalDate.of(2025, 9, 1), "A1234567890"));

    individualCustomer.removeIdentityDocumentByType("za_id_card");

    individualCustomer.removeContactMechanism("fax_number", "main_fax_number");

    individualCustomer
        .getContactMechanism(ContactMechanismType.EMAIL_ADDRESS, "personal_email_address")
        .setValue("test.updated@test.com");

    individualCustomer.addContactMechanism(
        new ContactMechanism("phone_number", "home_phone_number", "0115551234"));

    individualCustomer.removePreference("correspondence_language");

    individualCustomer.addPreference(new PartyPreference("time_to_contact", "anytime"));

    individualCustomer.removeAttribute("weight");

    individualCustomer.addAttribute(new PartyAttribute("height", "180cm"));

    customerService.updateIndividualCustomer(individualCustomer);

    IndividualCustomers filteredIndividualCustomers =
        customerService.getIndividualCustomers(
            "", IndividualCustomerSortBy.NAME, SortDirection.ASCENDING, 0, 100);

    assertEquals(
        "The correct number of filtered individual customers was not retrieved",
        1,
        filteredIndividualCustomers.getIndividualCustomers().size());

    compareIndividualCustomers(
        individualCustomer, filteredIndividualCustomers.getIndividualCustomers().get(0));

    filteredIndividualCustomers =
        customerService.getIndividualCustomers(
            "Updated", IndividualCustomerSortBy.NAME, SortDirection.ASCENDING, 0, 100);

    assertEquals(
        "The correct number of filtered individual customers was not retrieved",
        1,
        filteredIndividualCustomers.getIndividualCustomers().size());

    compareIndividualCustomers(
        individualCustomer, filteredIndividualCustomers.getIndividualCustomers().get(0));

    customerService.deleteIndividualCustomer(individualCustomer.getId());
  }

  /** Test the individual customer validation functionality. */
  @Test
  public void validateIndividualCustomerTest() throws Exception {
    IndividualCustomer individualCustomer = getTestCompleteIndividualCustomerDetails();

    Set<ConstraintViolation<IndividualCustomer>> constraintViolations =
        customerService.validateIndividualCustomer(individualCustomer);
  }

  private void compareAttributes(PartyAttribute attribute1, PartyAttribute attribute2) {
    assertEquals(
        "The type values for the two party attributes do not match",
        attribute1.getType(),
        attribute2.getType());
    assertEquals(
        "The string value values for the two party attributes do not match",
        attribute1.getStringValue(),
        attribute2.getStringValue());
  }

  private void compareIndividualCustomers(
      IndividualCustomer individualCustomer1, IndividualCustomer individualCustomer2) {
    assertEquals(
        "The country of birth values for the two individual customers do not match",
        individualCustomer1.getCountryOfBirth(),
        individualCustomer2.getCountryOfBirth());
    assertEquals(
        "The country of residence values for the two individual customers do not match",
        individualCustomer1.getCountryOfResidence(),
        individualCustomer2.getCountryOfResidence());
    assertEquals(
        "The countries of tax residence values for the two individual customers do not match",
        individualCustomer1.getCountriesOfTaxResidence(),
        individualCustomer2.getCountriesOfTaxResidence());
    assertEquals(
        "The date of birth values for the two individual customers do not match",
        individualCustomer1.getDateOfBirth(),
        individualCustomer2.getDateOfBirth());
    assertEquals(
        "The date of death values for the two individual customers do not match",
        individualCustomer1.getDateOfDeath(),
        individualCustomer2.getDateOfDeath());
    assertEquals(
        "The emancipated minor values for the two individual customers do not match",
        individualCustomer1.getEmancipatedMinor(),
        individualCustomer2.getEmancipatedMinor());
    assertEquals(
        "The employment status values for the two individual customers do not match",
        individualCustomer1.getEmploymentStatus(),
        individualCustomer2.getEmploymentStatus());
    assertEquals(
        "The employment type values for the two individual customers do not match",
        individualCustomer1.getEmploymentType(),
        individualCustomer2.getEmploymentType());
    assertEquals(
        "The gender values for the two individual customers do not match",
        individualCustomer1.getGender(),
        individualCustomer2.getGender());
    assertEquals(
        "The given name values for the two individual customers do not match",
        individualCustomer1.getGivenName(),
        individualCustomer2.getGivenName());
    assertEquals(
        "The home language values for the two individual customers do not match",
        individualCustomer1.getHomeLanguage(),
        individualCustomer2.getHomeLanguage());
    assertEquals(
        "The ID values for the two individual customers do not match",
        individualCustomer1.getId(),
        individualCustomer2.getId());
    assertEquals(
        "The initials values for the two individual customers do not match",
        individualCustomer1.getInitials(),
        individualCustomer2.getInitials());
    assertEquals(
        "The maiden name values for the two individual customers do not match",
        individualCustomer1.getMaidenName(),
        individualCustomer2.getMaidenName());
    assertEquals(
        "The marital status values for the two individual customers do not match",
        individualCustomer1.getMaritalStatus(),
        individualCustomer2.getMaritalStatus());
    assertEquals(
        "The marriage type values for the two individual customers do not match",
        individualCustomer1.getMarriageType(),
        individualCustomer2.getMarriageType());
    assertEquals(
        "The middle names values for the two individual customers do not match",
        individualCustomer1.getMiddleNames(),
        individualCustomer2.getMiddleNames());
    assertEquals(
        "The name values for the two individual customers do not match",
        individualCustomer1.getName(),
        individualCustomer2.getName());
    assertEquals(
        "The occupation values for the two individual customers do not match",
        individualCustomer1.getOccupation(),
        individualCustomer2.getOccupation());
    assertEquals(
        "The preferred name values for the two individual customers do not match",
        individualCustomer1.getPreferredName(),
        individualCustomer2.getPreferredName());
    assertEquals(
        "The race values for the two individual customers do not match",
        individualCustomer1.getRace(),
        individualCustomer2.getRace());
    assertEquals(
        "The residency status values for the two individual customers do not match",
        individualCustomer1.getResidencyStatus(),
        individualCustomer2.getResidencyStatus());
    assertEquals(
        "The residential type values for the two individual customers do not match",
        individualCustomer1.getResidentialType(),
        individualCustomer2.getResidentialType());
    assertEquals(
        "The surname values for the two individual customers do not match",
        individualCustomer1.getSurname(),
        individualCustomer2.getSurname());
    assertEquals(
        "The tenant ID values for the two individual customers do not match",
        individualCustomer1.getTenantId(),
        individualCustomer2.getTenantId());
    assertEquals(
        "The title values for the two individual customers do not match",
        individualCustomer1.getTitle(),
        individualCustomer2.getTitle());

    assertEquals(
        "The number of identity documents for the two individual customers do not match",
        individualCustomer1.getIdentityDocuments().size(),
        individualCustomer2.getIdentityDocuments().size());

    for (IdentityDocument individualCustomer1IdentityDocument :
        individualCustomer1.getIdentityDocuments()) {
      boolean foundIdentityDocument = false;

      for (IdentityDocument individualCustomer2IdentityDocument :
          individualCustomer2.getIdentityDocuments()) {
        if (individualCustomer1IdentityDocument
                .getType()
                .equals(individualCustomer2IdentityDocument.getType())
            && individualCustomer1IdentityDocument
                .getCountryOfIssue()
                .equals(individualCustomer2IdentityDocument.getCountryOfIssue())
            && individualCustomer1IdentityDocument
                .getDateOfIssue()
                .equals(individualCustomer2IdentityDocument.getDateOfIssue())) {

          assertEquals(
              "The date of expiry for the two identity documents do not match",
              individualCustomer1IdentityDocument.getDateOfExpiry(),
              individualCustomer2IdentityDocument.getDateOfExpiry());
          assertEquals(
              "The numbers for the two identity documents do not match",
              individualCustomer1IdentityDocument.getNumber(),
              individualCustomer2IdentityDocument.getNumber());

          foundIdentityDocument = true;
        }
      }

      if (!foundIdentityDocument) {
        fail(
            "Failed to find the identity document ("
                + individualCustomer1IdentityDocument.getType()
                + ")("
                + individualCustomer1IdentityDocument.getCountryOfIssue()
                + ")("
                + individualCustomer1IdentityDocument.getDateOfIssue()
                + ")");
      }
    }

    assertEquals(
        "The number of tax numbers for the two individual customers do not match",
        individualCustomer1.getTaxNumbers().size(),
        individualCustomer2.getTaxNumbers().size());

    for (TaxNumber individualCustomer1TaxNumber : individualCustomer1.getTaxNumbers()) {
      boolean foundTaxNumber = false;

      for (TaxNumber individualCustomer2TaxNumber : individualCustomer2.getTaxNumbers()) {
        if (individualCustomer1TaxNumber.getType().equals(individualCustomer2TaxNumber.getType())) {

          assertEquals(
              "The country of issue for the two tax numbers do not match",
              individualCustomer1TaxNumber.getCountryOfIssue(),
              individualCustomer2TaxNumber.getCountryOfIssue());
          assertEquals(
              "The numbers for the two tax numbers do not match",
              individualCustomer1TaxNumber.getNumber(),
              individualCustomer2TaxNumber.getNumber());

          foundTaxNumber = true;
        }
      }

      if (!foundTaxNumber) {
        fail("Failed to find the tax number (" + individualCustomer1TaxNumber.getType() + ")");
      }
    }

    assertEquals(
        "The number of attributes for the two individual customers do not match",
        individualCustomer1.getAttributes().size(),
        individualCustomer2.getAttributes().size());

    for (PartyAttribute individualCustomer1Attribute : individualCustomer1.getAttributes()) {
      boolean foundAttribute = false;

      for (PartyAttribute individualCustomer2Attribute : individualCustomer2.getAttributes()) {

        if (Objects.equals(
                individualCustomer1Attribute.getParty(), individualCustomer2Attribute.getParty())
            && Objects.equals(
                individualCustomer1Attribute.getType(), individualCustomer2Attribute.getType())) {

          compareAttributes(individualCustomer1Attribute, individualCustomer2Attribute);

          foundAttribute = true;
        }
      }

      if (!foundAttribute) {
        fail("Failed to find the attribute (" + individualCustomer1Attribute.getType() + ")");
      }
    }

    assertEquals(
        "The number of contact mechanisms for the two individual customers do not match",
        individualCustomer1.getContactMechanisms().size(),
        individualCustomer2.getContactMechanisms().size());

    for (ContactMechanism individualCustomer1ContactMechanism :
        individualCustomer1.getContactMechanisms()) {
      boolean foundContactMechanism = false;

      for (ContactMechanism individualCustomer2ContactMechanism :
          individualCustomer2.getContactMechanisms()) {

        if (Objects.equals(
                individualCustomer1ContactMechanism.getParty(),
                individualCustomer2ContactMechanism.getParty())
            && Objects.equals(
                individualCustomer1ContactMechanism.getType(),
                individualCustomer2ContactMechanism.getType())
            && Objects.equals(
                individualCustomer1ContactMechanism.getPurpose(),
                individualCustomer2ContactMechanism.getPurpose())) {
          assertEquals(
              "The values for the two contact mechanisms do not match",
              individualCustomer1ContactMechanism.getValue(),
              individualCustomer2ContactMechanism.getValue());

          foundContactMechanism = true;
        }
      }

      if (!foundContactMechanism) {
        fail(
            "Failed to find the contact mechanism ("
                + individualCustomer1ContactMechanism.getType()
                + ")("
                + individualCustomer1ContactMechanism.getPurpose()
                + ")");
      }
    }

    assertEquals(
        "The number of physical addresses for the two individual customers do not match",
        individualCustomer1.getPhysicalAddresses().size(),
        individualCustomer2.getPhysicalAddresses().size());

    for (PhysicalAddress individualCustomer1PhysicalAddress :
        individualCustomer1.getPhysicalAddresses()) {
      boolean foundPhysicalAddress = false;

      for (PhysicalAddress individualCustomer2PhysicalAddress :
          individualCustomer2.getPhysicalAddresses()) {

        if (Objects.equals(
                individualCustomer1PhysicalAddress.getParty(),
                individualCustomer2PhysicalAddress.getParty())
            && Objects.equals(
                individualCustomer1PhysicalAddress.getId(),
                individualCustomer2PhysicalAddress.getId())) {

          comparePartyPhysicalAddresses(
              individualCustomer1PhysicalAddress, individualCustomer2PhysicalAddress);

          foundPhysicalAddress = true;
        }
      }

      if (!foundPhysicalAddress) {
        fail(
            "Failed to find the physical address ("
                + individualCustomer1PhysicalAddress.getId()
                + ")");
      }
    }

    assertEquals(
        "The number of preferences for the two individual customers do not match",
        individualCustomer1.getPreferences().size(),
        individualCustomer2.getPreferences().size());

    for (PartyPreference individualCustomer1Preference : individualCustomer1.getPreferences()) {
      boolean foundPreference = false;

      for (PartyPreference individualCustomer2Preference : individualCustomer2.getPreferences()) {

        if (Objects.equals(
                individualCustomer1Preference.getParty(), individualCustomer2Preference.getParty())
            && Objects.equals(
                individualCustomer1Preference.getType(), individualCustomer2Preference.getType())) {

          comparePreferences(individualCustomer1Preference, individualCustomer2Preference);

          foundPreference = true;
        }
      }

      if (!foundPreference) {
        fail("Failed to find the preference (" + individualCustomer1Preference.getType() + ")");
      }
    }
  }

  private void comparePartyPhysicalAddresses(
      PhysicalAddress partyPhysicalAddress1, PhysicalAddress partyPhysicalAddress2) {
    assertEquals(
        "The building floor values for the two party physical addresses do not match",
        partyPhysicalAddress1.getBuildingFloor(),
        partyPhysicalAddress2.getBuildingFloor());
    assertEquals(
        "The building name values for the two party physical addresses do not match",
        partyPhysicalAddress1.getBuildingName(),
        partyPhysicalAddress2.getBuildingName());
    assertEquals(
        "The building room values for the two party physical addresses do not match",
        partyPhysicalAddress1.getBuildingRoom(),
        partyPhysicalAddress2.getBuildingRoom());
    assertEquals(
        "The city values for the two party physical addresses do not match",
        partyPhysicalAddress1.getCity(),
        partyPhysicalAddress2.getCity());
    assertEquals(
        "The complex name values for the two party physical addresses do not match",
        partyPhysicalAddress1.getComplexName(),
        partyPhysicalAddress2.getComplexName());
    assertEquals(
        "The complex unit number values for the two party physical addresses do not match",
        partyPhysicalAddress1.getComplexUnitNumber(),
        partyPhysicalAddress2.getComplexUnitNumber());
    assertEquals(
        "The farm description values for the two party physical addresses do not match",
        partyPhysicalAddress1.getFarmDescription(),
        partyPhysicalAddress2.getFarmDescription());
    assertEquals(
        "The farm name values for the two party physical addresses do not match",
        partyPhysicalAddress1.getFarmName(),
        partyPhysicalAddress2.getFarmName());
    assertEquals(
        "The farm number values for the two party physical addresses do not match",
        partyPhysicalAddress1.getFarmNumber(),
        partyPhysicalAddress2.getFarmNumber());
    assertEquals(
        "The line 1 values for the two party physical addresses do not match",
        partyPhysicalAddress1.getLine1(),
        partyPhysicalAddress2.getLine1());
    assertEquals(
        "The line 2 values for the two party physical addresses do not match",
        partyPhysicalAddress1.getLine2(),
        partyPhysicalAddress2.getLine2());
    assertEquals(
        "The line 3 values for the two party physical addresses do not match",
        partyPhysicalAddress1.getLine3(),
        partyPhysicalAddress2.getLine3());
    assertEquals(
        "The purpose values for the two party physical addresses do not match",
        partyPhysicalAddress1.getPurposes(),
        partyPhysicalAddress2.getPurposes());
    assertEquals(
        "The region values for the two party physical addresses do not match",
        partyPhysicalAddress1.getRegion(),
        partyPhysicalAddress2.getRegion());
    assertEquals(
        "The site block values for the two party physical addresses do not match",
        partyPhysicalAddress1.getSiteBlock(),
        partyPhysicalAddress2.getSiteBlock());
    assertEquals(
        "The site number values for the two party physical addresses do not match",
        partyPhysicalAddress1.getSiteNumber(),
        partyPhysicalAddress2.getSiteNumber());
    assertEquals(
        "The street name values for the two party physical addresses do not match",
        partyPhysicalAddress1.getStreetName(),
        partyPhysicalAddress2.getStreetName());
    assertEquals(
        "The street number values for the two party physical addresses do not match",
        partyPhysicalAddress1.getStreetNumber(),
        partyPhysicalAddress2.getStreetNumber());
    assertEquals(
        "The suburb values for the two party physical addresses do not match",
        partyPhysicalAddress1.getSuburb(),
        partyPhysicalAddress2.getSuburb());
    assertEquals(
        "The type values for the two party physical addresses do not match",
        partyPhysicalAddress1.getType(),
        partyPhysicalAddress2.getType());
  }

  private void comparePreferences(PartyPreference preference1, PartyPreference preference2) {
    assertEquals(
        "The type values for the two party preferences do not match",
        preference1.getType(),
        preference2.getType());
    assertEquals(
        "The value values for the two party preferences do not match",
        preference1.getValue(),
        preference2.getValue());
  }
}
