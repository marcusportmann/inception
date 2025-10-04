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

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import digital.inception.core.util.StringUtil;
import digital.inception.core.validation.constraint.ValidISO8601DurationOrPeriod;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.xml.bind.Unmarshaller;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlTransient;
import jakarta.xml.bind.annotation.XmlType;
import java.io.Serial;
import java.io.Serializable;
import java.time.Duration;
import org.springframework.boot.convert.DurationStyle;

/**
 * The {@code WorkflowDefinitionDocumentDefinition} class holds the information for an association
 * of a document definition with a workflow definition.
 *
 * @author Marcus Portmann
 */
@Schema(description = "An association of a document definition with a workflow definition")
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
  "documentDefinitionId",
  "required",
  "singular",
  "verifiable",
  "internal",
  "validityPeriod"
})
@XmlRootElement(
    name = "WorkflowDefinitionDocumentDefinition",
    namespace = "https://inception.digital/operations")
@XmlType(
    name = "WorkflowDefinitionDocumentDefinition",
    namespace = "https://inception.digital/operations",
    propOrder = {
      "documentDefinitionId",
      "required",
      "singular",
      "verifiable",
      "internal",
      "validityPeriod"
    })
@XmlAccessorType(XmlAccessType.FIELD)
@Entity
@Table(name = "operations_workflow_definition_document_definitions")
@IdClass(WorkflowDefinitionDocumentDefinitionId.class)
public class WorkflowDefinitionDocumentDefinition implements Serializable {

  @Serial private static final long serialVersionUID = 1000000;

