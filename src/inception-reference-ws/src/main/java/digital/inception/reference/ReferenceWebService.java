/*
 * Copyright 2020 Marcus Portmann
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

package digital.inception.reference;

// ~--- JDK imports ------------------------------------------------------------

import java.util.List;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.validation.Validator;
import javax.xml.bind.annotation.XmlElement;

/**
 * The <code>ReferenceWebService</code> class.
 *
 * @author Marcus Portmann
 */
@WebService(
    serviceName = "ReferenceService",
    name = "IReferenceService",
    targetNamespace = "http://reference.inception.digital")
@SOAPBinding
@SuppressWarnings({"unused", "ValidExternallyBoundObject"})
public class ReferenceWebService {

  /** The Reference Service. */
  private final IReferenceService referenceService;

  /** The JSR-303 validator. */
  private final Validator validator;

  /**
   * Constructs a new <code>ReferenceWebService</code>.
   *
   * @param referenceService the Reference Service
   * @param validator the JSR-303 validator
   */
  public ReferenceWebService(IReferenceService referenceService, Validator validator) {
    this.referenceService = referenceService;
    this.validator = validator;
  }

  /**
   * Retrieve the countries.
   *
   * @param localeId the Unicode locale identifier identifying the locale to retrieve the countries
   *     for or <code>null</code> to retrieve the countries for all locales
   * @return the countries
   */
  @WebMethod(operationName = "GetCountries")
  public List<Country> getCountries(
      @WebParam(name = "LocaleId") @XmlElement(required = true) String localeId)
      throws ReferenceServiceException {
    return referenceService.getCountries(localeId);
  }

  /**
   * Retrieve the employment statuses.
   *
   * @param localeId the Unicode locale identifier identifying the locale to retrieve the employment
   *     statuses for or <code>null</code> to retrieve the employment statuses for all locales
   * @return the employment statuses
   */
  @WebMethod(operationName = "GetEmploymentStatuses")
  public List<EmploymentStatus> getEmploymentStatuses(
      @WebParam(name = "LocaleId") @XmlElement(required = true) String localeId)
      throws ReferenceServiceException {
    return referenceService.getEmploymentStatuses(localeId);
  }

  /**
   * Retrieve the employment types.
   *
   * @param localeId the Unicode locale identifier identifying the locale to retrieve the employment
   *     types for or <code>null</code> to retrieve the employment types for all locales
   * @return the employment types
   */
  @WebMethod(operationName = "GetEmploymentTypes")
  public List<EmploymentType> getEmploymentTypes(
      @WebParam(name = "LocaleId") @XmlElement(required = true) String localeId)
      throws ReferenceServiceException {
    return referenceService.getEmploymentTypes(localeId);
  }

  /**
   * Retrieve the genders.
   *
   * @param localeId the Unicode locale identifier identifying the locale to retrieve the genders
   *     for or <code>null</code> to retrieve the genders for all locales
   * @return the genders
   */
  @WebMethod(operationName = "GetGenders")
  public List<Gender> getGenders(
      @WebParam(name = "LocaleId") @XmlElement(required = true) String localeId)
      throws ReferenceServiceException {
    return referenceService.getGenders(localeId);
  }

  /**
   * Retrieve the identity document types.
   *
   * @param localeId the Unicode locale identifier identifying the locale to retrieve the identity
   *     document types for or <code>null</code> to retrieve the identity document types for all
   *     locales
   * @return the identity document types
   */
  @WebMethod(operationName = "GetIdentityDocumentTypes")
  public List<IdentityDocumentType> getIdentityDocumentTypes(
      @WebParam(name = "LocaleId") @XmlElement(required = true) String localeId)
      throws ReferenceServiceException {
    return referenceService.getIdentityDocumentTypes(localeId);
  }

