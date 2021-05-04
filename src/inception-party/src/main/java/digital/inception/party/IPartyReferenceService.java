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
import java.util.Optional;
import java.util.UUID;

/**
 * The <b>IPartyReferenceService</b> interface defines the functionality provided by a Party
 * Reference Service implementation.
 *
 * @author Marcus Portmann
 */
public interface IPartyReferenceService {

  /** The default locale ID. */
  String DEFAULT_LOCALE_ID = "en-US";
  /** The Universally Unique Identifier (UUID) for the default tenant. */
  UUID DEFAULT_TENANT_ID = UUID.fromString("00000000-0000-0000-0000-000000000000");

  /**
   * Retrieve the attribute type category reference data for a specific locale.
   *
   * @param localeId the Unicode locale identifier for the locale to retrieve the attribute type
   *     category reference data for
   * @return the attribute type category reference data
   */
  List<AttributeTypeCategory> getAttributeTypeCategories(String localeId)
      throws InvalidArgumentException, ServiceUnavailableException;

  /**
   * Retrieve the attribute type category reference data for a specific tenant and locale.
   *
   * @param tenantId the Universally Unique Identifier (UUID) for the tenant the attribute type
   *     category reference data is specific to
   * @param localeId the Unicode locale identifier for the locale to retrieve the attribute type
   *     category reference data for
   * @return the attribute type category reference data
   */
  List<AttributeTypeCategory> getAttributeTypeCategories(UUID tenantId, String localeId)
      throws InvalidArgumentException, ServiceUnavailableException;

  /**
   * Retrieve the attribute type category reference data for all locales.
   *
   * @return the attribute type category reference data
   */
  List<AttributeTypeCategory> getAttributeTypeCategories() throws ServiceUnavailableException;

  /**
   * Retrieve the value type for the first attribute type with the specified code for any tenant or
   * locale.
   *
   * @param attributeTypeCode the code for the attribute type
   * @return the value type for the attribute type
   */
  Optional<ValueType> getAttributeTypeValueType(String attributeTypeCode)
      throws ServiceUnavailableException;

  /**
   * Retrieve the attribute type reference data for a specific locale.
   *
   * @param localeId the Unicode locale identifier for the locale to retrieve the attribute type
   *     reference data for
   * @return the attribute type reference data
   */
  List<AttributeType> getAttributeTypes(String localeId)
      throws InvalidArgumentException, ServiceUnavailableException;

  /**
   * Retrieve the attribute type reference data for a specific tenant and locale.
   *
   * @param tenantId the Universally Unique Identifier (UUID) for the tenant the attribute type
   *     reference data is specific to
   * @param localeId the Unicode locale identifier for the locale to retrieve the attribute type
   *     reference data for
   * @return the attribute type reference data
   */
  List<AttributeType> getAttributeTypes(UUID tenantId, String localeId)
      throws InvalidArgumentException, ServiceUnavailableException;

  /**
   * Retrieve the attribute type reference data for all locales.
   *
   * @return the attribute type reference data
   */
  List<AttributeType> getAttributeTypes() throws ServiceUnavailableException;

  /**
   * Retrieve the consent type reference data for a specific locale.
   *
   * @param localeId the Unicode locale identifier for the locale to retrieve the consent type
   *     reference data for
   * @return the consent type reference data
   */
  List<ConsentType> getConsentTypes(String localeId)
      throws InvalidArgumentException, ServiceUnavailableException;

  /**
   * Retrieve the consent type reference data for a specific tenant and locale.
   *
   * @param tenantId the Universally Unique Identifier (UUID) for the tenant the consent type
   *     reference data is specific to
   * @param localeId the Unicode locale identifier for the locale to retrieve the consent type
   *     reference data for
   * @return the consent type reference data
   */
  List<ConsentType> getConsentTypes(UUID tenantId, String localeId)
      throws InvalidArgumentException, ServiceUnavailableException;

  /**
   * Retrieve the consent type reference data for all locales.
   *
   * @return the consent type reference data
   */
  List<ConsentType> getConsentTypes() throws ServiceUnavailableException;

  /**
   * Retrieve the contact mechanism purpose reference data for a specific locale.
   *
   * @param localeId the Unicode locale identifier for the locale to retrieve the contact mechanism
   *     purpose reference data for
   * @return the contact mechanism purpose reference data
   */
  List<ContactMechanismPurpose> getContactMechanismPurposes(String localeId)
      throws InvalidArgumentException, ServiceUnavailableException;

  /**
   * Retrieve the contact mechanism purpose reference data for a specific tenant and locale.
   *
   * @param tenantId the Universally Unique Identifier (UUID) for the tenant the contact mechanism
   *     purpose reference data is specific to
   * @param localeId the Unicode locale identifier for the locale to retrieve the contact mechanism
   *     purpose reference data for
   * @return the contact mechanism purpose reference data
   */
  List<ContactMechanismPurpose> getContactMechanismPurposes(UUID tenantId, String localeId)
      throws InvalidArgumentException, ServiceUnavailableException;

  /**
   * Retrieve the contact mechanism purpose reference data for all locales.
   *
   * @return the contact mechanism purpose reference data
   */
  List<ContactMechanismPurpose> getContactMechanismPurposes() throws ServiceUnavailableException;

  /**
   * Retrieve the contact mechanism role reference data for a specific locale.
   *
   * @param localeId the Unicode locale identifier for the locale to retrieve the contact mechanism
   *     role reference data for
   * @return the contact mechanism role reference data
   */
  List<ContactMechanismRole> getContactMechanismRoles(String localeId)
      throws InvalidArgumentException, ServiceUnavailableException;

  /**
   * Retrieve the contact mechanism role reference data for a specific tenant and locale.
   *
   * @param tenantId the Universally Unique Identifier (UUID) for the tenant the contact mechanism
   *     role reference data is specific to
   * @param localeId the Unicode locale identifier for the locale to retrieve the contact mechanism
   *     role reference data for
   * @return the contact mechanism role reference data
   */
  List<ContactMechanismRole> getContactMechanismRoles(UUID tenantId, String localeId)
      throws InvalidArgumentException, ServiceUnavailableException;

  /**
   * Retrieve the contact mechanism role reference data for all locales.
   *
   * @return the contact mechanism role reference data
   */
  List<ContactMechanismRole> getContactMechanismRoles() throws ServiceUnavailableException;

  /**
   * Retrieve the contact mechanism type reference data for a specific locale.
   *
   * @param localeId the Unicode locale identifier for the locale to retrieve the contact mechanism
   *     type reference data for
   * @return the contact mechanism type reference data
   */
  List<ContactMechanismType> getContactMechanismTypes(String localeId)
      throws InvalidArgumentException, ServiceUnavailableException;

  /**
   * Retrieve the contact mechanism type reference data for a specific tenant and locale.
   *
   * @param tenantId the Universally Unique Identifier (UUID) for the tenant the contact mechanism
   *     type reference data is specific to
   * @param localeId the Unicode locale identifier for the locale to retrieve the contact mechanism
   *     type reference data for
   * @return the contact mechanism type reference data
   */
  List<ContactMechanismType> getContactMechanismTypes(UUID tenantId, String localeId)
      throws InvalidArgumentException, ServiceUnavailableException;

  /**
   * Retrieve the contact mechanism type reference data for all locales.
   *
   * @return the contact mechanism type reference data
   */
  List<ContactMechanismType> getContactMechanismTypes() throws ServiceUnavailableException;

  /**
   * Retrieve the employment status reference data for a specific locale.
   *
   * @param localeId the Unicode locale identifier for the locale to retrieve the employment status
   *     reference data for
   * @return the employment status reference data
   */
  List<EmploymentStatus> getEmploymentStatuses(String localeId)
      throws InvalidArgumentException, ServiceUnavailableException;

