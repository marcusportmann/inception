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

/**
 * The {@code ValidSouthAfricanMobileNumberValidator} class implements the custom constraint
 * validator that validates whether a <b>String</b> contains a valid South African mobile number.
 *
 * @author Marcus Portmann
 */
public class ValidSouthAfricanMobileNumberValidator
    implements ConstraintValidator<ValidSouthAfricanMobileNumber, Object> {

  /** Creates a new {@code ValidSouthAfricanMobileNumberValidator} instance. */
  public ValidSouthAfricanMobileNumberValidator() {}

  @Override
  public void initialize(ValidSouthAfricanMobileNumber constraintAnnotation) {
    ConstraintValidator.super.initialize(constraintAnnotation);
  }

  @Override
  public boolean isValid(Object object, ConstraintValidatorContext constraintValidatorContext) {
    if (object == null) {
      return true;
    }

    if (object instanceof String mobileNumber) {
      if (formatMobileNumber(mobileNumber) != null) {
        return true;
      }
    }

    addConstraintViolation(constraintValidatorContext, object.toString());
    return false;
  }

  private void addConstraintViolation(ConstraintValidatorContext context, String invalidValue) {
    // Disable the default message
    context.disableDefaultConstraintViolation();

    // Build the custom error message with the encoded value
    String message =
        String.format("invalid South African mobile number (%s)".formatted(invalidValue));

    context.buildConstraintViolationWithTemplate(message).addConstraintViolation();
  }

  /**
   * Formats a mobile number in common South African formats into an MSISDN format. MSISDN format:
   * starts with "27", followed by the 9-digit mobile number.
   *
   * @param mobileNumber the mobile number in a common format
   * @return the formatted MSISDN, or null if the number could not be formatted
   */
  private String formatMobileNumber(String mobileNumber) {
    if (mobileNumber == null || mobileNumber.isEmpty()) {
      return null;
    }

    // Remove all non-numeric characters except the leading '+'
    String sanitizedNumber = mobileNumber.replaceAll("[^\\d+]", "");

    // Check if the number starts with +27 (international format)
    if (sanitizedNumber.startsWith("+27")) {
      sanitizedNumber = sanitizedNumber.substring(1); // Remove the '+'
    }
    // Check if the number starts with 27 (without the plus sign)
    else if (sanitizedNumber.startsWith("27")) {
      // Already in MSISDN format, nothing to change
    }
    // Check if the number starts with '0', convert to MSISDN by replacing '0' with '27'
    else if (sanitizedNumber.startsWith("0")) {
      sanitizedNumber = "27" + sanitizedNumber.substring(1);
    }
    // Otherwise, it's invalid for South Africa
    else {
      return null; // Not a valid South African mobile number
    }

    // Ensure that the sanitized number is exactly 11 digits (27 + 9 digits for the phone number)
    if (sanitizedNumber.length() == 11) {
      return sanitizedNumber;
    }

    // If it doesn't fit the correct length, it's invalid
    return null;
  }
}