  /**
   * Retrieve the languages.
   *
   * @param localeId the Unicode locale identifier identifying the locale to retrieve the languages
   *     for or <code>null</code> to retrieve the languages for all locales
   * @return the languages
   */
  @WebMethod(operationName = "GetLanguages")
  public List<Language> getLanguages(
      @WebParam(name = "LocaleId") @XmlElement(required = true) String localeId)
      throws ReferenceServiceException {
    return referenceService.getLanguages(localeId);
  }

  /**
   * Retrieve the marital statuses.
   *
   * @param localeId the Unicode locale identifier identifying the locale to retrieve the marital
   *     statuses for or <code>null</code> to retrieve the marital statuses for all locales
   * @return the marital statuses
   */
  @WebMethod(operationName = "GetMaritalStatuses")
  public List<MaritalStatus> getMaritalStatuses(
      @WebParam(name = "LocaleId") @XmlElement(required = true) String localeId)
      throws ReferenceServiceException {
    return referenceService.getMaritalStatuses(localeId);
  }

  /**
   * Retrieve the marriage types.
   *
   * @param localeId the Unicode locale identifier identifying the locale to retrieve the marriage
   *     types for or <code>null</code> to retrieve the marriage types for all locales
   * @return the marriage types
   */
  @WebMethod(operationName = "GetMarriageTypes")
  public List<MarriageType> getMarriageTypes(
      @WebParam(name = "LocaleId") @XmlElement(required = true) String localeId)
      throws ReferenceServiceException {
    return referenceService.getMarriageTypes(localeId);
  }

  /**
   * Retrieve the next of kin types.
   *
   * @param localeId the Unicode locale identifier identifying the locale to retrieve the next of
   *     kin types for or <code>null</code> to retrieve the next of kin types for all locales
   * @return the next of kin types
   */
  @WebMethod(operationName = "GetNextOfKinTypes")
  public List<NextOfKinType> getNextOfKinTypes(
      @WebParam(name = "LocaleId") @XmlElement(required = true) String localeId)
      throws ReferenceServiceException {
    return referenceService.getNextOfKinTypes(localeId);
  }

  /**
   * Retrieve the occupations.
   *
   * @param localeId the Unicode locale identifier identifying the locale to retrieve the
   *     occupations for or <code>null</code> to retrieve the occupations for all locales
   * @return the occupations
   */
  @WebMethod(operationName = "GetOccupations")
  public List<Occupation> getOccupations(
      @WebParam(name = "LocaleId") @XmlElement(required = true) String localeId)
      throws ReferenceServiceException {
    return referenceService.getOccupations(localeId);
  }

  /**
   * Retrieve the races.
   *
   * @param localeId the Unicode locale identifier identifying the locale to retrieve the races for
   *     or <code>null</code> to retrieve the races for all locales
   * @return the races
   */
  @WebMethod(operationName = "GetRaces")
  public List<Race> getRaces(
      @WebParam(name = "LocaleId") @XmlElement(required = true) String localeId)
      throws ReferenceServiceException {
    return referenceService.getRaces(localeId);
  }

  /**
   * Retrieve the regions.
   *
   * @param localeId the Unicode locale identifier identifying the locale to retrieve the regions
   *     for or <code>null</code> to retrieve the regions for all locales
   * @return the regions
   */
  @WebMethod(operationName = "GetRegions")
  public List<Region> getRegions(
      @WebParam(name = "LocaleId") @XmlElement(required = true) String localeId)
      throws ReferenceServiceException {
    return referenceService.getRegions(localeId);
  }

  /**
   * Retrieve the residence permit types.
   *
   * @param localeId the Unicode locale identifier identifying the locale to retrieve the residence
   *     permit types for or <code>null</code> to retrieve the residence permit types for all
   *     locales
   * @return the residence permit types
   */
  @WebMethod(operationName = "GetResidencePermitTypes")
  public List<ResidencePermitType> getResidencePermitTypes(
      @WebParam(name = "LocaleId") @XmlElement(required = true) String localeId)
      throws ReferenceServiceException {
    return referenceService.getResidencePermitTypes(localeId);
  }

