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
import { Observable, ReplaySubject, Subject } from 'rxjs';
import { finalize, first } from 'rxjs/operators';
import { SecurityService } from './security.service';
import { Tenant } from './tenant';
import { Tenants } from './tenants';

/**
 * The TenantDataSource class implements the tenant data source.
 *
 * @author Marcus Portmann
 */
export class TenantDataSource implements DataSource<Tenant> {
  private dataSubject$: Subject<Tenant[]> = new ReplaySubject<Tenant[]>(1);

  private loadingSubject$: Subject<boolean> = new ReplaySubject<boolean>(1);

  loading$ = this.loadingSubject$.asObservable();

  private totalSubject$: Subject<number> = new ReplaySubject<number>(1);

  total$ = this.totalSubject$.asObservable();

  constructor(private securityService: SecurityService) {}

  /**
   * Clear the data source.
   */
  clear(): void {
    this.totalSubject$.next(0);
    this.dataSubject$.next([]);
  }

  connect(collectionViewer: CollectionViewer): Observable<Tenant[] | readonly Tenant[]> {
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
   * Load the tenants.
   *
   * @param filter        The filter to apply to the tenants.
   * @param sortDirection The sort direction to apply to the tenants.
   * @param pageIndex     The page index.
   * @param pageSize      The page size.
   */
  load(
    filter?: string,
    sortDirection?: SortDirection,
    pageIndex?: number,
    pageSize?: number
  ): void {
    this.loadingSubject$.next(true);

    this.securityService
      .getTenants(filter, sortDirection, pageIndex, pageSize)
      .pipe(
        first(),
        finalize(() => this.loadingSubject$.next(false))
      )
      .subscribe({
        next: (tenants: Tenants) => {
          this.totalSubject$.next(tenants.total);
          this.dataSubject$.next(tenants.tenants);
        },
        error: (error: Error) => {
          this.totalSubject$.next(0);
          this.loadingSubject$.error(error);
        }
      });
  }
}
