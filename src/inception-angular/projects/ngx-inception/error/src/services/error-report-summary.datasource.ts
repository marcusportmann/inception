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

import {CollectionViewer, DataSource} from '@angular/cdk/collections';
import {SortDirection} from 'ngx-inception/core';
import {Observable, ReplaySubject, Subject} from 'rxjs';
import {first} from 'rxjs/operators';
import {ErrorReportSortBy} from './error-report-sort-by';
import {ErrorReportSummaries} from './error-report-summaries';
import {ErrorReportSummary} from './error-report-summary';
import {ErrorService} from './error.service';

/**
 * The ErrorReportSummaryDatasource class implements the error report summary data source.
 *
 * @author Marcus Portmann
 */
export class ErrorReportSummaryDatasource implements DataSource<ErrorReportSummary> {

  private dataSubject$: Subject<ErrorReportSummary[]> = new ReplaySubject<ErrorReportSummary[]>();

  private loadingSubject$: Subject<boolean> = new ReplaySubject<boolean>();

  loading$ = this.loadingSubject$.asObservable();

  private totalSubject$: Subject<number> = new ReplaySubject<number>();

  total$ = this.totalSubject$.asObservable();

  constructor(private errorService: ErrorService) {
  }

  /**
   * Clear the data source.
   */
  clear(): void {
    this.totalSubject$.next(0);
    this.dataSubject$.next([]);
  }

  connect(collectionViewer: CollectionViewer): Observable<ErrorReportSummary[] | ReadonlyArray<ErrorReportSummary>> {
    return this.dataSubject$.asObservable();
  }

  disconnect(collectionViewer: CollectionViewer): void {
    this.dataSubject$.complete();
    this.loadingSubject$.complete();
  }

  /**
   * Load the error report summaries.
   *
   * @param filter        The optional filter to apply to the error report summaries.
   * @param fromDate      ISO 8601 format date value for the date to retrieve the error report
   *                      summaries from.
   * @param toDate        ISO 8601 format date value for the date to retrieve the error report
   *                      summaries to.
   * @param sortBy        The optional method used to sort the error report summaries e.g. by who
   *                      submitted them.
   * @param sortDirection The optional sort direction to apply to the error report summaries.
   * @param pageIndex     The optional page index.
   * @param pageSize      The optional page size.
   */
  load(filter?: string, fromDate?: string, toDate?: string, sortBy?: ErrorReportSortBy,
       sortDirection?: SortDirection, pageIndex?: number, pageSize?: number): void {
    this.loadingSubject$.next(true);

    this.errorService.getErrorReportSummaries(filter, fromDate, toDate, sortBy, sortDirection,
      pageIndex, pageSize)
    .pipe(first())
    .subscribe((errorReportSummaries: ErrorReportSummaries) => {
      this.loadingSubject$.next(false);

      this.totalSubject$.next(errorReportSummaries.total);

      this.dataSubject$.next(errorReportSummaries.errorReportSummaries);
    }, (error: Error) => {
      this.loadingSubject$.next(false);

      this.totalSubject$.next(0);

      this.loadingSubject$.error(error);
    });
  }
}
