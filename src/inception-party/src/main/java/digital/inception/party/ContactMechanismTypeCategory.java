/*
 * Copyright 2020 Marcus Portmann
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
 * The <code>ContactMechanismCategory</code> enumeration defines the possible contact mechanism
 * categories.
 *
 * @author Marcus Portmann
 */
@Schema(description = "The contact mechanism type category")
@XmlEnum
@XmlType(name = "ContactMechanismTypeCategory", namespace = "http://party.inception.digital")
public enum ContactMechanismTypeCategory {
  @XmlEnumValue("PhoneNumbers")
  PHONE_NUMBERS("phone_numbers", "Phone Numbers"),
  @XmlEnumValue("FaxNumbers")
  FAX_NUMBERS("fax_numbers", "Fax Numbers"),
  @XmlEnumValue("EmailAddresses")
  EMAIL_ADDRESSES("email_addresses", "E-mail Addresses"),
  @XmlEnumValue("SocialMedia")
  SOCIAL_MEDIA("social_media", "Social Media");

  private final String code;

  private final String description;

  ContactMechanismTypeCategory(String code, String description) {
    this.code = code;
    this.description = description;
  }

  /**
   * Returns the contact mechanism type category given by the specified code value.
   *
   * @param code the code value identifying the contact mechanism type category
   * @return the contact mechanism type category given by the specified code value
   */
  @JsonCreator
  public static ContactMechanismTypeCategory fromCode(String code) {
    switch (code) {
      case "phone_numbers":
        return ContactMechanismTypeCategory.PHONE_NUMBERS;

      case "fax_numbers":
        return ContactMechanismTypeCategory.FAX_NUMBERS;

      case "email_addresses":
        return ContactMechanismTypeCategory.EMAIL_ADDRESSES;

      case "social_media":
        return ContactMechanismTypeCategory.SOCIAL_MEDIA;

      default:
        throw new RuntimeException(
            "Failed to determine the contact mechanism type category with the invalid code ("
                + code
                + ")");
    }
  }

  /**
   * Returns the contact mechanism type category for the specified numeric code.
   *
   * @param numericCode the numeric code identifying the contact mechanism type category
   * @return the contact mechanism type category given by the specified numeric code value
   */
  public static ContactMechanismTypeCategory fromNumericCode(int numericCode) {
    switch (numericCode) {
      case 1:
        return ContactMechanismTypeCategory.PHONE_NUMBERS;
      case 2:
        return ContactMechanismTypeCategory.FAX_NUMBERS;
      case 3:
        return ContactMechanismTypeCategory.EMAIL_ADDRESSES;
      case 4:
        return ContactMechanismTypeCategory.SOCIAL_MEDIA;
      default:
        throw new RuntimeException(
            "Failed to determine the contact mechanism type category for the numeric code ("
                + numericCode
                + ")");
    }
  }

  /**
   * Returns the numeric code for the contact mechanism type category.
   *
   * @param contactMechanismTypeCategory the contact mechanism type category
   * @return the numeric code for the contact mechanism type category
   */
  public static int toNumericCode(ContactMechanismTypeCategory contactMechanismTypeCategory) {
    switch (contactMechanismTypeCategory) {
      case PHONE_NUMBERS:
        return 1;
      case FAX_NUMBERS:
        return 2;
      case EMAIL_ADDRESSES:
        return 3;
      case SOCIAL_MEDIA:
        return 4;
      default:
        throw new RuntimeException(
            "Failed to determine the numeric code for the contact mechanism type category ("
                + contactMechanismTypeCategory.code()
                + ")");
    }
  }

  /**
   * Returns the code value identifying for the contact mechanism type category.
   *
   * @return the code value identifying for the contact mechanism type category
   */
  @JsonValue
  public String code() {
    return code;
  }

  /**
   * Returns the description for the contact mechanism type category.
   *
   * @return the description for the contact mechanism type category
   */
  public String description() {
    return description;
  }
}
