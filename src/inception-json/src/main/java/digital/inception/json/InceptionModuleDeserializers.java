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

import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.module.SimpleDeserializers;
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
 * The <b>InceptionModuleDeserializers</b> class dynamically provides a CodeEnumDeserializer for an
 * Enum implementing the CodeEnum interface.
 *
 * @author Marcus Portmann
 */
public class InceptionModuleDeserializers extends SimpleDeserializers {

  /** Constructs a new <b>InceptionModuleDeserializers</b>>. */
  public InceptionModuleDeserializers() {

    addDeserializer(Date.class, new DateDeserializer());
    addDeserializer(Instant.class, new InstantDeserializer());
    addDeserializer(LocalDate.class, new LocalDateDeserializer());
    addDeserializer(LocalTime.class, new LocalTimeDeserializer());
    addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer());
    addDeserializer(OffsetDateTime.class, new OffsetDateTimeDeserializer());
    addDeserializer(OffsetTime.class, new OffsetTimeDeserializer());
    addDeserializer(ZonedDateTime.class, new ZonedDateTimeDeserializer());
  }

  @Override
  public JsonDeserializer<?> findEnumDeserializer(
      Class<?> type, DeserializationConfig config, BeanDescription beanDesc)
      throws JsonMappingException {

    // Is the type an enum that implements CodeEnum?
    if (type.isEnum() && CodeEnum.class.isAssignableFrom(type)) {
      JavaType javaType = config.getTypeFactory().constructType(type);
      // Create a contextual CodeEnumDeserializer for this specific enum subtype
      return new CodeEnumDeserializer<>(javaType);
    }

    // For other enums, let Jackson handle them normally
    return null;
  }
}
