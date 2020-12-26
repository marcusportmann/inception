/*
 * Copyright 2017 Marcus Portmann
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

// ~--- JDK imports ------------------------------------------------------------

import digital.inception.core.sorting.SortDirection;
import java.util.UUID;

/**
 * The <code>IPartyService</code> interface defines the functionality provided by a Party Service
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
      throws DuplicateOrganizationException, PartyServiceException;

  /**
   * Create the new person.
   *
   * @param person the person
   */
  void createPerson(Person person) throws DuplicatePersonException, PartyServiceException;

  /**
   * Delete the organization.
   *
   * @param organizationId the Universally Unique Identifier (UUID) uniquely identifying the
   *     organization
   */
  void deleteOrganization(UUID organizationId)
      throws OrganizationNotFoundException, PartyServiceException;

  /**
   * Delete the party.
   *
   * @param partyId the Universally Unique Identifier (UUID) uniquely identifying the party
   */
  void deleteParty(UUID partyId) throws PartyNotFoundException, PartyServiceException;

  /**
   * Delete the person.
   *
   * @param personId the Universally Unique Identifier (UUID) uniquely identifying the person
   */
  void deletePerson(UUID personId) throws PersonNotFoundException, PartyServiceException;

  /**
   * Retrieve the organization.
   *
   * @param organizationId the Universally Unique Identifier (UUID) uniquely identifying the
   *     organization
   * @return the organization
   */
  Organization getOrganization(UUID organizationId)
      throws OrganizationNotFoundException, PartyServiceException;

  /**
   * Retrieve the organizations.
   *
   * @param filter the optional filter to apply to the organizations
   * @param sortDirection the optional sort direction to apply to the organizations
   * @param pageIndex the optional page index
   * @param pageSize the optional page size
   * @return the organizations
   */
  Organizations getOrganizations(
      String filter, SortDirection sortDirection, Integer pageIndex, Integer pageSize)
      throws PartyServiceException;

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
      throws PartyServiceException;

  /**
   * Retrieve the party.
   *
   * @param partyId the Universally Unique Identifier (UUID) uniquely identifying the party
   * @return the party
   */
  Party getParty(UUID partyId) throws PartyNotFoundException, PartyServiceException;

  /**
   * Retrieve the person.
   *
   * @param personId the Universally Unique Identifier (UUID) uniquely identifying the person
   * @return the person
   */
  Person getPerson(UUID personId) throws PersonNotFoundException, PartyServiceException;

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
      throws PartyServiceException;

  /**
   * Update the organization.
   *
   * @param organization the organization
   */
  void updateOrganization(Organization organization)
      throws OrganizationNotFoundException, PartyServiceException;

  /**
   * Update the person.
   *
   * @param person the person
   */
  void updatePerson(Person person) throws PersonNotFoundException, PartyServiceException;
}
