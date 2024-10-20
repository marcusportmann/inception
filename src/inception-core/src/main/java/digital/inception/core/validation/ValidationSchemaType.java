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

package digital.inception.core.validation;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlEnumValue;
import jakarta.xml.bind.annotation.XmlType;

/**
 * The <b>ValidationSchemaType</b> enumeration defines the possible validation schema types.
 *
 * @author Marcus Portmann
 */
@Schema(description = "The validation schema type")
@XmlEnum
@XmlType(name = "ValidationSchemaType", namespace = "https://inception.digital/core")
public enum ValidationSchemaType {

  /** JSON Schema. */
  @XmlEnumValue("JSON")
  JSON("json", "application/schema+json", "JSON Schema"),

  /** XML Schema (XSD). */
  @XmlEnumValue("XML")
  XML("xml", "application/xsd+xml", "XML Schema (XSD)");

  /** The validation schema types. */
  public static final ValidationSchemaType[] SCHEMA_TYPES = new ValidationSchemaType[] {JSON, XML};

  private final String code;

  private final String description;

  private final String mimeType;

  ValidationSchemaType(String code, String mimeType, String description) {
    this.code = code;
    this.mimeType = mimeType;
    this.description = description;
  }

  /**
   * Returns the validation schema type given by the specified code value.
   *
   * @param code the code for the validation schema type
   * @return the validation schema type given by the specified code value
   */
  @JsonCreator
  public static ValidationSchemaType fromCode(String code) {
    for (ValidationSchemaType value : ValidationSchemaType.values()) {
      if (value.code.equalsIgnoreCase(code)) {
        return value;
      }
    }
    throw new RuntimeException(
        "Failed to determine the validation schema type with the invalid code (" + code + ")");
  }

  /**
   * Returns the validation schema type with the specified MIME type.
   *
   * @param mimeType the MIME type for the validation schema type
   * @return the validation schema type with the specified MIME type
   */
  public static ValidationSchemaType fromMimeType(String mimeType) {
    return switch (mimeType) {
      case "application/schema+json" -> ValidationSchemaType.JSON;
      case "application/xsd+xml" -> ValidationSchemaType.XML;
      default ->
          throw new RuntimeException(
              "Failed to determine the validation schema type with the unknown mime type ("
                  + mimeType
                  + ")");
    };
  }

  /**
   * Returns the code for the validation schema type.
   *
   * @return the code for the validation schema type
   */
  @JsonValue
  public String code() {
    return code;
  }

  /**
   * Returns the description for the validation schema type.
   *
   * @return the description for the validation schema type
   */
  public String description() {
    return description;
  }

  /**
   * Returns the MIME type for the validation schema type.
   *
   * @return the MIME type for the validation schema type
   */
  @JsonValue
  public String mimeType() {
    return mimeType;
  }
}
