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
import digital.inception.core.model.CodeEnum;
import io.swagger.v3.core.converter.AnnotatedType;
import io.swagger.v3.core.converter.ModelConverter;
import io.swagger.v3.core.converter.ModelConverterContext;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.media.StringSchema;
import java.time.LocalTime;
import java.time.OffsetTime;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * The <b>SpringDocOpenApiModelConverter</b> class.
 *
 * @author Marcus Portmann
 */
public class SpringDocOpenApiModelConverter implements ModelConverter {

  /** Constructs a new <b>SpringDocOpenApiModelConverter</b>. */
  public SpringDocOpenApiModelConverter() {}

  @Override
  public Schema<?> resolve(
      AnnotatedType annotatedType, ModelConverterContext context, Iterator<ModelConverter> chain) {
    // Extract the raw class from the AnnotatedType's Type
    final Object type = annotatedType.getType();
    final Class<?> rawClass =
        type instanceof SimpleType simpleType
            ? simpleType.getRawClass()
            : type instanceof Class<?> clazz ? clazz : null;

    if (rawClass != null) {
      // Special handling for LocalTime and OffsetTime
      if (rawClass == LocalTime.class) {
        return new StringSchema().type("string").format("time").example("12:34:56.123");
      }
      if (rawClass == OffsetTime.class) {
        return new StringSchema()
            .type("string")
            .format("time-offset")
            .example("12:34:56.123+01:00");
      }
      // Handle CodeEnum types by populating the enum values on the schema
      if (CodeEnum.class.isAssignableFrom(rawClass)) {
        @SuppressWarnings("unchecked")
        Class<? extends CodeEnum> codeEnumClass = (Class<? extends CodeEnum>) rawClass;
        Schema<?> schema =
            (chain != null && chain.hasNext())
                ? chain.next().resolve(annotatedType, context, chain)
                : null;
        if (schema != null) {
          if (schema instanceof StringSchema stringSchema) {
            List<String> enumCodes =
                Arrays.stream(codeEnumClass.getEnumConstants())
                    .map(CodeEnum::code)
                    .collect(Collectors.toList());
            stringSchema.setEnum(enumCodes);
            return schema;
          }
          throw new RuntimeException(
              "Failed to populate the enum values for the CodeEnum ("
                  + codeEnumClass
                  + ") on the the unexpected schema class ("
                  + schema.getClass()
                  + ")");
        }
      }
    }

    // Fallback to the next converter in the chain if available
    return (chain != null && chain.hasNext())
        ? chain.next().resolve(annotatedType, context, chain)
        : null;
  }
}
