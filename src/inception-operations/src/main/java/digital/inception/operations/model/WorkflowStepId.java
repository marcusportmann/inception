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

import digital.inception.core.util.StringUtil;
import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

/**
 * The {@code WorkflowStepId} class implements the ID class for the {@code WorkflowStep} class.
 *
 * @author Marcus Portmann
 */
@SuppressWarnings("unused")
public class WorkflowStepId implements Serializable {

  @Serial private static final long serialVersionUID = 1000000;

  /** The code for the workflow step. */
  private String code;

  /** The ID for the workflow the workflow step is associated with. */
  private UUID workflowId;

  /** Constructs a new {@code WorkflowStepId}. */
  public WorkflowStepId() {}

  /**
   * Constructs a new {@code WorkflowStepId}.
   *
   * @param workflowId the ID for the workflow the workflow step is associated with
   * @param code the code for the workflow step
   */
  public WorkflowStepId(UUID workflowId, String code) {
    this.workflowId = workflowId;
    this.code = code;
  }

  /**
   * Indicates whether some other object is "equal to" this one.
   *
   * @param object the reference object with which to compare
   * @return {@code true} if this object is the same as the object argument, otherwise {@code false}
   */
  @Override
  public boolean equals(Object object) {
    if (this == object) {
      return true;
    }

    if (object == null) {
      return false;
    }

    if (getClass() != object.getClass()) {
      return false;
    }

    WorkflowStepId other = (WorkflowStepId) object;

    return Objects.equals(workflowId, other.workflowId)
        && StringUtil.equalsIgnoreCase(code, other.code);
  }

  /**
   * Returns a hash code value for the object.
   *
   * @return a hash code value for the object
   */
  @Override
  public int hashCode() {
    return ((workflowId == null) ? 0 : workflowId.hashCode())
        + ((code == null) ? 0 : code.hashCode());
  }
}
