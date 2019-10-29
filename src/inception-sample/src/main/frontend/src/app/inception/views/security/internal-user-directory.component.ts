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

import {Component, forwardRef} from '@angular/core';
import {
  AbstractControl,
  ControlValueAccessor,
  FormControl,
  FormGroup,
  NG_VALIDATORS,
  NG_VALUE_ACCESSOR,
  ValidationErrors,
  Validator,
  Validators
} from '@angular/forms';
import {UserDirectoryParameter} from '../../services/security/user-directory-parameter';
import {UserDirectoryUtil} from '../../services/security/user-directory-util';

@Component({
  selector: 'internal-user-directory',
  templateUrl: 'internal-user-directory.component.html',
  styleUrls: ['internal-user-directory.component.css'],
  providers: [{
    provide: NG_VALUE_ACCESSOR,
    useExisting: forwardRef(() => InternalUserDirectoryComponent),
    multi: true
  }, {
    provide: NG_VALIDATORS,
    useExisting: forwardRef(() => InternalUserDirectoryComponent),
    multi: true
  }
  ]
})
export class InternalUserDirectoryComponent implements ControlValueAccessor, Validator {

  internalUserDirectoryForm: FormGroup;

  constructor() {
    // Initialise the form
    this.internalUserDirectoryForm = new FormGroup({
      maxFilteredGroupMembers: new FormControl('', [Validators.required, Validators.pattern('^\\d+$')]),
      maxFilteredUsers: new FormControl('', [Validators.required, Validators.pattern('^\\d+$')]),
      maxFilteredGroups: new FormControl('', [Validators.required, Validators.pattern('^\\d+$')]),
      maxPasswordAttempts: new FormControl('', [Validators.required, Validators.pattern('^\\d+$')]),
      passwordExpiryMonths: new FormControl('',
        [Validators.required, Validators.pattern('^\\d+$')]),
      passwordHistoryMonths: new FormControl('',
        [Validators.required, Validators.pattern('^\\d+$')])
    });
  }

  getParameters(): UserDirectoryParameter[] {
    const parameters: UserDirectoryParameter[] = [];

    UserDirectoryUtil.setParameter(parameters, 'MaxPasswordAttempts',
      this.internalUserDirectoryForm.get('maxPasswordAttempts')!.value);
    UserDirectoryUtil.setParameter(parameters, 'PasswordExpiryMonths',
      this.internalUserDirectoryForm.get('passwordExpiryMonths')!.value);
    UserDirectoryUtil.setParameter(parameters, 'PasswordHistoryMonths',
      this.internalUserDirectoryForm.get('passwordHistoryMonths')!.value);
    UserDirectoryUtil.setParameter(parameters, 'MaxFilteredUsers',
      this.internalUserDirectoryForm.get('maxFilteredUsers')!.value);
    UserDirectoryUtil.setParameter(parameters, 'MaxFilteredGroups',
      this.internalUserDirectoryForm.get('maxFilteredGroups')!.value);
    UserDirectoryUtil.setParameter(parameters, 'MaxFilteredGroupMembers',
      this.internalUserDirectoryForm.get('maxFilteredGroupMembers')!.value);

    return parameters;
  }

  onTouched: () => void = () => {
  };

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

  setParameters(parameters: UserDirectoryParameter[]) {
    this.internalUserDirectoryForm.get('maxPasswordAttempts')!.setValue(
      UserDirectoryUtil.hasParameter(parameters, 'MaxPasswordAttempts') ?
        UserDirectoryUtil.getParameter(parameters, 'MaxPasswordAttempts') : '5');
    this.internalUserDirectoryForm.get('passwordExpiryMonths')!.setValue(
      UserDirectoryUtil.hasParameter(parameters, 'PasswordExpiryMonths') ?
        UserDirectoryUtil.getParameter(parameters, 'PasswordExpiryMonths') : '12');
    this.internalUserDirectoryForm.get('passwordHistoryMonths')!.setValue(
      UserDirectoryUtil.hasParameter(parameters, 'PasswordHistoryMonths') ?
        UserDirectoryUtil.getParameter(parameters, 'PasswordHistoryMonths') : '24');
    this.internalUserDirectoryForm.get('maxFilteredUsers')!.setValue(
      UserDirectoryUtil.hasParameter(parameters, 'MaxFilteredUsers') ?
        UserDirectoryUtil.getParameter(parameters, 'MaxFilteredUsers') : '100');
    this.internalUserDirectoryForm.get('maxFilteredGroups')!.setValue(
      UserDirectoryUtil.hasParameter(parameters, 'MaxFilteredGroups') ?
        UserDirectoryUtil.getParameter(parameters, 'MaxFilteredGroups') : '100');
    this.internalUserDirectoryForm.get('maxFilteredGroupMembers')!.setValue(
      UserDirectoryUtil.hasParameter(parameters, 'MaxFilteredGroupMembers') ?
        UserDirectoryUtil.getParameter(parameters, 'MaxFilteredGroupMembers') : '100');
  }

  validate(c: AbstractControl): ValidationErrors | null {
    return this.internalUserDirectoryForm.valid ? null : {
      invalidForm: {
        valid: false,
        message: 'internalUserDirectoryForm fields are invalid'
      }
    };
  }

  // tslint:disable-next-line:no-any
  writeValue(val: any): void {
    if (val) {
      this.internalUserDirectoryForm.setValue(val, {emitEvent: false});
    }
  }
}

