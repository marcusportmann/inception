/*
 * Copyright 2021 Marcus Portmann
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
import {Association} from "./association";
import {AssociationSortBy} from "./association-sorty-by";

/**
 * The Associations class holds the results of a request to retrieve a list of associations.
 *
 * @author Marcus Portmann
 */
export class Associations {

  /**
   * The associations.
   */
  associations: Association[];

  /**
   * The optional page index.
   */
  pageIndex?: number;

  /**
   * The optional page size.
   */
  pageSize?: number;

  /**
   * The ID for the party the associations are associated with.
   */
  partyId: string;

  /**
   * The optional method used to sort the associations e.g. by name.
   */
  sortBy?: AssociationSortBy;

  /**
   * The optional sort direction that was applied to the associations.
   */
  sortDirection?: SortDirection;

  /**
   * The ID for the tenant the associations are associated with.
   */
  tenantId: string;

  /**
   * The total number of associations.
   */
  total: number;

  /**
   * Constructs a new Associations.
   *
   * @param tenantId      The ID for the tenant the associations are associated with.
   * @param partyId       The ID for the party the associations are associated with
   * @param associations  The associations.
   * @param total         The total number of associations.
   * @param sortBy        The optional method used to sort the associations e.g. by name.
   * @param sortDirection The optional sort direction that was applied to the associations.
   * @param pageIndex     The optional page index.
   * @param pageSize      The optional page size.
   */
  constructor(tenantId: string, partyId: string, associations: Association[], total: number,
              sortBy?: AssociationSortBy, sortDirection?: SortDirection, pageIndex?: number,
              pageSize?: number) {
    this.tenantId = tenantId;
    this.partyId = partyId;
    this.associations = associations;
    this.total = total;
    this.sortBy = sortBy;
    this.sortDirection = sortDirection;
    this.pageIndex = pageIndex;
    this.pageSize = pageSize;
  }
}
