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
  ChangeDetectionStrategy, ChangeDetectorRef, Component, HostBinding, inject, Input, OnDestroy,
  OnInit, ViewChild
} from '@angular/core';
import { ControlValueAccessor, NgControl } from '@angular/forms';
import { MatAutocompleteSelectedEvent } from '@angular/material/autocomplete';
import { MatFormFieldControl } from '@angular/material/form-field';
import { MatInput } from '@angular/material/input';
import { AutocompleteSelectionRequiredDirective, CoreModule } from 'ngx-inception/core';
import { BehaviorSubject, ReplaySubject, Subject, Subscription } from 'rxjs';
import { debounceTime, first } from 'rxjs/operators';
import { Language } from '../services/language';
import { ReferenceService } from '../services/reference.service';

/**
 * The LanguageInputComponent class implements the language input component.
 *
 * @author Marcus Portmann
 */
@Component({
  selector: 'inception-reference-language-input',
  imports: [CoreModule, AutocompleteSelectionRequiredDirective],
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <div matAutocompleteOrigin #origin="matAutocompleteOrigin">
      <input
        #languageInput
        type="text"
        matInput
        autocompleteSelectionRequired
        [required]="required"
        [placeholder]="placeholder"
        [matAutocomplete]="languageAutocomplete"
        [matAutocompleteConnectedTo]="origin"
        (input)="inputChanged($event)"
        (focusin)="onFocusIn($event)"
        (focusout)="onFocusOut($event)" />
      <mat-autocomplete
        #languageAutocomplete="matAutocomplete"
        (closed)="onClosed()"
        (optionSelected)="optionSelected($event)"
        [displayWith]="displayWith">
        @for (filteredOption of filteredOptions$ | async; track filteredOption) {
          <mat-option [value]="filteredOption">
            {{ filteredOption.shortName }}
          </mat-option>
        }
      </mat-autocomplete>
    </div>
  `,
  providers: [
    {
      provide: MatFormFieldControl,
      useExisting: LanguageInputComponent
    }
  ]
})
export class LanguageInputComponent
  implements MatFormFieldControl<string>, ControlValueAccessor, OnInit, OnDestroy
{
  private static _nextId = 0;

  /**
   * The name for the control type.
   */
  readonly controlType = 'language-input';

  /**
   * The filtered options for the autocomplete.
   */
  readonly filteredOptions$ = new BehaviorSubject<Language[]>([]);

  /**
   * Whether the control is focused.
   */
  focused = false;

  /**
   * The ID for the control.
   */
  @HostBinding() id = `language-input-${LanguageInputComponent._nextId++}`;

  /**
   * The language input.
   */
  @ViewChild(MatInput, { static: true }) input!: MatInput;

  readonly ngControl = inject(NgControl, { optional: true, self: true });

  /**
   * The observable indicating that the state of the control has changed.
   */
  readonly stateChanges = new Subject<void>();

  /**
   * Has the control received a touch event?
   */
  touched = false;

  /**
   * The options for the language.
   */
  private _options: Language[] = [];

  private readonly cdr = inject(ChangeDetectorRef);

  /**
   * The observable providing access to the value for the language input as it changes.
   */
  private readonly inputValue$ = new ReplaySubject<string>(1);

  private readonly referenceService = inject(ReferenceService);

  private readonly subscriptions = new Subscription();

  constructor() {
    if (this.ngControl) {
      // Avoid circular DI by setting the accessor directly.
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
    this.cdr.markForCheck();
  }

  /**
   * The placeholder for the language input.
   */
  private _placeholder = '';

  @Input()
  get placeholder(): string {
    return this._placeholder;
  }

  set placeholder(placeholder: string) {
    this._placeholder = placeholder ?? '';
    this.stateChanges.next();
    this.cdr.markForCheck();
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
    this.cdr.markForCheck();
  }

  /**
   * The ISO 639-1 alpha-2 code for the selected language.
   */
  private _value: string | null = null;

  /**
   * Returns the ISO 639-1 alpha-2 code for the selected language.
   */
  get value(): string | null {
    return this._value;
  }

  /**
   * Set the ISO 639-1 alpha-2 code for the selected language.
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
        const match = this._options.find((o) => o.code === newValue);

        if (match) {
          if (this.input) {
            this.input.value = match.name;
          }
          this._value = newValue;
        }
      } else {
        // Assume valid; verify when options are loaded.
        this._value = newValue;
      }
    }

    this.onChange(this._value);
    this.stateChanges.next();
    this.cdr.markForCheck();
  }

  get empty(): boolean {
    return this._value == null || this._value.length === 0;
  }

  get errorState(): boolean {
    const controlInvalid = !!this.ngControl && !!this.ngControl.invalid && !!this.ngControl.touched;

    const requiredInvalid =
      this.required && (this._value == null || this._value.length === 0) && this.touched;

    return controlInvalid || requiredInvalid;
  }

  @HostBinding('class.floating')
  get shouldLabelFloat(): boolean {
    return this.focused || !this.empty || !!this.input?.focused;
  }

  displayWith(language: Language | null): string {
    return language?.name ?? '';
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
    // Load languages
    const languagesSub = this.referenceService
      .getLanguages()
      .pipe(first())
      .subscribe((languages: Map<string, Language>) => {
        this._options = Array.from(languages.values());

        this.filteredOptions$.next(this._options);

        // If a value has already been set, confirm it's valid and update the input display.
        if (this.value) {
          const match = this._options.find((o) => o.code === this.value);

          if (match) {
            if (this.input) {
              this.input.value = match.name;
            }
          } else {
            // Invalid value; clear it
            this.value = null;
          }
        }

        this.cdr.markForCheck();
      });

    this.subscriptions.add(languagesSub);

    // Filter handling
    const filterSub = this.inputValue$.pipe(debounceTime(250)).subscribe((value: string) => {
      // If a new value is being typed, clear the selected code.
      if (this._value) {
        this._value = null;
        this.onChange(this._value);
        this.touched = true;
        this.stateChanges.next();
      }

      const filterValue = value.toLowerCase().trim();
      let filteredOptions = this._options;

      if (filterValue.length > 0) {
        filteredOptions = this._options.filter((option) =>
          option.name.toLowerCase().includes(filterValue)
        );
      }

      /*
       * If there are no filtered options, reset the input value and fall back to all options.
       * This forces the user to enter a valid filter.
       */
      if (filteredOptions.length === 0) {
        if (this.input) {
          this.input.value = '';
        }
        filteredOptions = this._options;
      }

      this.filteredOptions$.next(filteredOptions);
      this.cdr.markForCheck();
    });

    this.subscriptions.add(filterSub);
  }

  onClosed(): void {
    /*
     * If the user entered the text but did not select an option, then reset the input value and
     * the filtered options.
     */
    if (!this._value && this.input && this.input.value) {
      this.input.value = '';
      this.filteredOptions$.next(this._options);
      this.cdr.markForCheck();
    }
  }

  onContainerClick(event: MouseEvent): void {
    if ((event.target as Element).tagName.toLowerCase() !== 'input') {
      this.input?.focus();
    }
  }

  // eslint-disable-next-line @typescript-eslint/no-unused-vars
  onFocusIn(_: FocusEvent): void {
    if (!this.focused) {
      this.focused = true;
      this.stateChanges.next();
      this.cdr.markForCheck();
    }
  }

  // eslint-disable-next-line @typescript-eslint/no-unused-vars
  onFocusOut(_: FocusEvent): void {
    this.touched = true;
    this.onTouched();
    this.focused = false;
    this.stateChanges.next();
    this.cdr.markForCheck();
  }

  optionSelected(event: MatAutocompleteSelectedEvent): void {
    const language: Language | null = event.option.value ?? null;
    this.value = language ? language.code : null;
  }

  registerOnChange(fn: (value: string | null) => void): void {
    this.onChange = fn;
  }

  registerOnTouched(fn: () => void): void {
    this.onTouched = fn;
  }

  // eslint-disable-next-line @typescript-eslint/no-unused-vars
  setDescribedByIds(_ids: string[]): void {
    // Implement if necessary for accessibility:
    // controlElement.setAttribute('aria-describedby', ids.join(' '));
  }

  /**
   * Writes a new value to the control.
   *
   * This method is called by the forms API to write to the view when programmatic changes from
   * model to view are requested.
   */
  writeValue(value: unknown): void {
    if (typeof value === 'string') {
      this.value = value;
    } else if (value == null) {
      this.value = null;
    }
  }

  private onChange: (value: string | null) => void = () => {
    /* empty */
  };

  private onTouched: () => void = () => {
    /* empty */
  };
}
