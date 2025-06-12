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

package digital.inception.operations.persistence.jpa;

import digital.inception.core.model.CodeEnum;
import digital.inception.operations.model.RequiredDocumentAttribute;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import java.util.Arrays;
import java.util.List;
import java.util.StringJoiner;
import java.util.stream.Collectors;

/**
 * The {@code RequiredDocumentAttributeListAttributeConverter} class implements the custom JPA
 * converter used to store a list of {@code RequiredDocumentAttribute} instances as a String.
 *
 * @author Marcus Portmann
 */
@Converter(autoApply = true)
public class RequiredDocumentAttributeListAttributeConverter
    implements AttributeConverter<List<RequiredDocumentAttribute>, String> {

  /** Constructs a new {@code RequiredDocumentAttributeListAttributeConverter}. */
  public RequiredDocumentAttributeListAttributeConverter() {}

  /**
   * Converts the value stored in the entity attribute into the data representation to be stored in
   * the database.
   *
   * @param attribute the entity attribute value to be converted
   * @return the converted data to be stored in the database column
   */
  @Override
  public String convertToDatabaseColumn(List<RequiredDocumentAttribute> attribute) {
    if (attribute == null || attribute.isEmpty()) {
      return null;
    }

    StringJoiner joiner = new StringJoiner(",");
    for (RequiredDocumentAttribute requiredDocumentAttribute : attribute) {
      joiner.add(requiredDocumentAttribute.code());
    }

    return joiner.toString();
  }

  /**
   * Converts the data stored in the database column into the value to be stored in the entity
   * attribute. Note that it is the responsibility of the converter writer to specify the correct
   * dbData type for the corresponding column for use by the JDBC driver: i.e., persistence
   * providers are not expected to do such type conversion.
   *
   * @param dbData the data from the database column to be converted
   * @return the converted value to be stored in the entity attribute
   */
  @Override
  public List<RequiredDocumentAttribute> convertToEntityAttribute(String dbData) {
    if (dbData == null || dbData.trim().isEmpty()) {
      return null;
    }

    return Arrays.stream(dbData.split(","))
        .map(
            requiredDocumentAttributeCode ->
                CodeEnum.fromCode(RequiredDocumentAttribute.class, requiredDocumentAttributeCode))
        .collect(Collectors.toList());
  }
}
