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

import com.github.f4b6a3.uuid.UuidCreator;
import digital.inception.party.IPartyService;
import digital.inception.party.Organization;
import digital.inception.party.Organizations;
import digital.inception.party.Parties;
import digital.inception.party.Party;
import digital.inception.party.PartyType;
import digital.inception.party.Person;
import digital.inception.party.PersonSortBy;
import digital.inception.party.Persons;
import digital.inception.party.SortDirection;
import digital.inception.test.TestClassRunner;
import digital.inception.test.TestConfiguration;
import java.security.SecureRandom;
import java.time.LocalDate;
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

  /**
   * The Party Service.
   */
  @Autowired
  private IPartyService partyService;

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
    party.setType(PartyType.UNKNOWN);
    party.setName("Party Name " + partyCount);

    return party;
  }

  private static synchronized Person getTestPersonDetails() {
    personCount++;

    Person person = new Person();

    person.setId(UuidCreator.getShortPrefixComb());
    person.setName("Person Name " + personCount);
    person.setDateOfBirth(LocalDate.of(1976, 3, 7));
    person.setGender(String.valueOf(random.nextInt(4)));

    return person;
  }

  /**
   * Test the organization functionality.
   */
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

    Parties filteredParties = partyService.getParties("", SortDirection.ASCENDING, 0, 100);

    assertEquals(
        "The correct number of filtered parties was not retrieved",
        1,
        filteredParties.getParties().size());

    partyService.deleteOrganization(organization.getId());
  }

  /**
   * Test the party functionality.
   */
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

    partyService.deleteParty(party.getId());
  }

  /**
   * Test the person functionality.
   */
  @Test
  public void personTest() throws Exception {
    Person person = getTestPersonDetails();

    partyService.createPerson(person);

    Persons filteredPersons =
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
        "The ID values for the two organizations do not match", organization1.getId(),
        organization2.getId());
    assertEquals(
        "The name values for the two organizations do not match", organization1.getName(),
        organization2.getName());
  }

  private void compareParties(Party party1, Party party2) {
    assertEquals(
        "The ID values for the two parties do not match", party1.getId(), party2.getId());
    assertEquals(
        "The name values for the two parties do not match", party1.getName(), party2.getName());
  }

  private void comparePersons(Person person1, Person person2) {
    assertEquals(
        "The ID values for the two persons do not match", person1.getId(), person2.getId());
    assertEquals(
        "The name values for the two persons do not match", person1.getName(), person2.getName());
    assertEquals(
        "The date of birth values for the two persons do not match", person1.getDateOfBirth(),
        person2.getDateOfBirth());
    assertEquals(
        "The gender values for the two persons do not match", person1.getGender(),
        person2.getGender());
  }


}
