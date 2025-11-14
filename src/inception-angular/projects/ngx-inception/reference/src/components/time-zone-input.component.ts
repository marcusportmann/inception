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
import {
  ChangeDetectorRef, Component, HostBinding, Input, OnDestroy, OnInit, Optional, Self, ViewChild
} from '@angular/core';
import { ControlValueAccessor, NgControl } from '@angular/forms';
import { MatAutocompleteSelectedEvent } from '@angular/material/autocomplete';
import { MatFormFieldControl } from '@angular/material/form-field';
import { MatInput } from '@angular/material/input';
import { AutocompleteSelectionRequiredDirective, CoreModule } from 'ngx-inception/core';
import { BehaviorSubject, ReplaySubject, Subject } from 'rxjs';
import { debounceTime, distinctUntilChanged, first, takeUntil } from 'rxjs/operators';
import { ReferenceService } from '../services/reference.service';
import { TimeZone } from '../services/time-zone';

/**
 * The TimeZoneInputComponent class implements the time zone input component.
 *
 * @author Marcus Portmann
 */
@Component({
  selector: 'inception-reference-time-zone-input',
  imports: [CoreModule, AutocompleteSelectionRequiredDirective],
  template: `
    <div matAutocompleteOrigin #origin="matAutocompleteOrigin">
      <input
        #timeZoneInput
        type="text"
        matInput
        autocompleteSelectionRequired
        [required]="required"
        [matAutocomplete]="timeZoneAutocomplete"
        [matAutocompleteConnectedTo]="origin"
        (input)="inputChanged($event)"
        (focusin)="onFocusIn($event)"
        (focusout)="onFocusOut($event)" />
      <mat-autocomplete
        #timeZoneAutocomplete="matAutocomplete"
        (closed)="onClosed()"
        (optionSelected)="optionSelected($event)"
        [displayWith]="displayWith">
        <mat-option
          *ngFor="let filteredOption of filteredOptions$ | async"
          [value]="filteredOption">
          {{ filteredOption.id }}
        </mat-option>
      </mat-autocomplete>
    </div>
  `,
  providers: [
    {
      provide: MatFormFieldControl,
      useExisting: TimeZoneInputComponent
    }
  ]
})
export class TimeZoneInputComponent
  implements MatFormFieldControl<string | null>, ControlValueAccessor, OnInit, OnDestroy
{
  private static _nextId = 0;

  /** Name for the control type. */
  readonly controlType = 'time-zone-input';

  /** For accessibility: IDs of elements that describe this control. */
  @HostBinding('attr.aria-describedby') describedBy = '';

  /** The filtered options for the autocomplete. */
  readonly filteredOptions$ = new BehaviorSubject<TimeZone[]>([]);

  /** Whether the control is focused. */
  focused = false;

  /** The ID for the control. */
  @HostBinding() id = `time-zone-input-${TimeZoneInputComponent._nextId++}`;

  /** The input. */
  @ViewChild(MatInput, { static: true }) input!: MatInput;

  /** The observable indicating that the state of the control has changed. */
  readonly stateChanges = new Subject<void>();

  /** Has the control received a touch event. */
  touched = false;

  /** The options for the autocomplete. */
  private _options: TimeZone[] = [];

  /** Destroy notifier for subscriptions. */
  private readonly destroy$ = new Subject<void>();

  /** The observable providing access to the value for the input as it changes. */
  private readonly inputValue$ = new ReplaySubject<string>(1);

  constructor(
    private readonly referenceService: ReferenceService,
    @Optional() @Self() public readonly ngControl: NgControl | null,
    private readonly changeDetectorRef: ChangeDetectorRef
  ) {
    if (this.ngControl) {
      // Avoid circular imports from using providers
      this.ngControl.valueAccessor = this;
    }
  }

  /** Whether the control is disabled. */
  private _disabled = false;

  @Input()
  get disabled(): boolean {
    return this._disabled;
  }
  set disabled(value: boolean) {
    const coerced = coerceBooleanProperty(value);
    if (coerced === this._disabled) {
      return;
    }

    this._disabled = coerced;

    if (this.input) {
      this.input.disabled = this._disabled;
    }

    this.stateChanges.next();
  }

  /** The placeholder for the input. */
  private _placeholder = '';

  @Input()
  get placeholder(): string {
    return this._placeholder;
  }
  set placeholder(placeholder: string) {
    if (placeholder === this._placeholder) {
      return;
    }

    this._placeholder = placeholder;

    // MatInput exists by the time setters are typically hit,
    // but we still guard to be safe in tests / edge cases.
    if (this.input) {
      this.input.placeholder = this._placeholder;
    }

    this.stateChanges.next();
  }

  /** Whether the control is required. */
  private _required = false;

  @Input()
  get required(): boolean {
    return this._required;
  }
  set required(req: unknown) {
    const coerced = coerceBooleanProperty(req);
    if (coerced === this._required) {
      return;
    }

    this._required = coerced;
    this.stateChanges.next();
  }

  /** The ID for the selected time zone. */
  private _value: string | null = null;

  /**
   * Returns the ID for the selected time zone.
   *
   * @return The ID for the selected time zone.
   */
  get value(): string | null {
    return this._value;
  }

  /**
   * Set the ID for the selected time zone.
   *
   * @param value the ID for the selected time zone
   */
  @Input()
  set value(value: string | null) {
    if (value === undefined) {
      value = null;
    }

    if (this._value === value) {
      return;
    }

    this._value = null;

    if (value) {
      // If options are already loaded, validate and set input text
      if (this._options.length > 0) {
        const option = this._options.find((o) => o.id === value);
        if (option) {
          this.input.value = option.id;
          this._value = value;
        }
      } else {
        // Assume the new value is valid, it will be checked when the options are loaded
        this._value = value;
      }
    }

    this.onChange(this._value);
    this.changeDetectorRef.markForCheck();
    this.stateChanges.next();
  }

  // Optional ARIA invalid binding (nice for a11y, but not strictly required)
  @HostBinding('attr.aria-invalid')
  get ariaInvalid(): boolean {
    return this.errorState;
  }

  get empty(): boolean {
    return !this._value || this._value.length === 0;
  }

  get errorState(): boolean {
    const requiredError = this.required && (!this._value || this._value.length === 0);
    const controlInvalid = !!this.ngControl?.invalid && this.touched;

    return controlInvalid || (requiredError && this.touched);
  }

  @HostBinding('class.floating')
  get shouldLabelFloat(): boolean {
    return this.focused || !this.empty || (this.input?.focused ?? false);
  }

  displayWith(timeZone: TimeZone | null): string {
    return timeZone ? timeZone.id : '';
  }

  inputChanged(event: Event): void {
    const value = (event.target as HTMLInputElement).value ?? '';
    this.inputValue$.next(value);
  }

  ngOnDestroy(): void {
    this.destroy$.next();
    this.destroy$.complete();
    this.stateChanges.complete();
  }

  ngOnInit(): void {
    // Keep the MatInput placeholder in sync with our property.
    this.input.placeholder = this._placeholder;

    // Load time zones.
    this.referenceService
      .getTimeZones()
      .pipe(first())
      .subscribe((timeZones: Map<string, TimeZone>) => {
        this._options = Array.from(timeZones.values());
        this.filteredOptions$.next(this._options);

        // If a value has already been set, confirm it is valid.
        if (this.value) {
          const option = this._options.find((o) => o.id === this.value);
          if (option) {
            this.input.value = option.id;
          } else {
            // The value is invalid, so clear it
            this.value = null;
          }
        }
      });

    // React to the user typing into the input
    this.inputValue$
      .pipe(debounceTime(250), distinctUntilChanged(), takeUntil(this.destroy$))
      .subscribe((value) => this.handleUserInput(value));
  }

  // eslint-disable-next-line @typescript-eslint/no-explicit-any
  onChange: any = () => {
    /* empty */
  };

  onClosed(): void {
    /*
     * If the user entered text in the input to filter the options, but they did not select one of
     * the filtered options, then the selected value will be null. However, the input value will be
     * valid, i.e., not null or blank. We then need to reset the input value and the filtered
     * options so that if the control is activated again, all options are available.
     */
    if (!this._value && this.input.value) {
      this.input.value = '';
      this.filteredOptions$.next(this._options);
    }
  }

  onContainerClick(event: MouseEvent): void {
    if ((event.target as Element).tagName.toLowerCase() !== 'input') {
      this.input.focus();
    }
  }

  // eslint-disable-next-line @typescript-eslint/no-unused-vars
  onFocusIn(_event: Event): void {
    if (!this.focused) {
      this.focused = true;
      this.stateChanges.next();
    }
  }

  // eslint-disable-next-line @typescript-eslint/no-unused-vars
  onFocusOut(_event: Event): void {
    this.touched = true;
    this.onTouched();
    this.focused = false;
    this.stateChanges.next();
  }

  // eslint-disable-next-line @typescript-eslint/no-explicit-any
  onTouched: any = () => {
    /* empty */
  };

  optionSelected(event: MatAutocompleteSelectedEvent): void {
    this.value = event.option.value.id;
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
    this.describedBy = ids.join(' ');
  }

  /**
   * ControlValueAccessor hook to update the disabled state from forms API.
   */
  setDisabledState(isDisabled: boolean): void {
    this.disabled = isDisabled;
  }

  /**
   * Writes a new value to the control.
   *
   * This method is called by the forms API to write to the view when programmatic changes from
   * model to view are requested.
   *
   * @param value The new value for the control.
   */
  // eslint-disable-next-line @typescript-eslint/no-explicit-any
  writeValue(value: any): void {
    if (typeof value === 'string' || value === null || value === undefined) {
      this.value = value ?? null;
    }
  }

  private handleUserInput(value: string): void {
    // Reset the selected value when the user types
    if (this._value) {
      this._value = null;
      this.onChange(this._value);

      // Flag the control as touched to trigger validation
      this.touched = true;
      this.changeDetectorRef.markForCheck();
      this.stateChanges.next();
    }

    const lowerValue = value.trim().toLowerCase();

    let filteredOptions = this._options;

    if (lowerValue) {
      filteredOptions = this._options.filter((option) =>
        option.id.toLowerCase().includes(lowerValue)
      );
    }

    /*
     * If there are no filtered options, as a result of there being no options at all or no
     * options matching the filter specified by the user, then reset the input value and the
     * filtered options. This has the effect of forcing the user to enter a valid filter.
     */
    if (filteredOptions.length === 0) {
      this.input.value = '';
      filteredOptions = this._options;
    }

    this.filteredOptions$.next(filteredOptions);
  }
}
