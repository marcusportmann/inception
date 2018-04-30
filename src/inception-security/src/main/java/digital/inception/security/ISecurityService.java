/*
 * Copyright 2018 Marcus Portmann
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

//~--- JDK imports ------------------------------------------------------------

import java.util.List;
import java.util.UUID;

/**
 * The <code>ISecurityService</code> interface defines the functionality provided by a Security
 * Service implementation, which manages the security related information for an application.
 *
 * @author Marcus Portmann
 */
public interface ISecurityService
{
  /**
   * Add the user to the group.
   *
   * @param userDirectoryId the Universally Unique Identifier (UUID) used to uniquely identify the
   *                        user directory
   * @param username        the username identifying the user
   * @param groupName       the name of the security group uniquely identifying the security group
   */
  void addUserToGroup(UUID userDirectoryId, String username, String groupName)
    throws InvalidArgumentException, UserDirectoryNotFoundException, UserNotFoundException,
        GroupNotFoundException, SecurityServiceException;

  /**
   * Administratively change the password for the user.
   *
   * @param userDirectoryId      the Universally Unique Identifier (UUID) used to uniquely identify
   *                             the user directory
   * @param username             the username identifying the user
   * @param newPassword          the new password
   * @param expirePassword       expire the user's password
   * @param lockUser             lock the user
   * @param resetPasswordHistory reset the user's password history
   * @param reason               the reason for changing the password
   */
  void adminChangePassword(UUID userDirectoryId, String username, String newPassword,
      boolean expirePassword, boolean lockUser, boolean resetPasswordHistory,
      PasswordChangeReason reason)
    throws InvalidArgumentException, UserDirectoryNotFoundException, UserNotFoundException,
        SecurityServiceException;

  /**
   * Authenticate the user.
   *
   * @param username the username identifying the user
   * @param password the password being used to authenticate
   *
   * @return the Universally Unique Identifier (UUID) used to uniquely identify the user directory
   */
  UUID authenticate(String username, String password)
    throws InvalidArgumentException, AuthenticationFailedException, UserLockedException,
        ExpiredPasswordException, UserNotFoundException, SecurityServiceException;

  /**
   * Change the password for the user.
   *
   * @param username    the username identifying the user
   * @param password    the password for the user that is used to authorise the operation
   * @param newPassword the new password
   *
   * @return the Universally Unique Identifier (UUID) used to uniquely identify the user directory
   */
  UUID changePassword(String username, String password, String newPassword)
    throws InvalidArgumentException, AuthenticationFailedException, UserLockedException,
        UserNotFoundException, ExistingPasswordException, SecurityServiceException;

  /**
   * Create a new authorised function.
   *
   * @param function the function
   */
  void createFunction(Function function)
    throws InvalidArgumentException, DuplicateFunctionException, SecurityServiceException;

  /**
   * Create a new security group.
   *
   * @param userDirectoryId the Universally Unique Identifier (UUID) used to uniquely identify the
   *                        user directory
   * @param group           the security group
   */
  void createGroup(UUID userDirectoryId, Group group)
    throws InvalidArgumentException, UserDirectoryNotFoundException, DuplicateGroupException,
        SecurityServiceException;

  /**
   * Create a new organization.
   *
   * @param organization        the organization
   * @param createUserDirectory should a new internal user directory be created for the organization
   *
   * @return the new internal user directory that was created for the organization or
   *         <code>null</code> if no user directory was created
   */
  UserDirectory createOrganization(Organization organization, boolean createUserDirectory)
    throws InvalidArgumentException, DuplicateOrganizationException, SecurityServiceException;

  /**
   * Create a new user.
   *
   * @param userDirectoryId the Universally Unique Identifier (UUID) used to uniquely identify the
   *                        user directory
   * @param user            the user
   * @param expiredPassword create the user with its password expired
   * @param userLocked      create the user locked
   */
  void createUser(UUID userDirectoryId, User user, boolean expiredPassword, boolean userLocked)
    throws InvalidArgumentException, UserDirectoryNotFoundException, DuplicateUserException,
        SecurityServiceException;

  /**
   * Create a new user directory.
   *
   * @param userDirectory the user directory
   */
  void createUserDirectory(UserDirectory userDirectory)
    throws InvalidArgumentException, SecurityServiceException;

  /**
   * Delete the authorised function.
   *
   * @param code the code identifying the authorised function
   */
  void deleteFunction(String code)
    throws InvalidArgumentException, FunctionNotFoundException, SecurityServiceException;

  /**
   * Delete the security group.
   *
   * @param userDirectoryId the Universally Unique Identifier (UUID) used to uniquely identify the
   *                        user directory
   * @param groupName       the name of the security group uniquely identifying the security group
   */
  void deleteGroup(UUID userDirectoryId, String groupName)
    throws InvalidArgumentException, UserDirectoryNotFoundException, GroupNotFoundException,
        ExistingGroupMembersException, SecurityServiceException;

