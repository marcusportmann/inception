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

package digital.inception.security.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlEnumValue;
import jakarta.xml.bind.annotation.XmlType;

/**
 * The <b>TokenType</b> enumeration defines the possible token types.
 *
 * @author Marcus Portmann
 */
@Schema(description = "The token type")
@XmlEnum
@XmlType(name = "TokenType", namespace = "http://digital.inception.co.za/security")
public enum TokenType {
  /** JWT. */
  @XmlEnumValue("JWT")
  JWT("jwt", "JWT");

  private final String code;

  private final String description;

  TokenType(String code, String description) {
    this.code = code;
    this.description = description;
  }

  /**
   * Returns the token type given by the specified code value.
   *
   * @param code the code for the token type
   * @return the token type given by the specified code value
   */
  @JsonCreator
  public static TokenType fromCode(String code) {
    return switch (code) {
      case "jwt" -> TokenType.JWT;
      default ->
          throw new RuntimeException(
              "Failed to determine the token type with the invalid code (" + code + ")");
    };
  }

  /**
   * Returns the code for the token type.
   *
   * @return the code for the token type
   */
  @JsonValue
  public String code() {
    return code;
  }

  /**
   * Returns the description for the token type.
   *
   * @return the description for the token type
   */
  public String description() {
    return description;
  }
}
