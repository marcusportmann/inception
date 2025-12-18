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

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.github.f4b6a3.uuid.UuidCreator;
import digital.inception.core.xml.OffsetDateTimeAdapter;
import digital.inception.processor.AbstractProcessableObject;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlSchemaType;
import jakarta.xml.bind.annotation.XmlType;
import jakarta.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.io.Serial;
import java.io.Serializable;
import java.time.OffsetDateTime;
import java.util.UUID;

/**
 * The {@code Event} class holds the information for an event.
 *
 * @author Marcus Portmann
 */
@Schema(description = "An event")
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
  "id",
  "tenantId",
  "status",
  "objectType",
  "objectId",
  "type",
  "occurred",
  "actor",
  "processed",
  "processingAttempts",
  "processingTime",
  "processingSuspended",
  "lastProcessed",
  "nextProcessed",
  "locked",
  "lockName"
})
@XmlRootElement(name = "Event", namespace = "https://inception.digital/operations")
@XmlType(
    name = "Event",
    namespace = "https://inception.digital/operations",
    propOrder = {
      "id",
      "tenantId",
      "status",
      "objectType",
      "objectId",
      "type",
      "occurred",
      "actor",
      "processed",
      "processingAttempts",
      "processingTime",
      "processingSuspended",
      "lastProcessed",
      "nextProcessed",
      "locked",
      "lockName"
    })
@XmlAccessorType(XmlAccessType.FIELD)
@Entity
@Table(name = "operations_events")
@SuppressWarnings({"unused", "WeakerAccess"})
public class Event extends AbstractProcessableObject<UUID, EventStatus> implements Serializable {

  @Serial private static final long serialVersionUID = 1000000;

  /** The person or system who completed the action that led to the event. */
  @Schema(
      description = "The person or system who completed the action that led to the event",
      requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElement(name = "Actor", required = true)
  @NotNull
  @Size(min = 1, max = 100)
  @Column(name = "actor", length = 100, nullable = false)
  private String actor;

  /** The ID for the event. */
  @Schema(description = "The ID for the event", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElement(name = "Id", required = true)
  @NotNull
  @Id
  @Column(name = "id", nullable = false)
  private UUID id;

  /** The ID for the object the event is associated with. */
  @Schema(
      description = "The ID for the object the event is associated with",
      requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElement(name = "ObjectId", required = true)
  @NotNull
  @Column(name = "object_id", nullable = false)
  private UUID objectId;

  /** The type of object the event is associated with. */
  @Schema(
      description = "The type of object the event is associated with",
      requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElement(name = "ObjectType", required = true)
  @NotNull
  @Column(name = "object_type", nullable = false)
  private ObjectType objectType;

  /** The date and time the event occurred. */
  @Schema(
      description = "The date and time the event occurred",
      requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElement(name = "Occurred", required = true)
  @XmlJavaTypeAdapter(OffsetDateTimeAdapter.class)
  @XmlSchemaType(name = "dateTime")
  @Column(name = "occurred", nullable = false)
  private OffsetDateTime occurred;

  /** The status of the event. */
  @Schema(description = "The status of the event", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElement(name = "Status", required = true)
  @NotNull
  @Column(name = "status", nullable = false)
  private EventStatus status;

  /** The ID for the tenant the event is associated with. */
  @Schema(
      description = "The ID for the tenant the event is associated with",
      requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElement(name = "TenantId", required = true)
  @NotNull
  @Column(name = "tenant_id", nullable = false)
  private UUID tenantId;

  /** The event type. */
  @Schema(description = "The event type", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElement(name = "Type", required = true)
  @NotNull
  @Column(name = "type", nullable = false, length = 50)
  private EventType type;

  /** Constructs a new {@code Event}. */
  public Event() {
    super(EventStatus.QUEUED);
  }

  /**
   * Constructs a new {@code Event}.
   *
   * @param tenantId the ID for the tenant the event is associated with
   * @param objectType the type of object the event is associated with
   * @param objectId the ID for the object the event is associated with
   * @param type the event type
   * @param occurred the date and time the event occurred
   * @param actor the person or system who completed the action that led to the event
   */
  public Event(
      UUID tenantId,
      ObjectType objectType,
      UUID objectId,
      EventType type,
      OffsetDateTime occurred,
      String actor) {
    super(EventStatus.QUEUED);

    this.id = UuidCreator.getTimeOrderedEpoch();
    this.tenantId = tenantId;
    this.objectType = objectType;
    this.objectId = objectId;
    this.type = type;
    this.occurred = occurred;
    this.actor = actor;
    this.status = EventStatus.QUEUED;
  }

  /**
   * Returns the person or system who completed the action that led to the event.
   *
   * @return the person or system who completed the action that led to the event
   */
  public String getActor() {
    return actor;
  }

  /**
   * Returns the ID for the event.
   *
   * @return the ID for the event
   */
  @Override
  public UUID getId() {
    return id;
  }

  @JsonIgnore
  @Override
  public String getIdAsKey() {
    return id.toString();
  }

  /**
   * Returns the ID for the object the event is associated with.
   *
   * @return the ID for the object the event is associated with
   */
  public UUID getObjectId() {
    return objectId;
  }

  /**
   * Returns the type of object the event is associated with.
   *
   * @return the type of object the event is associated with
   */
  public ObjectType getObjectType() {
    return objectType;
  }

  /**
   * Returns the date and time the event occurred.
   *
   * @return the date and time the event occurred
   */
  public OffsetDateTime getOccurred() {
    return occurred;
  }

  /**
   * Returns the status of the event.
   *
   * @return the status of the event
   */
  public EventStatus getStatus() {
    return status;
  }

  /**
   * Returns the ID for the tenant the event is associated with.
   *
   * @return the ID for the tenant the event is associated with
   */
  public UUID getTenantId() {
    return tenantId;
  }

  /**
   * Returns the event type.
   *
   * @return the event type
   */
  public EventType getType() {
    return type;
  }

  /**
   * Sets the person or system who completed the action that led to the event.
   *
   * @param actor the person or system who completed the action that led to the event
   */
  public void setActor(String actor) {
    this.actor = actor;
  }

  /**
   * Sets the ID for the event.
   *
   * @param id the ID for the event
   */
  public void setId(UUID id) {
    this.id = id;
  }

  /**
   * Sets the ID for the object the event is associated with.
   *
   * @param objectId the ID for the object the event is associated with
   */
  public void setObjectId(UUID objectId) {
    this.objectId = objectId;
  }

  /**
   * Sets the type of object the event is associated with.
   *
   * @param objectType the type of object the event is associated with
   */
  public void setObjectType(ObjectType objectType) {
    this.objectType = objectType;
  }

  /**
   * Sets the date and time the event occurred.
   *
   * @param occurred the date and time the event occurred
   */
  public void setOccurred(OffsetDateTime occurred) {
    this.occurred = occurred;
  }

  /**
   * Sets the status of the event.
   *
   * @param status the status of the event
   */
  public void setStatus(EventStatus status) {
    this.status = status;
  }

  /**
   * Sets the ID for the tenant the event is associated with.
   *
   * @param tenantId the ID for the tenant the event is associated with
   */
  public void setTenantId(UUID tenantId) {
    this.tenantId = tenantId;
  }

  /**
   * Sets the event type.
   *
   * @param type the event type
   */
  public void setType(EventType type) {
    this.type = type;
  }
}
