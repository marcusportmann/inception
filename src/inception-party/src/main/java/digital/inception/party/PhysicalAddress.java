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

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

/**
 * The <code>PhysicalAddress</code> class holds the information for a physical address.
 *
 * <p><b>NOTE:</b> The JPA 2.2 spec (10.6) does not support attribute converters for attributes
 * annotated with @Id. If Enum types are used for these attributes then the ordinal value is always
 * used. As a result, the type and purpose attributes for this class are Integers and the Getter and
 * Setters (a.k.a. Accessors and Mutators) convert to and from the Enum types. A consequence of this
 * is that the attributes are marked as @JsonIgnore and @XmlTransient and the Getters are annotated
 * with @JsonProperty and @XmlElement.
 *
 * @author Marcus Portmann
 */
@Schema(description = "A physical address associated with a party")
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({"type", "purpose"})
@XmlRootElement(name = "PhysicalAddress", namespace = "http://party.inception.digital")
@XmlType(
    name = "PhysicalAddress",
    namespace = "http://party.inception.digital",
    propOrder = {"type", "purpose"})
@XmlAccessorType(XmlAccessType.FIELD)
@Entity
@Table(schema = "party", name = "physical_addresses")
@IdClass(PhysicalAddressId.class)
public class PhysicalAddress implements Serializable {

  private static final long serialVersionUID = 1000000;

  /** The date and time the physical address was created. */
  @JsonIgnore
  @XmlTransient
  @CreationTimestamp
  @Column(name = "created", nullable = false, updatable = false)
  private LocalDateTime created;

  /** The party the physical address is associated with. */
  @Schema(hidden = true)
  @JsonBackReference
  @XmlTransient
  @Id
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "party_id")
  private Party party;

  /** The physical address purpose. */
  @JsonIgnore
  @XmlTransient
  @NotNull
  @Id
  @Column(name = "purpose", nullable = false)
  private Integer purpose;

  /** The physical address type. */
  @JsonIgnore
  @XmlTransient
  @NotNull
  @Id
  @Column(name = "type", nullable = false)
  private Integer type;

  /** The date and time the physical address was last updated. */
  @JsonIgnore
  @XmlTransient
  @UpdateTimestamp
  @Column(name = "updated", insertable = false)
  private LocalDateTime updated;

  /** Constructs a new <code>PhysicalAddress</code>. */
  public PhysicalAddress() {}

  /**
   * Constructs a new <code>PhysicalAddress</code>.
   *
   * @param type the physical address type
   * @param purpose the physical address purpose
   */
  public PhysicalAddress(PhysicalAddressType type, PhysicalAddressPurpose purpose) {
    this.type = PhysicalAddressType.toNumericCode(type);
    this.purpose = PhysicalAddressPurpose.toNumericCode(purpose);
  }

  /**
   * Indicates whether some other object is "equal to" this one.
   *
   * @param object the reference object with which to compare
   * @return <code>true</code> if this object is the same as the object argument otherwise <code>
   * false</code>
   */
  @Override
  public boolean equals(Object object) {
    if (this == object) {
      return true;
    }

    if (object == null) {
      return false;
    }

    if (getClass() != object.getClass()) {
      return false;
    }

    PhysicalAddress other = (PhysicalAddress) object;

    return Objects.equals(party, other.party)
        && Objects.equals(type, other.type)
        && Objects.equals(purpose, other.purpose);
  }

  /**
   * Returns the date and time the physical address was created.
   *
   * @return the date and time the physical address was created
   */
  public LocalDateTime getCreated() {
    return created;
  }

  /**
   * Returns the party the physical address is associated with.
   *
   * @return the party the physical address is associated with
   */
  @Schema(hidden = true)
  public Party getParty() {
    return party;
  }

  /**
   * Returns the physical address purpose.
   *
   * @return the physical address purpose
   */
  @Schema(description = "The physical address purpose", required = true)
  @JsonProperty(required = true)
  @XmlElement(name = "Purpose", required = true)
  public PhysicalAddressPurpose getPurpose() {
    return PhysicalAddressPurpose.fromNumericCode(purpose);
  }

  /**
   * Returns the physical address type.
   *
   * @return the physical address type
   */
  @Schema(description = "The physical address type", required = true)
  @JsonProperty(required = true)
  @XmlElement(name = "Type", required = true)
  public PhysicalAddressType getType() {
    return PhysicalAddressType.fromNumericCode(type);
  }

  /**
   * Returns the date and time the physical address was last updated.
   *
   * @return the date and time the physical address was last updated
   */
  public LocalDateTime getUpdated() {
    return updated;
  }

  /**
   * Returns a hash code value for the object.
   *
   * @return a hash code value for the object
   */
  @Override
  public int hashCode() {
    return (((party == null) || (party.getId() == null)) ? 0 : party.getId().hashCode())
        + ((type == null) ? 0 : type.hashCode())
        + ((purpose == null) ? 0 : purpose.hashCode());
  }

  /**
   * Set the party the physical address is associated with.
   *
   * @param party the party the physical address is associated with
   */
  @Schema(hidden = true)
  public void setParty(Party party) {
    this.party = party;
  }

  /**
   * Set the physical address purpose.
   *
   * @param purpose the physical address purpose
   */
  public void setPurpose(PhysicalAddressPurpose purpose) {
    this.purpose = PhysicalAddressPurpose.toNumericCode(purpose);
  }

  /**
   * Set the physical address type.
   *
   * @param type the physical address type
   */
  public void setType(PhysicalAddressType type) {
    this.type = PhysicalAddressType.toNumericCode(type);
  }
}
