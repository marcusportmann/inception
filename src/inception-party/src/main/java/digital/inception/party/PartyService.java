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
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import javax.annotation.Resource;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
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

  /* Logger */
  private static final Logger logger = LoggerFactory.getLogger(PartyService.class);

  /** The Spring application context. */
  private final ApplicationContext applicationContext;

  /** /** The Organization Repository. */
  private final OrganizationRepository organizationRepository;

  /** The Party Repository. */
  private final PartyRepository partyRepository;

  /** The Person Repository. */
  private final PersonRepository personRepository;

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
   */
  public PartyService(
      ApplicationContext applicationContext,
      Validator validator,
      OrganizationRepository organizationRepository,
      PartyRepository partyRepository,
      PersonRepository personRepository) {
    this.applicationContext = applicationContext;
    this.validator = validator;
    this.organizationRepository = organizationRepository;
    this.partyRepository = partyRepository;
    this.personRepository = personRepository;
  }

  /**
   * Create the new organization.
   *
   * @param organization the organization
   */
  @Override
  @Transactional
  public void createOrganization(Organization organization)
      throws InvalidArgumentException, DuplicateOrganizationException, ServiceUnavailableException {
    if (organization == null) {
      throw new InvalidArgumentException("organization");
    }

    Set<ConstraintViolation<Organization>> constraintViolations = validator.validate(organization);

    if (!constraintViolations.isEmpty()) {
      throw new InvalidArgumentException(
          "organization", ValidationError.toValidationErrors(constraintViolations));
    }

    try {
      if (organizationRepository.existsById(organization.getId())) {
        throw new DuplicateOrganizationException(organization.getId());
      }

      organizationRepository.saveAndFlush(organization);
    } catch (DuplicateOrganizationException e) {
      throw e;
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to create the organization (" + organization.getId() + ")", e);
    }
  }

  /**
   * Create the new person.
   *
   * @param person the person
   */
  @Override
  @Transactional
  public void createPerson(Person person)
      throws InvalidArgumentException, DuplicatePersonException, ServiceUnavailableException {
    if (person == null) {
      throw new InvalidArgumentException("person");
    }

    Set<ConstraintViolation<Person>> constraintViolations = validator.validate(person);

    if (!constraintViolations.isEmpty()) {
      throw new InvalidArgumentException(
          "person", ValidationError.toValidationErrors(constraintViolations));
    }

    try {
      if (personRepository.existsById(person.getId())) {
        throw new DuplicatePersonException(person.getId());
      }

      personRepository.saveAndFlush(person);
    } catch (DuplicatePersonException e) {
      throw e;
    } catch (Throwable e) {

      logger.error("ERROR: " + e.getMessage(), e);

      throw new ServiceUnavailableException(
          "Failed to create the person (" + person.getId() + ")", e);
    }
  }

  /**
   * Delete the organization.
   *
   * @param organizationId the Universally Unique Identifier (UUID) for the organization
   */
  @Override
  @Transactional
  public void deleteOrganization(UUID organizationId)
      throws InvalidArgumentException, OrganizationNotFoundException, ServiceUnavailableException {
    if (organizationId == null) {
      throw new InvalidArgumentException("organizationId");
    }

    try {
      if (!organizationRepository.existsById(organizationId)) {
        throw new OrganizationNotFoundException(organizationId);
      }

      organizationRepository.deleteById(organizationId);
    } catch (OrganizationNotFoundException e) {
      throw e;
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to delete the organization (" + organizationId + ")", e);
    }
  }

  /**
   * Delete the party.
   *
   * @param partyId the Universally Unique Identifier (UUID) for the party
   */
  @Override
  @Transactional
  public void deleteParty(UUID partyId)
      throws InvalidArgumentException, PartyNotFoundException, ServiceUnavailableException {
    if (partyId == null) {
      throw new InvalidArgumentException("partyId");
    }

    try {
      if (!partyRepository.existsById(partyId)) {
        throw new PartyNotFoundException(partyId);
      }

      partyRepository.deleteById(partyId);
    } catch (PartyNotFoundException e) {
      throw e;
    } catch (Throwable e) {
      throw new ServiceUnavailableException("Failed to delete the party (" + partyId + ")", e);
    }
  }

  /**
   * Delete the person.
   *
   * @param personId the Universally Unique Identifier (UUID) for the person
   */
  @Override
  @Transactional
  public void deletePerson(UUID personId)
      throws InvalidArgumentException, PersonNotFoundException, ServiceUnavailableException {
    if (personId == null) {
      throw new InvalidArgumentException("personId");
    }

    try {
      if (!personRepository.existsById(personId)) {
        throw new PersonNotFoundException(personId);
      }

      personRepository.deleteById(personId);
    } catch (PersonNotFoundException e) {
      throw e;
    } catch (Throwable e) {
      throw new ServiceUnavailableException("Failed to delete the person (" + personId + ")", e);
    }
  }

  /**
   * Retrieve the organization.
   *
   * @param organizationId the Universally Unique Identifier (UUID) for the organization
   * @return the organization
   */
  @Override
  public Organization getOrganization(UUID organizationId)
      throws InvalidArgumentException, OrganizationNotFoundException, ServiceUnavailableException {
    if (organizationId == null) {
      throw new InvalidArgumentException("organizationId");
    }

    try {
      Optional<Organization> organizationOptional = organizationRepository.findById(organizationId);

      if (organizationOptional.isPresent()) {
        return organizationOptional.get();
      } else {
        throw new OrganizationNotFoundException(organizationId);
      }
    } catch (OrganizationNotFoundException e) {
      throw e;
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to retrieve the organization (" + organizationId + ")", e);
    }
  }

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
  @Override
  public Organizations getOrganizations(
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
                (sortDirection == SortDirection.ASCENDING)
                    ? Sort.Direction.ASC
                    : Sort.Direction.DESC,
                "name");
      } else {
        pageRequest =
            PageRequest.of(
                pageIndex,
                Math.min(pageSize, MAX_FILTERED_ORGANISATIONS),
                (sortDirection == SortDirection.ASCENDING)
                    ? Sort.Direction.ASC
                    : Sort.Direction.DESC,
                "name");
      }

      Page<Organization> organizationPage;
      if (StringUtils.hasText(filter)) {
        organizationPage = organizationRepository.findFiltered("%" + filter + "%", pageRequest);
      } else {
        organizationPage = organizationRepository.findAll(pageRequest);
      }

      return new Organizations(
          organizationPage.toList(),
          organizationPage.getTotalElements(),
          filter,
          sortBy,
          sortDirection,
          pageIndex,
          pageSize);
    } catch (Throwable e) {

      logger.error("Failed to retrieve the filtered organizations", e);

      throw new ServiceUnavailableException("Failed to retrieve the filtered organizations", e);
    }
  }

  /**
   * Retrieve the parties.
   *
   * @param filter the optional filter to apply to the parties
   * @param sortDirection the optional sort direction to apply to the parties
   * @param pageIndex the optional page index
   * @param pageSize the optional page size
   * @return the parties
   */
  @Override
  public Parties getParties(
      String filter, SortDirection sortDirection, Integer pageIndex, Integer pageSize)
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
        partyPage = partyRepository.findAll(pageRequest);
      }

      return new Parties(
          partyPage.toList(),
          partyPage.getTotalElements(),
          filter,
          sortDirection,
          pageIndex,
          pageSize);
    } catch (Throwable e) {
      throw new ServiceUnavailableException("Failed to retrieve the filtered parties", e);
    }
  }

  /**
   * Retrieve the party.
   *
   * @param partyId the Universally Unique Identifier (UUID) for the party
   * @return the party
   */
  @Override
  public Party getParty(UUID partyId)
      throws InvalidArgumentException, PartyNotFoundException, ServiceUnavailableException {
    if (partyId == null) {
      throw new InvalidArgumentException("partyId");
    }

    try {
      Optional<Party> partyOptional = partyRepository.findById(partyId);

      if (partyOptional.isPresent()) {
        return partyOptional.get();
      } else {
        throw new PartyNotFoundException(partyId);
      }
    } catch (PartyNotFoundException e) {
      throw e;
    } catch (Throwable e) {
      throw new ServiceUnavailableException("Failed to retrieve the party (" + partyId + ")", e);
    }
  }

  /**
   * Retrieve the person.
   *
   * @param personId the Universally Unique Identifier (UUID) for the person
   * @return the person
   */
  @Override
  // @Cacheable(value = "persons", key = "#personId")
  public Person getPerson(UUID personId)
      throws InvalidArgumentException, PersonNotFoundException, ServiceUnavailableException {
    if (personId == null) {
      throw new InvalidArgumentException("personId");
    }

    try {
      Optional<Person> personOptional = personRepository.findById(personId);

      if (personOptional.isPresent()) {
        return personOptional.get();
      } else {
        throw new PersonNotFoundException(personId);
      }
    } catch (PersonNotFoundException e) {
      throw e;
    } catch (Throwable e) {
      throw new ServiceUnavailableException("Failed to retrieve the person (" + personId + ")", e);
    }
  }

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
  @Override
  public Persons getPersons(
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
                (sortDirection == SortDirection.ASCENDING)
                    ? Sort.Direction.ASC
                    : Sort.Direction.DESC,
                "preferredName");
      } else {
        pageRequest =
            PageRequest.of(
                pageIndex,
                Math.min(pageSize, MAX_FILTERED_PERSONS),
                (sortDirection == SortDirection.ASCENDING)
                    ? Sort.Direction.ASC
                    : Sort.Direction.DESC,
                "name");
      }

      Page<Person> personPage;
      if (StringUtils.hasText(filter)) {
        personPage = personRepository.findFiltered("%" + filter + "%", pageRequest);
      } else {
        personPage = personRepository.findAll(pageRequest);
      }

      return new Persons(
          personPage.toList(),
          personPage.getTotalElements(),
          filter,
          sortBy,
          sortDirection,
          pageIndex,
          pageSize);
    } catch (Throwable e) {

      logger.error("Failed to retrieve the filtered persons", e);

      throw new ServiceUnavailableException("Failed to retrieve the filtered persons", e);
    }
  }

  /**
   * Update the organization.
   *
   * @param organization the organization
   */
  @Override
  @Transactional
  public void updateOrganization(Organization organization)
      throws InvalidArgumentException, OrganizationNotFoundException, ServiceUnavailableException {
    if (organization == null) {
      throw new InvalidArgumentException("organization");
    }

    Set<ConstraintViolation<Organization>> constraintViolations = validator.validate(organization);

    if (!constraintViolations.isEmpty()) {
      throw new InvalidArgumentException(
          "organization", ValidationError.toValidationErrors(constraintViolations));
    }

    try {
      if (!organizationRepository.existsById(organization.getId())) {
        throw new OrganizationNotFoundException(organization.getId());
      }

      organizationRepository.saveAndFlush(organization);
    } catch (OrganizationNotFoundException e) {
      throw e;
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to update the organization (" + organization.getId() + ")", e);
    }
  }

  /**
   * Update the person.
   *
   * @param person the person
   */
  @Override
  @Transactional
  // @CacheEvict(value="persons", allEntries=true)
  public void updatePerson(Person person)
      throws InvalidArgumentException, PersonNotFoundException, ServiceUnavailableException {
    if (person == null) {
      throw new InvalidArgumentException("person");
    }

    Set<ConstraintViolation<Person>> constraintViolations = validator.validate(person);

    if (!constraintViolations.isEmpty()) {
      throw new InvalidArgumentException(
          "person", ValidationError.toValidationErrors(constraintViolations));
    }

    try {
      if (!personRepository.existsById(person.getId())) {
        throw new PersonNotFoundException(person.getId());
      }

      personRepository.saveAndFlush(person);
    } catch (PersonNotFoundException e) {
      throw e;
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to update the person (" + person.getId() + ")", e);
    }
  }

  /**
   * Validate the organization.
   *
   * @param organization the organization
   * @return the constraint violations for the organization
   */
  @Override
  public Set<ConstraintViolation<Organization>> validateOrganization(Organization organization) {
    return validator.validate(organization);
  }

  /**
   * Validate the party.
   *
   * @param party the party
   * @return the constraint violations for the party
   */
  @Override
  public Set<ConstraintViolation<Party>> validateParty(Party party) {
    return validator.validate(party);
  }

  /**
   * Validate the person.
   *
   * @param person the person
   * @return the constraint violations for the person
   */
  @Override
  public Set<ConstraintViolation<Person>> validatePerson(Person person) {
    return validator.validate(person);
  }

  /**
   * Validate the physical address.
   *
   * @param physicalAddress the physical address
   * @return the constraint violations for the physical address
   */
  @Override
  public Set<ConstraintViolation<PhysicalAddress>> validatePhysicalAddress(
      PhysicalAddress physicalAddress) {
    return validator.validate(physicalAddress);
  }
}
