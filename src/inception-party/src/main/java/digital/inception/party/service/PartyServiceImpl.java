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
import digital.inception.core.service.AbstractServiceBase;
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
import digital.inception.party.store.PartyStore;
import jakarta.validation.ConstraintViolation;
import java.time.LocalDate;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

/**
 * The {@code PartyServiceImpl} class provides the Party Service implementation.
 *
 * @author Marcus Portmann
 */
@Service
@SuppressWarnings("unused")
public class PartyServiceImpl extends AbstractServiceBase implements PartyService {

  /** The party store. */
  private final PartyStore partyStore;

  /** The maximum number of associations that will be returned by the data store. */
  @Value("${inception.party.max-associations:#{100}}")
  private int maxAssociations;

  /** The maximum number of filtered organizations that will be returned by the data store. */
  @Value("${inception.party.max-filtered-organizations:#{100}}")
  private int maxFilteredOrganizations;

  /** The maximum number of filtered parties that will be returned by the data store. */
  @Value("${inception.party.max-filtered-parties:#{100}}")
  private int maxFilteredParties;

  /** The maximum number of filtered persons that will be returned by the data store. */
  @Value("${inception.party.max-filtered-persons:#{100}}")
  private int maxFilteredPersons;

  /** The maximum number of mandates that will be returned by the data store. */
  @Value("${inception.party.max-mandates:#{100}}")
  private int maxMandates;

  /** The maximum number of snapshots for a party that will be returned by the data store. */
  @Value("${inception.party.max-snapshots:#{100}}")
  private int maxSnapshots;

  /** The internal reference to the Party Service to enable caching. */
  private PartyService partyService;

  /**
   * Constructs a new {@code PartyServiceImpl}.
   *
   * @param applicationContext the Spring application context
   * @param partyStore the Party Store
   */
  public PartyServiceImpl(ApplicationContext applicationContext, PartyStore partyStore) {
    super(applicationContext);

    this.partyStore = partyStore;
  }

  @Override
  public Association createAssociation(UUID tenantId, Association association)
      throws InvalidArgumentException,
          DuplicateAssociationException,
          PartyNotFoundException,
          ServiceUnavailableException {
    if (tenantId == null) {
      throw new InvalidArgumentException("tenantId");
    }

    validateArgument("association", association);

    if (!Objects.equals(tenantId, association.getTenantId())) {
      throw new InvalidArgumentException("association.tenantId");
    }

    return partyStore.createAssociation(tenantId, association);
  }

  @Override
  public Mandate createMandate(UUID tenantId, Mandate mandate)
      throws InvalidArgumentException,
          DuplicateMandateException,
          PartyNotFoundException,
          ServiceUnavailableException {
    if (tenantId == null) {
      throw new InvalidArgumentException("tenantId");
    }

    validateArgument("mandate", mandate);

    if (!Objects.equals(tenantId, mandate.getTenantId())) {
      throw new InvalidArgumentException("mandate.tenantId");
    }

    return partyStore.createMandate(tenantId, mandate);
  }

  @Override
  @CachePut(cacheNames = "organizations", key = "#organization.id")
  public Organization createOrganization(UUID tenantId, Organization organization)
      throws InvalidArgumentException, DuplicateOrganizationException, ServiceUnavailableException {
    if (tenantId == null) {
      throw new InvalidArgumentException("tenantId");
    }

    validateArgument("organization", organization);

    if (!Objects.equals(tenantId, organization.getTenantId())) {
      throw new InvalidArgumentException("organization.tenantId");
    }

    return partyStore.createOrganization(tenantId, organization);
  }

  @Override
  @CachePut(cacheNames = "persons", key = "#person.id")
  public Person createPerson(UUID tenantId, Person person)
      throws InvalidArgumentException, DuplicatePersonException, ServiceUnavailableException {
    if (tenantId == null) {
      throw new InvalidArgumentException("tenantId");
    }

    validateArgument("person", person);

    if (!Objects.equals(tenantId, person.getTenantId())) {
      throw new InvalidArgumentException("person.tenantId");
    }

    return partyStore.createPerson(tenantId, person);
  }

  @Override
  public void deleteAssociation(UUID tenantId, UUID associationId)
      throws InvalidArgumentException, AssociationNotFoundException, ServiceUnavailableException {
    if (tenantId == null) {
      throw new InvalidArgumentException("tenantId");
    }

    if (associationId == null) {
      throw new InvalidArgumentException("associationId");
    }

    partyStore.deleteAssociation(tenantId, associationId);
  }

