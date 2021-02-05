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

import digital.inception.core.service.ServiceUnavailableException;
import digital.inception.core.util.Base64Util;
import digital.inception.core.validation.InvalidArgumentException;
import digital.inception.core.validation.ValidationError;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

/**
 * The <b>ConfigurationService</b> class provides the Configuration Service implementation.
 *
 * @author Marcus Portmann
 */
@Service
@SuppressWarnings("unused")
public class ConfigurationService implements IConfigurationService {

  /** The Configuration Repository. */
  private final ConfigurationRepository configurationRepository;

  /** The Configuration Summary Repository. */
  private final ConfigurationSummaryRepository configurationSummaryRepository;

  /** The JSR-303 validator. */
  private final Validator validator;

  /**
   * Constructs a new <b>ConfigurationService</b>.
   *
   * @param validator the JSR-303 validator
   * @param configurationRepository the Configuration Repository
   * @param configurationSummaryRepository the Configuration Summary Repository
   */
  public ConfigurationService(
      Validator validator,
      ConfigurationRepository configurationRepository,
      ConfigurationSummaryRepository configurationSummaryRepository) {
    this.validator = validator;
    this.configurationRepository = configurationRepository;
    this.configurationSummaryRepository = configurationSummaryRepository;
  }

  /**
   * Remove the configuration with the specified key.
   *
   * @param key the key for the configuration
   */
  @Override
  @Transactional
  public void deleteConfiguration(String key)
      throws InvalidArgumentException, ConfigurationNotFoundException, ServiceUnavailableException {
    if (!StringUtils.hasText(key)) {
      throw new InvalidArgumentException("key");
    }

    try {
      if (!configurationRepository.existsByKeyIgnoreCase(key)) {
        throw new ConfigurationNotFoundException(key);
      }

      configurationRepository.deleteByKeyIgnoreCase(key);
    } catch (ConfigurationNotFoundException e) {
      throw e;
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to delete the configuration with the key (" + key + ")", e);
    }
  }

  /**
   * Retrieve the binary configuration.
   *
   * @param key the key for the configuration
   * @return the binary configuration
   */
  @Override
  public byte[] getBinary(String key)
      throws InvalidArgumentException, ConfigurationNotFoundException, ServiceUnavailableException {
    if (!StringUtils.hasText(key)) {
      throw new InvalidArgumentException("key");
    }

    try {
      Optional<String> valueOptional = configurationRepository.getValueByKeyIgnoreCase(key);

      if (valueOptional.isPresent()) {
        return Base64Util.decode(valueOptional.get());
      } else {
        throw new ConfigurationNotFoundException(key);
      }
    } catch (ConfigurationNotFoundException e) {
      throw e;
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to retrieve the binary configuration with the key (" + key + ")", e);
    }
  }

  /**
   * Retrieve the binary configuration.
   *
   * @param key the key for the configuration
   * @param defaultValue the default value to return if the configuration does not exist
   * @return the binary configuration or the default value if the configuration does not exist
   */
  @Override
  public byte[] getBinary(String key, byte[] defaultValue)
      throws InvalidArgumentException, ServiceUnavailableException {
    if (!StringUtils.hasText(key)) {
      throw new InvalidArgumentException("key");
    }

    try {
      Optional<String> valueOptional = configurationRepository.getValueByKeyIgnoreCase(key);

      return valueOptional.map(Base64Util::decode).orElse(defaultValue);
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to retrieve the binary configuration with the key (" + key + ")", e);
    }
  }

  /**
   * Retrieve the <b>Boolean</b> configuration.
   *
   * @param key the key for the configuration
   * @return the <b>Boolean</b> configuration
   */
  @Override
  public boolean getBoolean(String key)
      throws InvalidArgumentException, ConfigurationNotFoundException, ServiceUnavailableException {
    if (!StringUtils.hasText(key)) {
      throw new InvalidArgumentException("key");
    }

    try {
      Optional<String> valueOptional = configurationRepository.getValueByKeyIgnoreCase(key);

      if (valueOptional.isPresent()) {
        return Boolean.parseBoolean(valueOptional.get());
      } else {
        throw new ConfigurationNotFoundException(key);
      }
    } catch (ConfigurationNotFoundException e) {
      throw e;
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to retrieve the Boolean configuration with the key (" + key + ")", e);
    }
  }

