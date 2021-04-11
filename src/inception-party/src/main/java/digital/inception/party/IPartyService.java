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

package digital.inception.party;

import digital.inception.core.service.InvalidArgumentException;
import digital.inception.core.service.ServiceUnavailableException;
import digital.inception.core.sorting.SortDirection;
import java.time.LocalDate;
import java.util.Set;
import java.util.UUID;
import javax.validation.ConstraintViolation;

/**
 * The <b>IPartyService</b> interface defines the functionality provided by a Party Service
 * implementation.
 *
 * @author Marcus Portmann
 */
@SuppressWarnings("unused")
public interface IPartyService {

  /**
   * Create the new organization.
   *
   * @param organization the organization
   */
  void createOrganization(Organization organization)
      throws InvalidArgumentException, DuplicateOrganizationException, ServiceUnavailableException;

  /**
   * Create the new person.
   *
   * @param person the person
   */
  void createPerson(Person person)
      throws InvalidArgumentException, DuplicatePersonException, ServiceUnavailableException;

  /**
   * Delete the organization.
   *
   * @param organizationId the Universally Unique Identifier (UUID) for the organization
   */
  void deleteOrganization(UUID organizationId)
      throws InvalidArgumentException, OrganizationNotFoundException, ServiceUnavailableException;

  /**
   * Delete the party.
   *
   * @param partyId the Universally Unique Identifier (UUID) for the party
   */
  void deleteParty(UUID partyId)
      throws InvalidArgumentException, PartyNotFoundException, ServiceUnavailableException;

  /**
   * Delete the person.
   *
   * @param personId the Universally Unique Identifier (UUID) for the person
   */
  void deletePerson(UUID personId)
      throws InvalidArgumentException, PersonNotFoundException, ServiceUnavailableException;

  /**
   * Retrieve the organization.
   *
   * @param organizationId the Universally Unique Identifier (UUID) for the organization
   * @return the organization
   */
  Organization getOrganization(UUID organizationId)
      throws InvalidArgumentException, OrganizationNotFoundException, ServiceUnavailableException;

  /**
   * Retrieve the organizations.
   *
   * @param filter the optional filter to apply to the organizations
   * @param sortBy the optional method used to sort the organizations e.g. by name
   * @param sortDirection the optional sort direction to apply to the organizations
   * @param pageIndex the optional page index
   * @param pageSize the optional page size
   * @return the organizations
   */
  Organizations getOrganizations(
      String filter,
      OrganizationSortBy sortBy,
      SortDirection sortDirection,
      Integer pageIndex,
      Integer pageSize)
      throws InvalidArgumentException, ServiceUnavailableException;

  /**
   * Retrieve the parties.
   *
   * @param filter the optional filter to apply to the parties
   * @param sortDirection the optional sort direction to apply to the parties
   * @param pageIndex the optional page index
   * @param pageSize the optional page size
   * @return the parties
   */
  Parties getParties(
      String filter, SortDirection sortDirection, Integer pageIndex, Integer pageSize)
      throws InvalidArgumentException, ServiceUnavailableException;

  /**
   * Retrieve the party.
   *
   * @param partyId the Universally Unique Identifier (UUID) for the party
   * @return the party
   */
  Party getParty(UUID partyId)
      throws InvalidArgumentException, PartyNotFoundException, ServiceUnavailableException;

  /**
   * Retrieve the person.
   *
   * @param personId the Universally Unique Identifier (UUID) for the person
   * @return the person
   */
  Person getPerson(UUID personId)
      throws InvalidArgumentException, PersonNotFoundException, ServiceUnavailableException;

  /**
   * Retrieve the persons.
   *
   * @param filter the optional filter to apply to the persons
   * @param sortBy the optional method used to sort the persons e.g. by name
   * @param sortDirection the optional sort direction to apply to the persons
   * @param pageIndex the optional page index
   * @param pageSize the optional page size
   * @return the persons
   */
  Persons getPersons(
      String filter,
      PersonSortBy sortBy,
      SortDirection sortDirection,
      Integer pageIndex,
      Integer pageSize)
      throws InvalidArgumentException, ServiceUnavailableException;

  /**
   * Retrieve the snapshots for the party.
   *
   * @param partyId the Universally Unique Identifier (UUID) for the party
   * @param from the optional date to retrieve the snapshots from
   * @param to the optional date to retrieve the snapshots to
   * @param sortDirection the optional sort direction to apply to the snapshots
   * @param pageIndex the optional page index
   * @param pageSize the optional page size
   * @return the snapshots
   */
  Snapshots getSnapshots(
      UUID partyId,
      LocalDate from,
      LocalDate to,
      SortDirection sortDirection,
      Integer pageIndex,
      Integer pageSize)
      throws InvalidArgumentException, ServiceUnavailableException;

  /**
   * Update the organization.
   *
   * @param organization the organization
   */
  void updateOrganization(Organization organization)
      throws InvalidArgumentException, OrganizationNotFoundException, ServiceUnavailableException;

  /**
   * Update the person.
   *
   * @param person the person
   */
  void updatePerson(Person person)
      throws InvalidArgumentException, PersonNotFoundException, ServiceUnavailableException;

  /**
   * Validate the organization.
   *
   * @param organization the organization
   * @return the constraint violations for the organization
   */
  Set<ConstraintViolation<Organization>> validateOrganization(Organization organization);

  /**
   * Validate the party.
   *
   * @param party the party
   * @return the constraint violations for the party
   */
  Set<ConstraintViolation<Party>> validateParty(Party party);

  /**
   * Validate the person.
   *
   * @param person the person
   * @return the constraint violations for the person
   */
  Set<ConstraintViolation<Person>> validatePerson(Person person);

  /**
   * Validate the physical address.
   *
   * @param physicalAddress the physical address
   * @return the constraint violations for the physical address
   */
  Set<ConstraintViolation<PhysicalAddress>> validatePhysicalAddress(
      PhysicalAddress physicalAddress);
}
