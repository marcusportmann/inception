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
import digital.inception.core.model.CodeEnum;
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

  // Cache for conversion methods (either annotated with @JsonCreator or valueOf(String))
  private static final ConcurrentHashMap<Class<? extends Enum<?>>, Method> enumConversionMethods =
      new ConcurrentHashMap<>();

  // Cache for the fromCode methods for Enums implementing the CodeEnum interface
  private static final ConcurrentHashMap<Class<? extends Enum<?>>, Method> enumFromCodeMethods =
      new ConcurrentHashMap<>();

  // Cache for the @JsonValue method (if one exists)
  private static final ConcurrentHashMap<Class<? extends Enum<?>>, Method> enumJsonValueMethods =
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

      if (enumValue == null) {
        throw new HttpMessageNotReadableException("No enum value provided", inputMessage);
      }

      // If the enum implements CodeEnum, use its static fromCode method directly.
      if (CodeEnum.class.isAssignableFrom(clazz)) {
        return (Enum<?>)
            enumFromCodeMethods
                .computeIfAbsent(
                    clazz,
                    clz -> {
                      try {
                        return CodeEnum.class.getMethod("fromCode", Class.class, String.class);
                      } catch (Throwable e) {
                        throw new RuntimeException(
                            "Failed to retrieve the enum conversion method (fromCode) for the Enum class ("
                                + clz.getSimpleName()
                                + ") implementing the CodeEnum interface",
                            e);
                      }
                    })
                .invoke(null, clazz, enumValue);
      }

      // Otherwise, look up the conversion method (cached).
      Method conversionMethod = getEnumConversionMethod(clazz);
      Object enumObject = conversionMethod.invoke(null, enumValue);
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
  protected void writeInternal(@NonNull Enum<?> value, @NonNull HttpOutputMessage outputMessage)
      throws IOException, HttpMessageNotWritableException {
    try (OutputStreamWriter writer =
        new OutputStreamWriter(outputMessage.getBody(), StandardCharsets.UTF_8)) {

      if (value instanceof CodeEnum codeEnum) {
        writer.write(codeEnum.code());
      } else {
        Method jsonValueMethod = getJsonValueMethod(value.getClass());
        if (jsonValueMethod != null) {
          Object enumValue = jsonValueMethod.invoke(value);
          writer.write(enumValue != null ? enumValue.toString() : "");
        } else {
          writer.write(value.name());
        }
      }
    } catch (Throwable e) {
      throw new HttpMessageNotWritableException(
          "Failed to write the enum value (" + value + ")", e);
    }
  }

  /**
   * Returns the conversion method for the enum class.
   *
   * <p>This method either returns a method annotated with @JsonCreator that takes a single String
   * parameter or the standard valueOf(String) method.
   */
  private Method getEnumConversionMethod(Class<? extends Enum<?>> clazz) {
    return enumConversionMethods.computeIfAbsent(
        clazz,
        clz -> {
          try {
            // Look for a method annotated with @JsonCreator with a single parameter assignable from
            // String.
            for (Method method : clz.getMethods()) {
              if (method.isAnnotationPresent(JsonCreator.class)) {
                Class<?>[] params = method.getParameterTypes();
                if (params.length == 1 && params[0].isAssignableFrom(String.class)) {
                  return method;
                }
              }
            }
            // Fallback to the standard valueOf(String) method.
            return clz.getMethod("valueOf", String.class);
          } catch (Throwable e) {
            throw new RuntimeException(
                "Failed to retrieve the enum conversion method (valueOf) for the Enum class ("
                    + clz.getSimpleName()
                    + ")",
                e);
          }
        });
  }

  /** Returns the method annotated with @JsonValue for the enum class if available. */
  private Method getJsonValueMethod(Class<?> clazz) {
    @SuppressWarnings("unchecked")
    Class<? extends Enum<?>> enumClass = (Class<? extends Enum<?>>) clazz;
    return enumJsonValueMethods.computeIfAbsent(
        enumClass,
        clz -> {
          for (Method method : clz.getMethods()) {
            if (method.isAnnotationPresent(JsonValue.class)) {
              return method;
            }
          }
          return null;
        });
  }
}
