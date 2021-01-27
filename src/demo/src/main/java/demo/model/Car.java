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
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.SecondaryTable;
import javax.persistence.SecondaryTables;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

/**
 * The <code>Car</code> class.
 *
 * @author Marcus Portmann
 */
@Schema(description = "A car")
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({"id", "name", "numberOfDoors"})
@XmlRootElement(name = "Car", namespace = "http://demo")
@XmlType(
    name = "Car",
    namespace = "http://demo",
    propOrder = {"id", "name", "numberOfDoors"})
@XmlAccessorType(XmlAccessType.PROPERTY)
@Entity
@Table(schema = "demo", name = "vehicles")
@SecondaryTables({
  @SecondaryTable(
      schema = "demo",
      name = "cars",
      pkJoinColumns = @PrimaryKeyJoinColumn(name = "id", referencedColumnName = "id"))
})
public class Car extends VehicleBase implements Serializable {

  private static final long serialVersionUID = 1000000;

  /** The number of doors for the car. */
  @Schema(description = "The number of doors for the car", required = true)
  @JsonProperty(required = true)
  @XmlElement(name = "NumberOfDoors", required = true)
  @NotNull
  @Column(table = "cars", name = "number_of_doors", nullable = false)
  private int numberOfDoors;

  /** Constructs a new <code>Car</code>. */
  public Car() {
    super(VehicleType.CAR);
  }

  /**
   * Constructs a new <code>Car</code>.
   *
   * @param name the name of the car
   * @param numberOfDoors the number of doors
   */
  public Car(String name, int numberOfDoors) {
    super(VehicleType.CAR, name);

    this.numberOfDoors = numberOfDoors;
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

    Car other = (Car) object;

    return Objects.equals(getId(), other.getId());
  }

  /**
   * Returns the date and time the car was created.
   *
   * @return the date and time the car was created
   */
  @JsonIgnore
  @XmlTransient
  @Override
  public LocalDateTime getCreated() {
    return super.getCreated();
  }

  /**
   * Returns the Universally Unique Identifier (UUID) for the car.
   *
   * @return the Universally Unique Identifier (UUID) for the car
   */
  @Schema(description = "The Universally Unique Identifier (UUID) for the car", required = true)
  @JsonProperty(required = true)
  @XmlElement(name = "Id", required = true)
  @Override
  public UUID getId() {
    return super.getId();
  }

  /**
   * Returns the name of the car.
   *
   * @return the name of the car
   */
  @Schema(description = "The name of the car", required = true)
  @JsonProperty(required = true)
  @XmlElement(name = "Name", required = true)
  @Override
  public String getName() {
    return super.getName();
  }

  /**
   * Returns the number of doors for the car.
   *
   * @return the number of doors for the car
   */
  public int getNumberOfDoors() {
    return numberOfDoors;
  }

  /**
   * Returns the vehicle type for the car.
   *
   * @return the vehicle type for the car
   */
  @JsonIgnore
  @XmlTransient
  @Override
  public VehicleType getType() {
    return super.getType();
  }

  /**
   * Returns the date and time the car was last updated.
   *
   * @return the date and time the car was last updated
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
   * Set the Universally Unique Identifier (UUID) for the car.
   *
   * @param id the Universally Unique Identifier (UUID) for the car
   */
  @Override
  public void setId(UUID id) {
    super.setId(id);
  }

  /**
   * Set the name of the car.
   *
   * @param name the name of the car
   */
  @Override
  public void setName(String name) {
    super.setName(name);
  }

  /**
   * Set the number of doors for the car.
   *
   * @param numberOfDoors the number of doors for the car
   */
  public void setNumberOfDoors(int numberOfDoors) {
    this.numberOfDoors = numberOfDoors;
  }
}
