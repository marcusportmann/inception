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

import {AfterViewInit, Component, Input, OnDestroy, OnInit} from '@angular/core';
import {FormGroup} from '@angular/forms';
import {PartyReferenceService, Person} from 'ngx-inception/party';
import {ReferenceService} from 'ngx-inception/reference';
import {Subscription} from 'rxjs';

/**
 * The PersonComponent class implements the person component.
 *
 * @author Marcus Portmann
 */
@Component({
  // tslint:disable-next-line:component-selector
  selector: 'person',
  templateUrl: 'person.component.html',
  styleUrls: ['person.component.scss'],
})
export class PersonComponent implements OnInit, OnDestroy, AfterViewInit {

  personForm: FormGroup;

  /**
   * The person.
   */
  @Input() person: Person | null = null;

  private subscriptions: Subscription = new Subscription();

  constructor(private partyReferenceService: PartyReferenceService,
              private referenceService: ReferenceService) {
    // Initialise the form
    this.personForm = new FormGroup({});
  }

  ngOnDestroy(): void {
    this.subscriptions.unsubscribe();
  }

  ngOnInit(): void {
    console.log('[ngOnInit] person = ', this.person);
  }

  ngAfterViewInit(): void {
    console.log('[ngAfterViewInit] person = ', this.person);
  }
}
