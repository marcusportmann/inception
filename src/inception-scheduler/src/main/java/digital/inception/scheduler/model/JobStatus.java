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

package digital.inception.scheduler.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlEnumValue;
import jakarta.xml.bind.annotation.XmlType;

/**
 * The enumeration giving the possible statuses for a job.
 *
 * @author Marcus Portmann
 */
@Schema(description = "The job status")
@XmlEnum
@XmlType(name = "JobStatus", namespace = "https://inception.digital/scheduler")
public enum JobStatus {
  /** Unscheduled. */
  @XmlEnumValue("Unscheduled")
  UNSCHEDULED("unscheduled", "Unscheduled"),

  /** Scheduled. */
  @XmlEnumValue("Scheduled")
  SCHEDULED("scheduled", "Scheduled"),

  /** Executing. */
  @XmlEnumValue("Executing")
  EXECUTING("executing", "Executing"),

  /** Executed. */
  @XmlEnumValue("Executed")
  EXECUTED("executed", "Executed"),

  /** Aborted. */
  @XmlEnumValue("Aborted")
  ABORTED("aborted", "Aborted"),

  /** Failed. */
  @XmlEnumValue("Failed")
  FAILED("failed", "Failed"),

  /** Once-off. */
  @XmlEnumValue("OnceOff")
  ONCE_OFF("once_off", "Once-Off");

  private final String code;

  private final String description;

  JobStatus(String code, String description) {
    this.code = code;
    this.description = description;
  }

  /**
   * Returns the job status given by the specified code value.
   *
   * @param code the code for the job status
   * @return the job status given by the specified code value
   */
  @JsonCreator
  public static JobStatus fromCode(String code) {
    switch (code) {
      case "unscheduled":
        return JobStatus.UNSCHEDULED;
      case "scheduled":
        return JobStatus.SCHEDULED;
      case "executing":
        return JobStatus.EXECUTING;
      case "executed":
        return JobStatus.EXECUTED;
      case "aborted":
        return JobStatus.ABORTED;
      case "failed":
        return JobStatus.FAILED;
      case "once_off":
        return JobStatus.ONCE_OFF;
      default:
        throw new RuntimeException(
            "Failed to determine the job status with the invalid code (" + code + ")");
    }
  }

  /**
   * Returns the code for the job status.
   *
   * @return the code for the job status
   */
  @JsonValue
  public String code() {
    return code;
  }

  /**
   * Returns the description for the job status.
   *
   * @return the description for the job status
   */
  public String description() {
    return description;
  }

  /**
   * Returns the string representation of the job status enumeration value.
   *
   * @return the string representation of the job status enumeration value
   */
  public String toString() {
    return description;
  }
}
