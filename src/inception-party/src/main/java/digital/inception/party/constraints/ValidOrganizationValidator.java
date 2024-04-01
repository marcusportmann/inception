/*
 * Copyright Marcus Portmann
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

import digital.inception.party.model.Attribute;
import digital.inception.party.model.AttributeType;
import digital.inception.party.model.ConstraintType;
import digital.inception.party.model.ContactMechanism;
import digital.inception.party.model.ContactMechanismRole;
import digital.inception.party.model.ContactMechanismType;
import digital.inception.party.model.ExternalReference;
import digital.inception.party.model.Identification;
import digital.inception.party.model.IndustryAllocation;
import digital.inception.party.model.Lock;
import digital.inception.party.model.Organization;
import digital.inception.party.model.PhysicalAddress;
import digital.inception.party.model.Preference;
import digital.inception.party.model.PreferenceType;
import digital.inception.party.model.Role;
import digital.inception.party.model.RoleTypeAttributeTypeConstraint;
import digital.inception.party.model.RoleTypePreferenceTypeConstraint;
import digital.inception.party.model.SegmentAllocation;
import digital.inception.party.model.Status;
import digital.inception.party.model.TaxNumber;
import digital.inception.party.model.ValueType;
import digital.inception.party.service.IPartyReferenceService;
import digital.inception.reference.service.IReferenceService;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.ValidationException;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.hibernate.validator.constraintvalidation.HibernateConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

/**
 * The <b>ValidOrganizationValidator</b> class implements the custom constraint validator for
 * validating an organization.
 *
 * @author Marcus Portmann
 */
