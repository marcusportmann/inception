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
import digital.inception.party.model.PhysicalAddress;
import digital.inception.party.model.PhysicalAddressType;
import digital.inception.party.model.Preference;
import digital.inception.party.service.IPartyReferenceService;
import digital.inception.reference.service.IReferenceService;
import jakarta.validation.ValidationException;
import java.time.LocalDate;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.regex.Pattern;
import org.hibernate.validator.constraintvalidation.HibernateConstraintValidatorContext;
import org.springframework.util.StringUtils;

/**
 * The <b>PartyValidator</b> class implements the base class that all party validators are derived
 * from, which provides common party validation functionality.
 *
 * @author Marcus Portmann
 */
public abstract class PartyValidator {

  /** The regex pattern used to validate e-mail addresses. */
  private static final String EMAIL_ADDRESS_PATTERN =
      "^$|(?:[a-zA-Z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\"
          + ".[a-zA-Z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d"
          + "-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-zA-Z0-9](?:[a-zA-Z0-9-]*[a-zA-Z0-9])?\\"
          + ".)+[a-zA-Z0-9](?:[a-zA-Z0-9-]*[a-zA-Z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}"
          + "(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-zA-Z0-9-]*[a-zA-Z0-9]:"
          + "(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e"
          + "-\\x7f])+)\\])";

  /** The regex pattern used to validate mobile numbers. */
  private static final String MOBILE_NUMBER_PATTERN =
      "^$|(\\+|00)(297|93|244|1264|358|355|376|971|54|374|1684|1268|61"
          + "|43|994|257|32|229|226|880|359|973|1242|387|590|375|501|1441|591|55|1246|673|975|267|236|1"
          + "|61|41|56|86|225|237|243|242|682|57|269|238|506|53|5999|61|1345|357|420|49|253|1767|45|1809"
          + "|1829|1849|213|593|20|291|212|34|372|251|358|679|500|33|298|691|241|44|995|44|233|350|224"
          + "|590|220|245|240|30|1473|299|502|594|1671|592|852|504|385|509|36|62|44|91|246|353|98|964|354"
          + "|972|39|1876|44|962|81|76|77|254|996|855|686|1869|82|383|965|856|961|231|218|1758|423|94|266"
          + "|370|352|371|853|590|212|377|373|261|960|52|692|389|223|356|95|382|976|1670|258|222|1664|596"
          + "|230|265|60|262|264|687|227|672|234|505|683|31|47|977|674|64|968|92|507|64|51|63|680|675|48"
          + "|1787|1939|850|351|595|970|689|974|262|40|7|250|966|249|221|65|500|4779|677|232|503|378|252"
          + "|508|381|211|239|597|421|386|46|268|1721|248|963|1649|235|228|66|992|690|993|670|676|1868"
          + "|216|90|688|886|255|256|380|598|1|998|3906698|379|1784|58|1284|1340|84|678|681|685|967|27"
          + "|260|263)(9[976]\\d|8[987530]\\d|6[987]\\d|5[90]\\d|42\\d|3[875]\\d|2[98654321]\\d|9[8543210"
          + "]|8[6421]|6[6543210]|5[87654321]|4[987654310]|3[9643210]|2[70]|7|1)\\d{4,20}$";

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
  public PartyValidator(
      IPartyReferenceService partyReferenceService, IReferenceService referenceService) {
    this.partyReferenceService = partyReferenceService;
    this.referenceService = referenceService;
  }

  /**
   * Returns the Party Reference Service
   *
   * @return the Party Reference Service
   */
  public IPartyReferenceService getPartyReferenceService() {
    return partyReferenceService;
  }

  /**
   * Returns the Reference Service
   *
   * @return the Reference Service
   */
  public IReferenceService getReferenceService() {
    return referenceService;
  }

  /**
   * Check whether the e-mail address is valid.
   *
   * @param emailAddress the e-mail address to validate
   * @return <b>true</b> if the e-mail address is valid or <b>false</b> otherwise
   */
  protected boolean isValidEmailAddress(String emailAddress) {
    if (StringUtils.hasText(emailAddress)) {
      return Pattern.matches(EMAIL_ADDRESS_PATTERN, emailAddress);
    } else {
      return false;
    }
  }

  /**
   * Check whether the mobile number is valid.
   *
   * @param mobileNumber the mobile number to validate
   * @return <b>true</b> if the mobile number is valid or <b>false</b> otherwise
   */
  protected boolean isValidMobileNumber(String mobileNumber) {
    if (StringUtils.hasText(mobileNumber)) {
      return Pattern.matches(MOBILE_NUMBER_PATTERN, mobileNumber);
    } else {
      return false;
    }
  }

  /**
   * Validate the maximum size attribute constraint.
   *
   * @param maximumSize the maximum size
   * @param attribute the attribute
   * @param hibernateConstraintValidatorContext the Hibernate constraint validator context
   * @return <b>true</b> if the attribute is valid or <b>false</b> otherwise
   */
  protected boolean validateMaximumSizeAttributeConstraint(
      int maximumSize,
      Attribute attribute,
      HibernateConstraintValidatorContext hibernateConstraintValidatorContext)
      throws ValidationException {
    if ((!StringUtils.hasText(attribute.getStringValue()))
        || (attribute.getStringValue().length() > maximumSize)) {
      hibernateConstraintValidatorContext
          .addMessageParameter(
              "value",
              StringUtils.hasText(attribute.getStringValue()) ? attribute.getStringValue() : "")
          .addMessageParameter("maximumSize", maximumSize)
          .addMessageParameter("attributeType", attribute.getType())
          .buildConstraintViolationWithTemplate(
              "{digital.inception.party.constraints.ValidParty.invalidMaximumSizeForAttributeType.message}")
          .addPropertyNode("attributes")
          .addPropertyNode("stringValue")
          .inIterable()
          .addConstraintViolation();

      return false;
    } else {
      return true;
    }
  }

