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
import java.util.UUID;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.xml.bind.annotation.XmlElement;

/**
 * The <b>PartyReferenceWebService</b> class.
 *
 * @author Marcus Portmann
 */
@WebService(
    serviceName = "PartyReferenceService",
    name = "IPartyReferenceService",
    targetNamespace = "http://inception.digital/party")
@SOAPBinding
@SuppressWarnings({"unused", "ValidExternallyBoundObject"})
public class PartyReferenceWebService {

  /** The Party Reference Service. */
  private final IPartyReferenceService partyReferenceService;

  /**
   * Constructs a new <b>PartyReferenceWebService</b>.
   *
   * @param partyReferenceService the Party Reference Service
   */
  public PartyReferenceWebService(IPartyReferenceService partyReferenceService) {
    this.partyReferenceService = partyReferenceService;
  }

  /**
   * Retrieve the association property type reference data for a specific locale.
   *
   * @param tenantId the Universally Unique Identifier (UUID) for the tenant the association
   *     property type reference data is specific to
   * @param localeId the Unicode locale identifier for the locale to retrieve the association
   *     property type reference data for
   * @return the association property type reference data
   * @throws InvalidArgumentException if an argument is invalid
   * @throws ServiceUnavailableException if the association property type reference data could not
   *     be retrieved
   */
  @WebMethod(operationName = "GetAssociationPropertyTypes")
  public List<AssociationPropertyType> getAssociationPropertyTypes(
      @WebParam(name = "TenantId") @XmlElement(required = true) UUID tenantId,
      @WebParam(name = "LocaleId") @XmlElement(required = true) String localeId)
      throws InvalidArgumentException, ServiceUnavailableException {
    return partyReferenceService.getAssociationPropertyTypes(tenantId, localeId);
  }

  /**
   * Retrieve the association type reference data for a specific locale.
   *
   * @param tenantId the Universally Unique Identifier (UUID) for the tenant the association type
   *     reference data is specific to
   * @param localeId the Unicode locale identifier for the locale to retrieve the association type
   *     reference data for
   * @return the association type reference data
   * @throws InvalidArgumentException if an argument is invalid
   * @throws ServiceUnavailableException if the association type reference data could not be
   *     retrieved
   */
  @WebMethod(operationName = "GetAssociationTypes")
  public List<AssociationType> getAssociationTypes(
      @WebParam(name = "TenantId") @XmlElement(required = true) UUID tenantId,
      @WebParam(name = "LocaleId") @XmlElement(required = true) String localeId)
      throws InvalidArgumentException, ServiceUnavailableException {
    return partyReferenceService.getAssociationTypes(tenantId, localeId);
  }

  /**
   * Retrieve the attribute type category reference data for a specific locale.
   *
   * @param tenantId the Universally Unique Identifier (UUID) for the tenant the attribute type
   *     category reference data is specific to
   * @param localeId the Unicode locale identifier for the locale to retrieve the attribute type
   *     category reference data for
   * @return the attribute type category reference data
   * @throws InvalidArgumentException if an argument is invalid
   * @throws ServiceUnavailableException if the attribute type category reference data could not be
   *     retrieved
   */
  @WebMethod(operationName = "GetAttributeTypeCategories")
  public List<AttributeTypeCategory> getAttributeTypeCategories(
      @WebParam(name = "TenantId") @XmlElement(required = true) UUID tenantId,
      @WebParam(name = "LocaleId") @XmlElement(required = true) String localeId)
      throws InvalidArgumentException, ServiceUnavailableException {
    return partyReferenceService.getAttributeTypeCategories(tenantId, localeId);
  }

  /**
   * Retrieve the attribute type reference data for a specific locale.
   *
   * @param tenantId the Universally Unique Identifier (UUID) for the tenant the attribute type
   *     reference data is specific to
   * @param localeId the Unicode locale identifier for the locale to retrieve the attribute type
   *     reference data for
   * @return the attribute type reference data
   * @throws InvalidArgumentException if an argument is invalid
   * @throws ServiceUnavailableException if the attribute type reference data could not be retrieved
   */
  @WebMethod(operationName = "GetAttributeTypes")
  public List<AttributeType> getAttributeTypes(
      @WebParam(name = "TenantId") @XmlElement(required = true) UUID tenantId,
      @WebParam(name = "LocaleId") @XmlElement(required = true) String localeId)
      throws InvalidArgumentException, ServiceUnavailableException {
    return partyReferenceService.getAttributeTypes(tenantId, localeId);
  }