  /** The ID for the document definition. */
  @Schema(
      description = "The ID for the document definition",
      requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElement(name = "DocumentDefinitionId", required = true)
  @NotNull
  @Size(min = 1, max = 50)
  @Id
  @Column(name = "document_definition_id", length = 50, nullable = false)
  private String documentDefinitionId;

  /**
   * Is a document with the document definition ID internal-only and excluded for external users.
   */
  @Schema(
      description =
          "Is a document with the document definition ID internal-only and excluded for external users",
      requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElement(name = "Internal", required = true)
  @NotNull
  @Column(name = "internal", nullable = false)
  private Boolean internal;

  /**
   * Is a document with the document definition ID required for a workflow with the workflow
   * definition ID and workflow definition version.
   */
  @Schema(
      description =
          "Is a document with the document definition ID required for a workflow with the workflow definition ID and workflow definition version",
      requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElement(name = "Required", required = true)
  @NotNull
  @Column(name = "required", nullable = false)
  private Boolean required;

  /**
   * Is a workflow with the workflow definition ID and workflow definition version limited to a
   * single document with the document definition ID.
   */
  @Schema(
      description =
          "Is a workflow with the workflow definition ID and workflow definition version limited to a single document with the document definition ID",
      requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElement(name = "Singular", required = true)
  @NotNull
  @Column(name = "singular", nullable = false)
  private Boolean singular;

  /**
   * The ISO-8601 duration format validity period from a document's issue date during which the
   * document, with the document definition ID, can be associated with a workflow with the workflow
   * definition ID and workflow definition version.
   */
  @Schema(
      description =
          "The ISO-8601 duration format validity period from a document's issue date during which the document, with the document definition ID, can be associated with a workflow with the workflow definition ID and workflow definition version")
  @JsonProperty
  @XmlElement(name = "ValidityPeriod")
  @ValidISO8601DurationOrPeriod
  @Column(name = "validity_period", length = 50)
  private String validityPeriod;

  /**
   * Should a document with the document definition ID be manually or automatically verified after
   * being provided for a workflow with the workflow definition ID and workflow definition version.
   */
  @Schema(
      description =
          "Should a document with the document definition ID be manually or automatically verified after being provided for a workflow with the workflow definition ID and workflow definition version",
      requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElement(name = "Verifiable", required = true)
  @NotNull
  @Column(name = "verifiable", nullable = false)
  private Boolean verifiable;

  /**
   * The ID for the workflow definition the workflow definition document definition is associated
   * with.
   */
  @Schema(hidden = true)
  @JsonIgnore
  @XmlTransient
  @Id
  @Column(name = "workflow_definition_id", nullable = false)
  private String workflowDefinitionId;

  /**
   * The version of the workflow definition the workflow definition document definition is
   * associated with.
   */
  @Schema(hidden = true)
  @JsonIgnore
  @XmlTransient
  @Id
  @Column(name = "workflow_definition_version", nullable = false)
  private int workflowDefinitionVersion;

  /** Constructs a new {@code WorkflowDefinitionDocumentDefinition}. */
  public WorkflowDefinitionDocumentDefinition() {}

  /**
   * Constructs a new {@code WorkflowDefinitionDocumentDefinition}.
   *
   * @param workflowDefinition the workflow definition the workflow definition document definition
   *     is associated with
   * @param documentDefinitionId ID for the document definition
   * @param required is a document with the document definition ID required for a workflow with the
   *     workflow definition ID and workflow definition version
   * @param singular is a workflow with the workflow definition ID and workflow definition version
   *     limited to a single document with the document definition ID
   * @param verifiable should a document with the document definition ID be manually or
   *     automatically verified after being provided for a workflow with the workflow definition ID
   *     and workflow definition version
   * @param internal is a document with the document definition ID internal-only and excluded for
   *     external users
   */
  public WorkflowDefinitionDocumentDefinition(
      WorkflowDefinition workflowDefinition,
      String documentDefinitionId,
      boolean required,
      boolean singular,
      boolean verifiable,
      boolean internal) {
    this.workflowDefinitionId = workflowDefinition.getId();
    this.workflowDefinitionVersion = workflowDefinition.getVersion();
    this.documentDefinitionId = documentDefinitionId;
    this.required = required;
    this.singular = singular;
    this.verifiable = verifiable;
    this.internal = internal;
  }

  /**
   * Constructs a new {@code WorkflowDefinitionDocumentDefinition}.
   *
   * @param workflowDefinition the workflow definition the workflow definition document definition
   *     is associated with
   * @param documentDefinitionId ID for the document definition
   * @param required is a document with the document definition ID required for a workflow with the
   *     workflow definition ID and workflow definition version
   * @param singular is a workflow with the workflow definition ID and workflow definition version
   *     limited to a single document with the document definition ID
   * @param verifiable should a document with the document definition ID be manually or
   *     automatically verified after being provided for a workflow with the workflow definition ID
   *     and workflow definition version
   * @param internal is a document with the document definition ID internal-only and excluded for
   *     external users
   * @param validityPeriod the ISO-8601 duration format validity period from a document's issue date
   *     during which the document, with the document definition ID, can be associated with a
   *     workflow with the workflow definition ID and workflow definition version
   */
  public WorkflowDefinitionDocumentDefinition(
      WorkflowDefinition workflowDefinition,
      String documentDefinitionId,
      boolean required,
      boolean singular,
      boolean verifiable,
      boolean internal,
      String validityPeriod) {
    this.workflowDefinitionId = workflowDefinition.getId();
    this.workflowDefinitionVersion = workflowDefinition.getVersion();
    this.documentDefinitionId = documentDefinitionId;
    this.required = required;
    this.singular = singular;
    this.verifiable = verifiable;
    this.internal = internal;
    this.validityPeriod = validityPeriod;
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

    WorkflowDefinitionDocumentDefinition other = (WorkflowDefinitionDocumentDefinition) object;

    return StringUtil.equalsIgnoreCase(workflowDefinitionId, other.workflowDefinitionId)
        && workflowDefinitionVersion == other.workflowDefinitionVersion
        && StringUtil.equalsIgnoreCase(documentDefinitionId, other.documentDefinitionId);
  }

  /**
   * Returns the ID for the document definition.
   *
   * @return the ID for the document definition
   */
  public String getDocumentDefinitionId() {
    return documentDefinitionId;
  }

  /**
   * Returns the ISO-8601 duration format validity period from a document's issue date during which
   * the document, with the document definition ID, can be associated with a workflow with the
   * workflow definition ID and workflow definition version.
   *
   * @return the ISO-8601 duration format validity period from a document's issue date during which
   *     the document, with the document definition ID, can be associated with a workflow with the
   *     workflow definition ID and workflow definition version
   */
  public String getValidityPeriod() {
    return validityPeriod;
  }

  /**
   * Returns the {@code Duration} for the validity period from a document's issue date during which
   * the document, with the document definition ID, can be associated with a workflow with the
   * workflow definition ID and workflow definition version.
   *
   * @return the {@code Duration} for the validity period from a document's issue date during which
   *     the document, with the document definition ID, can be associated with a workflow with the
   *     workflow definition ID and workflow definition version or {@code null} if no validity
   *     period has been specified
   */
  @JsonIgnore
  @XmlTransient
  public Duration getValidityPeriodAsDuration() {
    if (this.validityPeriod == null) {
      return null;
    } else {
      return DurationStyle.detectAndParse(validityPeriod);
    }
  }

  /**
   * Returns whether a a document with the document definition ID is internal-only and excluded for
   * external users.
   *
   * @return {@code true} if a document with the document definition ID is internal-only and
   *     excluded for external users or {@code false} otherwise
   */
  public boolean isInternal() {
    return internal;
  }

  /**
   * Returns whether a document with the document definition ID is required for a workflow with the
   * workflow definition ID and workflow definition version.
   *
   * @return {@code true} if a document with the document definition ID is required for a workflow
   *     with the workflow definition ID and workflow definition version or {@code false} otherwise
   */
  public boolean isRequired() {
    return required;
  }

  /**
   * Returns whether a workflow with the workflow definition ID and workflow definition version is
   * limited to a single document with the document definition ID.
   *
   * @return {@code true} if a workflow with the workflow definition ID and workflow definition
   *     version is limited to a single document with the document definition ID or {@code false}
   *     otherwise
   */
  public boolean isSingular() {
    return singular;
  }

  /**
   * Returns whether a document with the document definition ID should be manually or automatically
   * verified after being provided for a workflow with the workflow definition ID and workflow
   * definition version.
   *
   * @return {@code true} if a document with the document definition ID should be manually or
   *     automatically verified after being provided for a workflow with the workflow definition ID
   *     and workflow definition version or {@code false otherwise}
   */
  public boolean isVerifiable() {
    return verifiable;
  }

  /**
   * Set the ID for the document definition.
   *
   * @param documentDefinitionId the ID for the document definition
   */
  public void setDocumentDefinitionId(String documentDefinitionId) {
    this.documentDefinitionId = documentDefinitionId;
  }

  /**
   * Set whether a document with the document definition ID is internal-only and excluded for
   * external users.
   *
   * @param internal {@code true} if a document with the document definition ID is internal-only and
   *     excluded for external users or {@code false otherwise}
   */
  public void setInternal(boolean internal) {
    this.internal = internal;
  }

  /**
   * Set whether a document with the document definition ID is required for a workflow with the
   * workflow definition ID and workflow definition version.
   *
   * @param required {@code true} if a document with the document definition ID is required for a
   *     workflow with the workflow definition ID and workflow definition version or {@code false}
   *     otherwise
   */
  public void setRequired(boolean required) {
    this.required = required;
  }

  /**
   * Set whether a workflow with the workflow definition ID and workflow definition version is
   * limited to a single document with the document definition ID.
   *
   * @param unique {@code true} if a workflow with the workflow definition ID and workflow
   *     definition version is limited to a single document with the document definition ID or
   *     {@code false} otherwise
   */
  public void setSingular(boolean unique) {
    this.singular = unique;
  }

  /**
   * Set the ISO-8601 duration format validity period from a document's issue date during which the
   * document, with the document definition ID, can be associated with a workflow with the workflow
   * definition ID and workflow definition version.
   *
   * @param validityPeriod the ISO-8601 duration format validity period from a document's issue date
   *     during which the document, with the document definition ID, can be associated with a
   *     workflow with the workflow definition ID and workflow definition version
   */
  public void setValidityPeriod(String validityPeriod) {
    this.validityPeriod = validityPeriod;
  }

  /**
   * Set whether a document with the document definition ID should be manually or automatically
   * verified after being provided for a workflow with the workflow definition ID and workflow
   * definition version.
   *
   * @param verifiable {@code true} if a document with the document definition ID should be manually
   *     or automatically verified after being provided for a workflow with the workflow definition
   *     ID and workflow definition version or {@code false otherwise}
   */
  public void setVerifiable(boolean verifiable) {
    this.verifiable = verifiable;
  }

  /**
   * Set the workflow definition the workflow definition document definition is associated with.
   *
   * @param workflowDefinition the workflow definition the workflow definition document definition
   *     is associated with
   */
  @JsonBackReference("workflowDefinitionDocumentDefinitionReference")
  @Schema(hidden = true)
  public void setWorkflowDefinition(WorkflowDefinition workflowDefinition) {
    if (workflowDefinition != null) {
      this.workflowDefinitionId = workflowDefinition.getId();
      this.workflowDefinitionVersion = workflowDefinition.getVersion();
    } else {
      this.workflowDefinitionId = null;
      this.workflowDefinitionVersion = 0;
    }
  }

  /**
   * Called by the JAXB runtime when an instance of this class has been completely unmarshalled, but
   * before it is added to its parent.
   *
   * @param unmarshaller the JAXB unmarshaller
   * @param parentObject the parent object
   */
  @SuppressWarnings("unused")
  private void afterUnmarshal(Unmarshaller unmarshaller, Object parentObject) {
    if (parentObject instanceof WorkflowDefinition parent) {
      setWorkflowDefinition(parent);
    }
  }
}