  /**
   * Validate the maximum size preference constraint.
   *
   * @param maximumSize the maximum size
   * @param preference the preference
   * @param hibernateConstraintValidatorContext the Hibernate constraint validator context
   * @return <b>true</b> if the preference is valid or <b>false</b> otherwise
   */
  protected boolean validateMaximumSizePreferenceConstraint(
      int maximumSize,
      Preference preference,
      HibernateConstraintValidatorContext hibernateConstraintValidatorContext)
      throws ValidationException {
    if ((!StringUtils.hasText(preference.getValue()))
        || (preference.getValue().length() > maximumSize)) {
      hibernateConstraintValidatorContext
          .addMessageParameter(
              "value", StringUtils.hasText(preference.getValue()) ? preference.getValue() : "")
          .addMessageParameter("maximumSize", maximumSize)
          .addMessageParameter("preferenceType", preference.getType())
          .buildConstraintViolationWithTemplate(
              "{digital.inception.party.constraints.ValidParty.invalidMaximumSizeForPreferenceType.message}")
          .addPropertyNode("preferences")
          .addPropertyNode("value")
          .inIterable()
          .addConstraintViolation();

      return false;
    } else {
      return true;
    }
  }

  /**
   * Validate the minimum size attribute constraint.
   *
   * @param minimumSize the minimum size
   * @param attribute the attribute
   * @param hibernateConstraintValidatorContext the Hibernate constraint validator context
   * @return <b>true</b> if the attribute is valid or <b>false</b> otherwise
   */
  protected boolean validateMinimumSizeAttributeConstraint(
      int minimumSize,
      Attribute attribute,
      HibernateConstraintValidatorContext hibernateConstraintValidatorContext)
      throws ValidationException {
    if ((!StringUtils.hasText(attribute.getStringValue()))
        || (attribute.getStringValue().length() < minimumSize)) {
      hibernateConstraintValidatorContext
          .addMessageParameter(
              "value",
              StringUtils.hasText(attribute.getStringValue()) ? attribute.getStringValue() : "")
          .addMessageParameter("minimumSize", minimumSize)
          .addMessageParameter("attributeType", attribute.getType())
          .buildConstraintViolationWithTemplate(
              "{digital.inception.party.constraints.ValidParty.invalidMinimumSizeForAttributeType.message}")
          .addPropertyNode("attributes")
          .addPropertyNode("stringValue")
          .inIterable()
          .addConstraintViolation();

      return false;
    } else {
      return true;
    }
  }

  /**
   * Validate the minimum size preference constraint.
   *
   * @param minimumSize the minimum size
   * @param preference the preference
   * @param hibernateConstraintValidatorContext the Hibernate constraint validator context
   * @return <b>true</b> if the preference is valid or <b>false</b> otherwise
   */
  protected boolean validateMinimumSizePreferenceConstraint(
      int minimumSize,
      Preference preference,
      HibernateConstraintValidatorContext hibernateConstraintValidatorContext)
      throws ValidationException {
    if ((!StringUtils.hasText(preference.getValue()))
        || (preference.getValue().length() < minimumSize)) {
      hibernateConstraintValidatorContext
          .addMessageParameter(
              "value", StringUtils.hasText(preference.getValue()) ? preference.getValue() : "")
          .addMessageParameter("minimumSize", minimumSize)
          .addMessageParameter("preferenceType", preference.getType())
          .buildConstraintViolationWithTemplate(
              "{digital.inception.party.constraints.ValidParty.invalidMinimumSizeForPreferenceType.message}")
          .addPropertyNode("preferences")
          .addPropertyNode("value")
          .inIterable()
          .addConstraintViolation();

      return false;
    } else {
      return true;
    }
  }

  /**
   * Validate the pattern attribute constraint.
   *
   * @param pattern the pattern
   * @param attribute the attribute
   * @param hibernateConstraintValidatorContext the Hibernate constraint validator context
   * @return <b>true</b> if the attribute is valid or <b>false</b> otherwise
   */
  protected boolean validatePatternAttributeConstraint(
      String pattern,
      Attribute attribute,
      HibernateConstraintValidatorContext hibernateConstraintValidatorContext)
      throws ValidationException {
    if (StringUtils.hasText(attribute.getStringValue())) {
      if (!Pattern.matches(pattern, attribute.getStringValue())) {

        hibernateConstraintValidatorContext
            .addMessageParameter(
                "value",
                StringUtils.hasText(attribute.getStringValue()) ? attribute.getStringValue() : "")
            .addMessageParameter("attributeType", attribute.getType())
            .buildConstraintViolationWithTemplate(
                "{digital.inception.party.constraints.ValidParty.patternMatchFailedForAttributeType.message}")
            .addPropertyNode("attributes")
            .addPropertyNode("stringValue")
            .inIterable()
            .addConstraintViolation();

        return false;
      } else {
        return true;
      }
    } else {
      return true;
    }
  }

  /**
   * Validate the pattern preference constraint.
   *
   * @param pattern the pattern
   * @param preference the preference
   * @param hibernateConstraintValidatorContext the Hibernate constraint validator context
   * @return <b>true</b> if the preference is valid or <b>false</b> otherwise
   */
  protected boolean validatePatternPreferenceConstraint(
      String pattern,
      Preference preference,
      HibernateConstraintValidatorContext hibernateConstraintValidatorContext)
      throws ValidationException {
    if (StringUtils.hasText(preference.getValue())) {
      if (!Pattern.matches(pattern, preference.getValue())) {

        hibernateConstraintValidatorContext
            .addMessageParameter(
                "value", StringUtils.hasText(preference.getValue()) ? preference.getValue() : "")
            .addMessageParameter("preferenceType", preference.getType())
            .buildConstraintViolationWithTemplate(
                "{digital.inception.party.constraints.ValidParty.patternMatchFailedForPreferenceType.message}")
            .addPropertyNode("preferences")
            .addPropertyNode("value")
            .inIterable()
            .addConstraintViolation();

        return false;
      } else {
        return true;
      }
    } else {
      return true;
    }
  }

