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

import digital.inception.banking.customer.BusinessCustomer;
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

/**
 * The <b>ValidBusinessCustomerValidator</b> class implements the custom constraint validator for
 * validating a business customer.
 *
 * @author Marcus Portmann
 */
public class ValidBusinessCustomerValidator
    implements ConstraintValidator<ValidBusinessCustomer, BusinessCustomer> {

  private final IPartyReferenceService partyReferenceService;

  private final IReferenceService referenceService;

  @Autowired
  public ValidBusinessCustomerValidator(
      IPartyReferenceService partyReferenceService, IReferenceService referenceService) {
    this.partyReferenceService = partyReferenceService;
    this.referenceService = referenceService;
  }

  @Override
  public void initialize(ValidBusinessCustomer constraintAnnotation) {}

  @Override
  public boolean isValid(
      BusinessCustomer businessCustomer, ConstraintValidatorContext constraintValidatorContext) {

    boolean isValid = true;

    // Disable the default constraint violation
    constraintValidatorContext.disableDefaultConstraintViolation();

    HibernateConstraintValidatorContext hibernateConstraintValidatorContext =
        constraintValidatorContext.unwrap(HibernateConstraintValidatorContext.class);

    try {
      // Validate contact mechanisms
      for (ContactMechanism contactMechanism : businessCustomer.getContactMechanisms()) {
        if (!partyReferenceService.isValidContactMechanismType(contactMechanism.getType())) {
          hibernateConstraintValidatorContext
              .addMessageParameter("contactMechanismType", contactMechanism.getType())
              .addMessageParameter("contactMechanismPurpose", contactMechanism.getPurpose())
              .buildConstraintViolationWithTemplate(
                  "{digital.inception.party.constraints.ValidBusinessCustomer.invalidContactMechanismPurposeCode.message}")
              .addConstraintViolation();

          isValid = false;
        }

        if (!partyReferenceService.isValidContactMechanismPurpose(
            businessCustomer.getPartyType().code(),
            contactMechanism.getType(),
            contactMechanism.getPurpose())) {
          hibernateConstraintValidatorContext
              .addMessageParameter("contactMechanismPurpose", contactMechanism.getPurpose())
              .addMessageParameter("partyType", businessCustomer.getPartyType().code())
              .buildConstraintViolationWithTemplate(
                  "{digital.inception.party.constraints.ValidBusinessCustomer.invalidContactMechanismPurposeCodeForPartyType.message}")
              .addConstraintViolation();

          isValid = false;
        }
      }

      // Validate countries of tax residence
      for (String countryOfTaxResidence : businessCustomer.getCountriesOfTaxResidence()) {
        if (!referenceService.isValidCountry(countryOfTaxResidence)) {
          hibernateConstraintValidatorContext
              .addMessageParameter("countryOfTaxResidence", countryOfTaxResidence)
              .buildConstraintViolationWithTemplate(
                  "{digital.inception.party.constraints.ValidBusinessCustomer.invalidCountryOfTaxResidenceCode.message}")
              .addConstraintViolation();

          isValid = false;
        }
      }

      // Validate identity documents
      for (IdentityDocument identityDocument : businessCustomer.getIdentityDocuments()) {
        if (!referenceService.isValidCountry(identityDocument.getCountryOfIssue())) {
          hibernateConstraintValidatorContext
              .addMessageParameter("countryOfIssue", identityDocument.getCountryOfIssue())
              .buildConstraintViolationWithTemplate(
                  "{digital.inception.party.constraints.ValidBusinessCustomer.invalidIdentityDocumentCountryOfIssueCode.message}")
              .addConstraintViolation();

          isValid = false;
        }

        if (!partyReferenceService.isValidIdentityDocumentType(
            businessCustomer.getPartyType().code(), identityDocument.getType())) {
          hibernateConstraintValidatorContext
              .addMessageParameter("type", identityDocument.getType())
              .buildConstraintViolationWithTemplate(
                  "{digital.inception.party.constraints.ValidBusinessCustomer.invalidIdentityDocumentTypeCode.message}")
              .addConstraintViolation();

          isValid = false;
        }
      }

      // Validate party roles
      for (PartyRole partyRole : businessCustomer.getRoles()) {
        if (!partyReferenceService.isValidPartyRoleType(
            businessCustomer.getPartyType().code(), partyRole.getType())) {
          hibernateConstraintValidatorContext
              .addMessageParameter("type", partyRole.getType())
              .buildConstraintViolationWithTemplate(
                  "{digital.inception.party.constraints.ValidBusinessCustomer.invalidPartyRoleTypeCode.message}")
              .addConstraintViolation();

          isValid = false;
        }
      }

      // Validate physical addresses
      for (PhysicalAddress physicalAddress : businessCustomer.getPhysicalAddresses()) {
        if (!partyReferenceService.isValidPhysicalAddressRole(
            businessCustomer.getPartyType().code(), physicalAddress.getRole())) {
          hibernateConstraintValidatorContext
              .addMessageParameter("physicalAddressRole", physicalAddress.getRole())
              .addMessageParameter("partyType", businessCustomer.getPartyType().code())
              .buildConstraintViolationWithTemplate(
                  "{digital.inception.party.constraints.ValidBusinessCustomer.invalidPhysicalAddressRoleCodeForPartyType.message}")
              .addConstraintViolation();

          isValid = false;
        }

        for (String physicalAddressPurpose : physicalAddress.getPurposes()) {
          if (!partyReferenceService.isValidPhysicalAddressPurpose(
              businessCustomer.getPartyType().code(), physicalAddressPurpose)) {
            hibernateConstraintValidatorContext
                .addMessageParameter("physicalAddressPurpose", physicalAddressPurpose)
                .addMessageParameter("partyType", businessCustomer.getPartyType().code())
                .buildConstraintViolationWithTemplate(
                    "{digital.inception.party.constraints.ValidBusinessCustomer.invalidPhysicalAddressPurposeCodeForPartyType.message}")
                .addConstraintViolation();

            isValid = false;
          }
        }
      }

      // Validate preferences
      for (Preference preference : businessCustomer.getPreferences()) {
        if (!partyReferenceService.isValidPreferenceType(
            businessCustomer.getPartyType().code(), preference.getType())) {
          hibernateConstraintValidatorContext
              .addMessageParameter("type", preference.getType())
              .buildConstraintViolationWithTemplate(
                  "{digital.inception.party.constraints.ValidBusinessCustomer.invalidPreferenceTypeCode.message}")
              .addConstraintViolation();

          isValid = false;
        }
      }

      // Validate tax numbers
      for (TaxNumber taxNumber : businessCustomer.getTaxNumbers()) {
        if (!referenceService.isValidCountry(taxNumber.getCountryOfIssue())) {
          hibernateConstraintValidatorContext
              .addMessageParameter("countryOfIssue", taxNumber.getCountryOfIssue())
              .buildConstraintViolationWithTemplate(
                  "{digital.inception.party.constraints.ValidBusinessCustomer.invalidTaxNumberCountryOfIssueCode.message}")
              .addConstraintViolation();

          isValid = false;
        }

        if (!partyReferenceService.isValidTaxNumberType(taxNumber.getType())) {
          hibernateConstraintValidatorContext
              .addMessageParameter("type", taxNumber.getType())
              .buildConstraintViolationWithTemplate(
                  "{digital.inception.party.constraints.ValidBusinessCustomer.invalidTaxNumberTypeCode.message}")
              .addConstraintViolation();

          isValid = false;
        }
      }

    } catch (Throwable e) {
      throw new ValidationException("Failed to validate the business customer", e);
    }

    return isValid;
  }
}
