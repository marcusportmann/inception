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
 * The <code>ContactMechanismPurpose</code> enumeration defines the possible contact mechanism
 * purposes.
 *
 * <p>NOTE: The numeric codes for the contact mechanism purposes fall into ranges based on the
 * contact mechanism type they are associated with, e.g. the numeric code for the phone number
 * contact mechanism type is 1 and all contact mechanism purposes associated with this contact
 * mechanism type start with 1; 101 = Mobile Phone Number, 102 = Home Phone Number, etc.
 *
 * @author Marcus Portmann
 */
@Schema(description = "The contact mechanism purpose")
@XmlEnum
@XmlType(name = "ContactMechanismPurpose", namespace = "http://party.inception.digital")
public enum ContactMechanismPurpose {
  @XmlEnumValue("PersonalMobileNumber")
  PERSONAL_MOBILE_NUMBER(
      "personal_mobile_number",
      ContactMechanismType.MOBILE_NUMBER,
      new PartyType[] {PartyType.PERSON},
      "Personal Mobile Number"),
  @XmlEnumValue("WorkMobileNumber")
  WORK_MOBILE_NUMBER(
      "work_mobile_number",
      ContactMechanismType.MOBILE_NUMBER,
      new PartyType[] {PartyType.PERSON},
      "Work Mobile Number"),
  @XmlEnumValue("OtherMobileNumber")
  OTHER_MOBILE_NUMBER(
      "other_mobile_number",
      ContactMechanismType.MOBILE_NUMBER,
      new PartyType[] {PartyType.ORGANIZATION, PartyType.PERSON},
      "Other Mobile Number"),
  @XmlEnumValue("MainMobileNumber")
  MAIN_MOBILE_NUMBER(
      "main_mobile_number",
      ContactMechanismType.MOBILE_NUMBER,
      new PartyType[] {PartyType.ORGANIZATION},
      "Main Mobile Number"),
  @XmlEnumValue("HomePhoneNumber")
  HOME_PHONE_NUMBER(
      "home_phone_number",
      ContactMechanismType.PHONE_NUMBER,
      new PartyType[] {PartyType.PERSON},
      "Home Phone Number"),
  @XmlEnumValue("WorkPhoneNumber")
  WORK_PHONE_NUMBER(
      "work_phone_number",
      ContactMechanismType.PHONE_NUMBER,
      new PartyType[] {PartyType.PERSON},
      "Work Phone Number"),
  @XmlEnumValue("SchoolPhoneNumber")
  SCHOOL_PHONE_NUMBER(
      "school_phone_number",
      ContactMechanismType.PHONE_NUMBER,
      new PartyType[] {PartyType.PERSON},
      "School Phone Number"),
  @XmlEnumValue("PagerPhoneNumber")
  PAGER_PHONE_NUMBER(
      "pager_phone_number",
      ContactMechanismType.PHONE_NUMBER,
      new PartyType[] {PartyType.PERSON},
      "Pager Phone Number"),
  @XmlEnumValue("OtherPhoneNumber")
  OTHER_PHONE_NUMBER(
      "other_phone_number",
      ContactMechanismType.PHONE_NUMBER,
      new PartyType[] {PartyType.ORGANIZATION, PartyType.PERSON},
      "Other Phone Number"),
  @XmlEnumValue("MainPhoneNumber")
  MAIN_PHONE_NUMBER(
      "main_phone_number",
      ContactMechanismType.PHONE_NUMBER,
      new PartyType[] {PartyType.ORGANIZATION},
      "Main Phone Number"),
  @XmlEnumValue("HomeFaxNumber")
  HOME_FAX_NUMBER(
      "home_fax_number",
      ContactMechanismType.FAX_NUMBER,
      new PartyType[] {PartyType.PERSON},
      "Home Fax Number"),
  @XmlEnumValue("WorkFaxNumber")
  WORK_FAX_NUMBER(
      "work_fax_number",
      ContactMechanismType.FAX_NUMBER,
      new PartyType[] {PartyType.PERSON},
      "Work Fax Number"),
  @XmlEnumValue("OtherFaxNumber")
  OTHER_FAX_NUMBER(
      "other_fax_number",
      ContactMechanismType.FAX_NUMBER,
      new PartyType[] {PartyType.ORGANIZATION, PartyType.PERSON},
      "Other Fax Number"),
  @XmlEnumValue("MainFaxNumber")
  MAIN_FAX_NUMBER(
      "main_fax_number",
      ContactMechanismType.FAX_NUMBER,
      new PartyType[] {PartyType.ORGANIZATION},
      "Main Fax Number"),
  @XmlEnumValue("PersonalEmailAddress")
  PERSONAL_EMAIL_ADDRESS(
      "personal_email_address",
      ContactMechanismType.EMAIL_ADDRESS,
      new PartyType[] {PartyType.PERSON},
      "Personal E-mail Address"),
  @XmlEnumValue("WorkEmailAddress")
  WORK_EMAIL_ADDRESS(
      "work_email_address",
      ContactMechanismType.EMAIL_ADDRESS,
      new PartyType[] {PartyType.PERSON},
      "Work E-mail Address"),
  @XmlEnumValue("SchoolEmailAddress")
  SCHOOL_EMAIL_ADDRESS(
      "school_email_address",
      ContactMechanismType.EMAIL_ADDRESS,
      new PartyType[] {PartyType.PERSON},
      "School E-mail Address"),
  @XmlEnumValue("OtherEmailAddress")
  OTHER_EMAIL_ADDRESS(
      "other_email_address",
      ContactMechanismType.EMAIL_ADDRESS,
      new PartyType[] {PartyType.ORGANIZATION, PartyType.PERSON},
      "Other E-mail Address"),
  @XmlEnumValue("MainEmailAddress")
  MAIN_EMAIL_ADDRESS(
      "main_email_address",
      ContactMechanismType.EMAIL_ADDRESS,
      new PartyType[] {PartyType.ORGANIZATION},
      "Main E-mail Address"),
  @XmlEnumValue("WhatsAppUserID")
  WHATSAPP_USER_ID(
      "whatsapp_user_id",
      ContactMechanismType.SOCIAL_MEDIA,
      new PartyType[] {PartyType.PERSON},
      "WhatsApp User ID"),
  @XmlEnumValue("TwitterID")
  TWITTER_ID(
      "twitter_id",
      ContactMechanismType.SOCIAL_MEDIA,
      new PartyType[] {PartyType.PERSON},
      "Twitter ID");

