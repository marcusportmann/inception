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

import digital.inception.core.api.ApiUtil;
import digital.inception.api.SecureApiController;
import digital.inception.config.model.Config;
import digital.inception.config.model.ConfigNotFoundException;
import digital.inception.config.model.ConfigSummary;
import digital.inception.config.service.IConfigService;
import digital.inception.core.service.InvalidArgumentException;
import digital.inception.core.service.ServiceUnavailableException;
import java.util.List;
import org.springframework.context.ApplicationContext;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;

/**
 * The <b>ConfigApiController</b> class.
 *
 * @author Marcus Portmann
 */
@RestController
@CrossOrigin
@SuppressWarnings({"unused"})
public class ConfigApiController extends SecureApiController implements IConfigApiController {

  /** The Config Service. */
  private final IConfigService configService;

  /**
   * Constructs a new <b>ConfigApiController</b>.
   *
   * @param applicationContext the Spring application context
   * @param configService the Config Service
   */
  public ConfigApiController(ApplicationContext applicationContext, IConfigService configService) {
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
