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

package digital.inception.processor.test.model;

import digital.inception.processor.ProcessableObjectStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.EnumSet;

/**
 * The {@code ReportStatus} enumeration defines the possible statuses for a report and maps each
 * status to a high-level {@link ProcessingPhase} for use by the processing infrastructure.
 *
 * @author Marcus Portmann
 */
@Schema(description = "The report status")
public enum ReportStatus implements ProcessableObjectStatus {

  // PENDING
  REQUESTED("requested", ProcessingPhase.PENDING, "Requested"),
  GENERATION_INITIATED("generation_initiated", ProcessingPhase.PENDING, "Generation Initiated"),
  QUEUED_FOR_PUBLISHING("queued_for_publishing", ProcessingPhase.PENDING, "Queued For Publishing"),
  QUEUED_FOR_SENDING("queued_for_sending", ProcessingPhase.PENDING, "Queued For Sending"),
  SEND_INITIATED("send_initiated", ProcessingPhase.PENDING, "Send Initiated"),
  CONFIRM_DELIVERY("confirm_delivery", ProcessingPhase.PENDING, "Confirm Delivery"),

  // PROCESSING
  GENERATING("generating", ProcessingPhase.PROCESSING, "Generating"),
  VERIFYING_GENERATION("verifying_generation", ProcessingPhase.PROCESSING, "Verifying Generation"),
  PUBLISHING("publishing", ProcessingPhase.PROCESSING, "Publishing"),
  SENDING("sending", ProcessingPhase.PROCESSING, "Sending"),
  VERIFYING_SENDING("verifying_sending", ProcessingPhase.PROCESSING, "Verifying Sending"),
  CONFIRMING_DELIVERY("confirming_delivery", ProcessingPhase.PROCESSING, "Confirming Delivery"),

  // COMPLETED
  PUBLISHED("published", ProcessingPhase.COMPLETED, "Published"),
  SENT("sent", ProcessingPhase.COMPLETED, "Sent"),
  DELIVERED("delivered", ProcessingPhase.COMPLETED, "Delivered"),

  // FAILED
  REJECTED("rejected", ProcessingPhase.FAILED, "Rejected"),
  UNDELIVERABLE("undeliverable", ProcessingPhase.FAILED, "Undeliverable"),
  FAILED("failed", ProcessingPhase.FAILED, "Failed");

  private final String code;
  private final String description;
  private final ProcessingPhase processingPhase;

  ReportStatus(String code, ProcessingPhase processingPhase, String description) {
    this.code = code;
    this.processingPhase = processingPhase;
    this.description = description;
  }

  /**
   * Returns the set of statuses that represent PENDING states in the report state machine.
   *
   * @return the set of PENDING statuses
   */
  public static EnumSet<ReportStatus> pendingStatuses() {
    EnumSet<ReportStatus> set = EnumSet.noneOf(ReportStatus.class);
    for (ReportStatus status : ReportStatus.values()) {
      if (status.processingPhase == ProcessingPhase.PENDING) {
        set.add(status);
      }
    }
    return set;
  }

  @Override
  public String code() {
    return code;
  }

  @Override
  public String description() {
    return description;
  }

  @Override
  public ProcessingPhase getProcessingPhase() {
    return processingPhase;
  }

  @Override
  public String toString() {
    return description;
  }
}
