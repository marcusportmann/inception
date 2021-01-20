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
 * The <code>ContactMechanismType</code> enumeration defines the possible contact mechanism types.
 *
 * @author Marcus Portmann
 */
@Schema(description = "The contact mechanism type")
@XmlEnum
@XmlType(name = "ContactMechanismType", namespace = "http://party.inception.digital")
public enum ContactMechanismType {
  @XmlEnumValue("MobileNumber")
  MOBILE_NUMBER("mobile_number", "Mobile Number"),
  @XmlEnumValue("PhoneNumber")
  PHONE_NUMBER("phone_number", "Phone Number"),
  @XmlEnumValue("FaxNumber")
  FAX_NUMBER("fax_number", "Fax Number"),
  @XmlEnumValue("EmailAddress")
  EMAIL_ADDRESS("email_address", "E-mail Address"),
  @XmlEnumValue("SocialMedia")
  SOCIAL_MEDIA("social_media", "Social Media");

  private final String code;

  private final String description;

  ContactMechanismType(String code, String description) {
    this.code = code;
    this.description = description;
  }

  /**
   * Returns the contact mechanism type given by the specified code value.
   *
   * @param code the code for the contact mechanism type
   * @return the contact mechanism type given by the specified code value
   */
  @JsonCreator
  public static ContactMechanismType fromCode(String code) {
    switch (code) {
      case "mobile_number":
        return ContactMechanismType.MOBILE_NUMBER;

      case "phone_number":
        return ContactMechanismType.PHONE_NUMBER;

      case "fax_number":
        return ContactMechanismType.FAX_NUMBER;

      case "email_address":
        return ContactMechanismType.EMAIL_ADDRESS;

      case "social_media":
        return ContactMechanismType.SOCIAL_MEDIA;

      default:
        throw new RuntimeException(
            "Failed to determine the contact mechanism type with the invalid code (" + code + ")");
    }
  }

  /**
   * Returns the code for the contact mechanism type.
   *
   * @return the code for the contact mechanism type
   */
  @JsonValue
  public String code() {
    return code;
  }

  /**
   * Returns the description for the contact mechanism type.
   *
   * @return the description for the contact mechanism type
   */
  public String description() {
    return description;
  }
}
