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

package digital.inception.party.ws;

import digital.inception.core.service.InvalidArgumentException;
import digital.inception.core.service.ServiceUnavailableException;
import digital.inception.party.model.*;
import digital.inception.party.service.PartyReferenceService;
import digital.inception.ws.AbstractWebServiceBase;
import jakarta.jws.WebMethod;
import jakarta.jws.WebParam;
import jakarta.jws.WebResult;
import jakarta.jws.WebService;
import jakarta.jws.soap.SOAPBinding;
import jakarta.xml.bind.annotation.XmlElement;
import java.util.List;
import java.util.UUID;
import org.springframework.context.ApplicationContext;

/**
 * The {@code PartyReferenceWebService} class.
 *
 * @author Marcus Portmann
 */
@WebService(
    serviceName = "PartyReferenceService",
    name = "IPartyReferenceService",
    targetNamespace = "https://inception.digital/party")
@SOAPBinding
@SuppressWarnings({"unused", "ValidExternallyBoundObject"})
public class PartyReferenceWebService extends AbstractWebServiceBase {

  /** The Party Reference Service. */
  private final PartyReferenceService partyReferenceService;

  /**
   * Creates a new {@code PartyReferenceWebService} instance.
   *
   * @param applicationContext the Spring application context
   * @param partyReferenceService the Party Reference Service
   */
  public PartyReferenceWebService(
      ApplicationContext applicationContext, PartyReferenceService partyReferenceService) {
    super(applicationContext);

    this.partyReferenceService = partyReferenceService;
  }

  /**
   * Retrieve the association property type reference data for a specific locale.
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
  @WebMethod(operationName = "GetAssociationPropertyTypes")
  @WebResult(name = "AssociationPropertyType")
  public List<AssociationPropertyType> getAssociationPropertyTypes(
      @WebParam(name = "TenantId") @XmlElement(required = true) UUID tenantId,
      @WebParam(name = "LocaleId") @XmlElement(required = true) String localeId)
      throws InvalidArgumentException, ServiceUnavailableException {
    return partyReferenceService.getAssociationPropertyTypes(tenantId, localeId);
  }

  /**
   * Retrieve the association type reference data for a specific locale.
   *
   * @param tenantId the ID for the tenant the association type reference data is specific to
   * @param localeId the Unicode locale identifier for the locale to retrieve the association type
   *     reference data for
   * @return the association type reference data
   * @throws InvalidArgumentException if an argument is invalid
   * @throws ServiceUnavailableException if the association type reference data could not be
   *     retrieved
   */
  @WebMethod(operationName = "GetAssociationTypes")
  @WebResult(name = "AssociationType")
  public List<AssociationType> getAssociationTypes(
      @WebParam(name = "TenantId") @XmlElement(required = true) UUID tenantId,
      @WebParam(name = "LocaleId") @XmlElement(required = true) String localeId)
      throws InvalidArgumentException, ServiceUnavailableException {
    return partyReferenceService.getAssociationTypes(tenantId, localeId);
  }

  /**
   * Retrieve the attribute type category reference data for a specific locale.
   *
   * @param tenantId the ID for the tenant the attribute type category reference data is specific to
   * @param localeId the Unicode locale identifier for the locale to retrieve the attribute type
   *     category reference data for
   * @return the attribute type category reference data
   * @throws InvalidArgumentException if an argument is invalid
   * @throws ServiceUnavailableException if the attribute type category reference data could not be
   *     retrieved
   */
  @WebMethod(operationName = "GetAttributeTypeCategories")
  @WebResult(name = "AttributeTypeCategory")
  public List<AttributeTypeCategory> getAttributeTypeCategories(
      @WebParam(name = "TenantId") @XmlElement(required = true) UUID tenantId,
      @WebParam(name = "LocaleId") @XmlElement(required = true) String localeId)
      throws InvalidArgumentException, ServiceUnavailableException {
    return partyReferenceService.getAttributeTypeCategories(tenantId, localeId);
  }

  /**
   * Retrieve the attribute type reference data for a specific locale.
   *
   * @param tenantId the ID for the tenant the attribute type reference data is specific to
   * @param localeId the Unicode locale identifier for the locale to retrieve the attribute type
   *     reference data for
   * @return the attribute type reference data
   * @throws InvalidArgumentException if an argument is invalid
   * @throws ServiceUnavailableException if the attribute type reference data could not be retrieved
   */
  @WebMethod(operationName = "GetAttributeTypes")
  @WebResult(name = "AttributeType")
  public List<AttributeType> getAttributeTypes(
      @WebParam(name = "TenantId") @XmlElement(required = true) UUID tenantId,
      @WebParam(name = "LocaleId") @XmlElement(required = true) String localeId)
      throws InvalidArgumentException, ServiceUnavailableException {
    return partyReferenceService.getAttributeTypes(tenantId, localeId);
  }

  /**
   * Retrieve the consent type reference data for a specific locale.
   *
   * @param tenantId the ID for the tenant the consent type reference data is specific to
   * @param localeId the Unicode locale identifier for the locale to retrieve the consent type
   *     reference data for
   * @return the consent type reference data
   * @throws InvalidArgumentException if an argument is invalid
   * @throws ServiceUnavailableException if the consent type reference data could not be retrieved
   */
  @WebMethod(operationName = "GetConsentTypes")
  @WebResult(name = "ConsentType")
  public List<ConsentType> getConsentTypes(
      @WebParam(name = "TenantId") @XmlElement(required = true) UUID tenantId,
      @WebParam(name = "LocaleId") @XmlElement(required = true) String localeId)
      throws InvalidArgumentException, ServiceUnavailableException {
    return partyReferenceService.getConsentTypes(tenantId, localeId);
  }

