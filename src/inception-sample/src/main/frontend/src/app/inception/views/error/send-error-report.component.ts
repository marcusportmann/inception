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

import {Component, OnInit} from '@angular/core';
import {AbstractControl, FormBuilder, FormGroup, Validators} from '@angular/forms';
import {ErrorService} from '../../services/error/error.service';
import {first, map} from 'rxjs/operators';
import {ActivatedRoute, Router} from '@angular/router';
import {Error} from '../../errors/error';
import {SpinnerService} from '../../services/layout/spinner.service';
import {DialogService} from '../../services/dialog/dialog.service';
import {I18n} from '@ngx-translate/i18n-polyfill';
import { MatDialogRef } from '@angular/material/dialog';
import {InformationDialogComponent} from '../../components/dialogs';

/**
 * The SendErrorReportComponent class implements the send error report component.
 *
 * @author Marcus Portmann
 */
@Component({
  templateUrl: 'send-error-report.component.html'
})
export class SendErrorReportComponent implements OnInit {

  sendErrorReportForm: FormGroup;

  error: Error;

  constructor(private router: Router, private activatedRoute: ActivatedRoute,
              private formBuilder: FormBuilder, private i18n: I18n,
              private dialogService: DialogService, private errorService: ErrorService,
              private spinnerService: SpinnerService) {
    this.sendErrorReportForm = this.formBuilder.group({
      // tslint:disable-next-line
      message: [''],
      email: ['', Validators.email],
      feedback: ['']
    });
  }

  get emailFormControl(): AbstractControl {
    return this.sendErrorReportForm.get('email');
  }

  get feedbackFormControl(): AbstractControl {
    return this.sendErrorReportForm.get('feedback');
  }

  get messageFormControl(): AbstractControl {
    return this.sendErrorReportForm.get('message');
  }

  ngOnInit(): void {
    this.activatedRoute.paramMap
      .pipe(first(), map((state: any) => window.history.state))
      .subscribe((state: any) => {
        if (state.error) {
          this.error = state.error;

          this.messageFormControl.setValue(this.error.message);

          console.log('Error: ', this.error);
        } else {
          console.log('No error found, redirecting to the application root');

          // noinspection JSIgnoredPromiseFromCall
          this.router.navigate(['/']);
        }
      });
  }

  onSendErrorReport(): void {
    if (this.sendErrorReportForm.valid) {
      this.spinnerService.showSpinner();

      this.errorService.sendErrorReport(this.emailFormControl.value, this.feedbackFormControl.value,
        this.error)
        .pipe(first())
        .subscribe(result => {
          this.spinnerService.hideSpinner();

          const dialogRef: MatDialogRef<InformationDialogComponent, boolean> = this.dialogService.showInformationDialog(
            {
              message: this.i18n({
                id: '@@send_error_component_error_report_submitted',
                value: 'Your error report was submitted.'
              }, {})
            });

          dialogRef.afterClosed()
            .pipe(first())
            .subscribe((confirmation: boolean) => {
              this.router.navigate(['/']);
            });
        }, (error: Error) => {
          this.spinnerService.hideSpinner();

          this.dialogService.showErrorDialog(error);
        });
    }
  }
}
