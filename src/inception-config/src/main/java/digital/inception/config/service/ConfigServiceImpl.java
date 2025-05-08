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
import digital.inception.config.persistence.jpa.ConfigRepository;
import digital.inception.config.persistence.jpa.ConfigSummaryRepository;
import digital.inception.core.service.AbstractServiceBase;
import digital.inception.core.service.InvalidArgumentException;
import digital.inception.core.service.ServiceUnavailableException;
import java.util.Base64;
import java.util.List;
import java.util.Optional;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * The {@code ConfigServiceImpl} class provides the Config Service implementation.
 *
 * @author Marcus Portmann
 */
@Service
@SuppressWarnings("unused")
public class ConfigServiceImpl extends AbstractServiceBase implements ConfigService {

  /** The Config Repository. */
  private final ConfigRepository configRepository;

  /** The Config Summary Repository. */
  private final ConfigSummaryRepository configSummaryRepository;

  /**
   * Creates a new {@code ConfigServiceImpl} instance.
   *
   * @param applicationContext the Spring application context
   * @param configRepository the Config Repository
   * @param configSummaryRepository the Config Summary Repository
   */
  public ConfigServiceImpl(
      ApplicationContext applicationContext,
      ConfigRepository configRepository,
      ConfigSummaryRepository configSummaryRepository) {
    super(applicationContext);

    this.configRepository = configRepository;
    this.configSummaryRepository = configSummaryRepository;
  }

  @Override
  public void deleteConfig(String id)
      throws InvalidArgumentException, ConfigNotFoundException, ServiceUnavailableException {
    if (!StringUtils.hasText(id)) {
      throw new InvalidArgumentException("id");
    }

    try {
      if (!configRepository.existsByIdIgnoreCase(id)) {
        throw new ConfigNotFoundException(id);
      }

      configRepository.deleteByIdIgnoreCase(id);
    } catch (ConfigNotFoundException e) {
      throw e;
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to delete the config with the ID (" + id + ")", e);
    }
  }

  @Override
  public byte[] getBinary(String id)
      throws InvalidArgumentException, ConfigNotFoundException, ServiceUnavailableException {
    if (!StringUtils.hasText(id)) {
      throw new InvalidArgumentException("id");
    }

    try {
      Optional<String> valueOptional = configRepository.getValueByIdIgnoreCase(id);

      if (valueOptional.isPresent()) {
        return Base64.getDecoder().decode(valueOptional.get());
      } else {
        throw new ConfigNotFoundException(id);
      }
    } catch (ConfigNotFoundException e) {
      throw e;
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to retrieve the binary config with the ID (" + id + ")", e);
    }
  }

  @Override
  public byte[] getBinary(String id, byte[] defaultValue)
      throws InvalidArgumentException, ServiceUnavailableException {
    if (!StringUtils.hasText(id)) {
      throw new InvalidArgumentException("id");
    }

    try {
      Optional<String> valueOptional = configRepository.getValueByIdIgnoreCase(id);

      return valueOptional.map(Base64.getDecoder()::decode).orElse(defaultValue);
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to retrieve the binary config with the ID (" + id + ")", e);
    }
  }

  @Override
  public boolean getBoolean(String id)
      throws InvalidArgumentException, ConfigNotFoundException, ServiceUnavailableException {
    if (!StringUtils.hasText(id)) {
      throw new InvalidArgumentException("id");
    }

    try {
      Optional<String> valueOptional = configRepository.getValueByIdIgnoreCase(id);

      if (valueOptional.isPresent()) {
        return Boolean.parseBoolean(valueOptional.get());
      } else {
        throw new ConfigNotFoundException(id);
      }
    } catch (ConfigNotFoundException e) {
      throw e;
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to retrieve the Boolean config with the ID (" + id + ")", e);
    }
  }

