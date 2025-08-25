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
import java.util.Objects;
import java.util.UUID;

/**
 * The {@code InteractionNote} class holds the information for an interaction note.
 *
 * @author Marcus Portmann
 */
@Schema(description = "A interaction note")
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
  "id",
  "tenantId",
  "interactionId",
  "content",
  "created",
  "createdBy",
  "updated",
  "updatedBy"
})
@XmlRootElement(name = "InteractionNote", namespace = "https://inception.digital/operations")
@XmlType(
    name = "InteractionNote",
    namespace = "https://inception.digital/operations",
    propOrder = {
      "id",
      "tenantId",
      "interactionId",
      "content",
      "created",
      "createdBy",
      "updated",
      "updatedBy"
    })
@XmlAccessorType(XmlAccessType.FIELD)
@Entity
@Table(name = "operations_interaction_notes")
@SuppressWarnings({"unused", "WeakerAccess"})
public class InteractionNote implements Serializable {

  @Serial private static final long serialVersionUID = 1000000;

  /** The content for the interaction note. */
  @Schema(
      description = "The content for the interaction note",
      requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElement(name = "Content", required = true)
  @NotNull
  @Size(min = 1, max = 2000)
  @Column(name = "content", length = 2000, nullable = false)
  private String content;

  /** The date and time the interaction note was created. */
  @Schema(
      description = "The date and time the interaction note was created",
      requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElement(name = "Created", required = true)
  @XmlJavaTypeAdapter(OffsetDateTimeAdapter.class)
  @XmlSchemaType(name = "dateTime")
  @NotNull
  @Column(name = "created", nullable = false)
  private OffsetDateTime created;

  /** The person or system that created the interaction note. */
  @Schema(
      description = "The person or system that created the interaction note",
      requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElement(name = "CreatedBy", required = true)
  @NotNull
  @Size(min = 1, max = 100)
  @Column(name = "created_by", length = 100, nullable = false)
  private String createdBy;

  /** The ID for the interaction note. */
  @Schema(
      description = "The ID for the interaction note",
      requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElement(name = "Id", required = true)
  @NotNull
  @Id
  @Column(name = "id", nullable = false)
  private UUID id;

  /** The ID for the interaction the interaction note is associated with. */
  @Schema(
      description = "The ID for the interaction the interaction note is associated with",
      requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElement(name = "InteractionId", required = true)
  @NotNull
  @Column(name = "interaction_id", length = 50, nullable = false)
  private UUID interactionId;

  /** The ID for the tenant the interaction note is associated with. */
  @Schema(
      description = "The ID for the tenant the interaction note is associated with",
      requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElement(name = "TenantId", required = true)
  @NotNull
  @Column(name = "tenant_id", nullable = false)
  private UUID tenantId;

  /** The date and time the interaction note was last updated. */
  @Schema(description = "The date and time the interaction note was last updated")
  @JsonProperty
  @XmlElement(name = "Updated")
  @XmlJavaTypeAdapter(OffsetDateTimeAdapter.class)
  @XmlSchemaType(name = "dateTime")
  @Column(name = "updated")
  private OffsetDateTime updated;

  /** The person or system that last updated the interaction note. */
  @Schema(description = "The person or system that last updated the interaction note")
  @JsonProperty
  @XmlElement(name = "UpdatedBy")
  @Size(min = 1, max = 100)
  @Column(name = "updated_by", length = 100)
  private String updatedBy;

  /** Constructs a new {@code InteractionNote}. */
  public InteractionNote() {}

  /**
   * Constructs a new {@code InteractionNote}.
   *
   * @param tenantId the ID for the tenant the interaction note is associated with
   * @param interactionId the ID for the interaction the interaction note is associated with
   * @param content the content for the interaction note
   * @param created the date and time the interaction note was created
   * @param createdBy the person or system that created the interaction note
   */
  public InteractionNote(
      UUID tenantId, UUID interactionId, String content, OffsetDateTime created, String createdBy) {
    this.id = UuidCreator.getTimeOrderedEpoch();
    this.tenantId = tenantId;
    this.interactionId = interactionId;
    this.content = content;
    this.created = created;
    this.createdBy = createdBy;
  }

  /**
   * Indicates whether some other object is "equal to" this one.
   *
   * @param object the reference object with which to compare
   * @return {@code true} if this object is the same as the object argument otherwise {@code false}
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

    InteractionNote other = (InteractionNote) object;

    return Objects.equals(id, other.id);
  }

  /**
   * Returns the content for the interaction note.
   *
   * @return the content for the interaction note
   */
  public String getContent() {
    return content;
  }

  /**
   * Returns the date and time the interaction note was created.
   *
   * @return the date and time the interaction note was created
   */
  public OffsetDateTime getCreated() {
    return created;
  }

  /**
   * Returns the person or system that created the interaction note.
   *
   * @return the person or system that created the interaction note
   */
  public String getCreatedBy() {
    return createdBy;
  }

  /**
   * Returns the ID for the interaction note.
   *
   * @return the ID for the interaction note
   */
  public UUID getId() {
    return id;
  }

  /**
   * Returns the ID for the interaction the interaction note is associated with.
   *
   * @return the ID for the interaction the interaction note is associated with
   */
  public UUID getInteractionId() {
    return interactionId;
  }

  /**
   * Returns the ID for the tenant the interaction note is associated with.
   *
   * @return the ID for the tenant the interaction note is associated with
   */
  public UUID getTenantId() {
    return tenantId;
  }

  /**
   * Returns the date and time the interaction note was last updated.
   *
   * @return the date and time the interaction note was last updated
   */
  public OffsetDateTime getUpdated() {
    return updated;
  }

  /**
   * Returns the person or system that last updated the interaction note.
   *
   * @return the person or system that last updated the interaction note
   */
  public String getUpdatedBy() {
    return updatedBy;
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
   * Set the content for the interaction note.
   *
   * @param content the content for the interaction note
   */
  public void setContent(String content) {
    this.content = content;
  }

  /**
   * Set the date and time the interaction note was created.
   *
   * @param created the date and time the interaction note was created
   */
  public void setCreated(OffsetDateTime created) {
    this.created = created;
  }

  /**
   * Set the person or system that created the interaction note.
   *
   * @param createdBy the person or system that created the interaction note
   */
  public void setCreatedBy(String createdBy) {
    this.createdBy = createdBy;
  }

  /**
   * Set the ID for the interaction note.
   *
   * @param id the ID for the interaction note
   */
  public void setId(UUID id) {
    this.id = id;
  }

  /**
   * Set the ID for the interaction the interaction note is associated with.
   *
   * @param interactionId the ID for the interaction the interaction note is associated with
   */
  public void setInteractionId(UUID interactionId) {
    this.interactionId = interactionId;
  }

  /**
   * Set the ID for the tenant the interaction note is associated with.
   *
   * @param tenantId the ID for the tenant the interaction note is associated with
   */
  public void setTenantId(UUID tenantId) {
    this.tenantId = tenantId;
  }

  /**
   * Set the date and time the interaction note was last updated.
   *
   * @param updated the date and time the interaction note was last updated
   */
  public void setUpdated(OffsetDateTime updated) {
    this.updated = updated;
  }

  /**
   * Set the person or system that last updated the interaction note.
   *
   * @param updatedBy the person or system that last updated the interaction note
   */
  public void setUpdatedBy(String updatedBy) {
    this.updatedBy = updatedBy;
  }
}
