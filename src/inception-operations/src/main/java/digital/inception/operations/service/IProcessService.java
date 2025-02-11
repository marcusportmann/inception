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

package digital.inception.operations.service;

import digital.inception.core.service.InvalidArgumentException;
import digital.inception.core.service.ServiceUnavailableException;
import digital.inception.operations.model.Process;
import digital.inception.operations.model.ProcessDefinition;
import digital.inception.operations.model.ProcessDefinitionNotFoundException;
import digital.inception.operations.model.ProcessDefinitionVersionNotFoundException;
import digital.inception.operations.model.ProcessNotFoundException;
import digital.inception.operations.model.CreateProcessRequest;
import digital.inception.operations.model.Document;
import digital.inception.operations.model.DocumentDefinition;
import digital.inception.operations.model.DocumentDefinitionNotFoundException;
import digital.inception.operations.model.DocumentNotFoundException;
import digital.inception.operations.model.DuplicateProcessDefinitionException;
import digital.inception.operations.model.DuplicateDocumentDefinitionException;
import digital.inception.operations.model.DuplicateDocumentException;
import digital.inception.operations.model.UpdateProcessRequest;
import java.time.OffsetDateTime;
import java.util.UUID;

/**
 * The <b>IProcessService</b> interface defines the functionality provided by a Process Service
 * implementation.
 *
 * @author Marcus Portmann
 */
public interface IProcessService {

  /**
   * Check whether the process definition exists.
   *
   * @param processDefinitionId the ID for the process definition
   * @return <b>true</b> if the process definition exists or <b>false</b> otherwise
   * @throws InvalidArgumentException if an argument is invalid
   * @throws ServiceUnavailableException if the check for the process definition failed
   */
  boolean processDefinitionExists(String processDefinitionId)
      throws InvalidArgumentException, ServiceUnavailableException;

  /**
   * Check whether the process definition version exists.
   *
   * @param processDefinitionId the ID for the process definition
   * @param processDefinitionVersion the version for the process definition
   * @return <b>true</b> if the process definition version exists or <b>false</b> otherwise
   * @throws InvalidArgumentException if an argument is invalid
   * @throws ServiceUnavailableException if the check for the process definition version failed
   */
  boolean processDefinitionVersionExists(String processDefinitionId, int processDefinitionVersion)
      throws InvalidArgumentException, ServiceUnavailableException;

  /**
   * Create a new process.
   *
   * @param createProcessRequest the request to create a process
   * @param createdBy the person or system requesting the process creation
   * @return the process
   * @throws InvalidArgumentException if an argument is invalid
   * @throws ProcessDefinitionNotFoundException if the process definition could not be found
   * @throws ServiceUnavailableException if the process could not be created
   */
  Process createProcess(CreateProcessRequest createProcessRequest, String createdBy)
      throws InvalidArgumentException, ProcessDefinitionNotFoundException, ServiceUnavailableException;

  /**
   * Create the new process definition.
   *
   * @param processDefinition the process definition
   * @throws InvalidArgumentException if an argument is invalid
   * @throws DuplicateProcessDefinitionException if the process definition already exists
   * @throws ServiceUnavailableException if the process definition could not be created
   */
  void createProcessDefinition(ProcessDefinition processDefinition)
      throws InvalidArgumentException,
      DuplicateProcessDefinitionException,
      ServiceUnavailableException;

  /**
   * Delete the process.
   *
   * @param processId the ID for the process
   * @throws InvalidArgumentException if an argument is invalid
   * @throws ProcessNotFoundException if the process could not be found
   * @throws ServiceUnavailableException if the process could not be deleted
   */
  void deleteProcess(UUID processId)
      throws InvalidArgumentException, ProcessNotFoundException, ServiceUnavailableException;

  /**
   * Delete all versions of the process definition.
   *
   * @param processDefinitionId the ID for the process definition
   * @throws InvalidArgumentException if an argument is invalid
   * @throws ProcessDefinitionNotFoundException if the process definition could not be found
   * @throws ServiceUnavailableException if the process could not be deleted
   */
  void deleteProcessDefinition(String processDefinitionId)
      throws InvalidArgumentException, ProcessDefinitionNotFoundException, ServiceUnavailableException;

  /**
   * Delete the process definition version.
   *
   * @param processDefinitionId the ID for the process definition
   * @param processDefinitionVersion the version for the process definition
   * @throws InvalidArgumentException if an argument is invalid
   * @throws ProcessDefinitionVersionNotFoundException if the process definition version could not be
   *     found
   * @throws ServiceUnavailableException if the process definition version could not be deleted
   */
  void deleteProcessDefinitionVersion(String processDefinitionId, int processDefinitionVersion)
      throws InvalidArgumentException,
      ProcessDefinitionVersionNotFoundException,
      ServiceUnavailableException;

  /**
   * Retrieve the process.
   *
   * @param processId the ID for the process
   * @return the process
   * @throws InvalidArgumentException if an argument is invalid
   * @throws ProcessNotFoundException if the process could not be found
   * @throws ServiceUnavailableException if the process could not be retrieved
   */
  Process getProcess(UUID processId)
      throws InvalidArgumentException, ProcessNotFoundException, ServiceUnavailableException;

  /**
   * Retrieve the latest version of the process definition.
   *
   * @param processDefinitionId the ID for the process definition
   * @return the latest version of the process definition
   * @throws InvalidArgumentException if an argument is invalid
   * @throws ProcessDefinitionNotFoundException if the process definition could not be found
   * @throws ServiceUnavailableException if the latest version of the process definition could not be
   *     retrieved
   */
  ProcessDefinition getProcessDefinition(String processDefinitionId)
      throws InvalidArgumentException, ProcessDefinitionNotFoundException, ServiceUnavailableException;

  /**
   * Retrieve the process definition version.
   *
   * @param processDefinitionId the ID for the process definition
   * @param processDefinitionVersion the version of the process definition
   * @return the latest process definition version
   * @throws InvalidArgumentException if an argument is invalid
   * @throws ProcessDefinitionVersionNotFoundException if the process definition could not be found
   * @throws ServiceUnavailableException if the latest process definition version could not be
   *     retrieved
   */
  ProcessDefinition getProcessDefinitionVersion(String processDefinitionId, int processDefinitionVersion)
      throws InvalidArgumentException,
      ProcessDefinitionVersionNotFoundException,
      ServiceUnavailableException;

  /**
   * Update the process.
   *
   * @param updateProcessRequest the request to update the process
   * @param updatedBy the person or system that updated the process
   * @return the updated process
   * @throws InvalidArgumentException if an argument is invalid
   * @throws ProcessNotFoundException if the process could not be found
   * @throws ServiceUnavailableException if the process could not be created
   */
  Process updateProcess(UpdateProcessRequest updateProcessRequest, String updatedBy)
      throws InvalidArgumentException, ProcessNotFoundException, ServiceUnavailableException;

  /**
   * Update the process definition.
   *
   * @param processDefinition the process definition
   * @throws InvalidArgumentException if an argument is invalid
   * @throws ProcessDefinitionNotFoundException if the process definition could not be found
   * @throws ServiceUnavailableException if the process definition could not be updated
   */
  void updateProcessDefinition(ProcessDefinition processDefinition)
      throws InvalidArgumentException, ProcessDefinitionNotFoundException, ServiceUnavailableException;
}
