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
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.github.f4b6a3.uuid.UuidCreator;
import digital.inception.core.file.FileType;
import digital.inception.core.util.StringUtil;
import digital.inception.core.xml.OffsetDateTimeAdapter;
import digital.inception.operations.constraint.ValidDocument;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OrderBy;
import jakarta.persistence.Table;
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
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

/**
 * The {@code Document} class holds the information for a document.
 *
 * @author Marcus Portmann
 */
@Schema(description = "A document")
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
  "id",
  "tenantId",
  "definitionId",
  "fileType",
  "name",
  "hash",
  "sourceDocumentId",
  "attributes",
  "externalReferences",
  "data",
  "created",
  "createdBy",
  "updated",
  "updatedBy"
})
@XmlRootElement(name = "Document", namespace = "https://inception.digital/operations")
@XmlType(
    name = "Document",
    namespace = "https://inception.digital/operations",
    propOrder = {
      "id",
      "tenantId",
      "definitionId",
      "fileType",
      "name",
      "hash",
      "sourceDocumentId",
      "attributes",
      "externalReferences",
      "data",
      "created",
      "createdBy",
      "updated",
      "updatedBy"
    })
@XmlAccessorType(XmlAccessType.FIELD)
@ValidDocument
@Entity
@Table(name = "operations_documents")
@SuppressWarnings({"unused", "WeakerAccess"})
public class Document implements Serializable {

  @Serial private static final long serialVersionUID = 1000000;

  /** The attributes for the document. */
  @Schema(description = "The attributes for the document")
  @JsonProperty
  @JsonManagedReference("documentAttributeReference")
  @XmlElementWrapper(name = "Attributes")
  @XmlElement(name = "Attribute")
  @Valid
  @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
  @OrderBy("name")
  @JoinColumn(
      name = "document_id",
      referencedColumnName = "id",
      insertable = false,
      updatable = false)
  private final List<DocumentAttribute> attributes = new ArrayList<>();

  /** The external references for the document. */
  @Schema(description = "The external references for the document")
  @JsonProperty
  @JsonManagedReference("documentExternalReferenceReference")
  @XmlElementWrapper(name = "ExternalReferences")
  @XmlElement(name = "ExternalReference")
  @Valid
  @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
  @OrderBy("type")
  @JoinColumn(
      name = "object_id",
      referencedColumnName = "id",
      insertable = false,
      updatable = false)
  private final List<DocumentExternalReference> externalReferences = new ArrayList<>();

