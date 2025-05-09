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

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import digital.inception.core.model.CodeEnum;
import java.io.IOException;

/**
 * The {@code CodeEnumSerializer} class implements the Jackson serializer for any Enum that
 * implements {@link CodeEnum}, by invoking {@code * CodeEnum.code(...)}.
 *
 * @author Marcus Portmann
 */
public class CodeEnumSerializer extends JsonSerializer<CodeEnum> {

  /** Constructs a new {@code CodeEnumSerializer}. */
  public CodeEnumSerializer() {}

  @Override
  public void serialize(
      CodeEnum codeEnum, JsonGenerator jsonGenerator, SerializerProvider serializers)
      throws IOException {
    jsonGenerator.writeString(codeEnum.code());
  }
}
