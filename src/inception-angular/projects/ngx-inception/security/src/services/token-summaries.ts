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
import {TokenSortBy} from './token-sort-by';
import {TokenSummary} from './token-summary';

/**
 * The TokenSummaries class holds the results of a request to retrieve a list of token summaries.
 *
 * @author Marcus Portmann
 */
export class TokenSummaries {

  /**
   * The optional filter that was applied to the token summaries.
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
   * The method used to sort the token summaries e.g. by name.
   */
  sortBy: TokenSortBy;

  /**
   * The sort direction that was applied to the token summaries.
   */
  sortDirection: SortDirection;

  /**
   * The token summaries.
   */
  tokenSummaries: TokenSummary[];

  /**
   * The total number of token summaries.
   */
  total: number;

  /**
   * Constructs a new TokenSummaries.
   *
   * @param tokenSummaries The token summaries.
   * @param total          The total number of token summaries.
   * @param sortBy         The method used to sort the token summaries e.g. by name.
   * @param sortDirection  The sort direction that was applied to the token summaries.
   * @param pageIndex      The page index.
   * @param pageSize       The page size.
   * @param filter         The optional filter that was applied to the token summaries.
   */
  constructor(tokenSummaries: TokenSummary[], total: number, sortBy: TokenSortBy,
              sortDirection: SortDirection, pageIndex: number, pageSize: number, filter?: string) {
    this.tokenSummaries = tokenSummaries;
    this.total = total;
    this.sortBy = sortBy;
    this.sortDirection = sortDirection;
    this.pageIndex = pageIndex;
    this.pageSize = pageSize;
    this.filter = !!filter ? filter : null;
  }
}
