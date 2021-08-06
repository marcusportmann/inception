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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * The <b>PartyService</b> class provides the Party Service implementation.
 *
 * @author Marcus Portmann
 */
@Service
@SuppressWarnings("unused")
public class PartyService implements IPartyService {

  /* Logger */
  private static final Logger logger = LoggerFactory.getLogger(PartyService.class);

  /** The Spring application context. */
  private final ApplicationContext applicationContext;

  /** The JSR-303 validator. */
  private final Validator validator;

  /** The party data store. */
  private IPartyDataStore dataStore;

  /** The fully qualified name of the Java class that implements the party data store. */
  @Value("${inception.party.data-store.class-name:digital.inception.party.InternalPartyDataStore}")
  private String dataStoreClassName;

  /** The maximum number of filtered organizations that will be returned by the data store. */
  @Value("${inception.party.max-filtered-organizations:#{100}}")
  private int maxFilteredOrganizations;

  /** The maximum number of filtered parties that will be returned by the data store. */
  @Value("${inception.party.max-filtered-parties:#{100}}")
  private int maxFilteredParties;

  /** The maximum number of filtered persons that will be returned by the data store. */
  @Value("${inception.party.max-filtered-persons:#{100}}")
  private int maxFilteredPersons;

  /** The maximum number of snapshots for a party that will be returned by the data store. */
  @Value("${inception.party.max-snapshots:#{100}}")
  private int maxSnapshots;

  /** The internal reference to the Party Service to enable caching. */
  @Resource private IPartyService self;

  /**
   * Constructs a new <b>PartyService</b>.
   *
   * @param applicationContext the Spring application context
   * @param validator the JSR-303 validator
   */
  public PartyService(ApplicationContext applicationContext, Validator validator) {

    this.applicationContext = applicationContext;
    this.validator = validator;
  }

  /**
   * Create the new association.
   *
   * @param tenantId the Universally Unique Identifier (UUID) for the tenant
   * @param association the association
   * @return the association
   */
  @Override
  @Transactional
  @CachePut(cacheNames = "associations", key = "#association.id")
  public Association createAssociation(UUID tenantId, Association association)
      throws InvalidArgumentException, DuplicateAssociationException, ServiceUnavailableException {
    if (association == null) {
      throw new InvalidArgumentException("association");
    }

    if (!Objects.equals(tenantId, association.getTenantId())) {
      throw new InvalidArgumentException("association.tenantId");
    }

    Set<ConstraintViolation<Association>> constraintViolations =
        validateAssociation(tenantId, association);

    if (!constraintViolations.isEmpty()) {
      throw new InvalidArgumentException(
          "association", ValidationError.toValidationErrors(constraintViolations));
    }

    return getDataStore().createAssociation(tenantId, association);
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

    return getDataStore().createOrganization(tenantId, organization);
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

    return getDataStore().createPerson(tenantId, person);
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

    getDataStore().deleteOrganization(tenantId, organizationId);
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

    getDataStore().deleteParty(tenantId, partyId);
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

    getDataStore().deletePerson(tenantId, personId);
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

    return getDataStore().getOrganization(tenantId, organizationId);
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

    if (pageIndex == null) {
      pageIndex = 0;
    }

    if (pageSize == null) {
      pageSize = maxFilteredOrganizations;
    } else {
      pageSize = Math.min(pageSize, maxFilteredOrganizations);
    }

    return getDataStore()
        .getOrganizations(tenantId, filter, sortBy, sortDirection, pageIndex, pageSize);
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
      pageSize = maxFilteredParties;
    } else {
      pageSize = Math.min(pageSize, maxFilteredParties);
    }

    return getDataStore().getParties(tenantId, filter, sortDirection, pageIndex, pageSize);
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

    return getDataStore().getParty(tenantId, partyId);
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

    return getDataStore().getPerson(tenantId, personId);
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

    if (pageIndex == null) {
      pageIndex = 0;
    }

    if (pageSize == null) {
      pageSize = maxFilteredPersons;
    } else {
      pageSize = Math.min(pageSize, maxFilteredPersons);
    }

    return getDataStore().getPersons(tenantId, filter, sortBy, sortDirection, pageIndex, pageSize);
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
  @Transactional
  public Snapshots getSnapshots(
      UUID tenantId,
      EntityType entityType,
      UUID entityId,
      LocalDate from,
      LocalDate to,
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

    if (sortDirection == null) {
      sortDirection = SortDirection.ASCENDING;
    }

    if (pageIndex == null) {
      pageIndex = 0;
    }

    if (pageSize == null) {
      pageSize = maxSnapshots;
    } else {
      pageSize = Math.min(pageSize, maxSnapshots);
    }

    return getDataStore()
        .getSnapshots(tenantId, entityType, entityId, from, to, sortDirection, pageIndex, pageSize);
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

    return getDataStore().getTenantIdForParty(partyId);
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

    return getDataStore().updateOrganization(tenantId, organization);
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

    return getDataStore().updatePerson(tenantId, person);
  }

  /**
   * Validate the association.
   *
   * @param tenantId the Universally Unique Identifier (UUID) for the tenant
   * @param association the association
   * @return the constraint violations for the association
   */
  @Override
  public Set<ConstraintViolation<Association>> validateAssociation(
      UUID tenantId, Association association) {
    return validator.validate(association);
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

  /**
   * Retrieve the party data store.
   *
   * @return the party data store
   */
  private IPartyDataStore getDataStore() throws ServiceUnavailableException {
    if (dataStore == null) {
      try {
        Class<?> clazz =
            Thread.currentThread().getContextClassLoader().loadClass(dataStoreClassName);

        if (!IPartyDataStore.class.isAssignableFrom(clazz)) {
          throw new ServiceUnavailableException(
              "The party data store class ("
                  + dataStoreClassName
                  + ") does not implement the IPartyDataStore interface");
        }

        Class<? extends IPartyDataStore> dataStoreClass = clazz.asSubclass(IPartyDataStore.class);

        dataStore = applicationContext.getAutowireCapableBeanFactory().createBean(dataStoreClass);

        return dataStore;
      } catch (Throwable e) {
        throw new ServiceUnavailableException(
            "Failed to initialize the party data store (" + dataStoreClassName + ")", e);
      }
    } else {
      return dataStore;
    }
  }
}
