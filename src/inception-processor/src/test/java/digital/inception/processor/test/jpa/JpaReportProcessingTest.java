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

package digital.inception.processor.test.jpa;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import digital.inception.jpa.JpaUtil;
import digital.inception.processor.BackgroundObjectProcessor;
import digital.inception.processor.ObjectProcessingResult;
import digital.inception.processor.ObjectProcessor;
import digital.inception.processor.ProcessableObjectStatus.ProcessingPhase;
import digital.inception.processor.RetryHandling;
import digital.inception.processor.persistence.jpa.JpaRepositoryBackedBackgroundObjectProcessor;
import digital.inception.processor.persistence.jpa.JpaRepositoryBackedObjectProcessor;
import digital.inception.processor.persistence.jpa.ProcessableObjectJpaRepository;
import digital.inception.processor.test.jpa.JpaReportProcessingTest.JpaReportProcessingTestConfig;
import digital.inception.processor.test.model.Report;
import digital.inception.processor.test.model.ReportStatus;
import digital.inception.test.InceptionExtension;
import digital.inception.test.TestConfiguration;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.UUID;
import javax.sql.DataSource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.ConfigDataApplicationContextInitializer;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;

/**
 * JPA-backed equivalent of {@code ReportProcessingTest}, using the H2 in-memory database to verify
 * the {@link JpaRepositoryBackedObjectProcessor} / {@link BackgroundObjectProcessor} integration.
 *
 * @author Marcus Portmann
 */
@ExtendWith(SpringExtension.class)
@ExtendWith(InceptionExtension.class)
@ContextConfiguration(
    classes = {TestConfiguration.class, JpaReportProcessingTestConfig.class},
    initializers = {ConfigDataApplicationContextInitializer.class})
@TestExecutionListeners(
    listeners = {
      DependencyInjectionTestExecutionListener.class,
      DirtiesContextTestExecutionListener.class,
      TransactionalTestExecutionListener.class
    })
public class JpaReportProcessingTest {

  @Autowired private ApplicationContext applicationContext;

  @Autowired private JpaReportRepository jpaReportRepository;

  @BeforeEach
  void cleanDatabase() {
    jpaReportRepository.deleteAll();
  }

  /**
   * Happy-path test that drives the report through multiple states until it reaches DELIVERED
   * (COMPLETED phase) using the JPA-backed processor.
   */
  @Test
  void testJpaReportHappyPathDelivered() throws Exception {
    Report report = jpaReportRepository.save(new Report());

    JpaBackgroundReportProcessor backgroundReportProcessor =
        new JpaBackgroundReportProcessor(
            applicationContext, jpaReportRepository, 2, 10, 0, 1_000L, 3, 1_000L, false);

    backgroundReportProcessor.start();

    int safety = 60;

    while (isNotTerminal(report) && safety-- > 0) {
      backgroundReportProcessor.processObjects();
      backgroundReportProcessor.waitUntilIdle(2_000L);

      // Reload from database to observe persisted changes
      report = jpaReportRepository.findById(report.getId()).orElseThrow();
    }

    assertTrue(safety > 0, "Safety counter exhausted before reaching a terminal status");

    assertEquals(ReportStatus.DELIVERED, report.getStatus());
    assertTrue(backgroundReportProcessor.getSuccessCount() > 0);
    assertEquals(1, report.getProcessingAttempts());
    assertNull(report.getNextProcessed());
  }

  /**
   * Test that a report which always fails to process eventually transitions to a FAILED status
   * after the maximum number of attempts, using JPA-backed processing.
   */
  @Test
  void testJpaReportPermanentFailureAfterMaxAttempts() throws Exception {
    Report report = jpaReportRepository.save(new Report());

    JpaBackgroundReportProcessor backgroundReportProcessor =
        new JpaBackgroundReportProcessor(
            applicationContext, jpaReportRepository, 2, 10, 0, 1_000L, 3, 1_000L, true);

    backgroundReportProcessor.start();

    int safety = 60;

    while (isNotTerminal(report) && safety-- > 0) {
      backgroundReportProcessor.processObjects();
      backgroundReportProcessor.waitUntilIdle(2_000L);

      // Reload from database to observe persisted changes
      report = jpaReportRepository.findById(report.getId()).orElseThrow();
    }

    assertTrue(safety > 0, "Safety counter exhausted before reaching a terminal status");

    assertEquals(ProcessingPhase.FAILED, report.getStatus().getProcessingPhase());
    assertEquals(ReportStatus.FAILED, report.getStatus());
    assertTrue(
        report.getProcessingAttempts()
            >= backgroundReportProcessor.getObjectProcessor().getMaxProcessingAttempts());
    assertTrue(backgroundReportProcessor.getPermanentFailureCount() > 0);
    assertNull(report.getNextProcessed());
  }

