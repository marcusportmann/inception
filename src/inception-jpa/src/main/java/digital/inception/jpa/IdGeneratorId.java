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

package digital.inception.jpa;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import org.hibernate.annotations.IdGeneratorType;

/**
 * Marks an entity identifier as "assigned-or-generated" using the {@code IdGenerator}.
 *
 * <p>If the id field already has a value, that value is used. Otherwise, an id is allocated from
 * IdGenerator.nextId(name).
 *
 * @author Marcus Portmann
 */
@IdGeneratorType(IdGeneratorIdHibernateGenerator.class)
@Target({FIELD, METHOD})
@Retention(RUNTIME)
public @interface IdGeneratorId {
  /**
   * The logical key used by the IdGenerator (e.g. "Member", "Policy", etc.).
   *
   * @return the logical key used by the IdGenerator
   */
  String name();
}
