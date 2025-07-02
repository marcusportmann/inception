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
 * The {@code CreateDocumentNoteRequest} class represents a request to create a document note.
 *
 * @author Marcus Portmann
 */
@Schema(description = "A request to create a document note")
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({"documentId", "content"})
@XmlRootElement(
    name = "CreateDocumentNoteRequest",
    namespace = "https://inception.digital/operations")
@XmlType(
    name = "CreateDocumentNoteRequest",
    namespace = "https://inception.digital/operations",
    propOrder = {"documentId", "content"})
@XmlAccessorType(XmlAccessType.FIELD)
@SuppressWarnings({"unused", "WeakerAccess"})
public class CreateDocumentNoteRequest implements Serializable {

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

  /** The ID for the document the document note is associated with. */
  @Schema(
      description = "The ID for the document the document note is associated with",
      requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElement(name = "DocumentId", required = true)
  @NotNull
  private UUID documentId;

  /** Constructs a new {@code CreateDocumentNoteRequest}. */
  public CreateDocumentNoteRequest() {}

  /**
   * Constructs a new {@code CreateDocumentNoteRequest}.
   *
   * @param documentId the ID for the document the document note is associated with
   * @param content the content for the document note
   */
  public CreateDocumentNoteRequest(UUID documentId, String content) {
    this.documentId = documentId;
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
   * Returns the ID for the document the document note is associated with.
   *
   * @return the ID for the document the document note is associated with
   */
  public UUID getDocumentId() {
    return documentId;
  }

  /**
   * Set the content for the document note.
   *
   * @param content the content for the document note
   */
  public void setContent(String content) {
    this.content = content;
  }

  /**
   * Set the ID for the document the document note is associated with.
   *
   * @param documentId the ID for the document the document note is associated with
   */
  public void setDocumentId(UUID documentId) {
    this.documentId = documentId;
  }
}
