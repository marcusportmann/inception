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

import {Component} from '@angular/core';
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
export class PartyComponentsComponent {

  partyComponentsForm: FormGroup;

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
      title: this.titleControl
    });
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
    console.log('External Reference Type = ', this.partyComponentsForm.get('employmentReferenceType')!.value);
    console.log('Title = ', this.partyComponentsForm.get('title')!.value);
  }
}
