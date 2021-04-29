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
import java.util.List;
import java.util.Objects;
import java.util.UUID;
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

  /** The External Reference Type Repository. */
  private final ExternalReferenceTypeRepository externalReferenceTypeRepository;

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
   * @param externalReferenceTypeRepository the External Reference Type Repository
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
      ExternalReferenceTypeRepository externalReferenceTypeRepository,
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
    this.externalReferenceTypeRepository = externalReferenceTypeRepository;
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
   * Retrieve the attribute type category reference data for a specific locale.
   *
   * @param localeId the Unicode locale identifier for the locale to retrieve the attribute type
   *     category reference data for
   * @return the attribute type category reference data
   */
  @Override
  @Cacheable(cacheNames = "reference", key = "'attributeTypeCategories.' + #localeId")
  public List<AttributeTypeCategory> getAttributeTypeCategories(String localeId)
      throws InvalidArgumentException, ServiceUnavailableException {
    if (!StringUtils.hasText(localeId)) {
      throw new InvalidArgumentException("localeId");
    }

    try {
      return attributeTypeCategoryRepository.findByLocaleIdIgnoreCase(localeId);
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to retrieve the attribute type category reference data", e);
    }
  }

  /**
   * Retrieve the attribute type category reference data for all locales.
   *
   * @return the attribute type category reference data
   */
  @Override
  @Cacheable(cacheNames = "reference", key = "'attributeTypeCategories.ALL'")
  public List<AttributeTypeCategory> getAttributeTypeCategories()
      throws ServiceUnavailableException {
    try {
      return attributeTypeCategoryRepository.findAll();
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to retrieve the attribute type category reference data", e);
    }
  }

  /**
   * Retrieve the attribute type category reference data for a specific tenant and locale.
   *
   * @param tenantId the Universally Unique Identifier (UUID) for the tenant the attribute type
   *     category reference data is specific to
   * @param localeId the Unicode locale identifier for the locale to retrieve the attribute type
   *     category reference data for
   * @return the attribute type category reference data
   */
  @Override
  public List<AttributeTypeCategory> getAttributeTypeCategories(UUID tenantId, String localeId)
      throws InvalidArgumentException, ServiceUnavailableException {
    return self.getAttributeTypeCategories(localeId).stream()
        .filter(
            attributeTypeCategory ->
                (attributeTypeCategory.getTenantId() == null
                    || (Objects.equals(attributeTypeCategory.getTenantId(), tenantId))))
        .collect(Collectors.toList());
  }

  /**
   * Retrieve the attribute type reference data for a specific locale.
   *
   * @param localeId the Unicode locale identifier for the locale to retrieve the attribute type
   *     reference data for
   * @return the attribute type reference data
   */
  @Override
  @Cacheable(cacheNames = "reference", key = "'attributeTypes.' + #localeId")
  public List<AttributeType> getAttributeTypes(String localeId)
      throws InvalidArgumentException, ServiceUnavailableException {
    if (!StringUtils.hasText(localeId)) {
      throw new InvalidArgumentException("localeId");
    }

    try {
      return attributeTypeRepository.findByLocaleIdIgnoreCase(localeId);
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to retrieve the attribute type reference data", e);
    }
  }

  /**
   * Retrieve the attribute type reference data for all locales.
   *
   * @return the attribute type reference data
   */
  @Override
  @Cacheable(cacheNames = "reference", key = "'attributeTypes.ALL'")
  public List<AttributeType> getAttributeTypes() throws ServiceUnavailableException {
    try {
      return attributeTypeRepository.findAll();
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to retrieve the attribute type reference data", e);
    }
  }

  /**
   * Retrieve the attribute type reference data for a specific tenant and locale.
   *
   * @param tenantId the Universally Unique Identifier (UUID) for the tenant the attribute type
   *     reference data is specific to
   * @param localeId the Unicode locale identifier for the locale to retrieve the attribute type
   *     reference data for
   * @return the attribute type reference data
   */
  @Override
  public List<AttributeType> getAttributeTypes(UUID tenantId, String localeId)
      throws InvalidArgumentException, ServiceUnavailableException {
    return self.getAttributeTypes(localeId).stream()
        .filter(
            attributeType ->
                (attributeType.getTenantId() == null
                    || (Objects.equals(attributeType.getTenantId(), tenantId))))
        .collect(Collectors.toList());
  }

  /**
   * Retrieve the consent type reference data for a specific locale.
   *
   * @param localeId the Unicode locale identifier for the locale to retrieve the consent type
   *     reference data for
   * @return the consent type reference data
   */
  @Override
  @Cacheable(cacheNames = "reference", key = "'consentTypes.' + #localeId")
  public List<ConsentType> getConsentTypes(String localeId)
      throws InvalidArgumentException, ServiceUnavailableException {
    if (!StringUtils.hasText(localeId)) {
      throw new InvalidArgumentException("localeId");
    }

    try {
      return consentTypeRepository.findByLocaleIdIgnoreCase(localeId);
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to retrieve the consent type reference data", e);
    }
  }

  /**
   * Retrieve the consent type reference data for all locales.
   *
   * @return the consent type reference data
   */
  @Override
  @Cacheable(cacheNames = "reference", key = "'consentTypes.ALL'")
  public List<ConsentType> getConsentTypes() throws ServiceUnavailableException {
    try {
      return consentTypeRepository.findAll();
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to retrieve the consent type reference data", e);
    }
  }

  /**
   * Retrieve the consent type reference data for a specific tenant and locale.
   *
   * @param tenantId the Universally Unique Identifier (UUID) for the tenant the consent type
   *     reference data is specific to
   * @param localeId the Unicode locale identifier for the locale to retrieve the consent type
   *     reference data for
   * @return the consent type reference data
   */
  @Override
  public List<ConsentType> getConsentTypes(UUID tenantId, String localeId)
      throws InvalidArgumentException, ServiceUnavailableException {
    return self.getConsentTypes(localeId).stream()
        .filter(
            consentType ->
                (consentType.getTenantId() == null
                    || (Objects.equals(consentType.getTenantId(), tenantId))))
        .collect(Collectors.toList());
  }

  /**
   * Retrieve the contact mechanism purpose reference data for a specific locale.
   *
   * @param localeId the Unicode locale identifier for the locale to retrieve the contact mechanism
   *     purpose reference data for
   * @return the contact mechanism purpose reference data
   */
  @Override
  @Cacheable(cacheNames = "reference", key = "'contactMechanismPurposes.' + #localeId")
  public List<ContactMechanismPurpose> getContactMechanismPurposes(String localeId)
      throws InvalidArgumentException, ServiceUnavailableException {
    if (!StringUtils.hasText(localeId)) {
      throw new InvalidArgumentException("localeId");
    }

    try {
      return contactMechanismPurposeRepository.findByLocaleIdIgnoreCase(localeId);
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to retrieve the contact mechanism purpose reference data", e);
    }
  }

  /**
   * Retrieve the contact mechanism purpose reference data for all locales.
   *
   * @return the contact mechanism purpose reference data
   */
  @Override
  @Cacheable(cacheNames = "reference", key = "'contactMechanismPurposes.ALL'")
  public List<ContactMechanismPurpose> getContactMechanismPurposes()
      throws ServiceUnavailableException {
    try {
      return contactMechanismPurposeRepository.findAll();
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to retrieve the contact mechanism purpose reference data", e);
    }
  }

  /**
   * Retrieve the contact mechanism purpose reference data for a specific tenant and locale.
   *
   * @param tenantId the Universally Unique Identifier (UUID) for the tenant the contact mechanism
   *     purpose reference data is specific to
   * @param localeId the Unicode locale identifier for the locale to retrieve the contact mechanism
   *     purpose reference data for
   * @return the contact mechanism purpose reference data
   */
  @Override
  public List<ContactMechanismPurpose> getContactMechanismPurposes(UUID tenantId, String localeId)
      throws InvalidArgumentException, ServiceUnavailableException {
    return self.getContactMechanismPurposes(localeId).stream()
        .filter(
            contactMechanismPurpose ->
                (contactMechanismPurpose.getTenantId() == null
                    || (Objects.equals(contactMechanismPurpose.getTenantId(), tenantId))))
        .collect(Collectors.toList());
  }

  /**
   * Retrieve the contact mechanism role reference data for a specific locale.
   *
   * @param localeId the Unicode locale identifier for the locale to retrieve the contact mechanism
   *     role reference data for
   * @return the contact mechanism role reference data
   */
  @Override
  @Cacheable(cacheNames = "reference", key = "'contactMechanismRoles.' + #localeId")
  public List<ContactMechanismRole> getContactMechanismRoles(String localeId)
      throws InvalidArgumentException, ServiceUnavailableException {
    if (!StringUtils.hasText(localeId)) {
      throw new InvalidArgumentException("localeId");
    }

    try {
      return contactMechanismRoleRepository.findByLocaleIdIgnoreCase(localeId);
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to retrieve the contact mechanism role reference data", e);
    }
  }

  /**
   * Retrieve the contact mechanism role reference data for all locales.
   *
   * @return the contact mechanism role reference data
   */
  @Override
  @Cacheable(cacheNames = "reference", key = "'contactMechanismRoles.ALL'")
  public List<ContactMechanismRole> getContactMechanismRoles() throws ServiceUnavailableException {
    try {
      return contactMechanismRoleRepository.findAll();
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to retrieve the contact mechanism role reference data", e);
    }
  }

  /**
   * Retrieve the contact mechanism role reference data for a specific tenant and locale.
   *
   * @param tenantId the Universally Unique Identifier (UUID) for the tenant the contact mechanism
   *     role reference data is specific to
   * @param localeId the Unicode locale identifier for the locale to retrieve the contact mechanism
   *     role reference data for
   * @return the contact mechanism role reference data
   */
  @Override
  public List<ContactMechanismRole> getContactMechanismRoles(UUID tenantId, String localeId)
      throws InvalidArgumentException, ServiceUnavailableException {
    return self.getContactMechanismRoles(localeId).stream()
        .filter(
            contactMechanismRole ->
                (contactMechanismRole.getTenantId() == null
                    || (Objects.equals(contactMechanismRole.getTenantId(), tenantId))))
        .collect(Collectors.toList());
  }

  /**
   * Retrieve the contact mechanism type reference data for a specific locale.
   *
   * @param localeId the Unicode locale identifier for the locale to retrieve the contact mechanism
   *     type reference data for
   * @return the contact mechanism type reference data
   */
  @Override
  @Cacheable(cacheNames = "reference", key = "'contactMechanismTypes.' + #localeId")
  public List<ContactMechanismType> getContactMechanismTypes(String localeId)
      throws InvalidArgumentException, ServiceUnavailableException {
    if (!StringUtils.hasText(localeId)) {
      throw new InvalidArgumentException("localeId");
    }

    try {
      return contactMechanismTypeRepository.findByLocaleIdIgnoreCase(localeId);
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to retrieve the contact mechanism type reference data", e);
    }
  }

  /**
   * Retrieve the contact mechanism type reference data for all locales.
   *
   * @return the contact mechanism type reference data
   */
  @Override
  @Cacheable(cacheNames = "reference", key = "'contactMechanismTypes.ALL'")
  public List<ContactMechanismType> getContactMechanismTypes() throws ServiceUnavailableException {
    try {
      return contactMechanismTypeRepository.findAll();
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to retrieve the contact mechanism type reference data", e);
    }
  }

  /**
   * Retrieve the contact mechanism type reference data for a specific tenant and locale.
   *
   * @param tenantId the Universally Unique Identifier (UUID) for the tenant the contact mechanism
   *     type reference data is specific to
   * @param localeId the Unicode locale identifier for the locale to retrieve the contact mechanism
   *     type reference data for
   * @return the contact mechanism type reference data
   */
  @Override
  public List<ContactMechanismType> getContactMechanismTypes(UUID tenantId, String localeId)
      throws InvalidArgumentException, ServiceUnavailableException {
    return self.getContactMechanismTypes(localeId).stream()
        .filter(
            contactMechanismType ->
                (contactMechanismType.getTenantId() == null
                    || (Objects.equals(contactMechanismType.getTenantId(), tenantId))))
        .collect(Collectors.toList());
  }

  /**
   * Retrieve the employment status reference data for a specific locale.
   *
   * @param localeId the Unicode locale identifier for the locale to retrieve the employment status
   *     reference data for
   * @return the employment status reference data
   */
  @Override
  @Cacheable(cacheNames = "reference", key = "'employmentStatuses.' + #localeId")
  public List<EmploymentStatus> getEmploymentStatuses(String localeId)
      throws InvalidArgumentException, ServiceUnavailableException {
    if (!StringUtils.hasText(localeId)) {
      throw new InvalidArgumentException("localeId");
    }

    try {
      return employmentStatusRepository.findByLocaleIdIgnoreCase(localeId);
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to retrieve the employment status reference data", e);
    }
  }

  /**
   * Retrieve the employment status reference data for all locales.
   *
   * @return the employment status reference data
   */
  @Override
  @Cacheable(cacheNames = "reference", key = "'employmentStatuses.ALL'")
  public List<EmploymentStatus> getEmploymentStatuses() throws ServiceUnavailableException {
    try {
      return employmentStatusRepository.findAll();
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to retrieve the employment status reference data", e);
    }
  }

  /**
   * Retrieve the employment status reference data for a specific tenant and locale.
   *
   * @param tenantId the Universally Unique Identifier (UUID) for the tenant the employment status
   *     reference data is specific to
   * @param localeId the Unicode locale identifier for the locale to retrieve the employment status
   *     reference data for
   * @return the employment status reference data
   */
  @Override
  public List<EmploymentStatus> getEmploymentStatuses(UUID tenantId, String localeId)
      throws InvalidArgumentException, ServiceUnavailableException {
    return self.getEmploymentStatuses(localeId).stream()
        .filter(
            employmentStatus ->
                (employmentStatus.getTenantId() == null
                    || (Objects.equals(employmentStatus.getTenantId(), tenantId))))
        .collect(Collectors.toList());
  }

  /**
   * Retrieve the employment type reference data for a specific locale.
   *
   * @param localeId the Unicode locale identifier for the locale to retrieve the employment type
   *     reference data for
   * @return the employment type reference data
   */
  @Override
  @Cacheable(cacheNames = "reference", key = "'employmentTypes.' + #localeId")
  public List<EmploymentType> getEmploymentTypes(String localeId)
      throws InvalidArgumentException, ServiceUnavailableException {
    if (!StringUtils.hasText(localeId)) {
      throw new InvalidArgumentException("localeId");
    }

    try {
      return employmentTypeRepository.findByLocaleIdIgnoreCase(localeId);
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to retrieve the employment type reference data", e);
    }
  }

  /**
   * Retrieve the employment type reference data for all locales.
   *
   * @return the employment type reference data
   */
  @Override
  @Cacheable(cacheNames = "reference", key = "'employmentTypes.ALL'")
  public List<EmploymentType> getEmploymentTypes() throws ServiceUnavailableException {
    try {
      return employmentTypeRepository.findAll();
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to retrieve the employment type reference data", e);
    }
  }

  /**
   * Retrieve the employment type reference data for a specific tenant and locale.
   *
   * @param tenantId the Universally Unique Identifier (UUID) for the tenant the employment type
   *     reference data is specific to
   * @param localeId the Unicode locale identifier for the locale to retrieve the employment type
   *     reference data for
   * @return the employment type reference data
   */
  @Override
  public List<EmploymentType> getEmploymentTypes(UUID tenantId, String localeId)
      throws InvalidArgumentException, ServiceUnavailableException {
    return self.getEmploymentTypes(localeId).stream()
        .filter(
            employmentType ->
                (employmentType.getTenantId() == null
                    || (Objects.equals(employmentType.getTenantId(), tenantId))))
        .collect(Collectors.toList());
  }

  /**
   * Retrieve the external reference type reference data for a specific locale.
   *
   * @param localeId the Unicode locale identifier for the locale to retrieve the external reference
   *     type reference data for
   * @return the external reference type reference data
   */
  @Override
  @Cacheable(cacheNames = "reference", key = "'externalReferenceTypes.' + #localeId")
  public List<ExternalReferenceType> getExternalReferenceTypes(String localeId)
      throws InvalidArgumentException, ServiceUnavailableException {
    if (!StringUtils.hasText(localeId)) {
      throw new InvalidArgumentException("localeId");
    }

    try {
      return externalReferenceTypeRepository.findByLocaleIdIgnoreCase(localeId);
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to retrieve the external reference type reference data", e);
    }
  }

  /**
   * Retrieve the external reference type reference data for all locales.
   *
   * @return the external reference type reference data
   */
  @Override
  @Cacheable(cacheNames = "reference", key = "'externalReferenceTypes.ALL'")
  public List<ExternalReferenceType> getExternalReferenceTypes()
      throws ServiceUnavailableException {
    try {
      return externalReferenceTypeRepository.findAll();
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to retrieve the external reference type reference data", e);
    }
  }

  /**
   * Retrieve the external reference type reference data for a specific tenant and locale.
   *
   * @param tenantId the Universally Unique Identifier (UUID) for the tenant the external reference
   *     type reference data is specific to
   * @param localeId the Unicode locale identifier for the locale to retrieve the external reference
   *     type reference data for
   * @return the external reference type reference data
   */
  @Override
  public List<ExternalReferenceType> getExternalReferenceTypes(UUID tenantId, String localeId)
      throws InvalidArgumentException, ServiceUnavailableException {
    return self.getExternalReferenceTypes(localeId).stream()
        .filter(
            externalReferenceType ->
                (externalReferenceType.getTenantId() == null
                    || (Objects.equals(externalReferenceType.getTenantId(), tenantId))))
        .collect(Collectors.toList());
  }

  /**
   * Retrieve the fields of study reference data for a specific locale.
   *
   * @param localeId the Unicode locale identifier for the locale to retrieve the fields of study
   *     reference data for
   * @return the fields of study reference data
   */
  @Override
  @Cacheable(cacheNames = "reference", key = "'fieldsOfStudy.' + #localeId")
  public List<FieldOfStudy> getFieldsOfStudy(String localeId)
      throws InvalidArgumentException, ServiceUnavailableException {
    if (!StringUtils.hasText(localeId)) {
      throw new InvalidArgumentException("localeId");
    }

    try {
      return fieldOfStudyRepository.findByLocaleIdIgnoreCase(localeId);
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to retrieve the fields of study reference data", e);
    }
  }

  /**
   * Retrieve the fields of study reference data for all locales.
   *
   * @return the fields of study reference data
   */
  @Override
  @Cacheable(cacheNames = "reference", key = "'fieldsOfStudy.ALL'")
  public List<FieldOfStudy> getFieldsOfStudy() throws ServiceUnavailableException {
    try {
      return fieldOfStudyRepository.findAll();
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to retrieve the fields of study reference data", e);
    }
  }

  /**
   * Retrieve the fields of study reference data for a specific tenant and locale.
   *
   * @param tenantId the Universally Unique Identifier (UUID) for the tenant the fields of study
   *     reference data is specific to
   * @param localeId the Unicode locale identifier for the locale to retrieve the fields of study
   *     reference data for
   * @return the fields of study reference data
   */
  @Override
  public List<FieldOfStudy> getFieldsOfStudy(UUID tenantId, String localeId)
      throws InvalidArgumentException, ServiceUnavailableException {
    return self.getFieldsOfStudy(localeId).stream()
        .filter(
            fieldOfStudy ->
                (fieldOfStudy.getTenantId() == null
                    || (Objects.equals(fieldOfStudy.getTenantId(), tenantId))))
        .collect(Collectors.toList());
  }

  /**
   * Retrieve the gender reference data for a specific locale.
   *
   * @param localeId the Unicode locale identifier for the locale to retrieve the gender reference
   *     data for
   * @return the gender reference data
   */
  @Override
  @Cacheable(cacheNames = "reference", key = "'genders.' + #localeId")
  public List<Gender> getGenders(String localeId)
      throws InvalidArgumentException, ServiceUnavailableException {
    if (!StringUtils.hasText(localeId)) {
      throw new InvalidArgumentException("localeId");
    }

    try {
      return genderRepository.findByLocaleIdIgnoreCase(localeId);
    } catch (Throwable e) {
      throw new ServiceUnavailableException("Failed to retrieve the gender reference data", e);
    }
  }

  /**
   * Retrieve the gender reference data for all locales.
   *
   * @return the gender reference data
   */
  @Override
  @Cacheable(cacheNames = "reference", key = "'genders.ALL'")
  public List<Gender> getGenders() throws ServiceUnavailableException {
    try {
      return genderRepository.findAll();
    } catch (Throwable e) {
      throw new ServiceUnavailableException("Failed to retrieve the gender reference data", e);
    }
  }

  /**
   * Retrieve the gender reference data for a specific tenant and locale.
   *
   * @param tenantId the Universally Unique Identifier (UUID) for the tenant the gender reference
   *     data is specific to
   * @param localeId the Unicode locale identifier for the locale to retrieve the gender reference
   *     data for
   * @return the gender reference data
   */
  @Override
  public List<Gender> getGenders(UUID tenantId, String localeId)
      throws InvalidArgumentException, ServiceUnavailableException {
    return self.getGenders(localeId).stream()
        .filter(
            gender ->
                (gender.getTenantId() == null || (Objects.equals(gender.getTenantId(), tenantId))))
        .collect(Collectors.toList());
  }

  /**
   * Retrieve the identity document type reference data for a specific locale.
   *
   * @param localeId the Unicode locale identifier for the locale to retrieve the identity document
   *     type reference data for
   * @return the identity document type reference data
   */
  @Override
  @Cacheable(cacheNames = "reference", key = "'identityDocumentTypes.' + #localeId")
  public List<IdentityDocumentType> getIdentityDocumentTypes(String localeId)
      throws InvalidArgumentException, ServiceUnavailableException {
    if (!StringUtils.hasText(localeId)) {
      throw new InvalidArgumentException("localeId");
    }

    try {
      return identityDocumentTypeRepository.findByLocaleIdIgnoreCase(localeId);
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to retrieve the identity document type reference data", e);
    }
  }

  /**
   * Retrieve the identity document type reference data for all locales.
   *
   * @return the identity document type reference data
   */
  @Override
  @Cacheable(cacheNames = "reference", key = "'identityDocumentTypes.ALL'")
  public List<IdentityDocumentType> getIdentityDocumentTypes() throws ServiceUnavailableException {
    try {
      return identityDocumentTypeRepository.findAll();
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to retrieve the identity document type reference data", e);
    }
  }

  /**
   * Retrieve the identity document type reference data for a specific tenant and locale.
   *
   * @param tenantId the Universally Unique Identifier (UUID) for the tenant the identity document
   *     type reference data is specific to
   * @param localeId the Unicode locale identifier for the locale to retrieve the identity document
   *     type reference data for
   * @return the identity document type reference data
   */
  @Override
  public List<IdentityDocumentType> getIdentityDocumentTypes(UUID tenantId, String localeId)
      throws InvalidArgumentException, ServiceUnavailableException {
    return self.getIdentityDocumentTypes(localeId).stream()
        .filter(
            identityDocumentType ->
                (identityDocumentType.getTenantId() == null
                    || (Objects.equals(identityDocumentType.getTenantId(), tenantId))))
        .collect(Collectors.toList());
  }

  /**
   * Retrieve the lock type category reference data for a specific locale.
   *
   * @param localeId the Unicode locale identifier for the locale to retrieve the lock type category
   *     reference data for
   * @return the lock type category reference data
   */
  @Override
  @Cacheable(cacheNames = "reference", key = "'lockTypeCategories.' + #localeId")
  public List<LockTypeCategory> getLockTypeCategories(String localeId)
      throws InvalidArgumentException, ServiceUnavailableException {
    if (!StringUtils.hasText(localeId)) {
      throw new InvalidArgumentException("localeId");
    }

    try {
      return lockTypeCategoryRepository.findByLocaleIdIgnoreCase(localeId);
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to retrieve the lock type category reference data", e);
    }
  }

  /**
   * Retrieve the lock type category reference data for all locales.
   *
   * @return the lock type category reference data
   */
  @Override
  @Cacheable(cacheNames = "reference", key = "'lockTypeCategories.ALL'")
  public List<LockTypeCategory> getLockTypeCategories() throws ServiceUnavailableException {
    try {
      return lockTypeCategoryRepository.findAll();
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to retrieve the lock type category reference data", e);
    }
  }

  /**
   * Retrieve the lock type category reference data for a specific tenant and locale.
   *
   * @param tenantId the Universally Unique Identifier (UUID) for the tenant the lock type category
   *     reference data is specific to
   * @param localeId the Unicode locale identifier for the locale to retrieve the lock type category
   *     reference data for
   * @return the lock type category reference data
   */
  @Override
  public List<LockTypeCategory> getLockTypeCategories(UUID tenantId, String localeId)
      throws InvalidArgumentException, ServiceUnavailableException {
    return self.getLockTypeCategories(localeId).stream()
        .filter(
            lockTypeCategory ->
                (lockTypeCategory.getTenantId() == null
                    || (Objects.equals(lockTypeCategory.getTenantId(), tenantId))))
        .collect(Collectors.toList());
  }

  /**
   * Retrieve the lock type reference data for a specific locale.
   *
   * @param localeId the Unicode locale identifier for the locale to retrieve the lock type
   *     reference data for
   * @return the lock type reference data
   */
  @Override
  @Cacheable(cacheNames = "reference", key = "'lockTypes.' + #localeId")
  public List<LockType> getLockTypes(String localeId)
      throws InvalidArgumentException, ServiceUnavailableException {
    if (!StringUtils.hasText(localeId)) {
      throw new InvalidArgumentException("localeId");
    }

    try {
      return lockTypeRepository.findByLocaleIdIgnoreCase(localeId);
    } catch (Throwable e) {
      throw new ServiceUnavailableException("Failed to retrieve the lock type reference data", e);
    }
  }

  /**
   * Retrieve the lock type reference data for all locales.
   *
   * @return the lock type reference data
   */
  @Override
  @Cacheable(cacheNames = "reference", key = "'lockTypes.ALL'")
  public List<LockType> getLockTypes() throws ServiceUnavailableException {
    try {
      return lockTypeRepository.findAll();
    } catch (Throwable e) {
      throw new ServiceUnavailableException("Failed to retrieve the lock type reference data", e);
    }
  }

  /**
   * Retrieve the lock type reference data for a specific tenant and locale.
   *
   * @param tenantId the Universally Unique Identifier (UUID) for the tenant the lock type reference
   *     data is specific to
   * @param localeId the Unicode locale identifier for the locale to retrieve the lock type
   *     reference data for
   * @return the lock type reference data
   */
  @Override
  public List<LockType> getLockTypes(UUID tenantId, String localeId)
      throws InvalidArgumentException, ServiceUnavailableException {
    return self.getLockTypes(localeId).stream()
        .filter(
            lockType ->
                (lockType.getTenantId() == null
                    || (Objects.equals(lockType.getTenantId(), tenantId))))
        .collect(Collectors.toList());
  }

  /**
   * Retrieve the marital status reference data for a specific locale.
   *
   * @param localeId the Unicode locale identifier for the locale to retrieve the marital status
   *     reference data for
   * @return the marital status reference data
   */
  @Override
  @Cacheable(cacheNames = "reference", key = "'maritalStatuses.' + #localeId")
  public List<MaritalStatus> getMaritalStatuses(String localeId)
      throws InvalidArgumentException, ServiceUnavailableException {
    if (!StringUtils.hasText(localeId)) {
      throw new InvalidArgumentException("localeId");
    }

    try {
      return maritalStatusRepository.findByLocaleIdIgnoreCase(localeId);
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to retrieve the marital status reference data", e);
    }
  }

  /**
   * Retrieve the marital status reference data for all locales.
   *
   * @return the marital status reference data
   */
  @Override
  @Cacheable(cacheNames = "reference", key = "'maritalStatuses.ALL'")
  public List<MaritalStatus> getMaritalStatuses() throws ServiceUnavailableException {
    try {
      return maritalStatusRepository.findAll();
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to retrieve the marital status reference data", e);
    }
  }

  /**
   * Retrieve the marital status reference data for a specific tenant and locale.
   *
   * @param tenantId the Universally Unique Identifier (UUID) for the tenant the marital status
   *     reference data is specific to
   * @param localeId the Unicode locale identifier for the locale to retrieve the marital status
   *     reference data for
   * @return the marital status reference data
   */
  @Override
  public List<MaritalStatus> getMaritalStatuses(UUID tenantId, String localeId)
      throws InvalidArgumentException, ServiceUnavailableException {
    return self.getMaritalStatuses(localeId).stream()
        .filter(
            maritalStatus ->
                (maritalStatus.getTenantId() == null
                    || (Objects.equals(maritalStatus.getTenantId(), tenantId))))
        .collect(Collectors.toList());
  }

  /**
   * Retrieve the marriage type reference data for a specific locale.
   *
   * @param localeId the Unicode locale identifier for the locale to retrieve the marriage type
   *     reference data
   * @return the marriage type reference data
   */
  @Override
  @Cacheable(cacheNames = "reference", key = "'marriageTypes.' + #localeId")
  public List<MarriageType> getMarriageTypes(String localeId)
      throws InvalidArgumentException, ServiceUnavailableException {
    if (!StringUtils.hasText(localeId)) {
      throw new InvalidArgumentException("localeId");
    }

    try {
      return marriageTypeRepository.findByLocaleIdIgnoreCase(localeId);
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to retrieve the marriage type reference data", e);
    }
  }

  /**
   * Retrieve the marriage type reference data for all locales.
   *
   * @return the marriage type reference data
   */
  @Override
  @Cacheable(cacheNames = "reference", key = "'marriageTypes.ALL'")
  public List<MarriageType> getMarriageTypes() throws ServiceUnavailableException {
    try {
      return marriageTypeRepository.findAll();
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to retrieve the marriage type reference data", e);
    }
  }

  /**
   * Retrieve the marriage type reference data for a specific tenant and locale.
   *
   * @param tenantId the Universally Unique Identifier (UUID) for the tenant the marriage type
   *     reference data is specific to
   * @param localeId the Unicode locale identifier for the locale to retrieve the marriage type
   *     reference data
   * @return the marriage type reference data
   */
  @Override
  public List<MarriageType> getMarriageTypes(UUID tenantId, String localeId)
      throws InvalidArgumentException, ServiceUnavailableException {
    return self.getMarriageTypes(localeId).stream()
        .filter(
            marriageType ->
                (marriageType.getTenantId() == null
                    || (Objects.equals(marriageType.getTenantId(), tenantId))))
        .collect(Collectors.toList());
  }

  /**
   * Retrieve the next of kin type reference data for a specific locale.
   *
   * @param localeId the Unicode locale identifier for the locale to retrieve the next of kin type
   *     reference data for
   * @return the next of kin type reference data
   */
  @Override
  @Cacheable(cacheNames = "reference", key = "'nextOfKinTypes.' + #localeId")
  public List<NextOfKinType> getNextOfKinTypes(String localeId)
      throws InvalidArgumentException, ServiceUnavailableException {
    if (!StringUtils.hasText(localeId)) {
      throw new InvalidArgumentException("localeId");
    }

    try {
      return nextOfKinTypeRepository.findByLocaleIdIgnoreCase(localeId);
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to retrieve the next of kin type reference data", e);
    }
  }

  /**
   * Retrieve the next of kin type reference data for all locales.
   *
   * @return the next of kin type reference data
   */
  @Override
  @Cacheable(cacheNames = "reference", key = "'nextOfKinTypes.ALL'")
  public List<NextOfKinType> getNextOfKinTypes() throws ServiceUnavailableException {
    try {
      return nextOfKinTypeRepository.findAll();
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to retrieve the next of kin type reference data", e);
    }
  }

  /**
   * Retrieve the next of kin type reference data for a specific tenant and locale.
   *
   * @param tenantId the Universally Unique Identifier (UUID) for the tenant the next of kin type
   *     reference data is specific to
   * @param localeId the Unicode locale identifier for the locale to retrieve the next of kin type
   *     reference data for
   * @return the next of kin type reference data
   */
  @Override
  public List<NextOfKinType> getNextOfKinTypes(UUID tenantId, String localeId)
      throws InvalidArgumentException, ServiceUnavailableException {
    return self.getNextOfKinTypes(localeId).stream()
        .filter(
            nextOfKinType ->
                (nextOfKinType.getTenantId() == null
                    || (Objects.equals(nextOfKinType.getTenantId(), tenantId))))
        .collect(Collectors.toList());
  }

  /**
   * Retrieve the occupation reference data for a specific locale.
   *
   * @param localeId the Unicode locale identifier for the locale to retrieve the occupation
   *     reference data for
   * @return the occupation reference data
   */
  @Override
  @Cacheable(cacheNames = "reference", key = "'occupations.' + #localeId")
  public List<Occupation> getOccupations(String localeId)
      throws InvalidArgumentException, ServiceUnavailableException {
    if (!StringUtils.hasText(localeId)) {
      throw new InvalidArgumentException("localeId");
    }

    try {
      return occupationRepository.findByLocaleIdIgnoreCase(localeId);
    } catch (Throwable e) {
      throw new ServiceUnavailableException("Failed to retrieve the occupation reference data", e);
    }
  }

  /**
   * Retrieve the occupation reference data for all locales.
   *
   * @return the occupation reference data
   */
  @Override
  @Cacheable(cacheNames = "reference", key = "'occupations.ALL'")
  public List<Occupation> getOccupations() throws ServiceUnavailableException {
    try {
      return occupationRepository.findAll();
    } catch (Throwable e) {
      throw new ServiceUnavailableException("Failed to retrieve the occupation reference data", e);
    }
  }

  /**
   * Retrieve the occupation reference data for a specific tenant and locale.
   *
   * @param tenantId the Universally Unique Identifier (UUID) for the tenant the occupation
   *     reference data is specific to
   * @param localeId the Unicode locale identifier for the locale to retrieve the occupation
   *     reference data for
   * @return the occupation reference data
   */
  @Override
  public List<Occupation> getOccupations(UUID tenantId, String localeId)
      throws InvalidArgumentException, ServiceUnavailableException {
    return self.getOccupations(localeId).stream()
        .filter(
            occupation ->
                (occupation.getTenantId() == null
                    || (Objects.equals(occupation.getTenantId(), tenantId))))
        .collect(Collectors.toList());
  }

  /**
   * Retrieve the physical address purpose reference data for a specific locale.
   *
   * @param localeId the Unicode locale identifier for the locale to retrieve the physical address
   *     purpose reference data for
   * @return the physical address purpose reference data
   */
  @Override
  @Cacheable(cacheNames = "reference", key = "'physicalAddressPurposes.' + #localeId")
  public List<PhysicalAddressPurpose> getPhysicalAddressPurposes(String localeId)
      throws InvalidArgumentException, ServiceUnavailableException {
    if (!StringUtils.hasText(localeId)) {
      throw new InvalidArgumentException("localeId");
    }

    try {
      return physicalAddressPurposeRepository.findByLocaleIdIgnoreCase(localeId);
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to retrieve the physical address purpose reference data", e);
    }
  }

  /**
   * Retrieve the physical address purpose reference data for all locales.
   *
   * @return the physical address purpose reference data
   */
  @Override
  @Cacheable(cacheNames = "reference", key = "'physicalAddressPurposes.ALL'")
  public List<PhysicalAddressPurpose> getPhysicalAddressPurposes()
      throws ServiceUnavailableException {
    try {
      return physicalAddressPurposeRepository.findAll();
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to retrieve the physical address purpose reference data", e);
    }
  }

  /**
   * Retrieve the physical address purpose reference data for a specific tenant and locale.
   *
   * @param tenantId the Universally Unique Identifier (UUID) for the tenant the physical address
   *     purpose reference data is specific to
   * @param localeId the Unicode locale identifier for the locale to retrieve the physical address
   *     purpose reference data for
   * @return the physical address purpose reference data
   */
  @Override
  public List<PhysicalAddressPurpose> getPhysicalAddressPurposes(UUID tenantId, String localeId)
      throws InvalidArgumentException, ServiceUnavailableException {
    return self.getPhysicalAddressPurposes(localeId).stream()
        .filter(
            physicalAddressPurpose ->
                (physicalAddressPurpose.getTenantId() == null
                    || (Objects.equals(physicalAddressPurpose.getTenantId(), tenantId))))
        .collect(Collectors.toList());
  }

  /**
   * Retrieve the physical address role reference data for a specific locale.
   *
   * @param localeId the Unicode locale identifier for the locale to retrieve the physical address
   *     role reference data for
   * @return the physical address role reference data
   */
  @Override
  @Cacheable(cacheNames = "reference", key = "'physicalAddressRoles.' + #localeId")
  public List<PhysicalAddressRole> getPhysicalAddressRoles(String localeId)
      throws InvalidArgumentException, ServiceUnavailableException {
    if (!StringUtils.hasText(localeId)) {
      throw new InvalidArgumentException("localeId");
    }

    try {
      return physicalAddressRoleRepository.findByLocaleIdIgnoreCase(localeId);
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to retrieve the physical address role reference data", e);
    }
  }

  /**
   * Retrieve the physical address role reference data for all locales.
   *
   * @return the physical address role reference data
   */
  @Override
  @Cacheable(cacheNames = "reference", key = "'physicalAddressRoles.ALL'")
  public List<PhysicalAddressRole> getPhysicalAddressRoles() throws ServiceUnavailableException {
    try {
      return physicalAddressRoleRepository.findAll();
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to retrieve the physical address role reference data", e);
    }
  }

  /**
   * Retrieve the physical address role reference data for a specific tenant and locale.
   *
   * @param tenantId the Universally Unique Identifier (UUID) for the tenant the physical address
   *     role reference data is specific to
   * @param localeId the Unicode locale identifier for the locale to retrieve the physical address
   *     role reference data for
   * @return the physical address role reference data
   */
  @Override
  public List<PhysicalAddressRole> getPhysicalAddressRoles(UUID tenantId, String localeId)
      throws InvalidArgumentException, ServiceUnavailableException {
    return self.getPhysicalAddressRoles(localeId).stream()
        .filter(
            physicalAddressRole ->
                (physicalAddressRole.getTenantId() == null
                    || (Objects.equals(physicalAddressRole.getTenantId(), tenantId))))
        .collect(Collectors.toList());
  }

  /**
   * Retrieve the physical address type reference data for a specific locale.
   *
   * @param localeId the Unicode locale identifier for the locale to retrieve the physical address
   *     type reference data for
   * @return the physical address type reference data
   */
  @Override
  @Cacheable(cacheNames = "reference", key = "'physicalAddressTypes.' + #localeId")
  public List<PhysicalAddressType> getPhysicalAddressTypes(String localeId)
      throws InvalidArgumentException, ServiceUnavailableException {
    if (!StringUtils.hasText(localeId)) {
      throw new InvalidArgumentException("localeId");
    }

    try {
      return physicalAddressTypeRepository.findByLocaleIdIgnoreCase(localeId);
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to retrieve the physical address type reference data", e);
    }
  }

  /**
   * Retrieve the physical address type reference data for all locales.
   *
   * @return the physical address type reference data
   */
  @Override
  @Cacheable(cacheNames = "reference", key = "'physicalAddressTypes.ALL'")
  public List<PhysicalAddressType> getPhysicalAddressTypes() throws ServiceUnavailableException {
    try {
      return physicalAddressTypeRepository.findAll();
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to retrieve the physical address type reference data", e);
    }
  }

  /**
   * Retrieve the physical address type reference data for a specific tenant and locale.
   *
   * @param tenantId the Universally Unique Identifier (UUID) for the tenant the physical address
   *     type reference data is specific to
   * @param localeId the Unicode locale identifier for the locale to retrieve the physical address
   *     type reference data for
   * @return the physical address type reference data
   */
  @Override
  public List<PhysicalAddressType> getPhysicalAddressTypes(UUID tenantId, String localeId)
      throws InvalidArgumentException, ServiceUnavailableException {
    return self.getPhysicalAddressTypes(localeId).stream()
        .filter(
            physicalAddressType ->
                (physicalAddressType.getTenantId() == null
                    || (Objects.equals(physicalAddressType.getTenantId(), tenantId))))
        .collect(Collectors.toList());
  }

  /**
   * Retrieve the preference type category reference data for a specific locale.
   *
   * @param localeId the Unicode locale identifier for the locale to retrieve the preference type
   *     category reference data for
   * @return the preference type category reference data
   */
  @Override
  @Cacheable(cacheNames = "reference", key = "'preferenceTypeCategories.' + #localeId")
  public List<PreferenceTypeCategory> getPreferenceTypeCategories(String localeId)
      throws InvalidArgumentException, ServiceUnavailableException {
    if (!StringUtils.hasText(localeId)) {
      throw new InvalidArgumentException("localeId");
    }

    try {
      return preferenceTypeCategoryRepository.findByLocaleIdIgnoreCase(localeId);
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to retrieve the preference type category reference data", e);
    }
  }

  /**
   * Retrieve the preference type category reference data for all locales.
   *
   * @return the preference type category reference data
   */
  @Override
  @Cacheable(cacheNames = "reference", key = "'preferenceTypeCategories.ALL'")
  public List<PreferenceTypeCategory> getPreferenceTypeCategories()
      throws ServiceUnavailableException {
    try {
      return preferenceTypeCategoryRepository.findAll();
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to retrieve the preference type category reference data", e);
    }
  }

  /**
   * Retrieve the preference type category reference data for a specific tenant and locale.
   *
   * @param tenantId the Universally Unique Identifier (UUID) for the tenant the preference type
   *     category reference data is specific to
   * @param localeId the Unicode locale identifier for the locale to retrieve the preference type
   *     category reference data for
   * @return the preference type category reference data
   */
  @Override
  public List<PreferenceTypeCategory> getPreferenceTypeCategories(UUID tenantId, String localeId)
      throws InvalidArgumentException, ServiceUnavailableException {
    return self.getPreferenceTypeCategories(localeId).stream()
        .filter(
            preferenceTypeCategory ->
                (preferenceTypeCategory.getTenantId() == null
                    || (Objects.equals(preferenceTypeCategory.getTenantId(), tenantId))))
        .collect(Collectors.toList());
  }

  /**
   * Retrieve the preference type reference data for a specific locale.
   *
   * @param localeId the Unicode locale identifier for the locale to retrieve the preference type
   *     reference data for
   * @return the preference type reference data
   */
  @Override
  @Cacheable(cacheNames = "reference", key = "'preferenceTypes.' + #localeId")
  public List<PreferenceType> getPreferenceTypes(String localeId)
      throws InvalidArgumentException, ServiceUnavailableException {
    if (!StringUtils.hasText(localeId)) {
      throw new InvalidArgumentException("localeId");
    }

    try {
      return preferenceTypeRepository.findByLocaleIdIgnoreCase(localeId);
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to retrieve the preference type reference data", e);
    }
  }

  /**
   * Retrieve the preference type reference data for all locales.
   *
   * @return the preference type reference data
   */
  @Override
  @Cacheable(cacheNames = "reference", key = "'preferenceTypes.ALL'")
  public List<PreferenceType> getPreferenceTypes() throws ServiceUnavailableException {
    try {
      return preferenceTypeRepository.findAll();
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to retrieve the preference type reference data", e);
    }
  }

  /**
   * Retrieve the preference type reference data for a specific tenant and locale.
   *
   * @param tenantId the Universally Unique Identifier (UUID) for the tenant the preference type
   *     reference data is specific to
   * @param localeId the Unicode locale identifier for the locale to retrieve the preference type
   *     reference data for
   * @return the preference type reference data
   */
  @Override
  public List<PreferenceType> getPreferenceTypes(UUID tenantId, String localeId)
      throws InvalidArgumentException, ServiceUnavailableException {
    return self.getPreferenceTypes(localeId).stream()
        .filter(
            preferenceType ->
                (preferenceType.getTenantId() == null
                    || (Objects.equals(preferenceType.getTenantId(), tenantId))))
        .collect(Collectors.toList());
  }

  /**
   * Retrieve the qualification type reference data for a specific locale.
   *
   * @param localeId the Unicode locale identifier for the locale to retrieve the qualification type
   *     reference data for
   * @return the qualification type reference data
   */
  @Override
  @Cacheable(cacheNames = "reference", key = "'qualificationTypes.' + #localeId")
  public List<QualificationType> getQualificationTypes(String localeId)
      throws InvalidArgumentException, ServiceUnavailableException {
    if (!StringUtils.hasText(localeId)) {
      throw new InvalidArgumentException("localeId");
    }

    try {
      return qualificationTypeRepository.findByLocaleIdIgnoreCase(localeId);
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to retrieve the qualification type reference data", e);
    }
  }

  /**
   * Retrieve the qualification type reference data for all locales.
   *
   * @return the qualification type reference data
   */
  @Override
  @Cacheable(cacheNames = "reference", key = "'qualificationTypes.ALL'")
  public List<QualificationType> getQualificationTypes() throws ServiceUnavailableException {
    try {
      return qualificationTypeRepository.findAll();
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to retrieve the qualification type reference data", e);
    }
  }

  /**
   * Retrieve the qualification type reference data for a specific tenant and locale.
   *
   * @param tenantId the Universally Unique Identifier (UUID) for the tenant the qualification type
   *     reference data is specific to
   * @param localeId the Unicode locale identifier for the locale to retrieve the qualification type
   *     reference data for
   * @return the qualification type reference data
   */
  @Override
  public List<QualificationType> getQualificationTypes(UUID tenantId, String localeId)
      throws InvalidArgumentException, ServiceUnavailableException {
    return self.getQualificationTypes(localeId).stream()
        .filter(
            qualificationType ->
                (qualificationType.getTenantId() == null
                    || (Objects.equals(qualificationType.getTenantId(), tenantId))))
        .collect(Collectors.toList());
  }

  /**
   * Retrieve the race reference data for a specific locale.
   *
   * @param localeId the Unicode locale identifier for the locale to retrieve the race reference
   *     data for
   * @return the race reference data
   */
  @Override
  @Cacheable(cacheNames = "reference", key = "'races.' + #localeId")
  public List<Race> getRaces(String localeId)
      throws InvalidArgumentException, ServiceUnavailableException {
    if (!StringUtils.hasText(localeId)) {
      throw new InvalidArgumentException("localeId");
    }

    try {
      return raceRepository.findByLocaleIdIgnoreCase(localeId);
    } catch (Throwable e) {
      throw new ServiceUnavailableException("Failed to retrieve the race reference data", e);
    }
  }

  /**
   * Retrieve the race reference data for all locales.
   *
   * @return the race reference data
   */
  @Override
  @Cacheable(cacheNames = "reference", key = "'races.ALL'")
  public List<Race> getRaces() throws ServiceUnavailableException {
    try {
      return raceRepository.findAll();
    } catch (Throwable e) {
      throw new ServiceUnavailableException("Failed to retrieve the race reference data", e);
    }
  }

  /**
   * Retrieve the race reference data for a specific tenant and locale.
   *
   * @param tenantId the Universally Unique Identifier (UUID) for the tenant the race reference data
   *     is specific to
   * @param localeId the Unicode locale identifier for the locale to retrieve the race reference
   *     data for
   * @return the race reference data
   */
  @Override
  public List<Race> getRaces(UUID tenantId, String localeId)
      throws InvalidArgumentException, ServiceUnavailableException {
    return self.getRaces(localeId).stream()
        .filter(
            race -> (race.getTenantId() == null || (Objects.equals(race.getTenantId(), tenantId))))
        .collect(Collectors.toList());
  }

  /**
   * Retrieve the relationship type reference data for a specific locale.
   *
   * @param localeId the Unicode locale identifier for the locale to retrieve the relationship type
   *     reference data for
   * @return the relationship type reference data
   */
  @Override
  @Cacheable(cacheNames = "reference", key = "'relationshipTypes.' + #localeId")
  public List<RelationshipType> getRelationshipTypes(String localeId)
      throws InvalidArgumentException, ServiceUnavailableException {
    if (!StringUtils.hasText(localeId)) {
      throw new InvalidArgumentException("localeId");
    }

    try {
      return relationshipTypeRepository.findByLocaleIdIgnoreCase(localeId);
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to retrieve the relationship type reference data", e);
    }
  }

  /**
   * Retrieve the relationship type reference data for all locales.
   *
   * @return the relationship type reference data
   */
  @Override
  @Cacheable(cacheNames = "reference", key = "'relationshipTypes.ALL'")
  public List<RelationshipType> getRelationshipTypes() throws ServiceUnavailableException {
    try {
      return relationshipTypeRepository.findAll();
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to retrieve the relationship type reference data", e);
    }
  }

  /**
   * Retrieve the relationship type reference data for a specific tenant and locale.
   *
   * @param tenantId the Universally Unique Identifier (UUID) for the tenant the relationship type
   *     reference data is specific to
   * @param localeId the Unicode locale identifier for the locale to retrieve the relationship type
   *     reference data for
   * @return the relationship type reference data
   */
  @Override
  public List<RelationshipType> getRelationshipTypes(UUID tenantId, String localeId)
      throws InvalidArgumentException, ServiceUnavailableException {
    return self.getRelationshipTypes(localeId).stream()
        .filter(
            relationshipType ->
                (relationshipType.getTenantId() == null
                    || (Objects.equals(relationshipType.getTenantId(), tenantId))))
        .collect(Collectors.toList());
  }

  /**
   * Retrieve the residence permit type reference data for a specific locale.
   *
   * @param localeId the Unicode locale identifier for the locale to retrieve the residence permit
   *     type reference data for
   * @return the residence permit type reference data
   */
  @Override
  @Cacheable(cacheNames = "reference", key = "'residencePermitTypes.' + #localeId")
  public List<ResidencePermitType> getResidencePermitTypes(String localeId)
      throws InvalidArgumentException, ServiceUnavailableException {
    if (!StringUtils.hasText(localeId)) {
      throw new InvalidArgumentException("localeId");
    }

    try {
      return residencePermitTypeRepository.findByLocaleIdIgnoreCase(localeId);
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to retrieve the residence permit type reference data", e);
    }
  }

  /**
   * Retrieve the residence permit type reference data for all locales.
   *
   * @return the residence permit type reference data
   */
  @Override
  @Cacheable(cacheNames = "reference", key = "'residencePermitTypes.ALL'")
  public List<ResidencePermitType> getResidencePermitTypes() throws ServiceUnavailableException {
    try {
      return residencePermitTypeRepository.findAll();
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to retrieve the residence permit type reference data", e);
    }
  }

  /**
   * Retrieve the residence permit type reference data for a specific tenant and locale.
   *
   * @param tenantId the Universally Unique Identifier (UUID) for the tenant the residence permit
   *     type reference data is specific to
   * @param localeId the Unicode locale identifier for the locale to retrieve the residence permit
   *     type reference data for
   * @return the residence permit type reference data
   */
  @Override
  public List<ResidencePermitType> getResidencePermitTypes(UUID tenantId, String localeId)
      throws InvalidArgumentException, ServiceUnavailableException {
    return self.getResidencePermitTypes(localeId).stream()
        .filter(
            residencePermitType ->
                (residencePermitType.getTenantId() == null
                    || (Objects.equals(residencePermitType.getTenantId(), tenantId))))
        .collect(Collectors.toList());
  }

  /**
   * Retrieve the residency status reference data for a specific locale.
   *
   * @param localeId the Unicode locale identifier for the locale to retrieve the residency status
   *     reference data for
   * @return the residency status reference data
   */
  @Override
  @Cacheable(cacheNames = "reference", key = "'residencyStatuses.' + #localeId")
  public List<ResidencyStatus> getResidencyStatuses(String localeId)
      throws InvalidArgumentException, ServiceUnavailableException {
    if (!StringUtils.hasText(localeId)) {
      throw new InvalidArgumentException("localeId");
    }

    try {
      return residencyStatusRepository.findByLocaleIdIgnoreCase(localeId);
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to retrieve the residency status reference data", e);
    }
  }

  /**
   * Retrieve the residency status reference data for all locales.
   *
   * @return the residency status reference data
   */
  @Override
  @Cacheable(cacheNames = "reference", key = "'residencyStatuses.ALL'")
  public List<ResidencyStatus> getResidencyStatuses() throws ServiceUnavailableException {
    try {
      return residencyStatusRepository.findAll();
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to retrieve the residency status reference data", e);
    }
  }

  /**
   * Retrieve the residency status reference data for a specific tenant and locale.
   *
   * @param tenantId the Universally Unique Identifier (UUID) for the tenant the residency status
   *     reference data is specific to
   * @param localeId the Unicode locale identifier for the locale to retrieve the residency status
   *     reference data for
   * @return the residency status reference data
   */
  @Override
  public List<ResidencyStatus> getResidencyStatuses(UUID tenantId, String localeId)
      throws InvalidArgumentException, ServiceUnavailableException {
    return self.getResidencyStatuses(localeId).stream()
        .filter(
            residencyStatus ->
                (residencyStatus.getTenantId() == null
                    || (Objects.equals(residencyStatus.getTenantId(), tenantId))))
        .collect(Collectors.toList());
  }

  /**
   * Retrieve the residential type reference data for a specific locale.
   *
   * @param localeId the Unicode locale identifier for the locale to retrieve the residential type
   *     reference data for
   * @return the residential type reference data
   */
  @Override
  @Cacheable(cacheNames = "reference", key = "'residentialTypes.' + #localeId")
  public List<ResidentialType> getResidentialTypes(String localeId)
      throws InvalidArgumentException, ServiceUnavailableException {
    if (!StringUtils.hasText(localeId)) {
      throw new InvalidArgumentException("localeId");
    }

    try {
      return residentialTypeRepository.findByLocaleIdIgnoreCase(localeId);
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to retrieve the residential type reference data", e);
    }
  }

  /**
   * Retrieve the residential type reference data for all locales.
   *
   * @return the residential type reference data
   */
  @Override
  @Cacheable(cacheNames = "reference", key = "'residentialTypes.ALL'")
  public List<ResidentialType> getResidentialTypes() throws ServiceUnavailableException {
    try {
      return residentialTypeRepository.findAll();
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to retrieve the residential type reference data", e);
    }
  }

  /**
   * Retrieve the residential type reference data for a specific tenant and locale.
   *
   * @param tenantId the Universally Unique Identifier (UUID) for the tenant the residential type
   *     reference data is specific to
   * @param localeId the Unicode locale identifier for the locale to retrieve the residential type
   *     reference data for
   * @return the residential type reference data
   */
  @Override
  public List<ResidentialType> getResidentialTypes(UUID tenantId, String localeId)
      throws InvalidArgumentException, ServiceUnavailableException {
    return self.getResidentialTypes(localeId).stream()
        .filter(
            residentialType ->
                (residentialType.getTenantId() == null
                    || (Objects.equals(residentialType.getTenantId(), tenantId))))
        .collect(Collectors.toList());
  }

  /**
   * Retrieve the role purpose reference data for a specific locale.
   *
   * @param localeId the Unicode locale identifier for the locale to retrieve the role purpose
   *     reference data for
   * @return the role purpose reference data
   */
  @Override
  @Cacheable(cacheNames = "reference", key = "'rolePurposes.' + #localeId")
  public List<RolePurpose> getRolePurposes(String localeId)
      throws InvalidArgumentException, ServiceUnavailableException {
    if (!StringUtils.hasText(localeId)) {
      throw new InvalidArgumentException("localeId");
    }

    try {
      return rolePurposeRepository.findByLocaleIdIgnoreCase(localeId);
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to retrieve the role purpose reference data", e);
    }
  }

  /**
   * Retrieve the role purpose reference data for all locales.
   *
   * @return the role purpose reference data
   */
  @Override
  @Cacheable(cacheNames = "reference", key = "'rolePurposes.ALL'")
  public List<RolePurpose> getRolePurposes() throws ServiceUnavailableException {
    try {
      return rolePurposeRepository.findAll();
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to retrieve the role purpose reference data", e);
    }
  }

  /**
   * Retrieve the role purpose reference data for a specific tenant and locale.
   *
   * @param tenantId the Universally Unique Identifier (UUID) for the tenant the role purpose
   *     reference data is specific to
   * @param localeId the Unicode locale identifier for the locale to retrieve the role purpose
   *     reference data for
   * @return the role purpose reference data
   */
  @Override
  public List<RolePurpose> getRolePurposes(UUID tenantId, String localeId)
      throws InvalidArgumentException, ServiceUnavailableException {
    return self.getRolePurposes(localeId).stream()
        .filter(
            rolePurpose ->
                (rolePurpose.getTenantId() == null
                    || (Objects.equals(rolePurpose.getTenantId(), tenantId))))
        .collect(Collectors.toList());
  }

  /**
   * Retrieve the role type attribute type constraints for all role types.
   *
   * @return the role type attribute type constraints
   */
  @Override
  @Cacheable(cacheNames = "reference", key = "'roleTypeAttributeTypeConstraints.ALL'")
  public List<RoleTypeAttributeTypeConstraint> getRoleTypeAttributeTypeConstraints()
      throws ServiceUnavailableException {
    try {
      return roleTypeAttributeTypeConstraintRepository.findAll(
          Sort.by(Direction.ASC, "roleType", "attributeType"));
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to retrieve the role type attribute type constraints", e);
    }
  }

  /**
   * Retrieve the role type attribute type constraints for a specific role type.
   *
   * @param roleType the code for the role type to retrieve the attribute constraints for
   * @return the role type attribute type constraints
   */
  @Override
  @Cacheable(cacheNames = "reference", key = "'roleTypeAttributeTypeConstraints.' + #roleType")
  public List<RoleTypeAttributeTypeConstraint> getRoleTypeAttributeTypeConstraints(String roleType)
      throws InvalidArgumentException, ServiceUnavailableException {
    if (!StringUtils.hasText(roleType)) {
      throw new InvalidArgumentException("roleType");
    }

    try {
      return roleTypeAttributeTypeConstraintRepository.findByRoleTypeIgnoreCase(
          roleType, Sort.by(Direction.ASC, "roleType", "attributeType"));
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to retrieve the role type attribute type constraints", e);
    }
  }

  /**
   * Retrieve the role type preference type constraints for all role types.
   *
   * @return the role type preference type constraints
   */
  @Override
  @Cacheable(cacheNames = "reference", key = "'roleTypePreferenceTypeConstraints.ALL'")
  public List<RoleTypePreferenceTypeConstraint> getRoleTypePreferenceTypeConstraints()
      throws ServiceUnavailableException {
    try {
      return roleTypePreferenceTypeConstraintRepository.findAll(
          Sort.by(Direction.ASC, "roleType", "preferenceType"));
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to retrieve the role type preference type constraints", e);
    }
  }

  /**
   * Retrieve the role type preference type constraints for a specific role type.
   *
   * @param roleType the code for the role type to retrieve the preference constraints for
   * @return the role type preference type constraints
   */
  @Override
  @Cacheable(cacheNames = "reference", key = "'roleTypePreferenceTypeConstraints.' + #roleType")
  public List<RoleTypePreferenceTypeConstraint> getRoleTypePreferenceTypeConstraints(
      String roleType) throws InvalidArgumentException, ServiceUnavailableException {
    if (!StringUtils.hasText(roleType)) {
      throw new InvalidArgumentException("roleType");
    }

    try {
      return roleTypePreferenceTypeConstraintRepository.findByRoleTypeIgnoreCase(
          roleType, Sort.by(Direction.ASC, "roleType", "preferenceType"));
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to retrieve the role type preference type constraints", e);
    }
  }

  /**
   * Retrieve the role type reference data for a specific locale.
   *
   * @param localeId the Unicode locale identifier for the locale to retrieve the role type
   *     reference data for
   * @return the role type reference data
   */
  @Override
  @Cacheable(cacheNames = "reference", key = "'roleTypes.' + #localeId")
  public List<RoleType> getRoleTypes(String localeId)
      throws InvalidArgumentException, ServiceUnavailableException {
    if (!StringUtils.hasText(localeId)) {
      throw new InvalidArgumentException("localeId");
    }

    try {
      return roleTypeRepository.findByLocaleIdIgnoreCase(localeId);
    } catch (Throwable e) {
      throw new ServiceUnavailableException("Failed to retrieve the role type reference data", e);
    }
  }

  /**
   * Retrieve the role type reference data for all locales.
   *
   * @return the role type reference data
   */
  @Override
  @Cacheable(cacheNames = "reference", key = "'roleTypes.ALL'")
  public List<RoleType> getRoleTypes() throws ServiceUnavailableException {
    try {
      return roleTypeRepository.findAll();
    } catch (Throwable e) {
      throw new ServiceUnavailableException("Failed to retrieve the role type reference data", e);
    }
  }

  /**
   * Retrieve the role type reference data for a specific tenant and locale.
   *
   * @param tenantId the Universally Unique Identifier (UUID) for the tenant the role type reference
   *     data is specific to
   * @param localeId the Unicode locale identifier for the locale to retrieve the role type
   *     reference data for
   * @return the role type reference data
   */
  @Override
  public List<RoleType> getRoleTypes(UUID tenantId, String localeId)
      throws InvalidArgumentException, ServiceUnavailableException {
    return self.getRoleTypes(localeId).stream()
        .filter(
            roleType ->
                (roleType.getTenantId() == null
                    || (Objects.equals(roleType.getTenantId(), tenantId))))
        .collect(Collectors.toList());
  }

  /**
   * Retrieve the segment reference data for a specific locale.
   *
   * @param localeId the Unicode locale identifier for the locale to retrieve the segment reference
   *     data for
   * @return the segment reference data
   */
  @Override
  @Cacheable(cacheNames = "reference", key = "'segments.' + #localeId")
  public List<Segment> getSegments(String localeId)
      throws InvalidArgumentException, ServiceUnavailableException {
    if (!StringUtils.hasText(localeId)) {
      throw new InvalidArgumentException("localeId");
    }

    try {
      return segmentRepository.findByLocaleIdIgnoreCase(localeId);
    } catch (Throwable e) {
      throw new ServiceUnavailableException("Failed to retrieve the segment reference data", e);
    }
  }

  /**
   * Retrieve the segment reference data for all locales.
   *
   * @return the segment reference data
   */
  @Override
  @Cacheable(cacheNames = "reference", key = "'segments.ALL'")
  public List<Segment> getSegments() throws ServiceUnavailableException {
    try {
      return segmentRepository.findAll();
    } catch (Throwable e) {
      throw new ServiceUnavailableException("Failed to retrieve the segment reference data", e);
    }
  }

  /**
   * Retrieve the segment reference data for a specific tenant and locale.
   *
   * @param tenantId the Universally Unique Identifier (UUID) for the tenant the segment reference
   *     data is specific to
   * @param localeId the Unicode locale identifier for the locale to retrieve the segment reference
   *     data for
   * @return the segment reference data
   */
  @Override
  public List<Segment> getSegments(UUID tenantId, String localeId)
      throws InvalidArgumentException, ServiceUnavailableException {
    return self.getSegments(localeId).stream()
        .filter(
            segment ->
                (segment.getTenantId() == null
                    || (Objects.equals(segment.getTenantId(), tenantId))))
        .collect(Collectors.toList());
  }

  /**
   * Retrieve the source of funds type reference data for a specific locale.
   *
   * @param localeId the Unicode locale identifier for the locale to retrieve the source of funds
   *     type reference data
   * @return the source of funds type reference data
   */
  @Override
  @Cacheable(cacheNames = "reference", key = "'sourceOfFundsTypes.' + #localeId")
  public List<SourceOfFundsType> getSourceOfFundsTypes(String localeId)
      throws InvalidArgumentException, ServiceUnavailableException {
    if (!StringUtils.hasText(localeId)) {
      throw new InvalidArgumentException("localeId");
    }

    try {
      return sourceOfFundsTypeRepository.findByLocaleIdIgnoreCase(localeId);
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to retrieve the source of funds type reference data", e);
    }
  }

  /**
   * Retrieve the source of funds type reference data for all locales.
   *
   * @return the source of funds type reference data
   */
  @Override
  @Cacheable(cacheNames = "reference", key = "'sourceOfFundsTypes.ALL'")
  public List<SourceOfFundsType> getSourceOfFundsTypes() throws ServiceUnavailableException {
    try {
      return sourceOfFundsTypeRepository.findAll();
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to retrieve the source of funds type reference data", e);
    }
  }

  /**
   * Retrieve the source of funds type reference data for a specific tenant and locale.
   *
   * @param tenantId the Universally Unique Identifier (UUID) for the tenant the source of funds
   *     type reference data is specific to
   * @param localeId the Unicode locale identifier for the locale to retrieve the source of funds
   *     type reference data
   * @return the source of funds type reference data
   */
  @Override
  public List<SourceOfFundsType> getSourceOfFundsTypes(UUID tenantId, String localeId)
      throws InvalidArgumentException, ServiceUnavailableException {
    return self.getSourceOfFundsTypes(localeId).stream()
        .filter(
            sourceOfFundsType ->
                (sourceOfFundsType.getTenantId() == null
                    || (Objects.equals(sourceOfFundsType.getTenantId(), tenantId))))
        .collect(Collectors.toList());
  }

  /**
   * Retrieve the source of wealth type reference data for a specific locale.
   *
   * @param localeId the Unicode locale identifier for the locale to retrieve the source of wealth
   *     type reference data for
   * @return the source of wealth type reference data
   */
  @Override
  @Cacheable(cacheNames = "reference", key = "'sourceOfWealthTypes.' + #localeId")
  public List<SourceOfWealthType> getSourceOfWealthTypes(String localeId)
      throws InvalidArgumentException, ServiceUnavailableException {
    if (!StringUtils.hasText(localeId)) {
      throw new InvalidArgumentException("localeId");
    }

    try {
      return sourceOfWealthTypeRepository.findByLocaleIdIgnoreCase(localeId);
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to retrieve the source of wealth type reference data", e);
    }
  }

  /**
   * Retrieve the source of wealth type reference data for all locales.
   *
   * @return the source of wealth type reference data
   */
  @Override
  @Cacheable(cacheNames = "reference", key = "'sourceOfWealthTypes.ALL'")
  public List<SourceOfWealthType> getSourceOfWealthTypes() throws ServiceUnavailableException {
    try {
      return sourceOfWealthTypeRepository.findAll();
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to retrieve the source of wealth type reference data", e);
    }
  }

  /**
   * Retrieve the source of wealth type reference data for a specific tenant and locale.
   *
   * @param tenantId the Universally Unique Identifier (UUID) for the tenant the source of wealth
   *     type reference data is specific to
   * @param localeId the Unicode locale identifier for the locale to retrieve the source of wealth
   *     type reference data for
   * @return the source of wealth type reference data
   */
  @Override
  public List<SourceOfWealthType> getSourceOfWealthTypes(UUID tenantId, String localeId)
      throws InvalidArgumentException, ServiceUnavailableException {
    return self.getSourceOfWealthTypes(localeId).stream()
        .filter(
            sourceOfWealthType ->
                (sourceOfWealthType.getTenantId() == null
                    || (Objects.equals(sourceOfWealthType.getTenantId(), tenantId))))
        .collect(Collectors.toList());
  }

  /**
   * Retrieve the status type category reference data for a specific locale.
   *
   * @param localeId the Unicode locale identifier for the locale to retrieve the status type
   *     category reference data for
   * @return the status type category reference data
   */
  @Override
  @Cacheable(cacheNames = "reference", key = "'statusTypeCategories.' + #localeId")
  public List<StatusTypeCategory> getStatusTypeCategories(String localeId)
      throws InvalidArgumentException, ServiceUnavailableException {
    if (!StringUtils.hasText(localeId)) {
      throw new InvalidArgumentException("localeId");
    }

    try {
      return statusTypeCategoryRepository.findByLocaleIdIgnoreCase(localeId);
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to retrieve the status type category reference data", e);
    }
  }

  /**
   * Retrieve the status type category reference data for all locales.
   *
   * @return the status type category reference data
   */
  @Override
  @Cacheable(cacheNames = "reference", key = "'statusTypeCategories.ALL'")
  public List<StatusTypeCategory> getStatusTypeCategories() throws ServiceUnavailableException {
    try {
      return statusTypeCategoryRepository.findAll();
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to retrieve the status type category reference data", e);
    }
  }

  /**
   * Retrieve the status type category reference data for a specific tenant and locale.
   *
   * @param tenantId the Universally Unique Identifier (UUID) for the tenant the status type
   *     category reference data is specific to
   * @param localeId the Unicode locale identifier for the locale to retrieve the status type
   *     category reference data for
   * @return the status type category reference data
   */
  @Override
  public List<StatusTypeCategory> getStatusTypeCategories(UUID tenantId, String localeId)
      throws InvalidArgumentException, ServiceUnavailableException {
    return self.getStatusTypeCategories(localeId).stream()
        .filter(
            statusTypeCategory ->
                (statusTypeCategory.getTenantId() == null
                    || (Objects.equals(statusTypeCategory.getTenantId(), tenantId))))
        .collect(Collectors.toList());
  }

  /**
   * Retrieve the status type reference data for a specific locale.
   *
   * @param localeId the Unicode locale identifier for the locale to retrieve the status type
   *     reference data for
   * @return the status type reference data
   */
  @Override
  @Cacheable(cacheNames = "reference", key = "'statusTypes.' + #localeId")
  public List<StatusType> getStatusTypes(String localeId)
      throws InvalidArgumentException, ServiceUnavailableException {
    if (!StringUtils.hasText(localeId)) {
      throw new InvalidArgumentException("localeId");
    }

    try {
      return statusTypeRepository.findByLocaleIdIgnoreCase(localeId);
    } catch (Throwable e) {
      throw new ServiceUnavailableException("Failed to retrieve the status type reference data", e);
    }
  }

  /**
   * Retrieve the status type reference data for all locales.
   *
   * @return the status type reference data
   */
  @Override
  @Cacheable(cacheNames = "reference", key = "'statusTypes.ALL'")
  public List<StatusType> getStatusTypes() throws ServiceUnavailableException {
    try {
      return statusTypeRepository.findAll();
    } catch (Throwable e) {
      throw new ServiceUnavailableException("Failed to retrieve the status type reference data", e);
    }
  }

  /**
   * Retrieve the status type reference data for a specific tenant and locale.
   *
   * @param tenantId the Universally Unique Identifier (UUID) for the tenant the status type
   *     reference data is specific to
   * @param localeId the Unicode locale identifier for the locale to retrieve the status type
   *     reference data for
   * @return the status type reference data
   */
  @Override
  public List<StatusType> getStatusTypes(UUID tenantId, String localeId)
      throws InvalidArgumentException, ServiceUnavailableException {
    return self.getStatusTypes(localeId).stream()
        .filter(
            statusType ->
                (statusType.getTenantId() == null
                    || (Objects.equals(statusType.getTenantId(), tenantId))))
        .collect(Collectors.toList());
  }

  /**
   * Retrieve the tax number type reference data for a specific locale.
   *
   * @param localeId the Unicode locale identifier for the locale to retrieve the tax number type
   *     reference data for
   * @return the tax number type reference data
   */
  @Override
  @Cacheable(cacheNames = "reference", key = "'taxNumberTypes.' + #localeId")
  public List<TaxNumberType> getTaxNumberTypes(String localeId)
      throws InvalidArgumentException, ServiceUnavailableException {
    if (!StringUtils.hasText(localeId)) {
      throw new InvalidArgumentException("localeId");
    }

    try {
      return taxNumberTypeRepository.findByLocaleIdIgnoreCase(localeId);
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to retrieve the tax number type reference data", e);
    }
  }

  /**
   * Retrieve the tax number type reference data for all locales.
   *
   * @return the tax number type reference data
   */
  @Override
  @Cacheable(cacheNames = "reference", key = "'taxNumberTypes.ALL'")
  public List<TaxNumberType> getTaxNumberTypes() throws ServiceUnavailableException {
    try {
      return taxNumberTypeRepository.findAll();
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to retrieve the tax number type reference data", e);
    }
  }

  /**
   * Retrieve the tax number type reference data for a specific tenant and locale.
   *
   * @param tenantId the Universally Unique Identifier (UUID) for the tenant the tax number type
   *     reference data is specific to
   * @param localeId the Unicode locale identifier for the locale to retrieve the tax number type
   *     reference data for
   * @return the tax number type reference data
   */
  @Override
  public List<TaxNumberType> getTaxNumberTypes(UUID tenantId, String localeId)
      throws InvalidArgumentException, ServiceUnavailableException {
    return self.getTaxNumberTypes(localeId).stream()
        .filter(
            taxNumberType ->
                (taxNumberType.getTenantId() == null
                    || (Objects.equals(taxNumberType.getTenantId(), tenantId))))
        .collect(Collectors.toList());
  }

  /**
   * Retrieve the times to contact reference data for a specific locale.
   *
   * @param localeId the Unicode locale identifier for the locale to retrieve the times to contact
   *     reference data for
   * @return the times to contact reference data
   */
  @Override
  @Cacheable(cacheNames = "reference", key = "'timesToContact.' + #localeId")
  public List<TimeToContact> getTimesToContact(String localeId)
      throws InvalidArgumentException, ServiceUnavailableException {
    if (!StringUtils.hasText(localeId)) {
      throw new InvalidArgumentException("localeId");
    }

    try {
      return timeToContactRepository.findByLocaleIdIgnoreCase(localeId);
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to retrieve the times to contact reference data", e);
    }
  }

  /**
   * Retrieve the times to contact reference data for all locales.
   *
   * @return the times to contact reference data
   */
  @Override
  @Cacheable(cacheNames = "reference", key = "'timesToContact.ALL'")
  public List<TimeToContact> getTimesToContact() throws ServiceUnavailableException {
    try {
      return timeToContactRepository.findAll();
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to retrieve the times to contact reference data", e);
    }
  }

  /**
   * Retrieve the times to contact reference data for a specific tenant and locale.
   *
   * @param tenantId the Universally Unique Identifier (UUID) for the tenant the times to contact
   *     reference data is specific to
   * @param localeId the Unicode locale identifier for the locale to retrieve the times to contact
   *     reference data for
   * @return the times to contact reference data
   */
  @Override
  public List<TimeToContact> getTimesToContact(UUID tenantId, String localeId)
      throws InvalidArgumentException, ServiceUnavailableException {
    return self.getTimesToContact(localeId).stream()
        .filter(
            timeToContact ->
                (timeToContact.getTenantId() == null
                    || (Objects.equals(timeToContact.getTenantId(), tenantId))))
        .collect(Collectors.toList());
  }

  /**
   * Retrieve the title reference data for a specific locale.
   *
   * @param localeId the Unicode locale identifier for the locale to retrieve the title reference
   *     data for
   * @return the title reference data
   */
  @Override
  @Cacheable(cacheNames = "reference", key = "'titles.' + #localeId")
  public List<Title> getTitles(String localeId)
      throws InvalidArgumentException, ServiceUnavailableException {
    if (!StringUtils.hasText(localeId)) {
      throw new InvalidArgumentException("localeId");
    }

    try {
      return titleRepository.findByLocaleIdIgnoreCase(localeId);
    } catch (Throwable e) {
      throw new ServiceUnavailableException("Failed to retrieve the title reference data", e);
    }
  }

  /**
   * Retrieve the title reference data for all locales.
   *
   * @return the title reference data
   */
  @Override
  @Cacheable(cacheNames = "reference", key = "'titles.ALL'")
  public List<Title> getTitles() throws ServiceUnavailableException {
    try {
      return titleRepository.findAll();
    } catch (Throwable e) {
      throw new ServiceUnavailableException("Failed to retrieve the title reference data", e);
    }
  }

  /**
   * Retrieve the title reference data for a specific tenant and locale.
   *
   * @param tenantId the Universally Unique Identifier (UUID) for the tenant the title reference
   *     data is specific to
   * @param localeId the Unicode locale identifier for the locale to retrieve the title reference
   *     data for
   * @return the title reference data
   */
  @Override
  public List<Title> getTitles(UUID tenantId, String localeId)
      throws InvalidArgumentException, ServiceUnavailableException {
    return self.getTitles(localeId).stream()
        .filter(
            title ->
                (title.getTenantId() == null || (Objects.equals(title.getTenantId(), tenantId))))
        .collect(Collectors.toList());
  }

  /**
   * Check whether the code is a valid code for an attribute type for the party type.
   *
   * @param tenantId the Universally Unique Identifier (UUID) for the tenant
   * @param partyTypeCode the party type code
   * @param attributeTypeCode the code for the attribute type
   * @return <b>true</b> if the code is a valid code for an attribute type or <b>false</b> otherwise
   */
  @Override
  public boolean isValidAttributeType(UUID tenantId, String partyTypeCode, String attributeTypeCode)
      throws ServiceUnavailableException {
    if (!StringUtils.hasText(attributeTypeCode)) {
      return false;
    }

    return self.getAttributeTypes().stream()
        .anyMatch(
            attributeType ->
                (attributeType.getTenantId() == null
                        || attributeType.getTenantId().equals(tenantId))
                    && Objects.equals(attributeType.getCode(), attributeTypeCode)
                    && attributeType.isValidForPartyType(partyTypeCode));
  }

  /**
   * Check whether the code is a valid code for an attribute type category.
   *
   * @param tenantId the Universally Unique Identifier (UUID) for the tenant
   * @param attributeTypeCategoryCode the code for the attribute type category
   * @return <b>true</b> if the code is a valid code for an attribute type category or <b>false</b>
   *     otherwise
   */
  @Override
  public boolean isValidAttributeTypeCategory(UUID tenantId, String attributeTypeCategoryCode)
      throws ServiceUnavailableException {
    if (!StringUtils.hasText(attributeTypeCategoryCode)) {
      return false;
    }

    return self.getAttributeTypeCategories().stream()
        .anyMatch(
            attributeTypeCategory ->
                (attributeTypeCategory.getTenantId() == null
                        || Objects.equals(attributeTypeCategory.getTenantId(), tenantId))
                    && Objects.equals(attributeTypeCategory.getCode(), attributeTypeCategoryCode));
  }

  /**
   * Check whether the code is a valid code for a consent type.
   *
   * @param tenantId the Universally Unique Identifier (UUID) for the tenant
   * @param consentTypeCode the code for the consent type
   * @return <b>true</b> if the code is a valid code for a consent type or <b>false</b> otherwise
   */
  @Override
  public boolean isValidConsentType(UUID tenantId, String consentTypeCode)
      throws ServiceUnavailableException {
    if (!StringUtils.hasText(consentTypeCode)) {
      return false;
    }

    return self.getConsentTypes().stream()
        .anyMatch(
            consentType ->
                (consentType.getTenantId() == null
                        || Objects.equals(consentType.getTenantId(), tenantId))
                    && Objects.equals(consentType.getCode(), consentTypeCode));
  }

  /**
   * Check whether the code is a valid code for a contact mechanism purpose for the party type.
   *
   * @param tenantId the Universally Unique Identifier (UUID) for the tenant
   * @param partyTypeCode the party type code
   * @param contactMechanismTypeCode the code for the contact mechanism type
   * @param contactMechanismPurposeCode the code for the contact mechanism purpose
   * @return <b>true</b> if the code is a valid code for a contact mechanism purpose or <b>false</b>
   *     otherwise
   */
  @Override
  public boolean isValidContactMechanismPurpose(
      UUID tenantId,
      String partyTypeCode,
      String contactMechanismTypeCode,
      String contactMechanismPurposeCode)
      throws ServiceUnavailableException {
    if (!StringUtils.hasText(contactMechanismPurposeCode)) {
      return false;
    }

    return self.getContactMechanismPurposes().stream()
        .anyMatch(
            contactMechanismPurpose ->
                (contactMechanismPurpose.getTenantId() == null
                        || Objects.equals(contactMechanismPurpose.getTenantId(), tenantId))
                    && contactMechanismPurpose.isValidForPartyType(partyTypeCode)
                    && contactMechanismPurpose.isValidForContactMechanismType(
                        contactMechanismTypeCode)
                    && Objects.equals(
                        contactMechanismPurpose.getCode(), contactMechanismPurposeCode));
  }

  /**
   * Check whether the code is a valid code for a contact mechanism role for the party type.
   *
   * @param tenantId the Universally Unique Identifier (UUID) for the tenant
   * @param partyTypeCode the party type code
   * @param contactMechanismTypeCode the code for the contact mechanism type
   * @param contactMechanismRoleCode the code for the contact mechanism role
   * @return <b>true</b> if the code is a valid code for a contact mechanism role or <b>false</b>
   *     otherwise
   */
  @Override
  public boolean isValidContactMechanismRole(
      UUID tenantId,
      String partyTypeCode,
      String contactMechanismTypeCode,
      String contactMechanismRoleCode)
      throws ServiceUnavailableException {
    if (!StringUtils.hasText(contactMechanismRoleCode)) {
      return false;
    }

    return self.getContactMechanismRoles().stream()
        .anyMatch(
            contactMechanismRole ->
                (contactMechanismRole.getTenantId() == null
                        || Objects.equals(contactMechanismRole.getTenantId(), tenantId))
                    && contactMechanismRole.isValidForPartyType(partyTypeCode)
                    && contactMechanismRole
                        .getContactMechanismType()
                        .equals(contactMechanismTypeCode)
                    && Objects.equals(contactMechanismRole.getCode(), contactMechanismRoleCode));
  }

  /**
   * Check whether the code is a valid code for a contact mechanism type.
   *
   * @param tenantId the Universally Unique Identifier (UUID) for the tenant
   * @param contactMechanismTypeCode the code for the contact mechanism type
   * @return <b>true</b> if the code is a valid code for a contact mechanism type or <b>false</b>
   *     otherwise
   */
  @Override
  public boolean isValidContactMechanismType(UUID tenantId, String contactMechanismTypeCode)
      throws ServiceUnavailableException {
    if (!StringUtils.hasText(contactMechanismTypeCode)) {
      return false;
    }

    return self.getContactMechanismTypes().stream()
        .anyMatch(
            contactMechanismType ->
                (contactMechanismType.getTenantId() == null
                        || Objects.equals(contactMechanismType.getTenantId(), tenantId))
                    && Objects.equals(contactMechanismType.getCode(), contactMechanismTypeCode));
  }

  /**
   * Check whether the code is a valid code for an employment status.
   *
   * @param tenantId the Universally Unique Identifier (UUID) for the tenant
   * @param employmentStatusCode the code for the employment status
   * @return <b>true</b> if the code is a valid code for an employment status or <b>false</b>
   *     otherwise
   */
  @Override
  public boolean isValidEmploymentStatus(UUID tenantId, String employmentStatusCode)
      throws ServiceUnavailableException {
    if (!StringUtils.hasText(employmentStatusCode)) {
      return false;
    }

    return self.getEmploymentStatuses().stream()
        .anyMatch(
            employmentStatus ->
                (employmentStatus.getTenantId() == null
                        || Objects.equals(employmentStatus.getTenantId(), tenantId))
                    && Objects.equals(employmentStatus.getCode(), employmentStatusCode));
  }

  /**
   * Check whether the code is a valid code for an employment type.
   *
   * @param tenantId the Universally Unique Identifier (UUID) for the tenant
   * @param employmentStatusCode the code for the employment status
   * @param employmentTypeCode the code for the employment type
   * @return <b>true</b> if the code is a valid code for an employment type or <b>false</b>
   *     otherwise
   */
  @Override
  public boolean isValidEmploymentType(
      UUID tenantId, String employmentStatusCode, String employmentTypeCode)
      throws ServiceUnavailableException {
    if (!StringUtils.hasText(employmentTypeCode)) {
      return false;
    }

    return self.getEmploymentTypes().stream()
        .anyMatch(
            employmentType ->
                (employmentType.getTenantId() == null
                        || Objects.equals(employmentType.getTenantId(), tenantId))
                    && Objects.equals(employmentType.getCode(), employmentTypeCode)
                    && Objects.equals(employmentType.getEmploymentStatus(), employmentStatusCode));
  }

  /**
   * Check whether the code is a valid code for an employment type.
   *
   * @param tenantId the Universally Unique Identifier (UUID) for the tenant
   * @param employmentTypeCode the code for the employment type
   * @return <b>true</b> if the code is a valid code for an employment type or <b>false</b>
   *     otherwise
   */
  @Override
  public boolean isValidEmploymentType(UUID tenantId, String employmentTypeCode)
      throws ServiceUnavailableException {
    if (!StringUtils.hasText(employmentTypeCode)) {
      return false;
    }

    return self.getEmploymentTypes().stream()
        .anyMatch(
            employmentType ->
                (employmentType.getTenantId() == null
                        || Objects.equals(employmentType.getTenantId(), tenantId))
                    && Objects.equals(employmentType.getCode(), employmentTypeCode));
  }

  /**
   * Check whether the code is a valid code for an external reference type for the party type
   *
   * @param tenantId the Universally Unique Identifier (UUID) for the tenant
   * @param partyTypeCode the party type code
   * @param externalReferenceTypeCode the code for the external reference type
   * @return <b>true</b> if the code is a valid code for an external reference type or <b>false</b>
   *     otherwise
   */
  @Override
  public boolean isValidExternalReferenceType(
      UUID tenantId, String partyTypeCode, String externalReferenceTypeCode)
      throws ServiceUnavailableException {
    if (!StringUtils.hasText(externalReferenceTypeCode)) {
      return false;
    }

    return self.getExternalReferenceTypes().stream()
        .anyMatch(
            externalReferenceType ->
                (externalReferenceType.getTenantId() == null
                        || Objects.equals(externalReferenceType.getTenantId(), tenantId))
                    && Objects.equals(externalReferenceType.getCode(), externalReferenceTypeCode)
                    && externalReferenceType.isValidForPartyType(partyTypeCode));
  }

  /**
   * Check whether the code is a valid code for a field of study.
   *
   * @param tenantId the Universally Unique Identifier (UUID) for the tenant
   * @param fieldOfStudyCode the code for the field of study
   * @return <b>true</b> if the code is a valid code for a field of study or <b>false</b> otherwise
   */
  @Override
  public boolean isValidFieldOfStudy(UUID tenantId, String fieldOfStudyCode)
      throws ServiceUnavailableException {
    if (!StringUtils.hasText(fieldOfStudyCode)) {
      return false;
    }

    return self.getFieldsOfStudy().stream()
        .anyMatch(
            fieldOfStudy ->
                (fieldOfStudy.getTenantId() == null
                        || Objects.equals(fieldOfStudy.getTenantId(), tenantId))
                    && Objects.equals(fieldOfStudy.getCode(), fieldOfStudyCode));
  }

  /**
   * Check whether the code is a valid code for a gender.
   *
   * @param tenantId the Universally Unique Identifier (UUID) for the tenant
   * @param genderCode the code for the gender
   * @return <b>true</b> if the code is a valid code for a gender or <b>false</b> otherwise
   */
  @Override
  public boolean isValidGender(UUID tenantId, String genderCode)
      throws ServiceUnavailableException {
    if (!StringUtils.hasText(genderCode)) {
      return false;
    }

    return self.getGenders().stream()
        .anyMatch(
            gender ->
                (gender.getTenantId() == null || Objects.equals(gender.getTenantId(), tenantId))
                    && Objects.equals(gender.getCode(), genderCode));
  }

  /**
   * Check whether the code is a valid code for an identity document type for the party type
   *
   * @param tenantId the Universally Unique Identifier (UUID) for the tenant
   * @param partyTypeCode the party type code
   * @param identityDocumentTypeCode the code for the identity document type
   * @return <b>true</b> if the code is a valid code for an identity document type or <b>false</b>
   *     otherwise
   */
  @Override
  public boolean isValidIdentityDocumentType(
      UUID tenantId, String partyTypeCode, String identityDocumentTypeCode)
      throws ServiceUnavailableException {
    if (!StringUtils.hasText(identityDocumentTypeCode)) {
      return false;
    }

    return self.getIdentityDocumentTypes().stream()
        .anyMatch(
            identityDocumentType ->
                (identityDocumentType.getTenantId() == null
                        || Objects.equals(identityDocumentType.getTenantId(), tenantId))
                    && Objects.equals(identityDocumentType.getCode(), identityDocumentTypeCode)
                    && identityDocumentType.isValidForPartyType(partyTypeCode));
  }

  /**
   * Check whether the code is a valid code for a lock type for the party type.
   *
   * @param tenantId the Universally Unique Identifier (UUID) for the tenant
   * @param partyTypeCode the party type code
   * @param lockTypeCode the code for the lock type
   * @return <b>true</b> if the code is a valid code for a lock type or <b>false</b> otherwise
   */
  @Override
  public boolean isValidLockType(UUID tenantId, String partyTypeCode, String lockTypeCode)
      throws ServiceUnavailableException {
    if (!StringUtils.hasText(lockTypeCode)) {
      return false;
    }

    return self.getLockTypes().stream()
        .anyMatch(
            lockType ->
                (lockType.getTenantId() == null || Objects.equals(lockType.getTenantId(), tenantId))
                    && Objects.equals(lockType.getCode(), lockTypeCode)
                    && lockType.isValidForPartyType(partyTypeCode));
  }

  /**
   * Check whether the code is a valid code for a lock type category.
   *
   * @param tenantId the Universally Unique Identifier (UUID) for the tenant
   * @param lockTypeCategoryCode the code for the lock type category
   * @return <b>true</b> if the code is a valid code for a lock type category or <b>false</b>
   *     otherwise
   */
  @Override
  public boolean isValidLockTypeCategory(UUID tenantId, String lockTypeCategoryCode)
      throws ServiceUnavailableException {
    if (!StringUtils.hasText(lockTypeCategoryCode)) {
      return false;
    }

    return self.getLockTypeCategories().stream()
        .anyMatch(
            lockTypeCategory ->
                (lockTypeCategory.getTenantId() == null
                        || Objects.equals(lockTypeCategory.getTenantId(), tenantId))
                    && Objects.equals(lockTypeCategory.getCode(), lockTypeCategoryCode));
  }

  /**
   * Check whether the code is a valid code for a marital status.
   *
   * @param tenantId the Universally Unique Identifier (UUID) for the tenant
   * @param maritalStatusCode the code for the marital status
   * @return <b>true</b> if the code is a valid code for a marital status or <b>false</b> otherwise
   */
  @Override
  public boolean isValidMaritalStatus(UUID tenantId, String maritalStatusCode)
      throws ServiceUnavailableException {
    if (!StringUtils.hasText(maritalStatusCode)) {
      return false;
    }

    return self.getMaritalStatuses().stream()
        .anyMatch(
            maritalStatus ->
                (maritalStatus.getTenantId() == null
                        || Objects.equals(maritalStatus.getTenantId(), tenantId))
                    && Objects.equals(maritalStatus.getCode(), maritalStatusCode));
  }

  /**
   * Check whether the code is a valid code for a marriage type.
   *
   * @param tenantId the Universally Unique Identifier (UUID) for the tenant
   * @param maritalStatusCode the code for the marital status
   * @param marriageTypeCode the code for the marriage type
   * @return <b>true</b> if the code is a valid code for a marriage type or <b>false</b> otherwise
   */
  @Override
  public boolean isValidMarriageType(
      UUID tenantId, String maritalStatusCode, String marriageTypeCode)
      throws ServiceUnavailableException {
    if (!StringUtils.hasText(maritalStatusCode)) {
      return true;
    }

    // Find marriage types for the specified marital status
    List<MarriageType> marriageTypes =
        self.getMarriageTypes().stream()
            .filter(
                marriageType ->
                    ((marriageType.getTenantId() == null)
                            && Objects.equals(marriageType.getMaritalStatus(), maritalStatusCode)
                        || (Objects.equals(marriageType.getTenantId(), tenantId))
                            && Objects.equals(marriageType.getMaritalStatus(), maritalStatusCode)))
            .collect(Collectors.toList());

    // If we have marriage types for the specified marital status then check if one matches
    if (marriageTypes.size() > 0) {
      return marriageTypes.stream()
          .anyMatch(
              marriageType ->
                  (marriageType.getTenantId() == null
                          || Objects.equals(marriageType.getTenantId(), tenantId))
                      && Objects.equals(marriageType.getCode(), marriageTypeCode)
                      && Objects.equals(marriageType.getMaritalStatus(), maritalStatusCode));
    } else {
      return true;
    }
  }

  /**
   * Check whether the measurement unit is valid for the attribute type with the specified code.
   *
   * @param tenantId the Universally Unique Identifier (UUID) for the tenant
   * @param attributeTypeCode the code for the attribute type
   * @param measurementUnit the measurement unit
   * @return <b>true</b> if the measurement unit is valid for the attribute type with the specified
   *     code or <b>false</b> otherwise
   */
  public boolean isValidMeasurementUnitForAttributeType(
      UUID tenantId, String attributeTypeCode, MeasurementUnit measurementUnit)
      throws ServiceUnavailableException {
    if (!StringUtils.hasText(attributeTypeCode)) {
      return false;
    }

    return self.getAttributeTypes().stream()
        .anyMatch(
            attributeType ->
                (attributeType.getTenantId() == null
                        || Objects.equals(attributeType.getTenantId(), tenantId))
                    && Objects.equals(attributeType.getCode(), attributeTypeCode)
                    && Objects.equals(
                        attributeType.getUnitType(),
                        measurementUnit == null ? null : measurementUnit.getType()));
  }

  /**
   * Check whether the code is a valid code for a next of kin type.
   *
   * @param tenantId the Universally Unique Identifier (UUID) for the tenant
   * @param nextOfKinTypeCode the code for the next of kin type
   * @return <b>true</b> if the code is a valid code for a next of kin type or <b>false</b>
   *     otherwise
   */
  @Override
  public boolean isValidNextOfKinType(UUID tenantId, String nextOfKinTypeCode)
      throws ServiceUnavailableException {
    if (!StringUtils.hasText(nextOfKinTypeCode)) {
      return false;
    }

    return self.getNextOfKinTypes().stream()
        .anyMatch(
            nextOfKinType ->
                (nextOfKinType.getTenantId() == null
                        || Objects.equals(nextOfKinType.getTenantId(), tenantId))
                    && Objects.equals(nextOfKinType.getCode(), nextOfKinTypeCode));
  }

  /**
   * Check whether the code is a valid code for an occupation.
   *
   * @param tenantId the Universally Unique Identifier (UUID) for the tenant
   * @param occupationCode the code for the occupation
   * @return <b>true</b> if the code is a valid code for an occupation or <b>false</b> otherwise
   */
  @Override
  public boolean isValidOccupation(UUID tenantId, String occupationCode)
      throws ServiceUnavailableException {
    if (!StringUtils.hasText(occupationCode)) {
      return false;
    }

    return self.getOccupations().stream()
        .anyMatch(
            occupation ->
                (occupation.getTenantId() == null
                        || Objects.equals(occupation.getTenantId(), tenantId))
                    && Objects.equals(occupation.getCode(), occupationCode));
  }

  /**
   * Check whether the code is a valid code for a physical address purpose for the party type.
   *
   * @param tenantId the Universally Unique Identifier (UUID) for the tenant
   * @param partyTypeCode the party type code
   * @param physicalAddressPurposeCode the code for the physical address purpose
   * @return <b>true</b> if the code is a valid code for a physical address purpose or <b>false</b>
   *     otherwise
   */
  @Override
  public boolean isValidPhysicalAddressPurpose(
      UUID tenantId, String partyTypeCode, String physicalAddressPurposeCode)
      throws ServiceUnavailableException {
    if (!StringUtils.hasText(physicalAddressPurposeCode)) {
      return false;
    }

    return self.getPhysicalAddressPurposes().stream()
        .anyMatch(
            physicalAddressPurpose ->
                (physicalAddressPurpose.getTenantId() == null
                        || Objects.equals(physicalAddressPurpose.getTenantId(), tenantId))
                    && Objects.equals(physicalAddressPurpose.getCode(), physicalAddressPurposeCode)
                    && physicalAddressPurpose.isValidForPartyType(partyTypeCode));
  }

  /**
   * Check whether the code is a valid code for a physical address purpose.
   *
   * @param tenantId the Universally Unique Identifier (UUID) for the tenant
   * @param physicalAddressPurposeCode the code for the physical address purpose
   * @return <b>true</b> if the code is a valid code for a physical address purpose or <b>false</b>
   *     otherwise
   */
  @Override
  public boolean isValidPhysicalAddressPurpose(UUID tenantId, String physicalAddressPurposeCode)
      throws ServiceUnavailableException {
    if (!StringUtils.hasText(physicalAddressPurposeCode)) {
      return false;
    }

    return self.getPhysicalAddressPurposes().stream()
        .anyMatch(
            physicalAddressPurpose ->
                (physicalAddressPurpose.getTenantId() == null
                        || Objects.equals(physicalAddressPurpose.getTenantId(), tenantId))
                    && Objects.equals(
                        physicalAddressPurpose.getCode(), physicalAddressPurposeCode));
  }

  /**
   * Check whether the code is a valid code for a physical address role for the party type.
   *
   * @param tenantId the Universally Unique Identifier (UUID) for the tenant
   * @param partyTypeCode the party type code
   * @param physicalAddressRoleCode the code for the physical address role
   * @return <b>true</b> if the code is a valid code for a physical address role or <b>false</b>
   *     otherwise
   */
  @Override
  public boolean isValidPhysicalAddressRole(
      UUID tenantId, String partyTypeCode, String physicalAddressRoleCode)
      throws ServiceUnavailableException {
    if (!StringUtils.hasText(physicalAddressRoleCode)) {
      return false;
    }

    return self.getPhysicalAddressRoles().stream()
        .anyMatch(
            physicalAddressRole ->
                (physicalAddressRole.getTenantId() == null
                        || Objects.equals(physicalAddressRole.getTenantId(), tenantId))
                    && Objects.equals(physicalAddressRole.getCode(), physicalAddressRoleCode)
                    && physicalAddressRole.isValidForPartyType(partyTypeCode));
  }

  /**
   * Check whether the code is a valid code for a physical address role.
   *
   * @param tenantId the Universally Unique Identifier (UUID) for the tenant
   * @param physicalAddressRoleCode the code for the physical address role
   * @return <b>true</b> if the code is a valid code for a physical address role or <b>false</b>
   *     otherwise
   */
  @Override
  public boolean isValidPhysicalAddressRole(UUID tenantId, String physicalAddressRoleCode)
      throws ServiceUnavailableException {
    if (!StringUtils.hasText(physicalAddressRoleCode)) {
      return false;
    }

    return self.getPhysicalAddressRoles().stream()
        .anyMatch(
            physicalAddressRole ->
                (physicalAddressRole.getTenantId() == null
                        || Objects.equals(physicalAddressRole.getTenantId(), tenantId))
                    && Objects.equals(physicalAddressRole.getCode(), physicalAddressRoleCode));
  }

  /**
   * Check whether the code is a valid code for a physical address type.
   *
   * @param tenantId the Universally Unique Identifier (UUID) for the tenant
   * @param physicalAddressTypeCode the code for the physical address type
   * @return <b>true</b> if the code is a valid code for a physical address type or <b>false</b>
   *     otherwise
   */
  @Override
  public boolean isValidPhysicalAddressType(UUID tenantId, String physicalAddressTypeCode)
      throws ServiceUnavailableException {
    if (!StringUtils.hasText(physicalAddressTypeCode)) {
      return false;
    }

    return self.getPhysicalAddressTypes().stream()
        .anyMatch(
            physicalAddressType ->
                (physicalAddressType.getTenantId() == null
                        || Objects.equals(physicalAddressType.getTenantId(), tenantId))
                    && Objects.equals(physicalAddressType.getCode(), physicalAddressTypeCode));
  }

  /**
   * Check whether the code is a valid code for a preference type for the party type.
   *
   * @param tenantId the Universally Unique Identifier (UUID) for the tenant
   * @param partyTypeCode the party type code
   * @param preferenceTypeCode the code for the preference type
   * @return <b>true</b> if the code is a valid code for a preference type or <b>false</b> otherwise
   */
  @Override
  public boolean isValidPreferenceType(
      UUID tenantId, String partyTypeCode, String preferenceTypeCode)
      throws ServiceUnavailableException {
    if (!StringUtils.hasText(preferenceTypeCode)) {
      return false;
    }

    return self.getPreferenceTypes().stream()
        .anyMatch(
            preferenceType ->
                (preferenceType.getTenantId() == null
                        || Objects.equals(preferenceType.getTenantId(), tenantId))
                    && Objects.equals(preferenceType.getCode(), preferenceTypeCode)
                    && preferenceType.isValidForPartyType(partyTypeCode));
  }

  /**
   * Check whether the code is a valid code for a preference type category.
   *
   * @param tenantId the Universally Unique Identifier (UUID) for the tenant
   * @param preferenceTypeCategoryCode the code for the preference type category
   * @return <b>true</b> if the code is a valid code for a preference type category or <b>false</b>
   *     otherwise
   */
  @Override
  public boolean isValidPreferenceTypeCategory(UUID tenantId, String preferenceTypeCategoryCode)
      throws ServiceUnavailableException {
    if (!StringUtils.hasText(preferenceTypeCategoryCode)) {
      return false;
    }

    return self.getPreferenceTypeCategories().stream()
        .anyMatch(
            preferenceTypeCategory ->
                (preferenceTypeCategory.getTenantId() == null
                        || Objects.equals(preferenceTypeCategory.getTenantId(), tenantId))
                    && Objects.equals(
                        preferenceTypeCategory.getCode(), preferenceTypeCategoryCode));
  }

  /**
   * Check whether the code is a valid code for a qualification type.
   *
   * @param tenantId the Universally Unique Identifier (UUID) for the tenant
   * @param qualificationTypeCode the code for the qualification type
   * @return <b>true</b> if the code is a valid code for a qualification type or <b>false</b>
   *     otherwise
   */
  @Override
  public boolean isValidQualificationType(UUID tenantId, String qualificationTypeCode)
      throws ServiceUnavailableException {
    if (!StringUtils.hasText(qualificationTypeCode)) {
      return false;
    }

    return self.getQualificationTypes().stream()
        .anyMatch(
            qualificationType ->
                (qualificationType.getTenantId() == null
                        || Objects.equals(qualificationType.getTenantId(), tenantId))
                    && Objects.equals(qualificationType.getCode(), qualificationTypeCode));
  }

  /**
   * Check whether the code is a valid code for a race.
   *
   * @param tenantId the Universally Unique Identifier (UUID) for the tenant
   * @param raceCode the code for the race
   * @return <b>true</b> if the code is a valid code for a race or <b>false</b> otherwise
   */
  @Override
  public boolean isValidRace(UUID tenantId, String raceCode) throws ServiceUnavailableException {
    if (!StringUtils.hasText(raceCode)) {
      return false;
    }

    return self.getRaces().stream()
        .anyMatch(
            race ->
                (race.getTenantId() == null || Objects.equals(race.getTenantId(), tenantId))
                    && Objects.equals(race.getCode(), raceCode));
  }

  /**
   * Check whether the code is a valid code for a relationship type for the party type.
   *
   * @param tenantId the Universally Unique Identifier (UUID) for the tenant
   * @param partyTypeCode the party type code
   * @param relationshipTypeCode the code for the relationship type
   * @return <b>true</b> if the code is a valid code for a relationship type or <b>false</b>
   *     otherwise
   */
  @Override
  public boolean isValidRelationshipType(
      UUID tenantId, String partyTypeCode, String relationshipTypeCode)
      throws ServiceUnavailableException {
    if (!StringUtils.hasText(relationshipTypeCode)) {
      return false;
    }

    return self.getRelationshipTypes().stream()
        .anyMatch(
            relationshipType ->
                (relationshipType.getTenantId() == null
                        || Objects.equals(relationshipType.getTenantId(), tenantId))
                    && Objects.equals(relationshipType.getCode(), relationshipTypeCode));
  }

  /**
   * Check whether the code is a valid code for a residence permit type.
   *
   * @param tenantId the Universally Unique Identifier (UUID) for the tenant
   * @param residencePermitTypeCode the code for the residence permit type
   * @return <b>true</b> if the code is a valid code for a residence permit type or <b>false</b>
   *     otherwise
   */
  @Override
  public boolean isValidResidencePermitType(UUID tenantId, String residencePermitTypeCode)
      throws ServiceUnavailableException {
    if (!StringUtils.hasText(residencePermitTypeCode)) {
      return false;
    }

    return self.getResidencePermitTypes().stream()
        .anyMatch(
            residencePermitType ->
                (residencePermitType.getTenantId() == null
                        || Objects.equals(residencePermitType.getTenantId(), tenantId))
                    && Objects.equals(residencePermitType.getCode(), residencePermitTypeCode));
  }

  /**
   * Check whether the code is a valid code for a residency status.
   *
   * @param tenantId the Universally Unique Identifier (UUID) for the tenant
   * @param residencyStatusCode the code for the residency status
   * @return <b>true</b> if the code is a valid code for a residency status or <b>false</b>
   *     otherwise
   */
  @Override
  public boolean isValidResidencyStatus(UUID tenantId, String residencyStatusCode)
      throws ServiceUnavailableException {
    if (!StringUtils.hasText(residencyStatusCode)) {
      return false;
    }

    return self.getResidencyStatuses().stream()
        .anyMatch(
            residencyStatus ->
                (residencyStatus.getTenantId() == null
                        || Objects.equals(residencyStatus.getTenantId(), tenantId))
                    && Objects.equals(residencyStatus.getCode(), residencyStatusCode));
  }

  /**
   * Check whether the code is a valid code for a residential type.
   *
   * @param tenantId the Universally Unique Identifier (UUID) for the tenant
   * @param residentialTypeCode the code for the residential type
   * @return <b>true</b> if the code is a valid code for a residential type or <b>false</b>
   *     otherwise
   */
  @Override
  public boolean isValidResidentialType(UUID tenantId, String residentialTypeCode)
      throws ServiceUnavailableException {
    if (!StringUtils.hasText(residentialTypeCode)) {
      return false;
    }

    return self.getResidentialTypes().stream()
        .anyMatch(
            residentialType ->
                (residentialType.getTenantId() == null
                        || Objects.equals(residentialType.getTenantId(), tenantId))
                    && Objects.equals(residentialType.getCode(), residentialTypeCode));
  }

  /**
   * Check whether the code is a valid code for a role purpose.
   *
   * @param tenantId the Universally Unique Identifier (UUID) for the tenant
   * @param rolePurposeCode the code for the role purpose
   * @return <b>true</b> if the code is a valid code for a role purpose or <b>false</b> otherwise
   */
  @Override
  public boolean isValidRolePurpose(UUID tenantId, String rolePurposeCode)
      throws ServiceUnavailableException {
    if (!StringUtils.hasText(rolePurposeCode)) {
      return false;
    }

    return self.getRolePurposes().stream()
        .anyMatch(
            rolePurpose ->
                (rolePurpose.getTenantId() == null
                        || (Objects.equals(rolePurpose.getTenantId(), tenantId)))
                    && Objects.equals(rolePurpose.getCode(), rolePurposeCode));
  }

  /**
   * Check whether the code is a valid code for a role type for the party type.
   *
   * @param tenantId the Universally Unique Identifier (UUID) for the tenant
   * @param partyTypeCode the party type code
   * @param roleTypeCode the code for the role type
   * @return <b>true</b> if the code is a valid code for a role type or <b>false</b> otherwise
   */
  @Override
  public boolean isValidRoleType(UUID tenantId, String partyTypeCode, String roleTypeCode)
      throws ServiceUnavailableException {
    if (!StringUtils.hasText(roleTypeCode)) {
      return false;
    }

    return self.getRoleTypes().stream()
        .anyMatch(
            roleType ->
                (roleType.getTenantId() == null || Objects.equals(roleType.getTenantId(), tenantId))
                    && Objects.equals(roleType.getCode(), roleTypeCode)
                    && roleType.isValidForPartyType(partyTypeCode));
  }

  /**
   * Check whether the code is a valid code for a segment.
   *
   * @param tenantId the Universally Unique Identifier (UUID) for the tenant
   * @param segmentCode the code for the segment
   * @return <b>true</b> if the code is a valid code for a segment or <b>false</b> otherwise
   */
  @Override
  public boolean isValidSegment(UUID tenantId, String segmentCode)
      throws ServiceUnavailableException {
    if (!StringUtils.hasText(segmentCode)) {
      return false;
    }

    return self.getSegments().stream()
        .anyMatch(
            segment ->
                (segment.getTenantId() == null || Objects.equals(segment.getTenantId(), tenantId))
                    && Objects.equals(segment.getCode(), segmentCode));
  }

  /**
   * Check whether the code is a valid code for a source of funds type.
   *
   * @param tenantId the Universally Unique Identifier (UUID) for the tenant
   * @param sourceOfFundsTypeCode the code for the source of funds type
   * @return <b>true</b> if the code is a valid code for a source of funds type or <b>false</b>
   *     otherwise
   */
  @Override
  public boolean isValidSourceOfFundsType(UUID tenantId, String sourceOfFundsTypeCode)
      throws ServiceUnavailableException {
    if (!StringUtils.hasText(sourceOfFundsTypeCode)) {
      return false;
    }

    return self.getSourceOfFundsTypes().stream()
        .anyMatch(
            sourceOfFunds ->
                (sourceOfFunds.getTenantId() == null
                        || Objects.equals(sourceOfFunds.getTenantId(), tenantId))
                    && Objects.equals(sourceOfFunds.getCode(), sourceOfFundsTypeCode));
  }

  /**
   * Check whether the code is a valid code for a source of wealth type.
   *
   * @param tenantId the Universally Unique Identifier (UUID) for the tenant
   * @param sourceOfWealthTypeCode the code for the source of wealth type
   * @return <b>true</b> if the code is a valid code for a source of wealth type or <b>false</b>
   *     otherwise
   */
  @Override
  public boolean isValidSourceOfWealthType(UUID tenantId, String sourceOfWealthTypeCode)
      throws ServiceUnavailableException {
    if (!StringUtils.hasText(sourceOfWealthTypeCode)) {
      return false;
    }

    return self.getSourceOfWealthTypes().stream()
        .anyMatch(
            sourceOfWealth ->
                (sourceOfWealth.getTenantId() == null
                        || Objects.equals(sourceOfWealth.getTenantId(), tenantId))
                    && Objects.equals(sourceOfWealth.getCode(), sourceOfWealthTypeCode));
  }

  /**
   * Check whether the code is a valid code for a status type for the party type.
   *
   * @param tenantId the Universally Unique Identifier (UUID) for the tenant
   * @param partyTypeCode the party type code
   * @param statusTypeCode the code for the status type
   * @return <b>true</b> if the code is a valid code for a status type or <b>false</b> otherwise
   */
  @Override
  public boolean isValidStatusType(UUID tenantId, String partyTypeCode, String statusTypeCode)
      throws ServiceUnavailableException {
    if (!StringUtils.hasText(statusTypeCode)) {
      return false;
    }

    return self.getStatusTypes().stream()
        .anyMatch(
            statusType ->
                (statusType.getTenantId() == null
                        || Objects.equals(statusType.getTenantId(), tenantId))
                    && Objects.equals(statusType.getCode(), statusTypeCode)
                    && statusType.isValidForPartyType(partyTypeCode));
  }

  /**
   * Check whether the code is a valid code for a status type category.
   *
   * @param tenantId the Universally Unique Identifier (UUID) for the tenant
   * @param statusTypeCategoryCode the code for the status type category
   * @return <b>true</b> if the code is a valid code for a status type category or <b>false</b>
   *     otherwise
   */
  @Override
  public boolean isValidStatusTypeCategory(UUID tenantId, String statusTypeCategoryCode)
      throws ServiceUnavailableException {
    if (!StringUtils.hasText(statusTypeCategoryCode)) {
      return false;
    }

    return self.getStatusTypeCategories().stream()
        .anyMatch(
            statusTypeCategory ->
                (statusTypeCategory.getTenantId() == null
                        || Objects.equals(statusTypeCategory.getTenantId(), tenantId))
                    && Objects.equals(statusTypeCategory.getCode(), statusTypeCategoryCode));
  }

  /**
   * Check whether the code is a valid code for a tax number type for the party type.
   *
   * @param tenantId the Universally Unique Identifier (UUID) for the tenant
   * @param partyTypeCode the party type code
   * @param taxNumberTypeCode the code for the tax number type
   * @return <b>true</b> if the code is a valid code for a tax number type or <b>false</b> otherwise
   */
  @Override
  public boolean isValidTaxNumberType(UUID tenantId, String partyTypeCode, String taxNumberTypeCode)
      throws ServiceUnavailableException {
    if (!StringUtils.hasText(taxNumberTypeCode)) {
      return false;
    }

    return self.getTaxNumberTypes().stream()
        .anyMatch(
            taxNumberType ->
                (taxNumberType.getTenantId() == null
                        || Objects.equals(taxNumberType.getTenantId(), tenantId))
                    && Objects.equals(taxNumberType.getCode(), taxNumberTypeCode)
                    && taxNumberType.isValidForPartyType(partyTypeCode));
  }

  /**
   * Check whether the code is a valid code for a time to contact.
   *
   * @param tenantId the Universally Unique Identifier (UUID) for the tenant
   * @param timeToContactCode the code for the time to contact
   * @return <b>true</b> if the code is a valid code for a time to contact or <b>false</b> otherwise
   */
  @Override
  public boolean isValidTimeToContact(UUID tenantId, String timeToContactCode)
      throws ServiceUnavailableException {
    if (!StringUtils.hasText(timeToContactCode)) {
      return false;
    }

    return self.getTimesToContact().stream()
        .anyMatch(
            timeToContact ->
                (timeToContact.getTenantId() == null
                        || Objects.equals(timeToContact.getTenantId(), tenantId))
                    && Objects.equals(timeToContact.getCode(), timeToContactCode));
  }

  /**
   * Check whether the code is a valid code for a title.
   *
   * @param tenantId the Universally Unique Identifier (UUID) for the tenant
   * @param titleCode the code for the title
   * @return <b>true</b> if the code is a valid code for a title or <b>false</b> otherwise
   */
  @Override
  public boolean isValidTitle(UUID tenantId, String titleCode) throws ServiceUnavailableException {
    if (!StringUtils.hasText(titleCode)) {
      return false;
    }

    return self.getTitles().stream()
        .anyMatch(
            title ->
                (title.getTenantId() == null || Objects.equals(title.getTenantId(), tenantId))
                    && Objects.equals(title.getCode(), titleCode));
  }
}
