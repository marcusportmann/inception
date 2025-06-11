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

import digital.inception.operations.model.DocumentDefinitionCategory;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * The {@code DocumentDefinitionCategoryRepository} interface declares the persistence for the
 * {@code DocumentDefinitionCategory} domain type.
 *
 * @author Marcus Portmann
 */
public interface DocumentDefinitionCategoryRepository
    extends JpaRepository<DocumentDefinitionCategory, String> {

  /**
   * Returns all the document definition categories that are either
   *
   * <ul>
   *   <li>specific to the given tenant ({@code tenantId} matches), or
   *   <li>global (the document definition category’s {@code tenantId} is {@code null}).
   * </ul>
   *
   * @param tenantId the tenant identifier to match (may not be {@code null})
   * @return the matching document definition categories, ordered by ID
   */
  @Query(
      """
         select ddc
           from DocumentDefinitionCategory ddc
          where ddc.tenantId = :tenantId
             or ddc.tenantId is null
          order by ddc.id
         """)
  List<DocumentDefinitionCategory> findForTenantOrGlobal(@Param("tenantId") UUID tenantId);
}