  /**
   * Retrieve the contact mechanism purpose reference data for a specific locale.
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
  @WebMethod(operationName = "GetContactMechanismPurposes")
  @WebResult(name = "ContactMechanismPurpose")
  public List<ContactMechanismPurpose> getContactMechanismPurposes(
      @WebParam(name = "TenantId") @XmlElement(required = true) UUID tenantId,
      @WebParam(name = "LocaleId") @XmlElement(required = true) String localeId)
      throws InvalidArgumentException, ServiceUnavailableException {
    return partyReferenceService.getContactMechanismPurposes(tenantId, localeId);
  }

  /**
   * Retrieve the contact mechanism role reference data for a specific locale.
   *
   * @param tenantId the ID for the tenant the contact mechanism role reference data is specific to
   * @param localeId the Unicode locale identifier for the locale to retrieve the contact mechanism
   *     role reference data for
   * @return the contact mechanism role reference data
   * @throws InvalidArgumentException if an argument is invalid
   * @throws ServiceUnavailableException if the contact mechanism role reference data could not be
   *     retrieved
   */
  @WebMethod(operationName = "GetContactMechanismRoles")
  @WebResult(name = "ContactMechanismRole")
  public List<ContactMechanismRole> getContactMechanismRoles(
      @WebParam(name = "TenantId") @XmlElement(required = true) UUID tenantId,
      @WebParam(name = "LocaleId") @XmlElement(required = true) String localeId)
      throws InvalidArgumentException, ServiceUnavailableException {
    return partyReferenceService.getContactMechanismRoles(tenantId, localeId);
  }

  /**
   * Retrieve the contact mechanism type reference data for a specific locale.
   *
   * @param tenantId the ID for the tenant the contact mechanism type reference data is specific to
   * @param localeId the Unicode locale identifier for the locale to retrieve the contact mechanism
   *     type reference data for
   * @return the contact mechanism type reference data
   * @throws InvalidArgumentException if an argument is invalid
   * @throws ServiceUnavailableException if the contact mechanism type reference data could not be
   *     retrieved
   */
  @WebMethod(operationName = "GetContactMechanismTypes")
  @WebResult(name = "ContactMechanismType")
  public List<ContactMechanismType> getContactMechanismTypes(
      @WebParam(name = "TenantId") @XmlElement(required = true) UUID tenantId,
      @WebParam(name = "LocaleId") @XmlElement(required = true) String localeId)
      throws InvalidArgumentException, ServiceUnavailableException {
    return partyReferenceService.getContactMechanismTypes(tenantId, localeId);
  }

  /**
   * Retrieve the employment status reference data for a specific locale.
   *
   * @param tenantId the ID for the tenant the employment status reference data is specific to
   * @param localeId the Unicode locale identifier for the locale to retrieve the employment status
   *     reference data for
   * @return the employment status reference data
   * @throws InvalidArgumentException if an argument is invalid
   * @throws ServiceUnavailableException if the employment status reference data could not be
   *     retrieved
   */
  @WebMethod(operationName = "GetEmploymentStatuses")
  @WebResult(name = "EmploymentStatus")
  public List<EmploymentStatus> getEmploymentStatuses(
      @WebParam(name = "TenantId") @XmlElement(required = true) UUID tenantId,
      @WebParam(name = "LocaleId") @XmlElement(required = true) String localeId)
      throws InvalidArgumentException, ServiceUnavailableException {
    return partyReferenceService.getEmploymentStatuses(tenantId, localeId);
  }

  /**
   * Retrieve the employment type reference data for a specific locale.
   *
   * @param tenantId the ID for the tenant the employment type reference data is specific to
   * @param localeId the Unicode locale identifier for the locale to retrieve the employment type
   *     reference data for
   * @return the employment type reference data
   * @throws InvalidArgumentException if an argument is invalid
   * @throws ServiceUnavailableException if the employment type reference data could not be
   *     retrieved
   */
  @WebMethod(operationName = "GetEmploymentTypes")
  @WebResult(name = "EmploymentType")
  public List<EmploymentType> getEmploymentTypes(
      @WebParam(name = "TenantId") @XmlElement(required = true) UUID tenantId,
      @WebParam(name = "LocaleId") @XmlElement(required = true) String localeId)
      throws InvalidArgumentException, ServiceUnavailableException {
    return partyReferenceService.getEmploymentTypes(tenantId, localeId);
  }

  /**
   * Retrieve the external reference type reference data for a specific locale.
   *
   * @param tenantId the ID for the tenant the external reference type reference data is specific to
   * @param localeId the Unicode locale identifier for the locale to retrieve the contact mechanism
   *     type reference data for
   * @return the external reference type reference data
   * @throws InvalidArgumentException if an argument is invalid
   * @throws ServiceUnavailableException if the external reference type reference data could not be
   *     retrieved
   */
  @WebMethod(operationName = "GetExternalReferenceTypes")
  @WebResult(name = "ExternalReferenceType")
  public List<ExternalReferenceType> getExternalReferenceTypes(
      @WebParam(name = "TenantId") @XmlElement(required = true) UUID tenantId,
      @WebParam(name = "LocaleId") @XmlElement(required = true) String localeId)
      throws InvalidArgumentException, ServiceUnavailableException {
    return partyReferenceService.getExternalReferenceTypes(tenantId, localeId);
  }

  /**
   * Retrieve the fields of study reference data for a specific locale.
   *
   * @param tenantId the ID for the tenant the fields of study reference data is specific to
   * @param localeId the Unicode locale identifier for the locale to retrieve the fields of study
   *     reference data for
   * @return the fields of study reference data
   * @throws InvalidArgumentException if an argument is invalid
   * @throws ServiceUnavailableException if the fields of study reference data could not be
   *     retrieved
   */
  @WebMethod(operationName = "GetFieldsOfStudy")
  @WebResult(name = "FieldOfStudy")
  public List<FieldOfStudy> getFieldsOfStudy(
      @WebParam(name = "TenantId") @XmlElement(required = true) UUID tenantId,
      @WebParam(name = "LocaleId") @XmlElement(required = true) String localeId)
      throws InvalidArgumentException, ServiceUnavailableException {
    return partyReferenceService.getFieldsOfStudy(tenantId, localeId);
  }

