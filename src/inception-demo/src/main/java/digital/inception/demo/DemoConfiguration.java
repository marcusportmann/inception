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

package digital.inception.demo;

import digital.inception.demo.controller.Version;
import io.swagger.v3.oas.models.info.Info;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories;

/**
 * The <b>DemoConfiguration</b> class provides the configuration class for the demo application.
 *
 * @author Marcus Portmann
 */
@Configuration
@EnableJpaRepositories(
    basePackages = {"digital.inception.demo.model", "digital.inception.demo.persistence.jpa"},
    entityManagerFactoryRef = "applicationEntityManagerFactory")
@EnableR2dbcRepositories(
    basePackages = {"digital.inception.demo.model", "digital.inception.demo.persistence.r2dbc"},
    entityOperationsRef = "applicationEntityOperations")
public class DemoConfiguration {

  /** Constructs a new <b>DemoConfiguration</b>. */
  public DemoConfiguration() {}

  /**
   * Returns the grouped Open API for the demo application.
   *
   * @return the grouped Open API for the demo application
   */
  @Bean
  public GroupedOpenApi demoOpenApi() {
    return GroupedOpenApi.builder()
        .group("demo")
        .packagesToScan("digital.inception.demo")
        .addOpenApiCustomizer(
            openApi -> openApi.info(new Info().title("Demo API").version(Version.PROJECT_VERSION)))
        .build();
  }
}
