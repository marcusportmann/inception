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
import digital.inception.party.constraints.ValidAssociation;
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
 * The <b>Association</b> class holds the information for a association between two parties.
 *
 * @author Marcus Portmann
 */
@Schema(description = "A association between two parties")
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
  "id",
  "tenantId",
  "type",
  "firstPartyId",
  "secondPartyId",
  "effectiveFrom",
  "effectiveTo"
})
@XmlRootElement(name = "Association", namespace = "http://inception.digital/party")
@XmlType(
    name = "Association",
    namespace = "http://inception.digital/party",
    propOrder = {
      "id",
      "tenantId",
      "type",
      "firstPartyId",
      "secondPartyId",
      "effectiveFrom",
      "effectiveTo"
    })
@XmlAccessorType(XmlAccessType.FIELD)
@ValidAssociation
@Entity
@Table(schema = "party", name = "associations")
public class Association implements Serializable {

  private static final long serialVersionUID = 1000000;

  /** The date and time the association was created. */
  @JsonIgnore
  @XmlTransient
  @CreationTimestamp
  @Column(name = "created", nullable = false, updatable = false)
  private LocalDateTime created;

  /** The date the association is effective from. */
  @Schema(description = "The date the association is effective from")
  @JsonProperty
  @XmlElement(name = "EffectiveFrom")
  @XmlJavaTypeAdapter(LocalDateAdapter.class)
  @XmlSchemaType(name = "date")
  @Column(name = "effective_from")
  private LocalDate effectiveFrom;

  /** The date the association is effective to. */
  @Schema(description = "The date the association is effective to")
  @JsonProperty
  @XmlElement(name = "EffectiveTo")
  @XmlJavaTypeAdapter(LocalDateAdapter.class)
  @XmlSchemaType(name = "date")
  @Column(name = "effective_to")
  private LocalDate effectiveTo;

  /** The Universally Unique Identifier (UUID) for the first party in the association. */
  @Schema(
      description =
          "The Universally Unique Identifier (UUID) for the first party in the association",
      required = true)
  @JsonProperty(required = true)
  @XmlElement(name = "FirstPartyId", required = true)
  @NotNull
  @Column(name = "first_party_id", nullable = false)
  private UUID firstPartyId;

  /** The Universally Unique Identifier (UUID) for the association. */
  @Schema(
      description = "The Universally Unique Identifier (UUID) for the association",
      required = true)
  @JsonProperty(required = true)
  @XmlElement(name = "Id", required = true)
  @NotNull
  @Id
  @Column(name = "id", nullable = false)
  private UUID id;

  /** The Universally Unique Identifier (UUID) for the second party in the association. */
  @Schema(
      description =
          "The Universally Unique Identifier (UUID) for the second party in the association",
      required = true)
  @JsonProperty(required = true)
  @XmlElement(name = "SecondPartyId", required = true)
  @NotNull
  @Column(name = "second_party_id", nullable = false)
  private UUID secondPartyId;

  /** The Universally Unique Identifier (UUID) for the tenant the association is associated with. */
  @Schema(
      description =
          "The Universally Unique Identifier (UUID) for the tenant the association is associated with",
      required = true)
  @JsonProperty(required = true)
  @XmlElement(name = "TenantId", required = true)
  @NotNull
  @Column(name = "tenant_id", nullable = false)
  private UUID tenantId;

  /** The code for the association type. */
  @Schema(description = "The code for the association type", required = true)
  @JsonProperty(required = true)
  @XmlElement(name = "Type", required = true)
  @NotNull
  @Size(min = 1, max = 30)
  @Column(name = "type", length = 30, nullable = false)
  private String type;

  /** The date and time the association was last updated. */
  @JsonIgnore
  @XmlTransient
  @UpdateTimestamp
  @Column(name = "updated", insertable = false)
  private LocalDateTime updated;

  /** Constructs a new <b>Association</b>. */
  public Association() {}

  /**
   * Constructs a new <b>Association</b>.
   *
   * @param tenantId the Universally Unique Identifier (UUID) for the tenant the association is
   *     associated with
   * @param type the code for the association type
   * @param firstPartyId the Universally Unique Identifier (UUID) for the first party in the
   *     association
   * @param secondPartyId the Universally Unique Identifier (UUID) for the second party in the
   *     association
   */
  public Association(UUID tenantId, String type, UUID firstPartyId, UUID secondPartyId) {
    this.id = UuidCreator.getShortPrefixComb();
    this.tenantId = tenantId;
    this.type = type;
    this.firstPartyId = firstPartyId;
    this.secondPartyId = secondPartyId;
  }

