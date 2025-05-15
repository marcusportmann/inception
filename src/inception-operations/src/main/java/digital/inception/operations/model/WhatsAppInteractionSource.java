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

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
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
 * The {@code WhatsAppInteractionSource} class holds the information for a WhatsApp interaction
 * source.
 */
@Schema(description = "A WhatsApp interaction source")
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties({"type"})
@JsonPropertyOrder({"id", "tenantId", "name"})
@XmlRootElement(
    name = "WhatsAppInteractionSource",
    namespace = "https://inception.digital/operations")
@XmlType(
    name = "WhatsAppInteractionSource",
    namespace = "https://inception.digital/operations",
    propOrder = {"id", "tenantId", "name"})
@XmlAccessorType(XmlAccessType.PROPERTY)
@Entity
@Table(name = "operations_whatsapp_interaction_sources")
public class WhatsAppInteractionSource extends InteractionSourceBase implements Serializable {

  @Serial private static final long serialVersionUID = 1000000;

  ADD TENANT ID

  /** Constructs a new {@code WhatsAppInteractionSource}. */
  public WhatsAppInteractionSource() {
    super(InteractionSourceType.MAILBOX);
  }

  /**
   * Constructs a new {@code WhatsAppInteractionSource}.
   *
   * @param id the ID for the WhatsApp interaction source
   * @param name the name of the WhatsApp interaction source
   */
  public WhatsAppInteractionSource(String id, String name) {
    super(id, InteractionSourceType.MAILBOX, name);
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

    WhatsAppInteractionSource other = (WhatsAppInteractionSource) object;

    return Objects.equals(getId(), other.getId());
  }

  /**
   * Returns the ID for the WhatsApp interaction source.
   *
   * @return the ID for the WhatsApp interaction source
   */
  @Schema(
      description = "The ID for the WhatsApp interaction source",
      requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElement(name = "Id", required = true)
  @Override
  public String getId() {
    return super.getId();
  }

  /**
   * Returns the name of the WhatsApp interaction source.
   *
   * @return the name of the WhatsApp interaction source
   */
  @Schema(description = "The name of the WhatsApp interaction source")
  @JsonProperty(required = true)
  @XmlElement(name = "Name", required = true)
  @Override
  public String getName() {
    return super.getName();
  }

  /**
   * Returns the interaction source type for the WhatsApp interaction source.
   *
   * @return the interaction source type for the WhatsApp interaction source
   */
  @JsonIgnore
  @XmlTransient
  @Override
  public InteractionSourceType getType() {
    return super.getType();
  }

  /**
   * Returns a hash code value for the object.
   *
   * @return a hash code value for the object
   */
  @Override
  public int hashCode() {
    return ((getId() == null) ? 0 : getId().hashCode());
  }

  /**
   * Set the ID for the WhatsApp interaction source.
   *
   * @param id the ID for the WhatsApp interaction source
   */
  @Override
  public void setId(String id) {
    super.setId(id);
  }

  /**
   * Set the name of the WhatsApp interaction source.
   *
   * @param name the name of the WhatsApp interaction source
   */
  @Override
  public void setName(String name) {
    super.setName(name);
  }
}
