/*
 * Copyright Marcus Portmann
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
  AbstractControl, ControlValueAccessor, FormControl, FormGroup, NG_VALIDATORS, NG_VALUE_ACCESSOR,
  ValidationErrors, Validator, Validators
} from '@angular/forms';
import {UserDirectoryParameter} from '../services/user-directory-parameter';
import {UserDirectoryUtil} from '../services/user-directory-util';

@Component({
  selector: 'inception-internal-user-directory',
  templateUrl: 'internal-user-directory.component.html',
  styleUrls: ['internal-user-directory.component.css'],
  providers: [
    {
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

  maxFilteredGroupMembersControl: FormControl;

  maxFilteredGroupsControl: FormControl;

  maxFilteredUsersControl: FormControl;

  maxPasswordAttemptsControl: FormControl;

  passwordExpiryMonthsControl: FormControl;

  passwordHistoryMonthsControl: FormControl;

  constructor() {
    // Initialise the form controls
    this.maxFilteredGroupMembersControl = new FormControl('',
      [Validators.required, Validators.pattern('^\\d+$')]);
    this.maxFilteredGroupsControl = new FormControl('',
      [Validators.required, Validators.pattern('^\\d+$')]);
    this.maxFilteredUsersControl = new FormControl('',
      [Validators.required, Validators.pattern('^\\d+$')]);
    this.maxPasswordAttemptsControl = new FormControl('',
      [Validators.required, Validators.pattern('^\\d+$')]);
    this.passwordExpiryMonthsControl = new FormControl('',
      [Validators.required, Validators.pattern('^\\d+$')]);
    this.passwordHistoryMonthsControl = new FormControl('',
      [Validators.required, Validators.pattern('^\\d+$')]);

    // Initialise the form
    this.internalUserDirectoryForm = new FormGroup({
      maxFilteredGroupMembers: this.maxFilteredGroupMembersControl,
      maxFilteredGroups: this.maxFilteredGroupsControl,
      maxFilteredUsers: this.maxFilteredUsersControl,
      maxPasswordAttempts: this.maxPasswordAttemptsControl,
      passwordExpiryMonths: this.passwordExpiryMonthsControl,
      passwordHistoryMonths: this.passwordHistoryMonthsControl
    });
  }

  getParameters(): UserDirectoryParameter[] {
    const parameters: UserDirectoryParameter[] = [];

    UserDirectoryUtil.setParameter(parameters, 'MaxPasswordAttempts',
      this.maxPasswordAttemptsControl.value);
    UserDirectoryUtil.setParameter(parameters, 'PasswordExpiryMonths',
      this.passwordExpiryMonthsControl.value);
    UserDirectoryUtil.setParameter(parameters, 'PasswordHistoryMonths',
      this.passwordHistoryMonthsControl.value);
    UserDirectoryUtil.setParameter(parameters, 'MaxFilteredUsers',
      this.maxFilteredUsersControl.value);
    UserDirectoryUtil.setParameter(parameters, 'MaxFilteredGroups',
      this.maxFilteredGroupsControl.value);
    UserDirectoryUtil.setParameter(parameters, 'MaxFilteredGroupMembers',
      this.maxFilteredGroupMembersControl.value);

    return parameters;
  }

  // TODO: CHECK IF WE CAN REMOVE THIS -- MARCUS
  onTouched: () => void = () => {
  }

  // eslint-disable-next-line @typescript-eslint/no-explicit-any
  registerOnChange(fn: any): void {
    this.internalUserDirectoryForm.valueChanges.subscribe(fn);
  }

  // eslint-disable-next-line @typescript-eslint/no-explicit-any
  registerOnTouched(fn: any): void {
    this.onTouched = fn;
  }

  setDisabledState?(isDisabled: boolean): void {
    isDisabled ? this.internalUserDirectoryForm.disable() : this.internalUserDirectoryForm.enable();
  }

  setParameters(parameters: UserDirectoryParameter[]) {
    this.maxPasswordAttemptsControl.setValue(
      UserDirectoryUtil.hasParameter(parameters, 'MaxPasswordAttempts') ?
        UserDirectoryUtil.getParameter(parameters, 'MaxPasswordAttempts') : '5');
    this.passwordExpiryMonthsControl.setValue(
      UserDirectoryUtil.hasParameter(parameters, 'PasswordExpiryMonths') ?
        UserDirectoryUtil.getParameter(parameters, 'PasswordExpiryMonths') : '12');
    this.passwordHistoryMonthsControl.setValue(
      UserDirectoryUtil.hasParameter(parameters, 'PasswordHistoryMonths') ?
        UserDirectoryUtil.getParameter(parameters, 'PasswordHistoryMonths') : '24');
    this.maxFilteredUsersControl.setValue(
      UserDirectoryUtil.hasParameter(parameters, 'MaxFilteredUsers') ?
        UserDirectoryUtil.getParameter(parameters, 'MaxFilteredUsers') : '100');
    this.maxFilteredGroupsControl.setValue(
      UserDirectoryUtil.hasParameter(parameters, 'MaxFilteredGroups') ?
        UserDirectoryUtil.getParameter(parameters, 'MaxFilteredGroups') : '100');
    this.maxFilteredGroupMembersControl.setValue(
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

  // eslint-disable-next-line @typescript-eslint/no-explicit-any
  writeValue(val: any): void {
    if (val) {
      this.internalUserDirectoryForm.setValue(val, {emitEvent: false});
    }
  }
}

