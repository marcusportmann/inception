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
 * The <code>ContactMechanismType</code> enumeration defines the possible contact mechanism types.
 *
 * @author Marcus Portmann
 */
@Schema(description = "The contact mechanism type")
@XmlEnum
@XmlType(name = "ContactMechanismType", namespace = "http://party.inception.digital")
public enum ContactMechanismType {
  @XmlEnumValue("MobilePhoneNumber")
  MOBILE_PHONE_NUMBER(
      "mobile_phone_number", ContactMechanismTypeCategory.PHONE_NUMBERS, "Mobile Phone Number"),
  @XmlEnumValue("HomePhoneNumber")
  HOME_PHONE_NUMBER(
      "home_phone_number", ContactMechanismTypeCategory.PHONE_NUMBERS, "Home Phone Number"),
  @XmlEnumValue("WorkPhoneNumber")
  WORK_PHONE_NUMBER(
      "work_phone_number", ContactMechanismTypeCategory.PHONE_NUMBERS, "Work Phone Number"),
  @XmlEnumValue("SchoolPhoneNumber")
  SCHOOL_PHONE_NUMBER(
      "school_phone_number", ContactMechanismTypeCategory.PHONE_NUMBERS, "School Phone Number"),
  @XmlEnumValue("PagerPhoneNumber")
  PAGER_PHONE_NUMBER(
      "pager_phone_number", ContactMechanismTypeCategory.PHONE_NUMBERS, "Pager Phone Number"),
  @XmlEnumValue("OtherPhoneNumber")
  OTHER_PHONE_NUMBER(
      "other_phone_number", ContactMechanismTypeCategory.PHONE_NUMBERS, "Other Phone Number"),
  @XmlEnumValue("HomeFaxNumber")
  HOME_FAX_NUMBER("home_fax_number", ContactMechanismTypeCategory.FAX_NUMBERS, "Home Fax Number"),
  @XmlEnumValue("WorkFaxNumber")
  WORK_FAX_NUMBER("work_fax_number", ContactMechanismTypeCategory.FAX_NUMBERS, "Work Fax Number"),
  @XmlEnumValue("OtherFaxNumber")
  OTHER_FAX_NUMBER(
      "other_fax_number", ContactMechanismTypeCategory.FAX_NUMBERS, "Other Fax Number"),
  @XmlEnumValue("PersonalEmailAddress")
  PERSONAL_EMAIL_ADDRESS(
      "personal_email_address",
      ContactMechanismTypeCategory.EMAIL_ADDRESSES,
      "Personal E-mail Address"),
  @XmlEnumValue("HomeEmailAddress")
  HOME_EMAIL_ADDRESS(
      "home_email_address", ContactMechanismTypeCategory.EMAIL_ADDRESSES, "Home E-mail Address"),
  @XmlEnumValue("WorkEmailAddress")
  WORK_EMAIL_ADDRESS(
      "work_email_address", ContactMechanismTypeCategory.EMAIL_ADDRESSES, "Work E-mail Address"),
  @XmlEnumValue("SchoolEmailAddress")
  SCHOOL_EMAIL_ADDRESS(
      "school_email_address",
      ContactMechanismTypeCategory.EMAIL_ADDRESSES,
      "School E-mail Address"),
  @XmlEnumValue("OtherEmailAddress")
  OTHER_EMAIL_ADDRESS(
      "other_email_address", ContactMechanismTypeCategory.EMAIL_ADDRESSES, "Other E-mail Address"),
  @XmlEnumValue("WhatsAppUserID")
  WHATSAPP_USER_ID(
      "whatsapp_user_id", ContactMechanismTypeCategory.SOCIAL_MEDIA, "WhatsApp User ID"),
  @XmlEnumValue("TwitterID")
  TWITTER_ID("twitter_id", ContactMechanismTypeCategory.SOCIAL_MEDIA, "Twitter ID");

  private final ContactMechanismTypeCategory category;

  private final String code;

  private final String description;

  ContactMechanismType(String code, ContactMechanismTypeCategory category, String description) {
    this.code = code;
    this.category = category;
    this.description = description;
  }

