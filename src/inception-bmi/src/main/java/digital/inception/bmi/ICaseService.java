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

package digital.inception.bmi;

// ~--- JDK imports ------------------------------------------------------------

import java.util.List;

/**
 * The <code>ICaseService</code> interface defines the functionality provided by a Case Service
 * implementation.
 *
 * @author Marcus Portmann
 */
@SuppressWarnings("unused")
public interface ICaseService {

  /**
   * Check whether the case definition exists.
   *
   * @param caseDefinitionId the ID uniquely identifying the case definition
   *
   * @return <code>true</code> if the case definition exists or <code>false</code> otherwise
   */
  boolean caseDefinitionExists(String caseDefinitionId) throws CaseServiceException;

  //  /**
  //   * Delete the existing case definition.
  //   *
  //   * @param caseDefinitionId the ID uniquely identifying the case definition
  //   */
  //  void deleteCaseDefinition(String caseDefinitionId)
  //    throws CaseDefinitionNotFoundException, CaseServiceException;

  //  /**
  //   * Retrieve the case definition.
  //   *
  //   * @param caseDefinitionId the ID uniquely identifying the case definition
  //   *
  //   * @return the case definition
  //   */
  //  CaseDefinition getCaseDefinition(String caseDefinitionId)
  //    throws CaseDefinitionNotFoundException, CaseServiceException;

  //  /**
  //   * Retrieve the name of the case definition.
  //   *
  //   * @param caseDefinitionId the ID uniquely identifying the case definition
  //   *
  //   * @return the name of the case definition
  //   */
  //  String getCaseDefinitionName(String caseDefinitionId)
  //    throws CaseDefinitionNotFoundException, CaseServiceException;

  /**
   * Create the new case definition(s).
   *
   * @param caseDefinitionData the CMMN XML data for the case definition(s)
   *
   * @return the case definition summaries for the CMMN cases defined by the CMMN XML data
   */
  List<CaseDefinitionSummary> createCaseDefinition(byte[] caseDefinitionData)
      throws InvalidCMMNException, DuplicateCaseDefinitionException, CaseServiceException;

  //  /**
  //   * Retrieve the summary for the case definition.
  //   *
  //   * @param caseDefinitionId the ID uniquely identifying the case definition
  //   *
  //   * @return the summary for the case definition
  //   */
  //  CaseDefinitionSummary getCaseDefinitionSummary(String caseDefinitionId)
  //    throws CaseDefinitionNotFoundException, CaseServiceException;
  //
  //  /**
  //   * Returns all the case definitions.
  //   *
  //   * @return all the case definitions
  //   */
  //  List<CaseDefinition> getCaseDefinitions()
  //    throws CaseServiceException;

  /**
   * Returns the summaries for all the case definitions.
   *
   * @return the summaries for all the case definitions
   */
  List<CaseDefinitionSummary> getCaseDefinitionSummaries() throws CaseServiceException;

  /**
   * Update the case definition(s).
   *
   * @param caseDefinitionData the CMMN XML data for the case definition(s)
   *
   * @return the case definition summaries for the CMMN cases defined by the CMMN XML data
   */
  List<CaseDefinitionSummary> updateCaseDefinition(byte[] caseDefinitionData)
      throws InvalidCMMNException, CaseDefinitionNotFoundException, CaseServiceException;

  /**
   * Validate the CMMN XML data.
   *
   * @param cmmnXml the CMMN XML data
   *
   * @return the case definition summaries for the CMMN cases if the CMMN XML data was successfully
   * validated
   */
  List<CaseDefinitionSummary> validateCMMN(byte[] cmmnXml)
      throws InvalidCMMNException, CaseServiceException;

  /**
   * Validate the CMMN XML data.
   *
   * @param cmmnXml the CMMN XML data
   *
   * @return the case definition summaries for the CMMN cases if the CMMN XML data was successfully
   * validated
   */
  List<CaseDefinitionSummary> validateCMMN(String cmmnXml)
      throws InvalidCMMNException, CaseServiceException;
}
