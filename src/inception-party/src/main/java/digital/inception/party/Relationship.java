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

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.github.f4b6a3.uuid.UuidCreator;
import digital.inception.core.xml.LocalDateAdapter;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
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
 * The <b>Relationship</b> class holds the information for a relationship between two parties.
 *
 * @author Marcus Portmann
 */
@Schema(description = "A relationship between two parties")
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({"id", "type", "firstPartyId", "secondPartyId", "effectiveFrom", "effectiveTo"})
@XmlRootElement(name = "Relationship", namespace = "http://inception.digital/party")
@XmlType(
    name = "Relationship",
    namespace = "http://inception.digital/party",
    propOrder = {"id", "type", "firstPartyId", "secondPartyId", "effectiveFrom", "effectiveTo"})
@XmlAccessorType(XmlAccessType.FIELD)
@Entity
@Table(schema = "party", name = "relationships")
public class Relationship implements Serializable {

  private static final long serialVersionUID = 1000000;

  /** The date and time the relationship was created. */
  @JsonIgnore
  @XmlTransient
  @CreationTimestamp
  @Column(name = "created", nullable = false, updatable = false)
  private LocalDateTime created;

  /** The date the relationship is effective from. */
  @Schema(description = "The date the relationship is effective from")
  @JsonProperty
  @XmlElement(name = "EffectiveFrom")
  @XmlJavaTypeAdapter(LocalDateAdapter.class)
  @XmlSchemaType(name = "date")
  @Column(name = "effective_from")
  private LocalDate effectiveFrom;

  /** The date the relationship is effective to. */
  @Schema(description = "The date the relationship is effective to")
  @JsonProperty
  @XmlElement(name = "EffectiveTo")
  @XmlJavaTypeAdapter(LocalDateAdapter.class)
  @XmlSchemaType(name = "date")
  @Column(name = "effective_to")
  private LocalDate effectiveTo;

  /** The Universally Unique Identifier (UUID) for the first party in the relationship. */
  @Schema(
      description =
          "The Universally Unique Identifier (UUID) for the first party in the relationship",
      required = true)
  @JsonProperty(required = true)
  @XmlElement(name = "FirstPartyId", required = true)
  @NotNull
  @Column(name = "first_party_id", nullable = false)
  private UUID firstPartyId;

  /** The Universally Unique Identifier (UUID) for the relationship. */
  @Schema(
      description = "The Universally Unique Identifier (UUID) for the relationship",
      required = true)
  @JsonProperty(required = true)
  @XmlElement(name = "Id", required = true)
  @NotNull
  @Id
  @Column(name = "id", nullable = false)
  private UUID id;

  /** The Universally Unique Identifier (UUID) for the second party in the relationship. */
  @Schema(
      description =
          "The Universally Unique Identifier (UUID) for the second party in the relationship",
      required = true)
  @JsonProperty(required = true)
  @XmlElement(name = "SecondPartyId", required = true)
  @NotNull
  @Column(name = "second_party_id", nullable = false)
  private UUID secondPartyId;

  /** The code for the relationship type. */
  @Schema(description = "The code for the relationship type", required = true)
  @JsonProperty(required = true)
  @XmlElement(name = "Type", required = true)
  @NotNull
  @Size(min = 1, max = 30)
  @Column(name = "type", length = 30, nullable = false)
  private String type;

  /** The date and time the relationship was last updated. */
  @JsonIgnore
  @XmlTransient
  @UpdateTimestamp
  @Column(name = "updated", insertable = false)
  private LocalDateTime updated;

  /** Constructs a new <b>Relationship</b>. */
  public Relationship() {}

  /**
   * Constructs a new <b>Relationship</b>.
   *
   * @param type the code for the relationship type
   * @param firstPartyId the Universally Unique Identifier (UUID) for the first party in the
   *     relationship
   * @param secondPartyId the Universally Unique Identifier (UUID) for the second party in the
   *     relationship
   */
  public Relationship(String type, UUID firstPartyId, UUID secondPartyId) {
    this.id = UuidCreator.getShortPrefixComb();
    this.type = type;
    this.firstPartyId = firstPartyId;
    this.secondPartyId = secondPartyId;
  }

