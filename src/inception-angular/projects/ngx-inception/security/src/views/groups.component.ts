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
  AfterViewInit, ChangeDetectionStrategy, Component, HostBinding, inject, ViewChild
} from '@angular/core';
import { MatPaginator } from '@angular/material/paginator';
import { MatSelect, MatSelectChange } from '@angular/material/select';
import { MatSort } from '@angular/material/sort';
import {
  CoreModule, HasAuthorityDirective, Session, SessionService, SortDirection, StatefulListView,
  TableFilterComponent
} from 'ngx-inception/core';
import { BehaviorSubject, EMPTY, forkJoin, Observable, of } from 'rxjs';
import { catchError, finalize, first, map, switchMap, takeUntil, tap } from 'rxjs/operators';
import { GroupDataSource } from '../services/group-data-source';
import { Groups } from '../services/groups';
import { SecurityService } from '../services/security.service';
import { UserDirectoryCapabilities } from '../services/user-directory-capabilities';
import { UserDirectorySummary } from '../services/user-directory-summary';

interface GroupsListExtras {
  userDirectoryId: string | null;
}

/**
 * The GroupsComponent class implements the Groups component.
 *
 * @author Marcus Portmann
 */
@Component({
  selector: 'inception-security-groups',
  standalone: true,
  imports: [CoreModule, TableFilterComponent, HasAuthorityDirective],
  templateUrl: 'groups.component.html',
  styleUrls: ['groups.component.css'],
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class GroupsComponent extends StatefulListView<GroupsListExtras> implements AfterViewInit {
  readonly dataSource: GroupDataSource;

  readonly defaultSortActive = 'name';

  readonly displayedColumns = ['name', 'actions'] as const;

  @HostBinding('class') hostClass = 'flex flex-column flex-fill';

  readonly listStateKey = 'security.groups';

  @ViewChild(MatPaginator, { static: true }) override paginator!: MatPaginator;

  @ViewChild(MatSort, { static: true }) override sort!: MatSort;

  @ViewChild(TableFilterComponent, { static: true })
  override tableFilter!: TableFilterComponent;

  readonly title = $localize`:@@security_groups_title:Groups`;

  userDirectories: UserDirectorySummary[] = [];

  userDirectoryCapabilities$ = new BehaviorSubject<UserDirectoryCapabilities | null>(null);

  userDirectoryId$ = new BehaviorSubject<string | null>(null);

  @ViewChild('userDirectorySelect', { static: true })
  userDirectorySelect!: MatSelect;

  /** Whether this navigation requested a state reset (from the sidebar). */
  private readonly resetStateRequested: boolean;

  private readonly securityService = inject(SecurityService);

  private readonly sessionService = inject(SessionService);

  constructor() {
    super();

    const nav = this.router.currentNavigation();

    this.resetStateRequested = !!nav?.extras.state?.['resetState'];

    this.dataSource = new GroupDataSource(this.securityService);
  }

  get enableActionsMenu$(): Observable<boolean> {
    return this.userDirectoryCapabilities$.pipe(
      map((caps) => caps?.supportsGroupAdministration ?? false)
    );
  }

  get enableNewButton$(): Observable<boolean> {
    return this.enableActionsMenu$;
  }

  deleteGroup(groupName: string): void {
    const userDirectoryId = this.userDirectoryId$.value;
    if (!userDirectoryId) {
      return;
    }

    const message = $localize`:@@security_groups_confirm_delete_group:Are you sure you want to delete the group?`;

    this.confirmAndProcessAction(
      message,
      () => this.securityService.deleteGroup(userDirectoryId, groupName),
      () => this.loadGroups(userDirectoryId)
    );
  }

  editGroup(groupName: string): void {
    const userDirectoryId = this.userDirectoryId$.value;
    if (!userDirectoryId) {
      return;
    }

    this.saveState();

    void this.router.navigate([`${userDirectoryId}/${groupName}/edit`], {
      relativeTo: this.activatedRoute
    });
  }

  groupMembers(groupName: string): void {
    const userDirectoryId = this.userDirectoryId$.value;
    if (!userDirectoryId) {
      return;
    }

    this.saveState();

    this.listStateService.clear('security.group-members');

    void this.router.navigate([`${userDirectoryId}/${groupName}/members`], {
      relativeTo: this.activatedRoute
    });
  }

  groupRoles(groupName: string): void {
    const userDirectoryId = this.userDirectoryId$.value;
    if (!userDirectoryId) {
      return;
    }

    this.saveState();

    void this.router.navigate([`${userDirectoryId}/${groupName}/roles`], {
      relativeTo: this.activatedRoute
    });
  }

  newGroup(): void {
    const userDirectoryId = this.userDirectoryId$.value;
    if (!userDirectoryId) {
      return;
    }

    this.saveState();

    void this.router.navigate([`${userDirectoryId}/new`], {
      relativeTo: this.activatedRoute
    });
  }

  ngAfterViewInit(): void {
    const directorySelection$ = this.userDirectorySelect.selectionChange.pipe(
      tap((change: MatSelectChange) => {
        this.userDirectoryId$.next(change.value);
      })
    );

    this.initializeStatefulList(this.resetStateRequested, () => this.loadGroupsData(), [
      {
        observable: directorySelection$,
        resetPage: true
      }
    ]);

    this.loadUserDirectories();

    this.changeDetectorRef.detectChanges();
  }

  protected override getExtrasState(): GroupsListExtras | undefined {
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

  protected override restoreExtrasState(extras: GroupsListExtras | undefined): void {
    if (extras?.userDirectoryId) {
      this.userDirectoryId$.next(extras.userDirectoryId);

      if (this.userDirectorySelect) {
        this.userDirectorySelect.value = extras.userDirectoryId;
      }
    }
  }

  private loadGroups(userDirectoryId: string): Observable<Groups> {
    const filter = this.tableFilter.filter?.trim().toLowerCase() || '';
    const sortDirection =
      this.sort.direction === 'asc' ? SortDirection.Ascending : SortDirection.Descending;

    return this.dataSource.load(
      userDirectoryId,
      filter,
      sortDirection,
      this.paginator.pageIndex,
      this.paginator.pageSize
    );
  }

  private loadGroupsData(): void {
    const userDirectoryId = this.userDirectoryId$.value;

    if (!userDirectoryId) {
      this.dataSource.clear();
      this.userDirectoryCapabilities$.next(null);
      this.changeDetectorRef.markForCheck();
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

      groups: this.loadGroups(userDirectoryId).pipe(
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
        next: ({ userDirectoryCapabilities, groups }) => {
          if (userDirectoryCapabilities) {
            this.userDirectoryCapabilities$.next(userDirectoryCapabilities);
          }

          // Sync paginator to what the server actually returned/corrected
          this.restoringState = true;
          try {
            if (groups && this.paginator) {
              this.paginator.pageIndex = groups.pageIndex;
              this.paginator.pageSize = groups.pageSize;
              this.saveState();
            }
          } finally {
            this.restoringState = false;
          }

          this.changeDetectorRef.markForCheck();
        },
        error: (error: Error) => this.handleError(error, false)
      });
  }

  private loadUserDirectories(): void {
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
          } else {
            return of([] as UserDirectorySummary[]);
          }
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

          this.changeDetectorRef.markForCheck();
        },
        error: (error: Error) => this.handleError(error, false)
      });
  }
}
