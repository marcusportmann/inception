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

import digital.inception.core.util.PasswordUtil;

import org.springframework.data.domain.*;
import org.springframework.util.StringUtils;

//~--- JDK imports ------------------------------------------------------------

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import java.util.List;
import java.util.Optional;

/**
 * The <code>InternalUserDirectory</code> class provides the internal user directory implementation.
 *
 * @author Marcus Portmann
 */
public class InternalUserDirectory extends UserDirectoryBase
{
  /**
   * The default maximum number of filtered groups.
   */
  private static final int DEFAULT_MAX_FILTERED_GROUPS = 100;

  /**
   * The default maximum number of filtered users.
   */
  private static final int DEFAULT_MAX_FILTERED_USERS = 100;

  /**
   * The default number of failed password attempts before the user is locked.
   */
  private static final int DEFAULT_MAX_PASSWORD_ATTEMPTS = 5;

  /**
   * The default number of months before a user's password expires.
   */
  private static final int DEFAULT_PASSWORD_EXPIRY_MONTHS = 3;

  /**
   * The default number of months to check password history against.
   */
  private static final int DEFAULT_PASSWORD_HISTORY_MONTHS = 12;

  /**
   * The maximum number of filtered groups to return.
   */
  private int maxFilteredGroups;

  /**
   * The maximum number of filtered users to return.
   */
  private int maxFilteredUsers;

  /**
   * The maximum number of password attempts.
   */
  private int maxPasswordAttempts;

  /**
   * The password expiry period in months.
   */
  private int passwordExpiryMonths;

  /**
   * The password history period in months.
   */
  private int passwordHistoryMonths;

  /**
   * Constructs a new <code>InternalUserDirectory</code>.
   *
   * @param userDirectoryId the ID used to uniquely identify the user directory
   * @param parameters      the parameters for the user directory
   * @param groupRepository the Group Repository
   * @param userRepository  the User Repository
   */
  public InternalUserDirectory(Long userDirectoryId, List<UserDirectoryParameter> parameters,
      GroupRepository groupRepository, UserRepository userRepository)
    throws SecurityServiceException
  {
    super(userDirectoryId, parameters, groupRepository, userRepository);

    try
    {
      if (UserDirectoryParameter.contains(parameters, "MaxPasswordAttempts"))
      {
        maxPasswordAttempts = UserDirectoryParameter.getIntegerValue(parameters,
            "MaxPasswordAttempts");
      }
      else
      {
        maxPasswordAttempts = DEFAULT_MAX_PASSWORD_ATTEMPTS;
      }

      if (UserDirectoryParameter.contains(parameters, "PasswordExpiryMonths"))
      {
        passwordExpiryMonths = UserDirectoryParameter.getIntegerValue(parameters,
            "PasswordExpiryMonths");
      }
      else
      {
        passwordExpiryMonths = DEFAULT_PASSWORD_EXPIRY_MONTHS;
      }

      if (UserDirectoryParameter.contains(parameters, "PasswordHistoryMonths"))
      {
        passwordHistoryMonths = UserDirectoryParameter.getIntegerValue(parameters,
            "PasswordHistoryMonths");
      }
      else
      {
        passwordHistoryMonths = DEFAULT_PASSWORD_HISTORY_MONTHS;
      }

      if (UserDirectoryParameter.contains(parameters, "MaxFilteredUsers"))
      {
        maxFilteredUsers = UserDirectoryParameter.getIntegerValue(parameters, "MaxFilteredUsers");
      }
      else
      {
        maxFilteredUsers = DEFAULT_MAX_FILTERED_USERS;
      }

      if (UserDirectoryParameter.contains(parameters, "MaxFilteredGroups"))
      {
        maxFilteredGroups = UserDirectoryParameter.getIntegerValue(parameters, "MaxFilteredGroups");
      }
      else
      {
        maxFilteredGroups = DEFAULT_MAX_FILTERED_GROUPS;
      }
    }
    catch (Throwable e)
    {
      throw new SecurityServiceException(String.format(
          "Failed to initialize the user directory (%s)", userDirectoryId), e);
    }
  }

