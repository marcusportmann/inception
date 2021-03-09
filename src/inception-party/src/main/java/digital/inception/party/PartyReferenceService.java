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
import java.util.stream.Collectors;
import javax.annotation.Resource;
import javax.validation.Validator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
@SuppressWarnings("unused")
public class PartyReferenceService implements IPartyReferenceService {

  /* Logger */
  private static final Logger logger = LoggerFactory.getLogger(PartyReferenceService.class);

  /** The Party Attribute Type Category Repository. */
  private final AttributeTypeCategoryRepository attributeTypeCategoryRepository;

  /** The Party Attribute Type Repository. */
  private final AttributeTypeRepository attributeTypeRepository;

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

  /** The Race Repository. */
  private final RaceRepository raceRepository;

  /** The Residence Permit Type Repository. */
  private final ResidencePermitTypeRepository residencePermitTypeRepository;

  /** The Residency Status Repository. */
  private final ResidencyStatusRepository residencyStatusRepository;

  /** The Residential Type Repository. */
  private final ResidentialTypeRepository residentialTypeRepository;

  /** The Party Role Purpose Repository. */
  private final RolePurposeRepository rolePurposeRepository;

  /** The Role Type Attribute Constraint Repository. */
  private final RoleTypeAttributeConstraintRepository roleTypeAttributeConstraintRepository;

  /** The Party Role Type Repository. */
  private final RoleTypeRepository roleTypeRepository;

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

  /** The internal reference to the Party Reference Service to enable caching. */
  @Resource private IPartyReferenceService self;

  /**
   * Constructs a new <b>PartyReferenceService</b>.
   *
   * @param validator the JSR-303 validator
   * @param contactMechanismPurposeRepository the Contact Mechanism Purpose Repository
   * @param contactMechanismRoleRepository the Contact Mechanism Role Repository
   * @param contactMechanismTypeRepository the Contact Mechanism Type Repository
   * @param employmentStatusRepository the Employment Status Repository
   * @param employmentTypeRepository the Employment Type Repository
   * @param genderRepository the Gender Repository
   * @param identityDocumentTypeRepository the Identity Document Type Repository
   * @param maritalStatusRepository the Marital Status Repository
   * @param marriageTypeRepository the Marriage Type Repository
   * @param nextOfKinTypeRepository the Next Of Kin Repository
   * @param occupationRepository the Occupation Repository
   * @param attributeTypeCategoryRepository the Party Attribute Type Category Repository
   * @param attributeTypeRepository the Party Attribute Type Repository
   * @param rolePurposeRepository the Party Role Purpose Repository
   * @param roleTypeRepository the Party Role Type Repository
   * @param physicalAddressPurposeRepository the Physical Address Purpose Repository
   * @param physicalAddressRoleRepository the Physical Address Role Repository
   * @param physicalAddressTypeRepository the Physical Address Type Repository
   * @param preferenceTypeCategoryRepository the Preference Type Category Repository
   * @param preferenceTypeRepository the Preference Type Repository
   * @param raceRepository the Race Repository
   * @param residencePermitTypeRepository the Residence Permit Type Repository
   * @param residencyStatusRepository the Residency Status Repository
   * @param residentialTypeRepository the Residential Type Repository
   * @param roleTypeAttributeConstraintRepository the Role Type Attribute Constraint Repository
   * @param sourceOfFundsRepository the Source Of Funds Repository
   * @param taxNumberTypeRepository the Tax Number Type Repository
   * @param timeToContactRepository the Time To Contact Repository
   * @param titleRepository the Title Repository
   */
  public PartyReferenceService(
      Validator validator,
      ContactMechanismPurposeRepository contactMechanismPurposeRepository,
      ContactMechanismRoleRepository contactMechanismRoleRepository,
      ContactMechanismTypeRepository contactMechanismTypeRepository,
      EmploymentStatusRepository employmentStatusRepository,
      EmploymentTypeRepository employmentTypeRepository,
      GenderRepository genderRepository,
      IdentityDocumentTypeRepository identityDocumentTypeRepository,
      MaritalStatusRepository maritalStatusRepository,
      MarriageTypeRepository marriageTypeRepository,
      NextOfKinTypeRepository nextOfKinTypeRepository,
      OccupationRepository occupationRepository,
      AttributeTypeCategoryRepository attributeTypeCategoryRepository,
      AttributeTypeRepository attributeTypeRepository,
      RolePurposeRepository rolePurposeRepository,
      RoleTypeRepository roleTypeRepository,
      PhysicalAddressPurposeRepository physicalAddressPurposeRepository,
      PhysicalAddressRoleRepository physicalAddressRoleRepository,
      PhysicalAddressTypeRepository physicalAddressTypeRepository,
      PreferenceTypeCategoryRepository preferenceTypeCategoryRepository,
      PreferenceTypeRepository preferenceTypeRepository,
      RaceRepository raceRepository,
      ResidencePermitTypeRepository residencePermitTypeRepository,
      ResidencyStatusRepository residencyStatusRepository,
      ResidentialTypeRepository residentialTypeRepository,
      RoleTypeAttributeConstraintRepository roleTypeAttributeConstraintRepository,
      SourceOfFundsRepository sourceOfFundsRepository,
      TaxNumberTypeRepository taxNumberTypeRepository,
      TimeToContactRepository timeToContactRepository,
      TitleRepository titleRepository) {
    this.validator = validator;
    this.contactMechanismPurposeRepository = contactMechanismPurposeRepository;
    this.contactMechanismRoleRepository = contactMechanismRoleRepository;
    this.contactMechanismTypeRepository = contactMechanismTypeRepository;
    this.employmentStatusRepository = employmentStatusRepository;
    this.employmentTypeRepository = employmentTypeRepository;
    this.genderRepository = genderRepository;
    this.identityDocumentTypeRepository = identityDocumentTypeRepository;
    this.maritalStatusRepository = maritalStatusRepository;
    this.marriageTypeRepository = marriageTypeRepository;
    this.nextOfKinTypeRepository = nextOfKinTypeRepository;
    this.occupationRepository = occupationRepository;
    this.attributeTypeCategoryRepository = attributeTypeCategoryRepository;
    this.attributeTypeRepository = attributeTypeRepository;
    this.rolePurposeRepository = rolePurposeRepository;
    this.roleTypeRepository = roleTypeRepository;
    this.physicalAddressPurposeRepository = physicalAddressPurposeRepository;
    this.physicalAddressRoleRepository = physicalAddressRoleRepository;
    this.physicalAddressTypeRepository = physicalAddressTypeRepository;
    this.preferenceTypeCategoryRepository = preferenceTypeCategoryRepository;
    this.preferenceTypeRepository = preferenceTypeRepository;
    this.raceRepository = raceRepository;
    this.residencePermitTypeRepository = residencePermitTypeRepository;
    this.residencyStatusRepository = residencyStatusRepository;
    this.residentialTypeRepository = residentialTypeRepository;
    this.roleTypeAttributeConstraintRepository = roleTypeAttributeConstraintRepository;
    this.sourceOfFundsRepository = sourceOfFundsRepository;
    this.taxNumberTypeRepository = taxNumberTypeRepository;
    this.timeToContactRepository = timeToContactRepository;
    this.titleRepository = titleRepository;
  }

