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

package digital.inception.party.model;

import com.fasterxml.jackson.annotation.JsonValue;
import digital.inception.core.model.CodeEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlEnumValue;
import jakarta.xml.bind.annotation.XmlType;

/**
 * The {@code MeasurementSystem} enumeration defines the measurement systems.
 *
 * <p>A system of measurement is a collection of units of measurement and rules relating them to
 * each other. Systems of measurement have historically been important, regulated and defined for
 * the purposes of science and commerce. Systems of measurement in use include the International
 * System of Units (SI), the modern form of the metric system, the British imperial system, and the
 * United States customary system.
 *
 * @author Marcus Portmann
 */
@Schema(description = "A system of measurement")
@XmlEnum
@XmlType(name = "MeasurementSystem", namespace = "https://inception.digital/party")
public enum MeasurementSystem implements CodeEnum {
  /** Customary. */
  @XmlEnumValue("Customary")
  CUSTOMARY("customary", "United States Customary"),

  /** Imperial. */
  @XmlEnumValue("Imperial")
  IMPERIAL("imperial", "British Imperial"),

  /** Metric. */
  @XmlEnumValue("Metric")
  METRIC("metric", "Metric");

  private final String code;

  private final String description;

  MeasurementSystem(String code, String description) {
    this.code = code;
    this.description = description;
  }

  /**
   * Returns the code for the measurement system.
   *
   * @return the code for the measurement system
   */
  @JsonValue
  public String code() {
    return code;
  }

  /**
   * Returns the description for the measurement system.
   *
   * @return the description for the measurement system
   */
  public String description() {
    return description;
  }
}
