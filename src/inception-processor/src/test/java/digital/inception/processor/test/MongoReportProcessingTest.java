/*
 * Copyright (c) Discovery Ltd. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Discovery Ltd
 * ("Confidential Information"). It may not be copied or reproduced in any manner
 * without the express written permission of Discovery Ltd.
 */

package digital.inception.processor.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import digital.inception.mongo.MongoConfiguration;
import digital.inception.processor.AbstractProcessableObject;
import digital.inception.processor.BackgroundObjectProcessor;
import digital.inception.processor.ObjectProcessingResult;
import digital.inception.processor.ObjectProcessor;
import digital.inception.processor.ProcessableObjectStatus;
import digital.inception.processor.ProcessableObjectStatus.ProcessingPhase;
import digital.inception.processor.RetryHandling;
import digital.inception.processor.persistence.mongo.AbstractProcessableObjectMongoOperations;
import digital.inception.processor.persistence.mongo.MongoRepositoryBackedBackgroundObjectProcessor;
import digital.inception.processor.persistence.mongo.MongoRepositoryBackedObjectProcessor;
import digital.inception.processor.persistence.mongo.ProcessableObjectMongoOperations;
import digital.inception.processor.test.MongoReportProcessingTest.MongoReportProcessingTestConfig;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.ArrayList;
import java.util.Collection;
import java.util.EnumSet;
import java.util.Map;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.stereotype.Repository;

/**
 * Mongo-backed equivalent of {@code ReportProcessingTest}, using embedded MongoDB (flapdoodle) to
 * verify the {@link MongoRepositoryBackedObjectProcessor} / {@link BackgroundObjectProcessor}
 * integration.
 *
 * @author Marcus Portmann
 */
@SpringBootTest(
    classes = {MongoReportProcessingTestConfig.class, MongoConfiguration.class},
    properties = {
      "spring.data.mongodb.uri=mongodb://localhost:29017/test",
      "spring.data.mongodb.embedded=mongo-java-server"
    },
    webEnvironment = WebEnvironment.NONE)
public class MongoReportProcessingTest {

  @Autowired private ApplicationContext applicationContext;

  @Autowired private MongoReportRepository mongoReportRepository;

  @BeforeEach
  void cleanMongo() {
    mongoReportRepository.deleteAll();
  }

  /**
   * Happy-path test that drives the report through multiple states until it reaches DELIVERED
   * (COMPLETED phase) using the Mongo-backed processor.
   */
  @Test
  void testMongoReportHappyPathDelivered() throws Exception {
    Report report = mongoReportRepository.save(new Report());

    MongoBackgroundReportProcessor backgroundReportProcessor =
        new MongoBackgroundReportProcessor(
            applicationContext, mongoReportRepository, 2, 10, 0, 1_000L, 3, 1_000L, false);

    backgroundReportProcessor.start();

    int safety = 60;

    while (isNotTerminal(report) && safety-- > 0) {
      backgroundReportProcessor.processObjects();
      backgroundReportProcessor.waitUntilIdle(2_000L);

      // Reload from Mongo to observe persisted changes
      report = mongoReportRepository.findById(report.getId()).orElseThrow();
    }

    assertTrue(safety > 0, "Safety counter exhausted before reaching a terminal status");

    assertEquals(ReportStatus.DELIVERED, report.getStatus());
    assertTrue(backgroundReportProcessor.getSuccessCount() > 0);
    assertEquals(1, report.getProcessingAttempts());
    assertNull(report.getNextProcessed());
  }

