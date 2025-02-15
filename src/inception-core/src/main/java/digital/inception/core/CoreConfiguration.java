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

package digital.inception.core;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;

/** The {@code CoreConfiguration} class provides the Spring configuration for the Core module. */
@Configuration
public class CoreConfiguration {

  /** The Spring {@link ApplicationContext}. */
  private final ApplicationContext applicationContext;

  /**
   * Constructs a new {@code CoreConfiguration}.
   *
   * @param applicationContext the Spring {@link ApplicationContext}
   */
  public CoreConfiguration(ApplicationContext applicationContext) {
    this.applicationContext = applicationContext;
  }
}
