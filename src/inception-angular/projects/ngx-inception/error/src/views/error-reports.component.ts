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

import {
  AfterViewInit,
  Component,
  HostBinding,
  OnDestroy,
  ViewChild
} from '@angular/core';
import { FormControl, Validators } from '@angular/forms';
import { MatPaginator } from '@angular/material/paginator';
import { MatSort } from '@angular/material/sort';
import { ActivatedRoute, Router } from '@angular/router';
import { add, isWithinInterval } from 'date-fns';
import {
  AccessDeniedError,
  AdminContainerView,
  DialogService,
  Error,
  InvalidArgumentError,
  ISO8601Util,
  ServiceUnavailableError,
  SortDirection,
  SpinnerService,
  TableFilterComponent
} from 'ngx-inception/core';
import { merge, Observable, Subject, throwError } from 'rxjs';
import { catchError, debounceTime, finalize, takeUntil } from 'rxjs/operators';
import { ErrorReportSortBy } from '../services/error-report-sort-by';
import { ErrorReportSummaries } from '../services/error-report-summaries';
import { ErrorReportSummaryDataSource } from '../services/error-report-summary-data-source';
import { ErrorService } from '../services/error.service';

/**
 * The ErrorReportsComponent class implements the error reports component.
 *
 * @author Marcus Portmann
 */
@Component({
  templateUrl: 'error-reports.component.html',
  styleUrls: ['error-reports.component.css'],
  standalone: false
})
export class ErrorReportsComponent
  extends AdminContainerView
  implements AfterViewInit, OnDestroy
{
  dataSource: ErrorReportSummaryDataSource;

  displayedColumns = ['created', 'who', 'description', 'actions'];

  fromDateControl: FormControl;

  @HostBinding('class') hostClass = 'flex flex-column flex-fill';

  @ViewChild(MatPaginator, { static: true }) paginator!: MatPaginator;

  @ViewChild(MatSort, { static: true }) sort!: MatSort;

  @ViewChild(TableFilterComponent, { static: true })
  tableFilter!: TableFilterComponent;

  toDateControl: FormControl;

  private destroy$ = new Subject<void>();

  constructor(
    private router: Router,
    private activatedRoute: ActivatedRoute,
    private errorService: ErrorService,
    private dialogService: DialogService,
    private spinnerService: SpinnerService
  ) {
    super();

    // Initialize form controls
    this.fromDateControl = new FormControl(add(new Date(), { months: -1 }), [
      Validators.required
    ]);
    this.toDateControl = new FormControl(new Date(), [Validators.required]);

    // Initialize the data source
    this.dataSource = new ErrorReportSummaryDataSource(this.errorService);
  }

  get title(): string {
    return $localize`:@@error_error_reports_title:Error Reports`;
  }

  dateRangeChanged(): void {
    this.loadData();
  }

  dateRangeFilter(toDateCheck: Date | null): boolean {
    const minDate = add(new Date(), { years: -1 });
    const maxDate = new Date();
    return toDateCheck
      ? isWithinInterval(toDateCheck, {
          start: minDate,
          end: maxDate
        })
      : false;
  }

  ngAfterViewInit(): void {
    this.initializeDataLoaders();
    this.loadData();
  }

  ngOnDestroy(): void {
    this.destroy$.next();
    this.destroy$.complete();
  }

  viewErrorReport(errorReportId: string): void {
    // noinspection JSIgnoredPromiseFromCall
    this.router.navigate([encodeURIComponent(errorReportId)], {
      relativeTo: this.activatedRoute
    });
  }

  private formatDate(value: Date | string | null): string | null {
    if (!value) return null;
    return typeof value === 'string' ? value : ISO8601Util.toString(value);
  }

  private handleError(error: Error): Observable<never> {
    if (
      error instanceof AccessDeniedError ||
      error instanceof InvalidArgumentError ||
      error instanceof ServiceUnavailableError
    ) {
      // noinspection JSIgnoredPromiseFromCall
      this.router.navigateByUrl('/error/send-error-report', {
        state: { error }
      });
    } else {
      this.dialogService.showErrorDialog(error);
    }
    return throwError(() => error);
  }

  private initializeDataLoaders(): void {
    // Reset paginator and load data on sort, filter, or pagination change
    this.sort.sortChange
      .pipe(takeUntil(this.destroy$))
      .subscribe(() => (this.paginator.pageIndex = 0));

    merge(this.sort.sortChange, this.tableFilter.changed, this.paginator.page)
      .pipe(debounceTime(200), takeUntil(this.destroy$))
      .subscribe(() => this.loadData());
  }

  private loadData(): void {
    this.spinnerService.showSpinner();
    this.loadErrorReportSummaries()
      .pipe(finalize(() => this.spinnerService.hideSpinner()))
      .subscribe({
        next: () => {
          // Load complete
        },
        error: (error) => this.handleError(error)
      });
  }

  private loadErrorReportSummaries(): Observable<ErrorReportSummaries> {
    const filter = this.tableFilter.filter?.trim().toLowerCase() || '';

    let sortBy = ErrorReportSortBy.Created;
    let sortDirection = SortDirection.Descending;

    if (this.sort.active) {
      sortBy =
        this.sort.active === 'who'
          ? ErrorReportSortBy.Who
          : ErrorReportSortBy.Created;
      sortDirection =
        this.sort.direction === 'asc'
          ? SortDirection.Ascending
          : SortDirection.Descending;
    }

    const fromDate =
      this.formatDate(this.fromDateControl.value) ||
      ISO8601Util.toString(add(new Date(), { months: -1 }));
    const toDate =
      this.formatDate(this.toDateControl.value) ||
      ISO8601Util.toString(new Date());

    return this.dataSource
      .load(
        filter,
        fromDate,
        toDate,
        sortBy,
        sortDirection,
        this.paginator.pageIndex,
        this.paginator.pageSize
      )
      .pipe(catchError((error) => this.handleError(error)));
  }
}
