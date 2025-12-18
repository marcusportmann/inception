/*
 * Copyright (c) Discovery Ltd. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Discovery Ltd
 * ("Confidential Information"). It may not be copied or reproduced in any manner
 * without the express written permission of Discovery Ltd.
 */

package digital.inception.processor.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import digital.inception.processor.AbstractProcessableObject;
import digital.inception.processor.BackgroundObjectProcessor;
import digital.inception.processor.ObjectProcessingResult;
import digital.inception.processor.ObjectProcessor;
import digital.inception.processor.ProcessableObjectStatus;
import digital.inception.processor.ProcessableObjectStatus.ProcessingPhase;
import digital.inception.processor.RetryHandling;
import digital.inception.processor.test.ReportProcessingTest.ReportProcessingTestConfig;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

/**
 * The {@code ReportProcessingTest} class.
 *
 * @author Marcus Portmann
 */
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = ReportProcessingTestConfig.class)
@SuppressWarnings({"unused", "FieldCanBeLocal"})
public class ReportProcessingTest {

  @Autowired private ApplicationContext applicationContext;

  /**
   * Happy-path test that drives the report through multiple states until it reaches DELIVERED
   * (COMPLETED phase).
   */
  @Test
  void testReportHappyPathDelivered() throws Exception {
    Report report = new Report(1L);

    InMemoryReportProcessor inMemoryReportProcessor =
        new InMemoryReportProcessor(List.of(report), false);
    BackgroundReportProcessor backgroundReportProcessor =
        new BackgroundReportProcessor(
            applicationContext, inMemoryReportProcessor, 2, 10, 0, 1_000L);
    backgroundReportProcessor.start();

    int safety = 60;

    while (isNotTerminal(report) && safety-- > 0) {
      backgroundReportProcessor.processObjects();
      backgroundReportProcessor.waitUntilIdle(2_000L);
    }

    // Catch early-exit cases explicitly
    assertTrue(safety > 0, "Safety counter exhausted before reaching a terminal status");

    // The test state machine is configured to end at DELIVERED on success.
    assertEquals(ReportStatus.DELIVERED, report.getStatus());
    assertTrue(backgroundReportProcessor.getSuccessCount() > 0);
    assertEquals(1, report.getProcessingAttempts());
    assertNull(report.getNextProcessed());
  }

  /**
   * Test that a report which always fails to process eventually transitions to a FAILED status
   * after the maximum number of attempts.
   */
  @Test
  void testReportPermanentFailureAfterMaxAttempts() throws Exception {
    Report report = new Report(2L);

    InMemoryReportProcessor inMemoryReportProcessor =
        new InMemoryReportProcessor(List.of(report), true);
    BackgroundReportProcessor backgroundReportProcessor =
        new BackgroundReportProcessor(
            applicationContext, inMemoryReportProcessor, 1, 10, 0, 1_000L);
    backgroundReportProcessor.start();

    int safety = 60;

    while (isNotTerminal(report) && safety-- > 0) {
      backgroundReportProcessor.processObjects();
      backgroundReportProcessor.waitUntilIdle(2_000L);
    }

    assertEquals(ProcessingPhase.FAILED, report.getStatus().getProcessingPhase());
    assertEquals(ReportStatus.FAILED, report.getStatus());
    assertTrue(
        report.getProcessingAttempts() >= inMemoryReportProcessor.getMaxProcessingAttempts());
    assertTrue(backgroundReportProcessor.getPermanentFailureCount() > 0);
    assertNull(report.getNextProcessed());
  }

  private boolean isNotTerminal(Report report) {
    ProcessingPhase phase = report.getStatus().getProcessingPhase();
    return phase != ProcessingPhase.COMPLETED && phase != ProcessingPhase.FAILED;
  }

  /**
   * The {@code ReportStatus} enumeration defines the possible statuses for a report and maps each
   * status to a high-level {@link ProcessingPhase} for use by the processing infrastructure.
   */
  @Schema(description = "The report status")
  public enum ReportStatus implements ProcessableObjectStatus {

    // PENDING
    REQUESTED("requested", ProcessingPhase.PENDING, "Requested"),
    GENERATION_INITIATED("generation_initiated", ProcessingPhase.PENDING, "Generation Initiated"),
    QUEUED_FOR_PUBLISHING(
        "queued_for_publishing", ProcessingPhase.PENDING, "Queued For Publishing"),
    QUEUED_FOR_SENDING("queued_for_sending", ProcessingPhase.PENDING, "Queued For Sending"),
    SEND_INITIATED("send_initiated", ProcessingPhase.PENDING, "Send Initiated"),
    CONFIRM_DELIVERY("confirm_delivery", ProcessingPhase.PENDING, "Confirm Delivery"),

    // PROCESSING
    GENERATING("generating", ProcessingPhase.PROCESSING, "Generating"),
    VERIFYING_GENERATION(
        "verifying_generation", ProcessingPhase.PROCESSING, "Verifying Generation"),
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

