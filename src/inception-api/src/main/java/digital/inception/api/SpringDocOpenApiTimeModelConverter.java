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
import io.swagger.v3.core.converter.AnnotatedType;
import io.swagger.v3.core.converter.ModelConverter;
import io.swagger.v3.core.converter.ModelConverterContext;
import io.swagger.v3.oas.models.media.Schema;
import java.time.LocalTime;
import java.time.OffsetTime;
import java.util.Iterator;

/**
 * The <b>SpringDocOpenApiTimeModelConverter</b> class.
 *
 * @author Marcus Portmann
 */
public class SpringDocOpenApiTimeModelConverter implements ModelConverter {

  /** Constructs a new <b>SpringDocOpenApiTimeModelConverter</b>. */
  public SpringDocOpenApiTimeModelConverter() {}

  @Override
  public Schema<?> resolve(
      AnnotatedType annotatedType, ModelConverterContext context, Iterator<ModelConverter> chain) {
    if (annotatedType.getType() instanceof SimpleType simpleType) {
      if (simpleType.getRawClass() == LocalTime.class) {
        return new Schema<String>().type("string").format("time").example("12:34:56.123");
      } else if (simpleType.getRawClass() == OffsetTime.class) {
        return new Schema<String>().type("string").format("time-offset").example("12:34:56.123+01:00");
      }
    }
    return (chain != null && chain.hasNext())
        ? chain.next().resolve(annotatedType, context, chain)
        : null;
  }
}
