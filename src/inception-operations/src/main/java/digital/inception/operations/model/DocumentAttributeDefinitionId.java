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

/**
 * The {@code DocumentAttributeDefinitionId} class implements the ID class for the {@code
 * DocumentAttributeDefinition} class.
 *
 * @author Marcus Portmann
 */
@SuppressWarnings("unused")
public class DocumentAttributeDefinitionId implements Serializable {

  @Serial private static final long serialVersionUID = 1000000;

  /** The ID for the document definition the document attribute definition is associated with. */
  private String definitionId;

  /** The name of the document attribute. */
  private String name;

  /** Constructs a new {@code DocumentAttributeDefinitionId}. */
  public DocumentAttributeDefinitionId() {}

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

    DocumentAttributeDefinitionId other = (DocumentAttributeDefinitionId) object;

    return StringUtil.equalsIgnoreCase(definitionId, other.definitionId)
        && StringUtil.equalsIgnoreCase(name, other.name);
  }

  /**
   * Returns a hash code value for the object.
   *
   * @return a hash code value for the object
   */
  @Override
  public int hashCode() {
    return ((definitionId == null) ? 0 : definitionId.hashCode())
        + ((name == null) ? 0 : name.hashCode());
  }
}
