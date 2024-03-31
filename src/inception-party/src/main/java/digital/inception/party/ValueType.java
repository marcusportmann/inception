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

package digital.inception.party;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlEnumValue;
import jakarta.xml.bind.annotation.XmlType;

/**
 * The <b>ValueType</b> enumeration defines the possible value types.
 *
 * @author Marcus Portmann
 */
@Schema(description = "The value type")
@XmlEnum
@XmlType(name = "ValueType", namespace = "http://inception.digital/party")
public enum ValueType {
  /** Boolean. */
  @XmlEnumValue("Boolean")
  BOOLEAN("boolean", "A boolean value"),

  /** Date. */
  @XmlEnumValue("Date")
  DATE("date", "A date value"),

  /** Decimal. */
  @XmlEnumValue("Decimal")
  DECIMAL("decimal", "A decimal value"),

  /** Double. */
  @XmlEnumValue("Double")
  DOUBLE("double", "A double value"),

  /** Integer. */
  @XmlEnumValue("Integer")
  INTEGER("integer", "An integer value"),

  /** String. */
  @XmlEnumValue("String")
  STRING("string", "A string value");

  private final String code;

  private final String description;

  ValueType(String code, String description) {
    this.code = code;
    this.description = description;
  }

  /**
   * Returns the value type given by the specified code value.
   *
   * @param code the code for the value type
   * @return the value type given by the specified code value
   */
  @JsonCreator
  public static ValueType fromCode(String code) {
    return switch (code) {
      case "boolean" -> ValueType.BOOLEAN;
      case "date" -> ValueType.DATE;
      case "decimal" -> ValueType.DECIMAL;
      case "double" -> ValueType.DOUBLE;
      case "integer" -> ValueType.INTEGER;
      case "string" -> ValueType.STRING;
      default -> throw new RuntimeException(
          "Failed to determine the value type with the invalid code (" + code + ")");
    };
  }

  /**
   * Returns the code for the value type.
   *
   * @return the code for the value type
   */
  @JsonValue
  public String code() {
    return code;
  }

  /**
   * Returns the description for the value type.
   *
   * @return the description for the value type
   */
  public String description() {
    return description;
  }

  /**
   * Return the string representation of the value type enumeration value.
   *
   * @return the string representation of the value type enumeration value
   */
  public String toString() {
    return description;
  }
}