  /**
   * Delete the organization.
   *
   * @param organizationId the Universally Unique Identifier (UUID) used to uniquely identify the
   *                       organization
   */
  void deleteOrganization(UUID organizationId)
    throws InvalidArgumentException, OrganizationNotFoundException, SecurityServiceException;

  /**
   * Delete the user.
   *
   * @param userDirectoryId the Universally Unique Identifier (UUID) used to uniquely identify the
   *                        user directory
   * @param username        the username identifying the user
   */
  void deleteUser(UUID userDirectoryId, String username)
    throws InvalidArgumentException, UserDirectoryNotFoundException, UserNotFoundException,
        SecurityServiceException;

  /**
   * Delete the user directory.
   *
   * @param userDirectoryId the Universally Unique Identifier (UUID) used to uniquely identify the
   *                        user directory
   */
  void deleteUserDirectory(UUID userDirectoryId)
    throws InvalidArgumentException, UserDirectoryNotFoundException, SecurityServiceException;

  /**
   * Retrieve the users matching the attribute criteria.
   *
   * @param userDirectoryId the Universally Unique Identifier (UUID) used to uniquely identify the
   *                        user directory
   * @param attributes      the attribute criteria used to select the users
   *
   * @return the list of users whose attributes match the attribute criteria
   */
  List<User> findUsers(UUID userDirectoryId, List<Attribute> attributes)
    throws InvalidArgumentException, UserDirectoryNotFoundException, InvalidAttributeException,
        SecurityServiceException;

  /**
   * Retrieve the filtered list of organizations.
   *
   * @param filter the filter to apply to the organizations
   *
   * @return the filtered list of organizations
   */
  List<Organization> getFilteredOrganizations(String filter)
    throws InvalidArgumentException, SecurityServiceException;

  /**
   * Retrieve the filtered list of user directories.
   *
   * @param filter the filter to apply to the user directories
   *
   * @return the filtered list of user directories
   */
  List<UserDirectory> getFilteredUserDirectories(String filter)
    throws InvalidArgumentException, SecurityServiceException;

  /**
   * Retrieve the filtered list of users.
   *
   * @param userDirectoryId the Universally Unique Identifier (UUID) used to uniquely identify the
   *                        user directory
   * @param filter          the filter to apply to the users
   *
   * @return the filtered list of users
   */
  List<User> getFilteredUsers(UUID userDirectoryId, String filter)
    throws InvalidArgumentException, UserDirectoryNotFoundException, SecurityServiceException;

  /**
   * Retrieve the authorised function.
   *
   * @param code the code identifying the function
   *
   * @return the authorised function
   */
  Function getFunction(String code)
    throws InvalidArgumentException, FunctionNotFoundException, SecurityServiceException;

  /**
   * Retrieve the authorised function codes for the user.
   *
   * @param userDirectoryId the Universally Unique Identifier (UUID) used to uniquely identify the
   *                        user directory
   * @param username        the username identifying the user
   *
   * @return the list of authorised function codes for the user
   */
  List<String> getFunctionCodesForUser(UUID userDirectoryId, String username)
    throws InvalidArgumentException, UserDirectoryNotFoundException, UserNotFoundException,
        SecurityServiceException;

  /**
   * Retrieve all the authorised functions.
   *
   * @return the list of authorised functions
   */
  List<Function> getFunctions()
    throws SecurityServiceException;

  /**
   * Retrieve the security group.
   *
   * @param userDirectoryId the Universally Unique Identifier (UUID) used to uniquely identify the
   *                        user directory
   * @param groupName       the name of the security group uniquely identifying the security group
   *
   * @return the security group
   */
  Group getGroup(UUID userDirectoryId, String groupName)
    throws InvalidArgumentException, UserDirectoryNotFoundException, GroupNotFoundException,
        SecurityServiceException;

  /**
   * Retrieve the security group names for the user.
   *
   * @param userDirectoryId the Universally Unique Identifier (UUID) used to uniquely identify the
   *                        user directory
   * @param username        the username identifying the user
   *
   * @return the security group names for the user
   */
  List<String> getGroupNamesForUser(UUID userDirectoryId, String username)
    throws InvalidArgumentException, UserDirectoryNotFoundException, UserNotFoundException,
        SecurityServiceException;

  /**
   * Retrieve all the security groups.
   *
   * @param userDirectoryId the Universally Unique Identifier (UUID) used to uniquely identify the
   *                        user directory
   *
   * @return the list of security groups
   */
  List<Group> getGroups(UUID userDirectoryId)
    throws InvalidArgumentException, UserDirectoryNotFoundException, SecurityServiceException;