  /**
   * Retrieve the consent type reference data for a specific locale.
   *
   * @param tenantId the Universally Unique Identifier (UUID) for the tenant the consent type
   *     reference data is specific to
   * @param localeId the Unicode locale identifier for the locale to retrieve the consent type
   *     reference data for
   * @return the consent type reference data
   * @throws InvalidArgumentException if an argument is invalid
   * @throws ServiceUnavailableException if the consent type reference data could not be retrieved
   */
  @WebMethod(operationName = "GetConsentTypes")
  public List<ConsentType> getConsentTypes(
      @WebParam(name = "TenantId") @XmlElement(required = true) UUID tenantId,
      @WebParam(name = "LocaleId") @XmlElement(required = true) String localeId)
      throws InvalidArgumentException, ServiceUnavailableException {
    return partyReferenceService.getConsentTypes(tenantId, localeId);
  }

  /**
   * Retrieve the contact mechanism purpose reference data for a specific locale.
   *
   * @param tenantId the Universally Unique Identifier (UUID) for the tenant the contact mechanism
   *     purpose reference data is specific to
   * @param localeId the Unicode locale identifier for the locale to retrieve the contact mechanism
   *     purpose reference data for
   * @return the contact mechanism purpose reference data
   * @throws InvalidArgumentException if an argument is invalid
   * @throws ServiceUnavailableException if the contact mechanism purpose reference data could not
   *     be retrieved
   */
  @WebMethod(operationName = "GetContactMechanismPurposes")
  public List<ContactMechanismPurpose> getContactMechanismPurposes(
      @WebParam(name = "TenantId") @XmlElement(required = true) UUID tenantId,
      @WebParam(name = "LocaleId") @XmlElement(required = true) String localeId)
      throws InvalidArgumentException, ServiceUnavailableException {
    return partyReferenceService.getContactMechanismPurposes(tenantId, localeId);
  }

  /**
   * Retrieve the contact mechanism role reference data for a specific locale.
   *
   * @param tenantId the Universally Unique Identifier (UUID) for the tenant the contact mechanism
   *     role reference data is specific to
   * @param localeId the Unicode locale identifier for the locale to retrieve the contact mechanism
   *     role reference data for
   * @return the contact mechanism role reference data
   * @throws InvalidArgumentException if an argument is invalid
   * @throws ServiceUnavailableException if the contact mechanism role reference data could not be
   *     retrieved
   */
  @WebMethod(operationName = "GetContactMechanismRoles")
  public List<ContactMechanismRole> getContactMechanismRoles(
      @WebParam(name = "TenantId") @XmlElement(required = true) UUID tenantId,
      @WebParam(name = "LocaleId") @XmlElement(required = true) String localeId)
      throws InvalidArgumentException, ServiceUnavailableException {
    return partyReferenceService.getContactMechanismRoles(tenantId, localeId);
  }

  /**
   * Retrieve the contact mechanism type reference data for a specific locale.
   *
   * @param tenantId the Universally Unique Identifier (UUID) for the tenant the contact mechanism
   *     type reference data is specific to
   * @param localeId the Unicode locale identifier for the locale to retrieve the contact mechanism
   *     type reference data for
   * @return the contact mechanism type reference data
   * @throws InvalidArgumentException if an argument is invalid
   * @throws ServiceUnavailableException if the contact mechanism type reference data could not be
   *     retrieved
   */
  @WebMethod(operationName = "GetContactMechanismTypes")
  public List<ContactMechanismType> getContactMechanismTypes(
      @WebParam(name = "TenantId") @XmlElement(required = true) UUID tenantId,
      @WebParam(name = "LocaleId") @XmlElement(required = true) String localeId)
      throws InvalidArgumentException, ServiceUnavailableException {
    return partyReferenceService.getContactMechanismTypes(tenantId, localeId);
  }

  /**
   * Retrieve the employment status reference data for a specific locale.
   *
   * @param tenantId the Universally Unique Identifier (UUID) for the tenant the employment status
   *     reference data is specific to
   * @param localeId the Unicode locale identifier for the locale to retrieve the employment status
   *     reference data for
   * @return the employment status reference data
   * @throws InvalidArgumentException if an argument is invalid
   * @throws ServiceUnavailableException if the employment status reference data could not be
   *     retrieved
   */
  @WebMethod(operationName = "GetEmploymentStatuses")
  public List<EmploymentStatus> getEmploymentStatuses(
      @WebParam(name = "TenantId") @XmlElement(required = true) UUID tenantId,
      @WebParam(name = "LocaleId") @XmlElement(required = true) String localeId)
      throws InvalidArgumentException, ServiceUnavailableException {
    return partyReferenceService.getEmploymentStatuses(tenantId, localeId);
  }

