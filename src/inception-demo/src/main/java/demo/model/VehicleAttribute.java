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

package demo.model;

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
 * The {@code VehicleAttribute} class holds the information for a vehicle attribute for a vehicle.
 *
 * @author Marcus Portmann
 */
@Schema(description = "A vehicle attribute for a vehicle")
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({"type", "value"})
@XmlRootElement(name = "VehicleAttribute", namespace = "https://demo")
@XmlType(
    name = "VehicleAttribute",
    namespace = "https://demo",
    propOrder = {"type", "value"})
@XmlAccessorType(XmlAccessType.FIELD)
@Entity
@Table(name = "demo_vehicle_attributes")
@IdClass(VehicleAttributeId.class)
public class VehicleAttribute implements Serializable {

  @Serial private static final long serialVersionUID = 1000000;

  /** The code for the vehicle attribute type. */
  @Schema(
      description = "The code for the vehicle attribute type",
      requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElement(name = "Type", required = true)
  @NotNull
  @Size(min = 1, max = 50)
  @Id
  @Column(name = "type", length = 50, nullable = false)
  private String type;

  /** The value for the vehicle attribute. */
  @Schema(
      description = "The value for the vehicle attribute",
      requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElement(name = "Value", required = true)
  @NotNull
  @Size(min = 1, max = 200)
  @Column(name = "value", length = 200, nullable = false)
  private String value;

  /** The vehicle the vehicle attribute is associated with. */
  @Schema(hidden = true)
  @JsonBackReference("vehicleAttributeReference")
  @XmlTransient
  @Id
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "vehicle_id")
  private VehicleBase vehicle;

  /** Constructs a new {@code VehicleAttribute}. */
  public VehicleAttribute() {}

  /**
   * Constructs a new {@code VehicleAttribute}.
   *
   * @param type the vehicle attribute type
   * @param value the value for the vehicle attribute
   */
  public VehicleAttribute(String type, String value) {
    this.type = type;
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

    VehicleAttribute other = (VehicleAttribute) object;

    return Objects.equals(vehicle, other.vehicle) && Objects.equals(type, other.type);
  }

  /**
   * Returns the code for the vehicle attribute type.
   *
   * @return the code for the vehicle attribute type
   */
  public String getType() {
    return type;
  }

  /**
   * Returns the value for the vehicle attribute.
   *
   * @return the value for the vehicle attribute
   */
  public String getValue() {
    return value;
  }

  /**
   * Returns the vehicle the vehicle attribute is associated with.
   *
   * @return the vehicle the vehicle attribute is associated with
   */
  @Schema(hidden = true)
  public VehicleBase getVehicle() {
    return vehicle;
  }

  /**
   * Returns a hash code value for the object.
   *
   * @return a hash code value for the object
   */
  @Override
  public int hashCode() {
    return (((vehicle == null) || (vehicle.getId() == null)) ? 0 : vehicle.getId().hashCode())
        + ((type == null) ? 0 : type.hashCode());
  }

  /**
   * Set the code for the vehicle attribute type.
   *
   * @param type the code for the vehicle attribute type
   */
  public void setType(String type) {
    this.type = type;
  }

  /**
   * Set the value for the vehicle attribute.
   *
   * @param value the value for the vehicle attribute
   */
  public void setValue(String value) {
    this.value = value;
  }

  /**
   * Set the vehicle the vehicle attribute is associated with.
   *
   * @param vehicle the vehicle the vehicle attribute is associated with
   */
  @Schema(hidden = true)
  public void setVehicle(VehicleBase vehicle) {
    this.vehicle = vehicle;
  }
}
