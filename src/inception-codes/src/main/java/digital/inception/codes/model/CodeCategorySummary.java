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

package digital.inception.codes.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
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
import java.util.Objects;

/**
 * The <b>CodeCategorySummary</b> class holds the summary information for a code category.
 *
 * @author Marcus Portmann
 */
@Schema(description = "A code category summary")
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({"id", "name", "lastModified"})
@XmlRootElement(name = "CodeCategorySummary", namespace = "http://inception.digital/codes")
@XmlType(
    name = "CodeCategorySummary",
    namespace = "http://inception.digital/codes",
    propOrder = {"id", "name", "lastModified"})
@XmlAccessorType(XmlAccessType.FIELD)
@Entity
@Table(name = "codes_code_categories")
public class CodeCategorySummary implements Serializable {

  @Serial private static final long serialVersionUID = 1000000;

  /** The ID for the code category. */
  @Schema(description = "The ID for the code category", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElement(name = "Id", required = true)
  @NotNull
  @Size(min = 1, max = 100)
  @Id
  @Column(name = "id", length = 100, nullable = false)
  private String id;

  /** The date and time the code category was last modified. */
  @Schema(description = "The date and time the code category was last modified")
  @JsonProperty
  @XmlElement(name = "LastModified")
  @XmlJavaTypeAdapter(OffsetDateTimeAdapter.class)
  @XmlSchemaType(name = "dateTime")
  @Column(name = "last_modified")
  private OffsetDateTime lastModified;

  /** The name of the code category. */
  @Schema(
      description = "The name of the code category",
      requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElement(name = "Name", required = true)
  @NotNull
  @Size(min = 1, max = 100)
  @Column(name = "name", length = 100, nullable = false)
  private String name;

  /** Constructs a new <b>CodeCategorySummary</b>. */
  public CodeCategorySummary() {}

  /**
   * Constructs a new <b>CodeCategorySummary</b>.
   *
   * @param id the ID for the code category
   * @param name the name of the code category
   * @param lastModified the date and time the code category was last modified
   */
  public CodeCategorySummary(String id, String name, OffsetDateTime lastModified) {
    this.id = id;
    this.name = name;
    this.lastModified = lastModified;
  }

  /**
   * Indicates whether some other object is "equal to" this one.
   *
   * @param object the reference object with which to compare
   * @return <b>true</b> if this object is the same as the object argument otherwise <b>false</b>
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

    CodeCategorySummary other = (CodeCategorySummary) object;

    return Objects.equals(id, other.id);
  }

  /**
   * Returns the ID for the code category.
   *
   * @return the ID for the code category
   */
  public String getId() {
    return id;
  }

  /**
   * Returns the date and time the code category was last modified.
   *
   * @return the date and time the code category was last modified
   */
  public OffsetDateTime getLastModified() {
    return lastModified;
  }

  /**
   * Returns the name of the code category.
   *
   * @return the name of the code category
   */
  public String getName() {
    return name;
  }

  /**
   * Returns a hash code value for the object.
   *
   * @return a hash code value for the object
   */
  @Override
  public int hashCode() {
    return (id == null) ? 0 : id.hashCode();
  }
}
