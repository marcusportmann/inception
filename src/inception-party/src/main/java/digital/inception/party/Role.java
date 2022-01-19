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
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import digital.inception.core.xml.LocalDateAdapter;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import java.time.LocalDate;
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
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

/**
 * The <b>Role</b> class holds the information for a role assigned directly to an organization or
 * person, which may or may not be time based.
 *
 * @author Marcus Portmann
 */
@Schema(
    description =
        "A role assigned directly to an organization or person, which may or may not be time based")
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({"type", "purpose", "effectiveFrom", "effectiveTo"})
@XmlRootElement(name = "Role", namespace = "http://inception.digital/party")
@XmlType(
    name = "Role",
    namespace = "http://inception.digital/party",
    propOrder = {"type", "purpose", "effectiveFrom", "effectiveTo"})
@XmlAccessorType(XmlAccessType.FIELD)
@Entity
@Table(schema = "party", name = "roles")
@IdClass(RoleId.class)
public class Role implements Serializable {

  private static final long serialVersionUID = 1000000;

  /** The date the role is effective from. */
  @Schema(description = "The date the role is effective from")
  @JsonProperty
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
  @XmlElement(name = "EffectiveFrom")
  @XmlJavaTypeAdapter(LocalDateAdapter.class)
  @XmlSchemaType(name = "date")
  @Column(name = "effective_from")
  private LocalDate effectiveFrom;

  /** The date the role is effective to. */
  @Schema(description = "The date the role is effective to")
  @JsonProperty
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
  @XmlElement(name = "EffectiveTo")
  @XmlJavaTypeAdapter(LocalDateAdapter.class)
  @XmlSchemaType(name = "date")
  @Column(name = "effective_to")
  private LocalDate effectiveTo;

  /** The party the role is associated with. */
  @Schema(hidden = true)
  @JsonBackReference("roleReference")
  @XmlTransient
  @Id
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "party_id")
  private PartyBase party;

  /** The code for the role purpose. */
  @Schema(description = "The code for the role purpose")
  @JsonProperty
  @XmlElement(name = "Purpose")
  @Size(min = 1, max = 30)
  @Column(name = "purpose", length = 30)
  private String purpose;

  /** The code for the role type. */
  @Schema(description = "The code for the role type", required = true)
  @JsonProperty(required = true)
  @XmlElement(name = "Type", required = true)
  @NotNull
  @Size(min = 1, max = 30)
  @Id
  @Column(name = "type", length = 30, nullable = false)
  private String type;

  /** Constructs a new <b>Role</b>. */
  public Role() {}

  /**
   * Constructs a new <b>Role</b>.
   *
   * @param type the role type
   */
  public Role(String type) {
    this.type = type;
  }

  /**
   * Constructs a new <b>Role</b>.
   *
   * @param type the role type
   * @param effectiveFrom the date the role is effective from
   */
  public Role(String type, LocalDate effectiveFrom) {
    this.type = type;
    this.effectiveFrom = effectiveFrom;
  }

  /**
   * Constructs a new <b>Role</b>.
   *
   * @param type the role type
   * @param effectiveFrom the date the role is effective from
   * @param effectiveTo the date the role is effective to
   */
  public Role(String type, LocalDate effectiveFrom, LocalDate effectiveTo) {
    this.type = type;
    this.effectiveFrom = effectiveFrom;
    this.effectiveTo = effectiveTo;
  }

  /**
   * Constructs a new <b>Role</b>.
   *
   * @param type the role type
   * @param purpose the code for the role purpose
   */
  public Role(String type, String purpose) {
    this.type = type;
    this.purpose = purpose;
  }

  /**
   * Constructs a new <b>Role</b>.
   *
   * @param type the role type
   * @param purpose the code for the role purpose
   * @param effectiveFrom the date the role is effective from
   */
  public Role(String type, String purpose, LocalDate effectiveFrom) {
    this.type = type;
    this.purpose = purpose;
    this.effectiveFrom = effectiveFrom;
  }

  /**
   * Constructs a new <b>Role</b>.
   *
   * @param type the role type
   * @param purpose the code for the role purpose
   * @param effectiveFrom the date the role is effective from
   * @param effectiveTo the date the role is effective to
   */
  public Role(String type, String purpose, LocalDate effectiveFrom, LocalDate effectiveTo) {
    this.type = type;
    this.purpose = purpose;
    this.effectiveFrom = effectiveFrom;
    this.effectiveTo = effectiveTo;
  }

  /**
   * Indicates whether some other object is "equal to" this one.
   *
   * @param object the reference object with which to compare
   * @return <b>true</b> if this object is the same as the object argument otherwise <b>false</b>
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

    Role other = (Role) object;

    return Objects.equals(party, other.party) && Objects.equals(type, other.type);
  }

  /**
   * Returns the date the role is effective from.
   *
   * @return the date the role is effective from
   */
  public LocalDate getEffectiveFrom() {
    return effectiveFrom;
  }

  /**
   * Returns the date the role is effective to.
   *
   * @return the date the role is effective to
   */
  public LocalDate getEffectiveTo() {
    return effectiveTo;
  }

  /**
   * Returns the party the role is associated with.
   *
   * @return the party the role is associated with
   */
  @Schema(hidden = true)
  public PartyBase getParty() {
    return party;
  }

  /**
   * Returns the code for the role purpose.
   *
   * @return the code for the role purpose
   */
  public String getPurpose() {
    return purpose;
  }

  /**
   * Returns the code for the role type.
   *
   * @return the code for the role type
   */
  public String getType() {
    return type;
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
   * Set the date the role is effective from.
   *
   * @param effectiveFrom the date the role is effective from
   */
  public void setEffectiveFrom(LocalDate effectiveFrom) {
    this.effectiveFrom = effectiveFrom;
  }

  /**
   * Set the date the role is effective to.
   *
   * @param effectiveTo the date the role is effective to
   */
  public void setEffectiveTo(LocalDate effectiveTo) {
    this.effectiveTo = effectiveTo;
  }

  /**
   * Set the party the role is associated with.
   *
   * @param party the party the role is associated with
   */
  @Schema(hidden = true)
  public void setParty(PartyBase party) {
    this.party = party;
  }

  /**
   * Set the code for the role purpose.
   *
   * @param purpose the code for the role purpose
   */
  public void setPurpose(String purpose) {
    this.purpose = purpose;
  }

  /**
   * Set the code for the role type.
   *
   * @param type the code for the role type
   */
  public void setType(String type) {
    this.type = type;
  }
}