  /**
   * Retrieve the security groups for the user.
   *
   * @param userDirectoryId the Universally Unique Identifier (UUID) used to uniquely identify the
   *                        user directory
   * @param username        the username identifying the user
   *
   * @return the security groups for the user
   */
  List<Group> getGroupsForUser(UUID userDirectoryId, String username)
    throws InvalidArgumentException, UserDirectoryNotFoundException, UserNotFoundException,
        SecurityServiceException;

  /**
   * Retrieve the number of filtered organizations.
   *
   * @param filter the filter to apply to the organizations
   *
   * @return the number of filtered organizations
   */
  int getNumberOfFilteredOrganizations(String filter)
    throws InvalidArgumentException, SecurityServiceException;

  /**
   * Retrieve the number of filtered user directories.
   *
   * @param filter the filter to apply to the user directories
   *
   * @return the number of filtered user directories
   */
  int getNumberOfFilteredUserDirectories(String filter)
    throws InvalidArgumentException, SecurityServiceException;

  /**
   * Retrieve the number of filtered users.
   *
   * @param userDirectoryId the Universally Unique Identifier (UUID) used to uniquely identify the
   *                        user directory
   * @param filter          the filter to apply to the users
   *
   * @return the number of filtered users
   */
  int getNumberOfFilteredUsers(UUID userDirectoryId, String filter)
    throws InvalidArgumentException, UserDirectoryNotFoundException, SecurityServiceException;

  /**
   * Retrieve the number of security groups
   *
   * @param userDirectoryId the Universally Unique Identifier (UUID) used to uniquely identify the
   *                        user directory
   *
   * @return the number of security groups
   */
  int getNumberOfGroups(UUID userDirectoryId)
    throws InvalidArgumentException, UserDirectoryNotFoundException, SecurityServiceException;

  /**
   * Retrieve the number of organizations
   *
   * @return the number of organizations
   */
  int getNumberOfOrganizations()
    throws SecurityServiceException;

  /**
   * Retrieve the number of user directories
   *
   * @return the number of user directories
   */
  int getNumberOfUserDirectories()
    throws SecurityServiceException;

  /**
   * Retrieve the number of users.
   *
   * @param userDirectoryId the Universally Unique Identifier (UUID) used to uniquely identify the
   *                        user directory
   *
   * @return the number of users
   */
  int getNumberOfUsers(UUID userDirectoryId)
    throws InvalidArgumentException, UserDirectoryNotFoundException, SecurityServiceException;

  /**
   * Retrieve the organization.
   *
   * @param organizationId the Universally Unique Identifier (UUID) used to uniquely identify the
   *                       organization
   *
   * @return the organization
   */
  Organization getOrganization(UUID organizationId)
    throws InvalidArgumentException, OrganizationNotFoundException, SecurityServiceException;

  /**
   * Retrieve the Universally Unique Identifiers (UUIDs) used to uniquely identify the organizations
   * associated with the user directory.
   *
   * @param userDirectoryId the Universally Unique Identifier (UUID) used to uniquely identify the
   *                        user directory
   *
   * @return the Universally Unique Identifiers (UUIDs) used to uniquely identify the organizations
   *         associated with the user directory
   */
  List<UUID> getOrganizationIdsForUserDirectory(UUID userDirectoryId)
    throws InvalidArgumentException, UserDirectoryNotFoundException, SecurityServiceException;

  /**
   * Retrieve the organizations.
   *
   * @return the list of organizations
   */
  List<Organization> getOrganizations()
    throws SecurityServiceException;

  /**
   * Retrieve the organizations associated with the user directory.
   *
   * @param userDirectoryId the Universally Unique Identifier (UUID) used to uniquely identify the
   *                        user directory
   *
   * @return the organizations associated with the user directory
   */
  List<Organization> getOrganizationsForUserDirectory(UUID userDirectoryId)
    throws InvalidArgumentException, UserDirectoryNotFoundException, SecurityServiceException;

  /**
   * Retrieve the user.
   *
   * @param userDirectoryId the Universally Unique Identifier (UUID) used to uniquely identify the
   *                        user directory
   * @param username        the username identifying the user
   *
   * @return the user
   */
  User getUser(UUID userDirectoryId, String username)
    throws InvalidArgumentException, UserDirectoryNotFoundException, UserNotFoundException,
        SecurityServiceException;

  /**
   * Retrieve the user directories.
   *
   * @return the list of user directories
   */
  List<UserDirectory> getUserDirectories()
    throws SecurityServiceException;

