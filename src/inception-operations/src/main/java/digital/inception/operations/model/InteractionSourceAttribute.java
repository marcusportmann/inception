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

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlTransient;
import jakarta.xml.bind.annotation.XmlType;
import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

/**
 * The {@code InteractionSourceAttribute} class holds the information for an attribute for an
 * interaction source.
 */
@Schema(description = "An attribute for an interaction source")
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({"name", "value"})
@XmlRootElement(
    name = "InteractionSourceAttribute",
    namespace = "https://inception.digital/operations")
@XmlType(
    name = "InteractionSourceAttribute",
    namespace = "https://inception.digital/operations",
    propOrder = {"name", "value"})
@XmlAccessorType(XmlAccessType.FIELD)
@Entity
@Table(name = "operations_interaction_source_attributes")
@IdClass(InteractionSourceAttributeId.class)
public class InteractionSourceAttribute implements Serializable {

  @Serial private static final long serialVersionUID = 1000000;

  /** The interaction source the interaction source attribute is associated with. */
  @Schema(hidden = true)
  @JsonBackReference("interactionSourceAttributeReference")
  @XmlTransient
  @Id
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "source_id")
  private InteractionSource interactionSource;

  /** The name of the interaction source attribute. */
  @Schema(
      description = "The name of the interaction source attribute",
      requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElement(name = "Name", required = true)
  @NotNull
  @Size(min = 1, max = 50)
  @Id
  @Column(name = "name", length = 50, nullable = false)
  private String name;

  /** The value for the interaction source attribute. */
  @Schema(
      description = "The value for the interaction source attribute",
      requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElement(name = "Value", required = true)
  @NotNull
  @Size(min = 1, max = 1000)
  @Column(name = "value", length = 1000, nullable = false)
  private String value;

  /** Constructs a new {@code InteractionSourceAttribute}. */
  public InteractionSourceAttribute() {}

  /**
   * Constructs a new {@code InteractionSourceAttribute}.
   *
   * @param name the name of the interaction source attribute
   * @param value the value for the interaction source attribute
   */
  public InteractionSourceAttribute(String name, String value) {
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

    InteractionSourceAttribute other = (InteractionSourceAttribute) object;

    return Objects.equals(interactionSource, other.interactionSource)
        && Objects.equals(name, other.name);
  }

  /**
   * Returns the interaction source the interaction source attribute is associated with.
   *
   * @return the interaction source the interaction source attribute is associated with
   */
  @Schema(hidden = true)
  public InteractionSource getInteractionSource() {
    return interactionSource;
  }

  /**
   * Returns the name of the interaction source attribute.
   *
   * @return the name of the interaction source attribute
   */
  public String getName() {
    return name;
  }

  /**
   * Returns the value for the interaction source attribute.
   *
   * @return the value for the interaction source attribute
   */
  public String getValue() {
    return value;
  }

  /**
   * Returns a hash code value for the object.
   *
   * @return a hash code value for the object
   */
  @Override
  public int hashCode() {
    return ((interactionSource == null) ? 0 : interactionSource.hashCode())
        + ((name == null) ? 0 : name.hashCode());
  }

  /**
   * Set the interaction source the interaction source attribute is associated with.
   *
   * @param interactionSource the interaction source the interaction source attribute is associated
   *     with
   */
  @Schema(hidden = true)
  public void setInteractionSource(InteractionSource interactionSource) {
    this.interactionSource = interactionSource;
  }

  /**
   * Set the name of the interaction source attribute.
   *
   * @param name the name of the interaction source attribute
   */
  public void setName(String name) {
    this.name = name;
  }

  /**
   * Set the value for the interaction source attribute.
   *
   * @param value the value for the interaction source attribute
   */
  public void setValue(String value) {
    this.value = value;
  }
}
