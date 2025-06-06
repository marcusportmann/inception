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
import digital.inception.security.model.User;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.query.QueryByExampleExecutor;
import org.springframework.transaction.annotation.Transactional;

/**
 * The {@code UserRepository} interface declares the persistence for the {@code User} domain type.
 *
 * @author Marcus Portmann
 */
public interface UserRepository
    extends JpaRepository<User, UUID>,
        QueryByExampleExecutor<User>,
        JpaSpecificationExecutor<User> {

  /**
   * Change the password for the user.
   *
   * @param userId the ID for the user
   * @param password the password
   * @param passwordAttempts the password attempts
   * @param passwordExpiry the password expiry
   */
  @Transactional
  @Modifying
  @Query(
      "update User u set u.password = :password, u.passwordAttempts = :passwordAttempts, "
          + "u.passwordExpiry = :passwordExpiry where u.id = :userId")
  void changePassword(
      @Param("userId") UUID userId,
      @Param("password") String password,
      @Param("passwordAttempts") int passwordAttempts,
      @Param("passwordExpiry") Optional<OffsetDateTime> passwordExpiry);

  /**
   * Check whether the user directory has existing users.
   *
   * @param userDirectoryId the ID for the user directory
   * @return {@code true} if the user directory has existing users or {@code false} otherwise
   */
  boolean existsByUserDirectoryId(UUID userDirectoryId);

  /**
   * Check whether the user exists.
   *
   * @param userDirectoryId the ID for the user directory
   * @param username the username
   * @return {@code true} if the user exists or {@code false} otherwise
   */
  boolean existsByUserDirectoryIdAndUsernameIgnoreCase(UUID userDirectoryId, String username);

  /**
   * Retrieve the users for the user directory.
   *
   * @param userDirectoryId the ID for the user directory
   * @return the users for the user directory
   */
  List<User> findByUserDirectoryId(UUID userDirectoryId);

  /**
   * Retrieve the user with the specified username for the user directory.
   *
   * @param userDirectoryId the ID for the user directory
   * @param username the username
   * @return an Optional containing the user or an empty Optional if the user could not be found
   */
  Optional<User> findByUserDirectoryIdAndUsernameIgnoreCase(UUID userDirectoryId, String username);

  /**
   * Retrieve the function codes for the user.
   *
   * @param userId the ID for the user
   * @return the function codes for the user
   */
  @Query(
      "select f.code from User u join u.groups as g join g.roles as r join r.functions as f "
          + "where u.id = :userId")
  List<String> getFunctionCodesByUserId(@Param("userId") UUID userId);

  /**
   * Retrieve the group names for the user
   *
   * @param userId the ID for the user
   * @return the group names for the user
   */
  @Query("select g.name from User u join u.groups as g where u.id = :userId")
  List<String> getGroupNamesByUserId(@Param("userId") UUID userId);

  /**
   * Retrieve the groups for the user.
   *
   * @param userId the ID for the user
   * @return the groups for the user
   */
  @Query("select g from User u join u.groups as g where u.id = :userId")
  List<Group> getGroupsByUserId(@Param("userId") UUID userId);

  /**
   * Retrieve the ID for the user with the specified username for the user directory.
   *
   * @param userDirectoryId the ID for the user directory
   * @param username the username
   * @return an Optional containing the ID for the user with the specified username for the user
   *     directory or an empty Optional if the user could not be found
   */
  @Query(
      "select u.id from User u where u.userDirectoryId = :userDirectoryId and lower(u.username) "
          + "like lower(:username)")
  Optional<UUID> getIdByUserDirectoryIdAndUsernameIgnoreCase(
      @Param("userDirectoryId") UUID userDirectoryId, @Param("username") String username);

  /**
   * Retrieve the name for the user with the specified username for the user directory.
   *
   * @param userDirectoryId the ID for the user directory
   * @param username the username
   * @return an Optional containing the name for the user with the specified username for the user
   *     directory or an empty Optional if the user could not be found
   */
  @Query(
      "select u.name from User u where ((lower(u.username) = lower(:username)) "
          + "and u.userDirectoryId = :userDirectoryId)")
  Optional<String> getNameByUserDirectoryIdAndUsernameIgnoreCase(
      @Param("userDirectoryId") UUID userDirectoryId, @Param("username") String username);

  /**
   * Retrieve the password history for the user.
   *
   * @param userId the ID for the user
   * @param after the date and time after which password history entries will be retrieved
   * @return the password history for the user
   */
  @Query(
      value =
          "select encoded_password from security_users_password_history "
              + "where user_id = :userId and changed > :after",
      nativeQuery = true)
  List<String> getPasswordHistory(
      @Param("userId") UUID userId, @Param("after") OffsetDateTime after);

  /**
   * Retrieve the role codes for the user.
   *
   * @param userId the ID for the user
   * @return the role codes for the user
   */
  @Query("select r.code from User u join u.groups as g join g.roles as r where u.id = :userId")
  List<String> getRoleCodesByUserId(@Param("userId") UUID userId);

  /**
   * Retrieve the ID for the user directory for the user with the specified username.
   *
   * @param username the username
   * @return an Optional containing the ID for the user directory for the user with the specified
   *     username or an empty Optional if the user could not be found
   */
  @Query("select u.userDirectoryId from User u where lower(u.username) = lower(:username)")
  Optional<UUID> getUserDirectoryIdByUsernameIgnoreCase(@Param("username") String username);

  /**
   * Increment the password attempts for the user.
   *
   * @param userId the ID for the user
   */
  @Transactional
  @Modifying
  @Query("update User u set u.passwordAttempts = u.passwordAttempts + 1 where u.id = :userId")
  void incrementPasswordAttempts(@Param("userId") UUID userId);

  /**
   * Check whether the user is a member of the group.
   *
   * @param userId the ID for the user
   * @param groupId the ID for the group
   * @return {@code true} if the user is a member of the group or {@code false} otherwise
   */
  @Query(
      "select case when (count(u.id) > 0) then true else false end from User u join u.groups "
          + "as g where u.id = :userId and g.id = :groupId")
  boolean isUserInGroup(@Param("userId") UUID userId, @Param("groupId") UUID groupId);

  /**
   * Reset the password for the user.
   *
   * @param userId the ID for the user
   * @param password the password
   * @param passwordExpiry the password expiry
   */
  @Transactional
  @Modifying
  @Query(
      "update User u set u.password = :password, u.passwordAttempts = 0, "
          + "u.passwordExpiry = :passwordExpiry where u.id = :userId")
  void resetPassword(
      @Param("userId") UUID userId,
      @Param("password") String password,
      @Param("passwordExpiry") OffsetDateTime passwordExpiry);

  /**
   * Reset the password history for the user.
   *
   * @param userId the ID for the user
   */
  @Transactional
  @Modifying
  @Query(
      value = "delete from security_users_password_history where user_id = :userId",
      nativeQuery = true)
  void resetPasswordHistory(@Param("userId") UUID userId);

  /**
   * Save the encoded password in the password history for the user.
   *
   * @param id the ID for the password history
   * @param userId the ID for the user
   * @param encodedPassword the encoded password
   */
  @Transactional
  @Modifying
  @Query(
      value =
          "insert into security_users_password_history(id, user_id, changed, encoded_password) "
              + "values (:id, :userId, current_timestamp, :encodedPassword)",
      nativeQuery = true)
  void savePasswordInPasswordHistory(
      @Param("id") UUID id,
      @Param("userId") UUID userId,
      @Param("encodedPassword") String encodedPassword);
}