  /**
   * Retrieve the gender reference data for a specific locale.
   *
   * @param tenantId the ID for the tenant the gender reference data is specific to
   * @param localeId the Unicode locale identifier for the locale to retrieve the gender reference
   *     data for
   * @return the gender reference data
   * @throws InvalidArgumentException if an argument is invalid
   * @throws ServiceUnavailableException if the gender reference data could not be retrieved
   */
  @WebMethod(operationName = "GetGenders")
  @WebResult(name = "Gender")
  public List<Gender> getGenders(
      @WebParam(name = "TenantId") @XmlElement(required = true) UUID tenantId,
      @WebParam(name = "LocaleId") @XmlElement(required = true) String localeId)
      throws InvalidArgumentException, ServiceUnavailableException {
    return partyReferenceService.getGenders(tenantId, localeId);
  }

  /**
   * Retrieve the identification type reference data for a specific locale.
   *
   * @param tenantId the ID for the tenant the identification type reference data is specific to
   * @param localeId the Unicode locale identifier for the locale to retrieve the identification
   *     type reference data for
   * @return the identification type reference data
   * @throws InvalidArgumentException if an argument is invalid
   * @throws ServiceUnavailableException if the identification type reference data could not be
   *     retrieved
   */
  @WebMethod(operationName = "GetIdentificationTypes")
  @WebResult(name = "IdentificationType")
  public List<IdentificationType> getIdentificationTypes(
      @WebParam(name = "TenantId") @XmlElement(required = true) UUID tenantId,
      @WebParam(name = "LocaleId") @XmlElement(required = true) String localeId)
      throws InvalidArgumentException, ServiceUnavailableException {
    return partyReferenceService.getIdentificationTypes(tenantId, localeId);
  }

  /**
   * Retrieve the industry classification category reference data for a specific locale.
   *
   * @param tenantId the ID for the tenant the industry classification category reference data is
   *     specific to
   * @param localeId the Unicode locale identifier for the locale to retrieve the industry
   *     classification category reference data for
   * @return the industry classification category reference data
   * @throws InvalidArgumentException if an argument is invalid
   * @throws ServiceUnavailableException if the industry classification category reference data
   *     could not be retrieved
   */
  @WebMethod(operationName = "GetIndustryClassificationCategories")
  @WebResult(name = "IndustryClassificationCategory")
  public List<IndustryClassificationCategory> getIndustryClassificationCategories(
      @WebParam(name = "TenantId") @XmlElement(required = true) UUID tenantId,
      @WebParam(name = "LocaleId") @XmlElement(required = true) String localeId)
      throws InvalidArgumentException, ServiceUnavailableException {
    return partyReferenceService.getIndustryClassificationCategories(tenantId, localeId);
  }

  /**
   * Retrieve the industry classification system reference data for a specific locale.
   *
   * @param tenantId the ID for the tenant the industry classification system reference data is
   *     specific to
   * @param localeId the Unicode locale identifier for the locale to retrieve the industry
   *     classification system reference data for
   * @return the industry classification system reference data
   * @throws InvalidArgumentException if an argument is invalid
   * @throws ServiceUnavailableException if the industry classification system reference data could
   *     not be retrieved
   */
  @WebMethod(operationName = "GetIndustryClassificationSystems")
  @WebResult(name = "IndustryClassificationSystem")
  public List<IndustryClassificationSystem> getIndustryClassificationSystems(
      @WebParam(name = "TenantId") @XmlElement(required = true) UUID tenantId,
      @WebParam(name = "LocaleId") @XmlElement(required = true) String localeId)
      throws InvalidArgumentException, ServiceUnavailableException {
    return partyReferenceService.getIndustryClassificationSystems(tenantId, localeId);
  }

  /**
   * Retrieve the industry classification reference data for a specific locale.
   *
   * @param tenantId the ID for the tenant the industry classification reference data is specific to
   * @param localeId the Unicode locale identifier for the locale to retrieve the industry
   *     classification reference data for
   * @return the industry classification reference data
   * @throws InvalidArgumentException if an argument is invalid
   * @throws ServiceUnavailableException if the industry classification reference data could not be
   *     retrieved
   */
  @WebMethod(operationName = "GetIndustryClassifications")
  @WebResult(name = "IndustryClassification")
  public List<IndustryClassification> getIndustryClassifications(
      @WebParam(name = "TenantId") @XmlElement(required = true) UUID tenantId,
      @WebParam(name = "LocaleId") @XmlElement(required = true) String localeId)
      throws InvalidArgumentException, ServiceUnavailableException {
    return partyReferenceService.getIndustryClassifications(tenantId, localeId);
  }

  /**
   * Retrieve the link type reference data for a specific locale.
   *
   * @param tenantId the ID for the tenant the link type reference data is specific to
   * @param localeId the Unicode locale identifier for the locale to retrieve the link type
   *     reference data for
   * @return the link type reference data
   * @throws InvalidArgumentException if an argument is invalid
   * @throws ServiceUnavailableException if the link type reference data could not be retrieved
   */
  @WebMethod(operationName = "GetLinkTypes")
  @WebResult(name = "LinkType")
  public List<LinkType> getLinkTypes(
      @WebParam(name = "TenantId") @XmlElement(required = true) UUID tenantId,
      @WebParam(name = "LocaleId") @XmlElement(required = true) String localeId)
      throws InvalidArgumentException, ServiceUnavailableException {
    return partyReferenceService.getLinkTypes(tenantId, localeId);
  }

