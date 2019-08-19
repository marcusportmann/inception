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
import {SecurityService} from '../../services/security/security.service';
import {SecurityServiceError} from '../../services/security/security.service.errors';
import {UserStatus} from '../../services/security/user-status';
import {v4 as uuid} from 'uuid';

/**
 * The NewUserComponent class implements the new user component.
 *
 * @author Marcus Portmann
 */
@Component({
  templateUrl: 'new-user.component.html',
  styleUrls: ['new-user.component.css'],
})
export class NewUserComponent extends AdminContainerView implements AfterViewInit {

  confirmPasswordFormControl: FormControl;

  emailFormControl: FormControl;

  expiredPasswordFormControl: FormControl;

  firstNameFormControl: FormControl;

  lastNameFormControl: FormControl;

  mobileNumberFormControl: FormControl;

  newUserForm: FormGroup;

  passwordFormControl: FormControl;

  phoneNumberFormControl: FormControl;

  user?: User;

  userDirectoryId: string;

  userLockedFormControl: FormControl;

  usernameFormControl: FormControl;

  constructor(private router: Router, private activatedRoute: ActivatedRoute,
              private formBuilder: FormBuilder, private i18n: I18n,
              private securityService: SecurityService,
              private dialogService: DialogService, private spinnerService: SpinnerService) {
    super();

    // Retrieve the parameters
    this.userDirectoryId = decodeURIComponent(
      this.activatedRoute.snapshot.paramMap.get('userDirectoryId')!);

    // Initialise form controls
    this.confirmPasswordFormControl = new FormControl('',
      [Validators.required, Validators.maxLength(4000)]);

    this.emailFormControl = new FormControl('',
      [Validators.maxLength(4000)]);

    this.expiredPasswordFormControl = new FormControl(false);

    this.firstNameFormControl = new FormControl('',
      [Validators.maxLength(4000)]);

    this.lastNameFormControl = new FormControl('',
      [Validators.maxLength(4000)]);

    this.mobileNumberFormControl = new FormControl('',
      [Validators.maxLength(4000)]);

    this.passwordFormControl = new FormControl('',
      [Validators.required, Validators.maxLength(4000)]);

    this.phoneNumberFormControl = new FormControl('',
      [Validators.maxLength(4000)]);

    this.userLockedFormControl = new FormControl(false);

    this.usernameFormControl = new FormControl('',
      [Validators.required, Validators.maxLength(4000)]);

    // Initialise form
    this.newUserForm = new FormGroup({
      confirmPassword: this.confirmPasswordFormControl,
      email: this.emailFormControl,
      expiredPassword: this.expiredPasswordFormControl,
      firstName: this.firstNameFormControl,
      lastName: this.lastNameFormControl,
      mobileNumber: this.mobileNumberFormControl,
      password: this.passwordFormControl,
      phoneNumber: this.phoneNumberFormControl,
      userLocked: this.userLockedFormControl,
      username: this.usernameFormControl
    });
  }

  get backNavigation(): BackNavigation {
    return new BackNavigation(this.i18n({
        id: '@@new_user_component_back_title',
        value: 'Users'
      }), ['../..'],
      {relativeTo: this.activatedRoute, state: {userDirectoryId: this.userDirectoryId}});
  }

  get title(): string {
    return this.i18n({
      id: '@@new_user_component_title',
      value: 'New User'
    })
  }

  ngAfterViewInit(): void {
    // Construct the new user
    this.user = new User(uuid(), this.userDirectoryId, '', '', '', '', '', '', '', 0,
      UserStatus.Active);

    // Initialise the form controls
  }

  onCancel(): void {
    // noinspection JSIgnoredPromiseFromCall
    this.router.navigate(['../..'],
      {relativeTo: this.activatedRoute, state: {userDirectoryId: this.userDirectoryId}});
  }

  onOK(): void {
    if (this.user && this.newUserForm.valid) {
      // Check that the password and confirmation password match
      if (this.passwordFormControl.value !== this.confirmPasswordFormControl.value) {
        this.dialogService.showErrorDialog(new Error(this.i18n({
          id: '@@new_user_component_passwords_do_not_match',
          value: 'The passwords do not match.'
        })));

        return;
      }

      this.user.username = this.usernameFormControl.value;
      this.user.firstName = this.firstNameFormControl.value;
      this.user.lastName = this.lastNameFormControl.value;
      this.user.mobileNumber = this.mobileNumberFormControl.value;
      this.user.phoneNumber = this.phoneNumberFormControl.value;
      this.user.email = this.emailFormControl.value;
      this.user.password = this.passwordFormControl.value;

      this.spinnerService.showSpinner();

      this.securityService.createUser(this.user, this.expiredPasswordFormControl.value,
        this.userLockedFormControl.value)
        .pipe(first(), finalize(() => this.spinnerService.hideSpinner()))
        .subscribe(() => {
          // noinspection JSIgnoredPromiseFromCall
          this.router.navigate(['../..'],
            {relativeTo: this.activatedRoute, state: {userDirectoryId: this.userDirectoryId}});
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
