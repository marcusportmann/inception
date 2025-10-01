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
import { User } from './user';
import { UserSortBy } from './user-sort-by';
import { Users } from './users';

/**
 * The UserDataSource class implements the user data source.
 *
 * @author Marcus Portmann
 */
export class UserDataSource implements DataSource<User> {
  private dataSubject$ = new BehaviorSubject<User[]>([]);

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

  connect(collectionViewer: CollectionViewer): Observable<User[]> {
    return this.dataSubject$.asObservable();
  }

  disconnect(collectionViewer: CollectionViewer): void {
    this.dataSubject$.complete();
    this.loadingSubject$.complete();
    this.totalSubject$.complete();
  }

  /**
   * Load the users.
   *
   * @param userDirectoryId The ID for the user directory the users are associated with.
   * @param filter          The filter to apply to the users.
   * @param sortBy          The method used to sort the users e.g. by name.
   * @param sortDirection   The sort direction to apply to the users.
   * @param pageIndex       The page index.
   * @param pageSize        The page size.
   *
   * @return The users.
   */
  load(
    userDirectoryId: string,
    filter?: string,
    sortBy?: UserSortBy,
    sortDirection?: SortDirection,
    pageIndex?: number,
    pageSize?: number
  ): Observable<Users> {
    this.loadingSubject$.next(true);

    return this.securityService
      .getUsers(
        userDirectoryId,
        filter,
        sortBy,
        sortDirection,
        pageIndex,
        pageSize
      )
      .pipe(
        tap((users: Users) => {
          this.updateData(users);
        }),
        catchError((error: Error) => this.handleError(error)),
        finalize(() => this.loadingSubject$.next(false))
      );
  }

  /**
   * Handle errors during the user load operation.
   *
   * @param error The error encountered.
   *
   * @return An observable that emits the error.
   */
  private handleError(error: Error): Observable<never> {
    console.error('Failed to load the users:', error);
    this.totalSubject$.next(0);
    this.dataSubject$.next([]);
    return throwError(() => error);
  }

  /**
   * Update the data source with the fetched users.
   *
   * @param users The users to update.
   */
  private updateData(users: Users): void {
    this.totalSubject$.next(users.total);
    this.dataSubject$.next(users.users);
  }
}
