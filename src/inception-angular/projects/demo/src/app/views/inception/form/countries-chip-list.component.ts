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

import {coerceBooleanProperty} from '@angular/cdk/coercion';
import {
  Component, ElementRef, HostBinding, Input, OnDestroy, OnInit, Optional, Self, ViewChild
} from '@angular/core';
import {ControlValueAccessor, NgControl} from '@angular/forms';
import {MatAutocompleteSelectedEvent} from '@angular/material/autocomplete';
import {MatChipList} from '@angular/material/chips';
import {MatFormFieldControl} from '@angular/material/form-field';
import {MatInput} from '@angular/material/input';
import {Country, ReferenceService} from 'ngx-inception/reference';
import {ReplaySubject, Subject, Subscription} from 'rxjs';
import {debounceTime, first, map, startWith} from 'rxjs/operators';

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
  providers: [
    {
      provide: MatFormFieldControl,
      useExisting: CountriesChipListComponent
    }
  ]
})
// export class CountriesChipListComponent implements MatFormFieldControl<string[]>, ControlValueAccessor, OnInit, OnDestroy, AfterViewInit {
export class CountriesChipListComponent implements MatFormFieldControl<string[]>, ControlValueAccessor, OnInit, OnDestroy {

  private static _nextId: number = 0;

  /**
   * The reference to the element for the add country input.
   */
  @ViewChild('addCountryInput') addCountryElementRef!: ElementRef;

  /**
   * The add country input.
   */
  @ViewChild(MatInput, {static: true}) addCountryInput!: MatInput;

  /**
   * The observable providing access to the value for the add country input as it changes.
   */
  addCountryValue$: Subject<string> = new ReplaySubject<string>();

  /**
   * The name for the control type.
   */
  controlType = 'countries-chip-list';

  /**
   * The selected countries.
   */
  countries: Country[] = [];

  /**
   * The countries chip list.
   */
  @ViewChild('countriesChipList', {static: true}) countriesChipList!: MatChipList;

  /**
   * The ISO 3166-1 alpha-2 codes for the selected countries.
   */
  countryCodes: string[] = [];

  /**
   * The filtered countries for the autocomplete for the add country input.
   */
  filteredCountries$: Subject<Country[]> = new ReplaySubject<Country[]>();

  /**
   * Whether the control is focused.
   */
  focused = false;

  /**
   * The ID for the control.
   */
  @HostBinding() id = `countries-chip-list-${CountriesChipListComponent._nextId++}`;

  /**
   * The observable indicating that the state of the control has changed.
   */
  stateChanges = new Subject<void>();

  /**
   * Has the control received a touch event.
   */
  touched: boolean = false;

  @Input('aria-describedby') userAriaDescribedBy?: string;

  /**
   * Whether the control is disabled.
   * @private
   */
  private _disabled = false;

  /**
   * The placeholder for the add country
   * @private
   */
  private _placeholder: string = '';

  /**
   * Whether the control is required.
   * @private
   */
  private _required: boolean = false;

  private subscriptions: Subscription = new Subscription();

  constructor(private referenceService: ReferenceService, @Optional() @Self() public ngControl: NgControl) {
    if (this.ngControl != null) {
      /*
       * Setting the value accessor directly (instead of using the providers) to avoid running into
       * a circular import.
       */
      this.ngControl.valueAccessor = this;
    }
  }

  @Input()
  get disabled(): boolean {
    return this._disabled;
  }

  set disabled(value: boolean) {
    this._disabled = coerceBooleanProperty(value);

    if (this._disabled) {
      this.countriesChipList.disabled = true;
      this.addCountryInput.disabled = true;
    }

    this.stateChanges.next();
  }

  get empty(): boolean {
    return this.countryCodes.length === 0;
  }

  get errorState(): boolean {
    return this.required && (this.countries.length == 0) && this.touched;
  }

  @Input()
  get placeholder() {
    return this._placeholder;
  }

  set placeholder(placeholder) {
    this._placeholder = placeholder;
    this.stateChanges.next();
  }

  @Input()
  get required(): boolean {
    return this._required;
  }

  set required(req: any) {
    this._required = coerceBooleanProperty(req);
    this.stateChanges.next();
  }

  @HostBinding('class.floating')
  get shouldLabelFloat() {
    return this.focused || !this.empty || this.countriesChipList.focused || this.addCountryInput.focused;
  }

  /**
   * Returns the ISO 3166-1 alpha-2 codes for the selected countries.
   *
   * @return the ISO 3166-1 alpha-2 codes for the selected countries
   */
  public get value(): string[] | null {
    return this.countryCodes;
  }

