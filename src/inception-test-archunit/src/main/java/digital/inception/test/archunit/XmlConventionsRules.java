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

package digital.inception.test.archunit;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.fields;

import com.tngtech.archunit.base.ArchUnitException;
import com.tngtech.archunit.core.domain.JavaAnnotation;
import com.tngtech.archunit.core.domain.JavaClass;
import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.domain.JavaField;
import com.tngtech.archunit.core.domain.JavaModifier;
import com.tngtech.archunit.lang.ArchCondition;
import com.tngtech.archunit.lang.ArchRule;
import com.tngtech.archunit.lang.ConditionEvents;
import com.tngtech.archunit.lang.SimpleConditionEvent;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Optional;

/**
 * The {@code XmlConventionsRules} class holds the ArchUnit rules that verify JAXB conventions for
 * XML model classes and enums.
 *
 * <p>Class Rules (non-enums): apply to classes annotated with {@code @XmlType} but NOT
 * {@code @XmlEnum}.
 *
 * <ul>
 *   <li>Must also be annotated with {@code @XmlRootElement} and {@code @XmlAccessorType}.
 *   <li>{@code name} of {@code @XmlType} and {@code @XmlRootElement} must match class simple name
 *       (or {@code ##default}).
 *   <li>Fields (unless {@code @XmlIgnore}):
 *       <ul>
 *         <li>Single value → {@code @XmlElement(name = CapitalisedFieldName)} (or {@code
 *             ##default}).
 *         <li>Collection/array → both {@code @XmlElement} and {@code @XmlElementWrapper(name =
 *             CapitalisedFieldName)} (or {@code ##default}).
 *       </ul>
 * </ul>
 *
 * <p>Enum Rules: apply to classes annotated with {@code @XmlEnum}.
 *
 * <ul>
 *   <li>Must also have {@code @XmlType(name = <SimpleClassName>)}.
 *   <li>Every enum constant must have {@code @XmlEnumValue("<PascalCaseOfConstant>")}, e.g. {@code
 *       STEP_COMPLETED} → {@code "StepCompleted"}.
 * </ul>
 *
 * @author Marcus Portmann
 */
public final class XmlConventionsRules {

  private static final String JAXB_DEFAULT = "##default";

  private static final String TRANSIENT = "jakarta.persistence.Transient";

  private static final String XML_ACCESSOR_TYPE = "jakarta.xml.bind.annotation.XmlAccessorType";

  private static final String XML_ELEMENT = "jakarta.xml.bind.annotation.XmlElement";

  private static final String XML_ELEMENT_WRAPPER = "jakarta.xml.bind.annotation.XmlElementWrapper";

  private static final String XML_ENUM = "jakarta.xml.bind.annotation.XmlEnum";

  private static final String XML_ENUM_VALUE = "jakarta.xml.bind.annotation.XmlEnumValue";

  private static final String XML_IGNORE = "jakarta.xml.bind.annotation.XmlIgnore";

  private static final String XML_ROOT_ELEMENT = "jakarta.xml.bind.annotation.XmlRootElement";

  private static final String XML_TRANSIENT = "jakarta.xml.bind.annotation.XmlTransient";

  private static final String XML_TYPE = "jakarta.xml.bind.annotation.XmlType";

  /**
   * Classes with @XmlType (but NOT @XmlEnum) must also have @XmlRootElement and @XmlAccessorType.
   * Names of @XmlType and @XmlRootElement must match the class simple name (or "##default").
   */
  public static final ArchRule XML_CLASSES_HAVE_REQUIRED_ANNOTATIONS_AND_NAMES =
      classes()
          .that()
          .areAnnotatedWith(XML_TYPE)
          .and()
          .areNotAnnotatedWith(XML_ENUM) // ⬅️ skip enums
          .should()
          .beAnnotatedWith(XML_ROOT_ELEMENT)
          .andShould()
          .beAnnotatedWith(XML_ACCESSOR_TYPE)
          .andShould(
              new ArchCondition<JavaClass>(
                  "have @XmlType.name and @XmlRootElement.name matching class name (or ##default)") {
                @Override
                public void check(JavaClass clazz, ConditionEvents events) {
                  String simpleName = clazz.getSimpleName();

                  if (!annotationNameMatchesClass(clazz, XML_TYPE, "name", simpleName)) {
                    events.add(
                        SimpleConditionEvent.violated(
                            clazz,
                            String.format(
                                "%s: @XmlType.name must match \"%s\" or be \"%s\"",
                                clazz.getName(), simpleName, JAXB_DEFAULT)));
                  }

                  if (!annotationNameMatchesClass(clazz, XML_ROOT_ELEMENT, "name", simpleName)) {
                    events.add(
                        SimpleConditionEvent.violated(
                            clazz,
                            String.format(
                                "%s: @XmlRootElement.name must match \"%s\" or be \"%s\"",
                                clazz.getName(), simpleName, JAXB_DEFAULT)));
                  }
                }
              })
          .allowEmptyShould(true);

