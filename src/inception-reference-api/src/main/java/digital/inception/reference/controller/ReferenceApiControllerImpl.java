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

package digital.inception.reference.controller;

import digital.inception.api.SecureApiController;
import digital.inception.core.exception.InvalidArgumentException;
import digital.inception.core.exception.ServiceUnavailableException;
import digital.inception.reference.model.Country;
import digital.inception.reference.model.Language;
import digital.inception.reference.model.MeasurementSystem;
import digital.inception.reference.model.MeasurementUnit;
import digital.inception.reference.model.MeasurementUnitType;
import digital.inception.reference.model.Region;
import digital.inception.reference.model.TimeZone;
import digital.inception.reference.service.ReferenceService;
import java.util.List;
import org.springframework.context.ApplicationContext;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;

/**
 * The {@code ReferenceApiControllerImpl} class.
 *
 * @author Marcus Portmann
 */
@RestController
@CrossOrigin
@SuppressWarnings({"unused", "WeakerAccess"})
public class ReferenceApiControllerImpl extends SecureApiController
    implements ReferenceApiController {

  /** The Reference Service. */
  private final ReferenceService referenceService;

  /**
   * Constructs a new {@code ReferenceApiControllerImpl}.
   *
   * @param applicationContext the Spring {@link ApplicationContext}
   * @param referenceService the Reference Service
   */
  public ReferenceApiControllerImpl(
      ApplicationContext applicationContext, ReferenceService referenceService) {
    super(applicationContext);

    this.referenceService = referenceService;
  }

  @Override
  public List<Country> getCountries(String localeId)
      throws InvalidArgumentException, ServiceUnavailableException {
    return referenceService.getCountries(localeId);
  }

  @Override
  public List<Language> getLanguages(String localeId)
      throws InvalidArgumentException, ServiceUnavailableException {
    return referenceService.getLanguages(localeId);
  }

  @Override
  public List<MeasurementSystem> getMeasurementSystems(String localeId)
      throws InvalidArgumentException, ServiceUnavailableException {
    return referenceService.getMeasurementSystems(localeId);
  }

  @Override
  public List<MeasurementUnitType> getMeasurementUnitTypes(String localeId)
      throws InvalidArgumentException, ServiceUnavailableException {
    return referenceService.getMeasurementUnitTypes(localeId);
  }

  @Override
  public List<MeasurementUnit> getMeasurementUnits(String localeId)
      throws InvalidArgumentException, ServiceUnavailableException {
    return referenceService.getMeasurementUnits(localeId);
  }

  @Override
  public List<Region> getRegions(String localeId, String country)
      throws InvalidArgumentException, ServiceUnavailableException {
    if (StringUtils.hasText(country)) {
      return referenceService.getRegions(localeId, country);
    } else {
      return referenceService.getRegions();
    }
  }

  @Override
  public List<TimeZone> getTimeZones(String localeId)
      throws InvalidArgumentException, ServiceUnavailableException {
    return referenceService.getTimeZones(localeId);
  }
}
