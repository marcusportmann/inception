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

// ~--- non-JDK imports --------------------------------------------------------

import digital.inception.core.util.PasswordUtil;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.util.StringUtils;

// ~--- JDK imports ------------------------------------------------------------

/**
 * The <code>InternalUserDirectory</code> class provides the internal user directory implementation.
 *
 * @author Marcus Portmann
 */
public class InternalUserDirectory extends UserDirectoryBase {

  /** The default maximum number of filtered groups. */
  private static final int DEFAULT_MAX_FILTERED_GROUPS = 100;

  /** The default maximum number of filtered group members. */
  private static final int DEFAULT_MAX_FILTERED_GROUP_MEMBERS = 100;

  /** The default maximum number of filtered users. */
  private static final int DEFAULT_MAX_FILTERED_USERS = 100;

  /** The default number of failed password attempts before the user is locked. */
  private static final int DEFAULT_MAX_PASSWORD_ATTEMPTS = 5;

  /** The default number of months before a user's password expires. */
  private static final int DEFAULT_PASSWORD_EXPIRY_MONTHS = 3;

  /** The default number of months to check password history against. */
  private static final int DEFAULT_PASSWORD_HISTORY_MONTHS = 12;

  /** The user directory capabilities common to all internal user directory instances. */
  private static final UserDirectoryCapabilities INTERNAL_USER_DIRECTORY_CAPABILITIES =
      new UserDirectoryCapabilities(true, true, true, true, true, true, true, true);

  /** The maximum number of filtered group members to return. */
  private int maxFilteredGroupMembers;

  /** The maximum number of filtered groups to return. */
  private int maxFilteredGroups;

  /** The maximum number of filtered users to return. */
  private int maxFilteredUsers;

  /** The maximum number of password attempts. */
  private int maxPasswordAttempts;

  /** The password expiry period in months. */
  private int passwordExpiryMonths;

  /** The password history period in months. */
  private int passwordHistoryMonths;

  /**
   * Constructs a new <code>InternalUserDirectory</code>.
   *
   * @param userDirectoryId the Universally Unique Identifier (UUID) uniquely identifying the user
   *     directory
   * @param parameters the parameters for the user directory
   * @param groupRepository the Group Repository
   * @param userRepository the User Repository
   * @param roleRepository the Role Repository
   */
  public InternalUserDirectory(
      UUID userDirectoryId,
      List<UserDirectoryParameter> parameters,
      GroupRepository groupRepository,
      UserRepository userRepository,
      RoleRepository roleRepository)
      throws SecurityServiceException {
    super(userDirectoryId, parameters, groupRepository, userRepository, roleRepository);

    try {
      if (UserDirectoryParameter.contains(parameters, "MaxPasswordAttempts")) {
        maxPasswordAttempts =
            UserDirectoryParameter.getIntegerValue(parameters, "MaxPasswordAttempts");
      } else {
        maxPasswordAttempts = DEFAULT_MAX_PASSWORD_ATTEMPTS;
      }

      if (UserDirectoryParameter.contains(parameters, "PasswordExpiryMonths")) {
        passwordExpiryMonths =
            UserDirectoryParameter.getIntegerValue(parameters, "PasswordExpiryMonths");
      } else {
        passwordExpiryMonths = DEFAULT_PASSWORD_EXPIRY_MONTHS;
      }

      if (UserDirectoryParameter.contains(parameters, "PasswordHistoryMonths")) {
        passwordHistoryMonths =
            UserDirectoryParameter.getIntegerValue(parameters, "PasswordHistoryMonths");
      } else {
        passwordHistoryMonths = DEFAULT_PASSWORD_HISTORY_MONTHS;
      }

      if (UserDirectoryParameter.contains(parameters, "MaxFilteredUsers")) {
        maxFilteredUsers = UserDirectoryParameter.getIntegerValue(parameters, "MaxFilteredUsers");
      } else {
        maxFilteredUsers = DEFAULT_MAX_FILTERED_USERS;
      }

      if (UserDirectoryParameter.contains(parameters, "MaxFilteredGroups")) {
        maxFilteredGroups = UserDirectoryParameter.getIntegerValue(parameters, "MaxFilteredGroups");
      } else {
        maxFilteredGroups = DEFAULT_MAX_FILTERED_GROUPS;
      }

      if (UserDirectoryParameter.contains(parameters, "MaxFilteredGroupMembers")) {
        maxFilteredGroupMembers =
            UserDirectoryParameter.getIntegerValue(parameters, "MaxFilteredGroupMembers");
      } else {
        maxFilteredGroupMembers = DEFAULT_MAX_FILTERED_GROUP_MEMBERS;
      }
    } catch (Throwable e) {
      throw new SecurityServiceException(
          "Failed to initialize the user directory (" + userDirectoryId + ")", e);
    }
  }

  /**
   * Add the group member to the group.
   *
   * @param groupName the name identifying the group
   * @param memberType the group member type
   * @param memberName the group member name
   */
  @Override
  public void addMemberToGroup(String groupName, GroupMemberType memberType, String memberName)
      throws GroupNotFoundException, UserNotFoundException, ExistingGroupMemberException,
          SecurityServiceException {
    if (memberType != GroupMemberType.USER) {
      throw new SecurityServiceException(
          "Unsupported group member type (" + memberType.description() + ")");
    }

    if (isUserInGroup(groupName, memberName)) {
      throw new ExistingGroupMemberException(memberType, memberName);
    }

    addUserToGroup(groupName, memberName);
  }

  /**
   * Add the role to the group.
   *
   * @param groupName the name identifying the group
   * @param roleCode the code uniquely identifying the role
   */
  @Override
  public void addRoleToGroup(String groupName, String roleCode)
      throws GroupNotFoundException, RoleNotFoundException, ExistingGroupRoleException,
          SecurityServiceException {
    try {
      Optional<UUID> groupIdOptional =
          getGroupRepository()
              .getIdByUserDirectoryIdAndNameIgnoreCase(getUserDirectoryId(), groupName);

      if (groupIdOptional.isEmpty()) {
        throw new GroupNotFoundException(groupName);
      }

      if (!getRoleRepository().existsById(roleCode)) {
        throw new RoleNotFoundException(roleCode);
      }

      if (getGroupRepository().countGroupRole(groupIdOptional.get(), roleCode) > 0) {
        throw new ExistingGroupRoleException(roleCode);
      }

      getGroupRepository().addRoleToGroup(groupIdOptional.get(), roleCode);
    } catch (GroupNotFoundException | RoleNotFoundException | ExistingGroupRoleException e) {
      throw e;
    } catch (Throwable e) {
      throw new SecurityServiceException(
          "Failed to add the role ("
              + roleCode
              + ") to the group ("
              + groupName
              + ") for the user directory ("
              + getUserDirectoryId()
              + ")",
          e);
    }
  }

