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
import org.springframework.transaction.annotation.Transactional;

//~--- JDK imports ------------------------------------------------------------

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * The <code>GroupRepository</code> interface declares the repository for the
 * <code>Group</code> domain type.
 *
 * @author Marcus Portmann
 */
public interface GroupRepository extends JpaRepository<Group, UUID>
{
  @Modifying
  @Query(value = "insert into security.role_to_group_map(role_code, group_id) "
      + "values (:roleCode, :groupId)",
      nativeQuery = true)
  void addRoleToGroup(@Param("groupId") UUID groupId, @Param("roleCode") String roleCode);

  @Modifying
  @Query(value = "insert into security.user_to_group_map(user_id, group_id) "
      + "values (:userId, :groupId)",
      nativeQuery = true)
  void addUserToGroup(@Param("groupId") UUID groupId, @Param("userId") UUID userId);

  long countByUserDirectoryId(UUID userDirectoryId);

  @Query("select count(g.id) from Group g where (upper(g.name) like upper(:filter)) and "
      + "g.userDirectoryId = :userDirectoryId")
  long countFiltered(@Param("userDirectoryId") UUID userDirectoryId, @Param(
      "filter") String filter);

  @Query("select count(u.id) from Group g join g.users as u where g.userDirectoryId = "
      + ":userDirectoryId and g.id = :groupId and (upper(u.username) like upper(:filter))")
  long countFilteredUsernamesForGroup(@Param("userDirectoryId") UUID userDirectoryId, @Param(
      "groupId") UUID groupId, @Param("filter") String filter);

  @Query(value = "select count(role_code) from security.role_to_group_map rtgm where "
      + "rtgm.role_code = :roleCode and rtgm.group_id = :groupId",
      nativeQuery = true)
  long countGroupRole(@Param("groupId") UUID groupId, @Param("roleCode") String roleCode);

  @Query("select count(u.id) from Group g join g.users as u "
      + "where g.userDirectoryId = :userDirectoryId and g.id = :groupId")
  long countUsernamesForGroup(@Param("userDirectoryId") UUID userDirectoryId, @Param(
      "groupId") UUID groupId);

  @Query("select count(u.id) from Group g join g.users as u where g.id = :groupId")
  long countUsersById(@Param("groupId") UUID groupId);

  @Modifying
  @Query("delete from Group g where g.id = :groupId")
  void deleteById(@Param("groupId") UUID groupId);

  @Transactional
  boolean existsByUserDirectoryIdAndNameIgnoreCase(UUID userDirectoryId, String name);

  List<Group> findByUserDirectoryId(UUID userDirectoryId);

  List<Group> findByUserDirectoryId(UUID userDirectoryId, Pageable pageable);

  Optional<Group> findByUserDirectoryIdAndNameIgnoreCase(UUID userDirectoryId, String name);

  @Query("select g from Group g where (upper(g.name) like upper(:filter)) and "
      + "g.userDirectoryId = :userDirectoryId")
  List<Group> findFiltered(@Param("userDirectoryId") UUID userDirectoryId, @Param(
      "filter") String filter, Pageable pageable);

  @Query("select u.username from Group g join g.users as u where g.userDirectoryId = "
      + ":userDirectoryId and g.id = :groupId and (upper(u.username) like upper(:filter))")
  List<String> getFilteredUsernamesForGroup(@Param("userDirectoryId") UUID userDirectoryId, @Param(
      "groupId") UUID groupId, @Param("filter") String filter, Pageable pageable);

  @Query("select g.id from Group g where g.userDirectoryId = :userDirectoryId and "
      + "upper(g.name) like upper(:name)")
  Optional<UUID> getIdByUserDirectoryIdAndNameIgnoreCase(@Param(
      "userDirectoryId") UUID userDirectoryId, @Param("name") String name);

  @Query("select g.name from Group g where g.userDirectoryId = :userDirectoryId")
  List<String> getNamesByUserDirectoryId(@Param("userDirectoryId") UUID userDirectoryId);

  @Query("select r.code from Group g join g.roles as r where g.id = :groupId")
  List<String> getRoleCodesByGroupId(@Param("groupId") UUID groupId);

  @Query("select r from Group g join g.roles as r where g.id = :groupId")
  List<Role> getRolesByGroupId(@Param("groupId") UUID groupId);

  @Query("select u.username from Group g join g.users as u "
      + "where g.userDirectoryId = :userDirectoryId and g.id = :groupId")
  List<String> getUsernamesForGroup(@Param("userDirectoryId") UUID userDirectoryId, @Param(
      "groupId") UUID groupId);

  @Query("select u.username from Group g join g.users as u "
      + "where g.userDirectoryId = :userDirectoryId and g.id = :groupId")
  List<String> getUsernamesForGroup(@Param("userDirectoryId") UUID userDirectoryId, @Param(
      "groupId") UUID groupId, Pageable pageable);

  @Modifying
  @Query(value = "delete from security.user_to_group_map "
      + "where user_id = :userId and group_id=:groupId",
      nativeQuery = true)
  void removeUserFromGroup(@Param("groupId") UUID groupId, @Param("userId") UUID userId);
}
