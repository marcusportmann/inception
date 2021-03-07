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
import digital.inception.party.AttributeConstraintType;
import digital.inception.party.ContactMechanism;
import digital.inception.party.IPartyReferenceService;
import digital.inception.party.IdentityDocument;
import digital.inception.party.Person;
import digital.inception.party.PhysicalAddress;
import digital.inception.party.Preference;
import digital.inception.party.Role;
import digital.inception.party.RoleTypeAttributeConstraint;
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
 * <p>NOTE: The <b>@Autowired</b> constructor is not used when Spring Boot automatically invokes the
 * JSR 380 validation when persisting an entity. Instead, the default constructor is invoked when
 * the validator is initialized by Hibernate. As a result the custom validation that requires the
 * injected services will not be executed.
 *
 * @author Marcus Portmann
 */
public class ValidPersonValidator extends PartyValidator
    implements ConstraintValidator<ValidPerson, Person> {

  /** The Party Reference Service. */
  private final IPartyReferenceService partyReferenceService;

  /** The Reference Service. */
  private final IReferenceService referenceService;

  /**
   * Constructs a new <b>ValidPersonValidator</b>.
   *
   * @param partyReferenceService the Party Reference Service
   * @param referenceService the Reference Service
   */
  @Autowired
  public ValidPersonValidator(
      IPartyReferenceService partyReferenceService, IReferenceService referenceService) {
    this.partyReferenceService = partyReferenceService;
    this.referenceService = referenceService;
  }

  /** Constructs a new <b>ValidPersonValidator</b>. */
  public ValidPersonValidator() {
    this.partyReferenceService = null;
    this.referenceService = null;
  }

  @Override
  public void initialize(ValidPerson constraintAnnotation) {}

  @Override
  public boolean isValid(Person person, ConstraintValidatorContext constraintValidatorContext) {

    if ((partyReferenceService != null) && (referenceService != null)) {
      boolean isValid = true;

      // Disable the default constraint violation
      constraintValidatorContext.disableDefaultConstraintViolation();

      HibernateConstraintValidatorContext hibernateConstraintValidatorContext =
          constraintValidatorContext.unwrap(HibernateConstraintValidatorContext.class);

      try {
        // Validate contact mechanisms
        for (ContactMechanism contactMechanism : person.getContactMechanisms()) {
          if (!partyReferenceService.isValidContactMechanismType(contactMechanism.getType())) {
            hibernateConstraintValidatorContext
                .addMessageParameter("contactMechanismType", contactMechanism.getType())
                .addMessageParameter("contactMechanismPurpose", contactMechanism.getPurpose())
                .buildConstraintViolationWithTemplate(
                    "{digital.inception.party.constraints.ValidPerson.invalidContactMechanismPurposeCode.message}")
                .addConstraintViolation();

            isValid = false;
          }

          if (!partyReferenceService.isValidContactMechanismPurpose(
              person.getType().code(), contactMechanism.getType(), contactMechanism.getPurpose())) {
            hibernateConstraintValidatorContext
                .addMessageParameter("contactMechanismPurpose", contactMechanism.getPurpose())
                .addMessageParameter("partyType", person.getType().code())
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
            && (!partyReferenceService.isValidEmploymentStatus(person.getEmploymentStatus()))) {
          hibernateConstraintValidatorContext
              .addMessageParameter("employmentStatus", person.getEmploymentStatus())
              .buildConstraintViolationWithTemplate(
                  "{digital.inception.party.constraints.ValidPerson.invalidEmploymentStatusCode.message}")
              .addConstraintViolation();

          isValid = false;
        }

        // Validate employment type
        if (StringUtils.hasText(person.getEmploymentType())
            && (!partyReferenceService.isValidEmploymentType(
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
            && (!partyReferenceService.isValidGender(person.getGender()))) {
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

          if (!partyReferenceService.isValidIdentityDocumentType(
              person.getType().code(), identityDocument.getType())) {
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
            && (!partyReferenceService.isValidMaritalStatus(person.getMaritalStatus()))) {
          hibernateConstraintValidatorContext
              .addMessageParameter("maritalStatus", person.getMaritalStatus())
              .buildConstraintViolationWithTemplate(
                  "{digital.inception.party.constraints.ValidPerson.invalidMaritalStatusCode.message}")
              .addConstraintViolation();

          isValid = false;
        }

        // Validate marriage type
        if (!partyReferenceService.isValidMarriageType(
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
            && (!partyReferenceService.isValidOccupation(person.getOccupation()))) {
          hibernateConstraintValidatorContext
              .addMessageParameter("occupation", person.getOccupation())
              .buildConstraintViolationWithTemplate(
                  "{digital.inception.party.constraints.ValidPerson.invalidOccupationCode.message}")
              .addConstraintViolation();

          isValid = false;
        }

        // Validate roles
        for (Role role : person.getRoles()) {
          if (!partyReferenceService.isValidRoleType(person.getType().code(), role.getType())) {
            hibernateConstraintValidatorContext
                .addMessageParameter("type", role.getType())
                .buildConstraintViolationWithTemplate(
                    "{digital.inception.party.constraints.ValidPerson.invalidRoleTypeCode.message}")
                .addConstraintViolation();

            isValid = false;
          }
        }

        // Validate physical addresses
        for (PhysicalAddress physicalAddress : person.getPhysicalAddresses()) {
          if (!partyReferenceService.isValidPhysicalAddressRole(
              person.getType().code(), physicalAddress.getRole())) {
            hibernateConstraintValidatorContext
                .addMessageParameter("physicalAddressRole", physicalAddress.getRole())
                .addMessageParameter("partyType", person.getType().code())
                .buildConstraintViolationWithTemplate(
                    "{digital.inception.party.constraints.ValidPerson.invalidPhysicalAddressRoleCodeForPartyType.message}")
                .addConstraintViolation();

            isValid = false;
          }

          for (String physicalAddressPurpose : physicalAddress.getPurposes()) {
            if (!partyReferenceService.isValidPhysicalAddressPurpose(
                person.getType().code(), physicalAddressPurpose)) {
              hibernateConstraintValidatorContext
                  .addMessageParameter("physicalAddressPurpose", physicalAddressPurpose)
                  .addMessageParameter("partyType", person.getType().code())
                  .buildConstraintViolationWithTemplate(
                      "{digital.inception.party.constraints.ValidPerson.invalidPhysicalAddressPurposeCodeForPartyType.message}")
                  .addConstraintViolation();

              isValid = false;
            }
          }
        }

        // Validate attributes
        for (Attribute attribute : person.getAttributes()) {
          for (String reservedAttributeTypeCode : Attribute.RESERVED_ATTRIBUTE_TYPE_CODES) {
            if (reservedAttributeTypeCode.equalsIgnoreCase(attribute.getType())) {
              hibernateConstraintValidatorContext
                  .addMessageParameter("attributeType", attribute.getType())
                  .buildConstraintViolationWithTemplate(
                      "{digital.inception.party.constraints.ValidPerson.invalidReservedAttributeTypeCode.message}")
                  .addConstraintViolation();

              isValid = false;
            }
          }
        }

        // Validate preferences
        for (Preference preference : person.getPreferences()) {
          if (!partyReferenceService.isValidPreferenceType(
              person.getType().code(), preference.getType())) {
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
            && (!partyReferenceService.isValidRace(person.getRace()))) {
          hibernateConstraintValidatorContext
              .addMessageParameter("race", person.getRace())
              .buildConstraintViolationWithTemplate(
                  "{digital.inception.party.constraints.ValidPerson.invalidRaceCode.message}")
              .addConstraintViolation();

          isValid = false;
        }

        // Validate residency status
        if (StringUtils.hasText(person.getResidencyStatus())
            && (!partyReferenceService.isValidResidencyStatus(person.getResidencyStatus()))) {
          hibernateConstraintValidatorContext
              .addMessageParameter("residencyStatus", person.getResidencyStatus())
              .buildConstraintViolationWithTemplate(
                  "{digital.inception.party.constraints.ValidPerson.invalidResidencyStatusCode.message}")
              .addConstraintViolation();

          isValid = false;
        }

        // Validate residential type
        if (StringUtils.hasText(person.getResidentialType())
            && (!partyReferenceService.isValidResidentialType(person.getResidentialType()))) {
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

          if (!partyReferenceService.isValidTaxNumberType(taxNumber.getType())) {
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
            && (!partyReferenceService.isValidTitle(person.getTitle()))) {
          hibernateConstraintValidatorContext
              .addMessageParameter("title", person.getTitle())
              .buildConstraintViolationWithTemplate(
                  "{digital.inception.party.constraints.ValidPerson.invalidTitleCode.message}")
              .addConstraintViolation();

          isValid = false;
        }

        for (Role role : person.getRoles()) {
          if (!validatePersonWithRole(
              person, role.getType(), hibernateConstraintValidatorContext)) {
            isValid = false;
          }
        }

      } catch (Throwable e) {
        throw new ValidationException("Failed to validate the person", e);
      }

      return isValid;
    } else {
      return true;
    }
  }

  private boolean validatePersonWithRole(
      Person person,
      String roleType,
      HibernateConstraintValidatorContext hibernateConstraintValidatorContext)
      throws Exception {
    boolean isValid = true;

    for (RoleTypeAttributeConstraint roleTypeAttributeConstraint :
        partyReferenceService.getRoleTypeAttributeConstraints(roleType)) {

      if (roleTypeAttributeConstraint.getType() == AttributeConstraintType.REQUIRED) {
        switch (roleTypeAttributeConstraint.getAttributeType()) {
          case "contact_mechanism":
            if (!person.hasContactMechanismType(roleTypeAttributeConstraint.getAttributeTypeQualifier())) {
              hibernateConstraintValidatorContext
                  .addMessageParameter(
                      "contactMechanismType",
                      roleTypeAttributeConstraint.getAttributeTypeQualifier())
                  .addMessageParameter("roleType", roleType)
                  .buildConstraintViolationWithTemplate(
                      "{digital.inception.party.constraints.ValidPerson.contactMechanismTypeRequiredForRoleType.message}")
                  .addConstraintViolation();

              isValid = false;
            }

            break;

          case "contact_mechanisms":
            if (!validateRequiredAttributeForRoleType(
                roleType,
                person.getContactMechanisms(),
                "{digital.inception.party.constraints.ValidPerson.contactMechanismRequiredForRoleType.message}",
                hibernateConstraintValidatorContext)) {
              isValid = false;
            }

            break;

          case "country_of_birth":
            if (!validateRequiredAttributeForRoleType(
                roleType,
                person.getCountryOfBirth(),
                "{digital.inception.party.constraints.ValidPerson.countryOfBirthRequiredForRoleType.message}",
                hibernateConstraintValidatorContext)) {
              isValid = false;
            }

            break;

          case "country_of_residence":
            if (!validateRequiredAttributeForRoleType(
                roleType,
                person.getCountryOfResidence(),
                "{digital.inception.party.constraints.ValidPerson.countryOfResidenceRequiredForRoleType.message}",
                hibernateConstraintValidatorContext)) {
              isValid = false;
            }

            break;

          case "countries_of_tax_residence":
            if (!validateRequiredAttributeForRoleType(
                roleType,
                person.getCountriesOfTaxResidence(),
                "{digital.inception.party.constraints.ValidPerson.countryOfTaxResidenceRequiredForRoleType.message}",
                hibernateConstraintValidatorContext)) {
              isValid = false;
            }

            break;

          case "date_of_birth":
            if (!validateRequiredAttributeForRoleType(
                roleType,
                person.getDateOfBirth(),
                "{digital.inception.party.constraints.ValidPerson.dateOfBirthRequiredForRoleType.message}",
                hibernateConstraintValidatorContext)) {
              isValid = false;
            }

            break;

          case "employment_status":
            if (!validateRequiredAttributeForRoleType(
                roleType,
                person.getEmploymentStatus(),
                "{digital.inception.party.constraints.ValidPerson.employmentStatusRequiredForRoleType.message}",
                hibernateConstraintValidatorContext)) {
              isValid = false;
            }

            break;

          case "employment_type":
            if (!validateRequiredAttributeForRoleType(
                roleType,
                person.getEmploymentType(),
                "{digital.inception.party.constraints.ValidPerson.employmentTypeRequiredForRoleType.message}",
                hibernateConstraintValidatorContext)) {
              isValid = false;
            }

            break;

          case "gender":
            if (!validateRequiredAttributeForRoleType(
                roleType,
                person.getGender(),
                "{digital.inception.party.constraints.ValidPerson.genderRequiredForRoleType.message}",
                hibernateConstraintValidatorContext)) {
              isValid = false;
            }

            break;

          case "given_name":
            if (!validateRequiredAttributeForRoleType(
                roleType,
                person.getGivenName(),
                "{digital.inception.party.constraints.ValidPerson.givenNameRequiredForRoleType.message}",
                hibernateConstraintValidatorContext)) {
              isValid = false;
            }

            break;

          case "home_language":
            if (!validateRequiredAttributeForRoleType(
                roleType,
                person.getHomeLanguage(),
                "{digital.inception.party.constraints.ValidPerson.homeLanguageRequiredForRoleType.message}",
                hibernateConstraintValidatorContext)) {
              isValid = false;
            }

            break;

          case "identity_documents":
            if (!validateRequiredAttributeForRoleType(
                roleType,
                person.getIdentityDocuments(),
                "{digital.inception.party.constraints.ValidPerson.identityDocumentRequiredForRoleType.message}",
                hibernateConstraintValidatorContext)) {
              isValid = false;
            }

            break;

          case "initials":
            if (!validateRequiredAttributeForRoleType(
                roleType,
                person.getInitials(),
                "{digital.inception.party.constraints.ValidPerson.initialsRequiredForRoleType.message}",
                hibernateConstraintValidatorContext)) {
              isValid = false;
            }

            break;

          case "marital_status":
            if (!validateRequiredAttributeForRoleType(
                roleType,
                person.getMaritalStatus(),
                "{digital.inception.party.constraints.ValidPerson.maritalStatusRequiredForRoleType.message}",
                hibernateConstraintValidatorContext)) {
              isValid = false;
            }

            break;

          case "marriage_type":
            if (!validateRequiredAttributeForRoleType(
                roleType,
                person.getMarriageType(),
                "{digital.inception.party.constraints.ValidPerson.marriageTypeRequiredForRoleType.message}",
                hibernateConstraintValidatorContext)) {
              isValid = false;
            }

            break;

          case "occupation":
            if (!validateRequiredAttributeForRoleType(
                roleType,
                person.getOccupation(),
                "{digital.inception.party.constraints.ValidPerson.occupationRequiredForRoleType.message}",
                hibernateConstraintValidatorContext)) {
              isValid = false;
            }

            break;

          case "physical_addresses":
            if (!validateRequiredAttributeForRoleType(
                roleType,
                person.getPhysicalAddresses(),
                "{digital.inception.party.constraints.ValidPerson.physicalAddressRequiredForRoleType.message}",
                hibernateConstraintValidatorContext)) {
              isValid = false;
            }

            break;

          case "physical_address":
            if (!person.hasPhysicalAddressRole(roleTypeAttributeConstraint.getAttributeTypeQualifier())) {
              hibernateConstraintValidatorContext
                  .addMessageParameter(
                      "physicalAddressRole",
                      roleTypeAttributeConstraint.getAttributeTypeQualifier())
                  .addMessageParameter("roleType", roleType)
                  .buildConstraintViolationWithTemplate(
                      "{digital.inception.party.constraints.ValidPerson.physicalAddressRoleRequiredForRoleType.message}")
                  .addConstraintViolation();

              isValid = false;
            }

            break;

          case "preferred_name":
            if (!validateRequiredAttributeForRoleType(
                roleType,
                person.getPreferredName(),
                "{digital.inception.party.constraints.ValidPerson.preferredNameRequiredForRoleType.message}",
                hibernateConstraintValidatorContext)) {
              isValid = false;
            }

            break;

          case "race":
            if (!validateRequiredAttributeForRoleType(
                roleType,
                person.getRace(),
                "{digital.inception.party.constraints.ValidPerson.raceRequiredForRoleType.message}",
                hibernateConstraintValidatorContext)) {
              isValid = false;
            }

            break;

          case "residency_status":
            if (!validateRequiredAttributeForRoleType(
                roleType,
                person.getResidencyStatus(),
                "{digital.inception.party.constraints.ValidPerson.residencyStatusRequiredForRoleType.message}",
                hibernateConstraintValidatorContext)) {
              isValid = false;
            }

            break;

          case "residential_type":
            if (!validateRequiredAttributeForRoleType(
                roleType,
                person.getResidentialType(),
                "{digital.inception.party.constraints.ValidPerson.residentialTypeRequiredForRoleType.message}",
                hibernateConstraintValidatorContext)) {
              isValid = false;
            }

            break;

          case "surname":
            if (!validateRequiredAttributeForRoleType(
                roleType,
                person.getSurname(),
                "{digital.inception.party.constraints.ValidPerson.surnameRequiredForRoleType.message}",
                hibernateConstraintValidatorContext)) {
              isValid = false;
            }

            break;

          case "tax_numbers":
            if (!validateRequiredAttributeForRoleType(
                roleType,
                person.getTaxNumbers(),
                "{digital.inception.party.constraints.ValidPerson.taxNumberRequiredForRoleType.message}",
                hibernateConstraintValidatorContext)) {
              isValid = false;
            }

            break;

          case "title":
            if (!validateRequiredAttributeForRoleType(
                roleType,
                person.getTitle(),
                "{digital.inception.party.constraints.ValidPerson.titleRequiredForRoleType.message}",
                hibernateConstraintValidatorContext)) {
              isValid = false;
            }
            break;
        }
      }
    }

    return isValid;
  }
}