  private final String code;

  private final String description;

  private final PartyType[] partyTypes;

  private final ContactMechanismType type;

  ContactMechanismPurpose(
      String code, ContactMechanismType type, PartyType[] partyTypes, String description) {
    this.code = code;
    this.type = type;
    this.partyTypes = partyTypes;
    this.description = description;
  }

  /**
   * Returns the contact mechanism purpose given by the specified code value.
   *
   * @param code the code value identifying the contact mechanism purpose
   * @return the contact mechanism purpose given by the specified code value
   */
  @JsonCreator
  public static ContactMechanismPurpose fromCode(String code) {
    switch (code) {
      case "personal_mobile_number":
        return ContactMechanismPurpose.PERSONAL_MOBILE_NUMBER;

      case "work_mobile_number":
        return ContactMechanismPurpose.WORK_MOBILE_NUMBER;

      case "other_mobile_number":
        return ContactMechanismPurpose.OTHER_MOBILE_NUMBER;

      case "main_mobile_number":
        return ContactMechanismPurpose.MAIN_MOBILE_NUMBER;

      case "home_phone_number":
        return ContactMechanismPurpose.HOME_PHONE_NUMBER;

      case "work_phone_number":
        return ContactMechanismPurpose.WORK_PHONE_NUMBER;

      case "school_phone_number":
        return ContactMechanismPurpose.SCHOOL_PHONE_NUMBER;

      case "pager_phone_number":
        return ContactMechanismPurpose.PAGER_PHONE_NUMBER;

      case "other_phone_number":
        return ContactMechanismPurpose.OTHER_PHONE_NUMBER;

      case "main_phone_number":
        return ContactMechanismPurpose.MAIN_PHONE_NUMBER;

      case "home_fax_number":
        return ContactMechanismPurpose.HOME_FAX_NUMBER;

      case "work_fax_number":
        return ContactMechanismPurpose.WORK_FAX_NUMBER;

      case "other_fax_number":
        return ContactMechanismPurpose.OTHER_FAX_NUMBER;

      case "main_fax_number":
        return ContactMechanismPurpose.MAIN_FAX_NUMBER;

      case "personal_email_address":
        return ContactMechanismPurpose.PERSONAL_EMAIL_ADDRESS;

      case "work_email_address":
        return ContactMechanismPurpose.WORK_EMAIL_ADDRESS;

      case "school_email_address":
        return ContactMechanismPurpose.SCHOOL_EMAIL_ADDRESS;

      case "other_email_address":
        return ContactMechanismPurpose.OTHER_EMAIL_ADDRESS;

      case "main_email_address":
        return ContactMechanismPurpose.MAIN_EMAIL_ADDRESS;

      case "whatsapp_user_id":
        return ContactMechanismPurpose.WHATSAPP_USER_ID;

      case "twitter_id":
        return ContactMechanismPurpose.TWITTER_ID;

      default:
        throw new RuntimeException(
            "Failed to determine the contact mechanism purpose with the invalid code ("
                + code
                + ")");
    }
  }

