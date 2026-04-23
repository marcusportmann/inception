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

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
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
 * The {@code DecryptInteractionAttachmentRequest} class represents a request to decrypt an
 * interaction attachment.
 *
 * @author Marcus Portmann
 */
@Schema(description = "A request to decrypt an interaction attachment")
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({"interactionAttachmentId", "password"})
@XmlRootElement(
    name = "DecryptInteractionAttachmentRequest",
    namespace = "https://inception.digital/operations")
@XmlType(
    name = "DecryptInteractionAttachmentRequest",
    namespace = "https://inception.digital/operations",
    propOrder = {"interactionAttachmentId", "password"})
@XmlAccessorType(XmlAccessType.FIELD)
@SuppressWarnings({"unused", "WeakerAccess"})
public class DecryptInteractionAttachmentRequest implements Serializable {

  @Serial private static final long serialVersionUID = 1000000;

  /** The ID for the interaction attachment. */
  @Schema(
      description = "The ID for the interaction attachment",
      requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElement(name = "InteractionAttachmentId", required = true)
  @NotNull
  private UUID interactionAttachmentId;

  /** The password to use to decrypt the interaction attachment. */
  @Schema(
      description = "The password to use to decrypt the interaction attachment",
      requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElement(name = "Password", required = true)
  @NotBlank
  @Size(max = 100)
  private String password;

  /** Constructs a new {@code DecryptInteractionAttachmentRequest}. */
  public DecryptInteractionAttachmentRequest() {}

  /**
   * Constructs a new {@code DecryptInteractionAttachmentRequest}.
   *
   * @param interactionAttachmentId the ID for the interaction attachment
   * @param password the password to use to decrypt the interaction attachment
   */
  @JsonCreator(mode = JsonCreator.Mode.DISABLED)
  public DecryptInteractionAttachmentRequest(UUID interactionAttachmentId, String password) {
    this.interactionAttachmentId = interactionAttachmentId;
    this.password = password;
  }

  /**
   * Returns the ID for the interaction attachment.
   *
   * @return the ID for the interaction attachment
   */
  public UUID getInteractionAttachmentId() {
    return interactionAttachmentId;
  }

  /**
   * Returns the password to use to decrypt the interaction attachment.
   *
   * @return the password to use to decrypt the interaction attachment
   */
  public String getPassword() {
    return password;
  }

  /**
   * Set the ID for the interaction attachment.
   *
   * @param interactionAttachmentId the ID for the interaction attachment
   */
  public void setInteractionAttachmentId(UUID interactionAttachmentId) {
    this.interactionAttachmentId = interactionAttachmentId;
  }

  /**
   * Set the password to use to decrypt the interaction attachment.
   *
   * @param password the password to use to decrypt the interaction attachment
   */
  public void setPassword(String password) {
    this.password = password;
  }
}
