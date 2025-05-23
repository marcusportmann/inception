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

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

/**
 * The {@code DateToLocalDateTimeConverter} class implements the Spring converter that converts a
 * {@code Date} type into a {@code LocalDateTime} type.
 *
 * @author Marcus Portmann
 */
@SuppressWarnings("unused")
@Component
public final class DateToLocalDateTimeConverter implements Converter<Date, LocalDateTime> {

  /** Constructs a new {@code DateToLocalDateTimeConverter}. */
  public DateToLocalDateTimeConverter() {}

  @Override
  public LocalDateTime convert(Date source) {
    return LocalDateTime.ofInstant(source.toInstant(), ZoneOffset.UTC);
  }
}
