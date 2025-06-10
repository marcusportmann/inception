/*
 * Copyright Marcus Portmann
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

package digital.inception.party.service;

import digital.inception.core.exception.InvalidArgumentException;
import digital.inception.core.exception.ServiceUnavailableException;
import digital.inception.core.sorting.SortDirection;
import digital.inception.party.exception.AssociationNotFoundException;
import digital.inception.party.exception.DuplicateAssociationException;
import digital.inception.party.exception.DuplicateMandateException;
import digital.inception.party.exception.DuplicateOrganizationException;
import digital.inception.party.exception.DuplicatePersonException;
import digital.inception.party.exception.MandateNotFoundException;
import digital.inception.party.exception.OrganizationNotFoundException;
import digital.inception.party.exception.PartyNotFoundException;
import digital.inception.party.exception.PersonNotFoundException;
import digital.inception.party.model.Association;
import digital.inception.party.model.AssociationSortBy;
import digital.inception.party.model.AssociationsForParty;
import digital.inception.party.model.EntityType;
import digital.inception.party.model.Mandate;
import digital.inception.party.model.MandateSortBy;
import digital.inception.party.model.MandatesForParty;
import digital.inception.party.model.Organization;
import digital.inception.party.model.OrganizationSortBy;
import digital.inception.party.model.Organizations;
import digital.inception.party.model.Parties;
import digital.inception.party.model.Party;
import digital.inception.party.model.PartySortBy;
import digital.inception.party.model.PartyType;
import digital.inception.party.model.Person;
import digital.inception.party.model.PersonSortBy;
import digital.inception.party.model.Persons;
import digital.inception.party.model.Snapshots;
import jakarta.validation.ConstraintViolation;
import java.time.LocalDate;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

/**
 * The {@code PartyService} interface defines the functionality provided by a Party Service
 * implementation.
 *
 * @author Marcus Portmann
 */
@SuppressWarnings("unused")
public interface PartyService {

  /** The ID for the default tenant. */
  UUID DEFAULT_TENANT_ID = UUID.fromString("00000000-0000-0000-0000-000000000000");

  /**
   * Create the association.
   *
   * @param tenantId the ID for the tenant
   * @param association the association
   * @return the association
   * @throws InvalidArgumentException if an argument is invalid
   * @throws DuplicateAssociationException if the association already exists
   * @throws PartyNotFoundException if one or more parties for the association could not be found
   * @throws ServiceUnavailableException if the association could not be created
   */
  Association createAssociation(UUID tenantId, Association association)
      throws InvalidArgumentException,
          DuplicateAssociationException,
          PartyNotFoundException,
          ServiceUnavailableException;

  /**
   * Create the mandate.
   *
   * @param tenantId the ID for the tenant
   * @param mandate the mandate
   * @return the mandate
   * @throws InvalidArgumentException if an argument is invalid
   * @throws DuplicateMandateException if the mandate already exists
   * @throws PartyNotFoundException if one or more parties for the mandate could not be found
   * @throws ServiceUnavailableException if the mandate could not be created
   */
  Mandate createMandate(UUID tenantId, Mandate mandate)
      throws InvalidArgumentException,
          DuplicateMandateException,
          PartyNotFoundException,
          ServiceUnavailableException;

  /**
   * Create the organization.
   *
   * @param tenantId the ID for the tenant
   * @param organization the organization
   * @return the organization
   * @throws InvalidArgumentException if an argument is invalid
   * @throws DuplicateOrganizationException if the organization already exists
   * @throws ServiceUnavailableException if the organization could not be created
   */
  Organization createOrganization(UUID tenantId, Organization organization)
      throws InvalidArgumentException, DuplicateOrganizationException, ServiceUnavailableException;

  /**
   * Create the person.
   *
   * @param tenantId the ID for the tenant
   * @param person the person
   * @return the person
   * @throws InvalidArgumentException if an argument is invalid
   * @throws DuplicatePersonException if the person already exists
   * @throws ServiceUnavailableException if the person could not be created
   */
  Person createPerson(UUID tenantId, Person person)
      throws InvalidArgumentException, DuplicatePersonException, ServiceUnavailableException;

  /**
   * Delete the association.
   *
   * @param tenantId the ID for the tenant
   * @param associationId the ID for the association
   * @throws InvalidArgumentException if an argument is invalid
   * @throws AssociationNotFoundException if the association could not be found
   * @throws ServiceUnavailableException if the association could not be deleted
   */
  void deleteAssociation(UUID tenantId, UUID associationId)
      throws InvalidArgumentException, AssociationNotFoundException, ServiceUnavailableException;