  /**
   * Retrieve the employment status reference data for a specific tenant and locale.
   *
   * @param tenantId the Universally Unique Identifier (UUID) for the tenant the employment status
   *     reference data is specific to
   * @param localeId the Unicode locale identifier for the locale to retrieve the employment status
   *     reference data for
   * @return the employment status reference data
   */
  List<EmploymentStatus> getEmploymentStatuses(UUID tenantId, String localeId)
      throws InvalidArgumentException, ServiceUnavailableException;

  /**
   * Retrieve the employment status reference data for all locales.
   *
   * @return the employment status reference data
   */
  List<EmploymentStatus> getEmploymentStatuses() throws ServiceUnavailableException;

  /**
   * Retrieve the employment type reference data for a specific locale.
   *
   * @param localeId the Unicode locale identifier for the locale to retrieve the employment type
   *     reference data for
   * @return the employment type reference data
   */
  List<EmploymentType> getEmploymentTypes(String localeId)
      throws InvalidArgumentException, ServiceUnavailableException;

  /**
   * Retrieve the employment type reference data for a specific tenant and locale.
   *
   * @param tenantId the Universally Unique Identifier (UUID) for the tenant the employment type
   *     reference data is specific to
   * @param localeId the Unicode locale identifier for the locale to retrieve the employment type
   *     reference data for
   * @return the employment type reference data
   */
  List<EmploymentType> getEmploymentTypes(UUID tenantId, String localeId)
      throws InvalidArgumentException, ServiceUnavailableException;

  /**
   * Retrieve the employment type reference data for all locales.
   *
   * @return the employment type reference data
   */
  List<EmploymentType> getEmploymentTypes() throws ServiceUnavailableException;

  /**
   * Retrieve the external reference type reference data for a specific locale.
   *
   * @param localeId the Unicode locale identifier for the locale to retrieve the external reference
   *     type reference data for
   * @return the external reference type reference data
   */
  List<ExternalReferenceType> getExternalReferenceTypes(String localeId)
      throws InvalidArgumentException, ServiceUnavailableException;

  /**
   * Retrieve the external reference type reference data for a specific tenant and locale.
   *
   * @param tenantId the Universally Unique Identifier (UUID) for the tenant the external reference
   *     type reference data is specific to
   * @param localeId the Unicode locale identifier for the locale to retrieve the external reference
   *     type reference data for
   * @return the external reference type reference data
   */
  List<ExternalReferenceType> getExternalReferenceTypes(UUID tenantId, String localeId)
      throws InvalidArgumentException, ServiceUnavailableException;

  /**
   * Retrieve the external reference type reference data for all locales.
   *
   * @return the external reference type reference data
   */
  List<ExternalReferenceType> getExternalReferenceTypes() throws ServiceUnavailableException;

  /**
   * Retrieve the fields of study reference data for a specific locale.
   *
   * @param localeId the Unicode locale identifier for the locale to retrieve the fields of study
   *     reference data for
   * @return the fields of study reference data
   */
  List<FieldOfStudy> getFieldsOfStudy(String localeId)
      throws InvalidArgumentException, ServiceUnavailableException;

  /**
   * Retrieve the fields of study reference data for a specific tenant and locale.
   *
   * @param tenantId the Universally Unique Identifier (UUID) for the tenant the fields of study
   *     reference data is specific to
   * @param localeId the Unicode locale identifier for the locale to retrieve the fields of study
   *     reference data for
   * @return the fields of study reference data
   */
  List<FieldOfStudy> getFieldsOfStudy(UUID tenantId, String localeId)
      throws InvalidArgumentException, ServiceUnavailableException;

  /**
   * Retrieve the fields of study reference data for all locales.
   *
   * @return the fields of study reference data
   */
  List<FieldOfStudy> getFieldsOfStudy() throws ServiceUnavailableException;

  /**
   * Retrieve the gender reference data for a specific locale.
   *
   * @param localeId the Unicode locale identifier for the locale to retrieve the gender reference
   *     data for
   * @return the gender reference data
   */
  List<Gender> getGenders(String localeId)
      throws InvalidArgumentException, ServiceUnavailableException;

  /**
   * Retrieve the gender reference data for a specific tenant and locale.
   *
   * @param tenantId the Universally Unique Identifier (UUID) for the tenant the gender reference
   *     data is specific to
   * @param localeId the Unicode locale identifier for the locale to retrieve the gender reference
   *     data for
   * @return the gender reference data
   */
  List<Gender> getGenders(UUID tenantId, String localeId)
      throws InvalidArgumentException, ServiceUnavailableException;

  /**
   * Retrieve the gender reference data for all locales.
   *
   * @return the gender reference data
   */
  List<Gender> getGenders() throws ServiceUnavailableException;

  /**
   * Retrieve the identity document type reference data for a specific locale.
   *
   * @param localeId the Unicode locale identifier for the locale to retrieve the identity document
   *     type reference data for
   * @return the identity document type reference data
   */
  List<IdentityDocumentType> getIdentityDocumentTypes(String localeId)
      throws InvalidArgumentException, ServiceUnavailableException;

  /**
   * Retrieve the identity document type reference data for a specific tenant and locale.
   *
   * @param tenantId the Universally Unique Identifier (UUID) for the tenant the identity document
   *     type reference data is specific to
   * @param localeId the Unicode locale identifier for the locale to retrieve the identity document
   *     type reference data for
   * @return the identity document type reference data
   */
  List<IdentityDocumentType> getIdentityDocumentTypes(UUID tenantId, String localeId)
      throws InvalidArgumentException, ServiceUnavailableException;

  /**
   * Retrieve the identity document type reference data for all locales.
   *
   * @return the identity document type reference data
   */
  List<IdentityDocumentType> getIdentityDocumentTypes() throws ServiceUnavailableException;

  /**
   * Retrieve the lock type category reference data for a specific locale.
   *
   * @param localeId the Unicode locale identifier for the locale to retrieve the lock type category
   *     reference data for
   * @return the lock type category reference data
   */
  List<LockTypeCategory> getLockTypeCategories(String localeId)
      throws InvalidArgumentException, ServiceUnavailableException;

  /**
   * Retrieve the lock type category reference data for a specific tenant and locale.
   *
   * @param tenantId the Universally Unique Identifier (UUID) for the tenant the lock type category
   *     reference data is specific to
   * @param localeId the Unicode locale identifier for the locale to retrieve the lock type category
   *     reference data for
   * @return the lock type category reference data
   */
  List<LockTypeCategory> getLockTypeCategories(UUID tenantId, String localeId)
      throws InvalidArgumentException, ServiceUnavailableException;

  /**
   * Retrieve the lock type category reference data for all locales.
   *
   * @return the lock type category reference data
   */
  List<LockTypeCategory> getLockTypeCategories() throws ServiceUnavailableException;

  /**
   * Retrieve the lock type reference data for a specific locale.
   *
   * @param localeId the Unicode locale identifier for the locale to retrieve the lock type
   *     reference data for
   * @return the lock type reference data
   */
  List<LockType> getLockTypes(String localeId)
      throws InvalidArgumentException, ServiceUnavailableException;

  /**
   * Retrieve the lock type reference data for a specific tenant and locale.
   *
   * @param tenantId the Universally Unique Identifier (UUID) for the tenant the lock type reference
   *     data is specific to
   * @param localeId the Unicode locale identifier for the locale to retrieve the lock type
   *     reference data for
   * @return the lock type reference data
   */
  List<LockType> getLockTypes(UUID tenantId, String localeId)
      throws InvalidArgumentException, ServiceUnavailableException;

  /**
   * Retrieve the lock type reference data for all locales.
   *
   * @return the lock type reference data
   */
  List<LockType> getLockTypes() throws ServiceUnavailableException;

  /**
   * Retrieve the marital status reference data for a specific locale.
   *
   * @param localeId the Unicode locale identifier for the locale to retrieve the marital status
   *     reference data for
   * @return the marital status reference data
   */
  List<MaritalStatus> getMaritalStatuses(String localeId)
      throws InvalidArgumentException, ServiceUnavailableException;

