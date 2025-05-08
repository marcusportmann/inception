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

package digital.inception.operations.ws;

import digital.inception.core.service.InvalidArgumentException;
import digital.inception.core.service.ServiceUnavailableException;
import digital.inception.core.sorting.SortDirection;
import digital.inception.operations.model.BatchTasksNotFoundException;
import digital.inception.operations.model.DuplicateTaskTypeException;
import digital.inception.operations.model.InvalidTaskStatusException;
import digital.inception.operations.model.QueueTaskRequest;
import digital.inception.operations.model.Task;
import digital.inception.operations.model.TaskEvent;
import digital.inception.operations.model.TaskNotFoundException;
import digital.inception.operations.model.TaskSortBy;
import digital.inception.operations.model.TaskStatus;
import digital.inception.operations.model.TaskSummaries;
import digital.inception.operations.model.TaskType;
import digital.inception.operations.model.TaskTypeNotFoundException;
import digital.inception.operations.service.IOperationsService;
import jakarta.jws.WebMethod;
import jakarta.jws.WebParam;
import jakarta.jws.WebResult;
import jakarta.jws.WebService;
import jakarta.jws.soap.SOAPBinding;
import jakarta.xml.bind.annotation.XmlElement;
import java.util.List;
import java.util.UUID;

/**
 * The {@code OperationsWebService} class.
 *
 * @author Marcus Portmann
 */
@WebService(
    serviceName = "OperationsService",
    name = "IOperationsService",
    targetNamespace = "https://inception.digital/operations")
@SOAPBinding
@SuppressWarnings({"unused", "ValidExternallyBoundObject"})
public class OperationsWebService extends AbstractWebServiceBase {

  /** The Operations Service. */
  private final IOperationsService operationsService;

  /**
   * Creates a new {@code OperationsWebService} instance.
   *
   * @param applicationContext the Spring application context
   * @param operationsService the Operations Service
   */
  public OperationsWebService(ApplicationContext applicationContext,  operationsService) {
    super(applicationContext);

    this.operationsService = operationsService;
  }
}
