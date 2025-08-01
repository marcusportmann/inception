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
import digital.inception.core.time.TimeUnit;
import digital.inception.core.util.StringUtil;
import digital.inception.core.validation.ValidationSchemaType;
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
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import org.springframework.util.StringUtils;

/**
 * The {@code WorkflowDefinition} class holds the information for a workflow definition.
 *
 * @author Marcus Portmann
 */
@Schema(description = "A workflow definition")
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
  "id",
  "version",
  "categoryId",
  "tenantId",
  "name",
  "engineId",
  "attributes",
  "documentDefinitions",
  "stepDefinitions",
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
      "categoryId",
      "tenantId",
      "name",
      "engineId",
      "attributes",
      "documentDefinitions",
      "stepDefinitions",
      "validationSchemaType",
      "validationSchema"
    })
@XmlAccessorType(XmlAccessType.FIELD)
@Entity
@Table(name = "operations_workflow_definitions")
@IdClass(WorkflowDefinitionId.class)
public class WorkflowDefinition implements Serializable {

  @Serial private static final long serialVersionUID = 1000000;

  /** The attributes for the workflow definition. */
  @Schema(description = "The attributes for the workflow definition")
  @JsonProperty
  @JsonManagedReference("workflowDefinitionAttributeReference")
  @XmlElementWrapper(name = "Attributes")
  @XmlElement(name = "Attribute")
  @Valid
  @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
  @OrderBy("code")
  @JoinColumns({
    @JoinColumn(
        name = "definition_id",
        referencedColumnName = "id",
        insertable = false,
        updatable = false),
    @JoinColumn(
        name = "definition_version",
        referencedColumnName = "version",
        insertable = false,
        updatable = false)
  })
  private final List<WorkflowDefinitionAttribute> attributes = new ArrayList<>();

