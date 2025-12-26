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
import { BackNavigation, CoreModule, ValidatedFormDirective } from 'ngx-inception/core';
import { finalize, first } from 'rxjs/operators';
import { Job } from '../services/job';
import { JobStatus } from '../services/job-status';
import { SchedulerService } from '../services/scheduler.service';
import { JobComponent } from './job.component';

/**
 * The EditJobComponent class implements the edit job component.
 *
 * @author Marcus Portmann
 */
@Component({
  selector: 'inception-scheduler-edit-job',
  imports: [CoreModule, ValidatedFormDirective],
  templateUrl: 'edit-job.component.html',
  styleUrls: ['edit-job.component.css']
})
export class EditJobComponent extends JobComponent implements OnInit {
  readonly editJobForm: FormGroup<{
    enabled: FormControl<boolean>;
    id: FormControl<string>;
    jobClass: FormControl<string>;
    name: FormControl<string>;
    schedulingPattern: FormControl<string>;
    status: FormControl<JobStatus>;
  }>;

  readonly enabledControl: FormControl<boolean>;

  readonly idControl: FormControl<string>;

  job: Job | null = null;

  readonly jobClassControl: FormControl<string>;

  readonly jobId: string;

  readonly nameControl: FormControl<string>;

  readonly schedulingPatternControl: FormControl<string>;

  readonly statusControl: FormControl<JobStatus>;

  readonly title = $localize`:@@scheduler_edit_job_title:Edit Job`;

  private readonly schedulerService = inject(SchedulerService);

  constructor() {
    super();

    // Retrieve the route parameters
    const jobId = this.activatedRoute.snapshot.paramMap.get('jobId');
    if (!jobId) {
      throw new globalThis.Error('No jobId route parameter found');
    }
    this.jobId = jobId;

    // Initialize the form controls
    this.enabledControl = new FormControl<boolean>(true, {
      nonNullable: true,
      validators: [Validators.required]
    });

    this.idControl = new FormControl<string>(
      {
        value: '',
        disabled: true
      },
      { nonNullable: true, validators: [Validators.required] }
    );

    this.jobClassControl = new FormControl<string>('', {
      nonNullable: true,
      validators: [Validators.required, Validators.maxLength(1000)]
    });

    this.nameControl = new FormControl<string>('', {
      nonNullable: true,
      validators: [Validators.required, Validators.maxLength(100)]
    });

    this.schedulingPatternControl = new FormControl<string>('0 * * * *', {
      nonNullable: true,
      validators: [
        Validators.required,
        Validators.maxLength(100),
        Validators.pattern(
          '(((([*])|(((([0-5])?[0-9])((-(([0-5])?[0-9])))?)))((/(([0-9])?([0-9])?([0-9])?([0-9])?([0-9])?([0-9])?([0-9])?([0-9])?([0-9])?([0-9])?[0-9])))?))(,(((([*])|(((([0-5])?[0-9])((-(([0-5])?[0-9])))?)))((/(([0-9])?([0-9])?([0-9])?([0-9])?([0-9])?([0-9])?([0-9])?([0-9])?([0-9])?([0-9])?[0-9])))?)))* (((([*])|(((((([0-1])?[0-9]))|(([2][0-3])))((-(((([0-1])?[0-9]))|(([2][0-3])))))?)))((/(([0-9])?([0-9])?([0-9])?([0-9])?([0-9])?([0-9])?([0-9])?([0-9])?([0-9])?([0-9])?[0-9])))?))(,(((([*])|(((((([0-1])?[0-9]))|(([2][0-3])))((-(((([0-1])?[0-9]))|(([2][0-3])))))?)))((/(([0-9])?([0-9])?([0-9])?([0-9])?([0-9])?([0-9])?([0-9])?([0-9])?([0-9])?([0-9])?[0-9])))?)))* (((((((([*])|(((((([1-2])?[0-9]))|(([3][0-1]))|(([1-9])))((-(((([1-2])?[0-9]))|(([3][0-1]))|(([1-9])))))?)))((/(([0-9])?([0-9])?([0-9])?([0-9])?([0-9])?([0-9])?([0-9])?([0-9])?([0-9])?([0-9])?[0-9])))?))|(L)|(((((([1-2])?[0-9]))|(([3][0-1]))|(([1-9])))W))))(,(((((([*])|(((((([1-2])?[0-9]))|(([3][0-1]))|(([1-9])))((-(((([1-2])?[0-9]))|(([3][0-1]))|(([1-9])))))?)))((/(([0-9])?([0-9])?([0-9])?([0-9])?([0-9])?([0-9])?([0-9])?([0-9])?([0-9])?([0-9])?[0-9])))?))|(L)|(((((([1-2])?[0-9]))|(([3][0-1]))|(([1-9])))W)))))*)|([?])) (((([*])|((((([1-9]))|(([1][0-2])))((-((([1-9]))|(([1][0-2])))))?))|((((JAN)|(FEB)|(MAR)|(APR)|(MAY)|(JUN)|(JUL)|(AUG)|(SEP)|(OKT)|(NOV)|(DEC))((-((JAN)|(FEB)|(MAR)|(APR)|(MAY)|(JUN)|(JUL)|(AUG)|(SEP)|(OKT)|(NOV)|(DEC))))?)))((/(([0-9])?([0-9])?([0-9])?([0-9])?([0-9])?([0-9])?([0-9])?([0-9])?([0-9])?([0-9])?[0-9])))?))(,(((([*])|((((([1-9]))|(([1][0-2])))((-((([1-9]))|(([1][0-2])))))?))|((((JAN)|(FEB)|(MAR)|(APR)|(MAY)|(JUN)|(JUL)|(AUG)|(SEP)|(OKT)|(NOV)|(DEC))((-((JAN)|(FEB)|(MAR)|(APR)|(MAY)|(JUN)|(JUL)|(AUG)|(SEP)|(OKT)|(NOV)|(DEC))))?)))((/(([0-9])?([0-9])?([0-9])?([0-9])?([0-9])?([0-9])?([0-9])?([0-9])?([0-9])?([0-9])?[0-9])))?)))* (((((((([*])|((([0-6])((-([0-6])))?))|((((SUN)|(MON)|(TUE)|(WED)|(THU)|(FRI)|(SAT))((-((SUN)|(MON)|(TUE)|(WED)|(THU)|(FRI)|(SAT))))?)))((/(([0-9])?([0-9])?([0-9])?([0-9])?([0-9])?([0-9])?([0-9])?([0-9])?([0-9])?([0-9])?[0-9])))?))|((([0-6])L))|(W)|(([#][1-5]))))(,(((((([*])|((([0-6])((-([0-6])))?))|((((SUN)|(MON)|(TUE)|(WED)|(THU)|(FRI)|(SAT))((-((SUN)|(MON)|(TUE)|(WED)|(THU)|(FRI)|(SAT))))?)))((/(([0-9])?([0-9])?([0-9])?([0-9])?([0-9])?([0-9])?([0-9])?([0-9])?([0-9])?([0-9])?[0-9])))?))|((([0-6])L))|(W)|(([#][1-5])))))*)|([?]))((( (((([*])|((([1-2][0-9][0-9][0-9])((-([1-2][0-9][0-9][0-9])))?)))((/(([0-9])?([0-9])?([0-9])?([0-9])?([0-9])?([0-9])?([0-9])?([0-9])?([0-9])?([0-9])?[0-9])))?))(,(((([*])|((([1-2][0-9][0-9][0-9])((-([1-2][0-9][0-9][0-9])))?)))((/(([0-9])?([0-9])?([0-9])?([0-9])?([0-9])?([0-9])?([0-9])?([0-9])?([0-9])?([0-9])?[0-9])))?)))*))?)'
        )
      ]
    });

    this.statusControl = new FormControl<JobStatus>(JobStatus.Unscheduled, {
      nonNullable: true,
      validators: [Validators.required]
    });

    // Initialize the form
    this.editJobForm = new FormGroup({
      enabled: this.enabledControl,
      id: this.idControl,
      jobClass: this.jobClassControl,
      name: this.nameControl,
      schedulingPattern: this.schedulingPatternControl,
      status: this.statusControl
    });
  }

