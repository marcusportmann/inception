/*
 * Copyright 2018 Marcus Portmann
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

import {
  AfterContentChecked,
  AfterContentInit, AfterViewInit, ChangeDetectionStrategy,
  ChangeDetectorRef,
  Component,
  ContentChild, ContentChildren,
  ElementRef, Inject,
  Input,
  Optional, QueryList,
  ViewEncapsulation
} from '@angular/core';
import {
  CanColor,
  FloatLabelType,
  LabelOptions,
  MAT_FORM_FIELD_DEFAULT_OPTIONS,
  MAT_LABEL_GLOBAL_OPTIONS,
  MatError,
  matFormFieldAnimations,
  MatFormFieldAppearance,
  MatFormFieldDefaultOptions,
  MatHint,
  MatLabel,
  MatRadioGroup,
  mixinColor
} from "@angular/material";
import {ANIMATION_MODULE_TYPE} from "@angular/platform-browser/animations";
import {startWith} from "rxjs/operators";


export function getMatRadioGroupMissingControlError(): Error {
  return Error('radio-group-form-field must contain a MatRadioGroup.');
}


/**
 * Boilerplate for applying mixins to RadioGroupFormField.
 * @docs-private
 */
export class MatRadioGroupFormFieldBase {
  constructor(public _elementRef: ElementRef) {
  }
}

/**
 * Base class to which we're applying the radio group form field mixins.
 * @docs-private
 */
export const _MatRadioGroupFormFieldMixinBase = mixinColor(MatRadioGroupFormFieldBase, 'primary');


