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
import digital.inception.core.file.FileType;
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
import jakarta.xml.bind.annotation.XmlType;
import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

/**
 * The {@code InteractionAttachmentSummary} class holds the summary information for an interaction
 * attachment.
 */
@Schema(description = "An interaction attachment summary")
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
  "id",
  "tenantId",
  "interactionId",
  "sourceId",
  "sourceReference",
  "fileType",
  "name",
  "hash"
})
@XmlRootElement(name = "InteractionAttachment", namespace = "https://inception.digital/operations")
@XmlType(
    name = "InteractionAttachmentSummary",
    namespace = "https://inception.digital/operations",
    propOrder = {
      "id",
      "tenantId",
      "interactionId",
      "sourceId",
      "sourceReference",
      "fileType",
      "name",
      "hash"
    })
@XmlAccessorType(XmlAccessType.FIELD)
@Entity
@Table(name = "operations_interaction_attachments")
@SuppressWarnings({"unused", "WeakerAccess"})
public class InteractionAttachmentSummary implements Serializable {

  @Serial private static final long serialVersionUID = 1000000;

  /** The file type for the interaction attachment. */
  @Schema(
      description = "The file type for the interaction attachment",
      requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElement(name = "FileType", required = true)
  @NotNull
  @Column(name = "file_type", nullable = false)
  private FileType fileType;

  /** The base-64 encoded SHA-256 hash of the data for the interaction attachment. */
  @Schema(
      description = "The base-64 encoded SHA-256 hash of the data for the interaction attachment",
      requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElement(name = "Hash", required = true)
  @NotNull
  @Size(min = 1, max = 50)
  @Column(name = "hash", length = 50, nullable = false)
  private String hash;

  /** The ID for the interaction attachment. */
  @Schema(
      description = "The ID for the interaction attachment",
      requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElement(name = "Id", required = true)
  @NotNull
  @Id
  @Column(name = "id", nullable = false)
  private UUID id;

  /** The ID for the interaction that the interaction attachment is associated with. */
  @Schema(
      description = "The ID for the interaction that the interaction attachment is associated with",
      requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElement(name = "InteractionId", required = true)
  @NotNull
  @Column(name = "interaction_id", nullable = false)
  private UUID interactionId;

  /** The name of the interaction attachment. */
  @Schema(
      description = "The name of the interaction attachment",
      requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElement(name = "Name", required = true)
  @NotNull
  @Size(min = 1, max = 200)
  @Column(name = "name", length = 200, nullable = false)
  private String name;

  /** The ID for the interaction source the interaction attachment is associated with. */
  @Schema(
      description =
          "The ID for the interaction source the interaction attachment is associated with",
      requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElement(name = "SourceId", required = true)
  @NotNull
  @Column(name = "source_id", nullable = false)
  private UUID sourceId;

  /** The interaction source specific reference for the interaction attachment. */
  @Schema(description = "The interaction source specific reference for the interaction attachment")
  @JsonProperty
  @XmlElement(name = "SourceReference")
  @Size(min = 1, max = 400)
  @Column(name = "source_reference", length = 400)
  private String sourceReference;

  /** The ID for the tenant the interaction attachment is associated with. */
  @Schema(
      description = "The ID for the tenant the interaction attachment is associated with",
      requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElement(name = "TenantId", required = true)
  @NotNull
  @Column(name = "tenant_id", nullable = false)
  private UUID tenantId;

  /** Constructs a new {@code InteractionAttachmentSummary}. */
  public InteractionAttachmentSummary() {}

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

    InteractionAttachmentSummary other = (InteractionAttachmentSummary) object;

    return Objects.equals(id, other.id);
  }

  /**
   * Returns the file type for the interaction attachment.
   *
   * @return the file type for the interaction attachment
   */
  public FileType getFileType() {
    return fileType;
  }

  /**
   * Returns the base-64 encoded SHA-256 hash of the data for the interaction attachment.
   *
   * @return the base-64 encoded SHA-256 hash of the data for the interaction attachment
   */
  public String getHash() {
    return hash;
  }

  /**
   * Returns the ID for the interaction attachment.
   *
   * @return the ID for the interaction attachment
   */
  public UUID getId() {
    return id;
  }

  /**
   * Returns the ID for the interaction that the interaction attachment is associated with.
   *
   * @return the ID for the interaction that the interaction attachment is associated with
   */
  public UUID getInteractionId() {
    return interactionId;
  }

  /**
   * Returns the name of the interaction attachment.
   *
   * @return the name of the interaction attachment
   */
  public String getName() {
    return name;
  }

  /**
   * Returns the ID for the interaction source the interaction attachment is associated with.
   *
   * @return the ID for the interaction source the interaction attachment is associated with
   */
  public UUID getSourceId() {
    return sourceId;
  }

  /**
   * Returns the ID for the tenant the interaction attachment is associated with.
   *
   * @return the ID for the tenant the interaction attachment is associated with
   */
  public UUID getTenantId() {
    return tenantId;
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
}
