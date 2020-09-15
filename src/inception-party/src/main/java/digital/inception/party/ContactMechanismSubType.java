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
 * The <code>ContactMechanismSubType</code> enumeration defines the possible contact mechanism sub
 * types.
 *
 * <p>NOTE: The numeric codes for the contact mechanism sub types fall into ranges based on the
 * contact mechanism type they are associated with, e.g. the numeric code for the phone number
 * contact mechanism type is 1 and all contact mechanism subtypes associated with this contact
 * mechanism type start with 1; 101 = Mobile Phone Number, 102 = Home Phone Number, etc.
 *
 * @author Marcus Portmann
 */
@Schema(description = "The contact mechanism sub type")
@XmlEnum
@XmlType(name = "ContactMechanismSubType", namespace = "http://party.inception.digital")
public enum ContactMechanismSubType {
  @XmlEnumValue("MobilePhoneNumber")
  MOBILE_PHONE_NUMBER(
      "mobile_phone_number",
      ContactMechanismType.PHONE_NUMBER,
      PartyType.PERSON,
      "Mobile Phone Number"),
  @XmlEnumValue("HomePhoneNumber")
  HOME_PHONE_NUMBER(
      "home_phone_number",
      ContactMechanismType.PHONE_NUMBER,
      PartyType.PERSON,
      "Home Phone Number"),
  @XmlEnumValue("WorkPhoneNumber")
  WORK_PHONE_NUMBER(
      "work_phone_number",
      ContactMechanismType.PHONE_NUMBER,
      PartyType.PERSON,
      "Work Phone Number"),
  @XmlEnumValue("SchoolPhoneNumber")
  SCHOOL_PHONE_NUMBER(
      "school_phone_number",
      ContactMechanismType.PHONE_NUMBER,
      PartyType.PERSON,
      "School Phone Number"),
  @XmlEnumValue("PagerPhoneNumber")
  PAGER_PHONE_NUMBER(
      "pager_phone_number",
      ContactMechanismType.PHONE_NUMBER,
      PartyType.PERSON,
      "Pager Phone Number"),
  @XmlEnumValue("OtherPhoneNumber")
  OTHER_PHONE_NUMBER(
      "other_phone_number",
      ContactMechanismType.PHONE_NUMBER,
      PartyType.PERSON,
      "Other Phone Number"),
  @XmlEnumValue("MainPhoneNumber")
  MAIN_PHONE_NUMBER(
      "main_phone_number",
      ContactMechanismType.PHONE_NUMBER,
      PartyType.ORGANIZATION,
      "Main Phone Number"),
  @XmlEnumValue("HomeFaxNumber")
  HOME_FAX_NUMBER(
      "home_fax_number", ContactMechanismType.FAX_NUMBER, PartyType.PERSON, "Home Fax Number"),
  @XmlEnumValue("WorkFaxNumber")
  WORK_FAX_NUMBER(
      "work_fax_number", ContactMechanismType.FAX_NUMBER, PartyType.PERSON, "Work Fax Number"),
  @XmlEnumValue("OtherFaxNumber")
  OTHER_FAX_NUMBER(
      "other_fax_number", ContactMechanismType.FAX_NUMBER, PartyType.PERSON, "Other Fax Number"),
  @XmlEnumValue("MainFaxNumber")
  MAIN_FAX_NUMBER(
      "main_fax_number",
      ContactMechanismType.FAX_NUMBER,
      PartyType.ORGANIZATION,
      "Main Fax Number"),
  @XmlEnumValue("PersonalEmailAddress")
  PERSONAL_EMAIL_ADDRESS(
      "personal_email_address",
      ContactMechanismType.EMAIL_ADDRESS,
      PartyType.PERSON,
      "Personal E-mail Address"),
  @XmlEnumValue("WorkEmailAddress")
  WORK_EMAIL_ADDRESS(
      "work_email_address",
      ContactMechanismType.EMAIL_ADDRESS,
      PartyType.PERSON,
      "Work E-mail Address"),
  @XmlEnumValue("SchoolEmailAddress")
  SCHOOL_EMAIL_ADDRESS(
      "school_email_address",
      ContactMechanismType.EMAIL_ADDRESS,
      PartyType.PERSON,
      "School E-mail Address"),

