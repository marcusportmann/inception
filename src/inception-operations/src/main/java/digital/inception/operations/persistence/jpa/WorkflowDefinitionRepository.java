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

import digital.inception.operations.model.WorkflowDefinition;
import digital.inception.operations.model.WorkflowDefinitionId;
import digital.inception.operations.model.WorkflowDefinitionPermission;
import digital.inception.operations.model.WorkflowStepDefinition;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

/**
 * The {@code WorkflowDefinitionRepository} interface declares the persistence for the {@code
 * WorkflowDefinition} domain type.
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
  @Modifying(clearAutomatically = true, flushAutomatically = true)
  @Query("delete from WorkflowDefinition wd where wd.id = :workflowDefinitionId")
  void deleteById(@Param("workflowDefinitionId") String workflowDefinitionId);

  /**
   * Returns whether a workflow definition with the specified ID exists and is associated with the
   * workflow definition category with the specified ID.
   *
   * @param categoryId the ID for the workflow definition category
   * @param id the ID for the workflow definition
   * @return {@code true} if a workflow definition with the specified ID exists and is associated
   *     with the workflow definition category with the specified ID or {@code false} otherwise
   */
  boolean existsByCategoryIdAndId(String categoryId, String id);

  /**
   * Returns whether a version of the workflow definition with the specified ID exists.
   *
   * @param workflowDefinitionId the ID for the workflow definition
   * @return {@code true} if a version of the workflow definition with the specified ID exists or
   *     {@code false} otherwise
   */
  @Query("select count(wd) > 0 from WorkflowDefinition wd where wd.id = :workflowDefinitionId")
  boolean existsById(@Param("workflowDefinitionId") String workflowDefinitionId);

  /**
   * Returns whether a version of the workflow definition with the specified ID and version exists.
   *
   * @param workflowDefinitionId the ID for the workflow definition
   * @param workflowDefinitionVersion the version of the workflow definition
   * @return {@code true} if the version of the workflow definition with the specified ID exists or
   *     {@code false} otherwise
   */
  @Query(
      "select count(wd) > 0 from WorkflowDefinition wd "
          + "where wd.id = :workflowDefinitionId and wd.version = :workflowDefinitionVersion")
  boolean existsByIdAndVersion(
      @Param("workflowDefinitionId") String workflowDefinitionId,
      @Param("workflowDefinitionVersion") int workflowDefinitionVersion);

  /**
   * Returns the latest versions of all the workflow definitions that are associated with the
   * workflow definition category with the specified ID and either
   *
   * <ul>
   *   <li>specific to the given tenant ({@code tenantId} matches), or
   *   <li>global (the workflow definition’s {@code tenantId} is {@code null}).
   * </ul>
   *
   * @param categoryId the ID for the workflow definition category the workflow definitions are
   *     associated with
   * @param tenantId the tenant identifier to match (may not be {@code null})
   * @return the matching workflow definitions, ordered by ID
   */
  @Query(
      """
       select wd
         from WorkflowDefinition wd
        where wd.categoryId = :categoryId
          and (wd.tenantId = :tenantId or wd.tenantId is null)
          and wd.version = (
                select max(wd2.version)
                  from WorkflowDefinition wd2
                 where wd2.id        = wd.id
              )
        order by wd.id
       """)
  List<WorkflowDefinition> findForCategoryAndTenantOrGlobal(
      @Param("categoryId") String categoryId, @Param("tenantId") UUID tenantId);

  /**
   * Retrieve the latest version of the workflow definition with the specified ID.
   *
   * @param workflowDefinitionId the ID for the workflow definition
   * @return the latest version of the workflow definition with the specified ID or an empty
   *     Optional if no versions of the workflow definition exist
   */
  @Query(
      value =
          "select * from operations_workflow_definitions wd where wd.id = :workflowDefinitionId order by wd.version desc limit 1",
      nativeQuery = true)
  Optional<WorkflowDefinition> findLatestVersionById(
      @Param("workflowDefinitionId") String workflowDefinitionId);

  /**
   * Retrieve the workflow definition permissions for the workflow definition with the specified ID
   * and version.
   *
   * @param workflowDefinitionId the ID for the workflow definition
   * @param workflowDefinitionVersion the version of the workflow definition
   * @return the workflow definition permissions for the workflow definition with the specified ID
   *     and version
   */
  @Query(
      """
         select p
         from WorkflowDefinition wd
           join wd.permissions p
         where wd.id = :workflowDefinitionId
           and wd.version = :workflowDefinitionVersion
         order by p.roleCode
         """)
  List<WorkflowDefinitionPermission> findPermissionsByIdAndVersion(
      @Param("workflowDefinitionId") String workflowDefinitionId,
      @Param("workflowDefinitionVersion") int workflowDefinitionVersion);

  /**
   * Retrieve the workflow step definitions for the workflow definition with the specified ID and
   * version.
   *
   * @param workflowDefinitionId the ID for the workflow definition
   * @param workflowDefinitionVersion the version of the workflow definition
   * @return the workflow step definitions for the workflow definition with the specified ID and
   *     version or an empty list if the workflow definition with the specified ID and version could
   *     not be found or has no associated workflow step definitions
   */
  @Query(
      """
       select wsd
       from WorkflowDefinition wd
       join wd.stepDefinitions wsd
       where wd.id = :workflowDefinitionId
         and wd.version = :workflowDefinitionVersion
       order by wsd.code
       """)
  List<WorkflowStepDefinition> findStepDefinitionsByWorkflowDefinitionIdAndVersion(
      @Param("workflowDefinitionId") String workflowDefinitionId,
      @Param("workflowDefinitionVersion") int workflowDefinitionVersion);

  /**
   * Retrieve the next version of the workflow definition with the specified ID.
   *
   * @param workflowDefinitionId the ID for the workflow definition
   * @return the next version of the workflow definition with the specified ID
   */
  @Query(
      "select coalesce(max(wd.version), 0) + 1 from WorkflowDefinition wd where wd.id = :workflowDefinitionId")
  Integer getNextVersionById(@Param("workflowDefinitionId") String workflowDefinitionId);
}
