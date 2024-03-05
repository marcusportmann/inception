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
import {SecurityService} from './security.service';
import {TokenSortBy} from './token-sort-by';
import {TokenStatus} from './token-status';
import {TokenSummaries} from './token-summaries';
import {TokenSummary} from './token-summary';

/**
 * The TokenSummaryDatasource class implements the token summary data source.
 *
 * @author Marcus Portmann
 */
export class TokenSummaryDatasource implements DataSource<TokenSummary> {

  private dataSubject$: Subject<TokenSummary[]> = new ReplaySubject<TokenSummary[]>();

  private loadingSubject$: Subject<boolean> = new ReplaySubject<boolean>();

  loading$ = this.loadingSubject$.asObservable();

  private totalSubject$: Subject<number> = new ReplaySubject<number>();

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

  connect(collectionViewer: CollectionViewer): Observable<TokenSummary[] | ReadonlyArray<TokenSummary>> {
    return this.dataSubject$.asObservable();
  }

  disconnect(collectionViewer: CollectionViewer): void {
    this.dataSubject$.complete();
    this.loadingSubject$.complete();
  }

  getTokenStatus(tokenSummary: TokenSummary): TokenStatus {
    if (!!tokenSummary.revocationDate) {
      return TokenStatus.Revoked;
    } else if ((!!tokenSummary.expiryDate) && (new Date().getTime() > Date.parse(
      tokenSummary.expiryDate))) {
      return TokenStatus.Expired;
    } else if ((!!tokenSummary.validFromDate) && (Date.parse(
      tokenSummary.validFromDate) > new Date().getTime())) {
      return TokenStatus.Pending;
    } else {
      return TokenStatus.Active;
    }
  }

  /**
   * Load the token summaries.
   *
   * @param requiredStatus The required token status to apply to the token summaries.
   * @param filter         The optional filter to apply to the token summaries.
   * @param sortDirection  The optional sort direction to apply to the token summaries.
   * @param pageIndex      The optional page index.
   * @param pageSize       The optional page size.
   */
  load(requiredStatus: TokenStatus, filter?: string, sortDirection?: SortDirection,
       pageIndex?: number, pageSize?: number): void {
    this.loadingSubject$.next(true);

    this.securityService.getTokenSummaries(filter, TokenSortBy.Name, sortDirection, pageIndex,
      pageSize)
    .pipe(first())
    .subscribe((tokenSummaries: TokenSummaries) => {
      this.loadingSubject$.next(false);

      let filteredTokenSummaries: TokenSummary[] = tokenSummaries.tokenSummaries.filter(
        (tokenSummary: TokenSummary) => {
          let tokenStatus = this.getTokenStatus(tokenSummary);

          if (requiredStatus == TokenStatus.All) {
            return true;
          } else {
            return requiredStatus == tokenSummary.status;
          }
        });

      this.totalSubject$.next(filteredTokenSummaries.length);

      this.dataSubject$.next(filteredTokenSummaries);
    }, (error: Error) => {
      this.loadingSubject$.next(false);

      this.totalSubject$.next(0);

      this.loadingSubject$.error(error);
    });
  }
}
