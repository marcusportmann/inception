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

import { Component, inject, OnInit } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import {
  AdminContainerView, BackNavigation, CoreModule, GroupFormFieldComponent, ValidatedFormDirective
} from 'ngx-inception/core';
import { combineLatest } from 'rxjs';
import { finalize, first } from 'rxjs/operators';
import { SecurityService } from '../services/security.service';
import { User } from '../services/user';
import { UserDirectoryCapabilities } from '../services/user-directory-capabilities';

/**
 * The ResetUserPasswordComponent class implements the reset user password component.
 *
 * @author Marcus Portmann
 */
@Component({
  selector: 'inception-security-reset-user-password',
  imports: [CoreModule, ValidatedFormDirective, GroupFormFieldComponent],
  templateUrl: 'reset-user-password.component.html',
  styleUrls: ['reset-user-password.component.css']
})
export class ResetUserPasswordComponent extends AdminContainerView implements OnInit {
  readonly confirmPasswordControl: FormControl<string>;

  readonly expirePasswordControl: FormControl<boolean>;

  readonly lockUserControl: FormControl<boolean>;

  readonly nameControl: FormControl<string>;

  readonly passwordControl: FormControl<string>;

  readonly resetPasswordHistoryControl: FormControl<boolean>;

  readonly resetUserPasswordForm: FormGroup<{
    confirmPassword: FormControl<string>;
    name: FormControl<string>;
    password: FormControl<string>;
    resetPasswordHistory?: FormControl<boolean>;
    username: FormControl<string>;
    expirePassword?: FormControl<boolean>;
    lockUser?: FormControl<boolean>;
  }>;

  readonly title = $localize`:@@security_reset_user_password_title:Reset User Password`;

  userDirectoryCapabilities: UserDirectoryCapabilities | null = null;

  readonly userDirectoryId: string;

  readonly username: string;

  readonly usernameControl: FormControl<string>;

  private readonly securityService = inject(SecurityService);

  constructor() {
    super();

    // Retrieve the route parameters
    const userDirectoryId = this.activatedRoute.snapshot.paramMap.get('userDirectoryId');
    if (!userDirectoryId) {
      throw new globalThis.Error('No userDirectoryId route parameter found');
    }
    this.userDirectoryId = userDirectoryId;

    const username = this.activatedRoute.snapshot.paramMap.get('username');
    if (!username) {
      throw new globalThis.Error('No username route parameter found');
    }
    this.username = username;

    // Initialize the form controls
    this.confirmPasswordControl = new FormControl<string>('', {
      nonNullable: true,
      validators: [Validators.required, Validators.maxLength(100)]
    });

    this.expirePasswordControl = new FormControl<boolean>(false, { nonNullable: true });

    this.nameControl = new FormControl<string>(
      {
        value: '',
        disabled: true
      },
      { nonNullable: true }
    );

    this.lockUserControl = new FormControl<boolean>(false, { nonNullable: true });

    this.passwordControl = new FormControl<string>('', {
      nonNullable: true,
      validators: [Validators.required, Validators.maxLength(100)]
    });

    this.resetPasswordHistoryControl = new FormControl<boolean>(false, { nonNullable: true });

    this.usernameControl = new FormControl<string>(
      {
        value: '',
        disabled: true
      },
      { nonNullable: true }
    );

    // Initialize the form
    this.resetUserPasswordForm = new FormGroup({
      confirmPassword: this.confirmPasswordControl,
      name: this.nameControl,
      password: this.passwordControl,
      username: this.usernameControl
    });
  }

  override get backNavigation(): BackNavigation {
    return new BackNavigation(
      $localize`:@@security_reset_user_password_back_navigation:Users`,
      ['.'],
      {
        relativeTo: this.activatedRoute.parent?.parent,
        state: { userDirectoryId: this.userDirectoryId }
      }
    );
  }

  cancel(): void {
    void this.router.navigate(['.'], {
      relativeTo: this.activatedRoute.parent?.parent,
      state: { userDirectoryId: this.userDirectoryId }
    });
  }

  ngOnInit(): void {
    // Retrieve the existing user and initialize the form fields
    this.spinnerService.showSpinner();

    combineLatest([
      this.securityService.getUserDirectoryCapabilities(this.userDirectoryId),
      this.securityService.getUser(this.userDirectoryId, this.username)
    ])
      .pipe(
        first(),
        finalize(() => this.spinnerService.hideSpinner())
      )
      .subscribe({
        next: ([userDirectoryCapabilities, user]: [UserDirectoryCapabilities, User]) => {
          this.userDirectoryCapabilities = userDirectoryCapabilities;

          this.nameControl.setValue(user.name);
          this.usernameControl.setValue(user.username);

          if (this.userDirectoryCapabilities.supportsPasswordExpiry) {
            this.resetUserPasswordForm.addControl('expirePassword', this.expirePasswordControl);
          }

          if (this.userDirectoryCapabilities.supportsUserLocks) {
            this.resetUserPasswordForm.addControl('lockUser', this.lockUserControl);
          }

          if (this.userDirectoryCapabilities.supportsPasswordHistory) {
            this.resetUserPasswordForm.addControl(
              'resetPasswordHistory',
              this.resetPasswordHistoryControl
            );
          }
        },
        error: (error: Error) => this.handleError(error, false)
      });
  }

  ok(): void {
    if (!this.resetUserPasswordForm.valid) {
      return;
    }

    // Check that the password and confirmation password match
    if (this.passwordControl.value !== this.confirmPasswordControl.value) {
      this.dialogService.showErrorDialog(new Error('The passwords do not match.'));
      return;
    }

    const password = this.passwordControl.value;

    const expirePassword = this.resetUserPasswordForm.contains('expirePassword')
      ? this.expirePasswordControl.value
      : false;

    const lockUser = this.resetUserPasswordForm.contains('lockUser')
      ? this.lockUserControl.value
      : false;

    const resetPasswordHistory = this.resetUserPasswordForm.contains('resetPasswordHistory')
      ? this.resetPasswordHistoryControl.value
      : false;

    this.spinnerService.showSpinner();

    this.securityService
      .adminChangePassword(
        this.userDirectoryId,
        this.username,
        password,
        expirePassword,
        lockUser,
        resetPasswordHistory
      )
      .pipe(
        first(),
        finalize(() => this.spinnerService.hideSpinner())
      )
      .subscribe({
        next: () => {
          void this.router.navigate(['.'], {
            relativeTo: this.activatedRoute.parent?.parent,
            state: { userDirectoryId: this.userDirectoryId }
          });
        },
        error: (error: Error) => this.handleError(error, false)
      });
  }
}
