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
import {OrganizationDatasource} from '../../services/security/organization.datasource';
import {SortDirection} from "../../services/security/sort-direction";
import {merge, Subscription} from "rxjs";
import {TableFilter} from "../../components/controls";
import {AdminContainerView} from "../../components/layout/admin-container-view";

/**
 * The OrganizationsComponent class implements the organizations component.
 *
 * @author Marcus Portmann
 */
@Component({
  templateUrl: 'organizations.component.html',
  styleUrls: ['organizations.component.css'],
  host: {
    'class': 'flex flex-column flex-fill',
  }
})
export class OrganizationsComponent extends AdminContainerView implements AfterViewInit, OnDestroy, OnInit {

  private subscriptions: Subscription = new Subscription();

  dataSource: OrganizationDatasource;

  displayedColumns: string[] = ['name', 'actions'];

  @ViewChild(MatPaginator, {static: true}) paginator: MatPaginator;

  @ViewChild(MatSort, {static: true}) sort: MatSort;

  @ViewChild(TableFilter, {static: true}) tableFilter: TableFilter;

  constructor(private router: Router, private activatedRoute: ActivatedRoute, private i18n: I18n,
              private securityService: SecurityService, private dialogService: DialogService,
              private spinnerService: SpinnerService) {
    super();
  }

  deleteOrganization(organizationId: string): void {
    const dialogRef: MatDialogRef<ConfirmationDialogComponent, boolean> = this.dialogService.showConfirmationDialog(
      {
        message: this.i18n({
          id: '@@organizations_component_confirm_delete_organization',
          value: 'Are you sure you want to delete the organization?'
        })
      });

    dialogRef.afterClosed()
      .pipe(first())
      .subscribe((confirmation: boolean) => {
        if (confirmation === true) {
          this.spinnerService.showSpinner();

          this.securityService.deleteOrganization(organizationId)
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
  }

  editOrganization(organizationId: string): void {
    // noinspection JSIgnoredPromiseFromCall
    this.router.navigate([organizationId], {relativeTo: this.activatedRoute});
  }

  loadOrganizations(): void {
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

  newOrganization(): void {
    // noinspection JSIgnoredPromiseFromCall
    this.router.navigate(['new-organization'], {relativeTo: this.activatedRoute});
  }

  ngAfterViewInit(): void {
    this.subscriptions.add(this.sort.sortChange.subscribe(() => this.paginator.pageIndex = 0));

    this.subscriptions.add(this.tableFilter.changed.subscribe(() => this.paginator.pageIndex = 0));

    this.subscriptions.add(
      merge(this.sort.sortChange, this.tableFilter.changed, this.paginator.page)
        .pipe(tap(() => {
          this.loadOrganizations();
        })).subscribe());

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

    this.loadOrganizations();
  }

  ngOnDestroy(): void {
    this.subscriptions.unsubscribe();
  }

  ngOnInit(): void {
    this.dataSource = new OrganizationDatasource(this.securityService);
  }
}

