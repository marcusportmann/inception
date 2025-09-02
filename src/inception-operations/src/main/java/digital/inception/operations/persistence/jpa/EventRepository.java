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

import digital.inception.operations.model.Event;
import digital.inception.operations.model.EventStatus;
import digital.inception.operations.model.ObjectType;
import jakarta.persistence.LockModeType;
import java.time.OffsetDateTime;
import java.util.List;
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
 * The {@code EventRepository} interface declares the persistence for the {@code Event} domain type.
 *
 * @author Marcus Portmann
 */
public interface EventRepository
    extends JpaRepository<Event, UUID>, JpaSpecificationExecutor<Event> {

  /**
   * Returns whether an event with the specified tenant ID and ID exists.
   *
   * @param tenantId the ID for the tenant
   * @param eventId the ID for the event
   * @return {@code true} if an event with the specified tenant ID and ID exists or {@code false}
   *     otherwise
   */
  boolean existsByTenantIdAndId(UUID tenantId, UUID eventId);

  /**
   * Find the events for the object.
   *
   * @param tenantId the ID for the tenant
   * @param objectType the object type for the object
   * @param objectId the ID for the object
   * @return the events for the object
   */
  List<Event> findByTenantIdAndObjectTypeAndObjectId(
      UUID tenantId, ObjectType objectType, UUID objectId);

  /**
   * Find the events queued for processing.
   *
   * @param tenantId the ID for the tenant
   * @param lastProcessedBefore the date and time used to select failed events for re-processing
   * @param pageable the pagination information
   * @return the events queued for processing
   */
  @Lock(LockModeType.PESSIMISTIC_WRITE)
  @Query(
      "select e from Event e "
          + "where e.tenantId = :tenantId "
          + "and e.status = digital.inception.operations.model.EventStatus.QUEUED "
          + "and (e.lastProcessed < :lastProcessedBefore or e.lastProcessed is null)")
  List<Event> findEventsQueuedForProcessingForWrite(
      @Param("tenantId") UUID tenantId,
      @Param("lastProcessedBefore") OffsetDateTime lastProcessedBefore,
      Pageable pageable);

  /**
   * Lock the event for processing.
   *
   * @param tenantId the ID for the tenant
   * @param eventId the ID for the event
   * @param lockName the name of the lock
   * @param when the date and time the event is locked for processing
   */
  @Transactional
  @Modifying(clearAutomatically = true, flushAutomatically = true)
  @Query(
      "update Event e set e.lockName = :lockName, e.status = digital.inception.operations.model.EventStatus.PROCESSING, "
          + "e.processingAttempts = e.processingAttempts + 1, e.lastProcessed = :when "
          + "where e.tenantId = :tenantId and e.id = :eventId")
  void lockEventForProcessing(
      @Param("tenantId") UUID tenantId,
      @Param("eventId") UUID eventId,
      @Param("lockName") String lockName,
      @Param("when") OffsetDateTime when);

  /**
   * Reset the event locks with the specified status.
   *
   * @param tenantId the ID for the tenant
   * @param status the status
   * @param newStatus the new status for the events
   * @param lockName the lock name
   */
  @Transactional
  @Modifying(clearAutomatically = true, flushAutomatically = true)
  @Query(
      "update Event e set e.status = :newStatus, e.lockName = null "
          + "where e.tenantId = :tenantId and e.lockName = :lockName and e.status = :status")
  void resetEventLocks(
      @Param("tenantId") UUID tenantId,
      @Param("status") EventStatus status,
      @Param("newStatus") EventStatus newStatus,
      @Param("lockName") String lockName);

  /**
   * Unlock the event.
   *
   * @param tenantId the ID for the tenant
   * @param eventId the ID for the event
   * @param status the status for the event
   */
  @Transactional
  @Modifying(clearAutomatically = true, flushAutomatically = true)
  @Query(
      "update Event e set e.status = :status, e.lockName = null "
          + "where e.tenantId = :tenantId and  e.id = :eventId")
  void unlockEvent(
      @Param("tenantId") UUID tenantId,
      @Param("eventId") UUID eventId,
      @Param("status") EventStatus status);
}
