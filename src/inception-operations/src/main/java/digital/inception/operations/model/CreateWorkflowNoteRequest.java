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
 * The {@code CreateWorkflowNoteRequest} class represents a request to create a workflow note.
 *
 * @author Marcus Portmann
 */
@Schema(description = "A request to create a workflow note")
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({"workflowId", "content"})
@XmlRootElement(
    name = "CreateWorkflowNoteRequest",
    namespace = "https://inception.digital/operations")
@XmlType(
    name = "CreateWorkflowNoteRequest",
    namespace = "https://inception.digital/operations",
    propOrder = {"workflowId", "content"})
@XmlAccessorType(XmlAccessType.FIELD)
@SuppressWarnings({"unused", "WeakerAccess"})
public class CreateWorkflowNoteRequest implements Serializable {

  @Serial private static final long serialVersionUID = 1000000;

  /** The content for the workflow note. */
  @Schema(
      description = "The content for the workflow note",
      requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElement(name = "Content", required = true)
  @NotNull
  @Size(min = 1, max = 2000)
  private String content;

  /** The ID for the workflow the workflow note is associated with. */
  @Schema(
      description = "The ID for the workflow the workflow note is associated with",
      requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElement(name = "WorkflowId", required = true)
  @NotNull
  private UUID workflowId;

  /** Constructs a new {@code CreateWorkflowNoteRequest}. */
  public CreateWorkflowNoteRequest() {}

  /**
   * Constructs a new {@code CreateWorkflowNoteRequest}.
   *
   * @param workflowId the ID for the workflow the workflow note is associated with
   * @param content the content for the workflow note
   */
  public CreateWorkflowNoteRequest(UUID workflowId, String content) {
    this.workflowId = workflowId;
    this.content = content;
  }

  /**
   * Returns the content for the workflow note.
   *
   * @return the content for the workflow note
   */
  public String getContent() {
    return content;
  }

  /**
   * Returns the ID for the workflow the workflow note is associated with.
   *
   * @return the ID for the workflow the workflow note is associated with
   */
  public UUID getWorkflowId() {
    return workflowId;
  }

  /**
   * Set the content for the workflow note.
   *
   * @param content the content for the workflow note
   */
  public void setContent(String content) {
    this.content = content;
  }

  /**
   * Set the ID for the workflow the workflow note is associated with.
   *
   * @param workflowId the ID for the workflow the workflow note is associated with
   */
  public void setWorkflowId(UUID workflowId) {
    this.workflowId = workflowId;
  }
}