  /**
   * Retrieve the marital status reference data for a specific tenant and locale.
   *
   * @param tenantId the Universally Unique Identifier (UUID) for the tenant the marital status
   *     reference data is specific to
   * @param localeId the Unicode locale identifier for the locale to retrieve the marital status
   *     reference data for
   * @return the marital status reference data
   */
  List<MaritalStatus> getMaritalStatuses(UUID tenantId, String localeId)
      throws InvalidArgumentException, ServiceUnavailableException;

  /**
   * Retrieve the marital status reference data for all locales.
   *
   * @return the marital status reference data
   */
  List<MaritalStatus> getMaritalStatuses() throws ServiceUnavailableException;

  /**
   * Retrieve the marriage type reference data for a specific locale.
   *
   * @param localeId the Unicode locale identifier for the locale to retrieve the marriage type
   *     reference data
   * @return the marriage type reference data
   */
  List<MarriageType> getMarriageTypes(String localeId)
      throws InvalidArgumentException, ServiceUnavailableException;

  /**
   * Retrieve the marriage type reference data for a specific tenant and locale.
   *
   * @param tenantId the Universally Unique Identifier (UUID) for the tenant the marriage type
   *     reference data is specific to
   * @param localeId the Unicode locale identifier for the locale to retrieve the marriage type
   *     reference data
   * @return the marriage type reference data
   */
  List<MarriageType> getMarriageTypes(UUID tenantId, String localeId)
      throws InvalidArgumentException, ServiceUnavailableException;

  /**
   * Retrieve the marriage type reference data for all locales.
   *
   * @return the marriage type reference data
   */
  List<MarriageType> getMarriageTypes() throws ServiceUnavailableException;

  /**
   * Retrieve the next of kin type reference data for a specific locale.
   *
   * @param localeId the Unicode locale identifier for the locale to retrieve the next of kin type
   *     reference data for
   * @return the next of kin type reference data
   */
  List<NextOfKinType> getNextOfKinTypes(String localeId)
      throws InvalidArgumentException, ServiceUnavailableException;

  /**
   * Retrieve the next of kin type reference data for a specific tenant and locale.
   *
   * @param tenantId the Universally Unique Identifier (UUID) for the tenant the next of kin type
   *     reference data is specific to
   * @param localeId the Unicode locale identifier for the locale to retrieve the next of kin type
   *     reference data for
   * @return the next of kin type reference data
   */
  List<NextOfKinType> getNextOfKinTypes(UUID tenantId, String localeId)
      throws InvalidArgumentException, ServiceUnavailableException;

  /**
   * Retrieve the next of kin type reference data for all locales.
   *
   * @return the next of kin type reference data
   */
  List<NextOfKinType> getNextOfKinTypes() throws ServiceUnavailableException;

  /**
   * Retrieve the occupation reference data for a specific locale.
   *
   * @param localeId the Unicode locale identifier for the locale to retrieve the occupation
   *     reference data for
   * @return the occupation reference data
   */
  List<Occupation> getOccupations(String localeId)
      throws InvalidArgumentException, ServiceUnavailableException;

  /**
   * Retrieve the occupation reference data for a specific tenant and locale.
   *
   * @param tenantId the Universally Unique Identifier (UUID) for the tenant the occupation
   *     reference data is specific to
   * @param localeId the Unicode locale identifier for the locale to retrieve the occupation
   *     reference data for
   * @return the occupation reference data
   */
  List<Occupation> getOccupations(UUID tenantId, String localeId)
      throws InvalidArgumentException, ServiceUnavailableException;

  /**
   * Retrieve the occupation reference data for all locales.
   *
   * @return the occupation reference data
   */
  List<Occupation> getOccupations() throws ServiceUnavailableException;

  /**
   * Retrieve the physical address purpose reference data for a specific locale.
   *
   * @param localeId the Unicode locale identifier for the locale to retrieve the physical address
   *     purpose reference data for
   * @return the physical address purpose reference data
   */
  List<PhysicalAddressPurpose> getPhysicalAddressPurposes(String localeId)
      throws InvalidArgumentException, ServiceUnavailableException;

  /**
   * Retrieve the physical address purpose reference data for a specific tenant and locale.
   *
   * @param tenantId the Universally Unique Identifier (UUID) for the tenant the physical address
   *     purpose reference data is specific to
   * @param localeId the Unicode locale identifier for the locale to retrieve the physical address
   *     purpose reference data for
   * @return the physical address purpose reference data
   */
  List<PhysicalAddressPurpose> getPhysicalAddressPurposes(UUID tenantId, String localeId)
      throws InvalidArgumentException, ServiceUnavailableException;

  /**
   * Retrieve the physical address purpose reference data for all locales.
   *
   * @return the physical address purpose reference data
   */
  List<PhysicalAddressPurpose> getPhysicalAddressPurposes() throws ServiceUnavailableException;

  /**
   * Retrieve the physical address role reference data for a specific locale.
   *
   * @param localeId the Unicode locale identifier for the locale to retrieve the physical address
   *     role reference data for
   * @return the physical address role reference data
   */
  List<PhysicalAddressRole> getPhysicalAddressRoles(String localeId)
      throws InvalidArgumentException, ServiceUnavailableException;

  /**
   * Retrieve the physical address role reference data for a specific tenant and locale.
   *
   * @param tenantId the Universally Unique Identifier (UUID) for the tenant the physical address
   *     role reference data is specific to
   * @param localeId the Unicode locale identifier for the locale to retrieve the physical address
   *     role reference data for
   * @return the physical address role reference data
   */
  List<PhysicalAddressRole> getPhysicalAddressRoles(UUID tenantId, String localeId)
      throws InvalidArgumentException, ServiceUnavailableException;

  /**
   * Retrieve the physical address role reference data for all locales.
   *
   * @return the physical address role reference data
   */
  List<PhysicalAddressRole> getPhysicalAddressRoles() throws ServiceUnavailableException;

  /**
   * Retrieve the physical address type reference data for a specific locale.
   *
   * @param localeId the Unicode locale identifier for the locale to retrieve the physical address
   *     type reference data for
   * @return the physical address type reference data
   */
  List<PhysicalAddressType> getPhysicalAddressTypes(String localeId)
      throws InvalidArgumentException, ServiceUnavailableException;

  /**
   * Retrieve the physical address type reference data for a specific tenant and locale.
   *
   * @param tenantId the Universally Unique Identifier (UUID) for the tenant the physical address
   *     type reference data is specific to
   * @param localeId the Unicode locale identifier for the locale to retrieve the physical address
   *     type reference data for
   * @return the physical address type reference data
   */
  List<PhysicalAddressType> getPhysicalAddressTypes(UUID tenantId, String localeId)
      throws InvalidArgumentException, ServiceUnavailableException;

  /**
   * Retrieve the physical address type reference data for all locales.
   *
   * @return the physical address type reference data
   */
  List<PhysicalAddressType> getPhysicalAddressTypes() throws ServiceUnavailableException;

  /**
   * Retrieve the preference type category reference data for a specific locale.
   *
   * @param localeId the Unicode locale identifier for the locale to retrieve the preference type
   *     category reference data for
   * @return the preference type category reference data
   */
  List<PreferenceTypeCategory> getPreferenceTypeCategories(String localeId)
      throws InvalidArgumentException, ServiceUnavailableException;

  /**
   * Retrieve the preference type category reference data for a specific tenant and locale.
   *
   * @param tenantId the Universally Unique Identifier (UUID) for the tenant the preference type
   *     category reference data is specific to
   * @param localeId the Unicode locale identifier for the locale to retrieve the preference type
   *     category reference data for
   * @return the preference type category reference data
   */
  List<PreferenceTypeCategory> getPreferenceTypeCategories(UUID tenantId, String localeId)
      throws InvalidArgumentException, ServiceUnavailableException;

  /**
   * Retrieve the preference type category reference data for all locales.
   *
   * @return the preference type category reference data
   */
  List<PreferenceTypeCategory> getPreferenceTypeCategories() throws ServiceUnavailableException;

