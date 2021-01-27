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
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

/**
 * The <code>PartyRole</code> class holds the information for a role assigned to a party independent
 * of a party association.
 *
 * @author Marcus Portmann
 */
@Schema(description = "A role assigned to a party independent of a party association")
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({"type", "purpose"})
@XmlRootElement(name = "PartyRole", namespace = "http://party.inception.digital")
@XmlType(
    name = "PartyRole",
    namespace = "http://party.inception.digital",
    propOrder = {"type", "purpose"})
@XmlAccessorType(XmlAccessType.FIELD)
@Entity
@Table(schema = "party", name = "party_roles")
@IdClass(PartyRoleId.class)
public class PartyRole implements Serializable {

  private static final long serialVersionUID = 1000000;

  /** The date and time the party role was created. */
  @JsonIgnore
  @XmlTransient
  @CreationTimestamp
  @Column(name = "created", nullable = false, updatable = false)
  private LocalDateTime created;

  /** The party the party role is associated with. */
  @Schema(hidden = true)
  @JsonBackReference("partyRoleReference")
  @XmlTransient
  @Id
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "party_id")
  private Party party;

  /** The optional code for the party role purpose. */
  @Schema(description = "The optional code for the party role purpose")
  @JsonProperty
  @XmlElement(name = "Purpose")
  @Size(min = 1, max = 30)
  @Column(name = "purpose", length = 30)
  private String purpose;

  /** The code for the party role type. */
  @Schema(description = "The code for the party role type", required = true)
  @JsonProperty(required = true)
  @XmlElement(name = "Type", required = true)
  @NotNull
  @Size(min = 1, max = 30)
  @Id
  @Column(name = "type", length = 30, nullable = false)
  private String type;

  /** The date and time the party role was last updated. */
  @JsonIgnore
  @XmlTransient
  @UpdateTimestamp
  @Column(name = "updated", insertable = false)
  private LocalDateTime updated;

  /** Constructs a new <code>PartyRole</code>. */
  public PartyRole() {}

  /**
   * Constructs a new <code>PartyRole</code>.
   *
   * @param type the party role type
   */
  public PartyRole(String type) {
    this.type = type;
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

    PartyRole other = (PartyRole) object;

    return Objects.equals(party, other.party) && Objects.equals(type, other.type);
  }

  /**
   * Returns the date and time the party role was created.
   *
   * @return the date and time the party role was created
   */
  public LocalDateTime getCreated() {
    return created;
  }

  /**
   * Returns the party the party role is associated with.
   *
   * @return the party the party role is associated with
   */
  @Schema(hidden = true)
  public Party getParty() {
    return party;
  }

  /**
   * Returns the optional code for the party role purpose.
   *
   * @return the optional code for the party role purpose
   */
  public String getPurpose() {
    return purpose;
  }

  /**
   * Returns the code for the party role type.
   *
   * @return the code for the party role type
   */
  public String getType() {
    return type;
  }

  /**
   * Returns the date and time the party role was last updated.
   *
   * @return the date and time the party role was last updated
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
        + ((type == null) ? 0 : type.hashCode());
  }

  /**
   * Set the party the party role is associated with.
   *
   * @param party the party the party role is associated with
   */
  @Schema(hidden = true)
  public void setParty(Party party) {
    this.party = party;
  }

  /**
   * Set the optional code for the party role purpose.
   *
   * @param purpose the optional code for the party role purpose
   */
  public void setPurpose(String purpose) {
    this.purpose = purpose;
  }

  /**
   * Set the code for the party role type.
   *
   * @param type the code for the party role type
   */
  public void setType(String type) {
    this.type = type;
  }
}
