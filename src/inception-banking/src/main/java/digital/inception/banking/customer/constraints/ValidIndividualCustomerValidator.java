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

package digital.inception.banking.customer.constraints;

import digital.inception.banking.customer.IndividualCustomer;
import digital.inception.party.ContactMechanism;
import digital.inception.party.IdentityDocument;
import digital.inception.party.PartyRole;
import digital.inception.party.PartyType;
import digital.inception.party.Preference;
import digital.inception.party.TaxNumber;
import digital.inception.reference.IReferenceService;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.ValidationException;
import org.hibernate.validator.constraintvalidation.HibernateConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

/**
 * The <b>ValidIndividualCustomerValidator</b> class implements the custom constraint validator for
 * validating an individual customer.
 *
 * @author Marcus Portmann
 */
public class ValidIndividualCustomerValidator
    implements ConstraintValidator<ValidIndividualCustomer, IndividualCustomer> {

  private final IReferenceService referenceService;

  @Autowired
  public ValidIndividualCustomerValidator(IReferenceService referenceService) {
    this.referenceService = referenceService;
  }

  @Override
  public void initialize(ValidIndividualCustomer constraintAnnotation) {}

  @Override
  public boolean isValid(
      IndividualCustomer individualCustomer,
      ConstraintValidatorContext constraintValidatorContext) {

    boolean isValid = true;

    // Disable the default constraint violation
    constraintValidatorContext.disableDefaultConstraintViolation();

    HibernateConstraintValidatorContext hibernateConstraintValidatorContext =
        constraintValidatorContext.unwrap(HibernateConstraintValidatorContext.class);

    try {
      // Validate the attributes inherited from the Person entity
      for (ContactMechanism contactMechanism : individualCustomer.getContactMechanisms()) {
        if (!(contactMechanism
            .getPurpose()
            .isValidForPartyType(individualCustomer.getPartyType()))) {
          hibernateConstraintValidatorContext
              .addMessageParameter("contactMechanismPurpose", contactMechanism.getPurpose().code())
              .addMessageParameter("partyType", individualCustomer.getPartyType().code())
              .buildConstraintViolationWithTemplate(
                  "{digital.inception.banking.customer.constraints.ValidIndividualCustomer.invalidContactMechanismPurposeCodeForPartyType.message}")
              .addConstraintViolation();

          isValid = false;
        }

        if (!contactMechanism.getPurpose().type().equals(contactMechanism.getType())) {
          hibernateConstraintValidatorContext
              .addMessageParameter("contactMechanismType", contactMechanism.getType().code())
              .addMessageParameter("contactMechanismPurpose", contactMechanism.getPurpose().code())
              .buildConstraintViolationWithTemplate(
                  "{digital.inception.banking.customer.constraints.ValidIndividualCustomer.invalidContactMechanismPurposeCode.message}")
              .addConstraintViolation();

          isValid = false;
        }
      }

      for (IdentityDocument identityDocument : individualCustomer.getIdentityDocuments()) {
        if (!referenceService.isValidIdentityDocumentType(
            PartyType.PERSON.code(), identityDocument.getType())) {
          hibernateConstraintValidatorContext
              .addMessageParameter("type", identityDocument.getType())
              .buildConstraintViolationWithTemplate(
                  "{digital.inception.banking.customer.constraints.ValidIndividualCustomer.invalidIdentityDocumentTypeCode.message}")
              .addConstraintViolation();

          isValid = false;
        }

        if (!referenceService.isValidCountry(identityDocument.getCountryOfIssue())) {
          hibernateConstraintValidatorContext
              .addMessageParameter("countryOfIssue", identityDocument.getCountryOfIssue())
              .buildConstraintViolationWithTemplate(
                  "{digital.inception.banking.customer.constraints.ValidIndividualCustomer.invalidIdentityDocumentCountryOfIssueCode.message}")
              .addConstraintViolation();

          isValid = false;
        }
      }

      for (Preference preference : individualCustomer.getPreferences()) {
        if (!referenceService.isValidPreferenceType(
            PartyType.PERSON.code(), preference.getType())) {
          hibernateConstraintValidatorContext
              .addMessageParameter("type", preference.getType())
              .buildConstraintViolationWithTemplate(
                  "{digital.inception.banking.customer.constraints.ValidIndividualCustomer.invalidPreferenceTypeCode.message}")
              .addConstraintViolation();

          isValid = false;
        }
      }

      for (PartyRole partyRole : individualCustomer.getRoles()) {
        if (!referenceService.isValidPartyRoleType(PartyType.PERSON.code(), partyRole.getType())) {
          hibernateConstraintValidatorContext
              .addMessageParameter("type", partyRole.getType())
              .buildConstraintViolationWithTemplate(
                  "{digital.inception.banking.customer.constraints.ValidIndividualCustomer.invalidRoleTypeCode.message}")
              .addConstraintViolation();

          isValid = false;
        }
      }

      for (TaxNumber taxNumber : individualCustomer.getTaxNumbers()) {
        if (!referenceService.isValidTaxNumberType(taxNumber.getType())) {
          hibernateConstraintValidatorContext
              .addMessageParameter("type", taxNumber.getType())
              .buildConstraintViolationWithTemplate(
                  "{digital.inception.banking.customer.constraints.ValidIndividualCustomer.invalidTaxNumberTypeCode.message}")
              .addConstraintViolation();

          isValid = false;
        }

        if (!referenceService.isValidCountry(taxNumber.getCountryOfIssue())) {
          hibernateConstraintValidatorContext
              .addMessageParameter("countryOfIssue", taxNumber.getCountryOfIssue())
              .buildConstraintViolationWithTemplate(
                  "{digital.inception.banking.customer.constraints.ValidIndividualCustomer.invalidTaxNumberCountryOfIssueCode.message}")
              .addConstraintViolation();

          isValid = false;
        }
      }

      for (String countryOfTaxResidence : individualCustomer.getCountriesOfTaxResidence()) {
        if (!referenceService.isValidCountry(countryOfTaxResidence)) {
          hibernateConstraintValidatorContext
              .addMessageParameter("countryOfTaxResidence", countryOfTaxResidence)
              .buildConstraintViolationWithTemplate(
                  "{digital.inception.banking.customer.constraints.ValidIndividualCustomer.invalidCountryOfTaxResidenceCode.message}")
              .addConstraintViolation();

          isValid = false;
        }
      }

      if (StringUtils.hasText(individualCustomer.getCountryOfBirth())
          && (!referenceService.isValidCountry(individualCustomer.getCountryOfBirth()))) {
        hibernateConstraintValidatorContext
            .addMessageParameter("countryOfBirth", individualCustomer.getCountryOfBirth())
            .buildConstraintViolationWithTemplate(
                "{digital.inception.banking.customer.constraints.ValidIndividualCustomer.invalidCountryOfBirthCode.message}")
            .addConstraintViolation();

        isValid = false;
      }

      if (StringUtils.hasText(individualCustomer.getCountryOfResidence())
          && (!referenceService.isValidCountry(individualCustomer.getCountryOfResidence()))) {
        hibernateConstraintValidatorContext
            .addMessageParameter("countryOfResidence", individualCustomer.getCountryOfResidence())
            .buildConstraintViolationWithTemplate(
                "{digital.inception.banking.customer.constraints.ValidIndividualCustomer.invalidCountryOfResidenceCode.message}")
            .addConstraintViolation();

        isValid = false;
      }

      if (StringUtils.hasText(individualCustomer.getEmploymentStatus())
          && (!referenceService.isValidEmploymentStatus(
              individualCustomer.getEmploymentStatus()))) {
        hibernateConstraintValidatorContext
            .addMessageParameter("employmentStatus", individualCustomer.getEmploymentStatus())
            .buildConstraintViolationWithTemplate(
                "{digital.inception.banking.customer.constraints.ValidIndividualCustomer.invalidEmploymentStatusCode.message}")
            .addConstraintViolation();

        isValid = false;
      }

      if (StringUtils.hasText(individualCustomer.getEmploymentType())
          && (!referenceService.isValidEmploymentType(
              individualCustomer.getEmploymentStatus(), individualCustomer.getEmploymentType()))) {
        hibernateConstraintValidatorContext
            .addMessageParameter("employmentType", individualCustomer.getEmploymentType())
            .buildConstraintViolationWithTemplate(
                "{digital.inception.banking.customer.constraints.ValidIndividualCustomer.invalidEmploymentTypeCode.message}")
            .addConstraintViolation();

        isValid = false;
      }

      if (StringUtils.hasText(individualCustomer.getGender())
          && (!referenceService.isValidGender(individualCustomer.getGender()))) {
        hibernateConstraintValidatorContext
            .addMessageParameter("gender", individualCustomer.getGender())
            .buildConstraintViolationWithTemplate(
                "{digital.inception.banking.customer.constraints.ValidIndividualCustomer.invalidGenderCode.message}")
            .addConstraintViolation();

        isValid = false;
      }

      if (StringUtils.hasText(individualCustomer.getHomeLanguage())
          && (!referenceService.isValidLanguage(individualCustomer.getHomeLanguage()))) {
        hibernateConstraintValidatorContext
            .addMessageParameter("homeLanguage", individualCustomer.getHomeLanguage())
            .buildConstraintViolationWithTemplate(
                "{digital.inception.banking.customer.constraints.ValidIndividualCustomer.invalidHomeLanguageCode.message}")
            .addConstraintViolation();

        isValid = false;
      }

      if (StringUtils.hasText(individualCustomer.getMaritalStatus())
          && (!referenceService.isValidMaritalStatus(individualCustomer.getMaritalStatus()))) {
        hibernateConstraintValidatorContext
            .addMessageParameter("maritalStatus", individualCustomer.getMaritalStatus())
            .buildConstraintViolationWithTemplate(
                "{digital.inception.banking.customer.constraints.ValidIndividualCustomer.invalidMaritalStatusCode.message}")
            .addConstraintViolation();

        isValid = false;
      }

      if (StringUtils.hasText(individualCustomer.getMarriageType())
          && (!referenceService.isValidMarriageType(
              individualCustomer.getMaritalStatus(), individualCustomer.getMarriageType()))) {
        hibernateConstraintValidatorContext
            .addMessageParameter("maritalStatus", individualCustomer.getMaritalStatus())
            .addMessageParameter("marriageType", individualCustomer.getMarriageType())
            .buildConstraintViolationWithTemplate(
                "{digital.inception.banking.customer.constraints.ValidIndividualCustomer.invalidMarriageTypeCode.message}")
            .addConstraintViolation();

        isValid = false;
      }

      if (StringUtils.hasText(individualCustomer.getOccupation())
          && (!referenceService.isValidOccupation(individualCustomer.getOccupation()))) {
        hibernateConstraintValidatorContext
            .addMessageParameter("occupation", individualCustomer.getOccupation())
            .buildConstraintViolationWithTemplate(
                "{digital.inception.banking.customer.constraints.ValidIndividualCustomer.invalidOccupationCode.message}")
            .addConstraintViolation();

        isValid = false;
      }

      if (StringUtils.hasText(individualCustomer.getRace())
          && (!referenceService.isValidRace(individualCustomer.getRace()))) {
        hibernateConstraintValidatorContext
            .addMessageParameter("race", individualCustomer.getRace())
            .buildConstraintViolationWithTemplate(
                "{digital.inception.banking.customer.constraints.ValidIndividualCustomer.invalidRaceCode.message}")
            .addConstraintViolation();

        isValid = false;
      }

      if (StringUtils.hasText(individualCustomer.getResidencyStatus())
          && (!referenceService.isValidResidencyStatus(individualCustomer.getResidencyStatus()))) {
        hibernateConstraintValidatorContext
            .addMessageParameter("residencyStatus", individualCustomer.getResidencyStatus())
            .buildConstraintViolationWithTemplate(
                "{digital.inception.banking.customer.constraints.ValidIndividualCustomer.invalidResidencyStatusCode.message}")
            .addConstraintViolation();

        isValid = false;
      }

      if (StringUtils.hasText(individualCustomer.getResidentialType())
          && (!referenceService.isValidResidentialType(individualCustomer.getResidentialType()))) {
        hibernateConstraintValidatorContext
            .addMessageParameter("residentialType", individualCustomer.getResidentialType())
            .buildConstraintViolationWithTemplate(
                "{digital.inception.banking.customer.constraints.ValidIndividualCustomer.invalidResidentialTypeCode.message}")
            .addConstraintViolation();

        isValid = false;
      }

      if (StringUtils.hasText(individualCustomer.getTitle())
          && (!referenceService.isValidTitle(individualCustomer.getTitle()))) {
        hibernateConstraintValidatorContext
            .addMessageParameter("title", individualCustomer.getTitle())
            .buildConstraintViolationWithTemplate(
                "{digital.inception.banking.customer.constraints.ValidIndividualCustomer.invalidTitleCode.message}")
            .addConstraintViolation();

        isValid = false;
      }

      // Validate the attributes specific to the Individual Customer entity

    } catch (Throwable e) {
      throw new ValidationException("Failed to validate the individual customer", e);
    }

    return isValid;
  }
}
