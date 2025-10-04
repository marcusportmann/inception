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

import { SortDirection } from 'ngx-inception/core';
import { ErrorReportSortBy } from './error-report-sort-by';
import { ErrorReportSummary } from './error-report-summary';

/**
 * The ErrorReportSummaries class holds the results of a request to retrieve a list of error report
 * summaries.
 *
 * @author Marcus Portmann
 */
export class ErrorReportSummaries {
  /**
   * The error report summaries.
   */
  errorReportSummaries: ErrorReportSummary[];

  /**
   * The page index.
   */
  pageIndex: number;

  /**
   * The page size.
   */
  pageSize: number;

  /**
   * The method used to sort the error report summaries e.g. by who submitted them.
   */
  sortBy: ErrorReportSortBy;

  /**
   * The sort direction that was applied to the error report summaries.
   */
  sortDirection: SortDirection;

  /**
   * The total number of error report summaries.
   */
  total: number;

  /**
   * Constructs a new ErrorReportSummaries.
   *
   * @param errorReportSummaries The error report summaries.
   * @param total                The total number of error report summaries.
   * @param sortBy               The method used to sort the error report summaries e.g. by who
   *                             submitted them.
   * @param sortDirection        The sort direction that was applied to the error report summaries.
   * @param pageIndex            The page index.
   * @param pageSize             The page size.
   */
  constructor(
    errorReportSummaries: ErrorReportSummary[],
    total: number,
    sortBy: ErrorReportSortBy,
    sortDirection: SortDirection,
    pageIndex: number,
    pageSize: number
  ) {
    this.errorReportSummaries = errorReportSummaries;
    this.total = total;
    this.sortBy = sortBy;
    this.sortDirection = sortDirection;
    this.pageIndex = pageIndex;
    this.pageSize = pageSize;
  }
}
