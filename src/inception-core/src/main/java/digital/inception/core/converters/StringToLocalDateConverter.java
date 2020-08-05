/*
 * Copyright 2020 Marcus Portmann
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

// ~--- non-JDK imports --------------------------------------------------------

import digital.inception.core.util.ISO8601Util;
import java.time.LocalDate;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

// ~--- JDK imports ------------------------------------------------------------

/**
 * The <code>StringToLocalDateConverter</code> class implements the Spring converter that converts
 * a
 * <code>String</code> type into a <code>LocalDate</code> type.
 *
 * @author Marcus Portmann
 */
@Component
@SuppressWarnings("unused")
public final class StringToLocalDateConverter implements Converter<String, LocalDate> {

  /**
   * Constructs a new <code>StringToLocalDateConverter</code>.
   */
  public StringToLocalDateConverter() {
  }

  @Override
  public LocalDate convert(String source) {
    if (StringUtils.isEmpty(source)) {
      return null;
    }

    try {
      return ISO8601Util.toLocalDate(source);
    } catch (Throwable e) {
      throw new RuntimeException("Failed to parse the ISO8601Util date value (" + source + ")", e);
    }
  }
}