  /**
   * Retrieve the user directories the organization is associated with.
   *
   * @param organizationId the Universally Unique Identifier (UUID) used to uniquely identify the
   *                       organization
   *
   * @return the user directories the organization is associated with
   */
  List<UserDirectory> getUserDirectoriesForOrganization(UUID organizationId)
    throws InvalidArgumentException, OrganizationNotFoundException, SecurityServiceException;

  /**
   * Retrieve the user directory.
   *
   * @param userDirectoryId the Universally Unique Identifier (UUID) used to uniquely identify the
   *                        user directory
   *
   * @return the user directory
   */
  UserDirectory getUserDirectory(UUID userDirectoryId)
    throws InvalidArgumentException, UserDirectoryNotFoundException, SecurityServiceException;

  /**
   * Retrieve the Universally Unique Identifier (UUID) used to uniquely identify the user directory
   * that the user with the specified username is associated with.
   *
   * @param username the username identifying the user
   *
   * @return the Universally Unique Identifier (UUID) used to uniquely identify the user directory
   *         that the user with the specified username is associated with or <code>null</code> if
   *         the user cannot be found
   */
  UUID getUserDirectoryIdForUser(String username)
    throws InvalidArgumentException, SecurityServiceException;

  /**
   * Retrieve the user directory types.
   *
   * @return the user directory types
   */
  List<UserDirectoryType> getUserDirectoryTypes()
    throws SecurityServiceException;

  /**
   * Retrieve all the users.
   *
   * @param userDirectoryId the Universally Unique Identifier (UUID) used to uniquely identify the
   *                        user directory
   *
   * @return the list of users
   */
  List<User> getUsers(UUID userDirectoryId)
    throws InvalidArgumentException, UserDirectoryNotFoundException, SecurityServiceException;

  /**
   * Is the user in the security group?
   *
   * @param userDirectoryId the Universally Unique Identifier (UUID) used to uniquely identify the
   *                        user directory
   * @param username        the username identifying the user
   * @param groupName       the name of the security group uniquely identifying the security group
   *
   * @return <code>true</code> if the user is a member of the security group or <code>false</code>
   *         otherwise
   */
  boolean isUserInGroup(UUID userDirectoryId, String username, String groupName)
    throws InvalidArgumentException, UserDirectoryNotFoundException, UserNotFoundException,
        GroupNotFoundException, SecurityServiceException;

  /**
   * Reload the user directories.
   */
  void reloadUserDirectories()
    throws SecurityServiceException;

  /**
   * Remove the user from the security group.
   *
   * @param userDirectoryId the Universally Unique Identifier (UUID) used to uniquely identify the
   *                        user directory
   * @param username        the username identifying the user
   * @param groupName       the security group name
   */
  void removeUserFromGroup(UUID userDirectoryId, String username, String groupName)
    throws InvalidArgumentException, UserDirectoryNotFoundException, UserNotFoundException,
        GroupNotFoundException, SecurityServiceException;

  /**
   * Does the user directory support administering security groups.
   *
   * @param userDirectoryId the Universally Unique Identifier (UUID) used to uniquely identify the
   *                        user directory
   *
   * @return <code>true</code> if the user directory supports administering security groups or
   *         <code>false</code> otherwise
   */
  boolean supportsGroupAdministration(UUID userDirectoryId)
    throws InvalidArgumentException, UserDirectoryNotFoundException;

  /**
   * Does the user directory support administering users.
   *
   * @param userDirectoryId the Universally Unique Identifier (UUID) used to uniquely identify the
   *                        user directory
   *
   * @return <code>true</code> if the user directory supports administering users or
   *         <code>false</code> otherwise
   */
  boolean supportsUserAdministration(UUID userDirectoryId)
    throws InvalidArgumentException, UserDirectoryNotFoundException;

  /**
   * Update the authorised function.
   *
   * @param function the function
   */
  void updateFunction(Function function)
    throws InvalidArgumentException, FunctionNotFoundException, SecurityServiceException;

  /**
   * Update the security group.
   *
   * @param userDirectoryId the Universally Unique Identifier (UUID) used to uniquely identify the
   *                        user directory
   * @param group           the security group
   */
  void updateGroup(UUID userDirectoryId, Group group)
    throws InvalidArgumentException, UserDirectoryNotFoundException, GroupNotFoundException,
        SecurityServiceException;

  /**
   * Update the organization.
   *
   * @param organization the organization
   */
  void updateOrganization(Organization organization)
    throws InvalidArgumentException, OrganizationNotFoundException, SecurityServiceException;

  /**
   * Update the user.
   *
   * @param userDirectoryId the Universally Unique Identifier (UUID) used to uniquely identify the
   *                        user directory
   * @param user            the user
   * @param expirePassword  expire the user's password as part of the update
   * @param lockUser        lock the user as part of the update
   */
  void updateUser(UUID userDirectoryId, User user, boolean expirePassword, boolean lockUser)
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
