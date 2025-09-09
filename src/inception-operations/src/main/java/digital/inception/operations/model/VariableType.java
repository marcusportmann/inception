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

import digital.inception.core.model.CodeEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlEnumValue;
import jakarta.xml.bind.annotation.XmlType;

/**
 * The {@code VariableType} enumeration defines the possible variable types.
 *
 * @author Marcus Portmann
 */
@Schema(description = "The variable type type")
@XmlEnum
@XmlType(name = "VariableType", namespace = "https://inception.digital/operations")
public enum VariableType implements CodeEnum {

  /** Boolean. */
  @XmlEnumValue("Boolean")
  BOOLEAN("boolean", "Boolean"),

  /** Date. */
  @XmlEnumValue("Date")
  DATE("date", "Date"),

  /** Decimal. */
  @XmlEnumValue("Decimal")
  DECIMAL("decimal", "Decimal"),

  /** Double. */
  @XmlEnumValue("Double")
  DOUBLE("double", "Double"),

  /** Integer. */
  @XmlEnumValue("Integer")
  INTEGER("integer", "Integer"),

  /** JSON. */
  @XmlEnumValue("JSON")
  JSON("json", "JSON"),

  /** Long. */
  @XmlEnumValue("Long")
  LONG("long", "Long"),

  /** String. */
  @XmlEnumValue("String")
  STRING("string", "String");

  private final String code;

  private final String description;

  VariableType(String code, String description) {
    this.code = code;
    this.description = description;
  }

  /**
   * Returns the code for the variable type.
   *
   * @return the code for the variable type
   */
  public String code() {
    return code;
  }

  /**
   * Returns the description for the variable type.
   *
   * @return the description for the variable type
   */
  public String description() {
    return description;
  }
}
