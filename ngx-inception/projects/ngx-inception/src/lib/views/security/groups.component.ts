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

import {AfterViewInit, Component, HostBinding, OnDestroy, ViewChild} from '@angular/core';
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
import {BehaviorSubject, merge, Observable, ReplaySubject, Subject, Subscription} from 'rxjs';
import {TableFilterComponent} from '../../components/controls';
import {SessionService} from '../../services/session/session.service';
import {Session} from '../../services/session/session';
import {UserDirectorySummary} from '../../services/security/user-directory-summary';
import {MatSelect, MatSelectChange} from '@angular/material';
import {AdminContainerView} from '../../components/layout/admin-container-view';
import {GroupDatasource} from '../../services/security/group.datasource';
import {UserDirectoryCapabilities} from '../../services/security/user-directory-capabilities';

/**
 * The GroupsComponent class implements the groups component.
 *
 * @author Marcus Portmann
 */
@Component({
  templateUrl: 'groups.component.html',
  styleUrls: ['groups.component.css']
})
export class GroupsComponent extends AdminContainerView implements AfterViewInit, OnDestroy {

  @HostBinding('class') hostClass = 'flex flex-column flex-fill';

  private subscriptions: Subscription = new Subscription();

  dataSource: GroupDatasource;

  displayedColumns = ['name', 'actions'];

  @ViewChild(MatPaginator, {static: true}) paginator!: MatPaginator;

  @ViewChild(MatSort, {static: true}) sort!: MatSort;

  @ViewChild(TableFilterComponent, {static: true}) tableFilter!: TableFilterComponent;

  userDirectoryCapabilities$: Subject<UserDirectoryCapabilities> = new ReplaySubject<UserDirectoryCapabilities>();

  userDirectoryId$: BehaviorSubject<string> = new BehaviorSubject<string>('');

  userDirectories: UserDirectorySummary[] = [];

  @ViewChild('userDirectorySelect', {static: true}) userDirectorySelect!: MatSelect;

  constructor(private router: Router, private activatedRoute: ActivatedRoute, private i18n: I18n, private securityService: SecurityService,
              private sessionService: SessionService, private dialogService: DialogService, private spinnerService: SpinnerService) {
    super();

    this.dataSource = new GroupDatasource(this.sessionService, this.securityService);
  }

  get enableActionsMenu$(): Observable<boolean> {
    return this.userDirectoryCapabilities$.pipe(map((userDirectoryCapabilities: UserDirectoryCapabilities) => {
      return userDirectoryCapabilities.supportsGroupAdministration;
    }));
  }

  get enableNewButton$(): Observable<boolean> {
    return this.userDirectoryCapabilities$.pipe(map((userDirectoryCapabilities: UserDirectoryCapabilities) => {
      return userDirectoryCapabilities.supportsGroupAdministration;
    }));
  }

  get title(): string {
    return this.i18n({
      id: '@@security_groups_component_title',
      value: 'Groups'
    });
  }