  /**
   * Retrieve all the attribute type categories.
   *
   * @return the attribute type categories
   */
  @Override
  @Cacheable(value = "reference", key = "'attributeTypeCategories.ALL'")
  public List<AttributeTypeCategory> getAttributeTypeCategories()
      throws ServiceUnavailableException {
    return getAttributeTypeCategories(null);
  }

  /**
   * Retrieve the attribute type categories.
   *
   * @param localeId the Unicode locale identifier for the locale to retrieve the attribute type
   *     categories for or <b>null</b> to retrieve the attribute type categories for all locales
   * @return the attribute type categories
   */
  @Override
  @Cacheable(value = "reference", key = "'attributeTypeCategories.' + #localeId")
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
   * Retrieve all the attribute types.
   *
   * @return the attribute types
   */
  @Override
  @Cacheable(value = "reference", key = "'attributeTypes.ALL'")
  public List<AttributeType> getAttributeTypes() throws ServiceUnavailableException {
    return getAttributeTypes(null);
  }

  /**
   * Retrieve the attribute types.
   *
   * @param localeId the Unicode locale identifier for the locale to retrieve the attribute types
   *     for or <b>null</b> to retrieve the attribute types for all locales
   * @return the attribute types
   */
  @Override
  @Cacheable(value = "reference", key = "'attributeTypes.' + #localeId")
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
   * Retrieve all the contact mechanism roles.
   *
   * @return the contact mechanism roles
   */
  @Override
  @Cacheable(value = "reference", key = "'contactMechanismRoles.ALL'")
  public List<ContactMechanismRole> getContactMechanismRoles() throws ServiceUnavailableException {
    return getContactMechanismRoles(null);
  }

  /**
   * Retrieve the contact mechanism roles.
   *
   * @param localeId the Unicode locale identifier for the locale to retrieve the contact mechanism
   *     purposes for or <b>null</b> to retrieve the contact mechanism roles for all locales
   * @return the contact mechanism roles
   */
  @Override
  @Cacheable(value = "reference", key = "'contactMechanismRoles.' + #localeId")
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
   * Retrieve all the physical address roles.
   *
   * @return the physical address roles
   */
  @Override
  @Cacheable(value = "reference", key = "'physicalAddressRoles.ALL'")
  public List<PhysicalAddressRole> getPhysicalAddressRoles() throws ServiceUnavailableException {
    return getPhysicalAddressRoles(null);
  }

