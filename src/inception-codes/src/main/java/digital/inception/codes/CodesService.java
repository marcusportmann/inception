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

package digital.inception.codes;

//~--- non-JDK imports --------------------------------------------------------

import digital.inception.core.xml.DtdJarResolver;
import digital.inception.core.xml.XmlParserErrorHandler;
import digital.inception.core.xml.XmlUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import org.xml.sax.InputSource;

//~--- JDK imports ------------------------------------------------------------

import java.lang.reflect.Constructor;

import java.net.URL;

import java.sql.*;

import java.time.LocalDateTime;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

/**
 * The <code>CodesService</code> class provides the Codes Service implementation.
 *
 * @author Marcus Portmann
 */
@Service
@SuppressWarnings("unused")
public class CodesService
  implements ICodesService, InitializingBean
{
  /**
   * The path to the code provider configuration files (META-INF/code-providers.xml) on the
   * classpath.
   */
  private static final String CODE_PROVIDERS_CONFIGURATION_PATH = "META-INF/code-providers.xml";

  /* Logger */
  private static final Logger logger = LoggerFactory.getLogger(CodesService.class);

  /**
   * The Spring application context.
   */
  private ApplicationContext applicationContext;

  /**
   * The configuration information for the code providers read from the code provider configuration
   * files (META-INF/code-providers.xml) on the classpath.
   */
  private List<CodeProviderConfig> codeProviderConfigs;

  /* The code providers. */
  private List<ICodeProvider> codeProviders;

  /**
   * The data source used to provide connections to the application database.
   */
  private DataSource dataSource;

  /**
   * Constructs a new <code>CodesService</code>.
   *
   * @param applicationContext the Spring application context
   * @param dataSource         the data source used to provide connections to the application
   *                           database
   */
  public CodesService(ApplicationContext applicationContext, @Qualifier(
      "applicationDataSource") DataSource dataSource)
  {
    this.applicationContext = applicationContext;
    this.dataSource = dataSource;
  }

  /**
   * Initialize the Codes Service.
   */
  @Override
  public void afterPropertiesSet()
  {
    logger.info("Initializing the Codes Service");

    codeProviders = new ArrayList<>();

    try
    {
      // Read the codes configuration
      readCodeProviderConfigurations();

      // Initialize the code providers
      initCodeProviders();
    }
    catch (Throwable e)
    {
      throw new RuntimeException("Failed to initialize the Codes Service", e);
    }
  }

  /**
   * Check whether the code category exists.
   *
   * @param codeCategoryId the ID used to uniquely identify the code category
   *
   * @return <code>true</code> if the code category exists or <code>false</code> otherwise
   */
  @Override
  public boolean codeCategoryExists(String codeCategoryId)
    throws CodesServiceException
  {
    try (Connection connection = dataSource.getConnection())
    {
      // Check if the code category exists in the database
      return codeCategoryExists(connection, codeCategoryId);
    }
    catch (Throwable e)
    {
      throw new CodesServiceException(String.format(
          "Failed to check whether the code category (%s) exists", codeCategoryId), e);
    }
  }

  /**
   * Check whether the code exists.
   *
   * @param codeCategoryId the ID used to uniquely identify the code category the code is associated
   *                       with
   * @param codeId         the ID used to uniquely identify the code
   *
   * @return <code>true</code> if the code exists or <code>false</code> otherwise
   */
  @Override
  public boolean codeExists(String codeCategoryId, String codeId)
    throws CodesServiceException
  {
    try (Connection connection = dataSource.getConnection())
    {
      return codeExists(connection, codeCategoryId, codeId);
    }
    catch (Throwable e)
    {
      throw new CodesServiceException(String.format(
          "Failed to check whether the code (%s) exists for the code category (%s)", codeId,
          codeCategoryId), e);
    }
  }

  /**
   * Create the new code.
   *
   * @param code the <code>Code</code> instance containing the information for the new code
   */
  @Override
  @Transactional
  public void createCode(Code code)
    throws DuplicateCodeException, CodeCategoryNotFoundException, CodesServiceException
  {
    String createCodeSQL =
        "INSERT INTO codes.codes (id, code_category_id, name, value) VALUES (?, ?, ?, ?)";

    try (Connection connection = dataSource.getConnection();
      PreparedStatement statement = connection.prepareStatement(createCodeSQL))
    {
      if (codeExists(connection, code.getCodeCategoryId(), code.getId()))
      {
        throw new DuplicateCodeException(code.getCodeCategoryId(), code.getId());
      }

      if (!codeCategoryExists(connection, code.getCodeCategoryId()))
      {
        throw new CodeCategoryNotFoundException(code.getCodeCategoryId());
      }

      statement.setString(1, code.getId());
      statement.setString(2, code.getCodeCategoryId());
      statement.setString(3, code.getName());
      statement.setString(4, code.getValue());

      if (statement.executeUpdate() != 1)
      {
        throw new CodesServiceException(String.format(
            "No rows were affected as a result of executing the SQL statement (%s)",
            createCodeSQL));
      }
    }
    catch (DuplicateCodeException | CodeCategoryNotFoundException e)
    {
      throw e;
    }
    catch (Throwable e)
    {
      throw new CodesServiceException(String.format(
          "Failed to create the code (%s) for the code category (%s)", code.getName(),
          code.getCodeCategoryId()), e);
    }
  }

  /**
   * Create the new code category.
   *
   * @param codeCategory the <code>CodeCategory</code> instance containing the information for the
   *                     new code category
   */
  @Override
  @Transactional
  public void createCodeCategory(CodeCategory codeCategory)
    throws DuplicateCodeCategoryException, CodesServiceException
  {
    String createCodeCategorySQL =
        "INSERT INTO codes.code_categories (id, name, data, updated) VALUES (?, ?, ?, ?)";

    try (Connection connection = dataSource.getConnection();
      PreparedStatement statement = connection.prepareStatement(createCodeCategorySQL))
    {
      if (codeCategoryExists(connection, codeCategory.getId()))
      {
        throw new DuplicateCodeCategoryException(codeCategory.getId());
      }

      statement.setString(1, codeCategory.getId());
      statement.setString(2, codeCategory.getName());
      statement.setString(3, codeCategory.getData());

      if (codeCategory.getUpdated() == null)
      {
        LocalDateTime updated = LocalDateTime.now();

        statement.setTimestamp(4, Timestamp.valueOf(updated));

        codeCategory.setUpdated(updated);
      }
      else
      {
        statement.setTimestamp(4, Timestamp.valueOf(codeCategory.getUpdated()));
      }

      if (statement.executeUpdate() != 1)
      {
        throw new CodesServiceException(String.format(
            "No rows were affected as a result of executing the SQL statement (%s)",
            createCodeCategorySQL));
      }
    }
    catch (DuplicateCodeCategoryException e)
    {
      throw e;
    }
    catch (Throwable e)
    {
      throw new CodesServiceException(String.format("Failed to create the code category (%s)",
          codeCategory.getId()), e);
    }
  }

  /**
   * Delete the code.
   *
   * @param codeCategoryId the ID used to uniquely identify the code category the code is associated
   *                       with
   * @param codeId         the ID uniquely identifying the code
   */
  @Override
  @Transactional
  public void deleteCode(String codeCategoryId, String codeId)
    throws CodeNotFoundException, CodesServiceException
  {
    String deleteCodeSQL = "DELETE FROM codes.codes WHERE code_category_id=? AND id=?";

    try (Connection connection = dataSource.getConnection();
      PreparedStatement statement = connection.prepareStatement(deleteCodeSQL))
    {
      statement.setString(1, codeCategoryId);
      statement.setString(2, codeId);

      if (statement.executeUpdate() == 0)
      {
        throw new CodeNotFoundException(codeCategoryId, codeId);
      }
    }
    catch (CodeNotFoundException e)
    {
      throw e;
    }
    catch (Throwable e)
    {
      throw new CodesServiceException(String.format(
          "Failed to delete the code (%s) for the code category (%s)", codeId, codeCategoryId), e);
    }
  }

  /**
   * Delete the code category.
   *
   * @param codeCategoryId the ID used to uniquely identify the code category
   */
  @Override
  @Transactional
  public void deleteCodeCategory(String codeCategoryId)
    throws CodeCategoryNotFoundException, CodesServiceException
  {
    String deleteCodeCategorySQL = "DELETE FROM codes.code_categories WHERE id=?";

    try (Connection connection = dataSource.getConnection();
      PreparedStatement statement = connection.prepareStatement(deleteCodeCategorySQL))
    {
      statement.setString(1, codeCategoryId);

      if (statement.executeUpdate() == 0)
      {
        throw new CodeCategoryNotFoundException(codeCategoryId);
      }
    }
    catch (CodeCategoryNotFoundException e)
    {
      throw e;
    }
    catch (Throwable e)
    {
      throw new CodesServiceException(String.format("Failed to delete the code category (%s)",
          codeCategoryId), e);
    }

  }

  /**
   * Retrieve the code.
   *
   * @param codeCategoryId the ID used to uniquely identify the code category the code is associated
   *                       with
   * @param codeId         the ID uniquely identifying the code
   *
   * @return the code
   */
  @Override
  public Code getCode(String codeCategoryId, String codeId)
    throws CodeNotFoundException, CodesServiceException
  {
    String getCodeSQL = "SELECT id, code_category_id, name, VALUE FROM codes.codes "
        + "WHERE code_category_id=? AND id=?";

    try (Connection connection = dataSource.getConnection();
      PreparedStatement statement = connection.prepareStatement(getCodeSQL))
    {
      statement.setString(1, codeCategoryId);
      statement.setString(2, codeId);

      try (ResultSet rs = statement.executeQuery())
      {
        if (rs.next())
        {
          return getCode(rs);
        }
        else
        {
          // Check if one of the registered code providers supports the code category
          for (ICodeProvider codeProvider : codeProviders)
          {
            if (codeProvider.codeCategoryExists(codeCategoryId))
            {
              return codeProvider.getCode(codeCategoryId, codeId);
            }
          }
        }

        throw new CodeNotFoundException(codeCategoryId, codeId);
      }
    }
    catch (CodeNotFoundException e)
    {
      throw e;
    }
    catch (Throwable e)
    {
      throw new CodesServiceException(String.format(
          "Failed to retrieve the code (%s) for the code category (%s)", codeId, codeCategoryId),
          e);
    }

  }

  /**
   * Returns all the code categories.
   *
   * @return all the code categories
   */
  @Override
  public List<CodeCategory> getCodeCategories()
    throws CodesServiceException
  {
    String getCodeCategoriesSQL =
        "SELECT id, name, data, updated FROM codes.code_categories ORDER BY name";

    try (Connection connection = dataSource.getConnection();
      PreparedStatement statement = connection.prepareStatement(getCodeCategoriesSQL))
    {
      try (ResultSet rs = statement.executeQuery())
      {
        List<CodeCategory> codeCategories = new ArrayList<>();

        while (rs.next())
        {
          codeCategories.add(getCodeCategory(rs));
        }

        return codeCategories;
      }
    }
    catch (Throwable e)
    {
      throw new CodesServiceException("Failed to retrieve the code categories", e);
    }
  }

  /**
   * Retrieve the code category.
   *
   * @param codeCategoryId the ID used to uniquely identify the code category
   *
   * @return the code category
   */
  @Override
  public CodeCategory getCodeCategory(String codeCategoryId)
    throws CodeCategoryNotFoundException, CodesServiceException
  {
    String getCodeCategorySQL =
        "SELECT id, name, data, updated FROM codes.code_categories WHERE id=?";

    try (Connection connection = dataSource.getConnection();
      PreparedStatement statement = connection.prepareStatement(getCodeCategorySQL))
    {
      statement.setString(1, codeCategoryId);

      try (ResultSet rs = statement.executeQuery())
      {
        if (rs.next())
        {
          return getCodeCategory(rs);
        }
        else
        {
          // Check if one of the registered code providers supports the code category
          for (ICodeProvider codeProvider : codeProviders)
          {
            if (codeProvider.codeCategoryExists(codeCategoryId))
            {
              return codeProvider.getCodeCategory(codeCategoryId);
            }
          }
        }

        throw new CodeCategoryNotFoundException(codeCategoryId);
      }
    }
    catch (CodeCategoryNotFoundException e)
    {
      throw e;
    }
    catch (Throwable e)
    {
      throw new CodesServiceException(String.format("Failed to retrieve the code category (%s)",
          codeCategoryId), e);
    }
  }

  /**
   * Retrieve the XML or JSON data for the code category.
   * <p/>
   * NOTE: This will also attempt to retrieve the data from the appropriate code provider that has
   * been registered with the Codes Service in the <code>META-INF/code-providers.xml</code>
   * configuration file.
   *
   * @param codeCategoryId the ID used to uniquely identify the code category
   *
   * @return the XML or JSON data for the code category
   */
  @Override
  public String getCodeCategoryData(String codeCategoryId)
    throws CodeCategoryNotFoundException, CodesServiceException
  {
    String getCodeCategorySQL = "SELECT data FROM codes.code_categories WHERE id=?";

    try (Connection connection = dataSource.getConnection();
      PreparedStatement statement = connection.prepareStatement(getCodeCategorySQL))
    {
      statement.setString(1, codeCategoryId);

      try (ResultSet rs = statement.executeQuery())
      {
        if (rs.next())
        {
          String data = rs.getString(1);

          return StringUtils.isEmpty(data)
              ? ""
              : data;
        }
      }

      // Check if one of the registered code providers supports the code category
      for (ICodeProvider codeProvider : codeProviders)
      {
        if (codeProvider.codeCategoryExists(codeCategoryId))
        {
          String data = codeProvider.getDataForCodeCategory(codeCategoryId);

          return StringUtils.isEmpty(data)
              ? ""
              : data;
        }
      }

      throw new CodeCategoryNotFoundException(codeCategoryId);
    }
    catch (CodeCategoryNotFoundException e)
    {
      throw e;
    }
    catch (Throwable e)
    {
      throw new CodesServiceException(String.format(
          "Failed to retrieve the data for the code category (%s)", codeCategoryId), e);
    }
  }

  /**
   * Retrieve the XML or JSON data for the code category using the specified parameters.
   * <p/>
   * NOTE: This will also attempt to retrieve the data from the appropriate code provider that has
   * been registered with the Codes Service in the <code>META-INF/code-providers.xml</code>
   * configuration file.
   *
   * @param codeCategoryId the ID used to uniquely identify the code category
   * @param parameters     the parameters
   *
   * @return the XML or JSON data for the code category
   */
  @Override
  public String getCodeCategoryDataWithParameters(String codeCategoryId, Map<String,
      String> parameters)
    throws CodeCategoryNotFoundException, CodesServiceException
  {
    String getCodeCategorySQL = "SELECT data FROM codes.code_categories WHERE id=?";

    try (Connection connection = dataSource.getConnection();
      PreparedStatement statement = connection.prepareStatement(getCodeCategorySQL))
    {
      statement.setString(1, codeCategoryId);

      try (ResultSet rs = statement.executeQuery())
      {
        if (rs.next())
        {
          return rs.getString(1);
        }
      }

      // Check if one of the registered code providers supports the code category
      for (ICodeProvider codeProvider : codeProviders)
      {
        if (codeProvider.codeCategoryExists(codeCategoryId))
        {
          return codeProvider.getDataForCodeCategoryWithParameters(codeCategoryId, parameters);
        }
      }

      throw new CodeCategoryNotFoundException(codeCategoryId);
    }
    catch (CodeCategoryNotFoundException e)
    {
      throw e;
    }
    catch (Throwable e)
    {
      throw new CodesServiceException(String.format(
          "Failed to retrieve the data for the code category (%s)", codeCategoryId), e);
    }
  }

  /**
   * Returns the summaries for all the code categories.
   *
   * @return the summaries for all the code categories
   */
  @Override
  public List<CodeCategorySummary> getCodeCategorySummaries()
    throws CodesServiceException
  {
    String getCodeCategorySummariesSQL =
        "SELECT id, name, updated FROM codes.code_categories ORDER BY name";

    try (Connection connection = dataSource.getConnection();
      PreparedStatement statement = connection.prepareStatement(getCodeCategorySummariesSQL))
    {
      try (ResultSet rs = statement.executeQuery())
      {
        List<CodeCategorySummary> codeCategorySummaries = new ArrayList<>();

        while (rs.next())
        {
          codeCategorySummaries.add(getCodeCategorySummary(rs));
        }

        return codeCategorySummaries;
      }
    }
    catch (Throwable e)
    {
      throw new CodesServiceException("Failed to retrieve the summaries for the code categories",
          e);
    }
  }

  /**
   * Returns the date and time the code category was last updated.
   *
   * @param codeCategoryId the ID used to uniquely identify the code category
   *
   * @return the date and time the code category was last updated
   */
  @Override
  public LocalDateTime getCodeCategoryUpdated(String codeCategoryId)
    throws CodeCategoryNotFoundException, CodesServiceException
  {
    String getCodeCategoryLastUpdatedSQL = "SELECT updated FROM codes.code_categories WHERE id=?";

    try (Connection connection = dataSource.getConnection();
      PreparedStatement statement = connection.prepareStatement(getCodeCategoryLastUpdatedSQL))
    {
      statement.setString(1, codeCategoryId);

      try (ResultSet rs = statement.executeQuery())
      {
        if (rs.next())
        {
          return (rs.getTimestamp(1) == null)
              ? null
              : rs.getTimestamp(1).toLocalDateTime();
        }
        else
        {
          // Check if one of the registered code providers supports the code category
          for (ICodeProvider codeProvider : codeProviders)
          {
            if (codeProvider.codeCategoryExists(codeCategoryId))
            {
              return codeProvider.getCodeCategoryLastUpdated(codeCategoryId);
            }
          }
        }

        throw new CodeCategoryNotFoundException(codeCategoryId);
      }
    }
    catch (CodeCategoryNotFoundException e)
    {
      throw e;
    }
    catch (Throwable e)
    {
      throw new CodesServiceException(String.format(
          "Failed to retrieve the date and time the code category (%s) was last updated",
          codeCategoryId), e);
    }
  }

  /**
   * Retrieve the codes for the code category.
   * <p/>
   * NOTE: This will also attempt to retrieve the codes from the appropriate code provider that has
   * been registered with the Codes Service in the <code>META-INF/code-providers.xml</code>
   * configuration file.
   *
   * @param codeCategoryId the ID used to uniquely identify the code category
   *
   * @return the codes for the code category
   */
  @Override
  public List<Code> getCodes(String codeCategoryId)
    throws CodeCategoryNotFoundException, CodesServiceException
  {
    try (Connection connection = dataSource.getConnection())
    {
      if (codeCategoryExists(connection, codeCategoryId))
      {
        return getCodesForCodeCategory(connection, codeCategoryId);
      }

      // Check if one of the registered code providers supports the code category
      for (ICodeProvider codeProvider : codeProviders)
      {
        if (codeProvider.codeCategoryExists(codeCategoryId))
        {
          return codeProvider.getCodesForCodeCategory(codeCategoryId);
        }
      }

      throw new CodeCategoryNotFoundException(codeCategoryId);
    }
    catch (CodeCategoryNotFoundException e)
    {
      throw e;
    }
    catch (Throwable e)
    {
      throw new CodesServiceException(String.format(
          "Failed to retrieve the codes for the code category (%s)", codeCategoryId), e);
    }
  }

  /**
   * Retrieve the codes for the code category using the specified parameters.
   * <p/>
   * NOTE: This will also attempt to retrieve the codes from the appropriate code provider that has
   * been registered with the Codes Service in the <code>META-INF/code-providers.xml</code>
   * configuration file.
   *
   * @param codeCategoryId the ID used to uniquely identify the code category
   * @param parameters     the parameters
   *
   * @return the codes for the code category
   */
  @Override
  public List<Code> getCodesWithParameters(String codeCategoryId, Map<String, String> parameters)
    throws CodeCategoryNotFoundException, CodesServiceException
  {
    try (Connection connection = dataSource.getConnection())
    {
      if (codeCategoryExists(connection, codeCategoryId))
      {
        return getCodesForCodeCategory(connection, codeCategoryId);
      }

      // Check if one of the registered code providers supports the code category
      for (ICodeProvider codeProvider : codeProviders)
      {
        if (codeProvider.codeCategoryExists(codeCategoryId))
        {
          return codeProvider.getCodesForCodeCategoryWithParameters(codeCategoryId, parameters);
        }
      }

      throw new CodeCategoryNotFoundException(codeCategoryId);
    }
    catch (CodeCategoryNotFoundException e)
    {
      throw e;
    }
    catch (Throwable e)
    {
      throw new CodesServiceException(String.format(
          "Failed to retrieve the codes for the code category (%s)", codeCategoryId), e);
    }
  }

  /**
   * Returns the number of code categories.
   *
   * @return the number of code categories
   */
  @Override
  public int getNumberOfCodeCategories()
    throws CodesServiceException
  {
    String getNumberOfCodeCategoriesSQL = "SELECT COUNT(ID) FROM codes.code_categories";

    try (Connection connection = dataSource.getConnection();
      PreparedStatement statement = connection.prepareStatement(getNumberOfCodeCategoriesSQL))
    {
      try (ResultSet rs = statement.executeQuery())
      {
        if (rs.next())
        {
          return rs.getInt(1);
        }
        else
        {
          throw new CodesServiceException(String.format(
              "No results were returned as a result of executing the SQL statement (%s)",
              getNumberOfCodeCategoriesSQL));
        }
      }
    }
    catch (Throwable e)
    {
      throw new CodesServiceException("Failed to retrieve the number of code categories", e);
    }
  }

  /**
   * Returns the number of codes for the code category.
   *
   * @param codeCategoryId the ID used to uniquely identify the code category
   *
   * @return the number of codes for the code category
   */
  @Override
  public int getNumberOfCodes(String codeCategoryId)
    throws CodeCategoryNotFoundException, CodesServiceException
  {
    String getNumberOfCodesForCodeCategorySQL =
        "SELECT COUNT(ID) FROM codes.codes WHERE code_category_id=?";

    try (Connection connection = dataSource.getConnection();
      PreparedStatement statement = connection.prepareStatement(getNumberOfCodesForCodeCategorySQL))
    {
      if (!codeCategoryExists(connection, codeCategoryId))
      {
        throw new CodeCategoryNotFoundException(codeCategoryId);
      }

      statement.setString(1, codeCategoryId);

      try (ResultSet rs = statement.executeQuery())
      {
        if (rs.next())
        {
          return rs.getInt(1);
        }
        else
        {
          throw new CodesServiceException(String.format(
              "No results were returned as a result of executing the SQL statement (%s)",
              getNumberOfCodesForCodeCategorySQL));
        }
      }
    }
    catch (CodeCategoryNotFoundException e)
    {
      throw e;
    }
    catch (Throwable e)
    {
      throw new CodesServiceException(String.format(
          "Failed to retrieve the number of codes for the code category (%s)", codeCategoryId), e);
    }

  }

  /**
   * Update the existing code.
   *
   * @param code the <code>Code</code> instance containing the updated information for the code
   *
   * @return the updated code
   */
  @Override
  public Code updateCode(Code code)
    throws CodeNotFoundException, CodesServiceException
  {
    String updateCodeSQL = "UPDATE codes.codes SET NAME=?, VALUE=? WHERE ID=?";

    try (Connection connection = dataSource.getConnection();
      PreparedStatement statement = connection.prepareStatement(updateCodeSQL))
    {
      statement.setString(1, code.getName());
      statement.setString(2, code.getValue());
      statement.setString(3, code.getId());

      if (statement.executeUpdate() == 0)
      {
        throw new CodeNotFoundException(code.getCodeCategoryId(), code.getId());
      }

      return code;
    }
    catch (CodeNotFoundException e)
    {
      throw e;
    }
    catch (Throwable e)
    {
      throw new CodesServiceException(String.format("Failed to update the code (%s)",
          code.getId()), e);
    }

  }

  /**
   * Update the existing code category.
   *
   * @param codeCategory the <code>CodeCategory</code> instance containing the updated information
   *                     for the code category
   *
   * @return the updated code category
   */
  @Override
  public CodeCategory updateCodeCategory(CodeCategory codeCategory)
    throws CodeCategoryNotFoundException, CodesServiceException
  {
    String updateCodeCategorySQL =
        "UPDATE codes.code_categories SET name=?, data=?, updated=? WHERE id=?";

    try (Connection connection = dataSource.getConnection();
      PreparedStatement statement = connection.prepareStatement(updateCodeCategorySQL))
    {
      LocalDateTime updated = LocalDateTime.now();

      statement.setString(1, codeCategory.getName());
      statement.setString(2, codeCategory.getData());
      statement.setTimestamp(3, Timestamp.valueOf(updated));
      statement.setString(4, codeCategory.getId());

      if (statement.executeUpdate() == 0)
      {
        throw new CodeCategoryNotFoundException(codeCategory.getId());
      }

      codeCategory.setUpdated(updated);

      return codeCategory;
    }
    catch (CodeCategoryNotFoundException e)
    {
      throw e;
    }
    catch (Throwable e)
    {
      throw new CodesServiceException(String.format("Failed to update the code category (%s)",
          codeCategory.getId()), e);
    }
  }

  /**
   * Update the XML or JSON data for the code category.
   *
   * @param codeCategoryId the ID used to uniquely identify the code category
   * @param data           the updated XML or JSON data
   */
  @Override
  public void updateCodeCategoryData(String codeCategoryId, String data)
    throws CodeCategoryNotFoundException, CodesServiceException
  {
    String updateCodeCategoryDataSQL =
        "UPDATE codes.code_categories SET data=?, updated=? WHERE id=?";

    try (Connection connection = dataSource.getConnection();
      PreparedStatement statement = connection.prepareStatement(updateCodeCategoryDataSQL))
    {
      LocalDateTime updated = LocalDateTime.now();

      statement.setString(1, data);
      statement.setTimestamp(2, Timestamp.valueOf(updated));
      statement.setString(3, codeCategoryId);

      if (statement.executeUpdate() == 0)
      {
        throw new CodeCategoryNotFoundException(codeCategoryId);
      }
    }
    catch (CodeCategoryNotFoundException e)
    {
      throw e;
    }
    catch (Throwable e)
    {
      throw new CodesServiceException(String.format(
          "Failed to update the data for the code category (%s)", codeCategoryId), e);
    }
  }

  private boolean codeCategoryExists(Connection connection, String codeCategoryId)
    throws SQLException
  {
    String codeCategoryExistsSQL = "SELECT id FROM codes.code_categories WHERE id=?";

    try (PreparedStatement statement = connection.prepareStatement(codeCategoryExistsSQL))
    {
      statement.setString(1, codeCategoryId);

      try (ResultSet rs = statement.executeQuery())
      {
        return rs.next();
      }
    }
  }

  private boolean codeExists(Connection connection, String codeCategoryId, String codeId)
    throws SQLException
  {
    String codeExistsSQL =
        "SELECT code_category_id, id FROM codes.codes WHERE code_category_id=? AND id=?";

    try (PreparedStatement statement = connection.prepareStatement(codeExistsSQL))
    {
      statement.setString(1, codeCategoryId);
      statement.setString(2, codeId);

      try (ResultSet rs = statement.executeQuery())
      {
        return rs.next();
      }
    }
  }

  private Code getCode(ResultSet rs)
    throws SQLException
  {
    return new Code(rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4));
  }

  private CodeCategory getCodeCategory(ResultSet rs)
    throws SQLException
  {
    return new CodeCategory(rs.getString(1), rs.getString(2), rs.getString(3),
        (rs.getTimestamp(4) == null)
        ? null
        : rs.getTimestamp(4).toLocalDateTime());
  }

  private CodeCategorySummary getCodeCategorySummary(ResultSet rs)
    throws SQLException
  {
    return new CodeCategorySummary(rs.getString(1), rs.getString(2),
        (rs.getTimestamp(3) == null)
        ? null
        : rs.getTimestamp(3).toLocalDateTime());
  }

  private List<Code> getCodes(PreparedStatement statement)
    throws SQLException
  {
    try (ResultSet rs = statement.executeQuery())
    {
      List<Code> codes = new ArrayList<>();

      while (rs.next())
      {
        codes.add(getCode(rs));
      }

      return codes;
    }
  }

  private List<Code> getCodesForCodeCategory(Connection connection, String codeCategoryId)
    throws SQLException
  {
    String getCodesForCodeCategorySQL =
        "SELECT id, code_category_id, name, value FROM codes.codes "
        + "WHERE code_category_id=? ORDER BY name";

    try (PreparedStatement statement = connection.prepareStatement(getCodesForCodeCategorySQL))
    {
      statement.setString(1, codeCategoryId);

      return getCodes(statement);
    }
  }

  /**
   * Initialize the code providers.
   */
  private void initCodeProviders()
  {
    // Initialize each code provider
    for (CodeProviderConfig codeProviderConfig : codeProviderConfigs)
    {
      try
      {
        logger.info(String.format("Initializing the code provider (%s) with class (%s)",
            codeProviderConfig.getName(), codeProviderConfig.getClassName()));

        Class<?> clazz = Thread.currentThread().getContextClassLoader().loadClass(
            codeProviderConfig.getClassName());

        Constructor<?> constructor = clazz.getConstructor(CodeProviderConfig.class);

        if (constructor != null)
        {
          // Create an instance of the code provider
          ICodeProvider codeProvider = (ICodeProvider) constructor.newInstance(codeProviderConfig);

          // Perform dependency injection on the code provider
          applicationContext.getAutowireCapableBeanFactory().autowireBean(codeProvider);

          codeProviders.add(codeProvider);
        }
        else
        {
          logger.error(String.format(
              "Failed to register the code provider (%s): The code provider "
              + "class does not provide a constructor with the required signature",
              codeProviderConfig.getClassName()));
        }
      }
      catch (Throwable e)
      {
        logger.error(String.format("Failed to initialize the code provider (%s) with class (%s)",
            codeProviderConfig.getName(), codeProviderConfig.getClassName()), e);
      }
    }
  }

  /**
   * Read the code provider configurations from all the <i>META-INF/code-providers.xml</i>
   * configuration files that can be found on the classpath.
   */
  private void readCodeProviderConfigurations()
    throws CodesServiceException
  {
    try
    {
      codeProviderConfigs = new ArrayList<>();

      ClassLoader classLoader = Thread.currentThread().getContextClassLoader();

      // Load the code provider configuration files from the classpath
      Enumeration<URL> codeProviderConfigurationFiles = classLoader.getResources(
          CODE_PROVIDERS_CONFIGURATION_PATH);

      while (codeProviderConfigurationFiles.hasMoreElements())
      {
        URL codeProviderConfigurationFile = codeProviderConfigurationFiles.nextElement();

        if (logger.isDebugEnabled())
        {
          logger.debug(String.format("Reading the code provider configuration file (%s)",
              codeProviderConfigurationFile.toURI().toString()));
        }

        // Retrieve a document builder instance using the factory
        DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();

        builderFactory.setValidating(true);

        // builderFactory.setNamespaceAware(true);
        DocumentBuilder builder = builderFactory.newDocumentBuilder();

        builder.setEntityResolver(new DtdJarResolver("code-providers.dtd",
            "META-INF/code-providers.dtd"));
        builder.setErrorHandler(new XmlParserErrorHandler());

        // Parse the code providers configuration file using the document builder
        InputSource inputSource = new InputSource(codeProviderConfigurationFile.openStream());
        Document document = builder.parse(inputSource);
        Element rootElement = document.getDocumentElement();

        List<Element> codeProviderElements = XmlUtil.getChildElements(rootElement, "codeProvider");

        for (Element codeProviderElement : codeProviderElements)
        {
          // Read the handler configuration
          String name = XmlUtil.getChildElementText(codeProviderElement, "name");
          String className = XmlUtil.getChildElementText(codeProviderElement, "class");

          CodeProviderConfig codeProviderConfig = new CodeProviderConfig(name, className);

          codeProviderConfigs.add(codeProviderConfig);
        }
      }
    }
    catch (Throwable e)
    {
      throw new CodesServiceException("Failed to read the code provider configuration files", e);
    }
  }
}
