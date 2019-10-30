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

import {AfterViewInit, Component, OnDestroy} from '@angular/core';
import {FormBuilder} from '@angular/forms';
import {ActivatedRoute, Router} from '@angular/router';
import {DialogService} from '../../services/dialog/dialog.service';
import {SpinnerService} from '../../services/layout/spinner.service';
import {I18n} from '@ngx-translate/i18n-polyfill';
import {Error} from '../../errors/error';
import {finalize, first} from 'rxjs/operators';
import {SystemUnavailableError} from '../../errors/system-unavailable-error';
import {AccessDeniedError} from '../../errors/access-denied-error';
import {AdminContainerView} from '../../components/layout/admin-container-view';
import {BackNavigation} from '../../components/layout/back-navigation';
import {SecurityServiceError} from '../../services/security/security.service.errors';
import {SecurityService} from '../../services/security/security.service';
import {ReplaySubject, Subject, Subscription} from 'rxjs';
import {MatTableDataSource} from "@angular/material/table";
import {GroupMemberType} from "../../services/security/group-member-type";
import {MatDialogRef} from "@angular/material/dialog";
import {ConfirmationDialogComponent} from "../../components/dialogs";

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

  private subscriptions: Subscription = new Subscription();

  allGroupNames: string[] = [];

  availableGroupNames$: Subject<string[]> = new ReplaySubject<string[]>();

  dataSource = new MatTableDataSource<string>([]);

  displayedColumns = ['existingGroupName', 'actions'];

  selectedGroupName: string = '';

  userDirectoryId: string;

  username: string;

  constructor(private router: Router, private activatedRoute: ActivatedRoute,
              private formBuilder: FormBuilder, private i18n: I18n,
              private securityService: SecurityService, private dialogService: DialogService,
              private spinnerService: SpinnerService) {
    super();

    // Retrieve parameters
    this.userDirectoryId =
      decodeURIComponent(this.activatedRoute.snapshot.paramMap.get('userDirectoryId')!);
    this.username = decodeURIComponent(this.activatedRoute.snapshot.paramMap.get('username')!);
  }

  get backNavigation(): BackNavigation {
    return new BackNavigation(this.i18n({
      id: '@@user_groups_component_back_title',
      value: 'Back'
    }), ['../../..'], {
      relativeTo: this.activatedRoute,
      state: {userDirectoryId: this.userDirectoryId}
    });
  }

  get title(): string {
    return this.i18n({
      id: '@@user_groups_component_title',
      value: 'User Groups'
    })
  }

  addUserToGroup(): void {
    if (!!this.selectedGroupName) {
      this.spinnerService.showSpinner();

      this.securityService.addMemberToGroup(this.userDirectoryId, this.selectedGroupName,
        GroupMemberType.User, this.username)
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

        this.availableGroupNames$.next(
          this.calculateAvailableGroupNames(this.allGroupNames, this.dataSource.data));
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
    const dialogRef: MatDialogRef<ConfirmationDialogComponent, boolean> = this.dialogService.showConfirmationDialog(
      {
        message: this.i18n({
          id: '@@user_groups_component_confirm_remove_user_from_group',
          value: 'Are you sure you want to remove the user from the group?'
        })
      });

    dialogRef.afterClosed()
      .pipe(first())
      .subscribe((confirmation: boolean | undefined) => {
        if (confirmation === true) {
          this.spinnerService.showSpinner();

          this.securityService.removeMemberFromGroup(this.userDirectoryId, groupName,
            GroupMemberType.User, this.username)
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

  private calculateAvailableGroupNames(allGroupNames: string[],
                                       existingGroupNames: string[]): string[] {

    let availableGroupNames: string[] = [];

    for (let i = 0; i < allGroupNames.length; i++) {
      let foundExistingGroup: boolean = false;

      for (let j = 0; j < existingGroupNames.length; j++) {
        if (allGroupNames[i] == existingGroupNames[j]) {
          foundExistingGroup = true;
          break;
        }
      }

      if (!foundExistingGroup) {
        availableGroupNames.push(allGroupNames[i]);
      }
    }

    return availableGroupNames;
  }
}
