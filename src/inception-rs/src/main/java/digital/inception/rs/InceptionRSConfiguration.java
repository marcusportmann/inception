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

//~--- non-JDK imports --------------------------------------------------------

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

//~--- JDK imports ------------------------------------------------------------

import java.util.Arrays;
import java.util.Collections;

/**
 * The <code>InceptionRSConfiguration</code> class provides the configuration for the
 * <b>inception-rs</b> library.
 *
 * @author Marcus Portmann
 */
@Configuration
public class InceptionRSConfiguration
{
  /**
   * Enable cross-origin resource sharing (CORS).
   */
  @Value("${server.enableCORS:#{false}}")
  private boolean isCORSEnabled;

  /**
   * Returns the cross-origin resource sharing (CORS) filter registration bean.
   *
   * @return the cross-origin resource sharing (CORS) filter registration bean
   */
  @Bean
  protected FilterRegistrationBean<CorsFilter> corsFilterRegistrationBean()
  {
    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();

    if (isCORSEnabled)
    {
      CorsConfiguration config = new CorsConfiguration();
      config.applyPermitDefaultValues();
      config.setAllowCredentials(true);
      config.setAllowedOrigins(Collections.singletonList("*"));
      config.setAllowedHeaders(Collections.singletonList("*"));
      config.setAllowedMethods(Collections.singletonList("*"));
      config.setExposedHeaders(Arrays.asList("Content-Length", "X-Total-Count"));
      config.setMaxAge(3600L);
      source.registerCorsConfiguration("/**", config);
    }

    FilterRegistrationBean<CorsFilter> bean = new FilterRegistrationBean<>(new CorsFilter(source));
    bean.setOrder(0);

    return bean;
  }
}