  /**
   * Retrieve the <b>Boolean</b> configuration.
   *
   * @param key the key for the configuration
   * @param defaultValue the default value to return if the configuration does not exist
   * @return the <b>Boolean</b> configuration or the default value if the configuration value does
   *     not exist
   */
  @Override
  public boolean getBoolean(String key, boolean defaultValue)
      throws InvalidArgumentException, ServiceUnavailableException {
    if (!StringUtils.hasText(key)) {
      throw new InvalidArgumentException("key");
    }

    try {
      Optional<String> valueOptional = configurationRepository.getValueByKeyIgnoreCase(key);

      return valueOptional.map(Boolean::parseBoolean).orElse(defaultValue);
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to retrieve the Boolean configuration with the key (" + key + ")", e);
    }
  }

  /**
   * Retrieve the configuration.
   *
   * @param key the key for the configuration
   * @return the configuration
   */
  @Override
  public Configuration getConfiguration(String key)
      throws InvalidArgumentException, ConfigurationNotFoundException, ServiceUnavailableException {
    if (!StringUtils.hasText(key)) {
      throw new InvalidArgumentException("key");
    }

    try {
      Optional<Configuration> configurationOptional =
          configurationRepository.findByKeyIgnoreCase(key);

      if (configurationOptional.isEmpty()) {
        throw new ConfigurationNotFoundException(key);
      } else {
        return configurationOptional.get();
      }
    } catch (ConfigurationNotFoundException e) {
      throw e;
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to retrieve the configuration with the key (" + key + ")", e);
    }
  }

  /**
   * Retrieve all the configuration summaries.
   *
   * @return all the configuration summaries
   */
  @Override
  public List<ConfigurationSummary> getConfigurationSummaries() throws ServiceUnavailableException {
    try {
      return configurationSummaryRepository.findAllByOrderByKeyDesc();
    } catch (Throwable e) {
      throw new ServiceUnavailableException("Failed to retrieve the configuration summaries", e);
    }
  }

  /**
   * Retrieve all the configurations.
   *
   * @return all the configurations
   */
  @Override
  public List<Configuration> getConfigurations() throws ServiceUnavailableException {
    try {
      return configurationRepository.findAllByOrderByKeyDesc();
    } catch (Throwable e) {
      throw new ServiceUnavailableException("Failed to retrieve the configurations", e);
    }
  }

  /**
   * Retrieve the <b>Double</b> configuration.
   *
   * @param key the key for the configuration
   * @return the <b>Double</b> configuration
   */
  @Override
  public Double getDouble(String key)
      throws InvalidArgumentException, ConfigurationNotFoundException, ServiceUnavailableException {
    if (!StringUtils.hasText(key)) {
      throw new InvalidArgumentException("key");
    }

    try {
      Optional<String> valueOptional = configurationRepository.getValueByKeyIgnoreCase(key);

      if (valueOptional.isPresent()) {
        return Double.parseDouble(valueOptional.get());
      } else {
        throw new ConfigurationNotFoundException(key);
      }
    } catch (ConfigurationNotFoundException e) {
      throw e;
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to retrieve the Double configuration with the key (" + key + ")", e);
    }
  }

  /**
   * Retrieve the <b>Double</b> configuration.
   *
   * @param key the key for the configuration
   * @param defaultValue the default value to return if the configuration does not exist
   * @return the <b>Double</b> configuration or the default value if the configuration entry does
   *     not exist
   */
  @Override
  public double getDouble(String key, double defaultValue)
      throws InvalidArgumentException, ServiceUnavailableException {
    if (!StringUtils.hasText(key)) {
      throw new InvalidArgumentException("key");
    }

    try {
      Optional<String> valueOptional = configurationRepository.getValueByKeyIgnoreCase(key);

      return valueOptional.map(Double::parseDouble).orElse(defaultValue);
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to retrieve the Double configuration with the key (" + key + ")", e);
    }
  }

