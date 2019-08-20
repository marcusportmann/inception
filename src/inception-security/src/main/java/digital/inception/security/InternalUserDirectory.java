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
import org.springframework.util.StringUtils;

//~--- JDK imports ------------------------------------------------------------

import java.sql.*;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import java.util.*;
import java.util.Date;

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

  /**
   * The data source used to provide connections to the application database.
   */
  @Autowired
  @Qualifier("applicationDataSource")
  private DataSource dataSource;

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
   */
  public InternalUserDirectory(UUID userDirectoryId, List<UserDirectoryParameter> parameters)
    throws SecurityServiceException
  {
    super(userDirectoryId, parameters);

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
  public void addUserToGroup(String username, String groupName)
    throws UserNotFoundException, GroupNotFoundException, SecurityServiceException
  {
    String addUserToGroupSQL = "INSERT INTO security.user_to_group_map "
        + "(user_id, group_id) VALUES (?, ?)";

    try (Connection connection = dataSource.getConnection();
      PreparedStatement statement = connection.prepareStatement(addUserToGroupSQL))
    {
      // Get the ID of the user with the specified username
      UUID userId;

      if ((userId = getUserId(connection, username)) == null)
      {
        throw new UserNotFoundException(username);
      }

      // Get the ID of the internal security group with the specified group name
      UUID groupId;

      if ((groupId = getGroupId(connection, groupName)) == null)
      {
        throw new GroupNotFoundException(groupName);
      }

      // Check if the user has already been added to the security group for the user directory
      if (isUserInGroup(connection, userId, groupId))
      {
        // The user is already a member of the specified security group do nothing
        return;
      }

      // Add the user to the security group
      statement.setObject(1, userId);
      statement.setObject(2, groupId);

      if (statement.executeUpdate() != 1)
      {
        throw new SecurityServiceException(String.format(
            "No rows were affected as a result of executing the SQL statement (%s)",
            addUserToGroupSQL));
      }
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
  public void adminChangePassword(String username, String newPassword, boolean expirePassword,
      boolean lockUser, boolean resetPasswordHistory, PasswordChangeReason reason)
    throws UserNotFoundException, SecurityServiceException
  {
    String changeUserPasswordSQL =
        "UPDATE security.users SET password=?, password_attempts=?, password_expiry=? "
        + "WHERE user_directory_id=? AND id=?";

    try (Connection connection = dataSource.getConnection();
      PreparedStatement statement = connection.prepareStatement(changeUserPasswordSQL))
    {
      User user = getUser(connection, username);

      if (user == null)
      {
        throw new UserNotFoundException(username);
      }

      String passwordHash = createPasswordHash(newPassword);

      statement.setString(1, passwordHash);

      if (lockUser)
      {
        statement.setInt(2, maxPasswordAttempts);
      }
      else
      {
        if (user.getPasswordAttempts() == null)
        {
          statement.setNull(2, java.sql.Types.INTEGER);
        }
        else
        {
          statement.setInt(2, 0);
        }
      }

      if (expirePassword)
      {
        statement.setTimestamp(3, new Timestamp(0));
      }
      else
      {
        if (user.getPasswordExpiry() == null)
        {
          statement.setNull(3, Types.TIMESTAMP);
        }
        else
        {
          Calendar calendar = Calendar.getInstance();
          calendar.setTime(new Date());
          calendar.add(Calendar.MONTH, passwordExpiryMonths);

          statement.setTimestamp(3, new Timestamp(calendar.getTimeInMillis()));
        }
      }

      statement.setObject(4, getUserDirectoryId());
      statement.setObject(5, user.getId());

      if (statement.executeUpdate() != 1)
      {
        throw new SecurityServiceException(String.format(
            "No rows were affected as a result of executing the SQL statement (%s)",
            changeUserPasswordSQL));
      }

      savePasswordHistory(connection, user.getId(), passwordHash);
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
    try (Connection connection = dataSource.getConnection())
    {
      User user = getUser(connection, username);

      if (user == null)
      {
        throw new UserNotFoundException(username);
      }

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
          incrementPasswordAttempts(user.getId());
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
    String changeUserPasswordSQL =
        "UPDATE security.users SET password=?, password_attempts=?, password_expiry=? "
        + "WHERE user_directory_id=? AND id=?";

    try (Connection connection = dataSource.getConnection();
      PreparedStatement statement = connection.prepareStatement(changeUserPasswordSQL))
    {
      User user = getUser(connection, username);

      if (user == null)
      {
        throw new UserNotFoundException(username);
      }

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

      if (isPasswordInHistory(connection, user.getId(), newPasswordHash))
      {
        throw new ExistingPasswordException(username);
      }

      statement.setString(1, newPasswordHash);

      if (user.getPasswordAttempts() == null)
      {
        statement.setNull(2, java.sql.Types.INTEGER);
      }
      else
      {
        statement.setInt(2, 0);
      }

      if (user.getPasswordExpiry() == null)
      {
        statement.setNull(3, Types.TIMESTAMP);
      }
      else
      {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.MONTH, passwordExpiryMonths);

        statement.setTimestamp(3, new Timestamp(calendar.getTimeInMillis()));
      }

      statement.setObject(4, getUserDirectoryId());
      statement.setObject(5, user.getId());

      if (statement.executeUpdate() != 1)
      {
        throw new SecurityServiceException(String.format(
            "No rows were affected as a result of executing the SQL statement (%s)",
            changeUserPasswordSQL));
      }

      savePasswordHistory(connection, user.getId(), newPasswordHash);
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
  public void createGroup(Group group)
    throws DuplicateGroupException, SecurityServiceException
  {
    try (Connection connection = dataSource.getConnection())
    {
      if (getGroupId(connection, group.getGroupName()) != null)
      {
        throw new DuplicateGroupException(group.getGroupName());
      }

      UUID groupId = idGenerator.nextUUID();

      createGroup(connection, groupId, group.getGroupName(), group.getDescription());

      group.setId(groupId);
      group.setUserDirectoryId(getUserDirectoryId());
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
  public void createUser(User user, boolean expiredPassword, boolean userLocked)
    throws DuplicateUserException, SecurityServiceException
  {
    String createUserSQL = "INSERT INTO security.users "
        + "(id, user_directory_id, username, status, first_name, last_name, phone, mobile, email, "
        + "password, password_attempts, password_expiry) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

    try (Connection connection = dataSource.getConnection();
      PreparedStatement statement = connection.prepareStatement(createUserSQL))
    {
      if (getUserId(connection, user.getUsername()) != null)
      {
        throw new DuplicateUserException(user.getUsername());
      }

      UUID userId = idGenerator.nextUUID();

      statement.setObject(1, userId);
      statement.setObject(2, getUserDirectoryId());
      statement.setString(3, user.getUsername());
      statement.setInt(4, user.getStatus().code());
      statement.setString(5,
          StringUtils.isEmpty(user.getFirstName())
          ? ""
          : user.getFirstName());
      statement.setString(6,
          StringUtils.isEmpty(user.getLastName())
          ? ""
          : user.getLastName());
      statement.setString(7,
          StringUtils.isEmpty(user.getPhoneNumber())
          ? ""
          : user.getPhoneNumber());
      statement.setString(8,
          StringUtils.isEmpty(user.getMobileNumber())
          ? ""
          : user.getMobileNumber());
      statement.setString(9,
          StringUtils.isEmpty(user.getEmail())
          ? ""
          : user.getEmail());

      String passwordHash;

      if (!isNullOrEmpty(user.getPassword()))
      {
        passwordHash = createPasswordHash(user.getPassword());
      }
      else
      {
        passwordHash = createPasswordHash(PasswordUtil.generateRandomPassword());
      }

      statement.setString(10, passwordHash);

      if (userLocked)
      {
        statement.setInt(11, maxPasswordAttempts);
        user.setPasswordAttempts(maxPasswordAttempts);
      }
      else
      {
        statement.setInt(11, 0);
        user.setPasswordAttempts(0);
      }

      if (expiredPassword)
      {
        Timestamp expiryTime = new Timestamp(0);

        statement.setTimestamp(12, expiryTime);
        user.setPasswordExpiry(expiryTime.toLocalDateTime());
      }
      else
      {
        LocalDateTime passwordExpiry = LocalDateTime.now();

        passwordExpiry = passwordExpiry.plus(passwordExpiryMonths, ChronoUnit.MONTHS);

        statement.setTimestamp(12, Timestamp.valueOf(passwordExpiry));
        user.setPasswordExpiry(passwordExpiry);
      }

      if (statement.executeUpdate() != 1)
      {
        throw new SecurityServiceException(String.format(
            "No rows were affected as a result of executing the SQL statement (%s)",
            createUserSQL));
      }

      user.setId(userId);
      user.setUserDirectoryId(getUserDirectoryId());

      // Save the password in the password history if one was specified
      if (passwordHash != null)
      {
        savePasswordHistory(connection, userId, passwordHash);
      }
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
  public void deleteGroup(String groupName)
    throws GroupNotFoundException, ExistingGroupMembersException, SecurityServiceException
  {
    try (Connection connection = dataSource.getConnection())
    {
      UUID groupId = getGroupId(connection, groupName);

      if (groupId == null)
      {
        throw new GroupNotFoundException(groupName);
      }

      if (getNumberOfUsersForGroup(connection, groupId) > 0)
      {
        throw new ExistingGroupMembersException(groupName);
      }

      deleteGroup(connection, groupName);
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
  public void deleteUser(String username)
    throws UserNotFoundException, SecurityServiceException
  {
    String deleteUserSQL = "DELETE FROM security.users WHERE user_directory_id=? AND id=?";

    try (Connection connection = dataSource.getConnection();
      PreparedStatement statement = connection.prepareStatement(deleteUserSQL))
    {
      UUID userId = getUserId(connection, username);

      if (userId == null)
      {
        throw new UserNotFoundException(username);
      }

      statement.setObject(1, getUserDirectoryId());
      statement.setObject(2, userId);

      if (statement.executeUpdate() <= 0)
      {
        throw new SecurityServiceException(String.format(
            "No rows were affected as a result of executing the SQL statement (%s)",
            deleteUserSQL));
      }
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
    try (Connection connection = dataSource.getConnection())
    {
      try (PreparedStatement statement = buildFindUsersStatement(connection, attributes))
      {
        try (ResultSet rs = statement.executeQuery())
        {
          List<User> list = new ArrayList<>();

          while (rs.next())
          {
            User user = buildUserFromResultSet(rs);

            list.add(user);
          }

          return list;
        }
      }
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
    try (Connection connection = dataSource.getConnection())
    {
      // Get the ID of the user with the specified username
      UUID userId = getUserId(connection, username);

      if (userId == null)
      {
        throw new UserNotFoundException(username);
      }

      return getFunctionCodesForUserId(connection, userId);
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
    try (Connection connection = dataSource.getConnection())
    {
      return getGroup(connection, groupName);
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
    try (Connection connection = dataSource.getConnection())
    {
      // Get the ID of the user with the specified username
      UUID userId = getUserId(connection, username);

      if (userId == null)
      {
        throw new UserNotFoundException(username);
      }

      return getGroupNamesForUser(connection, userId);
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
    try (Connection connection = dataSource.getConnection())
    {
      return getGroups(connection);
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
    try (Connection connection = dataSource.getConnection())
    {
      // Get the ID of the user with the specified username
      UUID userId = getUserId(connection, username);

      if (userId == null)
      {
        throw new UserNotFoundException(username);
      }

      // Get the groups the user is associated with
      return getGroupsForUser(connection, userId);
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
  public int getNumberOfGroups()
    throws SecurityServiceException
  {
    try (Connection connection = dataSource.getConnection())
    {
      return getNumberOfGroups(connection);
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
  public int getNumberOfUsers(String filter)
    throws SecurityServiceException
  {
    String getNumberOfUsersSQL = "SELECT COUNT(id) FROM security.users";

    if (StringUtils.isEmpty(filter))
    {
      getNumberOfUsersSQL += " WHERE user_directory_id=?";
    }
    else
    {
      getNumberOfUsersSQL +=
          " WHERE user_directory_id=? AND ((UPPER(username) LIKE ?) OR (UPPER(first_name) LIKE ?) "
          + "OR (UPPER(last_name) LIKE ?))";
    }

    try (Connection connection = dataSource.getConnection();
      PreparedStatement statement = connection.prepareStatement(getNumberOfUsersSQL))
    {
      if (StringUtils.isEmpty(filter))
      {
        statement.setObject(1, getUserDirectoryId());
      }
      else
      {
        StringBuilder filterBuffer = new StringBuilder("%");

        filterBuffer.append(filter.toUpperCase());
        filterBuffer.append("%");

        statement.setObject(1, getUserDirectoryId());
        statement.setString(2, filterBuffer.toString());
        statement.setString(3, filterBuffer.toString());
        statement.setString(4, filterBuffer.toString());
      }

      try (ResultSet rs = statement.executeQuery())
      {
        if (rs.next())
        {
          return rs.getInt(1);
        }
        else
        {
          return 0;
        }
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
   * Retrieve the names for the roles that the user has been assigned.
   *
   * @param username the username identifying the user
   *
   * @return the names for the roles that the user has been assigned
   */
  @Override
  public List<String> getRoleNamesForUser(String username)
    throws UserNotFoundException, SecurityServiceException
  {
    try (Connection connection = dataSource.getConnection())
    {
      // Get the ID of the user with the specified username
      UUID userId = getUserId(connection, username);

      if (userId == null)
      {
        throw new UserNotFoundException(username);
      }

      return getRoleNamesForUserId(connection, userId);
    }
    catch (UserNotFoundException e)
    {
      throw e;
    }
    catch (Throwable e)
    {
      throw new SecurityServiceException(String.format(
          "Failed to retrieve the role names for the user (%s) for the user directory (%s)",
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
    try (Connection connection = dataSource.getConnection())
    {
      User user = getUser(connection, username);

      if (user != null)
      {
        return user;
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
    String getUsersSQL = "SELECT id, username, status, first_name, "
        + "last_name, phone, mobile, email, password, password_attempts, password_expiry "
        + "FROM security.users WHERE user_directory_id=? ORDER BY username";

    try (Connection connection = dataSource.getConnection();
      PreparedStatement statement = connection.prepareStatement(getUsersSQL))
    {
      statement.setObject(1, getUserDirectoryId());

      try (ResultSet rs = statement.executeQuery())
      {
        List<User> list = new ArrayList<>();

        while (rs.next())
        {
          User user = buildUserFromResultSet(rs);

          list.add(user);
        }

        return list;
      }
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
    String getUsersSQL = "SELECT id, username, status, first_name, "
        + "last_name, phone, mobile, email, password, password_attempts, password_expiry "
        + "FROM security.users";

    if (StringUtils.isEmpty(filter))
    {
      getUsersSQL += " WHERE user_directory_id=?";
    }
    else
    {
      getUsersSQL +=
          " WHERE user_directory_id=? AND ((UPPER(username) LIKE ?) OR (UPPER(first_name) LIKE ?) "
          + "OR (UPPER(last_name) LIKE ?))";
    }

    if ((sortBy != null) && (sortDirection != null))
    {
      if (sortBy == UserSortBy.FIRST_NAME)
      {
        getUsersSQL += " ORDER BY first_name";
      }
      else if (sortBy == UserSortBy.LAST_NAME)
      {
        getUsersSQL += " ORDER BY last_name";
      }
      else if (sortBy == UserSortBy.USERNAME)
      {
        getUsersSQL += " ORDER BY username";
      }

      if (sortDirection == SortDirection.ASCENDING)
      {
        getUsersSQL += " ASC";
      }
      else
      {
        getUsersSQL += " DESC";
      }
    }

    if ((pageIndex != null) && (pageSize != null))
    {
      getUsersSQL += " LIMIT " + pageSize + " OFFSET " + (pageIndex * pageSize);
    }
    else
    {
      getUsersSQL += " LIMIT " + maxFilteredUsers;
    }

    try (Connection connection = dataSource.getConnection();
      PreparedStatement statement = connection.prepareStatement(getUsersSQL))
    {
      statement.setMaxRows(maxFilteredUsers);

      if (StringUtils.isEmpty(filter))
      {
        statement.setObject(1, getUserDirectoryId());
      }
      else
      {
        StringBuilder filterBuffer = new StringBuilder("%");

        filterBuffer.append(filter.toUpperCase());
        filterBuffer.append("%");

        statement.setObject(1, getUserDirectoryId());
        statement.setString(2, filterBuffer.toString());
        statement.setString(3, filterBuffer.toString());
        statement.setString(4, filterBuffer.toString());
      }

      try (ResultSet rs = statement.executeQuery())
      {
        List<User> list = new ArrayList<>();

        while (rs.next())
        {
          User user = buildUserFromResultSet(rs);

          list.add(user);
        }

        return list;
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
   *         otherwise
   */
  @Override
  public boolean isExistingUser(String username)
    throws SecurityServiceException
  {
    try (Connection connection = dataSource.getConnection())
    {
      return (getUserId(connection, username) != null);
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
   *         otherwise
   */
  @Override
  public boolean isUserInGroup(String username, String groupName)
    throws UserNotFoundException, GroupNotFoundException, SecurityServiceException
  {
    try (Connection connection = dataSource.getConnection())
    {
      // Get the ID of the user with the specified username
      UUID userId = getUserId(connection, username);

      if (userId == null)
      {
        throw new UserNotFoundException(username);
      }

      // Get the ID of the internal security group with the specified group name
      UUID groupId = getGroupId(connection, groupName);

      if (groupId == null)
      {
        throw new GroupNotFoundException(groupName);
      }

      // Get the current internal security groups for the user
      return isUserInGroup(connection, userId, groupId);
    }
    catch (UserNotFoundException | GroupNotFoundException e)
    {
      throw e;
    }
    catch (Throwable e)
    {
      throw new SecurityServiceException(String.format(
          "Failed to check if the user (%s) is in the security group (%s) for the user directory (%s)",
          username, groupName, getUserDirectoryId()), e);
    }
  }

  /**
   * Remove the user from the security group.
   *
   * @param username  the username identifying the user
   * @param groupName the security group name
   */
  @Override
  public void removeUserFromGroup(String username, String groupName)
    throws UserNotFoundException, GroupNotFoundException, SecurityServiceException
  {
    String removeUserFromGroupSQL = "DELETE FROM security.user_to_group_map "
        + "WHERE user_id=? AND group_id=?";

    try (Connection connection = dataSource.getConnection();
      PreparedStatement statement = connection.prepareStatement(removeUserFromGroupSQL))
    {
      // Get the ID of the user with the specified username
      UUID userId = getUserId(connection, username);

      if (userId == null)
      {
        throw new UserNotFoundException(username);
      }

      // Get the ID of the group with the specified group name
      UUID groupId = getGroupId(connection, groupName);

      if (groupId == null)
      {
        throw new GroupNotFoundException(groupName);
      }

      // Remove the user from the group
      statement.setObject(1, userId);
      statement.setObject(2, groupId);
      statement.executeUpdate();
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
   *         <code>false</code> otherwise
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
   *         <code>false</code> otherwise
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
    String updateGroupSQL =
        "UPDATE security.groups SET description=? WHERE user_directory_id=? AND id=?";

    try (Connection connection = dataSource.getConnection();
      PreparedStatement statement = connection.prepareStatement(updateGroupSQL))
    {
      UUID groupId = getGroupId(connection, group.getGroupName());

      if (groupId == null)
      {
        throw new GroupNotFoundException(group.getGroupName());
      }

      String description = group.getDescription();

      statement.setString(1,
          StringUtils.isEmpty(description)
          ? ""
          : description);
      statement.setObject(2, getUserDirectoryId());
      statement.setObject(3, groupId);

      if (statement.executeUpdate() <= 0)
      {
        throw new SecurityServiceException(String.format(
            "No rows were affected as a result of executing the SQL statement (%s)",
            updateGroupSQL));
      }
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
  public void updateUser(User user, boolean expirePassword, boolean lockUser)
    throws UserNotFoundException, SecurityServiceException
  {
    try (Connection connection = dataSource.getConnection())
    {
      UUID userId = getUserId(connection, user.getUsername());

      if (userId == null)
      {
        throw new UserNotFoundException(user.getUsername());
      }

      StringBuilder buffer = new StringBuilder();

      buffer.append("UPDATE security.users ");

      StringBuilder fieldsBuffer = new StringBuilder();

      if (user.getFirstName() != null)
      {
        fieldsBuffer.append((fieldsBuffer.length() == 0)
            ? "SET first_name=?"
            : ", first_name=?");
      }

      if (user.getLastName() != null)
      {
        fieldsBuffer.append((fieldsBuffer.length() == 0)
            ? "SET last_name=?"
            : ", last_name=?");
      }

      if (user.getEmail() != null)
      {
        fieldsBuffer.append((fieldsBuffer.length() == 0)
            ? "SET email=?"
            : ", email=?");
      }

      if (user.getPhoneNumber() != null)
      {
        fieldsBuffer.append((fieldsBuffer.length() == 0)
            ? "SET phone=?"
            : ", phone=?");
      }

      if (user.getMobileNumber() != null)
      {
        fieldsBuffer.append((fieldsBuffer.length() == 0)
            ? "SET mobile=?"
            : ", mobile=?");
      }

      if (!StringUtils.isEmpty(user.getPassword()))
      {
        fieldsBuffer.append((fieldsBuffer.length() == 0)
            ? "SET password=?"
            : ", password=?");
      }

      if (lockUser || (user.getPasswordAttempts() != null))
      {
        fieldsBuffer.append((fieldsBuffer.length() == 0) ? "SET password_attempts=?" : ", password_attempts=?");
      }

      if (expirePassword || (user.getPasswordExpiry() != null))
      {
        fieldsBuffer.append((fieldsBuffer.length() == 0) ? "SET password_expiry=?" : ", password_expiry=?");
      }

      buffer.append(fieldsBuffer.toString());
      buffer.append(" WHERE user_directory_id=? AND id=?");

      String updateUserSQL = buffer.toString();

      try (PreparedStatement statement = connection.prepareStatement(updateUserSQL))
      {
        int parameterIndex = 1;

        if (user.getFirstName() != null)
        {
          statement.setString(parameterIndex, user.getFirstName());
          parameterIndex++;
        }

        if (user.getLastName() != null)
        {
          statement.setString(parameterIndex, user.getLastName());
          parameterIndex++;
        }

        if (user.getEmail() != null)
        {
          statement.setString(parameterIndex, user.getEmail());
          parameterIndex++;
        }

        if (user.getPhoneNumber() != null)
        {
          statement.setString(parameterIndex, user.getPhoneNumber());
          parameterIndex++;
        }

        if (user.getMobileNumber() != null)
        {
          statement.setString(parameterIndex, user.getMobileNumber());
          parameterIndex++;
        }

        if (user.getPassword() != null)
        {
          if (user.getPassword().length() > 0)
          {
            statement.setString(parameterIndex, createPasswordHash(user.getPassword()));
          }
          else
          {
            statement.setString(parameterIndex, "");
          }

          parameterIndex++;
        }

        if (lockUser)
        {
          statement.setInt(parameterIndex, maxPasswordAttempts);
          parameterIndex++;
        }
        else if (user.getPasswordAttempts() != null)
        {
          statement.setInt(parameterIndex, user.getPasswordAttempts());
          parameterIndex++;
        }

        if (expirePassword)
        {
          statement.setTimestamp(parameterIndex, new Timestamp(System.currentTimeMillis()));
          parameterIndex++;
        }
        else if (user.getPasswordExpiry() != null)
        {
          statement.setTimestamp(parameterIndex, Timestamp.valueOf(user.getPasswordExpiry()));
          parameterIndex++;
        }

        statement.setObject(parameterIndex, getUserDirectoryId());

        parameterIndex++;

        statement.setObject(parameterIndex, userId);

        if (statement.executeUpdate() != 1)
        {
          throw new SecurityServiceException(String.format(
              "No rows were affected as a result of executing the SQL statement (%s)",
              updateUserSQL));
        }
      }
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

  /**
   * Build the JDBC <code>PreparedStatement</code> for the SQL query that will select the users
   * in the USERS table using the values of the specified attributes as the selection criteria.
   *
   * @param connection the existing database connection to use
   * @param attributes the attributes to be used as the selection criteria
   *
   * @return the <code>PreparedStatement</code> for the SQL query that will select the users in the
   *         USERS table using the values of the specified attributes as the selection criteria
   */
  private PreparedStatement buildFindUsersStatement(Connection connection,
      List<Attribute> attributes)
    throws InvalidAttributeException, SQLException
  {
    // Build the SQL statement to select the users
    StringBuilder buffer = new StringBuilder();

    buffer.append("SELECT id, username, status, first_name, last_name, phone, mobile, email, ");
    buffer.append("password, password_attempts, password_expiry FROM security.users");

    if (attributes.size() > 0)
    {
      // Build the parameters for the "WHERE" clause for the SQL statement
      StringBuilder whereParameters = new StringBuilder();

      for (Attribute attribute : attributes)
      {
        whereParameters.append(" AND ");

        if (attribute.getName().equalsIgnoreCase("status"))
        {
          whereParameters.append("status = ?");
        }
        else if (attribute.getName().equalsIgnoreCase("email"))
        {
          whereParameters.append("LOWER(email) LIKE LOWER(?)");
        }
        else if (attribute.getName().equalsIgnoreCase("firstName"))
        {
          whereParameters.append("LOWER(first_name) LIKE LOWER(?)");
        }
        else if (attribute.getName().equalsIgnoreCase("lastName"))
        {
          whereParameters.append("LOWER(last_name) LIKE LOWER(?)");
        }
        else if (attribute.getName().equalsIgnoreCase("phoneNumber"))
        {
          whereParameters.append("LOWER(phone) LIKE LOWER(?)");
        }
        else if (attribute.getName().equalsIgnoreCase("mobileNumber"))
        {
          whereParameters.append("LOWER(mobile) LIKE LOWER(?)");
        }
        else if (attribute.getName().equalsIgnoreCase("username"))
        {
          whereParameters.append("LOWER(username) LIKE LOWER(?)");
        }
        else
        {
          throw new InvalidAttributeException(attribute.getName());
        }
      }

      buffer.append(" WHERE user_directory_id=?");
      buffer.append(whereParameters.toString());
    }
    else
    {
      buffer.append(" WHERE user_directory_id=?");
    }

    PreparedStatement statement = connection.prepareStatement(buffer.toString());

    statement.setObject(1, getUserDirectoryId());

    // Set the parameters for the prepared statement
    int parameterIndex = 2;

    for (Attribute attribute : attributes)
    {
      if (attribute.getName().equalsIgnoreCase("status"))
      {
        statement.setInt(parameterIndex, Integer.parseInt(attribute.getStringValue()));
        parameterIndex++;
      }
      else if (attribute.getName().equalsIgnoreCase("email"))
      {
        statement.setString(parameterIndex, attribute.getStringValue());
        parameterIndex++;
      }
      else if (attribute.getName().equalsIgnoreCase("firstName"))
      {
        statement.setString(parameterIndex, attribute.getStringValue());
        parameterIndex++;
      }
      else if (attribute.getName().equalsIgnoreCase("lastName"))
      {
        statement.setString(parameterIndex, attribute.getStringValue());
        parameterIndex++;
      }
      else if (attribute.getName().equalsIgnoreCase("phoneNumber"))
      {
        statement.setString(parameterIndex, attribute.getStringValue());
        parameterIndex++;
      }
      else if (attribute.getName().equalsIgnoreCase("mobileNumber"))
      {
        statement.setString(parameterIndex, attribute.getStringValue());
        parameterIndex++;
      }
      else if (attribute.getName().equalsIgnoreCase("username"))
      {
        statement.setString(parameterIndex, attribute.getStringValue());
        parameterIndex++;
      }
    }

    return statement;
  }

  /**
   * Create a new <code>User</code> instance and populate it with the contents of the current
   * row in the specified <code>ResultSet</code>.
   *
   * @param rs the <code>ResultSet</code> whose current row will be used to populate the
   *           <code>User</code> instance
   *
   * @return the populated <code>User</code> instance
   */
  private User buildUserFromResultSet(ResultSet rs)
    throws SQLException
  {
    User user = new User();

    user.setId(UUID.fromString(rs.getString(1)));
    user.setUsername(rs.getString(2));
    user.setUserDirectoryId(getUserDirectoryId());
    user.setStatus(UserStatus.fromCode(rs.getInt(3)));

    String firstName = rs.getString(4);

    user.setFirstName(StringUtils.isEmpty(firstName)
        ? ""
        : firstName);

    String lastName = rs.getString(5);

    user.setLastName(StringUtils.isEmpty(lastName)
        ? ""
        : lastName);

    String phoneNumber = rs.getString(6);

    user.setPhoneNumber(StringUtils.isEmpty(phoneNumber)
        ? ""
        : phoneNumber);

    String mobilePhoneNumber = rs.getString(7);

    user.setMobileNumber(StringUtils.isEmpty(mobilePhoneNumber)
        ? ""
        : mobilePhoneNumber);

    String email = rs.getString(8);

    user.setEmail(StringUtils.isEmpty(email)
        ? ""
        : email);

    String password = rs.getString(9);

    user.setPassword(StringUtils.isEmpty(password)
        ? ""
        : password);

    if (rs.getObject(10) != null)
    {
      user.setPasswordAttempts(rs.getInt(10));
    }

    if (rs.getObject(11) != null)
    {
      user.setPasswordExpiry(rs.getTimestamp(11).toLocalDateTime());
    }

    return user;
  }

  /**
   * Retrieve the authorised function codes for the user.
   *
   * @param connection the existing database connection to use
   * @param userId     the Universally Unique Identifier (UUID) used to uniquely identify the user
   *
   * @return the authorised function codes for the user
   */
  private List<String> getFunctionCodesForUserId(Connection connection, UUID userId)
    throws SQLException
  {
    String getFunctionCodesForUserIdSQL = "SELECT DISTINCT f.code FROM security.functions f "
        + "INNER JOIN security.function_to_role_map ftrm ON ftrm.function_id = f.id "
        + "INNER JOIN security.role_to_group_map rtgm ON rtgm.role_id = ftrm.role_id "
        + "INNER JOIN security.groups g ON g.id = rtgm.group_id "
        + "INNER JOIN security.user_to_group_map utgm ON utgm.group_id = g.ID WHERE utgm.user_id=?";

    List<String> functionCodes = new ArrayList<>();

    try (PreparedStatement statement = connection.prepareStatement(getFunctionCodesForUserIdSQL))
    {
      statement.setObject(1, userId);

      try (ResultSet rs = statement.executeQuery())
      {
        while (rs.next())
        {
          functionCodes.add(rs.getString(1));
        }

        return functionCodes;
      }
    }
  }

  /**
   * Retrieve the names for all the security groups that the user with the specific numeric
   * ID is associated with.
   *
   * @param connection the existing database connection
   * @param userId     the Universally Unique Identifier (UUID) used to uniquely identify the user
   *
   * @return the security groups
   */
  private List<String> getGroupNamesForUser(Connection connection, UUID userId)
    throws SQLException
  {
    String getGroupNamesForUserSQL = "SELECT groupname FROM "
        + "security.groups g, security.user_to_group_map utgm "
        + "WHERE g.id = utgm.group_id AND utgm.user_id=? ORDER BY g.groupname";

    try (PreparedStatement statement = connection.prepareStatement(getGroupNamesForUserSQL))
    {
      statement.setObject(1, userId);

      try (ResultSet rs = statement.executeQuery())
      {
        List<String> list = new ArrayList<>();

        while (rs.next())
        {
          list.add(rs.getString(1));
        }

        return list;
      }
    }
  }

  /**
   * Retrieve all the internal security groups that the user with the specific numeric ID
   * is associated with.
   *
   * @param connection the existing database connection
   * @param userId     the Universally Unique Identifier (UUID) used to uniquely identify the user
   *
   * @return the internal security groups
   */
  private List<Group> getGroupsForUser(Connection connection, UUID userId)
    throws SQLException
  {
    String getGroupsForUserSQL = "SELECT id, groupname, description FROM "
        + "security.groups g, security.user_to_group_map utgm "
        + "WHERE g.id = utgm.group_id AND utgm.user_id=? " + "ORDER BY g.groupname";

    try (PreparedStatement statement = connection.prepareStatement(getGroupsForUserSQL))
    {
      statement.setObject(1, userId);

      try (ResultSet rs = statement.executeQuery())
      {
        List<Group> list = new ArrayList<>();

        while (rs.next())
        {
          Group group = new Group(rs.getString(2));

          group.setId(UUID.fromString(rs.getString(1)));
          group.setUserDirectoryId(getUserDirectoryId());
          group.setDescription(rs.getString(3));
          list.add(group);
        }

        return list;
      }
    }
  }

  /**
   * Retrieve the number of users for the internal security group.
   *
   * @param connection the existing database connection
   * @param groupId    the Universally Unique Identifier (UUID) used to uniquely identify the
   *                   internal security group
   *
   * @return the IDs for all the users that are associated with the internal security group
   *         with the specified ID
   */
  private long getNumberOfUsersForGroup(Connection connection, UUID groupId)
    throws SQLException
  {
    String getNumberOfUsersForGroupSQL = "SELECT COUNT (user_id) FROM security.user_to_group_map "
        + "WHERE group_id=?";

    try (PreparedStatement statement = connection.prepareStatement(getNumberOfUsersForGroupSQL))
    {
      statement.setObject(1, groupId);

      try (ResultSet rs = statement.executeQuery())
      {
        if (rs.next())
        {
          return rs.getLong(1);
        }
        else
        {
          return 0;
        }
      }
    }
  }

  /**
   * Retrieve the names for the roles that the user has been assigned.
   *
   * @param connection the existing database connection to use
   * @param userId     the Universally Unique Identifier (UUID) used to uniquely identify the user
   *
   * @return the the names for the roles that the user has been assigned
   */
  private List<String> getRoleNamesForUserId(Connection connection, UUID userId)
    throws SQLException
  {
    String getRoleNamesForUserIdSQL = "SELECT DISTINCT r.name FROM security.roles r "
        + "INNER JOIN security.role_to_group_map rtgm ON rtgm.role_id = r.id "
        + "INNER JOIN security.groups g ON g.id = rtgm.group_id "
        + "INNER JOIN security.user_to_group_map utgm ON utgm.group_id = g.ID WHERE utgm.user_id=?";

    List<String> functionCodes = new ArrayList<>();

    try (PreparedStatement statement = connection.prepareStatement(getRoleNamesForUserIdSQL))
    {
      statement.setObject(1, userId);

      try (ResultSet rs = statement.executeQuery())
      {
        while (rs.next())
        {
          functionCodes.add(rs.getString(1));
        }

        return functionCodes;
      }
    }
  }

  /**
   * Retrieve the information for the user with the specified username.
   *
   * @param connection the existing database connection to use
   * @param username   the username identifying the user
   *
   * @return the <code>User</code> or <code>null</code> if the user could not be found
   */
  private User getUser(Connection connection, String username)
    throws SQLException
  {
    String getUserSQL = "SELECT id, username, status, first_name, last_name, phone, "
        + "mobile, email, password, password_attempts, password_expiry FROM security.users "
        + "WHERE user_directory_id=? AND UPPER(username)=UPPER(CAST(? AS VARCHAR(100)))";

    try (PreparedStatement statement = connection.prepareStatement(getUserSQL))
    {
      statement.setObject(1, getUserDirectoryId());
      statement.setString(2, username);

      try (ResultSet rs = statement.executeQuery())
      {
        if (rs.next())
        {
          return buildUserFromResultSet(rs);
        }
        else
        {
          return null;
        }
      }
    }
  }

  /**
   * Returns the Universally Unique Identifier (UUID) used to uniquely identify the user
   * with the specified username.
   *
   * @param connection the existing database connection to use
   * @param username   the username uniquely identifying the user
   *
   * @return the Universally Unique Identifier (UUID) used to uniquely identify the user
   *         with the specified username
   */
  private UUID getUserId(Connection connection, String username)
    throws SecurityServiceException
  {
    String getUserIdSQL = "SELECT id FROM security.users "
        + "WHERE user_directory_id=? AND UPPER(username)=UPPER(CAST(? AS VARCHAR(100)))";

    try (PreparedStatement statement = connection.prepareStatement(getUserIdSQL))
    {
      statement.setObject(1, getUserDirectoryId());
      statement.setString(2, username);

      try (ResultSet rs = statement.executeQuery())
      {
        if (rs.next())
        {
          return UUID.fromString(rs.getString(1));
        }
        else
        {
          return null;
        }
      }
    }
    catch (Throwable e)
    {
      throw new SecurityServiceException(String.format(
          "Failed to retrieve the ID for the user (%s) for the user directory (%s)", username,
          getUserDirectoryId()), e);
    }
  }

  /**
   * Increment the password attempts for the user.
   *
   * @param userId the Universally Unique Identifier (UUID) used to uniquely identify the user
   */
  private void incrementPasswordAttempts(UUID userId)
    throws SecurityServiceException
  {
    String incrementPasswordAttemptsSQL = "UPDATE security.users "
        + "SET password_attempts = password_attempts + 1 WHERE user_directory_id=? AND id=?";

    try (Connection connection = dataSource.getConnection();
      PreparedStatement statement = connection.prepareStatement(incrementPasswordAttemptsSQL))
    {
      statement.setObject(1, getUserDirectoryId());
      statement.setObject(2, userId);

      if (statement.executeUpdate() != 1)
      {
        throw new SecurityServiceException(String.format(
            "No rows were affected as a result of executing the SQL statement (%s)",
            incrementPasswordAttemptsSQL));
      }
    }
    catch (Throwable e)
    {
      throw new SecurityServiceException(String.format("Failed to increment the password attempts "
          + "for the user (%s) for the user directory (%s)", userId, getUserDirectoryId()), e);
    }
  }

  /**
   * Is the password, given by the specified password hash, a historical password that cannot
   * be reused for a period of time i.e. was the password used previously in the last X months.
   *
   * @param connection   the existing database connection
   * @param userId       the Universally Unique Identifier (UUID) used to uniquely identify the user
   * @param passwordHash the password hash
   *
   * @return <code>true</code> if the password was previously used and cannot be reused for a
   *         period of time or <code>false</code> otherwise
   */
  private boolean isPasswordInHistory(Connection connection, UUID userId, String passwordHash)
    throws SQLException
  {
    String isPasswordInUserPasswordHistorySQL = "SELECT id FROM  security.users_password_history "
        + "WHERE user_id=? AND changed > ? AND password=?";

    try (PreparedStatement statement = connection.prepareStatement(
        isPasswordInUserPasswordHistorySQL))
    {
      Calendar calendar = Calendar.getInstance();

      calendar.setTime(new Date());
      calendar.add(Calendar.MONTH, -1 * passwordHistoryMonths);

      statement.setObject(1, userId);
      statement.setTimestamp(2, new Timestamp(calendar.getTimeInMillis()));
      statement.setString(3, passwordHash);

      try (ResultSet rs = statement.executeQuery())
      {
        return rs.next();
      }
    }
  }

  /**
   * Is the user in the security group?
   *
   * @param connection the existing database connection
   * @param userId     the Universally Unique Identifier (UUID) used to uniquely identify the user
   * @param groupId    the Universally Unique Identifier (UUID) used to uniquely identify the
   *                   internal security group
   *
   * @return <code>true</code> if the user is a member of the security group or <code>false</code>
   *         otherwise
   */
  private boolean isUserInGroup(Connection connection, UUID userId, UUID groupId)
    throws SQLException
  {
    String isUserInGroupSQL = "SELECT user_id FROM "
        + "security.user_to_group_map WHERE user_id=? AND " + "group_id=?";

    try (PreparedStatement statement = connection.prepareStatement(isUserInGroupSQL))
    {
      statement.setObject(1, userId);
      statement.setObject(2, groupId);

      try (ResultSet rs = statement.executeQuery())
      {
        return rs.next();
      }
    }
  }

  private void savePasswordHistory(Connection connection, UUID userId, String passwordHash)
    throws SQLException
  {
    String saveUserPasswordHistorySQL = "INSERT INTO security.users_password_history "
        + "(id, user_id, changed, password) VALUES (?, ?, ?, ?)";

    try (PreparedStatement statement = connection.prepareStatement(saveUserPasswordHistorySQL))
    {
      UUID id = idGenerator.nextUUID();

      statement.setObject(1, id);
      statement.setObject(2, userId);
      statement.setTimestamp(3, new Timestamp(System.currentTimeMillis()));
      statement.setString(4, passwordHash);
      statement.execute();
    }
  }
}
