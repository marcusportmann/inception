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
// * The <code>UnstructuredAddress</code> class holds the information for an unstructured address.
// *
// * @author Marcus Portmann
// */
//@Embeddable
//public class UnstructuredAddress implements Serializable {
//
//  private static final long serialVersionUID = 1000000;
//
//  /** The address line 1 for the unstructured address. */
//  @Schema(description = "The address line 1 for the unstructured address", required = true)
//  @JsonProperty(required = true)
//  @XmlElement(name = "Line1", required = true)
//  @NotNull
//  @Size(max = 100)
//  @Pattern(
//      message = "{digital.inception.party.UnstructuredAddress.Line1.Pattern.message}",
//      regexp = "^(?!\\s+)[\\pL\\pN-' ]+(?!\\s+)$")
//  private String line1;
//
//  /** The address line 2 for the unstructured address. */
//  @Schema(description = "The address line 2 for the unstructured address")
//  @JsonProperty
//  @XmlElement(name = "Line2")
//  @Size(max = 100)
//  @Pattern(
//      message = "{digital.inception.party.UnstructuredAddress.Line2.Pattern.message}",
//      regexp = "^(?!\\s+)[\\pL\\pN-' ]*(?!\\s+)$")
//  private String line2;
//
//  /** The address line 3 for the unstructured address. */
//  @Schema(description = "The address line 3 for the unstructured address")
//  @JsonProperty
//  @XmlElement(name = "Line3")
//  @Size(max = 100)
//  @Pattern(
//      message = "{digital.inception.party.UnstructuredAddress.Line3.Pattern.message}",
//      regexp = "^(?!\\s+)[\\pL\\pN-' ]*(?!\\s+)$")
//  private String line3;
//
//  /** Constructs a new <code>UnstructuredAddress</code>. */
//  public UnstructuredAddress() {}
//
//  /**
//   * Constructs a new <code>UnstructuredAddress</code>.
//   *
//   * @param line1 the address line 1 for the unstructured address
//   * @param line2 the address line 2 for the unstructured address
//   * @param line3 the address line 3 for the unstructured address
//   */
//  public UnstructuredAddress(String line1, String line2, String line3) {
//    this.line1 = line1;
//    this.line2 = line2;
//    this.line3 = line3;
//  }
//
//  /**
//   * Constructs a new <code>UnstructuredAddress</code>.
//   *
//   * @param line1 the address line 1 for the unstructured address
//   * @param line2 the address line 2 for the unstructured address
//   */
//  public UnstructuredAddress(String line1, String line2) {
//    this.line1 = line1;
//    this.line2 = line2;
//  }
//
//  /**
//   * Constructs a new <code>UnstructuredAddress</code>.
//   *
//   * @param line1 the address line 1 for the unstructured address
//   */
//  public UnstructuredAddress(String line1) {
//    this.line1 = line1;
//  }
//
//  /**
//   * Returns the address line 1 for the unstructured address.
//   *
//   * @return the address line 1 for the unstructured address
//   */
//  public String getLine1() {
//    return line1;
//  }
//
//  /**
//   * Returns the address line 2 for the unstructured address.
//   *
//   * @return the address line 2 for the unstructured address
//   */
//  public String getLine2() {
//    return line2;
//  }
//
//  /**
//   * Returns the address line 3 for the unstructured address.
//   *
//   * @return the address line 3 for the unstructured address
//   */
//  public String getLine3() {
//    return line3;
//  }
//
//  /**
//   * Set the address line 1 for the unstructured address.
//   *
//   * @param line1 the address line 1 for the unstructured address
//   */
//  public void setLine1(String line1) {
//    this.line1 = line1;
//  }
//
//  /**
//   * Set the address line 2 for the unstructured address.
//   *
//   * @param line2 the address line 2 for the unstructured address
//   */
//  public void setLine2(String line2) {
//    this.line2 = line2;
//  }
//
//  /**
//   * Set the address line 3 for the unstructured address.
//   *
//   * @param line3 the address line 3 for the unstructured address
//   */
//  public void setLine3(String line3) {
//    this.line3 = line3;
//  }
//}
