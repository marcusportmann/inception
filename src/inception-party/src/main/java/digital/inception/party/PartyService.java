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
import com.fasterxml.jackson.databind.SerializationFeature;
import digital.inception.core.service.InvalidArgumentException;
import digital.inception.core.service.ServiceUnavailableException;
import digital.inception.core.service.ValidationError;
import digital.inception.core.sorting.SortDirection;
import java.time.LocalDate;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import javax.annotation.Resource;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.ApplicationContext;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

/**
 * The <b>PartyService</b> class provides the Party Service implementation.
 *
 * @author Marcus Portmann
 */
@Service
@SuppressWarnings("unused")
public class PartyService implements IPartyService {

  /** The maximum number of filtered organizations. */
  private static final int MAX_FILTERED_ORGANISATIONS = 100;

  /** The maximum number of filtered parties. */
  private static final int MAX_FILTERED_PARTIES = 100;

  /** The maximum number of filtered persons. */
  private static final int MAX_FILTERED_PERSONS = 100;

  /** The maximum number of snapshots. */
  private static final int MAX_PARTY_SNAPSHOTS = 100;

  /* Logger */
  private static final Logger logger = LoggerFactory.getLogger(PartyService.class);

  /** The Spring application context. */
  private final ApplicationContext applicationContext;

  /** The Jackson2 Object Mapper Builder. */
  private final Jackson2ObjectMapperBuilder jackson2ObjectMapperBuilder;

  /** /** The Organization Repository. */
  private final OrganizationRepository organizationRepository;

  /** The Party Repository. */
  private final PartyRepository partyRepository;

  /** The Person Repository. */
  private final PersonRepository personRepository;

  /** The Snapshot Repository. */
  private final SnapshotRepository snapshotRepository;

  /** The JSR-303 validator. */
  private final Validator validator;

  /** The internal reference to the Party Service to enable caching. */
  @Resource private IPartyService self;

  /**
   * Constructs a new <b>PartyService</b>.
   *
   * @param applicationContext the Spring application context
   * @param validator the JSR-303 validator
   * @param organizationRepository the Organization Repository
   * @param partyRepository the Party Repository
   * @param personRepository the Person Repository
   * @param snapshotRepository the Snapshot Repository
   */
  public PartyService(
      ApplicationContext applicationContext,
      Jackson2ObjectMapperBuilder jackson2ObjectMapperBuilder,
      Validator validator,
      OrganizationRepository organizationRepository,
      PartyRepository partyRepository,
      PersonRepository personRepository,
      SnapshotRepository snapshotRepository) {
    this.applicationContext = applicationContext;
    this.jackson2ObjectMapperBuilder = jackson2ObjectMapperBuilder;
    this.validator = validator;
    this.organizationRepository = organizationRepository;
    this.partyRepository = partyRepository;
    this.personRepository = personRepository;
    this.snapshotRepository = snapshotRepository;
  }

