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

import { AfterViewInit, Component, HostBinding, OnDestroy, ViewChild } from '@angular/core';
import { MatPaginator } from '@angular/material/paginator';
import { MatSort } from '@angular/material/sort';
import {
  AdminContainerView, BackNavigation, CoreModule, Error, SortDirection, TableFilterComponent
} from 'ngx-inception/core';
import { EMPTY, merge, Observable, Subject, tap } from 'rxjs';
import {
  catchError, debounceTime, filter, finalize, first, switchMap, takeUntil
} from 'rxjs/operators';
import { GroupMember } from '../services/group-member';
import { GroupMemberDataSource } from '../services/group-member-data-source';
import { GroupMemberType } from '../services/group-member-type';
import { GroupMembers } from '../services/group-members';
import { SecurityService } from '../services/security.service';

/**
 * The GroupMembersComponent class implements the group members component.
 *
 * @author Marcus Portmann
 */
@Component({
  selector: 'inception-security-group-members',
  standalone: true,
  imports: [CoreModule, TableFilterComponent],
  templateUrl: 'group-members.component.html',
  styleUrls: ['group-members.component.css']
})
export class GroupMembersComponent extends AdminContainerView implements AfterViewInit, OnDestroy {
  dataSource: GroupMemberDataSource;

  displayedColumns = ['memberName', 'memberType', 'actions'];

  groupName: string;

  @HostBinding('class') hostClass = 'flex flex-column flex-fill';

  @ViewChild(MatPaginator, { static: true }) paginator!: MatPaginator;

  @ViewChild(MatSort, { static: true }) sort!: MatSort;

  @ViewChild(TableFilterComponent, { static: true })
  tableFilter!: TableFilterComponent;

  readonly title = $localize`:@@security_group_members_title:Group Members`;

  userDirectoryId: string;

  private destroy$ = new Subject<void>();

  constructor(private securityService: SecurityService) {
    super();

    // Retrieve the route parameters
    const userDirectoryId = this.activatedRoute.snapshot.paramMap.get('userDirectoryId');

    if (!userDirectoryId) {
      throw new Error('No userDirectoryId route parameter found');
    }

    this.userDirectoryId = decodeURIComponent(userDirectoryId);

    const groupName = this.activatedRoute.snapshot.paramMap.get('groupName');

    if (!groupName) {
      throw new Error('No groupName route parameter found');
    }

    this.groupName = decodeURIComponent(groupName);

    this.dataSource = new GroupMemberDataSource(this.securityService);
  }

  override get backNavigation(): BackNavigation {
    return new BackNavigation(
      $localize`:@@security_group_members_back_navigation:Back`,
      ['../../..'],
      {
        relativeTo: this.activatedRoute,
        state: { userDirectoryId: this.userDirectoryId }
      }
    );
  }

  addMemberToGroup(): void {
    // // noinspection JSIgnoredPromiseFromCall
    // this.router.navigate(['new'], {relativeTo: this.activatedRoute});
  }

  groupMemberTypeName(groupMemberType: GroupMemberType): string {
    if (groupMemberType === GroupMemberType.User) {
      return 'User';
    } else if (groupMemberType === GroupMemberType.Group) {
      return 'Group';
    } else {
      return 'Unknown';
    }
  }

  ngAfterViewInit(): void {
    this.initializeDataLoaders();
    this.loadData();
  }

  ngOnDestroy(): void {
    this.destroy$.next();
    this.destroy$.complete();
  }

  removeMemberFromGroup(groupMember: GroupMember): void {
    const dialogRef = this.dialogService.showConfirmationDialog({
      message: $localize`:@@security_group_members_confirm_remove_member_from_group:Are you sure you want to remove the group member from the group?`
    });

    dialogRef
      .afterClosed()
      .pipe(
        first(),
        filter((confirmed) => confirmed === true),
        tap(() => this.spinnerService.showSpinner()),
        switchMap(() =>
          this.securityService
            .removeMemberFromGroup(
              this.userDirectoryId,
              this.groupName,
              groupMember.memberType,
              groupMember.memberName
            )
            .pipe(
              tap(() => this.resetTable()),
              // After delete completes, reload members
              switchMap(() =>
                this.loadGroupMembers().pipe(
                  catchError((error: Error) => {
                    this.handleError(error, false);
                    return EMPTY;
                  })
                )
              ),
              catchError((error: Error) => {
                this.handleError(error, false);
                return EMPTY;
              }),
              finalize(() => this.spinnerService.hideSpinner())
            )
        ),
        takeUntil(this.destroy$)
      )
      .subscribe();
  }

  private initializeDataLoaders(): void {
    this.sort.sortChange
      .pipe(takeUntil(this.destroy$))
      .subscribe(() => (this.paginator.pageIndex = 0));

    merge(this.sort.sortChange, this.tableFilter.changed, this.paginator.page)
      .pipe(debounceTime(200), takeUntil(this.destroy$))
      .subscribe(() => this.loadData());
  }

  private loadData(): void {
    this.spinnerService.showSpinner();
    this.loadGroupMembers()
      .pipe(finalize(() => this.spinnerService.hideSpinner()))
      .subscribe({
        next: () => {
          // Load complete
        },
        error: (error) => this.handleError(error, false)
      });
  }

  private loadGroupMembers(): Observable<GroupMembers> {
    const filter = this.tableFilter.filter?.trim().toLowerCase() || '';

    let sortDirection = SortDirection.Descending;

    if (this.sort.active) {
      sortDirection =
        this.sort.direction === 'asc' ? SortDirection.Ascending : SortDirection.Descending;
    }

    return this.dataSource.load(
      this.userDirectoryId,
      this.groupName,
      filter,
      sortDirection,
      this.paginator.pageIndex,
      this.paginator.pageSize
    );
  }

  private resetTable(): void {
    this.tableFilter.reset(false);
    this.paginator.pageIndex = 0;
    this.sort.active = '';
    this.sort.direction = 'asc' as SortDirection;
  }
}
