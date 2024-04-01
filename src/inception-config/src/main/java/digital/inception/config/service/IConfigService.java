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

package digital.inception.config.service;

import digital.inception.config.model.Config;
import digital.inception.config.model.ConfigNotFoundException;
import digital.inception.config.model.ConfigSummary;
import digital.inception.core.service.InvalidArgumentException;
import digital.inception.core.service.ServiceUnavailableException;
import java.util.List;

/**
 * The <b>IConfigService</b> interface defines the functionality provided by a Config Service
 * implementation, which manages the config information for an application or service.
 *
 * @author Marcus Portmann
 */
public interface IConfigService {

  /**
   * Delete the config with the specified key.
   *
   * @param key the key for the config
   * @throws InvalidArgumentException if an argument is invalid
   * @throws ConfigNotFoundException if the config could not be found
   * @throws ServiceUnavailableException if the config could not be deleted
   */
  void deleteConfig(String key)
      throws InvalidArgumentException, ConfigNotFoundException, ServiceUnavailableException;

  /**
   * Retrieve the binary config.
   *
   * @param key the key for the config
   * @return the binary config
   * @throws InvalidArgumentException if an argument is invalid
   * @throws ConfigNotFoundException if the config could not be found
   * @throws ServiceUnavailableException if the binary config value could not be retrieved
   */
  byte[] getBinary(String key)
      throws InvalidArgumentException, ConfigNotFoundException, ServiceUnavailableException;

  /**
   * Retrieve the binary config.
   *
   * @param key the key for the config
   * @param defaultValue the default value to return if the config does not exist
   * @return the binary config or the default value if the config does not exist
   * @throws InvalidArgumentException if an argument is invalid
   * @throws ServiceUnavailableException if the binary config value could not be retrieved
   */
  byte[] getBinary(String key, byte[] defaultValue)
      throws InvalidArgumentException, ServiceUnavailableException;

  /**
   * Retrieve the <b>Boolean</b> config.
   *
   * @param key the key for the config
   * @return the <b>Boolean</b> config
   * @throws InvalidArgumentException if an argument is invalid
   * @throws ConfigNotFoundException if the config could not be found
   * @throws ServiceUnavailableException if the boolean config value could not be retrieved
   */
  boolean getBoolean(String key)
      throws InvalidArgumentException, ConfigNotFoundException, ServiceUnavailableException;

  /**
   * Retrieve the <b>Boolean</b> config.
   *
   * @param key the key for the config
   * @param defaultValue the default value to return if the config does not exist
   * @return the <b>Boolean</b> config or the default value if the config value does not exist
   * @throws InvalidArgumentException if an argument is invalid
   * @throws ServiceUnavailableException if the boolean config value could not be retrieved
   */
  boolean getBoolean(String key, boolean defaultValue)
      throws InvalidArgumentException, ServiceUnavailableException;

  /**
   * Retrieve the config.
   *
   * @param key the key for the config
   * @return the config
   * @throws InvalidArgumentException if an argument is invalid
   * @throws ConfigNotFoundException if the config could not be found
   * @throws ServiceUnavailableException if the config could not be retrieved
   */
  Config getConfig(String key)
      throws InvalidArgumentException, ConfigNotFoundException, ServiceUnavailableException;

  /**
   * Retrieve all the config summaries.
   *
   * @return the config summaries
   * @throws ServiceUnavailableException if the config summaries could not be retrieved
   */
  List<ConfigSummary> getConfigSummaries() throws ServiceUnavailableException;

  /**
   * Retrieve all the configs.
   *
   * @return the configs
   * @throws ServiceUnavailableException if the configs could not be retrieved
   */
  List<Config> getConfigs() throws ServiceUnavailableException;

  /**
   * Retrieve the <b>Double</b> config.
   *
   * @param key the key for the config
   * @return the <b>Double</b> config
   * @throws InvalidArgumentException if an argument is invalid
   * @throws ConfigNotFoundException if the config could not be found
   * @throws ServiceUnavailableException if the double config value could not be retrieved
   */
  Double getDouble(String key)
      throws InvalidArgumentException, ConfigNotFoundException, ServiceUnavailableException;

  /**
   * Retrieve the <b>Double</b> config.
   *
   * @param key the key for the config
   * @param defaultValue the default value to return if the config does not exist
   * @return the <b>Double</b> config or the default value if the config entry does not exist
   * @throws InvalidArgumentException if an argument is invalid
   * @throws ServiceUnavailableException if the double config value could not be retrieved
   */
  double getDouble(String key, double defaultValue)
      throws InvalidArgumentException, ServiceUnavailableException;

