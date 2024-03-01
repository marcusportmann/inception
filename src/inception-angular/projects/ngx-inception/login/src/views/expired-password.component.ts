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

import {Component, OnInit} from '@angular/core';
import {FormControl, FormGroup, Validators} from '@angular/forms';
import {MatDialogRef} from '@angular/material/dialog';
import {ActivatedRoute, Router} from '@angular/router';
import {
  AccessDeniedError, DialogService, Error, InformationDialogComponent, InvalidArgumentError,
  ServiceUnavailableError, SpinnerService
} from 'ngx-inception/core';
import {SecurityService} from 'ngx-inception/security';
import {finalize, first, map} from 'rxjs/operators';

/**
 * The ExpiredPasswordComponent class implements the expired password component.
 *
 * @author Marcus Portmann
 */
@Component({
  templateUrl: 'expired-password.component.html'
})
export class ExpiredPasswordComponent implements OnInit {

  confirmNewPasswordControl: FormControl;

  expiredPasswordForm: FormGroup;

  newPasswordControl: FormControl;

  passwordControl: FormControl;

  usernameControl: FormControl;

  /**
   * Constructs a new ExpiredPasswordComponent.
   *
   * @param router          The router.
   * @param activatedRoute  The activated route.
   * @param dialogService   The dialog service.
   * @param securityService The security service.
   * @param spinnerService  The spinner service.
   */
  constructor(private router: Router, private activatedRoute: ActivatedRoute,
              private dialogService: DialogService,
              private securityService: SecurityService, private spinnerService: SpinnerService) {

    // Initialise the form controls
    this.confirmNewPasswordControl = new FormControl('',
      [Validators.required, Validators.maxLength(100)]);
    this.newPasswordControl = new FormControl('', [Validators.required, Validators.maxLength(100)]);
    this.passwordControl = new FormControl('', [Validators.required, Validators.maxLength(100)]);
    this.usernameControl = new FormControl({
      value: '',
      disabled: true
    });

    // Initialise the form
    this.expiredPasswordForm = new FormGroup({
      confirmNewPassword: this.confirmNewPasswordControl,
      newPassword: this.newPasswordControl,
      password: this.passwordControl,
      username: this.usernameControl
    });
  }

  cancel(): void {
    // noinspection JSIgnoredPromiseFromCall
    this.router.navigate(['..'], {
      relativeTo: this.activatedRoute
    });
  }

  changePassword(): void {
    if (this.expiredPasswordForm.valid) {
      const username = this.usernameControl.value;
      const password = this.passwordControl.value;
      const newPassword = this.newPasswordControl.value;
      const confirmNewPassword = this.confirmNewPasswordControl.value;

      // Check that the password and confirmation password match
      if (newPassword !== confirmNewPassword) {
        this.dialogService.showErrorDialog(new Error('The passwords do not match.'));

        return;
      }

      this.spinnerService.showSpinner();

      this.securityService.changePassword(username, password, newPassword)
      .pipe(first(), finalize(() => this.spinnerService.hideSpinner()))
      .subscribe(() => {

        const dialogRef: MatDialogRef<InformationDialogComponent, boolean> = this.dialogService.showInformationDialog(
          {
            message: 'Your password was successfully changed.'
          });

        dialogRef.afterClosed()
        .pipe(first())
        .subscribe(() => {
          // noinspection JSIgnoredPromiseFromCall
          this.router.navigate(['..'], {
            relativeTo: this.activatedRoute,
            state: {username}
          });
        });
      }, (error: Error) => {
        // noinspection SuspiciousTypeOfGuard
        if ((error instanceof AccessDeniedError) || (error instanceof InvalidArgumentError) ||
          (error instanceof ServiceUnavailableError)) {
          // noinspection JSIgnoredPromiseFromCall
          this.router.navigateByUrl('/error/send-error-report', {state: {error}});
        } else {
          this.dialogService.showErrorDialog(error);
        }
      });
    }
  }

  ngOnInit(): void {
    this.activatedRoute.paramMap
    .pipe(first(), map(() => window.history.state))
    .subscribe((state) => {
      if (state.username) {
        this.usernameControl.setValue(state.username);
      } else {
        // noinspection JSIgnoredPromiseFromCall
        this.router.navigate(['..'], {
          relativeTo: this.activatedRoute
        });
      }
    });
  }
}
