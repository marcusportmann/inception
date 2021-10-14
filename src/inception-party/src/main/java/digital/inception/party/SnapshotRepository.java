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

package digital.inception.party;

import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * The <b>SnapshotRepository</b> interface declares the repository for the <b>Snapshot</b> domain
 * type.
 *
 * @author Marcus Portmann
 */
public interface SnapshotRepository extends JpaRepository<Snapshot, UUID> {

  /**
   * Retrieve the snapshots for the entity.
   *
   * @param tenantId the ID for the tenant
   * @param entityType the type of entity
   * @param entityId the ID for the entity
   * @param pageable the pagination information
   * @return the snapshots for the entity
   */
  Page<Snapshot> findByTenantIdAndEntityTypeAndEntityId(
      UUID tenantId, EntityType entityType, UUID entityId, Pageable pageable);
}
