/*
 * Copyright Marcus Portmann
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

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlTransient;
import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

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
@Table(name = "party_parties")
public class PartyBase implements Serializable {

  @Serial private static final long serialVersionUID = 1000000;

  /** The ID for the party. */
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

  /** The ID for the tenant the party is associated with. */
  @NotNull
  @Column(name = "tenant_id", nullable = false)
  private UUID tenantId;

  /** The type for the party. */
  @NotNull
  @Column(name = "type", length = 50, nullable = false)
  private PartyType type;

  /** Constructs a new <b>PartyBase</b>. */
  protected PartyBase() {}

  /**
   * Constructs a new <b>PartyBase</b>.
   *
   * @param type the party type
   */
  protected PartyBase(PartyType type) {
    this.type = type;
  }

  /**
   * Constructs a new <b>PartyBase</b>.
   *
   * @param id the ID for the party
   * @param tenantId the ID for the tenant the party is associated with
   * @param type the party type
   */
  protected PartyBase(UUID id, UUID tenantId, PartyType type) {
    this.id = id;
    this.tenantId = tenantId;
    this.type = type;
  }

  /**
   * Constructs a new <b>PartyBase</b>.
   *
   * @param id the ID for the party
   * @param tenantId the ID for the tenant the party is associated with
   * @param type the type for the party
   * @param name the name of the party
   */
  public PartyBase(UUID id, UUID tenantId, PartyType type, String name) {
    this.id = id;
    this.tenantId = tenantId;
    this.type = type;
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
   * Returns the ID for the party.
   *
   * @return the ID for the party
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
   * Returns the ID for the tenant the party is associated with.
   *
   * @return the ID for the tenant the party is associated with
   */
  @XmlTransient
  public UUID getTenantId() {
    return tenantId;
  }

  /**
   * Returns the type for the party.
   *
   * @return the type for the party
   */
  @XmlTransient
  public PartyType getType() {
    return type;
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
   * Set the ID for the party.
   *
   * @param id the ID for the party
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
   * Set the ID for the tenant the party is associated with.
   *
   * @param tenantId the ID for the tenant the party is associated with
   */
  public void setTenantId(UUID tenantId) {
    this.tenantId = tenantId;
  }

  /**
   * Set the type for the party.
   *
   * @param partyType the type for the party
   */
  public void setType(PartyType partyType) {
    this.type = partyType;
  }
}
