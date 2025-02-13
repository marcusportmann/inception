/// *
// * Copyright Marcus Portmann
// *
// * Licensed under the Apache License, Version 2.0 (the "License");
// * you may not use this file except in compliance with the License.
// * You may obtain a copy of the License at
// *
// *   http://www.apache.org/licenses/LICENSE-2.0
// *
// * Unless required by applicable law or agreed to in writing, software
// * distributed under the License is distributed on an "AS IS" BASIS,
// * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// * See the License for the specific language governing permissions and
// * limitations under the License.
// */
//
// package digital.inception.mongo;
//
// import com.fasterxml.jackson.annotation.JsonCreator;
// import java.lang.reflect.Method;
// import java.lang.reflect.Modifier;
// import java.util.Arrays;
// import java.util.concurrent.ConcurrentHashMap;
// import org.springframework.core.convert.converter.Converter;
// import org.springframework.core.convert.converter.ConverterFactory;
// import org.springframework.data.convert.ReadingConverter;
//
/// **
// * The <b>MongoStringToEnumConverterFactory</b> class implements a custom ReadingConverter
// factory,
// * which converts String values to Enum values using the Jackson approach. This approach uses the
// * {@code @JsonCreator} annotation on a method on the custom Enum class. If this is not possible,
// * the standard approach of using the {@code valueOf()} method on the Enum class is used.
// */
// @ReadingConverter
// public class MongoStringToEnumConverterFactory implements ConverterFactory<String, Enum<?>> {
//
//  // Cache to avoid repeated reflective lookups per enum class.
//  private static final ConcurrentHashMap<Class<?>, EnumConversionMethods> methodCache =
//      new ConcurrentHashMap<>();
//
//  /** Constructs a new <b>MongoStringToEnumConverterFactory</b>. */
//  public MongoStringToEnumConverterFactory() {}
//
//  @Override
//  public <T extends Enum<?>> Converter<String, T> getConverter(Class<T> targetType) {
//    // Compute and cache the conversion methods for this enum type if not already cached.
//    EnumConversionMethods enumConversionMethods =
//        methodCache.computeIfAbsent(
//            targetType,
//            clazz -> {
//              // Look for a static method annotated with @JsonCreator
//              Method jsonCreator =
//                  Arrays.stream(clazz.getDeclaredMethods())
//                      .filter(method -> Modifier.isStatic(method.getModifiers()))
//                      .filter(method -> method.isAnnotationPresent(JsonCreator.class))
//                      .findFirst()
//                      .orElse(null);
//
//              // Get the standard valueOf(String) method.
//              Method valueOfMethod;
//              try {
//                valueOfMethod = clazz.getMethod("valueOf", String.class);
//              } catch (Throwable e) {
//                throw new IllegalStateException(
//                    "Failed to find the valueOf(String) method on the Enum class (" + clazz + ")",
//                    e);
//              }
//              return new EnumConversionMethods(jsonCreator, valueOfMethod);
//            });
//
//    // Return a converter that uses the cached methods.
//    return new Converter<String, T>() {
//      @Override
//      public T convert(String source) {
//        if (source == null) {
//          return null;
//        }
//        try {
//          if (enumConversionMethods.jsonCreatorMethod() != null) {
//            Object result = enumConversionMethods.jsonCreatorMethod().invoke(null, source);
//            return targetType.cast(result);
//          }
//          // Fallback: use the cached valueOf(String) method.
//          Object result = enumConversionMethods.valueOfMethod().invoke(null, source);
//          return targetType.cast(result);
//        } catch (Throwable e) {
//          String methodUsed =
//              enumConversionMethods.jsonCreatorMethod() != null ? "JsonCreator" :
// "valueOf(String)";
//          throw new IllegalStateException(
//              "Failed to convert the String value ("
//                  + source
//                  + ") to an Enum value of type ("
//                  + targetType
//                  + ") using the method ("
//                  + methodUsed
//                  + ")",
//              e);
//        }
//      }
//    };
//  }
//
//  // Record to hold the methods we need for a given enum class.
//  private record EnumConversionMethods(Method jsonCreatorMethod, Method valueOfMethod) {}
// }
