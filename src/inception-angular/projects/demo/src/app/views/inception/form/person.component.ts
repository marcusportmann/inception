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
import {Language, ReferenceService} from 'ngx-inception/reference';
import {ReplaySubject, Subject, Subscription} from 'rxjs';
import {debounceTime, first, map, startWith} from 'rxjs/operators';

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
export class PersonComponent implements OnInit, OnDestroy {

  countriesOfCitizenshipControl: FormControl = new FormControl([], Validators.required);

  filteredLanguages$: Subject<Language[]> = new ReplaySubject<Language[]>();

  givenNameControl: FormControl = new FormControl('', Validators.required);

  languageControl: FormControl = new FormControl('', Validators.required);

  /**
   * The person.
   */
  @Input() person: Person | null = null;

  surnameControl: FormControl = new FormControl('', Validators.required);

  private subscriptions: Subscription = new Subscription();

  constructor(private partyReferenceService: PartyReferenceService,
              private referenceService: ReferenceService) {
  }

  clickMe(): void {
    this.countriesOfCitizenshipControl.setValue(['ZA']);
  }

  displayLanguage(language: Language): string {
    if (!!language) {
      return language.name;
    } else {
      return '';
    }
  }

  ngOnDestroy(): void {
    this.subscriptions.unsubscribe();
  }

  ngOnInit(): void {
    this.referenceService.getLanguages().pipe(first()).subscribe((languages: Map<string, Language>) => {
      if (this.languageControl) {
        this.subscriptions.add(this.languageControl.valueChanges.pipe(
          startWith(''),
          debounceTime(500),
          map((value: string | Language) => {
            if (typeof (value) === 'string') {
              value = value.toLowerCase();
            } else {
              value = value.shortName.toLowerCase();
            }

            let filteredLanguages: Language[] = [];

            for (const language of languages.values()) {
              if (language.shortName.toLowerCase().indexOf(value) === 0) {
                filteredLanguages.push(language);
              }
            }

            this.filteredLanguages$.next(filteredLanguages);
          })).subscribe());
      }
    });
  }
}
