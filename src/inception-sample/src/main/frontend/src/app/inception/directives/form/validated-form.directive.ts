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
  AfterViewInit,
  Directive,
  Host,
  Optional,
  Self, ViewContainerRef
} from '@angular/core'

import {
  FormGroupDirective,
} from '@angular/forms';


/**
 * The ValidatedFormDirective class implements the validated form directive.
 *
 * @author Marcus Portmann
 */
@Directive({
  selector: 'form[validatedForm]',
  host: {'(submit)': 'onSubmit($event)', '(reset)': 'onReset()'}
})
export class ValidatedFormDirective implements AfterViewInit {

  /**
   * Constructs a new ValidatedFormDirective.
   *
   * @param {ViewContainerRef} viewContainerRef     The view container reference.
   * @param {FormGroupDirective} formGroupDirective The form group directive.
   */
  constructor(private viewContainerRef: ViewContainerRef,
    @Host() @Self() @Optional() private formGroupDirective: FormGroupDirective) {
  }

  ngAfterViewInit(): void {
  }

  onReset(): void {
  }

  onSubmit($event: Event): boolean {
    // Mark all controls as touched
    if (this.formGroupDirective && this.formGroupDirective.control && this.formGroupDirective.control.controls) {
      let form = this.formGroupDirective.control;

      for (let controlName in form.controls) {
        form.controls[controlName].markAsTouched();
      }
    }

    // Find the first invalid form control and set focus to it
    if (this.checkForInvalidFormControlAndSetFocus(this.viewContainerRef.element.nativeElement)) {
      return false;
    }

    return true;
  }

  private checkForInvalidFormControlAndSetFocus(nativeElement: any): boolean {
    if (nativeElement.children && (nativeElement.children.length > 0)) {
      for (let i = 0; i < nativeElement.children.length; i++) {
        let nativeChildElement = nativeElement.children[i];

        if (nativeChildElement && nativeChildElement.nodeName && this.isFormElement(nativeChildElement.nodeName)) {
          if (this.formGroupDirective && this.formGroupDirective.control && this.formGroupDirective.control.controls) {
            let formControl = this.formGroupDirective.control.controls[nativeChildElement.name];

            if (formControl && formControl.invalid) {
              nativeChildElement.focus();

              return true;
            }
          }
        }

        if (this.checkForInvalidFormControlAndSetFocus(nativeChildElement)) {
          return true;
        }
      }
    }

    return false;
  }

  private isFormElement(nodeName: string): boolean {
    switch(nodeName) {
      case 'INPUT': {
        return true;
      }

      default: {
        return false;
      }
    }
  }
}
