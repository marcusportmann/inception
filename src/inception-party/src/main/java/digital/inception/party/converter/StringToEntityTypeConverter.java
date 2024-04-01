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

package digital.inception.party.converter;

import digital.inception.party.model.EntityType;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;
import org.springframework.stereotype.Component;

/**
 * The <b>StringToEntityTypeConverter</b> class implements the Spring converter that converts a
 * <b>String</b> type into a <b>EntityType</b> type.
 *
 * @author Marcus Portmann
 */
@Component
@ReadingConverter
public class StringToEntityTypeConverter implements Converter<String, EntityType> {

  /** Constructs a new <b>StringToEntityTypeConverter</b>. */
  public StringToEntityTypeConverter() {}

  @Override
  public EntityType convert(String source) {
    return EntityType.fromCode(source);
  }
}
