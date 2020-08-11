/*
 * Copyright 2020 Marcus Portmann
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

// ~--- JDK imports ------------------------------------------------------------

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * The <code>ICodesService</code> interface defines the functionality provided by a Codes Service
 * implementation.
 *
 * @author Marcus Portmann
 */
public interface ICodesService {

  /**
   * Check whether the code category exists.
   *
   * @param codeCategoryId the ID uniquely identifying the code category
   *
   * @return <code>true</code> if the code category exists or <code>false</code> otherwise
   */
  boolean codeCategoryExists(String codeCategoryId) throws CodesServiceException;

  /**
   * Check whether the code exists.
   *
   * @param codeCategoryId the ID uniquely identifying the code category the code is associated
   *                       with
   * @param codeId         the ID uniquely identifying the code
   *
   * @return <code>true</code> if the code exists or <code>false</code> otherwise
   */
  boolean codeExists(String codeCategoryId, String codeId) throws CodesServiceException;

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
   * @param codeCategoryId the ID uniquely identifying the code category the code is associated
   *                       with
   * @param codeId         the ID uniquely identifying the code
   */
  void deleteCode(String codeCategoryId, String codeId)
      throws CodeNotFoundException, CodesServiceException;

  /**
   * Delete the code category.
   *
   * @param codeCategoryId the ID uniquely identifying the code category
   */
  void deleteCodeCategory(String codeCategoryId)
      throws CodeCategoryNotFoundException, CodesServiceException;

  /**
   * Retrieve the code.
   *
   * @param codeCategoryId the ID uniquely identifying the code category the code is associated
   *                       with
   * @param codeId         the ID uniquely identifying the code
   *
   * @return the code
   */
  Code getCode(String codeCategoryId, String codeId)
      throws CodeNotFoundException, CodesServiceException;

  /**
   * Returns all the code categories.
   *
   * @return all the code categories
   */
  List<CodeCategory> getCodeCategories() throws CodesServiceException;

  /**
   * Retrieve the code category.
   *
   * @param codeCategoryId the ID uniquely identifying the code category
   *
   * @return the code category
   */
  CodeCategory getCodeCategory(String codeCategoryId)
      throws CodeCategoryNotFoundException, CodesServiceException;

  /**
   * Retrieve the XML or JSON data for the code category.
   *
   * <p>NOTE: This will also attempt to retrieve the data from the appropriate code provider that
   * has been registered with the Codes Service in the <code>META-INF/code-providers.xml</code>
   * configuration file.
   *
   * @param codeCategoryId the ID uniquely identifying the code category
   *
   * @return the XML or JSON data for the code category
   */
  String getCodeCategoryData(String codeCategoryId)
      throws CodeCategoryNotFoundException, CodesServiceException;

  /**
   * Retrieve the XML or JSON data for the code category using the specified parameters.
   *
   * <p>NOTE: This will also attempt to retrieve the data from the appropriate code provider that
   * has been registered with the Codes Service in the <code>META-INF/code-providers.xml</code>
   * configuration file.
   *
   * @param codeCategoryId the ID uniquely identifying the code category
   * @param parameters     the parameters
   *
   * @return the XML or JSON data for the code category
   */
  String getCodeCategoryDataWithParameters(String codeCategoryId, Map<String, String> parameters)
      throws CodeCategoryNotFoundException, CodesServiceException;

  /**
   * Retrieve the name of the code category.
   *
   * @param codeCategoryId the ID uniquely identifying the code category
   *
   * @return the name of the code category
   */
  String getCodeCategoryName(String codeCategoryId)
      throws CodeCategoryNotFoundException, CodesServiceException;

  /**
   * Returns the summaries for all the code categories.
   *
   * @return the summaries for all the code categories
   */
  List<CodeCategorySummary> getCodeCategorySummaries() throws CodesServiceException;

  /**
   * Returns the date and time the code category was last updated.
   *
   * @param codeCategoryId the ID uniquely identifying the code category
   *
   * @return the date and time the code category was last updated
   */
  LocalDateTime getCodeCategoryUpdated(String codeCategoryId)
      throws CodeCategoryNotFoundException, CodesServiceException;

  /**
   * Retrieve the name of the code.
   *
   * @param codeCategoryId the ID uniquely identifying the code category the code is associated
   *                       with
   * @param codeId         the ID uniquely identifying the code
   *
   * @return the name of the code
   */
  String getCodeName(String codeCategoryId, String codeId)
      throws CodeNotFoundException, CodesServiceException;

  /**
   * Retrieve the codes for the code category.
   *
   * <p>NOTE: This will also attempt to retrieve the codes from the appropriate code provider that
   * has been registered with the Codes Service in the <code>META-INF/code-providers.xml</code>
   * configuration file.
   *
   * @param codeCategoryId the ID uniquely identifying the code category
   *
   * @return the codes for the code category
   */
  List<Code> getCodesForCodeCategory(String codeCategoryId)
      throws CodeCategoryNotFoundException, CodesServiceException;

  /**
   * Retrieve the codes for the code category using the specified parameters.
   *
   * <p>NOTE: This will also attempt to retrieve the codes from the appropriate code provider that
   * has been registered with the Codes Service in the <code>META-INF/code-providers.xml</code>
   * configuration file.
   *
   * @param codeCategoryId the ID uniquely identifying the code category
   * @param parameters     the parameters
   *
   * @return the codes for the code category
   */
  List<Code> getCodesForCodeCategoryWithParameters(
      String codeCategoryId, Map<String, String> parameters)
      throws CodeCategoryNotFoundException, CodesServiceException;

  /**
   * Update the existing code.
   *
   * @param code the <code>Code</code> instance containing the updated information for the code
   */
  void updateCode(Code code) throws CodeNotFoundException, CodesServiceException;

  /**
   * Update the existing code category.
   *
   * @param codeCategory the <code>CodeCategory</code> instance containing the updated information
   *                     for the code category
   */
  void updateCodeCategory(CodeCategory codeCategory)
      throws CodeCategoryNotFoundException, CodesServiceException;

  /**
   * Update the XML or JSON data for the code category.
   *
   * @param codeCategoryId the ID uniquely identifying the code category
   * @param data           the updated XML or JSON data
   */
  void updateCodeCategoryData(String codeCategoryId, String data)
      throws CodeCategoryNotFoundException, CodesServiceException;
}
