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
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
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
 * The <code>Party</code> class holds the information for a party.
 *
 * @author Marcus Portmann
 */
@Schema(description = "A person or organization")
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({"id", "tenantId", "type", "name"})
@XmlRootElement(name = "Party", namespace = "http://party.inception.digital")
@XmlType(
    name = "Party",
    namespace = "http://party.inception.digital",
    propOrder = {"id", "tenantId", "type", "name"})
@XmlAccessorType(XmlAccessType.FIELD)
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Table(schema = "party", name = "parties")
public class Party implements Serializable {

  private static final long serialVersionUID = 1000000;

  /** The date and time the party was created. */
  @JsonIgnore
  @XmlTransient
  @CreationTimestamp
  @Column(name = "created", nullable = false, updatable = false)
  protected LocalDateTime created;

  /** The date and time the party was last updated. */
  @JsonIgnore
  @XmlTransient
  @UpdateTimestamp
  @Column(name = "updated", insertable = false)
  protected LocalDateTime updated;

  /** The Universally Unique Identifier (UUID) for the party. */
  @Schema(description = "The Universally Unique Identifier (UUID) for the party", required = true)
  @JsonProperty(required = true)
  @XmlElement(name = "Id", required = true)
  @NotNull
  @Id
  @Column(name = "id", nullable = false)
  private UUID id;

  /**
   * The name of the party, e.g. the full legal name of a person, the legal name of an organization,
   * the name of a family, etc.
   */
  @Schema(description = "The name of the party", required = true)
  @JsonProperty(required = true)
  @XmlElement(name = "Name", required = true)
  @NotNull
  @Size(min = 1, max = 100)
  @Column(name = "name", nullable = false, length = 100)
  private String name;

  /** The Universally Unique Identifier (UUID) for the tenant the party is associated with. */
  @Schema(
      description =
          "The Universally Unique Identifier (UUID) for the tenant the party is associated with",
      required = true)
  @JsonProperty(required = true)
  @XmlElement(name = "TenantId", required = true)
  @NotNull
  @Column(name = "tenant_id", nullable = false)
  private UUID tenantId;

  /** The party type. */
  @Schema(description = "The party type", required = true)
  @JsonProperty(required = true)
  @XmlElement(name = "Type", required = true)
  @NotNull
  // @Column(name = "type", nullable = false, insertable = false, updatable = false)
  @Column(name = "type", nullable = false)
  private PartyType type;

  /** Constructs a new <code>Party</code>. */
  public Party() {}

  /**
   * Constructs a new <code>Party</code>.
   *
   * @param type the party type
   */
  public Party(PartyType type) {
    this.type = type;
  }

  /**
   * Constructs a new <code>Party</code>.
   *
   * @param tenantId the Universally Unique Identifier (UUID) for the tenant the party is associated
   *     with
   * @param type the party type
   * @param name the name of the party
   */
  public Party(UUID tenantId, PartyType type, String name) {
    this.id = UuidCreator.getShortPrefixComb();
    this.tenantId = tenantId;
    this.type = type;
    this.name = name;
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

    Party other = (Party) object;

    return Objects.equals(id, other.id);
  }

  /**
   * Returns the date and time the party was created.
   *
   * @return the date and time the party was created
   */
  public LocalDateTime getCreated() {
    return created;
  }

  /**
   * Returns the Universally Unique Identifier (UUID) for the party.
   *
   * @return the Universally Unique Identifier (UUID) for the party
   */
  public UUID getId() {
    return id;
  }

  /**
   * Returns the name of the party.
   *
   * @return the name of the party
   */
  public String getName() {
    return name;
  }

  /**
   * Returns the Universally Unique Identifier (UUID) for the tenant the party is associated with.
   *
   * @return the Universally Unique Identifier (UUID) for the tenant the party is associated with
   */
  public UUID getTenantId() {
    return tenantId;
  }

  /**
   * Returns the party type.
   *
   * @return the party type
   */
  public PartyType getType() {
    return type;
  }

  /**
   * Returns the date and time the party was last updated.
   *
   * @return the date and time the party was last updated
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
    return ((id == null) ? 0 : id.hashCode());
  }

  /**
   * Set the Universally Unique Identifier (UUID) for the party.
   *
   * @param id the Universally Unique Identifier (UUID) for the party
   */
  public void setId(UUID id) {
    this.id = id;
  }

  /**
   * Set the name of the party.
   *
   * @param name the name of the party
   */
  public void setName(String name) {
    this.name = name;
  }

  /**
   * Set the Universally Unique Identifier (UUID) for the tenant the party is associated with.
   *
   * @param tenantId the Universally Unique Identifier (UUID) for the tenant the party is associated
   *     with
   */
  public void setTenantId(UUID tenantId) {
    this.tenantId = tenantId;
  }

  /**
   * Set the party type.
   *
   * @param type the party type
   */
  public void setType(PartyType type) {
    this.type = type;
  }
}
