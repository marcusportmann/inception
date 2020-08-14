/*
 * Copyright 2020 Marcus Portmann
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
import {ActivatedRoute, Router} from '@angular/router';
import {finalize, first} from 'rxjs/operators';
import {ReplaySubject, Subject, Subscription} from 'rxjs';
import {MatTableDataSource} from '@angular/material/table';
import {MatDialogRef} from '@angular/material/dialog';
import {MatPaginator} from '@angular/material/paginator';
import {Error} from '../../core/errors/error';
import {AdminContainerView} from '../../layout/components/admin-container-view';
import {SecurityService} from '../services/security.service';
import {SpinnerService} from '../../layout/services/spinner.service';
import {DialogService} from '../../dialog/services/dialog.service';
import {BackNavigation} from '../../layout/components/back-navigation';
import {GroupMemberType} from '../services/group-member-type';
import {SecurityServiceError} from '../services/security.service.errors';
import {AccessDeniedError} from '../../core/errors/access-denied-error';
import {SystemUnavailableError} from '../../core/errors/system-unavailable-error';
import {ConfirmationDialogComponent} from '../../dialog/components/confirmation-dialog.component';

/**
 * The UserGroupsComponent class implements the user groups component.
 *
 * @author Marcus Portmann
 */
@Component({
  templateUrl: 'user-groups.component.html',
  styleUrls: ['user-groups.component.css']
})
export class UserGroupsComponent extends AdminContainerView implements AfterViewInit, OnDestroy {

  allGroupNames: string[] = [];
  availableGroupNames$: Subject<string[]> = new ReplaySubject<string[]>();
  dataSource = new MatTableDataSource<string>([]);
  displayedColumns = ['existingGroupName', 'actions'];
  @ViewChild(MatPaginator, {static: true}) paginator!: MatPaginator;
  selectedGroupName = '';
  userDirectoryId: string;
  username: string;
  private subscriptions: Subscription = new Subscription();

  constructor(private router: Router, private activatedRoute: ActivatedRoute, private securityService: SecurityService,
              private dialogService: DialogService, private spinnerService: SpinnerService) {
    super();

    // Retrieve the route parameters
    const userDirectoryId = this.activatedRoute.snapshot.paramMap.get('userDirectoryId');

    if (!userDirectoryId) {
      throw(new Error('No userDirectoryId route parameter found'));
    }

    this.userDirectoryId = decodeURIComponent(userDirectoryId);

    const username = this.activatedRoute.snapshot.paramMap.get('username');

    if (!username) {
      throw(new Error('No username route parameter found'));
    }

    this.username = decodeURIComponent(username);
  }

  get backNavigation(): BackNavigation {
    return new BackNavigation('Back', ['../../..'], {
      relativeTo: this.activatedRoute,
      state: {userDirectoryId: this.userDirectoryId}
    });
  }

  get title(): string {
    return 'User Groups';
  }

  addUserToGroup(): void {
    if (!!this.selectedGroupName) {
      this.spinnerService.showSpinner();

      this.securityService.addMemberToGroup(this.userDirectoryId, this.selectedGroupName, GroupMemberType.User,
        this.username)
      .pipe(first(), finalize(() => this.spinnerService.hideSpinner()))
      .subscribe(() => {
        this.loadGroupNamesForUser();
        this.selectedGroupName = '';
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
  }

  loadGroupNamesForUser(): void {
    this.spinnerService.showSpinner();

    this.securityService.getGroupNamesForUser(this.userDirectoryId, this.username)
    .pipe(first(), finalize(() => this.spinnerService.hideSpinner()))
    .subscribe((groupNamesForUser: string[]) => {
      this.dataSource.data = groupNamesForUser;

      this.availableGroupNames$.next(this.calculateAvailableGroupNames(this.allGroupNames, this.dataSource.data));
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

  ngAfterViewInit(): void {
    this.dataSource.paginator = this.paginator;

    // Retrieve the existing user and initialise the form fields
    this.spinnerService.showSpinner();

    this.securityService.getGroupNames(this.userDirectoryId)
    .pipe(first(), finalize(() => this.spinnerService.hideSpinner()))
    .subscribe((groupNames: string[]) => {
      this.allGroupNames = groupNames;

      this.loadGroupNamesForUser();
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

  ngOnDestroy(): void {
    this.subscriptions.unsubscribe();
  }

  removeUserFromGroup(groupName: string) {
    const dialogRef: MatDialogRef<ConfirmationDialogComponent, boolean> = this.dialogService.showConfirmationDialog({
      message: 'Are you sure you want to remove the user from the group?'
    });

    dialogRef.afterClosed()
    .pipe(first())
    .subscribe((confirmation: boolean | undefined) => {
      if (confirmation === true) {
        this.spinnerService.showSpinner();

        this.securityService.removeMemberFromGroup(this.userDirectoryId, groupName, GroupMemberType.User,
          this.username)
        .pipe(first(), finalize(() => this.spinnerService.hideSpinner()))
        .subscribe(() => {
          this.loadGroupNamesForUser();
          this.selectedGroupName = '';
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

  private calculateAvailableGroupNames(allGroupNames: string[], existingGroupNames: string[]): string[] {

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
