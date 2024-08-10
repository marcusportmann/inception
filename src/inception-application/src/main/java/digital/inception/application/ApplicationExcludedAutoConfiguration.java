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

package digital.inception.application;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Configuration;

/**
 * The <b>ApplicationExcludedAutoConfiguration</b> class disables specific Spring Boot
 * auto-configuration.
 *
 * @author Marcus Portmann
 */
@Configuration
@EnableAutoConfiguration(
    excludeName = {
      "org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration",
      "org.springframework.boot.autoconfigure.liquibase.LiquibaseAutoConfiguration"
    })
public class ApplicationExcludedAutoConfiguration {

  /** Constructs a new <b>ApplicationExcludedAutoConfiguration</b>. */
  public ApplicationExcludedAutoConfiguration() {}
}
