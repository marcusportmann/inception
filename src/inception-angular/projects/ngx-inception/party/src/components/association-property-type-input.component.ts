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
import {MatAutocompleteSelectedEvent} from '@angular/material/autocomplete';
import {MatFormFieldControl} from '@angular/material/form-field';
import {MatInput} from '@angular/material/input';
import {
  BehaviorSubject, combineLatest, ReplaySubject, Subject, Subscription, throttleTime
} from 'rxjs';
import {debounceTime, first, map, startWith} from 'rxjs/operators';
import {AssociationType} from '../services/association-type';
import {PartyReferenceService} from '../services/party-reference.service';
import {AssociationPropertyType} from '../services/association-property-type';

/**
 * The AssociationPropertyTypeInputComponent class implements the association property type input component.
 *
 * @author Marcus Portmann
 */
@Component({
  // eslint-disable-next-line @angular-eslint/component-selector
  selector: 'association-property-type-input',
  template: `
    <div matAutocompleteOrigin #origin="matAutocompleteOrigin">
      <input
        #associationPropertyTypeInput
        type="text"
        matInput
        autocompleteSelectionRequired
        required="required"
        [matAutocomplete]="associationPropertyTypeAutocomplete"
        [matAutocompleteConnectedTo]="origin"
        (input)="inputChanged($event)"
        (focusin)="onFocusIn($event)"
        (focusout)="onFocusOut($event)">
      <mat-autocomplete
        #associationPropertyTypeAutocomplete="matAutocomplete"
        (optionSelected)="optionSelected($event)"
        [displayWith]="displayWith">
        <mat-option *ngFor="let associationPropertyType of filteredAssociationPropertyTypes$ | async" [value]="associationPropertyType">
          {{ associationPropertyType.name }}
        </mat-option>
      </mat-autocomplete>
    </div>
  `,
  providers: [
    {
      provide: MatFormFieldControl,
      useExisting: AssociationPropertyTypeInputComponent
    }
  ]
})
export class AssociationPropertyTypeInputComponent implements MatFormFieldControl<string>,
  ControlValueAccessor, OnInit, OnDestroy {

  private static _nextId: number = 0;

  /**
   * The name for the control type.
   */
  controlType = 'association-property-type-input';

  /**
   * The filtered association property types for the autocomplete.
   */
  filteredAssociationPropertyTypes$: BehaviorSubject<AssociationPropertyType[]> = new BehaviorSubject<AssociationPropertyType[]>([]);

  /**
   * Whether the control is focused.
   */
  focused = false;

  /**
   * The ID for the control.
   */
  @HostBinding() id = `association-property-type-input-${AssociationPropertyTypeInputComponent._nextId++}`;

  /**
   * The association property type input.
   */
  @ViewChild(MatInput, {static: true}) associationPropertyTypeInput!: MatInput;

  /**
   * The reference to the element for the association property type input.
   */
  @ViewChild('associationPropertyTypeInput') associationPropertyTypeInputElementRef!: ElementRef;

  /**
   * The observable providing access to the value for the association property type input as it changes.
   */
  associationPropertyTypeInputValue$: Subject<string> = new ReplaySubject<string>();

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
   * The association property types for the country.
   */
  private _associationPropertyTypes: AssociationPropertyType[] = [];

  /**
   * The code for the association type to retrieve the association property types for.
   */
  private associationType$: BehaviorSubject<string | null> = new BehaviorSubject<string | null>(null);

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
      this.associationPropertyTypeInput.disabled = true;
    }

    this.stateChanges.next();
  }

  /**
   * The placeholder for the association property type input.
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
   * The code for the selected association property type.
   */
  private _value: string | null = null;

  /**
   * Returns the code for the selected association property type.
   *
   * @return The code for the selected association property type.
   */
  public get value(): string | null {
    return this._value;
  }

  /**
   * Set the code for the selected association property type.
   *
   * @param value the code for the selected association property type
   */
  @Input()
  public set value(value: string | null) {
    if (value == undefined) {
      value = null;
    }

    if (this._value !== value) {
      console.log('Setting the value to ' + value + ", existing value = " + this._value);

      this._value = value;

      // If options have been loaded, check if the value is valid
      if (!!this._value) {
        if (this._associationPropertyTypes.length > 0) {
          for (const associationPropertyType of this._associationPropertyTypes) {
            if (associationPropertyType.code === value) {
              this.associationPropertyTypeInput.value = associationPropertyType.name;

              this._value = value;
              this.onChange(this._value);
              this.changeDetectorRef.detectChanges();
              this.stateChanges.next();

              return;
            }
          }

          this._value = null;
        }
      }

      this.associationPropertyTypeInput.value = '';

      this.onChange(this._value);
      this.changeDetectorRef.detectChanges();
      this.stateChanges.next();
    }
  }

  /**
   * The code for the association type to retrieve the association property types for.
   */
  @Input() get associationType(): string | null {
    return this.associationType$.value;
  }

  set associationType(associationType: string | null) {
    if (associationType == undefined) {
      associationType = null;
    }

    if (associationType !== this.associationType$.value) {
      this.associationType$.next(associationType);
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
    return this.focused || !this.empty || this.associationPropertyTypeInput.focused;
  }

  inputChanged(event: Event) {
    if (((event.target as HTMLInputElement).value) !== undefined) {
      this.associationPropertyTypeInputValue$.next((event.target as HTMLInputElement).value);
    }
  }

  ngOnDestroy(): void {
    this.subscriptions.unsubscribe();
    this.stateChanges.complete();
  }

  ngOnInit(): void {
    this.associationPropertyTypeInput.placeholder = this._placeholder;

    this.subscriptions.add(combineLatest([this.associationType$]).pipe(throttleTime(250), map(values => ({
      associationType: this.associationType$.value
    }))).subscribe(parameters => {
      this.partyReferenceService.getAssociationPropertyTypes().pipe(first()).subscribe((associationPropertyTypes: Map<string, AssociationPropertyType>) => {
        this._associationPropertyTypes = [];

        for (const associationPropertyType of associationPropertyTypes.values()) {
          if (!!parameters.associationType) {
            if (associationPropertyType.associationType === parameters.associationType) {
              this._associationPropertyTypes.push(associationPropertyType);
            }
          } else {
            this._associationPropertyTypes.push(associationPropertyType);
          }
        }

        this.filteredAssociationPropertyTypes$.next(this._associationPropertyTypes);

        if (!!this.value) {
          for (const associationPropertyType of this._associationPropertyTypes) {
            if (associationPropertyType.code === this.value) {
              console.log('Setting input value based on matching associationPropertyType = ', associationPropertyType);
              this.associationPropertyTypeInput.value = associationPropertyType.name;
              return;
            }
          }

          // The value is invalid so clear it
          console.log('Clearing invalid value that does not match a valid association property type');
          this.value = null;
        }
      });
    }));

    this.subscriptions.add(this.associationPropertyTypeInputValue$.pipe(
      debounceTime(500)).subscribe((value: string) => {
      console.log('Input value changed to value (' + value + '), resetting this.value');

      if (!!this._value) {
        this._value = null;
        this.onChange(this._value);
        this.changeDetectorRef.detectChanges();
        this.stateChanges.next();
      }

      value = value.toLowerCase();

      let filteredAssociationPropertyTypes: AssociationPropertyType[] = [];

      for (const associationPropertyType of this._associationPropertyTypes) {
        if (associationPropertyType.name.toLowerCase().indexOf(value) === 0) {
          filteredAssociationPropertyTypes.push(associationPropertyType);
        }
      }

      this.filteredAssociationPropertyTypes$.next(filteredAssociationPropertyTypes);
    }));
  }

  onChange: any = (_: any) => {
  };

  onContainerClick(event: MouseEvent) {
    if ((event.target as Element).tagName.toLowerCase() != 'input') {
      this.associationPropertyTypeInput.focus();
    }
  }

  onFocusIn(event: FocusEvent) {
    if (!this.focused) {
      this.focused = true;
      this.stateChanges.next();
    }
  }

  onFocusOut(event: FocusEvent) {
    console.log('Losing focus, this._value = ' + this._value + ' and this.associationPropertyTypeInput.value = ', this.associationPropertyTypeInput.value );

    // If we have a valid value
    if (!!this._value) {
      // If we have cleared the input then clear the value
      if (!this.associationPropertyTypeInput.value) {
        console.log('Clearing value when input is empty and focus is lost, this.associationPropertyTypeInput.value = ', this.associationPropertyTypeInput.value);
        this.filteredAssociationPropertyTypes$.next(this._associationPropertyTypes);
        this.value = null;
      }
    }
    // If we do not have a valid value then clear the input
    else {
      console.log('Clearing input when no valid value exists and focus is lost, this.value = ', this.value);
      this.filteredAssociationPropertyTypes$.next(this._associationPropertyTypes);
      this.associationPropertyTypeInput.value = '';
    }

    this.touched = true;
    this.onTouched();
    this.focused = this.associationPropertyTypeInput.focused;
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


  displayWith(associationType: AssociationType): string {
    if (!!associationType) {
      return associationType.name;
    } else {
      return '';
    }
  }
}
