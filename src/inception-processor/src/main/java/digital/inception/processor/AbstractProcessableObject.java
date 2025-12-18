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

package digital.inception.processor;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import digital.inception.core.xml.OffsetDateTimeAdapter;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import jakarta.validation.constraints.NotNull;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlSchemaType;
import jakarta.xml.bind.annotation.XmlTransient;
import jakarta.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.io.Serializable;
import java.time.OffsetDateTime;

/**
 * The {@code AbstractProcessableObject} class provides the common persistence and bookkeeping
 * fields for objects that are processed asynchronously by an {@link ObjectProcessor} and {@link
 * BackgroundObjectProcessor}.
 *
 * <p>Concrete processable objects (for example, {@code Event}, {@code Interaction}, or {@code
 * Correspondence}) should extend this class and specify:
 *
 * <ul>
 *   <li>their identifier type via the generic type parameter {@code ID}. The identifier type is
 *       typically something like {@link java.util.UUID}, {@link Long}, or {@link String} and must
 *       be {@link Serializable}, and
 *   <li>their status type via the generic type parameter {@code S}, which must be an enum
 *       implementing {@link ProcessableObjectStatus} that represents the states in the object's
 *       processing state machine.
 * </ul>
 *
 * <p>This base class does <strong>not</strong> impose any particular state machine or workflow
 * model. Instead, it provides shared metadata required for reliable processing:
 *
 * <ul>
 *   <li>lock information (who locked the object and when),
 *   <li>an explicit {@code status} field of type {@code S},
 *   <li>a processing attempt count for retry policies,
 *   <li>the total processing time,
 *   <li>timestamps for when the object was last processed and when processing completed,
 *   <li>a scheduled date and time indicating when the object should next be considered for
 *       processing, and
 *   <li>a flag indicating whether processing has been suspended for the object.
 * </ul>
 *
 * <p>The {@link ObjectProcessor} and {@link BackgroundObjectProcessor} implementations use these
 * fields to coordinate claiming, locking, processing, retrying, and unlocking processable objects
 * in a consistent way, while the concrete status type {@code S} defines the domain-specific states
 * and transitions.
 *
 * @param <ID> the type of the unique identifier for the processable object
 * @param <S> the type of the status for the processable object, which must be an enum implementing
 *     {@link ProcessableObjectStatus}
 * @author Marcus Portmann
 */
