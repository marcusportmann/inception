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

package digital.inception.party.constraints;

import digital.inception.party.model.Mandate;
import digital.inception.party.model.MandateProperty;
import digital.inception.party.model.MandatePropertyType;
import digital.inception.party.service.IPartyReferenceService;
import digital.inception.party.service.IPartyService;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.ValidationException;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.hibernate.validator.constraintvalidation.HibernateConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

/**
 * The <b>ValidMandateValidator</b> class implements the custom constraint validator for validating
 * a mandate.
 *
 * @author Marcus Portmann
 */
@SuppressWarnings("unused")
public class ValidMandateValidator implements ConstraintValidator<ValidMandate, Mandate> {

  /** The Party Reference Service. */
  private final IPartyReferenceService partyReferenceService;

  /** The Party Service. */
  private final IPartyService partyService;

  /**
   * Constructs a new <b>ValidMandateValidator</b>.
   *
   * @param partyService the Party Service
   * @param partyReferenceService the Party Reference Service
   */
  @Autowired
  public ValidMandateValidator(
      IPartyService partyService, IPartyReferenceService partyReferenceService) {
    this.partyService = partyService;
    this.partyReferenceService = partyReferenceService;
  }

  /** Constructs a new <b>ValidMandateValidator</b>. */
  public ValidMandateValidator() {
    this.partyService = null;
    this.partyReferenceService = null;
  }

  @Override
  public void initialize(ValidMandate constraintAnnotation) {}

  @Override
  public boolean isValid(Mandate mandate, ConstraintValidatorContext constraintValidatorContext) {
    if ((partyService != null) && (partyReferenceService != null)) {
      boolean isValid = true;

      // Disable the default constraint violation
      constraintValidatorContext.disableDefaultConstraintViolation();

      HibernateConstraintValidatorContext hibernateConstraintValidatorContext =
          constraintValidatorContext.unwrap(HibernateConstraintValidatorContext.class);

      try {
        // Validate mandate type
        if (StringUtils.hasText(mandate.getType())
            && (!partyReferenceService.isValidMandateType(
                mandate.getTenantId(), mandate.getType()))) {
          hibernateConstraintValidatorContext
              .addMessageParameter("mandateType", mandate.getType())
              .buildConstraintViolationWithTemplate(
                  "{digital.inception.party.constraints.ValidMandate.invalidType.message}")
              .addPropertyNode("type")
              .addConstraintViolation();

          isValid = false;
        }

        // Validate mandate properties
        for (MandateProperty mandateProperty : mandate.getProperties()) {
          Optional<MandatePropertyType> mandatePropertyTypeOptional =
              partyReferenceService.getMandatePropertyType(
                  mandate.getTenantId(), mandate.getType(), mandateProperty.getType());

          if (mandatePropertyTypeOptional.isPresent()) {
            MandatePropertyType mandatePropertyType = mandatePropertyTypeOptional.get();

            switch (mandatePropertyTypeOptional.get().getValueType()) {
              case BOOLEAN:
                if (mandateProperty.getBooleanValue() == null) {
                  hibernateConstraintValidatorContext
                      .addMessageParameter("mandatePropertyType", mandateProperty.getType())
                      .addMessageParameter("mandateType", mandate.getType())
                      .buildConstraintViolationWithTemplate(
                          "{digital.inception.party.constraints.ValidMandate.invalidMandatePropertyWithNullBooleanValue.message}")
                      .addPropertyNode("properties")
                      .addConstraintViolation();

                  isValid = false;
                }

                break;
              case DATE:
                if (mandateProperty.getDateValue() == null) {
                  hibernateConstraintValidatorContext
                      .addMessageParameter("mandatePropertyType", mandateProperty.getType())
                      .addMessageParameter("mandateType", mandate.getType())
                      .buildConstraintViolationWithTemplate(
                          "{digital.inception.party.constraints.ValidMandate.invalidMandatePropertyWithNullDateValue.message}")
                      .addPropertyNode("properties")
                      .addConstraintViolation();

                  isValid = false;
                }

                break;
              case DECIMAL:
                if (mandateProperty.getDecimalValue() == null) {
                  hibernateConstraintValidatorContext
                      .addMessageParameter("mandatePropertyType", mandateProperty.getType())
                      .addMessageParameter("mandateType", mandate.getType())
                      .buildConstraintViolationWithTemplate(
                          "{digital.inception.party.constraints.ValidMandate.invalidMandatePropertyWithNullDecimalValue.message}")
                      .addPropertyNode("properties")
                      .addConstraintViolation();

                  isValid = false;
                }

                break;
              case DOUBLE:
                if (mandateProperty.getDoubleValue() == null) {
                  hibernateConstraintValidatorContext
                      .addMessageParameter("mandatePropertyType", mandateProperty.getType())
                      .addMessageParameter("mandateType", mandate.getType())
                      .buildConstraintViolationWithTemplate(
                          "{digital.inception.party.constraints.ValidMandate.invalidMandatePropertyWithNullDoubleValue.message}")
                      .addPropertyNode("properties")
                      .addConstraintViolation();

                  isValid = false;
                }

                break;
              case INTEGER:
                if (mandateProperty.getIntegerValue() == null) {
                  hibernateConstraintValidatorContext
                      .addMessageParameter("mandatePropertyType", mandateProperty.getType())
                      .addMessageParameter("mandateType", mandate.getType())
                      .buildConstraintViolationWithTemplate(
                          "{digital.inception.party.constraints.ValidMandate.invalidMandatePropertyWithNullIntegerValue.message}")
                      .addPropertyNode("properties")
                      .addConstraintViolation();

                  isValid = false;
                }

                break;
              case STRING:
                if (mandateProperty.getStringValue() == null) {
                  hibernateConstraintValidatorContext
                      .addMessageParameter("mandatePropertyType", mandateProperty.getType())
                      .addMessageParameter("mandateType", mandate.getType())
                      .buildConstraintViolationWithTemplate(
                          "{digital.inception.party.constraints.ValidMandate.invalidMandatePropertyWithNullStringValue.message}")
                      .addPropertyNode("properties")
                      .addConstraintViolation();

                  isValid = false;
                } else {
                  if (StringUtils.hasText(mandatePropertyType.getPattern())) {
                    Pattern pattern = mandatePropertyType.getCompiledPattern();

                    Matcher matcher = pattern.matcher(mandateProperty.getStringValue());

                    if (!matcher.matches()) {
                      hibernateConstraintValidatorContext
                          .addMessageParameter("mandatePropertyType", mandateProperty.getType())
                          .addMessageParameter("stringValue", mandateProperty.getStringValue())
                          .addMessageParameter("mandateType", mandate.getType())
                          .buildConstraintViolationWithTemplate(
                              "{digital.inception.party.constraints.ValidMandate.invalidMandatePropertyWithStringValue.message}")
                          .addPropertyNode("properties")
                          .addConstraintViolation();

                      isValid = false;
                    }
                  }
                }

                break;
            }
          } else {
            hibernateConstraintValidatorContext
                .addMessageParameter("mandatePropertyType", mandateProperty.getType())
                .addMessageParameter("mandateType", mandate.getType())
                .buildConstraintViolationWithTemplate(
                    "{digital.inception.party.constraints.ValidMandate.invalidMandatePropertyType.message}")
                .addPropertyNode("properties")
                .addConstraintViolation();

            isValid = false;
          }
        }
      } catch (Throwable e) {
        throw new ValidationException("Failed to validate the mandate", e);
      }

      return isValid;
    } else {
      return true;
    }
  }
}
