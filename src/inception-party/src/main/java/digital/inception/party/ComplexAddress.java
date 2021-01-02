///*
// * Copyright 2020 Marcus Portmann
// *
// * Licensed under the Apache License, Version 2.0 (the "License");
// * you may not use this file except in compliance with the License.
// * You may obtain a copy of the License at
// *
// *   http://www.apache.org/licenses/LICENSE-2.0
// *
// * Unless required by applicable law or agreed to in writing, software
// * distributed under the License is distributed on an "AS IS" BASIS,
// * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// * See the License for the specific language governing permissions and
// * limitations under the License.
// */
//
//package digital.inception.party;
//
//import com.fasterxml.jackson.annotation.JsonProperty;
//import io.swagger.v3.oas.annotations.media.Schema;
//import java.io.Serializable;
//import javax.persistence.Embeddable;
//import javax.validation.constraints.NotNull;
//import javax.validation.constraints.Pattern;
//import javax.validation.constraints.Size;
//import javax.xml.bind.annotation.XmlElement;
//
///**
// * The <code>ComplexAddress</code> class holds the information for a complex address.
// *
// * @author Marcus Portmann
// */
//@Embeddable
//public class ComplexAddress implements Serializable {
//
//  private static final long serialVersionUID = 1000000;
//
//  /** The complex name for the complex address. */
//  @Schema(description = "The complex name for the complex address", required = true)
//  @JsonProperty(required = true)
//  @XmlElement(name = "Name", required = true)
//  @NotNull
//  @Size(min = 1, max = 50)
//  @Pattern(
//      message = "{digital.inception.party.ComplexAddress.Name.Pattern.message}",
//      regexp = "^[\\pL\\pN-' ]+$")
//  private String name;
//
//  /** The unit number for the complex address. */
//  @Schema(description = "The unit number for the complex address", required = true)
//  @JsonProperty(required = true)
//  @XmlElement(name = "UnitNumber", required = true)
//  @NotNull
//  @Size(min = 1, max = 20)
//  @Pattern(
//      message = "{digital.inception.party.ComplexAddress.UnitNumber.Pattern.message}",
//      regexp = "^[\\pL\\pN-' ]+$")
//  private String unitNumber;
//
//  /** Constructs a new <code>ComplexAddress</code>. */
//  public ComplexAddress() {}
//
//  /**
//   * Constructs a new <code>ComplexAddress</code>.
//   *
//   * @param name the complex name for the complex address
//   * @param unitNumber the unit number for the complex address
//   */
//  public ComplexAddress(String name, String unitNumber) {
//    this.name = name;
//    this.unitNumber = unitNumber;
//  }
//
//  /**
//   * Returns the complex name for the complex address.
//   *
//   * @return the complex name for the complex address
//   */
//  public String getName() {
//    return name;
//  }
//
//  /**
//   * Returns the unit number for the complex address.
//   *
//   * @return the unit number for the complex address
//   */
//  public String getUnitNumber() {
//    return unitNumber;
//  }
//
//  /**
//   * Set the complex name for the complex address.
//   *
//   * @param name the complex name for the complex address
//   */
//  public void setName(String name) {
//    this.name = name;
//  }
//
//  /**
//   * Set the unit number for the complex address.
//   *
//   * @param unitNumber the unit number for the complex address
//   */
//  public void setUnitNumber(String unitNumber) {
//    this.unitNumber = unitNumber;
//  }
//}
