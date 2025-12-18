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

package digital.inception.processor.persistence.mongo;

import com.mongodb.client.result.UpdateResult;
import digital.inception.processor.AbstractProcessableObject;
import digital.inception.processor.ProcessableObjectStatus;
import java.io.Serializable;
import java.time.OffsetDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

/**
 * Abstract base class that implements the generic MongoDB operations for processable objects.
 *
 * <p>This class provides a reusable implementation of {@link ProcessableObjectMongoOperations} in
 * terms of {@link MongoOperations}. It encapsulates the common behavior required by the
 * background-processing infrastructure:
 *
 * <ul>
 *   <li>locating the next objects eligible for processing,
 *   <li>atomically locking an object for processing using a {@code findAndModify} operation,
 *   <li>resetting stale PROCESSING locks back to a PENDING state, and
 *   <li>updating processing-related metadata when an object is unlocked.
 * </ul>
 *
 * <p>Concrete, per-entity custom repository implementation classes should extend this base class
 * and supply the concrete entity type via the constructor. A typical setup looks like:
 *
 * <ul>
 *   <li>{@code MongoReportRepository} extends {@code MongoRepository<MongoReport, String>} and
 *       {@code MongoReportRepositoryCustom},
 *   <li>{@code MongoReportRepositoryCustom} extends {@code
 *       ProcessableObjectMongoOperations<MongoReport, String, ReportStatus>}, and
 *   <li>{@code MongoReportRepositoryImpl} extends {@code
 *       AbstractProcessableObjectMongoOperations<MongoReport, String, ReportStatus>} and implements
 *       {@code MongoReportRepositoryCustom}.
 * </ul>
 *
 * @param <T> the concrete processable object type
 * @param <ID> the identifier type for the processable object
 * @param <S> the status type for the processable object, which must implement {@link
 *     ProcessableObjectStatus}
 * @author Marcus Portmann
 */
