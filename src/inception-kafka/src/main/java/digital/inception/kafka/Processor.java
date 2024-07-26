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

package digital.inception.kafka;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import java.time.Duration;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import org.apache.kafka.clients.consumer.*;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.errors.WakeupException;
import org.apache.kafka.common.header.Header;
import org.apache.kafka.common.serialization.Deserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.BeanInitializationException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.boot.ssl.DefaultSslBundleRegistry;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.util.StringUtils;

/**
 * The <code>Processor</code> class implements the base class for a thread that retrieves and
 * processes records from an Apache Kafka topic.
 *
 * @param <K> the key type
 * @param <V> the value type
 * @author Marcus Portmann
 */
public abstract class Processor<K, V> extends Thread {

  /* Logger */
  private static final Logger logger = LoggerFactory.getLogger(Processor.class);

  /** Is the processor active? */
  private final AtomicBoolean isActive = new AtomicBoolean(true);

  /** The Spring Kafka properties. */
  private final KafkaProperties kafkaProperties;

  /** The Apache Kafka key deserializer. */
  private final Deserializer<K> keyDeserializer;

  /** The Apache Kafka value deserializer. */
  private final Deserializer<V> valueDeserializer;

  /**
   * The amount of time in milliseconds the processor will pause after failing to commit a processed
   * record.
   */
  @Value("${spring.kafka.consumer.commit-failure-pause:#{5000}}")
  private int commitFailurePause;

  /** The Apache Kafka consumer used to retrieve the messages. */
  private Consumer<K, V> consumer;

  /**
   * The amount of time in milliseconds the processor will pause when a critical error is
   * encountered while processing a record.
   */
  @Value("${spring.kafka.consumer.critical-error-pause:#{30000}}")
  private int criticalErrorPause;

  /** The maximum number of bytes returned in a call to poll() */
  @Value("${spring.kafka.consumer.max-fetch-bytes:#{0}}")
  private int maxFetchBytes;

  /** The maximum poll interval in milliseconds. */
  @Value("${spring.kafka.consumer.max-poll-interval:#{0}}")
  private int maxPollInterval;

  /** The maximum number of records returned in a call to poll() */
  @Value("${spring.kafka.consumer.max-poll-records:#{0}}")
  private int maxPollRecords;

  /** Are metrics enabled? */
  @Value("${spring.kafka.consumer.metrics-enabled:#{false}}")
  private boolean metricsEnabled;

  /** The timeout when polling for records from the Apache Kafka topic. */
  @Value("${spring.kafka.consumer.poll-timeout:#{30000}}")
  private int pollTimeout;

  /** The last time in ms that the processor active status was logged */
  private long processorActiveLastLogTime = -1L;

  /** The time in milliseconds between attempts to log that a processor is still active. */
  @Value("${spring.kafka.processor-active-logging-interval:#{180000}}")
  private long processorActiveLoggingInterval;

  /**
   * The amount of time in milliseconds the processor will pause when it is unable to handle the
   * failed processing of a record.
   */
  @Value("${spring.kafka.consumer.record-processing-failure-pause:#{30000}}")
  private int recordProcessingFailurePause;

  private Timer recordProcessingTimer;

  /**
   * The amount of time in milliseconds the processor will pause when the processor or one of its
   * dependencies is temporarily unavailable.
   */
  @Value("${spring.kafka.consumer.temporarily-unavailable-pause:#{5000}}")
  private int temporarilyUnavailablePause;

  /**
   * Constructs a new <code>Processor</code>.
   *
   * @param kafkaProperties the Spring Kafka properties
   * @param keyDeserializer the Apache Kafka key deserializer
   * @param valueDeserializer the Apache Kafka value deserializer
   */
  public Processor(
      KafkaProperties kafkaProperties,
      Deserializer<K> keyDeserializer,
      Deserializer<V> valueDeserializer) {
    this(kafkaProperties, keyDeserializer, valueDeserializer, null);
  }

  /**
   * Constructs a new <code>Processor</code>.
   *
   * @param kafkaProperties the Spring Kafka properties
   * @param keyDeserializer the Apache Kafka key deserializer
   * @param valueDeserializer the Apache Kafka value deserializer
   * @param meterRegistry the meter registry
   */
  public Processor(
      KafkaProperties kafkaProperties,
      Deserializer<K> keyDeserializer,
      Deserializer<V> valueDeserializer,
      MeterRegistry meterRegistry) {
    this.kafkaProperties = kafkaProperties;
    this.keyDeserializer = keyDeserializer;
    this.valueDeserializer = valueDeserializer;

    if (!StringUtils.hasText(kafkaProperties.getConsumer().getGroupId())) {
      throw new BeanInitializationException(
          "Failed to initialize the processor ("
              + this.getClass().getSimpleName()
              + ") without a valid Kafka consumer group ID (spring.kafka.consumer.group-id)");
    }

    if (meterRegistry != null && metricsEnabled) {
      recordProcessingTimer =
          meterRegistry.timer("timer." + this.getClass().getSimpleName() + ".recordProcessingTime");
    }

    setName("Processor (" + getClass().getSimpleName() + ") for topic (" + getTopic() + ")");

    logger.debug(
        "Initialized the processor ("
            + getClass().getSimpleName()
            + ") for the topic ("
            + getTopic());
  }

