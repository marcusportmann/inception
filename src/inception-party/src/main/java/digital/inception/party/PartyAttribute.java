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
 * The <b>PartyAttribute</b> class holds the information for an attribute for a party.
 *
 * @author Marcus Portmann
 */
@Schema(name = "PartyAttribute", description = "An attribute for a party")
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({"type", "stringValue"})
@XmlRootElement(name = "PartyAttribute", namespace = "http://inception.digital/party")
@XmlType(
    name = "PartyAttribute",
    namespace = "http://inception.digital/party",
    propOrder = {"type", "stringValue"})
@XmlAccessorType(XmlAccessType.FIELD)
@Entity
@Table(schema = "party", name = "party_attributes")
@IdClass(PartyAttributeId.class)
public class PartyAttribute implements Serializable {

  private static final long serialVersionUID = 1000000;

  /** The date and time the party attribute was created. */
  @JsonIgnore
  @XmlTransient
  @CreationTimestamp
  @Column(name = "created", nullable = false, updatable = false)
  private LocalDateTime created;

  /** The party the party attribute is associated with. */
  @Schema(hidden = true)
  @JsonBackReference("partyAttributeReference")
  @XmlTransient
  @Id
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "party_id")
  private PartyBase party;

  /** The string value for the party attribute. */
  @Schema(description = "The string value for the party attribute")
  @JsonProperty
  @XmlElement(name = "StringValue")
  @Size(min = 1, max = 200)
  @Column(name = "string_value", length = 200)
  private String stringValue;

  /** The code for the party attribute type. */
  @Schema(description = "The code for the party attribute type", required = true)
  @JsonProperty(required = true)
  @XmlElement(name = "Type", required = true)
  @NotNull
  @Size(min = 1, max = 30)
  @Id
  @Column(name = "type", length = 30, nullable = false)
  private String type;

  /** The date and time the party attribute was last updated. */
  @JsonIgnore
  @XmlTransient
  @UpdateTimestamp
  @Column(name = "updated", insertable = false)
  private LocalDateTime updated;

  /** Constructs a new <b>PartyAttribute</b>. */
  public PartyAttribute() {}

  /**
   * Constructs a new <b>PartyAttribute</b>.
   *
   * @param type the party attribute type
   * @param stringValue the string value for the party attribute
   */
  public PartyAttribute(String type, String stringValue) {
    this.type = type;
    this.stringValue = stringValue;
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

    PartyAttribute other = (PartyAttribute) object;

    return Objects.equals(party, other.party) && Objects.equals(type, other.type);
  }

  /**
   * Returns the date and time the party attribute was created.
   *
   * @return the date and time the party attribute was created
   */
  public LocalDateTime getCreated() {
    return created;
  }

  /**
   * Returns the party the party attribute is associated with.
   *
   * @return the party the party attribute is associated with
   */
  @Schema(hidden = true)
  public PartyBase getParty() {
    return party;
  }

  /**
   * Returns the string value for the party attribute.
   *
   * @return the string value for the party attribute
   */
  public String getStringValue() {
    return stringValue;
  }

  /**
   * Returns the code for the party attribute type.
   *
   * @return the code for the party attribute type
   */
  public String getType() {
    return type;
  }

  /**
   * Returns the date and time the party attribute was last updated.
   *
   * @return the date and time the party attribute was last updated
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
   * Set the party the party attribute is associated with.
   *
   * @param party the party the party attribute is associated with
   */
  @Schema(hidden = true)
  public void setParty(PartyBase party) {
    this.party = party;
  }

  /**
   * Set the string value for the party attribute.
   *
   * @param stringValue the string value for the party attribute
   */
  public void setStringValue(String stringValue) {
    this.stringValue = stringValue;
  }

  /**
   * Set the code for the party attribute type.
   *
   * @param type the code for the party attribute type
   */
  public void setType(String type) {
    this.type = type;
  }
}
