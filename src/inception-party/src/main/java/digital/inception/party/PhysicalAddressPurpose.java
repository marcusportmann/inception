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
import java.util.Arrays;
import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;

/**
 * The <b>PhysicalAddressPurpose</b> enumeration defines the possible physical address purposes.
 *
 * @author Marcus Portmann
 */
@Schema(description = "The physical address purpose")
@XmlEnum
@XmlType(name = "PhysicalAddressPurpose", namespace = "http://party.inception.digital")
public enum PhysicalAddressPurpose {
  @XmlEnumValue("Billing")
  BILLING("billing", new PartyType[] {PartyType.ORGANIZATION, PartyType.PERSON}, "Billing Address"),
  @XmlEnumValue("Business")
  BUSINESS("business", new PartyType[] {PartyType.ORGANIZATION}, "Business Address"),
  @XmlEnumValue("Correspondence")
  CORRESPONDENCE(
      "correspondence",
      new PartyType[] {PartyType.ORGANIZATION, PartyType.PERSON},
      "Correspondence Address"),
  @XmlEnumValue("Delivery")
  DELIVERY(
      "delivery", new PartyType[] {PartyType.ORGANIZATION, PartyType.PERSON}, "Delivery Address"),
  @XmlEnumValue("Home")
  HOME("home", new PartyType[] {PartyType.PERSON}, "Home Address"),
  @XmlEnumValue("Main")
  MAIN("main", new PartyType[] {PartyType.ORGANIZATION}, "Main Address"),
  @XmlEnumValue("Permanent")
  PERMANENT("permanent", new PartyType[] {PartyType.PERSON}, "Permanent Address"),
  @XmlEnumValue("RegisteredOffice")
  REGISTERED_OFFICE(
      "registered_office", new PartyType[] {PartyType.ORGANIZATION}, "Registered Office Address"),
  @XmlEnumValue("Residential")
  RESIDENTIAL("residential", new PartyType[] {PartyType.PERSON}, "Residential Address"),
  @XmlEnumValue("Service")
  SERVICE("service", new PartyType[] {PartyType.ORGANIZATION, PartyType.PERSON}, "Service Address"),
  @XmlEnumValue("SoleTrader")
  SOLE_TRADER("sole_trader", new PartyType[] {PartyType.PERSON}, "Sole Trader Address"),
  @XmlEnumValue("Temporary")
  TEMPORARY("temporary", new PartyType[] {PartyType.PERSON}, "Temporary Address"),
  @XmlEnumValue("Work")
  WORK("work", new PartyType[] {PartyType.PERSON}, "Work Address");

  private final String code;

  private final String description;

  private final PartyType[] partyTypes;

  PhysicalAddressPurpose(String code, PartyType[] partyTypes, String description) {
    this.code = code;
    this.partyTypes = partyTypes;
    this.description = description;
  }

  /**
   * Returns the physical address purpose given by the specified code value.
   *
   * @param code the code for the physical address purpose
   * @return the physical address purpose given by the specified code value
   */
  @JsonCreator
  public static PhysicalAddressPurpose fromCode(String code) {
    switch (code) {
      case "billing":
        return PhysicalAddressPurpose.BILLING;

      case "business":
        return PhysicalAddressPurpose.BUSINESS;

      case "correspondence":
        return PhysicalAddressPurpose.CORRESPONDENCE;

      case "delivery":
        return PhysicalAddressPurpose.DELIVERY;

      case "home":
        return PhysicalAddressPurpose.HOME;

      case "main":
        return PhysicalAddressPurpose.MAIN;

      case "permanent":
        return PhysicalAddressPurpose.PERMANENT;

      case "registered_office":
        return PhysicalAddressPurpose.REGISTERED_OFFICE;

      case "residential":
        return PhysicalAddressPurpose.RESIDENTIAL;

      case "service":
        return PhysicalAddressPurpose.SERVICE;

      case "sole_trader":
        return PhysicalAddressPurpose.SOLE_TRADER;

      case "temporary":
        return PhysicalAddressPurpose.TEMPORARY;

      case "work":
        return PhysicalAddressPurpose.WORK;

      default:
        throw new RuntimeException(
            "Failed to determine the physical address purpose with the invalid code ("
                + code
                + ")");
    }
  }

  /**
   * Returns the physical address purpose for the specified numeric code.
   *
   * @param numericCode the numeric code for the physical address purpose
   * @return the physical address purpose given by the specified numeric code value
   */
  public static PhysicalAddressPurpose fromNumericCode(int numericCode) {
    switch (numericCode) {
      case 1:
        return PhysicalAddressPurpose.BILLING;
      case 2:
        return PhysicalAddressPurpose.BUSINESS;
      case 3:
        return PhysicalAddressPurpose.CORRESPONDENCE;
      case 4:
        return PhysicalAddressPurpose.DELIVERY;
      case 5:
        return PhysicalAddressPurpose.HOME;
      case 6:
        return PhysicalAddressPurpose.MAIN;
      case 7:
        return PhysicalAddressPurpose.PERMANENT;
      case 8:
        return PhysicalAddressPurpose.REGISTERED_OFFICE;
      case 9:
        return PhysicalAddressPurpose.RESIDENTIAL;
      case 10:
        return PhysicalAddressPurpose.SERVICE;
      case 11:
        return PhysicalAddressPurpose.SOLE_TRADER;
      case 12:
        return PhysicalAddressPurpose.TEMPORARY;
      case 13:
        return PhysicalAddressPurpose.WORK;

      default:
        throw new RuntimeException(
            "Failed to determine the physical address purpose for the numeric code ("
                + numericCode
                + ")");
    }
  }

  /**
   * Returns the numeric code for the physical address purpose.
   *
   * @param contactMechanismPurpose the physical address purpose
   * @return the numeric code for the physical address purpose
   */
  public static int toNumericCode(PhysicalAddressPurpose contactMechanismPurpose) {
    switch (contactMechanismPurpose) {
      case BILLING:
        return 1;
      case BUSINESS:
        return 2;
      case CORRESPONDENCE:
        return 3;
      case DELIVERY:
        return 4;
      case HOME:
        return 5;
      case MAIN:
        return 6;
      case PERMANENT:
        return 7;
      case REGISTERED_OFFICE:
        return 8;
      case RESIDENTIAL:
        return 9;
      case SERVICE:
        return 10;
      case SOLE_TRADER:
        return 11;
      case TEMPORARY:
        return 12;
      case WORK:
        return 13;
      default:
        throw new RuntimeException(
            "Failed to determine the numeric code for the physical address purpose ("
                + contactMechanismPurpose.code()
                + ")");
    }
  }

  /**
   * Returns the code value for the physical address purpose.
   *
   * @return the code value for the physical address purpose
   */
  @JsonValue
  public String code() {
    return code;
  }

  /**
   * Returns the description for the physical address purpose.
   *
   * @return the description for the physical address purpose
   */
  public String description() {
    return description;
  }

  /**
   * Returns whether the physical address purpose is valid for the party type.
   *
   * @param partyType the party type
   * @return <b>true</b> if the physical address purpose is valid for the party type or <b>false</b>
   *     otherwise
   */
  public boolean isValidForPartyType(PartyType partyType) {
    return Arrays.stream(partyTypes).anyMatch(validPartyType -> validPartyType.equals(partyType));
  }

  /**
   * Returns the party types the physical address purpose is associated with.
   *
   * @return the party types the physical address purpose is associated with
   */
  public PartyType[] partyTypes() {
    return partyTypes;
  }
}
