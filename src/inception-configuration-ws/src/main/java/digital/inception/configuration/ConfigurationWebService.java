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

package digital.inception.configuration;



import digital.inception.core.validation.InvalidArgumentException;
import java.util.List;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.xml.bind.annotation.XmlElement;



/**
 * The <b>ConfigurationWebService</b> class.
 *
 * @author Marcus Portmann
 */
@WebService(
    serviceName = "ConfigurationService",
    name = "IConfigurationService",
    targetNamespace = "http://configuration.inception.digital")
@SOAPBinding
@SuppressWarnings({"unused", "ValidExternallyBoundObject"})
public class ConfigurationWebService {

  /** The Configuration Service. */
  private final IConfigurationService configurationService;

  /**
   * Constructs a new <b>ConfigurationWebService</b>.
   *
   * @param configurationService the Configuration Service
   */
  public ConfigurationWebService(IConfigurationService configurationService) {
    this.configurationService = configurationService;
  }

  /**
   * Delete the configuration.
   *
   * @param key the key for the configuration
   */
  @WebMethod(operationName = "DeleteConfiguration")
  public void deleteConfiguration(@WebParam(name = "Key") @XmlElement(required = true) String key)
      throws InvalidArgumentException, ConfigurationNotFoundException,
          ConfigurationServiceException {
    configurationService.deleteConfiguration(key);
  }

  /**
   * Retrieve the configuration.
   *
   * @param key the key for the configuration
   * @return the configuration
   */
  @WebMethod(operationName = "GetConfiguration")
  @WebResult(name = "Configuration")
  public Configuration getConfiguration(
      @WebParam(name = "Key") @XmlElement(required = true) String key)
      throws InvalidArgumentException, ConfigurationNotFoundException,
          ConfigurationServiceException {
    return configurationService.getConfiguration(key);
  }

  /**
   * Retrieve the configuration value.
   *
   * @param key the key for the configuration
   * @return the configuration value
   */
  @WebMethod(operationName = "GetConfigurationValue")
  @WebResult(name = "ConfigurationValue")
  public String getConfigurationValue(
      @WebParam(name = "Key") @XmlElement(required = true) String key)
      throws InvalidArgumentException, ConfigurationNotFoundException,
          ConfigurationServiceException {
    return configurationService.getString(key);
  }

  /**
   * Retrieve all the configurations.
   *
   * @return all the configurations
   */
  @WebMethod(operationName = "GetConfigurations")
  @WebResult(name = "Configuration")
  public List<Configuration> getConfigurations() throws ConfigurationServiceException {
    return configurationService.getConfigurations();
  }

  /**
   * Set the configuration.
   *
   * @param configuration the configuration
   */
  @WebMethod(operationName = "SetConfiguration")
  public void setConfiguration(
      @WebParam(name = "Configuration") @XmlElement(required = true) Configuration configuration)
      throws InvalidArgumentException, ConfigurationServiceException {
    configurationService.setConfiguration(configuration);
  }
}