  /**
   * Retrieve the employment type reference data for a specific locale.
   *
   * @param tenantId the Universally Unique Identifier (UUID) for the tenant the employment type
   *     reference data is specific to
   * @param localeId the Unicode locale identifier for the locale to retrieve the employment type
   *     reference data for
   * @return the employment type reference data
   * @throws InvalidArgumentException if an argument is invalid
   * @throws ServiceUnavailableException if the employment type reference data could not be
   *     retrieved
   */
  @WebMethod(operationName = "GetEmploymentTypes")
  public List<EmploymentType> getEmploymentTypes(
      @WebParam(name = "TenantId") @XmlElement(required = true) UUID tenantId,
      @WebParam(name = "LocaleId") @XmlElement(required = true) String localeId)
      throws InvalidArgumentException, ServiceUnavailableException {
    return partyReferenceService.getEmploymentTypes(tenantId, localeId);
  }

  /**
   * Retrieve the fields of study reference data for a specific locale.
   *
   * @param tenantId the Universally Unique Identifier (UUID) for the tenant the fields of study
   *     reference data is specific to
   * @param localeId the Unicode locale identifier for the locale to retrieve the fields of study
   *     reference data for
   * @return the fields of study reference data
   * @throws InvalidArgumentException if an argument is invalid
   * @throws ServiceUnavailableException if the fields of study reference data could not be
   *     retrieved
   */
  @WebMethod(operationName = "GetFieldsOfStudy")
  public List<FieldOfStudy> getFieldsOfStudy(
      @WebParam(name = "TenantId") @XmlElement(required = true) UUID tenantId,
      @WebParam(name = "LocaleId") @XmlElement(required = true) String localeId)
      throws InvalidArgumentException, ServiceUnavailableException {
    return partyReferenceService.getFieldsOfStudy(tenantId, localeId);
  }

  /**
   * Retrieve the gender reference data for a specific locale.
   *
   * @param tenantId the Universally Unique Identifier (UUID) for the tenant the gender reference
   *     data is specific to
   * @param localeId the Unicode locale identifier for the locale to retrieve the gender reference
   *     data for
   * @return the gender reference data
   * @throws InvalidArgumentException if an argument is invalid
   * @throws ServiceUnavailableException if the gender reference data could not be retrieved
   */
  @WebMethod(operationName = "GetGenders")
  public List<Gender> getGenders(
      @WebParam(name = "TenantId") @XmlElement(required = true) UUID tenantId,
      @WebParam(name = "LocaleId") @XmlElement(required = true) String localeId)
      throws InvalidArgumentException, ServiceUnavailableException {
    return partyReferenceService.getGenders(tenantId, localeId);
  }

  /**
   * Retrieve the identity document type reference data for a specific locale.
   *
   * @param tenantId the Universally Unique Identifier (UUID) for the tenant the identity document
   *     type reference data is specific to
   * @param localeId the Unicode locale identifier for the locale to retrieve the identity document
   *     type reference data for
   * @return the identity document type reference data
   * @throws InvalidArgumentException if an argument is invalid
   * @throws ServiceUnavailableException if the identity document type reference data could not be
   *     retrieved
   */
  @WebMethod(operationName = "GetIdentityDocumentTypes")
  public List<IdentityDocumentType> getIdentityDocumentTypes(
      @WebParam(name = "TenantId") @XmlElement(required = true) UUID tenantId,
      @WebParam(name = "LocaleId") @XmlElement(required = true) String localeId)
      throws InvalidArgumentException, ServiceUnavailableException {
    return partyReferenceService.getIdentityDocumentTypes(tenantId, localeId);
  }

  /**
   * Retrieve the lock type category reference data for a specific locale.
   *
   * @param tenantId the Universally Unique Identifier (UUID) for the tenant the lock type category
   *     reference data is specific to
   * @param localeId the Unicode locale identifier for the locale to retrieve the lock type category
   *     reference data for
   * @return the lock type category reference data
   * @throws InvalidArgumentException if an argument is invalid
   * @throws ServiceUnavailableException if the lock type category reference data could not be
   *     retrieved
   */
  @WebMethod(operationName = "GetLockTypeCategories")
  public List<LockTypeCategory> getLockTypeCategories(
      @WebParam(name = "TenantId") @XmlElement(required = true) UUID tenantId,
      @WebParam(name = "LocaleId") @XmlElement(required = true) String localeId)
      throws InvalidArgumentException, ServiceUnavailableException {
    return partyReferenceService.getLockTypeCategories(tenantId, localeId);
  }

  /**
   * Retrieve the lock type reference data for a specific locale.
   *
   * @param tenantId the Universally Unique Identifier (UUID) for the tenant the lock type reference
   *     data is specific to
   * @param localeId the Unicode locale identifier for the locale to retrieve the lock type
   *     reference data for
   * @return the lock type reference data
   * @throws InvalidArgumentException if an argument is invalid
   * @throws ServiceUnavailableException if the lock type reference data could not be retrieved
   */
  @WebMethod(operationName = "GetLockTypes")
  public List<LockType> getLockTypes(
      @WebParam(name = "TenantId") @XmlElement(required = true) UUID tenantId,
      @WebParam(name = "LocaleId") @XmlElement(required = true) String localeId)
      throws InvalidArgumentException, ServiceUnavailableException {
    return partyReferenceService.getLockTypes(tenantId, localeId);
  }

