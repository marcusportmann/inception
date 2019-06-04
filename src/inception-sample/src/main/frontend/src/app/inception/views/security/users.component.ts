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
import {MatDialogRef, MatPaginator, MatSort} from '@angular/material';
import {finalize, first, tap} from 'rxjs/operators';
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
import {SortDirection} from "../../services/security/sort-direction";
import {merge, Subscription} from "rxjs";
import {TableFilter} from "../../components/controls";
import {UserDatasource} from "../../services/security/users.datasource";
import {SessionService} from "../../services/session/session.service";

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
  }
})
export class UsersComponent implements AfterViewInit, OnInit, OnDestroy {

  private subscriptions: Subscription = new Subscription();

  dataSource: UserDatasource;

  displayedColumns: string[] = ['username', 'actions'];

  @ViewChild(MatPaginator) paginator: MatPaginator;

  @ViewChild(MatSort) sort: MatSort;

  @ViewChild(TableFilter) tableFilter: TableFilter;

  constructor(private router: Router, private activatedRoute: ActivatedRoute, private i18n: I18n,
              private securityService: SecurityService, private sessionService: SessionService,
              private dialogService: DialogService, private spinnerService: SpinnerService) {

  }

  deleteUser(userId: string): void {
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

  editUser(userId: string): void {
    // noinspection JSIgnoredPromiseFromCall
    this.router.navigate([userId], {relativeTo: this.activatedRoute});
  }

  loadUsers(): void {

    let filter: string = '';

    if (!!this.tableFilter.filter) {
      filter = this.tableFilter.filter;
      filter = filter.trim();
      filter = filter.toLowerCase();
    }

    this.dataSource.load(filter,
      this.sort.direction == 'asc' ? SortDirection.Ascending : SortDirection.Descending,
      this.paginator.pageIndex,
      this.paginator.pageSize);
  }

  newUser(): void {
    // noinspection JSIgnoredPromiseFromCall
    this.router.navigate(['new-user'], {relativeTo: this.activatedRoute});
  }

  ngAfterViewInit(): void {
    this.loadUsers();

    this.subscriptions.add(this.sort.sortChange.subscribe(() => this.paginator.pageIndex = 0));

    this.subscriptions.add(this.tableFilter.changed.subscribe(() => this.paginator.pageIndex = 0));

    this.subscriptions.add(merge(this.sort.sortChange, this.tableFilter.changed, this.paginator.page)
      .pipe(tap(() => {
        this.loadUsers();
      })).subscribe());
  }

  ngOnInit(): void {
    this.dataSource = new UserDatasource(this.sessionService, this.securityService);

    this.subscriptions.add(this.dataSource.loading.subscribe((next: boolean) => {
      if (next) {
        this.spinnerService.showSpinner()
      } else {
        this.spinnerService.hideSpinner();
      }
    }, (error: Error) => {
      if ((error instanceof SecurityServiceError) || (error instanceof AccessDeniedError) ||
        (error instanceof SystemUnavailableError)) {
        // noinspection JSIgnoredPromiseFromCall
        this.router.navigateByUrl('/error/send-error-report', {state: {error: error}});
      } else {
        this.dialogService.showErrorDialog(error);
      }
    }));
  }

  ngOnDestroy(): void {
    this.subscriptions.unsubscribe();
  }
}

