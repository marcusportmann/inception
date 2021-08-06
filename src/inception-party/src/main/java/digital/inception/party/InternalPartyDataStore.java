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

import com.fasterxml.jackson.databind.ObjectMapper;
import digital.inception.core.service.ServiceUnavailableException;
import digital.inception.core.sorting.SortDirection;
import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.util.StringUtils;

/**
 * The <b>InternalPartyDataStore</b> class provides the internal party data store implementation.
 *
 * @author Marcus Portmann
 */
@SuppressWarnings("unused")
public class InternalPartyDataStore implements IPartyDataStore {

  /* Logger */
  private static final Logger logger = LoggerFactory.getLogger(InternalPartyDataStore.class);

  /** The Association Repository. */
  private final AssociationRepository associationRepository;

  /** The Jackson 2 object mapper */
  private final ObjectMapper objectMapper;

  /** /** The Organization Repository. */
  private final OrganizationRepository organizationRepository;

  /** The Party Repository. */
  private final PartyRepository partyRepository;

  /** The Person Repository. */
  private final PersonRepository personRepository;

  /** The Snapshot Repository. */
  private final SnapshotRepository snapshotRepository;

  /**
   * Constructs a new <b>InternalPartyDataStore</b>.
   *
   * @param objectMapper the Jackson2 object mapper
   * @param organizationRepository the Organization Repository
   * @param partyRepository the Party Repository
   * @param personRepository the Person Repository
   * @param associationRepository the Association Repository
   * @param snapshotRepository the Snapshot Repository
   */
  public InternalPartyDataStore(
      ObjectMapper objectMapper,
      OrganizationRepository organizationRepository,
      PartyRepository partyRepository,
      PersonRepository personRepository,
      AssociationRepository associationRepository,
      SnapshotRepository snapshotRepository) {

    this.objectMapper = objectMapper;
    this.organizationRepository = organizationRepository;
    this.partyRepository = partyRepository;
    this.personRepository = personRepository;
    this.associationRepository = associationRepository;
    this.snapshotRepository = snapshotRepository;
  }

  /**
   * Create the new association.
   *
   * @param tenantId the Universally Unique Identifier (UUID) for the tenant
   * @param association the association
   * @return the association
   */
  @Override
  public Association createAssociation(UUID tenantId, Association association)
      throws DuplicateAssociationException, ServiceUnavailableException {
    try {
      if (associationRepository.existsById(association.getId())) {
        throw new DuplicateAssociationException(association.getId());
      }

      // Serialize the association object as JSON
      String associationJson = objectMapper.writeValueAsString(association);

      associationRepository.saveAndFlush(association);

      snapshotRepository.saveAndFlush(
          new Snapshot(EntityType.ASSOCIATION, association.getId(), associationJson));

      return association;
    } catch (DuplicateAssociationException e) {
      throw e;
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to create the association ("
              + association.getId()
              + ") for the tenant ("
              + tenantId
              + ")",
          e);
    }
  }

  /**
   * Create the new organization.
   *
   * @param tenantId the Universally Unique Identifier (UUID) for the tenant
   * @param organization the organization
   * @return the organization
   */
  @Override
  public Organization createOrganization(UUID tenantId, Organization organization)
      throws DuplicateOrganizationException, ServiceUnavailableException {
    try {
      if (organizationRepository.existsById(organization.getId())) {
        throw new DuplicateOrganizationException(organization.getId());
      }

      // Serialize the organization object as JSON
      String organizationJson = objectMapper.writeValueAsString(organization);

      organizationRepository.saveAndFlush(organization);

      snapshotRepository.saveAndFlush(
          new Snapshot(EntityType.ORGANIZATION, organization.getId(), organizationJson));

      return organization;
    } catch (DuplicateOrganizationException e) {
      throw e;
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to create the organization ("
              + organization.getId()
              + ") for the tenant ("
              + tenantId
              + ")",
          e);
    }
  }

