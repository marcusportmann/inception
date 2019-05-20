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
import {Organization} from './organization';
import {BehaviorSubject, Observable} from 'rxjs';
import {SecurityService} from './security.service';
import {SortDirection} from "./sort-direction";
import {FilteredOrganizations} from "./filtered-organizations";

/**
 * The OrganizationDatasource class implements the organization data source.
 *
 * @author Marcus Portmann
 */
export class OrganizationDatasource implements DataSource<Organization> {

  private totalSubject = new BehaviorSubject<number>(0);

  private dataSubject = new BehaviorSubject<Organization[]>([]);

  private loadingSubject = new BehaviorSubject<boolean>(false);

  total = this.totalSubject.asObservable();

  loading = this.loadingSubject.asObservable();

  constructor(private securityService: SecurityService) {
  }

  connect(collectionViewer: CollectionViewer): Observable<Organization[] | ReadonlyArray<Organization>> {
    return this.dataSubject.asObservable();
  }

  disconnect(collectionViewer: CollectionViewer): void {
    this.dataSubject.complete();
    this.loadingSubject.complete();
  }

  /**
   * Load the organizations.
   *
   * @param filter        the optional filter to apply to the organization name
   * @param sortDirection the optional sort direction to apply to the organization name
   * @param pageIndex     the optional page index
   * @param pageSize      the optional page size
   */
  load(filter?: string, sortDirection?: SortDirection, pageIndex?: number, pageSize?: number) {
    this.loadingSubject.next(true);

    this.securityService.getFilteredOrganizations(filter, sortDirection, pageIndex, pageSize)
      .subscribe((filteredOrganizations: FilteredOrganizations) => {
        this.loadingSubject.next(false);

        this.totalSubject.next(filteredOrganizations.total);

        this.dataSubject.next(filteredOrganizations.organizations)
      }, (error: Error) => {
        this.loadingSubject.next(false);

        this.totalSubject.next(0);

        this.loadingSubject.error(error);
      });
  }
}
