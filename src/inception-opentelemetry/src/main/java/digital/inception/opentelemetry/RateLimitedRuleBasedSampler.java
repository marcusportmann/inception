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
import io.opentelemetry.api.common.Attributes;
import io.opentelemetry.api.trace.SpanKind;
import io.opentelemetry.context.Context;
import io.opentelemetry.sdk.trace.data.LinkData;
import io.opentelemetry.sdk.trace.samplers.Sampler;
import io.opentelemetry.sdk.trace.samplers.SamplingResult;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * The <b>RateLimitedRuleBasedSampler</b> class provides a sampler that applies rate limiting to
 * spans evaluated against a list of {@link SamplingRule}s to determine sampling decisions based on
 * span attributes and predefined patterns.
 *
 * <p>Matching is performed using regular expressions.
 *
 * <p>If the span kind differs from the specified kind, or if no rules match, the fallback sampler
 * is used to make the decision.
 *
 * <p>Note: Only attributes set on {@link io.opentelemetry.api.trace.SpanBuilder} are considered;
 * attributes added after span creation are ignored.
 *
 * <p>This is based on the <b>io.opentelemetry.contrib.sampler.RuleBasedSampler</b> class, which is
 * part of the OpenTelemetry <b>opentelemetry-java-contrib</b> project.
 *
 * @author Marcus Portmann
 */
public final class RateLimitedRuleBasedSampler implements Sampler {

  private static final long RATE_LIMIT_BURST_MULTIPLIER = 100;

  private static final AttributeKey<String> THREAD_NAME = AttributeKey.stringKey("thread.name");

  private static final Logger logger =
      Logger.getLogger(RateLimitedRuleBasedSampler.class.getName());

  private final boolean enableRuleLogging;

  private final Sampler fallbackSampler;

  private final TokenBucketRateLimiter rateLimiter;

  private final List<SamplingRule> rules;

  private final long spansPerSecondLimit;

  /**
   * Constructs a <b>RateLimitedRuleBasedSampler</b> with the specified spans per second limit,
   * rules, span kind, and fallback sampler.
   *
   * @param enableRuleLogging enable additional logging during the processing of rules
   * @param spansPerSecondLimit the maximum number of spans to sample per second
   * @param fallbackSampler the fallback sampler to use when no rules match
   * @param rules the list of sampling rules to apply
   */
  public RateLimitedRuleBasedSampler(
      boolean enableRuleLogging,
      long spansPerSecondLimit,
      Sampler fallbackSampler,
      List<SamplingRule> rules) {
    this.enableRuleLogging = enableRuleLogging;
    this.spansPerSecondLimit = spansPerSecondLimit;
    this.rateLimiter =
        new TokenBucketRateLimiter(
            RATE_LIMIT_BURST_MULTIPLIER * spansPerSecondLimit, spansPerSecondLimit);
    this.fallbackSampler =
        Objects.requireNonNull(fallbackSampler, "fallback sampler must not be null");
    this.rules = List.copyOf(Objects.requireNonNull(rules, "rules must not be null"));

    logger.info(
        String.format(
            "Initialized the RateLimitedRuleBasedSampler with enableRuleLogging=%b, spansPerSecondLimit=%d, fallbackSampler=%s, rules=%s",
            enableRuleLogging, spansPerSecondLimit, fallbackSampler, rules));
  }

  /**
   * Creates a builder for <b>RateLimitedRuleBasedSampler</b>.
   *
   * @param enableRuleLogging enable additional logging during the processing of rules
   * @param spansPerSecondLimit the maximum number of spans to sample per second
   * @param fallbackSampler the fallback sampler to use when no rules match
   * @return a new builder instance
   */
  public static RateLimitedRuleBasedSamplerBuilder builder(
      boolean enableRuleLogging, long spansPerSecondLimit, Sampler fallbackSampler) {
    return new RateLimitedRuleBasedSamplerBuilder(
        enableRuleLogging, spansPerSecondLimit, fallbackSampler);
  }

  @Override
  public String getDescription() {
    return String.format(
        "RateLimitedRuleBasedSampler{enableRuleLogging=%b, spansPerSecondLimit=%d, fallbackSampler=%s, rules=%s}",
        enableRuleLogging, spansPerSecondLimit, fallbackSampler, rules);
  }

  @Override
  public SamplingResult shouldSample(
      Context parentContext,
      String traceId,
      String name,
      SpanKind spanKind,
      Attributes attributes,
      List<LinkData> parentLinks) {

    if (!rateLimiter.tryAcquire()) {
      if (logger.isLoggable(Level.FINE)) {
        logger.fine(
            "Spans per second rate limit ("
                + spansPerSecondLimit
                + ") exceeded when processing span with trace ID ("
                + traceId
                + "), kind ("
                + spanKind.name()
                + "), name ("
                + name
                + ") and attributes ("
                + getAttributesAsString(attributes)
                + ")");
      }

      return SamplingResult.drop();
    }

    // Evaluate sampling rules
    for (SamplingRule rule : rules) {
      String attributeValue = getAttributeValue(rule.getAttributeKey(), attributes);
      if (rule.getSpanKind().equals(spanKind)
          && attributeValue != null
          && rule.getPattern().matcher(attributeValue).find()) {

        if (enableRuleLogging || logger.isLoggable(Level.FINE)) {
          logger.info(
              "Rule with span kind ("
                  + rule.getSpanKind()
                  + "), attribute ("
                  + rule.getAttributeKey()
                  + ") and pattern ("
                  + rule.getPattern()
                  + ") matches the span with trace ID ("
                  + traceId
                  + "), kind ("
                  + spanKind.name()
                  + "), name ("
                  + name
                  + ") and attributes ("
                  + getAttributesAsString(attributes)
                  + ")");
        }

        return rule.getDelegate()
            .shouldSample(parentContext, traceId, name, spanKind, attributes, parentLinks);
      }
    }

    if (enableRuleLogging || logger.isLoggable(Level.FINE)) {
      logger.info(
          "No rules match the span with trace ID ("
              + traceId
              + "), kind ("
              + spanKind.name()
              + "), name ("
              + name
              + ") and attributes ("
              + getAttributesAsString(attributes)
              + ")");
    }

    // Fallback sampling decision
    return fallbackSampler.shouldSample(
        parentContext, traceId, name, spanKind, attributes, parentLinks);
  }

  @Override
  public String toString() {
    return getDescription();
  }

  private String getAttributeValue(AttributeKey<String> key, Attributes attributes) {
    if (THREAD_NAME.equals(key)) {
      return Thread.currentThread().getName();
    }
    return attributes.get(key);
  }

  private String getAttributesAsString(Attributes attributes) {
    StringBuilder buffer = new StringBuilder();
    attributes.forEach(
        (attributeKey, object) -> {
          if (!buffer.isEmpty()) {
            buffer.append(", ");
          }
          buffer.append(String.format("\"%s\":\"%s\"", attributeKey, object));
        });

    return buffer.toString();
  }
}
