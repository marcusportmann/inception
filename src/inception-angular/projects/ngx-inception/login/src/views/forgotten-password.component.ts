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

import { Component, inject } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { MatDialogRef } from '@angular/material/dialog';
import { ActivatedRoute, Router } from '@angular/router';
import {
  AccessDeniedError, CoreModule, DialogService, Error, InformationDialogComponent,
  InvalidArgumentError, ServiceUnavailableError, SpinnerService, ValidatedFormDirective
} from 'ngx-inception/core';
import { SecurityService } from 'ngx-inception/security';
import { Observable, throwError } from 'rxjs';
import { catchError, finalize, first } from 'rxjs/operators';

/**
 * The ForgottenPasswordComponent class implements the forgotten password component.
 *
 * @author Marcus Portmann
 */
@Component({
  selector: 'inception-login-forgotten-password',
  standalone: true,
  imports: [CoreModule, ValidatedFormDirective],
  templateUrl: 'forgotten-password.component.html'
})
export class ForgottenPasswordComponent {
  readonly forgottenPasswordForm: FormGroup<{
    username: FormControl<string>;
  }>;

  readonly usernameControl: FormControl<string>;

  private activatedRoute = inject(ActivatedRoute);

  private dialogService = inject(DialogService);

  private router = inject(Router);

  private securityService = inject(SecurityService);

  private spinnerService = inject(SpinnerService);

  constructor() {
    // Initialize the form controls
    this.usernameControl = new FormControl<string>('', {
      nonNullable: true,
      validators: [Validators.required, Validators.maxLength(100)]
    });

    // Initialize the form
    this.forgottenPasswordForm = new FormGroup({
      username: this.usernameControl
    });
  }

  cancel(): void {
    // noinspection JSIgnoredPromiseFromCall
    this.router.navigate(['..'], { relativeTo: this.activatedRoute });
  }

  resetPassword(): void {
    if (this.forgottenPasswordForm.invalid) {
      this.forgottenPasswordForm.markAllAsTouched();
      return;
    }

    const { username } = this.forgottenPasswordForm.getRawValue();

    const resetPasswordUrl = this.buildResetPasswordUrl();

    this.spinnerService.showSpinner();

    this.securityService
      .initiatePasswordReset(username, resetPasswordUrl)
      .pipe(
        first(),
        finalize(() => this.spinnerService.hideSpinner()),
        catchError((error: Error) => this.handleError(error))
      )
      .subscribe(() => this.showSuccessDialog());
  }

  /**
   * Build the reset password URL that will be embedded in the email.
   * Kept as a separate helper for clarity and easier future changes.
   */
  private buildResetPasswordUrl(): string {
    // Preserve existing hash-based routing behavior
    return `${window.location.origin}/#/login/reset-password`;
  }

  private handleError(error: Error): Observable<never> {
    if (
      error instanceof AccessDeniedError ||
      error instanceof InvalidArgumentError ||
      error instanceof ServiceUnavailableError
    ) {
      // Redirect to the error report page
      // noinspection JSIgnoredPromiseFromCall
      this.router.navigateByUrl('/error/send-error-report', {
        state: { error }
      });
    } else {
      // Show the error dialog and keep the user on this screen
      this.dialogService.showErrorDialog(error);
    }

    return throwError(() => error);
  }

  private showSuccessDialog(): void {
    const dialogRef: MatDialogRef<InformationDialogComponent, boolean> =
      this.dialogService.showInformationDialog({
        message: 'The password reset process was initiated. Please check your email to proceed.'
      });

    dialogRef
      .afterClosed()
      .pipe(first())
      .subscribe(() => {
        // Navigate back after the dialog is closed
        // noinspection JSIgnoredPromiseFromCall
        this.router.navigate(['..'], { relativeTo: this.activatedRoute });
      });
  }
}
