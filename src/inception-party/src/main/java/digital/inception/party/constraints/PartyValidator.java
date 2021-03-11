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
import digital.inception.party.ContactMechanism;
import digital.inception.party.IPartyReferenceService;
import digital.inception.reference.IReferenceService;
import java.time.LocalDate;
import java.util.Set;
import java.util.regex.Pattern;
import javax.validation.ValidationException;
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
      "^$|(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\"
          + ".[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d"
          + "-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\"
          + ".)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}"
          + "(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:"
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
   * Validate the contact mechanism type attribute.
   *
   * @param attribute the attribute
   * @param hibernateConstraintValidatorContext the Hibernate constraint validator context
   * @return <b>true</b> if the attribute value is valid or <b>false</b> otherwise
   */
  protected boolean validateContactMechanismTypeAttribute(
      Attribute attribute, HibernateConstraintValidatorContext hibernateConstraintValidatorContext)
      throws ValidationException {
    try {
      boolean isValid =
          partyReferenceService.isValidContactMechanismType(attribute.getStringValue());

      if (!isValid) {
        hibernateConstraintValidatorContext
            .addMessageParameter("contactMechanismType", attribute.getStringValue())
            .addMessageParameter("attributeType", attribute.getType())
            .buildConstraintViolationWithTemplate(
                "{digital.inception.party.constraints.ValidParty.invalidContactMechanismTypeForAttributeType.message}")
            .addPropertyNode("attributes")
            .addPropertyNode("type")
            .inIterable()
            .addConstraintViolation();
      }

      return isValid;
    } catch (Throwable e) {
      throw new ValidationException(
          "Failed to verify that the attribute value ("
              + attribute.getStringValue()
              + ") is a valid contact mechanism type");
    }
  }

  /**
   * Validate the contact mechanism value.
   *
   * @param contactMechanism the contact mechanism
   * @param hibernateConstraintValidatorContext the Hibernate constraint validator context
   * @return <b>true</b> if the contact mechanism value is valid or <b>false</b> otherwise
   */
  protected boolean validateContactMechanismValue(
      ContactMechanism contactMechanism,
      HibernateConstraintValidatorContext hibernateConstraintValidatorContext)
      throws ValidationException {
    boolean isValid = true;

    switch (contactMechanism.getType()) {
      case "email_address":
        if (!isValidEmailAddress(contactMechanism.getValue())) {
          hibernateConstraintValidatorContext
              .addMessageParameter("emailAddress", contactMechanism.getValue())
              .buildConstraintViolationWithTemplate(
                  "{digital.inception.party.constraints.PartyValidator.invalidEmailAddress.message}")
              .addPropertyNode("contactMechanisms")
              .addPropertyNode("value")
              .inIterable()
              .addConstraintViolation();

          isValid = false;
        }
        break;

      case "fax_number":
        break;

      case "mobile_number":
        if (!isValidMobileNumber(contactMechanism.getValue())) {
          hibernateConstraintValidatorContext
              .addMessageParameter("mobileNumber", contactMechanism.getValue())
              .buildConstraintViolationWithTemplate(
                  "{digital.inception.party.constraints.PartyValidator.invalidMobileNumber.message}")
              .addPropertyNode("contactMechanisms")
              .addPropertyNode("value")
              .inIterable()
              .addConstraintViolation();

          isValid = false;
        }

        break;

      case "phone_number":
        break;

      case "social_media":
        break;
    }

    return isValid;
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
  protected boolean validateRequiredAttributeForRoleType(
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
      } else if (attributeValue instanceof Set) {
        isValid = ((Set<?>) attributeValue).size() > 0;
      } else if (attributeValue instanceof LocalDate) {
        // The fact we have a valid LocalDate instance means we parsed the attribute value
        isValid = true;
      } else {
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
}
