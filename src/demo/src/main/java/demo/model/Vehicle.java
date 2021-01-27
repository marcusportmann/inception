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

package demo.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

/**
 * The <code>Vehicle</code> class.
 *
 * @author Marcus Portmann
 */
@Schema(description = "A vehicle")
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({"id", "name", "type"})
@XmlRootElement(name = "Vehicle", namespace = "http://demo")
@XmlType(
    name = "Vehicle",
    namespace = "http://demo",
    propOrder = {"id", "name", "type"})
@XmlAccessorType(XmlAccessType.PROPERTY)
@Entity
@Table(schema = "demo", name = "vehicles")
public class Vehicle extends VehicleBase implements Serializable {

  private static final long serialVersionUID = 1000000;

  /** Constructs a new <code>Vehicle</code>. */
  public Vehicle() {}

  /**
   * Constructs a new <code>Vehicle</code>.
   *
   * @param type the vehicle type
   */
  public Vehicle(VehicleType type) {
    super(type);
  }

  /**
   * Constructs a new <code>Vehicle</code>.
   *
   * @param type the vehicle type
   * @param name the name of the vehicle
   */
  public Vehicle(VehicleType type, String name) {
    super(type, name);
  }

  /**
   * Indicates whether some other object is "equal to" this one.
   *
   * @param object the reference object with which to compare
   * @return <code>true</code> if this object is the same as the object argument otherwise <code>
   * false</code>
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

    Vehicle other = (Vehicle) object;

    return Objects.equals(getId(), other.getId());
  }

  /**
   * Returns the date and time the vehicle was created.
   *
   * @return the date and time the vehicle was created
   */
  @JsonIgnore
  @XmlTransient
  @Override
  public LocalDateTime getCreated() {
    return super.getCreated();
  }

  /**
   * Returns the Universally Unique Identifier (UUID) for the vehicle.
   *
   * @return the Universally Unique Identifier (UUID) for the vehicle
   */
  @Schema(description = "The Universally Unique Identifier (UUID) for the vehicle", required = true)
  @JsonProperty(required = true)
  @XmlElement(name = "Id", required = true)
  @Override
  public UUID getId() {
    return super.getId();
  }

  /**
   * Returns the name of the vehicle.
   *
   * @return the name of the vehicle
   */
  @Schema(description = "The name of the vehicle", required = true)
  @JsonProperty(required = true)
  @XmlElement(name = "Name", required = true)
  @Override
  public String getName() {
    return super.getName();
  }

  /**
   * Returns the vehicle type.
   *
   * @return the vehicle type
   */
  @Schema(description = "The vehicle type", required = true)
  @JsonProperty(required = true)
  @XmlElement(name = "Type", required = true)
  @Override
  public VehicleType getType() {
    return super.getType();
  }

  /**
   * Returns the date and time the vehicle was last updated.
   *
   * @return the date and time the vehicle was last updated
   */
  @JsonIgnore
  @XmlTransient
  @Override
  public LocalDateTime getUpdated() {
    return super.getUpdated();
  }

  /**
   * Returns a hash code value for the object.
   *
   * @return a hash code value for the object
   */
  @Override
  public int hashCode() {
    return ((super.getId() == null) ? 0 : super.getId().hashCode());
  }

  /**
   * Set the Universally Unique Identifier (UUID) for the vehicle.
   *
   * @param id the Universally Unique Identifier (UUID) for the vehicle
   */
  @Override
  public void setId(UUID id) {
    super.setId(id);
  }

  /**
   * Set the name of the vehicle.
   *
   * @param name the name of the vehicle
   */
  @Override
  public void setName(String name) {
    super.setName(name);
  }

  /**
   * Set the vehicle type.
   *
   * @param type the vehicle type
   */
  @Override
  public void setType(VehicleType type) {
    super.setType(type);
  }
}
