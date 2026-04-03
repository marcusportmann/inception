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

package digital.inception.operations.model;

import digital.inception.core.model.CodeEnum;
import org.hibernate.type.descriptor.WrapperOptions;
import org.hibernate.type.descriptor.java.AbstractClassJavaType;

/**
 * The {@code InteractionPermissionTypeJavaType} class provides a Hibernate {@code JavaType} that
 * maps {@link InteractionPermissionType} values to a JDBC {@code VARCHAR} column using the enum's
 * code value.
 *
 * <p><strong>Why this is required</strong>
 *
 * <ul>
 *   <li>The {@code type} attribute of the {@link InteractionSourcePermission} entity forms part of
 *       a composite primary key defined using {@code @IdClass}.
 *   <li>JPA attribute converters are not applied to identifier attributes in this scenario, which
 *       means a standard {@code AttributeConverter} cannot be relied on to persist and retrieve the
 *       enum using its custom code value.
 *   <li>This Hibernate-specific {@code JavaType} explicitly controls how the {@link
 *       InteractionPermissionType} enum is converted to and from its JDBC representation.
 *   <li>The enum is stored as a {@code VARCHAR} containing the value returned by {@link
 *       InteractionPermissionType#code()} rather than the enum ordinal or enum constant name.
 * </ul>
 *
 * <p>This allows the use of custom string-based enum codes for identifier fields while remaining
 * compatible with Hibernate 6 and later.
 *
 * @author Marcus Portmann
 */
public class InteractionPermissionTypeJavaType
    extends AbstractClassJavaType<InteractionPermissionType> {

  /** Constructs a new {@code InteractionPermissionTypeJavaType}. */
  public InteractionPermissionTypeJavaType() {
    super(InteractionPermissionType.class);
  }

  /**
   * Convert the specified character sequence to an {@link InteractionPermissionType}.
   *
   * <p>This method is used by Hibernate when converting a string-based database value to the
   * corresponding enum value.
   *
   * @param string the character sequence containing the interaction permission type code
   * @return the corresponding {@link InteractionPermissionType}, or {@code null} if the supplied
   *     value is {@code null}
   */
  @Override
  public InteractionPermissionType fromString(CharSequence string) {
    return string == null
        ? null
        : CodeEnum.fromCode(InteractionPermissionType.class, string.toString());
  }

  /**
   * Convert the specified {@link InteractionPermissionType} to its string representation.
   *
   * <p>This method returns the custom enum code that should be persisted to the database.
   *
   * @param value the interaction permission type
   * @return the string code for the interaction permission type, or {@code null} if the supplied
   *     value is {@code null}
   */
  @Override
  public String toString(InteractionPermissionType value) {
    return value == null ? null : value.code();
  }

  /**
   * Unwrap the specified {@link InteractionPermissionType} to a supported relational form.
   *
   * <p>Hibernate invokes this method when binding the enum value to JDBC. The enum is converted to
   * its custom string code.
   *
   * @param value the interaction permission type value to unwrap
   * @param type the requested target type
   * @param options the wrapper options provided by Hibernate
   * @param <X> the target type
   * @return the unwrapped value
   * @throws org.hibernate.HibernateException if the requested unwrap target type is not supported
   */
  @Override
  public <X> X unwrap(InteractionPermissionType value, Class<X> type, WrapperOptions options) {
    if (value == null) {
      return null;
    }

    String code = value.code();

    if (type.isAssignableFrom(String.class)) {
      return type.cast(code);
    }
    if (type.isAssignableFrom(CharSequence.class)) {
      return type.cast(code);
    }
    if (type == Object.class) {
      return type.cast(code);
    }

    throw unknownUnwrap(type);
  }

  /**
   * Wrap the specified relational value as an {@link InteractionPermissionType}.
   *
   * <p>Hibernate invokes this method when reading a value from JDBC and converting it back to the
   * domain-specific enum.
   *
   * @param value the relational value to wrap
   * @param options the wrapper options provided by Hibernate
   * @return the wrapped {@link InteractionPermissionType}, or {@code null} if the supplied value is
   *     {@code null}
   * @throws org.hibernate.HibernateException if the supplied value type is not supported
   */
  @Override
  public InteractionPermissionType wrap(Object value, WrapperOptions options) {
    if (value == null) {
      return null;
    }

    if (value instanceof InteractionPermissionType enumValue) {
      return enumValue;
    }
    if (value instanceof String stringValue) {
      return CodeEnum.fromCode(InteractionPermissionType.class, stringValue);
    }
    if (value instanceof CharSequence charSequence) {
      return CodeEnum.fromCode(InteractionPermissionType.class, charSequence.toString());
    }

    throw unknownWrap(value.getClass());
  }
}