  /**
   * Fields inside @XmlType (non-enum) classes must carry the correct JAXB field-level annotations.
   */
  public static final ArchRule XML_FIELDS_HAVE_CORRECT_ANNOTATIONS =
      fields()
          .that()
          .areDeclaredInClassesThat()
          .areAnnotatedWith(XML_TYPE)
          .and()
          .areDeclaredInClassesThat()
          .areNotAnnotatedWith(XML_ENUM)
          .and()
          .areNotStatic()
          .should(
              new ArchCondition<JavaField>(
                  "be annotated with JAXB correctly (@XmlElement / @XmlElementWrapper) unless @XmlIgnore/@XmlTransient/@Transient or class uses XmlAccessType.PROPERTY") {
                @Override
                public void check(JavaField field, ConditionEvents events) {
                  // Skip fields if the declaring class uses property access
                  JavaClass owner = field.getOwner();
                  if (owner.isAnnotatedWith(XML_ACCESSOR_TYPE)) {
                    JavaAnnotation<?> acc = owner.getAnnotationOfType(XML_ACCESSOR_TYPE);
                    Optional<Object> value = acc.get("value");
                    if (value.isPresent()
                        && value.get()
                            instanceof com.tngtech.archunit.core.domain.JavaEnumConstant c) {
                      if ("PROPERTY".equals(c.name())) {
                        return; // class uses @XmlAccessorType(XmlAccessType.PROPERTY) -> do not
                        // validate fields
                      }
                    }
                  }

                  if (field.isAnnotatedWith(XML_IGNORE) || field.isAnnotatedWith(XML_TRANSIENT) || field.isAnnotatedWith(TRANSIENT)) {
                    return;
                  }

                  // Skip Java keyword 'transient' fields
                  if (field.getModifiers().contains(JavaModifier.TRANSIENT)) {
                    return;
                  }

                  JavaClass rawType = field.getRawType();
                  boolean isArray = rawType.isArray();
                  boolean isCollection =
                      rawType.isAssignableTo(java.util.Collection.class) || isArray;

                  String expectedCapitalised = capitalise(field.getName());

                  if (isCollection) {
                    // Always require @XmlElement for collections/arrays
                    if (!field.isAnnotatedWith(XML_ELEMENT)) {
                      events.add(
                          SimpleConditionEvent.violated(
                              field,
                              message(
                                  field, "must be annotated with @XmlElement for collections")));
                    }

                    // Skip @XmlElementWrapper when this is an array of "simple" JAXB types
                    boolean isArrayOfSimple =
                        isArray && isJaxbSimpleType(rawType.getComponentType());

                    if (!isArrayOfSimple) {
                      if (!field.isAnnotatedWith(XML_ELEMENT_WRAPPER)) {
                        events.add(
                            SimpleConditionEvent.violated(
                                field,
                                message(
                                    field,
                                    "must be annotated with @XmlElementWrapper for collections")));
                      } else if (!annotationNameMatches(
                          field, XML_ELEMENT_WRAPPER, "name", expectedCapitalised)) {
                        events.add(
                            SimpleConditionEvent.violated(
                                field,
                                message(
                                    field,
                                    String.format(
                                        "@XmlElementWrapper.name must be \"%s\" or \"%s\"",
                                        expectedCapitalised, JAXB_DEFAULT))));
                      }
                    }
                  } else {
                    // Single-valued fields
                    if (!field.isAnnotatedWith(XML_ELEMENT)) {
                      events.add(
                          SimpleConditionEvent.violated(
                              field,
                              message(
                                  field,
                                  "must be annotated with @XmlElement for single-valued fields")));
                    } else if (!annotationNameMatches(
                        field, XML_ELEMENT, "name", expectedCapitalised)) {
                      events.add(
                          SimpleConditionEvent.violated(
                              field,
                              message(
                                  field,
                                  String.format(
                                      "@XmlElement.name must be \"%s\" or \"%s\"",
                                      expectedCapitalised, JAXB_DEFAULT))));
                    }
                  }
                }
              })
          .allowEmptyShould(true);

