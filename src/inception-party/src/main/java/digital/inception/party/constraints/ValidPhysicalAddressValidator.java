/// *
// * Copyright Marcus Portmann
// *
// * Licensed under the Apache License, Version 2.0 (the "License");
// * you may not use this file except in compliance with the License.
// * You may obtain a copy of the License at
// *
// *   http://www.apache.org/licenses/LICENSE-2.0
// *
// * Unless required by applicable law or agreed to in writing, software
// * distributed under the License is distributed on an "AS IS" BASIS,
// * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// * See the License for the specific language governing permissions and
// * limitations under the License.
// */
//
// package digital.inception.party.constraints;
//
// import digital.inception.party.service.IPartyReferenceService;
// import digital.inception.party.model.PhysicalAddress;
// import digital.inception.party.model.PhysicalAddressType;
// import digital.inception.reference.service.IReferenceService;
// import java.util.Objects;
// import jakarta.validation.ConstraintValidator;
// import jakarta.validation.ConstraintValidatorContext;
// import jakarta.validation.ValidationException;
// import org.hibernate.validator.constraintvalidation.HibernateConstraintValidatorContext;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.util.StringUtils;
//
/// **
// * The <b>ValidPhysicalAddressValidator</b> class implements the custom constraint validator for
// * validating a physical address.
// *
// * <p>NOTE: The <b>@Autowired</b> constructor is not used when Spring Boot automatically invokes
// the
// * JSR 380 validation when persisting an entity. Instead, the default constructor is invoked when
// * the validator is initialized by Hibernate. As a result the custom validation that requires the
// * injected services will not be executed.
// *
// * @author Marcus Portmann
// */
// public class ValidPhysicalAddressValidator
//    implements ConstraintValidator<ValidPhysicalAddress, PhysicalAddress> {
//
//  private final IPartyReferenceService partyReferenceService;
//
//  private final IReferenceService referenceService;
//
//  @Autowired
//  public ValidPhysicalAddressValidator(
//      IPartyReferenceService partyReferenceService, IReferenceService referenceService) {
//    this.partyReferenceService = partyReferenceService;
//    this.referenceService = referenceService;
//  }
//
//  public ValidPhysicalAddressValidator() {
//    this.partyReferenceService = null;
//    this.referenceService = null;
//  }
//
//  @Override
//  public void initialize(ValidPhysicalAddress constraintAnnotation) {}
//
//  @Override
//  public boolean isValid(
//      PhysicalAddress physicalAddress, ConstraintValidatorContext constraintValidatorContext) {
//
//    /*
//    The following validation rules are applied for the different address types:
//
//
// +----------------------+-----------+----------+----------+---------------+----------+----------+----------+--------------+
//    |                      |  Building | Complex  |   Farm   | International |  Postal  |   Site
// |  Street  | Unstructured |
//
// +----------------------+-----------+----------+----------+---------------+----------+----------+----------+--------------+
//    | Building Floor       | Optional  | Invalid  | Invalid  | Invalid       | Invalid  | Invalid
// | Invalid  | Invalid      |
//    | Building Name        | Required  | Invalid  | Invalid  | Invalid       | Invalid  | Invalid
// | Invalid  | Invalid      |
//    | Building Room        | Optional  | Invalid  | Invalid  | Invalid       | Invalid  | Invalid
// | Invalid  | Invalid      |
//    | City                 | Required  | Required | Optional | Optional      | Required | Required
// | Required | Optional     |
//    | Complex Name         | Invalid   | Required | Invalid  | Invalid       | Invalid  | Invalid
// | Invalid  | Invalid      |
//    | Complex Unit Number  | Invalid   | Required | Invalid  | Invalid       | Invalid  | Invalid
// | Invalid  | Invalid      |
//    | Country              | Required  | Required | Required | Required      | Required | Required
// | Required | Required     |
//    | Farm Description     | Invalid   | Invalid  | Optional | Invalid       | Invalid  | Invalid
// | Invalid  | Invalid      |
//    | Farm Name            | Invalid   | Invalid  | Optional | Invalid       | Invalid  | Invalid
// | Invalid  | Invalid      |
//    | Farm Number          | Invalid   | Invalid  | Required | Required      | Invalid  | Invalid
// | Invalid  | Invalid      |
//    | Line 1               | Invalid   | Invalid  | Invalid  | Required      | Required | Invalid
// | Invalid  | Required     |
//    | Line 2               | Invalid   | Invalid  | Invalid  | Optional      | Optional | Invalid
// | Invalid  | Optional     |
//    | Line 3               | Invalid   | Invalid  | Invalid  | Optional      | Optional | Invalid
// | Invalid  | Optional     |
//    | Line 4               | Invalid   | Invalid  | Invalid  | Optional      | Optional | Invalid
// | Invalid  | Optional     |
//    | Postal Code Required | Required  | Required | Required | Required      | Required | Required
// | Required | Required     |
//    | Region               | Optional  | Optional | Optional | Optional      | Optional | Optional
// | Optional | Optional     |
//    | Site Block           | Invalid   | Invalid  | Invalid  | Invalid       | Invalid  | Required
// | Invalid  | Invalid      |
//    | Site Number          | Invalid   | Invalid  | Invalid  | Invalid       | Invalid  | Required
// | Invalid  | Invalid      |
//    | Street Name          | Required  | Required | Optional | Invalid       | Invalid  | Optional
// | Required | Invalid      |
//    | Street Number        | Optional  | Optional | Optional | Invalid       | Invalid  | Optional
// | Optional | Invalid      |
//    | Suburb               | Optional  | Optional | Optional | Invalid       | Optional | Invalid
// | Optional | Invalid      |
//
// +----------------------+-----------+----------+----------+---------------+----------+----------+----------+--------------+
//    */
//
//    if ((partyReferenceService != null) && (referenceService != null)) {
//      boolean isValid = true;
//
//      // Disable the default constraint violation
//      constraintValidatorContext.disableDefaultConstraintViolation();
//
//      HibernateConstraintValidatorContext hibernateConstraintValidatorContext =
//          constraintValidatorContext.unwrap(HibernateConstraintValidatorContext.class);
//
//      try {
//        if (!partyReferenceService.isValidPhysicalAddressType(physicalAddress.getType())) {
//          hibernateConstraintValidatorContext
//              .addMessageParameter("type", physicalAddress.getType())
//              .buildConstraintViolationWithTemplate(
//
// "{digital.inception.party.constraints.ValidPhysicalAddress.invalidType.message}")
//              .addPropertyNode("type")
//              .addConstraintViolation();
//
//          isValid = false;
//        } else {
//          switch (physicalAddress.getType()) {
//              // Validate a building address
//            case PhysicalAddressType.BUILDING:
//              if (!StringUtils.hasText(physicalAddress.getBuildingName())) {
//                hibernateConstraintValidatorContext
//                    .buildConstraintViolationWithTemplate(
//
// "{digital.inception.party.constraints.ValidPhysicalAddress.buildingNameRequired.message}")
//                    .addPropertyNode("buildingName")
//                    .addConstraintViolation();
//
//                isValid = false;
//              }
//
//              if (!StringUtils.hasText(physicalAddress.getCity())) {
//                hibernateConstraintValidatorContext
//                    .buildConstraintViolationWithTemplate(
//
// "{digital.inception.party.constraints.ValidPhysicalAddress.cityRequired.message}")
//                    .addPropertyNode("city")
//                    .addConstraintViolation();
//
//                isValid = false;
//              }
//
//              if (!StringUtils.hasText(physicalAddress.getStreetName())) {
//                hibernateConstraintValidatorContext
//                    .buildConstraintViolationWithTemplate(
//
// "{digital.inception.party.constraints.ValidPhysicalAddress.streetNameRequired.message}")
//                    .addPropertyNode("streetName")
//                    .addConstraintViolation();
//
//                isValid = false;
//              }
//              break;
//              // Validate a complex address
//            case PhysicalAddressType.COMPLEX:
//              if (!StringUtils.hasText(physicalAddress.getCity())) {
//                hibernateConstraintValidatorContext
//                    .buildConstraintViolationWithTemplate(
//
// "{digital.inception.party.constraints.ValidPhysicalAddress.cityRequired.message}")
//                    .addPropertyNode("city")
//                    .addConstraintViolation();
//
//                isValid = false;
//              }
//
//              if (!StringUtils.hasText(physicalAddress.getComplexName())) {
//                hibernateConstraintValidatorContext
//                    .buildConstraintViolationWithTemplate(
//
// "{digital.inception.party.constraints.ValidPhysicalAddress.complexNameRequired.message}")
//                    .addPropertyNode("complexName")
//                    .addConstraintViolation();
//
//                isValid = false;
//              }
//
//              if (!StringUtils.hasText(physicalAddress.getComplexUnitNumber())) {
//                hibernateConstraintValidatorContext
//                    .buildConstraintViolationWithTemplate(
//
// "{digital.inception.party.constraints.ValidPhysicalAddress.complexUnitNumberRequired.message}")
//                    .addPropertyNode("complexNumber")
//                    .addConstraintViolation();
//
//                isValid = false;
//              }
//
//              if (!StringUtils.hasText(physicalAddress.getStreetName())) {
//                hibernateConstraintValidatorContext
//                    .buildConstraintViolationWithTemplate(
//
// "{digital.inception.party.constraints.ValidPhysicalAddress.streetNameRequired.message}")
//                    .addPropertyNode("streetName")
//                    .addConstraintViolation();
//
//                isValid = false;
//              }
//              break;
//              // Validate a farm address
//            case PhysicalAddressType.FARM:
//              if (!StringUtils.hasText(physicalAddress.getFarmNumber())) {
//                hibernateConstraintValidatorContext
//                    .buildConstraintViolationWithTemplate(
//
// "{digital.inception.party.constraints.ValidPhysicalAddress.farmNumberRequired.message}")
//                    .addPropertyNode("farmNumber")
//                    .addConstraintViolation();
//
//                isValid = false;
//              }
//              break;
//              // Validate an international address
//            case PhysicalAddressType.INTERNATIONAL:
//              if (!StringUtils.hasText(physicalAddress.getLine1())) {
//                hibernateConstraintValidatorContext
//                    .buildConstraintViolationWithTemplate(
//
// "{digital.inception.party.constraints.ValidPhysicalAddress.line1InternationalRequired.message}")
//                    .addPropertyNode("line1")
//                    .addConstraintViolation();
//
//                isValid = false;
//              }
//
//              if (StringUtils.hasText(physicalAddress.getStreetName())) {
//                hibernateConstraintValidatorContext
//                    .buildConstraintViolationWithTemplate(
//
// "{digital.inception.party.constraints.ValidPhysicalAddress.streetNameNotSupported.message}")
//                    .addPropertyNode("streetName")
//                    .addConstraintViolation();
//
//                isValid = false;
//              }
//
//              if (StringUtils.hasText(physicalAddress.getStreetNumber())) {
//                hibernateConstraintValidatorContext
//                    .buildConstraintViolationWithTemplate(
//
// "{digital.inception.party.constraints.ValidPhysicalAddress.streetNumberNotSupported.message}")
//                    .addPropertyNode("streetNumber")
//                    .addConstraintViolation();
//
//                isValid = false;
//              }
//
//              if (StringUtils.hasText(physicalAddress.getSuburb())) {
//                hibernateConstraintValidatorContext
//                    .buildConstraintViolationWithTemplate(
//
// "{digital.inception.party.constraints.ValidPhysicalAddress.suburbNotSupported.message}")
//                    .addPropertyNode("suburb")
//                    .addConstraintViolation();
//
//                isValid = false;
//              }
//              break;
//              // Validate a postal address
//            case PhysicalAddressType.POSTAL:
//              if (!StringUtils.hasText(physicalAddress.getLine1())) {
//                hibernateConstraintValidatorContext
//                    .buildConstraintViolationWithTemplate(
//
// "{digital.inception.party.constraints.ValidPhysicalAddress.line1PostalRequired.message}")
//                    .addPropertyNode("line1")
//                    .addConstraintViolation();
//
//                isValid = false;
//              }
//
//              if (StringUtils.hasText(physicalAddress.getStreetName())) {
//                hibernateConstraintValidatorContext
//                    .buildConstraintViolationWithTemplate(
//
// "{digital.inception.party.constraints.ValidPhysicalAddress.streetNameNotSupported.message}")
//                    .addPropertyNode("streetName")
//                    .addConstraintViolation();
//
//                isValid = false;
//              }
//
//              if (StringUtils.hasText(physicalAddress.getStreetNumber())) {
//                hibernateConstraintValidatorContext
//                    .buildConstraintViolationWithTemplate(
//
// "{digital.inception.party.constraints.ValidPhysicalAddress.streetNumberNotSupported.message}")
//                    .addPropertyNode("streetNumber")
//                    .addConstraintViolation();
//
//                isValid = false;
//              }
//              break;
//              // Validate a site address
//            case PhysicalAddressType.SITE:
//              if (!StringUtils.hasText(physicalAddress.getCity())) {
//                hibernateConstraintValidatorContext
//                    .buildConstraintViolationWithTemplate(
//
// "{digital.inception.party.constraints.ValidPhysicalAddress.cityRequired.message}")
//                    .addPropertyNode("city")
//                    .addConstraintViolation();
//
//                isValid = false;
//              }
//
//              if (!StringUtils.hasText(physicalAddress.getSiteBlock())) {
//                hibernateConstraintValidatorContext
//                    .buildConstraintViolationWithTemplate(
//
// "{digital.inception.party.constraints.ValidPhysicalAddress.siteBlockRequired.message}")
//                    .addPropertyNode("siteBlock")
//                    .addConstraintViolation();
//
//                isValid = false;
//              }
//
//              if (!StringUtils.hasText(physicalAddress.getSiteNumber())) {
//                hibernateConstraintValidatorContext
//                    .buildConstraintViolationWithTemplate(
//
// "{digital.inception.party.constraints.ValidPhysicalAddress.siteNumberRequired.message}")
//                    .addPropertyNode("siteNumber")
//                    .addConstraintViolation();
//
//                isValid = false;
//              }
//              break;
//              // Validate a street address
//            case PhysicalAddressType.STREET:
//              if (!StringUtils.hasText(physicalAddress.getCity())) {
//                hibernateConstraintValidatorContext
//                    .buildConstraintViolationWithTemplate(
//
// "{digital.inception.party.constraints.ValidPhysicalAddress.cityRequired.message}")
//                    .addPropertyNode("city")
//                    .addConstraintViolation();
//
//                isValid = false;
//              }
//
//              if (!StringUtils.hasText(physicalAddress.getStreetName())) {
//                hibernateConstraintValidatorContext
//                    .buildConstraintViolationWithTemplate(
//
// "{digital.inception.party.constraints.ValidPhysicalAddress.streetNameRequired.message}")
//                    .addPropertyNode("streetName")
//                    .addConstraintViolation();
//
//                isValid = false;
//              }
//
//              //      if (!StringUtils.hasText(physicalAddress.getStreetNumber())) {
//              //        context.buildConstraintViolationWithTemplate(
//              //
//              //
// "{digital.inception.party.constraints.ValidPhysicalAddress.streetNumberRequired.message}")
//              //            .addConstraintViolation();
//              //
//              //        isValid = false;
//              //      }
//              break;
//              // Validate an unstructured address
//            case PhysicalAddressType.UNSTRUCTURED:
//              if (!StringUtils.hasText(physicalAddress.getLine1())) {
//                hibernateConstraintValidatorContext
//                    .buildConstraintViolationWithTemplate(
//
// "{digital.inception.party.constraints.ValidPhysicalAddress.line1UnstructuredRequired.message}")
//                    .addPropertyNode("line1")
//                    .addConstraintViolation();
//
//                isValid = false;
//              }
//
//              if (StringUtils.hasText(physicalAddress.getStreetName())) {
//                hibernateConstraintValidatorContext
//                    .buildConstraintViolationWithTemplate(
//
// "{digital.inception.party.constraints.ValidPhysicalAddress.streetNameNotSupported.message}")
//                    .addPropertyNode("streetName")
//                    .addConstraintViolation();
//
//                isValid = false;
//              }
//
//              if (StringUtils.hasText(physicalAddress.getStreetNumber())) {
//                hibernateConstraintValidatorContext
//                    .buildConstraintViolationWithTemplate(
//
// "{digital.inception.party.constraints.ValidPhysicalAddress.streetNumberNotSupported.message}")
//                    .addPropertyNode("streetNumber")
//                    .addConstraintViolation();
//
//                isValid = false;
//              }
//
//              if (StringUtils.hasText(physicalAddress.getSuburb())) {
//                hibernateConstraintValidatorContext
//                    .buildConstraintViolationWithTemplate(
//
// "{digital.inception.party.constraints.ValidPhysicalAddress.suburbNotSupported.message}")
//                    .addPropertyNode("suburb")
//                    .addConstraintViolation();
//
//                isValid = false;
//              }
//              break;
//          }
//
//          if (!StringUtils.hasText(physicalAddress.getCountry())) {
//            hibernateConstraintValidatorContext
//                .buildConstraintViolationWithTemplate(
//
// "{digital.inception.party.constraints.ValidPhysicalAddress.countryRequired.message}")
//                .addPropertyNode("country")
//                .addConstraintViolation();
//
//            isValid = false;
//          }
//
//          if (!StringUtils.hasText(physicalAddress.getPostalCode())) {
//            hibernateConstraintValidatorContext
//                .buildConstraintViolationWithTemplate(
//
// "{digital.inception.party.constraints.ValidPhysicalAddress.postalCodeRequired.message}")
//                .addPropertyNode("postalCode")
//                .addConstraintViolation();
//
//            isValid = false;
//          }
//
//          if (StringUtils.hasText(physicalAddress.getSuburb())
//              && (!StringUtils.hasText(physicalAddress.getCity()))) {
//            hibernateConstraintValidatorContext
//                .buildConstraintViolationWithTemplate(
//
// "{digital.inception.party.constraints.ValidPhysicalAddress.cityRequired.message}")
//                .addPropertyNode("city")
//                .addConstraintViolation();
//
//            isValid = false;
//          }
//
//          /*
//           * Check that building address fields have not been specified for an address that is not
// a
//           * building address.
//           */
//          if (!physicalAddress.getType().equals(PhysicalAddressType.BUILDING)) {
//            if (StringUtils.hasText(physicalAddress.getBuildingFloor())) {
//              hibernateConstraintValidatorContext
//                  .buildConstraintViolationWithTemplate(
//
// "{digital.inception.party.constraints.ValidPhysicalAddress.buildingFloorNotSupported.message}")
//                  .addPropertyNode("buildingFloor")
//                  .addConstraintViolation();
//
//              isValid = false;
//            }
//
//            if (StringUtils.hasText(physicalAddress.getBuildingName())) {
//              hibernateConstraintValidatorContext
//                  .buildConstraintViolationWithTemplate(
//
// "{digital.inception.party.constraints.ValidPhysicalAddress.buildingNameNotSupported.message}")
//                  .addPropertyNode("buildingName")
//                  .addConstraintViolation();
//
//              isValid = false;
//            }
//
//            if (StringUtils.hasText(physicalAddress.getBuildingRoom())) {
//              hibernateConstraintValidatorContext
//                  .buildConstraintViolationWithTemplate(
//
// "{digital.inception.party.constraints.ValidPhysicalAddress.buildingRoomNotSupported.message}")
//                  .addPropertyNode("buildingRoom")
//                  .addConstraintViolation();
//
//              isValid = false;
//            }
//          }
//
//          /*
//           * Check that complex address fields have not been specified for an address that is not
// a
//           * complex address.
//           */
//          if (!physicalAddress.getType().equals(PhysicalAddressType.COMPLEX)) {
//            if (StringUtils.hasText(physicalAddress.getComplexName())) {
//              hibernateConstraintValidatorContext
//                  .buildConstraintViolationWithTemplate(
//
// "{digital.inception.party.constraints.ValidPhysicalAddress.complexNameNotSupported.message}")
//                  .addPropertyNode("complexName")
//                  .addConstraintViolation();
//
//              isValid = false;
//            }
//
//            if (StringUtils.hasText(physicalAddress.getComplexUnitNumber())) {
//              hibernateConstraintValidatorContext
//                  .buildConstraintViolationWithTemplate(
//
// "{digital.inception.party.constraints.ValidPhysicalAddress.complexUnitNumberNotSupported.message}")
//                  .addPropertyNode("complexUnitNumber")
//                  .addConstraintViolation();
//
//              isValid = false;
//            }
//          }
//
//          /*
//           * Check that farm address fields have not been specified for an address that is not a
//           * farm address.
//           */
//          if (!physicalAddress.getType().equals(PhysicalAddressType.FARM)) {
//            if (StringUtils.hasText(physicalAddress.getFarmDescription())) {
//              hibernateConstraintValidatorContext
//                  .buildConstraintViolationWithTemplate(
//
// "{digital.inception.party.constraints.ValidPhysicalAddress.farmDescriptionNotSupported.message}")
//                  .addPropertyNode("farmDescription")
//                  .addConstraintViolation();
//
//              isValid = false;
//            }
//
//            if (StringUtils.hasText(physicalAddress.getFarmName())) {
//              hibernateConstraintValidatorContext
//                  .buildConstraintViolationWithTemplate(
//
// "{digital.inception.party.constraints.ValidPhysicalAddress.farmNameNotSupported.message}")
//                  .addPropertyNode("farmName")
//                  .addConstraintViolation();
//
//              isValid = false;
//            }
//
//            if (StringUtils.hasText(physicalAddress.getFarmNumber())) {
//              hibernateConstraintValidatorContext
//                  .buildConstraintViolationWithTemplate(
//
// "{digital.inception.party.constraints.ValidPhysicalAddress.farmNumberNotSupported.message}")
//                  .addPropertyNode("farmNumber")
//                  .addConstraintViolation();
//
//              isValid = false;
//            }
//          }
//
//          /*
//           * Check that site address fields have not been specified for an address that is not a
//           * site address.
//           */
//          if (!physicalAddress.getType().equals(PhysicalAddressType.SITE)) {
//            if (StringUtils.hasText(physicalAddress.getSiteBlock())) {
//              hibernateConstraintValidatorContext
//                  .buildConstraintViolationWithTemplate(
//
// "{digital.inception.party.constraints.ValidPhysicalAddress.siteBlockNotSupported.message}")
//                  .addPropertyNode("siteBlock")
//                  .addConstraintViolation();
//
//              isValid = false;
//            }
//
//            if (StringUtils.hasText(physicalAddress.getSiteNumber())) {
//              hibernateConstraintValidatorContext
//                  .buildConstraintViolationWithTemplate(
//
// "{digital.inception.party.constraints.ValidPhysicalAddress.siteNumberNotSupported.message}")
//                  .addPropertyNode("siteNumber")
//                  .addConstraintViolation();
//
//              isValid = false;
//            }
//          }
//
//          /*
//           * Check that international, postal and unstructured address fields have not been
// specified for
//           * an address that is not an international address, postal address or unstructured
// address.
//           */
//          if ((!physicalAddress.getType().equals(PhysicalAddressType.INTERNATIONAL))
//              && (!physicalAddress.getType().equals(PhysicalAddressType.POSTAL))
//              && (!Objects.equals(physicalAddress.getType(), PhysicalAddressType.UNSTRUCTURED))) {
//            if (StringUtils.hasText(physicalAddress.getLine1())) {
//              hibernateConstraintValidatorContext
//                  .buildConstraintViolationWithTemplate(
//
// "{digital.inception.party.constraints.ValidPhysicalAddress.line1NotSupported.message}")
//                  .addPropertyNode("line1")
//                  .addConstraintViolation();
//
//              isValid = false;
//            }
//
//            if (StringUtils.hasText(physicalAddress.getLine2())) {
//              hibernateConstraintValidatorContext
//                  .buildConstraintViolationWithTemplate(
//
// "{digital.inception.party.constraints.ValidPhysicalAddress.line2NotSupported.message}")
//                  .addPropertyNode("line2")
//                  .addConstraintViolation();
//
//              isValid = false;
//            }
//
//            if (StringUtils.hasText(physicalAddress.getLine3())) {
//              hibernateConstraintValidatorContext
//                  .buildConstraintViolationWithTemplate(
//
// "{digital.inception.party.constraints.ValidPhysicalAddress.line3NotSupported.message}")
//                  .addPropertyNode("line3")
//                  .addConstraintViolation();
//
//              isValid = false;
//            }
//
//            if (StringUtils.hasText(physicalAddress.getLine4())) {
//              hibernateConstraintValidatorContext
//                  .buildConstraintViolationWithTemplate(
//
// "{digital.inception.party.constraints.ValidPhysicalAddress.line4NotSupported.message}")
//                  .addPropertyNode("line4")
//                  .addConstraintViolation();
//
//              isValid = false;
//            }
//          }
//
//          if (StringUtils.hasText(physicalAddress.getCountry())
//              && (!referenceService.isValidCountry(physicalAddress.getCountry()))) {
//            hibernateConstraintValidatorContext
//                .addMessageParameter("country", physicalAddress.getCountry())
//                .buildConstraintViolationWithTemplate(
//
// "{digital.inception.party.constraints.ValidPhysicalAddress.invalidCountryCode.message}")
//                .addPropertyNode("country")
//                .addConstraintViolation();
//
//            isValid = false;
//          }
//        }
//
//        for (String purpose : physicalAddress.getPurposes()) {
//          if (!partyReferenceService.isValidPhysicalAddressPurpose(purpose)) {
//            hibernateConstraintValidatorContext
//                .addMessageParameter("purpose", purpose)
//                .buildConstraintViolationWithTemplate(
//
// "{digital.inception.party.constraints.ValidPhysicalAddress.invalidPurpose.message}")
//                .addPropertyNode("purposes")
//                .addConstraintViolation();
//
//            isValid = false;
//          }
//        }
//
//        if (StringUtils.hasText(physicalAddress.getRole())) {
//          if (!partyReferenceService.isValidPhysicalAddressRole(physicalAddress.getRole())) {
//            hibernateConstraintValidatorContext
//                .addMessageParameter("role", physicalAddress.getRole())
//                .buildConstraintViolationWithTemplate(
//
// "{digital.inception.party.constraints.ValidPhysicalAddress.invalidRole.message}")
//                .addPropertyNode("role")
//                .addConstraintViolation();
//
//            isValid = false;
//          }
//        }
//      } catch (Throwable e) {
//        throw new ValidationException("Failed to validate the physical address", e);
//      }
//
//      return isValid;
//    } else {
//      return true;
//    }
//  }
// }
//
/// *
// See: https://ozh.github.io/ascii-tables/
//
// , Building,Complex,Farm,International,Postal,Site,Street,Unstructured
// Building Floor,Optional,Invalid,Invalid,Invalid,Invalid,Invalid,Invalid,Invalid
// Building Name,Required,Invalid,Invalid,Invalid,Invalid,Invalid,Invalid,Invalid
// Building Room,Optional,Invalid,Invalid,Invalid,Invalid,Invalid,Invalid,Invalid
// City,Required,Required,Optional,Optional,Required,Required,Required,Optional
// Complex Name,Invalid,Required,Invalid,Invalid,Invalid,Invalid,Invalid,Invalid
// Complex Unit Number,Invalid,Required,Invalid,Invalid,Invalid,Invalid,Invalid,Invalid
// Country,Required,Required,Required,Required,Required,Required,Required,Required
// Farm Description,Invalid,Invalid,Optional,Invalid,Invalid,Invalid,Invalid,Invalid
// Farm Name,Invalid,Invalid,Optional,Invalid,Invalid,Invalid,Invalid,Invalid
// Farm Number,Invalid,Invalid,Required,Required,Invalid,Invalid,Invalid,Invalid
// Line 1,Invalid,Invalid,Invalid,Required,Required,Invalid,Invalid,Required
// Line 2,Invalid,Invalid,Invalid,Optional,Optional,Invalid,Invalid,Optional
// Line 3,Invalid,Invalid,Invalid,Optional,Optional,Invalid,Invalid,Optional
// Line 4,Invalid,Invalid,Invalid,Optional,Optional,Invalid,Invalid,Optional
// Postal Code Required,Required,Required,Required,Required,Required,Required,Required,Required
// Region,Optional,Optional,Optional,Optional,Optional,Optional,Optional,Optional
// Site Block,Invalid,Invalid,Invalid,Invalid,Invalid,Required,Invalid,Invalid
// Site Number,Invalid,Invalid,Invalid,Invalid,Invalid,Required,Invalid,Invalid
// Street Name,Required,Required,Optional,Invalid,Invalid,Optional,Required,Invalid
// Street Number,Optional,Optional,Optional,Invalid,Invalid,Optional,Optional,Invalid
// Suburb,Optional,Optional,Optional,Invalid,Optional,Invalid,Optional,Invalid
// */
