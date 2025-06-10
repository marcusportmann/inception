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

import digital.inception.operations.model.Document;
import digital.inception.operations.model.Workflow;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * The {@code DocumentRepository} interface declares the persistence for the {@code Document} domain
 * type.
 *
 * @author Marcus Portmann
 */
public interface DocumentRepository extends JpaRepository<Document, UUID> {

  /**
   * Returns whether a document with the specified tenant ID and ID exists.
   *
   * @param tenantId the ID for the tenant the document is associated with
   * @param documentId the ID for the document
   * @return {@code true} if a document with the specified tenant ID and ID exists or {@code false}
   *     otherwise
   */
  boolean existsByTenantIdAndId(UUID tenantId, UUID documentId);

  /**
   * Retrieve the document.
   *
   * @param tenantId the ID for the tenant the document is associated with
   * @param id the ID for the document
   * @return an Optional containing the document or an empty Optional if the document could not be
   *     found
   */
  Optional<Document> findByTenantIdAndId(UUID tenantId, UUID id);
}
