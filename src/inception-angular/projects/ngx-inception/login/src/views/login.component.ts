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

import { AfterViewInit, Component, ElementRef, OnInit, ViewChild, inject } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import {
  AccessDeniedError, CoreModule, DialogService, Error, INCEPTION_CONFIG, InceptionConfig,
  InvalidArgumentError, PasswordExpiredError, ServiceUnavailableError, Session, SessionService,
  SpinnerService, ValidatedFormDirective
} from 'ngx-inception/core';
import { SecurityService, Tenant, Tenants } from 'ngx-inception/security';
import { catchError, finalize, first, map, Observable, of, switchMap, throwError } from 'rxjs';

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
  private config = inject<InceptionConfig>(INCEPTION_CONFIG);
  private router = inject(Router);
  private activatedRoute = inject(ActivatedRoute);
  private dialogService = inject(DialogService);
  private securityService = inject(SecurityService);
  private sessionService = inject(SessionService);
  private spinnerService = inject(SpinnerService);

  readonly loginForm: FormGroup;

  readonly passwordControl: FormControl<string>;

  readonly usernameControl: FormControl<string>;

  @ViewChild('usernameInput') usernameInput!: ElementRef<HTMLInputElement>;

  constructor() {
    this.passwordControl = new FormControl<string>(this.config.prepopulatedLoginPassword || '', {
      nonNullable: true,
      validators: [Validators.required, Validators.maxLength(100)]
    });

    this.usernameControl = new FormControl<string>(this.config.prepopulatedLoginUsername || '', {
      nonNullable: true,
      validators: [Validators.required, Validators.maxLength(100)]
    });

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
    if (this.loginForm.invalid) {
      this.loginForm.markAllAsTouched();
      this.usernameInput?.nativeElement.focus();
      return;
    }

    const { username, password } = this.loginForm.getRawValue() as {
      username: string;
      password: string;
    };

    this.spinnerService.showSpinner();

    this.sessionService
      .login(username, password)
      .pipe(
        first(),
        // Once logged in, resolve the tenant list for this session
        switchMap((session: Session | null) => {
          if (!session) {
            return of<{ session: Session | null; tenants: Tenant[] }>({
              session: null,
              tenants: []
            });
          }

          return this.getTenantsForSession(session).pipe(map((tenants) => ({ session, tenants })));
        }),
        finalize(() => this.spinnerService.hideSpinner()),
        catchError((error: Error) => this.handleError(error, username))
      )
      .subscribe(({ session, tenants }) => {
        if (!session) {
          // No session, just go home
          // noinspection JSIgnoredPromiseFromCall
          this.router.navigate(['/']);
          return;
        }

        this.handleTenantSelection(session, tenants);
      });
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
        if (state && state.username) {
          this.usernameControl.setValue(state.username);
        }
      });
  }

  /**
   * Resolve the list of tenants for the given session.
   */
  private getTenantsForSession(session: Session): Observable<Tenant[]> {
    if (session.hasRole('Administrator')) {
      // Administrators see all tenants
      return this.securityService.getTenants().pipe(map((tenants: Tenants) => tenants.tenants));
    }

    // Non-admins see only tenants for their user directory
    return this.securityService.getTenantsForUserDirectory(session.userDirectoryId);
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

  /**
   * Decide what to do with the resolved tenants: set tenant on the session and route appropriately.
   */
  private handleTenantSelection(session: Session, tenants: Tenant[]): void {
    if (!tenants || tenants.length === 0) {
      // No tenants – just navigate to root; backend / guards can handle it.
      // noinspection JSIgnoredPromiseFromCall
      this.router.navigate(['/']);
      return;
    }

    if (tenants.length === 1) {
      session.tenantId = tenants[0].id;
      // noinspection JSIgnoredPromiseFromCall
      this.router.navigate(['/']);
      return;
    }

    // Multiple tenants – let the user choose
    // noinspection JSIgnoredPromiseFromCall
    this.router.navigate(['select-tenant'], {
      relativeTo: this.activatedRoute,
      state: { tenants }
    });
  }
}
