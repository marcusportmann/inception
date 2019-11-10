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

import {AfterViewInit, Component, ViewChild} from '@angular/core';
import {MatDialogRef} from '@angular/material/dialog';
import {MatPaginator} from '@angular/material/paginator';
import {MatSort} from '@angular/material/sort';
import {MatTableDataSource} from '@angular/material/table';
import {finalize, first} from 'rxjs/operators';
import {DialogService} from '../../services/dialog/dialog.service';
import {SpinnerService} from '../../services/layout/spinner.service';
import {I18n} from '@ngx-translate/i18n-polyfill';
import {Error} from '../../errors/error';
import {ActivatedRoute, Router} from '@angular/router';
import {ConfirmationDialogComponent} from '../../components/dialogs';
import {SystemUnavailableError} from '../../errors/system-unavailable-error';
import {AccessDeniedError} from '../../errors/access-denied-error';
import {AdminContainerView} from '../../components/layout/admin-container-view';
import {ReportDefinitionSummary} from "../../services/reporting/report-definition-summary";
import {ReportingService} from "../../services/reporting/reporting.service";
import {ReportingServiceError} from "../../services/reporting/reporting.service.errors";

/**
 * The ReportDefinitionsComponent class implements the Report Definitions component.
 *
 * @author Marcus Portmann
 */
@Component({
  templateUrl: 'report-definitions.component.html',
  styleUrls: ['report-definitions.component.css'],
  host: {
    'class': 'flex flex-column flex-fill',
  }
})
export class ReportDefinitionsComponent extends AdminContainerView implements AfterViewInit {

  dataSource: MatTableDataSource<ReportDefinitionSummary> = new MatTableDataSource<ReportDefinitionSummary>();

  displayedColumns = ['name', 'actions'];

  @ViewChild(MatPaginator, {static: true}) paginator?: MatPaginator;

  @ViewChild(MatSort, {static: true}) sort?: MatSort;

  constructor(private router: Router, private activatedRoute: ActivatedRoute, private i18n: I18n,
              private reportingService: ReportingService, private dialogService: DialogService,
              private spinnerService: SpinnerService) {
    super();

    // Set the data source filter
    this.dataSource.filterPredicate =
      (data, filter): boolean => data.name.toLowerCase().includes(filter);
  }

  get title(): string {
    return this.i18n({
      id: '@@reporting_report_definitions_component_title',
      value: 'Report Definitions'
    })
  }

  applyFilter(filterValue: string): void {
    filterValue = filterValue.trim();
    filterValue = filterValue.toLowerCase();
    this.dataSource.filter = filterValue;
  }

  deleteReportDefinition(reportDefinitionId: string): void {
    const dialogRef: MatDialogRef<ConfirmationDialogComponent, boolean> = this.dialogService.showConfirmationDialog(
      {
        message: this.i18n({
          id: '@@reporting_report_definitions_component_confirm_delete_report_definition',
          value: 'Are you sure you want to delete the report definition?'
        })
      });

    dialogRef.afterClosed()
      .pipe(first())
      .subscribe((confirmation: boolean | undefined) => {
        if (confirmation === true) {
          this.spinnerService.showSpinner();

          this.reportingService.deleteReportDefinition(reportDefinitionId)
            .pipe(first(), finalize(() => this.spinnerService.hideSpinner()))
            .subscribe(() => {
              this.loadReportDefinitions();
            }, (error: Error) => {
              // noinspection SuspiciousTypeOfGuard
              if ((error instanceof ReportingServiceError) || (error instanceof AccessDeniedError) ||
                (error instanceof SystemUnavailableError)) {
                // noinspection JSIgnoredPromiseFromCall
                this.router.navigateByUrl('/error/send-error-report', {state: {error}});
              } else {
                this.dialogService.showErrorDialog(error);
              }
            });
        }
      });
  }

  editReportDefinition(reportDefinitionId: string): void {
    // noinspection JSIgnoredPromiseFromCall
    this.router.navigate([encodeURIComponent(reportDefinitionId) + '/edit'],
      {relativeTo: this.activatedRoute});
  }

  loadReportDefinitions(): void {
    this.spinnerService.showSpinner();

    this.reportingService.getReportDefinitionSummaries()
      .pipe(first(), finalize(() => this.spinnerService.hideSpinner()))
      .subscribe((reportDefinitionSummaries: ReportDefinitionSummary[]) => {
        this.dataSource.data = reportDefinitionSummaries;
      }, (error: Error) => {
        // noinspection SuspiciousTypeOfGuard
        if ((error instanceof ReportingService) || (error instanceof AccessDeniedError) ||
          (error instanceof SystemUnavailableError)) {
          // noinspection JSIgnoredPromiseFromCall
          this.router.navigateByUrl('/error/send-error-report', {state: {error}});
        } else {
          this.dialogService.showErrorDialog(error);
        }
      });
  }

  newReportDefinition(): void {
    // noinspection JSIgnoredPromiseFromCall
    this.router.navigate(['new'], {relativeTo: this.activatedRoute});
  }

  ngAfterViewInit(): void {
    this.dataSource.paginator = this.paginator!;
    this.dataSource.sort = this.sort!;

    this.loadReportDefinitions();
  }
}

