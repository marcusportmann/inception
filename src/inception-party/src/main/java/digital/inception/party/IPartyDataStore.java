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
   * @throws DuplicateAssociationException if the association already exists
   * @throws PartyNotFoundException if one or more parties for the association could not be found
   * @throws ServiceUnavailableException if the association could not be created
   */
  Association createAssociation(UUID tenantId, Association association)
      throws DuplicateAssociationException, PartyNotFoundException, ServiceUnavailableException;

  /**
   * Create the new organization.
   *
   * @param tenantId the Universally Unique Identifier (UUID) for the tenant
   * @param organization the organization
   * @return the organization
   * @throws DuplicateOrganizationException if the organization already exists
   * @throws ServiceUnavailableException if the organization could not be created
   */
  Organization createOrganization(UUID tenantId, Organization organization)
      throws DuplicateOrganizationException, ServiceUnavailableException;

  /**
   * Create the new person.
   *
   * @param tenantId the Universally Unique Identifier (UUID) for the tenant
   * @param person the person
   * @return the person
   * @throws DuplicatePersonException if the person already exists
   * @throws ServiceUnavailableException if the person could not be created
   */
  Person createPerson(UUID tenantId, Person person)
      throws DuplicatePersonException, ServiceUnavailableException;

  /**
   * Delete the association.
   *
   * @param tenantId the Universally Unique Identifier (UUID) for the tenant
   * @param associationId the Universally Unique Identifier (UUID) for the association
   * @throws AssociationNotFoundException if the association could not be found
   * @throws ServiceUnavailableException if the association could not be deleted
   */
  void deleteAssociation(UUID tenantId, UUID associationId)
      throws AssociationNotFoundException, ServiceUnavailableException;

  /**
   * Delete the organization.
   *
   * @param tenantId the Universally Unique Identifier (UUID) for the tenant
   * @param organizationId the Universally Unique Identifier (UUID) for the organization
   * @throws OrganizationNotFoundException if the organization could not be found
   * @throws ServiceUnavailableException if the organization could not be deleted
   */
  void deleteOrganization(UUID tenantId, UUID organizationId)
      throws OrganizationNotFoundException, ServiceUnavailableException;

  /**
   * Delete the party.
   *
   * @param tenantId the Universally Unique Identifier (UUID) for the tenant
   * @param partyId the Universally Unique Identifier (UUID) for the party
   * @throws PartyNotFoundException if the party could not be found
   * @throws ServiceUnavailableException if the party could not be deleted
   */
  void deleteParty(UUID tenantId, UUID partyId)
      throws PartyNotFoundException, ServiceUnavailableException;

  /**
   * Delete the person.
   *
   * @param tenantId the Universally Unique Identifier (UUID) for the tenant
   * @param personId the Universally Unique Identifier (UUID) for the person
   * @throws PersonNotFoundException if the person could not be found
   * @throws ServiceUnavailableException if the person could not be deleted
   */
  void deletePerson(UUID tenantId, UUID personId)
      throws PersonNotFoundException, ServiceUnavailableException;

  /**
   * Retrieve the association.
   *
   * @param tenantId the Universally Unique Identifier (UUID) for the tenant
   * @param associationId the Universally Unique Identifier (UUID) for the association
   * @return the association
   * @throws AssociationNotFoundException if the association could not be found
   * @throws ServiceUnavailableException if the association could not be retrieved
   */
  Association getAssociation(UUID tenantId, UUID associationId)
      throws AssociationNotFoundException, ServiceUnavailableException;

  /**
   * Retrieve the associations for the party.
   *
   * @param tenantId the Universally Unique Identifier (UUID) for the tenant
   * @param partyId the Universally Unique Identifier (UUID) for the party
   * @param sortBy the optional method used to sort the associations e.g. by type
   * @param sortDirection the optional sort direction to apply to the associations
   * @param pageIndex the optional page index
   * @param pageSize the optional page size
   * @return the associations
   * @throws PartyNotFoundException if the party could not be found
   * @throws ServiceUnavailableException if the associations could not be retrieved
   */
  Associations getAssociationsForParty(
      UUID tenantId,
      UUID partyId,
      AssociationSortBy sortBy,
      SortDirection sortDirection,
      Integer pageIndex,
      Integer pageSize)
      throws PartyNotFoundException, ServiceUnavailableException;

  /**
   * Retrieve the organization.
   *
   * @param tenantId the Universally Unique Identifier (UUID) for the tenant
   * @param organizationId the Universally Unique Identifier (UUID) for the organization
   * @return the organization
   * @throws OrganizationNotFoundException if the organization could not be found
   * @throws ServiceUnavailableException if the organization could not be retrieved
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
   * @throws ServiceUnavailableException if the organizations could not be retrieved
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
   * @param sortBy the optional method used to sort the parties e.g. by name
   * @param sortDirection the optional sort direction to apply to the parties
   * @param pageIndex the optional page index
   * @param pageSize the optional page size
   * @return the parties
   * @throws ServiceUnavailableException if the parties could not be retrieved
   */
  Parties getParties(
      UUID tenantId,
      String filter,
      PartySortBy sortBy,
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
   * @throws PartyNotFoundException if the party could not be found
   * @throws ServiceUnavailableException if the party could not be retrieved
   */
  Party getParty(UUID tenantId, UUID partyId)
      throws PartyNotFoundException, ServiceUnavailableException;

  /**
   * Retrieve the person.
   *
   * @param tenantId the Universally Unique Identifier (UUID) for the tenant
   * @param personId the Universally Unique Identifier (UUID) for the person
   * @return the person
   * @throws PersonNotFoundException if the person could not be found
   * @throws ServiceUnavailableException if the person could not be retrieved
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
   * @throws ServiceUnavailableException if the persons could not be retrieved
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
   * @throws ServiceUnavailableException if the snapshots for the entity could not be retrieved
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
   * @throws ServiceUnavailableException if the Universally Unique Identifier (UUID) for the tenant
   *     the party is associated with could not be retrieved
   */
  Optional<UUID> getTenantIdForParty(UUID partyId) throws ServiceUnavailableException;

  /**
   * Update the association.
   *
   * @param tenantId the Universally Unique Identifier (UUID) for the tenant
   * @param association the association
   * @return the association
   * @throws AssociationNotFoundException if the association could not be found
   * @throws PartyNotFoundException if one or more parties for the association could not be found
   * @throws ServiceUnavailableException if the association could not be updated
   */
  Association updateAssociation(UUID tenantId, Association association)
      throws AssociationNotFoundException, PartyNotFoundException, ServiceUnavailableException;

  /**
   * Update the organization.
   *
   * @param tenantId the Universally Unique Identifier (UUID) for the tenant
   * @param organization the organization
   * @return the organization
   * @throws OrganizationNotFoundException if the organization could not be found
   * @throws ServiceUnavailableException if the organization could not be updated
   */
  Organization updateOrganization(UUID tenantId, Organization organization)
      throws OrganizationNotFoundException, ServiceUnavailableException;

  /**
   * Update the person.
   *
   * @param tenantId the Universally Unique Identifier (UUID) for the tenant
   * @param person the person
   * @return the person
   * @throws PersonNotFoundException if the person could not be found
   * @throws ServiceUnavailableException if the person could not be updated
   */
  Person updatePerson(UUID tenantId, Person person)
      throws PersonNotFoundException, ServiceUnavailableException;
}
