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

import {AfterViewInit, Component} from '@angular/core';
import {FormControl, FormGroup, Validators} from '@angular/forms';
import {MatDialog, MatDialogRef} from '@angular/material/dialog';
import {ActivatedRoute, Router} from '@angular/router';
import {
  AccessDeniedError, AdminContainerView, BackNavigation, DialogService, Error, InvalidArgumentError,
  ServiceUnavailableError, SpinnerService
} from 'ngx-inception/core';
import {finalize, first} from 'rxjs/operators';
import {Job} from '../services/job';
import {JobParameter} from '../services/job-parameter';
import {JobStatus} from '../services/job-status';
import {SchedulerService} from '../services/scheduler.service';
import {
  JobParameterDialogComponent, JobParameterDialogData
} from './job-parameter-dialog.component';

/**
 * The NewJobComponent class implements the new job component.
 *
 * @author Marcus Portmann
 */
@Component({
  templateUrl: 'new-job.component.html',
  styleUrls: ['new-job.component.css'],
  standalone: false
})
export class NewJobComponent extends AdminContainerView implements AfterViewInit {

  JobStatus = JobStatus;

  enabledControl: FormControl;

  getJobStatusDescription = SchedulerService.getJobStatusDescription;

  idControl: FormControl;

  job: Job | null = null;

  jobClassControl: FormControl;

  jobParameters: JobParameter[] = [];

  jobStatuses: JobStatus[] = [
    JobStatus.Unscheduled, JobStatus.Scheduled, JobStatus.Executing,
    JobStatus.Executed, JobStatus.Aborted, JobStatus.Failed, JobStatus.OnceOff];

  nameControl: FormControl;

  newJobForm: FormGroup;

  schedulingPatternControl: FormControl;

  statusControl: FormControl;

