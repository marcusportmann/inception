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
import javax.xml.bind.annotation.XmlTransient;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

/**
 * The <b>PartyBase</b> class holds the information common to all types of parties and is the base
 * class that the classes for the different party types are derived from, e.g. <b>Person</b>.
 *
 * <p>The <b>Party</b> and <b>PartyBase</b> classes are both JPA entity classes mapped to the same
 * <b>party.parties</b> table. The <b>PartyBase</b> class provides the common base class for all JPA
 * entity classes that form part of the party inheritance model, e.g. <b>Organization</b>,
 * <b>Person</b>, etc. This inheritance model is required to allow the same child classes to be
 * mapped to the different parent classes for the different party types, e.g. to support the
 * one-to-many mappings for both the <b>Organization</b> and <b>Person</b> classes to the
 * <b>PhysicalAddress</b> class. The <b>Party</b> class provides a mechanism to retrieve the minimum
 * amount of party information without executing the polymorphic query that would result from
 * retrieving the same entities using a query that specifies the <b>PartyBase</b> class as the
 * result type.
 *
 * <p>This class and its subclasses expose the JSON and XML properties using a property-based
 * approach rather than a field-based approach to support the JPA inheritance model.
 *
 * @author Marcus Portmann
 */
@XmlAccessorType(XmlAccessType.NONE)
@XmlTransient
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Table(schema = "party", name = "parties")
public class PartyBase implements Serializable {

  private static final long serialVersionUID = 1000000;

  /** The date and time the party was created. */
  @CreationTimestamp
  @Column(name = "created", nullable = false, updatable = false)
  private LocalDateTime created;

  /** The Universally Unique Identifier (UUID) for the party. */
  @NotNull
  @Id
  @Column(name = "id", nullable = false)
  private UUID id;

  /**
   * The name of the party, e.g. the full legal name of a person, the legal name of an organization,
   * the name of a family, etc.
   */
  @NotNull
  @Size(min = 1, max = 100)
  @Column(name = "name", length = 100, nullable = false)
  private String name;

  /** The party type for the party. */
  @NotNull
  @Column(name = "type", length = 30, nullable = false)
  private PartyType partyType;

  /** The Universally Unique Identifier (UUID) for the tenant the party is associated with. */
  @NotNull
  @Column(name = "tenant_id", nullable = false)
  private UUID tenantId;

  /** The date and time the party was last updated. */
  @UpdateTimestamp
  @Column(name = "updated", insertable = false)
  private LocalDateTime updated;

  /** Constructs a new <b>PartyBase</b>. */
  protected PartyBase() {}

  /**
   * Constructs a new <b>PartyBase</b>.
   *
   * @param id the Universally Unique Identifier (UUID) for the party
   * @param tenantId the Universally Unique Identifier (UUID) for the tenant the party is associated
   *     with
   * @param partyType the party type
   */
  protected PartyBase(UUID id, UUID tenantId, PartyType partyType) {
    this.id = id;
    this.tenantId = tenantId;
    this.partyType = partyType;
  }

  /**
   * Constructs a new <b>PartyBase</b>.
   *
   * @param id the Universally Unique Identifier (UUID) for the party
   * @param tenantId the Universally Unique Identifier (UUID) for the tenant the party is associated
   *     with
   * @param partyType the party type for the party
   * @param name the name of the party
   */
  public PartyBase(UUID id, UUID tenantId, PartyType partyType, String name) {
    this.id = id;
    this.tenantId = tenantId;
    this.partyType = partyType;
    this.name = name;
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

    PartyBase other = (PartyBase) object;

    return Objects.equals(id, other.id);
  }

  /**
   * Returns the date and time the party was created.
   *
   * @return the date and time the party was created
   */
  @XmlTransient
  public LocalDateTime getCreated() {
    return created;
  }

  /**
   * Returns the Universally Unique Identifier (UUID) for the party.
   *
   * @return the Universally Unique Identifier (UUID) for the party
   */
  @XmlTransient
  public UUID getId() {
    return id;
  }

  /**
   * Returns the name of the party.
   *
   * @return the name of the party
   */
  @XmlTransient
  public String getName() {
    return name;
  }

  /**
   * Returns the party type for the party.
   *
   * @return the party type for the party
   */
  @XmlTransient
  public PartyType getPartyType() {
    return partyType;
  }

  /**
   * Returns the Universally Unique Identifier (UUID) for the tenant the party is associated with.
   *
   * @return the Universally Unique Identifier (UUID) for the tenant the party is associated with
   */
  @XmlTransient
  public UUID getTenantId() {
    return tenantId;
  }

  /**
   * Returns the date and time the party was last updated.
   *
   * @return the date and time the party was last updated
   */
  @XmlTransient
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
   * Set the party type for the party.
   *
   * @param partyType the party type for the party
   */
  public void setPartyType(PartyType partyType) {
    this.partyType = partyType;
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
}
