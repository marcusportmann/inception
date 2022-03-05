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

import {EntityType} from "./entity-type";

/**
 * The Snapshot class holds the information for a snapshot, which is a view of the data for
 * an entity at a specific point in time.
 *
 * @author Marcus Portmann
 */
export class Snapshot {

  /**
   * The JSON data for the entity.
   */
  data: string;

  /**
   * The ID for the entity.
   */
  entityId: string;

  /**
   * The type of entity.
   */
  entityType: EntityType;

  /**
   * The ID for the snapshot.
   */
  id: string;

  /**
   * The ID for the tenant the snapshot is associated with.
   */
  tenantId: string;

  /**
   * The date and time the snapshot was created.
   */
  timestamp: Date;

  /**
   * Constructs a new Snapshot.
   *
   * @param id         The ID for the snapshot.
   * @param tenantId   The ID for the tenant the snapshot is associated with.
   * @param entityType The type of entity.
   * @param entityId   The ID for the entity.
   * @param data       The JSON data for the entity.
   * @param timestamp  The date and time the snapshot was created.
   */
  constructor(id: string, tenantId: string, entityType: EntityType, entityId: string, data: string,
              timestamp: Date) {
    this.id = id;
    this.tenantId = tenantId;
    this.entityType = entityType;
    this.entityId = entityId;
    this.data = data;
    this.timestamp = timestamp;
  }
}
