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

import { ReportDefinitionSummary } from '../services/report-definition-summary';
import { ReportingService } from '../services/reporting.service';

/**
 * The ReportDefinitionsComponent class implements the Report Definitions component.
 *
 * @author Marcus
 */
@Component({
  selector: 'inception-reporting-report-definitions',
  standalone: true,
  imports: [CoreModule, TableFilterComponent],
  templateUrl: 'report-definitions.component.html',
  styleUrls: ['report-definitions.component.css']
})
export class ReportDefinitionsComponent extends FilteredPaginatedListView<ReportDefinitionSummary> {
  readonly displayedColumns: readonly string[] = ['name', 'actions'];

  @HostBinding('class') readonly hostClass = 'flex flex-column flex-fill';

  readonly listKey = 'reporting.report-definitions';

  readonly title = $localize`:@@reporting_report_definitions_title:Report Definitions`;

  constructor(private reportingService: ReportingService) {
    super();
  }

  deleteReportDefinition(reportDefinitionId: string): void {
    this.confirmAndProcessAction(
      $localize`:@@reporting_report_definitions_confirm_delete_report_definition:Are you sure you want to delete the report definition?`,
      () => this.reportingService.deleteReportDefinition(reportDefinitionId)
    );
  }

  editReportDefinition(reportDefinitionId: string): void {
    // noinspection JSIgnoredPromiseFromCall
    this.router.navigate([encodeURIComponent(reportDefinitionId) + '/edit'], {
      relativeTo: this.activatedRoute
    });
  }

  newReportDefinition(): void {
    // noinspection JSIgnoredPromiseFromCall
    this.router.navigate(['new'], { relativeTo: this.activatedRoute });
  }

  protected override createFilterPredicate(): (
    data: ReportDefinitionSummary,
    filter: string
  ) => boolean {
    return (data: ReportDefinitionSummary, filter: string): boolean => {
      const normalizedFilter = (filter ?? '').toLowerCase();
      const name = (data.name ?? '').toLowerCase();

      return name.includes(normalizedFilter);
    };
  }

  protected override loadData(): void {
    this.spinnerService.showSpinner();

    this.reportingService
      .getReportDefinitionSummaries()
      .pipe(finalize(() => this.spinnerService.hideSpinner()))
      .subscribe({
        next: (reportDefinitionSummaries: ReportDefinitionSummary[]) => {
          this.dataSource.data = reportDefinitionSummaries;

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
              this.reportingService.getReportDefinitionSummaries().pipe(
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
        next: (reportDefinitionSummaries: ReportDefinitionSummary[]) => {
          this.dataSource.data = reportDefinitionSummaries;

          this.restorePageAfterDataLoaded();
        }
      });
  }
}
