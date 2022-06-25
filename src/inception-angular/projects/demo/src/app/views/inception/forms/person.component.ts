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

import {Component, Input, OnDestroy, OnInit} from '@angular/core';
import {FormControl, Validators} from '@angular/forms';
import {
  PartyReferenceService, Person, Role, RoleTypeAttributeTypeConstraint
} from 'ngx-inception/party';
import {ReferenceService} from 'ngx-inception/reference';
import {ISO8601Util} from 'ngx-inception/core';
import {Subject, Subscription} from 'rxjs';
import {first} from 'rxjs/operators';
import {ConstraintType} from '../../../../../../ngx-inception/party/src/services/constraint-type';

/**
 * The PersonComponent class implements the person component.
 *
 * @author Marcus Portmann
 */
@Component({
  // eslint-disable-next-line @angular-eslint/component-selector
  selector: 'person',
  templateUrl: 'person.component.html',
  styleUrls: ['person.component.scss'],
})
export class PersonComponent implements OnInit, OnDestroy {

  activePanel: number = 0;

  countriesOfCitizenshipControl: FormControl = new FormControl([], Validators.required);

  countryOfBirthControl: FormControl = new FormControl('');

  countryOfResidenceControl: FormControl = new FormControl('', Validators.required);

  givenNameControl: FormControl = new FormControl('', Validators.required);

  languageControl: FormControl = new FormControl('', Validators.required);

  maidenNameControl: FormControl = new FormControl('');

  middleNamesControl: FormControl = new FormControl('');

  nameControl: FormControl = new FormControl({value: '', disabled: true});

  preferredNameControl: FormControl = new FormControl('');

  surnameControl: FormControl = new FormControl('', Validators.required);

  titleControl: FormControl = new FormControl('');

  roles$: Subject<Role[]> = new Subject<Role[]>();

  private subscriptions: Subscription = new Subscription();

  constructor(private partyReferenceService: PartyReferenceService,
              private referenceService: ReferenceService) {
  }

  private _person: Person | null = null;

  /**
   * The person.
   */
  @Input() get person(): Person | null {

    if (this._person != null) {
      this._person.countriesOfCitizenship = (!!this.countriesOfCitizenshipControl.value) ? this.countriesOfCitizenshipControl.value : [];
      this._person.countryOfBirth = (!!this.countryOfBirthControl.value) ? this.countryOfBirthControl.value : undefined;
      this._person.countryOfResidence = (!!this.countryOfResidenceControl.value) ? this.countryOfResidenceControl.value : undefined;
      this._person.givenName = (!!this.givenNameControl.value) ? this.givenNameControl.value : undefined;
      this._person.language = (!!this.languageControl.value) ? this.languageControl.value : undefined;
      this._person.maidenName = (!!this.maidenNameControl.value) ? this.maidenNameControl.value : undefined;
      this._person.middleNames = (!!this.middleNamesControl.value) ? this.middleNamesControl.value : undefined;
      this._person.name = (!!this.nameControl.value) ? this.nameControl.value : undefined;
      this._person.preferredName = (!!this.preferredNameControl.value) ? this.preferredNameControl.value : undefined;
      this._person.surname = (!!this.surnameControl.value) ? this.surnameControl.value : undefined;
      this._person.title = (!!this.titleControl.value) ? this.titleControl.value : undefined;
    }

    return this._person;
  }

  set person(person: Person | null) {
    this._person = person;

    if (person != null) {
      this.countriesOfCitizenshipControl.setValue(person.countriesOfCitizenship);
      this.countryOfBirthControl.setValue(person.countryOfBirth);
      this.countryOfResidenceControl.setValue(person.countryOfResidence);
      this.givenNameControl.setValue(person.givenName);
      this.languageControl.setValue(person.language);
      this.maidenNameControl.setValue(person.maidenName);
      this.middleNamesControl.setValue(person.middleNames);
      this.nameControl.setValue(person.name);
      this.preferredNameControl.setValue(person.preferredName);
      this.surnameControl.setValue(person.surname);
      this.titleControl.setValue(person.title);

      this.roles$.next(person.roles);
    }
  }

  get valid(): boolean {
    return this.countriesOfCitizenshipControl.valid
      && this.countryOfBirthControl.valid
      && this.countryOfResidenceControl.valid
      && this.givenNameControl.valid
      && this.languageControl.valid
      && this.maidenNameControl.valid
      && this.middleNamesControl.valid
      && this.preferredNameControl.valid
      && this.surnameControl.valid
      && this.titleControl.valid;
  }

  deriveName(): void {
    if ((!!this.givenNameControl.value) || (!!this.middleNamesControl.value) || (!!this.surnameControl.value)) {
      let derivedName: string = this.givenNameControl.value
        + ((!!this.middleNamesControl.value) ? (' ' + this.middleNamesControl.value + ' ') : ' ')
        + this.surnameControl.value;

      this.nameControl.setValue(derivedName);

      console.log('derivedName = "' + derivedName + '"');

    } else {
      this.nameControl.setValue('');
    }

    this.nameControl.markAsTouched();
  }

  nextPanel(): void {
    this.activePanel++;
  }

  ngOnDestroy(): void {
    this.subscriptions.unsubscribe();
  }

  ngOnInit(): void {
    this.subscriptions.add(this.roles$.subscribe((roles: Role[]) => {
      this.partyReferenceService.getRoleTypeAttributeTypeConstraints().pipe(first()).subscribe((roleTypeAttributeTypeConstraints: RoleTypeAttributeTypeConstraint[]) => {
        let now = new Date();

        for (const roleTypeAttributeTypeConstraint of roleTypeAttributeTypeConstraints) {
          for (const role of roles) {
            console.log('role = ', role);
            console.log('role.effectiveFrom = ', role.effectiveFrom);
            console.log('typeof(role.effectiveFrom) = ', typeof(role.effectiveFrom));

            // Clear existing validators
            this.titleControl.clearValidators();

            // Add the appropriate validators based on the
            if ((roleTypeAttributeTypeConstraint.roleType === role.type)
              && ((!role.effectiveFrom) || (ISO8601Util.toDate(role.effectiveFrom).getTime() <= now.getTime()))
              && ((!role.effectiveTo) || (ISO8601Util.toDate(role.effectiveTo).getTime() >= now.getTime()))) {

              console.log('Found role type attribute type constraint for role type (' + roleTypeAttributeTypeConstraint.roleType + ') and attribute type (' + roleTypeAttributeTypeConstraint.attributeType + ')');

              // Process a "Required" constraint
              if (roleTypeAttributeTypeConstraint.type === ConstraintType.Required) {
                if (roleTypeAttributeTypeConstraint.attributeType === 'title') {
                  this.titleControl.addValidators(Validators.required);
                }
              }

            }
          }
        }
      })
    }));

    this.subscriptions.add(this.givenNameControl.valueChanges.subscribe((value: string) => {
      this.deriveName();
    }));

    this.subscriptions.add(this.middleNamesControl.valueChanges.subscribe((value: string) => {
      this.deriveName();
    }));

    this.subscriptions.add(this.surnameControl.valueChanges.subscribe((value: string) => {
      this.deriveName();
    }));
  }

  previousPanel(): void {
    this.activePanel--;
  }

  setActivePanel(activePanel: number): void {
    this.activePanel = activePanel;
  }

  get titleRequired(): boolean {
    return this.titleControl.hasValidator(Validators.required);
  }
}



