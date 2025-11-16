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

import { Component, forwardRef } from '@angular/core';
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
import { CoreModule } from 'ngx-inception/core';
import { UserDirectoryParameter } from '../services/user-directory-parameter';
import { UserDirectoryUtil } from '../services/user-directory-util';

@Component({
  selector: 'inception-security-internal-user-directory',
  standalone: true,
  imports: [CoreModule],
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
export class InternalUserDirectoryComponent
  implements ControlValueAccessor, Validator
{
  internalUserDirectoryForm: FormGroup;

  maxFilteredGroupMembersControl: FormControl;
  maxFilteredGroupsControl: FormControl;
  maxFilteredUsersControl: FormControl;
  maxPasswordAttemptsControl: FormControl;
  passwordExpiryMonthsControl: FormControl;
  passwordHistoryMonthsControl: FormControl;

  // ---- ControlValueAccessor callbacks ----
  // The external value is the parameter list
  private onChange: (value: UserDirectoryParameter[] | null) => void = () => { /* empty */ };
  private onTouched: () => void = () => { /* empty */ };

  constructor() {
    // Initialize the form controls
    this.maxFilteredGroupMembersControl = new FormControl('', [
      Validators.required,
      Validators.pattern('^\\d+$')
    ]);
    this.maxFilteredGroupsControl = new FormControl('', [
      Validators.required,
      Validators.pattern('^\\d+$')
    ]);
    this.maxFilteredUsersControl = new FormControl('', [
      Validators.required,
      Validators.pattern('^\\d+$')
    ]);
    this.maxPasswordAttemptsControl = new FormControl('', [
      Validators.required,
      Validators.pattern('^\\d+$')
    ]);
    this.passwordExpiryMonthsControl = new FormControl('', [
      Validators.required,
      Validators.pattern('^\\d+$')
    ]);
    this.passwordHistoryMonthsControl = new FormControl('', [
      Validators.required,
      Validators.pattern('^\\d+$')
    ]);

    // Initialize the form
    this.internalUserDirectoryForm = new FormGroup({
      maxFilteredGroupMembers: this.maxFilteredGroupMembersControl,
      maxFilteredGroups: this.maxFilteredGroupsControl,
      maxFilteredUsers: this.maxFilteredUsersControl,
      maxPasswordAttempts: this.maxPasswordAttemptsControl,
      passwordExpiryMonths: this.passwordExpiryMonthsControl,
      passwordHistoryMonths: this.passwordHistoryMonthsControl
    });

    // Whenever the internal form changes, propagate the parameter list
    this.internalUserDirectoryForm.valueChanges.subscribe(() => {
      this.onChange(this.getParameters());
    });
  }

  // Expose parameters as the "value" of the control
  getParameters(): UserDirectoryParameter[] {
    const parameters: UserDirectoryParameter[] = [];

    UserDirectoryUtil.setParameter(
      parameters,
      'MaxPasswordAttempts',
      this.maxPasswordAttemptsControl.value
    );
    UserDirectoryUtil.setParameter(
      parameters,
      'PasswordExpiryMonths',
      this.passwordExpiryMonthsControl.value
    );
    UserDirectoryUtil.setParameter(
      parameters,
      'PasswordHistoryMonths',
      this.passwordHistoryMonthsControl.value
    );
    UserDirectoryUtil.setParameter(
      parameters,
      'MaxFilteredUsers',
      this.maxFilteredUsersControl.value
    );
    UserDirectoryUtil.setParameter(
      parameters,
      'MaxFilteredGroups',
      this.maxFilteredGroupsControl.value
    );
    UserDirectoryUtil.setParameter(
      parameters,
      'MaxFilteredGroupMembers',
      this.maxFilteredGroupMembersControl.value
    );

    return parameters;
  }

  // Helper to populate from an existing parameter list
  setParameters(parameters: UserDirectoryParameter[]): void {
    this.maxPasswordAttemptsControl.setValue(
      UserDirectoryUtil.hasParameter(parameters, 'MaxPasswordAttempts')
        ? UserDirectoryUtil.getParameter(parameters, 'MaxPasswordAttempts')
        : '5'
    );
    this.passwordExpiryMonthsControl.setValue(
      UserDirectoryUtil.hasParameter(parameters, 'PasswordExpiryMonths')
        ? UserDirectoryUtil.getParameter(parameters, 'PasswordExpiryMonths')
        : '12'
    );
    this.passwordHistoryMonthsControl.setValue(
      UserDirectoryUtil.hasParameter(parameters, 'PasswordHistoryMonths')
        ? UserDirectoryUtil.getParameter(parameters, 'PasswordHistoryMonths')
        : '24'
    );
    this.maxFilteredUsersControl.setValue(
      UserDirectoryUtil.hasParameter(parameters, 'MaxFilteredUsers')
        ? UserDirectoryUtil.getParameter(parameters, 'MaxFilteredUsers')
        : '100'
    );
    this.maxFilteredGroupsControl.setValue(
      UserDirectoryUtil.hasParameter(parameters, 'MaxFilteredGroups')
        ? UserDirectoryUtil.getParameter(parameters, 'MaxFilteredGroups')
        : '100'
    );
    this.maxFilteredGroupMembersControl.setValue(
      UserDirectoryUtil.hasParameter(parameters, 'MaxFilteredGroupMembers')
        ? UserDirectoryUtil.getParameter(parameters, 'MaxFilteredGroupMembers')
        : '100'
    );

    // Reset the touch/pristine state when writing from outside
    this.internalUserDirectoryForm.markAsPristine();
    this.internalUserDirectoryForm.markAsUntouched();

    // keep outer control value in sync as well
    this.onChange(this.getParameters());
  }

  // ---- ControlValueAccessor implementation ----

  // Angular calls this to push a value INTO the component
  // Expected value: UserDirectoryParameter[] | null
  // eslint-disable-next-line @typescript-eslint/no-explicit-any
  writeValue(val: any): void {
    if (val && Array.isArray(val)) {
      this.setParameters(val);
    }
    // If null/undefined/empty, we keep current defaults
  }

  // eslint-disable-next-line @typescript-eslint/no-explicit-any
  registerOnChange(fn: any): void {
    this.onChange = fn;
  }

  // eslint-disable-next-line @typescript-eslint/no-explicit-any
  registerOnTouched(fn: any): void {
    this.onTouched = fn;
  }

  setDisabledState?(isDisabled: boolean): void {
    if (isDisabled) {
      this.internalUserDirectoryForm.disable();
    } else {
      this.internalUserDirectoryForm.enable();
    }
  }

  // Call this from the template on blur if you want proper "touched" behavior
  markAsTouched(): void {
    if (this.onTouched) {
      this.onTouched();
    }
  }

  // ---- Validator implementation ----

  validate(_control: AbstractControl): ValidationErrors | null {
    void _control;

    return this.internalUserDirectoryForm.valid
      ? null
      : {
        invalidForm: {
          valid: false,
          message: 'internalUserDirectoryForm fields are invalid'
        }
      };
  }
}
