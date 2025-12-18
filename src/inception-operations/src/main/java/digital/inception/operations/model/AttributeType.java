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

package digital.inception.operations.model;

import com.fasterxml.jackson.annotation.JsonValue;
import digital.inception.core.model.CodeEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlEnumValue;
import jakarta.xml.bind.annotation.XmlType;

/**
 * The {@code AttributeType} enumeration defines the possible attribute types.
 *
 * @author Marcus Portmann
 */
@Schema(description = "The attribute type type")
@XmlEnum
@XmlType(name = "AttributeType", namespace = "https://inception.digital/operations")
public enum AttributeType implements CodeEnum {

  /** Boolean. */
  @XmlEnumValue("Boolean")
  BOOLEAN("boolean", "Boolean"),

  /** Date. */
  @XmlEnumValue("Date")
  DATE("date", "Date"),

  /** DateTime. */
  @XmlEnumValue("DateTime")
  DATE_TIME("date_time", "DateTime"),

  /** Decimal. */
  @XmlEnumValue("Decimal")
  DECIMAL("decimal", "Decimal"),

  /** Double. */
  @XmlEnumValue("Double")
  DOUBLE("double", "Double"),

  /** Integer. */
  @XmlEnumValue("Integer")
  INTEGER("integer", "Integer"),

  /** Long. */
  @XmlEnumValue("Long")
  LONG("long", "Long"),

  /** String. */
  @XmlEnumValue("String")
  STRING("string", "String");

  private final String code;

  private final String description;

  AttributeType(String code, String description) {
    this.code = code;
    this.description = description;
  }

  /**
   * Returns the code for the attribute type.
   *
   * @return the code for the attribute type
   */
  @JsonValue
  public String code() {
    return code;
  }

  /**
   * Returns the description for the attribute type.
   *
   * @return the description for the attribute type
   */
  public String description() {
    return description;
  }
}
