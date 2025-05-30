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

import digital.inception.core.model.CodeEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlEnumValue;
import jakarta.xml.bind.annotation.XmlType;

/**
 * The {@code MeasurementUnit} enumeration defines the possible measurement units.
 *
 * @author Marcus Portmann
 */
@Schema(description = "The measurement unit")
@XmlEnum
@XmlType(name = "MeasurementUnit", namespace = "https://inception.digital/party")
public enum MeasurementUnit implements CodeEnum {
  /** Metric centimeter. */
  @XmlEnumValue("MetricCentimeter")
  METRIC_CENTIMETER(
      "metric_centimeter",
      "Metric Centimeter",
      MeasurementSystem.METRIC,
      MeasurementUnitType.LENGTH),

  /** Metric meter. */
  @XmlEnumValue("MetricMeter")
  METRIC_METER(
      "metric_meter", "Metric Meter", MeasurementSystem.METRIC, MeasurementUnitType.LENGTH),

  /** Metric kilogram. */
  @XmlEnumValue("MetricKilogram")
  METRIC_KILOGRAM(
      "metric_kilogram", "Metric Kilogram", MeasurementSystem.METRIC, MeasurementUnitType.MASS),

  /** Imperial inch. */
  @XmlEnumValue("ImperialInch")
  IMPERIAL_INCH(
      "imperial_inch", "Imperial Inch", MeasurementSystem.IMPERIAL, MeasurementUnitType.LENGTH),

  /** Imperial foot. */
  @XmlEnumValue("ImperialFoot")
  IMPERIAL_FOOT(
      "imperial_foot", "Imperial Foot", MeasurementSystem.IMPERIAL, MeasurementUnitType.LENGTH),

  /** Imperial pound. */
  @XmlEnumValue("ImperialPound")
  IMPERIAL_POUND(
      "imperial_pound", "Imperial Pound", MeasurementSystem.IMPERIAL, MeasurementUnitType.MASS),

  /** Customary inch. */
  @XmlEnumValue("CustomaryInch")
  CUSTOMARY_INCH(
      "customary_inch", "Customary Inch", MeasurementSystem.CUSTOMARY, MeasurementUnitType.LENGTH),

  /** Customary foot. */
  @XmlEnumValue("CustomaryFoot")
  CUSTOMARY_FOOT(
      "customary_foot", "Customary Foot", MeasurementSystem.CUSTOMARY, MeasurementUnitType.LENGTH),

  /** Customary pound. */
  @XmlEnumValue("CustomaryPound")
  CUSTOMARY_POUND(
      "customary_pound", "Customary Pound", MeasurementSystem.CUSTOMARY, MeasurementUnitType.MASS);

  private final String code;

  private final String description;

  private final MeasurementSystem system;

  private final MeasurementUnitType type;

  MeasurementUnit(
      String code, String description, MeasurementSystem system, MeasurementUnitType type) {
    this.code = code;
    this.description = description;
    this.system = system;
    this.type = type;
  }

  /**
   * Returns the code for the measurement unit.
   *
   * @return the code for the measurement unit
   */
  public String code() {
    return code;
  }

  /**
   * Returns the description for the measurement unit.
   *
   * @return the description for the measurement unit
   */
  public String description() {
    return description;
  }

  /**
   * Returns the measurement system for the measurement unit.
   *
   * @return the measurement system for the measurement unit
   */
  public MeasurementSystem getSystem() {
    return system;
  }

  /**
   * Returns the measurement unit type for the measurement unit.
   *
   * @return the measurement unit type for the measurement unit
   */
  public MeasurementUnitType getType() {
    return type;
  }
}
