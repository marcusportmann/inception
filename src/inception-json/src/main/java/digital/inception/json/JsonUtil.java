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

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

/**
 * The <b>JsonUtil</b> class provides JSON-related and Jackson-related utility methods.
 *
 * @author Marcus Portmann
 */
@SuppressWarnings("unused")
public final class JsonUtil {

  private static final ObjectMapper objectMapper;

  static {
    objectMapper = new ObjectMapper();
    objectMapper.disable(SerializationFeature.INDENT_OUTPUT);
    objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
    objectMapper.registerModule(new InceptionDateTimeModule());
  }

  /** Private constructor to prevent instantiation. */
  private JsonUtil() {}

  /**
   * Retrieve the Jackson2 object mapper configured with the required modules and features.
   *
   * @return the Jackson2 object mapper configured with the required modules and features
   */
  public static ObjectMapper getObjectMapper() {
    return objectMapper;
  }
}
