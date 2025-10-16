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

import digital.inception.operations.model.WorkflowDefinitionSummary;
import digital.inception.operations.model.WorkflowDefinitionSummaryId;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * The {@code WorkflowDefinitionSummaryRepository} interface declares the persistence for the {@code
 * WorkflowDefinitionSummary} domain type.
 *
 * @author Marcus Portmann
 */
public interface WorkflowDefinitionSummaryRepository
    extends JpaRepository<WorkflowDefinitionSummary, WorkflowDefinitionSummaryId> {

  /**
   * Find the latest versions of all the workflow definitions that are associated with the workflow
   * definition category with the specified ID and are either
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
         from WorkflowDefinitionSummary wd
        where wd.categoryId = :categoryId
          and (wd.tenantId = :tenantId or wd.tenantId is null)
          and wd.version = (
                select max(wd2.version)
                  from WorkflowDefinitionSummary wd2
                 where wd2.id        = wd.id
              )
        order by wd.id
       """)
  List<WorkflowDefinitionSummary> findForCategoryAndTenantOrGlobal(
      @Param("categoryId") String categoryId, @Param("tenantId") UUID tenantId);
}
