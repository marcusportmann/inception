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

package digital.inception.operations.model;

import com.fasterxml.jackson.annotation.JsonValue;
import digital.inception.processor.ProcessableObjectStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlEnumValue;
import jakarta.xml.bind.annotation.XmlType;

/**
 * The {@code EventStatus} enumeration defines the possible statuses for an event.
 *
 * @author Marcus Portmann
 */
@Schema(description = "The event status")
@XmlEnum
@XmlType(name = "EventStatus", namespace = "https://inception.digital/operations")
public enum EventStatus implements ProcessableObjectStatus {

  /** Queued. */
  @XmlEnumValue("Queued")
  QUEUED("queued", ProcessingPhase.PENDING, "Queued"),

  /** Processing. */
  @XmlEnumValue("Processing")
  PROCESSING("processing", ProcessingPhase.PROCESSING, "Processing"),

  /** Processed. */
  @XmlEnumValue("Processed")
  PROCESSED("processed", ProcessingPhase.COMPLETED, "Processed"),

  /** Failed. */
  @XmlEnumValue("Failed")
  FAILED("failed", ProcessingPhase.FAILED, "Failed");

  private final String code;

  private final String description;

  private final ProcessingPhase processingPhase;

  EventStatus(String code, ProcessingPhase processingPhase, String description) {
    this.code = code;
    this.processingPhase = processingPhase;
    this.description = description;
  }

  /**
   * Returns the code for the event status.
   *
   * @return the code for the event status
   */
  @JsonValue
  public String code() {
    return code;
  }

  /**
   * Returns the description for the event status.
   *
   * @return the description for the event status
   */
  public String description() {
    return description;
  }

  /**
   * Returns the processing phase for the event status.
   *
   * @return the processing phase for the event status
   */
  @Override
  public ProcessingPhase getProcessingPhase() {
    return processingPhase;
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