  /**
   * Test that a report which always fails to process eventually transitions to a FAILED status
   * after the maximum number of attempts, using Mongo-backed processing.
   */
  @Test
  void testMongoReportPermanentFailureAfterMaxAttempts() throws Exception {
    Report report = mongoReportRepository.save(new Report());

    MongoBackgroundReportProcessor backgroundReportProcessor =
        new MongoBackgroundReportProcessor(
            applicationContext, mongoReportRepository, 2, 10, 0, 1_000L, 3, 1_000L, true);

    backgroundReportProcessor.start();

    int safety = 60;

    while (isNotTerminal(report) && safety-- > 0) {
      backgroundReportProcessor.processObjects();
      backgroundReportProcessor.waitUntilIdle(2_000L);

      // Reload from Mongo to observe persisted changes
      report = mongoReportRepository.findById(report.getId()).orElseThrow();
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
   * Test that a report marked as "processing suspended" is not processed, using Mongo-backed
   * processing.
   */
  @Test
  void testMongoReportProcessingSuspended() throws Exception {
    Report report = new Report();

    report.setProcessingSuspended(true);

    mongoReportRepository.save(report);

    MongoBackgroundReportProcessor backgroundReportProcessor =
        new MongoBackgroundReportProcessor(
            applicationContext, mongoReportRepository, 2, 10, 0, 1_000L, 3, 1_000L, true);

    backgroundReportProcessor.start();

    int safety = 60;

    while (isNotTerminal(report) && safety-- > 0) {
      backgroundReportProcessor.processObjects();
      backgroundReportProcessor.waitUntilIdle(2_000L);

      // Reload from Mongo to observe persisted changes
      report = mongoReportRepository.findById(report.getId()).orElseThrow();
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
   * Spring Data Mongo repository for {@link Report}, extending the shared {@link
   * ProcessableObjectMongoOperations} abstraction via {@link MongoReportRepositoryCustom}.
   */
  public interface MongoReportRepository
      extends MongoRepository<Report, String>, MongoReportRepositoryCustom {}

  public interface MongoReportRepositoryCustom
      extends ProcessableObjectMongoOperations<Report, String, ReportStatus> {
    // No extra methods yet – add report-specific Mongo operations here if needed.
  }

  /**
   * Test {@link MongoRepositoryBackedBackgroundObjectProcessor} specialization that tracks how many
   * successes and permanent failures occur, and provides a method to wait until the executor is
   * idle (no active tasks, empty queue), using Mongo-backed reports.
   */
  public static class MongoBackgroundReportProcessor
      extends MongoRepositoryBackedBackgroundObjectProcessor<
          Report, String, ReportStatus, MongoReportRepositoryCustom> {

    private int permanentFailureCount;
    private int successCount;

    public MongoBackgroundReportProcessor(
        ApplicationContext applicationContext,
        MongoReportRepository mongoReportRepository,
        int processingThreadCount,
        int maximumQueueLength,
        long lockTimeoutSeconds,
        long processingTimeoutMillis,
        int maximumProcessingAttempts,
        long retryDelayMillis,
        boolean alwaysFail) {

      super(
          applicationContext,
          mongoReportRepository,
          new MongoReportProcessor(
              mongoReportRepository, maximumProcessingAttempts, retryDelayMillis, alwaysFail),
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
      return MongoReportProcessor.PROCESSING_STATUS_TRANSITIONS;
    }
  }

  @Configuration
  @EnableMongoRepositories(
      considerNestedRepositories = true,
      mongoTemplateRef = "defaultMongoTemplate")
  @EnableMongoAuditing
  static class MongoReportProcessingTestConfig {
    // Spring Boot + @EnableMongoRepositories + embedded Mongo do the heavy lifting.
  }

  /**
   * Mongo-backed {@link ObjectProcessor} implementation for {@link Report} used in tests.
   *
   * <p>This mirrors the in-memory processor but delegates persistence and locking to {@link
   * MongoRepositoryBackedObjectProcessor} and {@link MongoReportRepository}.
   */
  public static class MongoReportProcessor
      extends MongoRepositoryBackedObjectProcessor<
          Report, String, ReportStatus, MongoReportRepository> {

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

    private final long retryDelayMillis;

    public MongoReportProcessor(
        MongoReportRepository repository,
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
    public ObjectProcessingResult<ReportStatus> process(Report report) throws Exception {
      System.out.println("Mongo process(): " + report.getStatus());

      if (alwaysFail) {
        throw new RuntimeException("Simulated processing failure (Mongo)");
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

  @Repository
  public static class MongoReportRepositoryImpl
      extends AbstractProcessableObjectMongoOperations<Report, String, ReportStatus>
      implements MongoReportRepositoryCustom {

    public MongoReportRepositoryImpl(
        @Qualifier("defaultMongoTemplate") MongoTemplate mongoTemplate) {
      super(mongoTemplate, Report.class);
    }
  }

  /** Mongo-backed report domain object used for tests. */
  @Document("reports")
  public static class Report extends AbstractProcessableObject<String, ReportStatus> {

    @Id private String id;

    public Report() {
      super(ReportStatus.REQUESTED);
      // Pre-assign an ObjectId-based identifier.
      this.id = new ObjectId().toHexString();
    }

    @Override
    public String getId() {
      return id;
    }

    @Override
    public String getIdAsKey() {
      return id;
    }

    public void setId(String id) {
      this.id = id;
    }
  }
}
