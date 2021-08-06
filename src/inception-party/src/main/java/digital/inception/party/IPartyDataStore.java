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

import digital.inception.core.service.ServiceUnavailableException;
import digital.inception.core.sorting.SortDirection;
import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

/**
 * The <b>IPartyDataStore</b> interface defines the functionality provided by a party data store,
 * which manages structured and unstructured information for parties.
 *
 * @author Marcus Portmann
 */
public interface IPartyDataStore {

  /**
   * Create the new association.
   *
   * @param tenantId the Universally Unique Identifier (UUID) for the tenant
   * @param association the association
   * @return the association
   */
  Association createAssociation(UUID tenantId, Association association)
      throws DuplicateAssociationException, ServiceUnavailableException;

  /**
   * Create the new organization.
   *
   * @param tenantId the Universally Unique Identifier (UUID) for the tenant
   * @param organization the organization
   * @return the organization
   */
  Organization createOrganization(UUID tenantId, Organization organization)
      throws DuplicateOrganizationException, ServiceUnavailableException;

  /**
   * Create the new person.
   *
   * @param tenantId the Universally Unique Identifier (UUID) for the tenant
   * @param person the person
   */
  Person createPerson(UUID tenantId, Person person)
      throws DuplicatePersonException, ServiceUnavailableException;

  /**
   * Delete the organization.
   *
   * @param tenantId the Universally Unique Identifier (UUID) for the tenant
   * @param organizationId the Universally Unique Identifier (UUID) for the organization
   */
  void deleteOrganization(UUID tenantId, UUID organizationId)
      throws OrganizationNotFoundException, ServiceUnavailableException;

  /**
   * Delete the party.
   *
   * @param tenantId the Universally Unique Identifier (UUID) for the tenant
   * @param partyId the Universally Unique Identifier (UUID) for the party
   */
  void deleteParty(UUID tenantId, UUID partyId)
      throws PartyNotFoundException, ServiceUnavailableException;

  /**
   * Delete the person.
   *
   * @param tenantId the Universally Unique Identifier (UUID) for the tenant
   * @param personId the Universally Unique Identifier (UUID) for the person
   */
  void deletePerson(UUID tenantId, UUID personId)
      throws PersonNotFoundException, ServiceUnavailableException;

  /**
   * Retrieve the organization.
   *
   * @param tenantId the Universally Unique Identifier (UUID) for the tenant
   * @param organizationId the Universally Unique Identifier (UUID) for the organization
   * @return the organization
   */
  Organization getOrganization(UUID tenantId, UUID organizationId)
      throws OrganizationNotFoundException, ServiceUnavailableException;

  /**
   * Retrieve the organizations.
   *
   * @param tenantId the Universally Unique Identifier (UUID) for the tenant
   * @param filter the optional filter to apply to the organizations
   * @param sortBy the optional method used to sort the organizations e.g. by name
   * @param sortDirection the optional sort direction to apply to the organizations
   * @param pageIndex the optional page index
   * @param pageSize the optional page size
   * @return the organizations
   */
  Organizations getOrganizations(
      UUID tenantId,
      String filter,
      OrganizationSortBy sortBy,
      SortDirection sortDirection,
      Integer pageIndex,
      Integer pageSize)
      throws ServiceUnavailableException;

  /**
   * Retrieve the parties.
   *
   * @param tenantId the Universally Unique Identifier (UUID) for the tenant
   * @param filter the optional filter to apply to the parties
   * @param sortDirection the optional sort direction to apply to the parties
   * @param pageIndex the optional page index
   * @param pageSize the optional page size
   * @return the parties
   */
  Parties getParties(
      UUID tenantId,
      String filter,
      SortDirection sortDirection,
      Integer pageIndex,
      Integer pageSize)
      throws ServiceUnavailableException;

  /**
   * Retrieve the party.
   *
   * @param tenantId the Universally Unique Identifier (UUID) for the tenant
   * @param partyId the Universally Unique Identifier (UUID) for the party
   * @return the party
   */
  Party getParty(UUID tenantId, UUID partyId)
      throws PartyNotFoundException, ServiceUnavailableException;

  /**
   * Retrieve the person.
   *
   * @param tenantId the Universally Unique Identifier (UUID) for the tenant
   * @param personId the Universally Unique Identifier (UUID) for the person
   * @return the person
   */
  Person getPerson(UUID tenantId, UUID personId)
      throws PersonNotFoundException, ServiceUnavailableException;

  /**
   * Retrieve the persons.
   *
   * @param tenantId the Universally Unique Identifier (UUID) for the tenant
   * @param filter the optional filter to apply to the persons
   * @param sortBy the optional method used to sort the persons e.g. by name
   * @param sortDirection the optional sort direction to apply to the persons
   * @param pageIndex the optional page index
   * @param pageSize the optional page size
   * @return the persons
   */
  Persons getPersons(
      UUID tenantId,
      String filter,
      PersonSortBy sortBy,
      SortDirection sortDirection,
      Integer pageIndex,
      Integer pageSize)
      throws ServiceUnavailableException;

  /**
   * Retrieve the snapshots for an entity.
   *
   * @param tenantId the Universally Unique Identifier (UUID) for the tenant
   * @param entityType the type of entity
   * @param entityId the Universally Unique Identifier (UUID) for the entity
   * @param from the optional date to retrieve the snapshots from
   * @param to the optional date to retrieve the snapshots to
   * @param sortDirection the optional sort direction to apply to the snapshots
   * @param pageIndex the optional page index
   * @param pageSize the optional page size
   * @return the snapshots
   */
  Snapshots getSnapshots(
      UUID tenantId,
      EntityType entityType,
      UUID entityId,
      LocalDate from,
      LocalDate to,
      SortDirection sortDirection,
      Integer pageIndex,
      Integer pageSize)
      throws ServiceUnavailableException;

  /**
   * Retrieve the Universally Unique Identifier (UUID) for the tenant the party is associated with.
   *
   * @param partyId the Universally Unique Identifier (UUID) for the party
   * @return an Optional containing the Universally Unique Identifier (UUID) for the tenant the
   *     party is associated with or an empty Optional if the party could not be found
   */
  Optional<UUID> getTenantIdForParty(UUID partyId) throws ServiceUnavailableException;

  /**
   * Update the organization.
   *
   * @param tenantId the Universally Unique Identifier (UUID) for the tenant
   * @param organization the organization
   * @return the organization
   */
  Organization updateOrganization(UUID tenantId, Organization organization)
      throws OrganizationNotFoundException, ServiceUnavailableException;

  /**
   * Update the person.
   *
   * @param tenantId the Universally Unique Identifier (UUID) for the tenant
   * @param person the person
   * @return the person
   */
  Person updatePerson(UUID tenantId, Person person)
      throws PersonNotFoundException, ServiceUnavailableException;
}
