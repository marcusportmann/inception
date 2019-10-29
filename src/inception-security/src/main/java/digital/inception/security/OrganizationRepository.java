/*
 * Copyright 2019 Marcus Portmann
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

package digital.inception.security;

//~--- non-JDK imports --------------------------------------------------------

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

//~--- JDK imports ------------------------------------------------------------

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * The <code>OrganizationRepository</code> interface declares the repository for the
 * <code>Organization</code> domain type.
 *
 * @author Marcus Portmann
 */
public interface OrganizationRepository extends JpaRepository<Organization, UUID>
{
  long countByNameContainingIgnoreCase(String name);

  @Modifying
  @Query("delete from Organization u where u.id = :organizationId")
  void deleteById(@Param("organizationId") UUID organizationId);

  boolean existsByNameIgnoreCase(String name);

  List<Organization> findAllByOrderByNameAsc(Pageable pageable);

  List<Organization> findAllByOrderByNameDesc(Pageable pageable);

  @Query("select o from Organization o join o.userDirectories as ud where ud.id = :userDirectoryId")
  List<Organization> findAllByUserDirectoryId(@Param("userDirectoryId") UUID userDirectoryId);

  List<Organization> findByNameContainingIgnoreCaseOrderByNameAsc(String name, Pageable pageable);

  List<Organization> findByNameContainingIgnoreCaseOrderByNameDesc(String name, Pageable pageable);

  @Query("select o.name from Organization o where o.id = :organizationId")
  Optional<String> getNameById(@Param("organizationId") UUID organizationId);

  @Query(
      "select ud.id from UserDirectory ud join ud.organizations as o where o.id = :organizationId")
  List<UUID> getUserDirectoryIdsById(@Param("organizationId") UUID organizationId);
}
