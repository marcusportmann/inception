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
import {MatDialogRef} from "@angular/material/dialog";
import {ConfirmationDialogComponent} from "../../components/dialogs";
import {Role} from "../../services/security/role";
import {GroupRole} from "../../services/security/group-role";
import {MatPaginator} from "@angular/material/paginator";
import {MatSort} from "@angular/material/sort";

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

  @ViewChild(MatPaginator, {static: true}) paginator?: MatPaginator;

  selectedRole?: Role;

  userDirectoryId: string;

  groupName: string;

  constructor(private router: Router, private activatedRoute: ActivatedRoute,
              private formBuilder: FormBuilder, private i18n: I18n,
              private securityService: SecurityService, private dialogService: DialogService,
              private spinnerService: SpinnerService) {
    super();

    // Retrieve parameters
    this.userDirectoryId =
      decodeURIComponent(this.activatedRoute.snapshot.paramMap.get('userDirectoryId')!);
    this.groupName = decodeURIComponent(this.activatedRoute.snapshot.paramMap.get('groupName')!);
  }

  get backNavigation(): BackNavigation {
    return new BackNavigation(this.i18n({
      id: '@@group_roles_component_back_title',
      value: 'Back'
    }), ['../../..'], {
      relativeTo: this.activatedRoute,
      state: {userDirectoryId: this.userDirectoryId}
    });
  }

  get title(): string {
    return this.i18n({
      id: '@@group_roles_component_title',
      value: 'Group Roles'
    })
  }

  addRoleToGroup(): void {
    if (this.selectedRole) {
      this.spinnerService.showSpinner();

      this.securityService.addRoleToGroup(this.userDirectoryId, this.groupName,
        this.selectedRole.code)
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

        this.availableRoles$.next(this.calculateAvailableRoles(this.allRoles, this.dataSource.data));
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
    this.dataSource.paginator = this.paginator!;

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
    const dialogRef: MatDialogRef<ConfirmationDialogComponent, boolean> = this.dialogService.showConfirmationDialog(
      {
        message: this.i18n({
          id: '@@group_roles_component_confirm_remove_role_from_group',
          value: 'Are you sure you want to remove the role from the group?'
        })
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

  private calculateAvailableRoles(allRoles: Role[], existingGroupRoles: GroupRole[]): Role[] {

    let availableRoles: Role[] = [];

    for (let i = 0; i < allRoles.length; i++) {
      let foundExistingRole: boolean = false;

      for (let j = 0; j < existingGroupRoles.length; j++) {
        if (allRoles[i].code === existingGroupRoles[j].roleCode) {
          foundExistingRole = true;
          break;
        }
      }

      if (!foundExistingRole) {
        if (allRoles[i].code !== 'Administrator') {
          availableRoles.push(allRoles[i]);
        }
      }
    }

    return availableRoles;
  }

  private roleCodeToName(roleCode: string): string {
    for (let i = 0; i < this.allRoles.length; i++) {
      if (this.allRoles[i].code === roleCode) {
        return this.allRoles[i].name;
      }
    }

    return 'Unknown';
  }
}
