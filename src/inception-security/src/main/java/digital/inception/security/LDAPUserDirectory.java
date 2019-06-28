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
import digital.inception.core.util.JNDIUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.util.StringUtils;

//~--- JDK imports ------------------------------------------------------------

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

import java.util.*;

import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.*;
import javax.naming.ldap.LdapName;

import javax.sql.DataSource;

/**
 * The <code>LDAPUserDirectory</code> class provides the LDAP user directory implementation.
 *
 * @author Marcus Portmann
 */
@SuppressWarnings({ "unused", "WeakerAccess", "SpringJavaAutowiredMembersInspection" })
public class LDAPUserDirectory extends UserDirectoryBase
{
  /**
   * The default number of failed password attempts before the user is locked.
   */
  public static final int DEFAULT_MAX_PASSWORD_ATTEMPTS = 5;

  /**
   * The default number of months before a user's password expires.
   */
  public static final int DEFAULT_PASSWORD_EXPIRY_MONTHS = 3;

  /**
   * The default number of months to check password history against.
   */
  public static final int DEFAULT_PASSWORD_HISTORY_MONTHS = 12;

  /**
   * The default maximum number of filtered groups.
   */
  private static final int DEFAULT_MAX_FILTERED_GROUPS = 100;

  /**
   * The default maximum number of filtered users.
   */
  private static final int DEFAULT_MAX_FILTERED_USERS = 100;

  /**
   * The default maximum length of a users's password history.
   */
  private static final int DEFAULT_PASSWORD_HISTORY_MAX_LENGTH = 128;
  private static final String[] EMPTY_ATTRIBUTE_LIST = new String[0];

  /* Logger */
  private static final Logger logger = LoggerFactory.getLogger(LDAPUserDirectory.class);
  private LdapName baseDN;
  private String bindDN;
  private String bindPassword;

  /**
   * The data source used to provide connections to the application database.
   */
  @Autowired
  @Qualifier("applicationDataSource")
  private DataSource dataSource;
  private LdapName groupBaseDN;
  private String groupDescriptionAttribute;
  private String groupMemberAttribute;
  private String[] groupMemberAttributeArray;
  private String groupNameAttribute;
  private String groupObjectClass;
  private String host;

  /**
   * The ID generator.
   */
  @Autowired
  private IDGenerator idGenerator;
  private int maxFilteredGroups;
  private int maxFilteredUsers;
  private int maxPasswordAttempts;
  private int passwordExpiryMonths;
  private int passwordHistoryMaxLength;
  private int passwordHistoryMonths;
  private int port;
  private LdapName sharedBaseDN;
  private boolean supportPasswordHistory;
  private boolean useSSL;
  private LdapName userBaseDN;
  private String userEmailAttribute;
  private String userFirstNameAttribute;
  private String userLastNameAttribute;
  private String userMobileNumberAttribute;
  private String userObjectClass;
  private String userPasswordAttemptsAttribute;
  private String userPasswordExpiryAttribute;
  private String userPasswordHistoryAttribute;
  private String[] userPasswordHistoryAttributeArray;
  private String userPhoneNumberAttribute;
  private String userUsernameAttribute;