  /**
   * Retrieve the lock type category reference data for a specific locale.
   *
   * @param tenantId the ID for the tenant the lock type category reference data is specific to
   * @param localeId the Unicode locale identifier for the locale to retrieve the lock type category
   *     reference data for
   * @return the lock type category reference data
   * @throws InvalidArgumentException if an argument is invalid
   * @throws ServiceUnavailableException if the lock type category reference data could not be
   *     retrieved
   */
  @WebMethod(operationName = "GetLockTypeCategories")
  @WebResult(name = "LockTypeCategory")
  public List<LockTypeCategory> getLockTypeCategories(
      @WebParam(name = "TenantId") @XmlElement(required = true) UUID tenantId,
      @WebParam(name = "LocaleId") @XmlElement(required = true) String localeId)
      throws InvalidArgumentException, ServiceUnavailableException {
    return partyReferenceService.getLockTypeCategories(tenantId, localeId);
  }

  /**
   * Retrieve the lock type reference data for a specific locale.
   *
   * @param tenantId the ID for the tenant the lock type reference data is specific to
   * @param localeId the Unicode locale identifier for the locale to retrieve the lock type
   *     reference data for
   * @return the lock type reference data
   * @throws InvalidArgumentException if an argument is invalid
   * @throws ServiceUnavailableException if the lock type reference data could not be retrieved
   */
  @WebMethod(operationName = "GetLockTypes")
  @WebResult(name = "LockType")
  public List<LockType> getLockTypes(
      @WebParam(name = "TenantId") @XmlElement(required = true) UUID tenantId,
      @WebParam(name = "LocaleId") @XmlElement(required = true) String localeId)
      throws InvalidArgumentException, ServiceUnavailableException {
    return partyReferenceService.getLockTypes(tenantId, localeId);
  }

  /**
   * Retrieve the mandatary role reference data for a specific locale.
   *
   * @param tenantId the ID for the tenant the mandatary role reference data is specific to
   * @param localeId the Unicode locale identifier for the locale to retrieve the mandatary role
   *     reference data for
   * @return the mandatary role reference data
   * @throws InvalidArgumentException if an argument is invalid
   * @throws ServiceUnavailableException if the mandatary role reference data could not be retrieved
   */
  @WebMethod(operationName = "GetMandataryRoles")
  @WebResult(name = "MandataryRole")
  public List<MandataryRole> getMandataryRoles(
      @WebParam(name = "TenantId") @XmlElement(required = true) UUID tenantId,
      @WebParam(name = "LocaleId") @XmlElement(required = true) String localeId)
      throws InvalidArgumentException, ServiceUnavailableException {
    return partyReferenceService.getMandataryRoles(tenantId, localeId);
  }

  /**
   * Retrieve the mandate property type reference data for a specific locale.
   *
   * @param tenantId the ID for the tenant the mandate property type reference data is specific to
   * @param localeId the Unicode locale identifier for the locale to retrieve the mandate property
   *     type reference data for
   * @return the mandate property type reference data
   * @throws InvalidArgumentException if an argument is invalid
   * @throws ServiceUnavailableException if the mandate property type reference data could not be
   *     retrieved
   */
  @WebMethod(operationName = "GetMandatePropertyTypes")
  @WebResult(name = "MandatePropertyType")
  public List<MandatePropertyType> getMandatePropertyTypes(
      @WebParam(name = "TenantId") @XmlElement(required = true) UUID tenantId,
      @WebParam(name = "LocaleId") @XmlElement(required = true) String localeId)
      throws InvalidArgumentException, ServiceUnavailableException {
    return partyReferenceService.getMandatePropertyTypes(tenantId, localeId);
  }

  /**
   * Retrieve the mandate type reference data for a specific locale.
   *
   * @param tenantId the ID for the tenant the mandate type reference data is specific to
   * @param localeId the Unicode locale identifier for the locale to retrieve the mandate type
   *     reference data for
   * @return the mandate type reference data
   * @throws InvalidArgumentException if an argument is invalid
   * @throws ServiceUnavailableException if the mandate type reference data could not be retrieved
   */
  @WebMethod(operationName = "GetMandateTypes")
  @WebResult(name = "MandateType")
  public List<MandateType> getMandateTypes(
      @WebParam(name = "TenantId") @XmlElement(required = true) UUID tenantId,
      @WebParam(name = "LocaleId") @XmlElement(required = true) String localeId)
      throws InvalidArgumentException, ServiceUnavailableException {
    return partyReferenceService.getMandateTypes(tenantId, localeId);
  }

  /**
   * Retrieve the marital status reference data for a specific locale.
   *
   * @param tenantId the ID for the tenant the marital status reference data is specific to
   * @param localeId the Unicode locale identifier for the locale to retrieve the marital status
   *     reference data for
   * @return the marital status reference data
   * @throws InvalidArgumentException if an argument is invalid
   * @throws ServiceUnavailableException if the marital status reference data could not be retrieved
   */
  @WebMethod(operationName = "GetMaritalStatuses")
  @WebResult(name = "MaritalStatus")
  public List<MaritalStatus> getMaritalStatuses(
      @WebParam(name = "TenantId") @XmlElement(required = true) UUID tenantId,
      @WebParam(name = "LocaleId") @XmlElement(required = true) String localeId)
      throws InvalidArgumentException, ServiceUnavailableException {
    return partyReferenceService.getMaritalStatuses(tenantId, localeId);
  }

