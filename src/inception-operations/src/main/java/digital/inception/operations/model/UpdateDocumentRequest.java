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
import digital.inception.operations.constraint.ValidUpdateDocumentRequest;
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
 * The <b>UpdateDocumentRequest</b> class holds the information for a request to update a document.
 *
 * @author Marcus Portmann
 */
@Schema(description = "A request to update a document")
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({"id", "status", "data"})
@XmlRootElement(name = "UpdateDocumentRequest", namespace = "https://inception.digital/operations")
@XmlType(
    name = "UpdateDocumentRequest",
    namespace = "https://inception.digital/operations",
    propOrder = {"id", "status", "data"})
@XmlAccessorType(XmlAccessType.FIELD)
@ValidUpdateDocumentRequest
@SuppressWarnings({"unused", "WeakerAccess"})
public class UpdateDocumentRequest implements Serializable {

  @Serial private static final long serialVersionUID = 1000000;


  /** The ID for the document. */
  @Schema(description = "The ID for the document", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElement(name = "Id", required = true)
  @NotNull
  private UUID id;


  ADD DOCUMENT ATTRIBUTES
  
  
  
  /** Constructs a new <b>UpdateDocumentRequest</b>. */
  public UpdateDocumentRequest() {}

  /**
   * Constructs a new <b>UpdateDocumentRequest</b>.
   *
   * @param id the ID for the document
   * @param data the updated data for the document
   */
  public UpdateDocumentRequest(UUID id, String data) {
    this.id = id;
    this.data = data;
  }

  /**
   * Constructs a new <b>UpdateDocumentRequest</b>.
   *
   * @param id the ID for the document
   * @param status the updated status of the document
   */
  public UpdateDocumentRequest(UUID id, DocumentStatus status) {
    this.id = id;
    this.status = status;
  }

  /**
   * Constructs a new <b>UpdateDocumentRequest</b>.
   *
   * @param id the ID for the document
   * @param status the updated status of the document
   * @param data the updated data for the document
   */
  public UpdateDocumentRequest(UUID id, DocumentStatus status, String data) {
    this.id = id;
    this.status = status;
    this.data = data;
  }

  /**
   * Returns the updated data for the document.
   *
   * @return the updated data for the document
   */
  public String getData() {
    return data;
  }

  /**
   * Returns the ID for the document.
   *
   * @return the ID for the document
   */
  public UUID getId() {
    return id;
  }

  /**
   * Returns the updated status of the document.
   *
   * @return the updated status of the document
   */
  public DocumentStatus getStatus() {
    return status;
  }

  /**
   * Set the updated data for the document.
   *
   * @param data the updated data for the document
   */
  public void setData(String data) {
    this.data = data;
  }

  /**
   * Set the ID for the document.
   *
   * @param id the ID for the document
   */
  public void setId(UUID id) {
    this.id = id;
  }

  /**
   * Set the updated status of the document.
   *
   * @param status the updated status of the document
   */
  public void setStatus(DocumentStatus status) {
    this.status = status;
  }
}
