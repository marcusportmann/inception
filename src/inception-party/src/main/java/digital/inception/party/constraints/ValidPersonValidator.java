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

import digital.inception.party.ContactMechanism;
import digital.inception.party.IdentityDocument;
import digital.inception.party.PartyRole;
import digital.inception.party.PartyType;
import digital.inception.party.Person;
import digital.inception.party.PhysicalAddress;
import digital.inception.party.PhysicalAddressPurpose;
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
 * The <b>ValidPersonValidator</b> class implements the custom constraint validator for validating a
 * person.
 *
 * @author Marcus Portmann
 */
public class ValidPersonValidator implements ConstraintValidator<ValidPerson, Person> {

  private final IReferenceService referenceService;

  @Autowired
  public ValidPersonValidator(IReferenceService referenceService) {
    this.referenceService = referenceService;
  }

  @Override
  public void initialize(ValidPerson constraintAnnotation) {}

  @Override
  public boolean isValid(Person person, ConstraintValidatorContext constraintValidatorContext) {

    boolean isValid = true;

    // Disable the default constraint violation
    constraintValidatorContext.disableDefaultConstraintViolation();

    HibernateConstraintValidatorContext hibernateConstraintValidatorContext =
        constraintValidatorContext.unwrap(HibernateConstraintValidatorContext.class);

    try {
      // Validate contact mechanisms
      for (ContactMechanism contactMechanism : person.getContactMechanisms()) {
        if (!contactMechanism.getPurpose().type().equals(contactMechanism.getType())) {
          hibernateConstraintValidatorContext
              .addMessageParameter("contactMechanismType", contactMechanism.getType().code())
              .addMessageParameter("contactMechanismPurpose", contactMechanism.getPurpose().code())
              .buildConstraintViolationWithTemplate(
                  "{digital.inception.party.constraints.ValidPerson.invalidContactMechanismPurposeCode.message}")
              .addConstraintViolation();

          isValid = false;
        }

        if (!contactMechanism.getPurpose().isValidForPartyType(person.getPartyType())) {
          hibernateConstraintValidatorContext
              .addMessageParameter("contactMechanismPurpose", contactMechanism.getPurpose().code())
              .addMessageParameter("partyType", person.getPartyType().code())
              .buildConstraintViolationWithTemplate(
                  "{digital.inception.party.constraints.ValidPerson.invalidContactMechanismPurposeCodeForPartyType.message}")
              .addConstraintViolation();

          isValid = false;
        }
      }

      // Validate countries of tax residence
      for (String countryOfTaxResidence : person.getCountriesOfTaxResidence()) {
        if (!referenceService.isValidCountry(countryOfTaxResidence)) {
          hibernateConstraintValidatorContext
              .addMessageParameter("countryOfTaxResidence", countryOfTaxResidence)
              .buildConstraintViolationWithTemplate(
                  "{digital.inception.party.constraints.ValidPerson.invalidCountryOfTaxResidenceCode.message}")
              .addConstraintViolation();

          isValid = false;
        }
      }

      // Validate country of birth
      if (StringUtils.hasText(person.getCountryOfBirth())
          && (!referenceService.isValidCountry(person.getCountryOfBirth()))) {
        hibernateConstraintValidatorContext
            .addMessageParameter("countryOfBirth", person.getCountryOfBirth())
            .buildConstraintViolationWithTemplate(
                "{digital.inception.party.constraints.ValidPerson.invalidCountryOfBirthCode.message}")
            .addConstraintViolation();

        isValid = false;
      }

      // Validate country of residence
      if (StringUtils.hasText(person.getCountryOfResidence())
          && (!referenceService.isValidCountry(person.getCountryOfResidence()))) {
        hibernateConstraintValidatorContext
            .addMessageParameter("countryOfResidence", person.getCountryOfResidence())
            .buildConstraintViolationWithTemplate(
                "{digital.inception.party.constraints.ValidPerson.invalidCountryOfResidenceCode.message}")
            .addConstraintViolation();

        isValid = false;
      }

      // Validate employment status
      if (StringUtils.hasText(person.getEmploymentStatus())
          && (!referenceService.isValidEmploymentStatus(person.getEmploymentStatus()))) {
        hibernateConstraintValidatorContext
            .addMessageParameter("employmentStatus", person.getEmploymentStatus())
            .buildConstraintViolationWithTemplate(
                "{digital.inception.party.constraints.ValidPerson.invalidEmploymentStatusCode.message}")
            .addConstraintViolation();

        isValid = false;
      }

      // Validate employment type
      if (StringUtils.hasText(person.getEmploymentType())
          && (!referenceService.isValidEmploymentType(
          person.getEmploymentStatus(), person.getEmploymentType()))) {
        hibernateConstraintValidatorContext
            .addMessageParameter("employmentType", person.getEmploymentType())
            .buildConstraintViolationWithTemplate(
                "{digital.inception.party.constraints.ValidPerson.invalidEmploymentTypeCode.message}")
            .addConstraintViolation();

        isValid = false;
      }

      // Validate gender
      if (StringUtils.hasText(person.getGender())
          && (!referenceService.isValidGender(person.getGender()))) {
        hibernateConstraintValidatorContext
            .addMessageParameter("gender", person.getGender())
            .buildConstraintViolationWithTemplate(
                "{digital.inception.party.constraints.ValidPerson.invalidGenderCode.message}")
            .addConstraintViolation();

        isValid = false;
      }

      // Validate home language
      if (StringUtils.hasText(person.getHomeLanguage())
          && (!referenceService.isValidLanguage(person.getHomeLanguage()))) {
        hibernateConstraintValidatorContext
            .addMessageParameter("homeLanguage", person.getHomeLanguage())
            .buildConstraintViolationWithTemplate(
                "{digital.inception.party.constraints.ValidPerson.invalidHomeLanguageCode.message}")
            .addConstraintViolation();

        isValid = false;
      }

      // Validate identity documents
      for (IdentityDocument identityDocument : person.getIdentityDocuments()) {
        if (!referenceService.isValidCountry(identityDocument.getCountryOfIssue())) {
          hibernateConstraintValidatorContext
              .addMessageParameter("countryOfIssue", identityDocument.getCountryOfIssue())
              .buildConstraintViolationWithTemplate(
                  "{digital.inception.party.constraints.ValidPerson.invalidIdentityDocumentCountryOfIssueCode.message}")
              .addConstraintViolation();

          isValid = false;
        }

        if (!referenceService.isValidIdentityDocumentType(
            PartyType.PERSON.code(), identityDocument.getType())) {
          hibernateConstraintValidatorContext
              .addMessageParameter("type", identityDocument.getType())
              .buildConstraintViolationWithTemplate(
                  "{digital.inception.party.constraints.ValidPerson.invalidIdentityDocumentTypeCode.message}")
              .addConstraintViolation();

          isValid = false;
        }
      }

      // Validate marital status
      if (StringUtils.hasText(person.getMaritalStatus())
          && (!referenceService.isValidMaritalStatus(person.getMaritalStatus()))) {
        hibernateConstraintValidatorContext
            .addMessageParameter("maritalStatus", person.getMaritalStatus())
            .buildConstraintViolationWithTemplate(
                "{digital.inception.party.constraints.ValidPerson.invalidMaritalStatusCode.message}")
            .addConstraintViolation();

        isValid = false;
      }

      // Validate marriage type
      if (!referenceService.isValidMarriageType(
          person.getMaritalStatus(), person.getMarriageType())) {
        hibernateConstraintValidatorContext
            .addMessageParameter("maritalStatus", person.getMaritalStatus())
            .addMessageParameter("marriageType", person.getMarriageType())
            .buildConstraintViolationWithTemplate(
                "{digital.inception.party.constraints.ValidPerson.invalidMarriageTypeCode.message}")
            .addConstraintViolation();

        isValid = false;
      }

      // Validate occupation
      if (StringUtils.hasText(person.getOccupation())
          && (!referenceService.isValidOccupation(person.getOccupation()))) {
        hibernateConstraintValidatorContext
            .addMessageParameter("occupation", person.getOccupation())
            .buildConstraintViolationWithTemplate(
                "{digital.inception.party.constraints.ValidPerson.invalidOccupationCode.message}")
            .addConstraintViolation();

        isValid = false;
      }

      // Validate party roles
      for (PartyRole partyRole : person.getRoles()) {
        if (!referenceService.isValidPartyRoleType(PartyType.PERSON.code(), partyRole.getType())) {
          hibernateConstraintValidatorContext
              .addMessageParameter("type", partyRole.getType())
              .buildConstraintViolationWithTemplate(
                  "{digital.inception.party.constraints.ValidPerson.invalidPartyRoleTypeCode.message}")
              .addConstraintViolation();

          isValid = false;
        }
      }

      // Validate physical addresses
      for (PhysicalAddress physicalAddress : person.getPhysicalAddresses()) {
        for (PhysicalAddressPurpose physicalAddressPurpose : physicalAddress.getPurposes()) {

          if (!(physicalAddressPurpose
              .isValidForPartyType(person.getPartyType()))) {
            hibernateConstraintValidatorContext
                .addMessageParameter(
                    "physicalAddressPurpose", physicalAddressPurpose.code())
                .addMessageParameter("partyType", person.getPartyType().code())
                .buildConstraintViolationWithTemplate(
                    "{digital.inception.party.constraints.ValidPerson.invalidPhysicalAddressPurposeCodeForPartyType.message}")
                .addConstraintViolation();

            isValid = false;
          }
        }

      }

      // Validate preferences
      for (Preference preference : person.getPreferences()) {
        if (!referenceService.isValidPreferenceType(
            PartyType.PERSON.code(), preference.getType())) {
          hibernateConstraintValidatorContext
              .addMessageParameter("type", preference.getType())
              .buildConstraintViolationWithTemplate(
                  "{digital.inception.party.constraints.ValidPerson.invalidPreferenceTypeCode.message}")
              .addConstraintViolation();

          isValid = false;
        }
      }

      // Validate race
      if (StringUtils.hasText(person.getRace())
          && (!referenceService.isValidRace(person.getRace()))) {
        hibernateConstraintValidatorContext
            .addMessageParameter("race", person.getRace())
            .buildConstraintViolationWithTemplate(
                "{digital.inception.party.constraints.ValidPerson.invalidRaceCode.message}")
            .addConstraintViolation();

        isValid = false;
      }

      // Validate residency status
      if (StringUtils.hasText(person.getResidencyStatus())
          && (!referenceService.isValidResidencyStatus(person.getResidencyStatus()))) {
        hibernateConstraintValidatorContext
            .addMessageParameter("residencyStatus", person.getResidencyStatus())
            .buildConstraintViolationWithTemplate(
                "{digital.inception.party.constraints.ValidPerson.invalidResidencyStatusCode.message}")
            .addConstraintViolation();

        isValid = false;
      }

      // Validate residential type
      if (StringUtils.hasText(person.getResidentialType())
          && (!referenceService.isValidResidentialType(person.getResidentialType()))) {
        hibernateConstraintValidatorContext
            .addMessageParameter("residentialType", person.getResidentialType())
            .buildConstraintViolationWithTemplate(
                "{digital.inception.party.constraints.ValidPerson.invalidResidentialTypeCode.message}")
            .addConstraintViolation();

        isValid = false;
      }

      // Validate tax numbers
      for (TaxNumber taxNumber : person.getTaxNumbers()) {
        if (!referenceService.isValidCountry(taxNumber.getCountryOfIssue())) {
          hibernateConstraintValidatorContext
              .addMessageParameter("countryOfIssue", taxNumber.getCountryOfIssue())
              .buildConstraintViolationWithTemplate(
                  "{digital.inception.party.constraints.ValidPerson.invalidTaxNumberCountryOfIssueCode.message}")
              .addConstraintViolation();

          isValid = false;
        }

        if (!referenceService.isValidTaxNumberType(taxNumber.getType())) {
          hibernateConstraintValidatorContext
              .addMessageParameter("type", taxNumber.getType())
              .buildConstraintViolationWithTemplate(
                  "{digital.inception.party.constraints.ValidPerson.invalidTaxNumberTypeCode.message}")
              .addConstraintViolation();

          isValid = false;
        }
      }

      // Validate title
      if (StringUtils.hasText(person.getTitle())
          && (!referenceService.isValidTitle(person.getTitle()))) {
        hibernateConstraintValidatorContext
            .addMessageParameter("title", person.getTitle())
            .buildConstraintViolationWithTemplate(
                "{digital.inception.party.constraints.ValidPerson.invalidTitleCode.message}")
            .addConstraintViolation();

        isValid = false;
      }

    } catch (Throwable e) {
      throw new ValidationException("Failed to validate the person", e);
    }

    return isValid;
  }
}
