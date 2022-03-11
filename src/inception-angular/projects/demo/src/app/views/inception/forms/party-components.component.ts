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

  titleControl: FormControl = new FormControl('', Validators.required);

  constructor(private router: Router, private activatedRoute: ActivatedRoute,
              private formBuilder: FormBuilder) {

    this.partyComponentsForm = this.formBuilder.group({
      // hideRequired: false,
      // floatLabel: 'auto',
      // eslint-disable-next-line
      title: this.titleControl
    });
  }

  ok(): void {
    console.log('Title = ', this.partyComponentsForm.get('title')!.value);
  }
}
