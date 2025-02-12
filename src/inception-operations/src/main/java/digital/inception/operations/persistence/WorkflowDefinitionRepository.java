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

package digital.inception.operations.persistence;

import digital.inception.operations.model.WorkflowDefinition;
import digital.inception.operations.model.WorkflowDefinitionId;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

/**
 * The <b>WorkflowDefinitionRepository</b> interface declares the persistence for the
 * <b>WorkflowDefinition</b> domain type.
 *
 * @author Marcus Portmann
 */
public interface WorkflowDefinitionRepository
    extends JpaRepository<WorkflowDefinition, WorkflowDefinitionId> {

  /**
   * Delete all the versions of the workflow definition with the specified ID.
   *
   * @param workflowDefinitionId the ID for the workflow definition
   */
  @Transactional
  @Modifying
  @Query("delete from WorkflowDefinition cd where cd.id = :workflowDefinitionId")
  void deleteById(@Param("workflowDefinitionId") String workflowDefinitionId);

  /**
   * Returns whether a version of the workflow definition with the specified ID exists.
   *
   * @param workflowDefinitionId the ID for the workflow definition
   * @return <b>true</b> if a version of the workflow definition with the specified ID exists or
   *     <b>false</b> otherwise
   */
  @Query("select count(cd) > 0 from WorkflowDefinition cd where cd.id = :workflowDefinitionId")
  boolean existsById(@Param("workflowDefinitionId") String workflowDefinitionId);

  /**
   * Retrieve the latest version of the workflow definition with the specified ID.
   *
   * @param workflowDefinitionId the ID for the workflow definition
   * @return the latest version of the workflow definition with the specified ID or an empty
   *     Optional if no versions of the workflow definition exist
   */
  @Query(
      value =
          "select * from operations_workflow_definitions cd where cd.id = :workflowDefinitionId order by cd.version desc limit 1",
      nativeQuery = true)
  Optional<WorkflowDefinition> findLatestVersionById(
      @Param("workflowDefinitionId") String workflowDefinitionId);

  /**
   * Retrieve the next version of the workflow definition with the specified ID.
   *
   * @param workflowDefinitionId the ID for the workflow definition
   * @return the next version of the workflow definition with the specified ID
   */
  @Query(
      "select coalesce(max(cd.version), 0) + 1 from WorkflowDefinition cd "
          + "where cd.id = :workflowDefinitionId")
  Integer getNextVersionById(@Param("workflowDefinitionId") String workflowDefinitionId);
}
