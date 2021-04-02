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

import digital.inception.party.Attribute;
import digital.inception.party.Consent;
import digital.inception.party.ConstraintType;
import digital.inception.party.ContactMechanism;
import digital.inception.party.Education;
import digital.inception.party.Employment;
import digital.inception.party.IPartyReferenceService;
import digital.inception.party.IdentityDocument;
import digital.inception.party.Lock;
import digital.inception.party.Person;
import digital.inception.party.PhysicalAddress;
import digital.inception.party.Preference;
import digital.inception.party.ResidencePermit;
import digital.inception.party.Role;
import digital.inception.party.RoleTypeAttributeTypeConstraint;
import digital.inception.party.RoleTypePreferenceTypeConstraint;
import digital.inception.party.SourceOfFunds;
import digital.inception.party.SourceOfWealth;
import digital.inception.party.Status;
import digital.inception.party.TaxNumber;
import digital.inception.reference.IReferenceService;
import java.util.Optional;
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
 * <p>NOTE: The <b>@Autowired</b> constructor is not used when Spring Boot automatically invokes the
 * JSR 380 validation when persisting an entity. Instead, the default constructor is invoked when
 * the validator is initialized by Hibernate. As a result the custom validation that requires the
 * injected services will not be executed.
 *
 * @author Marcus Portmann
 */
