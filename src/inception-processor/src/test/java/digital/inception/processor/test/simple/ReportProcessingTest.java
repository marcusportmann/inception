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

package digital.inception.processor.test.simple;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import digital.inception.processor.AbstractProcessableObject;
import digital.inception.processor.BackgroundObjectProcessor;
import digital.inception.processor.ObjectProcessingResult;
import digital.inception.processor.ObjectProcessor;
import digital.inception.processor.ProcessableObjectStatus.ProcessingPhase;
import digital.inception.processor.RetryHandling;
import digital.inception.processor.test.simple.ReportProcessingTest.ReportProcessingTestConfig;
import digital.inception.processor.test.model.Report;
import digital.inception.processor.test.model.ReportStatus;
import java.time.OffsetDateTime;
import java.util.ArrayList;
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
    Report report = new Report();

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
    Report report = new Report();

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
   * always fail during {@link #process} to test retry and failure behavior.
   *
   * <p>It honors the {@link AbstractProcessableObject#getNextProcessed} field by only claiming
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

  @Configuration
  static class ReportProcessingTestConfig {}
}
