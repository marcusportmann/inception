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

package digital.inception.api;

import com.fasterxml.jackson.databind.type.SimpleType;
import io.swagger.v3.core.converter.ModelConverters;
import jakarta.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.OffsetTime;
import java.util.Set;
import org.springdoc.core.customizers.PropertyCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * The <b>SpringDocOpenApiConfig</b> class provides the customization configuration for the
 * <b>springdoc-openapi</b> library.
 *
 * @author Marcus Portmann
 */
@Configuration
public class SpringDocOpenApiConfig {

  /** Constructs a new <b>SpringDocOpenApiConfig</b>. */
  public SpringDocOpenApiConfig() {}

  /** Register the time model converter to support LocalTime and OffsetTime properties. */
  @PostConstruct
  public void registerTimeModelConverter() {
    ModelConverters.getInstance().addConverter(new SpringDocOpenApiTimeModelConverter());
  }

  /**
   * Returns the schema property customizer for time-related properties for the
   * <b>springdoc-openapi</b> library.
   *
   * @return the schema property customizer for time-related properties for the
   *     <b>springdoc-openapi</b> library
   */
  @SuppressWarnings("unchecked")
  @Bean
  public PropertyCustomizer timePropertyCustomizer() {
    return (property, annotatedType) -> {
      if (annotatedType.getType() instanceof SimpleType simpleType) {
        if (simpleType.getRawClass() == LocalTime.class) {
          property.setTypes(Set.of("string"));
          property.setFormat("time");
          property.setExample("12:34:56.123");
          property.setProperties(null);
          property.set$ref(null);
        } else if (simpleType.getRawClass() == OffsetTime.class) {
          property.setTypes(Set.of("string"));
          property.setFormat("time-offset");
          property.setExample("12:34:56.123+01:00");
          property.setProperties(null);
          property.set$ref(null);
        } else if (simpleType.getRawClass() == LocalDateTime.class) {
          property.setTypes(Set.of("string"));
          property.setFormat("date-time");
          property.setExample("2025-01-20T11:25:11.505");
          property.setProperties(null);
          property.set$ref(null);
        } else if (simpleType.getRawClass() == OffsetDateTime.class) {
          property.setTypes(Set.of("string"));
          property.setFormat("date-time");
          property.setExample("2025-01-20T11:25:11.505+02:00");
          property.setProperties(null);
          property.set$ref(null);
        }
      }

      return property;
    };
  }
}
