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
 * The {@code WorkflowPermissionType} enumeration defines the possible workflow permission types.
 *
 * @author Marcus Portmann
 */
@Schema(description = "The workflow permission type")
@XmlEnum
@XmlType(name = "WorkflowPermissionType", namespace = "https://inception.digital/operations")
public enum WorkflowPermissionType implements CodeEnum {

  /** Cancel Workflow. */
  @XmlEnumValue("CancelWorkflow")
  CANCEL_WORKFLOW("cancel_workflow", "Cancel Workflow"),

  /** Initiate Workflow. */
  @XmlEnumValue("InitiateWorkflow")
  INITIATE_WORKFLOW("initiate_workflow", "Initiate Workflow"),

  /** Suspend Workflow. */
  @XmlEnumValue("SuspendWorkflow")
  SUSPEND_WORKFLOW("suspend_workflow", "Suspend Workflow"),

  /** Unsuspend Workflow. */
  @XmlEnumValue("UnsuspendWorkflow")
  UNSUSPEND_WORKFLOW("unsuspend_workflow", "Unsuspend Workflow"),

  /** Verify Workflow Document. */
  @XmlEnumValue("VerifyWorkflowDocument")
  VERIFY_WORKFLOW_DOCUMENT("verify_workflow_document", "Verify Workflow Document"),

  /** View Workflow. */
  @XmlEnumValue("ViewWorkflow")
  VIEW_WORKFLOW("view_workflow", "View Workflow"),

  /** Waive Workflow Document. */
  @XmlEnumValue("WaiveWorkflowDocument")
  WAIVE_WORKFLOW_DOCUMENT("waive_workflow_document", "Waive Workflow Document");

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
  @JsonValue
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