  constructor(private router: Router, private activatedRoute: ActivatedRoute,
              private schedulerService: SchedulerService, private dialogService: DialogService,
              private spinnerService: SpinnerService, private matDialog: MatDialog) {
    super();

    // Initialise the form controls
    this.enabledControl = new FormControl(true, [Validators.required]);
    this.idControl = new FormControl('', [Validators.required]);
    this.jobClassControl = new FormControl('', [Validators.required, Validators.maxLength(1000)]);
    this.nameControl = new FormControl('', [Validators.required, Validators.maxLength(100)]);
    this.schedulingPatternControl =
      // eslint-disable-next-line max-len
      new FormControl('0 * * * *',
        [
          Validators.required, Validators.maxLength(100), Validators.pattern(// eslint-disable-next-line max-len
          '(((([*])|(((([0-5])?[0-9])((-(([0-5])?[0-9])))?)))((/(([0-9])?([0-9])?([0-9])?([0-9])?([0-9])?([0-9])?([0-9])?([0-9])?([0-9])?([0-9])?[0-9])))?))(,(((([*])|(((([0-5])?[0-9])((-(([0-5])?[0-9])))?)))((/(([0-9])?([0-9])?([0-9])?([0-9])?([0-9])?([0-9])?([0-9])?([0-9])?([0-9])?([0-9])?[0-9])))?)))* (((([*])|(((((([0-1])?[0-9]))|(([2][0-3])))((-(((([0-1])?[0-9]))|(([2][0-3])))))?)))((/(([0-9])?([0-9])?([0-9])?([0-9])?([0-9])?([0-9])?([0-9])?([0-9])?([0-9])?([0-9])?[0-9])))?))(,(((([*])|(((((([0-1])?[0-9]))|(([2][0-3])))((-(((([0-1])?[0-9]))|(([2][0-3])))))?)))((/(([0-9])?([0-9])?([0-9])?([0-9])?([0-9])?([0-9])?([0-9])?([0-9])?([0-9])?([0-9])?[0-9])))?)))* (((((((([*])|(((((([1-2])?[0-9]))|(([3][0-1]))|(([1-9])))((-(((([1-2])?[0-9]))|(([3][0-1]))|(([1-9])))))?)))((/(([0-9])?([0-9])?([0-9])?([0-9])?([0-9])?([0-9])?([0-9])?([0-9])?([0-9])?([0-9])?[0-9])))?))|(L)|(((((([1-2])?[0-9]))|(([3][0-1]))|(([1-9])))W))))(,(((((([*])|(((((([1-2])?[0-9]))|(([3][0-1]))|(([1-9])))((-(((([1-2])?[0-9]))|(([3][0-1]))|(([1-9])))))?)))((/(([0-9])?([0-9])?([0-9])?([0-9])?([0-9])?([0-9])?([0-9])?([0-9])?([0-9])?([0-9])?[0-9])))?))|(L)|(((((([1-2])?[0-9]))|(([3][0-1]))|(([1-9])))W)))))*)|([?])) (((([*])|((((([1-9]))|(([1][0-2])))((-((([1-9]))|(([1][0-2])))))?))|((((JAN)|(FEB)|(MAR)|(APR)|(MAY)|(JUN)|(JUL)|(AUG)|(SEP)|(OKT)|(NOV)|(DEC))((-((JAN)|(FEB)|(MAR)|(APR)|(MAY)|(JUN)|(JUL)|(AUG)|(SEP)|(OKT)|(NOV)|(DEC))))?)))((/(([0-9])?([0-9])?([0-9])?([0-9])?([0-9])?([0-9])?([0-9])?([0-9])?([0-9])?([0-9])?[0-9])))?))(,(((([*])|((((([1-9]))|(([1][0-2])))((-((([1-9]))|(([1][0-2])))))?))|((((JAN)|(FEB)|(MAR)|(APR)|(MAY)|(JUN)|(JUL)|(AUG)|(SEP)|(OKT)|(NOV)|(DEC))((-((JAN)|(FEB)|(MAR)|(APR)|(MAY)|(JUN)|(JUL)|(AUG)|(SEP)|(OKT)|(NOV)|(DEC))))?)))((/(([0-9])?([0-9])?([0-9])?([0-9])?([0-9])?([0-9])?([0-9])?([0-9])?([0-9])?([0-9])?[0-9])))?)))* (((((((([*])|((([0-6])((-([0-6])))?))|((((SUN)|(MON)|(TUE)|(WED)|(THU)|(FRI)|(SAT))((-((SUN)|(MON)|(TUE)|(WED)|(THU)|(FRI)|(SAT))))?)))((/(([0-9])?([0-9])?([0-9])?([0-9])?([0-9])?([0-9])?([0-9])?([0-9])?([0-9])?([0-9])?[0-9])))?))|((([0-6])L))|(W)|(([#][1-5]))))(,(((((([*])|((([0-6])((-([0-6])))?))|((((SUN)|(MON)|(TUE)|(WED)|(THU)|(FRI)|(SAT))((-((SUN)|(MON)|(TUE)|(WED)|(THU)|(FRI)|(SAT))))?)))((/(([0-9])?([0-9])?([0-9])?([0-9])?([0-9])?([0-9])?([0-9])?([0-9])?([0-9])?([0-9])?[0-9])))?))|((([0-6])L))|(W)|(([#][1-5])))))*)|([?]))((( (((([*])|((([1-2][0-9][0-9][0-9])((-([1-2][0-9][0-9][0-9])))?)))((/(([0-9])?([0-9])?([0-9])?([0-9])?([0-9])?([0-9])?([0-9])?([0-9])?([0-9])?([0-9])?[0-9])))?))(,(((([*])|((([1-2][0-9][0-9][0-9])((-([1-2][0-9][0-9][0-9])))?)))((/(([0-9])?([0-9])?([0-9])?([0-9])?([0-9])?([0-9])?([0-9])?([0-9])?([0-9])?([0-9])?[0-9])))?)))*))?)')
        ]);
    this.statusControl = new FormControl(JobStatus.Unscheduled, [Validators.required]);

    // Initialise the form
    this.newJobForm = new FormGroup({
      enabled: this.enabledControl,
      id: this.idControl,
      jobClass: this.jobClassControl,
      name: this.nameControl,
      schedulingPattern: this.schedulingPatternControl,
      status: this.statusControl
    });
  }

