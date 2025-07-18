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

package digital.inception.config.controller;

import digital.inception.api.SecureApiController;
import digital.inception.config.exception.ConfigNotFoundException;
import digital.inception.config.model.Config;
import digital.inception.config.model.ConfigSummary;
import digital.inception.config.service.ConfigService;
import digital.inception.core.api.ApiUtil;
import digital.inception.core.exception.InvalidArgumentException;
import digital.inception.core.exception.ServiceUnavailableException;
import java.util.List;
import org.springframework.context.ApplicationContext;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;

/**
 * The {@code ConfigApiControllerImpl} class.
 *
 * @author Marcus Portmann
 */
@RestController
@CrossOrigin
@SuppressWarnings({"unused"})
public class ConfigApiControllerImpl extends SecureApiController implements ConfigApiController {

  /** The Config Service. */
  private final ConfigService configService;

  /**
   * Constructs a new {@code ConfigApiControllerImpl}.
   *
   * @param applicationContext the Spring application context
   * @param configService the Config Service
   */
  public ConfigApiControllerImpl(
      ApplicationContext applicationContext, ConfigService configService) {
    super(applicationContext);

    this.configService = configService;
  }

  @Override
  public void deleteConfig(String id)
      throws InvalidArgumentException, ConfigNotFoundException, ServiceUnavailableException {
    configService.deleteConfig(id);
  }

  @Override
  public Config getConfig(String id)
      throws InvalidArgumentException, ConfigNotFoundException, ServiceUnavailableException {
    return configService.getConfig(id);
  }

  @Override
  public List<ConfigSummary> getConfigSummaries() throws ServiceUnavailableException {
    return configService.getConfigSummaries();
  }

  @Override
  public String getConfigValue(String id)
      throws InvalidArgumentException, ConfigNotFoundException, ServiceUnavailableException {
    return ApiUtil.quote(configService.getString(id));
  }

  @Override
  public List<Config> getConfigs() throws ServiceUnavailableException {
    return configService.getConfigs();
  }

  @Override
  public void setConfig(Config config)
      throws InvalidArgumentException, ServiceUnavailableException {
    configService.setConfig(config);
  }
}