  /**
   * Retrieve the physical address roles.
   *
   * @param localeId the Unicode locale identifier for the locale to retrieve the physical address
   *     roles for or <b>null</b> to retrieve the physical address roles for all locales
   * @return the physical address roles
   */
  @Override
  @Cacheable(value = "reference", key = "'physicalAddressRoles.' + #localeId")
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
   * Retrieve all the role purposes.
   *
   * @return the role purposes
   */
  @Override
  @Cacheable(value = "reference", key = "'rolePurposes.ALL'")
  public List<RolePurpose> getRolePurposes() throws ServiceUnavailableException {
    return getRolePurposes(null);
  }

  /**
   * Retrieve the role purposes.
   *
   * @param localeId the Unicode locale identifier for the locale to retrieve the role purposes for
   *     or <b>null</b> to retrieve the role purposes for all locales
   * @return the role purposes
   */
  @Override
  @Cacheable(value = "reference", key = "'rolePurposes.' + #localeId")
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
   * Retrieve the role type attribute constraints.
   *
   * @return the role type attribute constraints
   */
  @Override
  @Cacheable(value = "reference", key = "'roleTypeAttributeConstraints.ALL'")
  public List<RoleTypeAttributeConstraint> getRoleTypeAttributeConstraints()
      throws ServiceUnavailableException {
    return getRoleTypeAttributeConstraints(null);
  }

  /**
   * Retrieve the role type attribute constraints.
   *
   * @param roleType the code for the role type to retrieve the attribute constraints for
   * @return the role type attribute constraints
   */
  @Override
  @Cacheable(value = "reference", key = "'roleTypeAttributeConstraints.' + #roleType")
  public List<RoleTypeAttributeConstraint> getRoleTypeAttributeConstraints(String roleType)
      throws ServiceUnavailableException {
    try {
      if (!StringUtils.hasText(roleType)) {
        return roleTypeAttributeConstraintRepository.findAll(
            Sort.by(Direction.ASC, "roleType", "attributeType"));
      } else {
        return roleTypeAttributeConstraintRepository.findByRoleTypeIgnoreCase(
            roleType, Sort.by(Direction.ASC, "roleType", "attributeType"));
      }
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to retrieve the role type attribute constraints", e);
    }
  }

  /**
   * Retrieve all the role types.
   *
   * @return the role types
   */
  @Override
  @Cacheable(value = "reference", key = "'roleTypes.ALL'")
  public List<RoleType> getRoleTypes() throws ServiceUnavailableException {
    return getRoleTypes(null);
  }

  /**
   * Retrieve the role types.
   *
   * @param localeId the Unicode locale identifier for the locale to retrieve the role types for or
   *     <b>null</b> to retrieve the role types for all locales
   * @return the role types
   */
  @Override
  @Cacheable(value = "reference", key = "'roleTypes.' + #localeId")
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

    return self.getAttributeTypes().stream()
        .anyMatch(
            attributeType ->
                (attributeType.getCode().equals(attributeTypeCode)
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

    return self.getAttributeTypeCategories().stream()
        .anyMatch(
            attributeTypeCategory ->
                attributeTypeCategory.getCode().equals(attributeTypeCategoryCode));
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
                (contactMechanismPurpose.isValidForPartyType(partyTypeCode)
                    && contactMechanismPurpose.isValidForContactMechanismType(
                        contactMechanismTypeCode)
                    && contactMechanismPurpose.getCode().equals(contactMechanismPurposeCode)));
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

    return self.getContactMechanismRoles().stream()
        .anyMatch(
            contactMechanismRole ->
                (contactMechanismRole.isValidForPartyType(partyTypeCode)
                    && contactMechanismRole
                        .getContactMechanismType()
                        .equals(contactMechanismTypeCode)
                    && contactMechanismRole.getCode().equals(contactMechanismRoleCode)));
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

    return self.getPhysicalAddressPurposes().stream()
        .anyMatch(
            physicalAddressPurpose ->
                physicalAddressPurpose.getCode().equals(physicalAddressPurposeCode));
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

    return self.getPhysicalAddressRoles().stream()
        .anyMatch(
            physicalAddressRole ->
                (physicalAddressRole.getCode().equals(physicalAddressRoleCode)
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

    return self.getPhysicalAddressRoles().stream()
        .anyMatch(
            physicalAddressRole -> physicalAddressRole.getCode().equals(physicalAddressRoleCode));
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

    return self.getRolePurposes().stream()
        .anyMatch(rolePurpose -> rolePurpose.getCode().equals(rolePurposeCode));
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

    return self.getRoleTypes().stream()
        .anyMatch(
            roleType ->
                (roleType.getCode().equals(roleTypeCode)
                    && roleType.isValidForPartyType(partyTypeCode)));
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
}
