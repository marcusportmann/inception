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
import digital.inception.core.util.StringUtil;
import digital.inception.core.validation.ValidationSchemaType;
import digital.inception.core.validation.constraint.ValidISO8601DurationOrPeriod;
import digital.inception.jpa.StringListAttributeConverter;
import digital.inception.operations.persistence.jpa.WorkflowFormTypeListAttributeConverter;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
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
  "description",
  "engineId",
  "timeToComplete",
  "attributes",
  "documentDefinitions",
  "stepDefinitions",
  "variableDefinitions",
  "permissions",
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
      "description",
      "engineId",
      "timeToComplete",
      "attributes",
      "documentDefinitions",
      "stepDefinitions",
      "variableDefinitions",
      "permissions",
      "validationSchemaType",
      "validationSchema"
    })
@XmlAccessorType(XmlAccessType.FIELD)
@Entity
@Table(name = "operations_workflow_definitions")
@IdClass(WorkflowDefinitionId.class)
public class WorkflowDefinition implements Serializable {

  @Serial private static final long serialVersionUID = 1000000;

  /** The workflow attribute definitions for the workflow definition. */
  @Schema(description = "The workflow attribute definitions for the workflow definition")
  @JsonProperty
  @JsonManagedReference("workflowDefinitionAttributeDefinitionReference")
  @XmlElementWrapper(name = "AttributeDefinitions")
  @XmlElement(name = "AttributeDefinition")
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
  private final List<WorkflowAttributeDefinition> attributeDefinitions = new ArrayList<>();

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

  /** The permissions for the workflow definition. */
  @Schema(description = "The permissions for the workflow definition")
  @JsonProperty
  @JsonManagedReference("workflowDefinitionPermissionReference")
  @XmlElementWrapper(name = "Permissions")
  @XmlElement(name = "Permission")
  @Valid
  @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
  @OrderBy("roleCode")
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
  private final List<WorkflowDefinitionPermission> permissions = new ArrayList<>();

  /** The workflow step definitions for the workflow definition. */
  @Schema(description = "The workflow step definitions for the workflow definition")
  @JsonProperty
  @JsonManagedReference("workflowDefinitionStepDefinitionReference")
  @XmlElementWrapper(name = "StepDefinitions")
  @XmlElement(name = "StepDefinition")
  @Valid
  @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
  @OrderBy("sequence")
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

  /** The workflow variable definitions for the workflow definition. */
  @Schema(description = "The workflow variable definitions for the workflow definition")
  @JsonProperty
  @JsonManagedReference("workflowDefinitionVariableDefinitionReference")
  @XmlElementWrapper(name = "VariableDefinitions")
  @XmlElement(name = "VariableDefinition")
  @Valid
  @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
  @OrderBy("name")
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
  private final List<WorkflowVariableDefinition> variableDefinitions = new ArrayList<>();

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

  /** The description for the workflow definition. */
  @Schema(description = "The description for the workflow definition")
  @JsonProperty
  @XmlElement(name = "Description")
  @Size(min = 1, max = 500)
  @Column(name = "description", length = 500)
  private String description;

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

  /** The codes for the required external reference types for the workflow definition. */
  @Schema(
      description =
          "The codes for the required external reference types for the workflow definition")
  @JsonProperty
  @XmlElementWrapper(name = "RequiredExternalReferenceTypes")
  @XmlElement(name = "RequiredExternalReferenceType")
  @Size(max = 10)
  @Convert(converter = StringListAttributeConverter.class)
  @Column(name = "required_external_reference_types", length = 510)
  private List<String> requiredExternalReferenceTypes;

  /** The supported workflow form types for the workflow definition. */
  @Schema(description = "The supported workflow form types for the workflow definition")
  @JsonProperty
  @XmlElementWrapper(name = "SupportedWorkflowFormTypes")
  @XmlElement(name = "SupportedWorkflowFormType")
  @Size(max = 10)
  @Convert(converter = WorkflowFormTypeListAttributeConverter.class)
  @Column(name = "supported_workflow_form_types", length = 510)
  private List<WorkflowFormType> supportedWorkflowFormTypes;

