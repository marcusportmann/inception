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
import { Tenant } from './tenant';

/**
 * The Tenants class holds the results of a request to retrieve a list of tenants.
 *
 * @author Marcus Portmann
 */
export class Tenants {
  /**
   * The filter that was applied to the tenants.
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
   * The sort direction that was applied to the tenants.
   */
  sortDirection: SortDirection;

  /**
   * The tenants.
   */
  tenants: Tenant[];

  /**
   * The total number of tenants.
   */
  total: number;

  /**
   * Constructs a new Tenants.
   *
   * @param tenants       The tenants.
   * @param total         The total number of tenants.
   * @param sortDirection The sort direction that was applied to the tenants.
   * @param pageIndex     The page index.
   * @param pageSize      The page size.
   * @param filter        The filter that was applied to the tenants.
   */
  constructor(
    tenants: Tenant[],
    total: number,
    sortDirection: SortDirection,
    pageIndex: number,
    pageSize: number,
    filter?: string
  ) {
    this.tenants = tenants;
    this.total = total;
    this.sortDirection = sortDirection;
    this.pageIndex = pageIndex;
    this.pageSize = pageSize;
    this.filter = !!filter ? filter : null;
  }
}
