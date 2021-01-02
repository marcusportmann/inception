/*
 * Copyright 2020 Marcus Portmann
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

// ~--- JDK imports ------------------------------------------------------------

import digital.inception.core.sorting.SortDirection;
import digital.inception.core.validation.InvalidArgumentException;
import java.util.List;
import java.util.UUID;

/**
 * The <code>ISecurityService</code> interface defines the functionality provided by a Security
 * Service implementation, which manages the security related information for an application.
 *
 * @author Marcus Portmann
 */
public interface ISecurityService {

  /**
   * Add the group member to the group.
   *
   * @param userDirectoryId the Universally Unique Identifier (UUID) uniquely identifying the user
   *     directory
   * @param groupName the name identifying the group
   * @param memberType the group member type
   * @param memberName the group member name
   */
  void addMemberToGroup(
      UUID userDirectoryId, String groupName, GroupMemberType memberType, String memberName)
      throws InvalidArgumentException, UserDirectoryNotFoundException, GroupNotFoundException,
          UserNotFoundException, ExistingGroupMemberException, SecurityServiceException;

  /**
   * Add the role to the group.
   *
   * @param userDirectoryId the Universally Unique Identifier (UUID) uniquely identifying the user
   *     directory
   * @param groupName the name identifying the group
   * @param roleCode the code uniquely identifying the role
   */
  void addRoleToGroup(UUID userDirectoryId, String groupName, String roleCode)
      throws InvalidArgumentException, UserDirectoryNotFoundException, GroupNotFoundException,
          RoleNotFoundException, ExistingGroupRoleException, SecurityServiceException;

  /**
   * Add the user directory to the tenant.
   *
   * @param tenantId the Universally Unique Identifier (UUID) uniquely identifying the tenant
   * @param userDirectoryId the Universally Unique Identifier (UUID) uniquely identifying the user
   *     directory
   */
  void addUserDirectoryToTenant(UUID tenantId, UUID userDirectoryId)
      throws InvalidArgumentException, TenantNotFoundException, UserDirectoryNotFoundException,
          ExistingTenantUserDirectoryException, SecurityServiceException;

  /**
   * Add the user to the group.
   *
   * @param userDirectoryId the Universally Unique Identifier (UUID) uniquely identifying the user
   *     directory
   * @param groupName the name identifying the group
   * @param username the username identifying the user
   */
  void addUserToGroup(UUID userDirectoryId, String groupName, String username)
      throws InvalidArgumentException, UserDirectoryNotFoundException, GroupNotFoundException,
          UserNotFoundException, SecurityServiceException;

  /**
   * Administratively change the password for the user.
   *
   * @param userDirectoryId the Universally Unique Identifier (UUID) uniquely identifying the user
   *     directory
   * @param username the username identifying the user
   * @param newPassword the new password
   * @param expirePassword expire the user's password
   * @param lockUser lock the user
   * @param resetPasswordHistory reset the user's password history
   * @param reason the reason for changing the password
   */
  void adminChangePassword(
      UUID userDirectoryId,
      String username,
      String newPassword,
      boolean expirePassword,
      boolean lockUser,
      boolean resetPasswordHistory,
      PasswordChangeReason reason)
      throws InvalidArgumentException, UserDirectoryNotFoundException, UserNotFoundException,
          SecurityServiceException;

  /**
   * Authenticate the user.
   *
   * @param username the username identifying the user
   * @param password the password being used to authenticate
   * @return the Universally Unique Identifier (UUID) uniquely identifying the user directory
   */
  UUID authenticate(String username, String password)
      throws InvalidArgumentException, AuthenticationFailedException, UserLockedException,
          ExpiredPasswordException, UserNotFoundException, SecurityServiceException;

  /**
   * Change the password for the user.
   *
   * @param username the username identifying the user
   * @param password the password for the user that is used to authorise the operation
   * @param newPassword the new password
   * @return the Universally Unique Identifier (UUID) uniquely identifying the user directory
   */
  UUID changePassword(String username, String password, String newPassword)
      throws InvalidArgumentException, AuthenticationFailedException, UserLockedException,
          UserNotFoundException, ExistingPasswordException, SecurityServiceException;