  /**
   * Retrieve the marital status reference data for a specific locale.
   *
   * @param tenantId the Universally Unique Identifier (UUID) for the tenant the marital status
   *     reference data is specific to
   * @param localeId the Unicode locale identifier for the locale to retrieve the marital status
   *     reference data for
   * @return the marital status reference data
   * @throws InvalidArgumentException if an argument is invalid
   * @throws ServiceUnavailableException if the marital status reference data could not be retrieved
   */
  @WebMethod(operationName = "GetMaritalStatuses")
  public List<MaritalStatus> getMaritalStatuses(
      @WebParam(name = "TenantId") @XmlElement(required = true) UUID tenantId,
      @WebParam(name = "LocaleId") @XmlElement(required = true) String localeId)
      throws InvalidArgumentException, ServiceUnavailableException {
    return partyReferenceService.getMaritalStatuses(tenantId, localeId);
  }

  /**
   * Retrieve the marriage type reference data for a specific locale.
   *
   * @param tenantId the Universally Unique Identifier (UUID) for the tenant the marriage type
   *     reference data is specific to
   * @param localeId the Unicode locale identifier for the locale to retrieve the marriage type
   *     reference data
   * @return the marriage type reference data
   * @throws InvalidArgumentException if an argument is invalid
   * @throws ServiceUnavailableException if the marriage type reference data could not be retrieved
   */
  @WebMethod(operationName = "GetMarriageTypes")
  public List<MarriageType> getMarriageTypes(
      @WebParam(name = "TenantId") @XmlElement(required = true) UUID tenantId,
      @WebParam(name = "LocaleId") @XmlElement(required = true) String localeId)
      throws InvalidArgumentException, ServiceUnavailableException {
    return partyReferenceService.getMarriageTypes(tenantId, localeId);
  }

  /**
   * Retrieve the next of kin type reference data for a specific locale.
   *
   * @param tenantId the Universally Unique Identifier (UUID) for the tenant the next of kin type
   *     reference data is specific to
   * @param localeId the Unicode locale identifier for the locale to retrieve the next of kin type
   *     reference data for
   * @return the next of kin type reference data
   * @throws InvalidArgumentException if an argument is invalid
   * @throws ServiceUnavailableException if the next of kin type reference data could not be
   *     retrieved
   */
  @WebMethod(operationName = "GetNextOfKinTypes")
  public List<NextOfKinType> getNextOfKinTypes(
      @WebParam(name = "TenantId") @XmlElement(required = true) UUID tenantId,
      @WebParam(name = "LocaleId") @XmlElement(required = true) String localeId)
      throws InvalidArgumentException, ServiceUnavailableException {
    return partyReferenceService.getNextOfKinTypes(tenantId, localeId);
  }

  /**
   * Retrieve the occupation reference data for a specific locale.
   *
   * @param tenantId the Universally Unique Identifier (UUID) for the tenant the occupation
   *     reference data is specific to
   * @param localeId the Unicode locale identifier for the locale to retrieve the occupation
   *     reference data for
   * @return the occupation reference data
   * @throws InvalidArgumentException if an argument is invalid
   * @throws ServiceUnavailableException if the occupation reference data could not be retrieved
   */
  @WebMethod(operationName = "GetOccupations")
  public List<Occupation> getOccupations(
      @WebParam(name = "TenantId") @XmlElement(required = true) UUID tenantId,
      @WebParam(name = "LocaleId") @XmlElement(required = true) String localeId)
      throws InvalidArgumentException, ServiceUnavailableException {
    return partyReferenceService.getOccupations(tenantId, localeId);
  }

  /**
   * Retrieve the physical address purpose reference data for a specific locale.
   *
   * @param tenantId the Universally Unique Identifier (UUID) for the tenant the physical address
   *     purpose reference data is specific to
   * @param localeId the Unicode locale identifier for the locale to retrieve the physical address
   *     purpose reference data for
   * @return the physical address purpose reference data
   * @throws InvalidArgumentException if an argument is invalid
   * @throws ServiceUnavailableException if the physical address purpose reference data could not be
   *     retrieved
   */
  @WebMethod(operationName = "GetPhysicalAddressPurposes")
  public List<PhysicalAddressPurpose> getPhysicalAddressPurposes(
      @WebParam(name = "TenantId") @XmlElement(required = true) UUID tenantId,
      @WebParam(name = "LocaleId") @XmlElement(required = true) String localeId)
      throws InvalidArgumentException, ServiceUnavailableException {
    return partyReferenceService.getPhysicalAddressPurposes(tenantId, localeId);
  }

