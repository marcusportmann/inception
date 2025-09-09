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
 * The {@code InitiateWorkflowInteractionLink} class holds the information for a workflow
 * interaction link specified when initiating a workflow.
 *
 * @author Marcus Portmann
 */
@Schema(description = "A workflow interaction link specified when initiating a workflow")
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({"interactionId", "conversationId"})
@XmlRootElement(
    name = "InitiateWorkflowInteractionLink",
    namespace = "https://inception.digital/operations")
@XmlType(
    name = "InitiateWorkflowInteractionLink",
    namespace = "https://inception.digital/operations",
    propOrder = {"interactionId", "conversationId"})
@XmlAccessorType(XmlAccessType.FIELD)
public class InitiateWorkflowInteractionLink implements Serializable {

  @Serial private static final long serialVersionUID = 1000000;

  /** The ID for the conversation the workflow interaction link is associated with. */
  @Schema(
      description = "The ID for the conversation the workflow interaction link is associated with", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElement(name = "ConversationId", required = true)
  @NotNull
  @Size(min = 1, max = 30)
  private String conversationId;

  /** The ID for the interaction the workflow interaction link is associated with. */
  @Schema(
      description = "The ID for the interaction the workflow interaction link is associated with",
      requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElement(name = "InteractionId", required = true)
  @NotNull
  private UUID interactionId;

  /** Constructs a new {@code InitiateWorkflowInteractionLink}. */
  public InitiateWorkflowInteractionLink() {}

  /**
   * Constructs a new {@code InitiateWorkflowInteractionLink}.
   *
   * @param interactionId the ID for the interaction the workflow interaction link is associated
   *     with
   * @param conversationId the ID for the conversation the workflow interaction link is associated
   *     with
   */
  public InitiateWorkflowInteractionLink(UUID interactionId, String conversationId) {
    this.interactionId = interactionId;
    this.conversationId = conversationId;
  }

  /**
   * Returns the ID for the conversation the workflow interaction link is associated with.
   *
   * @return the ID for the conversation the workflow interaction link is associated with
   */
  public String getConversationId() {
    return conversationId;
  }

  /**
   * Returns the ID for the interaction the workflow interaction link is associated with.
   *
   * @return the ID for the interaction the workflow interaction link is associated with
   */
  public UUID getInteractionId() {
    return interactionId;
  }

  /**
   * Set the ID for the conversation the workflow interaction link is associated with.
   *
   * @param conversationId the ID for the conversation the workflow interaction link is associated
   *     with
   */
  public void setConversationId(String conversationId) {
    this.conversationId = conversationId;
  }

  /**
   * Set the ID for the interaction the workflow interaction link is associated with.
   *
   * @param interactionId the ID for the interaction the workflow interaction link is associated
   *     with
   */
  public void setInteractionId(UUID interactionId) {
    this.interactionId = interactionId;
  }
}
