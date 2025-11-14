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

import { AfterViewInit, Component, HostBinding, OnDestroy, ViewChild } from '@angular/core';
import { MatPaginator } from '@angular/material/paginator';
import { MatSelect, MatSelectChange } from '@angular/material/select';
import { MatSort } from '@angular/material/sort';
import {
  AdminContainerView, CoreModule, Error, HasAuthorityDirective, Session, SessionService, SortDirection,
  TableFilterComponent
} from 'ngx-inception/core';
import { BehaviorSubject, EMPTY, forkJoin, merge, Observable, of, Subject, tap } from 'rxjs';
import {
  catchError, debounceTime, filter, finalize, first, map, switchMap, takeUntil
} from 'rxjs/operators';
import { SecurityService } from '../services/security.service';
import { UserDataSource } from '../services/user-data-source';
import { UserDirectoryCapabilities } from '../services/user-directory-capabilities';
import { UserDirectorySummary } from '../services/user-directory-summary';
import { UserSortBy } from '../services/user-sort-by';
import { Users } from '../services/users';

/**
 * The UsersComponent class implements the users component.
 *
 * @author Marcus Portmann
 */
@Component({
  selector: 'inception-security-users',
  imports: [CoreModule, TableFilterComponent, HasAuthorityDirective],
  standalone: true,
  templateUrl: 'users.component.html',
  styleUrls: ['users.component.css']
})
export class UsersComponent extends AdminContainerView implements AfterViewInit, OnDestroy {
  dataSource: UserDataSource;

  displayedColumns = ['name', 'username', 'actions'];

  @HostBinding('class') hostClass = 'flex flex-column flex-fill';

  @ViewChild(MatPaginator, { static: true }) paginator!: MatPaginator;

  @ViewChild(MatSort, { static: true }) sort!: MatSort;

  @ViewChild(TableFilterComponent, { static: true })
  tableFilter!: TableFilterComponent;

  readonly title = $localize`:@@security_users_title:Users`;

  userDirectories: UserDirectorySummary[] = [];

  userDirectoryCapabilities$ = new BehaviorSubject<UserDirectoryCapabilities | null>(null);

  userDirectoryId$: BehaviorSubject<string> = new BehaviorSubject<string>('');

  @ViewChild('userDirectorySelect', { static: true })
  userDirectorySelect!: MatSelect;

  private destroy$ = new Subject<void>();

  constructor(
    private securityService: SecurityService,
    private sessionService: SessionService
  ) {
    super();
    this.dataSource = new UserDataSource(this.securityService);
  }

  get enableActionsMenu$(): Observable<boolean> {
    return this.userDirectoryCapabilities$.pipe(
      map((userDirectoryCapabilities) => {
        if (userDirectoryCapabilities) {
          return (
            userDirectoryCapabilities.supportsUserAdministration ||
            userDirectoryCapabilities.supportsGroupAdministration ||
            userDirectoryCapabilities.supportsAdminChangePassword
          );
        } else {
          return false;
        }
      })
    );
  }

  get enableNewButton$(): Observable<boolean> {
    return this.userDirectoryCapabilities$.pipe(
      map((userDirectoryCapabilities) => {
        if (userDirectoryCapabilities) {
          return userDirectoryCapabilities.supportsUserAdministration;
        } else {
          return false;
        }
      })
    );
  }

  deleteUser(username: string): void {
    const message = $localize`:@@security_users_confirm_delete_user:Are you sure you want to delete the user?`;
    this.confirmAndProcessAction(message, () =>
      this.securityService.deleteUser(this.userDirectoryId$.value, username)
    );
  }

  editUser(username: string): void {
    // noinspection JSIgnoredPromiseFromCall
    this.router.navigate(
      [this.userDirectoryId$.value + '/' + encodeURIComponent(username) + '/edit'],
      { relativeTo: this.activatedRoute }
    );
  }

  loadUsers(): Observable<Users> {
    const filter = this.tableFilter.filter?.trim().toLowerCase() || '';
    let sortBy: UserSortBy = UserSortBy.Username;
    let sortDirection = SortDirection.Ascending;

    if (this.sort.active) {
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
        .load(
          this.userDirectoryId$.value,
          filter,
          sortBy,
          sortDirection,
          this.paginator.pageIndex,
          this.paginator.pageSize
        )
        .pipe(
          catchError((error) => {
            this.handleError(error, false);

            return EMPTY;
          })
        );
    } else {
      this.dataSource.clear();
      return of();
    }
  }

  newUser(): void {
    // noinspection JSIgnoredPromiseFromCall
    this.router.navigate([this.userDirectoryId$.value + '/new'], {
      relativeTo: this.activatedRoute
    });
  }

  ngAfterViewInit(): void {
    this.initializeDataLoaders();
    this.loadData();
  }

  ngOnDestroy(): void {
    this.destroy$.next();
    this.destroy$.complete();
  }

  resetUserPassword(username: string): void {
    // noinspection JSIgnoredPromiseFromCall
    this.router.navigate(
      [this.userDirectoryId$.value + '/' + encodeURIComponent(username) + '/reset-user-password'],
      { relativeTo: this.activatedRoute }
    );
  }

