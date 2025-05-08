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

package digital.inception.jpa;

import digital.inception.core.model.CodeEnum;
import jakarta.persistence.AttributeConverter;

/**
 * The {@code CodeEnumAttributeConverter} class provides a base class that implements a generic JPA
 * AttributeConverter for any enum that implements {@link CodeEnum}.
 *
 * @param <E> the enumeration type
 * @author Marcus Portmann
 */
public abstract class AbstractCodeEnumAttributeConverter<E extends Enum<E> & CodeEnum>
    implements AttributeConverter<E, String> {

  private final Class<E> enumType;

  /**
   * Creates a new {@code CodeEnumAttributeConverter} instance.
   *
   * @param enumType the Enum type
   */
  protected AbstractCodeEnumAttributeConverter(Class<E> enumType) {
    this.enumType = enumType;
  }

  @Override
  public String convertToDatabaseColumn(E attribute) {
    // Null-safe
    return attribute == null ? null : attribute.code();
  }

  @Override
  public E convertToEntityAttribute(String dbData) {
    // Also handle null/empty from DB
    if (dbData == null || dbData.isEmpty()) {
      return null;
    }
    return CodeEnum.fromCode(enumType, dbData);
  }
}
