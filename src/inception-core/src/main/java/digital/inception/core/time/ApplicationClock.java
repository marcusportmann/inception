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
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;

/**
 * The authoritative application-wide clock abstraction. This allows the perceived current time to
 * be manipulated, primarily for deterministic testing.
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
   * The immutable wall-clock baseline for the running JVM.
   *
   * <p>This clock always reflects real system time and is never time-travelled. It exists so that
   * {@link #jumpTo(Instant)}, {@link #advanceBy(Duration)}, {@link #reset()}, and {@link
   * #getState()} can compare and rebuild the effective application clock relative to a stable
   * reference point.
   */
  private static final Clock SYSTEM_CLOCK = Clock.systemDefaultZone();

  /**
   * The effective application clock used by all public "current time" accessors in this class.
   *
   * <p>Defaults to {@link #SYSTEM_CLOCK}. When time travel is active, this becomes an offset clock
   * derived from {@link #SYSTEM_CLOCK}. Read methods such as {@link #instant()}, {@link #now()},
   * {@link #today()}, and their zone-aware variants must derive time from this clock, either
   * directly or indirectly via {@link #instant()}.
   */
  private static volatile Clock clock = SYSTEM_CLOCK;

  /**
   * The difference between the effective {@link #clock} and the immutable {@link #SYSTEM_CLOCK}.
   *
   * <p>A zero duration means the application clock is aligned with wall-clock time.
   */
  private static volatile Duration offset = Duration.ZERO;

  /** Hidden constructor to prevent instantiation. */
  private ApplicationClock() {}

  /**
   * Advances the application clock by the specified duration.
   *
   * <p>The existing offset is increased by the supplied duration and the effective {@link #clock}
   * is rebuilt relative to {@link #SYSTEM_CLOCK}. Using {@link #SYSTEM_CLOCK} as the baseline keeps
   * the clock state deterministic and ensures that advances are measured against real system time
   * rather than against a previously shifted synthetic clock instance.
   *
   * @param duration the amount of time to advance the clock by; may be negative to move backwards
   */
  public static synchronized void advanceBy(Duration duration) {
    offset = offset.plus(duration);
    clock = offset.isZero() ? SYSTEM_CLOCK : Clock.offset(SYSTEM_CLOCK, offset);
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
   * Returns the current clock state for diagnostics and test-support APIs.
   *
   * <p>The returned state intentionally exposes both the effective application instant and the
   * underlying wall-clock instant, together with the explicit offset between them. This makes it
   * possible to see whether time travel is active and how far the application clock has diverged
   * from {@link #SYSTEM_CLOCK}.
   *
   * @return the current application clock state
   */
  public static State getState() {
    Duration currentOffset = offset;
    Instant systemInstant = Instant.now(SYSTEM_CLOCK);
    Instant applicationInstant = systemInstant.plus(currentOffset);
    ZoneId zoneId = getZone();
    return new State(
        applicationInstant,
        systemInstant,
        currentOffset,
        formatCalendarIso8601Duration(systemInstant, applicationInstant, zoneId),
        zoneId.getId(),
        !currentOffset.isZero());
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
   * <p>This is the canonical source of "now" for the application. Zone-aware helpers such as {@link
   * #now(ZoneId)}, {@link #today(ZoneId)}, {@link #offsetNow(ZoneId)}, and {@link
   * #zonedNow(ZoneId)} all derive from this same effective application time.
   *
   * @return the current application time as an instant
   */
  public static Instant instant() {
    return Instant.now(clock);
  }

  /**
   * Returns whether the application clock is currently offset from system time.
   *
   * <p>This is derived from whether {@link #offset} is non-zero, not by comparing instants at read
   * time.
   *
   * @return {@code true} when time travel is active
   */
  public static boolean isTimeTravelActive() {
    return !offset.isZero();
  }

  /**
   * Jumps the application clock so that the current {@link #instant()} becomes the specified target
   * instant.
   *
   * <p>This method calculates the required offset relative to {@link #SYSTEM_CLOCK} and replaces
   * the effective {@link #clock} with an offset clock derived from that stable baseline. Using
   * {@link #SYSTEM_CLOCK} avoids compounding shifts relative to an already time-travelled clock
   * instance.
   *
   * @param target the target instant that should be observed as "now"
   */
  public static synchronized void jumpTo(Instant target) {
    offset = Duration.between(Instant.now(SYSTEM_CLOCK), target);
    clock = offset.isZero() ? SYSTEM_CLOCK : Clock.offset(SYSTEM_CLOCK, offset);
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
   * Returns the current application time as a {@link LocalDateTime} for the supplied zone.
   *
   * <p>This does not read directly from the JVM wall clock. It re-expresses the current {@link
   * #instant()} in the supplied zone, so the result still respects any active time travel.
   *
   * @param zoneId the target zone
   * @return the current application time in the supplied zone
   */
  public static LocalDateTime now(ZoneId zoneId) {
    return LocalDateTime.ofInstant(instant(), zoneId);
  }

  /**
   * Returns the current application time as an {@link OffsetDateTime} using the clock zone.
   *
   * @return the current application time as an offset date-time
   */
  public static OffsetDateTime offsetNow() {
    return OffsetDateTime.ofInstant(instant(), getZone());
  }

  /**
   * Returns the current application time as an {@link OffsetDateTime} for the supplied zone.
   *
   * <p>This is the current {@link #instant()} viewed in the supplied zone.
   *
   * @param zoneId the target zone
   * @return the current application time as an offset date-time
   */
  public static OffsetDateTime offsetNow(ZoneId zoneId) {
    return OffsetDateTime.ofInstant(instant(), zoneId);
  }

  /**
   * Resets the application clock back to the system clock using the default time zone.
   *
   * <p>This clears the stored {@link #offset} and restores {@link #clock} to {@link #SYSTEM_CLOCK}.
   * Call this at the end of tests to avoid clock state leaking between test cases.
   */
  public static synchronized void reset() {
    offset = Duration.ZERO;
    clock = SYSTEM_CLOCK;
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
   * Returns the current application date for the supplied zone.
   *
   * <p>This does not read directly from the JVM wall clock. It re-expresses the current {@link
   * #instant()} in the supplied zone, so the result still respects any active time travel.
   *
   * @param zoneId the target zone
   * @return the current application date
   */
  public static LocalDate today(ZoneId zoneId) {
    return LocalDate.ofInstant(instant(), zoneId);
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

  /**
   * Returns the current application time as a {@link ZonedDateTime} for the supplied zone.
   *
   * <p>This is the current {@link #instant()} viewed in the supplied zone.
   *
   * @param zoneId the target zone
   * @return the current application time as a zoned date-time
   */
  public static ZonedDateTime zonedNow(ZoneId zoneId) {
    return instant().atZone(zoneId);
  }

  private static String formatCalendarIso8601Duration(
      Instant systemInstant, Instant applicationInstant, ZoneId zoneId) {
    if (systemInstant.equals(applicationInstant)) {
      return Duration.ZERO.toString();
    }

    boolean negative = applicationInstant.isBefore(systemInstant);
    ZonedDateTime start = (negative ? applicationInstant : systemInstant).atZone(zoneId);
    ZonedDateTime end = (negative ? systemInstant : applicationInstant).atZone(zoneId);

    long years = start.until(end, ChronoUnit.YEARS);
    start = start.plusYears(years);

    long months = start.until(end, ChronoUnit.MONTHS);
    start = start.plusMonths(months);

    long days = start.until(end, ChronoUnit.DAYS);
    start = start.plusDays(days);

    long hours = start.until(end, ChronoUnit.HOURS);
    start = start.plusHours(hours);

    long minutes = start.until(end, ChronoUnit.MINUTES);
    start = start.plusMinutes(minutes);

    long seconds = start.until(end, ChronoUnit.SECONDS);
    start = start.plusSeconds(seconds);

    long nanos = start.until(end, ChronoUnit.NANOS);

    StringBuilder builder = new StringBuilder();
    if (negative) {
      builder.append('-');
    }

    builder.append('P');

    if (years != 0L) {
      builder.append(years).append('Y');
    }

    if (months != 0L) {
      builder.append(months).append('M');
    }

    if (days != 0L) {
      builder.append(days).append('D');
    }

    if (hours != 0L || minutes != 0L || seconds != 0L || nanos != 0L) {
      builder.append('T');

      if (hours != 0L) {
        builder.append(hours).append('H');
      }

      if (minutes != 0L) {
        builder.append(minutes).append('M');
      }

      if (seconds != 0L || nanos != 0L) {
        appendSeconds(builder, seconds, nanos);
      }
    }

    if (builder.charAt(builder.length() - 1) == 'P') {
      builder.append("T0S");
    }

    return builder.toString();
  }

  private static void appendSeconds(StringBuilder builder, long seconds, long nanos) {
    builder.append(seconds);

    if (nanos != 0L) {
      String fraction = String.format("%09d", nanos);
      int lastDigitIndex = fraction.length() - 1;

      while (lastDigitIndex >= 0 && fraction.charAt(lastDigitIndex) == '0') {
        lastDigitIndex--;
      }

      builder.append('.').append(fraction, 0, lastDigitIndex + 1);
    }

    builder.append('S');
  }

  /**
   * Snapshot of the current application clock state.
   *
   * <p>This captures both the effective application time and the underlying wall-clock time, plus
   * the offset between them.
   *
   * @param applicationInstant the effective current application instant
   * @param systemInstant the underlying wall-clock instant
   * @param offsetDuration the exact application-time offset relative to system time
   * @param offsetIso8601 the calendar-aware ISO-8601 representation of the offset
   * @param zoneId the zone used to derive calendar-aware values
   * @param timeTravelActive whether the application clock is currently offset from wall-clock time
   */
  public record State(
      Instant applicationInstant,
      Instant systemInstant,
      Duration offsetDuration,
      String offsetIso8601,
      String zoneId,
      boolean timeTravelActive) {

    /**
     * Returns the effective current application instant.
     *
     * @return the effective application instant
     */
    public Instant getApplicationInstant() {
      return applicationInstant;
    }

    /**
     * Returns the difference between the effective application time and the underlying wall-clock
     * time.
     *
     * <p>This is calculated as {@code applicationInstant - systemInstant}. A positive duration
     * means the application clock is ahead of real time, while a negative duration means it is
     * behind real time.
     *
     * @return the application-time offset relative to system time
     */
    public Duration getOffsetDuration() {
      return offsetDuration;
    }

    /**
     * Returns the application-time offset relative to system time as a calendar-aware ISO-8601
     * duration string derived from the state instants and zone.
     *
     * @return the calendar-aware ISO-8601 duration string
     */
    public String getOffsetIso8601() {
      return offsetIso8601;
    }

    /**
     * Returns the underlying wall-clock instant.
     *
     * @return the underlying system instant
     */
    public Instant getSystemInstant() {
      return systemInstant;
    }

    /**
     * Returns the zone used to derive calendar-aware values for the clock state.
     *
     * @return the application clock zone identifier
     */
    public String getZoneId() {
      return zoneId;
    }

    /**
     * Indicates whether the application clock is currently offset from wall-clock time.
     *
     * @return {@code true} when time travel is active, otherwise {@code false}
     */
    public boolean isTimeTravelActive() {
      return timeTravelActive;
    }
  }
}
