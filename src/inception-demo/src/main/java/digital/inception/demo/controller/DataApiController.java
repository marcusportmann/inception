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

package digital.inception.demo.controller;

import digital.inception.api.SecureApiController;
import digital.inception.core.service.InvalidArgumentException;
import digital.inception.core.service.ServiceUnavailableException;
import digital.inception.core.util.ISO8601Util;
import digital.inception.demo.model.Data;
import digital.inception.demo.model.ReactiveData;
import digital.inception.demo.service.IDataService;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.OffsetTime;
import java.util.List;
import org.springframework.context.ApplicationContext;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

/**
 * The <b>DataApiController</b> class.
 *
 * @author Marcus Portmann
 */
@RestController
@CrossOrigin
@SuppressWarnings({"unused"})
public class DataApiController extends SecureApiController implements IDataApiController {

  private final IDataService dataService;

  /**
   * Constructs a new <b>DataApiController</b>.
   *
   * @param applicationContext the Spring application context
   * @param dataService the Data Service
   */
  public DataApiController(ApplicationContext applicationContext, IDataService dataService) {
    super(applicationContext);

    this.dataService = dataService;
  }

  @Override
  public List<Data> getAllData() throws ServiceUnavailableException {
    return dataService.getAllData();
  }

  @Override
  public Flux<ReactiveData> getAllReactiveData() throws ServiceUnavailableException {
    return dataService.getAllReactiveData();
  }

  @Override
  public Data getData() throws ServiceUnavailableException {
    long id = System.currentTimeMillis();

    Data data = new Data();
    data.setId(id);
    data.setBooleanValue(true);
    data.setDateValue(ISO8601Util.toLocalDate("1976-03-07"));
    data.setDecimalValue(new BigDecimal("111.111"));
    data.setDoubleValue(222.222);
    data.setFloatValue(333.333f);
    data.setIntegerValue(444);
    data.setStringValue("This is a valid string value");
    data.setTimeValue(ISO8601Util.toLocalTime("14:30:00"));
    data.setTimeWithTimeZoneValue(ISO8601Util.toOffsetTime("18:30:00+02:00"));
    data.setTimestampValue(ISO8601Util.toLocalDateTime("2016-07-17T23:56:19.123"));
    data.setTimestampWithTimeZoneValue(
        ISO8601Util.toOffsetDateTime("2019-02-28T00:14:27.505+02:00"));
    data.setCountry("ZA");
    data.setLanguage("EN");

    dataService.createData(data);

    data = dataService.getData(data.getId());

    return data;
  }

  @Override
  public void process(Data data) throws InvalidArgumentException, ServiceUnavailableException {
    dataService.validateData(data);
  }

  @Override
  public void validate(Data data) throws InvalidArgumentException, ServiceUnavailableException {
    dataService.validateData(data);
  }
}
