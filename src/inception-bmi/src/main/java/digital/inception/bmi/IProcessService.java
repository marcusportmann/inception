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
import java.util.Map;

/**
 * The <code>IProcessService</code> interface defines the functionality provided by a Process
 * Service implementation.
 *
 * @author Marcus Portmann
 */
@SuppressWarnings("unused")
public interface IProcessService {

  /**
   * Create the new process definition(s).
   *
   * @param processDefinitionData the BPMN XML data for the process definition(s)
   *
   * @return the process definition summaries for the BPMN processes defined by the BPMN XML data
   */
  List<ProcessDefinitionSummary> createProcessDefinition(byte[] processDefinitionData)
      throws InvalidBPMNException, DuplicateProcessDefinitionException, ProcessServiceException;

  /// **
  // * Delete the existing process definition.
  // *
  // * @param processDefinitionId the ID uniquely identifying the process definition
  // */
  // void deleteProcessDefinition(String processDefinitionId)
  //  throws ProcessDefinitionNotFoundException, ProcessServiceException;

  /// **
  // * Retrieve the process definition.
  // *
  // * @param processDefinitionId the ID uniquely identifying the process definition
  // *
  // * @return the process definition
  // */
  // ProcessDefinition getProcessDefinition(String processDefinitionId)
  //  throws ProcessDefinitionNotFoundException, ProcessServiceException;

  /// **
  // * Retrieve the name of the process definition.
  // *
  // * @param processDefinitionId the ID uniquely identifying the process definition
  // *
  // * @return the name of the process definition
  // */
  // String getProcessDefinitionName(String processDefinitionId)
  //  throws ProcessDefinitionNotFoundException, ProcessServiceException;

  /**
   * Returns the summaries for all the process definitions.
   *
   * @return the summaries for all the process definitions
   */
  List<ProcessDefinitionSummary> getProcessDefinitionSummaries() throws ProcessServiceException;

  /**
   * Check whether the process definition exists.
   *
   * @param processDefinitionId the ID uniquely identifying the process definition
   *
   * @return <code>true</code> if the process definition exists or <code>false</code> otherwise
   */
  boolean processDefinitionExists(String processDefinitionId) throws ProcessServiceException;

  /// **
  // * Retrieve the summary for the process definition.
  // *
  // * @param processDefinitionId the ID uniquely identifying the process definition
  // *
  // * @return the summary for the process definition
  // */
  // ProcessDefinitionSummary getProcessDefinitionSummary(String processDefinitionId)
  //  throws ProcessDefinitionNotFoundException, ProcessServiceException;
  //
  /// **
  // * Returns all the process definitions.
  // *
  // * @return all the process definitions
  // */
  // List<ProcessDefinition> getProcessDefinitions()
  //  throws ProcessServiceException;

  /**
   * Start a process instance.
   *
   * @param processDefinitionId the ID uniquely identifying the process definition
   * @param parameters          the parameters for the process instance
   */
  void startProcessInstance(String processDefinitionId, Map<String, Object> parameters)
      throws ProcessDefinitionNotFoundException, ProcessServiceException;

  /**
   * Update the process definition(s).
   *
   * @param processDefinitionData the BPMN XML data for the process definition(s)
   *
   * @return the process definition summaries for the BPMN processes defined by the BPMN XML data
   */
  List<ProcessDefinitionSummary> updateProcessDefinition(byte[] processDefinitionData)
      throws InvalidBPMNException, ProcessDefinitionNotFoundException, ProcessServiceException;

  /**
   * Validate the BPMN XML data.
   *
   * @param bpmnXml the BPMN XML data
   *
   * @return the process definition summaries for the BPMN processes if the BPMN XML data was
   * successfully validated
   */
  List<ProcessDefinitionSummary> validateBPMN(byte[] bpmnXml)
      throws InvalidBPMNException, ProcessServiceException;

  /**
   * Validate the BPMN XML data.
   *
   * @param bpmnXml the BPMN XML data
   *
   * @return the process definition summaries for the BPMN processes if the BPMN XML data was
   * successfully validated
   */
  List<ProcessDefinitionSummary> validateBPMN(String bpmnXml)
      throws InvalidBPMNException, ProcessServiceException;
}