  /**
   * Test {@link BackgroundObjectProcessor} specialization that tracks how many successes and
   * permanent failures occur, and provides a method to wait until the executor is idle (no active
   * tasks, empty queue).
   */
  public static class BackgroundReportProcessor
      extends BackgroundObjectProcessor<Report, ReportStatus> {

    private int permanentFailureCount;

    private int successCount;

    public BackgroundReportProcessor(
        ApplicationContext applicationContext,
        ObjectProcessor<Report, ReportStatus> objectProcessor,
        int processingThreadCount,
        int maximumQueueLength,
        long lockTimeoutSeconds,
        long processingTimeoutMillis) {

      super(
          applicationContext,
          objectProcessor,
          processingThreadCount,
          maximumQueueLength,
          lockTimeoutSeconds,
          processingTimeoutMillis);
    }

    public int getPermanentFailureCount() {
      return permanentFailureCount;
    }

    public int getSuccessCount() {
      return successCount;
    }

    /**
     * Block until the underlying executor has no active tasks and the queue is empty, or until the
     * timeout is reached.
     *
     * @param timeoutMillis maximum time to wait
     * @throws InterruptedException if interrupted while waiting
     */
    public void waitUntilIdle(long timeoutMillis) throws InterruptedException {
      long deadline = System.currentTimeMillis() + timeoutMillis;

      while (System.currentTimeMillis() < deadline) {
        if (!hasQueuedOrActiveObjects()) {
          return;
        }

        //noinspection BusyWait
        Thread.sleep(10L);
      }

      // If we get here, we timed-out – for tests this is a failure scenario.
      throw new IllegalStateException("Executor did not become idle within timeout");
    }

    @Override
    protected void afterPermanentFailure(
        Report object, ReportStatus fromStatus, ReportStatus toStatus, Throwable cause) {
      permanentFailureCount++;
    }

    @Override
    protected void afterSuccess(
        Report object, ReportStatus fromStatus, ReportStatus toStatus, long processingDuration) {
      successCount++;
    }

    @Override
    protected void resetStaleLocks(long lockTimeoutSeconds) {
      // For tests, do nothing. In production, you would move long-held PROCESSING statuses
      // back to appropriate PENDING statuses based on lock age.
    }
  }

  /**
   * In-memory {@link ObjectProcessor} implementation for {@link Report} used in tests.
   *
   * <p>This processor uses a simple, deterministic state machine to drive a report from REQUESTED
   * through various intermediate states to DELIVERED on success. It can also be configured to
   * always fail during {@link #process} to test retry and failure behaviour.
   *
   * <p>It honours the {@link AbstractProcessableObject#getNextProcessed} field by only claiming
   * reports whose next processing time is due (i.e., {@code null} or less than or equal to now).
   */
  public static class InMemoryReportProcessor implements ObjectProcessor<Report, ReportStatus> {

    // Map PENDING states to the PROCESSING state used when claiming.
    private static final Map<ReportStatus, ReportStatus> CLAIM_TRANSITIONS =
        Map.ofEntries(
            Map.entry(ReportStatus.REQUESTED, ReportStatus.GENERATING),
            Map.entry(ReportStatus.GENERATION_INITIATED, ReportStatus.VERIFYING_GENERATION),
            Map.entry(ReportStatus.QUEUED_FOR_PUBLISHING, ReportStatus.PUBLISHING),
            Map.entry(ReportStatus.QUEUED_FOR_SENDING, ReportStatus.SENDING),
            Map.entry(ReportStatus.SEND_INITIATED, ReportStatus.VERIFYING_SENDING),
            Map.entry(ReportStatus.CONFIRM_DELIVERY, ReportStatus.CONFIRMING_DELIVERY));

    // Map PROCESSING states back to PENDING on retry.
    private static final Map<ReportStatus, ReportStatus> RETRY_TRANSITIONS =
        Map.ofEntries(
            Map.entry(ReportStatus.GENERATING, ReportStatus.REQUESTED),
            Map.entry(ReportStatus.VERIFYING_GENERATION, ReportStatus.GENERATION_INITIATED),
            Map.entry(ReportStatus.PUBLISHING, ReportStatus.QUEUED_FOR_PUBLISHING),
            Map.entry(ReportStatus.SENDING, ReportStatus.QUEUED_FOR_SENDING),
            Map.entry(ReportStatus.VERIFYING_SENDING, ReportStatus.SEND_INITIATED),
            Map.entry(ReportStatus.CONFIRMING_DELIVERY, ReportStatus.CONFIRM_DELIVERY));

