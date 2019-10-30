/*
 * Copyright 2019 Marcus Portmann
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
import {Observable, ReplaySubject, Subject} from 'rxjs';
import {SecurityService} from './security.service';
import {SortDirection} from './sort-direction';

import {first} from 'rxjs/operators';
import {User} from './user';
import {Users} from './users';
import {SessionService} from '../session/session.service';
import {UserSortBy} from './user-sort-by';

/**
 * The UserDatasource class implements the user data source.
 *
 * @author Marcus Portmann
 */
export class UserDatasource implements DataSource<User> {

  private totalSubject$: Subject<number> = new ReplaySubject<number>();

  private dataSubject$: Subject<User[]> = new ReplaySubject<User[]>();

  private loadingSubject$: Subject<boolean> = new ReplaySubject<boolean>();

  total$ = this.totalSubject$.asObservable();

  loading$ = this.loadingSubject$.asObservable();

  constructor(private sessionService: SessionService, private securityService: SecurityService) {
  }

  /**
   * Clear the data source.
   */
  clear(): void {
    this.totalSubject$.next(0);
    this.dataSubject$.next([]);
  }

  connect(collectionViewer: CollectionViewer): Observable<User[] | ReadonlyArray<User>> {
    return this.dataSubject$.asObservable();
  }

  disconnect(collectionViewer: CollectionViewer): void {
    this.dataSubject$.complete();
    this.loadingSubject$.complete();
  }

  /**
   * Load the users.
   *
   * @param userDirectoryId The ID used to uniquely identify the user directory the users are
   *                        associated with.
   * @param filter          The optional filter to apply to the users.
   * @param sortBy          The optional method used to sort the users e.g. by last name.
   * @param sortDirection   The optional sort direction to apply to the users.
   * @param pageIndex       The optional page index.
   * @param pageSize        The optional page size.
   */
  load(userDirectoryId: string, filter?: string, sortBy?: UserSortBy, sortDirection?: SortDirection,
       pageIndex?: number, pageSize?: number): void {
    this.loadingSubject$.next(true);

    this.securityService.getUsers(userDirectoryId, filter, sortBy, sortDirection, pageIndex,
      pageSize)
      .pipe(first())
      .subscribe((users: Users) => {
        this.loadingSubject$.next(false);

        this.totalSubject$.next(users.total);

        this.dataSubject$.next(users.users);
      }, (error: Error) => {
        this.loadingSubject$.next(false);

        this.totalSubject$.next(0);

        this.loadingSubject$.error(error);
      });
  }
}