@MappedSuperclass
@SuppressWarnings({"unused"})
public abstract class AbstractProcessableObject<
    ID extends Serializable, S extends ProcessableObjectStatus> {

  /**
   * The date and time the last processing attempt was made for the object.
   *
   * <p>This field is updated each time an {@link ObjectProcessor} attempts to process the object,
   * regardless of whether the attempt was successful.
   */
  @Schema(description = "The date and time the last processing attempt was made")
  @JsonProperty
  @XmlElement(name = "LastProcessed")
  @XmlJavaTypeAdapter(OffsetDateTimeAdapter.class)
  @XmlSchemaType(name = "dateTime")
  @Column(name = "last_processed")
  private OffsetDateTime lastProcessed;

  /**
   * The name of the processing instance that has locked the object.
   *
   * <p>This is commonly the hostname, instance name, or node identifier of the process that claimed
   * the object. It can be used for logging, diagnostics, and manual intervention.
   */
  @Schema(description = "The name of the processing instance that applied the lock")
  @JsonProperty
  @XmlElement(name = "LockName")
  @Column(name = "lock_name", length = 100)
  private String lockName;

  /**
   * The date and time the object was locked for processing.
   *
   * <p>When an {@link ObjectProcessor} claims an object for processing, it should set this field to
   * the current date and time. This can be used both for debugging and for detecting stale or
   * abandoned locks.
   */
  @Schema(description = "The date and time the processing lock was applied")
  @JsonProperty
  @XmlElement(name = "Locked")
  @XmlJavaTypeAdapter(OffsetDateTimeAdapter.class)
  @XmlSchemaType(name = "dateTime")
  @Column(name = "locked")
  private OffsetDateTime locked;

  /**
   * The date and time when this object should next be considered for processing.
   *
   * <p>The {@link ObjectProcessor#claimNextProcessableObject()} implementation should only select
   * objects whose {@code nextProcessed} is less than or equal to the current date and time, in
   * addition to any status-based criteria (such as being in a PENDING phase).
   *
   * <p>This field allows the processing infrastructure to support configurable delays between
   * processing attempts, back-off strategies on failure, or scheduled/periodic processing.
   */
  @Schema(description = "The date and time when processing should next be attempted")
  @JsonProperty
  @XmlElement(name = "NextProcessed")
  @XmlJavaTypeAdapter(OffsetDateTimeAdapter.class)
  @XmlSchemaType(name = "dateTime")
  @Column(name = "next_processed")
  private OffsetDateTime nextProcessed;

  /**
   * The date and time the object completed processing successfully.
   *
   * <p>This field is typically set when the object transitions into a terminal {@link
   * ProcessableObjectStatus.ProcessingPhase#COMPLETED COMPLETED} status. It may be used for
   * auditing, reporting, or for applying retention policies.
   */
  @Schema(description = "The date and time processing was successfully completed")
  @JsonProperty
  @XmlElement(name = "Processed")
  @XmlJavaTypeAdapter(OffsetDateTimeAdapter.class)
  @XmlSchemaType(name = "dateTime")
  @Column(name = "processed")
  private OffsetDateTime processed;

  /**
   * The number of times processing has been attempted for the object.
   *
   * <p>This counter is normally incremented when the object is claimed and moved into a PROCESSING
   * status. The background processing infrastructure compares this value with {@link
   * ObjectProcessor#getMaxProcessingAttempts()} to decide whether to retry processing or transition
   * the object to a terminal failure status.
   *
   * <p>Typical usage is to reset this counter when moving from a PROCESSING status to a PENDING
   * status, and to retain it when moving from a PROCESSING status to a COMPLETED or FAILED status.
   */
  @Schema(
      description = "The number of processing attempts",
      requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElement(name = "ProcessingAttempts", required = true)
  @NotNull
  @Column(name = "processing_attempts", nullable = false)
  private int processingAttempts;

  /**
   * Flag indicating whether processing has been suspended for this object.
   *
   * <p>When {@code true}, the processing infrastructure or domain-specific logic may choose to skip
   * this object even if its status and {@code nextProcessed} timestamp would otherwise make it
   * eligible for processing.
   */
  @Schema(
      description = "Has processing been suspended for the object",
      requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElement(name = "ProcessingSuspended", required = true)
  @Column(name = "processing_suspended", nullable = false)
  private boolean processingSuspended;

  /**
   * The cumulative processing time, in milliseconds, for this object across all processing
   * attempts.
   *
   * <p>This value is intended to reflect the total wall-clock time spent in {@link
   * ObjectProcessor#process(AbstractProcessableObject)} for this instance, including both
   * successful and failed attempts. Each time the object is processed, the duration of that attempt
   * is added to the existing value.
   *
   * <p>The field is initialized to {@code 0} for newly created objects and grows monotonically as
   * additional processing work is performed.
   */
  @Schema(
      description = "The cumulative processing time in milliseconds across all processing attempts",
      requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElement(name = "ProcessingTime", required = true)
  @Column(name = "processing_time", nullable = false)
  private long processingTime = 0L;

  /**
   * The current status of the object within its processing state machine.
   *
   * <p>The concrete type {@code S} is supplied by subclasses as an {@code enum} implementing {@link
   * ProcessableObjectStatus}. The {@link ProcessableObjectStatus} contract exposes the {@link
   * ProcessableObjectStatus.ProcessingPhase}, which is used by the processing infrastructure (for
   * example, to distinguish between PENDING, PROCESSING, COMPLETED, and FAILED states).
   */
  @Schema(description = "The status of the object", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElement(name = "Status", required = true)
  @NotNull
  @Column(name = "status", nullable = false)
  private S status;

  /** Constructs a new {@code AbstractProcessableObject}. */
  protected AbstractProcessableObject() {}

  /**
   * Constructs a new {@code AbstractProcessableObject}.
   *
   * @param initialStatus the initial status for the object
   * @param nextProcessed the date and time when processing should next be attempted for the object
   */
  protected AbstractProcessableObject(S initialStatus, OffsetDateTime nextProcessed) {
    this.nextProcessed = nextProcessed;
    this.status = initialStatus;
  }

  /**
   * Constructs a new {@code AbstractProcessableObject}.
   *
   * <p>The {@code nextProcessed} field is initialized to the current date and time.
   *
   * @param initialStatus the initial status for the object
   */
  protected AbstractProcessableObject(S initialStatus) {
    this.nextProcessed = OffsetDateTime.now();
    this.status = initialStatus;
  }

  /**
   * Returns the unique identifier for the processable object.
   *
   * <p>Concrete subclasses must implement this method to expose their primary key so that the
   * processing infrastructure can reference and update specific rows in the underlying store.
   *
   * @return the unique identifier for the object
   */
  public abstract ID getId();

  /**
   * Returns a stable {@code String} key derived from this object's unique identifier.
   *
   * <p>This key is intended for internal use in caches, telemetry maps, and other structures that
   * require a {@code String}-based key. Implementations will typically derive this value from
   * {@link #getId()}, for example, by calling {@code String.valueOf(getId())} or using a
   * domain-specific formatting rule.
   *
   * <p>The returned value should:
   *
   * <ul>
   *   <li>uniquely identify this object within the relevant cache/telemetry context, and
   *   <li>remain stable for the lifetime of the object (i.e. not change between calls).
   * </ul>
   *
   * @return a {@code String} key derived from this object's identifier for use in caches and
   *     telemetry
   */
  @JsonIgnore
  @XmlTransient
  public abstract String getIdAsKey();

  /**
   * Returns the date and time the last processing attempt was made for the object.
   *
   * @return the timestamp of the last processing attempt, or {@code null} if the object has not
   *     been processed yet
   */
  public OffsetDateTime getLastProcessed() {
    return lastProcessed;
  }

  /**
   * Returns the name of the processing instance that has locked the object.
   *
   * @return the lock owner name, or {@code null} if the object is not currently locked
   */
  public String getLockName() {
    return lockName;
  }

  /**
   * Returns the date and time the object was locked for processing.
   *
   * @return the lock timestamp, or {@code null} if the object is not currently locked
   */
  public OffsetDateTime getLocked() {
    return locked;
  }

  /**
   * Returns the date and time when this object should next be considered for processing.
   *
   * @return the next processing time, or {@code null} if no next processing time has been scheduled
   */
  public OffsetDateTime getNextProcessed() {
    return nextProcessed;
  }

  /**
   * Returns the date and time the object completed processing successfully.
   *
   * @return the completion timestamp, or {@code null} if the object has not completed processing
   */
  public OffsetDateTime getProcessed() {
    return processed;
  }

  /**
   * Returns the number of times processing has been attempted for the object.
   *
   * <p>The counter is initialised to {@code 0} for newly created objects and incremented each time
   * the object is claimed for processing.
   *
   * @return the number of processing attempts
   */
  public int getProcessingAttempts() {
    return processingAttempts;
  }

  /**
   * Returns the total processing time in milliseconds for the object.
   *
   * @return the total processing time in milliseconds for the object
   */
  public long getProcessingTime() {
    return processingTime;
  }

  /**
   * Returns the current status of the object.
   *
   * @return the status of the object
   */
  public S getStatus() {
    return status;
  }

  /** Increments the number of times processing has been attempted for the object. */
  public void incrementProcessingAttempts() {
    processingAttempts = processingAttempts + 1;
  }

  /**
   * Returns whether processing has been suspended for this object.
   *
   * @return {@code true} if processing has been suspended for this object or {@code false}
   *     otherwise.
   */
  public boolean isProcessingSuspended() {
    return processingSuspended;
  }

  /**
   * Sets the date and time the last processing attempt was made for the object.
   *
   * @param lastProcessed the timestamp of the last processing attempt
   */
  public void setLastProcessed(OffsetDateTime lastProcessed) {
    this.lastProcessed = lastProcessed;
  }

  /**
   * Sets the name of the processing instance that has locked the object.
   *
   * @param lockName the lock owner name
   */
  public void setLockName(String lockName) {
    this.lockName = lockName;
  }

  /**
   * Sets the date and time the object was locked for processing.
   *
   * @param locked the lock timestamp
   */
  public void setLocked(OffsetDateTime locked) {
    this.locked = locked;
  }

  /**
   * Sets the date and time when this object should next be considered for processing.
   *
   * @param nextProcessed the next processing time
   */
  public void setNextProcessed(OffsetDateTime nextProcessed) {
    this.nextProcessed = nextProcessed;
  }

  /**
   * Sets the date and time the object completed processing successfully.
   *
   * @param processed the completion timestamp, or {@code null} if the object has not completed
   *     processing or the completion timestamp is being cleared
   */
  public void setProcessed(OffsetDateTime processed) {
    this.processed = processed;
  }

  /**
   * Sets the number of times processing has been attempted for the object.
   *
   * @param attempts the number of processing attempts
   */
  public void setProcessingAttempts(int attempts) {
    this.processingAttempts = attempts;
  }

  /**
   * Set whether processing has been suspended for this object.
   *
   * @param processingSuspended {@code true} if processing has been suspended for this object or
   *     {@code false} otherwise
   */
  public void setProcessingSuspended(boolean processingSuspended) {
    this.processingSuspended = processingSuspended;
  }

  /**
   * Sets the total processing time in milliseconds for the object.
   *
   * @param processingTime the total processing time in milliseconds for the object
   */
  public void setProcessingTime(long processingTime) {
    this.processingTime = processingTime;
  }

  /**
   * Sets the current status of the object.
   *
   * <p>The supplied status must be a value of the concrete status type {@code S}, which is expected
   * to implement {@link ProcessableObjectStatus} and participate in the domain-specific state
   * machine for this object.
   *
   * @param status the status of the object
   */
  public void setStatus(S status) {
    this.status = status;
  }
}