  /**
   * Retrieve the physical address role reference data for a specific locale.
   *
   * @param tenantId the Universally Unique Identifier (UUID) for the tenant the physical address
   *     role reference data is specific to
   * @param localeId the Unicode locale identifier for the locale to retrieve the physical address
   *     role reference data for
   * @return the physical address role reference data
   * @throws InvalidArgumentException if an argument is invalid
   * @throws ServiceUnavailableException if the physical address role reference data could not be
   *     retrieved
   */
  @WebMethod(operationName = "GetPhysicalAddressRoles")
  public List<PhysicalAddressRole> getPhysicalAddressRoles(
      @WebParam(name = "TenantId") @XmlElement(required = true) UUID tenantId,
      @WebParam(name = "LocaleId") @XmlElement(required = true) String localeId)
      throws InvalidArgumentException, ServiceUnavailableException {
    return partyReferenceService.getPhysicalAddressRoles(tenantId, localeId);
  }

  /**
   * Retrieve the physical address type reference data for a specific locale.
   *
   * @param tenantId the Universally Unique Identifier (UUID) for the tenant the physical address
   *     type reference data is specific to
   * @param localeId the Unicode locale identifier for the locale to retrieve the physical address
   *     type reference data for
   * @return the physical address type reference data
   * @throws InvalidArgumentException if an argument is invalid
   * @throws ServiceUnavailableException if the physical address type reference data could not be
   *     retrieved
   */
  @WebMethod(operationName = "GetPhysicalAddressTypes")
  public List<PhysicalAddressType> getPhysicalAddressTypes(
      @WebParam(name = "TenantId") @XmlElement(required = true) UUID tenantId,
      @WebParam(name = "LocaleId") @XmlElement(required = true) String localeId)
      throws InvalidArgumentException, ServiceUnavailableException {
    return partyReferenceService.getPhysicalAddressTypes(tenantId, localeId);
  }

  /**
   * Retrieve the preference type category reference data for a specific locale.
   *
   * @param tenantId the Universally Unique Identifier (UUID) for the tenant the preference type
   *     category reference data is specific to
   * @param localeId the Unicode locale identifier for the locale to retrieve the preference type
   *     category reference data for
   * @return the preference type category reference data
   * @throws InvalidArgumentException if an argument is invalid
   * @throws ServiceUnavailableException if the preference type category reference data could not be
   *     retrieved
   */
  @WebMethod(operationName = "GetPreferenceTypeCategories")
  public List<PreferenceTypeCategory> getPreferenceTypeCategories(
      @WebParam(name = "TenantId") @XmlElement(required = true) UUID tenantId,
      @WebParam(name = "LocaleId") @XmlElement(required = true) String localeId)
      throws InvalidArgumentException, ServiceUnavailableException {
    return partyReferenceService.getPreferenceTypeCategories(tenantId, localeId);
  }

  /**
   * Retrieve the preference type reference data for a specific locale.
   *
   * @param tenantId the Universally Unique Identifier (UUID) for the tenant the preference type
   *     reference data is specific to
   * @param localeId the Unicode locale identifier for the locale to retrieve the preference type
   *     reference data for
   * @return the preference type reference data
   * @throws InvalidArgumentException if an argument is invalid
   * @throws ServiceUnavailableException if the preference type reference data could not be
   *     retrieved
   */
  @WebMethod(operationName = "GetPreferenceTypes")
  public List<PreferenceType> getPreferenceTypes(
      @WebParam(name = "TenantId") @XmlElement(required = true) UUID tenantId,
      @WebParam(name = "LocaleId") @XmlElement(required = true) String localeId)
      throws InvalidArgumentException, ServiceUnavailableException {
    return partyReferenceService.getPreferenceTypes(tenantId, localeId);
  }

  /**
   * Retrieve the qualification type reference data for a specific locale.
   *
   * @param tenantId the Universally Unique Identifier (UUID) for the tenant the qualification type
   *     reference data is specific to
   * @param localeId the Unicode locale identifier for the locale to retrieve the qualification type
   *     reference data for
   * @return the qualification type reference data
   * @throws InvalidArgumentException if an argument is invalid
   * @throws ServiceUnavailableException if the qualification type reference data could not be
   *     retrieved
   */
  @WebMethod(operationName = "GetQualificationTypes")
  public List<QualificationType> getQualificationTypes(
      @WebParam(name = "TenantId") @XmlElement(required = true) UUID tenantId,
      @WebParam(name = "LocaleId") @XmlElement(required = true) String localeId)
      throws InvalidArgumentException, ServiceUnavailableException {
    return partyReferenceService.getQualificationTypes(tenantId, localeId);
  }

