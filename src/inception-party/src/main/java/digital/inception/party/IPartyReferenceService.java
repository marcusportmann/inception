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
  /** The ID for the default tenant. */
  UUID DEFAULT_TENANT_ID = UUID.fromString("00000000-0000-0000-0000-000000000000");

  /**
   * Retrieve the association property type with the specified code for the association type with
   * the specified code for the tenant with the specified ID for the first matching locale.
   *
   * @param tenantId the ID for the tenant the association property type reference data is specific
   *     to
   * @param associationTypeCode the code for the association type
   * @param associationPropertyTypeCode the code for the association property type
   * @return an Optional containing the association property type with the specified code for the
   *     association type with the specified code for the tenant with the specified ID for the first
   *     matching locale or an empty Optional if the association property type could not be found
   * @throws ServiceUnavailableException if the association property type could not be retrieved
   */
  Optional<AssociationPropertyType> getAssociationPropertyType(
      UUID tenantId, String associationTypeCode, String associationPropertyTypeCode)
      throws ServiceUnavailableException;

  /**
   * Retrieve the association property type reference data for a specific locale.
   *
   * @param localeId the Unicode locale identifier for the locale to retrieve the association
   *     property type reference data for
   * @return the association property type reference data
   * @throws InvalidArgumentException if an argument is invalid
   * @throws ServiceUnavailableException if the association property type reference data could not
   *     be retrieved
   */
  List<AssociationPropertyType> getAssociationPropertyTypes(String localeId)
      throws InvalidArgumentException, ServiceUnavailableException;

  /**
   * Retrieve the association property type reference data for a specific tenant and locale.
   *
   * @param tenantId the ID for the tenant the association property type reference data is specific
   *     to
   * @param localeId the Unicode locale identifier for the locale to retrieve the association
   *     property type reference data for
   * @return the association property type reference data
   * @throws InvalidArgumentException if an argument is invalid
   * @throws ServiceUnavailableException if the association property type reference data could not
   *     be retrieved
   */
  List<AssociationPropertyType> getAssociationPropertyTypes(UUID tenantId, String localeId)
      throws InvalidArgumentException, ServiceUnavailableException;

  /**
   * Retrieve the association property type reference data for all locales.
   *
   * @return the association property type reference data
   * @throws ServiceUnavailableException if the association property type reference data could not
   *     be retrieved
   */
  List<AssociationPropertyType> getAssociationPropertyTypes() throws ServiceUnavailableException;

  /**
   * Retrieve the association type reference data for a specific locale.
   *
   * @param localeId the Unicode locale identifier for the locale to retrieve the association type
   *     reference data for
   * @return the association type reference data
   * @throws InvalidArgumentException if an argument is invalid
   * @throws ServiceUnavailableException if the association type reference data could not be
   *     retrieved
   */
  List<AssociationType> getAssociationTypes(String localeId)
      throws InvalidArgumentException, ServiceUnavailableException;

  /**
   * Retrieve the association type reference data for a specific tenant and locale.
   *
   * @param tenantId the ID for the tenant the association type reference data is specific to
   * @param localeId the Unicode locale identifier for the locale to retrieve the association type
   *     reference data for
   * @return the association type reference data
   * @throws InvalidArgumentException if an argument is invalid
   * @throws ServiceUnavailableException if the association type reference data could not be
   *     retrieved
   */
  List<AssociationType> getAssociationTypes(UUID tenantId, String localeId)
      throws InvalidArgumentException, ServiceUnavailableException;

  /**
   * Retrieve the association type reference data for all locales.
   *
   * @return the association type reference data
   * @throws ServiceUnavailableException if the association type reference data could not be
   *     retrieved
   */
  List<AssociationType> getAssociationTypes() throws ServiceUnavailableException;

  /**
   * Retrieve the attribute type with the specified code for the party type with the specified code
   * for the tenant with the specified ID for the first matching locale.
   *
   * @param tenantId the ID for the tenant the attribute type reference data is specific to
   * @param partyTypeCode the code for the party type
   * @param attributeTypeCode the code for the attribute type
   * @return an Optional containing the attribute type with the specified code for the party type
   *     with the specified code for the tenant with the specified ID for the first matching locale
   *     or an empty Optional if the attribute type could not be found
   * @throws ServiceUnavailableException if the attribute type could not be retrieved
   */
  Optional<AttributeType> getAttributeType(
      UUID tenantId, String partyTypeCode, String attributeTypeCode)
      throws ServiceUnavailableException;

  /**
   * Retrieve the attribute type category reference data for a specific locale.
   *
   * @param localeId the Unicode locale identifier for the locale to retrieve the attribute type
   *     category reference data for
   * @return the attribute type category reference data
   * @throws InvalidArgumentException if an argument is invalid
   * @throws ServiceUnavailableException if the attribute type category reference data could not be
   *     retrieved
   */
  List<AttributeTypeCategory> getAttributeTypeCategories(String localeId)
      throws InvalidArgumentException, ServiceUnavailableException;

  /**
   * Retrieve the attribute type category reference data for a specific tenant and locale.
   *
   * @param tenantId the ID for the tenant the attribute type category reference data is specific to
   * @param localeId the Unicode locale identifier for the locale to retrieve the attribute type
   *     category reference data for
   * @return the attribute type category reference data
   * @throws InvalidArgumentException if an argument is invalid
   * @throws ServiceUnavailableException if the attribute type category reference data could not be
   *     retrieved
   */
  List<AttributeTypeCategory> getAttributeTypeCategories(UUID tenantId, String localeId)
      throws InvalidArgumentException, ServiceUnavailableException;

  /**
   * Retrieve the attribute type category reference data for all locales.
   *
   * @return the attribute type category reference data
   * @throws ServiceUnavailableException if the attribute type category reference data could not be
   *     retrieved
   */
  List<AttributeTypeCategory> getAttributeTypeCategories() throws ServiceUnavailableException;

  /**
   * Retrieve the value type for the first attribute type with the specified code for any tenant or
   * locale.
   *
   * @param attributeTypeCode the code for the attribute type
   * @return the value type for the attribute type
   * @throws ServiceUnavailableException if the value type for the first attribute type with the
   *     specified code for any tenant or locale could not be retrieved
   */
  Optional<ValueType> getAttributeTypeValueType(String attributeTypeCode)
      throws ServiceUnavailableException;

  /**
   * Retrieve the attribute type reference data for a specific locale.
   *
   * @param localeId the Unicode locale identifier for the locale to retrieve the attribute type
   *     reference data for
   * @return the attribute type reference data
   * @throws InvalidArgumentException if an argument is invalid
   * @throws ServiceUnavailableException if the attribute type reference data could not be retrieved
   */
  List<AttributeType> getAttributeTypes(String localeId)
      throws InvalidArgumentException, ServiceUnavailableException;

  /**
   * Retrieve the attribute type reference data for a specific tenant and locale.
   *
   * @param tenantId the ID for the tenant the attribute type reference data is specific to
   * @param localeId the Unicode locale identifier for the locale to retrieve the attribute type
   *     reference data for
   * @return the attribute type reference data
   * @throws InvalidArgumentException if an argument is invalid
   * @throws ServiceUnavailableException if the attribute type reference data could not be retrieved
   */
  List<AttributeType> getAttributeTypes(UUID tenantId, String localeId)
      throws InvalidArgumentException, ServiceUnavailableException;

  /**
   * Retrieve the attribute type reference data for all locales.
   *
   * @return the attribute type reference data
   * @throws ServiceUnavailableException if the attribute type reference data could not be retrieved
   */
  List<AttributeType> getAttributeTypes() throws ServiceUnavailableException;

  /**
   * Retrieve the consent type reference data for a specific locale.
   *
   * @param localeId the Unicode locale identifier for the locale to retrieve the consent type
   *     reference data for
   * @return the consent type reference data
   * @throws InvalidArgumentException if an argument is invalid
   * @throws ServiceUnavailableException if the consent type reference data could not be retrieved
   */
  List<ConsentType> getConsentTypes(String localeId)
      throws InvalidArgumentException, ServiceUnavailableException;

  /**
   * Retrieve the consent type reference data for a specific tenant and locale.
   *
   * @param tenantId the ID for the tenant the consent type reference data is specific to
   * @param localeId the Unicode locale identifier for the locale to retrieve the consent type
   *     reference data for
   * @return the consent type reference data
   * @throws InvalidArgumentException if an argument is invalid
   * @throws ServiceUnavailableException if the consent type reference data could not be retrieved
   */
  List<ConsentType> getConsentTypes(UUID tenantId, String localeId)
      throws InvalidArgumentException, ServiceUnavailableException;

  /**
   * Retrieve the consent type reference data for all locales.
   *
   * @return the consent type reference data
   * @throws ServiceUnavailableException if the consent type reference data could not be retrieved
   */
  List<ConsentType> getConsentTypes() throws ServiceUnavailableException;

  /**
   * Retrieve the contact mechanism purpose reference data for a specific locale.
   *
   * @param localeId the Unicode locale identifier for the locale to retrieve the contact mechanism
   *     purpose reference data for
   * @return the contact mechanism purpose reference data
   * @throws InvalidArgumentException if an argument is invalid
   * @throws ServiceUnavailableException if the contact mechanism purpose reference data could not
   *     be retrieved
   */
  List<ContactMechanismPurpose> getContactMechanismPurposes(String localeId)
      throws InvalidArgumentException, ServiceUnavailableException;

  /**
   * Retrieve the contact mechanism purpose reference data for a specific tenant and locale.
   *
   * @param tenantId the ID for the tenant the contact mechanism purpose reference data is specific
   *     to
   * @param localeId the Unicode locale identifier for the locale to retrieve the contact mechanism
   *     purpose reference data for
   * @return the contact mechanism purpose reference data
   * @throws InvalidArgumentException if an argument is invalid
   * @throws ServiceUnavailableException if the contact mechanism purpose reference data could not
   *     be retrieved
   */
  List<ContactMechanismPurpose> getContactMechanismPurposes(UUID tenantId, String localeId)
      throws InvalidArgumentException, ServiceUnavailableException;

  /**
   * Retrieve the contact mechanism purpose reference data for all locales.
   *
   * @return the contact mechanism purpose reference data
   * @throws ServiceUnavailableException if the contact mechanism purpose reference data could not
   *     be retrieved
   */
  List<ContactMechanismPurpose> getContactMechanismPurposes() throws ServiceUnavailableException;

  /**
   * Retrieve the contact mechanism role with the specified code, for the contact mechanism type
   * with the specified code and the party type with the specified code, for the tenant with the
   * specified ID for the first matching locale.
   *
   * @param tenantId the ID for the tenant
   * @param partyTypeCode the code for the party type
   * @param contactMechanismTypeCode the code for the contact mechanism type
   * @param contactMechanismRoleCode the code for the contact mechanism role
   * @return an Optional containing the contact mechanism role with the specified code, for the
   *     contact mechanism type with the specified code and the party type with the specified code,
   *     for the tenant with the specified ID for the first matching locale or an empty Optional if
   *     the attribute type could not be found
   * @throws ServiceUnavailableException if the contact mechanism role type could not be retrieved
   */
  Optional<ContactMechanismRole> getContactMechanismRole(
      UUID tenantId,
      String partyTypeCode,
      String contactMechanismTypeCode,
      String contactMechanismRoleCode)
      throws ServiceUnavailableException;

  /**
   * Retrieve the contact mechanism role reference data for a specific locale.
   *
   * @param localeId the Unicode locale identifier for the locale to retrieve the contact mechanism
   *     role reference data for
   * @return the contact mechanism role reference data
   * @throws InvalidArgumentException if an argument is invalid
   * @throws ServiceUnavailableException if the contact mechanism role reference data could not be
   *     retrieved
   */
  List<ContactMechanismRole> getContactMechanismRoles(String localeId)
      throws InvalidArgumentException, ServiceUnavailableException;

  /**
   * Retrieve the contact mechanism role reference data for a specific tenant and locale.
   *
   * @param tenantId the ID for the tenant the contact mechanism role reference data is specific to
   * @param localeId the Unicode locale identifier for the locale to retrieve the contact mechanism
   *     role reference data for
   * @return the contact mechanism role reference data
   * @throws InvalidArgumentException if an argument is invalid
   * @throws ServiceUnavailableException if the contact mechanism role reference data could not be
   *     retrieved
   */
  List<ContactMechanismRole> getContactMechanismRoles(UUID tenantId, String localeId)
      throws InvalidArgumentException, ServiceUnavailableException;

  /**
   * Retrieve the contact mechanism role reference data for all locales.
   *
   * @return the contact mechanism role reference data
   * @throws ServiceUnavailableException if the contact mechanism role reference data could not be
   *     retrieved
   */
  List<ContactMechanismRole> getContactMechanismRoles() throws ServiceUnavailableException;

  /**
   * Retrieve the contact mechanism type with the specified code for the tenant with the specified
   * ID for the first matching locale.
   *
   * @param tenantId the ID for the tenant
   * @param contactMechanismTypeCode the code for the contact mechanism type
   * @return an Optional containing the contact mechanism type with the specified code for the
   *     tenant with the specified ID for the first matching locale or an empty Optional if the
   *     contact mechanism type could not be found
   * @throws ServiceUnavailableException if the contact mechanism type could not be retrieved
   */
  Optional<ContactMechanismType> getContactMechanismType(
      UUID tenantId, String contactMechanismTypeCode) throws ServiceUnavailableException;

  /**
   * Retrieve the contact mechanism type reference data for a specific locale.
   *
   * @param localeId the Unicode locale identifier for the locale to retrieve the contact mechanism
   *     type reference data for
   * @return the contact mechanism type reference data
   * @throws InvalidArgumentException if an argument is invalid
   * @throws ServiceUnavailableException if the contact mechanism type reference data could not be
   *     retrieved
   */
  List<ContactMechanismType> getContactMechanismTypes(String localeId)
      throws InvalidArgumentException, ServiceUnavailableException;

  /**
   * Retrieve the contact mechanism type reference data for a specific tenant and locale.
   *
   * @param tenantId the ID for the tenant the contact mechanism type reference data is specific to
   * @param localeId the Unicode locale identifier for the locale to retrieve the contact mechanism
   *     type reference data for
   * @return the contact mechanism type reference data
   * @throws InvalidArgumentException if an argument is invalid
   * @throws ServiceUnavailableException if the contact mechanism type reference data could not be
   *     retrieved
   */
  List<ContactMechanismType> getContactMechanismTypes(UUID tenantId, String localeId)
      throws InvalidArgumentException, ServiceUnavailableException;

  /**
   * Retrieve the contact mechanism type reference data for all locales.
   *
   * @return the contact mechanism type reference data
   * @throws ServiceUnavailableException if the contact mechanism type reference data could not be
   *     retrieved
   */
  List<ContactMechanismType> getContactMechanismTypes() throws ServiceUnavailableException;

  /**
   * Retrieve the employment status reference data for a specific locale.
   *
   * @param localeId the Unicode locale identifier for the locale to retrieve the employment status
   *     reference data for
   * @return the employment status reference data
   * @throws InvalidArgumentException if an argument is invalid
   * @throws ServiceUnavailableException if the employment status reference data could not be
   *     retrieved
   */
  List<EmploymentStatus> getEmploymentStatuses(String localeId)
      throws InvalidArgumentException, ServiceUnavailableException;

  /**
   * Retrieve the employment status reference data for a specific tenant and locale.
   *
   * @param tenantId the ID for the tenant the employment status reference data is specific to
   * @param localeId the Unicode locale identifier for the locale to retrieve the employment status
   *     reference data for
   * @return the employment status reference data
   * @throws InvalidArgumentException if an argument is invalid
   * @throws ServiceUnavailableException if the employment status reference data could not be
   *     retrieved
   */
  List<EmploymentStatus> getEmploymentStatuses(UUID tenantId, String localeId)
      throws InvalidArgumentException, ServiceUnavailableException;

  /**
   * Retrieve the employment status reference data for all locales.
   *
   * @return the employment status reference data
   * @throws ServiceUnavailableException if the employment status reference data could not be
   *     retrieved
   */
  List<EmploymentStatus> getEmploymentStatuses() throws ServiceUnavailableException;

  /**
   * Retrieve the employment type reference data for a specific locale.
   *
   * @param localeId the Unicode locale identifier for the locale to retrieve the employment type
   *     reference data for
   * @return the employment type reference data
   * @throws InvalidArgumentException if an argument is invalid
   * @throws ServiceUnavailableException if the employment type reference data could not be
   *     retrieved
   */
  List<EmploymentType> getEmploymentTypes(String localeId)
      throws InvalidArgumentException, ServiceUnavailableException;

  /**
   * Retrieve the employment type reference data for a specific tenant and locale.
   *
   * @param tenantId the ID for the tenant the employment type reference data is specific to
   * @param localeId the Unicode locale identifier for the locale to retrieve the employment type
   *     reference data for
   * @return the employment type reference data
   * @throws InvalidArgumentException if an argument is invalid
   * @throws ServiceUnavailableException if the employment type reference data could not be
   *     retrieved
   */
  List<EmploymentType> getEmploymentTypes(UUID tenantId, String localeId)
      throws InvalidArgumentException, ServiceUnavailableException;

  /**
   * Retrieve the employment type reference data for all locales.
   *
   * @return the employment type reference data
   * @throws ServiceUnavailableException if the employment type reference data could not be
   *     retrieved
   */
  List<EmploymentType> getEmploymentTypes() throws ServiceUnavailableException;

  /**
   * Retrieve the external reference type reference data for a specific locale.
   *
   * @param localeId the Unicode locale identifier for the locale to retrieve the external reference
   *     type reference data for
   * @return the external reference type reference data
   * @throws InvalidArgumentException if an argument is invalid
   * @throws ServiceUnavailableException if the external reference type reference data could not be
   *     retrieved
   */
  List<ExternalReferenceType> getExternalReferenceTypes(String localeId)
      throws InvalidArgumentException, ServiceUnavailableException;

  /**
   * Retrieve the external reference type reference data for a specific tenant and locale.
   *
   * @param tenantId the ID for the tenant the external reference type reference data is specific to
   * @param localeId the Unicode locale identifier for the locale to retrieve the external reference
   *     type reference data for
   * @return the external reference type reference data
   * @throws InvalidArgumentException if an argument is invalid
   * @throws ServiceUnavailableException if the external reference type reference data could not be
   *     retrieved
   */
  List<ExternalReferenceType> getExternalReferenceTypes(UUID tenantId, String localeId)
      throws InvalidArgumentException, ServiceUnavailableException;

  /**
   * Retrieve the external reference type reference data for all locales.
   *
   * @return the external reference type reference data
   * @throws ServiceUnavailableException if the external reference type reference data could not be
   *     retrieved
   */
  List<ExternalReferenceType> getExternalReferenceTypes() throws ServiceUnavailableException;

  /**
   * Retrieve the fields of study reference data for a specific locale.
   *
   * @param localeId the Unicode locale identifier for the locale to retrieve the fields of study
   *     reference data for
   * @return the fields of study reference data
   * @throws InvalidArgumentException if an argument is invalid
   * @throws ServiceUnavailableException if the fields of study reference data could not be
   *     retrieved
   */
  List<FieldOfStudy> getFieldsOfStudy(String localeId)
      throws InvalidArgumentException, ServiceUnavailableException;

  /**
   * Retrieve the fields of study reference data for a specific tenant and locale.
   *
   * @param tenantId the ID for the tenant the fields of study reference data is specific to
   * @param localeId the Unicode locale identifier for the locale to retrieve the fields of study
   *     reference data for
   * @return the fields of study reference data
   * @throws InvalidArgumentException if an argument is invalid
   * @throws ServiceUnavailableException if the fields of study reference data could not be
   *     retrieved
   */
  List<FieldOfStudy> getFieldsOfStudy(UUID tenantId, String localeId)
      throws InvalidArgumentException, ServiceUnavailableException;

  /**
   * Retrieve the fields of study reference data for all locales.
   *
   * @return the fields of study reference data
   * @throws ServiceUnavailableException if the fields of study reference data could not be
   *     retrieved
   */
  List<FieldOfStudy> getFieldsOfStudy() throws ServiceUnavailableException;

  /**
   * Retrieve the gender reference data for a specific locale.
   *
   * @param localeId the Unicode locale identifier for the locale to retrieve the gender reference
   *     data for
   * @return the gender reference data
   * @throws InvalidArgumentException if an argument is invalid
   * @throws ServiceUnavailableException if the gender reference data could not be retrieved
   */
  List<Gender> getGenders(String localeId)
      throws InvalidArgumentException, ServiceUnavailableException;

  /**
   * Retrieve the gender reference data for a specific tenant and locale.
   *
   * @param tenantId the ID for the tenant the gender reference data is specific to
   * @param localeId the Unicode locale identifier for the locale to retrieve the gender reference
   *     data for
   * @return the gender reference data
   * @throws InvalidArgumentException if an argument is invalid
   * @throws ServiceUnavailableException if the gender reference data could not be retrieved
   */
  List<Gender> getGenders(UUID tenantId, String localeId)
      throws InvalidArgumentException, ServiceUnavailableException;

  /**
   * Retrieve the gender reference data for all locales.
   *
   * @return the gender reference data
   * @throws ServiceUnavailableException if the gender reference data could not be retrieved
   */
  List<Gender> getGenders() throws ServiceUnavailableException;

  /**
   * Retrieve the identity document type reference data for a specific locale.
   *
   * @param localeId the Unicode locale identifier for the locale to retrieve the identity document
   *     type reference data for
   * @return the identity document type reference data
   * @throws InvalidArgumentException if an argument is invalid
   * @throws ServiceUnavailableException if the identity document type reference data could not be
   *     retrieved
   */
  List<IdentityDocumentType> getIdentityDocumentTypes(String localeId)
      throws InvalidArgumentException, ServiceUnavailableException;

  /**
   * Retrieve the identity document type reference data for a specific tenant and locale.
   *
   * @param tenantId the ID for the tenant the identity document type reference data is specific to
   * @param localeId the Unicode locale identifier for the locale to retrieve the identity document
   *     type reference data for
   * @return the identity document type reference data
   * @throws InvalidArgumentException if an argument is invalid
   * @throws ServiceUnavailableException if the identity document type reference data could not be
   *     retrieved
   */
  List<IdentityDocumentType> getIdentityDocumentTypes(UUID tenantId, String localeId)
      throws InvalidArgumentException, ServiceUnavailableException;

  /**
   * Retrieve the identity document type reference data for all locales.
   *
   * @return the identity document type reference data
   * @throws ServiceUnavailableException if the identity document type reference data could not be
   *     retrieved
   */
  List<IdentityDocumentType> getIdentityDocumentTypes() throws ServiceUnavailableException;

  /**
   * Retrieve the link type reference data for a specific locale.
   *
   * @param localeId the Unicode locale identifier for the locale to retrieve the link type
   *     reference data for
   * @return the link type reference data
   * @throws InvalidArgumentException if an argument is invalid
   * @throws ServiceUnavailableException if the link type reference data could not be retrieved
   */
  List<LinkType> getLinkTypes(String localeId)
      throws InvalidArgumentException, ServiceUnavailableException;

  /**
   * Retrieve the link type reference data for a specific tenant and locale.
   *
   * @param tenantId the ID for the tenant the link type reference data is specific to
   * @param localeId the Unicode locale identifier for the locale to retrieve the link type
   *     reference data for
   * @return the link type reference data
   * @throws InvalidArgumentException if an argument is invalid
   * @throws ServiceUnavailableException if the link type reference data could not be retrieved
   */
  List<LinkType> getLinkTypes(UUID tenantId, String localeId)
      throws InvalidArgumentException, ServiceUnavailableException;

  /**
   * Retrieve the link type reference data for all locales.
   *
   * @return the link type reference data
   * @throws ServiceUnavailableException if the link type reference data could not be retrieved
   */
  List<LinkType> getLinkTypes() throws ServiceUnavailableException;

  /**
   * Retrieve the lock type category reference data for a specific locale.
   *
   * @param localeId the Unicode locale identifier for the locale to retrieve the lock type category
   *     reference data for
   * @return the lock type category reference data
   * @throws InvalidArgumentException if an argument is invalid
   * @throws ServiceUnavailableException if the lock type category reference data could not be
   *     retrieved
   */
  List<LockTypeCategory> getLockTypeCategories(String localeId)
      throws InvalidArgumentException, ServiceUnavailableException;

  /**
   * Retrieve the lock type category reference data for a specific tenant and locale.
   *
   * @param tenantId the ID for the tenant the lock type category reference data is specific to
   * @param localeId the Unicode locale identifier for the locale to retrieve the lock type category
   *     reference data for
   * @return the lock type category reference data
   * @throws InvalidArgumentException if an argument is invalid
   * @throws ServiceUnavailableException if the lock type category reference data could not be
   *     retrieved
   */
  List<LockTypeCategory> getLockTypeCategories(UUID tenantId, String localeId)
      throws InvalidArgumentException, ServiceUnavailableException;

  /**
   * Retrieve the lock type category reference data for all locales.
   *
   * @return the lock type category reference data
   * @throws ServiceUnavailableException if the lock type category reference data could not be
   *     retrieved
   */
  List<LockTypeCategory> getLockTypeCategories() throws ServiceUnavailableException;

  /**
   * Retrieve the lock type reference data for a specific locale.
   *
   * @param localeId the Unicode locale identifier for the locale to retrieve the lock type
   *     reference data for
   * @return the lock type reference data
   * @throws InvalidArgumentException if an argument is invalid
   * @throws ServiceUnavailableException if the lock type reference data could not be retrieved
   */
  List<LockType> getLockTypes(String localeId)
      throws InvalidArgumentException, ServiceUnavailableException;

  /**
   * Retrieve the lock type reference data for a specific tenant and locale.
   *
   * @param tenantId the ID for the tenant the lock type reference data is specific to
   * @param localeId the Unicode locale identifier for the locale to retrieve the lock type
   *     reference data for
   * @return the lock type reference data
   * @throws InvalidArgumentException if an argument is invalid
   * @throws ServiceUnavailableException if the lock type reference data could not be retrieved
   */
  List<LockType> getLockTypes(UUID tenantId, String localeId)
      throws InvalidArgumentException, ServiceUnavailableException;

  /**
   * Retrieve the lock type reference data for all locales.
   *
   * @return the lock type reference data
   * @throws ServiceUnavailableException if the lock type reference data could not be retrieved
   */
  List<LockType> getLockTypes() throws ServiceUnavailableException;

  /**
   * Retrieve the mandatary role reference data for a specific locale.
   *
   * @param localeId the Unicode locale identifier for the locale to retrieve the mandatary role
   *     reference data for
   * @return the mandatary role reference data
   * @throws InvalidArgumentException if an argument is invalid
   * @throws ServiceUnavailableException if the mandatary role reference data could not be retrieved
   */
  List<MandataryRole> getMandataryRoles(String localeId)
      throws InvalidArgumentException, ServiceUnavailableException;

  /**
   * Retrieve the mandatary role reference data for a specific tenant and locale.
   *
   * @param tenantId the ID for the tenant the mandatary role reference data is specific to
   * @param localeId the Unicode locale identifier for the locale to retrieve the mandatary role
   *     reference data for
   * @return the mandatary role reference data
   * @throws InvalidArgumentException if an argument is invalid
   * @throws ServiceUnavailableException if the mandatary role reference data could not be retrieved
   */
  List<MandataryRole> getMandataryRoles(UUID tenantId, String localeId)
      throws InvalidArgumentException, ServiceUnavailableException;

  /**
   * Retrieve the mandatary role reference data for all locales.
   *
   * @return the mandatary role reference data
   * @throws ServiceUnavailableException if the mandatary role reference data could not be retrieved
   */
  List<MandataryRole> getMandataryRoles() throws ServiceUnavailableException;

  /**
   * Retrieve the mandate property type with the specified code for the mandate type with the
   * specified code for the tenant with the specified ID for the first matching locale.
   *
   * @param tenantId the ID for the tenant the mandate property type reference data is specific to
   * @param mandatePropertyTypeCode the code for the mandate property type
   * @return an Optional containing the mandate property type with the specified code for the
   *     mandate type with the specified code for the tenant with the specified ID for the first
   *     matching locale or an empty Optional if the mandate property type could not be found
   * @throws ServiceUnavailableException if the mandate property type could not be retrieved
   */
  Optional<MandatePropertyType> getMandatePropertyType(
      UUID tenantId, String mandateTypeCode, String mandatePropertyTypeCode)
      throws ServiceUnavailableException;

  /**
   * Retrieve the mandate property type reference data for a specific locale.
   *
   * @param localeId the Unicode locale identifier for the locale to retrieve the mandate property
   *     type reference data for
   * @return the mandate property type reference data
   * @throws InvalidArgumentException if an argument is invalid
   * @throws ServiceUnavailableException if the mandate property type reference data could not be
   *     retrieved
   */
  List<MandatePropertyType> getMandatePropertyTypes(String localeId)
      throws InvalidArgumentException, ServiceUnavailableException;

  /**
   * Retrieve the mandate property type reference data for a specific tenant and locale.
   *
   * @param tenantId the ID for the tenant the mandate property type reference data is specific to
   * @param localeId the Unicode locale identifier for the locale to retrieve the mandate property
   *     type reference data for
   * @return the mandate property type reference data
   * @throws InvalidArgumentException if an argument is invalid
   * @throws ServiceUnavailableException if the mandate property type reference data could not be
   *     retrieved
   */
  List<MandatePropertyType> getMandatePropertyTypes(UUID tenantId, String localeId)
      throws InvalidArgumentException, ServiceUnavailableException;

  /**
   * Retrieve the mandate property type reference data for all locales.
   *
   * @return the mandate property type reference data
   * @throws ServiceUnavailableException if the mandate property type reference data could not be
   *     retrieved
   */
  List<MandatePropertyType> getMandatePropertyTypes() throws ServiceUnavailableException;

  /**
   * Retrieve the mandate type reference data for a specific locale.
   *
   * @param localeId the Unicode locale identifier for the locale to retrieve the mandate type
   *     reference data for
   * @return the mandate type reference data
   * @throws InvalidArgumentException if an argument is invalid
   * @throws ServiceUnavailableException if the mandate type reference data could not be retrieved
   */
  List<MandateType> getMandateTypes(String localeId)
      throws InvalidArgumentException, ServiceUnavailableException;

  /**
   * Retrieve the mandate type reference data for a specific tenant and locale.
   *
   * @param tenantId the ID for the tenant the mandate type reference data is specific to
   * @param localeId the Unicode locale identifier for the locale to retrieve the mandate type
   *     reference data for
   * @return the mandate type reference data
   * @throws InvalidArgumentException if an argument is invalid
   * @throws ServiceUnavailableException if the mandate type reference data could not be retrieved
   */
  List<MandateType> getMandateTypes(UUID tenantId, String localeId)
      throws InvalidArgumentException, ServiceUnavailableException;

  /**
   * Retrieve the mandate type reference data for all locales.
   *
   * @return the mandate type reference data
   * @throws ServiceUnavailableException if the mandate type reference data could not be retrieved
   */
  List<MandateType> getMandateTypes() throws ServiceUnavailableException;

  /**
   * Retrieve the marital status reference data for a specific locale.
   *
   * @param localeId the Unicode locale identifier for the locale to retrieve the marital status
   *     reference data for
   * @return the marital status reference data
   * @throws InvalidArgumentException if an argument is invalid
   * @throws ServiceUnavailableException if the marital status reference data could not be retrieved
   */
  List<MaritalStatus> getMaritalStatuses(String localeId)
      throws InvalidArgumentException, ServiceUnavailableException;

  /**
   * Retrieve the marital status reference data for a specific tenant and locale.
   *
   * @param tenantId the ID for the tenant the marital status reference data is specific to
   * @param localeId the Unicode locale identifier for the locale to retrieve the marital status
   *     reference data for
   * @return the marital status reference data
   * @throws InvalidArgumentException if an argument is invalid
   * @throws ServiceUnavailableException if the marital status reference data could not be retrieved
   */
  List<MaritalStatus> getMaritalStatuses(UUID tenantId, String localeId)
      throws InvalidArgumentException, ServiceUnavailableException;

  /**
   * Retrieve the marital status reference data for all locales.
   *
   * @return the marital status reference data
   * @throws ServiceUnavailableException if the marital status reference data could not be retrieved
   */
  List<MaritalStatus> getMaritalStatuses() throws ServiceUnavailableException;

  /**
   * Retrieve the marriage type reference data for a specific locale.
   *
   * @param localeId the Unicode locale identifier for the locale to retrieve the marriage type
   *     reference data
   * @return the marriage type reference data
   * @throws InvalidArgumentException if an argument is invalid
   * @throws ServiceUnavailableException if the marriage type reference data could not be retrieved
   */
  List<MarriageType> getMarriageTypes(String localeId)
      throws InvalidArgumentException, ServiceUnavailableException;

  /**
   * Retrieve the marriage type reference data for a specific tenant and locale.
   *
   * @param tenantId the ID for the tenant the marriage type reference data is specific to
   * @param localeId the Unicode locale identifier for the locale to retrieve the marriage type
   *     reference data
   * @return the marriage type reference data
   * @throws InvalidArgumentException if an argument is invalid
   * @throws ServiceUnavailableException if the marriage type reference data could not be retrieved
   */
  List<MarriageType> getMarriageTypes(UUID tenantId, String localeId)
      throws InvalidArgumentException, ServiceUnavailableException;

  /**
   * Retrieve the marriage type reference data for all locales.
   *
   * @return the marriage type reference data
   * @throws ServiceUnavailableException if the marriage type reference data could not be retrieved
   */
  List<MarriageType> getMarriageTypes() throws ServiceUnavailableException;

  /**
   * Retrieve the next of kin type reference data for a specific locale.
   *
   * @param localeId the Unicode locale identifier for the locale to retrieve the next of kin type
   *     reference data for
   * @return the next of kin type reference data
   * @throws InvalidArgumentException if an argument is invalid
   * @throws ServiceUnavailableException if the next of kin type reference data could not be
   *     retrieved
   */
  List<NextOfKinType> getNextOfKinTypes(String localeId)
      throws InvalidArgumentException, ServiceUnavailableException;

  /**
   * Retrieve the next of kin type reference data for a specific tenant and locale.
   *
   * @param tenantId the ID for the tenant the next of kin type reference data is specific to
   * @param localeId the Unicode locale identifier for the locale to retrieve the next of kin type
   *     reference data for
   * @return the next of kin type reference data
   * @throws InvalidArgumentException if an argument is invalid
   * @throws ServiceUnavailableException if the next of kin type reference data could not be
   *     retrieved
   */
  List<NextOfKinType> getNextOfKinTypes(UUID tenantId, String localeId)
      throws InvalidArgumentException, ServiceUnavailableException;

  /**
   * Retrieve the next of kin type reference data for all locales.
   *
   * @return the next of kin type reference data
   * @throws ServiceUnavailableException if the next of kin type reference data could not be
   *     retrieved
   */
  List<NextOfKinType> getNextOfKinTypes() throws ServiceUnavailableException;

  /**
   * Retrieve the occupation reference data for a specific locale.
   *
   * @param localeId the Unicode locale identifier for the locale to retrieve the occupation
   *     reference data for
   * @return the occupation reference data
   * @throws InvalidArgumentException if an argument is invalid
   * @throws ServiceUnavailableException if the occupation reference data could not be retrieved
   */
  List<Occupation> getOccupations(String localeId)
      throws InvalidArgumentException, ServiceUnavailableException;

  /**
   * Retrieve the occupation reference data for a specific tenant and locale.
   *
   * @param tenantId the ID for the tenant the occupation reference data is specific to
   * @param localeId the Unicode locale identifier for the locale to retrieve the occupation
   *     reference data for
   * @return the occupation reference data
   * @throws InvalidArgumentException if an argument is invalid
   * @throws ServiceUnavailableException if the occupation reference data could not be retrieved
   */
  List<Occupation> getOccupations(UUID tenantId, String localeId)
      throws InvalidArgumentException, ServiceUnavailableException;

  /**
   * Retrieve the occupation reference data for all locales.
   *
   * @return the occupation reference data
   * @throws ServiceUnavailableException if the occupation reference data could not be retrieved
   */
  List<Occupation> getOccupations() throws ServiceUnavailableException;

  /**
   * Retrieve the physical address purpose reference data for a specific locale.
   *
   * @param localeId the Unicode locale identifier for the locale to retrieve the physical address
   *     purpose reference data for
   * @return the physical address purpose reference data
   * @throws InvalidArgumentException if an argument is invalid
   * @throws ServiceUnavailableException if the physical address purpose reference data could not be
   *     retrieved
   */
  List<PhysicalAddressPurpose> getPhysicalAddressPurposes(String localeId)
      throws InvalidArgumentException, ServiceUnavailableException;

  /**
   * Retrieve the physical address purpose reference data for a specific tenant and locale.
   *
   * @param tenantId the ID for the tenant the physical address purpose reference data is specific
   *     to
   * @param localeId the Unicode locale identifier for the locale to retrieve the physical address
   *     purpose reference data for
   * @return the physical address purpose reference data
   * @throws InvalidArgumentException if an argument is invalid
   * @throws ServiceUnavailableException if the physical address purpose reference data could not be
   *     retrieved
   */
  List<PhysicalAddressPurpose> getPhysicalAddressPurposes(UUID tenantId, String localeId)
      throws InvalidArgumentException, ServiceUnavailableException;

  /**
   * Retrieve the physical address purpose reference data for all locales.
   *
   * @return the physical address purpose reference data
   * @throws ServiceUnavailableException if the physical address purpose reference data could not be
   *     retrieved
   */
  List<PhysicalAddressPurpose> getPhysicalAddressPurposes() throws ServiceUnavailableException;

  /**
   * Retrieve the physical address role reference data for a specific locale.
   *
   * @param localeId the Unicode locale identifier for the locale to retrieve the physical address
   *     role reference data for
   * @return the physical address role reference data
   * @throws InvalidArgumentException if an argument is invalid
   * @throws ServiceUnavailableException if the physical address role reference data could not be
   *     retrieved
   */
  List<PhysicalAddressRole> getPhysicalAddressRoles(String localeId)
      throws InvalidArgumentException, ServiceUnavailableException;

  /**
   * Retrieve the physical address role reference data for a specific tenant and locale.
   *
   * @param tenantId the ID for the tenant the physical address role reference data is specific to
   * @param localeId the Unicode locale identifier for the locale to retrieve the physical address
   *     role reference data for
   * @return the physical address role reference data
   * @throws InvalidArgumentException if an argument is invalid
   * @throws ServiceUnavailableException if the physical address role reference data could not be
   *     retrieved
   */
  List<PhysicalAddressRole> getPhysicalAddressRoles(UUID tenantId, String localeId)
      throws InvalidArgumentException, ServiceUnavailableException;

  /**
   * Retrieve the physical address role reference data for all locales.
   *
   * @return the physical address role reference data
   * @throws ServiceUnavailableException if the physical address role reference data could not be
   *     retrieved
   */
  List<PhysicalAddressRole> getPhysicalAddressRoles() throws ServiceUnavailableException;

  /**
   * Retrieve the physical address type reference data for a specific locale.
   *
   * @param localeId the Unicode locale identifier for the locale to retrieve the physical address
   *     type reference data for
   * @return the physical address type reference data
   * @throws InvalidArgumentException if an argument is invalid
   * @throws ServiceUnavailableException if the physical address type reference data could not be
   *     retrieved
   */
  List<PhysicalAddressType> getPhysicalAddressTypes(String localeId)
      throws InvalidArgumentException, ServiceUnavailableException;

  /**
   * Retrieve the physical address type reference data for a specific tenant and locale.
   *
   * @param tenantId the ID for the tenant the physical address type reference data is specific to
   * @param localeId the Unicode locale identifier for the locale to retrieve the physical address
   *     type reference data for
   * @return the physical address type reference data
   * @throws InvalidArgumentException if an argument is invalid
   * @throws ServiceUnavailableException if the physical address type reference data could not be
   *     retrieved
   */
  List<PhysicalAddressType> getPhysicalAddressTypes(UUID tenantId, String localeId)
      throws InvalidArgumentException, ServiceUnavailableException;

  /**
   * Retrieve the physical address type reference data for all locales.
   *
   * @return the physical address type reference data
   * @throws ServiceUnavailableException if the physical address type reference data could not be
   *     retrieved
   */
  List<PhysicalAddressType> getPhysicalAddressTypes() throws ServiceUnavailableException;

  /**
   * Retrieve the preference type with the specified code for the party type with the specified code
   * for the tenant with the specified ID for the first matching locale.
   *
   * @param tenantId the ID for the tenant the preference type reference data is specific to
   * @param partyTypeCode the code for the party type
   * @param preferenceTypeCode the code for the preference type
   * @return an Optional containing the preference type with the specified code for the party type
   *     with the specified code for the tenant with the specified ID for the first matching locale
   *     or an empty Optional if the preference type could not be found
   * @throws ServiceUnavailableException if the preference type could not be retrieved
   */
  Optional<PreferenceType> getPreferenceType(
      UUID tenantId, String partyTypeCode, String preferenceTypeCode)
      throws ServiceUnavailableException;

  /**
   * Retrieve the preference type category reference data for a specific locale.
   *
   * @param localeId the Unicode locale identifier for the locale to retrieve the preference type
   *     category reference data for
   * @return the preference type category reference data
   * @throws InvalidArgumentException if an argument is invalid
   * @throws ServiceUnavailableException if the preference type category reference data could not be
   *     retrieved
   */
  List<PreferenceTypeCategory> getPreferenceTypeCategories(String localeId)
      throws InvalidArgumentException, ServiceUnavailableException;

  /**
   * Retrieve the preference type category reference data for a specific tenant and locale.
   *
   * @param tenantId the ID for the tenant the preference type category reference data is specific
   *     to
   * @param localeId the Unicode locale identifier for the locale to retrieve the preference type
   *     category reference data for
   * @return the preference type category reference data
   * @throws InvalidArgumentException if an argument is invalid
   * @throws ServiceUnavailableException if the preference type category reference data could not be
   *     retrieved
   */
  List<PreferenceTypeCategory> getPreferenceTypeCategories(UUID tenantId, String localeId)
      throws InvalidArgumentException, ServiceUnavailableException;

  /**
   * Retrieve the preference type category reference data for all locales.
   *
   * @return the preference type category reference data
   * @throws ServiceUnavailableException if the preference type category reference data could not be
   *     retrieved
   */
  List<PreferenceTypeCategory> getPreferenceTypeCategories() throws ServiceUnavailableException;

  /**
   * Retrieve the preference type reference data for a specific locale.
   *
   * @param localeId the Unicode locale identifier for the locale to retrieve the preference type
   *     reference data for
   * @return the preference type reference data
   * @throws InvalidArgumentException if an argument is invalid
   * @throws ServiceUnavailableException if the preference type reference data could not be
   *     retrieved
   */
  List<PreferenceType> getPreferenceTypes(String localeId)
      throws InvalidArgumentException, ServiceUnavailableException;

  /**
   * Retrieve the preference type reference data for a specific tenant and locale.
   *
   * @param tenantId the ID for the tenant the preference type reference data is specific to
   * @param localeId the Unicode locale identifier for the locale to retrieve the preference type
   *     reference data for
   * @return the preference type reference data
   * @throws InvalidArgumentException if an argument is invalid
   * @throws ServiceUnavailableException if the preference type reference data could not be
   *     retrieved
   */
  List<PreferenceType> getPreferenceTypes(UUID tenantId, String localeId)
      throws InvalidArgumentException, ServiceUnavailableException;

  /**
   * Retrieve the preference type reference data for all locales.
   *
   * @return the preference type reference data
   * @throws ServiceUnavailableException if the preference type reference data could not be
   *     retrieved
   */
  List<PreferenceType> getPreferenceTypes() throws ServiceUnavailableException;

  /**
   * Retrieve the qualification type reference data for a specific locale.
   *
   * @param localeId the Unicode locale identifier for the locale to retrieve the qualification type
   *     reference data for
   * @return the qualification type reference data
   * @throws InvalidArgumentException if an argument is invalid
   * @throws ServiceUnavailableException if the qualification type reference data could not be
   *     retrieved
   */
  List<QualificationType> getQualificationTypes(String localeId)
      throws InvalidArgumentException, ServiceUnavailableException;

  /**
   * Retrieve the qualification type reference data for a specific tenant and locale.
   *
   * @param tenantId the ID for the tenant the qualification type reference data is specific to
   * @param localeId the Unicode locale identifier for the locale to retrieve the qualification type
   *     reference data for
   * @return the qualification type reference data
   * @throws InvalidArgumentException if an argument is invalid
   * @throws ServiceUnavailableException if the qualification type reference data could not be
   *     retrieved
   */
  List<QualificationType> getQualificationTypes(UUID tenantId, String localeId)
      throws InvalidArgumentException, ServiceUnavailableException;

  /**
   * Retrieve the qualification type reference data for all locales.
   *
   * @return the qualification type reference data
   * @throws ServiceUnavailableException if the qualification type reference data could not be
   *     retrieved
   */
  List<QualificationType> getQualificationTypes() throws ServiceUnavailableException;

  /**
   * Retrieve the race reference data for a specific locale.
   *
   * @param localeId the Unicode locale identifier for the locale to retrieve the race reference
   *     data for
   * @return the race reference data
   * @throws InvalidArgumentException if an argument is invalid
   * @throws ServiceUnavailableException if the race reference data could not be retrieved
   */
  List<Race> getRaces(String localeId) throws InvalidArgumentException, ServiceUnavailableException;

  /**
   * Retrieve the race reference data for a specific tenant and locale.
   *
   * @param tenantId the ID for the tenant the race reference data is specific to
   * @param localeId the Unicode locale identifier for the locale to retrieve the race reference
   *     data for
   * @return the race reference data
   * @throws InvalidArgumentException if an argument is invalid
   * @throws ServiceUnavailableException if the race reference data could not be retrieved
   */
  List<Race> getRaces(UUID tenantId, String localeId)
      throws InvalidArgumentException, ServiceUnavailableException;

  /**
   * Retrieve the race reference data for all locales.
   *
   * @return the race reference data
   * @throws ServiceUnavailableException if the race reference data could not be retrieved
   */
  List<Race> getRaces() throws ServiceUnavailableException;

  /**
   * Retrieve the residence permit type reference data for a specific locale.
   *
   * @param localeId the Unicode locale identifier for the locale to retrieve the residence permit
   *     type reference data for
   * @return the residence permit type reference data
   * @throws InvalidArgumentException if an argument is invalid
   * @throws ServiceUnavailableException if the residence permit type reference data could not be
   *     retrieved
   */
  List<ResidencePermitType> getResidencePermitTypes(String localeId)
      throws InvalidArgumentException, ServiceUnavailableException;

  /**
   * Retrieve the residence permit type reference data for a specific tenant and locale.
   *
   * @param tenantId the ID for the tenant the residence permit type reference data is specific to
   * @param localeId the Unicode locale identifier for the locale to retrieve the residence permit
   *     type reference data for
   * @return the residence permit type reference data
   * @throws InvalidArgumentException if an argument is invalid
   * @throws ServiceUnavailableException if the residence permit type reference data could not be
   *     retrieved
   */
  List<ResidencePermitType> getResidencePermitTypes(UUID tenantId, String localeId)
      throws InvalidArgumentException, ServiceUnavailableException;

  /**
   * Retrieve the residence permit type reference data for all locales.
   *
   * @return the residence permit type reference data
   * @throws ServiceUnavailableException if the residence permit type reference data could not be
   *     retrieved
   */
  List<ResidencePermitType> getResidencePermitTypes() throws ServiceUnavailableException;

  /**
   * Retrieve the residency status reference data for a specific locale.
   *
   * @param localeId the Unicode locale identifier for the locale to retrieve the residency status
   *     reference data for
   * @return the residency status reference data
   * @throws InvalidArgumentException if an argument is invalid
   * @throws ServiceUnavailableException if the residency status reference data could not be
   *     retrieved
   */
  List<ResidencyStatus> getResidencyStatuses(String localeId)
      throws InvalidArgumentException, ServiceUnavailableException;

  /**
   * Retrieve the residency status reference data for a specific tenant and locale.
   *
   * @param tenantId the ID for the tenant the residency status reference data is specific to
   * @param localeId the Unicode locale identifier for the locale to retrieve the residency status
   *     reference data for
   * @return the residency status reference data
   * @throws InvalidArgumentException if an argument is invalid
   * @throws ServiceUnavailableException if the residency status reference data could not be
   *     retrieved
   */
  List<ResidencyStatus> getResidencyStatuses(UUID tenantId, String localeId)
      throws InvalidArgumentException, ServiceUnavailableException;

  /**
   * Retrieve the residency status reference data for all locales.
   *
   * @return the residency status reference data
   * @throws ServiceUnavailableException if the residency status reference data could not be
   *     retrieved
   */
  List<ResidencyStatus> getResidencyStatuses() throws ServiceUnavailableException;

  /**
   * Retrieve the residential type reference data for a specific locale.
   *
   * @param localeId the Unicode locale identifier for the locale to retrieve the residential type
   *     reference data for
   * @return the residential type reference data
   * @throws InvalidArgumentException if an argument is invalid
   * @throws ServiceUnavailableException if the residential type reference data could not be
   *     retrieved
   */
  List<ResidentialType> getResidentialTypes(String localeId)
      throws InvalidArgumentException, ServiceUnavailableException;

  /**
   * Retrieve the residential type reference data for a specific tenant and locale.
   *
   * @param tenantId the ID for the tenant the residential type reference data is specific to
   * @param localeId the Unicode locale identifier for the locale to retrieve the residential type
   *     reference data for
   * @return the residential type reference data
   * @throws InvalidArgumentException if an argument is invalid
   * @throws ServiceUnavailableException if the residential type reference data could not be
   *     retrieved
   */
  List<ResidentialType> getResidentialTypes(UUID tenantId, String localeId)
      throws InvalidArgumentException, ServiceUnavailableException;

  /**
   * Retrieve the residential type reference data for all locales.
   *
   * @return the residential type reference data
   * @throws ServiceUnavailableException if the residential type reference data could not be
   *     retrieved
   */
  List<ResidentialType> getResidentialTypes() throws ServiceUnavailableException;

  /**
   * Retrieve the role purpose reference data for a specific locale.
   *
   * @param localeId the Unicode locale identifier for the locale to retrieve the role purpose
   *     reference data for
   * @return the role purpose reference data
   * @throws InvalidArgumentException if an argument is invalid
   * @throws ServiceUnavailableException if the role purpose reference data could not be retrieved
   */
  List<RolePurpose> getRolePurposes(String localeId)
      throws InvalidArgumentException, ServiceUnavailableException;

  /**
   * Retrieve the role purpose reference data for a specific tenant and locale.
   *
   * @param tenantId the ID for the tenant the role purpose reference data is specific to
   * @param localeId the Unicode locale identifier for the locale to retrieve the role purpose
   *     reference data for
   * @return the role purpose reference data
   * @throws InvalidArgumentException if an argument is invalid
   * @throws ServiceUnavailableException if the role purpose reference data could not be retrieved
   */
  List<RolePurpose> getRolePurposes(UUID tenantId, String localeId)
      throws InvalidArgumentException, ServiceUnavailableException;

  /**
   * Retrieve the role purpose reference data for all locales.
   *
   * @return the role purpose reference data
   * @throws ServiceUnavailableException if the role purpose reference data could not be retrieved
   */
  List<RolePurpose> getRolePurposes() throws ServiceUnavailableException;

  /**
   * Retrieve the role type attribute type constraints for all role types.
   *
   * @return the role type attribute type constraints
   * @throws ServiceUnavailableException if the role type attribute type constraints for all role
   *     types could not be retrieved
   */
  List<RoleTypeAttributeTypeConstraint> getRoleTypeAttributeTypeConstraints()
      throws ServiceUnavailableException;

  /**
   * Retrieve the role type attribute type constraints for a specific role type.
   *
   * @param roleType the code for the role type to retrieve the attribute constraints for
   * @return the role type attribute type constraints
   * @throws InvalidArgumentException if an argument is invalid
   * @throws ServiceUnavailableException if the role type attribute type constraints for a specific
   *     role type could not be retrieved
   */
  List<RoleTypeAttributeTypeConstraint> getRoleTypeAttributeTypeConstraints(String roleType)
      throws InvalidArgumentException, ServiceUnavailableException;

  /**
   * Retrieve the role type preference type constraints for all role types.
   *
   * @return the role type preference type constraints
   * @throws ServiceUnavailableException if the role type preference type constraints for all role
   *     types could not be retrieved
   */
  List<RoleTypePreferenceTypeConstraint> getRoleTypePreferenceTypeConstraints()
      throws ServiceUnavailableException;

  /**
   * Retrieve the role type preference type constraints for a specific role type.
   *
   * @param roleType the code for the role type to retrieve the preference constraints for
   * @return the role type preference type constraints
   * @throws InvalidArgumentException if an argument is invalid
   * @throws ServiceUnavailableException if the role type preference type constraints for a specific
   *     role type could not be retrieved
   */
  List<RoleTypePreferenceTypeConstraint> getRoleTypePreferenceTypeConstraints(String roleType)
      throws InvalidArgumentException, ServiceUnavailableException;

  /**
   * Retrieve the role type reference data for a specific locale.
   *
   * @param localeId the Unicode locale identifier for the locale to retrieve the role type
   *     reference data for
   * @return the role type reference data
   * @throws InvalidArgumentException if an argument is invalid
   * @throws ServiceUnavailableException if the role type reference data could not be retrieved
   */
  List<RoleType> getRoleTypes(String localeId)
      throws InvalidArgumentException, ServiceUnavailableException;

  /**
   * Retrieve the role type reference data for a specific tenant and locale.
   *
   * @param tenantId the ID for the tenant the role type reference data is specific to
   * @param localeId the Unicode locale identifier for the locale to retrieve the role type
   *     reference data for
   * @return the role type reference data
   * @throws InvalidArgumentException if an argument is invalid
   * @throws ServiceUnavailableException if the role type reference data could not be retrieved
   */
  List<RoleType> getRoleTypes(UUID tenantId, String localeId)
      throws InvalidArgumentException, ServiceUnavailableException;

  /**
   * Retrieve the role type reference data for all locales.
   *
   * @return the role type reference data
   * @throws ServiceUnavailableException if the role type reference data could not be retrieved
   */
  List<RoleType> getRoleTypes() throws ServiceUnavailableException;

  /**
   * Retrieve the segmentation type reference data for a specific tenant and locale.
   *
   * @param tenantId the ID for the tenant the segmentation type reference data is specific to
   * @param localeId the Unicode locale identifier for the locale to retrieve the segmentation type
   *     reference data for
   * @return the segmentation type reference data
   * @throws InvalidArgumentException if an argument is invalid
   * @throws ServiceUnavailableException if the segmentation type reference data could not be
   *     retrieved
   */
  List<SegmentationType> getSegmentationTypes(UUID tenantId, String localeId)
      throws InvalidArgumentException, ServiceUnavailableException;

  /**
   * Retrieve the segmentation type reference data for a specific locale.
   *
   * @param localeId the Unicode locale identifier for the locale to retrieve the segmentation type
   *     reference data for
   * @return the segmentation type reference data
   * @throws InvalidArgumentException if an argument is invalid
   * @throws ServiceUnavailableException if the segmentation type reference data could not be
   *     retrieved
   */
  List<SegmentationType> getSegmentationTypes(String localeId)
      throws InvalidArgumentException, ServiceUnavailableException;

  /**
   * Retrieve the segment reference data for a specific locale.
   *
   * @param localeId the Unicode locale identifier for the locale to retrieve the segment reference
   *     data for
   * @return the segment reference data
   * @throws InvalidArgumentException if an argument is invalid
   * @throws ServiceUnavailableException if the segment reference data could not be retrieved
   */
  List<Segment> getSegments(String localeId)
      throws InvalidArgumentException, ServiceUnavailableException;

  /**
   * Retrieve the segment reference data for a specific tenant and locale.
   *
   * @param tenantId the ID for the tenant the segment reference data is specific to
   * @param localeId the Unicode locale identifier for the locale to retrieve the segment reference
   *     data for
   * @return the segment reference data
   * @throws InvalidArgumentException if an argument is invalid
   * @throws ServiceUnavailableException if the segment reference data could not be retrieved
   */
  List<Segment> getSegments(UUID tenantId, String localeId)
      throws InvalidArgumentException, ServiceUnavailableException;

  /**
   * Retrieve the segment reference data for all locales.
   *
   * @return the segment reference data
   * @throws ServiceUnavailableException if the segment reference data could not be retrieved
   */
  List<Segment> getSegments() throws ServiceUnavailableException;

  /**
   * Retrieve the source of funds type reference data for a specific locale.
   *
   * @param localeId the Unicode locale identifier for the locale to retrieve the source of funds
   *     type reference data
   * @return the source of funds type reference data
   * @throws InvalidArgumentException if an argument is invalid
   * @throws ServiceUnavailableException if the source of funds type reference data could not be
   *     retrieved
   */
  List<SourceOfFundsType> getSourceOfFundsTypes(String localeId)
      throws InvalidArgumentException, ServiceUnavailableException;

  /**
   * Retrieve the source of funds type reference data for a specific tenant and locale.
   *
   * @param tenantId the ID for the tenant the source of funds type reference data is specific to
   * @param localeId the Unicode locale identifier for the locale to retrieve the source of funds
   *     type reference data
   * @return the source of funds type reference data
   * @throws InvalidArgumentException if an argument is invalid
   * @throws ServiceUnavailableException if the source of funds type reference data could not be
   *     retrieved
   */
  List<SourceOfFundsType> getSourceOfFundsTypes(UUID tenantId, String localeId)
      throws InvalidArgumentException, ServiceUnavailableException;

  /**
   * Retrieve the source of funds type reference data for all locales.
   *
   * @return the source of funds type reference data
   * @throws ServiceUnavailableException if the source of funds type reference data could not be
   *     retrieved
   */
  List<SourceOfFundsType> getSourceOfFundsTypes() throws ServiceUnavailableException;

  /**
   * Retrieve the source of wealth type reference data for a specific locale.
   *
   * @param localeId the Unicode locale identifier for the locale to retrieve the source of wealth
   *     type reference data for
   * @return the source of wealth type reference data
   * @throws InvalidArgumentException if an argument is invalid
   * @throws ServiceUnavailableException if the source of wealth type reference data could not be
   *     retrieved
   */
  List<SourceOfWealthType> getSourceOfWealthTypes(String localeId)
      throws InvalidArgumentException, ServiceUnavailableException;

  /**
   * Retrieve the source of wealth type reference data for a specific tenant and locale.
   *
   * @param tenantId the ID for the tenant the source of wealth type reference data is specific to
   * @param localeId the Unicode locale identifier for the locale to retrieve the source of wealth
   *     type reference data for
   * @return the source of wealth type reference data
   * @throws InvalidArgumentException if an argument is invalid
   * @throws ServiceUnavailableException if the source of wealth type reference data could not be
   *     retrieved
   */
  List<SourceOfWealthType> getSourceOfWealthTypes(UUID tenantId, String localeId)
      throws InvalidArgumentException, ServiceUnavailableException;

  /**
   * Retrieve the source of wealth type reference data for all locales.
   *
   * @return the source of wealth type reference data
   * @throws ServiceUnavailableException if the source of wealth type reference data could not be
   *     retrieved
   */
  List<SourceOfWealthType> getSourceOfWealthTypes() throws ServiceUnavailableException;

  /**
   * Retrieve the status type category reference data for a specific locale.
   *
   * @param localeId the Unicode locale identifier for the locale to retrieve the status type
   *     category reference data for
   * @return the status type category reference data
   * @throws InvalidArgumentException if an argument is invalid
   * @throws ServiceUnavailableException if the status type category reference data could not be
   *     retrieved
   */
  List<StatusTypeCategory> getStatusTypeCategories(String localeId)
      throws InvalidArgumentException, ServiceUnavailableException;

  /**
   * Retrieve the status type category reference data for a specific tenant and locale.
   *
   * @param tenantId the ID for the tenant the status type category reference data is specific to
   * @param localeId the Unicode locale identifier for the locale to retrieve the status type
   *     category reference data for
   * @return the status type category reference data
   * @throws InvalidArgumentException if an argument is invalid
   * @throws ServiceUnavailableException if the status type category reference data could not be
   *     retrieved
   */
  List<StatusTypeCategory> getStatusTypeCategories(UUID tenantId, String localeId)
      throws InvalidArgumentException, ServiceUnavailableException;

  /**
   * Retrieve the status type category reference data for all locales.
   *
   * @return the status type category reference data
   * @throws ServiceUnavailableException if the status type category reference data could not be
   *     retrieved
   */
  List<StatusTypeCategory> getStatusTypeCategories() throws ServiceUnavailableException;

  /**
   * Retrieve the status type reference data for a specific locale.
   *
   * @param localeId the Unicode locale identifier for the locale to retrieve the status type
   *     reference data for
   * @return the status type reference data
   * @throws InvalidArgumentException if an argument is invalid
   * @throws ServiceUnavailableException if the status type reference data could not be retrieved
   */
  List<StatusType> getStatusTypes(String localeId)
      throws InvalidArgumentException, ServiceUnavailableException;

  /**
   * Retrieve the status type reference data for a specific tenant and locale.
   *
   * @param tenantId the ID for the tenant the status type reference data is specific to
   * @param localeId the Unicode locale identifier for the locale to retrieve the status type
   *     reference data for
   * @return the status type reference data
   * @throws InvalidArgumentException if an argument is invalid
   * @throws ServiceUnavailableException if the status type reference data could not be retrieved
   */
  List<StatusType> getStatusTypes(UUID tenantId, String localeId)
      throws InvalidArgumentException, ServiceUnavailableException;

  /**
   * Retrieve the status type reference data for all locales.
   *
   * @return the status type reference data
   * @throws ServiceUnavailableException if the status type reference data could not be retrieved
   */
  List<StatusType> getStatusTypes() throws ServiceUnavailableException;

  /**
   * Retrieve the tax number type reference data for a specific locale.
   *
   * @param localeId the Unicode locale identifier for the locale to retrieve the tax number type
   *     reference data for
   * @return the tax number type reference data
   * @throws InvalidArgumentException if an argument is invalid
   * @throws ServiceUnavailableException if the tax number type reference data could not be
   *     retrieved
   */
  List<TaxNumberType> getTaxNumberTypes(String localeId)
      throws InvalidArgumentException, ServiceUnavailableException;

  /**
   * Retrieve the tax number type reference data for a specific tenant and locale.
   *
   * @param tenantId the ID for the tenant the tax number type reference data is specific to
   * @param localeId the Unicode locale identifier for the locale to retrieve the tax number type
   *     reference data for
   * @return the tax number type reference data
   * @throws InvalidArgumentException if an argument is invalid
   * @throws ServiceUnavailableException if the tax number type reference data could not be
   *     retrieved
   */
  List<TaxNumberType> getTaxNumberTypes(UUID tenantId, String localeId)
      throws InvalidArgumentException, ServiceUnavailableException;

  /**
   * Retrieve the tax number type reference data for all locales.
   *
   * @return the tax number type reference data
   * @throws ServiceUnavailableException if the tax number type reference data could not be
   *     retrieved
   */
  List<TaxNumberType> getTaxNumberTypes() throws ServiceUnavailableException;

  /**
   * Retrieve the times to contact reference data for a specific locale.
   *
   * @param localeId the Unicode locale identifier for the locale to retrieve the times to contact
   *     reference data for
   * @return the times to contact reference data
   * @throws InvalidArgumentException if an argument is invalid
   * @throws ServiceUnavailableException if the times to contact reference data could not be
   *     retrieved
   */
  List<TimeToContact> getTimesToContact(String localeId)
      throws InvalidArgumentException, ServiceUnavailableException;

  /**
   * Retrieve the times to contact reference data for a specific tenant and locale.
   *
   * @param tenantId the ID for the tenant the times to contact reference data is specific to
   * @param localeId the Unicode locale identifier for the locale to retrieve the times to contact
   *     reference data for
   * @return the times to contact reference data
   * @throws InvalidArgumentException if an argument is invalid
   * @throws ServiceUnavailableException if the times to contact reference data could not be
   *     retrieved
   */
  List<TimeToContact> getTimesToContact(UUID tenantId, String localeId)
      throws InvalidArgumentException, ServiceUnavailableException;

  /**
   * Retrieve the times to contact reference data for all locales.
   *
   * @return the times to contact reference data
   * @throws ServiceUnavailableException if the times to contact reference data could not be
   *     retrieved
   */
  List<TimeToContact> getTimesToContact() throws ServiceUnavailableException;

  /**
   * Retrieve the title reference data for a specific locale.
   *
   * @param localeId the Unicode locale identifier for the locale to retrieve the title reference
   *     data for
   * @return the title reference data
   * @throws InvalidArgumentException if an argument is invalid
   * @throws ServiceUnavailableException if the title reference data could not be retrieved
   */
  List<Title> getTitles(String localeId)
      throws InvalidArgumentException, ServiceUnavailableException;

  /**
   * Retrieve the title reference data for a specific tenant and locale.
   *
   * @param tenantId the ID for the tenant the title reference data is specific to
   * @param localeId the Unicode locale identifier for the locale to retrieve the title reference
   *     data for
   * @return the title reference data
   * @throws InvalidArgumentException if an argument is invalid
   * @throws ServiceUnavailableException if the title reference data could not be retrieved
   */
  List<Title> getTitles(UUID tenantId, String localeId)
      throws InvalidArgumentException, ServiceUnavailableException;

  /**
   * Retrieve the title reference data for all locales.
   *
   * @return the title reference data
   * @throws ServiceUnavailableException if the title reference data could not be retrieved
   */
  List<Title> getTitles() throws ServiceUnavailableException;

  /**
   * Check whether the code is a valid code for an association property type for the association
   * type.
   *
   * @param tenantId the ID for the tenant
   * @param associationTypeCode the code for the association type
   * @param associationPropertyTypeCode the code for the association property type
   * @return <b>true</b> if the code is a valid code for an association property type or
   *     <b>false</b> otherwise
   * @throws ServiceUnavailableException if the association property type check failed
   */
  boolean isValidAssociationPropertyType(
      UUID tenantId, String associationTypeCode, String associationPropertyTypeCode)
      throws ServiceUnavailableException;

  /**
   * Check whether the code is a valid code for an association type.
   *
   * @param tenantId the ID for the tenant
   * @param associationTypeCode the code for the association type
   * @return <b>true</b> if the code is a valid code for an association type or <b>false</b>
   *     otherwise
   * @throws ServiceUnavailableException if the association type check failed
   */
  boolean isValidAssociationType(UUID tenantId, String associationTypeCode)
      throws ServiceUnavailableException;

  /**
   * Check whether the code is a valid code for an attribute type for the party type.
   *
   * @param tenantId the ID for the tenant
   * @param partyTypeCode the code for the party type
   * @param attributeTypeCode the code for the attribute type
   * @return <b>true</b> if the code is a valid code for an attribute type or <b>false</b> otherwise
   * @throws ServiceUnavailableException if the attribute type check failed
   */
  boolean isValidAttributeType(UUID tenantId, String partyTypeCode, String attributeTypeCode)
      throws ServiceUnavailableException;

  /**
   * Check whether the code is a valid code for an attribute type category.
   *
   * @param tenantId the ID for the tenant
   * @param attributeTypeCategoryCode the code for the attribute type category
   * @return <b>true</b> if the code is a valid code for an attribute type category or <b>false</b>
   *     otherwise
   * @throws ServiceUnavailableException if the attribute type category check failed
   */
  boolean isValidAttributeTypeCategory(UUID tenantId, String attributeTypeCategoryCode)
      throws ServiceUnavailableException;

  /**
   * Check whether the code is a valid code for a consent type.
   *
   * @param tenantId the ID for the tenant
   * @param consentTypeCode the code for the consent type
   * @return <b>true</b> if the code is a valid code for a consent type or <b>false</b> otherwise
   * @throws ServiceUnavailableException if the consent type check failed
   */
  boolean isValidConsentType(UUID tenantId, String consentTypeCode)
      throws ServiceUnavailableException;

  /**
   * Check whether the code is a valid code for a contact mechanism purpose for the party type.
   *
   * @param tenantId the ID for the tenant
   * @param partyTypeCode the code for the party type
   * @param contactMechanismTypeCode the code for the contact mechanism type
   * @param contactMechanismPurposeCode the code for the contact mechanism purpose
   * @return <b>true</b> if the code is a valid code for a contact mechanism purpose or <b>false</b>
   *     otherwise
   * @throws ServiceUnavailableException if the contact mechanism purpose check failed
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
   * @param tenantId the ID for the tenant
   * @param partyTypeCode the code for the party type
   * @param contactMechanismTypeCode the code for the contact mechanism type
   * @param contactMechanismRoleCode the code for the contact mechanism role
   * @return <b>true</b> if the code is a valid code for a contact mechanism role or <b>false</b>
   *     otherwise
   * @throws ServiceUnavailableException if the contact mechanism role check failed
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
   * @param tenantId the ID for the tenant
   * @param contactMechanismTypeCode the code for the contact mechanism type
   * @return <b>true</b> if the code is a valid code for a contact mechanism type or <b>false</b>
   *     otherwise
   * @throws ServiceUnavailableException if the contact mechanism type check failed
   */
  boolean isValidContactMechanismType(UUID tenantId, String contactMechanismTypeCode)
      throws ServiceUnavailableException;

  /**
   * Check whether the code is a valid code for an employment status.
   *
   * @param tenantId the ID for the tenant
   * @param employmentStatusCode the code for the employment status
   * @return <b>true</b> if the code is a valid code for an employment status or <b>false</b>
   *     otherwise
   * @throws ServiceUnavailableException if the employment status check failed
   */
  boolean isValidEmploymentStatus(UUID tenantId, String employmentStatusCode)
      throws ServiceUnavailableException;

  /**
   * Check whether the code is a valid code for an employment type.
   *
   * @param tenantId the ID for the tenant
   * @param employmentStatusCode the code for the employment status
   * @param employmentTypeCode the code for the employment type
   * @return <b>true</b> if the code is a valid code for an employment type or <b>false</b>
   *     otherwise
   * @throws ServiceUnavailableException if the employment type check failed
   */
  boolean isValidEmploymentType(
      UUID tenantId, String employmentStatusCode, String employmentTypeCode)
      throws ServiceUnavailableException;

  /**
   * Check whether the code is a valid code for an employment type.
   *
   * @param tenantId the ID for the tenant
   * @param employmentTypeCode the code for the employment type
   * @return <b>true</b> if the code is a valid code for an employment type or <b>false</b>
   *     otherwise
   * @throws ServiceUnavailableException if the employment type check failed
   */
  boolean isValidEmploymentType(UUID tenantId, String employmentTypeCode)
      throws ServiceUnavailableException;

  /**
   * Check whether the external reference is valid.
   *
   * @param tenantId the ID for the tenant
   * @param partyTypeCode the code for the party type
   * @param externalReferenceTypeCode the code for the external reference type
   * @param value the value for the external reference
   * @return <b>true</b> if the external reference is valid or <b>false</b> otherwise
   * @throws ServiceUnavailableException if the external reference check failed
   */
  boolean isValidExternalReference(
      UUID tenantId, String partyTypeCode, String externalReferenceTypeCode, String value)
      throws ServiceUnavailableException;

  /**
   * Check whether the code is a valid code for an external reference type for the party type.
   *
   * @param tenantId the ID for the tenant
   * @param partyTypeCode the code for the party type
   * @param externalReferenceTypeCode the code for the external reference type
   * @return <b>true</b> if the code is a valid code for an external reference type or <b>false</b>
   *     otherwise
   * @throws ServiceUnavailableException if the external reference type check failed
   */
  boolean isValidExternalReferenceType(
      UUID tenantId, String partyTypeCode, String externalReferenceTypeCode)
      throws ServiceUnavailableException;

  /**
   * Check whether the code is a valid code for a field of study.
   *
   * @param tenantId the ID for the tenant
   * @param fieldOfStudyCode the code for the field of study
   * @return <b>true</b> if the code is a valid code for a field of study or <b>false</b> otherwise
   * @throws ServiceUnavailableException if the field of study check failed
   */
  boolean isValidFieldOfStudy(UUID tenantId, String fieldOfStudyCode)
      throws ServiceUnavailableException;

  /**
   * Check whether the code is a valid code for a gender.
   *
   * @param tenantId the ID for the tenant
   * @param genderCode the code for the gender
   * @return <b>true</b> if the code is a valid code for a gender or <b>false</b> otherwise
   * @throws ServiceUnavailableException if the gender check failed
   */
  boolean isValidGender(UUID tenantId, String genderCode) throws ServiceUnavailableException;

  /**
   * Check whether the identity document is valid.
   *
   * @param tenantId the ID for the tenant
   * @param partyTypeCode the code for the party type
   * @param identityDocumentTypeCode the code for the identity document type
   * @param number the number for the identity document
   * @return <b>true</b> if the identity document is valid or <b>false</b> otherwise
   * @throws ServiceUnavailableException if the identity document check failed
   */
  boolean isValidIdentityDocument(
      UUID tenantId, String partyTypeCode, String identityDocumentTypeCode, String number)
      throws ServiceUnavailableException;

  /**
   * Check whether the code is a valid code for an identity document type for the party type.
   *
   * @param tenantId the ID for the tenant
   * @param partyTypeCode the code for the party type
   * @param identityDocumentTypeCode the code for the identity document type
   * @return <b>true</b> if the code is a valid code for an identity document type or <b>false</b>
   *     otherwise
   * @throws ServiceUnavailableException if the identity document type check failed
   */
  boolean isValidIdentityDocumentType(
      UUID tenantId, String partyTypeCode, String identityDocumentTypeCode)
      throws ServiceUnavailableException;

  /**
   * Check whether the code is a valid code for a lock type for the party type.
   *
   * @param tenantId the ID for the tenant
   * @param partyTypeCode the code for the party type
   * @param lockTypeCode the code for the lock type
   * @return <b>true</b> if the code is a valid code for a lock type or <b>false</b> otherwise
   * @throws ServiceUnavailableException if the lock type check failed
   */
  boolean isValidLockType(UUID tenantId, String partyTypeCode, String lockTypeCode)
      throws ServiceUnavailableException;

  /**
   * Check whether the code is a valid code for a lock type category.
   *
   * @param tenantId the ID for the tenant
   * @param lockTypeCategoryCode the code for the lock type category
   * @return <b>true</b> if the code is a valid code for a lock type category or <b>false</b>
   *     otherwise
   * @throws ServiceUnavailableException if the lock type category check failed
   */
  boolean isValidLockTypeCategory(UUID tenantId, String lockTypeCategoryCode)
      throws ServiceUnavailableException;

  /**
   * Check whether the code is a valid code for a mandate type.
   *
   * @param tenantId the ID for the tenant
   * @param mandateTypeCode the code for the mandate type
   * @return <b>true</b> if the code is a valid code for a mandate type or <b>false</b> otherwise
   * @throws ServiceUnavailableException if the mandate type check failed
   */
  boolean isValidMandateType(UUID tenantId, String mandateTypeCode)
      throws ServiceUnavailableException;

  /**
   * Check whether the code is a valid code for a marital status.
   *
   * @param tenantId the ID for the tenant
   * @param maritalStatusCode the code for the marital status
   * @return <b>true</b> if the code is a valid code for a marital status or <b>false</b> otherwise
   * @throws ServiceUnavailableException if the marital status check failed
   */
  boolean isValidMaritalStatus(UUID tenantId, String maritalStatusCode)
      throws ServiceUnavailableException;

  /**
   * Check whether the code is a valid code for a marriage type.
   *
   * @param tenantId the ID for the tenant
   * @param maritalStatusCode the code for the marital status
   * @param marriageTypeCode the code for the marriage type
   * @return <b>true</b> if the code is a valid code for a marriage type or <b>false</b> otherwise
   * @throws ServiceUnavailableException if the marriage type check failed
   */
  boolean isValidMarriageType(UUID tenantId, String maritalStatusCode, String marriageTypeCode)
      throws ServiceUnavailableException;

  /**
   * Check whether the measurement unit is valid for the attribute type with the specified code.
   *
   * @param tenantId the ID for the tenant
   * @param attributeTypeCode the code for the attribute type
   * @param measurementUnit the measurement unit
   * @return <b>true</b> if the measurement unit is valid for the attribute type with the specified
   *     code or <b>false</b> otherwise
   * @throws ServiceUnavailableException if the measurement unit for attribute type check failed
   */
  boolean isValidMeasurementUnitForAttributeType(
      UUID tenantId, String attributeTypeCode, MeasurementUnit measurementUnit)
      throws ServiceUnavailableException;

  /**
   * Check whether the code is a valid code for a next of kin type.
   *
   * @param tenantId the ID for the tenant
   * @param nextOfKinTypeCode the code for the next of kin type
   * @return <b>true</b> if the code is a valid code for a next of kin type or <b>false</b>
   *     otherwise
   * @throws ServiceUnavailableException if the next of kin type check failed
   */
  boolean isValidNextOfKinType(UUID tenantId, String nextOfKinTypeCode)
      throws ServiceUnavailableException;

  /**
   * Check whether the code is a valid code for an occupation.
   *
   * @param tenantId the ID for the tenant
   * @param occupationCode the code for the occupation
   * @return <b>true</b> if the code is a valid code for an occupation or <b>false</b> otherwise
   * @throws ServiceUnavailableException if the occupation check failed
   */
  boolean isValidOccupation(UUID tenantId, String occupationCode)
      throws ServiceUnavailableException;

  /**
   * Check whether the code is a valid code for a physical address purpose for the party type.
   *
   * @param tenantId the ID for the tenant
   * @param partyTypeCode the code for the party type
   * @param physicalAddressPurposeCode the code for the physical address purpose
   * @return <b>true</b> if the code is a valid code for a physical address purpose or <b>false</b>
   *     otherwise
   * @throws ServiceUnavailableException if the physical address purpose check failed
   */
  boolean isValidPhysicalAddressPurpose(
      UUID tenantId, String partyTypeCode, String physicalAddressPurposeCode)
      throws ServiceUnavailableException;

  /**
   * Check whether the code is a valid code for a physical address purpose.
   *
   * @param tenantId the ID for the tenant
   * @param physicalAddressPurposeCode the code for the physical address purpose
   * @return <b>true</b> if the code is a valid code for a physical address purpose or <b>false</b>
   *     otherwise
   * @throws ServiceUnavailableException if the physical address purpose check failed
   */
  boolean isValidPhysicalAddressPurpose(UUID tenantId, String physicalAddressPurposeCode)
      throws ServiceUnavailableException;

  /**
   * Check whether the code is a valid code for a physical address role for the party type.
   *
   * @param tenantId the ID for the tenant
   * @param partyTypeCode the code for the party type
   * @param physicalAddressRoleCode the code for the physical address role
   * @return <b>true</b> if the code is a valid code for a physical address role or <b>false</b>
   *     otherwise
   * @throws ServiceUnavailableException if the physical address role check failed
   */
  boolean isValidPhysicalAddressRole(
      UUID tenantId, String partyTypeCode, String physicalAddressRoleCode)
      throws ServiceUnavailableException;

  /**
   * Check whether the code is a valid code for a physical address role for the party type.
   *
   * @param tenantId the ID for the tenant
   * @param physicalAddressRoleCode the code for the physical address role
   * @return <b>true</b> if the code is a valid code for a physical address role or <b>false</b>
   *     otherwise
   * @throws ServiceUnavailableException if the physical address role check failed
   */
  boolean isValidPhysicalAddressRole(UUID tenantId, String physicalAddressRoleCode)
      throws ServiceUnavailableException;

  /**
   * Check whether the code is a valid code for a physical address type.
   *
   * @param tenantId the ID for the tenant
   * @param physicalAddressTypeCode the code for the physical address type
   * @return <b>true</b> if the code is a valid code for a physical address type or <b>false</b>
   *     otherwise
   * @throws ServiceUnavailableException if the physical address type check failed
   */
  boolean isValidPhysicalAddressType(UUID tenantId, String physicalAddressTypeCode)
      throws ServiceUnavailableException;

  /**
   * Check whether the code is a valid code for a preference type for the party type.
   *
   * @param tenantId the ID for the tenant
   * @param partyTypeCode the code for the party type
   * @param preferenceTypeCode the code for the preference type
   * @return <b>true</b> if the code is a valid code for a preference type or <b>false</b> otherwise
   * @throws ServiceUnavailableException if the preference type check failed
   */
  boolean isValidPreferenceType(UUID tenantId, String partyTypeCode, String preferenceTypeCode)
      throws ServiceUnavailableException;

  /**
   * Check whether the code is a valid code for a preference type category.
   *
   * @param tenantId the ID for the tenant
   * @param preferenceTypeCategoryCode the code for the preference type category
   * @return <b>true</b> if the code is a valid code for a preference type category or <b>false</b>
   *     otherwise
   * @throws ServiceUnavailableException if the preference type category check failed
   */
  boolean isValidPreferenceTypeCategory(UUID tenantId, String preferenceTypeCategoryCode)
      throws ServiceUnavailableException;

  /**
   * Check whether the code is a valid code for a qualification type.
   *
   * @param tenantId the ID for the tenant
   * @param qualificationTypeCode the code for the qualification type
   * @return <b>true</b> if the code is a valid code for a qualification type or <b>false</b>
   *     otherwise
   * @throws ServiceUnavailableException if the qualification type check failed
   */
  boolean isValidQualificationType(UUID tenantId, String qualificationTypeCode)
      throws ServiceUnavailableException;

  /**
   * Check whether the code is a valid code for a race.
   *
   * @param tenantId the ID for the tenant
   * @param raceCode the code for the race
   * @return <b>true</b> if the code is a valid code for a race or <b>false</b> otherwise
   * @throws ServiceUnavailableException if the race check failed
   */
  boolean isValidRace(UUID tenantId, String raceCode) throws ServiceUnavailableException;

  /**
   * Check whether the residence permit is valid.
   *
   * @param tenantId the ID for the tenant
   * @param partyTypeCode the code for the party type
   * @param residencePermitTypeCode the code for the residence permit type
   * @param number the number for the residence permit
   * @return <b>true</b> if the residence permit is valid or <b>false</b> otherwise
   * @throws ServiceUnavailableException if the residence permit check failed
   */
  boolean isValidResidencePermit(
      UUID tenantId, String partyTypeCode, String residencePermitTypeCode, String number)
      throws ServiceUnavailableException;

  /**
   * Check whether the code is a valid code for a residence permit type.
   *
   * @param tenantId the ID for the tenant
   * @param residencePermitTypeCode the code for the residence permit type
   * @return <b>true</b> if the code is a valid code for a residence permit type or <b>false</b>
   *     otherwise
   * @throws ServiceUnavailableException if the residence permit type check failed
   */
  boolean isValidResidencePermitType(UUID tenantId, String residencePermitTypeCode)
      throws ServiceUnavailableException;

  /**
   * Check whether the code is a valid code for a residency status.
   *
   * @param tenantId the ID for the tenant
   * @param residencyStatusCode the code for the residency status
   * @return <b>true</b> if the code is a valid code for a residency status or <b>false</b>
   *     otherwise
   * @throws ServiceUnavailableException if the residency status check failed
   */
  boolean isValidResidencyStatus(UUID tenantId, String residencyStatusCode)
      throws ServiceUnavailableException;

  /**
   * Check whether the code is a valid code for a residential type.
   *
   * @param tenantId the ID for the tenant
   * @param residentialTypeCode the code for the residential type
   * @return <b>true</b> if the code is a valid code for a residential type or <b>false</b>
   *     otherwise
   * @throws ServiceUnavailableException if the residential type check failed
   */
  boolean isValidResidentialType(UUID tenantId, String residentialTypeCode)
      throws ServiceUnavailableException;

  /**
   * Check whether the code is a valid code for a role purpose.
   *
   * @param tenantId the ID for the tenant
   * @param rolePurposeCode the code for the role purpose
   * @return <b>true</b> if the code is a valid code for a role purpose or <b>false</b> otherwise
   * @throws ServiceUnavailableException if the role purpose check failed
   */
  boolean isValidRolePurpose(UUID tenantId, String rolePurposeCode)
      throws ServiceUnavailableException;

  /**
   * Check whether the code is a valid code for a role type for the party type.
   *
   * @param tenantId the ID for the tenant
   * @param partyTypeCode the code for the party type
   * @param roleTypeCode the code for the role type
   * @return <b>true</b> if the code is a valid code for a role type or <b>false</b> otherwise
   * @throws ServiceUnavailableException if the role type check failed
   */
  boolean isValidRoleType(UUID tenantId, String partyTypeCode, String roleTypeCode)
      throws ServiceUnavailableException;

  /**
   * Check whether the code is a valid code for a segment.
   *
   * @param tenantId the ID for the tenant
   * @param segmentCode the code for the segment
   * @return <b>true</b> if the code is a valid code for a segment or <b>false</b> otherwise
   * @throws ServiceUnavailableException if the segment check failed
   */
  boolean isValidSegment(UUID tenantId, String segmentCode) throws ServiceUnavailableException;

  /**
   * Check whether the code is a valid code for a source of funds type.
   *
   * @param tenantId the ID for the tenant
   * @param sourceOfFundsTypeCode the code for the source of funds type
   * @return <b>true</b> if the code is a valid code for a source of funds type or <b>false</b>
   *     otherwise
   * @throws ServiceUnavailableException if the source of funds type check failed
   */
  boolean isValidSourceOfFundsType(UUID tenantId, String sourceOfFundsTypeCode)
      throws ServiceUnavailableException;

  /**
   * Check whether the code is a valid code for a source of wealth type.
   *
   * @param tenantId the ID for the tenant
   * @param sourceOfWealthTypeCode the code for the source of wealth type
   * @return <b>true</b> if the code is a valid code for a source of wealth type or <b>false</b>
   *     otherwise
   * @throws ServiceUnavailableException if the source of wealth type check failed
   */
  boolean isValidSourceOfWealthType(UUID tenantId, String sourceOfWealthTypeCode)
      throws ServiceUnavailableException;

  /**
   * Check whether the code is a valid code for a status type for the party type.
   *
   * @param tenantId the ID for the tenant
   * @param partyTypeCode the code for the party type
   * @param statusTypeCode the code for the status type
   * @return <b>true</b> if the code is a valid code for a status type or <b>false</b> otherwise
   * @throws ServiceUnavailableException if the status type check failed
   */
  boolean isValidStatusType(UUID tenantId, String partyTypeCode, String statusTypeCode)
      throws ServiceUnavailableException;

  /**
   * Check whether the code is a valid code for a status type category.
   *
   * @param tenantId the ID for the tenant
   * @param statusTypeCategoryCode the code for the status type category
   * @return <b>true</b> if the code is a valid code for a status type category or <b>false</b>
   *     otherwise
   * @throws ServiceUnavailableException if the status type category check failed
   */
  boolean isValidStatusTypeCategory(UUID tenantId, String statusTypeCategoryCode)
      throws ServiceUnavailableException;

  /**
   * Check whether the tax number is valid.
   *
   * @param tenantId the ID for the tenant
   * @param partyTypeCode the code for the party type
   * @param taxNumberTypeCode the code for the tax number type
   * @param number the number for the tax number
   * @return <b>true</b> if the tax number is valid or <b>false</b> otherwise
   * @throws ServiceUnavailableException if the tax number check failed
   */
  boolean isValidTaxNumber(
      UUID tenantId, String partyTypeCode, String taxNumberTypeCode, String number)
      throws ServiceUnavailableException;

  /**
   * Check whether the code is a valid code for a tax number type.
   *
   * @param tenantId the ID for the tenant
   * @param partyTypeCode the code for the party type
   * @param taxNumberTypeCode the code for the tax number type
   * @return <b>true</b> if the code is a valid code for a tax number type or <b>false</b> otherwise
   * @throws ServiceUnavailableException if the tax number type check failed
   */
  boolean isValidTaxNumberType(UUID tenantId, String partyTypeCode, String taxNumberTypeCode)
      throws ServiceUnavailableException;

  /**
   * Check whether the code is a valid code for a time to contact.
   *
   * @param tenantId the ID for the tenant
   * @param timeToContactCode the code for the time to contact
   * @return <b>true</b> if the code is a valid code for a time to contact or <b>false</b> otherwise
   * @throws ServiceUnavailableException if the time to contact check failed
   */
  boolean isValidTimeToContact(UUID tenantId, String timeToContactCode)
      throws ServiceUnavailableException;

  /**
   * Check whether the code is a valid code for a title.
   *
   * @param tenantId the ID for the tenant
   * @param titleCode the code for the title
   * @return <b>true</b> if the code is a valid code for a title or <b>false</b> otherwise
   * @throws ServiceUnavailableException if the title check failed
   */
  boolean isValidTitle(UUID tenantId, String titleCode) throws ServiceUnavailableException;
}