  /**
   * Test that a report marked as "processing suspended" is not processed, using JPA-backed
   * processing.
   */
  @Test
  void testJpaReportProcessingSuspended() throws Exception {
    Report report = new Report();

    report.setProcessingSuspended(true);

    jpaReportRepository.save(report);

    JpaBackgroundReportProcessor backgroundReportProcessor =
        new JpaBackgroundReportProcessor(
            applicationContext, jpaReportRepository, 2, 10, 0, 1_000L, 3, 1_000L, true);

    backgroundReportProcessor.start();

    int safety = 60;

    while (isNotTerminal(report) && safety-- > 0) {
      backgroundReportProcessor.processObjects();
      backgroundReportProcessor.waitUntilIdle(2_000L);

      // Reload from database to observe persisted changes
      report = jpaReportRepository.findById(report.getId()).orElseThrow();
    }

    assertEquals(ProcessingPhase.PENDING, report.getStatus().getProcessingPhase());
    assertEquals(ReportStatus.REQUESTED, report.getStatus());
    assertEquals(0, report.getProcessingAttempts());
    assertNotNull(report.getNextProcessed());
  }

  private boolean isNotTerminal(Report report) {
    ProcessingPhase phase = report.getStatus().getProcessingPhase();
    return phase != ProcessingPhase.COMPLETED && phase != ProcessingPhase.FAILED;
  }

  /**
   * Spring Data JPA repository for {@link Report}, extending the {@link
   * ProcessableObjectJpaRepository} interface.
   */
  public interface JpaReportRepository
      extends ProcessableObjectJpaRepository<Report, UUID, ReportStatus> {}