@SuppressWarnings("unused")
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
            Optional<AttributeType> attributeTypeOptional =
                getPartyReferenceService()
                    .getAttributeType(
                        organization.getTenantId(),
                        organization.getType().code(),
                        attribute.getType());

            if (attributeTypeOptional.isPresent()) {
              AttributeType attributeType = attributeTypeOptional.get();

              if (attributeType.getUnitType() != null) {
                if ((attribute.getUnit() == null)
                    || (attribute.getUnit().getType() != attributeType.getUnitType())) {
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
              }

              switch (attributeType.getValueType()) {
                case BOOLEAN:
                  if (attribute.getBooleanValue() == null) {
                    hibernateConstraintValidatorContext
                        .addMessageParameter("attributeType", attribute.getType())
                        .buildConstraintViolationWithTemplate(
                            "{digital.inception.party.constraints.ValidOrganization.invalidAttributeWithNullBooleanValue.message}")
                        .addPropertyNode("attributes")
                        .addPropertyNode("booleanValue")
                        .addConstraintViolation();

                    isValid = false;
                  }

                  break;
                case DATE:
                  if (attribute.getDateValue() == null) {
                    hibernateConstraintValidatorContext
                        .addMessageParameter("attributeType", attribute.getType())
                        .buildConstraintViolationWithTemplate(
                            "{digital.inception.party.constraints.ValidOrganization.invalidAttributeWithNullDateValue.message}")
                        .addPropertyNode("attributes")
                        .addPropertyNode("dateValue")
                        .addConstraintViolation();

                    isValid = false;
                  }

                  break;
                case DECIMAL:
                  if (attribute.getDecimalValue() == null) {
                    hibernateConstraintValidatorContext
                        .addMessageParameter("attributeType", attribute.getType())
                        .buildConstraintViolationWithTemplate(
                            "{digital.inception.party.constraints.ValidOrganization.invalidAttributeWithNullDecimalValue.message}")
                        .addPropertyNode("attributes")
                        .addPropertyNode("decimalValue")
                        .addConstraintViolation();

                    isValid = false;
                  }

                  break;
                case DOUBLE:
                  if (attribute.getDoubleValue() == null) {
                    hibernateConstraintValidatorContext
                        .addMessageParameter("attributeType", attribute.getType())
                        .buildConstraintViolationWithTemplate(
                            "{digital.inception.party.constraints.ValidOrganization.invalidAttributeWithNullDoubleValue.message}")
                        .addPropertyNode("attributes")
                        .addPropertyNode("doubleValue")
                        .addConstraintViolation();

                    isValid = false;
                  }

                  break;
                case INTEGER:
                  if (attribute.getIntegerValue() == null) {
                    hibernateConstraintValidatorContext
                        .addMessageParameter("attributeType", attribute.getType())
                        .buildConstraintViolationWithTemplate(
                            "{digital.inception.party.constraints.ValidOrganization.invalidAttributeWithNullIntegerValue.message}")
                        .addPropertyNode("attributes")
                        .addPropertyNode("integerValue")
                        .addConstraintViolation();

                    isValid = false;
                  }

                  break;
                case STRING:
                  if (attribute.getStringValue() == null) {
                    hibernateConstraintValidatorContext
                        .addMessageParameter("attributeType", attribute.getType())
                        .buildConstraintViolationWithTemplate(
                            "{digital.inception.party.constraints.ValidOrganization.invalidAttributeWithNullStringValue.message}")
                        .addPropertyNode("attributes")
                        .addPropertyNode("stringValue")
                        .addConstraintViolation();

                    isValid = false;
                  } else {
                    if (StringUtils.hasText(attributeType.getPattern())) {
                      Pattern pattern = attributeType.getCompiledPattern();

                      Matcher matcher = pattern.matcher(attribute.getStringValue());

                      if (!matcher.matches()) {
                        hibernateConstraintValidatorContext
                            .addMessageParameter("attributeType", attribute.getType())
                            .addMessageParameter("stringValue", attribute.getStringValue())
                            .buildConstraintViolationWithTemplate(
                                "{digital.inception.party.constraints.ValidOrganization.invalidAttributeWithStringValue.message}")
                            .addPropertyNode("attributes")
                            .addPropertyNode("stringValue")
                            .addConstraintViolation();

                        isValid = false;
                      }
                    }
                  }

                  break;
              }
            } else {
              hibernateConstraintValidatorContext
                  .addMessageParameter("attributeType", attribute.getType())
                  .buildConstraintViolationWithTemplate(
                      "{digital.inception.party.constraints.ValidOrganization.invalidAttributeType.message}")
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
            Optional<ContactMechanismType> contactMechanismTypeOptional =
                getPartyReferenceService()
                    .getContactMechanismType(
                        organization.getTenantId(), contactMechanism.getType());

            if (contactMechanismTypeOptional.isPresent()) {
              ContactMechanismType contactMechanismType = contactMechanismTypeOptional.get();

              if (StringUtils.hasText(contactMechanism.getRole())) {
                Optional<ContactMechanismRole> contactMechanismRoleOptional =
                    getPartyReferenceService()
                        .getContactMechanismRole(
                            organization.getTenantId(),
                            organization.getType().code(),
                            contactMechanism.getType(),
                            contactMechanism.getRole());

                if (contactMechanismRoleOptional.isPresent()) {
                  ContactMechanismRole contactMechanismRole = contactMechanismRoleOptional.get();

                  /*
                   * The pattern specified for a contact mechanism role takes priority over the
                   * pattern specified for a contact mechanism type.
                   */
                  if (StringUtils.hasText(contactMechanism.getValue())) {
                    if (StringUtils.hasText(contactMechanismRole.getPattern())) {
                      Pattern pattern = contactMechanismRole.getCompiledPattern();

                      Matcher matcher = pattern.matcher(contactMechanism.getValue());

                      if (!matcher.matches()) {
                        hibernateConstraintValidatorContext
                            .addMessageParameter("contactMechanismType", contactMechanism.getType())
                            .addMessageParameter("contactMechanismRole", contactMechanism.getRole())
                            .addMessageParameter(
                                "contactMechanismValue", contactMechanism.getValue())
                            .buildConstraintViolationWithTemplate(
                                "{digital.inception.party.constraints.ValidOrganization.invalidContactMechanism.message}")
                            .addPropertyNode("contactMechanisms")
                            .addPropertyNode("value")
                            .addConstraintViolation();

                        isValid = false;
                      }
                    } else if (StringUtils.hasText(contactMechanismType.getPattern())) {

                      Pattern pattern = contactMechanismType.getCompiledPattern();

                      Matcher matcher = pattern.matcher(contactMechanism.getValue());

                      if (!matcher.matches()) {
                        hibernateConstraintValidatorContext
                            .addMessageParameter("contactMechanismType", contactMechanism.getType())
                            .addMessageParameter("contactMechanismRole", contactMechanism.getRole())
                            .addMessageParameter(
                                "contactMechanismValue", contactMechanism.getValue())
                            .buildConstraintViolationWithTemplate(
                                "{digital.inception.party.constraints.ValidOrganization.invalidContactMechanism.message}")
                            .addPropertyNode("contactMechanisms")
                            .addPropertyNode("value")
                            .addConstraintViolation();

                        isValid = false;
                      }
                    }
                  }
                } else {
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
            } else {
              hibernateConstraintValidatorContext
                  .addMessageParameter("contactMechanismType", contactMechanism.getType())
                  .buildConstraintViolationWithTemplate(
                      "{digital.inception.party.constraints.ValidOrganization.invalidContactMechanismType.message}")
                  .addPropertyNode("contactMechanisms")
                  .addPropertyNode("type")
                  .inIterable()
                  .addConstraintViolation();

              isValid = false;
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
              .isValidExternalReference(
                  organization.getTenantId(),
                  organization.getType().code(),
                  externalReference.getType(),
                  externalReference.getValue())) {
            hibernateConstraintValidatorContext
                .addMessageParameter("externalReferenceType", externalReference.getType())
                .addMessageParameter("externalReferenceValue", externalReference.getValue())
                .buildConstraintViolationWithTemplate(
                    "{digital.inception.party.constraints.ValidOrganization.invalidExternalReference.message}")
                .addPropertyNode("externalReferences")
                .addConstraintViolation();

            isValid = false;
          }
        }

        // Validate identification type
        if (StringUtils.hasText(organization.getIdentificationType())) {
          if (!getPartyReferenceService()
              .isValidIdentificationType(
                  organization.getTenantId(),
                  organization.getType().code(),
                  organization.getIdentificationType())) {
            hibernateConstraintValidatorContext
                .addMessageParameter("identificationType", organization.getIdentificationType())
                .addMessageParameter("partyType", organization.getType().code())
                .buildConstraintViolationWithTemplate(
                    "{digital.inception.party.constraints.ValidOrganization.invalidIdentificationTypeForPartyType.message}")
                .addPropertyNode("identificationType")
                .inIterable()
                .addConstraintViolation();

            isValid = false;
          } else {
            // Validate identification number
            if (StringUtils.hasText(organization.getIdentificationNumber())) {
              if (!getPartyReferenceService()
                  .isValidIdentification(
                      organization.getTenantId(),
                      organization.getType().code(),
                      organization.getIdentificationType(),
                      organization.getIdentificationNumber())) {
                hibernateConstraintValidatorContext
                    .addMessageParameter(
                        "identificationNumber", organization.getIdentificationNumber())
                    .addMessageParameter("identificationType", organization.getIdentificationType())
                    .buildConstraintViolationWithTemplate(
                        "{digital.inception.party.constraints.ValidOrganization.invalidIdentificationNumber.message}")
                    .addPropertyNode("identificationNumber")
                    .addConstraintViolation();

                isValid = false;
              }
            }
          }
        }

        // Validate identification country of issue
        if (StringUtils.hasText(organization.getIdentificationCountryOfIssue())
            && (!getReferenceService()
                .isValidCountry(organization.getIdentificationCountryOfIssue()))) {
          hibernateConstraintValidatorContext
              .addMessageParameter("countryOfIssue", organization.getIdentificationCountryOfIssue())
              .buildConstraintViolationWithTemplate(
                  "{digital.inception.party.constraints.ValidOrganization.invalidIdentificationCountryOfIssue.message}")
              .addPropertyNode("identificationCountryOfIssue")
              .addConstraintViolation();

          isValid = false;
        }

        // Validate identifications
        for (Identification identification : organization.getIdentifications()) {

          if (StringUtils.hasText(identification.getCountryOfIssue())) {
            if (!getReferenceService().isValidCountry(identification.getCountryOfIssue())) {
              hibernateConstraintValidatorContext
                  .addMessageParameter("countryOfIssue", identification.getCountryOfIssue())
                  .buildConstraintViolationWithTemplate(
                      "{digital.inception.party.constraints.ValidOrganization.invalidIdentificationCountryOfIssue.message}")
                  .addPropertyNode("identifications")
                  .addPropertyNode("countryOfIssue")
                  .inIterable()
                  .addConstraintViolation();

              isValid = false;
            }
          }

          if (StringUtils.hasText(identification.getType())) {
            if (!getPartyReferenceService()
                .isValidIdentificationType(
                    organization.getTenantId(),
                    organization.getType().code(),
                    identification.getType())) {
              hibernateConstraintValidatorContext
                  .addMessageParameter("identificationType", identification.getType())
                  .addMessageParameter("partyType", organization.getType().code())
                  .buildConstraintViolationWithTemplate(
                      "{digital.inception.party.constraints.ValidOrganization.invalidIdentificationTypeForPartyType.message}")
                  .addPropertyNode("identifications")
                  .addPropertyNode("type")
                  .inIterable()
                  .addConstraintViolation();

              isValid = false;
            }
          }

          if (StringUtils.hasText(identification.getNumber())) {
            if (!getPartyReferenceService()
                .isValidIdentification(
                    organization.getTenantId(),
                    organization.getType().code(),
                    identification.getType(),
                    identification.getNumber())) {
              hibernateConstraintValidatorContext
                  .addMessageParameter("identificationType", identification.getType())
                  .addMessageParameter("identificationNumber", identification.getNumber())
                  .buildConstraintViolationWithTemplate(
                      "{digital.inception.party.constraints.ValidOrganization.invalidIdentification.message}")
                  .addPropertyNode("identifications")
                  .addConstraintViolation();

              isValid = false;
            }
          }
        }

        // Validate industry allocations
        for (IndustryAllocation industryAllocation : organization.getIndustryAllocations()) {
          if (StringUtils.hasText(industryAllocation.getSystem())
              && StringUtils.hasText(industryAllocation.getIndustry())) {
            if (!getPartyReferenceService()
                .isValidIndustryClassification(
                    organization.getTenantId(),
                    industryAllocation.getSystem(),
                    industryAllocation.getIndustry())) {
              hibernateConstraintValidatorContext
                  .addMessageParameter(
                      "industryClassificationSystem", industryAllocation.getSystem())
                  .addMessageParameter("industryClassification", industryAllocation.getIndustry())
                  .buildConstraintViolationWithTemplate(
                      "{digital.inception.party.constraints.ValidOrganization.invalidIndustryAllocation.message}")
                  .addPropertyNode("industryAllocations")
                  .addPropertyNode("industry")
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
            Optional<PreferenceType> preferenceTypeOptional =
                getPartyReferenceService()
                    .getPreferenceType(
                        organization.getTenantId(),
                        organization.getType().code(),
                        preference.getType());

            if (preferenceTypeOptional.isPresent()) {
              PreferenceType preferenceType = preferenceTypeOptional.get();

              if (StringUtils.hasText(preferenceType.getPattern())) {
                Pattern pattern = preferenceType.getCompiledPattern();

                Matcher matcher = pattern.matcher(preference.getValue());

                if (!matcher.matches()) {
                  hibernateConstraintValidatorContext
                      .addMessageParameter("preferenceType", preference.getType())
                      .addMessageParameter("preferenceValue", preference.getValue())
                      .buildConstraintViolationWithTemplate(
                          "{digital.inception.party.constraints.ValidOrganization.invalidPreferenceValue.message}")
                      .addPropertyNode("preferences")
                      .addPropertyNode("value")
                      .addConstraintViolation();

                  isValid = false;
                }
              }
            } else {
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

          if (StringUtils.hasText(taxNumber.getNumber())) {
            if (!getPartyReferenceService()
                .isValidTaxNumber(
                    organization.getTenantId(),
                    organization.getType().code(),
                    taxNumber.getType(),
                    taxNumber.getNumber())) {
              hibernateConstraintValidatorContext
                  .addMessageParameter("taxNumberType", taxNumber.getType())
                  .addMessageParameter("taxNumberNumber", taxNumber.getNumber())
                  .buildConstraintViolationWithTemplate(
                      "{digital.inception.party.constraints.ValidOrganization.invalidTaxNumber.message}")
                  .addPropertyNode("taxNumbers")
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

          case "identification":
            if (!organization.hasIdentificationWithType(
                roleTypeAttributeTypeConstraint.getAttributeTypeQualifier())) {
              hibernateConstraintValidatorContext
                  .addMessageParameter(
                      "identificationType",
                      roleTypeAttributeTypeConstraint.getAttributeTypeQualifier())
                  .addMessageParameter("roleType", roleType)
                  .buildConstraintViolationWithTemplate(
                      "{digital.inception.party.constraints.ValidOrganization.identificationTypeRequiredForRoleType.message}")
                  .addPropertyNode("identifications")
                  .addConstraintViolation();

              isValid = false;
            }

            break;

          case "identifications":
            if (!validateRequiredAttributeConstraint(
                roleType,
                organization.getIdentifications(),
                "identifications",
                "{digital.inception.party.constraints.ValidOrganization.identificationRequiredForRoleType.message}",
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
