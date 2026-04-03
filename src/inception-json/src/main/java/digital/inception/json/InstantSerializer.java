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
import java.time.Instant;
import tools.jackson.core.JsonGenerator;
import tools.jackson.databind.SerializationContext;
import tools.jackson.databind.ValueSerializer;

/**
 * The {@code InstantSerializer} class implements the Jackson serializer for the {@code Instant}
 * type.
 *
 * @author Marcus Portmann
 */
public class InstantSerializer extends ValueSerializer<Instant> {

  /** Constructs a new {@code InstantSerializer}. */
  public InstantSerializer() {}

  @Override
  public void serialize(
      Instant instant, JsonGenerator jsonGenerator, SerializationContext context) {
    jsonGenerator.writeString(ISO8601Util.fromInstant(instant));
  }
}