  /**
   * Test {@link JpaRepositoryBackedBackgroundObjectProcessor} specialization that tracks how many
   * successes and permanent failures occur, and provides a method to wait until the executor is
   * idle (no active tasks, empty queue), using JPA-backed reports.
   */
  public static class JpaBackgroundReportProcessor
      extends JpaRepositoryBackedBackgroundObjectProcessor<
          Report, UUID, ReportStatus, JpaReportRepository> {

    private int permanentFailureCount;
    private int successCount;

    public JpaBackgroundReportProcessor(
        ApplicationContext applicationContext,
        JpaReportRepository jpaReportRepository,
        int processingThreadCount,
        int maximumQueueLength,
        long lockTimeoutSeconds,
        long processingTimeoutMillis,
        int maximumProcessingAttempts,
        long retryDelayMillis,
        boolean alwaysFail) {

      super(
          applicationContext,
          jpaReportRepository,
          new JpaReportProcessor(
              jpaReportRepository, maximumProcessingAttempts, retryDelayMillis, alwaysFail),
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

        // noinspection BusyWait
        Thread.sleep(10L);
      }

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
    protected Map<ReportStatus, ReportStatus> getPendingToProcessingStatusMappings() {
      return JpaReportProcessor.PROCESSING_STATUS_TRANSITIONS;
    }
  }

  @Configuration
  @EnableJpaRepositories(
      basePackages = {"digital.inception.processor.test.jpa"},
      considerNestedRepositories = true,
      entityManagerFactoryRef = "jpaReportProcessingEntityManagerFactory")
  static class JpaReportProcessingTestConfig {
    @Bean
    public LocalContainerEntityManagerFactoryBean jpaReportProcessingEntityManagerFactory(
        ApplicationContext applicationContext,
        @Qualifier("applicationDataSource") DataSource dataSource) {
      return JpaUtil.createEntityManager(
          applicationContext,
          "jpaReportProcessing",
          dataSource,
          "digital.inception.processor.test.model");
    }
  }

  /**
   * JPA-backed {@link ObjectProcessor} implementation for {@link Report} used in tests.
   *
   * <p>This mirrors the in-memory processor but delegates persistence and locking to {@link
   * JpaRepositoryBackedObjectProcessor} and {@link JpaReportRepository}.
   */
  public static class JpaReportProcessor
      extends JpaRepositoryBackedObjectProcessor<Report, UUID, ReportStatus, JpaReportRepository> {

    // Map PENDING states to the PROCESSING state used when claiming.
    public static final Map<ReportStatus, ReportStatus> PROCESSING_STATUS_TRANSITIONS =
        Map.ofEntries(
            Map.entry(ReportStatus.REQUESTED, ReportStatus.GENERATING),
            Map.entry(ReportStatus.GENERATION_INITIATED, ReportStatus.VERIFYING_GENERATION),
            Map.entry(ReportStatus.QUEUED_FOR_PUBLISHING, ReportStatus.PUBLISHING),
            Map.entry(ReportStatus.QUEUED_FOR_SENDING, ReportStatus.SENDING),
            Map.entry(ReportStatus.SEND_INITIATED, ReportStatus.VERIFYING_SENDING),
            Map.entry(ReportStatus.CONFIRM_DELIVERY, ReportStatus.CONFIRMING_DELIVERY));

    // Map PROCESSING states back to PENDING on retry.
    public static final Map<ReportStatus, ReportStatus> RETRY_STATUS_TRANSITIONS =
        Map.ofEntries(
            Map.entry(ReportStatus.GENERATING, ReportStatus.REQUESTED),
            Map.entry(ReportStatus.VERIFYING_GENERATION, ReportStatus.GENERATION_INITIATED),
            Map.entry(ReportStatus.PUBLISHING, ReportStatus.QUEUED_FOR_PUBLISHING),
            Map.entry(ReportStatus.SENDING, ReportStatus.QUEUED_FOR_SENDING),
            Map.entry(ReportStatus.VERIFYING_SENDING, ReportStatus.SEND_INITIATED),
            Map.entry(ReportStatus.CONFIRMING_DELIVERY, ReportStatus.CONFIRM_DELIVERY));

    // Map PROCESSING states to the next status on successful processing.
    public static final Map<ReportStatus, ReportStatus> SUCCESS_STATUS_TRANSITIONS =
        Map.ofEntries(
            Map.entry(ReportStatus.GENERATING, ReportStatus.GENERATION_INITIATED),
            Map.entry(ReportStatus.VERIFYING_GENERATION, ReportStatus.QUEUED_FOR_PUBLISHING),
            Map.entry(ReportStatus.PUBLISHING, ReportStatus.QUEUED_FOR_SENDING),
            Map.entry(ReportStatus.SENDING, ReportStatus.SEND_INITIATED),
            Map.entry(ReportStatus.VERIFYING_SENDING, ReportStatus.CONFIRM_DELIVERY),
            Map.entry(ReportStatus.CONFIRMING_DELIVERY, ReportStatus.DELIVERED));

    private final boolean alwaysFail;

    @SuppressWarnings({"FieldCanBeLocal", "unused"})
    private final long retryDelayMillis;

    public JpaReportProcessor(
        JpaReportRepository repository,
        int maxProcessingAttempts,
        long retryDelayMillis,
        boolean alwaysFail) {
      super(repository, maxProcessingAttempts);

      this.retryDelayMillis = retryDelayMillis;

      this.alwaysFail = alwaysFail;
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
          RETRY_STATUS_TRANSITIONS.getOrDefault(currentStatus, ReportStatus.FAILED));
    }

    @Override
    public Collection<ReportStatus> getPendingStatuses() {
      return new ArrayList<>(ReportStatus.pendingStatuses());
    }

    @Override
    public ObjectProcessingResult<ReportStatus> process(Report report) {
      System.out.println("JPA process(): " + report.getStatus());

      if (alwaysFail) {
        throw new RuntimeException("Simulated processing failure (JPA)");
      }

      ReportStatus currentStatus = report.getStatus();

      // If we have a configured transition, use it.
      if (SUCCESS_STATUS_TRANSITIONS.containsKey(currentStatus)) {
        ReportStatus nextStatus = SUCCESS_STATUS_TRANSITIONS.get(currentStatus);
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
    protected ReportStatus determineProcessingStatusOnClaim(
        Report object, ReportStatus currentStatus) {
      return PROCESSING_STATUS_TRANSITIONS.getOrDefault(currentStatus, currentStatus);
    }
  }
}
