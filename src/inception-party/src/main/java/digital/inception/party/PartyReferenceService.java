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
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import javax.annotation.Resource;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * The <b>PartyReferenceService</b> class provides the Party Reference Service implementation.
 *
 * @author Marcus Portmann
 */
@Service
public class PartyReferenceService implements IPartyReferenceService {

  /** The Party Attribute Type Category Repository. */
  private final AttributeTypeCategoryRepository attributeTypeCategoryRepository;

  /** The Party Attribute Type Repository. */
  private final AttributeTypeRepository attributeTypeRepository;

  /** The Consent Type Repository */
  private final ConsentTypeRepository consentTypeRepository;

  /** The Contact Mechanism Purpose Repository. */
  private final ContactMechanismPurposeRepository contactMechanismPurposeRepository;

  /** The Contact Mechanism Role Repository. */
  private final ContactMechanismRoleRepository contactMechanismRoleRepository;

  /** The Contact Mechanism Type Repository. */
  private final ContactMechanismTypeRepository contactMechanismTypeRepository;

  /** The Employment Status Repository */
  private final EmploymentStatusRepository employmentStatusRepository;

  /** The Employment Type Repository. */
  private final EmploymentTypeRepository employmentTypeRepository;

  /** The Field Of Study Repository */
  private final FieldOfStudyRepository fieldOfStudyRepository;

  /** The Gender Repository. */
  private final GenderRepository genderRepository;

  /** The Identity Document Type Repository. */
  private final IdentityDocumentTypeRepository identityDocumentTypeRepository;

  /** The Lock Type Category Repository. */
  private final LockTypeCategoryRepository lockTypeCategoryRepository;

  /** The Lock Type Repository */
  private final LockTypeRepository lockTypeRepository;

  /** The Marital Status Repository. */
  private final MaritalStatusRepository maritalStatusRepository;

  /** The Marriage Type Repository. */
  private final MarriageTypeRepository marriageTypeRepository;

  /** The Next Of Kin Type Repository. */
  private final NextOfKinTypeRepository nextOfKinTypeRepository;

  /** The Occupation Repository. */
  private final OccupationRepository occupationRepository;

  /** The Physical Address Purpose Repository. */
  private final PhysicalAddressPurposeRepository physicalAddressPurposeRepository;

  /** The Physical Address Role Repository. */
  private final PhysicalAddressRoleRepository physicalAddressRoleRepository;

  /** The Physical Address Type Repository. */
  private final PhysicalAddressTypeRepository physicalAddressTypeRepository;

  /** The Preference Type Category Repository. */
  private final PreferenceTypeCategoryRepository preferenceTypeCategoryRepository;

  /** The Preference Type Repository */
  private final PreferenceTypeRepository preferenceTypeRepository;

  /** The Qualification Type Repository. */
  private final QualificationTypeRepository qualificationTypeRepository;

  /** The Race Repository. */
  private final RaceRepository raceRepository;

  /** The Relationship Type Repository. */
  private final RelationshipTypeRepository relationshipTypeRepository;

  /** The Residence Permit Type Repository. */
  private final ResidencePermitTypeRepository residencePermitTypeRepository;

  /** The Residency Status Repository. */
  private final ResidencyStatusRepository residencyStatusRepository;

  /** The Residential Type Repository. */
  private final ResidentialTypeRepository residentialTypeRepository;

  /** The Party Role Purpose Repository. */
  private final RolePurposeRepository rolePurposeRepository;

  /** The Role Type Attribute Type Constraint Repository. */
  private final RoleTypeAttributeTypeConstraintRepository roleTypeAttributeTypeConstraintRepository;

  /** The Role Type Preference Type Constraint Repository. */
  private final RoleTypePreferenceTypeConstraintRepository
      roleTypePreferenceTypeConstraintRepository;

  /** The Party Role Type Repository. */
  private final RoleTypeRepository roleTypeRepository;

  /** The Segment Repository. */
  private final SegmentRepository segmentRepository;

  /** The Source of Funds Type Repository. */
  private final SourceOfFundsTypeRepository sourceOfFundsTypeRepository;

  /** The Source of Wealth Type Repository. */
  private final SourceOfWealthTypeRepository sourceOfWealthTypeRepository;

  /** The Status Type Category Repository. */
  private final StatusTypeCategoryRepository statusTypeCategoryRepository;

  /** The Status Type Repository */
  private final StatusTypeRepository statusTypeRepository;

  /** The Tax Number Type Repository. */
  private final TaxNumberTypeRepository taxNumberTypeRepository;

  /** The Time To Contact Repository. */
  private final TimeToContactRepository timeToContactRepository;

  /** The Title Repository. */
  private final TitleRepository titleRepository;

  /** The internal reference to the Party Reference Service to enable caching. */
  @Resource private IPartyReferenceService self;