  /**
   * Returns the contact mechanism type given by the specified code value.
   *
   * @param code the code value identifying the contact mechanism type
   * @return the contact mechanism type given by the specified code value
   */
  @JsonCreator
  public static ContactMechanismType fromCode(String code) {
    switch (code) {
      case "mobile_phone_number":
        return ContactMechanismType.MOBILE_PHONE_NUMBER;

      case "home_phone_number":
        return ContactMechanismType.HOME_PHONE_NUMBER;

      case "work_phone_number":
        return ContactMechanismType.WORK_PHONE_NUMBER;

      case "school_phone_number":
        return ContactMechanismType.SCHOOL_PHONE_NUMBER;

      case "pager_phone_number":
        return ContactMechanismType.PAGER_PHONE_NUMBER;

      case "other_phone_number":
        return ContactMechanismType.OTHER_PHONE_NUMBER;

      case "home_fax_number":
        return ContactMechanismType.HOME_FAX_NUMBER;

      case "work_fax_number":
        return ContactMechanismType.WORK_FAX_NUMBER;

      case "other_fax_number":
        return ContactMechanismType.OTHER_FAX_NUMBER;

      case "personal_email_address":
        return ContactMechanismType.PERSONAL_EMAIL_ADDRESS;

      case "home_email_address":
        return ContactMechanismType.HOME_EMAIL_ADDRESS;

      case "work_email_address":
        return ContactMechanismType.WORK_EMAIL_ADDRESS;

      case "school_email_address":
        return ContactMechanismType.SCHOOL_EMAIL_ADDRESS;

      case "other_email_address":
        return ContactMechanismType.OTHER_EMAIL_ADDRESS;

      case "whatsapp_user_id":
        return ContactMechanismType.WHATSAPP_USER_ID;

      case "twitter_id":
        return ContactMechanismType.TWITTER_ID;

      default:
        throw new RuntimeException(
            "Failed to determine the contact mechanism type with the invalid code ("
                + code
                + ")");
    }
  }

  /**
   * Returns the contact mechanism type for the specified numeric code.
   *
   * @param numericCode the numeric code identifying the contact mechanism type
   * @return the contact mechanism type given by the specified numeric code value
   */
  public static ContactMechanismType fromNumericCode(int numericCode) {
    switch (numericCode) {
      case 101:
        return ContactMechanismType.MOBILE_PHONE_NUMBER;
      case 102:
        return ContactMechanismType.HOME_PHONE_NUMBER;
      case 103:
        return ContactMechanismType.WORK_PHONE_NUMBER;
      case 104:
        return ContactMechanismType.SCHOOL_PHONE_NUMBER;
      case 105:
        return ContactMechanismType.PAGER_PHONE_NUMBER;
      case 199:
        return ContactMechanismType.OTHER_PHONE_NUMBER;
      case 201:
        return ContactMechanismType.HOME_FAX_NUMBER;
      case 202:
        return ContactMechanismType.WORK_FAX_NUMBER;
      case 299:
        return ContactMechanismType.OTHER_FAX_NUMBER;
      case 301:
        return ContactMechanismType.PERSONAL_EMAIL_ADDRESS;
      case 302:
        return ContactMechanismType.HOME_EMAIL_ADDRESS;
      case 303:
        return ContactMechanismType.WORK_EMAIL_ADDRESS;
      case 304:
        return ContactMechanismType.SCHOOL_EMAIL_ADDRESS;
      case 399:
        return ContactMechanismType.OTHER_EMAIL_ADDRESS;
      case 401:
        return ContactMechanismType.WHATSAPP_USER_ID;
      case 402:
        return ContactMechanismType.TWITTER_ID;
      default:
        throw new RuntimeException(
            "Failed to determine the contact mechanism type for the numeric code ("
                + numericCode
                + ")");
    }
  }

  /**
   * Returns the numeric code for the contact mechanism type.
   *
   * @param contactMechanismType the contact mechanism type
   * @return the numeric code for the contact mechanism type
   */
  public static int toNumericCode(ContactMechanismType contactMechanismType) {
    switch (contactMechanismType) {
      case MOBILE_PHONE_NUMBER:
        return 101;
      case HOME_PHONE_NUMBER:
        return 102;
      case WORK_PHONE_NUMBER:
        return 103;
      case SCHOOL_PHONE_NUMBER:
        return 104;
      case PAGER_PHONE_NUMBER:
        return 105;
      case OTHER_PHONE_NUMBER:
        return 199;
      case HOME_FAX_NUMBER:
        return 201;
      case WORK_FAX_NUMBER:
        return 202;
      case OTHER_FAX_NUMBER:
        return 299;
      case PERSONAL_EMAIL_ADDRESS:
        return 301;
      case HOME_EMAIL_ADDRESS:
        return 302;
      case WORK_EMAIL_ADDRESS:
        return 303;
      case SCHOOL_EMAIL_ADDRESS:
        return 304;
      case OTHER_EMAIL_ADDRESS:
        return 399;
      case WHATSAPP_USER_ID:
        return 401;
      case TWITTER_ID:
        return 402;
      default:
        throw new RuntimeException(
            "Failed to determine the numeric code for the contact mechanism type ("
                + contactMechanismType.code()
                + ")");
    }
  }

  /**
   * Returns the contact mechanism type category the contact mechanism type is associated with.
   *
   * @return the contact mechanism type category the contact mechanism type is associated with
   */
  public ContactMechanismTypeCategory category() {
    return category;
  }

  /**
   * Returns the code value for the contact mechanism type.
   *
   * @return the code value for the contact mechanism type
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
