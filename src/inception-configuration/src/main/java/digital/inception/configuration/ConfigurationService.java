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

package digital.inception.configuration;

//~--- non-JDK imports --------------------------------------------------------

import digital.inception.core.util.Base64Util;
import digital.inception.core.util.StringUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

//~--- JDK imports ------------------------------------------------------------

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

/**
 * The <code>ConfigurationService</code> class provides the Configuration Service implementation.
 *
 * @author Marcus Portmann
 */
@Service
@SuppressWarnings("unused")
public class ConfigurationService
  implements IConfigurationService
{
  /**
   * The data source used to provide connections to the application database.
   */
  @Autowired
  @Qualifier("applicationDataSource")
  private DataSource dataSource;

  /**
   * Remove the configuration with the specified key.
   *
   * @param key the key used to uniquely identify the configuration
   */
  @Override
  public void deleteConfiguration(String key)
    throws ConfigurationNotFoundException, ConfigurationServiceException
  {
    String deleteConfigurationSQL =
        "DELETE FROM configuration.configuration WHERE (UPPER(key) LIKE ?)";

    try (Connection connection = dataSource.getConnection();
      PreparedStatement statement = connection.prepareStatement(deleteConfigurationSQL))
    {
      statement.setString(1, key.toUpperCase());

      if (statement.executeUpdate() <= 0)
      {
        throw new ConfigurationNotFoundException(key);
      }
    }
    catch (ConfigurationNotFoundException e)
    {
      throw e;
    }
    catch (Throwable e)
    {
      throw new ConfigurationServiceException(String.format(
          "Failed to delete the configuration with the key (%s)", key), e);
    }
  }

  /**
   * Retrieve the binary configuration.
   *
   * @param key the key used to uniquely identify the configuration
   *
   * @return the binary configuration
   */
  @Override
  public byte[] getBinary(String key)
    throws ConfigurationNotFoundException, ConfigurationServiceException
  {
    try
    {
      String stringValue = getString(key);

      if (stringValue != null)
      {
        return Base64Util.decode(stringValue);
      }
      else
      {
        throw new ConfigurationNotFoundException(key);
      }
    }
    catch (ConfigurationNotFoundException e)
    {
      throw e;
    }
    catch (Throwable e)
    {
      throw new ConfigurationServiceException(String.format(
          "Failed to retrieve the binary configuration with the key (%s)", key), e);
    }
  }

  /**
   * Retrieve the binary configuration.
   *
   * @param key          the key used to uniquely identify the configuration
   * @param defaultValue the default value to return if the configuration does not exist
   *
   * @return the binary configuration or the default value if the configuration does
   *         not exist
   */
  @Override
  public byte[] getBinary(String key, byte[] defaultValue)
    throws ConfigurationServiceException
  {
    try
    {
      String stringValue = getString(key);

      if (stringValue != null)
      {
        return Base64Util.decode(stringValue);
      }
      else
      {
        return defaultValue;
      }
    }
    catch (Throwable e)
    {
      throw new ConfigurationServiceException(String.format(
          "Failed to retrieve the binary configuration with the key (%s)", key), e);
    }
  }

  /**
   * Retrieve the <code>Boolean</code> configuration.
   *
   * @param key the key used to uniquely identify the configuration
   *
   * @return the <code>Boolean</code> configuration
   */
  @Override
  public boolean getBoolean(String key)
    throws ConfigurationNotFoundException, ConfigurationServiceException
  {
    try
    {
      String stringValue = getString(key);

      if (stringValue != null)
      {
        return Boolean.parseBoolean(stringValue);
      }
      else
      {
        throw new ConfigurationNotFoundException(key);
      }
    }
    catch (ConfigurationNotFoundException e)
    {
      throw e;
    }
    catch (Throwable e)
    {
      throw new ConfigurationServiceException(String.format(
          "Failed to retrieve the Boolean configuration with the key (%s)", key), e);
    }
  }

  /**
   * Retrieve the <code>Boolean</code> configuration.
   *
   * @param key          the key used to uniquely identify the configuration
   * @param defaultValue the default value to return if the configuration does not exist
   *
   * @return the <code>Boolean</code> configuration or the default value if the configuration
   *         value does not exist
   */
  @Override
  public boolean getBoolean(String key, boolean defaultValue)
    throws ConfigurationServiceException
  {
    try
    {
      String stringValue = getString(key);

      if (stringValue != null)
      {
        return Boolean.parseBoolean(stringValue);
      }
      else
      {
        return defaultValue;
      }
    }
    catch (Throwable e)
    {
      throw new ConfigurationServiceException(String.format(
          "Failed to retrieve the Boolean configuration with the key (%s)", key), e);
    }
  }

  /**
   * Retrieve the configuration.
   *
   * @param key the key used to uniquely identify the configuration
   *
   * @return the configuration
   */
  @Override
  public Configuration getConfiguration(String key)
    throws ConfigurationNotFoundException, ConfigurationServiceException
  {
    String getValueSQL =
        "SELECT key, value, description FROM configuration.configuration WHERE (UPPER(key) = ?)";

    try (Connection connection = dataSource.getConnection();
      PreparedStatement statement = connection.prepareStatement(getValueSQL))
    {
      statement.setString(1, key.toUpperCase());

      try (ResultSet rs = statement.executeQuery())
      {
        if (rs.next())
        {
          return new Configuration(rs.getString(1), rs.getString(2), rs.getString(3));
        }
        else
        {
          throw new ConfigurationNotFoundException(key);
        }
      }
    }
    catch (ConfigurationNotFoundException e)
    {
      throw e;
    }
    catch (Throwable e)
    {
      throw new ConfigurationServiceException(String.format(
          "Failed to retrieve the configuration with the key (%s)", key), e);
    }
  }

  /**
   * Retrieve all the configurations.
   *
   * @return all the configurations
   */
  @Override
  public List<Configuration> getConfigurations()
    throws ConfigurationServiceException
  {
    String getValueSQL =
        "SELECT key, value, description FROM configuration.configuration ORDER BY key";

    try (Connection connection = dataSource.getConnection();
      PreparedStatement statement = connection.prepareStatement(getValueSQL))
    {
      return getConfigurations(statement);
    }
    catch (Throwable e)
    {
      throw new ConfigurationServiceException("Failed to retrieve the configurations", e);
    }
  }

  /**
   * Retrieve the <code>Double</code> configuration.
   *
   * @param key the key used to uniquely identify the configuration
   *
   * @return the <code>Double</code> configuration
   */
  @Override
  public Double getDouble(String key)
    throws ConfigurationNotFoundException, ConfigurationServiceException
  {
    try
    {
      String stringValue = getString(key);

      if (stringValue != null)
      {
        return Double.parseDouble(stringValue);
      }
      else
      {
        throw new ConfigurationNotFoundException(key);
      }
    }
    catch (ConfigurationNotFoundException e)
    {
      throw e;
    }
    catch (Throwable e)
    {
      throw new ConfigurationServiceException(String.format(
          "Failed to retrieve the Double configuration with the key (%s)", key), e);
    }
  }

  /**
   * Retrieve the <code>Double</code> configuration.
   *
   * @param key          the key used to uniquely identify the configuration
   * @param defaultValue the default value to return if the configuration does not exist
   *
   * @return the <code>Double</code> configuration or the default value if the configuration
   *         entry does not exist
   */
  @Override
  public double getDouble(String key, double defaultValue)
    throws ConfigurationServiceException
  {
    try
    {
      String stringValue = getString(key);

      if (stringValue != null)
      {
        return Double.parseDouble(stringValue);
      }
      else
      {
        return defaultValue;
      }
    }
    catch (Throwable e)
    {
      throw new ConfigurationServiceException(String.format(
          "Failed to retrieve the Double configuration with the key (%s)", key), e);
    }
  }

  /**
   * Retrieve the filtered configurations.
   *
   * @param filter the filter to apply to the keys for the configuration
   *
   * @return the filtered configurations
   */
  @Override
  public List<Configuration> getFilteredConfigurations(String filter)
    throws ConfigurationServiceException
  {
    String getConfigValuesSQL = "SELECT key, value, description FROM "
        + "configuration.configuration ORDER BY key";

    String getFilteredConfigValuesSQL = "SELECT key, value, description FROM "
        + "configuration.configuration WHERE (UPPER(key) LIKE ?) ORDER BY key";

    try (Connection connection = dataSource.getConnection();
      PreparedStatement statement = connection.prepareStatement(StringUtil.isNullOrEmpty(filter)
          ? getConfigValuesSQL
          : getFilteredConfigValuesSQL))
    {
      if (!StringUtil.isNullOrEmpty(filter))
      {
        statement.setString(1, "%" + filter.toUpperCase() + "%");
      }

      return getConfigurations(statement);
    }
    catch (Throwable e)
    {
      throw new ConfigurationServiceException(String.format(
          "Failed to retrieve the configuration matching the filter (%s)", filter), e);
    }
  }

  /**
   * Retrieve the <code>Integer</code> configuration.
   *
   * @param key the key used to uniquely identify the configuration
   *
   * @return the <code>Integer</code> configuration
   */
  @Override
  public Integer getInteger(String key)
    throws ConfigurationNotFoundException, ConfigurationServiceException
  {
    try
    {
      String stringValue = getString(key);

      if (stringValue != null)
      {
        return Integer.parseInt(stringValue);
      }
      else
      {
        throw new ConfigurationNotFoundException(key);
      }
    }
    catch (ConfigurationNotFoundException e)
    {
      throw e;
    }
    catch (Throwable e)
    {
      throw new ConfigurationServiceException(String.format(
          "Failed to retrieve the Integer configuration with the key (%s)", key), e);
    }
  }

  /**
   * Retrieve the <code>Integer</code> configuration.
   *
   * @param key          the key used to uniquely identify the configuration
   * @param defaultValue the default value to return if the configuration does not exist
   *
   * @return the <code>Integer</code> configuration or the default value if the configuration
   *         entry does not exist
   */
  @Override
  public int getInteger(String key, int defaultValue)
    throws ConfigurationServiceException
  {
    try
    {
      String stringValue = getString(key);

      if (stringValue != null)
      {
        return Integer.parseInt(stringValue);
      }
      else
      {
        return defaultValue;
      }
    }
    catch (Throwable e)
    {
      throw new ConfigurationServiceException(String.format(
          "Failed to retrieve the Integer configuration with the key (%s)", key), e);
    }
  }

  /**
   * Retrieve the <code>Long</code> configuration.
   *
   * @param key the key used to uniquely identify the configuration
   *
   * @return the <code>Long</code> configuration
   */
  @Override
  public Long getLong(String key)
    throws ConfigurationNotFoundException, ConfigurationServiceException
  {
    try
    {
      String stringValue = getString(key);

      if (stringValue != null)
      {
        return Long.parseLong(stringValue);
      }
      else
      {
        throw new ConfigurationNotFoundException(key);
      }
    }
    catch (ConfigurationNotFoundException e)
    {
      throw e;
    }
    catch (Throwable e)
    {
      throw new ConfigurationServiceException(String.format(
          "Failed to retrieve the Long configuration with the key (%s)", key), e);
    }
  }

  /**
   * Retrieve the <code>Long</code> configuration.
   *
   * @param key          the key used to uniquely identify the configuration
   * @param defaultValue the default value to return if the configuration does not exist
   *
   * @return the <code>Long</code> configuration or the default value if the configuration
   *         entry does not exist
   */
  @Override
  public long getLong(String key, long defaultValue)
    throws ConfigurationServiceException
  {
    try
    {
      String stringValue = getString(key);

      if (stringValue != null)
      {
        return Long.parseLong(stringValue);
      }
      else
      {
        return defaultValue;
      }
    }
    catch (Throwable e)
    {
      throw new ConfigurationServiceException(String.format(
          "Failed to retrieve the Long configuration with the key (%s)", key), e);
    }
  }

  /**
   * Retrieve the numbered of configurations.
   *
   * @return the number of configurations
   */
  @Override
  public int getNumberOfConfigurations()
    throws ConfigurationServiceException
  {
    String getNumberOfConfigurationsSQL = "SELECT COUNT(key) FROM configuration.configuration";

    try (Connection connection = dataSource.getConnection();
      PreparedStatement statement = connection.prepareStatement(getNumberOfConfigurationsSQL))
    {
      try (ResultSet rs = statement.executeQuery())
      {
        if (rs.next())
        {
          return rs.getInt(1);
        }
        else
        {
          throw new ConfigurationServiceException(String.format(
              "No rows were returned as a result of executing the SQL statement (%s)",
              getNumberOfConfigurationsSQL));
        }
      }
    }
    catch (Throwable e)
    {
      throw new ConfigurationServiceException("Failed to retrieve the number of configurations", e);
    }
  }

  /**
   * Retrieve the numbered of filtered configurations.
   *
   * @param filter the filter to apply to the keys for the configuration
   *
   * @return the number of filtered configurations
   */
  @Override
  public int getNumberOfFilteredConfigurations(String filter)
    throws ConfigurationServiceException
  {
    String getNumberOfConfigurationsSQL = "SELECT COUNT(key) FROM configuration.configuration";

    String getNumberOfFilteredConfigurationsSQL = "SELECT COUNT(key) FROM "
        + "configuration.configuration WHERE (UPPER(key) LIKE ?)";

    try (Connection connection = dataSource.getConnection();
      PreparedStatement statement = connection.prepareStatement(StringUtil.isNullOrEmpty(filter)
          ? getNumberOfConfigurationsSQL
          : getNumberOfFilteredConfigurationsSQL))
    {
      if (!StringUtil.isNullOrEmpty(filter))
      {
        statement.setString(1, "%" + filter.toUpperCase() + "%");
      }

      try (ResultSet rs = statement.executeQuery())
      {
        if (rs.next())
        {
          return rs.getInt(1);
        }
        else
        {
          throw new ConfigurationServiceException(String.format(
              "No rows were returned as a result of executing the SQL statement (%s)",
              getNumberOfFilteredConfigurationsSQL));
        }
      }
    }
    catch (Throwable e)
    {
      throw new ConfigurationServiceException(String.format(
          "Failed to retrieve the number of configurations matching the filter (%s)", filter), e);
    }
  }

  /**
   * Retrieve the value for the <code>String</code> configuration.
   *
   * @param key the key used to uniquely identify the configuration
   *
   * @return the value for the <code>String</code> configuration
   */
  @Override
  public String getString(String key)
    throws ConfigurationNotFoundException, ConfigurationServiceException
  {
    String getValueSQL = "SELECT value FROM configuration.configuration WHERE (UPPER(key) = ?)";

    try (Connection connection = dataSource.getConnection();
      PreparedStatement statement = connection.prepareStatement(getValueSQL))
    {
      statement.setString(1, key.toUpperCase());

      try (ResultSet rs = statement.executeQuery())
      {
        if (rs.next())
        {
          return rs.getString(1);
        }
        else
        {
          throw new ConfigurationNotFoundException(key);
        }
      }
    }
    catch (ConfigurationNotFoundException e)
    {
      throw e;
    }
    catch (Throwable e)
    {
      throw new ConfigurationServiceException(String.format(
          "Failed to retrieve the String configuration with the key (%s)", key), e);
    }
  }

  /**
   * Retrieve the value for the <code>String</code> configuration.
   *
   * @param key          the key used to uniquely identify the configuration
   * @param defaultValue the default value to return if the configuration does not exist
   *
   * @return the value for the <code>String</code> configuration or the default value if the
   *         configuration does not exist
   */
  @Override
  public String getString(String key, String defaultValue)
    throws ConfigurationServiceException
  {
    String getValueSQL = "SELECT value FROM configuration.configuration WHERE (UPPER(key) = ?)";

    try (Connection connection = dataSource.getConnection();
      PreparedStatement statement = connection.prepareStatement(getValueSQL))
    {
      statement.setString(1, key.toUpperCase());

      try (ResultSet rs = statement.executeQuery())
      {
        if (rs.next())
        {
          return rs.getString(1);
        }
        else
        {
          return defaultValue;
        }
      }
    }
    catch (Throwable e)
    {
      throw new ConfigurationServiceException(String.format(
          "Failed to retrieve the String configuration with the key (%s)", key), e);
    }
  }

  /**
   * Check if a configuration with the specified key exists.
   *
   * @param key the key used to uniquely identify the configuration
   *
   * @return <code>true</code> if a configuration with the specified key exists or
   *         <code>false</code> otherwise
   */
  @Override
  public boolean keyExists(String key)
    throws ConfigurationServiceException
  {
    try (Connection connection = dataSource.getConnection())
    {
      return keyExists(connection, key);
    }
    catch (Throwable e)
    {
      throw new ConfigurationServiceException("Failed to checked whether the configuration key ("
          + key + ") exists", e);
    }
  }

  /**
   * Set the configuration.
   *
   * @param configuration the configuration
   */
  @Override
  @Transactional
  public void setConfiguration(Configuration configuration)
    throws ConfigurationServiceException
  {
    setConfiguration(configuration.getKey(), configuration.getValue(),
        configuration.getDescription());
  }

  /**
   * Set the configuration key to the specified value.
   *
   * @param key         the key used to uniquely identify the configuration
   * @param value       the value for the configuration
   * @param description the description for the configuration
   */
  @Override
  @Transactional
  public void setConfiguration(String key, Object value, String description)
    throws ConfigurationServiceException
  {
    String updateValueSQL =
        "UPDATE configuration.configuration SET value = ?, description = ? WHERE (UPPER(key) = ?)";

    try (Connection connection = dataSource.getConnection())
    {
      String stringValue;

      if (value instanceof String)
      {
        stringValue = (String) value;
      }
      else if (value instanceof byte[])
      {
        stringValue = Base64Util.encodeBytes((byte[]) value);
      }
      else
      {
        stringValue = value.toString();
      }

      if (keyExists(connection, key))
      {
        try (PreparedStatement statement = connection.prepareStatement(updateValueSQL))
        {
          statement.setString(1, stringValue);
          statement.setString(2, StringUtil.notNull(description));
          statement.setString(3, key.toUpperCase());

          if (statement.executeUpdate() <= 0)
          {
            throw new ConfigurationServiceException(String.format(
                "No rows were affected as a result of executing the SQL statement (%s)",
                updateValueSQL));
          }
        }
      }
      else
      {
        createValue(connection, key, stringValue, description);
      }
    }
    catch (Throwable e)
    {
      throw new ConfigurationServiceException(String.format(
          "Failed to set the configuration with the key (%s)", key), e);
    }
  }

  private void createValue(Connection connection, String key, Object value, String description)
    throws SQLException, ConfigurationServiceException
  {
    String createValueSQL =
        "INSERT INTO configuration.configuration (key, value, description) VALUES (?, ?, ?)";

    String stringValue;

    if (value instanceof String)
    {
      stringValue = (String) value;
    }
    else
    {
      stringValue = value.toString();
    }

    try (PreparedStatement statement = connection.prepareStatement(createValueSQL))
    {
      statement.setString(1, key);
      statement.setString(2, stringValue);
      statement.setString(3, StringUtil.notNull(description));

      if (statement.executeUpdate() <= 0)
      {
        throw new ConfigurationServiceException(String.format(
            "No rows were affected as a result of executing the SQL statement (%s)",
            createValueSQL));
      }
    }
  }

  private List<Configuration> getConfigurations(PreparedStatement statement)
    throws SQLException
  {
    try (ResultSet rs = statement.executeQuery())
    {
      List<Configuration> configurations = new ArrayList<>();

      while (rs.next())
      {
        configurations.add(new Configuration(rs.getString(1), rs.getString(2), rs.getString(3)));
      }

      return configurations;
    }
  }

  private boolean keyExists(Connection connection, String key)
    throws SQLException
  {
    String keyExistsSQL =
        "SELECT COUNT(key) FROM configuration.configuration WHERE (UPPER(key) LIKE ?)";

    try (PreparedStatement statement = connection.prepareStatement(keyExistsSQL))
    {
      statement.setString(1, key.toUpperCase());

      try (ResultSet rs = statement.executeQuery())
      {
        if (rs.next())
        {
          return (rs.getInt(1) > 0);
        }
        else
        {
          throw new SQLException(String.format(
              "Failed to check whether the configuration with the key (%s) exists: "
              + "No results were returned as a result of executing the SQL statement (%s)", key,
              keyExistsSQL));
        }
      }
    }
  }
}
