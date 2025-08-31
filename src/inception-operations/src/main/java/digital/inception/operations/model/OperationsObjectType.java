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
import jakarta.xml.bind.annotation.XmlType;

/**
 * The {@code OperationsObjectType} enumeration defines the possible operations object types.
 *
 * @author Marcus Portmann
 */
@Schema(description = "The operations object type")
@XmlEnum
@XmlType(name = "OperationsObjectType", namespace = "https://inception.digital/operations")
public enum OperationsObjectType implements CodeEnum {

  /** Document. */
  DOCUMENT("document", "Document"),

  /** Workflow. */
  WORKFLOW("workflow", "Workflow");

  private final String code;

  private final String description;

  OperationsObjectType(String code, String description) {
    this.code = code;
    this.description = description;
  }

  /**
   * Returns the code for the operations object type.
   *
   * @return the code for the operations object type
   */
  public String code() {
    return code;
  }

  /**
   * Returns the description for the operations object type.
   *
   * @return the description for the operations object type
   */
  public String description() {
    return description;
  }
}
