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

package digital.inception.party.model;

import digital.inception.core.model.CodeEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlEnumValue;
import jakarta.xml.bind.annotation.XmlType;

/**
 * The {@code MeasurementUnitType} enumeration defines the measurement unit types.
 *
 * @author Marcus Portmann
 */
@Schema(description = "A type of unit of measurement")
@XmlEnum
@XmlType(name = "MeasurementUnitType", namespace = "https://inception.digital/party")
public enum MeasurementUnitType implements CodeEnum {
  /** Length. */
  @XmlEnumValue("Length")
  LENGTH("length", "Length"),

  /** Mass. */
  @XmlEnumValue("Mass")
  MASS("mass", "Mass"),

  /** Volume. */
  @XmlEnumValue("Volume")
  VOLUME("volume", "Volume");

  private final String code;

  private final String description;

  MeasurementUnitType(String code, String description) {
    this.code = code;
    this.description = description;
  }

  /**
   * Returns the code for the measurement unit type.
   *
   * @return the code for the measurement unit type
   */
  public String code() {
    return code;
  }

  /**
   * Returns the description for the measurement unit type.
   *
   * @return the description for the measurement unit type
   */
  public String description() {
    return description;
  }
}
