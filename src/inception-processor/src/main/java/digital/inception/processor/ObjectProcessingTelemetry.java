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
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import digital.inception.core.xml.OffsetDateTimeAdapter;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlSchemaType;
import jakarta.xml.bind.annotation.XmlTransient;
import jakarta.xml.bind.annotation.XmlType;
import jakarta.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.io.Serial;
import java.io.Serializable;
import java.time.OffsetDateTime;

/**
 * The {@code ObjectProcessingTelemetry} class holds the telemetry for a single processing attempt
 * of a processable object.
 *
 * <p>A telemetry instance is created when the object is claimed and queued for processing and is
 * updated as the processing lifecycle progresses:
 *
 * <ul>
 *   <li>when the object is claimed (claim time, initial status, attempts),
 *   <li>when a worker thread actually starts processing (start time), and
 *   <li>when processing completes (end time, final status, attempts, outcome, failure cause).
 * </ul>
 *
 * <p>Instances are kept in an in-memory collection of "in-flight" objects while processing is
 * ongoing and removed once the attempt has completed.
 *
 * @author Marcus Portmann
 */
@Schema(description = "The telemetry for a single processing attempt of a processable object")
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
  "objectId",
  "processorName",
  "claimed",
  "processingStatus",
  "processingAttempts",
  "processingAttemptStarted",
  "processingAttemptCompleted",
  "processingAttemptDuration",
  "statusAfterProcessing"
})
@XmlRootElement(
    name = "ObjectProcessingTelemetry",
    namespace = "https://inception.digital/processing")
@XmlType(
    name = "ObjectProcessingTelemetry",
    namespace = "https://inception.digital/processing",
    propOrder = {
      "objectId",
      "processorName",
      "claimed",
      "processingStatus",
      "processingAttempts",
      "processingAttemptStarted",
      "processingAttemptCompleted",
      "processingAttemptDuration",
      "statusAfterProcessing"
    })
@XmlAccessorType(XmlAccessType.FIELD)
@SuppressWarnings({"unused"})
public class ObjectProcessingTelemetry implements Serializable {

  @Serial private static final long serialVersionUID = 1000000;

