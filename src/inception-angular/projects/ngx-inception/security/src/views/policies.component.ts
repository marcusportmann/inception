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
import { MatSort } from '@angular/material/sort';
import {
  AdminContainerView, CoreModule, SortDirection, TableFilterComponent
} from 'ngx-inception/core';
import { EMPTY, merge, Observable, Subject, tap } from 'rxjs';
import {
  catchError, debounceTime, filter, finalize, first, switchMap, takeUntil
} from 'rxjs/operators';
import { PolicySummaries } from '../services/policy-summaries';
import { PolicySummaryDataSource } from '../services/policy-summary-data-source';
import { PolicyType } from '../services/policy-type';
import { SecurityService } from '../services/security.service';

/**
 * The PolicesComponent class implements the policies component.
 *
 * @author Marcus Portmann
 */
@Component({
  selector: 'inception-security-policies',
  standalone: true,
  imports: [CoreModule, TableFilterComponent],
  templateUrl: 'policies.component.html',
  styleUrls: ['policies.component.css']
})
export class PoliciesComponent extends AdminContainerView implements AfterViewInit, OnDestroy {
  dataSource: PolicySummaryDataSource;

  displayedColumns = ['id', 'version', 'name', 'type', 'actions'];

  @HostBinding('class') hostClass = 'flex flex-column flex-fill';

  @ViewChild(MatPaginator, { static: true }) paginator!: MatPaginator;

  @ViewChild(MatSort, { static: true }) sort!: MatSort;

  @ViewChild(TableFilterComponent, { static: true })
  tableFilter!: TableFilterComponent;

  readonly title = $localize`:@@security_policies_title:Policies`;

  protected readonly PolicyType = PolicyType;

  private destroy$ = new Subject<void>();

  constructor(private securityService: SecurityService) {
    super();

    this.dataSource = new PolicySummaryDataSource(this.securityService);
  }

  deletePolicy(policyId: string): void {
    this.confirmAndProcessAction(
      $localize`:@@security_policies_confirm_delete_policy:Are you sure you want to delete the policy?`,
      () => this.securityService.deletePolicy(policyId)
    );
  }

  editPolicy(policyId: string): void {
    // noinspection JSIgnoredPromiseFromCall
    this.router.navigate([encodeURIComponent(policyId)], {
      relativeTo: this.activatedRoute
    });
  }

  getPolicyTypeName(policyType: PolicyType): string {
    if (policyType == PolicyType.XACMLPolicy) {
      return 'XACML Policy';
    } else if (policyType == PolicyType.XACMLPolicySet) {
      return 'XACML Policy Set';
    } else {
      return 'Unknown';
    }
  }

  newPolicy(): void {
    // noinspection JSIgnoredPromiseFromCall
    this.router.navigate(['new'], { relativeTo: this.activatedRoute });
  }

  ngAfterViewInit(): void {
    this.initializeDataLoaders();
    this.loadData();
  }

  ngOnDestroy(): void {
    this.destroy$.next();
    this.destroy$.complete();
  }

  private confirmAndProcessAction(
    confirmationMessage: string,
    action: () => Observable<void | boolean>
  ): void {
    const dialogRef = this.dialogService.showConfirmationDialog({
      message: confirmationMessage
    });

    dialogRef
      .afterClosed()
      .pipe(
        first(),
        filter((confirmed) => confirmed === true),
        switchMap(() => {
          this.spinnerService.showSpinner();
          return action().pipe(
            catchError((error) => {
              this.handleError(error, false);
              return EMPTY;
            }),
            tap(() => this.resetTable()),
            switchMap(() =>
              this.loadPolicySummaries().pipe(
                catchError((error) => {
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
    this.sort.sortChange
      .pipe(takeUntil(this.destroy$))
      .subscribe(() => (this.paginator.pageIndex = 0));

    merge(this.sort.sortChange, this.tableFilter.changed, this.paginator.page)
      .pipe(debounceTime(200), takeUntil(this.destroy$))
      .subscribe(() => this.loadData());
  }

  private loadData(): void {
    this.spinnerService.showSpinner();
    this.loadPolicySummaries()
      .pipe(finalize(() => this.spinnerService.hideSpinner()))
      .subscribe({
        next: () => {
          // Load complete
        },
        error: (error) => this.handleError(error, false)
      });
  }

  private loadPolicySummaries(): Observable<PolicySummaries> {
    const filter = this.tableFilter.filter?.trim().toLowerCase() || '';

    let sortDirection = SortDirection.Descending;

    if (this.sort.active) {
      sortDirection =
        this.sort.direction === 'asc' ? SortDirection.Ascending : SortDirection.Descending;
    }

    return this.dataSource.load(
      filter,
      sortDirection,
      this.paginator.pageIndex,
      this.paginator.pageSize
    );
  }

  private resetTable(): void {
    this.tableFilter.reset(false);
    this.paginator.pageIndex = 0;
    this.sort.active = '';
    this.sort.direction = 'asc' as SortDirection;
  }
}
