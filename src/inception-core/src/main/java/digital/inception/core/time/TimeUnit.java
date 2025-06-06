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

package digital.inception.core.time;

import digital.inception.core.model.CodeEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlEnumValue;
import jakarta.xml.bind.annotation.XmlType;

/**
 * The {@code TimeUnit} enumeration defines the possible time units.
 *
 * @author Marcus Portmann
 */
@Schema(description = "The time unit")
@XmlEnum
@XmlType(name = "TimeUnit", namespace = "https://inception.digital/core")
public enum TimeUnit implements CodeEnum {

  /** Nanoseconds. */
  @XmlEnumValue("Nanoseconds")
  NANOSECONDS("nanoseconds", "Nanoseconds"),

  /** Microseconds. */
  @XmlEnumValue("Microseconds")
  MICROSECONDS("microseconds", "Microseconds"),

  /** Milliseconds. */
  @XmlEnumValue("Milliseconds")
  MILLISECONDS("milliseconds", "Milliseconds"),

  /** Seconds. */
  @XmlEnumValue("Seconds")
  SECONDS("seconds", "Seconds"),

  /** Minutes. */
  @XmlEnumValue("Minutes")
  MINUTES("minutes", "Minutes"),

  /** Hours. */
  @XmlEnumValue("Hours")
  HOURS("hours", "Hours"),

  /** Days. */
  @XmlEnumValue("Days")
  DAYS("days", "Days"),

  /** Weeks. */
  @XmlEnumValue("Weeks")
  WEEKS("weeks", "Weeks"),

  /** Months. */
  @XmlEnumValue("Months")
  MONTHS("months", "Months"),

  /** Years. */
  @XmlEnumValue("Years")
  YEARS("years", "Years");

  private final String code;

  private final String description;

  TimeUnit(String code, String description) {
    this.code = code;
    this.description = description;
  }

  /**
   * Returns the code for the time unit.
   *
   * @return the code for the time unit
   */
  public String code() {
    return code;
  }

  /**
   * Returns the description for the time unit.
   *
   * @return the description for the time unit
   */
  public String description() {
    return description;
  }

  /**
   * Returns the {@code java.util.concurrent.TimeUnit} enumeration value for the time unit.
   *
   * @return the {@code java.util.concurrent.TimeUnit} enumeration value for the time unit
   */
  public java.util.concurrent.TimeUnit getTimeUnit() {
    return switch (this) {
      case NANOSECONDS -> java.util.concurrent.TimeUnit.NANOSECONDS;
      case MICROSECONDS -> java.util.concurrent.TimeUnit.MICROSECONDS;
      case MILLISECONDS -> java.util.concurrent.TimeUnit.MILLISECONDS;
      case SECONDS -> java.util.concurrent.TimeUnit.SECONDS;
      case MINUTES -> java.util.concurrent.TimeUnit.MINUTES;
      case HOURS -> java.util.concurrent.TimeUnit.HOURS;
      case DAYS -> java.util.concurrent.TimeUnit.DAYS;
      default ->
          throw new RuntimeException(
              "No java.util.concurrent.TimeUnit equivalent for the time unit with code ("
                  + code
                  + ") and description ("
                  + description
                  + ")");
    };
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
