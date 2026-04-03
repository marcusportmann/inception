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

import java.time.Duration;
import tools.jackson.core.JsonGenerator;
import tools.jackson.databind.SerializationContext;
import tools.jackson.databind.ValueSerializer;

/**
 * The {@code DurationSerializer} class implements the Jackson serializer for the {@code Duration}
 * type.
 *
 * @author Marcus Portmann
 */
public class DurationSerializer extends ValueSerializer<Duration> {

  /** Constructs a new {@code DurationSerializer}. */
  public DurationSerializer() {}

  @Override
  public void serialize(
      Duration duration, JsonGenerator jsonGenerator, SerializationContext context) {
    jsonGenerator.writeString(duration.toString());
  }
}
