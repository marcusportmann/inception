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

  countriesOfCitizenshipControl: FormControl = new FormControl([], Validators.required);

  givenNameControl: FormControl = new FormControl('', Validators.required);

  languageControl: FormControl = new FormControl('', Validators.required);

  nameControl: FormControl = new FormControl({value: '', disabled: true});

  activePanel: number = 0;

  surnameControl: FormControl = new FormControl('', Validators.required);

  private subscriptions: Subscription = new Subscription();

  constructor(private partyReferenceService: PartyReferenceService,
              private referenceService: ReferenceService) {
  }

  private _person: Person | null = null;

  /**
   * The person.
   */
  @Input() get person(): Person | null {
    return this._person;
  }

  set person(person: Person | null) {
    console.log('[PersonComponent][person] person = ', person);

    this._person = person;

    if (person != null) {
      this.countriesOfCitizenshipControl.setValue(this._person?.countriesOfCitizenship);
      this.givenNameControl.setValue(this._person?.givenName);
      this.languageControl.setValue(this._person?.language);
      this.nameControl.setValue(this._person?.name);
      this.surnameControl.setValue(this._person?.surname);


    }
  }

  clickMe(): void {
    console.log('[PersonComponent][clickMe] this.countriesOfCitizenshipControl.value = ', this.countriesOfCitizenshipControl.value);
    console.log('[PersonComponent][clickMe] this.givenName.value = ', this.givenNameControl.value);
    console.log('[PersonComponent][clickMe] this.languageControl.value = ', this.languageControl.value);
    console.log('[PersonComponent][clickMe] this.nameControl.value = ', this.nameControl.value);
    console.log('[PersonComponent][clickMe] this.surnameControl.value = ', this.surnameControl.value);
  }

  nextPanel(): void {
    this.activePanel++;
  }

  ngOnDestroy(): void {
    this.subscriptions.unsubscribe();
  }

  ngOnInit(): void {
    console.log('[PersonComponent][ngOnInit] this.person = ', this.person);
  }

  previousPanel(): void {
    this.activePanel--;
  }

  setActivePanel(activePanel: number): void {
    this.activePanel = activePanel;
  }
}