  /**
   * Constructs a new <b>PartyReferenceService</b>.
   *
   * @param attributeTypeCategoryRepository the Attribute Type Category Repository
   * @param attributeTypeRepository the Attribute Type Repository
   * @param consentTypeRepository the Consent Type Repository
   * @param contactMechanismPurposeRepository the Contact Mechanism Purpose Repository
   * @param contactMechanismRoleRepository the Contact Mechanism Role Repository
   * @param contactMechanismTypeRepository the Contact Mechanism Type Repository
   * @param employmentStatusRepository the Employment Status Repository
   * @param employmentTypeRepository the Employment Type Repository
   * @param fieldOfStudyRepository the Field Of Study Repository
   * @param genderRepository the Gender Repository
   * @param identityDocumentTypeRepository the Identity Document Type Repository
   * @param lockTypeCategoryRepository the Lock Type Category Repository
   * @param lockTypeRepository the Lock Type Repository
   * @param maritalStatusRepository the Marital Status Repository
   * @param marriageTypeRepository the Marriage Type Repository
   * @param nextOfKinTypeRepository the Next Of Kin Repository
   * @param occupationRepository the Occupation Repository
   * @param physicalAddressPurposeRepository the Physical Address Purpose Repository
   * @param physicalAddressRoleRepository the Physical Address Role Repository
   * @param physicalAddressTypeRepository the Physical Address Type Repository
   * @param preferenceTypeCategoryRepository the Preference Type Category Repository
   * @param preferenceTypeRepository the Preference Type Repository
   * @param qualificationTypeRepository the Qualification Type Repository
   * @param raceRepository the Race Repository
   * @param relationshipTypeRepository the Relationship Type Repository
   * @param residencePermitTypeRepository the Residence Permit Type Repository
   * @param residencyStatusRepository the Residency Status Repository
   * @param residentialTypeRepository the Residential Type Repository
   * @param rolePurposeRepository the Party Role Purpose Repository
   * @param roleTypeRepository the Party Role Type Repository
   * @param roleTypeAttributeTypeConstraintRepository the Role Type Attribute Type Constraint
   *     Repository
   * @param roleTypePreferenceTypeConstraintRepository the Role Type Preference Type Constraint
   *     Repository
   * @param segmentRepository the Segment Repository
   * @param sourceOfFundsTypeRepository the Source Of Funds Repository
   * @param sourceOfWealthTypeRepository the Source Of Wealth Repository
   * @param statusTypeCategoryRepository the Status Type Category Repository
   * @param statusTypeRepository the Status Type Repository
   * @param taxNumberTypeRepository the Tax Number Type Repository
   * @param timeToContactRepository the Time To Contact Repository
   * @param titleRepository the Title Repository
   */
  public PartyReferenceService(
      AttributeTypeCategoryRepository attributeTypeCategoryRepository,
      AttributeTypeRepository attributeTypeRepository,
      ConsentTypeRepository consentTypeRepository,
      ContactMechanismPurposeRepository contactMechanismPurposeRepository,
      ContactMechanismRoleRepository contactMechanismRoleRepository,
      ContactMechanismTypeRepository contactMechanismTypeRepository,
      EmploymentStatusRepository employmentStatusRepository,
      EmploymentTypeRepository employmentTypeRepository,
      FieldOfStudyRepository fieldOfStudyRepository,
      GenderRepository genderRepository,
      IdentityDocumentTypeRepository identityDocumentTypeRepository,
      LockTypeCategoryRepository lockTypeCategoryRepository,
      LockTypeRepository lockTypeRepository,
      MaritalStatusRepository maritalStatusRepository,
      MarriageTypeRepository marriageTypeRepository,
      NextOfKinTypeRepository nextOfKinTypeRepository,
      OccupationRepository occupationRepository,
      PhysicalAddressPurposeRepository physicalAddressPurposeRepository,
      PhysicalAddressRoleRepository physicalAddressRoleRepository,
      PhysicalAddressTypeRepository physicalAddressTypeRepository,
      PreferenceTypeCategoryRepository preferenceTypeCategoryRepository,
      PreferenceTypeRepository preferenceTypeRepository,
      QualificationTypeRepository qualificationTypeRepository,
      RaceRepository raceRepository,
      RelationshipTypeRepository relationshipTypeRepository,
      ResidencePermitTypeRepository residencePermitTypeRepository,
      ResidencyStatusRepository residencyStatusRepository,
      ResidentialTypeRepository residentialTypeRepository,
      RolePurposeRepository rolePurposeRepository,
      RoleTypeRepository roleTypeRepository,
      RoleTypeAttributeTypeConstraintRepository roleTypeAttributeTypeConstraintRepository,
      RoleTypePreferenceTypeConstraintRepository roleTypePreferenceTypeConstraintRepository,
      SegmentRepository segmentRepository,
      SourceOfFundsTypeRepository sourceOfFundsTypeRepository,
      SourceOfWealthTypeRepository sourceOfWealthTypeRepository,
      StatusTypeCategoryRepository statusTypeCategoryRepository,
      StatusTypeRepository statusTypeRepository,
      TaxNumberTypeRepository taxNumberTypeRepository,
      TimeToContactRepository timeToContactRepository,
      TitleRepository titleRepository) {
    this.attributeTypeCategoryRepository = attributeTypeCategoryRepository;
    this.attributeTypeRepository = attributeTypeRepository;
    this.consentTypeRepository = consentTypeRepository;
    this.contactMechanismPurposeRepository = contactMechanismPurposeRepository;
    this.contactMechanismRoleRepository = contactMechanismRoleRepository;
    this.contactMechanismTypeRepository = contactMechanismTypeRepository;
    this.employmentStatusRepository = employmentStatusRepository;
    this.employmentTypeRepository = employmentTypeRepository;
    this.fieldOfStudyRepository = fieldOfStudyRepository;
    this.genderRepository = genderRepository;
    this.identityDocumentTypeRepository = identityDocumentTypeRepository;
    this.lockTypeCategoryRepository = lockTypeCategoryRepository;
    this.lockTypeRepository = lockTypeRepository;
    this.maritalStatusRepository = maritalStatusRepository;
    this.marriageTypeRepository = marriageTypeRepository;
    this.nextOfKinTypeRepository = nextOfKinTypeRepository;
    this.occupationRepository = occupationRepository;
    this.physicalAddressPurposeRepository = physicalAddressPurposeRepository;
    this.physicalAddressRoleRepository = physicalAddressRoleRepository;
    this.physicalAddressTypeRepository = physicalAddressTypeRepository;
    this.preferenceTypeCategoryRepository = preferenceTypeCategoryRepository;
    this.preferenceTypeRepository = preferenceTypeRepository;
    this.qualificationTypeRepository = qualificationTypeRepository;
    this.raceRepository = raceRepository;
    this.relationshipTypeRepository = relationshipTypeRepository;
    this.residencePermitTypeRepository = residencePermitTypeRepository;
    this.residencyStatusRepository = residencyStatusRepository;
    this.residentialTypeRepository = residentialTypeRepository;
    this.rolePurposeRepository = rolePurposeRepository;
    this.roleTypeRepository = roleTypeRepository;
    this.roleTypeAttributeTypeConstraintRepository = roleTypeAttributeTypeConstraintRepository;
    this.roleTypePreferenceTypeConstraintRepository = roleTypePreferenceTypeConstraintRepository;
    this.segmentRepository = segmentRepository;
    this.sourceOfFundsTypeRepository = sourceOfFundsTypeRepository;
    this.sourceOfWealthTypeRepository = sourceOfWealthTypeRepository;
    this.statusTypeCategoryRepository = statusTypeCategoryRepository;
    this.statusTypeRepository = statusTypeRepository;
    this.taxNumberTypeRepository = taxNumberTypeRepository;
    this.timeToContactRepository = timeToContactRepository;
    this.titleRepository = titleRepository;
  }

  /**
   * Retrieve the attribute type categories.
   *
   * @param localeId the Unicode locale identifier for the locale to retrieve the attribute type
   *     categories for or <b>null</b> to retrieve the attribute type categories for all locales
   * @return the attribute type categories
   */
  @Override
  @Cacheable(cacheNames = "reference", key = "'attributeTypeCategories.' + #localeId")
  public List<AttributeTypeCategory> getAttributeTypeCategories(String localeId)
      throws ServiceUnavailableException {
    try {
      if (!StringUtils.hasText(localeId)) {
        return attributeTypeCategoryRepository.findAll(
            Sort.by(Direction.ASC, "localeId", "sortIndex"));
      } else {
        return attributeTypeCategoryRepository.findByLocaleIdIgnoreCase(
            localeId, Sort.by(Direction.ASC, "localeId", "sortIndex"));
      }
    } catch (Throwable e) {
      throw new ServiceUnavailableException("Failed to retrieve the attribute type categories", e);
    }
  }

  /**
   * Retrieve the attribute types.
   *
   * @param localeId the Unicode locale identifier for the locale to retrieve the attribute types
   *     for or <b>null</b> to retrieve the attribute types for all locales
   * @return the attribute types
   */
  @Override
  @Cacheable(cacheNames = "reference", key = "'attributeTypes.' + #localeId")
  public List<AttributeType> getAttributeTypes(String localeId) throws ServiceUnavailableException {
    try {
      if (!StringUtils.hasText(localeId)) {
        return attributeTypeRepository.findAll(Sort.by(Direction.ASC, "localeId", "sortIndex"));
      } else {
        return attributeTypeRepository.findByLocaleIdIgnoreCase(
            localeId, Sort.by(Direction.ASC, "localeId", "sortIndex"));
      }
    } catch (Throwable e) {
      throw new ServiceUnavailableException("Failed to retrieve the attribute types", e);
    }
  }

