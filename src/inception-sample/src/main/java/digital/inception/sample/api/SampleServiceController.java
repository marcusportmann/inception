/*
 * Copyright 2019 Marcus Portmann
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

package digital.inception.sample.api;

//~--- non-JDK imports --------------------------------------------------------

import digital.inception.sample.model.Data;
import digital.inception.sample.model.ISampleService;
import digital.inception.sample.model.SampleServiceException;
import digital.inception.validation.ValidationError;

import io.swagger.annotations.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

//~--- JDK imports ------------------------------------------------------------

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
 * The <code>SampleServiceController</code> class.
 *
 * @author Marcus Portmann
 */
@Api(tags="Sample API", description = "Sample Service Controller")
@RestController
@RequestMapping(value = "/api/sample")
@WebService(serviceName = "SampleService", name = "ISampleService",
    targetNamespace = "http://sample.inception.digital")
@SOAPBinding(style = SOAPBinding.Style.DOCUMENT, use = SOAPBinding.Use.LITERAL,
    parameterStyle = SOAPBinding.ParameterStyle.WRAPPED)
@SuppressWarnings({ "unused", "WeakerAccess" })
public class SampleServiceController
{
  @Autowired
  private ISampleService sampleService;

  /**
   * Returns all the data.
   *
   * @return all the data
   */
  @RequestMapping(value = "/allData", method = RequestMethod.GET, produces = "application/json")
  @WebMethod(operationName = "GetAllData")
  @WebResult(name = "Data")
  public List<Data> allData()
    throws SampleServiceException
  {
    return sampleService.getAllData();
  }

  /**
   * The data RESTful web service.
   *
   * @return the data
   */
  @RequestMapping(value = "/data", method = RequestMethod.GET, produces = "application/json")
  @WebMethod(operationName = "GetData")
  @WebResult(name = "Data")
  public Data data()
    throws SampleServiceException
  {
    long id = System.currentTimeMillis();

    Data data = new Data(id, "Test Name " + id, 777, "Test Value " + id, LocalDate.now(),
        LocalDateTime.now());

    sampleService.addData(data);

    data = sampleService.getData(data.getId());

    return data;
  }

  /**
   * Test the exception handling.
   */
  @RequestMapping(value = "/testExceptionHandling", method = RequestMethod.GET)
  @WebMethod(operationName = "TestExceptionHandling")
  public void testExceptionHandling()
    throws SampleServiceException
  {
    throw new SampleServiceException("Testing 1.. 2.. 3..");
  }

  /**
   * Test the local date time mapping.
   *
   * @param localDateTime the local date time
   */
  @ApiOperation(value = "Test the local date time serialization",
      notes = "Test the local date time serialization")
  @ApiResponses(value = { @ApiResponse(code = 200, message = "OK") })
  @RequestMapping(value = "/testLocalDateTime", method = RequestMethod.GET,
      produces = "application/json")
  @WebMethod(operationName = "TestLocalDateTime")
  public LocalDateTime testLocalDateTime(@ApiParam(name = "localDateTime",
      value = "The local date time", required = true)
  @RequestParam("localDateTime")
  @WebParam(name = "LocalDateTime")
  @XmlElement(required = true) LocalDateTime localDateTime)
    throws SampleServiceException
  {
    if (true)
    {
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
  @ApiOperation(value = "Test the zoned date time serialization",
      notes = "Test the zoned date time serialization")
  @ApiResponses(value = { @ApiResponse(code = 200, message = "OK") })
  @RequestMapping(value = "/testZonedDateTime", method = RequestMethod.GET,
      produces = "application/json")
  @WebMethod(operationName = "TestZonedDateTime")
  public ZonedDateTime testZonedDateTime(@ApiParam(name = "zonedDateTime",
      value = "The zoned date time", required = true)
  @RequestParam("zonedDateTime")
  @WebParam(name = "ZonedDateTime")
  @XmlElement(required = true) ZonedDateTime zonedDateTime)
    throws SampleServiceException
  {
    if (false)
    {
      throw new SampleServiceException("Testing 1.. 2.. 3...");
    }

    System.out.println("zonedDateTime = " + zonedDateTime);

    return zonedDateTime;
  }

  /**
   * Validate the data.
   */
  @ApiOperation(value = "Validate the data", notes = "Validate the data")
  @ApiResponses(value = { @ApiResponse(code = 200, message = "OK", response = ValidationError.class,
      responseContainer = "List") })
  @RequestMapping(value = "/validate", method = RequestMethod.POST, produces = "application/json")
  @WebMethod(operationName = "Validate")
  @WebResult(name = "ValidationError")
  public List<ValidationError> validate(@ApiParam(name = "data", value = "The data",
      required = true)
  @RequestBody
  @WebParam(name = "Data")
  @XmlElement(required = true) Data data)
    throws SampleServiceException
  {
    return sampleService.validate(data);
  }
}
