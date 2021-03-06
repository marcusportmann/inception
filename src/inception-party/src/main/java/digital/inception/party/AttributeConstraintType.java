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
 * The <b>AttributeConstraintType</b> enumeration defines the types of constraints that can be
 * applied when validating an attribute for a party.
 *
 * @author Marcus Portmann
 */
@Schema(description = "The attribute constraint type")
@XmlEnum
@XmlType(name = "AttributeConstraintType", namespace = "http://inception.digital/party")
public enum AttributeConstraintType {
  @XmlEnumValue("MaxSize")
  MAX_SIZE("max_size", "Maximum Size"),
  @XmlEnumValue("MinSize")
  MIN_SIZE("min_size", "Minimum Size"),
  @XmlEnumValue("NotNull")
  NOT_NULL("not_null", "Not Null"),
  @XmlEnumValue("Pattern")
  PATTERN("pattern", "Pattern"),
  @XmlEnumValue("Required")
  REQUIRED("required", "Required"),
  @XmlEnumValue("Size")
  SIZE("size", "Size"),
  @XmlEnumValue("Type")
  TYPE("type", "Type");

  private final String code;

  private final String description;

  AttributeConstraintType(String code, String description) {
    this.code = code;
    this.description = description;
  }

  /**
   * Returns the attribute constraint type given by the specified code value.
   *
   * @param code the code for the attribute constraint type
   * @return the attribute constraint type given by the specified code value
   */
  @JsonCreator
  public static AttributeConstraintType fromCode(String code) {
    switch (code) {
      case "max_size":
        return AttributeConstraintType.MAX_SIZE;

      case "min_size":
        return AttributeConstraintType.MIN_SIZE;

      case "not_null":
        return AttributeConstraintType.NOT_NULL;

      case "pattern":
        return AttributeConstraintType.PATTERN;

      case "required":
        return AttributeConstraintType.REQUIRED;

      case "size":
        return AttributeConstraintType.SIZE;

      case "type":
        return AttributeConstraintType.TYPE;

      default:
        throw new RuntimeException(
            "Failed to determine the attribute constraint type with the invalid code ("
                + code
                + ")");
    }
  }

  /**
   * Returns the code for the attribute constraint type.
   *
   * @return the code for the attribute constraint type
   */
  @JsonValue
  public String code() {
    return code;
  }

  /**
   * Returns the description for the attribute constraint type.
   *
   * @return the description for the attribute constraint type
   */
  public String description() {
    return description;
  }
}
