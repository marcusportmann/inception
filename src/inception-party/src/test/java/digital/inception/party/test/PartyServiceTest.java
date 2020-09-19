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

package digital.inception.party.test;

// ~--- non-JDK imports --------------------------------------------------------

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import com.github.f4b6a3.uuid.UuidCreator;
import digital.inception.core.sorting.SortDirection;
import digital.inception.party.ContactMechanism;
import digital.inception.party.ContactMechanismPurpose;
import digital.inception.party.ContactMechanismType;
import digital.inception.party.IPartyService;
import digital.inception.party.IdentityDocument;
import digital.inception.party.Organization;
import digital.inception.party.Organizations;
import digital.inception.party.Parties;
import digital.inception.party.Party;
import digital.inception.party.PartyType;
import digital.inception.party.Person;
import digital.inception.party.PersonSortBy;
import digital.inception.party.Persons;
import digital.inception.party.PhysicalAddress;
import digital.inception.party.PhysicalAddressPurpose;
import digital.inception.party.PhysicalAddressType;
import digital.inception.test.TestClassRunner;
import digital.inception.test.TestConfiguration;
import java.security.SecureRandom;
import java.time.LocalDate;
import java.util.Objects;
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
 * The <code>PartyServiceTest</code> class contains the implementation of the JUnit tests for the
 * <code>PartyService</code> class.
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
public class PartyServiceTest {

  private static final SecureRandom random = new SecureRandom();

  private static int organizationCount;

  private static int partyCount;

  private static int personCount;

  /** The Party Service. */
  @Autowired private IPartyService partyService;

  private static synchronized Organization getTestOrganizationDetails() {
    organizationCount++;

    Organization organization = new Organization();

    organization.setId(UuidCreator.getShortPrefixComb());
    organization.setName("Organization Name " + organizationCount);

    return organization;
  }

  private static synchronized Party getTestPartyDetails() {
    partyCount++;

    Party party = new Party();

    party.setId(UuidCreator.getShortPrefixComb());
    party.setType(PartyType.ORGANIZATION);
    party.setName("Party Name " + partyCount);

    return party;
  }

  private static synchronized Person getTestCompletePersonDetails() {
    personCount++;

    Person person = new Person();

    person.setCountryOfBirth("US");
    person.setDateOfBirth(LocalDate.of(1976, 3, 7));
    person.setEmploymentStatus("O");
    person.setGender("M");
    person.setGivenName("GivenName" + personCount);
    person.setId(UuidCreator.getShortPrefixComb());
    person.setInitials("G M");
    person.setMaidenName("MaidenName" + personCount);
    person.setMaritalStatus("M");
    person.setMarriageType("1");
    person.setMiddleNames("MiddleName" + personCount);
    person.setName(
        "GivenName"
            + personCount
            + " "
            + "MiddleName"
            + personCount
            + " "
            + "Surname"
            + personCount);
    person.setPreferredName("PreferredName" + personCount);
    person.setRace("W");
    person.setSurname("Surname" + personCount);
    person.setTitle("1");

    person.addIdentityDocument(
        new IdentityDocument("ZAIDCARD", "ZA", LocalDate.of(2012, 5, 1), "8904085800089"));

    person.addContactMechanism(
        new ContactMechanism(
            ContactMechanismType.MOBILE_NUMBER,
            ContactMechanismPurpose.PERSONAL_MOBILE_NUMBER,
            "+27835551234"));
    person.addContactMechanism(
        new ContactMechanism(
            ContactMechanismType.EMAIL_ADDRESS,
            ContactMechanismPurpose.PERSONAL_EMAIL_ADDRESS,
            "test@test.com"));

    person.addPhysicalAddress(
        new PhysicalAddress(PhysicalAddressType.STREET, PhysicalAddressPurpose.RESIDENTIAL));
    person.addPhysicalAddress(
        new PhysicalAddress(PhysicalAddressType.UNSTRUCTURED, PhysicalAddressPurpose.POSTAL));

    return person;
  }

  private static synchronized Person getTestBasicPersonDetails() {
    personCount++;

    Person person = new Person();

    person.setId(UuidCreator.getShortPrefixComb());
    person.setName("Full Name " + personCount);

    person.addIdentityDocument(
        new IdentityDocument("ZAIDCARD", "ZA", LocalDate.of(2012, 5, 1), "8904085800089"));

    return person;
  }