@SuppressWarnings("unused")
public abstract class AbstractProcessableObjectMongoOperations<
        T extends AbstractProcessableObject<ID, S>,
        ID extends Serializable,
        S extends ProcessableObjectStatus>
    implements ProcessableObjectMongoOperations<T, ID, S> {

  /**
   * The concrete entity class handled by the {@code ProcessableObjectMongoOperations}
   * implementation.
   */
  protected final Class<T> entityClass;

  /** The {@link MongoOperations} instance used to perform MongoDB operations. */
  protected final MongoOperations mongoTemplate;

  /**
   * Constructs a new {@code AbstractProcessableObjectMongoOperations}.
   *
   * @param mongoTemplate the {@link MongoOperations} used to interact with MongoDB
   * @param entityClass the concrete entity class corresponding to {@code T}
   */
  protected AbstractProcessableObjectMongoOperations(
      MongoOperations mongoTemplate, Class<T> entityClass) {
    this.mongoTemplate = mongoTemplate;
    this.entityClass = entityClass;
  }

  /**
   * Finds the next batch of objects that are eligible for processing.
   *
   * <p>Eligibility is defined as:
   *
   * <ul>
   *   <li>the object's {@code status} is contained in {@code pendingStatuses}, and
   *   <li>{@code nextProcessed} is less than or equal to {@code now}.
   * </ul>
   *
   * <p>The results are ordered by {@code nextProcessed} ascending and paged according to the
   * supplied {@link Pageable}.
   *
   * @param pendingStatuses the collection of statuses that represent PENDING states
   * @param now the current time used to compare against {@code nextProcessed}
   * @param pageable the paging information (for example, typically first page with size {@code 1})
   * @return a list of objects eligible for processing, possibly empty
   */
  @Override
  public List<T> findNextObjectsQueuedForProcessing(
      Collection<S> pendingStatuses, OffsetDateTime now, Pageable pageable) {

    Query query =
        Query.query(
                Criteria.where("status")
                    .in(pendingStatuses)
                    .and("nextProcessed")
                    .lte(now)
                    .and("processingSuspended")
                    .is(false))
            .with(pageable)
            .with(Sort.by(Sort.Direction.ASC, "nextProcessed"));

    return mongoTemplate.find(query, entityClass);
  }

  /**
   * Atomically locks the specified object for processing using a {@code findAndModify} operation.
   *
   * <p>The update will only be applied if all the following are true:
   *
   * <ul>
   *   <li>the document's {@code _id} matches {@code id},
   *   <li>the current {@code status} matches {@code expectedCurrentStatus}, and
   *   <li>the document is currently unlocked ({@code lockName == null} and {@code locked == null}).
   * </ul>
   *
   * <p>If the lock is acquired, the method:
   *
   * <ul>
   *   <li>sets {@code status} to {@code processingStatus},
   *   <li>sets {@code lockName} and {@code locked},
   *   <li>sets {@code processingAttempts} to the supplied {@code processingAttempts}, and
   *   <li>returns the updated document.
   * </ul>
   *
   * <p>If no document matches the criteria (for example, another instance claimed the object
   * first), {@link Optional#empty()} is returned.
   *
   * @param id the identifier of the object to lock
   * @param expectedCurrentStatus the expected current status of the object (usually a PENDING
   *     status)
   * @param processingStatus the PROCESSING status to set when the lock is acquired
   * @param lockName the logical name of the processing instance acquiring the lock
   * @param locked the timestamp when the lock is acquired
   * @param processingAttempts the new processing-attempt count to persist
   * @return an {@link Optional} containing the updated object if the lock was acquired, or {@link
   *     Optional#empty()} if the lock could not be obtained
   */
  @Override
  public Optional<T> lockForProcessing(
      ID id,
      S expectedCurrentStatus,
      S processingStatus,
      String lockName,
      OffsetDateTime locked,
      int processingAttempts) {

    Query query =
        Query.query(
            Criteria.where("_id")
                .is(id)
                .and("status")
                .is(expectedCurrentStatus)
                .and("lockName")
                .is(null)
                .and("locked")
                .is(null));

    Update update =
        new Update()
            .set("status", processingStatus)
            .set("lockName", lockName)
            .set("locked", locked)
            .set("processingAttempts", processingAttempts);

    FindAndModifyOptions options = FindAndModifyOptions.options().returnNew(true);

    T updated = mongoTemplate.findAndModify(query, update, options, entityClass);
    return Optional.ofNullable(updated);
  }

  /**
   * Resets stale locks for objects in a PROCESSING status back to the corresponding PENDING status.
   *
   * <p>This method locates all documents where:
   *
   * <ul>
   *   <li>{@code status == processingStatus},
   *   <li>{@code locked != null}, and
   *   <li>{@code locked <= lockCutoff}.
   * </ul>
   *
   * <p>For each matching document, it:
   *
   * <ul>
   *   <li>sets {@code status} to {@code pendingStatus}, and
   *   <li>clears {@code lockName} and {@code locked}.
   * </ul>
   *
   * @param processingStatus the PROCESSING status to search for when identifying stale locks
   * @param pendingStatus the PENDING status to which stale-locked objects should be reset
   * @param lockCutoff the cutoff time; any lock with {@code locked <= lockCutoff} is considered
   *     stale
   * @return the number of documents that were updated
   */
  @Override
  public int resetStaleLocks(S processingStatus, S pendingStatus, OffsetDateTime lockCutoff) {

    Query query =
        Query.query(
            Criteria.where("status").is(processingStatus).and("locked").ne(null).lte(lockCutoff));

    Update update = new Update().set("status", pendingStatus).unset("lockName").unset("locked");

    UpdateResult result = mongoTemplate.updateMulti(query, update, entityClass);
    return (int) result.getModifiedCount();
  }

  /**
   * Updates the object's status and processing metadata when it is unlocked after a processing run.
   *
   * <p>The update:
   *
   * <ul>
   *   <li>sets {@code status} to {@code newStatus},
   *   <li>clears {@code lockName} and {@code locked},
   *   <li>increments {@code processingTime} by {@code processingDuration}, and
   *   <li>sets {@code lastProcessed}, {@code nextProcessed}, {@code processed}, and {@code
   *       processingAttempts} to the supplied values.
   * </ul>
   *
   * @param id the identifier of the object to update
   * @param newStatus the new status to apply (for example, a PENDING, COMPLETED, or FAILED status)
   * @param processingDuration the time, in milliseconds, spent processing this object in the most
   *     recent attempt (added to the cumulative {@code processingTime})
   * @param lastProcessed the timestamp of the most recent processing attempt
   * @param nextProcessed the time when the object should next be considered for processing, or
   *     {@code null} if no further processing is scheduled
   * @param processed the time when processing completed successfully, or {@code null} if not
   *     applicable
   * @param processingAttempts the total number of processing attempts to persist for this object
   * @return the number of documents that were updated (either {@code 0} or {@code 1})
   */
  @Override
  public int updateOnUnlock(
      ID id,
      S newStatus,
      long processingDuration,
      OffsetDateTime lastProcessed,
      OffsetDateTime nextProcessed,
      OffsetDateTime processed,
      Integer processingAttempts) {

    Query query = Query.query(Criteria.where("_id").is(id));

    Update update =
        new Update()
            .set("status", newStatus)
            .unset("lockName")
            .unset("locked")
            .inc("processingTime", processingDuration)
            .set("lastProcessed", lastProcessed)
            .set("nextProcessed", nextProcessed)
            .set("processed", processed)
            .set("processingAttempts", processingAttempts);

    UpdateResult result = mongoTemplate.updateFirst(query, update, entityClass);
    return (int) result.getModifiedCount();
  }
}