  /**
   * Enums annotated with @XmlEnum must: - also have @XmlType(name = {@code SimpleClassName}) -
   * have @XmlEnumValue on EVERY enum constant with value case-insensitively equal to the PascalCase
   * of the constant name (e.g., STEP_COMPLETED -> StepCompleted), i.e., comparison is done with
   * equalsIgnoreCase.
   */
  public static final ArchRule XML_ENUMS_HAVE_XMLTYPE_AND_ENUM_VALUES =
      classes()
          .that()
          .areAnnotatedWith(XML_ENUM)
          .should()
          .beAnnotatedWith(XML_TYPE)
          .andShould(
              new ArchCondition<JavaClass>(
                  "have @XmlType.name == simple class name and case-insensitive @XmlEnumValue on every enum constant") {
                @Override
                public void check(JavaClass enumClass, ConditionEvents events) {
                  if (!enumClass.isEnum()) {
                    events.add(
                        SimpleConditionEvent.violated(
                            enumClass, enumClass.getName() + " is @XmlEnum but not an enum"));
                    return;
                  }

                  // 1) Class-level: @XmlType(name = <SimpleClassName>)
                  if (!enumClass.isAnnotatedWith(XML_TYPE)) {
                    events.add(
                        SimpleConditionEvent.violated(
                            enumClass, enumClass.getName() + " must be annotated with @XmlType"));
                  } else {
                    JavaAnnotation<?> ann = enumClass.getAnnotationOfType(XML_TYPE);
                    String expected = enumClass.getSimpleName();
                    Object nameAttr = ann.get("name").orElse(null);
                    if (!(nameAttr instanceof String s) || !expected.equals(s)) {
                      events.add(
                          SimpleConditionEvent.violated(
                              enumClass,
                              String.format(
                                  "%s: @XmlType.name must equal \"%s\"",
                                  enumClass.getName(), expected)));
                    }
                  }

                  // 2) Constant-level: use reflection to read @XmlEnumValue (case-insensitive
                  // match)
                  final Class<?> reflected;
                  try {
                    reflected = enumClass.reflect();
                  } catch (ArchUnitException.ReflectionException ex) {
                    events.add(
                        SimpleConditionEvent.violated(
                            enumClass,
                            "Unable to reflect enum "
                                + enumClass.getName()
                                + " to verify @XmlEnumValue: "
                                + ex.getMessage()));
                    return;
                  }

                  try {
                    Method valueMethod =
                        jakarta.xml.bind.annotation.XmlEnumValue.class.getMethod("value");

                    for (Field f : reflected.getDeclaredFields()) {
                      if (!f.isEnumConstant()) continue;

                      String constantName = f.getName(); // e.g., STEP_COMPLETED
                      String expected = pascalCaseFromEnumName(constantName); // StepCompleted

                      Annotation ann =
                          f.getAnnotation(jakarta.xml.bind.annotation.XmlEnumValue.class);
                      if (ann == null) {
                        events.add(
                            SimpleConditionEvent.violated(
                                enumClass,
                                String.format(
                                    "%s.%s must be annotated with @XmlEnumValue(\"%s\") (case-insensitive accepted)",
                                    enumClass.getName(), constantName, expected)));
                        continue;
                      }

                      Object actual = valueMethod.invoke(ann);
                      String actualStr = (actual instanceof String) ? (String) actual : null;

                      if (actualStr == null || !actualStr.equalsIgnoreCase(expected)) {
                        events.add(
                            SimpleConditionEvent.violated(
                                enumClass,
                                String.format(
                                    "%s.%s: @XmlEnumValue(\"%s\") must equal \"%s\" (case-insensitive)",
                                    enumClass.getName(), constantName, actualStr, expected)));
                      }
                    }
                  } catch (Exception reflectError) {
                    events.add(
                        SimpleConditionEvent.violated(
                            enumClass,
                            "Failed to inspect enum constants for @XmlEnumValue on "
                                + enumClass.getName()
                                + ": "
                                + reflectError.getMessage()));
                  }
                }
              })
          .allowEmptyShould(true);

