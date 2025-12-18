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

import digital.inception.processor.ProcessableObjectStatus.ProcessingPhase;
import java.time.OffsetDateTime;

/**
 * The {@code ObjectProcessingResult} value encapsulates the outcome of processing a single {@link
 * AbstractProcessableObject} instance.
 *
 * <p>It contains:
 *
 * <ul>
 *   <li>the next {@link ProcessableObjectStatus} that the object should transition to, and
 *   <li>the date and time when the object should next be considered for processing.
 * </ul>
 *
 * <p>The {@code nextProcessed} value is typically used to populate {@link
 * AbstractProcessableObject#setNextProcessed} so that later calls to {@link
 * ObjectProcessor#claimNextProcessableObject} can select objects whose next processing time has
 * been reached.
 *
 * @param nextStatus the status the object should be transitioned to
 * @param nextProcessed the date and time when processing should next be attempted for the object
 * @param <S> the status type for the processable object, which must implement {@link
 *     ProcessableObjectStatus}
 * @author Marcus Portmann
 */
@SuppressWarnings("unused")
public record ObjectProcessingResult<S extends ProcessableObjectStatus>(
    S nextStatus, OffsetDateTime nextProcessed) {

  /**
   * Convenience constructor that derives the next processing time from the status.
   *
   * <p>If the supplied {@code nextStatus} is in the {@link ProcessingPhase#PENDING} phase, {@code
   * nextProcessed} is initialized to {@link OffsetDateTime#now}, indicating that the object is
   * eligible for immediate processing.
   *
   * <p>For any other phase (for example {@link ProcessingPhase#COMPLETED}, or {@link
   * ProcessingPhase#FAILED}), this constructor sets {@code nextProcessed} to {@code null} to
   * indicate that no further scheduled processing is required.
   *
   * @param nextStatus the next status the processable object should transition to
   */
  public ObjectProcessingResult(S nextStatus) {
    this(
        nextStatus,
        nextStatus.getProcessingPhase() == ProcessingPhase.PENDING ? OffsetDateTime.now() : null);
  }
}
