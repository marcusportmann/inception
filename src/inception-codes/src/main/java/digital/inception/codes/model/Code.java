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
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
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
import java.util.Objects;

/**
 * The {@code Code} class holds the information for a code.
 *
 * @author Marcus Portmann
 */
@Schema(
    description =
        "Reference data in the form of a key-value pair that is used to classify or categorize other data")
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({"id", "codeCategoryId", "name", "value"})
@XmlRootElement(name = "Code", namespace = "https://inception.digital/codes")
@XmlType(
    name = "Code",
    namespace = "https://inception.digital/codes",
    propOrder = {"id", "codeCategoryId", "name", "value"})
@XmlAccessorType(XmlAccessType.FIELD)
@Entity
@Table(name = "codes_codes")
@IdClass(CodeId.class)
public class Code implements Serializable {

  @Serial private static final long serialVersionUID = 1000000;

  /** The ID for the code category the code is associated with. */
  @Schema(
      description = "The ID for the code category the code is associated with",
      requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElement(name = "CodeCategoryId", required = true)
  @NotNull
  @Size(min = 1, max = 100)
  @Id
  @Column(name = "code_category_id", length = 100, nullable = false)
  private String codeCategoryId;

  /** The ID for the code. */
  @Schema(description = "The ID for the code", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElement(name = "Id", required = true)
  @NotNull
  @Size(min = 1, max = 100)
  @Id
  @Column(name = "id", length = 100, nullable = false)
  private String id;

  /** The name of the code. */
  @Schema(description = "The name of the code", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElement(name = "Name", required = true)
  @NotNull
  @Size(min = 1, max = 100)
  @Column(name = "name", length = 100, nullable = false)
  private String name;

  /** The value for the code. */
  @Schema(description = "The value for the code", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElement(name = "Value", required = true)
  @NotNull
  @Size(max = 2000)
  @Column(name = "value", length = 2000, nullable = false)
  private String value;

  /** Creates a new {@code Code} instance. */
  public Code() {}

  /**
   * Creates a new {@code Code} instance.
   *
   * @param codeCategoryId the ID for the code category the code is associated with
   */
  public Code(String codeCategoryId) {
    this.codeCategoryId = codeCategoryId;
  }

  /**
   * Creates a new {@code Code} instance.
   *
   * @param id the ID for the code
   * @param codeCategoryId the ID for the code category the code is associated with
   * @param name the name of the code
   * @param value the value for the code
   */
  public Code(String id, String codeCategoryId, String name, String value) {
    this.id = id;
    this.codeCategoryId = codeCategoryId;
    this.name = name;
    this.value = value;
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

    Code other = (Code) object;

    return Objects.equals(codeCategoryId, other.codeCategoryId) && Objects.equals(id, other.id);
  }

  /**
   * Returns the ID for the code category the code is associated with.
   *
   * @return the ID for the code category the code is associated with
   */
  public String getCodeCategoryId() {
    return codeCategoryId;
  }

  /**
   * Returns the ID for the code.
   *
   * @return the ID for the code
   */
  public String getId() {
    return id;
  }

  /**
   * Returns the name of the code.
   *
   * @return the name of the code
   */
  public String getName() {
    return name;
  }

  /**
   * Returns the value for the code.
   *
   * @return the value for the code
   */
  public String getValue() {
    return this.value;
  }

  /**
   * Returns a hash code value for the object.
   *
   * @return a hash code value for the object
   */
  @Override
  public int hashCode() {
    return ((codeCategoryId == null) ? 0 : codeCategoryId.hashCode())
        + ((id == null) ? 0 : id.hashCode());
  }

  /**
   * Set the ID for the code category the code is associated with.
   *
   * @param codeCategoryId the ID for the code category the code is associated with
   */
  public void setCodeCategoryId(String codeCategoryId) {
    this.codeCategoryId = codeCategoryId;
  }

  /**
   * Set the ID for the code.
   *
   * @param id the ID for the code
   */
  public void setId(String id) {
    this.id = id;
  }

  /**
   * Set the name of the code.
   *
   * @param name the name of the code
   */
  public void setName(String name) {
    this.name = name;
  }

  /**
   * Set the value for the code.
   *
   * @param value the value for the code
   */
  public void setValue(String value) {
    this.value = value;
  }
}
