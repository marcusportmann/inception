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

import { coerceBooleanProperty } from '@angular/cdk/coercion';
import { COMMA, ENTER } from '@angular/cdk/keycodes';
import {
  ChangeDetectorRef, Component, ElementRef, HostBinding, inject, Input, OnDestroy, OnInit, ViewChild
} from '@angular/core';
import { ControlValueAccessor, NgControl } from '@angular/forms';
import { MatAutocompleteSelectedEvent } from '@angular/material/autocomplete';
import { MatChipGrid } from '@angular/material/chips';
import { MatFormFieldControl } from '@angular/material/form-field';
import { MatInput } from '@angular/material/input';
import { CoreModule } from 'ngx-inception/core';
import { ReplaySubject, Subject } from 'rxjs';
import { debounceTime, first, startWith, takeUntil } from 'rxjs/operators';
import { Country } from '../services/country';
import { ReferenceService } from '../services/reference.service';

/**
 * The CountriesChipGridComponent class implements the countries chip grid component.
 *
 * @author Marcus Portmann
 */
@Component({
  selector: 'inception-reference-countries-chip-grid',
  imports: [CoreModule],
  template: `
    <mat-chip-grid #countriesChipGrid>
      @for (country of countries; track trackByCode(index, country); let index = $index) {
        <mat-chip-row (removed)="removeCountry(country, index)">
          {{ country.shortName }}
          <button matChipRemove type="button">
            <mat-icon>cancel</mat-icon>
          </button>
        </mat-chip-row>
      }

      <input
        #addCountryInput
        matInput
        type="text"
        [placeholder]="placeholder"
        [matAutocomplete]="addCountryAutocomplete"
        [matChipInputFor]="countriesChipGrid"
        (input)="addCountryInputChanged($event)"
        (focusin)="onFocusIn($event)"
        (focusout)="onFocusOut($event)" />

      <mat-autocomplete
        #addCountryAutocomplete="matAutocomplete"
        (optionSelected)="selectCountry($event)">
        @for (filteredCountry of filteredCountries$ | async; track filteredCountry) {
          <mat-option [value]="filteredCountry">
            {{ filteredCountry.shortName }}
          </mat-option>
        }
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
export class CountriesChipGridComponent
  implements MatFormFieldControl<string[]>, ControlValueAccessor, OnInit, OnDestroy
{
  private static nextId = 0;

  @ViewChild(MatInput, { static: true }) addCountryInput!: MatInput;

  /** MatFormFieldControl contract */
  controlType = 'countries-chip-grid';

  @ViewChild('countriesChipGrid', { static: true })
  countriesChipGrid!: MatChipGrid;

  @HostBinding('attr.aria-describedby') describedBy = '';

  /** Filtered options for the autocomplete for the addCountryInput. */
  readonly filteredCountries$ = new ReplaySubject<Country[]>(1);

  /** Whether the control is focused. */
  focused = false;

  @HostBinding() id = `countries-chip-list-${CountriesChipGridComponent.nextId++}`;

  @ViewChild('addCountryInput') inputElementRef!: ElementRef<HTMLInputElement>;

  ngControl = inject(NgControl, { optional: true, self: true });

  /** Separator keys for chips (not currently used but kept for completeness). */
  readonly separatorKeysCodes: number[] = [ENTER, COMMA];

  /** Emits when the control state changes (for MatFormFieldControl). */
  readonly stateChanges = new Subject<void>();

  /** Has the control been touched? */
  touched = false;

  /**
   * The observable providing access to the value for the addCountryInput as it changes.
   */
  private readonly addCountryInputValue$ = new ReplaySubject<string>(1);

  private readonly changeDetectorRef = inject(ChangeDetectorRef);

  /** Destroy notifier for subscriptions. */
  private readonly destroy$ = new Subject<void>();

  private readonly referenceService = inject(ReferenceService);

  constructor() {
    if (this.ngControl != null) {
      // Avoid circular dependency with providers
      this.ngControl.valueAccessor = this;
    }
  }

  /**
   * The selected countries.
   */
  private _countries: Country[] = [];
  get countries(): Country[] {
    return this._countries;
  }

  /** Disabled handling */
  private _disabled = false;

  @Input()
  get disabled(): boolean {
    return this._disabled;
  }

  set disabled(value: boolean) {
    this._disabled = coerceBooleanProperty(value);

    // Guard ViewChilds in case disabled is set before view init
    if (this.countriesChipGrid) {
      this.countriesChipGrid.disabled = this._disabled;
    }
    if (this.addCountryInput) {
      this.addCountryInput.disabled = this._disabled;
    }

    this.stateChanges.next();
  }

  /** Placeholder text */
  private _placeholder = '';

  @Input()
  get placeholder(): string {
    return this._placeholder;
  }

  set placeholder(placeholder: string) {
    this._placeholder = placeholder || '';
    this.stateChanges.next();
  }

  /** Required flag */
  private _required = false;

  @Input()
  get required(): boolean {
    return this._required;
  }

  set required(req: boolean | string | null | undefined) {
    this._required = coerceBooleanProperty(req);
    this.stateChanges.next();
  }

  /**
   * The ISO 3166-1 alpha-2 codes for the selected countries.
   */
  private _value: string[] = [];

  get value(): string[] | null {
    return this._value;
  }

  @Input()
  set value(countryCodes: string[] | null) {
    if (countryCodes == null) {
      countryCodes = [];
    }

    // Avoid unnecessary work
    if (this._value === countryCodes) {
      return;
    }

    this.referenceService
      .getCountries()
      .pipe(first(), takeUntil(this.destroy$))
      .subscribe((countries: Map<string, Country>) => {
        this._countries = [];
        this._value = [];

        for (const code of countryCodes!) {
          const country = countries.get(code);
          if (country) {
            this._countries.push(country);
            this._value.push(country.code);
          }
        }

        this._valueChanged(this._value);
      });
  }

  /** MatFormFieldControl: is empty */
  get empty(): boolean {
    return this._value.length === 0;
  }

  /** MatFormFieldControl: error state */
  get errorState(): boolean {
    const controlInvalid = this.ngControl?.invalid ?? false;
    return (
      (controlInvalid && this.touched) ||
      (this.required && this.countries.length === 0 && this.touched)
    );
  }

  /** MatFormFieldControl: label should float */
  get shouldLabelFloat(): boolean {
    const gridNotEmpty = this.countriesChipGrid ? !this.countriesChipGrid.empty : false;
    const inputFocused = this.addCountryInput ? this.addCountryInput.focused : false;
    return gridNotEmpty || inputFocused;
  }

  // UI events
  addCountryInputChanged(event: Event): void {
    const input = event.target as HTMLInputElement | null;
    if (input && input.value !== undefined) {
      this.addCountryInputValue$.next(input.value);
    }
  }

  ngOnDestroy(): void {
    this.stateChanges.complete();
    this.destroy$.next();
    this.destroy$.complete();
  }

  ngOnInit(): void {
    // Initialize filtered countries based on the input value
    this.referenceService
      .getCountries()
      .pipe(first(), takeUntil(this.destroy$))
      .subscribe((countries: Map<string, Country>) => {
        const allCountries = Array.from(countries.values());

        this.addCountryInputValue$
          .pipe(startWith(''), debounceTime(300), takeUntil(this.destroy$))
          .subscribe((value: string) => {
            this.filteredCountries$.next(this._filterCountries(allCountries, value));
          });
      });
  }

  // ControlValueAccessor callbacks
  onChange: (value: string[] | null) => void = () => {
    /* empty */
  };

  // MatFormFieldControl: container click
  onContainerClick(event: MouseEvent): void {
    const target = event.target as HTMLElement | null;
    if (target && target.tagName.toLowerCase() !== 'input') {
      this.addCountryInput?.focus();
    }
  }

  // eslint-disable-next-line @typescript-eslint/no-unused-vars
  onFocusIn(_: FocusEvent): void {
    if (!this.focused) {
      this.focused = true;
      this.stateChanges.next();
    }
  }

  // eslint-disable-next-line @typescript-eslint/no-unused-vars
  onFocusOut(_: FocusEvent): void {
    this.touched = true;
    this.onTouched();
    this.focused =
      (this.countriesChipGrid && this.countriesChipGrid.focused) ||
      (this.addCountryInput && this.addCountryInput.focused) ||
      false;
    this.stateChanges.next();
  }

  onTouched: () => void = () => {
    /* empty */
  };

  // ControlValueAccessor
  registerOnChange(fn: (value: string[] | null) => void): void {
    this.onChange = fn;
  }

  registerOnTouched(fn: () => void): void {
    this.onTouched = fn;
  }

  removeCountry(_country: Country, index: number): void {
    this.countries.splice(index, 1);
    this._value.splice(index, 1);
    this._valueChanged(this._value);
  }

  selectCountry(event: MatAutocompleteSelectedEvent): void {
    const country = event.option.value as Country;

    // Defensive: avoid duplicates in case something slips through filtering
    if (!this._value.includes(country.code)) {
      this.countries.push(country);
      this._value.push(country.code);
      this._valueChanged(this._value);
    }
  }

  // MatFormFieldControl: accessibility
  setDescribedByIds(ids: string[]): void {
    this.describedBy = ids.join(' ');
  }

  setDisabledState(isDisabled: boolean): void {
    this.disabled = isDisabled;
  }

  trackByCode(_: number, country: Country): string {
    return country.code;
  }

  writeValue(value: string[] | string | null): void {
    if (typeof value === 'string') {
      this.value = value ? [value] : [];
    } else if (Array.isArray(value)) {
      this.value = value as string[];
    } else {
      this.value = [];
    }
  }

  // Helpers
  private _filterCountries(countries: Country[], value: string): Country[] {
    const term = (value || '').toLowerCase();

    return countries.filter((country: Country) => {
      // Exclude already-selected countries
      if (this._value.includes(country.code)) {
        return false;
      }

      if (!term) {
        return true;
      }

      return country.shortName.toLowerCase().includes(term);
    });
  }

  private _resetAddCountry(): void {
    if (this.addCountryInput) {
      this.addCountryInput.value = '';
    }
    this.addCountryInputValue$.next('');
    if (this.inputElementRef) {
      this.inputElementRef.nativeElement.blur();
    }
  }

  private _valueChanged(value: string[] | null): void {
    this._resetAddCountry();
    this.onChange(value);
    this.changeDetectorRef.detectChanges();
    this.stateChanges.next();
  }
}
