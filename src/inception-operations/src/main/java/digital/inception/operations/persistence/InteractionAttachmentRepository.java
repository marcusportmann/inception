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

package digital.inception.operations.persistence;

import digital.inception.operations.model.InteractionAttachment;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

/**
 * The <b>InteractionAttachmentRepository</b> interface declares the persistence for the
 * <b>InteractionAttachment</b> domain type.
 *
 * @author Marcus Portmann
 */
public interface InteractionAttachmentRepository
    extends JpaRepository<InteractionAttachment, UUID> {

  /**
   * Retrieve the ID for the interaction attachment with the specified interaction ID and hash.
   *
   * @param interactionId the ID for the interaction that the interaction attachment is associated
   *     with
   * @param hash the hash for interaction attachment
   * @return an Optional containing the ID for the interaction attachment with the specified
   *     interaction ID and hash or an empty optional if the interaction attachment could not be
   *     found
   */
  @Query(
      "select ia.id from InteractionAttachment ia where ia.interactionId = :interactionId "
          + "and ia.hash = :hash")
  Optional<UUID> getIdByInteractionIdAndHash(UUID interactionId, String hash);
}
