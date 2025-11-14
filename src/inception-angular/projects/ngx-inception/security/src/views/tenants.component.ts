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
import { MatDialogRef } from '@angular/material/dialog';
import { MatPaginator } from '@angular/material/paginator';
import { MatSort } from '@angular/material/sort';
import {
  AdminContainerView, ConfirmationDialogComponent, CoreModule, Error, SortDirection,
  TableFilterComponent
} from 'ngx-inception/core';
import { merge, Subscription } from 'rxjs';
import { finalize, first } from 'rxjs/operators';
import { SecurityService } from '../services/security.service';
import { TenantDataSource } from '../services/tenant-data-source';

/**
 * The TenantsComponent class implements the tenants component.
 *
 * @author Marcus Portmann
 */
@Component({
  selector: 'inception-security-tenants',
  standalone: true,
  imports: [CoreModule, TableFilterComponent],
  templateUrl: 'tenants.component.html',
  styleUrls: ['tenants.component.css']
})
export class TenantsComponent extends AdminContainerView implements AfterViewInit, OnDestroy {
  dataSource: TenantDataSource;

  displayedColumns = ['name', 'actions'];

  @HostBinding('class') hostClass = 'flex flex-column flex-fill';

  @ViewChild(MatPaginator, { static: true }) paginator!: MatPaginator;

  @ViewChild(MatSort, { static: true }) sort!: MatSort;

  @ViewChild(TableFilterComponent, { static: true })
  tableFilter!: TableFilterComponent;

  readonly title = $localize`:@@security_tenants_title:Tenants`;

  private subscriptions: Subscription = new Subscription();

  constructor(private securityService: SecurityService) {
    super();

    this.dataSource = new TenantDataSource(this.securityService);
  }

  deleteTenant(tenantId: string): void {
    const dialogRef: MatDialogRef<ConfirmationDialogComponent, boolean> =
      this.dialogService.showConfirmationDialog({
        message: $localize`:@@security_tenants_confirm_delete_tenant:Are you sure you want to delete the tenant?`
      });

    dialogRef
      .afterClosed()
      .pipe(first())
      .subscribe({
        next: (confirmation: boolean | undefined) => {
          if (confirmation !== true) {
            return;
          }

          this.spinnerService.showSpinner();

          this.securityService
            .deleteTenant(tenantId)
            .pipe(
              first(),
              finalize(() => this.spinnerService.hideSpinner())
            )
            .subscribe({
              next: () => {
                this.loadTenants();
              },
              error: (error: Error) => this.handleError(error, false)
            });
        }
      });
  }

  editTenant(tenantId: string): void {
    // noinspection JSIgnoredPromiseFromCall
    this.router.navigate([encodeURIComponent(tenantId) + '/edit'], {
      relativeTo: this.activatedRoute
    });
  }

  loadTenants(): void {
    let filter = '';

    if (this.tableFilter.filter) {
      filter = this.tableFilter.filter;
      filter = filter.trim();
      filter = filter.toLowerCase();
    }

    const sortDirection =
      this.sort.direction === 'asc' ? SortDirection.Ascending : SortDirection.Descending;

    this.dataSource.load(filter, sortDirection, this.paginator.pageIndex, this.paginator.pageSize);
  }

  newTenant(): void {
    // noinspection JSIgnoredPromiseFromCall
    this.router.navigate(['new'], { relativeTo: this.activatedRoute });
  }

  ngAfterViewInit(): void {
    this.subscriptions.add(
      this.dataSource.loading$.subscribe({
        next: (next: boolean) => {
          if (next) {
            this.spinnerService.showSpinner();
          } else {
            this.spinnerService.hideSpinner();
          }
        },
        error: (error: Error) => this.handleError(error, false)
      })
    );

    this.subscriptions.add(
      this.sort.sortChange.subscribe(() => {
        if (this.paginator) {
          this.paginator.pageIndex = 0;
        }
      })
    );

    this.subscriptions.add(
      this.tableFilter.changed.subscribe(() => {
        if (this.paginator) {
          this.paginator.pageIndex = 0;
        }
      })
    );

    this.subscriptions.add(
      merge(this.sort.sortChange, this.tableFilter.changed, this.paginator.page).subscribe({
        next: () => {
          this.loadTenants();
        },
        error: (error: Error) => this.handleError(error, false)
      })
    );

    this.loadTenants();
  }

  ngOnDestroy(): void {
    this.subscriptions.unsubscribe();
  }

  tenantUserDirectories(tenantId: string) {
    // noinspection JSIgnoredPromiseFromCall
    this.router.navigate([encodeURIComponent(tenantId) + '/user-directories'], {
      relativeTo: this.activatedRoute
    });
  }
}
