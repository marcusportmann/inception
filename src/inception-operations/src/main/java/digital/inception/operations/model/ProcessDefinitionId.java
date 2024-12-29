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
 * The <b>ProcessDefinitionId</b> class implements the ID class for the <b>ProcessDefinition</b>
 * class.
 *
 * @author Marcus Portmann
 */
@SuppressWarnings("unused")
public class ProcessDefinitionId implements Serializable {

  @Serial private static final long serialVersionUID = 1000000;

  /** The ID for the process definition. */
  private String id;

  /** The version of the process definition. */
  private int version;

  /** Constructs a new <b>ProcessDefinitionId</b>. */
  public ProcessDefinitionId() {}

  /**
   * Constructs a new <b>ProcessDefinitionId</b>.
   *
   * @param id the ID for the process definition
   * @param version the version of the process definition
   */
  public ProcessDefinitionId(String id, int version) {
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

    ProcessDefinitionId other = (ProcessDefinitionId) object;

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
