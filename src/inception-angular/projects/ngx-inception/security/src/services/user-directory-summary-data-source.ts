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
import { UserDirectorySummaries } from './user-directory-summaries';
import { UserDirectorySummary } from './user-directory-summary';

/**
 * The UserDirectorySummaryDataSource class implements the token summary data source.
 *
 * @author Marcus Portmann
 */
export class UserDirectorySummaryDataSource implements DataSource<UserDirectorySummary> {
  private dataSubject$ = new BehaviorSubject<UserDirectorySummary[]>([]);

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

  connect(collectionViewer: CollectionViewer): Observable<UserDirectorySummary[]> {
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
   * Load the user directory summaries.
   *
   * @param filter        The filter to apply to the user directory summaries.
   * @param sortDirection The sort direction to apply to the user directory summaries.
   * @param pageIndex     The page index.
   * @param pageSize      The page size.
   *
   * @return The user directory summaries.
   */
  load(
    filter?: string,
    sortDirection?: SortDirection,
    pageIndex?: number,
    pageSize?: number
  ): Observable<UserDirectorySummaries> {
    this.loadingSubject$.next(true);

    return this.securityService
      .getUserDirectorySummaries(filter, sortDirection, pageIndex, pageSize)
      .pipe(
        tap((userDirectorySummaries: UserDirectorySummaries) => {
          this.updateData(userDirectorySummaries);
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
    console.error('Failed to load the user directory summaries:', error);
    this.totalSubject$.next(0);
    this.dataSubject$.next([]);
    return throwError(() => error);
  }

  /**
   * Update the data source with the fetched user directory summaries.
   *
   * @param userDirectorySummaries The user directory summaries to update.
   */
  private updateData(userDirectorySummaries: UserDirectorySummaries): void {
    this.totalSubject$.next(userDirectorySummaries.total);
    this.dataSubject$.next(userDirectorySummaries.userDirectorySummaries);
  }
}
