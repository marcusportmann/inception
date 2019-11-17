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

import {Component, OnInit} from '@angular/core';
import {FormBuilder, FormControl, FormGroup, Validators} from '@angular/forms';
import {InceptionModule} from '../../inception.module';
import {SecurityService} from '../../services/security/security.service';
import {finalize, first, map} from 'rxjs/operators';
import {SessionService} from '../../services/session/session.service';
import {ActivatedRoute, Router} from '@angular/router';
import {Error} from '../../errors/error';
import {SpinnerService} from '../../services/layout/spinner.service';
import {
  PasswordExpiredError,
  SessionServiceError
} from '../../services/session/session.service.errors';
import {DialogService} from '../../services/dialog/dialog.service';
import {I18n} from '@ngx-translate/i18n-polyfill';
import {MatDialogRef} from '@angular/material/dialog';
import {ConfirmationDialogComponent} from '../../components/dialogs';
import {SystemUnavailableError} from '../../errors/system-unavailable-error';
import {AccessDeniedError} from '../../errors/access-denied-error';
import {Session} from '../../services/session/session';
import {Organization} from '../../services/security/organization';
import {Organizations} from '../../services/security/organizations';

/**
 * The LoginComponent class implements the login component.
 *
 * @author Marcus Portmann
 */
@Component({
  templateUrl: 'login.component.html'
})
export class LoginComponent implements OnInit {

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
    // Initialise the form
    this.loginForm = new FormGroup({
      username: new FormControl('Administrator', Validators.required),
      password: new FormControl('Password1', Validators.required)
    });
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

    //this.dialogService.showInformationDialog({message: 'This is an information message.'});

    //this.dialogService.showWarningDialog({message: 'This is a warning message.'});

    //this.dialogService.showErrorDialog(new Error('This is an error message.'));

    const dialogRef: MatDialogRef<ConfirmationDialogComponent, boolean> = this.dialogService.showConfirmationDialog(
      {message: 'Are you sure you want to delete the code category \'XXX\'?'});

    dialogRef.afterClosed()
      .pipe(first())
      .subscribe((confirmation: boolean | undefined) => {

        console.log('confirmation = ', confirmation);

        if (confirmation === true) {
          console.log('Confirmed deletion');
        }
      });

    // this.dialogService.showInformationDialog({message: this.i18n({id: '@@login_xxx', value: 'This is a test {{myVar}} !'}, {myVar: '^_^'})});

    // this.errorService.showConfirm('This is a title', 'This is a message');

    // console.log('Cancel clicked!');
    // let control = this.loginForm.get('username')
    // control.disabled ? control.enable() : control.disable()
  }

  ngOnInit(): void {
    this.activatedRoute.paramMap
      .pipe(first(), map(() => window.history.state))
      .subscribe((state) => {
        if (state.username) {
          this.loginForm.get('username')!.setValue(state.username);
        }
      });
  }

  onLogin(): void {
    if (this.loginForm.valid) {
      let username = this.loginForm.get('username')!.value;
      let password = this.loginForm.get('password')!.value;

      this.spinnerService.showSpinner();

      this.sessionService.login(username, password)
        .pipe(first())
        .subscribe((session: Session | null) => {
          if (session) {
            if (session.hasRole('Administrator')) {
              this.securityService.getOrganizations()
                .pipe(first(), finalize(() => this.spinnerService.hideSpinner()))
                .subscribe((organizations: Organizations) => {
                  if (organizations.total === 1) {
                    session.organization = organizations.organizations[0];
                    // noinspection JSIgnoredPromiseFromCall
                    this.router.navigate(['/']);
                  } else {
                    // noinspection JSIgnoredPromiseFromCall
                    this.router.navigate(['select-organization'], {
                      relativeTo: this.activatedRoute,
                      state: {organizations: organizations.organizations}
                    });
                  }
                }, (error: Error) => {
                  // noinspection SuspiciousTypeOfGuard
                  if ((error instanceof SessionServiceError) || (error instanceof AccessDeniedError) ||
                    (error instanceof SystemUnavailableError)) {
                    // noinspection JSIgnoredPromiseFromCall
                    this.router.navigateByUrl('/error/send-error-report', {state: {error}});
                  } else {
                    this.dialogService.showErrorDialog(error);
                  }
                })
            } else {
              this.securityService.getOrganizationsForUserDirectory(session.userDirectoryId)
                .pipe(first(), finalize(() => this.spinnerService.hideSpinner()))
                .subscribe((organizations: Organization[]) => {
                  if (organizations.length === 1) {
                    session.organization = organizations[0];
                    // noinspection JSIgnoredPromiseFromCall
                    this.router.navigate(['/']);
                  } else {
                    // noinspection JSIgnoredPromiseFromCall
                    this.router.navigate(['select-organization'],
                      {relativeTo: this.activatedRoute, state: {organizations}});
                  }
                }, (error: Error) => {
                  // noinspection SuspiciousTypeOfGuard
                  if ((error instanceof SessionServiceError) || (error instanceof AccessDeniedError) ||
                    (error instanceof SystemUnavailableError)) {
                    // noinspection JSIgnoredPromiseFromCall
                    this.router.navigateByUrl('/error/send-error-report', {state: {error}});
                  } else {
                    this.dialogService.showErrorDialog(error);
                  }
                });
            }
          } else {
            this.spinnerService.hideSpinner();

            // noinspection JSIgnoredPromiseFromCall
            this.router.navigate(['/']);
          }
        }, (error: Error) => {
          this.spinnerService.hideSpinner();

          // noinspection SuspiciousTypeOfGuard
          if ((error instanceof SessionServiceError) || (error instanceof AccessDeniedError) ||
            (error instanceof SystemUnavailableError)) {
            // noinspection JSIgnoredPromiseFromCall
            this.router.navigateByUrl('/error/send-error-report', {state: {error}});
          } else if (error instanceof PasswordExpiredError) {
            // noinspection JSIgnoredPromiseFromCall
            this.router.navigate(['expired-password'], {
              relativeTo: this.activatedRoute,
              state: {username: username}
            });
          } else {
            this.dialogService.showErrorDialog(error);
          }
        });
    }
  }
}
