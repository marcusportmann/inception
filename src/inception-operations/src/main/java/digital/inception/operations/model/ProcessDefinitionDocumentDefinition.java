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
@JsonPropertyOrder({"documentDefinitionId", "required"})
@XmlRootElement(
    name = "ProcessDefinitionDocumentDefinition",
    namespace = "https://inception.digital/operations")
@XmlType(
    name = "ProcessDefinitionDocumentDefinition",
    namespace = "https://inception.digital/operations",
    propOrder = {"documentDefinitionId", "required"})
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
  @Id
  @Column(name = "document_definition_id", length = 100, nullable = false)
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
   * definition ID and version?
   */
  @Schema(
      description =
          "Is a document with the document definition ID required for a process with the process definition ID and version",
      requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElement(name = "Required", required = true)
  @NotNull
  @Column(name = "required", nullable = false)
  private boolean required;

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
   * Returns whether a document with the document definition ID is required for a process with the
   * process definition ID and version.
   *
   * @return <b>true</b> if a document with the document definition ID is required for a process
   *     with the process definition ID and version or <b>false</b> otherwise
   */
  public boolean getRequired() {
    return required;
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
}