  /**
   * Retrieve the preference type reference data for a specific locale.
   *
   * @param localeId the Unicode locale identifier for the locale to retrieve the preference type
   *     reference data for
   * @return the preference type reference data
   */
  List<PreferenceType> getPreferenceTypes(String localeId)
      throws InvalidArgumentException, ServiceUnavailableException;

  /**
   * Retrieve the preference type reference data for a specific tenant and locale.
   *
   * @param tenantId the Universally Unique Identifier (UUID) for the tenant the preference type
   *     reference data is specific to
   * @param localeId the Unicode locale identifier for the locale to retrieve the preference type
   *     reference data for
   * @return the preference type reference data
   */
  List<PreferenceType> getPreferenceTypes(UUID tenantId, String localeId)
      throws InvalidArgumentException, ServiceUnavailableException;

  /**
   * Retrieve the preference type reference data for all locales.
   *
   * @return the preference type reference data
   */
  List<PreferenceType> getPreferenceTypes() throws ServiceUnavailableException;

  /**
   * Retrieve the qualification type reference data for a specific locale.
   *
   * @param localeId the Unicode locale identifier for the locale to retrieve the qualification type
   *     reference data for
   * @return the qualification type reference data
   */
  List<QualificationType> getQualificationTypes(String localeId)
      throws InvalidArgumentException, ServiceUnavailableException;

  /**
   * Retrieve the qualification type reference data for a specific tenant and locale.
   *
   * @param tenantId the Universally Unique Identifier (UUID) for the tenant the qualification type
   *     reference data is specific to
   * @param localeId the Unicode locale identifier for the locale to retrieve the qualification type
   *     reference data for
   * @return the qualification type reference data
   */
  List<QualificationType> getQualificationTypes(UUID tenantId, String localeId)
      throws InvalidArgumentException, ServiceUnavailableException;

  /**
   * Retrieve the qualification type reference data for all locales.
   *
   * @return the qualification type reference data
   */
  List<QualificationType> getQualificationTypes() throws ServiceUnavailableException;

  /**
   * Retrieve the race reference data for a specific locale.
   *
   * @param localeId the Unicode locale identifier for the locale to retrieve the race reference
   *     data for
   * @return the race reference data
   */
  List<Race> getRaces(String localeId) throws InvalidArgumentException, ServiceUnavailableException;

  /**
   * Retrieve the race reference data for a specific tenant and locale.
   *
   * @param tenantId the Universally Unique Identifier (UUID) for the tenant the race reference data
   *     is specific to
   * @param localeId the Unicode locale identifier for the locale to retrieve the race reference
   *     data for
   * @return the race reference data
   */
  List<Race> getRaces(UUID tenantId, String localeId)
      throws InvalidArgumentException, ServiceUnavailableException;

  /**
   * Retrieve the race reference data for all locales.
   *
   * @return the race reference data
   */
  List<Race> getRaces() throws ServiceUnavailableException;

  /**
   * Retrieve the value type for the first relationship property type with the specified code for
   * any tenant or locale.
   *
   * @param relationshipPropertyTypeCode the code for the relationship property type
   * @return the value type for the relationship property type
   */
  Optional<ValueType> getRelationshipPropertyTypeValueType(String relationshipPropertyTypeCode)
      throws ServiceUnavailableException;

  /**
   * Retrieve the relationship property type reference data for a specific locale.
   *
   * @param localeId the Unicode locale identifier for the locale to retrieve the relationship
   *     property type reference data for
   * @return the relationship property type reference data
   */
  List<RelationshipPropertyType> getRelationshipPropertyTypes(String localeId)
      throws InvalidArgumentException, ServiceUnavailableException;

  /**
   * Retrieve the relationship property type reference data for a specific tenant and locale.
   *
   * @param tenantId the Universally Unique Identifier (UUID) for the tenant the relationship
   *     property type reference data is specific to
   * @param localeId the Unicode locale identifier for the locale to retrieve the relationship
   *     property type reference data for
   * @return the relationship property type reference data
   */
  List<RelationshipPropertyType> getRelationshipPropertyTypes(UUID tenantId, String localeId)
      throws InvalidArgumentException, ServiceUnavailableException;

  /**
   * Retrieve the relationship property type reference data for all locales.
   *
   * @return the relationship property type reference data
   */
  List<RelationshipPropertyType> getRelationshipPropertyTypes() throws ServiceUnavailableException;

  /**
   * Retrieve the relationship type reference data for a specific locale.
   *
   * @param localeId the Unicode locale identifier for the locale to retrieve the relationship type
   *     reference data for
   * @return the relationship type reference data
   */
  List<RelationshipType> getRelationshipTypes(String localeId)
      throws InvalidArgumentException, ServiceUnavailableException;

  /**
   * Retrieve the relationship type reference data for a specific tenant and locale.
   *
   * @param tenantId the Universally Unique Identifier (UUID) for the tenant the relationship type
   *     reference data is specific to
   * @param localeId the Unicode locale identifier for the locale to retrieve the relationship type
   *     reference data for
   * @return the relationship type reference data
   */
  List<RelationshipType> getRelationshipTypes(UUID tenantId, String localeId)
      throws InvalidArgumentException, ServiceUnavailableException;

  /**
   * Retrieve the relationship type reference data for all locales.
   *
   * @return the relationship type reference data
   */
  List<RelationshipType> getRelationshipTypes() throws ServiceUnavailableException;

  /**
   * Retrieve the residence permit type reference data for a specific locale.
   *
   * @param localeId the Unicode locale identifier for the locale to retrieve the residence permit
   *     type reference data for
   * @return the residence permit type reference data
   */
  List<ResidencePermitType> getResidencePermitTypes(String localeId)
      throws InvalidArgumentException, ServiceUnavailableException;

  /**
   * Retrieve the residence permit type reference data for a specific tenant and locale.
   *
   * @param tenantId the Universally Unique Identifier (UUID) for the tenant the residence permit
   *     type reference data is specific to
   * @param localeId the Unicode locale identifier for the locale to retrieve the residence permit
   *     type reference data for
   * @return the residence permit type reference data
   */
  List<ResidencePermitType> getResidencePermitTypes(UUID tenantId, String localeId)
      throws InvalidArgumentException, ServiceUnavailableException;

  /**
   * Retrieve the residence permit type reference data for all locales.
   *
   * @return the residence permit type reference data
   */
  List<ResidencePermitType> getResidencePermitTypes() throws ServiceUnavailableException;

  /**
   * Retrieve the residency status reference data for a specific locale.
   *
   * @param localeId the Unicode locale identifier for the locale to retrieve the residency status
   *     reference data for
   * @return the residency status reference data
   */
  List<ResidencyStatus> getResidencyStatuses(String localeId)
      throws InvalidArgumentException, ServiceUnavailableException;

  /**
   * Retrieve the residency status reference data for a specific tenant and locale.
   *
   * @param tenantId the Universally Unique Identifier (UUID) for the tenant the residency status
   *     reference data is specific to
   * @param localeId the Unicode locale identifier for the locale to retrieve the residency status
   *     reference data for
   * @return the residency status reference data
   */
  List<ResidencyStatus> getResidencyStatuses(UUID tenantId, String localeId)
      throws InvalidArgumentException, ServiceUnavailableException;

  /**
   * Retrieve the residency status reference data for all locales.
   *
   * @return the residency status reference data
   */
  List<ResidencyStatus> getResidencyStatuses() throws ServiceUnavailableException;

  /**
   * Retrieve the residential type reference data for a specific locale.
   *
   * @param localeId the Unicode locale identifier for the locale to retrieve the residential type
   *     reference data for
   * @return the residential type reference data
   */
  List<ResidentialType> getResidentialTypes(String localeId)
      throws InvalidArgumentException, ServiceUnavailableException;

  /**
   * Retrieve the residential type reference data for a specific tenant and locale.
   *
   * @param tenantId the Universally Unique Identifier (UUID) for the tenant the residential type
   *     reference data is specific to
   * @param localeId the Unicode locale identifier for the locale to retrieve the residential type
   *     reference data for
   * @return the residential type reference data
   */
  List<ResidentialType> getResidentialTypes(UUID tenantId, String localeId)
      throws InvalidArgumentException, ServiceUnavailableException;

