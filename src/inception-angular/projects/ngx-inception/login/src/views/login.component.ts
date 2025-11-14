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

import { AfterViewInit, Component, ElementRef, Inject, OnInit, ViewChild } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import {
  AccessDeniedError, CoreModule, DialogService, Error, INCEPTION_CONFIG, InceptionConfig,
  InvalidArgumentError, PasswordExpiredError, ServiceUnavailableError, Session, SessionService,
  SpinnerService, ValidatedFormDirective
} from 'ngx-inception/core';
import { SecurityService, Tenant, Tenants } from 'ngx-inception/security';
import { catchError, finalize, first, map, Observable, throwError } from 'rxjs';

/**
 * The LoginComponent class implements the login component.
 *
 * @author Marcus Portmann
 */
@Component({
  selector: 'inception-login-login',
  standalone: true,
  imports: [CoreModule, ValidatedFormDirective],
  templateUrl: 'login.component.html'
})
export class LoginComponent implements AfterViewInit, OnInit {
  loginForm: FormGroup;

  passwordControl: FormControl;

  usernameControl: FormControl;

  @ViewChild('usernameInput') usernameInput!: ElementRef<HTMLInputElement>;

  constructor(
    @Inject(INCEPTION_CONFIG) private config: InceptionConfig,
    private router: Router,
    private activatedRoute: ActivatedRoute,
    private dialogService: DialogService,
    private securityService: SecurityService,
    private sessionService: SessionService,
    private spinnerService: SpinnerService
  ) {
    // Initialize the form controls
    this.passwordControl = new FormControl(this.config.prepopulatedLoginPassword || '', [
      Validators.required,
      Validators.maxLength(100)
    ]);
    this.usernameControl = new FormControl(this.config.prepopulatedLoginUsername || '', [
      Validators.required,
      Validators.maxLength(100)
    ]);

    // Initialize the form
    this.loginForm = new FormGroup({
      password: this.passwordControl,
      username: this.usernameControl
    });
  }

  applicationVersion(): string {
    return this.config.applicationVersion || 'unknown';
  }

  forgotPassword(): void {
    // noinspection JSIgnoredPromiseFromCall
    this.router.navigate(['forgotten-password'], {
      relativeTo: this.activatedRoute
    });
  }

  isForgottenPasswordEnabled(): boolean {
    return this.config.forgottenPasswordEnabled;
  }

  login(): void {
    if (this.loginForm.valid) {
      const username = this.usernameControl.value;
      const password = this.passwordControl.value;

      this.spinnerService.showSpinner();

      this.sessionService
        .login(username, password)
        .pipe(
          first(),
          finalize(() => this.spinnerService.hideSpinner()),
          catchError((error: Error) => this.handleError(error, username))
        )
        .subscribe((session: Session | null) => {
          if (session) {
            this.handleSession(session);
          } else {
            // noinspection JSIgnoredPromiseFromCall
            this.router.navigate(['/']);
          }
        });
    }
  }

  ngAfterViewInit(): void {
    // Delay slightly to ensure it's safe
    setTimeout(() => {
      this.usernameInput.nativeElement.focus();
    });
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
        }
      });
  }

  private handleError(error: Error, username?: string): Observable<never> {
    if (
      error instanceof AccessDeniedError ||
      error instanceof InvalidArgumentError ||
      error instanceof ServiceUnavailableError
    ) {
      // noinspection JSIgnoredPromiseFromCall
      this.router.navigateByUrl('/error/send-error-report', {
        state: { error }
      });
    } else if (error instanceof PasswordExpiredError && username) {
      // noinspection JSIgnoredPromiseFromCall
      this.router.navigate(['expired-password'], {
        relativeTo: this.activatedRoute,
        state: { username }
      });
    } else {
      this.dialogService
        .showErrorDialog(error)
        .afterClosed()
        .pipe(first())
        .subscribe(() => {
          setTimeout(() => {
            this.usernameInput.nativeElement.focus();
          });
        });
    }
    return throwError(() => error);
  }

  private handleSession(session: Session): void {
    if (session.hasRole('Administrator')) {
      this.loadTenants(this.securityService.getTenants(), session);
    } else {
      this.loadTenants(
        this.securityService.getTenantsForUserDirectory(session.userDirectoryId),
        session
      );
    }
  }

  private loadTenants(tenants$: Observable<Tenants | Tenant[]>, session: Session): void {
    tenants$
      .pipe(
        first(),
        finalize(() => this.spinnerService.hideSpinner()),
        catchError((error) => this.handleError(error))
      )
      .subscribe((tenants) => {
        const tenantArray = Array.isArray(tenants) ? tenants : tenants.tenants;
        if (tenantArray.length === 1) {
          session.tenantId = tenantArray[0].id;
          // noinspection JSIgnoredPromiseFromCall
          this.router.navigate(['/']);
        } else {
          // noinspection JSIgnoredPromiseFromCall
          this.router.navigate(['select-tenant'], {
            relativeTo: this.activatedRoute,
            state: { tenants: tenantArray }
          });
        }
      });
  }
}
