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

package digital.inception.core.sorting;

import digital.inception.core.model.CodeEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlEnumValue;
import jakarta.xml.bind.annotation.XmlType;

/**
 * The {@code SortDirection} enumeration defines the possible sort directions when retrieving
 * security related entities.
 *
 * @author Marcus Portmann
 */
@Schema(description = "The sort direction")
@XmlEnum
@XmlType(name = "SortDirection", namespace = "https://inception.digital/core")
public enum SortDirection implements CodeEnum {
  /** Sort ascending. */
  @XmlEnumValue("Ascending")
  ASCENDING("asc", "Ascending"),

  /** Sort descending. */
  @XmlEnumValue("Descending")
  DESCENDING("desc", "Descending");

  private final String code;

  private final String description;

  SortDirection(String code, String description) {
    this.code = code;
    this.description = description;
  }

  /**
   * Returns the code for the sort direction.
   *
   * @return the code for the sort direction
   */
  public String code() {
    return code;
  }

  /**
   * Returns the description for the sort direction.
   *
   * @return the description for the sort direction
   */
  public String description() {
    return description;
  }

  /**
   * Returns the string representation of the enumeration value.
   *
   * @return the string representation of the enumeration value
   */
  public String toString() {
    return description;
  }
}