  /**
   * Retrieve the race reference data for a specific locale.
   *
   * @param tenantId the Universally Unique Identifier (UUID) for the tenant the race reference data
   *     is specific to
   * @param localeId the Unicode locale identifier for the locale to retrieve the race reference
   *     data for
   * @return the race reference data
   * @throws InvalidArgumentException if an argument is invalid
   * @throws ServiceUnavailableException if the race reference data could not be retrieved
   */
  @WebMethod(operationName = "GetRaces")
  public List<Race> getRaces(
      @WebParam(name = "TenantId") @XmlElement(required = true) UUID tenantId,
      @WebParam(name = "LocaleId") @XmlElement(required = true) String localeId)
      throws InvalidArgumentException, ServiceUnavailableException {
    return partyReferenceService.getRaces(tenantId, localeId);
  }

  /**
   * Retrieve the residence permit type reference data for a specific locale.
   *
   * @param tenantId the Universally Unique Identifier (UUID) for the tenant the residence permit
   *     type reference data is specific to
   * @param localeId the Unicode locale identifier for the locale to retrieve the residence permit
   *     type reference data for
   * @return the residence permit type reference data
   * @throws InvalidArgumentException if an argument is invalid
   * @throws ServiceUnavailableException if the residence permit type reference data could not be
   *     retrieved
   */
  @WebMethod(operationName = "GetResidencePermitTypes")
  public List<ResidencePermitType> getResidencePermitTypes(
      @WebParam(name = "TenantId") @XmlElement(required = true) UUID tenantId,
      @WebParam(name = "LocaleId") @XmlElement(required = true) String localeId)
      throws InvalidArgumentException, ServiceUnavailableException {
    return partyReferenceService.getResidencePermitTypes(tenantId, localeId);
  }

  /**
   * Retrieve the residency status reference data for a specific locale.
   *
   * @param tenantId the Universally Unique Identifier (UUID) for the tenant the residency status
   *     reference data is specific to
   * @param localeId the Unicode locale identifier for the locale to retrieve the residency status
   *     reference data for
   * @return the residency status reference data
   * @throws InvalidArgumentException if an argument is invalid
   * @throws ServiceUnavailableException if the residency status reference data could not be
   *     retrieved
   */
  @WebMethod(operationName = "GetResidencyStatuses")
  public List<ResidencyStatus> getResidencyStatuses(
      @WebParam(name = "TenantId") @XmlElement(required = true) UUID tenantId,
      @WebParam(name = "LocaleId") @XmlElement(required = true) String localeId)
      throws InvalidArgumentException, ServiceUnavailableException {
    return partyReferenceService.getResidencyStatuses(tenantId, localeId);
  }

  /**
   * Retrieve the residential type reference data for a specific locale.
   *
   * @param tenantId the Universally Unique Identifier (UUID) for the tenant the residential type
   *     reference data is specific to
   * @param localeId the Unicode locale identifier for the locale to retrieve the residential type
   *     reference data for
   * @return the residential type reference data
   * @throws InvalidArgumentException if an argument is invalid
   * @throws ServiceUnavailableException if the residential type reference data could not be
   *     retrieved
   */
  @WebMethod(operationName = "GetResidentialTypes")
  public List<ResidentialType> getResidentialTypes(
      @WebParam(name = "TenantId") @XmlElement(required = true) UUID tenantId,
      @WebParam(name = "LocaleId") @XmlElement(required = true) String localeId)
      throws InvalidArgumentException, ServiceUnavailableException {
    return partyReferenceService.getResidentialTypes(tenantId, localeId);
  }

  /**
   * Retrieve the role purpose reference data for a specific locale.
   *
   * @param tenantId the Universally Unique Identifier (UUID) for the tenant the role purpose
   *     reference data is specific to
   * @param localeId the Unicode locale identifier for the locale to retrieve the role purpose
   *     reference data for
   * @return the role purpose reference data
   * @throws InvalidArgumentException if an argument is invalid
   * @throws ServiceUnavailableException if the role purpose reference data could not be retrieved
   */
  @WebMethod(operationName = "GetRolePurposes")
  public List<RolePurpose> getRolePurposes(
      @WebParam(name = "TenantId") @XmlElement(required = true) UUID tenantId,
      @WebParam(name = "LocaleId") @XmlElement(required = true) String localeId)
      throws InvalidArgumentException, ServiceUnavailableException {
    return partyReferenceService.getRolePurposes(tenantId, localeId);
  }