  /**
   * Retrieve the filtered configuration summaries.
   *
   * @param filter the filter to apply to the keys for the configuration summaries
   * @return the filtered configuration summaries
   */
  @Override
  public List<ConfigurationSummary> getFilteredConfigurationSummaries(String filter)
      throws ServiceUnavailableException {
    try {
      if (StringUtils.hasText(filter)) {
        return configurationSummaryRepository.findByKeyIgnoreCaseContaining(filter);
      } else {
        return configurationSummaryRepository.findAllByOrderByKeyDesc();
      }
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to retrieve the configuration summaries matching the filter (" + filter + ")", e);
    }
  }

  /**
   * Retrieve the filtered configurations.
   *
   * @param filter the filter to apply to the keys for the configurations
   * @return the configurations
   */
  @Override
  public List<Configuration> getFilteredConfigurations(String filter)
      throws ServiceUnavailableException {
    try {
      if (StringUtils.hasText(filter)) {
        return configurationRepository.findByKeyIgnoreCaseContaining(filter);
      } else {
        return configurationRepository.findAllByOrderByKeyDesc();
      }
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to retrieve the configuration matching the filter (" + filter + ")", e);
    }
  }

  /**
   * Retrieve the <b>Integer</b> configuration.
   *
   * @param key the key for the configuration
   * @return the <b>Integer</b> configuration
   */
  @Override
  public Integer getInteger(String key)
      throws InvalidArgumentException, ConfigurationNotFoundException, ServiceUnavailableException {
    if (!StringUtils.hasText(key)) {
      throw new InvalidArgumentException("key");
    }

    try {
      Optional<String> valueOptional = configurationRepository.getValueByKeyIgnoreCase(key);

      if (valueOptional.isPresent()) {
        return Integer.parseInt(valueOptional.get());
      } else {
        throw new ConfigurationNotFoundException(key);
      }
    } catch (ConfigurationNotFoundException e) {
      throw e;
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to retrieve the Integer configuration with the key (" + key + ")", e);
    }
  }

  /**
   * Retrieve the <b>Integer</b> configuration.
   *
   * @param key the key for the configuration
   * @param defaultValue the default value to return if the configuration does not exist
   * @return the <b>Integer</b> configuration or the default value if the configuration entry does
   *     not exist
   */
  @Override
  public int getInteger(String key, int defaultValue)
      throws InvalidArgumentException, ServiceUnavailableException {
    if (!StringUtils.hasText(key)) {
      throw new InvalidArgumentException("key");
    }

    try {
      Optional<String> valueOptional = configurationRepository.getValueByKeyIgnoreCase(key);

      return valueOptional.map(Integer::parseInt).orElse(defaultValue);
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to retrieve the Integer configuration with the key (" + key + ")", e);
    }
  }

  /**
   * Retrieve the <b>Long</b> configuration.
   *
   * @param key the key for the configuration
   * @return the <b>Long</b> configuration
   */
  @Override
  public Long getLong(String key)
      throws InvalidArgumentException, ConfigurationNotFoundException, ServiceUnavailableException {
    if (!StringUtils.hasText(key)) {
      throw new InvalidArgumentException("key");
    }

    try {
      Optional<String> valueOptional = configurationRepository.getValueByKeyIgnoreCase(key);

      if (valueOptional.isPresent()) {
        return Long.parseLong(valueOptional.get());
      } else {
        throw new ConfigurationNotFoundException(key);
      }
    } catch (ConfigurationNotFoundException e) {
      throw e;
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to retrieve the Long configuration with the key (" + key + ")", e);
    }
  }

  /**
   * Retrieve the <b>Long</b> configuration.
   *
   * @param key the key for the configuration
   * @param defaultValue the default value to return if the configuration does not exist
   * @return the <b>Long</b> configuration or the default value if the configuration entry does not
   *     exist
   */
  @Override
  public long getLong(String key, long defaultValue)
      throws InvalidArgumentException, ServiceUnavailableException {
    if (!StringUtils.hasText(key)) {
      throw new InvalidArgumentException("key");
    }

    try {
      Optional<String> valueOptional = configurationRepository.getValueByKeyIgnoreCase(key);

      return valueOptional.map(Long::parseLong).orElse(defaultValue);
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to retrieve the Long configuration with the key (" + key + ")", e);
    }
  }

