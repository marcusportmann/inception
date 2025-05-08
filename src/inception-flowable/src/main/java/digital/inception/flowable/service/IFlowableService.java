/// *
// * Copyright Marcus Portmann
// *
// * Licensed under the Apache License, Version 2.0 (the "License");
// * you may not use this file except in compliance with the License.
// * You may obtain a copy of the License at
// *
// *   http://www.apache.org/licenses/LICENSE-2.0
// *
// * Unless required by applicable law or agreed to in writing, software
// * distributed under the License is distributed on an "AS IS" BASIS,
// * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// * See the License for the specific language governing permissions and
// * limitations under the License.
// */
//
// package digital.inception.flowable.service;
//
// import digital.inception.core.service.InvalidArgumentException;
// import digital.inception.core.service.ServiceUnavailableException;
// import digital.inception.flowable.model.InvalidBPMNException;
// import digital.inception.flowable.model.ProcessDefinitionSummary;
// import java.util.List;
//
/// **
// * The {@code IFlowableService} interface defines the functionality provided by a Flowable Service
// * implementation.
// *
// * @author Marcus Portmann
// */
// @SuppressWarnings("unused")
// public interface IFlowableService {
//
//  //  /**
//  //   * Create the new process definition(s).
//  //   *
//  //   * @param processDefinitionData the BPMN XML data for the process definition(s)
//  //   * @return the process definition summaries for the BPMN processes defined by the BPMN XML
//  // data
//  //   * @throws InvalidArgumentException if an argument is invalid
//  //   * @throws InvalidBPMNException if the BPMN XML data for the process definition(s) is
// invalid
//  //   * @throws DuplicateProcessDefinitionException if the process definition already exists
//  //   * @throws ServiceUnavailableException if the process definition could not be created
//  //   */
//  //  List<ProcessDefinitionSummary> createProcessDefinition(byte[] processDefinitionData)
//  //      throws InvalidArgumentException, InvalidBPMNException,
//  // DuplicateProcessDefinitionException,
//  //          ServiceUnavailableException;
//  //
//  //  /**
//  //   * Delete the process definition.
//  //   *
//  //   * @param processDefinitionId the ID for the process definition
//  //   * @throws InvalidArgumentException if an argument is invalid
//  //   * @throws ProcessDefinitionNotFoundException if the process definition could not be found
//  //   * @throws ServiceUnavailableException if the process definition could not be deleted
//  //   */
//  //  void deleteProcessDefinition(String processDefinitionId)
//  //      throws InvalidArgumentException, ProcessDefinitionNotFoundException,
//  //          ServiceUnavailableException;
//  //
//  //  /// **
//  //  // * Retrieve the process definition.
//  //  // *
//  //  // * @param processDefinitionId the ID for the process definition
//  //  // *
//  //  // * @return the process definition
//  //  // */
//  //  // ProcessDefinition getProcessDefinition(String processDefinitionId)
//  //  //  throws ProcessDefinitionNotFoundException, ServiceUnavailableException;
//  //
//  //  /// **
//  //  // * Retrieve the name of the process definition.
//  //  // *
//  //  // * @param processDefinitionId the ID for the process definition
//  //  // *
//  //  // * @return the name of the process definition
//  //  // */
//  //  // String getProcessDefinitionName(String processDefinitionId)
//  //  //  throws ProcessDefinitionNotFoundException, ServiceUnavailableException;
//  //
//  //  /**
//  //   * Returns the summaries for all the process definitions.
//  //   *
//  //   * @return the summaries for all the process definitions
//  //   * @throws ServiceUnavailableException if the process definition summaries could not be
//  // retrieved
//  //   */
//  //  List<ProcessDefinitionSummary> getProcessDefinitionSummaries() throws
//  // ServiceUnavailableException;
//  //
//  //  /**
//  //   * Check whether the process definition exists.
//  //   *
//  //   * @param processDefinitionId the ID for the process definition
//  //   * @return {@code true} if the process definition exists or {@code false} otherwise
//  //   * @throws InvalidArgumentException if an argument is invalid
//  //   * @throws ServiceUnavailableException if the check for the existing process definition
// failed
//  //   */
//  //  boolean processDefinitionExists(String processDefinitionId)
//  //      throws InvalidArgumentException, ServiceUnavailableException;
//  //
//  //  /**
//  //   * Start a process instance.
//  //   *
//  //   * @param processDefinitionId the ID for the process definition
//  //   * @param parameters the parameters for the process instance
//  //   * @throws InvalidArgumentException if an argument is invalid
//  //   * @throws ProcessDefinitionNotFoundException if the process definition could not be found
//  //   * @throws ServiceUnavailableException if the process instance could not be started
//  //   */
//  //  void startProcessInstance(String processDefinitionId, Map<String, Object> parameters)
//  //      throws InvalidArgumentException, ProcessDefinitionNotFoundException,
//  //          ServiceUnavailableException;
//  //
//  //  /**
//  //   * Update the process definition(s).
//  //   *
//  //   * @param processDefinitionData the BPMN XML data for the process definition(s)
//  //   * @return the process definition summaries for the BPMN processes defined by the BPMN XML
//  // data
//  //   * @throws InvalidArgumentException if an argument is invalid
//  //   * @throws InvalidBPMNException if the BPMN XML data for the process definition(s) is
// invalid
//  //   * @throws ProcessDefinitionNotFoundException if the process definition could not be found
//  //   * @throws ServiceUnavailableException if the process definition could not be updated
//  //   */
//  //  List<ProcessDefinitionSummary> updateProcessDefinition(byte[] processDefinitionData)
//  //      throws InvalidArgumentException, InvalidBPMNException,
// ProcessDefinitionNotFoundException,
//  //          ServiceUnavailableException;
//
//  /**
//   * Validate the BPMN XML data.
//   *
//   * @param bpmnXml the BPMN XML data
//   * @return the process definition summaries for the BPMN processes if the BPMN XML data was
//   *     successfully validated
//   * @throws InvalidArgumentException if an argument is invalid
//   * @throws InvalidBPMNException if the BPMN XML data for the process definition(s) is invalid
//   * @throws ServiceUnavailableException if the BPMN XML data for the process definition(s) could
//   *     not be validated
//   */
//  List<ProcessDefinitionSummary> validateBPMN(byte[] bpmnXml)
//      throws InvalidArgumentException, InvalidBPMNException, ServiceUnavailableException;
//
//  /**
//   * Validate the BPMN XML data.
//   *
//   * @param bpmnXml the BPMN XML data
//   * @return the process definition summaries for the BPMN processes if the BPMN XML data was
//   *     successfully validated
//   * @throws InvalidArgumentException if an argument is invalid
//   * @throws InvalidBPMNException if the BPMN XML data for the process definition(s) is invalid
//   * @throws ServiceUnavailableException if the BPMN XML data for the process definition(s) could
//   *     not be validated
//   */
//  List<ProcessDefinitionSummary> validateBPMN(String bpmnXml)
//      throws InvalidArgumentException, InvalidBPMNException, ServiceUnavailableException;
// }
