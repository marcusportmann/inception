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

import digital.inception.core.time.TimeUnit;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

/**
 * The <b>StringToTimeUnitConverter</b> class implements the Spring converter that converts a
 * <b>String</b> type into a <b>TimeUnit</b> type.
 *
 * @author Marcus Portmann
 */
@SuppressWarnings("unused")
@Component
public final class StringToTimeUnitConverter implements Converter<String, TimeUnit> {

  /** Constructs a new <b>StringToTimeUnitConverter</b>. */
  public StringToTimeUnitConverter() {}

  @Override
  public TimeUnit convert(@NonNull String source) {
    if (!StringUtils.hasText(source)) {
      return null;
    }

    return TimeUnit.fromCode(source);
  }
}
