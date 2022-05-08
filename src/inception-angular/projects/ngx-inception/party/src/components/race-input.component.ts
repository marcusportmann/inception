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

import {coerceBooleanProperty} from '@angular/cdk/coercion';
import {
  ChangeDetectorRef, Component, HostBinding, Input, OnDestroy, OnInit, Optional, Self, ViewChild
} from '@angular/core';
import {ControlValueAccessor, NgControl} from '@angular/forms';
import {MatAutocompleteSelectedEvent} from '@angular/material/autocomplete';
import {MatFormFieldControl} from '@angular/material/form-field';
import {MatInput} from '@angular/material/input';
import {BehaviorSubject, ReplaySubject, Subject, Subscription} from 'rxjs';
import {debounceTime, first} from 'rxjs/operators';
import {PartyReferenceService} from '../services/party-reference.service';
import {Race} from '../services/race';

/**
 * The RaceInputComponent class implements the race input component.
 *
 * @author Marcus Portmann
 */
@Component({
  // eslint-disable-next-line @angular-eslint/component-selector
  selector: 'race-input',
  template: `
    <div matAutocompleteOrigin #origin="matAutocompleteOrigin">
      <input
        #raceInput
        type="text"
        matInput
        autocompleteSelectionRequired
        required="required"
        [matAutocomplete]="raceAutocomplete"
        [matAutocompleteConnectedTo]="origin"
        (input)="inputChanged($event)"
        (focusin)="onFocusIn($event)"
        (focusout)="onFocusOut($event)">
      <mat-autocomplete
        #raceAutocomplete="matAutocomplete"
        (closed)="onClosed()"
        (optionSelected)="optionSelected($event)"
        [displayWith]="displayWith">
        <mat-option
          *ngFor="let filteredOption of filteredOptions$ | async"
          [value]="filteredOption">
          {{ filteredOption.name }}
        </mat-option>
      </mat-autocomplete>
    </div>
  `,
  providers: [
    {
      provide: MatFormFieldControl,
      useExisting: RaceInputComponent
    }
  ]
})
export class RaceInputComponent implements MatFormFieldControl<string>,
  ControlValueAccessor, OnInit, OnDestroy {

  private static _nextId: number = 0;

  /**
   * The name for the control type.
   */
  controlType = 'race-input';

  /**
   * The filtered options for the autocomplete.
   */
  filteredOptions$: BehaviorSubject<Race[]> = new BehaviorSubject<Race[]>([]);

  /**
   * Whether the control is focused.
   */
  focused = false;

  /**
   * The ID for the control.
   */
  @HostBinding() id = `race-input-${RaceInputComponent._nextId++}`;

  /**
   * The input.
   */
  @ViewChild(MatInput, {static: true}) input!: MatInput;

  /**
   * The observable providing access to the value for the input as it changes.
   */
  inputValue$: Subject<string> = new ReplaySubject<string>();

  /**
   * The observable indicating that the state of the control has changed.
   */
  stateChanges = new Subject<void>();

  /**
   * Has the control received a touch event.
   */
  touched: boolean = false;

  //@Input('aria-describedby') userAriaDescribedBy?: string;

  /**
   * The options for the autocomplete.
   */
  private _options: Race[] = [];

  private subscriptions: Subscription = new Subscription();

  constructor(private partyReferenceService: PartyReferenceService,
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
      this.input.disabled = true;
    }

    this.stateChanges.next();
  }

  /**
   * The placeholder for the input.
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
   * The code for the selected race.
   */
  private _value: string | null = null;

  /**
   * Returns the code for the selected race.
   *
   * @return The code for the selected race.
   */
  public get value(): string | null {
    return this._value;
  }

  /**
   * Set the code for the selected race.
   *
   * @param value the code for the selected race
   */
  @Input()
  public set value(value: string | null) {
    if (value == undefined) {
      value = null;
    }

    if (this._value !== value) {
      this._value = null;

      // If the new value is not null
      if (!!value) {
        /*
         * If the options have been loaded, check if the new value is valid by confirming that
         * there is a corresponding option. If the new value is valid, then set the value and set
         * the input value using the name for the option.
         */
        if (this._options.length > 0) {
          for (const option of this._options) {
            if (option.code === value) {
              this.input.value = option.name;
              this._value = value;
              break;
            }
          }
        } else {
          // Assume the new value is valid, it will be checked when the options are loaded
          this._value = value;
        }
      }

      this.onChange(this._value);
      this.changeDetectorRef.detectChanges();
      this.stateChanges.next();
    }
  }

  get empty(): boolean {
    return ((this._value == null) || (this._value.length == 0));
  }

  get errorState(): boolean {
    return this.required && ((this._value == null) || (this._value.length == 0)) && this.touched;
  }

  @HostBinding('class.floating')
  get shouldLabelFloat() {
    return this.focused || !this.empty || this.input.focused;
  }

  displayWith(race: Race): string {
    if (!!race) {
      return race.name;
    } else {
      return '';
    }
  }

  inputChanged(event: Event) {
    if (((event.target as HTMLInputElement).value) !== undefined) {
      this.inputValue$.next((event.target as HTMLInputElement).value);
    }
  }

  ngOnDestroy(): void {
    this.subscriptions.unsubscribe();
    this.stateChanges.complete();
  }

  ngOnInit(): void {
    this.input.placeholder = this._placeholder;

    this.partyReferenceService.getRaces().pipe(first()).subscribe((races: Map<string, Race>) => {
      this._options = Array.from(races.values());

      this.filteredOptions$.next(this._options);

      /*
       * If a value has already been set, attempt to confirm it is valid by finding the
       * corresponding option. If a match is found, use the option's name as the input's value.
       * If we cannot find a corresponding option, i.e. the value is invalid, reset the value.
       */
      if (!!this.value) {
        for (const option of this._options) {
          if (option.code === this.value) {
            this.input.value = option.name;
            return;
          }
        }

        // The value is invalid so clear it
        this.value = null;
      }
    });

    this.subscriptions.add(this.inputValue$.pipe(
      debounceTime(250)).subscribe((value: string) => {
      if (!!this._value) {
        this._value = null;
        this.onChange(this._value);
        // Flag the control as touched to trigger validation
        this.touched = true;
        this.changeDetectorRef.detectChanges();
        this.stateChanges.next();
      }

      value = value.toLowerCase();

      let filteredOptions: Race[] = [];

      for (const option of this._options) {
        if (option.name.toLowerCase().indexOf(value) !== -1) {
          filteredOptions.push(option);
        }
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
    }));
  }

  onChange: any = (_: any) => {
  };

  onClosed(): void {
    /*
     * If the user entered text in the input to filter the options, but they did not select an
     * option, then the selected value will be null but the input value will be valid, i.e. not null
     * or blank. We then need to reset the input value and the filtered options so that if the
     * control is activated again all options are available.
     */
    if (!this._value) {
      if (!!this.input.value) {
        this.input.value = '';
        this.filteredOptions$.next(this._options);
      }
    }
  }

  onContainerClick(event: MouseEvent) {
    if ((event.target as Element).tagName.toLowerCase() != 'input') {
      this.input.focus();
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
    this.focused = false;
    this.stateChanges.next();
  }

  onTouched: any = () => {
  };

  optionSelected(event: MatAutocompleteSelectedEvent): void {
    this.value = event.option.value.code;
  }

  // eslint-disable-next-line @typescript-eslint/no-explicit-any
  registerOnChange(fn: any): void {
    this.onChange = fn;
  }

  // eslint-disable-next-line @typescript-eslint/no-explicit-any
  registerOnTouched(fn: any): void {
    this.onTouched = fn;
  }

  setDescribedByIds(ids: string[]) {
    // TODO: IMPLEMENT THIS IF NECESSARY -- MARCUS
    // https://material.angular.io/guide/creating-a-custom-form-field-control

    // const controlElement = this._elementRef.nativeElement
    // .querySelector('.example-tel-input-container')!;

    // const controlElement = this._elementRef.nativeElement
    // .querySelector('.example-tel-input-container')!;
    // controlElement.setEmployment('aria-describedby', ids.join(' '));
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
    if (typeof value === 'string') {
      this.value = value as string;
    }
  }
}

