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

import java.util.Collections;
import java.util.Locale;
import org.hibernate.validator.internal.engine.messageinterpolation.DefaultLocaleResolver;
import org.hibernate.validator.internal.engine.messageinterpolation.InterpolationTerm;
import org.hibernate.validator.internal.engine.messageinterpolation.ParameterTermResolver;
import org.hibernate.validator.messageinterpolation.AbstractMessageInterpolator;
import org.hibernate.validator.resourceloading.PlatformResourceBundleLocator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The <b>ValidationMessageInterpolator</b> provides a bundle-backed message interpolator, which
 * does not support EL expressions, but does support parameter value expressions.
 *
 * @author Marcus Portmann
 */
@SuppressWarnings("unused")
public class ValidationMessageInterpolator extends AbstractMessageInterpolator {

  /* Logger */
  private static final Logger log = LoggerFactory.getLogger(ValidationMessageInterpolator.class);

  /** Constructs a new <b>ValidationMessageInterpolator</b>. */
  public ValidationMessageInterpolator() {
    super(
        new PlatformResourceBundleLocator(
            USER_VALIDATION_MESSAGES, Collections.emptySet(), null, true),
        Collections.emptySet(),
        Locale.getDefault(),
        new DefaultLocaleResolver(),
        false);
  }

  @Override
  protected String interpolate(Context context, Locale locale, String term) {
    if (InterpolationTerm.isElExpression(term)) {
      if (log.isWarnEnabled()) {
        log.warn(
            "The message contains an EL expression, which is not supported by the message interpolator ("
                + term
                + ")");
      }
      return term;
    } else {
      ParameterTermResolver parameterTermResolver = new ParameterTermResolver();
      return parameterTermResolver.interpolate(context, term);
    }
  }
}
