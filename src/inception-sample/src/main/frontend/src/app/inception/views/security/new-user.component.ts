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
import {UserDirectoryType} from '../../services/security/user-directory-type';

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

  newUserForm: FormGroup;

  user?: User;

  userDirectoryType?: UserDirectoryType;

  constructor(private router: Router, private activatedRoute: ActivatedRoute,
              private formBuilder: FormBuilder, private i18n: I18n,
              private securityService: SecurityService,
              private dialogService: DialogService, private spinnerService: SpinnerService) {
    super();

    // Initialise the form
    this.newUserForm = new FormGroup({
      confirmPassword: new FormControl('', [Validators.required, Validators.maxLength(4000)]),
      email: new FormControl('', [Validators.maxLength(4000)]),
      firstName: new FormControl('', [Validators.required, Validators.maxLength(4000)]),
      lastName: new FormControl('', [Validators.required, Validators.maxLength(4000)]),
      mobileNumber: new FormControl('', [Validators.maxLength(4000)]),
      password: new FormControl('', [Validators.required, Validators.maxLength(4000)]),
      phoneNumber: new FormControl('', [Validators.maxLength(4000)]),
      username: new FormControl('', [Validators.required, Validators.maxLength(4000)])
    });
  }

  get backNavigation(): BackNavigation {
    const userDirectoryId = decodeURIComponent(
      this.activatedRoute.snapshot.paramMap.get('userDirectoryId')!);

    return new BackNavigation(this.i18n({
        id: '@@new_user_component_back_title',
        value: 'Users'
      }), ['../..'],
      {relativeTo: this.activatedRoute, state: {userDirectoryId}});
  }

  get title(): string {
    return this.i18n({
      id: '@@new_user_component_title',
      value: 'New User'
    })
  }

  ngAfterViewInit(): void {
    const userDirectoryId = decodeURIComponent(
      this.activatedRoute.snapshot.paramMap.get('userDirectoryId')!);

    // Retrieve the existing user and initialise the form fields
    this.spinnerService.showSpinner();

    this.securityService.getUserDirectoryTypeForUserDirectory(userDirectoryId)
      .pipe(first(), finalize(() => this.spinnerService.hideSpinner()))
      .subscribe((userDirectoryType: UserDirectoryType) => {
        this.userDirectoryType = userDirectoryType;

        this.user = new User(uuid(), userDirectoryId, '', '', '', '', '', '', '', 0,
          UserStatus.Active);

        if (this.userDirectoryType!.code === 'InternalUserDirectory') {
          this.newUserForm.addControl('expiredPassword', new FormControl(false));
          this.newUserForm.addControl('userLocked', new FormControl(false));
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
    const userDirectoryId = decodeURIComponent(
      this.activatedRoute.snapshot.paramMap.get('userDirectoryId')!);

    // noinspection JSIgnoredPromiseFromCall
    this.router.navigate(['../..'],
      {relativeTo: this.activatedRoute, state: {userDirectoryId}});
  }

  onOK(): void {
    if (this.user && this.newUserForm.valid) {
      // Check that the password and confirmation password match
      if (this.newUserForm.get('password')!.value !== this.newUserForm.get(
        'confirmPassword')!.value) {
        this.dialogService.showErrorDialog(new Error(this.i18n({
          id: '@@new_user_component_passwords_do_not_match',
          value: 'The passwords do not match.'
        })));

        return;
      }

      this.user.username = this.newUserForm.get('username')!.value;
      this.user.firstName = this.newUserForm.get('firstName')!.value;
      this.user.lastName = this.newUserForm.get('lastName')!.value;
      this.user.mobileNumber = this.newUserForm.get('mobileNumber')!.value;
      this.user.phoneNumber = this.newUserForm.get('phoneNumber')!.value;
      this.user.email = this.newUserForm.get('email')!.value;
      this.user.password = this.newUserForm.get('password')!.value;

      this.spinnerService.showSpinner();

      this.securityService.createUser(this.user,
        this.newUserForm.contains('expiredPassword') ? this.newUserForm.get(
          'expiredPassword')!.value : false,
        this.newUserForm.contains('userLocked') ? this.newUserForm.get('userLocked')!.value : false)
        .pipe(first(), finalize(() => this.spinnerService.hideSpinner()))
        .subscribe(() => {
          const userDirectoryId = decodeURIComponent(
            this.activatedRoute.snapshot.paramMap.get('userDirectoryId')!);

          // noinspection JSIgnoredPromiseFromCall
          this.router.navigate(['../..'],
            {relativeTo: this.activatedRoute, state: {userDirectoryId}});
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
