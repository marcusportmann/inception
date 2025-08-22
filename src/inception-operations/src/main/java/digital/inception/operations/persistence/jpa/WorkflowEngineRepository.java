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

package digital.inception.operations.persistence.jpa;

import digital.inception.operations.model.WorkflowEngine;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

/**
 * The {@code WorkflowEngineRepository} interface provides the persistence operations for the {@link
 * WorkflowEngine} domain type.
 *
 * <p>This repository extends {@code JpaRepository} to provide standard CRUD (Create, Read, Update,
 * Delete) operations.
 *
 * @author Marcus Portmann
 */
public interface WorkflowEngineRepository extends JpaRepository<WorkflowEngine, String> {

  /**
   * Returns the IDs for all workflow engines.
   *
   * @return the list of workflow engine IDs
   */
  @Transactional(readOnly = true)
  @Query("select we.id from WorkflowEngine we order by we.id")
  List<String> findAllIds();
}
