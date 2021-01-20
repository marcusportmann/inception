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
 * The <code>PhysicalAddressType</code> enumeration defines the possible physical address types.
 *
 * @author Marcus Portmann
 */
@Schema(description = "The physical address type")
@XmlEnum
@XmlType(name = "PhysicalAddressType", namespace = "http://party.inception.digital")
public enum PhysicalAddressType {
  @XmlEnumValue("Building")
  BUILDING("building", "Building"),
  @XmlEnumValue("Complex")
  COMPLEX("complex", "Complex"),
  @XmlEnumValue("Farm")
  FARM("farm", "Farm"),
  @XmlEnumValue("International")
  INTERNATIONAL("international", "International"),
  @XmlEnumValue("Postal")
  POSTAL("postal", "Postal"),
  @XmlEnumValue("Site")
  SITE("site", "Site"),
  @XmlEnumValue("Street")
  STREET("street", "Street"),
  @XmlEnumValue("Unstructured")
  UNSTRUCTURED("unstructured", "Unstructured");

  private final String code;

  private final String description;

  PhysicalAddressType(String code, String description) {
    this.code = code;
    this.description = description;
  }

  /**
   * Returns the physical address type given by the specified code value.
   *
   * @param code the code for the physical address type
   * @return the physical address type given by the specified code value
   */
  @JsonCreator
  public static PhysicalAddressType fromCode(String code) {
    switch (code) {
      case "building":
        return PhysicalAddressType.BUILDING;

      case "complex":
        return PhysicalAddressType.COMPLEX;

      case "farm":
        return PhysicalAddressType.FARM;

      case "international":
        return PhysicalAddressType.INTERNATIONAL;

      case "postal":
        return PhysicalAddressType.POSTAL;

      case "site":
        return PhysicalAddressType.SITE;

      case "street":
        return PhysicalAddressType.STREET;

      case "unstructured":
        return PhysicalAddressType.UNSTRUCTURED;

      default:
        throw new RuntimeException(
            "Failed to determine the physical address type with the invalid code (" + code + ")");
    }
  }

  /**
   * Returns the code for the physical address type.
   *
   * @return the code for the physical address type
   */
  @JsonValue
  public String code() {
    return code;
  }

  /**
   * Returns the description for the physical address type.
   *
   * @return the description for the physical address type
   */
  public String description() {
    return description;
  }
}
