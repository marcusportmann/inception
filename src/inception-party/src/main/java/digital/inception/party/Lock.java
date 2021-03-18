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
import digital.inception.core.xml.LocalDateAdapter;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import java.time.LocalDate;
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
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

/**
 * The <b>Lock</b> class holds the information for a lock applied to an organization or person.
 *
 * @author Marcus Portmann
 */
@Schema(description = "A lock applied to an organization or person")
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({"type", "effectiveFrom", "effectiveTo"})
@XmlRootElement(name = "Lock", namespace = "http://inception.digital/party")
@XmlType(
    name = "Lock",
    namespace = "http://inception.digital/party",
    propOrder = {"type", "effectiveFrom", "effectiveTo"})
@XmlAccessorType(XmlAccessType.FIELD)
@Entity
@Table(schema = "party", name = "locks")
@IdClass(LockId.class)
public class Lock implements Serializable {

  private static final long serialVersionUID = 1000000;

  /** The date and time the lock was created. */
  @JsonIgnore
  @XmlTransient
  @CreationTimestamp
  @Column(name = "created", nullable = false, updatable = false)
  private LocalDateTime created;

  /** The date the lock is effective from. */
  @Schema(description = "The date the lock is effective from", required = true)
  @JsonProperty(required = true)
  @XmlElement(name = "EffectiveFrom", required = true)
  @XmlJavaTypeAdapter(LocalDateAdapter.class)
  @XmlSchemaType(name = "date")
  @NotNull
  @Column(name = "effective_from", nullable = false)
  private LocalDate effectiveFrom;

  /** The optional date the lock is effective to. */
  @Schema(description = "The optional date the lock is effective to")
  @JsonProperty
  @XmlElement(name = "EffectiveTo")
  @XmlJavaTypeAdapter(LocalDateAdapter.class)
  @XmlSchemaType(name = "date")
  @Column(name = "effective_to")
  private LocalDate effectiveTo;

  /** The party the lock is associated with. */
  @Schema(hidden = true)
  @JsonBackReference("lockReference")
  @XmlTransient
  @Id
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "party_id")
  private PartyBase party;

  /** The code for the lock type. */
  @Schema(description = "The code for the lock type", required = true)
  @JsonProperty(required = true)
  @XmlElement(name = "Type", required = true)
  @NotNull
  @Size(min = 1, max = 30)
  @Id
  @Column(name = "type", length = 30, nullable = false)
  private String type;

  /** The date and time the lock was last updated. */
  @JsonIgnore
  @XmlTransient
  @UpdateTimestamp
  @Column(name = "updated", insertable = false)
  private LocalDateTime updated;

  /** Constructs a new <b>Lock</b>. */
  public Lock() {}

  /**
   * Constructs a new <b>Lock</b>.
   *
   * @param type the lock type
   */
  public Lock(String type) {
    this.type = type;
    this.effectiveFrom = LocalDate.now();
  }

  /**
   * Constructs a new <b>Lock</b>.
   *
   * @param type the lock type
   * @param effectiveFrom the date the lock is effective from
   */
  public Lock(String type, LocalDate effectiveFrom) {
    this.type = type;
    this.effectiveFrom = effectiveFrom;
  }

  /**
   * Constructs a new <b>Lock</b>.
   *
   * @param type the lock type
   * @param effectiveFrom the date the lock is effective from
   * @param effectiveTo the date the lock is effective to
   */
  public Lock(String type, LocalDate effectiveFrom, LocalDate effectiveTo) {
    this.type = type;
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

    Lock other = (Lock) object;

    return Objects.equals(party, other.party) && Objects.equals(type, other.type);
  }

  /**
   * Returns the date and time the lock was created.
   *
   * @return the date and time the lock was created
   */
  public LocalDateTime getCreated() {
    return created;
  }

  /**
   * Returns the date the lock is effective from.
   *
   * @return the date the lock is effective from
   */
  public LocalDate getEffectiveFrom() {
    return effectiveFrom;
  }

  /**
   * Returns the optional date the lock is effective to.
   *
   * @return the optional date the lock is effective to
   */
  public LocalDate getEffectiveTo() {
    return effectiveTo;
  }

  /**
   * Returns the party the lock is associated with.
   *
   * @return the party the lock is associated with
   */
  @Schema(hidden = true)
  public PartyBase getParty() {
    return party;
  }

  /**
   * Returns the code for the lock type.
   *
   * @return the code for the lock type
   */
  public String getType() {
    return type;
  }

  /**
   * Returns the date and time the lock was last updated.
   *
   * @return the date and time the lock was last updated
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
   * Set the date the lock is effective from.
   *
   * @param effectiveFrom the date the lock is effective from
   */
  public void setEffectiveFrom(LocalDate effectiveFrom) {
    this.effectiveFrom = effectiveFrom;
  }

  /**
   * Set the optional date the lock is effective to.
   *
   * @param effectiveTo the optional date the lock is effective to
   */
  public void setEffectiveTo(LocalDate effectiveTo) {
    this.effectiveTo = effectiveTo;
  }

  /**
   * Set the party the lock is associated with.
   *
   * @param party the party the lock is associated with
   */
  @Schema(hidden = true)
  public void setParty(PartyBase party) {
    this.party = party;
  }

  /**
   * Set the code for the lock type.
   *
   * @param type the code for the lock type
   */
  public void setType(String type) {
    this.type = type;
  }
}
