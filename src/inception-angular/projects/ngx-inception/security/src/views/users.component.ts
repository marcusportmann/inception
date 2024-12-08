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

import {AfterViewInit, Component, HostBinding, OnDestroy, ViewChild} from '@angular/core';
import {MatPaginator} from '@angular/material/paginator';
import {MatSelect, MatSelectChange} from '@angular/material/select';
import {MatSort} from '@angular/material/sort';
import {ActivatedRoute, Router} from '@angular/router';
import {
  AccessDeniedError, AdminContainerView, DialogService, Error, InvalidArgumentError,
  ServiceUnavailableError, SessionService, SortDirection, SpinnerService, TableFilterComponent
} from 'ngx-inception/core';
import {BehaviorSubject, forkJoin, merge, Observable, of, Subject, tap, throwError} from 'rxjs';
import {
  catchError, debounceTime, filter, finalize, first, map, switchMap, takeUntil
} from 'rxjs/operators';
import {SecurityService} from '../services/security.service';
import {UserDataSource} from '../services/user-data-source';
import {UserDirectoryCapabilities} from '../services/user-directory-capabilities';
import {UserDirectorySummary} from '../services/user-directory-summary';
import {UserSortBy} from '../services/user-sort-by';
import {Users} from '../services/users';

/**
 * The UsersComponent class implements the users component.
 *
 * @author Marcus Portmann
 */
@Component({
  templateUrl: 'users.component.html',
  styleUrls: ['users.component.css']
})
export class UsersComponent extends AdminContainerView implements AfterViewInit, OnDestroy {
  dataSource: UserDataSource;

  displayedColumns = ['name', 'username', 'actions'];

  @HostBinding('class') hostClass = 'flex flex-column flex-fill';

  @ViewChild(MatPaginator, {static: true}) paginator!: MatPaginator;

  @ViewChild(MatSort, {static: true}) sort!: MatSort;

  @ViewChild(TableFilterComponent, {static: true}) tableFilter!: TableFilterComponent;

  userDirectories: UserDirectorySummary[] = [];

  userDirectoryCapabilities$ = new BehaviorSubject<UserDirectoryCapabilities | null>(null);

  userDirectoryId$: BehaviorSubject<string> = new BehaviorSubject<string>('');

  @ViewChild('userDirectorySelect', {static: true}) userDirectorySelect!: MatSelect;

  private destroy$ = new Subject<void>();

  constructor(private router: Router, private activatedRoute: ActivatedRoute,
              private securityService: SecurityService, private sessionService: SessionService,
              private dialogService: DialogService, private spinnerService: SpinnerService) {
    super();
    this.dataSource = new UserDataSource(this.securityService);
  }

  get enableActionsMenu$(): Observable<boolean> {
    return this.userDirectoryCapabilities$.pipe(map((userDirectoryCapabilities) => {
      if (userDirectoryCapabilities) {
        return userDirectoryCapabilities.supportsUserAdministration || userDirectoryCapabilities.supportsGroupAdministration || userDirectoryCapabilities.supportsAdminChangePassword;
      } else {
        return false;
      }
    }));
  }

  get enableNewButton$(): Observable<boolean> {
    return this.userDirectoryCapabilities$.pipe(map((userDirectoryCapabilities) => {
      if (userDirectoryCapabilities) {
        return userDirectoryCapabilities.supportsUserAdministration;
      } else {
        return false;
      }
    }));
  }

  get title(): string {
    return $localize`:@@security_users_title:Users`
  }

  deleteUser(username: string): void {
    const dialogRef = this.dialogService.showConfirmationDialog({
      message: $localize`:@@security_users_confirm_delete_user:Are you sure you want to delete the user?`,
    });

    dialogRef
    .afterClosed()
    .pipe(first(), filter((confirmed) => confirmed === true), switchMap(() => {
      this.spinnerService.showSpinner();
      return this.securityService
      .deleteUser(this.userDirectoryId$.value, username)
      .pipe(catchError((error) => this.handleError(error)), tap(() => this.resetTable()),
        // After delete completes
        switchMap(() => this.loadUsers().pipe(catchError((error) => this.handleError(error)))),
        finalize(() => this.spinnerService.hideSpinner()));
    }), takeUntil(this.destroy$))
    .subscribe();
  }

  editUser(username: string): void {
    // noinspection JSIgnoredPromiseFromCall
    this.router.navigate(
      [this.userDirectoryId$.value + '/' + encodeURIComponent(username) + '/edit'],
      {relativeTo: this.activatedRoute});
  }

