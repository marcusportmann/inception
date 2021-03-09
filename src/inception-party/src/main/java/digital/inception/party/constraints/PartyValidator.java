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

import digital.inception.core.util.ISO8601Util;
import java.time.LocalDate;
import java.util.Set;
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
