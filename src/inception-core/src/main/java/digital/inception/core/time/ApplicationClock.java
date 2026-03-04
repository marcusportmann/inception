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

package digital.inception.core.time;

import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

/**
 * A static, application-wide clock abstraction that allows the perceived current time to be
 * manipulated, primarily for deterministic testing.
 *
 * <p>The clock defaults to {@link Clock#systemDefaultZone()} and is used by all "now" methods
 * provided by this class. During unit/integration testing, callers may advance time or jump to a
 * specific instant without changing system time.
 *
 * <h2>Thread-safety</h2>
 *
 * The underlying {@link Clock} reference is stored in a {@code volatile} field. Reads will observe
 * the most recently published clock; updates replace the clock instance atomically by assignment.
 *
 * <p><strong>Note:</strong> This class intentionally introduces global mutable state. Use with care
 * in tests (e.g., call {@link #reset()} in {@code @AfterEach} / {@code @AfterAll}) to avoid
 * cross-test interference.
 *
 * @author Marcus Portmann
 */
public final class ApplicationClock {

  /**
   * The underlying clock used to derive the current application time.
   *
   * <p>Defaults to {@link Clock#systemDefaultZone()}. Mutated by {@link #advanceBy(Duration)} and
   * {@link #jumpTo(Instant)}.
   */
  private static volatile Clock clock = Clock.systemDefaultZone();

  /** Hidden constructor to prevent instantiation. */
  private ApplicationClock() {}

  /**
   * Advances the application clock by the specified duration.
   *
   * <p>Internally, this replaces the underlying clock with an offset clock derived from the current
   * clock.
   *
   * @param duration the amount of time to advance the clock by; may be negative to move backwards
   */
  public static synchronized void advanceBy(Duration duration) {
    clock = Clock.offset(clock, duration);
  }

  /**
   * Returns the underlying {@link Clock} used by this class.
   *
   * <p>This may be useful for passing into other APIs that accept a {@link Clock}.
   *
   * @return the current underlying clock
   */
  public static Clock getClock() {
    return clock;
  }

  /**
   * Returns the {@link ZoneId} used by the underlying {@link Clock}.
   *
   * @return the clock zone
   */
  public static ZoneId getZone() {
    return clock.getZone();
  }

  /**
   * Returns the current application time as an {@link Instant} using the underlying {@link
   * #getClock() clock}.
   *
   * @return the current application time as an instant
   */
  public static Instant instant() {
    return Instant.now(clock);
  }

  /**
   * Jumps the application clock so that the current {@link #instant()} becomes the specified target
   * instant.
   *
   * <p>This method calculates the required offset relative to the current underlying clock and
   * replaces the underlying clock with an offset clock.
   *
   * @param target the target instant that should be observed as "now"
   */
  public static synchronized void jumpTo(Instant target) {
    Duration offset = Duration.between(Instant.now(clock), target);
    clock = Clock.offset(clock, offset);
  }

  /**
   * Returns the current application time as a {@link LocalDateTime} using the underlying {@link
   * #getClock() clock}.
   *
   * @return the current application time as a local date-time
   */
  public static LocalDateTime now() {
    return LocalDateTime.now(clock);
  }

  /**
   * Resets the application clock back to the system clock using the default time zone.
   *
   * <p>Call this at the end of tests to avoid clock state leaking between test cases.
   */
  public static void reset() {
    clock = Clock.systemDefaultZone();
  }

  /**
   * Returns the current application date as a {@link LocalDate} using the underlying {@link
   * #getClock() clock}.
   *
   * @return the current application date
   */
  public static LocalDate today() {
    return LocalDate.now(clock);
  }

  /**
   * Returns the current application time as a {@link ZonedDateTime} using the underlying {@link
   * #getClock() clock}.
   *
   * <p>The returned value uses the zone of the underlying clock (see {@link #getZone()}).
   *
   * @return the current application time as a zoned date-time
   */
  public static ZonedDateTime zonedNow() {
    return ZonedDateTime.now(clock);
  }
}
