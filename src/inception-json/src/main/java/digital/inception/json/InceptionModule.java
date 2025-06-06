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
import com.fasterxml.jackson.databind.module.SimpleModule;
import digital.inception.core.model.CodeEnum;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.OffsetTime;
import java.time.ZonedDateTime;
import java.util.Date;

/**
 * The {@code InceptionModule} class implements the custom Jackson module that registers the
 * serializer and deserializer extensions that support the Java 8 Date/Time API and enumerations
 * that implement the CodeEnum interface.
 *
 * @author Marcus Portmann
 */
public class InceptionModule extends SimpleModule {

  /** Constructs a new {@code InceptionModule}. */
  public InceptionModule() {
    super("InceptionModule", new Version(1, 0, 0, null, "digital.inception", "inception-json"));

    addSerializer(CodeEnum.class, new CodeEnumSerializer());
    addSerializer(Date.class, new DateSerializer());
    addSerializer(Instant.class, new InstantSerializer());
    addSerializer(LocalDate.class, new LocalDateSerializer());
    addSerializer(LocalTime.class, new LocalTimeSerializer());
    addSerializer(LocalDateTime.class, new LocalDateTimeSerializer());
    addSerializer(OffsetDateTime.class, new OffsetDateTimeSerializer());
    addSerializer(OffsetTime.class, new OffsetTimeSerializer());
    addSerializer(ZonedDateTime.class, new ZonedDateTimeSerializer());

    this.setDeserializers(new InceptionModuleDeserializers());
  }
}
