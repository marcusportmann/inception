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

import { ChangeDetectorRef, inject} from '@angular/core';
import {MatDialog, MatDialogRef} from '@angular/material/dialog';
import {AdminContainerView} from 'ngx-inception/core';
import {first} from 'rxjs/operators';
import {JobParameter} from '../services/job-parameter';
import {JobStatus} from '../services/job-status';
import {SchedulerService} from '../services/scheduler.service';
import {
  JobParameterDialogComponent, JobParameterDialogData
} from './job-parameter-dialog.component';

/**
 * The JobsComponent class provides common scheduler job UI behavior for job-related
 * views/components.
 *
 * It exposes the available {@link JobStatus} values and their user-friendly descriptions, and
 * manages the collection of {@link JobParameter}s for a job (add, edit, and delete).
 * Job parameters are edited via the {@link JobParameterDialogComponent}.
 *
 * Concrete components (e.g. NewJobComponent and EditJobComponent) extend this class to reuse the
 * shared parameter management and status helper functionality.
 *
 * @author Marcus Portmann
 */
export abstract class JobComponent extends AdminContainerView {
  readonly JobStatus = JobStatus;

  getJobStatusDescription = SchedulerService.getJobStatusDescription;

  jobParameters: JobParameter[] = [];

  readonly jobStatuses: JobStatus[] = [
    JobStatus.Unscheduled,
    JobStatus.Scheduled,
    JobStatus.Executing,
    JobStatus.Executed,
    JobStatus.Aborted,
    JobStatus.Failed,
    JobStatus.OnceOff
  ];

  private readonly changeDetectorRef = inject(ChangeDetectorRef);

  private readonly matDialog = inject(MatDialog);

  protected constructor() {
    super();
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
      JobParameterDialogComponent,
      {
        restoreFocus: false,
        data
      }
    );

    dialogRef
      .afterClosed()
      .pipe(first())
      .subscribe((jobParameter: JobParameter | undefined) => {
        if (jobParameter) {
          for (const aJobParameter of this.jobParameters) {
            if (aJobParameter.name === jobParameter.name) {
              aJobParameter.value = jobParameter.value;
              return;
            }
          }

          this.changeDetectorRef.markForCheck();
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
      JobParameterDialogComponent,
      {
        restoreFocus: false,
        data
      }
    );

    dialogRef
      .afterClosed()
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

          this.changeDetectorRef.markForCheck();
        }
      });
  }
}
