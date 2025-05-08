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

package digital.inception.core.converter;

import digital.inception.core.util.ISO8601Util;
import java.time.LocalTime;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

/**
 * The {@code LocalTimeToStringConverter} class implements the Spring converter that converts a
 * {@code LocalTime} type into a {@code String} type.
 *
 * @author Marcus Portmann
 */
@SuppressWarnings("unused")
@Component
public final class LocalTimeToStringConverter implements Converter<LocalTime, String> {

  /** Creates a new {@code LocalTimeToStringConverter} instance. */
  public LocalTimeToStringConverter() {}

  @Override
  public String convert(LocalTime source) {
    return ISO8601Util.fromLocalTime(source);
  }
}