  /**
   * Retrieve the filtered config summaries.
   *
   * @param filter the filter to apply to the keys for the config summaries
   * @return the filtered config summaries
   * @throws ServiceUnavailableException if the filtered config summaries could not be retrieved
   */
  List<ConfigSummary> getFilteredConfigSummaries(String filter) throws ServiceUnavailableException;

  /**
   * Retrieve the filtered configs.
   *
   * @param filter the filter to apply to the keys for the config
   * @return the filtered configs
   * @throws ServiceUnavailableException if the filtered configs could not be retrieved
   */
  List<Config> getFilteredConfigs(String filter) throws ServiceUnavailableException;

  /**
   * Retrieve the <b>Integer</b> config.
   *
   * @param key the key for the config
   * @return the <b>Integer</b> config
   * @throws InvalidArgumentException if an argument is invalid
   * @throws ConfigNotFoundException if the config could not be found
   * @throws ServiceUnavailableException if the integer config value could not be retrieved
   */
  Integer getInteger(String key)
      throws InvalidArgumentException, ConfigNotFoundException, ServiceUnavailableException;

  /**
   * Retrieve the <b>Integer</b> config.
   *
   * @param key the key for the config
   * @param defaultValue the default value to return if the config does not exist
   * @return the <b>Integer</b> config or the default value if the config entry does not exist
   * @throws InvalidArgumentException if an argument is invalid
   * @throws ServiceUnavailableException if the integer config value could not be retrieved
   */
  int getInteger(String key, int defaultValue)
      throws InvalidArgumentException, ServiceUnavailableException;

  /**
   * Retrieve the <b>Long</b> config.
   *
   * @param key the key for the config
   * @return the <b>Long</b> config
   * @throws InvalidArgumentException if an argument is invalid
   * @throws ConfigNotFoundException if the config could not be found
   * @throws ServiceUnavailableException if the long config value could not be retrieved
   */
  Long getLong(String key)
      throws InvalidArgumentException, ConfigNotFoundException, ServiceUnavailableException;

  /**
   * Retrieve the <b>Long</b> config.
   *
   * @param key the key for the config
   * @param defaultValue the default value to return if the config does not exist
   * @return the <b>Long</b> config or the default value if the config entry does not exist
   * @throws InvalidArgumentException if an argument is invalid
   * @throws ServiceUnavailableException if the long config value could not be retrieved
   */
  long getLong(String key, long defaultValue)
      throws InvalidArgumentException, ServiceUnavailableException;

  /**
   * Retrieve the value for the <b>String</b> config.
   *
   * @param key the key for the config
   * @return the value for the <b>String</b> config
   * @throws InvalidArgumentException if an argument is invalid
   * @throws ConfigNotFoundException if the config could not be found
   * @throws ServiceUnavailableException if the string config value could not be retrieved
   */
  String getString(String key)
      throws InvalidArgumentException, ConfigNotFoundException, ServiceUnavailableException;

  /**
   * Retrieve the value for the <b>String</b> config.
   *
   * @param key the key for the config
   * @param defaultValue the default value to return if the config does not exist
   * @return the value for the <b>String</b> config or the default value if the config does not
   *     exist
   * @throws InvalidArgumentException if an argument is invalid
   * @throws ServiceUnavailableException if the string config value could not be retrieved
   */
  String getString(String key, String defaultValue)
      throws InvalidArgumentException, ServiceUnavailableException;

  /**
   * Check if a config with the specified key exists.
   *
   * @param key the key for the config
   * @return <b>true</b> if the config with the specified key exists or <b>false</b> otherwise
   * @throws InvalidArgumentException if an argument is invalid
   * @throws ServiceUnavailableException if the check for the existing key failed
   */
  boolean keyExists(String key) throws InvalidArgumentException, ServiceUnavailableException;

  /**
   * Set the config.
   *
   * @param config the config
   * @throws InvalidArgumentException if an argument is invalid
   * @throws ServiceUnavailableException if the config could not be set
   */
  void setConfig(Config config) throws InvalidArgumentException, ServiceUnavailableException;

  /**
   * Set the config key to the specified value.
   *
   * @param key the key for the config
   * @param value the value for the config
   * @param description the description for the config
   * @throws InvalidArgumentException if an argument is invalid
   * @throws ServiceUnavailableException if the config could not be set
   */
  void setConfig(String key, Object value, String description)
      throws InvalidArgumentException, ServiceUnavailableException;
}
