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

import {Component, forwardRef, OnInit} from '@angular/core';
import {
  AbstractControl,
  ControlValueAccessor,
  FormGroup,
  NG_VALIDATORS,
  NG_VALUE_ACCESSOR,
  ValidationErrors,
  Validator
} from '@angular/forms';

@Component({
  selector: 'ldap-user-directory',
  templateUrl: 'ldap-user-directory.component.html',
  styleUrls: ['ldap-user-directory.component.css'],
  providers: [
    {
      provide: NG_VALUE_ACCESSOR,
      useExisting: forwardRef(() => LdapUserDirectoryComponent),
      multi: true
    },
    {
      provide: NG_VALIDATORS,
      useExisting: forwardRef(() => LdapUserDirectoryComponent),
      multi: true
    }
  ]
})
export class LdapUserDirectoryComponent implements OnInit, ControlValueAccessor, Validator {

  ldapUserDirectoryForm: FormGroup;

  constructor() {
    // Initialise the form
    this.ldapUserDirectoryForm = new FormGroup({
      //name: new FormControl('',       [Validators.required, Validators.maxLength(4000)]),
      //userDirectoryType: new FormControl('')
    });
  }

  ngOnInit() {
  }

  onTouched: () => void = () => {
  };

  // tslint:disable-next-line:no-any
  writeValue(val: any): void {
    if (val) {
      this.ldapUserDirectoryForm.setValue(val, {emitEvent: false});
    }
  }

  // tslint:disable-next-line:no-any
  registerOnChange(fn: any): void {
    this.ldapUserDirectoryForm.valueChanges.subscribe(fn);
  }

  // tslint:disable-next-line:no-any
  registerOnTouched(fn: any): void {
    this.onTouched = fn;
  }

  setDisabledState?(isDisabled: boolean): void {
    isDisabled ? this.ldapUserDirectoryForm.disable() : this.ldapUserDirectoryForm.enable();
  }

  validate(c: AbstractControl): ValidationErrors | null {
    console.log('[LdapUserDirectoryComponent][validate] this.ldapUserDirectoryForm.valid = ', this.ldapUserDirectoryForm.valid);

    return null;

    // return this.ldapUserDirectoryForm.valid ? null : {
    //   invalidForm: {
    //     valid: false,
    //     message: 'ldapUserDirectoryForm fields are invalid'
    //   }
    // };
  }
}
