/*
 * Copyright 2021 Marcus Portmann
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
import {Language} from '../services/language';
import {ReferenceService} from '../services/reference.service';

/**
 * The LanguageInputComponent class implements the language input component.
 *
 * @author Marcus Portmann
 */
@Component({
  // tslint:disable-next-line:component-selector
  selector: 'language-input',
  template: `
    <div matAutocompleteOrigin #origin="matAutocompleteOrigin">
      <input
        #languageInput
        type="text"
        matInput
        autocompleteSelectionRequired
        required="required"
        [matAutocomplete]="languageAutocomplete"
        [matAutocompleteConnectedTo]="origin"
        (input)="languageInputChanged($event)"
        (focusin)="onFocusIn($event)"
        (focusout)="onFocusOut($event)">
      <mat-autocomplete
        #languageAutocomplete="matAutocomplete"
        [displayWith]="displayLanguage"
        (optionSelected)="selectLanguage($event)">
        <mat-option *ngFor="let language of filteredLanguages$ | async" [value]="language">
          {{language.shortName}}
        </mat-option>
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
export class LanguageInputComponent implements MatFormFieldControl<string>,
  ControlValueAccessor, OnInit, OnDestroy {

  private static _nextId: number = 0;

  /**
   * The name for the control type.
   */
  controlType = 'language-input';

  /**
   * The filtered languages for the autocomplete.
   */
  filteredLanguages$: Subject<Language[]> = new ReplaySubject<Language[]>();

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
  @ViewChild(MatInput, {static: true}) languageInput!: MatInput;

  /**
   * The reference to the element for the language input.
   */
  @ViewChild('languageInput') languageInputElementRef!: ElementRef;

  /**
   * The observable providing access to the value for the language input as it changes.
   */
  languageInputValue$: Subject<string> = new ReplaySubject<string>();

  /**
   * The observable indicating that the state of the control has changed.
   */
  stateChanges = new Subject<void>();

  /**
   * Has the control received a touch event.
   */
  touched: boolean = false;

  @Input('aria-describedby') userAriaDescribedBy?: string;

  /**
   * Whether the control is disabled.
   * @private
   */
  private _disabled = false;

  /**
   * The placeholder for the language input.
   * @private
   */
  private _placeholder: string = '';

  /**
   * Whether the control is required.
   * @private
   */
  private _required: boolean = false;

  /**
   * The ISO 639-1 alpha-2 code for the selected language.
   */
  private _value: string | null = null;

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

  @Input()
  get disabled(): boolean {
    return this._disabled;
  }

  set disabled(value: boolean) {
    this._disabled = coerceBooleanProperty(value);

    if (this._disabled) {
      this.languageInput.disabled = true;
    }

    this.stateChanges.next();
  }

  get empty(): boolean {
    return this.languageInput.empty;
  }

  get errorState(): boolean {
    return this.required && this.languageInput.empty && this.touched;
  }

  @Input()
  get placeholder() {
    return this._placeholder;
  }

  set placeholder(placeholder) {
    this._placeholder = placeholder;
    this.stateChanges.next();
  }

  @Input()
  get required(): boolean {
    return this._required;
  }

  set required(req: any) {
    this._required = coerceBooleanProperty(req);
    this.stateChanges.next();
  }

  @HostBinding('class.floating')
  get shouldLabelFloat() {
    return this.focused || !this.empty || this.languageInput.focused;
  }

  /**
   * Returns the ISO 639-1 alpha-2 code for the selected language.
   *
   * @return the ISO 639-1 alpha-2 code for the selected language
   */
  public get value(): string | null {
    return this._value;
  }

  /**
   * Set the ISO 639-1 alpha-2 code for the selected language.
   *
   * @param value the ISO 639-1 alpha-2 code for the selected language
   */
  @Input()
  public set value(value: string | null) {
    if (value !== undefined) {
      if (this._value !== value) {
        this._value = value;
        this.onChange(this._value);
        this.changeDetectorRef.detectChanges();
        this.stateChanges.next();
      }
    } else {
      this._value = null
      this.onChange(this._value);
      this.changeDetectorRef.detectChanges();
      this.stateChanges.next();
    }
  }

  displayLanguage(language: Language): string {
    if (!!language) {
      return language.name;
    } else {
      return '';
    }
  }

  languageInputChanged(event: Event) {
    if (((event.target as HTMLInputElement).value) !== undefined) {
      this.languageInputValue$.next((event.target as HTMLInputElement).value);
    }
  }

  ngOnDestroy(): void {
    this.subscriptions.unsubscribe();
    this.stateChanges.complete();
  }

  ngOnInit(): void {
    this.languageInput.placeholder = this._placeholder;

    this.referenceService.getLanguages().pipe(first()).subscribe((languages: Map<string, Language>) => {
      this.subscriptions.add(this.languageInputValue$.pipe(
        startWith(''),
        debounceTime(500),
        map((value: string | Language) => {
          if (typeof (value) === 'string') {
            value = value.toLowerCase();
          } else {
            value = value.shortName.toLowerCase();
          }

          let filteredLanguages: Language[] = [];

          for (const language of languages.values()) {
            if (language.shortName.toLowerCase().indexOf(value) === 0) {
              filteredLanguages.push(language);
            }
          }

          this.filteredLanguages$.next(filteredLanguages);
        })).subscribe());
    });
  }

  onChange: any = (_: any) => {
  };

  onContainerClick(event: MouseEvent) {
    if ((event.target as Element).tagName.toLowerCase() != 'input') {
      this.languageInput.focus();
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
    this.focused = this.languageInput.focused;
    this.stateChanges.next();
  }

  onTouched: any = () => {
  };

  // tslint:disable-next-line:no-any
  registerOnChange(fn: any): void {
    this.onChange = fn;
  }

  // tslint:disable-next-line:no-any
  registerOnTouched(fn: any): void {
    this.onTouched = fn;
  }

  selectLanguage(event: MatAutocompleteSelectedEvent): void {
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

  // tslint:disable-next-line:no-any
  writeValue(value: any): void {
    console.log('[LanguageInputComponent][writeValue] value = ', value);

    if (typeof value === 'string') {
      this._value = value as string;
    }
  }

  private _valueChanged(value: string | null) {
    this.onChange(value);
    this.changeDetectorRef.detectChanges();
    this.stateChanges.next();
  }
}
