/*
 * Copyright 2021 Marcus Portmann
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

package digital.inception.party;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

/**
 * The <b>MandateLink</b> class holds the information for a mandate link identifying an entity a
 * mandate applies to.
 *
 * @author Marcus Portmann
 */
@Schema(
    name = "MandateLink",
    description = "A mandate link identifying an entity a mandate applies to")
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({"type", "target"})
@XmlRootElement(name = "MandateLink", namespace = "http://inception.digital/party")
@XmlType(
    name = "MandateLink",
    namespace = "http://inception.digital/party",
    propOrder = {"type", "target"})
@XmlAccessorType(XmlAccessType.FIELD)
@Entity
@Table(schema = "party", name = "mandate_links")
@IdClass(MandateLinkId.class)
public class MandateLink implements Serializable {

  private static final long serialVersionUID = 1000000;

  /** The mandate the mandate link is associated with. */
  @Schema(hidden = true)
  @JsonBackReference
  @XmlTransient
  @Id
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "mandate_id")
  private Mandate mandate;

  /** The target for the mandate link. */
  @Schema(description = "The target for the mandate link", required = true)
  @JsonProperty(required = true)
  @XmlElement(name = "Target", required = true)
  @NotNull
  @Size(min = 1, max = 200)
  @Id
  @Column(name = "target", length = 200, nullable = false)
  private String target;

  /** The code for the link type. */
  @Schema(description = "The code for the link type", required = true)
  @JsonProperty(required = true)
  @XmlElement(name = "Type", required = true)
  @NotNull
  @Size(min = 1, max = 30)
  @Id
  @Column(name = "type", length = 30, nullable = false)
  private String type;

  /** Constructs a new <b>MandateLink</b>. */
  public MandateLink() {}

  /**
   * Constructs a new <b>MandateLink</b>.
   *
   * @param type the code for the link type
   * @param target the target for the mandate link
   */
  public MandateLink(String type, String target) {
    this.type = type;
    this.target = target;
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

    MandateLink other = (MandateLink) object;

    return Objects.equals(mandate, other.mandate)
        && Objects.equals(type, other.type)
        && Objects.equals(target, other.target);
  }

  /**
   * Returns the mandate the mandate link is associated with.
   *
   * @return the mandate the mandate link is associated with
   */
  @Schema(hidden = true)
  public Mandate getMandate() {
    return mandate;
  }

  /**
   * Returns the target for the mandate link.
   *
   * @return the target for the mandate link
   */
  public String getTarget() {
    return target;
  }

  /**
   * Returns the code for the link type.
   *
   * @return the code for the link type
   */
  public String getType() {
    return type;
  }

  /**
   * Returns a hash code value for the object.
   *
   * @return a hash code value for the object
   */
  @Override
  public int hashCode() {
    return (((mandate == null) || (mandate.getId() == null)) ? 0 : mandate.getId().hashCode())
        + ((type == null) ? 0 : type.hashCode())
        + ((target == null) ? 0 : target.hashCode());
  }

  /**
   * Set the mandate the mandate link is associated with.
   *
   * @param mandate the mandate the mandate link is associated with
   */
  @Schema(hidden = true)
  public void setMandate(Mandate mandate) {
    this.mandate = mandate;
  }

  /**
   * Set the target for the mandate link.
   *
   * @param target the target for the mandate link
   */
  public void setTarget(String target) {
    this.target = target;
  }

  /**
   * Set the code for the mandate link type.
   *
   * @param type the code for the mandate link type
   */
  public void setType(String type) {
    this.type = type;
  }
}
