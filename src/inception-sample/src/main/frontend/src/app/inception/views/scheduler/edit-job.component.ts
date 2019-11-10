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
import {Job} from "../../services/scheduler/job";
import {SchedulerService} from "../../services/scheduler/scheduler.service";
import {SchedulerServiceError} from "../../services/scheduler/scheduler.service.errors";

/**
 * The EditJobComponent class implements the edit job component.
 *
 * @author Marcus Portmann
 */
@Component({
  templateUrl: 'edit-job.component.html',
  styleUrls: ['edit-job.component.css'],
})
export class EditJobComponent extends AdminContainerView implements AfterViewInit {

  job?: Job;

  jobId: string;

  editJobForm: FormGroup;

  constructor(private router: Router, private activatedRoute: ActivatedRoute,
              private formBuilder: FormBuilder, private i18n: I18n,
              private schedulerService: SchedulerService, private dialogService: DialogService,
              private spinnerService: SpinnerService) {
    super();

    // Retrieve parameters
    this.jobId =
      decodeURIComponent(this.activatedRoute.snapshot.paramMap.get('jobId')!);

    // Initialise the form
    this.editJobForm = new FormGroup({
      id: new FormControl({
        value: '',
        disabled: true
      }, [Validators.required, Validators.maxLength(100)]),
      name: new FormControl('', [Validators.required, Validators.maxLength(100)])
    });
  }

  get backNavigation(): BackNavigation {
    return new BackNavigation(this.i18n({
      id: '@@scheduler_edit_job_component_back_title',
      value: 'Jobs'
    }), ['../..'], {relativeTo: this.activatedRoute});
  }

  get title(): string {
    return this.i18n({
      id: '@@scheduler_edit_job_component_title',
      value: 'Edit Job'
    })
  }

  ngAfterViewInit(): void {
    this.spinnerService.showSpinner();

    // Retrieve the existing job and initialise the form controls
    this.schedulerService.getJob(this.jobId)
      .pipe(first(), finalize(() => this.spinnerService.hideSpinner()))
      .subscribe((job: Job) => {
        this.job = job;
        this.editJobForm.get('id')!.setValue(job.id);
        this.editJobForm.get('name')!.setValue(job.name);
      }, (error: Error) => {
        // noinspection SuspiciousTypeOfGuard
        if ((error instanceof SchedulerServiceError) || (error instanceof AccessDeniedError) ||
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
    if (this.job && this.editJobForm.valid) {
      //const data = this.editJobForm.get('data')!.value;

      this.job.name = this.editJobForm.get('name')!.value;
      //this.job.data = (!!data) ? data : null;

      this.spinnerService.showSpinner();

      this.schedulerService.updateJob(this.job)
        .pipe(first(), finalize(() => this.spinnerService.hideSpinner()))
        .subscribe(() => {
          // noinspection JSIgnoredPromiseFromCall
          this.router.navigate(['../..'], {relativeTo: this.activatedRoute});
        }, (error: Error) => {
          // noinspection SuspiciousTypeOfGuard
          if ((error instanceof SchedulerServiceError) || (error instanceof AccessDeniedError) ||
            (error instanceof SystemUnavailableError)) {
            // noinspection JSIgnoredPromiseFromCall
            this.router.navigateByUrl('/error/send-error-report', {state: {error}});
          } else {
            this.dialogService.showErrorDialog(error);
          }
        });
    }
  }
}