  /**
   * Constructs a new <code>LDAPUserDirectory</code>.
   *
   * @param userDirectoryId the Universally Unique Identifier (UUID) used to uniquely identify the
   *                        user directory
   * @param parameters      the parameters for the user directory
   */
  public LDAPUserDirectory(UUID userDirectoryId, List<UserDirectoryParameter> parameters)
    throws SecurityServiceException
  {
    super(userDirectoryId, parameters);

    try
    {
      if (UserDirectoryParameter.contains(parameters, "Host"))
      {
        host = UserDirectoryParameter.getStringValue(parameters, "Host");
      }
      else
      {
        throw new SecurityServiceException(String.format(
            "No Host parameter found for the user directory (%s)", userDirectoryId));
      }

      if (UserDirectoryParameter.contains(parameters, "Port"))
      {
        port = UserDirectoryParameter.getIntegerValue(parameters, "Port");
      }
      else
      {
        throw new SecurityServiceException(String.format(
            "No Port parameter found for the user directory (%s)", userDirectoryId));
      }

      useSSL = UserDirectoryParameter.contains(parameters, "UseSSL")
          && Boolean.parseBoolean(UserDirectoryParameter.getStringValue(parameters, "UseSSL"));

      if (UserDirectoryParameter.contains(parameters, "BindDN"))
      {
        bindDN = UserDirectoryParameter.getStringValue(parameters, "BindDN");
      }
      else
      {
        throw new SecurityServiceException(String.format(
            "No BindDN parameter found for the user directory (%s)", userDirectoryId));
      }

      if (UserDirectoryParameter.contains(parameters, "BindPassword"))
      {
        bindPassword = UserDirectoryParameter.getStringValue(parameters, "BindPassword");
      }
      else
      {
        throw new SecurityServiceException(String.format(
            "No BindPassword parameter found for the user directory (%s)", userDirectoryId));
      }

      if (UserDirectoryParameter.contains(parameters, "BaseDN"))
      {
        baseDN = new LdapName(UserDirectoryParameter.getStringValue(parameters, "BaseDN"));
      }
      else
      {
        throw new SecurityServiceException(String.format(
            "No BindDN parameter found for the user directory (%s)", userDirectoryId));
      }

      if (UserDirectoryParameter.contains(parameters, "UserBaseDN"))
      {
        userBaseDN = new LdapName(UserDirectoryParameter.getStringValue(parameters, "UserBaseDN"));
      }
      else
      {
        throw new SecurityServiceException(String.format(
            "No UserBaseDN parameter found for the user directory (%s)", userDirectoryId));
      }

      if (UserDirectoryParameter.contains(parameters, "GroupBaseDN"))
      {
        groupBaseDN = new LdapName(UserDirectoryParameter.getStringValue(parameters,
            "GroupBaseDN"));
      }
      else
      {
        throw new SecurityServiceException(String.format(
            "No GroupBaseDN parameter found for the user directory (%s)", userDirectoryId));
      }

      if ((UserDirectoryParameter.contains(parameters, "SharedBaseDN"))
          && (!StringUtils.isEmpty(UserDirectoryParameter.getStringValue(parameters,
              "SharedBaseDN"))))
      {
        sharedBaseDN = new LdapName(UserDirectoryParameter.getStringValue(parameters,
            "SharedBaseDN"));
      }

      if (UserDirectoryParameter.contains(parameters, "UserObjectClass"))
      {
        userObjectClass = UserDirectoryParameter.getStringValue(parameters, "UserObjectClass");
      }
      else
      {
        throw new SecurityServiceException(String.format(
            "No UserObjectClass parameter found for the user directory (%s)", userDirectoryId));
      }

      if (UserDirectoryParameter.contains(parameters, "UserUsernameAttribute"))
      {
        userUsernameAttribute = UserDirectoryParameter.getStringValue(parameters,
            "UserUsernameAttribute");
      }
      else
      {
        throw new SecurityServiceException(String.format(
            "No UserUsernameAttribute parameter found for the user directory (%s)",
            userDirectoryId));
      }

      if (UserDirectoryParameter.contains(parameters, "UserPasswordExpiryAttribute"))
      {
        userPasswordExpiryAttribute = UserDirectoryParameter.getStringValue(parameters,
            "UserPasswordExpiryAttribute");
      }
      else
      {
        throw new SecurityServiceException(String.format(
            "No UserPasswordExpiryAttribute parameter found for the user directory " + "(%s)",
            userDirectoryId));
      }

      if (UserDirectoryParameter.contains(parameters, "UserPasswordAttemptsAttribute"))
      {
        userPasswordAttemptsAttribute = UserDirectoryParameter.getStringValue(parameters,
            "UserPasswordAttemptsAttribute");
      }
      else
      {
        throw new SecurityServiceException(String.format(
            "No UserPasswordAttemptsAttribute parameter found for the user directory " + "(%s)",
            userDirectoryId));
      }

      if (UserDirectoryParameter.contains(parameters, "UserPasswordHistoryAttribute"))
      {
        userPasswordHistoryAttribute = UserDirectoryParameter.getStringValue(parameters,
            "UserPasswordHistoryAttribute");
        userPasswordHistoryAttributeArray = new String[] { userPasswordHistoryAttribute };
      }
      else
      {
        throw new SecurityServiceException(String.format(
            "No UserPasswordHistoryAttribute parameter found for the user directory " + "(%s)",
            userDirectoryId));
      }

      if (UserDirectoryParameter.contains(parameters, "UserFirstNameAttribute"))
      {
        userFirstNameAttribute = UserDirectoryParameter.getStringValue(parameters,
            "UserFirstNameAttribute");
      }
      else
      {
        throw new SecurityServiceException(String.format(
            "No UserFirstNameAttribute parameter found for the user directory (%s)",
            userDirectoryId));
      }

      if (UserDirectoryParameter.contains(parameters, "UserLastNameAttribute"))
      {
        userLastNameAttribute = UserDirectoryParameter.getStringValue(parameters,
            "UserLastNameAttribute");
      }
      else
      {
        throw new SecurityServiceException(String.format(
            "No UserLastNameAttribute parameter found for the user directory (%s)",
            userDirectoryId));
      }

      if (UserDirectoryParameter.contains(parameters, "UserPhoneNumberAttribute"))
      {
        userPhoneNumberAttribute = UserDirectoryParameter.getStringValue(parameters,
            "UserPhoneNumberAttribute");
      }
      else
      {
        throw new SecurityServiceException(String.format(
            "No UserPhoneNumberAttribute parameter found for the user directory (%s)",
            userDirectoryId));
      }

      if (UserDirectoryParameter.contains(parameters, "UserMobileNumberAttribute"))
      {
        userMobileNumberAttribute = UserDirectoryParameter.getStringValue(parameters,
            "UserMobileNumberAttribute");
      }
      else
      {
        throw new SecurityServiceException(String.format(
            "No UserMobileNumberAttribute parameter found for the user directory (%s)",
            userDirectoryId));
      }

      if (UserDirectoryParameter.contains(parameters, "UserEmailAttribute"))
      {
        userEmailAttribute = UserDirectoryParameter.getStringValue(parameters,
            "UserEmailAttribute");
      }
      else
      {
        throw new SecurityServiceException(String.format(
            "No UserEmailAttribute parameter found for the user directory (%s)", userDirectoryId));
      }

      if (UserDirectoryParameter.contains(parameters, "GroupObjectClass"))
      {
        groupObjectClass = UserDirectoryParameter.getStringValue(parameters, "GroupObjectClass");
      }
      else
      {
        throw new SecurityServiceException(String.format(
            "No GroupObjectClass parameter found for the user directory (%s)", userDirectoryId));
      }

      if (UserDirectoryParameter.contains(parameters, "GroupNameAttribute"))
      {
        groupNameAttribute = UserDirectoryParameter.getStringValue(parameters,
            "GroupNameAttribute");
      }
      else
      {
        throw new SecurityServiceException(String.format(
            "No GroupNameAttribute parameter found for the user directory (%s)", userDirectoryId));
      }

      if (UserDirectoryParameter.contains(parameters, "GroupMemberAttribute"))
      {
        groupMemberAttribute = UserDirectoryParameter.getStringValue(parameters,
            "GroupMemberAttribute");

        groupMemberAttributeArray = new String[] { groupMemberAttribute };
      }
      else
      {
        throw new SecurityServiceException(String.format(
            "No GroupMemberAttribute parameter found for the user directory (%s)",
            userDirectoryId));
      }

      if (UserDirectoryParameter.contains(parameters, "GroupDescriptionAttribute"))
      {
        groupDescriptionAttribute = UserDirectoryParameter.getStringValue(parameters,
            "GroupDescriptionAttribute");
      }

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

      supportPasswordHistory = UserDirectoryParameter.contains(parameters, "SupportPasswordHistory")
          && Boolean.parseBoolean(UserDirectoryParameter.getStringValue(parameters,
              "SupportPasswordHistory"));

      if (UserDirectoryParameter.contains(parameters, "PasswordHistoryMonths"))
      {
        passwordHistoryMonths = UserDirectoryParameter.getIntegerValue(parameters,
            "PasswordHistoryMonths");
      }
      else
      {
        passwordHistoryMonths = DEFAULT_PASSWORD_HISTORY_MONTHS;
      }

      if (UserDirectoryParameter.contains(parameters, "PasswordHistoryMaxLength"))
      {
        passwordHistoryMaxLength = UserDirectoryParameter.getIntegerValue(parameters,
            "PasswordHistoryMaxLength");
      }
      else
      {
        passwordHistoryMonths = DEFAULT_PASSWORD_HISTORY_MAX_LENGTH;
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
   * Add the user to the security group.
   *
   * @param username  the username identifying the user
   * @param groupName the name of the security group uniquely identifying the security group
   */
  @Override
  public void addUserToGroup(String username, String groupName)
    throws UserNotFoundException, GroupNotFoundException, SecurityServiceException
  {
    DirContext dirContext = null;

    try
    {
      dirContext = getDirContext(bindDN, bindPassword);

      LdapName userDN = getUserDN(dirContext, username);

      if (userDN == null)
      {
        throw new UserNotFoundException(username);
      }

      LdapName groupDN = getGroupDN(dirContext, groupName);

      if (groupDN == null)
      {
        throw new GroupNotFoundException(groupName);
      }

      Attributes attributes = dirContext.getAttributes(groupDN, groupMemberAttributeArray);

      BasicAttribute attribute = new BasicAttribute(groupMemberAttribute);

      if (attributes.get(groupMemberAttribute) != null)
      {
        @SuppressWarnings("unchecked")
        NamingEnumeration<String> groupMembers = (NamingEnumeration<String>) attributes.get(
            groupMemberAttribute).getAll();

        while (groupMembers.hasMore())
        {
          LdapName groupMemberDN = new LdapName(groupMembers.next());

          if (groupMemberDN.equals(userDN))
          {
            return;
          }
          else
          {
            attribute.add(groupMemberDN.toString());
          }
        }
      }

      attribute.add(userDN.toString());

      dirContext.modifyAttributes(groupDN, new ModificationItem[] { new ModificationItem(DirContext
          .REPLACE_ATTRIBUTE, attribute) });
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
    finally
    {
      JNDIUtil.close(dirContext);
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
    DirContext dirContext = null;

    try
    {
      dirContext = getDirContext(bindDN, bindPassword);

      LdapName userDN = getUserDN(dirContext, username);

      if (userDN == null)
      {
        throw new UserNotFoundException(username);
      }

      List<ModificationItem> modificationItems = new ArrayList<>();

      BasicAttribute passwordAttribute = new BasicAttribute("userPassword");
      passwordAttribute.add(newPassword);

      modificationItems.add(new ModificationItem(DirContext.REPLACE_ATTRIBUTE, passwordAttribute));

      if (!StringUtils.isEmpty(userPasswordExpiryAttribute))
      {
        if (expirePassword)
        {
          BasicAttribute passwordExpiryAttribute = new BasicAttribute(userPasswordExpiryAttribute);
          passwordExpiryAttribute.add("0");

          modificationItems.add(new ModificationItem(DirContext.REPLACE_ATTRIBUTE,
              passwordExpiryAttribute));
        }
        else
        {
          Calendar calendar = Calendar.getInstance();
          calendar.add(Calendar.MONTH, passwordExpiryMonths);

          BasicAttribute passwordExpiryAttribute = new BasicAttribute(userPasswordExpiryAttribute);
          passwordExpiryAttribute.add(String.valueOf(calendar.getTimeInMillis()));

          modificationItems.add(new ModificationItem(DirContext.REPLACE_ATTRIBUTE,
              passwordExpiryAttribute));
        }
      }

      if (!StringUtils.isEmpty(userPasswordAttemptsAttribute))
      {
        if (lockUser)
        {
          BasicAttribute passwordAttemptsAttribute = new BasicAttribute(
              userPasswordAttemptsAttribute);
          passwordAttemptsAttribute.add(String.valueOf(maxPasswordAttempts));

          modificationItems.add(new ModificationItem(DirContext.REPLACE_ATTRIBUTE,
              passwordAttemptsAttribute));
        }
        else
        {
          BasicAttribute passwordAttemptsAttribute = new BasicAttribute(
              userPasswordAttemptsAttribute);
          passwordAttemptsAttribute.add("0");

          modificationItems.add(new ModificationItem(DirContext.REPLACE_ATTRIBUTE,
              passwordAttemptsAttribute));
        }
      }

      if (!StringUtils.isEmpty(userPasswordHistoryAttribute))
      {
        if (resetPasswordHistory)
        {
          BasicAttribute passwordHistoryAttribute = new BasicAttribute(
              userPasswordHistoryAttribute);

          modificationItems.add(new ModificationItem(DirContext.REMOVE_ATTRIBUTE,
              passwordHistoryAttribute));
        }
      }

      dirContext.modifyAttributes(userDN, modificationItems.toArray(new ModificationItem[0]));
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
    finally
    {
      JNDIUtil.close(dirContext);
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
    DirContext dirContext = null;

    try
    {
      dirContext = getDirContext(bindDN, bindPassword);

      User user = getUser(dirContext, username);

      if (user == null)
      {
        throw new UserNotFoundException(username);
      }

      LdapName userDN = new LdapName(user.getExternalReference());

      if (!userDN.startsWith(sharedBaseDN))
      {
        if ((user.getPasswordAttempts() != null)
            && (user.getPasswordAttempts() >= maxPasswordAttempts))
        {
          throw new UserLockedException(username);
        }

        if (user.hasPasswordExpired())
        {
          throw new ExpiredPasswordException(username);
        }
      }

      DirContext userDirContext = null;

      try
      {
        userDirContext = getDirContext(user.getExternalReference(), password);

        user.setPassword(password);
      }
      catch (Throwable e)
      {
        if (e.getCause() instanceof javax.naming.AuthenticationException)
        {
          incrementPasswordAttempts(dirContext, user);

          throw new AuthenticationFailedException(String.format(
              "Failed to authenticate the user (%s) for the user directory (%s)", username,
              getUserDirectoryId()));
        }
        else
        {
          logger.error(String.format(
              "Failed to authenticate the user (%s) for the user directory (%s)", username,
              getUserDirectoryId()), e);

          throw new AuthenticationFailedException(String.format(
              "Failed to authenticate the user (%s) for the user directory (%s)", username,
              getUserDirectoryId()), e);
        }
      }
      finally
      {
        JNDIUtil.close(userDirContext);
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
    finally
    {
      JNDIUtil.close(dirContext);
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
    throw new SecurityServiceException("TODO: NOT IMPLEMENTED");

/*

    try (Connection connection = getDataSource().getConnection();
      PreparedStatement statement = connection.prepareStatement(changeInternalUserPasswordSQL))
    {
      User user = getUser(connection, username);

      if (user == null)
      {
        throw new UserNotFoundException(username);
      }

      if ((user.getPasswordAttempts() == null)
          || (user.getPasswordAttempts() > maxPasswordAttempts))
      {
        throw new UserLockedException("The user (" + username
            + ") has exceeded the number of failed password attempts and has been locked");
      }

      String passwordHash = createPasswordHash(password);
      String newPasswordHash = createPasswordHash(newPassword);

      if (!user.getPassword().equals(passwordHash))
      {
        throw new AuthenticationFailedException("Authentication failed while attempting to change "
            + "the password for the user (" + username + ")");
      }

      if (isPasswordInHistory(connection, user.getId(), newPasswordHash))
      {
        throw new ExistingPasswordException("The new password for the user (" + username
            + ") has been used recently and is not valid");
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

      statement.setLong(4, getUserDirectoryId());
      statement.setLong(5, user.getId());

      if (statement.executeUpdate() != 1)
      {
        throw new SecurityServiceException(
            "No rows were affected as a result of executing the SQL statement ("
            + changeInternalUserPasswordSQL + ")");
      }

      savePasswordHistory(connection, user.getId(), newPasswordHash);
    }
    catch (AuthenticationFailedException | ExistingPasswordException | UserNotFoundException
        | ExpiredPasswordException | UserLockedException e)
    {
      throw e;
    }
    catch (Throwable e)
    {
      throw new SecurityServiceException("Failed to change the password for the user (" + username
          + ") for the user directory (" + getUserDirectoryId() + "): " + e.getMessage(), e);
    }


 */

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
    DirContext dirContext = null;

    try (Connection connection = dataSource.getConnection())
    {
      dirContext = getDirContext(bindDN, bindPassword);

      LdapName groupDN = getGroupDN(dirContext, group.getGroupName());

      if (groupDN != null)
      {
        throw new DuplicateGroupException(group.getGroupName());
      }

      Attributes attributes = new BasicAttributes();

      attributes.put(new BasicAttribute("objectclass", "top"));
      attributes.put(new BasicAttribute("objectclass", groupObjectClass));

      attributes.put(new BasicAttribute(groupNameAttribute, group.getGroupName()));

      if (!StringUtils.isEmpty(groupDescriptionAttribute))
      {
        attributes.put(new BasicAttribute(groupDescriptionAttribute,
            StringUtils.isEmpty(group.getDescription())
            ? ""
            : group.getDescription()));
      }

      dirContext.bind(groupNameAttribute + "=" + group.getGroupName() + ","
          + groupBaseDN.toString(), dirContext, attributes);

      // Create the corresponding group in the database that will be used to map to one or more roles
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
    finally
    {
      JNDIUtil.close(dirContext);
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
    DirContext dirContext = null;

    try
    {
      dirContext = getDirContext(bindDN, bindPassword);

      LdapName userDN = getUserDN(dirContext, user.getUsername());

      if (userDN != null)
      {
        throw new DuplicateUserException(user.getUsername());
      }

      Attributes attributes = new BasicAttributes();

      attributes.put(new BasicAttribute("objectclass", "top"));
      attributes.put(new BasicAttribute("objectclass", userObjectClass));

      attributes.put(new BasicAttribute(userUsernameAttribute, user.getUsername()));

      if (!StringUtils.isEmpty(userFirstNameAttribute))
      {
        attributes.put(new BasicAttribute(userFirstNameAttribute,
            StringUtils.isEmpty(user.getFirstName())
            ? ""
            : user.getFirstName()));
      }

      if (!StringUtils.isEmpty(userLastNameAttribute))
      {
        attributes.put(new BasicAttribute(userLastNameAttribute,
            StringUtils.isEmpty(user.getLastName())
            ? ""
            : user.getLastName()));
      }

      if (!StringUtils.isEmpty(userEmailAttribute))
      {
        attributes.put(new BasicAttribute(userEmailAttribute,
            StringUtils.isEmpty(user.getEmail())
            ? ""
            : user.getEmail()));
      }

      if (!StringUtils.isEmpty(userPhoneNumberAttribute))
      {
        attributes.put(new BasicAttribute(userPhoneNumberAttribute,
            StringUtils.isEmpty(user.getPhoneNumber())
            ? ""
            : user.getPhoneNumber()));
      }

      if (!StringUtils.isEmpty(userMobileNumberAttribute))
      {
        attributes.put(new BasicAttribute(userMobileNumberAttribute,
            StringUtils.isEmpty(user.getMobileNumber())
            ? ""
            : user.getMobileNumber()));
      }

      String passwordHash;

      if (!isNullOrEmpty(user.getPassword()))
      {
        passwordHash = createPasswordHash(user.getPassword());
      }
      else
      {
        passwordHash = createPasswordHash("");
      }

      if (!StringUtils.isEmpty(userPasswordHistoryAttribute))
      {
        attributes.put(new BasicAttribute(userPasswordHistoryAttribute, passwordHash));
      }

      attributes.put(new BasicAttribute("userPassword",
          StringUtils.isEmpty(user.getPassword())
          ? ""
          : user.getPassword()));

      if (!StringUtils.isEmpty(userPasswordAttemptsAttribute))
      {
        if (userLocked)
        {
          attributes.put(new BasicAttribute(userPasswordAttemptsAttribute, String.valueOf(
              maxPasswordAttempts)));
        }
        else
        {
          attributes.put(new BasicAttribute(userPasswordAttemptsAttribute, "0"));
        }
      }

      if (!StringUtils.isEmpty(userPasswordExpiryAttribute))
      {
        if (expiredPassword)
        {
          attributes.put(new BasicAttribute(userPasswordExpiryAttribute, "0"));
        }
        else
        {
          Calendar calendar = Calendar.getInstance();
          calendar.add(Calendar.MONTH, passwordExpiryMonths);

          attributes.put(new BasicAttribute(userPasswordExpiryAttribute, String.valueOf(
              calendar.getTimeInMillis())));
        }
      }

      userDN = new LdapName(userUsernameAttribute + "=" + user.getUsername() + ","
          + userBaseDN.toString());

      dirContext.bind(userDN, dirContext, attributes);
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
    finally
    {
      JNDIUtil.close(dirContext);
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
    DirContext dirContext = null;

    try (Connection connection = dataSource.getConnection())
    {
      dirContext = getDirContext(bindDN, bindPassword);

      LdapName groupDN = getGroupDN(dirContext, groupName);

      if (groupDN == null)
      {
        throw new GroupNotFoundException(groupName);
      }

      Attributes attributes = dirContext.getAttributes(groupDN, groupMemberAttributeArray);

      if ((attributes.get(groupMemberAttribute) != null)
          && (attributes.get(groupMemberAttribute).size() > 0))
      {
        throw new ExistingGroupMembersException(groupName);
      }

      dirContext.destroySubcontext(groupDN);

      // Delete the corresponding group in the database
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
    finally
    {
      JNDIUtil.close(dirContext);
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
    DirContext dirContext = null;

    try
    {
      dirContext = getDirContext(bindDN, bindPassword);

      LdapName userDN = getUserDN(dirContext, username);

      if (userDN == null)
      {
        throw new UserNotFoundException(username);
      }

      dirContext.destroySubcontext(userDN);
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
    finally
    {
      JNDIUtil.close(dirContext);
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
    DirContext dirContext = null;
    NamingEnumeration<SearchResult> searchResultsNonSharedUsers = null;
    NamingEnumeration<SearchResult> searchResultsSharedUsers = null;

    try
    {
      dirContext = getDirContext(bindDN, bindPassword);

      String searchFilter = "(objectClass=" + userObjectClass + ")";

      if (attributes.size() > 0)
      {
        StringBuilder buffer = new StringBuilder();

        buffer.append("(&(objectClass=");
        buffer.append(userObjectClass);
        buffer.append(")");

        for (Attribute attribute : attributes)
        {
          buffer.append("(");
          buffer.append(attribute.getName());
          buffer.append("=*");

          buffer.append(attribute.getValue());

          buffer.append("*)");
        }

        buffer.append(")");

        searchFilter = buffer.toString();
      }

      SearchControls searchControls = new SearchControls();
      searchControls.setSearchScope(SearchControls.SUBTREE_SCOPE);
      searchControls.setReturningObjFlag(false);
      searchControls.setCountLimit(maxFilteredUsers);

      List<User> users = new ArrayList<>();

      searchResultsNonSharedUsers = dirContext.search(userBaseDN, searchFilter, searchControls);

      while (searchResultsNonSharedUsers.hasMore() && (users.size() <= maxFilteredUsers))
      {
        users.add(buildUserFromSearchResult(searchResultsNonSharedUsers.next(), false));
      }

      if (sharedBaseDN != null)
      {
        searchResultsSharedUsers = dirContext.search(sharedBaseDN, searchFilter, searchControls);

        while (searchResultsSharedUsers.hasMore() && (users.size() <= maxFilteredUsers))
        {
          users.add(buildUserFromSearchResult(searchResultsSharedUsers.next(), true));
        }
      }

      return users;
    }
    catch (Throwable e)
    {
      throw new SecurityServiceException(String.format(
          "Failed to find the users for the user directory (%s)", getUserDirectoryId()), e);
    }
    finally
    {
      JNDIUtil.close(searchResultsSharedUsers);
      JNDIUtil.close(searchResultsNonSharedUsers);
      JNDIUtil.close(dirContext);
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
    DirContext dirContext = null;
    NamingEnumeration<SearchResult> searchResults = null;

    try
    {
      dirContext = getDirContext(bindDN, bindPassword);

      LdapName userDN = getUserDN(dirContext, username);

      if (userDN == null)
      {
        throw new UserNotFoundException(username);
      }

      String searchFilter = String.format("(&(objectClass=%s)(%s=%s))", groupObjectClass,
          groupMemberAttribute, userDN.toString());

      SearchControls searchControls = new SearchControls();
      searchControls.setSearchScope(SearchControls.SUBTREE_SCOPE);
      searchControls.setReturningObjFlag(false);

      searchResults = dirContext.search(groupBaseDN, searchFilter, searchControls);

      List<String> groupNames = new ArrayList<>();

      while (searchResults.hasMore())
      {
        SearchResult searchResult = searchResults.next();

        if (searchResult.getAttributes().get(groupNameAttribute) != null)
        {
          groupNames.add(String.valueOf(searchResult.getAttributes().get(groupNameAttribute)
              .get()));
        }
      }

      /*
       * Build the SQL statement used to retrieve the function codes associated with the LDAP
       * groups the user is a member of.
       *
       * NOTE: This is not the ideal solution as a carefully crafted group name in the LDAP
       *       directory can be used to perpetrate a SQL injection attack. A better option
       *       would to be to use the ANY operator in the WHERE clause but this is not
       *       supported by H2.
       */
      String getFunctionCodesForGroupsSQL = "SELECT DISTINCT f.code FROM security.functions f "
          + "INNER JOIN security.function_to_role_map ftrm ON ftrm.function_id = f.id "
          + "INNER JOIN security.role_to_group_map rtgm ON rtgm.role_id = ftrm.role_id "
          + "INNER JOIN security.groups g ON g.id = rtgm.group_id";

      StringBuilder buffer = new StringBuilder(getFunctionCodesForGroupsSQL);
      buffer.append(" WHERE g.user_directory_id='").append(getUserDirectoryId());
      buffer.append("' AND g.groupname IN (");

      for (int i = 0; i < groupNames.size(); i++)
      {
        if (i > 0)
        {
          buffer.append(",");
        }

        buffer.append("'").append(groupNames.get(i).replaceAll("'", "''")).append("'");
      }

      buffer.append(")");

      List<String> functionCodes = new ArrayList<>();

      try (Connection connection = dataSource.getConnection();
        Statement statement = connection.createStatement())
      {
        try (ResultSet rs = statement.executeQuery(buffer.toString()))
        {
          while (rs.next())
          {
            functionCodes.add(rs.getString(1));
          }
        }
      }

      return functionCodes;
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
    finally
    {
      JNDIUtil.close(searchResults);
      JNDIUtil.close(dirContext);
    }
  }

  /**
   * Retrieve the security group.
   *
   * @param groupName the name of the security group uniquely identifying the security group
   *
   * @return the security group
   */
  @Override
  public Group getGroup(String groupName)
    throws GroupNotFoundException, SecurityServiceException
  {
    DirContext dirContext = null;
    NamingEnumeration<SearchResult> searchResults = null;

    try
    {
      dirContext = getDirContext(bindDN, bindPassword);

      String searchFilter = String.format("(&(objectClass=%s)(%s=%s))", groupObjectClass,
          groupNameAttribute, groupName);

      SearchControls searchControls = new SearchControls();
      searchControls.setSearchScope(SearchControls.SUBTREE_SCOPE);
      searchControls.setReturningObjFlag(false);

      searchResults = dirContext.search(groupBaseDN, searchFilter, searchControls);

      if (searchResults.hasMore())
      {
        return buildGroupFromSearchResult(searchResults.next());
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
    finally
    {
      JNDIUtil.close(searchResults);
      JNDIUtil.close(dirContext);
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
    DirContext dirContext = null;
    NamingEnumeration<SearchResult> searchResults = null;

    try
    {
      dirContext = getDirContext(bindDN, bindPassword);

      LdapName userDN = getUserDN(dirContext, username);

      if (userDN == null)
      {
        throw new UserNotFoundException(username);
      }

      String searchFilter = String.format("(&(objectClass=%s)(%s=%s))", groupObjectClass,
          groupMemberAttribute, userDN.toString());

      SearchControls searchControls = new SearchControls();
      searchControls.setSearchScope(SearchControls.SUBTREE_SCOPE);
      searchControls.setReturningObjFlag(false);

      searchResults = dirContext.search(groupBaseDN, searchFilter, searchControls);

      List<String> groupNames = new ArrayList<>();

      while (searchResults.hasMore())
      {
        SearchResult searchResult = searchResults.next();

        if (searchResult.getAttributes().get(groupNameAttribute) != null)
        {
          groupNames.add(String.valueOf(searchResult.getAttributes().get(groupNameAttribute)
              .get()));
        }
      }

      return groupNames;
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
    finally
    {
      JNDIUtil.close(searchResults);
      JNDIUtil.close(dirContext);
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
    DirContext dirContext = null;
    NamingEnumeration<SearchResult> searchResults = null;

    try
    {
      dirContext = getDirContext(bindDN, bindPassword);

      String searchFilter = String.format("(objectClass=%s)", groupObjectClass);

      SearchControls searchControls = new SearchControls();
      searchControls.setSearchScope(SearchControls.SUBTREE_SCOPE);
      searchControls.setReturningObjFlag(false);
      searchControls.setCountLimit(maxFilteredGroups);

      searchResults = dirContext.search(groupBaseDN, searchFilter, searchControls);

      List<Group> groups = new ArrayList<>();

      while (searchResults.hasMore())
      {
        groups.add(buildGroupFromSearchResult(searchResults.next()));
      }

      return groups;
    }
    catch (Throwable e)
    {
      throw new SecurityServiceException(String.format(
          "Failed to retrieve the security groups for the user directory (%s)",
          getUserDirectoryId()), e);
    }
    finally
    {
      JNDIUtil.close(searchResults);
      JNDIUtil.close(dirContext);
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
    DirContext dirContext = null;
    NamingEnumeration<SearchResult> searchResults = null;

    try
    {
      dirContext = getDirContext(bindDN, bindPassword);

      LdapName userDN = getUserDN(dirContext, username);

      if (userDN == null)
      {
        throw new UserNotFoundException(username);
      }

      String searchFilter = String.format("(&(objectClass=%s)(%s=%s))", groupObjectClass,
          groupMemberAttribute, userDN.toString());

      SearchControls searchControls = new SearchControls();
      searchControls.setSearchScope(SearchControls.SUBTREE_SCOPE);
      searchControls.setReturningObjFlag(false);

      searchResults = dirContext.search(groupBaseDN, searchFilter, searchControls);

      List<Group> groups = new ArrayList<>();

      while (searchResults.hasMore())
      {
        groups.add(buildGroupFromSearchResult(searchResults.next()));
      }

      return groups;
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
    finally
    {
      JNDIUtil.close(searchResults);
      JNDIUtil.close(dirContext);
    }
  }

  /**
   * Retrieve the number of security groups
   *
   * @return the number of security groups
   */
  @Override
  public int getNumberOfGroups()
    throws SecurityServiceException
  {
    DirContext dirContext = null;
    NamingEnumeration<SearchResult> searchResults = null;

    try
    {
      dirContext = getDirContext(bindDN, bindPassword);

      String searchFilter = String.format("(objectClass=%s)", groupObjectClass);

      SearchControls searchControls = new SearchControls();
      searchControls.setSearchScope(SearchControls.SUBTREE_SCOPE);
      searchControls.setReturningObjFlag(false);
      searchControls.setReturningAttributes(EMPTY_ATTRIBUTE_LIST);
      searchControls.setCountLimit(maxFilteredGroups);

      searchResults = dirContext.search(groupBaseDN, searchFilter, searchControls);

      int numberOfGroups = 0;

      while (searchResults.hasMore())
      {
        searchResults.next();

        numberOfGroups++;
      }

      return numberOfGroups;
    }
    catch (Throwable e)
    {
      throw new SecurityServiceException(String.format(
          "Failed to retrieve the number of security groups for the user directory (%s):%s",
          getUserDirectoryId(), e.getMessage()), e);
    }
    finally
    {
      JNDIUtil.close(searchResults);
      JNDIUtil.close(dirContext);
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
    DirContext dirContext = null;
    NamingEnumeration<SearchResult> searchResultsNonSharedUsers = null;
    NamingEnumeration<SearchResult> searchResultsSharedUsers = null;

    try
    {
      dirContext = getDirContext(bindDN, bindPassword);

      String searchFilter = "(objectClass=" + userObjectClass + ")";

      if (!StringUtils.isEmpty(filter))
      {
        searchFilter = String.format("(&(objectClass=%s)(|(%s=*%s*)(%s=*%s*)(%s=*%s*)))",
            userObjectClass, userUsernameAttribute, filter, userFirstNameAttribute, filter,
            userLastNameAttribute, filter);
      }

      SearchControls searchControls = new SearchControls();
      searchControls.setSearchScope(SearchControls.SUBTREE_SCOPE);
      searchControls.setReturningObjFlag(false);
      searchControls.setReturningAttributes(EMPTY_ATTRIBUTE_LIST);
      searchControls.setCountLimit(maxFilteredUsers);

      int numberOfUsers = 0;

      searchResultsNonSharedUsers = dirContext.search(userBaseDN, searchFilter, searchControls);

      while (searchResultsNonSharedUsers.hasMore() && (numberOfUsers <= maxFilteredUsers))
      {
        searchResultsNonSharedUsers.next();

        numberOfUsers++;
      }

      if (sharedBaseDN != null)
      {
        searchResultsSharedUsers = dirContext.search(sharedBaseDN, searchFilter, searchControls);

        while (searchResultsSharedUsers.hasMore() && (numberOfUsers <= maxFilteredUsers))
        {
          searchResultsSharedUsers.next();

          numberOfUsers++;
        }
      }

      return numberOfUsers;
    }
    catch (Throwable e)
    {
      throw new SecurityServiceException(String.format(
          "Failed to retrieve the number of users for the user directory (%s):%s",
          getUserDirectoryId(), e.getMessage()), e);
    }
    finally
    {
      JNDIUtil.close(searchResultsSharedUsers);
      JNDIUtil.close(searchResultsNonSharedUsers);
      JNDIUtil.close(dirContext);
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
    DirContext dirContext = null;
    NamingEnumeration<SearchResult> searchResults = null;

    try
    {
      dirContext = getDirContext(bindDN, bindPassword);

      LdapName userDN = getUserDN(dirContext, username);

      if (userDN == null)
      {
        throw new UserNotFoundException(username);
      }

      String searchFilter = String.format("(&(objectClass=%s)(%s=%s))", groupObjectClass,
          groupMemberAttribute, userDN.toString());

      SearchControls searchControls = new SearchControls();
      searchControls.setSearchScope(SearchControls.SUBTREE_SCOPE);
      searchControls.setReturningObjFlag(false);

      searchResults = dirContext.search(groupBaseDN, searchFilter, searchControls);

      List<String> groupNames = new ArrayList<>();

      while (searchResults.hasMore())
      {
        SearchResult searchResult = searchResults.next();

        if (searchResult.getAttributes().get(groupNameAttribute) != null)
        {
          groupNames.add(String.valueOf(searchResult.getAttributes().get(groupNameAttribute)
              .get()));
        }
      }

      /*
       * Build the SQL statement used to retrieve the function codes associated with the LDAP
       * groups the user is a member of.
       *
       * NOTE: This is not the ideal solution as a carefully crafted group name in the LDAP
       *       directory can be used to perpetrate a SQL injection attack. A better option
       *       would to be to use the ANY operator in the WHERE clause but this is not
       *       supported by H2.
       */
      String getFunctionCodesForGroupsSQL = "SELECT DISTINCT r.name FROM security.roles r "
          + "INNER JOIN security.role_to_group_map rtgm ON rtgm.role_id = r.id "
          + "INNER JOIN security.groups g ON g.id = rtgm.group_id";

      StringBuilder buffer = new StringBuilder(getFunctionCodesForGroupsSQL);
      buffer.append(" WHERE g.user_directory_id='").append(getUserDirectoryId());
      buffer.append("' AND g.groupname IN (");

      for (int i = 0; i < groupNames.size(); i++)
      {
        if (i > 0)
        {
          buffer.append(",");
        }

        buffer.append("'").append(groupNames.get(i).replaceAll("'", "''")).append("'");
      }

      buffer.append(")");

      List<String> roleNames = new ArrayList<>();

      try (Connection connection = dataSource.getConnection();
        Statement statement = connection.createStatement())
      {
        try (ResultSet rs = statement.executeQuery(buffer.toString()))
        {
          while (rs.next())
          {
            roleNames.add(rs.getString(1));
          }
        }
      }

      return roleNames;
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
    finally
    {
      JNDIUtil.close(searchResults);
      JNDIUtil.close(dirContext);
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
    DirContext dirContext = null;

    try
    {
      dirContext = getDirContext(bindDN, bindPassword);

      User user = getUser(dirContext, username);

      if (user == null)
      {
        throw new UserNotFoundException(username);
      }

      return user;
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
    finally
    {
      JNDIUtil.close(dirContext);
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
    DirContext dirContext = null;
    NamingEnumeration<SearchResult> searchResultsNonSharedUsers = null;
    NamingEnumeration<SearchResult> searchResultsSharedUsers = null;

    try
    {
      dirContext = getDirContext(bindDN, bindPassword);

      String searchFilter = String.format("(objectClass=%s)", userObjectClass);

      SearchControls searchControls = new SearchControls();
      searchControls.setSearchScope(SearchControls.SUBTREE_SCOPE);
      searchControls.setReturningObjFlag(false);
      searchControls.setCountLimit(maxFilteredUsers);

      List<User> users = new ArrayList<>();

      searchResultsNonSharedUsers = dirContext.search(userBaseDN, searchFilter, searchControls);

      while (searchResultsNonSharedUsers.hasMore() && (users.size() <= maxFilteredUsers))
      {
        users.add(buildUserFromSearchResult(searchResultsNonSharedUsers.next(), false));
      }

      if (sharedBaseDN != null)
      {
        searchResultsSharedUsers = dirContext.search(sharedBaseDN, searchFilter, searchControls);

        while (searchResultsSharedUsers.hasMore() && (users.size() <= maxFilteredUsers))
        {
          users.add(buildUserFromSearchResult(searchResultsSharedUsers.next(), true));
        }
      }

      return users;
    }
    catch (Throwable e)
    {
      throw new SecurityServiceException(String.format(
          "Failed to retrieve the users for the user directory (%s)", getUserDirectoryId()), e);
    }
    finally
    {
      JNDIUtil.close(searchResultsSharedUsers);
      JNDIUtil.close(searchResultsNonSharedUsers);
      JNDIUtil.close(dirContext);
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
  public List<User> getUsers(String filter, UserSortBy sortBy, SortDirection sortDirection,
      Integer pageIndex, Integer pageSize)
    throws SecurityServiceException
  {
    DirContext dirContext = null;
    NamingEnumeration<SearchResult> searchResultsNonSharedUsers = null;
    NamingEnumeration<SearchResult> searchResultsSharedUsers = null;

    try
    {
      dirContext = getDirContext(bindDN, bindPassword);

      String searchFilter = String.format("(objectClass=%s)", userObjectClass);

      if (!StringUtils.isEmpty(filter))
      {
        searchFilter = String.format("(&(objectClass=%s)(|(%s=*%s*)(%s=*%s*)(%s=*%s*)))",
            userObjectClass, userUsernameAttribute, filter, userFirstNameAttribute, filter,
            userLastNameAttribute, filter);
      }

      SearchControls searchControls = new SearchControls();
      searchControls.setSearchScope(SearchControls.SUBTREE_SCOPE);
      searchControls.setReturningObjFlag(false);
      searchControls.setCountLimit(maxFilteredUsers);

      // TODO: Implement sorting of users for LDAP queries -- MARCUS

      List<User> users = new ArrayList<>();

      searchResultsNonSharedUsers = dirContext.search(userBaseDN, searchFilter, searchControls);

      while (searchResultsNonSharedUsers.hasMore() && (users.size() <= maxFilteredUsers))
      {
        users.add(buildUserFromSearchResult(searchResultsNonSharedUsers.next(), false));
      }

      if (sharedBaseDN != null)
      {
        searchResultsSharedUsers = dirContext.search(sharedBaseDN, searchFilter, searchControls);

        while (searchResultsSharedUsers.hasMore() && (users.size() <= maxFilteredUsers))
        {
          users.add(buildUserFromSearchResult(searchResultsSharedUsers.next(), true));
        }
      }

      return users;
    }
    catch (Throwable e)
    {
      throw new SecurityServiceException(String.format(
          "Failed to retrieve the filtered users for the user directory (%s)",
          getUserDirectoryId()), e);
    }
    finally
    {
      JNDIUtil.close(searchResultsSharedUsers);
      JNDIUtil.close(searchResultsNonSharedUsers);
      JNDIUtil.close(dirContext);
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
    DirContext dirContext = null;
    NamingEnumeration<SearchResult> searchResultsSharedUsers = null;
    NamingEnumeration<SearchResult> searchResultsNonSharedUsers = null;

    try
    {
      dirContext = getDirContext(bindDN, bindPassword);

      String searchFilter = String.format("(&(objectClass=%s)(%s=%s))", userObjectClass,
          userUsernameAttribute, username);

      SearchControls searchControls = new SearchControls();
      searchControls.setSearchScope(SearchControls.SUBTREE_SCOPE);
      searchControls.setReturningObjFlag(false);

      // First search for a non-shared user
      searchResultsNonSharedUsers = dirContext.search(userBaseDN, searchFilter, searchControls);

      if (searchResultsNonSharedUsers.hasMore())
      {
        return true;
      }

      // Next search for a shared user
      if (sharedBaseDN != null)
      {
        searchResultsSharedUsers = dirContext.search(sharedBaseDN, searchFilter, searchControls);

        return searchResultsSharedUsers.hasMore();
      }

      return false;
    }
    catch (Throwable e)
    {
      throw new SecurityServiceException(String.format(
          "Failed to check whether the user (%s) is an existing user for the user directory (%s)",
          username, getUserDirectoryId()), e);
    }
    finally
    {
      JNDIUtil.close(searchResultsSharedUsers);
      JNDIUtil.close(searchResultsNonSharedUsers);
      JNDIUtil.close(dirContext);
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
    DirContext dirContext = null;

    try
    {
      dirContext = getDirContext(bindDN, bindPassword);

      LdapName userDN = getUserDN(dirContext, username);

      if (userDN == null)
      {
        throw new UserNotFoundException(username);
      }

      LdapName groupDN = getGroupDN(dirContext, groupName);

      if (groupDN == null)
      {
        throw new GroupNotFoundException(groupName);
      }

      Attributes attributes = dirContext.getAttributes(groupDN, groupMemberAttributeArray);

      if (attributes.get(groupMemberAttribute) != null)
      {
        NamingEnumeration<?> attributeValues = attributes.get(groupMemberAttribute).getAll();

        while (attributeValues.hasMore())
        {
          LdapName memberDN = new LdapName((String) attributeValues.next());

          if (memberDN.equals(userDN))
          {
            return true;
          }
        }
      }

      return false;
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
    finally
    {
      JNDIUtil.close(dirContext);
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
    DirContext dirContext = null;

    try
    {
      dirContext = getDirContext(bindDN, bindPassword);

      LdapName userDN = getUserDN(dirContext, username);

      if (userDN == null)
      {
        throw new UserNotFoundException(username);
      }

      LdapName groupDN = getGroupDN(dirContext, groupName);

      if (groupDN == null)
      {
        throw new GroupNotFoundException(groupName);
      }

      Attributes attributes = dirContext.getAttributes(groupDN, groupMemberAttributeArray);

      BasicAttribute attribute = new BasicAttribute(groupMemberAttribute);

      if (attributes.get(groupMemberAttribute) != null)
      {
        @SuppressWarnings("unchecked")
        NamingEnumeration<String> groupMembers = (NamingEnumeration<String>) attributes.get(
            groupMemberAttribute).getAll();

        while (groupMembers.hasMore())
        {
          LdapName groupMemberDN = new LdapName(groupMembers.next());

          if (!groupMemberDN.equals(userDN))
          {
            attribute.add(groupMemberDN.toString());
          }
        }
      }

      if (attribute.size() > 0)
      {
        dirContext.modifyAttributes(groupDN, new ModificationItem[] { new ModificationItem(
            DirContext.REPLACE_ATTRIBUTE, attribute) });
      }
      else
      {
        dirContext.modifyAttributes(groupDN, new ModificationItem[] { new ModificationItem(
            DirContext.REMOVE_ATTRIBUTE, attribute) });
      }
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
    finally
    {
      JNDIUtil.close(dirContext);
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
    DirContext dirContext = null;

    try (Connection connection = dataSource.getConnection())
    {
      dirContext = getDirContext(bindDN, bindPassword);

      LdapName groupDN = getGroupDN(dirContext, group.getGroupName());

      if (groupDN == null)
      {
        throw new GroupNotFoundException(group.getGroupName());
      }

      List<ModificationItem> modificationItems = new ArrayList<>();

      if (!StringUtils.isEmpty(groupDescriptionAttribute))
      {
        modificationItems.add(new ModificationItem(DirContext.REPLACE_ATTRIBUTE, new BasicAttribute(
            groupDescriptionAttribute,
            StringUtils.isEmpty(group.getDescription())
            ? ""
            : group.getDescription())));
      }

      if (modificationItems.size() > 0)
      {
        dirContext.modifyAttributes(groupDN, modificationItems.toArray(new ModificationItem[0]));
      }

      // Update the corresponding group in the database
      UUID groupId = getGroupId(connection, group.getGroupName());

      updateGroup(connection, groupId, group.getGroupName(), group.getDescription());
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
    finally
    {
      JNDIUtil.close(dirContext);
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
    DirContext dirContext = null;

    try
    {
      dirContext = getDirContext(bindDN, bindPassword);

      LdapName userDN = getUserDN(dirContext, user.getUsername());

      if (userDN == null)
      {
        throw new UserNotFoundException(user.getUsername());
      }

      List<ModificationItem> modificationItems = new ArrayList<>();

      if (!StringUtils.isEmpty(userFirstNameAttribute))
      {
        modificationItems.add(new ModificationItem(DirContext.REPLACE_ATTRIBUTE, new BasicAttribute(
            userFirstNameAttribute,
            StringUtils.isEmpty(user.getFirstName())
            ? ""
            : user.getFirstName())));
      }

      if (!StringUtils.isEmpty(userLastNameAttribute))
      {
        modificationItems.add(new ModificationItem(DirContext.REPLACE_ATTRIBUTE, new BasicAttribute(
            userLastNameAttribute,
            StringUtils.isEmpty(user.getLastName())
            ? ""
            : user.getLastName())));
      }

      if (!StringUtils.isEmpty(userEmailAttribute))
      {
        modificationItems.add(new ModificationItem(DirContext.REPLACE_ATTRIBUTE, new BasicAttribute(
            userEmailAttribute,
            StringUtils.isEmpty(user.getEmail())
            ? ""
            : user.getEmail())));
      }

      if (!StringUtils.isEmpty(userPhoneNumberAttribute))
      {
        modificationItems.add(new ModificationItem(DirContext.REPLACE_ATTRIBUTE, new BasicAttribute(
            userPhoneNumberAttribute,
            StringUtils.isEmpty(user.getPhoneNumber())
            ? ""
            : user.getPhoneNumber())));
      }

      if (!StringUtils.isEmpty(userMobileNumberAttribute))
      {
        modificationItems.add(new ModificationItem(DirContext.REPLACE_ATTRIBUTE, new BasicAttribute(
            userMobileNumberAttribute,
            StringUtils.isEmpty(user.getMobileNumber())
            ? ""
            : user.getMobileNumber())));
      }

      if ((!StringUtils.isEmpty(userPasswordAttemptsAttribute))
          && (user.getPasswordAttempts() != null))
      {
        if (lockUser)
        {
          modificationItems.add(new ModificationItem(DirContext.REPLACE_ATTRIBUTE,
              new BasicAttribute(userPasswordAttemptsAttribute, String.valueOf(
              maxPasswordAttempts))));
        }
        else
        {
          modificationItems.add(new ModificationItem(DirContext.REPLACE_ATTRIBUTE,
              new BasicAttribute(userPasswordAttemptsAttribute, String.valueOf(
              user.getPasswordAttempts()))));
        }
      }

      if ((!StringUtils.isEmpty(userPasswordExpiryAttribute)) && (user.getPasswordExpiry() != null))
      {
        if (expirePassword)
        {
          modificationItems.add(new ModificationItem(DirContext.REPLACE_ATTRIBUTE,
              new BasicAttribute(userPasswordExpiryAttribute, String.valueOf(
              System.currentTimeMillis()))));
        }
        else
        {
          modificationItems.add(new ModificationItem(DirContext.REPLACE_ATTRIBUTE,
              new BasicAttribute(userPasswordExpiryAttribute, String.valueOf(
              user.getPasswordExpiry().toEpochSecond(ZoneOffset.UTC)))));
        }
      }

      if (modificationItems.size() > 0)
      {
        dirContext.modifyAttributes(userDN, modificationItems.toArray(new ModificationItem[0]));
      }
    }
    catch (UserNotFoundException e)
    {
      throw e;
    }
    catch (Throwable e)
    {
      throw new SecurityServiceException("Failed to update the user (" + user.getUsername()
          + ") for the user directory (" + getUserDirectoryId() + "): " + e.getMessage(), e);
    }
    finally
    {
      JNDIUtil.close(dirContext);
    }
  }

  private Group buildGroupFromSearchResult(SearchResult searchResult)
    throws NamingException
  {
    Attributes attributes = searchResult.getAttributes();

    Group group = new Group(String.valueOf(attributes.get(groupNameAttribute).get()));

    group.setId(null);
    group.setUserDirectoryId(getUserDirectoryId());

    if ((!StringUtils.isEmpty(groupDescriptionAttribute))
        && (attributes.get(groupDescriptionAttribute) != null))
    {
      group.setDescription(String.valueOf(attributes.get(groupDescriptionAttribute).get()));
    }
    else
    {
      group.setDescription("");
    }

    return group;
  }

  private User buildUserFromSearchResult(SearchResult searchResult, boolean readOnly)
    throws NamingException
  {
    Attributes attributes = searchResult.getAttributes();

    User user = new User();

    user.setId(null);
    user.setUsername(String.valueOf(attributes.get(userUsernameAttribute).get()));
    user.setUserDirectoryId(getUserDirectoryId());
    user.setReadOnly(readOnly);
    user.setPassword("");

    if ((!StringUtils.isEmpty(userFirstNameAttribute))
        && (attributes.get(userFirstNameAttribute) != null))
    {
      user.setFirstName(String.valueOf(attributes.get(userFirstNameAttribute).get()));
    }
    else
    {
      user.setFirstName("");
    }

    if ((!StringUtils.isEmpty(userLastNameAttribute))
        && (attributes.get(userLastNameAttribute) != null))
    {
      user.setLastName(String.valueOf(attributes.get(userLastNameAttribute).get()));
    }
    else
    {
      user.setLastName("");
    }

    if ((!StringUtils.isEmpty(userPhoneNumberAttribute))
        && (attributes.get(userPhoneNumberAttribute) != null))
    {
      user.setPhoneNumber(String.valueOf(attributes.get(userPhoneNumberAttribute).get()));
    }
    else
    {
      user.setPhoneNumber("");
    }

    if ((!StringUtils.isEmpty(userMobileNumberAttribute))
        && (attributes.get(userMobileNumberAttribute) != null))
    {
      user.setMobileNumber(String.valueOf(attributes.get(userMobileNumberAttribute).get()));
    }
    else
    {
      user.setMobileNumber("");
    }

    if ((!StringUtils.isEmpty(userEmailAttribute)) && (attributes.get(userEmailAttribute) != null))
    {
      user.setEmail(String.valueOf(attributes.get(userEmailAttribute).get()));
    }
    else
    {
      user.setEmail("");
    }

    if ((!StringUtils.isEmpty(userPasswordAttemptsAttribute))
        && (attributes.get(userPasswordAttemptsAttribute) != null))
    {
      String userPasswordAttemptsAttributeValue = String.valueOf(attributes.get(
          userPasswordAttemptsAttribute).get());

      if ((!StringUtils.isEmpty(userPasswordAttemptsAttributeValue))
          && (!userPasswordAttemptsAttributeValue.equals("-1")))
      {
        user.setPasswordAttempts(Integer.parseInt(String.valueOf(attributes.get(
            userPasswordAttemptsAttribute).get())));
      }
    }

    if ((!StringUtils.isEmpty(userPasswordExpiryAttribute))
        && (attributes.get(userPasswordExpiryAttribute) != null))
    {
      String userPasswordExpiryAttributeValue = String.valueOf(attributes.get(
          userPasswordExpiryAttribute).get());

      if ((!StringUtils.isEmpty(userPasswordExpiryAttributeValue))
          && (!userPasswordExpiryAttributeValue.equals("-1")))
      {
        LocalDateTime epochSecond = LocalDateTime.ofEpochSecond(Long.parseLong(String.valueOf(
            attributes.get(userPasswordExpiryAttribute).get())), 0, ZoneOffset.UTC);

        user.setPasswordExpiry(epochSecond);
      }
    }

    user.setExternalReference(new LdapName(searchResult.getNameInNamespace()
        .toLowerCase()).toString());

    return user;
  }

  private DirContext getDirContext(String userDN, String password)
    throws SecurityServiceException
  {
    try
    {
      String url = useSSL
          ? "ldaps://"
          : "ldap://";
      url += host;
      url += ":";
      url += port;

      String connectionType = "simple";

      Hashtable<String, String> environment = new Hashtable<>();

      environment.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
      environment.put(Context.PROVIDER_URL, url);
      environment.put(Context.SECURITY_AUTHENTICATION, connectionType);
      environment.put(Context.SECURITY_PRINCIPAL, userDN);
      environment.put(Context.SECURITY_CREDENTIALS, password);

      return new InitialDirContext(environment);
    }
    catch (Throwable e)
    {
      throw new SecurityServiceException(String.format(
          "Failed to retrieve the JNDI directory context for the user directory (%s)",
          getUserDirectoryId()), e);
    }
  }

  private LdapName getGroupDN(DirContext dirContext, String groupName)
    throws SecurityServiceException
  {
    NamingEnumeration<SearchResult> searchResults = null;

    try
    {
      List<LdapName> groupDNs = new ArrayList<>();

      String searchFilter = String.format("(&(objectClass=%s)(%s=%s))", groupObjectClass,
          groupNameAttribute, groupName);

      SearchControls searchControls = new SearchControls();
      searchControls.setSearchScope(SearchControls.SUBTREE_SCOPE);
      searchControls.setReturningObjFlag(false);
      searchControls.setReturningAttributes(EMPTY_ATTRIBUTE_LIST);

      searchResults = dirContext.search(groupBaseDN, searchFilter, searchControls);

      while (searchResults.hasMore())
      {
        groupDNs.add(new LdapName(searchResults.next().getNameInNamespace().toLowerCase()));
      }

      if (groupDNs.size() == 0)
      {
        return null;
      }
      else if (groupDNs.size() == 1)
      {
        return groupDNs.get(0);
      }
      else
      {
        StringBuilder buffer = new StringBuilder();

        for (LdapName groupDN : groupDNs)
        {
          if (buffer.length() > 0)
          {
            buffer.append(" ");
          }

          buffer.append("(").append(groupDN).append(")");
        }

        throw new SecurityServiceException(String.format(
            "Found multiple security groups (%d) with the security group name (%s) with DNs %s",
            groupDNs.size(), groupName, buffer.toString()));
      }
    }
    catch (Throwable e)
    {
      throw new SecurityServiceException(String.format(
          "Failed to retrieve the DN for the security group (%s) from the LDAP directory (%s:%d)",
          groupName, host, port), e);
    }
    finally
    {
      JNDIUtil.close(searchResults);
    }
  }

  private User getUser(DirContext dirContext, String username)
    throws SecurityServiceException
  {
    NamingEnumeration<SearchResult> searchResultsNonSharedUsers = null;
    NamingEnumeration<SearchResult> searchResultsSharedUsers = null;

    try
    {
      List<User> users = new ArrayList<>();

      String searchFilter = String.format("(&(objectClass=%s)(%s=%s))", userObjectClass,
          userUsernameAttribute, username);

      SearchControls searchControls = new SearchControls();
      searchControls.setSearchScope(SearchControls.SUBTREE_SCOPE);
      searchControls.setReturningObjFlag(false);

      // First search for a non-shared user
      searchResultsNonSharedUsers = dirContext.search(userBaseDN, searchFilter, searchControls);

      while (searchResultsNonSharedUsers.hasMore())
      {
        users.add(buildUserFromSearchResult(searchResultsNonSharedUsers.next(), false));
      }

      // Next search for a shared user
      if (sharedBaseDN != null)
      {
        searchResultsSharedUsers = dirContext.search(sharedBaseDN, searchFilter, searchControls);

        while (searchResultsSharedUsers.hasMore())
        {
          users.add(buildUserFromSearchResult(searchResultsSharedUsers.next(), true));
        }
      }

      if (users.size() == 0)
      {
        return null;
      }
      else if (users.size() == 1)
      {
        return users.get(0);
      }
      else
      {
        StringBuilder buffer = new StringBuilder();

        for (User user : users)
        {
          if (buffer.length() > 0)
          {
            buffer.append(" ");
          }

          buffer.append("(").append(user.getExternalReference()).append(")");
        }

        throw new SecurityServiceException(String.format(
            "Found multiple users (%d) with the username (%s) with DNs %s", users.size(), username,
            buffer.toString()));
      }
    }
    catch (Throwable e)
    {
      throw new SecurityServiceException(String.format(
          "Failed to retrieve the details for the user (%s) from the LDAP directory (%s:%d)",
          username, host, port), e);
    }
    finally
    {
      JNDIUtil.close(searchResultsSharedUsers);
      JNDIUtil.close(searchResultsNonSharedUsers);
    }
  }

  private LdapName getUserDN(DirContext dirContext, String username)
    throws SecurityServiceException
  {
    NamingEnumeration<SearchResult> searchResultsNonSharedUsers = null;
    NamingEnumeration<SearchResult> searchResultsSharedUsers = null;

    try
    {
      List<LdapName> userDNs = new ArrayList<>();

      String searchFilter = String.format("(&(objectClass=%s)(%s=%s))", userObjectClass,
          userUsernameAttribute, username);

      SearchControls searchControls = new SearchControls();
      searchControls.setSearchScope(SearchControls.SUBTREE_SCOPE);
      searchControls.setReturningObjFlag(false);
      searchControls.setReturningAttributes(EMPTY_ATTRIBUTE_LIST);

      // First search for a non-shared user
      searchResultsNonSharedUsers = dirContext.search(userBaseDN, searchFilter, searchControls);

      while (searchResultsNonSharedUsers.hasMore())
      {
        userDNs.add(new LdapName(searchResultsNonSharedUsers.next().getNameInNamespace()
            .toLowerCase()));
      }

      // Next search for a shared user
      if (sharedBaseDN != null)
      {
        searchResultsSharedUsers = dirContext.search(sharedBaseDN, searchFilter, searchControls);

        while (searchResultsSharedUsers.hasMore())
        {
          userDNs.add(new LdapName(searchResultsSharedUsers.next().getNameInNamespace()
              .toLowerCase()));
        }
      }

      if (userDNs.size() == 0)
      {
        return null;
      }
      else if (userDNs.size() == 1)
      {
        return userDNs.get(0);
      }
      else
      {
        StringBuilder buffer = new StringBuilder();

        for (LdapName userDN : userDNs)
        {
          if (buffer.length() > 0)
          {
            buffer.append(" ");
          }

          buffer.append("(").append(userDN).append(")");
        }

        throw new SecurityServiceException(String.format(
            "Found multiple users (%d) with the username (%s) with DNs %s", userDNs.size(),
            username, buffer.toString()));
      }
    }
    catch (Throwable e)
    {
      throw new SecurityServiceException(String.format(
          "Failed to retrieve the DN for the user (%s) from the LDAP directory (%s:%d)", username,
          host, port), e);
    }
    finally
    {
      JNDIUtil.close(searchResultsSharedUsers);
      JNDIUtil.close(searchResultsNonSharedUsers);
    }
  }

  private void incrementPasswordAttempts(DirContext dirContext, User user)
  {
    try
    {
      if ((!StringUtils.isEmpty(userPasswordAttemptsAttribute))
          && (user.getPasswordAttempts() != null)
          && (user.getPasswordAttempts() != -1))
      {
        dirContext.modifyAttributes(user.getExternalReference(), new ModificationItem[] {
            new ModificationItem(DirContext.REPLACE_ATTRIBUTE, new BasicAttribute(
            userPasswordAttemptsAttribute, String.valueOf(user.getPasswordAttempts() + 1))) });
      }
    }
    catch (Throwable e)
    {
      logger.error(String.format(
          "Failed to increment the password attempts for the user (%s) for the user directory (%s)",
          user.getUsername(), getUserDirectoryId()), e);
    }
  }

  private boolean isPasswordInHistory(DirContext dirContext, String username, LdapName userDN,
      String passwordHash)
    throws SecurityServiceException
  {
    try
    {
      if (!StringUtils.isEmpty(userPasswordHistoryAttribute))
      {
        Attributes attributes = dirContext.getAttributes(userDN, userPasswordHistoryAttributeArray);

        if (attributes.get(userPasswordHistoryAttribute) != null)
        {
          @SuppressWarnings("unchecked")
          NamingEnumeration<String> existingPasswordHashes =
              (NamingEnumeration<String>) attributes.get(userPasswordHistoryAttribute).getAll();

          while (existingPasswordHashes.hasMore())
          {
            if (existingPasswordHashes.next().equals(passwordHash))
            {
              return true;
            }
          }
        }
      }
    }
    catch (Throwable e)
    {
      throw new SecurityServiceException(String.format(
          "Failed to check whether the password hash (%s) is in the password history for the user "
          + "(%s) for the user directory (%s)", passwordHash, username, getUserDirectoryId()), e);
    }

    return false;
  }

  private void savePasswordHistory(DirContext dirContext, String username, LdapName userDN,
      String passwordHash)
    throws SecurityServiceException
  {
    try
    {
      if (!StringUtils.isEmpty(userPasswordHistoryAttribute))
      {
        BasicAttribute attribute = new BasicAttribute(userPasswordHistoryAttribute);

        Attributes attributes = dirContext.getAttributes(userDN, userPasswordHistoryAttributeArray);

        if (attributes.get(userPasswordHistoryAttribute) != null)
        {
          @SuppressWarnings("unchecked")
          NamingEnumeration<String> existingPasswordHashes =
              (NamingEnumeration<String>) attributes.get(userPasswordHistoryAttribute).getAll();

          while (existingPasswordHashes.hasMore())
          {
            attribute.add(existingPasswordHashes.next());
          }
        }

        attribute.add(passwordHash);

        dirContext.modifyAttributes(userDN, new ModificationItem[] { new ModificationItem(DirContext
            .REPLACE_ATTRIBUTE, attribute) });
      }
    }
    catch (Throwable e)
    {
      throw new SecurityServiceException(String.format(
          "Failed to save the password hash (%s) for the user (%s) for the user directory (%s)",
          passwordHash, username, getUserDirectoryId()), e);
    }
  }
}
