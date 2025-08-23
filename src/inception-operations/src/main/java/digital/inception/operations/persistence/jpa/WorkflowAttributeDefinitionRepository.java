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

import digital.inception.operations.model.WorkflowAttributeDefinition;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * The {@code WorkflowAttributeDefinitionRepository} interface declares the persistence for the
 * {@code WorkflowAttributeDefinition} domain type.
 *
 * @author Marcus Portmann
 */
public interface WorkflowAttributeDefinitionRepository
    extends JpaRepository<WorkflowAttributeDefinition, String> {

  /**
   * Find all the workflow attribute definitions that are specific to the given tenant ({@code
   * tenantId} matches), or global (the workflow attribute definition’s {@code tenantId} is {@code
   * null}).
   *
   * @param tenantId the ID for the tenant
   * @return the matching workflow attribute definitions, ordered by code
   */
  @Query(
      """
         select dad
           from WorkflowAttributeDefinition dad
          where (dad.tenantId = :tenantId or dad.tenantId is null)
          order by dad.code
         """)
  List<WorkflowAttributeDefinition> findForTenantOrGlobal(@Param("tenantId") UUID tenantId);

  /**
   * Find all the workflow attribute definitions that are
   *
   * <ul>
   *   <li>specific to the given workflow definition ({@code workflowDefinitionId} matches), or
   *       global (the workflow attribute definition’s {@code workflowDefinitionId} is {@code
   *       null}).
   *   <li>specific to the given tenant ({@code tenantId} matches), or global (the workflow
   *       attribute definition’s {@code tenantId} is {@code null}).
   * </ul>
   *
   * @param workflowDefinitionId the ID for the workflow definition
   * @param tenantId the ID for the tenant
   * @return the matching workflow attribute definitions, ordered by code
   */
  @Query(
      """
         select wad
           from WorkflowAttributeDefinition wad
          where (wad.workflowDefinitionId = :workflowDefinitionId or wad.workflowDefinitionId is null)
             and (wad.tenantId = :tenantId or wad.tenantId is null)
          order by wad.code
         """)
  List<WorkflowAttributeDefinition> findForWorkflowDefinitionAndTenantOrGlobal(
      @Param("workflowDefinitionId") String workflowDefinitionId, @Param("tenantId") UUID tenantId);
}