  /**
   * Validate the physical address.
   *
   * @param tenantId the ID for the tenant the party is associated with
   * @param partyType the code for the party type
   * @param physicalAddress the physical address
   * @param hibernateConstraintValidatorContext the Hibernate constraint validator context
   * @return <b>true</b> if the physical address is valid or <b>false</b> otherwise
   */
  protected boolean validatePhysicalAddress(
      UUID tenantId,
      String partyType,
      PhysicalAddress physicalAddress,
      HibernateConstraintValidatorContext hibernateConstraintValidatorContext) {
    /*
    The following validation rules are applied for the different address types:

    +----------------------+-----------+----------+----------+---------------+----------+----------+----------+--------------+
    |                      |  Building | Complex  |   Farm   | International |  Postal  |   Site   |  Street  | Unstructured |
    +----------------------+-----------+----------+----------+---------------+----------+----------+----------+--------------+
    | Building Floor       | Optional  | Invalid  | Invalid  | Invalid       | Invalid  | Invalid  | Invalid  | Invalid      |
    | Building Name        | Required  | Invalid  | Invalid  | Invalid       | Invalid  | Invalid  | Invalid  | Invalid      |
    | Building Room        | Optional  | Invalid  | Invalid  | Invalid       | Invalid  | Invalid  | Invalid  | Invalid      |
    | City                 | Required  | Required | Optional | Optional      | Required | Required | Required | Optional     |
    | Complex Name         | Invalid   | Required | Invalid  | Invalid       | Invalid  | Invalid  | Invalid  | Invalid      |
    | Complex Unit Number  | Invalid   | Required | Invalid  | Invalid       | Invalid  | Invalid  | Invalid  | Invalid      |
    | Country              | Required  | Required | Required | Required      | Required | Required | Required | Required     |
    | Farm Description     | Invalid   | Invalid  | Optional | Invalid       | Invalid  | Invalid  | Invalid  | Invalid      |
    | Farm Name            | Invalid   | Invalid  | Optional | Invalid       | Invalid  | Invalid  | Invalid  | Invalid      |
    | Farm Number          | Invalid   | Invalid  | Required | Required      | Invalid  | Invalid  | Invalid  | Invalid      |
    | Line 1               | Invalid   | Invalid  | Invalid  | Required      | Required | Invalid  | Invalid  | Required     |
    | Line 2               | Invalid   | Invalid  | Invalid  | Optional      | Optional | Invalid  | Invalid  | Optional     |
    | Line 3               | Invalid   | Invalid  | Invalid  | Optional      | Optional | Invalid  | Invalid  | Optional     |
    | Line 4               | Invalid   | Invalid  | Invalid  | Optional      | Optional | Invalid  | Invalid  | Optional     |
    | Postal Code Required | Required  | Required | Required | Required      | Required | Required | Required | Required     |
    | Region               | Optional  | Optional | Optional | Optional      | Optional | Optional | Optional | Optional     |
    | Site Block           | Invalid   | Invalid  | Invalid  | Invalid       | Invalid  | Required | Invalid  | Invalid      |
    | Site Number          | Invalid   | Invalid  | Invalid  | Invalid       | Invalid  | Required | Invalid  | Invalid      |
    | Street Name          | Required  | Required | Optional | Invalid       | Invalid  | Optional | Required | Invalid      |
    | Street Number        | Optional  | Optional | Optional | Invalid       | Invalid  | Optional | Optional | Invalid      |
    | Suburb               | Optional  | Optional | Optional | Invalid       | Optional | Invalid  | Optional | Invalid      |
    +----------------------+-----------+----------+----------+---------------+----------+----------+----------+--------------+
    */

    boolean isValid = true;

    try {
      if (!partyReferenceService.isValidPhysicalAddressType(tenantId, physicalAddress.getType())) {
        hibernateConstraintValidatorContext
            .addMessageParameter("type", physicalAddress.getType())
            .buildConstraintViolationWithTemplate(
                "{digital.inception.party.constraints.ValidPhysicalAddress.invalidType.message}")
            .addPropertyNode("physicalAddresses")
            .addPropertyNode("type")
            .addConstraintViolation();

        isValid = false;
      } else {
        switch (physicalAddress.getType()) {
            // Validate a building address
          case PhysicalAddressType.BUILDING:
            if (!StringUtils.hasText(physicalAddress.getBuildingName())) {
              hibernateConstraintValidatorContext
                  .buildConstraintViolationWithTemplate(
                      "{digital.inception.party.constraints.ValidPhysicalAddress.buildingNameRequired.message}")
                  .addPropertyNode("physicalAddresses")
                  .addPropertyNode("buildingName")
                  .addConstraintViolation();

              isValid = false;
            }

            if (!StringUtils.hasText(physicalAddress.getCity())) {
              hibernateConstraintValidatorContext
                  .buildConstraintViolationWithTemplate(
                      "{digital.inception.party.constraints.ValidPhysicalAddress.cityRequired.message}")
                  .addPropertyNode("physicalAddresses")
                  .addPropertyNode("city")
                  .addConstraintViolation();

              isValid = false;
            }

            if (!StringUtils.hasText(physicalAddress.getStreetName())) {
              hibernateConstraintValidatorContext
                  .buildConstraintViolationWithTemplate(
                      "{digital.inception.party.constraints.ValidPhysicalAddress.streetNameRequired.message}")
                  .addPropertyNode("physicalAddresses")
                  .addPropertyNode("streetName")
                  .addConstraintViolation();

              isValid = false;
            }
            break;
            // Validate a complex address
          case PhysicalAddressType.COMPLEX:
            if (!StringUtils.hasText(physicalAddress.getCity())) {
              hibernateConstraintValidatorContext
                  .buildConstraintViolationWithTemplate(
                      "{digital.inception.party.constraints.ValidPhysicalAddress.cityRequired.message}")
                  .addPropertyNode("physicalAddresses")
                  .addPropertyNode("city")
                  .addConstraintViolation();

              isValid = false;
            }

            if (!StringUtils.hasText(physicalAddress.getComplexName())) {
              hibernateConstraintValidatorContext
                  .buildConstraintViolationWithTemplate(
                      "{digital.inception.party.constraints.ValidPhysicalAddress.complexNameRequired.message}")
                  .addPropertyNode("physicalAddresses")
                  .addPropertyNode("complexName")
                  .addConstraintViolation();

              isValid = false;
            }

            if (!StringUtils.hasText(physicalAddress.getComplexUnitNumber())) {
              hibernateConstraintValidatorContext
                  .buildConstraintViolationWithTemplate(
                      "{digital.inception.party.constraints.ValidPhysicalAddress.complexUnitNumberRequired.message}")
                  .addPropertyNode("physicalAddresses")
                  .addPropertyNode("complexNumber")
                  .addConstraintViolation();

              isValid = false;
            }

            if (!StringUtils.hasText(physicalAddress.getStreetName())) {
              hibernateConstraintValidatorContext
                  .buildConstraintViolationWithTemplate(
                      "{digital.inception.party.constraints.ValidPhysicalAddress.streetNameRequired.message}")
                  .addPropertyNode("physicalAddresses")
                  .addPropertyNode("streetName")
                  .addConstraintViolation();

              isValid = false;
            }
            break;
            // Validate a farm address
          case PhysicalAddressType.FARM:
            if (!StringUtils.hasText(physicalAddress.getFarmNumber())) {
              hibernateConstraintValidatorContext
                  .buildConstraintViolationWithTemplate(
                      "{digital.inception.party.constraints.ValidPhysicalAddress.farmNumberRequired.message}")
                  .addPropertyNode("physicalAddresses")
                  .addPropertyNode("farmNumber")
                  .addConstraintViolation();

              isValid = false;
            }
            break;
            // Validate an international address
          case PhysicalAddressType.INTERNATIONAL:
            if (!StringUtils.hasText(physicalAddress.getLine1())) {
              hibernateConstraintValidatorContext
                  .buildConstraintViolationWithTemplate(
                      "{digital.inception.party.constraints.ValidPhysicalAddress.line1InternationalRequired.message}")
                  .addPropertyNode("physicalAddresses")
                  .addPropertyNode("line1")
                  .addConstraintViolation();

              isValid = false;
            }

            if (StringUtils.hasText(physicalAddress.getStreetName())) {
              hibernateConstraintValidatorContext
                  .buildConstraintViolationWithTemplate(
                      "{digital.inception.party.constraints.ValidPhysicalAddress.streetNameNotSupported.message}")
                  .addPropertyNode("physicalAddresses")
                  .addPropertyNode("streetName")
                  .addConstraintViolation();

              isValid = false;
            }

            if (StringUtils.hasText(physicalAddress.getStreetNumber())) {
              hibernateConstraintValidatorContext
                  .buildConstraintViolationWithTemplate(
                      "{digital.inception.party.constraints.ValidPhysicalAddress.streetNumberNotSupported.message}")
                  .addPropertyNode("physicalAddresses")
                  .addPropertyNode("streetNumber")
                  .addConstraintViolation();

              isValid = false;
            }

            if (StringUtils.hasText(physicalAddress.getSuburb())) {
              hibernateConstraintValidatorContext
                  .buildConstraintViolationWithTemplate(
                      "{digital.inception.party.constraints.ValidPhysicalAddress.suburbNotSupported.message}")
                  .addPropertyNode("physicalAddresses")
                  .addPropertyNode("suburb")
                  .addConstraintViolation();

              isValid = false;
            }
            break;
            // Validate a postal address
          case PhysicalAddressType.POSTAL:
            if (!StringUtils.hasText(physicalAddress.getLine1())) {
              hibernateConstraintValidatorContext
                  .buildConstraintViolationWithTemplate(
                      "{digital.inception.party.constraints.ValidPhysicalAddress.line1PostalRequired.message}")
                  .addPropertyNode("physicalAddresses")
                  .addPropertyNode("line1")
                  .addConstraintViolation();

              isValid = false;
            }

            if (StringUtils.hasText(physicalAddress.getStreetName())) {
              hibernateConstraintValidatorContext
                  .buildConstraintViolationWithTemplate(
                      "{digital.inception.party.constraints.ValidPhysicalAddress.streetNameNotSupported.message}")
                  .addPropertyNode("physicalAddresses")
                  .addPropertyNode("streetName")
                  .addConstraintViolation();

              isValid = false;
            }

            if (StringUtils.hasText(physicalAddress.getStreetNumber())) {
              hibernateConstraintValidatorContext
                  .buildConstraintViolationWithTemplate(
                      "{digital.inception.party.constraints.ValidPhysicalAddress.streetNumberNotSupported.message}")
                  .addPropertyNode("physicalAddresses")
                  .addPropertyNode("streetNumber")
                  .addConstraintViolation();

              isValid = false;
            }
            break;
            // Validate a site address
          case PhysicalAddressType.SITE:
            if (!StringUtils.hasText(physicalAddress.getCity())) {
              hibernateConstraintValidatorContext
                  .buildConstraintViolationWithTemplate(
                      "{digital.inception.party.constraints.ValidPhysicalAddress.cityRequired.message}")
                  .addPropertyNode("physicalAddresses")
                  .addPropertyNode("city")
                  .addConstraintViolation();

              isValid = false;
            }

            if (!StringUtils.hasText(physicalAddress.getSiteBlock())) {
              hibernateConstraintValidatorContext
                  .buildConstraintViolationWithTemplate(
                      "{digital.inception.party.constraints.ValidPhysicalAddress.siteBlockRequired.message}")
                  .addPropertyNode("physicalAddresses")
                  .addPropertyNode("siteBlock")
                  .addConstraintViolation();

              isValid = false;
            }

            if (!StringUtils.hasText(physicalAddress.getSiteNumber())) {
              hibernateConstraintValidatorContext
                  .buildConstraintViolationWithTemplate(
                      "{digital.inception.party.constraints.ValidPhysicalAddress.siteNumberRequired.message}")
                  .addPropertyNode("physicalAddresses")
                  .addPropertyNode("siteNumber")
                  .addConstraintViolation();

              isValid = false;
            }
            break;
            // Validate a street address
          case PhysicalAddressType.STREET:
            if (!StringUtils.hasText(physicalAddress.getCity())) {
              hibernateConstraintValidatorContext
                  .buildConstraintViolationWithTemplate(
                      "{digital.inception.party.constraints.ValidPhysicalAddress.cityRequired.message}")
                  .addPropertyNode("physicalAddresses")
                  .addPropertyNode("city")
                  .addConstraintViolation();

              isValid = false;
            }

            if (!StringUtils.hasText(physicalAddress.getStreetName())) {
              hibernateConstraintValidatorContext
                  .buildConstraintViolationWithTemplate(
                      "{digital.inception.party.constraints.ValidPhysicalAddress.streetNameRequired.message}")
                  .addPropertyNode("physicalAddresses")
                  .addPropertyNode("streetName")
                  .addConstraintViolation();

              isValid = false;
            }

            //      if (!StringUtils.hasText(physicalAddress.getStreetNumber())) {
            //        context.buildConstraintViolationWithTemplate(
            //
            // "{digital.inception.party.constraints.ValidPhysicalAddress.streetNumberRequired.message}")
            //            .addConstraintViolation();
            //
            //        isValid = false;
            //      }
            break;
            // Validate an unstructured address
          case PhysicalAddressType.UNSTRUCTURED:
            if (!StringUtils.hasText(physicalAddress.getLine1())) {
              hibernateConstraintValidatorContext
                  .buildConstraintViolationWithTemplate(
                      "{digital.inception.party.constraints.ValidPhysicalAddress.line1UnstructuredRequired.message}")
                  .addPropertyNode("physicalAddresses")
                  .addPropertyNode("line1")
                  .addConstraintViolation();

              isValid = false;
            }

            if (StringUtils.hasText(physicalAddress.getStreetName())) {
              hibernateConstraintValidatorContext
                  .buildConstraintViolationWithTemplate(
                      "{digital.inception.party.constraints.ValidPhysicalAddress.streetNameNotSupported.message}")
                  .addPropertyNode("physicalAddresses")
                  .addPropertyNode("streetName")
                  .addConstraintViolation();

              isValid = false;
            }

            if (StringUtils.hasText(physicalAddress.getStreetNumber())) {
              hibernateConstraintValidatorContext
                  .buildConstraintViolationWithTemplate(
                      "{digital.inception.party.constraints.ValidPhysicalAddress.streetNumberNotSupported.message}")
                  .addPropertyNode("physicalAddresses")
                  .addPropertyNode("streetNumber")
                  .addConstraintViolation();

              isValid = false;
            }

            if (StringUtils.hasText(physicalAddress.getSuburb())) {
              hibernateConstraintValidatorContext
                  .buildConstraintViolationWithTemplate(
                      "{digital.inception.party.constraints.ValidPhysicalAddress.suburbNotSupported.message}")
                  .addPropertyNode("physicalAddresses")
                  .addPropertyNode("suburb")
                  .addConstraintViolation();

              isValid = false;
            }
            break;
        }

        if (!StringUtils.hasText(physicalAddress.getCountry())) {
          hibernateConstraintValidatorContext
              .buildConstraintViolationWithTemplate(
                  "{digital.inception.party.constraints.ValidPhysicalAddress.countryRequired.message}")
              .addPropertyNode("physicalAddresses")
              .addPropertyNode("country")
              .addConstraintViolation();

          isValid = false;
        }

        if (!StringUtils.hasText(physicalAddress.getPostalCode())) {
          hibernateConstraintValidatorContext
              .buildConstraintViolationWithTemplate(
                  "{digital.inception.party.constraints.ValidPhysicalAddress.postalCodeRequired.message}")
              .addPropertyNode("physicalAddresses")
              .addPropertyNode("postalCode")
              .addConstraintViolation();

          isValid = false;
        }

        if (StringUtils.hasText(physicalAddress.getSuburb())
            && (!StringUtils.hasText(physicalAddress.getCity()))) {
          hibernateConstraintValidatorContext
              .buildConstraintViolationWithTemplate(
                  "{digital.inception.party.constraints.ValidPhysicalAddress.cityRequired.message}")
              .addPropertyNode("physicalAddresses")
              .addPropertyNode("city")
              .addConstraintViolation();

          isValid = false;
        }

        /*
         * Check that building address fields have not been specified for an address that is not a
         * building address.
         */
        if (!physicalAddress.getType().equals(PhysicalAddressType.BUILDING)) {
          if (StringUtils.hasText(physicalAddress.getBuildingFloor())) {
            hibernateConstraintValidatorContext
                .buildConstraintViolationWithTemplate(
                    "{digital.inception.party.constraints.ValidPhysicalAddress.buildingFloorNotSupported.message}")
                .addPropertyNode("physicalAddresses")
                .addPropertyNode("buildingFloor")
                .addConstraintViolation();

            isValid = false;
          }

          if (StringUtils.hasText(physicalAddress.getBuildingName())) {
            hibernateConstraintValidatorContext
                .buildConstraintViolationWithTemplate(
                    "{digital.inception.party.constraints.ValidPhysicalAddress.buildingNameNotSupported.message}")
                .addPropertyNode("physicalAddresses")
                .addPropertyNode("buildingName")
                .addConstraintViolation();

            isValid = false;
          }

          if (StringUtils.hasText(physicalAddress.getBuildingRoom())) {
            hibernateConstraintValidatorContext
                .buildConstraintViolationWithTemplate(
                    "{digital.inception.party.constraints.ValidPhysicalAddress.buildingRoomNotSupported.message}")
                .addPropertyNode("physicalAddresses")
                .addPropertyNode("buildingRoom")
                .addConstraintViolation();

            isValid = false;
          }
        }

        /*
         * Check that complex address fields have not been specified for an address that is not a
         * complex address.
         */
        if (!physicalAddress.getType().equals(PhysicalAddressType.COMPLEX)) {
          if (StringUtils.hasText(physicalAddress.getComplexName())) {
            hibernateConstraintValidatorContext
                .buildConstraintViolationWithTemplate(
                    "{digital.inception.party.constraints.ValidPhysicalAddress.complexNameNotSupported.message}")
                .addPropertyNode("physicalAddresses")
                .addPropertyNode("complexName")
                .addConstraintViolation();

            isValid = false;
          }

          if (StringUtils.hasText(physicalAddress.getComplexUnitNumber())) {
            hibernateConstraintValidatorContext
                .buildConstraintViolationWithTemplate(
                    "{digital.inception.party.constraints.ValidPhysicalAddress.complexUnitNumberNotSupported.message}")
                .addPropertyNode("physicalAddresses")
                .addPropertyNode("complexUnitNumber")
                .addConstraintViolation();

            isValid = false;
          }
        }

        /*
         * Check that farm address fields have not been specified for an address that is not a
         * farm address.
         */
        if (!physicalAddress.getType().equals(PhysicalAddressType.FARM)) {
          if (StringUtils.hasText(physicalAddress.getFarmDescription())) {
            hibernateConstraintValidatorContext
                .buildConstraintViolationWithTemplate(
                    "{digital.inception.party.constraints.ValidPhysicalAddress.farmDescriptionNotSupported.message}")
                .addPropertyNode("physicalAddresses")
                .addPropertyNode("farmDescription")
                .addConstraintViolation();

            isValid = false;
          }

          if (StringUtils.hasText(physicalAddress.getFarmName())) {
            hibernateConstraintValidatorContext
                .buildConstraintViolationWithTemplate(
                    "{digital.inception.party.constraints.ValidPhysicalAddress.farmNameNotSupported.message}")
                .addPropertyNode("physicalAddresses")
                .addPropertyNode("farmName")
                .addConstraintViolation();

            isValid = false;
          }

          if (StringUtils.hasText(physicalAddress.getFarmNumber())) {
            hibernateConstraintValidatorContext
                .buildConstraintViolationWithTemplate(
                    "{digital.inception.party.constraints.ValidPhysicalAddress.farmNumberNotSupported.message}")
                .addPropertyNode("physicalAddresses")
                .addPropertyNode("farmNumber")
                .addConstraintViolation();

            isValid = false;
          }
        }

        /*
         * Check that site address fields have not been specified for an address that is not a
         * site address.
         */
        if (!physicalAddress.getType().equals(PhysicalAddressType.SITE)) {
          if (StringUtils.hasText(physicalAddress.getSiteBlock())) {
            hibernateConstraintValidatorContext
                .buildConstraintViolationWithTemplate(
                    "{digital.inception.party.constraints.ValidPhysicalAddress.siteBlockNotSupported.message}")
                .addPropertyNode("physicalAddresses")
                .addPropertyNode("siteBlock")
                .addConstraintViolation();

            isValid = false;
          }

          if (StringUtils.hasText(physicalAddress.getSiteNumber())) {
            hibernateConstraintValidatorContext
                .buildConstraintViolationWithTemplate(
                    "{digital.inception.party.constraints.ValidPhysicalAddress.siteNumberNotSupported.message}")
                .addPropertyNode("physicalAddresses")
                .addPropertyNode("siteNumber")
                .addConstraintViolation();

            isValid = false;
          }
        }

        /*
         * Check that international, postal and unstructured address fields have not been specified for
         * an address that is not an international address, postal address or unstructured address.
         */
        if ((!physicalAddress.getType().equals(PhysicalAddressType.INTERNATIONAL))
            && (!physicalAddress.getType().equals(PhysicalAddressType.POSTAL))
            && (!Objects.equals(physicalAddress.getType(), PhysicalAddressType.UNSTRUCTURED))) {
          if (StringUtils.hasText(physicalAddress.getLine1())) {
            hibernateConstraintValidatorContext
                .buildConstraintViolationWithTemplate(
                    "{digital.inception.party.constraints.ValidPhysicalAddress.line1NotSupported.message}")
                .addPropertyNode("physicalAddresses")
                .addPropertyNode("line1")
                .addConstraintViolation();

            isValid = false;
          }

          if (StringUtils.hasText(physicalAddress.getLine2())) {
            hibernateConstraintValidatorContext
                .buildConstraintViolationWithTemplate(
                    "{digital.inception.party.constraints.ValidPhysicalAddress.line2NotSupported.message}")
                .addPropertyNode("physicalAddresses")
                .addPropertyNode("line2")
                .addConstraintViolation();

            isValid = false;
          }

          if (StringUtils.hasText(physicalAddress.getLine3())) {
            hibernateConstraintValidatorContext
                .buildConstraintViolationWithTemplate(
                    "{digital.inception.party.constraints.ValidPhysicalAddress.line3NotSupported.message}")
                .addPropertyNode("physicalAddresses")
                .addPropertyNode("line3")
                .addConstraintViolation();

            isValid = false;
          }

          if (StringUtils.hasText(physicalAddress.getLine4())) {
            hibernateConstraintValidatorContext
                .buildConstraintViolationWithTemplate(
                    "{digital.inception.party.constraints.ValidPhysicalAddress.line4NotSupported.message}")
                .addPropertyNode("physicalAddresses")
                .addPropertyNode("line4")
                .addConstraintViolation();

            isValid = false;
          }
        }

        if (StringUtils.hasText(physicalAddress.getCountry())
            && (!referenceService.isValidCountry(physicalAddress.getCountry()))) {
          hibernateConstraintValidatorContext
              .addMessageParameter("country", physicalAddress.getCountry())
              .buildConstraintViolationWithTemplate(
                  "{digital.inception.party.constraints.ValidPhysicalAddress.invalidCountryCode.message}")
              .addPropertyNode("physicalAddresses")
              .addPropertyNode("country")
              .addConstraintViolation();

          isValid = false;
        }
      }

      if (StringUtils.hasText(physicalAddress.getRole())) {
        if (!getPartyReferenceService()
            .isValidPhysicalAddressRole(tenantId, partyType, physicalAddress.getRole())) {
          hibernateConstraintValidatorContext
              .addMessageParameter("role", physicalAddress.getRole())
              .addMessageParameter("partyType", partyType)
              .buildConstraintViolationWithTemplate(
                  "{digital.inception.party.constraints.ValidPhysicalAddress.invalidRoleForPartyType.message}")
              .addPropertyNode("physicalAddresses")
              .addPropertyNode("role")
              .inIterable()
              .addConstraintViolation();

          isValid = false;
        }
      }

      for (String physicalAddressPurpose : physicalAddress.getPurposes()) {
        if (!getPartyReferenceService()
            .isValidPhysicalAddressPurpose(tenantId, partyType, physicalAddressPurpose)) {
          hibernateConstraintValidatorContext
              .addMessageParameter("purpose", physicalAddressPurpose)
              .addMessageParameter("partyType", partyType)
              .buildConstraintViolationWithTemplate(
                  "{digital.inception.party.constraints.ValidPhysicalAddress.invalidPurposeForPartyType.message}")
              .addPropertyNode("physicalAddresses")
              .addPropertyNode("purposes")
              .inIterable()
              .addConstraintViolation();

          isValid = false;
        }
      }

    } catch (Throwable e) {
      throw new ValidationException("Failed to validate the physical address", e);
    }

    return isValid;
  }

