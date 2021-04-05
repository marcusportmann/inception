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
import org.springframework.util.StringUtils;

/**
 * The <b>ReferenceWebService</b> class.
 *
 * @author Marcus Portmann
 */
@WebService(
    serviceName = "ReferenceService",
    name = "IReferenceService",
    targetNamespace = "http://inception.digital/reference")
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
    return referenceService.getCountries(
        StringUtils.hasText(localeId) ? localeId : ReferenceService.DEFAULT_LOCALE_ID);
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
    return referenceService.getLanguages(
        StringUtils.hasText(localeId) ? localeId : ReferenceService.DEFAULT_LOCALE_ID);
  }

  /**
   * Retrieve the measurement systems.
   *
   * @param localeId the Unicode locale identifier for the locale to retrieve the measurement
   *     systems for or <b>null</b> to retrieve the measurement systems for all locales
   * @return the measurement systems
   */
  @WebMethod(operationName = "GetMeasurementSystems")
  public List<MeasurementSystem> getMeasurementSystems(
      @WebParam(name = "LocaleId") @XmlElement String localeId) throws ServiceUnavailableException {
    return referenceService.getMeasurementSystems(
        StringUtils.hasText(localeId) ? localeId : ReferenceService.DEFAULT_LOCALE_ID);
  }

  /**
   * Retrieve the measurement unit types.
   *
   * @param localeId the Unicode locale identifier for the locale to retrieve the measurement
   *     systems for or <b>null</b> to retrieve the measurement unit types for all locales
   * @return the measurement unit types
   */
  @WebMethod(operationName = "GetMeasurementUnitTypes")
  public List<MeasurementUnitType> getMeasurementUnitTypes(
      @WebParam(name = "LocaleId") @XmlElement String localeId) throws ServiceUnavailableException {
    return referenceService.getMeasurementUnitTypes(
        StringUtils.hasText(localeId) ? localeId : ReferenceService.DEFAULT_LOCALE_ID);
  }

  /**
   * Retrieve the measurement units.
   *
   * @param localeId the Unicode locale identifier for the locale to retrieve the measurement
   *     systems for or <b>null</b> to retrieve the measurement units for all locales
   * @return the measurement units
   */
  @WebMethod(operationName = "GetMeasurementUnits")
  public List<MeasurementUnit> getMeasurementUnits(
      @WebParam(name = "LocaleId") @XmlElement String localeId) throws ServiceUnavailableException {
    return referenceService.getMeasurementUnits(
        StringUtils.hasText(localeId) ? localeId : ReferenceService.DEFAULT_LOCALE_ID);
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
    return referenceService.getRegions(
        StringUtils.hasText(localeId) ? localeId : ReferenceService.DEFAULT_LOCALE_ID);
  }

  /**
   * Retrieve the time zones.
   *
   * @param localeId the Unicode locale identifier for the locale to retrieve the time zones for or
   *     <b>null</b> to retrieve the time zones for all locales
   * @return the time zones
   */
  @WebMethod(operationName = "GetTimeZones")
  public List<TimeZone> getTimeZones(@WebParam(name = "LocaleId") @XmlElement String localeId)
      throws ServiceUnavailableException {
    return referenceService.getTimeZones(
        StringUtils.hasText(localeId) ? localeId : ReferenceService.DEFAULT_LOCALE_ID);
  }
}
