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

import {
  AfterViewInit, Component, ElementRef, HostBinding, Input, OnDestroy, OnInit, Optional, Self,
  ViewChild
} from '@angular/core';
import {ControlValueAccessor, FormBuilder, FormControl, NgControl} from '@angular/forms';
import {MatAutocompleteSelectedEvent} from '@angular/material/autocomplete';
import {MatChipInputEvent, MatChipList} from '@angular/material/chips';
import {MatFormFieldControl} from '@angular/material/form-field';
import {ActivatedRoute, Router} from '@angular/router';
import {Country, ReferenceService} from 'ngx-inception/reference';
import {Observable, ReplaySubject, Subject, Subscription} from 'rxjs';
import {debounceTime, first, map, startWith} from 'rxjs/operators';
import {NavigationItem} from '../../../../../../ngx-inception/core/src/layout/services/navigation-item';

/**
 * The CountriesChipListComponent class implements the countries chip list component.
 *
 * @author Marcus Portmann
 */
@Component({
  // tslint:disable-next-line:component-selector
  selector: 'countries-chip-list',
  templateUrl: 'countries-chip-list.component.html',
  styleUrls: ['countries-chip-list.component.scss'],
})
//export class CountriesChipListComponent implements MatFormFieldControl<string[]>, ControlValueAccessor, OnInit, OnDestroy {

export class CountriesChipListComponent implements ControlValueAccessor, OnInit, OnDestroy, AfterViewInit {





  /**
   * The ISO 3166-1 alpha-2 codes for the selected countries.
   */
  @Input()
  value: string[] | null = [];




  /**
   * The selected countries.
   */
  countries: Country[] = [];

  private subscriptions: Subscription = new Subscription();


  @ViewChild(MatChipList) countriesChipList!: MatChipList;


  //countriesChipListControl: FormControl = new FormControl();

  //addCountryInputControl: FormControl = new FormControl();

  //@ViewChild('addCountryInput') addCountryInput!: ElementRef;

  //@ViewChild('countriesChipList') countriesChipList!: ElementRef;

  filteredCountries$: Subject<Country[]> = new ReplaySubject<Country[]>();

  constructor(private referenceService: ReferenceService) {
  }






  ngOnDestroy(): void {
    this.subscriptions.unsubscribe();
  }

  displayCountry(country: Country): string {
    if (!!country) {
      return country.shortName;
    } else {
      return '';
    }
  }

  // @Input()
  // get countriesOfCitizenship(): Country[] {
  //   return [new Country('ZA', )];
  // }

  ngOnInit(): void {


    /*
    this.referenceService.getCountries().pipe(first()).subscribe((countries: Country[]) => {

      for (const country of this.countryCodes) {
        if (country.code == 'ZA') {
          this.countriesOfCitizenship.push(country);
        }
      }

      this.subscriptions.add(this.addCountryControl.valueChanges.pipe(
        startWith(''),
        debounceTime(500),
        map((value: string | Country) => {
          this.filteredCountriesOfCitizenship$.next(this._filterCountries(countries,
            this.countriesOfCitizenship, value));

          // if (typeof (value) === 'string') {
          //   this.filteredCountriesOfCitizenship$.next(countries.filter(
          //     country => country.shortName.toLowerCase().indexOf(value.toLowerCase()) === 0));
          // } else {
          //   this.filteredCountriesOfCitizenship$.next(countries.filter(
          //     country => country.shortName.toLowerCase().indexOf(value.shortName.toLowerCase()) === 0));
          // }
        })).subscribe());
    });

     */
  }

  selectCountry(event: MatAutocompleteSelectedEvent): void {
    // this.countries.push(event.option.value);
    // this.addCountryInput.nativeElement.value = '';
    // this.addCountryControl.setValue(null);
  }

  removeCountry(country: Country, index: number): void {
    // this.countries.splice(index, 1);
    // this.stateChanges.next();

    // const index = this.fruits.indexOf(fruit);
    //
    // if (index >= 0) {
    //   this.fruits.splice(index, 1);
    // }
  }

  addCountry(event: MatChipInputEvent): void {
    // const value = (event.value || '').trim();
    //
    // // Add our fruit
    // if (value) {
    //   this.fruits.push(value);
    // }
    //
    // // Clear the input value
    // event.chipInput!.clear();
    //
    // this.fruitCtrl.setValue(null);
  }

  private _filterCountries(countries: Country[], value: string | Country): Country[] {

    if (value) {
      let lowercaseValue: string;

      if (typeof (value) === 'string') {
        lowercaseValue = value.toLowerCase()
      } else {
        lowercaseValue = value.shortName.toLowerCase();
      }

      return countries.filter((country: Country) => {
        for (const selectedCountry of this.countries) {
          if (country.code == selectedCountry.code) {
            return false;
          }
        }

        return country.shortName.toLowerCase().indexOf(lowercaseValue) === 0;
      });
    } else {
      return countries;
    }
  }




  ngAfterViewInit(): void {
    console.log('[CountriesChipListComponent][ngAfterViewInit] this.value = ', this.value);

    console.log('[CountriesChipListComponent][ngAfterViewInit] this.countriesChipList = ', this.countriesChipList);

  }

  // tslint:disable-next-line:no-any
  registerOnChange(fn: any): void {

  }

  // tslint:disable-next-line:no-any
  registerOnTouched(fn: any): void {
  }

  // tslint:disable-next-line:no-any
  writeValue(obj: any): void {
  }

}
