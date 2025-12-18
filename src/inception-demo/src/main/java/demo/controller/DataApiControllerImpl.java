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

package demo.controller;

import demo.model.Data;
import demo.model.ReactiveData;
import demo.service.DataService;
import digital.inception.api.SecureApiController;
import digital.inception.core.exception.InvalidArgumentException;
import digital.inception.core.exception.ServiceUnavailableException;
import digital.inception.core.util.ISO8601Util;
import java.math.BigDecimal;
import java.util.List;
import org.springframework.context.ApplicationContext;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

/**
 * The {@code DataApiControllerImpl} class.
 *
 * @author Marcus Portmann
 */
@RestController
@CrossOrigin
@SuppressWarnings({"unused"})
public class DataApiControllerImpl extends SecureApiController implements DataApiController {

  private final DataService dataService;

  /**
   * Constructs a new {@code DataApiControllerImpl}.
   *
   * @param applicationContext the Spring {@link ApplicationContext}
   * @param dataService the Data Service
   */
  public DataApiControllerImpl(ApplicationContext applicationContext, DataService dataService) {
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
