/*
 * Copyright 2018 Marcus Portmann
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

package digital.inception.codes.test;

//~--- non-JDK imports --------------------------------------------------------

import digital.inception.codes.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

//~--- JDK imports ------------------------------------------------------------

/**
 * The <code>TestCodeProvider</code> class.
 *
 * @author Marcus Portmann
 */
@SuppressWarnings("unused")
public class TestCodeProvider
  implements ICodeProvider
{
  private CodeCategory codeCategory;
  private List<Code> codes;

  /**
   * Constructs a new <code>TestCodeProvider</code>.
   *
   * @param codeProviderConfig the configuration for the code provider
   */
  public TestCodeProvider(CodeProviderConfig codeProviderConfig)
  {
    codeCategory = new CodeCategory(UUID.fromString("887d7963-284e-400c-b5ae-e10b3d5297f0"),
        "Test Code Category Name", LocalDateTime.now());

    codes = new ArrayList<>();

    codes.add(new Code(UUID.randomUUID().toString(), codeCategory.getId(), "Test Code Name 1",
        "Test Code Value 1"));
    codes.add(new Code(UUID.randomUUID().toString(), codeCategory.getId(), "Test Code Name 2",
        "Test Code Value 2"));
    codes.add(new Code(UUID.randomUUID().toString(), codeCategory.getId(), "Test Code Name 3",
        "Test Code Value 3"));
  }

  /**
   * Returns whether the code provider supports the code category.
   *
   * @param codeCategoryId the Universally Unique Identifier (UUID) used to uniquely identify the
   *                       code category
   *
   * @return <code>true</code> if the code provider supports the code category or <code>false</code>
   *         otherwise
   */
  @Override
  public boolean codeCategoryExists(UUID codeCategoryId)
    throws CodeProviderException
  {
    try
    {
      return codeCategory.getId().equals(codeCategoryId);
    }
    catch (Throwable e)
    {
      throw new CodeProviderException(String.format(
          "Failed to check whether the code provider supports the code category (%s)",
          codeCategoryId), e);
    }
  }

  /**
   * Check whether the code exists.
   *
   * @param codeCategoryId the Universally Unique Identifier (UUID) used to uniquely identify the
   *                       code category
   * @param codeId         the ID used to uniquely identify the code
   *
   * @return <code>true</code> if the code exists or <code>false</code> otherwise
   */
  @Override
  public boolean codeExists(UUID codeCategoryId, String codeId)
    throws CodeProviderException
  {
    try
    {
      if (codeCategory.getId().equals(codeCategoryId))
      {
        for (Code code : codes)
        {
          if (code.getId().equals(codeId))
          {
            return true;
          }
        }
      }

      return false;
    }
    catch (Throwable e)
    {
      throw new CodeProviderException(String.format(
          "Failed to check whether the code (%s) exists for the code category (%s)", codeId,
          codeCategoryId), e);

    }
  }

  /**
   * Retrieve the code.
   *
   * @param codeCategoryId the Universally Unique Identifier (UUID) used to uniquely identify the
   *                       code category
   * @param codeId         the ID uniquely identifying the code
   *
   * @return the code
   */
  @Override
  public Code getCode(UUID codeCategoryId, String codeId)
    throws CodeNotFoundException, CodeProviderException
  {
    try
    {
      if (codeCategory.getId().equals(codeCategoryId))
      {
        for (Code code : codes)
        {
          if (code.getId().equals(codeId))
          {
            return code;
          }
        }
      }
    }
    catch (Throwable e)
    {
      throw new CodeProviderException(String.format(
          "Failed to retrieve the code (%s) for the code category (%s)", codeId, codeCategoryId),
          e);
    }

    throw new CodeNotFoundException(codeCategoryId, codeId);
  }

  /**
   * Returns all the code categories for the code provider.
   *
   * @return all the code categories for the code provider
   */
  @Override
  public List<CodeCategory> getCodeCategories()
    throws CodeProviderException
  {
    try
    {
      List<CodeCategory> codeCategories = new ArrayList<>();

      codeCategories.add(codeCategory);

      return codeCategories;
    }
    catch (Throwable e)
    {
      throw new CodeProviderException("Failed to retrieve code categories", e);
    }

  }

  /**
   * Retrieve the code category.
   *
   * @param codeCategoryId the Universally Unique Identifier (UUID) used to uniquely identify the
   *                       code category
   *
   * @return the code category
   */
  @Override
  public CodeCategory getCodeCategory(UUID codeCategoryId)
    throws CodeCategoryNotFoundException, CodeProviderException
  {
    try
    {
      if (codeCategory.getId().equals(codeCategoryId))
      {
        return codeCategory;
      }

      throw new CodeCategoryNotFoundException(codeCategoryId);
    }
    catch (CodeCategoryNotFoundException e)
    {
      throw e;
    }
    catch (Throwable e)
    {
      throw new CodeProviderException(String.format("Failed to retrieve the code category (%s)",
          codeCategoryId), e);
    }
  }

  /**
   * Returns the date and time the code category was last updated.
   *
   * @param codeCategoryId the Universally Unique Identifier (UUID) used to uniquely identify the
   *                       code category
   *
   * @return the date and time the code category was last updated
   */
  @Override
  public LocalDateTime getCodeCategoryLastUpdated(UUID codeCategoryId)
    throws CodeCategoryNotFoundException, CodeProviderException
  {
    try
    {
      if (codeCategory.getId().equals(codeCategoryId))
      {
        return codeCategory.getUpdated();
      }

      throw new CodeCategoryNotFoundException(codeCategoryId);
    }
    catch (CodeCategoryNotFoundException e)
    {
      throw e;
    }
    catch (Throwable e)
    {
      throw new CodeProviderException(String.format(
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
   * @param codeCategoryId the Universally Unique Identifier (UUID) used to uniquely identify the
   *                       code category
   *
   * @return the codes for the code category
   */
  @Override
  public List<Code> getCodesForCodeCategory(UUID codeCategoryId)
    throws CodeCategoryNotFoundException, CodeProviderException
  {
    try
    {
      if (codeCategory.getId().equals(codeCategoryId))
      {
        return codes;
      }

      throw new CodeCategoryNotFoundException(codeCategoryId);
    }
    catch (CodeCategoryNotFoundException e)
    {
      throw e;
    }
    catch (Throwable e)
    {
      throw new CodeProviderException(String.format(
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
   * @param codeCategoryId the Universally Unique Identifier (UUID) used to uniquely identify the
   *                       code category
   * @param parameters     the parameters
   *
   * @return the codes for the code category
   */
  @Override
  public List<Code> getCodesForCodeCategoryWithParameters(UUID codeCategoryId, Map<String,
      String> parameters)
    throws CodeCategoryNotFoundException, CodeProviderException
  {
    return getCodesForCodeCategory(codeCategoryId);
  }

  /**
   * Retrieve the XML or JSON data for the code category.
   * <p/>
   * NOTE: This will also attempt to retrieve the data from the appropriate code provider that has
   * been registered with the Codes Service in the <code>META-INF/code-providers.xml</code>
   * configuration file.
   *
   * @param codeCategoryId the Universally Unique Identifier (UUID) used to uniquely identify the code category
   *
   * @return the XML or JSON data for the code category
   */
  @Override
  public String getDataForCodeCategory(UUID codeCategoryId)
    throws CodeCategoryNotFoundException, CodeProviderException
  {
    try
    {
      if (codeCategory.getId().equals(codeCategoryId))
      {
        return "<codes><code name=\"name1\" value=\"value1\"/></codes>";
      }

      throw new CodeCategoryNotFoundException(codeCategoryId);
    }
    catch (CodeCategoryNotFoundException e)
    {
      throw e;
    }
    catch (Throwable e)
    {
      throw new CodeProviderException(String.format(
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
   * @param codeCategoryId the Universally Unique Identifier (UUID) used to uniquely identify the
   *                       code category
   * @param parameters     the parameters
   *
   * @return the XML or JSON data for the code category
   */
  @Override
  public String getDataForCodeCategoryWithParameters(UUID codeCategoryId, Map<String,
      String> parameters)
    throws CodeCategoryNotFoundException, CodeProviderException
  {
    return getDataForCodeCategory(codeCategoryId);
  }
}
