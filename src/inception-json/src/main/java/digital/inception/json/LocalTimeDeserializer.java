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

import digital.inception.core.util.ISO8601Util;
import java.time.LocalTime;
import tools.jackson.core.JsonParser;
import tools.jackson.databind.DeserializationContext;
import tools.jackson.databind.ValueDeserializer;

/**
 * The {@code LocalTimeDeserializer} class implements the Jackson deserializer for the {@code
 * LocalTime} type.
 *
 * @author Marcus Portmann
 */
public class LocalTimeDeserializer extends ValueDeserializer<LocalTime> {

  /** Constructs a new {@code LocalTimeDeserializer}. */
  public LocalTimeDeserializer() {}

  @Override
  public LocalTime deserialize(
      JsonParser jsonParser, DeserializationContext deserializationContext) {
    try {
      return ISO8601Util.toLocalTime(jsonParser.getValueAsString());
    } catch (Throwable e) {
      throw new RuntimeException(
          "Failed to deserialize the ISO 8601 value (" + jsonParser.getValueAsString() + ")", e);
    }
  }
}