  /**
   * Validate the reference attribute constraint.
   *
   * @param tenantId the ID for the tenant the party is associated with
   * @param referenceType the type of reference
   * @param attribute the attribute
   * @param hibernateConstraintValidatorContext the Hibernate constraint validator context
   * @return <b>true</b> if the attribute is valid or <b>false</b> otherwise
   */
  protected boolean validateReferenceAttributeConstraint(
      UUID tenantId,
      String referenceType,
      Attribute attribute,
      HibernateConstraintValidatorContext hibernateConstraintValidatorContext)
      throws ValidationException {
    // A null or empty string value is not invalid unless a required constraint is specified
    if (!StringUtils.hasText(attribute.getStringValue())) {
      return true;
    }

    boolean isValid;

    try {
      switch (referenceType) {
        case "contact_mechanism_type":
          isValid =
              partyReferenceService.isValidContactMechanismType(
                  tenantId, attribute.getStringValue());

          if (!isValid) {
            hibernateConstraintValidatorContext
                .addMessageParameter("contactMechanismType", attribute.getStringValue())
                .addMessageParameter("attributeType", attribute.getType())
                .buildConstraintViolationWithTemplate(
                    "{digital.inception.party.constraints.ValidParty.invalidContactMechanismTypeForAttributeType.message}")
                .addPropertyNode("attributes")
                .addPropertyNode("stringValue")
                .inIterable()
                .addConstraintViolation();
          }

          break;

        case "country":
          isValid = referenceService.isValidCountry(attribute.getStringValue());

          if (!isValid) {
            hibernateConstraintValidatorContext
                .addMessageParameter("country", attribute.getStringValue())
                .addMessageParameter("attributeType", attribute.getType())
                .buildConstraintViolationWithTemplate(
                    "{digital.inception.party.constraints.ValidParty.invalidCountryForAttributeType.message}")
                .addPropertyNode("attributes")
                .addPropertyNode("stringValue")
                .inIterable()
                .addConstraintViolation();
          }

          break;

        case "language":
          isValid = referenceService.isValidLanguage(attribute.getStringValue());

          if (!isValid) {
            hibernateConstraintValidatorContext
                .addMessageParameter("language", attribute.getStringValue())
                .addMessageParameter("attributeType", attribute.getType())
                .buildConstraintViolationWithTemplate(
                    "{digital.inception.party.constraints.ValidParty.invalidLanguageForAttributeType.message}")
                .addPropertyNode("attributes")
                .addPropertyNode("stringValue")
                .inIterable()
                .addConstraintViolation();
          }

          break;

        default:
          throw new ValidationException("Invalid reference type (" + referenceType + ")");
      }

      return isValid;
    } catch (Throwable e) {
      throw new ValidationException(
          "Failed to verify that the attribute value ("
              + attribute.getStringValue()
              + ") is a valid reference value of type ("
              + referenceType
              + ')');
    }
  }

