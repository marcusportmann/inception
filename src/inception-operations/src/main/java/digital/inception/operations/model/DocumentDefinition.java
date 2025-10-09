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
import digital.inception.operations.persistence.jpa.RequiredDocumentAttributeListAttributeConverter;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlElementWrapper;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;
import java.io.Serial;
import java.io.Serializable;
import java.util.List;
import java.util.UUID;

/**
 * The {@code DocumentDefinition} class holds the information for a document definition.
 *
 * @author Marcus Portmann
 */
@Schema(description = "A document definition")
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
  "id",
  "categoryId",
  "tenantId",
  "name",
  "description",
  "templateId",
  "requiredDocumentAttributes"
})
@XmlRootElement(name = "DocumentDefinition", namespace = "https://inception.digital/operations")
@XmlType(
    name = "DocumentDefinition",
    namespace = "https://inception.digital/operations",
    propOrder = {
      "id",
      "categoryId",
      "tenantId",
      "name",
      "description",
      "templateId",
      "requiredDocumentAttributes"
    })
@XmlAccessorType(XmlAccessType.FIELD)
@Entity
@Table(name = "operations_document_definitions")
public class DocumentDefinition implements Serializable {

  @Serial private static final long serialVersionUID = 1000000;

  /** The ID for the document definition category the document definition is associated with. */
  @Schema(
      description =
          "The ID for the document definition category the document definition is associated with",
      requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElement(name = "CategoryId", required = true)
  @NotNull
  @Size(min = 1, max = 50)
  @Column(name = "category_id", length = 50, nullable = false)
  private String categoryId;

  /** The description for the document definition. */
  @Schema(description = "The description for the document definition")
  @JsonProperty
  @XmlElement(name = "Description")
  @Size(min = 1, max = 500)
  @Column(name = "description", length = 500)
  private String description;

  /** The ID for the document definition. */
  @Schema(
      description = "The ID for the document definition",
      requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElement(name = "Id", required = true)
  @NotNull
  @Size(min = 1, max = 50)
  @Id
  @Column(name = "id", length = 50, nullable = false)
  private String id;

  /** The name of the document definition. */
  @Schema(
      description = "The name of the document definition",
      requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElement(name = "Name", required = true)
  @NotNull
  @Size(min = 1, max = 100)
  @Column(name = "name", length = 100, nullable = false)
  private String name;

  /** The required attributes for a document associated with the document definition. */
  @Schema(
      description =
          "The required attributes for a document associated with the document definition")
  @JsonProperty
  @XmlElementWrapper(name = "RequiredDocumentAttributes")
  @XmlElement(name = "RequiredDocumentAttribute")
  @Size(max = 10)
  @Convert(converter = RequiredDocumentAttributeListAttributeConverter.class)
  @Column(name = "required_document_attributes", length = 510)
  private List<RequiredDocumentAttribute> requiredDocumentAttributes;

  /** The ID for the document template for the document definition. */
  @Schema(description = "The ID for the document template for the document definition")
  @JsonProperty
  @XmlElement(name = "TemplateId")
  @Size(min = 1, max = 50)
  @Column(name = "template_id", length = 50)
  private String templateId;

  /** The ID for the tenant the document definition is specific to. */
  @Schema(description = "The ID for the tenant the document definition is specific to")
  @JsonProperty
  @XmlElement(name = "TenantId")
  @Column(name = "tenant_id")
  private UUID tenantId;

  /** Constructs a new {@code DocumentDefinition}. */
  public DocumentDefinition() {}

  /**
   * Constructs a new {@code DocumentDefinition}.
   *
   * @param id ID for the document definition
   * @param categoryId the ID for the document definition category the document definition is
   *     associated with
   * @param tenantId ID for the tenant the document definition is specific to
   * @param name name of the document definition
   * @param description the description for the document definition
   * @param templateId the ID for the document template for the document definition
   * @param requiredDocumentAttributes the required attributes for a document associated with the
   *     document definition
   */
  public DocumentDefinition(
      String id,
      String categoryId,
      UUID tenantId,
      String name,
      String description,
      String templateId,
      List<RequiredDocumentAttribute> requiredDocumentAttributes) {
    this.id = id;
    this.categoryId = categoryId;
    this.tenantId = tenantId;
    this.name = name;
    this.description = description;
    this.templateId = templateId;
    this.requiredDocumentAttributes = requiredDocumentAttributes;
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

    DocumentDefinition other = (DocumentDefinition) object;

    return StringUtil.equalsIgnoreCase(id, other.id);
  }

  /**
   * Returns the ID for the document definition category the document definition is associated with.
   *
   * @return the ID for the document definition category the document definition is associated with
   */
  public String getCategoryId() {
    return categoryId;
  }

  /**
   * Returns the description for the document definition.
   *
   * @return the description for the document definition
   */
  public String getDescription() {
    return description;
  }

  /**
   * Returns the ID for the document definition.
   *
   * @return the ID for the document definition
   */
  public String getId() {
    return id;
  }

  /**
   * Returns the name of the document definition.
   *
   * @return the name of the document definition
   */
  public String getName() {
    return name;
  }

  /**
   * Returns the required attributes for a document associated with the document definition.
   *
   * @return the required attributes for a document associated with the document definition
   */
  public List<RequiredDocumentAttribute> getRequiredDocumentAttributes() {
    return requiredDocumentAttributes;
  }

  /**
   * Returns the ID for the document template for the document definition.
   *
   * @return the ID for the document template for the document definition
   */
  public String getTemplateId() {
    return templateId;
  }

  /**
   * Returns the ID for the tenant the document definition is specific to.
   *
   * @return the ID for the tenant the document definition is specific to
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
   * Set the ID for the document definition category the document definition is associated with.
   *
   * @param categoryId the ID for the document definition category the document definition is
   *     associated with
   */
  public void setCategoryId(String categoryId) {
    this.categoryId = categoryId;
  }

  /**
   * Set the description for the document definition.
   *
   * @param description the description for the document definition
   */
  public void setDescription(String description) {
    this.description = description;
  }

  /**
   * Set the ID for the document definition.
   *
   * @param id the ID for the document definition
   */
  public void setId(String id) {
    this.id = id;
  }

  /**
   * Set the name of the document definition.
   *
   * @param name the name of the document definition
   */
  public void setName(String name) {
    this.name = name;
  }

  /**
   * Set the required attributes for a document associated with the document definition.
   *
   * @param requiredDocumentAttributes the required attributes for a document associated with the
   *     document definition
   */
  public void setRequiredDocumentAttributes(
      List<RequiredDocumentAttribute> requiredDocumentAttributes) {
    this.requiredDocumentAttributes = requiredDocumentAttributes;
  }

  /**
   * Set the ID for the document template for the document definition.
   *
   * @param templateId the ID for the document template for the document definition
   */
  public void setTemplateId(String templateId) {
    this.templateId = templateId;
  }

  /**
   * Set the ID for the tenant the document definition is specific to.
   *
   * @param tenantId the ID for the tenant the document definition is specific to
   */
  public void setTenantId(UUID tenantId) {
    this.tenantId = tenantId;
  }
}
