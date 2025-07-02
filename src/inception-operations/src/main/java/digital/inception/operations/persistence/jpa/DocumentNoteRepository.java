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

import digital.inception.operations.model.DocumentNote;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * The {@code DocumentNoteRepository} interface provides the persistence operations for the {@link
 * DocumentNote} domain type.
 *
 * <p>This repository extends {@code JpaRepository} to provide standard CRUD (Create, Read, Update,
 * Delete) operations and {@code JpaSpecificationExecutor} to support more complex queries using
 * specifications.
 *
 * @author Marcus Portmann
 */
public interface DocumentNoteRepository
    extends JpaRepository<DocumentNote, UUID>, JpaSpecificationExecutor<DocumentNote> {

  /**
   * Returns whether a document note with the specified tenant ID and ID exists.
   *
   * @param tenantId the ID for the tenant the document note is associated with
   * @param documentNoteId the ID for the document note
   * @return {@code true} if a document note with the specified tenant ID and ID exists or {@code
   *     false} otherwise
   */
  boolean existsByTenantIdAndId(UUID tenantId, UUID documentNoteId);

  /**
   * Retrieve the document note.
   *
   * @param tenantId the ID for the tenant the document note is associated with
   * @param documentNoteId the ID for the document note
   * @return an Optional containing the document note or an empty Optional if the document note
   *     could not be found
   */
  Optional<DocumentNote> findByTenantIdAndId(UUID tenantId, UUID documentNoteId);
}
