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

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import digital.inception.core.util.ISO8601Util;
import java.io.IOException;
import java.time.OffsetTime;

/**
 * The {@code OffsetTimeDeserializer} class implements the Jackson deserializer for the {@code
 * OffsetTime} type.
 *
 * @author Marcus Portmann
 */
public class OffsetTimeDeserializer extends JsonDeserializer<OffsetTime> {

  /** Constructs a new {@code OffsetTimeDeserializer}. */
  public OffsetTimeDeserializer() {}

  @Override
  public OffsetTime deserialize(
      JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
    try {
      return ISO8601Util.toOffsetTime(jsonParser.getValueAsString());
    } catch (Throwable e) {
      throw new IOException(
          "Failed to deserialize the ISO 8601 value (" + jsonParser.getValueAsString() + ")");
    }
  }
}