  /**
   * Delete the mandate.
   *
   * @param tenantId the ID for the tenant
   * @param mandateId the ID for the mandate
   * @throws InvalidArgumentException if an argument is invalid
   * @throws MandateNotFoundException if the mandate could not be found
   * @throws ServiceUnavailableException if the mandate could not be deleted
   */
  void deleteMandate(UUID tenantId, UUID mandateId)
      throws InvalidArgumentException, MandateNotFoundException, ServiceUnavailableException;

  /**
   * Delete the organization.
   *
   * @param tenantId the ID for the tenant
   * @param organizationId the ID for the organization
   * @throws InvalidArgumentException if an argument is invalid
   * @throws OrganizationNotFoundException if the organization could not be found
   * @throws ServiceUnavailableException if the organization could not be deleted
   */
  void deleteOrganization(UUID tenantId, UUID organizationId)
      throws InvalidArgumentException, OrganizationNotFoundException, ServiceUnavailableException;

  /**
   * Delete the party.
   *
   * @param tenantId the ID for the tenant
   * @param partyId the ID for the party
   * @throws InvalidArgumentException if an argument is invalid
   * @throws PartyNotFoundException if the party could not be found
   * @throws ServiceUnavailableException if the party could not be deleted
   */
  void deleteParty(UUID tenantId, UUID partyId)
      throws InvalidArgumentException, PartyNotFoundException, ServiceUnavailableException;

  /**
   * Delete the person.
   *
   * @param tenantId the ID for the tenant
   * @param personId the ID for the person
   * @throws InvalidArgumentException if an argument is invalid
   * @throws PersonNotFoundException if the person could not be found
   * @throws ServiceUnavailableException if the person could not be deleted
   */
  void deletePerson(UUID tenantId, UUID personId)
      throws InvalidArgumentException, PersonNotFoundException, ServiceUnavailableException;

  /**
   * Retrieve the association.
   *
   * @param tenantId the ID for the tenant
   * @param associationId the ID for the association
   * @return the association
   * @throws InvalidArgumentException if an argument is invalid
   * @throws AssociationNotFoundException if the association could not be found
   * @throws ServiceUnavailableException if the association could not be retrieved
   */
  Association getAssociation(UUID tenantId, UUID associationId)
      throws InvalidArgumentException, AssociationNotFoundException, ServiceUnavailableException;

  /**
   * Retrieve the associations for the party.
   *
   * @param tenantId the ID for the tenant
   * @param partyId the ID for the party
   * @param sortBy the method used to sort the associations e.g. by type
   * @param sortDirection the sort direction to apply to the associations
   * @param pageIndex the page index
   * @param pageSize the page size
   * @return the associations
   * @throws InvalidArgumentException if an argument is invalid
   * @throws PartyNotFoundException if the party could not be found
   * @throws ServiceUnavailableException if the associations could not be retrieved
   */
  AssociationsForParty getAssociationsForParty(
      UUID tenantId,
      UUID partyId,
      AssociationSortBy sortBy,
      SortDirection sortDirection,
      Integer pageIndex,
      Integer pageSize)
      throws InvalidArgumentException, PartyNotFoundException, ServiceUnavailableException;

  /**
   * Retrieve the mandate.
   *
   * @param tenantId the ID for the tenant
   * @param mandateId the ID for the mandate
   * @return the mandate
   * @throws InvalidArgumentException if an argument is invalid
   * @throws MandateNotFoundException if the mandate could not be found
   * @throws ServiceUnavailableException if the mandate could not be retrieved
   */
  Mandate getMandate(UUID tenantId, UUID mandateId)
      throws InvalidArgumentException, MandateNotFoundException, ServiceUnavailableException;

  /**
   * Retrieve the mandates for the party.
   *
   * @param tenantId the ID for the tenant
   * @param partyId the ID for the party
   * @param sortBy the method used to sort the mandates e.g. by type
   * @param sortDirection the sort direction to apply to the mandates
   * @param pageIndex the page index
   * @param pageSize the page size
   * @return the mandates
   * @throws InvalidArgumentException if an argument is invalid
   * @throws PartyNotFoundException if the party could not be found
   * @throws ServiceUnavailableException if the mandates could not be retrieved
   */
  MandatesForParty getMandatesForParty(
      UUID tenantId,
      UUID partyId,
      MandateSortBy sortBy,
      SortDirection sortDirection,
      Integer pageIndex,
      Integer pageSize)
      throws InvalidArgumentException, PartyNotFoundException, ServiceUnavailableException;

