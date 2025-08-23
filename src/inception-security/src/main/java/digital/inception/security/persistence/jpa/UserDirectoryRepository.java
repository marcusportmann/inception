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

package digital.inception.security.persistence.jpa;

import digital.inception.security.model.UserDirectory;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * The {@code UserDirectoryRepository} interface declares the persistence for the {@code
 * UserDirectory} domain type.
 *
 * @author Marcus Portmann
 */
public interface UserDirectoryRepository
    extends JpaRepository<UserDirectory, UUID>, JpaSpecificationExecutor<UserDirectory> {

  /**
   * Check whether the user directory with the specified name exists.
   *
   * @param name the name of the user directory
   * @return {@code true} if a user directory with the specified name exists or {@code false}
   *     otherwise
   */
  boolean existsByNameIgnoreCase(String name);

  /**
   * Find the user directories ordered by name ascending.
   *
   * @return the user directories ordered by name ascending
   */
  List<UserDirectory> findAllByOrderByNameAsc();

  /**
   * Find the user directories for the tenant.
   *
   * @param tenantId the ID for the tenant
   * @return the user directories for the tenant
   */
  @Query("select ud from UserDirectory ud join ud.tenants as o where o.id = :tenantId")
  List<UserDirectory> findAllByTenantId(@Param("tenantId") UUID tenantId);

  /**
   * Find the name of the user directory.
   *
   * @param userDirectoryId the ID for the user directory
   * @return an Optional containing the name of the user directory or an empty Optional if the user
   *     directory could not be found
   */
  @Query("select ud.name from UserDirectory ud where ud.id = :userDirectoryId")
  Optional<String> findNameById(@Param("userDirectoryId") UUID userDirectoryId);

  /**
   * Find the IDs for the tenants for the user directory.
   *
   * @param userDirectoryId the ID for the user directory
   * @return the IDs for the tenants for the user directory
   */
  @Query("select t.id from Tenant t join t.userDirectories as ud where ud.id = :userDirectoryId")
  List<UUID> findTenantIdsById(@Param("userDirectoryId") UUID userDirectoryId);

  /**
   * Find the type for the user directory.
   *
   * @param userDirectoryId the ID for the user directory
   * @return an Optional containing the type for the user directory or an empty Optional if the user
   *     directory could not be found
   */
  @Query("select ud.type from UserDirectory ud where ud.id = :userDirectoryId")
  Optional<String> findTypeForUserDirectoryById(@Param("userDirectoryId") UUID userDirectoryId);
}