  @Override
  public void run() {
    try {
      Map<String, Object> properties =
          kafkaProperties.buildConsumerProperties(new DefaultSslBundleRegistry());

      if (StringUtils.hasText(kafkaProperties.getConsumer().getGroupId())) {
        properties.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
      } else {
        properties.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "latest");
      }

      properties.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "false");

      if (!properties.containsKey(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG)) {
        properties.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, "1000");
      }

      /*
       * If maximum interval between poll attempts is not specified then to prevent Kafka from
       * re-balancing the topic while we are paused for a transient error, critical error or commit
       * error we set the max poll interval accordingly.
       */
      if (getMaxPollInterval() > 0) {
        properties.put(ConsumerConfig.MAX_POLL_INTERVAL_MS_CONFIG, getMaxPollInterval());
      } else {
        properties.put(
            ConsumerConfig.MAX_POLL_INTERVAL_MS_CONFIG,
            Integer.min(
                Integer.max(
                    getCommitFailurePause(),
                    Integer.max(getTemporarilyUnavailablePause(), getCriticalErrorPause()))
                    + 5000,
                60000));
      }

      if (!properties.containsKey(ConsumerConfig.MAX_POLL_RECORDS_CONFIG)) {
        if (getMaxPollRecords() > 0) {
          properties.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, getMaxPollRecords());
        }
      }

      if (getMaxFetchBytes() > 0) {
        properties.put(ConsumerConfig.FETCH_MAX_BYTES_CONFIG, getMaxFetchBytes());
      }

      DefaultKafkaConsumerFactory<K, V> consumerFactory =
          new DefaultKafkaConsumerFactory<>(properties, keyDeserializer, valueDeserializer);

      consumer = consumerFactory.createConsumer();

      consumer.subscribe(Collections.singletonList(getTopic()));

      int commitFailureCount = 0;

      while (isActive.get()) {
        try {
          if (logger.isDebugEnabled()) {
            if ((processorActiveLastLogTime == -1)
                || ((System.currentTimeMillis() - processorActiveLastLogTime)
                >= getProcessorActiveLoggingInterval())) {
              processorActiveLastLogTime = System.currentTimeMillis();
              logger.debug(
                  "The processor ({}) is still active and processing with thread ID ({})",
                  getClass().getSimpleName(),
                  Thread.currentThread().threadId());
            }
          }

          ConsumerRecords<K, V> records = consumer.poll(Duration.ofMillis(getPollTimeout()));

          if (logger.isTraceEnabled()) {
            logger.trace(
                "Retrieved {} records for {} partitions from the topic {}",
                records.count(),
                records.partitions().size(),
                getTopic());
          }

          for (TopicPartition partition : records.partitions()) {
            List<ConsumerRecord<K, V>> partitionRecords = records.records(partition);

            if (logger.isTraceEnabled()) {
              logger.trace(
                  "Retrieved {} records for the partition {} from the topic {}",
                  partitionRecords.size(),
                  partition.partition(),
                  getTopic());
            }

            for (ConsumerRecord<K, V> record : partitionRecords) {
              V value = null;

              try {
                value = record.value();

                if (value != null) {
                  if (logger.isTraceEnabled()) {
                    logger.trace("Processing the record value: {}", value);
                  }

                  Map<String, byte[]> headers = new HashMap<>();

                  for (Header header : record.headers().toArray()) {
                    headers.put(header.key(), header.value());
                  }

                  processRecordTimed(headers, value);

                  if (logger.isDebugEnabled()) {
                    logger.debug(
                        "Committing the record with key ("
                            + record.key()
                            + ") and offset ("
                            + record.offset()
                            + ") for the partition ("
                            + partition.partition()
                            + ") and topic "
                            + "("
                            + partition.topic()
                            + ")");
                  }
                } else {
                  logger.error(
                      "The value for the record with key ("
                          + record.key()
                          + ") and offset ("
                          + record.offset()
                          + ") for the partition ("
                          + partition.partition()
                          + ") and topic ("
                          + partition.topic()
                          + ") is invalid and will be ignored");
                }

                consumer.commitSync(
                    Collections.singletonMap(
                        partition, new OffsetAndMetadata(record.offset() + 1)));
              } catch (InvalidRecordValueException e) {
                try {
                  handleInvalidRecordValue(value);

                  logger.error(
                      "The value for the record with key ("
                          + record.key()
                          + ") and offset ("
                          + record.offset()
                          + ") for the partition ("
                          + partition.partition()
                          + ") and topic "
                          + "("
                          + partition.topic()
                          + ") is invalid and will be ignored",
                      e);

                  consumer.commitSync(
                      Collections.singletonMap(
                          partition, new OffsetAndMetadata(record.offset() + 1)));
                } catch (Throwable t) {
                  logger.error(
                      "Failed to handle the invalid value for the record with key ("
                          + record.key()
                          + ") and offset ("
                          + record.offset()
                          + ") for the partition ("
                          + partition.partition()
                          + ") and topic "
                          + "("
                          + partition.topic()
                          + "). This will be retried in "
                          + getRecordProcessingFailurePause()
                          + "ms",
                      t);

                  try {
                    // noinspection ALL
                    sleep(getRecordProcessingFailurePause());
                  } catch (Throwable ignored) {
                  }
                }
              } catch (ProcessingFailedException e) {
                try {
                  handleRecordProcessingFailure(value);

                  logger.error(
                      "Failed to process the record with key ("
                          + record.key()
                          + ") and offset ("
                          + record.offset()
                          + ") for the partition ("
                          + partition.partition()
                          + ") and topic "
                          + "("
                          + partition.topic()
                          + "). This record will be ignored",
                      e);

                  consumer.commitSync(
                      Collections.singletonMap(
                          partition, new OffsetAndMetadata(record.offset() + 1)));
                } catch (Throwable t) {
                  logger.error(
                      "Failed to handle the processing failure for the record with key ("
                          + record.key()
                          + ") and offset ("
                          + record.offset()
                          + ") for the partition ("
                          + partition.partition()
                          + ") and topic "
                          + "("
                          + partition.topic()
                          + "). This will be retried in "
                          + getRecordProcessingFailurePause()
                          + "ms",
                      t);

                  try {
                    // noinspection ALL
                    sleep(getRecordProcessingFailurePause());
                  } catch (Throwable ignored) {
                  }
                }
              } catch (TransientErrorException e) {
                try {
                  if (logger.isDebugEnabled()) {
                    logger.debug(
                        "Sleeping "
                            + temporarilyUnavailablePause
                            + "ms for transient error exception",
                        e);
                  }

                  // noinspection ALL
                  sleep(getTemporarilyUnavailablePause());
                } catch (Throwable ignored) {
                }

              } catch (Throwable e) {
                logger.error(
                    "A critical error occurred while attempting to process the record with key ("
                        + record.key()
                        + ") and offset ("
                        + record.offset()
                        + ") for the partition ("
                        + partition.partition()
                        + ") and topic "
                        + "("
                        + partition.topic()
                        + ")",
                    e);

                logger.warn(
                    "Shutting down processor ("
                        + getClass().getSimpleName()
                        + ") for topic ("
                        + getTopic()
                        + ") due to an unhandled exception");

                isActive.set(false);

                return;
              }
            }
          }
        } catch (CommitFailedException e) {
          commitFailureCount++;

          logger.error(
              "Failed to commit the record retrieved from the topic ({}). This error has occurred {} time(s). Sleeping for {} ms.",
              getTopic(),
              commitFailureCount,
              getCommitFailurePause(),
              e);

          try {
            // noinspection ALL
            sleep(getCommitFailurePause());
          } catch (Throwable ignored) {
          }
        }
      }
    } catch (WakeupException e) {
      // Ignore the exception if closing
      if (isActive.get()) {
        logger.error("Failed to execute the processor (" + getClass().getSimpleName() + ")", e);
      }
    } catch (Throwable e) {
      logger.error("Failed to execute the processor (" + getClass().getSimpleName() + ")", e);
    } finally {
      try {
        if (consumer != null) {
          consumer.close();
        }
      } catch (Throwable e) {
        logger.error("Failed to close the consumer", e);
      }
    }
  }

  /** Shutdown the processor and the associated background thread. */
  public void shutdown() {
    if (isActive.get()) {
      isActive.set(false);

      if (consumer != null) {
        consumer.wakeup();
      }
    }

    logger.info("Waiting for processor (" + getClass().getSimpleName() + ") to shutdown");

    try {
      join();
    } catch (Throwable ignored) {
    }

    logger.info("Processor (" + getClass().getSimpleName() + ") shutdown");
  }

  /**
   * Returns the amount of time in milliseconds the processor will pause after failing to commit a
   * processed record.
   *
   * @return the amount of time in milliseconds the processor will pause after failing to commit a
   *     processed record
   */
  protected int getCommitFailurePause() {
    return commitFailurePause;
  }

  /**
   * Returns the amount of time in milliseconds the processor will pause when a critical error is
   * encountered while processing a record.
   *
   * @return the amount of time in milliseconds the processor will pause when a critical error is
   *     encountered while processing a record
   */
  protected int getCriticalErrorPause() {
    return criticalErrorPause;
  }

  /**
   * Returns the maximum number of bytes returned in a call to poll().
   *
   * @return the maximum number of bytes returned in a call to poll()
   */
  protected int getMaxFetchBytes() {
    return maxFetchBytes;
  }

  /**
   * Returns the maximum poll interval in milliseconds.
   *
   * @return the maximum poll interval in milliseconds
   */
  protected int getMaxPollInterval() {
    return maxPollInterval;
  }

  /**
   * Returns the maximum number of records returned in a call to poll().
   *
   * @return the maximum number of records returned in a call to poll()
   */
  protected int getMaxPollRecords() {
    return maxPollRecords;
  }

  /**
   * Returns whether metrics are enabled.
   *
   * @return <b>true</b> if metrics are enabled <b>false</b> otherwise
   */
  protected boolean getMetricsEnabled() {
    return metricsEnabled;
  }

  /**
   * Returns the timeout when polling for records from the Apache Kafka topic.
   *
   * @return the timeout when polling for records from the Apache Kafka topic
   */
  protected int getPollTimeout() {
    return pollTimeout;
  }

  /**
   * Returns the time in milliseconds between attempts to log that a processor is still active.
   *
   * @return the time in milliseconds between attempts to log that a processor is still active
   */
  protected long getProcessorActiveLoggingInterval() {
    return processorActiveLoggingInterval;
  }

  /**
   * Returns the amount of time in milliseconds the processor will pause when it is unable to handle
   * the failed processing of a record.
   *
   * @return the amount of time in milliseconds the processor will pause when it is unable to handle
   *     the failed processing of a record
   */
  protected int getRecordProcessingFailurePause() {
    return recordProcessingFailurePause;
  }

  /**
   * Returns the amount of time in milliseconds the processor will pause when the processor or one
   * of its dependencies is temporarily unavailable.
   *
   * @return the amount of time in milliseconds the processor will pause when the processor or one
   *     of its dependencies is temporarily unavailable
   */
  protected int getTemporarilyUnavailablePause() {
    return temporarilyUnavailablePause;
  }

  /**
   * Returns the OID identifying the Kafka topic associated with this processor.
   *
   * @return the OID identifying the Kafka topic associated with this processor
   */
  protected abstract String getTopic();

  /**
   * Handle the processing of an invalid record value.
   *
   * @param value the record value
   * @throws Exception if the invalid record value could not be handled
   */
  protected abstract void handleInvalidRecordValue(V value) throws Exception;

  /**
   * Handle the failed processing of a record value.
   *
   * @param value the record value
   * @throws Exception if the record value processing failure could not be handled
   */
  protected abstract void handleRecordProcessingFailure(V value) throws Exception;

  /**
   * Process the record value.
   *
   * @param headers the record headers
   * @param value the record value
   * @throws InvalidRecordValueException if the record value is invalid and cannot be processed
   * @throws TransientErrorException if a transient error occurred and the processing of the record
   *     value should be retried
   * @throws ProcessingFailedException if the processing of the record value failed and should not
   *     be retried
   */
  protected abstract void processRecord(Map<String, byte[]> headers, V value)
      throws InvalidRecordValueException, TransientErrorException, ProcessingFailedException;

  /**
   * Time the processing of a record.
   *
   * @param headers the record headers
   * @param value the record value
   * @throws InvalidRecordValueException if the record value is invalid and cannot be processed
   * @throws TransientErrorException if a transient error occurred and the processing of the record
   *     value should be retried
   * @throws ProcessingFailedException if the processing of the record value failed and should not
   *     be retried
   */
  private void processRecordTimed(Map<String, byte[]> headers, V value)
      throws InvalidRecordValueException, TransientErrorException, ProcessingFailedException {
    if (recordProcessingTimer != null) {
      long startTime = System.currentTimeMillis();

      try {
        processRecord(headers, value);
      } finally {
        recordProcessingTimer.record(Duration.ofMillis(System.currentTimeMillis() - startTime));
      }
    } else {
      processRecord(headers, value);
    }
  }
}
