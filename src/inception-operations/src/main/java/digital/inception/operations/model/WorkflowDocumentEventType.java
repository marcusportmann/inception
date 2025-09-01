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

import digital.inception.core.model.CodeEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlEnumValue;
import jakarta.xml.bind.annotation.XmlType;

/**
 * The {@code WorkflowDocumentEventType} enumeration defines the possible workflow document event
 * types.
 *
 * @author Marcus Portmann
 */
@Schema(description = "The workflow document event type")
@XmlEnum
@XmlType(name = "WorkflowDocumentEventType", namespace = "https://inception.digital/operations")
public enum WorkflowDocumentEventType implements CodeEnum {

  /** Document Requested. */
  @XmlEnumValue("DocumentRequested")
  DOCUMENT_REQUESTED("document_requested", "Document Requested"),

  /** Document Provided. */
  @XmlEnumValue("DocumentProvided")
  DOCUMENT_PROVIDED("document_provided", "Document Provided"),

  /** Document Rejected. */
  @XmlEnumValue("DocumentRejected")
  DOCUMENT_REJECTED("document_rejected", "Document Rejected"),

  /** Document Verified. */
  @XmlEnumValue("Document Verified")
  DOCUMENT_VERIFIED("document_verified", "Document Verified"),

  /** Document Waived. */
  @XmlEnumValue("DocumentWaived")
  DOCUMENT_WAIVED("document_waived", "Document Waived");

  private final String code;

  private final String description;

  WorkflowDocumentEventType(String code, String description) {
    this.code = code;
    this.description = description;
  }

  /**
   * Returns the code for the workflow document event type.
   *
   * @return the code for the workflow document event type
   */
  public String code() {
    return code;
  }

  /**
   * Returns the description for the workflow document event type.
   *
   * @return the description for the workflow document event type
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
