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
import java.time.LocalDate;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

/**
 * The {@code LocalDateToStringConverter} class implements the Spring converter that converts a
 * {@code LocalDate} type into a {@code String} type.
 *
 * @author Marcus Portmann
 */
@Component
@SuppressWarnings("unused")
public final class LocalDateToStringConverter implements Converter<LocalDate, String> {

  /** Creates a new {@code LocalDateToStringConverter} instance. */
  public LocalDateToStringConverter() {}

  @Override
  public String convert(LocalDate source) {
    return ISO8601Util.fromLocalDate(source);
  }
}