  /** The ID for the tenant the workflow definition is specific to. */
  @Schema(description = "The ID for the tenant the workflow definition is specific to")
  @JsonProperty
  @XmlElement(name = "TenantId")
  @Column(name = "tenant_id")
  private UUID tenantId;

  /**
   * The ISO-8601 duration format amount of time to complete a workflow associated with the workflow
   * definition.
   */
  @Schema(
      description =
          "The ISO-8601 duration format amount of time to complete a workflow associated with the workflow definition")
  @JsonProperty
  @XmlElement(name = "TimeToComplete")
  @ValidISO8601DurationOrPeriod
  @Column(name = "time_to_complete", length = 50)
  private String timeToComplete;

  /** The JSON or XML (XSD) validation schema for the workflow definition. */
  @Schema(description = "The JSON or XML (XSD) validation schema for the workflow definition")
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
   * @param tenantId the ID for the tenant the workflow definition is specific to
   * @param name the name of the workflow definition
   * @param description the description for the workflow definition
   * @param engineId the ID for the workflow engine the workflow definition is associated with
   * @param timeToComplete the ISO-8601 duration format amount of time to complete a workflow
   *     associated with the workflow definition
   * @param validationSchemaType the validation schema type for the workflow definition
   * @param validationSchema the JSON or XML (XSD) validation schema for the workflow definition
   * @param attributes the attributes for the workflow definition
   * @param attributeDefinitions the workflow attribute definitions for the workflow definition
   * @param documentDefinitions the document definitions associated with the workflow definition
   * @param stepDefinitions the workflow step definitions for the workflow definition
   * @param variableDefinitions the workflow variable definitions for the workflow definition
   * @param requiredExternalReferenceTypes the codes for the required external reference types for
   *     the workflow definition
   * @param supportedWorkflowFormTypes the supported workflow form types for the workflow definition
   * @param permissions the permissions for the workflow definition
   */
  public WorkflowDefinition(
      String id,
      int version,
      String categoryId,
      UUID tenantId,
      String name,
      String description,
      String engineId,
      String timeToComplete,
      ValidationSchemaType validationSchemaType,
      String validationSchema,
      List<WorkflowDefinitionAttribute> attributes,
      List<WorkflowAttributeDefinition> attributeDefinitions,
      List<WorkflowDefinitionDocumentDefinition> documentDefinitions,
      List<WorkflowStepDefinition> stepDefinitions,
      List<WorkflowVariableDefinition> variableDefinitions,
      List<String> requiredExternalReferenceTypes,
      List<WorkflowFormType> supportedWorkflowFormTypes,
      List<WorkflowDefinitionPermission> permissions) {
    this.id = id;
    this.version = version;
    this.categoryId = categoryId;
    this.tenantId = tenantId;
    this.name = name;
    this.description = description;
    this.engineId = engineId;
    this.timeToComplete = timeToComplete;

    this.validationSchemaType = validationSchemaType;
    this.validationSchema = validationSchema;

    if (attributes != null) {
      for (WorkflowDefinitionAttribute attribute : attributes) {
        attribute.setWorkflowDefinition(this);
      }

      this.attributes.addAll(attributes);
    }

    if (attributeDefinitions != null) {
      for (WorkflowAttributeDefinition attributeDefinition : attributeDefinitions) {
        attributeDefinition.setWorkflowDefinition(this);
      }

      this.attributeDefinitions.addAll(attributeDefinitions);
    }

    if (documentDefinitions != null) {
      for (WorkflowDefinitionDocumentDefinition documentDefinition : documentDefinitions) {
        documentDefinition.setWorkflowDefinition(this);
      }

      this.documentDefinitions.addAll(documentDefinitions);
    }

    if (stepDefinitions != null) {
      for (WorkflowStepDefinition stepDefinition : stepDefinitions) {
        stepDefinition.setWorkflowDefinition(this);
      }

      this.stepDefinitions.addAll(stepDefinitions);
    }

    if (variableDefinitions != null) {
      for (WorkflowVariableDefinition variableDefinition : variableDefinitions) {
        variableDefinition.setWorkflowDefinition(this);
      }

      this.variableDefinitions.addAll(variableDefinitions);
    }

    this.requiredExternalReferenceTypes = requiredExternalReferenceTypes;

    this.supportedWorkflowFormTypes = supportedWorkflowFormTypes;

    if (permissions != null) {
      for (WorkflowDefinitionPermission permission : permissions) {
        permission.setWorkflowDefinition(this);
      }

      this.permissions.addAll(permissions);
    }
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
   * Add the workflow attribute definition for the workflow definition.
   *
   * @param attributeDefinition the workflow attribute definition
   */
  public void addAttributeDefinition(WorkflowAttributeDefinition attributeDefinition) {
    attributeDefinitions.removeIf(
        existingAttributeDefinition ->
            StringUtil.equalsIgnoreCase(
                existingAttributeDefinition.getCode(), attributeDefinition.getCode()));

    attributeDefinition.setWorkflowDefinition(this);

    attributeDefinitions.add(attributeDefinition);
  }

  /**
   * Associate the document definition with the workflow definition.
   *
   * @param documentDefinitionId the ID for the document definition
   * @param required is a document with the document definition ID required for a workflow
   *     associated with the workflow definition
   * @param singular is a workflow associated with the workflow definition limited to a single
   *     document with the document definition ID
   * @param verifiable should a document with the document definition ID be manually or
   *     automatically verified after being provided for a workflow with the workflow definition ID
   *     and workflow definition version
   * @param internal is a document with the document definition ID internal-only and excluded for
   *     external users
   */
  public void addDocumentDefinition(
      String documentDefinitionId,
      boolean required,
      boolean singular,
      boolean verifiable,
      boolean internal) {
    documentDefinitions.removeIf(
        existingDocumentDefinition ->
            StringUtil.equalsIgnoreCase(
                existingDocumentDefinition.getDocumentDefinitionId(), documentDefinitionId));

    documentDefinitions.add(
        new WorkflowDefinitionDocumentDefinition(
            this, documentDefinitionId, required, singular, verifiable, internal));
  }

  /**
   * Associate the document definition with the workflow definition.
   *
   * @param documentDefinitionId the ID for the document definition
   * @param required is a document with the document definition ID required for a workflow
   *     associated with the workflow definition
   * @param singular is a workflow associated with the workflow definition limited to a single
   *     document with the document definition ID
   * @param verifiable should a document with the document definition ID be manually or
   *     automatically verified after being provided for a workflow with the workflow definition ID
   *     and workflow definition version
   * @param internal is a document with the document definition ID internal-only and excluded for
   *     external users
   * @param validityPeriod the ISO-8601 duration format validity period from a document's issue date
   *     during which the document, with the document definition ID, can be associated with a
   *     workflow with the workflow definition ID and workflow definition version
   */
  public void addDocumentDefinition(
      String documentDefinitionId,
      boolean required,
      boolean singular,
      boolean verifiable,
      boolean internal,
      String validityPeriod) {
    documentDefinitions.removeIf(
        existingDocumentDefinition ->
            StringUtil.equalsIgnoreCase(
                existingDocumentDefinition.getDocumentDefinitionId(), documentDefinitionId));

    documentDefinitions.add(
        new WorkflowDefinitionDocumentDefinition(
            this, documentDefinitionId, required, singular, verifiable, internal, validityPeriod));
  }

  /**
   * Add the permission for the workflow definition.
   *
   * @param permission the permission
   */
  public void addPermission(WorkflowDefinitionPermission permission) {
    permissions.removeIf(
        existingPermission ->
            (StringUtil.equalsIgnoreCase(existingPermission.getRoleCode(), permission.getRoleCode())
                && existingPermission.getType() == permission.getType()));

    permission.setWorkflowDefinition(this);

    permissions.add(permission);
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
   * Add the workflow variable definition for the workflow definition.
   *
   * @param variableDefinition the workflow variable definition
   */
  public void addVariableDefinition(WorkflowVariableDefinition variableDefinition) {
    variableDefinitions.removeIf(
        existingVariableDefinition ->
            StringUtil.equalsIgnoreCase(
                existingVariableDefinition.getName(), variableDefinition.getName()));

    variableDefinition.setWorkflowDefinition(this);

    variableDefinitions.add(variableDefinition);
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
  public Optional<WorkflowDefinitionAttribute> getAttribute(String code) {
    return attributes.stream()
        .filter(attribute -> StringUtil.equalsIgnoreCase(attribute.getCode(), code))
        .findFirst();
  }

  /**
   * Returns the workflow attribute definitions for the workflow definition.
   *
   * @return the workflow attribute definitions for the workflow definition
   */
  public List<WorkflowAttributeDefinition> getAttributeDefinitions() {
    return attributeDefinitions;
  }

  /**
   * Retrieve the value for the attribute with the specified code for the workflow definition.
   *
   * @param code the code for the attribute
   * @return an Optional containing the value for the attribute with the specified code for the
   *     workflow definition or an empty Optional if the attribute could not be found
   */
  public Optional<String> getAttributeValue(String code) {
    Optional<WorkflowDefinitionAttribute> attribute = getAttribute(code);

    return attribute.map(WorkflowDefinitionAttribute::getValue);
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
   * Returns the description for the workflow definition.
   *
   * @return the description for the workflow definition
   */
  public String getDescription() {
    return description;
  }

  /**
   * Retrieve the document definition with the specified ID for the workflow definition.
   *
   * @param documentDefinitionId the ID for the document definition
   * @return an Optional containing the document definition with the specified ID for the workflow
   *     definition or an empty Optional if the document definition could not be found
   */
  public Optional<WorkflowDefinitionDocumentDefinition> getDocumentDefinition(
      String documentDefinitionId) {
    return documentDefinitions.stream()
        .filter(
            workflowDefinitionDocumentDefinition ->
                StringUtil.equalsIgnoreCase(
                    workflowDefinitionDocumentDefinition.getDocumentDefinitionId(),
                    documentDefinitionId))
        .findFirst();
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
   * Returns the permissions for the workflow definition.
   *
   * @return the permissions for the workflow definition
   */
  public List<WorkflowDefinitionPermission> getPermissions() {
    return permissions;
  }

  /**
   * Returns the codes for the required external reference types for the workflow definition.
   *
   * @return the codes for the required external reference types for the workflow definition
   */
  public List<String> getRequiredExternalReferenceTypes() {
    return requiredExternalReferenceTypes;
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
   * Returns the supported workflow form types for the workflow definition.
   *
   * @return the supported workflow form types for the workflow definition
   */
  public List<WorkflowFormType> getSupportedWorkflowFormTypes() {
    return supportedWorkflowFormTypes;
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
   * Returns the ISO-8601 duration format amount of time to complete a workflow associated with the
   * workflow definition.
   *
   * @return the ISO-8601 duration format amount of time to complete a workflow associated with the
   *     workflow definition
   */
  public String getTimeToComplete() {
    return timeToComplete;
  }

  /**
   * Returns the JSON or XML (XSD) validation schema for the workflow definition.
   *
   * @return the JSON or XML (XSD) validation schema for the workflow definition
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
   * Returns the workflow variable definitions for the workflow definition.
   *
   * @return the workflow variable definitions for the workflow definition
   */
  public List<WorkflowVariableDefinition> getVariableDefinitions() {
    return variableDefinitions;
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
  public void removeAttribute(String code) {
    attributes.removeIf(
        existingAttribute -> StringUtil.equalsIgnoreCase(existingAttribute.getCode(), code));
  }

  /**
   * Remove the workflow attribute definition with the specified code for the workflow definition.
   *
   * @param code the code for the workflow attribute definition
   */
  public void removeAttributeDefinition(String code) {
    attributeDefinitions.removeIf(
        existingAttributeDefinition ->
            StringUtil.equalsIgnoreCase(existingAttributeDefinition.getCode(), code));
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
   * Remove the permission with the specified role code and workflow permission type for the
   * workflow definition.
   *
   * @param roleCode the role code for the permission
   * @param type the workflow permission type
   */
  public void removePermission(String roleCode, WorkflowPermissionType type) {
    permissions.removeIf(
        existingPermission ->
            (StringUtil.equalsIgnoreCase(existingPermission.getRoleCode(), roleCode)
                && (existingPermission.getType() == type)));
  }

  /**
   * Remove the workflow step definition with the specified code for the workflow definition.
   *
   * @param code the code for the workflow step definition
   */
  public void removeStepDefinition(String code) {
    stepDefinitions.removeIf(
        existingStepDefinition ->
            StringUtil.equalsIgnoreCase(existingStepDefinition.getCode(), code));
  }

  /**
   * Remove the workflow variable definition with the specified name for the workflow definition.
   *
   * @param name the name of the workflow variable
   */
  public void removeVariableDefinition(String name) {
    variableDefinitions.removeIf(
        existingVariableDefinition ->
            StringUtil.equalsIgnoreCase(existingVariableDefinition.getName(), name));
  }

  /**
   * Set the workflow attribute definitions for the workflow definition.
   *
   * @param attributeDefinitions the workflow attribute definitions for the workflow definition
   */
  public void setAttributeDefinitions(List<WorkflowAttributeDefinition> attributeDefinitions) {
    attributeDefinitions.forEach(
        attributeDefinition -> attributeDefinition.setWorkflowDefinition(this));
    this.attributeDefinitions.clear();
    this.attributeDefinitions.addAll(attributeDefinitions);
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
   * Set the description for the workflow definition.
   *
   * @param description the description for the workflow definition
   */
  public void setDescription(String description) {
    this.description = description;
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
   * Set the permissions for the workflow definition.
   *
   * @param permissions the permissions for the workflow definition
   */
  public void setPermissions(List<WorkflowDefinitionPermission> permissions) {
    permissions.forEach(permission -> permission.setWorkflowDefinition(this));
    this.permissions.clear();
    this.permissions.addAll(permissions);
  }

  /**
   * Set the codes for the required external reference types for the workflow definition.
   *
   * @param requiredExternalReferenceTypes the codes for the required external reference types for
   *     the workflow definition
   */
  public void setRequiredExternalReferenceTypes(List<String> requiredExternalReferenceTypes) {
    this.requiredExternalReferenceTypes = requiredExternalReferenceTypes;
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
   * Set the supported workflow form types for the workflow definition.
   *
   * @param supportedWorkflowFormTypes the supported workflow form types for the workflow definition
   */
  public void setSupportedWorkflowFormTypes(List<WorkflowFormType> supportedWorkflowFormTypes) {
    this.supportedWorkflowFormTypes = supportedWorkflowFormTypes;
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
   * Set the ISO-8601 duration format amount of time to complete a workflow associated with the
   * workflow definition.
   *
   * @param timeToComplete the ISO-8601 duration format amount of time to complete a workflow
   *     associated with the workflow definition
   */
  public void setTimeToComplete(String timeToComplete) {
    this.timeToComplete = timeToComplete;
  }

  /**
   * Set the JSON or XML (XSD) schema for the workflow definition.
   *
   * @param validationSchema the JSON or XML (XSD) validation schema for the workflow definition
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
   * Set the workflow variable definitions for the workflow definition.
   *
   * @param variableDefinitions the workflow variable definitions for the workflow definition
   */
  public void setVariableDefinitions(List<WorkflowVariableDefinition> variableDefinitions) {
    variableDefinitions.forEach(
        variableDefinition -> variableDefinition.setWorkflowDefinition(this));
    this.variableDefinitions.clear();
    this.variableDefinitions.addAll(variableDefinitions);
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

    // Update the version on the attribute definitions
    attributeDefinitions.forEach(attribute -> attribute.setWorkflowDefinition(this));

    // Update the version on the workflow definition document definitions
    documentDefinitions.forEach(
        workflowDefinitionDocumentDefinition ->
            workflowDefinitionDocumentDefinition.setWorkflowDefinition(this));

    // Update the version on the step definitions
    permissions.forEach(permission -> permission.setWorkflowDefinition(this));

    // Update the version on the step definitions
    stepDefinitions.forEach(
        workflowStepDefinition -> workflowStepDefinition.setWorkflowDefinition(this));

    // Update the version on the variable definitions
    variableDefinitions.forEach(
        workflowVariableDefinition -> workflowVariableDefinition.setWorkflowDefinition(this));
  }

  /**
   * Returns whether the workflow definition supports the workflow form type
   *
   * @param workflowFormType the workflow form type
   * @return {@code true} if the workflow definition supports the workflow form type or {@code
   *     false} otherwise
   */
  public boolean supportsWorkflowFormType(WorkflowFormType workflowFormType) {
    return (supportedWorkflowFormTypes != null)
        && (supportedWorkflowFormTypes.contains(workflowFormType));
  }
}