  override get backNavigation(): BackNavigation {
    return new BackNavigation($localize`:@@scheduler_edit_job_back_navigation:Jobs`, ['.'], {
      relativeTo: this.activatedRoute.parent?.parent
    });
  }

  cancel(): void {
    void this.router.navigate(['.'], { relativeTo: this.activatedRoute.parent?.parent });
  }

  ngOnInit(): void {
    // Retrieve the existing job and initialize the form controls
    this.spinnerService.showSpinner();

    this.schedulerService
      .getJob(this.jobId)
      .pipe(
        first(),
        finalize(() => this.spinnerService.hideSpinner())
      )
      .subscribe({
        next: (job: Job) => {
          this.job = job;
          this.enabledControl.setValue(job.enabled);
          this.idControl.setValue(job.id);
          this.jobClassControl.setValue(job.jobClass);
          this.nameControl.setValue(job.name);
          this.schedulingPatternControl.setValue(job.schedulingPattern);
          this.statusControl.setValue(job.status);
          this.jobParameters = job.parameters;
        },
        error: (error: Error) =>
          this.handleError(error, true, ['.'], { relativeTo: this.activatedRoute.parent?.parent })
      });
  }

  ok(): void {
    if (!this.job || !this.editJobForm.valid) {
      return;
    }

    this.job.enabled = this.enabledControl.value;
    this.job.id = this.idControl.value;
    this.job.jobClass = this.jobClassControl.value;
    this.job.name = this.nameControl.value;
    this.job.parameters = this.jobParameters;
    this.job.schedulingPattern = this.schedulingPatternControl.value;
    this.job.status = this.statusControl.value;

    this.spinnerService.showSpinner();

    this.schedulerService
      .updateJob(this.job)
      .pipe(
        first(),
        finalize(() => this.spinnerService.hideSpinner())
      )
      .subscribe({
        next: () => {
          void this.router.navigate(['.'], {
            relativeTo: this.activatedRoute.parent?.parent
          });
        },
        error: (error: Error) => this.handleError(error, false)
      });
  }
}