  /** Test the organization functionality. */
  @Test
  public void organizationTest() throws Exception {
    Organization organization = getTestOrganizationDetails();

    partyService.createOrganization(organization);

    Organizations filteredOrganizations =
        partyService.getOrganizations("", SortDirection.ASCENDING, 0, 100);

    assertEquals(
        "The correct number of filtered organizations was not retrieved",
        1,
        filteredOrganizations.getOrganizations().size());

    compareOrganizations(organization, filteredOrganizations.getOrganizations().get(0));

    organization.setName(organization.getName() + " Updated");

    organization.addContactMechanism(
        new ContactMechanism(
            ContactMechanismType.PHONE_NUMBER,
            ContactMechanismPurpose.MAIN_PHONE_NUMBER,
            "0115551234"));

    organization.addPhysicalAddress(
        new PhysicalAddress(PhysicalAddressType.STREET, PhysicalAddressPurpose.MAIN));

    partyService.updateOrganization(organization);

    filteredOrganizations = partyService.getOrganizations("", SortDirection.ASCENDING, 0, 100);

    assertEquals(
        "The correct number of filtered organizations was not retrieved",
        1,
        filteredOrganizations.getOrganizations().size());

    compareOrganizations(organization, filteredOrganizations.getOrganizations().get(0));

    Parties filteredParties = partyService.getParties("", SortDirection.ASCENDING, 0, 100);

    assertEquals(
        "The correct number of filtered parties was not retrieved",
        1,
        filteredParties.getParties().size());

    partyService.deleteOrganization(organization.getId());
  }

  /** Test the party inheritance functionality. */
  @Test
  public void partyInheritanceTest() throws Exception {
    Organization organization = getTestOrganizationDetails();

    partyService.createOrganization(organization);

    Person person = getTestCompletePersonDetails();

    partyService.createPerson(person);

    Parties filteredParties = partyService.getParties("", SortDirection.ASCENDING, 0, 100);

    assertEquals(
        "The correct number of filtered parties was not retrieved",
        2,
        filteredParties.getParties().size());

    partyService.deletePerson(person.getId());

    partyService.deleteOrganization(organization.getId());
  }

  /** Test the party functionality. */
  @Test
  public void partyTest() throws Exception {
    Party party = getTestPartyDetails();

    partyService.createParty(party);

    Parties filteredParties = partyService.getParties("", SortDirection.ASCENDING, 0, 100);

    assertEquals(
        "The correct number of filtered parties was not retrieved",
        1,
        filteredParties.getParties().size());

    compareParties(party, filteredParties.getParties().get(0));

    party.setName(party.getName() + " Updated");

    partyService.updateParty(party);

    filteredParties = partyService.getParties("", SortDirection.ASCENDING, 0, 100);

    assertEquals(
        "The correct number of filtered parties was not retrieved",
        1,
        filteredParties.getParties().size());

    compareParties(party, filteredParties.getParties().get(0));

    partyService.deleteParty(party.getId());
  }

  /** Test the person functionality. */
  @Test
  public void personTest() throws Exception {
    Person person = getTestBasicPersonDetails();

    partyService.createPerson(person);

    Persons filteredPersons =
        partyService.getPersons("", PersonSortBy.NAME, SortDirection.ASCENDING, 0, 100);

    assertEquals(
        "The correct number of filtered persons was not retrieved",
        1,
        filteredPersons.getPersons().size());

    comparePersons(person, filteredPersons.getPersons().get(0));

    partyService.deletePerson(person.getId());

    person = getTestCompletePersonDetails();

    partyService.createPerson(person);

    filteredPersons =
        partyService.getPersons("", PersonSortBy.NAME, SortDirection.ASCENDING, 0, 100);

    assertEquals(
        "The correct number of filtered persons was not retrieved",
        1,
        filteredPersons.getPersons().size());

    comparePersons(person, filteredPersons.getPersons().get(0));

    person.setCorrespondenceLanguage("EN");
    person.setCountryOfBirth("UK");
    person.setCountryOfResidence("ZA");
    person.setDateOfBirth(LocalDate.of(1985, 5, 1));
    person.setDateOfDeath(LocalDate.of(2200, 1, 1));
    person.setEmploymentStatus("E");
    person.setEmploymentType("F");
    person.setGender("F");
    person.setGivenName(person.getGivenName() + " Updated");
    person.setHomeLanguage("AF");
    person.setInitials(person.getInitials() + " Updated");
    person.setMaidenName(person.getMaidenName() + " Updated");
    person.setMaritalStatus("D");
    person.setMarriageType(null);
    person.setMiddleNames(person.getMiddleNames() + " Updated");
    person.setName(person.getName() + " Updated");
    person.setPreferredName(person.getPreferredName() + " Updated");
    person.setRace("B");
    person.setResidencyStatus("C");
    person.setSurname(person.getSurname() + " Updated");
    person.setTitle("5");

    person.addIdentityDocument(
        new IdentityDocument(
            "PASSPORT", "ZA", LocalDate.of(2016, 10, 7), LocalDate.of(2025, 9, 1), "A1234567890"));

    person.removeIdentityDocumentByType("ZAIDCARD");

    person.removeContactMechanism(
        ContactMechanismType.MOBILE_NUMBER, ContactMechanismPurpose.PERSONAL_MOBILE_NUMBER);

    person
        .getContactMechanism(
            ContactMechanismType.EMAIL_ADDRESS, ContactMechanismPurpose.PERSONAL_EMAIL_ADDRESS)
        .setValue("test.updated@test.com");

    person.addContactMechanism(
        new ContactMechanism(
            ContactMechanismType.PHONE_NUMBER,
            ContactMechanismPurpose.HOME_PHONE_NUMBER,
            "0115551234"));

    partyService.updatePerson(person);

    filteredPersons =
        partyService.getPersons("", PersonSortBy.NAME, SortDirection.ASCENDING, 0, 100);

    assertEquals(
        "The correct number of filtered persons was not retrieved",
        1,
        filteredPersons.getPersons().size());

    comparePersons(person, filteredPersons.getPersons().get(0));

    Parties filteredParties = partyService.getParties("", SortDirection.ASCENDING, 0, 100);

    assertEquals(
        "The correct number of filtered parties was not retrieved",
        1,
        filteredParties.getParties().size());

    partyService.deletePerson(person.getId());
  }

