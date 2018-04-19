/*
 * Copyright 2018 Marcus Portmann
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

package digital.inception.configuration;

//~--- non-JDK imports --------------------------------------------------------

import digital.inception.core.util.StringUtil;
import digital.inception.rs.RestControllerError;
import digital.inception.validation.InvalidArgumentException;
import digital.inception.validation.ValidationError;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.util.List;
import java.util.Set;

//~--- JDK imports ------------------------------------------------------------

/**
 * The <code>ConfigurationRestController</code> class.
 *
 * @author Marcus Portmann
 */
@RestController
@RequestMapping(value = "/")
@SuppressWarnings({ "unused", "WeakerAccess" })
public class ConfigurationRestController
{
  /**
   * The Configuration Service.
   */
  @Autowired
  private IConfigurationService configurationService;

  /* Validator */
  @Autowired
  private Validator validator;

  /**
   * Delete the configuration.
   *
   * @param key the key used to uniquely identify the configuration
   */
  @ApiOperation(value = "Delete the configuration", notes = "Delete the configuration")
  @ApiResponses(value = { @ApiResponse(code = 204,
      message = "The configuration was deleted successfully") ,
      @ApiResponse(code = 400, message = "Invalid argument", response = RestControllerError.class) ,
      @ApiResponse(code = 404, message = "The configuration could not be found",
          response = RestControllerError.class) ,
      @ApiResponse(code = 500,
          message = "An error has occurred and the service is unable to process the request at this time",
          response = RestControllerError.class) })
  @RequestMapping(value = "/api/configuration/{key}", method = RequestMethod.DELETE,
      produces = "application/json")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void deleteConfiguration(@ApiParam(name = "key",
      value = "The key used to uniquely identify the configuration", required = true)
  @PathVariable String key)
    throws InvalidArgumentException, ConfigurationNotFoundException, ConfigurationServiceException
  {
    if (StringUtil.isNullOrEmpty(key))
    {
      throw new InvalidArgumentException("key");
    }

    configurationService.deleteConfiguration(key);
  }

  /**
   * Retrieve the configuration.
   *
   * @param key the key used to uniquely identify the configuration
   *
   * @return the configuration
   */
  @ApiOperation(value = "Retrieve the configuration", notes = "Retrieve the configuration")
  @ApiResponses(value = { @ApiResponse(code = 200, message = "OK") ,
      @ApiResponse(code = 400, message = "Invalid argument", response = RestControllerError.class) ,
      @ApiResponse(code = 404, message = "The configuration could not be found",
          response = RestControllerError.class) ,
      @ApiResponse(code = 500,
          message = "An error has occurred and the service is unable to process the request at this time",
          response = RestControllerError.class) })
  @RequestMapping(value = "/api/configuration/{key}", method = RequestMethod.GET,
      produces = "application/json")
  public Configuration getConfiguration(@ApiParam(name = "key",
      value = "The key used to uniquely identify the configuration", required = true)
  @PathVariable String key)
    throws InvalidArgumentException, ConfigurationNotFoundException, ConfigurationServiceException
  {
    if (StringUtil.isNullOrEmpty(key))
    {
      throw new InvalidArgumentException("key");
    }

    return configurationService.getConfiguration(key);
  }

  /**
   * Retrieve the configuration value.
   *
   * @param key the key used to uniquely identify the configuration
   *
   * @return the configuration value
   */
  @ApiOperation(value = "Retrieve the configuration value",
      notes = "Retrieve the configuration value")
  @ApiResponses(value = { @ApiResponse(code = 200, message = "OK") ,
      @ApiResponse(code = 400, message = "Invalid argument", response = RestControllerError.class) ,
      @ApiResponse(code = 404, message = "The configuration could not be found",
          response = RestControllerError.class) ,
      @ApiResponse(code = 500,
          message = "An error has occurred and the service is unable to process the request at this time",
          response = RestControllerError.class) })
  @RequestMapping(value = "/api/configuration/{key}/value", method = RequestMethod.GET)
  @ResponseBody
  public String getConfigurationValue(@ApiParam(name = "key",
      value = "The key used to uniquely identify the configuration", required = true)
  @PathVariable String key)
    throws InvalidArgumentException, ConfigurationNotFoundException, ConfigurationServiceException
  {
    if (StringUtil.isNullOrEmpty(key))
    {
      throw new InvalidArgumentException("key");
    }

    return configurationService.getString(key);
  }

  /**
   * Retrieve all the configurations.
   *
   * @return all the configurations
   */
  @ApiOperation(value = "Retrieve all the configurations",
      notes = "Retrieve all the configurations")
  @ApiResponses(value = { @ApiResponse(code = 200, message = "OK") ,
      @ApiResponse(code = 500,
          message = "An error has occurred and the service is unable to process the request at this time",
          response = RestControllerError.class) })
  @RequestMapping(value = "/api/configuration", method = RequestMethod.GET,
      produces = "application/json")
  public List<Configuration> getConfigurations()
    throws ConfigurationServiceException
  {
    return configurationService.getConfigurations();
  }

  /**
   * Set the configuration.
   *
   * @param configuration the configuration
   */
  @ApiOperation(value = "Set the configuration", notes = "Set the configuration")
  @ApiResponses(value = { @ApiResponse(code = 204,
      message = "The configuration was set successfully") ,
      @ApiResponse(code = 400, message = "Invalid argument", response = RestControllerError.class) ,
      @ApiResponse(code = 500,
          message = "An error has occurred and the service is unable to process the request at this time",
          response = RestControllerError.class) })
  @RequestMapping(value = "/api/configuration", method = RequestMethod.POST,
      produces = "application/json")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void setConfiguration(@ApiParam(name = "configuration", value = "The configuration",
      required = true)
  @RequestBody Configuration configuration)
    throws InvalidArgumentException, ConfigurationServiceException
  {
    Set<ConstraintViolation<Configuration>> constraintViolations = validator.validate(
        configuration);

    if (!constraintViolations.isEmpty())
    {
      throw new InvalidArgumentException("configuration", ValidationError.toValidationErrors(
          constraintViolations));
    }

    configurationService.setConfiguration(configuration);
  }
}