  override get backNavigation(): BackNavigation {
    return new BackNavigation($localize`:@@scheduler_new_job_back_navigation:Jobs`,
      ['..'],
      {relativeTo: this.activatedRoute});
  }

  get title(): string {
    return $localize`:@@scheduler_new_job_title:New Job`
  }

  cancel(): void {
    // noinspection JSIgnoredPromiseFromCall
    this.router.navigate(['..'], {relativeTo: this.activatedRoute});
  }

  deleteJobParameter(existingJobParameter: JobParameter): void {
    this.jobParameters.forEach((jobParameter, index) => {
      if (jobParameter.name === existingJobParameter.name) {
        this.jobParameters.splice(index, 1);
      }
    });
  }

  editJobParameter(existingJobParameter: JobParameter): void {
    const data: JobParameterDialogData = {
      name: existingJobParameter.name,
      readonlyName: true,
      value: existingJobParameter.value
    };

    const dialogRef: MatDialogRef<JobParameterDialogComponent, JobParameter> = this.matDialog.open(
      JobParameterDialogComponent, {
        restoreFocus: false,
        data
      });

    dialogRef.afterClosed()
    .pipe(first())
    .subscribe((jobParameter: JobParameter | undefined) => {
      if (jobParameter) {
        for (const aJobParameter of this.jobParameters) {
          if (aJobParameter.name === jobParameter.name) {
            aJobParameter.value = jobParameter.value;
            return;
          }
        }
      }
    });
  }

  newJobParameter(): void {
    const data: JobParameterDialogData = {
      name: '',
      value: '',
      readonlyName: false
    };

    const dialogRef: MatDialogRef<JobParameterDialogComponent, JobParameter> = this.matDialog.open(
      JobParameterDialogComponent, {
        restoreFocus: false,
        data
      });

    dialogRef.afterClosed()
    .pipe(first())
    .subscribe((jobParameter: JobParameter | undefined) => {
      if (jobParameter) {
        for (const aJobParameter of this.jobParameters) {
          if (aJobParameter.name === jobParameter.name) {
            this.dialogService.showErrorDialog(new Error('The job parameter already exists.'));

            return;
          }
        }

        this.jobParameters.push(jobParameter);

        this.jobParameters.sort((a: JobParameter, b: JobParameter) => {
          if ((a.name ? a.name.toLowerCase() : '') < (b.name ? b.name.toLowerCase() : '')) {
            return -1;
          }
          if ((a.name ? a.name.toLowerCase() : '') > (b.name ? b.name.toLowerCase() : '')) {
            return 1;
          }
          return 0;
        });
      }
    });
  }

  ngAfterViewInit(): void {
    this.job = new Job('', '', '', '', true, []);
  }

  ok(): void {
    if (this.job && this.newJobForm.valid) {
      this.job.enabled = this.enabledControl.value;
      this.job.id = this.idControl.value;
      this.job.jobClass = this.jobClassControl.value;
      this.job.name = this.nameControl.value;
      this.job.parameters = this.jobParameters;
      this.job.schedulingPattern = this.schedulingPatternControl.value;
      this.job.status = this.statusControl.value;

      this.spinnerService.showSpinner();

      this.schedulerService.createJob(this.job)
      .pipe(first(), finalize(() => this.spinnerService.hideSpinner()))
      .subscribe(() => {
        // noinspection JSIgnoredPromiseFromCall
        this.router.navigate(['..'], {relativeTo: this.activatedRoute});
      }, (error: Error) => {
        // noinspection SuspiciousTypeOfGuard
        if ((error instanceof AccessDeniedError) || (error instanceof InvalidArgumentError) ||
          (error instanceof ServiceUnavailableError)) {
          // noinspection JSIgnoredPromiseFromCall
          this.router.navigateByUrl('/error/send-error-report', {state: {error}});
        } else {
          this.dialogService.showErrorDialog(error);
        }
      });
    }
  }
}
