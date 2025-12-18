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
import { ChangeDetectorRef, Component, HostBinding, Input, OnDestroy, OnInit, ViewChild, inject } from '@angular/core';
import { ControlValueAccessor, NgControl } from '@angular/forms';
import { MatAutocompleteSelectedEvent } from '@angular/material/autocomplete';
import { MatFormFieldControl } from '@angular/material/form-field';
import { MatInput } from '@angular/material/input';
import { AutocompleteSelectionRequiredDirective, CoreModule } from 'ngx-inception/core';
import { BehaviorSubject, ReplaySubject, Subject, Subscription } from 'rxjs';
import { debounceTime, first } from 'rxjs/operators';
import { Country } from '../services/country';
import { ReferenceService } from '../services/reference.service';

/**
 * The CountryInputComponent class implements the country input component.
 *
 * @author Marcus Portmann
 */
@Component({
  selector: 'inception-reference-country-input',
  imports: [CoreModule, AutocompleteSelectionRequiredDirective],
  template: `
    <div matAutocompleteOrigin #origin="matAutocompleteOrigin">
      <input
        #countryInput
        type="text"
        matInput
        autocompleteSelectionRequired
        [required]="required"
        [matAutocomplete]="countryAutocomplete"
        [matAutocompleteConnectedTo]="origin"
        (input)="inputChanged($event)"
        (focusin)="onFocusIn($event)"
        (focusout)="onFocusOut($event)" />
      <mat-autocomplete
        #countryAutocomplete="matAutocomplete"
        (closed)="onClosed()"
        (optionSelected)="optionSelected($event)"
        [displayWith]="displayWith">
        @for (filteredOption of filteredOptions$ | async; track filteredOption) {
          <mat-option
            [value]="filteredOption">
            {{ filteredOption.shortName }}
          </mat-option>
        }
      </mat-autocomplete>
    </div>
    `,
  providers: [
    {
      provide: MatFormFieldControl,
      useExisting: CountryInputComponent
    }
  ]
})
export class CountryInputComponent
  implements MatFormFieldControl<string>, ControlValueAccessor, OnInit, OnDestroy
{
  private readonly referenceService = inject(ReferenceService);
  ngControl = inject(NgControl, { optional: true, self: true });
  private readonly changeDetectorRef = inject(ChangeDetectorRef);

  private static _nextId = 0;

  /**
   * The name for the control type.
   */
  readonly controlType = 'country-input';

  /**
   * The filtered options for the autocomplete.
   */
  readonly filteredOptions$ = new BehaviorSubject<Country[]>([]);

  /**
   * Whether the control is focused.
   */
  focused = false;

  /**
   * The ID for the control.
   */
  @HostBinding() id = `country-input-${CountryInputComponent._nextId++}`;

  /**
   * The country input.
   */
  @ViewChild(MatInput, { static: true }) input!: MatInput;

  /**
   * The observable providing access to the value for the country input as it changes.
   */
  readonly inputValue$ = new ReplaySubject<string>(1);

  /**
   * The observable indicating that the state of the control has changed.
   */
  readonly stateChanges = new Subject<void>();

  /**
   * Has the control received a touch event.
   */
  touched = false;

  /**
   * The options for the autocomplete.
   */
  private _options: Country[] = [];

  private readonly subscriptions = new Subscription();

  constructor() {
    if (this.ngControl) {
      // Avoid circular imports by setting the accessor directly.
      this.ngControl.valueAccessor = this;
    }
  }

  /**
   * Whether the control is disabled.
   */
  private _disabled = false;

  @Input()
  get disabled(): boolean {
    return this._disabled;
  }

  set disabled(value: boolean) {
    this._disabled = coerceBooleanProperty(value);

    if (this.input) {
      this.input.disabled = this._disabled;
    }

    this.stateChanges.next();
  }

  /**
   * The placeholder for the country input.
   */
  private _placeholder = '';

  @Input()
  get placeholder(): string {
    return this._placeholder;
  }

  set placeholder(placeholder: string) {
    this._placeholder = placeholder ?? '';
    if (this.input) {
      this.input.placeholder = this._placeholder;
    }
    this.stateChanges.next();
  }

  /**
   * Whether the control is required.
   */
  private _required = false;

  @Input()
  get required(): boolean {
    return this._required;
  }

  set required(req: boolean | string) {
    this._required = coerceBooleanProperty(req);
    this.stateChanges.next();
  }

  /**
   * The ISO 639-1 alpha-2 code for the selected country.
   */
  private _value: string | null = null;

  /**
   * Returns the ISO 639-1 alpha-2 code for the selected country.
   */
  get value(): string | null {
    return this._value;
  }

  /**
   * Set the ISO 639-1 alpha-2 code for the selected country.
   */
  @Input()
  set value(value: string | null) {
    const newValue = value ?? null;

    if (this._value === newValue) {
      return;
    }

    this._value = null;

    if (newValue) {
      if (this._options.length > 0) {
        const option = this._options.find((o) => o.code === newValue);
        if (option) {
          this.input.value = option.name;
          this._value = newValue;
        }
      } else {
        // Assume value is valid until options are loaded
        this._value = newValue;
      }
    }

    this.onChange(this._value);
    this.changeDetectorRef.detectChanges();
    this.stateChanges.next();
  }

  get empty(): boolean {
    return this._value == null || this._value.length === 0;
  }

  get errorState(): boolean {
    if (this.ngControl && this.ngControl.invalid && this.touched) {
      return true;
    }

    return this.required && (this._value == null || this._value.length === 0) && this.touched;
  }

  @HostBinding('class.floating')
  get shouldLabelFloat(): boolean {
    return this.focused || !this.empty || !!this.input?.focused;
  }

  displayWith(country?: Country | null): string {
    return country ? country.name : '';
  }

  inputChanged(event: Event): void {
    const target = event.target as HTMLInputElement | null;
    const value = target?.value ?? '';
    this.inputValue$.next(value);
  }

  ngOnDestroy(): void {
    this.subscriptions.unsubscribe();
    this.stateChanges.complete();
    this.filteredOptions$.complete();
    this.inputValue$.complete();
  }

  ngOnInit(): void {
    // Initialize the placeholder on the underlying input
    if (this.input) {
      this.input.placeholder = this._placeholder;
      this.input.disabled = this._disabled;
    }

    this.referenceService
      .getCountries()
      .pipe(first())
      .subscribe((countries: Map<string, Country>) => {
        this._options = Array.from(countries.values());
        this.filteredOptions$.next(this._options);

        if (this.value) {
          const option = this._options.find((o) => o.code === this.value);
          if (option) {
            this.input.value = option.name;
            return;
          }

          // The value is invalid, so clear it
          this.value = null;
        }
      });

    this.subscriptions.add(
      this.inputValue$.pipe(debounceTime(250)).subscribe((value: string) => {
        // If a filter is being typed, clear the selected value
        if (this._value) {
          this._value = null;
          this.onChange(this._value);
          this.touched = true;
          this.changeDetectorRef.detectChanges();
          this.stateChanges.next();
        }

        const filterValue = (value ?? '').toLowerCase();

        const filteredOptions =
          filterValue.length === 0
            ? this._options
            : this._options.filter((option) => option.name.toLowerCase().includes(filterValue));

        /*
         * If there are no filtered options, reset the input value and the filtered options.
         * This forces the user to enter a valid filter.
         */
        if (filteredOptions.length === 0) {
          this.input.value = '';
          this.filteredOptions$.next(this._options);
        } else {
          this.filteredOptions$.next(filteredOptions);
        }
      })
    );
  }

  onChange: (value: string | null) => void = () => {
    /* empty */
  };

  onClosed(): void {
    /*
     * If the user entered text in the input to filter the options, but they did not select an
     * option, then the selected value will be null but the input value will be valid.
     * Reset the input and filtered options.
     */
    if (!this._value && this.input.value) {
      this.input.value = '';
      this.filteredOptions$.next(this._options);
    }
  }

  onContainerClick(event: MouseEvent): void {
    if (this.disabled) {
      return;
    }

    if ((event.target as Element).tagName.toLowerCase() !== 'input') {
      this.input.focus();
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
    this.focused = false;
    this.stateChanges.next();
  }

  onTouched: () => void = () => {
    /* empty */
  };

  optionSelected(event: MatAutocompleteSelectedEvent): void {
    const country = event.option.value as Country | null;

    if (country) {
      this.value = country.code;
    } else {
      this.value = null;
    }
  }

  // eslint-disable-next-line @typescript-eslint/no-explicit-any
  registerOnChange(fn: any): void {
    this.onChange = fn;
  }

  // eslint-disable-next-line @typescript-eslint/no-explicit-any
  registerOnTouched(fn: any): void {
    this.onTouched = fn;
  }

  setDescribedByIds(ids: string[]): void {
    // If you want full aria-describedBy support on the host element:
    // this.userAriaDescribedBy = ids.join(' ');
    void ids;
  }

  /**
   * Writes a new value to the control.
   *
   * This method is called by the forms API to write to the view when programmatic changes from
   * model to view are requested.
   */
  writeValue(value: string | null): void {
    if (typeof value === 'string' || value === null) {
      this.value = value;
    }
  }
}
