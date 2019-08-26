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
  ChangeDetectorRef,
  Component,
  forwardRef,
  Host,
  HostBinding,
  Input,
  OnInit
} from '@angular/core';
import {
  AbstractControl, AsyncValidator,
  ControlValueAccessor, FormControl,
  FormGroup, NG_ASYNC_VALIDATORS,
  NG_VALIDATORS,
  NG_VALUE_ACCESSOR,
  ValidationErrors,
  Validator, Validators
} from '@angular/forms';
import {Observable, of} from 'rxjs';

@Component({
  selector: 'internal-user-directory',
  templateUrl: 'internal-user-directory.component.html',
  styleUrls: ['internal-user-directory.component.css'],
  providers: [
    {
      provide: NG_VALUE_ACCESSOR,
      useExisting: forwardRef(() => InternalUserDirectoryComponent),
      multi: true
    },
    {
      provide: NG_VALIDATORS,
      useExisting: forwardRef(() => InternalUserDirectoryComponent),
      multi: true
    }
  ]
})
export class InternalUserDirectoryComponent implements OnInit, ControlValueAccessor, Validator {

  internalUserDirectoryForm: FormGroup;

  constructor() {
    // Initialise the form
    this.internalUserDirectoryForm = new FormGroup({
      maxPasswordAttempts: new FormControl('', [Validators.required]),
      passwordExpiryMonths: new FormControl('', [Validators.required]),
      passwordHistoryMonths: new FormControl('', [Validators.required]),
      maxFilteredUsers: new FormControl('', [Validators.required])
    });
  }

  ngOnInit() {
  }

  onTouched: () => void = () => {
  };

  // tslint:disable-next-line:no-any
  writeValue(val: any): void {
    if (val) {
      this.internalUserDirectoryForm.setValue(val, {emitEvent: false});
    }
  }

  // tslint:disable-next-line:no-any
  registerOnChange(fn: any): void {
    this.internalUserDirectoryForm.valueChanges.subscribe(fn);
  }

  // tslint:disable-next-line:no-any
  registerOnTouched(fn: any): void {
    this.onTouched = fn;
  }

  setDisabledState?(isDisabled: boolean): void {
    isDisabled ? this.internalUserDirectoryForm.disable() : this.internalUserDirectoryForm.enable();
  }

  validate(c: AbstractControl): ValidationErrors | null {
    return this.internalUserDirectoryForm.valid ? null : {
      invalidForm: {
        valid: false,
        message: 'internalUserDirectoryForm fields are invalid'
      }
    };
  }
}

