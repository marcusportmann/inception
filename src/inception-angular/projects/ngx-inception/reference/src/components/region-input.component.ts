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
  ChangeDetectorRef, Component, ElementRef, HostBinding, Input, OnDestroy, OnInit, Optional, Self,
  ViewChild
} from '@angular/core';
import {ControlValueAccessor, NgControl} from '@angular/forms';
import {MatAutocomplete, MatAutocompleteSelectedEvent} from '@angular/material/autocomplete';
import {MatFormFieldControl} from '@angular/material/form-field';
import {MatInput} from '@angular/material/input';
import {
  BehaviorSubject, combineLatest, ReplaySubject, Subject, Subscription, throttleTime
} from 'rxjs';
import {debounceTime, first, map} from 'rxjs/operators';
import {ReferenceService} from '../services/reference.service';
import {Region} from '../services/region';

/**
 * The RegionInputComponent class implements the region input component.
 *
 * @author Marcus Portmann
 */
@Component({
  // eslint-disable-next-line @angular-eslint/component-selector
  selector: 'region-input',
  template: `
    <div matAutocompleteOrigin #origin="matAutocompleteOrigin">
      <input
        #regionInput
        type="text"
        matInput
        autocompleteSelectionRequired
        required="required"
        [matAutocomplete]="regionAutocomplete"
        [matAutocompleteConnectedTo]="origin"
        (input)="inputChanged($event)"
        (focusin)="onFocusIn($event)"
        (focusout)="onFocusOut($event)">
      <mat-autocomplete
        #regionAutocomplete="matAutocomplete"
        (optionSelected)="optionSelected($event)"
        [displayWith]="displayWith">
        <mat-option
          *ngFor="let region of filteredOptions$ | async"
          [value]="region">
          {{ region.name }}
        </mat-option>
      </mat-autocomplete>
    </div>
  `,
  providers: [
    {
      provide: MatFormFieldControl,
      useExisting: RegionInputComponent
    }
  ]
})
export class RegionInputComponent implements MatFormFieldControl<string>,
  ControlValueAccessor, OnInit, OnDestroy {

  private static _nextId: number = 0;

  /**
   * The name for the control type.
   */
  controlType = 'region-input';

  /**
   * The filtered options for the autocomplete.
   */
  filteredOptions$: BehaviorSubject<Region[]> = new BehaviorSubject<Region[]>([]);

  /**
   * Whether the control is focused.
   */
  focused = false;

  /**
   * The ID for the control.
   */
  @HostBinding() id = `region-input-${RegionInputComponent._nextId++}`;


  /**
   * The input.
   */
  @ViewChild(MatInput, {static: true}) input!: MatInput;

  /**
   * The observable providing access to the value for the region input as it changes.
   */
  regionInputValue$: Subject<string> = new ReplaySubject<string>();

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
   * The regions for the country.
   */
  private _regions: Region[] = [];

  /**
   * The ISO 639-1 alpha-2 code for the country to retrieve the regions for.
   */
  private country$: BehaviorSubject<string | null> = new BehaviorSubject<string | null>(null);

  private subscriptions: Subscription = new Subscription();

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
   * The placeholder for the region input.
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
   * The code for the selected region.
   */
  private _value: string | null = null;

  /**
   * Returns the code for the selected region.
   *
   * @return The code for the selected region.
   */
  public get value(): string | null {
    return this._value;
  }

  /**
   * Set the code for the selected region.
   *
   * @param value the code for the selected region
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
        // If options have been loaded, check if the new value is valid.
        if (this._regions.length > 0) {
          for (const region of this._regions) {
            if (region.code === value) {
              console.log('Setting the validated value ' + value);
              this.input.value = region.name;
              this._value = value;
              break;
            }
          }
        } else {
          console.log('Setting the unvalidated value to ' + value);

          // Assume the new value is valid, it will be checked when the options are loaded
          this._value = value;
        }
      }

      //this.regionInput.value = '';

      this.onChange(this._value);
      this.changeDetectorRef.detectChanges();
      this.stateChanges.next();
    }
  }

  /**
   * The ISO 639-1 alpha-2 code for the country to retrieve the regions for.
   */
  @Input() get country(): string | null {
    return this.country$.value;
  }

  set country(country: string | null) {
    if (country == undefined) {
      country = null;
    }

    if (country !== this.country$.value) {
      this.country$.next(country);
    }
  }

  get empty(): boolean {
    return ((this._value == null) || (this._value.length == 0));
  }

  get errorState(): boolean {
    return this.required && this.empty && this.touched;
  }

  @HostBinding('class.floating')
  get shouldLabelFloat() {
    return this.focused || !this.empty || this.input.focused;
  }

  displayWith(region: Region): string {
    if (!!region) {
      return region.name;
    } else {
      return '';
    }
  }

  inputChanged(event: Event) {
    if (((event.target as HTMLInputElement).value) !== undefined) {
      this.regionInputValue$.next((event.target as HTMLInputElement).value);
    }
  }

  ngOnDestroy(): void {
    this.subscriptions.unsubscribe();
    this.stateChanges.complete();
  }

  ngOnInit(): void {
    this.input.placeholder = this._placeholder;

    this.subscriptions.add(combineLatest([this.country$]).pipe(throttleTime(250), map(values => ({
      country: this.country$.value
    }))).subscribe(parameters => {
      this.referenceService.getRegions().pipe(first()).subscribe((regions: Map<string, Region>) => {
        this._regions = [];

        for (const region of regions.values()) {
          if (!!parameters.country) {
            if (region.country === parameters.country) {
              this._regions.push(region);
            }
          } else {
            this._regions.push(region);
          }
        }

        this.filteredOptions$.next(this._regions);

        if (!!this.value) {
          for (const region of this._regions) {
            if (region.code === this.value) {
              console.log('Setting input value based on matching option = ', region);
              this.input.value = region.name;
              return;
            }
          }

          // The value is invalid so clear it
          console.log('Clearing invalid value that does not match a valid option');
          this.value = null;
        }
      });
    }));

    this.subscriptions.add(this.regionInputValue$.pipe(
      debounceTime(500)).subscribe((value: string) => {
      console.log('Input value changed to value (' + value + '), resetting this.value');

      if (!!this._value) {
        this._value = null;
        this.onChange(this._value);
        this.changeDetectorRef.detectChanges();
        this.stateChanges.next();
      }

      value = value.toLowerCase();

      let filteredRegions: Region[] = [];

      for (const region of this._regions) {
        if (region.name.toLowerCase().indexOf(value) !== -1) {
          filteredRegions.push(region);
        }
      }

      this.filteredOptions$.next(filteredRegions);
    }));
  }

  onChange: any = (_: any) => {
  };

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
    console.log('Losing focus, this._value = ' + this._value + ' and this.regionInput.value = ', this.input.value);

    // If we have a valid value
    if (!!this._value) {
      // If we have cleared the input then clear the value
      if (!this.input.value) {
        console.log('Clearing value when input is empty and focus is lost, this.regionInput.value = ', this.input.value);
        this.filteredOptions$.next(this._regions);
        this.value = null;
      }
    }
    // If we do not have a valid value then clear the input
    else {
      console.log('this.filteredRegions$.value = ', this.filteredOptions$.value)

      // console.log('Clearing input when no valid value exists and focus is lost, this.value = ', this.value);
      // this.filteredRegions$.next(this._regions);
      // this.regionInput.value = '';
    }

    this.touched = true;
    this.onTouched();
    this.focused = this.input.focused;
    this.stateChanges.next();
  }

  onTouched: any = () => {
  };

  optionSelected(event: MatAutocompleteSelectedEvent): void {
    console.log('optionSelected event = ', event);


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
  // eslint-disable-next-line @typescript-eslint/no-explicit-any
  writeValue(value: any): void {
    if (typeof value === 'string') {
      this.value = value as string;
    }
  }
}

