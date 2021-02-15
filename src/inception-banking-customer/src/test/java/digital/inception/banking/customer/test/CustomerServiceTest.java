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
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import com.github.f4b6a3.uuid.UuidCreator;
import digital.inception.banking.customer.BusinessCustomer;
import digital.inception.banking.customer.BusinessCustomerSortBy;
import digital.inception.banking.customer.BusinessCustomers;
import digital.inception.banking.customer.ICustomerService;
import digital.inception.banking.customer.IndividualCustomer;
import digital.inception.banking.customer.IndividualCustomerSortBy;
import digital.inception.banking.customer.IndividualCustomers;
import digital.inception.core.sorting.SortDirection;
import digital.inception.party.ContactMechanism;
import digital.inception.party.ContactMechanismType;
import digital.inception.party.IdentityDocument;
import digital.inception.party.PartyAttribute;
import digital.inception.party.PartyRole;
import digital.inception.party.PhysicalAddress;
import digital.inception.party.PhysicalAddressPurpose;
import digital.inception.party.PhysicalAddressRole;
import digital.inception.party.PhysicalAddressType;
import digital.inception.party.Preference;
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

  private static int businessCustomerCount;

  /**
   * The Customer Service.
   */
  @Autowired
  private ICustomerService customerService;

  /**
   * The JSR-303 validator.
   */
  @Autowired
  private Validator validator;

  private static synchronized BusinessCustomer getTestBusinessCustomerDetails() {
    businessCustomerCount++;

    BusinessCustomer businessCustomer =
        new BusinessCustomer(
            UUID.fromString("00000000-0000-0000-0000-000000000000"));

    businessCustomer.setName("Organization Name " + businessCustomerCount);

    businessCustomer.addIdentityDocument(
        new IdentityDocument("za_company_registration", "ZA", LocalDate.of(2006, 4, 2), "2006/123456/23"));

    businessCustomer.addRole(new PartyRole("employer"));

    return businessCustomer;
  }

  private static synchronized IndividualCustomer getTestIndividualCustomerDetails() {
    individualCustomerCount++;

    IndividualCustomer individualCustomer = new IndividualCustomer(UUID.fromString("00000000-0000-0000-0000-000000000000"));

    individualCustomer.setCountryOfBirth("US");
    individualCustomer.setCountryOfResidence("ZA");
    individualCustomer.setCountryOfTaxResidence("ZA");
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

    individualCustomer.addAttribute(new PartyAttribute("weight", "80kg"));

    individualCustomer.addContactMechanism(
        new ContactMechanism(
            ContactMechanismType.MOBILE_NUMBER, "personal_mobile_number", "+27835551234"));
    individualCustomer.addContactMechanism(
        new ContactMechanism(
            ContactMechanismType.EMAIL_ADDRESS, "personal_email_address", "test@test.com"));

    individualCustomer.addIdentityDocument(
        new IdentityDocument("za_id_card", "ZA", LocalDate.of(2012, 5, 1), "8904085800089"));

    PhysicalAddress residentialAddress =
        new PhysicalAddress(PhysicalAddressType.STREET, PhysicalAddressRole.RESIDENTIAL, Set.of(new String[] {PhysicalAddressPurpose.CORRESPONDENCE, PhysicalAddressPurpose.BILLING}));
    residentialAddress.setStreetNumber("13");
    residentialAddress.setStreetName("Kraalbessie Avenue");
    residentialAddress.setSuburb("Weltevreden Park");
    residentialAddress.setCity("Johannesburg");
    residentialAddress.setRegion("GP");
    residentialAddress.setCountry("ZA");
    residentialAddress.setPostalCode("1709");

    individualCustomer.addPhysicalAddress(residentialAddress);

    PhysicalAddress correspondenceAddress =
        new PhysicalAddress(
            PhysicalAddressType.UNSTRUCTURED, PhysicalAddressRole.WORK);

    correspondenceAddress.setLine1("15 Troy Street");
    correspondenceAddress.setCity("Johannesburg");
    correspondenceAddress.setRegion("GP");
    correspondenceAddress.setCountry("ZA");
    correspondenceAddress.setPostalCode("2000");

    individualCustomer.addPhysicalAddress(correspondenceAddress);

    individualCustomer.addPreference(new Preference("correspondence_language", "EN"));

    individualCustomer.addRole(new PartyRole("employee"));

    individualCustomer.addTaxNumber(new TaxNumber("za_income_tax_number", "ZA", "123456789"));

    return individualCustomer;
  }

  /**
   * Test the individual customer functionality.
   */
  @Test
  public void individualCustomerTest() throws Exception {
    IndividualCustomer individualCustomer = getTestIndividualCustomerDetails();

    customerService.createIndividualCustomer(individualCustomer);

    IndividualCustomer retrievedIndividualCustomer =
        customerService.getIndividualCustomer(individualCustomer.getId());

    compareIndividualCustomers(individualCustomer, retrievedIndividualCustomer);

    individualCustomer.setCountryOfBirth("GB");
    individualCustomer.setCountryOfResidence("ZA");
    individualCustomer.setCountryOfTaxResidence("GB");
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

    individualCustomer.removeAttribute("weight");

    individualCustomer.addAttribute(new PartyAttribute("height", "180cm"));

    individualCustomer.removeContactMechanism("fax_number", "main_fax_number");

    individualCustomer
        .getContactMechanism(ContactMechanismType.EMAIL_ADDRESS, "personal_email_address")
        .setValue("test.updated@test.com");

    individualCustomer.addContactMechanism(
        new ContactMechanism("phone_number", "home_phone_number", "0115551234"));

    individualCustomer.addIdentityDocument(
        new IdentityDocument(
            "passport", "ZA", LocalDate.of(2016, 10, 7), LocalDate.of(2025, 9, 1), "A1234567890"));

    individualCustomer.removeIdentityDocumentByType("za_id_card");

    individualCustomer.removePreference("correspondence_language");

    individualCustomer.addPreference(new Preference("time_to_contact", "anytime"));

    individualCustomer.addTaxNumber(new TaxNumber("uk_tax_number", "GB", "987654321"));

    individualCustomer.removeTaxNumber("za_income_tax_number");

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





  /** Test the business customer functionality. */
  @Test
  public void businessCustomerTest() throws Exception {
    BusinessCustomer businessCustomer = getTestBusinessCustomerDetails();

    customerService.createBusinessCustomer(businessCustomer);

    BusinessCustomer retrievedBusinessCustomer =
        customerService.getBusinessCustomer(businessCustomer.getId());

    compareBusinessCustomers(businessCustomer, retrievedBusinessCustomer);

    businessCustomer.setName(businessCustomer.getName() + " Updated");

    businessCustomer.setCountriesOfTaxResidence(Set.of("GB", "ZA"));

    businessCustomer.addContactMechanism(
        new ContactMechanism("phone_number", "main_phone_number", "0115551234"));

    PhysicalAddress mainAddress =
        new PhysicalAddress(PhysicalAddressType.STREET, PhysicalAddressRole.MAIN);
    mainAddress.setStreetNumber("1");
    mainAddress.setStreetName("Discovery Place");
    mainAddress.setSuburb("Sandhurst");
    mainAddress.setCity("Sandton");
    mainAddress.setRegion("GP");
    mainAddress.setCountry("ZA");
    mainAddress.setPostalCode("2194");

    businessCustomer.addPhysicalAddress(mainAddress);

    businessCustomer.addPreference(new Preference("correspondence_language", "EN"));

    businessCustomer.removeRole("employer");

    businessCustomer.addRole(new PartyRole("supplier"));

    customerService.updateBusinessCustomer(businessCustomer);

    BusinessCustomers filteredBusinessCustomers =
        customerService.getBusinessCustomers(
            "", BusinessCustomerSortBy.NAME, SortDirection.ASCENDING, 0, 100);

    assertEquals(
        "The correct number of filtered business customers was not retrieved",
        1,
        filteredBusinessCustomers.getBusinessCustomers().size());

    compareBusinessCustomers(
        businessCustomer, filteredBusinessCustomers.getBusinessCustomers().get(0));

    filteredBusinessCustomers =
        customerService.getBusinessCustomers(
            "Updated", BusinessCustomerSortBy.NAME, SortDirection.ASCENDING, 0, 100);

    assertEquals(
        "The correct number of filtered business customers was not retrieved",
        1,
        filteredBusinessCustomers.getBusinessCustomers().size());

    compareBusinessCustomers(
        businessCustomer, filteredBusinessCustomers.getBusinessCustomers().get(0));

    customerService.deleteBusinessCustomer(businessCustomer.getId());
  }


  private void compareBusinessCustomers(BusinessCustomer businessCustomer1, BusinessCustomer businessCustomer2) {
    assertEquals(
        "The countries of tax residence values for the two business customers do not match",
        businessCustomer1.getCountriesOfTaxResidence(),
        businessCustomer2.getCountriesOfTaxResidence());
    assertEquals(
        "The ID values for the two business customers do not match",
        businessCustomer1.getId(),
        businessCustomer2.getId());
    assertEquals(
        "The name values for the two business customers do not match",
        businessCustomer1.getName(),
        businessCustomer2.getName());
    assertEquals(
        "The tenant ID values for the two business customers do not match",
        businessCustomer1.getTenantId(),
        businessCustomer2.getTenantId());

    assertEquals(
        "The number of attributes for the two business customers do not match",
        businessCustomer1.getAttributes().size(),
        businessCustomer2.getAttributes().size());

    for (PartyAttribute businessCustomer1Attribute : businessCustomer1.getAttributes()) {
      boolean foundAttribute = false;

      for (PartyAttribute businessCustomer2Attribute : businessCustomer2.getAttributes()) {

        if (Objects.equals(businessCustomer1Attribute.getParty(), businessCustomer2Attribute.getParty())
            && Objects.equals(businessCustomer1Attribute.getType(), businessCustomer2Attribute.getType())) {

          compareAttributes(businessCustomer1Attribute, businessCustomer2Attribute);

          foundAttribute = true;
        }
      }

      if (!foundAttribute) {
        fail("Failed to find the attribute (" + businessCustomer1Attribute.getType() + ")");
      }
    }

    assertEquals(
        "The number of contact mechanisms for the two business customers do not match",
        businessCustomer1.getContactMechanisms().size(),
        businessCustomer2.getContactMechanisms().size());

    for (ContactMechanism person1ContactMechanism : businessCustomer1.getContactMechanisms()) {
      boolean foundContactMechanism = false;

      for (ContactMechanism person2ContactMechanism : businessCustomer2.getContactMechanisms()) {

        if (Objects.equals(person1ContactMechanism.getParty(), person2ContactMechanism.getParty())
            && Objects.equals(person1ContactMechanism.getType(), person2ContactMechanism.getType())
            && Objects.equals(
            person1ContactMechanism.getPurpose(), person2ContactMechanism.getPurpose())) {
          assertEquals(
              "The values for the two contact mechanisms do not match",
              person1ContactMechanism.getValue(),
              person2ContactMechanism.getValue());

          foundContactMechanism = true;
        }
      }

      if (!foundContactMechanism) {
        fail(
            "Failed to find the contact mechanism ("
                + person1ContactMechanism.getType()
                + ")("
                + person1ContactMechanism.getPurpose()
                + ")");
      }
    }

    assertEquals(
        "The number of physical addresses for the two business customers do not match",
        businessCustomer1.getPhysicalAddresses().size(),
        businessCustomer2.getPhysicalAddresses().size());

    for (PhysicalAddress person1PhysicalAddress : businessCustomer1.getPhysicalAddresses()) {
      boolean foundPhysicalAddress = false;

      for (PhysicalAddress person2PhysicalAddress : businessCustomer2.getPhysicalAddresses()) {

        if (Objects.equals(person1PhysicalAddress.getParty(), person2PhysicalAddress.getParty())
            && Objects.equals(person1PhysicalAddress.getId(), person2PhysicalAddress.getId())) {

          comparePhysicalAddresses(person1PhysicalAddress, person2PhysicalAddress);

          foundPhysicalAddress = true;
        }
      }

      if (!foundPhysicalAddress) {
        fail("Failed to find the physical address (" + person1PhysicalAddress.getId() + ")");
      }
    }

    assertEquals(
        "The number of preferences for the two business customers do not match",
        businessCustomer1.getPreferences().size(),
        businessCustomer2.getPreferences().size());

    for (Preference businessCustomer1Preference : businessCustomer1.getPreferences()) {
      boolean foundPreference = false;

      for (Preference businessCustomer2Preference : businessCustomer2.getPreferences()) {

        if (Objects.equals(businessCustomer1Preference.getParty(), businessCustomer2Preference.getParty())
            && Objects.equals(
            businessCustomer1Preference.getType(), businessCustomer2Preference.getType())) {

          comparePreferences(businessCustomer1Preference, businessCustomer2Preference);

          foundPreference = true;
        }
      }

      if (!foundPreference) {
        fail("Failed to find the preference (" + businessCustomer1Preference.getType() + ")");
      }
    }
  }


  /**
   * Test the individual customer validation functionality.
   */
  @Test
  public void validateIndividualCustomerTest() throws Exception {
    IndividualCustomer individualCustomer = getTestIndividualCustomerDetails();

    Set<ConstraintViolation<IndividualCustomer>> constraintViolations =
        customerService.validateIndividualCustomer(individualCustomer);

    if (constraintViolations.size() > 0) {
      fail("Failed to successfully validate the individual customer");
    }
  }

  /**
   * Test the business customer validation functionality.
   */
  @Test
  public void validateBusinessCustomerTest() throws Exception {
    BusinessCustomer businessCustomer = getTestBusinessCustomerDetails();

    Set<ConstraintViolation<BusinessCustomer>> constraintViolations =
        customerService.validateBusinessCustomer(businessCustomer);

    if (constraintViolations.size() > 0) {
      fail("Failed to successfully validate the business customer");
    }
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
        "The number of party attributes for the two individual customers do not match",
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
        fail("Failed to find the party attribute (" + individualCustomer1Attribute.getType() + ")");
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

          comparePhysicalAddresses(
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

    for (Preference individualCustomer1Preference : individualCustomer1.getPreferences()) {
      boolean foundPreference = false;

      for (Preference individualCustomer2Preference : individualCustomer2.getPreferences()) {

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

    assertEquals(
        "The number of party roles for the two individual customers do not match",
        individualCustomer1.getRoles().size(),
        individualCustomer2.getRoles().size());

    for (PartyRole individualCustomer1Role : individualCustomer1.getRoles()) {
      boolean foundPartyRole = false;

      for (PartyRole individualCustomer2Role : individualCustomer2.getRoles()) {
        if (individualCustomer1Role.getType().equals(individualCustomer2Role.getType())) {

          assertEquals(
              "The purpose for the two party roles do not match",
              individualCustomer1Role.getPurpose(),
              individualCustomer2Role.getPurpose());

          foundPartyRole = true;
        }
      }

      if (!foundPartyRole) {
        fail("Failed to find the party role (" + individualCustomer1Role.getType() + ")");
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
  }

  private void comparePhysicalAddresses(
      PhysicalAddress physicalAddress1, PhysicalAddress physicalAddress2) {
    assertEquals(
        "The building floor values for the two party physical addresses do not match",
        physicalAddress1.getBuildingFloor(),
        physicalAddress2.getBuildingFloor());
    assertEquals(
        "The building name values for the two party physical addresses do not match",
        physicalAddress1.getBuildingName(),
        physicalAddress2.getBuildingName());
    assertEquals(
        "The building room values for the two party physical addresses do not match",
        physicalAddress1.getBuildingRoom(),
        physicalAddress2.getBuildingRoom());
    assertEquals(
        "The city values for the two party physical addresses do not match",
        physicalAddress1.getCity(),
        physicalAddress2.getCity());
    assertEquals(
        "The complex name values for the two party physical addresses do not match",
        physicalAddress1.getComplexName(),
        physicalAddress2.getComplexName());
    assertEquals(
        "The complex unit number values for the two party physical addresses do not match",
        physicalAddress1.getComplexUnitNumber(),
        physicalAddress2.getComplexUnitNumber());
    assertEquals(
        "The farm description values for the two party physical addresses do not match",
        physicalAddress1.getFarmDescription(),
        physicalAddress2.getFarmDescription());
    assertEquals(
        "The farm name values for the two party physical addresses do not match",
        physicalAddress1.getFarmName(),
        physicalAddress2.getFarmName());
    assertEquals(
        "The farm number values for the two party physical addresses do not match",
        physicalAddress1.getFarmNumber(),
        physicalAddress2.getFarmNumber());
    assertEquals(
        "The line 1 values for the two party physical addresses do not match",
        physicalAddress1.getLine1(),
        physicalAddress2.getLine1());
    assertEquals(
        "The line 2 values for the two party physical addresses do not match",
        physicalAddress1.getLine2(),
        physicalAddress2.getLine2());
    assertEquals(
        "The line 3 values for the two party physical addresses do not match",
        physicalAddress1.getLine3(),
        physicalAddress2.getLine3());
    assertEquals(
        "The purpose values for the two party physical addresses do not match",
        physicalAddress1.getPurposes(),
        physicalAddress2.getPurposes());
    assertEquals(
        "The region values for the two party physical addresses do not match",
        physicalAddress1.getRegion(),
        physicalAddress2.getRegion());
    assertEquals(
        "The site block values for the two party physical addresses do not match",
        physicalAddress1.getSiteBlock(),
        physicalAddress2.getSiteBlock());
    assertEquals(
        "The site number values for the two party physical addresses do not match",
        physicalAddress1.getSiteNumber(),
        physicalAddress2.getSiteNumber());
    assertEquals(
        "The street name values for the two party physical addresses do not match",
        physicalAddress1.getStreetName(),
        physicalAddress2.getStreetName());
    assertEquals(
        "The street number values for the two party physical addresses do not match",
        physicalAddress1.getStreetNumber(),
        physicalAddress2.getStreetNumber());
    assertEquals(
        "The suburb values for the two party physical addresses do not match",
        physicalAddress1.getSuburb(),
        physicalAddress2.getSuburb());
    assertEquals(
        "The type values for the two party physical addresses do not match",
        physicalAddress1.getType(),
        physicalAddress2.getType());
  }

  private void comparePreferences(Preference preference1, Preference preference2) {
    assertEquals(
        "The type values for the two preferences do not match",
        preference1.getType(),
        preference2.getType());
    assertEquals(
        "The value values for the two preferences do not match",
        preference1.getValue(),
        preference2.getValue());
  }
}
