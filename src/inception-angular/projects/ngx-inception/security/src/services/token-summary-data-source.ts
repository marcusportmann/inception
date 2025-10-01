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
import { SecurityService } from './security.service';
import { TokenSortBy } from './token-sort-by';
import { TokenStatus } from './token-status';
import { TokenSummaries } from './token-summaries';
import { TokenSummary } from './token-summary';

/**
 * The TokenSummaryDataSource class implements the token summary data source.
 *
 * @author Marcus Portmann
 */
export class TokenSummaryDataSource implements DataSource<TokenSummary> {
  private dataSubject$ = new BehaviorSubject<TokenSummary[]>([]);

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

  connect(collectionViewer: CollectionViewer): Observable<TokenSummary[]> {
    return this.dataSubject$.asObservable();
  }

  disconnect(collectionViewer: CollectionViewer): void {
    this.dataSubject$.complete();
    this.loadingSubject$.complete();
    this.totalSubject$.complete();
  }

  /**
   * Load the token summaries.
   *
   * @param requiredStatus The required token status filter to apply to the token summaries.
   * @param filter         The filter to apply to the token summaries.
   * @param sortDirection  The sort direction to apply to the token summaries.
   * @param pageIndex      The page index.
   * @param pageSize       The page size.
   *
   * @return The token summaries.
   */
  load(
    requiredStatus: TokenStatus,
    filter?: string,
    sortDirection?: SortDirection,
    pageIndex?: number,
    pageSize?: number
  ): Observable<TokenSummaries> {
    this.loadingSubject$.next(true);

    return this.securityService
      .getTokenSummaries(
        requiredStatus,
        filter,
        TokenSortBy.Name,
        sortDirection,
        pageIndex,
        pageSize
      )
      .pipe(
        tap((tokenSummaries: TokenSummaries) => {
          this.updateData(tokenSummaries);
        }),
        catchError((error: Error) => this.handleError(error)),
        finalize(() => this.loadingSubject$.next(false))
      );
  }

  /**
   * Handle errors during the token summaries load operation.
   *
   * @param error The error encountered.
   *
   * @return An observable that emits the error.
   */
  private handleError(error: Error): Observable<never> {
    console.error('Failed to load the token summaries:', error);
    this.totalSubject$.next(0);
    this.dataSubject$.next([]);
    return throwError(() => error);
  }

  /**
   * Update the data source with the fetched token summaries.
   *
   * @param tokenSummaries The token summaries to update.
   */
  private updateData(tokenSummaries: TokenSummaries): void {
    this.totalSubject$.next(tokenSummaries.total);
    this.dataSubject$.next(tokenSummaries.tokenSummaries);
  }
}
