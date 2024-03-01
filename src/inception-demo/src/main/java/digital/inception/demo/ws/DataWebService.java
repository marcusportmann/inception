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

package digital.inception.demo.ws;

import digital.inception.core.service.InvalidArgumentException;
import digital.inception.core.service.ServiceUnavailableException;
import digital.inception.demo.model.Data;
import digital.inception.demo.service.IDataService;
import jakarta.jws.WebMethod;
import jakarta.jws.WebParam;
import jakarta.jws.WebResult;
import jakarta.jws.WebService;
import jakarta.jws.soap.SOAPBinding;
import jakarta.xml.bind.annotation.XmlElement;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZonedDateTime;
import java.util.List;

/**
 * The <b>DataWebService</b> class.
 *
 * @author Marcus Portmann
 */
@WebService(
    serviceName = "DataService",
    name = "IDataService",
    targetNamespace = "http://inception.digital/demo")
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
   * @throws ServiceUnavailableException if the data could not be retrieved
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
   * @throws ServiceUnavailableException if the data could not be retrieved
   */
  @WebMethod(operationName = "GetData")
  @WebResult(name = "Data")
  public Data getData() throws ServiceUnavailableException {
    long id = System.currentTimeMillis();

    Data data = new Data(id, 777, "Test Value " + id, LocalDate.now(), OffsetDateTime.now());

    dataService.createData(data);

    data = dataService.getData(data.getId());

    return data;
  }

  /**
   * Test the exception handling.
   *
   * @throws ServiceUnavailableException if an exception is thrown
   */
  @WebMethod(operationName = "TestExceptionHandling")
  public void testExceptionHandling() throws ServiceUnavailableException {
    throw new ServiceUnavailableException("Testing 1.. 2.. 3..");
  }

  /**
   * Test the local date time mapping.
   *
   * @param localDateTime the local date time
   * @return the local date time
   */
  @WebMethod(operationName = "TestLocalDateTime")
  public LocalDateTime testLocalDateTime(
      @WebParam(name = "LocalDateTime") @XmlElement(required = true) LocalDateTime localDateTime) {
    System.out.println("localDateTime = " + localDateTime);
    return localDateTime;
  }

  /**
   * Test the offset date time mapping.
   *
   * @param offsetDateTime the offset date time
   * @return the offset date time
   */
  @WebMethod(operationName = "TestOffsetDateTime")
  public OffsetDateTime testOffsetDateTime(
      @WebParam(name = "OffsetDateTime") @XmlElement(required = true)
          OffsetDateTime offsetDateTime) {
    System.out.println("offsetDateTime = " + offsetDateTime);
    return offsetDateTime;
  }

  /**
   * Test the zoned date time mapping.
   *
   * @param zonedDateTime the zoned date time
   * @return the zoned date time
   */
  @WebMethod(operationName = "TestZonedDateTime")
  public ZonedDateTime testZonedDateTime(
      @WebParam(name = "ZonedDateTime") @XmlElement(required = true) ZonedDateTime zonedDateTime) {
    System.out.println("zonedDateTime = " + zonedDateTime);
    return zonedDateTime;
  }

  /**
   * Validate the data.
   *
   * @param data the data to validate
   * @throws InvalidArgumentException if an argument is invalid
   * @throws ServiceUnavailableException if the data could not be validated
   */
  @WebMethod(operationName = "ValidateData")
  public void validateData(@WebParam(name = "Data") @XmlElement(required = true) Data data)
      throws InvalidArgumentException, ServiceUnavailableException {
    dataService.validateData(data);
  }
}
