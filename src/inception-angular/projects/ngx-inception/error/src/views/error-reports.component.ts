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

import {AfterViewInit, Component, HostBinding, OnDestroy, OnInit, ViewChild} from '@angular/core';
import {FormControl, Validators} from '@angular/forms';
import {MatPaginator} from '@angular/material/paginator';
import {MatSort} from '@angular/material/sort';
import {ActivatedRoute, Router} from '@angular/router';
import {add, isWithinInterval} from 'date-fns';
import {
  AccessDeniedError, AdminContainerView, DialogService, Error, InvalidArgumentError, ISO8601Util,
  ServiceUnavailableError, SortDirection, SpinnerService, TableFilterComponent
} from 'ngx-inception/core';
import {merge, Subscription} from 'rxjs';
import {ErrorReportSortBy} from '../services/error-report-sort-by';
import {ErrorReportSummaryDatasource} from '../services/error-report-summary.datasource';
import {ErrorService} from '../services/error.service';

/**
 * The ErrorReportsComponent class implements the error reports component.
 *
 * @author Marcus Portmann
 */
@Component({
  templateUrl: 'error-reports.component.html',
  styleUrls: ['error-reports.component.css']
})
export class ErrorReportsComponent extends AdminContainerView implements AfterViewInit, OnInit, OnDestroy {

  dataSource: ErrorReportSummaryDatasource;

  displayedColumns = ['created', 'who', 'description', 'actions'];

  fromDateControl: FormControl;

  @HostBinding('class') hostClass = 'flex flex-column flex-fill';

  @ViewChild(MatPaginator, {static: true}) paginator!: MatPaginator;

  @ViewChild(MatSort, {static: true}) sort!: MatSort;

  @ViewChild(TableFilterComponent, {static: true}) tableFilter!: TableFilterComponent;

  toDateControl: FormControl;

  private subscriptions: Subscription = new Subscription();

  constructor(private router: Router, private activatedRoute: ActivatedRoute,
              private errorService: ErrorService, private dialogService: DialogService,
              private spinnerService: SpinnerService) {
    super();

    this.fromDateControl = new FormControl({
      value: add(new Date(), {months: -1}),
      disabled: false
    }, [Validators.required]);

    this.toDateControl = new FormControl({
      value: new Date(),
      disabled: false
    }, [Validators.required]);

    this.dataSource = new ErrorReportSummaryDatasource(this.errorService);
  }

  get title(): string {
    return $localize`:@@error_error_reports_title:Error Reports`
  }

  dateRangeChanged(): void {
    this.loadErrorReportSummaries();
  }

  dateRangeFilter(toDateCheck: Date | null): boolean {
    let minDate: Date = add(new Date, {years: -1});
    let maxDate: Date = new Date();

    if (!toDateCheck) {
      toDateCheck = maxDate;
    }

    return isWithinInterval(toDateCheck, {
      start: minDate,
      end: maxDate
    });
  }


  loadErrorReportSummaries(): void {
    let filter = '';

    if (!!this.tableFilter.filter) {
      filter = this.tableFilter.filter;
      filter = filter.trim();
      filter = filter.toLowerCase();
    }

    let sortBy: ErrorReportSortBy = ErrorReportSortBy.Created;
    let sortDirection = SortDirection.Descending;

    if (!!this.sort.active) {
      if (this.sort.active === 'created') {
        sortBy = ErrorReportSortBy.Created;
      } else if (this.sort.active === 'who') {
        sortBy = ErrorReportSortBy.Who;
      }

      if (this.sort.direction === 'asc') {
        sortDirection = SortDirection.Ascending;
      }
    }

    let fromDate: string;
    let toDate: string;

    if (this.fromDateControl.value && this.toDateControl.value) {
      if (typeof this.fromDateControl.value === 'string') {
        fromDate = this.fromDateControl.value;
      } else {
        fromDate = ISO8601Util.toString(this.fromDateControl.value);
      }

      if (typeof this.toDateControl.value === 'string') {
        toDate = this.toDateControl.value;
      } else {
        toDate = ISO8601Util.toString(this.toDateControl.value);
      }
    } else {
      fromDate = ISO8601Util.toString(add(new Date(), {months: -1}));
      toDate = ISO8601Util.toString(new Date());
    }

    this.dataSource.load(filter, fromDate, toDate, sortBy, sortDirection, this.paginator.pageIndex,
      this.paginator.pageSize);
  }

  ngAfterViewInit(): void {
    this.subscriptions.add(this.dataSource.loading$.subscribe((next: boolean) => {
      if (next) {
        this.spinnerService.showSpinner();
      } else {
        this.spinnerService.hideSpinner();
      }
    }, (error: Error) => {
      // noinspection SuspiciousTypeOfGuard
      if ((error instanceof AccessDeniedError) || (error instanceof InvalidArgumentError) || (error instanceof ServiceUnavailableError)) {
        // noinspection JSIgnoredPromiseFromCall
        this.router.navigateByUrl('/error/send-error-report', {state: {error}});
      } else {
        this.dialogService.showErrorDialog(error);
      }
    }));

    this.subscriptions.add(this.sort.sortChange.subscribe(() => {
      this.paginator.pageIndex = 0;
    }));

    this.subscriptions.add(this.tableFilter.changed.subscribe(() => {
      this.paginator.pageIndex = 0;
    }));

    this.subscriptions.add(
      merge(this.sort.sortChange, this.tableFilter.changed, this.paginator.page)
      .subscribe(() => {
        this.loadErrorReportSummaries();
      }));

    this.loadErrorReportSummaries();
  }

  ngOnDestroy(): void {
    this.subscriptions.unsubscribe();
  }

  ngOnInit(): void {
    this.sort.active = 'created';
    this.sort.direction = 'desc' as SortDirection;
  }

  viewErrorReport(errorReportId: string): void {
    // noinspection JSIgnoredPromiseFromCall
    this.router.navigate([encodeURIComponent(errorReportId)], {relativeTo: this.activatedRoute});
  }
}

