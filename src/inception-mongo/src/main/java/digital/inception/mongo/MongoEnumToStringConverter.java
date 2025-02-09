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

package digital.inception.mongo;

import com.fasterxml.jackson.annotation.JsonValue;
import java.lang.reflect.Method;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.WritingConverter;

/**
 * The <b>MongoEnumToStringConverter</b> class implements a custom WritingConverter, which converts
 * Enum values to String values using the Jackson approach. This approach uses the
 * {@code @JsonValue} annotation on a method on the custom Enum class. If this is not possible, the
 * standard approach of using the {@code name()} method on the Enum class is used.
 *
 * @author Marcus Portmann
 */
@WritingConverter
public class MongoEnumToStringConverter implements Converter<Enum<?>, String> {

  // Cache for @JsonValue lookups. For each Enum class, we cache the found Method (or an empty
  // Optional) so that reflection is performed only once per class.
  private static final ConcurrentHashMap<Class<?>, Optional<Method>> jsonValueMethodCache =
      new ConcurrentHashMap<>();

  /** Constructs a new <b>MongoEnumToStringConverter</b>. */
  public MongoEnumToStringConverter() {}

  @Override
  public String convert(Enum<?> source) {
    if (source == null) {
      return null;
    }
    Method jsonValueMethod = findJsonValueMethod(source.getClass());
    if (jsonValueMethod != null) {
      try {
        Object value = jsonValueMethod.invoke(source);
        return (value != null) ? value.toString() : null;
      } catch (Throwable e) {
        throw new IllegalStateException(
            "Failed to convert the enum "
                + source.getClass().getName()
                + " to a String using @JsonValue",
            e);
      }
    }
    // Fallback: use the enum’s name
    return source.name();
  }

  /**
   * Finds a method annotated with {@code @JsonValue} in the given enum class. The result (including
   * an absence) is cached so that reflection is only performed once per enum class.
   *
   * @param enumClass the Enum class to check
   * @return the method annotated with {@code @JsonValue} or {@code null} if none is found
   */
  private Method findJsonValueMethod(Class<?> enumClass) {
    // First, check the cache
    Optional<Method> cached = jsonValueMethodCache.get(enumClass);
    if (cached != null) {
      return cached.orElse(null);
    }
    // No cached value: perform reflection to find a method annotated with @JsonValue
    Method foundMethod = null;
    for (Method method : enumClass.getMethods()) {
      if (method.isAnnotationPresent(JsonValue.class)) {
        foundMethod = method;
        break;
      }
    }
    // Cache the result (either the found method or an empty Optional if none found)
    jsonValueMethodCache.put(enumClass, Optional.ofNullable(foundMethod));
    return foundMethod;
  }
}
