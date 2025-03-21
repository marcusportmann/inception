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
import io.opentelemetry.api.incubator.config.DeclarativeConfigProperties;
import io.opentelemetry.api.trace.SpanKind;
import io.opentelemetry.sdk.autoconfigure.spi.ConfigurationException;
import io.opentelemetry.sdk.autoconfigure.spi.internal.ComponentProvider;
import io.opentelemetry.sdk.extension.incubator.fileconfig.DeclarativeConfiguration;
import io.opentelemetry.sdk.trace.samplers.Sampler;
import java.util.List;

/**
 * The <b>RateLimitedRuleBasedSamplerComponentProvider</b> class provides a declarative
 * configuration SPI implementation for {@link RateLimitedRuleBasedSampler}.
 *
 * <p>This class is internal and is hence not for public use. Its APIs are unstable and can change
 * at any time.
 */
public class RateLimitedRuleBasedSamplerComponentProvider implements ComponentProvider<Sampler> {

  private static final String ACTION_DROP = "DROP";

  private static final String ACTION_RECORD_AND_SAMPLE = "RECORD_AND_SAMPLE";

  /** Constructs a new <b>RateLimitedRuleBasedSamplerComponentProvider</b>. */
  public RateLimitedRuleBasedSamplerComponentProvider() {}

  @Override
  public Sampler create(DeclarativeConfigProperties config) {
    boolean enableRuleLogging = config.getBoolean("enable_rule_logging", false);

    DeclarativeConfigProperties fallbackSamplerString = config.getStructured("fallback_sampler");
    if (fallbackSamplerString == null) {
      throw new ConfigurationException(
          "rate_limited_rule_based sampler .fallback is required but is null");
    }
    Sampler fallbackSampler;
    try {
      fallbackSampler = DeclarativeConfiguration.createSampler(fallbackSamplerString);
    } catch (ConfigurationException e) {
      throw new ConfigurationException(
          "rule_Based_routing sampler failed to create .fallback sampler", e);
    }

    long spansPerSecondLimit = config.getLong("spans_per_second_limit", 1);

    RateLimitedRuleBasedSamplerBuilder builder =
        RateLimitedRuleBasedSampler.builder(
            enableRuleLogging, spansPerSecondLimit, fallbackSampler);

    List<DeclarativeConfigProperties> rules = config.getStructuredList("rules");
    if (rules == null || rules.isEmpty()) {
      throw new ConfigurationException("rate_limited_rule_based sampler .rules is required");
    }

    for (DeclarativeConfigProperties rule : rules) {
      String spanKindString = rule.getString("span_kind");
      if (spanKindString == null) {
        throw new ConfigurationException(
            "rate_limited_rule_based sampler .rules[].span_kind is required");
      }

      SpanKind spanKind;
      try {
        spanKind = SpanKind.valueOf(spanKindString);
      } catch (IllegalArgumentException e) {
        throw new ConfigurationException(
            "rate_limited_rule_based sampler .rules[].span_kind is invalid: " + spanKindString, e);
      }

      String attribute = rule.getString("attribute");
      if (attribute == null) {
        throw new ConfigurationException(
            "rate_limited_rule_based sampler .rules[].attribute is required");
      }
      AttributeKey<String> attributeKey = AttributeKey.stringKey(attribute);
      String pattern = rule.getString("pattern");
      if (pattern == null) {
        throw new ConfigurationException(
            "rate_limited_rule_based sampler .rules[].pattern is required");
      }
      String action = rule.getString("action");
      if (action == null) {
        throw new ConfigurationException(
            "rate_limited_rule_based sampler .rules[].action is required");
      }
      if (action.equals(ACTION_RECORD_AND_SAMPLE)) {
        builder.recordAndSample(spanKind, attributeKey, pattern);
      } else if (action.equals(ACTION_DROP)) {
        builder.drop(spanKind, attributeKey, pattern);
      } else {
        throw new ConfigurationException(
            "rate_limited_rule_based sampler .rules[].action is must be "
                + ACTION_RECORD_AND_SAMPLE
                + " or "
                + ACTION_DROP);
      }
    }

    return builder.build();
  }

  @Override
  public String getName() {
    return "rate_limited_rule_based";
  }

  @Override
  public Class<Sampler> getType() {
    return Sampler.class;
  }
}