  /**
   * Retrieve the marriage type reference data for a specific locale.
   *
   * @param tenantId the ID for the tenant the marriage type reference data is specific to
   * @param localeId the Unicode locale identifier for the locale to retrieve the marriage type
   *     reference data
   * @return the marriage type reference data
   * @throws InvalidArgumentException if an argument is invalid
   * @throws ServiceUnavailableException if the marriage type reference data could not be retrieved
   */
  @WebMethod(operationName = "GetMarriageTypes")
  @WebResult(name = "MarriageType")
  public List<MarriageType> getMarriageTypes(
      @WebParam(name = "TenantId") @XmlElement(required = true) UUID tenantId,
      @WebParam(name = "LocaleId") @XmlElement(required = true) String localeId)
      throws InvalidArgumentException, ServiceUnavailableException {
    return partyReferenceService.getMarriageTypes(tenantId, localeId);
  }

  /**
   * Retrieve the next of kin type reference data for a specific locale.
   *
   * @param tenantId the ID for the tenant the next of kin type reference data is specific to
   * @param localeId the Unicode locale identifier for the locale to retrieve the next of kin type
   *     reference data for
   * @return the next of kin type reference data
   * @throws InvalidArgumentException if an argument is invalid
   * @throws ServiceUnavailableException if the next of kin type reference data could not be
   *     retrieved
   */
  @WebMethod(operationName = "GetNextOfKinTypes")
  @WebResult(name = "NextOfKinType")
  public List<NextOfKinType> getNextOfKinTypes(
      @WebParam(name = "TenantId") @XmlElement(required = true) UUID tenantId,
      @WebParam(name = "LocaleId") @XmlElement(required = true) String localeId)
      throws InvalidArgumentException, ServiceUnavailableException {
    return partyReferenceService.getNextOfKinTypes(tenantId, localeId);
  }

  /**
   * Retrieve the occupation reference data for a specific locale.
   *
   * @param tenantId the ID for the tenant the occupation reference data is specific to
   * @param localeId the Unicode locale identifier for the locale to retrieve the occupation
   *     reference data for
   * @return the occupation reference data
   * @throws InvalidArgumentException if an argument is invalid
   * @throws ServiceUnavailableException if the occupation reference data could not be retrieved
   */
  @WebMethod(operationName = "GetOccupations")
  @WebResult(name = "Occupation")
  public List<Occupation> getOccupations(
      @WebParam(name = "TenantId") @XmlElement(required = true) UUID tenantId,
      @WebParam(name = "LocaleId") @XmlElement(required = true) String localeId)
      throws InvalidArgumentException, ServiceUnavailableException {
    return partyReferenceService.getOccupations(tenantId, localeId);
  }

  /**
   * Retrieve the physical address purpose reference data for a specific locale.
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
  @WebMethod(operationName = "GetPhysicalAddressPurposes")
  @WebResult(name = "PhysicalAddressPurpose")
  public List<PhysicalAddressPurpose> getPhysicalAddressPurposes(
      @WebParam(name = "TenantId") @XmlElement(required = true) UUID tenantId,
      @WebParam(name = "LocaleId") @XmlElement(required = true) String localeId)
      throws InvalidArgumentException, ServiceUnavailableException {
    return partyReferenceService.getPhysicalAddressPurposes(tenantId, localeId);
  }

  /**
   * Retrieve the physical address role reference data for a specific locale.
   *
   * @param tenantId the ID for the tenant the physical address role reference data is specific to
   * @param localeId the Unicode locale identifier for the locale to retrieve the physical address
   *     role reference data for
   * @return the physical address role reference data
   * @throws InvalidArgumentException if an argument is invalid
   * @throws ServiceUnavailableException if the physical address role reference data could not be
   *     retrieved
   */
  @WebMethod(operationName = "GetPhysicalAddressRoles")
  @WebResult(name = "PhysicalAddressRole")
  public List<PhysicalAddressRole> getPhysicalAddressRoles(
      @WebParam(name = "TenantId") @XmlElement(required = true) UUID tenantId,
      @WebParam(name = "LocaleId") @XmlElement(required = true) String localeId)
      throws InvalidArgumentException, ServiceUnavailableException {
    return partyReferenceService.getPhysicalAddressRoles(tenantId, localeId);
  }

  /**
   * Retrieve the physical address type reference data for a specific locale.
   *
   * @param tenantId the ID for the tenant the physical address type reference data is specific to
   * @param localeId the Unicode locale identifier for the locale to retrieve the physical address
   *     type reference data for
   * @return the physical address type reference data
   * @throws InvalidArgumentException if an argument is invalid
   * @throws ServiceUnavailableException if the physical address type reference data could not be
   *     retrieved
   */
  @WebMethod(operationName = "GetPhysicalAddressTypes")
  @WebResult(name = "PhysicalAddressType")
  public List<PhysicalAddressType> getPhysicalAddressTypes(
      @WebParam(name = "TenantId") @XmlElement(required = true) UUID tenantId,
      @WebParam(name = "LocaleId") @XmlElement(required = true) String localeId)
      throws InvalidArgumentException, ServiceUnavailableException {
    return partyReferenceService.getPhysicalAddressTypes(tenantId, localeId);
  }

  /**
   * Retrieve the preference type category reference data for a specific locale.
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
  @WebMethod(operationName = "GetPreferenceTypeCategories")
  @WebResult(name = "PreferenceTypeCategory")
  public List<PreferenceTypeCategory> getPreferenceTypeCategories(
      @WebParam(name = "TenantId") @XmlElement(required = true) UUID tenantId,
      @WebParam(name = "LocaleId") @XmlElement(required = true) String localeId)
      throws InvalidArgumentException, ServiceUnavailableException {
    return partyReferenceService.getPreferenceTypeCategories(tenantId, localeId);
  }

  /**
   * Retrieve the preference type reference data for a specific locale.
   *
   * @param tenantId the ID for the tenant the preference type reference data is specific to
   * @param localeId the Unicode locale identifier for the locale to retrieve the preference type
   *     reference data for
   * @return the preference type reference data
   * @throws InvalidArgumentException if an argument is invalid
   * @throws ServiceUnavailableException if the preference type reference data could not be
   *     retrieved
   */
  @WebMethod(operationName = "GetPreferenceTypes")
  @WebResult(name = "PreferenceType")
  public List<PreferenceType> getPreferenceTypes(
      @WebParam(name = "TenantId") @XmlElement(required = true) UUID tenantId,
      @WebParam(name = "LocaleId") @XmlElement(required = true) String localeId)
      throws InvalidArgumentException, ServiceUnavailableException {
    return partyReferenceService.getPreferenceTypes(tenantId, localeId);
  }

