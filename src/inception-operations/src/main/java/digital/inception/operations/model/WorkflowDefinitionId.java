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

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

/**
 * The <b>WorkflowDefinitionId</b> class implements the ID class for the <b>WorkflowDefinition</b> class.
 *
 * @author Marcus Portmann
 */
@SuppressWarnings("unused")
public class WorkflowDefinitionId implements Serializable {

  @Serial private static final long serialVersionUID = 1000000;

  /** The ID for the case definition. */
  private String id;

  /** The version of the case definition. */
  private int version;

  /** Constructs a new <b>WorkflowDefinitionId</b>. */
  public WorkflowDefinitionId() {}

  /**
   * Constructs a new <b>WorkflowDefinitionId</b>.
   *
   * @param id the ID for the Workflow definition
   * @param version the version of the workflow definition
   */
  public WorkflowDefinitionId(String id, int version) {
    this.id = id;
    this.version = version;
  }

  /**
   * Indicates whether some other object is "equal to" this one.
   *
   * @param object the reference object with which to compare
   * @return <b>true</b> if this object is the same as the object argument, otherwise <b>false</b>
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

    WorkflowDefinitionId other = (WorkflowDefinitionId) object;

    return Objects.equals(id, other.id) && (version == other.version);
  }

  /**
   * Returns a hash code value for the object.
   *
   * @return a hash code value for the object
   */
  @Override
  public int hashCode() {
    return ((id == null) ? 0 : id.hashCode()) + version;
  }
}