  /** The {@code String} representation of the ID for the object being processed. */
  @Schema(
      description = "The string representation of the ID for the object being processed",
      requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElement(name = "ObjectId", required = true)
  @NotNull
  private final String objectId;

  /**
   * The logical name of the processing instance (e.g. node or instance identifier) processing the
   * object.
   */
  @Schema(
      description =
          "The logical name of the processing instance (e.g. node or instance identifier) processing the object",
      requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElement(name = "ProcessorName", required = true)
  @NotNull
  private final String processorName;

  /** The date and time the object was claimed and locked for processing. */
  @Schema(
      description = "The date and time the object was claimed and locked for processing",
      requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElement(name = "Claimed", required = true)
  @XmlJavaTypeAdapter(OffsetDateTimeAdapter.class)
  @XmlSchemaType(name = "dateTime")
  @NotNull
  private OffsetDateTime claimed;

  /** The exception that caused the failure (if any). */
  @JsonIgnore @XmlTransient private Throwable failureCause;

  /** The next scheduled processing date and time after this attempt. */
  @Schema(description = "The next scheduled processing date and time after this attempt")
  @JsonProperty
  @XmlElement(name = "NextProcessed")
  @XmlJavaTypeAdapter(OffsetDateTimeAdapter.class)
  @XmlSchemaType(name = "dateTime")
  private OffsetDateTime nextProcessed;

  /** The date and time the object completed processing successfully. */
  @Schema(description = "The date and time the object completed processing successfully")
  @JsonProperty
  @XmlElement(name = "Processed")
  @XmlJavaTypeAdapter(OffsetDateTimeAdapter.class)
  @XmlSchemaType(name = "dateTime")
  private OffsetDateTime processed;

  /** The date and time the processing attempt completed (success or failure). */
  @Schema(description = "The date and time the processing attempt completed (success or failure)")
  @JsonProperty
  @XmlElement(name = "ProcessingAttemptCompleted")
  @XmlJavaTypeAdapter(OffsetDateTimeAdapter.class)
  @XmlSchemaType(name = "dateTime")
  private OffsetDateTime processingAttemptCompleted;

  /** The duration for the processing attempt in milliseconds. */
  @Schema(description = "The duration for the processing attempt in milliseconds")
  @JsonProperty
  @XmlElement(name = "ProcessingAttemptDuration")
  private Long processingAttemptDuration;

  /** The date and time the worker thread started processing the object. */
  @Schema(description = "The date and time the worker thread started processing the object")
  @JsonProperty
  @XmlElement(name = "ProcessingAttemptStarted")
  @XmlJavaTypeAdapter(OffsetDateTimeAdapter.class)
  @XmlSchemaType(name = "dateTime")
  private OffsetDateTime processingAttemptStarted;

  /** The processing attempts count for the object. */
  @Schema(
      description = "The processing attempts count for the object",
      requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElement(name = "ProcessingAttempts", required = true)
  @NotNull
  private int processingAttempts;

  /** The status of the object while processing. */
  @Schema(
      description = "The status of the object while processing",
      requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElement(name = "ProcessingStatus", required = true)
  private ProcessableObjectStatus processingStatus;

  /** The status of the object after the processing attempt completed. */
  @Schema(description = "The status of the object after the processing attempt completed")
  @JsonProperty
  @XmlElement(name = "StatusAfterProcessing")
  private ProcessableObjectStatus statusAfterProcessing;

  /**
   * Constructs a new {@code ObjectProcessingTelemetry}.
   *
   * @param objectId the {@code String} representation of the ID for the object being processed
   * @param processorName the logical name of the processing instance (e.g. node or instance
   *     identifier) processing the object
   */
  public ObjectProcessingTelemetry(String objectId, String processorName) {
    this.objectId = objectId;
    this.processorName = processorName;
  }

  /**
   * Returns the date and time the object was claimed and locked for processing.
   *
   * @return the date and time the object was claimed and locked for processing
   */
  public OffsetDateTime getClaimed() {
    return claimed;
  }

  /**
   * Returns the exception that caused the failure (if any).
   *
   * @return the exception that caused the failure (if any)
   */
  public Throwable getFailureCause() {
    return failureCause;
  }

  /**
   * Returns the next scheduled processing date and time after this attempt (may be {@code null}).
   *
   * @return the next scheduled processing date and time after this attempt (may be {@code null})
   */
  public OffsetDateTime getNextProcessed() {
    return nextProcessed;
  }

  /**
   * Returns the {@code String} representation of the ID for the object being processed.
   *
   * @return the {@code String} representation of the ID for the object being processed
   */
  public String getObjectId() {
    return objectId;
  }

  /**
   * Returns the date and time the object completed processing successfully (may be {@code null}).
   *
   * @return the date and time the object completed processing successfully (may be {@code null})
   */
  public OffsetDateTime getProcessed() {
    return processed;
  }

  /**
   * Returns the date and time the processing attempt completed (success or failure).
   *
   * @return the date and time the processing attempt completed (success or failure)
   */
  public OffsetDateTime getProcessingAttemptCompleted() {
    return processingAttemptCompleted;
  }

  /**
   * Returns the duration for the processing attempt in milliseconds.
   *
   * @return the duration for the processing attempt in milliseconds
   */
  public Long getProcessingAttemptDuration() {
    return processingAttemptDuration;
  }

  /**
   * Returns the current elapsed time for the processing attempt in milliseconds.
   *
   * @return the current elapsed time for the processing attempt in milliseconds
   */
  @Schema(
      description = "The current elapsed time for the processing attempt in milliseconds",
      requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElement(name = "ProcessingAttemptElapsedTime", required = true)
  public long getProcessingAttemptElapsedTime() {
    if (processingAttemptStarted == null) {
      return 0;
    }
    return System.currentTimeMillis() - processingAttemptStarted.toInstant().toEpochMilli();
  }

  /**
   * Returns the date and time the worker thread started processing the object.
   *
   * @return the date and time the worker thread started processing the object
   */
  public OffsetDateTime getProcessingAttemptStarted() {
    return processingAttemptStarted;
  }

  /**
   * Returns the processing attempts count before this attempt.
   *
   * @return the processing attempts count before this attempt
   */
  public int getProcessingAttempts() {
    return processingAttempts;
  }

  /**
   * Returns the status of the object before the processing attempt started.
   *
   * @return the status of the object before the processing attempt started
   */
  public ProcessableObjectStatus getProcessingStatus() {
    return processingStatus;
  }

  /**
   * Returns the logical name of the processing instance processing the object.
   *
   * @return the logical name of the processing instance processing the object
   */
  public String getProcessorName() {
    return processorName;
  }

  /**
   * Returns the status of the object after the processing attempt completed.
   *
   * @return the status of the object after the processing attempt completed
   */
  public ProcessableObjectStatus getStatusAfterProcessing() {
    return statusAfterProcessing;
  }

  /**
   * Sets the date and time the object was claimed and locked for processing.
   *
   * @param claimed the date and time the object was claimed and locked for processing
   */
  public void setClaimed(OffsetDateTime claimed) {
    this.claimed = claimed;
  }

  /**
   * Sets the exception that caused the failure.
   *
   * @param failureCause the exception that caused the failure
   */
  public void setFailureCause(Throwable failureCause) {
    this.failureCause = failureCause;
  }

  /**
   * Sets the next scheduled processing date and time after this attempt (may be {@code null}).
   *
   * @param nextProcessed the next scheduled processing date and time after this attempt (may be
   *     {@code null})
   */
  public void setNextProcessed(OffsetDateTime nextProcessed) {
    this.nextProcessed = nextProcessed;
  }

  /**
   * Sets the date and time the object completed processing successfully (may be {@code null}).
   *
   * @param processed the date and time the object completed processing successfully (may be {@code
   *     null})
   */
  public void setProcessed(OffsetDateTime processed) {
    this.processed = processed;
  }

  /**
   * Sets the date and time the processing attempt completed (success or failure).
   *
   * @param processingAttemptCompleted the date and time the processing attempt completed (success
   *     or failure)
   */
  public void setProcessingAttemptCompleted(OffsetDateTime processingAttemptCompleted) {
    this.processingAttemptCompleted = processingAttemptCompleted;
  }

  /**
   * Sets the duration for the processing attempt in milliseconds.
   *
   * @param processingAttemptDuration the duration for the processing attempt in milliseconds
   */
  public void setProcessingAttemptDuration(long processingAttemptDuration) {
    this.processingAttemptDuration = processingAttemptDuration;
  }

  /**
   * Sets the date and time the worker thread started processing the object.
   *
   * @param processingAttemptStarted the date and time the worker thread started processing the
   *     object
   */
  public void setProcessingAttemptStarted(OffsetDateTime processingAttemptStarted) {
    this.processingAttemptStarted = processingAttemptStarted;
  }

  /**
   * Sets the processing attempts count before this attempt.
   *
   * @param processingAttempts the processing attempts count before this attempt
   */
  public void setProcessingAttempts(int processingAttempts) {
    this.processingAttempts = processingAttempts;
  }

  /**
   * Sets the status of the object before the processing attempt started.
   *
   * @param processingStatus the status of the object before the processing attempt started
   */
  public void setProcessingStatus(ProcessableObjectStatus processingStatus) {
    this.processingStatus = processingStatus;
  }

  /**
   * Sets the status of the object after the processing attempt completed.
   *
   * @param statusAfterProcessing the status of the object after the processing attempt completed
   */
  public void setStatusAfterProcessing(ProcessableObjectStatus statusAfterProcessing) {
    this.statusAfterProcessing = statusAfterProcessing;
  }
}
