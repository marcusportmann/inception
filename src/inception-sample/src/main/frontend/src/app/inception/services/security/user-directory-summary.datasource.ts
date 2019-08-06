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
import {UserDirectorySummary} from './user-directory-summary';
import {UserDirectorySummaries} from './user-directory-summaries';

/**
 * The UserDirectorySummaryDatasource class implements the user directory summary data source.
 *
 * @author Marcus Portmann
 */
export class UserDirectorySummaryDatasource implements DataSource<UserDirectorySummary> {

  private totalSubject: Subject<number> = new ReplaySubject<number>();

  private dataSubject: Subject<UserDirectorySummary[]> = new ReplaySubject<UserDirectorySummary[]>();

  private loadingSubject: Subject<boolean> = new ReplaySubject<boolean>();

  total = this.totalSubject.asObservable();

  loading = this.loadingSubject.asObservable();

  constructor(private securityService: SecurityService) {
  }

  connect(collectionViewer: CollectionViewer): Observable<UserDirectorySummary[] | ReadonlyArray<UserDirectorySummary>> {
    return this.dataSubject.asObservable();
  }

  disconnect(collectionViewer: CollectionViewer): void {
    this.dataSubject.complete();
    this.loadingSubject.complete();
  }

  /**
   * Load the user directory summaries.
   *
   * @param filter        The optional filter to apply to the user directories.
   * @param sortDirection The optional sort direction to apply to the user directories.
   * @param pageIndex     The optional page index.
   * @param pageSize      The optional page size.
   */
  load(filter?: string, sortDirection?: SortDirection, pageIndex?: number,
       pageSize?: number): void {
    this.loadingSubject.next(true);

    this.securityService.getUserDirectorySummaries(filter, sortDirection, pageIndex, pageSize)
      .pipe(first())
      .subscribe((userDirectorySummaries: UserDirectorySummaries) => {
        this.loadingSubject.next(false);

        this.totalSubject.next(userDirectorySummaries.total);

        this.dataSubject.next(userDirectorySummaries.userDirectorySummaries);
      }, (error: Error) => {
        this.loadingSubject.next(false);

        this.totalSubject.next(0);

        this.loadingSubject.error(error);
      });
  }
}
