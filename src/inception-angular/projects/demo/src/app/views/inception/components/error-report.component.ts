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

import {Component} from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';
import {
  AccessDeniedError, DialogService, Error, InvalidArgumentError, ServiceUnavailableError
} from 'ngx-inception/core';
import {first} from 'rxjs/operators';
import {TestService} from '../../../services/test.service';

/**
 * The Menu1Component class implements the menu 1 component.
 *
 * @author Marcus Portmann
 */
@Component({
  templateUrl: 'error-report.component.html'
})
export class ErrorReportComponent {

  constructor(private router: Router, private activatedRoute: ActivatedRoute,
              private dialogService: DialogService, private testService: TestService) {
  }

  testErrorReport(): void {
    this.testService.testExceptionHandling().pipe(first()).subscribe((result: boolean) => {
    }, (error: Error) => {
      // noinspection SuspiciousTypeOfGuard
      if ((error instanceof AccessDeniedError) || (error instanceof InvalidArgumentError) ||
        (error instanceof ServiceUnavailableError)) {
        // noinspection JSIgnoredPromiseFromCall
        this.router.navigateByUrl('/error/send-error-report', {state: {error}});
      } else {
        this.dialogService.showErrorDialog(error).afterClosed()
        .pipe(first())
        .subscribe(() => {
        });
      }
    });
  }
}