@Component({
  selector: 'radio-group-form-field',
  template: `
    <div class="mat-form-field-wrapper">
      <div class="mat-form-field-flex">
        <div class="mat-form-field-infix">
          <ng-content></ng-content>
          <span class="mat-form-field-label-wrapper">
            <label class="mat-form-field-label"
                   [class.mat-accent]="color == 'accent'"
                   [class.mat-warn]="color == 'warn'"
                   #label
                   *ngIf="hasLabel"><ng-content select="mat-label"></ng-content><span class="mat-placeholder-required mat-form-field-required-marker" aria-hidden="true" *ngIf="!hideRequiredMarker && radioGroup.required && !radioGroup.disabled">&nbsp;*</span>
              <!-- @deletion-target 8.0.0 remove \`mat-placeholder-required\` class -->
            </label>
          </span>
        </div>
      </div>

      <div class="mat-form-field-subscript-wrapper">
        <div *ngIf="hasError" [@transitionMessages]="subscriptAnimationState">
          <ng-content select="mat-error"></ng-content>
        </div>

        <div class="mat-form-field-hint-wrapper" *ngIf="!hasError"
             [@transitionMessages]="subscriptAnimationState">
          <ng-content select="mat-hint:not([align='end'])"></ng-content>
          <div class="mat-form-field-hint-spacer"></div>
          <ng-content select="mat-hint[align='end']"></ng-content>
        </div>
      </div>
    </div>
  `,
  styleUrls: [
    'radio-group-form-field.css',
  ],
  animations: [matFormFieldAnimations.transitionMessages],
  host: {
    'class': 'mat-form-field radio-group-form-field',
    '[class.mat-form-field-appearance-standard]': 'appearance == "standard"',
    '[class.mat-form-field-appearance-fill]': 'appearance == "fill"',
    '[class.mat-form-field-appearance-outline]': 'appearance == "outline"',
    '[class.mat-form-field-appearance-legacy]': 'appearance == "legacy"',
    '[class.mat-form-field-invalid]': 'hasError',
    '[class.mat-form-field-can-float]': 'canLabelFloat',
    '[class.mat-form-field-should-float]': 'shouldLabelFloat',
    '[class.mat-form-field-disabled]': 'radioGroup.disabled',
    '[class.mat-accent]': 'color == "accent"',
    '[class.mat-warn]': 'color == "warn"',
    '[class._mat-animation-noopable]': '!_animationsEnabled'
  },
  inputs: ['color'],
  encapsulation: ViewEncapsulation.None,
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class RadioGroupFormField extends _MatRadioGroupFormFieldMixinBase
  implements AfterContentInit, AfterContentChecked, AfterViewInit, CanColor {

  private _animationsEnabled: boolean;

  private _appearance: MatFormFieldAppearance;

  @ContentChildren(MatError)
  private _errorChildren: QueryList<MatError>;

  private _floatLabel: FloatLabelType;

  private readonly _formFieldDefaultOptions: MatFormFieldDefaultOptions;

  @ContentChildren(MatHint)
  private _hintChildren: QueryList<MatHint>;

  @ContentChild(MatLabel)
  private _labelChild: MatLabel;

  private _labelOptions: LabelOptions;

  // Override for the logic that disables the label animation in certain cases.
  private _showAlwaysAnimate: boolean = false;

  /**
   * Hide the required marker.
   *
   * @type {boolean}
   */
  hideRequiredMarker: boolean = false;

  /**
   * The radio group associated with the radio group form field.
   */
  @ContentChild(MatRadioGroup)
  radioGroup: MatRadioGroup;

  /**
   * The state of the mat-hint and mat-error animations.
   *
   * @type {string}
   */
  subscriptAnimationState: string = '';

  /**
   * Constructs a new RadioGroupFormField.
   *
   * @param {ElementRef} _elementRef
   * @param {ChangeDetectorRef} _changeDetectorRef
   * @param {LabelOptions} labelOptions
   * @param {MatFormFieldDefaultOptions} formFieldDefaultOptions
   * @param {string} _animationMode
   */
  constructor(public _elementRef: ElementRef,
              private _changeDetectorRef: ChangeDetectorRef,
              @Optional() @Inject(MAT_LABEL_GLOBAL_OPTIONS) private labelOptions: LabelOptions,
              @Optional() @Inject(MAT_FORM_FIELD_DEFAULT_OPTIONS) private formFieldDefaultOptions: MatFormFieldDefaultOptions,
              @Optional() @Inject(ANIMATION_MODULE_TYPE) _animationMode?: string) {
    super(_elementRef);

    this._labelOptions = labelOptions ? labelOptions : {};

    this._formFieldDefaultOptions = formFieldDefaultOptions ? formFieldDefaultOptions : {};

    this._floatLabel = this._labelOptions.float || 'always';

    this._animationsEnabled = _animationMode !== 'NoopAnimations';
  }

  /**
   * The form field appearance style.
   *
   * @returns {MatFormFieldAppearance} The form field appearance style.
   */
  @Input()
  get appearance(): MatFormFieldAppearance {
    return this._appearance || this._formFieldDefaultOptions && this._formFieldDefaultOptions.appearance || 'legacy';
  }

  set appearance(value: MatFormFieldAppearance) {
    this._appearance = value;
  }

  /**
   * Whether the label can float or not.
   *
   * @returns {boolean} True if the label can fault or false otherwise.
   */
  get canLabelFloat(): boolean {
    return this.floatLabel !== 'never';
  }

  /**
   * Whether the label should always float, never float or float as the user types.
   *
   * @returns {FloatLabelType} the label should always float, never float or float as the user types.
   */
  @Input()
  get floatLabel(): FloatLabelType {
    return this.appearance !== 'legacy' && this._floatLabel === 'never' ? 'auto' : this._floatLabel;
  }

  set floatLabel(value: FloatLabelType) {
    if (value !== this._floatLabel) {
      this._floatLabel = value || this._labelOptions.float || 'auto';
      this._changeDetectorRef.markForCheck();
    }
  }

  /**
   * Whether there are one or more errors associated with the radio group form field.
   *
   * @returns {boolean} True if there are one or more errors associated with the radio group form
   *                    field or false otherwise.
   */
  get hasError(): boolean {
    if (this._errorChildren) {
      if (this._errorChildren.length > 0) {
        return true;
      }
    }

    return false;
  }

  /**
   * Whether the radio group form field has a label.
   *
   * @returns {boolean} True if the radio group form field has a label or false otherwise.
   */
  get hasLabel(): boolean {
    return !!this._labelChild;
  }

  /**
   * Whether the floating label should always float or not.
   *
   * @returns {boolean} True if the label should always float or false otherwise.
   */
  get shouldLabelAlwaysFloat(): boolean {
    return this.floatLabel === 'always' && !this._showAlwaysAnimate;
  }

  /**
   * Returns whether the label should float or not.
   *
   * @returns {boolean} True if the label should float or false otherwise.
   */
  get shouldLabelFloat(): boolean {
    return this.canLabelFloat && this.shouldLabelAlwaysFloat;
  }

  ngAfterContentChecked() {
    this._validateControlChild();
  }

  ngAfterContentInit() {
    this._validateControlChild();

    // Re-validate when the number of hints changes.
    this._hintChildren.changes.pipe(startWith(null)).subscribe(() => {
      this._changeDetectorRef.markForCheck();
    });

    // Update the aria-described by when the number of errors changes.
    this._errorChildren.changes.pipe(startWith(null)).subscribe(() => {
      this._changeDetectorRef.markForCheck();
    });
  }

  ngAfterViewInit() {
    // Avoid animations on load.
    this.subscriptAnimationState = 'enter';
    this._changeDetectorRef.detectChanges();
  }

  private _validateControlChild() {
    if (!this.radioGroup) {
      throw getMatRadioGroupMissingControlError();
    }
  }
}