  /**
   * Constructs a new <b>Relationship</b>.
   *
   * @param type the code for the relationship type
   * @param firstPartyId the Universally Unique Identifier (UUID) for the first party in the
   *     relationship
   * @param secondPartyId the Universally Unique Identifier (UUID) for the second party in the
   *     relationship
   * @param effectiveFrom the date that the relationship is effective from
   */
  public Relationship(String type, UUID firstPartyId, UUID secondPartyId, LocalDate effectiveFrom) {
    this.id = UuidCreator.getShortPrefixComb();
    this.type = type;
    this.firstPartyId = firstPartyId;
    this.secondPartyId = secondPartyId;
    this.effectiveFrom = effectiveFrom;
  }

  /**
   * Constructs a new <b>Relationship</b>.
   *
   * @param type the code for the relationship type
   * @param firstPartyId the Universally Unique Identifier (UUID) for the first party in the
   *     relationship
   * @param secondPartyId the Universally Unique Identifier (UUID) for the second party in the
   *     relationship
   * @param effectiveFrom the date that the relationship is effective from
   * @param effectiveTo the date that the relationship is effective to
   */
  public Relationship(
      String type,
      UUID firstPartyId,
      UUID secondPartyId,
      LocalDate effectiveFrom,
      LocalDate effectiveTo) {
    this.id = UuidCreator.getShortPrefixComb();
    this.type = type;
    this.firstPartyId = firstPartyId;
    this.secondPartyId = secondPartyId;
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

    Relationship other = (Relationship) object;

    return Objects.equals(id, other.id);
  }

  /**
   * Returns the date and time the relationship was created.
   *
   * @return the date and time the relationship was created
   */
  public LocalDateTime getCreated() {
    return created;
  }

  /**
   * Returns the date the relationship is effective from.
   *
   * @return the date the relationship is effective from
   */
  public LocalDate getEffectiveFrom() {
    return effectiveFrom;
  }

  /**
   * Returns the date the relationship is effective to.
   *
   * @return the date the relationship is effective to
   */
  public LocalDate getEffectiveTo() {
    return effectiveTo;
  }

  /**
   * Returns the Universally Unique Identifier (UUID) for the first party in the relationship.
   *
   * @return the Universally Unique Identifier (UUID) for the first party in the relationship
   */
  public UUID getFirstPartyId() {
    return firstPartyId;
  }

  /**
   * Returns the Universally Unique Identifier (UUID) for the relationship.
   *
   * @return the Universally Unique Identifier (UUID) for the relationship
   */
  public UUID getId() {
    return id;
  }

  /**
   * Returns the Universally Unique Identifier (UUID) for the second party in the relationship.
   *
   * @return the Universally Unique Identifier (UUID) for the second party in the relationship
   */
  public UUID getSecondPartyId() {
    return secondPartyId;
  }

  /**
   * Returns the code for the relationship type.
   *
   * @return the code for the relationship type
   */
  public String getType() {
    return type;
  }

  /**
   * Returns the date and time the relationship was last updated.
   *
   * @return the date and time the relationship was last updated
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
    return (id == null) ? 0 : id.hashCode();
  }

  /**
   * Set the date the relationship is effective from.
   *
   * @param effectiveFrom the date the relationship is effective from
   */
  public void setEffectiveFrom(LocalDate effectiveFrom) {
    this.effectiveFrom = effectiveFrom;
  }

  /**
   * Set the date the relationship is effective to.
   *
   * @param effectiveTo the date the relationship is effective to
   */
  public void setEffectiveTo(LocalDate effectiveTo) {
    this.effectiveTo = effectiveTo;
  }

  /**
   * Set the Universally Unique Identifier (UUID) for the first party in the relationship.
   *
   * @param firstPartyId the Universally Unique Identifier (UUID) for the first party in the
   *     relationship
   */
  public void setFirstPartyId(UUID firstPartyId) {
    this.firstPartyId = firstPartyId;
  }

  /**
   * Set the Universally Unique Identifier (UUID) for the relationship.
   *
   * @param id the Universally Unique Identifier (UUID) for the relationship
   */
  public void setId(UUID id) {
    this.id = id;
  }

  /**
   * Set the Universally Unique Identifier (UUID) for the second party in the relationship.
   *
   * @param secondPartyId the Universally Unique Identifier (UUID) for the second party in the
   *     relationship
   */
  public void setSecondPartyId(UUID secondPartyId) {
    this.secondPartyId = secondPartyId;
  }

  /**
   * Set the code for the relationship type.
   *
   * @param type the code for the relationship type
   */
  public void setType(String type) {
    this.type = type;
  }
}
