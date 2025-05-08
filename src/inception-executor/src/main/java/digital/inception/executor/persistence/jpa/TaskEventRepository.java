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

import digital.inception.executor.model.TaskEvent;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * The {@code TaskEventRepository} interface declares the persistence for the <b>TaskEvent</b>
 * domain type.
 *
 * @author Marcus Portmann
 */
public interface TaskEventRepository extends JpaRepository<TaskEvent, UUID> {

  /**
   * Retrieve the task events for the task.
   *
   * @param taskId the ID for the task
   * @return the task events
   */
  List<TaskEvent> findByTaskIdOrderByTimestampAsc(UUID taskId);
}
