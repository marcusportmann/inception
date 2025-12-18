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
 * The {@code WorkflowDocumentSortBy} enumeration defines the possible methods used to sort a list
 * of workflow documents.
 *
 * @author Marcus Portmann
 */
@Schema(description = "The method used to sort the list of workflow documents")
@XmlEnum
@XmlType(name = "WorkflowDocumentSortBy", namespace = "https://inception.digital/operations")
public enum WorkflowDocumentSortBy implements CodeEnum {
  /** Sort by requested. */
  @XmlEnumValue("Requested")
  REQUESTED("requested", "Sort By Requested"),

  /** Sort by requested by. */
  @XmlEnumValue("RequestedBy")
  REQUESTED_BY("requested_by", "Sort By Requested By"),

  /** Sort by provided. */
  @XmlEnumValue("Provided")
  PROVIDED("provided", "Sort By Provided"),

  /** Sort by provided by. */
  @XmlEnumValue("ProvidedBy")
  PROVIDED_BY("provided_by", "Sort By Provided By"),

  /** Sort by verified. */
  @XmlEnumValue("Verified")
  VERIFIED("verified", "Sort By Verified"),

  /** Sort by verified by. */
  @XmlEnumValue("VerifiedBy")
  VERIFIED_BY("verified_by", "Sort By Verified By");

  private final String code;

  private final String description;

  WorkflowDocumentSortBy(String code, String description) {
    this.code = code;
    this.description = description;
  }

  /**
   * Resolve the Spring Data sort property name for the method used to sort a list of workflow
   * documents.
   *
   * @param sortBy the method used to sort a list of workflow documents
   * @return the Spring Data sort property name for the method used to sort a list of workflow
   *     documents
   */
  @SuppressWarnings({"SameReturnValue"})
  public static String resolveSortByPropertyName(WorkflowDocumentSortBy sortBy) {
    if (sortBy == null) return "requested";
    return switch (sortBy) {
      case WorkflowDocumentSortBy.REQUESTED -> "requested";
      case WorkflowDocumentSortBy.REQUESTED_BY -> "requestedBy";
      case WorkflowDocumentSortBy.PROVIDED -> "provided";
      case WorkflowDocumentSortBy.PROVIDED_BY -> "providedBy";
      case WorkflowDocumentSortBy.VERIFIED -> "verified";
      case WorkflowDocumentSortBy.VERIFIED_BY -> "verifiedBy";
      default -> "requested";
    };
  }

  /**
   * Returns the code for the method used to sort a list of workflow documents.
   *
   * @return the code for the method used to sort a list of workflow documents
   */
  @JsonValue
  public String code() {
    return code;
  }

  /**
   * Returns the description for the method used to sort a list of workflow documents.
   *
   * @return the description for the method used to sort a list of workflow documents
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
