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

import { Component, inject, OnInit } from '@angular/core';
import { FormControl, FormGroup } from '@angular/forms';
import { AdminContainerView, BackNavigation, CoreModule } from 'ngx-inception/core';
import { finalize, first } from 'rxjs/operators';
import { ErrorReport } from '../services/error-report';
import { ErrorService } from '../services/error.service';

/**
 * The ErrorReportComponent class implements the error report component.
 *
 * @author Marcus Portmann
 */
@Component({
  selector: 'inception-error-error-report',
  standalone: true,
  imports: [CoreModule],
  templateUrl: 'error-report.component.html',
  styleUrls: ['error-report.component.css']
})
export class ErrorReportComponent extends AdminContainerView implements OnInit {
  readonly applicationIdControl: FormControl<string>;

  readonly applicationVersionControl: FormControl<string>;

  readonly createdControl: FormControl<Date>;

  readonly descriptionControl: FormControl<string>;

  readonly detailControl: FormControl<string>;

  readonly deviceIdControl: FormControl<string>;

  errorReport: ErrorReport | null = null;

  readonly errorReportForm: FormGroup<{
    applicationId: FormControl<string>;
    applicationVersion: FormControl<string>;
    created: FormControl<Date>;
    description: FormControl<string>;
    detail: FormControl<string>;
    deviceId: FormControl<string>;
    feedback: FormControl<string>;
    id: FormControl<string>;
    who: FormControl<string>;
  }>;

  readonly errorReportId: string;

  readonly feedbackControl: FormControl<string>;

  readonly idControl: FormControl<string>;

  readonly title = $localize`:@@error_error_report_title:View Error Report`;

  readonly whoControl: FormControl<string>;

  private readonly errorService = inject(ErrorService);

  constructor() {
    super();

    // Retrieve the route parameters
    const errorReportId = this.activatedRoute.snapshot.paramMap.get('errorReportId');
    if (!errorReportId) {
      throw new globalThis.Error('No errorReportId route parameter found');
    }
    this.errorReportId = errorReportId;

    // Initialize form controls
    this.applicationIdControl = new FormControl<string>(
      {
        value: '',
        disabled: true
      },
      { nonNullable: true }
    );

    this.applicationVersionControl = new FormControl<string>(
      {
        value: '',
        disabled: true
      },
      { nonNullable: true }
    );

    this.createdControl = new FormControl<Date>(
      {
        value: new Date(),
        disabled: true
      },
      { nonNullable: true }
    );

    this.descriptionControl = new FormControl<string>(
      {
        value: '',
        disabled: true
      },
      { nonNullable: true }
    );

    this.detailControl = new FormControl<string>(
      {
        value: '',
        disabled: true
      },
      { nonNullable: true }
    );

    this.deviceIdControl = new FormControl<string>(
      {
        value: '',
        disabled: true
      },
      { nonNullable: true }
    );

    this.feedbackControl = new FormControl<string>(
      {
        value: '',
        disabled: true
      },
      { nonNullable: true }
    );

    this.idControl = new FormControl<string>(
      {
        value: '',
        disabled: true
      },
      { nonNullable: true }
    );

    this.whoControl = new FormControl<string>(
      {
        value: '',
        disabled: true
      },
      { nonNullable: true }
    );

    // Initialize the form group
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
      ['.'],
      { relativeTo: this.activatedRoute.parent }
    );
  }

  ngOnInit(): void {
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
        error: (error: Error) =>
          this.handleError(error, true, ['.'], { relativeTo: this.activatedRoute.parent?.parent })
      });
  }

  ok(): void {
    void this.router.navigate(['.'], { relativeTo: this.activatedRoute.parent });
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
    this.whoControl.setValue(errorReport.who || 'No user');
  }
}