  /**
   * Validate the reference preference constraint.
   *
   * @param tenantId the ID for the tenant the party is associated with
   * @param referenceType the type of reference
   * @param preference the preference
   * @param hibernateConstraintValidatorContext the Hibernate constraint validator context
   * @return <b>true</b> if the preference is valid or <b>false</b> otherwise
   */
  protected boolean validateReferencePreferenceConstraint(
      UUID tenantId,
      String referenceType,
      Preference preference,
      HibernateConstraintValidatorContext hibernateConstraintValidatorContext)
      throws ValidationException {
    // A null or empty string value is not invalid unless a required constraint is specified
    if (!StringUtils.hasText(preference.getValue())) {
      return true;
    }

    boolean isValid;

    try {
      switch (referenceType) {
        case "contact_mechanism_type":
          isValid =
              partyReferenceService.isValidContactMechanismType(tenantId, preference.getValue());

          if (!isValid) {
            hibernateConstraintValidatorContext
                .addMessageParameter("contactMechanismType", preference.getValue())
                .addMessageParameter("preferenceType", preference.getType())
                .buildConstraintViolationWithTemplate(
                    "{digital.inception.party.constraints.ValidParty.invalidContactMechanismTypeForPreferenceType.message}")
                .addPropertyNode("preferences")
                .addPropertyNode("value")
                .inIterable()
                .addConstraintViolation();
          }

          break;

        case "country":
          isValid = referenceService.isValidCountry(preference.getValue());

          if (!isValid) {
            hibernateConstraintValidatorContext
                .addMessageParameter("country", preference.getValue())
                .addMessageParameter("preferenceType", preference.getType())
                .buildConstraintViolationWithTemplate(
                    "{digital.inception.party.constraints.ValidParty.invalidCountryForPreferenceType.message}")
                .addPropertyNode("preferences")
                .addPropertyNode("value")
                .inIterable()
                .addConstraintViolation();
          }

          break;

        case "language":
          isValid = referenceService.isValidLanguage(preference.getValue());

          if (!isValid) {
            hibernateConstraintValidatorContext
                .addMessageParameter("language", preference.getValue())
                .addMessageParameter("preferenceType", preference.getType())
                .buildConstraintViolationWithTemplate(
                    "{digital.inception.party.constraints.ValidParty.invalidLanguageForPreferenceType.message}")
                .addPropertyNode("preferences")
                .addPropertyNode("value")
                .inIterable()
                .addConstraintViolation();
          }

          break;

        default:
          throw new ValidationException("Invalid reference type (" + referenceType + ")");
      }

      return isValid;
    } catch (Throwable e) {
      throw new ValidationException(
          "Failed to verify that the preference value ("
              + preference.getValue()
              + ") is a valid reference value of type ("
              + referenceType
              + ')');
    }
  }