public class ValidPersonValidator extends PartyValidator
    implements ConstraintValidator<ValidPerson, Person> {

  /**
   * Constructs a new <b>ValidPersonValidator</b>.
   *
   * @param partyReferenceService the Party Reference Service
   * @param referenceService the Reference Service
   */
  @Autowired
  public ValidPersonValidator(
      IPartyReferenceService partyReferenceService, IReferenceService referenceService) {
    super(partyReferenceService, referenceService);
  }

  /** Constructs a new <b>ValidPersonValidator</b>. */
  public ValidPersonValidator() {
    super(null, null);
  }

  @Override
  public void initialize(ValidPerson constraintAnnotation) {}

  @Override
  public boolean isValid(Person person, ConstraintValidatorContext constraintValidatorContext) {

    if ((getPartyReferenceService() != null) && (getReferenceService() != null)) {
      boolean isValid = true;

      // Disable the default constraint violation
      constraintValidatorContext.disableDefaultConstraintViolation();

      HibernateConstraintValidatorContext hibernateConstraintValidatorContext =
          constraintValidatorContext.unwrap(HibernateConstraintValidatorContext.class);

      try {
        // Validate attributes
        for (Attribute attribute : person.getAttributes()) {
          for (String reservedAttributeTypeCode : Attribute.RESERVED_ATTRIBUTE_TYPE_CODES) {
            if (reservedAttributeTypeCode.equalsIgnoreCase(attribute.getType())) {
              hibernateConstraintValidatorContext
                  .addMessageParameter("attributeType", attribute.getType())
                  .buildConstraintViolationWithTemplate(
                      "{digital.inception.party.constraints.ValidPerson.invalidReservedAttributeType.message}")
                  .addPropertyNode("attributes")
                  .addPropertyNode("type")
                  .inIterable()
                  .addConstraintViolation();

              isValid = false;
            }
          }
        }

        // Validate consents
        for (Consent consent : person.getConsents()) {
          if (StringUtils.hasText(consent.getType())) {
            if (!getPartyReferenceService().isValidConsentType(consent.getType())) {
              hibernateConstraintValidatorContext
                  .addMessageParameter("consentType", consent.getType())
                  .buildConstraintViolationWithTemplate(
                      "{digital.inception.party.constraints.ValidPerson.invalidConsentType.message}")
                  .addPropertyNode("consents")
                  .addPropertyNode("type")
                  .inIterable()
                  .addConstraintViolation();

              isValid = false;
            }
          }
        }

        // Validate contact mechanisms
        for (ContactMechanism contactMechanism : person.getContactMechanisms()) {
          if (StringUtils.hasText(contactMechanism.getType())) {
            if (!ContactMechanism.VALID_CONTACT_MECHANISM_TYPES.contains(
                contactMechanism.getType())) {
              // Do not add a constraint violation here as it would duplicate the regex validation
            } else {

              if (StringUtils.hasText(contactMechanism.getRole())) {
                if (!getPartyReferenceService()
                    .isValidContactMechanismRole(
                        person.getType().code(),
                        contactMechanism.getType(),
                        contactMechanism.getRole())) {
                  hibernateConstraintValidatorContext
                      .addMessageParameter("contactMechanismRole", contactMechanism.getRole())
                      .addMessageParameter("contactMechanismType", contactMechanism.getType())
                      .addMessageParameter("partyType", person.getType().code())
                      .buildConstraintViolationWithTemplate(
                          "{digital.inception.party.constraints.ValidPerson.invalidContactMechanismRoleForPartyType.message}")
                      .addPropertyNode("contactMechanisms")
                      .addPropertyNode("role")
                      .inIterable()
                      .addConstraintViolation();

                  isValid = false;
                }
              }

              for (String contactMechanismPurpose : contactMechanism.getPurposes()) {
                if (!getPartyReferenceService()
                    .isValidContactMechanismPurpose(
                        person.getType().code(),
                        contactMechanism.getType(),
                        contactMechanismPurpose)) {
                  hibernateConstraintValidatorContext
                      .addMessageParameter("contactMechanismPurpose", contactMechanismPurpose)
                      .addMessageParameter("contactMechanismType", contactMechanism.getType())
                      .addMessageParameter("partyType", person.getType().code())
                      .buildConstraintViolationWithTemplate(
                          "{digital.inception.party.constraints.ValidPerson.invalidContactMechanismPurposeForPartyType.message}")
                      .addPropertyNode("contactMechanisms")
                      .addPropertyNode("purpose")
                      .inIterable()
                      .addConstraintViolation();

                  isValid = false;
                }
              }

              if (!validateContactMechanismValue(
                  contactMechanism, hibernateConstraintValidatorContext)) {
                isValid = false;
              }
            }
          }
        }

        // Validate countries of citizenship
        for (String countryOfCitizenship : person.getCountriesOfCitizenship()) {
          if (!getReferenceService().isValidCountry(countryOfCitizenship)) {
            hibernateConstraintValidatorContext
                .addMessageParameter("countryOfCitizenship", countryOfCitizenship)
                .buildConstraintViolationWithTemplate(
                    "{digital.inception.party.constraints.ValidPerson.invalidCountryOfCitizenship.message}")
                .addPropertyNode("countriesOfCitizenship")
                .addConstraintViolation();

            isValid = false;
          }
        }

        // Validate countries of tax residence
        for (String countryOfTaxResidence : person.getCountriesOfTaxResidence()) {
          if (!getReferenceService().isValidCountry(countryOfTaxResidence)) {
            hibernateConstraintValidatorContext
                .addMessageParameter("countryOfTaxResidence", countryOfTaxResidence)
                .buildConstraintViolationWithTemplate(
                    "{digital.inception.party.constraints.ValidPerson.invalidCountryOfTaxResidence.message}")
                .addPropertyNode("countriesOfTaxResidence")
                .addConstraintViolation();

            isValid = false;
          }
        }

        // Validate country of birth
        if (StringUtils.hasText(person.getCountryOfBirth())
            && (!getReferenceService().isValidCountry(person.getCountryOfBirth()))) {
          hibernateConstraintValidatorContext
              .addMessageParameter("countryOfBirth", person.getCountryOfBirth())
              .buildConstraintViolationWithTemplate(
                  "{digital.inception.party.constraints.ValidPerson.invalidCountryOfBirth.message}")
              .addPropertyNode("countryOfBirth")
              .addConstraintViolation();

          isValid = false;
        }

        // Validate country of residence
        if (StringUtils.hasText(person.getCountryOfResidence())
            && (!getReferenceService().isValidCountry(person.getCountryOfResidence()))) {
          hibernateConstraintValidatorContext
              .addMessageParameter("countryOfResidence", person.getCountryOfResidence())
              .buildConstraintViolationWithTemplate(
                  "{digital.inception.party.constraints.ValidPerson.invalidCountryOfResidence.message}")
              .addPropertyNode("countryOfResidence")
              .addConstraintViolation();

          isValid = false;
        }

        // Validate educations
        for (Education education : person.getEducations()) {
          if (StringUtils.hasText(education.getInstitutionCountry())) {
            if (!getReferenceService().isValidCountry(education.getInstitutionCountry())) {
              hibernateConstraintValidatorContext
                  .addMessageParameter("institutionCountry", education.getInstitutionCountry())
                  .buildConstraintViolationWithTemplate(
                      "{digital.inception.party.constraints.ValidPerson.invalidInstitutionCountryForEducation.message}")
                  .addPropertyNode("educations")
                  .addPropertyNode("institutionCountry")
                  .inIterable()
                  .addConstraintViolation();

              isValid = false;
            }
          }

          if (StringUtils.hasText(education.getQualificationType())) {
            if (!getPartyReferenceService()
                .isValidQualificationType(education.getQualificationType())) {
              hibernateConstraintValidatorContext
                  .addMessageParameter("qualificationType", education.getQualificationType())
                  .buildConstraintViolationWithTemplate(
                      "{digital.inception.party.constraints.ValidPerson.invalidQualificationTypeForEducation.message}")
                  .addPropertyNode("educations")
                  .addPropertyNode("qualificationType")
                  .inIterable()
                  .addConstraintViolation();

              isValid = false;
            }
          }

          if (StringUtils.hasText(education.getFieldOfStudy())) {
            if (!getPartyReferenceService().isValidFieldOfStudy(education.getFieldOfStudy())) {
              hibernateConstraintValidatorContext
                  .addMessageParameter("fieldOfStudy", education.getFieldOfStudy())
                  .buildConstraintViolationWithTemplate(
                      "{digital.inception.party.constraints.ValidPerson.invalidFieldOfStudyForEducation.message}")
                  .addPropertyNode("educations")
                  .addPropertyNode("fieldOfStudy")
                  .inIterable()
                  .addConstraintViolation();

              isValid = false;
            }
          }
        }

        // Validate employment status
        if (StringUtils.hasText(person.getEmploymentStatus())
            && (!getPartyReferenceService()
                .isValidEmploymentStatus(person.getEmploymentStatus()))) {
          hibernateConstraintValidatorContext
              .addMessageParameter("employmentStatus", person.getEmploymentStatus())
              .buildConstraintViolationWithTemplate(
                  "{digital.inception.party.constraints.ValidPerson.invalidEmploymentStatus.message}")
              .addPropertyNode("employmentStatus")
              .addConstraintViolation();

          isValid = false;
        }

        // Validate employment type
        if (StringUtils.hasText(person.getEmploymentType())
            && (!getPartyReferenceService()
                .isValidEmploymentType(person.getEmploymentStatus(), person.getEmploymentType()))) {
          hibernateConstraintValidatorContext
              .addMessageParameter("employmentType", person.getEmploymentType())
              .buildConstraintViolationWithTemplate(
                  "{digital.inception.party.constraints.ValidPerson.invalidEmploymentType.message}")
              .addPropertyNode("employmentType")
              .addConstraintViolation();

          isValid = false;
        }

        // Validate employments
        for (Employment employment : person.getEmployments()) {
          if (StringUtils.hasText(employment.getEmployerAddressCountry())) {
            if (!getReferenceService().isValidCountry(employment.getEmployerAddressCountry())) {
              hibernateConstraintValidatorContext
                  .addMessageParameter(
                      "employerAddressCountry", employment.getEmployerAddressCountry())
                  .buildConstraintViolationWithTemplate(
                      "{digital.inception.party.constraints.ValidPerson.invalidEmployerAddressCountryForEmployment.message}")
                  .addPropertyNode("employments")
                  .addPropertyNode("employerAddressCountry")
                  .inIterable()
                  .addConstraintViolation();

              isValid = false;
            }
          }

          if (StringUtils.hasText(employment.getOccupation())) {
            if (!getPartyReferenceService().isValidOccupation(employment.getOccupation())) {
              hibernateConstraintValidatorContext
                  .addMessageParameter("occupation", employment.getOccupation())
                  .buildConstraintViolationWithTemplate(
                      "{digital.inception.party.constraints.ValidPerson.invalidOccupationForEmployment.message}")
                  .addPropertyNode("employments")
                  .addPropertyNode("occupation")
                  .inIterable()
                  .addConstraintViolation();

              isValid = false;
            }
          }

          if (StringUtils.hasText(employment.getType())) {
            if (!getPartyReferenceService().isValidEmploymentType(employment.getType())) {
              hibernateConstraintValidatorContext
                  .addMessageParameter("employmentType", employment.getType())
                  .buildConstraintViolationWithTemplate(
                      "{digital.inception.party.constraints.ValidPerson.invalidEmploymentTypeForEmployment.message}")
                  .addPropertyNode("employments")
                  .addPropertyNode("type")
                  .inIterable()
                  .addConstraintViolation();

              isValid = false;
            }
          }
        }

        // Validate gender
        if (StringUtils.hasText(person.getGender())
            && (!getPartyReferenceService().isValidGender(person.getGender()))) {
          hibernateConstraintValidatorContext
              .addMessageParameter("gender", person.getGender())
              .buildConstraintViolationWithTemplate(
                  "{digital.inception.party.constraints.ValidPerson.invalidGender.message}")
              .addPropertyNode("gender")
              .addConstraintViolation();

          isValid = false;
        }

        // Validate highest qualification type
        if (StringUtils.hasText(person.getHighestQualificationType())
            && (!getPartyReferenceService()
                .isValidQualificationType(person.getHighestQualificationType()))) {
          hibernateConstraintValidatorContext
              .addMessageParameter("highestQualificationType", person.getHighestQualificationType())
              .buildConstraintViolationWithTemplate(
                  "{digital.inception.party.constraints.ValidPerson.invalidHighestQualificationType.message}")
              .addPropertyNode("highestQualificationType")
              .addConstraintViolation();

          isValid = false;
        }

        // Validate identity documents
        for (IdentityDocument identityDocument : person.getIdentityDocuments()) {
          if (StringUtils.hasText(identityDocument.getCountryOfIssue())) {
            if (!getReferenceService().isValidCountry(identityDocument.getCountryOfIssue())) {
              hibernateConstraintValidatorContext
                  .addMessageParameter("countryOfIssue", identityDocument.getCountryOfIssue())
                  .buildConstraintViolationWithTemplate(
                      "{digital.inception.party.constraints.ValidPerson.invalidIdentityDocumentCountryOfIssue.message}")
                  .addPropertyNode("identityDocuments")
                  .addPropertyNode("countryOfIssue")
                  .inIterable()
                  .addConstraintViolation();

              isValid = false;
            }
          }

          if (StringUtils.hasText(identityDocument.getType())) {
            if (!getPartyReferenceService()
                .isValidIdentityDocumentType(person.getType().code(), identityDocument.getType())) {
              hibernateConstraintValidatorContext
                  .addMessageParameter("identityDocumentType", identityDocument.getType())
                  .addMessageParameter("partyType", person.getType().code())
                  .buildConstraintViolationWithTemplate(
                      "{digital.inception.party.constraints.ValidPerson.invalidIdentityDocumentTypeForPartyType.message}")
                  .addPropertyNode("identityDocuments")
                  .addPropertyNode("type")
                  .inIterable()
                  .addConstraintViolation();

              isValid = false;
            }
          }
        }

        // Validate language
        if (StringUtils.hasText(person.getLanguage())
            && (!getReferenceService().isValidLanguage(person.getLanguage()))) {
          hibernateConstraintValidatorContext
              .addMessageParameter("language", person.getLanguage())
              .buildConstraintViolationWithTemplate(
                  "{digital.inception.party.constraints.ValidPerson.invalidLanguage.message}")
              .addPropertyNode("language")
              .addConstraintViolation();

          isValid = false;
        }

        // Validate locks
        for (Lock lock : person.getLocks()) {
          if (StringUtils.hasText(lock.getType())) {
            if (!getPartyReferenceService()
                .isValidLockType(person.getType().code(), lock.getType())) {
              hibernateConstraintValidatorContext
                  .addMessageParameter("lockType", lock.getType())
                  .addMessageParameter("partyType", person.getType().code())
                  .buildConstraintViolationWithTemplate(
                      "{digital.inception.party.constraints.ValidPerson.invalidLockTypeForPartyType.message}")
                  .addPropertyNode("locks")
                  .addPropertyNode("type")
                  .inIterable()
                  .addConstraintViolation();

              isValid = false;
            }
          }
        }

        // Validate marital status
        if (StringUtils.hasText(person.getMaritalStatus())
            && (!getPartyReferenceService().isValidMaritalStatus(person.getMaritalStatus()))) {
          hibernateConstraintValidatorContext
              .addMessageParameter("maritalStatus", person.getMaritalStatus())
              .buildConstraintViolationWithTemplate(
                  "{digital.inception.party.constraints.ValidPerson.invalidMaritalStatus.message}")
              .addPropertyNode("maritalStatus")
              .addConstraintViolation();

          isValid = false;
        } else {
          // Validate marriage type
          if (!getPartyReferenceService()
              .isValidMarriageType(person.getMaritalStatus(), person.getMarriageType())) {
            hibernateConstraintValidatorContext
                .addMessageParameter("maritalStatus", person.getMaritalStatus())
                .addMessageParameter(
                    "marriageType",
                    StringUtils.hasText(person.getMarriageType()) ? person.getMarriageType() : "")
                .buildConstraintViolationWithTemplate(
                    "{digital.inception.party.constraints.ValidPerson.invalidMarriageType.message}")
                .addPropertyNode("marriageType")
                .addConstraintViolation();

            isValid = false;
          }
        }

        // Validate occupation
        if (StringUtils.hasText(person.getOccupation())
            && (!getPartyReferenceService().isValidOccupation(person.getOccupation()))) {
          hibernateConstraintValidatorContext
              .addMessageParameter("occupation", person.getOccupation())
              .buildConstraintViolationWithTemplate(
                  "{digital.inception.party.constraints.ValidPerson.invalidOccupation.message}")
              .addPropertyNode("occupation")
              .addConstraintViolation();

          isValid = false;
        }

        // Validate physical addresses
        for (PhysicalAddress physicalAddress : person.getPhysicalAddresses()) {
          if (StringUtils.hasText(physicalAddress.getRole())) {
            if (!getPartyReferenceService()
                .isValidPhysicalAddressRole(person.getType().code(), physicalAddress.getRole())) {
              hibernateConstraintValidatorContext
                  .addMessageParameter("physicalAddressRole", physicalAddress.getRole())
                  .addMessageParameter("partyType", person.getType().code())
                  .buildConstraintViolationWithTemplate(
                      "{digital.inception.party.constraints.ValidPerson.invalidPhysicalAddressRoleForPartyType.message}")
                  .addPropertyNode("physicalAddresses")
                  .addPropertyNode("role")
                  .inIterable()
                  .addConstraintViolation();

              isValid = false;
            }
          }

          for (String physicalAddressPurpose : physicalAddress.getPurposes()) {
            if (!getPartyReferenceService()
                .isValidPhysicalAddressPurpose(person.getType().code(), physicalAddressPurpose)) {
              hibernateConstraintValidatorContext
                  .addMessageParameter("physicalAddressPurpose", physicalAddressPurpose)
                  .addMessageParameter("partyType", person.getType().code())
                  .buildConstraintViolationWithTemplate(
                      "{digital.inception.party.constraints.ValidPerson.invalidPhysicalAddressPurposeForPartyType.message}")
                  .addPropertyNode("physicalAddresses")
                  .addPropertyNode("purposes")
                  .inIterable()
                  .addConstraintViolation();

              isValid = false;
            }
          }
        }

        // Validate preferences
        for (Preference preference : person.getPreferences()) {
          if (StringUtils.hasText(preference.getType())) {
            if (!getPartyReferenceService()
                .isValidPreferenceType(person.getType().code(), preference.getType())) {
              hibernateConstraintValidatorContext
                  .addMessageParameter("preferenceType", preference.getType())
                  .addMessageParameter("partyType", person.getType().code())
                  .buildConstraintViolationWithTemplate(
                      "{digital.inception.party.constraints.ValidPerson.invalidPreferenceTypeForPartyType.message}")
                  .addPropertyNode("preferences")
                  .addPropertyNode("type")
                  .inIterable()
                  .addConstraintViolation();

              isValid = false;
            }
          }
        }

        // Validate race
        if (StringUtils.hasText(person.getRace())
            && (!getPartyReferenceService().isValidRace(person.getRace()))) {
          hibernateConstraintValidatorContext
              .addMessageParameter("race", person.getRace())
              .buildConstraintViolationWithTemplate(
                  "{digital.inception.party.constraints.ValidPerson.invalidRace.message}")
              .addPropertyNode("race")
              .addConstraintViolation();

          isValid = false;
        }

        // Validate residency status
        if (StringUtils.hasText(person.getResidencyStatus())
            && (!getPartyReferenceService().isValidResidencyStatus(person.getResidencyStatus()))) {
          hibernateConstraintValidatorContext
              .addMessageParameter("residencyStatus", person.getResidencyStatus())
              .buildConstraintViolationWithTemplate(
                  "{digital.inception.party.constraints.ValidPerson.invalidResidencyStatus.message}")
              .addPropertyNode("residencyStatus")
              .addConstraintViolation();

          isValid = false;
        }

        // Validate residence permits
        for (ResidencePermit residencePermit : person.getResidencePermits()) {
          if (StringUtils.hasText(residencePermit.getCountryOfIssue())) {
            if (!getReferenceService().isValidCountry(residencePermit.getCountryOfIssue())) {
              hibernateConstraintValidatorContext
                  .addMessageParameter("countryOfIssue", residencePermit.getCountryOfIssue())
                  .buildConstraintViolationWithTemplate(
                      "{digital.inception.party.constraints.ValidPerson.invalidResidencePermitCountryOfIssue.message}")
                  .addPropertyNode("residencePermits")
                  .addPropertyNode("countryOfIssue")
                  .inIterable()
                  .addConstraintViolation();

              isValid = false;
            }
          }

          if (StringUtils.hasText(residencePermit.getType())) {
            if (!getPartyReferenceService().isValidResidencePermitType(residencePermit.getType())) {
              hibernateConstraintValidatorContext
                  .addMessageParameter("residencePermitType", residencePermit.getType())
                  .buildConstraintViolationWithTemplate(
                      "{digital.inception.party.constraints.ValidPerson.invalidResidencePermitType.message}")
                  .addPropertyNode("residencePermits")
                  .addPropertyNode("type")
                  .inIterable()
                  .addConstraintViolation();

              isValid = false;
            }
          }
        }

        // Validate residential type
        if (StringUtils.hasText(person.getResidentialType())
            && (!getPartyReferenceService().isValidResidentialType(person.getResidentialType()))) {
          hibernateConstraintValidatorContext
              .addMessageParameter("residentialType", person.getResidentialType())
              .buildConstraintViolationWithTemplate(
                  "{digital.inception.party.constraints.ValidPerson.invalidResidentialType.message}")
              .addPropertyNode("residentialType")
              .addConstraintViolation();

          isValid = false;
        }

        // Validate roles
        for (Role role : person.getRoles()) {
          if (StringUtils.hasText(role.getType())) {
            if (!getPartyReferenceService()
                .isValidRoleType(person.getType().code(), role.getType())) {
              hibernateConstraintValidatorContext
                  .addMessageParameter("roleType", role.getType())
                  .addMessageParameter("partyType", person.getType().code())
                  .buildConstraintViolationWithTemplate(
                      "{digital.inception.party.constraints.ValidPerson.invalidRoleTypeForPartyType.message}")
                  .addPropertyNode("roles")
                  .addPropertyNode("type")
                  .inIterable()
                  .addConstraintViolation();

              isValid = false;
            } else {
              if (!validateRoleTypeAttributeTypeConstraintsForPersonWithRole(
                  person, role.getType(), hibernateConstraintValidatorContext)) {
                isValid = false;
              }

              if (!validateRoleTypePreferenceTypeConstraintsForPersonWithRole(
                  person, role.getType(), hibernateConstraintValidatorContext)) {
                isValid = false;
              }
            }
          }
        }

        // Validate sources of funds
        for (SourceOfFunds sourceOfFunds : person.getSourcesOfFunds()) {
          if (StringUtils.hasText(sourceOfFunds.getType())) {
            if (!getPartyReferenceService().isValidSourceOfFundsType(sourceOfFunds.getType())) {
              hibernateConstraintValidatorContext
                  .addMessageParameter("sourceOfFundsType", sourceOfFunds.getType())
                  .buildConstraintViolationWithTemplate(
                      "{digital.inception.party.constraints.ValidPerson.invalidSourceOfFundsType.message}")
                  .addPropertyNode("sourcesOfFunds")
                  .addPropertyNode("type")
                  .inIterable()
                  .addConstraintViolation();

              isValid = false;
            }
          }
        }

        // Validate sources of wealth
        for (SourceOfWealth sourceOfWealth : person.getSourcesOfWealth()) {
          if (StringUtils.hasText(sourceOfWealth.getType())) {
            if (!getPartyReferenceService().isValidSourceOfWealthType(sourceOfWealth.getType())) {
              hibernateConstraintValidatorContext
                  .addMessageParameter("sourceOfWealthType", sourceOfWealth.getType())
                  .buildConstraintViolationWithTemplate(
                      "{digital.inception.party.constraints.ValidPerson.invalidSourceOfWealthType.message}")
                  .addPropertyNode("sourcesOfWealth")
                  .addPropertyNode("type")
                  .inIterable()
                  .addConstraintViolation();

              isValid = false;
            }
          }
        }

        // Validate statuses
        for (Status status : person.getStatuses()) {
          if (StringUtils.hasText(status.getType())) {
            if (!getPartyReferenceService()
                .isValidStatusType(person.getType().code(), status.getType())) {
              hibernateConstraintValidatorContext
                  .addMessageParameter("statusType", status.getType())
                  .addMessageParameter("partyType", person.getType().code())
                  .buildConstraintViolationWithTemplate(
                      "{digital.inception.party.constraints.ValidPerson.invalidStatusTypeForPartyType.message}")
                  .addPropertyNode("statuses")
                  .addPropertyNode("type")
                  .inIterable()
                  .addConstraintViolation();

              isValid = false;
            }
          }
        }

        // Validate tax numbers
        for (TaxNumber taxNumber : person.getTaxNumbers()) {
          if (StringUtils.hasText(taxNumber.getCountryOfIssue())) {
            if (!getReferenceService().isValidCountry(taxNumber.getCountryOfIssue())) {
              hibernateConstraintValidatorContext
                  .addMessageParameter("countryOfIssue", taxNumber.getCountryOfIssue())
                  .buildConstraintViolationWithTemplate(
                      "{digital.inception.party.constraints.ValidPerson.invalidTaxNumberCountryOfIssue.message}")
                  .addPropertyNode("taxNumbers")
                  .addPropertyNode("countryOfIssue")
                  .inIterable()
                  .addConstraintViolation();

              isValid = false;
            }
          }

          if (StringUtils.hasText(taxNumber.getType())) {
            if (!getPartyReferenceService()
                .isValidTaxNumberType(person.getType().code(), taxNumber.getType())) {
              hibernateConstraintValidatorContext
                  .addMessageParameter("taxNumberType", taxNumber.getType())
                  .addMessageParameter("partyType", person.getType().code())
                  .buildConstraintViolationWithTemplate(
                      "{digital.inception.party.constraints.ValidPerson.invalidTaxNumberTypeForPartyType.message}")
                  .addPropertyNode("taxNumbers")
                  .addPropertyNode("type")
                  .inIterable()
                  .addConstraintViolation();

              isValid = false;
            }
          }
        }

        // Validate title
        if (StringUtils.hasText(person.getTitle())
            && (!getPartyReferenceService().isValidTitle(person.getTitle()))) {
          hibernateConstraintValidatorContext
              .addMessageParameter("title", person.getTitle())
              .buildConstraintViolationWithTemplate(
                  "{digital.inception.party.constraints.ValidPerson.invalidTitle.message}")
              .addPropertyNode("title")
              .addConstraintViolation();

          isValid = false;
        }
      } catch (Throwable e) {
        throw new ValidationException("Failed to validate the person", e);
      }

      return isValid;
    } else {
      return true;
    }
  }

  private boolean validateRoleTypeAttributeTypeConstraintsForPersonWithRole(
      Person person,
      String roleType,
      HibernateConstraintValidatorContext hibernateConstraintValidatorContext)
      throws Exception {
    boolean isValid = true;

    for (RoleTypeAttributeTypeConstraint roleTypeAttributeTypeConstraint :
        getPartyReferenceService().getRoleTypeAttributeTypeConstraints(roleType)) {
      try {
        if (roleTypeAttributeTypeConstraint.getType() == ConstraintType.MAX_SIZE) {
          Optional<Attribute> attributeOptional =
              person.getAttributeWithType(roleTypeAttributeTypeConstraint.getAttributeType());

          if (attributeOptional.isPresent()) {
            if (!validateMaximumSizeAttributeConstraint(
                roleTypeAttributeTypeConstraint.getIntegerValue(),
                attributeOptional.get(),
                hibernateConstraintValidatorContext)) {
              isValid = false;
            }
          }
        } else if (roleTypeAttributeTypeConstraint.getType() == ConstraintType.MIN_SIZE) {
          Optional<Attribute> attributeOptional =
              person.getAttributeWithType(roleTypeAttributeTypeConstraint.getAttributeType());

          if (attributeOptional.isPresent()) {
            if (!validateMinimumSizeAttributeConstraint(
                roleTypeAttributeTypeConstraint.getIntegerValue(),
                attributeOptional.get(),
                hibernateConstraintValidatorContext)) {
              isValid = false;
            }
          }
        } else if (roleTypeAttributeTypeConstraint.getType() == ConstraintType.PATTERN) {
          Optional<Attribute> attributeOptional =
              person.getAttributeWithType(roleTypeAttributeTypeConstraint.getAttributeType());

          if (attributeOptional.isPresent()) {
            if (!validatePatternAttributeConstraint(
                roleTypeAttributeTypeConstraint.getValue(),
                attributeOptional.get(),
                hibernateConstraintValidatorContext)) {
              isValid = false;
            }
          }
        } else if (roleTypeAttributeTypeConstraint.getType() == ConstraintType.REFERENCE) {
          Optional<Attribute> attributeOptional =
              person.getAttributeWithType(roleTypeAttributeTypeConstraint.getAttributeType());

          if (attributeOptional.isPresent()) {
            if (!validateReferenceAttributeConstraint(
                roleTypeAttributeTypeConstraint.getValue(),
                attributeOptional.get(),
                hibernateConstraintValidatorContext)) {
              isValid = false;
            }
          }
        } else if (roleTypeAttributeTypeConstraint.getType() == ConstraintType.REQUIRED) {
          switch (roleTypeAttributeTypeConstraint.getAttributeType()) {
            case "contact_mechanism":
              if (!person.hasContactMechanismWithType(
                  roleTypeAttributeTypeConstraint.getAttributeTypeQualifier())) {
                hibernateConstraintValidatorContext
                    .addMessageParameter(
                        "contactMechanismType",
                        roleTypeAttributeTypeConstraint.getAttributeTypeQualifier())
                    .addMessageParameter("roleType", roleType)
                    .buildConstraintViolationWithTemplate(
                        "{digital.inception.party.constraints.ValidPerson.contactMechanismTypeRequiredForRoleType.message}")
                    .addPropertyNode("contactMechanisms")
                    .addConstraintViolation();

                isValid = false;
              }

              break;

            case "contact_mechanisms":
              if (!validateRequiredAttributeConstraint(
                  roleType,
                  person.getContactMechanisms(),
                  "contactMechanisms",
                  "{digital.inception.party.constraints.ValidPerson.contactMechanismRequiredForRoleType.message}",
                  hibernateConstraintValidatorContext)) {
                isValid = false;
              }

              break;

            case "countries_of_citizenship":
              if (!validateRequiredAttributeConstraint(
                  roleType,
                  person.getCountriesOfCitizenship(),
                  "countriesOfCitizenship",
                  "{digital.inception.party.constraints.ValidPerson.countryOfCitizenshipRequiredForRoleType.message}",
                  hibernateConstraintValidatorContext)) {
                isValid = false;
              }

              break;

            case "countries_of_tax_residence":
              if (!validateRequiredAttributeConstraint(
                  roleType,
                  person.getCountriesOfTaxResidence(),
                  "countriesOfTaxResidence",
                  "{digital.inception.party.constraints.ValidPerson.countryOfTaxResidenceRequiredForRoleType.message}",
                  hibernateConstraintValidatorContext)) {
                isValid = false;
              }

              break;

            case "country_of_birth":
              if (!validateRequiredAttributeConstraint(
                  roleType,
                  person.getCountryOfBirth(),
                  "countryOfBirth",
                  "{digital.inception.party.constraints.ValidPerson.countryOfBirthRequiredForRoleType.message}",
                  hibernateConstraintValidatorContext)) {
                isValid = false;
              }

              break;

            case "country_of_residence":
              if (!validateRequiredAttributeConstraint(
                  roleType,
                  person.getCountryOfResidence(),
                  "countryOfResidence",
                  "{digital.inception.party.constraints.ValidPerson.countryOfResidenceRequiredForRoleType.message}",
                  hibernateConstraintValidatorContext)) {
                isValid = false;
              }

              break;

            case "date_of_birth":
              if (!validateRequiredAttributeConstraint(
                  roleType,
                  person.getDateOfBirth(),
                  "dateOfBirth",
                  "{digital.inception.party.constraints.ValidPerson.dateOfBirthRequiredForRoleType.message}",
                  hibernateConstraintValidatorContext)) {
                isValid = false;
              }

              break;

            case "educations":
              if (!validateRequiredAttributeConstraint(
                  roleType,
                  person.getEducations(),
                  "educations",
                  "{digital.inception.party.constraints.ValidPerson.educationRequiredForRoleType.message}",
                  hibernateConstraintValidatorContext)) {
                isValid = false;
              }

              break;

            case "employments":
              if (!validateRequiredAttributeConstraint(
                  roleType,
                  person.getEmployments(),
                  "employments",
                  "{digital.inception.party.constraints.ValidPerson.employmentRequiredForRoleType.message}",
                  hibernateConstraintValidatorContext)) {
                isValid = false;
              }

              break;

            case "employment_status":
              if (!validateRequiredAttributeConstraint(
                  roleType,
                  person.getEmploymentStatus(),
                  "employmentStatus",
                  "{digital.inception.party.constraints.ValidPerson.employmentStatusRequiredForRoleType.message}",
                  hibernateConstraintValidatorContext)) {
                isValid = false;
              }

              break;

            case "employment_type":
              if (!validateRequiredAttributeConstraint(
                  roleType,
                  person.getEmploymentType(),
                  "employmentType",
                  "{digital.inception.party.constraints.ValidPerson.employmentTypeRequiredForRoleType.message}",
                  hibernateConstraintValidatorContext)) {
                isValid = false;
              }

              break;

            case "gender":
              if (!validateRequiredAttributeConstraint(
                  roleType,
                  person.getGender(),
                  "gender",
                  "{digital.inception.party.constraints.ValidPerson.genderRequiredForRoleType.message}",
                  hibernateConstraintValidatorContext)) {
                isValid = false;
              }

              break;

            case "given_name":
              if (!validateRequiredAttributeConstraint(
                  roleType,
                  person.getGivenName(),
                  "givenName",
                  "{digital.inception.party.constraints.ValidPerson.givenNameRequiredForRoleType.message}",
                  hibernateConstraintValidatorContext)) {
                isValid = false;
              }

              break;

            case "highest_qualification_type":
              if (!validateRequiredAttributeConstraint(
                  roleType,
                  person.getHighestQualificationType(),
                  "highestQualificationType",
                  "{digital.inception.party.constraints.ValidPerson.highestQualificationTypeRequiredForRoleType.message}",
                  hibernateConstraintValidatorContext)) {
                isValid = false;
              }

              break;

            case "identity_documents":
              if (!validateRequiredAttributeConstraint(
                  roleType,
                  person.getIdentityDocuments(),
                  "identityDocuments",
                  "{digital.inception.party.constraints.ValidPerson.identityDocumentRequiredForRoleType.message}",
                  hibernateConstraintValidatorContext)) {
                isValid = false;
              }

              break;

            case "initials":
              if (!validateRequiredAttributeConstraint(
                  roleType,
                  person.getInitials(),
                  "initials",
                  "{digital.inception.party.constraints.ValidPerson.initialsRequiredForRoleType.message}",
                  hibernateConstraintValidatorContext)) {
                isValid = false;
              }

              break;

            case "language":
              if (!validateRequiredAttributeConstraint(
                  roleType,
                  person.getLanguage(),
                  "language",
                  "{digital.inception.party.constraints.ValidPerson.languageRequiredForRoleType.message}",
                  hibernateConstraintValidatorContext)) {
                isValid = false;
              }

              break;

            case "marital_status":
              if (!validateRequiredAttributeConstraint(
                  roleType,
                  person.getMaritalStatus(),
                  "maritalStatus",
                  "{digital.inception.party.constraints.ValidPerson.maritalStatusRequiredForRoleType.message}",
                  hibernateConstraintValidatorContext)) {
                isValid = false;
              }

              break;

            case "marital_status_date":
              if (!validateRequiredAttributeConstraint(
                  roleType,
                  person.getMaritalStatusDate(),
                  "maritalStatusDate",
                  "{digital.inception.party.constraints.ValidPerson.maritalStatusDateRequiredForRoleType.message}",
                  hibernateConstraintValidatorContext)) {
                isValid = false;
              }

              break;

            case "marriage_type":
              if (!validateRequiredAttributeConstraint(
                  roleType,
                  person.getMarriageType(),
                  "marriageType",
                  "{digital.inception.party.constraints.ValidPerson.marriageTypeRequiredForRoleType.message}",
                  hibernateConstraintValidatorContext)) {
                isValid = false;
              }

              break;

            case "occupation":
              if (!validateRequiredAttributeConstraint(
                  roleType,
                  person.getOccupation(),
                  "occupation",
                  "{digital.inception.party.constraints.ValidPerson.occupationRequiredForRoleType.message}",
                  hibernateConstraintValidatorContext)) {
                isValid = false;
              }

              break;

            case "physical_addresses":
              if (!validateRequiredAttributeConstraint(
                  roleType,
                  person.getPhysicalAddresses(),
                  "physicalAddresses",
                  "{digital.inception.party.constraints.ValidPerson.physicalAddressRequiredForRoleType.message}",
                  hibernateConstraintValidatorContext)) {
                isValid = false;
              }

              break;

            case "physical_address":
              if (!person.hasPhysicalAddressWithRole(
                  roleTypeAttributeTypeConstraint.getAttributeTypeQualifier())) {
                hibernateConstraintValidatorContext
                    .addMessageParameter(
                        "physicalAddressRole",
                        roleTypeAttributeTypeConstraint.getAttributeTypeQualifier())
                    .addMessageParameter("roleType", roleType)
                    .buildConstraintViolationWithTemplate(
                        "{digital.inception.party.constraints.ValidPerson.physicalAddressRoleRequiredForRoleType.message}")
                    .addPropertyNode("physicalAddresses")
                    .addPropertyNode("role")
                    .inIterable()
                    .addConstraintViolation();

                isValid = false;
              }

              break;

            case "preferred_name":
              if (!validateRequiredAttributeConstraint(
                  roleType,
                  person.getPreferredName(),
                  "preferredName",
                  "{digital.inception.party.constraints.ValidPerson.preferredNameRequiredForRoleType.message}",
                  hibernateConstraintValidatorContext)) {
                isValid = false;
              }

              break;

            case "race":
              if (!validateRequiredAttributeConstraint(
                  roleType,
                  person.getRace(),
                  "race",
                  "{digital.inception.party.constraints.ValidPerson.raceRequiredForRoleType.message}",
                  hibernateConstraintValidatorContext)) {
                isValid = false;
              }

              break;

            case "residency_status":
              if (!validateRequiredAttributeConstraint(
                  roleType,
                  person.getResidencyStatus(),
                  "residencyStatus",
                  "{digital.inception.party.constraints.ValidPerson.residencyStatusRequiredForRoleType.message}",
                  hibernateConstraintValidatorContext)) {
                isValid = false;
              }

              break;

            case "residential_type":
              if (!validateRequiredAttributeConstraint(
                  roleType,
                  person.getResidentialType(),
                  "residentialType",
                  "{digital.inception.party.constraints.ValidPerson.residentialTypeRequiredForRoleType.message}",
                  hibernateConstraintValidatorContext)) {
                isValid = false;
              }

              break;

            case "sources_of_funds":
              if (!validateRequiredAttributeConstraint(
                  roleType,
                  person.getSourcesOfFunds(),
                  "sourcesOfFunds",
                  "{digital.inception.party.constraints.ValidPerson.sourceOfFundsRequiredForRoleType.message}",
                  hibernateConstraintValidatorContext)) {
                isValid = false;
              }

              break;

            case "sources_of_wealth":
              if (!validateRequiredAttributeConstraint(
                  roleType,
                  person.getSourcesOfWealth(),
                  "sourcesOfWealth",
                  "{digital.inception.party.constraints.ValidPerson.sourceOfWealthRequiredForRoleType.message}",
                  hibernateConstraintValidatorContext)) {
                isValid = false;
              }

              break;

            case "surname":
              if (!validateRequiredAttributeConstraint(
                  roleType,
                  person.getSurname(),
                  "surname",
                  "{digital.inception.party.constraints.ValidPerson.surnameRequiredForRoleType.message}",
                  hibernateConstraintValidatorContext)) {
                isValid = false;
              }

              break;

            case "tax_numbers":
              if (!validateRequiredAttributeConstraint(
                  roleType,
                  person.getTaxNumbers(),
                  "taxNumbers",
                  "{digital.inception.party.constraints.ValidPerson.taxNumberRequiredForRoleType.message}",
                  hibernateConstraintValidatorContext)) {
                isValid = false;
              }

              break;

            case "title":
              if (!validateRequiredAttributeConstraint(
                  roleType,
                  person.getTitle(),
                  "title",
                  "{digital.inception.party.constraints.ValidPerson.titleRequiredForRoleType.message}",
                  hibernateConstraintValidatorContext)) {
                isValid = false;
              }
              break;

            default:
              Optional<Attribute> attributeOptional =
                  person.getAttributeWithType(roleTypeAttributeTypeConstraint.getAttributeType());

              if (attributeOptional.isEmpty() || (!attributeOptional.get().hasValue())) {
                hibernateConstraintValidatorContext
                    .addMessageParameter(
                        "attributeType", roleTypeAttributeTypeConstraint.getAttributeType())
                    .addMessageParameter("roleType", roleType)
                    .buildConstraintViolationWithTemplate(
                        "{digital.inception.party.constraints.ValidParty.attributeTypeRequiredForRoleType.message}")
                    .addPropertyNode("attributes")
                    .addPropertyNode("type")
                    .inIterable()
                    .addConstraintViolation();

                isValid = false;
              }
              break;
          }
        } else if (roleTypeAttributeTypeConstraint.getType() == ConstraintType.SIZE) {
          Optional<Attribute> attributeOptional =
              person.getAttributeWithType(roleTypeAttributeTypeConstraint.getAttributeType());

          if (attributeOptional.isPresent()) {
            if (!validateSizeAttributeConstraint(
                roleTypeAttributeTypeConstraint.getIntegerValue(),
                attributeOptional.get(),
                hibernateConstraintValidatorContext)) {
              isValid = false;
            }
          }
        }
      } catch (Throwable e) {
        throw new ValidationException(
            "Failed to validate the role attribute constraint of type ("
                + roleTypeAttributeTypeConstraint.getType()
                + ") for the role type  ("
                + roleTypeAttributeTypeConstraint.getRoleType()
                + ") and attribute type ("
                + roleTypeAttributeTypeConstraint.getAttributeType()
                + ") with the attribute type qualifier ("
                + roleTypeAttributeTypeConstraint.getAttributeTypeQualifier()
                + ") and value ("
                + roleTypeAttributeTypeConstraint.getValue()
                + ")",
            e);
      }
    }

    return isValid;
  }

  private boolean validateRoleTypePreferenceTypeConstraintsForPersonWithRole(
      Person person,
      String roleType,
      HibernateConstraintValidatorContext hibernateConstraintValidatorContext)
      throws Exception {
    boolean isValid = true;

    for (RoleTypePreferenceTypeConstraint roleTypePreferenceTypeConstraint :
        getPartyReferenceService().getRoleTypePreferenceTypeConstraints(roleType)) {
      try {
        if (roleTypePreferenceTypeConstraint.getType() == ConstraintType.MAX_SIZE) {
          Optional<Preference> preferenceOptional =
              person.getPreferenceWithType(roleTypePreferenceTypeConstraint.getPreferenceType());

          if (preferenceOptional.isPresent()) {
            if (!validateMaximumSizePreferenceConstraint(
                roleTypePreferenceTypeConstraint.getIntegerValue(),
                preferenceOptional.get(),
                hibernateConstraintValidatorContext)) {
              isValid = false;
            }
          }
        } else if (roleTypePreferenceTypeConstraint.getType() == ConstraintType.MIN_SIZE) {
          Optional<Preference> preferenceOptional =
              person.getPreferenceWithType(roleTypePreferenceTypeConstraint.getPreferenceType());

          if (preferenceOptional.isPresent()) {
            if (!validateMinimumSizePreferenceConstraint(
                roleTypePreferenceTypeConstraint.getIntegerValue(),
                preferenceOptional.get(),
                hibernateConstraintValidatorContext)) {
              isValid = false;
            }
          }
        } else if (roleTypePreferenceTypeConstraint.getType() == ConstraintType.PATTERN) {
          Optional<Preference> preferenceOptional =
              person.getPreferenceWithType(roleTypePreferenceTypeConstraint.getPreferenceType());

          if (preferenceOptional.isPresent()) {
            if (!validatePatternPreferenceConstraint(
                roleTypePreferenceTypeConstraint.getValue(),
                preferenceOptional.get(),
                hibernateConstraintValidatorContext)) {
              isValid = false;
            }
          }
        } else if (roleTypePreferenceTypeConstraint.getType() == ConstraintType.REFERENCE) {
          Optional<Preference> preferenceOptional =
              person.getPreferenceWithType(roleTypePreferenceTypeConstraint.getPreferenceType());

          if (preferenceOptional.isPresent()) {
            if (!validateReferencePreferenceConstraint(
                roleTypePreferenceTypeConstraint.getValue(),
                preferenceOptional.get(),
                hibernateConstraintValidatorContext)) {
              isValid = false;
            }
          }
        } else if (roleTypePreferenceTypeConstraint.getType() == ConstraintType.REQUIRED) {
          Optional<Preference> preferenceOptional =
              person.getPreferenceWithType(roleTypePreferenceTypeConstraint.getPreferenceType());

          if (preferenceOptional.isEmpty()
              || (!StringUtils.hasText(preferenceOptional.get().getValue()))) {
            hibernateConstraintValidatorContext
                .addMessageParameter(
                    "preferenceType", roleTypePreferenceTypeConstraint.getPreferenceType())
                .addMessageParameter("roleType", roleType)
                .buildConstraintViolationWithTemplate(
                    "{digital.inception.party.constraints.ValidParty.preferenceTypeRequiredForRoleType.message}")
                .addPropertyNode("preferences")
                .addPropertyNode("type")
                .inIterable()
                .addConstraintViolation();

            isValid = false;
          }
        } else if (roleTypePreferenceTypeConstraint.getType() == ConstraintType.SIZE) {
          Optional<Preference> preferenceOptional =
              person.getPreferenceWithType(roleTypePreferenceTypeConstraint.getPreferenceType());

          if (preferenceOptional.isPresent()) {
            if (!validateSizePreferenceConstraint(
                roleTypePreferenceTypeConstraint.getIntegerValue(),
                preferenceOptional.get(),
                hibernateConstraintValidatorContext)) {
              isValid = false;
            }
          }
        }
      } catch (Throwable e) {
        throw new ValidationException(
            "Failed to validate the role preference constraint of type ("
                + roleTypePreferenceTypeConstraint.getType()
                + ") for the role type  ("
                + roleTypePreferenceTypeConstraint.getRoleType()
                + ") and preference type ("
                + roleTypePreferenceTypeConstraint.getPreferenceType()
                + ") and value ("
                + roleTypePreferenceTypeConstraint.getValue()
                + ")",
            e);
      }
    }

    return isValid;
  }
}
