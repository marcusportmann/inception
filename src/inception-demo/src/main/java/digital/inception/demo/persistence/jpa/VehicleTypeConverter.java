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

package digital.inception.demo.persistence.jpa;

import digital.inception.demo.model.VehicleType;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

/**
 * The <b>VehicleTypeConverter</b> class implements the custom JPA converter for the <b>
 * VehicleType</b> enumeration.
 *
 * @author Marcus Portmann
 */
@Converter(autoApply = true)
public class VehicleTypeConverter implements AttributeConverter<VehicleType, String> {

  /** Constructs a new <b>VehicleTypeConverter</b>. */
  public VehicleTypeConverter() {}

  /**
   * Converts the value stored in the entity attribute into the data representation to be stored in
   * the database.
   *
   * @param attribute the entity attribute value to be converted
   * @return the converted data to be stored in the database column
   */
  @Override
  public String convertToDatabaseColumn(VehicleType attribute) {
    if (attribute == null) return null;
    return attribute.code();
  }

  /**
   * Converts the stored in the database column into the value to be stored in the entity attribute.
   * Note that it is the responsibility of the converter writer to specify the correct dbData type
   * for the corresponding column for use by the JDBC driver: i.e., persistence providers are not
   * expected to do such type conversion.
   *
   * @param dbData the data from the database column to be converted
   * @return the converted value to be stored in the entity attribute
   */
  @Override
  public VehicleType convertToEntityAttribute(String dbData) {
    if (dbData == null) return null;
    return VehicleType.fromCode(dbData);
  }
}
