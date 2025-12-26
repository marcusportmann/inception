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
import { FormControl, FormGroup, Validators } from '@angular/forms';
import {
  AdminContainerView, BackNavigation, Base64, CoreModule, FileUploadComponent, FileValidator,
  ValidatedFormDirective
} from 'ngx-inception/core';
import { finalize, first } from 'rxjs/operators';
import { ReportDefinition } from '../services/report-definition';
import { ReportingService } from '../services/reporting.service';

/**
 * The NewReportDefinitionComponent class implements the new report definition component.
 *
 * @author Marcus Portmann
 */
@Component({
  selector: 'inception-reporting-new-report-definition',
  imports: [CoreModule, ValidatedFormDirective, FileUploadComponent],
  templateUrl: 'new-report-definition.component.html',
  styleUrls: ['new-report-definition.component.css']
})
export class NewReportDefinitionComponent extends AdminContainerView implements OnInit {
  readonly idControl: FormControl<string>;

  readonly nameControl: FormControl<string>;

  readonly newReportDefinitionForm: FormGroup<{
    id: FormControl<string>;
    name: FormControl<string>;
    template: FormControl<File[] | null>;
  }>;

  reportDefinition: ReportDefinition | null = null;

  readonly templateControl: FormControl<File[] | null>;

  readonly title = $localize`:@@reporting_new_report_definition_title:New Report Definition`;

  private readonly reportingService = inject(ReportingService);

  constructor() {
    super();

    // Initialize the form controls
    this.idControl = new FormControl<string>('', {
      nonNullable: true,
      validators: [Validators.required]
    });

    this.nameControl = new FormControl<string>('', {
      nonNullable: true,
      validators: [Validators.required, Validators.maxLength(100)]
    });

    this.templateControl = new FormControl<File[] | null>(null, [
      Validators.required,
      FileValidator.minSize(1),
      FileValidator.maxSize(ReportingService.MAX_TEMPLATE_SIZE)
    ]);

    // Initialize the form
    this.newReportDefinitionForm = new FormGroup({
      id: this.idControl,
      name: this.nameControl,
      template: this.templateControl
    });
  }

  override get backNavigation(): BackNavigation {
    return new BackNavigation(
      $localize`:@@reporting_new_report_definition_back_navigation:Report Definitions`,
      ['.'],
      { relativeTo: this.activatedRoute.parent }
    );
  }

  cancel(): void {
    void this.router.navigate(['.'], { relativeTo: this.activatedRoute.parent });
  }

  ngOnInit(): void {
    this.reportDefinition = new ReportDefinition('', '', '');
  }

  ok(): void {
    if (!this.reportDefinition || !this.newReportDefinitionForm.valid) {
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

      reportDefinition.id = this.idControl.value;
      reportDefinition.name = this.nameControl.value;
      reportDefinition.template = base64;

      this.spinnerService.showSpinner();

      this.reportingService
        .createReportDefinition(reportDefinition)
        .pipe(
          first(),
          finalize(() => this.spinnerService.hideSpinner())
        )
        .subscribe({
          next: () => {
            void this.router.navigate(['.'], {
              relativeTo: this.activatedRoute.parent
            });
          },
          error: (error: Error) => this.handleError(error, false)
        });
    };

    fileReader.readAsArrayBuffer(files[0]);
  }
}
