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
import { GroupMember } from './group-member';
import { GroupMembers } from './group-members';
import { SecurityService } from './security.service';

/**
 * The GroupMemberDataSource class implements the group members data source.
 *
 * @author Marcus Portmann
 */
export class GroupMemberDataSource implements DataSource<GroupMember> {
  private dataSubject$ = new BehaviorSubject<GroupMember[]>([]);

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

  connect(collectionViewer: CollectionViewer): Observable<GroupMember[]> {
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
   * Load the group members.
   *
   * @param userDirectoryId The ID for the user directory the group is associated with.
   * @param groupName       The name of the group.
   * @param filter          The filter to apply to the group members.
   * @param sortDirection   The sort direction to apply to the group members.
   * @param pageIndex       The page index.
   * @param pageSize        The page size.
   *
   * @return The group members.
   */
  load(
    userDirectoryId: string,
    groupName: string,
    filter?: string,
    sortDirection?: SortDirection,
    pageIndex?: number,
    pageSize?: number
  ): Observable<GroupMembers> {
    this.loadingSubject$.next(true);

    return this.securityService
      .getMembersForGroup(
        userDirectoryId,
        groupName,
        filter,
        sortDirection,
        pageIndex,
        pageSize
      )
      .pipe(
        tap((groupMembers: GroupMembers) => {
          this.updateData(groupMembers);
        }),
        catchError((error: Error) => this.handleError(error)),
        finalize(() => this.loadingSubject$.next(false))
      );
  }

  /**
   * Handle errors during the load operation.
   *
   * @param error The error encountered.
   *
   * @return An observable that emits the error.
   */
  private handleError(error: Error): Observable<never> {
    console.error('Failed to load the group members:', error);
    this.totalSubject$.next(0);
    this.dataSubject$.next([]);
    return throwError(() => error);
  }

  /**
   * Update the data source with the fetched group members.
   *
   * @param groupMembers The group members to update.
   */
  private updateData(groupMembers: GroupMembers): void {
    this.totalSubject$.next(groupMembers.total);
    this.dataSubject$.next(groupMembers.groupMembers);
  }
}