  /**
   * Set the ISO 3166-1 alpha-2 codes for the selected countries.
   *
   * @param countryCodes the ISO 3166-1 alpha-2 codes for the selected countries
   */
  @Input()
  public set value(countryCodes: string[] | null) {
    if (countryCodes !== undefined) {
      if (this.countryCodes !== countryCodes) {
        this.referenceService.getCountries().pipe(first()).subscribe((countries: Map<string, Country>) => {
          this.countries = [];
          this.countryCodes = [];

          if (!!countryCodes) {
            for (const countryCode of countryCodes) {
              let country: Country | undefined = countries.get(countryCode);

              if (!!country) {
                this.countries.push(country);
                this.countryCodes.push(country.code);
              }
            }

            this.stateChanges.next();
          }
        });
      }
    } else {
      this.countries = [];
      this.countryCodes = [];
      this.stateChanges.next();
    }
  }

  addCountryInputChanged(event: Event) {
    if (((event.target as HTMLInputElement).value) !== undefined) {
      this.addCountryValue$.next((event.target as HTMLInputElement).value);
    }
  }

  ngOnDestroy(): void {
    this.subscriptions.unsubscribe();
    this.stateChanges.complete();
  }

  ngOnInit(): void {
    // Pass the placeholder to the add country input
    this.addCountryInput.placeholder = this._placeholder;

    // When the countries chip list is touched then we consider ourselves touched
    this.countriesChipList.registerOnTouched(() => {
      this.touched = true;
      this.onTouched();
      this.stateChanges.next();
    });

    // Update filtered countries based on the value of add country input
    this.referenceService.getCountries().pipe(first()).subscribe((countries: Map<string, Country>) => {
      this.subscriptions.add(this.addCountryValue$.pipe(
        startWith(''),
        debounceTime(500),
        map((value: string) => {
          this.filteredCountries$.next(this._filterCountries(Array.from(countries.values()), value));
        })).subscribe());
    });
  }

  onChange: any = () => {
  };

  onContainerClick(event: MouseEvent) {
    if ((event.target as Element).tagName.toLowerCase() != 'input') {
      this.addCountryInput.focus();
    }
  }

  onFocusIn(event: FocusEvent) {
    if (!this.focused) {
      this.focused = true;
      this.stateChanges.next();
    }
  }

  onFocusOut(event: FocusEvent) {
    this.touched = true;
    this.onTouched();
    this.focused = this.countriesChipList.focused || this.addCountryInput.focused;
    this.stateChanges.next();
  }

  onTouched: any = () => {
  };

  // tslint:disable-next-line:no-any
  registerOnChange(fn: any): void {
    this.onChange = fn;
    this.countriesChipList.registerOnChange(fn);
  }

  // tslint:disable-next-line:no-any
  registerOnTouched(fn: any): void {
    this.onTouched = fn;
    this.countriesChipList.registerOnTouched(fn);
  }

  removeCountry(country: Country, index: number): void {
    this.countries.splice(index, 1);
    this.countryCodes.splice(index, 1);
    this.resetAddCountry();
    this.stateChanges.next();
  }

  resetAddCountry(): void {
    this.addCountryInput.value = '';
    this.addCountryValue$.next('');
    this.addCountryElementRef.nativeElement.blur();
  }

  selectCountry(event: MatAutocompleteSelectedEvent): void {
    this.countries.push(event.option.value);
    this.countryCodes.push((event.option.value as Country).code);
    this.resetAddCountry();
    this.stateChanges.next();
  }

  setDescribedByIds(ids: string[]) {
    // TODO: IMPLEMENT THIS
    // https://material.angular.io/guide/creating-a-custom-form-field-control

    // const controlElement = this._elementRef.nativeElement
    // .querySelector('.example-tel-input-container')!;
    // controlElement.setAttribute('aria-describedby', ids.join(' '));
  }

  // tslint:disable-next-line:no-any
  writeValue(value: any): void {
    if (typeof value === 'string') {
      if (value === '') {
        this.value = [];
      } else {
        this.value = [value];
      }
    } else if (Array.isArray(value)) {
      this.value = value as string[];
    }
  }

  private _filterCountries(countries: Country[], value: string): Country[] {
    if (value) {
      value = value.toLowerCase();

      return countries.filter((country: Country) => {
        if (this.countryCodes.includes(country.code)) {
          return false;
        } else {
          return (country.shortName.toLowerCase().indexOf(value) === 0);
        }
      });
    } else {
      return countries.filter((country: Country) => {
        if (this.countryCodes.includes(country.code)) {
          return false;
        } else {
          return true;
        }
      });
    }
  }
}
