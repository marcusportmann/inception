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
 * The <code>PhysicalAddressPurpose</code> enumeration defines the possible physical address
 * purposes.
 *
 * @author Marcus Portmann
 */
@Schema(description = "The physical address purpose")
@XmlEnum
@XmlType(name = "PhysicalAddressPurpose", namespace = "http://party.inception.digital")
public enum PhysicalAddressPurpose {
  @XmlEnumValue("Billing")
  BILLING("billing", PartyType.PERSON, "Billing Address"),
  @XmlEnumValue("Delivery")
  DELIVERY("delivery", PartyType.PERSON, "Delivery Address"),
  @XmlEnumValue("Main")
  MAIN("main", PartyType.ORGANIZATION, "Main Address"),
  @XmlEnumValue("Postal")
  POSTAL("postal", PartyType.PERSON, "Postal Address"),
  @XmlEnumValue("Residential")
  RESIDENTIAL("residential", PartyType.PERSON, "Residential Address");

  private final String code;

  private final String description;

  private final PartyType partyType;

  PhysicalAddressPurpose(String code, PartyType partyType, String description) {
    this.code = code;
    this.partyType = partyType;
    this.description = description;
  }

  /**
   * Returns the physical address purpose given by the specified code value.
   *
   * @param code the code value identifying the physical address purpose
   * @return the physical address purpose given by the specified code value
   */
  @JsonCreator
  public static PhysicalAddressPurpose fromCode(String code) {
    switch (code) {
      case "billing":
        return PhysicalAddressPurpose.BILLING;

      case "delivery":
        return PhysicalAddressPurpose.DELIVERY;

      case "main":
        return PhysicalAddressPurpose.MAIN;

      case "postal":
        return PhysicalAddressPurpose.POSTAL;

      case "residential":
        return PhysicalAddressPurpose.RESIDENTIAL;

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
   * @param numericCode the numeric code identifying the physical address purpose
   * @return the physical address purpose given by the specified numeric code value
   */
  public static PhysicalAddressPurpose fromNumericCode(int numericCode) {
    switch (numericCode) {
      case 1:
        return PhysicalAddressPurpose.BILLING;
      case 2:
        return PhysicalAddressPurpose.DELIVERY;
      case 3:
        return PhysicalAddressPurpose.POSTAL;
      case 4:
        return PhysicalAddressPurpose.RESIDENTIAL;
      case 10:
        return PhysicalAddressPurpose.MAIN;

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
      case DELIVERY:
        return 2;
      case POSTAL:
        return 3;
      case RESIDENTIAL:
        return 4;
      case MAIN:
        return 10;
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
   * Returns the party type the physical address purpose is associated with.
   *
   * @return the party type the physical address purpose is associated with
   */
  public PartyType partyType() {
    return partyType;
  }
}
