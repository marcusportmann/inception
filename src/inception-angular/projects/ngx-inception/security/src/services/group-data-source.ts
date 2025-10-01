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
import { Group } from './group';
import { Groups } from './groups';
import { SecurityService } from './security.service';

/**
 * The GroupDataSource class implements the group data source.
 *
 * @author Marcus Portmann
 */
export class GroupDataSource implements DataSource<Group> {
  private dataSubject$ = new BehaviorSubject<Group[]>([]);

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

  connect(collectionViewer: CollectionViewer): Observable<Group[]> {
    return this.dataSubject$.asObservable();
  }

  disconnect(collectionViewer: CollectionViewer): void {
    this.dataSubject$.complete();
    this.loadingSubject$.complete();
    this.totalSubject$.complete();
  }

  /**
   * Load the groups.
   *
   * @param userDirectoryId The ID for the user directory the groups are associated with.
   * @param filter The filter to apply to the groups.
   * @param sortDirection The sort direction to apply to the groups.
   * @param pageIndex The page index.
   * @param pageSize The page size.
   *
   * @return The groups.
   */
  load(
    userDirectoryId: string,
    filter?: string,
    sortDirection?: SortDirection,
    pageIndex?: number,
    pageSize?: number
  ): Observable<Groups> {
    this.loadingSubject$.next(true);

    return this.securityService
      .getGroups(userDirectoryId, filter, sortDirection, pageIndex, pageSize)
      .pipe(
        tap((groups: Groups) => {
          this.updateData(groups);
        }),
        catchError((error: Error) => this.handleError(error)),
        finalize(() => this.loadingSubject$.next(false))
      );
  }

  /**
   * Handle errors during the group load operation.
   *
   * @param error The error encountered.
   *
   * @return An observable that emits the error.
   */
  private handleError(error: Error): Observable<never> {
    console.error('Failed to load the groups:', error);
    this.totalSubject$.next(0);
    this.dataSubject$.next([]);
    return throwError(() => error);
  }

  /**
   * Update the data source with the fetched groups.
   *
   * @param groups The groups to update.
   */
  private updateData(groups: Groups): void {
    this.totalSubject$.next(groups.total);
    this.dataSubject$.next(groups.groups);
  }
}
