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

package digital.inception.error.model;

import com.fasterxml.jackson.annotation.JsonValue;
import digital.inception.core.model.CodeEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlEnumValue;
import jakarta.xml.bind.annotation.XmlType;

/**
 * The {@code ErrorReportSortBy} enumeration defines the possible methods used to sort a list of
 * error reports.
 *
 * @author Marcus Portmann
 */
@Schema(description = "The method used to sort the list of error reports")
@XmlEnum
@XmlType(name = "ErrorReportSortBy", namespace = "https://inception.digital/error")
public enum ErrorReportSortBy implements CodeEnum {
  /** Sort by created. */
  @XmlEnumValue("Created")
  CREATED("created", "Sort By Created"),

  /** Sort by who. */
  @XmlEnumValue("Who")
  WHO("who", "Sort By Who");

  private final String code;

  private final String description;

  ErrorReportSortBy(String code, String description) {
    this.code = code;
    this.description = description;
  }

  /**
   * Returns the code for the method used to sort a list of error reports.
   *
   * @return the code for the method used to sort a list of error reports
   */
  @JsonValue
  public String code() {
    return code;
  }

  /**
   * Returns the description for the method used to sort a list of error reports.
   *
   * @return the description for the method used to sort a list of error reports
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
