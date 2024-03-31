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

package digital.inception.party;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlEnumValue;
import jakarta.xml.bind.annotation.XmlType;

/**
 * The <b>MeasurementUnit</b> enumeration defines the possible measurement units.
 *
 * @author Marcus Portmann
 */
@Schema(description = "The measurement unit")
@XmlEnum
@XmlType(name = "MeasurementUnit", namespace = "http://inception.digital/party")
public enum MeasurementUnit {
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
   * Returns the measurement unit given by the specified code value.
   *
   * @param code the code for the measurement unit
   * @return the measurement unit given by the specified code value
   */
  @JsonCreator
  public static MeasurementUnit fromCode(String code) {
    return switch (code) {
      case "metric_centimeter" -> MeasurementUnit.METRIC_CENTIMETER;
      case "metric_meter" -> MeasurementUnit.METRIC_METER;
      case "metric_kilogram" -> MeasurementUnit.METRIC_KILOGRAM;
      case "imperial_inch" -> MeasurementUnit.IMPERIAL_INCH;
      case "imperial_foot" -> MeasurementUnit.IMPERIAL_FOOT;
      case "imperial_pound" -> MeasurementUnit.IMPERIAL_POUND;
      case "customary_inch" -> MeasurementUnit.CUSTOMARY_INCH;
      case "customary_foot" -> MeasurementUnit.CUSTOMARY_FOOT;
      case "customary_pound" -> MeasurementUnit.CUSTOMARY_POUND;
      default -> throw new RuntimeException(
          "Failed to determine the measurement unit with the invalid code (" + code + ")");
    };
  }

  /**
   * Returns the code for the measurement unit.
   *
   * @return the code for the measurement unit
   */
  @JsonValue
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