  @Override
  public void deleteMandate(UUID tenantId, UUID mandateId)
      throws InvalidArgumentException, MandateNotFoundException, ServiceUnavailableException {
    if (tenantId == null) {
      throw new InvalidArgumentException("tenantId");
    }

    if (mandateId == null) {
      throw new InvalidArgumentException("mandateId");
    }

    partyStore.deleteMandate(tenantId, mandateId);
  }

  @Override
  @CacheEvict(
      cacheNames = {"organizations", "partyTenantIds", "partyTypes"},
      key = "#organizationId")
  public void deleteOrganization(UUID tenantId, UUID organizationId)
      throws InvalidArgumentException, OrganizationNotFoundException, ServiceUnavailableException {
    if (tenantId == null) {
      throw new InvalidArgumentException("tenantId");
    }

    if (organizationId == null) {
      throw new InvalidArgumentException("organizationId");
    }

    partyStore.deleteOrganization(tenantId, organizationId);
  }

  @Override
  @CacheEvict(
      cacheNames = {"organizations", "persons", "partyTenantIds", "partyTypes"},
      key = "#partyId")
  public void deleteParty(UUID tenantId, UUID partyId)
      throws InvalidArgumentException, PartyNotFoundException, ServiceUnavailableException {
    if (tenantId == null) {
      throw new InvalidArgumentException("tenantId");
    }

    if (partyId == null) {
      throw new InvalidArgumentException("partyId");
    }

    partyStore.deleteParty(tenantId, partyId);
  }

  @Override
  @CacheEvict(
      cacheNames = {"persons", "partyTenantIds", "partyTypes"},
      key = "#personId")
  public void deletePerson(UUID tenantId, UUID personId)
      throws InvalidArgumentException, PersonNotFoundException, ServiceUnavailableException {
    if (tenantId == null) {
      throw new InvalidArgumentException("tenantId");
    }

    if (personId == null) {
      throw new InvalidArgumentException("personId");
    }

    partyStore.deletePerson(tenantId, personId);
  }

  @Override
  public Association getAssociation(UUID tenantId, UUID associationId)
      throws InvalidArgumentException, AssociationNotFoundException, ServiceUnavailableException {
    if (tenantId == null) {
      throw new InvalidArgumentException("tenantId");
    }

    if (associationId == null) {
      throw new InvalidArgumentException("associationId");
    }

    return partyStore.getAssociation(tenantId, associationId);
  }

  @Override
  public AssociationsForParty getAssociationsForParty(
      UUID tenantId,
      UUID partyId,
      AssociationSortBy sortBy,
      SortDirection sortDirection,
      Integer pageIndex,
      Integer pageSize)
      throws InvalidArgumentException, PartyNotFoundException, ServiceUnavailableException {
    if (tenantId == null) {
      throw new InvalidArgumentException("tenantId");
    }

    if (partyId == null) {
      throw new InvalidArgumentException("partyId");
    }

    if ((pageIndex != null) && (pageIndex < 0)) {
      throw new InvalidArgumentException("pageIndex");
    }

    if ((pageSize != null) && (pageSize <= 0)) {
      throw new InvalidArgumentException("pageSize");
    }

    if (sortBy == null) {
      sortBy = AssociationSortBy.TYPE;
    }

    if (sortDirection == null) {
      sortDirection = SortDirection.ASCENDING;
    }

    if (pageIndex == null) {
      pageIndex = 0;
    }

    if (pageSize == null) {
      pageSize = maxAssociations;
    } else {
      pageSize = Math.min(pageSize, maxAssociations);
    }

    return partyStore.getAssociationsForParty(
        tenantId, partyId, sortBy, sortDirection, pageIndex, pageSize);
  }

  @Override
  public Mandate getMandate(UUID tenantId, UUID mandateId)
      throws InvalidArgumentException, MandateNotFoundException, ServiceUnavailableException {
    if (tenantId == null) {
      throw new InvalidArgumentException("tenantId");
    }

    if (mandateId == null) {
      throw new InvalidArgumentException("mandateId");
    }

    return partyStore.getMandate(tenantId, mandateId);
  }

