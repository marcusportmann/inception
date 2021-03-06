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

import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * The <b>OrganizationRepository</b> interface declares the repository for the <b>Organization</b>
 * domain type.
 *
 * @author Marcus Portmann
 */
public interface OrganizationRepository extends JpaRepository<Organization, UUID> {

  void deleteByTenantIdAndId(UUID tenantId, UUID id);

  boolean existsByTenantIdAndId(UUID tenantId, UUID id);

  Page<Organization> findByTenantId(UUID tenantId, Pageable pageable);

  Optional<Organization> findByTenantIdAndId(UUID tenantId, UUID id);

  @Query("select o from Organization o where (lower(o.name) like lower(:filter))")
  Page<Organization> findFiltered(@Param("filter") String filter, Pageable pageable);
}
