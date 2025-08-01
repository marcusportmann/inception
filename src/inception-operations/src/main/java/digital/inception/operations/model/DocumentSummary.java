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
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import digital.inception.core.file.FileType;
import digital.inception.core.xml.LocalDateAdapter;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlSchemaType;
import jakarta.xml.bind.annotation.XmlType;
import jakarta.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;
import java.util.UUID;

/**
 * The {@code DocumentSummary} class holds the information for a document summary.
 *
 * @author Marcus Portmann
 */
@Schema(description = "A document summary")
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
  "id",
  "tenantId",
  "definitionId",
  "fileType",
  "name",
  "hash",
  "externalReference",
  "sourceDocumentId",
  "issueDate",
  "expiryDate"
})
@XmlRootElement(name = "DocumentSummary", namespace = "https://inception.digital/operations")
@XmlType(
    name = "DocumentSummary",
    namespace = "https://inception.digital/operations",
    propOrder = {
      "id",
      "tenantId",
      "definitionId",
      "fileType",
      "name",
      "hash",
      "externalReference",
      "sourceDocumentId",
      "issueDate",
      "expiryDate"
    })
@XmlAccessorType(XmlAccessType.FIELD)
@Entity
@Table(name = "operations_documents")
@SuppressWarnings({"unused", "WeakerAccess"})
public class DocumentSummary implements Serializable {

  @Serial private static final long serialVersionUID = 1000000;

  /** The ID for the document definition the document is associated with. */
  @Schema(
      description = "The ID for the document definition the document is associated with",
      requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElement(name = "DefinitionId", required = true)
  @NotNull
  @Size(min = 1, max = 50)
  @Column(name = "definition_id", length = 50, nullable = false)
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

  /** The external reference used to link this document to an external system. */
  @Schema(description = "The external reference used to link this document to an external system")
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
  private FileType fileType;

  /** The ID for the document. */
  @Schema(description = "The ID for the document", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElement(name = "Id", required = true)
  @NotNull
  @Id
  @Column(name = "id", nullable = false)
  private UUID id;

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

  /** The ID for the tenant the document is associated with. */
  @Schema(
      description = "The ID for the tenant the document is associated with",
      requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElement(name = "TenantId", required = true)
  @NotNull
  @Column(name = "tenant_id", nullable = false)
  private UUID tenantId;

  /** Constructs a new {@code DocumentSummary}. */
  public DocumentSummary() {}

  /**
   * Indicates whether some other object is "equal to" this one.
   *
   * @param object the reference object with which to compare
   * @return {@code true} if this object is the same as the object argument otherwise {@code false}
   */
  @Override
  public boolean equals(Object object) {
    if (this == object) {
      return true;
    }

    if (object == null) {
      return false;
    }

    if (getClass() != object.getClass()) {
      return false;
    }

    DocumentSummary other = (DocumentSummary) object;

    return Objects.equals(id, other.id);
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
   * Returns the expiry date for the document.
   *
   * @return the expiry date for the document
   */
  public LocalDate getExpiryDate() {
    return expiryDate;
  }

  /**
   * Returns the external reference used to link this document to an external system.
   *
   * @return the external reference used to link this document to an external system
   */
  public String getExternalReference() {
    return externalReference;
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
   * Returns the ID for the document.
   *
   * @return the ID for the document
   */
  public UUID getId() {
    return id;
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
   * Returns the ID for the tenant the document is associated with.
   *
   * @return the ID for the tenant the document is associated with
   */
  public UUID getTenantId() {
    return tenantId;
  }
}
