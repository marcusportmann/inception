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
// * The <code>SiteAddress</code> class holds the information for a site address.
// *
// * @author Marcus Portmann
// */
//@Embeddable
//public class SiteAddress implements Serializable {
//
//  private static final long serialVersionUID = 1000000;
//
//  /** The site block for the site address. */
//  @Schema(description = "The site block for the site address", required = true)
//  @JsonProperty(required = true)
//  @XmlElement(name = "Block", required = true)
//  @NotNull
//  @Size(min = 1, max = 50)
//  @Pattern(
//      message = "{digital.inception.party.SiteAddress.Block.Pattern.message}",
//      regexp = "^[\\pL\\pN-' ]+$")
//  private String block;
//
//  /** The site number for the site address. */
//  @Schema(description = "The site number for the site address", required = true)
//  @JsonProperty(required = true)
//  @XmlElement(name = "Number", required = true)
//  @NotNull
//  @Size(min = 1, max = 50)
//  @Pattern(
//      message = "{digital.inception.party.SiteAddress.Number.Pattern.message}",
//      regexp = "^[\\pL\\pN-' ]+$")
//  private String number;
//
//  /** Constructs a new <code>SiteAddress</code>. */
//  public SiteAddress() {}
//
//  /**
//   * Constructs a new <code>SiteAddress</code>.
//   *
//   * @param block the site block for the site address
//   * @param number the site number for the site address
//   */
//  public SiteAddress(String block, String number) {
//    this.block = block;
//    this.number = number;
//  }
//
//  /**
//   * Returns the site block for the site address.
//   *
//   * @return the site block for the site address
//   */
//  public String getBlock() {
//    return block;
//  }
//
//  /**
//   * Returns the site number for the site address.
//   *
//   * @return the site number for the site address
//   */
//  public String getNumber() {
//    return number;
//  }
//
//  /**
//   * Set the site block for the site address.
//   *
//   * @param block the site block for the site address
//   */
//  public void setBlock(String block) {
//    this.block = block;
//  }
//
//  /**
//   * Set the site number for the site address.
//   *
//   * @param number the site number for the site address
//   */
//  public void setNumber(String number) {
//    this.number = number;
//  }
//}
