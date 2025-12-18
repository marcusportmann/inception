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
 * The {@code UpdateDocumentNoteRequest} class represents a request to update a document note.
 *
 * @author Marcus Portmann
 */
@Schema(description = "A request to update a document note")
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({"documentNoteId", "content"})
@XmlRootElement(
    name = "UpdateDocumentNoteRequest",
    namespace = "https://inception.digital/operations")
@XmlType(
    name = "UpdateDocumentNoteRequest",
    namespace = "https://inception.digital/operations",
    propOrder = {"documentNoteId", "content"})
@XmlAccessorType(XmlAccessType.FIELD)
@SuppressWarnings({"unused", "WeakerAccess"})
public class UpdateDocumentNoteRequest implements Serializable {

  @Serial private static final long serialVersionUID = 1000000;

  /** The content for the document note. */
  @Schema(
      description = "The content for the document note",
      requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElement(name = "Content", required = true)
  @NotNull
  @Size(min = 1, max = 2000)
  private String content;

  /** The ID for the document note. */
  @Schema(description = "The ID for the document note", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElement(name = "DocumentNoteId", required = true)
  @NotNull
  private UUID documentNoteId;

  /** Constructs a new {@code UpdateDocumentNoteRequest}. */
  public UpdateDocumentNoteRequest() {}

  /**
   * Constructs a new {@code UpdateDocumentNoteRequest}.
   *
   * @param documentNoteId the ID for the document note
   * @param content the content for the document note
   */
  public UpdateDocumentNoteRequest(UUID documentNoteId, String content) {
    this.documentNoteId = documentNoteId;
    this.content = content;
  }

  /**
   * Returns the content for the document note.
   *
   * @return the content for the document note
   */
  public String getContent() {
    return content;
  }

  /**
   * Returns the ID for the document note.
   *
   * @return the ID for the document note
   */
  public UUID getDocumentNoteId() {
    return documentNoteId;
  }

  /**
   * Sets the content for the document note.
   *
   * @param content the content for the document note
   */
  public void setContent(String content) {
    this.content = content;
  }

  /**
   * Sets the ID for the document note.
   *
   * @param documentNoteId the ID for the document note
   */
  public void setDocumentNoteId(UUID documentNoteId) {
    this.documentNoteId = documentNoteId;
  }
}
