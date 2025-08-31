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
import digital.inception.core.util.StringUtil;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinColumns;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OrderBy;
import jakarta.persistence.Table;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlElementWrapper;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;
import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * The {@code WorkflowDefinitionSummary} class holds the information for a workflow definition
 * summary.
 *
 * @author Marcus Portmann
 */
@Schema(description = "A workflow definition summary")
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
  "id",
  "version",
  "categoryId",
  "tenantId",
  "name",
  "engineId",
  "documentDefinitions"
})
@XmlRootElement(
    name = "WorkflowDefinitionSummary",
    namespace = "https://inception.digital/operations")
@XmlType(
    name = "WorkflowDefinitionSummary",
    namespace = "https://inception.digital/operations",
    propOrder = {
      "id",
      "version",
      "categoryId",
      "tenantId",
      "name",
      "engineId",
      "documentDefinitions"
    })
@XmlAccessorType(XmlAccessType.FIELD)
@Entity
@Table(name = "operations_workflow_definitions")
@IdClass(WorkflowDefinitionSummaryId.class)
public class WorkflowDefinitionSummary implements Serializable {

  @Serial private static final long serialVersionUID = 1000000;

  /** The document definitions associated with the workflow definition. */
  @Schema(
      description = "The document definitions associated with the workflow definition",
      requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElementWrapper(name = "DocumentDefinitions", required = true)
  @XmlElement(name = "DocumentDefinition", required = true)
  @Valid
  @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
  @OrderBy("documentDefinitionId")
  @JoinColumns({
    @JoinColumn(
        name = "workflow_definition_id",
        referencedColumnName = "id",
        insertable = false,
        updatable = false),
    @JoinColumn(
        name = "workflow_definition_version",
        referencedColumnName = "version",
        insertable = false,
        updatable = false)
  })
  private final List<WorkflowDefinitionDocumentDefinition> documentDefinitions = new ArrayList<>();

  /** The ID for the workflow definition category the workflow definition is associated with. */
  @Schema(
      description =
          "The ID for the workflow definition category the workflow definition is associated with",
      requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElement(name = "CategoryId", required = true)
  @NotNull
  @Size(min = 1, max = 50)
  @Column(name = "category_id", length = 50, nullable = false)
  private String categoryId;

  /** The ID for the workflow engine the workflow definition is associated with. */
  @Schema(
      description = "The ID for the workflow engine the workflow definition is associated with",
      requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElement(name = "EngineId", required = true)
  @NotNull
  @Size(min = 1, max = 50)
  @Column(name = "engine_id", length = 50, nullable = false)
  private String engineId;

  /** The ID for the workflow definition. */
  @Schema(
      description = "The ID for the workflow definition",
      requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElement(name = "Id", required = true)
  @NotNull
  @Size(min = 1, max = 50)
  @Id
  @Column(name = "id", length = 50, nullable = false)
  private String id;

  /** The name of the workflow definition. */
  @Schema(
      description = "The name of the workflow definition",
      requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElement(name = "Name", required = true)
  @NotNull
  @Size(min = 1, max = 100)
  @Column(name = "name", length = 100, nullable = false)
  private String name;

  /** The ID for the tenant the workflow definition is specific to. */
  @Schema(description = "The ID for the tenant the workflow definition is specific to")
  @JsonProperty
  @XmlElement(name = "TenantId")
  @Column(name = "tenant_id")
  private UUID tenantId;

  /** The version of the workflow definition. */
  @Schema(
      description = "The version of the workflow definition",
      requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElement(name = "Version", required = true)
  @NotNull
  @Id
  @Column(name = "version", nullable = false)
  private int version;

  /** Constructs a new {@code WorkflowDefinitionSummary}. */
  public WorkflowDefinitionSummary() {}

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

    WorkflowDefinitionSummary other = (WorkflowDefinitionSummary) object;

    return StringUtil.equalsIgnoreCase(id, other.id) && (version == other.version);
  }

  /**
   * Returns the ID for the workflow definition category the workflow definition is associated with.
   *
   * @return the ID for the workflow definition category the workflow definition is associated with
   */
  public String getCategoryId() {
    return categoryId;
  }

  /**
   * Returns the document definitions associated with the workflow definition.
   *
   * @return the document definitions associated with the workflow definition
   */
  public List<WorkflowDefinitionDocumentDefinition> getDocumentDefinitions() {
    return documentDefinitions;
  }

  /**
   * Returns the ID for the workflow engine the workflow definition is associated with.
   *
   * @return the ID for the workflow engine the workflow definition is associated with
   */
  public String getEngineId() {
    return engineId;
  }

  /**
   * Returns the ID for the workflow definition.
   *
   * @return the ID for the workflow definition
   */
  public String getId() {
    return id;
  }

  /**
   * Returns the name of the workflow definition.
   *
   * @return the name of the workflow definition
   */
  public String getName() {
    return name;
  }

  /**
   * Returns the ID for the tenant the workflow definition is specific to.
   *
   * @return the ID for the tenant the workflow definition is specific to
   */
  public UUID getTenantId() {
    return tenantId;
  }

  /**
   * Returns the version of the workflow definition.
   *
   * @return the version of the workflow definition
   */
  public int getVersion() {
    return version;
  }

  /**
   * Returns a hash code value for the object.
   *
   * @return a hash code value for the object
   */
  @Override
  public int hashCode() {
    return ((id == null) ? 0 : id.hashCode()) + version;
  }
}