  /**
   * Create the new authorised function.
   *
   * @param function the function
   */
  void createFunction(Function function)
      throws InvalidArgumentException, DuplicateFunctionException, SecurityServiceException;

  /**
   * Create the new group.
   *
   * @param group the group
   */
  void createGroup(Group group)
      throws InvalidArgumentException, UserDirectoryNotFoundException, DuplicateGroupException,
          SecurityServiceException;

  /**
   * Create the new tenant.
   *
   * @param tenant the tenant
   * @param createUserDirectory should a new internal user directory be created for the tenant
   * @return the new internal user directory that was created for the tenant or <code>null
   * </code> if no user directory was created
   */
  UserDirectory createTenant(Tenant tenant, boolean createUserDirectory)
      throws InvalidArgumentException, DuplicateTenantException, SecurityServiceException;

  /**
   * Create the new user.
   *
   * @param user the user
   * @param expiredPassword create the user with its password expired
   * @param userLocked create the user locked
   */
  void createUser(User user, boolean expiredPassword, boolean userLocked)
      throws InvalidArgumentException, UserDirectoryNotFoundException, DuplicateUserException,
          SecurityServiceException;

  /**
   * Create the new user directory.
   *
   * @param userDirectory the user directory
   */
  void createUserDirectory(UserDirectory userDirectory)
      throws InvalidArgumentException, DuplicateUserDirectoryException, SecurityServiceException;

  /**
   * Delete the authorised function.
   *
   * @param functionCode the code uniquely identifying the function
   */
  void deleteFunction(String functionCode)
      throws InvalidArgumentException, FunctionNotFoundException, SecurityServiceException;

  /**
   * Delete the group.
   *
   * @param userDirectoryId the Universally Unique Identifier (UUID) uniquely identifying the user
   *     directory
   * @param groupName the name identifying the group
   */
  void deleteGroup(UUID userDirectoryId, String groupName)
      throws InvalidArgumentException, UserDirectoryNotFoundException, GroupNotFoundException,
          ExistingGroupMembersException, SecurityServiceException;

  /**
   * Delete the tenant.
   *
   * @param tenantId the Universally Unique Identifier (UUID) uniquely identifying the tenant
   */
  void deleteTenant(UUID tenantId)
      throws InvalidArgumentException, TenantNotFoundException, SecurityServiceException;

  /**
   * Delete the user.
   *
   * @param userDirectoryId the Universally Unique Identifier (UUID) uniquely identifying the user
   *     directory
   * @param username the username identifying the user
   */
  void deleteUser(UUID userDirectoryId, String username)
      throws InvalidArgumentException, UserDirectoryNotFoundException, UserNotFoundException,
          SecurityServiceException;

  /**
   * Delete the user directory.
   *
   * @param userDirectoryId the Universally Unique Identifier (UUID) uniquely identifying the user
   *     directory
   */
  void deleteUserDirectory(UUID userDirectoryId)
      throws InvalidArgumentException, UserDirectoryNotFoundException, SecurityServiceException;

  /**
   * Retrieve the users matching the attribute criteria.
   *
   * @param userDirectoryId the Universally Unique Identifier (UUID) uniquely identifying the user
   *     directory
   * @param attributes the attribute criteria used to select the users
   * @return the users whose attributes match the attribute criteria
   */
  List<User> findUsers(UUID userDirectoryId, List<Attribute> attributes)
      throws InvalidArgumentException, UserDirectoryNotFoundException, InvalidAttributeException,
          SecurityServiceException;

  /**
   * Retrieve the authorised function.
   *
   * @param functionCode the code uniquely identifying the function
   * @return the authorised function
   */
  Function getFunction(String functionCode)
      throws InvalidArgumentException, FunctionNotFoundException, SecurityServiceException;

  /**
   * Retrieve the authorised function codes for the user.
   *
   * @param userDirectoryId the Universally Unique Identifier (UUID) uniquely identifying the user
   *     directory
   * @param username the username identifying the user
   * @return the authorised function codes for the user
   */
  List<String> getFunctionCodesForUser(UUID userDirectoryId, String username)
      throws InvalidArgumentException, UserDirectoryNotFoundException, UserNotFoundException,
          SecurityServiceException;

