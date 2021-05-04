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
import digital.inception.party.ExternalReference;
import digital.inception.party.IPartyReferenceService;
import digital.inception.party.IdentityDocument;
import digital.inception.party.Lock;
import digital.inception.party.Organization;
import digital.inception.party.PhysicalAddress;
import digital.inception.party.Preference;
import digital.inception.party.Role;
import digital.inception.party.RoleTypeAttributeTypeConstraint;
import digital.inception.party.RoleTypePreferenceTypeConstraint;
import digital.inception.party.SegmentAllocation;
import digital.inception.party.Status;
import digital.inception.party.TaxNumber;
import digital.inception.party.ValueType;
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
          boolean isReservedAttribute = false;

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

              isReservedAttribute = true;
              isValid = false;
              break;
            }
          }
          if (!isReservedAttribute) {
            if (!getPartyReferenceService()
                .isValidAttributeType(
                    organization.getTenantId(),
                    organization.getType().code(),
                    attribute.getType())) {
              hibernateConstraintValidatorContext
                  .addMessageParameter("attributeType", attribute.getType())
                  .buildConstraintViolationWithTemplate(
                      "{digital.inception.party.constraints.ValidOrganization.invalidAttributeType.message}")
                  .addPropertyNode("attributes")
                  .addPropertyNode("type")
                  .inIterable()
                  .addConstraintViolation();

              isValid = false;
            } else {
              if (!getPartyReferenceService()
                  .isValidMeasurementUnitForAttributeType(
                      organization.getTenantId(), attribute.getType(), attribute.getUnit())) {
                hibernateConstraintValidatorContext
                    .addMessageParameter(
                        "unit", (attribute.getUnit() != null) ? attribute.getUnit().code() : "")
                    .addMessageParameter("attributeType", attribute.getType())
                    .buildConstraintViolationWithTemplate(
                        "{digital.inception.party.constraints.ValidOrganization.invalidUnitForAttributeType.message}")
                    .addPropertyNode("attributes")
                    .addPropertyNode("unit")
                    .inIterable()
                    .addConstraintViolation();

                isValid = false;
              }

              Optional<ValueType> valueTypeOptional =
                  getPartyReferenceService().getAttributeTypeValueType(attribute.getType());

              if (valueTypeOptional.isPresent() && (!attribute.hasValue(valueTypeOptional.get()))) {
                hibernateConstraintValidatorContext
                    .addMessageParameter("valueType", valueTypeOptional.get().code())
                    .addMessageParameter("attributeType", attribute.getType())
                    .buildConstraintViolationWithTemplate(
                        "{digital.inception.party.constraints.ValidOrganization.invalidValueForAttributeType.message}")
                    .addPropertyNode("attributes")
                    .addPropertyNode(
                        Attribute.getAttributeNameForValueType(valueTypeOptional.get()))
                    .inIterable()
                    .addConstraintViolation();

                isValid = false;
              }
            }
          }
        }

        // Validate contact mechanisms
        for (ContactMechanism contactMechanism : organization.getContactMechanisms()) {
          if (StringUtils.hasText(contactMechanism.getType())) {
            //noinspection StatementWithEmptyBody
            if (!ContactMechanism.VALID_CONTACT_MECHANISM_TYPES.contains(
                contactMechanism.getType())) {
              // Do not add a constraint violation here as it would duplicate the regex validation
            } else {

              if (StringUtils.hasText(contactMechanism.getRole())) {
                if (!getPartyReferenceService()
                    .isValidContactMechanismRole(
                        organization.getTenantId(),
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
                        organization.getTenantId(),
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

        // Validate external references
        for (ExternalReference externalReference : organization.getExternalReferences()) {
          if (!getPartyReferenceService()
              .isValidExternalReferenceType(
                  organization.getTenantId(),
                  organization.getType().code(),
                  externalReference.getType())) {
            hibernateConstraintValidatorContext
                .addMessageParameter("externalReferenceType", externalReference.getType())
                .buildConstraintViolationWithTemplate(
                    "{digital.inception.party.constraints.ValidOrganization.invalidExternalReferenceType.message}")
                .addPropertyNode("externalReferences")
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
                    organization.getTenantId(),
                    organization.getType().code(),
                    identityDocument.getType())) {
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

        // Validate locks
        for (Lock lock : organization.getLocks()) {
          if (StringUtils.hasText(lock.getType())) {
            if (!getPartyReferenceService()
                .isValidLockType(
                    organization.getTenantId(), organization.getType().code(), lock.getType())) {
              hibernateConstraintValidatorContext
                  .addMessageParameter("lockType", lock.getType())
                  .addMessageParameter("partyType", organization.getType().code())
                  .buildConstraintViolationWithTemplate(
                      "{digital.inception.party.constraints.ValidOrganization.invalidLockTypeForPartyType.message}")
                  .addPropertyNode("locks")
                  .addPropertyNode("type")
                  .inIterable()
                  .addConstraintViolation();

              isValid = false;
            }
          }
        }

        // Validate physical addresses
        for (PhysicalAddress physicalAddress : organization.getPhysicalAddresses()) {
          if (!validatePhysicalAddress(
              organization.getTenantId(),
              organization.getType().code(),
              physicalAddress,
              hibernateConstraintValidatorContext)) {
            isValid = false;
          }
        }

        // Validate preferences
        for (Preference preference : organization.getPreferences()) {
          if (StringUtils.hasText(preference.getType())) {
            if (!getPartyReferenceService()
                .isValidPreferenceType(
                    organization.getTenantId(),
                    organization.getType().code(),
                    preference.getType())) {
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

        // Validate roles
        for (Role role : organization.getRoles()) {
          if (StringUtils.hasText(role.getType())) {
            if (!getPartyReferenceService()
                .isValidRoleType(
                    organization.getTenantId(), organization.getType().code(), role.getType())) {
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
              if (!validateRoleTypeAttributeTypeConstraintsForOrganizationWithRole(
                  organization, role.getType(), hibernateConstraintValidatorContext)) {
                isValid = false;
              }

              if (!validateRoleTypePreferenceTypeConstraintsForOrganizationWithRole(
                  organization, role.getType(), hibernateConstraintValidatorContext)) {
                isValid = false;
              }
            }
          }
        }

        // Validate segment allocations
        for (SegmentAllocation segmentAllocation : organization.getSegmentAllocations()) {
          if (StringUtils.hasText(segmentAllocation.getSegment())) {
            if (!getPartyReferenceService()
                .isValidSegment(organization.getTenantId(), segmentAllocation.getSegment())) {
              hibernateConstraintValidatorContext
                  .addMessageParameter("segment", segmentAllocation.getSegment())
                  .buildConstraintViolationWithTemplate(
                      "{digital.inception.party.constraints.ValidOrganization.invalidSegmentForSegmentAllocation.message}")
                  .addPropertyNode("segmentAllocations")
                  .addPropertyNode("segment")
                  .inIterable()
                  .addConstraintViolation();

              isValid = false;
            }
          }
        }

        // Validate statuses
        for (Status status : organization.getStatuses()) {
          if (StringUtils.hasText(status.getType())) {
            if (!getPartyReferenceService()
                .isValidStatusType(
                    organization.getTenantId(), organization.getType().code(), status.getType())) {
              hibernateConstraintValidatorContext
                  .addMessageParameter("statusType", status.getType())
                  .addMessageParameter("partyType", organization.getType().code())
                  .buildConstraintViolationWithTemplate(
                      "{digital.inception.party.constraints.ValidOrganization.invalidStatusTypeForPartyType.message}")
                  .addPropertyNode("statuses")
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
                .isValidTaxNumberType(
                    organization.getTenantId(),
                    organization.getType().code(),
                    taxNumber.getType())) {
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
      } catch (Throwable e) {
        throw new ValidationException("Failed to validate the organization", e);
      }

      return isValid;
    } else {
      return true;
    }
  }

  private boolean validateRoleTypeAttributeTypeConstraintsForOrganizationWithRole(
      Organization organization,
      String roleType,
      HibernateConstraintValidatorContext hibernateConstraintValidatorContext)
      throws Exception {
    boolean isValid = true;

    for (RoleTypeAttributeTypeConstraint roleTypeAttributeTypeConstraint :
        getPartyReferenceService().getRoleTypeAttributeTypeConstraints(roleType)) {

      if (roleTypeAttributeTypeConstraint.getType() == ConstraintType.MAX_SIZE) {
        Optional<Attribute> attributeOptional =
            organization.getAttributeWithType(roleTypeAttributeTypeConstraint.getAttributeType());

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
            organization.getAttributeWithType(roleTypeAttributeTypeConstraint.getAttributeType());

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
            organization.getAttributeWithType(roleTypeAttributeTypeConstraint.getAttributeType());

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
            organization.getAttributeWithType(roleTypeAttributeTypeConstraint.getAttributeType());

        if (attributeOptional.isPresent()) {
          if (!validateReferenceAttributeConstraint(
              organization.getTenantId(),
              roleTypeAttributeTypeConstraint.getValue(),
              attributeOptional.get(),
              hibernateConstraintValidatorContext)) {
            isValid = false;
          }
        }
      } else if (roleTypeAttributeTypeConstraint.getType() == ConstraintType.REQUIRED) {
        switch (roleTypeAttributeTypeConstraint.getAttributeType()) {
          case "contact_mechanism":
            if (!organization.hasContactMechanismWithType(
                roleTypeAttributeTypeConstraint.getAttributeTypeQualifier())) {
              hibernateConstraintValidatorContext
                  .addMessageParameter(
                      "contactMechanismType",
                      roleTypeAttributeTypeConstraint.getAttributeTypeQualifier())
                  .addMessageParameter("roleType", roleType)
                  .buildConstraintViolationWithTemplate(
                      "{digital.inception.party.constraints.ValidOrganization.contactMechanismTypeRequiredForRoleType.message}")
                  .addPropertyNode("contactMechanisms")
                  .addConstraintViolation();

              isValid = false;
            }

            break;

          case "contact_mechanisms":
            if (!validateRequiredAttributeConstraint(
                roleType,
                organization.getContactMechanisms(),
                "contactMechanisms",
                "{digital.inception.party.constraints.ValidOrganization.contactMechanismRequiredForRoleType.message}",
                hibernateConstraintValidatorContext)) {
              isValid = false;
            }

            break;

          case "countries_of_tax_residence":
            if (!validateRequiredAttributeConstraint(
                roleType,
                organization.getCountriesOfTaxResidence(),
                "countriesOfTaxResidence",
                "{digital.inception.party.constraints.ValidOrganization.countryOfTaxResidenceRequiredForRoleType.message}",
                hibernateConstraintValidatorContext)) {
              isValid = false;
            }

            break;

          case "external_reference":
            if (!organization.hasExternalReferenceWithType(
                roleTypeAttributeTypeConstraint.getAttributeTypeQualifier())) {
              hibernateConstraintValidatorContext
                  .addMessageParameter(
                      "externalReferenceType",
                      roleTypeAttributeTypeConstraint.getAttributeTypeQualifier())
                  .addMessageParameter("roleType", roleType)
                  .buildConstraintViolationWithTemplate(
                      "{digital.inception.party.constraints.ValidOrganization.externalReferenceTypeRequiredForRoleType.message}")
                  .addPropertyNode("externalReferences")
                  .addConstraintViolation();

              isValid = false;
            }

            break;

          case "identity_document":
            if (!organization.hasIdentityDocumentWithType(
                roleTypeAttributeTypeConstraint.getAttributeTypeQualifier())) {
              hibernateConstraintValidatorContext
                  .addMessageParameter(
                      "identityDocumentType",
                      roleTypeAttributeTypeConstraint.getAttributeTypeQualifier())
                  .addMessageParameter("roleType", roleType)
                  .buildConstraintViolationWithTemplate(
                      "{digital.inception.party.constraints.ValidOrganization.identityDocumentTypeRequiredForRoleType.message}")
                  .addPropertyNode("identityDocuments")
                  .addConstraintViolation();

              isValid = false;
            }

            break;

          case "identity_documents":
            if (!validateRequiredAttributeConstraint(
                roleType,
                organization.getIdentityDocuments(),
                "identityDocuments",
                "{digital.inception.party.constraints.ValidOrganization.identityDocumentRequiredForRoleType.message}",
                hibernateConstraintValidatorContext)) {
              isValid = false;
            }

            break;

          case "physical_addresses":
            if (!validateRequiredAttributeConstraint(
                roleType,
                organization.getPhysicalAddresses(),
                "physicalAddresses",
                "{digital.inception.party.constraints.ValidOrganization.physicalAddressRequiredForRoleType.message}",
                hibernateConstraintValidatorContext)) {
              isValid = false;
            }

            break;

          case "physical_address":
            if (!organization.hasPhysicalAddressWithRole(
                roleTypeAttributeTypeConstraint.getAttributeTypeQualifier())) {
              hibernateConstraintValidatorContext
                  .addMessageParameter(
                      "physicalAddressRole",
                      roleTypeAttributeTypeConstraint.getAttributeTypeQualifier())
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

          case "segment_allocations":
            if (!validateRequiredAttributeConstraint(
                roleType,
                organization.getSegmentAllocations(),
                "segmentAllocations",
                "{digital.inception.party.constraints.ValidOrganization.segmentAllocationRequiredForRoleType.message}",
                hibernateConstraintValidatorContext)) {
              isValid = false;
            }

            break;

          case "tax_numbers":
            if (!validateRequiredAttributeConstraint(
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
                organization.getAttributeWithType(
                    roleTypeAttributeTypeConstraint.getAttributeType());

            Optional<ValueType> valueTypeOptional =
                getPartyReferenceService()
                    .getAttributeTypeValueType(roleTypeAttributeTypeConstraint.getAttributeType());

            if (attributeOptional.isEmpty()
                || (valueTypeOptional.isPresent()
                    && (!attributeOptional.get().hasValue(valueTypeOptional.get())))) {
              hibernateConstraintValidatorContext
                  .addMessageParameter(
                      "attributeType", roleTypeAttributeTypeConstraint.getAttributeType())
                  .addMessageParameter(
                      "valueType",
                      valueTypeOptional.isPresent() ? valueTypeOptional.get().code() : "unknown")
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
            organization.getAttributeWithType(roleTypeAttributeTypeConstraint.getAttributeType());

        if (attributeOptional.isPresent()) {
          if (!validateSizeAttributeConstraint(
              roleTypeAttributeTypeConstraint.getIntegerValue(),
              attributeOptional.get(),
              hibernateConstraintValidatorContext)) {
            isValid = false;
          }
        }
      }
    }

    return isValid;
  }

  private boolean validateRoleTypePreferenceTypeConstraintsForOrganizationWithRole(
      Organization organization,
      String roleType,
      HibernateConstraintValidatorContext hibernateConstraintValidatorContext)
      throws Exception {
    boolean isValid = true;

    for (RoleTypePreferenceTypeConstraint roleTypePreferenceTypeConstraint :
        getPartyReferenceService().getRoleTypePreferenceTypeConstraints(roleType)) {

      if (roleTypePreferenceTypeConstraint.getType() == ConstraintType.MAX_SIZE) {
        Optional<Preference> preferenceOptional =
            organization.getPreferenceWithType(
                roleTypePreferenceTypeConstraint.getPreferenceType());

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
            organization.getPreferenceWithType(
                roleTypePreferenceTypeConstraint.getPreferenceType());

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
            organization.getPreferenceWithType(
                roleTypePreferenceTypeConstraint.getPreferenceType());

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
            organization.getPreferenceWithType(
                roleTypePreferenceTypeConstraint.getPreferenceType());

        if (preferenceOptional.isPresent()) {
          if (!validateReferencePreferenceConstraint(
              organization.getTenantId(),
              roleTypePreferenceTypeConstraint.getValue(),
              preferenceOptional.get(),
              hibernateConstraintValidatorContext)) {
            isValid = false;
          }
        }
      } else if (roleTypePreferenceTypeConstraint.getType() == ConstraintType.REQUIRED) {
        Optional<Preference> preferenceOptional =
            organization.getPreferenceWithType(
                roleTypePreferenceTypeConstraint.getPreferenceType());

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
            organization.getPreferenceWithType(
                roleTypePreferenceTypeConstraint.getPreferenceType());

        if (preferenceOptional.isPresent()) {
          if (!validateSizePreferenceConstraint(
              roleTypePreferenceTypeConstraint.getIntegerValue(),
              preferenceOptional.get(),
              hibernateConstraintValidatorContext)) {
            isValid = false;
          }
        }
      }
    }

    return isValid;
  }
}
