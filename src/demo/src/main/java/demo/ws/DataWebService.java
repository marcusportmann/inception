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

package demo.ws;

import demo.model.Data;
import demo.model.IDataService;
import digital.inception.core.service.ServiceUnavailableException;
import digital.inception.core.service.InvalidArgumentException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.List;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.xml.bind.annotation.XmlElement;

/**
 * The <b>DataWebService</b> class.
 *
 * @author Marcus Portmann
 */
@WebService(serviceName = "DataService", name = "IDataService", targetNamespace = "http://demo")
@SOAPBinding
@SuppressWarnings({"unused", "ValidExternallyBoundObject"})
public class DataWebService {

  private final IDataService dataService;

  /**
   * Constructs a new <b>DataWebService</b>.
   *
   * @param dataService the Data Service
   */
  public DataWebService(IDataService dataService) {
    this.dataService = dataService;
  }

  /**
   * Returns all the data.
   *
   * @return all the data
   */
  @WebMethod(operationName = "GetAllData")
  @WebResult(name = "Data")
  public List<Data> getAllData() throws ServiceUnavailableException {
    return dataService.getAllData();
  }

  /**
   * Retrieve the data.
   *
   * @return the data
   */
  @WebMethod(operationName = "GetData")
  @WebResult(name = "Data")
  public Data getData() throws ServiceUnavailableException {
    long id = System.currentTimeMillis();

    Data data = new Data(id, 777, "Test Value " + id, LocalDate.now(), LocalDateTime.now());

    dataService.createData(data);

    data = dataService.getData(data.getId());

    return data;
  }

  /** Test the exception handling. */
  @WebMethod(operationName = "TestExceptionHandling")
  public void testExceptionHandling() throws ServiceUnavailableException {
    throw new ServiceUnavailableException("Testing 1.. 2.. 3..");
  }

  /**
   * Test the local date time mapping.
   *
   * @param localDateTime the local date time
   */
  @WebMethod(operationName = "TestLocalDateTime")
  public LocalDateTime testLocalDateTime(
      @WebParam(name = "LocalDateTime") @XmlElement(required = true) LocalDateTime localDateTime)
      throws ServiceUnavailableException {
    if (true) {
      throw new ServiceUnavailableException("Testing 1.. 2.. 3...");
    }

    System.out.println("localDateTime = " + localDateTime);

    return localDateTime;
  }

  /**
   * Test the zoned date time mapping.
   *
   * @param zonedDateTime the zoned date time
   */
  @WebMethod(operationName = "TestZonedDateTime")
  public ZonedDateTime testZonedDateTime(
      @WebParam(name = "ZonedDateTime") @XmlElement(required = true) ZonedDateTime zonedDateTime)
      throws ServiceUnavailableException {
    if (false) {
      throw new ServiceUnavailableException("Testing 1.. 2.. 3...");
    }

    System.out.println("zonedDateTime = " + zonedDateTime);

    return zonedDateTime;
  }

  /** Validate the data. */
  @WebMethod(operationName = "ValidateData")
  public void validateData(
      @WebParam(name = "Data") @XmlElement(required = true) Data data)
      throws InvalidArgumentException, ServiceUnavailableException {
    dataService.validateData(data);
  }
}
