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
import { FormControl } from '@angular/forms';
import { MatDialogRef } from '@angular/material/dialog';
import { MatPaginator } from '@angular/material/paginator';
import { MatTableDataSource } from '@angular/material/table';
import {
  AdminContainerView, AutocompleteSelectionRequiredDirective, BackNavigation,
  ConfirmationDialogComponent, CoreModule
} from 'ngx-inception/core';
import { ReplaySubject, Subject, Subscription } from 'rxjs';
import { debounceTime, finalize, first, startWith } from 'rxjs/operators';
import { SecurityService } from '../services/security.service';
import { UserDirectorySummaries } from '../services/user-directory-summaries';
import { UserDirectorySummary } from '../services/user-directory-summary';

/**
 * The TenantUserDirectoriesComponent class implements the tenant user directories
 * component.
 *
 * @author Marcus Portmann
 */
@Component({
  selector: 'inception-security-tenant-user-directories',
  imports: [CoreModule, AutocompleteSelectionRequiredDirective],
  standalone: true,
  templateUrl: 'tenant-user-directories.component.html',
  styleUrls: ['tenant-user-directories.component.css']
})
export class TenantUserDirectoriesComponent
  extends AdminContainerView
  implements AfterViewInit, OnDestroy
{
  allUserDirectories: UserDirectorySummary[] = [];

  availableUserDirectories$: Subject<UserDirectorySummary[]> = new ReplaySubject<
    UserDirectorySummary[]
  >(1);

  dataSource = new MatTableDataSource<UserDirectorySummary>([]);

  readonly displayedColumns = ['existingUserDirectoryName', 'actions'];

  filteredUserDirectories$: Subject<UserDirectorySummary[]> = new ReplaySubject<
    UserDirectorySummary[]
  >(1);

  newUserDirectoryControl: FormControl;

  @ViewChild(MatPaginator, { static: true }) paginator!: MatPaginator;

  tenantId: string;

  readonly title = $localize`:@@security_tenant_user_directories_title:Tenant User Directories`;

  private readonly securityService = inject(SecurityService);

  private readonly subscriptions: Subscription = new Subscription();

  constructor() {
    super();

    // Retrieve the route parameters
    const tenantId = this.activatedRoute.snapshot.paramMap.get('tenantId');

    if (!tenantId) {
      throw new globalThis.Error('No tenantId route parameter found');
    }

    this.tenantId = tenantId;

    // Initialize the form controls
    this.newUserDirectoryControl = new FormControl('');
  }

  override get backNavigation(): BackNavigation {
    return new BackNavigation(
      $localize`:@@security_tenant_user_directories_back_navigation:Back`,
      ['.'],
      {
        relativeTo: this.activatedRoute.parent?.parent
      }
    );
  }

  addUserDirectoryToTenant(): void {
    if (!this.isUserDirectorySelected()) {
      return;
    }

    const selectedUserDirectory = this.newUserDirectoryControl.value;
    const userDirectoryId = selectedUserDirectory?.id;

    if (!userDirectoryId) {
      return;
    }

    this.spinnerService.showSpinner();

    this.securityService
      .addUserDirectoryToTenant(this.tenantId, userDirectoryId)
      .pipe(
        first(),
        finalize(() => this.spinnerService.hideSpinner())
      )
      .subscribe({
        next: () => {
          this.loadUserDirectoriesForTenant();
          this.newUserDirectoryControl.setValue('');
        },
        error: (error: Error) => this.handleError(error, false)
      });
  }

  applyFilter(filterValue: string): void {
    filterValue = filterValue.trim();
    filterValue = filterValue.toLowerCase();
    this.dataSource.filter = filterValue;
  }

  displayUserDirectory(userDirectorySummary: UserDirectorySummary): string {
    return userDirectorySummary.name;
  }

  isUserDirectorySelected(): boolean {
    if (this.newUserDirectoryControl.value as UserDirectorySummary) {
      if (
        typeof this.newUserDirectoryControl.value.id !== 'undefined' &&
        this.newUserDirectoryControl.value.id !== null
      ) {
        return true;
      }
    }

    return false;
  }

  loadUserDirectoriesForTenant(): void {
    this.spinnerService.showSpinner();

    this.securityService
      .getUserDirectorySummariesForTenant(this.tenantId)
      .pipe(
        first(),
        finalize(() => this.spinnerService.hideSpinner())
      )
      .subscribe({
        next: (userDirectorySummaries: UserDirectorySummary[]) => {
          this.dataSource.data = userDirectorySummaries;

          const availableUserDirectories =
            TenantUserDirectoriesComponent.calculateAvailableUserDirectories(
              this.allUserDirectories,
              this.dataSource.data
            );

          this.subscriptions.add(
            this.newUserDirectoryControl.valueChanges
              .pipe(startWith(''), debounceTime(500))
              .subscribe((value) => {
                this.filteredUserDirectories$.next(
                  this.filterUserDirectories(availableUserDirectories, value)
                );
              })
          );

          this.availableUserDirectories$.next(availableUserDirectories);
        },
        error: (error: Error) => this.handleError(error, false)
      });
  }

  ngAfterViewInit(): void {
    this.dataSource.paginator = this.paginator;

    // Retrieve the existing user directories and initialize the table
    this.spinnerService.showSpinner();

    this.securityService
      .getUserDirectorySummaries()
      .pipe(
        first(),
        finalize(() => this.spinnerService.hideSpinner())
      )
      .subscribe({
        next: (userDirectorySummaries: UserDirectorySummaries) => {
          this.allUserDirectories = userDirectorySummaries.userDirectorySummaries;
          this.loadUserDirectoriesForTenant();
        },
        error: (error: Error) => this.handleError(error, false)
      });
  }

  ngOnDestroy(): void {
    this.subscriptions.unsubscribe();
  }

  removeUserDirectoryFromTenant(userDirectoryId: string): void {
    const dialogRef: MatDialogRef<ConfirmationDialogComponent, boolean> =
      this.dialogService.showConfirmationDialog({
        message: $localize`:@@security_tenant_user_directories_confirm_remove_user_directory_from_tenant:Are you sure you want to remove the user directory from the tenant?`
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
            .removeUserDirectoryFromTenant(this.tenantId, userDirectoryId)
            .pipe(
              first(),
              finalize(() => this.spinnerService.hideSpinner())
            )
            .subscribe({
              next: () => {
                this.loadUserDirectoriesForTenant();
                this.newUserDirectoryControl.setValue('');
              },
              error: (error: Error) => this.handleError(error, false)
            });
        }
      });
  }

  private static calculateAvailableUserDirectories(
    allUserDirectories: UserDirectorySummary[],
    existingTenantUserDirectories: UserDirectorySummary[]
  ): UserDirectorySummary[] {
    const availableUserDirectories: UserDirectorySummary[] = [];

    for (const possibleUserDirectory of allUserDirectories) {
      let foundExistingUserDirectory = false;

      for (const existingTenantUserDirectory of existingTenantUserDirectories) {
        if (possibleUserDirectory.id === existingTenantUserDirectory.id) {
          foundExistingUserDirectory = true;
          break;
        }
      }

      if (!foundExistingUserDirectory) {
        availableUserDirectories.push(possibleUserDirectory);
      }
    }

    return availableUserDirectories;
  }

  private filterUserDirectories(
    userDirectories: UserDirectorySummary[],
    value: string | UserDirectorySummary
  ): UserDirectorySummary[] {
    let filterValue = '';

    if (typeof value === 'string') {
      filterValue = (value as string).toLowerCase();
    } else if (typeof value === 'object') {
      filterValue = (value as UserDirectorySummary).name.toLowerCase();
    }

    return userDirectories.filter(
      (userDirectory) => userDirectory.name.toLowerCase().indexOf(filterValue) === 0
    );
  }
}
