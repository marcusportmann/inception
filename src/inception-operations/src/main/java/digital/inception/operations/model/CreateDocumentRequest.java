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
import digital.inception.operations.constraint.ValidCreateDocumentRequest;
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
 * The <b>CreateDocumentRequest</b> class holds the information for a request to create a document.
 *
 * @author Marcus Portmann
 */
@Schema(description = "A request to create a document")
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({"tenantId", "parentId", "definitionId", "externalReference", "data"})
@XmlRootElement(name = "CreateDocumentRequest", namespace = "https://inception.digital/operations")
@XmlType(
    name = "CreateDocumentRequest",
    namespace = "https://inception.digital/operations",
    propOrder = {"tenantId", "parentId", "definitionId", "externalReference", "data"})
@XmlAccessorType(XmlAccessType.FIELD)
@ValidCreateDocumentRequest
@SuppressWarnings({"unused", "WeakerAccess"})
public class CreateDocumentRequest implements Serializable {

  @Serial private static final long serialVersionUID = 1000000;

ADD DOCUMENT

  /** The data for the document. */
  @Schema(description = "The data for the document", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElement(name = "Data", required = true)
  @NotNull
  @Size(min = 1, max = 41943040)
  private byte[] data;

  /** The ID for the document definition the document is associated with. */
  @Schema(
      description = "The ID for the document definition the document is associated with",
      requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElement(name = "DefinitionId", required = true)
  @NotNull
  @Size(min = 1, max = 50)
  private String definitionId;

  /** The expiry date for the document. */
  @Schema(description = "The ISO 8601 format expiry date for the document")
  @JsonProperty
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
  @XmlElement(name = "ExpiryDate")
  @XmlJavaTypeAdapter(LocalDateAdapter.class)
  @XmlSchemaType(name = "date")
  @Column(name = "expiry_date")
  private LocalDate expiryDate;

  /** The external reference for the document. */
  @Schema(description = "The external reference for the document")
  @JsonProperty
  @XmlElement(name = "ExternalReference")
  @Size(max = 100)
  @Column(name = "external_reference", length = 100)
  private String externalReference;

  /** The file type for the document. */
  @Schema(
      description = "The file type for the document",
      requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElement(name = "FileType", required = true)
  @NotNull
  @Column(name = "file_type", nullable = false)
  private FileType fileType; ATTRIBUTES




  /** Constructs a new <b>CreateDocumentRequest</b>. */
  public CreateDocumentRequest() {}

  /**
   * Constructs a new <b>CreateDocumentRequest</b>.
   *
   * @param definitionId the ID for the document definition the document is associated with
   * @param data the data for the document
   */
  public CreateDocumentRequest(String definitionId, String data) {
    this.definitionId = definitionId;
    this.data = data;
  }

  /**
   * Constructs a new <b>CreateDocumentRequest</b>.
   *
   * @param parentId the ID for the parent document
   * @param definitionId the ID for the document definition the document is associated with
   * @param data the data for the document
   */
  public CreateDocumentRequest(UUID parentId, String definitionId, String data) {
    this.parentId = parentId;
    this.definitionId = definitionId;
    this.data = data;
  }

  /**
   * Returns the data for the document.
   *
   * @return the data for the document
   */
  public String getData() {
    return data;
  }

  /**
   * Returns the ID for the document definition the document is associated with.
   *
   * @return the ID for the document definition the document is associated with
   */
  public String getDefinitionId() {
    return definitionId;
  }

  /**
   * Returns the external reference for the document.
   *
   * @return the external reference for the document
   */
  public String getExternalReference() {
    return externalReference;
  }

  /**
   * Returns the ID for the parent document.
   *
   * @return the ID for the parent document
   */
  public UUID getParentId() {
    return parentId;
  }

  /**
   * Returns the ID for the tenant the document is associated with.
   *
   * @return the ID for the tenant the document is associated with
   */
  public UUID getTenantId() {
    return tenantId;
  }

  /**
   * Set the data for the document.
   *
   * @param data the data for the document
   */
  public void setData(String data) {
    this.data = data;
  }

  /**
   * Set the ID for the document definition the document is associated with.
   *
   * @param definitionId the ID for the document definition the document is associated with
   */
  public void setDefinitionId(String definitionId) {
    this.definitionId = definitionId;
  }

  /**
   * Set the external reference for the document.
   *
   * @param externalReference the external reference for the document
   */
  public void setExternalReference(String externalReference) {
    this.externalReference = externalReference;
  }

  /**
   * Set the ID for the parent document.
   *
   * @param parentId the ID for the parent document
   */
  public void setParentId(UUID parentId) {
    this.parentId = parentId;
  }

  /**
   * Set the ID for the tenant the document is associated with.
   *
   * @param tenantId the ID for the tenant the document is associated with
   */
  public void setTenantId(@NotNull UUID tenantId) {
    this.tenantId = tenantId;
  }
}