  /**
   * Add the user to the group.
   *
   * @param groupName the name identifying the group
   * @param username the username identifying the user
   */
  @Override
  public void addUserToGroup(String groupName, String username)
      throws GroupNotFoundException, UserNotFoundException, SecurityServiceException {
    try {
      Optional<UUID> groupIdOptional =
          getGroupRepository()
              .getIdByUserDirectoryIdAndNameIgnoreCase(getUserDirectoryId(), groupName);

      if (groupIdOptional.isEmpty()) {
        throw new GroupNotFoundException(groupName);
      }

      Optional<UUID> userIdOptional =
          getUserRepository()
              .getIdByUserDirectoryIdAndUsernameIgnoreCase(getUserDirectoryId(), username);

      if (userIdOptional.isEmpty()) {
        throw new UserNotFoundException(username);
      }

      getGroupRepository().addUserToGroup(groupIdOptional.get(), userIdOptional.get());
    } catch (GroupNotFoundException | UserNotFoundException e) {
      throw e;
    } catch (Throwable e) {
      throw new SecurityServiceException(
          "Failed to add the user ("
              + username
              + ") to the group ("
              + groupName
              + ") for the user directory ("
              + getUserDirectoryId()
              + ")",
          e);
    }
  }

  /**
   * Administratively change the password for the user.
   *
   * @param username the username identifying the user
   * @param newPassword the new password
   * @param expirePassword expire the user's password
   * @param lockUser lock the user
   * @param resetPasswordHistory reset the user's password history
   * @param reason the reason for changing the password
   */
  @Override
  public void adminChangePassword(
      String username,
      String newPassword,
      boolean expirePassword,
      boolean lockUser,
      boolean resetPasswordHistory,
      PasswordChangeReason reason)
      throws UserNotFoundException, SecurityServiceException {
    try {
      Optional<UUID> userIdOptional =
          getUserRepository()
              .getIdByUserDirectoryIdAndUsernameIgnoreCase(getUserDirectoryId(), username);

      if (userIdOptional.isEmpty()) {
        throw new UserNotFoundException(username);
      }

      String newPasswordHash = PasswordUtil.createPasswordHash(newPassword);

      int passwordAttempts = 0;

      if (lockUser) {
        passwordAttempts = maxPasswordAttempts;
      }

      LocalDateTime passwordExpiry;

      if (expirePassword) {
        passwordExpiry = LocalDateTime.now();
      } else {
        passwordExpiry = LocalDateTime.now();
        passwordExpiry = passwordExpiry.plus(passwordExpiryMonths, ChronoUnit.MONTHS);
      }

      getUserRepository()
          .changePassword(
              userIdOptional.get(), newPasswordHash, passwordAttempts, Optional.of(passwordExpiry));

      if (resetPasswordHistory) {
        getUserRepository().resetPasswordHistory(userIdOptional.get());
      }

      getUserRepository().savePasswordInPasswordHistory(userIdOptional.get(), newPasswordHash);
    } catch (UserNotFoundException e) {
      throw e;
    } catch (Throwable e) {
      throw new SecurityServiceException(
          "Failed to change the password for the user ("
              + username
              + ") for the user directory ("
              + getUserDirectoryId()
              + ")",
          e);
    }
  }

  /**
   * Authenticate the user.
   *
   * @param username the username identifying the user
   * @param password the password being used to authenticate
   */
  @Override
  public void authenticate(String username, String password)
      throws AuthenticationFailedException, UserLockedException, ExpiredPasswordException,
          UserNotFoundException, SecurityServiceException {
    try {
      Optional<User> userOptional =
          getUserRepository()
              .findByUserDirectoryIdAndUsernameIgnoreCase(getUserDirectoryId(), username);

      if (userOptional.isEmpty()) {
        throw new UserNotFoundException(username);
      }

      User user = userOptional.get();

      if ((user.getPasswordAttempts() != null)
          && (user.getPasswordAttempts() >= maxPasswordAttempts)) {
        throw new UserLockedException(username);
      }

      String passwordHash = PasswordUtil.createPasswordHash(password);

      if (!user.getPassword().equals(passwordHash)) {
        if ((user.getPasswordAttempts() != null) && (user.getPasswordAttempts() != -1)) {
          getUserRepository().incrementPasswordAttempts(user.getId());
        }

        throw new AuthenticationFailedException(
            "Authentication failed for the user (" + username + ")");
      }

      if (user.hasPasswordExpired()) {
        throw new ExpiredPasswordException(username);
      }
    } catch (AuthenticationFailedException
        | UserNotFoundException
        | UserLockedException
        | ExpiredPasswordException e) {
      throw e;
    } catch (Throwable e) {
      throw new SecurityServiceException(
          "Failed to authenticate the user ("
              + username
              + ") for the user directory ("
              + getUserDirectoryId()
              + ")",
          e);
    }
  }

