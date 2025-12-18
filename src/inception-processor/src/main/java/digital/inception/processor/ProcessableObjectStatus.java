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

import digital.inception.core.model.CodeEnum;

/**
 * The {@code ProcessableObjectStatus} interface defines the lifecycle status for a {@link
 * AbstractProcessableObject} within a logical processing state machine.
 *
 * <p>Each processable object participates in a state machine where the concrete status values
 * (typically an {@code enum} such as {@code EventStatus} or {@code InteractionStatus}) represent
 * the distinct <em>states</em> in that machine. The status values are grouped into a small set of
 * high-level processing phases:
 *
 * <ul>
 *   <li>{@link ProcessingPhase#PENDING PENDING} – states in which the object is eligible to be
 *       selected for processing. A transition from a pending state into a processing state usually
 *       occurs when an {@code ObjectProcessor} claims the object.
 *   <li>{@link ProcessingPhase#PROCESSING PROCESSING} – states in which the object is currently
 *       being processed. While in a processing state, the object is locked for exclusive
 *       processing. From a processing state, the state machine may transition to another pending
 *       state (for a later state), a completed state, or a failed state.
 *   <li>{@link ProcessingPhase#COMPLETED COMPLETED} – terminal states indicating that the object
 *       has successfully reached the end of its processing state machine. No further automatic
 *       transitions are expected.
 *   <li>{@link ProcessingPhase#FAILED FAILED} – terminal states indicating that processing for the
 *       object has failed and will not be automatically retried. Any further handling is expected
 *       to be manual or performed by a separate state machine.
 * </ul>
 *
 * <p>Implementations of this interface are typically {@code enum} types on concrete processable
 * objects and may define multiple status values within each processing phase. This allows you to
 * model multi-state processing flows (e.g., validation, enrichment, indexing) as explicit
 * transitions in a state machine while still enabling the infrastructure to reason about statuses
 * in terms of the broader {@link ProcessingPhase}.
 *
 * <p>The {@code ObjectProcessor} and background processing infrastructure use {@link
 * #getProcessingPhase} to:
 *
 * <ul>
 *   <li>Identify which states are considered pending and therefore eligible for selection.
 *   <li>Distinguish processing states from terminal completed/failed states.
 *   <li>Apply generic behaviors such as locking, retry handling, and terminal-state handling, while
 *       leaving the concrete state transitions (e.g., which state to move to next) under the
 *       control of the {@code ObjectProcessor} implementation.
 * </ul>
 *
 * @author Marcus Portmann
 */
public interface ProcessableObjectStatus extends CodeEnum {

  /**
   * Returns the high-level processing phase that this status belongs to within the object's
   * processing state machine.
   *
   * @return the processing phase for this processable object status
   */
  ProcessingPhase getProcessingPhase();

  /**
   * The high-level processing phase that a processable object status belongs to within its state
   * machine.
   */
  enum ProcessingPhase {
    /**
     * States in which the object is waiting to be processed and can be selected for execution by an
     * {@code ObjectProcessor}.
     */
    PENDING,

    /**
     * States in which the object is actively being processed and typically held under an execution
     * lock.
     */
    PROCESSING,

    /**
     * Terminal states representing successful completion of the object's processing state machine.
     */
    COMPLETED,

    /**
     * Terminal states representing an unrecoverable failure in the object's processing state
     * machine.
     */
    FAILED,

    /**
     * States in which the object is waiting for system or user intervention before processing can
     * continue.
     */
    WAITING
  }
}
