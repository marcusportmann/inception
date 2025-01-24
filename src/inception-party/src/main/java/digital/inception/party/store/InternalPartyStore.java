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

package digital.inception.party.store;

import com.fasterxml.jackson.databind.ObjectMapper;
import digital.inception.core.service.InvalidArgumentException;
import digital.inception.core.service.ServiceUnavailableException;
import digital.inception.core.sorting.SortDirection;
import digital.inception.party.model.Association;
import digital.inception.party.model.AssociationNotFoundException;
import digital.inception.party.model.AssociationSortBy;
import digital.inception.party.model.AssociationsForParty;
import digital.inception.party.model.DuplicateAssociationException;
import digital.inception.party.model.DuplicateMandateException;
import digital.inception.party.model.DuplicateOrganizationException;
import digital.inception.party.model.DuplicatePersonException;
import digital.inception.party.model.EntityType;
import digital.inception.party.model.Mandatary;
import digital.inception.party.model.Mandate;
import digital.inception.party.model.MandateNotFoundException;
import digital.inception.party.model.MandateSortBy;
import digital.inception.party.model.MandatesForParty;
import digital.inception.party.model.Organization;
import digital.inception.party.model.OrganizationNotFoundException;
import digital.inception.party.model.OrganizationSortBy;
import digital.inception.party.model.Organizations;
import digital.inception.party.model.Parties;
import digital.inception.party.model.Party;
import digital.inception.party.model.PartyNotFoundException;
import digital.inception.party.model.PartySortBy;
import digital.inception.party.model.PartyType;
import digital.inception.party.model.Person;
import digital.inception.party.model.PersonNotFoundException;
import digital.inception.party.model.PersonSortBy;
import digital.inception.party.model.Persons;
import digital.inception.party.model.Snapshot;
import digital.inception.party.model.Snapshots;
import digital.inception.party.persistence.AssociationRepository;
import digital.inception.party.persistence.MandateRepository;
import digital.inception.party.persistence.OrganizationRepository;
import digital.inception.party.persistence.PartyRepository;
import digital.inception.party.persistence.PersonRepository;
import digital.inception.party.persistence.SnapshotRepository;
import jakarta.persistence.criteria.Predicate;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.context.annotation.Conditional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * The <b>InternalPartyStore</b> class provides the internal party store implementation.
 *
 * @author Marcus Portmann
 */
@Service
@Conditional(InternalPartyStoreEnabledCondition.class)
@SuppressWarnings("unused")
public class InternalPartyStore implements PartyStore {

  /** The Association Repository. */
  private final AssociationRepository associationRepository;

  /** The Mandate Repository. */
  private final MandateRepository mandateRepository;

  /** The Jackson 2 object mapper */
  private final ObjectMapper objectMapper;

  /** The Organization Repository. */
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
   * @param mandateRepository the Mandate Repository
   * @param organizationRepository the Organization Repository
   * @param partyRepository the Party Repository
   * @param personRepository the Person Repository
   * @param associationRepository the Association Repository
   * @param snapshotRepository the Snapshot Repository
   */
  public InternalPartyStore(
      ObjectMapper objectMapper,
      MandateRepository mandateRepository,
      OrganizationRepository organizationRepository,
      PartyRepository partyRepository,
      PersonRepository personRepository,
      AssociationRepository associationRepository,
      SnapshotRepository snapshotRepository) {
    this.objectMapper = objectMapper;
    this.mandateRepository = mandateRepository;
    this.organizationRepository = organizationRepository;
    this.partyRepository = partyRepository;
    this.personRepository = personRepository;
    this.associationRepository = associationRepository;
    this.snapshotRepository = snapshotRepository;
  }

