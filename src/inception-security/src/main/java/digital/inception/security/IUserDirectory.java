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

//~--- JDK imports ------------------------------------------------------------

import java.util.List;

/**
 * The <code>IUserDirectoryProvider</code> interface defines the functionality provided by a user
 * directory, which manages users and security groups.
 *
 * @author Marcus Portmann
 */
interface IUserDirectory
{
  /**
   * Add the user to the security group.
   *
   * @param username  the username identifying the user
   * @param groupName the name of the security group uniquely identifying the security group
   */
  void addUserToGroup(String username, String groupName)
    throws UserNotFoundException, GroupNotFoundException, SecurityServiceException;

  /**
   * Administratively change the password for the user.
   *
   * @param username             the username identifying the user
   * @param newPassword          the new password
   * @param expirePassword       expire the user's password
   * @param lockUser             lock the user
   * @param resetPasswordHistory reset the user's password history
   * @param reason               the reason for changing the password
   */
  void adminChangePassword(String username, String newPassword, boolean expirePassword,
      boolean lockUser, boolean resetPasswordHistory, PasswordChangeReason reason)
    throws UserNotFoundException, SecurityServiceException;

  /**
   * Authenticate the user.
   *
   * @param username the username identifying the user
   * @param password the password being used to authenticate
   */
  void authenticate(String username, String password)
    throws AuthenticationFailedException, UserLockedException, ExpiredPasswordException,
        UserNotFoundException, SecurityServiceException;

  /**
   * Change the password for the user.
   *
   * @param username    the username identifying the user
   * @param password    the password for the user that is used to authorise the operation
   * @param newPassword the new password
   */
  void changePassword(String username, String password, String newPassword)
    throws AuthenticationFailedException, UserLockedException, UserNotFoundException,
        ExistingPasswordException, SecurityServiceException;

  /**
   * Create a new security group.
   *
   * @param group the security group
   */
  void createGroup(Group group)
    throws DuplicateGroupException, SecurityServiceException;

  /**
   * Create a new user.
   *
   * @param user            the user
   * @param expiredPassword create the user with its password expired
   * @param userLocked      create the user locked
   */
  void createUser(User user, boolean expiredPassword, boolean userLocked)
    throws DuplicateUserException, SecurityServiceException;

  /**
   * Delete the security group.
   *
   * @param groupName the name of the security group uniquely identifying the security group
   */
  void deleteGroup(String groupName)
    throws GroupNotFoundException, ExistingGroupMembersException, SecurityServiceException;

  /**
   * Delete the user.
   *
   * @param username the username identifying the user
   */
  void deleteUser(String username)
    throws UserNotFoundException, SecurityServiceException;

  /**
   * Retrieve the users matching the attribute criteria.
   *
   * @param attributes the attribute criteria used to select the users
   *
   * @return the list of users whose attributes match the attribute criteria
   */
  List<User> findUsers(List<Attribute> attributes)
    throws InvalidAttributeException, SecurityServiceException;

  /**
   * Retrieve the filtered list of users.
   *
   * @param filter the filter to apply to the users
   *
   * @return the filtered list of users
   */
  List<User> getFilteredUsers(String filter)
    throws SecurityServiceException;

  /**
   * Retrieve the authorised function codes for the user.
   *
   * @param username the username identifying the user
   *
   * @return the list of authorised function codes for the user
   */
  List<String> getFunctionCodesForUser(String username)
    throws UserNotFoundException, SecurityServiceException;

  /**
   * Retrieve the security group.
   *
   * @param groupName the name of the security group uniquely identifying the security group
   *
   * @return the security group
   */
  Group getGroup(String groupName)
    throws GroupNotFoundException, SecurityServiceException;

  /**
   * Retrieve the security group names for the user.
   *
   * @param username the username identifying the user
   *
   * @return the security group names for the user
   */
  List<String> getGroupNamesForUser(String username)
    throws UserNotFoundException, SecurityServiceException;

  /**
   * Retrieve all the security groups.
   *
   * @return the list of security groups
   */
  List<Group> getGroups()
    throws SecurityServiceException;

  /**
   * Retrieve the security groups for the user.
   *
   * @param username the username identifying the user
   *
   * @return the security groups for the user
   */
  List<Group> getGroupsForUser(String username)
    throws UserNotFoundException, SecurityServiceException;

  /**
   * Retrieve the number of filtered users.
   *
   * @param filter the filter to apply to the users
   *
   * @return the number of filtered users
   */
  int getNumberOfFilteredUsers(String filter)
    throws SecurityServiceException;

  /**
   * Retrieve the number of security groups
   *
   * @return the number of security groups
   */
  int getNumberOfGroups()
    throws SecurityServiceException;

  /**
   * Retrieve the number of users.
   *
   * @return the number of users
   */
  int getNumberOfUsers()
    throws SecurityServiceException;

  /**
   * Retrieve the user.
   *
   * @param username the username identifying the user
   *
   * @return the user
   */
  User getUser(String username)
    throws UserNotFoundException, SecurityServiceException;

  /**
   * Retrieve all the users.
   *
   * @return the list of users
   */
  List<User> getUsers()
    throws SecurityServiceException;

  /**
   * Does the user with the specified username exist?
   *
   * @param username the username identifying the user
   *
   * @return <code>true</code> if a user with specified username exists or <code>false</code>
   *         otherwise
   */
  boolean isExistingUser(String username)
    throws SecurityServiceException;

  /**
   * Is the user in the security group?
   *
   * @param username  the username identifying the user
   * @param groupName the name of the security group uniquely identifying the security group
   *
   * @return <code>true</code> if the user is a member of the security group or <code>false</code>
   *         otherwise
   */
  boolean isUserInGroup(String username, String groupName)
    throws UserNotFoundException, GroupNotFoundException, SecurityServiceException;

  /**
   * Remove the user from the security group.
   *
   * @param username  the username identifying the user
   * @param groupName the security group name
   */
  void removeUserFromGroup(String username, String groupName)
    throws UserNotFoundException, GroupNotFoundException, SecurityServiceException;

  /**
   * Does the user directory support administering security groups.
   *
   * @return <code>true</code> if the user directory supports administering security groups or
   *         <code>false</code> otherwise
   */
  boolean supportsGroupAdministration();

  /**
   * Does the user directory support administering users.
   *
   * @return <code>true</code> if the user directory supports administering users or
   *         <code>false</code> otherwise
   */
  boolean supportsUserAdministration();

  /**
   * Update the security group.
   *
   * @param group the security group
   */
  void updateGroup(Group group)
    throws GroupNotFoundException, SecurityServiceException;

  /**
   * Update the user.
   *
   * @param user           the user
   * @param expirePassword expire the user's password as part of the update
   * @param lockUser       lock the user as part of the update
   */
  void updateUser(User user, boolean expirePassword, boolean lockUser)
    throws UserNotFoundException, SecurityServiceException;
}