  /**
   * Retrieve all the authorised functions.
   *
   * @return the authorised functions
   */
  List<Function> getFunctions() throws SecurityServiceException;

  /**
   * Retrieve the group.
   *
   * @param userDirectoryId the Universally Unique Identifier (UUID) uniquely identifying the user
   *     directory
   * @param groupName the name identifying the group
   * @return the group
   */
  Group getGroup(UUID userDirectoryId, String groupName)
      throws InvalidArgumentException, UserDirectoryNotFoundException, GroupNotFoundException,
          SecurityServiceException;

  /**
   * Retrieve all the group names.
   *
   * @param userDirectoryId the Universally Unique Identifier (UUID) uniquely identifying the user
   *     directory
   * @return the group names
   */
  List<String> getGroupNames(UUID userDirectoryId)
      throws InvalidArgumentException, UserDirectoryNotFoundException, SecurityServiceException;

  /**
   * Retrieve the names identifying the groups the user is a member of.
   *
   * @param userDirectoryId the Universally Unique Identifier (UUID) uniquely identifying the user
   *     directory
   * @param username the username identifying the user
   * @return the names identifying the groups the user is a member of
   */
  List<String> getGroupNamesForUser(UUID userDirectoryId, String username)
      throws InvalidArgumentException, UserDirectoryNotFoundException, UserNotFoundException,
          SecurityServiceException;

  /**
   * Retrieve all the groups.
   *
   * @param userDirectoryId the Universally Unique Identifier (UUID) uniquely identifying the user
   *     directory
   * @return the groups
   */
  List<Group> getGroups(UUID userDirectoryId)
      throws InvalidArgumentException, UserDirectoryNotFoundException, SecurityServiceException;

  /**
   * Retrieve the groups.
   *
   * @param userDirectoryId the Universally Unique Identifier (UUID) uniquely identifying the user
   *     directory
   * @param filter the optional filter to apply to the groups
   * @param sortDirection the optional sort direction to apply to the groups
   * @param pageIndex the optional page index
   * @param pageSize the optional page size
   * @return the groups
   */
  Groups getGroups(
      UUID userDirectoryId,
      String filter,
      SortDirection sortDirection,
      Integer pageIndex,
      Integer pageSize)
      throws InvalidArgumentException, UserDirectoryNotFoundException, SecurityServiceException;

  /**
   * Retrieve the groups the user is a member of.
   *
   * @param userDirectoryId the Universally Unique Identifier (UUID) uniquely identifying the user
   *     directory
   * @param username the username identifying the user
   * @return the groups the user is a member of
   */
  List<Group> getGroupsForUser(UUID userDirectoryId, String username)
      throws InvalidArgumentException, UserDirectoryNotFoundException, UserNotFoundException,
          SecurityServiceException;

  /**
   * Retrieve the group members for the group.
   *
   * @param userDirectoryId the Universally Unique Identifier (UUID) uniquely identifying the user
   *     directory
   * @param groupName the name identifying the group
   * @return the group members for the group
   */
  List<GroupMember> getMembersForGroup(UUID userDirectoryId, String groupName)
      throws InvalidArgumentException, UserDirectoryNotFoundException, GroupNotFoundException,
          SecurityServiceException;

  /**
   * Retrieve the group members for the group.
   *
   * @param userDirectoryId the Universally Unique Identifier (UUID) uniquely identifying the user
   *     directory
   * @param groupName the name identifying the group
   * @param filter the optional filter to apply to the group members
   * @param sortDirection the optional sort direction to apply to the group members
   * @param pageIndex the optional page index
   * @param pageSize the optional page size
   * @return the group members for the group
   */
  GroupMembers getMembersForGroup(
      UUID userDirectoryId,
      String groupName,
      String filter,
      SortDirection sortDirection,
      Integer pageIndex,
      Integer pageSize)
      throws InvalidArgumentException, UserDirectoryNotFoundException, GroupNotFoundException,
          SecurityServiceException;

