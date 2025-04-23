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

package digital.inception.executor.persistence.jpa;

import digital.inception.executor.model.TaskType;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * The <b>ErrorRepository</b> interface declares the persistence for the <b>TaskType</b> domain
 * type.
 *
 * @author Marcus Portmann
 */
public interface TaskTypeRepository extends JpaRepository<TaskType, String> {

  /**
   * Retrieve the task types ordered by name ascending.
   *
   * @return the task types ordered by name ascending
   */
  List<TaskType> findAllByOrderByNameAsc();
}
