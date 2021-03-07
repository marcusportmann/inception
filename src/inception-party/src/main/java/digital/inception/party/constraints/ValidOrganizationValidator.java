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
import digital.inception.party.Organization;
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

/**
 * The <b>ValidOrganizationValidator</b> class implements the custom constraint validator for
 * validating an organization.
 *
 * @author Marcus Portmann
 */
public class ValidOrganizationValidator extends PartyValidator
    implements ConstraintValidator<ValidOrganization, Organization> {

  /** The Party Reference Service. */
  private final IPartyReferenceService partyReferenceService;

  /** The Reference Service. */
  private final IReferenceService referenceService;

  /**
   * Constructs a new <b>ValidOrganizationValidator</b>.
   *
   * @param partyReferenceService the Party Reference Service
   * @param referenceService the Reference Service
   */
  @Autowired
  public ValidOrganizationValidator(
      IPartyReferenceService partyReferenceService, IReferenceService referenceService) {
    this.partyReferenceService = partyReferenceService;
    this.referenceService = referenceService;
  }

  @Override
  public void initialize(ValidOrganization constraintAnnotation) {}

  @Override
  public boolean isValid(
      Organization organization, ConstraintValidatorContext constraintValidatorContext) {

    boolean isValid = true;

    // Disable the default constraint violation
    constraintValidatorContext.disableDefaultConstraintViolation();

    HibernateConstraintValidatorContext hibernateConstraintValidatorContext =
        constraintValidatorContext.unwrap(HibernateConstraintValidatorContext.class);

    try {
      // Validate contact mechanisms
      for (ContactMechanism contactMechanism : organization.getContactMechanisms()) {
        if (!partyReferenceService.isValidContactMechanismType(contactMechanism.getType())) {
          hibernateConstraintValidatorContext
              .addMessageParameter("contactMechanismType", contactMechanism.getType())
              .addMessageParameter("contactMechanismPurpose", contactMechanism.getPurpose())
              .buildConstraintViolationWithTemplate(
                  "{digital.inception.party.constraints.ValidOrganization.invalidContactMechanismPurposeCode.message}")
              .addConstraintViolation();

          isValid = false;
        }

        if (!partyReferenceService.isValidContactMechanismPurpose(
            organization.getType().code(),
            contactMechanism.getType(),
            contactMechanism.getPurpose())) {
          hibernateConstraintValidatorContext
              .addMessageParameter("contactMechanismPurpose", contactMechanism.getPurpose())
              .addMessageParameter("partyType", organization.getType().code())
              .buildConstraintViolationWithTemplate(
                  "{digital.inception.party.constraints.ValidOrganization.invalidContactMechanismPurposeCodeForPartyType.message}")
              .addConstraintViolation();

          isValid = false;
        }
      }

      // Validate countries of tax residence
      for (String countryOfTaxResidence : organization.getCountriesOfTaxResidence()) {
        if (!referenceService.isValidCountry(countryOfTaxResidence)) {
          hibernateConstraintValidatorContext
              .addMessageParameter("countryOfTaxResidence", countryOfTaxResidence)
              .buildConstraintViolationWithTemplate(
                  "{digital.inception.party.constraints.ValidOrganization.invalidCountryOfTaxResidenceCode.message}")
              .addConstraintViolation();

          isValid = false;
        }
      }

      // Validate identity documents
      for (IdentityDocument identityDocument : organization.getIdentityDocuments()) {
        if (!referenceService.isValidCountry(identityDocument.getCountryOfIssue())) {
          hibernateConstraintValidatorContext
              .addMessageParameter("countryOfIssue", identityDocument.getCountryOfIssue())
              .buildConstraintViolationWithTemplate(
                  "{digital.inception.party.constraints.ValidOrganization.invalidIdentityDocumentCountryOfIssueCode.message}")
              .addConstraintViolation();

          isValid = false;
        }

        if (!partyReferenceService.isValidIdentityDocumentType(
            organization.getType().code(), identityDocument.getType())) {
          hibernateConstraintValidatorContext
              .addMessageParameter("type", identityDocument.getType())
              .buildConstraintViolationWithTemplate(
                  "{digital.inception.party.constraints.ValidOrganization.invalidIdentityDocumentTypeCode.message}")
              .addConstraintViolation();

          isValid = false;
        }
      }

      // Validate roles
      for (Role role : organization.getRoles()) {
        if (!partyReferenceService.isValidRoleType(organization.getType().code(), role.getType())) {
          hibernateConstraintValidatorContext
              .addMessageParameter("type", role.getType())
              .buildConstraintViolationWithTemplate(
                  "{digital.inception.party.constraints.ValidOrganization.invalidRoleTypeCode.message}")
              .addConstraintViolation();

          isValid = false;
        }
      }

      // Validate physical addresses
      for (PhysicalAddress physicalAddress : organization.getPhysicalAddresses()) {
        if (!partyReferenceService.isValidPhysicalAddressRole(
            organization.getType().code(), physicalAddress.getRole())) {
          hibernateConstraintValidatorContext
              .addMessageParameter("physicalAddressRole", physicalAddress.getRole())
              .addMessageParameter("partyType", organization.getType().code())
              .buildConstraintViolationWithTemplate(
                  "{digital.inception.party.constraints.ValidOrganization.invalidPhysicalAddressRoleCodeForPartyType.message}")
              .addConstraintViolation();

          isValid = false;
        }

        for (String physicalAddressPurpose : physicalAddress.getPurposes()) {
          if (!partyReferenceService.isValidPhysicalAddressPurpose(
              organization.getType().code(), physicalAddressPurpose)) {
            hibernateConstraintValidatorContext
                .addMessageParameter("physicalAddressPurpose", physicalAddressPurpose)
                .addMessageParameter("partyType", organization.getType().code())
                .buildConstraintViolationWithTemplate(
                    "{digital.inception.party.constraints.ValidOrganization.invalidPhysicalAddressPurposeCodeForPartyType.message}")
                .addConstraintViolation();

            isValid = false;
          }
        }
      }

      // Validate attributes
      for (Attribute attribute : organization.getAttributes()) {
        for (String reservedAttributeTypeCode : Attribute.RESERVED_ATTRIBUTE_TYPE_CODES) {
          if (reservedAttributeTypeCode.equalsIgnoreCase(attribute.getType())) {
            hibernateConstraintValidatorContext
                .addMessageParameter("attributeType", attribute.getType())
                .buildConstraintViolationWithTemplate(
                    "{digital.inception.party.constraints.ValidOrganization.invalidReservedAttributeTypeCode.message}")
                .addConstraintViolation();

            isValid = false;
          }
        }
      }

      // Validate preferences
      for (Preference preference : organization.getPreferences()) {
        if (!partyReferenceService.isValidPreferenceType(
            organization.getType().code(), preference.getType())) {
          hibernateConstraintValidatorContext
              .addMessageParameter("type", preference.getType())
              .buildConstraintViolationWithTemplate(
                  "{digital.inception.party.constraints.ValidOrganization.invalidPreferenceTypeCode.message}")
              .addConstraintViolation();

          isValid = false;
        }
      }

      // Validate tax numbers
      for (TaxNumber taxNumber : organization.getTaxNumbers()) {
        if (!referenceService.isValidCountry(taxNumber.getCountryOfIssue())) {
          hibernateConstraintValidatorContext
              .addMessageParameter("countryOfIssue", taxNumber.getCountryOfIssue())
              .buildConstraintViolationWithTemplate(
                  "{digital.inception.party.constraints.ValidOrganization.invalidTaxNumberCountryOfIssueCode.message}")
              .addConstraintViolation();

          isValid = false;
        }

        if (!partyReferenceService.isValidTaxNumberType(taxNumber.getType())) {
          hibernateConstraintValidatorContext
              .addMessageParameter("type", taxNumber.getType())
              .buildConstraintViolationWithTemplate(
                  "{digital.inception.party.constraints.ValidOrganization.invalidTaxNumberTypeCode.message}")
              .addConstraintViolation();

          isValid = false;
        }
      }

      for (Role role : organization.getRoles()) {
        if (!validateOrganizationWithRole(
            organization, role.getType(), hibernateConstraintValidatorContext)) {
          isValid = false;
        }
      }

    } catch (Throwable e) {
      throw new ValidationException("Failed to validate the organization", e);
    }

    return isValid;
  }

  private boolean validateOrganizationWithRole(
      Organization organization,
      String roleType,
      HibernateConstraintValidatorContext hibernateConstraintValidatorContext)
      throws Exception {
    boolean isValid = true;

    for (RoleTypeAttributeConstraint roleTypeAttributeConstraint :
        partyReferenceService.getRoleTypeAttributeConstraints(roleType)) {

      if (roleTypeAttributeConstraint.getType() == AttributeConstraintType.REQUIRED) {
        switch (roleTypeAttributeConstraint.getAttributeType()) {
          case "contact_mechanism":
            if (!organization.hasContactMechanismType(
                roleTypeAttributeConstraint.getAttributeTypeQualifier())) {
              hibernateConstraintValidatorContext
                  .addMessageParameter(
                      "contactMechanismType",
                      roleTypeAttributeConstraint.getAttributeTypeQualifier())
                  .addMessageParameter("roleType", roleType)
                  .buildConstraintViolationWithTemplate(
                      "{digital.inception.party.constraints.ValidOrganization.contactMechanismTypeRequiredForRoleType.message}")
                  .addConstraintViolation();

              isValid = false;
            }

            break;

          case "contact_mechanisms":
            if (!validateRequiredAttributeForRoleType(
                roleType,
                organization.getContactMechanisms(),
                "{digital.inception.party.constraints.ValidOrganization.contactMechanismRequiredForRoleType.message}",
                hibernateConstraintValidatorContext)) {
              isValid = false;
            }

            break;

          case "countries_of_tax_residence":
            if (!validateRequiredAttributeForRoleType(
                roleType,
                organization.getCountriesOfTaxResidence(),
                "{digital.inception.party.constraints.ValidOrganization.countryOfTaxResidenceRequiredForRoleType.message}",
                hibernateConstraintValidatorContext)) {
              isValid = false;
            }

            break;

          case "identity_documents":
            if (!validateRequiredAttributeForRoleType(
                roleType,
                organization.getIdentityDocuments(),
                "{digital.inception.party.constraints.ValidOrganization.identityDocumentRequiredForRoleType.message}",
                hibernateConstraintValidatorContext)) {
              isValid = false;
            }

            break;

          case "physical_addresses":
            if (!validateRequiredAttributeForRoleType(
                roleType,
                organization.getPhysicalAddresses(),
                "{digital.inception.party.constraints.ValidOrganization.physicalAddressRequiredForRoleType.message}",
                hibernateConstraintValidatorContext)) {
              isValid = false;
            }

            break;

          case "physical_address":
            if (!organization.hasPhysicalAddressRole(roleTypeAttributeConstraint.getAttributeTypeQualifier())) {
              hibernateConstraintValidatorContext
                  .addMessageParameter(
                      "physicalAddressRole",
                      roleTypeAttributeConstraint.getAttributeTypeQualifier())
                  .addMessageParameter("roleType", roleType)
                  .buildConstraintViolationWithTemplate(
                      "{digital.inception.party.constraints.ValidOrganization.physicalAddressRoleRequiredForRoleType.message}")
                  .addConstraintViolation();

              isValid = false;
            }

            break;

          case "tax_numbers":
            if (!validateRequiredAttributeForRoleType(
                roleType,
                organization.getTaxNumbers(),
                "{digital.inception.party.constraints.ValidOrganization.taxNumberRequiredForRoleType.message}",
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
