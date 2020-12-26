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

// ~--- non-JDK imports --------------------------------------------------------

import digital.inception.core.sorting.SortDirection;
import java.util.Optional;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

// ~--- JDK imports ------------------------------------------------------------

/**
 * The <code>PartyService</code> class provides the Party Service implementation.
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

  /** The Organization Repository. */
  private final OrganizationRepository organizationRepository;

  /** The Party Repository. */
  private final PartyRepository partyRepository;

  /** The Person Repository. */
  private final PersonRepository personRepository;

  /**
   * Constructs a new <code>PartyService</code>.
   *
   * @param applicationContext the Spring application context
   * @param partyRepository the Party Repository
   * @param personRepository the Person Repository
   * @param organizationRepository the Organization Repository
   */
  public PartyService(
      ApplicationContext applicationContext,
      PartyRepository partyRepository,
      PersonRepository personRepository,
      OrganizationRepository organizationRepository) {
    this.applicationContext = applicationContext;
    this.partyRepository = partyRepository;
    this.personRepository = personRepository;
    this.organizationRepository = organizationRepository;
  }

  /**
   * Create the new organization.
   *
   * @param organization the organization
   */
  @Override
  @Transactional
  public void createOrganization(Organization organization)
      throws DuplicateOrganizationException, PartyServiceException {
    try {
      if (organizationRepository.existsById(organization.getId())) {
        throw new DuplicateOrganizationException(organization.getId());
      }

      organizationRepository.saveAndFlush(organization);
    } catch (DuplicateOrganizationException e) {
      throw e;
    } catch (Throwable e) {
      throw new PartyServiceException(
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
  public void createPerson(Person person) throws DuplicatePersonException, PartyServiceException {
    try {
      if (personRepository.existsById(person.getId())) {
        throw new DuplicatePersonException(person.getId());
      }

      personRepository.saveAndFlush(person);
    } catch (DuplicatePersonException e) {
      throw e;
    } catch (Throwable e) {
      throw new PartyServiceException("Failed to create the person (" + person.getId() + ")", e);
    }
  }

  /**
   * Delete the organization.
   *
   * @param organizationId the Universally Unique Identifier (UUID) uniquely identifying the
   *     organization
   */
  @Override
  @Transactional
  public void deleteOrganization(UUID organizationId)
      throws OrganizationNotFoundException, PartyServiceException {
    try {
      if (!organizationRepository.existsById(organizationId)) {
        throw new OrganizationNotFoundException(organizationId);
      }

      organizationRepository.deleteById(organizationId);
    } catch (OrganizationNotFoundException e) {
      throw e;
    } catch (Throwable e) {
      throw new PartyServiceException(
          "Failed to delete the organization (" + organizationId + ")", e);
    }
  }

  /**
   * Delete the party.
   *
   * @param partyId the Universally Unique Identifier (UUID) uniquely identifying the party
   */
  @Override
  @Transactional
  public void deleteParty(UUID partyId) throws PartyNotFoundException, PartyServiceException {
    try {
      if (!partyRepository.existsById(partyId)) {
        throw new PartyNotFoundException(partyId);
      }

      partyRepository.deleteById(partyId);
    } catch (PartyNotFoundException e) {
      throw e;
    } catch (Throwable e) {
      throw new PartyServiceException("Failed to delete the party (" + partyId + ")", e);
    }
  }

  /**
   * Delete the person.
   *
   * @param personId the Universally Unique Identifier (UUID) uniquely identifying the person
   */
  @Override
  @Transactional
  public void deletePerson(UUID personId) throws PersonNotFoundException, PartyServiceException {
    try {
      if (!personRepository.existsById(personId)) {
        throw new PersonNotFoundException(personId);
      }

      personRepository.deleteById(personId);
    } catch (PersonNotFoundException e) {
      throw e;
    } catch (Throwable e) {
      throw new PartyServiceException("Failed to delete the person (" + personId + ")", e);
    }
  }

  /**
   * Retrieve the organization.
   *
   * @param organizationId the Universally Unique Identifier (UUID) uniquely identifying the
   *     organization
   * @return the organization
   */
  @Override
  public Organization getOrganization(UUID organizationId)
      throws OrganizationNotFoundException, PartyServiceException {
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
      throw new PartyServiceException(
          "Failed to retrieve the organization (" + organizationId + ")", e);
    }
  }

  /**
   * Retrieve the organizations.
   *
   * @param filter the optional filter to apply to the organizations
   * @param sortDirection the optional sort direction to apply to the organizations
   * @param pageIndex the optional page index
   * @param pageSize the optional page size
   * @return the organizations
   */
  @Override
  public Organizations getOrganizations(
      String filter, SortDirection sortDirection, Integer pageIndex, Integer pageSize)
      throws PartyServiceException {
    PageRequest pageRequest;

    if ((pageIndex != null) && (pageSize != null)) {
      pageRequest =
          PageRequest.of(
              pageIndex,
              (pageSize > MAX_FILTERED_ORGANISATIONS) ? MAX_FILTERED_ORGANISATIONS : pageSize);
    } else {
      pageRequest = PageRequest.of(0, MAX_FILTERED_ORGANISATIONS);
    }

    try {
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
          sortDirection,
          pageIndex,
          pageSize);
    } catch (Throwable e) {
      throw new PartyServiceException("Failed to retrieve the filtered organizations", e);
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
      throws PartyServiceException {

    PageRequest pageRequest;

    if ((pageIndex != null) && (pageSize != null)) {
      pageRequest =
          PageRequest.of(
              pageIndex, (pageSize > MAX_FILTERED_PARTIES) ? MAX_FILTERED_PARTIES : pageSize);
    } else {
      pageRequest = PageRequest.of(0, MAX_FILTERED_PARTIES);
    }

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
      throw new PartyServiceException("Failed to retrieve the filtered parties", e);
    }
  }

  /**
   * Retrieve the party.
   *
   * @param partyId the Universally Unique Identifier (UUID) uniquely identifying the party
   * @return the party
   */
  @Override
  public Party getParty(UUID partyId) throws PartyNotFoundException, PartyServiceException {
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
      throw new PartyServiceException("Failed to retrieve the party (" + partyId + ")", e);
    }
  }

  /**
   * Retrieve the person.
   *
   * @param personId the Universally Unique Identifier (UUID) uniquely identifying the person
   * @return the person
   */
  @Override
  public Person getPerson(UUID personId) throws PersonNotFoundException, PartyServiceException {
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
      throw new PartyServiceException("Failed to retrieve the person (" + personId + ")", e);
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
      throws PartyServiceException {
    if (sortBy == null) {
      sortBy = PersonSortBy.NAME;
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
                (pageSize > MAX_FILTERED_PERSONS) ? MAX_FILTERED_PERSONS : pageSize,
                (sortDirection == SortDirection.ASCENDING)
                    ? Sort.Direction.ASC
                    : Sort.Direction.DESC,
                "preferredName");
      } else {
        pageRequest =
            PageRequest.of(
                pageIndex,
                (pageSize > MAX_FILTERED_PERSONS) ? MAX_FILTERED_PERSONS : pageSize,
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
      throw new PartyServiceException("Failed to retrieve the filtered persons", e);
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
      throws OrganizationNotFoundException, PartyServiceException {
    try {
      if (!organizationRepository.existsById(organization.getId())) {
        throw new OrganizationNotFoundException(organization.getId());
      }

      organizationRepository.saveAndFlush(organization);
    } catch (OrganizationNotFoundException e) {
      throw e;
    } catch (Throwable e) {
      throw new PartyServiceException(
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
  public void updatePerson(Person person) throws PersonNotFoundException, PartyServiceException {
    try {
      if (!personRepository.existsById(person.getId())) {
        throw new PersonNotFoundException(person.getId());
      }

      personRepository.saveAndFlush(person);
    } catch (PersonNotFoundException e) {
      throw e;
    } catch (Throwable e) {
      throw new PartyServiceException("Failed to update the person (" + person.getId() + ")", e);
    }
  }
}
