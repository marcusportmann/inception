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

import org.springframework.util.StringUtils;

//~--- JDK imports ------------------------------------------------------------

import java.nio.charset.StandardCharsets;

import java.security.MessageDigest;

import java.sql.*;

import java.util.ArrayList;
import java.util.List;
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
   * The parameters for the user directory.
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
   * @param parameters      the parameters for the user directory
   */
  public UserDirectoryBase(UUID userDirectoryId, List<UserDirectoryParameter> parameters)
  {
    this.userDirectoryId = userDirectoryId;
    this.parameters = parameters;
  }

  /**
   * Returns the parameters for the user directory.
   *
   * @return the parameters for the user directory
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
   * @param connection       the existing database connection
   * @param groupId          the Universally Unique Identifier (UUID) used to uniquely identify the
   *                         security group
   * @param groupName        the group name uniquely identifying the security group
   * @param groupDescription a description for the group
   *
   * @return the Universally Unique Identifier (UUID) used to uniquely identify the security group
   */
  protected UUID createGroup(Connection connection, UUID groupId, String groupName,
      String groupDescription)
    throws SQLException
  {
    String createGroupSQL =
        "INSERT INTO security.groups (id, user_directory_id, groupname, description) VALUES (?, ?, ?, ?)";

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

      if (StringUtils.isEmpty(groupDescription))
      {
        statement.setNull(4, Types.VARCHAR);
      }
      else
      {
        statement.setString(4, groupDescription);
      }

      if (statement.executeUpdate() != 1)
      {
        throw new SQLException(String.format(
            "No rows were affected as a result of executing the SQL statement (%s)",
            createGroupSQL));
      }

      return groupId;
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

      md.update(password.getBytes(StandardCharsets.ISO_8859_1), 0, password.length());

      return Base64Util.encodeBytes(md.digest());
    }
    catch (Throwable e)
    {
      throw new SecurityServiceException(String.format(
          "Failed to generate a SHA-256 hash of the password (%s)", password), e);
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
    throws SQLException
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
          throw new SQLException(String.format(
              "No rows were affected as a result of executing the SQL statement (%s)",
              deleteGroupSQL));
        }
      }

      return groupId;
    }
  }

  /**
   * Retrieve the security group.
   *
   * @param connection the existing database connection
   * @param groupName  the name of the security group uniquely identifying the security group
   *
   * @return the group
   */
  protected Group getGroup(Connection connection, String groupName)
    throws GroupNotFoundException, SQLException
  {
    String getGroupSQL = "SELECT id, groupname, description FROM security.groups "
        + "WHERE user_directory_id=? AND UPPER(groupname)=UPPER(CAST(? AS VARCHAR(100)))";

    try (PreparedStatement statement = connection.prepareStatement(getGroupSQL))
    {
      statement.setObject(1, getUserDirectoryId());
      statement.setString(2, groupName);

      try (ResultSet rs = statement.executeQuery())
      {
        if (rs.next())
        {
          return buildGroupFromResultSet(rs);
        }
        else
        {
          throw new GroupNotFoundException(groupName);
        }
      }
    }
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
  protected UUID getGroupId(Connection connection, String groupName)
    throws SQLException
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
  }

  /**
   * Retrieve all the security groups.
   *
   * @param connection the existing database connection
   *
   * @return the security groups
   */
  protected List<Group> getGroups(Connection connection)
    throws SecurityServiceException
  {
    String getGroupsSQL = "SELECT id, groupname, description FROM "
        + "security.groups WHERE user_directory_id=? ORDER BY groupname";

    try (PreparedStatement statement = connection.prepareStatement(getGroupsSQL))
    {
      statement.setObject(1, getUserDirectoryId());

      try (ResultSet rs = statement.executeQuery())
      {
        List<Group> list = new ArrayList<>();

        while (rs.next())
        {
          list.add(buildGroupFromResultSet(rs));
        }

        return list;
      }
    }
    catch (Throwable e)
    {
      throw new SecurityServiceException(String.format(
          "Failed to retrieve the security groups for the user directory (%s)",
          getUserDirectoryId()), e);
    }
  }

  /**
   * Retrieve the number of security groups.
   *
   * @param connection the existing database connection to use
   *
   * @return the number of security groups
   */
  protected int getNumberOfGroups(Connection connection)
    throws SQLException
  {
    String getNumberOfGroupsSQL = "SELECT COUNT(id) FROM security.groups WHERE user_directory_id=?";

    try (PreparedStatement statement = connection.prepareStatement(getNumberOfGroupsSQL))
    {
      statement.setObject(1, getUserDirectoryId());

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
      return ((String) value).length() == 0;
    }

    return false;
  }

  /**
   * Update the security group.
   *
   * @param connection       the existing database connection
   * @param groupId          the Universally Unique Identifier (UUID) used to uniquely identify the
   *                         security group
   * @param groupName        the group name uniquely identifying the security group
   * @param groupDescription a description for the group
   */
  protected void updateGroup(Connection connection, UUID groupId, String groupName,
      String groupDescription)
    throws SQLException
  {
    String updateGroupSQL =
        "UPDATE security.groups SET description=? WHERE user_directory_id=? AND id=?";

    try (PreparedStatement statement = connection.prepareStatement(updateGroupSQL))
    {
      statement.setString(1,
          StringUtils.isEmpty(groupDescription)
          ? ""
          : groupDescription);
      statement.setObject(2, getUserDirectoryId());
      statement.setObject(3, groupId);

      if (statement.executeUpdate() <= 0)
      {
        throw new SQLException(String.format(
            "No rows were affected as a result of executing the SQL statement (%s)",
            updateGroupSQL));
      }
    }
  }

  /**
   * Create a new <code>Group</code> instance and populate it with the contents of the current
   * row in the specified <code>ResultSet</code>.
   *
   * @param rs the <code>ResultSet</code> whose current row will be used to populate the
   *           <code>Group</code> instance
   *
   * @return the populated <code>Group</code> instance
   */
  private Group buildGroupFromResultSet(ResultSet rs)
    throws SQLException
  {
    Group group = new Group(rs.getString(2));

    group.setId(UUID.fromString(rs.getString(1)));
    group.setUserDirectoryId(getUserDirectoryId());

    String description = rs.getString(3);

    group.setDescription(StringUtils.isEmpty(description)
        ? ""
        : description);

    return group;
  }
}