  /**
   * Change the password for the user.
   *
   * @param username the username identifying the user
   * @param password the password for the user that is used to authorise the operation
   * @param newPassword the new password
   */
  @Override
  public void changePassword(String username, String password, String newPassword)
      throws AuthenticationFailedException, UserLockedException, UserNotFoundException,
          ExistingPasswordException, SecurityServiceException {
    try {
      Optional<User> userOptional =
          getUserRepository()
              .findByUserDirectoryIdAndUsernameIgnoreCase(getUserDirectoryId(), username);

      if (userOptional.isEmpty()) {
        throw new UserNotFoundException(username);
      }

      User user = userOptional.get();

      if ((user.getPasswordAttempts() != null)
          && (user.getPasswordAttempts() > maxPasswordAttempts)) {
        throw new UserLockedException(username);
      }

      String passwordHash = PasswordUtil.createPasswordHash(password);
      String newPasswordHash = PasswordUtil.createPasswordHash(newPassword);

      if (!user.getPassword().equals(passwordHash)) {
        throw new AuthenticationFailedException(
            "Authentication failed while attempting to change the password for the user ("
                + username
                + ")");
      }

      if (isPasswordInHistory(user.getId(), newPasswordHash)) {
        throw new ExistingPasswordException(username);
      }

      LocalDateTime passwordExpiry = LocalDateTime.now();
      passwordExpiry = passwordExpiry.plus(passwordExpiryMonths, ChronoUnit.MONTHS);

      getUserRepository()
          .changePassword(user.getId(), newPasswordHash, 0, Optional.of(passwordExpiry));

      getUserRepository().savePasswordInPasswordHistory(user.getId(), newPasswordHash);
    } catch (AuthenticationFailedException
        | ExistingPasswordException
        | UserNotFoundException
        | UserLockedException e) {
      throw e;
    } catch (Throwable e) {
      throw new SecurityServiceException(
          "Failed to change the password for the user ("
              + username
              + ") for the user directory ("
              + getUserDirectoryId()
              + ")",
          e);
    }
  }

  /**
   * Create the new group.
   *
   * @param group the group
   */
  @Override
  public void createGroup(Group group) throws DuplicateGroupException, SecurityServiceException {
    try {
      if (getGroupRepository()
          .existsByUserDirectoryIdAndNameIgnoreCase(getUserDirectoryId(), group.getName())) {
        throw new DuplicateGroupException(group.getName());
      }

      group.setId(UUID.randomUUID());

      getGroupRepository().saveAndFlush(group);
    } catch (DuplicateGroupException e) {
      throw e;
    } catch (Throwable e) {
      throw new SecurityServiceException(
          "Failed to create the group ("
              + group.getName()
              + ") for the user directory ("
              + getUserDirectoryId()
              + ")",
          e);
    }
  }

  /**
   * Create the new user.
   *
   * @param user the user
   * @param expiredPassword create the user with its password expired
   * @param userLocked create the user locked
   */
  @Override
  public void createUser(User user, boolean expiredPassword, boolean userLocked)
      throws DuplicateUserException, SecurityServiceException {
    try {
      if (getUserRepository()
          .existsByUserDirectoryIdAndUsernameIgnoreCase(getUserDirectoryId(), user.getUsername())) {
        throw new DuplicateUserException(user.getUsername());
      }

      user.setId(UUID.randomUUID());

      if (!isNullOrEmpty(user.getPassword())) {
        user.setPassword(PasswordUtil.createPasswordHash(user.getPassword()));
      } else {
        user.setPassword(PasswordUtil.createPasswordHash(PasswordUtil.generateRandomPassword()));
      }

      if (userLocked) {
        user.setPasswordAttempts(maxPasswordAttempts);
      } else {
        user.setPasswordAttempts(0);
      }

      if (expiredPassword) {
        user.setPasswordExpiry(LocalDateTime.now());
      } else {
        LocalDateTime passwordExpiry = LocalDateTime.now();

        passwordExpiry = passwordExpiry.plus(passwordExpiryMonths, ChronoUnit.MONTHS);

        user.setPasswordExpiry(passwordExpiry);
      }

      getUserRepository().saveAndFlush(user);

      getUserRepository().savePasswordInPasswordHistory(user.getId(), user.getPassword());
    } catch (DuplicateUserException e) {
      throw e;
    } catch (Throwable e) {
      throw new SecurityServiceException(
          "Failed to create the user ("
              + user.getUsername()
              + ") for the user directory ("
              + getUserDirectoryId()
              + ")",
          e);
    }
  }

  /**
   * Delete the group.
   *
   * @param groupName the name identifying the group
   */
  @Override
  public void deleteGroup(String groupName)
      throws GroupNotFoundException, ExistingGroupMembersException, SecurityServiceException {
    try {
      Optional<UUID> groupIdOptional =
          getGroupRepository()
              .getIdByUserDirectoryIdAndNameIgnoreCase(getUserDirectoryId(), groupName);

      if (groupIdOptional.isEmpty()) {
        throw new GroupNotFoundException(groupName);
      }

      if (getGroupRepository().countUsersById(groupIdOptional.get()) > 0) {
        throw new ExistingGroupMembersException(groupName);
      }

      getGroupRepository().deleteById(groupIdOptional.get());
    } catch (GroupNotFoundException | ExistingGroupMembersException e) {
      throw e;
    } catch (Throwable e) {
      throw new SecurityServiceException(
          "Failed to delete the group ("
              + groupName
              + ") for the user directory ("
              + getUserDirectoryId()
              + ")",
          e);
    }
  }

  /**
   * Delete the user.
   *
   * @param username the username identifying the user
   */
  @Override
  public void deleteUser(String username) throws UserNotFoundException, SecurityServiceException {
    try {
      Optional<UUID> userIdOptional =
          getUserRepository()
              .getIdByUserDirectoryIdAndUsernameIgnoreCase(getUserDirectoryId(), username);

      if (userIdOptional.isEmpty()) {
        throw new UserNotFoundException(username);
      }

      getUserRepository().deleteById(userIdOptional.get());
    } catch (UserNotFoundException e) {
      throw e;
    } catch (Throwable e) {
      throw new SecurityServiceException(
          "Failed to delete the user ("
              + username
              + ") for the user directory ("
              + getUserDirectoryId()
              + ")",
          e);
    }
  }