  loadUsers(): Observable<Users> {
    const filter = this.tableFilter.filter?.trim().toLowerCase() || '';
    let sortBy: UserSortBy = UserSortBy.Username;
    let sortDirection = SortDirection.Ascending;

    if (!!this.sort.active) {
      if (this.sort.active === 'name') {
        sortBy = UserSortBy.Name;
      } else if (this.sort.active === 'preferredName') {
        sortBy = UserSortBy.PreferredName;
      }

      if (this.sort.direction === 'desc') {
        sortDirection = SortDirection.Descending;
      }
    }

    if (this.userDirectoryId$.value) {
      return this.dataSource
      .load(this.userDirectoryId$.value, filter, sortBy, sortDirection, this.paginator.pageIndex,
        this.paginator.pageSize)
      .pipe(catchError((error) => {
        return this.handleError(error);
      }));
    } else {
      this.dataSource.clear();
      return of();
    }
  }

  newUser(): void {
    // noinspection JSIgnoredPromiseFromCall
    this.router.navigate([this.userDirectoryId$.value + '/new'], {relativeTo: this.activatedRoute});
  }

  ngAfterViewInit(): void {
    // Handle user directory selection changes
    this.userDirectoryId$
    .pipe(takeUntil(this.destroy$), switchMap((userDirectoryId) => {
      if (userDirectoryId) {
        this.resetTable();
        this.spinnerService.showSpinner();

        return forkJoin({
          userDirectoryCapabilities: this.securityService
          .getUserDirectoryCapabilities(userDirectoryId)
          .pipe(catchError((error) => this.handleError(error))),
          groups: this.loadUsers().pipe(catchError((error) => this.handleError(error))),
        }).pipe(tap(({userDirectoryCapabilities}) => this.userDirectoryCapabilities$.next(
          userDirectoryCapabilities)), finalize(() => this.spinnerService.hideSpinner()));
      } else {
        return of();
      }
    }))
    .subscribe();

    // Handle selection changes in the user directory select
    this.userDirectorySelect.selectionChange
    .pipe(takeUntil(this.destroy$))
    .subscribe((change: MatSelectChange) => {
      this.userDirectoryId$.next(change.value);
    });

    // Reset paginator on sort or filter changes
    merge(this.sort.sortChange, this.tableFilter.changed)
    .pipe(takeUntil(this.destroy$))
    .subscribe(() => (this.paginator.pageIndex = 0));

    // Load groups on sort, filter, or pagination changes
    merge(this.sort.sortChange, this.tableFilter.changed, this.paginator.page)
    .pipe(debounceTime(200), // Avoid redundant API calls
      takeUntil(this.destroy$))
    .subscribe(() => {
      if (this.userDirectoryId$.value) {
        this.spinnerService.showSpinner();
        this.loadUsers()
        .pipe(catchError((error) => this.handleError(error)),
          finalize(() => this.spinnerService.hideSpinner()))
        .subscribe();
      }
    });

    // Load user directories for the tenant
    this.sessionService.session$
    .pipe(first(), switchMap((session) => {
      if (session?.tenantId) {
        this.spinnerService.showSpinner();
        return this.securityService
        .getUserDirectorySummariesForTenant(session.tenantId)
        .pipe(catchError((error) => this.handleError(error)),
          finalize(() => this.spinnerService.hideSpinner()));
      } else {
        return of([]);
      }
    }), takeUntil(this.destroy$))
    .subscribe((userDirectories: UserDirectorySummary[]) => {
      this.userDirectories = userDirectories;

      if (this.userDirectories.length === 1) {
        this.userDirectoryId$.next(this.userDirectories[0].id);
      } else {
        this.activatedRoute.paramMap
        .pipe(first(), map(() => window.history.state), takeUntil(this.destroy$))
        .subscribe((state) => {
          const userDirectoryId = state.userDirectoryId;
          if (userDirectoryId) {
            const matchingDirectory = this.userDirectories.find((ud) => ud.id === userDirectoryId);
            if (matchingDirectory) {
              this.userDirectorySelect.value = matchingDirectory.id;
              this.userDirectoryId$.next(matchingDirectory.id);
            }
          }
        });
      }
    });
  }

  ngOnDestroy(): void {
    this.destroy$.next();
    this.destroy$.complete();
  }

  resetUserPassword(username: string): void {
    // noinspection JSIgnoredPromiseFromCall
    this.router.navigate(
      [this.userDirectoryId$.value + '/' + encodeURIComponent(username) + '/reset-user-password'],
      {relativeTo: this.activatedRoute});
  }

  userGroups(username: string): void {
    // noinspection JSIgnoredPromiseFromCall
    this.router.navigate(
      [this.userDirectoryId$.value + '/' + encodeURIComponent(username) + '/groups'],
      {relativeTo: this.activatedRoute});
  }

  private handleError(error: Error): Observable<never> {
    if (error instanceof AccessDeniedError || error instanceof InvalidArgumentError || error instanceof ServiceUnavailableError) {
      this.router.navigateByUrl('/error/send-error-report', {state: {error}});
    } else {
      this.dialogService.showErrorDialog(error);
    }
    return throwError(() => error);
  }

  private resetTable(): void {
    this.tableFilter.reset(false);
    this.paginator.pageIndex = 0;
    this.sort.active = '';
    this.sort.direction = 'asc' as SortDirection;
  }
}
