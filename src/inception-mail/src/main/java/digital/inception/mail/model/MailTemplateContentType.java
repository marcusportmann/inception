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

package digital.inception.mail.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlEnumValue;
import jakarta.xml.bind.annotation.XmlType;

/**
 * The <b>MailTemplateContentType</b> enumeration defines the possible content types for mail
 * templates.
 *
 * @author Marcus Portmann
 */
@Schema(description = "The mail template content type")
@XmlEnum
@XmlType(name = "MailTemplateContentType", namespace = "https://inception.digital/mail")
public enum MailTemplateContentType {
  /** Text. */
  @XmlEnumValue("Text")
  TEXT("text", "Text"),

  /** HTML. */
  @XmlEnumValue("HTML")
  HTML("html", "HTML");

  private final String code;

  private final String description;

  MailTemplateContentType(String code, String description) {
    this.code = code;
    this.description = description;
  }

  /**
   * Returns the mail template content type given by the specified code value.
   *
   * @param code the code for the mail template content type
   * @return the mail template content type given by the specified code value
   */
  @JsonCreator
  public static MailTemplateContentType fromCode(String code) {
    for (MailTemplateContentType value : MailTemplateContentType.values()) {
      if (value.code.equalsIgnoreCase(code)) {
        return value;
      }
    }
    throw new RuntimeException(
        "Failed to determine the mail template content type with the invalid code (" + code + ")");
  }

  /**
   * Returns the code for the mail template content type.
   *
   * @return the code for the mail template content type
   */
  @JsonValue
  public String code() {
    return code;
  }

  /**
   * Returns the description for the mail template content type.
   *
   * @return the description for the mail template content type
   */
  public String description() {
    return description;
  }
}
