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
  AfterViewInit, ChangeDetectionStrategy, Component, HostBinding, inject, ViewChild
} from '@angular/core';
import { MatPaginator } from '@angular/material/paginator';
import { MatSort } from '@angular/material/sort';
import {
  CoreModule, SortDirection, StatefulListView, TableFilterComponent
} from 'ngx-inception/core';
import { Observable } from 'rxjs';
import { finalize, takeUntil } from 'rxjs/operators';
import { PolicySummaries } from '../services/policy-summaries';
import { PolicySummaryDataSource } from '../services/policy-summary-data-source';
import { PolicyType } from '../services/policy-type';
import { SecurityService } from '../services/security.service';

/**
 * The PoliciesComponent class implements the Policies component.
 *
 * @author Marcus Portmann
 */
@Component({
  selector: 'inception-security-policies',
  standalone: true,
  imports: [CoreModule, TableFilterComponent],
  templateUrl: 'policies.component.html',
  styleUrls: ['policies.component.css'],
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class PoliciesComponent extends StatefulListView implements AfterViewInit {
  readonly dataSource: PolicySummaryDataSource;

  readonly defaultSortActive = 'id';

  readonly displayedColumns = ['id', 'version', 'name', 'type', 'actions'];

  @HostBinding('class') hostClass = 'flex flex-column flex-fill';

  readonly listKey = 'security.policies';

  @ViewChild(MatPaginator, { static: true }) override paginator!: MatPaginator;

  @ViewChild(MatSort, { static: true }) override sort!: MatSort;

  @ViewChild(TableFilterComponent, { static: true })
  override tableFilter!: TableFilterComponent;

  readonly title = $localize`:@@security_policies_title:Policies`;

  protected readonly PolicyType = PolicyType;

  /** Whether this navigation requested a state reset (from the sidebar). */
  private readonly resetStateRequested: boolean;

  private readonly securityService = inject(SecurityService);

  constructor() {
    super();

    // Read the reset flag from the current navigation
    const nav = this.router.currentNavigation();

    this.resetStateRequested = !!nav?.extras.state?.['resetState'];

    // Initialize the data source
    this.dataSource = new PolicySummaryDataSource(this.securityService);
  }

  deletePolicy(policyId: string): void {
    this.confirmAndProcessAction(
      $localize`:@@security_policies_confirm_delete_policy:Are you sure you want to delete the policy?`,
      () => this.securityService.deletePolicy(policyId),
      () => this.loadPolicySummaries()
    );
  }

  editPolicy(policyId: string): void {
    void this.router.navigate([policyId], {
      relativeTo: this.activatedRoute
    });
  }

  getPolicyTypeName(policyType: PolicyType): string {
    if (policyType === PolicyType.XACMLPolicy) {
      return 'XACML Policy';
    } else if (policyType === PolicyType.XACMLPolicySet) {
      return 'XACML Policy Set';
    } else {
      return 'Unknown';
    }
  }

  newPolicy(): void {
    void this.router.navigate(['new'], { relativeTo: this.activatedRoute });
  }

  ngAfterViewInit(): void {
    this.initializeStatefulList(this.resetStateRequested, () => this.loadData());

    // Stabilize view after mutating sort/paginator
    this.changeDetectorRef.detectChanges();
  }

  private loadData(): void {
    this.spinnerService.showSpinner();
    this.loadPolicySummaries()
      .pipe(
        finalize(() => this.spinnerService.hideSpinner()),
        takeUntil(this.destroy$)
      )
      .subscribe({
        next: () => {
          // Load complete
        },
        error: (error) => this.handleError(error, false)
      });
  }

  private loadPolicySummaries(): Observable<PolicySummaries> {
    const filterValue = this.tableFilter.filter?.trim().toLowerCase() || '';

    let sortDirection = SortDirection.Descending;

    if (this.sort.active && this.sort.direction) {
      sortDirection =
        this.sort.direction === 'asc' ? SortDirection.Ascending : SortDirection.Descending;
    }

    return this.dataSource.load(
      filterValue,
      sortDirection,
      this.paginator.pageIndex,
      this.paginator.pageSize
    );
  }
}
