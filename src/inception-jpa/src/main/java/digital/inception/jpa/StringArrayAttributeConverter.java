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

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import org.springframework.util.StringUtils;

/**
 * The {@code StringArrayAttributeConverter} class implements the custom JPA attribute converter to
 * store an array of strings as a comma-delimited string.
 *
 * @author Marcus Portmann
 */
@Converter(autoApply = true)
public class StringArrayAttributeConverter implements AttributeConverter<String[], String> {

  /** Constructs a new {@code StringArrayAttributeConverter}. */
  public StringArrayAttributeConverter() {}

  /**
   * Converts the value stored in the entity attribute into the data representation to be stored in
   * the database.
   *
   * @param attribute the entity attribute value to be converted
   * @return the converted data to be stored in the database column
   */
  @Override
  public String convertToDatabaseColumn(String[] attribute) {
    if (attribute == null) {
      return null;
    }

    return StringUtils.arrayToCommaDelimitedString(attribute);
  }

  /**
   * Converts the value stored in the database column into the value to be stored in the entity
   * attribute. Note that it is the responsibility of the converter writer to specify the correct
   * dbData type for the corresponding column for use by the JDBC driver: i.e., persistence
   * providers are not expected to do such type conversion.
   *
   * @param dbData the data from the database column to be converted
   * @return the converted value to be stored in the entity attribute
   */
  @Override
  public String[] convertToEntityAttribute(String dbData) {
    if (dbData == null) {
      return null;
    }

    return StringUtils.commaDelimitedListToStringArray(dbData);
  }
}
