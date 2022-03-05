/*
 * Copyright 2022 Marcus Portmann
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
import {Mandate} from "./mandate";
import {MandateSortBy} from "./mandate-sorty-by";

/**
 * The MandatesForParty class holds the results of a request to retrieve a list of mandates for a
 * party.
 *
 * @author Marcus Portmann
 */
export class MandatesForParty {

  /**
   * The mandates.
   */
  mandates: Mandate[];

  /**
   * The optional page index.
   */
  pageIndex?: number;

  /**
   * The optional page size.
   */
  pageSize?: number;

  /**
   * The ID for the party the mandates are associated with.
   */
  partyId: string;

  /**
   * The optional method used to sort the mandates e.g. by name.
   */
  sortBy?: MandateSortBy;

  /**
   * The optional sort direction that was applied to the mandates.
   */
  sortDirection?: SortDirection;

  /**
   * The ID for the tenant the mandates are associated with.
   */
  tenantId: string;

  /**
   * The total number of mandates.
   */
  total: number;

  /**
   * Constructs a new MandatesForParty.
   *
   * @param tenantId      The ID for the tenant the mandates are associated with.
   * @param partyId       The ID for the party the mandates are associated with
   * @param mandates      The mandates.
   * @param total         The total number of mandates.
   * @param sortBy        The optional method used to sort the mandates e.g. by name.
   * @param sortDirection The optional sort direction that was applied to the mandates.
   * @param pageIndex     The optional page index.
   * @param pageSize      The optional page size.
   */
  constructor(tenantId: string, partyId: string, mandates: Mandate[], total: number,
              sortBy?: MandateSortBy, sortDirection?: SortDirection, pageIndex?: number,
              pageSize?: number) {
    this.tenantId = tenantId;
    this.partyId = partyId;
    this.mandates = mandates;
    this.total = total;
    this.sortBy = sortBy;
    this.sortDirection = sortDirection;
    this.pageIndex = pageIndex;
    this.pageSize = pageSize;
  }
}
