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

import digital.inception.party.IPartyService;
import digital.inception.party.PhysicalAddress;
import digital.inception.party.PhysicalAddressType;
import digital.inception.reference.IReferenceService;
import java.util.Objects;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.ValidationException;
import org.hibernate.validator.constraintvalidation.HibernateConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

/**
 * The <b>ValidPhysicalAddressValidator</b> class implements the custom constraint validator for
 * validating a physical address.
 *
 * @author Marcus Portmann
 */
public class ValidPhysicalAddressValidator
    implements ConstraintValidator<ValidPhysicalAddress, PhysicalAddress> {

  private final IPartyService partyService;

  private final IReferenceService referenceService;

  @Autowired
  public ValidPhysicalAddressValidator(
      IPartyService partyService, IReferenceService referenceService) {
    this.partyService = partyService;
    this.referenceService = referenceService;
  }

  @Override
  public void initialize(ValidPhysicalAddress constraintAnnotation) {}

  @Override
  public boolean isValid(
      PhysicalAddress physicalAddress, ConstraintValidatorContext constraintValidatorContext) {

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

    // Disable the default constraint violation
    constraintValidatorContext.disableDefaultConstraintViolation();

    HibernateConstraintValidatorContext hibernateConstraintValidatorContext =
        constraintValidatorContext.unwrap(HibernateConstraintValidatorContext.class);

    try {
      if (!partyService.isValidPhysicalAddressType(physicalAddress.getType())) {
        hibernateConstraintValidatorContext
            .addMessageParameter("type", physicalAddress.getType())
            .buildConstraintViolationWithTemplate(
                "{digital.inception.party.constraints.ValidPhysicalAddress.invalidTypeCode.message}")
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
                  .addConstraintViolation();

              isValid = false;
            }

            if (!StringUtils.hasText(physicalAddress.getCity())) {
              hibernateConstraintValidatorContext
                  .buildConstraintViolationWithTemplate(
                      "{digital.inception.party.constraints.ValidPhysicalAddress.cityRequired.message}")
                  .addConstraintViolation();

              isValid = false;
            }

            if (!StringUtils.hasText(physicalAddress.getStreetName())) {
              hibernateConstraintValidatorContext
                  .buildConstraintViolationWithTemplate(
                      "{digital.inception.party.constraints.ValidPhysicalAddress.streetNameRequired.message}")
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
                  .addConstraintViolation();

              isValid = false;
            }

            if (!StringUtils.hasText(physicalAddress.getComplexName())) {
              hibernateConstraintValidatorContext
                  .buildConstraintViolationWithTemplate(
                      "{digital.inception.party.constraints.ValidPhysicalAddress.complexNameRequired.message}")
                  .addConstraintViolation();

              isValid = false;
            }

            if (!StringUtils.hasText(physicalAddress.getComplexUnitNumber())) {
              hibernateConstraintValidatorContext
                  .buildConstraintViolationWithTemplate(
                      "{digital.inception.party.constraints.ValidPhysicalAddress.complexUnitNumberRequired.message}")
                  .addConstraintViolation();

              isValid = false;
            }

            if (!StringUtils.hasText(physicalAddress.getStreetName())) {
              hibernateConstraintValidatorContext
                  .buildConstraintViolationWithTemplate(
                      "{digital.inception.party.constraints.ValidPhysicalAddress.streetNameRequired.message}")
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
                  .addConstraintViolation();

              isValid = false;
            }

            if (StringUtils.hasText(physicalAddress.getStreetName())) {
              hibernateConstraintValidatorContext
                  .buildConstraintViolationWithTemplate(
                      "{digital.inception.party.constraints.ValidPhysicalAddress.streetNameNotSupported.message}")
                  .addConstraintViolation();

              isValid = false;
            }

            if (StringUtils.hasText(physicalAddress.getStreetNumber())) {
              hibernateConstraintValidatorContext
                  .buildConstraintViolationWithTemplate(
                      "{digital.inception.party.constraints.ValidPhysicalAddress.streetNumberNotSupported.message}")
                  .addConstraintViolation();

              isValid = false;
            }

            if (StringUtils.hasText(physicalAddress.getSuburb())) {
              hibernateConstraintValidatorContext
                  .buildConstraintViolationWithTemplate(
                      "{digital.inception.party.constraints.ValidPhysicalAddress.suburbNotSupported.message}")
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
                  .addConstraintViolation();

              isValid = false;
            }

            if (StringUtils.hasText(physicalAddress.getStreetName())) {
              hibernateConstraintValidatorContext
                  .buildConstraintViolationWithTemplate(
                      "{digital.inception.party.constraints.ValidPhysicalAddress.streetNameNotSupported.message}")
                  .addConstraintViolation();

              isValid = false;
            }

            if (StringUtils.hasText(physicalAddress.getStreetNumber())) {
              hibernateConstraintValidatorContext
                  .buildConstraintViolationWithTemplate(
                      "{digital.inception.party.constraints.ValidPhysicalAddress.streetNumberNotSupported.message}")
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
                  .addConstraintViolation();

              isValid = false;
            }

            if (!StringUtils.hasText(physicalAddress.getSiteBlock())) {
              hibernateConstraintValidatorContext
                  .buildConstraintViolationWithTemplate(
                      "{digital.inception.party.constraints.ValidPhysicalAddress.siteBlockRequired.message}")
                  .addConstraintViolation();

              isValid = false;
            }

            if (!StringUtils.hasText(physicalAddress.getSiteNumber())) {
              hibernateConstraintValidatorContext
                  .buildConstraintViolationWithTemplate(
                      "{digital.inception.party.constraints.ValidPhysicalAddress.siteNumberRequired.message}")
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
                  .addConstraintViolation();

              isValid = false;
            }

            if (!StringUtils.hasText(physicalAddress.getStreetName())) {
              hibernateConstraintValidatorContext
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
            break;
            // Validate an unstructured address
          case PhysicalAddressType.UNSTRUCTURED:
            if (!StringUtils.hasText(physicalAddress.getLine1())) {
              hibernateConstraintValidatorContext
                  .buildConstraintViolationWithTemplate(
                      "{digital.inception.party.constraints.ValidPhysicalAddress.line1UnstructuredRequired.message}")
                  .addConstraintViolation();

              isValid = false;
            }

            if (StringUtils.hasText(physicalAddress.getStreetName())) {
              hibernateConstraintValidatorContext
                  .buildConstraintViolationWithTemplate(
                      "{digital.inception.party.constraints.ValidPhysicalAddress.streetNameNotSupported.message}")
                  .addConstraintViolation();

              isValid = false;
            }

            if (StringUtils.hasText(physicalAddress.getStreetNumber())) {
              hibernateConstraintValidatorContext
                  .buildConstraintViolationWithTemplate(
                      "{digital.inception.party.constraints.ValidPhysicalAddress.streetNumberNotSupported.message}")
                  .addConstraintViolation();

              isValid = false;
            }

            if (StringUtils.hasText(physicalAddress.getSuburb())) {
              hibernateConstraintValidatorContext
                  .buildConstraintViolationWithTemplate(
                      "{digital.inception.party.constraints.ValidPhysicalAddress.suburbNotSupported.message}")
                  .addConstraintViolation();

              isValid = false;
            }
            break;
        }

        if (!StringUtils.hasText(physicalAddress.getCountry())) {
          hibernateConstraintValidatorContext
              .buildConstraintViolationWithTemplate(
                  "{digital.inception.party.constraints.ValidPhysicalAddress.countryRequired.message}")
              .addConstraintViolation();

          isValid = false;
        }

        if (!StringUtils.hasText(physicalAddress.getPostalCode())) {
          hibernateConstraintValidatorContext
              .buildConstraintViolationWithTemplate(
                  "{digital.inception.party.constraints.ValidPhysicalAddress.postalCodeRequired.message}")
              .addConstraintViolation();

          isValid = false;
        }

        if (StringUtils.hasText(physicalAddress.getSuburb())
            && (!StringUtils.hasText(physicalAddress.getCity()))) {
          hibernateConstraintValidatorContext
              .buildConstraintViolationWithTemplate(
                  "{digital.inception.party.constraints.ValidPhysicalAddress.cityRequired.message}")
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
                .addConstraintViolation();

            isValid = false;
          }

          if (StringUtils.hasText(physicalAddress.getBuildingName())) {
            hibernateConstraintValidatorContext
                .buildConstraintViolationWithTemplate(
                    "{digital.inception.party.constraints.ValidPhysicalAddress.buildingNameNotSupported.message}")
                .addConstraintViolation();

            isValid = false;
          }

          if (StringUtils.hasText(physicalAddress.getBuildingRoom())) {
            hibernateConstraintValidatorContext
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
        if (!physicalAddress.getType().equals(PhysicalAddressType.COMPLEX)) {
          if (StringUtils.hasText(physicalAddress.getComplexName())) {
            hibernateConstraintValidatorContext
                .buildConstraintViolationWithTemplate(
                    "{digital.inception.party.constraints.ValidPhysicalAddress.complexNameNotSupported.message}")
                .addConstraintViolation();

            isValid = false;
          }

          if (StringUtils.hasText(physicalAddress.getComplexUnitNumber())) {
            hibernateConstraintValidatorContext
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
        if (!physicalAddress.getType().equals(PhysicalAddressType.FARM)) {
          if (StringUtils.hasText(physicalAddress.getFarmDescription())) {
            hibernateConstraintValidatorContext
                .buildConstraintViolationWithTemplate(
                    "{digital.inception.party.constraints.ValidPhysicalAddress.farmDescriptionNotSupported.message}")
                .addConstraintViolation();

            isValid = false;
          }

          if (StringUtils.hasText(physicalAddress.getFarmName())) {
            hibernateConstraintValidatorContext
                .buildConstraintViolationWithTemplate(
                    "{digital.inception.party.constraints.ValidPhysicalAddress.farmNameNotSupported.message}")
                .addConstraintViolation();

            isValid = false;
          }

          if (StringUtils.hasText(physicalAddress.getFarmNumber())) {
            hibernateConstraintValidatorContext
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
        if (!physicalAddress.getType().equals(PhysicalAddressType.SITE)) {
          if (StringUtils.hasText(physicalAddress.getSiteBlock())) {
            hibernateConstraintValidatorContext
                .buildConstraintViolationWithTemplate(
                    "{digital.inception.party.constraints.ValidPhysicalAddress.siteBlockNotSupported.message}")
                .addConstraintViolation();

            isValid = false;
          }

          if (StringUtils.hasText(physicalAddress.getSiteNumber())) {
            hibernateConstraintValidatorContext
                .buildConstraintViolationWithTemplate(
                    "{digital.inception.party.constraints.ValidPhysicalAddress.siteNumberNotSupported.message}")
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
                .addConstraintViolation();

            isValid = false;
          }

          if (StringUtils.hasText(physicalAddress.getLine2())) {
            hibernateConstraintValidatorContext
                .buildConstraintViolationWithTemplate(
                    "{digital.inception.party.constraints.ValidPhysicalAddress.line2NotSupported.message}")
                .addConstraintViolation();

            isValid = false;
          }

          if (StringUtils.hasText(physicalAddress.getLine3())) {
            hibernateConstraintValidatorContext
                .buildConstraintViolationWithTemplate(
                    "{digital.inception.party.constraints.ValidPhysicalAddress.line3NotSupported.message}")
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
              .addConstraintViolation();

          isValid = false;
        }
      }
    } catch (Throwable e) {
      throw new ValidationException("Failed to validate the physical address", e);
    }

    return isValid;
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
Postal Code Required,Required,Required,Required,Required,Required,Required,Required,Required
Region,Optional,Optional,Optional,Optional,Optional,Optional,Optional,Optional
Site Block,Invalid,Invalid,Invalid,Invalid,Invalid,Required,Invalid,Invalid
Site Number,Invalid,Invalid,Invalid,Invalid,Invalid,Required,Invalid,Invalid
Street Name,Required,Required,Optional,Invalid,Invalid,Optional,Required,Invalid
Street Number,Optional,Optional,Optional,Invalid,Invalid,Optional,Optional,Invalid
Suburb,Optional,Optional,Optional,Invalid,Optional,Invalid,Optional,Invalid
 */