  /** Constructs a new {@code XmlConventionsRules}. */
  public XmlConventionsRules() {}

  /**
   * Executes all JAXB XML convention checks against the supplied classes.
   *
   * @param classes the Java classes to check
   */
  public static void check(JavaClasses classes) {
    // If Jakarta JAXB isn't on the classpath for this module, don't run the rules.
    try {
      Class.forName("jakarta.xml.bind.annotation.XmlType");
    } catch (ClassNotFoundException e) {
      return;
    }

    XML_CLASSES_HAVE_REQUIRED_ANNOTATIONS_AND_NAMES.check(classes);
    XML_FIELDS_HAVE_CORRECT_ANNOTATIONS.check(classes);
    XML_ENUMS_HAVE_XMLTYPE_AND_ENUM_VALUES.check(classes);
  }

  private static boolean annotationNameMatches(
      JavaField field, String annotationType, String attribute, String expectedName) {
    if (!field.isAnnotatedWith(annotationType)) {
      return false;
    }
    JavaAnnotation<?> ann = field.getAnnotationOfType(annotationType);
    Optional<Object> value = ann.get(attribute);
    if (value.isEmpty()) {
      return true; // default acceptable
    }
    Object v = value.get();
    if (!(v instanceof String s)) {
      return false;
    }
    return JAXB_DEFAULT.equals(s) || expectedName.equals(s);
  }

  // ------------------------ Helper methods ------------------------

  private static boolean annotationNameMatchesClass(
      JavaClass clazz, String annotationType, String attribute, String expectedSimpleName) {
    if (!clazz.isAnnotatedWith(annotationType)) {
      return false;
    }
    JavaAnnotation<?> ann = clazz.getAnnotationOfType(annotationType);
    Optional<Object> value = ann.get(attribute);
    if (value.isEmpty()) {
      return true; // treat missing as JAXB default (acceptable for non-enum classes)
    }
    Object v = value.get();
    if (!(v instanceof String s)) {
      return false;
    }
    return JAXB_DEFAULT.equals(s) || expectedSimpleName.equals(s);
  }

  private static String capitalise(String s) {
    if (s == null || s.isEmpty()) return s;
    if (s.length() == 1) return s.toUpperCase();
    return Character.toUpperCase(s.charAt(0)) + s.substring(1);
  }

  /** Treat common JAXB “simple” types as scalars for the array exemption. */
  private static boolean isJaxbSimpleType(JavaClass type) {
    if (type.isPrimitive() || type.isEnum()) {
      return true;
    }

    String fqn = type.getName();
    return
    // Boxed primitives
    fqn.equals("java.lang.Boolean")
        || fqn.equals("java.lang.Byte")
        || fqn.equals("java.lang.Short")
        || fqn.equals("java.lang.Integer")
        || fqn.equals("java.lang.Long")
        || fqn.equals("java.lang.Float")
        || fqn.equals("java.lang.Double")
        || fqn.equals("java.lang.Character")
        ||
        // Common JAXB-mapped scalars
        fqn.equals("java.lang.String")
        || fqn.equals("java.math.BigDecimal")
        || fqn.equals("java.math.BigInteger")
        || fqn.equals("java.util.UUID");
  }

  private static String message(JavaField field, String tail) {
    return String.format("Field %s.%s %s", field.getOwner().getName(), field.getName(), tail);
  }

  private static String pascalCaseFromEnumName(String enumConstantName) {
    // STEP_COMPLETED -> StepCompleted
    String[] parts = enumConstantName.toLowerCase().split("_");
    StringBuilder b = new StringBuilder();
    for (String p : parts) {
      if (!p.isEmpty()) {
        b.append(Character.toUpperCase(p.charAt(0))).append(p.substring(1));
      }
    }
    return b.toString();
  }
}
