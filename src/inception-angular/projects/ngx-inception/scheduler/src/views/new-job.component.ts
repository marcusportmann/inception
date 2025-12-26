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
 * The NewJobComponent class implements the new job component.
 *
 * @author Marcus Portmann
 */
@Component({
  selector: 'inception-scheduler-new-job',
  imports: [CoreModule, ValidatedFormDirective],
  templateUrl: 'new-job.component.html',
  styleUrls: ['new-job.component.css']
})
export class NewJobComponent extends JobComponent implements OnInit {
  readonly enabledControl: FormControl<boolean>;

  readonly idControl: FormControl<string>;

  job: Job | null = null;

  readonly jobClassControl: FormControl<string>;

  readonly nameControl: FormControl<string>;

  readonly newJobForm: FormGroup<{
    enabled: FormControl<boolean>;
    id: FormControl<string>;
    jobClass: FormControl<string>;
    name: FormControl<string>;
    schedulingPattern: FormControl<string>;
    status: FormControl<JobStatus>;
  }>;

  readonly schedulingPatternControl: FormControl<string>;

  readonly statusControl: FormControl<JobStatus>;

  readonly title = $localize`:@@scheduler_new_job_title:New Job`;

  private readonly schedulerService = inject(SchedulerService);

  constructor() {
    super();

    // Initialize the form controls
    this.enabledControl = new FormControl<boolean>(true, {
      nonNullable: true,
      validators: [Validators.required]
    });

    this.idControl = new FormControl<string>('', {
      nonNullable: true,
      validators: [Validators.required]
    });

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
    return new BackNavigation($localize`:@@scheduler_new_job_back_navigation:Jobs`, ['.'], {
      relativeTo: this.activatedRoute.parent
    });
  }

  cancel(): void {
    void this.router.navigate(['.'], { relativeTo: this.activatedRoute.parent });
  }

  ngOnInit(): void {
    this.job = new Job('', '', '', '', true, []);
  }

  ok(): void {
    if (!this.job || !this.newJobForm.valid) {
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
      .createJob(this.job)
      .pipe(
        first(),
        finalize(() => this.spinnerService.hideSpinner())
      )
      .subscribe({
        next: () => {
          void this.router.navigate(['.'], { relativeTo: this.activatedRoute.parent });
        },
        error: (error: Error) => this.handleError(error, false)
      });
  }
}
