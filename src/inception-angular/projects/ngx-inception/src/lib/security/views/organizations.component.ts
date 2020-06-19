/*
 * Copyright 2020 Marcus Portmann
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
import {finalize, first, tap} from 'rxjs/operators';
import {ActivatedRoute, Router} from '@angular/router';
import {merge, Subscription} from 'rxjs';
import {AdminContainerView} from '../../layout/components/admin-container-view';
import {OrganizationDatasource} from '../services/organization.datasource';
import {TableFilterComponent} from '../../core/components/table-filter.component';
import {SecurityService} from '../services/security.service';
import {SpinnerService} from '../../layout/services/spinner.service';
import {DialogService} from '../../dialog/services/dialog.service';
import {ConfirmationDialogComponent} from '../../dialog/components/confirmation-dialog.component';
import {SecurityServiceError} from '../services/security.service.errors';
import {AccessDeniedError} from '../../core/errors/access-denied-error';
import {SystemUnavailableError} from '../../core/errors/system-unavailable-error';
import {SortDirection} from '../services/sort-direction';
import {Error} from '../../core/errors/error';

/**
 * The OrganizationsComponent class implements the organizations component.
 *
 * @author Marcus Portmann
 */
@Component({
  templateUrl: 'organizations.component.html',
  styleUrls: ['organizations.component.css']
})
export class OrganizationsComponent extends AdminContainerView implements AfterViewInit, OnDestroy {

  @HostBinding('class') hostClass = 'flex flex-column flex-fill';

  private subscriptions: Subscription = new Subscription();

  dataSource: OrganizationDatasource;

  displayedColumns = ['name', 'actions'];

  @ViewChild(MatPaginator, {static: true}) paginator!: MatPaginator;

  @ViewChild(MatSort, {static: true}) sort!: MatSort;

  @ViewChild(TableFilterComponent, {static: true}) tableFilter!: TableFilterComponent;

  constructor(private router: Router, private activatedRoute: ActivatedRoute, private securityService: SecurityService,
              private dialogService: DialogService, private spinnerService: SpinnerService) {
    super();

    this.dataSource = new OrganizationDatasource(this.securityService);
  }

  get title(): string {
    return 'Organizations';
  }

  deleteOrganization(organizationId: string): void {
    const dialogRef: MatDialogRef<ConfirmationDialogComponent, boolean> = this.dialogService.showConfirmationDialog({
      message: 'Are you sure you want to delete the organization?'
    });

    dialogRef.afterClosed()
      .pipe(first())
      .subscribe((confirmation: boolean | undefined) => {
        if (confirmation === true) {
          this.spinnerService.showSpinner();

          this.securityService.deleteOrganization(organizationId)
            .pipe(first(), finalize(() => this.spinnerService.hideSpinner()))
            .subscribe(() => {
              this.loadOrganizations();
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

  editOrganization(organizationId: string): void {
    // noinspection JSIgnoredPromiseFromCall
    this.router.navigate([encodeURIComponent(organizationId) + '/edit'], {relativeTo: this.activatedRoute});
  }

  loadOrganizations(): void {
    let filter = '';

    if (!!this.tableFilter.filter) {
      filter = this.tableFilter.filter;
      filter = filter.trim();
      filter = filter.toLowerCase();
    }

    const sortDirection = this.sort.direction === 'asc' ? SortDirection.Ascending : SortDirection.Descending;

    this.dataSource.load(filter, sortDirection, this.paginator.pageIndex, this.paginator.pageSize);
  }

  newOrganization(): void {
    // noinspection JSIgnoredPromiseFromCall
    this.router.navigate(['new'], {relativeTo: this.activatedRoute});
  }

  ngAfterViewInit(): void {
    this.subscriptions.add(this.dataSource.loading$.subscribe((next: boolean) => {
      if (next) {
        this.spinnerService.showSpinner();
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
        this.loadOrganizations();
      })).subscribe());

    this.loadOrganizations();
  }

  ngOnDestroy(): void {
    this.subscriptions.unsubscribe();
  }

  organizationUserDirectories(organizationId: string) {
    // noinspection JSIgnoredPromiseFromCall
    this.router.navigate([encodeURIComponent(organizationId) + '/user-directories'], {relativeTo: this.activatedRoute});
  }
}