  /**
   * Validate the required attribute for the role type.
   *
   * @param roleType the code for the role type
   * @param attributeValue the value for the attribute
   * @param propertyNodeName the property node name
   * @param messageTemplate the message template
   * @param hibernateConstraintValidatorContext the Hibernate constraint validator context
   * @return <b>true</b> if the attribute value is valid or <b>false</b> otherwise
   */
  protected boolean validateRequiredAttributeConstraint(
      String roleType,
      Object attributeValue,
      String propertyNodeName,
      String messageTemplate,
      HibernateConstraintValidatorContext hibernateConstraintValidatorContext)
      throws ValidationException {
    boolean isValid;

    if (attributeValue == null) {
      isValid = false;
    } else {
      if (attributeValue instanceof String) {
        isValid = StringUtils.hasText((String) attributeValue);
      } else if (attributeValue instanceof Set<?>) {
        isValid = !((Set<?>) attributeValue).isEmpty();
      } else if (attributeValue instanceof LocalDate) {
        // The fact we have a valid LocalDate instance means we parsed the attribute value
        isValid = true;
      } else if (attributeValue instanceof Enum<?>) {
        // The fact we have a valid Enum value means we parsed the attribute value
        isValid = true;
      }
      else {
        throw new ValidationException(
            "Failed to validate the required attribute value ("
                + attributeValue
                + ") with the unrecognized type ("
                + attributeValue.getClass().getName()
                + ")");
      }
    }

    if (!isValid) {
      hibernateConstraintValidatorContext
          .addMessageParameter("roleType", roleType)
          .buildConstraintViolationWithTemplate(messageTemplate)
          .addPropertyNode(propertyNodeName)
          .addConstraintViolation();
    }

    return isValid;
  }

