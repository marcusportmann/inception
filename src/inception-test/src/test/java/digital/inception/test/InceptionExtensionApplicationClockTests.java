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

package digital.inception.test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import digital.inception.core.time.ApplicationClock;
import java.time.Duration;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;

/**
 * Verifies that {@link InceptionExtension} resets {@link ApplicationClock} after each test.
 *
 * @author Marcus Portmann
 */
@ExtendWith(InceptionExtension.class)
@TestMethodOrder(OrderAnnotation.class)
public class InceptionExtensionApplicationClockTests {

  @Test
  @Order(1)
  void firstTestCanEnableTimeTravel() {
    ApplicationClock.advanceBy(Duration.ofDays(1));

    assertTrue(ApplicationClock.getState().isTimeTravelActive());
  }

  @Test
  @Order(2)
  void secondTestStartsWithClockReset() {
    assertFalse(ApplicationClock.getState().isTimeTravelActive());
  }
}
