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

package digital.inception.party.constraint;

import digital.inception.party.model.Association;
import digital.inception.party.model.AssociationProperty;
import digital.inception.party.model.AssociationPropertyType;
import digital.inception.party.model.AssociationType;
import digital.inception.party.model.PartyType;
import digital.inception.party.service.PartyReferenceService;
import digital.inception.party.service.PartyService;
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
 * The {@code ValidAssociationValidator} class implements the custom constraint validator for
 * validating an association.
 *
 * @author Marcus Portmann
 */
@SuppressWarnings("unused")
public class ValidAssociationValidator
    implements ConstraintValidator<ValidAssociation, Association> {

  /** The Party Reference Service. */
  private final PartyReferenceService partyReferenceService;

  /** The Party Service. */
  private final PartyService partyService;

  /**
   * Constructs a new {@code ValidAssociationValidator}.
   *
   * @param partyService the Party Service
   * @param partyReferenceService the Party Reference Service
   */
  @Autowired
  public ValidAssociationValidator(
      PartyService partyService, PartyReferenceService partyReferenceService) {
    this.partyService = partyService;
    this.partyReferenceService = partyReferenceService;
  }

  /** Constructs a new {@code ValidAssociationValidator}. */
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
        if (StringUtils.hasText(association.getType())) {
          Optional<AssociationType> associationTypeOptional =
              partyReferenceService.getAssociationType(
                  association.getTenantId(), association.getType());

          if (associationTypeOptional.isPresent()) {
            AssociationType associationType = associationTypeOptional.get();

            // Validate association properties
            for (AssociationProperty associationProperty : association.getProperties()) {
              Optional<AssociationPropertyType> associationPropertyTypeOptional =
                  partyReferenceService.getAssociationPropertyType(
                      association.getTenantId(),
                      association.getType(),
                      associationProperty.getType());

              if (associationPropertyTypeOptional.isPresent()) {
                AssociationPropertyType associationPropertyType =
                    associationPropertyTypeOptional.get();

                switch (associationPropertyType.getValueType()) {
                  case BOOLEAN:
                    if (associationProperty.getBooleanValue() == null) {
                      hibernateConstraintValidatorContext
                          .addMessageParameter(
                              "associationPropertyType", associationProperty.getType())
                          .addMessageParameter("associationType", association.getType())
                          .buildConstraintViolationWithTemplate(
                              "{digital.inception.party.constraint.ValidAssociation.invalidAssociationPropertyWithNullBooleanValue.message}")
                          .addPropertyNode("properties")
                          .addPropertyNode("booleanValue")
                          .addConstraintViolation();

                      isValid = false;
                    }

                    break;
                  case DATE:
                    if (associationProperty.getDateValue() == null) {
                      hibernateConstraintValidatorContext
                          .addMessageParameter(
                              "associationPropertyType", associationProperty.getType())
                          .addMessageParameter("associationType", association.getType())
                          .buildConstraintViolationWithTemplate(
                              "{digital.inception.party.constraint.ValidAssociation.invalidAssociationPropertyWithNullDateValue.message}")
                          .addPropertyNode("properties")
                          .addPropertyNode("dateValue")
                          .addConstraintViolation();

                      isValid = false;
                    }

                    break;
                  case DECIMAL:
                    if (associationProperty.getDecimalValue() == null) {
                      hibernateConstraintValidatorContext
                          .addMessageParameter(
                              "associationPropertyType", associationProperty.getType())
                          .addMessageParameter("associationType", association.getType())
                          .buildConstraintViolationWithTemplate(
                              "{digital.inception.party.constraint.ValidAssociation.invalidAssociationPropertyWithNullDecimalValue.message}")
                          .addPropertyNode("properties")
                          .addPropertyNode("decimalValue")
                          .addConstraintViolation();

                      isValid = false;
                    }

                    break;
                  case DOUBLE:
                    if (associationProperty.getDoubleValue() == null) {
                      hibernateConstraintValidatorContext
                          .addMessageParameter(
                              "associationPropertyType", associationProperty.getType())
                          .addMessageParameter("associationType", association.getType())
                          .buildConstraintViolationWithTemplate(
                              "{digital.inception.party.constraint.ValidAssociation.invalidAssociationPropertyWithNullDoubleValue.message}")
                          .addPropertyNode("properties")
                          .addPropertyNode("doubleValue")
                          .addConstraintViolation();

                      isValid = false;
                    }

                    break;
                  case INTEGER:
                    if (associationProperty.getIntegerValue() == null) {
                      hibernateConstraintValidatorContext
                          .addMessageParameter(
                              "associationPropertyType", associationProperty.getType())
                          .addMessageParameter("associationType", association.getType())
                          .buildConstraintViolationWithTemplate(
                              "{digital.inception.party.constraint.ValidAssociation.invalidAssociationPropertyWithNullIntegerValue.message}")
                          .addPropertyNode("properties")
                          .addPropertyNode("integerValue")
                          .addConstraintViolation();

                      isValid = false;
                    }

                    break;
                  case STRING:
                    if (associationProperty.getStringValue() == null) {
                      hibernateConstraintValidatorContext
                          .addMessageParameter(
                              "associationPropertyType", associationProperty.getType())
                          .addMessageParameter("associationType", association.getType())
                          .buildConstraintViolationWithTemplate(
                              "{digital.inception.party.constraint.ValidAssociation.invalidAssociationPropertyWithNullStringValue.message}")
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
                              .addMessageParameter(
                                  "stringValue", associationProperty.getStringValue())
                              .addMessageParameter("associationType", association.getType())
                              .buildConstraintViolationWithTemplate(
                                  "{digital.inception.party.constraint.ValidAssociation.invalidAssociationPropertyWithStringValue.message}")
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
                        "{digital.inception.party.constraint.ValidAssociation.invalidAssociationPropertyType.message}")
                    .addPropertyNode("properties")
                    .addConstraintViolation();

                isValid = false;
              }
            }

            // Validate parties for the associations
            if (association.getFirstPartyId() != null) {
              Optional<PartyType> firstPartyTypeOptional =
                  partyService.getTypeForParty(
                      association.getTenantId(), association.getFirstPartyId());

              if (firstPartyTypeOptional.isPresent()) {
                if (!associationType.isValidFirstPartyType(firstPartyTypeOptional.get().code())) {
                  hibernateConstraintValidatorContext
                      .addMessageParameter("firstPartyType", firstPartyTypeOptional.get().code())
                      .addMessageParameter("firstPartyId", association.getFirstPartyId())
                      .buildConstraintViolationWithTemplate(
                          "{digital.inception.party.constraint.ValidAssociation.invalidPartyTypeForFirstParty.message}")
                      .addPropertyNode("firstPartyId")
                      .addConstraintViolation();

                  isValid = false;
                }
              }
            }

            if (association.getSecondPartyId() != null) {
              Optional<PartyType> secondPartyTypeOptional =
                  partyService.getTypeForParty(
                      association.getTenantId(), association.getFirstPartyId());

              if (secondPartyTypeOptional.isPresent()) {
                if (!associationType.isValidSecondPartyType(secondPartyTypeOptional.get().code())) {
                  hibernateConstraintValidatorContext
                      .addMessageParameter("secondPartyType", secondPartyTypeOptional.get().code())
                      .addMessageParameter("secondPartyId", association.getFirstPartyId())
                      .buildConstraintViolationWithTemplate(
                          "{digital.inception.party.constraint.ValidAssociation.invalidPartyTypeForSecondParty.message}")
                      .addPropertyNode("secondPartyId")
                      .addConstraintViolation();

                  isValid = false;
                }
              }
            }
          } else {
            hibernateConstraintValidatorContext
                .addMessageParameter("associationType", association.getType())
                .buildConstraintViolationWithTemplate(
                    "{digital.inception.party.constraint.ValidAssociation.invalidType.message}")
                .addPropertyNode("type")
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