  /**
   * Validate the required preference for the role type.
   *
   * @param roleType the code for the role type
   * @param preferenceValue the value for the preference
   * @param propertyNodeName the property node name
   * @param messageTemplate the message template
   * @param hibernateConstraintValidatorContext the Hibernate constraint validator context
   * @return <b>true</b> if the preference value is valid or <b>false</b> otherwise
   */
  protected boolean validateRequiredPreferenceConstraint(
      String roleType,
      Object preferenceValue,
      String propertyNodeName,
      String messageTemplate,
      HibernateConstraintValidatorContext hibernateConstraintValidatorContext)
      throws ValidationException {
    boolean isValid;

    if (preferenceValue == null) {
      isValid = false;
    } else {
      if (preferenceValue instanceof String) {
        isValid = StringUtils.hasText((String) preferenceValue);
      } else if (preferenceValue instanceof Set) {
        isValid = !((Set<?>) preferenceValue).isEmpty();
      } else if (preferenceValue instanceof LocalDate) {
        // The fact we have a valid LocalDate instance means we parsed the preference value
        isValid = true;
      } else {
        throw new ValidationException(
            "Failed to validate the required preference value ("
                + preferenceValue
                + ") with the unrecognized type ("
                + preferenceValue.getClass().getName()
                + ")");
      }
    }

    if (!isValid) {
      hibernateConstraintValidatorContext
          .addMessageParameter("roleType", roleType)
          .buildConstraintViolationWithTemplate(messageTemplate)
          .addPropertyNode(propertyNodeName)
          .addConstraintViolation();
    }

    return isValid;
  }

