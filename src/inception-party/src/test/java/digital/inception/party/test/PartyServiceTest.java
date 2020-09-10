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
import digital.inception.test.TestClassRunner;
import digital.inception.test.TestConfiguration;
import java.security.SecureRandom;
import java.time.LocalDate;
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

    person.setId(UuidCreator.getShortPrefixComb());
    person.setName(
        "GivenName"
            + personCount
            + " "
            + "MiddleName"
            + personCount
            + " "
            + "Surname"
            + personCount);
//    person.setPreferredName("PreferredName" + personCount);
//    person.setTitle("1");
//    person.setGivenName("GivenName" + personCount);
//    person.setMiddleNames("MiddleName" + personCount);
//    person.setInitials("G M");
//    person.setSurname("Surname" + personCount);
//    person.setMaidenName("MaidenName" + personCount);
//    person.setGender("M");
//    person.setRace("W");
//    person.setMaritalStatus("M");
//    person.setMarriageType("1");
//    person.setDateOfBirth(LocalDate.of(1976, 3, 7));
//    person.setDateOfBirth(LocalDate.of(2200, 1, 1));
//    person.setCountryOfBirth("US");
//    person.setGender(String.valueOf(random.nextInt(4)));

    IdentityDocument zaidcIdentityDocument = new IdentityDocument();
    zaidcIdentityDocument.setId(UuidCreator.getShortPrefixComb());
    zaidcIdentityDocument.setType("ZAIDCARD");
    zaidcIdentityDocument.setDateOfIssue(LocalDate.of(2012, 5, 1));
    zaidcIdentityDocument.setCountryOfIssue("ZA");

    //person.addIdentityDocument(zaidcIdentityDocument);

    return person;
  }

  private static synchronized Person getTestBasicPersonDetails() {
    personCount++;

    Person person = new Person();

    person.setId(UuidCreator.getShortPrefixComb());
    person.setName("Full Name " + personCount);

    IdentityDocument zaidcIdentityDocument = new IdentityDocument();
    zaidcIdentityDocument.setId(UuidCreator.getShortPrefixComb());
    zaidcIdentityDocument.setType("ZAIDCARD");
    zaidcIdentityDocument.setDateOfIssue(LocalDate.of(2012, 5, 1));
    zaidcIdentityDocument.setCountryOfIssue("ZA");

    //person.addIdentityDocument(zaidcIdentityDocument);

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

//    person.setCountryOfBirth("UK");
//    person.setDateOfBirth(LocalDate.of(1985, 5, 1));
//    person.setGender(String.valueOf(random.nextInt(4)));
//    person.setGivenName(person.getGivenName() + " Updated");
//    person.setPreferredName(person.getPreferredName() + " Updated");
//    person.setSurname(person.getSurname() + " Updated");

    IdentityDocument passportIdentityDocument = new IdentityDocument();
    passportIdentityDocument.setId(UuidCreator.getShortPrefixComb());
    passportIdentityDocument.setType("PASSPORT");
    passportIdentityDocument.setDateOfIssue(LocalDate.of(2016, 10, 7));
    passportIdentityDocument.setDateOfExpiry(LocalDate.of(2025, 9, 1));
    passportIdentityDocument.setCountryOfIssue("ZA");

//    person.addIdentityDocument(passportIdentityDocument);

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
  }

  private void compareParties(Party party1, Party party2) {
    assertEquals("The ID values for the two parties do not match", party1.getId(), party2.getId());
    assertEquals(
        "The name values for the two parties do not match", party1.getName(), party2.getName());
  }

  private void comparePersons(Person person1, Person person2) {

//    assertEquals(
//        "The country of birth values for the two persons do not match",
//        person1.getCountryOfBirth(),
//        person2.getCountryOfBirth());
//    assertEquals(
//        "The date of birth values for the two persons do not match",
//        person1.getDateOfBirth(),
//        person2.getDateOfBirth());
//    assertEquals(
//        "The date of death values for the two persons do not match",
//        person1.getDateOfBirth(),
//        person2.getDateOfBirth());
//
//    assertEquals(
//        "The gender values for the two persons do not match",
//        person1.getGender(),
//        person2.getGender());
//    assertEquals(
//        "The given name values for the two persons do not match",
//        person1.getGivenName(),
//        person2.getGivenName());
//    assertEquals(
//        "The ID values for the two persons do not match", person1.getId(), person2.getId());
//    assertEquals(
//        "The initials values for the two persons do not match",
//        person1.getInitials(),
//        person2.getInitials());
//    assertEquals(
//        "The maiden name values for the two persons do not match",
//        person1.getMaidenName(),
//        person2.getMaidenName());
//    assertEquals(
//        "The marital status values for the two persons do not match",
//        person1.getMaritalStatus(),
//        person2.getMaritalStatus());
//    assertEquals(
//        "The marriage type values for the two persons do not match",
//        person1.getMarriageType(),
//        person2.getMarriageType());
//
//    assertEquals(
//        "The middle names values for the two persons do not match",
//        person1.getMiddleNames(),
//        person2.getMiddleNames());
//    assertEquals(
//        "The name values for the two persons do not match", person1.getName(), person2.getName());
//    assertEquals(
//        "The preferred name values for the two persons do not match",
//        person1.getPreferredName(),
//        person2.getPreferredName());
//    assertEquals(
//        "The race values for the two persons do not match", person1.getRace(), person2.getRace());
//
//    assertEquals(
//        "The surname values for the two persons do not match",
//        person1.getSurname(),
//        person2.getSurname());
//    assertEquals(
//        "The title values for the two persons do not match",
//        person1.getTitle(),
//        person2.getTitle());
//
//    assertEquals(
//        "The number of identity documents for the two persons do not match",
//        person1.getIdentityDocuments().size(),
//        person2.getIdentityDocuments().size());
//
//    for (IdentityDocument person1IdentityDocument : person1.getIdentityDocuments()) {
//      boolean foundIdentityDocument = false;
//
//      for (IdentityDocument person2IdentityDocument : person2.getIdentityDocuments()) {
//        if (person1IdentityDocument.getId().equals(person2IdentityDocument.getId())) {
//          assertEquals(
//              "The dates of issue for the two identity documents do not match",
//              person1IdentityDocument.getDateOfIssue(),
//              person2IdentityDocument.getDateOfIssue());
//
//          assertEquals(
//              "The types for the two identity documents do not match",
//              person1IdentityDocument.getType(),
//              person2IdentityDocument.getType());
//
//          foundIdentityDocument = true;
//        }
//      }
//
//      if (!foundIdentityDocument) {
//        fail("Failed to find the identity document (" + person1IdentityDocument.getId() + ")");
//      }
//    }
  }
}
