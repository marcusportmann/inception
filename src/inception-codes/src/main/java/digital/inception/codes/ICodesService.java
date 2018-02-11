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
 * The <code>ICodesService</code> interface defines the functionality provided by a Codes Service
 * implementation.
 *
 * @author Marcus Portmann
 */
public interface ICodesService
{
  /**
   * Check whether the code category exists.
   *
   * @param codeCategoryId the Universally Unique Identifier (UUID) used to uniquely identify the
   *                       code category
   *
   * @return <code>true</code> if the code category exists or <code>false</code> otherwise
   */
  boolean codeCategoryExists(UUID codeCategoryId)
    throws CodesServiceException;

  /**
   * Check whether the code exists.
   *
   * @param codeCategoryId the Universally Unique Identifier (UUID) used to uniquely identify the
   *                       code category
   * @param codeId         the ID used to uniquely identify the code
   *
   * @return <code>true</code> if the code exists or <code>false</code> otherwise
   */
  boolean codeExists(UUID codeCategoryId, String codeId)
    throws CodesServiceException;

  /**
   * Create the new code.
   *
   * @param code the <code>Code</code> instance containing the information for the new code
   */
  void createCode(Code code)
    throws DuplicateCodeException, CodeCategoryNotFoundException, CodesServiceException;

  /**
   * Create the new code category.
   *
   * @param codeCategory the <code>CodeCategory</code> instance containing the information for the
   *                     new code category
   */
  void createCodeCategory(CodeCategory codeCategory)
    throws DuplicateCodeCategoryException, CodesServiceException;

  /**
   * Delete the code.
   *
   * @param codeCategoryId the Universally Unique Identifier (UUID) used to uniquely identify the
   *                       code category
   * @param codeId         the ID uniquely identifying the code
   */
  void deleteCode(UUID codeCategoryId, String codeId)
    throws CodeNotFoundException, CodesServiceException;

  /**
   * Delete the code category.
   *
   * @param codeCategoryId the Universally Unique Identifier (UUID) used to uniquely identify the
   *                       code category
   */
  void deleteCodeCategory(UUID codeCategoryId)
    throws CodeCategoryNotFoundException, CodesServiceException;

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
    throws CodeNotFoundException, CodesServiceException;

  /**
   * Returns all the code categories.
   *
   * @return all the code categories
   */
  List<CodeCategory> getCodeCategories()
    throws CodesServiceException;

  /**
   * Retrieve the code category.
   *
   * @param codeCategoryId the Universally Unique Identifier (UUID) used to uniquely identify the
   *                       code category
   *
   * @return the code category
   */
  CodeCategory getCodeCategory(UUID codeCategoryId)
    throws CodeCategoryNotFoundException, CodesServiceException;

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
  List<Code> getCodeCategoryCodes(UUID codeCategoryId)
    throws CodeCategoryNotFoundException, CodesServiceException;

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
  List<Code> getCodeCategoryCodesWithParameters(UUID codeCategoryId, Map<String, String> parameters)
    throws CodeCategoryNotFoundException, CodesServiceException;

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
  String getCodeCategoryData(UUID codeCategoryId)
    throws CodeCategoryNotFoundException, CodesServiceException;

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
  String getCodeCategoryDataWithParameters(UUID codeCategoryId, Map<String, String> parameters)
    throws CodeCategoryNotFoundException, CodesServiceException;

  /**
   * Returns the date and time the code category was last updated.
   *
   * @param codeCategoryId the Universally Unique Identifier (UUID) used to uniquely identify the
   *                       code category
   *
   * @return the date and time the code category was last updated
   */
  LocalDateTime getCodeCategoryUpdated(UUID codeCategoryId)
    throws CodeCategoryNotFoundException, CodesServiceException;

  /**
   * Returns the number of code categories.
   *
   * @return the number of code categories
   */
  int getNumberOfCodeCategories()
    throws CodesServiceException;

  /**
   * Returns the number of codes for the code category.
   *
   * @param codeCategoryId the Universally Unique Identifier (UUID) used to uniquely identify the
   *                       code category
   *
   * @return the number of codes for the code category
   */
  int getNumberOfCodesForCodeCategory(UUID codeCategoryId)
    throws CodeCategoryNotFoundException, CodesServiceException;

  /**
   * Update the existing code.
   *
   * @param code the <code>Code</code> instance containing the updated information for the code
   *
   * @return the updated code
   */
  Code updateCode(Code code)
    throws CodeNotFoundException, CodesServiceException;

  /**
   * Update the existing code category.
   *
   * @param codeCategory the <code>CodeCategory</code> instance containing the updated information
   *                     for the code category
   *
   * @return the updated code category
   */
  CodeCategory updateCodeCategory(CodeCategory codeCategory)
    throws CodeCategoryNotFoundException, CodesServiceException;

  /**
   * Update the XML or JSON data for the code category.
   *
   * @param codeCategoryId the Universally Unique Identifier (UUID) used to uniquely identify the
   *                       code category
   * @param data           the updated XML or JSON data
   */
  void updateCodeCategoryData(UUID codeCategoryId, String data)
    throws CodeCategoryNotFoundException, CodesServiceException;
}
