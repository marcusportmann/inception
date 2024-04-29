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

package digital.inception.executor.persistence;

import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import digital.inception.executor.model.TaskStatus;
import digital.inception.executor.model.TaskSummary;

/**
 * The <b>TaskSummaryRepository</b> interface declares the persistence for the <b>TaskSummary</b>
 * domain type.
 *
 * @author Marcus Portmann
 */
public interface TaskSummaryRepository extends JpaRepository<TaskSummary, UUID> {

  /**
   * Retrieve the summaries for the tasks with the specified status.
   *
   * @param status the task status
   * @param pageable the pagination information
   * @return the summaries for the tasks with the specified status
   */
  Page<TaskSummary> findByStatus(TaskStatus status, Pageable pageable);

  /**
   * Retrieve the summaries for the tasks with the specified type.
   *
   * @param type the task type code
   * @param pageable the pagination information
   * @return the summaries for the tasks with the specified status
   */
  Page<TaskSummary> findByType(String type, Pageable pageable);

  /**
   * Retrieve the summaries for the tasks with the specified type and status.
   *
   * @param type the task type code
   * @param status the task status
   * @param pageable the pagination information
   * @return the summaries for the tasks with the specified status
   */
  Page<TaskSummary> findByTypeAndStatus(String type, TaskStatus status, Pageable pageable);

  /**
   * Retrieve the filtered task summaries.
   *
   * @param filter the filter to apply to the task summaries
   * @param pageable the pagination information
   * @return the filtered task summaries
   */
  @Query(
      "select ts from TaskSummary ts where "
          + "((lower(ts.batchId) like lower(:filter)) or "
          + "(lower(ts.externalReference) like lower(:filter)))")
  Page<TaskSummary> findFiltered(@Param("filter") String filter, Pageable pageable);

  /**
   * Retrieve the filtered summaries for the tasks with the specified status.
   *
   * @param filter the filter to apply to the task summaries
   * @param status the task status
   * @param pageable the pagination information
   * @return the filtered task summaries
   */
  @Query(
      "select ts from TaskSummary ts where "
          + "(ts.status = :status) and "
          + "((lower(ts.batchId) like lower(:filter)) or "
          + "(lower(ts.externalReference) like lower(:filter)))")
  Page<TaskSummary> findFilteredWithStatus(
      @Param("filter") String filter, @Param("status") TaskStatus status, Pageable pageable);

  /**
   * Retrieve the filtered summaries for the tasks with the specified type.
   *
   * @param filter the filter to apply to the task summaries
   * @param type the task type code
   * @param pageable the pagination information
   * @return the filtered task summaries
   */
  @Query(
      "select ts from TaskSummary ts where "
          + "(ts.type = :type) and "
          + "((lower(ts.batchId) like lower(:filter)) or "
          + "(lower(ts.externalReference) like lower(:filter)))")
  Page<TaskSummary> findFilteredWithType(
      @Param("filter") String filter, @Param("type") String type, Pageable pageable);

  /**
   * Retrieve the filtered summaries for the tasks with the specified type and status.
   *
   * @param filter the filter to apply to the task summaries
   * @param type the task type code
   * @param status the task status
   * @param pageable the pagination information
   * @return the filtered task summaries
   */
  @Query(
      "select ts from TaskSummary ts where "
          + "(ts.type = :type) and "
          + "(ts.status = :status) and "
          + "((lower(ts.batchId) like lower(:filter)) or "
          + "(lower(ts.externalReference) like lower(:filter)))")
  Page<TaskSummary> findFilteredWithTypeAndStatus(
      @Param("filter") String filter,
      @Param("type") String type,
      @Param("status") TaskStatus status,
      Pageable pageable);
}
