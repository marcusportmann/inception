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

import digital.inception.core.persistence.IDGenerator;
import digital.inception.core.util.PasswordUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.*;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

//~--- JDK imports ------------------------------------------------------------

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import javax.sql.DataSource;

/**
 * The <code>InternalUserDirectory</code> class provides the internal user directory implementation.
 *
 * @author Marcus Portmann
 */
public class InternalUserDirectory extends UserDirectoryBase
{
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

//  /**
//   * The data source used to provide connections to the application database.
//   */
//  @Autowired
//  @Qualifier("applicationDataSource")
//  private DataSource dataSource;

  /**
   * The ID generator.
   */
  @Autowired
  private IDGenerator idGenerator;

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
   * @param userDirectoryId the Universally Unique Identifier (UUID) used to uniquely identify the
   *                        user directory
   * @param parameters      the parameters for the user directory
   * @param groupRepository the Group Repository
   * @param userRepository  the User Repository
   */
  public InternalUserDirectory(UUID userDirectoryId, List<UserDirectoryParameter> parameters,
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
    }
    catch (Throwable e)
    {
      throw new SecurityServiceException(String.format(
          "Failed to initialize the user directory (%s)", userDirectoryId), e);
    }
  }

  /**
   * Add the user to the security group.
   *
   * @param username  the username identifying the user
   * @param groupName the name of the security group uniquely identifying the security group
   */
  @Override
  @Transactional
  public void addUserToGroup(String username, String groupName)
    throws UserNotFoundException, GroupNotFoundException, SecurityServiceException
  {
    try
    {
      Optional<UUID> userIdOptional =
          getUserRepository().getIdByUserDirectoryIdAndUsernameIgnoreCase(getUserDirectoryId(),
          username);

      if (userIdOptional.isEmpty())
      {
        throw new UserNotFoundException(username);
      }

      Optional<UUID> groupIdOptional =
          getGroupRepository().getIdByUserDirectoryIdAndGroupNameIgnoreCase(getUserDirectoryId(),
          groupName);

      if (groupIdOptional.isEmpty())
      {
        throw new GroupNotFoundException(groupName);
      }

      getGroupRepository().addUserToGroup(userIdOptional.get(), groupIdOptional.get());
    }
    catch (UserNotFoundException | GroupNotFoundException e)
    {
      throw e;
    }
    catch (Throwable e)
    {
      throw new SecurityServiceException(String.format(
          "Failed to add the user (%s) to the security group (%s) for the user directory (%s)",
          username, groupName, getUserDirectoryId()), e);
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
  @Transactional
  public void adminChangePassword(String username, String newPassword, boolean expirePassword,
      boolean lockUser, boolean resetPasswordHistory, PasswordChangeReason reason)
    throws UserNotFoundException, SecurityServiceException
  {
    try
    {
      Optional<UUID> userIdOptional =
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

      getUserRepository().savePasswordInPasswordHistory(UUID.randomUUID(), userIdOptional.get(),
          newPasswordHash);
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
  @Transactional
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
  @Transactional
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

      getUserRepository().savePasswordInPasswordHistory(UUID.randomUUID(), user.getId(),
          newPasswordHash);
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
   * Create the new security group.
   *
   * @param group the security group
   */
  @Override
  @Transactional
  public void createGroup(Group group)
    throws DuplicateGroupException, SecurityServiceException
  {
    try
    {
      if (getGroupRepository().existsByUserDirectoryIdAndGroupNameIgnoreCase(getUserDirectoryId(),
          group.getGroupName()))
      {
        throw new DuplicateGroupException(group.getGroupName());
      }

      group.setId(idGenerator.nextUUID());
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
          "Failed to create the security group (%s) for the user directory (%s)",
          group.getGroupName(), getUserDirectoryId()), e);
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
  @Transactional
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

      user.setId(idGenerator.nextUUID());
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

      getUserRepository().savePasswordInPasswordHistory(UUID.randomUUID(), user.getId(),
          user.getPassword());
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
   * Delete the security group.
   *
   * @param groupName the name of the security group uniquely identifying the security group
   */
  @Override
  @Transactional
  public void deleteGroup(String groupName)
    throws GroupNotFoundException, ExistingGroupMembersException, SecurityServiceException
  {
    try
    {
      Optional<UUID> groupIdOptional =
          getGroupRepository().getIdByUserDirectoryIdAndGroupNameIgnoreCase(getUserDirectoryId(),
          groupName);

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
          "Failed to delete the security group (%s) for the user directory (%s)", groupName,
          getUserDirectoryId()), e);
    }
  }

  /**
   * Delete the user.
   *
   * @param username the username identifying the user
   */
  @Override
  @Transactional
  public void deleteUser(String username)
    throws UserNotFoundException, SecurityServiceException
  {
    try
    {
      Optional<UUID> userIdOptional =
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

      ExampleMatcher matcher = ExampleMatcher.matching().withIncludeNullValues().withIgnoreCase()
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
      Optional<UUID> userIdOptional =
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
   * Retrieve the security group.
   *
   * @param groupName the name of the security group uniquely identifying the security group
   *
   * @return the group
   */
  @Override
  public Group getGroup(String groupName)
    throws GroupNotFoundException, SecurityServiceException
  {
    try
    {
      Optional<Group> groupOptional =
          getGroupRepository().findByUserDirectoryIdAndGroupNameIgnoreCase(getUserDirectoryId(),
          groupName);

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
          "Failed to retrieve the security group (%s) for the user directory (%s)", groupName,
          getUserDirectoryId()), e);
    }
  }

  /**
   * Retrieve the security group names for the user.
   *
   * @param username the username identifying the user
   *
   * @return the security group names for the user
   */
  @Override
  public List<String> getGroupNamesForUser(String username)
    throws UserNotFoundException, SecurityServiceException
  {
    try
    {
      Optional<UUID> userIdOptional =
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
          "Failed to retrieve the security group names for the user (%s) for the user directory (%s)",
          username, getUserDirectoryId()), e);
    }
  }

  /**
   * Retrieve all the security groups.
   *
   * @return the security groups
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
      throw new SecurityServiceException(String.format(
          "Failed to retrieve the security groups for the user directory (%s)",
          getUserDirectoryId()), e);
    }
  }

  /**
   * Retrieve the security groups for the user.
   *
   * @param username the username identifying the user
   *
   * @return the security groups for the user
   */
  @Override
  public List<Group> getGroupsForUser(String username)
    throws UserNotFoundException, SecurityServiceException
  {
    try
    {
      Optional<UUID> userIdOptional =
          getUserRepository().getIdByUserDirectoryIdAndUsernameIgnoreCase(getUserDirectoryId(),
          username);

      if (userIdOptional.isEmpty())
      {
        throw new UserNotFoundException(username);
      }

      return getGroupRepository().findGroupsForUserById(userIdOptional.get());
    }
    catch (UserNotFoundException e)
    {
      throw e;
    }
    catch (Throwable e)
    {
      throw new SecurityServiceException(String.format(
          "Failed to retrieve the security groups for the user (%s) for the user directory (%s)",
          username, getUserDirectoryId()), e);
    }
  }

  /**
   * Retrieve the number of security groups.
   *
   * @return the number of security groups
   */
  @Override
  public long getNumberOfGroups()
    throws SecurityServiceException
  {
    try
    {
      return getGroupRepository().countByUserDirectoryId(getUserDirectoryId());
    }
    catch (Throwable e)
    {
      throw new SecurityServiceException(String.format(
          "Failed to retrieve the number of security groups for the user directory (%s)",
          getUserDirectoryId()), e);
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
        return getUserRepository().count();
      }
      else
      {
        return getUserRepository().countByUsernameIgnoreCaseContainingOrFirstNameIgnoreCaseContainingOrLastNameIgnoreCaseContaining(
            filter, filter, filter);
      }
    }
    catch (Throwable e)
    {
      throw new SecurityServiceException(String.format(
          "Failed to retrieve the number of users for the user directory (%s)",
          getUserDirectoryId()), e);
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
      Optional<UUID> userIdOptional =
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
      throw new SecurityServiceException(String.format(
          "Failed to retrieve the role codes for the user (%s) for the user directory (%s)",
          username, getUserDirectoryId()), e);
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
      throw new SecurityServiceException(String.format(
          "Failed to retrieve the user (%s) for the user directory (%s)", username,
          getUserDirectoryId()), e);
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
      throw new SecurityServiceException(String.format(
          "Failed to retrieve the users for the user directory (%s)", getUserDirectoryId()), e);
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
        return getUserRepository().findAll(pageable).getContent();
      }
      else
      {
        return getUserRepository().findByUsernameIgnoreCaseContainingOrFirstNameIgnoreCaseContainingOrLastNameIgnoreCaseContaining(
            filter, filter, filter, pageable);
      }

    }
    catch (Throwable e)
    {
      throw new SecurityServiceException(String.format(
          "Failed to retrieve the filtered users for the user directory (%s)",
          getUserDirectoryId()), e);
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
      throw new SecurityServiceException(String.format(
          "Failed to check whether the user (%s) is an existing user for the user directory (%s)",
          username, getUserDirectoryId()), e);
    }
  }

  /**
   * Is the user in the security group?
   *
   * @param username  the username identifying the user
   * @param groupName the name of the security group uniquely identifying the security group
   *
   * @return <code>true</code> if the user is a member of the security group or <code>false</code>
   * otherwise
   */
  @Override
  public boolean isUserInGroup(String username, String groupName)
    throws UserNotFoundException, GroupNotFoundException, SecurityServiceException
  {
    try
    {
      Optional<UUID> userIdOptional =
          getUserRepository().getIdByUserDirectoryIdAndUsernameIgnoreCase(getUserDirectoryId(),
          username);

      if (userIdOptional.isEmpty())
      {
        throw new UserNotFoundException(username);
      }

      Optional<UUID> groupIdOptional =
          getGroupRepository().getIdByUserDirectoryIdAndGroupNameIgnoreCase(getUserDirectoryId(),
          groupName);

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
      throw new SecurityServiceException(String.format(
          "Failed to check if the user (%s) is in the security group (%s) for the user directory "
          + "(%s)", username, groupName, getUserDirectoryId()), e);
    }
  }

  /**
   * Remove the user from the security group.
   *
   * @param username  the username identifying the user
   * @param groupName the security group name
   */
  @Override
  @Transactional
  public void removeUserFromGroup(String username, String groupName)
    throws UserNotFoundException, GroupNotFoundException, SecurityServiceException
  {
    try
    {
      Optional<UUID> userIdOptional =
          getUserRepository().getIdByUserDirectoryIdAndUsernameIgnoreCase(getUserDirectoryId(),
          username);

      if (userIdOptional.isEmpty())
      {
        throw new UserNotFoundException(username);
      }

      Optional<UUID> groupIdOptional =
          getGroupRepository().getIdByUserDirectoryIdAndGroupNameIgnoreCase(getUserDirectoryId(),
          groupName);

      if (groupIdOptional.isEmpty())
      {
        throw new GroupNotFoundException(groupName);
      }

      getGroupRepository().removeUserFromGroup(userIdOptional.get(), groupIdOptional.get());
    }
    catch (UserNotFoundException | GroupNotFoundException e)
    {
      throw e;
    }
    catch (Throwable e)
    {
      throw new SecurityServiceException(String.format(
          "Failed to remove the user (%s) from the security group (%s) for the user directory (%s)",
          username, groupName, getUserDirectoryId()), e);
    }
  }

  /**
   * Does the user directory support administering security groups.
   *
   * @return <code>true</code> if the user directory supports administering security groups or
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
   * Update the security group.
   *
   * @param group the security group
   */
  @Override
  public void updateGroup(Group group)
    throws GroupNotFoundException, SecurityServiceException
  {
    try
    {
      Optional<UUID> groupIdOptional =
          getGroupRepository().getIdByUserDirectoryIdAndGroupNameIgnoreCase(getUserDirectoryId(),
          group.getGroupName());

      if (groupIdOptional.isEmpty())
      {
        throw new GroupNotFoundException(group.getGroupName());
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
      throw new SecurityServiceException(String.format(
          "Failed to update the security group (%s) for the user directory (%s)",
          group.getGroupName(), getUserDirectoryId()), e);
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
  @Transactional
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
        existingUser.setPhoneNumber(user.getPassword());
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
      throw new SecurityServiceException(String.format(
          "Failed to update the user (%s) for the user directory (%s)", user.getUsername(),
          getUserDirectoryId()), e);
    }
  }

///**
// * Build the JDBC <code>PreparedStatement</code> for the SQL query that will select the users
// * in the USERS table using the values of the specified attributes as the selection criteria.
// *
// * @param connection the existing database connection to use
// * @param attributes the attributes to be used as the selection criteria
// *
// * @return the <code>PreparedStatement</code> for the SQL query that will select the users in the
// * USERS table using the values of the specified attributes as the selection criteria
// */
//private PreparedStatement buildFindUsersStatement(Connection connection,
//    List<Attribute> attributes)
//  throws InvalidAttributeException, SQLException
//{
//  // Build the SQL statement to select the users
//  StringBuilder buffer = new StringBuilder();
//
//  buffer.append("SELECT id, username, status, first_name, last_name, phone, mobile, email, ");
//  buffer.append("password, password_attempts, password_expiry FROM security.users");
//
//  if (attributes.size() > 0)
//  {
//    // Build the parameters for the "WHERE" clause for the SQL statement
//    StringBuilder whereParameters = new StringBuilder();
//
//    for (Attribute attribute : attributes)
//    {
//      whereParameters.append(" AND ");
//
//      if (attribute.getName().equalsIgnoreCase("status"))
//      {
//        whereParameters.append("status = ?");
//      }
//      else if (attribute.getName().equalsIgnoreCase("email"))
//      {
//        whereParameters.append("LOWER(email) LIKE LOWER(?)");
//      }
//      else if (attribute.getName().equalsIgnoreCase("firstName"))
//      {
//        whereParameters.append("LOWER(first_name) LIKE LOWER(?)");
//      }
//      else if (attribute.getName().equalsIgnoreCase("lastName"))
//      {
//        whereParameters.append("LOWER(last_name) LIKE LOWER(?)");
//      }
//      else if (attribute.getName().equalsIgnoreCase("phoneNumber"))
//      {
//        whereParameters.append("LOWER(phone) LIKE LOWER(?)");
//      }
//      else if (attribute.getName().equalsIgnoreCase("mobileNumber"))
//      {
//        whereParameters.append("LOWER(mobile) LIKE LOWER(?)");
//      }
//      else if (attribute.getName().equalsIgnoreCase("username"))
//      {
//        whereParameters.append("LOWER(username) LIKE LOWER(?)");
//      }
//      else
//      {
//        throw new InvalidAttributeException(attribute.getName());
//      }
//    }
//
//    buffer.append(" WHERE user_directory_id=?");
//    buffer.append(whereParameters.toString());
//  }
//  else
//  {
//    buffer.append(" WHERE user_directory_id=?");
//  }
//
//  PreparedStatement statement = connection.prepareStatement(buffer.toString());
//
//  statement.setObject(1, UUID.fromString(getUserDirectoryId()));
//
//  // Set the parameters for the prepared statement
//  int parameterIndex = 2;
//
//  for (Attribute attribute : attributes)
//  {
//    if (attribute.getName().equalsIgnoreCase("status"))
//    {
//      statement.setInt(parameterIndex, Integer.parseInt(attribute.getStringValue()));
//      parameterIndex++;
//    }
//    else if (attribute.getName().equalsIgnoreCase("email"))
//    {
//      statement.setString(parameterIndex, attribute.getStringValue());
//      parameterIndex++;
//    }
//    else if (attribute.getName().equalsIgnoreCase("firstName"))
//    {
//      statement.setString(parameterIndex, attribute.getStringValue());
//      parameterIndex++;
//    }
//    else if (attribute.getName().equalsIgnoreCase("lastName"))
//    {
//      statement.setString(parameterIndex, attribute.getStringValue());
//      parameterIndex++;
//    }
//    else if (attribute.getName().equalsIgnoreCase("phoneNumber"))
//    {
//      statement.setString(parameterIndex, attribute.getStringValue());
//      parameterIndex++;
//    }
//    else if (attribute.getName().equalsIgnoreCase("mobileNumber"))
//    {
//      statement.setString(parameterIndex, attribute.getStringValue());
//      parameterIndex++;
//    }
//    else if (attribute.getName().equalsIgnoreCase("username"))
//    {
//      statement.setString(parameterIndex, attribute.getStringValue());
//      parameterIndex++;
//    }
//  }
//
//  return statement;
//}

//  /**
//   * Create a new <code>User</code> instance and populate it with the contents of the current
//   * row in the specified <code>ResultSet</code>.
//   *
//   * @param rs the <code>ResultSet</code> whose current row will be used to populate the
//   *           <code>User</code> instance
//   *
//   * @return the populated <code>User</code> instance
//   */
//  private User buildUserFromResultSet(ResultSet rs)
//    throws SQLException
//  {
//    User user = new User();
//
//    user.setId(rs.getObject(1, UUID.class));
//    user.setUsername(rs.getString(2));
//    user.setUserDirectoryId(getUserDirectoryId());
//    user.setStatus(UserStatus.fromCode(rs.getInt(3)));
//
//    String firstName = rs.getString(4);
//
//    user.setFirstName(StringUtils.isEmpty(firstName)
//        ? ""
//        : firstName);
//
//    String lastName = rs.getString(5);
//
//    user.setLastName(StringUtils.isEmpty(lastName)
//        ? ""
//        : lastName);
//
//    String phoneNumber = rs.getString(6);
//
//    user.setPhoneNumber(StringUtils.isEmpty(phoneNumber)
//        ? ""
//        : phoneNumber);
//
//    String mobilePhoneNumber = rs.getString(7);
//
//    user.setMobileNumber(StringUtils.isEmpty(mobilePhoneNumber)
//        ? ""
//        : mobilePhoneNumber);
//
//    String email = rs.getString(8);
//
//    user.setEmail(StringUtils.isEmpty(email)
//        ? ""
//        : email);
//
//    String password = rs.getString(9);
//
//    user.setPassword(StringUtils.isEmpty(password)
//        ? ""
//        : password);
//
//    if (rs.getObject(10) != null)
//    {
//      user.setPasswordAttempts(rs.getInt(10));
//    }
//
//    if (rs.getObject(11) != null)
//    {
//      user.setPasswordExpiry(rs.getTimestamp(11).toLocalDateTime());
//    }
//
//    return user;
//  }

//  /**
//   * Retrieve the authorised function codes for the user.
//   *
//   * @param connection the existing database connection to use
//   * @param userId     the Universally Unique Identifier (UUID) used to uniquely identify the user
//   *
//   * @return the authorised function codes for the user
//   */
//  private List<String> getFunctionCodesForUserId(Connection connection, String userId)
//    throws SQLException
//  {
//    String getFunctionCodesForUserIdSQL =
//        "SELECT DISTINCT ftrm.function_code FROM security.function_to_role_map ftrm "
//        + "INNER JOIN security.role_to_group_map rtgm ON rtgm.role_code = ftrm.role_code "
//        + "INNER JOIN security.groups g ON g.id = rtgm.group_id "
//        + "INNER JOIN security.user_to_group_map utgm ON utgm.group_id = g.ID WHERE utgm.user_id=?";
//
//    List<String> functionCodes = new ArrayList<>();
//
//    try (PreparedStatement statement = connection.prepareStatement(getFunctionCodesForUserIdSQL))
//    {
//      statement.setObject(1, UUID.fromString(userId));
//
//      try (ResultSet rs = statement.executeQuery())
//      {
//        while (rs.next())
//        {
//          functionCodes.add(rs.getString(1));
//        }
//
//        return functionCodes;
//      }
//    }
//  }

//  /**
//   * Retrieve the names for all the security groups that the user with the specific numeric
//   * ID is associated with.
//   *
//   * @param connection the existing database connection
//   * @param userId     the Universally Unique Identifier (UUID) used to uniquely identify the user
//   *
//   * @return the security groups
//   */
//  private List<String> getGroupNamesForUser(Connection connection, String userId)
//    throws SQLException
//  {
//    String getGroupNamesForUserSQL = "SELECT groupname FROM "
//        + "security.groups g, security.user_to_group_map utgm "
//        + "WHERE g.id = utgm.group_id AND utgm.user_id=? ORDER BY g.groupname";
//
//    try (PreparedStatement statement = connection.prepareStatement(getGroupNamesForUserSQL))
//    {
//      statement.setObject(1, UUID.fromString(userId));
//
//      try (ResultSet rs = statement.executeQuery())
//      {
//        List<String> list = new ArrayList<>();
//
//        while (rs.next())
//        {
//          list.add(rs.getString(1));
//        }
//
//        return list;
//      }
//    }
//  }

///**
// * Retrieve all the internal security groups that the user with the specific numeric ID
// * is associated with.
// *
// * @param connection the existing database connection
// * @param userId     the Universally Unique Identifier (UUID) used to uniquely identify the user
// *
// * @return the internal security groups
// */
//private List<Group> getGroupsForUser(Connection connection, String userId)
//  throws SQLException
//{
//  String getGroupsForUserSQL = "SELECT id, groupname, description FROM "
//      + "security.groups g, security.user_to_group_map utgm "
//      + "WHERE g.id = utgm.group_id AND utgm.user_id=? " + "ORDER BY g.groupname";
//
//  try (PreparedStatement statement = connection.prepareStatement(getGroupsForUserSQL))
//  {
//    statement.setObject(1, UUID.fromString(userId));
//
//    try (ResultSet rs = statement.executeQuery())
//    {
//      List<Group> list = new ArrayList<>();
//
//      while (rs.next())
//      {
//        Group group = new Group(rs.getString(2));
//
//        group.setId(rs.getString(1));
//        group.setUserDirectoryId(getUserDirectoryId());
//        group.setDescription(rs.getString(3));
//        list.add(group);
//      }
//
//      return list;
//    }
//  }
//}

//  /**
//   * Retrieve the number of users for the internal security group.
//   *
//   * @param connection the existing database connection
//   * @param groupId    the ID used to uniquely identify the internal security group
//   *
//   * @return the number of users for the internal security group
//   */
//  private long getNumberOfUsersForGroup(Connection connection, String groupId)
//    throws SQLException
//  {
//    String getNumberOfUsersForGroupSQL = "SELECT COUNT (user_id) FROM security.user_to_group_map "
//        + "WHERE group_id=?";
//
//    try (PreparedStatement statement = connection.prepareStatement(getNumberOfUsersForGroupSQL))
//    {
//      statement.setObject(1, UUID.fromString(groupId));
//
//      try (ResultSet rs = statement.executeQuery())
//      {
//        if (rs.next())
//        {
//          return rs.getLong(1);
//        }
//        else
//        {
//          return 0;
//        }
//      }
//    }
//  }

//  /**
//   * Retrieve the codes for the roles that the user has been assigned.
//   *
//   * @param connection the existing database connection to use
//   * @param userId     the Universally Unique Identifier (UUID) used to uniquely identify the user
//   *
//   * @return the codes for the roles that the user has been assigned
//   */
//  private List<String> getRoleCodesForUserId(Connection connection, String userId)
//    throws SQLException
//  {
//    String getRoleCodesForUserIdSQL =
//        "SELECT DISTINCT rtgm.role_code FROM security.role_to_group_map rtgm "
//        + "INNER JOIN security.groups g ON g.id = rtgm.group_id "
//        + "INNER JOIN security.user_to_group_map utgm ON utgm.group_id = g.ID WHERE utgm.user_id=?";
//
//    List<String> roleCodes = new ArrayList<>();
//
//    try (PreparedStatement statement = connection.prepareStatement(getRoleCodesForUserIdSQL))
//    {
//      statement.setObject(1, UUID.fromString(userId));
//
//      try (ResultSet rs = statement.executeQuery())
//      {
//        while (rs.next())
//        {
//          roleCodes.add(rs.getString(1));
//        }
//
//        return roleCodes;
//      }
//    }
//  }

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
  private boolean isPasswordInHistory(UUID userId, String passwordHash)
  {
    LocalDateTime after = LocalDateTime.now();
    after = after.minus(passwordHistoryMonths, ChronoUnit.MONTHS);

    return getUserRepository().countPasswordHistory(userId, after, passwordHash) > 0;
  }
}
