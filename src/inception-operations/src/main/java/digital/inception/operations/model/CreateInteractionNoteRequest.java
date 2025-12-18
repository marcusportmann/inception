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
 * The {@code CreateInteractionNoteRequest} class represents a request to create an interaction
 * note.
 *
 * @author Marcus Portmann
 */
@Schema(description = "A request to create an interaction note")
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({"interactionId", "content"})
@XmlRootElement(
    name = "CreateInteractionNoteRequest",
    namespace = "https://inception.digital/operations")
@XmlType(
    name = "CreateInteractionNoteRequest",
    namespace = "https://inception.digital/operations",
    propOrder = {"interactionId", "content"})
@XmlAccessorType(XmlAccessType.FIELD)
@SuppressWarnings({"unused", "WeakerAccess"})
public class CreateInteractionNoteRequest implements Serializable {

  @Serial private static final long serialVersionUID = 1000000;

  /** The content for the interaction note. */
  @Schema(
      description = "The content for the interaction note",
      requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElement(name = "Content", required = true)
  @NotNull
  @Size(min = 1, max = 2000)
  private String content;

  /** The ID for the interaction the interaction note is associated with. */
  @Schema(
      description = "The ID for the interaction the interaction note is associated with",
      requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElement(name = "InteractionId", required = true)
  @NotNull
  private UUID interactionId;

  /** Constructs a new {@code CreateInteractionNoteRequest}. */
  public CreateInteractionNoteRequest() {}

  /**
   * Constructs a new {@code CreateInteractionNoteRequest}.
   *
   * @param interactionId the ID for the interaction the interaction note is associated with
   * @param content the content for the interaction note
   */
  public CreateInteractionNoteRequest(UUID interactionId, String content) {
    this.interactionId = interactionId;
    this.content = content;
  }

  /**
   * Returns the content for the interaction note.
   *
   * @return the content for the interaction note
   */
  public String getContent() {
    return content;
  }

  /**
   * Returns the ID for the interaction the interaction note is associated with.
   *
   * @return the ID for the interaction the interaction note is associated with
   */
  public UUID getInteractionId() {
    return interactionId;
  }

  /**
   * Sets the content for the interaction note.
   *
   * @param content the content for the interaction note
   */
  public void setContent(String content) {
    this.content = content;
  }

  /**
   * Sets the ID for the interaction the interaction note is associated with.
   *
   * @param interactionId the ID for the interaction the interaction note is associated with
   */
  public void setInteractionId(UUID interactionId) {
    this.interactionId = interactionId;
  }
}
