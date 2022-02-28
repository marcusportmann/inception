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
 * The PartyReferenceFormComponent class implements the party reference form component.
 *
 * @author Marcus Portmann
 */
@Component({
  templateUrl: 'party-reference-form.component.html'
})
export class PartyReferenceFormComponent implements OnInit, OnDestroy {

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

  filteredTitles$: Subject<Title[]> = new ReplaySubject<Title[]>();

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

  displayTitle(title: Title): string {
    if (!!title) {
      return title.name;
    } else {
      return '';
    }
  }

  ngOnDestroy(): void {
    this.subscriptions.unsubscribe();
  }

  ngOnInit(): void {
    this.partyReferenceService.getAssociationPropertyTypes().pipe(first()).subscribe((associationPropertyTypes: AssociationPropertyType[]) => {
      const associationPropertyTypeControl = this.partyReferenceForm.get('associationPropertyType');

      if (associationPropertyTypeControl) {
        this.subscriptions.add(associationPropertyTypeControl.valueChanges.pipe(
          startWith(''),
          debounceTime(500),
          map((value: string | AssociationPropertyType) => {
            if (typeof (value) === 'string') {
              this.filteredAssociationPropertyTypes$.next(associationPropertyTypes.filter(
                associationPropertyType => associationPropertyType.name.toLowerCase().indexOf(value.toLowerCase()) === 0));
            } else {
              this.filteredAssociationPropertyTypes$.next(associationPropertyTypes.filter(
                associationPropertyType => associationPropertyType.name.toLowerCase().indexOf(value.name.toLowerCase()) === 0));
            }
          })).subscribe());
      }
    });

    this.partyReferenceService.getAssociationTypes().pipe(first()).subscribe((associationTypes: AssociationType[]) => {
      const associationTypeControl = this.partyReferenceForm.get('associationType');

      if (associationTypeControl) {
        this.subscriptions.add(associationTypeControl.valueChanges.pipe(
          startWith(''),
          debounceTime(500),
          map((value: string | AssociationType) => {
            if (typeof (value) === 'string') {
              this.filteredAssociationTypes$.next(associationTypes.filter(
                associationType => associationType.name.toLowerCase().indexOf(value.toLowerCase()) === 0));
            } else {
              this.filteredAssociationTypes$.next(associationTypes.filter(
                associationType => associationType.name.toLowerCase().indexOf(value.name.toLowerCase()) === 0));
            }
          })).subscribe());
      }
    });

    this.partyReferenceService.getAttributeTypeCategories().pipe(first()).subscribe((attributeTypeCategories: AttributeTypeCategory[]) => {
      const attributeTypeCategoryControl = this.partyReferenceForm.get('attributeTypeCategory');

      if (attributeTypeCategoryControl) {
        this.subscriptions.add(attributeTypeCategoryControl.valueChanges.pipe(
          startWith(''),
          debounceTime(500),
          map((value: string | AttributeTypeCategory) => {
            if (typeof (value) === 'string') {
              this.filteredAttributeTypeCategories$.next(attributeTypeCategories.filter(
                attributeTypeCategory => attributeTypeCategory.name.toLowerCase().indexOf(value.toLowerCase()) === 0));
            } else {
              this.filteredAttributeTypeCategories$.next(attributeTypeCategories.filter(
                attributeTypeCategory => attributeTypeCategory.name.toLowerCase().indexOf(value.name.toLowerCase()) === 0));
            }
          })).subscribe());
      }
    });

    this.partyReferenceService.getAttributeTypes().pipe(first()).subscribe((attributeTypes: AttributeType[]) => {
      const attributeTypeControl = this.partyReferenceForm.get('attributeType');

      if (attributeTypeControl) {
        this.subscriptions.add(attributeTypeControl.valueChanges.pipe(
          startWith(''),
          debounceTime(500),
          map((value: string | AttributeType) => {
            if (typeof (value) === 'string') {
              this.filteredAttributeTypes$.next(attributeTypes.filter(
                attributeType => attributeType.name.toLowerCase().indexOf(value.toLowerCase()) === 0));
            } else {
              this.filteredAttributeTypes$.next(attributeTypes.filter(
                attributeType => attributeType.name.toLowerCase().indexOf(value.name.toLowerCase()) === 0));
            }
          })).subscribe());
      }
    });

    this.partyReferenceService.getConsentTypes().pipe(first()).subscribe((consentTypes: ConsentType[]) => {
      const consentTypeControl = this.partyReferenceForm.get('consentType');

      if (consentTypeControl) {
        this.subscriptions.add(consentTypeControl.valueChanges.pipe(
          startWith(''),
          debounceTime(500),
          map((value: string | ConsentType) => {
            if (typeof (value) === 'string') {
              this.filteredConsentTypes$.next(consentTypes.filter(
                attributeType => attributeType.name.toLowerCase().indexOf(value.toLowerCase()) === 0));
            } else {
              this.filteredConsentTypes$.next(consentTypes.filter(
                attributeType => attributeType.name.toLowerCase().indexOf(value.name.toLowerCase()) === 0));
            }
          })).subscribe());
      }
    });

    this.partyReferenceService.getContactMechanismPurposes().pipe(first()).subscribe((contactMechanismPurposes: ContactMechanismPurpose[]) => {
      const contactMechanismPurposeControl = this.partyReferenceForm.get('contactMechanismPurpose');

      if (contactMechanismPurposeControl) {
        this.subscriptions.add(contactMechanismPurposeControl.valueChanges.pipe(
          startWith(''),
          debounceTime(500),
          map((value: string | ContactMechanismPurpose) => {
            if (typeof (value) === 'string') {
              this.filteredContactMechanismPurposes$.next(contactMechanismPurposes.filter(
                contactMechanismPurpose => contactMechanismPurpose.name.toLowerCase().indexOf(value.toLowerCase()) === 0));
            } else {
              this.filteredContactMechanismPurposes$.next(contactMechanismPurposes.filter(
                contactMechanismPurpose => contactMechanismPurpose.name.toLowerCase().indexOf(value.name.toLowerCase()) === 0));
            }
          })).subscribe());
      }
    });

    this.partyReferenceService.getContactMechanismRoles().pipe(first()).subscribe((contactMechanismRoles: ContactMechanismRole[]) => {
      const contactMechanismRoleControl = this.partyReferenceForm.get('contactMechanismRole');

      if (contactMechanismRoleControl) {
        this.subscriptions.add(contactMechanismRoleControl.valueChanges.pipe(
          startWith(''),
          debounceTime(500),
          map((value: string | ContactMechanismRole) => {
            if (typeof (value) === 'string') {
              this.filteredContactMechanismRoles$.next(contactMechanismRoles.filter(
                contactMechanismRole => contactMechanismRole.name.toLowerCase().indexOf(value.toLowerCase()) === 0));
            } else {
              this.filteredContactMechanismRoles$.next(contactMechanismRoles.filter(
                contactMechanismRole => contactMechanismRole.name.toLowerCase().indexOf(value.name.toLowerCase()) === 0));
            }
          })).subscribe());
      }
    });

    this.partyReferenceService.getContactMechanismTypes().pipe(first()).subscribe((contactMechanismTypes: ContactMechanismType[]) => {
      const contactMechanismTypeControl = this.partyReferenceForm.get('contactMechanismType');

      if (contactMechanismTypeControl) {
        this.subscriptions.add(contactMechanismTypeControl.valueChanges.pipe(
          startWith(''),
          debounceTime(500),
          map((value: string | ContactMechanismType) => {
            if (typeof (value) === 'string') {
              this.filteredContactMechanismTypes$.next(contactMechanismTypes.filter(
                contactMechanismType => contactMechanismType.name.toLowerCase().indexOf(value.toLowerCase()) === 0));
            } else {
              this.filteredContactMechanismTypes$.next(contactMechanismTypes.filter(
                contactMechanismType => contactMechanismType.name.toLowerCase().indexOf(value.name.toLowerCase()) === 0));
            }
          })).subscribe());
      }
    });

    this.partyReferenceService.getEmploymentStatuses().pipe(first()).subscribe((employmentStatuses: EmploymentStatus[]) => {
      const employmentStatusControl = this.partyReferenceForm.get('employmentStatus');

      if (employmentStatusControl) {
        this.subscriptions.add(employmentStatusControl.valueChanges.pipe(
          startWith(''),
          debounceTime(500),
          map((value: string | EmploymentStatus) => {
            if (typeof (value) === 'string') {
              this.filteredEmploymentStatuses$.next(employmentStatuses.filter(
                employmentStatus => employmentStatus.name.toLowerCase().indexOf(value.toLowerCase()) === 0));
            } else {
              this.filteredEmploymentStatuses$.next(employmentStatuses.filter(
                employmentStatus => employmentStatus.name.toLowerCase().indexOf(value.name.toLowerCase()) === 0));
            }
          })).subscribe());
      }
    });

    this.partyReferenceService.getEmploymentTypes().pipe(first()).subscribe((employmentTypes: EmploymentType[]) => {
      const employmentTypeControl = this.partyReferenceForm.get('employmentType');

      if (employmentTypeControl) {
        this.subscriptions.add(employmentTypeControl.valueChanges.pipe(
          startWith(''),
          debounceTime(500),
          map((value: string | EmploymentType) => {
            if (typeof (value) === 'string') {
              this.filteredEmploymentTypes$.next(employmentTypes.filter(
                employmentType => employmentType.name.toLowerCase().indexOf(value.toLowerCase()) === 0));
            } else {
              this.filteredEmploymentTypes$.next(employmentTypes.filter(
                employmentType => employmentType.name.toLowerCase().indexOf(value.name.toLowerCase()) === 0));
            }
          })).subscribe());
      }
    });

    this.partyReferenceService.getExternalReferenceTypes().pipe(first()).subscribe((externalReferenceTypes: ExternalReferenceType[]) => {
      const externalReferenceTypeControl = this.partyReferenceForm.get('externalReferenceType');

      if (externalReferenceTypeControl) {
        this.subscriptions.add(externalReferenceTypeControl.valueChanges.pipe(
          startWith(''),
          debounceTime(500),
          map((value: string | ExternalReferenceType) => {
            if (typeof (value) === 'string') {
              this.filteredExternalReferenceTypes$.next(externalReferenceTypes.filter(
                attributeType => attributeType.name.toLowerCase().indexOf(value.toLowerCase()) === 0));
            } else {
              this.filteredExternalReferenceTypes$.next(externalReferenceTypes.filter(
                attributeType => attributeType.name.toLowerCase().indexOf(value.name.toLowerCase()) === 0));
            }
          })).subscribe());
      }
    });

    this.partyReferenceService.getFieldsOfStudy().pipe(first()).subscribe((fieldsOfStudy: FieldOfStudy[]) => {
      const fieldOfStudyControl = this.partyReferenceForm.get('fieldOfStudy');

      if (fieldOfStudyControl) {
        this.subscriptions.add(fieldOfStudyControl.valueChanges.pipe(
          startWith(''),
          debounceTime(500),
          map((value: string | FieldOfStudy) => {
            if (typeof (value) === 'string') {
              this.filteredFieldsOfStudy$.next(fieldsOfStudy.filter(
                fieldOfStudy => fieldOfStudy.name.toLowerCase().indexOf(value.toLowerCase()) === 0));
            } else {
              this.filteredFieldsOfStudy$.next(fieldsOfStudy.filter(
                fieldOfStudy => fieldOfStudy.name.toLowerCase().indexOf(value.name.toLowerCase()) === 0));
            }
          })).subscribe());
      }
    });

    this.partyReferenceService.getGenders().pipe(first()).subscribe((genders: Gender[]) => {
      const genderControl = this.partyReferenceForm.get('gender');

      if (genderControl) {
        this.subscriptions.add(genderControl.valueChanges.pipe(
          startWith(''),
          debounceTime(500),
          map((value: string | Gender) => {
            if (typeof (value) === 'string') {
              this.filteredGenders$.next(genders.filter(
                gender => gender.name.toLowerCase().indexOf(value.toLowerCase()) === 0));
            } else {
              this.filteredGenders$.next(genders.filter(
                gender => gender.name.toLowerCase().indexOf(value.name.toLowerCase()) === 0));
            }
          })).subscribe());
      }
    });

    this.partyReferenceService.getIdentityDocumentTypes().pipe(first()).subscribe((identityDocumentTypes: IdentityDocumentType[]) => {
      const identityDocumentTypeControl = this.partyReferenceForm.get('identityDocumentType');

      if (identityDocumentTypeControl) {
        this.subscriptions.add(identityDocumentTypeControl.valueChanges.pipe(
          startWith(''),
          debounceTime(500),
          map((value: string | IdentityDocumentType) => {
            if (typeof (value) === 'string') {
              this.filteredIdentityDocumentTypes$.next(identityDocumentTypes.filter(
                identityDocumentType => identityDocumentType.name.toLowerCase().indexOf(value.toLowerCase()) === 0));
            } else {
              this.filteredIdentityDocumentTypes$.next(identityDocumentTypes.filter(
                identityDocumentType => identityDocumentType.name.toLowerCase().indexOf(value.name.toLowerCase()) === 0));
            }
          })).subscribe());
      }
    });

    this.partyReferenceService.getIndustryClassifications().pipe(first()).subscribe((industryClassifications: IndustryClassification[]) => {
      const industryClassificationControl = this.partyReferenceForm.get('industryClassification');

      if (industryClassificationControl) {
        this.subscriptions.add(industryClassificationControl.valueChanges.pipe(
          startWith(''),
          debounceTime(500),
          map((value: string | IndustryClassification) => {
            if (typeof (value) === 'string') {
              this.filteredIndustryClassifications$.next(industryClassifications.filter(
                industryClassification => industryClassification.name.toLowerCase().indexOf(value.toLowerCase()) === 0));
            } else {
              this.filteredIndustryClassifications$.next(industryClassifications.filter(
                industryClassification => industryClassification.name.toLowerCase().indexOf(value.name.toLowerCase()) === 0));
            }
          })).subscribe());
      }
    });

    this.partyReferenceService.getIndustryClassificationCategories().pipe(first()).subscribe((industryClassificationCategories: IndustryClassificationCategory[]) => {
      const industryClassificationCategoryControl = this.partyReferenceForm.get('industryClassificationCategory');

      if (industryClassificationCategoryControl) {
        this.subscriptions.add(industryClassificationCategoryControl.valueChanges.pipe(
          startWith(''),
          debounceTime(500),
          map((value: string | IndustryClassificationCategory) => {
            if (typeof (value) === 'string') {
              this.filteredIndustryClassificationCategories$.next(industryClassificationCategories.filter(
                industryClassificationCategory => industryClassificationCategory.name.toLowerCase().indexOf(value.toLowerCase()) === 0));
            } else {
              this.filteredIndustryClassificationCategories$.next(industryClassificationCategories.filter(
                industryClassificationCategory => industryClassificationCategory.name.toLowerCase().indexOf(value.name.toLowerCase()) === 0));
            }
          })).subscribe());
      }
    });



    this.partyReferenceService.getIndustryClassificationSystems().pipe(first()).subscribe((industryClassificationSystems: IndustryClassificationSystem[]) => {
      const industryClassificationSystemControl = this.partyReferenceForm.get('industryClassificationSystem');

      if (industryClassificationSystemControl) {
        this.subscriptions.add(industryClassificationSystemControl.valueChanges.pipe(
          startWith(''),
          debounceTime(500),
          map((value: string | IndustryClassificationSystem) => {
            if (typeof (value) === 'string') {
              this.filteredIndustryClassificationSystems$.next(industryClassificationSystems.filter(
                industryClassificationSystem => industryClassificationSystem.name.toLowerCase().indexOf(value.toLowerCase()) === 0));
            } else {
              this.filteredIndustryClassificationSystems$.next(industryClassificationSystems.filter(
                industryClassificationSystem => industryClassificationSystem.name.toLowerCase().indexOf(value.name.toLowerCase()) === 0));
            }
          })).subscribe());
      }
    });

    this.partyReferenceService.getLockTypeCategories().pipe(first()).subscribe((lockTypeCategories: LockTypeCategory[]) => {
      const lockTypeCategoryControl = this.partyReferenceForm.get('lockTypeCategory');

      if (lockTypeCategoryControl) {
        this.subscriptions.add(lockTypeCategoryControl.valueChanges.pipe(
          startWith(''),
          debounceTime(500),
          map((value: string | LockTypeCategory) => {
            if (typeof (value) === 'string') {
              this.filteredLockTypeCategories$.next(lockTypeCategories.filter(
                lockTypeCategory => lockTypeCategory.name.toLowerCase().indexOf(value.toLowerCase()) === 0));
            } else {
              this.filteredLockTypeCategories$.next(lockTypeCategories.filter(
                lockTypeCategory => lockTypeCategory.name.toLowerCase().indexOf(value.name.toLowerCase()) === 0));
            }
          })).subscribe());
      }
    });

    this.partyReferenceService.getLockTypes().pipe(first()).subscribe((lockTypes: LockType[]) => {
      const lockTypeControl = this.partyReferenceForm.get('lockType');

      if (lockTypeControl) {
        this.subscriptions.add(lockTypeControl.valueChanges.pipe(
          startWith(''),
          debounceTime(500),
          map((value: string | LockType) => {
            if (typeof (value) === 'string') {
              this.filteredLockTypes$.next(lockTypes.filter(
                lockType => lockType.name.toLowerCase().indexOf(value.toLowerCase()) === 0));
            } else {
              this.filteredLockTypes$.next(lockTypes.filter(
                lockType => lockType.name.toLowerCase().indexOf(value.name.toLowerCase()) === 0));
            }
          })).subscribe());
      }
    });

    this.partyReferenceService.getMaritalStatuses().pipe(first()).subscribe((maritalStatuses: MaritalStatus[]) => {
      const maritalStatusControl = this.partyReferenceForm.get('maritalStatus');

      if (maritalStatusControl) {
        this.subscriptions.add(maritalStatusControl.valueChanges.pipe(
          startWith(''),
          debounceTime(500),
          map((value: string | MaritalStatus) => {
            if (typeof (value) === 'string') {
              this.filteredMaritalStatuses$.next(maritalStatuses.filter(
                maritalStatus => maritalStatus.name.toLowerCase().indexOf(value.toLowerCase()) === 0));
            } else {
              this.filteredMaritalStatuses$.next(maritalStatuses.filter(
                maritalStatus => maritalStatus.name.toLowerCase().indexOf(value.name.toLowerCase()) === 0));
            }
          })).subscribe());
      }
    });

    this.partyReferenceService.getMarriageTypes().pipe(first()).subscribe((marriageTypes: MarriageType[]) => {
      const marriageTypeControl = this.partyReferenceForm.get('marriageType');

      if (marriageTypeControl) {
        this.subscriptions.add(marriageTypeControl.valueChanges.pipe(
          startWith(''),
          debounceTime(500),
          map((value: string | MarriageType) => {
            if (typeof (value) === 'string') {
              this.filteredMarriageTypes$.next(marriageTypes.filter(
                marriageType => marriageType.name.toLowerCase().indexOf(value.toLowerCase()) === 0));
            } else {
              this.filteredMarriageTypes$.next(marriageTypes.filter(
                marriageType => marriageType.name.toLowerCase().indexOf(value.name.toLowerCase()) === 0));
            }
          })).subscribe());
      }
    });

    this.partyReferenceService.getNextOfKinTypes().pipe(first()).subscribe((nextOfKinTypes: NextOfKinType[]) => {
      const nextOfKinTypeControl = this.partyReferenceForm.get('nextOfKinType');

      if (nextOfKinTypeControl) {
        this.subscriptions.add(nextOfKinTypeControl.valueChanges.pipe(
          startWith(''),
          debounceTime(500),
          map((value: string | NextOfKinType) => {
            if (typeof (value) === 'string') {
              this.filteredNextOfKinTypes$.next(nextOfKinTypes.filter(
                nextOfKinType => nextOfKinType.name.toLowerCase().indexOf(value.toLowerCase()) === 0));
            } else {
              this.filteredNextOfKinTypes$.next(nextOfKinTypes.filter(
                nextOfKinType => nextOfKinType.name.toLowerCase().indexOf(value.name.toLowerCase()) === 0));
            }
          })).subscribe());
      }
    });

    this.partyReferenceService.getOccupations().pipe(first()).subscribe((occupations: Occupation[]) => {
      const occupationControl = this.partyReferenceForm.get('occupation');

      if (occupationControl) {
        this.subscriptions.add(occupationControl.valueChanges.pipe(
          startWith(''),
          debounceTime(500),
          map((value: string | Occupation) => {
            if (typeof (value) === 'string') {
              this.filteredOccupations$.next(occupations.filter(
                occupation => occupation.name.toLowerCase().indexOf(value.toLowerCase()) === 0));
            } else {
              this.filteredOccupations$.next(occupations.filter(
                occupation => occupation.name.toLowerCase().indexOf(value.name.toLowerCase()) === 0));
            }
          })).subscribe());
      }
    });

    this.partyReferenceService.getPhysicalAddressPurposes().pipe(first()).subscribe((physicalAddressPurposes: PhysicalAddressPurpose[]) => {
      const physicalAddressPurposeControl = this.partyReferenceForm.get('physicalAddressPurpose');

      if (physicalAddressPurposeControl) {
        this.subscriptions.add(physicalAddressPurposeControl.valueChanges.pipe(
          startWith(''),
          debounceTime(500),
          map((value: string | PhysicalAddressPurpose) => {
            if (typeof (value) === 'string') {
              this.filteredPhysicalAddressPurposes$.next(physicalAddressPurposes.filter(
                physicalAddressPurpose => physicalAddressPurpose.name.toLowerCase().indexOf(value.toLowerCase()) === 0));
            } else {
              this.filteredPhysicalAddressPurposes$.next(physicalAddressPurposes.filter(
                physicalAddressPurpose => physicalAddressPurpose.name.toLowerCase().indexOf(value.name.toLowerCase()) === 0));
            }
          })).subscribe());
      }
    });

    this.partyReferenceService.getPhysicalAddressRoles().pipe(first()).subscribe((physicalAddressRoles: PhysicalAddressRole[]) => {
      const physicalAddressRoleControl = this.partyReferenceForm.get('physicalAddressRole');

      if (physicalAddressRoleControl) {
        this.subscriptions.add(physicalAddressRoleControl.valueChanges.pipe(
          startWith(''),
          debounceTime(500),
          map((value: string | PhysicalAddressRole) => {
            if (typeof (value) === 'string') {
              this.filteredPhysicalAddressRoles$.next(physicalAddressRoles.filter(
                physicalAddressRole => physicalAddressRole.name.toLowerCase().indexOf(value.toLowerCase()) === 0));
            } else {
              this.filteredPhysicalAddressRoles$.next(physicalAddressRoles.filter(
                physicalAddressRole => physicalAddressRole.name.toLowerCase().indexOf(value.name.toLowerCase()) === 0));
            }
          })).subscribe());
      }
    });

    this.partyReferenceService.getPhysicalAddressTypes().pipe(first()).subscribe((physicalAddressTypes: PhysicalAddressType[]) => {
      const physicalAddressTypeControl = this.partyReferenceForm.get('physicalAddressType');

      if (physicalAddressTypeControl) {
        this.subscriptions.add(physicalAddressTypeControl.valueChanges.pipe(
          startWith(''),
          debounceTime(500),
          map((value: string | PhysicalAddressType) => {
            if (typeof (value) === 'string') {
              this.filteredPhysicalAddressTypes$.next(physicalAddressTypes.filter(
                physicalAddressType => physicalAddressType.name.toLowerCase().indexOf(value.toLowerCase()) === 0));
            } else {
              this.filteredPhysicalAddressTypes$.next(physicalAddressTypes.filter(
                physicalAddressType => physicalAddressType.name.toLowerCase().indexOf(value.name.toLowerCase()) === 0));
            }
          })).subscribe());
      }
    });

    this.partyReferenceService.getPreferenceTypeCategories().pipe(first()).subscribe((preferenceTypeCategories: PreferenceTypeCategory[]) => {
      const preferenceTypeCategoryControl = this.partyReferenceForm.get('preferenceTypeCategory');

      if (preferenceTypeCategoryControl) {
        this.subscriptions.add(preferenceTypeCategoryControl.valueChanges.pipe(
          startWith(''),
          debounceTime(500),
          map((value: string | PreferenceTypeCategory) => {
            if (typeof (value) === 'string') {
              this.filteredPreferenceTypeCategories$.next(preferenceTypeCategories.filter(
                preferenceTypeCategory => preferenceTypeCategory.name.toLowerCase().indexOf(value.toLowerCase()) === 0));
            } else {
              this.filteredPreferenceTypeCategories$.next(preferenceTypeCategories.filter(
                preferenceTypeCategory => preferenceTypeCategory.name.toLowerCase().indexOf(value.name.toLowerCase()) === 0));
            }
          })).subscribe());
      }
    });

    this.partyReferenceService.getPreferenceTypes().pipe(first()).subscribe((preferenceTypes: PreferenceType[]) => {
      const preferenceTypeControl = this.partyReferenceForm.get('preferenceType');

      if (preferenceTypeControl) {
        this.subscriptions.add(preferenceTypeControl.valueChanges.pipe(
          startWith(''),
          debounceTime(500),
          map((value: string | PreferenceType) => {
            if (typeof (value) === 'string') {
              this.filteredPreferenceTypes$.next(preferenceTypes.filter(
                preferenceType => preferenceType.name.toLowerCase().indexOf(value.toLowerCase()) === 0));
            } else {
              this.filteredPreferenceTypes$.next(preferenceTypes.filter(
                preferenceType => preferenceType.name.toLowerCase().indexOf(value.name.toLowerCase()) === 0));
            }
          })).subscribe());
      }
    });

    this.partyReferenceService.getQualificationTypes().pipe(first()).subscribe((qualificationTypes: QualificationType[]) => {
      const qualificationTypeControl = this.partyReferenceForm.get('qualificationType');

      if (qualificationTypeControl) {
        this.subscriptions.add(qualificationTypeControl.valueChanges.pipe(
          startWith(''),
          debounceTime(500),
          map((value: string | QualificationType) => {
            if (typeof (value) === 'string') {
              this.filteredQualificationTypes$.next(qualificationTypes.filter(
                qualificationType => qualificationType.name.toLowerCase().indexOf(value.toLowerCase()) === 0));
            } else {
              this.filteredQualificationTypes$.next(qualificationTypes.filter(
                qualificationType => qualificationType.name.toLowerCase().indexOf(value.name.toLowerCase()) === 0));
            }
          })).subscribe());
      }
    });

    this.partyReferenceService.getRaces().pipe(first()).subscribe((races: Race[]) => {
      const raceControl = this.partyReferenceForm.get('race');

      if (raceControl) {
        this.subscriptions.add(raceControl.valueChanges.pipe(
          startWith(''),
          debounceTime(500),
          map((value: string | Race) => {
            if (typeof (value) === 'string') {
              this.filteredRaces$.next(races.filter(
                race => race.name.toLowerCase().indexOf(value.toLowerCase()) === 0));
            } else {
              this.filteredRaces$.next(races.filter(
                race => race.name.toLowerCase().indexOf(value.name.toLowerCase()) === 0));
            }
          })).subscribe());
      }
    });

    this.partyReferenceService.getResidencePermitTypes().pipe(first()).subscribe((residencePermitTypes: ResidencePermitType[]) => {
      const residencePermitTypeControl = this.partyReferenceForm.get('residencePermitType');

      if (residencePermitTypeControl) {
        this.subscriptions.add(residencePermitTypeControl.valueChanges.pipe(
          startWith(''),
          debounceTime(500),
          map((value: string | ResidencePermitType) => {
            if (typeof (value) === 'string') {
              this.filteredResidencePermitTypes$.next(residencePermitTypes.filter(
                permitType => permitType.name.toLowerCase().indexOf(value.toLowerCase()) === 0));
            } else {
              this.filteredResidencePermitTypes$.next(residencePermitTypes.filter(
                permitType => permitType.name.toLowerCase().indexOf(value.name.toLowerCase()) === 0));
            }
          })).subscribe());
      }
    });

    this.partyReferenceService.getResidencyStatuses().pipe(first()).subscribe((residencyStatuses: ResidencyStatus[]) => {
      const residencyStatusControl = this.partyReferenceForm.get('residencyStatus');

      if (residencyStatusControl) {
        this.subscriptions.add(residencyStatusControl.valueChanges.pipe(
          startWith(''),
          debounceTime(500),
          map((value: string | ResidencyStatus) => {
            if (typeof (value) === 'string') {
              this.filteredResidencyStatuses$.next(residencyStatuses.filter(
                residentialStatus => residentialStatus.name.toLowerCase().indexOf(value.toLowerCase()) === 0));
            } else {
              this.filteredResidencyStatuses$.next(residencyStatuses.filter(
                residentialStatus => residentialStatus.name.toLowerCase().indexOf(value.name.toLowerCase()) === 0));
            }
          })).subscribe());
      }
    });

    this.partyReferenceService.getResidentialTypes().pipe(first()).subscribe((residentialTypes: ResidentialType[]) => {
      const residentialTypeControl = this.partyReferenceForm.get('residentialType');

      if (residentialTypeControl) {
        this.subscriptions.add(residentialTypeControl.valueChanges.pipe(
          startWith(''),
          debounceTime(500),
          map((value: string | ResidentialType) => {
            if (typeof (value) === 'string') {
              this.filteredResidentialTypes$.next(residentialTypes.filter(
                residentialType => residentialType.name.toLowerCase().indexOf(value.toLowerCase()) === 0));
            } else {
              this.filteredResidentialTypes$.next(residentialTypes.filter(
                residentialType => residentialType.name.toLowerCase().indexOf(value.name.toLowerCase()) === 0));
            }
          })).subscribe());
      }
    });

    this.partyReferenceService.getRolePurposes().pipe(first()).subscribe((rolePurposes: RolePurpose[]) => {
      const rolePurposeControl = this.partyReferenceForm.get('rolePurpose');

      if (rolePurposeControl) {
        this.subscriptions.add(rolePurposeControl.valueChanges.pipe(
          startWith(''),
          debounceTime(500),
          map((value: string | RolePurpose) => {
            if (typeof (value) === 'string') {
              this.filteredRolePurposes$.next(rolePurposes.filter(
                rolePurpose => rolePurpose.name.toLowerCase().indexOf(value.toLowerCase()) === 0));
            } else {
              this.filteredRolePurposes$.next(rolePurposes.filter(
                rolePurpose => rolePurpose.name.toLowerCase().indexOf(value.name.toLowerCase()) === 0));
            }
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

    this.partyReferenceService.getRoleTypes().pipe(first()).subscribe((roleTypes: RoleType[]) => {
      const roleTypeControl = this.partyReferenceForm.get('roleType');

      if (roleTypeControl) {
        this.subscriptions.add(roleTypeControl.valueChanges.pipe(
          startWith(''),
          debounceTime(500),
          map((value: string | RoleType) => {
            if (typeof (value) === 'string') {
              this.filteredRoleTypes$.next(roleTypes.filter(
                roleType => roleType.name.toLowerCase().indexOf(value.toLowerCase()) === 0));
            } else {
              this.filteredRoleTypes$.next(roleTypes.filter(
                roleType => roleType.name.toLowerCase().indexOf(value.name.toLowerCase()) === 0));
            }
          })).subscribe());
      }
    });

    this.partyReferenceService.getSegmentationTypes().pipe(first()).subscribe((segmentationTypes: SegmentationType[]) => {
      const segmentationTypeControl = this.partyReferenceForm.get('segmentationType');

      if (segmentationTypeControl) {
        this.subscriptions.add(segmentationTypeControl.valueChanges.pipe(
          startWith(''),
          debounceTime(500),
          map((value: string | SegmentationType) => {
            if (typeof (value) === 'string') {
              this.filteredSegmentationTypes$.next(segmentationTypes.filter(
                segmentationType => segmentationType.name.toLowerCase().indexOf(value.toLowerCase()) === 0));
            } else {
              this.filteredSegmentationTypes$.next(segmentationTypes.filter(
                segmentationType => segmentationType.name.toLowerCase().indexOf(value.name.toLowerCase()) === 0));
            }
          })).subscribe());
      }
    });

    this.partyReferenceService.getSegments().pipe(first()).subscribe((segments: Segment[]) => {
      const segmentControl = this.partyReferenceForm.get('segment');

      if (segmentControl) {
        this.subscriptions.add(segmentControl.valueChanges.pipe(
          startWith(''),
          debounceTime(500),
          map((value: string | Segment) => {
            if (typeof (value) === 'string') {
              this.filteredSegments$.next(segments.filter(
                segment => segment.name.toLowerCase().indexOf(value.toLowerCase()) === 0));
            } else {
              this.filteredSegments$.next(segments.filter(
                segment => segment.name.toLowerCase().indexOf(value.name.toLowerCase()) === 0));
            }
          })).subscribe());
      }
    });

    this.partyReferenceService.getSkillTypes().pipe(first()).subscribe((skillTypes: SkillType[]) => {
      const skillTypeControl = this.partyReferenceForm.get('skillType');

      if (skillTypeControl) {
        this.subscriptions.add(skillTypeControl.valueChanges.pipe(
          startWith(''),
          debounceTime(500),
          map((value: string | SkillType) => {
            if (typeof (value) === 'string') {
              this.filteredSkillTypes$.next(skillTypes.filter(
                skillType => skillType.name.toLowerCase().indexOf(value.toLowerCase()) === 0));
            } else {
              this.filteredSkillTypes$.next(skillTypes.filter(
                skillType => skillType.name.toLowerCase().indexOf(value.name.toLowerCase()) === 0));
            }
          })).subscribe());
      }
    });

    this.partyReferenceService.getSourceOfFundsTypes().pipe(first()).subscribe((sourceOfFundsTypes: SourceOfFundsType[]) => {
      const sourceOfFundsTypeControl = this.partyReferenceForm.get('sourceOfFundsType');

      if (sourceOfFundsTypeControl) {
        this.subscriptions.add(sourceOfFundsTypeControl.valueChanges.pipe(
          startWith(''),
          debounceTime(500),
          map((value: string | SourceOfFundsType) => {
            if (typeof (value) === 'string') {
              this.filteredSourceOfFundsTypes$.next(sourceOfFundsTypes.filter(
                sourceOfFunds => sourceOfFunds.name.toLowerCase().indexOf(value.toLowerCase()) === 0));
            } else {
              this.filteredSourceOfFundsTypes$.next(sourceOfFundsTypes.filter(
                sourceOfFunds => sourceOfFunds.name.toLowerCase().indexOf(value.name.toLowerCase()) === 0));
            }
          })).subscribe());
      }
    });

    this.partyReferenceService.getSourceOfWealthTypes().pipe(first()).subscribe((sourceOfWealthTypes: SourceOfWealthType[]) => {
      const sourceOfWealthTypeControl = this.partyReferenceForm.get('sourceOfWealthType');

      if (sourceOfWealthTypeControl) {
        this.subscriptions.add(sourceOfWealthTypeControl.valueChanges.pipe(
          startWith(''),
          debounceTime(500),
          map((value: string | SourceOfWealthType) => {
            if (typeof (value) === 'string') {
              this.filteredSourceOfWealthTypes$.next(sourceOfWealthTypes.filter(
                sourceOfWealth => sourceOfWealth.name.toLowerCase().indexOf(value.toLowerCase()) === 0));
            } else {
              this.filteredSourceOfWealthTypes$.next(sourceOfWealthTypes.filter(
                sourceOfWealth => sourceOfWealth.name.toLowerCase().indexOf(value.name.toLowerCase()) === 0));
            }
          })).subscribe());
      }
    });

    this.partyReferenceService.getStatusTypeCategories().pipe(first()).subscribe((statusTypeCategories: StatusTypeCategory[]) => {
      const statusTypeCategoryControl = this.partyReferenceForm.get('statusTypeCategory');

      if (statusTypeCategoryControl) {
        this.subscriptions.add(statusTypeCategoryControl.valueChanges.pipe(
          startWith(''),
          debounceTime(500),
          map((value: string | StatusTypeCategory) => {
            if (typeof (value) === 'string') {
              this.filteredStatusTypeCategories$.next(statusTypeCategories.filter(
                statusTypeCategory => statusTypeCategory.name.toLowerCase().indexOf(value.toLowerCase()) === 0));
            } else {
              this.filteredStatusTypeCategories$.next(statusTypeCategories.filter(
                statusTypeCategory => statusTypeCategory.name.toLowerCase().indexOf(value.name.toLowerCase()) === 0));
            }
          })).subscribe());
      }
    });

    this.partyReferenceService.getStatusTypes().pipe(first()).subscribe((statusTypes: StatusType[]) => {
      const statusTypeControl = this.partyReferenceForm.get('statusType');

      if (statusTypeControl) {
        this.subscriptions.add(statusTypeControl.valueChanges.pipe(
          startWith(''),
          debounceTime(500),
          map((value: string | StatusType) => {
            if (typeof (value) === 'string') {
              this.filteredStatusTypes$.next(statusTypes.filter(
                statusType => statusType.name.toLowerCase().indexOf(value.toLowerCase()) === 0));
            } else {
              this.filteredStatusTypes$.next(statusTypes.filter(
                statusType => statusType.name.toLowerCase().indexOf(value.name.toLowerCase()) === 0));
            }
          })).subscribe());
      }
    });

    this.partyReferenceService.getTaxNumberTypes().pipe(first()).subscribe((taxNumberTypes: TaxNumberType[]) => {
      const taxNumberTypeControl = this.partyReferenceForm.get('taxNumberType');

      if (taxNumberTypeControl) {
        this.subscriptions.add(taxNumberTypeControl.valueChanges.pipe(
          startWith(''),
          debounceTime(500),
          map((value: string | TaxNumberType) => {
            if (typeof (value) === 'string') {
              this.filteredTaxNumberTypes$.next(taxNumberTypes.filter(
                taxNumberType => taxNumberType.name.toLowerCase().indexOf(value.toLowerCase()) === 0));
            } else {
              this.filteredTaxNumberTypes$.next(taxNumberTypes.filter(
                taxNumberType => taxNumberType.name.toLowerCase().indexOf(value.name.toLowerCase()) === 0));
            }
          })).subscribe());
      }
    });

    this.partyReferenceService.getTimesToContact().pipe(first()).subscribe((timesToContact: TimeToContact[]) => {
      const timeToContactControl = this.partyReferenceForm.get('timeToContact');

      if (timeToContactControl) {
        this.subscriptions.add(timeToContactControl.valueChanges.pipe(
          startWith(''),
          debounceTime(500),
          map((value: string | TimeToContact) => {
            if (typeof (value) === 'string') {
              this.filteredTimesToContact$.next(timesToContact.filter(
                timeToContact => timeToContact.name.toLowerCase().indexOf(value.toLowerCase()) === 0));
            } else {
              this.filteredTimesToContact$.next(timesToContact.filter(
                timeToContact => timeToContact.name.toLowerCase().indexOf(value.name.toLowerCase()) === 0));
            }
          })).subscribe());
      }
    });

    this.partyReferenceService.getTitles().pipe(first()).subscribe((titles: Title[]) => {
      const titleControl = this.partyReferenceForm.get('title');

      if (titleControl) {
        this.subscriptions.add(titleControl.valueChanges.pipe(
          startWith(''),
          debounceTime(500),
          map((value: string | Title) => {
            if (typeof (value) === 'string') {
              this.filteredTitles$.next(titles.filter(
                title => title.name.toLowerCase().indexOf(value.toLowerCase()) === 0));
            } else {
              this.filteredTitles$.next(titles.filter(
                title => title.name.toLowerCase().indexOf(value.name.toLowerCase()) === 0));
            }
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
    console.log('Title = ', this.partyReferenceForm.get('title')!.value);
  }
}
