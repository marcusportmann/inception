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
import {GroupMember} from "./group-member";
import {GroupMembers} from "./group-members";

/**
 * The GroupMemberDatasource class implements the group members data source.
 *
 * @author Marcus Portmann
 */
export class GroupMemberDatasource implements DataSource<GroupMember> {

  private totalSubject: Subject<number> = new ReplaySubject<number>();

  private dataSubject: Subject<GroupMember[]> = new ReplaySubject<GroupMember[]>();

  private loadingSubject: Subject<boolean> = new ReplaySubject<boolean>();

  total = this.totalSubject.asObservable();

  loading = this.loadingSubject.asObservable();

  constructor(private securityService: SecurityService) {
  }

  /**
   * Clear the data source.
   */
  clear(): void {
    this.totalSubject.next(0);
    this.dataSubject.next([]);
  }

  connect(collectionViewer: CollectionViewer): Observable<GroupMember[] | ReadonlyArray<GroupMember>> {
    return this.dataSubject.asObservable();
  }

  disconnect(collectionViewer: CollectionViewer): void {
    this.dataSubject.complete();
    this.loadingSubject.complete();
  }

  /**
   * Load the group members.
   *
   * @param userDirectoryId The ID used to uniquely identify the user directory the group is
   *                        associated with.
   * @param groupName       The name identifying the group.
   * @param filter          The optional filter to apply to the group members.
   * @param sortDirection   The optional sort direction to apply to the group members.
   * @param pageIndex       The optional page index.
   * @param pageSize        The optional page size.
   */
  load(userDirectoryId: string, groupName: string, filter?: string, sortDirection?: SortDirection,
       pageIndex?: number, pageSize?: number): void {
    this.loadingSubject.next(true);

    this.securityService.getGroupMembers(userDirectoryId, groupName, filter, sortDirection,
      pageIndex, pageSize)
      .pipe(first())
      .subscribe((groupMembers: GroupMembers) => {
        this.loadingSubject.next(false);

        this.totalSubject.next(groupMembers.total);

        this.dataSubject.next(groupMembers.groupMembers);
      }, (error: Error) => {
        this.loadingSubject.next(false);

        this.totalSubject.next(0);

        this.loadingSubject.error(error);
      });
  }
}
