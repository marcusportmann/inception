/*
 * Copyright 2019 Marcus Portmann
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

package digital.inception.scheduler;

// ~--- non-JDK imports --------------------------------------------------------

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import io.swagger.v3.oas.annotations.media.Schema;
import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;

// ~--- JDK imports ------------------------------------------------------------

/**
 * The enumeration giving the possible statuses for a job.
 *
 * @author Marcus Portmann
 */
@Schema(description = "JobStatus")
@XmlEnum
@XmlType(name = "JobStatus", namespace = "http://scheduler.inception.digital")
public enum JobStatus {
  @XmlEnumValue("Unscheduled")
  UNSCHEDULED(0, "Unscheduled"),
  @XmlEnumValue("Scheduled")
  SCHEDULED(1, "Scheduled"),
  @XmlEnumValue("Executing")
  EXECUTING(2, "Executing"),
  @XmlEnumValue("Executed")
  EXECUTED(3, "Executed"),
  @XmlEnumValue("Aborted")
  ABORTED(4, "Aborted"),
  @XmlEnumValue("Failed")
  FAILED(5, "Failed"),
  @XmlEnumValue("OnceOff")
  ONCE_OFF(6, "Once-Off"),
  @XmlEnumValue("Unknown")
  UNKNOWN(-1, "Unknown");

  private int code;
  private String description;

  JobStatus(int code, String description) {
    this.code = code;
    this.description = description;
  }

  /**
   * Returns the status given by the specified numeric code value.
   *
   * @param code the numeric code value identifying the status
   * @return the status given by the specified numeric code value
   */
  @JsonCreator
  public static JobStatus fromCode(int code) {
    switch (code) {
      case 0:
        return JobStatus.UNSCHEDULED;

      case 1:
        return JobStatus.SCHEDULED;

      case 2:
        return JobStatus.EXECUTING;

      case 3:
        return JobStatus.EXECUTED;

      case 4:
        return JobStatus.ABORTED;

      case 5:
        return JobStatus.FAILED;

      case 6:
        return JobStatus.ONCE_OFF;

      default:
        return JobStatus.UNKNOWN;
    }
  }

  /**
   * Returns the numeric code value identifying the status.
   *
   * @return the numeric code value identifying the status
   */
  @JsonValue
  public int code() {
    return code;
  }

  /**
   * Returns the <code>String</code> value of the numeric code value identifying the status.
   *
   * @return the <code>String</code> value of the numeric code value identifying the status
   */
  public String codeAsString() {
    return String.valueOf(code);
  }

  /**
   * Returns the description for the status.
   *
   * @return the description for the status
   */
  public String description() {
    return description;
  }

  /**
   * Return the string representation of the status enumeration value.
   *
   * @return the string representation of the status enumeration value
   */
  public String toString() {
    return description;
  }
}
