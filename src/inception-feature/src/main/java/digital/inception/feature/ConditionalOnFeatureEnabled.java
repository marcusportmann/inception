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

package digital.inception.feature;

import java.lang.annotation.*;
import org.springframework.context.annotation.Conditional;

/**
 * Conditional annotation that enables or disables Spring configuration elements based on a boolean
 * feature value resolved from the application {@code Environment}.
 *
 * <h2>Supported targets</h2>
 *
 * This annotation may be placed on:
 *
 * <ul>
 *   <li>{@code @Configuration} classes or any {@code @Component} type ({@link ElementType#TYPE})
 *   <li>{@code @Bean} methods and other annotated factory methods ({@link ElementType#METHOD})
 * </ul>
 *
 * <h2>Lookup strategy (hybrid)</h2>
 *
 * The associated {@link FeatureEnabledCondition} evaluates a feature key using the following
 * precedence order:
 *
 * <ol>
 *   <li><b>Application-scoped</b> property: {@code <spring.application.name>.features.<key>}
 *   <li><b>Top-level</b> property: {@code features.<key>}
 *   <li>If neither property exists, the outcome is controlled by {@link #matchIfMissing()}.
 * </ol>
 *
 * <p>The application name used for the application-scoped prefix is obtained from {@code
 * spring.application.name}. Implementations typically fall back to a default name (for example
 * {@code "application"}) if the property is missing.
 *
 * <h2>Example</h2>
 *
 * <pre>{@code
 * // application.yml
 * spring:
 *   application:
 *     name: inception-demo
 *
 * features:
 *   pdf-export: false
 *
 * inception-demo:
 *   features:
 *     pdf-export: true
 *
 * // Java
 * @Bean
 * @ConditionalOnFeatureEnabled("pdf-export")
 * PdfExportService pdfExportService() {
 *   return new PdfExportService();
 * }
 * }</pre>
 *
 * <p>In the example above, the bean will be created because the application-scoped override {@code
 * inception-demo.features.pdf-export=true} takes precedence over the top-level default.
 *
 * @see FeatureEnabledCondition
 * @author Marcus Portmann
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Conditional(FeatureEnabledCondition.class)
public @interface ConditionalOnFeatureEnabled {

  /**
   * The expected boolean value for this condition to match.
   *
   * <p>By default, the condition matches when the resolved feature value is {@code true}. Setting
   * this to {@code false} inverts the condition, causing it to match when the feature is explicitly
   * disabled.
   *
   * @return {@code true} to match when enabled; {@code false} to match when disabled
   */
  boolean expected() default true;

  /**
   * Controls the condition outcome when the feature key is not configured in either the
   * application-scoped or top-level configuration.
   *
   * <p>When {@code false} (default), a missing key causes the condition to <em>not</em> match,
   * preventing the annotated configuration element from being applied.
   *
   * <p>When {@code true}, a missing key causes the condition to match, which is useful when a
   * feature should be enabled by default and only disabled explicitly.
   *
   * @return {@code true} if missing keys should be treated as a match; {@code false} otherwise
   */
  boolean matchIfMissing() default false;

  /**
   * The feature key to evaluate.
   *
   * <p>The key is resolved as a boolean from the {@code Environment} using the hybrid precedence:
   *
   * <ol>
   *   <li>{@code <spring.application.name>.features.<key>}
   *   <li>{@code features.<key>}
   * </ol>
   *
   * <p>Callers must provide the key only (for example {@code "pdf-export"}); do not include the
   * {@code "features."} prefix.
   *
   * @return the feature key used by {@link FeatureEnabledCondition} to resolve the configured value
   */
  String value();
}
