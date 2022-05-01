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

import {Component, OnInit} from '@angular/core';
import {FormBuilder, FormControl, FormGroup, Validators} from '@angular/forms';
import {ActivatedRoute, Router} from '@angular/router';

/**
 * The PartyComponentsComponent class.
 *
 * @author Marcus Portmann
 */
@Component({
  templateUrl: 'party-components.component.html'
})
export class PartyComponentsComponent implements OnInit {

  associationPropertyTypeControl: FormControl = new FormControl('', Validators.required);

  associationTypeControl: FormControl = new FormControl('', Validators.required);

  attributeTypeCategoryControl: FormControl = new FormControl('', Validators.required);

  attributeTypeControl: FormControl = new FormControl('', Validators.required);

  consentTypeControl: FormControl = new FormControl('', Validators.required);

  contactMechanismPurposeControl: FormControl = new FormControl('', Validators.required);

  contactMechanismRoleControl: FormControl = new FormControl('', Validators.required);

  contactMechanismTypeControl: FormControl = new FormControl('', Validators.required);

  employmentStatusControl: FormControl = new FormControl('', Validators.required);

  employmentTypeControl: FormControl = new FormControl('', Validators.required);

  externalReferenceTypeControl: FormControl = new FormControl('', Validators.required);

  fieldOfStudyControl: FormControl = new FormControl('', Validators.required);

  genderControl: FormControl = new FormControl('', Validators.required);

  identityDocumentTypeControl: FormControl = new FormControl('', Validators.required);

  industryClassificationCategoryControl: FormControl = new FormControl('', Validators.required);

  industryClassificationControl: FormControl = new FormControl('', Validators.required);

  industryClassificationSystemControl: FormControl = new FormControl('', Validators.required);

  lockTypeCategoryControl: FormControl = new FormControl('', Validators.required);

  lockTypeControl: FormControl = new FormControl('', Validators.required);

  maritalStatusControl: FormControl = new FormControl('', Validators.required);

  marriageTypeControl: FormControl = new FormControl('', Validators.required);

  nextOfKinTypeControl: FormControl = new FormControl('', Validators.required);

  occupationControl: FormControl = new FormControl('', Validators.required);

  partyComponentsForm: FormGroup;

  physicalAddressPurposeControl: FormControl = new FormControl('', Validators.required);

  physicalAddressRoleControl: FormControl = new FormControl('', Validators.required);

  physicalAddressTypeControl: FormControl = new FormControl('', Validators.required);

  preferenceTypeCategoryControl: FormControl = new FormControl('', Validators.required);

  preferenceTypeControl: FormControl = new FormControl('', Validators.required);

  qualificationTypeControl: FormControl = new FormControl('', Validators.required);

  raceControl: FormControl = new FormControl('', Validators.required);

  residencePermitTypeControl: FormControl = new FormControl('', Validators.required);

  residencyStatusControl: FormControl = new FormControl('', Validators.required);

  residentialTypeControl: FormControl = new FormControl('', Validators.required);

  rolePurposeControl: FormControl = new FormControl('', Validators.required);

  roleTypeControl: FormControl = new FormControl('', Validators.required);

  segmentControl: FormControl = new FormControl('', Validators.required);

  segmentationTypeControl: FormControl = new FormControl('', Validators.required);

  skillTypeControl: FormControl = new FormControl('', Validators.required);

  sourceOfFundsTypeControl: FormControl = new FormControl('', Validators.required);

  sourceOfWealthTypeControl: FormControl = new FormControl('', Validators.required);

  statusTypeCategoryControl: FormControl = new FormControl('', Validators.required);

  statusTypeControl: FormControl = new FormControl('', Validators.required);

  taxNumberTypeControl: FormControl = new FormControl('', Validators.required);

  timeToContactControl: FormControl = new FormControl('', Validators.required);

  titleControl: FormControl = new FormControl('', Validators.required);

  constructor(private router: Router, private activatedRoute: ActivatedRoute,
              private formBuilder: FormBuilder) {

    this.partyComponentsForm = this.formBuilder.group({
      // hideRequired: false,
      // floatLabel: 'auto',
      // eslint-disable-next-line
      associationPropertyType: this.associationPropertyTypeControl,
      associationType: this.associationTypeControl,
      attributeTypeCategory: this.attributeTypeCategoryControl,
      attributeType: this.attributeTypeControl,
      consentType: this.consentTypeControl,
      contactMechanismPurpose: this.contactMechanismPurposeControl,
      contactMechanismRole: this.contactMechanismRoleControl,
      contactMechanismType: this.contactMechanismTypeControl,
      employmentStatus: this.employmentStatusControl,
      employmentType: this.employmentTypeControl,
      externalReferenceType: this.externalReferenceTypeControl,
      fieldOfStudy: this.fieldOfStudyControl,
      gender: this.genderControl,
      identityDocumentType: this.identityDocumentTypeControl,
      industryClassificationCategory: this.industryClassificationCategoryControl,
      industryClassification: this.industryClassificationControl,
      industryClassificationSystem: this.industryClassificationSystemControl,
      lockTypeCategory: this.lockTypeCategoryControl,
      lockType: this.lockTypeControl,
      maritalStatus: this.maritalStatusControl,
      marriageType: this.marriageTypeControl,
      nextOfKinType: this.nextOfKinTypeControl,
      occupation: this.occupationControl,
      physicalAddressPurpose: this.physicalAddressPurposeControl,
      physicalAddressRole: this.physicalAddressRoleControl,
      physicalAddressType: this.physicalAddressTypeControl,
      preferenceTypeCategory: this.preferenceTypeCategoryControl,
      preferenceType: this.preferenceTypeControl,
      qualificationType: this.qualificationTypeControl,
      race: this.raceControl,
      residencePermitType: this.residencePermitTypeControl,
      residencyStatus: this.residencyStatusControl,
      residentialType: this.residentialTypeControl,
      rolePurpose: this.rolePurposeControl,
      roleType: this.roleTypeControl,
      segment: this.segmentControl,
      segmentationType: this.segmentationTypeControl,
      skillType: this.skillTypeControl,
      sourceOfFundsType: this.sourceOfFundsTypeControl,
      sourceOfWealthType: this.sourceOfWealthTypeControl,
      statusTypeCategory: this.statusTypeCategoryControl,
      statusType: this.statusTypeControl,
      taxNumberType: this.taxNumberTypeControl,
      timeToContact: this.timeToContactControl,
      title: this.titleControl
    });
  }