  /**
   * Create the new person.
   *
   * @param tenantId the Universally Unique Identifier (UUID) for the tenant
   * @param person the person
   */
  @Override
  public Person createPerson(UUID tenantId, Person person)
      throws DuplicatePersonException, ServiceUnavailableException {
    try {
      if (personRepository.existsById(person.getId())) {
        throw new DuplicatePersonException(person.getId());
      }

      // Serialize the person object as JSON
      String personJson = objectMapper.writeValueAsString(person);

      personRepository.saveAndFlush(person);

      snapshotRepository.saveAndFlush(new Snapshot(EntityType.PERSON, person.getId(), personJson));

      return person;
    } catch (DuplicatePersonException e) {
      throw e;
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to create the person (" + person.getId() + ") for the tenant (" + tenantId + ")",
          e);
    }
  }

  /**
   * Delete the organization.
   *
   * @param tenantId the Universally Unique Identifier (UUID) for the tenant
   * @param organizationId the Universally Unique Identifier (UUID) for the organization
   */
  @Override
  public void deleteOrganization(UUID tenantId, UUID organizationId)
      throws OrganizationNotFoundException, ServiceUnavailableException {
    try {
      if (!organizationRepository.existsByTenantIdAndId(tenantId, organizationId)) {
        throw new OrganizationNotFoundException(tenantId, organizationId);
      }

      organizationRepository.deleteByTenantIdAndId(tenantId, organizationId);
    } catch (OrganizationNotFoundException e) {
      throw e;
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to delete the organization ("
              + organizationId
              + ") for the tenant ("
              + tenantId
              + ")",
          e);
    }
  }

  /**
   * Delete the party.
   *
   * @param tenantId the Universally Unique Identifier (UUID) for the tenant
   * @param partyId the Universally Unique Identifier (UUID) for the party
   */
  @Override
  public void deleteParty(UUID tenantId, UUID partyId)
      throws PartyNotFoundException, ServiceUnavailableException {
    try {
      if (!partyRepository.existsByTenantIdAndId(tenantId, partyId)) {
        throw new PartyNotFoundException(tenantId, partyId);
      }

      partyRepository.deleteByTenantIdAndId(tenantId, partyId);
    } catch (PartyNotFoundException e) {
      throw e;
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to delete the party (" + partyId + ") for the tenant (" + tenantId + ")", e);
    }
  }

  /**
   * Delete the person.
   *
   * @param tenantId the Universally Unique Identifier (UUID) for the tenant
   * @param personId the Universally Unique Identifier (UUID) for the person
   */
  @Override
  public void deletePerson(UUID tenantId, UUID personId)
      throws PersonNotFoundException, ServiceUnavailableException {
    try {
      if (!personRepository.existsByTenantIdAndId(tenantId, personId)) {
        throw new PersonNotFoundException(tenantId, personId);
      }

      personRepository.deleteByTenantIdAndId(tenantId, personId);
    } catch (PersonNotFoundException e) {
      throw e;
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to delete the person (" + personId + ") for the tenant (" + tenantId + ")", e);
    }
  }

  /**
   * Retrieve the organization.
   *
   * @param tenantId the Universally Unique Identifier (UUID) for the tenant
   * @param organizationId the Universally Unique Identifier (UUID) for the organization
   * @return the organization
   */
  @Override
  public Organization getOrganization(UUID tenantId, UUID organizationId)
      throws OrganizationNotFoundException, ServiceUnavailableException {
    try {
      Optional<Organization> organizationOptional =
          organizationRepository.findByTenantIdAndId(tenantId, organizationId);

      if (organizationOptional.isPresent()) {
        return organizationOptional.get();
      } else {
        throw new OrganizationNotFoundException(tenantId, organizationId);
      }
    } catch (OrganizationNotFoundException e) {
      throw e;
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to retrieve the organization ("
              + organizationId
              + ") for the tenant ("
              + tenantId
              + ")",
          e);
    }
  }

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
  @Override
  public Organizations getOrganizations(
      UUID tenantId,
      String filter,
      OrganizationSortBy sortBy,
      SortDirection sortDirection,
      Integer pageIndex,
      Integer pageSize)
      throws ServiceUnavailableException {
    try {
      PageRequest pageRequest;

      if (sortBy == OrganizationSortBy.NAME) {
        pageRequest =
            PageRequest.of(
                pageIndex,
                pageSize,
                (sortDirection == SortDirection.ASCENDING) ? Direction.ASC : Direction.DESC,
                "name");
      } else {
        pageRequest =
            PageRequest.of(
                pageIndex,
                pageSize,
                (sortDirection == SortDirection.ASCENDING) ? Direction.ASC : Direction.DESC,
                "name");
      }

      Page<Organization> organizationPage;
      if (StringUtils.hasText(filter)) {
        organizationPage = organizationRepository.findFiltered("%" + filter + "%", pageRequest);
      } else {
        organizationPage = organizationRepository.findByTenantId(tenantId, pageRequest);
      }

      return new Organizations(
          tenantId,
          organizationPage.toList(),
          organizationPage.getTotalElements(),
          filter,
          sortBy,
          sortDirection,
          pageIndex,
          pageSize);
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to retrieve the filtered organizations for the tenant (" + tenantId + ")", e);
    }
  }

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
  @Override
  public Parties getParties(
      UUID tenantId,
      String filter,
      SortDirection sortDirection,
      Integer pageIndex,
      Integer pageSize)
      throws ServiceUnavailableException {

    try {
      PageRequest pageRequest = PageRequest.of(pageIndex, pageSize);

      Page<Party> partyPage;
      if (StringUtils.hasText(filter)) {
        partyPage = partyRepository.findFiltered("%" + filter + "%", pageRequest);
      } else {
        partyPage = partyRepository.findByTenantId(tenantId, pageRequest);
      }

      return new Parties(
          tenantId,
          partyPage.toList(),
          partyPage.getTotalElements(),
          filter,
          sortDirection,
          pageIndex,
          pageSize);
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to retrieve the filtered parties for the tenant (" + tenantId + ")", e);
    }
  }

  /**
   * Retrieve the party.
   *
   * @param tenantId the Universally Unique Identifier (UUID) for the tenant
   * @param partyId the Universally Unique Identifier (UUID) for the party
   * @return the party
   */
  @Override
  public Party getParty(UUID tenantId, UUID partyId)
      throws PartyNotFoundException, ServiceUnavailableException {
    try {
      Optional<Party> partyOptional = partyRepository.findByTenantIdAndId(tenantId, partyId);

      if (partyOptional.isPresent()) {
        return partyOptional.get();
      } else {
        throw new PartyNotFoundException(tenantId, partyId);
      }
    } catch (PartyNotFoundException e) {
      throw e;
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to retrieve the party (" + partyId + ") for the tenant (" + tenantId + ")", e);
    }
  }

  /**
   * Retrieve the person.
   *
   * @param tenantId the Universally Unique Identifier (UUID) for the tenant
   * @param personId the Universally Unique Identifier (UUID) for the person
   * @return the person
   */
  @Override
  public Person getPerson(UUID tenantId, UUID personId)
      throws PersonNotFoundException, ServiceUnavailableException {
    try {
      Optional<Person> personOptional = personRepository.findByTenantIdAndId(tenantId, personId);

      if (personOptional.isPresent()) {
        return personOptional.get();
      } else {
        throw new PersonNotFoundException(tenantId, personId);
      }
    } catch (PersonNotFoundException e) {
      throw e;
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to retrieve the person (" + personId + ") for the tenant (" + tenantId + ")", e);
    }
  }

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
  @Override
  public Persons getPersons(
      UUID tenantId,
      String filter,
      PersonSortBy sortBy,
      SortDirection sortDirection,
      Integer pageIndex,
      Integer pageSize)
      throws ServiceUnavailableException {
    try {
      PageRequest pageRequest;

      if (sortBy == PersonSortBy.PREFERRED_NAME) {
        pageRequest =
            PageRequest.of(
                pageIndex,
                pageSize,
                (sortDirection == SortDirection.ASCENDING) ? Direction.ASC : Direction.DESC,
                "preferredName");
      } else {
        pageRequest =
            PageRequest.of(
                pageIndex,
                pageSize,
                (sortDirection == SortDirection.ASCENDING) ? Direction.ASC : Direction.DESC,
                "name");
      }

      Page<Person> personPage;
      if (StringUtils.hasText(filter)) {
        personPage = personRepository.findFiltered("%" + filter + "%", pageRequest);
      } else {
        personPage = personRepository.findByTenantId(tenantId, pageRequest);
      }

      return new Persons(
          tenantId,
          personPage.toList(),
          personPage.getTotalElements(),
          filter,
          sortBy,
          sortDirection,
          pageIndex,
          pageSize);
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to retrieve the filtered persons for the tenant (" + tenantId + ")", e);
    }
  }

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
  @Override
  public Snapshots getSnapshots(
      UUID tenantId,
      EntityType entityType,
      UUID entityId,
      LocalDate from,
      LocalDate to,
      SortDirection sortDirection,
      Integer pageIndex,
      Integer pageSize)
      throws ServiceUnavailableException {
    try {
      PageRequest pageRequest;

      pageRequest =
          PageRequest.of(
              pageIndex,
              pageSize,
              (sortDirection == SortDirection.ASCENDING) ? Direction.ASC : Direction.DESC,
              "timestamp");

      Page<Snapshot> snapshotPage;

      if ((from != null) && (to != null)) {
        snapshotPage =
            snapshotRepository.findByEntityTypeAndEntityId(entityType, entityId, pageRequest);
      } else if (from != null) {
        snapshotPage =
            snapshotRepository.findByEntityTypeAndEntityId(entityType, entityId, pageRequest);
      } else if (to != null) {
        snapshotPage =
            snapshotRepository.findByEntityTypeAndEntityId(entityType, entityId, pageRequest);
      } else {
        snapshotPage =
            snapshotRepository.findByEntityTypeAndEntityId(entityType, entityId, pageRequest);
      }

      return new Snapshots(
          snapshotPage.toList(),
          snapshotPage.getTotalElements(),
          entityType,
          entityId,
          from,
          to,
          sortDirection,
          pageIndex,
          pageSize);
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to retrieve the snapshots for the entity ("
              + entityId
              + ") of type ("
              + entityType.code()
              + ") for the tenant ("
              + tenantId
              + ")",
          e);
    }
  }

  /**
   * Retrieve the Universally Unique Identifier (UUID) for the tenant the party is associated with.
   *
   * @param partyId the Universally Unique Identifier (UUID) for the party
   * @return an Optional containing the Universally Unique Identifier (UUID) for the tenant the
   *     party is associated with or an empty Optional if the party could not be found
   */
  @Override
  public Optional<UUID> getTenantIdForParty(UUID partyId) throws ServiceUnavailableException {
    try {
      return partyRepository.getTenantIdByPartyId(partyId);
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to retrieve the tenant ID for the party (" + partyId + ")", e);
    }
  }

  /**
   * Update the organization.
   *
   * @param tenantId the Universally Unique Identifier (UUID) for the tenant
   * @param organization the organization
   * @return the organization
   */
  @Override
  public Organization updateOrganization(UUID tenantId, Organization organization)
      throws OrganizationNotFoundException, ServiceUnavailableException {
    try {
      if (!organizationRepository.existsByTenantIdAndId(tenantId, organization.getId())) {
        throw new OrganizationNotFoundException(tenantId, organization.getId());
      }

      // Serialize the organization object as JSON
      String organizationJson = objectMapper.writeValueAsString(organization);

      organizationRepository.saveAndFlush(organization);

      snapshotRepository.saveAndFlush(
          new Snapshot(EntityType.ORGANIZATION, organization.getId(), organizationJson));

      return organization;
    } catch (OrganizationNotFoundException e) {
      throw e;
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to update the organization ("
              + organization.getId()
              + ") for the tenant ("
              + tenantId
              + ")",
          e);
    }
  }

  /**
   * Update the person.
   *
   * @param tenantId the Universally Unique Identifier (UUID) for the tenant
   * @param person the person
   * @return the person
   */
  @Override
  public Person updatePerson(UUID tenantId, Person person)
      throws PersonNotFoundException, ServiceUnavailableException {
    try {
      if (!personRepository.existsByTenantIdAndId(tenantId, person.getId())) {
        throw new PersonNotFoundException(tenantId, person.getId());
      }

      // Serialize the person object as JSON
      String personJson = objectMapper.writeValueAsString(person);

      personRepository.saveAndFlush(person);

      snapshotRepository.saveAndFlush(new Snapshot(EntityType.PERSON, person.getId(), personJson));

      return person;
    } catch (PersonNotFoundException e) {
      throw e;
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to update the person (" + person.getId() + ") for the tenant (" + tenantId + ")",
          e);
    }
  }
}
