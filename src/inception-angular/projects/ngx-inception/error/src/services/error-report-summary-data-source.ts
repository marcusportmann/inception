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

import { CollectionViewer, DataSource } from '@angular/cdk/collections';
import { SortDirection } from 'ngx-inception/core';
import { BehaviorSubject, Observable, tap, throwError } from 'rxjs';
import { catchError, finalize } from 'rxjs/operators';
import { ErrorReportSortBy } from './error-report-sort-by';
import { ErrorReportSummaries } from './error-report-summaries';
import { ErrorReportSummary } from './error-report-summary';
import { ErrorService } from './error.service';

/**
 * The ErrorReportSummaryDataSource class implements the error report summary data source.
 *
 * @author Marcus Portmann
 */
export class ErrorReportSummaryDataSource implements DataSource<ErrorReportSummary> {
  private dataSubject$ = new BehaviorSubject<ErrorReportSummary[]>([]);

  private loadingSubject$ = new BehaviorSubject<boolean>(false);

  private totalSubject$ = new BehaviorSubject<number>(0);

  total$ = this.totalSubject$.asObservable();

  constructor(private errorService: ErrorService) {}

  /**
   * Clear the data source.
   */
  // noinspection JSUnusedGlobalSymbols
  clear(): void {
    this.totalSubject$.next(0);
    this.dataSubject$.next([]);
  }

  connect(collectionViewer: CollectionViewer): Observable<ErrorReportSummary[]> {
    void collectionViewer;
    return this.dataSubject$.asObservable();
  }

  disconnect(collectionViewer: CollectionViewer): void {
    void collectionViewer;

    this.dataSubject$.complete();
    this.loadingSubject$.complete();
    this.totalSubject$.complete();
  }

  /**
   * Load the error report summaries.
   *
   * @param filter        The filter to apply to the error report summaries.
   * @param fromDate      ISO 8601 format date value for the date to retrieve the error report
   *                      summaries from.
   * @param toDate        ISO 8601 format date value for the date to retrieve the error report
   *                      summaries to.
   * @param sortBy        The method used to sort the error report summaries e.g., by who submitted
   *                      them.
   * @param sortDirection The sort direction to apply to the error report summaries.
   * @param pageIndex     The page index.
   * @param pageSize      The page size.
   *
   * @return The error report summaries.
   */
  load(
    filter?: string,
    fromDate?: string,
    toDate?: string,
    sortBy?: ErrorReportSortBy,
    sortDirection?: SortDirection,
    pageIndex?: number,
    pageSize?: number
  ): Observable<ErrorReportSummaries> {
    this.loadingSubject$.next(true);

    return this.errorService
      .getErrorReportSummaries(filter, fromDate, toDate, sortBy, sortDirection, pageIndex, pageSize)
      .pipe(
        tap((errorReportSummaries: ErrorReportSummaries) => {
          this.updateData(errorReportSummaries);
        }),
        catchError((error: Error) => this.handleError(error)),
        finalize(() => this.loadingSubject$.next(false))
      );
  }

  /**
   * Handle errors during the error report summaries load operation.
   *
   * @param error The error encountered.
   *
   * @return An observable that emits the error.
   */
  private handleError(error: Error): Observable<never> {
    console.error('Failed to load the error report summaries:', error);
    this.totalSubject$.next(0);
    this.dataSubject$.next([]);
    return throwError(() => error);
  }

  /**
   * Update the data source with the fetched error report summaries.
   *
   * @param errorReportSummaries The error report summaries to update.
   */
  private updateData(errorReportSummaries: ErrorReportSummaries): void {
    this.totalSubject$.next(errorReportSummaries.total);
    this.dataSubject$.next(errorReportSummaries.errorReportSummaries);
  }
}
