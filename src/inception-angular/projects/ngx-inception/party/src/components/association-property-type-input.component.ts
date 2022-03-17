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
import {ReplaySubject, Subject, Subscription} from 'rxjs';
import {debounceTime, first, map, startWith} from 'rxjs/operators';
import {AssociationPropertyType} from '../services/association-property-type';
import {PartyReferenceService} from '../services/party-reference.service';

/**
 * The AssociationPropertyTypeInputComponent class implements the association property type input
 * component.
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
        (input)="associationPropertyTypeInputChanged($event)"
        (focusin)="onFocusIn($event)"
        (focusout)="onFocusOut($event)">
      <mat-autocomplete
        #associationPropertyTypeAutocomplete="matAutocomplete"
        [displayWith]="displayAssociationPropertyType"

        (optionSelected)="selectAssociationPropertyType($event)">
        <mat-option
          *ngFor="let associationPropertyType of filteredAssociationPropertyTypes$ | async"
          [value]="associationPropertyType">
          {{associationPropertyType.name}}
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
   * The name for the control type.
   */
  controlType = 'association-property-type-input';

  /**
   * The filtered association property types for the autocomplete.
   */
  filteredAssociationPropertyTypes$: Subject<AssociationPropertyType[]> = new ReplaySubject<AssociationPropertyType[]>();

  /**
   * Whether the control is focused.
   */
  focused = false;

  /**
   * The ID for the control.
   */
  @HostBinding() id = `association-property-type-input-${AssociationPropertyTypeInputComponent._nextId++}`;

  /**
   * The observable indicating that the state of the control has changed.
   */
  stateChanges = new Subject<void>();

  /**
   * Has the control received a touch event.
   */
  touched: boolean = false;

  //@Input('aria-describedby') userAriaDescribedBy?: string;

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
   * @return the code for the selected association property type
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
      this.partyReferenceService.getAssociationPropertyTypes().pipe(first()).subscribe((associationPropertyTypes: Map<string, AssociationPropertyType>) => {
        this._value = null;
        this.associationPropertyTypeInput.value = '';

        if (!!value) {
          for (const associationPropertyType of associationPropertyTypes.values()) {
            if (associationPropertyType.code === value) {
              this._value = value;
              this.associationPropertyTypeInput.value = associationPropertyType.name;
              break;
            }
          }
        }

        this.onChange(this._value);
        this.changeDetectorRef.detectChanges();
        this.stateChanges.next();
      });
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
    return this.focused || !this.empty || this.associationPropertyTypeInput.focused;
  }

  associationPropertyTypeInputChanged(event: Event) {
    if (((event.target as HTMLInputElement).value) !== undefined) {
      this.associationPropertyTypeInputValue$.next((event.target as HTMLInputElement).value);
    }
  }

  displayAssociationPropertyType(associationPropertyType: AssociationPropertyType): string {
    if (!!associationPropertyType) {
      return associationPropertyType.name;
    } else {
      return '';
    }
  }

  ngOnDestroy(): void {
    this.subscriptions.unsubscribe();
    this.stateChanges.complete();
  }

  ngOnInit(): void {
    this.associationPropertyTypeInput.placeholder = this._placeholder;

    this.partyReferenceService.getAssociationPropertyTypes().pipe(first()).subscribe((associationPropertyTypes: Map<string, AssociationPropertyType>) => {
      this.subscriptions.add(this.associationPropertyTypeInputValue$.pipe(
        startWith(''),
        debounceTime(500),
        map((value: string | AssociationPropertyType) => {
          if (typeof (value) === 'string') {
            value = value.toLowerCase();
          } else {
            value = value.name.toLowerCase();
          }

          let filteredAssociationPropertyTypes: AssociationPropertyType[] = [];

          for (const associationPropertyType of associationPropertyTypes.values()) {
            if (associationPropertyType.name.toLowerCase().indexOf(value) === 0) {
              filteredAssociationPropertyTypes.push(associationPropertyType);
            }
          }

          this.filteredAssociationPropertyTypes$.next(filteredAssociationPropertyTypes);
        })).subscribe());
    });
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
    // If we have cleared the associationPropertyType input then clear the value when losing focus
    if ((!!this._value) && (!this.associationPropertyTypeInput.value)) {
      this._value = null;
      this.onChange(this._value);
      this.changeDetectorRef.detectChanges();
      this.stateChanges.next();
    }

    this.touched = true;
    this.onTouched();
    this.focused = this.associationPropertyTypeInput.focused;
    this.stateChanges.next();
  }

  onTouched: any = () => {
  };

  // eslint-disable-next-line @typescript-eslint/no-explicit-any
  registerOnChange(fn: any): void {
    this.onChange = fn;
  }

  // eslint-disable-next-line @typescript-eslint/no-explicit-any
  registerOnTouched(fn: any): void {
    this.onTouched = fn;
  }

  selectAssociationPropertyType(event: MatAutocompleteSelectedEvent): void {
    this.value = event.option.value.code;
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