  @Override
  public boolean getBoolean(String id, boolean defaultValue)
      throws InvalidArgumentException, ServiceUnavailableException {
    if (!StringUtils.hasText(id)) {
      throw new InvalidArgumentException("id");
    }

    try {
      Optional<String> valueOptional = configRepository.getValueByIdIgnoreCase(id);

      return valueOptional.map(Boolean::parseBoolean).orElse(defaultValue);
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to retrieve the Boolean config with the ID (" + id + ")", e);
    }
  }

  @Override
  public Config getConfig(String id)
      throws InvalidArgumentException, ConfigNotFoundException, ServiceUnavailableException {
    if (!StringUtils.hasText(id)) {
      throw new InvalidArgumentException("id");
    }

    try {
      Optional<Config> configOptional = configRepository.findByIdIgnoreCase(id);

      if (configOptional.isEmpty()) {
        throw new ConfigNotFoundException(id);
      } else {
        return configOptional.get();
      }
    } catch (ConfigNotFoundException e) {
      throw e;
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to retrieve the config with the ID (" + id + ")", e);
    }
  }

  @Override
  public List<ConfigSummary> getConfigSummaries() throws ServiceUnavailableException {
    try {
      return configSummaryRepository.findAllByOrderByIdAsc();
    } catch (Throwable e) {
      throw new ServiceUnavailableException("Failed to retrieve the config summaries", e);
    }
  }

  @Override
  public List<Config> getConfigs() throws ServiceUnavailableException {
    try {
      return configRepository.findAllByOrderByIdAsc();
    } catch (Throwable e) {
      throw new ServiceUnavailableException("Failed to retrieve the configs", e);
    }
  }

  @Override
  public Double getDouble(String id)
      throws InvalidArgumentException, ConfigNotFoundException, ServiceUnavailableException {
    if (!StringUtils.hasText(id)) {
      throw new InvalidArgumentException("id");
    }

    try {
      Optional<String> valueOptional = configRepository.getValueByIdIgnoreCase(id);

      if (valueOptional.isPresent()) {
        return Double.parseDouble(valueOptional.get());
      } else {
        throw new ConfigNotFoundException(id);
      }
    } catch (ConfigNotFoundException e) {
      throw e;
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to retrieve the Double config with the ID (" + id + ")", e);
    }
  }

  @Override
  public double getDouble(String id, double defaultValue)
      throws InvalidArgumentException, ServiceUnavailableException {
    if (!StringUtils.hasText(id)) {
      throw new InvalidArgumentException("id");
    }

    try {
      Optional<String> valueOptional = configRepository.getValueByIdIgnoreCase(id);

      return valueOptional.map(Double::parseDouble).orElse(defaultValue);
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to retrieve the Double config with the ID (" + id + ")", e);
    }
  }

  @Override
  public List<ConfigSummary> getFilteredConfigSummaries(String filter)
      throws ServiceUnavailableException {
    try {
      if (StringUtils.hasText(filter)) {
        return configSummaryRepository.findByIdIgnoreCaseContaining(filter);
      } else {
        return configSummaryRepository.findAllByOrderByIdAsc();
      }
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to retrieve the config summaries matching the filter (" + filter + ")", e);
    }
  }

  @Override
  public List<Config> getFilteredConfigs(String filter) throws ServiceUnavailableException {
    try {
      if (StringUtils.hasText(filter)) {
        return configRepository.findByIdIgnoreCaseContaining(filter);
      } else {
        return configRepository.findAllByOrderByIdAsc();
      }
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to retrieve the config matching the filter (" + filter + ")", e);
    }
  }

  @Override
  public Integer getInteger(String id)
      throws InvalidArgumentException, ConfigNotFoundException, ServiceUnavailableException {
    if (!StringUtils.hasText(id)) {
      throw new InvalidArgumentException("id");
    }

    try {
      Optional<String> valueOptional = configRepository.getValueByIdIgnoreCase(id);

      if (valueOptional.isPresent()) {
        return Integer.parseInt(valueOptional.get());
      } else {
        throw new ConfigNotFoundException(id);
      }
    } catch (ConfigNotFoundException e) {
      throw e;
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to retrieve the Integer config with the ID (" + id + ")", e);
    }
  }

