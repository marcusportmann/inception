/*
 * Copyright Marcus Portmann
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
import {COMMA, ENTER} from '@angular/cdk/keycodes';
import {
  ChangeDetectorRef, Component, ElementRef, HostBinding, Input, OnDestroy, OnInit, Optional, Self,
  ViewChild
} from '@angular/core';
import {ControlValueAccessor, NgControl} from '@angular/forms';
import {MatAutocompleteSelectedEvent} from '@angular/material/autocomplete';
import {MatChipGrid} from '@angular/material/chips';
import {MatFormFieldControl} from '@angular/material/form-field';
import {MatInput} from '@angular/material/input';
import {ReplaySubject, Subject, Subscription} from 'rxjs';
import {debounceTime, first, startWith} from 'rxjs/operators';
import {Country} from '../services/country';
import {ReferenceService} from '../services/reference.service';

/**
 * The CountriesChipGridComponent class implements the countries chip list component.
 *
 * @author Marcus Portmann
 */
@Component({
  // eslint-disable-next-line @angular-eslint/component-selector
  selector: 'countries-chip-grid',
  template: `

      <mat-chip-grid #countriesChipGrid>
          <mat-chip-row *ngFor="let country of countries; let index = index;"
                        (removed)="removeCountry(country, index)">
              {{ country.shortName }}
              <button matChipRemove>
                  <mat-icon>cancel</mat-icon>
              </button>
          </mat-chip-row>
          <input
                  #addCountryInput
                  type="text"
                  matInput
                  [matAutocomplete]="addCountryAutocomplete"
                  [matChipInputFor]="countriesChipGrid"
                  (input)="addCountryInputChanged($event)"
                  (focusin)="onFocusIn($event)"
                  (focusout)="onFocusOut($event)">
          <mat-autocomplete
                  #addCountryAutocomplete="matAutocomplete"
                  (optionSelected)="selectCountry($event)">
              <mat-option *ngFor="let filteredCountry of filteredCountries$ | async"
                          [value]="filteredCountry">
                  {{ filteredCountry.shortName }}
              </mat-option>
          </mat-autocomplete>
      </mat-chip-grid>
  `,
  styles: [
    `
    .mat-chip {
      font-weight: normal;
    }
  `
  ],
  providers: [
    {
      provide: MatFormFieldControl,
      useExisting: CountriesChipGridComponent
    }
  ]
})
export class CountriesChipGridComponent implements MatFormFieldControl<string[]>,
  ControlValueAccessor, OnInit, OnDestroy {

  private static nextId: number = 0;

  /**
   * The add country input.
   */
  @ViewChild(MatInput, {static: true}) addCountryInput!: MatInput;

  /**
   * The name for the control type.
   */
  controlType = 'countries-chip-grid';

  /**
   * The countries chip list.
   */
  @ViewChild('countriesChipGrid', {static: true}) countriesChipGrid!: MatChipGrid;

  /**
   * The filtered options for the autocomplete for the add country input.
   */
  filteredCountries$: Subject<Country[]> = new ReplaySubject<Country[]>(1);

  /**
   * Whether the control is focused.
   */
  focused = false;

  /**
   * The ID for the control.
   */
  @HostBinding() id = `countries-chip-list-${CountriesChipGridComponent.nextId++}`;

  /**
   * The reference to the element for the add country input.
   */
  @ViewChild('addCountryInput') inputElementRef!: ElementRef;

  separatorKeysCodes: number[] = [ENTER, COMMA];

  /**
   * The observable indicating that the state of the control has changed.
   */
  stateChanges = new Subject<void>();

  /**
   * Has the control received a touch event.
   */
  touched: boolean = false;

  private _subscriptions: Subscription = new Subscription();

  /**
   * The observable providing access to the value for the add country input as it changes.
   */
  private addCountryInputValue$: Subject<string> = new ReplaySubject<string>(1);

  constructor(private referenceService: ReferenceService,
              @Optional() @Self() public ngControl: NgControl,
              private changeDetectorRef: ChangeDetectorRef) {
    if (this.ngControl != null) {
      /*
       * Setting the value accessor directly (instead of using the providers) to avoid running into
       * a circular import.
       */
      this.ngControl.valueAccessor = this;
    }
  }

  /**
   * The selected countries.
   * @private
   */
  private _countries: Country[] = [];

  /**
   * The selected countries.
   */
  get countries(): Country[] {
    return this._countries;
  }

  /**
   * Whether the control is disabled.
   * @private
   */
  private _disabled = false;

  @Input()
  get disabled(): boolean {
    return this._disabled;
  }

  set disabled(value: boolean) {
    this._disabled = coerceBooleanProperty(value);

    if (this._disabled) {
      this.countriesChipGrid.disabled = true;
      this.addCountryInput.disabled = true;
    }

    this.stateChanges.next();
  }

  /**
   * The placeholder for the add country
   * @private
   */
  private _placeholder: string = '';

  @Input()
  get placeholder() {
    return this._placeholder;
  }

  set placeholder(placeholder) {
    this._placeholder = placeholder;
    this.stateChanges.next();
  }

  /**
   * Whether the control is required.
   * @private
   */
  private _required: boolean = false;

  @Input()
  get required(): boolean {
    return this._required;
  }

  set required(req: any) {
    this._required = coerceBooleanProperty(req);
    this.stateChanges.next();
  }

  /**
   * The ISO 3166-1 alpha-2 codes for the selected countries.
   */
  private _value: string[] = [];

  /**
   * Returns the ISO 3166-1 alpha-2 codes for the selected countries.
   *
   * @return The ISO 3166-1 alpha-2 codes for the selected countries.
   */
  public get value(): string[] | null {
    return this._value;
  }

  /**
   * Set the ISO 3166-1 alpha-2 codes for the selected countries.
   *
   * @param countryCodes the ISO 3166-1 alpha-2 codes for the selected countries
   */
  @Input()
  public set value(countryCodes: string[] | null) {
    if (countryCodes == undefined) {
      countryCodes = null;
    }

    if (this._value !== countryCodes) {
      this.referenceService.getCountries().pipe(first()).subscribe(
        (countries: Map<string, Country>) => {
          this._countries = [];
          this._value = [];

          if (!!countryCodes) {
            for (const countryCode of countryCodes) {
              let country: Country | undefined = countries.get(countryCode);

              if (!!country) {
                this.countries.push(country);
                this._value.push(country.code);
              }
            }
          }

          this._valueChanged(this._value);
        });
    }
  }

  get empty(): boolean {
    return this._value.length === 0;
  }

  get errorState(): boolean {
    return this.required && (this.countries.length == 0) && this.touched;
  }

  get shouldLabelFloat(): boolean {
    return !this.countriesChipGrid.empty || this.addCountryInput.focused;
  }

  addCountryInputChanged(event: Event) {
    if (((event.target as HTMLInputElement).value) !== undefined) {
      this.addCountryInputValue$.next((event.target as HTMLInputElement).value);
    }
  }

  ngOnDestroy(): void {
    this._subscriptions.unsubscribe();
    this.stateChanges.complete();
  }

  ngOnInit(): void {
    // Pass the placeholder to the add country input
    this.addCountryInput.placeholder = this._placeholder;

    // When the countries chip list is touched then we consider ourselves touched
    this.countriesChipGrid.registerOnTouched(() => {
      this.touched = true;
      this.onTouched();
      this.stateChanges.next();
    });

    // Update filtered countries based on the value of add country input
    this.referenceService.getCountries().pipe(first()).subscribe(
      (countries: Map<string, Country>) => {
        this._subscriptions.add(this.addCountryInputValue$.pipe(
          startWith(''),
          debounceTime(500)).subscribe((value: string) => {

          this.filteredCountries$.next(
            this._filterCountries(Array.from(countries.values()), value));
        }));
      });
  }

  onChange: any = (_: any) => {
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
    this.focused = this.countriesChipGrid.focused || this.addCountryInput.focused;
    this.stateChanges.next();
  }

  onTouched: any = () => {
  };

  registerOnChange(fn: any): void {
    this.onChange = fn;
  }

  registerOnTouched(fn: any): void {
    this.onTouched = fn;
  }

  removeCountry(country: Country, index: number): void {
    this.countries.splice(index, 1);
    this._value.splice(index, 1);
    this._valueChanged(this._value);
  }

  selectCountry(event: MatAutocompleteSelectedEvent): void {
    this.countries.push(event.option.value);
    this._value.push((event.option.value as Country).code);
    this._valueChanged(this._value);
  }

  setDescribedByIds(ids: string[]) {
    // TODO: IMPLEMENT THIS IF NECESSARY -- MARCUS
    // https://material.angular.io/guide/creating-a-custom-form-field-control

    // const controlElement = this._elementRef.nativeElement
    // .querySelector('.example-tel-input-container')!;


    // const controlElement = this._elementRef.nativeElement
    // .querySelector('.example-tel-input-container')!;
    // controlElement.setAttribute('aria-describedby', ids.join(' '));
  }

  /**
   * Writes a new value to the control.
   *
   * This method is called by the forms API to write to the view when programmatic changes from
   * model to view are requested.
   *
   * @param value The new value for the control.
   */
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
        if (this._value.includes(country.code)) {
          return false;
        } else {
          return (country.shortName.toLowerCase().indexOf(value) >= 0);
        }
      });
    } else {
      return countries.filter((country: Country) => {
        if (this._value.includes(country.code)) {
          return false;
        } else {
          return true;
        }
      });
    }
  }

  private _resetAddCountry(): void {
    this.addCountryInput.value = '';
    this.addCountryInputValue$.next('');
    if (!!this.inputElementRef) {
      this.inputElementRef.nativeElement.blur();
    }
  }

  private _valueChanged(value: string[] | null) {
    this._resetAddCountry();
    this.onChange(value);
    this.changeDetectorRef.detectChanges();
    this.stateChanges.next();
  }
}
