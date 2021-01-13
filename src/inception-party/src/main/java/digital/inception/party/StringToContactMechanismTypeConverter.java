/*
 * Copyright 2021 Marcus Portmann
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

package digital.inception.party;

// ~--- non-JDK imports --------------------------------------------------------

import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;
import org.springframework.stereotype.Component;

/**
 * The <code>StringToContactMechanismTypeConverter</code> class implements the Spring converter that
 * converts a <code>String</code> type into a <code>ContactMechanismType</code> type.
 *
 * @author Marcus Portmann
 */
@Component
@ReadingConverter
public class StringToContactMechanismTypeConverter
    implements Converter<String, ContactMechanismType> {

  /** Constructs a new <code>StringToContactMechanismTypeConverter</code>. */
  public StringToContactMechanismTypeConverter() {}

  @Override
  public ContactMechanismType convert(String source) {
    return ContactMechanismType.fromCode(source);
  }
}
