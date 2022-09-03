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
import {EntityType} from './entity-type';
import {Snapshot} from './snapshot';

/**
 * The Snapshots class holds the results of a request to retrieve a list of snapshots.
 *
 * @author Marcus Portmann
 */
export class Snapshots {

  /**
   * The ID for the entity.
   */
  entityId: string;

  /**
   * The type of entity.
   */
  entityType: EntityType;

  /**
   * The optional ISO 8601 format date to retrieve the snapshots from.
   */
  from?: string;

  /**
   * The optional page index.
   */
  pageIndex?: number;

  /**
   * The optional page size.
   */
  pageSize?: number;

  /**
   * The snapshots.
   */
  snapshots: Snapshot[];

  /**
   * The optional sort direction that was applied to the snapshots.
   */
  sortDirection?: SortDirection;

  /**
   * The ID for the tenant the snapshots are associated with.
   */
  tenantId: string;

  /**
   * The optional ISO 8601 format date to retrieve the snapshots to.
   */
  to?: string;

  /**
   * The total number of snapshots.
   */
  total: number;

  /**
   * Constructs a new Snapshots.
   *
   * @param tenantId      The ID for the tenant the snapshots are associated with.
   * @param snapshots     The snapshots.
   * @param entityType    The type of entity.
   * @param entityId      The ID for the entity.
   * @param total         The total number of snapshots.
   * @param from          The optional ISO 8601 format date to retrieve the snapshots from.
   * @param to            The optional ISO 8601 format date to retrieve the snapshots to.
   * @param sortDirection The optional sort direction that was applied to the snapshots.
   * @param pageIndex     The optional page index.
   * @param pageSize      The optional page size.
   */
  constructor(tenantId: string, snapshots: Snapshot[], total: number, entityType: EntityType,
              entityId: string, from?: string, to?: string, sortDirection?: SortDirection,
              pageIndex?: number, pageSize?: number) {
    this.tenantId = tenantId;
    this.snapshots = snapshots;
    this.total = total;
    this.entityType = entityType;
    this.entityId = entityId;
    this.from = from;
    this.to = to;
    this.sortDirection = sortDirection;
    this.pageIndex = pageIndex;
    this.pageSize = pageSize;
  }
}
