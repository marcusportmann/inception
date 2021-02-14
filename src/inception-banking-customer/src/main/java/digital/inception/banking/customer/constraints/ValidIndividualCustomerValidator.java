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
import digital.inception.party.IPartyReferenceService;
import digital.inception.party.IdentityDocument;
import digital.inception.party.PartyRole;
import digital.inception.party.PhysicalAddress;
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

  private final IPartyReferenceService partyReferenceService;

  private final IReferenceService referenceService;

  @Autowired
  public ValidIndividualCustomerValidator(
      IPartyReferenceService partyReferenceService, IReferenceService referenceService) {
    this.partyReferenceService = partyReferenceService;
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
      // Validate contact mechanisms
      for (ContactMechanism contactMechanism : individualCustomer.getContactMechanisms()) {
        if (!partyReferenceService.isValidContactMechanismType(contactMechanism.getType())) {
          hibernateConstraintValidatorContext
              .addMessageParameter("contactMechanismType", contactMechanism.getType())
              .addMessageParameter("contactMechanismPurpose", contactMechanism.getPurpose())
              .buildConstraintViolationWithTemplate(
                  "{digital.inception.banking.customer.constraints.ValidIndividualCustomer.invalidContactMechanismPurposeCode.message}")
              .addConstraintViolation();

          isValid = false;
        }

        if (!partyReferenceService.isValidContactMechanismPurpose(
            individualCustomer.getPartyType().code(),
            contactMechanism.getType(),
            contactMechanism.getPurpose())) {

          hibernateConstraintValidatorContext
              .addMessageParameter("contactMechanismPurpose", contactMechanism.getPurpose())
              .addMessageParameter("partyType", individualCustomer.getPartyType().code())
              .buildConstraintViolationWithTemplate(
                  "{digital.inception.banking.customer.constraints.ValidIndividualCustomer.invalidContactMechanismPurposeCodeForPartyType.message}")
              .addConstraintViolation();

          isValid = false;
        }
      }

      // Validate countries of tax residence
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

      // Validate country of birth
      if ((!StringUtils.hasText(individualCustomer.getCountryOfBirth()))
          || (!referenceService.isValidCountry(individualCustomer.getCountryOfBirth()))) {
        hibernateConstraintValidatorContext
            .addMessageParameter("countryOfBirth", individualCustomer.getCountryOfBirth())
            .buildConstraintViolationWithTemplate(
                "{digital.inception.banking.customer.constraints.ValidIndividualCustomer.invalidCountryOfBirthCode.message}")
            .addConstraintViolation();

        isValid = false;
      }

      // Validate country of residence
      if ((!StringUtils.hasText(individualCustomer.getCountryOfResidence()))
          || (!referenceService.isValidCountry(individualCustomer.getCountryOfResidence()))) {
        hibernateConstraintValidatorContext
            .addMessageParameter("countryOfResidence", individualCustomer.getCountryOfResidence())
            .buildConstraintViolationWithTemplate(
                "{digital.inception.banking.customer.constraints.ValidIndividualCustomer.invalidCountryOfResidenceCode.message}")
            .addConstraintViolation();

        isValid = false;
      }

      // Validate employment status
      if ((!StringUtils.hasText(individualCustomer.getEmploymentStatus()))
          || (!partyReferenceService.isValidEmploymentStatus(
              individualCustomer.getEmploymentStatus()))) {
        hibernateConstraintValidatorContext
            .addMessageParameter("employmentStatus", individualCustomer.getEmploymentStatus())
            .buildConstraintViolationWithTemplate(
                "{digital.inception.banking.customer.constraints.ValidIndividualCustomer.invalidEmploymentStatusCode.message}")
            .addConstraintViolation();

        isValid = false;
      }

      // Validate employment type
      if ((!StringUtils.hasText(individualCustomer.getEmploymentType()))
          || (!partyReferenceService.isValidEmploymentType(
              individualCustomer.getEmploymentStatus(), individualCustomer.getEmploymentType()))) {
        hibernateConstraintValidatorContext
            .addMessageParameter("employmentType", individualCustomer.getEmploymentType())
            .buildConstraintViolationWithTemplate(
                "{digital.inception.banking.customer.constraints.ValidIndividualCustomer.invalidEmploymentTypeCode.message}")
            .addConstraintViolation();

        isValid = false;
      }

      // Validate gender
      if ((!StringUtils.hasText(individualCustomer.getGender()))
          || (!partyReferenceService.isValidGender(individualCustomer.getGender()))) {
        hibernateConstraintValidatorContext
            .addMessageParameter("gender", individualCustomer.getGender())
            .buildConstraintViolationWithTemplate(
                "{digital.inception.banking.customer.constraints.ValidIndividualCustomer.invalidGenderCode.message}")
            .addConstraintViolation();

        isValid = false;
      }

      // Validate home language
      if ((!StringUtils.hasText(individualCustomer.getHomeLanguage()))
          || (!referenceService.isValidLanguage(individualCustomer.getHomeLanguage()))) {
        hibernateConstraintValidatorContext
            .addMessageParameter("homeLanguage", individualCustomer.getHomeLanguage())
            .buildConstraintViolationWithTemplate(
                "{digital.inception.banking.customer.constraints.ValidIndividualCustomer.invalidHomeLanguageCode.message}")
            .addConstraintViolation();

        isValid = false;
      }

      // Validate identity documents
      for (IdentityDocument identityDocument : individualCustomer.getIdentityDocuments()) {
        if (!referenceService.isValidCountry(identityDocument.getCountryOfIssue())) {
          hibernateConstraintValidatorContext
              .addMessageParameter("countryOfIssue", identityDocument.getCountryOfIssue())
              .buildConstraintViolationWithTemplate(
                  "{digital.inception.banking.customer.constraints.ValidIndividualCustomer.invalidIdentityDocumentCountryOfIssueCode.message}")
              .addConstraintViolation();

          isValid = false;
        }

        if (!partyReferenceService.isValidIdentityDocumentType(
            individualCustomer.getPartyType().code(), identityDocument.getType())) {
          hibernateConstraintValidatorContext
              .addMessageParameter("type", identityDocument.getType())
              .buildConstraintViolationWithTemplate(
                  "{digital.inception.banking.customer.constraints.ValidIndividualCustomer.invalidIdentityDocumentTypeCode.message}")
              .addConstraintViolation();

          isValid = false;
        }
      }

      // Validate marital status
      if ((!StringUtils.hasText(individualCustomer.getMaritalStatus()))
          || (!partyReferenceService.isValidMaritalStatus(individualCustomer.getMaritalStatus()))) {
        hibernateConstraintValidatorContext
            .addMessageParameter("maritalStatus", individualCustomer.getMaritalStatus())
            .buildConstraintViolationWithTemplate(
                "{digital.inception.banking.customer.constraints.ValidIndividualCustomer.invalidMaritalStatusCode.message}")
            .addConstraintViolation();

        isValid = false;
      }

      // Validate marriage type
      if (!partyReferenceService.isValidMarriageType(
          individualCustomer.getMaritalStatus(), individualCustomer.getMarriageType())) {
        hibernateConstraintValidatorContext
            .addMessageParameter("maritalStatus", individualCustomer.getMaritalStatus())
            .addMessageParameter("marriageType", individualCustomer.getMarriageType())
            .buildConstraintViolationWithTemplate(
                "{digital.inception.banking.customer.constraints.ValidIndividualCustomer.invalidMarriageTypeCode.message}")
            .addConstraintViolation();

        isValid = false;
      }

      // Validate occupation
      if ((!StringUtils.hasText(individualCustomer.getOccupation()))
          || (!partyReferenceService.isValidOccupation(individualCustomer.getOccupation()))) {
        hibernateConstraintValidatorContext
            .addMessageParameter("occupation", individualCustomer.getOccupation())
            .buildConstraintViolationWithTemplate(
                "{digital.inception.banking.customer.constraints.ValidIndividualCustomer.invalidOccupationCode.message}")
            .addConstraintViolation();

        isValid = false;
      }

      // Validate party roles
      for (PartyRole partyRole : individualCustomer.getRoles()) {
        if (!partyReferenceService.isValidPartyRoleType(
            individualCustomer.getPartyType().code(), partyRole.getType())) {
          hibernateConstraintValidatorContext
              .addMessageParameter("type", partyRole.getType())
              .buildConstraintViolationWithTemplate(
                  "{digital.inception.banking.customer.constraints.ValidIndividualCustomer.invalidPartyRoleTypeCode.message}")
              .addConstraintViolation();

          isValid = false;
        }
      }

      // Validate physical addresses
      for (PhysicalAddress physicalAddress : individualCustomer.getPhysicalAddresses()) {
        if (!partyReferenceService.isValidPhysicalAddressRole(
            individualCustomer.getPartyType().code(), physicalAddress.getRole())) {
          hibernateConstraintValidatorContext
              .addMessageParameter("physicalAddressRole", physicalAddress.getRole())
              .addMessageParameter("partyType", individualCustomer.getPartyType().code())
              .buildConstraintViolationWithTemplate(
                  "{digital.inception.banking.customer.constraints.ValidIndividualCustomer.invalidPhysicalAddressRoleCodeForPartyType.message}")
              .addConstraintViolation();

          isValid = false;
        }

        for (String physicalAddressPurpose : physicalAddress.getPurposes()) {
          if (!partyReferenceService.isValidPhysicalAddressPurpose(
              individualCustomer.getPartyType().code(), physicalAddressPurpose)) {
            hibernateConstraintValidatorContext
                .addMessageParameter("physicalAddressPurpose", physicalAddressPurpose)
                .addMessageParameter("partyType", individualCustomer.getPartyType().code())
                .buildConstraintViolationWithTemplate(
                    "{digital.inception.banking.customer.constraints.ValidIndividualCustomer.invalidPhysicalAddressPurposeCodeForPartyType.message}")
                .addConstraintViolation();

            isValid = false;
          }
        }
      }

      // Validate preferences
      for (Preference preference : individualCustomer.getPreferences()) {
        if (!partyReferenceService.isValidPreferenceType(
            individualCustomer.getPartyType().code(), preference.getType())) {
          hibernateConstraintValidatorContext
              .addMessageParameter("type", preference.getType())
              .buildConstraintViolationWithTemplate(
                  "{digital.inception.banking.customer.constraints.ValidIndividualCustomer.invalidPreferenceTypeCode.message}")
              .addConstraintViolation();

          isValid = false;
        }
      }

      // Validate race
      if ((!StringUtils.hasText(individualCustomer.getRace()))
          || (!partyReferenceService.isValidRace(individualCustomer.getRace()))) {
        hibernateConstraintValidatorContext
            .addMessageParameter("race", individualCustomer.getRace())
            .buildConstraintViolationWithTemplate(
                "{digital.inception.banking.customer.constraints.ValidIndividualCustomer.invalidRaceCode.message}")
            .addConstraintViolation();

        isValid = false;
      }

      //  Validate residency status
      if ((!StringUtils.hasText(individualCustomer.getResidencyStatus()))
          || (!partyReferenceService.isValidResidencyStatus(
              individualCustomer.getResidencyStatus()))) {
        hibernateConstraintValidatorContext
            .addMessageParameter("residencyStatus", individualCustomer.getResidencyStatus())
            .buildConstraintViolationWithTemplate(
                "{digital.inception.banking.customer.constraints.ValidIndividualCustomer.invalidResidencyStatusCode.message}")
            .addConstraintViolation();

        isValid = false;
      }

      // Validate residential type
      if ((!StringUtils.hasText(individualCustomer.getResidentialType()))
          || (!partyReferenceService.isValidResidentialType(
              individualCustomer.getResidentialType()))) {
        hibernateConstraintValidatorContext
            .addMessageParameter("residentialType", individualCustomer.getResidentialType())
            .buildConstraintViolationWithTemplate(
                "{digital.inception.banking.customer.constraints.ValidIndividualCustomer.invalidResidentialTypeCode.message}")
            .addConstraintViolation();

        isValid = false;
      }

      // Validate tax numbers
      for (TaxNumber taxNumber : individualCustomer.getTaxNumbers()) {
        if (!referenceService.isValidCountry(taxNumber.getCountryOfIssue())) {
          hibernateConstraintValidatorContext
              .addMessageParameter("countryOfIssue", taxNumber.getCountryOfIssue())
              .buildConstraintViolationWithTemplate(
                  "{digital.inception.banking.customer.constraints.ValidIndividualCustomer.invalidTaxNumberCountryOfIssueCode.message}")
              .addConstraintViolation();

          isValid = false;
        }

        if (!partyReferenceService.isValidTaxNumberType(taxNumber.getType())) {
          hibernateConstraintValidatorContext
              .addMessageParameter("type", taxNumber.getType())
              .buildConstraintViolationWithTemplate(
                  "{digital.inception.banking.customer.constraints.ValidIndividualCustomer.invalidTaxNumberTypeCode.message}")
              .addConstraintViolation();

          isValid = false;
        }
      }

      // Validate title
      if ((!StringUtils.hasText(individualCustomer.getTitle()))
          || (!partyReferenceService.isValidTitle(individualCustomer.getTitle()))) {
        hibernateConstraintValidatorContext
            .addMessageParameter("title", individualCustomer.getTitle())
            .buildConstraintViolationWithTemplate(
                "{digital.inception.banking.customer.constraints.ValidIndividualCustomer.invalidTitleCode.message}")
            .addConstraintViolation();

        isValid = false;
      }

    } catch (Throwable e) {
      throw new ValidationException("Failed to validate the person", e);
    }

    return isValid;
  }
}
