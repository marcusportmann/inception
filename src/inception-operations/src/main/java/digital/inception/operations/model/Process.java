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
 * The <b>Process</b> class holds the information for a process.
 *
 * @author Marcus Portmann
 */
@Schema(description = "A process")
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
  "id",
  "tenantId",
  "parentId",
  "definitionId",
  "definitionVersion",
  "status",
  "externalReference",
  "created",
  "createdBy",
  "updated",
  "updatedBy",
  "data"
})
@XmlRootElement(name = "Process", namespace = "https://inception.digital/operations")
@XmlType(
    name = "Process",
    namespace = "https://inception.digital/operations",
    propOrder = {
      "id",
      "tenantId",
      "parentId",
      "definitionId",
      "definitionVersion",
      "status",
      "externalReference",
      "created",
      "createdBy",
      "updated",
      "updatedBy",
      "data"
    })
@XmlAccessorType(XmlAccessType.FIELD)
@Entity
@Table(name = "operations_processes")
@SuppressWarnings({"unused", "WeakerAccess"})
public class Process implements Serializable {

  @Serial private static final long serialVersionUID = 1000000;

  /** The date and time the process was created. */
  @Schema(
      description = "The date and time the process was created",
      requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElement(name = "Created", required = true)
  @XmlJavaTypeAdapter(OffsetDateTimeAdapter.class)
  @XmlSchemaType(name = "dateTime")
  @NotNull
  @Column(name = "created", nullable = false)
  private OffsetDateTime created;

  /** The person or system that created the process. */
  @Schema(
      description = "The person or system that created the process",
      requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElement(name = "CreatedBy", required = true)
  @NotNull
  @Size(min = 1, max = 100)
  @Column(name = "created_by", length = 100, nullable = false)
  private String createdBy;

  /** The data for the process. */
  @Schema(description = "The data for the process")
  @JsonProperty
  @XmlElement(name = "Data")
  @Size(max = 10485760)
  @Column(name = "data", length = 10485760)
  private String data;

  /** The ID for the process definition the process is associated with. */
  @Schema(
      description = "The ID for the process definition the process is associated with",
      requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElement(name = "DefinitionId", required = true)
  @NotNull
  @Size(min = 1, max = 50)
  @Column(name = "definition_id", length = 50, nullable = false)
  private String definitionId;

  /** The version of the process definition the process is associated with. */
  @Schema(
      description = "The version of the process definition the process is associated with",
      requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElement(name = "DefinitionVersion", required = true)
  @NotNull
  @Column(name = "definition_version", nullable = false)
  private int definitionVersion;

  /** The external reference for the process. */
  @Schema(description = "The external reference for the process")
  @JsonProperty
  @XmlElement(name = "ExternalReference")
  @Size(max = 100)
  @Column(name = "external_reference", length = 100)
  private String externalReference;

  /** The ID for the process. */
  @Schema(description = "The ID for the process", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElement(name = "Id", required = true)
  @NotNull
  @Id
  @Column(name = "id", nullable = false)
  private UUID id;

  /** The ID for the parent process. */
  @Schema(description = "The ID for the parent process")
  @JsonProperty
  @XmlElement(name = "ParentId")
  @Column(name = "parent_id")
  private UUID parentId;

  /** The status of the process. */
  @Schema(description = "The status of the process", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElement(name = "Status", required = true)
  @NotNull
  @Column(name = "status", nullable = false)
  private ProcessStatus status;

  /** The ID for the tenant the process is associated with. */
  @Schema(
      description = "The ID for the tenant the process is associated with",
      requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElement(name = "TenantId", required = true)
  @NotNull
  @Column(name = "tenant_id", nullable = false)
  private UUID tenantId;

  /** The date and time the process was last updated. */
  @Schema(description = "The date and time the process was last updated")
  @JsonProperty
  @XmlElement(name = "Updated")
  @XmlJavaTypeAdapter(OffsetDateTimeAdapter.class)
  @XmlSchemaType(name = "dateTime")
  @Column(name = "updated")
  private OffsetDateTime updated;

  /** The person or system that last updated the process. */
  @Schema(description = "The person or system that last updated the process")
  @JsonProperty
  @XmlElement(name = "UpdatedBy")
  @Size(min = 1, max = 100)
  @Column(name = "updated_by", length = 100)
  private String updatedBy;

  /** Constructs a new <b>Process</b>. */
  public Process() {}

  /**
   * Constructs a new <b>Process</b>.
   *
   * @param id the ID for the process
   * @param definitionId the ID for the process definition the process is associated with
   * @param definitionVersion the version of the process definition the process is associated with
   * @param status the status of the process
   * @param data the data for the process
   * @param createdBy the person or system that created the process
   */
  public Process(
      UUID id, String definitionId, int definitionVersion, ProcessStatus status, String data, String createdBy) {
    this.id = id;
    this.definitionId = definitionId;
    this.definitionVersion = definitionVersion;
    this.status = status;
    this.data = data;
    this.created = OffsetDateTime.now();
    this.createdBy = createdBy;
  }

  /**
   * Constructs a new <b>Process</b>.
   *
   * @param id the ID for the process
   * @param parentId the ID for the parent process
   * @param definitionId the ID for the process definition the process is associated with
   * @param definitionVersion the version of the process definition the process is associated with
   * @param status the status of the process
   * @param data the data for the process
   * @param createdBy the person or system that created the process
   */
  public Process(
      UUID id,
      UUID parentId,
      String definitionId,
      int definitionVersion,
      ProcessStatus status,
      String data,
      String createdBy) {
    this.id = id;
    this.parentId = parentId;
    this.definitionId = definitionId;
    this.definitionVersion = definitionVersion;
    this.status = status;
    this.data = data;
    this.created = OffsetDateTime.now();
    this.createdBy = createdBy;
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

    Process other = (Process) object;

    return Objects.equals(id, other.id);
  }

  /**
   * Returns the date and time the process was created.
   *
   * @return the date and time the process was created
   */
  public OffsetDateTime getCreated() {
    return created;
  }

  /**
   * Returns the person or system that created the process.
   *
   * @return the person or system that created the process
   */
  public String getCreatedBy() {
    return createdBy;
  }

  /**
   * Returns the data for the process.
   *
   * @return the data for the process
   */
  public String getData() {
    return data;
  }

  /**
   * Returns the ID for the process definition the process is associated with.
   *
   * @return the ID for the process definition the process is associated with
   */
  public String getDefinitionId() {
    return definitionId;
  }

  /**
   * Returns the version of the process definition the process is associated with.
   *
   * @return the version of the process definition the process is associated with
   */
  public int getDefinitionVersion() {
    return definitionVersion;
  }

  /**
   * Returns the external reference for the process.
   *
   * @return the external reference for the process
   */
  public String getExternalReference() {
    return externalReference;
  }

  /**
   * Returns the ID for the process.
   *
   * @return the ID for the process
   */
  public UUID getId() {
    return id;
  }

  /**
   * Returns the ID for the parent process.
   *
   * @return the ID for the parent process
   */
  public UUID getParentId() {
    return parentId;
  }

  /**
   * Returns the status of the process.
   *
   * @return the status of the process
   */
  public ProcessStatus getStatus() {
    return status;
  }

  /**
   * Returns the ID for the tenant the process is associated with.
   *
   * @return the ID for the tenant the process is associated with
   */
  public UUID getTenantId() {
    return tenantId;
  }

  /**
   * Returns the date and time the process was last updated.
   *
   * @return the date and time the process was last updated
   */
  public OffsetDateTime getUpdated() {
    return updated;
  }

  /**
   * Returns the person or system that last updated the process.
   *
   * @return the person or system that last updated the process
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
   * Set the date and time the process was created.
   *
   * @param created the date and time the process was created
   */
  public void setCreated(OffsetDateTime created) {
    this.created = created;
  }

  /**
   * Set the person or system that created the process.
   *
   * @param createdBy the person or system that created the process
   */
  public void setCreatedBy(String createdBy) {
    this.createdBy = createdBy;
  }

  /**
   * Set the data for the process.
   *
   * @param data the data for the process
   */
  public void setData(String data) {
    this.data = data;
  }

  /**
   * Set the ID for the process definition the process is associated with.
   *
   * @param definitionId the ID for the process definition the process is associated with
   */
  public void setDefinitionId(String definitionId) {
    this.definitionId = definitionId;
  }

  /**
   * Set the version of the process definition the process is associated with.
   *
   * @param definitionVersion the version of the process definition the process is associated with
   */
  public void setDefinitionVersion(int definitionVersion) {
    this.definitionVersion = definitionVersion;
  }

  /**
   * Set the external reference for the process.
   *
   * @param externalReference the external reference for the process
   */
  public void setExternalReference(String externalReference) {
    this.externalReference = externalReference;
  }

  /**
   * Set the ID for the process.
   *
   * @param id the ID for the process
   */
  public void setId(UUID id) {
    this.id = id;
  }

  /**
   * Set the ID for the parent process.
   *
   * @param parentId the ID for the parent process
   */
  public void setParentId(UUID parentId) {
    this.parentId = parentId;
  }

  /**
   * Set the status of the process.
   *
   * @param status the status of the process
   */
  public void setStatus(ProcessStatus status) {
    this.status = status;
  }

  /**
   * Set the ID for the tenant the process is associated with.
   *
   * @param tenantId the ID for the tenant the process is associated with
   */
  public void setTenantId(UUID tenantId) {
    this.tenantId = tenantId;
  }

  /**
   * Set the date and time the process was last updated.
   *
   * @param updated the date and time the process was last updated
   */
  public void setUpdated(OffsetDateTime updated) {
    this.updated = updated;
  }

  /**
   * Set the person or system that last updated the process.
   *
   * @param updatedBy the person or system that last updated the process
   */
  public void setUpdatedBy(String updatedBy) {
    this.updatedBy = updatedBy;
  }
}
