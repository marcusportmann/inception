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
  AfterViewInit, ChangeDetectionStrategy, Component, HostBinding, inject, ViewChild
} from '@angular/core';
import { FormControl, Validators } from '@angular/forms';
import { MatPaginator } from '@angular/material/paginator';
import { MatSort } from '@angular/material/sort';
import { add, isWithinInterval } from 'date-fns';
import {
  CoreModule, ISO8601Util, SortDirection, StatefulListView, TableFilterComponent
} from 'ngx-inception/core';
import { Observable } from 'rxjs';
import { finalize, takeUntil } from 'rxjs/operators';
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
  styleUrls: ['error-reports.component.css'],
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class ErrorReportsComponent extends StatefulListView implements AfterViewInit {
  readonly dataSource: ErrorReportSummaryDataSource;

  readonly defaultSortActive = 'created';

  override readonly defaultSortDirection = 'desc';

  readonly displayedColumns = ['created', 'who', 'description', 'actions'] as const;

  fromDateControl: FormControl<Date | null>;

  @HostBinding('class') hostClass = 'flex flex-column flex-fill';

  readonly listStateKey = 'error.error-reports';

  @ViewChild(MatPaginator, { static: true }) override paginator!: MatPaginator;

  @ViewChild(MatSort, { static: true }) override sort!: MatSort;

  @ViewChild(TableFilterComponent, { static: true })
  override tableFilter!: TableFilterComponent;

  readonly title = $localize`:@@error_error_reports_title:Error Reports`;

  toDateControl: FormControl<Date | null>;

  private readonly errorService = inject(ErrorService);

  /** Whether this navigation requested a state reset (from the sidebar). */
  private readonly resetStateRequested: boolean;

  constructor() {
    super();

    // Read the reset flag from the current navigation
    const nav = this.router.currentNavigation();

    this.resetStateRequested = !!nav?.extras.state?.['resetState'];

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
      this.onStateChangingEvent(true);
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
    this.initializeStatefulList(this.resetStateRequested, () => this.loadData());

    // Stabilize view after mutating sort/paginator
    this.changeDetectorRef.detectChanges();
  }

  viewErrorReport(errorReportId: string): void {
    void this.router.navigate([errorReportId], {
      relativeTo: this.activatedRoute
    });
  }

  private formatDate(value: Date | string | null): string | null {
    if (!value) {
      return null;
    }
    return typeof value === 'string' ? value : ISO8601Util.toString(value);
  }

  private loadData(): void {
    this.spinnerService.showSpinner();
    this.loadErrorReportSummaries()
      .pipe(
        finalize(() => this.spinnerService.hideSpinner()),
        takeUntil(this.destroy$)
      )
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

    if (this.sort.active && this.sort.direction) {
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
