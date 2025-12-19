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
import { MatDialogRef } from '@angular/material/dialog';
import { ActivatedRoute, Router } from '@angular/router';
import {
  CoreModule, DialogService, InformationDialogComponent, ProblemDetails, SpinnerService,
  ValidatedFormDirective
} from 'ngx-inception/core';
import { Observable, throwError } from 'rxjs';
import { catchError, finalize, first, map } from 'rxjs/operators';
import { ErrorService } from '../services/error.service';

/**
 * The SendErrorReportComponent class implements the send error report component.
 *
 * @author Marcus Portmann
 */
@Component({
  selector: 'inception-error-send-error-report',
  standalone: true,
  imports: [CoreModule, ValidatedFormDirective],
  templateUrl: 'send-error-report.component.html'
})
export class SendErrorReportComponent implements OnInit {
  emailControl: FormControl;

  error: Error | null = null;

  feedbackControl: FormControl;

  messageControl: FormControl;

  sendErrorReportForm: FormGroup;

  private activatedRoute = inject(ActivatedRoute);

  private dialogService = inject(DialogService);

  private errorService = inject(ErrorService);

  private router = inject(Router);

  private spinnerService = inject(SpinnerService);

  constructor() {
    // Initialize form controls
    this.emailControl = new FormControl('', Validators.email);
    this.feedbackControl = new FormControl('');
    this.messageControl = new FormControl('');

    // Initialize the form group
    this.sendErrorReportForm = new FormGroup({
      message: this.messageControl,
      email: this.emailControl,
      feedback: this.feedbackControl
    });
  }

  ngOnInit(): void {
    this.activatedRoute.paramMap
      .pipe(
        first(),
        map(() => window.history.state)
      )
      .subscribe((state) => {
        if (state.error) {
          this.error = state.error;
          this.messageControl.setValue(state.error.message);
          this.logErrorDetails(this.error!);
        } else {
          console.log('No error found, redirecting to the application root');
          // noinspection JSIgnoredPromiseFromCall
          this.router.navigate(['/']);
        }
      });
  }

  sendErrorReport(): void {
    if (this.sendErrorReportForm.valid && this.error) {
      this.spinnerService.showSpinner();

      this.errorService
        .sendErrorReport(this.error, this.emailControl.value, this.feedbackControl.value)
        .pipe(
          first(),
          finalize(() => this.spinnerService.hideSpinner()),
          catchError((error) => this.handleError(error))
        )
        .subscribe(() => {
          const dialogRef: MatDialogRef<InformationDialogComponent, boolean> =
            this.dialogService.showInformationDialog({
              message: 'Your error report was submitted.'
            });

          dialogRef
            .afterClosed()
            .pipe(first())
            .subscribe(() => {
              // noinspection JSIgnoredPromiseFromCall
              this.router.navigate(['/']);
            });
        });
    }
  }

  private handleError(error: Error): Observable<never> {
    this.dialogService.showErrorDialog(error);
    return throwError(() => error);
  }

  private logErrorDetails(error: Error): void {
    console.log('Error: ', error);

    if (error.cause) {
      console.log('Cause: ', error.cause);

      const problemDetails = error.cause as ProblemDetails;
      if (problemDetails.stackTrace) {
        console.log('StackTrace: ', problemDetails.stackTrace);
      }
    }
  }
}