  /**
   * Add the user to the group.
   *
   * @param groupName the name identifying the group
   * @param username  the username identifying the user
   */
  @Override
  public void addUserToGroup(String groupName, String username)
    throws UserNotFoundException, GroupNotFoundException, SecurityServiceException
  {
    try
    {
      Optional<Long> groupIdOptional = getGroupRepository().getIdByUserDirectoryIdAndNameIgnoreCase(
          getUserDirectoryId(), groupName);

      if (groupIdOptional.isEmpty())
      {
        throw new GroupNotFoundException(groupName);
      }

      Optional<Long> userIdOptional =
        getUserRepository().getIdByUserDirectoryIdAndUsernameIgnoreCase(getUserDirectoryId(),
          username);

      if (userIdOptional.isEmpty())
      {
        throw new UserNotFoundException(username);
      }

      getGroupRepository().addUserToGroup(groupIdOptional.get(), userIdOptional.get());
    }
    catch (UserNotFoundException | GroupNotFoundException e)
    {
      throw e;
    }
    catch (Throwable e)
    {
      throw new SecurityServiceException(String.format(
          "Failed to add the user (%s) to the group (%s) for the user directory (%s)", username,
          groupName, getUserDirectoryId()), e);
    }
  }

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
  @Override
  public void adminChangePassword(String username, String newPassword, boolean expirePassword,
      boolean lockUser, boolean resetPasswordHistory, PasswordChangeReason reason)
    throws UserNotFoundException, SecurityServiceException
  {
    try
    {
      Optional<Long> userIdOptional =
          getUserRepository().getIdByUserDirectoryIdAndUsernameIgnoreCase(getUserDirectoryId(),
          username);

      if (userIdOptional.isEmpty())
      {
        throw new UserNotFoundException(username);
      }

      String newPasswordHash = createPasswordHash(newPassword);

      int passwordAttempts = 0;

      if (lockUser)
      {
        passwordAttempts = maxPasswordAttempts;
      }

      LocalDateTime passwordExpiry;

      if (expirePassword)
      {
        passwordExpiry = LocalDateTime.now();
      }
      else
      {
        passwordExpiry = LocalDateTime.now();
        passwordExpiry = passwordExpiry.plus(passwordExpiryMonths, ChronoUnit.MONTHS);
      }

      getUserRepository().changePassword(userIdOptional.get(), newPasswordHash, passwordAttempts,
          Optional.of(passwordExpiry));

      getUserRepository().savePasswordInPasswordHistory(userIdOptional.get(), newPasswordHash);
    }
    catch (UserNotFoundException e)
    {
      throw e;
    }
    catch (Throwable e)
    {
      throw new SecurityServiceException(String.format(
          "Failed to change the password for the user (%s) for the user directory (%s)", username,
          getUserDirectoryId()), e);
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
        UserNotFoundException, SecurityServiceException
  {
    try
    {
      Optional<User> userOptional = getUserRepository().findByUserDirectoryIdAndUsernameIgnoreCase(
          getUserDirectoryId(), username);

      if (userOptional.isEmpty())
      {
        throw new UserNotFoundException(username);
      }

      User user = userOptional.get();

      if ((user.getPasswordAttempts() != null)
          && (user.getPasswordAttempts() >= maxPasswordAttempts))
      {
        throw new UserLockedException(username);
      }

      if (user.hasPasswordExpired())
      {
        throw new ExpiredPasswordException(username);
      }

      if (!user.getPassword().equals(createPasswordHash(password)))
      {
        if ((user.getPasswordAttempts() != null) && (user.getPasswordAttempts() != -1))
        {
          getUserRepository().incrementPasswordAttempts(user.getId());
        }

        throw new AuthenticationFailedException(String.format(
            "Authentication failed for the user (%s)", username));
      }
    }
    catch (AuthenticationFailedException | UserNotFoundException | UserLockedException
        | ExpiredPasswordException e)
    {
      throw e;
    }
    catch (Throwable e)
    {
      throw new SecurityServiceException(String.format(
          "Failed to authenticate the user (%s) for the user directory (%s)", username,
          getUserDirectoryId()), e);
    }
  }

  /**
   * Change the password for the user.
   *
   * @param username    the username identifying the user
   * @param password    the password for the user that is used to authorise the operation
   * @param newPassword the new password
   */
  @Override
  public void changePassword(String username, String password, String newPassword)
    throws AuthenticationFailedException, UserLockedException, UserNotFoundException,
        ExistingPasswordException, SecurityServiceException
  {
    try
    {
      Optional<User> userOptional = getUserRepository().findByUserDirectoryIdAndUsernameIgnoreCase(
          getUserDirectoryId(), username);

      if (userOptional.isEmpty())
      {
        throw new UserNotFoundException(username);
      }

      User user = userOptional.get();

      if ((user.getPasswordAttempts() != null)
          && (user.getPasswordAttempts() > maxPasswordAttempts))
      {
        throw new UserLockedException(username);
      }

      String passwordHash = createPasswordHash(password);
      String newPasswordHash = createPasswordHash(newPassword);

      if (!user.getPassword().equals(passwordHash))
      {
        throw new AuthenticationFailedException(String.format(
            "Authentication failed while attempting to change the password for the user (%s)",
            username));
      }

      if (isPasswordInHistory(user.getId(), newPasswordHash))
      {
        throw new ExistingPasswordException(username);
      }

      LocalDateTime passwordExpiry = LocalDateTime.now();
      passwordExpiry = passwordExpiry.plus(passwordExpiryMonths, ChronoUnit.MONTHS);

      user.setPasswordExpiry(passwordExpiry);

      getUserRepository().changePassword(user.getId(), newPasswordHash, 0, Optional.of(
          passwordExpiry));

      getUserRepository().savePasswordInPasswordHistory(user.getId(), newPasswordHash);
    }
    catch (AuthenticationFailedException | ExistingPasswordException | UserNotFoundException
        | UserLockedException e)
    {
      throw e;
    }
    catch (Throwable e)
    {
      throw new SecurityServiceException(String.format(
          "Failed to change the password for the user (%s) for the user directory (%s)", username,
          getUserDirectoryId()), e);
    }
  }

  /**
   * Create the new group.
   *
   * @param group the group
   */
  @Override
  public void createGroup(Group group)
    throws DuplicateGroupException, SecurityServiceException
  {
    try
    {
      if (getGroupRepository().existsByUserDirectoryIdAndNameIgnoreCase(getUserDirectoryId(),
          group.getName()))
      {
        throw new DuplicateGroupException(group.getName());
      }

      group.setUserDirectoryId(getUserDirectoryId());

      getGroupRepository().saveAndFlush(group);
    }
    catch (DuplicateGroupException e)
    {
      throw e;
    }
    catch (Throwable e)
    {
      throw new SecurityServiceException(String.format(
          "Failed to create the group (%s) for the user directory (%s)", group.getName(),
          getUserDirectoryId()), e);
    }
  }

  /**
   * Create the new user.
   *
   * @param user            the user
   * @param expiredPassword create the user with its password expired
   * @param userLocked      create the user locked
   */
  @Override
  public void createUser(User user, boolean expiredPassword, boolean userLocked)
    throws DuplicateUserException, SecurityServiceException
  {
    try
    {
      if (getUserRepository().existsByUserDirectoryIdAndUsernameIgnoreCase(getUserDirectoryId(),
          user.getUsername()))
      {
        throw new DuplicateUserException(user.getUsername());
      }

      user.setUserDirectoryId(getUserDirectoryId());

      if (!isNullOrEmpty(user.getPassword()))
      {
        user.setPassword(createPasswordHash(user.getPassword()));
      }
      else
      {
        user.setPassword(createPasswordHash(PasswordUtil.generateRandomPassword()));
      }

      if (userLocked)
      {
        user.setPasswordAttempts(maxPasswordAttempts);
      }
      else
      {
        user.setPasswordAttempts(0);
      }

      if (expiredPassword)
      {
        user.setPasswordExpiry(LocalDateTime.now());
      }
      else
      {
        LocalDateTime passwordExpiry = LocalDateTime.now();

        passwordExpiry = passwordExpiry.plus(passwordExpiryMonths, ChronoUnit.MONTHS);

        user.setPasswordExpiry(passwordExpiry);
      }

      getUserRepository().saveAndFlush(user);

      getUserRepository().savePasswordInPasswordHistory(user.getId(), user.getPassword());
    }
    catch (DuplicateUserException e)
    {
      throw e;
    }
    catch (Throwable e)
    {
      throw new SecurityServiceException(String.format(
          "Failed to create the user (%s) for the user directory (%s)", user.getUsername(),
          getUserDirectoryId()), e);
    }
  }

  /**
   * Delete the group.
   *
   * @param groupName the name identifying the group
   */
  @Override
  public void deleteGroup(String groupName)
    throws GroupNotFoundException, ExistingGroupMembersException, SecurityServiceException
  {
    try
    {
      Optional<Long> groupIdOptional = getGroupRepository().getIdByUserDirectoryIdAndNameIgnoreCase(
          getUserDirectoryId(), groupName);

      if (groupIdOptional.isEmpty())
      {
        throw new GroupNotFoundException(groupName);
      }

      if (getGroupRepository().countUsersById(groupIdOptional.get()) > 0)
      {
        throw new ExistingGroupMembersException(groupName);
      }

      getGroupRepository().deleteById(groupIdOptional.get());
    }
    catch (GroupNotFoundException | ExistingGroupMembersException e)
    {
      throw e;
    }
    catch (Throwable e)
    {
      throw new SecurityServiceException(String.format(
          "Failed to delete the group (%s) for the user directory (%s)", groupName,
          getUserDirectoryId()), e);
    }
  }

  /**
   * Delete the user.
   *
   * @param username the username identifying the user
   */
  @Override
  public void deleteUser(String username)
    throws UserNotFoundException, SecurityServiceException
  {
    try
    {
      Optional<Long> userIdOptional =
          getUserRepository().getIdByUserDirectoryIdAndUsernameIgnoreCase(getUserDirectoryId(),
          username);

      if (userIdOptional.isEmpty())
      {
        throw new UserNotFoundException(username);
      }

      getUserRepository().deleteById(userIdOptional.get());
    }
    catch (UserNotFoundException e)
    {
      throw e;
    }
    catch (Throwable e)
    {
      throw new SecurityServiceException(String.format(
          "Failed to delete the user (%s) for the user directory (%s)", username,
          getUserDirectoryId()), e);
    }
  }

  /**
   * Retrieve the users matching the attribute criteria.
   *
   * @param attributes the attribute criteria used to select the users
   *
   * @return the users whose attributes match the attribute criteria
   */
  @Override
  public List<User> findUsers(List<Attribute> attributes)
    throws InvalidAttributeException, SecurityServiceException
  {
    try
    {
      User userCriteria = new User();

      userCriteria.setUserDirectoryId(getUserDirectoryId());

      for (Attribute attribute : attributes)
      {
        if (attribute.getName().equalsIgnoreCase("status"))
        {
          userCriteria.setStatus(UserStatus.fromCode(attribute.getIntegerValue()));
        }
        else if (attribute.getName().equalsIgnoreCase("email"))
        {
          userCriteria.setEmail(attribute.getValue());
        }
        else if (attribute.getName().equalsIgnoreCase("firstName"))
        {
          userCriteria.setFirstName(attribute.getValue());
        }
        else if (attribute.getName().equalsIgnoreCase("lastName"))
        {
          userCriteria.setLastName(attribute.getValue());
        }
        else if (attribute.getName().equalsIgnoreCase("phoneNumber"))
        {
          userCriteria.setPhoneNumber(attribute.getValue());
        }
        else if (attribute.getName().equalsIgnoreCase("mobileNumber"))
        {
          userCriteria.setMobileNumber(attribute.getValue());
        }
        else if (attribute.getName().equalsIgnoreCase("username"))
        {
          userCriteria.setUsername(attribute.getValue());
        }
        else
        {
          throw new InvalidAttributeException(attribute.getName());
        }
      }

      ExampleMatcher matcher = ExampleMatcher.matching().withIgnoreNullValues().withIgnoreCase()
          .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING);

      return getUserRepository().findAll(Example.of(userCriteria, matcher));
    }
    catch (InvalidAttributeException e)
    {
      throw e;
    }
    catch (Throwable e)
    {
      throw new SecurityServiceException(String.format(
          "Failed to find the users for the user directory (%s)", getUserDirectoryId()), e);
    }
  }

  /**
   * Retrieve the authorised function codes for the user.
   *
   * @param username the username identifying the user
   *
   * @return the authorised function codes for the user
   */
  @Override
  public List<String> getFunctionCodesForUser(String username)
    throws UserNotFoundException, SecurityServiceException
  {
    try
    {
      Optional<Long> userIdOptional =
          getUserRepository().getIdByUserDirectoryIdAndUsernameIgnoreCase(getUserDirectoryId(),
          username);

      if (userIdOptional.isEmpty())
      {
        throw new UserNotFoundException(username);
      }

      return getUserRepository().getFunctionCodesByUserId(userIdOptional.get());
    }
    catch (UserNotFoundException e)
    {
      throw e;
    }
    catch (Throwable e)
    {
      throw new SecurityServiceException(String.format(
          "Failed to retrieve the function codes for the user (%s) for the user directory (%s)",
          username, getUserDirectoryId()), e);
    }
  }

  /**
   * Retrieve the group.
   *
   * @param groupName the name identifying the group
   *
   * @return the group
   */
  @Override
  public Group getGroup(String groupName)
    throws GroupNotFoundException, SecurityServiceException
  {
    try
    {
      Optional<Group> groupOptional = getGroupRepository().findByUserDirectoryIdAndNameIgnoreCase(
          getUserDirectoryId(), groupName);

      if (groupOptional.isPresent())
      {
        return groupOptional.get();
      }
      else
      {
        throw new GroupNotFoundException(groupName);
      }
    }
    catch (GroupNotFoundException e)
    {
      throw e;
    }
    catch (Throwable e)
    {
      throw new SecurityServiceException(String.format(
          "Failed to retrieve the group (%s) for the user directory (%s)", groupName,
          getUserDirectoryId()), e);
    }
  }

  /**
   * Retrieve all the group names.
   *
   * @return the group names
   */
  @Override
  public List<String> getGroupNames()
    throws SecurityServiceException
  {
    try
    {
      return getGroupRepository().getNamesByUserDirectoryId(getUserDirectoryId());
    }
    catch (Throwable e)
    {
      throw new SecurityServiceException(
          "Failed to retrieve the group names for the user directory (" + getUserDirectoryId()
          + ")", e);
    }
  }

  /**
   * Retrieve the names identifying the groups the user is a member of.
   *
   * @param username the username identifying the user
   *
   * @return the names identifying the groups the user is a member of
   */
  @Override
  public List<String> getGroupNamesForUser(String username)
    throws UserNotFoundException, SecurityServiceException
  {
    try
    {
      Optional<Long> userIdOptional =
          getUserRepository().getIdByUserDirectoryIdAndUsernameIgnoreCase(getUserDirectoryId(),
          username);

      if (userIdOptional.isEmpty())
      {
        throw new UserNotFoundException(username);
      }

      return getUserRepository().getGroupNamesByUserId(userIdOptional.get());
    }
    catch (UserNotFoundException e)
    {
      throw e;
    }
    catch (Throwable e)
    {
      throw new SecurityServiceException(String.format(
          "Failed to retrieve the names identifying the groups the user is a member of (%s) for the user directory (%s)",
          username, getUserDirectoryId()), e);
    }
  }

  /**
   * Retrieve all the groups.
   *
   * @return the groups
   */
  @Override
  public List<Group> getGroups()
    throws SecurityServiceException
  {
    try
    {
      return getGroupRepository().findByUserDirectoryId(getUserDirectoryId());
    }
    catch (Throwable e)
    {
      throw new SecurityServiceException("Failed to retrieve the groups for the user directory ("
          + getUserDirectoryId() + ")", e);
    }
  }

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
  @Override
  public List<Group> getGroups(String filter, SortDirection sortDirection, Integer pageIndex,
      Integer pageSize)
    throws SecurityServiceException
  {
    try
    {
      Pageable pageable = null;

      if (pageIndex == null)
      {
        pageIndex = 0;
      }

      if (pageSize == null)
      {
        pageSize = maxFilteredGroups;
      }

      pageable = PageRequest.of(pageIndex,
          (pageSize > maxFilteredGroups)
          ? maxFilteredGroups
          : pageSize,
          (sortDirection == SortDirection.ASCENDING)
          ? Sort.Direction.ASC
          : Sort.Direction.DESC, "name");

      if (StringUtils.isEmpty(filter))
      {
        return getGroupRepository().findByUserDirectoryId(getUserDirectoryId(), pageable);
      }
      else
      {
        return getGroupRepository().findFiltered(getUserDirectoryId(), "%" + filter + "%",
            pageable);
      }

    }
    catch (Throwable e)
    {
      throw new SecurityServiceException(
          "Failed to retrieve the filtered groups for the user directory (" + getUserDirectoryId()
          + ")", e);
    }
  }

  /**
   * Retrieve the groups the user is a member of.
   *
   * @param username the username identifying the user
   *
   * @return the groups the user is a member of
   */
  @Override
  public List<Group> getGroupsForUser(String username)
    throws UserNotFoundException, SecurityServiceException
  {
    try
    {
      Optional<Long> userIdOptional =
          getUserRepository().getIdByUserDirectoryIdAndUsernameIgnoreCase(getUserDirectoryId(),
          username);

      if (userIdOptional.isEmpty())
      {
        throw new UserNotFoundException(username);
      }

      return getUserRepository().getGroupsByUserId(userIdOptional.get());
    }
    catch (UserNotFoundException e)
    {
      throw e;
    }
    catch (Throwable e)
    {
      throw new SecurityServiceException("Failed to retrieve the groups the user is a member of ("
          + username + ") for the user directory (" + getUserDirectoryId() + ")", e);
    }
  }

  /**
   * Retrieve the number of groups.
   *
   * @param filter the optional filter to apply to the groups
   *
   * @return the number of groups
   */
  @Override
  public long getNumberOfGroups(String filter)
    throws SecurityServiceException
  {
    try
    {
      if (StringUtils.isEmpty(filter))
      {
        return getGroupRepository().countByUserDirectoryId(getUserDirectoryId());
      }
      else
      {
        return getGroupRepository().countFiltered(getUserDirectoryId(), "%" + filter + "%");
      }
    }
    catch (Throwable e)
    {
      throw new SecurityServiceException(
          "Failed to retrieve the number of groups for the user directory (" + getUserDirectoryId()
          + ")", e);
    }
  }

  /**
   * Retrieve the number of users.
   *
   * @param filter the optional filter to apply to the users
   *
   * @return the number of users
   */
  @Override
  public long getNumberOfUsers(String filter)
    throws SecurityServiceException
  {
    try
    {
      if (StringUtils.isEmpty(filter))
      {
        return getUserRepository().countByUserDirectoryId(getUserDirectoryId());
      }
      else
      {
        return getUserRepository().countFiltered(getUserDirectoryId(), "%" + filter + "%");
      }
    }
    catch (Throwable e)
    {
      throw new SecurityServiceException(
          "Failed to retrieve the number of users for the user directory (" + getUserDirectoryId()
          + ")", e);
    }
  }

  /**
   * Retrieve the codes for the roles that the user has been assigned.
   *
   * @param username the username identifying the user
   *
   * @return the codes for the roles that the user has been assigned
   */
  @Override
  public List<String> getRoleCodesForUser(String username)
    throws UserNotFoundException, SecurityServiceException
  {
    try
    {
      Optional<Long> userIdOptional =
          getUserRepository().getIdByUserDirectoryIdAndUsernameIgnoreCase(getUserDirectoryId(),
          username);

      if (userIdOptional.isEmpty())
      {
        throw new UserNotFoundException(username);
      }

      return getUserRepository().getRoleCodesByUserId(userIdOptional.get());
    }
    catch (UserNotFoundException e)
    {
      throw e;
    }
    catch (Throwable e)
    {
      throw new SecurityServiceException("Failed to retrieve the role codes for the user ("
          + username + ") for the user directory (" + getUserDirectoryId() + ")", e);
    }
  }

  /**
   * Retrieve the user.
   *
   * @param username the username identifying the user
   *
   * @return the user
   */
  @Override
  public User getUser(String username)
    throws UserNotFoundException, SecurityServiceException
  {
    try
    {
      Optional<User> userOptional = getUserRepository().findByUserDirectoryIdAndUsernameIgnoreCase(
          getUserDirectoryId(), username);

      if (userOptional.isPresent())
      {
        return userOptional.get();
      }
      else
      {
        throw new UserNotFoundException(username);
      }
    }
    catch (UserNotFoundException e)
    {
      throw e;
    }
    catch (Throwable e)
    {
      throw new SecurityServiceException("Failed to retrieve the user (" + username
          + ") for the user directory (" + getUserDirectoryId() + ")", e);
    }
  }

  /**
   * Retrieve all the users.
   *
   * @return the users
   */
  @Override
  public List<User> getUsers()
    throws SecurityServiceException
  {
    try
    {
      return getUserRepository().findByUserDirectoryId(getUserDirectoryId());
    }
    catch (Throwable e)
    {
      throw new SecurityServiceException("Failed to retrieve the users for the user directory ("
          + getUserDirectoryId() + ")", e);
    }
  }

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
  @Override
  public List<User> getUsers(String filter, UserSortBy sortBy, SortDirection sortDirection,
      Integer pageIndex, Integer pageSize)
    throws SecurityServiceException
  {
    try
    {
      Pageable pageable = null;

      if (pageIndex == null)
      {
        pageIndex = 0;
      }

      if (pageSize == null)
      {
        pageSize = maxFilteredUsers;
      }

      if (sortBy == UserSortBy.USERNAME)
      {
        pageable = PageRequest.of(pageIndex, (pageSize > maxFilteredUsers)
            ? maxFilteredUsers
            : pageSize, (sortDirection == SortDirection.ASCENDING)
            ? Sort.Direction.ASC
            : Sort.Direction.DESC, "username");
      }
      else if (sortBy == UserSortBy.FIRST_NAME)
      {
        pageable = PageRequest.of(pageIndex, (pageSize > maxFilteredUsers)
            ? maxFilteredUsers
            : pageSize, (sortDirection == SortDirection.ASCENDING)
            ? Sort.Direction.ASC
            : Sort.Direction.DESC, "firstName");
      }
      else if (sortBy == UserSortBy.LAST_NAME)
      {
        pageable = PageRequest.of(pageIndex, (pageSize > maxFilteredUsers)
            ? maxFilteredUsers
            : pageSize, (sortDirection == SortDirection.ASCENDING)
            ? Sort.Direction.ASC
            : Sort.Direction.DESC, "lastName");
      }

      if (StringUtils.isEmpty(filter))
      {
        return getUserRepository().findByUserDirectoryId(getUserDirectoryId(), pageable);
      }
      else
      {
        return getUserRepository().findFiltered(getUserDirectoryId(), "%" + filter + "%", pageable);
      }

    }
    catch (Throwable e)
    {
      throw new SecurityServiceException(
          "Failed to retrieve the filtered users for the user directory (" + getUserDirectoryId()
          + ")", e);
    }
  }

  /**
   * Does the user with the specified username exist?
   *
   * @param username the username identifying the user
   *
   * @return <code>true</code> if a user with specified username exists or <code>false</code>
   * otherwise
   */
  @Override
  public boolean isExistingUser(String username)
    throws SecurityServiceException
  {
    try
    {
      return getUserRepository().existsByUserDirectoryIdAndUsernameIgnoreCase(getUserDirectoryId(),
          username);
    }
    catch (Throwable e)
    {
      throw new SecurityServiceException("Failed to check whether the user (" + username
          + ") is an existing user for the user directory (" + getUserDirectoryId() + ")", e);
    }
  }

  /**
   * Is the user in the group?
   *
   * @param username  the username identifying the user
   * @param groupName the name identifying the group
   *
   * @return <code>true</code> if the user is a member of the group or <code>false</code>
   * otherwise
   */
  @Override
  public boolean isUserInGroup(String username, String groupName)
    throws UserNotFoundException, GroupNotFoundException, SecurityServiceException
  {
    try
    {
      Optional<Long> userIdOptional =
          getUserRepository().getIdByUserDirectoryIdAndUsernameIgnoreCase(getUserDirectoryId(),
          username);

      if (userIdOptional.isEmpty())
      {
        throw new UserNotFoundException(username);
      }

      Optional<Long> groupIdOptional = getGroupRepository().getIdByUserDirectoryIdAndNameIgnoreCase(
          getUserDirectoryId(), groupName);

      if (groupIdOptional.isEmpty())
      {
        throw new GroupNotFoundException(groupName);
      }

      return getUserRepository().isUserInGroup(userIdOptional.get(), groupIdOptional.get());
    }
    catch (UserNotFoundException | GroupNotFoundException e)
    {
      throw e;
    }
    catch (Throwable e)
    {
      throw new SecurityServiceException("Failed to check if the user (" + username
          + ") is in the group (" + groupName + ") for the user directory (" + getUserDirectoryId()
          + ")", e);
    }
  }

  /**
   * Remove the user from the group.
   *
   * @param groupName the name identifying the group
   * @param username  the username identifying the user
   */
  @Override
  public void removeUserFromGroup(String groupName, String username)
    throws UserNotFoundException, GroupNotFoundException, SecurityServiceException
  {
    try
    {
      Optional<Long> groupIdOptional = getGroupRepository().getIdByUserDirectoryIdAndNameIgnoreCase(
        getUserDirectoryId(), groupName);

      if (groupIdOptional.isEmpty())
      {
        throw new GroupNotFoundException(groupName);
      }

      Optional<Long> userIdOptional =
          getUserRepository().getIdByUserDirectoryIdAndUsernameIgnoreCase(getUserDirectoryId(),
          username);

      if (userIdOptional.isEmpty())
      {
        throw new UserNotFoundException(username);
      }

      getGroupRepository().removeUserFromGroup(groupIdOptional.get(), userIdOptional.get());
    }
    catch (UserNotFoundException | GroupNotFoundException e)
    {
      throw e;
    }
    catch (Throwable e)
    {
      throw new SecurityServiceException("Failed to remove the user (" + username
          + ") from the group (" + groupName + ") for the user directory (" + getUserDirectoryId()
          + ")", e);
    }
  }

  /**
   * Does the user directory support administering groups.
   *
   * @return <code>true</code> if the user directory supports administering groups or
   * <code>false</code> otherwise
   */
  @Override
  public boolean supportsGroupAdministration()
  {
    return true;
  }

  /**
   * Does the user directory support administering users.
   *
   * @return <code>true</code> if the user directory supports administering users or
   * <code>false</code> otherwise
   */
  @Override
  public boolean supportsUserAdministration()
  {
    return true;
  }

  /**
   * Update the group.
   *
   * @param group the group
   */
  @Override
  public void updateGroup(Group group)
    throws GroupNotFoundException, SecurityServiceException
  {
    try
    {
      Optional<Long> groupIdOptional = getGroupRepository().getIdByUserDirectoryIdAndNameIgnoreCase(
          getUserDirectoryId(), group.getName());

      if (groupIdOptional.isEmpty())
      {
        throw new GroupNotFoundException(group.getName());
      }

      group.setId(groupIdOptional.get());

      getGroupRepository().saveAndFlush(group);
    }
    catch (GroupNotFoundException e)
    {
      throw e;
    }
    catch (Throwable e)
    {
      throw new SecurityServiceException("Failed to update the group (" + group.getName()
          + ") for the user directory (" + getUserDirectoryId() + ")", e);
    }
  }

  /**
   * Update the user.
   *
   * @param user           the user
   * @param expirePassword expire the user's password as part of the update
   * @param lockUser       lock the user as part of the update
   */
  @Override
  public void updateUser(User user, boolean expirePassword, boolean lockUser)
    throws UserNotFoundException, SecurityServiceException
  {
    try
    {
      Optional<User> userOptional = getUserRepository().findByUserDirectoryIdAndUsernameIgnoreCase(
          user.getUserDirectoryId(), user.getUsername());

      if (userOptional.isEmpty())
      {
        throw new UserNotFoundException(user.getUsername());
      }

      User existingUser = userOptional.get();

      if (user.getFirstName() != null)
      {
        existingUser.setFirstName(user.getFirstName());
      }

      if (user.getLastName() != null)
      {
        existingUser.setLastName(user.getLastName());
      }

      if (user.getEmail() != null)
      {
        existingUser.setEmail(user.getEmail());
      }

      if (user.getPhoneNumber() != null)
      {
        existingUser.setPhoneNumber(user.getPhoneNumber());
      }

      if (user.getMobileNumber() != null)
      {
        existingUser.setMobileNumber(user.getMobileNumber());
      }

      if (!StringUtils.isEmpty(user.getPassword()))
      {
        existingUser.setPassword(createPasswordHash(user.getPassword()));
      }

      if (lockUser)
      {
        existingUser.setPasswordAttempts(maxPasswordAttempts);
      }
      else if (user.getPasswordAttempts() != null)
      {
        existingUser.setPasswordAttempts(user.getPasswordAttempts());
      }

      if (expirePassword)
      {
        existingUser.setPasswordExpiry(LocalDateTime.now());
      }
      else if (user.getPasswordExpiry() != null)
      {
        existingUser.setPasswordExpiry(user.getPasswordExpiry());
      }

      getUserRepository().saveAndFlush(existingUser);
    }
    catch (UserNotFoundException e)
    {
      throw e;
    }
    catch (Throwable e)
    {
      throw new SecurityServiceException("Failed to update the user (" + user.getUsername()
          + ") for the user directory (" + getUserDirectoryId() + ")", e);
    }
  }

  /**
   * Is the password, given by the specified password hash, a historical password that cannot
   * be reused for a period of time i.e. was the password used previously in the last X months.
   *
   * @param userId       the Universally Unique Identifier (UUID) used to uniquely identify the user
   * @param passwordHash the password hash
   *
   * @return <code>true</code> if the password was previously used and cannot be reused for a
   *         period of time or <code>false</code> otherwise
   */
  private boolean isPasswordInHistory(Long userId, String passwordHash)
  {
    LocalDateTime after = LocalDateTime.now();
    after = after.minus(passwordHistoryMonths, ChronoUnit.MONTHS);

    return getUserRepository().countPasswordHistory(userId, after, passwordHash) > 0;
  }
}
