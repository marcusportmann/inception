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

  userDirectoryCapabilities?: UserDirectoryCapabilities;

  userDirectoryId: string;

  username: string;

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
    this.editUserForm = new FormGroup({
      email: new FormControl('', [Validators.maxLength(100)]),
      firstName: new FormControl('', [Validators.required, Validators.maxLength(100)]),
      lastName: new FormControl('', [Validators.required, Validators.maxLength(100)]),
      mobileNumber: new FormControl('', [Validators.maxLength(100)]),
      phoneNumber: new FormControl('', [Validators.maxLength(100)]),
      username: new FormControl({
        value: '',
        disabled: true
      })
    });
  }

  get backNavigation(): BackNavigation {
    return new BackNavigation(this.i18n({
      id: '@@security_edit_user_component_back_title',
      value: 'Users'
    }), ['../../..'], {
      relativeTo: this.activatedRoute,
      state: {userDirectoryId: this.userDirectoryId}
    });
  }

  get title(): string {
    return this.i18n({
      id: '@@security_edit_user_component_title',
      value: 'Edit User'
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

        this.user = results[1];

        this.editUserForm.get('email')!.setValue(results[1].email);
        this.editUserForm.get('firstName')!.setValue(results[1].firstName);
        this.editUserForm.get('lastName')!.setValue(results[1].lastName);
        this.editUserForm.get('mobileNumber')!.setValue(results[1].mobileNumber);
        this.editUserForm.get('phoneNumber')!.setValue(results[1].phoneNumber);
        this.editUserForm.get('username')!.setValue(results[1].username);

        if (this.userDirectoryCapabilities!.supportsPasswordExpiry) {
          this.editUserForm.addControl('expirePassword', new FormControl(false));
        }

        if (this.userDirectoryCapabilities!.supportsUserLocks) {
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
    // noinspection JSIgnoredPromiseFromCall
    this.router.navigate(['../../..'], {
      relativeTo: this.activatedRoute,
      state: {userDirectoryId: this.userDirectoryId}
    });
  }

  onOK(): void {
    if (this.user && this.editUserForm.valid) {
      this.user.firstName = this.editUserForm.get('firstName')!.value;
      this.user.lastName = this.editUserForm.get('lastName')!.value;
      this.user.mobileNumber = this.editUserForm.get('mobileNumber')!.value;
      this.user.phoneNumber = this.editUserForm.get('phoneNumber')!.value;
      this.user.email = this.editUserForm.get('email')!.value;

      this.spinnerService.showSpinner();

      this.securityService.updateUser(this.user, this.editUserForm.contains('expirePassword') ?
        this.editUserForm.get('expirePassword')!.value : false,
        this.editUserForm.contains('lockUser') ? this.editUserForm.get('lockUser')!.value : false)
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
