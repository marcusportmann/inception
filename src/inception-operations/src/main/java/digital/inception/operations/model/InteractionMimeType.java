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

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlEnumValue;
import jakarta.xml.bind.annotation.XmlType;

/**
 * The <b>InteractionMimeType</b> enumeration defines the possible interaction mime types.
 *
 * @author Marcus Portmann
 */
@Schema(description = "The interaction mime type")
@XmlEnum
@XmlType(name = "InteractionMimeType", namespace = "https://inception.digital/operations")
public enum InteractionMimeType {

  /** Text/Plain. */
  @XmlEnumValue("text/plain")
  TEXT_PLAIN("text/plain", "Text/Plain"),

  /** Text/HTML. */
  @XmlEnumValue("text/html")
  TEXT_HTML("text/html", "Text/HTML");

  private final String code;

  private final String description;

  InteractionMimeType(String code, String description) {
    this.code = code;
    this.description = description;
  }

  /**
   * Returns the interaction mime type given by the specified code value.
   *
   * @param code the code for the interaction mime type
   * @return the interaction mime type given by the specified code value
   */
  @JsonCreator
  public static InteractionMimeType fromCode(String code) {
    for (InteractionMimeType value : InteractionMimeType.values()) {
      if (value.code.equalsIgnoreCase(code)) {
        return value;
      }
    }
    throw new RuntimeException(
        "Failed to determine the interaction mime type with the invalid code (" + code + ")");
  }

  /**
   * Returns the code for the interaction mime type.
   *
   * @return the code for the interaction mime type
   */
  @JsonValue
  public String code() {
    return code;
  }

  /**
   * Returns the description for the interaction mime type.
   *
   * @return the description for the interaction mime type
   */
  public String description() {
    return description;
  }
}