  /**
   * Retrieve the organization.
   *
   * @param tenantId the ID for the tenant
   * @param organizationId the ID for the organization
   * @return the organization
   * @throws InvalidArgumentException if an argument is invalid
   * @throws OrganizationNotFoundException if the organization could not be found
   * @throws ServiceUnavailableException if the organization could not be retrieved
   */
  Organization getOrganization(UUID tenantId, UUID organizationId)
      throws InvalidArgumentException, OrganizationNotFoundException, ServiceUnavailableException;

  /**
   * Retrieve the organizations.
   *
   * @param tenantId the ID for the tenant
   * @param filter the filter to apply to the organizations
   * @param sortBy the method used to sort the organizations e.g. by name
   * @param sortDirection the sort direction to apply to the organizations
   * @param pageIndex the page index
   * @param pageSize the page size
   * @return the organizations
   * @throws InvalidArgumentException if an argument is invalid
   * @throws ServiceUnavailableException if the organizations could not be retrieved
   */
  Organizations getOrganizations(
      UUID tenantId,
      String filter,
      OrganizationSortBy sortBy,
      SortDirection sortDirection,
      Integer pageIndex,
      Integer pageSize)
      throws InvalidArgumentException, ServiceUnavailableException;

  /**
   * Retrieve the parties.
   *
   * @param tenantId the ID for the tenant
   * @param filter the filter to apply to the parties
   * @param sortBy the method used to sort the parties e.g. by name
   * @param sortDirection the sort direction to apply to the parties
   * @param pageIndex the page index
   * @param pageSize the page size
   * @return the parties
   * @throws InvalidArgumentException if an argument is invalid
   * @throws ServiceUnavailableException if the parties could not be retrieved
   */
  Parties getParties(
      UUID tenantId,
      String filter,
      PartySortBy sortBy,
      SortDirection sortDirection,
      Integer pageIndex,
      Integer pageSize)
      throws InvalidArgumentException, ServiceUnavailableException;

  /**
   * Retrieve the party.
   *
   * @param tenantId the ID for the tenant
   * @param partyId the ID for the party
   * @return the party
   * @throws InvalidArgumentException if an argument is invalid
   * @throws PartyNotFoundException if the party could not be found
   * @throws ServiceUnavailableException if the party could not be retrieved
   */
  Party getParty(UUID tenantId, UUID partyId)
      throws InvalidArgumentException, PartyNotFoundException, ServiceUnavailableException;

  /**
   * Retrieve the person.
   *
   * @param tenantId the ID for the tenant
   * @param personId the ID for the person
   * @return the person
   * @throws InvalidArgumentException if an argument is invalid
   * @throws PersonNotFoundException if the person could not be found
   * @throws ServiceUnavailableException if the person could not be retrieved
   */
  Person getPerson(UUID tenantId, UUID personId)
      throws InvalidArgumentException, PersonNotFoundException, ServiceUnavailableException;

  /**
   * Retrieve the persons.
   *
   * @param tenantId the ID for the tenant
   * @param filter the filter to apply to the persons
   * @param sortBy the method used to sort the persons e.g. by name
   * @param sortDirection the sort direction to apply to the persons
   * @param pageIndex the page index
   * @param pageSize the page size
   * @return the persons
   * @throws InvalidArgumentException if an argument is invalid
   * @throws ServiceUnavailableException if the persons could not be retrieved
   */
  Persons getPersons(
      UUID tenantId,
      String filter,
      PersonSortBy sortBy,
      SortDirection sortDirection,
      Integer pageIndex,
      Integer pageSize)
      throws InvalidArgumentException, ServiceUnavailableException;

  /**
   * Retrieve the snapshots for an entity.
   *
   * @param tenantId the ID for the tenant
   * @param entityType the type of entity
   * @param entityId the ID for the entity
   * @param from the date to retrieve the snapshots from
   * @param to the date to retrieve the snapshots to
   * @param sortDirection the sort direction to apply to the snapshots
   * @param pageIndex the page index
   * @param pageSize the page size
   * @return the snapshots
   * @throws InvalidArgumentException if an argument is invalid
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
      throws InvalidArgumentException, ServiceUnavailableException;

  /**
   * Retrieve the ID for the tenant the party is associated with.
   *
   * @param partyId the ID for the party
   * @return an Optional containing the ID for the tenant the party is associated with or an empty
   *     Optional if the party could not be found
   * @throws InvalidArgumentException if an argument is invalid
   * @throws ServiceUnavailableException if the ID for the tenant the party is associated with could
   *     not be retrieved
   */
  Optional<UUID> getTenantIdForParty(UUID partyId)
      throws InvalidArgumentException, ServiceUnavailableException;

