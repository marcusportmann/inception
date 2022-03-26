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

  filteredRoleTypeAttributeTypeConstraints$: Subject<RoleTypeAttributeTypeConstraint[]> = new ReplaySubject<RoleTypeAttributeTypeConstraint[]>();

  filteredRoleTypePreferenceTypeConstraints$: Subject<RoleTypePreferenceTypeConstraint[]> = new ReplaySubject<RoleTypePreferenceTypeConstraint[]>();

  partyReferenceForm: FormGroup;

  private subscriptions: Subscription = new Subscription();

  constructor(private router: Router, private activatedRoute: ActivatedRoute,
              private formBuilder: FormBuilder, private partyReferenceService: PartyReferenceService) {

    this.partyReferenceForm = this.formBuilder.group({
      // hideRequired: false,
      // floatLabel: 'auto',
      // eslint-disable-next-line
      roleTypeAttributeTypeConstraint: ['', Validators.required],
      roleTypePreferenceTypeConstraint: ['', Validators.required]
    });
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

  ngOnDestroy(): void {
    this.subscriptions.unsubscribe();
  }

  ngOnInit(): void {
    this.partyReferenceService.getRoleTypeAttributeTypeConstraints('individual_customer').pipe(first()).subscribe((roleTypeAttributeTypeConstraints: RoleTypeAttributeTypeConstraint[]) => {
      const roleTypeAttributeTypeConstraintControl = this.partyReferenceForm.get('roleTypeAttributeTypeConstraint');

      if (roleTypeAttributeTypeConstraintControl) {
        this.subscriptions.add(roleTypeAttributeTypeConstraintControl.valueChanges.pipe(
          startWith(''),
          debounceTime(500)).subscribe((value: string | RoleTypeAttributeTypeConstraint) => {
          if (typeof (value) === 'string') {
            this.filteredRoleTypeAttributeTypeConstraints$.next(roleTypeAttributeTypeConstraints.filter(
              roleTypeAttributeTypeConstraint => (roleTypeAttributeTypeConstraint.roleType.toLowerCase().indexOf(value.toLowerCase()) === 0) || (roleTypeAttributeTypeConstraint.attributeType.toLowerCase().indexOf(value.toLowerCase())) === 0));
          } else {
            this.filteredRoleTypeAttributeTypeConstraints$.next(roleTypeAttributeTypeConstraints.filter(
              roleTypeAttributeTypeConstraint => (roleTypeAttributeTypeConstraint.roleType.toLowerCase().indexOf(value.roleType.toLowerCase()) === 0) || (roleTypeAttributeTypeConstraint.attributeType.toLowerCase().indexOf(value.attributeType.toLowerCase())) === 0));
          }
        }));
      }
    });

    this.partyReferenceService.getRoleTypePreferenceTypeConstraints('individual_customer').pipe(first()).subscribe((roleTypePreferenceTypeConstraints: RoleTypePreferenceTypeConstraint[]) => {
      const roleTypePreferenceTypeConstraintControl = this.partyReferenceForm.get('roleTypePreferenceTypeConstraint');

      if (roleTypePreferenceTypeConstraintControl) {
        this.subscriptions.add(roleTypePreferenceTypeConstraintControl.valueChanges.pipe(
          startWith(''),
          debounceTime(500)).subscribe((value: string | RoleTypePreferenceTypeConstraint) => {
          if (typeof (value) === 'string') {
            this.filteredRoleTypePreferenceTypeConstraints$.next(roleTypePreferenceTypeConstraints.filter(
              roleTypePreferenceTypeConstraint => (roleTypePreferenceTypeConstraint.roleType.toLowerCase().indexOf(value.toLowerCase()) === 0) || roleTypePreferenceTypeConstraint.preferenceType.toLowerCase().indexOf(value.toLowerCase()) === 0));
          } else {
            this.filteredRoleTypePreferenceTypeConstraints$.next(roleTypePreferenceTypeConstraints.filter(
              roleTypePreferenceTypeConstraint => (roleTypePreferenceTypeConstraint.roleType.toLowerCase().indexOf(value.roleType.toLowerCase()) === 0) || roleTypePreferenceTypeConstraint.preferenceType.toLowerCase().indexOf(value.preferenceType.toLowerCase()) === 0));
          }
        }));
      }
    });
  }

  ok(): void {
    console.log('Role Type Attribute Type Constraint = ', this.partyReferenceForm.get('roleTypeAttributeTypeConstraint')!.value);
    console.log('Role Type Preference Type Constraint = ', this.partyReferenceForm.get('roleTypePreferenceTypeConstraint')!.value);
  }
}