  /**
   * Retrieve the residential type reference data for all locales.
   *
   * @return the residential type reference data
   */
  List<ResidentialType> getResidentialTypes() throws ServiceUnavailableException;

  /**
   * Retrieve the role purpose reference data for a specific locale.
   *
   * @param localeId the Unicode locale identifier for the locale to retrieve the role purpose
   *     reference data for
   * @return the role purpose reference data
   */
  List<RolePurpose> getRolePurposes(String localeId)
      throws InvalidArgumentException, ServiceUnavailableException;

  /**
   * Retrieve the role purpose reference data for a specific tenant and locale.
   *
   * @param tenantId the Universally Unique Identifier (UUID) for the tenant the role purpose
   *     reference data is specific to
   * @param localeId the Unicode locale identifier for the locale to retrieve the role purpose
   *     reference data for
   * @return the role purpose reference data
   */
  List<RolePurpose> getRolePurposes(UUID tenantId, String localeId)
      throws InvalidArgumentException, ServiceUnavailableException;

  /**
   * Retrieve the role purpose reference data for all locales.
   *
   * @return the role purpose reference data
   */
  List<RolePurpose> getRolePurposes() throws ServiceUnavailableException;

  /**
   * Retrieve the role type attribute type constraints for all role types.
   *
   * @return the role type attribute type constraints
   */
  List<RoleTypeAttributeTypeConstraint> getRoleTypeAttributeTypeConstraints()
      throws ServiceUnavailableException;

  /**
   * Retrieve the role type attribute type constraints for a specific role type.
   *
   * @param roleType the code for the role type to retrieve the attribute constraints for
   * @return the role type attribute type constraints
   */
  List<RoleTypeAttributeTypeConstraint> getRoleTypeAttributeTypeConstraints(String roleType)
      throws InvalidArgumentException, ServiceUnavailableException;

  /**
   * Retrieve the role type preference type constraints for all role types.
   *
   * @return the role type preference type constraints
   */
  List<RoleTypePreferenceTypeConstraint> getRoleTypePreferenceTypeConstraints()
      throws ServiceUnavailableException;

  /**
   * Retrieve the role type preference type constraints for a specific role type.
   *
   * @param roleType the code for the role type to retrieve the preference constraints for
   * @return the role type preference type constraints
   */
  List<RoleTypePreferenceTypeConstraint> getRoleTypePreferenceTypeConstraints(String roleType)
      throws InvalidArgumentException, ServiceUnavailableException;

  /**
   * Retrieve the role type reference data for a specific locale.
   *
   * @param localeId the Unicode locale identifier for the locale to retrieve the role type
   *     reference data for
   * @return the role type reference data
   */
  List<RoleType> getRoleTypes(String localeId)
      throws InvalidArgumentException, ServiceUnavailableException;

  /**
   * Retrieve the role type reference data for a specific tenant and locale.
   *
   * @param tenantId the Universally Unique Identifier (UUID) for the tenant the role type reference
   *     data is specific to
   * @param localeId the Unicode locale identifier for the locale to retrieve the role type
   *     reference data for
   * @return the role type reference data
   */
  List<RoleType> getRoleTypes(UUID tenantId, String localeId)
      throws InvalidArgumentException, ServiceUnavailableException;

  /**
   * Retrieve the role type reference data for all locales.
   *
   * @return the role type reference data
   */
  List<RoleType> getRoleTypes() throws ServiceUnavailableException;

  /**
   * Retrieve the segment reference data for a specific locale.
   *
   * @param localeId the Unicode locale identifier for the locale to retrieve the segment reference
   *     data for
   * @return the segment reference data
   */
  List<Segment> getSegments(String localeId)
      throws InvalidArgumentException, ServiceUnavailableException;

  /**
   * Retrieve the segment reference data for a specific tenant and locale.
   *
   * @param tenantId the Universally Unique Identifier (UUID) for the tenant the segment reference
   *     data is specific to
   * @param localeId the Unicode locale identifier for the locale to retrieve the segment reference
   *     data for
   * @return the segment reference data
   */
  List<Segment> getSegments(UUID tenantId, String localeId)
      throws InvalidArgumentException, ServiceUnavailableException;

  /**
   * Retrieve the segment reference data for all locales.
   *
   * @return the segment reference data
   */
  List<Segment> getSegments() throws ServiceUnavailableException;

  /**
   * Retrieve the source of funds type reference data for a specific locale.
   *
   * @param localeId the Unicode locale identifier for the locale to retrieve the source of funds
   *     type reference data
   * @return the source of funds type reference data
   */
  List<SourceOfFundsType> getSourceOfFundsTypes(String localeId)
      throws InvalidArgumentException, ServiceUnavailableException;

  /**
   * Retrieve the source of funds type reference data for a specific tenant and locale.
   *
   * @param tenantId the Universally Unique Identifier (UUID) for the tenant the source of funds
   *     type reference data is specific to
   * @param localeId the Unicode locale identifier for the locale to retrieve the source of funds
   *     type reference data
   * @return the source of funds type reference data
   */
  List<SourceOfFundsType> getSourceOfFundsTypes(UUID tenantId, String localeId)
      throws InvalidArgumentException, ServiceUnavailableException;

  /**
   * Retrieve the source of funds type reference data for all locales.
   *
   * @return the source of funds type reference data
   */
  List<SourceOfFundsType> getSourceOfFundsTypes() throws ServiceUnavailableException;

  /**
   * Retrieve the source of wealth type reference data for a specific locale.
   *
   * @param localeId the Unicode locale identifier for the locale to retrieve the source of wealth
   *     type reference data for
   * @return the source of wealth type reference data
   */
  List<SourceOfWealthType> getSourceOfWealthTypes(String localeId)
      throws InvalidArgumentException, ServiceUnavailableException;

  /**
   * Retrieve the source of wealth type reference data for a specific tenant and locale.
   *
   * @param tenantId the Universally Unique Identifier (UUID) for the tenant the source of wealth
   *     type reference data is specific to
   * @param localeId the Unicode locale identifier for the locale to retrieve the source of wealth
   *     type reference data for
   * @return the source of wealth type reference data
   */
  List<SourceOfWealthType> getSourceOfWealthTypes(UUID tenantId, String localeId)
      throws InvalidArgumentException, ServiceUnavailableException;

  /**
   * Retrieve the source of wealth type reference data for all locales.
   *
   * @return the source of wealth type reference data
   */
  List<SourceOfWealthType> getSourceOfWealthTypes() throws ServiceUnavailableException;

  /**
   * Retrieve the status type category reference data for a specific locale.
   *
   * @param localeId the Unicode locale identifier for the locale to retrieve the status type
   *     category reference data for
   * @return the status type category reference data
   */
  List<StatusTypeCategory> getStatusTypeCategories(String localeId)
      throws InvalidArgumentException, ServiceUnavailableException;

  /**
   * Retrieve the status type category reference data for a specific tenant and locale.
   *
   * @param tenantId the Universally Unique Identifier (UUID) for the tenant the status type
   *     category reference data is specific to
   * @param localeId the Unicode locale identifier for the locale to retrieve the status type
   *     category reference data for
   * @return the status type category reference data
   */
  List<StatusTypeCategory> getStatusTypeCategories(UUID tenantId, String localeId)
      throws InvalidArgumentException, ServiceUnavailableException;

  /**
   * Retrieve the status type category reference data for all locales.
   *
   * @return the status type category reference data
   */
  List<StatusTypeCategory> getStatusTypeCategories() throws ServiceUnavailableException;

  /**
   * Retrieve the status type reference data for a specific locale.
   *
   * @param localeId the Unicode locale identifier for the locale to retrieve the status type
   *     reference data for
   * @return the status type reference data
   */
  List<StatusType> getStatusTypes(String localeId)
      throws InvalidArgumentException, ServiceUnavailableException;

