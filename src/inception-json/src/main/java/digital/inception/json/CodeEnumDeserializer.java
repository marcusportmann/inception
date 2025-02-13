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
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer;
import digital.inception.core.model.CodeEnum;
import java.io.IOException;

/**
 * The <b>CodeEnumDeserializer</b> class implements the Jackson deserializer that can deserialize a
 * JSON string into any Enum that implements {@link CodeEnum}, by invoking {@code
 * CodeEnum.fromCode(...)}.
 *
 * @param <E> the enumeration type
 * @author Marcus Portmann
 */
public class CodeEnumDeserializer<E extends Enum<E> & CodeEnum> extends JsonDeserializer<E> {

  private final JavaType targetType;

  /**
   * Constructs a new <b>CodeEnumDeserializer</b>.
   *
   * @param targetType the target Enum type
   */
  public CodeEnumDeserializer(JavaType targetType) {
    this.targetType = targetType;
  }

  @Override
  public E deserialize(JsonParser jsonParser, DeserializationContext deserializationContext)
      throws IOException {
    String code = jsonParser.getValueAsString();
    if (code == null || code.isEmpty()) {
      return null; // or handle as you prefer (throw exception, etc.)
    }
    @SuppressWarnings("unchecked")
    Class<E> rawClass = (Class<E>) targetType.getRawClass();
    // Use the CodeEnum logic to find the correct enum constant
    return CodeEnum.fromCode(rawClass, code);
  }
}
