/*
 * Copyright Marcus Portmann
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

package digital.inception.codes.service;

import digital.inception.codes.exception.CodeCategoryNotFoundException;
import digital.inception.codes.exception.CodeNotFoundException;
import digital.inception.codes.exception.DuplicateCodeCategoryException;
import digital.inception.codes.exception.DuplicateCodeException;
import digital.inception.codes.model.Code;
import digital.inception.codes.model.CodeCategory;
import digital.inception.codes.model.CodeCategorySummary;
import digital.inception.core.exception.InvalidArgumentException;
import digital.inception.core.exception.ServiceUnavailableException;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;

/**
 * The {@code CodesService} interface defines the functionality provided by a Codes Service
 * implementation.
 *
 * @author Marcus Portmann
 */
public interface CodesService {

  /**
   * Check whether the code category exists.
   *
   * @param codeCategoryId the ID for the code category
   * @return {@code true} if the code category exists or {@code false} otherwise
   * @throws InvalidArgumentException if an argument is invalid
   * @throws ServiceUnavailableException if the check for the existing code category failed
   */
  boolean codeCategoryExists(String codeCategoryId)
      throws InvalidArgumentException, ServiceUnavailableException;

  /**
   * Check whether the code exists.
   *
   * @param codeCategoryId the ID for the code category the code is associated with
   * @param codeId the ID for the code
   * @return {@code true} if the code exists or {@code false} otherwise
   * @throws InvalidArgumentException if an argument is invalid
   * @throws ServiceUnavailableException if the check for the existing code failed
   */
  boolean codeExists(String codeCategoryId, String codeId)
      throws InvalidArgumentException, ServiceUnavailableException;

  /**
   * Create the code.
   *
   * @param code the {@code Code} instance containing the information for the new code
   * @throws InvalidArgumentException if an argument is invalid
   * @throws DuplicateCodeException if the code already exists
   * @throws CodeCategoryNotFoundException if the code category could not be found
   * @throws ServiceUnavailableException if the code could not be created
   */
  void createCode(Code code)
      throws InvalidArgumentException,
          DuplicateCodeException,
          CodeCategoryNotFoundException,
          ServiceUnavailableException;

  /**
   * Create the code category.
   *
   * @param codeCategory the {@code CodeCategory} instance containing the information for the new
   *     code category
   * @throws InvalidArgumentException if an argument is invalid
   * @throws DuplicateCodeCategoryException if the code category already exists
   * @throws ServiceUnavailableException if the code category could not be created
   */
  void createCodeCategory(CodeCategory codeCategory)
      throws InvalidArgumentException, DuplicateCodeCategoryException, ServiceUnavailableException;

  /**
   * Delete the code.
   *
   * @param codeCategoryId the ID for the code category the code is associated with
   * @param codeId the ID for the code
   * @throws InvalidArgumentException if an argument is invalid
   * @throws CodeNotFoundException if the code could not be found
   * @throws ServiceUnavailableException if the code could not be deleted
   */
  void deleteCode(String codeCategoryId, String codeId)
      throws InvalidArgumentException, CodeNotFoundException, ServiceUnavailableException;

  /**
   * Delete the code category.
   *
   * @param codeCategoryId the ID for the code category
   * @throws InvalidArgumentException if an argument is invalid
   * @throws CodeCategoryNotFoundException if the code category could not be found
   * @throws ServiceUnavailableException if the code category could not be deleted
   */
  void deleteCodeCategory(String codeCategoryId)
      throws InvalidArgumentException, CodeCategoryNotFoundException, ServiceUnavailableException;

  /**
   * Retrieve the code.
   *
   * @param codeCategoryId the ID for the code category the code is associated with
   * @param codeId the ID for the code
   * @return the code
   * @throws InvalidArgumentException if an argument is invalid
   * @throws CodeNotFoundException if the code could not be found
   * @throws ServiceUnavailableException if the code could not be retrieved
   */
  Code getCode(String codeCategoryId, String codeId)
      throws InvalidArgumentException, CodeNotFoundException, ServiceUnavailableException;

  /**
   * Retrieve all the code categories.
   *
   * @return the code categories
   * @throws ServiceUnavailableException if the code categories could not be retrieved
   */
  List<CodeCategory> getCodeCategories() throws ServiceUnavailableException;

  /**
   * Retrieve the code category.
   *
   * @param codeCategoryId the ID for the code category
   * @return the code category
   * @throws InvalidArgumentException if an argument is invalid
   * @throws CodeCategoryNotFoundException if the code category could not be found
   * @throws ServiceUnavailableException if the code category could not be retrieved
   */
  CodeCategory getCodeCategory(String codeCategoryId)
      throws InvalidArgumentException, CodeCategoryNotFoundException, ServiceUnavailableException;

  /**
   * Retrieve the XML or JSON data for the code category.
   *
   * <p>This will also attempt to retrieve the data from the appropriate code provider that has been
   * registered with the Codes Service in the {@code META-INF/code-providers.xml} configuration
   * file.
   *
   * @param codeCategoryId the ID for the code category
   * @return the XML or JSON data for the code category
   * @throws InvalidArgumentException if an argument is invalid
   * @throws CodeCategoryNotFoundException if the code category could not be found
   * @throws ServiceUnavailableException if the code category data could not be retrieved
   */
  String getCodeCategoryData(String codeCategoryId)
      throws InvalidArgumentException, CodeCategoryNotFoundException, ServiceUnavailableException;

