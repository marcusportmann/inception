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

package digital.inception.party.persistence;

import digital.inception.party.model.Organization;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * The <b>OrganizationRepository</b> interface declares the persistence for the <b>Organization</b>
 * domain type.
 *
 * @author Marcus Portmann
 */
@SuppressWarnings("BooleanMethodIsAlwaysInverted")
public interface OrganizationRepository extends JpaRepository<Organization, UUID> {

  /**
   * Delete the organization.
   *
   * @param tenantId the ID for the tenant
   * @param id the ID for the organization
   */
  void deleteByTenantIdAndId(UUID tenantId, UUID id);

  /**
   * Check whether the organization exists.
   *
   * @param tenantId the ID for the tenant
   * @param id the ID for the organization
   * @return <b>true</b> if the organization exists or <b>false</b> otherwise
   */
  boolean existsByTenantIdAndId(UUID tenantId, UUID id);

  /**
   * Retrieve the organizations for the tenant.
   *
   * @param tenantId the ID for the tenant
   * @param pageable the pagination information
   * @return the organizations for the tenant
   */
  Page<Organization> findByTenantId(UUID tenantId, Pageable pageable);

  /**
   * Retrieve the organization.
   *
   * @param tenantId the ID for the tenant
   * @param id the ID for the organization
   * @return an Optional containing the organization or an empty Optional if the organization could
   *     not be found
   */
  Optional<Organization> findByTenantIdAndId(UUID tenantId, UUID id);

  /**
   * Retrieve the filtered organizations for the tenant.
   *
   * @param tenantId the ID for the tenant
   * @param filter the filter to apply to the organizations
   * @param pageable the pagination information
   * @return the filtered organizations
   */
  @Query(
      "select o from Organization o where (o.tenantId = :tenantId) and (lower(o.name) like lower(:filter))")
  Page<Organization> findFiltered(
      @Param("tenantId") UUID tenantId, @Param("filter") String filter, Pageable pageable);
}
