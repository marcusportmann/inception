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
import java.time.LocalDate;
import tools.jackson.core.JsonGenerator;
import tools.jackson.databind.SerializationContext;
import tools.jackson.databind.ValueSerializer;

/**
 * The {@code LocalDateSerializer} class implements the Jackson serializer for the {@code LocalDate}
 * type.
 *
 * @author Marcus Portmann
 */
public class LocalDateSerializer extends ValueSerializer<LocalDate> {

  /** Constructs a new {@code LocalDateSerializer}. */
  public LocalDateSerializer() {}

  @Override
  public void serialize(
      LocalDate localDate, JsonGenerator jsonGenerator, SerializationContext context) {
    jsonGenerator.writeString(ISO8601Util.fromLocalDate(localDate));
  }
}