  @XmlEnumValue("OtherEmailAddress")
  OTHER_EMAIL_ADDRESS(
      "other_email_address",
      ContactMechanismType.EMAIL_ADDRESS,
      PartyType.PERSON,
      "Other E-mail Address"),

  @XmlEnumValue("MainEmailAddress")
  MAIN_EMAIL_ADDRESS(
      "main_email_address",
      ContactMechanismType.EMAIL_ADDRESS,
      PartyType.ORGANIZATION,
      "Main E-mail Address"),

  @XmlEnumValue("WhatsAppUserID")
  WHATSAPP_USER_ID(
      "whatsapp_user_id", ContactMechanismType.SOCIAL_MEDIA, PartyType.PERSON, "WhatsApp User ID"),

  @XmlEnumValue("TwitterID")
  TWITTER_ID("twitter_id", ContactMechanismType.SOCIAL_MEDIA, PartyType.PERSON, "Twitter ID");

  private final String code;

  private final String description;

  private final PartyType partyType;

  private final ContactMechanismType type;

  ContactMechanismSubType(
      String code, ContactMechanismType type, PartyType partyType, String description) {
    this.code = code;
    this.type = type;
    this.partyType = partyType;
    this.description = description;
  }

  /**
   * Returns the contact mechanism sub type given by the specified code value.
   *
   * @param code the code value identifying the contact mechanism sub type
   * @return the contact mechanism sub type given by the specified code value
   */
  @JsonCreator
  public static ContactMechanismSubType fromCode(String code) {
    switch (code) {
      case "mobile_phone_number":
        return ContactMechanismSubType.MOBILE_PHONE_NUMBER;

      case "home_phone_number":
        return ContactMechanismSubType.HOME_PHONE_NUMBER;

      case "work_phone_number":
        return ContactMechanismSubType.WORK_PHONE_NUMBER;

      case "school_phone_number":
        return ContactMechanismSubType.SCHOOL_PHONE_NUMBER;

      case "pager_phone_number":
        return ContactMechanismSubType.PAGER_PHONE_NUMBER;

      case "other_phone_number":
        return ContactMechanismSubType.OTHER_PHONE_NUMBER;

      case "main_phone_number":
        return ContactMechanismSubType.MAIN_PHONE_NUMBER;

      case "home_fax_number":
        return ContactMechanismSubType.HOME_FAX_NUMBER;

      case "work_fax_number":
        return ContactMechanismSubType.WORK_FAX_NUMBER;

      case "other_fax_number":
        return ContactMechanismSubType.OTHER_FAX_NUMBER;

      case "main_fax_number":
        return ContactMechanismSubType.MAIN_FAX_NUMBER;

      case "personal_email_address":
        return ContactMechanismSubType.PERSONAL_EMAIL_ADDRESS;

      case "work_email_address":
        return ContactMechanismSubType.WORK_EMAIL_ADDRESS;

      case "school_email_address":
        return ContactMechanismSubType.SCHOOL_EMAIL_ADDRESS;

      case "other_email_address":
        return ContactMechanismSubType.OTHER_EMAIL_ADDRESS;

      case "main_email_address":
        return ContactMechanismSubType.MAIN_EMAIL_ADDRESS;

      case "whatsapp_user_id":
        return ContactMechanismSubType.WHATSAPP_USER_ID;

      case "twitter_id":
        return ContactMechanismSubType.TWITTER_ID;

      default:
        throw new RuntimeException(
            "Failed to determine the contact mechanism sub type with the invalid code ("
                + code
                + ")");
    }
  }