  /**
   * Retrieve the party type for the party.
   *
   * @param tenantId the ID for the tenant
   * @param partyId the ID for the party
   * @return an Optional containing the party type for the party or an empty Optional if the party
   *     could not be found
   * @throws InvalidArgumentException if an argument is invalid
   * @throws ServiceUnavailableException if the party type for the party could not be retrieved
   */
  Optional<PartyType> getTypeForParty(UUID tenantId, UUID partyId)
      throws InvalidArgumentException, ServiceUnavailableException;

  /**
   * Update the association.
   *
   * @param tenantId the ID for the tenant
   * @param association the association
   * @return the association
   * @throws InvalidArgumentException if an argument is invalid
   * @throws AssociationNotFoundException if the association could not be found
   * @throws PartyNotFoundException if one or more parties for the association could not be found
   * @throws ServiceUnavailableException if the association could not be updated
   */
  Association updateAssociation(UUID tenantId, Association association)
      throws InvalidArgumentException,
          AssociationNotFoundException,
          PartyNotFoundException,
          ServiceUnavailableException;

  /**
   * Update the mandate.
   *
   * @param tenantId the ID for the tenant
   * @param mandate the mandate
   * @return the mandate
   * @throws InvalidArgumentException if an argument is invalid
   * @throws MandateNotFoundException if the mandate could not be found
   * @throws PartyNotFoundException if one or more parties for the mandate could not be found
   * @throws ServiceUnavailableException if the mandate could not be updated
   */
  Mandate updateMandate(UUID tenantId, Mandate mandate)
      throws InvalidArgumentException,
          MandateNotFoundException,
          PartyNotFoundException,
          ServiceUnavailableException;

  /**
   * Update the organization.
   *
   * @param tenantId the ID for the tenant
   * @param organization the organization
   * @return the organization
   * @throws InvalidArgumentException if an argument is invalid
   * @throws OrganizationNotFoundException if the organization could not be found
   * @throws ServiceUnavailableException if the organization could not be updated
   */
  Organization updateOrganization(UUID tenantId, Organization organization)
      throws InvalidArgumentException, OrganizationNotFoundException, ServiceUnavailableException;

  /**
   * Update the person.
   *
   * @param tenantId the ID for the tenant
   * @param person the person
   * @return the person
   * @throws InvalidArgumentException if an argument is invalid
   * @throws PersonNotFoundException if the person could not be found
   * @throws ServiceUnavailableException if the person could not be updated
   */
  Person updatePerson(UUID tenantId, Person person)
      throws InvalidArgumentException, PersonNotFoundException, ServiceUnavailableException;

  /**
   * Validate the association.
   *
   * @param tenantId the ID for the tenant
   * @param association the association
   * @return the constraint violations for the association
   * @throws ServiceUnavailableException if the association could not be validated
   */
  Set<ConstraintViolation<Association>> validateAssociation(UUID tenantId, Association association)
      throws ServiceUnavailableException;

  /**
   * Validate the mandate.
   *
   * @param tenantId the ID for the tenant
   * @param mandate the mandate
   * @return the constraint violations for the mandate
   * @throws ServiceUnavailableException if the mandate could not be validated
   */
  Set<ConstraintViolation<Mandate>> validateMandate(UUID tenantId, Mandate mandate)
      throws ServiceUnavailableException;

  /**
   * Validate the organization.
   *
   * @param tenantId the ID for the tenant
   * @param organization the organization
   * @return the constraint violations for the organization
   * @throws ServiceUnavailableException if the organization could not be validated
   */
  Set<ConstraintViolation<Organization>> validateOrganization(
      UUID tenantId, Organization organization) throws ServiceUnavailableException;

  /**
   * Validate the party.
   *
   * @param tenantId the ID for the tenant
   * @param party the party
   * @return the constraint violations for the party
   * @throws ServiceUnavailableException if the party could not be validated
   */
  Set<ConstraintViolation<Party>> validateParty(UUID tenantId, Party party)
      throws ServiceUnavailableException;

  /**
   * Validate the person.
   *
   * @param tenantId the ID for the tenant
   * @param person the person
   * @return the constraint violations for the person
   * @throws ServiceUnavailableException if the person could not be validated
   */
  Set<ConstraintViolation<Person>> validatePerson(UUID tenantId, Person person)
      throws ServiceUnavailableException;
}
