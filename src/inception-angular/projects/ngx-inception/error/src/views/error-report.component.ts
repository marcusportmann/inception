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

import { AfterViewInit, Component } from '@angular/core';
import { FormControl, FormGroup } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import {
  AccessDeniedError,
  AdminContainerView,
  BackNavigation,
  DialogService,
  Error,
  InvalidArgumentError,
  ServiceUnavailableError,
  SpinnerService
} from 'ngx-inception/core';
import { finalize, first } from 'rxjs/operators';
import { ErrorReport } from '../services/error-report';
import { ErrorService } from '../services/error.service';

/**
 * The ErrorReportComponent class implements the error report component.
 *
 * @author Marcus Portmann
 */
@Component({
  templateUrl: 'error-report.component.html',
  styleUrls: ['error-report.component.css'],
  standalone: false
})
export class ErrorReportComponent
  extends AdminContainerView
  implements AfterViewInit
{
  applicationIdControl: FormControl;

  applicationVersionControl: FormControl;

  createdControl: FormControl;

  descriptionControl: FormControl;

  detailControl: FormControl;

  deviceIdControl: FormControl;

  errorReport: ErrorReport | null = null;

  errorReportForm: FormGroup;

  errorReportId: string;

  feedbackControl: FormControl;

  idControl: FormControl;

  whoControl: FormControl;

  constructor(
    private router: Router,
    private activatedRoute: ActivatedRoute,
    private errorService: ErrorService,
    private dialogService: DialogService,
    private spinnerService: SpinnerService
  ) {
    super();

    // Retrieve the route parameters
    const errorReportId =
      this.activatedRoute.snapshot.paramMap.get('errorReportId');
    if (!errorReportId) {
      throw new Error('No errorReportId route parameter found');
    }
    this.errorReportId = decodeURIComponent(errorReportId);

    // Initialize form controls
    this.applicationIdControl = new FormControl({
      value: '',
      disabled: true
    });
    this.applicationVersionControl = new FormControl({
      value: '',
      disabled: true
    });
    this.createdControl = new FormControl({
      value: '',
      disabled: true
    });
    this.descriptionControl = new FormControl({
      value: '',
      disabled: true
    });
    this.detailControl = new FormControl({
      value: '',
      disabled: true
    });
    this.deviceIdControl = new FormControl({
      value: '',
      disabled: true
    });
    this.feedbackControl = new FormControl({
      value: '',
      disabled: true
    });
    this.idControl = new FormControl({
      value: '',
      disabled: true
    });
    this.whoControl = new FormControl({
      value: '',
      disabled: true
    });

    // Initialize form group
    this.errorReportForm = new FormGroup({
      applicationId: this.applicationIdControl,
      applicationVersion: this.applicationVersionControl,
      created: this.createdControl,
      description: this.descriptionControl,
      detail: this.detailControl,
      deviceId: this.deviceIdControl,
      feedback: this.feedbackControl,
      id: this.idControl,
      who: this.whoControl
    });
  }

  override get backNavigation(): BackNavigation {
    return new BackNavigation(
      $localize`:@@error_error_report_back_navigation:Error Reports`,
      ['..'],
      { relativeTo: this.activatedRoute }
    );
  }

  get title(): string {
    return $localize`:@@error_error_report_title:View Error Report`;
  }

  ngAfterViewInit(): void {
    // Retrieve the error report and populate form controls
    this.spinnerService.showSpinner();

    this.errorService
      .getErrorReport(this.errorReportId)
      .pipe(
        first(),
        finalize(() => this.spinnerService.hideSpinner())
      )
      .subscribe({
        next: (errorReport: ErrorReport) => this.populateForm(errorReport),
        error: (error: Error) => this.handleError(error)
      });
  }

  ok(): void {
    // noinspection JSIgnoredPromiseFromCall
    this.router.navigate(['..'], { relativeTo: this.activatedRoute });
  }

  private handleError(error: Error): void {
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
      this.dialogService
        .showErrorDialog(error)
        .afterClosed()
        .pipe(first())
        .subscribe(() => {
          // noinspection JSIgnoredPromiseFromCall
          this.router.navigate(['../..'], { relativeTo: this.activatedRoute });
        });
    }
  }

  private populateForm(errorReport: ErrorReport): void {
    this.errorReport = errorReport;

    this.applicationIdControl.setValue(errorReport.applicationId);
    this.applicationVersionControl.setValue(errorReport.applicationVersion);
    this.createdControl.setValue(errorReport.created);
    this.descriptionControl.setValue(errorReport.description);
    this.detailControl.setValue(errorReport.detail || 'No detail');
    this.deviceIdControl.setValue(errorReport.deviceId || 'No device ID');
    this.feedbackControl.setValue(errorReport.feedback || 'No feedback');
    this.idControl.setValue(errorReport.id);
    this.whoControl.setValue(errorReport.who);
  }
}
