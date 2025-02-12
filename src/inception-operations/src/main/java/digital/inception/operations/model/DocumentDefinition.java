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
import digital.inception.operations.persistence.RequiredDocumentAttributesConverter;
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
import java.util.Objects;
import java.util.UUID;

/**
 * The <b>DocumentDefinition</b> class holds the information for document definition.
 *
 * @author Marcus Portmann
 */
@Schema(description = "A document definition")
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({"id", "tenantId", "name", "requiredDocumentAttributes"})
@XmlRootElement(name = "DocumentDefinition", namespace = "https://inception.digital/operations")
@XmlType(
    name = "DocumentDefinition",
    namespace = "https://inception.digital/operations",
    propOrder = {"id", "tenantId", "name", "requiredDocumentAttributes"})
@XmlAccessorType(XmlAccessType.FIELD)
@Entity
@Table(name = "operations_document_definitions")
public class DocumentDefinition implements Serializable {

  @Serial private static final long serialVersionUID = 1000000;

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
          "The required attributes for a document associated with the document definition",
      requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElementWrapper(name = "RequiredDocumentAttributes", required = true)
  @XmlElement(name = "RequiredDocumentAttribute", required = true)
  @NotNull
  @Size(max = 10)
  @Convert(converter = RequiredDocumentAttributesConverter.class)
  @Column(name = "required_document_attributes", length = 510)
  private List<RequiredDocumentAttribute> requiredDocumentAttributes;

  /** The ID for the tenant the document definition is specific to. */
  @Schema(description = "The ID for the tenant the document definition is specific to")
  @JsonProperty
  @XmlElement(name = "TenantId")
  @Column(name = "tenant_id")
  private UUID tenantId;

  /** Constructs a new <b>DocumentDefinition</b>. */
  public DocumentDefinition() {}

  /**
   * Constructs a new <b>DocumentDefinition</b>.
   *
   * @param id ID for the document definition
   * @param name the name of the document definition
   */
  public DocumentDefinition(String id, String name) {
    this.id = id;
    this.name = name;
  }

  /**
   * Constructs a new <b>DocumentDefinition</b>.
   *
   * @param id ID for the document definition
   * @param tenantId the ID for the tenant the document definition is specific to
   * @param name the name of the document definition
   */
  public DocumentDefinition(String id, UUID tenantId, String name) {
    this.id = id;
    this.tenantId = tenantId;
    this.name = name;
  }

  /**
   * Indicates whether some other object is "equal to" this one.
   *
   * @param object the reference object with which to compare
   * @return <b>true</b> if this object is the same as the object argument, otherwise <b>false</b>
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

    return Objects.equals(id, other.id);
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
   * Set the ID for the tenant the document definition is specific to.
   *
   * @param tenantId the ID for the tenant the document definition is specific to
   */
  public void setTenantId(UUID tenantId) {
    this.tenantId = tenantId;
  }
}