  /**
   * Validate the size attribute constraint.
   *
   * @param size the size
   * @param attribute the attribute
   * @param hibernateConstraintValidatorContext the Hibernate constraint validator context
   * @return <b>true</b> if the attribute is valid or <b>false</b> otherwise
   */
  protected boolean validateSizeAttributeConstraint(
      int size,
      Attribute attribute,
      HibernateConstraintValidatorContext hibernateConstraintValidatorContext)
      throws ValidationException {
    if ((!StringUtils.hasText(attribute.getStringValue()))
        || (attribute.getStringValue().length() != size)) {
      hibernateConstraintValidatorContext
          .addMessageParameter(
              "value",
              StringUtils.hasText(attribute.getStringValue()) ? attribute.getStringValue() : "")
          .addMessageParameter("size", size)
          .addMessageParameter("attributeType", attribute.getType())
          .buildConstraintViolationWithTemplate(
              "{digital.inception.party.constraints.ValidParty.invalidSizeForAttributeType.message}")
          .addPropertyNode("attributes")
          .addPropertyNode("stringValue")
          .inIterable()
          .addConstraintViolation();

      return false;
    } else {
      return true;
    }
  }

  /**
   * Validate the size preference constraint.
   *
   * @param size the size
   * @param preference the preference
   * @param hibernateConstraintValidatorContext the Hibernate constraint validator context
   * @return <b>true</b> if the preference is valid or <b>false</b> otherwise
   */
  protected boolean validateSizePreferenceConstraint(
      int size,
      Preference preference,
      HibernateConstraintValidatorContext hibernateConstraintValidatorContext)
      throws ValidationException {
    if ((!StringUtils.hasText(preference.getValue())) || (preference.getValue().length() != size)) {
      hibernateConstraintValidatorContext
          .addMessageParameter(
              "value", StringUtils.hasText(preference.getValue()) ? preference.getValue() : "")
          .addMessageParameter("size", size)
          .addMessageParameter("preferenceType", preference.getType())
          .buildConstraintViolationWithTemplate(
              "{digital.inception.party.constraints.ValidParty.invalidSizeForPreferenceType.message}")
          .addPropertyNode("preferences")
          .addPropertyNode("value")
          .inIterable()
          .addConstraintViolation();

      return false;
    } else {
      return true;
    }
  }
}

/*
See: https://ozh.github.io/ascii-tables/

, Building,Complex,Farm,International,Postal,Site,Street,Unstructured
Building Floor,Optional,Invalid,Invalid,Invalid,Invalid,Invalid,Invalid,Invalid
Building Name,Required,Invalid,Invalid,Invalid,Invalid,Invalid,Invalid,Invalid
Building Room,Optional,Invalid,Invalid,Invalid,Invalid,Invalid,Invalid,Invalid
City,Required,Required,Optional,Optional,Required,Required,Required,Optional
Complex Name,Invalid,Required,Invalid,Invalid,Invalid,Invalid,Invalid,Invalid
Complex Unit Number,Invalid,Required,Invalid,Invalid,Invalid,Invalid,Invalid,Invalid
Country,Required,Required,Required,Required,Required,Required,Required,Required
Farm Description,Invalid,Invalid,Optional,Invalid,Invalid,Invalid,Invalid,Invalid
Farm Name,Invalid,Invalid,Optional,Invalid,Invalid,Invalid,Invalid,Invalid
Farm Number,Invalid,Invalid,Required,Required,Invalid,Invalid,Invalid,Invalid
Line 1,Invalid,Invalid,Invalid,Required,Required,Invalid,Invalid,Required
Line 2,Invalid,Invalid,Invalid,Optional,Optional,Invalid,Invalid,Optional
Line 3,Invalid,Invalid,Invalid,Optional,Optional,Invalid,Invalid,Optional
Line 4,Invalid,Invalid,Invalid,Optional,Optional,Invalid,Invalid,Optional
Postal Code Required,Required,Required,Required,Required,Required,Required,Required,Required
Region,Optional,Optional,Optional,Optional,Optional,Optional,Optional,Optional
Site Block,Invalid,Invalid,Invalid,Invalid,Invalid,Required,Invalid,Invalid
Site Number,Invalid,Invalid,Invalid,Invalid,Invalid,Required,Invalid,Invalid
Street Name,Required,Required,Optional,Invalid,Invalid,Optional,Required,Invalid
Street Number,Optional,Optional,Optional,Invalid,Invalid,Optional,Optional,Invalid
Suburb,Optional,Optional,Optional,Invalid,Optional,Invalid,Optional,Invalid
 */
