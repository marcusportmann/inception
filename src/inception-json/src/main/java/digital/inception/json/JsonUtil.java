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

import tools.jackson.databind.DeserializationFeature;
import tools.jackson.databind.SerializationFeature;
import tools.jackson.databind.json.JsonMapper;

/**
 * The {@code JsonUtil} class provides JSON-related and Jackson-related utility methods.
 *
 * @author Marcus Portmann
 */
@SuppressWarnings("unused")
public final class JsonUtil {

  private static final JsonMapper OBJECT_MAPPER =
      JsonMapper.builder()
          .disable(SerializationFeature.INDENT_OUTPUT)
          .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
          .addModule(new InceptionModule())
          .build();

  /** Private constructor to prevent instantiation. */
  private JsonUtil() {}

  /**
   * Retrieve the Jackson object mapper configured with the required modules and features.
   *
   * @return the Jackson object mapper configured with the required modules and features
   */
  public static JsonMapper getObjectMapper() {
    return OBJECT_MAPPER;
  }
}
