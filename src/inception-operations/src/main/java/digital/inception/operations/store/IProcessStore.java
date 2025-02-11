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

package digital.inception.operations.store;

import digital.inception.core.service.ServiceUnavailableException;
import digital.inception.core.sorting.SortDirection;
import digital.inception.operations.model.DuplicateProcessException;
import digital.inception.operations.model.Process;
import digital.inception.operations.model.ProcessDefinitionVersionNotFoundException;
import digital.inception.operations.model.ProcessNotFoundException;
import digital.inception.operations.model.ProcessSortBy;
import digital.inception.operations.model.ProcessStatus;
import digital.inception.operations.model.ProcessSummaries;
import java.util.UUID;

/**
 * The <b>IProcessStore</b> interface defines the functionality provided by a process store, which
 * manages the persistence of processes.
 *
 * @author Marcus Portmann
 */
public interface IProcessStore {

  /**
   * Create the new process.
   *
   * @param process the process
   * @return the process
   * @throws ProcessDefinitionVersionNotFoundException if the process definition version for the
   *     process could not be found
   * @throws DuplicateProcessException if the process already exists
   * @throws ServiceUnavailableException if the process could not be created
   */
  Process createProcess(Process process)
      throws ProcessDefinitionVersionNotFoundException,
          DuplicateProcessException,
          ServiceUnavailableException;

  /**
   * Delete the process.
   *
   * @param processId the ID for the process
   * @throws ProcessNotFoundException if the process could not be found
   * @throws ServiceUnavailableException if the process could not be deleted
   */
  void deleteProcess(UUID processId) throws ProcessNotFoundException, ServiceUnavailableException;

  /**
   * Retrieve the process.
   *
   * @param processId the ID for the process
   * @return the process
   * @throws ProcessNotFoundException if the process could not be found
   * @throws ServiceUnavailableException if the process could not be retrieved
   */
  Process getProcess(UUID processId) throws ProcessNotFoundException, ServiceUnavailableException;

  /**
   * Retrieve the summaries for the processes.
   *
   * @param status the status filter to apply to the process summaries
   * @param filter the filter to apply to the process summaries
   * @param sortBy the method used to sort the process summaries e.g. by definition ID
   * @param sortDirection the sort direction to apply to the process summaries
   * @param pageIndex the page index
   * @param pageSize the page size
   * @param maxResults the maximum number of process summaries that should be retrieved
   * @return the summaries for the processes
   * @throws ServiceUnavailableException if the process summaries could not be retrieved
   */
  ProcessSummaries getProcessSummaries(
      ProcessStatus status,
      String filter,
      ProcessSortBy sortBy,
      SortDirection sortDirection,
      Integer pageIndex,
      Integer pageSize,
      int maxResults)
      throws ServiceUnavailableException;

  /**
   * Update the process.
   *
   * @param process the process
   * @return the process
   * @throws ProcessNotFoundException if the process could not be found
   * @throws ServiceUnavailableException if the process could not be updated
   */
  Process updateProcess(Process process)
      throws ProcessNotFoundException, ServiceUnavailableException;
}
