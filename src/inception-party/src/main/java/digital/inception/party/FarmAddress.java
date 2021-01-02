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
// * The <code>FarmAddress</code> class holds the information for a farm address.
// *
// * @author Marcus Portmann
// */
//@Embeddable
//public class FarmAddress implements Serializable {
//
//  private static final long serialVersionUID = 1000000;
//
//  /** The farm description for the farm address. */
//  @Schema(description = "The farm description for the farm address")
//  @JsonProperty
//  @XmlElement(name = "Description")
//  @Size(max = 50)
//  @Pattern(
//      message = "{digital.inception.party.FarmAddress.Description.Pattern.message}",
//      regexp = "^[\\pL\\pN-' ]*$")
//  private String description;
//
//  /** The farm name for the farm address. */
//  @Schema(description = "The farm name for the farm address")
//  @JsonProperty
//  @XmlElement(name = "Name")
//  @Size(max = 50)
//  @Pattern(
//      message = "{digital.inception.party.FarmAddress.Name.Pattern.message}",
//      regexp = "^[\\pL\\pN-' ]*$")
//  private String name;
//
//  /** The farm number for the farm address. */
//  @Schema(description = "The farm number for the farm address", required = true)
//  @JsonProperty(required = true)
//  @XmlElement(name = "Number", required = true)
//  @NotNull
//  @Size(min = 1, max = 50)
//  @Pattern(
//      message = "{digital.inception.party.FarmAddress.Number.Pattern.message}",
//      regexp = "^[\\pL\\pN-' ]+$")
//  private String number;
//
//  /** Constructs a new <code>FarmAddress</code>. */
//  public FarmAddress() {}
//
//  /**
//   * Constructs a new <code>FarmAddress</code>.
//   *
//   * @param number the farm number for the farm address
//   * @param name the farm name for the farm address
//   * @param description the farm description for the farm address
//   */
//  public FarmAddress(String number, String name, String description) {
//    this.number = number;
//    this.name = name;
//    this.description = description;
//  }
//
//  /**
//   * Constructs a new <code>FarmAddress</code>.
//   *
//   * @param number the farm number for the farm address
//   * @param name the farm name for the farm address
//   */
//  public FarmAddress(String number, String name) {
//    this.number = number;
//    this.name = name;
//  }
//
//  /**
//   * Constructs a new <code>FarmAddress</code>.
//   *
//   * @param number the farm number for the farm address
//   */
//  public FarmAddress(String number) {
//    this.number = number;
//  }
//
//  /**
//   * Returns the farm description for the farm address.
//   *
//   * @return the farm description for the farm address
//   */
//  public String getDescription() {
//    return description;
//  }
//
//  /**
//   * Returns the farm name for the farm address.
//   *
//   * @return the farm name for the farm address
//   */
//  public String getName() {
//    return name;
//  }
//
//  /**
//   * Returns the farm number for the farm address.
//   *
//   * @return the farm number for the farm address
//   */
//  public String getNumber() {
//    return number;
//  }
//
//  /**
//   * Set the farm description for the farm address.
//   *
//   * @param description the farm description for the farm address
//   */
//  public void setDescription(String description) {
//    this.description = description;
//  }
//
//  /**
//   * Set the farm name for the farm address.
//   *
//   * @param name the farm name for the farm address
//   */
//  public void setName(String name) {
//    this.name = name;
//  }
//
//  /**
//   * Set the farm number for the farm address.
//   *
//   * @param number the farm number for the farm address
//   */
//  public void setNumber(String number) {
//    this.number = number;
//  }
//}