    // Map PROCESSING states to the next status on successful processing.
    private static final Map<ReportStatus, ReportStatus> SUCCESS_TRANSITIONS =
        Map.ofEntries(
            Map.entry(ReportStatus.GENERATING, ReportStatus.GENERATION_INITIATED),
            Map.entry(ReportStatus.VERIFYING_GENERATION, ReportStatus.QUEUED_FOR_PUBLISHING),
            Map.entry(ReportStatus.PUBLISHING, ReportStatus.QUEUED_FOR_SENDING),
            Map.entry(ReportStatus.SENDING, ReportStatus.SEND_INITIATED),
            Map.entry(ReportStatus.VERIFYING_SENDING, ReportStatus.CONFIRM_DELIVERY),
            Map.entry(ReportStatus.CONFIRMING_DELIVERY, ReportStatus.DELIVERED));

    private final boolean alwaysFail;

    private final int maxAttempts = 3;

    private final List<Report> store = new CopyOnWriteArrayList<>();

    public InMemoryReportProcessor(List<Report> reports, boolean alwaysFail) {
      this.store.addAll(reports);
      this.alwaysFail = alwaysFail;
    }

    @Override
    public Optional<Report> claimNextProcessableObject() {
      OffsetDateTime now = OffsetDateTime.now();

      for (Report report : store) {
        ReportStatus status = report.getStatus();

        if (status.getProcessingPhase() != ProcessingPhase.PENDING) {
          continue;
        }

        OffsetDateTime nextProcessed = report.getNextProcessed();
        // Eligible if nextProcessed <= now.
        boolean due = (nextProcessed != null) && now.isAfter(nextProcessed);

        if (due) {
          ReportStatus processing = CLAIM_TRANSITIONS.getOrDefault(status, status);
          report.setStatus(processing);
          report.incrementProcessingAttempts();
          report.setLocked(now);
          report.setLockName("test-instance");
          report.setLastProcessed(now);
          return Optional.of(report);
        }
      }
      return Optional.empty();
    }

    @Override
    public ReportStatus determineFailureStatus(
        Report object, ReportStatus currentStatus, Throwable cause) {
      return ReportStatus.FAILED;
    }

    @Override
    public RetryHandling<ReportStatus> determineRetryHandling(
        Report object, ReportStatus currentStatus, Throwable cause) {
      return RetryHandling.immediate(
          RETRY_TRANSITIONS.getOrDefault(currentStatus, ReportStatus.FAILED));
    }

    @Override
    public int getMaxProcessingAttempts() {
      return maxAttempts;
    }

    @Override
    public List<ReportStatus> getPendingStatuses() {
      return new ArrayList<>(ReportStatus.pendingStatuses());
    }

    @Override
    public ObjectProcessingResult<ReportStatus> process(Report report) {
      System.out.println("process(): " + report.getStatus());

      if (alwaysFail) {
        throw new RuntimeException("Simulated processing failure");
      }

      ReportStatus currentStatus = report.getStatus();

      // If we have a configured transition, use it.
      if (SUCCESS_TRANSITIONS.containsKey(currentStatus)) {
        ReportStatus nextStatus = SUCCESS_TRANSITIONS.get(currentStatus);
        return new ObjectProcessingResult<>(nextStatus);
      }

      // If already in a COMPLETED or FAILED phase, stay where we are and do not schedule further.
      ProcessingPhase phase = currentStatus.getProcessingPhase();
      if (phase == ProcessingPhase.COMPLETED || phase == ProcessingPhase.FAILED) {
        return new ObjectProcessingResult<>(currentStatus, null);
      }

      // Otherwise leave as-is.
      return new ObjectProcessingResult<>(currentStatus);
    }

    @Override
    public void unlockProcessableObject(
        Report object,
        ReportStatus newStatus,
        long processingDuration,
        OffsetDateTime nextProcessed,
        boolean resetProcessingAttempts) {
      System.out.println("unlock(): " + object.getStatus() + " -> " + newStatus);

      object.setStatus(newStatus);
      object.setLockName(null);
      object.setLocked(null);
      object.setLastProcessed(OffsetDateTime.now());
      object.setProcessingTime(object.getProcessingTime() + processingDuration);
      object.setNextProcessed(nextProcessed);

      if (resetProcessingAttempts) {
        object.setProcessingAttempts(0);
      }

      if (newStatus.getProcessingPhase() == ProcessingPhase.COMPLETED) {
        object.setProcessed(object.getLastProcessed());
        object.setNextProcessed(null);
      }
    }
  }

  /**
   * Simple report domain object used for tests. In production this would be a JPA entity extending
   * {@code AbstractProcessableObject<Long>}.
   */
  public static class Report extends AbstractProcessableObject<Long, ReportStatus> {

    private final Long id;

    public Report(Long id) {
      super(ReportStatus.REQUESTED);
      this.id = id;
    }

    @Override
    public Long getId() {
      return id;
    }

    @Override
    public String getIdAsKey() {
      if (id != null) {
        return String.valueOf(id);
      } else {
        return "";
      }
    }
  }

  @Configuration
  static class ReportProcessingTestConfig {}
}
