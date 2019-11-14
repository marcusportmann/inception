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
import {v4 as uuid} from "uuid";
import {FileValidator} from "../../validators/file-validator";
import {Base64} from "../../util";

/**
 * The NewReportDefinitionComponent class implements the new report definition component.
 *
 * @author Marcus Portmann
 */
@Component({
  templateUrl: 'new-report-definition.component.html',
  styleUrls: ['new-report-definition.component.css'],
})
export class NewReportDefinitionComponent extends AdminContainerView implements AfterViewInit {

  reportDefinition?: ReportDefinition;

  newReportDefinitionForm: FormGroup;

  constructor(private router: Router, private activatedRoute: ActivatedRoute,
              private formBuilder: FormBuilder, private i18n: I18n,
              private reportingService: ReportingService, private dialogService: DialogService,
              private spinnerService: SpinnerService) {
    super();

    // Initialise the form
    this.newReportDefinitionForm = new FormGroup({
      id: new FormControl(uuid(), [Validators.required]),
      name: new FormControl('', [Validators.required, Validators.maxLength(100)]),
      template: new FormControl('', [Validators.required, FileValidator.minSize(1),
        FileValidator.maxSize(ReportingService.MAX_TEMPLATE_SIZE)
      ])
    })
  }

  get backNavigation(): BackNavigation {
    return new BackNavigation(this.i18n({
      id: '@@reporting_new_report_definition_component_back_title',
      value: 'Report Definitions'
    }), ['..'], {relativeTo: this.activatedRoute});
  }

  get title(): string {
    return this.i18n({
      id: '@@reporting_new_report_definition_component_title',
      value: 'New Report Definition'
    })
  }

  ngAfterViewInit(): void {
    this.reportDefinition = new ReportDefinition('', '', '');
  }

  onCancel(): void {
    // noinspection JSIgnoredPromiseFromCall
    this.router.navigate(['..'], {relativeTo: this.activatedRoute});
  }

  onOK(): void {
    if (this.reportDefinition && this.newReportDefinitionForm.valid) {

      var fileReader: FileReader = new FileReader();

      fileReader.onloadend = (ev: ProgressEvent) => {
        var template = fileReader.result;

        if (template instanceof ArrayBuffer) {

          let base64: string = Base64.encode(template as ArrayBuffer);

          this.reportDefinition!.id = this.newReportDefinitionForm.get('id')!.value;
          this.reportDefinition!.name = this.newReportDefinitionForm.get('name')!.value;
          this.reportDefinition!.template = base64;


          this.spinnerService.showSpinner();

          this.reportingService.createReportDefinition(this.reportDefinition!)
            .pipe(first(), finalize(() => this.spinnerService.hideSpinner()))
            .subscribe(() => {
              // noinspection JSIgnoredPromiseFromCall
              this.router.navigate(['..'], {relativeTo: this.activatedRoute});
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
        } else {
          console.log('Failed to read the template file for the report definition (' + fileReader.result + ')');
        }
      };

      fileReader.readAsArrayBuffer(this.newReportDefinitionForm.get('template')!.value[0]);
    }
  }
}
