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

package digital.inception.party.generator;

import java.util.Locale;

/**
 * The {@code Generator} class that supports the generation of random party data.
 *
 * @author Marcus Portmann
 */
public class Generator {

  /** The locale to use when generating random party data. */
  private final Locale locale;

  /** Constructs a new {@code Generator}. */
  public Generator() {
    this.locale = Locale.forLanguageTag("en-US");
  }

  /**
   * Constructs a new {@code Generator}.
   *
   * @param locale the locale
   */
  public Generator(Locale locale) {
    this.locale = locale;
  }
}
