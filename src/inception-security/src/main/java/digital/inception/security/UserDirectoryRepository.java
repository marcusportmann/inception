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

/**
 * The <code>UserDirectoryRepository</code> interface declares the repository for the
 * <code>UserDirectory</code> domain type.
 *
 * @author Marcus Portmann
 */
public interface UserDirectoryRepository extends JpaRepository<UserDirectory, Long>
{
  long countByNameContainingIgnoreCase(String name);

  @Modifying
  @Query("delete from UserDirectory ud where ud.id = :userDirectoryId")
  void deleteById(@Param("userDirectoryId") Long userDirectoryId);

  boolean existsByNameIgnoreCase(String name);

  List<UserDirectory> findAllByOrderByNameAsc(Pageable pageable);

  List<UserDirectory> findAllByOrderByNameDesc(Pageable pageable);

  @Query("select ud from UserDirectory ud join ud.organizations as o where o.id = :organizationId")
  List<UserDirectory> findAllByOrganizationId(@Param("organizationId") Long organizationId);

  List<UserDirectory> findByNameContainingIgnoreCaseOrderByNameAsc(String name, Pageable pageable);

  List<UserDirectory> findByNameContainingIgnoreCaseOrderByNameDesc(String name, Pageable pageable);

  @Query(
      "select o.id from Organization o join o.userDirectories as ud where ud.id = :userDirectoryId")
  List<Long> getOrganizationIdsById(@Param("userDirectoryId") Long userDirectoryId);

  @Query("select ud.type from UserDirectory ud where ud.id = :userDirectoryId")
  Optional<String> getTypeForUserDirectoryById(@Param("userDirectoryId") Long userDirectoryId);
}
