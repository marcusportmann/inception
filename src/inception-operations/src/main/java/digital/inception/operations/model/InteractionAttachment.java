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
 * The {@code InteractionAttachment} class holds the information for an interaction attachment.
 *
 * @author Marcus Portmann
 */
@Schema(description = "An interaction attachment")
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
  "id",
  "tenantId",
  "interactionId",
  "sourceId",
  "sourceReference",
  "fileType",
  "name",
  "hash",
  "data"
})
@XmlRootElement(name = "InteractionAttachment", namespace = "https://inception.digital/operations")
@XmlType(
    name = "InteractionAttachment",
    namespace = "https://inception.digital/operations",
    propOrder = {
      "id",
      "tenantId",
      "interactionId",
      "sourceId",
      "sourceReference",
      "fileType",
      "name",
      "hash",
      "data"
    })
@XmlAccessorType(XmlAccessType.FIELD)
@Entity
@Table(name = "operations_interaction_attachments")
@SuppressWarnings({"unused", "WeakerAccess"})
public class InteractionAttachment implements Serializable {

  @Serial private static final long serialVersionUID = 1000000;

  /** The data for the interaction attachment. */
  @Schema(
      description = "The data for the interaction attachment",
      requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElement(name = "Data", required = true)
  @NotNull
  @Size(min = 1, max = 20971520)
  @Column(name = "data", nullable = false)
  private byte[] data;

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

  /** Constructs a new {@code InteractionAttachment}. */
  public InteractionAttachment() {}

  /**
   * Constructs a new {@code InteractionAttachment}.
   *
   * @param id the ID for the interaction attachment
   * @param tenantId the ID for the tenant the interaction attachment is associated with
   * @param interactionId the ID for the interaction that the interaction attachment is associated
   *     with
   * @param sourceId the ID for the interaction source the interaction attachment is associated with
   * @param sourceReference the interaction source specific reference for the interaction attachment
   * @param fileType the file type for the interaction attachment
   * @param name the name of the interaction attachment
   * @param hash the base-64 encoded SHA-256 hash of the data for the interaction attachment
   * @param data the data for the interaction attachment
   */
  public InteractionAttachment(
      UUID id,
      UUID tenantId,
      UUID interactionId,
      UUID sourceId,
      String sourceReference,
      FileType fileType,
      String name,
      String hash,
      byte[] data) {
    this.id = id;
    this.tenantId = tenantId;
    this.interactionId = interactionId;
    this.sourceId = sourceId;
    this.sourceReference = sourceReference;
    this.fileType = fileType;
    this.name = name;
    this.hash = hash;
    this.data = data;
  }

  /**
   * Constructs a new {@code InteractionAttachment}.
   *
   * @param id the ID for the interaction attachment
   * @param tenantId the ID for the tenant the interaction attachment is associated with
   * @param interactionId the ID for the interaction that the interaction attachment is associated
   *     with
   * @param sourceId the ID for the interaction source the interaction attachment is associated with
   * @param fileType the file type for the interaction attachment
   * @param name the name of the interaction attachment
   * @param hash the base-64 encoded SHA-256 hash of the data for the interaction attachment
   * @param data the data for the interaction attachment
   */
  public InteractionAttachment(
      UUID id,
      UUID tenantId,
      UUID interactionId,
      UUID sourceId,
      FileType fileType,
      String name,
      String hash,
      byte[] data) {
    this.id = id;
    this.tenantId = tenantId;
    this.interactionId = interactionId;
    this.sourceId = sourceId;
    this.fileType = fileType;
    this.name = name;
    this.hash = hash;
    this.data = data;
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

    InteractionAttachment other = (InteractionAttachment) object;

    return Objects.equals(id, other.id);
  }

  /**
   * Returns the binary data for the interaction attachment.
   *
   * @return the binary data for the interaction attachment
   */
  public byte[] getData() {
    return data;
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

  /**
   * Sets the binary data for the interaction attachment.
   *
   * @param data the binary data for the interaction attachment
   */
  public void setData(byte[] data) {
    this.data = data;
  }

  /**
   * Sets the file type for the interaction attachment.
   *
   * @param fileType the file type for the interaction attachment
   */
  public void setFileType(FileType fileType) {
    this.fileType = fileType;
  }

  /**
   * Sets the base-64 encoded SHA-256 hash of the data for the interaction attachment.
   *
   * @param hash the base-64 encoded SHA-256 hash of the data for the interaction attachment
   */
  public void setHash(String hash) {
    this.hash = hash;
  }

  /**
   * Sets the ID for the interaction attachment.
   *
   * @param id the ID for the interaction attachment
   */
  public void setId(UUID id) {
    this.id = id;
  }

  /**
   * Sets the ID for the interaction that the interaction attachment is associated with.
   *
   * @param interactionId the ID for the interaction that the interaction attachment is associated
   *     with
   */
  public void setInteractionId(UUID interactionId) {
    this.interactionId = interactionId;
  }

  /**
   * Sets the name of the interaction attachment.
   *
   * @param name the name of the interaction attachment
   */
  public void setName(String name) {
    this.name = name;
  }

  /**
   * Sets the ID for the interaction source the interaction attachment is associated with.
   *
   * @param sourceId the ID for the interaction source the interaction attachment is associated with
   */
  public void setSourceId(UUID sourceId) {
    this.sourceId = sourceId;
  }

  /**
   * Sets the interaction source specific reference for the interaction attachment.
   *
   * @param sourceReference the interaction source specific reference for the interaction attachment
   */
  public void setSourceReference(String sourceReference) {
    this.sourceReference = sourceReference;
  }

  /**
   * Sets the ID for the tenant the interaction attachment is associated with.
   *
   * @param tenantId the ID for the tenant the interaction attachment is associated with
   */
  public void setTenantId(UUID tenantId) {
    this.tenantId = tenantId;
  }

  /**
   * Returns the interaction source specific reference for the interaction attachment.
   *
   * @return the interaction source specific reference for the interaction attachment
   */
  String getSourceReference() {
    return sourceReference;
  }
}
