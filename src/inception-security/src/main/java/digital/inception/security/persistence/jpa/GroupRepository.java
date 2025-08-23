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

import digital.inception.security.model.Group;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

/**
 * The {@code GroupRepository} interface declares the persistence for the {@code Group} domain type.
 *
 * @author Marcus Portmann
 */
public interface GroupRepository
    extends JpaRepository<Group, UUID>, JpaSpecificationExecutor<Group> {

  /**
   * Add the role to the group.
   *
   * @param groupId the ID for the group
   * @param roleCode the code for the role
   */
  @Transactional
  @Modifying(clearAutomatically = true, flushAutomatically = true)
  @Query(
      value =
          "insert into security_role_to_group_map(role_code, group_id) values (:roleCode, :groupId)",
      nativeQuery = true)
  void addRoleToGroup(@Param("groupId") UUID groupId, @Param("roleCode") String roleCode);

  /**
   * Add the user to the group.
   *
   * @param groupId the ID for the group
   * @param userId the ID for the user
   */
  @Transactional
  @Modifying(clearAutomatically = true, flushAutomatically = true)
  @Query(
      value =
          "insert into security_user_to_group_map(user_id, group_id) values (:userId, :groupId)",
      nativeQuery = true)
  void addUserToGroup(@Param("groupId") UUID groupId, @Param("userId") UUID userId);

  /**
   * Check whether the user directory has existing groups.
   *
   * @param userDirectoryId the ID for the user directory
   * @return {@code true} if the user directory has existing groups or {@code false} otherwise
   */
  boolean existsByUserDirectoryId(UUID userDirectoryId);

  /**
   * Check whether the group exists.
   *
   * @param userDirectoryId the ID for the user directory
   * @param name the name of the group
   * @return {@code true} if the group exists or {@code false} otherwise
   */
  @Transactional
  boolean existsByUserDirectoryIdAndNameIgnoreCase(UUID userDirectoryId, String name);

  /**
   * Find the groups for the user directory.
   *
   * @param userDirectoryId the ID for the user directory
   * @return the groups for the user directory
   */
  List<Group> findByUserDirectoryId(UUID userDirectoryId);

  /**
   * Find the groups for the user directory.
   *
   * @param userDirectoryId the ID for the user directory
   * @param pageable the pagination information
   * @return the groups for the user directory
   */
  Page<Group> findByUserDirectoryId(UUID userDirectoryId, Pageable pageable);

  /**
   * Find the group.
   *
   * @param userDirectoryId the ID for the user directory
   * @param name the name of the group
   * @return an Optional containing the group or an empty Optional if the group could not be found
   */
  Optional<Group> findByUserDirectoryIdAndNameIgnoreCase(UUID userDirectoryId, String name);

  /**
   * Find the filtered usernames for the group.
   *
   * @param userDirectoryId the ID for the user directory
   * @param groupId the ID for the group
   * @param filter the filter to apply to the usernames
   * @param pageable the pagination information
   * @return the filtered usernames for the group
   */
  @Query(
      "select u.username from Group g join g.users as u where g.userDirectoryId = "
          + ":userDirectoryId and g.id = :groupId and (lower(u.username) like lower(:filter))")
  Page<String> getFilteredUsernamesForGroup(
      @Param("userDirectoryId") UUID userDirectoryId,
      @Param("groupId") UUID groupId,
      @Param("filter") String filter,
      Pageable pageable);

  /**
   * Find the codes for the functions associated with the groups for the user directory.
   *
   * @param userDirectoryId the ID for the user directory
   * @param groupNames the group names
   * @return the function codes
   */
  @Query(
      "select distinct f.code from Group g join g.roles as r join r.functions as f where g.userDirectoryId = :userDirectoryId and lower(g.name) in :groupNames")
  List<String> getFunctionCodesByUserDirectoryIdAndGroupNames(
      @Param("userDirectoryId") UUID userDirectoryId, @Param("groupNames") List<String> groupNames);

  /**
   * Find the ID for the group with the specified name for the user directory.
   *
   * @param userDirectoryId the ID for the user directory
   * @param name the group name
   * @return an Optional containing the ID for the group with the specified name for the user
   *     directory or an empty Optional if the group could not be found
   */
  @Query(
      "select g.id from Group g where g.userDirectoryId = :userDirectoryId and "
          + "lower(g.name) like lower(:name)")
  Optional<UUID> getIdByUserDirectoryIdAndNameIgnoreCase(
      @Param("userDirectoryId") UUID userDirectoryId, @Param("name") String name);

  /**
   * Find the group names for the user directory.
   *
   * @param userDirectoryId the ID for the user directory
   * @return the group names for the user directory
   */
  @Query("select g.name from Group g where g.userDirectoryId = :userDirectoryId")
  List<String> getNamesByUserDirectoryId(@Param("userDirectoryId") UUID userDirectoryId);

  /**
   * Find the number of users for the group.
   *
   * @param groupId the ID for the group
   * @return the number of users for the group
   */
  @Query("select count(u.id) from Group g join g.users as u where g.id = :groupId")
  int getNumberOfUsersForGroup(@Param("groupId") UUID groupId);

  /**
   * Find the role codes for the group.
   *
   * @param groupId the ID for the group
   * @return the role codes for the group
   */
  @Query("select r.code from Group g join g.roles as r where g.id = :groupId")
  List<String> getRoleCodesByGroupId(@Param("groupId") UUID groupId);

  /**
   * Find the codes for the roles associated with the groups for the user directory.
   *
   * @param userDirectoryId the ID for the group
   * @param groupNames the group names
   * @return the role codes
   */
  @Query(
      "select distinct r.code from Group g join g.roles as r where g.userDirectoryId = :userDirectoryId and lower(g.name) in :groupNames")
  List<String> getRoleCodesByUserDirectoryIdAndGroupNames(
      @Param("userDirectoryId") UUID userDirectoryId, @Param("groupNames") List<String> groupNames);

  /**
   * Find the usernames for the group.
   *
   * @param userDirectoryId the ID for the user directory
   * @param groupId the ID for the group
   * @return the usernames for the group
   */
  @Query(
      "select u.username from Group g join g.users as u "
          + "where g.userDirectoryId = :userDirectoryId and g.id = :groupId")
  List<String> getUsernamesForGroup(
      @Param("userDirectoryId") UUID userDirectoryId, @Param("groupId") UUID groupId);

  /**
   * Find the usernames for the group.
   *
   * @param userDirectoryId the ID for the user directory
   * @param groupId the ID for the group
   * @param pageable the pagination information
   * @return the usernames for the group
   */
  @Query(
      "select u.username from Group g join g.users as u "
          + "where g.userDirectoryId = :userDirectoryId and g.id = :groupId")
  Page<String> getUsernamesForGroup(
      @Param("userDirectoryId") UUID userDirectoryId,
      @Param("groupId") UUID groupId,
      Pageable pageable);

  /**
   * Remove the role from the group.
   *
   * @param groupId the ID for the group
   * @param roleCode the code for the role
   * @return the number of impacted role to group mappings
   */
  @Transactional
  @Modifying(clearAutomatically = true, flushAutomatically = true)
  @Query(
      value =
          "delete from security_role_to_group_map where group_id=:groupId and role_code = :roleCode",
      nativeQuery = true)
  int removeRoleFromGroup(@Param("groupId") UUID groupId, @Param("roleCode") String roleCode);

  /**
   * Remove the user from the group.
   *
   * @param groupId the ID for the group
   * @param userId the ID for the user
   */
  @Transactional
  @Modifying(clearAutomatically = true, flushAutomatically = true)
  @Query(
      value =
          "delete from security_user_to_group_map where group_id=:groupId and user_id = :userId",
      nativeQuery = true)
  void removeUserFromGroup(@Param("groupId") UUID groupId, @Param("userId") UUID userId);

  /**
   * Check whether the role to group mapping exists.
   *
   * @param groupId the ID for the group
   * @param roleCode the code for the role
   * @return {code 1} if the role to group mapping exists or {code 0} otherwise
   */
  @Query(
      value =
          "select case when count(1) > 0 then 1 else 0 end as row_exists from security_role_to_group_map where role_code = :roleCode and group_id = :groupId",
      nativeQuery = true)
  int roleToGroupMappingExists(@Param("groupId") UUID groupId, @Param("roleCode") String roleCode);
}