  private void compareOrganizations(Organization organization1, Organization organization2) {
    assertEquals(
        "The ID values for the two organizations do not match",
        organization1.getId(),
        organization2.getId());
    assertEquals(
        "The name values for the two organizations do not match",
        organization1.getName(),
        organization2.getName());

    assertEquals(
        "The number of contact mechanisms for the two persons do not match",
        organization1.getContactMechanisms().size(),
        organization2.getContactMechanisms().size());

    for (ContactMechanism person1ContactMechanism : organization1.getContactMechanisms()) {
      boolean foundContactMechanism = false;

      for (ContactMechanism person2ContactMechanism : organization2.getContactMechanisms()) {

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
        "The number of physical addresses for the two persons do not match",
        organization1.getPhysicalAddresses().size(),
        organization2.getPhysicalAddresses().size());

    for (PhysicalAddress person1PhysicalAddress : organization1.getPhysicalAddresses()) {
      boolean foundPhysicalAddress = false;

      for (PhysicalAddress person2PhysicalAddress : organization2.getPhysicalAddresses()) {

        if (Objects.equals(person1PhysicalAddress.getParty(), person2PhysicalAddress.getParty())
            && Objects.equals(person1PhysicalAddress.getType(), person2PhysicalAddress.getType())
            && Objects.equals(
                person1PhysicalAddress.getPurpose(), person2PhysicalAddress.getPurpose())) {

          foundPhysicalAddress = true;
        }
      }

      if (!foundPhysicalAddress) {
        fail(
            "Failed to find the physical address ("
                + person1PhysicalAddress.getType()
                + ")("
                + person1PhysicalAddress.getPurpose()
                + ")");
      }
    }
  }

  private void compareParties(Party party1, Party party2) {
    assertEquals("The ID values for the two parties do not match", party1.getId(), party2.getId());
    assertEquals(
        "The name values for the two parties do not match", party1.getName(), party2.getName());
  }

  private void comparePersons(Person person1, Person person2) {
    assertEquals(
        "The correspondence language values for the two persons do not match",
        person1.getCorrespondenceLanguage(),
        person2.getCorrespondenceLanguage());
    assertEquals(
        "The country of birth values for the two persons do not match",
        person1.getCountryOfBirth(),
        person2.getCountryOfBirth());
    assertEquals(
        "The country of residence values for the two persons do not match",
        person1.getCountryOfResidence(),
        person2.getCountryOfResidence());
    assertEquals(
        "The date of birth values for the two persons do not match",
        person1.getDateOfBirth(),
        person2.getDateOfBirth());
    assertEquals(
        "The date of death values for the two persons do not match",
        person1.getDateOfDeath(),
        person2.getDateOfDeath());
    assertEquals(
        "The employment status values for the two persons do not match",
        person1.getEmploymentStatus(),
        person2.getEmploymentStatus());
    assertEquals(
        "The employment type values for the two persons do not match",
        person1.getEmploymentType(),
        person2.getEmploymentType());
    assertEquals(
        "The gender values for the two persons do not match",
        person1.getGender(),
        person2.getGender());
    assertEquals(
        "The given name values for the two persons do not match",
        person1.getGivenName(),
        person2.getGivenName());
    assertEquals(
        "The home language values for the two persons do not match",
        person1.getHomeLanguage(),
        person2.getHomeLanguage());
    assertEquals(
        "The ID values for the two persons do not match", person1.getId(), person2.getId());
    assertEquals(
        "The initials values for the two persons do not match",
        person1.getInitials(),
        person2.getInitials());
    assertEquals(
        "The maiden name values for the two persons do not match",
        person1.getMaidenName(),
        person2.getMaidenName());
    assertEquals(
        "The marital status values for the two persons do not match",
        person1.getMaritalStatus(),
        person2.getMaritalStatus());
    assertEquals(
        "The marriage type values for the two persons do not match",
        person1.getMarriageType(),
        person2.getMarriageType());
    assertEquals(
        "The middle names values for the two persons do not match",
        person1.getMiddleNames(),
        person2.getMiddleNames());
    assertEquals(
        "The name values for the two persons do not match", person1.getName(), person2.getName());
    assertEquals(
        "The preferred name values for the two persons do not match",
        person1.getPreferredName(),
        person2.getPreferredName());
    assertEquals(
        "The race values for the two persons do not match", person1.getRace(), person2.getRace());
    assertEquals(
        "The residency status values for the two persons do not match",
        person1.getResidencyStatus(),
        person2.getResidencyStatus());
    assertEquals(
        "The surname values for the two persons do not match",
        person1.getSurname(),
        person2.getSurname());
    assertEquals(
        "The title values for the two persons do not match",
        person1.getTitle(),
        person2.getTitle());
    assertEquals(
        "The number of identity documents for the two persons do not match",
        person1.getIdentityDocuments().size(),
        person2.getIdentityDocuments().size());

    for (IdentityDocument person1IdentityDocument : person1.getIdentityDocuments()) {
      boolean foundIdentityDocument = false;

      for (IdentityDocument person2IdentityDocument : person2.getIdentityDocuments()) {
        if (person1IdentityDocument.getType().equals(person2IdentityDocument.getType())
            && person1IdentityDocument
                .getCountryOfIssue()
                .equals(person2IdentityDocument.getCountryOfIssue())
            && person1IdentityDocument
                .getDateOfIssue()
                .equals(person2IdentityDocument.getDateOfIssue())) {

          assertEquals(
              "The date of expiry for the two identity documents do not match",
              person1IdentityDocument.getDateOfExpiry(),
              person2IdentityDocument.getDateOfExpiry());
          assertEquals(
              "The numbers for the two identity documents do not match",
              person1IdentityDocument.getNumber(),
              person2IdentityDocument.getNumber());

          foundIdentityDocument = true;
        }
      }

      if (!foundIdentityDocument) {
        fail(
            "Failed to find the identity document ("
                + person1IdentityDocument.getType()
                + ")("
                + person1IdentityDocument.getCountryOfIssue()
                + ")("
                + person1IdentityDocument.getDateOfIssue()
                + ")");
      }
    }

    assertEquals(
        "The number of contact mechanisms for the two persons do not match",
        person1.getContactMechanisms().size(),
        person2.getContactMechanisms().size());

    for (ContactMechanism person1ContactMechanism : person1.getContactMechanisms()) {
      boolean foundContactMechanism = false;

      for (ContactMechanism person2ContactMechanism : person2.getContactMechanisms()) {

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
        "The number of physical addresses for the two persons do not match",
        person1.getPhysicalAddresses().size(),
        person2.getPhysicalAddresses().size());

    for (PhysicalAddress person1PhysicalAddress : person1.getPhysicalAddresses()) {
      boolean foundPhysicalAddress = false;

      for (PhysicalAddress person2PhysicalAddress : person2.getPhysicalAddresses()) {

        if (Objects.equals(person1PhysicalAddress.getParty(), person2PhysicalAddress.getParty())
            && Objects.equals(person1PhysicalAddress.getType(), person2PhysicalAddress.getType())
            && Objects.equals(
                person1PhysicalAddress.getPurpose(), person2PhysicalAddress.getPurpose())) {

          foundPhysicalAddress = true;
        }
      }

      if (!foundPhysicalAddress) {
        fail(
            "Failed to find the physical address ("
                + person1PhysicalAddress.getType()
                + ")("
                + person1PhysicalAddress.getPurpose()
                + ")");
      }
    }
  }



}
