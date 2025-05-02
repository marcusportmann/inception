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
import {GroupDataSource} from '../services/group-data-source';
import {Groups} from '../services/groups';
import {SecurityService} from '../services/security.service';
import {UserDirectoryCapabilities} from '../services/user-directory-capabilities';
import {UserDirectorySummary} from '../services/user-directory-summary';

/**
 * The GroupsComponent class implements the groups component.
 *
 * @author Marcus Portmann
 */
@Component({
  templateUrl: 'groups.component.html',
  styleUrls: ['groups.component.css'],
  standalone: false
})
export class GroupsComponent extends AdminContainerView implements AfterViewInit, OnDestroy {
  dataSource: GroupDataSource;

  displayedColumns = ['name', 'actions'];

  @HostBinding('class') hostClass = 'flex flex-column flex-fill';

  @ViewChild(MatPaginator, {static: true}) paginator!: MatPaginator;

  @ViewChild(MatSort, {static: true}) sort!: MatSort;

  @ViewChild(TableFilterComponent, {static: true}) tableFilter!: TableFilterComponent;

  userDirectories: UserDirectorySummary[] = [];

  userDirectoryCapabilities$ = new BehaviorSubject<UserDirectoryCapabilities | null>(null);

  userDirectoryId$ = new BehaviorSubject<string>('');

  @ViewChild('userDirectorySelect', {static: true}) userDirectorySelect!: MatSelect;

  private destroy$ = new Subject<void>();

  constructor(private router: Router, private activatedRoute: ActivatedRoute,
              private securityService: SecurityService, private sessionService: SessionService,
              private dialogService: DialogService, private spinnerService: SpinnerService) {
    super();
    this.dataSource = new GroupDataSource(this.securityService);
  }

  get enableActionsMenu$(): Observable<boolean> {
    return this.userDirectoryCapabilities$.pipe(map(
      (userDirectoryCapabilities) => userDirectoryCapabilities?.supportsGroupAdministration ?? false));
  }

  get enableNewButton$(): Observable<boolean> {
    return this.enableActionsMenu$;
  }

  get title(): string {
    return $localize`:@@security_groups_title:Groups`;
  }

  deleteGroup(groupName: string): void {
    const message = $localize`:@@security_groups_confirm_delete_group:Are you sure you want to delete the group?`;
    this.confirmAndProcessAction(message,
      () => this.securityService.deleteGroup(this.userDirectoryId$.value, groupName));
  }

  editGroup(groupName: string): void {
    // noinspection JSIgnoredPromiseFromCall
    this.router.navigate([`${this.userDirectoryId$.value}/${encodeURIComponent(groupName)}/edit`],
      {relativeTo: this.activatedRoute});
  }

  groupMembers(groupName: string): void {
    this.router.navigate(
      [`${this.userDirectoryId$.value}/${encodeURIComponent(groupName)}/members`],
      {relativeTo: this.activatedRoute});
  }

  groupRoles(groupName: string): void {
    this.router.navigate([`${this.userDirectoryId$.value}/${encodeURIComponent(groupName)}/roles`],
      {relativeTo: this.activatedRoute});
  }

  loadGroups(): Observable<Groups> {
    const filter = this.tableFilter.filter?.trim().toLowerCase() || '';
    const sortDirection = this.sort.direction === 'asc' ? SortDirection.Ascending :
      SortDirection.Descending;

    if (this.userDirectoryId$.value) {
      return this.dataSource
      .load(this.userDirectoryId$.value, filter, sortDirection, this.paginator.pageIndex,
        this.paginator.pageSize)
      .pipe(catchError((error) => {
        return this.handleError(error);
      }));
    } else {
      this.dataSource.clear();
      return of();
    }
  }

  newGroup(): void {
    this.router.navigate([`${this.userDirectoryId$.value}/new`], {
      relativeTo: this.activatedRoute,
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

  private confirmAndProcessAction(confirmationMessage: string,
                                  action: () => Observable<void | any>): void {
    const dialogRef = this.dialogService.showConfirmationDialog({message: confirmationMessage});

    dialogRef
    .afterClosed()
    .pipe(first(), filter((confirmed) => confirmed === true), switchMap(() => {
      this.spinnerService.showSpinner();
      return action().pipe(catchError((error) => this.handleError(error)),
        tap(() => this.resetTable()),
        switchMap(() => this.loadGroups().pipe(catchError((error) => this.handleError(error)))),
        finalize(() => this.spinnerService.hideSpinner()));
    }), takeUntil(this.destroy$))
    .subscribe();
  }

  private handleError(error: Error): Observable<never> {
    if (error instanceof AccessDeniedError || error instanceof InvalidArgumentError || error instanceof ServiceUnavailableError) {
      this.router.navigateByUrl('/error/send-error-report', {state: {error}});
    } else {
      this.dialogService.showErrorDialog(error);
    }
    return throwError(() => error);
  }

  private initializeDataLoaders(): void {
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
          groups: this.loadGroups().pipe(catchError((error) => this.handleError(error))),
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
        this.loadGroups()
        .pipe(catchError((error) => this.handleError(error)),
          finalize(() => this.spinnerService.hideSpinner()))
        .subscribe();
      }
    });
  }

  private loadData(): void {
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

  private resetTable(): void {
    this.tableFilter.reset(false);
    this.paginator.pageIndex = 0;
    this.sort.active = '';
    this.sort.direction = 'asc' as SortDirection;
  }
}
