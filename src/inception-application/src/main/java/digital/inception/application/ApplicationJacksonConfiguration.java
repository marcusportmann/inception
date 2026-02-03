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

package digital.inception.application;

import com.fasterxml.jackson.databind.ObjectMapper;
import digital.inception.json.JsonUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * The {@code ApplicationJacksonConfiguration} class initializes the Jackson components for the
 * application.
 *
 * @author Marcus Portmann
 */
@Configuration
public class ApplicationJacksonConfiguration {

  /** Constructs a new {@code ApplicationJacksonConfiguration}. */
  public ApplicationJacksonConfiguration() {}

  /**
   * Returns the Jackson2 object mapper.
   *
   * @return the Jackson2 object mapper
   */
  @Bean
  public ObjectMapper objectMapper() {
    return JsonUtil.getObjectMapper();
  }
}