  /**
   * Returns the contact mechanism sub type for the specified numeric code.
   *
   * @param numericCode the numeric code identifying the contact mechanism sub type
   * @return the contact mechanism sub type given by the specified numeric code value
   */
  public static ContactMechanismSubType fromNumericCode(int numericCode) {
    switch (numericCode) {
      case 101:
        return ContactMechanismSubType.MOBILE_PHONE_NUMBER;
      case 102:
        return ContactMechanismSubType.HOME_PHONE_NUMBER;
      case 103:
        return ContactMechanismSubType.WORK_PHONE_NUMBER;
      case 104:
        return ContactMechanismSubType.SCHOOL_PHONE_NUMBER;
      case 105:
        return ContactMechanismSubType.PAGER_PHONE_NUMBER;
      case 106:
        return ContactMechanismSubType.OTHER_PHONE_NUMBER;
      case 110:
        return ContactMechanismSubType.MAIN_PHONE_NUMBER;
      case 201:
        return ContactMechanismSubType.HOME_FAX_NUMBER;
      case 202:
        return ContactMechanismSubType.WORK_FAX_NUMBER;
      case 203:
        return ContactMechanismSubType.OTHER_FAX_NUMBER;
      case 210:
        return ContactMechanismSubType.MAIN_FAX_NUMBER;
      case 301:
        return ContactMechanismSubType.PERSONAL_EMAIL_ADDRESS;
      case 302:
        return ContactMechanismSubType.WORK_EMAIL_ADDRESS;
      case 303:
        return ContactMechanismSubType.SCHOOL_EMAIL_ADDRESS;
      case 304:
        return ContactMechanismSubType.OTHER_EMAIL_ADDRESS;
      case 310:
        return ContactMechanismSubType.MAIN_EMAIL_ADDRESS;
      case 401:
        return ContactMechanismSubType.WHATSAPP_USER_ID;
      case 402:
        return ContactMechanismSubType.TWITTER_ID;
      default:
        throw new RuntimeException(
            "Failed to determine the contact mechanism sub type for the numeric code ("
                + numericCode
                + ")");
    }
  }

  /**
   * Returns the numeric code for the contact mechanism sub type.
   *
   * @param contactMechanismSubType the contact mechanism sub type
   * @return the numeric code for the contact mechanism sub type
   */
  public static int toNumericCode(ContactMechanismSubType contactMechanismSubType) {
    switch (contactMechanismSubType) {
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
        return 106;
      case MAIN_PHONE_NUMBER:
        return 110;
      case HOME_FAX_NUMBER:
        return 201;
      case WORK_FAX_NUMBER:
        return 202;
      case OTHER_FAX_NUMBER:
        return 203;
      case MAIN_FAX_NUMBER:
        return 210;
      case PERSONAL_EMAIL_ADDRESS:
        return 301;
      case WORK_EMAIL_ADDRESS:
        return 302;
      case SCHOOL_EMAIL_ADDRESS:
        return 303;
      case OTHER_EMAIL_ADDRESS:
        return 304;
      case MAIN_EMAIL_ADDRESS:
        return 310;
      case WHATSAPP_USER_ID:
        return 401;
      case TWITTER_ID:
        return 402;
      default:
        throw new RuntimeException(
            "Failed to determine the numeric code for the contact mechanism sub type ("
                + contactMechanismSubType.code()
                + ")");
    }
  }

  /**
   * Returns the code value for the contact mechanism sub type.
   *
   * @return the code value for the contact mechanism sub type
   */
  @JsonValue
  public String code() {
    return code;
  }

  /**
   * Returns the description for the contact mechanism sub type.
   *
   * @return the description for the contact mechanism sub type
   */
  public String description() {
    return description;
  }

  /**
   * Returns the contact mechanism type the contact mechanism sub type is associated with.
   *
   * @return the contact mechanism type the contact mechanism sub type is associated with
   */
  public ContactMechanismType type() {
    return type;
  }
}
