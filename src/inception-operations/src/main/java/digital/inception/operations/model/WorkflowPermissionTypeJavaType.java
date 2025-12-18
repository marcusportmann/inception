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
 * The {@code WorkflowPermissionTypeJavaType} class provides a Hibernate 6 {@code JavaType} that
 * maps {@link WorkflowPermissionType} to a JDBC {@code VARCHAR} column using the enum's
 * <em>code</em> value.
 *
 * <p><strong>Why this is required</strong>
 *
 * <ul>
 *   <li>The {@code type} attributes of the {@code WorkflowDefinitionCategoryPermission} and {@code
 *       WorkflowDefinitionPermission} entities form part of a composite primary keys defined via
 *       {@code @IdClass}. JPA {@code @Convert} attribute converters are not applied to identifier
 *       attributes in an {@code @IdClass}, so a standard {@code AttributeConverter} will be
 *       ignored.
 *   <li>Without a custom Hibernate type, Hibernate falls back to default enum handling. If the enum
 *       is not annotated with {@code @Enumerated(EnumType.STRING)}, Hibernate may expect an ordinal
 *       (integer) and will fail to read database values like {@code "initiate_workflow"}, resulting
 *       in errors such as {@code Data conversion error converting "initiate_workflow" [22018-232]}.
 *   <li>This {@code JavaType} explicitly tells Hibernate how to read/write the enum as its
 *       <em>code</em> (a {@code String}) even when the property is an {@code @Id}, thereby enabling
 *       custom string codes while keeping the composite key.
 * </ul>
 *
 * @author Marcus Portmann
 */
public class WorkflowPermissionTypeJavaType extends AbstractClassJavaType<WorkflowPermissionType> {

  /** Constructs a new {@code WorkflowPermissionTypeJavaType}. */
  public WorkflowPermissionTypeJavaType() {
    super(WorkflowPermissionType.class);
  }

  @Override
  public WorkflowPermissionType fromString(CharSequence string) {
    return string == null
        ? null
        : CodeEnum.fromCode(WorkflowPermissionType.class, string.toString());
  }

  @Override
  public String toString(WorkflowPermissionType value) {
    return value == null ? null : value.code();
  }

  @Override
  public <X> X unwrap(WorkflowPermissionType value, Class<X> type, WrapperOptions options) {
    if (value == null) return null;

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

  @Override
  public WorkflowPermissionType wrap(Object value, WrapperOptions options) {
    if (value == null) return null;

    if (value instanceof WorkflowPermissionType e) {
      return e;
    }
    if (value instanceof String s) {
      return CodeEnum.fromCode(WorkflowPermissionType.class, s);
    }
    if (value instanceof CharSequence cs) {
      return CodeEnum.fromCode(WorkflowPermissionType.class, cs.toString());
    }

    throw unknownWrap(value.getClass());
  }
}
