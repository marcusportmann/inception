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
import digital.inception.party.IPartyReferenceService;
import digital.inception.party.IdentityDocument;
import digital.inception.party.Organization;
import digital.inception.party.PartyRole;
import digital.inception.party.PartyType;
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
 * The <b>ValidOrganizationValidator</b> class implements the custom constraint validator for
 * validating an organization.
 *
 * @author Marcus Portmann
 */
public class ValidOrganizationValidator
    implements ConstraintValidator<ValidOrganization, Organization> {

  private final IPartyReferenceService partyService;

  private final IReferenceService referenceService;

  @Autowired
  public ValidOrganizationValidator(
      IPartyReferenceService partyReferenceService, IReferenceService referenceService) {
    this.partyService = partyReferenceService;
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
        if (!partyService.isValidContactMechanismType(contactMechanism.getType())) {
          hibernateConstraintValidatorContext
              .addMessageParameter("contactMechanismType", contactMechanism.getType())
              .addMessageParameter("contactMechanismPurpose", contactMechanism.getPurpose())
              .buildConstraintViolationWithTemplate(
                  "{digital.inception.party.constraints.ValidOrganization.invalidContactMechanismPurposeCode.message}")
              .addConstraintViolation();

          isValid = false;
        }

        if (!partyService.isValidContactMechanismPurpose(
            organization.getPartyType().code(),
            contactMechanism.getType(),
            contactMechanism.getPurpose())) {
          hibernateConstraintValidatorContext
              .addMessageParameter("contactMechanismPurpose", contactMechanism.getPurpose())
              .addMessageParameter("partyType", organization.getPartyType().code())
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

        if (!partyService.isValidIdentityDocumentType(
            organization.getPartyType().code(), identityDocument.getType())) {
          hibernateConstraintValidatorContext
              .addMessageParameter("type", identityDocument.getType())
              .buildConstraintViolationWithTemplate(
                  "{digital.inception.party.constraints.ValidOrganization.invalidIdentityDocumentTypeCode.message}")
              .addConstraintViolation();

          isValid = false;
        }
      }

      // Validate party roles
      for (PartyRole partyRole : organization.getRoles()) {
        if (!partyService.isValidPartyRoleType(organization.getPartyType().code(), partyRole.getType())) {
          hibernateConstraintValidatorContext
              .addMessageParameter("type", partyRole.getType())
              .buildConstraintViolationWithTemplate(
                  "{digital.inception.party.constraints.ValidOrganization.invalidPartyRoleTypeCode.message}")
              .addConstraintViolation();

          isValid = false;
        }
      }

      // Validate physical addresses
      for (PhysicalAddress physicalAddress : organization.getPhysicalAddresses()) {
        if (!partyService.isValidPhysicalAddressRole(
            organization.getPartyType().code(), physicalAddress.getRole())) {
          hibernateConstraintValidatorContext
              .addMessageParameter("physicalAddressRole", physicalAddress.getRole())
              .addMessageParameter("partyType", organization.getPartyType().code())
              .buildConstraintViolationWithTemplate(
                  "{digital.inception.party.constraints.ValidOrganization.invalidPhysicalAddressRoleCodeForPartyType.message}")
              .addConstraintViolation();

          isValid = false;
        }

        for (String physicalAddressPurpose : physicalAddress.getPurposes()) {
          if (!partyService.isValidPhysicalAddressPurpose(
              organization.getPartyType().code(), physicalAddressPurpose)) {
            hibernateConstraintValidatorContext
                .addMessageParameter("physicalAddressPurpose", physicalAddressPurpose)
                .addMessageParameter("partyType", organization.getPartyType().code())
                .buildConstraintViolationWithTemplate(
                    "{digital.inception.party.constraints.ValidOrganization.invalidPhysicalAddressPurposeCodeForPartyType.message}")
                .addConstraintViolation();

            isValid = false;
          }
        }
      }

      // Validate preferences
      for (Preference preference : organization.getPreferences()) {
        if (!partyService.isValidPreferenceType(organization.getPartyType().code(), preference.getType())) {
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

        if (!partyService.isValidTaxNumberType(taxNumber.getType())) {
          hibernateConstraintValidatorContext
              .addMessageParameter("type", taxNumber.getType())
              .buildConstraintViolationWithTemplate(
                  "{digital.inception.party.constraints.ValidOrganization.invalidTaxNumberTypeCode.message}")
              .addConstraintViolation();

          isValid = false;
        }
      }

    } catch (Throwable e) {
      throw new ValidationException("Failed to validate the organization", e);
    }

    return isValid;  }
}
