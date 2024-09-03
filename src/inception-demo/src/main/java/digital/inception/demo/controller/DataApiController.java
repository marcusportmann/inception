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
import digital.inception.demo.model.Data;
import digital.inception.demo.service.IDataService;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.List;
import org.springframework.context.ApplicationContext;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;

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
  public Data getData() throws ServiceUnavailableException {
    long id = System.currentTimeMillis();

    Data data =
        new Data(id, 777, "Test Value " + id, LocalDate.now(), OffsetDateTime.now(), "ZA", "EN");

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
