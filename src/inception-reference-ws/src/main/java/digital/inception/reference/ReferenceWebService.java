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

package digital.inception.reference;

import digital.inception.core.service.ServiceUnavailableException;
import java.util.List;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.xml.bind.annotation.XmlElement;

/**
 * The <b>ReferenceWebService</b> class.
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

  /**
   * Constructs a new <b>ReferenceWebService</b>.
   *
   * @param referenceService the Reference Service
   */
  public ReferenceWebService(IReferenceService referenceService) {
    this.referenceService = referenceService;
  }

  /**
   * Retrieve the countries.
   *
   * @param localeId the Unicode locale identifier for the locale to retrieve the countries for or
   *     <b>null</b> to retrieve the countries for all locales
   * @return the countries
   */
  @WebMethod(operationName = "GetCountries")
  public List<Country> getCountries(@WebParam(name = "LocaleId") @XmlElement String localeId)
      throws ServiceUnavailableException {
    return referenceService.getCountries(localeId);
  }

  /**
   * Retrieve the languages.
   *
   * @param localeId the Unicode locale identifier for the locale to retrieve the languages for or
   *     <b>null</b> to retrieve the languages for all locales
   * @return the languages
   */
  @WebMethod(operationName = "GetLanguages")
  public List<Language> getLanguages(@WebParam(name = "LocaleId") @XmlElement String localeId)
      throws ServiceUnavailableException {
    return referenceService.getLanguages(localeId);
  }

  /**
   * Retrieve the regions.
   *
   * @param localeId the Unicode locale identifier for the locale to retrieve the regions for or
   *     <b>null</b> to retrieve the regions for all locales
   * @return the regions
   */
  @WebMethod(operationName = "GetRegions")
  public List<Region> getRegions(@WebParam(name = "LocaleId") @XmlElement String localeId)
      throws ServiceUnavailableException {
    return referenceService.getRegions(localeId);
  }

  /**
   * Retrieve the verification methods.
   *
   * @param localeId the Unicode locale identifier for the locale to retrieve the verification
   *     methods for or <b>null</b> to retrieve the verification methods for all locales
   * @return the verification methods
   */
  @WebMethod(operationName = "GetVerificationMethods")
  public List<VerificationMethod> getVerificationMethods(
      @WebParam(name = "LocaleId") @XmlElement String localeId) throws ServiceUnavailableException {
    return referenceService.getVerificationMethods(localeId);
  }

  /**
   * Retrieve the verification statuses.
   *
   * @param localeId the Unicode locale identifier for the locale to retrieve the verification
   *     statuses for or <b>null</b> to retrieve the verification statuses for all locales
   * @return the verification statuses
   */
  @WebMethod(operationName = "GetVerificationStatuses")
  public List<VerificationStatus> getVerificationStatuses(
      @WebParam(name = "LocaleId") @XmlElement String localeId) throws ServiceUnavailableException {
    return referenceService.getVerificationStatuses(localeId);
  }
}
