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
import {Role} from '../services/role';
import {GroupRole} from '../services/group-role';
import {SecurityService} from '../services/security.service';
import {DialogService} from '../../dialog/services/dialog.service';
import {SpinnerService} from '../../layout/services/spinner.service';
import {BackNavigation} from '../../layout/components/back-navigation';
import {SecurityServiceError} from '../services/security.service.errors';
import {AccessDeniedError} from '../../core/errors/access-denied-error';
import {SystemUnavailableError} from '../../core/errors/system-unavailable-error';
import {ConfirmationDialogComponent} from '../../dialog/components/confirmation-dialog.component';

/**
 * The GroupRolesComponent class implements the group roles component.
 *
 * @author Marcus Portmann
 */
@Component({
  templateUrl: 'group-roles.component.html',
  styleUrls: ['group-roles.component.css']
})
export class GroupRolesComponent extends AdminContainerView implements AfterViewInit, OnDestroy {

  private subscriptions: Subscription = new Subscription();

  allRoles: Role[] = [];

  availableRoles$: Subject<Role[]> = new ReplaySubject<Role[]>();

  dataSource = new MatTableDataSource<GroupRole>([]);

  displayedColumns = ['existingRoleName', 'actions'];

  @ViewChild(MatPaginator, {static: true}) paginator!: MatPaginator;

  selectedRole?: Role;

  userDirectoryId: string;

  groupName: string;

  constructor(private router: Router, private activatedRoute: ActivatedRoute, private securityService: SecurityService,
              private dialogService: DialogService, private spinnerService: SpinnerService) {
    super();

    // Retrieve the route parameters
    const userDirectoryId = this.activatedRoute.snapshot.paramMap.get('userDirectoryId');

    if (!userDirectoryId) {
      throw(new Error('No userDirectoryId route parameter found'));
    }

    this.userDirectoryId = decodeURIComponent(userDirectoryId);

    const groupName = this.activatedRoute.snapshot.paramMap.get('groupName');

    if (!groupName) {
      throw(new Error('No groupName route parameter found'));
    }

    this.groupName = decodeURIComponent(groupName);
  }

  get backNavigation(): BackNavigation {
    return new BackNavigation('Back', ['../../..'], {
      relativeTo: this.activatedRoute,
      state: {userDirectoryId: this.userDirectoryId}
    });
  }

  get title(): string {
    return 'Group Roles';
  }

  private static calculateAvailableRoles(allRoles: Role[], existingGroupRoles: GroupRole[]): Role[] {

    const availableRoles: Role[] = [];

    for (const possibleRole of allRoles) {
      let foundExistingRole = false;

      for (const existingRole of existingGroupRoles) {
        if (possibleRole.code === existingRole.roleCode) {
          foundExistingRole = true;
          break;
        }
      }

      if (!foundExistingRole) {
        if (possibleRole.code !== 'Administrator') {
          availableRoles.push(possibleRole);
        }
      }
    }

    return availableRoles;
  }

  addRoleToGroup(): void {
    if (this.selectedRole) {
      this.spinnerService.showSpinner();

      this.securityService.addRoleToGroup(this.userDirectoryId, this.groupName, this.selectedRole.code)
        .pipe(first(), finalize(() => this.spinnerService.hideSpinner()))
        .subscribe(() => {
          this.loadRolesForGroup();
          this.selectedRole = undefined;
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

  loadRolesForGroup(): void {
    this.spinnerService.showSpinner();

    this.securityService.getRolesForGroup(this.userDirectoryId, this.groupName)
      .pipe(first(), finalize(() => this.spinnerService.hideSpinner()))
      .subscribe((groupRoles: GroupRole[]) => {
        this.dataSource.data = groupRoles;

        this.availableRoles$.next(GroupRolesComponent.calculateAvailableRoles(this.allRoles, this.dataSource.data));
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

    this.securityService.getRoles()
      .pipe(first(), finalize(() => this.spinnerService.hideSpinner()))
      .subscribe((roles: Role[]) => {
        this.allRoles = roles;

        this.loadRolesForGroup();
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

  removeRoleFromGroup(roleCode: string) {
    const dialogRef: MatDialogRef<ConfirmationDialogComponent, boolean> = this.dialogService.showConfirmationDialog({
      message: 'Are you sure you want to remove the role from the group?'
    });

    dialogRef.afterClosed()
      .pipe(first())
      .subscribe((confirmation: boolean | undefined) => {
        if (confirmation === true) {
          this.spinnerService.showSpinner();

          this.securityService.removeRoleFromGroup(this.userDirectoryId, this.groupName, roleCode)
            .pipe(first(), finalize(() => this.spinnerService.hideSpinner()))
            .subscribe(() => {
              this.loadRolesForGroup();
              this.selectedRole = undefined;
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

  roleCodeToName(roleCode: string): string {
    for (const role of this.allRoles) {
      if (role.code === roleCode) {
        return role.name;
      }
    }

    return 'Unknown';
  }
}
