/*
 * Copyright 2019 Marcus Portmann
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
  AfterContentInit,
  AfterViewInit,
  ChangeDetectionStrategy,
  ChangeDetectorRef,
  Component,
  ContentChildren,
  ElementRef,
  Inject,
  NgZone,
  Optional,
  QueryList,
  ViewEncapsulation
} from '@angular/core';
import { MatCheckbox } from '@angular/material/checkbox';
import { CanColor, LabelOptions, MAT_LABEL_GLOBAL_OPTIONS } from '@angular/material/core';
import { MAT_FORM_FIELD_DEFAULT_OPTIONS, MatFormField, matFormFieldAnimations, MatFormFieldDefaultOptions } from '@angular/material/form-field';
import {ANIMATION_MODULE_TYPE} from '@angular/platform-browser/animations';
import {startWith} from 'rxjs/operators';
import {Directionality} from '@angular/cdk/bidi';
import {Platform} from '@angular/cdk/platform';

export function getMatCheckboxMissingControlError(): Error {
  return Error('checkbox-form-field must contain a MatCheckbox.');
}

@Component({
  // tslint:disable-next-line
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
    
    .mat-form-field.checkbox-form-field.mat-form-field-slim {
      border: 1px solid red !important;
    }
  `
  ],
  animations: [matFormFieldAnimations.transitionMessages], // tslint:disable-next-line
  host: {
    'class': 'mat-form-field checkbox-form-field',
    '[class.mat-form-field-appearance-standard]': 'appearance == "standard"',
    '[class.mat-form-field-appearance-fill]': 'appearance == "fill"',
    '[class.mat-form-field-appearance-outline]': 'appearance == "outline"',
    '[class.mat-form-field-appearance-legacy]': 'appearance == "legacy"',
    '[class.mat-form-field-can-float]': '_canLabelFloat',
    '[class.mat-form-field-should-float]': 'shouldLabelFloat',
    '[class.mat-accent]': 'color == "accent"',
    '[class.mat-warn]': 'color == "warn"',
    '[class._mat-animation-noopable]': '!_animationsEnabled'
  }, // tslint:disable-next-line
  inputs: ['color'],
  encapsulation: ViewEncapsulation.None,
  changeDetection: ChangeDetectionStrategy.OnPush
}) // tslint:disable-next-line
export class CheckboxFormFieldComponent extends MatFormField implements AfterContentInit,
  AfterContentChecked, AfterViewInit, CanColor {

  /**
   * The checkboxes associated with the checkbox form field.
   */
  @ContentChildren(MatCheckbox) checkboxChildren?: QueryList<MatCheckbox>;

  // constructor(elementRef: ElementRef, private changeDetectorRef: ChangeDetectorRef,
  //             labelOptions: LabelOptions, dir: Directionality, defaults: MatFormFieldDefaultOptions,
  //             platform: Platform, ngZone: NgZone, animationMode: string) {



  // constructor(elementRef: ElementRef, private changeDetectorRef: ChangeDetectorRef,
  //             @Optional() @Inject(MAT_LABEL_GLOBAL_OPTIONS) labelOptions: LabelOptions,
  //             @Optional() directionality: Directionality, @Optional() @Inject(
  //     MAT_FORM_FIELD_DEFAULT_OPTIONS) formFieldDefaultOptions: MatFormFieldDefaultOptions, // @deletion-target 7.0.0 _platform, _ngZone and _animationMode to be made required.
  //             platform?: Platform, zone?: NgZone,
  //             @Optional() @Inject(ANIMATION_MODULE_TYPE) animationMode?: string) {
  //   super(elementRef, changeDetectorRef, labelOptions, directionality, formFieldDefaultOptions,
  //     platform, zone, animationMode);
  //
  //   if (labelOptions) {
  //     this.floatLabel = labelOptions.float;
  //   } else {
  //     this.floatLabel = 'always';
  //   }
  // }

  constructor(elementRef: ElementRef, private changeDetectorRef: ChangeDetectorRef,
              @Optional() @Inject(MAT_LABEL_GLOBAL_OPTIONS) labelOptions: LabelOptions,
              @Optional() directionality: Directionality, @Optional() @Inject(
      MAT_FORM_FIELD_DEFAULT_OPTIONS) formFieldDefaultOptions: MatFormFieldDefaultOptions, // @deletion-target 7.0.0 _platform, _ngZone and _animationMode to be made required.
              platform: Platform, zone: NgZone,
              @Optional() @Inject(ANIMATION_MODULE_TYPE) animationMode: string) {
    super(elementRef, changeDetectorRef, labelOptions, directionality, formFieldDefaultOptions,
      platform, zone, animationMode);

    if (labelOptions && labelOptions.float) {
      this.floatLabel = labelOptions.float;
    } else {
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

  /** Whether the label should float or not. */
  get shouldLabelFloat(): boolean {
    return this._canLabelFloat && this._shouldAlwaysFloat;
  }

  ngAfterContentChecked(): void {
    this.validateCheckboxChildren();
  }

  ngAfterContentInit(): void {
    this.validateCheckboxChildren();

    // Re-validate when the number of hints changes.
    this._hintChildren.changes.pipe(startWith(null)).subscribe(() => {
      this.changeDetectorRef.markForCheck();
    });
  }

  ngAfterViewInit(): void {
    // Avoid animations on load.
    this._subscriptAnimationState = 'enter';
    this.changeDetectorRef.detectChanges();
  }

  protected validateCheckboxChildren(): void {
    if (this.checkboxChildren) {
      if (this.checkboxChildren.length > 0) {
        return;
      }
    }

    throw getMatCheckboxMissingControlError();
  }
}
