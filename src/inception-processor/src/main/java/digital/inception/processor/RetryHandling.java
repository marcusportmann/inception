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

import java.time.OffsetDateTime;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;

/**
 * The {@code RetryHandling} value specifies how a failed processing attempt should be retried.
 *
 * <p>It encapsulates:
 *
 * <ul>
 *   <li>the {@link ProcessableObjectStatus} that the object should transition to in order to be
 *       eligible for a retry, and
 *   <li>the date and time when the object should next be considered for processing.
 * </ul>
 *
 * <p>The {@code nextProcessed} value is typically used to populate {@link
 * AbstractProcessableObject#setNextProcessed} so that later calls to {@link
 * ObjectProcessor#claimNextProcessableObject} can select objects whose next processing time has
 * been reached.
 *
 * @param retryStatus the status to apply when scheduling the retry
 * @param nextProcessed the date and time when processing should next be attempted for the object
 * @param <S> the status type for the processable object, which must implement {@link
 *     ProcessableObjectStatus}
 * @author Marcus Portmann
 */
@SuppressWarnings("unused")
public record RetryHandling<S extends ProcessableObjectStatus>(
    S retryStatus, OffsetDateTime nextProcessed) {

  /**
   * Convenience factory method that creates a {@code RetryHandling} instructing a delayed retry
   * using the supplied {@code retryStatus}.
   *
   * <p>The returned instance sets {@code nextProcessed} to the current time plus the specified
   * delay in seconds, making the object eligible for reprocessing only after that point in time
   * (according to the standard selection rule, i.e., objects are considered due when {@code
   * nextProcessed <= now}).
   *
   * @param retryStatus the status to apply when scheduling the retry
   * @param retryDelaySeconds the delay, in seconds, before the object should be considered for
   *     reprocessing
   * @param <S> the status type
   * @return a {@code RetryHandling} with the given status and a {@code nextProcessed} timestamp of
   *     {@code OffsetDateTime.now().plus(retryDelaySeconds, ChronoUnit.SECONDS)}
   */
  public static <S extends ProcessableObjectStatus> RetryHandling<S> delayed(
      S retryStatus, long retryDelaySeconds) {
    return new RetryHandling<>(retryStatus, OffsetDateTime.now().plusSeconds(retryDelaySeconds));
  }

  /**
   * Convenience factory method that creates a {@code RetryHandling} instructing a delayed retry
   * using a geometric progression (exponential backoff) based on the retry count.
   *
   * <p>The delay is calculated as:
   *
   * <pre>
   * effectiveDelay = retryDelay * 2^(effectiveRetryCount - 1)
   * </pre>
   *
   * Where {@code effectiveRetryCount} is {@code max(1, retryCount)}. For example, with a base delay
   * of {@code 10} SECONDS:
   *
   * <ul>
   *   <li>retryCount = 1 → delay = 10 seconds
   *   <li>retryCount = 2 → delay = 20 seconds
   *   <li>retryCount = 3 → delay = 40 seconds
   *   <li>retryCount = 4 → delay = 80 seconds
   * </ul>
   *
   * <p>The returned instance sets {@code nextProcessed} to the current time plus the calculated
   * delay in the specified {@code retryDelayUnit}, making the object eligible for reprocessing only
   * after that point in time (according to the standard selection rule, i.e., objects are
   * considered due when {@code nextProcessed <= now}).
   *
   * @param retryStatus the status to apply when scheduling the retry
   * @param retryCount the (1-based) retry attempt number; values less than 1 are treated as 1
   * @param retryDelay the base delay amount, expressed in units of {@code retryDelayUnit}
   * @param retryDelayUnit the temporal unit for {@code retryDelay} (for example, {@link
   *     ChronoUnit#SECONDS} or {@link ChronoUnit#MINUTES})
   * @param <S> the status type
   * @return a {@code RetryHandling} with the given status and a {@code nextProcessed} timestamp
   *     computed using geometric backoff from the current time
   */
  public static <S extends ProcessableObjectStatus> RetryHandling<S> delayedWithBackoff(
      S retryStatus, int retryCount, long retryDelay, TemporalUnit retryDelayUnit) {

    int effectiveRetryCount = Math.max(1, retryCount);
    long multiplier = 1L << (effectiveRetryCount - 1); // 2^(n-1) using bit shift
    long effectiveDelay = retryDelay * multiplier;

    return new RetryHandling<>(
        retryStatus, OffsetDateTime.now().plus(effectiveDelay, retryDelayUnit));
  }

  /**
   * Convenience factory method that creates a {@code RetryHandling} instructing an immediate retry
   * using the supplied {@code retryStatus}.
   *
   * <p>The returned instance sets {@code nextProcessed} to {@link OffsetDateTime#now}, making the
   * object immediately eligible for reprocessing according to the standard selection rule (i.e.,
   * objects are considered due when {@code nextProcessed <= now}).
   *
   * @param retryStatus the status to apply when scheduling the retry
   * @param <S> the status type
   * @return a {@code RetryHandling} with the given status and a {@code nextProcessed} timestamp of
   *     {@link OffsetDateTime#now}
   */
  public static <S extends ProcessableObjectStatus> RetryHandling<S> immediate(S retryStatus) {
    return new RetryHandling<>(retryStatus, OffsetDateTime.now());
  }
}