  /**
   * Retrieve the residency statuses.
   *
   * @param localeId the Unicode locale identifier identifying the locale to retrieve the residency
   *     statuses for or <code>null</code> to retrieve the residency statuses for all locales
   * @return the residency statuses
   */
  @WebMethod(operationName = "GetResidencyStatuses")
  public List<ResidencyStatus> getResidencyStatuses(
      @WebParam(name = "LocaleId") @XmlElement(required = true) String localeId)
      throws ReferenceServiceException {
    return referenceService.getResidencyStatuses(localeId);
  }

  /**
   * Retrieve the residential types.
   *
   * @param localeId the Unicode locale identifier identifying the locale to retrieve the
   *     residential types for or <code>null</code> to retrieve the residential types for all
   *     locales
   * @return the residential types
   */
  @WebMethod(operationName = "GetResidentialTypes")
  public List<ResidentialType> getResidentialTypes(
      @WebParam(name = "LocaleId") @XmlElement(required = true) String localeId)
      throws ReferenceServiceException {
    return referenceService.getResidentialTypes(localeId);
  }

  /**
   * Retrieve the sources of funds.
   *
   * @param localeId the Unicode locale identifier identifying the locale to retrieve the sources of
   *     funds for or <code>null</code> to retrieve the sources of funds for all locales
   * @return the sources of funds
   */
  @WebMethod(operationName = "GetSourcesOfFunds")
  public List<SourceOfFunds> getSourcesOfFunds(
      @WebParam(name = "LocaleId") @XmlElement(required = true) String localeId)
      throws ReferenceServiceException {
    return referenceService.getSourcesOfFunds(localeId);
  }

  /**
   * Retrieve the tax number types.
   *
   * @param localeId the Unicode locale identifier identifying the locale to retrieve the tax number
   *     types for or <code>null</code> to retrieve the tax number types for all locales
   * @return the tax number types
   */
  @WebMethod(operationName = "GetTaxNumberTypes")
  public List<TaxNumberType> getTaxNumberTypes(
      @WebParam(name = "LocaleId") @XmlElement(required = true) String localeId)
      throws ReferenceServiceException {
    return referenceService.getTaxNumberTypes(localeId);
  }

  /**
   * Retrieve the titles.
   *
   * @param localeId the Unicode locale identifier identifying the locale to retrieve the titles for
   *     or <code>null</code> to retrieve the titles for all locales
   * @return the titles
   */
  @WebMethod(operationName = "GetTitles")
  public List<Title> getTitles(
      @WebParam(name = "LocaleId") @XmlElement(required = true) String localeId)
      throws ReferenceServiceException {
    return referenceService.getTitles(localeId);
  }

  /**
   * Retrieve the verification methods.
   *
   * @param localeId the Unicode locale identifier identifying the locale to retrieve the
   *     verification methods for or <code>null</code> to retrieve the verification methods for all
   *     locales
   * @return the verification methods
   */
  @WebMethod(operationName = "GetVerificationMethods")
  public List<VerificationMethod> getVerificationMethods(
      @WebParam(name = "LocaleId") @XmlElement(required = true) String localeId)
      throws ReferenceServiceException {
    return referenceService.getVerificationMethods(localeId);
  }

  /**
   * Retrieve the verification statuses.
   *
   * @param localeId the Unicode locale identifier identifying the locale to retrieve the
   *     verification statuses for or <code>null</code> to retrieve the verification statuses for
   *     all locales
   * @return the verification statuses
   */
  @WebMethod(operationName = "GetVerificationStatuses")
  public List<VerificationStatus> getVerificationStatuses(
      @WebParam(name = "LocaleId") @XmlElement(required = true) String localeId)
      throws ReferenceServiceException {
    return referenceService.getVerificationStatuses(localeId);
  }
}