  /**
   * Retrieve the status type reference data for a specific tenant and locale.
   *
   * @param tenantId the Universally Unique Identifier (UUID) for the tenant the status type
   *     reference data is specific to
   * @param localeId the Unicode locale identifier for the locale to retrieve the status type
   *     reference data for
   * @return the status type reference data
   */
  List<StatusType> getStatusTypes(UUID tenantId, String localeId)
      throws InvalidArgumentException, ServiceUnavailableException;

  /**
   * Retrieve the status type reference data for all locales.
   *
   * @return the status type reference data
   */
  List<StatusType> getStatusTypes() throws ServiceUnavailableException;

  /**
   * Retrieve the tax number type reference data for a specific locale.
   *
   * @param localeId the Unicode locale identifier for the locale to retrieve the tax number type
   *     reference data for
   * @return the tax number type reference data
   */
  List<TaxNumberType> getTaxNumberTypes(String localeId)
      throws InvalidArgumentException, ServiceUnavailableException;

  /**
   * Retrieve the tax number type reference data for a specific tenant and locale.
   *
   * @param tenantId the Universally Unique Identifier (UUID) for the tenant the tax number type
   *     reference data is specific to
   * @param localeId the Unicode locale identifier for the locale to retrieve the tax number type
   *     reference data for
   * @return the tax number type reference data
   */
  List<TaxNumberType> getTaxNumberTypes(UUID tenantId, String localeId)
      throws InvalidArgumentException, ServiceUnavailableException;

  /**
   * Retrieve the tax number type reference data for all locales.
   *
   * @return the tax number type reference data
   */
  List<TaxNumberType> getTaxNumberTypes() throws ServiceUnavailableException;

  /**
   * Retrieve the times to contact reference data for a specific locale.
   *
   * @param localeId the Unicode locale identifier for the locale to retrieve the times to contact
   *     reference data for
   * @return the times to contact reference data
   */
  List<TimeToContact> getTimesToContact(String localeId)
      throws InvalidArgumentException, ServiceUnavailableException;

  /**
   * Retrieve the times to contact reference data for a specific tenant and locale.
   *
   * @param tenantId the Universally Unique Identifier (UUID) for the tenant the times to contact
   *     reference data is specific to
   * @param localeId the Unicode locale identifier for the locale to retrieve the times to contact
   *     reference data for
   * @return the times to contact reference data
   */
  List<TimeToContact> getTimesToContact(UUID tenantId, String localeId)
      throws InvalidArgumentException, ServiceUnavailableException;

  /**
   * Retrieve the times to contact reference data for all locales.
   *
   * @return the times to contact reference data
   */
  List<TimeToContact> getTimesToContact() throws ServiceUnavailableException;

  /**
   * Retrieve the title reference data for a specific locale.
   *
   * @param localeId the Unicode locale identifier for the locale to retrieve the title reference
   *     data for
   * @return the title reference data
   */
  List<Title> getTitles(String localeId)
      throws InvalidArgumentException, ServiceUnavailableException;

  /**
   * Retrieve the title reference data for a specific tenant and locale.
   *
   * @param tenantId the Universally Unique Identifier (UUID) for the tenant the title reference
   *     data is specific to
   * @param localeId the Unicode locale identifier for the locale to retrieve the title reference
   *     data for
   * @return the title reference data
   */
  List<Title> getTitles(UUID tenantId, String localeId)
      throws InvalidArgumentException, ServiceUnavailableException;

  /**
   * Retrieve the title reference data for all locales.
   *
   * @return the title reference data
   */
  List<Title> getTitles() throws ServiceUnavailableException;

  /**
   * Check whether the code is a valid code for an attribute type for the party type.
   *
   * @param tenantId the Universally Unique Identifier (UUID) for the tenant
   * @param partyTypeCode the code for the party type
   * @param attributeTypeCode the code for the attribute type
   * @return <b>true</b> if the code is a valid code for an attribute type or <b>false</b> otherwise
   */
  boolean isValidAttributeType(UUID tenantId, String partyTypeCode, String attributeTypeCode)
      throws ServiceUnavailableException;

  /**
   * Check whether the code is a valid code for an attribute type category.
   *
   * @param tenantId the Universally Unique Identifier (UUID) for the tenant
   * @param attributeTypeCategoryCode the code for the attribute type category
   * @return <b>true</b> if the code is a valid code for an attribute type category or <b>false</b>
   *     otherwise
   */
  boolean isValidAttributeTypeCategory(UUID tenantId, String attributeTypeCategoryCode)
      throws ServiceUnavailableException;

  /**
   * Check whether the code is a valid code for a consent type.
   *
   * @param tenantId the Universally Unique Identifier (UUID) for the tenant
   * @param consentTypeCode the code for the consent type
   * @return <b>true</b> if the code is a valid code for a consent type or <b>false</b> otherwise
   */
  boolean isValidConsentType(UUID tenantId, String consentTypeCode)
      throws ServiceUnavailableException;

  /**
   * Check whether the code is a valid code for a contact mechanism purpose for the party type.
   *
   * @param tenantId the Universally Unique Identifier (UUID) for the tenant
   * @param partyTypeCode the code for the party type
   * @param contactMechanismTypeCode the code for the contact mechanism type
   * @param contactMechanismPurposeCode the code for the contact mechanism purpose
   * @return <b>true</b> if the code is a valid code for a contact mechanism purpose or <b>false</b>
   *     otherwise
   */
  boolean isValidContactMechanismPurpose(
      UUID tenantId,
      String partyTypeCode,
      String contactMechanismTypeCode,
      String contactMechanismPurposeCode)
      throws ServiceUnavailableException;

  /**
   * Check whether the code is a valid code for a contact mechanism role for the party type.
   *
   * @param tenantId the Universally Unique Identifier (UUID) for the tenant
   * @param partyTypeCode the code for the party type
   * @param contactMechanismTypeCode the code for the contact mechanism type
   * @param contactMechanismRoleCode the code for the contact mechanism role
   * @return <b>true</b> if the code is a valid code for a contact mechanism role or <b>false</b>
   *     otherwise
   */
  boolean isValidContactMechanismRole(
      UUID tenantId,
      String partyTypeCode,
      String contactMechanismTypeCode,
      String contactMechanismRoleCode)
      throws ServiceUnavailableException;

  /**
   * Check whether the code is a valid code for a contact mechanism type.
   *
   * @param tenantId the Universally Unique Identifier (UUID) for the tenant
   * @param contactMechanismTypeCode the code for the contact mechanism type
   * @return <b>true</b> if the code is a valid code for a contact mechanism type or <b>false</b>
   *     otherwise
   */
  boolean isValidContactMechanismType(UUID tenantId, String contactMechanismTypeCode)
      throws ServiceUnavailableException;

  /**
   * Check whether the code is a valid code for an employment status.
   *
   * @param tenantId the Universally Unique Identifier (UUID) for the tenant
   * @param employmentStatusCode the code for the employment status
   * @return <b>true</b> if the code is a valid code for an employment status or <b>false</b>
   *     otherwise
   */
  boolean isValidEmploymentStatus(UUID tenantId, String employmentStatusCode)
      throws ServiceUnavailableException;

  /**
   * Check whether the code is a valid code for an employment type.
   *
   * @param tenantId the Universally Unique Identifier (UUID) for the tenant
   * @param employmentStatusCode the code for the employment status
   * @param employmentTypeCode the code for the employment type
   * @return <b>true</b> if the code is a valid code for an employment type or <b>false</b>
   *     otherwise
   */
  boolean isValidEmploymentType(
      UUID tenantId, String employmentStatusCode, String employmentTypeCode)
      throws ServiceUnavailableException;

  /**
   * Check whether the code is a valid code for an employment type.
   *
   * @param tenantId the Universally Unique Identifier (UUID) for the tenant
   * @param employmentTypeCode the code for the employment type
   * @return <b>true</b> if the code is a valid code for an employment type or <b>false</b>
   *     otherwise
   */
  boolean isValidEmploymentType(UUID tenantId, String employmentTypeCode)
      throws ServiceUnavailableException;

