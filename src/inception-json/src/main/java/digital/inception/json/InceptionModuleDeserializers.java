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

import digital.inception.core.model.CodeEnum;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.OffsetTime;
import java.time.ZonedDateTime;
import java.util.Date;
import tools.jackson.databind.BeanDescription;
import tools.jackson.databind.DeserializationConfig;
import tools.jackson.databind.JavaType;
import tools.jackson.databind.ValueDeserializer;
import tools.jackson.databind.module.SimpleDeserializers;

/**
 * The {@code InceptionModuleDeserializers} class dynamically provides a {@code
 * CodeEnumDeserializer} for an enum implementing the {@link CodeEnum} interface.
 *
 * @author Marcus Portmann
 */
public class InceptionModuleDeserializers extends SimpleDeserializers {

  /** Constructs a new {@code InceptionModuleDeserializers} instance. */
  public InceptionModuleDeserializers() {
    addDeserializer(Date.class, new DateDeserializer());
    addDeserializer(Duration.class, new DurationDeserializer());
    addDeserializer(Instant.class, new InstantDeserializer());
    addDeserializer(LocalDate.class, new LocalDateDeserializer());
    addDeserializer(LocalTime.class, new LocalTimeDeserializer());
    addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer());
    addDeserializer(OffsetDateTime.class, new OffsetDateTimeDeserializer());
    addDeserializer(OffsetTime.class, new OffsetTimeDeserializer());
    addDeserializer(ZonedDateTime.class, new ZonedDateTimeDeserializer());
  }

  @Override
  public ValueDeserializer<?> findEnumDeserializer(
      JavaType enumType, DeserializationConfig config, BeanDescription.Supplier beanDescRef) {

    Class<?> rawClass = enumType.getRawClass();

    if (rawClass.isEnum() && CodeEnum.class.isAssignableFrom(rawClass)) {
      return new CodeEnumDeserializer<>(enumType);
    }

    return super.findEnumDeserializer(enumType, config, beanDescRef);
  }
}
