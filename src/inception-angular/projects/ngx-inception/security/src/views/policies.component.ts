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
import {MatSort} from '@angular/material/sort';
import {ActivatedRoute, Router} from '@angular/router';
import {
  AccessDeniedError, AdminContainerView, DialogService, Error, InvalidArgumentError,
  ServiceUnavailableError, SortDirection, SpinnerService, TableFilterComponent
} from 'ngx-inception/core';
import {merge, Observable, Subject, tap, throwError} from 'rxjs';
import {
  catchError, debounceTime, filter, finalize, first, switchMap, takeUntil
} from 'rxjs/operators';
import {PolicySummaries} from '../services/policy-summaries';
import {PolicySummaryDataSource} from '../services/policy-summary-data-source';
import {PolicyType} from '../services/policy-type';
import {SecurityService} from '../services/security.service';

/**
 * The PolicesComponent class implements the policies component.
 *
 * @author Marcus Portmann
 */
@Component({
  templateUrl: 'policies.component.html',
  styleUrls: ['policies.component.css']
})
export class PoliciesComponent extends AdminContainerView implements AfterViewInit, OnDestroy {
  dataSource: PolicySummaryDataSource;

  displayedColumns = ['id', 'version', 'name', 'type', 'actions'];

  @HostBinding('class') hostClass = 'flex flex-column flex-fill';

  @ViewChild(MatPaginator, {static: true}) paginator!: MatPaginator;

  @ViewChild(MatSort, {static: true}) sort!: MatSort;

  @ViewChild(TableFilterComponent, {static: true}) tableFilter!: TableFilterComponent;

  protected readonly PolicyType = PolicyType;

  private destroy$ = new Subject<void>();

  constructor(private router: Router, private activatedRoute: ActivatedRoute,
              private securityService: SecurityService, private dialogService: DialogService,
              private spinnerService: SpinnerService) {
    super();

    this.dataSource = new PolicySummaryDataSource(this.securityService);
  }

  get title(): string {
    return $localize`:@@security_policies_title:Policies`
  }

  deletePolicy(policyId: string): void {
    this.confirmAndProcessAction(
      $localize`:@@security_policies_confirm_delete_policy:Are you sure you want to delete the policy?`,
      () => this.securityService.deletePolicy(policyId));
  }

  editPolicy(policyId: string): void {
    // noinspection JSIgnoredPromiseFromCall
    this.router.navigate([encodeURIComponent(policyId)], {relativeTo: this.activatedRoute});
  }

  getPolicyTypeName(policyType: PolicyType): string {
    if (policyType == PolicyType.XACMLPolicy) {
      return "XACML Policy";
    } else if (policyType == PolicyType.XACMLPolicySet) {
      return "XACML Policy Set";
    } else {
      return "Unknown";
    }
  }

  newPolicy(): void {
    // noinspection JSIgnoredPromiseFromCall
    this.router.navigate(['new'], {relativeTo: this.activatedRoute});
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
                                  action: () => Observable<void | boolean>): void {
    const dialogRef = this.dialogService.showConfirmationDialog({message: confirmationMessage});

    dialogRef
    .afterClosed()
    .pipe(first(), filter((confirmed) => confirmed === true), switchMap(() => {
      this.spinnerService.showSpinner();
      return action().pipe(catchError((error) => this.handleError(error)),
        tap(() => this.resetTable()), switchMap(
          () => this.loadPolicySummaries().pipe(catchError((error) => this.handleError(error)))),
        finalize(() => this.spinnerService.hideSpinner()));
    }), takeUntil(this.destroy$))
    .subscribe();
  }

  private handleError(error: Error): Observable<never> {
    if (error instanceof AccessDeniedError || error instanceof InvalidArgumentError || error instanceof ServiceUnavailableError) {
      // noinspection JSIgnoredPromiseFromCall
      this.router.navigateByUrl('/error/send-error-report', {state: {error}});
    } else {
      this.dialogService.showErrorDialog(error);
    }
    return throwError(() => error);
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
      error: (error) => this.handleError(error),
    });
  }

  private loadPolicySummaries(): Observable<PolicySummaries> {
    const filter = this.tableFilter.filter?.trim().toLowerCase() || '';

    let sortDirection = SortDirection.Descending;

    if (this.sort.active) {
      sortDirection = this.sort.direction === 'asc' ? SortDirection.Ascending :
        SortDirection.Descending;
    }

    return this.dataSource
    .load(filter, sortDirection, this.paginator.pageIndex, this.paginator.pageSize)
    .pipe(catchError((error) => this.handleError(error)));
  }

  private resetTable(): void {
    this.tableFilter.reset(false);
    this.paginator.pageIndex = 0;
    this.sort.active = '';
    this.sort.direction = 'asc' as SortDirection;
  }
}
