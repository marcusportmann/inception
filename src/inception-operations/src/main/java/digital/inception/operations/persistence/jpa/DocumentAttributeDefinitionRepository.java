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

import digital.inception.operations.model.DocumentAttributeDefinition;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * The {@code DocumentAttributeDefinitionRepository} interface declares the persistence for the
 * {@code DocumentAttributeDefinition} domain type.
 *
 * @author Marcus Portmann
 */
public interface DocumentAttributeDefinitionRepository
    extends JpaRepository<DocumentAttributeDefinition, String> {

  /**
   * Returns all the document attribute definitions that are
   *
   * <ul>
   *   <li>specific to the given document definition ({@code documentDefinitionId} matches), or
   *       global (the document attribute definition’s {@code documentDefinitionId} is {@code
   *       null}).
   *   <li>specific to the given tenant ({@code tenantId} matches), or global (the document
   *       attribute definition’s {@code tenantId} is {@code null}) or
   * </ul>
   *
   * associated with
   *
   * @param documentDefinitionId the ID for the document definition
   * @param tenantId the ID for the tenant
   * @return the matching document attribute definitions, ordered by code
   */
  @Query(
      """
         select dad
           from DocumentAttributeDefinition dad
          where (dad.documentDefinitionId = :documentDefinitionId or dad.documentDefinitionId is null)
             and (dad.tenantId = :tenantId or dad.tenantId is null)
          order by dad.code
         """)
  List<DocumentAttributeDefinition> findForDocumentDefinitionAndTenantOrGlobal(
      @Param("documentDefinitionId") String documentDefinitionId, @Param("tenantId") UUID tenantId);
}
