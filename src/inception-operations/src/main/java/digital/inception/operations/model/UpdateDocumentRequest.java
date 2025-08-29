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

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import digital.inception.core.file.FileType;
import digital.inception.core.xml.LocalDateAdapter;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Id;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlElementWrapper;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlSchemaType;
import jakarta.xml.bind.annotation.XmlType;
import jakarta.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

/**
 * The {@code UpdateDocumentRequest} class represents a request to update a document.
 *
 * @author Marcus Portmann
 */
@Schema(description = "A request to update a document")
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
  "documentId",
  "fileType",
  "name",
  "sourceDocumentId",
  "issueDate",
  "expiryDate",
  "externalReferences",
  "attributes",
  "data"
})
@XmlRootElement(name = "UpdateDocumentRequest", namespace = "https://inception.digital/operations")
@XmlType(
    name = "UpdateDocumentRequest",
    namespace = "https://inception.digital/operations",
    propOrder = {
      "documentId",
      "fileType",
      "name",
      "sourceDocumentId",
      "issueDate",
      "expiryDate",
      "externalReferences",
      "attributes",
      "data"
    })
@XmlAccessorType(XmlAccessType.FIELD)
@SuppressWarnings({"unused", "WeakerAccess"})
public class UpdateDocumentRequest implements Serializable {

  @Serial private static final long serialVersionUID = 1000000;

  /** The attributes for the document. */
  @Schema(description = "The attributes for the document")
  @JsonProperty
  @XmlElementWrapper(name = "Attributes")
  @XmlElement(name = "Attribute")
  @Valid
  private List<DocumentAttribute> attributes;

  /** The data for the document. */
  @Schema(description = "The data for the document", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElement(name = "Data", required = true)
  @NotNull
  @Size(min = 1, max = 41943040)
  private byte[] data;

  /** The ID for the document. */
  @Schema(description = "The ID for the document", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElement(name = "DocumentId", required = true)
  @NotNull
  @Id
  private UUID documentId;

  /** The expiry date for the document. */
  @Schema(description = "The ISO 8601 format expiry date for the document")
  @JsonProperty
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
  @XmlElement(name = "ExpiryDate")
  @XmlJavaTypeAdapter(LocalDateAdapter.class)
  @XmlSchemaType(name = "date")
  private LocalDate expiryDate;

  /** The external references for the document. */
  @Schema(description = "The external references for the document")
  @JsonProperty
  @XmlElementWrapper(name = "ExternalReferences")
  @XmlElement(name = "ExternalReference")
  @Valid
  private List<DocumentExternalReference> externalReferences;

  /** The file type for the document. */
  @Schema(
      description = "The file type for the document",
      requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElement(name = "FileType", required = true)
  @NotNull
  private FileType fileType;

  /** The issue date for the document. */
  @Schema(description = "The ISO 8601 format issue date for the document")
  @JsonProperty
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
  @XmlElement(name = "IssueDate")
  @XmlJavaTypeAdapter(LocalDateAdapter.class)
  @XmlSchemaType(name = "date")
  @Column(name = "issue_date")
  private LocalDate issueDate;

  /** The name of the document. */
  @Schema(description = "The name of the document", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElement(name = "Name", required = true)
  @NotNull
  @Size(min = 1, max = 200)
  @Column(name = "name", length = 200, nullable = false)
  private String name;

  /** The ID for the source document that was split to create this document. */
  @Schema(description = "The ID for the source document that was split to create this document")
  @JsonProperty
  @XmlElement(name = "SourceDocumentId")
  @Column(name = "source_document_id")
  private UUID sourceDocumentId;

  /** Constructs a new {@code UpdateDocumentRequest}. */
  public UpdateDocumentRequest() {}

  /**
   * Returns the attributes for the document.
   *
   * @return the attributes for the document
   */
  public List<DocumentAttribute> getAttributes() {
    return attributes;
  }

  /**
   * Returns the data for the document.
   *
   * @return the data for the document
   */
  public byte[] getData() {
    return data;
  }

  /**
   * Returns the ID for the document.
   *
   * @return the ID for the document
   */
  public UUID getDocumentId() {
    return documentId;
  }

  /**
   * Returns the expiry date for the document.
   *
   * @return the expiry date for the document
   */
  public LocalDate getExpiryDate() {
    return expiryDate;
  }

  /**
   * Returns the external references for the document.
   *
   * @return the external references for the document
   */
  public List<DocumentExternalReference> getExternalReferences() {
    return externalReferences;
  }

  /**
   * Returns the file type for the document.
   *
   * @return the file type for the document
   */
  public FileType getFileType() {
    return fileType;
  }

  /**
   * Returns the issue date for the document.
   *
   * @return the issue date for the document
   */
  public LocalDate getIssueDate() {
    return issueDate;
  }

  /**
   * Returns the name of the document.
   *
   * @return the name of the document
   */
  public String getName() {
    return name;
  }

  /**
   * Returns the ID for the source document that was split to create this document.
   *
   * @return the ID for the source document that was split to create this document
   */
  public UUID getSourceDocumentId() {
    return sourceDocumentId;
  }

  /**
   * Set the attributes for the document.
   *
   * @param attributes the attributes for the document
   */
  public void setAttributes(List<DocumentAttribute> attributes) {
    this.attributes = attributes;
  }

  /**
   * Set the data for the document.
   *
   * @param data the data for the document
   */
  public void setData(byte[] data) {
    this.data = data;
  }

  /**
   * Set the ID for the document.
   *
   * @param documentId the ID for the document
   */
  public void setDocumentId(UUID documentId) {
    this.documentId = documentId;
  }

  /**
   * Set the expiry date for the document.
   *
   * @param expiryDate the expiry date for the document
   */
  public void setExpiryDate(LocalDate expiryDate) {
    this.expiryDate = expiryDate;
  }

  /**
   * Set the external references for the document.
   *
   * @param externalReferences the external references for the document
   */
  public void setExternalReferences(List<DocumentExternalReference> externalReferences) {
    this.externalReferences = externalReferences;
  }

  /**
   * Set the file type for the document.
   *
   * @param fileType the file type for the document
   */
  public void setFileType(FileType fileType) {
    this.fileType = fileType;
  }

  /**
   * Set the issue date for the document.
   *
   * @param issueDate the issue date for the document
   */
  public void setIssueDate(LocalDate issueDate) {
    this.issueDate = issueDate;
  }

  /**
   * Set the name of the document.
   *
   * @param name the name of the document
   */
  public void setName(String name) {
    this.name = name;
  }

  /**
   * Set the ID for the source document that was split to create this document.
   *
   * @param sourceDocumentId the ID for the source document that was split to create this document
   */
  public void setSourceDocumentId(UUID sourceDocumentId) {
    this.sourceDocumentId = sourceDocumentId;
  }
}
