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

@Component({
  templateUrl: 'advanced-form.component.html'
})
export class AdvancedFormComponent {

  public static readonly MIN_DATE = new Date(1900, 1, 1);
  public static readonly MAX_DATE = Date.now();

  private advancedForm: FormGroup;

  public titles: Array<any> = [
    {label: 'Mr', value: 'Mr'},
    {label: 'Mrs', value: 'Mrs'},
    {label: 'Ms', value: 'Ms'}
  ];

  constructor(private formBuilder: FormBuilder, private navigationService: NavigationService) {

    this.advancedForm = this.formBuilder.group({
      // tslint:disable-next-line
      firstName: ['', Validators.required],
      lastName: ['', Validators.required],
      title: ['', Validators.required],
      dateOfBirth: ['', Validators.required],
      password: ['', Validators.required],
      confirmPassword: ['', Validators.required],
      favouritePet: ['', Validators.required],
      employmentPeriod: ['', Validators.required],
      inlineRadio: ['', Validators.required],
      inlineCheckbox1: [''],
      inlineCheckbox2: [''],
      inlineCheckbox3: [''],
      radio: ['', Validators.required],
      checkbox1: [''],
      checkbox2: [''],
      checkbox3: [''],
      favouriteTime: ['', Validators.required],
      notes: ['', Validators.required]
    });
  }

  public onSubmit() {

  }
}
