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

import digital.inception.operations.model.ProcessDefinition;
import digital.inception.operations.model.ProcessDefinitionId;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

/**
 * The <b>ProcessDefinitionRepository</b> interface declares the persistence for the
 * <b>ProcessDefinition</b> domain type.
 *
 * @author Marcus Portmann
 */
public interface ProcessDefinitionRepository extends JpaRepository<ProcessDefinition, ProcessDefinitionId> {

  /**
   * Delete all the versions of the process definition with the specified ID.
   *
   * @param processDefinitionId the ID for the process definition
   */
  @Transactional
  @Modifying
  @Query("delete from ProcessDefinition pd where pd.id = :processDefinitionId")
  void deleteById(@Param("processDefinitionId") String processDefinitionId);

  /**
   * Returns whether a version of the process definition with the specified ID exists.
   *
   * @param processDefinitionId the ID for the process definition
   * @return <b>true</b> if a version of the process definition with the specified ID exists or
   *     <b>false</b> otherwise
   */
  @Query("select count(pd) > 0 from ProcessDefinition pd where pd.id = :processDefinitionId")
  boolean existsById(@Param("processDefinitionId") String processDefinitionId);

  /**
   * Retrieve the latest version of the process definition with the specified ID.
   *
   * @param processDefinitionId the ID for the process definition
   * @return the latest version of the process definition with the specified ID or an empty Optional if
   *     no versions of the process definition exist
   */
  @Query(
      value =
          "select * from operations_process_definitions pd where pd.id = :processDefinitionId order by pd.version desc limit 1",
      nativeQuery = true)
  Optional<ProcessDefinition> findLatestVersionById(
      @Param("processDefinitionId") String processDefinitionId);

  /**
   * Retrieve the next version of the process definition with the specified ID.
   *
   * @param processDefinitionId the ID for the process definition
   * @return the next version of the process definition with the specified ID
   */
  @Query(
      "select coalesce(max(pd.version), 0) + 1 from ProcessDefinition pd "
          + "where pd.id = :processDefinitionId")
  Integer getNextVersionById(@Param("processDefinitionId") String processDefinitionId);
}
