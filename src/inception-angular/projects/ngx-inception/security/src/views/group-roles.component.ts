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
  AdminContainerView, BackNavigation, ConfirmationDialogComponent, CoreModule } from 'ngx-inception/core';
import { ReplaySubject, Subject, Subscription } from 'rxjs';
import { finalize, first } from 'rxjs/operators';
import { GroupRole } from '../services/group-role';
import { Role } from '../services/role';
import { SecurityService } from '../services/security.service';

/**
 * The GroupRolesComponent class implements the group roles component.
 *
 * @author Marcus Portmann
 */
@Component({
  selector: 'inception-security-group-roles',
  standalone: true,
  imports: [CoreModule],
  templateUrl: 'group-roles.component.html',
  styleUrls: ['group-roles.component.css']
})
export class GroupRolesComponent extends AdminContainerView implements AfterViewInit, OnDestroy {
  allRoles: Role[] = [];

  availableRoles$: Subject<Role[]> = new ReplaySubject<Role[]>(1);

  dataSource = new MatTableDataSource<GroupRole>([]);

  displayedColumns = ['existingRoleName', 'actions'];

  groupName: string;

  @ViewChild(MatPaginator, { static: true }) paginator!: MatPaginator;

  selectedRole: Role | null = null;

  readonly title = $localize`:@@security_group_roles_title:Group Roles`;

  userDirectoryId: string;

  private securityService = inject(SecurityService);

  private subscriptions: Subscription = new Subscription();

  constructor() {
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
  }

  override get backNavigation(): BackNavigation {
    return new BackNavigation(
      $localize`:@@security_group_roles_back_navigation:Back`,
      ['../../..'],
      {
        relativeTo: this.activatedRoute,
        state: { userDirectoryId: this.userDirectoryId }
      }
    );
  }

  addRoleToGroup(): void {
    if (!this.selectedRole) {
      return;
    }

    this.spinnerService.showSpinner();

    this.securityService
      .addRoleToGroup(this.userDirectoryId, this.groupName, this.selectedRole.code)
      .pipe(
        first(),
        finalize(() => this.spinnerService.hideSpinner())
      )
      .subscribe({
        next: () => {
          this.loadRolesForGroup();
          this.selectedRole = null;
        },
        error: (error: Error) => this.handleError(error, false)
      });
  }

  loadRolesForGroup(): void {
    this.spinnerService.showSpinner();

    this.securityService
      .getRolesForGroup(this.userDirectoryId, this.groupName)
      .pipe(
        first(),
        finalize(() => this.spinnerService.hideSpinner())
      )
      .subscribe({
        next: (groupRoles: GroupRole[]) => {
          this.dataSource.data = groupRoles;

          this.availableRoles$.next(
            GroupRolesComponent.calculateAvailableRoles(this.allRoles, this.dataSource.data)
          );
        },
        error: (error: Error) => this.handleError(error, false)
      });
  }

  ngAfterViewInit(): void {
    this.dataSource.paginator = this.paginator;

    // Retrieve the roles and initialize the table
    this.spinnerService.showSpinner();

    this.securityService
      .getRoles()
      .pipe(
        first(),
        finalize(() => this.spinnerService.hideSpinner())
      )
      .subscribe({
        next: (roles: Role[]) => {
          this.allRoles = roles;
          this.loadRolesForGroup();
        },
        error: (error: Error) => this.handleError(error, false)
      });
  }

  ngOnDestroy(): void {
    this.subscriptions.unsubscribe();
  }

  removeRoleFromGroup(roleCode: string): void {
    const dialogRef: MatDialogRef<ConfirmationDialogComponent, boolean> =
      this.dialogService.showConfirmationDialog({
        message: $localize`:@@security_group_roles_confirm_remove_role_from_group:Are you sure you want to remove the role from the group?`
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
            .removeRoleFromGroup(this.userDirectoryId, this.groupName, roleCode)
            .pipe(
              first(),
              finalize(() => this.spinnerService.hideSpinner())
            )
            .subscribe({
              next: () => {
                this.loadRolesForGroup();
                this.selectedRole = null;
              },
              error: (error: Error) => this.handleError(error, false)
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

  private static calculateAvailableRoles(
    allRoles: Role[],
    existingGroupRoles: GroupRole[]
  ): Role[] {
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
}
