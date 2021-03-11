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
import digital.inception.party.Organization;
import digital.inception.party.PhysicalAddress;
import digital.inception.party.Preference;
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
 * The <b>ValidOrganizationValidator</b> class implements the custom constraint validator for
 * validating an organization.
 *
 * @author Marcus Portmann
 */
public class ValidOrganizationValidator extends PartyValidator
    implements ConstraintValidator<ValidOrganization, Organization> {

  /**
   * Constructs a new <b>ValidOrganizationValidator</b>.
   *
   * @param partyReferenceService the Party Reference Service
   * @param referenceService the Reference Service
   */
  @Autowired
  public ValidOrganizationValidator(
      IPartyReferenceService partyReferenceService, IReferenceService referenceService) {
    super(partyReferenceService, referenceService);
  }

  /** Constructs a new <b>ValidOrganizationValidator</b>. */
  public ValidOrganizationValidator() {
    super(null, null);
  }

  @Override
  public void initialize(ValidOrganization constraintAnnotation) {}

  @Override
  public boolean isValid(
      Organization organization, ConstraintValidatorContext constraintValidatorContext) {

    if ((getPartyReferenceService() != null) && (getReferenceService() != null)) {
      boolean isValid = true;

      // Disable the default constraint violation
      constraintValidatorContext.disableDefaultConstraintViolation();

      HibernateConstraintValidatorContext hibernateConstraintValidatorContext =
          constraintValidatorContext.unwrap(HibernateConstraintValidatorContext.class);

      try {
        // Validate attributes
        for (Attribute attribute : organization.getAttributes()) {
          for (String reservedAttributeTypeCode : Attribute.RESERVED_ATTRIBUTE_TYPE_CODES) {
            if (reservedAttributeTypeCode.equalsIgnoreCase(attribute.getType())) {
              hibernateConstraintValidatorContext
                  .addMessageParameter("attributeType", attribute.getType())
                  .buildConstraintViolationWithTemplate(
                      "{digital.inception.party.constraints.ValidOrganization.invalidReservedAttributeType.message}")
                  .addPropertyNode("attributes")
                  .addPropertyNode("type")
                  .inIterable()
                  .addConstraintViolation();

              isValid = false;
            }
          }
        }

        // Validate contact mechanisms
        for (ContactMechanism contactMechanism : organization.getContactMechanisms()) {
          if (StringUtils.hasText(contactMechanism.getType())) {
            if (!ContactMechanism.VALID_CONTACT_MECHANISM_TYPES.contains(
                contactMechanism.getType())) {
              // Do not add a constraint violation here as it would duplicate the regex validation
            } else {

              if (StringUtils.hasText(contactMechanism.getRole())) {
                if (!getPartyReferenceService()
                    .isValidContactMechanismRole(
                        organization.getType().code(),
                        contactMechanism.getType(),
                        contactMechanism.getRole())) {
                  hibernateConstraintValidatorContext
                      .addMessageParameter("contactMechanismRole", contactMechanism.getRole())
                      .addMessageParameter("contactMechanismType", contactMechanism.getType())
                      .addMessageParameter("partyType", organization.getType().code())
                      .buildConstraintViolationWithTemplate(
                          "{digital.inception.party.constraints.ValidOrganization.invalidContactMechanismRoleForPartyType.message}")
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
                        organization.getType().code(),
                        contactMechanism.getType(),
                        contactMechanismPurpose)) {
                  hibernateConstraintValidatorContext
                      .addMessageParameter("contactMechanismPurpose", contactMechanismPurpose)
                      .addMessageParameter("contactMechanismType", contactMechanism.getType())
                      .addMessageParameter("partyType", organization.getType().code())
                      .buildConstraintViolationWithTemplate(
                          "{digital.inception.party.constraints.ValidOrganization.invalidContactMechanismPurposeForPartyType.message}")
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

        // Validate countries of tax residence
        for (String countryOfTaxResidence : organization.getCountriesOfTaxResidence()) {
          if (!getReferenceService().isValidCountry(countryOfTaxResidence)) {
            hibernateConstraintValidatorContext
                .addMessageParameter("countryOfTaxResidence", countryOfTaxResidence)
                .buildConstraintViolationWithTemplate(
                    "{digital.inception.party.constraints.ValidOrganization.invalidCountryOfTaxResidence.message}")
                .addPropertyNode("countriesOfTaxResidence")
                .addConstraintViolation();

            isValid = false;
          }
        }

        // Validate identity documents
        for (IdentityDocument identityDocument : organization.getIdentityDocuments()) {

          if (StringUtils.hasText(identityDocument.getCountryOfIssue())) {
            if (!getReferenceService().isValidCountry(identityDocument.getCountryOfIssue())) {
              hibernateConstraintValidatorContext
                  .addMessageParameter("countryOfIssue", identityDocument.getCountryOfIssue())
                  .buildConstraintViolationWithTemplate(
                      "{digital.inception.party.constraints.ValidOrganization.invalidIdentityDocumentCountryOfIssue.message}")
                  .addPropertyNode("identityDocuments")
                  .addPropertyNode("countryOfIssue")
                  .inIterable()
                  .addConstraintViolation();

              isValid = false;
            }
          }

          if (StringUtils.hasText(identityDocument.getType())) {
            if (!getPartyReferenceService()
                .isValidIdentityDocumentType(
                    organization.getType().code(), identityDocument.getType())) {
              hibernateConstraintValidatorContext
                  .addMessageParameter("identityDocumentType", identityDocument.getType())
                  .addMessageParameter("partyType", organization.getType().code())
                  .buildConstraintViolationWithTemplate(
                      "{digital.inception.party.constraints.ValidOrganization.invalidIdentityDocumentTypeForPartyType.message}")
                  .addPropertyNode("identityDocuments")
                  .addPropertyNode("type")
                  .inIterable()
                  .addConstraintViolation();

              isValid = false;
            }
          }
        }

        // Validate physical addresses
        for (PhysicalAddress physicalAddress : organization.getPhysicalAddresses()) {
          if (StringUtils.hasText(physicalAddress.getRole())) {
            if (!getPartyReferenceService()
                .isValidPhysicalAddressRole(
                    organization.getType().code(), physicalAddress.getRole())) {
              hibernateConstraintValidatorContext
                  .addMessageParameter("physicalAddressRole", physicalAddress.getRole())
                  .addMessageParameter("partyType", organization.getType().code())
                  .buildConstraintViolationWithTemplate(
                      "{digital.inception.party.constraints.ValidOrganization.invalidPhysicalAddressRoleForPartyType.message}")
                  .addPropertyNode("physicalAddresses")
                  .addPropertyNode("role")
                  .inIterable()
                  .addConstraintViolation();

              isValid = false;
            }
          }

          for (String physicalAddressPurpose : physicalAddress.getPurposes()) {
            if (!getPartyReferenceService()
                .isValidPhysicalAddressPurpose(
                    organization.getType().code(), physicalAddressPurpose)) {
              hibernateConstraintValidatorContext
                  .addMessageParameter("physicalAddressPurpose", physicalAddressPurpose)
                  .addMessageParameter("partyType", organization.getType().code())
                  .buildConstraintViolationWithTemplate(
                      "{digital.inception.party.constraints.ValidOrganization.invalidPhysicalAddressPurposeForPartyType.message}")
                  .addPropertyNode("physicalAddresses")
                  .addPropertyNode("purposes")
                  .inIterable()
                  .addConstraintViolation();

              isValid = false;
            }
          }
        }

        // Validate preferences
        for (Preference preference : organization.getPreferences()) {
          if (StringUtils.hasText(preference.getType())) {
            if (!getPartyReferenceService()
                .isValidPreferenceType(organization.getType().code(), preference.getType())) {
              hibernateConstraintValidatorContext
                  .addMessageParameter("preferenceType", preference.getType())
                  .addMessageParameter("partyType", organization.getType().code())
                  .buildConstraintViolationWithTemplate(
                      "{digital.inception.party.constraints.ValidOrganization.invalidPreferenceTypeForPartyType.message}")
                  .addPropertyNode("preferences")
                  .addPropertyNode("type")
                  .inIterable()
                  .addConstraintViolation();

              isValid = false;
            }
          }
        }

        // Validate tax numbers
        for (TaxNumber taxNumber : organization.getTaxNumbers()) {
          if (StringUtils.hasText(taxNumber.getCountryOfIssue())) {
            if (!getReferenceService().isValidCountry(taxNumber.getCountryOfIssue())) {
              hibernateConstraintValidatorContext
                  .addMessageParameter("countryOfIssue", taxNumber.getCountryOfIssue())
                  .buildConstraintViolationWithTemplate(
                      "{digital.inception.party.constraints.ValidOrganization.invalidTaxNumberCountryOfIssue.message}")
                  .addPropertyNode("taxNumbers")
                  .addPropertyNode("countryOfIssue")
                  .inIterable()
                  .addConstraintViolation();

              isValid = false;
            }
          }

          if (StringUtils.hasText(taxNumber.getType())) {
            if (!getPartyReferenceService()
                .isValidTaxNumberType(organization.getType().code(), taxNumber.getType())) {
              hibernateConstraintValidatorContext
                  .addMessageParameter("taxNumberType", taxNumber.getType())
                  .addMessageParameter("partyType", organization.getType().code())
                  .buildConstraintViolationWithTemplate(
                      "{digital.inception.party.constraints.ValidOrganization.invalidTaxNumberTypeForPartyType.message}")
                  .addPropertyNode("taxNumbers")
                  .addPropertyNode("type")
                  .inIterable()
                  .addConstraintViolation();

              isValid = false;
            }
          }
        }

        // Validate roles
        for (Role role : organization.getRoles()) {
          if (StringUtils.hasText(role.getType())) {
            if (!getPartyReferenceService()
                .isValidRoleType(organization.getType().code(), role.getType())) {
              hibernateConstraintValidatorContext
                  .addMessageParameter("roleType", role.getType())
                  .addMessageParameter("partyType", organization.getType().code())
                  .buildConstraintViolationWithTemplate(
                      "{digital.inception.party.constraints.ValidOrganization.invalidRoleTypeForPartyType.message}")
                  .addPropertyNode("roles")
                  .addPropertyNode("type")
                  .inIterable()
                  .addConstraintViolation();

              isValid = false;
            } else {
              if (!validateOrganizationWithRole(
                  organization, role.getType(), hibernateConstraintValidatorContext)) {
                isValid = false;
              }
            }
          }
        }

      } catch (Throwable e) {
        throw new ValidationException("Failed to validate the organization", e);
      }

      return isValid;
    } else {
      return true;
    }
  }

  private boolean validateOrganizationWithRole(
      Organization organization,
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
            if (!organization.hasContactMechanismType(
                roleTypeAttributeConstraint.getAttributeTypeQualifier())) {
              hibernateConstraintValidatorContext
                  .addMessageParameter(
                      "contactMechanismType",
                      roleTypeAttributeConstraint.getAttributeTypeQualifier())
                  .addMessageParameter("roleType", roleType)
                  .buildConstraintViolationWithTemplate(
                      "{digital.inception.party.constraints.ValidOrganization.contactMechanismTypeRequiredForRoleType.message}")
                  .addPropertyNode("contactMechanisms")
                  .addConstraintViolation();

              isValid = false;
            }

            break;

          case "contact_mechanisms":
            if (!validateRequiredAttributeForRoleType(
                roleType,
                organization.getContactMechanisms(),
                "contactMechanisms",
                "{digital.inception.party.constraints.ValidOrganization.contactMechanismRequiredForRoleType.message}",
                hibernateConstraintValidatorContext)) {
              isValid = false;
            }

            break;

          case "countries_of_tax_residence":
            if (!validateRequiredAttributeForRoleType(
                roleType,
                organization.getCountriesOfTaxResidence(),
                "countriesOfTaxResidence",
                "{digital.inception.party.constraints.ValidOrganization.countryOfTaxResidenceRequiredForRoleType.message}",
                hibernateConstraintValidatorContext)) {
              isValid = false;
            }

            break;

          case "identity_documents":
            if (!validateRequiredAttributeForRoleType(
                roleType,
                organization.getIdentityDocuments(),
                "identityDocuments",
                "{digital.inception.party.constraints.ValidOrganization.identityDocumentRequiredForRoleType.message}",
                hibernateConstraintValidatorContext)) {
              isValid = false;
            }

            break;

          case "physical_addresses":
            if (!validateRequiredAttributeForRoleType(
                roleType,
                organization.getPhysicalAddresses(),
                "physicalAddresses",
                "{digital.inception.party.constraints.ValidOrganization.physicalAddressRequiredForRoleType.message}",
                hibernateConstraintValidatorContext)) {
              isValid = false;
            }

            break;

          case "physical_address":
            if (!organization.hasPhysicalAddressRole(
                roleTypeAttributeConstraint.getAttributeTypeQualifier())) {
              hibernateConstraintValidatorContext
                  .addMessageParameter(
                      "physicalAddressRole",
                      roleTypeAttributeConstraint.getAttributeTypeQualifier())
                  .addMessageParameter("roleType", roleType)
                  .buildConstraintViolationWithTemplate(
                      "{digital.inception.party.constraints.ValidOrganization.physicalAddressRoleRequiredForRoleType.message}")
                  .addPropertyNode("physicalAddresses")
                  .addPropertyNode("role")
                  .inIterable()
                  .addConstraintViolation();

              isValid = false;
            }

            break;

          case "tax_numbers":
            if (!validateRequiredAttributeForRoleType(
                roleType,
                organization.getTaxNumbers(),
                "taxNumbers",
                "{digital.inception.party.constraints.ValidOrganization.taxNumberRequiredForRoleType.message}",
                hibernateConstraintValidatorContext)) {
              isValid = false;
            }

            break;

          default:
            Optional<Attribute> attributeOptional =
                organization.getAttribute(roleTypeAttributeConstraint.getAttributeType());

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

      }
    }

    return isValid;
  }
}
