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
import {ErrorReportSortBy} from './error-report-sort-by';
import {ErrorReportSummary} from './error-report-summary';

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
   * The optional filter that was applied to the error report summaries.
   */
  filter: string | null = null;

  /**
   * The ISO 8601 format date value for the date to retrieve the error report summaries from.
   */
  fromDate: string;

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
   * The ISO 8601 format date value for the date to retrieve the error report summaries to.
   */
  toDate: string;

  /**
   * The total number of error report summaries.
   */
  total: number;

  /**
   * Constructs a new ErrorReportSummaries.
   *
   * @param errorReportSummaries The error report summaries.
   * @param total                The total number of error report summaries.
   * @param fromDate             The ISO 8601 format date value for the date to retrieve the error
   *                             report summaries from.
   * @param toDate               The ISO 8601 format date value for the date to retrieve the error
   *                             report summaries to.
   * @param sortBy               The method used to sort the error report summaries e.g. by who
   *                             submitted them.
   * @param sortDirection        The sort direction that was applied to the error report summaries.
   * @param pageIndex            The page index.
   * @param pageSize             The page size.
   * @param filter               The optional filter that was applied to the error report
   *                             summaries.
   */
  constructor(errorReportSummaries: ErrorReportSummary[], total: number, fromDate: string,
              toDate: string, sortBy: ErrorReportSortBy, sortDirection: SortDirection,
              pageIndex: number, pageSize: number, filter?: string) {
    this.errorReportSummaries = errorReportSummaries;
    this.fromDate = fromDate;
    this.toDate = toDate;
    this.total = total;
    this.sortBy = sortBy;
    this.sortDirection = sortDirection;
    this.pageIndex = pageIndex;
    this.pageSize = pageIndex;
    this.filter = !!filter ? filter : null;
  }
}
