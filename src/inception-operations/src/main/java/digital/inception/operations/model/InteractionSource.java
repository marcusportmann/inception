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
import java.util.Objects;

/**
 * The <b>InteractionSource</b> class holds the information for an interaction source.
 *
 * <p>The <b>InteractionSource</b> and <b>InteractionSourceBase</b> classes are both JPA entity
 * classes mapped to the same <b>operations_interaction_sources</b> table. The
 * <b>InteractionSourceBase</b> class provides the common base class for all JPA entity classes that
 * form part of the interaction source inheritance model, e.g. <b>MailboxInteractionSource</b>.
 *
 * <p>The <b>InteractionSource</b> class provides a mechanism to retrieve the minimum amount of
 * interaction source information without executing the polymorphic query that would result from
 * retrieving the same entities using a query that specifies the <b>InteractionSourceBase</b> class
 * as the result type.
 *
 * @author Marcus Portmann
 */
@Schema(description = "An interaction source")
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({"id", "type", "name"})
@XmlRootElement(name = "InteractionSource", namespace = "https://inception.digital/operations")
@XmlType(
    name = "InteractionSource",
    namespace = "https://inception.digital/operations",
    propOrder = {"id", "type", "name"})
@XmlAccessorType(XmlAccessType.FIELD)
@Entity
@Table(name = "operations_interaction_sources")
public class InteractionSource implements Serializable {

  @Serial private static final long serialVersionUID = 1000000;

  /** The ID for the interaction source. */
  @Schema(
      description = "The ID for the interaction source",
      requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElement(name = "Id", required = true)
  @NotNull
  @Id
  @Column(name = "id", length = 50, nullable = false)
  private String id;

  /** The name of the interaction source. */
  @Schema(
      description = "The name of the interaction source",
      requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElement(name = "Name", required = true)
  @NotNull
  @Size(min = 1, max = 50)
  @Column(name = "name", length = 50, nullable = false)
  private String name;

  // TODO: ADD ROLE MAPPING LIKE GROUP IN SECURITY MODEL

  // TODO: ADD AUDIT CAPABILIGTIES For all interaction-related operations

  /** The interaction source type. */
  @Schema(description = "The interaction source type", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElement(name = "Type", required = true)
  @NotNull
  @Column(name = "type", length = 50, nullable = false)
  private InteractionSourceType type;

  /** Constructs a new <b>InteractionSource</b>. */
  public InteractionSource() {}

  /**
   * Constructs a new <b>InteractionSource</b>.
   *
   * @param id the ID for the interaction source
   * @param type the interaction source type
   * @param name the name of the interaction source
   */
  public InteractionSource(String id, InteractionSourceType type, String name) {
    this.id = id;
    this.type = type;
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

    InteractionSource other = (InteractionSource) object;

    return Objects.equals(id, other.id);
  }

  /**
   * Returns the ID for the interaction source.
   *
   * @return the ID for the interaction source
   */
  public String getId() {
    return id;
  }

  /**
   * Returns the name of the interaction source.
   *
   * @return the name of the interaction source
   */
  public String getName() {
    return name;
  }

  /**
   * Returns the interaction source type.
   *
   * @return the interaction source type
   */
  public InteractionSourceType getType() {
    return type;
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
   * Set the ID for the interaction source.
   *
   * @param id the ID for the interaction source
   */
  public void setId(String id) {
    this.id = id;
  }

  /**
   * Set the name of the interaction source.
   *
   * @param name the name of the interaction source
   */
  public void setName(String name) {
    this.name = name;
  }

  /**
   * Set the interaction source type.
   *
   * @param type the interaction source type
   */
  public void setType(InteractionSourceType type) {
    this.type = type;
  }
}
