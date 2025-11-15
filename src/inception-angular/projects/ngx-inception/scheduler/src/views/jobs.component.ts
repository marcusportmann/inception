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

import { Component, HostBinding } from '@angular/core';
import {
  CoreModule, Error, FilteredPaginatedListView, TableFilterComponent
} from 'ngx-inception/core';
import { EMPTY, Observable } from 'rxjs';
import { catchError, filter, finalize, first, switchMap, takeUntil } from 'rxjs/operators';
import { Job } from '../services/job';
import { JobStatus } from '../services/job-status';
import { SchedulerService } from '../services/scheduler.service';

/**
 * The JobsComponent class implements the Jobs component.
 *
 * @author Marcus Portmann
 */
@Component({
  selector: 'inception-scheduler-jobs',
  standalone: true,
  imports: [CoreModule, TableFilterComponent],
  templateUrl: 'jobs.component.html',
  styleUrls: ['jobs.component.css']
})
export class JobsComponent extends FilteredPaginatedListView<Job> {
  // noinspection JSUnusedGlobalSymbols
  readonly JobStatus = JobStatus;

  readonly displayedColumns: readonly string[] = [
    'name',
    'status',
    'executionAttempts',
    'nextExecution',
    'actions'
  ];

  readonly getJobStatusDescription = SchedulerService.getJobStatusDescription;

  @HostBinding('class') readonly hostClass = 'flex flex-column flex-fill';

  readonly listKey = 'scheduler.jobs';

  readonly title = $localize`:@@scheduler_jobs_title:Jobs`;

  constructor(private schedulerService: SchedulerService) {
    super();
  }

  deleteJob(jobId: string): void {
    this.confirmAndProcessAction(
      $localize`:@@scheduler_jobs_confirm_delete_job:Are you sure you want to delete the job?`,
      () => this.schedulerService.deleteJob(jobId)
    );
  }

  editJob(jobId: string): void {
    // noinspection JSIgnoredPromiseFromCall
    this.router.navigate([encodeURIComponent(jobId) + '/edit'], {
      relativeTo: this.activatedRoute
    });
  }

  newJob(): void {
    // noinspection JSIgnoredPromiseFromCall
    this.router.navigate(['new'], { relativeTo: this.activatedRoute });
  }

  protected override createFilterPredicate(): (data: Job, filter: string) => boolean {
    return (data: Job, filter: string): boolean => {
      const normalized = (filter ?? '').toLowerCase();
      const name = (data.name ?? '').toLowerCase();
      return name.includes(normalized);
    };
  }

  protected override loadData(): void {
    this.spinnerService.showSpinner();

    this.schedulerService
      .getJobs()
      .pipe(finalize(() => this.spinnerService.hideSpinner()))
      .subscribe({
        next: (jobs: Job[]) => {
          this.dataSource.data = jobs;

          this.restorePageAfterDataLoaded();
        },
        error: (error: Error) => this.handleError(error, false)
      });
  }

  private confirmAndProcessAction(
    confirmationMessage: string,
    action: () => Observable<void | boolean>
  ): void {
    const dialogRef = this.dialogService.showConfirmationDialog({
      message: confirmationMessage
    });

    dialogRef
      .afterClosed()
      .pipe(
        first(),
        filter((confirmed) => confirmed === true),
        switchMap(() => {
          this.spinnerService.showSpinner();

          return action().pipe(
            catchError((error: Error) => {
              this.handleError(error, false);
              return EMPTY;
            }),
            switchMap(() =>
              this.schedulerService.getJobs().pipe(
                catchError((error: Error) => {
                  this.handleError(error, false);
                  return EMPTY;
                })
              )
            ),
            finalize(() => this.spinnerService.hideSpinner())
          );
        }),
        takeUntil(this.destroy$)
      )
      .subscribe({
        next: (jobs) => {
          this.dataSource.data = jobs;
        }
      });
  }
}
