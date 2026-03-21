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

package digital.inception.json.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import digital.inception.core.time.ApplicationClock;
import digital.inception.json.JsonUtil;
import java.time.Duration;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

/**
 * JSON serialization tests.
 *
 * @author Marcus Portmann
 */
public class JsonSerializationTests {

  @Test
  void objectMapperDeserializesDurationFromIso8601String() throws Exception {
    Duration duration = JsonUtil.getObjectMapper().readValue("\"PT2H\"", Duration.class);

    assertEquals(Duration.ofHours(2), duration);
  }

  @Test
  void objectMapperSerializesApplicationClockStateWithCalendarAwareOffset() throws Exception {
    ApplicationClock.advanceBy(Duration.ofHours(200));

    String json = JsonUtil.getObjectMapper().writeValueAsString(ApplicationClock.getState());

    assertTrue(json.contains("\"offsetDuration\":\"PT200H\""));
    assertTrue(json.contains("\"offsetIso8601\":\"P8DT8H\""));
  }

  @Test
  void objectMapperSerializesDurationAsIso8601String() throws Exception {
    String json = JsonUtil.getObjectMapper().writeValueAsString(Duration.ofHours(2));

    assertEquals("\"PT2H\"", json);
  }

  @AfterEach
  void tearDown() {
    ApplicationClock.reset();
  }
}