  /**
   * Retrieve the codes for the roles that have been assigned to the group.
   *
   * @param userDirectoryId the Universally Unique Identifier (UUID) uniquely identifying the user
   *     directory
   * @param groupName the name identifying the group
   * @return the codes for the roles that have been assigned to the group
   */
  List<String> getRoleCodesForGroup(UUID userDirectoryId, String groupName)
      throws InvalidArgumentException, UserDirectoryNotFoundException, GroupNotFoundException,
          SecurityServiceException;

  /**
   * Retrieve the codes for the roles that the user has been assigned.
   *
   * @param userDirectoryId the Universally Unique Identifier (UUID) uniquely identifying the user
   *     directory
   * @param username the username identifying the user
   * @return the codes for the roles that the user has been assigned
   */
  List<String> getRoleCodesForUser(UUID userDirectoryId, String username)
      throws InvalidArgumentException, UserDirectoryNotFoundException, UserNotFoundException,
          SecurityServiceException;

  /**
   * Retrieve all the roles.
   *
   * @return the roles
   */
  List<Role> getRoles() throws SecurityServiceException;

  /**
   * Retrieve the roles that have been assigned to the group.
   *
   * @param userDirectoryId the Universally Unique Identifier (UUID) uniquely identifying the user
   *     directory
   * @param groupName the name identifying the group
   * @return the roles that have been assigned to the group
   */
  List<GroupRole> getRolesForGroup(UUID userDirectoryId, String groupName)
      throws InvalidArgumentException, UserDirectoryNotFoundException, GroupNotFoundException,
          SecurityServiceException;

  /**
   * Retrieve the tenant.
   *
   * @param tenantId the Universally Unique Identifier (UUID) uniquely identifying the tenant
   * @return the tenant
   */
  Tenant getTenant(UUID tenantId)
      throws InvalidArgumentException, TenantNotFoundException, SecurityServiceException;

  /**
   * Retrieve the Universally Unique Identifiers (UUIDs) uniquely identifying the tenants the user
   * directory is associated with.
   *
   * @param userDirectoryId the Universally Unique Identifier (UUID) uniquely identifying the user
   *     directory
   * @return the Universally Unique Identifiers (UUIDs) uniquely identifying the tenants the user
   *     directory is associated with
   */
  List<UUID> getTenantIdsForUserDirectory(UUID userDirectoryId)
      throws InvalidArgumentException, UserDirectoryNotFoundException, SecurityServiceException;

  /**
   * Retrieve the name of the tenant.
   *
   * @param tenantId the Universally Unique Identifier (UUID) uniquely identifying the tenant
   * @return the name of the tenant
   */
  String getTenantName(UUID tenantId)
      throws InvalidArgumentException, TenantNotFoundException, SecurityServiceException;

  /**
   * Retrieve the tenants.
   *
   * @return the tenants
   */
  List<Tenant> getTenants() throws SecurityServiceException;

  /**
   * Retrieve the tenants.
   *
   * @param filter the optional filter to apply to the tenants
   * @param sortDirection the optional sort direction to apply to the tenants
   * @param pageIndex the optional page index
   * @param pageSize the optional page size
   * @return the tenants
   */
  Tenants getTenants(
      String filter, SortDirection sortDirection, Integer pageIndex, Integer pageSize)
      throws SecurityServiceException;

  /**
   * Retrieve the tenants the user directory is associated with.
   *
   * @param userDirectoryId the Universally Unique Identifier (UUID) uniquely identifying the user
   *     directory
   * @return the tenants the user directory is associated with
   */
  List<Tenant> getTenantsForUserDirectory(UUID userDirectoryId)
      throws InvalidArgumentException, UserDirectoryNotFoundException, SecurityServiceException;

  /**
   * Retrieve the user.
   *
   * @param userDirectoryId the Universally Unique Identifier (UUID) uniquely identifying the user
   *     directory
   * @param username the username identifying the user
   * @return the user
   */
  User getUser(UUID userDirectoryId, String username)
      throws InvalidArgumentException, UserDirectoryNotFoundException, UserNotFoundException,
          SecurityServiceException;

