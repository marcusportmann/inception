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

package digital.inception.cache;

import digital.inception.core.CoreConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/** The <b>CacheConfiguration</b> class provides the Spring configuration for the Cache module. */
@Configuration
@Import(CoreConfiguration.class)
public class CacheConfiguration {

  /** The Spring application context. */
  private final ApplicationContext applicationContext;

  /**
   * Constructs a new <b>CacheConfiguration</b>.
   *
   * @param applicationContext the Spring application context
   */
  public CacheConfiguration(ApplicationContext applicationContext) {
    this.applicationContext = applicationContext;
  }
}
