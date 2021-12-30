/*
 * Copyright 2021 Marcus Portmann
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

import digital.inception.party.Association;
import digital.inception.party.AssociationProperty;
import digital.inception.party.AssociationPropertyType;
import digital.inception.party.IPartyReferenceService;
import digital.inception.party.IPartyService;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.ValidationException;
import org.hibernate.validator.constraintvalidation.HibernateConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

/**
 * The <b>ValidAssociationValidator</b> class implements the custom constraint validator for
 * validating an association.
 *
 * @author Marcus Portmann
 */
public class ValidAssociationValidator
    implements ConstraintValidator<ValidAssociation, Association> {

  /** The Party Reference Service. */
  private final IPartyReferenceService partyReferenceService;

  /** The Party Service. */
  private final IPartyService partyService;

  /**
   * Constructs a new <b>ValidAssociationValidator</b>.
   *
   * @param partyService the Party Service
   * @param partyReferenceService the Party Reference Service
   */
  @Autowired
  public ValidAssociationValidator(
      IPartyService partyService, IPartyReferenceService partyReferenceService) {
    this.partyService = partyService;
    this.partyReferenceService = partyReferenceService;
  }

  /** Constructs a new <b>ValidAssociationValidator</b>. */
  public ValidAssociationValidator() {
    this.partyService = null;
    this.partyReferenceService = null;
  }

  @Override
  public void initialize(ValidAssociation constraintAnnotation) {}

  @Override
  public boolean isValid(
      Association association, ConstraintValidatorContext constraintValidatorContext) {
    if ((partyService != null) && (partyReferenceService != null)) {
      boolean isValid = true;

      // Disable the default constraint violation
      constraintValidatorContext.disableDefaultConstraintViolation();

      HibernateConstraintValidatorContext hibernateConstraintValidatorContext =
          constraintValidatorContext.unwrap(HibernateConstraintValidatorContext.class);

      try {
        // Validate association type
        if (StringUtils.hasText(association.getType())
            && (!partyReferenceService.isValidAssociationType(
                association.getTenantId(), association.getType()))) {
          hibernateConstraintValidatorContext
              .addMessageParameter("associationType", association.getType())
              .buildConstraintViolationWithTemplate(
                  "{digital.inception.party.constraints.ValidAssociation.invalidType.message}")
              .addPropertyNode("type")
              .addConstraintViolation();

          isValid = false;
        }

        // Validate association properties
        for (AssociationProperty associationProperty : association.getProperties()) {
          Optional<AssociationPropertyType> associationPropertyTypeOptional =
              partyReferenceService.getAssociationPropertyType(
                  association.getTenantId(), association.getType(), associationProperty.getType());

          if (associationPropertyTypeOptional.isPresent()) {
            AssociationPropertyType associationPropertyType = associationPropertyTypeOptional.get();

            switch (associationPropertyType.getValueType()) {
              case BOOLEAN:
                if (associationProperty.getBooleanValue() == null) {
                  hibernateConstraintValidatorContext
                      .addMessageParameter("associationPropertyType", associationProperty.getType())
                      .addMessageParameter("associationType", association.getType())
                      .buildConstraintViolationWithTemplate(
                          "{digital.inception.party.constraints.ValidAssociation.invalidAssociationPropertyWithNullBooleanValue.message}")
                      .addPropertyNode("properties")
                      .addPropertyNode("booleanValue")
                      .addConstraintViolation();

                  isValid = false;
                }

                break;
              case DATE:
                if (associationProperty.getDateValue() == null) {
                  hibernateConstraintValidatorContext
                      .addMessageParameter("associationPropertyType", associationProperty.getType())
                      .addMessageParameter("associationType", association.getType())
                      .buildConstraintViolationWithTemplate(
                          "{digital.inception.party.constraints.ValidAssociation.invalidAssociationPropertyWithNullDateValue.message}")
                      .addPropertyNode("properties")
                      .addPropertyNode("dateValue")
                      .addConstraintViolation();

                  isValid = false;
                }

                break;
              case DECIMAL:
                if (associationProperty.getDecimalValue() == null) {
                  hibernateConstraintValidatorContext
                      .addMessageParameter("associationPropertyType", associationProperty.getType())
                      .addMessageParameter("associationType", association.getType())
                      .buildConstraintViolationWithTemplate(
                          "{digital.inception.party.constraints.ValidAssociation.invalidAssociationPropertyWithNullDecimalValue.message}")
                      .addPropertyNode("properties")
                      .addPropertyNode("decimalValue")
                      .addConstraintViolation();

                  isValid = false;
                }

                break;
              case DOUBLE:
                if (associationProperty.getDoubleValue() == null) {
                  hibernateConstraintValidatorContext
                      .addMessageParameter("associationPropertyType", associationProperty.getType())
                      .addMessageParameter("associationType", association.getType())
                      .buildConstraintViolationWithTemplate(
                          "{digital.inception.party.constraints.ValidAssociation.invalidAssociationPropertyWithNullDoubleValue.message}")
                      .addPropertyNode("properties")
                      .addPropertyNode("doubleValue")
                      .addConstraintViolation();

                  isValid = false;
                }

                break;
              case INTEGER:
                if (associationProperty.getIntegerValue() == null) {
                  hibernateConstraintValidatorContext
                      .addMessageParameter("associationPropertyType", associationProperty.getType())
                      .addMessageParameter("associationType", association.getType())
                      .buildConstraintViolationWithTemplate(
                          "{digital.inception.party.constraints.ValidAssociation.invalidAssociationPropertyWithNullIntegerValue.message}")
                      .addPropertyNode("properties")
                      .addPropertyNode("integerValue")
                      .addConstraintViolation();

                  isValid = false;
                }

                break;
              case STRING:
                if (associationProperty.getStringValue() == null) {
                  hibernateConstraintValidatorContext
                      .addMessageParameter("associationPropertyType", associationProperty.getType())
                      .addMessageParameter("associationType", association.getType())
                      .buildConstraintViolationWithTemplate(
                          "{digital.inception.party.constraints.ValidAssociation.invalidAssociationPropertyWithNullStringValue.message}")
                      .addPropertyNode("properties")
                      .addPropertyNode("stringValue")
                      .addConstraintViolation();

                  isValid = false;
                } else {
                  if (StringUtils.hasText(associationPropertyType.getPattern())) {
                    Pattern pattern = associationPropertyType.getCompiledPattern();

                    Matcher matcher = pattern.matcher(associationProperty.getStringValue());

                    if (!matcher.matches()) {
                      hibernateConstraintValidatorContext
                          .addMessageParameter(
                              "associationPropertyType", associationProperty.getType())
                          .addMessageParameter("stringValue", associationProperty.getStringValue())
                          .addMessageParameter("associationType", association.getType())
                          .buildConstraintViolationWithTemplate(
                              "{digital.inception.party.constraints.ValidAssociation.invalidAssociationPropertyWithStringValue.message}")
                          .addPropertyNode("properties")
                          .addPropertyNode("stringValue")
                          .addConstraintViolation();

                      isValid = false;
                    }
                  }
                }

                break;
            }
          } else {
            hibernateConstraintValidatorContext
                .addMessageParameter("associationPropertyType", associationProperty.getType())
                .addMessageParameter("associationType", association.getType())
                .buildConstraintViolationWithTemplate(
                    "{digital.inception.party.constraints.ValidAssociation.invalidAssociationPropertyType.message}")
                .addPropertyNode("properties")
                .addConstraintViolation();

            isValid = false;
          }
        }
      } catch (Throwable e) {
        throw new ValidationException("Failed to validate the association", e);
      }

      return isValid;
    } else {
      return true;
    }
  }
}
