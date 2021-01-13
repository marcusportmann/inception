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

// ~--- non-JDK imports --------------------------------------------------------

import digital.inception.party.PhysicalAddress;
import digital.inception.party.PhysicalAddressType;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

// ~--- JDK imports ------------------------------------------------------------

/**
 * The <code>ValidPhysicalAddressValidator</code> class implements the custom constraint validator
 * for validating a physical address.
 *
 * @author Marcus Portmann
 */
public class ValidPhysicalAddressValidator
    implements ConstraintValidator<ValidPhysicalAddress, PhysicalAddress> {

  @Autowired
  public ValidPhysicalAddressValidator() {}

  @Override
  public void initialize(ValidPhysicalAddress constraintAnnotation) {}

  @Override
  public boolean isValid(PhysicalAddress physicalAddress, ConstraintValidatorContext context) {

    /*
    The following validation rules are applied for the different address types:

    +----------------------+----------+----------+----------+---------------+----------+----------+--------------+
    |                      | Building | Complex  |   Farm   | International |   Site   |  Street  | Unstructured |
    +----------------------+----------+----------+----------+---------------+----------+----------+--------------+
    | Building Floor       | Optional | Invalid  | Invalid  | Invalid       | Invalid  | Invalid  | Invalid      |
    | Building Name        | Required | Invalid  | Invalid  | Invalid       | Invalid  | Invalid  | Invalid      |
    | Building Room        | Optional | Invalid  | Invalid  | Invalid       | Invalid  | Invalid  | Invalid      |
    | City                 | Required | Required | Optional | Optional      | Required | Required | Optional     |
    | Complex Name         | Invalid  | Required | Invalid  | Invalid       | Invalid  | Invalid  | Invalid      |
    | Complex Unit Number  | Invalid  | Required | Invalid  | Invalid       | Invalid  | Invalid  | Invalid      |
    | Farm Description     | Invalid  | Invalid  | Optional | Invalid       | Invalid  | Invalid  | Invalid      |
    | Farm Name            | Invalid  | Invalid  | Optional | Invalid       | Invalid  | Invalid  | Invalid      |
    | Farm Number          | Invalid  | Invalid  | Required | Invalid       | Invalid  | Invalid  | Invalid      |
    | Line 1               | Invalid  | Invalid  | Invalid  | Required      | Invalid  | Invalid  | Required     |
    | Line 2               | Invalid  | Invalid  | Invalid  | Optional      | Invalid  | Invalid  | Optional     |
    | Line 3               | Invalid  | Invalid  | Invalid  | Optional      | Invalid  | Invalid  | Optional     |
    | Region               | Optional | Optional | Optional | Optional      | Optional | Optional | Optional     |
    | Site Block           | Invalid  | Invalid  | Invalid  | Invalid       | Required | Invalid  | Invalid      |
    | Site Number          | Invalid  | Invalid  | Invalid  | Invalid       | Required | Invalid  | Invalid      |
    | Street Name          | Required | Required | Optional | Invalid       | Optional | Required | Invalid      |
    | Street Number        | Optional | Optional | Optional | Invalid       | Optional | Optional | Invalid      |
    | Suburb               | Optional | Optional | Optional | Invalid       | Optional | Optional | Invalid      |
    +----------------------+----------+----------+----------+---------------+----------+----------+--------------+
    */
    boolean isValid = true;

    // Disable the default constraint violation
    context.disableDefaultConstraintViolation();

    // Validate a building address
    if (physicalAddress.getType() == PhysicalAddressType.BUILDING) {
      if (!StringUtils.hasText(physicalAddress.getBuildingName())) {
        context
            .buildConstraintViolationWithTemplate(
                "{digital.inception.party.constraints.ValidPhysicalAddress.buildingNameRequired.message}")
            .addConstraintViolation();

        isValid = false;
      }

      if (!StringUtils.hasText(physicalAddress.getCity())) {
        context
            .buildConstraintViolationWithTemplate(
                "{digital.inception.party.constraints.ValidPhysicalAddress.cityRequired.message}")
            .addConstraintViolation();

        isValid = false;
      }

      if (!StringUtils.hasText(physicalAddress.getStreetName())) {
        context
            .buildConstraintViolationWithTemplate(
                "{digital.inception.party.constraints.ValidPhysicalAddress.streetNameRequired.message}")
            .addConstraintViolation();

        isValid = false;
      }
    }
    // Validate a complex address
    else if (physicalAddress.getType() == PhysicalAddressType.COMPLEX) {
      if (!StringUtils.hasText(physicalAddress.getCity())) {
        context
            .buildConstraintViolationWithTemplate(
                "{digital.inception.party.constraints.ValidPhysicalAddress.cityRequired.message}")
            .addConstraintViolation();

        isValid = false;
      }

      if (!StringUtils.hasText(physicalAddress.getComplexName())) {
        context
            .buildConstraintViolationWithTemplate(
                "{digital.inception.party.constraints.ValidPhysicalAddress.complexNameRequired.message}")
            .addConstraintViolation();

        isValid = false;
      }

      if (!StringUtils.hasText(physicalAddress.getComplexUnitNumber())) {
        context
            .buildConstraintViolationWithTemplate(
                "{digital.inception.party.constraints.ValidPhysicalAddress.complexUnitNumberRequired.message}")
            .addConstraintViolation();

        isValid = false;
      }

      if (!StringUtils.hasText(physicalAddress.getStreetName())) {
        context
            .buildConstraintViolationWithTemplate(
                "{digital.inception.party.constraints.ValidPhysicalAddress.streetNameRequired.message}")
            .addConstraintViolation();

        isValid = false;
      }
    }
    // Validate a farm address
    else if (physicalAddress.getType() == PhysicalAddressType.FARM) {
      if (!StringUtils.hasText(physicalAddress.getFarmNumber())) {
        context
            .buildConstraintViolationWithTemplate(
                "{digital.inception.party.constraints.ValidPhysicalAddress.farmNumberRequired.message}")
            .addConstraintViolation();

        isValid = false;
      }
    }
    // Validate an international address
    else if (physicalAddress.getType() == PhysicalAddressType.INTERNATIONAL) {
      if (!StringUtils.hasText(physicalAddress.getLine1())) {
        context
            .buildConstraintViolationWithTemplate(
                "{digital.inception.party.constraints.ValidPhysicalAddress.line1InternationalRequired.message}")
            .addConstraintViolation();

        isValid = false;
      }

      if (StringUtils.hasText(physicalAddress.getStreetName())) {
        context
            .buildConstraintViolationWithTemplate(
                "{digital.inception.party.constraints.ValidPhysicalAddress.streetNameNotSupported.message}")
            .addConstraintViolation();

        isValid = false;
      }

      if (StringUtils.hasText(physicalAddress.getStreetNumber())) {
        context
            .buildConstraintViolationWithTemplate(
                "{digital.inception.party.constraints.ValidPhysicalAddress.streetNumberNotSupported.message}")
            .addConstraintViolation();

        isValid = false;
      }

      if (StringUtils.hasText(physicalAddress.getSuburb())) {
        context
            .buildConstraintViolationWithTemplate(
                "{digital.inception.party.constraints.ValidPhysicalAddress.suburbNotSupported.message}")
            .addConstraintViolation();

        isValid = false;
      }
    }
    // Validate a site address
    else if (physicalAddress.getType() == PhysicalAddressType.SITE) {
      if (!StringUtils.hasText(physicalAddress.getCity())) {
        context
            .buildConstraintViolationWithTemplate(
                "{digital.inception.party.constraints.ValidPhysicalAddress.cityRequired.message}")
            .addConstraintViolation();

        isValid = false;
      }

      if (!StringUtils.hasText(physicalAddress.getSiteBlock())) {
        context
            .buildConstraintViolationWithTemplate(
                "{digital.inception.party.constraints.ValidPhysicalAddress.siteBlockRequired.message}")
            .addConstraintViolation();

        isValid = false;
      }

      if (!StringUtils.hasText(physicalAddress.getSiteNumber())) {
        context
            .buildConstraintViolationWithTemplate(
                "{digital.inception.party.constraints.ValidPhysicalAddress.siteNumberRequired.message}")
            .addConstraintViolation();

        isValid = false;
      }
    }
    // Validate a street address
    else if (physicalAddress.getType() == PhysicalAddressType.STREET) {
      if (!StringUtils.hasText(physicalAddress.getCity())) {
        context
            .buildConstraintViolationWithTemplate(
                "{digital.inception.party.constraints.ValidPhysicalAddress.cityRequired.message}")
            .addConstraintViolation();

        isValid = false;
      }

      if (!StringUtils.hasText(physicalAddress.getStreetName())) {
        context
            .buildConstraintViolationWithTemplate(
                "{digital.inception.party.constraints.ValidPhysicalAddress.streetNameRequired.message}")
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
    }
    // Validate an unstructured address
    else if (physicalAddress.getType() == PhysicalAddressType.UNSTRUCTURED) {
      if (!StringUtils.hasText(physicalAddress.getLine1())) {
        context
            .buildConstraintViolationWithTemplate(
                "{digital.inception.party.constraints.ValidPhysicalAddress.line1UnstructuredRequired.message}")
            .addConstraintViolation();

        isValid = false;
      }

      if (StringUtils.hasText(physicalAddress.getStreetName())) {
        context
            .buildConstraintViolationWithTemplate(
                "{digital.inception.party.constraints.ValidPhysicalAddress.streetNameNotSupported.message}")
            .addConstraintViolation();

        isValid = false;
      }

      if (StringUtils.hasText(physicalAddress.getStreetNumber())) {
        context
            .buildConstraintViolationWithTemplate(
                "{digital.inception.party.constraints.ValidPhysicalAddress.streetNumberNotSupported.message}")
            .addConstraintViolation();

        isValid = false;
      }

      if (StringUtils.hasText(physicalAddress.getSuburb())) {
        context
            .buildConstraintViolationWithTemplate(
                "{digital.inception.party.constraints.ValidPhysicalAddress.suburbNotSupported.message}")
            .addConstraintViolation();

        isValid = false;
      }
    }

    if (!StringUtils.hasText(physicalAddress.getCountry())) {
      context
          .buildConstraintViolationWithTemplate(
              "{digital.inception.party.constraints.ValidPhysicalAddress.countryRequired.message}")
          .addConstraintViolation();

      isValid = false;
    }

    if (!StringUtils.hasText(physicalAddress.getPostalCode())) {
      context
          .buildConstraintViolationWithTemplate(
              "{digital.inception.party.constraints.ValidPhysicalAddress.postalCodeRequired.message}")
          .addConstraintViolation();

      isValid = false;
    }

    if (StringUtils.hasText(physicalAddress.getSuburb())
        && (!StringUtils.hasText(physicalAddress.getCity()))) {
      context
          .buildConstraintViolationWithTemplate(
              "{digital.inception.party.constraints.ValidPhysicalAddress.cityRequired.message}")
          .addConstraintViolation();

      isValid = false;
    }

    /*
     * Check that building address fields have not been specified for an address that is not a
     * building address.
     */
    if (physicalAddress.getType() != PhysicalAddressType.BUILDING) {
      if (StringUtils.hasText(physicalAddress.getBuildingFloor())) {
        context
            .buildConstraintViolationWithTemplate(
                "{digital.inception.party.constraints.ValidPhysicalAddress.buildingFloorNotSupported.message}")
            .addConstraintViolation();

        isValid = false;
      }

      if (StringUtils.hasText(physicalAddress.getBuildingName())) {
        context
            .buildConstraintViolationWithTemplate(
                "{digital.inception.party.constraints.ValidPhysicalAddress.buildingNameNotSupported.message}")
            .addConstraintViolation();

        isValid = false;
      }

      if (StringUtils.hasText(physicalAddress.getBuildingRoom())) {
        context
            .buildConstraintViolationWithTemplate(
                "{digital.inception.party.constraints.ValidPhysicalAddress.buildingRoomNotSupported.message}")
            .addConstraintViolation();

        isValid = false;
      }
    }

    /*
     * Check that complex address fields have not been specified for an address that is not a
     * complex address.
     */
    if (physicalAddress.getType() != PhysicalAddressType.COMPLEX) {
      if (StringUtils.hasText(physicalAddress.getComplexName())) {
        context
            .buildConstraintViolationWithTemplate(
                "{digital.inception.party.constraints.ValidPhysicalAddress.complexNameNotSupported.message}")
            .addConstraintViolation();

        isValid = false;
      }

      if (StringUtils.hasText(physicalAddress.getComplexUnitNumber())) {
        context
            .buildConstraintViolationWithTemplate(
                "{digital.inception.party.constraints.ValidPhysicalAddress.complexUnitNumberNotSupported.message}")
            .addConstraintViolation();

        isValid = false;
      }
    }

    /*
     * Check that farm address fields have not been specified for an address that is not a
     * farm address.
     */
    if (physicalAddress.getType() != PhysicalAddressType.FARM) {
      if (StringUtils.hasText(physicalAddress.getFarmDescription())) {
        context
            .buildConstraintViolationWithTemplate(
                "{digital.inception.party.constraints.ValidPhysicalAddress.farmDescriptionNotSupported.message}")
            .addConstraintViolation();

        isValid = false;
      }

      if (StringUtils.hasText(physicalAddress.getFarmName())) {
        context
            .buildConstraintViolationWithTemplate(
                "{digital.inception.party.constraints.ValidPhysicalAddress.farmNameNotSupported.message}")
            .addConstraintViolation();

        isValid = false;
      }

      if (StringUtils.hasText(physicalAddress.getFarmNumber())) {
        context
            .buildConstraintViolationWithTemplate(
                "{digital.inception.party.constraints.ValidPhysicalAddress.farmNumberNotSupported.message}")
            .addConstraintViolation();

        isValid = false;
      }
    }

    /*
     * Check that site address fields have not been specified for an address that is not a
     * site address.
     */
    if (physicalAddress.getType() != PhysicalAddressType.SITE) {
      if (StringUtils.hasText(physicalAddress.getSiteBlock())) {
        context
            .buildConstraintViolationWithTemplate(
                "{digital.inception.party.constraints.ValidPhysicalAddress.siteBlockNotSupported.message}")
            .addConstraintViolation();

        isValid = false;
      }

      if (StringUtils.hasText(physicalAddress.getSiteNumber())) {
        context
            .buildConstraintViolationWithTemplate(
                "{digital.inception.party.constraints.ValidPhysicalAddress.siteNumberNotSupported.message}")
            .addConstraintViolation();

        isValid = false;
      }
    }

    /*
     * Check that international and unstructured address fields have not been specified for an
     * address that is not an international address or unstructured address.
     */
    if ((physicalAddress.getType() != PhysicalAddressType.INTERNATIONAL)
        && (physicalAddress.getType() != PhysicalAddressType.UNSTRUCTURED)) {
      if (StringUtils.hasText(physicalAddress.getLine1())) {
        context
            .buildConstraintViolationWithTemplate(
                "{digital.inception.party.constraints.ValidPhysicalAddress.line1NotSupported.message}")
            .addConstraintViolation();

        isValid = false;
      }

      if (StringUtils.hasText(physicalAddress.getLine2())) {
        context
            .buildConstraintViolationWithTemplate(
                "{digital.inception.party.constraints.ValidPhysicalAddress.line2NotSupported.message}")
            .addConstraintViolation();

        isValid = false;
      }

      if (StringUtils.hasText(physicalAddress.getLine3())) {
        context
            .buildConstraintViolationWithTemplate(
                "{digital.inception.party.constraints.ValidPhysicalAddress.line3NotSupported.message}")
            .addConstraintViolation();

        isValid = false;
      }
    }

    return isValid;
  }
}

/*
+----------------------+----------+----------+----------+---------------+----------+----------+--------------+
|                      | Building | Complex  |   Farm   | International |   Site   |  Street  | Unstructured |
+----------------------+----------+----------+----------+---------------+----------+----------+--------------+
| Building Floor       | Optional | Invalid  | Invalid  | Invalid       | Invalid  | Invalid  | Invalid      |
| Building Name        | Required | Invalid  | Invalid  | Invalid       | Invalid  | Invalid  | Invalid      |
| Building Room        | Optional | Invalid  | Invalid  | Invalid       | Invalid  | Invalid  | Invalid      |
| City                 | Required | Required | Optional | Optional      | Required | Required | Optional     |
| Complex Name         | Invalid  | Required | Invalid  | Invalid       | Invalid  | Invalid  | Invalid      |
| Complex Unit Number  | Invalid  | Required | Invalid  | Invalid       | Invalid  | Invalid  | Invalid      |
| Farm Description     | Invalid  | Invalid  | Optional | Invalid       | Invalid  | Invalid  | Invalid      |
| Farm Name            | Invalid  | Invalid  | Optional | Invalid       | Invalid  | Invalid  | Invalid      |
| Farm Number          | Invalid  | Invalid  | Required | Invalid       | Invalid  | Invalid  | Invalid      |
| Line 1               | Invalid  | Invalid  | Invalid  | Required      | Invalid  | Invalid  | Required     |
| Line 2               | Invalid  | Invalid  | Invalid  | Optional      | Invalid  | Invalid  | Optional     |
| Line 3               | Invalid  | Invalid  | Invalid  | Optional      | Invalid  | Invalid  | Optional     |
| Region               | Optional | Optional | Optional | Optional      | Optional | Optional | Optional     |
| Site Block           | Invalid  | Invalid  | Invalid  | Invalid       | Required | Invalid  | Invalid      |
| Site Number          | Invalid  | Invalid  | Invalid  | Invalid       | Required | Invalid  | Invalid      |
| Street Name          | Required | Required | Optional | Invalid       | Optional | Required | Invalid      |
| Street Number        | Optional | Optional | Optional | Invalid       | Optional | Optional | Invalid      |
| Suburb               | Optional | Optional | Optional | Invalid       | Invalid  | Optional | Invalid      |
+----------------------+----------+----------+----------+---------------+----------+----------+--------------+
*/

/*

Type	Building Floor	Building Name	Building Room	City	Complex Name	Complex Unit Number	Country	Farm Description	Farm Name	Farm Number	Line 1	Line 2	Line 3	Postal Code	Region	Site Block	Site Number	Street Name	Street Number	Suburb
Building	Optional	Required	Optional	Required	Invalid	Invalid	Required	Invalid	Invalid	Invalid	Invalid	Invalid	Invalid	Required	Optional	Invalid	Invalid	Required	Optional	Optional
Complex	Invalid	Invalid	Invalid	Required	Required	Required	Required	Invalid	Invalid	Invalid	Invalid	Invalid	Invalid	Required	Optional	Invalid	Invalid	Required	Optional	Optional
Farm	Invalid	Invalid	Invalid	Optional	Invalid	Invalid	Required	Optional	Optional	Required	Invalid	Invalid	Invalid	Required	Optional	Invalid	Invalid	Optional	Optional	Optional
International	Invalid	Invalid	Invalid	Optional	Invalid	Invalid	Required	Invalid	Invalid	Invalid	Required	Optional	Optional	Required	Optional	Invalid	Invalid	Invalid	Invalid	Invalid
Site	Invalid	Invalid	Invalid	Required	Invalid	Invalid	Required	Invalid	Invalid	Invalid	Invalid	Invalid	Invalid	Required	Optional	Required	Required	Optional	Optional	Invalid
Street	Invalid	Invalid	Invalid	Required	Invalid	Invalid	Required	Invalid	Invalid	Invalid	Invalid	Invalid	Invalid	Required	Optional	Invalid	Invalid	Required	Optional	Optional
International	Invalid	Invalid	Invalid	Optional	Invalid	Invalid	Required	Invalid	Invalid	Invalid	Required	Optional	Optional	Required	Optional	Invalid	Invalid	Invalid	Invalid	Invalid


 */

/*
 	Building	Complex	Farm	International	Site	Street	Unstructured
Building Floor	Optional	Invalid	Invalid	Invalid	Invalid	Invalid	Invalid
Building Name	Required	Invalid	Invalid	Invalid	Invalid	Invalid	Invalid
Building Room	Optional	Invalid	Invalid	Invalid	Invalid	Invalid	Invalid
City	Required	Required	Optional	Optional	Required	Required	Optional
Complex Name	Invalid	Required	Invalid	Invalid	Invalid	Invalid	Invalid
Complex Unit Number	Invalid	Required	Invalid	Invalid	Invalid	Invalid	Invalid
Country	Required	Required	Required	Required	Required	Required	Required
Farm Description	Invalid	Invalid	Optional	Invalid	Invalid	Invalid	Invalid
Farm Name	Invalid	Invalid	Optional	Invalid	Invalid	Invalid	Invalid
Farm Number	Invalid	Invalid	Required	Invalid	Invalid	Invalid	Invalid
Line 1	Invalid	Invalid	Invalid	Required	Invalid	Invalid	Required
Line 2	Invalid	Invalid	Invalid	Optional	Invalid	Invalid	Optional
Line 3	Invalid	Invalid	Invalid	Optional	Invalid	Invalid	Optional
Postal Code Required	Required	Required	Required	Required	Required	Required	Required
Region	Optional	Optional	Optional	Optional	Optional	Optional	Optional
Site Block	Invalid	Invalid	Invalid	Invalid	Required	Invalid	Invalid
Site Number	Invalid	Invalid	Invalid	Invalid	Required	Invalid	Invalid
Street Name	Required	Required	Optional	Invalid	Optional	Required	Invalid
Street Number	Optional	Optional	Optional	Invalid	Optional	Optional	Invalid
Suburb	Optional	Optional	Optional	Invalid	Invalid	Optional	Invalid
 */
