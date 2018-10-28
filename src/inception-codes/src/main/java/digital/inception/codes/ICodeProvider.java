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

package digital.inception.codes;

//~--- JDK imports ------------------------------------------------------------

import java.time.LocalDateTime;

import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * The <code>ICodeProvider</code> interface defines the interface that must be implemented by all
 * custom code providers.
 *
 * @author Marcus Portmann
 */
public interface ICodeProvider
{
  /**
   * Returns whether the code provider supports the code category.
   *
   * @param codeCategoryId the Universally Unique Identifier (UUID) used to uniquely identify the
   *                       code category
   *
   * @return <code>true</code> if the code provider supports the code category or <code>false</code>
   *         otherwise
   */
  boolean codeCategoryExists(UUID codeCategoryId)
    throws CodeProviderException;

  /**
   * Check whether the code exists.
   *
   * @param codeCategoryId the Universally Unique Identifier (UUID) used to uniquely identify the
   *                       code ategory
   * @param codeId         the ID used to uniquely identify the code
   *
   * @return <code>true</code> if the code exists or <code>false</code> otherwise
   */
  boolean codeExists(UUID codeCategoryId, String codeId)
    throws CodeProviderException;

  /**
   * Retrieve the code.
   *
   * @param codeCategoryId the Universally Unique Identifier (UUID) used to uniquely identify the
   *                       code category
   * @param codeId         the ID uniquely identifying the code
   *
   * @return the code
   */
  Code getCode(UUID codeCategoryId, String codeId)
    throws CodeNotFoundException, CodeProviderException;

  /**
   * Returns all the code categories for the code provider.
   *
   * @return all the code categories for the code provider
   */
  List<CodeCategory> getCodeCategories()
    throws CodeProviderException;

  /**
   * Retrieve the code category.
   *
   * @param codeCategoryId the Universally Unique Identifier (UUID) used to uniquely identify the
   *                       code category
   *
   * @return the code category
   */
  CodeCategory getCodeCategory(UUID codeCategoryId)
    throws CodeCategoryNotFoundException, CodeProviderException;

  /**
   * Returns the date and time the code category was last updated.
   *
   * @param codeCategoryId the Universally Unique Identifier (UUID) used to uniquely identify the
   *                       code category
   *
   * @return the date and time the code category was last updated
   */
  LocalDateTime getCodeCategoryLastUpdated(UUID codeCategoryId)
    throws CodeCategoryNotFoundException, CodeProviderException;

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
  List<Code> getCodesForCodeCategory(UUID codeCategoryId)
    throws CodeCategoryNotFoundException, CodeProviderException;

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
  List<Code> getCodesForCodeCategoryWithParameters(UUID codeCategoryId, Map<String,
      String> parameters)
    throws CodeCategoryNotFoundException, CodeProviderException;

  /**
   * Retrieve the XML or JSON data for the code category.
   * <p/>
   * NOTE: This will also attempt to retrieve the data from the appropriate code provider that has
   * been registered with the Codes Service in the <code>META-INF/code-providers.xml</code>
   * configuration file.
   *
   * @param codeCategoryId the Universally Unique Identifier (UUID) used to uniquely identify the
   *                       code category
   *
   * @return the XML or JSON data for the code category
   */
  String getDataForCodeCategory(UUID codeCategoryId)
    throws CodeCategoryNotFoundException, CodeProviderException;

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
  String getDataForCodeCategoryWithParameters(UUID codeCategoryId, Map<String, String> parameters)
    throws CodeCategoryNotFoundException, CodeProviderException;
}
