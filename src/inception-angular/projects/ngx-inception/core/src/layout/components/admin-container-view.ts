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

import { inject } from '@angular/core';
import { ActivatedRoute, NavigationExtras, Router } from '@angular/router';
import { Observable } from 'rxjs';
import { first } from 'rxjs/operators';
import { DialogService } from '../../dialogs/services/dialog.service';
import { AccessDeniedError, InvalidArgumentError, ServiceUnavailableError } from '../../errors';
import { SpinnerService } from '../services/spinner.service';
import { BackNavigation } from './back-navigation';

/**
 * The AdminContainerView class provides the abstract base class that all admin container view
 * components should be derived from.
 *
 * @author Marcus Portmann
 */
export abstract class AdminContainerView {
  protected readonly activatedRoute = inject(ActivatedRoute);

  protected readonly dialogService = inject(DialogService);

  protected readonly router = inject(Router);

  protected readonly spinnerService = inject(SpinnerService);

  /**
   * The back navigation for the admin container view.
   */
  // eslint-disable-next-line @typescript-eslint/class-literal-property-style
  get backNavigation(): BackNavigation | null {
    return null;
  }

  /**
   * Should the breadcrumbs be shown for the admin container view.
   *
   * @return True if the breadcrumbs should be shown for the admin container view or false
   *   otherwise.
   */
  // eslint-disable-next-line @typescript-eslint/class-literal-property-style
  get breadcrumbsVisible(): boolean {
    return true;
  }

  /**
   * Should the sidebar be minimized for the admin container view.
   *
   * @return True if the sidebar should be minimized for the admin container view or false
   *   otherwise.
   */
  // eslint-disable-next-line @typescript-eslint/class-literal-property-style
  get sidebarMinimized(): boolean | null {
    return null;
  }

  /**
   * The title for the admin container view.
   */
  abstract get title(): string | Observable<string>;

  /**
   * Handles errors raised by admin container views consistently.
   *
   * If the error is an {@link AccessDeniedError}, {@link InvalidArgumentError} or
   * {@link ServiceUnavailableError}, the user is navigated to the
   * `/error/send-error-report` route and the error is passed via navigation state.
   *
   *  For all other errors an error dialog is displayed using the {@link DialogService}.
   *  Once the dialog is closed:
   *  - if `navigateOnClose` is `true`, the user is navigated to the route specified
   *    by `navigationUrl`, relative to the current {@link ActivatedRoute};
   *  - if `navigateOnClose` is `false`, no additional navigation occurs.
   *
   * This helper should be called by derived admin container views when handling service or UI
   * errors to ensure a consistent user experience.
   *
   * @param error            The error that occurred.
   * @param navigateOnClose  Whether navigation should be performed after the error dialog is
   *                         closed.
   * @param navigateCommands The router navigation commands for the navigation that should be
   *                         performed after the error dialog is closed.
   * @param navigateExtras   The router navigation extras for the navigation that should be
   *                         performed after the error dialog is closed.
   */
  handleError(
    error: Error,
    navigateOnClose: boolean,
    navigateCommands?: string[],
    navigateExtras?: NavigationExtras
  ): void {
    if (
      error instanceof AccessDeniedError ||
      error instanceof InvalidArgumentError ||
      error instanceof ServiceUnavailableError
    ) {
      void this.router.navigateByUrl('/error/send-error-report', {
        state: { error }
      });
    } else {
      this.dialogService
        .showErrorDialog(error)
        .afterClosed()
        .pipe(first())
        .subscribe(() => {
          if (navigateOnClose && navigateCommands) {
            void this.router.navigate(navigateCommands, navigateExtras);
          }
        });
    }
  }
}
