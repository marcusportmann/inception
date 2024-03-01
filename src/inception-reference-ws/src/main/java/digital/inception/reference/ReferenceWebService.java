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

package digital.inception.reference;

import digital.inception.core.service.InvalidArgumentException;
import digital.inception.core.service.ServiceUnavailableException;
import jakarta.jws.WebMethod;
import jakarta.jws.WebParam;
import jakarta.jws.WebResult;
import jakarta.jws.WebService;
import jakarta.jws.soap.SOAPBinding;
import jakarta.xml.bind.annotation.XmlElement;
import java.util.List;

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
   * Retrieve the country reference data for a specific locale.
   *
   * @param localeId the Unicode locale identifier for the locale to retrieve the country reference
   *     data for
   * @return the country reference data
   * @throws InvalidArgumentException if an argument is invalid
   * @throws ServiceUnavailableException if the country reference data could not be retrieved
   */
  @WebMethod(operationName = "GetCountries")
  @WebResult(name = "Country")
  public List<Country> getCountries(
      @WebParam(name = "LocaleId") @XmlElement(required = true) String localeId)
      throws InvalidArgumentException, ServiceUnavailableException {
    return referenceService.getCountries(localeId);
  }

  /**
   * Retrieve the language reference data for a specific locale.
   *
   * @param localeId the Unicode locale identifier for the locale to retrieve the language reference
   *     data for
   * @return the language reference data
   * @throws InvalidArgumentException if an argument is invalid
   * @throws ServiceUnavailableException if the language reference data could not be retrieved
   */
  @WebMethod(operationName = "GetLanguages")
  @WebResult(name = "Language")
  public List<Language> getLanguages(
      @WebParam(name = "LocaleId") @XmlElement(required = true) String localeId)
      throws InvalidArgumentException, ServiceUnavailableException {
    return referenceService.getLanguages(localeId);
  }

  /**
   * Retrieve the measurement system reference data for a specific locale.
   *
   * @param localeId the Unicode locale identifier for the locale to retrieve the measurement system
   *     reference data for
   * @return the measurement system reference data
   * @throws InvalidArgumentException if an argument is invalid
   * @throws ServiceUnavailableException if the measurement system reference data could not be
   *     retrieved
   */
  @WebMethod(operationName = "GetMeasurementSystems")
  @WebResult(name = "MeasurementSystem")
  public List<MeasurementSystem> getMeasurementSystems(
      @WebParam(name = "LocaleId") @XmlElement(required = true) String localeId)
      throws InvalidArgumentException, ServiceUnavailableException {
    return referenceService.getMeasurementSystems(localeId);
  }

  /**
   * Retrieve the measurement unit type reference data for a specific locale.
   *
   * @param localeId the Unicode locale identifier for the locale to retrieve the measurement unit
   *     type reference data for
   * @return the measurement unit type reference data
   * @throws InvalidArgumentException if an argument is invalid
   * @throws ServiceUnavailableException if the measurement unit type reference data could not be
   *     retrieved
   */
  @WebMethod(operationName = "GetMeasurementUnitTypes")
  @WebResult(name = "MeasurementUnitType")
  public List<MeasurementUnitType> getMeasurementUnitTypes(
      @WebParam(name = "LocaleId") @XmlElement(required = true) String localeId)
      throws InvalidArgumentException, ServiceUnavailableException {
    return referenceService.getMeasurementUnitTypes(localeId);
  }

  /**
   * Retrieve the measurement unit reference data for a specific locale.
   *
   * @param localeId the Unicode locale identifier for the locale to retrieve the measurement unit
   *     reference data for
   * @return the measurement unit reference data
   * @throws InvalidArgumentException if an argument is invalid
   * @throws ServiceUnavailableException if the measurement unit reference data could not be
   *     retrieved
   */
  @WebMethod(operationName = "GetMeasurementUnits")
  @WebResult(name = "MeasurementUnit")
  public List<MeasurementUnit> getMeasurementUnits(
      @WebParam(name = "LocaleId") @XmlElement(required = true) String localeId)
      throws InvalidArgumentException, ServiceUnavailableException {
    return referenceService.getMeasurementUnits(localeId);
  }

  /**
   * Retrieve the region reference data for a specific locale.
   *
   * @param localeId the Unicode locale identifier for the locale to retrieve the region reference
   *     data for
   * @return the region reference data
   * @throws InvalidArgumentException if an argument is invalid
   * @throws ServiceUnavailableException if the region reference data could not be retrieved
   */
  @WebMethod(operationName = "GetRegions")
  @WebResult(name = "Region")
  public List<Region> getRegions(
      @WebParam(name = "LocaleId") @XmlElement(required = true) String localeId)
      throws InvalidArgumentException, ServiceUnavailableException {
    return referenceService.getRegions(localeId);
  }

  /**
   * Retrieve the time zone reference data for a specific locale.
   *
   * @param localeId the Unicode locale identifier for the locale to retrieve the time zone
   *     reference data for
   * @return the time zone reference data
   * @throws InvalidArgumentException if an argument is invalid
   * @throws ServiceUnavailableException if the time zones reference data could not be retrieved
   */
  @WebMethod(operationName = "GetTimeZones")
  @WebResult(name = "TimeZone")
  public List<TimeZone> getTimeZones(
      @WebParam(name = "LocaleId") @XmlElement(required = true) String localeId)
      throws InvalidArgumentException, ServiceUnavailableException {
    return referenceService.getTimeZones(localeId);
  }
}