  /**
   * Retrieve the XML or JSON data for the code category using the specified parameters.
   *
   * <p>This will also attempt to retrieve the data from the appropriate code provider that has been
   * registered with the Codes Service in the {@code META-INF/code-providers.xml} configuration
   * file.
   *
   * @param codeCategoryId the ID for the code category
   * @param parameters the parameters
   * @return the XML or JSON data for the code category
   * @throws InvalidArgumentException if an argument is invalid
   * @throws CodeCategoryNotFoundException if the code category could not be found
   * @throws ServiceUnavailableException if the code category data could not be retrieved
   */
  String getCodeCategoryDataWithParameters(String codeCategoryId, Map<String, String> parameters)
      throws InvalidArgumentException, CodeCategoryNotFoundException, ServiceUnavailableException;

  /**
   * Returns the date and time the code category was last modified.
   *
   * @param codeCategoryId the ID for the code category
   * @return the date and time the code category was last modified
   * @throws InvalidArgumentException if an argument is invalid
   * @throws CodeCategoryNotFoundException if the code category could not be found
   * @throws ServiceUnavailableException if the date and time the code category was last modified
   *     could not be retrieved
   */
  OffsetDateTime getCodeCategoryLastModified(String codeCategoryId)
      throws InvalidArgumentException, CodeCategoryNotFoundException, ServiceUnavailableException;

  /**
   * Retrieve the name of the code category.
   *
   * @param codeCategoryId the ID for the code category
   * @return the name of the code category
   * @throws InvalidArgumentException if an argument is invalid
   * @throws CodeCategoryNotFoundException if the code category could not be found
   * @throws ServiceUnavailableException if the name of the code category could not be retrieved
   */
  String getCodeCategoryName(String codeCategoryId)
      throws InvalidArgumentException, CodeCategoryNotFoundException, ServiceUnavailableException;

  /**
   * Retrieve the summaries for all the code categories.
   *
   * @return the summaries for the code categories
   * @throws ServiceUnavailableException if the code category summaries could not be retrieved
   */
  List<CodeCategorySummary> getCodeCategorySummaries() throws ServiceUnavailableException;

  /**
   * Retrieve the name of the code.
   *
   * @param codeCategoryId the ID for the code category the code is associated with
   * @param codeId the ID for the code
   * @return the name of the code
   * @throws InvalidArgumentException if an argument is invalid
   * @throws CodeNotFoundException if the code could not be found
   * @throws ServiceUnavailableException if the name of the code could not be retrieved
   */
  String getCodeName(String codeCategoryId, String codeId)
      throws InvalidArgumentException, CodeNotFoundException, ServiceUnavailableException;

  /**
   * Retrieve the codes for the code category.
   *
   * <p>This will also attempt to retrieve the codes from the appropriate code provider that has
   * been registered with the Codes Service in the {@code META-INF/code-providers.xml} configuration
   * file.
   *
   * @param codeCategoryId the ID for the code category
   * @return the codes for the code category
   * @throws InvalidArgumentException if an argument is invalid
   * @throws CodeCategoryNotFoundException if the code category could not be found
   * @throws ServiceUnavailableException if the codes for the code category could not be retrieved
   */
  List<Code> getCodesForCodeCategory(String codeCategoryId)
      throws InvalidArgumentException, CodeCategoryNotFoundException, ServiceUnavailableException;

  /**
   * Retrieve the codes for the code category using the specified parameters.
   *
   * <p>This will also attempt to retrieve the codes from the appropriate code provider that has
   * been registered with the Codes Service in the {@code META-INF/code-providers.xml} configuration
   * file.
   *
   * @param codeCategoryId the ID for the code category
   * @param parameters the parameters
   * @return the codes for the code category
   * @throws InvalidArgumentException if an argument is invalid
   * @throws CodeCategoryNotFoundException if the code category could not be found
   * @throws ServiceUnavailableException if the codes for the code category could not be retrieved
   */
  List<Code> getCodesForCodeCategoryWithParameters(
      String codeCategoryId, Map<String, String> parameters)
      throws InvalidArgumentException, CodeCategoryNotFoundException, ServiceUnavailableException;

  /**
   * Update the existing code.
   *
   * @param code the {@code Code} instance containing the updated information for the code
   * @throws InvalidArgumentException if an argument is invalid
   * @throws CodeNotFoundException if the code could not be found
   * @throws ServiceUnavailableException if the code could not be updated
   */
  void updateCode(Code code)
      throws InvalidArgumentException, CodeNotFoundException, ServiceUnavailableException;

  /**
   * Update the existing code category.
   *
   * @param codeCategory the {@code CodeCategory} instance containing the updated information for
   *     the code category
   * @throws InvalidArgumentException if an argument is invalid
   * @throws CodeCategoryNotFoundException if the code category could not be found
   * @throws ServiceUnavailableException if the code category could not be updated
   */
  void updateCodeCategory(CodeCategory codeCategory)
      throws InvalidArgumentException, CodeCategoryNotFoundException, ServiceUnavailableException;

  /**
   * Update the XML or JSON data for the code category.
   *
   * @param codeCategoryId the ID for the code category
   * @param data the updated XML or JSON data
   * @throws InvalidArgumentException if an argument is invalid
   * @throws CodeCategoryNotFoundException if the code category could not be found
   * @throws ServiceUnavailableException if the code category data could not be updated
   */
  void updateCodeCategoryData(String codeCategoryId, String data)
      throws InvalidArgumentException, CodeCategoryNotFoundException, ServiceUnavailableException;
}
