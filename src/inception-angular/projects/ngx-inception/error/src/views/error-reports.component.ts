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

import { AfterViewInit, Component, HostBinding, OnDestroy, ViewChild } from '@angular/core';
import { FormControl, Validators } from '@angular/forms';
import { MatPaginator } from '@angular/material/paginator';
import { MatSort } from '@angular/material/sort';
import { add, isWithinInterval } from 'date-fns';
import {
  AdminContainerView, CoreModule, ISO8601Util, SortDirection, TableFilterComponent
} from 'ngx-inception/core';
import { merge, Observable, Subject } from 'rxjs';
import { debounceTime, finalize, takeUntil } from 'rxjs/operators';
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
  selector: 'inception-error-error-reports',
  standalone: true,
  imports: [CoreModule, TableFilterComponent],
  templateUrl: 'error-reports.component.html',
  styleUrls: ['error-reports.component.css']
})
export class ErrorReportsComponent extends AdminContainerView implements AfterViewInit, OnDestroy {
  dataSource: ErrorReportSummaryDataSource;

  displayedColumns = ['created', 'who', 'description', 'actions'];

  fromDateControl: FormControl<Date | null>;

  @HostBinding('class') hostClass = 'flex flex-column flex-fill';

  @ViewChild(MatPaginator, { static: true }) paginator!: MatPaginator;

  @ViewChild(MatSort, { static: true }) sort!: MatSort;

  @ViewChild(TableFilterComponent, { static: true })
  tableFilter!: TableFilterComponent;

  readonly title = $localize`:@@error_error_reports_title:Error Reports`;

  toDateControl: FormControl<Date | null>;

  private destroy$ = new Subject<void>();

  constructor(private errorService: ErrorService) {
    super();

    // Initialize form controls
    this.fromDateControl = new FormControl<Date | null>(add(new Date(), { months: -1 }), {
      validators: [Validators.required]
    });

    this.toDateControl = new FormControl<Date | null>(new Date(), {
      validators: [Validators.required]
    });

    // Initialize the data source
    this.dataSource = new ErrorReportSummaryDataSource(this.errorService);
  }

  dateRangeChanged(): void {
    if (this.fromDateControl.valid && this.toDateControl.valid) {
      this.loadData();
    }
  }

  dateRangeFilter = (toDateCheck: Date | null): boolean => {
    if (!toDateCheck) {
      return false;
    }

    const minAllowed = add(new Date(), { years: -1 });
    const maxAllowed = new Date();

    const fromValue = this.fromDateControl?.value as Date | null;
    const effectiveMin = fromValue && fromValue > minAllowed ? fromValue : minAllowed;

    return isWithinInterval(toDateCheck, {
      start: effectiveMin,
      end: maxAllowed
    });
  };

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
        error: (error) => this.handleError(error, false)
      });
  }

  private loadErrorReportSummaries(): Observable<ErrorReportSummaries> {
    const filter = this.tableFilter.filter?.trim().toLowerCase() || '';

    let sortBy = ErrorReportSortBy.Created;
    let sortDirection = SortDirection.Descending;

    if (this.sort.active) {
      sortBy = this.sort.active === 'who' ? ErrorReportSortBy.Who : ErrorReportSortBy.Created;
      sortDirection =
        this.sort.direction === 'asc' ? SortDirection.Ascending : SortDirection.Descending;
    }

    const fromDate =
      this.formatDate(this.fromDateControl.value) ||
      ISO8601Util.toString(add(new Date(), { months: -1 }));
    const toDate = this.formatDate(this.toDateControl.value) || ISO8601Util.toString(new Date());

    return this.dataSource.load(
      filter,
      fromDate,
      toDate,
      sortBy,
      sortDirection,
      this.paginator.pageIndex,
      this.paginator.pageSize
    );
  }
}
