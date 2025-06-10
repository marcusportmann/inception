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

package digital.inception.operations.model;

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
 * The {@code InteractionSourceBase} class holds the information common to all types of interaction
 * sources, and is the base class that the classes for the different interaction source types are
 * derived from, e.g. {@code MailboxInteractionSource}.
 *
 * <p>The {@code InteractionSource} and {@code InteractionSourceBase} classes are both JPA entity
 * classes mapped to the same {@code operations_interaction_sources} table.
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
@Table(name = "operations_interaction_sources")
public class InteractionSourceBase implements Serializable {

  @Serial private static final long serialVersionUID = 1000000;

  /** The ID for the interaction source. */
  @NotNull
  @Id
  @Column(name = "id", length = 50, nullable = false)
  private String id;

  /** The name of the interaction source. */
  @NotNull
  @Size(min = 1, max = 100)
  @Column(name = "name", length = 100, nullable = false)
  private String name;

  /** The ID for the tenant the interaction source is associated with. */
  @NotNull
  @Column(name = "tenant_id", nullable = false)
  private UUID tenantId;

  /** The type for the interaction source. */
  @NotNull
  @Column(name = "type", length = 50, nullable = false)
  private InteractionSourceType type;

  /** Constructs a new {@code InteractionSourceBase}. */
  protected InteractionSourceBase() {}

  /**
   * Constructs a new {@code InteractionSourceBase}.
   *
   * @param type the interaction source type
   */
  protected InteractionSourceBase(InteractionSourceType type) {
    this.type = type;
  }

  /**
   * Constructs a new {@code InteractionSourceBase}.
   *
   * @param id the ID for the interaction source
   * @param type the interaction source type
   */
  protected InteractionSourceBase(String id, InteractionSourceType type) {
    this.id = id;
    this.type = type;
  }

  /**
   * Constructs a new {@code InteractionSourceBase}.
   *
   * @param id the ID for the interaction source
   * @param type the type for the interaction source
   * @param name the name of the interaction source
   */
  public InteractionSourceBase(String id, InteractionSourceType type, String name) {
    this.id = id;
    this.type = type;
    this.name = name;
  }

  /**
   * Indicates whether some other object is "equal to" this one.
   *
   * @param object the reference object with which to compare
   * @return {@code true} if this object is the same as the object argument, otherwise {@code false}
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

    InteractionSourceBase other = (InteractionSourceBase) object;

    return Objects.equals(id, other.id);
  }

  /**
   * Returns the ID for the interaction source.
   *
   * @return the ID for the interaction source
   */
  public String getId() {
    return id;
  }

  /**
   * Returns the name of the interaction source.
   *
   * @return the name of the interaction source
   */
  public String getName() {
    return name;
  }

  /**
   * Returns the ID for the tenant the interaction source is associated with.
   *
   * @return the ID for the tenant the interaction source is associated with
   */
  public UUID getTenantId() {
    return tenantId;
  }

  /**
   * Returns the type for the interaction source.
   *
   * @return the type for the interaction source
   */
  public InteractionSourceType getType() {
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
   * Set the ID for the interaction source.
   *
   * @param id the ID for the interaction source
   */
  public void setId(String id) {
    this.id = id;
  }

  /**
   * Set the name of the interaction source.
   *
   * @param name the name of the interaction source
   */
  public void setName(String name) {
    this.name = name;
  }

  /**
   * Set the ID for the tenant the interaction source is associated with.
   *
   * @param tenantId the ID for the tenant the interaction source is associated with
   */
  public void setTenantId(UUID tenantId) {
    this.tenantId = tenantId;
  }

  /**
   * Set the type for the interaction source.
   *
   * @param interactionSourceType the type for the interaction source
   */
  public void setType(InteractionSourceType interactionSourceType) {
    this.type = interactionSourceType;
  }
}
