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
 * The {@code InteractionType} enumeration defines the possible interaction types.
 *
 * @author Marcus Portmann
 */
@Schema(description = "The interaction type")
@XmlEnum
@XmlType(name = "InteractionType", namespace = "https://inception.digital/operations")
public enum InteractionType implements CodeEnum {

  /** Email. */
  @XmlEnumValue("Email")
  EMAIL("email", "Email"),

  /** SMS. */
  @XmlEnumValue("SMS")
  SMS("sms", "SMS"),

  /** Twitter. */
  @XmlEnumValue("Twitter")
  TWITTER("twitter", "Twitter"),

  /** USSD. */
  @XmlEnumValue("USSD")
  USSD("ussd", "USSD"),

  /** Web. */
  @XmlEnumValue("Web")
  WEB("web", "Web"),

  /** WhatsApp. */
  @XmlEnumValue("WhatsApp")
  WHATSAPP("whatsapp", "WhatsApp");

  private final String code;

  private final String description;

  InteractionType(String code, String description) {
    this.code = code;
    this.description = description;
  }

  /**
   * Returns the code for the interaction type.
   *
   * @return the code for the interaction type
   */
  public String code() {
    return code;
  }

  /**
   * Returns the description for the interaction type.
   *
   * @return the description for the interaction type
   */
  public String description() {
    return description;
  }
}
