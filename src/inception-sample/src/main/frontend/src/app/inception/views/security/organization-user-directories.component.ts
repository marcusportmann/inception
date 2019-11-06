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
import {UserDirectorySummary} from "../../services/security/user-directory-summary";
import {UserDirectorySummaries} from "../../services/security/user-directory-summaries";
import {MatPaginator} from "@angular/material/paginator";

/**
 * The OrganizationUserDirectoriesComponent class implements the organization user directories
 * component.
 *
 * @author Marcus Portmann
 */
@Component({
  templateUrl: 'organization-user-directories.component.html',
  styleUrls: ['organization-user-directories.component.css']
})
export class OrganizationUserDirectoriesComponent extends AdminContainerView
  implements AfterViewInit, OnDestroy {

  private subscriptions: Subscription = new Subscription();

  allUserDirectories: UserDirectorySummary[] = [];

  availableUserDirectories$: Subject<UserDirectorySummary[]> = new ReplaySubject<UserDirectorySummary[]>();

  dataSource = new MatTableDataSource<UserDirectorySummary>([]);

  displayedColumns = ['existingUserDirectoryName', 'actions'];

  organizationId: string;

  @ViewChild(MatPaginator, {static: true}) paginator?: MatPaginator;

  selectedUserDirectory?: UserDirectorySummary;

  constructor(private router: Router, private activatedRoute: ActivatedRoute,
              private formBuilder: FormBuilder, private i18n: I18n,
              private securityService: SecurityService, private dialogService: DialogService,
              private spinnerService: SpinnerService) {
    super();

    // Retrieve parameters
    this.organizationId =
      decodeURIComponent(this.activatedRoute.snapshot.paramMap.get('organizationId')!);
  }

  get backNavigation(): BackNavigation {
    return new BackNavigation(this.i18n({
      id: '@@organization_user_directories_component_back_title',
      value: 'Back'
    }), ['../..'], {
      relativeTo: this.activatedRoute
    });
  }

  get title(): string {
    return this.i18n({
      id: '@@organization_user_directories_component_title',
      value: 'Organization User Directories'
    })
  }

  addUserDirectoryToOrganization(): void {
    if (this.selectedUserDirectory) {
      this.spinnerService.showSpinner();

      this.securityService.addUserDirectoryToOrganization(this.organizationId,
        this.selectedUserDirectory.id)
        .pipe(first(), finalize(() => this.spinnerService.hideSpinner()))
        .subscribe(() => {
          this.loadUserDirectoriesForOrganization();
          this.selectedUserDirectory = undefined;
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

  applyFilter(filterValue: string): void {
    filterValue = filterValue.trim();
    filterValue = filterValue.toLowerCase();
    this.dataSource.filter = filterValue;
  }

  loadUserDirectoriesForOrganization(): void {
    this.spinnerService.showSpinner();

    this.securityService.getUserDirectorySummariesForOrganization(this.organizationId)
      .pipe(first(), finalize(() => this.spinnerService.hideSpinner()))
      .subscribe((userDirectorySummaries: UserDirectorySummary[]) => {
        this.dataSource.data = userDirectorySummaries;

        this.availableUserDirectories$.next(
          this.calculateAvailableUserDirectories(this.allUserDirectories, this.dataSource.data));
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

    this.securityService.getUserDirectorySummaries()
      .pipe(first(), finalize(() => this.spinnerService.hideSpinner()))
      .subscribe((userDirectorySummaries: UserDirectorySummaries) => {
        this.allUserDirectories = userDirectorySummaries.userDirectorySummaries;

        this.loadUserDirectoriesForOrganization();
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

  removeUserDirectoryFromOrganization(userDirectoryId: string) {
    const dialogRef: MatDialogRef<ConfirmationDialogComponent, boolean> = this.dialogService.showConfirmationDialog(
      {
        message: this.i18n({
          id: '@@organization_user_directories_component_confirm_remove_user_directory_from_organization',
          value: 'Are you sure you want to remove the user directory from the organization?'
        })
      });

    dialogRef.afterClosed()
      .pipe(first())
      .subscribe((confirmation: boolean | undefined) => {
        if (confirmation === true) {
          this.spinnerService.showSpinner();

          this.securityService.removeUserDirectoryFromOrganization(this.organizationId,
            userDirectoryId)
            .pipe(first(), finalize(() => this.spinnerService.hideSpinner()))
            .subscribe(() => {
              this.loadUserDirectoriesForOrganization();
              this.selectedUserDirectory = undefined;
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

  private calculateAvailableUserDirectories(allUserDirectories: UserDirectorySummary[],
                                            existingOrganizationUserDirectories: UserDirectorySummary[]): UserDirectorySummary[] {

    let availableUserDirectories: UserDirectorySummary[] = [];

    for (let i = 0; i < allUserDirectories.length; i++) {
      let foundExistingUserDirectory: boolean = false;

      for (let j = 0; j < existingOrganizationUserDirectories.length; j++) {
        if (allUserDirectories[i].id === existingOrganizationUserDirectories[j].id) {
          foundExistingUserDirectory = true;
          break;
        }
      }

      if (!foundExistingUserDirectory) {
        availableUserDirectories.push(allUserDirectories[i]);
      }
    }

    return availableUserDirectories;
  }
}
