/*
 * Copyright 2020 Marcus Portmann
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

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlElement;

/**
 * The <code>BuildingAddress</code> class holds the information for a building address.
 *
 * @author Marcus Portmann
 */
public class BuildingAddress implements Serializable {

  private static final long serialVersionUID = 1000000;

  /** The floor for the building address. */
  @Schema(description = "The floor for the building address")
  @JsonProperty
  @XmlElement(name = "Floor")
  @Size(max = 10)
  @Pattern(message = "invalid floor", regexp = "^[\\pL\\pN-' ]*$")
  private String floor;

  /** The building name for the building address. */
  @Schema(description = "The building name for the building address", required = true)
  @JsonProperty(required = true)
  @XmlElement(name = "Name", required = true)
  @NotNull
  @Size(min = 1, max = 49)
  @Pattern(message = "invalid building name", regexp = "^[\\pL\\pN-' ]+$")
  private String name;

  /** The room number for the building address. */
  @Schema(description = "The room number for the building address")
  @JsonProperty
  @XmlElement(name = "Room")
  @Size(max = 10)
  @Pattern(message = "invalid room number", regexp = "^[\\pL\\pN-' ]*$")
  private String room;

  /** Constructs a new <code>BuildingAddress</code>. */
  public BuildingAddress() {}

  /**
   * Returns the floor for the building address.
   *
   * @return the floor for the building address
   */
  public String getFloor() {
    return floor;
  }

  /**
   * Returns the building name for the building address.
   *
   * @return the building name for the building address
   */
  public String getName() {
    return name;
  }

  /**
   * Returns the room number for the building address.
   *
   * @return the room number for the building address
   */
  public String getRoom() {
    return room;
  }

  /**
   * Set the floor for the building address.
   *
   * @param floor the floor for the building address
   */
  public void setFloor(String floor) {
    this.floor = floor;
  }

  /**
   * Set the building name for the building address.
   *
   * @param name the building name for the building address
   */
  public void setName(String name) {
    this.name = name;
  }

  /**
   * Set the room number for the building address.
   *
   * @param room the room number for the building address
   */
  public void setRoom(String room) {
    this.room = room;
  }
}
