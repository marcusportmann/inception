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
package digital.inception.opentelemetry;

import java.util.concurrent.atomic.AtomicLong;

/**
 * The <b>TokenBucketRateLimiter</b> class implements a rate-limiting mechanism using the token
 * bucket algorithm. This is a thread-safe implementation that ensures requests are processed at a
 * controlled rate.
 *
 * <p>The rate limiter allows up to a specified number of tokens per second (the refill rate), and
 * it can accommodate bursts of requests up to the specified bucket capacity.
 *
 * <p>This class is suitable for scenarios where rate-limiting logic needs to prevent overload or
 * control traffic flow.
 *
 * @author Marcus Portmann
 */
public class TokenBucketRateLimiter {

  /** The current number of available tokens in the bucket. */
  private final AtomicLong availableTokens;

  /** The maximum number of tokens the bucket can hold. */
  private final long capacity;

  /** The timestamp of the last refill operation, in nanoseconds. */
  private final AtomicLong lastRefillTime;

  /** The number of tokens added to the bucket per second. */
  private final long tokensPerSecond;

  /**
   * Constructs a new <b>TokenBucketRateLimiter</b>.
   *
   * @param capacity the maximum number of tokens the bucket can hold
   * @param tokensPerSecond the number of tokens added to the bucket per second
   * @throws IllegalArgumentException if {@code capacity} or {@code tokensPerSecond} is non-positive
   */
  public TokenBucketRateLimiter(long capacity, long tokensPerSecond) {
    if (capacity <= 0 || tokensPerSecond <= 0) {
      throw new IllegalArgumentException("Capacity and tokens per second must be positive");
    }
    this.capacity = capacity;
    this.tokensPerSecond = tokensPerSecond;
    this.availableTokens = new AtomicLong(capacity);
    this.lastRefillTime = new AtomicLong(System.nanoTime());
  }

  /**
   * Attempts to acquire a token from the bucket. If a token is available, it is consumed and the
   * method returns <b>true</b>. If no tokens are available, the method returns <b>false</b>.
   *
   * @return <b>true</b> if a token was successfully acquired, otherwise <b>false</b>
   */
  public boolean tryAcquire() {
    refill();
    long currentTokens;
    do {
      currentTokens = availableTokens.get();
      if (currentTokens == 0) {
        return false;
      }
    } while (!availableTokens.compareAndSet(currentTokens, currentTokens - 1));
    return true;
  }

  /**
   * Refills the bucket with tokens based on the elapsed time since the last refill. The number of
   * tokens added is proportional to the elapsed time and the refill rate.
   */
  private void refill() {
    long now = System.nanoTime();
    long lastRefill = lastRefillTime.get();
    long elapsedTime = now - lastRefill;
    long tokensToAdd = (elapsedTime * tokensPerSecond) / 1_000_000_000L;

    if (tokensToAdd > 0) {
      long newTokens;
      do {
        long currentTokens = availableTokens.get();
        newTokens = Math.min(capacity, currentTokens + tokensToAdd);
      } while (!availableTokens.compareAndSet(availableTokens.get(), newTokens));
      lastRefillTime.compareAndSet(lastRefill, now);
    }
  }
}
