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
import {Group} from './group';

/**
 * The Groups class holds the results of a request to retrieve a list of groups.
 *
 * @author Marcus Portmann
 */
export class Groups {

  /**
   * The filter that was applied to the groups.
   */
  filter: string | null = null;

  /**
   * The groups.
   */
  groups: Group[];

  /**
   * The page index.
   */
  pageIndex: number;

  /**
   * The page size.
   */
  pageSize: number;

  /**
   * The sort direction that was applied to the groups.
   */
  sortDirection: SortDirection;

  /**
   * The total number of groups.
   */
  total: number;

  /**
   * The ID for the user directory the groups are associated with.
   */
  userDirectoryId: string;

  /**
   * Constructs a new Groups.
   *
   * @param userDirectoryId The ID for the user directory the groups are associated with.
   * @param groups          The groups.
   * @param total           The total number of groups.
   * @param sortDirection   The sort direction that was applied to the groups.
   * @param pageIndex       The page index.
   * @param pageSize        The page size.
   * @param filter          The filter that was applied to the groups.
   */
  constructor(userDirectoryId: string, groups: Group[], total: number, sortDirection: SortDirection,
              pageIndex: number, pageSize: number, filter?: string,) {
    this.userDirectoryId = userDirectoryId;
    this.groups = groups;
    this.total = total;
    this.sortDirection = sortDirection;
    this.pageIndex = pageIndex;
    this.pageSize = pageSize;
    this.filter = !!filter ? filter : null;
  }
}
