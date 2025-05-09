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
import java.time.LocalDateTime;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

/**
 * The {@code StringToLocalDateTimeConverter} class implements the Spring converter that converts a
 * {@code String} type into a {@code LocalDateTime} type.
 *
 * @author Marcus Portmann
 */
@SuppressWarnings("unused")
@Component
public final class StringToLocalDateTimeConverter implements Converter<String, LocalDateTime> {

  /** Constructs a new {@code StringToLocalDateTimeConverter}. */
  public StringToLocalDateTimeConverter() {}

  @Override
  public LocalDateTime convert(@NonNull String source) {
    if (!StringUtils.hasText(source)) {
      return null;
    }

    try {
      return ISO8601Util.toLocalDateTime(source);
    } catch (Throwable e) {
      throw new RuntimeException("Failed to parse the ISO8601 date time value (" + source + ")", e);
    }
  }
}
