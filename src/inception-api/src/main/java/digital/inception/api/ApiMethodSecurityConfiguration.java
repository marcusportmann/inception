/*
 * Copyright 2022 Marcus Portmann
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

package digital.inception.api;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.GlobalMethodSecurityConfiguration;

/**
 * The <b>ApiMethodSecurityConfiguration</b> class applies the API-specific method security
 * configuration to the global method security configuration.
 *
 * @author Marcus Portmann
 */
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class ApiMethodSecurityConfiguration extends GlobalMethodSecurityConfiguration
    implements ApplicationContextAware {

  /** The Spring application context. */
  private ApplicationContext applicationContext;

  /** Constructs a new <b>ApiMethodSecurityConfiguration</b>. */
  public ApiMethodSecurityConfiguration() {}

  @Override
  public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
    this.applicationContext = applicationContext;
  }

  @Override
  protected ApiMethodSecurityExpressionHandler createExpressionHandler() {
    return new ApiMethodSecurityExpressionHandler(applicationContext);
  }
}