  /** The date and time the document was created. */
  @Schema(
      description = "The date and time the document was created",
      requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElement(name = "Created", required = true)
  @XmlJavaTypeAdapter(OffsetDateTimeAdapter.class)
  @XmlSchemaType(name = "dateTime")
  @NotNull
  @Column(name = "created", nullable = false)
  private OffsetDateTime created;

  /** The person or system that created the document. */
  @Schema(
      description = "The person or system that created the document",
      requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElement(name = "CreatedBy", required = true)
  @NotNull
  @Size(min = 1, max = 100)
  @Column(name = "created_by", length = 100, nullable = false)
  private String createdBy;

  /** The data for the document. */
  @Schema(description = "The data for the document", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElement(name = "Data", required = true)
  @NotNull
  @Size(min = 1, max = 41943040)
  @Column(name = "data", length = 41943040, nullable = false)
  private byte[] data;

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

  /** The file type for the document. */
  @Schema(
      description = "The file type for the document",
      requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElement(name = "FileType", required = true)
  @NotNull
  @Column(name = "file_type", nullable = false)
  private FileType fileType;

  /** The base-64 encoded SHA-256 hash of the data for the document. */
  @Schema(description = "The base-64 encoded SHA-256 hash of the data for the document")
  @JsonProperty
  @XmlElement(name = "Hash")
  @Size(min = 1, max = 50)
  @Column(name = "hash", length = 50, nullable = false)
  private String hash;

  /** The ID for the document. */
  @Schema(description = "The ID for the document", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElement(name = "Id", required = true)
  @NotNull
  @Id
  @Column(name = "id", nullable = false)
  private UUID id;

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

  /** The date and time the document was last updated. */
  @Schema(description = "The date and time the document was last updated")
  @JsonProperty
  @XmlElement(name = "Updated")
  @XmlJavaTypeAdapter(OffsetDateTimeAdapter.class)
  @XmlSchemaType(name = "dateTime")
  @Column(name = "updated")
  private OffsetDateTime updated;

  /** The person or system that last updated the document. */
  @Schema(description = "The person or system that last updated the document")
  @JsonProperty
  @XmlElement(name = "UpdatedBy")
  @Size(min = 1, max = 100)
  @Column(name = "updated_by", length = 100)
  private String updatedBy;

  /** Constructs a new {@code Document}. */
  public Document() {}

  /**
   * Constructs a new {@code Document}.
   *
   * @param definitionId the ID for the document definition the document is associated with
   */
  public Document(String definitionId) {
    this.id = UuidCreator.getTimeOrderedEpoch();
    this.definitionId = definitionId;
  }

  /**
   * Add the attribute for the document.
   *
   * @param attribute the attribute
   */
  public void addAttribute(DocumentAttribute attribute) {
    attributes.removeIf(
        existingAttribute ->
            StringUtil.equalsIgnoreCase(existingAttribute.getName(), attribute.getName()));

    attribute.setDocument(this);

    attributes.add(attribute);
  }

  /**
   * Add the external reference for the document.
   *
   * @param externalReference the external reference
   */
  public void addExternalReference(DocumentExternalReference externalReference) {
    externalReferences.removeIf(
        existingExternalReference ->
            StringUtil.equalsIgnoreCase(
                existingExternalReference.getType(), externalReference.getType()));

    externalReference.setDocument(this);

    externalReferences.add(externalReference);
  }

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

    Document other = (Document) object;

    return Objects.equals(id, other.id);
  }

  /**
   * Retrieve the attribute with the specified name for the document.
   *
   * @param name the name of the attribute
   * @return an Optional containing the attribute with the specified name for the document or an
   *     empty Optional if the attribute could not be found
   */
  public Optional<DocumentAttribute> getAttribute(String name) {
    return attributes.stream()
        .filter(attribute -> StringUtil.equalsIgnoreCase(attribute.getName(), name))
        .findFirst();
  }

  /**
   * Returns the attributes for the document.
   *
   * @return the attributes for the document
   */
  public List<DocumentAttribute> getAttributes() {
    return attributes;
  }

  /**
   * Returns the date and time the document was created.
   *
   * @return the date and time the document was created
   */
  public OffsetDateTime getCreated() {
    return created;
  }

  /**
   * Returns the person or system that created the document.
   *
   * @return the person or system that created the document
   */
  public String getCreatedBy() {
    return createdBy;
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
   * Returns the ID for the document definition the document is associated with.
   *
   * @return the ID for the document definition the document is associated with
   */
  public String getDefinitionId() {
    return definitionId;
  }

  /**
   * Retrieve the external reference with the specified type for the document.
   *
   * @param type the code for the external reference type
   * @return an Optional containing the external reference with the specified type for the document
   *     or an empty Optional if the external reference could not be found
   */
  public Optional<DocumentExternalReference> getExternalReference(String type) {
    return externalReferences.stream()
        .filter(externalReference -> StringUtil.equalsIgnoreCase(externalReference.getType(), type))
        .findFirst();
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
   * Returns the base-64 encoded SHA-256 hash of the data for the document.
   *
   * @return the base-64 encoded SHA-256 hash of the data for the document
   */
  public String getHash() {
    return hash;
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

  /**
   * Returns the date and time the document was last updated.
   *
   * @return the date and time the document was last updated
   */
  public OffsetDateTime getUpdated() {
    return updated;
  }

  /**
   * Returns the person or system that last updated the document.
   *
   * @return the person or system that last updated the document
   */
  public String getUpdatedBy() {
    return updatedBy;
  }

  /**
   * Remove the attribute with the specified name for the workflow.
   *
   * @param name the name of the attribute
   */
  public void removeAttribute(String name) {
    attributes.removeIf(
        existingAttribute -> StringUtil.equalsIgnoreCase(existingAttribute.getName(), name));
  }

  /**
   * Remove the external reference with the specified type for the document.
   *
   * @param type the code for the external reference type
   */
  public void removeExternalReference(String type) {
    externalReferences.removeIf(
        externalReference -> Objects.equals(externalReference.getType(), type));
  }

  /**
   * Set the attributes for the document.
   *
   * @param attributes the attributes for the document
   */
  public void setAttributes(List<DocumentAttribute> attributes) {
    attributes.forEach(attribute -> attribute.setDocument(this));
    this.attributes.clear();
    this.attributes.addAll(attributes);
  }

  /**
   * Set the date and time the document was created.
   *
   * @param created the date and time the document was created
   */
  public void setCreated(OffsetDateTime created) {
    this.created = created;
  }

  /**
   * Set the person or system that created the document.
   *
   * @param createdBy the person or system that created the document
   */
  public void setCreatedBy(String createdBy) {
    this.createdBy = createdBy;
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
   * Set the ID for the document definition the document is associated with.
   *
   * @param definitionId the ID for the document definition the document is associated with
   */
  public void setDefinitionId(String definitionId) {
    this.definitionId = definitionId;
  }

  /**
   * Set the external references for the document.
   *
   * @param externalReferences the external references for the document
   */
  public void setExternalReferences(List<DocumentExternalReference> externalReferences) {
    externalReferences.forEach(externalReference -> externalReference.setDocument(this));
    this.externalReferences.clear();
    this.externalReferences.addAll(externalReferences);
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
   * Set the base-64 encoded SHA-256 hash of the data for the document.
   *
   * @param hash the base-64 encoded SHA-256 hash of the data for the document
   */
  public void setHash(String hash) {
    this.hash = hash;
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

  /**
   * Set the ID for the tenant the document is associated with.
   *
   * @param tenantId the ID for the tenant the document is associated with
   */
  public void setTenantId(UUID tenantId) {
    this.tenantId = tenantId;
  }

  /**
   * Set the date and time the document was last updated.
   *
   * @param updated the date and time the document was last updated
   */
  public void setUpdated(OffsetDateTime updated) {
    this.updated = updated;
  }

  /**
   * Set the person or system that last updated the document.
   *
   * @param updatedBy the person or system that last updated the document
   */
  public void setUpdatedBy(String updatedBy) {
    this.updatedBy = updatedBy;
  }
}
