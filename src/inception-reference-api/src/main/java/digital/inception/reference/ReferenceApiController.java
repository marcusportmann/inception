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

import digital.inception.api.SecureApiController;
import digital.inception.core.service.InvalidArgumentException;
import digital.inception.core.service.ServiceUnavailableException;
import java.util.List;
import org.springframework.context.ApplicationContext;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;

/**
 * The <b>ReferenceApiController</b> class.
 *
 * @author Marcus Portmann
 */
@RestController
@CrossOrigin
@SuppressWarnings({"unused", "WeakerAccess"})
public class ReferenceApiController extends SecureApiController implements IReferenceApiController {

  /** The Reference Service. */
  private final IReferenceService referenceService;

  /**
   * Constructs a new <b>ReferenceApiController</b>.
   *
   * @param applicationContext the Spring application context
   * @param referenceService the Reference Service
   */
  public ReferenceApiController(
      ApplicationContext applicationContext, IReferenceService referenceService) {
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
    return referenceService.getRegions(localeId, country);
  }

  @Override
  public List<TimeZone> getTimeZones(String localeId)
      throws InvalidArgumentException, ServiceUnavailableException {
    return referenceService.getTimeZones(localeId);
  }
}
