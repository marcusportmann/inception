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
import { ChangeDetectionStrategy, ChangeDetectorRef, Component, HostBinding, Input, OnDestroy, OnInit, ViewChild, inject } from '@angular/core';
import { ControlValueAccessor, NgControl } from '@angular/forms';
import { MatAutocompleteSelectedEvent } from '@angular/material/autocomplete';
import { MatFormFieldControl } from '@angular/material/form-field';
import { MatInput } from '@angular/material/input';
import { AutocompleteSelectionRequiredDirective, CoreModule } from 'ngx-inception/core';
import { BehaviorSubject, ReplaySubject, Subject } from 'rxjs';
import {
  debounceTime, distinctUntilChanged, first, map, switchMap, takeUntil
} from 'rxjs/operators';
import { ReferenceService } from '../services/reference.service';
import { Region } from '../services/region';

/**
 * The RegionInputComponent class implements the region input component.
 *
 * @author Marcus Portmann
 */
@Component({
  selector: 'inception-reference-region-input',
  imports: [CoreModule, AutocompleteSelectionRequiredDirective],
  template: `
    <div matAutocompleteOrigin #origin="matAutocompleteOrigin">
      <input
        #input
        type="text"
        matInput
        autocompleteSelectionRequired
        [required]="required"
        [matAutocomplete]="regionAutocomplete"
        [matAutocompleteConnectedTo]="origin"
        (input)="inputChanged($event)"
        (focusin)="onFocusIn($event)"
        (focusout)="onFocusOut($event)" />
      <mat-autocomplete
        #regionAutocomplete="matAutocomplete"
        (closed)="onClosed()"
        (optionSelected)="optionSelected($event)"
        [displayWith]="displayWith">
        @for (filteredOption of filteredOptions$ | async; track filteredOption) {
          <mat-option
            [value]="filteredOption">
            {{ filteredOption.name }}
          </mat-option>
        }
      </mat-autocomplete>
    </div>
    `,
  providers: [
    {
      provide: MatFormFieldControl,
      useExisting: RegionInputComponent
    }
  ],
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class RegionInputComponent
  implements MatFormFieldControl<string>, ControlValueAccessor, OnInit, OnDestroy
{
  private readonly referenceService = inject(ReferenceService);
  readonly ngControl = inject(NgControl, { optional: true, self: true });
  private readonly changeDetectorRef = inject(ChangeDetectorRef);

  private static _nextId = 0;

  /** The name for the control type. */
  controlType = 'region-input';

  /** ARIA described-by ids set by the form-field. */
  @HostBinding('attr.aria-describedby') describedBy = '';

  /** The filtered options for the autocomplete. */
  readonly filteredOptions$ = new BehaviorSubject<Region[]>([]);

  /** Whether the control is focused. */
  focused = false;

  /** The ID for the control. */
  @HostBinding() id = `region-input-${RegionInputComponent._nextId++}`;

  /** The input. */
  @ViewChild(MatInput, { static: true }) input!: MatInput;

  /** The observable providing access to the value for the region input as it changes. */
  readonly inputValue$ = new ReplaySubject<string>(1);

  /** The observable indicating that the state of the control has changed. */
  readonly stateChanges = new Subject<void>();

  /** Has the control received a touch event. */
  touched = false;

  /** The options for the autocomplete. */
  private _options: Region[] = [];

  /** The ISO 3166-1 alpha-2 code for the country to retrieve the regions for. */
  private readonly country$ = new BehaviorSubject<string | null>(null);

  /** Emits when the component is destroyed. */
  private readonly destroy$ = new Subject<void>();

  constructor() {
    if (this.ngControl != null) {
      // Setting the value accessor directly to avoid circular imports.
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
    const newDisabled = coerceBooleanProperty(value);

    if (newDisabled === this._disabled) {
      return;
    }

    this._disabled = newDisabled;

    if (this.input) {
      this.input.disabled = this._disabled;
    }

    this.stateChanges.next();
    this.changeDetectorRef.markForCheck();
  }

  /** The placeholder for the region input. */
  private _placeholder = '';

  @Input()
  get placeholder(): string {
    return this._placeholder;
  }

  set placeholder(placeholder: string) {
    this._placeholder = placeholder ?? '';

    // keep the native input in sync if it's already available
    if (this.input) {
      this.input.placeholder = this._placeholder;
    }

    this.stateChanges.next();
    this.changeDetectorRef.markForCheck();
  }

  /** Whether the control is required. */
  private _required = false;

  @Input()
  get required(): boolean {
    return this._required;
  }

  set required(req: boolean | string) {
    this._required = coerceBooleanProperty(req);
    this.stateChanges.next();
    this.changeDetectorRef.markForCheck();
  }

  /** The code for the selected region. */
  private _value: string | null = null;

  /** Returns the code for the selected region. */
  get value(): string | null {
    return this._value;
  }

  /** Set the code for the selected region. */
  @Input()
  set value(value: string | null) {
    const normalized = value ?? null;

    if (this._value === normalized) {
      return;
    }

    this._value = null;

    if (normalized) {
      if (this._options.length > 0) {
        const match = this._options.find((option) => option.code === normalized);
        if (match) {
          this._value = normalized;
          this.input.value = match.name;
        }
      } else {
        // Assume the new value is valid; it will be checked when the options are loaded.
        this._value = normalized;
      }
    }

    this.onChange(this._value);
    this.stateChanges.next();
    this.changeDetectorRef.markForCheck();
  }

  /** The ISO 3166-1 alpha-2 code for the country to retrieve the regions for. */
  @Input()
  get country(): string | null {
    return this.country$.value;
  }

  set country(country: string | null) {
    const normalized = country ?? null;
    if (normalized !== this.country$.value) {
      this.country$.next(normalized);
    }
  }

  get empty(): boolean {
    return !this._value || this._value.length === 0;
  }

  get errorState(): boolean {
    if (this.ngControl && this.ngControl.invalid && this.touched) {
      return true;
    }
    return this.required && this.empty && this.touched;
  }

  @HostBinding('class.floating')
  get shouldLabelFloat(): boolean {
    return this.focused || !this.empty || (this.input && this.input.focused);
  }

  displayWith(region?: Region | null): string {
    return region?.name ?? '';
  }

  inputChanged(event: Event): void {
    const target = event.target as HTMLInputElement | null;
    if (target && target.value !== undefined) {
      this.inputValue$.next(target.value);
    }
  }

  ngOnDestroy(): void {
    this.destroy$.next();
    this.destroy$.complete();

    this.filteredOptions$.complete();
    this.stateChanges.complete();
    this.inputValue$.complete();
  }

  ngOnInit(): void {
    if (this.input) {
      this.input.placeholder = this._placeholder;
      this.input.disabled = this._disabled;
    }

    // Load & filter regions when the country changes.
    this.country$
      .pipe(
        distinctUntilChanged(),
        switchMap((country) =>
          this.referenceService.getRegions().pipe(
            first(),
            map((regions: Map<string, Region>) => {
              const options: Region[] = [];
              for (const region of regions.values()) {
                if (!country || region.country === country) {
                  options.push(region);
                }
              }
              return options;
            })
          )
        ),
        takeUntil(this.destroy$)
      )
      .subscribe((options: Region[]) => {
        this._options = options;
        this.filteredOptions$.next(options);
        this.syncValueWithOptions();
        this.stateChanges.next();
        this.changeDetectorRef.markForCheck();
      });

    // Filter options as the user types.
    this.inputValue$
      .pipe(debounceTime(250), takeUntil(this.destroy$))
      .subscribe((value: string) => {
        if (this._value) {
          this._value = null;
          this.onChange(this._value);
          this.touched = true;
        }

        const filter = value.toLowerCase().trim();
        let filteredOptions = this._options.filter((option) =>
          option.name.toLowerCase().includes(filter)
        );

        // No filtered options -> clear input and show all, forcing the user to refine.
        if (filteredOptions.length === 0) {
          this.input.value = '';
          filteredOptions = this._options;
        }

        this.filteredOptions$.next(filteredOptions);
        this.stateChanges.next();
        this.changeDetectorRef.markForCheck();
      });
  }

  // eslint-disable-next-line @typescript-eslint/no-explicit-any
  onChange: (value: any) => void = () => {
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
      this.stateChanges.next();
      this.changeDetectorRef.markForCheck();
    }
  }

  onContainerClick(event: MouseEvent): void {
    const target = event.target as Element | null;
    if (target && target.tagName.toLowerCase() !== 'input') {
      this.input.focus();
    }
  }

  // eslint-disable-next-line @typescript-eslint/no-unused-vars
  onFocusIn(_event: FocusEvent): void {
    if (!this.focused) {
      this.focused = true;
      this.stateChanges.next();
      this.changeDetectorRef.markForCheck();
    }
  }

  // eslint-disable-next-line @typescript-eslint/no-unused-vars
  onFocusOut(_event: FocusEvent): void {
    this.touched = true;
    this.onTouched();
    this.focused = false;
    this.stateChanges.next();
    this.changeDetectorRef.markForCheck();
  }

  onTouched: () => void = () => {
    /* empty */
  };

  optionSelected(event: MatAutocompleteSelectedEvent): void {
    const region: Region | null = event.option.value;
    this.value = region ? region.code : null;
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

  setDisabledState(isDisabled: boolean): void {
    this.disabled = isDisabled;
  }

  // eslint-disable-next-line @typescript-eslint/no-explicit-any
  writeValue(value: any): void {
    if (typeof value === 'string' || value === null || value === undefined) {
      this.value = value ?? null;
    }
  }

  private syncValueWithOptions(): void {
    if (!this._value) {
      return;
    }

    const match = this._options.find((option) => option.code === this._value);

    if (match) {
      this.input.value = match.name;
    } else {
      // invalid value -> reset
      this.value = null;
    }
  }
}
