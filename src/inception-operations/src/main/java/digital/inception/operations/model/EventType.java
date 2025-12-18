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
import digital.inception.core.model.CodeEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlEnumValue;
import jakarta.xml.bind.annotation.XmlType;

/**
 * The {@code EventType} enumeration defines the possible event types.
 *
 * @author Marcus Portmann
 */
@Schema(description = "The event type")
@XmlEnum
@XmlType(name = "EventType", namespace = "https://inception.digital/operations")
public enum EventType implements CodeEnum {

  /** Workflow Document Requested. */
  @XmlEnumValue("WorkflowDocumentRequested")
  WORKFLOW_DOCUMENT_REQUESTED("workflow_document_requested", "Workflow Document Requested"),

  /** Workflow Document Provided. */
  @XmlEnumValue("WorkflowDocumentProvided")
  WORKFLOW_DOCUMENT_PROVIDED("workflow_document_provided", "Workflow Document Provided"),

  /** Workflow Document Rejected. */
  @XmlEnumValue("WorkflowDocumentRejected")
  WORKFLOW_DOCUMENT_REJECTED("workflow_document_rejected", "Workflow Document Rejected"),

  /** Workflow Document Reset. */
  @XmlEnumValue("WorkflowDocumentReset")
  WORKFLOW_DOCUMENT_RESET("workflow_document_reset", "Workflow Document Reset"),

  /** Workflow Document Verified. */
  @XmlEnumValue("WorkflowDocumentVerified")
  WORKFLOW_DOCUMENT_VERIFIED("workflow_document_verified", "Workflow Document Verified"),

  /** Workflow Document Waived. */
  @XmlEnumValue("WorkflowDocumentWaived")
  WORKFLOW_DOCUMENT_WAIVED("workflow_document_waived", "Workflow Document Waived");

  private final String code;

  private final String description;

  EventType(String code, String description) {
    this.code = code;
    this.description = description;
  }

  /**
   * Returns the code for the event type.
   *
   * @return the code for the event type
   */
  @JsonValue
  public String code() {
    return code;
  }

  /**
   * Returns the description for the event type.
   *
   * @return the description for the event type
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
