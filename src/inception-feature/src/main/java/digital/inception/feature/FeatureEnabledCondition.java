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

import java.util.Map;
import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;
import org.springframework.lang.NonNull;

/**
 * {@link Condition} implementation backing {@link ConditionalOnFeatureEnabled}.
 *
 * <h2>Purpose</h2>
 *
 * Evaluates a boolean feature key from the Spring {@code Environment} during configuration parsing
 * to determine whether a {@code @Configuration} class, {@code @Component} type, or {@code @Bean}
 * method annotated with {@link ConditionalOnFeatureEnabled} should be included in the application
 * context.
 *
 * <h2>Hybrid lookup strategy</h2>
 *
 * The condition resolves the configured value for the feature key using the following precedence:
 *
 * <ol>
 *   <li><b>Application-scoped</b> property: {@code <spring.application.name>.features.<key>}
 *   <li><b>Top-level</b> property: {@code features.<key>}
 *   <li>If neither property exists, the condition outcome is determined by {@link
 *       ConditionalOnFeatureEnabled#matchIfMissing()}.
 * </ol>
 *
 * <p>The application name is obtained from {@code spring.application.name} and defaults to {@code
 * "application"} if missing.
 *
 * <h2>Notes</h2>
 *
 * <ul>
 *   <li>This condition reads directly from {@code Environment} and does not depend on any beans.
 *       This makes it safe to execute early during context bootstrap.
 *   <li>If the annotation is not present on the evaluated metadata, this condition returns {@code
 *       true} (i.e., it does not block registration). This behavior is consistent with the
 *       expectation that the condition is only authoritative when explicitly applied.
 * </ul>
 *
 * @see ConditionalOnFeatureEnabled
 * @author Marcus Portmann
 */
public class FeatureEnabledCondition implements Condition {

  /** Constructs a new {@code FeatureEnabledCondition}. */
  public FeatureEnabledCondition() {}

  /**
   * Evaluates whether the annotated type or method should match based on the configured feature
   * value.
   *
   * <p>Evaluation steps:
   *
   * <ol>
   *   <li>Read {@link ConditionalOnFeatureEnabled} attributes from {@code metadata}.
   *   <li>Resolve {@code spring.application.name} (default {@code "application"}).
   *   <li>Attempt to read {@code <appName>.features.<key>} as {@link Boolean}. If present, compare
   *       to {@code expected}.
   *   <li>Otherwise attempt to read {@code features.<key>} as {@link Boolean}. If present, compare
   *       to {@code expected}.
   *   <li>If missing in both locations, return {@code matchIfMissing}.
   * </ol>
   *
   * @param context the condition context providing access to the {@code Environment} and other
   *     infrastructure
   * @param metadata metadata describing the annotated type or method being evaluated
   * @return {@code true} if the condition matches and the annotated element should be included;
   *     {@code false} otherwise
   */
  @Override
  public boolean matches(@NonNull ConditionContext context, AnnotatedTypeMetadata metadata) {
    Map<String, Object> attrs =
        metadata.getAnnotationAttributes(ConditionalOnFeatureEnabled.class.getName());
    if (attrs == null) return true;

    String key = (String) attrs.get("value");
    boolean expected = (boolean) attrs.get("expected");
    boolean matchIfMissing = (boolean) attrs.get("matchIfMissing");

    String appName = context.getEnvironment().getProperty("spring.application.name", "application");

    String appPath = appName + ".features." + key;
    Boolean appValue = context.getEnvironment().getProperty(appPath, Boolean.class);
    if (appValue != null) return appValue == expected;

    String globalPath = "features." + key;
    Boolean globalValue = context.getEnvironment().getProperty(globalPath, Boolean.class);
    if (globalValue != null) return globalValue == expected;

    return matchIfMissing;
  }
}
