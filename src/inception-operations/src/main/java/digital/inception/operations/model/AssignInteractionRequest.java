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
 * The <b>AssignInteractionRequest</b> class.
 *
 * @author Marcus Portmann
 */
@Schema(description = "A request to assign an interaction to a user")
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({"interactionId", "username"})
@XmlRootElement(
    name = "AssignInteractionRequest",
    namespace = "https://inception.digital/operations")
@XmlType(
    name = "AssignInteractionRequest",
    namespace = "https://inception.digital/operations",
    propOrder = {"interactionId", "username"})
@XmlAccessorType(XmlAccessType.FIELD)
@SuppressWarnings({"unused", "WeakerAccess"})
public class AssignInteractionRequest implements Serializable {

  @Serial private static final long serialVersionUID = 1000000;

  /** The ID for the interaction. */
  @Schema(description = "The ID for the interaction", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElement(name = "InteractionId", required = true)
  @NotNull
  private UUID interactionId;

  /** The username for the user the interaction should be assigned to. */
  @Schema(
      description = "The username for the user the interaction should be assigned to",
      requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElement(name = "Username", required = true)
  @NotNull
  @Size(min = 1, max = 100)
  private String username;

  /** Constructs a new <b>AssignInteractionRequest</b>. */
  public AssignInteractionRequest() {}

  /**
   * Constructs a new <b>AssignInteractionRequest</b>.
   *
   * @param interactionId the ID for the interaction
   * @param username the username for the user the interaction should be assigned to
   */
  public AssignInteractionRequest(UUID interactionId, String username) {
    this.interactionId = interactionId;
    this.username = username;
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
   * Returns the username for the user the interaction should be assigned to.
   *
   * @return the username for the user the interaction should be assigned to
   */
  public String getUsername() {
    return username;
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
   * Set the username for the user the interaction should be assigned to
   *
   * @param username the username for the user the interaction should be assigned to
   */
  public void setUsername(String username) {
    this.username = username;
  }
}
