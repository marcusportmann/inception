/*
 * Copyright 2022 Marcus Portmann
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

package digital.inception.party.controller;

import digital.inception.api.SecureApiController;
import digital.inception.core.service.InvalidArgumentException;
import digital.inception.core.service.ServiceUnavailableException;
import digital.inception.party.model.*;
import digital.inception.party.service.IPartyReferenceService;
import java.util.List;
import java.util.UUID;
import org.springframework.context.ApplicationContext;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;

/**
 * The <b>PartyReferenceApiController</b> class.
 *
 * @author Marcus Portmann
 */
@RestController
@CrossOrigin
public class PartyReferenceApiController extends SecureApiController
    implements IPartyReferenceApiController {

  /** The Party Reference Service. */
  private final IPartyReferenceService partyReferenceService;

  /**
   * Constructs a new <b>PartyReferenceRestController</b>.
   *
   * @param applicationContext the Spring application context
   * @param partyReferenceService the Party Reference Service
   */
  public PartyReferenceApiController(
      ApplicationContext applicationContext, IPartyReferenceService partyReferenceService) {
    super(applicationContext);

    this.partyReferenceService = partyReferenceService;
  }

  @Override
  public List<AssociationPropertyType> getAssociationPropertyTypes(UUID tenantId, String localeId)
      throws InvalidArgumentException, ServiceUnavailableException {
    return partyReferenceService.getAssociationPropertyTypes(tenantId, localeId);
  }

  @Override
  public List<AssociationType> getAssociationTypes(UUID tenantId, String localeId)
      throws InvalidArgumentException, ServiceUnavailableException {
    return partyReferenceService.getAssociationTypes(tenantId, localeId);
  }

  @Override
  public List<AttributeTypeCategory> getAttributeTypeCategories(UUID tenantId, String localeId)
      throws InvalidArgumentException, ServiceUnavailableException {
    return partyReferenceService.getAttributeTypeCategories(tenantId, localeId);
  }

  @Override
  public List<AttributeType> getAttributeTypes(UUID tenantId, String localeId)
      throws InvalidArgumentException, ServiceUnavailableException {
    return partyReferenceService.getAttributeTypes(tenantId, localeId);
  }

  @Override
  public List<ConsentType> getConsentTypes(UUID tenantId, String localeId)
      throws InvalidArgumentException, ServiceUnavailableException {
    return partyReferenceService.getConsentTypes(tenantId, localeId);
  }

  @Override
  public List<ContactMechanismPurpose> getContactMechanismPurposes(UUID tenantId, String localeId)
      throws InvalidArgumentException, ServiceUnavailableException {
    return partyReferenceService.getContactMechanismPurposes(tenantId, localeId);
  }

  @Override
  public List<ContactMechanismRole> getContactMechanismRoles(UUID tenantId, String localeId)
      throws InvalidArgumentException, ServiceUnavailableException {
    return partyReferenceService.getContactMechanismRoles(tenantId, localeId);
  }

  @Override
  public List<ContactMechanismType> getContactMechanismTypes(UUID tenantId, String localeId)
      throws InvalidArgumentException, ServiceUnavailableException {
    return partyReferenceService.getContactMechanismTypes(tenantId, localeId);
  }

  @Override
  public List<EmploymentStatus> getEmploymentStatuses(UUID tenantId, String localeId)
      throws InvalidArgumentException, ServiceUnavailableException {
    return partyReferenceService.getEmploymentStatuses(tenantId, localeId);
  }

  @Override
  public List<EmploymentType> getEmploymentTypes(UUID tenantId, String localeId)
      throws InvalidArgumentException, ServiceUnavailableException {
    return partyReferenceService.getEmploymentTypes(tenantId, localeId);
  }

  @Override
  public List<ExternalReferenceType> getExternalReferenceTypes(UUID tenantId, String localeId)
      throws InvalidArgumentException, ServiceUnavailableException {
    return partyReferenceService.getExternalReferenceTypes(tenantId, localeId);
  }

  @Override
  public List<FieldOfStudy> getFieldsOfStudy(UUID tenantId, String localeId)
      throws InvalidArgumentException, ServiceUnavailableException {
    return partyReferenceService.getFieldsOfStudy(tenantId, localeId);
  }

  @Override
  public List<Gender> getGenders(UUID tenantId, String localeId)
      throws InvalidArgumentException, ServiceUnavailableException {
    return partyReferenceService.getGenders(tenantId, localeId);
  }

  @Override
  public List<IdentificationType> getIdentificationTypes(UUID tenantId, String localeId)
      throws InvalidArgumentException, ServiceUnavailableException {
    return partyReferenceService.getIdentificationTypes(tenantId, localeId);
  }

  @Override
  public List<IndustryClassificationCategory> getIndustryClassificationCategories(
      UUID tenantId, String localeId) throws InvalidArgumentException, ServiceUnavailableException {
    return partyReferenceService.getIndustryClassificationCategories(tenantId, localeId);
  }

  @Override
  public List<IndustryClassificationSystem> getIndustryClassificationSystems(
      UUID tenantId, String localeId) throws InvalidArgumentException, ServiceUnavailableException {
    return partyReferenceService.getIndustryClassificationSystems(tenantId, localeId);
  }

  @Override
  public List<IndustryClassification> getIndustryClassifications(UUID tenantId, String localeId)
      throws InvalidArgumentException, ServiceUnavailableException {
    return partyReferenceService.getIndustryClassifications(tenantId, localeId);
  }

  @Override
  public List<LinkType> getLinkTypes(UUID tenantId, String localeId)
      throws InvalidArgumentException, ServiceUnavailableException {
    return partyReferenceService.getLinkTypes(tenantId, localeId);
  }

  @Override
  public List<LockTypeCategory> getLockTypeCategories(UUID tenantId, String localeId)
      throws InvalidArgumentException, ServiceUnavailableException {
    return partyReferenceService.getLockTypeCategories(tenantId, localeId);
  }

  @Override
  public List<LockType> getLockTypes(UUID tenantId, String localeId)
      throws InvalidArgumentException, ServiceUnavailableException {
    return partyReferenceService.getLockTypes(tenantId, localeId);
  }

  @Override
  public List<MandataryRole> getMandataryRoles(UUID tenantId, String localeId)
      throws InvalidArgumentException, ServiceUnavailableException {
    return partyReferenceService.getMandataryRoles(tenantId, localeId);
  }

  @Override
  public List<MandatePropertyType> getMandatePropertyTypes(UUID tenantId, String localeId)
      throws InvalidArgumentException, ServiceUnavailableException {
    return partyReferenceService.getMandatePropertyTypes(tenantId, localeId);
  }

  @Override
  public List<MandateType> getMandateTypes(UUID tenantId, String localeId)
      throws InvalidArgumentException, ServiceUnavailableException {
    return partyReferenceService.getMandateTypes(tenantId, localeId);
  }

  @Override
  public List<MaritalStatus> getMaritalStatuses(UUID tenantId, String localeId)
      throws InvalidArgumentException, ServiceUnavailableException {
    return partyReferenceService.getMaritalStatuses(tenantId, localeId);
  }

  @Override
  public List<MarriageType> getMarriageTypes(UUID tenantId, String localeId)
      throws InvalidArgumentException, ServiceUnavailableException {
    return partyReferenceService.getMarriageTypes(tenantId, localeId);
  }

  @Override
  public List<NextOfKinType> getNextOfKinTypes(UUID tenantId, String localeId)
      throws InvalidArgumentException, ServiceUnavailableException {
    return partyReferenceService.getNextOfKinTypes(tenantId, localeId);
  }

  @Override
  public List<Occupation> getOccupations(UUID tenantId, String localeId)
      throws InvalidArgumentException, ServiceUnavailableException {
    return partyReferenceService.getOccupations(tenantId, localeId);
  }

  @Override
  public List<PhysicalAddressPurpose> getPhysicalAddressPurposes(UUID tenantId, String localeId)
      throws InvalidArgumentException, ServiceUnavailableException {
    return partyReferenceService.getPhysicalAddressPurposes(tenantId, localeId);
  }

  @Override
  public List<PhysicalAddressRole> getPhysicalAddressRoles(UUID tenantId, String localeId)
      throws InvalidArgumentException, ServiceUnavailableException {
    return partyReferenceService.getPhysicalAddressRoles(tenantId, localeId);
  }

  @Override
  public List<PhysicalAddressType> getPhysicalAddressTypes(UUID tenantId, String localeId)
      throws InvalidArgumentException, ServiceUnavailableException {
    return partyReferenceService.getPhysicalAddressTypes(tenantId, localeId);
  }

  @Override
  public List<PreferenceTypeCategory> getPreferenceTypeCategories(UUID tenantId, String localeId)
      throws InvalidArgumentException, ServiceUnavailableException {
    return partyReferenceService.getPreferenceTypeCategories(tenantId, localeId);
  }

  @Override
  public List<PreferenceType> getPreferenceTypes(UUID tenantId, String localeId)
      throws InvalidArgumentException, ServiceUnavailableException {
    return partyReferenceService.getPreferenceTypes(tenantId, localeId);
  }

  @Override
  public List<QualificationType> getQualificationTypes(UUID tenantId, String localeId)
      throws InvalidArgumentException, ServiceUnavailableException {
    return partyReferenceService.getQualificationTypes(tenantId, localeId);
  }

  @Override
  public List<Race> getRaces(UUID tenantId, String localeId)
      throws InvalidArgumentException, ServiceUnavailableException {
    return partyReferenceService.getRaces(tenantId, localeId);
  }

  @Override
  public List<ResidencePermitType> getResidencePermitTypes(UUID tenantId, String localeId)
      throws InvalidArgumentException, ServiceUnavailableException {
    return partyReferenceService.getResidencePermitTypes(tenantId, localeId);
  }

  @Override
  public List<ResidencyStatus> getResidencyStatuses(UUID tenantId, String localeId)
      throws InvalidArgumentException, ServiceUnavailableException {
    return partyReferenceService.getResidencyStatuses(tenantId, localeId);
  }

  @Override
  public List<ResidentialType> getResidentialTypes(UUID tenantId, String localeId)
      throws InvalidArgumentException, ServiceUnavailableException {
    return partyReferenceService.getResidentialTypes(tenantId, localeId);
  }

  @Override
  public List<RolePurpose> getRolePurposes(UUID tenantId, String localeId)
      throws InvalidArgumentException, ServiceUnavailableException {
    return partyReferenceService.getRolePurposes(tenantId, localeId);
  }

  @Override
  public List<RoleTypeAttributeTypeConstraint> getRoleTypeAttributeTypeConstraints(String roleType)
      throws InvalidArgumentException, ServiceUnavailableException {
    return partyReferenceService.getRoleTypeAttributeTypeConstraints(roleType);
  }

  @Override
  public List<RoleTypePreferenceTypeConstraint> getRoleTypePreferenceTypeConstraints(
      String roleType) throws InvalidArgumentException, ServiceUnavailableException {
    return partyReferenceService.getRoleTypePreferenceTypeConstraints(roleType);
  }

  @Override
  public List<RoleType> getRoleTypes(UUID tenantId, String localeId)
      throws InvalidArgumentException, ServiceUnavailableException {
    return partyReferenceService.getRoleTypes(tenantId, localeId);
  }

  @Override
  public List<SegmentationType> getSegmentationTypes(UUID tenantId, String localeId)
      throws InvalidArgumentException, ServiceUnavailableException {
    return partyReferenceService.getSegmentationTypes(tenantId, localeId);
  }

  @Override
  public List<Segment> getSegments(UUID tenantId, String localeId)
      throws InvalidArgumentException, ServiceUnavailableException {
    return partyReferenceService.getSegments(tenantId, localeId);
  }

  @Override
  public List<SkillType> getSkillTypes(UUID tenantId, String localeId)
      throws InvalidArgumentException, ServiceUnavailableException {
    return partyReferenceService.getSkillTypes(tenantId, localeId);
  }

  @Override
  public List<SourceOfFundsType> getSourceOfFundsTypes(UUID tenantId, String localeId)
      throws InvalidArgumentException, ServiceUnavailableException {
    return partyReferenceService.getSourceOfFundsTypes(tenantId, localeId);
  }

  @Override
  public List<SourceOfWealthType> getSourceOfWealthTypes(UUID tenantId, String localeId)
      throws InvalidArgumentException, ServiceUnavailableException {
    return partyReferenceService.getSourceOfWealthTypes(tenantId, localeId);
  }

  @Override
  public List<StatusTypeCategory> getStatusTypeCategories(UUID tenantId, String localeId)
      throws InvalidArgumentException, ServiceUnavailableException {
    return partyReferenceService.getStatusTypeCategories(tenantId, localeId);
  }

  @Override
  public List<StatusType> getStatusTypes(UUID tenantId, String localeId)
      throws InvalidArgumentException, ServiceUnavailableException {
    return partyReferenceService.getStatusTypes(tenantId, localeId);
  }

  @Override
  public List<TaxNumberType> getTaxNumberTypes(UUID tenantId, String localeId)
      throws InvalidArgumentException, ServiceUnavailableException {
    return partyReferenceService.getTaxNumberTypes(tenantId, localeId);
  }

  @Override
  public List<TimeToContact> getTimesToContact(UUID tenantId, String localeId)
      throws InvalidArgumentException, ServiceUnavailableException {
    return partyReferenceService.getTimesToContact(tenantId, localeId);
  }

  @Override
  public List<Title> getTitles(UUID tenantId, String localeId)
      throws InvalidArgumentException, ServiceUnavailableException {
    return partyReferenceService.getTitles(tenantId, localeId);
  }
}
