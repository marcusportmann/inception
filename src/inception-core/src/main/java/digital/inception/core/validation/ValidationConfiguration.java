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

package digital.inception.core.validation;

import org.hibernate.validator.internal.engine.ConfigurationImpl;
import org.hibernate.validator.messageinterpolation.ExpressionLanguageFeatureLevel;
import org.springframework.beans.BeanUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

/**
 * The {@code ValidationConfiguration} class provides access to the JSR-380 validation configuration
 * and initialises the JSR-380 validator.
 *
 * @author Marcus Portmann
 */
@Configuration
public class ValidationConfiguration {

  /** Creates a new {@code ValidationConfiguration} instance. */
  public ValidationConfiguration() {}

  /**
   * Returns the local validator factory bean.
   *
   * @return the local validator factory bean
   */
  @Bean
  public static LocalValidatorFactoryBean validator() {
    LocalValidatorFactoryBean factoryBean = new LocalValidatorFactoryBean();

    factoryBean.setMessageInterpolator(
        BeanUtils.instantiateClass(ValidationMessageInterpolator.class));

    factoryBean.setConfigurationInitializer(
        (jakarta.validation.Configuration<?> configuration) -> {
          if (configuration instanceof ConfigurationImpl hibernateConfiguration) {
            hibernateConfiguration.customViolationExpressionLanguageFeatureLevel(
                ExpressionLanguageFeatureLevel.DEFAULT);
            hibernateConfiguration.constraintExpressionLanguageFeatureLevel(
                ExpressionLanguageFeatureLevel.DEFAULT);
          }
        });
    return factoryBean;
  }
}
