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

package digital.inception.json;

import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.module.SimpleModule;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.OffsetTime;
import java.time.ZonedDateTime;
import java.util.Date;

/**
 * The <b>InceptionDateTimeModule</b> implements the custom Jackson module that registers the
 * serializer and deserializer extensions that support the Java 8 Date/Time API.
 *
 * @author Marcus Portmann
 */
public class InceptionDateTimeModule extends SimpleModule {

  /** Constructs a new <b>InceptionDateTimeModule</b>. */
  public InceptionDateTimeModule() {
    super(
        "InceptionDateTimeModule",
        new Version(1, 0, 0, null, "digital.inception", "inception-json"));

    addHandlers(Date.class, new DateSerializer(), new DateDeserializer());
    addHandlers(Instant.class, new InstantSerializer(), new InstantDeserializer());
    addHandlers(LocalDate.class, new LocalDateSerializer(), new LocalDateDeserializer());
    addHandlers(LocalTime.class, new LocalTimeSerializer(), new LocalTimeDeserializer());
    addHandlers(
        LocalDateTime.class, new LocalDateTimeSerializer(), new LocalDateTimeDeserializer());
    addHandlers(
        OffsetDateTime.class, new OffsetDateTimeSerializer(), new OffsetDateTimeDeserializer());
    addHandlers(OffsetTime.class, new OffsetTimeSerializer(), new OffsetTimeDeserializer());
    addHandlers(
        ZonedDateTime.class, new ZonedDateTimeSerializer(), new ZonedDateTimeDeserializer());
  }

  private <T> void addHandlers(
      Class<T> type, JsonSerializer<T> serializer, JsonDeserializer<T> deserializer) {
    addSerializer(type, serializer);
    addDeserializer(type, deserializer);
  }
}
