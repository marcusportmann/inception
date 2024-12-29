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
import java.util.Objects;
import java.util.regex.Pattern;

/**
 * The <b>SamplingRule</b> hold the information for a sampling rule that matches a span's attribute
 * against a regex pattern and delegates the sampling decision to a specified sampler if the match
 * is successful.
 *
 * @author Marcus Portmann
 */
public final class SamplingRule {

  private final AttributeKey<String> attributeKey;

  private final Sampler delegate;

  private final Pattern pattern;

  private final SpanKind spanKind;

  /**
   * Constructs a new <b>SamplingRule</b>>.
   *
   * @param spanKind the span kind
   * @param attributeKey the attribute key to match against
   * @param pattern the regex pattern to apply
   * @param delegate the sampler to delegate to upon a successful match
   * @throws NullPointerException if any of the parameters are {@code null}
   */
  public SamplingRule(
      SpanKind spanKind, AttributeKey<String> attributeKey, String pattern, Sampler delegate) {
    this.spanKind = spanKind;
    this.attributeKey = Objects.requireNonNull(attributeKey, "attributeKey must not be null");
    this.pattern = Pattern.compile(Objects.requireNonNull(pattern, "pattern must not be null"));
    this.delegate = Objects.requireNonNull(delegate, "delegate must not be null");
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof SamplingRule that)) {
      return false;
    }
    return spanKind.equals(that.spanKind)
        && attributeKey.equals(that.attributeKey)
        && pattern.pattern().equals(that.pattern.pattern())
        && delegate.equals(that.delegate);
  }

  /**
   * Returns the attribute key associated with this sampling rule.
   *
   * @return the attribute key
   */
  public AttributeKey<String> getAttributeKey() {
    return attributeKey;
  }

  /**
   * Returns the sampler delegate associated with this sampling rule.
   *
   * @return the sampler delegate
   */
  public Sampler getDelegate() {
    return delegate;
  }

  /**
   * Returns the regex pattern associated with this sampling rule.
   *
   * @return the regex pattern
   */
  public Pattern getPattern() {
    return pattern;
  }

  /**
   * Returns the span kind associated with this sampling rule.
   *
   * @return the span kind associated with this sampling rule
   */
  public SpanKind getSpanKind() {
    return spanKind;
  }

  @Override
  public int hashCode() {
    return Objects.hash(attributeKey, pattern.pattern(), delegate);
  }

  @Override
  public String toString() {
    return "SamplingRule{"
        + "spanKind="
        + spanKind
        + ", attributeKey="
        + attributeKey
        + ", delegate="
        + delegate
        + ", pattern="
        + pattern
        + '}';
  }
}
