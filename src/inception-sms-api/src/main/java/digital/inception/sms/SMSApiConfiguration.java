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

package digital.inception.sms;

import digital.inception.Version;
import io.swagger.v3.oas.models.info.Info;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * The {@code SMSApiConfiguration} class provides the Spring configuration for the SMS API module.
 *
 * @author Marcus Portmann
 */
@Configuration
public class SMSApiConfiguration {

  /** Constructs a new {@code SMSApiConfiguration}. */
  public SMSApiConfiguration() {}

  /**
   * Returns the grouped Open API for the SMS API.
   *
   * @return the grouped Open API for the SMS API
   */
  @Bean
  public GroupedOpenApi smsOpenApi() {
    GroupedOpenApi build =
        GroupedOpenApi.builder()
            .group("sms")
            .packagesToScan("digital.inception.sms")
            .addOpenApiCustomizer(
                openApi ->
                    openApi.info(
                        new Info().title("SMS API").version(Version.INCEPTION_FRAMEWORK_VERSION)))
            .build();
    return build;
  }
}