  /**
   * Retrieve the consent types.
   *
   * @param localeId the Unicode locale identifier for the locale to retrieve the consent types for
   *     or <b>null</b> to retrieve the consent types for all locales
   * @return the consent types
   */
  @Override
  @Cacheable(cacheNames = "reference", key = "'consentTypes.' + #localeId")
  public List<ConsentType> getConsentTypes(String localeId) throws ServiceUnavailableException {
    try {
      if (!StringUtils.hasText(localeId)) {
        return consentTypeRepository.findAll(Sort.by(Direction.ASC, "localeId", "sortIndex"));
      } else {
        return consentTypeRepository.findByLocaleIdIgnoreCase(
            localeId, Sort.by(Direction.ASC, "localeId", "sortIndex"));
      }
    } catch (Throwable e) {
      throw new ServiceUnavailableException("Failed to retrieve the consent types", e);
    }
  }

  /**
   * Retrieve the contact mechanism purposes.
   *
   * @param localeId the Unicode locale identifier for the locale to retrieve the contact mechanism
   *     purposes for or <b>null</b> to retrieve the contact mechanism purposes for all locales
   * @return the contact mechanism purposes
   */
  @Override
  @Cacheable(cacheNames = "reference", key = "'contactMechanismPurposes.' + #localeId")
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
   * Retrieve the contact mechanism roles.
   *
   * @param localeId the Unicode locale identifier for the locale to retrieve the contact mechanism
   *     purposes for or <b>null</b> to retrieve the contact mechanism roles for all locales
   * @return the contact mechanism roles
   */
  @Override
  @Cacheable(cacheNames = "reference", key = "'contactMechanismRoles.' + #localeId")
  public List<ContactMechanismRole> getContactMechanismRoles(String localeId)
      throws ServiceUnavailableException {
    try {
      if (!StringUtils.hasText(localeId)) {
        return contactMechanismRoleRepository.findAll(
            Sort.by(Direction.ASC, "localeId", "sortIndex"));
      } else {
        return contactMechanismRoleRepository.findByLocaleIdIgnoreCase(
            localeId, Sort.by(Direction.ASC, "localeId", "sortIndex"));
      }
    } catch (Throwable e) {
      throw new ServiceUnavailableException("Failed to retrieve the contact mechanism roles", e);
    }
  }

  /**
   * Retrieve the contact mechanism types.
   *
   * @param localeId the Unicode locale identifier for the locale to retrieve the contact mechanism
   *     types for or <b>null</b> to retrieve the contact mechanism types for all locales
   * @return the contact mechanism types
   */
  @Override
  @Cacheable(cacheNames = "reference", key = "'contactMechanismTypes.' + #localeId")
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
   * Retrieve the employment statuses.
   *
   * @param localeId the Unicode locale identifier for the locale to retrieve the employment
   *     statuses for or <b>null</b> to retrieve the employment statuses for all locales
   * @return the employment statuses
   */
  @Override
  @Cacheable(cacheNames = "reference", key = "'employmentStatuses.' + #localeId")
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
   * Retrieve the employment types.
   *
   * @param localeId the Unicode locale identifier for the locale to retrieve the employment types
   *     for or <b>null</b> to retrieve the employment types for all locales
   * @return the employment types
   */
  @Override
  @Cacheable(cacheNames = "reference", key = "'employmentTypes.' + #localeId")
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
   * Retrieve the fields of study.
   *
   * @param localeId the Unicode locale identifier for the locale to retrieve the qualification
   *     types for or <b>null</b> to retrieve the fields of study for all locales
   * @return the fields of study
   */
  @Override
  @Cacheable(cacheNames = "reference", key = "'fieldsOfStudy.' + #localeId")
  public List<FieldOfStudy> getFieldsOfStudy(String localeId) throws ServiceUnavailableException {
    try {
      if (!StringUtils.hasText(localeId)) {
        return fieldOfStudyRepository.findAll(Sort.by(Direction.ASC, "localeId", "sortIndex"));
      } else {
        return fieldOfStudyRepository.findByLocaleIdIgnoreCase(
            localeId, Sort.by(Direction.ASC, "localeId", "sortIndex"));
      }
    } catch (Throwable e) {
      throw new ServiceUnavailableException("Failed to retrieve the fields of study", e);
    }
  }

  /**
   * Retrieve the genders.
   *
   * @param localeId the Unicode locale identifier for the locale to retrieve the genders for or
   *     <b>null</b> to retrieve the genders for all locales
   * @return the genders
   */
  @Override
  @Cacheable(cacheNames = "reference", key = "'genders.' + #localeId")
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
   * Retrieve the identity document types.
   *
   * @param localeId the Unicode locale identifier for the locale to retrieve the identity document
   *     types for or <b>null</b> to retrieve the identity document types for all locales
   * @return the identity document types
   */
  @Override
  @Cacheable(cacheNames = "reference", key = "'identityDocumentTypes.' + #localeId")
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
   * Retrieve the lock type categories.
   *
   * @param localeId the Unicode locale identifier for the locale to retrieve the lock type
   *     categories for or <b>null</b> to retrieve the lock type categories for all locales
   * @return the lock type categories
   */
  @Override
  @Cacheable(cacheNames = "reference", key = "'lockTypeCategories.' + #localeId")
  public List<LockTypeCategory> getLockTypeCategories(String localeId)
      throws ServiceUnavailableException {
    try {
      if (!StringUtils.hasText(localeId)) {
        return lockTypeCategoryRepository.findAll(Sort.by(Direction.ASC, "localeId", "sortIndex"));
      } else {
        return lockTypeCategoryRepository.findByLocaleIdIgnoreCase(
            localeId, Sort.by(Direction.ASC, "localeId", "sortIndex"));
      }
    } catch (Throwable e) {
      throw new ServiceUnavailableException("Failed to retrieve the lock type categories", e);
    }
  }

  /**
   * Retrieve the lock types.
   *
   * @param localeId the Unicode locale identifier for the locale to retrieve the lock types for or
   *     <b>null</b> to retrieve the lock types for all locales
   * @return the lock types
   */
  @Override
  @Cacheable(cacheNames = "reference", key = "'lockTypes.' + #localeId")
  public List<LockType> getLockTypes(String localeId) throws ServiceUnavailableException {
    try {
      if (!StringUtils.hasText(localeId)) {
        return lockTypeRepository.findAll(Sort.by(Direction.ASC, "localeId", "sortIndex"));
      } else {
        return lockTypeRepository.findByLocaleIdIgnoreCase(
            localeId, Sort.by(Direction.ASC, "localeId", "sortIndex"));
      }
    } catch (Throwable e) {
      throw new ServiceUnavailableException("Failed to retrieve the lock types", e);
    }
  }

