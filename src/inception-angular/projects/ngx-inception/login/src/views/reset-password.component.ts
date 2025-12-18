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
import {
  AbstractControl, FormControl, FormGroup, ValidationErrors, Validators
} from '@angular/forms';
import { MatDialogRef } from '@angular/material/dialog';
import { ActivatedRoute, Params, Router } from '@angular/router';
import {
  AccessDeniedError, CoreModule, DialogService, Error, InformationDialogComponent,
  InvalidArgumentError, ServiceUnavailableError, SpinnerService, ValidatedFormDirective
} from 'ngx-inception/core';
import { SecurityService } from 'ngx-inception/security';
import { catchError, finalize, first, Observable, throwError } from 'rxjs';

/**
 * The ResetPasswordComponent class implements the reset password component.
 *
 * @author Marcus Portmann
 */
@Component({
  selector: 'inception-login-reset-password',
  standalone: true,
  imports: [CoreModule, ValidatedFormDirective],
  templateUrl: 'reset-password.component.html'
})
export class ResetPasswordComponent implements OnInit {
  private router = inject(Router);
  private activatedRoute = inject(ActivatedRoute);
  private dialogService = inject(DialogService);
  private securityService = inject(SecurityService);
  private spinnerService = inject(SpinnerService);

  readonly confirmNewPasswordControl: FormControl<string>;

  readonly newPasswordControl: FormControl<string>;

  readonly resetPasswordForm: FormGroup<{
    username: FormControl<string>;
    newPassword: FormControl<string>;
    confirmNewPassword: FormControl<string>;
  }>;

  readonly usernameControl: FormControl<string>;

  private securityCode: string | null = null;

  constructor() {
    this.newPasswordControl = new FormControl<string>('', {
      nonNullable: true,
      validators: [Validators.required, Validators.maxLength(100)]
    });

    this.confirmNewPasswordControl = new FormControl<string>('', {
      nonNullable: true,
      validators: [Validators.required, Validators.maxLength(100)]
    });

    this.usernameControl = new FormControl<string>(
      { value: '', disabled: true },
      { nonNullable: true }
    );

    this.resetPasswordForm = new FormGroup(
      {
        username: this.usernameControl,
        newPassword: this.newPasswordControl,
        confirmNewPassword: this.confirmNewPasswordControl
      },
      { validators: ResetPasswordComponent.passwordsMatchValidator }
    );
  }

  cancel(): void {
    // noinspection JSIgnoredPromiseFromCall
    this.router.navigate(['..'], { relativeTo: this.activatedRoute });
  }

  ngOnInit(): void {
    this.activatedRoute.queryParams.pipe(first()).subscribe((params: Params) => {
      const username = params['username'];
      const securityCode = params['securityCode'];

      if (!username || !securityCode) {
        this.dialogService
        .showErrorDialog(
          new Error(
            'The password reset link is invalid or has expired. Please request a new reset link.'
          )
        )
        .afterClosed()
        .pipe(first())
        .subscribe(() => {
          // noinspection JSIgnoredPromiseFromCall
          this.router.navigate(['..'], { relativeTo: this.activatedRoute });
        });
        return;
      }

      this.usernameControl.setValue(username);
      this.securityCode = securityCode;
    });
  }

  resetPassword(): void {
    if (this.resetPasswordForm.invalid || !this.securityCode) {
      this.resetPasswordForm.markAllAsTouched();
      return;
    }

    const { username, newPassword } = this.resetPasswordForm.getRawValue();

    this.spinnerService.showSpinner();

    this.securityService
    .resetPassword(username, newPassword, this.securityCode)
    .pipe(
      first(),
      finalize(() => this.spinnerService.hideSpinner()),
      catchError((error: Error) => this.handleError(error))
    )
    .subscribe(() => this.showSuccessDialog(username));
  }

  private static passwordsMatchValidator(
    control: AbstractControl
  ): ValidationErrors | null {
    const newPassword = control.get('newPassword')?.value;
    const confirmNewPassword = control.get('confirmNewPassword')?.value;

    if (!newPassword || !confirmNewPassword) {
      return null;
    }

    return newPassword === confirmNewPassword
      ? null
      : { passwordsMismatch: true };
  }

  private handleError(error: Error): Observable<never> {
    if (
      error instanceof AccessDeniedError ||
      error instanceof InvalidArgumentError ||
      error instanceof ServiceUnavailableError
    ) {
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
