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

package digital.inception.core.validation.constraint;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.lang.reflect.Field;
import java.util.Base64;
import java.util.Optional;
import java.util.regex.Pattern;

/**
 * The <b>NoScriptOrSQLInjectionValidator</b> class implements the custom constraint validator for
 * applying the string validation, which detects whether a <b>String</b> or the <b>String</b>
 * properties of an object contain unsafe script or SQL injection content.
 *
 * @author Marcus Portmann
 */
public class NoScriptOrSQLInjectionValidator
    implements ConstraintValidator<NoScriptOrSQLInjection, Object> {

  private static final Pattern SCRIPT_PATTERN =
      Pattern.compile(".*<script>(.*?)</script>.*", Pattern.CASE_INSENSITIVE);
  private static final Pattern SQL_INJECTION_PATTERN =
      Pattern.compile(
          ".*('.+--)|(--)|(\\|)|(%7C)|('.*--)|(--)|(;)|(\\*)|(\\b(select|insert|delete|update|union|drop|create|alter|exec|execute|grant|revoke)\\b).*",
          Pattern.CASE_INSENSITIVE);

  /** Constructs a new <b>NoScriptOrSQLInjectionValidator</b>. */
  public NoScriptOrSQLInjectionValidator() {}

  @Override
  public void initialize(NoScriptOrSQLInjection constraintAnnotation) {
    ConstraintValidator.super.initialize(constraintAnnotation);
  }

  @Override
  public boolean isValid(Object object, ConstraintValidatorContext constraintValidatorContext) {
    if (object != null) {
      if (object instanceof String string) {
        if (isInvalidString(string)) {
          addConstraintViolation(constraintValidatorContext, null, string);
          return false;
        }
      } else {
        Field[] fields = object.getClass().getDeclaredFields();

        boolean isValid = true;

        for (Field field : fields) {
          if (field.getType() == String.class) {
            field.setAccessible(true);

            try {
              Optional<String> value = Optional.ofNullable((String) field.get(object));

              if (value.isPresent() && isInvalidString(value.get())) {
                addConstraintViolation(constraintValidatorContext, field.getName(), value.get());
                isValid = false;
              }
            } catch (Throwable e) {
              throw new RuntimeException(
                  "Failed to check the field ("
                      + field.getName()
                      + ") for the object of type ("
                      + object.getClass().getName()
                      + ") for script or SQL injection",
                  e);
            }
          }
        }

        return isValid;
      }
    }

    return true;
  }

  private void addConstraintViolation(
      ConstraintValidatorContext context, String fieldName, String invalidValue) {
    // Encode the invalid value using Base64
    String encodedValue = Base64.getEncoder().encodeToString(invalidValue.getBytes());

    // Disable the default message
    context.disableDefaultConstraintViolation();

    // Build the custom error message with the encoded value
    String message =
        String.format(
            "invalid string value containing script or SQL injection (%s)".formatted(encodedValue));

    if ((fieldName != null) && (!fieldName.isEmpty())) {
      context
          .buildConstraintViolationWithTemplate(message)
          .addPropertyNode(fieldName)
          .addConstraintViolation();
    } else {
      context.buildConstraintViolationWithTemplate(message).addConstraintViolation();
    }
  }

  private boolean isInvalidString(String value) {
    // Check for <script> tags
    if (SCRIPT_PATTERN.matcher(value).matches()) {
      return true;
    }

    // Check for SQL injection patterns
    return SQL_INJECTION_PATTERN.matcher(value).matches();
  }
}
