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
import { PolicySortBy } from './policy-sort-by';
import { PolicySummaries } from './policy-summaries';
import { PolicySummary } from './policy-summary';
import { SecurityService } from './security.service';

/**
 * The PolicySummaryDataSource class implements the policy summary data source.
 *
 * @author Marcus Portmann
 */
export class PolicySummaryDataSource implements DataSource<PolicySummary> {
  private dataSubject$ = new BehaviorSubject<PolicySummary[]>([]);

  private loadingSubject$ = new BehaviorSubject<boolean>(false);

  loading$ = this.loadingSubject$.asObservable();

  private totalSubject$ = new BehaviorSubject<number>(0);

  total$ = this.totalSubject$.asObservable();

  constructor(private securityService: SecurityService) {}

  /**
   * Clear the data source.
   */
  clear(): void {
    this.totalSubject$.next(0);
    this.dataSubject$.next([]);
  }

  connect(collectionViewer: CollectionViewer): Observable<PolicySummary[]> {
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
   * Load the policy summaries.
   *
   * @param filter        The filter to apply to the policy summaries.
   * @param sortBy         The method used to sort the policy summaries e.g., by name.
   * @param sortDirection The sort direction to apply to the policy summaries.
   * @param pageIndex     The page index.
   * @param pageSize      The page size.
   *
   * @return The policy summaries.
   */
  load(
    filter?: string,
    sortBy?: PolicySortBy,
    sortDirection?: SortDirection,
    pageIndex?: number,
    pageSize?: number
  ): Observable<PolicySummaries> {
    this.loadingSubject$.next(true);

    return this.securityService
      .getPolicySummaries(filter, sortBy, sortDirection, pageIndex, pageSize)
      .pipe(
        tap((policySummaries: PolicySummaries) => {
          this.updateData(policySummaries);
        }),
        catchError((error: Error) => this.handleError(error)),
        finalize(() => this.loadingSubject$.next(false))
      );
  }

  /**
   * Handle errors during the load operation.
   *
   * @param error The error encountered.
   *
   * @return An observable that emits the error.
   */
  private handleError(error: Error): Observable<never> {
    console.error('Failed to load the policy summaries:', error);
    this.totalSubject$.next(0);
    this.dataSubject$.next([]);
    return throwError(() => error);
  }

  /**
   * Update the data source with the fetched policy summaries.
   *
   * @param policySummaries The policy summaries to update.
   */
  private updateData(policySummaries: PolicySummaries): void {
    this.totalSubject$.next(policySummaries.total);
    this.dataSubject$.next(policySummaries.policySummaries);
  }
}