  /** The document definitions associated with the workflow definition. */
  @Schema(
      description = "The document definitions associated with the workflow definition",
      requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @JsonManagedReference("workflowDefinitionDocumentDefinitionReference")
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

  /** The workflow step definitions for the workflow definition. */
  @Schema(description = "The workflow step definitions for the workflow definition")
  @JsonProperty
  @JsonManagedReference("workflowStepDefinitionReference")
  @XmlElementWrapper(name = "StepDefinitions")
  @XmlElement(name = "StepDefinition")
  @Valid
  @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
  @OrderBy("code")
  @JoinColumns({
    @JoinColumn(
        name = "definition_id",
        referencedColumnName = "id",
        insertable = false,
        updatable = false),
    @JoinColumn(
        name = "definition_version",
        referencedColumnName = "version",
        insertable = false,
        updatable = false)
  })
  private final List<WorkflowStepDefinition> stepDefinitions = new ArrayList<>();

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

  /** Constructs a new {@code WorkflowDefinition}. */
  public WorkflowDefinition() {}

  /**
   * Constructs a new {@code WorkflowDefinition}.
   *
   * @param id the ID for the workflow definition
   * @param version the version of the workflow definition
   * @param categoryId the ID for the workflow definition category the workflow definition is
   *     associated with
   * @param name the name of the workflow definition
   * @param engineId the ID for the workflow engine the workflow definition is associated with
   */
  public WorkflowDefinition(
      String id, int version, String categoryId, String name, String engineId) {
    this.id = id;
    this.version = version;
    this.categoryId = categoryId;
    this.name = name;
    this.engineId = engineId;
  }

  /**
   * Constructs a new {@code WorkflowDefinition}.
   *
   * @param id the ID for the workflow definition
   * @param version the version of the workflow definition
   * @param categoryId the ID for the workflow definition category the workflow definition is
   *     associated with
   * @param name the name of the workflow definition
   * @param engineId the ID for the workflow engine the workflow definition is associated with
   * @param validationSchemaType the validation schema type for the workflow definition
   * @param validationSchema the XML (XSD) or JSON validation schema for the workflow definition
   */
  public WorkflowDefinition(
      String id,
      int version,
      String categoryId,
      String name,
      String engineId,
      ValidationSchemaType validationSchemaType,
      String validationSchema) {
    this.id = id;
    this.version = version;
    this.categoryId = categoryId;
    this.name = name;
    this.engineId = engineId;
    this.validationSchemaType = validationSchemaType;
    this.validationSchema = validationSchema;
  }

  /**
   * Constructs a new {@code WorkflowDefinition}.
   *
   * @param id the ID for the workflow definition
   * @param version the version of the workflow definition
   * @param categoryId the ID for the workflow definition category the workflow definition is
   *     associated with
   * @param name the name of the workflow definition
   * @param engineId the ID for the workflow engine the workflow definition is associated with
   * @param validationSchemaType the validation schema type for the workflow definition
   * @param validationSchema the XML (XSD) or JSON validation schema for the workflow definition
   * @param documentDefinitions the document definitions associated with the workflow definition
   */
  public WorkflowDefinition(
      String id,
      int version,
      String categoryId,
      String name,
      String engineId,
      ValidationSchemaType validationSchemaType,
      String validationSchema,
      Set<WorkflowDefinitionDocumentDefinition> documentDefinitions) {
    this.id = id;
    this.version = version;
    this.categoryId = categoryId;
    this.name = name;
    this.engineId = engineId;
    this.validationSchemaType = validationSchemaType;
    this.validationSchema = validationSchema;

    for (WorkflowDefinitionDocumentDefinition documentDefinition : documentDefinitions) {
      documentDefinition.setWorkflowDefinition(this);
    }

    this.documentDefinitions.addAll(documentDefinitions);
  }

  /**
   * Constructs a new {@code WorkflowDefinition}.
   *
   * @param id the ID for the workflow definition
   * @param version the version of the workflow definition
   * @param categoryId the ID for the workflow definition category the workflow definition is
   *     associated with
   * @param name the name of the workflow definition
   * @param engineId the ID for the workflow engine the workflow definition is associated with
   * @param documentDefinitions the document definitions associated with the workflow definition
   */
  public WorkflowDefinition(
      String id,
      int version,
      String categoryId,
      String name,
      String engineId,
      Set<WorkflowDefinitionDocumentDefinition> documentDefinitions) {
    this.id = id;
    this.version = version;
    this.categoryId = categoryId;
    this.name = name;
    this.engineId = engineId;

    for (WorkflowDefinitionDocumentDefinition documentDefinition : documentDefinitions) {
      documentDefinition.setWorkflowDefinition(this);
    }

    this.documentDefinitions.addAll(documentDefinitions);
  }

  /**
   * Constructs a new {@code WorkflowDefinition}.
   *
   * @param id the ID for the workflow definition
   * @param version the version of the workflow definition
   * @param categoryId the ID for the workflow definition category the workflow definition is
   *     associated with
   * @param tenantId the ID for the tenant the workflow definition is specific to
   * @param name the name of the workflow definition
   * @param engineId the ID for the workflow engine the workflow definition is associated with
   */
  public WorkflowDefinition(
      String id, int version, String categoryId, UUID tenantId, String name, String engineId) {
    this.id = id;
    this.version = version;
    this.categoryId = categoryId;
    this.tenantId = tenantId;
    this.name = name;
    this.engineId = engineId;
  }

  /**
   * Constructs a new {@code WorkflowDefinition}.
   *
   * @param id the ID for the workflow definition
   * @param version the version of the workflow definition
   * @param categoryId the ID for the workflow definition category the workflow definition is
   *     associated with
   * @param tenantId the ID for the tenant the workflow definition is specific to
   * @param name the name of the workflow definition
   * @param engineId the ID for the workflow engine the workflow definition is associated with
   * @param validationSchemaType the validation schema type for the workflow definition
   * @param validationSchema the XML (XSD) or JSON validation schema for the workflow definition
   */
  public WorkflowDefinition(
      String id,
      int version,
      String categoryId,
      UUID tenantId,
      String name,
      String engineId,
      ValidationSchemaType validationSchemaType,
      String validationSchema) {
    this.id = id;
    this.version = version;
    this.categoryId = categoryId;
    this.tenantId = tenantId;
    this.name = name;
    this.engineId = engineId;
    this.validationSchemaType = validationSchemaType;
    this.validationSchema = validationSchema;
  }

  /**
   * Constructs a new {@code WorkflowDefinition}.
   *
   * @param id the ID for the workflow definition
   * @param version the version of the workflow definition
   * @param categoryId the ID for the workflow definition category the workflow definition is
   *     associated with
   * @param tenantId the ID for the tenant the workflow definition is specific to
   * @param name the name of the workflow definition
   * @param engineId the ID for the workflow engine the workflow definition is associated with
   * @param validationSchemaType the validation schema type for the workflow definition
   * @param validationSchema the XML (XSD) or JSON validation schema for the workflow definition
   * @param documentDefinitions the document definitions associated with the workflow definition
   */
  public WorkflowDefinition(
      String id,
      int version,
      String categoryId,
      UUID tenantId,
      String name,
      String engineId,
      ValidationSchemaType validationSchemaType,
      String validationSchema,
      Set<WorkflowDefinitionDocumentDefinition> documentDefinitions) {
    this.id = id;
    this.version = version;
    this.categoryId = categoryId;
    this.tenantId = tenantId;
    this.name = name;
    this.engineId = engineId;
    this.validationSchemaType = validationSchemaType;
    this.validationSchema = validationSchema;

    for (WorkflowDefinitionDocumentDefinition documentDefinition : documentDefinitions) {
      documentDefinition.setWorkflowDefinition(this);
    }

    this.documentDefinitions.addAll(documentDefinitions);
  }

  /**
   * Constructs a new {@code WorkflowDefinition}.
   *
   * @param id the ID for the workflow definition
   * @param version the version of the workflow definition
   * @param categoryId the ID for the workflow definition category the workflow definition is
   *     associated with
   * @param tenantId the ID for the tenant the workflow definition is specific to
   * @param name the name of the workflow definition
   * @param engineId the ID for the workflow engine the workflow definition is associated with
   * @param documentDefinitions the document definitions associated with the workflow definition
   */
  public WorkflowDefinition(
      String id,
      int version,
      String categoryId,
      UUID tenantId,
      String name,
      String engineId,
      Set<WorkflowDefinitionDocumentDefinition> documentDefinitions) {
    this.id = id;
    this.version = version;
    this.categoryId = categoryId;
    this.tenantId = tenantId;
    this.name = name;
    this.engineId = engineId;

    for (WorkflowDefinitionDocumentDefinition documentDefinition : documentDefinitions) {
      documentDefinition.setWorkflowDefinition(this);
    }

    this.documentDefinitions.addAll(documentDefinitions);
  }

  /**
   * Add the attribute for the workflow definition.
   *
   * @param attribute the attribute
   */
  public void addAttribute(WorkflowDefinitionAttribute attribute) {
    attributes.removeIf(
        existingAttribute ->
            StringUtil.equalsIgnoreCase(existingAttribute.getCode(), attribute.getCode()));

    attribute.setWorkflowDefinition(this);

    attributes.add(attribute);
  }

  /**
   * Associate the document definition with the workflow definition.
   *
   * @param documentDefinitionId the ID for the document definition
   * @param required is a document with the document definition ID required for a workflow
   *     associated with the workflow definition
   * @param singular is a workflow associated with the workflow definition limited to a single
   *     document with the document definition ID
   */
  public void addDocumentDefinition(
      String documentDefinitionId, boolean required, boolean singular) {
    documentDefinitions.removeIf(
        existingDocumentDefinition ->
            StringUtil.equalsIgnoreCase(
                existingDocumentDefinition.getDocumentDefinitionId(), documentDefinitionId));

    documentDefinitions.add(
        new WorkflowDefinitionDocumentDefinition(this, documentDefinitionId, required, singular));
  }

  /**
   * Associate the document definition with the workflow definition.
   *
   * @param documentDefinitionId the ID for the document definition
   * @param required is a document with the document definition ID required for a workflow
   *     associated with the workflow definition
   * @param singular is a workflow associated with the workflow definition limited to a single
   *     document with the document definition ID
   * @param validityPeriodUnit the unit of measurement of time for the validity period from a
   *     document's issue date during which the document, with the document definition ID, can be
   *     associated with a workflow associated with the workflow definition
   * @param validityPeriodAmount the validity period from a document's issue date during which the
   *     document, with the document definition ID, can be associated with a workflow associated
   *     with the workflow definition
   */
  public void addDocumentDefinition(
      String documentDefinitionId,
      boolean required,
      boolean singular,
      TimeUnit validityPeriodUnit,
      Integer validityPeriodAmount) {
    documentDefinitions.removeIf(
        existingDocumentDefinition ->
            StringUtil.equalsIgnoreCase(
                existingDocumentDefinition.getDocumentDefinitionId(), documentDefinitionId));

    documentDefinitions.add(
        new WorkflowDefinitionDocumentDefinition(
            this,
            documentDefinitionId,
            required,
            singular,
            validityPeriodUnit,
            validityPeriodAmount));
  }

  /**
   * Add the workflow step definition for the workflow definition.
   *
   * @param stepDefinition the workflow step definition
   */
  public void addStepDefinition(WorkflowStepDefinition stepDefinition) {
    stepDefinitions.removeIf(
        existingStepDefinition ->
            StringUtil.equalsIgnoreCase(
                existingStepDefinition.getCode(), stepDefinition.getCode()));

    stepDefinition.setWorkflowDefinition(this);

    stepDefinitions.add(stepDefinition);
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

    WorkflowDefinition other = (WorkflowDefinition) object;

    return StringUtil.equalsIgnoreCase(id, other.id) && (version == other.version);
  }

  /**
   * Retrieve the attribute with the specified code for the workflow definition.
   *
   * @param code the code for the attribute
   * @return an Optional containing the attribute with the specified code for the workflow
   *     definition or an empty Optional if the attribute could not be found
   */
  public Optional<WorkflowDefinitionAttribute> getAttributeWithCode(String code) {
    return attributes.stream()
        .filter(attribute -> StringUtil.equalsIgnoreCase(attribute.getCode(), code))
        .findFirst();
  }

  /**
   * Returns the attributes for the workflow definition.
   *
   * @return the attributes for the workflow definition
   */
  public List<WorkflowDefinitionAttribute> getAttributes() {
    return attributes;
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
   * Returns the workflow step definitions for the workflow definition.
   *
   * @return the workflow step definitions for the workflow definition
   */
  public List<WorkflowStepDefinition> getStepDefinitions() {
    return stepDefinitions;
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
   * Returns the XML (XSD) or JSON validation schema for the workflow definition.
   *
   * @return the XML (XSD) or JSON validation schema for the workflow definition
   */
  public String getValidationSchema() {
    return validationSchema;
  }

  /**
   * Returns the validation schema type for the workflow definition.
   *
   * @return the validation schema type for the workflow definition
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
    return ((id == null) ? 0 : id.hashCode()) + version;
  }

  /**
   * Remove the attribute with the specified code for the workflow definition.
   *
   * @param code the code for the attribute
   */
  public void removeAttributeWithCode(String code) {
    attributes.removeIf(
        existingAttribute -> StringUtil.equalsIgnoreCase(existingAttribute.getCode(), code));
  }

  /**
   * Disassociate the document definition from the workflow definition.
   *
   * @param documentDefinitionId the ID for the document definition
   */
  public void removeDocumentDefinition(String documentDefinitionId) {
    if (!StringUtils.hasText(documentDefinitionId)) {
      return;
    }

    Iterator<WorkflowDefinitionDocumentDefinition> iterator = documentDefinitions.iterator();

    while (iterator.hasNext()) {
      WorkflowDefinitionDocumentDefinition workflowDefinitionDocumentDefinition = iterator.next();

      if (StringUtil.equalsIgnoreCase(
          workflowDefinitionDocumentDefinition.getDocumentDefinitionId(), documentDefinitionId)) {
        // break the parent link so orphanRemoval can cascade the delete
        workflowDefinitionDocumentDefinition.setWorkflowDefinition(null);
        iterator.remove();
      }
    }
  }

  /**
   * Remove the workflow step definition with the specified code for the workflow definition.
   *
   * @param code the code for the workflow step definition
   */
  public void removeStepDefinitionWithCode(String code) {
    stepDefinitions.removeIf(
        existingStepDefinition ->
            StringUtil.equalsIgnoreCase(existingStepDefinition.getCode(), code));
  }

  /**
   * Set the attributes for the workflow definition.
   *
   * @param attributes the attributes for the workflow definition
   */
  public void setAttributes(List<WorkflowDefinitionAttribute> attributes) {
    attributes.forEach(attribute -> attribute.setWorkflowDefinition(this));
    this.attributes.clear();
    this.attributes.addAll(attributes);
  }

  /**
   * Set the ID for the workflow definition category the workflow definition is associated with.
   *
   * @param categoryId the ID for the workflow definition category the workflow definition is
   *     associated with
   */
  public void setCategoryId(String categoryId) {
    this.categoryId = categoryId;
  }

  /**
   * Set the document definitions associated with the workflow definition.
   *
   * @param documentDefinitions the document definitions associated with the workflow definition
   */
  public void setDocumentDefinitions(
      List<WorkflowDefinitionDocumentDefinition> documentDefinitions) {
    for (WorkflowDefinitionDocumentDefinition documentDefinition : documentDefinitions) {
      documentDefinition.setWorkflowDefinition(this);
    }
    this.documentDefinitions.clear();
    this.documentDefinitions.addAll(documentDefinitions);
  }

  /**
   * Set the ID for the workflow engine the workflow definition is associated with.
   *
   * @param engineId the ID for the workflow engine the workflow definition is associated with
   */
  public void setEngineId(String engineId) {
    this.engineId = engineId;
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
   * Set the workflow step definitions for the workflow definition.
   *
   * @param stepDefinitions the workflow step definitions for the workflow definition
   */
  public void setStepDefinitions(List<WorkflowStepDefinition> stepDefinitions) {
    stepDefinitions.forEach(stepDefinition -> stepDefinition.setWorkflowDefinition(this));
    this.stepDefinitions.clear();
    this.stepDefinitions.addAll(stepDefinitions);
  }

  /**
   * Set the ID for the tenant the workflow definition is specific to.
   *
   * @param tenantId the ID for the tenant the workflow definition is specific to
   */
  public void setTenantId(UUID tenantId) {
    this.tenantId = tenantId;
  }

  /**
   * Set the XML (XSD) or JSON schema for the workflow definition.
   *
   * @param validationSchema the XML (XSD) or JSON validation schema for the workflow definition
   */
  public void setValidationSchema(String validationSchema) {
    this.validationSchema = validationSchema;
  }

  /**
   * Set the validation schema type for the workflow definition.
   *
   * @param validationSchemaType the validation schema type for the workflow definition
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

    // Update the version on the attributes
    attributes.forEach(attribute -> attribute.setWorkflowDefinition(this));

    // Update the version on the workflow definition document definitions
    documentDefinitions.forEach(
        workflowDefinitionDocumentDefinition ->
            workflowDefinitionDocumentDefinition.setWorkflowDefinition(this));

    // Update the version on the step definitions
    stepDefinitions.forEach(
        workflowStepDefinition -> workflowStepDefinition.setWorkflowDefinition(this));
  }
}
