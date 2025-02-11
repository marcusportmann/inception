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
import org.springframework.stereotype.Component;

/**
 * The <b>TimeUnitToStringConverter</b> class implements the Spring converter that converts a
 * <b>TimeUnit</b> type into a <b>String</b> type.
 *
 * @author Marcus Portmann
 */
@SuppressWarnings("unused")
@Component
public final class TimeUnitToStringConverter implements Converter<TimeUnit, String> {

  /** Constructs a new <b>TimeUnitToStringConverter</b>. */
  public TimeUnitToStringConverter() {}

  @Override
  public String convert(TimeUnit source) {
    return source.code();
  }
}
