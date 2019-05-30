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
import {AbstractControl, FormBuilder, FormGroup, Validators} from '@angular/forms';
import {InceptionModule} from '../../inception.module';
import {SecurityService} from '../../services/security/security.service';
import {first} from 'rxjs/operators';
import {SessionService} from '../../services/session/session.service';
import {ActivatedRoute, Router} from '@angular/router';
import {Error} from '../../errors/error';
import {SpinnerService} from '../../services/layout/spinner.service';
import {SessionServiceError} from '../../services/session/session.service.errors';
import {DialogService} from '../../services/dialog/dialog.service';
import {I18n} from '@ngx-translate/i18n-polyfill';
import {MatDialogRef} from '@angular/material';
import {ConfirmationDialogComponent} from '../../components/dialogs';
import {SystemUnavailableError} from '../../errors/system-unavailable-error';
import {AccessDeniedError} from '../../errors/access-denied-error';

/**
 * The LoginComponent class implements the login component.
 *
 * @author Marcus Portmann
 */
@Component({
  templateUrl: 'login.component.html'
})
export class LoginComponent {

  loginForm: FormGroup;

  /**
   * Constructs a new LoginComponent.
   *
   * @param router          The router.
   * @param activatedRoute  The activated route.
   * @param formBuilder     The form builder.
   * @param i18n            The internationalization service.
   * @param dialogService   The dialog service.
   * @param securityService The security service.
   * @param sessionService  The session service.
   * @param spinnerService  The spinner service.
   */
  constructor(private router: Router, private activatedRoute: ActivatedRoute,
              private formBuilder: FormBuilder, private i18n: I18n,
              private dialogService: DialogService, private securityService: SecurityService,
              private sessionService: SessionService, private spinnerService: SpinnerService) {
    this.loginForm = this.formBuilder.group({
      // TODO: Implement pattern validator for username
      // tslint:disable-next-line
      username: ['Administrator', Validators.required],
      password: ['Password1', Validators.required]
    });
  }

  get passwordFormControl(): AbstractControl {
    return this.loginForm.get('password');
  }

  get usernameFormControl(): AbstractControl {
    return this.loginForm.get('username');
  }

  static isForgottenPasswordEnabled(): boolean {
    return InceptionModule.forgottenPasswordEnabled;
  }

  static isRegistrationEnabled(): boolean {
    return InceptionModule.registrationEnabled;
  }

  onForgotPassword(): void {
    // this.router.navigate(['/']);

    // let error: Error = new Error(new Date(), 'This is the error message', 'This is the error detail', 'This is the error stack trace');

    // this.dialogService.showInformationDialog({message: 'This is an information message.'});

    // this.dialogService.showWarningDialog({message: 'This is a warning message.'});

    // this.dialogService.showErrorDialog(new Error('This is an error message.'));

    const dialogRef: MatDialogRef<ConfirmationDialogComponent, boolean> = this.dialogService.showConfirmationDialog(
      {message: 'Are you sure you want to delete the code category \'XXX\'?'});

    dialogRef.afterClosed()
      .pipe(first())
      .subscribe((confirmation: boolean) => {

        console.log('confirmation = ', confirmation);

        if (confirmation === true) {
          console.log('Confirmed deletion');
        }
      });

    // this.dialogService.showInformationDialog({message: this.i18n({id: '@@xxx', value: 'This is a test {{myVar}} !'}, {myVar: '^_^'})});

    // this.errorService.showConfirm('This is a title', 'This is a message');

    // console.log('Cancel clicked!');
    // let control = this.loginForm.get('username')
    // control.disabled ? control.enable() : control.disable()
  }

  onLogin(): void {
    if (this.loginForm.valid) {
      this.spinnerService.showSpinner();

      this.sessionService.login(this.usernameFormControl.value, this.passwordFormControl.value)
        .pipe(first())
        .subscribe(session => {
          this.spinnerService.hideSpinner();

          if (session.organizations.length === 1) {
            // noinspection JSIgnoredPromiseFromCall
            this.router.navigate(['/']);
          } else {
            // noinspection JSIgnoredPromiseFromCall
            this.router.navigate(['select-organization'], {relativeTo: this.activatedRoute});
          }
        }, (error: Error) => {
          this.spinnerService.hideSpinner();

          if ((error instanceof SessionServiceError) || (error instanceof AccessDeniedError) ||
            (error instanceof SystemUnavailableError)) {
            // noinspection JSIgnoredPromiseFromCall
            this.router.navigateByUrl('/error/send-error-report', {state: {error: error}});
          } else {
            this.dialogService.showErrorDialog(error);
          }
        });
    }
  }
}
