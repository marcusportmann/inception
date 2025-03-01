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

package demo.model;

import digital.inception.core.model.CodeEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlEnumValue;
import jakarta.xml.bind.annotation.XmlType;

/**
 * The enumeration giving the possible car types.
 *
 * @author Marcus Portmann
 */
@Schema(description = "The car type")
@XmlEnum
@XmlType(name = "CarType", namespace = "https://demo")
public enum CarType implements CodeEnum {

  /** Electric. */
  @XmlEnumValue("Electric")
  ELECTRIC("electric", "Electric"),

  /** Diesel. */
  @XmlEnumValue("Diesel")
  DIESEL("diesel", "Diesel"),

  /** Hybrid. */
  @XmlEnumValue("Hybrid")
  HYBRID("hybrid", "Hybrid"),

  /** Petrol. */
  @XmlEnumValue("Petrol")
  PETROL("petrol", "Petrol");

  private final String code;

  private final String description;

  CarType(String code, String description) {
    this.code = code;
    this.description = description;
  }

  /**
   * Returns the code for the car type.
   *
   * @return the code for the car type
   */
  public String code() {
    return code;
  }

  /**
   * Returns the description for the car type.
   *
   * @return the description for the car type
   */
  public String description() {
    return description;
  }

  /**
   * Returns the string representation of the enumeration value.
   *
   * @return the string representation of the enumeration value
   */
  public String toString() {
    return description;
  }
}
