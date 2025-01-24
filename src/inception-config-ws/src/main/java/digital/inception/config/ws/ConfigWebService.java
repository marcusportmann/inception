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

package digital.inception.config.ws;

import digital.inception.config.model.Config;
import digital.inception.config.model.ConfigNotFoundException;
import digital.inception.config.model.ConfigSummary;
import digital.inception.config.service.ConfigService;
import digital.inception.core.service.InvalidArgumentException;
import digital.inception.core.service.ServiceUnavailableException;
import jakarta.jws.WebMethod;
import jakarta.jws.WebParam;
import jakarta.jws.WebResult;
import jakarta.jws.WebService;
import jakarta.jws.soap.SOAPBinding;
import jakarta.xml.bind.annotation.XmlElement;
import java.util.List;

/**
 * The <b>ConfigWebService</b> class.
 *
 * @author Marcus Portmann
 */
@WebService(
    serviceName = "ConfigService",
    name = "IConfigService",
    targetNamespace = "https://inception.digital/config")
@SOAPBinding
@SuppressWarnings({"unused", "ValidExternallyBoundObject"})
public class ConfigWebService {

  /** The Config Service. */
  private final ConfigService configService;

  /**
   * Constructs a new <b>ConfigWebService</b>.
   *
   * @param configService the Config Service
   */
  public ConfigWebService(ConfigService configService) {
    this.configService = configService;
  }

  /**
   * Delete the config.
   *
   * @param id the ID for the config
   * @throws InvalidArgumentException if an argument is invalid
   * @throws ConfigNotFoundException if the config could not be found
   * @throws ServiceUnavailableException if the config could not be deleted
   */
  @WebMethod(operationName = "DeleteConfig")
  public void deleteConfig(@WebParam(name = "Id") @XmlElement(required = true) String id)
      throws InvalidArgumentException, ConfigNotFoundException, ServiceUnavailableException {
    configService.deleteConfig(id);
  }

  /**
   * Retrieve the config.
   *
   * @param id the ID for the config
   * @return the config
   * @throws InvalidArgumentException if an argument is invalid
   * @throws ConfigNotFoundException if the config could not be found
   * @throws ServiceUnavailableException if the config could not be retrieved
   */
  @WebMethod(operationName = "GetConfig")
  @WebResult(name = "Config")
  public Config getConfig(@WebParam(name = "Id") @XmlElement(required = true) String id)
      throws InvalidArgumentException, ConfigNotFoundException, ServiceUnavailableException {
    return configService.getConfig(id);
  }

  /**
   * Retrieve all the config summaries.
   *
   * @return all the config summaries
   * @throws ServiceUnavailableException if the config summaries could not be retrieved
   */
  @WebMethod(operationName = "GetConfigSummaries")
  @WebResult(name = "ConfigSummary")
  public List<ConfigSummary> getConfigSummaries() throws ServiceUnavailableException {
    return configService.getConfigSummaries();
  }

  /**
   * Retrieve the config value.
   *
   * @param id the ID for the config
   * @return the config value
   * @throws InvalidArgumentException if an argument is invalid
   * @throws ConfigNotFoundException if the config could not be found
   * @throws ServiceUnavailableException if the config value could not be retrieved
   */
  @WebMethod(operationName = "GetConfigValue")
  @WebResult(name = "ConfigValue")
  public String getConfigValue(@WebParam(name = "Id") @XmlElement(required = true) String id)
      throws InvalidArgumentException, ConfigNotFoundException, ServiceUnavailableException {
    return configService.getString(id);
  }

  /**
   * Retrieve all the configs.
   *
   * @return all the configs
   * @throws ServiceUnavailableException if the configs could not be retrieved
   */
  @WebMethod(operationName = "GetConfigs")
  @WebResult(name = "Config")
  public List<Config> getConfigs() throws ServiceUnavailableException {
    return configService.getConfigs();
  }

  /**
   * Set the config.
   *
   * @param config the config
   * @throws InvalidArgumentException if an argument is invalid
   * @throws ServiceUnavailableException if the config could not be set
   */
  @WebMethod(operationName = "SetConfig")
  public void setConfig(@WebParam(name = "Config") @XmlElement(required = true) Config config)
      throws InvalidArgumentException, ServiceUnavailableException {
    configService.setConfig(config);
  }
}
