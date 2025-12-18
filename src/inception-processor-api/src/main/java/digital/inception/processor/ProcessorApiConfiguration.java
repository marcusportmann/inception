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

package digital.inception.processor;

import digital.inception.Version;
import io.swagger.v3.oas.models.info.Info;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * The {@code ProcessorApiConfiguration} class provides the Spring configuration for the Processor
 * API module.
 *
 * @author Marcus Portmann
 */
@Configuration
public class ProcessorApiConfiguration {

  /** Constructs a new {@code ProcessorApiConfiguration}. */
  public ProcessorApiConfiguration() {}

  /**
   * Returns the grouped Open API for the Processor API.
   *
   * @return the grouped Open API for the Processor API
   */
  @Bean
  public GroupedOpenApi processorOpenApi() {
    return GroupedOpenApi.builder()
        .group("processor")
        .packagesToScan("digital.inception.processor")
        .addOpenApiCustomizer(
            openApi ->
                openApi.info(
                    new Info().title("Processor API").version(Version.INCEPTION_FRAMEWORK_VERSION)))
        .build();
  }
}
