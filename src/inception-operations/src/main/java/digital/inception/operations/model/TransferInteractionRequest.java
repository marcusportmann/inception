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
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;
import java.io.Serial;
import java.io.Serializable;
import java.util.UUID;

/**
 * The {@code TransferInteractionRequest} class represents a request to transfer an interaction to
 * an interaction source.
 *
 * @author Marcus Portmann
 */
@Schema(description = "A request to transfer an interaction to an interaction source")
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({"interactionId", "interactionSourceId"})
@XmlRootElement(
    name = "TransferInteractionRequest",
    namespace = "https://inception.digital/operations")
@XmlType(
    name = "TransferInteractionRequest",
    namespace = "https://inception.digital/operations",
    propOrder = {"interactionId", "interactionSourceId"})
@XmlAccessorType(XmlAccessType.FIELD)
@SuppressWarnings({"unused", "WeakerAccess"})
public class TransferInteractionRequest implements Serializable {

  @Serial private static final long serialVersionUID = 1000000;

  /** The ID for the interaction. */
  @Schema(description = "The ID for the interaction", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElement(name = "InteractionId", required = true)
  @NotNull
  private UUID interactionId;

  /** The ID for the interaction source the interaction should be transferred to. */
  @Schema(
      description = "The ID for the interaction source the interaction should be transferred to",
      requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElement(name = "InteractionSourceId", required = true)
  @NotNull
  private UUID interactionSourceId;

  /** Constructs a new {@code TransferInteractionRequest}. */
  public TransferInteractionRequest() {}

  /**
   * Constructs a new {@code TransferInteractionRequest}.
   *
   * @param interactionId the ID for the interaction
   * @param interactionSourceId the ID for the interaction source the interaction should be
   *     transferred to
   */
  public TransferInteractionRequest(UUID interactionId, UUID interactionSourceId) {
    this.interactionId = interactionId;
    this.interactionSourceId = interactionSourceId;
  }

  /**
   * Returns the ID for the interaction.
   *
   * @return the ID for the interaction
   */
  public UUID getInteractionId() {
    return interactionId;
  }

  /**
   * Returns the ID for the interaction source the interaction should be transferred to.
   *
   * @return the ID for the interaction source the interaction should be transferred to
   */
  public UUID getInteractionSourceId() {
    return interactionSourceId;
  }

  /**
   * Set the ID for the interaction.
   *
   * @param interactionId the ID for the interaction
   */
  public void setInteractionId(UUID interactionId) {
    this.interactionId = interactionId;
  }

  /**
   * Set the ID for the interaction source the interaction should be transferred to.
   *
   * @param interactionSourceId the ID for the interaction source the interaction should be
   *     transferred to
   */
  public void setInteractionSourceId(UUID interactionSourceId) {
    this.interactionSourceId = interactionSourceId;
  }
}
