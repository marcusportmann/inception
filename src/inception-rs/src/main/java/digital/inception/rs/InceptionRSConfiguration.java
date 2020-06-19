/*
 * Copyright 2019 Marcus Portmann
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

package digital.inception.rs;

// ~--- non-JDK imports --------------------------------------------------------

import java.util.Arrays;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

// ~--- JDK imports ------------------------------------------------------------

/**
 * The <code>InceptionRSConfiguration</code> class provides the configuration for the
 * <b>inception-rs</b> library.
 *
 * @author Marcus Portmann
 */
@Configuration
public class InceptionRSConfiguration {

  /**
   * Instruct browsers to expose the response to frontend JavaScript code when the request's
   * credentials mode (Request.credentials) is "include".
   */
  @Value("${server.cors.allowCredentials:false}")
  private boolean corsAllowCredentials;

  /** Instruct browsers to allow the specified headers. */
  @Value("${server.cors.allowedHeaders:}")
  private String[] corsAllowedHeaders;

  /** Instruct browsers to allow the specified methods. */
  @Value("${server.cors.allowedMethods:}")
  private String[] corsAllowedMethods;

  /** Instruct browsers to allow code from the specified origins. */
  @Value("${server.cors.allowedOrigins:}")
  private String[] corsAllowedOrigins;

  /** Enable cross-origin resource sharing (CORS). */
  @Value("${server.cors.enabled:false}")
  private boolean corsEnabled;

  /** Instruct browsers to expose the specified headers. */
  @Value("${server.cors.exposedHeaders:}")
  private String[] corsExposedHeaders;

  /**
   * How long the results of a preflight request (that is the information contained in the
   * Access-Control-Allow-Methods and Access-Control-Allow-Headers headers) can be cached.
   */
  @Value("${server.cors.maxAge:3600}")
  private long corsMaxAge;

  /**
   * Returns the cross-origin resource sharing (CORS) filter registration bean.
   *
   * @return the cross-origin resource sharing (CORS) filter registration bean
   */
  @Bean
  protected FilterRegistrationBean<CorsFilter> corsFilterRegistrationBean() {
    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();

    if (corsEnabled) {
      CorsConfiguration config = new CorsConfiguration();
      config.applyPermitDefaultValues();
      config.setAllowCredentials(corsAllowCredentials);
      config.setAllowedOrigins(Arrays.asList(corsAllowedOrigins));
      config.setAllowedHeaders(Arrays.asList(corsAllowedHeaders));
      config.setAllowedMethods(Arrays.asList(corsAllowedMethods));
      config.setExposedHeaders(Arrays.asList(corsExposedHeaders));
      config.setMaxAge(corsMaxAge);
      source.registerCorsConfiguration("/**", config);
    }

    FilterRegistrationBean<CorsFilter> bean = new FilterRegistrationBean<>(new CorsFilter(source));
    bean.setOrder(0);

    return bean;
  }
}
