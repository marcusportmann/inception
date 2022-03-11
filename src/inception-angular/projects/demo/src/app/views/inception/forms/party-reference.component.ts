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

import {Component, OnDestroy, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {ActivatedRoute, Router} from '@angular/router';
import {
  AssociationPropertyType, AssociationType, AttributeType, AttributeTypeCategory, ConsentType,
  ContactMechanismPurpose, ContactMechanismRole, ContactMechanismType, EmploymentStatus,
  EmploymentType, ExternalReferenceType, FieldOfStudy, Gender, IdentityDocumentType,
  IndustryClassification, IndustryClassificationCategory, IndustryClassificationSystem, LockType,
  LockTypeCategory, MaritalStatus, MarriageType, NextOfKinType, Occupation, PartyReferenceService,
  PhysicalAddressPurpose, PhysicalAddressRole, PhysicalAddressType, PreferenceType,
  PreferenceTypeCategory, QualificationType, Race, ResidencePermitType, ResidencyStatus,
  ResidentialType, RolePurpose, RoleType, RoleTypeAttributeTypeConstraint,
  RoleTypePreferenceTypeConstraint, Segment, SegmentationType, SkillType, SourceOfFundsType,
  SourceOfWealthType, StatusType, StatusTypeCategory, TaxNumberType, TimeToContact, Title
} from 'ngx-inception/party';
import {ReplaySubject, Subject, Subscription} from 'rxjs';
import {debounceTime, first, map, startWith} from 'rxjs/operators';

/**
 * The PartyReferenceComponent class.
 *
 * @author Marcus Portmann
 */
@Component({
  templateUrl: 'party-reference.component.html'
})
export class PartyReferenceComponent implements OnInit, OnDestroy {

  filteredAssociationPropertyTypes$: Subject<AssociationPropertyType[]> = new ReplaySubject<AssociationPropertyType[]>();

  filteredAssociationTypes$: Subject<AssociationType[]> = new ReplaySubject<AssociationType[]>();

  filteredAttributeTypeCategories$: Subject<AttributeTypeCategory[]> = new ReplaySubject<AttributeTypeCategory[]>();

  filteredAttributeTypes$: Subject<AttributeType[]> = new ReplaySubject<AttributeType[]>();

  filteredConsentTypes$: Subject<ConsentType[]> = new ReplaySubject<ConsentType[]>();

  filteredContactMechanismPurposes$: Subject<ContactMechanismPurpose[]> = new ReplaySubject<ContactMechanismPurpose[]>();

  filteredContactMechanismRoles$: Subject<ContactMechanismRole[]> = new ReplaySubject<ContactMechanismRole[]>();

  filteredContactMechanismTypes$: Subject<ContactMechanismType[]> = new ReplaySubject<ContactMechanismType[]>();

  filteredEmploymentStatuses$: Subject<EmploymentStatus[]> = new ReplaySubject<EmploymentStatus[]>();

  filteredEmploymentTypes$: Subject<EmploymentType[]> = new ReplaySubject<EmploymentType[]>();

  filteredExternalReferenceTypes$: Subject<ExternalReferenceType[]> = new ReplaySubject<ExternalReferenceType[]>();

  filteredFieldsOfStudy$: Subject<FieldOfStudy[]> = new ReplaySubject<FieldOfStudy[]>();

  filteredGenders$: Subject<Gender[]> = new ReplaySubject<Gender[]>();

  filteredIdentityDocumentTypes$: Subject<IdentityDocumentType[]> = new ReplaySubject<IdentityDocumentType[]>();

  filteredIndustryClassificationCategories$: Subject<IndustryClassificationCategory[]> = new ReplaySubject<IndustryClassificationCategory[]>();

  filteredIndustryClassificationSystems$: Subject<IndustryClassificationSystem[]> = new ReplaySubject<IndustryClassificationSystem[]>();

  filteredIndustryClassifications$: Subject<IndustryClassification[]> = new ReplaySubject<IndustryClassification[]>();

  filteredLockTypeCategories$: Subject<LockTypeCategory[]> = new ReplaySubject<LockTypeCategory[]>();

  filteredLockTypes$: Subject<LockType[]> = new ReplaySubject<LockType[]>();

  filteredMaritalStatuses$: Subject<MaritalStatus[]> = new ReplaySubject<MaritalStatus[]>();

  filteredMarriageTypes$: Subject<MarriageType[]> = new ReplaySubject<MarriageType[]>();

  filteredNextOfKinTypes$: Subject<NextOfKinType[]> = new ReplaySubject<NextOfKinType[]>();

  filteredOccupations$: Subject<Occupation[]> = new ReplaySubject<Occupation[]>();

  filteredPhysicalAddressPurposes$: Subject<PhysicalAddressPurpose[]> = new ReplaySubject<PhysicalAddressPurpose[]>();

  filteredPhysicalAddressRoles$: Subject<PhysicalAddressRole[]> = new ReplaySubject<PhysicalAddressRole[]>();

  filteredPhysicalAddressTypes$: Subject<PhysicalAddressType[]> = new ReplaySubject<PhysicalAddressType[]>();

  filteredPreferenceTypeCategories$: Subject<PreferenceTypeCategory[]> = new ReplaySubject<PreferenceTypeCategory[]>();

  filteredPreferenceTypes$: Subject<PreferenceType[]> = new ReplaySubject<PreferenceType[]>();

  filteredQualificationTypes$: Subject<QualificationType[]> = new ReplaySubject<QualificationType[]>();

  filteredRaces$: Subject<Race[]> = new ReplaySubject<Race[]>();

  filteredResidencePermitTypes$: Subject<ResidencePermitType[]> = new ReplaySubject<ResidencePermitType[]>();

  filteredResidencyStatuses$: Subject<ResidencyStatus[]> = new ReplaySubject<ResidencyStatus[]>();

  filteredResidentialTypes$: Subject<ResidentialType[]> = new ReplaySubject<ResidentialType[]>();

  filteredRolePurposes$: Subject<RolePurpose[]> = new ReplaySubject<RolePurpose[]>();

  filteredRoleTypeAttributeTypeConstraints$: Subject<RoleTypeAttributeTypeConstraint[]> = new ReplaySubject<RoleTypeAttributeTypeConstraint[]>();

  filteredRoleTypePreferenceTypeConstraints$: Subject<RoleTypePreferenceTypeConstraint[]> = new ReplaySubject<RoleTypePreferenceTypeConstraint[]>();

  filteredRoleTypes$: Subject<RoleType[]> = new ReplaySubject<RoleType[]>();

  filteredSegmentationTypes$: Subject<SegmentationType[]> = new ReplaySubject<SegmentationType[]>();

  filteredSegments$: Subject<Segment[]> = new ReplaySubject<Segment[]>();

  filteredSkillTypes$: Subject<SkillType[]> = new ReplaySubject<SkillType[]>();

  filteredSourceOfFundsTypes$: Subject<SourceOfFundsType[]> = new ReplaySubject<SourceOfFundsType[]>();

  filteredSourceOfWealthTypes$: Subject<SourceOfWealthType[]> = new ReplaySubject<SourceOfWealthType[]>();

  filteredStatusTypeCategories$: Subject<StatusTypeCategory[]> = new ReplaySubject<StatusTypeCategory[]>();

  filteredStatusTypes$: Subject<StatusType[]> = new ReplaySubject<StatusType[]>();

  filteredTaxNumberTypes$: Subject<TaxNumberType[]> = new ReplaySubject<TaxNumberType[]>();

  filteredTimesToContact$: Subject<TimeToContact[]> = new ReplaySubject<TimeToContact[]>();

  partyReferenceForm: FormGroup;

  private subscriptions: Subscription = new Subscription();

  constructor(private router: Router, private activatedRoute: ActivatedRoute,
              private formBuilder: FormBuilder, private partyReferenceService: PartyReferenceService) {

    this.partyReferenceForm = this.formBuilder.group({
      // hideRequired: false,
      // floatLabel: 'auto',
      // eslint-disable-next-line
      associationPropertyType: ['', Validators.required],
      associationType: ['', Validators.required],
      attributeTypeCategory: ['', Validators.required],
      attributeType: ['', Validators.required],
      consentType: ['', Validators.required],
      contactMechanismPurpose: ['', Validators.required],
      contactMechanismRole: ['', Validators.required],
      contactMechanismType: ['', Validators.required],
      employmentStatus: ['', Validators.required],
      employmentType: ['', Validators.required],
      externalReferenceType: ['', Validators.required],
      fieldOfStudy: ['', Validators.required],
      gender: ['', Validators.required],
      identityDocumentType: ['', Validators.required],
      industryClassification: ['', Validators.required],
      industryClassificationCategory: ['', Validators.required],
      industryClassificationSystem: ['', Validators.required],
      lockTypeCategory: ['', Validators.required],
      lockType: ['', Validators.required],
      maritalStatus: ['', Validators.required],
      marriageType: ['', Validators.required],
      minorType: ['', Validators.required],
      nextOfKinType: ['', Validators.required],
      occupation: ['', Validators.required],
      physicalAddressPurpose: ['', Validators.required],
      physicalAddressRole: ['', Validators.required],
      physicalAddressType: ['', Validators.required],
      preferenceTypeCategory: ['', Validators.required],
      preferenceType: ['', Validators.required],
      qualificationType: ['', Validators.required],
      race: ['', Validators.required],
      residencePermitType: ['', Validators.required],
      residencyStatus: ['', Validators.required],
      residentialType: ['', Validators.required],
      rolePurpose: ['', Validators.required],
      roleTypeAttributeTypeConstraint: ['', Validators.required],
      roleTypePreferenceTypeConstraint: ['', Validators.required],
      roleType: ['', Validators.required],
      segmentationType: ['', Validators.required],
      segment: ['', Validators.required],
      skillType: ['', Validators.required],
      sourceOfFundsType: ['', Validators.required],
      sourceOfWealthType: ['', Validators.required],
      statusTypeCategory: ['', Validators.required],
      statusType: ['', Validators.required],
      taxNumberType: ['', Validators.required],
      timeToContact: ['', Validators.required],
      title: ['', Validators.required]
    });
  }

  displayAssociationPropertyType(associationPropertyType: AssociationPropertyType): string {
    if (!!associationPropertyType) {
      return associationPropertyType.name;
    } else {
      return '';
    }
  }

  displayAssociationType(associationType: AssociationType): string {
    if (!!associationType) {
      return associationType.name;
    } else {
      return '';
    }
  }

  displayAttributeType(attributeType: AttributeType): string {
    if (!!attributeType) {
      return attributeType.name;
    } else {
      return '';
    }
  }

  displayAttributeTypeCategory(attributeTypeCategory: AttributeTypeCategory): string {
    if (!!attributeTypeCategory) {
      return attributeTypeCategory.name;
    } else {
      return '';
    }
  }

  displayConsentType(consentType: ConsentType): string {
    if (!!consentType) {
      return consentType.name;
    } else {
      return '';
    }
  }

  displayContactMechanismPurpose(contactMechanismPurpose: ContactMechanismPurpose): string {
    if (!!contactMechanismPurpose) {
      return contactMechanismPurpose.name;
    } else {
      return '';
    }
  }

  displayContactMechanismRole(contactMechanismRole: ContactMechanismRole): string {
    if (!!contactMechanismRole) {
      return contactMechanismRole.name;
    } else {
      return '';
    }
  }

  displayContactMechanismType(contactMechanismType: ContactMechanismType): string {
    if (!!contactMechanismType) {
      return contactMechanismType.name;
    } else {
      return '';
    }
  }

  displayEmploymentStatus(employmentStatus: EmploymentStatus): string {
    if (!!employmentStatus) {
      return employmentStatus.name;
    } else {
      return '';
    }
  }

  displayEmploymentType(employmentType: EmploymentType): string {
    if (!!employmentType) {
      return employmentType.name;
    } else {
      return '';
    }
  }

  displayExternalReferenceType(externalReferenceType: ExternalReferenceType): string {
    if (!!externalReferenceType) {
      return externalReferenceType.name;
    } else {
      return '';
    }
  }

  displayFieldOfStudy(fieldOfStudy: FieldOfStudy): string {
    if (!!fieldOfStudy) {
      return fieldOfStudy.name;
    } else {
      return '';
    }
  }

  displayGender(gender: Gender): string {
    if (!!gender) {
      return gender.name;
    } else {
      return '';
    }
  }

  displayIdentityDocumentType(identityDocumentType: IdentityDocumentType): string {
    if (!!identityDocumentType) {
      return identityDocumentType.name;
    } else {
      return '';
    }
  }

  displayIndustryClassification(industryClassification: IndustryClassification): string {
    if (!!industryClassification) {
      return industryClassification.name;
    } else {
      return '';
    }
  }

  displayIndustryClassificationCategory(industryClassificationCategory: IndustryClassificationCategory): string {
    if (!!industryClassificationCategory) {
      return industryClassificationCategory.name;
    } else {
      return '';
    }
  }

  displayIndustryClassificationSystem(industryClassificationSystem: IndustryClassificationSystem): string {
    if (!!industryClassificationSystem) {
      return industryClassificationSystem.name;
    } else {
      return '';
    }
  }

  displayLockType(lockType: LockType): string {
    if (!!lockType) {
      return lockType.name;
    } else {
      return '';
    }
  }

  displayLockTypeCategory(lockTypeCategory: LockTypeCategory): string {
    if (!!lockTypeCategory) {
      return lockTypeCategory.name;
    } else {
      return '';
    }
  }

  displayMaritalStatus(maritalStatus: MaritalStatus): string {
    if (!!maritalStatus) {
      return maritalStatus.name;
    } else {
      return '';
    }
  }

  displayMarriageType(marriageType: MarriageType): string {
    if (!!marriageType) {
      return marriageType.name;
    } else {
      return '';
    }
  }

  displayNextOfKinType(nextOfKinType: NextOfKinType): string {
    if (!!nextOfKinType) {
      return nextOfKinType.name;
    } else {
      return '';
    }
  }

  displayOccupation(occupation: Occupation): string {
    if (!!occupation) {
      return occupation.name;
    } else {
      return '';
    }
  }

  displayPhysicalAddressPurpose(physicalAddressPurpose: PhysicalAddressPurpose): string {
    if (!!physicalAddressPurpose) {
      return physicalAddressPurpose.name;
    } else {
      return '';
    }
  }

  displayPhysicalAddressRole(physicalAddressRole: PhysicalAddressRole): string {
    if (!!physicalAddressRole) {
      return physicalAddressRole.name;
    } else {
      return '';
    }
  }

  displayPhysicalAddressType(physicalAddressType: PhysicalAddressType): string {
    if (!!physicalAddressType) {
      return physicalAddressType.name;
    } else {
      return '';
    }
  }

  displayPreferenceType(preferenceType: PreferenceType): string {
    if (!!preferenceType) {
      return preferenceType.name;
    } else {
      return '';
    }
  }

  displayPreferenceTypeCategory(preferenceTypeCategory: PreferenceTypeCategory): string {
    if (!!preferenceTypeCategory) {
      return preferenceTypeCategory.name;
    } else {
      return '';
    }
  }

  displayQualificationType(qualificationType: QualificationType): string {
    if (!!qualificationType) {
      return qualificationType.name;
    } else {
      return '';
    }
  }

  displayRace(race: Race): string {
    if (!!race) {
      return race.name;
    } else {
      return '';
    }
  }

  displayResidencePermitType(residencePermitType: ResidencePermitType): string {
    if (!!residencePermitType) {
      return residencePermitType.name;
    } else {
      return '';
    }
  }

  displayResidencyStatus(residencyStatus: ResidencyStatus): string {
    if (!!residencyStatus) {
      return residencyStatus.name;
    } else {
      return '';
    }
  }

  displayResidentialType(residentialType: ResidentialType): string {
    if (!!residentialType) {
      return residentialType.name;
    } else {
      return '';
    }
  }

  displayRolePurpose(rolePurpose: RolePurpose): string {
    if (!!rolePurpose) {
      return rolePurpose.name;
    } else {
      return '';
    }
  }

  displayRoleType(roleType: RoleType): string {
    if (!!roleType) {
      return roleType.name;
    } else {
      return '';
    }
  }

  displayRoleTypeAttributeTypeConstraint(roleTypeAttributeTypeConstraint: RoleTypeAttributeTypeConstraint): string {
    if (!!roleTypeAttributeTypeConstraint) {
      return roleTypeAttributeTypeConstraint.roleType + ' - ' + roleTypeAttributeTypeConstraint.attributeType;
    } else {
      return '';
    }
  }

  displayRoleTypePreferenceTypeConstraint(roleTypePreferenceTypeConstraint: RoleTypePreferenceTypeConstraint): string {
    if (!!roleTypePreferenceTypeConstraint) {
      return roleTypePreferenceTypeConstraint.roleType + ' - ' + roleTypePreferenceTypeConstraint.preferenceType;
    } else {
      return '';
    }
  }

  displaySegment(segment: Segment): string {
    if (!!segment) {
      return segment.name;
    } else {
      return '';
    }
  }

  displaySegmentationType(segmentationType: SegmentationType): string {
    if (!!segmentationType) {
      return segmentationType.name;
    } else {
      return '';
    }
  }

  displaySkillType(skillType: SkillType): string {
    if (!!skillType) {
      return skillType.name;
    } else {
      return '';
    }
  }

  displaySourceOfFundsType(sourceOfFundsType: SourceOfFundsType): string {
    if (!!sourceOfFundsType) {
      return sourceOfFundsType.name;
    } else {
      return '';
    }
  }

  displaySourceOfWealthType(sourceOfWealthType: SourceOfWealthType): string {
    if (!!sourceOfWealthType) {
      return sourceOfWealthType.name;
    } else {
      return '';
    }
  }

  displayStatusType(statusType: StatusType): string {
    if (!!statusType) {
      return statusType.name;
    } else {
      return '';
    }
  }

  displayStatusTypeCategory(statusTypeCategory: StatusTypeCategory): string {
    if (!!statusTypeCategory) {
      return statusTypeCategory.name;
    } else {
      return '';
    }
  }

  displayTaxNumberType(taxNumberType: TaxNumberType): string {
    if (!!taxNumberType) {
      return taxNumberType.name;
    } else {
      return '';
    }
  }

  displayTimeToContact(timeToContact: TimeToContact): string {
    if (!!timeToContact) {
      return timeToContact.name;
    } else {
      return '';
    }
  }

  ngOnDestroy(): void {
    this.subscriptions.unsubscribe();
  }

  ngOnInit(): void {
    this.partyReferenceService.getAssociationPropertyTypes().pipe(first()).subscribe((associationPropertyTypes: Map<string, AssociationPropertyType>) => {
      const associationPropertyTypeControl = this.partyReferenceForm.get('associationPropertyType');

      if (associationPropertyTypeControl) {
        this.subscriptions.add(associationPropertyTypeControl.valueChanges.pipe(
          startWith(''),
          debounceTime(500),
          map((value: string | AssociationPropertyType) => {
            if (typeof (value) === 'string') {
              value = value.toLowerCase();
            } else {
              value = value.name.toLowerCase();
            }

            let filteredAssociationPropertyTypes: AssociationPropertyType[] = [];

            for (const associationPropertyType of associationPropertyTypes.values()) {
              if (associationPropertyType.name.toLowerCase().indexOf(value) === 0) {
                filteredAssociationPropertyTypes.push(associationPropertyType);
              }
            }

            this.filteredAssociationPropertyTypes$.next(filteredAssociationPropertyTypes);
          })).subscribe());
      }
    });

    this.partyReferenceService.getAssociationTypes().pipe(first()).subscribe((associationTypes: Map<string, AssociationType>) => {
      const associationTypeControl = this.partyReferenceForm.get('associationType');

      if (associationTypeControl) {
        this.subscriptions.add(associationTypeControl.valueChanges.pipe(
          startWith(''),
          debounceTime(500),
          map((value: string | AssociationType) => {
            if (typeof (value) === 'string') {
              value = value.toLowerCase();
            } else {
              value = value.name.toLowerCase();
            }

            let filteredAssociationTypes: AssociationType[] = [];

            for (const associationType of associationTypes.values()) {
              if (associationType.name.toLowerCase().indexOf(value) === 0) {
                filteredAssociationTypes.push(associationType);
              }
            }

            this.filteredAssociationTypes$.next(filteredAssociationTypes);
          })).subscribe());
      }
    });

    this.partyReferenceService.getAttributeTypeCategories().pipe(first()).subscribe((attributeTypeCategories: Map<string, AttributeTypeCategory>) => {
      const attributeTypeCategoryControl = this.partyReferenceForm.get('attributeTypeCategory');

      if (attributeTypeCategoryControl) {
        this.subscriptions.add(attributeTypeCategoryControl.valueChanges.pipe(
          startWith(''),
          debounceTime(500),
          map((value: string | AttributeTypeCategory) => {
            if (typeof (value) === 'string') {
              value = value.toLowerCase();
            } else {
              value = value.name.toLowerCase();
            }

            let filteredAttributeTypeCategories: AttributeTypeCategory[] = [];

            for (const attributeTypeCategory of attributeTypeCategories.values()) {
              if (attributeTypeCategory.name.toLowerCase().indexOf(value) === 0) {
                filteredAttributeTypeCategories.push(attributeTypeCategory);
              }
            }

            this.filteredAttributeTypeCategories$.next(filteredAttributeTypeCategories);
          })).subscribe());
      }
    });

    this.partyReferenceService.getAttributeTypes().pipe(first()).subscribe((attributeTypes: Map<string, AttributeType>) => {
      const attributeTypeControl = this.partyReferenceForm.get('attributeType');

      if (attributeTypeControl) {
        this.subscriptions.add(attributeTypeControl.valueChanges.pipe(
          startWith(''),
          debounceTime(500),
          map((value: string | AttributeType) => {
            if (typeof (value) === 'string') {
              value = value.toLowerCase();
            } else {
              value = value.name.toLowerCase();
            }

            let filteredAttributeTypes: AttributeType[] = [];

            for (const attributeType of attributeTypes.values()) {
              if (attributeType.name.toLowerCase().indexOf(value) === 0) {
                filteredAttributeTypes.push(attributeType);
              }
            }

            this.filteredAttributeTypes$.next(filteredAttributeTypes);
          })).subscribe());
      }
    });

    this.partyReferenceService.getConsentTypes().pipe(first()).subscribe((consentTypes: Map<string, ConsentType>) => {
      const consentTypeControl = this.partyReferenceForm.get('consentType');

      if (consentTypeControl) {
        this.subscriptions.add(consentTypeControl.valueChanges.pipe(
          startWith(''),
          debounceTime(500),
          map((value: string | ConsentType) => {
            if (typeof (value) === 'string') {
              value = value.toLowerCase();
            } else {
              value = value.name.toLowerCase();
            }

            let filteredConsentTypes: ConsentType[] = [];

            for (const consentType of consentTypes.values()) {
              if (consentType.name.toLowerCase().indexOf(value) === 0) {
                filteredConsentTypes.push(consentType);
              }
            }

            this.filteredConsentTypes$.next(filteredConsentTypes);
          })).subscribe());
      }
    });

    this.partyReferenceService.getContactMechanismPurposes().pipe(first()).subscribe((contactMechanismPurposes: Map<string, ContactMechanismPurpose>) => {
      const contactMechanismPurposeControl = this.partyReferenceForm.get('contactMechanismPurpose');

      if (contactMechanismPurposeControl) {
        this.subscriptions.add(contactMechanismPurposeControl.valueChanges.pipe(
          startWith(''),
          debounceTime(500),
          map((value: string | ContactMechanismPurpose) => {
            if (typeof (value) === 'string') {
              value = value.toLowerCase();
            } else {
              value = value.name.toLowerCase();
            }

            let filteredContactMechanismPurposes: ContactMechanismPurpose[] = [];

            for (const contactMechanismPurpose of contactMechanismPurposes.values()) {
              if (contactMechanismPurpose.name.toLowerCase().indexOf(value) === 0) {
                filteredContactMechanismPurposes.push(contactMechanismPurpose);
              }
            }

            this.filteredContactMechanismPurposes$.next(filteredContactMechanismPurposes);
          })).subscribe());
      }
    });

    this.partyReferenceService.getContactMechanismRoles().pipe(first()).subscribe((contactMechanismRoles: Map<string, ContactMechanismRole>) => {
      const contactMechanismRoleControl = this.partyReferenceForm.get('contactMechanismRole');

      if (contactMechanismRoleControl) {
        this.subscriptions.add(contactMechanismRoleControl.valueChanges.pipe(
          startWith(''),
          debounceTime(500),
          map((value: string | ContactMechanismRole) => {
            if (typeof (value) === 'string') {
              value = value.toLowerCase();
            } else {
              value = value.name.toLowerCase();
            }

            let filteredContactMechanismRoles: ContactMechanismRole[] = [];

            for (const contactMechanismRole of contactMechanismRoles.values()) {
              if (contactMechanismRole.name.toLowerCase().indexOf(value) === 0) {
                filteredContactMechanismRoles.push(contactMechanismRole);
              }
            }

            this.filteredContactMechanismRoles$.next(filteredContactMechanismRoles);
          })).subscribe());
      }
    });

    this.partyReferenceService.getContactMechanismTypes().pipe(first()).subscribe((contactMechanismTypes: Map<string, ContactMechanismType>) => {
      const contactMechanismTypeControl = this.partyReferenceForm.get('contactMechanismType');

      if (contactMechanismTypeControl) {
        this.subscriptions.add(contactMechanismTypeControl.valueChanges.pipe(
          startWith(''),
          debounceTime(500),
          map((value: string | ContactMechanismType) => {
            if (typeof (value) === 'string') {
              value = value.toLowerCase();
            } else {
              value = value.name.toLowerCase();
            }

            let filteredContactMechanismTypes: ContactMechanismType[] = [];

            for (const contactMechanismType of contactMechanismTypes.values()) {
              if (contactMechanismType.name.toLowerCase().indexOf(value) === 0) {
                filteredContactMechanismTypes.push(contactMechanismType);
              }
            }

            this.filteredContactMechanismTypes$.next(filteredContactMechanismTypes);
          })).subscribe());
      }
    });

    this.partyReferenceService.getEmploymentStatuses().pipe(first()).subscribe((employmentStatuses: Map<string, EmploymentStatus>) => {
      const employmentStatusControl = this.partyReferenceForm.get('employmentStatus');

      if (employmentStatusControl) {
        this.subscriptions.add(employmentStatusControl.valueChanges.pipe(
          startWith(''),
          debounceTime(500),
          map((value: string | EmploymentStatus) => {
            if (typeof (value) === 'string') {
              value = value.toLowerCase();
            } else {
              value = value.name.toLowerCase();
            }

            let filteredEmploymentStatuses: EmploymentStatus[] = [];

            for (const employmentStatus of employmentStatuses.values()) {
              if (employmentStatus.name.toLowerCase().indexOf(value) === 0) {
                filteredEmploymentStatuses.push(employmentStatus);
              }
            }

            this.filteredEmploymentStatuses$.next(filteredEmploymentStatuses);
          })).subscribe());
      }
    });

    this.partyReferenceService.getEmploymentTypes().pipe(first()).subscribe((employmentTypes: Map<string, EmploymentType>) => {
      const employmentTypeControl = this.partyReferenceForm.get('employmentType');

      if (employmentTypeControl) {
        this.subscriptions.add(employmentTypeControl.valueChanges.pipe(
          startWith(''),
          debounceTime(500),
          map((value: string | EmploymentType) => {
            if (typeof (value) === 'string') {
              value = value.toLowerCase();
            } else {
              value = value.name.toLowerCase();
            }

            let filteredEmploymentTypes: EmploymentType[] = [];

            for (const employmentType of employmentTypes.values()) {
              if (employmentType.name.toLowerCase().indexOf(value) === 0) {
                filteredEmploymentTypes.push(employmentType);
              }
            }

            this.filteredEmploymentTypes$.next(filteredEmploymentTypes);
          })).subscribe());
      }
    });

    this.partyReferenceService.getExternalReferenceTypes().pipe(first()).subscribe((externalReferenceTypes: Map<string, ExternalReferenceType>) => {
      const externalReferenceTypeControl = this.partyReferenceForm.get('externalReferenceType');

      if (externalReferenceTypeControl) {
        this.subscriptions.add(externalReferenceTypeControl.valueChanges.pipe(
          startWith(''),
          debounceTime(500),
          map((value: string | ExternalReferenceType) => {
            if (typeof (value) === 'string') {
              value = value.toLowerCase();
            } else {
              value = value.name.toLowerCase();
            }

            let filteredExternalReferenceTypes: ExternalReferenceType[] = [];

            for (const externalReferenceType of externalReferenceTypes.values()) {
              if (externalReferenceType.name.toLowerCase().indexOf(value) === 0) {
                filteredExternalReferenceTypes.push(externalReferenceType);
              }
            }

            this.filteredExternalReferenceTypes$.next(filteredExternalReferenceTypes);
          })).subscribe());
      }
    });

    this.partyReferenceService.getFieldsOfStudy().pipe(first()).subscribe((fieldsOfStudy: Map<string, FieldOfStudy>) => {
      const fieldOfStudyControl = this.partyReferenceForm.get('fieldOfStudy');

      if (fieldOfStudyControl) {
        this.subscriptions.add(fieldOfStudyControl.valueChanges.pipe(
          startWith(''),
          debounceTime(500),
          map((value: string | FieldOfStudy) => {
            if (typeof (value) === 'string') {
              value = value.toLowerCase();
            } else {
              value = value.name.toLowerCase();
            }

            let filteredFieldsOfStudy: FieldOfStudy[] = [];

            for (const fieldOfStudy of fieldsOfStudy.values()) {
              if (fieldOfStudy.name.toLowerCase().indexOf(value) === 0) {
                filteredFieldsOfStudy.push(fieldOfStudy);
              }
            }

            this.filteredFieldsOfStudy$.next(filteredFieldsOfStudy);
          })).subscribe());
      }
    });

    this.partyReferenceService.getGenders().pipe(first()).subscribe((genders: Map<string, Gender>) => {
      const genderControl = this.partyReferenceForm.get('gender');

      if (genderControl) {
        this.subscriptions.add(genderControl.valueChanges.pipe(
          startWith(''),
          debounceTime(500),
          map((value: string | Gender) => {
            if (typeof (value) === 'string') {
              value = value.toLowerCase();
            } else {
              value = value.name.toLowerCase();
            }

            let filteredGenders: Gender[] = [];

            for (const gender of genders.values()) {
              if (gender.name.toLowerCase().indexOf(value) === 0) {
                filteredGenders.push(gender);
              }
            }

            this.filteredGenders$.next(filteredGenders);
          })).subscribe());
      }
    });

    this.partyReferenceService.getIdentityDocumentTypes().pipe(first()).subscribe((identityDocumentTypes: Map<string, IdentityDocumentType>) => {
      const identityDocumentTypeControl = this.partyReferenceForm.get('identityDocumentType');

      if (identityDocumentTypeControl) {
        this.subscriptions.add(identityDocumentTypeControl.valueChanges.pipe(
          startWith(''),
          debounceTime(500),
          map((value: string | IdentityDocumentType) => {
            if (typeof (value) === 'string') {
              value = value.toLowerCase();
            } else {
              value = value.name.toLowerCase();
            }

            let filteredIdentityDocumentTypes: IdentityDocumentType[] = [];

            for (const identityDocumentType of identityDocumentTypes.values()) {
              if (identityDocumentType.name.toLowerCase().indexOf(value) === 0) {
                filteredIdentityDocumentTypes.push(identityDocumentType);
              }
            }

            this.filteredIdentityDocumentTypes$.next(filteredIdentityDocumentTypes);
          })).subscribe());
      }
    });

    this.partyReferenceService.getIndustryClassifications().pipe(first()).subscribe((industryClassifications: Map<string, IndustryClassification>) => {
      const industryClassificationControl = this.partyReferenceForm.get('industryClassification');

      if (industryClassificationControl) {
        this.subscriptions.add(industryClassificationControl.valueChanges.pipe(
          startWith(''),
          debounceTime(500),
          map((value: string | IndustryClassification) => {
            if (typeof (value) === 'string') {
              value = value.toLowerCase();
            } else {
              value = value.name.toLowerCase();
            }

            let filteredIndustryClassifications: IndustryClassification[] = [];

            for (const industryClassification of industryClassifications.values()) {
              if (industryClassification.name.toLowerCase().indexOf(value) === 0) {
                filteredIndustryClassifications.push(industryClassification);
              }
            }

            this.filteredIndustryClassifications$.next(filteredIndustryClassifications);
          })).subscribe());
      }
    });

    this.partyReferenceService.getIndustryClassificationCategories().pipe(first()).subscribe((industryClassificationCategories: Map<string, IndustryClassificationCategory>) => {
      const industryClassificationCategoryControl = this.partyReferenceForm.get('industryClassificationCategory');

      if (industryClassificationCategoryControl) {
        this.subscriptions.add(industryClassificationCategoryControl.valueChanges.pipe(
          startWith(''),
          debounceTime(500),
          map((value: string | IndustryClassificationCategory) => {
            if (typeof (value) === 'string') {
              value = value.toLowerCase();
            } else {
              value = value.name.toLowerCase();
            }

            let filteredIndustryClassificationCategories: IndustryClassificationCategory[] = [];

            for (const industryClassificationCategory of industryClassificationCategories.values()) {
              if (industryClassificationCategory.name.toLowerCase().indexOf(value) === 0) {
                filteredIndustryClassificationCategories.push(industryClassificationCategory);
              }
            }

            this.filteredIndustryClassificationCategories$.next(filteredIndustryClassificationCategories);
          })).subscribe());
      }
    });


    this.partyReferenceService.getIndustryClassificationSystems().pipe(first()).subscribe((industryClassificationSystems: Map<string, IndustryClassificationSystem>) => {
      const industryClassificationSystemControl = this.partyReferenceForm.get('industryClassificationSystem');

      if (industryClassificationSystemControl) {
        this.subscriptions.add(industryClassificationSystemControl.valueChanges.pipe(
          startWith(''),
          debounceTime(500),
          map((value: string | IndustryClassificationSystem) => {
            if (typeof (value) === 'string') {
              value = value.toLowerCase();
            } else {
              value = value.name.toLowerCase();
            }

            let filteredIndustryClassificationSystems: IndustryClassificationSystem[] = [];

            for (const industryClassificationSystem of industryClassificationSystems.values()) {
              if (industryClassificationSystem.name.toLowerCase().indexOf(value) === 0) {
                filteredIndustryClassificationSystems.push(industryClassificationSystem);
              }
            }

            this.filteredIndustryClassificationSystems$.next(filteredIndustryClassificationSystems);
          })).subscribe());
      }
    });

    this.partyReferenceService.getLockTypeCategories().pipe(first()).subscribe((lockTypeCategories: Map<string, LockTypeCategory>) => {
      const lockTypeCategoryControl = this.partyReferenceForm.get('lockTypeCategory');

      if (lockTypeCategoryControl) {
        this.subscriptions.add(lockTypeCategoryControl.valueChanges.pipe(
          startWith(''),
          debounceTime(500),
          map((value: string | LockTypeCategory) => {
            if (typeof (value) === 'string') {
              value = value.toLowerCase();
            } else {
              value = value.name.toLowerCase();
            }

            let filteredLockTypeCategories: LockTypeCategory[] = [];

            for (const lockTypeCategory of lockTypeCategories.values()) {
              if (lockTypeCategory.name.toLowerCase().indexOf(value) === 0) {
                filteredLockTypeCategories.push(lockTypeCategory);
              }
            }

            this.filteredLockTypeCategories$.next(filteredLockTypeCategories);
          })).subscribe());
      }
    });

    this.partyReferenceService.getLockTypes().pipe(first()).subscribe((lockTypes: Map<string, LockType>) => {
      const lockTypeControl = this.partyReferenceForm.get('lockType');

      if (lockTypeControl) {
        this.subscriptions.add(lockTypeControl.valueChanges.pipe(
          startWith(''),
          debounceTime(500),
          map((value: string | LockType) => {
            if (typeof (value) === 'string') {
              value = value.toLowerCase();
            } else {
              value = value.name.toLowerCase();
            }

            let filteredLockTypes: LockType[] = [];

            for (const lockType of lockTypes.values()) {
              if (lockType.name.toLowerCase().indexOf(value) === 0) {
                filteredLockTypes.push(lockType);
              }
            }

            this.filteredLockTypes$.next(filteredLockTypes);
          })).subscribe());
      }
    });

    this.partyReferenceService.getMaritalStatuses().pipe(first()).subscribe((maritalStatuses: Map<string, MaritalStatus>) => {
      const maritalStatusControl = this.partyReferenceForm.get('maritalStatus');

      if (maritalStatusControl) {
        this.subscriptions.add(maritalStatusControl.valueChanges.pipe(
          startWith(''),
          debounceTime(500),
          map((value: string | MaritalStatus) => {
            if (typeof (value) === 'string') {
              value = value.toLowerCase();
            } else {
              value = value.name.toLowerCase();
            }

            let filteredMaritalStatuses: MaritalStatus[] = [];

            for (const maritalStatus of maritalStatuses.values()) {
              if (maritalStatus.name.toLowerCase().indexOf(value) === 0) {
                filteredMaritalStatuses.push(maritalStatus);
              }
            }

            this.filteredMaritalStatuses$.next(filteredMaritalStatuses);
          })).subscribe());
      }
    });

    this.partyReferenceService.getMarriageTypes().pipe(first()).subscribe((marriageTypes: Map<string, MarriageType>) => {
      const marriageTypeControl = this.partyReferenceForm.get('marriageType');

      if (marriageTypeControl) {
        this.subscriptions.add(marriageTypeControl.valueChanges.pipe(
          startWith(''),
          debounceTime(500),
          map((value: string | MarriageType) => {
            if (typeof (value) === 'string') {
              value = value.toLowerCase();
            } else {
              value = value.name.toLowerCase();
            }

            let filteredMarriageTypes: MarriageType[] = [];

            for (const marriageType of marriageTypes.values()) {
              if (marriageType.name.toLowerCase().indexOf(value) === 0) {
                filteredMarriageTypes.push(marriageType);
              }
            }

            this.filteredMarriageTypes$.next(filteredMarriageTypes);
          })).subscribe());
      }
    });

    this.partyReferenceService.getNextOfKinTypes().pipe(first()).subscribe((nextOfKinTypes: Map<string, NextOfKinType>) => {
      const nextOfKinTypeControl = this.partyReferenceForm.get('nextOfKinType');

      if (nextOfKinTypeControl) {
        this.subscriptions.add(nextOfKinTypeControl.valueChanges.pipe(
          startWith(''),
          debounceTime(500),
          map((value: string | NextOfKinType) => {
            if (typeof (value) === 'string') {
              value = value.toLowerCase();
            } else {
              value = value.name.toLowerCase();
            }

            let filteredNextOfKinTypes: NextOfKinType[] = [];

            for (const nextOfKinType of nextOfKinTypes.values()) {
              if (nextOfKinType.name.toLowerCase().indexOf(value) === 0) {
                filteredNextOfKinTypes.push(nextOfKinType);
              }
            }

            this.filteredNextOfKinTypes$.next(filteredNextOfKinTypes);
          })).subscribe());
      }
    });

    this.partyReferenceService.getOccupations().pipe(first()).subscribe((occupations: Map<string, Occupation>) => {
      const occupationControl = this.partyReferenceForm.get('occupation');

      if (occupationControl) {
        this.subscriptions.add(occupationControl.valueChanges.pipe(
          startWith(''),
          debounceTime(500),
          map((value: string | Occupation) => {
            if (typeof (value) === 'string') {
              value = value.toLowerCase();
            } else {
              value = value.name.toLowerCase();
            }

            let filteredOccupations: Occupation[] = [];

            for (const occupation of occupations.values()) {
              if (occupation.name.toLowerCase().indexOf(value) === 0) {
                filteredOccupations.push(occupation);
              }
            }

            this.filteredOccupations$.next(filteredOccupations);
          })).subscribe());
      }
    });

    this.partyReferenceService.getPhysicalAddressPurposes().pipe(first()).subscribe((physicalAddressPurposes: Map<string, PhysicalAddressPurpose>) => {
      const physicalAddressPurposeControl = this.partyReferenceForm.get('physicalAddressPurpose');

      if (physicalAddressPurposeControl) {
        this.subscriptions.add(physicalAddressPurposeControl.valueChanges.pipe(
          startWith(''),
          debounceTime(500),
          map((value: string | PhysicalAddressPurpose) => {
            if (typeof (value) === 'string') {
              value = value.toLowerCase();
            } else {
              value = value.name.toLowerCase();
            }

            let filteredPhysicalAddressPurposes: PhysicalAddressPurpose[] = [];

            for (const physicalAddressPurpose of physicalAddressPurposes.values()) {
              if (physicalAddressPurpose.name.toLowerCase().indexOf(value) === 0) {
                filteredPhysicalAddressPurposes.push(physicalAddressPurpose);
              }
            }

            this.filteredPhysicalAddressPurposes$.next(filteredPhysicalAddressPurposes);
          })).subscribe());
      }
    });

    this.partyReferenceService.getPhysicalAddressRoles().pipe(first()).subscribe((physicalAddressRoles: Map<string, PhysicalAddressRole>) => {
      const physicalAddressRoleControl = this.partyReferenceForm.get('physicalAddressRole');

      if (physicalAddressRoleControl) {
        this.subscriptions.add(physicalAddressRoleControl.valueChanges.pipe(
          startWith(''),
          debounceTime(500),
          map((value: string | PhysicalAddressRole) => {
            if (typeof (value) === 'string') {
              value = value.toLowerCase();
            } else {
              value = value.name.toLowerCase();
            }

            let filteredPhysicalAddressRoles: PhysicalAddressPurpose[] = [];

            for (const physicalAddressRole of physicalAddressRoles.values()) {
              if (physicalAddressRole.name.toLowerCase().indexOf(value) === 0) {
                filteredPhysicalAddressRoles.push(physicalAddressRole);
              }
            }

            this.filteredPhysicalAddressRoles$.next(filteredPhysicalAddressRoles);
          })).subscribe());
      }
    });

    this.partyReferenceService.getPhysicalAddressTypes().pipe(first()).subscribe((physicalAddressTypes: Map<string, PhysicalAddressType>) => {
      const physicalAddressTypeControl = this.partyReferenceForm.get('physicalAddressType');

      if (physicalAddressTypeControl) {
        this.subscriptions.add(physicalAddressTypeControl.valueChanges.pipe(
          startWith(''),
          debounceTime(500),
          map((value: string | PhysicalAddressType) => {
            if (typeof (value) === 'string') {
              value = value.toLowerCase();
            } else {
              value = value.name.toLowerCase();
            }

            let filteredPhysicalAddressTypes: PhysicalAddressType[] = [];

            for (const physicalAddressType of physicalAddressTypes.values()) {
              if (physicalAddressType.name.toLowerCase().indexOf(value) === 0) {
                filteredPhysicalAddressTypes.push(physicalAddressType);
              }
            }

            this.filteredPhysicalAddressTypes$.next(filteredPhysicalAddressTypes);
          })).subscribe());
      }
    });

    this.partyReferenceService.getPreferenceTypeCategories().pipe(first()).subscribe((preferenceTypeCategories: Map<string, PreferenceTypeCategory>) => {
      const preferenceTypeCategoryControl = this.partyReferenceForm.get('preferenceTypeCategory');

      if (preferenceTypeCategoryControl) {
        this.subscriptions.add(preferenceTypeCategoryControl.valueChanges.pipe(
          startWith(''),
          debounceTime(500),
          map((value: string | PreferenceTypeCategory) => {
            if (typeof (value) === 'string') {
              value = value.toLowerCase();
            } else {
              value = value.name.toLowerCase();
            }

            let filteredPreferenceTypeCategories: PreferenceTypeCategory[] = [];

            for (const preferenceTypeCategory of preferenceTypeCategories.values()) {
              if (preferenceTypeCategory.name.toLowerCase().indexOf(value) === 0) {
                filteredPreferenceTypeCategories.push(preferenceTypeCategory);
              }
            }

            this.filteredPreferenceTypeCategories$.next(filteredPreferenceTypeCategories);
          })).subscribe());
      }
    });

    this.partyReferenceService.getPreferenceTypes().pipe(first()).subscribe((preferenceTypes: Map<string, PreferenceType>) => {
      const preferenceTypeControl = this.partyReferenceForm.get('preferenceType');

      if (preferenceTypeControl) {
        this.subscriptions.add(preferenceTypeControl.valueChanges.pipe(
          startWith(''),
          debounceTime(500),
          map((value: string | PreferenceType) => {
            if (typeof (value) === 'string') {
              value = value.toLowerCase();
            } else {
              value = value.name.toLowerCase();
            }

            let filteredPreferenceTypes: PreferenceType[] = [];

            for (const preferenceType of preferenceTypes.values()) {
              if (preferenceType.name.toLowerCase().indexOf(value) === 0) {
                filteredPreferenceTypes.push(preferenceType);
              }
            }

            this.filteredPreferenceTypes$.next(filteredPreferenceTypes);
          })).subscribe());
      }
    });

    this.partyReferenceService.getQualificationTypes().pipe(first()).subscribe((qualificationTypes: Map<string, QualificationType>) => {
      const qualificationTypeControl = this.partyReferenceForm.get('qualificationType');

      if (qualificationTypeControl) {
        this.subscriptions.add(qualificationTypeControl.valueChanges.pipe(
          startWith(''),
          debounceTime(500),
          map((value: string | QualificationType) => {
            if (typeof (value) === 'string') {
              value = value.toLowerCase();
            } else {
              value = value.name.toLowerCase();
            }

            let filteredQualificationTypes: QualificationType[] = [];

            for (const qualificationType of qualificationTypes.values()) {
              if (qualificationType.name.toLowerCase().indexOf(value) === 0) {
                filteredQualificationTypes.push(qualificationType);
              }
            }

            this.filteredQualificationTypes$.next(filteredQualificationTypes);
          })).subscribe());
      }
    });

    this.partyReferenceService.getRaces().pipe(first()).subscribe((races: Map<string, Race>) => {
      const raceControl = this.partyReferenceForm.get('race');

      if (raceControl) {
        this.subscriptions.add(raceControl.valueChanges.pipe(
          startWith(''),
          debounceTime(500),
          map((value: string | Race) => {
            if (typeof (value) === 'string') {
              value = value.toLowerCase();
            } else {
              value = value.name.toLowerCase();
            }

            let filteredRaces: Race[] = [];

            for (const race of races.values()) {
              if (race.name.toLowerCase().indexOf(value) === 0) {
                filteredRaces.push(race);
              }
            }

            this.filteredRaces$.next(filteredRaces);
          })).subscribe());
      }
    });

    this.partyReferenceService.getResidencePermitTypes().pipe(first()).subscribe((residencePermitTypes: Map<string, ResidencePermitType>) => {
      const residencePermitTypeControl = this.partyReferenceForm.get('residencePermitType');

      if (residencePermitTypeControl) {
        this.subscriptions.add(residencePermitTypeControl.valueChanges.pipe(
          startWith(''),
          debounceTime(500),
          map((value: string | ResidencePermitType) => {
            if (typeof (value) === 'string') {
              value = value.toLowerCase();
            } else {
              value = value.name.toLowerCase();
            }

            let filteredResidencePermitTypes: ResidencePermitType[] = [];

            for (const residencePermitType of residencePermitTypes.values()) {
              if (residencePermitType.name.toLowerCase().indexOf(value) === 0) {
                filteredResidencePermitTypes.push(residencePermitType);
              }
            }

            this.filteredResidencePermitTypes$.next(filteredResidencePermitTypes);
          })).subscribe());
      }
    });

    this.partyReferenceService.getResidencyStatuses().pipe(first()).subscribe((residencyStatuses: Map<string, ResidencyStatus>) => {
      const residencyStatusControl = this.partyReferenceForm.get('residencyStatus');

      if (residencyStatusControl) {
        this.subscriptions.add(residencyStatusControl.valueChanges.pipe(
          startWith(''),
          debounceTime(500),
          map((value: string | ResidencyStatus) => {
            if (typeof (value) === 'string') {
              value = value.toLowerCase();
            } else {
              value = value.name.toLowerCase();
            }

            let filteredResidencyStatuses: ResidencyStatus[] = [];

            for (const residencyStatus of residencyStatuses.values()) {
              if (residencyStatus.name.toLowerCase().indexOf(value) === 0) {
                filteredResidencyStatuses.push(residencyStatus);
              }
            }

            this.filteredResidencyStatuses$.next(filteredResidencyStatuses);
          })).subscribe());
      }
    });

    this.partyReferenceService.getResidentialTypes().pipe(first()).subscribe((residentialTypes: Map<string, ResidentialType>) => {
      const residentialTypeControl = this.partyReferenceForm.get('residentialType');

      if (residentialTypeControl) {
        this.subscriptions.add(residentialTypeControl.valueChanges.pipe(
          startWith(''),
          debounceTime(500),
          map((value: string | ResidentialType) => {
            if (typeof (value) === 'string') {
              value = value.toLowerCase();
            } else {
              value = value.name.toLowerCase();
            }

            let filteredResidentialTypes: ResidentialType[] = [];

            for (const residentialType of residentialTypes.values()) {
              if (residentialType.name.toLowerCase().indexOf(value) === 0) {
                filteredResidentialTypes.push(residentialType);
              }
            }

            this.filteredResidentialTypes$.next(filteredResidentialTypes);
          })).subscribe());
      }
    });

    this.partyReferenceService.getRolePurposes().pipe(first()).subscribe((rolePurposes: Map<string, RolePurpose>) => {
      const rolePurposeControl = this.partyReferenceForm.get('rolePurpose');

      if (rolePurposeControl) {
        this.subscriptions.add(rolePurposeControl.valueChanges.pipe(
          startWith(''),
          debounceTime(500),
          map((value: string | RolePurpose) => {
            if (typeof (value) === 'string') {
              value = value.toLowerCase();
            } else {
              value = value.name.toLowerCase();
            }

            let filteredRolePurposes: RolePurpose[] = [];

            for (const rolePurpose of rolePurposes.values()) {
              if (rolePurpose.name.toLowerCase().indexOf(value) === 0) {
                filteredRolePurposes.push(rolePurpose);
              }
            }

            this.filteredRolePurposes$.next(filteredRolePurposes);
          })).subscribe());
      }
    });

    this.partyReferenceService.getRoleTypeAttributeTypeConstraints('individual_customer').pipe(first()).subscribe((roleTypeAttributeTypeConstraints: RoleTypeAttributeTypeConstraint[]) => {
      const roleTypeAttributeTypeConstraintControl = this.partyReferenceForm.get('roleTypeAttributeTypeConstraint');

      if (roleTypeAttributeTypeConstraintControl) {
        this.subscriptions.add(roleTypeAttributeTypeConstraintControl.valueChanges.pipe(
          startWith(''),
          debounceTime(500),
          map((value: string | RoleTypeAttributeTypeConstraint) => {
            if (typeof (value) === 'string') {
              this.filteredRoleTypeAttributeTypeConstraints$.next(roleTypeAttributeTypeConstraints.filter(
                roleTypeAttributeTypeConstraint => (roleTypeAttributeTypeConstraint.roleType.toLowerCase().indexOf(value.toLowerCase()) === 0) || (roleTypeAttributeTypeConstraint.attributeType.toLowerCase().indexOf(value.toLowerCase())) === 0));
            } else {
              this.filteredRoleTypeAttributeTypeConstraints$.next(roleTypeAttributeTypeConstraints.filter(
                roleTypeAttributeTypeConstraint => (roleTypeAttributeTypeConstraint.roleType.toLowerCase().indexOf(value.roleType.toLowerCase()) === 0) || (roleTypeAttributeTypeConstraint.attributeType.toLowerCase().indexOf(value.attributeType.toLowerCase())) === 0));
            }
          })).subscribe());
      }
    });

    this.partyReferenceService.getRoleTypePreferenceTypeConstraints('individual_customer').pipe(first()).subscribe((roleTypePreferenceTypeConstraints: RoleTypePreferenceTypeConstraint[]) => {
      const roleTypePreferenceTypeConstraintControl = this.partyReferenceForm.get('roleTypePreferenceTypeConstraint');

      if (roleTypePreferenceTypeConstraintControl) {
        this.subscriptions.add(roleTypePreferenceTypeConstraintControl.valueChanges.pipe(
          startWith(''),
          debounceTime(500),
          map((value: string | RoleTypePreferenceTypeConstraint) => {
            if (typeof (value) === 'string') {
              this.filteredRoleTypePreferenceTypeConstraints$.next(roleTypePreferenceTypeConstraints.filter(
                roleTypePreferenceTypeConstraint => (roleTypePreferenceTypeConstraint.roleType.toLowerCase().indexOf(value.toLowerCase()) === 0) || roleTypePreferenceTypeConstraint.preferenceType.toLowerCase().indexOf(value.toLowerCase()) === 0));
            } else {
              this.filteredRoleTypePreferenceTypeConstraints$.next(roleTypePreferenceTypeConstraints.filter(
                roleTypePreferenceTypeConstraint => (roleTypePreferenceTypeConstraint.roleType.toLowerCase().indexOf(value.roleType.toLowerCase()) === 0) || roleTypePreferenceTypeConstraint.preferenceType.toLowerCase().indexOf(value.preferenceType.toLowerCase()) === 0));
            }
          })).subscribe());
      }
    });

    this.partyReferenceService.getRoleTypes().pipe(first()).subscribe((roleTypes: Map<string, RoleType>) => {
      const roleTypeControl = this.partyReferenceForm.get('roleType');

      if (roleTypeControl) {
        this.subscriptions.add(roleTypeControl.valueChanges.pipe(
          startWith(''),
          debounceTime(500),
          map((value: string | RoleType) => {
            if (typeof (value) === 'string') {
              value = value.toLowerCase();
            } else {
              value = value.name.toLowerCase();
            }

            let filteredRoleTypes: RoleType[] = [];

            for (const roleType of roleTypes.values()) {
              if (roleType.name.toLowerCase().indexOf(value) === 0) {
                filteredRoleTypes.push(roleType);
              }
            }

            this.filteredRoleTypes$.next(filteredRoleTypes);
          })).subscribe());
      }
    });

    this.partyReferenceService.getSegmentationTypes().pipe(first()).subscribe((segmentationTypes: Map<string, SegmentationType>) => {
      const segmentationTypeControl = this.partyReferenceForm.get('segmentationType');

      if (segmentationTypeControl) {
        this.subscriptions.add(segmentationTypeControl.valueChanges.pipe(
          startWith(''),
          debounceTime(500),
          map((value: string | SegmentationType) => {
            if (typeof (value) === 'string') {
              value = value.toLowerCase();
            } else {
              value = value.name.toLowerCase();
            }

            let filteredSegmentationTypes: SegmentationType[] = [];

            for (const segmentationType of segmentationTypes.values()) {
              if (segmentationType.name.toLowerCase().indexOf(value) === 0) {
                filteredSegmentationTypes.push(segmentationType);
              }
            }

            this.filteredSegmentationTypes$.next(filteredSegmentationTypes);
          })).subscribe());
      }
    });

    this.partyReferenceService.getSegments().pipe(first()).subscribe((segments: Map<string, Segment>) => {
      const segmentControl = this.partyReferenceForm.get('segment');

      if (segmentControl) {
        this.subscriptions.add(segmentControl.valueChanges.pipe(
          startWith(''),
          debounceTime(500),
          map((value: string | Segment) => {
            if (typeof (value) === 'string') {
              value = value.toLowerCase();
            } else {
              value = value.name.toLowerCase();
            }

            let filteredSegments: Segment[] = [];

            for (const segment of segments.values()) {
              if (segment.name.toLowerCase().indexOf(value) === 0) {
                filteredSegments.push(segment);
              }
            }

            this.filteredSegments$.next(filteredSegments);
          })).subscribe());
      }
    });

    this.partyReferenceService.getSkillTypes().pipe(first()).subscribe((skillTypes: Map<string, SkillType>) => {
      const skillTypeControl = this.partyReferenceForm.get('skillType');

      if (skillTypeControl) {
        this.subscriptions.add(skillTypeControl.valueChanges.pipe(
          startWith(''),
          debounceTime(500),
          map((value: string | SkillType) => {
            if (typeof (value) === 'string') {
              value = value.toLowerCase();
            } else {
              value = value.name.toLowerCase();
            }

            let filteredSkillTypes: SkillType[] = [];

            for (const skillType of skillTypes.values()) {
              if (skillType.name.toLowerCase().indexOf(value) === 0) {
                filteredSkillTypes.push(skillType);
              }
            }

            this.filteredSkillTypes$.next(filteredSkillTypes);
          })).subscribe());
      }
    });

    this.partyReferenceService.getSourceOfFundsTypes().pipe(first()).subscribe((sourceOfFundsTypes: Map<string, SourceOfFundsType>) => {
      const sourceOfFundsTypeControl = this.partyReferenceForm.get('sourceOfFundsType');

      if (sourceOfFundsTypeControl) {
        this.subscriptions.add(sourceOfFundsTypeControl.valueChanges.pipe(
          startWith(''),
          debounceTime(500),
          map((value: string | SourceOfFundsType) => {
            if (typeof (value) === 'string') {
              value = value.toLowerCase();
            } else {
              value = value.name.toLowerCase();
            }

            let filteredSourceOfFundsTypes: SourceOfFundsType[] = [];

            for (const sourceOfFundsType of sourceOfFundsTypes.values()) {
              if (sourceOfFundsType.name.toLowerCase().indexOf(value) === 0) {
                filteredSourceOfFundsTypes.push(sourceOfFundsType);
              }
            }

            this.filteredSourceOfFundsTypes$.next(filteredSourceOfFundsTypes);
          })).subscribe());
      }
    });

    this.partyReferenceService.getSourceOfWealthTypes().pipe(first()).subscribe((sourceOfWealthTypes: Map<string, SourceOfWealthType>) => {
      const sourceOfWealthTypeControl = this.partyReferenceForm.get('sourceOfWealthType');

      if (sourceOfWealthTypeControl) {
        this.subscriptions.add(sourceOfWealthTypeControl.valueChanges.pipe(
          startWith(''),
          debounceTime(500),
          map((value: string | SourceOfWealthType) => {
            if (typeof (value) === 'string') {
              value = value.toLowerCase();
            } else {
              value = value.name.toLowerCase();
            }

            let filteredSourceOfWealthTypes: SourceOfWealthType[] = [];

            for (const sourceOfWealthType of sourceOfWealthTypes.values()) {
              if (sourceOfWealthType.name.toLowerCase().indexOf(value) === 0) {
                filteredSourceOfWealthTypes.push(sourceOfWealthType);
              }
            }

            this.filteredSourceOfWealthTypes$.next(filteredSourceOfWealthTypes);
          })).subscribe());
      }
    });

    this.partyReferenceService.getStatusTypeCategories().pipe(first()).subscribe((statusTypeCategories: Map<string, StatusTypeCategory>) => {
      const statusTypeCategoryControl = this.partyReferenceForm.get('statusTypeCategory');

      if (statusTypeCategoryControl) {
        this.subscriptions.add(statusTypeCategoryControl.valueChanges.pipe(
          startWith(''),
          debounceTime(500),
          map((value: string | StatusTypeCategory) => {
            if (typeof (value) === 'string') {
              value = value.toLowerCase();
            } else {
              value = value.name.toLowerCase();
            }

            let filteredStatusTypeCategories: StatusTypeCategory[] = [];

            for (const statusTypeCategory of statusTypeCategories.values()) {
              if (statusTypeCategory.name.toLowerCase().indexOf(value) === 0) {
                filteredStatusTypeCategories.push(statusTypeCategory);
              }
            }

            this.filteredStatusTypeCategories$.next(filteredStatusTypeCategories);
          })).subscribe());
      }
    });

    this.partyReferenceService.getStatusTypes().pipe(first()).subscribe((statusTypes: Map<string, StatusType>) => {
      const statusTypeControl = this.partyReferenceForm.get('statusType');

      if (statusTypeControl) {
        this.subscriptions.add(statusTypeControl.valueChanges.pipe(
          startWith(''),
          debounceTime(500),
          map((value: string | StatusType) => {
            if (typeof (value) === 'string') {
              value = value.toLowerCase();
            } else {
              value = value.name.toLowerCase();
            }

            let filteredStatusTypes: StatusType[] = [];

            for (const statusType of statusTypes.values()) {
              if (statusType.name.toLowerCase().indexOf(value) === 0) {
                filteredStatusTypes.push(statusType);
              }
            }

            this.filteredStatusTypes$.next(filteredStatusTypes);
          })).subscribe());
      }
    });

    this.partyReferenceService.getTaxNumberTypes().pipe(first()).subscribe((taxNumberTypes: Map<string, TaxNumberType>) => {
      const taxNumberTypeControl = this.partyReferenceForm.get('taxNumberType');

      if (taxNumberTypeControl) {
        this.subscriptions.add(taxNumberTypeControl.valueChanges.pipe(
          startWith(''),
          debounceTime(500),
          map((value: string | TaxNumberType) => {
            if (typeof (value) === 'string') {
              value = value.toLowerCase();
            } else {
              value = value.name.toLowerCase();
            }

            let filteredTaxNumberTypes: TaxNumberType[] = [];

            for (const taxNumberType of taxNumberTypes.values()) {
              if (taxNumberType.name.toLowerCase().indexOf(value) === 0) {
                filteredTaxNumberTypes.push(taxNumberType);
              }
            }

            this.filteredTaxNumberTypes$.next(filteredTaxNumberTypes);
          })).subscribe());
      }
    });

    this.partyReferenceService.getTimesToContact().pipe(first()).subscribe((timesToContact: Map<string, TimeToContact>) => {
      const timeToContactControl = this.partyReferenceForm.get('timeToContact');

      if (timeToContactControl) {
        this.subscriptions.add(timeToContactControl.valueChanges.pipe(
          startWith(''),
          debounceTime(500),
          map((value: string | TimeToContact) => {
            if (typeof (value) === 'string') {
              value = value.toLowerCase();
            } else {
              value = value.name.toLowerCase();
            }

            let filteredTimesToContact: TimeToContact[] = [];

            for (const timeToContact of timesToContact.values()) {
              if (timeToContact.name.toLowerCase().indexOf(value) === 0) {
                filteredTimesToContact.push(timeToContact);
              }
            }

            this.filteredTimesToContact$.next(filteredTimesToContact);
          })).subscribe());
      }
    });
  }

  ok(): void {
    console.log('Association Property Type = ', this.partyReferenceForm.get('associationPropertyType')!.value);
    console.log('Association Type = ', this.partyReferenceForm.get('associationType')!.value);
    console.log('Attribute Type Category = ', this.partyReferenceForm.get('attributeTypeCategory')!.value);
    console.log('Attribute Type = ', this.partyReferenceForm.get('attributeType')!.value);
    console.log('Consent Type = ', this.partyReferenceForm.get('consentType')!.value);
    console.log('Contact Mechanism Purpose = ', this.partyReferenceForm.get('contactMechanismPurpose')!.value);
    console.log('Contact Mechanism Sub Type = ', this.partyReferenceForm.get('contactMechanismRole')!.value);
    console.log('Contact Mechanism Type = ', this.partyReferenceForm.get('contactMechanismType')!.value);
    console.log('Employment Status = ', this.partyReferenceForm.get('employmentStatus')!.value);
    console.log('Employment Type = ', this.partyReferenceForm.get('employmentType')!.value);
    console.log('Field Of Study = ', this.partyReferenceForm.get('fieldOfStudy')!.value);
    console.log('Gender = ', this.partyReferenceForm.get('gender')!.value);
    console.log('Identity Document Type = ', this.partyReferenceForm.get('identityDocumentType')!.value);
    console.log('Industry Classification = ', this.partyReferenceForm.get('industryClassification')!.value);
    console.log('Industry Classification Category = ', this.partyReferenceForm.get('industryClassificationCategory')!.value);
    console.log('Industry Classification System = ', this.partyReferenceForm.get('industryClassificationSystem')!.value);
    console.log('Lock Type Category = ', this.partyReferenceForm.get('lockTypeCategory')!.value);
    console.log('Lock Type = ', this.partyReferenceForm.get('lockType')!.value);
    console.log('Marital Status = ', this.partyReferenceForm.get('maritalStatus')!.value);
    console.log('Marriage Type = ', this.partyReferenceForm.get('marriageType')!.value);
    console.log('Next Of Kin Type = ', this.partyReferenceForm.get('nextOfKinType')!.value);
    console.log('Occupation = ', this.partyReferenceForm.get('occupation')!.value);
    console.log('Physical Address Purpose = ', this.partyReferenceForm.get('physicalAddressPurpose')!.value);
    console.log('Physical Address Sub Type = ', this.partyReferenceForm.get('physicalAddressRole')!.value);
    console.log('Physical Address Type = ', this.partyReferenceForm.get('physicalAddressType')!.value);
    console.log('Preference Type Category = ', this.partyReferenceForm.get('preferenceTypeCategory')!.value);
    console.log('Preference Type = ', this.partyReferenceForm.get('preferenceType')!.value);
    console.log('Qualification Type = ', this.partyReferenceForm.get('qualificationType')!.value);
    console.log('Race = ', this.partyReferenceForm.get('race')!.value);
    console.log('Residence Permit Type = ', this.partyReferenceForm.get('residencePermitType')!.value);
    console.log('Residency Status = ', this.partyReferenceForm.get('residencyStatus')!.value);
    console.log('Residential Type = ', this.partyReferenceForm.get('residentialType')!.value);
    console.log('Role Purpose = ', this.partyReferenceForm.get('rolePurpose')!.value);
    console.log('Role Type Attribute Type Constraint = ', this.partyReferenceForm.get('roleTypeAttributeTypeConstraint')!.value);
    console.log('Role Type Preference Type Constraint = ', this.partyReferenceForm.get('roleTypePreferenceTypeConstraint')!.value);
    console.log('Role Type = ', this.partyReferenceForm.get('roleType')!.value);
    console.log('Segmentation Type = ', this.partyReferenceForm.get('segmentationType')!.value);
    console.log('Segment = ', this.partyReferenceForm.get('segment')!.value);
    console.log('Skill Type = ', this.partyReferenceForm.get('skillType')!.value);
    console.log('Source Of Funds = ', this.partyReferenceForm.get('sourceOfFundsType')!.value);
    console.log('Source Of Wealth = ', this.partyReferenceForm.get('sourceOfWealthType')!.value);
    console.log('Status Type Category = ', this.partyReferenceForm.get('statusTypeCategory')!.value);
    console.log('Status Type = ', this.partyReferenceForm.get('statusType')!.value);
    console.log('Tax Number Type = ', this.partyReferenceForm.get('taxNumberType')!.value);
    console.log('Time To Contact = ', this.partyReferenceForm.get('timeToContact')!.value);
  }
}
