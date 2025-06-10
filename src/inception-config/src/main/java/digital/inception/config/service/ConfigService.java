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

import digital.inception.config.exception.ConfigNotFoundException;
import digital.inception.config.model.Config;
import digital.inception.config.model.ConfigSummary;
import digital.inception.core.exception.InvalidArgumentException;
import digital.inception.core.exception.ServiceUnavailableException;
import java.util.List;

/**
 * The {@code ConfigService} interface defines the functionality provided by a Config Service
 * implementation, which manages the config information for an application or service.
 *
 * @author Marcus Portmann
 */
public interface ConfigService {

  /**
   * Delete the config with the specified ID.
   *
   * @param id the ID for the config
   * @throws InvalidArgumentException if an argument is invalid
   * @throws ConfigNotFoundException if the config could not be found
   * @throws ServiceUnavailableException if the config could not be deleted
   */
  void deleteConfig(String id)
      throws InvalidArgumentException, ConfigNotFoundException, ServiceUnavailableException;

  /**
   * Retrieve the binary config.
   *
   * @param id the ID for the config
   * @return the binary config
   * @throws InvalidArgumentException if an argument is invalid
   * @throws ConfigNotFoundException if the config could not be found
   * @throws ServiceUnavailableException if the binary config value could not be retrieved
   */
  byte[] getBinary(String id)
      throws InvalidArgumentException, ConfigNotFoundException, ServiceUnavailableException;

  /**
   * Retrieve the binary config.
   *
   * @param id the ID for the config
   * @param defaultValue the default value to return if the config does not exist
   * @return the binary config or the default value if the config does not exist
   * @throws InvalidArgumentException if an argument is invalid
   * @throws ServiceUnavailableException if the binary config value could not be retrieved
   */
  byte[] getBinary(String id, byte[] defaultValue)
      throws InvalidArgumentException, ServiceUnavailableException;

  /**
   * Retrieve the {@code Boolean} config.
   *
   * @param id the ID for the config
   * @return the {@code Boolean} config
   * @throws InvalidArgumentException if an argument is invalid
   * @throws ConfigNotFoundException if the config could not be found
   * @throws ServiceUnavailableException if the boolean config value could not be retrieved
   */
  boolean getBoolean(String id)
      throws InvalidArgumentException, ConfigNotFoundException, ServiceUnavailableException;

  /**
   * Retrieve the {@code Boolean} config.
   *
   * @param id the ID for the config
   * @param defaultValue the default value to return if the config does not exist
   * @return the {@code Boolean} config or the default value if the config value does not exist
   * @throws InvalidArgumentException if an argument is invalid
   * @throws ServiceUnavailableException if the boolean config value could not be retrieved
   */
  boolean getBoolean(String id, boolean defaultValue)
      throws InvalidArgumentException, ServiceUnavailableException;

  /**
   * Retrieve the config.
   *
   * @param id the ID for the config
   * @return the config
   * @throws InvalidArgumentException if an argument is invalid
   * @throws ConfigNotFoundException if the config could not be found
   * @throws ServiceUnavailableException if the config could not be retrieved
   */
  Config getConfig(String id)
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
   * Retrieve the {@code Double} config.
   *
   * @param id the ID for the config
   * @return the {@code Double} config
   * @throws InvalidArgumentException if an argument is invalid
   * @throws ConfigNotFoundException if the config could not be found
   * @throws ServiceUnavailableException if the double config value could not be retrieved
   */
  Double getDouble(String id)
      throws InvalidArgumentException, ConfigNotFoundException, ServiceUnavailableException;

  /**
   * Retrieve the {@code Double} config.
   *
   * @param id the ID for the config
   * @param defaultValue the default value to return if the config does not exist
   * @return the {@code Double} config or the default value if the config entry does not exist
   * @throws InvalidArgumentException if an argument is invalid
   * @throws ServiceUnavailableException if the double config value could not be retrieved
   */
  double getDouble(String id, double defaultValue)
      throws InvalidArgumentException, ServiceUnavailableException;

  /**
   * Retrieve the filtered config summaries.
   *
   * @param filter the filter to apply to the IDs for the config summaries
   * @return the filtered config summaries
   * @throws ServiceUnavailableException if the filtered config summaries could not be retrieved
   */
  List<ConfigSummary> getFilteredConfigSummaries(String filter) throws ServiceUnavailableException;

