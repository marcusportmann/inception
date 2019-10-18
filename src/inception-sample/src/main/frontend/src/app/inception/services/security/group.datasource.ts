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
import {SessionService} from '../session/session.service';
import {Group} from "./group";
import {Groups} from "./groups";

/**
 * The GroupDatasource class implements the group data source.
 *
 * @author Marcus Portmann
 */
export class GroupDatasource implements DataSource<Group> {

  private totalSubject: Subject<number> = new ReplaySubject<number>();

  private dataSubject: Subject<Group[]> = new ReplaySubject<Group[]>();

  private loadingSubject: Subject<boolean> = new ReplaySubject<boolean>();

  total = this.totalSubject.asObservable();

  loading = this.loadingSubject.asObservable();

  constructor(private sessionService: SessionService, private securityService: SecurityService) {
  }

  connect(collectionViewer: CollectionViewer): Observable<Group[] | ReadonlyArray<Group>> {
    return this.dataSubject.asObservable();
  }

  disconnect(collectionViewer: CollectionViewer): void {
    this.dataSubject.complete();
    this.loadingSubject.complete();
  }

  /**
   * Load the groups.
   *
   * @param userDirectoryId The ID used to uniquely identify the user directory the groups are
   *                        associated with.
   * @param filter          The optional filter to apply to the groups.
   * @param sortDirection   The optional sort direction to apply to the groups.
   * @param pageIndex       The optional page index.
   * @param pageSize        The optional page size.
   */
  load(userDirectoryId: number, filter?: string, sortDirection?: SortDirection,
       pageIndex?: number, pageSize?: number): void {
    this.loadingSubject.next(true);

    this.securityService.getGroups(userDirectoryId, filter, sortDirection, pageIndex,
      pageSize)
      .pipe(first())
      .subscribe((groups: Groups) => {
        this.loadingSubject.next(false);

        this.totalSubject.next(groups.total);

        this.dataSubject.next(groups.groups);
      }, (error: Error) => {
        this.loadingSubject.next(false);

        this.totalSubject.next(0);

        this.loadingSubject.error(error);
      });
  }
}