  @Override
  public int getInteger(String id, int defaultValue)
      throws InvalidArgumentException, ServiceUnavailableException {
    if (!StringUtils.hasText(id)) {
      throw new InvalidArgumentException("id");
    }

    try {
      Optional<String> valueOptional = configRepository.getValueByIdIgnoreCase(id);

      return valueOptional.map(Integer::parseInt).orElse(defaultValue);
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to retrieve the Integer config with the ID (" + id + ")", e);
    }
  }

  @Override
  public Long getLong(String id)
      throws InvalidArgumentException, ConfigNotFoundException, ServiceUnavailableException {
    if (!StringUtils.hasText(id)) {
      throw new InvalidArgumentException("id");
    }

    try {
      Optional<String> valueOptional = configRepository.getValueByIdIgnoreCase(id);

      if (valueOptional.isPresent()) {
        return Long.parseLong(valueOptional.get());
      } else {
        throw new ConfigNotFoundException(id);
      }
    } catch (ConfigNotFoundException e) {
      throw e;
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to retrieve the Long config with the ID (" + id + ")", e);
    }
  }

  @Override
  public long getLong(String id, long defaultValue)
      throws InvalidArgumentException, ServiceUnavailableException {
    if (!StringUtils.hasText(id)) {
      throw new InvalidArgumentException("id");
    }

    try {
      Optional<String> valueOptional = configRepository.getValueByIdIgnoreCase(id);

      return valueOptional.map(Long::parseLong).orElse(defaultValue);
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to retrieve the Long config with the ID (" + id + ")", e);
    }
  }

  @Override
  public String getString(String id)
      throws InvalidArgumentException, ConfigNotFoundException, ServiceUnavailableException {
    if (!StringUtils.hasText(id)) {
      throw new InvalidArgumentException("id");
    }

    try {
      Optional<String> valueOptional = configRepository.getValueByIdIgnoreCase(id);

      if (valueOptional.isPresent()) {
        return valueOptional.get();
      } else {
        throw new ConfigNotFoundException(id);
      }
    } catch (ConfigNotFoundException e) {
      throw e;
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to retrieve the String config with the ID (" + id + ")", e);
    }
  }

  @Override
  public String getString(String id, String defaultValue)
      throws InvalidArgumentException, ServiceUnavailableException {
    if (!StringUtils.hasText(id)) {
      throw new InvalidArgumentException("id");
    }

    try {
      Optional<String> valueOptional = configRepository.getValueByIdIgnoreCase(id);

      return valueOptional.orElse(defaultValue);
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to retrieve the String config with the ID (" + id + ")", e);
    }
  }

  @Override
  public boolean idExists(String id) throws InvalidArgumentException, ServiceUnavailableException {
    if (!StringUtils.hasText(id)) {
      throw new InvalidArgumentException("id");
    }

    try {
      return configRepository.existsByIdIgnoreCase(id);
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to checked whether the config ID (" + id + ") exists", e);
    }
  }

  @Override
  public void setConfig(Config config)
      throws InvalidArgumentException, ServiceUnavailableException {
    validateArgument("config", config);

    try {
      configRepository.saveAndFlush(config);
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to set the config with the ID (" + config.getId() + ")", e);
    }
  }

  @Override
  public void setConfig(String id, Object value, String description)
      throws InvalidArgumentException, ServiceUnavailableException {
    if (!StringUtils.hasText(id)) {
      throw new InvalidArgumentException("id");
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
        stringValue = Base64.getEncoder().encodeToString((byte[]) value);
      } else {
        stringValue = value.toString();
      }

      configRepository.save(new Config(id, stringValue, description));
    } catch (Throwable e) {
      throw new ServiceUnavailableException("Failed to set the config with the ID (" + id + ")", e);
    }
  }
}
