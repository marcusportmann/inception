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

import { AfterViewInit, Component, inject, OnDestroy, ViewChild } from '@angular/core';
import { MatDialogRef } from '@angular/material/dialog';
import { MatPaginator } from '@angular/material/paginator';
import { MatTableDataSource } from '@angular/material/table';
import {
  AdminContainerView, BackNavigation, ConfirmationDialogComponent, CoreModule
} from 'ngx-inception/core';
import { ReplaySubject, Subject, Subscription } from 'rxjs';
import { finalize, first } from 'rxjs/operators';
import { GroupMemberType } from '../services/group-member-type';
import { SecurityService } from '../services/security.service';

/**
 * The UserGroupsComponent class implements the user groups component.
 *
 * @author Marcus Portmann
 */
@Component({
  selector: 'inception-security-user-groups',
  standalone: true,
  imports: [CoreModule],
  templateUrl: 'user-groups.component.html',
  styleUrls: ['user-groups.component.css']
})
export class UserGroupsComponent extends AdminContainerView implements AfterViewInit, OnDestroy {
  allGroupNames: string[] = [];

  availableGroupNames$: Subject<string[]> = new ReplaySubject<string[]>(1);

  dataSource = new MatTableDataSource<string>([]);

  readonly displayedColumns = ['existingGroupName', 'actions'] as const;

  @ViewChild(MatPaginator, { static: true }) paginator!: MatPaginator;

  selectedGroupName = '';

  readonly title = $localize`:@@security_user_groups_title:User Groups`;

  userDirectoryId: string;

  username: string;

  private readonly securityService = inject(SecurityService);

  private readonly subscriptions: Subscription = new Subscription();

  constructor() {
    super();

    // Retrieve the route parameters
    const userDirectoryId = this.activatedRoute.snapshot.paramMap.get('userDirectoryId');

    if (!userDirectoryId) {
      throw new globalThis.Error('No userDirectoryId route parameter found');
    }

    this.userDirectoryId = userDirectoryId;

    const username = this.activatedRoute.snapshot.paramMap.get('username');

    if (!username) {
      throw new globalThis.Error('No username route parameter found');
    }

    this.username = username;
  }

  override get backNavigation(): BackNavigation {
    return new BackNavigation(
      $localize`:@@security_user_groups_back_navigation:Back`,
      ['../../..'],
      {
        relativeTo: this.activatedRoute,
        state: { userDirectoryId: this.userDirectoryId }
      }
    );
  }

  addUserToGroup(): void {
    if (!this.selectedGroupName) {
      return;
    }

    this.spinnerService.showSpinner();

    this.securityService
      .addMemberToGroup(
        this.userDirectoryId,
        this.selectedGroupName,
        GroupMemberType.User,
        this.username
      )
      .pipe(
        first(),
        finalize(() => this.spinnerService.hideSpinner())
      )
      .subscribe({
        next: () => {
          this.loadGroupNamesForUser();
          this.selectedGroupName = '';
        },
        error: (error: Error) => this.handleError(error, false)
      });
  }

  loadGroupNamesForUser(): void {
    this.spinnerService.showSpinner();

    this.securityService
      .getGroupNamesForUser(this.userDirectoryId, this.username)
      .pipe(
        first(),
        finalize(() => this.spinnerService.hideSpinner())
      )
      .subscribe({
        next: (groupNamesForUser: string[]) => {
          this.dataSource.data = groupNamesForUser;

          this.availableGroupNames$.next(
            this.calculateAvailableGroupNames(this.allGroupNames, this.dataSource.data)
          );
        },
        error: (error: Error) => this.handleError(error, false)
      });
  }

  ngAfterViewInit(): void {
    this.dataSource.paginator = this.paginator;

    // Retrieve the existing user and initialize the form fields
    this.spinnerService.showSpinner();

    this.securityService
      .getGroupNames(this.userDirectoryId)
      .pipe(
        first(),
        finalize(() => this.spinnerService.hideSpinner())
      )
      .subscribe({
        next: (groupNames: string[]) => {
          this.allGroupNames = groupNames;
          this.loadGroupNamesForUser();
        },
        error: (error: Error) => this.handleError(error, false)
      });
  }

  ngOnDestroy(): void {
    this.subscriptions.unsubscribe();
  }

  removeUserFromGroup(groupName: string): void {
    const dialogRef: MatDialogRef<ConfirmationDialogComponent, boolean> =
      this.dialogService.showConfirmationDialog({
        message: $localize`:@@security_user_groups_confirm_remove_user_from_group:Are you sure you want to remove the user from the group?`
      });

    dialogRef
      .afterClosed()
      .pipe(first())
      .subscribe({
        next: (confirmation: boolean | undefined) => {
          if (confirmation !== true) {
            return;
          }

          this.spinnerService.showSpinner();

          this.securityService
            .removeMemberFromGroup(
              this.userDirectoryId,
              groupName,
              GroupMemberType.User,
              this.username
            )
            .pipe(
              first(),
              finalize(() => this.spinnerService.hideSpinner())
            )
            .subscribe({
              next: () => {
                this.loadGroupNamesForUser();
                this.selectedGroupName = '';
              },
              error: (error: Error) => this.handleError(error, false)
            });
        }
      });
  }

  private calculateAvailableGroupNames(
    allGroupNames: string[],
    existingGroupNames: string[]
  ): string[] {
    const availableGroupNames: string[] = [];

    for (const possibleGroupName of allGroupNames) {
      let foundExistingGroup = false;

      for (const existingGroupName of existingGroupNames) {
        if (possibleGroupName === existingGroupName) {
          foundExistingGroup = true;
          break;
        }
      }

      if (!foundExistingGroup) {
        availableGroupNames.push(possibleGroupName);
      }
    }

    return availableGroupNames;
  }
}
