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

import {
  AfterViewInit, ChangeDetectorRef, Component, HostBinding, inject, ViewChild
} from '@angular/core';
import { MatPaginator } from '@angular/material/paginator';
import { MatSort } from '@angular/material/sort';
import {
  BackNavigation, CoreModule, Error, SortDirection, StatefulListView, TableFilterComponent
} from 'ngx-inception/core';
import { Observable } from 'rxjs';
import { finalize, takeUntil } from 'rxjs/operators';

import { GroupMember } from '../services/group-member';
import { GroupMemberDataSource } from '../services/group-member-data-source';
import { GroupMemberType } from '../services/group-member-type';
import { GroupMembers } from '../services/group-members';
import { SecurityService } from '../services/security.service';

/**
 * The GroupMembersComponent class implements the Group Members component.
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
export class GroupMembersComponent extends StatefulListView implements AfterViewInit {
  readonly dataSource: GroupMemberDataSource;

  readonly displayedColumns: readonly string[] = ['memberName', 'memberType', 'actions'];

  readonly groupName: string;

  @HostBinding('class') readonly hostClass = 'flex flex-column flex-fill';

  readonly listKey = 'security.group-members';

  @ViewChild(MatPaginator, { static: true }) override paginator!: MatPaginator;

  @ViewChild(MatSort, { static: true }) override sort!: MatSort;

  @ViewChild(TableFilterComponent, { static: true })
  override tableFilter!: TableFilterComponent;

  readonly title = $localize`:@@security_group_members_title:Group Members`;

  readonly userDirectoryId: string;

  private readonly changeDetectorRef = inject(ChangeDetectorRef);

  /** Whether this navigation requested a state reset (from the sidebar). */
  private readonly resetStateRequested: boolean;

  constructor(private securityService: SecurityService) {
    super();

    // Read the reset flag from the current navigation
    const nav = this.router.getCurrentNavigation();
    this.resetStateRequested = !!nav?.extras.state?.['resetState'];

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
    // When implemented, remember to call this.saveState() before navigation.
    // // noinspection JSIgnoredPromiseFromCall
    // this.saveState();
    // this.router.navigate(['new'], { relativeTo: this.activatedRoute });
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
    this.initializeStatefulList(this.resetStateRequested, () => this.loadData());

    // Stabilize view after paginator/sort mutations
    this.changeDetectorRef.detectChanges();
  }

  removeMemberFromGroup(groupMember: GroupMember): void {
    this.confirmAndProcessAction(
      $localize`:@@security_group_members_confirm_remove_member_from_group:Are you sure you want to remove the group member from the group?`,
      () =>
        this.securityService.removeMemberFromGroup(
          this.userDirectoryId,
          this.groupName,
          groupMember.memberType,
          groupMember.memberName
        ),
      () => this.loadGroupMembers()
    );
  }

  private loadData(): void {
    this.spinnerService.showSpinner();

    this.loadGroupMembers()
      .pipe(
        finalize(() => this.spinnerService.hideSpinner()),
        takeUntil(this.destroy$)
      )
      .subscribe({
        next: () => {
          // Load complete
        },
        error: (error: Error) => this.handleError(error, false)
      });
  }

  private loadGroupMembers(): Observable<GroupMembers> {
    const filterValue = this.tableFilter.filter?.trim().toLowerCase() || '';

    let sortDirection = SortDirection.Descending;

    if (this.sort.active) {
      sortDirection =
        this.sort.direction === 'asc' ? SortDirection.Ascending : SortDirection.Descending;
    }

    return this.dataSource.load(
      this.userDirectoryId,
      this.groupName,
      filterValue,
      sortDirection,
      this.paginator.pageIndex,
      this.paginator.pageSize
    );
  }
}
