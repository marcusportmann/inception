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

import digital.inception.operations.model.InteractionNote;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * The {@code InteractionNoteRepository} interface provides the persistence operations for the
 * {@link InteractionNote} domain type.
 *
 * <p>This repository extends {@code JpaRepository} to provide standard CRUD (Create, Read, Update,
 * Delete) operations and {@code JpaSpecificationExecutor} to support more complex queries using
 * specifications.
 *
 * @author Marcus Portmann
 */
public interface InteractionNoteRepository
    extends JpaRepository<InteractionNote, UUID>, JpaSpecificationExecutor<InteractionNote> {

  /**
   * Returns whether a interaction note with the specified tenant ID and ID exists.
   *
   * @param tenantId the ID for the tenant the interaction note is associated with
   * @param interactionNoteId the ID for the interaction note
   * @return {@code true} if a interaction note with the specified tenant ID and ID exists or {@code
   *     false} otherwise
   */
  boolean existsByTenantIdAndId(UUID tenantId, UUID interactionNoteId);

  /**
   * Returns whether a interaction note with the specified tenant ID, interaction ID and ID exists.
   *
   * @param tenantId the ID for the tenant the interaction note is associated with
   * @param interactionId the ID for the interaction the interaction note is associated with
   * @param interactionNoteId the ID for the interaction note
   * @return {@code true} if a interaction note with the specified tenant ID, interaction ID and ID
   *     exists or {@code false} otherwise
   */
  boolean existsByTenantIdAndInteractionIdAndId(
      UUID tenantId, UUID interactionId, UUID interactionNoteId);

  /**
   * Retrieve the interaction note.
   *
   * @param tenantId the ID for the tenant the interaction note is associated with
   * @param interactionNoteId the ID for the interaction note
   * @return an Optional containing the interaction note or an empty Optional if the interaction
   *     note could not be found
   */
  Optional<InteractionNote> findByTenantIdAndId(UUID tenantId, UUID interactionNoteId);
}