  /**
   * Retrieve the value for the <b>String</b> configuration.
   *
   * @param key the key for the configuration
   * @return the value for the <b>String</b> configuration
   */
  @Override
  public String getString(String key)
      throws InvalidArgumentException, ConfigurationNotFoundException, ServiceUnavailableException {
    if (!StringUtils.hasText(key)) {
      throw new InvalidArgumentException("key");
    }

    try {
      Optional<String> valueOptional = configurationRepository.getValueByKeyIgnoreCase(key);

      if (valueOptional.isPresent()) {
        return valueOptional.get();
      } else {
        throw new ConfigurationNotFoundException(key);
      }
    } catch (ConfigurationNotFoundException e) {
      throw e;
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to retrieve the String configuration with the key (" + key + ")", e);
    }
  }

  /**
   * Retrieve the value for the <b>String</b> configuration.
   *
   * @param key the key for the configuration
   * @param defaultValue the default value to return if the configuration does not exist
   * @return the value for the <b>String</b> configuration or the default value if the configuration
   *     does not exist
   */
  @Override
  public String getString(String key, String defaultValue)
      throws InvalidArgumentException, ServiceUnavailableException {
    if (!StringUtils.hasText(key)) {
      throw new InvalidArgumentException("key");
    }

    try {
      Optional<String> valueOptional = configurationRepository.getValueByKeyIgnoreCase(key);

      return valueOptional.orElse(defaultValue);
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to retrieve the String configuration with the key (" + key + ")", e);
    }
  }

  /**
   * Check if a configuration with the specified key exists.
   *
   * @param key the key for the configuration
   * @return <b>true</b> if a configuration with the specified key exists or <b>false</b> otherwise
   */
  @Override
  public boolean keyExists(String key)
      throws InvalidArgumentException, ServiceUnavailableException {
    if (!StringUtils.hasText(key)) {
      throw new InvalidArgumentException("key");
    }

    try {
      return configurationRepository.existsByKeyIgnoreCase(key);
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to checked whether the configuration key (" + key + ") exists", e);
    }
  }

  /**
   * Set the configuration.
   *
   * @param configuration the configuration
   */
  @Override
  @Transactional
  public void setConfiguration(Configuration configuration)
      throws InvalidArgumentException, ServiceUnavailableException {
    if (configuration == null) {
      throw new InvalidArgumentException("configuration");
    }

    Set<ConstraintViolation<Configuration>> constraintViolations =
        validator.validate(configuration);

    if (!constraintViolations.isEmpty()) {
      throw new InvalidArgumentException(
          "configuration", ValidationError.toValidationErrors(constraintViolations));
    }

    try {
      configurationRepository.saveAndFlush(configuration);
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to set the configuration with the key (" + configuration.getKey() + ")", e);
    }
  }

  /**
   * Set the configuration key to the specified value.
   *
   * @param key the key for the configuration
   * @param value the value for the configuration
   * @param description the description for the configuration
   */
  @Override
  @Transactional
  public void setConfiguration(String key, Object value, String description)
      throws InvalidArgumentException, ServiceUnavailableException {
    if (!StringUtils.hasText(key)) {
      throw new InvalidArgumentException("key");
    }

    if (value == null) {
      throw new InvalidArgumentException("value");
    }

    if (description == null) {
      throw new InvalidArgumentException("description");
    }

    try {
      String stringValue;

      if (value instanceof String) {
        stringValue = (String) value;
      } else if (value instanceof byte[]) {
        stringValue = Base64Util.encodeBytes((byte[]) value);
      } else {
        stringValue = value.toString();
      }

      configurationRepository.save(new Configuration(key, stringValue, description));
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to set the configuration with the key (" + key + ")", e);
    }
  }
}
