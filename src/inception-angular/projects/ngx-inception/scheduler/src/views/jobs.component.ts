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

import { Component, HostBinding, inject } from '@angular/core';
import { CoreModule, FilteredPaginatedListView, TableFilterComponent } from 'ngx-inception/core';
import { Observable } from 'rxjs';
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

  readonly displayedColumns = [
    'name',
    'status',
    'executionAttempts',
    'nextExecution',
    'actions'
  ] as const;

  readonly getJobStatusDescription = SchedulerService.getJobStatusDescription;

  @HostBinding('class') readonly hostClass = 'flex flex-column flex-fill';

  readonly listKey = 'scheduler.jobs';

  readonly title = $localize`:@@scheduler_jobs_title:Jobs`;

  private schedulerService = inject(SchedulerService);

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

  protected override fetchData(): Observable<Job[]> {
    return this.schedulerService.getJobs();
  }
}
