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

import {AfterViewInit, Component, OnDestroy, ViewChild} from '@angular/core';
import {MatDialogRef} from '@angular/material/dialog';
import {MatPaginator} from '@angular/material/paginator';
import {MatSort} from '@angular/material/sort';
import {finalize, first, tap} from 'rxjs/operators';
import {DialogService} from '../../services/dialog/dialog.service';
import {SpinnerService} from '../../services/layout/spinner.service';
import {I18n} from '@ngx-translate/i18n-polyfill';
import {Error} from '../../errors/error';
import {ActivatedRoute, Router} from '@angular/router';
import {ConfirmationDialogComponent} from '../../components/dialogs';
import {SystemUnavailableError} from '../../errors/system-unavailable-error';
import {AccessDeniedError} from '../../errors/access-denied-error';
import {SecurityService} from '../../services/security/security.service';
import {SecurityServiceError} from '../../services/security/security.service.errors';
import {SortDirection} from '../../services/security/sort-direction';
import {merge, Subscription} from 'rxjs';
import {TableFilter} from '../../components/controls';
import {AdminContainerView} from '../../components/layout/admin-container-view';
import {GroupMemberDatasource} from "../../services/security/group-member.datasource";
import {GroupMember} from "../../services/security/group-member";
import {GroupMemberType} from "../../services/security/group-member-type";
import {BackNavigation} from "../../components/layout/back-navigation";

/**
 * The GroupMembersComponent class implements the group members component.
 *
 * @author Marcus Portmann
 */
@Component({
  templateUrl: 'group-members.component.html',
  styleUrls: ['group-members.component.css'],
  host: {
    'class': 'flex flex-column flex-fill',
  }
})
export class GroupMembersComponent extends AdminContainerView implements AfterViewInit, OnDestroy {

  private subscriptions: Subscription = new Subscription();

  dataSource: GroupMemberDatasource;

  displayedColumns = ['memberName', 'memberType', 'actions'];

  @ViewChild(MatPaginator, {static: true}) paginator?: MatPaginator;

  @ViewChild(MatSort, {static: true}) sort?: MatSort;

  @ViewChild(TableFilter, {static: true}) tableFilter?: TableFilter;

  userDirectoryId: string;

  groupName: string;

  constructor(private router: Router, private activatedRoute: ActivatedRoute, private i18n: I18n,
              private securityService: SecurityService, private dialogService: DialogService,
              private spinnerService: SpinnerService) {
    super();

    // Retrieve parameters
    this.userDirectoryId =
      decodeURIComponent(this.activatedRoute.snapshot.paramMap.get('userDirectoryId')!);
    this.groupName = decodeURIComponent(this.activatedRoute.snapshot.paramMap.get('groupName')!);

    this.dataSource = new GroupMemberDatasource(this.securityService);
  }

  get backNavigation(): BackNavigation {
    return new BackNavigation(this.i18n({
      id: '@@group_members_component_back_title',
      value: 'Back'
    }), ['../../..'], {
      relativeTo: this.activatedRoute,
      state: {userDirectoryId: this.userDirectoryId}
    });
  }

  get title(): string {
    return this.i18n({
      id: '@@group_members_component_title',
      value: 'Group Members'
    })
  }

  addGroupMember(): void {
    // // noinspection JSIgnoredPromiseFromCall
    // this.router.navigate(['new'], {relativeTo: this.activatedRoute});
  }

  groupMemberTypeName(groupMemberType: GroupMemberType): string {
    if (groupMemberType == GroupMemberType.User) {
      return 'User';
    } else if (groupMemberType == GroupMemberType.Group) {
      return 'Group';
    } else {
      return 'Unknown';
    }
  }

  loadGroupMembers(): void {
    let filter = '';

    if (!!this.tableFilter!.filter) {
      filter = this.tableFilter!.filter;
      filter = filter.trim();
      filter = filter.toLowerCase();
    }

    const sortDirection = this.sort!.direction === 'asc' ? SortDirection.Ascending :
      SortDirection.Descending;

    this.dataSource.load(this.userDirectoryId, this.groupName, filter, sortDirection,
      this.paginator!.pageIndex, this.paginator!.pageSize);
  }

  ngAfterViewInit(): void {
    this.subscriptions.add(this.dataSource.loading.subscribe((next: boolean) => {
      if (next) {
        this.spinnerService.showSpinner()
      } else {
        this.spinnerService.hideSpinner();
      }
    }, (error: Error) => {
      // noinspection SuspiciousTypeOfGuard
      if ((error instanceof SecurityServiceError) || (error instanceof AccessDeniedError) ||
        (error instanceof SystemUnavailableError)) {
        // noinspection JSIgnoredPromiseFromCall
        this.router.navigateByUrl('/error/send-error-report', {state: {error}});
      } else {
        this.dialogService.showErrorDialog(error);
      }
    }));

    this.subscriptions.add(this.sort!.sortChange.subscribe(() => {
      if (this.paginator) {
        this.paginator.pageIndex = 0
      }
    }));

    this.subscriptions.add(this.tableFilter!.changed.subscribe(() => {
      if (this.paginator) {
        this.paginator.pageIndex = 0
      }
    }));

    this.subscriptions.add(
      merge(this.sort!.sortChange, this.tableFilter!.changed, this.paginator!.page)
        .pipe(tap(() => {
          this.loadGroupMembers();
        })).subscribe());

    this.loadGroupMembers();
  }

  ngOnDestroy(): void {
    this.subscriptions.unsubscribe();
  }

  removeGroupMember(groupMember: GroupMember): void {
    const dialogRef: MatDialogRef<ConfirmationDialogComponent, boolean> = this.dialogService.showConfirmationDialog(
      {
        message: this.i18n({
          id: '@@group_members_component_confirm_remove_group_member',
          value: 'Are you sure you want to remove the group member?'
        })
      });

    dialogRef.afterClosed()
      .pipe(first())
      .subscribe((confirmation: boolean | undefined) => {
        if (confirmation === true) {
          this.spinnerService.showSpinner();

          this.securityService.removeGroupMember(this.userDirectoryId, this.groupName,
            groupMember.memberType, groupMember.memberName)
            .pipe(first(), finalize(() => this.spinnerService.hideSpinner()))
            .subscribe(() => {
              this.loadGroupMembers();
            }, (error: Error) => {
              // noinspection SuspiciousTypeOfGuard
              if ((error instanceof SecurityServiceError) || (error instanceof AccessDeniedError) ||
                (error instanceof SystemUnavailableError)) {
                // noinspection JSIgnoredPromiseFromCall
                this.router.navigateByUrl('/error/send-error-report', {state: {error}});
              } else {
                this.dialogService.showErrorDialog(error);
              }
            });

        }
      });
  }
}

