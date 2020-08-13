/*
 * Copyright 2020 Marcus Portmann
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
import {map, startWith} from 'rxjs/operators';
import {ActivatedRoute, Router} from '@angular/router';
import {ReplaySubject, Subject, Subscription} from 'rxjs';

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
 * The ExampleFormComponent class implements the example form component.
 *
 * @author Marcus Portmann
 */
@Component({
  templateUrl: 'example-form.component.html'
})
export class ExampleFormComponent implements OnInit, OnDestroy {

  static readonly MIN_DATE = new Date(1900, 1, 1);

  static readonly MAX_DATE = Date.now();

  private subscriptions: Subscription = new Subscription();

  exampleForm: FormGroup;

  titles: Array<Title> = [new Title('Mr', 'Mr'), new Title('Mrs', 'Mrs'), new Title('Ms', 'Ms')];

  countryOptions = ['Botswana', 'Namibia', 'Mozambique', 'South Africa', 'Swaziland', 'Zimbabwe'];

  filteredCountryOptions$: Subject<string[]> = new ReplaySubject<string[]>();

  constructor(private router: Router, private activatedRoute: ActivatedRoute, private formBuilder: FormBuilder) {

    this.exampleForm = this.formBuilder.group({
      // hideRequired: false,
      // floatLabel: 'auto',
      // tslint:disable-next-line
      name: ['', Validators.required],
      preferredName: ['', Validators.required],
      title: ['', Validators.required], // dateOfBirth: [moment(), Validators.required],
      dateOfBirth: ['', Validators.required],
      password: ['', Validators.required],
      confirmPassword: ['', Validators.required],
      favoriteCountry: ['', Validators.required],
      grossIncome: ['', Validators.required],
      favoriteColor: ['', Validators.required],
      favoritePetDog: [''],
      favoritePetCat: [{value: 'true'}],
      favoritePetFish: [''],
      notes: ['']
    });
  }

  ngOnInit(): void {
    const favoriteCountryControl = this.exampleForm.get('favoriteCountry');

    if (favoriteCountryControl) {
      this.subscriptions.add(favoriteCountryControl.valueChanges
        .pipe(startWith(''), map(value => {
          this.filteredCountryOptions$.next(this.filter(value));
        })).subscribe());
    }
  }

  onSubmit(): void {
    const favoriteColorControl = this.exampleForm.get('favoriteColor');

    if (favoriteColorControl) {
      console.log('favorite color = ', favoriteColorControl.value);
    }
  }

  ngOnDestroy(): void {
    this.subscriptions.unsubscribe();
  }

  private filter(value: string): string[] {
    const filterValue = value.toLowerCase();

    return this.countryOptions.filter(option => option.toLowerCase().indexOf(filterValue) === 0);
  }
}