  /**
   * Returns the contact mechanism purpose for the specified numeric code.
   *
   * @param numericCode the numeric code identifying the contact mechanism purpose
   * @return the contact mechanism purpose given by the specified numeric code value
   */
  public static ContactMechanismPurpose fromNumericCode(int numericCode) {
    switch (numericCode) {
      case 100:
        return ContactMechanismPurpose.PERSONAL_MOBILE_NUMBER;
      case 101:
        return ContactMechanismPurpose.WORK_MOBILE_NUMBER;
      case 102:
        return ContactMechanismPurpose.OTHER_MOBILE_NUMBER;
      case 110:
        return ContactMechanismPurpose.MAIN_MOBILE_NUMBER;
      case 200:
        return ContactMechanismPurpose.HOME_PHONE_NUMBER;
      case 201:
        return ContactMechanismPurpose.WORK_PHONE_NUMBER;
      case 202:
        return ContactMechanismPurpose.SCHOOL_PHONE_NUMBER;
      case 203:
        return ContactMechanismPurpose.PAGER_PHONE_NUMBER;
      case 204:
        return ContactMechanismPurpose.OTHER_PHONE_NUMBER;
      case 210:
        return ContactMechanismPurpose.MAIN_PHONE_NUMBER;
      case 300:
        return ContactMechanismPurpose.HOME_FAX_NUMBER;
      case 301:
        return ContactMechanismPurpose.WORK_FAX_NUMBER;
      case 302:
        return ContactMechanismPurpose.OTHER_FAX_NUMBER;
      case 310:
        return ContactMechanismPurpose.MAIN_FAX_NUMBER;
      case 400:
        return ContactMechanismPurpose.PERSONAL_EMAIL_ADDRESS;
      case 401:
        return ContactMechanismPurpose.WORK_EMAIL_ADDRESS;
      case 402:
        return ContactMechanismPurpose.SCHOOL_EMAIL_ADDRESS;
      case 403:
        return ContactMechanismPurpose.OTHER_EMAIL_ADDRESS;
      case 410:
        return ContactMechanismPurpose.MAIN_EMAIL_ADDRESS;
      case 500:
        return ContactMechanismPurpose.WHATSAPP_USER_ID;
      case 501:
        return ContactMechanismPurpose.TWITTER_ID;
      default:
        throw new RuntimeException(
            "Failed to determine the contact mechanism purpose for the numeric code ("
                + numericCode
                + ")");
    }
  }

  /**
   * Returns the numeric code for the contact mechanism purpose.
   *
   * @param contactMechanismPurpose the contact mechanism purpose
   * @return the numeric code for the contact mechanism purpose
   */
  public static int toNumericCode(ContactMechanismPurpose contactMechanismPurpose) {
    switch (contactMechanismPurpose) {
      case PERSONAL_MOBILE_NUMBER:
        return 100;
      case WORK_MOBILE_NUMBER:
        return 101;
      case OTHER_MOBILE_NUMBER:
        return 102;
      case MAIN_MOBILE_NUMBER:
        return 110;
      case HOME_PHONE_NUMBER:
        return 200;
      case WORK_PHONE_NUMBER:
        return 201;
      case SCHOOL_PHONE_NUMBER:
        return 202;
      case PAGER_PHONE_NUMBER:
        return 203;
      case OTHER_PHONE_NUMBER:
        return 204;
      case MAIN_PHONE_NUMBER:
        return 210;
      case HOME_FAX_NUMBER:
        return 300;
      case WORK_FAX_NUMBER:
        return 301;
      case OTHER_FAX_NUMBER:
        return 302;
      case MAIN_FAX_NUMBER:
        return 310;
      case PERSONAL_EMAIL_ADDRESS:
        return 400;
      case WORK_EMAIL_ADDRESS:
        return 401;
      case SCHOOL_EMAIL_ADDRESS:
        return 402;
      case OTHER_EMAIL_ADDRESS:
        return 403;
      case MAIN_EMAIL_ADDRESS:
        return 410;
      case WHATSAPP_USER_ID:
        return 500;
      case TWITTER_ID:
        return 501;
      default:
        throw new RuntimeException(
            "Failed to determine the numeric code for the contact mechanism purpose ("
                + contactMechanismPurpose.code()
                + ")");
    }
  }

  /**
   * Returns the code value for the contact mechanism purpose.
   *
   * @return the code value for the contact mechanism purpose
   */
  @JsonValue
  public String code() {
    return code;
  }

  /**
   * Returns the description for the contact mechanism purpose.
   *
   * @return the description for the contact mechanism purpose
   */
  public String description() {
    return description;
  }

  /**
   * Returns the party types the contact mechanism purpose is associated with.
   *
   * @return the party types the contact mechanism purpose is associated with
   */
  public PartyType[] partyTypes() {
    return partyTypes;
  }

  /**
   * Returns the contact mechanism type the contact mechanism purpose is associated with.
   *
   * @return the contact mechanism type the contact mechanism purpose is associated with
   */
  public ContactMechanismType type() {
    return type;
  }
}
