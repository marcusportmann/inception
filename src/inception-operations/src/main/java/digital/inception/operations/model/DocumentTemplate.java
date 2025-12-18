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
import digital.inception.core.util.StringUtil;
import digital.inception.core.xml.OffsetDateTimeAdapter;
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
import java.time.OffsetDateTime;
import java.util.UUID;

/**
 * The {@code DocumentTemplate} class holds the information for a document template.
 *
 * @author Marcus Portmann
 */
@Schema(description = "A document template")
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
  "id",
  "categoryId",
  "tenantId",
  "name",
  "description",
  "hash",
  "data",
  "created",
  "createdBy",
  "updated",
  "updatedBy"
})
@XmlRootElement(name = "DocumentTemplate", namespace = "https://inception.digital/operations")
@XmlType(
    name = "DocumentTemplate",
    namespace = "https://inception.digital/operations",
    propOrder = {
      "id",
      "categoryId",
      "tenantId",
      "name",
      "description",
      "hash",
      "data",
      "created",
      "createdBy",
      "updated",
      "updatedBy"
    })
@XmlAccessorType(XmlAccessType.FIELD)
@Entity
@Table(name = "operations_document_templates")
public class DocumentTemplate implements Serializable {

  @Serial private static final long serialVersionUID = 1000000;

