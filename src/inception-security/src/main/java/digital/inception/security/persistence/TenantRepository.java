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

package digital.inception.security.persistence;

import digital.inception.security.model.Tenant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

/**
 * The <b>TenantRepository</b> interface declares the persistence for the <b>Tenant</b> domain type.
 *
 * @author Marcus Portmann
 */
public interface TenantRepository
    extends JpaRepository<Tenant, UUID>, JpaSpecificationExecutor<Tenant> {

  /**
   * Add the user directory to the tenant.
   *
   * @param tenantId the ID for the tenant
   * @param userDirectoryId the ID for the user directory
   */
  @Transactional
  @Modifying
  @Query(
      value =
          "insert into security_user_directory_to_tenant_map(tenant_id, user_directory_id) "
              + "values (:tenantId, :userDirectoryId)",
      nativeQuery = true)
  void addUserDirectoryToTenant(
      @Param("tenantId") UUID tenantId, @Param("userDirectoryId") UUID userDirectoryId);

  /**
   * Check whether the tenant with the specified name exists.
   *
   * @param name the name of the tenant
   * @return <b>true</b> if a tenant with the specified name exists or <b>false</b> otherwise
   */
  boolean existsByNameIgnoreCase(String name);

  /**
   * Retrieve the tenants ordered by name ascending.
   *
   * @return the tenants ordered by name ascending
   */
  List<Tenant> findAllByOrderByNameAsc();

  /**
   * Retrieve the tenants for the user directory.
   *
   * @param userDirectoryId the ID for the user directory
   * @return the tenants for the user directory
   */
  @Query("select t from Tenant t join t.userDirectories as ud where ud.id = :userDirectoryId")
  List<Tenant> findAllByUserDirectoryId(@Param("userDirectoryId") UUID userDirectoryId);

  /**
   * Retrieve the name of the tenant.
   *
   * @param tenantId the ID for the tenant
   * @return an Optional containing the name of the tenant or an empty Optional if the tenant could
   *     not be found
   */
  @Query("select t.name from Tenant t where t.id = :tenantId")
  Optional<String> getNameById(@Param("tenantId") UUID tenantId);

  /**
   * Retrieve the IDs for the user directories for the tenant.
   *
   * @param tenantId the ID for the tenant
   * @return the IDs for the user directories for the tenant
   */
  @Query("select ud.id from UserDirectory ud join ud.tenants as o where o.id = :tenantId")
  List<UUID> getUserDirectoryIdsById(@Param("tenantId") UUID tenantId);

  /**
   * Remove the user directory from the tenant
   *
   * @param tenantId the ID for the tenant
   * @param userDirectoryId the ID for the user directory
   */
  @Transactional
  @Modifying
  @Query(
      value =
          "delete from security_user_directory_to_tenant_map "
              + "where tenant_id=:tenantId and user_directory_id = :userDirectoryId",
      nativeQuery = true)
  void removeUserDirectoryFromTenant(
      @Param("tenantId") UUID tenantId, @Param("userDirectoryId") UUID userDirectoryId);

  /**
   * Check whether the user directory to tenant mapping exists.
   *
   * @param tenantId the ID for the tenant
   * @param userDirectoryId the ID for the user directory
   * @return <b>1</b> if the user directory to tenant mapping exists or <b>0</b> otherwise
   */
  @Query(
      value =
          "select (case when count(*) > 0 then 1 else 0 end) from security_user_directory_to_tenant_map where "
              + "tenant_id = :tenantId and user_directory_id = :userDirectoryId",
      nativeQuery = true)
  int userDirectoryToTenantMappingExists(
      @Param("tenantId") UUID tenantId, @Param("userDirectoryId") UUID userDirectoryId);
}
