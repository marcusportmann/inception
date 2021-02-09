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

  /** The Party Service. */
  private final IPartyService partyService;

  /**
   * Constructs a new <b>PartyReferenceWebService</b>.
   *
   * @param partyService the Party Service
   */
  public PartyReferenceWebService(IPartyService partyService) {
    this.partyService = partyService;
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
    return partyService.getContactMechanismPurposes(localeId);
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
    return partyService.getContactMechanismTypes(localeId);
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
    return partyService.getEmploymentStatuses(localeId);
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
    return partyService.getEmploymentTypes(localeId);
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
    return partyService.getGenders(localeId);
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
    return partyService.getIdentityDocumentTypes(localeId);
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
    return partyService.getMaritalStatuses(localeId);
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
    return partyService.getMarriageTypes(localeId);
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
    return partyService.getNextOfKinTypes(localeId);
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
    return partyService.getOccupations(localeId);
  }

  /**
   * Retrieve the party attribute type categories.
   *
   * @param localeId the Unicode locale identifier for the locale to retrieve the party attribute
   *     type categories for or <b>null</b> to retrieve the party attribute type categories for all
   *     locales
   * @return the party attribute type categories
   */
  @WebMethod(operationName = "GetPartyAttributeTypeCategories")
  public List<PartyAttributeTypeCategory> getPartyAttributeTypeCategories(
      @WebParam(name = "LocaleId") @XmlElement String localeId) throws ServiceUnavailableException {
    return partyService.getPartyAttributeTypeCategories(localeId);
  }

  /**
   * Retrieve the party attribute types.
   *
   * @param localeId the Unicode locale identifier for the locale to retrieve the party attribute
   *     types for or <b>null</b> to retrieve the party attribute types for all locales
   * @return the party attribute types
   */
  @WebMethod(operationName = "GetPartyAttributeTypes")
  public List<PartyAttributeType> getPartyAttributeTypes(
      @WebParam(name = "LocaleId") @XmlElement String localeId) throws ServiceUnavailableException {
    return partyService.getPartyAttributeTypes(localeId);
  }

  /**
   * Retrieve the party role purposes.
   *
   * @param localeId the Unicode locale identifier for the locale to retrieve the party role
   *     purposes for or <b>null</b> to retrieve the party role purposes for all locales
   * @return the party role purposes
   */
  @WebMethod(operationName = "GetPartyRolePurposes")
  public List<PartyRolePurpose> getPartyRolePurposes(
      @WebParam(name = "LocaleId") @XmlElement String localeId) throws ServiceUnavailableException {
    return partyService.getPartyRolePurposes(localeId);
  }

  /**
   * Retrieve the party role types.
   *
   * @param localeId the Unicode locale identifier for the locale to retrieve the party role types
   *     for or <b>null</b> to retrieve the party role types for all locales
   * @return the party role types
   */
  @WebMethod(operationName = "GetPartyRoleTypes")
  public List<PartyRoleType> getPartyRoleTypes(
      @WebParam(name = "LocaleId") @XmlElement String localeId) throws ServiceUnavailableException {
    return partyService.getPartyRoleTypes(localeId);
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
    return partyService.getPhysicalAddressPurposes(localeId);
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
    return partyService.getPhysicalAddressTypes(localeId);
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
    return partyService.getPreferenceTypeCategories(localeId);
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
    return partyService.getPreferenceTypes(localeId);
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
    return partyService.getRaces(localeId);
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
    return partyService.getResidencePermitTypes(localeId);
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
    return partyService.getResidencyStatuses(localeId);
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
    return partyService.getResidentialTypes(localeId);
  }

  /**
   * Retrieve the sources of funds.
   *
   * @param localeId the Unicode locale identifier for the locale to retrieve the sources of funds
   *     for or <b>null</b> to retrieve the sources of funds for all locales
   * @return the sources of funds
   */
  @WebMethod(operationName = "GetSourcesOfFunds")
  public List<SourceOfFunds> getSourcesOfFunds(
      @WebParam(name = "LocaleId") @XmlElement String localeId) throws ServiceUnavailableException {
    return partyService.getSourcesOfFunds(localeId);
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
    return partyService.getTaxNumberTypes(localeId);
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
    return partyService.getTimesToContact(localeId);
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
    return partyService.getTitles(localeId);
  }
}