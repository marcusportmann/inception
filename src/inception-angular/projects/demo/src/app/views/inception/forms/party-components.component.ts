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

import {Component, OnDestroy, OnInit, ViewChild} from '@angular/core';
import {FormBuilder, FormGroup} from '@angular/forms';
import {ActivatedRoute, Router} from '@angular/router';
import {
  EntityType, PartyReferenceService, PartyService, Person, Snapshots
} from 'ngx-inception/party';
import {forkJoin, Subscription} from "rxjs";
import {first} from 'rxjs/operators';
import {PersonComponent} from './person.component';

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

  constructor(private router: Router, private activatedRoute: ActivatedRoute,
              private formBuilder: FormBuilder) {

    this.partyComponentsForm = this.formBuilder.group({
      // hideRequired: false,
      // floatLabel: 'auto',
      // eslint-disable-next-line
    });
  }
}
