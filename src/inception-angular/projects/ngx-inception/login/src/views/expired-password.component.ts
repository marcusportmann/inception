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

import { Component, OnInit, inject } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { MatDialogRef } from '@angular/material/dialog';
import { ActivatedRoute, Router } from '@angular/router';
import {
  AccessDeniedError, CoreModule, DialogService, Error, InformationDialogComponent,
  InvalidArgumentError, ServiceUnavailableError, SpinnerService, ValidatedFormDirective
} from 'ngx-inception/core';
import { SecurityService } from 'ngx-inception/security';
import { Observable, throwError } from 'rxjs';
import { catchError, finalize, first, map } from 'rxjs/operators';

/**
 * The ExpiredPasswordComponent class implements the expired password component.
 *
 * @author Marcus Portmann
 */
@Component({
  selector: 'inception-login-expired-password',
  standalone: true,
  imports: [CoreModule, ValidatedFormDirective],
  templateUrl: 'expired-password.component.html'
})
export class ExpiredPasswordComponent implements OnInit {
  private router = inject(Router);
  private activatedRoute = inject(ActivatedRoute);
  private dialogService = inject(DialogService);
  private securityService = inject(SecurityService);
  private spinnerService = inject(SpinnerService);

  confirmNewPasswordControl: FormControl;

  expiredPasswordForm: FormGroup;

  newPasswordControl: FormControl;

  passwordControl: FormControl;

  usernameControl: FormControl;

  constructor() {
    // Initialize form controls
    this.confirmNewPasswordControl = new FormControl('', [
      Validators.required,
      Validators.maxLength(100)
    ]);
    this.newPasswordControl = new FormControl('', [Validators.required, Validators.maxLength(100)]);
    this.passwordControl = new FormControl('', [Validators.required, Validators.maxLength(100)]);
    this.usernameControl = new FormControl({
      value: '',
      disabled: true
    });

    // Initialize form
    this.expiredPasswordForm = new FormGroup({
      confirmNewPassword: this.confirmNewPasswordControl,
      newPassword: this.newPasswordControl,
      password: this.passwordControl,
      username: this.usernameControl
    });
  }

  cancel(): void {
    // noinspection JSIgnoredPromiseFromCall
    this.router.navigate(['..'], { relativeTo: this.activatedRoute });
  }

  changePassword(): void {
    if (this.expiredPasswordForm.valid) {
      const username = this.usernameControl.value;
      const password = this.passwordControl.value;
      const newPassword = this.newPasswordControl.value;
      const confirmNewPassword = this.confirmNewPasswordControl.value;

      if (newPassword !== confirmNewPassword) {
        this.dialogService.showErrorDialog(new Error('The passwords do not match.'));
        return;
      }

      this.spinnerService.showSpinner();

      this.securityService
        .changePassword(username, password, newPassword)
        .pipe(
          first(),
          finalize(() => this.spinnerService.hideSpinner()),
          catchError((error) => this.handleError(error))
        )
        .subscribe(() => {
          this.showSuccessDialog(username);
        });
    }
  }

  ngOnInit(): void {
    this.activatedRoute.paramMap
      .pipe(
        first(),
        map(() => window.history.state)
      )
      .subscribe((state) => {
        if (state.username) {
          this.usernameControl.setValue(state.username);
        } else {
          // Redirect if no username in state
          // noinspection JSIgnoredPromiseFromCall
          this.router.navigate(['..'], { relativeTo: this.activatedRoute });
        }
      });
  }

  private handleError(error: Error): Observable<never> {
    if (
      error instanceof AccessDeniedError ||
      error instanceof InvalidArgumentError ||
      error instanceof ServiceUnavailableError
    ) {
      // Redirect on critical errors
      // noinspection JSIgnoredPromiseFromCall
      this.router.navigateByUrl('/error/send-error-report', {
        state: { error }
      });
    } else {
      this.dialogService.showErrorDialog(error);
    }
    return throwError(() => error);
  }

  private showSuccessDialog(username: string): void {
    const dialogRef: MatDialogRef<InformationDialogComponent, boolean> =
      this.dialogService.showInformationDialog({
        message: 'Your password was successfully changed.'
      });

    dialogRef
      .afterClosed()
      .pipe(first())
      .subscribe(() => {
        // noinspection JSIgnoredPromiseFromCall
        this.router.navigate(['..'], {
          relativeTo: this.activatedRoute,
          state: { username }
        });
      });
  }
}
