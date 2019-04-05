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

package digital.inception.configuration;

//~--- non-JDK imports --------------------------------------------------------

import digital.inception.core.util.StringUtil;
import digital.inception.validation.InvalidArgumentException;
import digital.inception.validation.ValidationError;

import org.springframework.beans.factory.annotation.Autowired;

//~--- JDK imports ------------------------------------------------------------

import java.util.List;
import java.util.Set;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;

import javax.xml.bind.annotation.XmlElement;

/**
 * The <code>ConfigurationWebService</code> class.
 *
 * @author Marcus Portmann
 */
@WebService(serviceName = "ConfigurationService", name = "IConfigurationService",
    targetNamespace = "http://configuration.inception.digital")
@SOAPBinding(style = SOAPBinding.Style.DOCUMENT, use = SOAPBinding.Use.LITERAL,
    parameterStyle = SOAPBinding.ParameterStyle.WRAPPED)
@SuppressWarnings({ "unused", "WeakerAccess" })
public class ConfigurationWebService
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
  @WebMethod(operationName = "DeleteConfiguration")
  public void deleteConfiguration(@WebParam(name = "Key")
  @XmlElement(required = true) String key)
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
  @WebMethod(operationName = "GetConfiguration")
  public Configuration getConfiguration(@WebParam(name = "Key")
  @XmlElement(required = true) String key)
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
  @WebMethod(operationName = "GetConfigurationValue")
  public String getConfigurationValue(@WebParam(name = "Key")
  @XmlElement(required = true) String key)
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
  @WebMethod(operationName = "GetConfigurations")
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
  @WebMethod(operationName = "SetConfiguration")
  public void setConfiguration(@WebParam(name = "Configuration")
  @XmlElement(required = true) Configuration configuration)
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