  /**
   * Retrieve the user directories.
   *
   * @return the user directories
   */
  List<UserDirectory> getUserDirectories() throws SecurityServiceException;

  /**
   * Retrieve the user directories.
   *
   * @param filter the optional filter to apply to the user directories
   * @param sortDirection the optional sort direction to apply to the user directories
   * @param pageIndex the optional page index
   * @param pageSize the optional page size
   * @return the user directories
   */
  UserDirectories getUserDirectories(
      String filter, SortDirection sortDirection, Integer pageIndex, Integer pageSize)
      throws SecurityServiceException;

  /**
   * Retrieve the user directories the tenant is associated with.
   *
   * @param tenantId the Universally Unique Identifier (UUID) uniquely identifying the tenant
   * @return the user directories the tenant is associated with
   */
  List<UserDirectory> getUserDirectoriesForTenant(UUID tenantId)
      throws InvalidArgumentException, TenantNotFoundException, SecurityServiceException;

  /**
   * Retrieve the user directory.
   *
   * @param userDirectoryId the Universally Unique Identifier (UUID) uniquely identifying the user
   *     directory
   * @return the user directory
   */
  UserDirectory getUserDirectory(UUID userDirectoryId)
      throws InvalidArgumentException, UserDirectoryNotFoundException, SecurityServiceException;

  /**
   * Retrieve the capabilities the user directory supports.
   *
   * @param userDirectoryId the Universally Unique Identifier (UUID) uniquely identifying the user
   *     directory
   * @return the capabilities the user directory supports
   */
  UserDirectoryCapabilities getUserDirectoryCapabilities(UUID userDirectoryId)
      throws InvalidArgumentException, UserDirectoryNotFoundException, SecurityServiceException;

  /**
   * Retrieve the Universally Unique Identifier (UUID) uniquely identifying the user directory that
   * the user with the specified username is associated with.
   *
   * @param username the username identifying the user
   * @return the Universally Unique Identifier (UUID) uniquely identifying the user directory that
   *     the user with the specified username is associated with or <code>null</code> if the user
   *     cannot be found
   */
  UUID getUserDirectoryIdForUser(String username)
      throws InvalidArgumentException, SecurityServiceException;

  /**
   * Retrieve the Universally Unique Identifiers (UUIDs) uniquely identifying the user directories
   * the tenant is associated with.
   *
   * @param tenantId the Universally Unique Identifier (UUID) uniquely identifying the tenant
   * @return the Universally Unique Identifiers (UUIDs) uniquely identifying the user directories
   *     the tenant is associated with
   */
  List<UUID> getUserDirectoryIdsForTenant(UUID tenantId)
      throws InvalidArgumentException, TenantNotFoundException, SecurityServiceException;

  /**
   * Retrieve the Universally Unique Identifiers (UUIDs) uniquely identifying the user directories
   * the user is associated with. Every user is associated with a user directory, which is in turn
   * associated with one or more tenants, which are in turn associated with one or more user
   * directories. The user is therefore associated indirectly with all these user directories.
   *
   * @param username the username identifying the user
   * @return the Universally Unique Identifiers (UUIDs) uniquely identifying the user directories
   *     the user is associated with
   */
  List<UUID> getUserDirectoryIdsForUser(String username)
      throws InvalidArgumentException, UserNotFoundException, SecurityServiceException;

  /**
   * Retrieve the name of the user directory.
   *
   * @param userDirectoryId the Universally Unique Identifier (UUID) uniquely identifying the user
   *     directory
   * @return the name of the user directory
   */
  String getUserDirectoryName(UUID userDirectoryId)
      throws InvalidArgumentException, UserDirectoryNotFoundException, SecurityServiceException;

  /**
   * Retrieve the summaries for the user directories.
   *
   * @param filter the optional filter to apply to the user directories
   * @param sortDirection the optional sort direction to apply to the user directories
   * @param pageIndex the optional page index
   * @param pageSize the optional page size
   * @return the summaries for the user directories
   */
  UserDirectorySummaries getUserDirectorySummaries(
      String filter, SortDirection sortDirection, Integer pageIndex, Integer pageSize)
      throws SecurityServiceException;