  /**
   * Retrieve the marital statuses.
   *
   * @param localeId the Unicode locale identifier for the locale to retrieve the marital statuses
   *     for or <b>null</b> to retrieve the marital statuses for all locales
   * @return the marital statuses
   */
  @Override
  @Cacheable(cacheNames = "reference", key = "'maritalStatuses.' + #localeId")
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
   * Retrieve the marriage types.
   *
   * @param localeId the Unicode locale identifier for the locale to retrieve the marriage types for
   *     or <b>null</b> to retrieve the marriage types for all locales
   * @return the marriage types
   */
  @Override
  @Cacheable(cacheNames = "reference", key = "'marriageTypes.' + #localeId")
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
   * Retrieve the next of kin types.
   *
   * @param localeId the Unicode locale identifier for the locale to retrieve the next of kin types
   *     for or <b>null</b> to retrieve the next of kin types for all locales
   * @return the next of kin types
   */
  @Override
  @Cacheable(cacheNames = "reference", key = "'nextOfKinTypes.' + #localeId")
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
   * Retrieve the occupations.
   *
   * @param localeId the Unicode locale identifier for the locale to retrieve the occupations for or
   *     <b>null</b> to retrieve the occupations for all locales
   * @return the occupations
   */
  @Override
  @Cacheable(cacheNames = "reference", key = "'occupations.' + #localeId")
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
   * Retrieve the physical address purposes.
   *
   * @param localeId the Unicode locale identifier for the locale to retrieve the physical address
   *     purposes for or <b>null</b> to retrieve the physical address purposes for all locales
   * @return the physical address purposes
   */
  @Override
  @Cacheable(cacheNames = "reference", key = "'physicalAddressPurposes.' + #localeId")
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
   * Retrieve the physical address roles.
   *
   * @param localeId the Unicode locale identifier for the locale to retrieve the physical address
   *     roles for or <b>null</b> to retrieve the physical address roles for all locales
   * @return the physical address roles
   */
  @Override
  @Cacheable(cacheNames = "reference", key = "'physicalAddressRoles.' + #localeId")
  public List<PhysicalAddressRole> getPhysicalAddressRoles(String localeId)
      throws ServiceUnavailableException {
    try {
      if (!StringUtils.hasText(localeId)) {
        return physicalAddressRoleRepository.findAll(
            Sort.by(Direction.ASC, "localeId", "sortIndex"));
      } else {
        return physicalAddressRoleRepository.findByLocaleIdIgnoreCase(
            localeId, Sort.by(Direction.ASC, "localeId", "sortIndex"));
      }
    } catch (Throwable e) {
      throw new ServiceUnavailableException("Failed to retrieve the physical address roles", e);
    }
  }

  /**
   * Retrieve the physical address types.
   *
   * @param localeId the Unicode locale identifier for the locale to retrieve the physical address
   *     types for or <b>null</b> to retrieve the physical address types for all locales
   * @return the physical address types
   */
  @Override
  @Cacheable(cacheNames = "reference", key = "'physicalAddressTypes.' + #localeId")
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
   * Retrieve the preference type categories.
   *
   * @param localeId the Unicode locale identifier for the locale to retrieve the preference type
   *     categories for or <b>null</b> to retrieve the preference type categories for all locales
   * @return the preference type categories
   */
  @Override
  @Cacheable(cacheNames = "reference", key = "'preferenceTypeCategories.' + #localeId")
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
   * Retrieve the preference types.
   *
   * @param localeId the Unicode locale identifier for the locale to retrieve the preference types
   *     for or <b>null</b> to retrieve the preference types for all locales
   * @return the preference types
   */
  @Override
  @Cacheable(cacheNames = "reference", key = "'preferenceTypes.' + #localeId")
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
   * Retrieve the qualification types.
   *
   * @param localeId the Unicode locale identifier for the locale to retrieve the qualification
   *     types for or <b>null</b> to retrieve the qualification types for all locales
   * @return the qualification types
   */
  @Override
  @Cacheable(cacheNames = "reference", key = "'qualificationTypes.' + #localeId")
  public List<QualificationType> getQualificationTypes(String localeId)
      throws ServiceUnavailableException {
    try {
      if (!StringUtils.hasText(localeId)) {
        return qualificationTypeRepository.findAll(Sort.by(Direction.ASC, "localeId", "sortIndex"));
      } else {
        return qualificationTypeRepository.findByLocaleIdIgnoreCase(
            localeId, Sort.by(Direction.ASC, "localeId", "sortIndex"));
      }
    } catch (Throwable e) {
      throw new ServiceUnavailableException("Failed to retrieve the qualification types", e);
    }
  }

  /**
   * Retrieve the races.
   *
   * @param localeId the Unicode locale identifier for the locale to retrieve the races for or
   *     <b>null</b> to retrieve the races for all locales
   * @return the races
   */
  @Override
  @Cacheable(cacheNames = "reference", key = "'races.' + #localeId")
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
   * Retrieve the relationship types.
   *
   * @param localeId the Unicode locale identifier for the locale to retrieve the relationship types
   *     for or <b>null</b> to retrieve the relationship types for all locales
   * @return the relationship types
   */
  @Override
  @Cacheable(cacheNames = "reference", key = "'relationshipTypes.' + #localeId")
  public List<RelationshipType> getRelationshipTypes(String localeId)
      throws ServiceUnavailableException {
    try {
      if (!StringUtils.hasText(localeId)) {
        return relationshipTypeRepository.findAll(Sort.by(Direction.ASC, "localeId", "sortIndex"));
      } else {
        return relationshipTypeRepository.findByLocaleIdIgnoreCase(
            localeId, Sort.by(Direction.ASC, "localeId", "sortIndex"));
      }
    } catch (Throwable e) {
      throw new ServiceUnavailableException("Failed to retrieve the relationship types", e);
    }
  }

  /**
   * Retrieve the residence permit types.
   *
   * @param localeId the Unicode locale identifier for the locale to retrieve the residence permit
   *     types for or <b>null</b> to retrieve the residence permit types for all locales
   * @return the residence permit types
   */
  @Override
  @Cacheable(cacheNames = "reference", key = "'residencePermitTypes.' + #localeId")
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
   * Retrieve the residency statuses.
   *
   * @param localeId the Unicode locale identifier for the locale to retrieve the residency statuses
   *     for or <b>null</b> to retrieve the residency statuses for all locales
   * @return the residency statuses
   */
  @Override
  @Cacheable(cacheNames = "reference", key = "'residencyStatuses.' + #localeId")
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
   * Retrieve the residential types.
   *
   * @param localeId the Unicode locale identifier for the locale to retrieve the residential types
   *     for or <b>null</b> to retrieve the residential types for all locales
   * @return the residential types
   */
  @Override
  @Cacheable(cacheNames = "reference", key = "'residentialTypes.' + #localeId")
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
   * Retrieve the role purposes.
   *
   * @param localeId the Unicode locale identifier for the locale to retrieve the role purposes for
   *     or <b>null</b> to retrieve the role purposes for all locales
   * @return the role purposes
   */
  @Override
  @Cacheable(cacheNames = "reference", key = "'rolePurposes.' + #localeId")
  public List<RolePurpose> getRolePurposes(String localeId) throws ServiceUnavailableException {
    try {
      if (!StringUtils.hasText(localeId)) {
        return rolePurposeRepository.findAll(Sort.by(Direction.ASC, "localeId", "sortIndex"));
      } else {
        return rolePurposeRepository.findByLocaleIdIgnoreCase(
            localeId, Sort.by(Direction.ASC, "localeId", "sortIndex"));
      }
    } catch (Throwable e) {
      throw new ServiceUnavailableException("Failed to retrieve the role purposes", e);
    }
  }

  /**
   * Retrieve the role type attribute type constraints.
   *
   * @return the role type attribute type constraints
   */
  @Override
  @Cacheable(cacheNames = "reference", key = "'roleTypeAttributeTypeConstraints.ALL'")
  public List<RoleTypeAttributeTypeConstraint> getRoleTypeAttributeTypeConstraints()
      throws ServiceUnavailableException {
    return getRoleTypeAttributeTypeConstraints(null);
  }

