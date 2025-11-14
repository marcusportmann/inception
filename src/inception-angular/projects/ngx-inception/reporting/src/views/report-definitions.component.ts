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

import { AfterViewInit, Component, HostBinding, ViewChild } from '@angular/core';
import { MatDialogRef } from '@angular/material/dialog';
import { MatPaginator } from '@angular/material/paginator';
import { MatSort } from '@angular/material/sort';
import { MatTableDataSource } from '@angular/material/table';
import {
  AdminContainerView, ConfirmationDialogComponent, CoreModule, Error, TableFilterComponent
} from 'ngx-inception/core';
import { finalize, first } from 'rxjs/operators';
import { ReportDefinitionSummary } from '../services/report-definition-summary';
import { ReportingService } from '../services/reporting.service';

/**
 * The ReportDefinitionsComponent class implements the Report Definitions component.
 *
 * @author Marcus Portmann
 */
@Component({
  selector: 'inception-reporting-report-definitions',
  imports: [CoreModule, TableFilterComponent],
  templateUrl: 'report-definitions.component.html',
  styleUrls: ['report-definitions.component.css']
})
export class ReportDefinitionsComponent extends AdminContainerView implements AfterViewInit {
  dataSource: MatTableDataSource<ReportDefinitionSummary> =
    new MatTableDataSource<ReportDefinitionSummary>();

  displayedColumns = ['name', 'actions'];

  @HostBinding('class') hostClass = 'flex flex-column flex-fill';

  @ViewChild(MatPaginator, { static: true }) paginator!: MatPaginator;

  @ViewChild(MatSort, { static: true }) sort!: MatSort;

  readonly title = $localize`:@@reporting_report_definitions_title:Report Definitions`;

  constructor(private reportingService: ReportingService) {
    super();

    // Set the data source filter
    this.dataSource.filterPredicate = (data, filter): boolean =>
      data.name.toLowerCase().includes(filter);
  }

  applyFilter(filterValue: string): void {
    filterValue = filterValue.trim();
    filterValue = filterValue.toLowerCase();
    this.dataSource.filter = filterValue;
  }

  deleteReportDefinition(reportDefinitionId: string): void {
    const dialogRef: MatDialogRef<ConfirmationDialogComponent, boolean> =
      this.dialogService.showConfirmationDialog({
        message: $localize`:@@reporting_report_definitions_confirm_delete_report_definition:Are you sure you want to delete the report definition?`
      });

    dialogRef
      .afterClosed()
      .pipe(first())
      .subscribe({
        next: (confirmation: boolean | undefined) => {
          if (confirmation !== true) {
            return;
          }

          this.spinnerService.showSpinner();

          this.reportingService
            .deleteReportDefinition(reportDefinitionId)
            .pipe(
              first(),
              finalize(() => this.spinnerService.hideSpinner())
            )
            .subscribe({
              next: () => {
                this.loadReportDefinitions();
              },
              error: (error: Error) => this.handleError(error, false)
            });
        }
      });
  }

  editReportDefinition(reportDefinitionId: string): void {
    // noinspection JSIgnoredPromiseFromCall
    this.router.navigate([encodeURIComponent(reportDefinitionId) + '/edit'], {
      relativeTo: this.activatedRoute
    });
  }

  loadReportDefinitions(): void {
    this.spinnerService.showSpinner();

    this.reportingService
      .getReportDefinitionSummaries()
      .pipe(
        first(),
        finalize(() => this.spinnerService.hideSpinner())
      )
      .subscribe({
        next: (reportDefinitionSummaries: ReportDefinitionSummary[]) => {
          this.dataSource.data = reportDefinitionSummaries;
        },
        error: (error: Error) => this.handleError(error, false)
      });
  }

  newReportDefinition(): void {
    // noinspection JSIgnoredPromiseFromCall
    this.router.navigate(['new'], { relativeTo: this.activatedRoute });
  }

  ngAfterViewInit(): void {
    this.dataSource.paginator = this.paginator;
    this.dataSource.sort = this.sort;

    this.loadReportDefinitions();
  }
}
