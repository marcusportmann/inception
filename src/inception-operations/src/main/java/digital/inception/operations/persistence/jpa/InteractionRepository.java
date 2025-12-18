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
import digital.inception.operations.model.InteractionDirection;
import digital.inception.operations.model.InteractionStatus;
import digital.inception.operations.model.InteractionSummary;
import digital.inception.processor.persistence.jpa.ProcessableObjectJpaRepository;
import java.time.OffsetDateTime;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
    extends ProcessableObjectJpaRepository<Interaction, UUID, InteractionStatus> {

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
   * Find the interaction.
   *
   * @param tenantId the ID for the tenant
   * @param interactionId the ID for the interaction
   * @return an Optional containing the interaction or an empty Optional if the interaction could
   *     not be found
   */
  Optional<Interaction> findByTenantIdAndId(UUID tenantId, UUID interactionId);

  /**
   * Find the ID for the interaction with the specified source reference and source ID.
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
  Optional<UUID> findIdByTenantIdAndSourceIdAndSourceReference(
      @Param("tenantId") UUID tenantId,
      @Param("sourceId") UUID sourceId,
      @Param("sourceReference") String sourceReference);

  /**
   * Find the ID for the interaction source the interaction is associated with.
   *
   * @param tenantId the ID for the tenant
   * @param interactionId the ID for the interaction
   * @return an Optional containing the ID for the interaction source the interaction is associated
   *     with or an empty Optional if the interaction could not be found
   */
  @Query(
      "select i.sourceId from Interaction i where i.tenantId = :tenantId and i.id = :interactionId")
  Optional<UUID> findInteractionSourceIdByTenantIdAndInteractionId(
      @Param("tenantId") UUID tenantId, @Param("interactionId") UUID interactionId);

  /**
   * Find the summaries for the interactions matching the specified criteria.
   *
   * @param tenantId the ID for the tenant
   * @param interactionSourceId the the ID for the interaction source the interactions are
   *     associated with
   * @param status the status filter to apply to the interaction summaries
   * @param direction the direction filter to apply to the interaction summaries, i.e., inbound or
   *     outbound
   * @param filterLike the SQL like filter to apply to the interaction summaries
   * @param pageable the pagination information
   * @return the summaries for the interactions matching the specified criteria
   */
  @Query(
      "select new digital.inception.operations.model.InteractionSummary("
          + "  i.id, i.tenantId, i.status, i.sourceId, i.conversationId, i.partyId, i.type, i.direction, "
          + "  i.sender, i.recipients, i.subject, i.mimeType, i.priority, i.occurred, i.assigned, i.assignedTo, "
          + "  (select count(a) from InteractionAttachment a where a.interactionId = i.id), "
          + "  (select count(n) from InteractionNote n where n.interactionId = i.id)"
          + ") "
          + "from Interaction i "
          + "where i.tenantId = :tenantId "
          + "and i.sourceId = :interactionSourceId "
          + "and (:direction is null or i.direction = :direction) "
          + "and (:status is null or i.status = :status) "
          + "and (:filterLike is null or lower(i.sender) like :filterLike or lower(i.subject) like :filterLike)")
  Page<InteractionSummary> findInteractionSummaries(
      @Param("tenantId") UUID tenantId,
      @Param("interactionSourceId") UUID interactionSourceId,
      @Param("status") InteractionStatus status,
      @Param("direction") InteractionDirection direction,
      @Param("filterLike") String filterLike,
      Pageable pageable);

  /**
   * Find the summaries for the interactions matching the specified criteria.
   *
   * @param tenantId the ID for the tenant
   * @param interactionSourceId the the ID for the interaction source the interactions are
   *     associated with
   * @param status the status filter to apply to the interaction summaries
   * @param direction the direction filter to apply to the interaction summaries, i.e., inbound or
   *     outbound
   * @param filterLike the SQL like filter to apply to the interaction summaries
   * @param pageable the pagination information
   * @return the summaries for the interactions matching the specified criteria
   */
  @Query(
      "select new digital.inception.operations.model.InteractionSummary("
          + "  i.id, i.tenantId, i.status, i.sourceId, i.conversationId, i.partyId, i.type, i.direction, "
          + "  i.sender, i.recipients, i.subject, i.mimeType, i.priority, i.occurred, i.assigned, i.assignedTo"
          + ") "
          + "from Interaction i "
          + "where i.tenantId = :tenantId "
          + "and i.sourceId = :interactionSourceId "
          + "and (:direction is null or i.direction = :direction) "
          + "and (:status is null or i.status = :status) "
          + "and (:filterLike is null or lower(i.sender) like :filterLike or lower(i.subject) like :filterLike)")
  Page<InteractionSummary> findInteractionSummariesWithoutCounts(
      @Param("tenantId") UUID tenantId,
      @Param("interactionSourceId") UUID interactionSourceId,
      @Param("status") InteractionStatus status,
      @Param("direction") InteractionDirection direction,
      @Param("filterLike") String filterLike,
      Pageable pageable);

  /**
   * Find the subject for the interaction.
   *
   * @param interactionId the ID for the interaction
   * @return an Optional containing the subject for the interaction or an empty Optional if the
   *     interaction could not be found
   */
  @Query("select i.subject from Interaction i where i.id = :interactionId")
  Optional<String> findSubjectById(@Param("interactionId") UUID interactionId);

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
}