  ngOnInit(): void {
  }

  ok(): void {
    console.log('Association Property Type = ', this.partyComponentsForm.get('associationPropertyType')!.value);
    console.log('Association Type = ', this.partyComponentsForm.get('associationType')!.value);
    console.log('Attribute Type Category = ', this.partyComponentsForm.get('attributeTypeCategory')!.value);
    console.log('Attribute Type = ', this.partyComponentsForm.get('attributeType')!.value);
    console.log('Consent Type = ', this.partyComponentsForm.get('consentType')!.value);
    console.log('Contact Mechanism Purpose = ', this.partyComponentsForm.get('contactMechanismPurpose')!.value);
    console.log('Contact Mechanism Role = ', this.partyComponentsForm.get('contactMechanismRole')!.value);
    console.log('Contact Mechanism Type = ', this.partyComponentsForm.get('contactMechanismType')!.value);
    console.log('Employment Status = ', this.partyComponentsForm.get('employmentStatus')!.value);
    console.log('Employment Type = ', this.partyComponentsForm.get('employmentType')!.value);
    console.log('External Reference Type = ', this.partyComponentsForm.get('externalReferenceType')!.value);
    console.log('Field Of Study  = ', this.partyComponentsForm.get('fieldOfStudy')!.value);
    console.log('Gender = ', this.partyComponentsForm.get('gender')!.value);
    console.log('Identity Document Type = ', this.partyComponentsForm.get('identityDocumentType')!.value);
    console.log('Industry Classification Category = ', this.partyComponentsForm.get('industryClassificationCategory')!.value);
    console.log('Industry Classification = ', this.partyComponentsForm.get('industryClassification')!.value);
    console.log('Industry Classification System = ', this.partyComponentsForm.get('industryClassificationSystem')!.value);
    console.log('Lock Type Category = ', this.partyComponentsForm.get('lockTypeCategory')!.value);
    console.log('Lock Type = ', this.partyComponentsForm.get('lockType')!.value);
    console.log('Marital Status = ', this.partyComponentsForm.get('maritalStatus')!.value);
    console.log('Marriage Type = ', this.partyComponentsForm.get('marriageType')!.value);
    console.log('Next Of Kin Type = ', this.partyComponentsForm.get('nextOfKinType')!.value);
    console.log('Occupation = ', this.partyComponentsForm.get('occupation')!.value);
    console.log('Physical Address Purpose = ', this.partyComponentsForm.get('physicalAddressPurpose')!.value);
    console.log('Physical Address Role = ', this.partyComponentsForm.get('physicalAddressRole')!.value);
    console.log('Physical Address Type = ', this.partyComponentsForm.get('physicalAddressType')!.value);
    console.log('Preference Type Category = ', this.partyComponentsForm.get('preferenceTypeCategory')!.value);
    console.log('Preference Type = ', this.partyComponentsForm.get('preferenceType')!.value);
    console.log('Qualification Type = ', this.partyComponentsForm.get('qualificationType')!.value);
    console.log('Race = ', this.partyComponentsForm.get('race')!.value);
    console.log('Residence Permit Type = ', this.partyComponentsForm.get('residencePermitType')!.value);
    console.log('Residency Status = ', this.partyComponentsForm.get('residencyStatus')!.value);
    console.log('ResidentialType = ', this.partyComponentsForm.get('residentialType')!.value);
    console.log('Role Purpose = ', this.partyComponentsForm.get('rolePurpose')!.value);
    console.log('Role Type = ', this.partyComponentsForm.get('roleType')!.value);
    console.log('Segment = ', this.partyComponentsForm.get('segment')!.value);
    console.log('Segmentation Type = ', this.partyComponentsForm.get('segmentationType')!.value);
    console.log('Skill Type = ', this.partyComponentsForm.get('skillType')!.value);
    console.log('Source Of Funds Type = ', this.partyComponentsForm.get('sourceOfFundsType')!.value);
    console.log('Source Of Wealth Type = ', this.partyComponentsForm.get('sourceOfWealthType')!.value);
    console.log('Status Type Category = ', this.partyComponentsForm.get('statusTypeCategory')!.value);
    console.log('Status Type = ', this.partyComponentsForm.get('statusType')!.value);
    console.log('Tax Number Type = ', this.partyComponentsForm.get('taxNumberType')!.value);
    console.log('Time To Contact = ', this.partyComponentsForm.get('timeToContact')!.value);
    console.log('Title = ', this.partyComponentsForm.get('title')!.value);
  }
}