  // noinspection JSUnusedLocalSymbols
  deleteGroup(groupName: string): void {
    // noinspection JSUnusedLocalSymbols
    const dialogRef: MatDialogRef<ConfirmationDialogComponent, boolean> = this.dialogService.showConfirmationDialog({
      message: this.i18n({
        id: '@@security_groups_component_confirm_delete_group',
        value: 'Are you sure you want to delete the group?'
      })
    });

    dialogRef.afterClosed()
      .pipe(first())
      .subscribe((confirmation: boolean | undefined) => {
        if (confirmation === true) {
          this.spinnerService.showSpinner();

          this.securityService.deleteGroup(this.userDirectoryId$.value, groupName)
            .pipe(first(), finalize(() => this.spinnerService.hideSpinner()))
            .subscribe(() => {
              this.tableFilter.reset(false);

              this.paginator.pageIndex = 0;

              this.sort.active = '';
              this.sort.direction = 'asc' as SortDirection;

              this.loadGroups();
            }, (error: Error) => {
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

  editGroup(groupName: string): void {
    // noinspection JSIgnoredPromiseFromCall
    this.router.navigate([this.userDirectoryId$.value + '/' + encodeURIComponent(groupName) + '/edit'], {relativeTo: this.activatedRoute});
  }

  groupMembers(groupName: string): void {
    // noinspection JSIgnoredPromiseFromCall
    this.router.navigate([this.userDirectoryId$.value + '/' + encodeURIComponent(groupName) + '/members'],
      {relativeTo: this.activatedRoute});
  }

  groupRoles(groupName: string): void {
    // noinspection JSIgnoredPromiseFromCall
    this.router.navigate([this.userDirectoryId$.value + '/' + encodeURIComponent(groupName) + '/roles'], {relativeTo: this.activatedRoute});
  }

  loadGroups(): void {
    let filter = '';

    if (!!this.tableFilter.filter) {
      filter = this.tableFilter.filter;
      filter = filter.trim();
      filter = filter.toLowerCase();
    }

    const sortDirection = this.sort.direction === 'asc' ? SortDirection.Ascending : SortDirection.Descending;

    if (!!this.userDirectoryId$.value) {
      this.dataSource.load(this.userDirectoryId$.value, filter, sortDirection, this.paginator.pageIndex, this.paginator.pageSize);
    } else {
      this.dataSource.clear();
    }
  }

  newGroup(): void {
    // noinspection JSIgnoredPromiseFromCall
    this.router.navigate([this.userDirectoryId$.value + '/new'], {relativeTo: this.activatedRoute});
  }

  ngAfterViewInit(): void {
    this.subscriptions.add(this.userDirectoryId$.subscribe((userDirectoryId: string) => {
      if (!!userDirectoryId) {
        this.tableFilter.reset(false);

        this.paginator.pageIndex = 0;

        this.sort.active = 'name';
        this.sort.direction = 'asc' as SortDirection;

        this.spinnerService.showSpinner();

        this.securityService.getUserDirectoryCapabilities(userDirectoryId)
          .pipe(first(), finalize(() => this.spinnerService.hideSpinner()))
          .subscribe((userDirectoryCapabilities: UserDirectoryCapabilities) => {
            this.userDirectoryCapabilities$.next(userDirectoryCapabilities);

            this.loadGroups();
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
    }));

    this.subscriptions.add(this.dataSource.loading$.subscribe((next: boolean) => {
      if (next) {
        this.spinnerService.showSpinner();
      } else {
        this.spinnerService.hideSpinner();
      }
    }, (error: Error) => {
      // noinspection SuspiciousTypeOfGuard
      if ((error instanceof SecurityServiceError) || (error instanceof AccessDeniedError) || (error instanceof SystemUnavailableError)) {
        // noinspection JSIgnoredPromiseFromCall
        this.router.navigateByUrl('/error/send-error-report', {state: {error}});
      } else {
        this.dialogService.showErrorDialog(error);
      }
    }));

    this.subscriptions.add(this.userDirectorySelect.selectionChange.subscribe((matSelectChange: MatSelectChange) => {
      this.userDirectoryId$.next(matSelectChange.value);
    }));

    this.subscriptions.add(this.sort.sortChange.subscribe(() => {
      if (this.paginator) {
        this.paginator.pageIndex = 0;
      }
    }));

    this.subscriptions.add(this.tableFilter.changed.subscribe(() => {
      if (this.paginator) {
        this.paginator.pageIndex = 0;
      }
    }));

    this.subscriptions.add(merge(this.sort.sortChange, this.tableFilter.changed, this.paginator.page)
      .pipe(tap(() => {
        this.loadGroups();
      })).subscribe());

    this.sessionService.session$.pipe(first()).subscribe((session: Session | null) => {
      if (session && session.organization) {
        this.spinnerService.showSpinner();

        this.securityService.getUserDirectorySummariesForOrganization(session.organization.id)
          .pipe(first(), finalize(() => this.spinnerService.hideSpinner()))
          .subscribe((userDirectories: UserDirectorySummary[]) => {
            this.userDirectories = userDirectories;

            // If we only have one user directory then select it
            if (this.userDirectories.length === 1) {
              this.userDirectoryId$.next(this.userDirectories[0].id);
            } else {
              /*
               * If a user directory ID has been passed in then select the appropriate user
               * directory if it exists.
               */
              this.activatedRoute.paramMap
                .pipe(first(), map(() => window.history.state))
                .subscribe((state) => {
                  if (state.userDirectoryId) {
                    for (const userDirectory of userDirectories) {
                      if (userDirectory.id === state.userDirectoryId) {
                        this.userDirectorySelect.value = userDirectory.id;
                        this.userDirectoryId$.next(userDirectory.id);
                        break;
                      }
                    }
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
}
