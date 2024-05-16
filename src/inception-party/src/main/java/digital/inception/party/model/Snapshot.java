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

package digital.inception.party.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.github.f4b6a3.uuid.UuidCreator;
import digital.inception.core.xml.OffsetDateTimeAdapter;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;
import jakarta.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.io.Serial;
import java.io.Serializable;
import java.time.OffsetDateTime;
import java.util.Objects;
import java.util.UUID;

/**
 * The <b>Snapshot</b> class holds the information for a snapshot, which is a view of the data for
 * an entity at a specific point in time.
 *
 * <p>Changes to entities are recorded as snapshots, which include all the data for the entity
 * serialized as a JSON data structure.
 *
 * @author Marcus Portmann
 */
@Schema(description = "A snapshot")
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({"id", "tenantId", "entityType", "entityId", "timestamp", "data"})
@XmlRootElement(name = "Snapshot", namespace = "https://inception.digital/party")
@XmlType(
    name = "Snapshot",
    namespace = "https://inception.digital/party",
    propOrder = {"id", "tenantId", "entityType", "entityId", "timestamp", "data"})
@XmlAccessorType(XmlAccessType.FIELD)
@Entity
@Table(name = "party_snapshots")
public class Snapshot implements Serializable {

  @Serial private static final long serialVersionUID = 1000000;

  /** The JSON data for the entity. */
  @Schema(description = "The JSON data for the entity", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElement(name = "Data", required = true)
  @NotNull
  @Column(name = "data", nullable = false)
  private String data;

  /** The ID for the entity. */
  @Schema(description = "The ID for the entity", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElement(name = "EntityId", required = true)
  @NotNull
  @Column(name = "entity_id", nullable = false)
  private UUID entityId;

  /** The type of entity. */
  @Schema(description = "The type of entity", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElement(name = "EntityType", required = true)
  @Column(name = "entity_type", length = 10, nullable = false)
  private EntityType entityType;

  /** The ID for the snapshot. */
  @Schema(description = "The ID for the snapshot", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElement(name = "Id", required = true)
  @NotNull
  @Id
  @Column(name = "id", nullable = false)
  private UUID id;

  /** The ID for the tenant the snapshot is associated with. */
  @Schema(
      description = "The ID for the tenant the snapshot is associated with",
      requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElement(name = "TenantId", required = true)
  @NotNull
  @Column(name = "tenant_id", nullable = false)
  private UUID tenantId;

  /** The date and time the snapshot was created. */
  @Schema(
      description = "The date and time the snapshot was created",
      requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElement(name = "Timestamp", required = true)
  @XmlJavaTypeAdapter(OffsetDateTimeAdapter.class)
  @Column(name = "timestamp", nullable = false)
  private OffsetDateTime timestamp;

  /** Constructs a new <b>Snapshot</b>. */
  public Snapshot() {}

  /**
   * Constructs a new <b>Snapshot</b>.
   *
   * @param tenantId the ID for the tenant the snapshot is associated with
   * @param entityType the type of entity the snapshot is associated with
   * @param entityId the ID for the entity the snapshot is associated with
   * @param data the JSON data for the entity
   */
  public Snapshot(UUID tenantId, EntityType entityType, UUID entityId, String data) {
    this.id = UuidCreator.getShortPrefixComb();
    this.tenantId = tenantId;
    this.entityType = entityType;
    this.entityId = entityId;
    this.timestamp = OffsetDateTime.now();
    this.data = data;
  }

  /**
   * Indicates whether some other object is "equal to" this one.
   *
   * @param object the reference object with which to compare
   * @return <b>true</b> if this object is the same as the object argument, otherwise <b>false</b>
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

    Snapshot other = (Snapshot) object;

    return Objects.equals(id, other.id);
  }

  /**
   * Returns the JSON data for the entity.
   *
   * @return the JSON data for the entity
   */
  public String getData() {
    return data;
  }

  /**
   * Returns the ID for the entity.
   *
   * @return the ID for the entity
   */
  public UUID getEntityId() {
    return entityId;
  }

  /**
   * Returns the type of entity.
   *
   * @return the type of entity
   */
  public EntityType getEntityType() {
    return entityType;
  }

  /**
   * Returns the ID for the snapshot.
   *
   * @return the ID for the snapshot
   */
  public UUID getId() {
    return id;
  }

  /**
   * Returns the ID for the tenant the snapshot is associated with.
   *
   * @return the ID for the tenant the snapshot is associated with
   */
  public UUID getTenantId() {
    return tenantId;
  }

  /**
   * Returns the date and time the snapshot was created.
   *
   * @return the date and time the snapshot was created
   */
  public OffsetDateTime getTimestamp() {
    return timestamp;
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
   * Set the JSON data for the entity.
   *
   * @param data the JSON data for the entity
   */
  public void setData(String data) {
    this.data = data;
  }

  /**
   * Set the ID for the entity.
   *
   * @param entityId the ID for the entity
   */
  public void setEntityId(UUID entityId) {
    this.entityId = entityId;
  }

  /**
   * Set the type of entity.
   *
   * @param entityType the type of entity
   */
  public void setEntityType(EntityType entityType) {
    this.entityType = entityType;
  }

  /**
   * Set the ID for the snapshot.
   *
   * @param id the ID for the snapshot
   */
  public void setId(UUID id) {
    this.id = id;
  }

  /**
   * Set the ID for the tenant the snapshot is associated with.
   *
   * @param tenantId the ID for the tenant the snapshot is associated with
   */
  public void setTenantId(UUID tenantId) {
    this.tenantId = tenantId;
  }

  /**
   * Set the date and time the snapshot was created.
   *
   * @param timestamp the date and time the snapshot was created
   */
  public void setTimestamp(OffsetDateTime timestamp) {
    this.timestamp = timestamp;
  }
}
