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
 * The <b>ProcessDefinitionDocumentDefinitionId</b> class implements the ID class for the
 * <b>ProcessDefinitionDocumentDefinition</b> class.
 *
 * @author Marcus Portmann
 */
@SuppressWarnings("unused")
public class ProcessDefinitionDocumentDefinitionId implements Serializable {

  @Serial private static final long serialVersionUID = 1000000;

  /** The ID for the document definition. */
  private String documentDefinitionId;

  /** The process definition the process definition document definition is associated with. */
  private ProcessDefinition processDefinition;

  /** Constructs a new <b>ProcessDefinitionDocumentDefinitionId</b>. */
  public ProcessDefinitionDocumentDefinitionId() {}

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

    ProcessDefinitionDocumentDefinitionId other = (ProcessDefinitionDocumentDefinitionId) object;

    return Objects.equals(processDefinition, other.processDefinition)
        && Objects.equals(documentDefinitionId, other.documentDefinitionId);
  }

  /**
   * Returns a hash code value for the object.
   *
   * @return a hash code value for the object
   */
  @Override
  public int hashCode() {
    return ((processDefinition == null) ? 0 : processDefinition.hashCode())
        + ((documentDefinitionId == null) ? 0 : documentDefinitionId.hashCode());
  }
}
