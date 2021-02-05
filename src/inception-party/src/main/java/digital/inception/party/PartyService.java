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
import digital.inception.core.validation.InvalidArgumentException;
import digital.inception.core.validation.ValidationError;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import javax.annotation.Resource;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.ApplicationContext;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
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

  /** The Contact Mechanism Purpose Repository. */
  private final ContactMechanismPurposeRepository contactMechanismPurposeRepository;

  /** The Contact Mechanism Type Repository. */
  private final ContactMechanismTypeRepository contactMechanismTypeRepository;

  /** The Employment Status Repository */
  private final EmploymentStatusRepository employmentStatusRepository;

  /** The Employment Type Repository. */
  private final EmploymentTypeRepository employmentTypeRepository;

  /** The Gender Repository. */
  private final GenderRepository genderRepository;

  /** The Identity Document Type Repository. */
  private final IdentityDocumentTypeRepository identityDocumentTypeRepository;

  /** The Marital Status Repository. */
  private final MaritalStatusRepository maritalStatusRepository;

  /** The Marriage Type Repository. */
  private final MarriageTypeRepository marriageTypeRepository;

  /** The Next Of Kin Type Repository. */
  private final NextOfKinTypeRepository nextOfKinTypeRepository;

  /** The Occupation Repository. */
  private final OccupationRepository occupationRepository;

  /** The Organization Repository. */
  private final OrganizationRepository organizationRepository;

  /** The Party Attribute Type Category Repository. */
  private final PartyAttributeTypeCategoryRepository partyAttributeTypeCategoryRepository;

  /** The Party Attribute Type Repository. */
  private final PartyAttributeTypeRepository partyAttributeTypeRepository;

  /** The Party Repository. */
  private final PartyRepository partyRepository;

  /** The Party Role Purpose Repository. */
  private final PartyRolePurposeRepository partyRolePurposeRepository;

  /** The Party Role Type Repository. */
  private final PartyRoleTypeRepository partyRoleTypeRepository;

  /** The Person Repository. */
  private final PersonRepository personRepository;

  /** The Physical Address Purpose Repository. */
  private final PhysicalAddressPurposeRepository physicalAddressPurposeRepository;

  /** The Physical Address Type Repository. */
  private final PhysicalAddressTypeRepository physicalAddressTypeRepository;

  /** The Preference Type Category Repository. */
  private final PreferenceTypeCategoryRepository preferenceTypeCategoryRepository;

  /** The Preference Type Repository */
  private final PreferenceTypeRepository preferenceTypeRepository;

  /** The Race Repository. */
  private final RaceRepository raceRepository;

  /** The Residence Permit Type Repository. */
  private final ResidencePermitTypeRepository residencePermitTypeRepository;

  /** The Residency Status Repository. */
  private final ResidencyStatusRepository residencyStatusRepository;

  /** The Residential Type Repository. */
  private final ResidentialTypeRepository residentialTypeRepository;

  /** The Sources of Funds Repository. */
  private final SourceOfFundsRepository sourceOfFundsRepository;

  /** The Tax Number Type Repository. */
  private final TaxNumberTypeRepository taxNumberTypeRepository;

  /** The Time To Contact Repository. */
  private final TimeToContactRepository timeToContactRepository;

  /** The Title Repository. */
  private final TitleRepository titleRepository;

  /** The JSR-303 validator. */
  private final Validator validator;

  /** The internal reference to the Party Service to enable caching. */
  @Resource private IPartyService self;

  /**
   * Constructs a new <b>PartyService</b>.
   *
   * @param applicationContext the Spring application context
   * @param validator the JSR-303 validator
   * @param contactMechanismPurposeRepository the Contact Mechanism Purpose Repository
   * @param contactMechanismTypeRepository the Contact Mechanism Type Repository
   * @param employmentStatusRepository the Employment Status Repository
   * @param employmentTypeRepository the Employment Type Repository
   * @param genderRepository the Gender Repository
   * @param identityDocumentTypeRepository the Identity Document Type Repository
   * @param maritalStatusRepository the Marital Status Repository
   * @param marriageTypeRepository the Marriage Type Repository
   * @param nextOfKinTypeRepository the Next Of Kin Repository
   * @param occupationRepository the Occupation Repository
   * @param organizationRepository the Organization Repository
   * @param partyAttributeTypeCategoryRepository the Party Attribute Type Category Repository
   * @param partyAttributeTypeRepository the Party Attribute Type Repository
   * @param partyRepository the Party Repository
   * @param partyRolePurposeRepository the Party Role Purpose Repository
   * @param partyRoleTypeRepository the Party Role Type Repository
   * @param personRepository the Person Repository
   * @param physicalAddressPurposeRepository the Physical Address Purpose Repository
   * @param physicalAddressTypeRepository the Physical Address Type Repository
   * @param preferenceTypeCategoryRepository the Preference Type Category Repository
   * @param preferenceTypeRepository the Preference Type Repository
   * @param raceRepository the Race Repository
   * @param residencePermitTypeRepository the Residence Permit Type Repository*
   * @param residencyStatusRepository the Residency Status Repository
   * @param residentialTypeRepository the Residential Type Repository
   * @param sourceOfFundsRepository the Source Of Funds Repository
   * @param taxNumberTypeRepository the Tax Number Type Repository
   * @param timeToContactRepository the Time To Contact Repository
   * @param titleRepository the Title Repository
   */
  public PartyService(
      ApplicationContext applicationContext,
      Validator validator,
      ContactMechanismPurposeRepository contactMechanismPurposeRepository,
      ContactMechanismTypeRepository contactMechanismTypeRepository,
      EmploymentStatusRepository employmentStatusRepository,
      EmploymentTypeRepository employmentTypeRepository,
      GenderRepository genderRepository,
      IdentityDocumentTypeRepository identityDocumentTypeRepository,
      MaritalStatusRepository maritalStatusRepository,
      MarriageTypeRepository marriageTypeRepository,
      NextOfKinTypeRepository nextOfKinTypeRepository,
      OccupationRepository occupationRepository,
      OrganizationRepository organizationRepository,
      PartyAttributeTypeCategoryRepository partyAttributeTypeCategoryRepository,
      PartyAttributeTypeRepository partyAttributeTypeRepository,
      PartyRepository partyRepository,
      PartyRolePurposeRepository partyRolePurposeRepository,
      PartyRoleTypeRepository partyRoleTypeRepository,
      PersonRepository personRepository,
      PhysicalAddressPurposeRepository physicalAddressPurposeRepository,
      PhysicalAddressTypeRepository physicalAddressTypeRepository,
      PreferenceTypeCategoryRepository preferenceTypeCategoryRepository,
      PreferenceTypeRepository preferenceTypeRepository,
      RaceRepository raceRepository,
      ResidencePermitTypeRepository residencePermitTypeRepository,
      ResidencyStatusRepository residencyStatusRepository,
      ResidentialTypeRepository residentialTypeRepository,
      SourceOfFundsRepository sourceOfFundsRepository,
      TaxNumberTypeRepository taxNumberTypeRepository,
      TimeToContactRepository timeToContactRepository,
      TitleRepository titleRepository) {
    this.applicationContext = applicationContext;
    this.validator = validator;
    this.contactMechanismPurposeRepository = contactMechanismPurposeRepository;
    this.contactMechanismTypeRepository = contactMechanismTypeRepository;
    this.employmentStatusRepository = employmentStatusRepository;
    this.employmentTypeRepository = employmentTypeRepository;
    this.genderRepository = genderRepository;
    this.identityDocumentTypeRepository = identityDocumentTypeRepository;
    this.maritalStatusRepository = maritalStatusRepository;
    this.marriageTypeRepository = marriageTypeRepository;
    this.nextOfKinTypeRepository = nextOfKinTypeRepository;
    this.occupationRepository = occupationRepository;
    this.organizationRepository = organizationRepository;
    this.partyAttributeTypeCategoryRepository = partyAttributeTypeCategoryRepository;
    this.partyAttributeTypeRepository = partyAttributeTypeRepository;
    this.partyRepository = partyRepository;
    this.partyRolePurposeRepository = partyRolePurposeRepository;
    this.partyRoleTypeRepository = partyRoleTypeRepository;
    this.personRepository = personRepository;
    this.physicalAddressPurposeRepository = physicalAddressPurposeRepository;
    this.physicalAddressTypeRepository = physicalAddressTypeRepository;
    this.preferenceTypeCategoryRepository = preferenceTypeCategoryRepository;
    this.preferenceTypeRepository = preferenceTypeRepository;
    this.raceRepository = raceRepository;
    this.residencePermitTypeRepository = residencePermitTypeRepository;
    this.residencyStatusRepository = residencyStatusRepository;
    this.residentialTypeRepository = residentialTypeRepository;
    this.sourceOfFundsRepository = sourceOfFundsRepository;
    this.taxNumberTypeRepository = taxNumberTypeRepository;
    this.timeToContactRepository = timeToContactRepository;
    this.titleRepository = titleRepository;
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
   * Retrieve all the contact mechanism purposes.
   *
   * @return the contact mechanism purposes
   */
  @Override
  @Cacheable(value = "reference", key = "'contactMechanismPurposes.ALL'")
  public List<ContactMechanismPurpose> getContactMechanismPurposes()
      throws ServiceUnavailableException {
    return getContactMechanismPurposes(null);
  }

  /**
   * Retrieve the contact mechanism purposes.
   *
   * @param localeId the Unicode locale identifier for the locale to retrieve the contact mechanism
   *     purposes for or <b>null</b> to retrieve the contact mechanism purposes for all locales
   * @return the contact mechanism purposes
   */
  @Override
  @Cacheable(value = "reference", key = "'contactMechanismPurposes.' + #localeId")
  public List<ContactMechanismPurpose> getContactMechanismPurposes(String localeId)
      throws ServiceUnavailableException {
    try {
      if (!StringUtils.hasText(localeId)) {
        return contactMechanismPurposeRepository.findAll(
            Sort.by(Direction.ASC, "localeId", "sortIndex"));
      } else {
        return contactMechanismPurposeRepository.findByLocaleIdIgnoreCase(
            localeId, Sort.by(Direction.ASC, "localeId", "sortIndex"));
      }
    } catch (Throwable e) {
      throw new ServiceUnavailableException("Failed to retrieve the contact mechanism purposes", e);
    }
  }

  /**
   * Retrieve all the contact mechanism types.
   *
   * @return the contact mechanism types
   */
  @Override
  @Cacheable(value = "reference", key = "'contactMechanismTypes.ALL'")
  public List<ContactMechanismType> getContactMechanismTypes() throws ServiceUnavailableException {
    return getContactMechanismTypes(null);
  }

  /**
   * Retrieve the contact mechanism types.
   *
   * @param localeId the Unicode locale identifier for the locale to retrieve the contact mechanism
   *     types for or <b>null</b> to retrieve the contact mechanism types for all locales
   * @return the contact mechanism types
   */
  @Override
  @Cacheable(value = "reference", key = "'contactMechanismTypes.' + #localeId")
  public List<ContactMechanismType> getContactMechanismTypes(String localeId)
      throws ServiceUnavailableException {
    try {
      if (!StringUtils.hasText(localeId)) {
        return contactMechanismTypeRepository.findAll(
            Sort.by(Direction.ASC, "localeId", "sortIndex"));
      } else {
        return contactMechanismTypeRepository.findByLocaleIdIgnoreCase(
            localeId, Sort.by(Direction.ASC, "localeId", "sortIndex"));
      }
    } catch (Throwable e) {
      throw new ServiceUnavailableException("Failed to retrieve the contact mechanism types", e);
    }
  }

  /**
   * Retrieve all the employment statuses.
   *
   * @return the employment statuses
   */
  @Override
  @Cacheable(value = "reference", key = "'employmentStatuses.ALL'")
  public List<EmploymentStatus> getEmploymentStatuses() throws ServiceUnavailableException {
    return getEmploymentStatuses(null);
  }

  /**
   * Retrieve the employment statuses.
   *
   * @param localeId the Unicode locale identifier for the locale to retrieve the employment
   *     statuses for or <b>null</b> to retrieve the employment statuses for all locales
   * @return the employment statuses
   */
  @Override
  @Cacheable(value = "reference", key = "'employmentStatuses.' + #localeId")
  public List<EmploymentStatus> getEmploymentStatuses(String localeId)
      throws ServiceUnavailableException {
    try {
      if (!StringUtils.hasText(localeId)) {
        return employmentStatusRepository.findAll(Sort.by(Direction.ASC, "localeId", "sortIndex"));
      } else {
        return employmentStatusRepository.findByLocaleIdIgnoreCase(
            localeId, Sort.by(Direction.ASC, "localeId", "sortIndex"));
      }
    } catch (Throwable e) {
      throw new ServiceUnavailableException("Failed to retrieve the employment statuses", e);
    }
  }

  /**
   * Retrieve all the employment types.
   *
   * @return the employment types
   */
  @Override
  @Cacheable(value = "reference", key = "'employmentTypes.ALL'")
  public List<EmploymentType> getEmploymentTypes() throws ServiceUnavailableException {
    return getEmploymentTypes(null);
  }

  /**
   * Retrieve the employment types.
   *
   * @param localeId the Unicode locale identifier for the locale to retrieve the employment types
   *     for or <b>null</b> to retrieve the employment types for all locales
   * @return the employment types
   */
  @Override
  @Cacheable(value = "reference", key = "'employmentTypes.' + #localeId")
  public List<EmploymentType> getEmploymentTypes(String localeId)
      throws ServiceUnavailableException {
    try {
      if (!StringUtils.hasText(localeId)) {
        return employmentTypeRepository.findAll(Sort.by(Direction.ASC, "localeId", "sortIndex"));
      } else {
        return employmentTypeRepository.findByLocaleIdIgnoreCase(
            localeId, Sort.by(Direction.ASC, "localeId", "sortIndex"));
      }
    } catch (Throwable e) {
      throw new ServiceUnavailableException("Failed to retrieve the employment types", e);
    }
  }

  /**
   * Retrieve all the genders.
   *
   * @return the genders
   */
  @Override
  @Cacheable(value = "reference", key = "'genders.ALL'")
  public List<Gender> getGenders() throws ServiceUnavailableException {
    return getGenders(null);
  }

  /**
   * Retrieve the genders.
   *
   * @param localeId the Unicode locale identifier for the locale to retrieve the genders for or
   *     <b>null</b> to retrieve the genders for all locales
   * @return the genders
   */
  @Override
  @Cacheable(value = "reference", key = "'genders.' + #localeId")
  public List<Gender> getGenders(String localeId) throws ServiceUnavailableException {
    try {
      if (!StringUtils.hasText(localeId)) {
        return genderRepository.findAll(Sort.by(Direction.ASC, "localeId", "sortIndex"));
      } else {
        return genderRepository.findByLocaleIdIgnoreCase(
            localeId, Sort.by(Direction.ASC, "localeId", "sortIndex"));
      }
    } catch (Throwable e) {
      throw new ServiceUnavailableException("Failed to retrieve the genders", e);
    }
  }

  /**
   * Retrieve all the identity document types.
   *
   * @return the identity document types
   */
  @Override
  @Cacheable(value = "reference", key = "'identityDocumentTypes.ALL'")
  public List<IdentityDocumentType> getIdentityDocumentTypes() throws ServiceUnavailableException {
    return getIdentityDocumentTypes(null);
  }

  /**
   * Retrieve the identity document types.
   *
   * @param localeId the Unicode locale identifier for the locale to retrieve the identity document
   *     types for or <b>null</b> to retrieve the identity document types for all locales
   * @return the identity document types
   */
  @Override
  @Cacheable(value = "reference", key = "'identityDocumentTypes.' + #localeId")
  public List<IdentityDocumentType> getIdentityDocumentTypes(String localeId)
      throws ServiceUnavailableException {
    try {
      if (!StringUtils.hasText(localeId)) {
        return identityDocumentTypeRepository.findAll(
            Sort.by(Direction.ASC, "localeId", "sortIndex"));
      } else {
        return identityDocumentTypeRepository.findByLocaleIdIgnoreCase(
            localeId, Sort.by(Direction.ASC, "localeId", "sortIndex"));
      }
    } catch (Throwable e) {
      throw new ServiceUnavailableException("Failed to retrieve the identity document types", e);
    }
  }

  /**
   * Retrieve all the marital statuses.
   *
   * @return the marital statuses
   */
  @Override
  @Cacheable(value = "reference", key = "'maritalStatuses.ALL'")
  public List<MaritalStatus> getMaritalStatuses() throws ServiceUnavailableException {
    return getMaritalStatuses(null);
  }

  /**
   * Retrieve the marital statuses.
   *
   * @param localeId the Unicode locale identifier for the locale to retrieve the marital statuses
   *     for or <b>null</b> to retrieve the marital statuses for all locales
   * @return the marital statuses
   */
  @Override
  @Cacheable(value = "reference", key = "'maritalStatuses.' + #localeId")
  public List<MaritalStatus> getMaritalStatuses(String localeId)
      throws ServiceUnavailableException {
    try {
      if (!StringUtils.hasText(localeId)) {
        return maritalStatusRepository.findAll(Sort.by(Direction.ASC, "localeId", "sortIndex"));
      } else {
        return maritalStatusRepository.findByLocaleIdIgnoreCase(
            localeId, Sort.by(Direction.ASC, "localeId", "sortIndex"));
      }
    } catch (Throwable e) {
      throw new ServiceUnavailableException("Failed to retrieve the marital statuses", e);
    }
  }

  /**
   * Retrieve all the marriage types.
   *
   * @return the marriage types
   */
  @Override
  @Cacheable(value = "reference", key = "'marriageTypes.ALL'")
  public List<MarriageType> getMarriageTypes() throws ServiceUnavailableException {
    return getMarriageTypes(null);
  }

  /**
   * Retrieve the marriage types.
   *
   * @param localeId the Unicode locale identifier for the locale to retrieve the marriage types for
   *     or <b>null</b> to retrieve the marriage types for all locales
   * @return the marriage types
   */
  @Override
  @Cacheable(value = "reference", key = "'marriageTypes.' + #localeId")
  public List<MarriageType> getMarriageTypes(String localeId) throws ServiceUnavailableException {
    try {
      if (!StringUtils.hasText(localeId)) {
        return marriageTypeRepository.findAll(Sort.by(Direction.ASC, "localeId", "sortIndex"));
      } else {
        return marriageTypeRepository.findByLocaleIdIgnoreCase(
            localeId, Sort.by(Direction.ASC, "localeId", "sortIndex"));
      }
    } catch (Throwable e) {
      throw new ServiceUnavailableException("Failed to retrieve the marriage types", e);
    }
  }

  /**
   * Retrieve all the next of kin types.
   *
   * @return the next of kin types
   */
  @Override
  @Cacheable(value = "reference", key = "'nextOfKinTypes.ALL'")
  public List<NextOfKinType> getNextOfKinTypes() throws ServiceUnavailableException {
    return getNextOfKinTypes(null);
  }

  /**
   * Retrieve the next of kin types.
   *
   * @param localeId the Unicode locale identifier for the locale to retrieve the next of kin types
   *     for or <b>null</b> to retrieve the next of kin types for all locales
   * @return the next of kin types
   */
  @Override
  @Cacheable(value = "reference", key = "'nextOfKinTypes.' + #localeId")
  public List<NextOfKinType> getNextOfKinTypes(String localeId) throws ServiceUnavailableException {
    try {
      if (!StringUtils.hasText(localeId)) {
        return nextOfKinTypeRepository.findAll(Sort.by(Direction.ASC, "localeId", "sortIndex"));
      } else {
        return nextOfKinTypeRepository.findByLocaleIdIgnoreCase(
            localeId, Sort.by(Direction.ASC, "localeId", "sortIndex"));
      }
    } catch (Throwable e) {
      throw new ServiceUnavailableException("Failed to retrieve the next of kin types", e);
    }
  }

  /**
   * Retrieve all the occupations.
   *
   * @return the occupations
   */
  @Override
  @Cacheable(value = "reference", key = "'occupations.ALL'")
  public List<Occupation> getOccupations() throws ServiceUnavailableException {
    return getOccupations(null);
  }

  /**
   * Retrieve the occupations.
   *
   * @param localeId the Unicode locale identifier for the locale to retrieve the occupations for or
   *     <b>null</b> to retrieve the occupations for all locales
   * @return the occupations
   */
  @Override
  @Cacheable(value = "reference", key = "'occupations.' + #localeId")
  public List<Occupation> getOccupations(String localeId) throws ServiceUnavailableException {
    try {
      if (!StringUtils.hasText(localeId)) {
        return occupationRepository.findAll(Sort.by(Direction.ASC, "localeId", "sortIndex"));
      } else {
        return occupationRepository.findByLocaleIdIgnoreCase(
            localeId, Sort.by(Direction.ASC, "localeId", "sortIndex"));
      }
    } catch (Throwable e) {
      throw new ServiceUnavailableException("Failed to retrieve the occupations", e);
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
   * @param sortDirection the optional sort direction to apply to the organizations
   * @param pageIndex the optional page index
   * @param pageSize the optional page size
   * @return the organizations
   */
  @Override
  public Organizations getOrganizations(
      String filter, SortDirection sortDirection, Integer pageIndex, Integer pageSize)
      throws InvalidArgumentException, ServiceUnavailableException {
    if ((pageIndex != null) && (pageIndex < 0)) {
      throw new InvalidArgumentException("pageIndex");
    }

    if ((pageSize != null) && (pageSize <= 0)) {
      throw new InvalidArgumentException("pageSize");
    }

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
   * Retrieve all the party attribute type categories.
   *
   * @return the party attribute type categories
   */
  @Override
  @Cacheable(value = "reference", key = "'partyAttributeTypeCategories.ALL'")
  public List<PartyAttributeTypeCategory> getPartyAttributeTypeCategories()
      throws ServiceUnavailableException {
    return getPartyAttributeTypeCategories(null);
  }

  /**
   * Retrieve the party attribute type categories.
   *
   * @param localeId the Unicode locale identifier for the locale to retrieve the party attribute
   *     type categories for or <b>null</b> to retrieve the party attribute type categories for all
   *     locales
   * @return the party attribute type categories
   */
  @Override
  @Cacheable(value = "reference", key = "'partyAttributeTypeCategories.' + #localeId")
  public List<PartyAttributeTypeCategory> getPartyAttributeTypeCategories(String localeId)
      throws ServiceUnavailableException {
    try {
      if (!StringUtils.hasText(localeId)) {
        return partyAttributeTypeCategoryRepository.findAll(
            Sort.by(Direction.ASC, "localeId", "sortIndex"));
      } else {
        return partyAttributeTypeCategoryRepository.findByLocaleIdIgnoreCase(
            localeId, Sort.by(Direction.ASC, "localeId", "sortIndex"));
      }
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to retrieve the party attribute type categories", e);
    }
  }

  /**
   * Retrieve all the party attribute types.
   *
   * @return the party attribute types
   */
  @Override
  @Cacheable(value = "reference", key = "'partyAttributeTypes.ALL'")
  public List<PartyAttributeType> getPartyAttributeTypes() throws ServiceUnavailableException {
    return getPartyAttributeTypes(null);
  }

  /**
   * Retrieve the party attribute types.
   *
   * @param localeId the Unicode locale identifier for the locale to retrieve the party attribute
   *     types for or <b>null</b> to retrieve the party attribute types for all locales
   * @return the party attribute types
   */
  @Override
  @Cacheable(value = "reference", key = "'partyAttributeTypes.' + #localeId")
  public List<PartyAttributeType> getPartyAttributeTypes(String localeId)
      throws ServiceUnavailableException {
    try {
      if (!StringUtils.hasText(localeId)) {
        return partyAttributeTypeRepository.findAll(
            Sort.by(Direction.ASC, "localeId", "sortIndex"));
      } else {
        return partyAttributeTypeRepository.findByLocaleIdIgnoreCase(
            localeId, Sort.by(Direction.ASC, "localeId", "sortIndex"));
      }
    } catch (Throwable e) {
      throw new ServiceUnavailableException("Failed to retrieve the party attribute types", e);
    }
  }

  /**
   * Retrieve all the party role purposes.
   *
   * @return the party role purposes
   */
  @Override
  @Cacheable(value = "reference", key = "'partyRolePurposes.ALL'")
  public List<PartyRolePurpose> getPartyRolePurposes() throws ServiceUnavailableException {
    return getPartyRolePurposes(null);
  }

  /**
   * Retrieve the party role purposes.
   *
   * @param localeId the Unicode locale identifier for the locale to retrieve the party role
   *     purposes for or <b>null</b> to retrieve the party role purposes for all locales
   * @return the party role purposes
   */
  @Override
  @Cacheable(value = "reference", key = "'partyRolePurposes.' + #localeId")
  public List<PartyRolePurpose> getPartyRolePurposes(String localeId)
      throws ServiceUnavailableException {
    try {
      if (!StringUtils.hasText(localeId)) {
        return partyRolePurposeRepository.findAll(Sort.by(Direction.ASC, "localeId", "sortIndex"));
      } else {
        return partyRolePurposeRepository.findByLocaleIdIgnoreCase(
            localeId, Sort.by(Direction.ASC, "localeId", "sortIndex"));
      }
    } catch (Throwable e) {
      throw new ServiceUnavailableException("Failed to retrieve the party role purposes", e);
    }
  }

  /**
   * Retrieve all the party role types.
   *
   * @return the party role types
   */
  @Override
  @Cacheable(value = "reference", key = "'partyRoleTypes.ALL'")
  public List<PartyRoleType> getPartyRoleTypes() throws ServiceUnavailableException {
    return getPartyRoleTypes(null);
  }

  /**
   * Retrieve the party role types.
   *
   * @param localeId the Unicode locale identifier for the locale to retrieve the party role types
   *     for or <b>null</b> to retrieve the party role types for all locales
   * @return the party role types
   */
  @Override
  @Cacheable(value = "reference", key = "'partyRoleTypes.' + #localeId")
  public List<PartyRoleType> getPartyRoleTypes(String localeId) throws ServiceUnavailableException {
    try {
      if (!StringUtils.hasText(localeId)) {
        return partyRoleTypeRepository.findAll(Sort.by(Direction.ASC, "localeId", "sortIndex"));
      } else {
        return partyRoleTypeRepository.findByLocaleIdIgnoreCase(
            localeId, Sort.by(Direction.ASC, "localeId", "sortIndex"));
      }
    } catch (Throwable e) {
      throw new ServiceUnavailableException("Failed to retrieve the party role types", e);
    }
  }

  /**
   * Retrieve the person.
   *
   * @param personId the Universally Unique Identifier (UUID) for the person
   * @return the person
   */
  @Override
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

      logger.error("Failed to retrieve the filtered persons", e);

      throw new ServiceUnavailableException("Failed to retrieve the filtered persons", e);
    }
  }

  /**
   * Retrieve all the physical address purposes.
   *
   * @return the physical address purposes
   */
  @Override
  @Cacheable(value = "reference", key = "'physicalAddressPurposes.ALL'")
  public List<PhysicalAddressPurpose> getPhysicalAddressPurposes()
      throws ServiceUnavailableException {
    return getPhysicalAddressPurposes(null);
  }

  /**
   * Retrieve the physical address purposes.
   *
   * @param localeId the Unicode locale identifier for the locale to retrieve the physical address
   *     purposes for or <b>null</b> to retrieve the physical address purposes for all locales
   * @return the physical address purposes
   */
  @Override
  @Cacheable(value = "reference", key = "'physicalAddressPurposes.' + #localeId")
  public List<PhysicalAddressPurpose> getPhysicalAddressPurposes(String localeId)
      throws ServiceUnavailableException {
    try {
      if (!StringUtils.hasText(localeId)) {
        return physicalAddressPurposeRepository.findAll(
            Sort.by(Direction.ASC, "localeId", "sortIndex"));
      } else {
        return physicalAddressPurposeRepository.findByLocaleIdIgnoreCase(
            localeId, Sort.by(Direction.ASC, "localeId", "sortIndex"));
      }
    } catch (Throwable e) {
      throw new ServiceUnavailableException("Failed to retrieve the physical address purposes", e);
    }
  }

  /**
   * Retrieve all the physical address types.
   *
   * @return the physical address types
   */
  @Override
  @Cacheable(value = "reference", key = "'physicalAddressTypes.ALL'")
  public List<PhysicalAddressType> getPhysicalAddressTypes() throws ServiceUnavailableException {
    return getPhysicalAddressTypes(null);
  }

  /**
   * Retrieve the physical address types.
   *
   * @param localeId the Unicode locale identifier for the locale to retrieve the physical address
   *     types for or <b>null</b> to retrieve the physical address types for all locales
   * @return the physical address types
   */
  @Override
  @Cacheable(value = "reference", key = "'physicalAddressTypes.' + #localeId")
  public List<PhysicalAddressType> getPhysicalAddressTypes(String localeId)
      throws ServiceUnavailableException {
    try {
      if (!StringUtils.hasText(localeId)) {
        return physicalAddressTypeRepository.findAll(
            Sort.by(Direction.ASC, "localeId", "sortIndex"));
      } else {
        return physicalAddressTypeRepository.findByLocaleIdIgnoreCase(
            localeId, Sort.by(Direction.ASC, "localeId", "sortIndex"));
      }
    } catch (Throwable e) {
      throw new ServiceUnavailableException("Failed to retrieve the physical address types", e);
    }
  }

  /**
   * Retrieve all the preference type categories.
   *
   * @return the preference type categories
   */
  @Override
  @Cacheable(value = "reference", key = "'preferenceTypeCategories.ALL'")
  public List<PreferenceTypeCategory> getPreferenceTypeCategories()
      throws ServiceUnavailableException {
    return getPreferenceTypeCategories(null);
  }

  /**
   * Retrieve the preference type categories.
   *
   * @param localeId the Unicode locale identifier for the locale to retrieve the preference type
   *     categories for or <b>null</b> to retrieve the preference type categories for all locales
   * @return the preference type categories
   */
  @Override
  @Cacheable(value = "reference", key = "'preferenceTypeCategories.' + #localeId")
  public List<PreferenceTypeCategory> getPreferenceTypeCategories(String localeId)
      throws ServiceUnavailableException {
    try {
      if (!StringUtils.hasText(localeId)) {
        return preferenceTypeCategoryRepository.findAll(
            Sort.by(Direction.ASC, "localeId", "sortIndex"));
      } else {
        return preferenceTypeCategoryRepository.findByLocaleIdIgnoreCase(
            localeId, Sort.by(Direction.ASC, "localeId", "sortIndex"));
      }
    } catch (Throwable e) {
      throw new ServiceUnavailableException("Failed to retrieve the preference type categories", e);
    }
  }

  /**
   * Retrieve all the preference types.
   *
   * @return the preference types
   */
  @Override
  @Cacheable(value = "reference", key = "'preferenceTypes.ALL'")
  public List<PreferenceType> getPreferenceTypes() throws ServiceUnavailableException {
    return getPreferenceTypes(null);
  }

  /**
   * Retrieve the preference types.
   *
   * @param localeId the Unicode locale identifier for the locale to retrieve the preference types
   *     for or <b>null</b> to retrieve the preference types for all locales
   * @return the preference types
   */
  @Override
  @Cacheable(value = "reference", key = "'preferenceTypes.' + #localeId")
  public List<PreferenceType> getPreferenceTypes(String localeId)
      throws ServiceUnavailableException {
    try {
      if (!StringUtils.hasText(localeId)) {
        return preferenceTypeRepository.findAll(Sort.by(Direction.ASC, "localeId", "sortIndex"));
      } else {
        return preferenceTypeRepository.findByLocaleIdIgnoreCase(
            localeId, Sort.by(Direction.ASC, "localeId", "sortIndex"));
      }
    } catch (Throwable e) {
      throw new ServiceUnavailableException("Failed to retrieve the preference types", e);
    }
  }

  /**
   * Retrieve all the races.
   *
   * @return the races
   */
  @Override
  @Cacheable(value = "reference", key = "'races.ALL'")
  public List<Race> getRaces() throws ServiceUnavailableException {
    return getRaces(null);
  }

  /**
   * Retrieve the races.
   *
   * @param localeId the Unicode locale identifier for the locale to retrieve the races for or
   *     <b>null</b> to retrieve the races for all locales
   * @return the races
   */
  @Override
  @Cacheable(value = "reference", key = "'races.' + #localeId")
  public List<Race> getRaces(String localeId) throws ServiceUnavailableException {
    try {
      if (!StringUtils.hasText(localeId)) {
        return raceRepository.findAll(Sort.by(Direction.ASC, "localeId", "sortIndex"));
      } else {
        return raceRepository.findByLocaleIdIgnoreCase(
            localeId, Sort.by(Direction.ASC, "localeId", "sortIndex"));
      }
    } catch (Throwable e) {
      throw new ServiceUnavailableException("Failed to retrieve the races", e);
    }
  }

  /**
   * Retrieve all the residence permit types.
   *
   * @return the residence permit types
   */
  @Override
  @Cacheable(value = "reference", key = "'residencePermitTypes.ALL'")
  public List<ResidencePermitType> getResidencePermitTypes() throws ServiceUnavailableException {
    return getResidencePermitTypes(null);
  }

  /**
   * Retrieve the residence permit types.
   *
   * @param localeId the Unicode locale identifier for the locale to retrieve the residence permit
   *     types for or <b>null</b> to retrieve the residence permit types for all locales
   * @return the residence permit types
   */
  @Override
  @Cacheable(value = "reference", key = "'residencePermitTypes.' + #localeId")
  public List<ResidencePermitType> getResidencePermitTypes(String localeId)
      throws ServiceUnavailableException {
    try {
      if (!StringUtils.hasText(localeId)) {
        return residencePermitTypeRepository.findAll(
            Sort.by(Direction.ASC, "localeId", "sortIndex"));
      } else {
        return residencePermitTypeRepository.findByLocaleIdIgnoreCase(
            localeId, Sort.by(Direction.ASC, "localeId", "sortIndex"));
      }
    } catch (Throwable e) {
      throw new ServiceUnavailableException("Failed to retrieve the residence permit types", e);
    }
  }

  /**
   * Retrieve all the residency statuses.
   *
   * @return the residency statuses
   */
  @Override
  @Cacheable(value = "reference", key = "'residencyStatuses.ALL'")
  public List<ResidencyStatus> getResidencyStatuses() throws ServiceUnavailableException {
    return getResidencyStatuses(null);
  }

  /**
   * Retrieve the residency statuses.
   *
   * @param localeId the Unicode locale identifier for the locale to retrieve the residency statuses
   *     for or <b>null</b> to retrieve the residency statuses for all locales
   * @return the residency statuses
   */
  @Override
  @Cacheable(value = "reference", key = "'residencyStatuses.' + #localeId")
  public List<ResidencyStatus> getResidencyStatuses(String localeId)
      throws ServiceUnavailableException {
    try {
      if (!StringUtils.hasText(localeId)) {
        return residencyStatusRepository.findAll(Sort.by(Direction.ASC, "localeId", "sortIndex"));
      } else {
        return residencyStatusRepository.findByLocaleIdIgnoreCase(
            localeId, Sort.by(Direction.ASC, "localeId", "sortIndex"));
      }
    } catch (Throwable e) {
      throw new ServiceUnavailableException("Failed to retrieve the residency statuses", e);
    }
  }

  /**
   * Retrieve all the residential types.
   *
   * @return the residential types
   */
  @Override
  @Cacheable(value = "reference", key = "'residentialTypes.ALL'")
  public List<ResidentialType> getResidentialTypes() throws ServiceUnavailableException {
    return getResidentialTypes(null);
  }

  /**
   * Retrieve the residential types.
   *
   * @param localeId the Unicode locale identifier for the locale to retrieve the residential types
   *     for or <b>null</b> to retrieve the residential types for all locales
   * @return the residential types
   */
  @Override
  @Cacheable(value = "reference", key = "'residentialTypes.' + #localeId")
  public List<ResidentialType> getResidentialTypes(String localeId)
      throws ServiceUnavailableException {
    try {
      if (!StringUtils.hasText(localeId)) {
        return residentialTypeRepository.findAll(Sort.by(Direction.ASC, "localeId", "sortIndex"));
      } else {
        return residentialTypeRepository.findByLocaleIdIgnoreCase(
            localeId, Sort.by(Direction.ASC, "localeId", "sortIndex"));
      }
    } catch (Throwable e) {
      throw new ServiceUnavailableException("Failed to retrieve the residential types", e);
    }
  }

  /**
   * Retrieve all the sources of funds.
   *
   * @return the sources of funds
   */
  @Override
  @Cacheable(value = "reference", key = "'sourcesOfFunds.ALL'")
  public List<SourceOfFunds> getSourcesOfFunds() throws ServiceUnavailableException {
    return getSourcesOfFunds(null);
  }

  /**
   * Retrieve the sources of funds.
   *
   * @param localeId the Unicode locale identifier for the locale to retrieve the sources of funds
   *     for or <b>null</b> to retrieve the sources of funds for all locales
   * @return the sources of funds
   */
  @Override
  @Cacheable(value = "reference", key = "'sourcesOfFunds.' + #localeId")
  public List<SourceOfFunds> getSourcesOfFunds(String localeId) throws ServiceUnavailableException {
    try {
      if (!StringUtils.hasText(localeId)) {
        return sourceOfFundsRepository.findAll(Sort.by(Direction.ASC, "localeId", "sortIndex"));
      } else {
        return sourceOfFundsRepository.findByLocaleIdIgnoreCase(
            localeId, Sort.by(Direction.ASC, "localeId", "sortIndex"));
      }
    } catch (Throwable e) {
      throw new ServiceUnavailableException("Failed to retrieve the sources of funds", e);
    }
  }

  /**
   * Retrieve all the tax number types.
   *
   * @return the tax number types
   */
  @Override
  @Cacheable(value = "reference", key = "'taxNumberTypes.ALL'")
  public List<TaxNumberType> getTaxNumberTypes() throws ServiceUnavailableException {
    return getTaxNumberTypes(null);
  }

  /**
   * Retrieve the tax number types.
   *
   * @param localeId the Unicode locale identifier for the locale to retrieve the tax number types
   *     for or <b>null</b> to retrieve the tax number types for all locales
   * @return the tax number types
   */
  @Override
  @Cacheable(value = "reference", key = "'taxNumberTypes.' + #localeId")
  public List<TaxNumberType> getTaxNumberTypes(String localeId) throws ServiceUnavailableException {
    try {
      if (!StringUtils.hasText(localeId)) {
        return taxNumberTypeRepository.findAll(Sort.by(Direction.ASC, "localeId", "sortIndex"));
      } else {
        return taxNumberTypeRepository.findByLocaleIdIgnoreCase(
            localeId, Sort.by(Direction.ASC, "localeId", "sortIndex"));
      }
    } catch (Throwable e) {
      throw new ServiceUnavailableException("Failed to retrieve the tax number types", e);
    }
  }

  /**
   * Retrieve all the times to contact.
   *
   * @return the times to contact
   */
  @Override
  @Cacheable(value = "reference", key = "'timesToContact.ALL'")
  public List<TimeToContact> getTimesToContact() throws ServiceUnavailableException {
    return getTimesToContact(null);
  }

  /**
   * Retrieve the times to contact.
   *
   * @param localeId the Unicode locale identifier for the locale to retrieve the times to contact
   *     for or <b>null</b> to retrieve the times to contact for all locales
   * @return the times to contact
   */
  @Override
  @Cacheable(value = "reference", key = "'timesToContact.' + #localeId")
  public List<TimeToContact> getTimesToContact(String localeId) throws ServiceUnavailableException {
    try {
      if (!StringUtils.hasText(localeId)) {
        return timeToContactRepository.findAll(Sort.by(Direction.ASC, "localeId", "sortIndex"));
      } else {
        return timeToContactRepository.findByLocaleIdIgnoreCase(
            localeId, Sort.by(Direction.ASC, "localeId", "sortIndex"));
      }
    } catch (Throwable e) {
      throw new ServiceUnavailableException("Failed to retrieve the times to contact", e);
    }
  }

  /**
   * Retrieve all the titles.
   *
   * @return the titles
   */
  @Override
  @Cacheable(value = "reference", key = "'titles.ALL'")
  public List<Title> getTitles() throws ServiceUnavailableException {
    return getTitles(null);
  }

  /**
   * Retrieve the titles.
   *
   * @param localeId the Unicode locale identifier for the locale to retrieve the titles for or
   *     <b>null</b> to retrieve the titles for all locales
   * @return the titles
   */
  @Override
  @Cacheable(value = "reference", key = "'titles.' + #localeId")
  public List<Title> getTitles(String localeId) throws ServiceUnavailableException {
    try {
      if (!StringUtils.hasText(localeId)) {
        return titleRepository.findAll(Sort.by(Direction.ASC, "localeId", "sortIndex"));
      } else {
        return titleRepository.findByLocaleIdIgnoreCase(
            localeId, Sort.by(Direction.ASC, "localeId", "sortIndex"));
      }
    } catch (Throwable e) {
      throw new ServiceUnavailableException("Failed to retrieve the titles", e);
    }
  }

  /**
   * Check whether the code is a valid code for a contact mechanism purpose for the party type.
   *
   * @param partyTypeCode the party type code
   * @param contactMechanismTypeCode the code for the contact mechanism type
   * @param contactMechanismPurposeCode the code for the contact mechanism purpose
   * @return <b>true</b> if the code is a valid code for a contact mechanism purpose or <b>false</b>
   *     otherwise
   */
  @Override
  public boolean isValidContactMechanismPurpose(
      String partyTypeCode, String contactMechanismTypeCode, String contactMechanismPurposeCode)
      throws ServiceUnavailableException {
    if (!StringUtils.hasText(contactMechanismPurposeCode)) {
      return false;
    }

    return self.getContactMechanismPurposes().stream()
        .anyMatch(
            contactMechanismPurpose ->
                (contactMechanismPurpose.getType().equals(contactMechanismTypeCode)
                    && contactMechanismPurpose.getCode().equals(contactMechanismPurposeCode)
                    && contactMechanismPurpose.isValidForPartyType(partyTypeCode)));
  }

  /**
   * Check whether the code is a valid code for a contact mechanism type.
   *
   * @param contactMechanismTypeCode the code for the contact mechanism type
   * @return <b>true</b> if the code is a valid code for a contact mechanism type or <b>false</b>
   *     otherwise
   */
  @Override
  public boolean isValidContactMechanismType(String contactMechanismTypeCode)
      throws ServiceUnavailableException {
    if (!StringUtils.hasText(contactMechanismTypeCode)) {
      return false;
    }

    return self.getContactMechanismTypes().stream()
        .anyMatch(
            contactMechanismType ->
                contactMechanismType.getCode().equals(contactMechanismTypeCode));
  }

  /**
   * Check whether the code is a valid code for an employment status.
   *
   * @param employmentStatusCode the code for the employment status
   * @return <b>true</b> if the code is a valid code for an employment status or <b>false</b>
   *     otherwise
   */
  @Override
  public boolean isValidEmploymentStatus(String employmentStatusCode)
      throws ServiceUnavailableException {
    if (!StringUtils.hasText(employmentStatusCode)) {
      return false;
    }

    return self.getEmploymentStatuses().stream()
        .anyMatch(employmentStatus -> employmentStatus.getCode().equals(employmentStatusCode));
  }

  /**
   * Check whether the code is a valid code for an employment type.
   *
   * @param employmentStatusCode the code for the employment status
   * @param employmentTypeCode the code for the employment type
   * @return <b>true</b> if the code is a valid code for an employment type or <b>false</b>
   *     otherwise
   */
  @Override
  public boolean isValidEmploymentType(String employmentStatusCode, String employmentTypeCode)
      throws ServiceUnavailableException {
    if (!StringUtils.hasText(employmentTypeCode)) {
      return false;
    }

    return self.getEmploymentTypes().stream()
        .anyMatch(
            employmentType ->
                (employmentType.getCode().equals(employmentTypeCode)
                    && employmentType.getEmploymentStatus().equals(employmentStatusCode)));
  }

  /**
   * Check whether the code is a valid code for a gender.
   *
   * @param genderCode the code for the gender
   * @return <b>true</b> if the code is a valid code for a gender or <b>false</b> otherwise
   */
  @Override
  public boolean isValidGender(String genderCode) throws ServiceUnavailableException {
    if (!StringUtils.hasText(genderCode)) {
      return false;
    }

    return self.getGenders().stream().anyMatch(gender -> gender.getCode().equals(genderCode));
  }

  /**
   * Check whether the code is a valid code for an identity document type for the party type
   *
   * @param partyTypeCode the party type code
   * @param identityDocumentTypeCode the code for the identity document type
   * @return <b>true</b> if the code is a valid code for an identity document type or <b>false</b>
   *     otherwise
   */
  @Override
  public boolean isValidIdentityDocumentType(String partyTypeCode, String identityDocumentTypeCode)
      throws ServiceUnavailableException {
    if (!StringUtils.hasText(identityDocumentTypeCode)) {
      return false;
    }

    return self.getIdentityDocumentTypes().stream()
        .anyMatch(
            identityDocumentType ->
                (identityDocumentType.getCode().equals(identityDocumentTypeCode)
                    && identityDocumentType.isValidForPartyType(partyTypeCode)));
  }

  /**
   * Check whether the code is a valid code for a marital status.
   *
   * @param maritalStatusCode the code for the marital status
   * @return <b>true</b> if the code is a valid code for a marital status or <b>false</b> otherwise
   */
  @Override
  public boolean isValidMaritalStatus(String maritalStatusCode) throws ServiceUnavailableException {
    if (!StringUtils.hasText(maritalStatusCode)) {
      return false;
    }

    return self.getMaritalStatuses().stream()
        .anyMatch(maritalStatus -> maritalStatus.getCode().equals(maritalStatusCode));
  }

  /**
   * Check whether the code is a valid code for a marriage type.
   *
   * @param maritalStatusCode the code for the marital status
   * @param marriageTypeCode the code for the marriage type
   * @return <b>true</b> if the code is a valid code for a marriage type or <b>false</b> otherwise
   */
  @Override
  public boolean isValidMarriageType(String maritalStatusCode, String marriageTypeCode)
      throws ServiceUnavailableException {
    if (!StringUtils.hasText(maritalStatusCode)) {
      return true;
    }

    // Find marriage types for the specified marital status
    List<MarriageType> marriageTypes =
        self.getMarriageTypes().stream()
            .filter(marriageType -> marriageType.getMaritalStatus().equals(maritalStatusCode))
            .collect(Collectors.toList());

    // If we have marriage types for the specified marital status then check if one matches
    if (marriageTypes.size() > 0) {
      return marriageTypes.stream()
          .anyMatch(
              marriageType ->
                  (marriageType.getCode().equals(marriageTypeCode)
                      && marriageType.getMaritalStatus().equals(maritalStatusCode)));
    } else {
      return true;
    }
  }

  /**
   * Check whether the code is a valid code for a next of kin type.
   *
   * @param nextOfKinTypeCode the code for the next of kin type
   * @return <b>true</b> if the code is a valid code for a next of kin type or <b>false</b>
   *     otherwise
   */
  @Override
  public boolean isValidNextOfKinType(String nextOfKinTypeCode) throws ServiceUnavailableException {
    if (!StringUtils.hasText(nextOfKinTypeCode)) {
      return false;
    }

    return self.getNextOfKinTypes().stream()
        .anyMatch(nextOfKinType -> nextOfKinType.getCode().equals(nextOfKinTypeCode));
  }

  /**
   * Check whether the code is a valid code for a occupation.
   *
   * @param occupationCode the code for the occupation
   * @return <b>true</b> if the code is a valid code for a occupation or <b>false</b> otherwise
   */
  @Override
  public boolean isValidOccupation(String occupationCode) throws ServiceUnavailableException {
    if (!StringUtils.hasText(occupationCode)) {
      return false;
    }

    return self.getOccupations().stream()
        .anyMatch(occupation -> occupation.getCode().equals(occupationCode));
  }

  /**
   * Check whether the code is a valid code for a party attribute type for the party type.
   *
   * @param partyTypeCode the party type code
   * @param partyAttributeTypeCode the code for the party attribute type
   * @return <b>true</b> if the code is a valid code for a party attribute type or <b>false</b>
   *     otherwise
   */
  @Override
  public boolean isValidPartyAttributeType(String partyTypeCode, String partyAttributeTypeCode)
      throws ServiceUnavailableException {
    if (!StringUtils.hasText(partyAttributeTypeCode)) {
      return false;
    }

    return self.getPartyAttributeTypes().stream()
        .anyMatch(
            partyAttributeType ->
                (partyAttributeType.getCode().equals(partyAttributeTypeCode)
                    && partyAttributeType.isValidForPartyType(partyTypeCode)));
  }

  /**
   * Check whether the code is a valid code for a party attribute type category.
   *
   * @param partyAttributeTypeCategoryCode the code for the party attribute type category
   * @return <b>true</b> if the code is a valid code for a party attribute type category or
   *     <b>false</b> otherwise
   */
  @Override
  public boolean isValidPartyAttributeTypeCategory(String partyAttributeTypeCategoryCode)
      throws ServiceUnavailableException {
    if (!StringUtils.hasText(partyAttributeTypeCategoryCode)) {
      return false;
    }

    return self.getPartyAttributeTypeCategories().stream()
        .anyMatch(
            partyAttributeTypeCategory ->
                partyAttributeTypeCategory.getCode().equals(partyAttributeTypeCategoryCode));
  }

  /**
   * Check whether the code is a valid code for a party role purpose.
   *
   * @param partyRolePurposeCode the code for the party role purpose
   * @return <b>true</b> if the code is a valid code for a party role purpose or <b>false</b>
   *     otherwise
   */
  @Override
  public boolean isValidPartyRolePurpose(String partyRolePurposeCode)
      throws ServiceUnavailableException {
    if (!StringUtils.hasText(partyRolePurposeCode)) {
      return false;
    }

    return self.getPartyRolePurposes().stream()
        .anyMatch(partyRolePurpose -> partyRolePurpose.getCode().equals(partyRolePurposeCode));
  }

  /**
   * Check whether the code is a valid code for a party role type for the party type.
   *
   * @param partyTypeCode the party type code
   * @param partyRoleTypeCode the code for the party role type
   * @return <b>true</b> if the code is a valid code for a party role type or <b>false</b> otherwise
   */
  @Override
  public boolean isValidPartyRoleType(String partyTypeCode, String partyRoleTypeCode)
      throws ServiceUnavailableException {
    if (!StringUtils.hasText(partyRoleTypeCode)) {
      return false;
    }

    return self.getPartyRoleTypes().stream()
        .anyMatch(
            partyRoleType ->
                (partyRoleType.getCode().equals(partyRoleTypeCode)
                    && partyRoleType.isValidForPartyType(partyTypeCode)));
  }

  /**
   * Check whether the code is a valid code for a physical address purpose for the party type.
   *
   * @param partyTypeCode the party type code
   * @param physicalAddressPurposeCode the code for the physical address purpose
   * @return <b>true</b> if the code is a valid code for a physical address purpose or <b>false</b>
   *     otherwise
   */
  @Override
  public boolean isValidPhysicalAddressPurpose(
      String partyTypeCode, String physicalAddressPurposeCode) throws ServiceUnavailableException {
    if (!StringUtils.hasText(physicalAddressPurposeCode)) {
      return false;
    }

    return self.getPhysicalAddressPurposes().stream()
        .anyMatch(
            physicalAddressPurpose ->
                (physicalAddressPurpose.getCode().equals(physicalAddressPurposeCode)
                    && physicalAddressPurpose.isValidForPartyType(partyTypeCode)));
  }

  /**
   * Check whether the code is a valid code for a physical address type.
   *
   * @param physicalAddressTypeCode the code for the physical address type
   * @return <b>true</b> if the code is a valid code for a physical address type or <b>false</b>
   *     otherwise
   */
  @Override
  public boolean isValidPhysicalAddressType(String physicalAddressTypeCode)
      throws ServiceUnavailableException {
    if (!StringUtils.hasText(physicalAddressTypeCode)) {
      return false;
    }

    return self.getPhysicalAddressTypes().stream()
        .anyMatch(
            physicalAddressType -> physicalAddressType.getCode().equals(physicalAddressTypeCode));
  }

  /**
   * Check whether the code is a valid code for a preference type for the party type.
   *
   * @param partyTypeCode the party type code
   * @param preferenceTypeCode the code for the preference type
   * @return <b>true</b> if the code is a valid code for a preference type or <b>false</b> otherwise
   */
  @Override
  public boolean isValidPreferenceType(String partyTypeCode, String preferenceTypeCode)
      throws ServiceUnavailableException {
    if (!StringUtils.hasText(preferenceTypeCode)) {
      return false;
    }

    return self.getPreferenceTypes().stream()
        .anyMatch(
            preferenceType ->
                (preferenceType.getCode().equals(preferenceTypeCode)
                    && preferenceType.isValidForPartyType(partyTypeCode)));
  }

  /**
   * Check whether the code is a valid code for a preference type category.
   *
   * @param preferenceTypeCategoryCode the code for the preference type category
   * @return <b>true</b> if the code is a valid code for a preference type category or <b>false</b>
   *     otherwise
   */
  @Override
  public boolean isValidPreferenceTypeCategory(String preferenceTypeCategoryCode)
      throws ServiceUnavailableException {
    if (!StringUtils.hasText(preferenceTypeCategoryCode)) {
      return false;
    }

    return self.getPreferenceTypeCategories().stream()
        .anyMatch(
            preferenceTypeCategory ->
                preferenceTypeCategory.getCode().equals(preferenceTypeCategoryCode));
  }

  /**
   * Check whether the code is a valid code for a race.
   *
   * @param raceCode the code for the race
   * @return <b>true</b> if the code is a valid code for a race or <b>false</b> otherwise
   */
  @Override
  public boolean isValidRace(String raceCode) throws ServiceUnavailableException {
    if (!StringUtils.hasText(raceCode)) {
      return false;
    }

    return self.getRaces().stream().anyMatch(race -> race.getCode().equals(raceCode));
  }

  /**
   * Check whether the code is a valid code for a residence permit type.
   *
   * @param residencePermitTypeCode the code for the residence permit type
   * @return <b>true</b> if the code is a valid code for a residence permit type or <b>false</b>
   *     otherwise
   */
  @Override
  public boolean isValidResidencePermitType(String residencePermitTypeCode)
      throws ServiceUnavailableException {
    if (!StringUtils.hasText(residencePermitTypeCode)) {
      return false;
    }

    return self.getResidencePermitTypes().stream()
        .anyMatch(
            residencePermitType -> residencePermitType.getCode().equals(residencePermitTypeCode));
  }

  /**
   * Check whether the code is a valid code for a residency status.
   *
   * @param residencyStatusCode the code for the residency status
   * @return <b>true</b> if the code is a valid code for a residency status or <b>false</b>
   *     otherwise
   */
  @Override
  public boolean isValidResidencyStatus(String residencyStatusCode)
      throws ServiceUnavailableException {
    if (!StringUtils.hasText(residencyStatusCode)) {
      return false;
    }

    return self.getResidencyStatuses().stream()
        .anyMatch(residencyStatus -> residencyStatus.getCode().equals(residencyStatusCode));
  }

  /**
   * Check whether the code is a valid code for a residential type.
   *
   * @param residentialTypeCode the code for the residential type
   * @return <b>true</b> if the code is a valid code for a residential type or <b>false</b>
   *     otherwise
   */
  @Override
  public boolean isValidResidentialType(String residentialTypeCode)
      throws ServiceUnavailableException {
    if (!StringUtils.hasText(residentialTypeCode)) {
      return false;
    }

    return self.getResidentialTypes().stream()
        .anyMatch(residentialType -> residentialType.getCode().equals(residentialTypeCode));
  }

  /**
   * Check whether the code is a valid code for a source of funds.
   *
   * @param sourceOfFundsCode the code for the source of funds
   * @return <b>true</b> if the code is a valid code for a source of funds or <b>false</b> otherwise
   */
  @Override
  public boolean isValidSourceOfFunds(String sourceOfFundsCode) throws ServiceUnavailableException {
    if (!StringUtils.hasText(sourceOfFundsCode)) {
      return false;
    }

    return self.getSourcesOfFunds().stream()
        .anyMatch(sourceOfFunds -> sourceOfFunds.getCode().equals(sourceOfFundsCode));
  }

  /**
   * Check whether the code is a valid code for a tax number type.
   *
   * @param taxNumberTypeCode the code for the tax number type
   * @return <b>true</b> if the code is a valid code for a tax number type or <b>false</b> otherwise
   */
  @Override
  public boolean isValidTaxNumberType(String taxNumberTypeCode) throws ServiceUnavailableException {
    if (!StringUtils.hasText(taxNumberTypeCode)) {
      return false;
    }

    return self.getTaxNumberTypes().stream()
        .anyMatch(taxNumberType -> taxNumberType.getCode().equals(taxNumberTypeCode));
  }

  /**
   * Check whether the code is a valid code for a time to contact.
   *
   * @param timeToContactCode the code for the time to contact
   * @return <b>true</b> if the code is a valid code for a time to contact or <b>false</b> otherwise
   */
  @Override
  public boolean isValidTimeToContact(String timeToContactCode) throws ServiceUnavailableException {
    if (!StringUtils.hasText(timeToContactCode)) {
      return false;
    }

    return self.getTimesToContact().stream()
        .anyMatch(timeToContact -> timeToContact.getCode().equals(timeToContactCode));
  }

  /**
   * Check whether the code is a valid code for a title.
   *
   * @param titleCode the code for the title
   * @return <b>true</b> if the code is a valid code for a title or <b>false</b> otherwise
   */
  @Override
  public boolean isValidTitle(String titleCode) throws ServiceUnavailableException {
    if (!StringUtils.hasText(titleCode)) {
      return false;
    }

    return self.getTitles().stream().anyMatch(title -> title.getCode().equals(titleCode));
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
