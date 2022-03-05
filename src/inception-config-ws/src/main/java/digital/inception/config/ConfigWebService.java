/*
 * Copyright 2022 Marcus Portmann
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

package digital.inception.config;

import digital.inception.core.service.InvalidArgumentException;
import digital.inception.core.service.ServiceUnavailableException;
import java.util.List;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.xml.bind.annotation.XmlElement;

/**
 * The <b>ConfigWebService</b> class.
 *
 * @author Marcus Portmann
 */
@WebService(
    serviceName = "ConfigService",
    name = "IConfigService",
    targetNamespace = "http://inception.digital/config")
@SOAPBinding
@SuppressWarnings({"unused", "ValidExternallyBoundObject"})
public class ConfigWebService {

  /** The Config Service. */
  private final IConfigService configService;

  /**
   * Constructs a new <b>ConfigWebService</b>.
   *
   * @param configService the Config Service
   */
  public ConfigWebService(IConfigService configService) {
    this.configService = configService;
  }

  /**
   * Delete the config.
   *
   * @param key the key for the config
   * @throws InvalidArgumentException if an argument is invalid
   * @throws ConfigNotFoundException if the config could not be found
   * @throws ServiceUnavailableException if the config could not be deleted
   */
  @WebMethod(operationName = "DeleteConfig")
  public void deleteConfig(@WebParam(name = "Key") @XmlElement(required = true) String key)
      throws InvalidArgumentException, ConfigNotFoundException, ServiceUnavailableException {
    configService.deleteConfig(key);
  }

  /**
   * Retrieve the config.
   *
   * @param key the key for the config
   * @return the config
   * @throws InvalidArgumentException if an argument is invalid
   * @throws ConfigNotFoundException if the config could not be found
   * @throws ServiceUnavailableException if the config could not be retrieved
   */
  @WebMethod(operationName = "GetConfig")
  @WebResult(name = "Config")
  public Config getConfig(@WebParam(name = "Key") @XmlElement(required = true) String key)
      throws InvalidArgumentException, ConfigNotFoundException, ServiceUnavailableException {
    return configService.getConfig(key);
  }

  /**
   * Retrieve the config value.
   *
   * @param key the key for the config
   * @return the config value
   * @throws InvalidArgumentException if an argument is invalid
   * @throws ConfigNotFoundException if the config could not be found
   * @throws ServiceUnavailableException if the config value could not be retrieved
   */
  @WebMethod(operationName = "GetConfigValue")
  @WebResult(name = "ConfigValue")
  public String getConfigValue(@WebParam(name = "Key") @XmlElement(required = true) String key)
      throws InvalidArgumentException, ConfigNotFoundException, ServiceUnavailableException {
    return configService.getString(key);
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
