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
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;
import java.io.Serial;
import java.io.Serializable;
import java.util.UUID;

/**
 * The {@code RejectWorkflowDocumentRequest} class represents a request to reject a workflow
 * document.
 *
 * @author Marcus Portmann
 */
@Schema(description = "A request to reject a workflow document")
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({"workflowDocumentId", "rejectionReason", "description"})
@XmlRootElement(
    name = "RejectWorkflowDocumentRequest",
    namespace = "https://inception.digital/operations")
@XmlType(
    name = "RejectWorkflowDocumentRequest",
    namespace = "https://inception.digital/operations",
    propOrder = {"workflowDocumentId", "rejectionReason", "description"})
@XmlAccessorType(XmlAccessType.FIELD)
@SuppressWarnings({"unused", "WeakerAccess"})
public class RejectWorkflowDocumentRequest implements Serializable {

  @Serial private static final long serialVersionUID = 1000000;

  /** The description for the workflow document. */
  @Schema(description = "The description for the workflow document")
  @JsonProperty
  @XmlElement(name = "Description")
  @Size(max = 500)
  private String description;

  /** The reason the workflow document was rejected. */
  @Schema(description = "The reason the workflow document was rejected")
  @JsonProperty(required = true)
  @XmlElement(name = "RejectionReason", required = true)
  @NotNull
  @Size(min = 1, max = 100)
  private String rejectionReason;

  /** The ID for the workflow document. */
  @Schema(
      description = "The ID for the workflow document",
      requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElement(name = "WorkflowDocumentId", required = true)
  @NotNull
  private UUID workflowDocumentId;

  /** Constructs a new {@code RejectWorkflowDocumentRequest}. */
  public RejectWorkflowDocumentRequest() {}

  /**
   * Constructs a new {@code RejectWorkflowDocumentRequest}.
   *
   * @param workflowDocumentId the ID for the workflow document
   * @param rejectionReason the reason the workflow document was rejected
   */
  public RejectWorkflowDocumentRequest(UUID workflowDocumentId, String rejectionReason) {
    this.workflowDocumentId = workflowDocumentId;
    this.rejectionReason = rejectionReason;
  }

  /**
   * Constructs a new {@code RejectWorkflowDocumentRequest}.
   *
   * @param workflowDocumentId the ID for the workflow document
   * @param rejectionReason the reason the workflow document was rejected
   * @param description the description for the workflow document
   */
  public RejectWorkflowDocumentRequest(
      UUID workflowDocumentId, String rejectionReason, String description) {
    this.workflowDocumentId = workflowDocumentId;
    this.rejectionReason = rejectionReason;
    this.description = description;
  }

  /**
   * Returns the description for the workflow document.
   *
   * @return the description for the workflow document
   */
  public String getDescription() {
    return description;
  }

  /**
   * Returns the reason the workflow document was rejected.
   *
   * @return the reason the workflow document was rejected
   */
  public String getRejectionReason() {
    return rejectionReason;
  }

  /**
   * Returns the ID for the workflow document.
   *
   * @return the ID for the workflow document
   */
  public UUID getWorkflowDocumentId() {
    return workflowDocumentId;
  }

  /**
   * Set the description for the workflow document.
   *
   * @param description the description for the workflow document
   */
  public void setDescription(String description) {
    this.description = description;
  }

  /**
   * Set the reason the workflow document was rejected.
   *
   * @param rejectionReason the reason the workflow document was rejected
   */
  public void setRejectionReason(String rejectionReason) {
    this.rejectionReason = rejectionReason;
  }

  /**
   * Set the ID for the workflow document.
   *
   * @param workflowDocumentId the ID for the workflow document
   */
  public void setWorkflowDocumentId(UUID workflowDocumentId) {
    this.workflowDocumentId = workflowDocumentId;
  }
}
