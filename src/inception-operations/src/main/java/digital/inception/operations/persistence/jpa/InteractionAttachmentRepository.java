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

import digital.inception.operations.model.InteractionAttachment;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * The {@code InteractionAttachmentRepository} interface declares the persistence for the {@code
 * InteractionAttachment} domain type.
 *
 * @author Marcus Portmann
 */
public interface InteractionAttachmentRepository
    extends JpaRepository<InteractionAttachment, UUID> {

  /**
   * Returns whether an interaction attachment with the specified tenant ID and ID exists.
   *
   * @param tenantId the ID for the tenant
   * @param interactionAttachmentId the ID for the interaction attachment
   * @return {@code true} if an interaction attachment with the specified tenant ID and ID exists or
   *     {@code false} otherwise
   */
  boolean existsByTenantIdAndId(UUID tenantId, UUID interactionAttachmentId);

  /**
   * Find the interaction attachment.
   *
   * @param tenantId the ID for the tenant
   * @param interactionAttachmentId the ID for the interaction attachment
   * @return an Optional containing the interaction attachment or an empty Optional if the
   *     interaction attachment could not be found
   */
  Optional<InteractionAttachment> findByTenantIdAndId(UUID tenantId, UUID interactionAttachmentId);

  /**
   * Find the ID for the interaction attachment with the specified interaction ID and hash.
   *
   * @param tenantId the ID for the tenant
   * @param interactionId the ID for the interaction
   * @param hash the hash for interaction attachment
   * @return an Optional containing the ID for the interaction attachment with the specified
   *     interaction ID and hash or an empty optional if the interaction attachment could not be
   *     found
   */
  @Query(
      "select ia.id from InteractionAttachment ia where ia.tenantId = :tenantId and "
          + "ia.interactionId = :interactionId and ia.hash = :hash")
  Optional<UUID> getIdByTenantIdAndInteractionIdAndHash(
      @Param("tenantId") UUID tenantId,
      @Param("interactionId") UUID interactionId,
      @Param("hash") String hash);
}
