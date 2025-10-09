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
import jakarta.xml.bind.annotation.XmlType;
import java.io.Serial;
import java.io.Serializable;
import java.util.UUID;

/**
 * The {@code DocumentTemplate} class holds the information for a document template.
 *
 * @author Marcus Portmann
 */
@Schema(description = "A document template")
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({"id", "categoryId", "tenantId", "name", "description", "data"})
@XmlRootElement(name = "DocumentTemplate", namespace = "https://inception.digital/operations")
@XmlType(
    name = "DocumentTemplate",
    namespace = "https://inception.digital/operations",
    propOrder = {"id", "categoryId", "tenantId", "name", "description", "data"})
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

  /** Constructs a new {@code DocumentTemplate}. */
  public DocumentTemplate() {}

  /**
   * Constructs a new {@code DocumentTemplate}.
   *
   * @param id ID for the document template
   * @param categoryId the ID for the document template category the document template is associated
   *     with
   * @param tenantId ID for the tenant the document template is specific to
   * @param name name of the document template
   * @param description the description for the document template
   * @param data the data for the document template
   */
  public DocumentTemplate(
      String id, String categoryId, UUID tenantId, String name, String description, byte[] data) {
    this.id = id;
    this.categoryId = categoryId;
    this.tenantId = tenantId;
    this.name = name;
    this.description = description;
    this.data = data;
  }

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
   * Returns a hash code value for the object.
   *
   * @return a hash code value for the object
   */
  @Override
  public int hashCode() {
    return ((id == null) ? 0 : id.hashCode());
  }

  /**
   * Set the ID for the document template category the document template is associated with.
   *
   * @param categoryId the ID for the document template category the document template is associated
   *     with
   */
  public void setCategoryId(String categoryId) {
    this.categoryId = categoryId;
  }

  /**
   * Set the data for the document template.
   *
   * @param data the data for the document template
   */
  public void setData(byte[] data) {
    this.data = data;
  }

  /**
   * Set the description for the document template.
   *
   * @param description the description for the document template
   */
  public void setDescription(String description) {
    this.description = description;
  }

  /**
   * Set the ID for the document template.
   *
   * @param id the ID for the document template
   */
  public void setId(String id) {
    this.id = id;
  }

  /**
   * Set the name of the document template.
   *
   * @param name the name of the document template
   */
  public void setName(String name) {
    this.name = name;
  }

  /**
   * Set the ID for the tenant the document template is specific to.
   *
   * @param tenantId the ID for the tenant the document template is specific to
   */
  public void setTenantId(UUID tenantId) {
    this.tenantId = tenantId;
  }
}
