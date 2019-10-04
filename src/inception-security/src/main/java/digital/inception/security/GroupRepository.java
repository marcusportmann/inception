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
  @Query(value = "insert into security.user_to_group_map(user_id, group_id) "
      + "values (:userId, :groupId)",
      nativeQuery = true)
  void addUserToGroup(@Param("userId") UUID userId, @Param("groupId") UUID groupId);

  long countByUserDirectoryId(UUID userDirectoryId);

  @Query("select count(u.id) from Group g join g.users as u where g.id = :groupId")
  long countUsersById(@Param("groupId") UUID groupId);

  @Modifying
  @Query("delete from Group g where g.id = :groupId")
  void deleteById(@Param("groupId") UUID groupId);

  @Transactional
  boolean existsByUserDirectoryIdAndGroupNameIgnoreCase(UUID userDirectoryId, String groupName);

  List<Group> findByUserDirectoryId(UUID userDirectoryId);

  Optional<Group> findByUserDirectoryIdAndGroupNameIgnoreCase(UUID userDirectoryId,
      String groupName);

  @Query("select g.id from Group g where g.userDirectoryId = :userDirectoryId and "
      + "upper(g.groupName) like upper(:groupName)")
  Optional<UUID> getIdByUserDirectoryIdAndGroupNameIgnoreCase(@Param(
      "userDirectoryId") UUID userDirectoryId, @Param("groupName") String groupName);

  @Modifying
  @Query(value = "delete from security.user_to_group_map "
      + "where user_id = :userId and group_id=:groupId",
      nativeQuery = true)
  void removeUserFromGroup(@Param("userId") UUID userId, @Param("groupId") UUID groupId);
}
