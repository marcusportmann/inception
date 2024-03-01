/*
 * Copyright Marcus Portmann
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
import {FormBuilder, FormControl, FormGroup, Validators} from '@angular/forms';
import {ActivatedRoute, Router} from '@angular/router';
import {Country, ReferenceService} from 'ngx-inception/reference';
import {Subscription} from 'rxjs';

/**
 * The Title class holds title information for the example form component.
 */
class Title {
  name: string;

  value: string;

  constructor(name: string, value: string) {
    this.name = name;
    this.value = value;
  }
}

/**
 * The ExampleComponent class.
 *
 * @author Marcus Portmann
 */
@Component({
  templateUrl: 'example.component.html'
})
export class ExampleComponent implements OnInit, OnDestroy {

  static readonly MAX_DATE = Date.now();

  static readonly MIN_DATE = new Date(1900, 1, 1);

  confirmPasswordControl: FormControl;

  dateOfBirthControl: FormControl;

  exampleForm: FormGroup;

  favoriteColorControl: FormControl;

  favoriteCountryControl: FormControl;

  grossIncomeControl: FormControl;

  nameControl: FormControl;

  passwordControl: FormControl;

  preferredNameControl: FormControl;

  titleControl: FormControl;

  titles: Array<Title> = [new Title('Mr', 'Mr'), new Title('Mrs', 'Mrs'), new Title('Ms', 'Ms')];

  private subscriptions: Subscription = new Subscription();

  constructor(private router: Router, private activatedRoute: ActivatedRoute,
              private formBuilder: FormBuilder, private referenceService: ReferenceService) {

    // Initialise the form controls
    this.confirmPasswordControl = new FormControl([], Validators.required);
    this.dateOfBirthControl = new FormControl([], Validators.required);
    this.favoriteColorControl = new FormControl([], Validators.required);
    this.favoriteCountryControl = new FormControl([], Validators.required);
    this.grossIncomeControl = new FormControl([], Validators.required);
    this.nameControl = new FormControl([], Validators.required);
    this.passwordControl = new FormControl([], Validators.required);
    this.preferredNameControl = new FormControl([], Validators.required);
    this.titleControl = new FormControl([], Validators.required);

    // Initialise the form
    this.exampleForm = this.formBuilder.group({
      // hideRequired: false,
      // floatLabel: 'auto',
      // eslint-disable-next-line
      name: this.nameControl,
      preferredName: this.preferredNameControl,
      title: this.titleControl,
      dateOfBirth: this.dateOfBirthControl,
      password: this.passwordControl,
      confirmPassword: this.confirmPasswordControl,
      favoriteCountry: this.favoriteCountryControl,
      grossIncome: this.grossIncomeControl,
      favoriteColor: this.favoriteColorControl,
      favoritePetDog: [''],
      favoritePetCat: [{value: 'true'}],
      favoritePetFish: [''],
      notes: ['']
    });
  }

  displayCountry(country: Country): string {
    if (!!country) {
      return country.shortName;
    } else {
      return '';
    }
  }

  ngOnDestroy(): void {
    this.subscriptions.unsubscribe();
  }

  ngOnInit(): void {
  }

  onSubmit(): void {
    console.log('favorite color = ', this.favoriteColorControl.value);
  }
}
