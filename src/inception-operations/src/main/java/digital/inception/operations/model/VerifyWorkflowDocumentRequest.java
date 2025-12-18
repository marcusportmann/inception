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
 * The {@code VerifyWorkflowDocumentRequest} class represents a request to verify a workflow
 * document.
 *
 * @author Marcus Portmann
 */
@Schema(description = "A request to verify a workflow document")
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({"workflowDocumentId", "description"})
@XmlRootElement(
    name = "VerifyWorkflowDocumentRequest",
    namespace = "https://inception.digital/operations")
@XmlType(
    name = "VerifyWorkflowDocumentRequest",
    namespace = "https://inception.digital/operations",
    propOrder = {"workflowDocumentId", "description"})
@XmlAccessorType(XmlAccessType.FIELD)
@SuppressWarnings({"unused", "WeakerAccess"})
public class VerifyWorkflowDocumentRequest implements Serializable {

  @Serial private static final long serialVersionUID = 1000000;

  /** The description for the workflow document. */
  @Schema(description = "The description for the workflow document")
  @JsonProperty
  @XmlElement(name = "Description")
  @Size(max = 500)
  private String description;

  /** The ID for the workflow document. */
  @Schema(
      description = "The ID for the workflow document",
      requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElement(name = "WorkflowDocumentId", required = true)
  @NotNull
  private UUID workflowDocumentId;

  /** Constructs a new {@code VerifyWorkflowDocumentRequest}. */
  public VerifyWorkflowDocumentRequest() {}

  /**
   * Constructs a new {@code VerifyWorkflowDocumentRequest}.
   *
   * @param workflowDocumentId the ID for the workflow document
   */
  public VerifyWorkflowDocumentRequest(UUID workflowDocumentId) {
    this.workflowDocumentId = workflowDocumentId;
  }

  /**
   * Constructs a new {@code VerifyWorkflowDocumentRequest}.
   *
   * @param workflowDocumentId the ID for the workflow document
   * @param description the description for the workflow document
   */
  public VerifyWorkflowDocumentRequest(UUID workflowDocumentId, String description) {
    this.workflowDocumentId = workflowDocumentId;
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
   * Returns the ID for the workflow document.
   *
   * @return the ID for the workflow document
   */
  public UUID getWorkflowDocumentId() {
    return workflowDocumentId;
  }

  /**
   * Sets the description for the workflow document.
   *
   * @param description the description for the workflow document
   */
  public void setDescription(String description) {
    this.description = description;
  }

  /**
   * Sets the ID for the workflow document.
   *
   * @param workflowDocumentId the ID for the workflow document
   */
  public void setWorkflowDocumentId(UUID workflowDocumentId) {
    this.workflowDocumentId = workflowDocumentId;
  }
}
