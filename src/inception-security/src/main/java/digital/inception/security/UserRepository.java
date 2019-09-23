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
import org.springframework.data.repository.query.QueryByExampleExecutor;

//~--- JDK imports ------------------------------------------------------------

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * The <code>UserRepository</code> interface declares the repository for the
 * <code>User</code> domain type.
 *
 * @author Marcus Portmann
 */
public interface UserRepository extends JpaRepository<User, UUID>, QueryByExampleExecutor<User>
{
  @Modifying
  @Query("delete from User u where u.id = :userId")
  void deleteById(@Param("userId") UUID userId);


  Optional<User> findByUserDirectoryIdAndUsernameIgnoreCase(UUID userDirectoryId, String username);

  @Query("select u.id from User u where u.userDirectoryId = :userDirectoryId and upper(u.username) like upper(:username)")
  Optional<UUID> getIdByUserDirectoryIdAndUsernameIgnoreCase(@Param("userDirectoryId") UUID userDirectoryId, @Param("username") String username);


  boolean existsByUserDirectoryIdAndUsernameIgnoreCase(UUID userDirectoryId, String username);

  List<User> findByUserDirectoryId(UUID userDirectoryId);

  @Query("select u.userDirectoryId from User u where upper(u.username) = upper(:username)")
  Optional<UUID> getUserDirectoryIdByUsernameIgnoreCase(@Param("username") String username);


  @Modifying
  @Query(value = "insert into security.users_password_history(id, user_id, changed, password) values (:id, :userId, current_timestamp, :password)", nativeQuery = true)
  void savePasswordInPasswordHistory(@Param("id")UUID id, @Param("userId")UUID userId, @Param("password")String password);

  @Query(value = "select count(uph.id) from security.users_password_history uph where uph.user_id = :userId and uph.changed > :after and uph.password = :password", nativeQuery = true)
  long countPasswordHistory(@Param("userId")UUID userId, @Param("after")LocalDateTime after, @Param("password")String password);

  @Modifying
  @Query("update User u set u.password = :password, u.passwordAttempts = :passwordAttempts, u.passwordExpiry = :passwordExpiry where u.id = :userId")
  void changePassword(@Param("userId") UUID userId, @Param("password")String password, @Param("passwordAttempts")int passwordAttempts, @Param("passwordExpiry")Optional<LocalDateTime> passwordExpiry);


  @Modifying
  @Query("update User u set u.passwordAttempts = u.passwordAttempts + 1 where u.id = :userId")
  void incrementPasswordAttempts(@Param("userId")UUID userId);

  @Query("select f.code from User u inner join Group g inner join Role r inner join Function f where u.id = :userId")
  List<String> getFunctionCodesByUserId(@Param("userId")UUID userId);

  @Query("select g.groupName from User u inner join Group g where u.id = :userId")
  List<String> getGroupNamesByUserId(@Param("userId")UUID userId);

  @Query("select r.code from User u inner join Group g inner join Role r where u.id = :userId")
  List<String> getRoleCodesByUserId(@Param("userId")UUID userId);

  @Query("select case when (count(u.id) > 0) then true else false end from User u inner join Group g where u.id = :userId and g.id = :groupId")
  boolean isUserInGroup(@Param("userId")UUID userId, @Param("groupId")UUID groupId);


  List<User> findByUsernameIgnoreCaseContainingOrFirstNameIgnoreCaseContainingOrLastNameIgnoreCaseContaining(String usernameFilter, String firstNameFilter, String lastNameFilter, Pageable pageable);




  long countByUsernameIgnoreCaseContainingOrFirstNameIgnoreCaseContainingOrLastNameIgnoreCaseContaining(String usernameFilter, String firstNameFilter, String lastNameFilter);

}
