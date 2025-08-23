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

import digital.inception.operations.model.DocumentDefinition;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * The {@code DocumentDefinitionRepository} interface declares the persistence for the {@code
 * DocumentDefinition} domain type.
 *
 * @author Marcus Portmann
 */
public interface DocumentDefinitionRepository extends JpaRepository<DocumentDefinition, String> {

  /**
   * Returns whether a document definition with the specified ID exists and is associated with the
   * document definition category with the specified ID.
   *
   * @param categoryId the ID for the document definition category
   * @param id the ID for the document definition
   * @return {@code true} if a document definition with the specified ID exists and is associated
   *     with the document definition category with the specified ID or {@code false} otherwise
   */
  boolean existsByCategoryIdAndId(String categoryId, String id);

  /**
   * Find all the document definitions that are associated with the document definition category
   * with the specified ID and either
   *
   * <ul>
   *   <li>specific to the given tenant ({@code tenantId} matches), or
   *   <li>global (the document definition’s {@code tenantId} is {@code null}).
   * </ul>
   *
   * @param categoryId the ID for the document definition category the document definitions are
   *     associated with
   * @param tenantId the tenant identifier to match (may not be {@code null})
   * @return the matching document definitions, ordered by ID
   */
  @Query(
      """
         select dd
           from DocumentDefinition dd
          where dd.categoryId = :categoryId
             and (dd.tenantId = :tenantId or dd.tenantId is null)
          order by dd.id
         """)
  List<DocumentDefinition> findForCategoryAndTenantOrGlobal(
      @Param("categoryId") String categoryId, @Param("tenantId") UUID tenantId);
}
