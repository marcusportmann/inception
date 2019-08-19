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

import {AfterViewInit, Component, OnDestroy, OnInit, ViewChild} from '@angular/core';
import {MatDialogRef} from '@angular/material/dialog';
import {MatPaginator} from '@angular/material/paginator';
import {MatSort} from '@angular/material/sort';
import {finalize, first, map, tap} from 'rxjs/operators';
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
import {UserDatasource} from '../../services/security/user.datasource';
import {SessionService} from '../../services/session/session.service';
import {Session} from '../../services/session/session';
import {UserDirectorySummary} from '../../services/security/user-directory-summary';
import {FormBuilder} from '@angular/forms';
import {MatSelect, MatSelectChange} from '@angular/material';
import {UserSortBy} from '../../services/security/user-sort-by';
import {AdminContainerView} from '../../components/layout/admin-container-view';

/**
 * The UsersComponent class implements the users component.
 *
 * @author Marcus Portmann
 */
@Component({
  templateUrl: 'users.component.html',
  styleUrls: ['users.component.css'],
  host: {
    'class': 'flex flex-column flex-fill',
  },
  styles: [`
    .select-user-directory-container
    {
      display: flex;
      justify-content: space-between;
      align-items: center;
      border-bottom: 1px solid #c2cfd6;
      padding: 8px !important;
    }

    .select-user-directory-container > span
    {
      white-space: nowrap;
      flex: 0;
      padding-right: 8px;
    }

    .select-user-directory-container > .mat-form-field
    {
      flex: 1;
    }
  `
  ]
})
export class UsersComponent extends AdminContainerView implements AfterViewInit, OnDestroy, OnInit {

  private subscriptions: Subscription = new Subscription();

  dataSource: UserDatasource;

  displayedColumns = ['firstName', 'lastName', 'username', 'actions'];

  @ViewChild(MatPaginator, {static: true}) paginator?: MatPaginator;

  @ViewChild(MatSort, {static: true}) sort?: MatSort;

  @ViewChild(TableFilter, {static: true}) tableFilter?: TableFilter;

  userDirectoryId = '';

  userDirectories: UserDirectorySummary[] = [];

  @ViewChild('userDirectorySelect', {static: true}) userDirectorySelect?: MatSelect;

  constructor(private router: Router, private activatedRoute: ActivatedRoute,
              private formBuilder: FormBuilder, private i18n: I18n,
              private securityService: SecurityService, private sessionService: SessionService,
              private dialogService: DialogService, private spinnerService: SpinnerService) {
    super();

    this.dataSource = new UserDatasource(this.sessionService, this.securityService);
  }

  get title(): string {
    return this.i18n({
      id: '@@users_component_title',
      value: 'Users'
    })
  }

  get isUserDirectorySelected(): boolean {
    return (!!this.userDirectoryId);
  }

  // noinspection JSUnusedLocalSymbols
  deleteUser(username: string): void {
    // noinspection JSUnusedLocalSymbols
    const dialogRef: MatDialogRef<ConfirmationDialogComponent, boolean> = this.dialogService.showConfirmationDialog(
      {
        message: this.i18n({
          id: '@@users_component_confirm_delete_user',
          value: 'Are you sure you want to delete the user?'
        })
      });

    /*
    dialogRef.afterClosed()
      .pipe(first())
      .subscribe((confirmation: boolean) => {
        if (confirmation === true) {
          this.spinnerService.showSpinner();

          this.securityService.deleteUser(userId)
            .pipe(first(), finalize(() => this.spinnerService.hideSpinner()))
            .subscribe(() => {
              this.dataSource.load('', SortDirection.Ascending, 0, 10);
            }, (error: Error) => {
              if ((error instanceof SecurityServiceError) || (error instanceof AccessDeniedError) ||
                (error instanceof SystemUnavailableError)) {
                // noinspection JSIgnoredPromiseFromCall
                this.router.navigateByUrl('/error/send-error-report', {state: {error: error}});
              } else {
                this.dialogService.showErrorDialog(error);
              }
            });
        }
      });

     */

  }

