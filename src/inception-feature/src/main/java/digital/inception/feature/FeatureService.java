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

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.EnumerablePropertySource;
import org.springframework.core.env.Environment;
import org.springframework.core.env.PropertySource;
import org.springframework.stereotype.Component;

/**
 * The {@code FeatureService} provides capabilities for evaluating boolean feature configuration.
 *
 * <h2>Configuration model</h2>
 *
 * Features may be defined in two locations within the Spring {@code Environment}:
 *
 * <ol>
 *   <li><b>Application-scoped</b> (the highest precedence): {@code
 *       <spring.application-name>.features.<key>}
 *   <li><b>Top-level</b> defaults: {@code features.<key>}
 * </ol>
 *
 * <p>The application name used for application-scoped lookups is taken from {@code
 * spring.application.name}. Implementations should treat absent or blank application names
 * consistently (for example, using a sensible default such as {@code "application"}).
 *
 * <h2>Lookup and precedence</h2>
 *
 * When evaluating a feature key, the service resolves the value in the following order:
 *
 * <ol>
 *   <li>If {@code <application-name>.features.<key>} is present and can be converted to {@link
 *       Boolean}, that value is returned.
 *   <li>Otherwise, if {@code features.<key>} is present and can be converted to {@link Boolean},
 *       that value is returned.
 *   <li>Otherwise, the configured fallback (either {@code false} or the provided default) is
 *       returned.
 * </ol>
 *
 * <p>Keys are expected to match the property names under {@code features} (e.g. {@code
 * "pdf-export"}, {@code "demo-feature-1-enabled"}). No {@code "features."} prefix should be
 * included when calling these methods.
 *
 * <h2>Usage</h2>
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
 * if (featureService.isEnabled("pdf-export")) {
 *   // enabled for inception-demo
 * }
 * }</pre>
 *
 * @author Marcus Portmann
 */
@Component
@SuppressWarnings("unused")
public class FeatureService {

  private final String applicationName;

  private final Environment environment;

  /**
   * Constructs a new {@code FeatureService}.
   *
   * @param environment the Spring {@link Environment}
   */
  public FeatureService(Environment environment) {
    this.environment = environment;
    this.applicationName = environment.getProperty("spring.application.name", "application");
  }

  /**
   * Returns a merged view of all configured features as boolean values.
   *
   * <p>The returned map represents the effective configuration after applying the hybrid precedence
   * rules:
   *
   * <ul>
   *   <li>All top-level features under {@code features} are included first.
   *   <li>Any application-scoped features under {@code <application-name>.features} override
   *       entries with the same key.
   * </ul>
   *
   * <p>The map keys correspond to the feature keys beneath {@code features} (e.g. {@code
   * "pdf-export"}), not full property paths.
   *
   * <p>Implementations may return an empty map when no features are configured. Implementations
   * should also document whether the returned map is mutable or unmodifiable and whether iteration
   * order is stable.
   *
   * @return a merged map of configured feature keys to their resolved boolean values; never {@code
   *     null}
   */
  public Map<String, Boolean> all() {
    Map<String, Boolean> merged = new LinkedHashMap<>();

    // top-level first
    merged.putAll(extractBooleanMap(globalPrefix() + "."));

    // app-scoped overrides
    merged.putAll(extractBooleanMap(appPrefix() + "."));

    return Collections.unmodifiableMap(merged);
  }

  /**
   * Returns the resolved application name used for application-scoped feature lookups.
   *
   * <p>This value is typically sourced from {@code spring.application.name} and is used to build
   * the application-scoped prefix {@code <application-name>.features}.
   *
   * <p>Implementations should define how missing or blank application names are handled (for
   * example, returning a default such as {@code "application"}).
   *
   * @return the resolved application name used for application-scoped lookups; never {@code null}
   */
  public String applicationName() {
    return applicationName;
  }

