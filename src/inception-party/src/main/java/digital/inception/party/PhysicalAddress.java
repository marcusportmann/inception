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
//import com.fasterxml.jackson.annotation.JsonBackReference;
//import com.fasterxml.jackson.annotation.JsonIgnore;
//import com.fasterxml.jackson.annotation.JsonInclude;
//import com.fasterxml.jackson.annotation.JsonProperty;
//import com.fasterxml.jackson.annotation.JsonPropertyOrder;
//import io.swagger.v3.oas.annotations.media.Schema;
//import java.io.Serializable;
//import java.time.LocalDateTime;
//import java.util.Objects;
//import javax.persistence.Column;
//import javax.persistence.Entity;
//import javax.persistence.FetchType;
//import javax.persistence.Id;
//import javax.persistence.IdClass;
//import javax.persistence.ManyToOne;
//import javax.persistence.Table;
//import javax.validation.constraints.NotNull;
//import javax.validation.constraints.Size;
//import javax.xml.bind.annotation.XmlAccessType;
//import javax.xml.bind.annotation.XmlAccessorType;
//import javax.xml.bind.annotation.XmlElement;
//import javax.xml.bind.annotation.XmlRootElement;
//import javax.xml.bind.annotation.XmlTransient;
//import javax.xml.bind.annotation.XmlType;
//import org.hibernate.annotations.CreationTimestamp;
//import org.hibernate.annotations.UpdateTimestamp;
//
///**
// * The <code>PhysicalAddress</code> class holds the information for a physical address.
// *
// * @author Marcus Portmann
// */
//@Schema(description = "A physical address for a party")
//@JsonInclude(JsonInclude.Include.NON_NULL)
//@JsonPropertyOrder({"type"})
//@XmlRootElement(name = "PhysicalAddress", namespace = "http://party.inception.digital")
//@XmlType(
//    name = "PhysicalAddress",
//    namespace = "http://party.inception.digital",
//    propOrder = {"type"})
//@XmlAccessorType(XmlAccessType.FIELD)
//@Entity
//@Table(schema = "party", name = "physical_addresses")
//@IdClass(PhysicalAddressId.class)
//public class PhysicalAddress implements Serializable {
//
//  private static final long serialVersionUID = 1000000;
//
//  /** The date and time the physical address was created. */
//  @JsonIgnore
//  @XmlTransient
//  @CreationTimestamp
//  @Column(name = "created", nullable = false, updatable = false)
//  private LocalDateTime created;
//
//  /** The party the physical address is associated with. */
//  @Schema(hidden = true)
//  @JsonBackReference
//  @XmlTransient
//  @Id
//  @ManyToOne(fetch = FetchType.LAZY)
//  private Party party;
//
//  /** The physical address sub type. */
//  @Schema(description = "The physical address sub type", required = true)
//  @JsonProperty(required = true)
//  @XmlElement(name = "SubType", required = true)
//  @NotNull
//  @Id
//  @Column(name = "sub_type", nullable = false)
//  private PhysicalAddressSubType subType;
//
//  /** The physical address type. */
//  @Schema(description = "The physical address type", required = true)
//  @JsonProperty(required = true)
//  @XmlElement(name = "Type", required = true)
//  @NotNull
//  @Id
//  @Column(name = "type", nullable = false)
//  private PhysicalAddressType type;
//
//  /** The date and time the physical address was last updated. */
//  @JsonIgnore
//  @XmlTransient
//  @UpdateTimestamp
//  @Column(name = "updated", insertable = false)
//  private LocalDateTime updated;
//
//  /** The value for the physical address. */
//  @Schema(description = "The value for the physical address", required = true)
//  @JsonProperty(required = true)
//  @XmlElement(name = "Value", required = true)
//  @NotNull
//  @Size(min = 1, max = 200)
//  @Column(name = "value", nullable = false, length = 200)
//  private String value;
//
//  /** Constructs a new <code>PhysicalAddress</code>. */
//  public PhysicalAddress() {}
//
//  /**
//   * Indicates whether some other object is "equal to" this one.
//   *
//   * @param object the reference object with which to compare
//   * @return <code>true</code> if this object is the same as the object argument otherwise <code>
//   * false</code>
//   */
//  @Override
//  public boolean equals(Object object) {
//    if (this == object) {
//      return true;
//    }
//
//    if (object == null) {
//      return false;
//    }
//
//    if (getClass() != object.getClass()) {
//      return false;
//    }
//
//    PhysicalAddress other = (PhysicalAddress) object;
//
//    return Objects.equals(party, other.party)
//        && Objects.equals(type, other.type)
//        && Objects.equals(subType, other.subType);
//  }
//
//  /**
//   * Returns the date and time the physical address was created.
//   *
//   * @return the date and time the physical address was created
//   */
//  public LocalDateTime getCreated() {
//    return created;
//  }
//
//  /**
//   * Returns the party the physical address is associated with.
//   *
//   * @return the party the physical address is associated with
//   */
//  @Schema(hidden = true)
//  public Party getParty() {
//    return party;
//  }
//
//  /**
//   * Returns the physical address sub type.
//   *
//   * @return the physical address sub type
//   */
//  public PhysicalAddressSubType getSubType() {
//    return subType;
//  }
//
//  /**
//   * Returns the physical address type.
//   *
//   * @return the physical address type
//   */
//  public PhysicalAddressType getType() {
//    return type;
//  }
//
//  /**
//   * Returns the date and time the physical address was last updated.
//   *
//   * @return the date and time the physical address was last updated
//   */
//  public LocalDateTime getUpdated() {
//    return updated;
//  }
//
//  /**
//   * Returns the value for the physical address.
//   *
//   * @return the value for the physical address
//   */
//  public String getValue() {
//    return value;
//  }
//
//  /**
//   * Returns a hash code value for the object.
//   *
//   * @return a hash code value for the object
//   */
//  @Override
//  public int hashCode() {
//    return (((party == null) || (party.getId() == null)) ? 0 : party.getId().hashCode())
//        + ((type == null) ? 0 : type.hashCode())
//        + ((subType == null) ? 0 : subType.hashCode());
//  }
//
//  //  /**
//  //   * Set the Universally Unique Identifier (UUID) uniquely identifying the physical address.
//  //   *
//  //   * @param id the Universally Unique Identifier (UUID) uniquely identifying the contact
//  // mechanism
//  //   */
//  //  public void setId(UUID id) {
//  //    this.id = id;
//  //  }
//
//  /**
//   * Set the party the physical address is associated with.
//   *
//   * @param party the party the physical address is associated with
//   */
//  @Schema(hidden = true)
//  public void setParty(Party party) {
//    this.party = party;
//  }
//
//  /**
//   * Set the physical address sub type.
//   *
//   * @param subType the physical address sub type
//   */
//  public void setSubType(PhysicalAddressSubType subType) {
//    this.subType = subType;
//  }
//
//  /**
//   * Set the physical address type.
//   *
//   * @param type the physical address type
//   */
//  public void setType(PhysicalAddressType type) {
//    this.type = type;
//  }
//
//  /**
//   * Set the value for the physical address.
//   *
//   * @param value the value for the physical address
//   */
//  public void setValue(String value) {
//    this.value = value;
//  }
//}
