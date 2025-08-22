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

import digital.inception.operations.model.Interaction;
import digital.inception.operations.model.InteractionStatus;
import jakarta.persistence.LockModeType;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

/**
 * The {@code InteractionRepository} interface declares the persistence for the {@code Interaction}
 * domain type.
 *
 * @author Marcus Portmann
 */
public interface InteractionRepository
    extends JpaRepository<Interaction, UUID>, JpaSpecificationExecutor<Interaction> {

  /**
   * Assign the interaction to the user.
   *
   * @param interactionId the ID for the interaction
   * @param assigned the date and time the interaction was assigned
   * @param assignedTo the username for the user the interaction was assigned to
   */
  @Transactional
  @Modifying(clearAutomatically = true, flushAutomatically = true)
  @Query(
      "update Interaction i set i.assigned = :assigned, i.assignedTo = :assignedTo "
          + "where i.id = :interactionId")
  void assignInteraction(
      @Param("interactionId") UUID interactionId,
      @Param("assigned") OffsetDateTime assigned,
      @Param("assignedTo") String assignedTo);

  /**
   * Delink the party from the interaction.
   *
   * @param tenantId the ID for the tenant
   * @param interactionId the ID for the interaction
   * @return the number of rows that were updated (0 or 1)
   */
  @Transactional
  @Modifying(clearAutomatically = true, flushAutomatically = true)
  @Query(
      """
      update Interaction i
         set i.partyId  = null
       where i.tenantId = :tenantId
         and i.id       = :interactionId
      """)
  int delinkPartyFromInteraction(
      @Param("tenantId") UUID tenantId, @Param("interactionId") UUID interactionId);

  /**
   * Returns whether an interaction with the specified source reference for the interaction source
   * with the specified source ID exists.
   *
   * @param sourceId the ID for the interaction source the interaction is associated with
   * @param sourceReference the interaction source specific reference for the interaction
   * @return {@code true} if an interaction with the specified source reference for the interaction
   *     source with the specified ID exists or {@code false} otherwise
   */
  boolean existsBySourceIdAndSourceReference(UUID sourceId, String sourceReference);

  /**
   * Returns whether an interaction with the specified tenant ID and ID exists.
   *
   * @param tenantId the ID for the tenant
   * @param interactionId the ID for the interaction
   * @return {@code true} if an interaction with the specified tenant ID and ID exists or {@code
   *     false} otherwise
   */
  boolean existsByTenantIdAndId(UUID tenantId, UUID interactionId);

  /**
   * Retrieve the interaction.
   *
   * @param tenantId the ID for the tenant
   * @param interactionId the ID for the interaction
   * @return an Optional containing the interaction or an empty Optional if the interaction could
   *     not be found
   */
  Optional<Interaction> findByTenantIdAndId(UUID tenantId, UUID interactionId);

  /**
   * Retrieve the interactions queued for processing.
   *
   * @param lastProcessedBefore the date and time used to select failed interactions for
   *     re-processing
   * @param pageable the pagination information
   * @return the interactions queued for processing
   */
  @Lock(LockModeType.PESSIMISTIC_WRITE)
  @Query(
      "select i from Interaction i where i.status = digital.inception.operations.model.InteractionStatus.QUEUED "
          + "and (i.lastProcessed < :lastProcessedBefore or i.lastProcessed is null)")
  List<Interaction> findInteractionsQueuedForProcessingForWrite(
      @Param("lastProcessedBefore") OffsetDateTime lastProcessedBefore, Pageable pageable);

  /**
   * Retrieve the ID for the interaction with the specified source reference and source ID.
   *
   * @param tenantId the ID for the tenant
   * @param sourceId the ID for the interaction source the interaction is associated with
   * @param sourceReference the interaction source specific reference
   * @return an Optional containing the ID for the interaction with the specified source reference
   *     and source ID or an empty optional if the interaction could not be found
   */
  @Query(
      "select i.id from Interaction i where i.tenantId = :tenantId and i.sourceId = :sourceId "
          + "and i.sourceReference = :sourceReference")
  Optional<UUID> getIdByTenantIdAndSourceIdAndSourceReference(
      @Param("tenantId") UUID tenantId,
      @Param("sourceId") UUID sourceId,
      @Param("sourceReference") String sourceReference);

  /**
   * Retrieve the ID for the interaction source the interaction is associated with.
   *
   * @param tenantId the ID for the tenant
   * @param interactionId the ID for the interaction
   * @return an Optional containing the ID for the interaction source the interaction is associated
   *     with or an empty Optional if the interaction could not be found
   */
  @Query(
      "select i.sourceId from Interaction i where i.tenantId = :tenantId and i.id = :interactionId")
  Optional<UUID> getInteractionSourceIdByTenantIdAndInteractionId(
      @Param("tenantId") UUID tenantId, @Param("interactionId") UUID interactionId);

  /**
   * Retrieve the subject for the interaction.
   *
   * @param interactionId the ID for the interaction
   * @return an Optional containing the subject for the interaction or an empty Optional if the
   *     interaction could not be found
   */
  @Query("select i.subject from Interaction i where i.id = :interactionId")
  Optional<String> getSubjectById(@Param("interactionId") UUID interactionId);

  /**
   * Link the party to the interaction.
   *
   * @param tenantId the ID for the tenant
   * @param interactionId the ID for the interaction
   * @param partyId the ID for the party
   * @return the number of rows that were updated (0 or 1)
   */
  @Transactional
  @Modifying(clearAutomatically = true, flushAutomatically = true)
  @Query(
      """
      update Interaction i
         set i.partyId  = :partyId
       where i.tenantId = :tenantId
         and i.id       = :interactionId
      """)
  int linkPartyToInteraction(
      @Param("tenantId") UUID tenantId,
      @Param("interactionId") UUID interactionId,
      @Param("partyId") UUID partyId);

  /**
   * Lock the interaction for processing.
   *
   * @param interactionId the ID for the interaction
   * @param lockName the name of the lock
   * @param when the date and time the interaction is locked for processing
   */
  @Transactional
  @Modifying(clearAutomatically = true, flushAutomatically = true)
  @Query(
      "update Interaction i set i.lockName = :lockName, i.status = digital.inception.operations.model.InteractionStatus.PROCESSING, "
          + "i.processingAttempts = i.processingAttempts + 1, i.lastProcessed = :when where i.id = :interactionId")
  void lockInteractionForProcessing(
      @Param("interactionId") UUID interactionId,
      @Param("lockName") String lockName,
      @Param("when") OffsetDateTime when);

  /**
   * Reset the interaction locks with the specified status.
   *
   * @param status the status
   * @param newStatus the new status for the interactions
   * @param lockName the lock name
   */
  @Transactional
  @Modifying(clearAutomatically = true, flushAutomatically = true)
  @Query(
      "update Interaction i set i.status = :newStatus, i.lockName = null "
          + "where i.lockName = :lockName and i.status = :status")
  void resetInteractionLocks(
      @Param("status") InteractionStatus status,
      @Param("newStatus") InteractionStatus newStatus,
      @Param("lockName") String lockName);

  /**
   * Transfer the interaction to the interaction source.
   *
   * @param interactionId the ID for the interaction
   * @param interactionSourceId the ID for the interaction source the interaction should be
   *     transferred to
   */
  @Transactional
  @Modifying(clearAutomatically = true, flushAutomatically = true)
  @Query(
      "update Interaction i set i.sourceId = :interactionSourceId " + "where i.id = :interactionId")
  void transferInteraction(
      @Param("interactionId") UUID interactionId,
      @Param("interactionSourceId") UUID interactionSourceId);

  /**
   * Unlock the interaction.
   *
   * @param interactionId the ID for the interaction
   * @param status the status for the interaction
   */
  @Transactional
  @Modifying(clearAutomatically = true, flushAutomatically = true)
  @Query(
      "update Interaction i set i.status = :status, i.lockName = null where i.id = :interactionId")
  void unlockInteraction(
      @Param("interactionId") UUID interactionId, @Param("status") InteractionStatus status);
}
