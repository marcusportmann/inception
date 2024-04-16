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
import {PolicySortBy} from './policy-sort-by';
import {PolicySummaries} from './policy-summaries';
import {PolicySummary} from './policy-summary';
import {SecurityService} from './security.service';

/**
 * The PolicySummaryDatasource class implements the policy summary data source.
 *
 * @author Marcus Portmann
 */
export class PolicySummaryDatasource implements DataSource<PolicySummary> {

  private dataSubject$: Subject<PolicySummary[]> = new ReplaySubject<PolicySummary[]>(1);

  private loadingSubject$: Subject<boolean> = new ReplaySubject<boolean>(1);

  loading$ = this.loadingSubject$.asObservable();

  private totalSubject$: Subject<number> = new ReplaySubject<number>(1);

  total$ = this.totalSubject$.asObservable();

  constructor(private securityService: SecurityService) {
  }

  /**
   * Clear the data source.
   */
  clear(): void {
    this.totalSubject$.next(0);
    this.dataSubject$.next([]);
  }

  connect(collectionViewer: CollectionViewer): Observable<PolicySummary[] | ReadonlyArray<PolicySummary>> {
    return this.dataSubject$.asObservable();
  }

  disconnect(collectionViewer: CollectionViewer): void {
    this.dataSubject$.complete();
    this.loadingSubject$.complete();
  }

  /**
   * Load the policy summaries.
   *
   * @param filter        The optional filter to apply to the policy summaries.
   * @param sortDirection The optional sort direction to apply to the policy summaries.
   * @param pageIndex     The optional page index.
   * @param pageSize      The optional page size.
   */
  load(filter?: string, sortDirection?: SortDirection, pageIndex?: number,
       pageSize?: number): void {
    this.loadingSubject$.next(true);

    this.securityService.getPolicySummaries(filter, PolicySortBy.Name, sortDirection, pageIndex,
      pageSize)
    .pipe(first())
    .subscribe((policySummaries: PolicySummaries) => {
      this.loadingSubject$.next(false);

      this.totalSubject$.next(policySummaries.total);

      this.dataSubject$.next(policySummaries.policySummaries);
    }, (error: Error) => {
      this.loadingSubject$.next(false);

      this.totalSubject$.next(0);

      this.loadingSubject$.error(error);
    });
  }
}