  /**
   * Check whether the code is a valid code for an external reference type for the party type.
   *
   * @param tenantId the Universally Unique Identifier (UUID) for the tenant
   * @param partyTypeCode the code for the party type
   * @param externalReferenceTypeCode the code for the external reference type
   * @return <b>true</b> if the code is a valid code for an external reference type or <b>false</b>
   *     otherwise
   */
  boolean isValidExternalReferenceType(
      UUID tenantId, String partyTypeCode, String externalReferenceTypeCode)
      throws ServiceUnavailableException;

  /**
   * Check whether the code is a valid code for a field of study.
   *
   * @param tenantId the Universally Unique Identifier (UUID) for the tenant
   * @param fieldOfStudyCode the code for the field of study
   * @return <b>true</b> if the code is a valid code for a field of study or <b>false</b> otherwise
   */
  boolean isValidFieldOfStudy(UUID tenantId, String fieldOfStudyCode)
      throws ServiceUnavailableException;

  /**
   * Check whether the code is a valid code for a gender.
   *
   * @param tenantId the Universally Unique Identifier (UUID) for the tenant
   * @param genderCode the code for the gender
   * @return <b>true</b> if the code is a valid code for a gender or <b>false</b> otherwise
   */
  boolean isValidGender(UUID tenantId, String genderCode) throws ServiceUnavailableException;

  /**
   * Check whether the code is a valid code for an identity document type for the party type.
   *
   * @param tenantId the Universally Unique Identifier (UUID) for the tenant
   * @param partyTypeCode the code for the party type
   * @param identityDocumentTypeCode the code for the identity document type
   * @return <b>true</b> if the code is a valid code for an identity document type or <b>false</b>
   *     otherwise
   */
  boolean isValidIdentityDocumentType(
      UUID tenantId, String partyTypeCode, String identityDocumentTypeCode)
      throws ServiceUnavailableException;

  /**
   * Check whether the code is a valid code for a lock type for the party type.
   *
   * @param tenantId the Universally Unique Identifier (UUID) for the tenant
   * @param partyTypeCode the code for the party type
   * @param lockTypeCode the code for the lock type
   * @return <b>true</b> if the code is a valid code for a lock type or <b>false</b> otherwise
   */
  boolean isValidLockType(UUID tenantId, String partyTypeCode, String lockTypeCode)
      throws ServiceUnavailableException;

  /**
   * Check whether the code is a valid code for a lock type category.
   *
   * @param tenantId the Universally Unique Identifier (UUID) for the tenant
   * @param lockTypeCategoryCode the code for the lock type category
   * @return <b>true</b> if the code is a valid code for a lock type category or <b>false</b>
   *     otherwise
   */
  boolean isValidLockTypeCategory(UUID tenantId, String lockTypeCategoryCode)
      throws ServiceUnavailableException;

  /**
   * Check whether the code is a valid code for a marital status.
   *
   * @param tenantId the Universally Unique Identifier (UUID) for the tenant
   * @param maritalStatusCode the code for the marital status
   * @return <b>true</b> if the code is a valid code for a marital status or <b>false</b> otherwise
   */
  boolean isValidMaritalStatus(UUID tenantId, String maritalStatusCode)
      throws ServiceUnavailableException;

  /**
   * Check whether the code is a valid code for a marriage type.
   *
   * @param tenantId the Universally Unique Identifier (UUID) for the tenant
   * @param maritalStatusCode the code for the marital status
   * @param marriageTypeCode the code for the marriage type
   * @return <b>true</b> if the code is a valid code for a marriage type or <b>false</b> otherwise
   */
  boolean isValidMarriageType(UUID tenantId, String maritalStatusCode, String marriageTypeCode)
      throws ServiceUnavailableException;

  /**
   * Check whether the measurement unit is valid for the attribute type with the specified code.
   *
   * @param tenantId the Universally Unique Identifier (UUID) for the tenant
   * @param attributeTypeCode the code for the attribute type
   * @param measurementUnit the measurement unit
   * @return <b>true</b> if the measurement unit is valid for the attribute type with the specified
   *     code or <b>false</b> otherwise
   */
  boolean isValidMeasurementUnitForAttributeType(
      UUID tenantId, String attributeTypeCode, MeasurementUnit measurementUnit)
      throws ServiceUnavailableException;

  /**
   * Check whether the code is a valid code for a next of kin type.
   *
   * @param tenantId the Universally Unique Identifier (UUID) for the tenant
   * @param nextOfKinTypeCode the code for the next of kin type
   * @return <b>true</b> if the code is a valid code for a next of kin type or <b>false</b>
   *     otherwise
   */
  boolean isValidNextOfKinType(UUID tenantId, String nextOfKinTypeCode)
      throws ServiceUnavailableException;

  /**
   * Check whether the code is a valid code for an occupation.
   *
   * @param tenantId the Universally Unique Identifier (UUID) for the tenant
   * @param occupationCode the code for the occupation
   * @return <b>true</b> if the code is a valid code for an occupation or <b>false</b> otherwise
   */
  boolean isValidOccupation(UUID tenantId, String occupationCode)
      throws ServiceUnavailableException;

  /**
   * Check whether the code is a valid code for a physical address purpose for the party type.
   *
   * @param tenantId the Universally Unique Identifier (UUID) for the tenant
   * @param partyTypeCode the code for the party type
   * @param physicalAddressPurposeCode the code for the physical address purpose
   * @return <b>true</b> if the code is a valid code for a physical address purpose or <b>false</b>
   *     otherwise
   */
  boolean isValidPhysicalAddressPurpose(
      UUID tenantId, String partyTypeCode, String physicalAddressPurposeCode)
      throws ServiceUnavailableException;

  /**
   * Check whether the code is a valid code for a physical address purpose.
   *
   * @param tenantId the Universally Unique Identifier (UUID) for the tenant
   * @param physicalAddressPurposeCode the code for the physical address purpose
   * @return <b>true</b> if the code is a valid code for a physical address purpose or <b>false</b>
   *     otherwise
   */
  boolean isValidPhysicalAddressPurpose(UUID tenantId, String physicalAddressPurposeCode)
      throws ServiceUnavailableException;

  /**
   * Check whether the code is a valid code for a physical address role for the party type.
   *
   * @param tenantId the Universally Unique Identifier (UUID) for the tenant
   * @param partyTypeCode the code for the party type
   * @param physicalAddressRoleCode the code for the physical address role
   * @return <b>true</b> if the code is a valid code for a physical address role or <b>false</b>
   *     otherwise
   */
  boolean isValidPhysicalAddressRole(
      UUID tenantId, String partyTypeCode, String physicalAddressRoleCode)
      throws ServiceUnavailableException;

  /**
   * Check whether the code is a valid code for a physical address role for the party type.
   *
   * @param tenantId the Universally Unique Identifier (UUID) for the tenant
   * @param physicalAddressRoleCode the code for the physical address role
   * @return <b>true</b> if the code is a valid code for a physical address role or <b>false</b>
   *     otherwise
   */
  boolean isValidPhysicalAddressRole(UUID tenantId, String physicalAddressRoleCode)
      throws ServiceUnavailableException;

  /**
   * Check whether the code is a valid code for a physical address type.
   *
   * @param tenantId the Universally Unique Identifier (UUID) for the tenant
   * @param physicalAddressTypeCode the code for the physical address type
   * @return <b>true</b> if the code is a valid code for a physical address type or <b>false</b>
   *     otherwise
   */
  boolean isValidPhysicalAddressType(UUID tenantId, String physicalAddressTypeCode)
      throws ServiceUnavailableException;

  /**
   * Check whether the code is a valid code for a preference type for the party type.
   *
   * @param tenantId the Universally Unique Identifier (UUID) for the tenant
   * @param partyTypeCode the code for the party type
   * @param preferenceTypeCode the code for the preference type
   * @return <b>true</b> if the code is a valid code for a preference type or <b>false</b> otherwise
   */
  boolean isValidPreferenceType(UUID tenantId, String partyTypeCode, String preferenceTypeCode)
      throws ServiceUnavailableException;