  /**
   * Retrieve the role type attribute type constraints.
   *
   * @param roleType the code for the role type to retrieve the attribute constraints for
   * @return the role type attribute type constraints
   */
  @Override
  @Cacheable(cacheNames = "reference", key = "'roleTypeAttributeTypeConstraints.' + #roleType")
  public List<RoleTypeAttributeTypeConstraint> getRoleTypeAttributeTypeConstraints(String roleType)
      throws ServiceUnavailableException {
    try {
      if (!StringUtils.hasText(roleType)) {
        return roleTypeAttributeTypeConstraintRepository.findAll(
            Sort.by(Direction.ASC, "roleType", "attributeType"));
      } else {
        return roleTypeAttributeTypeConstraintRepository.findByRoleTypeIgnoreCase(
            roleType, Sort.by(Direction.ASC, "roleType", "attributeType"));
      }
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to retrieve the role type attribute type constraints", e);
    }
  }

  /**
   * Retrieve the role type preference type constraints.
   *
   * @return the role type preference type constraints
   */
  @Override
  @Cacheable(cacheNames = "reference", key = "'roleTypePreferenceTypeConstraints.ALL'")
  public List<RoleTypePreferenceTypeConstraint> getRoleTypePreferenceTypeConstraints()
      throws ServiceUnavailableException {
    return getRoleTypePreferenceTypeConstraints(null);
  }

  /**
   * Retrieve the role type preference type constraints.
   *
   * @param roleType the code for the role type to retrieve the preference constraints for
   * @return the role type preference type constraints
   */
  @Override
  @Cacheable(cacheNames = "reference", key = "'roleTypePreferenceTypeConstraints.' + #roleType")
  public List<RoleTypePreferenceTypeConstraint> getRoleTypePreferenceTypeConstraints(
      String roleType) throws ServiceUnavailableException {
    try {
      if (!StringUtils.hasText(roleType)) {
        return roleTypePreferenceTypeConstraintRepository.findAll(
            Sort.by(Direction.ASC, "roleType", "preferenceType"));
      } else {
        return roleTypePreferenceTypeConstraintRepository.findByRoleTypeIgnoreCase(
            roleType, Sort.by(Direction.ASC, "roleType", "preferenceType"));
      }
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to retrieve the role type preference type constraints", e);
    }
  }

  /**
   * Retrieve the role types.
   *
   * @param localeId the Unicode locale identifier for the locale to retrieve the role types for or
   *     <b>null</b> to retrieve the role types for all locales
   * @return the role types
   */
  @Override
  @Cacheable(cacheNames = "reference", key = "'roleTypes.' + #localeId")
  public List<RoleType> getRoleTypes(String localeId) throws ServiceUnavailableException {
    try {
      if (!StringUtils.hasText(localeId)) {
        return roleTypeRepository.findAll(Sort.by(Direction.ASC, "localeId", "sortIndex"));
      } else {
        return roleTypeRepository.findByLocaleIdIgnoreCase(
            localeId, Sort.by(Direction.ASC, "localeId", "sortIndex"));
      }
    } catch (Throwable e) {
      throw new ServiceUnavailableException("Failed to retrieve the role types", e);
    }
  }

  /**
   * Retrieve the segments.
   *
   * @param localeId the Unicode locale identifier for the locale to retrieve the segments for or
   *     <b>null</b> to retrieve the segments for all locales
   * @return the segments
   */
  @Override
  @Cacheable(cacheNames = "reference", key = "'segments.' + #localeId")
  public List<Segment> getSegments(String localeId) throws ServiceUnavailableException {
    try {
      if (!StringUtils.hasText(localeId)) {
        return segmentRepository.findAll(Sort.by(Direction.ASC, "localeId", "sortIndex"));
      } else {
        return segmentRepository.findByLocaleIdIgnoreCase(
            localeId, Sort.by(Direction.ASC, "localeId", "sortIndex"));
      }
    } catch (Throwable e) {
      throw new ServiceUnavailableException("Failed to retrieve the segments", e);
    }
  }

  /**
   * Retrieve the source of funds types.
   *
   * @param localeId the Unicode locale identifier for the locale to retrieve the source of funds
   *     types for or <b>null</b> to retrieve the source of funds types for all locales
   * @return the source of funds types
   */
  @Override
  @Cacheable(cacheNames = "reference", key = "'sourceOfFundsTypes.' + #localeId")
  public List<SourceOfFundsType> getSourceOfFundsTypes(String localeId)
      throws ServiceUnavailableException {
    try {
      if (!StringUtils.hasText(localeId)) {
        return sourceOfFundsTypeRepository.findAll(Sort.by(Direction.ASC, "localeId", "sortIndex"));
      } else {
        return sourceOfFundsTypeRepository.findByLocaleIdIgnoreCase(
            localeId, Sort.by(Direction.ASC, "localeId", "sortIndex"));
      }
    } catch (Throwable e) {
      throw new ServiceUnavailableException("Failed to retrieve the source of funds types", e);
    }
  }

  /**
   * Retrieve the source of wealth types.
   *
   * @param localeId the Unicode locale identifier for the locale to retrieve the source of wealth
   *     types for or <b>null</b> to retrieve the source of wealth types for all locales
   * @return the source of wealth types
   */
  @Override
  @Cacheable(cacheNames = "reference", key = "'sourceOfWealthTypes.' + #localeId")
  public List<SourceOfWealthType> getSourceOfWealthTypes(String localeId)
      throws ServiceUnavailableException {
    try {
      if (!StringUtils.hasText(localeId)) {
        return sourceOfWealthTypeRepository.findAll(
            Sort.by(Direction.ASC, "localeId", "sortIndex"));
      } else {
        return sourceOfWealthTypeRepository.findByLocaleIdIgnoreCase(
            localeId, Sort.by(Direction.ASC, "localeId", "sortIndex"));
      }
    } catch (Throwable e) {
      throw new ServiceUnavailableException("Failed to retrieve the source of wealth types", e);
    }
  }

  /**
   * Retrieve the status type categories.
   *
   * @param localeId the Unicode locale identifier for the locale to retrieve the status type
   *     categories for or <b>null</b> to retrieve the status type categories for all locales
   * @return the status type categories
   */
  @Override
  @Cacheable(cacheNames = "reference", key = "'statusTypeCategories.' + #localeId")
  public List<StatusTypeCategory> getStatusTypeCategories(String localeId)
      throws ServiceUnavailableException {
    try {
      if (!StringUtils.hasText(localeId)) {
        return statusTypeCategoryRepository.findAll(
            Sort.by(Direction.ASC, "localeId", "sortIndex"));
      } else {
        return statusTypeCategoryRepository.findByLocaleIdIgnoreCase(
            localeId, Sort.by(Direction.ASC, "localeId", "sortIndex"));
      }
    } catch (Throwable e) {
      throw new ServiceUnavailableException("Failed to retrieve the status type categories", e);
    }
  }

  /**
   * Retrieve the status types.
   *
   * @param localeId the Unicode locale identifier for the locale to retrieve the status types for
   *     or <b>null</b> to retrieve the status types for all locales
   * @return the status types
   */
  @Override
  @Cacheable(cacheNames = "reference", key = "'statusTypes.' + #localeId")
  public List<StatusType> getStatusTypes(String localeId) throws ServiceUnavailableException {
    try {
      if (!StringUtils.hasText(localeId)) {
        return statusTypeRepository.findAll(Sort.by(Direction.ASC, "localeId", "sortIndex"));
      } else {
        return statusTypeRepository.findByLocaleIdIgnoreCase(
            localeId, Sort.by(Direction.ASC, "localeId", "sortIndex"));
      }
    } catch (Throwable e) {
      throw new ServiceUnavailableException("Failed to retrieve the status types", e);
    }
  }

