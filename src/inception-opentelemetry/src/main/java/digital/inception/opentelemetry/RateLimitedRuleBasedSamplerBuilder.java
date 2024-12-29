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

package digital.inception.opentelemetry;

import io.opentelemetry.api.common.AttributeKey;
import io.opentelemetry.api.trace.SpanKind;
import io.opentelemetry.sdk.trace.samplers.Sampler;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * The <b>RateLimitedRuleBasedSamplerBuilder</b> provides a builder for {@link
 * RateLimitedRuleBasedSampler}. Allows configuration of sampling rules based on span attributes and
 * patterns.
 *
 * <p>This is based on the <b>io.opentelemetry.contrib.sampler.RuleBasedSamplerBuilder</b> class,
 * which forms part of the OpenTelemetry <b>opentelemetry-java-contrib</b> project.
 *
 * <p>This is based on the <b>io.opentelemetry.contrib.sampler.RuleBasedSamplerBuilder</b> class,
 * which forms part of the OpenTelemetry <b>opentelemetry-java-contrib</b> project.
 *
 * @author Marcus Portmann
 */
public final class RateLimitedRuleBasedSamplerBuilder {

  private final boolean enableRuleLogging;

  private final Sampler fallbackSampler;

  private final List<SamplingRule> rules = new ArrayList<>();

  private final long spansPerSecondLimit;

  /**
   * Constructs a new <b>RuleBasedSamplerBuilder</b>.
   *
   * @param enableRuleLogging enable additional logging during the processing of rules
   * @param spansPerSecondLimit the spans per second limit
   * @param fallbackSampler the fallback sampler to use when no rules match
   * @throws NullPointerException if any of the parameters are <b>null</b>
   */
  public RateLimitedRuleBasedSamplerBuilder(
      boolean enableRuleLogging, long spansPerSecondLimit, Sampler fallbackSampler) {
    this.enableRuleLogging = enableRuleLogging;
    this.spansPerSecondLimit = spansPerSecondLimit;
    this.fallbackSampler =
        Objects.requireNonNull(fallbackSampler, "Fallback sampler must not be null");
  }

  /**
   * Builds the {@link RateLimitedRuleBasedSampler} based on the configured rules.
   *
   * @return a new RuleBasedSamplerBuilder instance
   */
  public RateLimitedRuleBasedSampler build() {
    return new RateLimitedRuleBasedSampler(
        enableRuleLogging, spansPerSecondLimit, fallbackSampler, rules);
  }

  /**
   * Adds a custom sampling rule that uses the provided sampler when the value of the provided
   * {@link AttributeKey} matches the provided pattern.
   *
   * @param spanKind the span kind to match against
   * @param attributeKey the attribute key to match against
   * @param pattern the regex pattern to apply
   * @param sampler the sampler to delegate to upon a successful match
   * @return this builder for chaining
   * @throws NullPointerException if any of the parameters are <b>null</b>>
   */
  public RateLimitedRuleBasedSamplerBuilder customize(
      SpanKind spanKind, AttributeKey<String> attributeKey, String pattern, Sampler sampler) {
    rules.add(
        new SamplingRule(
            Objects.requireNonNull(spanKind, "SpanKind must not be null"),
            Objects.requireNonNull(attributeKey, "AttributeKey must not be null"),
            Objects.requireNonNull(pattern, "Pattern must not be null"),
            Objects.requireNonNull(sampler, "Sampler must not be null")));
    return this;
  }

  /**
   * Adds a rule to drop all spans when the value of the provided {@link AttributeKey} matches the
   * provided pattern.
   *
   * @param spanKind the span kind to match against
   * @param attributeKey the attribute key to match against
   * @param pattern the regex pattern to apply
   * @return this builder for chaining
   * @throws NullPointerException if any of the parameters are <b>null</b>>
   */
  public RateLimitedRuleBasedSamplerBuilder drop(
      SpanKind spanKind, AttributeKey<String> attributeKey, String pattern) {
    return customize(spanKind, attributeKey, pattern, Sampler.alwaysOff());
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof RateLimitedRuleBasedSamplerBuilder that)) return false;
    return enableRuleLogging == that.enableRuleLogging
        && spansPerSecondLimit == that.spansPerSecondLimit
        && rules.equals(that.rules)
        && fallbackSampler.equals(that.fallbackSampler);
  }

  @Override
  public int hashCode() {
    return Objects.hash(enableRuleLogging, spansPerSecondLimit, rules, fallbackSampler);
  }

  /**
   * Adds a rule to record and sample all spans when the value of the provided {@link AttributeKey}
   * matches the provided pattern.
   *
   * @param spanKind the span kind to match against
   * @param attributeKey the attribute key to match against
   * @param pattern the regex pattern to apply
   * @return this builder for chaining
   * @throws NullPointerException if any of the parameters are <b>null</b>>
   */
  public RateLimitedRuleBasedSamplerBuilder recordAndSample(
      SpanKind spanKind, AttributeKey<String> attributeKey, String pattern) {
    return customize(spanKind, attributeKey, pattern, Sampler.alwaysOn());
  }

  @Override
  public String toString() {
    return "RuleBasedSamplerBuilder{"
        + "enableRuleLogging="
        + enableRuleLogging
        + "spansPerSecondLimit="
        + spansPerSecondLimit
        + ", rules="
        + rules
        + ", fallbackSampler="
        + fallbackSampler
        + '}';
  }
}
