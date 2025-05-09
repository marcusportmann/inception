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

import digital.inception.core.converter.StringToCodeEnumConverterFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.http.MediaType;
import org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * The {@code ApplicationWebMvcConfiguration} class customizes the Java-based configuration for
 * Spring MVC.
 *
 * @author Marcus Portmann
 */
@Configuration
public class ApplicationWebMvcConfiguration implements WebMvcConfigurer {

  private final StringToCodeEnumConverterFactory stringToCodeEnumConverterFactory;

  /**
   * Constructs a new {@code ApplicationWebMvcConfiguration}.
   *
   * @param stringToCodeEnumConverterFactory the String to CodeEnum converter factory
   */
  public ApplicationWebMvcConfiguration(
      StringToCodeEnumConverterFactory stringToCodeEnumConverterFactory) {
    this.stringToCodeEnumConverterFactory = stringToCodeEnumConverterFactory;
  }

  @Override
  public void addFormatters(FormatterRegistry registry) {
    /*
     * NOTE: This enables the conversion of custom Enum types that implement the CodeEnum interface,
     *       e.g. the conversion of request parameters with the Enum type on a Spring rest
     *       controller.
     */
    registry.addConverterFactory(stringToCodeEnumConverterFactory);
  }

  @Override
  public void configureContentNegotiation(ContentNegotiationConfigurer configurer) {
    /*
     * NOTE: This is required to support returning text/plain or application/json content from the
     *       same controller method.
     */
    configurer.ignoreAcceptHeader(true).defaultContentType(MediaType.ALL);
  }
}
