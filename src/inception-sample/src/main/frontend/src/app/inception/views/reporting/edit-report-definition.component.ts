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

import {AfterViewInit, Component} from '@angular/core';
import {FormBuilder, FormControl, FormGroup, Validators} from '@angular/forms';
import {ActivatedRoute, Router} from '@angular/router';
import {DialogService} from '../../services/dialog/dialog.service';
import {SpinnerService} from '../../services/layout/spinner.service';
import {I18n} from '@ngx-translate/i18n-polyfill';
import {Error} from '../../errors/error';
import {finalize, first} from 'rxjs/operators';
import {SystemUnavailableError} from '../../errors/system-unavailable-error';
import {AccessDeniedError} from '../../errors/access-denied-error';
import {AdminContainerView} from '../../components/layout/admin-container-view';
import {BackNavigation} from '../../components/layout/back-navigation';
import {ReportDefinition} from "../../services/reporting/report-definition";
import {ReportingService} from "../../services/reporting/reporting.service";
import {ReportingServiceError} from "../../services/reporting/reporting.service.errors";
import {Base64} from "../../util";
import {FileValidator} from "../../validators/file-validator";

/**
 * The EditReportDefinitionComponent class implements the edit report definition component.
 *
 * @author Marcus Portmann
 */
@Component({
  templateUrl: 'edit-report-definition.component.html',
  styleUrls: ['edit-report-definition.component.css'],
})
export class EditReportDefinitionComponent extends AdminContainerView implements AfterViewInit {

  reportDefinition?: ReportDefinition;

  reportDefinitionId: string;

  editReportDefinitionForm: FormGroup;

  constructor(private router: Router, private activatedRoute: ActivatedRoute,
              private formBuilder: FormBuilder, private i18n: I18n,
              private reportingService: ReportingService, private dialogService: DialogService,
              private spinnerService: SpinnerService) {
    super();

    // Retrieve parameters
    this.reportDefinitionId =
      decodeURIComponent(this.activatedRoute.snapshot.paramMap.get('reportDefinitionId')!);

    // Initialise the form
    this.editReportDefinitionForm = new FormGroup({
      id: new FormControl({
        value: '',
        disabled: true
      }, [Validators.required, Validators.maxLength(100)]),
      name: new FormControl('', [Validators.required, Validators.maxLength(100)]),
      template: new FormControl('', [Validators.required, FileValidator.minSize(1),
        FileValidator.maxSize(ReportingService.MAX_TEMPLATE_SIZE)
      ])
    });
  }

  get backNavigation(): BackNavigation {
    return new BackNavigation(this.i18n({
      id: '@@reporting_edit_report_definition_component_back_title',
      value: 'Report Definitions'
    }), ['../..'], {relativeTo: this.activatedRoute});
  }

  get title(): string {
    return this.i18n({
      id: '@@reporting_edit_report_definition_component_title',
      value: 'Edit Report Definition'
    })
  }

  ngAfterViewInit(): void {
    this.spinnerService.showSpinner();

    // Retrieve the existing report definition and initialise the form controls
    this.reportingService.getReportDefinition(this.reportDefinitionId)
      .pipe(first(), finalize(() => this.spinnerService.hideSpinner()))
      .subscribe((reportDefinition: ReportDefinition) => {
        this.reportDefinition = reportDefinition;
        this.editReportDefinitionForm.get('id')!.setValue(reportDefinition.id);
        this.editReportDefinitionForm.get('name')!.setValue(reportDefinition.name);
      }, (error: Error) => {
        // noinspection SuspiciousTypeOfGuard
        if ((error instanceof ReportingServiceError) || (error instanceof AccessDeniedError) ||
          (error instanceof SystemUnavailableError)) {
          // noinspection JSIgnoredPromiseFromCall
          this.router.navigateByUrl('/error/send-error-report', {state: {error}});
        } else {
          this.dialogService.showErrorDialog(error);
        }
      });
  }

  onCancel(): void {
    // noinspection JSIgnoredPromiseFromCall
    this.router.navigate(['../..'], {relativeTo: this.activatedRoute});
  }

  onOK(): void {
    if (this.reportDefinition && this.editReportDefinitionForm.valid) {

      let fileReader: FileReader = new FileReader();

      fileReader.onloadend = (ev: ProgressEvent) => {
        let template = fileReader.result;

        if (template instanceof ArrayBuffer) {

          let base64: string = Base64.encode(template as ArrayBuffer);

          this.reportDefinition!.name = this.editReportDefinitionForm.get('name')!.value;
          this.reportDefinition!.template = base64;


          this.spinnerService.showSpinner();

          this.reportingService.updateReportDefinition(this.reportDefinition!)
            .pipe(first(), finalize(() => this.spinnerService.hideSpinner()))
            .subscribe(() => {
              // noinspection JSIgnoredPromiseFromCall
              this.router.navigate(['../..'], {relativeTo: this.activatedRoute});
            }, (error: Error) => {
              // noinspection SuspiciousTypeOfGuard
              if ((error instanceof ReportingServiceError) ||
                (error instanceof AccessDeniedError) || (error instanceof SystemUnavailableError)) {
                // noinspection JSIgnoredPromiseFromCall
                this.router.navigateByUrl('/error/send-error-report', {state: {error}});
              } else {
                this.dialogService.showErrorDialog(error);
              }
            });
        } else {
          console.log(
            'Failed to read the template file for the report definition (' + fileReader.result +
            ')');
        }
      };

      fileReader.readAsArrayBuffer(this.editReportDefinitionForm.get('template')!.value[0]);
    }
  }
}