  /**
   * Determines whether the given feature is enabled using the hybrid lookup strategy.
   *
   * <p>Resolution order:
   *
   * <ol>
   *   <li>{@code <application-name>.features.<key>}
   *   <li>{@code features.<key>}
   *   <li>Fallback to {@code false}
   * </ol>
   *
   * @param key the feature key (without the {@code "features."} prefix); must not be {@code null}
   * @return {@code true} if the resolved feature value is {@code true}; {@code false} if the
   *     resolved value is {@code false} or if the feature is not configured in either location
   * @throws IllegalArgumentException if {@code key} is blank, when enforced by an implementation
   */
  public boolean isEnabled(String key) {
    return isEnabled(key, false);
  }

  /**
   * Determines whether the given feature is enabled using the hybrid lookup strategy, falling back
   * to the supplied default value when the feature is not configured in either location.
   *
   * <p>Resolution order:
   *
   * <ol>
   *   <li>{@code <application-name>.features.<key>}
   *   <li>{@code features.<key>}
   *   <li>Fallback to {@code defaultValue}
   * </ol>
   *
   * <p>This method is useful when introducing a new feature key gradually, and you want a
   * predictable behavior across environments where the key may not yet exist.
   *
   * @param key the feature key (without the {@code "features."} prefix); must not be {@code null}
   * @param defaultValue the value to return if the feature key is not present in either the
   *     application-scoped or top-level configuration
   * @return the resolved value for the feature key, or {@code defaultValue} when missing in both
   *     locations
   * @throws IllegalArgumentException if {@code key} is blank, when enforced by an implementation
   */
  public boolean isEnabled(String key, boolean defaultValue) {
    Boolean appValue = environment.getProperty(appPrefix() + "." + key, Boolean.class);
    if (appValue != null) return appValue;

    Boolean globalValue = environment.getProperty(globalPrefix() + "." + key, Boolean.class);
    if (globalValue != null) return globalValue;

    return defaultValue;
  }

  /**
   * Returns the first existing property path that provides the effective value for the given
   * feature key, according to the hybrid lookup strategy.
   *
   * <p>Resolution order:
   *
   * <ol>
   *   <li>{@code <application-name>.features.<key>} (if present)
   *   <li>{@code features.<key>} (if present)
   * </ol>
   *
   * <p>This method is intended for diagnostics and observability (for example, logging or actuator
   * endpoints) to explain <em>where</em> a feature's value was sourced from.
   *
   * @param key the feature key (without the {@code "features."} prefix); must not be {@code null}
   * @return an {@link Optional} containing the resolved property path if the feature key exists in
   *     either location; otherwise {@link Optional#empty()}
   * @throws IllegalArgumentException if {@code key} is blank, when enforced by an implementation
   */
  public Optional<String> resolvedPropertyPath(String key) {
    String appPath = appPrefix() + "." + key;
    if (containsProperty(appPath)) return Optional.of(appPath);

    String globalPath = globalPrefix() + "." + key;
    if (containsProperty(globalPath)) return Optional.of(globalPath);

    return Optional.empty();
  }

  private String appPrefix() {
    return applicationName + ".features";
  }

  private boolean containsProperty(String propertyName) {
    // Environment has containsProperty only on ConfigurableEnvironment; otherwise just attempt
    // resolution
    if (environment instanceof ConfigurableEnvironment ce) {
      return ce.containsProperty(propertyName);
    }
    return environment.getProperty(propertyName) != null;
  }

  private Map<String, Boolean> extractBooleanMap(String prefixWithDot) {
    if (!(environment instanceof ConfigurableEnvironment ce)) {
      // Can't enumerate; return empty (or you could throw if you require enumeration support)
      return Collections.emptyMap();
    }

    Map<String, Boolean> result = new LinkedHashMap<>();

    for (PropertySource<?> ps : ce.getPropertySources()) {
      if (ps instanceof EnumerablePropertySource<?> eps) {
        for (String name : eps.getPropertyNames()) {
          if (name.startsWith(prefixWithDot)) {
            String key = name.substring(prefixWithDot.length());
            Boolean value = environment.getProperty(name, Boolean.class);
            if (value != null) {
              result.put(key, value);
            }
          }
        }
      }
    }
    return result;
  }

  private String globalPrefix() {
    return "features";
  }
}
