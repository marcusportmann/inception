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
import {UserDirectoryType} from '../../services/security/user-directory-type';
import {UserDirectory} from '../../services/security/user-directory';

/**
 * The EditUserComponent class implements the edit user component.
 *
 * @author Marcus Portmann
 */
@Component({
  templateUrl: 'edit-user.component.html',
  styleUrls: ['edit-user.component.css'],
})
export class EditUserComponent extends AdminContainerView implements AfterViewInit {

  editUserForm: FormGroup;

  user?: User;

  userDirectoryType?: UserDirectoryType;

  constructor(private router: Router, private activatedRoute: ActivatedRoute,
              private formBuilder: FormBuilder, private i18n: I18n,
              private securityService: SecurityService,
              private dialogService: DialogService, private spinnerService: SpinnerService) {
    super();

    // Initialise the form
    this.editUserForm = new FormGroup({
      email: new FormControl('', [Validators.maxLength(4000)]),
      firstName: new FormControl('', [Validators.required, Validators.maxLength(4000)]),
      lastName: new FormControl('', [Validators.required, Validators.maxLength(4000)]),
      mobileNumber: new FormControl('', [Validators.maxLength(4000)]),
      phoneNumber: new FormControl('', [Validators.maxLength(4000)]),
      username: new FormControl({value: '', disabled: true})
    });
  }

  get backNavigation(): BackNavigation {
    const userDirectoryId = decodeURIComponent(
      this.activatedRoute.snapshot.paramMap.get('userDirectoryId')!);

    return new BackNavigation(this.i18n({
        id: '@@edit_user_component_back_title',
        value: 'Users'
      }), ['../../..'],
      {relativeTo: this.activatedRoute, state: {userDirectoryId}});
  }

  get title(): string {
    return this.i18n({
      id: '@@edit_user_component_title',
      value: 'Edit User'
    })
  }

  ngAfterViewInit(): void {
    const userDirectoryId = decodeURIComponent(
      this.activatedRoute.snapshot.paramMap.get('userDirectoryId')!);
    const username = decodeURIComponent(this.activatedRoute.snapshot.paramMap.get('username')!);

    // Retrieve the existing user and initialise the form fields
    this.spinnerService.showSpinner();

    combineLatest([
      this.securityService.getUserDirectoryTypeForUserDirectory(userDirectoryId),
      this.securityService.getUser(userDirectoryId, username)
    ])
      .pipe(first(), finalize(() => this.spinnerService.hideSpinner()))
      .subscribe((results: [UserDirectoryType, User]) => {
        this.userDirectoryType = results[0];

        this.user = results[1];

        this.editUserForm.get('email')!.setValue(results[1].email);
        this.editUserForm.get('firstName')!.setValue(results[1].firstName);
        this.editUserForm.get('lastName')!.setValue(results[1].lastName);
        this.editUserForm.get('mobileNumber')!.setValue(results[1].mobileNumber);
        this.editUserForm.get('phoneNumber')!.setValue(results[1].phoneNumber);
        this.editUserForm.get('username')!.setValue(results[1].username);

        if (this.userDirectoryType!.code === 'InternalUserDirectory') {
          this.editUserForm.addControl('expirePassword', new FormControl(false));
          this.editUserForm.addControl('lockUser', new FormControl(false));
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
    this.router.navigate(['../../..'],
      {relativeTo: this.activatedRoute, state: {userDirectoryId}});
  }

  onOK(): void {
    if (this.user && this.editUserForm.valid) {
      this.user.firstName = this.editUserForm.get('firstName')!.value;
      this.user.lastName = this.editUserForm.get('lastName')!.value;
      this.user.mobileNumber = this.editUserForm.get('mobileNumber')!.value;
      this.user.phoneNumber = this.editUserForm.get('phoneNumber')!.value;
      this.user.email = this.editUserForm.get('email')!.value;

      this.spinnerService.showSpinner();

      this.securityService.updateUser(this.user,
        this.editUserForm.contains('expirePassword') ? this.editUserForm.get(
          'expirePassword')!.value : false,
        this.editUserForm.contains('lockUser') ? this.editUserForm.get('lockUser')!.value : false)
        .pipe(first(), finalize(() => this.spinnerService.hideSpinner()))
        .subscribe(() => {
          const userDirectoryId = decodeURIComponent(
            this.activatedRoute.snapshot.paramMap.get('userDirectoryId')!);

          // noinspection JSIgnoredPromiseFromCall
          this.router.navigate(['../../..'],
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
