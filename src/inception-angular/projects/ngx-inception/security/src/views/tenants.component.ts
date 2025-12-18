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
import { MatSort } from '@angular/material/sort';
import {
  CoreModule,
  Error,
  SortDirection,
  StatefulListView, TableFilterComponent
} from 'ngx-inception/core';
import { Observable } from 'rxjs';
import { finalize, takeUntil } from 'rxjs/operators';
import { SecurityService } from '../services/security.service';
import { TenantDataSource } from '../services/tenant-data-source';

@Component({
  selector: 'inception-security-tenants',
  standalone: true,
  imports: [CoreModule, TableFilterComponent],
  templateUrl: 'tenants.component.html',
  styleUrls: ['tenants.component.css']
})
export class TenantsComponent extends StatefulListView implements AfterViewInit {
  private securityService = inject(SecurityService);

  readonly dataSource: TenantDataSource;

  displayedColumns = ['name', 'actions'];

  @HostBinding('class') hostClass = 'flex flex-column flex-fill';

  readonly listKey = 'security.tenants';

  @ViewChild(MatPaginator, { static: true }) override paginator!: MatPaginator;

  @ViewChild(MatSort, { static: true }) override sort!: MatSort;

  @ViewChild(TableFilterComponent, { static: true })
  override tableFilter!: TableFilterComponent;

  readonly title = $localize`:@@security_tenants_title:Tenants`;

  private readonly changeDetectorRef = inject(ChangeDetectorRef);

  /** Whether this navigation requested a state reset (from the sidebar). */
  private readonly resetStateRequested: boolean;

  constructor() {
    super();

    const nav = this.router.getCurrentNavigation();
    this.resetStateRequested = !!nav?.extras.state?.['resetState'];

    this.dataSource = new TenantDataSource(this.securityService);
  }

  deleteTenant(tenantId: string): void {
    this.confirmAndProcessAction(
      $localize`:@@security_tenants_confirm_delete_tenant:Are you sure you want to delete the tenant?`,
      () => this.securityService.deleteTenant(tenantId),
      () => this.loadTenants()
    );
  }

  editTenant(tenantId: string): void {
    this.saveState();

    // noinspection JSIgnoredPromiseFromCall
    this.router.navigate([encodeURIComponent(tenantId) + '/edit'], {
      relativeTo: this.activatedRoute
    });
  }

  newTenant(): void {
    this.saveState();

    // noinspection JSIgnoredPromiseFromCall
    this.router.navigate(['new'], { relativeTo: this.activatedRoute });
  }

  ngAfterViewInit(): void {
    this.initializeStatefulList(this.resetStateRequested, () => this.loadData());

    // Stabilize view after paginator/sort mutations
    this.changeDetectorRef.detectChanges();
  }

  tenantUserDirectories(tenantId: string): void {
    this.saveState();

    // noinspection JSIgnoredPromiseFromCall
    this.router.navigate([encodeURIComponent(tenantId) + '/user-directories'], {
      relativeTo: this.activatedRoute
    });
  }

  private loadData(): void {
    this.spinnerService.showSpinner();

    this.loadTenants()
      .pipe(
        finalize(() => this.spinnerService.hideSpinner()),
        takeUntil(this.destroy$)
      )
      .subscribe({
        next: () => {
          // Load complete
        },
        error: (error: Error) => this.handleError(error, false)
      });
  }

  private loadTenants(): Observable<unknown> {
    const filterValue = this.tableFilter.filter?.trim().toLowerCase() || '';

    const sortDirection =
      this.sort.direction === 'asc' ? SortDirection.Ascending : SortDirection.Descending;

    return this.dataSource.load(
      filterValue,
      sortDirection,
      this.paginator.pageIndex,
      this.paginator.pageSize
    );
  }
}
