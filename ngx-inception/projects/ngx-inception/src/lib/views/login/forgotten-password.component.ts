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

import {Component} from '@angular/core';
import {FormBuilder, FormControl, FormGroup, Validators} from '@angular/forms';
import {SecurityService} from '../../services/security/security.service';
import {finalize, first} from 'rxjs/operators';
import {ActivatedRoute, Router} from '@angular/router';
import {Error} from '../../errors/error';
import {SpinnerService} from '../../services/layout/spinner.service';
import {DialogService} from '../../services/dialog/dialog.service';
import {I18n} from '@ngx-translate/i18n-polyfill';
import {SystemUnavailableError} from '../../errors/system-unavailable-error';
import {AccessDeniedError} from '../../errors/access-denied-error';
import {SecurityServiceError} from '../../services/security/security.service.errors';
import {MatDialogRef} from '@angular/material/dialog';
import {InformationDialogComponent} from '../../components/dialogs';

/**
 * The ForgottenPasswordComponent class implements the forgotten password component.
 *
 * @author Marcus Portmann
 */
@Component({
  templateUrl: 'forgotten-password.component.html'
})
export class ForgottenPasswordComponent {

  forgottenPasswordForm: FormGroup;

  usernameFormControl: FormControl;

  /**
   * Constructs a new ForgottenPasswordComponent.
   *
   * @param router          The router.
   * @param activatedRoute  The activated route.
   * @param i18n            The internationalization service.
   * @param dialogService   The dialog service.
   * @param securityService The security service.
   * @param spinnerService  The spinner service.
   */
  constructor(private router: Router, private activatedRoute: ActivatedRoute, private i18n: I18n, private dialogService: DialogService,
              private securityService: SecurityService, private spinnerService: SpinnerService) {

    // Initialise the form controls
    this.usernameFormControl = new FormControl('', [Validators.required, Validators.maxLength(100)]);

    // Initialise the form
    this.forgottenPasswordForm = new FormGroup({
      username: this.usernameFormControl,
    });
  }

  cancel(): void {
    // noinspection JSIgnoredPromiseFromCall
    this.router.navigate(['..'], {
      relativeTo: this.activatedRoute
    });
  }

  resetPassword(): void {
    if (this.forgottenPasswordForm.valid) {
      const username = this.usernameFormControl.value;

      const resetPasswordUrl = window.location.href.substr(0, window.location.href.length - this.router.url.length) +
        '/login/reset-password';

      this.spinnerService.showSpinner();

      this.securityService.initiatePasswordReset(username, resetPasswordUrl)
        .pipe(first(), finalize(() => this.spinnerService.hideSpinner()))
        .subscribe(() => {
          const dialogRef: MatDialogRef<InformationDialogComponent, boolean> = this.dialogService.showInformationDialog({
            message: this.i18n({
              id: '@@login_forgotten_password_component_password_reset_initiated',
              value: 'The password reset process was initiated. Please check your e-mail to proceed.'
            })
          });

          dialogRef.afterClosed()
            .pipe(first())
            .subscribe((confirmation: boolean | undefined) => {
              // noinspection JSIgnoredPromiseFromCall
              this.router.navigate(['..'], {
                relativeTo: this.activatedRoute
              });
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