  /**
   * Retrieve the role type attribute type constraints for a specific role type.
   *
   * @param roleType the code for the role type to retrieve the attribute constraints for
   * @return the role type attribute type constraints
   * @throws InvalidArgumentException if an argument is invalid
   * @throws ServiceUnavailableException if the role type attribute type constraints for a specific
   *     role type could not be retrieved
   */
  @WebMethod(operationName = "GetRoleTypeAttributeTypeConstraints")
  public List<RoleTypeAttributeTypeConstraint> getRoleTypeAttributeTypeConstraints(
      @WebParam(name = "RoleType") @XmlElement String roleType)
      throws InvalidArgumentException, ServiceUnavailableException {
    return partyReferenceService.getRoleTypeAttributeTypeConstraints(roleType);
  }

  /**
   * Retrieve the role type preference type constraints for a specific role type.
   *
   * @param roleType the code for the role type to retrieve the preference constraints for
   * @return the role type preference type constraints
   * @throws InvalidArgumentException if an argument is invalid
   * @throws ServiceUnavailableException if the role type preference type constraints for a specific
   *     role type could not be retrieved
   */
  @WebMethod(operationName = "GetRoleTypePreferenceTypeConstraints")
  public List<RoleTypePreferenceTypeConstraint> getRoleTypePreferenceTypeConstraints(
      @WebParam(name = "RoleType") @XmlElement String roleType)
      throws InvalidArgumentException, ServiceUnavailableException {
    return partyReferenceService.getRoleTypePreferenceTypeConstraints(roleType);
  }

  /**
   * Retrieve the role type reference data for a specific locale.
   *
   * @param tenantId the Universally Unique Identifier (UUID) for the tenant the role type reference
   *     data is specific to
   * @param localeId the Unicode locale identifier for the locale to retrieve the role type
   *     reference data for
   * @return the role type reference data
   * @throws InvalidArgumentException if an argument is invalid
   * @throws ServiceUnavailableException if the role type reference data could not be retrieved
   */
  @WebMethod(operationName = "GetRoleTypes")
  public List<RoleType> getRoleTypes(
      @WebParam(name = "TenantId") @XmlElement(required = true) UUID tenantId,
      @WebParam(name = "LocaleId") @XmlElement(required = true) String localeId)
      throws InvalidArgumentException, ServiceUnavailableException {
    return partyReferenceService.getRoleTypes(tenantId, localeId);
  }

  /**
   * Retrieve the segment reference data for a specific locale.
   *
   * @param tenantId the Universally Unique Identifier (UUID) for the tenant the segment reference
   *     data is specific to
   * @param localeId the Unicode locale identifier for the locale to retrieve the segment reference
   *     data for
   * @return the segment reference data
   * @throws InvalidArgumentException if an argument is invalid
   * @throws ServiceUnavailableException if the segment reference data could not be retrieved
   */
  @WebMethod(operationName = "GetSegments")
  public List<Segment> getSegments(
      @WebParam(name = "TenantId") @XmlElement(required = true) UUID tenantId,
      @WebParam(name = "LocaleId") @XmlElement(required = true) String localeId)
      throws InvalidArgumentException, ServiceUnavailableException {
    return partyReferenceService.getSegments(tenantId, localeId);
  }

  /**
   * Retrieve the source of funds type reference data for a specific locale.
   *
   * @param tenantId the Universally Unique Identifier (UUID) for the tenant the source of funds
   *     type reference data is specific to
   * @param localeId the Unicode locale identifier for the locale to retrieve the source of funds
   *     type reference data
   * @return the source of funds type reference data
   * @throws InvalidArgumentException if an argument is invalid
   * @throws ServiceUnavailableException if the source of funds type reference data could not be
   *     retrieved
   */
  @WebMethod(operationName = "GetSourceOfFundsTypes")
  public List<SourceOfFundsType> getSourceOfFundsTypes(
      @WebParam(name = "TenantId") @XmlElement(required = true) UUID tenantId,
      @WebParam(name = "LocaleId") @XmlElement(required = true) String localeId)
      throws InvalidArgumentException, ServiceUnavailableException {
    return partyReferenceService.getSourceOfFundsTypes(tenantId, localeId);
  }

  /**
   * Retrieve the source of wealth type reference data for a specific locale.
   *
   * @param tenantId the Universally Unique Identifier (UUID) for the tenant the source of wealth
   *     type reference data is specific to
   * @param localeId the Unicode locale identifier for the locale to retrieve the source of wealth
   *     type reference data for
   * @return the source of wealth type reference data
   * @throws InvalidArgumentException if an argument is invalid
   * @throws ServiceUnavailableException if the source of wealth type reference data could not be
   *     retrieved
   */
  @WebMethod(operationName = "GetSourceOfWealthTypes")
  public List<SourceOfWealthType> getSourceOfWealthTypes(
      @WebParam(name = "TenantId") @XmlElement(required = true) UUID tenantId,
      @WebParam(name = "LocaleId") @XmlElement(required = true) String localeId)
      throws InvalidArgumentException, ServiceUnavailableException {
    return partyReferenceService.getSourceOfWealthTypes(tenantId, localeId);
  }