  /** The ID for the document template category the document template is associated with. */
  @Schema(
      description =
          "The ID for the document template category the document template is associated with",
      requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElement(name = "CategoryId", required = true)
  @NotNull
  @Size(min = 1, max = 50)
  @Column(name = "category_id", length = 50, nullable = false)
  private String categoryId;

  /** The date and time the document template was created. */
  @Schema(
      description = "The date and time the document template was created",
      requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElement(name = "Created", required = true)
  @XmlJavaTypeAdapter(OffsetDateTimeAdapter.class)
  @XmlSchemaType(name = "dateTime")
  @NotNull
  @Column(name = "created", nullable = false)
  private OffsetDateTime created;

  /** The person or system that created the document template. */
  @Schema(
      description = "The person or system that created the document template",
      requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElement(name = "CreatedBy", required = true)
  @NotNull
  @Size(min = 1, max = 100)
  @Column(name = "created_by", length = 100, nullable = false)
  private String createdBy;

  /** The data for the document template. */
  @Schema(
      description = "The data for the document template",
      requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElement(name = "Data", required = true)
  @NotNull
  @Size(min = 1, max = 41943040)
  @Column(name = "data", length = 41943040, nullable = false)
  private byte[] data;

  /** The description for the document template. */
  @Schema(description = "The description for the document template")
  @JsonProperty
  @XmlElement(name = "Description")
  @Size(min = 1, max = 500)
  @Column(name = "description", length = 500)
  private String description;

  /** The base-64 encoded SHA-256 hash of the data for the document template. */
  @Schema(
      description = "The base-64 encoded SHA-256 hash of the data for the document template",
      requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElement(name = "Hash", required = true)
  @NotNull
  @Size(min = 1, max = 50)
  @Column(name = "hash", length = 50, nullable = false)
  private String hash;

  /** The ID for the document template. */
  @Schema(
      description = "The ID for the document template",
      requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElement(name = "Id", required = true)
  @NotNull
  @Size(min = 1, max = 50)
  @Id
  @Column(name = "id", length = 50, nullable = false)
  private String id;

  /** The name of the document template. */
  @Schema(
      description = "The name of the document template",
      requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElement(name = "Name", required = true)
  @NotNull
  @Size(min = 1, max = 100)
  @Column(name = "name", length = 100, nullable = false)
  private String name;

  /** The ID for the tenant the document template is specific to. */
  @Schema(description = "The ID for the tenant the document template is specific to")
  @JsonProperty
  @XmlElement(name = "TenantId")
  @Column(name = "tenant_id")
  private UUID tenantId;

  /** The date and time the document template was last updated. */
  @Schema(description = "The date and time the document template was last updated")
  @JsonProperty
  @XmlElement(name = "Updated")
  @XmlJavaTypeAdapter(OffsetDateTimeAdapter.class)
  @XmlSchemaType(name = "dateTime")
  @Column(name = "updated")
  private OffsetDateTime updated;

  /** The person or system that last updated the document template. */
  @Schema(description = "The person or system that last updated the document template")
  @JsonProperty
  @XmlElement(name = "UpdatedBy")
  @Size(min = 1, max = 100)
  @Column(name = "updated_by", length = 100)
  private String updatedBy;

  /** Constructs a new {@code DocumentTemplate}. */
  public DocumentTemplate() {}

  /**
   * Indicates whether some other object is "equal to" this one.
   *
   * @param object the reference object with which to compare
   * @return {@code true} if this object is the same as the object argument, otherwise {@code false}
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

    DocumentTemplate other = (DocumentTemplate) object;

    return StringUtil.equalsIgnoreCase(id, other.id);
  }

  /**
   * Returns the ID for the document template category the document template is associated with.
   *
   * @return the ID for the document template category the document template is associated with
   */
  public String getCategoryId() {
    return categoryId;
  }

  /**
   * Returns the date and time the document template was created.
   *
   * @return the date and time the document template was created
   */
  public OffsetDateTime getCreated() {
    return created;
  }

  /**
   * Returns the person or system that created the document template.
   *
   * @return the person or system that created the document template
   */
  public String getCreatedBy() {
    return createdBy;
  }

  /**
   * Returns the data for the document template.
   *
   * @return the data for the document template
   */
  public byte[] getData() {
    return data;
  }

  /**
   * Returns the description for the document template.
   *
   * @return the description for the document template
   */
  public String getDescription() {
    return description;
  }

  /**
   * Returns the base-64 encoded SHA-256 hash of the data for the document template.
   *
   * @return the base-64 encoded SHA-256 hash of the data for the document template
   */
  public String getHash() {
    return hash;
  }

  /**
   * Returns the ID for the document template.
   *
   * @return the ID for the document template
   */
  public String getId() {
    return id;
  }

  /**
   * Returns the name of the document template.
   *
   * @return the name of the document template
   */
  public String getName() {
    return name;
  }

  /**
   * Returns the ID for the tenant the document template is specific to.
   *
   * @return the ID for the tenant the document template is specific to
   */
  public UUID getTenantId() {
    return tenantId;
  }

  /**
   * Returns the date and time the document template was last updated.
   *
   * @return the date and time the document template was last updated
   */
  public OffsetDateTime getUpdated() {
    return updated;
  }

  /**
   * Returns the person or system that last updated the document template.
   *
   * @return the person or system that last updated the document template
   */
  public String getUpdatedBy() {
    return updatedBy;
  }

  /**
   * Returns a hash code value for the object.
   *
   * @return a hash code value for the object
   */
  @Override
  public int hashCode() {
    return ((id == null) ? 0 : id.hashCode());
  }

  /**
   * Sets the ID for the document template category the document template is associated with.
   *
   * @param categoryId the ID for the document template category the document template is associated
   *     with
   */
  public void setCategoryId(String categoryId) {
    this.categoryId = categoryId;
  }

  /**
   * Sets the date and time the document template was created.
   *
   * @param created the date and time the document template was created
   */
  public void setCreated(OffsetDateTime created) {
    this.created = created;
  }

  /**
   * Sets the person or system that created the document template.
   *
   * @param createdBy the person or system that created the document template
   */
  public void setCreatedBy(String createdBy) {
    this.createdBy = createdBy;
  }

  /**
   * Sets the data for the document template.
   *
   * @param data the data for the document template
   */
  public void setData(byte[] data) {
    this.data = data;
  }

  /**
   * Sets the description for the document template.
   *
   * @param description the description for the document template
   */
  public void setDescription(String description) {
    this.description = description;
  }

  /**
   * Sets the base-64 encoded SHA-256 hash of the data for the document template.
   *
   * @param hash the base-64 encoded SHA-256 hash of the data for the document template
   */
  public void setHash(String hash) {
    this.hash = hash;
  }

  /**
   * Sets the ID for the document template.
   *
   * @param id the ID for the document template
   */
  public void setId(String id) {
    this.id = id;
  }

  /**
   * Sets the name of the document template.
   *
   * @param name the name of the document template
   */
  public void setName(String name) {
    this.name = name;
  }

  /**
   * Sets the ID for the tenant the document template is specific to.
   *
   * @param tenantId the ID for the tenant the document template is specific to
   */
  public void setTenantId(UUID tenantId) {
    this.tenantId = tenantId;
  }

  /**
   * Sets the date and time the document template was last updated.
   *
   * @param updated the date and time the document template was last updated
   */
  public void setUpdated(OffsetDateTime updated) {
    this.updated = updated;
  }

  /**
   * Sets the person or system that last updated the document template.
   *
   * @param updatedBy the person or system that last updated the document template
   */
  public void setUpdatedBy(String updatedBy) {
    this.updatedBy = updatedBy;
  }
}