  /**
   * Check whether the code is a valid code for a preference type category.
   *
   * @param tenantId the Universally Unique Identifier (UUID) for the tenant
   * @param preferenceTypeCategoryCode the code for the preference type category
   * @return <b>true</b> if the code is a valid code for a preference type category or <b>false</b>
   *     otherwise
   */
  boolean isValidPreferenceTypeCategory(UUID tenantId, String preferenceTypeCategoryCode)
      throws ServiceUnavailableException;

  /**
   * Check whether the code is a valid code for a qualification type.
   *
   * @param tenantId the Universally Unique Identifier (UUID) for the tenant
   * @param qualificationTypeCode the code for the qualification type
   * @return <b>true</b> if the code is a valid code for a qualification type or <b>false</b>
   *     otherwise
   */
  boolean isValidQualificationType(UUID tenantId, String qualificationTypeCode)
      throws ServiceUnavailableException;

  /**
   * Check whether the code is a valid code for a race.
   *
   * @param tenantId the Universally Unique Identifier (UUID) for the tenant
   * @param raceCode the code for the race
   * @return <b>true</b> if the code is a valid code for a race or <b>false</b> otherwise
   */
  boolean isValidRace(UUID tenantId, String raceCode) throws ServiceUnavailableException;

  /**
   * Check whether the code is a valid code for a relationship property type for the relationship
   * type.
   *
   * @param tenantId the Universally Unique Identifier (UUID) for the tenant
   * @param relationshipTypeCode the code for the relationship type
   * @param relationshipPropertyTypeCode the code for the relationship property type
   * @return <b>true</b> if the code is a valid code for a relationship property type or
   *     <b>false</b> otherwise
   */
  boolean isValidRelationshipPropertyType(
      UUID tenantId, String relationshipTypeCode, String relationshipPropertyTypeCode)
      throws ServiceUnavailableException;

  /**
   * Check whether the code is a valid code for a relationship type for the party type.
   *
   * @param tenantId the Universally Unique Identifier (UUID) for the tenant
   * @param partyTypeCode the code for the party type
   * @param relationshipTypeCode the code for the relationship type
   * @return <b>true</b> if the code is a valid code for a relationship type or <b>false</b>
   *     otherwise
   */
  boolean isValidRelationshipType(UUID tenantId, String partyTypeCode, String relationshipTypeCode)
      throws ServiceUnavailableException;

  /**
   * Check whether the code is a valid code for a residence permit type.
   *
   * @param tenantId the Universally Unique Identifier (UUID) for the tenant
   * @param residencePermitTypeCode the code for the residence permit type
   * @return <b>true</b> if the code is a valid code for a residence permit type or <b>false</b>
   *     otherwise
   */
  boolean isValidResidencePermitType(UUID tenantId, String residencePermitTypeCode)
      throws ServiceUnavailableException;

  /**
   * Check whether the code is a valid code for a residency status.
   *
   * @param tenantId the Universally Unique Identifier (UUID) for the tenant
   * @param residencyStatusCode the code for the residency status
   * @return <b>true</b> if the code is a valid code for a residency status or <b>false</b>
   *     otherwise
   */
  boolean isValidResidencyStatus(UUID tenantId, String residencyStatusCode)
      throws ServiceUnavailableException;

  /**
   * Check whether the code is a valid code for a residential type.
   *
   * @param tenantId the Universally Unique Identifier (UUID) for the tenant
   * @param residentialTypeCode the code for the residential type
   * @return <b>true</b> if the code is a valid code for a residential type or <b>false</b>
   *     otherwise
   */
  boolean isValidResidentialType(UUID tenantId, String residentialTypeCode)
      throws ServiceUnavailableException;

  /**
   * Check whether the code is a valid code for a role purpose.
   *
   * @param tenantId the Universally Unique Identifier (UUID) for the tenant
   * @param rolePurposeCode the code for the role purpose
   * @return <b>true</b> if the code is a valid code for a role purpose or <b>false</b> otherwise
   */
  boolean isValidRolePurpose(UUID tenantId, String rolePurposeCode)
      throws ServiceUnavailableException;

  /**
   * Check whether the code is a valid code for a role type for the party type.
   *
   * @param tenantId the Universally Unique Identifier (UUID) for the tenant
   * @param partyTypeCode the code for the party type
   * @param roleTypeCode the code for the role type
   * @return <b>true</b> if the code is a valid code for a role type or <b>false</b> otherwise
   */
  boolean isValidRoleType(UUID tenantId, String partyTypeCode, String roleTypeCode)
      throws ServiceUnavailableException;

  /**
   * Check whether the code is a valid code for a segment.
   *
   * @param tenantId the Universally Unique Identifier (UUID) for the tenant
   * @param segmentCode the code for the segment
   * @return <b>true</b> if the code is a valid code for a segment or <b>false</b> otherwise
   */
  boolean isValidSegment(UUID tenantId, String segmentCode) throws ServiceUnavailableException;

  /**
   * Check whether the code is a valid code for a source of funds type.
   *
   * @param tenantId the Universally Unique Identifier (UUID) for the tenant
   * @param sourceOfFundsTypeCode the code for the source of funds type
   * @return <b>true</b> if the code is a valid code for a source of funds type or <b>false</b>
   *     otherwise
   */
  boolean isValidSourceOfFundsType(UUID tenantId, String sourceOfFundsTypeCode)
      throws ServiceUnavailableException;

  /**
   * Check whether the code is a valid code for a source of wealth type.
   *
   * @param tenantId the Universally Unique Identifier (UUID) for the tenant
   * @param sourceOfWealthTypeCode the code for the source of wealth type
   * @return <b>true</b> if the code is a valid code for a source of wealth type or <b>false</b>
   *     otherwise
   */
  boolean isValidSourceOfWealthType(UUID tenantId, String sourceOfWealthTypeCode)
      throws ServiceUnavailableException;

  /**
   * Check whether the code is a valid code for a status type for the party type.
   *
   * @param tenantId the Universally Unique Identifier (UUID) for the tenant
   * @param partyTypeCode the code for the party type
   * @param statusTypeCode the code for the status type
   * @return <b>true</b> if the code is a valid code for a status type or <b>false</b> otherwise
   */
  boolean isValidStatusType(UUID tenantId, String partyTypeCode, String statusTypeCode)
      throws ServiceUnavailableException;

  /**
   * Check whether the code is a valid code for a status type category.
   *
   * @param tenantId the Universally Unique Identifier (UUID) for the tenant
   * @param statusTypeCategoryCode the code for the status type category
   * @return <b>true</b> if the code is a valid code for a status type category or <b>false</b>
   *     otherwise
   */
  boolean isValidStatusTypeCategory(UUID tenantId, String statusTypeCategoryCode)
      throws ServiceUnavailableException;

  /**
   * Check whether the code is a valid code for a tax number type.
   *
   * @param tenantId the Universally Unique Identifier (UUID) for the tenant
   * @param partyTypeCode the code for the party type
   * @param taxNumberTypeCode the code for the tax number type
   * @return <b>true</b> if the code is a valid code for a tax number type or <b>false</b> otherwise
   */
  boolean isValidTaxNumberType(UUID tenantId, String partyTypeCode, String taxNumberTypeCode)
      throws ServiceUnavailableException;

  /**
   * Check whether the code is a valid code for a time to contact.
   *
   * @param tenantId the Universally Unique Identifier (UUID) for the tenant
   * @param timeToContactCode the code for the time to contact
   * @return <b>true</b> if the code is a valid code for a time to contact or <b>false</b> otherwise
   */
  boolean isValidTimeToContact(UUID tenantId, String timeToContactCode)
      throws ServiceUnavailableException;

  /**
   * Check whether the code is a valid code for a title.
   *
   * @param tenantId the Universally Unique Identifier (UUID) for the tenant
   * @param titleCode the code for the title
   * @return <b>true</b> if the code is a valid code for a title or <b>false</b> otherwise
   */
  boolean isValidTitle(UUID tenantId, String titleCode) throws ServiceUnavailableException;
}