  /**
   * Retrieve the status type category reference data for a specific locale.
   *
   * @param tenantId the Universally Unique Identifier (UUID) for the tenant the status type
   *     category reference data is specific to
   * @param localeId the Unicode locale identifier for the locale to retrieve the status type
   *     category reference data for
   * @return the status type category reference data
   * @throws InvalidArgumentException if an argument is invalid
   * @throws ServiceUnavailableException if the status type category reference data could not be
   *     retrieved
   */
  @WebMethod(operationName = "GetStatusTypeCategories")
  public List<StatusTypeCategory> getStatusTypeCategories(
      @WebParam(name = "TenantId") @XmlElement(required = true) UUID tenantId,
      @WebParam(name = "LocaleId") @XmlElement(required = true) String localeId)
      throws InvalidArgumentException, ServiceUnavailableException {
    return partyReferenceService.getStatusTypeCategories(tenantId, localeId);
  }

  /**
   * Retrieve the status type reference data for a specific locale.
   *
   * @param tenantId the Universally Unique Identifier (UUID) for the tenant the status type
   *     reference data is specific to
   * @param localeId the Unicode locale identifier for the locale to retrieve the status type
   *     reference data for
   * @return the status type reference data
   * @throws InvalidArgumentException if an argument is invalid
   * @throws ServiceUnavailableException if the status type reference data could not be retrieved
   */
  @WebMethod(operationName = "GetStatusTypes")
  public List<StatusType> getStatusTypes(
      @WebParam(name = "TenantId") @XmlElement(required = true) UUID tenantId,
      @WebParam(name = "LocaleId") @XmlElement(required = true) String localeId)
      throws InvalidArgumentException, ServiceUnavailableException {
    return partyReferenceService.getStatusTypes(tenantId, localeId);
  }

  /**
   * Retrieve the tax number type reference data for a specific locale.
   *
   * @param tenantId the Universally Unique Identifier (UUID) for the tenant the tax number type
   *     reference data is specific to
   * @param localeId the Unicode locale identifier for the locale to retrieve the tax number type
   *     reference data for
   * @return the tax number type reference data
   * @throws InvalidArgumentException if an argument is invalid
   * @throws ServiceUnavailableException if the tax number type reference data could not be
   *     retrieved
   */
  @WebMethod(operationName = "GetTaxNumberTypes")
  public List<TaxNumberType> getTaxNumberTypes(
      @WebParam(name = "TenantId") @XmlElement(required = true) UUID tenantId,
      @WebParam(name = "LocaleId") @XmlElement(required = true) String localeId)
      throws InvalidArgumentException, ServiceUnavailableException {
    return partyReferenceService.getTaxNumberTypes(tenantId, localeId);
  }

  /**
   * Retrieve the times to contact reference data for a specific locale.
   *
   * @param tenantId the Universally Unique Identifier (UUID) for the tenant the times to contact
   *     reference data is specific to
   * @param localeId the Unicode locale identifier for the locale to retrieve the times to contact
   *     reference data for
   * @return the times to contact reference data
   * @throws InvalidArgumentException if an argument is invalid
   * @throws ServiceUnavailableException if the times to contact reference data could not be
   *     retrieved
   */
  @WebMethod(operationName = "GetTimesToContact")
  public List<TimeToContact> getTimesToContact(
      @WebParam(name = "TenantId") @XmlElement(required = true) UUID tenantId,
      @WebParam(name = "LocaleId") @XmlElement(required = true) String localeId)
      throws InvalidArgumentException, ServiceUnavailableException {
    return partyReferenceService.getTimesToContact(tenantId, localeId);
  }

  /**
   * Retrieve the title reference data for a specific locale.
   *
   * @param tenantId the Universally Unique Identifier (UUID) for the tenant the title reference
   *     data is specific to
   * @param localeId the Unicode locale identifier for the locale to retrieve the title reference
   *     data for
   * @return the title reference data
   * @throws InvalidArgumentException if an argument is invalid
   * @throws ServiceUnavailableException if the title reference data could not be retrieved
   */
  @WebMethod(operationName = "GetTitles")
  public List<Title> getTitles(
      @WebParam(name = "TenantId") @XmlElement(required = true) UUID tenantId,
      @WebParam(name = "LocaleId") @XmlElement(required = true) String localeId)
      throws InvalidArgumentException, ServiceUnavailableException {
    return partyReferenceService.getTitles(tenantId, localeId);
  }
}
