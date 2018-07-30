/*
 * Copyright 2018 Marcus Portmann
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
import {patternValidator} from '../../../inception/validators/pattern-validator';
import {NavigationService} from "../../../inception/services/navigation/navigation.service";

import * as moment from 'moment';
import {Observable} from "rxjs/Observable";
import {map, startWith} from 'rxjs/operators';


export interface StateGroup {
  letter: string;
  names: string[];
}


@Component({
  templateUrl: 'example-form.component.html'
})
export class ExampleFormComponent implements OnInit {

  static readonly MIN_DATE = new Date(1900, 1, 1);
  static readonly MAX_DATE = Date.now();

  advancedForm: FormGroup;

  titles: Array<any> = [
    {name: 'Mr', value: 'Mr'},
    {name: 'Mrs', value: 'Mrs'},
    {name: 'Ms', value: 'Ms'}
  ];

  countryOptions: string[] = ['Botswana', 'Namibia', 'Mozambique', 'South Africa', 'Swaziland', 'Zimbabwe'];

  filteredCountryOptions: Observable<string[]>;

  constructor(private formBuilder: FormBuilder, private navigationService: NavigationService) {

    this.advancedForm = this.formBuilder.group({
      //hideRequired: false,
      //floatLabel: 'auto',
      // tslint:disable-next-line
      firstName: ['', Validators.required],
      lastName: ['', Validators.required],
      title: ['', Validators.required],
      //dateOfBirth: [moment(), Validators.required],
      dateOfBirth: ['', Validators.required],
      password: ['', Validators.required],
      confirmPassword: ['', Validators.required],
      favoriteCountry: ['', Validators.required],
      grossIncome: ['', Validators.required],
      favoriteColor: ['', Validators.required],
      favoritePetDog: [''],
      favoritePetCat: ['true'],
      favoritePetFish: [''],
      notes: ['']
    });
  }

  ngOnInit() {
    this.filteredCountryOptions = this.advancedForm.get('favoriteCountry').valueChanges.pipe(
      startWith(''),
      map(value => this._filter(value))
    );
  }

  onSubmit() {



    console.log('favorite color = ', this.advancedForm.get('favoriteColor').value);

  }

  private _filter(value: string): string[] {
    const filterValue = value.toLowerCase();

    return this.countryOptions.filter(option => option.toLowerCase().indexOf(filterValue) === 0);
  }
}
