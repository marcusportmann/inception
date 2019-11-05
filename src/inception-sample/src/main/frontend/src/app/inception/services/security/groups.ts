/*
 * Copyright 2019 Marcus Portmann
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

import {SortDirection} from './sort-direction';
import {Group} from "./group";

/**
 * The Groups class holds the results of a request to retrieve a list of groups.
 *
 * @author Marcus Portmann
 */
export class Groups {

  /**
   * The optional filter that was applied to the groups.
   */
  filter?: string;

  /**
   * The optional page size.
   */
  pageIndex?: number;

  /**
   * The optional page index.
   */
  pageSize?: number;

  /**
   * The groups.
   */
  groups: Group[];

  /**
   * The optional sort direction that was applied to the groups.
   */
  sortDirection?: SortDirection;

  /**
   * The total number of groups.
   */
  total: number;

  /**
   * The Universally Unique Identifier (UUID) used to uniquely identify the user directory.
   */
  userDirectoryId: string;

  /**
   * Constructs a new Groups.
   *
   * @param userDirectoryId The Universally Unique Identifier (UUID) used to uniquely identify the
   *                        user directory.
   * @param groups          The groups.
   * @param total           The total number of groups.
   * @param filter          The optional filter that was applied to the groups.
   * @param sortDirection   The optional sort direction that was applied to the groups.
   * @param pageIndex       The optional page index.
   * @param pageSize        The optional page size.
   */
  constructor(userDirectoryId: string, groups: Group[], total: number, filter?: string,
              sortDirection?: SortDirection, pageIndex?: number, pageSize?: number) {
    this.userDirectoryId = userDirectoryId;
    this.groups = groups;
    this.total = total;
    this.filter = filter;
    this.sortDirection = sortDirection;
    this.pageIndex = pageIndex;
    this.pageSize = pageSize;
  }
}