  @Override
  public MandatesForParty getMandatesForParty(
      UUID tenantId,
      UUID partyId,
      MandateSortBy sortBy,
      SortDirection sortDirection,
      Integer pageIndex,
      Integer pageSize)
      throws InvalidArgumentException, PartyNotFoundException, ServiceUnavailableException {
    if (tenantId == null) {
      throw new InvalidArgumentException("tenantId");
    }

    if (partyId == null) {
      throw new InvalidArgumentException("partyId");
    }

    if ((pageIndex != null) && (pageIndex < 0)) {
      throw new InvalidArgumentException("pageIndex");
    }

    if ((pageSize != null) && (pageSize <= 0)) {
      throw new InvalidArgumentException("pageSize");
    }

    if (sortBy == null) {
      sortBy = MandateSortBy.TYPE;
    }

    if (sortDirection == null) {
      sortDirection = SortDirection.ASCENDING;
    }

    if (pageIndex == null) {
      pageIndex = 0;
    }

    if (pageSize == null) {
      pageSize = maxMandates;
    } else {
      pageSize = Math.min(pageSize, maxMandates);
    }

    return partyStore.getMandatesForParty(
        tenantId, partyId, sortBy, sortDirection, pageIndex, pageSize);
  }

  @Override
  @Cacheable(cacheNames = "organizations", key = "#organizationId")
  public Organization getOrganization(UUID tenantId, UUID organizationId)
      throws InvalidArgumentException, OrganizationNotFoundException, ServiceUnavailableException {
    if (tenantId == null) {
      throw new InvalidArgumentException("tenantId");
    }

    if (organizationId == null) {
      throw new InvalidArgumentException("organizationId");
    }

    return partyStore.getOrganization(tenantId, organizationId);
  }

  @Override
  public Organizations getOrganizations(
      UUID tenantId,
      String filter,
      OrganizationSortBy sortBy,
      SortDirection sortDirection,
      Integer pageIndex,
      Integer pageSize)
      throws InvalidArgumentException, ServiceUnavailableException {
    if (tenantId == null) {
      throw new InvalidArgumentException("tenantId");
    }

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

    return partyStore.getOrganizations(
        tenantId, filter, sortBy, sortDirection, pageIndex, pageSize);
  }

  @Override
  public Parties getParties(
      UUID tenantId,
      String filter,
      PartySortBy sortBy,
      SortDirection sortDirection,
      Integer pageIndex,
      Integer pageSize)
      throws InvalidArgumentException, ServiceUnavailableException {
    if (tenantId == null) {
      throw new InvalidArgumentException("tenantId");
    }

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

    return partyStore.getParties(tenantId, filter, sortBy, sortDirection, pageIndex, pageSize);
  }

  @Override
  public Party getParty(UUID tenantId, UUID partyId)
      throws InvalidArgumentException, PartyNotFoundException, ServiceUnavailableException {
    if (tenantId == null) {
      throw new InvalidArgumentException("tenantId");
    }

    if (partyId == null) {
      throw new InvalidArgumentException("partyId");
    }

    return partyStore.getParty(tenantId, partyId);
  }

  @Override
  @Cacheable(cacheNames = "persons", key = "#personId")
  public Person getPerson(UUID tenantId, UUID personId)
      throws InvalidArgumentException, PersonNotFoundException, ServiceUnavailableException {
    if (tenantId == null) {
      throw new InvalidArgumentException("tenantId");
    }

    if (personId == null) {
      throw new InvalidArgumentException("personId");
    }

    return partyStore.getPerson(tenantId, personId);
  }

