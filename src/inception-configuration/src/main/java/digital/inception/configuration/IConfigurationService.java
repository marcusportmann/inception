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

/**
 * The <b>IConfigurationService</b> interface defines the functionality provided by a
 * Configuration Service implementation, which manages the configuration information for an
 * application or service.
 *
 * @author Marcus Portmann
 */
public interface IConfigurationService {

  /**
   * Delete the configuration with the specified key.
   *
   * @param key the key for the configuration
   */
  void deleteConfiguration(String key)
      throws InvalidArgumentException, ConfigurationNotFoundException,
          ConfigurationServiceException;

  /**
   * Retrieve the binary configuration.
   *
   * @param key the key for the configuration
   * @return the binary configuration
   */
  byte[] getBinary(String key)
      throws InvalidArgumentException, ConfigurationNotFoundException,
          ConfigurationServiceException;

  /**
   * Retrieve the binary configuration.
   *
   * @param key the key for the configuration
   * @param defaultValue the default value to return if the configuration does not exist
   * @return the binary configuration or the default value if the configuration does not exist
   */
  byte[] getBinary(String key, byte[] defaultValue)
      throws InvalidArgumentException, ConfigurationServiceException;

  /**
   * Retrieve the <b>Boolean</b> configuration.
   *
   * @param key the key for the configuration
   * @return the <b>Boolean</b> configuration
   */
  boolean getBoolean(String key)
      throws InvalidArgumentException, ConfigurationNotFoundException,
          ConfigurationServiceException;

  /**
   * Retrieve the <b>Boolean</b> configuration.
   *
   * @param key the key for the configuration
   * @param defaultValue the default value to return if the configuration does not exist
   * @return the <b>Boolean</b> configuration or the default value if the configuration value
   *     does not exist
   */
  boolean getBoolean(String key, boolean defaultValue)
      throws InvalidArgumentException, ConfigurationServiceException;

  /**
   * Retrieve the configuration.
   *
   * @param key the key for the configuration
   * @return the configuration
   */
  Configuration getConfiguration(String key)
      throws InvalidArgumentException, ConfigurationNotFoundException,
          ConfigurationServiceException;

  /**
   * Retrieve all the configuration summaries.
   *
   * @return all the configuration summaries
   */
  List<ConfigurationSummary> getConfigurationSummaries() throws ConfigurationServiceException;

  /**
   * Retrieve all the configurations.
   *
   * @return all the configurations
   */
  List<Configuration> getConfigurations() throws ConfigurationServiceException;

  /**
   * Retrieve the <b>Double</b> configuration.
   *
   * @param key the key for the configuration
   * @return the <b>Double</b> configuration
   */
  Double getDouble(String key)
      throws InvalidArgumentException, ConfigurationNotFoundException,
          ConfigurationServiceException;

  /**
   * Retrieve the <b>Double</b> configuration.
   *
   * @param key the key for the configuration
   * @param defaultValue the default value to return if the configuration does not exist
   * @return the <b>Double</b> configuration or the default value if the configuration entry
   *     does not exist
   */
  double getDouble(String key, double defaultValue)
      throws InvalidArgumentException, ConfigurationServiceException;

  /**
   * Retrieve the filtered configuration summaries.
   *
   * @param filter the filter to apply to the keys for the configuration summaries
   * @return the configuration summaries
   */
  List<ConfigurationSummary> getFilteredConfigurationSummaries(String filter)
      throws ConfigurationServiceException;

  /**
   * Retrieve the filtered configurations.
   *
   * @param filter the filter to apply to the keys for the configuration
   * @return the configurations
   */
  List<Configuration> getFilteredConfigurations(String filter) throws ConfigurationServiceException;

  /**
   * Retrieve the <b>Integer</b> configuration.
   *
   * @param key the key for the configuration
   * @return the <b>Integer</b> configuration
   */
  Integer getInteger(String key)
      throws InvalidArgumentException, ConfigurationNotFoundException,
          ConfigurationServiceException;

  /**
   * Retrieve the <b>Integer</b> configuration.
   *
   * @param key the key for the configuration
   * @param defaultValue the default value to return if the configuration does not exist
   * @return the <b>Integer</b> configuration or the default value if the configuration entry
   *     does not exist
   */
  int getInteger(String key, int defaultValue)
      throws InvalidArgumentException, ConfigurationServiceException;

  /**
   * Retrieve the <b>Long</b> configuration.
   *
   * @param key the key for the configuration
   * @return the <b>Long</b> configuration
   */
  Long getLong(String key)
      throws InvalidArgumentException, ConfigurationNotFoundException,
          ConfigurationServiceException;

  /**
   * Retrieve the <b>Long</b> configuration.
   *
   * @param key the key for the configuration
   * @param defaultValue the default value to return if the configuration does not exist
   * @return the <b>Long</b> configuration or the default value if the configuration entry
   *     does not exist
   */
  long getLong(String key, long defaultValue)
      throws InvalidArgumentException, ConfigurationServiceException;

  /**
   * Retrieve the value for the <b>String</b> configuration.
   *
   * @param key the key for the configuration
   * @return the value for the <b>String</b> configuration
   */
  String getString(String key)
      throws InvalidArgumentException, ConfigurationNotFoundException,
          ConfigurationServiceException;

  /**
   * Retrieve the value for the <b>String</b> configuration.
   *
   * @param key the key for the configuration
   * @param defaultValue the default value to return if the configuration does not exist
   * @return the value for the <b>String</b> configuration or the default value if the
   *     configuration does not exist
   */
  String getString(String key, String defaultValue)
      throws InvalidArgumentException, ConfigurationServiceException;

  /**
   * Check if a configuration with the specified key exists.
   *
   * @param key the key for the configuration
   * @return <b>true</b> if the configuration with the specified key exists or <b>false
   * </b> otherwise
   */
  boolean keyExists(String key) throws InvalidArgumentException, ConfigurationServiceException;

  /**
   * Set the configuration.
   *
   * @param configuration the configuration
   */
  void setConfiguration(Configuration configuration)
      throws InvalidArgumentException, ConfigurationServiceException;

  /**
   * Set the configuration key to the specified value.
   *
   * @param key the key for the configuration
   * @param value the value for the configuration
   * @param description the description for the configuration
   */
  void setConfiguration(String key, Object value, String description)
      throws InvalidArgumentException, ConfigurationServiceException;
}