  /**
   * Retrieve the filtered configs.
   *
   * @param filter the filter to apply to the IDs for the configs
   * @return the filtered configs
   * @throws ServiceUnavailableException if the filtered configs could not be retrieved
   */
  List<Config> getFilteredConfigs(String filter) throws ServiceUnavailableException;

  /**
   * Retrieve the {@code Integer} config.
   *
   * @param id the ID for the config
   * @return the {@code Integer} config
   * @throws InvalidArgumentException if an argument is invalid
   * @throws ConfigNotFoundException if the config could not be found
   * @throws ServiceUnavailableException if the integer config value could not be retrieved
   */
  Integer getInteger(String id)
      throws InvalidArgumentException, ConfigNotFoundException, ServiceUnavailableException;

  /**
   * Retrieve the {@code Integer} config.
   *
   * @param id the ID for the config
   * @param defaultValue the default value to return if the config does not exist
   * @return the {@code Integer} config or the default value if the config entry does not exist
   * @throws InvalidArgumentException if an argument is invalid
   * @throws ServiceUnavailableException if the integer config value could not be retrieved
   */
  int getInteger(String id, int defaultValue)
      throws InvalidArgumentException, ServiceUnavailableException;

  /**
   * Retrieve the {@code Long} config.
   *
   * @param id the ID for the config
   * @return the {@code Long} config
   * @throws InvalidArgumentException if an argument is invalid
   * @throws ConfigNotFoundException if the config could not be found
   * @throws ServiceUnavailableException if the long config value could not be retrieved
   */
  Long getLong(String id)
      throws InvalidArgumentException, ConfigNotFoundException, ServiceUnavailableException;

  /**
   * Retrieve the {@code Long} config.
   *
   * @param id the ID for the config
   * @param defaultValue the default value to return if the config does not exist
   * @return the {@code Long} config or the default value if the config entry does not exist
   * @throws InvalidArgumentException if an argument is invalid
   * @throws ServiceUnavailableException if the long config value could not be retrieved
   */
  long getLong(String id, long defaultValue)
      throws InvalidArgumentException, ServiceUnavailableException;

  /**
   * Retrieve the value for the {@code String} config.
   *
   * @param id the ID for the config
   * @return the value for the {@code String} config
   * @throws InvalidArgumentException if an argument is invalid
   * @throws ConfigNotFoundException if the config could not be found
   * @throws ServiceUnavailableException if the string config value could not be retrieved
   */
  String getString(String id)
      throws InvalidArgumentException, ConfigNotFoundException, ServiceUnavailableException;

  /**
   * Retrieve the value for the {@code String} config.
   *
   * @param id the ID for the config
   * @param defaultValue the default value to return if the config does not exist
   * @return the value for the {@code String} config or the default value if the config does not
   *     exist
   * @throws InvalidArgumentException if an argument is invalid
   * @throws ServiceUnavailableException if the string config value could not be retrieved
   */
  String getString(String id, String defaultValue)
      throws InvalidArgumentException, ServiceUnavailableException;

  /**
   * Check if a config with the specified ID exists.
   *
   * @param id the ID for the config
   * @return {@code true} if the config with the specified ID exists or {@code false} otherwise
   * @throws InvalidArgumentException if an argument is invalid
   * @throws ServiceUnavailableException if the check for the existing ID failed
   */
  boolean idExists(String id) throws InvalidArgumentException, ServiceUnavailableException;

  /**
   * Set the config.
   *
   * @param config the config
   * @throws InvalidArgumentException if an argument is invalid
   * @throws ServiceUnavailableException if the config could not be set
   */
  void setConfig(Config config) throws InvalidArgumentException, ServiceUnavailableException;

  /**
   * Set the config.
   *
   * @param id the ID for the config
   * @param value the value for the config
   * @param description the description for the config
   * @throws InvalidArgumentException if an argument is invalid
   * @throws ServiceUnavailableException if the config could not be set
   */
  void setConfig(String id, Object value, String description)
      throws InvalidArgumentException, ServiceUnavailableException;
}