  editUser(username: string): void {
    // noinspection JSIgnoredPromiseFromCall
    this.router.navigate(
      [encodeURIComponent(this.userDirectoryId) + '/' + encodeURIComponent(username) + '/edit'],
      {relativeTo: this.activatedRoute});
  }

  loadUsers(): void {
    let filter = '';

    if (!!this.tableFilter!.filter) {
      filter = this.tableFilter!.filter;
      filter = filter.trim();
      filter = filter.toLowerCase();
    }

    let sortBy: UserSortBy = UserSortBy.Username;

    if (!!this.sort!.active) {
      if (this.sort!.active === 'firstName') {
        sortBy = UserSortBy.FirstName;
      } else if (this.sort!.active === 'lastName') {
        sortBy = UserSortBy.LastName;
      }
    }

    const sortDirection = this.sort!.direction === 'asc' ? SortDirection.Ascending : SortDirection.Descending;

    this.dataSource.load(this.userDirectoryId, filter, sortBy, sortDirection,
      this.paginator!.pageIndex, this.paginator!.pageSize);
  }

  newUser(): void {
    // noinspection JSIgnoredPromiseFromCall
    this.router.navigate([encodeURIComponent(this.userDirectoryId) + '/new'], {relativeTo: this.activatedRoute});
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

    this.subscriptions.add(this.userDirectorySelect!.selectionChange.subscribe(
      (matSelectChange: MatSelectChange) => {
        this.userDirectoryId = matSelectChange.value;

        this.tableFilter!.reset(false);

        this.paginator!.pageIndex = 0;

        this.sort!.active = '';
        this.sort!.direction = 'asc' as SortDirection;
        this.sort!.sortChange.emit();
      }));

    this.subscriptions.add(
      this.sort!.sortChange.subscribe(() => {
        if (this.paginator) {
          this.paginator.pageIndex = 0
        }
      }));

    this.subscriptions.add(
      this.tableFilter!.changed.subscribe(() => {
        if (this.paginator) {
          this.paginator.pageIndex = 0
        }
      }));

    /*
     * NOTE: Changing the selected user directory will generate a "sort change" event, which will
     *       trigger the load of the users. If we also merged the "selection change" event from the
     *       userDirectorySelect MatSelect component instance we would trigger an unnecessary
     *       duplicate reload of the users.
     */
    this.subscriptions.add(
      merge(this.sort!.sortChange, this.tableFilter!.changed, this.paginator!.page)
        .pipe(tap(() => {
          if (!!this.userDirectoryId) {
            this.loadUsers();
          }
        })).subscribe());

    this.sessionService.session.pipe(first()).subscribe((session: Session | null) => {
      if (session && session.organization) {
        this.spinnerService.showSpinner();

        this.securityService.getUserDirectorySummariesForOrganization(session.organization.id)
          .pipe(first(), finalize(() => this.spinnerService.hideSpinner()))
          .subscribe((userDirectories: UserDirectorySummary[]) => {
            this.userDirectories = userDirectories;

            /*
             * If we only have one user directory available then load its users, otherwise if we
             * have a pre-selected user directory and it is one of the available user directories
             * then load its users.
             */
            if (userDirectories.length === 1) {
              this.userDirectoryId = userDirectories[0].id;
              this.loadUsers();
            } else if (!!this.userDirectoryId) {
              userDirectories.forEach((userDirectory: UserDirectorySummary) => {
                if (userDirectory.id === this.userDirectoryId) {
                  this.loadUsers();
                }
              });
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
          });
      }
    });
  }

  ngOnDestroy(): void {
    this.subscriptions.unsubscribe();
  }

  ngOnInit(): void {
    // If a user directory ID has been passed in then select the appropriate user directory
    this.activatedRoute.paramMap
      .pipe(first(), map(() => window.history.state))
      .subscribe((state) => {
        if (state.userDirectoryId) {
          this.userDirectoryId = state.userDirectoryId;
        }
      });
  }
}