  /**
   * Retrieve the qualification type reference data for a specific locale.
   *
   * @param tenantId the ID for the tenant the qualification type reference data is specific to
   * @param localeId the Unicode locale identifier for the locale to retrieve the qualification type
   *     reference data for
   * @return the qualification type reference data
   * @throws InvalidArgumentException if an argument is invalid
   * @throws ServiceUnavailableException if the qualification type reference data could not be
   *     retrieved
   */
  @WebMethod(operationName = "GetQualificationTypes")
  @WebResult(name = "QualificationType")
  public List<QualificationType> getQualificationTypes(
      @WebParam(name = "TenantId") @XmlElement(required = true) UUID tenantId,
      @WebParam(name = "LocaleId") @XmlElement(required = true) String localeId)
      throws InvalidArgumentException, ServiceUnavailableException {
    return partyReferenceService.getQualificationTypes(tenantId, localeId);
  }

  /**
   * Retrieve the race reference data for a specific locale.
   *
   * @param tenantId the ID for the tenant the race reference data is specific to
   * @param localeId the Unicode locale identifier for the locale to retrieve the race reference
   *     data for
   * @return the race reference data
   * @throws InvalidArgumentException if an argument is invalid
   * @throws ServiceUnavailableException if the race reference data could not be retrieved
   */
  @WebMethod(operationName = "GetRaces")
  @WebResult(name = "Race")
  public List<Race> getRaces(
      @WebParam(name = "TenantId") @XmlElement(required = true) UUID tenantId,
      @WebParam(name = "LocaleId") @XmlElement(required = true) String localeId)
      throws InvalidArgumentException, ServiceUnavailableException {
    return partyReferenceService.getRaces(tenantId, localeId);
  }

  /**
   * Retrieve the residence permit type reference data for a specific locale.
   *
   * @param tenantId the ID for the tenant the residence permit type reference data is specific to
   * @param localeId the Unicode locale identifier for the locale to retrieve the residence permit
   *     type reference data for
   * @return the residence permit type reference data
   * @throws InvalidArgumentException if an argument is invalid
   * @throws ServiceUnavailableException if the residence permit type reference data could not be
   *     retrieved
   */
  @WebMethod(operationName = "GetResidencePermitTypes")
  @WebResult(name = "ResidencePermitType")
  public List<ResidencePermitType> getResidencePermitTypes(
      @WebParam(name = "TenantId") @XmlElement(required = true) UUID tenantId,
      @WebParam(name = "LocaleId") @XmlElement(required = true) String localeId)
      throws InvalidArgumentException, ServiceUnavailableException {
    return partyReferenceService.getResidencePermitTypes(tenantId, localeId);
  }

  /**
   * Retrieve the residency status reference data for a specific locale.
   *
   * @param tenantId the ID for the tenant the residency status reference data is specific to
   * @param localeId the Unicode locale identifier for the locale to retrieve the residency status
   *     reference data for
   * @return the residency status reference data
   * @throws InvalidArgumentException if an argument is invalid
   * @throws ServiceUnavailableException if the residency status reference data could not be
   *     retrieved
   */
  @WebMethod(operationName = "GetResidencyStatuses")
  @WebResult(name = "ResidencyStatus")
  public List<ResidencyStatus> getResidencyStatuses(
      @WebParam(name = "TenantId") @XmlElement(required = true) UUID tenantId,
      @WebParam(name = "LocaleId") @XmlElement(required = true) String localeId)
      throws InvalidArgumentException, ServiceUnavailableException {
    return partyReferenceService.getResidencyStatuses(tenantId, localeId);
  }

  /**
   * Retrieve the residential type reference data for a specific locale.
   *
   * @param tenantId the ID for the tenant the residential type reference data is specific to
   * @param localeId the Unicode locale identifier for the locale to retrieve the residential type
   *     reference data for
   * @return the residential type reference data
   * @throws InvalidArgumentException if an argument is invalid
   * @throws ServiceUnavailableException if the residential type reference data could not be
   *     retrieved
   */
  @WebMethod(operationName = "GetResidentialTypes")
  @WebResult(name = "ResidentialType")
  public List<ResidentialType> getResidentialTypes(
      @WebParam(name = "TenantId") @XmlElement(required = true) UUID tenantId,
      @WebParam(name = "LocaleId") @XmlElement(required = true) String localeId)
      throws InvalidArgumentException, ServiceUnavailableException {
    return partyReferenceService.getResidentialTypes(tenantId, localeId);
  }

  /**
   * Retrieve the role purpose reference data for a specific locale.
   *
   * @param tenantId the ID for the tenant the role purpose reference data is specific to
   * @param localeId the Unicode locale identifier for the locale to retrieve the role purpose
   *     reference data for
   * @return the role purpose reference data
   * @throws InvalidArgumentException if an argument is invalid
   * @throws ServiceUnavailableException if the role purpose reference data could not be retrieved
   */
  @WebMethod(operationName = "GetRolePurposes")
  @WebResult(name = "RolePurpose")
  public List<RolePurpose> getRolePurposes(
      @WebParam(name = "TenantId") @XmlElement(required = true) UUID tenantId,
      @WebParam(name = "LocaleId") @XmlElement(required = true) String localeId)
      throws InvalidArgumentException, ServiceUnavailableException {
    return partyReferenceService.getRolePurposes(tenantId, localeId);
  }