  /**
   * Retrieve the summaries for the user directories the tenant is associated with.
   *
   * @param tenantId the Universally Unique Identifier (UUID) uniquely identifying the tenant
   * @return the summaries for the user directories the tenant is associated with
   */
  List<UserDirectorySummary> getUserDirectorySummariesForTenant(UUID tenantId)
      throws InvalidArgumentException, TenantNotFoundException, SecurityServiceException;

  /**
   * Retrieve the user directory type for the user directory.
   *
   * @param userDirectoryId the Universally Unique Identifier (UUID) uniquely identifying the user
   *     directory
   * @return the user directory type for the user directory
   */
  UserDirectoryType getUserDirectoryTypeForUserDirectory(UUID userDirectoryId)
      throws InvalidArgumentException, UserDirectoryNotFoundException,
          UserDirectoryTypeNotFoundException, SecurityServiceException;

  /**
   * Retrieve the user directory types.
   *
   * @return the user directory types
   */
  List<UserDirectoryType> getUserDirectoryTypes() throws SecurityServiceException;

  /**
   * Retrieve the name of the user.
   *
   * @param userDirectoryId the Universally Unique Identifier (UUID) uniquely identifying the user
   *     directory
   * @param username the username identifying the user
   * @return the name of the user
   */
  String getUserName(UUID userDirectoryId, String username)
      throws InvalidArgumentException, UserDirectoryNotFoundException, UserNotFoundException,
          SecurityServiceException;

  /**
   * Retrieve all the users.
   *
   * @param userDirectoryId the Universally Unique Identifier (UUID) uniquely identifying the user
   *     directory
   * @return the users
   */
  List<User> getUsers(UUID userDirectoryId)
      throws InvalidArgumentException, UserDirectoryNotFoundException, SecurityServiceException;

  /**
   * Retrieve the users.
   *
   * @param userDirectoryId the Universally Unique Identifier (UUID) uniquely identifying the user
   *     directory
   * @param filter the optional filter to apply to the users
   * @param sortBy the optional method used to sort the users e.g. by name
   * @param sortDirection the optional sort direction to apply to the users
   * @param pageIndex the optional page index
   * @param pageSize the optional page size
   * @return the users
   */
  Users getUsers(
      UUID userDirectoryId,
      String filter,
      UserSortBy sortBy,
      SortDirection sortDirection,
      Integer pageIndex,
      Integer pageSize)
      throws InvalidArgumentException, UserDirectoryNotFoundException, SecurityServiceException;

  /**
   * Initiate the password reset process for the user.
   *
   * @param username the username identifying the user
   * @param resetPasswordUrl the reset password URL
   * @param sendEmail should the password reset e-mail be sent to the user
   */
  void initiatePasswordReset(String username, String resetPasswordUrl, boolean sendEmail)
      throws InvalidArgumentException, UserNotFoundException, SecurityServiceException;

  /**
   * Initiate the password reset process for the user.
   *
   * @param username the username identifying the user
   * @param resetPasswordUrl the reset password URL
   * @param sendEmail should the password reset e-mail be sent to the user
   * @param securityCode the pre-generated security code to use
   */
  void initiatePasswordReset(
      String username, String resetPasswordUrl, boolean sendEmail, String securityCode)
      throws InvalidArgumentException, UserNotFoundException, SecurityServiceException;

  /**
   * Does the user with the specified username exist?
   *
   * @param userDirectoryId the Universally Unique Identifier (UUID) uniquely identifying the user
   *     directory
   * @param username the username identifying the user
   * @return <code>true</code> if a user with specified username exists or <code>false</code>
   *     otherwise
   */
  boolean isExistingUser(UUID userDirectoryId, String username)
      throws InvalidArgumentException, UserDirectoryNotFoundException, SecurityServiceException;

