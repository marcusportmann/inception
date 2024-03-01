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

import {SortDirection} from 'ngx-inception/core';
import {PolicySortBy} from './policy-sort-by';
import {PolicySummary} from './policy-summary';

/**
 * The PolicySummaries class holds the results of a request to retrieve a list of policy summaries.
 *
 * @author Marcus Portmann
 */
export class PolicySummaries {

  /**
   * The optional filter that was applied to the policy summaries.
   */
  filter: string | null = null;

  /**
   * The page index.
   */
  pageIndex: number;

  /**
   * The page size.
   */
  pageSize: number;

  /**
   * The policy summaries.
   */
  policySummaries: PolicySummary[];

  /**
   * The method used to sort the policy summaries e.g. by name.
   */
  sortBy: PolicySortBy;

  /**
   * The sort direction that was applied to the policy summaries.
   */
  sortDirection: SortDirection;

  /**
   * The total number of policy summaries.
   */
  total: number;

  /**
   * Constructs a new PolicySummaries.
   *
   * @param policySummaries The policy summaries.
   * @param total           The total number of policy summaries.
   * @param sortBy          The method used to sort the policy summaries e.g. by name.
   * @param sortDirection   The sort direction that was applied to the policy summaries.
   * @param pageIndex       The page index.
   * @param pageSize        The page size.
   * @param filter          The optional filter that was applied to the policy summaries.
   */
  constructor(policySummaries: PolicySummary[], total: number, sortBy: PolicySortBy,
              sortDirection: SortDirection, pageIndex: number, pageSize: number, filter?: string) {
    this.policySummaries = policySummaries;
    this.total = total;
    this.sortBy = sortBy;
    this.sortDirection = sortDirection;
    this.pageIndex = pageIndex;
    this.pageSize = pageSize;
    this.filter = !!filter ? filter : null;
  }
}
