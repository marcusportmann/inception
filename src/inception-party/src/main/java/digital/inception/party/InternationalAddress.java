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
// * The <code>InternationalAddress</code> class holds the information for an international address.
// *
// * @author Marcus Portmann
// */
//@Embeddable
//public class InternationalAddress implements Serializable {
//
//  /** The address line 1 for the international address. */
//  @Schema(description = "The address line 1 for the international address", required = true)
//  @JsonProperty(required = true)
//  @XmlElement(name = "Line1", required = true)
//  @NotNull
//  @Size(max = 100)
//  @Pattern(
//      message = "{digital.inception.party.InternationalAddress.Line1.Pattern.message}",
//      regexp = "^[\\pL\\pN-' ]+$")
//  private String line1;
//
//  /** The address line 2 for the international address. */
//  @Schema(description = "The address line 2 for the international address")
//  @JsonProperty
//  @XmlElement(name = "Line2")
//  @Size(max = 100)
//  @Pattern(
//      message = "{digital.inception.party.InternationalAddress.Line2.Pattern.message}",
//      regexp = "^[\\pL\\pN-' ]*$")
//  private String line2;
//
//  /** The address line 3 for the international address. */
//  @Schema(description = "The address line 3 for the international address")
//  @JsonProperty
//  @XmlElement(name = "Line3")
//  @Size(max = 100)
//  @Pattern(
//      message = "{digital.inception.party.InternationalAddress.Line3.Pattern.message}",
//      regexp = "^[\\pL\\pN-' ]*$")
//  private String line3;
//
//  /** Constructs a new <code>InternationalAddress</code>. */
//  public InternationalAddress() {}
//
//  /**
//   * Constructs a new <code>InternationalAddress</code>.
//   *
//   * @param line1 the address line 1 for the international address
//   * @param line2 the address line 2 for the international address
//   * @param line3 the address line 3 for the international address
//   */
//  public InternationalAddress(String line1, String line2, String line3) {
//    this.line1 = line1;
//    this.line2 = line2;
//    this.line3 = line3;
//  }
//
//  /**
//   * Constructs a new <code>InternationalAddress</code>.
//   *
//   * @param line1 the address line 1 for the international address
//   * @param line2 the address line 2 for the international address
//   */
//  public InternationalAddress(String line1, String line2) {
//    this.line1 = line1;
//    this.line2 = line2;
//  }
//
//  /**
//   * Constructs a new <code>InternationalAddress</code>.
//   *
//   * @param line1 the address line 1 for the international address
//   */
//  public InternationalAddress(String line1) {
//    this.line1 = line1;
//  }
//
//  /**
//   * Returns the address line 1 for the international address.
//   *
//   * @return the address line 1 for the international address
//   */
//  public String getLine1() {
//    return line1;
//  }
//
//  /**
//   * Returns the address line 2 for the international address.
//   *
//   * @return the address line 2 for the international address
//   */
//  public String getLine2() {
//    return line2;
//  }
//
//  /**
//   * Returns the address line 3 for the international address.
//   *
//   * @return the address line 3 for the international address
//   */
//  public String getLine3() {
//    return line3;
//  }
//
//  /**
//   * Set the address line 1 for the international address.
//   *
//   * @param line1 the address line 1 for the international address
//   */
//  public void setLine1(String line1) {
//    this.line1 = line1;
//  }
//
//  /**
//   * Set the address line 2 for the international address.
//   *
//   * @param line2 the address line 2 for the international address
//   */
//  public void setLine2(String line2) {
//    this.line2 = line2;
//  }
//
//  /**
//   * Set the address line 3 for the international address.
//   *
//   * @param line3 the address line 3 for the international address
//   */
//  public void setLine3(String line3) {
//    this.line3 = line3;
//  }
//}