  /**
   * Retrieve the role type attribute type constraint for a specific role type.
   *
   * @param roleType the code for the role type to retrieve the attribute constraint for
   * @return the role type attribute type constraint
   * @throws InvalidArgumentException if an argument is invalid
   * @throws ServiceUnavailableException if the role type attribute type constraint for a specific
   *     role type could not be retrieved
   */
  @WebMethod(operationName = "GetRoleTypeAttributeTypeConstraints")
  @WebResult(name = "RoleTypeAttributeTypeConstraint")
  public List<RoleTypeAttributeTypeConstraint> getRoleTypeAttributeTypeConstraints(
      @WebParam(name = "RoleType") @XmlElement String roleType)
      throws InvalidArgumentException, ServiceUnavailableException {
    return partyReferenceService.getRoleTypeAttributeTypeConstraints(roleType);
  }

  /**
   * Retrieve the role type preference type constraint for a specific role type.
   *
   * @param roleType the code for the role type to retrieve the preference constraint for
   * @return the role type preference type constraint
   * @throws InvalidArgumentException if an argument is invalid
   * @throws ServiceUnavailableException if the role type preference type constraint for a specific
   *     role type could not be retrieved
   */
  @WebMethod(operationName = "GetRoleTypePreferenceTypeConstraints")
  @WebResult(name = "RoleTypePreferenceTypeConstraint")
  public List<RoleTypePreferenceTypeConstraint> getRoleTypePreferenceTypeConstraints(
      @WebParam(name = "RoleType") @XmlElement String roleType)
      throws InvalidArgumentException, ServiceUnavailableException {
    return partyReferenceService.getRoleTypePreferenceTypeConstraints(roleType);
  }

  /**
   * Retrieve the role type reference data for a specific locale.
   *
   * @param tenantId the ID for the tenant the role type reference data is specific to
   * @param localeId the Unicode locale identifier for the locale to retrieve the role type
   *     reference data for
   * @return the role type reference data
   * @throws InvalidArgumentException if an argument is invalid
   * @throws ServiceUnavailableException if the role type reference data could not be retrieved
   */
  @WebMethod(operationName = "GetRoleTypes")
  @WebResult(name = "RoleType")
  public List<RoleType> getRoleTypes(
      @WebParam(name = "TenantId") @XmlElement(required = true) UUID tenantId,
      @WebParam(name = "LocaleId") @XmlElement(required = true) String localeId)
      throws InvalidArgumentException, ServiceUnavailableException {
    return partyReferenceService.getRoleTypes(tenantId, localeId);
  }

  /**
   * Retrieve the segmentation type reference data for a specific locale.
   *
   * @param tenantId the ID for the tenant the segmentation type reference data is specific to
   * @param localeId the Unicode locale identifier for the locale to retrieve the segmentation type
   *     reference data for
   * @return the segmentation type reference data
   * @throws InvalidArgumentException if an argument is invalid
   * @throws ServiceUnavailableException if the segmentation type reference data could not be
   *     retrieved
   */
  @WebMethod(operationName = "GetSegmentationTypes")
  @WebResult(name = "SegmentationType")
  public List<SegmentationType> getSegmentationTypes(
      @WebParam(name = "TenantId") @XmlElement(required = true) UUID tenantId,
      @WebParam(name = "LocaleId") @XmlElement(required = true) String localeId)
      throws InvalidArgumentException, ServiceUnavailableException {
    return partyReferenceService.getSegmentationTypes(tenantId, localeId);
  }

  /**
   * Retrieve the segment reference data for a specific locale.
   *
   * @param tenantId the ID for the tenant the segment reference data is specific to
   * @param localeId the Unicode locale identifier for the locale to retrieve the segment reference
   *     data for
   * @return the segment reference data
   * @throws InvalidArgumentException if an argument is invalid
   * @throws ServiceUnavailableException if the segment reference data could not be retrieved
   */
  @WebMethod(operationName = "GetSegments")
  @WebResult(name = "Segment")
  public List<Segment> getSegments(
      @WebParam(name = "TenantId") @XmlElement(required = true) UUID tenantId,
      @WebParam(name = "LocaleId") @XmlElement(required = true) String localeId)
      throws InvalidArgumentException, ServiceUnavailableException {
    return partyReferenceService.getSegments(tenantId, localeId);
  }

  /**
   * Retrieve the skill type reference data for a specific locale.
   *
   * @param tenantId the ID for the tenant the skill type reference data is specific to
   * @param localeId the Unicode locale identifier for the locale to retrieve the skill type
   *     reference data for
   * @return the skill type reference data
   * @throws InvalidArgumentException if an argument is invalid
   * @throws ServiceUnavailableException if the skill type reference data could not be retrieved
   */
  @WebMethod(operationName = "GetSkillTypes")
  @WebResult(name = "SkillType")
  public List<SkillType> getSkillTypes(
      @WebParam(name = "TenantId") @XmlElement(required = true) UUID tenantId,
      @WebParam(name = "LocaleId") @XmlElement(required = true) String localeId)
      throws InvalidArgumentException, ServiceUnavailableException {
    return partyReferenceService.getSkillTypes(tenantId, localeId);
  }

  /**
   * Retrieve the source of funds type reference data for a specific locale.
   *
   * @param tenantId the ID for the tenant the source of funds type reference data is specific to
   * @param localeId the Unicode locale identifier for the locale to retrieve the source of funds
   *     type reference data
   * @return the source of funds type reference data
   * @throws InvalidArgumentException if an argument is invalid
   * @throws ServiceUnavailableException if the source of funds type reference data could not be
   *     retrieved
   */
  @WebMethod(operationName = "GetSourceOfFundsTypes")
  @WebResult(name = "SourceOfFundsType")
  public List<SourceOfFundsType> getSourceOfFundsTypes(
      @WebParam(name = "TenantId") @XmlElement(required = true) UUID tenantId,
      @WebParam(name = "LocaleId") @XmlElement(required = true) String localeId)
      throws InvalidArgumentException, ServiceUnavailableException {
    return partyReferenceService.getSourceOfFundsTypes(tenantId, localeId);
  }

