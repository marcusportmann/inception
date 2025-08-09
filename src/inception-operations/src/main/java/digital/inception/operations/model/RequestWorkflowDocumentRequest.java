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
 * The {@code RequestWorkflowDocumentRequest} class represents a request to request a workflow
 * document.
 *
 * @author Marcus Portmann
 */
@Schema(description = "A request to request a workflow document")
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({"workflowId", "documentDefinitionId"})
@XmlRootElement(
    name = "RequestWorkflowDocumentRequest",
    namespace = "https://inception.digital/operations")
@XmlType(
    name = "RequestWorkflowDocumentRequest",
    namespace = "https://inception.digital/operations",
    propOrder = {"workflowId", "documentDefinitionId"})
@XmlAccessorType(XmlAccessType.FIELD)
@SuppressWarnings({"unused", "WeakerAccess"})
public class RequestWorkflowDocumentRequest implements Serializable {

  @Serial private static final long serialVersionUID = 1000000;

  /** The ID for the document definition the workflow document will be associated with. */
  @Schema(
      description =
          "The ID for the document definition the workflow document will be associated with",
      requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElement(name = "DocumentDefinitionId", required = true)
  @NotNull
  @Size(min = 1, max = 50)
  private String documentDefinitionId;

  /** The ID for the workflow. */
  @Schema(description = "The ID for the workflow", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElement(name = "WorkflowId", required = true)
  @NotNull
  private UUID workflowId;

  /** Constructs a new {@code RequestWorkflowDocumentRequest}. */
  public RequestWorkflowDocumentRequest() {}

  /**
   * Constructs a new {@code RequestWorkflowDocumentRequest}.
   *
   * @param workflowId the ID for the workflow
   * @param documentDefinitionId the ID for the document definition the workflow document will be
   *     associated with
   */
  public RequestWorkflowDocumentRequest(UUID workflowId, String documentDefinitionId) {
    this.workflowId = workflowId;
    this.documentDefinitionId = documentDefinitionId;
  }

  /**
   * Returns the ID for the document definition the workflow document will be associated with.
   *
   * @return the ID for the document definition the workflow document will be associated with
   */
  public String getDocumentDefinitionId() {
    return documentDefinitionId;
  }

  /**
   * Returns the ID for the workflow.
   *
   * @return the ID for the workflow
   */
  public UUID getWorkflowId() {
    return workflowId;
  }

  /**
   * Set the ID for the document definition the workflow document will be associated with.
   *
   * @param documentDefinitionId the ID for the document definition the workflow document will be
   *     associated with
   */
  public void setDocumentDefinitionId(String documentDefinitionId) {
    this.documentDefinitionId = documentDefinitionId;
  }

  /**
   * Set the ID for the workflow.
   *
   * @param workflowId the ID for the workflow
   */
  public void setWorkflowId(UUID workflowId) {
    this.workflowId = workflowId;
  }
}
