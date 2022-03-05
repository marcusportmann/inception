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
import {Person} from './person';
import {PersonSortBy} from './person-sorty-by';

/**
 * The Persons class holds the results of a request to retrieve a list of persons.
 *
 * @author Marcus Portmann
 */
export class Persons {

  /**
   * The optional filter that was applied to the persons.
   */
  filter?: string;

  /**
   * The optional page index.
   */
  pageIndex?: number;

  /**
   * The optional page size.
   */
  pageSize?: number;

  /**
   * The persons.
   */
  persons: Person[];

  /**
   * The optional method used to sort the persons e.g. by name.
   */
  sortBy?: PersonSortBy;

  /**
   * The optional sort direction that was applied to the persons.
   */
  sortDirection?: SortDirection;

  /**
   * The ID for the tenant the persons are associated with.
   */
  tenantId: string;

  /**
   * The total number of persons.
   */
  total: number;

  /**
   * Constructs a new Persons.
   *
   * @param tenantId      The ID for the tenant the persons are associated with.
   * @param persons       The persons.
   * @param total         The total number of persons.
   * @param filter        The optional filter that was applied to the persons.
   * @param sortBy        The optional method used to sort the persons e.g. by name.
   * @param sortDirection The optional sort direction that was applied to the persons.
   * @param pageIndex     The optional page index.
   * @param pageSize      The optional page size.
   */
  constructor(tenantId: string, persons: Person[], total: number, filter?: string,
              sortBy?: PersonSortBy, sortDirection?: SortDirection, pageIndex?: number,
              pageSize?: number) {
    this.tenantId = tenantId;
    this.persons = persons;
    this.total = total;
    this.filter = filter;
    this.sortBy = sortBy;
    this.sortDirection = sortDirection;
    this.pageIndex = pageIndex;
    this.pageSize = pageSize;
  }
}
