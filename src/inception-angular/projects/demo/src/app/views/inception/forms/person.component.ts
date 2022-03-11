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
import {PartyReferenceService, Person} from 'ngx-inception/party';
import {ReferenceService} from 'ngx-inception/reference';
import {Subscription} from 'rxjs';

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

  countryOfBirthControl: FormControl = new FormControl('', Validators.required);

  givenNameControl: FormControl = new FormControl('', Validators.required);

  languageControl: FormControl = new FormControl('', Validators.required);

  maidenNameControl: FormControl = new FormControl('');

  middleNamesControl: FormControl = new FormControl('');

  nameControl: FormControl = new FormControl({value: '', disabled: true});

  surnameControl: FormControl = new FormControl('', Validators.required);

  titleControl: FormControl = new FormControl('', Validators.required);

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
      this._person.givenName = (!!this.givenNameControl.value) ? this.givenNameControl.value : undefined;
      this._person.language = (!!this.languageControl.value) ? this.languageControl.value : undefined;
      this._person.maidenName = (!!this.maidenNameControl.value) ? this.maidenNameControl.value : undefined;
      this._person.middleNames = (!!this.middleNamesControl.value) ? this.middleNamesControl.value : undefined;
      this._person.name = (!!this.nameControl.value) ? this.nameControl.value : undefined;
      this._person.surname = (!!this.surnameControl.value) ? this.surnameControl.value : undefined;
      this._person.title = (!!this.titleControl.value) ? this.titleControl.value : undefined;
    }

    return this._person;
  }

  set person(person: Person | null) {
    if (person != null) {
      this.countriesOfCitizenshipControl.setValue(person.countriesOfCitizenship);
      this.countryOfBirthControl.setValue(person.countryOfBirth);
      this.givenNameControl.setValue(person.givenName);
      this.languageControl.setValue(person.language);
      this.maidenNameControl.setValue(person.maidenName);
      this.middleNamesControl.setValue(person.middleNames);
      this.nameControl.setValue(person.name);
      this.surnameControl.setValue(person.surname);
      this.titleControl.setValue(person.title);
    }

    this._person = person;
  }

  get valid(): boolean {
    return this.countriesOfCitizenshipControl.valid
      && this.countryOfBirthControl.valid
      && this.givenNameControl.valid
      && this.languageControl.valid
      && this.maidenNameControl.valid
      && this.middleNamesControl.valid
      && this.surnameControl.valid
      && this.titleControl.valid;
  }

  deriveName(): void {
    let derivedName: string = this.givenNameControl.value
      + ((!!this.middleNamesControl.value) ? (' ' + this.middleNamesControl.value + ' ') : ' ')
      + this.surnameControl.value;

    this.nameControl.setValue(derivedName);
  }

  nextPanel(): void {
    this.activePanel++;
  }

  ngOnDestroy(): void {
    this.subscriptions.unsubscribe();
  }

  ngOnInit(): void {
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
}



