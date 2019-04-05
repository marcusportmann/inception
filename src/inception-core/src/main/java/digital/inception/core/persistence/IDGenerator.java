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

package digital.inception.core.persistence;

//~--- non-JDK imports --------------------------------------------------------

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionException;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.transaction.support.TransactionTemplate;

//~--- JDK imports ------------------------------------------------------------

import java.sql.*;

import java.util.UUID;

import javax.sql.DataSource;

/**
 * The <code>IDGenerator</code> class provides unique IDs for the entity types in the database.
 * <p>
 * It requires the idgenerator table which must be created under the idgenerator schema within the
 * database. The unique ID will be retrieved using a new transaction while suspending the existing
 * database transaction. This is done to reduce deadlocks and improve performance.
 *
 * @author Marcus Portmann
 */
@Repository
public class IDGenerator
{
  /**
   * The data source used to provide connections to the application database.
   */
  @Autowired
  @Qualifier("applicationDataSource")
  private DataSource dataSource;

  /**
   * The Transaction Manager.
   */
  @Autowired
  private PlatformTransactionManager transactionManager;

  /**
   * Get the next unique <code>long</code> ID for the entity with the specified type.
   *
   * @param type the type of entity to retrieve the next ID for
   *
   * @return the next unique <code>long</code> ID for the entity with the specified type
   */
  public long next(String type)
  {
    TransactionTemplate transactionTemplate = new TransactionTemplate(transactionManager,
        new DefaultTransactionDefinition(TransactionDefinition.PROPAGATION_REQUIRES_NEW));

    try
    {
      return transactionTemplate.execute(
          status ->
          {
            try (Connection connection = dataSource.getConnection())
            {
              Long id = getCurrentId(connection, type);

              if (id == null)
              {
                id = 1L;
                insertId(connection, type, id);

                /*
                 * TODO: Handle a duplicate row exception caused by the INSERT/UPDATE race
                 *       condition. This race condition occurs when there is no row for a particular
                 *       type of entity in the idgenerator table. Assuming we have two different
                 *       threads that are both attempting to retrieve the next ID for this entity
                 *       type. When the first thread executes the SELECT FOR UPDATE call, it will
                 *       not able to lock a row and will then attempt to execute the INSERT. If
                 *       another thread manages to execute the SELECT FOR UPDATE call before the
                 *       first thread completes the INSERT then one of the threads will experience a
                 *       duplicate row exception as they will both attempt to INSERT. The easiest
                 *       way to prevent this from happening is to pre-populate the idgenerator
                 *       table with initial IDs.
                 */
              }
              else
              {
                id = id + 1L;
                updateId(connection, type, id);
              }

              return id;
            }
            catch (Throwable e)
            {
              throw new IDGeneratorException(String.format("Failed to retrieve the new ID for the "
                  + "entity of type (%s) from the idgenerator table: %s", type, e.getMessage()), e);
            }
          }
          );
    }
    catch (TransactionException e)
    {
      throw new IDGeneratorException(String.format("Failed to retrieve the new ID for the entity "
          + "of type (%s) from the idgenerator table: %s", type, e.getMessage()), e);
    }
  }

  /**
   * Returns the next <code>UUID</code>.
   *
   * @return the next <code>UUID</code>
   */
  public UUID nextUUID()
  {
    // TODO: Save the results of checking if we are using a PostgreSQL database

    /*
     * First check whether this is a PostgreSQL database and we should be using a stored procedure
     * to retrieve the next UUID.
     */
    try (Connection connection = dataSource.getConnection())
    {
      DatabaseMetaData metaData = connection.getMetaData();

      if (metaData.getDatabaseProductName().equals("PostgreSQL"))
      {
        // TODO: Retrieve the next UUID using a PostgreSQL stored procedure
      }
    }
    catch (Throwable e)
    {
      throw new IDGeneratorException("Failed to retrieve the next UUID", e);
    }

    return UUID.randomUUID();
  }

  private Long getCurrentId(Connection connection, String type)
    throws SQLException
  {
    try (PreparedStatement statement = connection.prepareStatement(
        "SELECT current FROM idgenerator.idgenerator WHERE name=? FOR UPDATE"))
    {
      statement.setString(1, type);

      try (ResultSet rs = statement.executeQuery())
      {
        if (rs.next())
        {
          return rs.getLong(1);
        }
        else
        {
          return null;
        }
      }
    }
  }

  private void insertId(Connection connection, String type, long id)
    throws SQLException
  {
    try (PreparedStatement statement = connection.prepareStatement(
        "INSERT INTO idgenerator.idgenerator (current, name) VALUES (?, ?)"))
    {
      statement.setLong(1, id);
      statement.setString(2, type);

      if (statement.executeUpdate() == 0)
      {
        throw new SQLException("No rows were affected while inserting the idgenerator.idgenerator "
            + "table row for the type (" + type + ")");
      }
    }
  }

  private void updateId(Connection connection, String type, long id)
    throws SQLException
  {
    try (PreparedStatement statement = connection.prepareStatement(
        "UPDATE idgenerator.idgenerator SET current=? WHERE name=?"))
    {
      statement.setLong(1, id);
      statement.setString(2, type);

      if (statement.executeUpdate() == 0)
      {
        throw new SQLException("No rows were affected while updating the idgenerator.idgenerator "
            + "table row for the type (" + type + ")");
      }
    }
  }
}
