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
  ContentChildren,
  ElementRef,
  Inject,
  NgZone,
  Optional, QueryList,
  ViewEncapsulation
} from '@angular/core';
import {
  CanColor,
  LabelOptions,
  MAT_FORM_FIELD_DEFAULT_OPTIONS,
  MAT_LABEL_GLOBAL_OPTIONS, MatCheckbox,
  MatFormField,
  matFormFieldAnimations,
  MatFormFieldDefaultOptions, MatHint
} from "@angular/material";
import {ANIMATION_MODULE_TYPE} from "@angular/platform-browser/animations";
import {startWith} from "rxjs/operators";
import {Directionality} from "@angular/cdk/bidi";
import {Platform} from "@angular/cdk/platform";


export function getMatCheckboxMissingControlError(): Error {
  return Error('checkbox-form-field must contain a MatCheckbox.');
}

@Component({
  selector: 'checkbox-form-field',
  template: `
    <div class="mat-form-field-wrapper">
      <div class="mat-form-field-flex">
        <div class="mat-form-field-infix">
          <ng-content></ng-content>
          <span class="mat-form-field-label-wrapper">
            <label class="mat-form-field-label"
                   [class.mat-accent]="color == 'accent'"
                   [class.mat-warn]="color == 'warn'"
                   #label *ngIf="_hasLabel">
              <ng-content select="mat-label"></ng-content>
            </label>
          </span>
        </div>
      </div>

      <div class="mat-form-field-subscript-wrapper">
        <div class="mat-form-field-hint-wrapper" *ngIf="hasHint"
             [@transitionMessages]="_subscriptAnimationState">
          <ng-content select="mat-hint:not([align='end'])"></ng-content>
          <div class="mat-form-field-hint-spacer"></div>
          <ng-content select="mat-hint[align='end']"></ng-content>
        </div>
      </div>
    </div>
  `,
  styles: [`

    .mat-form-field.checkbox-form-field .mat-checkbox {
      margin-right: 0.875em !important;
    }

    .mat-form-field.checkbox-form-field .mat-checkbox:last-child {
      margin-right: 0 !important;
    }
    
    .mat-form-field.checkbox-form-field .mat-form-field-infix {
      padding-bottom: 0 !important;
    }
  `],
  animations: [matFormFieldAnimations.transitionMessages],
  host: {
    'class': 'mat-form-field checkbox-form-field',
    '[class.mat-form-field-appearance-standard]': 'appearance == "standard"',
    '[class.mat-form-field-appearance-fill]': 'appearance == "fill"',
    '[class.mat-form-field-appearance-outline]': 'appearance == "outline"',
    '[class.mat-form-field-appearance-legacy]': 'appearance == "legacy"',
    '[class.mat-form-field-invalid]': 'hasError',
    '[class.mat-form-field-can-float]': '_canLabelFloat',
    '[class.mat-form-field-should-float]': 'shouldLabelFloat',
    //'[class.mat-form-field-disabled]': 'radioGroup.disabled',
    '[class.mat-accent]': 'color == "accent"',
    '[class.mat-warn]': 'color == "warn"',
    '[class._mat-animation-noopable]': '!_animationsEnabled'
  },
  inputs: ['color'],
  encapsulation: ViewEncapsulation.None,
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class CheckboxFormField extends MatFormField
  implements AfterContentInit, AfterContentChecked, AfterViewInit, CanColor {

  /**
   * The checkboxes associated with the checkbox form field.
   */
  @ContentChildren(MatCheckbox) private _checkboxChildren: QueryList<MatCheckbox>;

  /**
   * The hints associated with the checkbox form field.
   */
  @ContentChildren(MatHint) _hintChildren: QueryList<MatHint>;

  constructor(public _elementRef: ElementRef,
              private _newChangeDetectorRef: ChangeDetectorRef,
              @Optional() @Inject(MAT_LABEL_GLOBAL_OPTIONS) private labelOptions: LabelOptions,
              @Optional() private _newDir: Directionality,
              @Optional() @Inject(MAT_FORM_FIELD_DEFAULT_OPTIONS) private formFieldDefaultOptions:
                MatFormFieldDefaultOptions,
              // @deletion-target 7.0.0 _platform, _ngZone and _animationMode to be made required.
              private _newPlatform?: Platform,
              private _newNgZone?: NgZone,
              @Optional() @Inject(ANIMATION_MODULE_TYPE) _animationMode?: string) {
    super(_elementRef, _newChangeDetectorRef, labelOptions, _newDir, formFieldDefaultOptions,
      _newPlatform, _newNgZone, _animationMode);

    if (labelOptions) {
      this.floatLabel = labelOptions.float;
    }
    else {
      this.floatLabel = 'always';
    }
  }

  /** Whether there are one or more hints associated with the checkbox form field. */
  get hasHint(): boolean {

    if (this._hintChildren) {
      if (this._hintChildren.length > 0) {
        return true;
      }
    }

    return false;
  }

  /** Returns whether the label should float or not. */
  get shouldLabelFloat(): boolean {
    return this._canLabelFloat && this._shouldAlwaysFloat;
  }

  ngAfterContentChecked() {
    this._validateCheckboxChildren();
  }

  ngAfterContentInit() {
    this._validateCheckboxChildren();

    // Re-validate when the number of hints changes.
    this._hintChildren.changes.pipe(startWith(null)).subscribe(() => {
      this._newChangeDetectorRef.markForCheck();
    });
  }

  ngAfterViewInit() {
    // Avoid animations on load.
    this._subscriptAnimationState = 'enter';
    this._newChangeDetectorRef.detectChanges();
  }

  protected _validateCheckboxChildren() {
    if (this._checkboxChildren) {
      if (this._checkboxChildren.length > 0) {
        return;
      }
    }

    throw getMatCheckboxMissingControlError();
  }
}
