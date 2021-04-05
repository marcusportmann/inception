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

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import io.swagger.v3.oas.annotations.media.Schema;
import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;

/**
 * The <b>MeasurementUnitType</b> enumeration defines the measurement unit types.
 *
 * @author Marcus Portmann
 */
@Schema(description = "A type of unit of measurement")
@XmlEnum
@XmlType(name = "MeasurementUnitType", namespace = "http://inception.digital/party")
public enum MeasurementUnitType {
  @XmlEnumValue("Length")
  LENGTH("length", "Length"),
  @XmlEnumValue("Mass")
  MASS("mass", "Mass"),
  @XmlEnumValue("Volume")
  VOLUME("volume", "Volume");

  private final String code;

  private final String description;

  MeasurementUnitType(String code, String description) {
    this.code = code;
    this.description = description;
  }

  /**
   * Returns the measurement unit type given by the specified code value.
   *
   * @param code the code for the measurement unit type
   * @return the measurement unit type given by the specified code value
   */
  @JsonCreator
  public static MeasurementUnitType fromCode(String code) {
    switch (code) {
      case "length":
        return MeasurementUnitType.LENGTH;

      case "mass":
        return MeasurementUnitType.MASS;

      case "volume":
        return MeasurementUnitType.VOLUME;

      default:
        throw new RuntimeException(
            "Failed to determine the measurement unit type with the invalid code (" + code + ")");
    }
  }

  /**
   * Returns the code for the measurement unit type.
   *
   * @return the code for the measurement unit type
   */
  @JsonValue
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
