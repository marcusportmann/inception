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

package digital.inception.core.converters;

import digital.inception.core.util.ISO8601Util;
import java.time.LocalDateTime;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

/**
 * The <b>LocalDateTimeToStringConverter</b> class implements the Spring converter that converts a
 * <b>LocalDateTime</b> type into a <b>String</b> type.
 *
 * @author Marcus Portmann
 */
@SuppressWarnings("unused")
@Component
public final class LocalDateTimeToStringConverter implements Converter<LocalDateTime, String> {

  /** Constructs a new <b>LocalDateTimeToStringConverter</b>. */
  public LocalDateTimeToStringConverter() {}

  @Override
  public String convert(LocalDateTime source) {
    return ISO8601Util.fromLocalDateTime(source);
  }
}