  /**
   * Is the user in the group?
   *
   * @param userDirectoryId the Universally Unique Identifier (UUID) uniquely identifying the user
   *     directory
   * @param groupName the name identifying the group
   * @param username the username identifying the user
   * @return <code>true</code> if the user is a member of the group or <code>false</code> otherwise
   */
  boolean isUserInGroup(UUID userDirectoryId, String groupName, String username)
      throws InvalidArgumentException, UserDirectoryNotFoundException, UserNotFoundException,
          GroupNotFoundException, SecurityServiceException;

  /** Reload the user directories. */
  void reloadUserDirectories() throws SecurityServiceException;

  /**
   * Remove the group member from the group.
   *
   * @param userDirectoryId the Universally Unique Identifier (UUID) uniquely identifying the user
   *     directory
   * @param groupName the name identifying the group
   * @param memberType the group member type
   * @param memberName the group member name
   */
  void removeMemberFromGroup(
      UUID userDirectoryId, String groupName, GroupMemberType memberType, String memberName)
      throws InvalidArgumentException, UserDirectoryNotFoundException, GroupNotFoundException,
          GroupMemberNotFoundException, SecurityServiceException;

  /**
   * Remove the role from the group.
   *
   * @param userDirectoryId the Universally Unique Identifier (UUID) uniquely identifying the user
   *     directory
   * @param groupName the name identifying the group
   * @param roleCode the code uniquely identifying the role
   */
  void removeRoleFromGroup(UUID userDirectoryId, String groupName, String roleCode)
      throws InvalidArgumentException, UserDirectoryNotFoundException, GroupNotFoundException,
          GroupRoleNotFoundException, SecurityServiceException;

  /**
   * Remove the user directory from the tenant.
   *
   * @param tenantId the Universally Unique Identifier (UUID) uniquely identifying the tenant
   * @param userDirectoryId the Universally Unique Identifier (UUID) uniquely identifying the user
   *     directory
   */
  void removeUserDirectoryFromTenant(UUID tenantId, UUID userDirectoryId)
      throws InvalidArgumentException, TenantNotFoundException,
          TenantUserDirectoryNotFoundException, SecurityServiceException;

  /**
   * Remove the user from the group.
   *
   * @param userDirectoryId the Universally Unique Identifier (UUID) uniquely identifying the user
   *     directory
   * @param groupName the name identifying the group
   * @param username the username identifying the user
   */
  void removeUserFromGroup(UUID userDirectoryId, String groupName, String username)
      throws InvalidArgumentException, UserDirectoryNotFoundException, GroupNotFoundException,
          UserNotFoundException, SecurityServiceException;

  /**
   * Reset the password for the user.
   *
   * @param username the username identifying the user
   * @param newPassword the new password
   * @param securityCode the security code
   */
  void resetPassword(String username, String newPassword, String securityCode)
      throws InvalidArgumentException, UserNotFoundException, UserLockedException,
          InvalidSecurityCodeException, ExistingPasswordException, SecurityServiceException;

  /**
   * Update the authorised function.
   *
   * @param function the function
   */
  void updateFunction(Function function)
      throws InvalidArgumentException, FunctionNotFoundException, SecurityServiceException;

  /**
   * Update the group.
   *
   * @param group the group
   */
  void updateGroup(Group group)
      throws InvalidArgumentException, UserDirectoryNotFoundException, GroupNotFoundException,
          SecurityServiceException;

  /**
   * Update the tenant.
   *
   * @param tenant the tenant
   */
  void updateTenant(Tenant tenant)
      throws InvalidArgumentException, TenantNotFoundException, SecurityServiceException;

  /**
   * Update the user.
   *
   * @param user the user
   * @param expirePassword expire the user's password as part of the update
   * @param lockUser lock the user as part of the update
   */
  void updateUser(User user, boolean expirePassword, boolean lockUser)
      throws InvalidArgumentException, UserDirectoryNotFoundException, UserNotFoundException,
          SecurityServiceException;

  /**
   * Update the user directory.
   *
   * @param userDirectory the user directory
   */
  void updateUserDirectory(UserDirectory userDirectory)
      throws InvalidArgumentException, UserDirectoryNotFoundException, SecurityServiceException;
}
