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
import { GroupMember } from './group-member';

/**
 * The GroupMembers class holds the results of a request to retrieve a list of group members.
 *
 * @author Marcus Portmann
 */
export class GroupMembers {
  /**
   * The group members.
   */
  groupMembers: GroupMember[];

  /**
   * The name of the group that the group members are associated with.
   */
  groupName: string;

  /**
   * The page index.
   */
  pageIndex: number;

  /**
   * The page size.
   */
  pageSize: number;

  /**
   * The sort direction that was applied to the group members.
   */
  sortDirection: SortDirection;

  /**
   * The total number of group members.
   */
  total: number;

  /**
   * Constructs a new GroupMembers.
   *
   * @param groupName       The name of the group that the group members are associated with.
   * @param groupMembers    The group members.
   * @param total           The total number of groups.
   * @param sortDirection   The sort direction that was applied to the group members.
   * @param pageIndex       The page index.
   * @param pageSize        The page size.
   */
  constructor(
    groupName: string,
    groupMembers: GroupMember[],
    total: number,
    sortDirection: SortDirection,
    pageIndex: number,
    pageSize: number
  ) {
    this.groupName = groupName;
    this.groupMembers = groupMembers;
    this.total = total;
    this.sortDirection = sortDirection;
    this.pageIndex = pageIndex;
    this.pageSize = pageSize;
  }
}
