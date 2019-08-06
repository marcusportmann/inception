/*
 * Copyright 2019 Marcus Portmann
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
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {map, startWith} from 'rxjs/operators';
import {ActivatedRoute, Router} from '@angular/router';


/**
 * The ExampleFormComponent class implements the example form component.
 *
 * @author Marcus Portmann
 */
@Component({
  templateUrl: 'example-form.component.html'
})
export class ExampleFormComponent implements OnInit {

  static readonly MIN_DATE = new Date(1900, 1, 1);

  static readonly MAX_DATE = Date.now();

  exampleForm: FormGroup;

  titles: Array<{}> = [{
    name: 'Mr',
    value: 'Mr'
  }, {
    name: 'Mrs',
    value: 'Mrs'
  }, {
    name: 'Ms',
    value: 'Ms'
  }
  ];

  countryOptions = ['Botswana', 'Namibia', 'Mozambique', 'South Africa', 'Swaziland',
    'Zimbabwe'
  ];

  //filteredCountryOptions: Observable<string[]>;

  constructor(private router: Router, private activatedRoute: ActivatedRoute,
              private formBuilder: FormBuilder) {

    this.exampleForm = this.formBuilder.group({
      //hideRequired: false,
      //floatLabel: 'auto',
      // tslint:disable-next-line
      firstName: ['', Validators.required],
      lastName: ['', Validators.required],
      title: ['', Validators.required], //dateOfBirth: [moment(), Validators.required],
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
      favoriteCountryControl.valueChanges
        .pipe(startWith(''), map(value => this.filter(value)));
    }
  }

  onSubmit(): void {
    const favorityColorControl = this.exampleForm.get('favoriteColor');

    if (favorityColorControl) {
      console.log('favorite color = ', favorityColorControl.value);
    }
  }

  private filter(value: string): string[] {
    const filterValue = value.toLowerCase();

    return this.countryOptions.filter(option => option.toLowerCase().indexOf(filterValue) === 0);
  }
}