  /**
   * Retrieve the users matching the attribute criteria.
   *
   * @param attributes the attribute criteria used to select the users
   * @return the users whose attributes match the attribute criteria
   */
  @Override
  public List<User> findUsers(List<Attribute> attributes)
      throws InvalidAttributeException, SecurityServiceException {
    try {
      User userCriteria = new User();

      userCriteria.setUserDirectoryId(getUserDirectoryId());

      for (Attribute attribute : attributes) {
        if (attribute.getName().equalsIgnoreCase("status")) {
          userCriteria.setStatus(UserStatus.fromCode(attribute.getIntegerValue()));
        } else if (attribute.getName().equalsIgnoreCase("email")) {
          userCriteria.setEmail(attribute.getValue());
        } else if (attribute.getName().equalsIgnoreCase("firstName")) {
          userCriteria.setFirstName(attribute.getValue());
        } else if (attribute.getName().equalsIgnoreCase("lastName")) {
          userCriteria.setLastName(attribute.getValue());
        } else if (attribute.getName().equalsIgnoreCase("phoneNumber")) {
          userCriteria.setPhoneNumber(attribute.getValue());
        } else if (attribute.getName().equalsIgnoreCase("mobileNumber")) {
          userCriteria.setMobileNumber(attribute.getValue());
        } else if (attribute.getName().equalsIgnoreCase("username")) {
          userCriteria.setUsername(attribute.getValue());
        } else {
          throw new InvalidAttributeException(attribute.getName());
        }
      }

      ExampleMatcher matcher =
          ExampleMatcher.matching()
              .withIgnoreNullValues()
              .withIgnoreCase()
              .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING);

      return getUserRepository().findAll(Example.of(userCriteria, matcher));
    } catch (InvalidAttributeException e) {
      throw e;
    } catch (Throwable e) {
      throw new SecurityServiceException(
          "Failed to find the users for the user directory (" + getUserDirectoryId() + ")", e);
    }
  }

  /**
   * Retrieve the capabilities the user directory supports.
   *
   * @return the capabilities the user directory supports
   */
  @Override
  public UserDirectoryCapabilities getCapabilities() {
    return INTERNAL_USER_DIRECTORY_CAPABILITIES;
  }

  /**
   * Retrieve the authorised function codes for the user.
   *
   * @param username the username identifying the user
   * @return the authorised function codes for the user
   */
  @Override
  public List<String> getFunctionCodesForUser(String username)
      throws UserNotFoundException, SecurityServiceException {
    try {
      Optional<UUID> userIdOptional =
          getUserRepository()
              .getIdByUserDirectoryIdAndUsernameIgnoreCase(getUserDirectoryId(), username);

      if (userIdOptional.isEmpty()) {
        throw new UserNotFoundException(username);
      }

      return getUserRepository().getFunctionCodesByUserId(userIdOptional.get());
    } catch (UserNotFoundException e) {
      throw e;
    } catch (Throwable e) {
      throw new SecurityServiceException(
          "Failed to retrieve the function codes for the user ("
              + username
              + ") for the user directory ("
              + getUserDirectoryId()
              + ")",
          e);
    }
  }

  /**
   * Retrieve the group.
   *
   * @param groupName the name identifying the group
   * @return the group
   */
  @Override
  public Group getGroup(String groupName) throws GroupNotFoundException, SecurityServiceException {
    try {
      Optional<Group> groupOptional =
          getGroupRepository()
              .findByUserDirectoryIdAndNameIgnoreCase(getUserDirectoryId(), groupName);

      if (groupOptional.isPresent()) {
        return groupOptional.get();
      } else {
        throw new GroupNotFoundException(groupName);
      }
    } catch (GroupNotFoundException e) {
      throw e;
    } catch (Throwable e) {
      throw new SecurityServiceException(
          "Failed to retrieve the group ("
              + groupName
              + ") for the user directory ("
              + getUserDirectoryId()
              + ")",
          e);
    }
  }

  /**
   * Retrieve all the group names.
   *
   * @return the group names
   */
  @Override
  public List<String> getGroupNames() throws SecurityServiceException {
    try {
      return getGroupRepository().getNamesByUserDirectoryId(getUserDirectoryId());
    } catch (Throwable e) {
      throw new SecurityServiceException(
          "Failed to retrieve the group names for the user directory ("
              + getUserDirectoryId()
              + ")",
          e);
    }
  }

  /**
   * Retrieve the names identifying the groups the user is a member of.
   *
   * @param username the username identifying the user
   * @return the names identifying the groups the user is a member of
   */
  @Override
  public List<String> getGroupNamesForUser(String username)
      throws UserNotFoundException, SecurityServiceException {
    try {
      Optional<UUID> userIdOptional =
          getUserRepository()
              .getIdByUserDirectoryIdAndUsernameIgnoreCase(getUserDirectoryId(), username);

      if (userIdOptional.isEmpty()) {
        throw new UserNotFoundException(username);
      }

      return getUserRepository().getGroupNamesByUserId(userIdOptional.get());
    } catch (UserNotFoundException e) {
      throw e;
    } catch (Throwable e) {
      throw new SecurityServiceException(
          "Failed to retrieve the names identifying the groups the user ("
              + username
              + ") is a member of for the user directory ("
              + getUserDirectoryId()
              + ")",
          e);
    }
  }

  /**
   * Retrieve all the groups.
   *
   * @return the groups
   */
  @Override
  public List<Group> getGroups() throws SecurityServiceException {
    try {
      return getGroupRepository().findByUserDirectoryId(getUserDirectoryId());
    } catch (Throwable e) {
      throw new SecurityServiceException(
          "Failed to retrieve the groups for the user directory (" + getUserDirectoryId() + ")", e);
    }
  }

  /**
   * Retrieve the groups.
   *
   * @param filter the optional filter to apply to the groups
   * @param sortDirection the optional sort direction to apply to the groups
   * @param pageIndex the optional page index
   * @param pageSize the optional page size
   * @return the groups
   */
  @Override
  public List<Group> getGroups(
      String filter, SortDirection sortDirection, Integer pageIndex, Integer pageSize)
      throws SecurityServiceException {
    try {
      if (pageIndex == null) {
        pageIndex = 0;
      }

      if (pageSize == null) {
        pageSize = maxFilteredGroups;
      }

      Pageable pageable =
          PageRequest.of(
              pageIndex,
              (pageSize > maxFilteredGroups) ? maxFilteredGroups : pageSize,
              (sortDirection == SortDirection.ASCENDING) ? Sort.Direction.ASC : Sort.Direction.DESC,
              "name");

      if (StringUtils.isEmpty(filter)) {
        return getGroupRepository().findByUserDirectoryId(getUserDirectoryId(), pageable);
      } else {
        return getGroupRepository()
            .findFiltered(getUserDirectoryId(), "%" + filter + "%", pageable);
      }

    } catch (Throwable e) {
      throw new SecurityServiceException(
          "Failed to retrieve the filtered groups for the user directory ("
              + getUserDirectoryId()
              + ")",
          e);
    }
  }

  /**
   * Retrieve the groups the user is a member of.
   *
   * @param username the username identifying the user
   * @return the groups the user is a member of
   */
  @Override
  public List<Group> getGroupsForUser(String username)
      throws UserNotFoundException, SecurityServiceException {
    try {
      Optional<UUID> userIdOptional =
          getUserRepository()
              .getIdByUserDirectoryIdAndUsernameIgnoreCase(getUserDirectoryId(), username);

      if (userIdOptional.isEmpty()) {
        throw new UserNotFoundException(username);
      }

      return getUserRepository().getGroupsByUserId(userIdOptional.get());
    } catch (UserNotFoundException e) {
      throw e;
    } catch (Throwable e) {
      throw new SecurityServiceException(
          "Failed to retrieve the groups the user is a member of ("
              + username
              + ") for the user directory ("
              + getUserDirectoryId()
              + ")",
          e);
    }
  }

  /**
   * Retrieve the group members for the group.
   *
   * @param groupName the name identifying the group
   * @return the group members for the group
   */
  @Override
  public List<GroupMember> getMembersForGroup(String groupName)
      throws GroupNotFoundException, SecurityServiceException {
    try {
      Optional<UUID> groupIdOptional =
          getGroupRepository()
              .getIdByUserDirectoryIdAndNameIgnoreCase(getUserDirectoryId(), groupName);

      if (groupIdOptional.isEmpty()) {
        throw new GroupNotFoundException(groupName);
      }

      List<String> usernames =
          getGroupRepository().getUsernamesForGroup(getUserDirectoryId(), groupIdOptional.get());

      List<GroupMember> groupMembers = new ArrayList<>();

      for (String username : usernames) {
        groupMembers.add(
            new GroupMember(getUserDirectoryId(), groupName, GroupMemberType.USER, username));
      }

      return groupMembers;
    } catch (GroupNotFoundException e) {
      throw e;
    } catch (Throwable e) {
      throw new SecurityServiceException(
          "Failed to retrieve the group members for the group ("
              + groupName
              + ") for the user directory ("
              + getUserDirectoryId()
              + ")",
          e);
    }
  }

  /**
   * Retrieve the group members for the group.
   *
   * @param groupName the name identifying the group
   * @param filter the optional filter to apply to the group members
   * @param sortDirection the optional sort direction to apply to the group members
   * @param pageIndex the optional page index
   * @param pageSize the optional page size
   * @return the group members for the group
   */
  @Override
  public List<GroupMember> getMembersForGroup(
      String groupName,
      String filter,
      SortDirection sortDirection,
      Integer pageIndex,
      Integer pageSize)
      throws GroupNotFoundException, SecurityServiceException {
    try {
      Optional<UUID> groupIdOptional =
          getGroupRepository()
              .getIdByUserDirectoryIdAndNameIgnoreCase(getUserDirectoryId(), groupName);

      if (groupIdOptional.isEmpty()) {
        throw new GroupNotFoundException(groupName);
      }

      if (pageIndex == null) {
        pageIndex = 0;
      }

      if (pageSize == null) {
        pageSize = maxFilteredGroups;
      }

      Pageable pageable =
          PageRequest.of(
              pageIndex, (pageSize > maxFilteredGroupMembers) ? maxFilteredGroupMembers : pageSize);

      List<String> usernames;

      if (StringUtils.isEmpty(filter)) {
        usernames =
            getGroupRepository()
                .getUsernamesForGroup(getUserDirectoryId(), groupIdOptional.get(), pageable);
      } else {
        usernames =
            getGroupRepository()
                .getFilteredUsernamesForGroup(
                    getUserDirectoryId(), groupIdOptional.get(), "%" + filter + "%", pageable);
      }

      List<GroupMember> groupMembers = new ArrayList<>();

      for (String username : usernames) {
        groupMembers.add(
            new GroupMember(getUserDirectoryId(), groupName, GroupMemberType.USER, username));
      }

      if ((sortDirection == null) || (sortDirection == SortDirection.ASCENDING)) {
        groupMembers.sort(Comparator.comparing(GroupMember::getMemberName));
      } else {
        groupMembers.sort(Comparator.comparing(GroupMember::getMemberName).reversed());
      }

      return groupMembers;
    } catch (GroupNotFoundException e) {
      throw e;
    } catch (Throwable e) {
      throw new SecurityServiceException(
          "Failed to retrieve the group members for the group ("
              + groupName
              + ") for the user directory ("
              + getUserDirectoryId()
              + ")",
          e);
    }
  }

  /**
   * Retrieve the number of groups.
   *
   * @param filter the optional filter to apply to the groups
   * @return the number of groups
   */
  @Override
  public long getNumberOfGroups(String filter) throws SecurityServiceException {
    try {
      if (StringUtils.isEmpty(filter)) {
        return getGroupRepository().countByUserDirectoryId(getUserDirectoryId());
      } else {
        return getGroupRepository().countFiltered(getUserDirectoryId(), "%" + filter + "%");
      }
    } catch (Throwable e) {
      throw new SecurityServiceException(
          "Failed to retrieve the number of groups for the user directory ("
              + getUserDirectoryId()
              + ")",
          e);
    }
  }

  /**
   * Retrieve the number of group members for the group.
   *
   * @param groupName the name identifying the group
   * @param filter the optional filter to apply to the members
   * @return the number of group members for the group
   */
  @Override
  public long getNumberOfMembersForGroup(String groupName, String filter)
      throws GroupNotFoundException, SecurityServiceException {
    try {
      Optional<UUID> groupIdOptional =
          getGroupRepository()
              .getIdByUserDirectoryIdAndNameIgnoreCase(getUserDirectoryId(), groupName);

      if (groupIdOptional.isEmpty()) {
        throw new GroupNotFoundException(groupName);
      }

      if (StringUtils.isEmpty(filter)) {
        return getGroupRepository()
            .countUsernamesForGroup(getUserDirectoryId(), groupIdOptional.get());
      } else {
        return getGroupRepository()
            .countFilteredUsernamesForGroup(getUserDirectoryId(), groupIdOptional.get(), filter);
      }
    } catch (GroupNotFoundException e) {
      throw e;
    } catch (Throwable e) {
      throw new SecurityServiceException(
          "Failed to retrieve the number of group members for the group ("
              + groupName
              + ") for the user directory ("
              + getUserDirectoryId()
              + ")",
          e);
    }
  }

  /**
   * Retrieve the number of users.
   *
   * @param filter the optional filter to apply to the users
   * @return the number of users
   */
  @Override
  public long getNumberOfUsers(String filter) throws SecurityServiceException {
    try {
      if (StringUtils.isEmpty(filter)) {
        return getUserRepository().countByUserDirectoryId(getUserDirectoryId());
      } else {
        return getUserRepository().countFiltered(getUserDirectoryId(), "%" + filter + "%");
      }
    } catch (Throwable e) {
      throw new SecurityServiceException(
          "Failed to retrieve the number of users for the user directory ("
              + getUserDirectoryId()
              + ")",
          e);
    }
  }

  /**
   * Retrieve the codes for the roles that have been assigned to the group.
   *
   * @param groupName the name identifying the group
   * @return the codes for the roles that have been assigned to the group
   */
  @Override
  public List<String> getRoleCodesForGroup(String groupName)
      throws GroupNotFoundException, SecurityServiceException {
    try {
      Optional<UUID> groupIdOptional =
          getGroupRepository()
              .getIdByUserDirectoryIdAndNameIgnoreCase(getUserDirectoryId(), groupName);

      if (groupIdOptional.isEmpty()) {
        throw new GroupNotFoundException(groupName);
      }

      return getGroupRepository().getRoleCodesByGroupId(groupIdOptional.get());
    } catch (GroupNotFoundException e) {
      throw e;
    } catch (Throwable e) {
      throw new SecurityServiceException(
          "Failed to retrieve the role codes for the group ("
              + groupName
              + ") for the user directory ("
              + getUserDirectoryId()
              + ")",
          e);
    }
  }

  /**
   * Retrieve the codes for the roles that the user has been assigned.
   *
   * @param username the username identifying the user
   * @return the codes for the roles that the user has been assigned
   */
  @Override
  public List<String> getRoleCodesForUser(String username)
      throws UserNotFoundException, SecurityServiceException {
    try {
      Optional<UUID> userIdOptional =
          getUserRepository()
              .getIdByUserDirectoryIdAndUsernameIgnoreCase(getUserDirectoryId(), username);

      if (userIdOptional.isEmpty()) {
        throw new UserNotFoundException(username);
      }

      return getUserRepository().getRoleCodesByUserId(userIdOptional.get());
    } catch (UserNotFoundException e) {
      throw e;
    } catch (Throwable e) {
      throw new SecurityServiceException(
          "Failed to retrieve the role codes for the user ("
              + username
              + ") for the user directory ("
              + getUserDirectoryId()
              + ")",
          e);
    }
  }

  /**
   * Retrieve the roles that have been assigned to the group.
   *
   * @param groupName the name identifying the group
   * @return the roles that have been assigned to the group
   */
  @Override
  public List<GroupRole> getRolesForGroup(String groupName)
      throws GroupNotFoundException, SecurityServiceException {
    try {
      Optional<UUID> groupIdOptional =
          getGroupRepository()
              .getIdByUserDirectoryIdAndNameIgnoreCase(getUserDirectoryId(), groupName);

      if (groupIdOptional.isEmpty()) {
        throw new GroupNotFoundException(groupName);
      }

      List<GroupRole> groupRoles = new ArrayList<>();

      for (String roleCode : getGroupRepository().getRoleCodesByGroupId(groupIdOptional.get())) {
        groupRoles.add(new GroupRole(getUserDirectoryId(), groupName, roleCode));
      }

      return groupRoles;
    } catch (GroupNotFoundException e) {
      throw e;
    } catch (Throwable e) {
      throw new SecurityServiceException(
          "Failed to retrieve the roles for the group ("
              + groupName
              + ") for the user directory ("
              + getUserDirectoryId()
              + ")",
          e);
    }
  }

  /**
   * Retrieve the user.
   *
   * @param username the username identifying the user
   * @return the user
   */
  @Override
  public User getUser(String username) throws UserNotFoundException, SecurityServiceException {
    try {
      Optional<User> userOptional =
          getUserRepository()
              .findByUserDirectoryIdAndUsernameIgnoreCase(getUserDirectoryId(), username);

      if (userOptional.isPresent()) {
        return userOptional.get();
      } else {
        throw new UserNotFoundException(username);
      }
    } catch (UserNotFoundException e) {
      throw e;
    } catch (Throwable e) {
      throw new SecurityServiceException(
          "Failed to retrieve the user ("
              + username
              + ") for the user directory ("
              + getUserDirectoryId()
              + ")",
          e);
    }
  }

  /**
   * Retrieve the full name for the user.
   *
   * @param username the username identifying the user
   * @return the full name for the user
   */
  @Override
  public String getUserFullName(String username)
      throws UserNotFoundException, SecurityServiceException {
    try {
      Optional<FirstNameAndLastName> firstNameAndLastNameOptional =
          getUserRepository()
              .getFirstNameAndLastNameByUserDirectoryIdAndUsernameIgnoreCase(
                  getUserDirectoryId(), username);

      if (firstNameAndLastNameOptional.isPresent()) {
        StringBuilder buffer = new StringBuilder(firstNameAndLastNameOptional.get().getFirstName());

        if (!StringUtils.isEmpty(firstNameAndLastNameOptional.get().getLastName())) {
          if (buffer.length() > 0) {
            buffer.append(" ");
          }

          buffer.append(firstNameAndLastNameOptional.get().getLastName());
        }

        return buffer.toString();
      } else {
        throw new UserNotFoundException(username);
      }
    } catch (UserNotFoundException e) {
      throw e;
    } catch (Throwable e) {
      throw new SecurityServiceException(
          "Failed to retrieve the full name for the user ("
              + username
              + ") for the user directory ("
              + getUserDirectoryId()
              + ")",
          e);
    }
  }

  /**
   * Retrieve all the users.
   *
   * @return the users
   */
  @Override
  public List<User> getUsers() throws SecurityServiceException {
    try {
      return getUserRepository().findByUserDirectoryId(getUserDirectoryId());
    } catch (Throwable e) {
      throw new SecurityServiceException(
          "Failed to retrieve the users for the user directory (" + getUserDirectoryId() + ")", e);
    }
  }

  /**
   * Retrieve the users.
   *
   * @param filter the optional filter to apply to the users
   * @param sortBy the optional method used to sort the users e.g. by last name
   * @param sortDirection the optional sort direction to apply to the users
   * @param pageIndex the optional page index
   * @param pageSize the optional page size
   * @return the users
   */
  @Override
  public List<User> getUsers(
      String filter,
      UserSortBy sortBy,
      SortDirection sortDirection,
      Integer pageIndex,
      Integer pageSize)
      throws SecurityServiceException {
    try {
      Pageable pageable = null;

      if (pageIndex == null) {
        pageIndex = 0;
      }

      if (pageSize == null) {
        pageSize = maxFilteredUsers;
      }

      if (sortBy == UserSortBy.USERNAME) {
        pageable =
            PageRequest.of(
                pageIndex,
                (pageSize > maxFilteredUsers) ? maxFilteredUsers : pageSize,
                (sortDirection == SortDirection.ASCENDING)
                    ? Sort.Direction.ASC
                    : Sort.Direction.DESC,
                "username");
      } else if (sortBy == UserSortBy.FIRST_NAME) {
        pageable =
            PageRequest.of(
                pageIndex,
                (pageSize > maxFilteredUsers) ? maxFilteredUsers : pageSize,
                (sortDirection == SortDirection.ASCENDING)
                    ? Sort.Direction.ASC
                    : Sort.Direction.DESC,
                "firstName");
      } else if (sortBy == UserSortBy.LAST_NAME) {
        pageable =
            PageRequest.of(
                pageIndex,
                (pageSize > maxFilteredUsers) ? maxFilteredUsers : pageSize,
                (sortDirection == SortDirection.ASCENDING)
                    ? Sort.Direction.ASC
                    : Sort.Direction.DESC,
                "lastName");
      }

      if (StringUtils.isEmpty(filter)) {
        return getUserRepository().findByUserDirectoryId(getUserDirectoryId(), pageable);
      } else {
        return getUserRepository().findFiltered(getUserDirectoryId(), "%" + filter + "%", pageable);
      }

    } catch (Throwable e) {
      throw new SecurityServiceException(
          "Failed to retrieve the filtered users for the user directory ("
              + getUserDirectoryId()
              + ")",
          e);
    }
  }

  /**
   * Does the user with the specified username exist?
   *
   * @param username the username identifying the user
   * @return <code>true</code> if a user with specified username exists or <code>false</code>
   *     otherwise
   */
  @Override
  public boolean isExistingUser(String username) throws SecurityServiceException {
    try {
      return getUserRepository()
          .existsByUserDirectoryIdAndUsernameIgnoreCase(getUserDirectoryId(), username);
    } catch (Throwable e) {
      throw new SecurityServiceException(
          "Failed to check whether the user ("
              + username
              + ") is an existing user for the user directory ("
              + getUserDirectoryId()
              + ")",
          e);
    }
  }

  /**
   * Is the password, given by the specified password hash, a historical password that cannot be
   * reused for a period of time i.e. was the password used previously in the last X months.
   *
   * @param userId the Universally Unique Identifier (UUID) uniquely identifying the user
   * @param passwordHash the password hash
   * @return <code>true</code> if the password was previously used and cannot be reused for a period
   *     of time or <code>false</code> otherwise
   */
  private boolean isPasswordInHistory(UUID userId, String passwordHash) {
    LocalDateTime after = LocalDateTime.now();
    after = after.minus(passwordHistoryMonths, ChronoUnit.MONTHS);

    return getUserRepository().countPasswordHistory(userId, after, passwordHash) > 0;
  }

  /**
   * Is the user in the group?
   *
   * @param groupName the name identifying the group
   * @param username the username identifying the user
   * @return <code>true</code> if the user is a member of the group or <code>false</code> otherwise
   */
  @Override
  public boolean isUserInGroup(String groupName, String username)
      throws UserNotFoundException, GroupNotFoundException, SecurityServiceException {
    try {
      Optional<UUID> userIdOptional =
          getUserRepository()
              .getIdByUserDirectoryIdAndUsernameIgnoreCase(getUserDirectoryId(), username);

      if (userIdOptional.isEmpty()) {
        throw new UserNotFoundException(username);
      }

      Optional<UUID> groupIdOptional =
          getGroupRepository()
              .getIdByUserDirectoryIdAndNameIgnoreCase(getUserDirectoryId(), groupName);

      if (groupIdOptional.isEmpty()) {
        throw new GroupNotFoundException(groupName);
      }

      return getUserRepository().isUserInGroup(userIdOptional.get(), groupIdOptional.get());
    } catch (UserNotFoundException | GroupNotFoundException e) {
      throw e;
    } catch (Throwable e) {
      throw new SecurityServiceException(
          "Failed to check if the user ("
              + username
              + ") is in the group ("
              + groupName
              + ") for the user directory ("
              + getUserDirectoryId()
              + ")",
          e);
    }
  }

  /**
   * Remove the group member from the group.
   *
   * @param groupName the name identifying the group
   * @param memberType the group member type
   * @param memberName the group member name
   */
  @Override
  public void removeMemberFromGroup(String groupName, GroupMemberType memberType, String memberName)
      throws GroupNotFoundException, GroupMemberNotFoundException, SecurityServiceException {
    if (memberType != GroupMemberType.USER) {
      throw new SecurityServiceException(
          "Unsupported group member type (" + memberType.description() + ")");
    }

    try {
      removeUserFromGroup(groupName, memberName);
    } catch (UserNotFoundException e) {
      throw new GroupMemberNotFoundException(memberType, memberName);
    }
  }

  /**
   * Remove the role from the group.
   *
   * @param groupName the name identifying the group
   * @param roleCode the code uniquely identifying the role
   */
  @Override
  public void removeRoleFromGroup(String groupName, String roleCode)
      throws GroupNotFoundException, GroupRoleNotFoundException, SecurityServiceException {
    try {
      Optional<UUID> groupIdOptional =
          getGroupRepository()
              .getIdByUserDirectoryIdAndNameIgnoreCase(getUserDirectoryId(), groupName);

      if (groupIdOptional.isEmpty()) {
        throw new GroupNotFoundException(groupName);
      }

      if (getGroupRepository().removeRoleFromGroup(groupIdOptional.get(), roleCode) == 0) {
        throw new GroupRoleNotFoundException(roleCode);
      }
    } catch (GroupNotFoundException | GroupRoleNotFoundException e) {
      throw e;
    } catch (Throwable e) {
      throw new SecurityServiceException(
          "Failed to remove the role ("
              + roleCode
              + ") from the group ("
              + groupName
              + ") for the user directory ("
              + getUserDirectoryId()
              + ")",
          e);
    }
  }

  /**
   * Remove the user from the group.
   *
   * @param groupName the name identifying the group
   * @param username the username identifying the user
   */
  @Override
  public void removeUserFromGroup(String groupName, String username)
      throws GroupNotFoundException, UserNotFoundException, SecurityServiceException {
    try {
      Optional<UUID> groupIdOptional =
          getGroupRepository()
              .getIdByUserDirectoryIdAndNameIgnoreCase(getUserDirectoryId(), groupName);

      if (groupIdOptional.isEmpty()) {
        throw new GroupNotFoundException(groupName);
      }

      Optional<UUID> userIdOptional =
          getUserRepository()
              .getIdByUserDirectoryIdAndUsernameIgnoreCase(getUserDirectoryId(), username);

      if (userIdOptional.isEmpty()) {
        throw new UserNotFoundException(username);
      }

      getGroupRepository().removeUserFromGroup(groupIdOptional.get(), userIdOptional.get());
    } catch (GroupNotFoundException | UserNotFoundException e) {
      throw e;
    } catch (Throwable e) {
      throw new SecurityServiceException(
          "Failed to remove the user ("
              + username
              + ") from the group ("
              + groupName
              + ") for the user directory ("
              + getUserDirectoryId()
              + ")",
          e);
    }
  }

  /**
   * Reset the password for the user.
   *
   * @param username the username identifying the user
   * @param newPassword the new password
   */
  @Override
  public void resetPassword(String username, String newPassword)
      throws UserNotFoundException, UserLockedException, ExistingPasswordException,
          SecurityServiceException {
    try {
      Optional<User> userOptional =
          getUserRepository()
              .findByUserDirectoryIdAndUsernameIgnoreCase(getUserDirectoryId(), username);

      if (userOptional.isEmpty()) {
        throw new UserNotFoundException(username);
      }

      User user = userOptional.get();

      if ((user.getPasswordAttempts() != null)
          && (user.getPasswordAttempts() > maxPasswordAttempts)) {
        throw new UserLockedException(username);
      }

      String newPasswordHash = PasswordUtil.createPasswordHash(newPassword);

      if (isPasswordInHistory(user.getId(), newPasswordHash)) {
        throw new ExistingPasswordException(username);
      }

      LocalDateTime passwordExpiry = LocalDateTime.now();
      passwordExpiry = passwordExpiry.plus(passwordExpiryMonths, ChronoUnit.MONTHS);

      getUserRepository().resetPassword(user.getId(), newPasswordHash, passwordExpiry);

      getUserRepository().savePasswordInPasswordHistory(user.getId(), newPasswordHash);
    } catch (UserNotFoundException | UserLockedException | ExistingPasswordException e) {
      throw e;
    } catch (Throwable e) {
      throw new SecurityServiceException(
          "Failed to reset the password for the user ("
              + username
              + ") for the user directory ("
              + getUserDirectoryId()
              + ")",
          e);
    }
  }

  /**
   * Update the group.
   *
   * @param group the group
   */
  @Override
  public void updateGroup(Group group) throws GroupNotFoundException, SecurityServiceException {
    try {
      Optional<Group> groupOptional =
          getGroupRepository()
              .findByUserDirectoryIdAndNameIgnoreCase(group.getUserDirectoryId(), group.getName());

      if (groupOptional.isEmpty()) {
        throw new GroupNotFoundException(group.getName());
      }

      Group existingGroup = groupOptional.get();

      existingGroup.setDescription(group.getDescription());

      getGroupRepository().saveAndFlush(existingGroup);
    } catch (GroupNotFoundException e) {
      throw e;
    } catch (Throwable e) {
      throw new SecurityServiceException(
          "Failed to update the group ("
              + group.getName()
              + ") for the user directory ("
              + getUserDirectoryId()
              + ")",
          e);
    }
  }

  /**
   * Update the user.
   *
   * @param user the user
   * @param expirePassword expire the user's password as part of the update
   * @param lockUser lock the user as part of the update
   */
  @Override
  public void updateUser(User user, boolean expirePassword, boolean lockUser)
      throws UserNotFoundException, SecurityServiceException {
    try {
      Optional<User> userOptional =
          getUserRepository()
              .findByUserDirectoryIdAndUsernameIgnoreCase(
                  user.getUserDirectoryId(), user.getUsername());

      if (userOptional.isEmpty()) {
        throw new UserNotFoundException(user.getUsername());
      }

      User existingUser = userOptional.get();

      if (user.getFirstName() != null) {
        existingUser.setFirstName(user.getFirstName());
      }

      if (user.getLastName() != null) {
        existingUser.setLastName(user.getLastName());
      }

      if (user.getEmail() != null) {
        existingUser.setEmail(user.getEmail());
      }

      if (user.getPhoneNumber() != null) {
        existingUser.setPhoneNumber(user.getPhoneNumber());
      }

      if (user.getMobileNumber() != null) {
        existingUser.setMobileNumber(user.getMobileNumber());
      }

      if (!StringUtils.isEmpty(user.getPassword())) {
        existingUser.setPassword(PasswordUtil.createPasswordHash(user.getPassword()));
      }

      if (lockUser) {
        existingUser.setPasswordAttempts(maxPasswordAttempts);
      } else if (user.getPasswordAttempts() != null) {
        existingUser.setPasswordAttempts(user.getPasswordAttempts());
      }

      if (expirePassword) {
        existingUser.setPasswordExpiry(LocalDateTime.now());
      } else if (user.getPasswordExpiry() != null) {
        existingUser.setPasswordExpiry(user.getPasswordExpiry());
      }

      getUserRepository().saveAndFlush(existingUser);
    } catch (UserNotFoundException e) {
      throw e;
    } catch (Throwable e) {
      throw new SecurityServiceException(
          "Failed to update the user ("
              + user.getUsername()
              + ") for the user directory ("
              + getUserDirectoryId()
              + ")",
          e);
    }
  }
}