  /**
   * Retrieve the source of wealth type reference data for a specific locale.
   *
   * @param tenantId the ID for the tenant the source of wealth type reference data is specific to
   * @param localeId the Unicode locale identifier for the locale to retrieve the source of wealth
   *     type reference data for
   * @return the source of wealth type reference data
   * @throws InvalidArgumentException if an argument is invalid
   * @throws ServiceUnavailableException if the source of wealth type reference data could not be
   *     retrieved
   */
  @WebMethod(operationName = "GetSourceOfWealthTypes")
  @WebResult(name = "SourceOfWealthType")
  public List<SourceOfWealthType> getSourceOfWealthTypes(
      @WebParam(name = "TenantId") @XmlElement(required = true) UUID tenantId,
      @WebParam(name = "LocaleId") @XmlElement(required = true) String localeId)
      throws InvalidArgumentException, ServiceUnavailableException {
    return partyReferenceService.getSourceOfWealthTypes(tenantId, localeId);
  }

  /**
   * Retrieve the status type category reference data for a specific locale.
   *
   * @param tenantId the ID for the tenant the status type category reference data is specific to
   * @param localeId the Unicode locale identifier for the locale to retrieve the status type
   *     category reference data for
   * @return the status type category reference data
   * @throws InvalidArgumentException if an argument is invalid
   * @throws ServiceUnavailableException if the status type category reference data could not be
   *     retrieved
   */
  @WebMethod(operationName = "GetStatusTypeCategories")
  @WebResult(name = "StatusTypeCategory")
  public List<StatusTypeCategory> getStatusTypeCategories(
      @WebParam(name = "TenantId") @XmlElement(required = true) UUID tenantId,
      @WebParam(name = "LocaleId") @XmlElement(required = true) String localeId)
      throws InvalidArgumentException, ServiceUnavailableException {
    return partyReferenceService.getStatusTypeCategories(tenantId, localeId);
  }

  /**
   * Retrieve the status type reference data for a specific locale.
   *
   * @param tenantId the ID for the tenant the status type reference data is specific to
   * @param localeId the Unicode locale identifier for the locale to retrieve the status type
   *     reference data for
   * @return the status type reference data
   * @throws InvalidArgumentException if an argument is invalid
   * @throws ServiceUnavailableException if the status type reference data could not be retrieved
   */
  @WebMethod(operationName = "GetStatusTypes")
  @WebResult(name = "StatusType")
  public List<StatusType> getStatusTypes(
      @WebParam(name = "TenantId") @XmlElement(required = true) UUID tenantId,
      @WebParam(name = "LocaleId") @XmlElement(required = true) String localeId)
      throws InvalidArgumentException, ServiceUnavailableException {
    return partyReferenceService.getStatusTypes(tenantId, localeId);
  }

  /**
   * Retrieve the tax number type reference data for a specific locale.
   *
   * @param tenantId the ID for the tenant the tax number type reference data is specific to
   * @param localeId the Unicode locale identifier for the locale to retrieve the tax number type
   *     reference data for
   * @return the tax number type reference data
   * @throws InvalidArgumentException if an argument is invalid
   * @throws ServiceUnavailableException if the tax number type reference data could not be
   *     retrieved
   */
  @WebMethod(operationName = "GetTaxNumberTypes")
  @WebResult(name = "TaxNumberType")
  public List<TaxNumberType> getTaxNumberTypes(
      @WebParam(name = "TenantId") @XmlElement(required = true) UUID tenantId,
      @WebParam(name = "LocaleId") @XmlElement(required = true) String localeId)
      throws InvalidArgumentException, ServiceUnavailableException {
    return partyReferenceService.getTaxNumberTypes(tenantId, localeId);
  }

  /**
   * Retrieve the times to contact reference data for a specific locale.
   *
   * @param tenantId the ID for the tenant the times to contact reference data is specific to
   * @param localeId the Unicode locale identifier for the locale to retrieve the times to contact
   *     reference data for
   * @return the times to contact reference data
   * @throws InvalidArgumentException if an argument is invalid
   * @throws ServiceUnavailableException if the times to contact reference data could not be
   *     retrieved
   */
  @WebMethod(operationName = "GetTimesToContact")
  @WebResult(name = "TimeToContact")
  public List<TimeToContact> getTimesToContact(
      @WebParam(name = "TenantId") @XmlElement(required = true) UUID tenantId,
      @WebParam(name = "LocaleId") @XmlElement(required = true) String localeId)
      throws InvalidArgumentException, ServiceUnavailableException {
    return partyReferenceService.getTimesToContact(tenantId, localeId);
  }

  /**
   * Retrieve the title reference data for a specific locale.
   *
   * @param tenantId the ID for the tenant the title reference data is specific to
   * @param localeId the Unicode locale identifier for the locale to retrieve the title reference
   *     data for
   * @return the title reference data
   * @throws InvalidArgumentException if an argument is invalid
   * @throws ServiceUnavailableException if the title reference data could not be retrieved
   */
  @WebMethod(operationName = "GetTitles")
  @WebResult(name = "Title")
  public List<Title> getTitles(
      @WebParam(name = "TenantId") @XmlElement(required = true) UUID tenantId,
      @WebParam(name = "LocaleId") @XmlElement(required = true) String localeId)
      throws InvalidArgumentException, ServiceUnavailableException {
    return partyReferenceService.getTitles(tenantId, localeId);
  }
}
