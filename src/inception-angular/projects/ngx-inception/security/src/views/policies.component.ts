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
import {MatDialogRef} from '@angular/material/dialog';
import {MatPaginator} from '@angular/material/paginator';
import {MatSort} from '@angular/material/sort';
import {ActivatedRoute, Router} from '@angular/router';
import {
  AccessDeniedError, AdminContainerView, ConfirmationDialogComponent, DialogService, Error,
  InvalidArgumentError, ServiceUnavailableError, SortDirection, SpinnerService, TableFilterComponent
} from 'ngx-inception/core';
import {merge, Subscription} from 'rxjs';
import {finalize, first} from 'rxjs/operators';
import {PolicySummaryDatasource} from '../services/policy-summary.datasource';
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

  dataSource: PolicySummaryDatasource;

  displayedColumns = ['id', 'version', 'name', 'type', 'actions'];

  @HostBinding('class') hostClass = 'flex flex-column flex-fill';

  @ViewChild(MatPaginator, {static: true}) paginator!: MatPaginator;

  @ViewChild(MatSort, {static: true}) sort!: MatSort;

  @ViewChild(TableFilterComponent, {static: true}) tableFilter!: TableFilterComponent;

  protected readonly PolicyType = PolicyType;

  private subscriptions: Subscription = new Subscription();

  constructor(private router: Router, private activatedRoute: ActivatedRoute,
              private securityService: SecurityService, private dialogService: DialogService,
              private spinnerService: SpinnerService) {
    super();

    this.dataSource = new PolicySummaryDatasource(this.securityService);
  }

  get title(): string {
    return $localize`:@@security_policies_title:Policies`
  }

  deletePolicy(policyId: string): void {
    const dialogRef: MatDialogRef<ConfirmationDialogComponent, boolean> = this.dialogService.showConfirmationDialog(
      {
        message: $localize`:@@security_policies_confirm_delete_policy:Are you sure you want to delete the policy?`
      });

    dialogRef.afterClosed()
    .pipe(first())
    .subscribe((confirmation: boolean | undefined) => {
      if (confirmation === true) {
        this.spinnerService.showSpinner();

        this.securityService.deletePolicy(policyId)
        .pipe(first(), finalize(() => this.spinnerService.hideSpinner()))
        .subscribe(() => {
          this.loadPolicySummaries();
        }, (error: Error) => {
          // noinspection SuspiciousTypeOfGuard
          if ((error instanceof AccessDeniedError) || (error instanceof InvalidArgumentError) || (error instanceof ServiceUnavailableError)) {
            // noinspection JSIgnoredPromiseFromCall
            this.router.navigateByUrl('/error/send-error-report', {state: {error}});
          } else {
            this.dialogService.showErrorDialog(error);
          }
        });
      }
    });
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

  loadPolicySummaries(): void {
    let filter = '';

    if (!!this.tableFilter.filter) {
      filter = this.tableFilter.filter;
      filter = filter.trim();
      filter = filter.toLowerCase();
    }

    const sortDirection = this.sort.direction === 'asc' ? SortDirection.Ascending :
      SortDirection.Descending;

    this.dataSource.load(filter, sortDirection, this.paginator.pageIndex, this.paginator.pageSize);
  }

  newPolicy(): void {
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
      if ((error instanceof AccessDeniedError) || (error instanceof InvalidArgumentError) || (error instanceof ServiceUnavailableError)) {
        // noinspection JSIgnoredPromiseFromCall
        this.router.navigateByUrl('/error/send-error-report', {state: {error}});
      } else {
        this.dialogService.showErrorDialog(error);
      }
    }));

    this.subscriptions.add(this.sort.sortChange.subscribe(() => {
      this.paginator.pageIndex = 0;
    }));

    this.subscriptions.add(this.tableFilter.changed.subscribe(() => {
      this.paginator.pageIndex = 0;
    }));

    this.subscriptions.add(
      merge(this.sort.sortChange, this.tableFilter.changed, this.paginator.page)
      .subscribe(() => {
        this.loadPolicySummaries();
      }));

    this.loadPolicySummaries();
  }

  ngOnDestroy(): void {
    this.subscriptions.unsubscribe();
  }
}
