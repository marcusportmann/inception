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

import { Component } from '@angular/core';
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
  forgottenPasswordForm: FormGroup;

  usernameControl: FormControl;

  constructor(
    private router: Router,
    private activatedRoute: ActivatedRoute,
    private dialogService: DialogService,
    private securityService: SecurityService,
    private spinnerService: SpinnerService
  ) {
    // Initialize the form controls
    this.usernameControl = new FormControl('', [Validators.required, Validators.maxLength(100)]);

    // Initialize the form
    this.forgottenPasswordForm = new FormGroup({
      username: this.usernameControl
    });
  }

  cancel(): void {
    // Navigate back
    // noinspection JSIgnoredPromiseFromCall
    this.router.navigate(['..'], { relativeTo: this.activatedRoute });
  }

  resetPassword(): void {
    if (this.forgottenPasswordForm.valid) {
      const username = this.usernameControl.value;

      const resetPasswordUrl = window.location.origin + '/#/login/reset-password';

      this.spinnerService.showSpinner();

      this.securityService
        .initiatePasswordReset(username, resetPasswordUrl)
        .pipe(
          first(),
          finalize(() => this.spinnerService.hideSpinner()),
          catchError((error) => this.handleError(error))
        )
        .subscribe(() => this.showSuccessDialog());
    }
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
