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

import {AfterViewInit, Component} from '@angular/core';
import {FormBuilder, FormControl, FormGroup, Validators} from '@angular/forms';
import {ActivatedRoute, Router} from '@angular/router';
import {DialogService} from '../../services/dialog/dialog.service';
import {SpinnerService} from '../../services/layout/spinner.service';
import {I18n} from '@ngx-translate/i18n-polyfill';
import {Error} from '../../errors/error';
import {finalize, first} from 'rxjs/operators';
import {SystemUnavailableError} from '../../errors/system-unavailable-error';
import {AccessDeniedError} from '../../errors/access-denied-error';
import {AdminContainerView} from '../../components/layout/admin-container-view';
import {BackNavigation} from '../../components/layout/back-navigation';
import {User} from '../../services/security/user';
import {SecurityServiceError} from '../../services/security/security.service.errors';
import {SecurityService} from '../../services/security/security.service';
import {combineLatest} from 'rxjs';
import {UserDirectoryCapabilities} from "../../services/security/user-directory-capabilities";
import {PasswordChangeReason} from "../../services/security/password-change-reason";

/**
 * The ResetUserPasswordComponent class implements the reset user password component.
 *
 * @author Marcus Portmann
 */
@Component({
  templateUrl: 'reset-user-password.component.html',
  styleUrls: ['reset-user-password.component.css'],
})
export class ResetUserPasswordComponent extends AdminContainerView implements AfterViewInit {

  resetUserPasswordForm: FormGroup;

  userDirectoryId: string;

  username: string;

  userDirectoryCapabilities?: UserDirectoryCapabilities;

  constructor(private router: Router, private activatedRoute: ActivatedRoute,
              private formBuilder: FormBuilder, private i18n: I18n,
              private securityService: SecurityService, private dialogService: DialogService,
              private spinnerService: SpinnerService) {
    super();

    // Retrieve parameters
    this.userDirectoryId =
      decodeURIComponent(this.activatedRoute.snapshot.paramMap.get('userDirectoryId')!);
    this.username = decodeURIComponent(this.activatedRoute.snapshot.paramMap.get('username')!);

    // Initialise the form
    this.resetUserPasswordForm = new FormGroup({
      confirmPassword: new FormControl('', [Validators.required, Validators.maxLength(100)]),
      firstName: new FormControl({
        value: '',
        disabled: true
      }),
      lastName: new FormControl({
        value: '',
        disabled: true
      }),
      password: new FormControl('', [Validators.required, Validators.maxLength(100)]),
      username: new FormControl({
        value: '',
        disabled: true
      })
    });
  }

  get backNavigation(): BackNavigation {
    return new BackNavigation(this.i18n({
      id: '@@reset_user_password_component_back_title',
      value: 'Users'
    }), ['../../..'], {
      relativeTo: this.activatedRoute,
      state: {userDirectoryId: this.userDirectoryId}
    });
  }

  get title(): string {
    return this.i18n({
      id: '@@reset_user_password_component_title',
      value: 'Reset User Password'
    })
  }

  ngAfterViewInit(): void {
    // Retrieve the existing user and initialise the form fields
    this.spinnerService.showSpinner();

    combineLatest([this.securityService.getUserDirectoryCapabilities(this.userDirectoryId),
      this.securityService.getUser(this.userDirectoryId, this.username)
    ])
      .pipe(first(), finalize(() => this.spinnerService.hideSpinner()))
      .subscribe((results: [UserDirectoryCapabilities, User]) => {
        this.userDirectoryCapabilities = results[0];

        this.resetUserPasswordForm.get('firstName')!.setValue(results[1].firstName);
        this.resetUserPasswordForm.get('lastName')!.setValue(results[1].lastName);
        this.resetUserPasswordForm.get('username')!.setValue(results[1].username);

        if (this.userDirectoryCapabilities!.supportsPasswordExpiry) {
          this.resetUserPasswordForm.addControl('expirePassword', new FormControl(false));
        }

        if (this.userDirectoryCapabilities!.supportsUserLocks) {
          this.resetUserPasswordForm.addControl('lockUser', new FormControl(false));
        }

        if (this.userDirectoryCapabilities!.supportsPasswordHistory) {
          this.resetUserPasswordForm.addControl('resetPasswordHistory', new FormControl(false));
        }
      }, (error: Error) => {
        // noinspection SuspiciousTypeOfGuard
        if ((error instanceof SecurityServiceError) || (error instanceof AccessDeniedError) ||
          (error instanceof SystemUnavailableError)) {
          // noinspection JSIgnoredPromiseFromCall
          this.router.navigateByUrl('/error/send-error-report', {state: {error}});
        } else {
          this.dialogService.showErrorDialog(error);
        }
      });
  }

  onCancel(): void {
    // noinspection JSIgnoredPromiseFromCall
    this.router.navigate(['../../..'], {
      relativeTo: this.activatedRoute,
      state: {userDirectoryId: this.userDirectoryId}
    });
  }

  onOK(): void {
    if (this.resetUserPasswordForm.valid) {
      // Check that the password and confirmation password match
      if (this.resetUserPasswordForm.get('password')!.value !==
        this.resetUserPasswordForm.get('confirmPassword')!.value) {
        this.dialogService.showErrorDialog(new Error(this.i18n({
          id: '@@reset_user_password_component_passwords_do_not_match',
          value: 'The passwords do not match.'
        })));

        return;
      }

      const password = this.resetUserPasswordForm.get('password')!.value;

      this.spinnerService.showSpinner();

      this.securityService.adminChangePassword(this.userDirectoryId, this.username, password,
        this.resetUserPasswordForm.contains('expirePassword') ?
          this.resetUserPasswordForm.get('expirePassword')!.value : false,
        this.resetUserPasswordForm.contains('lockUser') ?
          this.resetUserPasswordForm.get('lockUser')!.value : false,
        this.resetUserPasswordForm.contains('resetPasswordHistory') ?
          this.resetUserPasswordForm.get('resetPasswordHistory')!.value : false,
        PasswordChangeReason.Administrative)
        .pipe(first(), finalize(() => this.spinnerService.hideSpinner()))
        .subscribe(() => {
          // noinspection JSIgnoredPromiseFromCall
          this.router.navigate(['../../..'], {
            relativeTo: this.activatedRoute,
            state: {userDirectoryId: this.userDirectoryId}
          });
        }, (error: Error) => {
          // noinspection SuspiciousTypeOfGuard
          if ((error instanceof SecurityServiceError) || (error instanceof AccessDeniedError) ||
            (error instanceof SystemUnavailableError)) {
            // noinspection JSIgnoredPromiseFromCall
            this.router.navigateByUrl('/error/send-error-report', {state: {error}});
          } else {
            this.dialogService.showErrorDialog(error);
          }
        });
    }
  }
}