  /**
   * Constructs a new <b>Association</b>.
   *
   * @param tenantId the Universally Unique Identifier (UUID) for the tenant the association is
   *     associated with
   * @param type the code for the association type
   * @param firstPartyId the Universally Unique Identifier (UUID) for the first party in the
   *     association
   * @param secondPartyId the Universally Unique Identifier (UUID) for the second party in the
   *     association
   * @param effectiveFrom the date that the association is effective from
   */
  public Association(
      UUID tenantId, String type, UUID firstPartyId, UUID secondPartyId, LocalDate effectiveFrom) {
    this.id = UuidCreator.getShortPrefixComb();
    this.tenantId = tenantId;
    this.type = type;
    this.firstPartyId = firstPartyId;
    this.secondPartyId = secondPartyId;
    this.effectiveFrom = effectiveFrom;
  }

  /**
   * Constructs a new <b>Association</b>.
   *
   * @param tenantId the Universally Unique Identifier (UUID) for the tenant the association is
   *     associated with
   * @param type the code for the association type
   * @param firstPartyId the Universally Unique Identifier (UUID) for the first party in the
   *     association
   * @param secondPartyId the Universally Unique Identifier (UUID) for the second party in the
   *     association
   * @param effectiveFrom the date that the association is effective from
   * @param effectiveTo the date that the association is effective to
   */
  public Association(
      UUID tenantId,
      String type,
      UUID firstPartyId,
      UUID secondPartyId,
      LocalDate effectiveFrom,
      LocalDate effectiveTo) {
    this.id = UuidCreator.getShortPrefixComb();
    this.tenantId = tenantId;
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

    Association other = (Association) object;

    return Objects.equals(id, other.id);
  }

  /**
   * Returns the date and time the association was created.
   *
   * @return the date and time the association was created
   */
  public LocalDateTime getCreated() {
    return created;
  }

  /**
   * Returns the date the association is effective from.
   *
   * @return the date the association is effective from
   */
  public LocalDate getEffectiveFrom() {
    return effectiveFrom;
  }

  /**
   * Returns the date the association is effective to.
   *
   * @return the date the association is effective to
   */
  public LocalDate getEffectiveTo() {
    return effectiveTo;
  }

  /**
   * Returns the Universally Unique Identifier (UUID) for the first party in the association.
   *
   * @return the Universally Unique Identifier (UUID) for the first party in the association
   */
  public UUID getFirstPartyId() {
    return firstPartyId;
  }

  /**
   * Returns the Universally Unique Identifier (UUID) for the association.
   *
   * @return the Universally Unique Identifier (UUID) for the association
   */
  public UUID getId() {
    return id;
  }

  /**
   * Returns the Universally Unique Identifier (UUID) for the second party in the association.
   *
   * @return the Universally Unique Identifier (UUID) for the second party in the association
   */
  public UUID getSecondPartyId() {
    return secondPartyId;
  }

  /**
   * Returns the Universally Unique Identifier (UUID) for the tenant the association is associated
   * with.
   *
   * @return the Universally Unique Identifier (UUID) for the tenant the association is associated
   *     with
   */
  public UUID getTenantId() {
    return tenantId;
  }

  /**
   * Returns the code for the association type.
   *
   * @return the code for the association type
   */
  public String getType() {
    return type;
  }

  /**
   * Returns the date and time the association was last updated.
   *
   * @return the date and time the association was last updated
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
   * Set the date the association is effective from.
   *
   * @param effectiveFrom the date the association is effective from
   */
  public void setEffectiveFrom(LocalDate effectiveFrom) {
    this.effectiveFrom = effectiveFrom;
  }

  /**
   * Set the date the association is effective to.
   *
   * @param effectiveTo the date the association is effective to
   */
  public void setEffectiveTo(LocalDate effectiveTo) {
    this.effectiveTo = effectiveTo;
  }

  /**
   * Set the Universally Unique Identifier (UUID) for the first party in the association.
   *
   * @param firstPartyId the Universally Unique Identifier (UUID) for the first party in the
   *     association
   */
  public void setFirstPartyId(UUID firstPartyId) {
    this.firstPartyId = firstPartyId;
  }

  /**
   * Set the Universally Unique Identifier (UUID) for the association.
   *
   * @param id the Universally Unique Identifier (UUID) for the association
   */
  public void setId(UUID id) {
    this.id = id;
  }

  /**
   * Set the Universally Unique Identifier (UUID) for the second party in the association.
   *
   * @param secondPartyId the Universally Unique Identifier (UUID) for the second party in the
   *     association
   */
  public void setSecondPartyId(UUID secondPartyId) {
    this.secondPartyId = secondPartyId;
  }

  /**
   * Set the Universally Unique Identifier (UUID) for the tenant the association is associated with.
   *
   * @param tenantId the Universally Unique Identifier (UUID) for the tenant the association is
   *     associated with
   */
  public void setTenantId(UUID tenantId) {
    this.tenantId = tenantId;
  }

  /**
   * Set the code for the association type.
   *
   * @param type the code for the association type
   */
  public void setType(String type) {
    this.type = type;
  }
}
