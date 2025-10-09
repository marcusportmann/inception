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
@Schema(description = "A document template summary")
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({"id", "categoryId", "tenantId", "name", "description"})
@XmlRootElement(
    name = "DocumentTemplateSummary",
    namespace = "https://inception.digital/operations")
@XmlType(
    name = "DocumentTemplateSummary",
    namespace = "https://inception.digital/operations",
    propOrder = {"id", "categoryId", "tenantId", "name", "description"})
@XmlAccessorType(XmlAccessType.FIELD)
public class DocumentTemplateSummary implements Serializable {

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
  private String categoryId;

  /** The description for the document template. */
  @Schema(description = "The description for the document template")
  @JsonProperty
  @XmlElement(name = "Description")
  @Size(min = 1, max = 500)
  private String description;

  /** The ID for the document template. */
  @Schema(
      description = "The ID for the document template",
      requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElement(name = "Id", required = true)
  @NotNull
  @Size(min = 1, max = 50)
  private String id;

  /** The name of the document template. */
  @Schema(
      description = "The name of the document template",
      requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElement(name = "Name", required = true)
  @NotNull
  @Size(min = 1, max = 100)
  private String name;

  /** The ID for the tenant the document template is specific to. */
  @Schema(description = "The ID for the tenant the document template is specific to")
  @JsonProperty
  @XmlElement(name = "TenantId")
  private UUID tenantId;

  /** Constructs a new {@code DocumentTemplate}. */
  public DocumentTemplateSummary() {}

  /**
   * Constructs a new {@code DocumentTemplate}.
   *
   * @param id ID for the document template
   * @param categoryId the ID for the document template category the document template is associated
   *     with
   * @param tenantId ID for the tenant the document template is specific to
   * @param name name of the document template
   * @param description the description for the document template
   */
  public DocumentTemplateSummary(
      String id, String categoryId, UUID tenantId, String name, String description) {
    this.id = id;
    this.categoryId = categoryId;
    this.tenantId = tenantId;
    this.name = name;
    this.description = description;
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

    DocumentTemplateSummary other = (DocumentTemplateSummary) object;

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
}
