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
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import digital.inception.core.validation.ValidationSchemaType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
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
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

/**
 * The <b>WorkflowDefinition</b> class holds the information for a workflow definition.
 *
 * @author Marcus Portmann
 */
@Schema(description = "A workflow definition")
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
  "id",
  "version",
  "tenantId",
  "name",
  "documentDefinitions",
  "validationSchemaType",
  "validationSchema"
})
@XmlRootElement(name = "WorkflowDefinition", namespace = "https://inception.digital/operations")
@XmlType(
    name = "WorkflowDefinition",
    namespace = "https://inception.digital/operations",
    propOrder = {
      "id",
      "version",
      "tenantId",
      "name",
      "documentDefinitions",
      "validationSchemaType",
      "validationSchema"
    })
@XmlAccessorType(XmlAccessType.FIELD)
@Entity
@Table(name = "operations_workflow_definitions")
@IdClass(WorkflowDefinitionId.class)
public class WorkflowDefinition implements Serializable {

  @Serial private static final long serialVersionUID = 1000000;

  /** The document definitions associated with the workflow definition. */
  @Schema(
      description = "The document definitions associated with the workflow definition",
      requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @JsonManagedReference("workflowDefinitionDocumentDefinitionReference")
  @XmlElementWrapper(name = "DocumentDefinitions", required = true)
  @XmlElement(name = "DocumentDefinition", required = true)
  @Valid
  @OneToMany(
      mappedBy = "workflowDefinition",
      cascade = CascadeType.ALL,
      fetch = FetchType.EAGER,
      orphanRemoval = true)
  @OrderBy("documentDefinitionId")
  private final List<WorkflowDefinitionDocumentDefinition> documentDefinitions = new ArrayList<>();

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

  /** The XML (XSD) or JSON validation schema for the workflow definition. */
  @Schema(description = "The XML (XSD) or JSON validation schema for the workflow definition")
  @JsonProperty
  @XmlElement(name = "ValidationSchema")
  @Size(min = 1, max = 10485760)
  @Column(name = "validation_schema", length = 10485760)
  private String validationSchema;

  /** The validation schema type for the workflow definition. */
  @Schema(description = "The validation schema type for the workflow definition")
  @JsonProperty
  @XmlElement(name = "ValidationSchemaType")
  @Column(name = "validation_schema_type", length = 50)
  private ValidationSchemaType validationSchemaType;

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

  /** Constructs a new <b>WorkflowDefinition</b>. */
  public WorkflowDefinition() {}

  /**
   * Constructs a new <b>WorkflowDefinition</b>.
   *
   * @param id ID for the workflow definition
   * @param version the version of the workflow definition
   * @param name the name of the workflow definition
   */
  public WorkflowDefinition(String id, int version, String name) {
    this.id = id;
    this.version = version;
    this.name = name;
  }

  /**
   * Constructs a new <b>WorkflowDefinition</b>.
   *
   * @param id ID for the workflow definition
   * @param version the version of the workflow definition
   * @param name the name of the workflow definition
   * @param validationSchemaType the schema type for the workflow definition
   * @param validationSchema the XML (XSD) or JSON schema for the workflow definition
   */
  public WorkflowDefinition(
      String id,
      int version,
      String name,
      ValidationSchemaType validationSchemaType,
      String validationSchema) {
    this.id = id;
    this.version = version;
    this.name = name;
    this.validationSchemaType = validationSchemaType;
    this.validationSchema = validationSchema;
  }

  /**
   * Constructs a new <b>WorkflowDefinition</b>.
   *
   * @param id ID for the workflow definition
   * @param version the version of the workflow definition
   * @param name the name of the workflow definition
   * @param validationSchemaType the schema type for the workflow definition
   * @param validationSchema the XML (XSD) or JSON schema for the workflow definition
   * @param documentDefinitions the document definitions associated with the workflow definition
   */
  public WorkflowDefinition(
      String id,
      int version,
      String name,
      ValidationSchemaType validationSchemaType,
      String validationSchema,
      Set<WorkflowDefinitionDocumentDefinition> documentDefinitions) {
    this.id = id;
    this.version = version;
    this.name = name;
    this.validationSchemaType = validationSchemaType;
    this.validationSchema = validationSchema;

    for (WorkflowDefinitionDocumentDefinition documentDefinition : documentDefinitions) {
      documentDefinition.setWorkflowDefinition(this);
    }

    this.documentDefinitions.addAll(documentDefinitions);
  }

  /**
   * Constructs a new <b>WorkflowDefinition</b>.
   *
   * @param id ID for the workflow definition
   * @param version the version of the workflow definition
   * @param name the name of the workflow definition
   * @param documentDefinitions the document definitions associated with the workflow definition
   */
  public WorkflowDefinition(
      String id,
      int version,
      String name,
      Set<WorkflowDefinitionDocumentDefinition> documentDefinitions) {
    this.id = id;
    this.version = version;
    this.name = name;

    for (WorkflowDefinitionDocumentDefinition documentDefinition : documentDefinitions) {
      documentDefinition.setWorkflowDefinition(this);
    }

    this.documentDefinitions.addAll(documentDefinitions);
  }

  /**
   * Constructs a new <b>WorkflowDefinition</b>.
   *
   * @param id ID for the workflow definition
   * @param version the version of the workflow definition
   * @param tenantId the ID for the tenant the workflow definition is specific to
   * @param name the name of the workflow definition
   */
  public WorkflowDefinition(String id, int version, UUID tenantId, String name) {
    this.id = id;
    this.version = version;
    this.tenantId = tenantId;
    this.name = name;
  }

  /**
   * Constructs a new <b>WorkflowDefinition</b>.
   *
   * @param id ID for the workflow definition
   * @param version the version of the workflow definition
   * @param tenantId the ID for the tenant the workflow definition is specific to
   * @param name the name of the workflow definition
   * @param validationSchemaType the schema type for the workflow definition
   * @param validationSchema the XML (XSD) or JSON schema for the workflow definition
   */
  public WorkflowDefinition(
      String id,
      int version,
      UUID tenantId,
      String name,
      ValidationSchemaType validationSchemaType,
      String validationSchema) {
    this.id = id;
    this.version = version;
    this.tenantId = tenantId;
    this.name = name;
    this.validationSchemaType = validationSchemaType;
    this.validationSchema = validationSchema;
  }

  /**
   * Constructs a new <b>WorkflowDefinition</b>.
   *
   * @param id ID for the workflow definition
   * @param version the version of the workflow definition
   * @param tenantId the ID for the tenant the workflow definition is specific to
   * @param name the name of the workflow definition
   * @param validationSchemaType the schema type for the workflow definition
   * @param validationSchema the XML (XSD) or JSON schema for the workflow definition
   * @param documentDefinitions the document definitions associated with the workflow definition
   */
  public WorkflowDefinition(
      String id,
      int version,
      UUID tenantId,
      String name,
      ValidationSchemaType validationSchemaType,
      String validationSchema,
      Set<WorkflowDefinitionDocumentDefinition> documentDefinitions) {
    this.id = id;
    this.version = version;
    this.tenantId = tenantId;
    this.name = name;
    this.validationSchemaType = validationSchemaType;
    this.validationSchema = validationSchema;

    for (WorkflowDefinitionDocumentDefinition documentDefinition : documentDefinitions) {
      documentDefinition.setWorkflowDefinition(this);
    }

    this.documentDefinitions.addAll(documentDefinitions);
  }

  /**
   * Constructs a new <b>WorkflowDefinition</b>.
   *
   * @param id ID for the workflow definition
   * @param version the version of the workflow definition
   * @param tenantId the ID for the tenant the workflow definition is specific to
   * @param name the name of the workflow definition
   * @param documentDefinitions the document definitions associated with the workflow definition
   */
  public WorkflowDefinition(
      String id,
      int version,
      UUID tenantId,
      String name,
      Set<WorkflowDefinitionDocumentDefinition> documentDefinitions) {
    this.id = id;
    this.version = version;
    this.tenantId = tenantId;
    this.name = name;

    for (WorkflowDefinitionDocumentDefinition documentDefinition : documentDefinitions) {
      documentDefinition.setWorkflowDefinition(this);
    }

    this.documentDefinitions.addAll(documentDefinitions);
  }

  /**
   * Associate the document definition with the workflow definition.
   *
   * @param documentDefinitionId the ID for the document definition
   * @param required is a document with the document definition ID required for a workflow associated
   *     with the workflow definition
   */
  public void addDocumentDefinition(String documentDefinitionId, boolean required) {
    documentDefinitions.add(
        new WorkflowDefinitionDocumentDefinition(this, documentDefinitionId, required));
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

    WorkflowDefinition other = (WorkflowDefinition) object;

    return Objects.equals(id, other.id);
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
   * Returns the XML (XSD) or JSON schema for the workflow definition.
   *
   * @return the XML (XSD) or JSON schema for the workflow definition
   */
  public String getValidationSchema() {
    return validationSchema;
  }

  /**
   * Returns the schema type for the workflow definition.
   *
   * @return the schema type for the workflow definition
   */
  public ValidationSchemaType getValidationSchemaType() {
    return validationSchemaType;
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
    return ((id == null) ? 0 : id.hashCode());
  }

  /**
   * Set the document definitions associated with the workflow definition.
   *
   * @param documentDefinitions the document definitions associated with the workflow definition
   */
  public void setDocumentDefinitions(List<WorkflowDefinitionDocumentDefinition> documentDefinitions) {
    for (WorkflowDefinitionDocumentDefinition documentDefinition : documentDefinitions) {
      documentDefinition.setWorkflowDefinition(this);
    }
    this.documentDefinitions.clear();
    this.documentDefinitions.addAll(documentDefinitions);
  }

  /**
   * Set the ID for the workflow definition.
   *
   * @param id the ID for the workflow definition
   */
  public void setId(String id) {
    this.id = id;
  }

  /**
   * Set the name of the workflow definition.
   *
   * @param name the name of the workflow definition
   */
  public void setName(String name) {
    this.name = name;
  }

  /**
   * Set the ID for the tenant the workflow definition is specific to.
   *
   * @param tenantId the ID for the tenant the workflow definition is specific to
   */
  public void setTenantId(@NotNull UUID tenantId) {
    this.tenantId = tenantId;
  }

  /**
   * Set the XML (XSD) or JSON schema for the workflow definition.
   *
   * @param validationSchema the XML (XSD) or JSON schema for the workflow definition
   */
  public void setValidationSchema(String validationSchema) {
    this.validationSchema = validationSchema;
  }

  /**
   * Set the schema type for the workflow definition.
   *
   * @param validationSchemaType the schema type for the workflow definition
   */
  public void setValidationSchemaType(ValidationSchemaType validationSchemaType) {
    this.validationSchemaType = validationSchemaType;
  }

  /**
   * Set the version of the workflow definition.
   *
   * @param version the version of the workflow definition
   */
  public void setVersion(int version) {
    this.version = version;
  }
}
