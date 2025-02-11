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
 * The <b>ProcessDefinitionDocumentDefinition</b> class holds the information for an association of
 * a document definition with a process definition.
 *
 * @author Marcus Portmann
 */
@Schema(description = "An association of a document definition with a process definition")
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
  "documentDefinitionId",
  "required",
  "unique",
  "validityPeriodUnit",
  "validityPeriodAmount"
})
@XmlRootElement(
    name = "ProcessDefinitionDocumentDefinition",
    namespace = "https://inception.digital/operations")
@XmlType(
    name = "ProcessDefinitionDocumentDefinition",
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
@Table(name = "operations_process_definition_document_definitions")
@IdClass(ProcessDefinitionDocumentDefinitionId.class)
public class ProcessDefinitionDocumentDefinition implements Serializable {

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

  /** The process definition the process definition document definition is associated with. */
  @Schema(hidden = true)
  @JsonBackReference("processDefinitionDocumentDefinitionReference")
  @XmlTransient
  @Id
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumns({
    @JoinColumn(name = "process_definition_id", referencedColumnName = "id"),
    @JoinColumn(name = "process_definition_version", referencedColumnName = "version")
  })
  private ProcessDefinition processDefinition;

  /**
   * Is a document with the document definition ID required for a process with the process
   * definition ID and process definition version?
   */
  @Schema(
      description =
          "Is a document with the document definition ID required for a process with the process definition ID and process definition version",
      requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElement(name = "Required", required = true)
  @NotNull
  @Column(name = "required", nullable = false)
  private boolean required;

  /**
   * Is a process with the process definition ID and process definition version limited to a single
   * document with the document definition ID?
   */
  @Schema(
      description =
          "Is a process with the process definition ID and process definition version limited to a single document with the document definition ID",
      requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElement(name = "Unique", required = true)
  @NotNull
  @Column(name = "unique", nullable = false)
  private boolean unique;

  /**
   * The validity period from a document's issue date during which the document, with the document
   * definition ID, can be associated with a process with the process definition ID and process
   * definition version.
   */
  @Schema(
      description =
          "The validity period from a document's issue date during which the document, with the document definition ID, can be associated with a process with the process definition ID and process definition version")
  @JsonProperty
  @XmlElement(name = "ValidityPeriodAmount")
  @Column(name = "validity_period_amount")
  private Integer validityPeriodAmount;

  /**
   * The unit of measurement of time for the validity period from a document's issue date during
   * which the document, with the document definition ID, can be associated with a process with the
   * process definition ID and process definition version.
   */
  @Schema(
      description =
          "The unit of measurement of time for the validity period from a document's issue date during which the document, with the document definition ID, can be associated with a process with the process definition ID and process definition version")
  @JsonProperty
  @XmlElement(name = "ValidityPeriodUnit")
  @Column(name = "validity_period_unit")
  private TimeUnit validityPeriodUnit;

  /** Constructs a new <b>ProcessDefinitionDocumentDefinition</b>. */
  public ProcessDefinitionDocumentDefinition() {}

  /**
   * Constructs a new <b>ProcessDefinitionDocumentDefinition</b>.
   *
   * @param processDefinition the process definition the process definition document definition is
   *     associated with
   * @param documentDefinitionId ID for the document definition
   * @param required is a document with the document definition ID required for a process with the
   *     process definition ID and version
   */
  public ProcessDefinitionDocumentDefinition(
      ProcessDefinition processDefinition, String documentDefinitionId, boolean required) {
    this.processDefinition = processDefinition;
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

    ProcessDefinitionDocumentDefinition other = (ProcessDefinitionDocumentDefinition) object;

    return Objects.equals(processDefinition, other.processDefinition)
        && Objects.equals(documentDefinitionId, other.documentDefinitionId);
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
   * Returns the process definition the process definition document definition is associated with.
   *
   * @return the process definition the process definition document definition is associated with
   */
  @Schema(hidden = true)
  public ProcessDefinition getProcessDefinition() {
    return processDefinition;
  }

  /**
   * Returns the validity period from a document's issue date during which the document, with the
   * document definition ID, can be associated with a process with the process definition ID and
   * process definition version.
   *
   * @return the validity period from a document's issue date during which the document, with the
   *     document definition ID, can be associated with a process with the process definition ID and
   *     process definition version
   */
  public Integer getValidityPeriodAmount() {
    return validityPeriodAmount;
  }

  /**
   * Returns the unit of measurement of time for the validity period from a document's issue date
   * during which the document, with the document definition ID, can be associated with a process
   * with the process definition ID and process definition version.
   *
   * @return the unit of measurement of time for the validity period from a document's issue date
   *     during which the document, with the document definition ID, can be associated with a
   *     process with the process definition ID and process definition version
   */
  public TimeUnit getValidityPeriodUnit() {
    return validityPeriodUnit;
  }

  /**
   * Returns whether a document with the document definition ID is required for a process with the
   * process definition ID and version.
   *
   * @return <b>true</b> if a document with the document definition ID is required for a process
   *     with the process definition ID and version or <b>false</b> otherwise
   */
  public boolean isRequired() {
    return required;
  }

  /**
   * Returns whether a process with the process definition ID and process definition version is
   * limited to a single document with the document definition ID.
   *
   * @return <b>true</b> if a process with the process definition ID and process definition version
   *     is limited to a single document with the document definition ID or <b>false</b> otherwise
   */
  public boolean isUnique() {
    return unique;
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
   * Set the process definition the process definition document definition is associated with.
   *
   * @param processDefinition the process definition the process definition document definition is
   *     associated with
   */
  @Schema(hidden = true)
  public void setProcessDefinition(ProcessDefinition processDefinition) {
    this.processDefinition = processDefinition;
  }

  /**
   * Set whether a document with the document definition ID is required for a process with the
   * process definition ID and version.
   *
   * @param required <b>true</b> if a document with the document definition ID is required for a
   *     process with the process definition ID and version or <b>false</b> otherwise
   */
  public void setRequired(boolean required) {
    this.required = required;
  }

  /**
   * Set whether a process with the process definition ID and process definition version is limited
   * to a single document with the document definition ID.
   *
   * @param unique <b>true</b> if a process with the process definition ID and process definition
   *     version is limited to a single document with the document definition ID or <b>false</b>
   *     otherwise
   */
  public void setUnique(boolean unique) {
    this.unique = unique;
  }

  /**
   * Set the validity period from a document's issue date during which the document, with the
   * document definition ID, can be associated with a process with the process definition ID and
   * process definition version.
   *
   * @param validityPeriodAmount the validity period from a document's issue date during which the
   *     document, with the document definition ID, can be associated with a process with the
   *     process definition ID and process definition version
   */
  public void setValidityPeriodAmount(Integer validityPeriodAmount) {
    this.validityPeriodAmount = validityPeriodAmount;
  }

  /**
   * Set the unit of measurement of time for the validity period from a document's issue date during
   * which the document, with the document definition ID, can be associated with a process with the
   * process definition ID and process definition version.
   *
   * @param validityPeriodUnit the unit of measurement of time for the validity period from a
   *     document's issue date during which the document, with the document definition ID, can be
   *     associated with a process with the process definition ID and process definition version
   */
  public void setValidityPeriodUnit(TimeUnit validityPeriodUnit) {
    this.validityPeriodUnit = validityPeriodUnit;
  }
}
