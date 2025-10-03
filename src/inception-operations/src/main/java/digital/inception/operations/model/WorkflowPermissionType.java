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
 * The {@code WorkflowPermissionType} enumeration defines the possible workflow permission types.
 *
 * @author Marcus Portmann
 */
@Schema(description = "The workflow permission type")
@XmlEnum
@XmlType(name = "WorkflowPermissionType", namespace = "https://inception.digital/operations")
public enum WorkflowPermissionType implements CodeEnum {

  /** Cancel Workflow. */
  CANCEL_WORKFLOW("cancel_workflow", "Cancel Workflow"),

  /** Initiate Workflow. */
  INITIATE_WORKFLOW("initiate_workflow", "Initiate Workflow"),

  /** Suspend Workflow. */
  SUSPEND_WORKFLOW("suspend_workflow", "Suspend Workflow"),

  /** Unsuspend Workflow. */
  UNSUSPEND_WORKFLOW("unsuspend_workflow", "Unsuspend Workflow"),

  /** View Workflow. */
  VIEW_WORKFLOW("view_workflow", "View Workflow");

  private final String code;

  private final String description;

  WorkflowPermissionType(String code, String description) {
    this.code = code;
    this.description = description;
  }

  /**
   * Returns the code for the workflow permission type.
   *
   * @return the code for the workflow permission type
   */
  public String code() {
    return code;
  }

  /**
   * Returns the description for the workflow permission type.
   *
   * @return the description for the workflow permission type
   */
  public String description() {
    return description;
  }
}
