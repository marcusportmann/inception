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
 * directory, which manages users and groups.
 *
 * @author Marcus Portmann
 */
interface IUserDirectory
{
  /**
   * Add the group member to the group.
   *
   * @param groupName  the name identifying the group
   * @param memberType the group member type
   * @param memberName the group member name
   */
  void addGroupMember(String groupName, GroupMemberType memberType, String memberName)
    throws GroupNotFoundException, UserNotFoundException, ExistingGroupMemberException,
        SecurityServiceException;

  /**
   * Add the user to the group.
   *
   * @param groupName the name identifying the group
   * @param username  the username identifying the user
   */
  void addUserToGroup(String groupName, String username)
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
   * Create the new group.
   *
   * @param group the group
   */
  void createGroup(Group group)
    throws DuplicateGroupException, SecurityServiceException;

  /**
   * Create the new user.
   *
   * @param user            the user
   * @param expiredPassword create the user with its password expired
   * @param userLocked      create the user locked
   */
  void createUser(User user, boolean expiredPassword, boolean userLocked)
    throws DuplicateUserException, SecurityServiceException;

  /**
   * Delete the group.
   *
   * @param groupName the name identifying the group
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
   * @return the users whose attributes match the attribute criteria
   */
  List<User> findUsers(List<Attribute> attributes)
    throws InvalidAttributeException, SecurityServiceException;

  /**
   * Retrieve the authorised function codes for the user.
   *
   * @param username the username identifying the user
   *
   * @return the authorised function codes for the user
   */
  List<String> getFunctionCodesForUser(String username)
    throws UserNotFoundException, SecurityServiceException;

  /**
   * Retrieve the group.
   *
   * @param groupName the name identifying the group
   *
   * @return the group
   */
  Group getGroup(String groupName)
    throws GroupNotFoundException, SecurityServiceException;

  /**
   * Retrieve the group members for the group.
   *
   * @param groupName the name identifying the group
   *
   * @return the group members for the group
   */
  List<GroupMember> getGroupMembers(String groupName)
    throws GroupNotFoundException, SecurityServiceException;

  /**
   * Retrieve the group members for the group.
   *
   * @param groupName     the name identifying the group
   * @param filter        the optional filter to apply to the group members
   * @param sortDirection the optional sort direction to apply to the group members
   * @param pageIndex     the optional page index
   * @param pageSize      the optional page size
   *
   * @return the group members for the group
   */
  List<GroupMember> getGroupMembers(String groupName, String filter, SortDirection sortDirection,
      Integer pageIndex, Integer pageSize)
    throws GroupNotFoundException, SecurityServiceException;

  /**
   * Retrieve all the group names.
   *
   * @return the group names
   */
  List<String> getGroupNames()
    throws SecurityServiceException;

  /**
   * Retrieve the names identifying the groups the user is a member of.
   *
   * @param username the username identifying the user
   *
   * @return the names identifying the groups the user is a member of
   */
  List<String> getGroupNamesForUser(String username)
    throws UserNotFoundException, SecurityServiceException;

  /**
   * Retrieve all the groups.
   *
   * @return the groups
   */
  List<Group> getGroups()
    throws SecurityServiceException;

  /**
   * Retrieve the groups.
   *
   * @param filter        the optional filter to apply to the groups
   * @param sortDirection the optional sort direction to apply to the groups
   * @param pageIndex     the optional page index
   * @param pageSize      the optional page size
   *
   * @return the groups
   */
  List<Group> getGroups(String filter, SortDirection sortDirection, Integer pageIndex,
      Integer pageSize)
    throws SecurityServiceException;

  /**
   * Retrieve the groups the user is a member of.
   *
   * @param username the username identifying the user
   *
   * @return the groups the user is a member of
   */
  List<Group> getGroupsForUser(String username)
    throws UserNotFoundException, SecurityServiceException;

  /**
   * Retrieve the number of group members for the group.
   *
   * @param groupName the name identifying the group
   * @param filter    the optional filter to apply to the members
   *
   * @return the number of group members for the group
   */
  long getNumberOfGroupMembers(String groupName, String filter)
    throws GroupNotFoundException, SecurityServiceException;

  /**
   * Retrieve the number of groups
   *
   * @param filter the optional filter to apply to the groups
   *
   * @return the number of groups
   */
  long getNumberOfGroups(String filter)
    throws SecurityServiceException;

  /**
   * Retrieve the number of users.
   *
   * @param filter the optional filter to apply to the users
   *
   * @return the number of users
   */
  long getNumberOfUsers(String filter)
    throws SecurityServiceException;

  /**
   * Retrieve the codes for the roles that the user has been assigned.
   *
   * @param username the username identifying the user
   *
   * @return the codes for the roles that the user has been assigned
   */
  List<String> getRoleCodesForUser(String username)
    throws UserNotFoundException, SecurityServiceException;

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
   * @return the users
   */
  List<User> getUsers()
    throws SecurityServiceException;

  /**
   * Retrieve the users.
   *
   * @param filter        the optional filter to apply to the users
   * @param sortBy        the optional method used to sort the users e.g. by last name
   * @param sortDirection the optional sort direction to apply to the users
   * @param pageIndex     the optional page index
   * @param pageSize      the optional page size
   *
   * @return the users
   */
  List<User> getUsers(String filter, UserSortBy sortBy, SortDirection sortDirection,
      Integer pageIndex, Integer pageSize)
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
   * Is the user in the group?
   *
   * @param groupName the name identifying the group
   * @param username  the username identifying the user
   *
   * @return <code>true</code> if the user is a member of the group or <code>false</code>
   *         otherwise
   */
  boolean isUserInGroup(String groupName, String username)
    throws UserNotFoundException, GroupNotFoundException, SecurityServiceException;

  /**
   * Remove the group member from the group.
   *
   * @param groupName  the name identifying the group
   * @param memberType the group member type
   * @param memberName the group member name
   */
  void removeGroupMember(String groupName, GroupMemberType memberType, String memberName)
    throws GroupNotFoundException, GroupMemberNotFoundException, SecurityServiceException;

  /**
   * Remove the user from the group.
   *
   * @param groupName the name identifying the group
   * @param username  the username identifying the user
   */
  void removeUserFromGroup(String groupName, String username)
    throws UserNotFoundException, GroupNotFoundException, SecurityServiceException;

  /**
   * Does the user directory support administering groups.
   *
   * @return <code>true</code> if the user directory supports administering groups or
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
   * Update the group.
   *
   * @param group the group
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
