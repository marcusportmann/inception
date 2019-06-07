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

import digital.inception.core.util.Base64Util;

//~--- JDK imports ------------------------------------------------------------

import java.security.MessageDigest;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * The <code>UserDirectoryBase</code> class provides the base class from which all user directory
 * classes should be derived.
 *
 * @author Marcus Portmann
 */
@SuppressWarnings({ "unused", "WeakerAccess" })
public abstract class UserDirectoryBase
  implements IUserDirectory
{
  /**
   * The configuration parameters for the user directory.
   */
  private List<UserDirectoryParameter> parameters;

  /**
   * The Universally Unique Identifier (UUID) used to uniquely identify the user directory.
   */
  private UUID userDirectoryId;

  /**
   * Constructs a new <code>UserDirectoryBase</code>.
   *
   * @param userDirectoryId the Universally Unique Identifier (UUID) used to uniquely identify the
   *                        user directory
   * @param parameters      the configuration parameters for the user directory
   */
  public UserDirectoryBase(UUID userDirectoryId, List<UserDirectoryParameter> parameters)
  {
    this.userDirectoryId = userDirectoryId;
    this.parameters = parameters;
  }

  /**
   * Returns the configuration parameters for the user directory.
   *
   * @return the configuration parameters for the user directory
   */
  public List<UserDirectoryParameter> getParameters()
  {
    return parameters;
  }

  /**
   * Returns the Universally Unique Identifier (UUID) used to uniquely identify the user directory.
   *
   * @return the Universally Unique Identifier (UUID) used to uniquely identify the user directory
   */
  public UUID getUserDirectoryId()
  {
    return userDirectoryId;
  }

  /**
   * Create the new security group.
   * <p/>
   * If a security group with the specified group name already exists the ID for this existing
   * security group will be returned.
   *
   * @param connection the existing database connection
   * @param groupId    the Universally Unique Identifier (UUID) used to uniquely identify the
   *                   security group
   * @param groupName  the group name uniquely identifying the security group
   *
   * @return the Universally Unique Identifier (UUID) used to uniquely identify the security group
   */
  protected UUID createGroup(Connection connection, UUID groupId, String groupName)
    throws SecurityServiceException
  {
    String createGroupSQL =
        "INSERT INTO security.groups (id, user_directory_id, groupname) VALUES (?, ?, ?)";

    try (PreparedStatement statement = connection.prepareStatement(createGroupSQL))
    {
      UUID existingGroupId = getGroupId(connection, groupName);

      if (existingGroupId != null)
      {
        return existingGroupId;
      }

      statement.setObject(1, groupId);
      statement.setObject(2, getUserDirectoryId());
      statement.setString(3, groupName);

      if (statement.executeUpdate() != 1)
      {
        throw new SecurityServiceException(String.format(
            "No rows were affected as a result of executing the SQL statement (%s)",
            createGroupSQL));
      }

      return groupId;
    }
    catch (Throwable e)
    {
      throw new SecurityServiceException(String.format(
          "Failed to create the security group (%s) with the ID (%s) for the user directory (%s): %s",
          groupName, groupId, getUserDirectoryId(), e.getMessage()), e);
    }
  }

  /**
   * Create the SHA-256 hash of the specified password.
   *
   * @param password the password to hash
   *
   * @return the SHA-256 hash of the password
   */
  protected String createPasswordHash(String password)
    throws SecurityServiceException
  {
    try
    {
      MessageDigest md = MessageDigest.getInstance("SHA-256");

      md.update(password.getBytes("iso-8859-1"), 0, password.length());

      return Base64Util.encodeBytes(md.digest());
    }
    catch (Throwable e)
    {
      throw new SecurityServiceException(String.format(
          "Failed to generate a SHA-256 hash of the password (%s): %s", password, e.getMessage()),
          e);
    }
  }

  /**
   * Delete the security group.
   *
   * @param connection the existing database connection to use
   * @param groupName  the group name uniquely identifying the security group
   *
   * @return the Universally Unique Identifier (UUID) used to uniquely identify the security group
   *         or <code>null</code> if a security group with the specified group name could not be
   *         found
   */
  protected UUID deleteGroup(Connection connection, String groupName)
    throws SecurityServiceException
  {
    String deleteGroupSQL = "DELETE FROM security.groups WHERE user_directory_id=? "
        + "AND UPPER(groupname)=UPPER(CAST(? AS VARCHAR(100)))";

    try (PreparedStatement statement = connection.prepareStatement(deleteGroupSQL))
    {
      UUID groupId = getGroupId(connection, groupName);

      if (groupId != null)
      {
        statement.setObject(1, getUserDirectoryId());
        statement.setString(2, groupName);

        if (statement.executeUpdate() <= 0)
        {
          throw new SecurityServiceException(String.format(
              "No rows were affected as a result of executing the SQL statement (%s)",
              deleteGroupSQL));
        }
      }

      return groupId;
    }
    catch (Throwable e)
    {
      throw new SecurityServiceException(String.format(
          "Failed to delete the security group (%s) for the user directory (%s): %s", groupName,
          getUserDirectoryId(), e.getMessage()), e);
    }
  }

  /**
   * Checks whether the specified value is <code>null</code> or blank.
   *
   * @param value the value to check
   *
   * @return true if the value is <code>null</code> or blank
   */
  protected boolean isNullOrEmpty(Object value)
  {
    if (value == null)
    {
      return true;
    }

    if (value instanceof String)
    {
      if (String.class.cast(value).length() == 0)
      {
        return true;
      }
    }

    return false;
  }

  /**
   * Returns the ID for the security group with the specified group name.
   *
   * @param connection the existing database connection to use
   * @param groupName  the group name uniquely identifying the security group
   *
   * @return the Universally Unique Identifier (UUID) used to uniquely identify the security group
   *         or <code>null</code> if a security group with the specified group name could not be
   *         found
   */
  private UUID getGroupId(Connection connection, String groupName)
    throws SecurityServiceException
  {
    String getGroupIdSQL = "SELECT id FROM security.groups WHERE user_directory_id=? AND "
        + "UPPER(groupname)=UPPER(CAST(? AS VARCHAR(100)))";

    try (PreparedStatement statement = connection.prepareStatement(getGroupIdSQL))
    {
      statement.setObject(1, getUserDirectoryId());
      statement.setString(2, groupName);

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
          "Failed to retrieve the ID for the security group (%s) for the user directory (%s)",
          groupName, getUserDirectoryId()), e);
    }
  }
}