  @Override
  public Persons getPersons(
      UUID tenantId,
      String filter,
      PersonSortBy sortBy,
      SortDirection sortDirection,
      Integer pageIndex,
      Integer pageSize)
      throws InvalidArgumentException, ServiceUnavailableException {
    if (tenantId == null) {
      throw new InvalidArgumentException("tenantId");
    }

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

    return partyStore.getPersons(tenantId, filter, sortBy, sortDirection, pageIndex, pageSize);
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
      throws InvalidArgumentException, ServiceUnavailableException {
    if (tenantId == null) {
      throw new InvalidArgumentException("tenantId");
    }

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

    return partyStore.getSnapshots(
        tenantId, entityType, entityId, from, to, sortDirection, pageIndex, pageSize);
  }

  @Override
  @Cacheable(cacheNames = "partyTenantIds", key = "#partyId")
  public Optional<UUID> getTenantIdForParty(UUID partyId)
      throws InvalidArgumentException, ServiceUnavailableException {
    if (partyId == null) {
      throw new InvalidArgumentException("partyId");
    }

    return partyStore.getTenantIdForParty(partyId);
  }

  @Override
  @Cacheable(cacheNames = "partyTypes", key = "#partyId")
  public Optional<PartyType> getTypeForParty(UUID tenantId, UUID partyId)
      throws InvalidArgumentException, ServiceUnavailableException {
    if (tenantId == null) {
      throw new InvalidArgumentException("tenantId");
    }

    if (partyId == null) {
      throw new InvalidArgumentException("partyId");
    }

    return partyStore.getTypeForParty(tenantId, partyId);
  }

  @Override
  public Association updateAssociation(UUID tenantId, Association association)
      throws InvalidArgumentException,
          AssociationNotFoundException,
          PartyNotFoundException,
          ServiceUnavailableException {
    if (tenantId == null) {
      throw new InvalidArgumentException("tenantId");
    }

    validateArgument("association", association);

    if (!Objects.equals(tenantId, association.getTenantId())) {
      throw new InvalidArgumentException("association.tenantId");
    }

    return partyStore.updateAssociation(tenantId, association);
  }

  @Override
  public Mandate updateMandate(UUID tenantId, Mandate mandate)
      throws InvalidArgumentException,
          MandateNotFoundException,
          PartyNotFoundException,
          ServiceUnavailableException {
    if (tenantId == null) {
      throw new InvalidArgumentException("tenantId");
    }

    validateArgument("mandate", mandate);

    if (!Objects.equals(tenantId, mandate.getTenantId())) {
      throw new InvalidArgumentException("mandate.tenantId");
    }

    return partyStore.updateMandate(tenantId, mandate);
  }

  @Override
  @CachePut(cacheNames = "organizations", key = "#organization.id")
  public Organization updateOrganization(UUID tenantId, Organization organization)
      throws InvalidArgumentException, OrganizationNotFoundException, ServiceUnavailableException {
    if (tenantId == null) {
      throw new InvalidArgumentException("tenantId");
    }

    validateArgument("organization", organization);

    if (!Objects.equals(tenantId, organization.getTenantId())) {
      throw new InvalidArgumentException("organization.tenantId");
    }

    return partyStore.updateOrganization(tenantId, organization);
  }

  @Override
  @CachePut(cacheNames = "persons", key = "#person.id")
  public Person updatePerson(UUID tenantId, Person person)
      throws InvalidArgumentException, PersonNotFoundException, ServiceUnavailableException {
    if (tenantId == null) {
      throw new InvalidArgumentException("tenantId");
    }

    validateArgument("person", person);

    if (!Objects.equals(tenantId, person.getTenantId())) {
      throw new InvalidArgumentException("person.tenantId");
    }

    return partyStore.updatePerson(tenantId, person);
  }

  @Override
  public Set<ConstraintViolation<Association>> validateAssociation(
      UUID tenantId, Association association) throws ServiceUnavailableException {
    try {
      return getValidator().validate(association);
    } catch (Throwable e) {
      throw new ServiceUnavailableException("Failed to validate the association", e);
    }
  }

  @Override
  public Set<ConstraintViolation<Mandate>> validateMandate(UUID tenantId, Mandate mandate)
      throws ServiceUnavailableException {
    try {
      return getValidator().validate(mandate);
    } catch (Throwable e) {
      throw new ServiceUnavailableException("Failed to validate the mandate", e);
    }
  }

  @Override
  public Set<ConstraintViolation<Organization>> validateOrganization(
      UUID tenantId, Organization organization) throws ServiceUnavailableException {
    try {
      return getValidator().validate(organization);
    } catch (Throwable e) {
      throw new ServiceUnavailableException("Failed to validate the organization", e);
    }
  }

  @Override
  public Set<ConstraintViolation<Party>> validateParty(UUID tenantId, Party party)
      throws ServiceUnavailableException {
    try {
      return getValidator().validate(party);
    } catch (Throwable e) {
      throw new ServiceUnavailableException("Failed to validate the party", e);
    }
  }

  @Override
  public Set<ConstraintViolation<Person>> validatePerson(UUID tenantId, Person person)
      throws ServiceUnavailableException {
    try {
      return getValidator().validate(person);
    } catch (Throwable e) {
      throw new ServiceUnavailableException("Failed to validate the person", e);
    }
  }

  /**
   * Returns the internal reference to the Party Service to enable caching.
   *
   * @return the internal reference to the Party Service to enable caching.
   */
  private PartyService getPartyService() {
    if (partyService == null) {
      partyService = getApplicationContext().getBean(PartyService.class);
    }

    return partyService;
  }
}
