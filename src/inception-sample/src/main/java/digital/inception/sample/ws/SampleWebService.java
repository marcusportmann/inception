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

package digital.inception.sample.ws;

// ~--- non-JDK imports --------------------------------------------------------

import digital.inception.sample.model.Data;
import digital.inception.sample.model.ISampleService;
import digital.inception.sample.model.SampleServiceException;
import digital.inception.core.validation.ValidationError;
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

// ~--- JDK imports ------------------------------------------------------------

/**
 * The <code>SampleWebService</code> class.
 *
 * @author Marcus Portmann
 */
@WebService(
    serviceName = "SampleService",
    name = "ISampleService",
    targetNamespace = "http://sample.inception.digital")
@SOAPBinding
@SuppressWarnings({"unused", "ValidExternallyBoundObject"})
public class SampleWebService {

  private ISampleService sampleService;

  /**
   * Constructs a new <code>SampleWebService</code>.
   *
   * @param sampleService the Sample Service
   */
  public SampleWebService(ISampleService sampleService) {
    this.sampleService = sampleService;
  }

  /**
   * Returns all the data.
   *
   * @return all the data
   */
  @WebMethod(operationName = "GetAllData")
  @WebResult(name = "Data")
  public List<Data> allData() throws SampleServiceException {
    return sampleService.getAllData();
  }

  /**
   * Retrieve the data.
   *
   * @return the data
   */
  @WebMethod(operationName = "GetData")
  @WebResult(name = "Data")
  public Data getData() throws SampleServiceException {
    long id = System.currentTimeMillis();

    Data data =
        new Data(
            id, "Test Name " + id, 777, "Test Value " + id, LocalDate.now(), LocalDateTime.now());

    sampleService.addData(data);

    data = sampleService.getData(data.getId());

    return data;
  }

  /** Test the exception handling. */
  @WebMethod(operationName = "TestExceptionHandling")
  public void testExceptionHandling() throws SampleServiceException {
    throw new SampleServiceException("Testing 1.. 2.. 3..");
  }

  /**
   * Test the local date time mapping.
   *
   * @param localDateTime the local date time
   */
  @WebMethod(operationName = "TestLocalDateTime")
  public LocalDateTime testLocalDateTime(
      @WebParam(name = "LocalDateTime") @XmlElement(required = true) LocalDateTime localDateTime)
      throws SampleServiceException {
    if (true) {
      throw new SampleServiceException("Testing 1.. 2.. 3...");
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
      throws SampleServiceException {
    if (false) {
      throw new SampleServiceException("Testing 1.. 2.. 3...");
    }

    System.out.println("zonedDateTime = " + zonedDateTime);

    return zonedDateTime;
  }

  /** Validate the data. */
  @WebMethod(operationName = "Validate")
  @WebResult(name = "ValidationError")
  public List<ValidationError> validate(
      @WebParam(name = "Data") @XmlElement(required = true) Data data)
      throws SampleServiceException {
    return sampleService.validate(data);
  }
}
