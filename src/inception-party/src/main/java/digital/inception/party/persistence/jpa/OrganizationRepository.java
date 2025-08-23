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

package digital.inception.party.persistence.jpa;

import digital.inception.party.model.Organization;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;

/**
 * The {@code OrganizationRepository} interface declares the persistence for the {@code
 * Organization} domain type.
 *
 * @author Marcus Portmann
 */
@SuppressWarnings("BooleanMethodIsAlwaysInverted")
public interface OrganizationRepository
    extends JpaRepository<Organization, UUID>, JpaSpecificationExecutor<Organization> {

  /**
   * Delete the organization.
   *
   * @param tenantId the ID for the tenant
   * @param organizationId the ID for the organization
   */
  @Transactional
  @Modifying(clearAutomatically = true, flushAutomatically = true)
  void deleteByTenantIdAndId(UUID tenantId, UUID organizationId);

  /**
   * Check whether the organization exists.
   *
   * @param tenantId the ID for the tenant
   * @param organizationId the ID for the organization
   * @return {@code true} if the organization exists or {@code false} otherwise
   */
  boolean existsByTenantIdAndId(UUID tenantId, UUID organizationId);

  /**
   * Find the organization.
   *
   * @param tenantId the ID for the tenant
   * @param organizationId the ID for the organization
   * @return an Optional containing the organization or an empty Optional if the organization could
   *     not be found
   */
  Optional<Organization> findByTenantIdAndId(UUID tenantId, UUID organizationId);
}