  @Override
  public Association createAssociation(UUID tenantId, Association association)
      throws DuplicateAssociationException, PartyNotFoundException, ServiceUnavailableException {
    try {
      if (associationRepository.existsById(association.getId())) {
        throw new DuplicateAssociationException(association.getId());
      }

      if (!partyRepository.existsByTenantIdAndId(tenantId, association.getFirstPartyId())) {
        throw new PartyNotFoundException(tenantId, association.getFirstPartyId());
      }

      if (!partyRepository.existsByTenantIdAndId(tenantId, association.getSecondPartyId())) {
        throw new PartyNotFoundException(tenantId, association.getSecondPartyId());
      }

      // Serialize the association object as JSON
      String associationJson = objectMapper.writeValueAsString(association);

      associationRepository.saveAndFlush(association);

      snapshotRepository.saveAndFlush(
          new Snapshot(tenantId, EntityType.ASSOCIATION, association.getId(), associationJson));

      return association;
    } catch (DuplicateAssociationException | PartyNotFoundException e) {
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

  @Override
  public Mandate createMandate(UUID tenantId, Mandate mandate)
      throws DuplicateMandateException, PartyNotFoundException, ServiceUnavailableException {
    try {
      if (mandateRepository.existsById(mandate.getId())) {
        throw new DuplicateMandateException(mandate.getId());
      }

      for (Mandatary mandatary : mandate.getMandataries()) {
        if (!partyRepository.existsByTenantIdAndId(tenantId, mandatary.getPartyId())) {
          throw new PartyNotFoundException(tenantId, mandatary.getPartyId());
        }
      }

      // Serialize the mandate object as JSON
      String mandateJson = objectMapper.writeValueAsString(mandate);

      mandateRepository.saveAndFlush(mandate);

      snapshotRepository.saveAndFlush(
          new Snapshot(tenantId, EntityType.MANDATE, mandate.getId(), mandateJson));

      return mandate;
    } catch (DuplicateMandateException | PartyNotFoundException e) {
      throw e;
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to create the mandate ("
              + mandate.getId()
              + ") for the tenant ("
              + tenantId
              + ")",
          e);
    }
  }

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
          new Snapshot(tenantId, EntityType.ORGANIZATION, organization.getId(), organizationJson));

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

      snapshotRepository.saveAndFlush(
          new Snapshot(tenantId, EntityType.PERSON, person.getId(), personJson));

      return person;
    } catch (DuplicatePersonException e) {
      throw e;
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to create the person (" + person.getId() + ") for the tenant (" + tenantId + ")",
          e);
    }
  }

  @Override
  public void deleteAssociation(UUID tenantId, UUID associationId)
      throws AssociationNotFoundException, ServiceUnavailableException {
    try {
      if (!associationRepository.existsByTenantIdAndId(tenantId, associationId)) {
        throw new AssociationNotFoundException(tenantId, associationId);
      }

      associationRepository.deleteByTenantIdAndId(tenantId, associationId);
    } catch (AssociationNotFoundException e) {
      throw e;
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to delete the association ("
              + associationId
              + ") for the tenant ("
              + tenantId
              + ")",
          e);
    }
  }

  @Override
  public void deleteMandate(UUID tenantId, UUID mandateId)
      throws MandateNotFoundException, ServiceUnavailableException {
    try {
      if (!mandateRepository.existsByTenantIdAndId(tenantId, mandateId)) {
        throw new MandateNotFoundException(tenantId, mandateId);
      }

      mandateRepository.deleteByTenantIdAndId(tenantId, mandateId);
    } catch (MandateNotFoundException e) {
      throw e;
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to delete the mandate (" + mandateId + ") for the tenant (" + tenantId + ")", e);
    }
  }

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

  @Override
  public Association getAssociation(UUID tenantId, UUID associationId)
      throws AssociationNotFoundException, ServiceUnavailableException {
    try {
      Optional<Association> associationOptional =
          associationRepository.findByTenantIdAndId(tenantId, associationId);

      if (associationOptional.isPresent()) {
        return associationOptional.get();
      } else {
        throw new AssociationNotFoundException(tenantId, associationId);
      }
    } catch (AssociationNotFoundException e) {
      throw e;
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to retrieve the association ("
              + associationId
              + ") for the tenant ("
              + tenantId
              + ")",
          e);
    }
  }

  @Override
  public AssociationsForParty getAssociationsForParty(
      UUID tenantId,
      UUID partyId,
      AssociationSortBy sortBy,
      SortDirection sortDirection,
      Integer pageIndex,
      Integer pageSize)
      throws PartyNotFoundException, ServiceUnavailableException {
    try {
      if (!partyRepository.existsByTenantIdAndId(tenantId, partyId)) {
        throw new PartyNotFoundException(tenantId, partyId);
      }

      PageRequest pageRequest;

      if (sortBy == AssociationSortBy.TYPE) {
        pageRequest =
            PageRequest.of(
                pageIndex,
                pageSize,
                (sortDirection == SortDirection.ASCENDING) ? Direction.ASC : Direction.DESC,
                "type");
      } else {
        pageRequest =
            PageRequest.of(
                pageIndex,
                pageSize,
                (sortDirection == SortDirection.ASCENDING) ? Direction.ASC : Direction.DESC,
                "type");
      }

      Page<Association> associationPage =
          associationRepository.findByTenantIdAndPartyId(tenantId, partyId, pageRequest);

      return new AssociationsForParty(
          tenantId,
          partyId,
          associationPage.toList(),
          associationPage.getTotalElements(),
          sortBy,
          sortDirection,
          pageIndex,
          pageSize);
    } catch (PartyNotFoundException e) {
      throw e;
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to retrieve the associations for the party ("
              + partyId
              + ") for the tenant ("
              + tenantId
              + ")",
          e);
    }
  }

  @Override
  public Mandate getMandate(UUID tenantId, UUID mandateId)
      throws MandateNotFoundException, ServiceUnavailableException {
    try {
      Optional<Mandate> mandateOptional =
          mandateRepository.findByTenantIdAndId(tenantId, mandateId);

      if (mandateOptional.isPresent()) {
        return mandateOptional.get();
      } else {
        throw new MandateNotFoundException(tenantId, mandateId);
      }
    } catch (MandateNotFoundException e) {
      throw e;
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to retrieve the mandate (" + mandateId + ") for the tenant (" + tenantId + ")",
          e);
    }
  }

  @Override
  public MandatesForParty getMandatesForParty(
      UUID tenantId,
      UUID partyId,
      MandateSortBy sortBy,
      SortDirection sortDirection,
      Integer pageIndex,
      Integer pageSize)
      throws PartyNotFoundException, ServiceUnavailableException {
    try {
      if (!partyRepository.existsByTenantIdAndId(tenantId, partyId)) {
        throw new PartyNotFoundException(tenantId, partyId);
      }

      PageRequest pageRequest;

      if (sortBy == MandateSortBy.TYPE) {
        pageRequest =
            PageRequest.of(
                pageIndex,
                pageSize,
                (sortDirection == SortDirection.ASCENDING) ? Direction.ASC : Direction.DESC,
                "type");
      } else {
        pageRequest =
            PageRequest.of(
                pageIndex,
                pageSize,
                (sortDirection == SortDirection.ASCENDING) ? Direction.ASC : Direction.DESC,
                "type");
      }

      Page<Mandate> mandatePage =
          mandateRepository.findByTenantIdAndPartyId(tenantId, partyId, pageRequest);

      return new MandatesForParty(
          tenantId,
          partyId,
          mandatePage.toList(),
          mandatePage.getTotalElements(),
          sortBy,
          sortDirection,
          pageIndex,
          pageSize);
    } catch (PartyNotFoundException e) {
      throw e;
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to retrieve the mandates for the party ("
              + partyId
              + ") for the tenant ("
              + tenantId
              + ")",
          e);
    }
  }

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
      String sortProperty;
      if (sortBy == OrganizationSortBy.NAME) {
        sortProperty = "name";
      } else {
        sortProperty = "name";
      }

      PageRequest pageRequest =
          PageRequest.of(
              pageIndex,
              pageSize,
              (sortDirection == SortDirection.ASCENDING) ? Direction.ASC : Direction.DESC,
              sortProperty);

      Page<Organization> organizationPage =
          organizationRepository.findAll(
              (Specification<Organization>)
                  (root, query, criteriaBuilder) -> {
                    List<Predicate> predicates = new ArrayList<>();

                    predicates.add(criteriaBuilder.equal(root.get("tenantId"), tenantId));

                    if (StringUtils.hasText(filter)) {
                      predicates.add(
                          criteriaBuilder.like(
                              criteriaBuilder.lower(root.get("name")),
                              "%" + filter.toLowerCase() + "%"));
                    }

                    return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
                  },
              pageRequest);

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

  @Override
  public Parties getParties(
      UUID tenantId,
      String filter,
      PartySortBy sortBy,
      SortDirection sortDirection,
      Integer pageIndex,
      Integer pageSize)
      throws ServiceUnavailableException {

    try {
      PageRequest pageRequest =
          PageRequest.of(
              pageIndex,
              pageSize,
              (sortDirection == SortDirection.ASCENDING) ? Direction.ASC : Direction.DESC,
              "name");

      Page<Party> partyPage =
          partyRepository.findAll(
              (Specification<Party>)
                  (root, query, criteriaBuilder) -> {
                    List<Predicate> predicates = new ArrayList<>();

                    predicates.add(criteriaBuilder.equal(root.get("tenantId"), tenantId));

                    if (StringUtils.hasText(filter)) {
                      predicates.add(
                          criteriaBuilder.like(
                              criteriaBuilder.lower(root.get("name")),
                              "%" + filter.toLowerCase() + "%"));
                    }

                    return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
                  },
              pageRequest);

      return new Parties(
          tenantId,
          partyPage.toList(),
          partyPage.getTotalElements(),
          filter,
          sortBy,
          sortDirection,
          pageIndex,
          pageSize);
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to retrieve the filtered parties for the tenant (" + tenantId + ")", e);
    }
  }

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
      String sortProperty;
      if (sortBy == PersonSortBy.PREFERRED_NAME) {
        sortProperty = "preferredName";
      } else {
        sortProperty = "name";
      }

      PageRequest pageRequest =
          PageRequest.of(
              pageIndex,
              pageSize,
              (sortDirection == SortDirection.ASCENDING) ? Direction.ASC : Direction.DESC,
              sortProperty);

      Page<Person> personPage =
          personRepository.findAll(
              (Specification<Person>)
                  (root, query, criteriaBuilder) -> {
                    List<Predicate> predicates = new ArrayList<>();

                    predicates.add(criteriaBuilder.equal(root.get("tenantId"), tenantId));

                    if (StringUtils.hasText(filter)) {
                      predicates.add(
                          criteriaBuilder.like(
                              criteriaBuilder.lower(root.get("name")),
                              "%" + filter.toLowerCase() + "%"));
                    }

                    return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
                  },
              pageRequest);

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
            snapshotRepository.findByTenantIdAndEntityTypeAndEntityId(
                tenantId, entityType, entityId, pageRequest);
      } else if (from != null) {
        snapshotPage =
            snapshotRepository.findByTenantIdAndEntityTypeAndEntityId(
                tenantId, entityType, entityId, pageRequest);
      } else if (to != null) {
        snapshotPage =
            snapshotRepository.findByTenantIdAndEntityTypeAndEntityId(
                tenantId, entityType, entityId, pageRequest);
      } else {
        snapshotPage =
            snapshotRepository.findByTenantIdAndEntityTypeAndEntityId(
                tenantId, entityType, entityId, pageRequest);
      }

      return new Snapshots(
          tenantId,
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

  @Override
  public Optional<UUID> getTenantIdForParty(UUID partyId) throws ServiceUnavailableException {
    try {
      return partyRepository.getTenantIdByPartyId(partyId);
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to retrieve the tenant ID for the party (" + partyId + ")", e);
    }
  }

  @Override
  public Optional<PartyType> getTypeForParty(UUID tenantId, UUID partyId)
      throws InvalidArgumentException, ServiceUnavailableException {
    try {
      return partyRepository.getTypeByTenantIdAndPartyId(tenantId, partyId);
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to retrieve the type for the party ("
              + partyId
              + ") for the tenant ("
              + tenantId
              + ")",
          e);
    }
  }

  @Override
  public Association updateAssociation(UUID tenantId, Association association)
      throws AssociationNotFoundException, ServiceUnavailableException {
    try {
      if (!associationRepository.existsByTenantIdAndId(tenantId, association.getId())) {
        throw new AssociationNotFoundException(tenantId, association.getId());
      }

      // Serialize the association object as JSON
      String associationJson = objectMapper.writeValueAsString(association);

      associationRepository.saveAndFlush(association);

      snapshotRepository.saveAndFlush(
          new Snapshot(tenantId, EntityType.ASSOCIATION, association.getId(), associationJson));

      return association;
    } catch (AssociationNotFoundException e) {
      throw e;
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to update the association ("
              + association.getId()
              + ") for the tenant ("
              + tenantId
              + ")",
          e);
    }
  }

  @Override
  public Mandate updateMandate(UUID tenantId, Mandate mandate)
      throws MandateNotFoundException, PartyNotFoundException, ServiceUnavailableException {
    try {
      if (!mandateRepository.existsByTenantIdAndId(tenantId, mandate.getId())) {
        throw new MandateNotFoundException(tenantId, mandate.getId());
      }

      for (Mandatary mandatary : mandate.getMandataries()) {
        if (!partyRepository.existsByTenantIdAndId(tenantId, mandatary.getPartyId())) {
          throw new PartyNotFoundException(tenantId, mandatary.getPartyId());
        }
      }

      // Serialize the mandate object as JSON
      String mandateJson = objectMapper.writeValueAsString(mandate);

      mandateRepository.saveAndFlush(mandate);

      snapshotRepository.saveAndFlush(
          new Snapshot(tenantId, EntityType.MANDATE, mandate.getId(), mandateJson));

      return mandate;
    } catch (MandateNotFoundException | PartyNotFoundException e) {
      throw e;
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to update the mandate ("
              + mandate.getId()
              + ") for the tenant ("
              + tenantId
              + ")",
          e);
    }
  }

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
          new Snapshot(tenantId, EntityType.ORGANIZATION, organization.getId(), organizationJson));

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

      snapshotRepository.saveAndFlush(
          new Snapshot(tenantId, EntityType.PERSON, person.getId(), personJson));

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