  /**
   * Retrieve the tax number types.
   *
   * @param localeId the Unicode locale identifier for the locale to retrieve the tax number types
   *     for or <b>null</b> to retrieve the tax number types for all locales
   * @return the tax number types
   */
  @Override
  @Cacheable(cacheNames = "reference", key = "'taxNumberTypes.' + #localeId")
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
   * Retrieve the times to contact.
   *
   * @param localeId the Unicode locale identifier for the locale to retrieve the times to contact
   *     for or <b>null</b> to retrieve the times to contact for all locales
   * @return the times to contact
   */
  @Override
  @Cacheable(cacheNames = "reference", key = "'timesToContact.' + #localeId")
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
   * Retrieve the titles.
   *
   * @param localeId the Unicode locale identifier for the locale to retrieve the titles for or
   *     <b>null</b> to retrieve the titles for all locales
   * @return the titles
   */
  @Override
  @Cacheable(cacheNames = "reference", key = "'titles.' + #localeId")
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
   * Check whether the code is a valid code for a attribute type for the party type.
   *
   * @param partyTypeCode the party type code
   * @param attributeTypeCode the code for the attribute type
   * @return <b>true</b> if the code is a valid code for a attribute type or <b>false</b> otherwise
   */
  @Override
  public boolean isValidAttributeType(String partyTypeCode, String attributeTypeCode)
      throws ServiceUnavailableException {
    if (!StringUtils.hasText(attributeTypeCode)) {
      return false;
    }

    return self.getAttributeTypes(DEFAULT_LOCALE_ID).stream()
        .anyMatch(
            attributeType ->
                (Objects.equals(attributeType.getCode(), attributeTypeCode)
                    && attributeType.isValidForPartyType(partyTypeCode)));
  }

  /**
   * Check whether the code is a valid code for a attribute type category.
   *
   * @param attributeTypeCategoryCode the code for the attribute type category
   * @return <b>true</b> if the code is a valid code for a attribute type category or <b>false</b>
   *     otherwise
   */
  @Override
  public boolean isValidAttributeTypeCategory(String attributeTypeCategoryCode)
      throws ServiceUnavailableException {
    if (!StringUtils.hasText(attributeTypeCategoryCode)) {
      return false;
    }

    return self.getAttributeTypeCategories(DEFAULT_LOCALE_ID).stream()
        .anyMatch(
            attributeTypeCategory ->
                Objects.equals(attributeTypeCategory.getCode(), attributeTypeCategoryCode));
  }

  /**
   * Check whether the code is a valid code for a consent type.
   *
   * @param consentTypeCode the code for the consent type
   * @return <b>true</b> if the code is a valid code for a consent type or <b>false</b> otherwise
   */
  @Override
  public boolean isValidConsentType(String consentTypeCode) throws ServiceUnavailableException {
    if (!StringUtils.hasText(consentTypeCode)) {
      return false;
    }

    return self.getConsentTypes(DEFAULT_LOCALE_ID).stream()
        .anyMatch(consentType -> Objects.equals(consentType.getCode(), consentTypeCode));
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

    return self.getContactMechanismPurposes(DEFAULT_LOCALE_ID).stream()
        .anyMatch(
            contactMechanismPurpose ->
                (contactMechanismPurpose.isValidForPartyType(partyTypeCode)
                    && contactMechanismPurpose.isValidForContactMechanismType(
                        contactMechanismTypeCode)
                    && Objects.equals(
                        contactMechanismPurpose.getCode(), contactMechanismPurposeCode)));
  }

  /**
   * Check whether the code is a valid code for a contact mechanism role for the party type.
   *
   * @param partyTypeCode the party type code
   * @param contactMechanismTypeCode the code for the contact mechanism type
   * @param contactMechanismRoleCode the code for the contact mechanism role
   * @return <b>true</b> if the code is a valid code for a contact mechanism role or <b>false</b>
   *     otherwise
   */
  @Override
  public boolean isValidContactMechanismRole(
      String partyTypeCode, String contactMechanismTypeCode, String contactMechanismRoleCode)
      throws ServiceUnavailableException {
    if (!StringUtils.hasText(contactMechanismRoleCode)) {
      return false;
    }

    return self.getContactMechanismRoles(DEFAULT_LOCALE_ID).stream()
        .anyMatch(
            contactMechanismRole ->
                (contactMechanismRole.isValidForPartyType(partyTypeCode)
                    && contactMechanismRole
                        .getContactMechanismType()
                        .equals(contactMechanismTypeCode)
                    && Objects.equals(contactMechanismRole.getCode(), contactMechanismRoleCode)));
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

    return self.getContactMechanismTypes(DEFAULT_LOCALE_ID).stream()
        .anyMatch(
            contactMechanismType ->
                Objects.equals(contactMechanismType.getCode(), contactMechanismTypeCode));
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

    return self.getEmploymentStatuses(DEFAULT_LOCALE_ID).stream()
        .anyMatch(
            employmentStatus -> Objects.equals(employmentStatus.getCode(), employmentStatusCode));
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

    return self.getEmploymentTypes(DEFAULT_LOCALE_ID).stream()
        .anyMatch(
            employmentType ->
                (Objects.equals(employmentType.getCode(), employmentTypeCode)
                    && Objects.equals(employmentType.getEmploymentStatus(), employmentStatusCode)));
  }

  /**
   * Check whether the code is a valid code for an employment type.
   *
   * @param employmentTypeCode the code for the employment type
   * @return <b>true</b> if the code is a valid code for an employment type or <b>false</b>
   *     otherwise
   */
  @Override
  public boolean isValidEmploymentType(String employmentTypeCode)
      throws ServiceUnavailableException {
    if (!StringUtils.hasText(employmentTypeCode)) {
      return false;
    }

    return self.getEmploymentTypes(DEFAULT_LOCALE_ID).stream()
        .anyMatch(employmentType -> Objects.equals(employmentType.getCode(), employmentTypeCode));
  }

  /**
   * Check whether the code is a valid code for a field of study.
   *
   * @param fieldOfStudyCode the code for the field of study
   * @return <b>true</b> if the code is a valid code for a field of study or <b>false</b> otherwise
   */
  @Override
  public boolean isValidFieldOfStudy(String fieldOfStudyCode) throws ServiceUnavailableException {
    if (!StringUtils.hasText(fieldOfStudyCode)) {
      return false;
    }

    return self.getFieldsOfStudy(DEFAULT_LOCALE_ID).stream()
        .anyMatch(fieldOfStudy -> Objects.equals(fieldOfStudy.getCode(), fieldOfStudyCode));
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

    return self.getGenders(DEFAULT_LOCALE_ID).stream()
        .anyMatch(gender -> Objects.equals(gender.getCode(), genderCode));
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

    return self.getIdentityDocumentTypes(DEFAULT_LOCALE_ID).stream()
        .anyMatch(
            identityDocumentType ->
                (Objects.equals(identityDocumentType.getCode(), identityDocumentTypeCode)
                    && identityDocumentType.isValidForPartyType(partyTypeCode)));
  }

