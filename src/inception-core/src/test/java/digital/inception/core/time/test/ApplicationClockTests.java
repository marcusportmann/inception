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

package digital.inception.core.time.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import digital.inception.core.time.ApplicationClock;
import java.lang.reflect.Method;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link ApplicationClock}.
 *
 * @author Marcus Portmann
 */
public class ApplicationClockTests {

  @Test
  void advanceByUpdatesStateAndZoneAwareAccessors() {
    ApplicationClock.advanceBy(Duration.ofHours(6));

    ApplicationClock.State state = ApplicationClock.getState();
    ZoneId zoneId = ZoneId.of("Africa/Johannesburg");
    OffsetDateTime offsetNow = ApplicationClock.offsetNow(zoneId);

    assertTrue(state.isTimeTravelActive());
    assertEquals(Duration.ofHours(6), state.getOffsetDuration());
    assertEquals("PT6H", state.getOffsetIso8601());
    assertTrue(
        Duration.between(state.getSystemInstant(), state.getApplicationInstant())
                .minus(Duration.ofHours(6))
                .abs()
                .compareTo(Duration.ofSeconds(1))
            < 0);
    assertEquals(
        LocalDate.ofInstant(state.getApplicationInstant(), zoneId), ApplicationClock.today(zoneId));
    assertTrue(
        Duration.between(state.getApplicationInstant(), offsetNow.toInstant())
                .abs()
                .compareTo(Duration.ofSeconds(1))
            < 0);
  }

  @Test
  void formatCalendarIso8601DurationWhenInstantsSpanMonthsUsesCalendarComponents()
      throws Exception {
    Method method =
        ApplicationClock.class.getDeclaredMethod(
            "formatCalendarIso8601Duration", Instant.class, Instant.class, ZoneId.class);
    method.setAccessible(true);

    String formattedDuration =
        (String)
            method.invoke(
                null,
                Instant.parse("2026-01-15T08:00:00Z"),
                Instant.parse("2026-03-15T08:00:00Z"),
                ZoneId.of("UTC"));

    assertEquals("P2M", formattedDuration);
  }

  @Test
  void getStateWhenResetReportsSystemTime() {
    ApplicationClock.State state = ApplicationClock.getState();

    assertFalse(state.isTimeTravelActive());
    assertEquals(ApplicationClock.getZone().getId(), state.getZoneId());
    assertEquals(Duration.ZERO, state.getOffsetDuration());
    assertEquals("PT0S", state.getOffsetIso8601());
    assertTrue(
        Duration.between(state.getSystemInstant(), state.getApplicationInstant())
                .abs()
                .compareTo(Duration.ofSeconds(1))
            < 0);
  }

  @Test
  void jumpToAndResetSwitchTimeTravelOnAndOff() {
    Instant target = Instant.parse("2035-04-10T12:00:00Z");

    ApplicationClock.jumpTo(target);

    ApplicationClock.State state = ApplicationClock.getState();
    assertTrue(state.isTimeTravelActive());
    assertTrue(
        Duration.between(target, state.getApplicationInstant())
                .abs()
                .compareTo(Duration.ofSeconds(1))
            < 0);

    ApplicationClock.reset();

    ApplicationClock.State resetState = ApplicationClock.getState();
    assertFalse(resetState.isTimeTravelActive());
    assertEquals(Duration.ZERO, resetState.getOffsetDuration());
    assertEquals("PT0S", resetState.getOffsetIso8601());
    assertTrue(
        Duration.between(resetState.getSystemInstant(), resetState.getApplicationInstant())
                .abs()
                .compareTo(Duration.ofSeconds(1))
            < 0);
  }

  @Test
  void jumpToInPastReportsNegativeOffsetDuration() {
    Duration expectedOffset = Duration.ofHours(-3);
    ApplicationClock.jumpTo(Instant.now().plus(expectedOffset));

    ApplicationClock.State state = ApplicationClock.getState();

    assertTrue(state.getOffsetDuration().isNegative());
    assertTrue(
        state.getOffsetDuration().minus(expectedOffset).abs().compareTo(Duration.ofSeconds(1)) < 0);
  }

  @AfterEach
  void tearDown() {
    ApplicationClock.reset();
  }
}
