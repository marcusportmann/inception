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

import {Component, OnDestroy, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {ActivatedRoute, Router} from '@angular/router';
import {Country, Language, ReferenceService, Region} from 'ngx-inception/reference';
import {ReplaySubject, Subject, Subscription} from 'rxjs';
import {debounceTime, first, map, startWith} from 'rxjs/operators';

/**
 * The ReferenceFormComponent class implements the reference form component.
 *
 * @author Marcus Portmann
 */
@Component({
  templateUrl: 'reference-form.component.html'
})
export class ReferenceFormComponent implements OnInit, OnDestroy {

  filteredCountries$: Subject<Country[]> = new ReplaySubject<Country[]>();

  filteredLanguages$: Subject<Language[]> = new ReplaySubject<Language[]>();

  filteredRegions$: Subject<Region[]> = new ReplaySubject<Region[]>();

  referenceForm: FormGroup;

  private subscriptions: Subscription = new Subscription();

  constructor(private router: Router, private activatedRoute: ActivatedRoute,
              private formBuilder: FormBuilder, private referenceService: ReferenceService) {

    this.referenceForm = this.formBuilder.group({
      // hideRequired: false,
      // floatLabel: 'auto',
      // eslint-disable-next-line
      country: ['', Validators.required],
      language: ['', Validators.required],
      region: ['', Validators.required]
    });
  }

  displayCountry(country: Country): string {
    if (!!country) {
      return country.shortName;
    } else {
      return '';
    }
  }

  displayLanguage(language: Language): string {
    if (!!language) {
      return language.name;
    } else {
      return '';
    }
  }

  displayRegion(region: Region): string {
    if (!!region) {
      return region.name;
    } else {
      return '';
    }
  }

  ngOnDestroy(): void {
    this.subscriptions.unsubscribe();
  }

  ngOnInit(): void {
    this.referenceService.getCountries().pipe(first()).subscribe((countries: Map<string, Country>) => {
      const countryControl = this.referenceForm.get('country');

      if (countryControl) {
        this.subscriptions.add(countryControl.valueChanges.pipe(
          startWith(''),
          debounceTime(500),
          map((value: string | Country) => {
            if (typeof (value) === 'string') {
              value = value.toLowerCase();
            } else {
              value = value.shortName.toLowerCase();
            }

            let filteredCountries: Country[] = [];

            for (const country of countries.values()) {
              if (country.shortName.toLowerCase().indexOf(value) === 0) {
                filteredCountries.push(country);
              }
            }

            this.filteredCountries$.next(filteredCountries);
          })).subscribe());
      }
    });

    this.referenceService.getLanguages().pipe(first()).subscribe((languages: Map<string, Language>) => {
      const languageControl = this.referenceForm.get('language');

      if (languageControl) {
        this.subscriptions.add(languageControl.valueChanges.pipe(
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

    this.referenceService.getRegions('ZA').pipe(first()).subscribe((regions: Map<string, Region>) => {
      const regionControl = this.referenceForm.get('region');

      if (regionControl) {
        this.subscriptions.add(regionControl.valueChanges.pipe(
          startWith(''),
          debounceTime(500),
          map((value: string | Region) => {
            if (typeof (value) === 'string') {
              value = value.toLowerCase();
            } else {
              value = value.name.toLowerCase();
            }

            let filteredRegions: Region[] = [];

            for (const region of regions.values()) {
              if (region.name.toLowerCase().indexOf(value) === 0) {
                filteredRegions.push(region);
              }
            }

            this.filteredRegions$.next(filteredRegions);
          })).subscribe());
      }
    });
  }

  ok(): void {
    console.log('Country = ', this.referenceForm.get('country')!.value);
    console.log('Language = ', this.referenceForm.get('language')!.value);
    console.log('Region = ', this.referenceForm.get('region')!.value);
  }

}
