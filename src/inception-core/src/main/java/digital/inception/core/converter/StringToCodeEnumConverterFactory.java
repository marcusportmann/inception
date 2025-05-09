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

package digital.inception.core.converter;

import digital.inception.core.model.CodeEnum;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.convert.converter.ConverterFactory;
import org.springframework.stereotype.Component;

/**
 * The {@code StringToCodeEnumConverterFactory} class provides a Spring converter factory that
 * creates converters for Enum types that implement the CodeEnum interface.
 *
 * @author Marcus Portmann
 */
@Component
public final class StringToCodeEnumConverterFactory implements ConverterFactory<String, CodeEnum> {

  /** Constructs a new {@code StringToCodeEnumConverterFactory}. */
  public StringToCodeEnumConverterFactory() {}

  @Override
  @SuppressWarnings("unchecked")
  public <T extends CodeEnum> Converter<String, T> getConverter(Class<T> targetType) {
    // Optionally validate it's an actual enum:
    if (!targetType.isEnum()) {
      throw new IllegalArgumentException("Target type must be an Enum that implements CodeEnum");
    }
    /*
     * Force-cast to Class<Enum<T> & CodeEnum> (the nested class wants that intersection).
     * The compiler won't let you do it "cleanly" without a cast, because the interface
     * only enforces T extends CodeEnum, not Enum<T>.
     */
    Class<? extends Enum<?>> enumClass = (Class<? extends Enum<?>>) targetType;

    // Then cast to the type the nested class expects:
    return (Converter<String, T>) new StringToCodeEnumConverter(enumClass);
  }

  private static class StringToCodeEnumConverter<E extends Enum<E> & CodeEnum>
      implements Converter<String, E> {

    private final Class<E> enumType;

    /**
     * Constructs a new {@code StringToCodeEnumConverter}.
     *
     * @param enumType the enum type
     */
    public StringToCodeEnumConverter(Class<E> enumType) {
      this.enumType = enumType;
    }

    @Override
    public E convert(String source) {
      if (source == null || source.isEmpty()) {
        return null;
      }
      // Now we can safely call getEnumConstants() because E is known to be an Enum
      return CodeEnum.fromCode(enumType, source);
    }
  }
}