  /**
   * Check whether the code is a valid code for a lock type for the party type.
   *
   * @param partyTypeCode the party type code
   * @param lockTypeCode the code for the lock type
   * @return <b>true</b> if the code is a valid code for a lock type or <b>false</b> otherwise
   */
  @Override
  public boolean isValidLockType(String partyTypeCode, String lockTypeCode)
      throws ServiceUnavailableException {
    if (!StringUtils.hasText(lockTypeCode)) {
      return false;
    }

    return self.getLockTypes(DEFAULT_LOCALE_ID).stream()
        .anyMatch(
            lockType ->
                (Objects.equals(lockType.getCode(), lockTypeCode)
                    && lockType.isValidForPartyType(partyTypeCode)));
  }

  /**
   * Check whether the code is a valid code for a lock type category.
   *
   * @param lockTypeCategoryCode the code for the lock type category
   * @return <b>true</b> if the code is a valid code for a lock type category or <b>false</b>
   *     otherwise
   */
  @Override
  public boolean isValidLockTypeCategory(String lockTypeCategoryCode)
      throws ServiceUnavailableException {
    if (!StringUtils.hasText(lockTypeCategoryCode)) {
      return false;
    }

    return self.getLockTypeCategories(DEFAULT_LOCALE_ID).stream()
        .anyMatch(
            lockTypeCategory -> Objects.equals(lockTypeCategory.getCode(), lockTypeCategoryCode));
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

    return self.getMaritalStatuses(DEFAULT_LOCALE_ID).stream()
        .anyMatch(maritalStatus -> Objects.equals(maritalStatus.getCode(), maritalStatusCode));
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
        self.getMarriageTypes(DEFAULT_LOCALE_ID).stream()
            .filter(
                marriageType -> Objects.equals(marriageType.getMaritalStatus(), maritalStatusCode))
            .collect(Collectors.toList());

    // If we have marriage types for the specified marital status then check if one matches
    if (marriageTypes.size() > 0) {
      return marriageTypes.stream()
          .anyMatch(
              marriageType ->
                  (Objects.equals(marriageType.getCode(), marriageTypeCode)
                      && Objects.equals(marriageType.getMaritalStatus(), maritalStatusCode)));
    } else {
      return true;
    }
  }

  /**
   * Check whether the measurement unit is valid for the attribute type with the specified code.
   *
   * @param attributeTypeCode the code for the attribute type
   * @param measurementUnit the measurement unit
   * @return <b>true</b> if the measurement unit is valid for the attribute type with the specified
   *     code or <b>false</b> otherwise
   */
  public boolean isValidMeasurementUnitForAttributeType(
      String attributeTypeCode, MeasurementUnit measurementUnit)
      throws ServiceUnavailableException {
    if (!StringUtils.hasText(attributeTypeCode)) {
      return false;
    }

    return self.getAttributeTypes(DEFAULT_LOCALE_ID).stream()
        .anyMatch(
            attributeType ->
                (Objects.equals(attributeType.getCode(), attributeTypeCode)
                    && Objects.equals(
                        attributeType.getUnitType(),
                        (measurementUnit == null) ? null : measurementUnit.getType())));
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

    return self.getNextOfKinTypes(DEFAULT_LOCALE_ID).stream()
        .anyMatch(nextOfKinType -> Objects.equals(nextOfKinType.getCode(), nextOfKinTypeCode));
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

    return self.getOccupations(DEFAULT_LOCALE_ID).stream()
        .anyMatch(occupation -> Objects.equals(occupation.getCode(), occupationCode));
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

    return self.getPhysicalAddressPurposes(DEFAULT_LOCALE_ID).stream()
        .anyMatch(
            physicalAddressPurpose ->
                (Objects.equals(physicalAddressPurpose.getCode(), physicalAddressPurposeCode)
                    && physicalAddressPurpose.isValidForPartyType(partyTypeCode)));
  }

  /**
   * Check whether the code is a valid code for a physical address purpose.
   *
   * @param physicalAddressPurposeCode the code for the physical address purpose
   * @return <b>true</b> if the code is a valid code for a physical address purpose or <b>false</b>
   *     otherwise
   */
  @Override
  public boolean isValidPhysicalAddressPurpose(String physicalAddressPurposeCode)
      throws ServiceUnavailableException {
    if (!StringUtils.hasText(physicalAddressPurposeCode)) {
      return false;
    }

    return self.getPhysicalAddressPurposes(DEFAULT_LOCALE_ID).stream()
        .anyMatch(
            physicalAddressPurpose ->
                Objects.equals(physicalAddressPurpose.getCode(), physicalAddressPurposeCode));
  }

  /**
   * Check whether the code is a valid code for a physical address role for the party type.
   *
   * @param partyTypeCode the party type code
   * @param physicalAddressRoleCode the code for the physical address role
   * @return <b>true</b> if the code is a valid code for a physical address role or <b>false</b>
   *     otherwise
   */
  @Override
  public boolean isValidPhysicalAddressRole(String partyTypeCode, String physicalAddressRoleCode)
      throws ServiceUnavailableException {
    if (!StringUtils.hasText(physicalAddressRoleCode)) {
      return false;
    }

    return self.getPhysicalAddressRoles(DEFAULT_LOCALE_ID).stream()
        .anyMatch(
            physicalAddressRole ->
                (Objects.equals(physicalAddressRole.getCode(), physicalAddressRoleCode)
                    && physicalAddressRole.isValidForPartyType(partyTypeCode)));
  }

