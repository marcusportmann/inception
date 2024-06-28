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

import digital.inception.core.validation.ValidationSchemaType;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

/**
 * The <b>StringToValidationSchemaTypeConverter</b> class implements the Spring converter that
 * converts a <b>String</b> type into a <b>ValidationSchemaType</b> type.
 *
 * @author Marcus Portmann
 */
@SuppressWarnings("unused")
@Component
public final class StringToValidationSchemaTypeConverter
    implements Converter<String, ValidationSchemaType> {

  /** Constructs a new <b>StringToValidationSchemaTypeConverter</b>. */
  public StringToValidationSchemaTypeConverter() {}

  @Override
  public ValidationSchemaType convert(@NonNull String source) {
    if (!StringUtils.hasText(source)) {
      return null;
    }

    return ValidationSchemaType.fromCode(source);
  }
}
