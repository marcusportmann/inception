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

import digital.inception.operations.model.DocumentTemplate;
import digital.inception.operations.model.DocumentTemplateSummary;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * The {@code DocumentTemplateRepository} interface declares the persistence for the {@code
 * DocumentTemplate} domain type.
 *
 * @author Marcus Portmann
 */
public interface DocumentTemplateRepository
    extends JpaRepository<DocumentTemplate, String>, JpaSpecificationExecutor<DocumentTemplate> {

  /**
   * Returns whether a document template with the specified tenant ID and ID exists.
   *
   * @param tenantId the ID for the tenant
   * @param documentTemplateId the ID for the document template
   * @return {@code true} if a document template with the specified tenant ID and ID exists or
   *     {@code false} otherwise
   */
  boolean existsByTenantIdAndId(UUID tenantId, String documentTemplateId);

  /**
   * Find the document template.
   *
   * @param tenantId the ID for the tenant
   * @param documentTemplateId the ID for the document template
   * @return an Optional containing the document template or an empty Optional if the document
   *     template could not be found
   */
  Optional<DocumentTemplate> findByTenantIdAndId(UUID tenantId, String documentTemplateId);

  /**
   * Find the summaries for the document templates matching the specified criteria.
   *
   * @param tenantId the ID for the tenant
   * @param filterLike the SQL like filter to apply to the document template summaries
   * @param pageable the pagination information
   * @return the summaries for the document templates matching the specified criteria
   */
  @Query(
      "select new digital.inception.operations.model.DocumentTemplateSummary("
          + "  dt.id, dt.categoryId, dt.tenantId, dt.name, dt.description, dt.hash,"
          + "  dt.created, dt.createdBy, dt.updated, dt.updatedBy"
          + ") "
          + "from DocumentTemplate dt "
          + "where (dt.tenantId = :tenantId or dt.tenantId is null)"
          + "and (:filterLike is null or lower(dt.name) like :filterLike or lower(dt.createdBy) like :filterLike or lower(dt.updatedBy) like :filterLike)")
  Page<DocumentTemplateSummary> findDocumentTemplateSummaries(
      @Param("tenantId") UUID tenantId, @Param("filterLike") String filterLike, Pageable pageable);

  /**
   * Find all the summaries for the document templates that are associated with the document
   * template category with the specified ID and are either
   *
   * <ul>
   *   <li>specific to the given tenant ({@code tenantId} matches), or
   *   <li>global (the document template’s {@code tenantId} is {@code null}).
   * </ul>
   *
   * @param categoryId the ID for the document template category the document templates are
   *     associated with
   * @param tenantId the tenant identifier to match (may not be {@code null})
   * @return the summaries for the matching document templates, ordered by ID
   */
  @Query(
      """
         select new digital.inception.operations.model.DocumentTemplateSummary(
           dt.id, dt.categoryId, dt.tenantId, dt.name, dt.description, dt.hash,
           dt.created, dt.createdBy, dt.updated, dt.updatedBy)
         from DocumentTemplate dt
         where dt.categoryId = :categoryId
           and (dt.tenantId = :tenantId or dt.tenantId is null)
         order by dt.id
         """)
  List<DocumentTemplateSummary> findDocumentTemplateSummariesForCategoryAndTenantOrGlobal(
      @Param("categoryId") String categoryId, @Param("tenantId") UUID tenantId);
}
