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
import digital.inception.party.ConstraintType;
import digital.inception.party.ContactMechanism;
import digital.inception.party.IPartyReferenceService;
import digital.inception.party.IdentityDocument;
import digital.inception.party.Person;
import digital.inception.party.PhysicalAddress;
import digital.inception.party.Preference;
import digital.inception.party.ResidencePermit;
import digital.inception.party.Role;
import digital.inception.party.RoleTypeAttributeConstraint;
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

        // Validate home language
        if (StringUtils.hasText(person.getHomeLanguage())
            && (!getReferenceService().isValidLanguage(person.getHomeLanguage()))) {
          hibernateConstraintValidatorContext
              .addMessageParameter("homeLanguage", person.getHomeLanguage())
              .buildConstraintViolationWithTemplate(
                  "{digital.inception.party.constraints.ValidPerson.invalidHomeLanguage.message}")
              .addPropertyNode("homeLanguage")
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

        // Validate resident permits
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

        // Validate residence permits

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
              if (!validatePersonWithRole(
                  person, role.getType(), hibernateConstraintValidatorContext)) {
                isValid = false;
              }
            }
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
        getPartyReferenceService().getRoleTypeAttributeConstraints(roleType)) {

      if (roleTypeAttributeConstraint.getType() == ConstraintType.MAX_SIZE) {
        // TODO: IMPLEMENT THIS VALIDATION  -- MARCUS
      } else if (roleTypeAttributeConstraint.getType() == ConstraintType.MIN_SIZE) {
        // TODO: IMPLEMENT THIS VALIDATION  -- MARCUS
      } else if (roleTypeAttributeConstraint.getType() == ConstraintType.PATTERN) {
        // TODO: IMPLEMENT THIS VALIDATION  -- MARCUS
      } else if (roleTypeAttributeConstraint.getType() == ConstraintType.REFERENCE) {
        // TODO: IMPLEMENT THIS VALIDATION  -- MARCUS
      } else if (roleTypeAttributeConstraint.getType() == ConstraintType.REQUIRED) {
        switch (roleTypeAttributeConstraint.getAttributeType()) {
          case "contact_mechanism":
            if (!person.hasContactMechanismType(
                roleTypeAttributeConstraint.getAttributeTypeQualifier())) {
              hibernateConstraintValidatorContext
                  .addMessageParameter(
                      "contactMechanismType",
                      roleTypeAttributeConstraint.getAttributeTypeQualifier())
                  .addMessageParameter("roleType", roleType)
                  .buildConstraintViolationWithTemplate(
                      "{digital.inception.party.constraints.ValidPerson.contactMechanismTypeRequiredForRoleType.message}")
                  .addPropertyNode("contactMechanisms")
                  .addConstraintViolation();

              isValid = false;
            }

            break;

          case "contact_mechanisms":
            if (!validateRequiredAttributeForRoleType(
                roleType,
                person.getContactMechanisms(),
                "contactMechanisms",
                "{digital.inception.party.constraints.ValidPerson.contactMechanismRequiredForRoleType.message}",
                hibernateConstraintValidatorContext)) {
              isValid = false;
            }

            break;

          case "countries_of_citizenship":
            if (!validateRequiredAttributeForRoleType(
                roleType,
                person.getCountriesOfCitizenship(),
                "countriesOfCitizenship",
                "{digital.inception.party.constraints.ValidPerson.countryOfCitizenshipRequiredForRoleType.message}",
                hibernateConstraintValidatorContext)) {
              isValid = false;
            }

            break;

          case "countries_of_tax_residence":
            if (!validateRequiredAttributeForRoleType(
                roleType,
                person.getCountriesOfTaxResidence(),
                "countriesOfTaxResidence",
                "{digital.inception.party.constraints.ValidPerson.countryOfTaxResidenceRequiredForRoleType.message}",
                hibernateConstraintValidatorContext)) {
              isValid = false;
            }

            break;

          case "country_of_birth":
            if (!validateRequiredAttributeForRoleType(
                roleType,
                person.getCountryOfBirth(),
                "countryOfBirth",
                "{digital.inception.party.constraints.ValidPerson.countryOfBirthRequiredForRoleType.message}",
                hibernateConstraintValidatorContext)) {
              isValid = false;
            }

            break;

          case "country_of_residence":
            if (!validateRequiredAttributeForRoleType(
                roleType,
                person.getCountryOfResidence(),
                "countryOfResidence",
                "{digital.inception.party.constraints.ValidPerson.countryOfResidenceRequiredForRoleType.message}",
                hibernateConstraintValidatorContext)) {
              isValid = false;
            }

            break;

          case "date_of_birth":
            if (!validateRequiredAttributeForRoleType(
                roleType,
                person.getDateOfBirth(),
                "dateOfBirth",
                "{digital.inception.party.constraints.ValidPerson.dateOfBirthRequiredForRoleType.message}",
                hibernateConstraintValidatorContext)) {
              isValid = false;
            }

            break;

          case "employment_status":
            if (!validateRequiredAttributeForRoleType(
                roleType,
                person.getEmploymentStatus(),
                "employmentStatus",
                "{digital.inception.party.constraints.ValidPerson.employmentStatusRequiredForRoleType.message}",
                hibernateConstraintValidatorContext)) {
              isValid = false;
            }

            break;

          case "employment_type":
            if (!validateRequiredAttributeForRoleType(
                roleType,
                person.getEmploymentType(),
                "employmentType",
                "{digital.inception.party.constraints.ValidPerson.employmentTypeRequiredForRoleType.message}",
                hibernateConstraintValidatorContext)) {
              isValid = false;
            }

            break;

          case "gender":
            if (!validateRequiredAttributeForRoleType(
                roleType,
                person.getGender(),
                "gender",
                "{digital.inception.party.constraints.ValidPerson.genderRequiredForRoleType.message}",
                hibernateConstraintValidatorContext)) {
              isValid = false;
            }

            break;

          case "given_name":
            if (!validateRequiredAttributeForRoleType(
                roleType,
                person.getGivenName(),
                "givenName",
                "{digital.inception.party.constraints.ValidPerson.givenNameRequiredForRoleType.message}",
                hibernateConstraintValidatorContext)) {
              isValid = false;
            }

            break;

          case "home_language":
            if (!validateRequiredAttributeForRoleType(
                roleType,
                person.getHomeLanguage(),
                "homeLanguage",
                "{digital.inception.party.constraints.ValidPerson.homeLanguageRequiredForRoleType.message}",
                hibernateConstraintValidatorContext)) {
              isValid = false;
            }

            break;

          case "identity_documents":
            if (!validateRequiredAttributeForRoleType(
                roleType,
                person.getIdentityDocuments(),
                "identityDocuments",
                "{digital.inception.party.constraints.ValidPerson.identityDocumentRequiredForRoleType.message}",
                hibernateConstraintValidatorContext)) {
              isValid = false;
            }

            break;

          case "initials":
            if (!validateRequiredAttributeForRoleType(
                roleType,
                person.getInitials(),
                "initials",
                "{digital.inception.party.constraints.ValidPerson.initialsRequiredForRoleType.message}",
                hibernateConstraintValidatorContext)) {
              isValid = false;
            }

            break;

          case "marital_status":
            if (!validateRequiredAttributeForRoleType(
                roleType,
                person.getMaritalStatus(),
                "maritalStatus",
                "{digital.inception.party.constraints.ValidPerson.maritalStatusRequiredForRoleType.message}",
                hibernateConstraintValidatorContext)) {
              isValid = false;
            }

            break;

          case "marriage_type":
            if (!validateRequiredAttributeForRoleType(
                roleType,
                person.getMarriageType(),
                "marriageType",
                "{digital.inception.party.constraints.ValidPerson.marriageTypeRequiredForRoleType.message}",
                hibernateConstraintValidatorContext)) {
              isValid = false;
            }

            break;

          case "occupation":
            if (!validateRequiredAttributeForRoleType(
                roleType,
                person.getOccupation(),
                "occupation",
                "{digital.inception.party.constraints.ValidPerson.occupationRequiredForRoleType.message}",
                hibernateConstraintValidatorContext)) {
              isValid = false;
            }

            break;

          case "physical_addresses":
            if (!validateRequiredAttributeForRoleType(
                roleType,
                person.getPhysicalAddresses(),
                "physicalAddresses",
                "{digital.inception.party.constraints.ValidPerson.physicalAddressRequiredForRoleType.message}",
                hibernateConstraintValidatorContext)) {
              isValid = false;
            }

            break;

          case "physical_address":
            if (!person.hasPhysicalAddressRole(
                roleTypeAttributeConstraint.getAttributeTypeQualifier())) {
              hibernateConstraintValidatorContext
                  .addMessageParameter(
                      "physicalAddressRole",
                      roleTypeAttributeConstraint.getAttributeTypeQualifier())
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
            if (!validateRequiredAttributeForRoleType(
                roleType,
                person.getPreferredName(),
                "preferredName",
                "{digital.inception.party.constraints.ValidPerson.preferredNameRequiredForRoleType.message}",
                hibernateConstraintValidatorContext)) {
              isValid = false;
            }

            break;

          case "race":
            if (!validateRequiredAttributeForRoleType(
                roleType,
                person.getRace(),
                "race",
                "{digital.inception.party.constraints.ValidPerson.raceRequiredForRoleType.message}",
                hibernateConstraintValidatorContext)) {
              isValid = false;
            }

            break;

          case "residency_status":
            if (!validateRequiredAttributeForRoleType(
                roleType,
                person.getResidencyStatus(),
                "residencyStatus",
                "{digital.inception.party.constraints.ValidPerson.residencyStatusRequiredForRoleType.message}",
                hibernateConstraintValidatorContext)) {
              isValid = false;
            }

            break;

          case "residential_type":
            if (!validateRequiredAttributeForRoleType(
                roleType,
                person.getResidentialType(),
                "residentialType",
                "{digital.inception.party.constraints.ValidPerson.residentialTypeRequiredForRoleType.message}",
                hibernateConstraintValidatorContext)) {
              isValid = false;
            }

            break;

          case "surname":
            if (!validateRequiredAttributeForRoleType(
                roleType,
                person.getSurname(),
                "surname",
                "{digital.inception.party.constraints.ValidPerson.surnameRequiredForRoleType.message}",
                hibernateConstraintValidatorContext)) {
              isValid = false;
            }

            break;

          case "tax_numbers":
            if (!validateRequiredAttributeForRoleType(
                roleType,
                person.getTaxNumbers(),
                "taxNumbers",
                "{digital.inception.party.constraints.ValidPerson.taxNumberRequiredForRoleType.message}",
                hibernateConstraintValidatorContext)) {
              isValid = false;
            }

            break;

          case "title":
            if (!validateRequiredAttributeForRoleType(
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
                person.getAttribute(roleTypeAttributeConstraint.getAttributeType());

            if (attributeOptional.isEmpty() || (!attributeOptional.get().hasValue())) {
              hibernateConstraintValidatorContext
                  .addMessageParameter(
                      "attributeType", roleTypeAttributeConstraint.getAttributeType())
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
      } else if (roleTypeAttributeConstraint.getType() == ConstraintType.SIZE) {
        // TODO: IMPLEMENT THIS VALIDATION  -- MARCUS
      }
    }

    return isValid;
  }
}
