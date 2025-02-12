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
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import digital.inception.core.time.TimeUnit;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinColumns;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlTransient;
import jakarta.xml.bind.annotation.XmlType;
import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

/**
 * The <b>WorkflowDefinitionDocumentDefinition</b> class holds the information for an association of a
 * document definition with a workflow definition.
 *
 * @author Marcus Portmann
 */
@Schema(description = "An association of a document definition with a workflow definition")
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
  "documentDefinitionId",
  "required",
  "unique",
  "validityPeriodUnit",
  "validityPeriodAmount"
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
      "unique",
      "validityPeriodUnit",
      "validityPeriodAmount"
    })
@XmlAccessorType(XmlAccessType.FIELD)
@Entity
@Table(name = "operations_workflow_definition_document_definitions")
@IdClass(WorkflowDefinitionDocumentDefinitionId.class)
public class WorkflowDefinitionDocumentDefinition implements Serializable {

  @Serial private static final long serialVersionUID = 1000000;

  /** The workflow definition the workflow definition document definition is associated with. */
  @Schema(hidden = true)
  @JsonBackReference("workflowDefinitionDocumentDefinitionReference")
  @XmlTransient
  @Id
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumns({
    @JoinColumn(name = "workflow_definition_id", referencedColumnName = "id"),
    @JoinColumn(name = "workflow_definition_version", referencedColumnName = "version")
  })
  private WorkflowDefinition workflowDefinition;

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
   * Is a document with the document definition ID required for a workflow with the workflow definition ID
   * and workflow definition version?
   */
  @Schema(
      description =
          "Is a document with the document definition ID required for a workflow with the workflow definition ID and workflow definition version",
      requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElement(name = "Required", required = true)
  @NotNull
  @Column(name = "required", nullable = false)
  private boolean required;

  /**
   * Is a workflow with the workflow definition ID and workflow definition version limited to a single document
   * with the document definition ID?
   */
  @Schema(
      description =
          "Is a workflow with the workflow definition ID and workflow definition version limited to a single document with the document definition ID",
      requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElement(name = "Unique", required = true)
  @NotNull
  @Column(name = "unique", nullable = false)
  private boolean unique;

  /**
   * The validity period from a document's issue date during which the document, with the document
   * definition ID, can be associated with a workflow with the workflow definition ID and workflow definition
   * version.
   */
  @Schema(
      description =
          "The validity period from a document's issue date during which the document, with the document definition ID, can be associated with a workflow with the workflow definition ID and workflow definition version")
  @JsonProperty
  @XmlElement(name = "ValidityPeriodAmount")
  @Column(name = "validity_period_amount")
  private Integer validityPeriodAmount;

  /**
   * The unit of measurement of time for the validity period from a document's issue date during
   * which the document, with the document definition ID, can be associated with a workflow with the
   * workflow definition ID and workflow definition version.
   */
  @Schema(
      description =
          "The unit of measurement of time for the validity period from a document's issue date during which the document, with the document definition ID, can be associated with a workflow with the workflow definition ID and workflow definition version")
  @JsonProperty
  @XmlElement(name = "ValidityPeriodUnit")
  @Column(name = "validity_period_unit")
  private TimeUnit validityPeriodUnit;

  /** Constructs a new <b>WorkflowDefinitionDocumentDefinition</b>. */
  public WorkflowDefinitionDocumentDefinition() {}

  /**
   * Constructs a new <b>WorkflowDefinitionDocumentDefinition</b>.
   *
   * @param workflowDefinition the workflow definition the workflow definition document definition is associated
   *     with
   * @param documentDefinitionId ID for the document definition
   * @param required is a document with the document definition ID required for a workflow with the workflow
   *     definition ID and workflow definition version
   */
  public WorkflowDefinitionDocumentDefinition(
      WorkflowDefinition workflowDefinition, String documentDefinitionId, boolean required) {
    this.workflowDefinition = workflowDefinition;
    this.documentDefinitionId = documentDefinitionId;
    this.required = required;
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

    WorkflowDefinitionDocumentDefinition other = (WorkflowDefinitionDocumentDefinition) object;

    return Objects.equals(workflowDefinition, other.workflowDefinition)
        && Objects.equals(documentDefinitionId, other.documentDefinitionId);
  }

  /**
   * Returns the workflow definition the workflow definition document definition is associated with.
   *
   * @return the workflow definition the workflow definition document definition is associated with
   */
  @Schema(hidden = true)
  public WorkflowDefinition getWorkflowDefinition() {
    return workflowDefinition;
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
   * Returns the validity period from a document's issue date during which the document, with the
   * document definition ID, can be associated with a workflow with the workflow definition ID and workflow
   * definition version.
   *
   * @return the validity period from a document's issue date during which the document, with the
   *     document definition ID, can be associated with a workflow with the workflow definition ID and workflow
   *     definition version
   */
  public Integer getValidityPeriodAmount() {
    return validityPeriodAmount;
  }

  /**
   * Returns the unit of measurement of time for the validity period from a document's issue date
   * during which the document, with the document definition ID, can be associated with a workflow with
   * the workflow definition ID and workflow definition version.
   *
   * @return the unit of measurement of time for the validity period from a document's issue date
   *     during which the document, with the document definition ID, can be associated with a workflow
   *     with the workflow definition ID and workflow definition version
   */
  public TimeUnit getValidityPeriodUnit() {
    return validityPeriodUnit;
  }

  /**
   * Returns whether a document with the document definition ID is required for a workflow with the workflow
   * definition ID and workflow definition version.
   *
   * @return <b>true</b> if a document with the document definition ID is required for a workflow with
   *     the workflow definition ID and workflow definition version or <b>false</b> otherwise
   */
  public boolean isRequired() {
    return required;
  }

  /**
   * Returns whether a workflow with the workflow definition ID and workflow definition version is limited to a
   * single document with the document definition ID.
   *
   * @return <b>true</b> if a workflow with the workflow definition ID and workflow definition version is
   *     limited to a single document with the document definition ID or <b>false</b> otherwise
   */
  public boolean isUnique() {
    return unique;
  }

  /**
   * Set the workflow definition the workflow definition document definition is associated with.
   *
   * @param workflowDefinition the workflow definition the workflow definition document definition is associated
   *     with
   */
  @Schema(hidden = true)
  public void setWorkflowDefinition(WorkflowDefinition workflowDefinition) {
    this.workflowDefinition = workflowDefinition;
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
   * Set whether a document with the document definition ID is required for a workflow with the workflow
   * definition ID and workflow definition version.
   *
   * @param required <b>true</b> if a document with the document definition ID is required for a
   *     workflow with the workflow definition ID and workflow definition version or <b>false</b> otherwise
   */
  public void setRequired(boolean required) {
    this.required = required;
  }

  /**
   * Set whether a workflow with the workflow definition ID and workflow definition version is limited to a
   * single document with the document definition ID.
   *
   * @param unique <b>true</b> if a workflow with the workflow definition ID and workflow definition version is
   *     limited to a single document with the document definition ID or <b>false</b> otherwise
   */
  public void setUnique(boolean unique) {
    this.unique = unique;
  }

  /**
   * Set the validity period from a document's issue date during which the document, with the
   * document definition ID, can be associated with a workflow with the workflow definition ID and workflow
   * definition version.
   *
   * @param validityPeriodAmount the validity period from a document's issue date during which the
   *     document, with the document definition ID, can be associated with a workflow with the workflow
   *     definition ID and workflow definition version
   */
  public void setValidityPeriodAmount(Integer validityPeriodAmount) {
    this.validityPeriodAmount = validityPeriodAmount;
  }

  /**
   * Set the unit of measurement of time for the validity period from a document's issue date during
   * which the document, with the document definition ID, can be associated with a workflow with the
   * workflow definition ID and workflow definition version.
   *
   * @param validityPeriodUnit the unit of measurement of time for the validity period from a
   *     document's issue date during which the document, with the document definition ID, can be
   *     associated with a workflow with the workflow definition ID and workflow definition version
   */
  public void setValidityPeriodUnit(TimeUnit validityPeriodUnit) {
    this.validityPeriodUnit = validityPeriodUnit;
  }
}
