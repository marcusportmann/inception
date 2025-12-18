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

import { AfterViewInit, Component, inject } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import {
  AdminContainerView,
  BackNavigation,
  Base64,
  CoreModule,
  Error,
  FileUploadComponent,
  FileValidator,
  ValidatedFormDirective
} from 'ngx-inception/core';
import { finalize, first } from 'rxjs/operators';
import { ReportDefinition } from '../services/report-definition';
import { ReportingService } from '../services/reporting.service';

/**
 * The EditReportDefinitionComponent class implements the edit report definition component.
 *
 * @author Marcus Portmann
 */
@Component({
  selector: 'inception-reporting-edit-report-definition',
  imports: [CoreModule, ValidatedFormDirective, FileUploadComponent],
  templateUrl: 'edit-report-definition.component.html',
  styleUrls: ['edit-report-definition.component.css']
})
export class EditReportDefinitionComponent extends AdminContainerView implements AfterViewInit {
  editReportDefinitionForm: FormGroup;

  idControl: FormControl;

  nameControl: FormControl;

  reportDefinition: ReportDefinition | null = null;

  reportDefinitionId: string;

  templateControl: FormControl;

  readonly title = $localize`:@@reporting_edit_report_definition_title:Edit Report Definition`;

  private reportingService = inject(ReportingService);

  constructor() {
    super();

    // Retrieve the route parameters
    const reportDefinitionId = this.activatedRoute.snapshot.paramMap.get('reportDefinitionId');

    if (!reportDefinitionId) {
      throw new Error('No reportDefinitionId route parameter found');
    }

    this.reportDefinitionId = decodeURIComponent(reportDefinitionId);

    // Initialize the form controls
    this.idControl = new FormControl(
      {
        value: '',
        disabled: true
      },
      [Validators.required, Validators.maxLength(100)]
    );
    this.nameControl = new FormControl('', [Validators.required, Validators.maxLength(100)]);
    this.templateControl = new FormControl('', [
      Validators.required,
      FileValidator.minSize(1),
      FileValidator.maxSize(ReportingService.MAX_TEMPLATE_SIZE)
    ]);

    // Initialize the form
    this.editReportDefinitionForm = new FormGroup({
      id: this.idControl,
      name: this.nameControl,
      template: this.templateControl
    });
  }

  override get backNavigation(): BackNavigation {
    return new BackNavigation(
      $localize`:@@reporting_edit_report_definition_back_navigation:Report Definitions`,
      ['../..'],
      { relativeTo: this.activatedRoute }
    );
  }

  cancel(): void {
    // noinspection JSIgnoredPromiseFromCall
    this.router.navigate(['../..'], { relativeTo: this.activatedRoute });
  }

  ngAfterViewInit(): void {
    // Retrieve the existing report definition and initialize the form controls
    this.spinnerService.showSpinner();

    this.reportingService
      .getReportDefinition(this.reportDefinitionId)
      .pipe(
        first(),
        finalize(() => this.spinnerService.hideSpinner())
      )
      .subscribe({
        next: (reportDefinition: ReportDefinition) => {
          this.reportDefinition = reportDefinition;
          this.idControl.setValue(reportDefinition.id);
          this.nameControl.setValue(reportDefinition.name);
        },
        error: (error: Error) => this.handleError(error, true, '../..')
      });
  }

  ok(): void {
    if (!this.reportDefinition || !this.editReportDefinitionForm.valid) {
      return;
    }

    const files = this.templateControl.value as File[] | null;

    if (!files || !files[0]) {
      console.log('No template file selected for the report definition.');
      return;
    }

    const reportDefinition = this.reportDefinition;
    const fileReader: FileReader = new FileReader();

    fileReader.onloadend = () => {
      const result = fileReader.result;

      if (!(result instanceof ArrayBuffer)) {
        console.log('Failed to read the template file for the report definition (' + result + ')');
        return;
      }

      const base64: string = Base64.encode(result as ArrayBuffer);

      reportDefinition.name = this.nameControl.value;
      reportDefinition.template = base64;

      this.spinnerService.showSpinner();

      this.reportingService
        .updateReportDefinition(reportDefinition)
        .pipe(
          first(),
          finalize(() => this.spinnerService.hideSpinner())
        )
        .subscribe({
          next: () => {
            // noinspection JSIgnoredPromiseFromCall
            this.router.navigate(['../..'], {
              relativeTo: this.activatedRoute
            });
          },
          error: (error: Error) => this.handleError(error, false)
        });
    };

    fileReader.readAsArrayBuffer(files[0]);
  }
}
