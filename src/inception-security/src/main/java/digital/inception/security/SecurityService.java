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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

//~--- JDK imports ------------------------------------------------------------

import java.lang.reflect.Constructor;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import javax.sql.DataSource;

/**
 * The <code>SecurityService</code> class provides the Security Service implementation.
 *
 * @author Marcus Portmann
 */
@Service
@SuppressWarnings({ "unused", "WeakerAccess" })
public class SecurityService
  implements ISecurityService, InitializingBean
{
  /**
   * The name of the Administrators group.
   */
  public static final String ADMINISTRATORS_GROUP_NAME = "Administrators";

  /**
   * The Universally Unique Identifier (UUID) used to uniquely identify the default internal user
   * directory.
   */
  public static final UUID DEFAULT_INTERNAL_USER_DIRECTORY_ID = UUID.fromString(
      "4ef18395-423a-4df6-b7d7-6bcdd85956e4");

  /**
   * The Universally Unique Identifier (UUID) used to uniquely identify the internal user directory
   * type.
   */
  public static final UUID INTERNAL_USER_DIRECTORY_TYPE_ID = UUID.fromString(
      "b43fda33-d3b0-4f80-a39a-110b8e530f4f");

  /**
   * The Universally Unique Identifier (UUID) used to uniquely identify the LDAP user directory
   * type.
   */
  public static final UUID LDAP_USER_DIRECTORY_TYPE_ID = UUID.fromString(
      "e5741a89-c87b-4406-8a60-2cc0b0a5fa3e");

  /**
   * The maximum number of filtered organizations.
   */
  private static final int MAX_FILTERED_ORGANISATIONS = 100;

  /**
   * The maximum number of filtered user directories.
   */
  private static final int MAX_FILTERED_USER_DIRECTORIES = 100;

  /* Logger */
  private static final Logger logger = LoggerFactory.getLogger(SecurityService.class);
  private Map<UUID, IUserDirectory> userDirectories = new ConcurrentHashMap<>();
  private Map<UUID, UserDirectoryType> userDirectoryTypes = new ConcurrentHashMap<>();

  /**
   * The Spring application context.
   */
  private ApplicationContext applicationContext;

  /**
   * The data source used to provide connections to the application database.
   */
  private DataSource dataSource;

  /**
   * The ID generator.
   */
  private IDGenerator idGenerator;

  /**
   * Constructs a new <code>SecurityService</code>.
   *
   * @param applicationContext the Spring application context
   * @param dataSource         the data source used to provide connections to the application
   *                           database
   * @param idGenerator        the ID generator
   */
  public SecurityService(ApplicationContext applicationContext, @Qualifier(
      "applicationDataSource") DataSource dataSource, IDGenerator idGenerator)
  {
    this.applicationContext = applicationContext;
    this.dataSource = dataSource;
    this.idGenerator = idGenerator;
  }

  /**
   * Add the user to the security group.
   *
   * @param userDirectoryId the Universally Unique Identifier (UUID) used to uniquely identify the
   *                        user directory
   * @param username        the username identifying the user
   * @param groupName       the name of the security group uniquely identifying the security group
   */
  @Override
  public void addUserToGroup(UUID userDirectoryId, String username, String groupName)
    throws UserDirectoryNotFoundException, UserNotFoundException, GroupNotFoundException,
        SecurityServiceException
  {
    IUserDirectory userDirectory = userDirectories.get(userDirectoryId);

    if (userDirectory == null)
    {
      throw new UserDirectoryNotFoundException(userDirectoryId);
    }

    userDirectory.addUserToGroup(username, groupName);
  }

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
  @Override
  public void adminChangePassword(UUID userDirectoryId, String username, String newPassword,
      boolean expirePassword, boolean lockUser, boolean resetPasswordHistory,
      PasswordChangeReason reason)
    throws UserDirectoryNotFoundException, UserNotFoundException, SecurityServiceException
  {
    IUserDirectory userDirectory = userDirectories.get(userDirectoryId);

    if (userDirectory == null)
    {
      throw new UserDirectoryNotFoundException(userDirectoryId);
    }

    userDirectory.adminChangePassword(username, newPassword, expirePassword, lockUser,
        resetPasswordHistory, reason);
  }

  /**
   * Initialize the Security Service.
   */
  @Override
  public void afterPropertiesSet()
  {
    try
    {
      // Load the user directory types
      reloadUserDirectoryTypes();

      // Load the user directories
      reloadUserDirectories();
    }
    catch (Throwable e)
    {
      throw new RuntimeException("Failed to initialize the Security Service", e);
    }
  }

  /**
   * Authenticate the user.
   *
   * @param username the username identifying the user
   * @param password the password being used to authenticate
   *
   * @return the Universally Unique Identifier (UUID) used to uniquely identify the user directory
   */
  @Override
  public UUID authenticate(String username, String password)
    throws AuthenticationFailedException, UserLockedException, ExpiredPasswordException,
        UserNotFoundException, SecurityServiceException
  {
    try
    {
      // First check if this is an internal user and if so determine the user directory ID
      UUID internalUserDirectoryId = getInternalUserDirectoryIdForUser(username);

      if (internalUserDirectoryId != null)
      {
        IUserDirectory internalUserDirectory = userDirectories.get(internalUserDirectoryId);

        if (internalUserDirectory == null)
        {
          throw new SecurityServiceException(String.format(
              "The user directory ID (%s) for the internal user (%s) is invalid",
              internalUserDirectoryId, username));
        }
        else
        {
          internalUserDirectory.authenticate(username, password);

          return internalUserDirectoryId;
        }
      }
      else
      {
        /*
         * Check all of the "external" user directories to see if one of them can authenticate this
         * user.
         */
        for (UUID userDirectoryId : userDirectories.keySet())
        {
          IUserDirectory userDirectory = userDirectories.get(userDirectoryId);

          if (userDirectory != null)
          {
            if (!(userDirectory instanceof InternalUserDirectory))
            {
              if (userDirectory.isExistingUser(username))
              {
                userDirectory.authenticate(username, password);

                return userDirectoryId;
              }
            }
          }
        }

        throw new UserNotFoundException(username);
      }
    }
    catch (AuthenticationFailedException | UserNotFoundException | UserLockedException
        | ExpiredPasswordException e)
    {
      throw e;
    }
    catch (Throwable e)
    {
      throw new SecurityServiceException(String.format("Failed to authenticate the user (%s)",
          username), e);
    }
  }

  /**
   * Change the password for the user.
   *
   * @param username    the username identifying the user
   * @param password    the password for the user that is used to authorise the operation
   * @param newPassword the new password
   *
   * @return the Universally Unique Identifier (UUID) used to uniquely identify the user directory
   */
  @Override
  public UUID changePassword(String username, String password, String newPassword)
    throws AuthenticationFailedException, UserLockedException, UserNotFoundException,
        ExistingPasswordException, SecurityServiceException
  {
    try
    {
      // First check if this is an internal user and if so determine the user directory ID
      UUID internalUserDirectoryId = getInternalUserDirectoryIdForUser(username);

      if (internalUserDirectoryId != null)
      {
        IUserDirectory internalUserDirectory = userDirectories.get(internalUserDirectoryId);

        if (internalUserDirectory == null)
        {
          throw new SecurityServiceException(String.format(
              "The user directory ID (%s) for the internal user (%s) is invalid",
              internalUserDirectoryId, username));
        }
        else
        {
          internalUserDirectory.changePassword(username, password, newPassword);

          return internalUserDirectoryId;
        }
      }
      else
      {
        /*
         * Check all of the "external" user directories to see if one of them can change the
         * password for this user.
         */
        for (UUID userDirectoryId : userDirectories.keySet())
        {
          IUserDirectory userDirectory = userDirectories.get(userDirectoryId);

          if (userDirectory != null)
          {
            if (!(userDirectory instanceof InternalUserDirectory))
            {
              if (userDirectory.isExistingUser(username))
              {
                userDirectory.changePassword(username, password, newPassword);

                return userDirectoryId;
              }
            }
          }
        }

        throw new UserNotFoundException(username);
      }
    }
    catch (AuthenticationFailedException | UserNotFoundException | UserLockedException
        | ExistingPasswordException e)
    {
      throw e;
    }
    catch (Throwable e)
    {
      throw new SecurityServiceException(String.format(
          "Failed to change the password for the user (%s)", username), e);
    }
  }

  /**
   * Create the new authorised function.
   *
   * @param function the function
   */
  @Override
  public void createFunction(Function function)
    throws DuplicateFunctionException, SecurityServiceException

  {
    String createFunctionSQL =
        "INSERT INTO security.functions (id, code, name, description) VALUES (?, ?, ?, ?)";

    try (Connection connection = dataSource.getConnection();
      PreparedStatement statement = connection.prepareStatement(createFunctionSQL))
    {
      if (getFunctionId(connection, function.getCode()) != null)
      {
        throw new DuplicateFunctionException(function.getCode());
      }

      statement.setObject(1, function.getId());
      statement.setString(2, function.getCode());
      statement.setString(3, function.getName());
      statement.setString(4, function.getDescription());

      if (statement.executeUpdate() != 1)
      {
        throw new SecurityServiceException(String.format(
            "No rows were affected as a result of executing the SQL statement (%s)",
            createFunctionSQL));
      }
    }
    catch (DuplicateFunctionException e)
    {
      throw e;
    }
    catch (Throwable e)
    {
      throw new SecurityServiceException(String.format("Failed to create the function (%s)",
          function.getCode()), e);
    }
  }

  /**
   * Create the new security group.
   *
   * @param userDirectoryId the Universally Unique Identifier (UUID) used to uniquely identify the
   *                        user directory
   * @param group           the security group
   */
  @Override
  public void createGroup(UUID userDirectoryId, Group group)
    throws UserDirectoryNotFoundException, DuplicateGroupException, SecurityServiceException
  {
    IUserDirectory userDirectory = userDirectories.get(userDirectoryId);

    if (userDirectory == null)
    {
      throw new UserDirectoryNotFoundException(userDirectoryId);
    }

    userDirectory.createGroup(group);
  }

  /**
   * Create the new organization.
   *
   * @param organization        the organization
   * @param createUserDirectory should a new internal user directory be created for the organization
   *
   * @return the new internal user directory that was created for the organization or
   *         <code>null</code> if no user directory was created
   */
  @Override
  @Transactional(rollbackFor = Exception.class)
  public UserDirectory createOrganization(Organization organization, boolean createUserDirectory)
    throws DuplicateOrganizationException, SecurityServiceException
  {
    try
    {
      UserDirectory userDirectory = null;

      try (Connection connection = dataSource.getConnection())
      {
        String createOrganizationSQL =
            "INSERT INTO security.organizations (id, name, status) VALUES (?, ?, ?)";

        try (PreparedStatement statement = connection.prepareStatement(createOrganizationSQL))
        {
          if (organizationWithIdExists(connection, organization.getId()))
          {
            throw new DuplicateOrganizationException(organization.getId());
          }

          if (organizationWithNameExists(connection, organization.getName()))
          {
            throw new DuplicateOrganizationException(organization.getName());
          }

          statement.setObject(1, organization.getId());
          statement.setString(2, organization.getName());
          statement.setInt(3, organization.getStatus().code());

          if (statement.executeUpdate() != 1)
          {
            throw new SecurityServiceException(String.format(
                "No rows were affected as a result of executing the SQL statement (%s)",
                createOrganizationSQL));
          }
        }

        String addUserDirectoryToOrganizationSQL =
            "INSERT INTO security.user_directory_to_organization_map "
            + "(user_directory_id, organization_id) VALUES (?, ?)";

        if (createUserDirectory)
        {
          userDirectory = newInternalUserDirectoryForOrganization(organization);

          String createUserDirectorySQL = "INSERT INTO security.user_directories "
              + "(id, type_id, name, configuration) VALUES (?, ?, ?, ?)";

          try (PreparedStatement statement = connection.prepareStatement(createUserDirectorySQL))
          {
            statement.setObject(1, userDirectory.getId());
            statement.setObject(2, userDirectory.getTypeId());
            statement.setString(3, userDirectory.getName());
            statement.setString(4, userDirectory.getConfiguration());

            if (statement.executeUpdate() != 1)
            {
              throw new SecurityServiceException(String.format(
                  "No rows were affected as a result of executing the SQL statement (%s)",
                  createUserDirectorySQL));
            }
          }

          // Link the new user directory to the new organization
          try (PreparedStatement statement = connection.prepareStatement(
              addUserDirectoryToOrganizationSQL))
          {
            statement.setObject(1, userDirectory.getId());
            statement.setObject(2, organization.getId());

            if (statement.executeUpdate() != 1)
            {
              throw new SecurityServiceException(String.format(
                  "No rows were affected as a result of executing the SQL statement (%s)",
                  addUserDirectoryToOrganizationSQL));
            }
          }
        }

        // Link the new organization to the default user directory
        try (PreparedStatement statement = connection.prepareStatement(
            addUserDirectoryToOrganizationSQL))
        {
          statement.setObject(1, DEFAULT_INTERNAL_USER_DIRECTORY_ID);
          statement.setObject(2, organization.getId());

          if (statement.executeUpdate() != 1)
          {
            throw new SecurityServiceException(String.format(
                "No rows were affected as a result of executing the SQL statement (%s)",
                addUserDirectoryToOrganizationSQL));
          }
        }
      }

      try
      {
        reloadUserDirectories();
      }
      catch (Throwable e)
      {
        logger.error("Failed to reload the user directories", e);
      }

      return userDirectory;
    }
    catch (DuplicateOrganizationException e)
    {
      throw e;
    }
    catch (Throwable e)
    {
      throw new SecurityServiceException(String.format("Failed to create the organization (%s)",
          organization.getId()), e);
    }
  }

  /**
   * Create the new user.
   *
   * @param userDirectoryId the Universally Unique Identifier (UUID) used to uniquely identify the
   *                        user directory
   * @param user            the user
   * @param expiredPassword create the user with its password expired
   * @param userLocked      create the user locked
   */
  @Override
  public void createUser(UUID userDirectoryId, User user, boolean expiredPassword,
      boolean userLocked)
    throws UserDirectoryNotFoundException, DuplicateUserException, SecurityServiceException
  {
    IUserDirectory userDirectory = userDirectories.get(userDirectoryId);

    if (userDirectory == null)
    {
      throw new UserDirectoryNotFoundException(userDirectoryId);
    }

    userDirectory.createUser(user, expiredPassword, userLocked);
  }

  /**
   * Create the new user directory.
   *
   * @param userDirectory the user directory
   */
  @Override
  public void createUserDirectory(UserDirectory userDirectory)
    throws SecurityServiceException
  {
    String createUserDirectorySQL = "INSERT INTO security.user_directories "
        + "(id, type_id, name, configuration) VALUES (?, ?, ?, ?)";

    try (Connection connection = dataSource.getConnection();
      PreparedStatement statement = connection.prepareStatement(createUserDirectorySQL))
    {
      UUID userDirectoryId = idGenerator.nextUUID();

      statement.setObject(1, userDirectoryId);
      statement.setObject(2, userDirectory.getTypeId());
      statement.setString(3, userDirectory.getName());
      statement.setString(4, userDirectory.getConfiguration());

      if (statement.executeUpdate() != 1)
      {
        throw new SecurityServiceException(String.format(
            "No rows were affected as a result of executing the SQL statement (%s)",
            createUserDirectorySQL));
      }

      userDirectory.setId(userDirectoryId);

      try
      {
        reloadUserDirectories();
      }
      catch (Throwable e)
      {
        logger.error("Failed to reload the user directories", e);
      }
    }
    catch (Throwable e)
    {
      throw new SecurityServiceException(String.format("Failed to create the user directory (%s)",
          userDirectory.getName()), e);
    }
  }

  /**
   * Delete the authorised function.
   *
   * @param code the code identifying the authorised function
   */
  @Override
  public void deleteFunction(String code)
    throws FunctionNotFoundException, SecurityServiceException
  {
    String deleteFunctionSQL = "DELETE FROM security.functions WHERE code=?";

    try (Connection connection = dataSource.getConnection();
      PreparedStatement statement = connection.prepareStatement(deleteFunctionSQL))
    {
      if (getFunctionId(connection, code) == null)
      {
        throw new FunctionNotFoundException(code);
      }

      statement.setString(1, code);

      if (statement.executeUpdate() <= 0)
      {
        throw new SecurityServiceException(String.format(
            "No rows were affected as a result of executing the SQL statement (%s)",
            deleteFunctionSQL));
      }
    }
    catch (FunctionNotFoundException e)
    {
      throw e;
    }
    catch (Throwable e)
    {
      throw new SecurityServiceException(String.format("Failed to delete the function (%s)", code),
          e);
    }
  }

  /**
   * Delete the security group.
   *
   * @param userDirectoryId the Universally Unique Identifier (UUID) used to uniquely identify the
   *                        user directory
   * @param groupName       the name of the security group uniquely identifying the security group
   */
  @Override
  public void deleteGroup(UUID userDirectoryId, String groupName)
    throws UserDirectoryNotFoundException, GroupNotFoundException, ExistingGroupMembersException,
        SecurityServiceException
  {
    IUserDirectory userDirectory = userDirectories.get(userDirectoryId);

    if (userDirectory == null)
    {
      throw new UserDirectoryNotFoundException(userDirectoryId);
    }

    userDirectory.deleteGroup(groupName);
  }

  /**
   * Delete the organization.
   *
   * @param organizationId the Universally Unique Identifier (UUID) used to uniquely identify the
   *                       organization
   */
  @Override
  public void deleteOrganization(UUID organizationId)
    throws OrganizationNotFoundException, SecurityServiceException
  {
    String deleteOrganizationSQL = "DELETE FROM security.organizations WHERE id=?";

    try (Connection connection = dataSource.getConnection();
      PreparedStatement statement = connection.prepareStatement(deleteOrganizationSQL))
    {
      if (!organizationExists(connection, organizationId))
      {
        throw new OrganizationNotFoundException(organizationId);
      }

      statement.setObject(1, organizationId);

      if (statement.executeUpdate() <= 0)
      {
        throw new SecurityServiceException(String.format(
            "No rows were affected as a result of executing the SQL statement (%s)",
            deleteOrganizationSQL));
      }
    }
    catch (OrganizationNotFoundException e)
    {
      throw e;
    }
    catch (Throwable e)
    {
      throw new SecurityServiceException(String.format("Failed to delete the organization (%s)",
          organizationId), e);
    }
  }

  /**
   * Delete the user.
   *
   * @param userDirectoryId the Universally Unique Identifier (UUID) used to uniquely identify the
   *                        user directory
   * @param username        the username identifying the user
   */
  @Override
  public void deleteUser(UUID userDirectoryId, String username)
    throws UserDirectoryNotFoundException, UserNotFoundException, SecurityServiceException
  {
    IUserDirectory userDirectory = userDirectories.get(userDirectoryId);

    if (userDirectory == null)
    {
      throw new UserDirectoryNotFoundException(userDirectoryId);
    }

    userDirectory.deleteUser(username);
  }

  /**
   * Delete the user directory.
   *
   * @param userDirectoryId the Universally Unique Identifier (UUID) used to uniquely identify the
   *                        user directory
   */
  @Override
  public void deleteUserDirectory(UUID userDirectoryId)
    throws UserDirectoryNotFoundException, SecurityServiceException
  {
    String deleteUserDirectorySQL = "DELETE FROM security.user_directories WHERE id=?";

    try (Connection connection = dataSource.getConnection();
      PreparedStatement statement = connection.prepareStatement(deleteUserDirectorySQL))
    {
      statement.setObject(1, userDirectoryId);

      if (statement.executeUpdate() <= 0)
      {
        throw new UserDirectoryNotFoundException(userDirectoryId);
      }

      try
      {
        reloadUserDirectories();
      }
      catch (Throwable e)
      {
        logger.error("Failed to reload the user directories", e);
      }
    }
    catch (UserDirectoryNotFoundException e)
    {
      throw e;
    }
    catch (Throwable e)
    {
      throw new SecurityServiceException(String.format("Failed to delete the user directory (%s)",
          userDirectoryId), e);
    }
  }

  /**
   * Retrieve the users matching the attribute criteria.
   *
   * @param userDirectoryId the Universally Unique Identifier (UUID) used to uniquely identify the
   *                        user directory
   * @param attributes      the attribute criteria used to select the users
   *
   * @return the users whose attributes match the attribute criteria
   */
  @Override
  public List<User> findUsers(UUID userDirectoryId, List<Attribute> attributes)
    throws UserDirectoryNotFoundException, InvalidAttributeException, SecurityServiceException
  {
    IUserDirectory userDirectory = userDirectories.get(userDirectoryId);

    if (userDirectory == null)
    {
      throw new UserDirectoryNotFoundException(userDirectoryId);
    }

    return userDirectory.findUsers(attributes);
  }

  /**
   * Retrieve the authorised function.
   *
   * @param code the code identifying the function
   *
   * @return the authorised function
   */
  @Override
  public Function getFunction(String code)
    throws FunctionNotFoundException, SecurityServiceException
  {
    String getFunctionSQL =
        "SELECT id, code, name, description FROM security.functions WHERE code=?";

    try
    {
      try (Connection connection = dataSource.getConnection();
        PreparedStatement statement = connection.prepareStatement(getFunctionSQL))
      {
        statement.setString(1, code);

        try (ResultSet rs = statement.executeQuery())
        {
          if (rs.next())
          {
            return buildFunctionFromResultSet(rs);
          }
          else
          {
            throw new FunctionNotFoundException(code);
          }
        }
      }
    }
    catch (FunctionNotFoundException e)
    {
      throw e;
    }
    catch (Throwable e)
    {
      throw new SecurityServiceException(String.format("Failed to retrieve the function (%s)",
          code), e);
    }
  }

  /**
   * Retrieve the authorised function codes for the user.
   *
   * @param userDirectoryId the Universally Unique Identifier (UUID) used to uniquely identify the
   *                        user directory
   * @param username        the username identifying the user
   *
   * @return the authorised function codes for the user
   */
  @Override
  public List<String> getFunctionCodesForUser(UUID userDirectoryId, String username)
    throws UserDirectoryNotFoundException, UserNotFoundException, SecurityServiceException
  {
    IUserDirectory userDirectory = userDirectories.get(userDirectoryId);

    if (userDirectory == null)
    {
      throw new UserDirectoryNotFoundException(userDirectoryId);
    }

    return userDirectory.getFunctionCodesForUser(username);
  }

  /**
   * Retrieve all the authorised functions.
   *
   * @return the authorised functions
   */
  @Override
  public List<Function> getFunctions()
    throws SecurityServiceException
  {
    String getFunctionsSQL = "SELECT id, code, name, description FROM security.functions";

    try (Connection connection = dataSource.getConnection();
      PreparedStatement statement = connection.prepareStatement(getFunctionsSQL))
    {
      try (ResultSet rs = statement.executeQuery())
      {
        List<Function> list = new ArrayList<>();

        while (rs.next())
        {
          list.add(buildFunctionFromResultSet(rs));
        }

        return list;
      }
    }
    catch (Throwable e)
    {
      throw new SecurityServiceException("Failed to retrieve the functions", e);
    }
  }

  /**
   * Retrieve the security group.
   *
   * @param userDirectoryId the Universally Unique Identifier (UUID) used to uniquely identify the
   *                        user directory
   * @param groupName       the name of the security group uniquely identifying the security group
   *
   * @return the security group
   */
  @Override
  public Group getGroup(UUID userDirectoryId, String groupName)
    throws UserDirectoryNotFoundException, GroupNotFoundException, SecurityServiceException
  {
    IUserDirectory userDirectory = userDirectories.get(userDirectoryId);

    if (userDirectory == null)
    {
      throw new UserDirectoryNotFoundException(userDirectoryId);
    }

    return userDirectory.getGroup(groupName);
  }

  /**
   * Retrieve the security group names for the user.
   *
   * @param userDirectoryId the Universally Unique Identifier (UUID) used to uniquely identify the
   *                        user directory
   * @param username        the username identifying the user
   *
   * @return the security group names for the user
   */
  @Override
  public List<String> getGroupNamesForUser(UUID userDirectoryId, String username)
    throws UserDirectoryNotFoundException, UserNotFoundException, SecurityServiceException
  {
    IUserDirectory userDirectory = userDirectories.get(userDirectoryId);

    if (userDirectory == null)
    {
      throw new UserDirectoryNotFoundException(userDirectoryId);
    }

    return userDirectory.getGroupNamesForUser(username);
  }

  /**
   * Retrieve all the security groups.
   *
   * @param userDirectoryId the Universally Unique Identifier (UUID) used to uniquely identify the
   *                        user directory
   *
   * @return the security groups
   */
  @Override
  public List<Group> getGroups(UUID userDirectoryId)
    throws UserDirectoryNotFoundException, SecurityServiceException
  {
    IUserDirectory userDirectory = userDirectories.get(userDirectoryId);

    if (userDirectory == null)
    {
      throw new UserDirectoryNotFoundException(userDirectoryId);
    }

    return userDirectory.getGroups();
  }

  /**
   * Retrieve the security groups for the user.
   *
   * @param userDirectoryId the Universally Unique Identifier (UUID) used to uniquely identify the
   *                        user directory
   * @param username        the username identifying the user
   *
   * @return the security groups for the user
   */
  @Override
  public List<Group> getGroupsForUser(UUID userDirectoryId, String username)
    throws UserDirectoryNotFoundException, UserNotFoundException, SecurityServiceException
  {
    IUserDirectory userDirectory = userDirectories.get(userDirectoryId);

    if (userDirectory == null)
    {
      throw new UserDirectoryNotFoundException(userDirectoryId);
    }

    return userDirectory.getGroupsForUser(username);
  }

  /**
   * Retrieve the number of security groups
   *
   * @param userDirectoryId the Universally Unique Identifier (UUID) used to uniquely identify the
   *                        user directory
   *
   * @return the number of security groups
   */
  @Override
  public int getNumberOfGroups(UUID userDirectoryId)
    throws UserDirectoryNotFoundException, SecurityServiceException
  {
    IUserDirectory userDirectory = userDirectories.get(userDirectoryId);

    if (userDirectory == null)
    {
      throw new UserDirectoryNotFoundException(userDirectoryId);
    }

    return userDirectory.getNumberOfGroups();
  }

  /**
   * Retrieve the number of organizations
   *
   * @return the number of organizations
   */
  @Override
  public int getNumberOfOrganizations()
    throws SecurityServiceException
  {
    return getNumberOfOrganizations(null);
  }

  /**
   * Retrieve the number of organizations
   *
   * @param filter the optional filter to apply to the organizations
   *
   * @return the number of organizations
   */
  @Override
  public int getNumberOfOrganizations(String filter)
    throws SecurityServiceException
  {
    String getNumberOfOrganizationsSQL = "SELECT COUNT(id) FROM security.organizations";

    if (!StringUtils.isEmpty(filter))
    {
      getNumberOfOrganizationsSQL += " WHERE (UPPER(name) LIKE ?)";
    }

    try (Connection connection = dataSource.getConnection();
      PreparedStatement statement = connection.prepareStatement(getNumberOfOrganizationsSQL))
    {
      if (!StringUtils.isEmpty(filter))
      {
        statement.setString(1, String.format("%%%s%%", filter.toUpperCase()));
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
      throw new SecurityServiceException("Failed to retrieve the number of organizations", e);
    }
  }

  /**
   * Retrieve the number of user directories
   *
   * @return the number of user directories
   */
  @Override
  public int getNumberOfUserDirectories()
    throws SecurityServiceException
  {
    return getNumberOfUserDirectories(null);
  }

  /**
   * Retrieve the number of user directories
   *
   * @param filter the optional filter to apply to the user directories
   *
   * @return the number of user directories
   */
  @Override
  public int getNumberOfUserDirectories(String filter)
    throws SecurityServiceException
  {
    String getNumberOfUserDirectoriesSQL = "SELECT COUNT(id) FROM security.user_directories";

    if (!StringUtils.isEmpty(filter))
    {
      getNumberOfUserDirectoriesSQL += " WHERE (UPPER(name) LIKE ?)";
    }

    try (Connection connection = dataSource.getConnection();
      PreparedStatement statement = connection.prepareStatement(getNumberOfUserDirectoriesSQL))
    {
      if (!StringUtils.isEmpty(filter))
      {
        statement.setString(1, String.format("%%%s%%", filter.toUpperCase()));
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
      throw new SecurityServiceException("Failed to retrieve the number of user directories", e);
    }
  }

  /**
   * Retrieve the number of users.
   *
   * @param userDirectoryId the Universally Unique Identifier (UUID) used to uniquely identify the
   *                        user directory
   *
   * @return the number of users
   */
  @Override
  public int getNumberOfUsers(UUID userDirectoryId)
    throws UserDirectoryNotFoundException, SecurityServiceException
  {
    return getNumberOfUsers(userDirectoryId, null);
  }

  /**
   * Retrieve the number of users.
   *
   * @param userDirectoryId the Universally Unique Identifier (UUID) used to uniquely identify the
   *                        user directory
   * @param filter          the optional filter to apply to the users
   *
   * @return the number of users
   */
  @Override
  public int getNumberOfUsers(UUID userDirectoryId, String filter)
    throws UserDirectoryNotFoundException, SecurityServiceException
  {
    IUserDirectory userDirectory = userDirectories.get(userDirectoryId);

    if (userDirectory == null)
    {
      throw new UserDirectoryNotFoundException(userDirectoryId);
    }

    return userDirectory.getNumberOfUsers(filter);
  }

  /**
   * Retrieve the organization.
   *
   * @param organizationId the Universally Unique Identifier (UUID) used to uniquely identify the
   *                       organization
   *
   * @return the organization
   */
  @Override
  public Organization getOrganization(UUID organizationId)
    throws OrganizationNotFoundException, SecurityServiceException
  {
    String getOrganizationSQL = "SELECT id, name, status FROM security.organizations WHERE id=?";

    try (Connection connection = dataSource.getConnection();
      PreparedStatement statement = connection.prepareStatement(getOrganizationSQL))
    {
      statement.setObject(1, organizationId);

      try (ResultSet rs = statement.executeQuery())
      {
        if (rs.next())
        {
          return buildOrganizationFromResultSet(rs);
        }
        else
        {
          throw new OrganizationNotFoundException(organizationId);
        }
      }
    }
    catch (OrganizationNotFoundException e)
    {
      throw e;
    }
    catch (Throwable e)
    {
      throw new SecurityServiceException(String.format("Failed to retrieve the organization (%s)",
          organizationId), e);
    }
  }

  /**
   * Retrieve the Universally Unique Identifiers (UUIDs) used to uniquely identify the organizations
   * the user directory is associated with.
   *
   * @param userDirectoryId the Universally Unique Identifier (UUID) used to uniquely identify the
   *                        user directory
   *
   * @return the Universally Unique Identifiers (UUIDs) used to uniquely identify the organizations
   *         the user directory is associated with
   */
  @Override
  public List<UUID> getOrganizationIdsForUserDirectory(UUID userDirectoryId)
    throws UserDirectoryNotFoundException, SecurityServiceException
  {
    String getOrganizationIdsForUserDirectorySQL =
        "SELECT organization_id FROM security.user_directory_to_organization_map "
        + "WHERE user_directory_id=?";

    try (Connection connection = dataSource.getConnection();
      PreparedStatement statement = connection.prepareStatement(
          getOrganizationIdsForUserDirectorySQL))
    {
      if (!userDirectoryExists(connection, userDirectoryId))
      {
        throw new UserDirectoryNotFoundException(userDirectoryId);
      }

      statement.setObject(1, userDirectoryId);

      try (ResultSet rs = statement.executeQuery())
      {
        List<UUID> list = new ArrayList<>();

        while (rs.next())
        {
          list.add(UUID.fromString(rs.getString(1)));
        }

        return list;
      }
    }
    catch (UserDirectoryNotFoundException e)
    {
      throw e;
    }
    catch (Throwable e)
    {
      throw new SecurityServiceException(String.format(
          "Failed to retrieve the IDs for the organizations for the user directory (%s)",
          userDirectoryId), e);
    }
  }

  /**
   * Retrieve the organizations.
   *
   * @return the organizations
   */
  @Override
  public List<Organization> getOrganizations()
    throws SecurityServiceException
  {
    String getOrganizationsSQL =
        "SELECT id, name, status FROM security.organizations ORDER BY name";

    try (Connection connection = dataSource.getConnection();
      PreparedStatement statement = connection.prepareStatement(getOrganizationsSQL))
    {
      try (ResultSet rs = statement.executeQuery())
      {
        List<Organization> list = new ArrayList<>();

        while (rs.next())
        {
          list.add(buildOrganizationFromResultSet(rs));
        }

        return list;
      }
    }
    catch (Throwable e)
    {
      throw new SecurityServiceException("Failed to retrieve the organizations", e);
    }
  }

  /**
   * Retrieve the organizations.
   *
   * @param filter        the optional filter to apply to the organizations
   * @param sortDirection the optional sort direction to apply to the organizations
   * @param pageIndex     the optional page index
   * @param pageSize      the optional page size
   *
   * @return the organizations
   */
  @Override
  public List<Organization> getOrganizations(String filter, SortDirection sortDirection,
      Integer pageIndex, Integer pageSize)
    throws SecurityServiceException
  {
    String getOrganizationsSQL = "SELECT id, name, status FROM security.organizations";

    if (!StringUtils.isEmpty(filter))
    {
      getOrganizationsSQL += " WHERE (UPPER(name) LIKE ?)";
    }

    getOrganizationsSQL += " ORDER BY name " + ((sortDirection == SortDirection.DESCENDING)
        ? "DESC"
        : "ASC");

    if ((pageIndex != null) && (pageSize != null))
    {
      getOrganizationsSQL += " LIMIT " + pageSize + " OFFSET " + (pageIndex * pageSize);

    }
    else
    {
      getOrganizationsSQL += " LIMIT " + MAX_FILTERED_ORGANISATIONS;
    }

    try (Connection connection = dataSource.getConnection();
      PreparedStatement statement = connection.prepareStatement(getOrganizationsSQL))
    {
      if (!StringUtils.isEmpty(filter))
      {
        statement.setString(1, String.format("%%%s%%", filter.toUpperCase()));
      }

      try (ResultSet rs = statement.executeQuery())
      {
        List<Organization> list = new ArrayList<>();

        while (rs.next())
        {
          list.add(buildOrganizationFromResultSet(rs));
        }

        return list;
      }
    }
    catch (Throwable e)
    {
      String message = "Failed to retrieve the organizations";

      if (!StringUtils.isEmpty(filter))
      {
        message += String.format(" matching the filter \"%s\"", filter);
      }

      if ((pageIndex != null) && (pageSize != null))
      {
        message += " for the page " + pageIndex + " using the page size " + pageSize;
      }

      message += ": ";

      message += e.getMessage();

      throw new SecurityServiceException(message, e);
    }
  }

  /**
   * Retrieve the organizations the user directory is associated with.
   *
   * @param userDirectoryId the Universally Unique Identifier (UUID) used to uniquely identify the
   *                        user directory
   *
   * @return the organizations the user directory is associated with
   */
  @Override
  public List<Organization> getOrganizationsForUserDirectory(UUID userDirectoryId)
    throws UserDirectoryNotFoundException, SecurityServiceException
  {
    String getOrganizationsForUserDirectorySQL =
        "SELECT o.id, o.name, o.status FROM security.organizations o INNER JOIN "
        + "security.user_directory_to_organization_map udtom ON o.id = udtom.organization_id WHERE "
        + "udtom.user_directory_id=?";

    try (Connection connection = dataSource.getConnection();
      PreparedStatement statement = connection.prepareStatement(
          getOrganizationsForUserDirectorySQL))
    {
      if (!userDirectoryExists(connection, userDirectoryId))
      {
        throw new UserDirectoryNotFoundException(userDirectoryId);
      }

      statement.setObject(1, userDirectoryId);

      try (ResultSet rs = statement.executeQuery())
      {
        List<Organization> list = new ArrayList<>();

        while (rs.next())
        {
          list.add(buildOrganizationFromResultSet(rs));
        }

        return list;
      }
    }
    catch (UserDirectoryNotFoundException e)
    {
      throw e;
    }
    catch (Throwable e)
    {
      throw new SecurityServiceException(String.format(
          "Failed to retrieve the organizations associated with the user directory (%s)",
          userDirectoryId), e);
    }
  }

  /**
   * Retrieve the user.
   *
   * @param userDirectoryId the Universally Unique Identifier (UUID) used to uniquely identify the
   *                        user directory
   * @param username        the username identifying the user
   *
   * @return the user
   */
  @Override
  public User getUser(UUID userDirectoryId, String username)
    throws UserDirectoryNotFoundException, UserNotFoundException, SecurityServiceException
  {
    IUserDirectory userDirectory = userDirectories.get(userDirectoryId);

    if (userDirectory == null)
    {
      throw new UserDirectoryNotFoundException(userDirectoryId);
    }

    return userDirectory.getUser(username);
  }

  /**
   * Retrieve the user directories.
   *
   * @return the user directories
   */
  @Override
  public List<UserDirectory> getUserDirectories()
    throws SecurityServiceException
  {
    String getUserDirectoriesSQL =
        "SELECT id, type_id, name, configuration FROM security.user_directories";

    try (Connection connection = dataSource.getConnection();
      PreparedStatement statement = connection.prepareStatement(getUserDirectoriesSQL))
    {
      try (ResultSet rs = statement.executeQuery())
      {
        List<UserDirectory> list = new ArrayList<>();

        while (rs.next())
        {
          list.add(buildUserDirectoryFromResultSet(rs));
        }

        return list;
      }
    }
    catch (Throwable e)
    {
      throw new SecurityServiceException("Failed to retrieve the user directories", e);
    }
  }

  /**
   * Retrieve the user directories.
   *
   * @param filter        the optional filter to apply to the user directories
   * @param sortDirection the optional sort direction to apply to the user directories
   * @param pageIndex     the optional page index
   * @param pageSize      the optional page size
   *
   * @return the user directories
   */
  @Override
  public List<UserDirectory> getUserDirectories(String filter, SortDirection sortDirection,
      Integer pageIndex, Integer pageSize)
    throws SecurityServiceException
  {
    String getUserDirectoriesSQL =
        "SELECT id, type_id, name, configuration FROM security.user_directories";

    if (!StringUtils.isEmpty(filter))
    {
      getUserDirectoriesSQL += " WHERE (UPPER(name) LIKE ?)";
    }

    getUserDirectoriesSQL += " ORDER BY name " + ((sortDirection == SortDirection.DESCENDING)
        ? "DESC"
        : "ASC");

    if ((pageIndex != null) && (pageSize != null))
    {
      getUserDirectoriesSQL += " LIMIT " + pageSize + " OFFSET " + (pageIndex * pageSize);

    }
    else
    {
      getUserDirectoriesSQL += " LIMIT " + MAX_FILTERED_ORGANISATIONS;
    }

    try (Connection connection = dataSource.getConnection();
      PreparedStatement statement = connection.prepareStatement(getUserDirectoriesSQL))
    {
      if (!StringUtils.isEmpty(filter))
      {
        String filterBuffer = String.format("%%%s%%", filter.toUpperCase());

        statement.setString(1, filterBuffer);
      }

      try (ResultSet rs = statement.executeQuery())
      {
        List<UserDirectory> list = new ArrayList<>();

        while (rs.next())
        {
          list.add(buildUserDirectoryFromResultSet(rs));
        }

        return list;
      }
    }
    catch (Throwable e)
    {
      throw new SecurityServiceException("Failed to retrieve the filtered user directories", e);
    }
  }

  /**
   * Retrieve the user directories the organization is associated with.
   *
   * @param organizationId                      the Universally Unique Identifier (UUID) used to
   *                                            uniquely identify the organization
   * @param includeDefaultInternalUserDirectory include the default internal user directory
   *
   * @return the user directories the organization is associated with
   */
  @Override
  public List<UserDirectory> getUserDirectoriesForOrganization(UUID organizationId,
      boolean includeDefaultInternalUserDirectory)
    throws OrganizationNotFoundException, SecurityServiceException
  {
    String getUserDirectoriesForOrganizationSQL =
        "SELECT ud.id, ud.type_id, ud.name, ud.configuration FROM security.user_directories ud "
        + "INNER JOIN security.user_directory_to_organization_map udtom "
        + "ON ud.id = udtom.user_directory_id INNER JOIN security.organizations o "
        + "ON udtom.organization_id = o.id WHERE o.id=?";

    try (Connection connection = dataSource.getConnection();
      PreparedStatement statement = connection.prepareStatement(
          getUserDirectoriesForOrganizationSQL))
    {
      if (!organizationExists(connection, organizationId))
      {
        throw new OrganizationNotFoundException(organizationId);
      }

      statement.setObject(1, organizationId);

      try (ResultSet rs = statement.executeQuery())
      {
        List<UserDirectory> list = new ArrayList<>();

        while (rs.next())
        {
          UserDirectory userDirectory = buildUserDirectoryFromResultSet(rs);

          if (userDirectory.getId().equals(SecurityService.DEFAULT_INTERNAL_USER_DIRECTORY_ID))
          {
            if (includeDefaultInternalUserDirectory)
            {
              list.add(userDirectory);
            }
          }
          else
          {
            list.add(userDirectory);
          }
        }

        return list;
      }
    }
    catch (OrganizationNotFoundException e)
    {
      throw e;
    }
    catch (Throwable e)
    {
      throw new SecurityServiceException(String.format(
          "Failed to retrieve the user directories associated with the organization (%s)",
          organizationId), e);
    }
  }

  /**
   * Retrieve the user directory.
   *
   * @param userDirectoryId the Universally Unique Identifier (UUID) used to uniquely identify the
   *                        user directory
   *
   * @return the user directory
   */
  @Override
  public UserDirectory getUserDirectory(UUID userDirectoryId)
    throws UserDirectoryNotFoundException, SecurityServiceException
  {
    String getUserDirectorySQL = "SELECT id, type_id, name, configuration "
        + "FROM security.user_directories WHERE id=?";

    try (Connection connection = dataSource.getConnection();
      PreparedStatement statement = connection.prepareStatement(getUserDirectorySQL))
    {
      statement.setObject(1, userDirectoryId);

      try (ResultSet rs = statement.executeQuery())
      {
        if (rs.next())
        {
          return buildUserDirectoryFromResultSet(rs);
        }
        else
        {
          throw new UserDirectoryNotFoundException(userDirectoryId);
        }
      }
    }
    catch (UserDirectoryNotFoundException e)
    {
      throw e;
    }
    catch (Throwable e)
    {
      throw new SecurityServiceException(String.format(
          "Failed to retrieve the user directory (%s)", userDirectoryId), e);
    }
  }

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
  @Override
  public UUID getUserDirectoryIdForUser(String username)
    throws SecurityServiceException
  {
    try
    {
      // First check if this is an internal user and if so determine the user directory ID
      UUID internalUserDirectoryId = getInternalUserDirectoryIdForUser(username);

      if (internalUserDirectoryId != null)
      {
        return internalUserDirectoryId;
      }
      else
      {
        /*
         * Check all of the "external" user directories to see if the user is associated with one
         * of them.
         */
        for (UUID userDirectoryId : userDirectories.keySet())
        {
          IUserDirectory userDirectory = userDirectories.get(userDirectoryId);

          if (userDirectory != null)
          {
            if (!(userDirectory instanceof InternalUserDirectory))
            {
              if (userDirectory.isExistingUser(username))
              {
                return userDirectoryId;
              }
            }
          }
        }

        return null;
      }
    }
    catch (Throwable e)
    {
      throw new SecurityServiceException(String.format(
          "Failed to retrieve the user directory ID for the user (%s)", username), e);
    }
  }

  /**
   * Retrieve the summaries for the user directories.
   *
   * @param filter        the optional filter to apply to the user directories
   * @param sortDirection the optional sort direction to apply to the user directories
   * @param pageIndex     the optional page index
   * @param pageSize      the optional page size
   *
   * @return the summaries for the user directories
   */
  @Override
  public List<UserDirectorySummary> getUserDirectorySummaries(String filter,
      SortDirection sortDirection, Integer pageIndex, Integer pageSize)
    throws SecurityServiceException
  {
    String getUserDirectorySummariesSQL = "SELECT id, type_id, name FROM security.user_directories";

    if (!StringUtils.isEmpty(filter))
    {
      getUserDirectorySummariesSQL += " WHERE (UPPER(name) LIKE ?)";
    }

    getUserDirectorySummariesSQL += " ORDER BY name " + ((sortDirection == SortDirection.DESCENDING)
        ? "DESC"
        : "ASC");

    if ((pageIndex != null) && (pageSize != null))
    {
      getUserDirectorySummariesSQL += " LIMIT " + pageSize + " OFFSET " + (pageIndex * pageSize);

    }
    else
    {
      getUserDirectorySummariesSQL += " LIMIT " + MAX_FILTERED_ORGANISATIONS;
    }

    try (Connection connection = dataSource.getConnection();
      PreparedStatement statement = connection.prepareStatement(getUserDirectorySummariesSQL))
    {
      if (!StringUtils.isEmpty(filter))
      {
        String filterBuffer = String.format("%%%s%%", filter.toUpperCase());

        statement.setString(1, filterBuffer);
      }

      try (ResultSet rs = statement.executeQuery())
      {
        List<UserDirectorySummary> list = new ArrayList<>();

        while (rs.next())
        {
          list.add(buildUserDirectorySummaryFromResultSet(rs));
        }

        return list;
      }
    }
    catch (Throwable e)
    {
      throw new SecurityServiceException(
          "Failed to retrieve the filtered summaries for the user directories", e);
    }
  }

  /**
   * Retrieve the summaries for the user directories the organization is associated with.
   *
   * @param organizationId                      the Universally Unique Identifier (UUID) used to
   *                                            uniquely identify the organization
   * @param includeDefaultInternalUserDirectory include the default internal user directory
   *
   * @return the summaries for the user directories the organization is associated with
   */
  @Override
  public List<UserDirectorySummary> getUserDirectorySummariesForOrganization(UUID organizationId,
      boolean includeDefaultInternalUserDirectory)
    throws OrganizationNotFoundException, SecurityServiceException
  {
    String getUserDirectorySummariesForOrganizationSQL =
        "SELECT ud.id, ud.type_id, ud.name FROM security.user_directories ud "
        + "INNER JOIN security.user_directory_to_organization_map udtom "
        + "ON ud.id = udtom.user_directory_id INNER JOIN security.organizations o "
        + "ON udtom.organization_id = o.id WHERE o.id=?";

    try (Connection connection = dataSource.getConnection();
      PreparedStatement statement = connection.prepareStatement(
          getUserDirectorySummariesForOrganizationSQL))
    {
      if (!organizationExists(connection, organizationId))
      {
        throw new OrganizationNotFoundException(organizationId);
      }

      statement.setObject(1, organizationId);

      try (ResultSet rs = statement.executeQuery())
      {
        List<UserDirectorySummary> list = new ArrayList<>();

        while (rs.next())
        {
          UserDirectorySummary userDirectorySummary = buildUserDirectorySummaryFromResultSet(rs);

          if (userDirectorySummary.getId().equals(SecurityService
              .DEFAULT_INTERNAL_USER_DIRECTORY_ID))
          {
            if (includeDefaultInternalUserDirectory)
            {
              list.add(userDirectorySummary);
            }
          }
          else
          {
            list.add(userDirectorySummary);
          }
        }

        return list;
      }
    }
    catch (OrganizationNotFoundException e)
    {
      throw e;
    }
    catch (Throwable e)
    {
      throw new SecurityServiceException(String.format("Failed to retrieve the summaries for the"
          + " user directories associated with the organization (%s)", organizationId), e);
    }
  }

  /**
   * Retrieve the user directory types.
   *
   * @return the user directory types
   */
  @Override
  public List<UserDirectoryType> getUserDirectoryTypes()
    throws SecurityServiceException
  {
    String getUserDirectoryTypesSQL = "SELECT id, name, user_directory_class "
        + "FROM security.user_directory_types";

    try (Connection connection = dataSource.getConnection();
      PreparedStatement statement = connection.prepareStatement(getUserDirectoryTypesSQL))
    {
      try (ResultSet rs = statement.executeQuery())
      {
        List<UserDirectoryType> list = new ArrayList<>();

        while (rs.next())
        {
          list.add(new UserDirectoryType(UUID.fromString(rs.getString(1)), rs.getString(2),
              rs.getString(3)));
        }

        return list;
      }
    }
    catch (Throwable e)
    {
      throw new SecurityServiceException("Failed to retrieve the user directory types", e);
    }
  }

  /**
   * Retrieve all the users.
   *
   * @param userDirectoryId the Universally Unique Identifier (UUID) used to uniquely identify the
   *                        user directory
   *
   * @return the users
   */
  @Override
  public List<User> getUsers(UUID userDirectoryId)
    throws UserDirectoryNotFoundException, SecurityServiceException
  {
    IUserDirectory userDirectory = userDirectories.get(userDirectoryId);

    if (userDirectory == null)
    {
      throw new UserDirectoryNotFoundException(userDirectoryId);
    }

    return userDirectory.getUsers();
  }

  /**
   * Retrieve the users.
   *
   * @param userDirectoryId the Universally Unique Identifier (UUID) used to uniquely identify the
   *                        user directory
   * @param filter          the optional filter to apply to the users
   * @param sortBy          the optional method used to sort the users e.g. by last name
   * @param sortDirection   the optional sort direction to apply to the users
   * @param pageIndex       the optional page index
   * @param pageSize        the optional page size
   *
   * @return the users
   */
  @Override
  public List<User> getUsers(UUID userDirectoryId, String filter, UserSortBy sortBy,
      SortDirection sortDirection, Integer pageIndex, Integer pageSize)
    throws UserDirectoryNotFoundException, SecurityServiceException
  {
    IUserDirectory userDirectory = userDirectories.get(userDirectoryId);

    if (userDirectory == null)
    {
      throw new UserDirectoryNotFoundException(userDirectoryId);
    }

    return userDirectory.getUsers(filter, sortBy, sortDirection, pageIndex, pageSize);
  }

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
  @Override
  public boolean isUserInGroup(UUID userDirectoryId, String username, String groupName)
    throws UserDirectoryNotFoundException, UserNotFoundException, GroupNotFoundException,
        SecurityServiceException
  {
    IUserDirectory userDirectory = userDirectories.get(userDirectoryId);

    if (userDirectory == null)
    {
      throw new UserDirectoryNotFoundException(userDirectoryId);
    }

    return userDirectory.isUserInGroup(username, groupName);
  }

  /**
   * Reload the user directories.
   */
  @Override
  public void reloadUserDirectories()
    throws SecurityServiceException
  {
    try
    {
      Map<UUID, IUserDirectory> reloadedUserDirectories = new ConcurrentHashMap<>();

      List<UserDirectoryType> userDirectoryTypes = getUserDirectoryTypes();

      for (UserDirectory userDirectory : getUserDirectories())
      {
        UserDirectoryType userDirectoryType;

        userDirectoryType = userDirectoryTypes.stream().filter(
            possibleUserDirectoryType -> possibleUserDirectoryType.getId().equals(
            userDirectory.getTypeId())).findFirst().orElse(null);

        if (userDirectoryType == null)
        {
          logger.error(String.format(
              "Failed to load the user directory (%s): The user directory type (%s) was not loaded",
              userDirectory.getId(), userDirectory.getTypeId()));

          continue;
        }

        try
        {
          Class<?> clazz = userDirectoryType.getUserDirectoryClass();

          Class<? extends IUserDirectory> userDirectoryClass = clazz.asSubclass(
              IUserDirectory.class);

          if (userDirectoryClass == null)
          {
            throw new SecurityServiceException(String.format(
                "The user directory class (%s) does not implement the IUserDirectory interface",
                userDirectoryType.getUserDirectoryClassName()));
          }

          Constructor<? extends IUserDirectory> userDirectoryClassConstructor =
              userDirectoryClass.getConstructor(UUID.class, List.class);

          if (userDirectoryClassConstructor == null)
          {
            throw new SecurityServiceException(String.format(
                "The user directory class (%s) does not provide a valid constructor (long, "
                + "Map<String,String>)", userDirectoryType.getUserDirectoryClassName()));
          }

          IUserDirectory userDirectoryInstance = userDirectoryClassConstructor.newInstance(
              userDirectory.getId(), userDirectory.getParameters());

          applicationContext.getAutowireCapableBeanFactory().autowireBean(userDirectoryInstance);

          reloadedUserDirectories.put(userDirectory.getId(), userDirectoryInstance);
        }
        catch (Throwable e)
        {
          throw new SecurityServiceException(String.format(
              "Failed to initialize the user directory (%s)(%s)", userDirectory.getId(),
              userDirectory.getName()), e);
        }
      }

      this.userDirectories = reloadedUserDirectories;
    }
    catch (Throwable e)
    {
      throw new SecurityServiceException("Failed to reload the user directories", e);
    }
  }

  /**
   * Remove the user from the security group.
   *
   * @param userDirectoryId the Universally Unique Identifier (UUID) used to uniquely identify the
   *                        user directory
   * @param username        the username identifying the user
   * @param groupName       the security group name
   */
  @Override
  public void removeUserFromGroup(UUID userDirectoryId, String username, String groupName)
    throws UserDirectoryNotFoundException, UserNotFoundException, GroupNotFoundException,
        SecurityServiceException
  {
    IUserDirectory userDirectory = userDirectories.get(userDirectoryId);

    if (userDirectory == null)
    {
      throw new UserDirectoryNotFoundException(userDirectoryId);
    }

    userDirectory.removeUserFromGroup(username, groupName);
  }

  /**
   * Does the user directory support administering security groups.
   *
   * @param userDirectoryId the Universally Unique Identifier (UUID) used to uniquely identify the
   *                        user directory
   *
   * @return <code>true</code> if the user directory supports administering security groups or
   *         <code>false</code> otherwise
   */
  @Override
  public boolean supportsGroupAdministration(UUID userDirectoryId)
    throws UserDirectoryNotFoundException
  {
    IUserDirectory userDirectory = userDirectories.get(userDirectoryId);

    if (userDirectory == null)
    {
      throw new UserDirectoryNotFoundException(userDirectoryId);
    }

    return userDirectory.supportsGroupAdministration();
  }

  /**
   * Does the user directory support administering users.
   *
   * @param userDirectoryId the Universally Unique Identifier (UUID) used to uniquely identify the
   *                        user directory
   *
   * @return <code>true</code> if the user directory supports administering users or
   *         <code>false</code> otherwise
   */
  @Override
  public boolean supportsUserAdministration(UUID userDirectoryId)
    throws UserDirectoryNotFoundException
  {
    IUserDirectory userDirectory = userDirectories.get(userDirectoryId);

    if (userDirectory == null)
    {
      throw new UserDirectoryNotFoundException(userDirectoryId);
    }

    return userDirectory.supportsUserAdministration();
  }

  /**
   * Update the authorised function.
   *
   * @param function the function
   */
  @Override
  public void updateFunction(Function function)
    throws FunctionNotFoundException, SecurityServiceException
  {
    String updateFunctionSQL = "UPDATE security.functions SET name=?, description=? WHERE code=?";

    try (Connection connection = dataSource.getConnection();
      PreparedStatement statement = connection.prepareStatement(updateFunctionSQL))
    {
      if (getFunctionId(connection, function.getCode()) == null)
      {
        throw new FunctionNotFoundException(function.getCode());
      }

      statement.setString(1, function.getName());
      statement.setString(2,
          StringUtils.isEmpty(function.getDescription())
          ? ""
          : function.getDescription());
      statement.setString(3, function.getCode());

      if (statement.executeUpdate() <= 0)
      {
        throw new SecurityServiceException(String.format(
            "No rows were affected as a result of executing the SQL statement (%s)",
            updateFunctionSQL));
      }
    }
    catch (FunctionNotFoundException e)
    {
      throw e;
    }
    catch (Throwable e)
    {
      throw new SecurityServiceException(String.format("Failed to update the function (%s)",
          function.getCode()), e);
    }
  }

  /**
   * Update the security group.
   *
   * @param userDirectoryId the Universally Unique Identifier (UUID) used to uniquely identify the
   *                        user directory
   * @param group           the security group
   */
  @Override
  public void updateGroup(UUID userDirectoryId, Group group)
    throws UserDirectoryNotFoundException, GroupNotFoundException, SecurityServiceException
  {
    IUserDirectory userDirectory = userDirectories.get(userDirectoryId);

    if (userDirectory == null)
    {
      throw new UserDirectoryNotFoundException(userDirectoryId);
    }

    userDirectory.updateGroup(group);
  }

  /**
   * Update the organization.
   *
   * @param organization the organization
   */
  @Override
  public void updateOrganization(Organization organization)
    throws OrganizationNotFoundException, SecurityServiceException
  {
    String updateOrganizationSQL = "UPDATE security.organizations SET name=?, status=? WHERE id=?";

    try (Connection connection = dataSource.getConnection();
      PreparedStatement statement = connection.prepareStatement(updateOrganizationSQL))
    {
      if (!organizationExists(connection, organization.getId()))
      {
        throw new OrganizationNotFoundException(organization.getId());
      }

      statement.setString(1, organization.getName());
      statement.setInt(2, organization.getStatus().code());
      statement.setObject(3, organization.getId());

      if (statement.executeUpdate() <= 0)
      {
        throw new SecurityServiceException(String.format(
            "No rows were affected as a result of executing the SQL statement (%s)",
            updateOrganizationSQL));
      }
    }
    catch (OrganizationNotFoundException e)
    {
      throw e;
    }
    catch (Throwable e)
    {
      throw new SecurityServiceException(String.format("Failed to update the organization (%s)",
          organization.getId()), e);
    }
  }

  /**
   * Update the user.
   *
   * @param userDirectoryId the Universally Unique Identifier (UUID) used to uniquely identify the
   *                        user directory
   * @param user            the user
   * @param expirePassword  expire the user's password as part of the update
   * @param lockUser        lock the user as part of the update
   */
  @Override
  public void updateUser(UUID userDirectoryId, User user, boolean expirePassword, boolean lockUser)
    throws UserDirectoryNotFoundException, UserNotFoundException, SecurityServiceException
  {
    IUserDirectory userDirectory = userDirectories.get(userDirectoryId);

    if (userDirectory == null)
    {
      throw new UserDirectoryNotFoundException(userDirectoryId);
    }

    userDirectory.updateUser(user, expirePassword, lockUser);
  }

  /**
   * Update the user directory.
   *
   * @param userDirectory the user directory
   */
  @Override
  public void updateUserDirectory(UserDirectory userDirectory)
    throws UserDirectoryNotFoundException, SecurityServiceException
  {
    String updateUserDirectorySQL = "UPDATE security.user_directories "
        + "SET name=?, configuration=? WHERE id=?";

    try (Connection connection = dataSource.getConnection();
      PreparedStatement statement = connection.prepareStatement(updateUserDirectorySQL))
    {
      statement.setString(1, userDirectory.getName());
      statement.setString(2, userDirectory.getConfiguration());
      statement.setObject(3, userDirectory.getId());

      if (statement.executeUpdate() != 1)
      {
        throw new UserDirectoryNotFoundException(userDirectory.getId());
      }
    }
    catch (UserDirectoryNotFoundException e)
    {
      throw e;
    }
    catch (Throwable e)
    {
      throw new SecurityServiceException(String.format("Failed to update the user directory (%s)",
          userDirectory.getName()), e);
    }
  }

  /**
   * Create a new <code>Function</code> instance and populate it with the contents of the
   * current row in the specified <code>ResultSet</code>.
   *
   * @param rs the <code>ResultSet</code> whose current row will be used to populate the
   *           <code>Function</code> instance
   *
   * @return the populated <code>Function</code> instance
   */
  private Function buildFunctionFromResultSet(ResultSet rs)
    throws SQLException
  {
    Function function = new Function();

    function.setId(UUID.fromString(rs.getString(1)));
    function.setCode(rs.getString(2));
    function.setName(rs.getString(3));

    String description = rs.getString(4);

    function.setDescription(StringUtils.isEmpty(description)
        ? ""
        : description);

    return function;
  }

  /**
   * Create a new <code>Organization</code> instance and populate it with the contents of the
   * current row in the specified <code>ResultSet</code>.
   *
   * @param rs the <code>ResultSet</code> whose current row will be used to populate the
   *           <code>Organization</code> instance
   *
   * @return the populated <code>Organization</code> instance
   */
  private Organization buildOrganizationFromResultSet(ResultSet rs)
    throws SQLException
  {
    Organization organization = new Organization();
    organization.setId(UUID.fromString(rs.getString(1)));
    organization.setName(rs.getString(2));
    organization.setStatus(OrganizationStatus.fromCode(rs.getInt(3)));

    return organization;
  }

  /**
   * Create a new <code>UserDirectory</code> instance and populate it with the contents of the
   * current row in the specified <code>ResultSet</code>.
   *
   * @param rs the <code>ResultSet</code> whose current row will be used to populate the
   *           <code>UserDirectory</code> instance
   *
   * @return the populated <code>UserDirectory</code> instance
   */
  private UserDirectory buildUserDirectoryFromResultSet(ResultSet rs)
    throws SQLException, SecurityServiceException
  {
    UserDirectory userDirectory = new UserDirectory();
    userDirectory.setId(UUID.fromString(rs.getString(1)));
    userDirectory.setTypeId(UUID.fromString(rs.getString(2)));
    userDirectory.setName(rs.getString(3));
    userDirectory.setConfiguration(rs.getString(4));

    return userDirectory;
  }

  /**
   * Create a new <code>UserDirectorySummary</code> instance and populate it with the contents of
   * the current row in the specified <code>ResultSet</code>.
   *
   * @param rs the <code>ResultSet</code> whose current row will be used to populate the
   *           <code>UserDirectorySummary</code> instance
   *
   * @return the populated <code>UserDirectorySummary</code> instance
   */
  private UserDirectorySummary buildUserDirectorySummaryFromResultSet(ResultSet rs)
    throws SQLException
  {
    UserDirectorySummary userDirectorySummary = new UserDirectorySummary();
    userDirectorySummary.setId(UUID.fromString(rs.getString(1)));
    userDirectorySummary.setTypeId(UUID.fromString(rs.getString(2)));
    userDirectorySummary.setName(rs.getString(3));

    return userDirectorySummary;
  }

  /**
   * Returns the Universally Unique Identifier (UUID) used to uniquely identify the function with
   * the specified code.
   *
   * @param connection the existing database connection to use
   * @param code       the code uniquely identifying the function
   *
   * @return the Universally Unique Identifier (UUID) used to uniquely identify the function or
   *         <code>null</code> if a function with the specified code cannot be found
   */
  private UUID getFunctionId(Connection connection, String code)
    throws SQLException
  {
    String getFunctionIdSQL = "SELECT id FROM security.functions WHERE code=?";

    try (PreparedStatement statement = connection.prepareStatement(getFunctionIdSQL))
    {
      statement.setString(1, code);

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
  }

  /**
   * Returns the Universally Unique Identifier (UUID) used to uniquely identify the internal user
   * directory the internal user with the specified username is associated with.
   *
   * @param username the username uniquely identifying the internal user
   *
   * @return the Universally Unique Identifier (UUID) used to uniquely identify the internal user
   *         directory the internal user with the specified username is associated with or
   *         <code>null</code> if an internal user with the specified username could not be found
   */
  private UUID getInternalUserDirectoryIdForUser(String username)
    throws SecurityServiceException
  {
    String getInternalUserDirectoryIdForUserSQL = "SELECT user_directory_id FROM "
        + "security.internal_users WHERE UPPER(username)=UPPER(CAST(? AS VARCHAR(100)))";

    try (Connection connection = dataSource.getConnection();
      PreparedStatement statement = connection.prepareStatement(
          getInternalUserDirectoryIdForUserSQL))
    {
      statement.setString(1, username);

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
          "Failed to retrieve the ID for the internal user directory for the internal user (%s)",
          username), e);
    }
  }

  /**
   * Checks whether the specified value is <code>null</code> or blank.
   *
   * @param value the value to check
   *
   * @return true if the value is <code>null</code> or blank
   */
  private boolean isNullOrEmpty(Object value)
  {
    if (value == null)
    {
      return true;
    }

    if (value instanceof String)
    {
      return ((String) value).length() == 0;
    }

    return false;
  }

  private UserDirectory newInternalUserDirectoryForOrganization(Organization organization)
    throws SecurityServiceException
  {
    UserDirectory userDirectory = new UserDirectory();

    userDirectory.setId(idGenerator.nextUUID());
    userDirectory.setTypeId(UUID.fromString("b43fda33-d3b0-4f80-a39a-110b8e530f4f"));
    userDirectory.setName(organization.getName() + " User Directory");

    String buffer = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><!DOCTYPE userDirectory "
        + "SYSTEM \"UserDirectoryConfiguration.dtd\"><userDirectory>"
        + "<parameter><name>MaxPasswordAttempts</name><value>5</value></parameter>"
        + "<parameter><name>PasswordExpiryMonths</name><value>12</value></parameter>"
        + "<parameter><name>PasswordHistoryMonths</name><value>24</value></parameter>"
        + "<parameter><name>MaxFilteredUsers</name><value>100</value></parameter>"
        + "</userDirectory>";

    userDirectory.setConfiguration(buffer);

    return userDirectory;
  }

  /**
   * Returns <code>true</code> if the organization exists or <code>false</code> otherwise.
   *
   * @param connection     the existing database connection to use
   * @param organizationId the Universally Unique Identifier (UUID) used to uniquely identify the
   *                       organization
   *
   * @return <code>true</code> if the organization exists or <code>false</code> otherwise
   */
  private boolean organizationExists(Connection connection, UUID organizationId)
    throws SecurityServiceException
  {
    String organizationExistsSQL = "SELECT COUNT(id) FROM security.organizations WHERE id=?";

    try (PreparedStatement statement = connection.prepareStatement(organizationExistsSQL))
    {
      statement.setObject(1, organizationId);

      try (ResultSet rs = statement.executeQuery())
      {
        return rs.next() && (rs.getInt(1) > 0);
      }
    }
    catch (Throwable e)
    {
      throw new SecurityServiceException(String.format(
          "Failed to check whether the organization (%s) exists", organizationId), e);
    }
  }

  /**
   * Returns <code>true</code> if an organization with the specified ID exists or
   * <code>false</code> otherwise.
   *
   * @param connection     the existing database connection to use
   * @param organizationId the Universally Unique Identifier (UUID) used to uniquely identify the
   *                       organization
   *
   * @return <code>true</code> if an organization with the specified ID exists or
   *         <code>false</code> otherwise
   */
  private boolean organizationWithIdExists(Connection connection, UUID organizationId)
    throws SecurityServiceException
  {
    String organizationWithIdExistsSQL =
        "SELECT COUNT(id) FROM security.organizations WHERE id = ?";

    try (PreparedStatement statement = connection.prepareStatement(organizationWithIdExistsSQL))
    {
      statement.setObject(1, organizationId);

      try (ResultSet rs = statement.executeQuery())
      {
        return rs.next() && (rs.getInt(1) > 0);
      }
    }
    catch (Throwable e)
    {
      throw new SecurityServiceException(String.format(
          "Failed to check whether an organization with the ID (%s) exists",
          organizationId.toString()), e);
    }
  }

  /**
   * Returns <code>true</code> if an organization with the specified name exists or
   * <code>false</code> otherwise.
   *
   * @param connection the existing database connection to use
   * @param name       the organization name
   *
   * @return <code>true</code> if an organization with the specified name exists or
   *         <code>false</code> otherwise
   */
  private boolean organizationWithNameExists(Connection connection, String name)
    throws SecurityServiceException
  {
    String organizationWithNameExistsSQL =
        "SELECT COUNT(id) FROM security.organizations WHERE (UPPER(name) LIKE ?)";

    try (PreparedStatement statement = connection.prepareStatement(organizationWithNameExistsSQL))
    {
      statement.setString(1, name.toUpperCase());

      try (ResultSet rs = statement.executeQuery())
      {
        return rs.next() && (rs.getInt(1) > 0);
      }
    }
    catch (Throwable e)
    {
      throw new SecurityServiceException(String.format(
          "Failed to check whether an organization with the name (%s) exists", name), e);
    }
  }

  /**
   * Reload the user directory types.
   */
  private void reloadUserDirectoryTypes()
    throws SecurityServiceException
  {
    try
    {
      Map<UUID, UserDirectoryType> reloadedUserDirectoryTypes = new ConcurrentHashMap<>();

      for (UserDirectoryType userDirectoryType : getUserDirectoryTypes())
      {
        try
        {
          userDirectoryType.getUserDirectoryClass();
        }
        catch (Throwable e)
        {
          logger.error(String.format("Failed to load the user directory type (%s): "
              + "Failed to retrieve the user directory class for the user directory type",
              userDirectoryType.getId()), e);

          continue;
        }

        reloadedUserDirectoryTypes.put(userDirectoryType.getId(), userDirectoryType);
      }

      this.userDirectoryTypes = reloadedUserDirectoryTypes;
    }
    catch (Throwable e)
    {
      throw new SecurityServiceException("Failed to reload the user directory types", e);
    }
  }

  /**
   * Returns <code>true</code> if the user directory exists or <code>false</code> otherwise.
   *
   * @param connection      the existing database connection to use
   * @param userDirectoryId the Universally Unique Identifier (UUID) used to uniquely identify the
   *                        user directory
   *
   * @return <code>true</code> if the user directory exists or <code>false</code> otherwise
   */
  private boolean userDirectoryExists(Connection connection, UUID userDirectoryId)
    throws SecurityServiceException
  {
    String userDirectoryExistsSQL = "SELECT COUNT(id) FROM security.user_directories WHERE id=?";

    try (PreparedStatement statement = connection.prepareStatement(userDirectoryExistsSQL))
    {
      statement.setObject(1, userDirectoryId);

      try (ResultSet rs = statement.executeQuery())
      {
        return rs.next() && (rs.getInt(1) > 0);
      }
    }
    catch (Throwable e)
    {
      throw new SecurityServiceException(String.format(
          "Failed to check whether the user_directory (%s) exists", userDirectoryId), e);
    }
  }
}
