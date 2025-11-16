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

import {
  AfterViewInit, ChangeDetectorRef, Component, HostBinding, inject, ViewChild
} from '@angular/core';
import { MatPaginator } from '@angular/material/paginator';
import { MatSelect, MatSelectChange } from '@angular/material/select';
import { MatSort } from '@angular/material/sort';
import {
  CoreModule, Error, HasAuthorityDirective, Session, SessionService, SortDirection,
  TableFilterComponent
} from 'ngx-inception/core';
import { BehaviorSubject, EMPTY, forkJoin, Observable, of } from 'rxjs';
import { catchError, finalize, first, map, switchMap, takeUntil, tap } from 'rxjs/operators';
import { StatefulListView } from '../../../core/src/layout/components/stateful-list.view';
import { SecurityService } from '../services/security.service';
import { UserDataSource } from '../services/user-data-source';
import { UserDirectoryCapabilities } from '../services/user-directory-capabilities';
import { UserDirectorySummary } from '../services/user-directory-summary';
import { UserSortBy } from '../services/user-sort-by';
import { Users } from '../services/users';

interface UsersListExtras {
  userDirectoryId: string | null;
}

/**
 * The UsersComponent class implements the Users component.
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
export class UsersComponent
  extends StatefulListView<UsersListExtras>
  implements AfterViewInit
{
  readonly dataSource: UserDataSource;

  displayedColumns = ['name', 'username', 'actions'];

  @HostBinding('class') hostClass = 'flex flex-column flex-fill';

  readonly listKey = 'security.users';

  @ViewChild(MatPaginator, { static: true }) override paginator!: MatPaginator;

  @ViewChild(MatSort, { static: true }) override sort!: MatSort;

  @ViewChild(TableFilterComponent, { static: true })
  override tableFilter!: TableFilterComponent;

  readonly title = $localize`:@@security_users_title:Users`;

  userDirectories: UserDirectorySummary[] = [];

  userDirectoryCapabilities$ = new BehaviorSubject<UserDirectoryCapabilities | null>(null);

  userDirectoryId$ = new BehaviorSubject<string | null>(null);

  @ViewChild('userDirectorySelect', { static: true })
  userDirectorySelect!: MatSelect;

  private readonly changeDetectorRef = inject(ChangeDetectorRef);

  /** Whether this navigation requested a state reset (from the sidebar). */
  private readonly resetStateRequested: boolean;

  constructor(
    private securityService: SecurityService,
    private sessionService: SessionService
  ) {
    super();

    const nav = this.router.getCurrentNavigation();
    this.resetStateRequested = !!nav?.extras.state?.['resetState'];

    this.dataSource = new UserDataSource(this.securityService);
  }

  get enableActionsMenu$(): Observable<boolean> {
    return this.userDirectoryCapabilities$.pipe(
      map((caps) => {
        if (!caps) {
          return false;
        }
        return (
          caps.supportsUserAdministration ||
          caps.supportsGroupAdministration ||
          caps.supportsAdminChangePassword
        );
      })
    );
  }

  get enableNewButton$(): Observable<boolean> {
    return this.userDirectoryCapabilities$.pipe(
      map((caps) => !!caps && caps.supportsUserAdministration)
    );
  }

  deleteUser(username: string): void {
    const userDirectoryId = this.userDirectoryId$.value;
    if (!userDirectoryId) {
      return;
    }

    const message = $localize`:@@security_users_confirm_delete_user:Are you sure you want to delete the user?`;

    this.confirmAndProcessAction(
      message,
      () => this.securityService.deleteUser(userDirectoryId, username),
      () => this.loadUsers(userDirectoryId)
    );
  }

  editUser(username: string): void {
    const userDirectoryId = this.userDirectoryId$.value;
    if (!userDirectoryId) {
      return;
    }

    // Make sure the current list state (page, sort, filter, directory) is persisted
    this.saveState();

    // noinspection JSIgnoredPromiseFromCall
    this.router.navigate(
      [userDirectoryId + '/' + encodeURIComponent(username) + '/edit'],
      {
        relativeTo: this.activatedRoute
      }
    );
  }

  newUser(): void {
    const userDirectoryId = this.userDirectoryId$.value;
    if (!userDirectoryId) {
      return;
    }

    this.saveState();

    // noinspection JSIgnoredPromiseFromCall
    this.router.navigate([userDirectoryId + '/new'], {
      relativeTo: this.activatedRoute
    });
  }

  ngAfterViewInit(): void {
    const directorySelection$ = this.userDirectorySelect.selectionChange.pipe(
      tap((change: MatSelectChange) => {
        this.userDirectoryId$.next(change.value);
      })
    );

    // Initialize generic stateful list behavior. We use the *selectionChange* stream, not the
    // BehaviorSubject itself, so we don't get a synthetic emission during restore that resets the
    // page.
    this.initializeStatefulList(
      this.resetStateRequested,
      () => this.loadUsersData(),
      [
        {
          observable: directorySelection$,
          resetPage: true
        }
      ]
    );

    // Load available directories and align the selection with the restored state / nav state.
    this.loadUserDirectories();

    // Stabilize view after sort/paginator mutations
    this.changeDetectorRef.detectChanges();
  }

  resetUserPassword(username: string): void {
    const userDirectoryId = this.userDirectoryId$.value;
    if (!userDirectoryId) {
      return;
    }

    this.saveState();

    // noinspection JSIgnoredPromiseFromCall
    this.router.navigate(
      [userDirectoryId + '/' + encodeURIComponent(username) + '/reset-user-password'],
      { relativeTo: this.activatedRoute }
    );
  }

  userGroups(username: string): void {
    const userDirectoryId = this.userDirectoryId$.value;
    if (!userDirectoryId) {
      return;
    }

    this.saveState();

    // noinspection JSIgnoredPromiseFromCall
    this.router.navigate(
      [userDirectoryId + '/' + encodeURIComponent(username) + '/groups'],
      { relativeTo: this.activatedRoute }
    );
  }

  protected override getExtrasState(): UsersListExtras | undefined {
    return {
      userDirectoryId: this.userDirectoryId$.value
    };
  }

  protected override resetExtrasState(): void {
    this.userDirectoryId$.next(null);
    this.userDirectoryCapabilities$.next(null);
    this.userDirectories = [];
    this.dataSource.clear();

    if (this.userDirectorySelect) {
      this.userDirectorySelect.value = null;
    }
  }

  protected override restoreExtrasState(extras: UsersListExtras | undefined): void {
    if (extras?.userDirectoryId) {
      this.userDirectoryId$.next(extras.userDirectoryId);

      if (this.userDirectorySelect) {
        this.userDirectorySelect.value = extras.userDirectoryId;
      }
    }
  }

  private loadUserDirectories(): void {
    this.sessionService.session$
    .pipe(
      first(),
      switchMap((session: Session | null) => {
        if (session?.tenantId) {
          this.spinnerService.showSpinner();

          return this.securityService
          .getUserDirectorySummariesForTenant(session.tenantId)
          .pipe(finalize(() => this.spinnerService.hideSpinner()));
        }

        return of([] as UserDirectorySummary[]);
      }),
      takeUntil(this.destroy$)
    )
    .subscribe({
      next: (userDirectories: UserDirectorySummary[]) => {
        this.userDirectories = userDirectories;

        let selectedId = this.userDirectoryId$.value;

        // If no selection from the restored state, infer one
        if (!selectedId) {
          if (userDirectories.length === 1) {
            selectedId = userDirectories[0].id;
          } else {
            const state = window.history.state as { userDirectoryId?: string };
            const fromNav = state?.userDirectoryId;

            if (fromNav && userDirectories.some((ud) => ud.id === fromNav)) {
              selectedId = fromNav;
            }
          }
        }

        if (selectedId && userDirectories.some((ud) => ud.id === selectedId)) {
          this.userDirectoryId$.next(selectedId);
          if (this.userDirectorySelect) {
            this.userDirectorySelect.value = selectedId;
          }
        }
      },
      error: (error: Error) => this.handleError(error, false)
    });
  }

  private loadUsers(userDirectoryId: string): Observable<Users> {
    const filter = this.tableFilter.filter?.trim().toLowerCase() || '';

    let sortBy: UserSortBy = UserSortBy.Username;
    let sortDirection = SortDirection.Ascending;

    if (this.sort.active) {
      if (this.sort.active === 'name') {
        sortBy = UserSortBy.Name;
      } else if (this.sort.active === 'preferredName') {
        sortBy = UserSortBy.PreferredName;
      }
    }

    if (this.sort.direction === 'desc') {
      sortDirection = SortDirection.Descending;
    }

    return this.dataSource.load(
      userDirectoryId,
      filter,
      sortBy,
      sortDirection,
      this.paginator.pageIndex,
      this.paginator.pageSize
    );
  }

  private loadUsersData(): void {
    const userDirectoryId = this.userDirectoryId$.value;

    if (!userDirectoryId) {
      this.dataSource.clear();
      this.userDirectoryCapabilities$.next(null);
      return;
    }

    this.spinnerService.showSpinner();

    forkJoin({
      userDirectoryCapabilities: this.securityService
      .getUserDirectoryCapabilities(userDirectoryId)
      .pipe(
        catchError((error: Error) => {
          this.handleError(error, false);
          return of(null);
        })
      ),
      users: this.loadUsers(userDirectoryId).pipe(
        catchError((error: Error) => {
          this.handleError(error, false);
          return EMPTY;
        })
      )
    })
    .pipe(
      finalize(() => this.spinnerService.hideSpinner()),
      takeUntil(this.destroy$)
    )
    .subscribe({
      next: ({ userDirectoryCapabilities }) => {
        if (userDirectoryCapabilities) {
          this.userDirectoryCapabilities$.next(userDirectoryCapabilities);
        }
      },
      error: (error: Error) => this.handleError(error, false)
    });
  }
}