  /**
   * Create the new organization.
   *
   * @param tenantId the Universally Unique Identifier (UUID) for the tenant
   * @param organization the organization
   * @return the organization
   */
  @Override
  @Transactional
  @CachePut(cacheNames = "organizations", key = "#organization.id")
  public Organization createOrganization(UUID tenantId, Organization organization)
      throws InvalidArgumentException, DuplicateOrganizationException, ServiceUnavailableException {
    if (organization == null) {
      throw new InvalidArgumentException("organization");
    }

    if (!Objects.equals(tenantId, organization.getTenantId())) {
      throw new InvalidArgumentException("organization.tenantId");
    }

    Set<ConstraintViolation<Organization>> constraintViolations =
        validateOrganization(tenantId, organization);

    if (!constraintViolations.isEmpty()) {
      throw new InvalidArgumentException(
          "organization", ValidationError.toValidationErrors(constraintViolations));
    }

    try {
      if (organizationRepository.existsById(organization.getId())) {
        throw new DuplicateOrganizationException(organization.getId());
      }

      // Serialize the organization object as JSON
      ObjectMapper objectMapper =
          jackson2ObjectMapperBuilder.build().disable(SerializationFeature.INDENT_OUTPUT);

      String organizationJson = objectMapper.writeValueAsString(organization);

      organizationRepository.saveAndFlush(organization);

      snapshotRepository.saveAndFlush(new Snapshot(organization.getId(), organizationJson));

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
  @Transactional
  @CachePut(cacheNames = "persons", key = "#person.id")
  public Person createPerson(UUID tenantId, Person person)
      throws InvalidArgumentException, DuplicatePersonException, ServiceUnavailableException {
    if (person == null) {
      throw new InvalidArgumentException("person");
    }

    if (!Objects.equals(tenantId, person.getTenantId())) {
      throw new InvalidArgumentException("person.tenantId");
    }

    Set<ConstraintViolation<Person>> constraintViolations = validatePerson(tenantId, person);

    if (!constraintViolations.isEmpty()) {
      throw new InvalidArgumentException(
          "person", ValidationError.toValidationErrors(constraintViolations));
    }

    try {
      if (personRepository.existsById(person.getId())) {
        throw new DuplicatePersonException(person.getId());
      }

      // Serialize the person object as JSON
      ObjectMapper objectMapper =
          jackson2ObjectMapperBuilder.build().disable(SerializationFeature.INDENT_OUTPUT);

      String personJson = objectMapper.writeValueAsString(person);

      personRepository.saveAndFlush(person);

      snapshotRepository.saveAndFlush(new Snapshot(person.getId(), personJson));

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
  @Transactional
  @CacheEvict(cacheNames = "organizations", key = "#organizationId")
  public void deleteOrganization(UUID tenantId, UUID organizationId)
      throws InvalidArgumentException, OrganizationNotFoundException, ServiceUnavailableException {
    if (organizationId == null) {
      throw new InvalidArgumentException("organizationId");
    }

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
  @Transactional
  @CacheEvict(
      cacheNames = {"organizations", "persons"},
      key = "#partyId")
  public void deleteParty(UUID tenantId, UUID partyId)
      throws InvalidArgumentException, PartyNotFoundException, ServiceUnavailableException {
    if (partyId == null) {
      throw new InvalidArgumentException("partyId");
    }

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
  @Transactional
  @CacheEvict(cacheNames = "persons", key = "#personId")
  public void deletePerson(UUID tenantId, UUID personId)
      throws InvalidArgumentException, PersonNotFoundException, ServiceUnavailableException {
    if (personId == null) {
      throw new InvalidArgumentException("personId");
    }

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
  @Cacheable(cacheNames = "organizations", key = "#organizationId")
  public Organization getOrganization(UUID tenantId, UUID organizationId)
      throws InvalidArgumentException, OrganizationNotFoundException, ServiceUnavailableException {
    if (organizationId == null) {
      throw new InvalidArgumentException("organizationId");
    }

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
      throws InvalidArgumentException, ServiceUnavailableException {
    if ((pageIndex != null) && (pageIndex < 0)) {
      throw new InvalidArgumentException("pageIndex");
    }

    if ((pageSize != null) && (pageSize <= 0)) {
      throw new InvalidArgumentException("pageSize");
    }

    if (sortBy == null) {
      sortBy = OrganizationSortBy.NAME;
    }

    if (sortDirection == null) {
      sortDirection = SortDirection.ASCENDING;
    }

    try {
      PageRequest pageRequest;

      if (pageIndex == null) {
        pageIndex = 0;
      }

      if (pageSize == null) {
        pageSize = MAX_FILTERED_ORGANISATIONS;
      }

      if (sortBy == OrganizationSortBy.NAME) {
        pageRequest =
            PageRequest.of(
                pageIndex,
                Math.min(pageSize, MAX_FILTERED_ORGANISATIONS),
                (sortDirection == SortDirection.ASCENDING) ? Direction.ASC : Direction.DESC,
                "name");
      } else {
        pageRequest =
            PageRequest.of(
                pageIndex,
                Math.min(pageSize, MAX_FILTERED_ORGANISATIONS),
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
      throws InvalidArgumentException, ServiceUnavailableException {
    if ((pageIndex != null) && (pageIndex < 0)) {
      throw new InvalidArgumentException("pageIndex");
    }

    if ((pageSize != null) && (pageSize <= 0)) {
      throw new InvalidArgumentException("pageSize");
    }

    if (pageIndex == null) {
      pageIndex = 0;
    }

    if (pageSize == null) {
      pageSize = MAX_FILTERED_PARTIES;
    }

    PageRequest pageRequest = PageRequest.of(pageIndex, Math.min(pageSize, MAX_FILTERED_PARTIES));

    try {

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
      throws InvalidArgumentException, PartyNotFoundException, ServiceUnavailableException {
    if (partyId == null) {
      throw new InvalidArgumentException("partyId");
    }

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
  @Cacheable(cacheNames = "persons", key = "#personId")
  public Person getPerson(UUID tenantId, UUID personId)
      throws InvalidArgumentException, PersonNotFoundException, ServiceUnavailableException {
    if (personId == null) {
      throw new InvalidArgumentException("personId");
    }

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
      throws InvalidArgumentException, ServiceUnavailableException {
    if ((pageIndex != null) && (pageIndex < 0)) {
      throw new InvalidArgumentException("pageIndex");
    }

    if ((pageSize != null) && (pageSize <= 0)) {
      throw new InvalidArgumentException("pageSize");
    }

    if (sortBy == null) {
      sortBy = PersonSortBy.NAME;
    }

    if (sortDirection == null) {
      sortDirection = SortDirection.ASCENDING;
    }

    try {
      PageRequest pageRequest;

      if (pageIndex == null) {
        pageIndex = 0;
      }

      if (pageSize == null) {
        pageSize = MAX_FILTERED_PERSONS;
      }

      if (sortBy == PersonSortBy.PREFERRED_NAME) {
        pageRequest =
            PageRequest.of(
                pageIndex,
                Math.min(pageSize, MAX_FILTERED_PERSONS),
                (sortDirection == SortDirection.ASCENDING) ? Direction.ASC : Direction.DESC,
                "preferredName");
      } else {
        pageRequest =
            PageRequest.of(
                pageIndex,
                Math.min(pageSize, MAX_FILTERED_PERSONS),
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
   * Retrieve the snapshots for the party.
   *
   * @param tenantId the Universally Unique Identifier (UUID) for the tenant
   * @param partyId the Universally Unique Identifier (UUID) for the party
   * @param from the optional date to retrieve the snapshots from
   * @param to the optional date to retrieve the snapshots to
   * @param sortDirection the optional sort direction to apply to the snapshots
   * @param pageIndex the optional page index
   * @param pageSize the optional page size
   * @return the snapshots
   */
  @Override
  @Transactional
  public Snapshots getSnapshots(
      UUID tenantId,
      UUID partyId,
      LocalDate from,
      LocalDate to,
      SortDirection sortDirection,
      Integer pageIndex,
      Integer pageSize)
      throws InvalidArgumentException, PartyNotFoundException, ServiceUnavailableException {
    if ((pageIndex != null) && (pageIndex < 0)) {
      throw new InvalidArgumentException("pageIndex");
    }

    if ((pageSize != null) && (pageSize <= 0)) {
      throw new InvalidArgumentException("pageSize");
    }

    if (sortDirection == null) {
      sortDirection = SortDirection.ASCENDING;
    }

    try {
      Optional<UUID> partyTenantId = self.getTenantIdForParty(partyId);

      if (partyTenantId.isEmpty() || (!Objects.equals(tenantId, partyTenantId.get()))) {
        throw new PartyNotFoundException(tenantId, partyId);
      }

      PageRequest pageRequest;

      if (pageIndex == null) {
        pageIndex = 0;
      }

      if (pageSize == null) {
        pageSize = MAX_FILTERED_PERSONS;
      }

      pageRequest =
          PageRequest.of(
              pageIndex,
              Math.min(pageSize, MAX_FILTERED_PERSONS),
              (sortDirection == SortDirection.ASCENDING) ? Direction.ASC : Direction.DESC,
              "timestamp");

      Page<Snapshot> partySnapshotPage;

      if ((from != null) && (to != null)) {
        partySnapshotPage = snapshotRepository.findByPartyId(partyId, pageRequest);
      } else if (from != null) {
        partySnapshotPage = snapshotRepository.findByPartyId(partyId, pageRequest);
      } else if (to != null) {
        partySnapshotPage = snapshotRepository.findByPartyId(partyId, pageRequest);
      } else {
        partySnapshotPage = snapshotRepository.findByPartyId(partyId, pageRequest);
      }

      return new Snapshots(
          partySnapshotPage.toList(),
          partySnapshotPage.getTotalElements(),
          partyId,
          from,
          to,
          sortDirection,
          pageIndex,
          pageSize);
    } catch (PartyNotFoundException e) {
      throw e;
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to retrieve the snapshots for the party ("
              + partyId
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
  @Cacheable(cacheNames = "partyTenantIds", key = "#partyId")
  public Optional<UUID> getTenantIdForParty(UUID partyId)
      throws InvalidArgumentException, ServiceUnavailableException {
    if (partyId == null) {
      throw new InvalidArgumentException("partyId");
    }

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
  @Transactional
  @CachePut(cacheNames = "organizations", key = "#organization.id")
  public Organization updateOrganization(UUID tenantId, Organization organization)
      throws InvalidArgumentException, OrganizationNotFoundException, ServiceUnavailableException {
    if (organization == null) {
      throw new InvalidArgumentException("organization");
    }

    Set<ConstraintViolation<Organization>> constraintViolations =
        validateOrganization(tenantId, organization);

    if (!constraintViolations.isEmpty()) {
      throw new InvalidArgumentException(
          "organization", ValidationError.toValidationErrors(constraintViolations));
    }

    try {
      if (!organizationRepository.existsByTenantIdAndId(tenantId, organization.getId())) {
        throw new OrganizationNotFoundException(tenantId, organization.getId());
      }

      // Serialize the organization object as JSON
      ObjectMapper objectMapper =
          jackson2ObjectMapperBuilder.build().disable(SerializationFeature.INDENT_OUTPUT);

      String organizationJson = objectMapper.writeValueAsString(organization);

      organizationRepository.saveAndFlush(organization);

      snapshotRepository.saveAndFlush(new Snapshot(organization.getId(), organizationJson));

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
  @Transactional
  @CachePut(cacheNames = "persons", key = "#person.id")
  public Person updatePerson(UUID tenantId, Person person)
      throws InvalidArgumentException, PersonNotFoundException, ServiceUnavailableException {
    if (person == null) {
      throw new InvalidArgumentException("person");
    }

    Set<ConstraintViolation<Person>> constraintViolations = validatePerson(tenantId, person);

    if (!constraintViolations.isEmpty()) {
      throw new InvalidArgumentException(
          "person", ValidationError.toValidationErrors(constraintViolations));
    }

    try {
      if (!personRepository.existsByTenantIdAndId(tenantId, person.getId())) {
        throw new PersonNotFoundException(tenantId, person.getId());
      }

      // Serialize the person object as JSON
      ObjectMapper objectMapper =
          jackson2ObjectMapperBuilder.build().disable(SerializationFeature.INDENT_OUTPUT);

      String personJson = objectMapper.writeValueAsString(person);

      personRepository.saveAndFlush(person);

      snapshotRepository.saveAndFlush(new Snapshot(person.getId(), personJson));

      return person;
    } catch (PersonNotFoundException e) {
      throw e;
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to update the person (" + person.getId() + ") for the tenant (" + tenantId + ")",
          e);
    }
  }

  /**
   * Validate the organization.
   *
   * @param tenantId the Universally Unique Identifier (UUID) for the tenant
   * @param organization the organization
   * @return the constraint violations for the organization
   */
  @Override
  public Set<ConstraintViolation<Organization>> validateOrganization(
      UUID tenantId, Organization organization) {
    return validator.validate(organization);
  }

  /**
   * Validate the party.
   *
   * @param tenantId the Universally Unique Identifier (UUID) for the tenant
   * @param party the party
   * @return the constraint violations for the party
   */
  @Override
  public Set<ConstraintViolation<Party>> validateParty(UUID tenantId, Party party) {
    return validator.validate(party);
  }

  /**
   * Validate the person.
   *
   * @param tenantId the Universally Unique Identifier (UUID) for the tenant
   * @param person the person
   * @return the constraint violations for the person
   */
  @Override
  public Set<ConstraintViolation<Person>> validatePerson(UUID tenantId, Person person) {
    return validator.validate(person);
  }
}
