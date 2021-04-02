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
   * Retrieve the attribute type categories.
   *
   * @param localeId the Unicode locale identifier for the locale to retrieve the attribute type
   *     categories for or <b>null</b> to retrieve the attribute type categories for all locales
   * @return the attribute type categories
   */
  @WebMethod(operationName = "GetAttributeTypeCategories")
  public List<AttributeTypeCategory> getAttributeTypeCategories(
      @WebParam(name = "LocaleId") @XmlElement String localeId) throws ServiceUnavailableException {
    return partyReferenceService.getAttributeTypeCategories(localeId);
  }

  /**
   * Retrieve the attribute types.
   *
   * @param localeId the Unicode locale identifier for the locale to retrieve the attribute types
   *     for or <b>null</b> to retrieve the attribute types for all locales
   * @return the attribute types
   */
  @WebMethod(operationName = "GetAttributeTypes")
  public List<AttributeType> getAttributeTypes(
      @WebParam(name = "LocaleId") @XmlElement String localeId) throws ServiceUnavailableException {
    return partyReferenceService.getAttributeTypes(localeId);
  }

  /**
   * Retrieve the consent types.
   *
   * @param localeId the Unicode locale identifier for the locale to retrieve the consent types for
   *     or <b>null</b> to retrieve the consent types for all locales
   * @return the consent types
   */
  @WebMethod(operationName = "GetConsentTypes")
  public List<ConsentType> getConsentTypes(@WebParam(name = "LocaleId") @XmlElement String localeId)
      throws ServiceUnavailableException {
    return partyReferenceService.getConsentTypes(localeId);
  }

  /**
   * Retrieve the contact mechanism purposes.
   *
   * @param localeId the Unicode locale identifier for the locale to retrieve the contact mechanism
   *     purposes for or <b>null</b> to retrieve the contact mechanism purposes for all locales
   * @return the contact mechanism purposes
   */
  @WebMethod(operationName = "GetContactMechanismPurposes")
  public List<ContactMechanismPurpose> getContactMechanismPurposes(
      @WebParam(name = "LocaleId") @XmlElement String localeId) throws ServiceUnavailableException {
    return partyReferenceService.getContactMechanismPurposes(localeId);
  }

  /**
   * Retrieve the contact mechanism roles.
   *
   * @param localeId the Unicode locale identifier for the locale to retrieve the contact mechanism
   *     purposes for or <b>null</b> to retrieve the contact mechanism roles for all locales
   * @return the contact mechanism roles
   */
  @WebMethod(operationName = "GetContactMechanismRoles")
  public List<ContactMechanismRole> getContactMechanismRoles(
      @WebParam(name = "LocaleId") @XmlElement String localeId) throws ServiceUnavailableException {
    return partyReferenceService.getContactMechanismRoles(localeId);
  }

  /**
   * Retrieve the contact mechanism types.
   *
   * @param localeId the Unicode locale identifier for the locale to retrieve the contact mechanism
   *     types for or <b>null</b> to retrieve the contact mechanism types for all locales
   * @return the contact mechanism types
   */
  @WebMethod(operationName = "GetContactMechanismTypes")
  public List<ContactMechanismType> getContactMechanismTypes(
      @WebParam(name = "LocaleId") @XmlElement String localeId) throws ServiceUnavailableException {
    return partyReferenceService.getContactMechanismTypes(localeId);
  }

  /**
   * Retrieve the employment statuses.
   *
   * @param localeId the Unicode locale identifier for the locale to retrieve the employment
   *     statuses for or <b>null</b> to retrieve the employment statuses for all locales
   * @return the employment statuses
   */
  @WebMethod(operationName = "GetEmploymentStatuses")
  public List<EmploymentStatus> getEmploymentStatuses(
      @WebParam(name = "LocaleId") @XmlElement String localeId) throws ServiceUnavailableException {
    return partyReferenceService.getEmploymentStatuses(localeId);
  }

  /**
   * Retrieve the employment types.
   *
   * @param localeId the Unicode locale identifier for the locale to retrieve the employment types
   *     for or <b>null</b> to retrieve the employment types for all locales
   * @return the employment types
   */
  @WebMethod(operationName = "GetEmploymentTypes")
  public List<EmploymentType> getEmploymentTypes(
      @WebParam(name = "LocaleId") @XmlElement String localeId) throws ServiceUnavailableException {
    return partyReferenceService.getEmploymentTypes(localeId);
  }

  /**
   * Retrieve the fields of study.
   *
   * @param localeId the Unicode locale identifier for the locale to retrieve the fields of study
   *     for or <b>null</b> to retrieve the fields of study for all locales
   * @return the fields of study
   */
  @WebMethod(operationName = "GetFieldsOfStudy")
  public List<FieldOfStudy> getFieldsOfStudy(
      @WebParam(name = "LocaleId") @XmlElement String localeId) throws ServiceUnavailableException {
    return partyReferenceService.getFieldsOfStudy(localeId);
  }

  /**
   * Retrieve the genders.
   *
   * @param localeId the Unicode locale identifier for the locale to retrieve the genders for or
   *     <b>null</b> to retrieve the genders for all locales
   * @return the genders
   */
  @WebMethod(operationName = "GetGenders")
  public List<Gender> getGenders(@WebParam(name = "LocaleId") @XmlElement String localeId)
      throws ServiceUnavailableException {
    return partyReferenceService.getGenders(localeId);
  }

  /**
   * Retrieve the identity document types.
   *
   * @param localeId the Unicode locale identifier for the locale to retrieve the identity document
   *     types for or <b>null</b> to retrieve the identity document types for all locales
   * @return the identity document types
   */
  @WebMethod(operationName = "GetIdentityDocumentTypes")
  public List<IdentityDocumentType> getIdentityDocumentTypes(
      @WebParam(name = "LocaleId") @XmlElement String localeId) throws ServiceUnavailableException {
    return partyReferenceService.getIdentityDocumentTypes(localeId);
  }

  /**
   * Retrieve the lock type categories.
   *
   * @param localeId the Unicode locale identifier for the locale to retrieve the lock type
   *     categories for or <b>null</b> to retrieve the lock type categories for all locales
   * @return the lock type categories
   */
  @WebMethod(operationName = "GetLockTypeCategories")
  public List<LockTypeCategory> getLockTypeCategories(
      @WebParam(name = "LocaleId") @XmlElement String localeId) throws ServiceUnavailableException {
    return partyReferenceService.getLockTypeCategories(localeId);
  }

  /**
   * Retrieve the lock types.
   *
   * @param localeId the Unicode locale identifier for the locale to retrieve the lock types for or
   *     <b>null</b> to retrieve the lock types for all locales
   * @return the lock types
   */
  @WebMethod(operationName = "GetLockTypes")
  public List<LockType> getLockTypes(@WebParam(name = "LocaleId") @XmlElement String localeId)
      throws ServiceUnavailableException {
    return partyReferenceService.getLockTypes(localeId);
  }

  /**
   * Retrieve the marital statuses.
   *
   * @param localeId the Unicode locale identifier for the locale to retrieve the marital statuses
   *     for or <b>null</b> to retrieve the marital statuses for all locales
   * @return the marital statuses
   */
  @WebMethod(operationName = "GetMaritalStatuses")
  public List<MaritalStatus> getMaritalStatuses(
      @WebParam(name = "LocaleId") @XmlElement String localeId) throws ServiceUnavailableException {
    return partyReferenceService.getMaritalStatuses(localeId);
  }

  /**
   * Retrieve the marriage types.
   *
   * @param localeId the Unicode locale identifier for the locale to retrieve the marriage types for
   *     or <b>null</b> to retrieve the marriage types for all locales
   * @return the marriage types
   */
  @WebMethod(operationName = "GetMarriageTypes")
  public List<MarriageType> getMarriageTypes(
      @WebParam(name = "LocaleId") @XmlElement String localeId) throws ServiceUnavailableException {
    return partyReferenceService.getMarriageTypes(localeId);
  }

  /**
   * Retrieve the next of kin types.
   *
   * @param localeId the Unicode locale identifier for the locale to retrieve the next of kin types
   *     for or <b>null</b> to retrieve the next of kin types for all locales
   * @return the next of kin types
   */
  @WebMethod(operationName = "GetNextOfKinTypes")
  public List<NextOfKinType> getNextOfKinTypes(
      @WebParam(name = "LocaleId") @XmlElement String localeId) throws ServiceUnavailableException {
    return partyReferenceService.getNextOfKinTypes(localeId);
  }

  /**
   * Retrieve the occupations.
   *
   * @param localeId the Unicode locale identifier for the locale to retrieve the occupations for or
   *     <b>null</b> to retrieve the occupations for all locales
   * @return the occupations
   */
  @WebMethod(operationName = "GetOccupations")
  public List<Occupation> getOccupations(@WebParam(name = "LocaleId") @XmlElement String localeId)
      throws ServiceUnavailableException {
    return partyReferenceService.getOccupations(localeId);
  }

  /**
   * Retrieve the physical address purposes.
   *
   * @param localeId the Unicode locale identifier for the locale to retrieve the physical address
   *     purposes for or <b>null</b> to retrieve the physical address purposes for all locales
   * @return the physical address purposes
   */
  @WebMethod(operationName = "GetPhysicalAddressPurposes")
  public List<PhysicalAddressPurpose> getPhysicalAddressPurposes(
      @WebParam(name = "LocaleId") @XmlElement String localeId) throws ServiceUnavailableException {
    return partyReferenceService.getPhysicalAddressPurposes(localeId);
  }

  /**
   * Retrieve the physical address roles.
   *
   * @param localeId the Unicode locale identifier for the locale to retrieve the physical address
   *     roles for or <b>null</b> to retrieve the physical address roles for all locales
   * @return the physical address roles
   */
  @WebMethod(operationName = "GetPhysicalAddressRoles")
  public List<PhysicalAddressRole> getPhysicalAddressRoles(
      @WebParam(name = "LocaleId") @XmlElement String localeId) throws ServiceUnavailableException {
    return partyReferenceService.getPhysicalAddressRoles(localeId);
  }

  /**
   * Retrieve the physical address types.
   *
   * @param localeId the Unicode locale identifier for the locale to retrieve the physical address
   *     types for or <b>null</b> to retrieve the physical address types for all locales
   * @return the physical address types
   */
  @WebMethod(operationName = "GetPhysicalAddressTypes")
  public List<PhysicalAddressType> getPhysicalAddressTypes(
      @WebParam(name = "LocaleId") @XmlElement String localeId) throws ServiceUnavailableException {
    return partyReferenceService.getPhysicalAddressTypes(localeId);
  }

  /**
   * Retrieve the preference type categories.
   *
   * @param localeId the Unicode locale identifier for the locale to retrieve the preference type
   *     categories for or <b>null</b> to retrieve the preference type categories for all locales
   * @return the preference type categories
   */
  @WebMethod(operationName = "GetPreferenceTypeCategories")
  public List<PreferenceTypeCategory> getPreferenceTypeCategories(
      @WebParam(name = "LocaleId") @XmlElement String localeId) throws ServiceUnavailableException {
    return partyReferenceService.getPreferenceTypeCategories(localeId);
  }

  /**
   * Retrieve the preference types.
   *
   * @param localeId the Unicode locale identifier for the locale to retrieve the preference types
   *     for or <b>null</b> to retrieve the preference types for all locales
   * @return the preference types
   */
  @WebMethod(operationName = "GetPreferenceTypes")
  public List<PreferenceType> getPreferenceTypes(
      @WebParam(name = "LocaleId") @XmlElement String localeId) throws ServiceUnavailableException {
    return partyReferenceService.getPreferenceTypes(localeId);
  }

  /**
   * Retrieve the qualification types.
   *
   * @param localeId the Unicode locale identifier for the locale to retrieve the qualification
   *     types for or <b>null</b> to retrieve the qualification types for all locales
   * @return the qualification types
   */
  @WebMethod(operationName = "GetQualificationTypes")
  public List<QualificationType> getQualificationTypes(
      @WebParam(name = "LocaleId") @XmlElement String localeId) throws ServiceUnavailableException {
    return partyReferenceService.getQualificationTypes(localeId);
  }

  /**
   * Retrieve the races.
   *
   * @param localeId the Unicode locale identifier for the locale to retrieve the races for or
   *     <b>null</b> to retrieve the races for all locales
   * @return the races
   */
  @WebMethod(operationName = "GetRaces")
  public List<Race> getRaces(@WebParam(name = "LocaleId") @XmlElement String localeId)
      throws ServiceUnavailableException {
    return partyReferenceService.getRaces(localeId);
  }

  /**
   * Retrieve the residence permit types.
   *
   * @param localeId the Unicode locale identifier for the locale to retrieve the residence permit
   *     types for or <b>null</b> to retrieve the residence permit types for all locales
   * @return the residence permit types
   */
  @WebMethod(operationName = "GetResidencePermitTypes")
  public List<ResidencePermitType> getResidencePermitTypes(
      @WebParam(name = "LocaleId") @XmlElement String localeId) throws ServiceUnavailableException {
    return partyReferenceService.getResidencePermitTypes(localeId);
  }

  /**
   * Retrieve the residency statuses.
   *
   * @param localeId the Unicode locale identifier for the locale to retrieve the residency statuses
   *     for or <b>null</b> to retrieve the residency statuses for all locales
   * @return the residency statuses
   */
  @WebMethod(operationName = "GetResidencyStatuses")
  public List<ResidencyStatus> getResidencyStatuses(
      @WebParam(name = "LocaleId") @XmlElement String localeId) throws ServiceUnavailableException {
    return partyReferenceService.getResidencyStatuses(localeId);
  }

  /**
   * Retrieve the residential types.
   *
   * @param localeId the Unicode locale identifier for the locale to retrieve the residential types
   *     for or <b>null</b> to retrieve the residential types for all locales
   * @return the residential types
   */
  @WebMethod(operationName = "GetResidentialTypes")
  public List<ResidentialType> getResidentialTypes(
      @WebParam(name = "LocaleId") @XmlElement String localeId) throws ServiceUnavailableException {
    return partyReferenceService.getResidentialTypes(localeId);
  }

  /**
   * Retrieve the role purposes.
   *
   * @param localeId the Unicode locale identifier for the locale to retrieve the role purposes for
   *     or <b>null</b> to retrieve the role purposes for all locales
   * @return the role purposes
   */
  @WebMethod(operationName = "GetRolePurposes")
  public List<RolePurpose> getRolePurposes(@WebParam(name = "LocaleId") @XmlElement String localeId)
      throws ServiceUnavailableException {
    return partyReferenceService.getRolePurposes(localeId);
  }

  /**
   * Retrieve the role type attribute type constraints.
   *
   * @param roleType the code for the role type to retrieve the role type attribute type constraints
   *     for or <b>null</b> to retrieve the role type attribute type constraints for all role types
   * @return the role type attribute type constraints
   */
  @WebMethod(operationName = "GetRoleTypeAttributeTypeConstraints")
  public List<RoleTypeAttributeTypeConstraint> getRoleTypeAttributeTypeConstraints(
      @WebParam(name = "RoleType") @XmlElement String roleType) throws ServiceUnavailableException {
    return partyReferenceService.getRoleTypeAttributeTypeConstraints(roleType);
  }

  /**
   * Retrieve the role type preference type constraints.
   *
   * @param roleType the code for the role type to retrieve the role type preference type
   *     constraints for or <b>null</b> to retrieve the role type preference type constraints for
   *     all role types
   * @return the role type preference type constraints
   */
  @WebMethod(operationName = "GetRoleTypePreferenceTypeConstraints")
  public List<RoleTypePreferenceTypeConstraint> getRoleTypePreferenceTypeConstraints(
      @WebParam(name = "RoleType") @XmlElement String roleType) throws ServiceUnavailableException {
    return partyReferenceService.getRoleTypePreferenceTypeConstraints(roleType);
  }

  /**
   * Retrieve the role types.
   *
   * @param localeId the Unicode locale identifier for the locale to retrieve the role types for or
   *     <b>null</b> to retrieve the role types for all locales
   * @return the role types
   */
  @WebMethod(operationName = "GetRoleTypes")
  public List<RoleType> getRoleTypes(@WebParam(name = "LocaleId") @XmlElement String localeId)
      throws ServiceUnavailableException {
    return partyReferenceService.getRoleTypes(localeId);
  }

  /**
   * Retrieve the source of funds types.
   *
   * @param localeId the Unicode locale identifier for the locale to retrieve the source of funds
   *     types for or <b>null</b> to retrieve the source of funds types for all locales
   * @return the source of funds types
   */
  @WebMethod(operationName = "GetSourceOfFundsTypes")
  public List<SourceOfFundsType> getSourceOfFundsTypes(
      @WebParam(name = "LocaleId") @XmlElement String localeId) throws ServiceUnavailableException {
    return partyReferenceService.getSourceOfFundsTypes(localeId);
  }

  /**
   * Retrieve the source of wealth types.
   *
   * @param localeId the Unicode locale identifier for the locale to retrieve the source of wealth
   *     types for or <b>null</b> to retrieve the source of wealth types for all locales
   * @return the source of wealth types
   */
  @WebMethod(operationName = "GetSourceOfWealthTypes")
  public List<SourceOfWealthType> getSourceOfWealthTypes(
      @WebParam(name = "LocaleId") @XmlElement String localeId) throws ServiceUnavailableException {
    return partyReferenceService.getSourceOfWealthTypes(localeId);
  }

  /**
   * Retrieve the status type categories.
   *
   * @param localeId the Unicode locale identifier for the locale to retrieve the status type
   *     categories for or <b>null</b> to retrieve the status type categories for all locales
   * @return the status type categories
   */
  @WebMethod(operationName = "GetStatusTypeCategories")
  public List<StatusTypeCategory> getStatusTypeCategories(
      @WebParam(name = "LocaleId") @XmlElement String localeId) throws ServiceUnavailableException {
    return partyReferenceService.getStatusTypeCategories(localeId);
  }

  /**
   * Retrieve the status types.
   *
   * @param localeId the Unicode locale identifier for the locale to retrieve the status types for
   *     or <b>null</b> to retrieve the status types for all locales
   * @return the status types
   */
  @WebMethod(operationName = "GetStatusTypes")
  public List<StatusType> getStatusTypes(@WebParam(name = "LocaleId") @XmlElement String localeId)
      throws ServiceUnavailableException {
    return partyReferenceService.getStatusTypes(localeId);
  }

  /**
   * Retrieve the tax number types.
   *
   * @param localeId the Unicode locale identifier for the locale to retrieve the tax number types
   *     for or <b>null</b> to retrieve the tax number types for all locales
   * @return the tax number types
   */
  @WebMethod(operationName = "GetTaxNumberTypes")
  public List<TaxNumberType> getTaxNumberTypes(
      @WebParam(name = "LocaleId") @XmlElement String localeId) throws ServiceUnavailableException {
    return partyReferenceService.getTaxNumberTypes(localeId);
  }

  /**
   * Retrieve the times to contact.
   *
   * @param localeId the Unicode locale identifier for the locale to retrieve the times to contact
   *     for or <b>null</b> to retrieve the times to contact for all locales
   * @return the times to contact
   */
  @WebMethod(operationName = "GetTimesToContact")
  public List<TimeToContact> getTimesToContact(
      @WebParam(name = "LocaleId") @XmlElement String localeId) throws ServiceUnavailableException {
    return partyReferenceService.getTimesToContact(localeId);
  }

  /**
   * Retrieve the titles.
   *
   * @param localeId the Unicode locale identifier for the locale to retrieve the titles for or
   *     <b>null</b> to retrieve the titles for all locales
   * @return the titles
   */
  @WebMethod(operationName = "GetTitles")
  public List<Title> getTitles(@WebParam(name = "LocaleId") @XmlElement String localeId)
      throws ServiceUnavailableException {
    return partyReferenceService.getTitles(localeId);
  }
}
