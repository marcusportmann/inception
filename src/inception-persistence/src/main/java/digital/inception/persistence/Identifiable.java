/*
 * Copyright 2019 Marcus Portmann
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

package digital.inception.persistence;

//~--- JDK imports ------------------------------------------------------------

import java.io.Serializable;

/**
 * The <code>Identifiable</code> interface defines the interface that must be implemented by JPA
 * entity classes that have an ID that can be explicitly specified in place of an automatically
 * generated value for an identity column in the database.
 *
 * @param <T>
 *
 * @author Marcus Portmann
 */
public interface Identifiable<T extends Serializable> {

  /**
   * Returns the ID for the entity.
   *
   * @return the ID for the entity
   */
  T getId();
}
