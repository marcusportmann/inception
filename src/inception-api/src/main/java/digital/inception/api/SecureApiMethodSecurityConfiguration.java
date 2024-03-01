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

package digital.inception.api;

import digital.inception.core.api.IPolicyDecisionPoint;
import java.util.Optional;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.expression.method.MethodSecurityExpressionHandler;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;

/**
 * The <b>SecureApiMethodSecurityConfiguration</b> class applies the secure API method security
 * configuration to the global method security configuration.
 *
 * @author Marcus Portmann
 */
@Configuration
@EnableMethodSecurity
@ConditionalOnClass(name = {"org.springframework.security.web.SecurityFilterChain"})
public class SecureApiMethodSecurityConfiguration {

  /** Constructs a new <b>SecureApiMethodSecurityConfiguration</b>. */
  public SecureApiMethodSecurityConfiguration() {}

  /**
   * Returns the method security expression handler.
   *
   * @param applicationContext the Spring application context
   * @param policyDecisionPoint the optional policy decision point
   * @return the method security expression handler
   */
  @Bean
  static MethodSecurityExpressionHandler methodSecurityExpressionHandler(
      ApplicationContext applicationContext, Optional<IPolicyDecisionPoint> policyDecisionPoint) {
    return new SecureApiMethodSecurityExpressionHandler(applicationContext, policyDecisionPoint);
  }
}