  /**
   * Check whether the code is a valid code for a physical address role.
   *
   * @param physicalAddressRoleCode the code for the physical address role
   * @return <b>true</b> if the code is a valid code for a physical address role or <b>false</b>
   *     otherwise
   */
  @Override
  public boolean isValidPhysicalAddressRole(String physicalAddressRoleCode)
      throws ServiceUnavailableException {
    if (!StringUtils.hasText(physicalAddressRoleCode)) {
      return false;
    }

    return self.getPhysicalAddressRoles(DEFAULT_LOCALE_ID).stream()
        .anyMatch(
            physicalAddressRole ->
                Objects.equals(physicalAddressRole.getCode(), physicalAddressRoleCode));
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

    return self.getPhysicalAddressTypes(DEFAULT_LOCALE_ID).stream()
        .anyMatch(
            physicalAddressType ->
                Objects.equals(physicalAddressType.getCode(), physicalAddressTypeCode));
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

    return self.getPreferenceTypes(DEFAULT_LOCALE_ID).stream()
        .anyMatch(
            preferenceType ->
                (Objects.equals(preferenceType.getCode(), preferenceTypeCode)
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

    return self.getPreferenceTypeCategories(DEFAULT_LOCALE_ID).stream()
        .anyMatch(
            preferenceTypeCategory ->
                Objects.equals(preferenceTypeCategory.getCode(), preferenceTypeCategoryCode));
  }

  /**
   * Check whether the code is a valid code for a qualification type.
   *
   * @param qualificationTypeCode the code for the qualification type
   * @return <b>true</b> if the code is a valid code for a qualification type or <b>false</b>
   *     otherwise
   */
  @Override
  public boolean isValidQualificationType(String qualificationTypeCode)
      throws ServiceUnavailableException {
    if (!StringUtils.hasText(qualificationTypeCode)) {
      return false;
    }

    return self.getQualificationTypes(DEFAULT_LOCALE_ID).stream()
        .anyMatch(
            qualificationType ->
                Objects.equals(qualificationType.getCode(), qualificationTypeCode));
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

    return self.getRaces(DEFAULT_LOCALE_ID).stream()
        .anyMatch(race -> Objects.equals(race.getCode(), raceCode));
  }

  /**
   * Check whether the code is a valid code for a relationship type for the party type.
   *
   * @param partyTypeCode the party type code
   * @param relationshipTypeCode the code for the relationship type
   * @return <b>true</b> if the code is a valid code for a relationship type or <b>false</b>
   *     otherwise
   */
  @Override
  public boolean isValidRelationshipType(String partyTypeCode, String relationshipTypeCode)
      throws ServiceUnavailableException {
    if (!StringUtils.hasText(relationshipTypeCode)) {
      return false;
    }

    return self.getRelationshipTypes(DEFAULT_LOCALE_ID).stream()
        .anyMatch(
            relationshipType -> Objects.equals(relationshipType.getCode(), relationshipTypeCode));
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

    return self.getResidencePermitTypes(DEFAULT_LOCALE_ID).stream()
        .anyMatch(
            residencePermitType ->
                Objects.equals(residencePermitType.getCode(), residencePermitTypeCode));
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

    return self.getResidencyStatuses(DEFAULT_LOCALE_ID).stream()
        .anyMatch(
            residencyStatus -> Objects.equals(residencyStatus.getCode(), residencyStatusCode));
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

    return self.getResidentialTypes(DEFAULT_LOCALE_ID).stream()
        .anyMatch(
            residentialType -> Objects.equals(residentialType.getCode(), residentialTypeCode));
  }

  /**
   * Check whether the code is a valid code for a role purpose.
   *
   * @param rolePurposeCode the code for the role purpose
   * @return <b>true</b> if the code is a valid code for a role purpose or <b>false</b> otherwise
   */
  @Override
  public boolean isValidRolePurpose(String rolePurposeCode) throws ServiceUnavailableException {
    if (!StringUtils.hasText(rolePurposeCode)) {
      return false;
    }

    return self.getRolePurposes(DEFAULT_LOCALE_ID).stream()
        .anyMatch(rolePurpose -> Objects.equals(rolePurpose.getCode(), rolePurposeCode));
  }

  /**
   * Check whether the code is a valid code for a role type for the party type.
   *
   * @param partyTypeCode the party type code
   * @param roleTypeCode the code for the role type
   * @return <b>true</b> if the code is a valid code for a role type or <b>false</b> otherwise
   */
  @Override
  public boolean isValidRoleType(String partyTypeCode, String roleTypeCode)
      throws ServiceUnavailableException {
    if (!StringUtils.hasText(roleTypeCode)) {
      return false;
    }

    return self.getRoleTypes(DEFAULT_LOCALE_ID).stream()
        .anyMatch(
            roleType ->
                (Objects.equals(roleType.getCode(), roleTypeCode)
                    && roleType.isValidForPartyType(partyTypeCode)));
  }

  /**
   * Check whether the code is a valid code for a segment.
   *
   * @param segmentCode the code for the segment
   * @return <b>true</b> if the code is a valid code for a segment or <b>false</b> otherwise
   */
  @Override
  public boolean isValidSegment(String segmentCode) throws ServiceUnavailableException {
    if (!StringUtils.hasText(segmentCode)) {
      return false;
    }

    return self.getSegments(DEFAULT_LOCALE_ID).stream()
        .anyMatch(segment -> Objects.equals(segment.getCode(), segmentCode));
  }

  /**
   * Check whether the code is a valid code for a source of funds type.
   *
   * @param sourceOfFundsTypeCode the code for the source of funds type
   * @return <b>true</b> if the code is a valid code for a source of funds type or <b>false</b>
   *     otherwise
   */
  @Override
  public boolean isValidSourceOfFundsType(String sourceOfFundsTypeCode)
      throws ServiceUnavailableException {
    if (!StringUtils.hasText(sourceOfFundsTypeCode)) {
      return false;
    }

    return self.getSourceOfFundsTypes(DEFAULT_LOCALE_ID).stream()
        .anyMatch(sourceOfFunds -> Objects.equals(sourceOfFunds.getCode(), sourceOfFundsTypeCode));
  }

  /**
   * Check whether the code is a valid code for a source of wealth type.
   *
   * @param sourceOfWealthTypeCode the code for the source of wealth type
   * @return <b>true</b> if the code is a valid code for a source of wealth type or <b>false</b>
   *     otherwise
   */
  @Override
  public boolean isValidSourceOfWealthType(String sourceOfWealthTypeCode)
      throws ServiceUnavailableException {
    if (!StringUtils.hasText(sourceOfWealthTypeCode)) {
      return false;
    }

    return self.getSourceOfWealthTypes(DEFAULT_LOCALE_ID).stream()
        .anyMatch(
            sourceOfWealth -> Objects.equals(sourceOfWealth.getCode(), sourceOfWealthTypeCode));
  }

  /**
   * Check whether the code is a valid code for a status type for the party type.
   *
   * @param partyTypeCode the party type code
   * @param statusTypeCode the code for the status type
   * @return <b>true</b> if the code is a valid code for a status type or <b>false</b> otherwise
   */
  @Override
  public boolean isValidStatusType(String partyTypeCode, String statusTypeCode)
      throws ServiceUnavailableException {
    if (!StringUtils.hasText(statusTypeCode)) {
      return false;
    }

    return self.getStatusTypes(DEFAULT_LOCALE_ID).stream()
        .anyMatch(
            statusType ->
                (Objects.equals(statusType.getCode(), statusTypeCode)
                    && statusType.isValidForPartyType(partyTypeCode)));
  }

  /**
   * Check whether the code is a valid code for a status type category.
   *
   * @param statusTypeCategoryCode the code for the status type category
   * @return <b>true</b> if the code is a valid code for a status type category or <b>false</b>
   *     otherwise
   */
  @Override
  public boolean isValidStatusTypeCategory(String statusTypeCategoryCode)
      throws ServiceUnavailableException {
    if (!StringUtils.hasText(statusTypeCategoryCode)) {
      return false;
    }

    return self.getStatusTypeCategories(DEFAULT_LOCALE_ID).stream()
        .anyMatch(
            statusTypeCategory ->
                Objects.equals(statusTypeCategory.getCode(), statusTypeCategoryCode));
  }

  /**
   * Check whether the code is a valid code for a tax number type for the party type.
   *
   * @param partyTypeCode the party type code
   * @param taxNumberTypeCode the code for the tax number type
   * @return <b>true</b> if the code is a valid code for a tax number type or <b>false</b> otherwise
   */
  @Override
  public boolean isValidTaxNumberType(String partyTypeCode, String taxNumberTypeCode)
      throws ServiceUnavailableException {
    if (!StringUtils.hasText(taxNumberTypeCode)) {
      return false;
    }

    return self.getTaxNumberTypes(DEFAULT_LOCALE_ID).stream()
        .anyMatch(
            taxNumberType ->
                Objects.equals(taxNumberType.getCode(), taxNumberTypeCode)
                    && taxNumberType.isValidForPartyType(partyTypeCode));
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

    return self.getTimesToContact(DEFAULT_LOCALE_ID).stream()
        .anyMatch(timeToContact -> Objects.equals(timeToContact.getCode(), timeToContactCode));
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

    return self.getTitles(DEFAULT_LOCALE_ID).stream()
        .anyMatch(title -> Objects.equals(title.getCode(), titleCode));
  }
}