  userGroups(username: string): void {
    // noinspection JSIgnoredPromiseFromCall
    this.router.navigate(
      [this.userDirectoryId$.value + '/' + encodeURIComponent(username) + '/groups'],
      { relativeTo: this.activatedRoute }
    );
  }

  private confirmAndProcessAction(
    confirmationMessage: string,
    action: () => Observable<void | unknown>
  ): void {
    const dialogRef = this.dialogService.showConfirmationDialog({
      message: confirmationMessage
    });

    dialogRef
      .afterClosed()
      .pipe(
        first(),
        filter((confirmed: boolean | undefined) => confirmed === true),
        switchMap(() => {
          this.spinnerService.showSpinner();

          return action().pipe(
            catchError((error: Error) => {
              this.handleError(error, false);
              return EMPTY;
            }),
            tap(() => this.resetTable()),
            switchMap(() =>
              this.loadUsers().pipe(
                catchError((error: Error) => {
                  this.handleError(error, false);
                  return EMPTY;
                })
              )
            ),
            finalize(() => this.spinnerService.hideSpinner())
          );
        }),
        takeUntil(this.destroy$)
      )
      .subscribe();
  }

  private initializeDataLoaders(): void {
    // Handle user directory selection changes
    this.userDirectoryId$
      .pipe(
        takeUntil(this.destroy$),
        switchMap((userDirectoryId: string | null | undefined) => {
          if (!userDirectoryId) {
            return EMPTY;
          }

          this.resetTable();
          this.spinnerService.showSpinner();

          return forkJoin({
            userDirectoryCapabilities: this.securityService
              .getUserDirectoryCapabilities(userDirectoryId)
              .pipe(
                catchError((error: Error) => {
                  this.handleError(error, false);
                  return EMPTY;
                })
              ),
            groups: this.loadUsers().pipe(
              catchError((error: Error) => {
                this.handleError(error, false);
                return EMPTY;
              })
            )
          }).pipe(
            tap(({ userDirectoryCapabilities }) =>
              this.userDirectoryCapabilities$.next(userDirectoryCapabilities)
            ),
            finalize(() => this.spinnerService.hideSpinner())
          );
        })
      )
      .subscribe({
        error: (error: Error) => this.handleError(error, false)
      });

    // Handle selection changes in the user directory select
    this.userDirectorySelect.selectionChange.pipe(takeUntil(this.destroy$)).subscribe({
      next: (change: MatSelectChange) => {
        this.userDirectoryId$.next(change.value);
      },
      error: (error: Error) => this.handleError(error, false)
    });

    // Reset paginator on sort or filter changes
    merge(this.sort.sortChange, this.tableFilter.changed)
      .pipe(takeUntil(this.destroy$))
      .subscribe({
        next: () => {
          if (this.paginator) {
            this.paginator.pageIndex = 0;
          }
        },
        error: (error: Error) => this.handleError(error, false)
      });

    // Load users on sort, filter, or pagination changes
    merge(this.sort.sortChange, this.tableFilter.changed, this.paginator.page)
      .pipe(
        debounceTime(200), // Avoid redundant API calls
        takeUntil(this.destroy$),
        switchMap(() => {
          const userDirectoryId = this.userDirectoryId$.value;

          if (!userDirectoryId) {
            return EMPTY;
          }

          this.spinnerService.showSpinner();

          return this.loadUsers().pipe(
            catchError((error: Error) => {
              this.handleError(error, false);
              return EMPTY;
            }),
            finalize(() => this.spinnerService.hideSpinner())
          );
        })
      )
      .subscribe({
        error: (error: Error) => this.handleError(error, false)
      });
  }

  private loadData(): void {
    // Load user directories for the tenant
    this.sessionService.session$
      .pipe(
        first(),
        switchMap((session: Session | null) => {
          if (session?.tenantId) {
            this.spinnerService.showSpinner();

            return this.securityService.getUserDirectorySummariesForTenant(session.tenantId).pipe(
              catchError((error: Error) => {
                this.handleError(error, false);
                return EMPTY;
              }),
              finalize(() => this.spinnerService.hideSpinner())
            );
          }

          return of([] as UserDirectorySummary[]);
        }),
        takeUntil(this.destroy$)
      )
      .subscribe({
        next: (userDirectories: UserDirectorySummary[]) => {
          this.userDirectories = userDirectories;

          if (this.userDirectories.length === 1) {
            this.userDirectoryId$.next(this.userDirectories[0].id);
            return;
          }

          this.activatedRoute.paramMap
            .pipe(
              first(),
              map(() => window.history.state),
              takeUntil(this.destroy$)
            )
            .subscribe({
              next: (state) => {
                const userDirectoryId = state.userDirectoryId;
                if (userDirectoryId) {
                  const matchingDirectory = this.userDirectories.find(
                    (ud) => ud.id === userDirectoryId
                  );
                  if (matchingDirectory) {
                    this.userDirectorySelect.value = matchingDirectory.id;
                    this.userDirectoryId$.next(matchingDirectory.id);
                  }
                }
              },
              error: (error: Error) => this.handleError(error, false)
            });
        },
        error: (error: Error) => this.handleError(error, false)
      });
  }

  private resetTable(): void {
    this.tableFilter.reset(false);
    this.paginator.pageIndex = 0;
    this.sort.active = '';
    this.sort.direction = 'asc' as SortDirection;
  }
}
