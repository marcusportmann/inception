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

package digital.inception.api.converter;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.AbstractHttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

/**
 * The <b>EnumMessageConverter</b> class.
 *
 * @author Marcus Portmann
 */
@Component
public class EnumMessageConverter extends AbstractHttpMessageConverter<Enum<?>> {

  private static final ConcurrentHashMap<Class<? extends Enum<?>>, Method> enumConversionMethods =
      new ConcurrentHashMap<>();

  /** Constructs a new <b>EnumMessageConverter</b>. */
  public EnumMessageConverter() {
    super(MediaType.TEXT_PLAIN);
  }

  @Override
  @NonNull
  protected Enum<?> readInternal(
      @NonNull Class<? extends Enum<?>> clazz, @NonNull HttpInputMessage inputMessage)
      throws IOException, HttpMessageNotReadableException {
    try (BufferedReader reader =
        new BufferedReader(new InputStreamReader(inputMessage.getBody(), StandardCharsets.UTF_8))) {
      String enumValue = reader.readLine();

      Method enumConversionMethod = getEnumConversionMethod(clazz);

      Object enumObject = enumConversionMethod.invoke(null, enumValue);

      return clazz.cast(enumObject);
    } catch (Throwable e) {
      throw new HttpMessageNotReadableException("Failed to read the enum value", e, inputMessage);
    }
  }

  @Override
  protected boolean supports(@NonNull Class<?> clazz) {
    return clazz.isEnum();
  }

  @Override
  protected void writeInternal(@NonNull Enum value, @NonNull HttpOutputMessage outputMessage)
      throws IOException, HttpMessageNotWritableException {
    try (OutputStreamWriter writer =
        new OutputStreamWriter(outputMessage.getBody(), StandardCharsets.UTF_8)) {

      for (Method method : value.getClass().getMethods()) {
        if (method.isAnnotationPresent(JsonValue.class)) {
          Object enumValue = method.invoke(value);

          writer.write(enumValue.toString());
          return;
        }
      }

      writer.write(value.name());
    } catch (Throwable e) {
      throw new HttpMessageNotWritableException("Failed to write the enum value", e);
    }
  }

  private Method getEnumConversionMethod(Class<? extends Enum<?>> clazz) {
    Method enumConversionMethod = enumConversionMethods.get(clazz);

    if (enumConversionMethod != null) {
      return enumConversionMethod;
    }

    try {
      for (Method method : clazz.getMethods()) {
        if (method.isAnnotationPresent(JsonCreator.class)) {
          Class<?>[] methodParameterTypes = method.getParameterTypes();

          if ((methodParameterTypes.length == 1)
              && (methodParameterTypes[0].isAssignableFrom(String.class))) {
            enumConversionMethods.put(clazz, method);
            return method;
          }
        }
      }

      Method method = clazz.getMethod("valueOf", String.class);
      enumConversionMethods.put(clazz, method);
      return method;
    } catch (Throwable e) {
      throw new RuntimeException("Failed to retrieve the enum conversion method", e);
    }
  }
}
